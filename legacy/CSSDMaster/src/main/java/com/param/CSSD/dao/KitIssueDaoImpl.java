package com.param.CSSD.dao;

import java.util.Date;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.CSSD.dto.IssuedKitTransitionMapperDto;
import com.param.CSSD.dto.KitIndentDto;
import com.param.CSSD.dto.KitIndentKitMapperDto;
import com.param.CSSD.dto.KitIssueDto;
import com.param.CSSD.dto.KitIssueKitMapperDto;
import com.param.CSSD.dto.PreSterilizationProcessInstrumentDto;
import com.param.CSSD.dto.SterilizationRequestDto;
import com.param.CSSD.dto.SterilizationRequestTransitionMapperDto;
import com.param.CSSD.model.IssuedKitTransitionMapper;
import com.param.CSSD.model.KitIndentKitMapper;
import com.param.CSSD.model.KitIssue;
import com.param.CSSD.model.KitIssueKitMapper;
import com.param.CSSD.model.KitMovementRecord;
import com.param.CSSD.model.LoadBatch;
import com.param.CSSD.model.PreSterilizationProcess;
import com.param.CSSDMaster.dto.KitInstrumentMapperDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class KitIssueDaoImpl extends GenericDao<KitIssue, Integer>
		implements IKitIssueDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(KitIssueDaoImpl.class);


	public KitIssueDaoImpl() {
		super(KitIssue.class);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unused")
	@Autowired
	private DozerBeanMapper mapper;

	@SuppressWarnings("unused")
	private IKitIndentDao iKitIndentDao;

	@Autowired
	private SessionService sessionService;
	
	@Override
	public Response createKitIssue(KitIssueDto kitIssueDto ) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			KitIssue kitIssue = new KitIssue();
			kitIssue.setCarrier(kitIssueDto.getCarrier());
			kitIssue.setCreatedBy(kitIssueDto.getCreatedBy());
			kitIssue.setCreatedDate(TimeZoneComponent.getDateByzone(kitIssueDto.getUnitId()));
			kitIssue.setFromStoreId(kitIssueDto.getFromStoreId());
			kitIssue.setRemark(kitIssueDto.getRemark());
			kitIssue.setToStoreId(kitIssueDto.getToStoreId());
			kitIssue.setUpdatedBy(kitIssueDto.getUpdatedBy());
			kitIssue.setOrganizationId(kitIssueDto.getOrganizationId());
			kitIssue.setUnitId(kitIssueDto.getUnitId());
			kitIssue.setUpdatedDate(TimeZoneComponent.getDateByzone(kitIssueDto.getUnitId()));
			kitIssue.setWithIndent(kitIssueDto.getWithIndent());
			
			if(kitIssueDto.getWithIndent() == 1)
			{
				kitIssue.setIndentNo(kitIssueDto.getIndentNo());
			}
			else if(kitIssueDto.getWithIndent() == 3)
			{
				kitIssue.setRequestNo(kitIssueDto.getRequestNo());
			}
			kitIssue.setStatus("New");
		
			Integer issueNo=(Integer)session.save(kitIssue);
			
			
			List<KitIssueKitMapperDto> kikmList=kitIssueDto.getKitList();
			for (KitIssueKitMapperDto kitIssueKitMapperDto : kikmList) {
				KitIssueKitMapper mapper=new KitIssueKitMapper();
				mapper.setIssueNo(issueNo);
				mapper.setKitId(kitIssueKitMapperDto.getKitId());
				mapper.setQuantity(kitIssueKitMapperDto.getQuantity());
				session.save(mapper);
				
				List<IssuedKitTransitionMapperDto> transitionList=kitIssueKitMapperDto.getSubKitList();
				if(transitionList != null && !transitionList.isEmpty() )
				{
					for (IssuedKitTransitionMapperDto issuedKitTransitionMapperDto : transitionList) {
						IssuedKitTransitionMapper transitionMapper = new IssuedKitTransitionMapper();
						transitionMapper.setIssueNo(issueNo);
						transitionMapper.setKitId(kitIssueKitMapperDto.getKitId());
						transitionMapper.setTransitionNumber(issuedKitTransitionMapperDto.getTransitionNumber());
						transitionMapper.setIsKitUsed('N');
						session.save(transitionMapper);
						
						/***********************************Start For Kit Movement Record**********************************/
						
						KitMovementRecord kitMovementRecord = new KitMovementRecord();
						kitMovementRecord.setTransitionNumber(issuedKitTransitionMapperDto.getTransitionNumber());
						kitMovementRecord.setFromStoreId(kitIssueDto.getFromStoreId());
						kitMovementRecord.setToStoreId(kitIssueDto.getToStoreId());
						kitMovementRecord.setMovedBy(kitIssueDto.getCreatedBy());
						kitMovementRecord.setMovementDate(TimeZoneComponent.getDateByzone(kitIssueDto.getUnitId()));
						
						kitMovementRecord.setWithIndent(kitIssueDto.getWithIndent());
						if(kitIssueDto.getWithIndent() == 1 && kitIssueDto.getIndentNo() != null) {
							kitMovementRecord.setRequestNo(kitIssueDto.getIndentNo());
							kitMovementRecord.setMovementType("Issue Against Indent");
						}
						else if(kitIssueDto.getWithIndent() == 2 && kitIssueDto.getRequestNo() != null) {
							kitMovementRecord.setRequestNo(kitIssueDto.getRequestNo());
							kitMovementRecord.setMovementType("Issue Against Sterilization Request");
						}else {
							kitMovementRecord.setMovementType("Issue Without Indent");
						}
						
						kitMovementRecord.setOrganizationId(kitIssueDto.getOrganizationId());
						kitMovementRecord.setUnitId(kitIssueDto.getUnitId());
						
						session.save(kitMovementRecord);
						
						/***********************************End For Kit Movement Record**********************************/
						
						
						PreSterilizationProcess preSterilizationProcess = (PreSterilizationProcess)session
								.get(PreSterilizationProcess.class, issuedKitTransitionMapperDto.getTransitionNumber());
						preSterilizationProcess.setIsIssued(true);
						session.update(preSterilizationProcess);
						
					}
				}
			}
			
			if(kitIssueDto.getWithIndent() == 1 && kitIssueDto.getIndentNo() != null)
			{
				for (KitIssueKitMapperDto kitIssueKitMapperDto : kitIssueDto.getKitList()) {
					
					KitIndentKitMapper kitIndentKitMapper = (KitIndentKitMapper)session.get(KitIndentKitMapper.class, kitIssueKitMapperDto.getId());
					if(kitIndentKitMapper.getQuantity() > kitIssueKitMapperDto.getQuantity())
					{
						kitIndentKitMapper.setIssueStatus("Partially Issued");
						kitIndentKitMapper.setIssuedQuantity(kitIssueKitMapperDto.getQuantity());
					}
					else
					{
						kitIndentKitMapper.setIssueStatus("Issued");
						kitIndentKitMapper.setIssuedQuantity(kitIssueKitMapperDto.getQuantity());
					}
					
					session.update(kitIndentKitMapper);
					
				}
				
				Long totalKitCount = (Long)session
						.createQuery("SELECT count(*) FROM KitIndentKitMapper mapper WHERE mapper.indentNo=:indentNo and mapper.status='Accepted'")
						.setParameter("indentNo", kitIssueDto.getIndentNo())
						.uniqueResult();
				
				Long totalIssuedKitCount = (Long)session
						.createQuery("SELECT count(*) FROM KitIndentKitMapper mapper WHERE mapper.indentNo=:indentNo and mapper.status='Accepted' "
								+ "and mapper.issueStatus='Issued'")
						.setParameter("indentNo", kitIssueDto.getIndentNo())
						.uniqueResult();
				
				Long totalPartiallyIssuedKitCount = (Long)session
						.createQuery("SELECT count(*) FROM KitIndentKitMapper mapper WHERE mapper.indentNo=:indentNo and mapper.status='Accepted' "
								+ "and mapper.issueStatus='Partially Issued'")
						.setParameter("indentNo", kitIssueDto.getIndentNo())
						.uniqueResult();
				
				if(totalIssuedKitCount == totalKitCount )
				{
					Query query=session.createQuery("UPDATE KitIndent set status='Issued' WHERE indentNo=:indentNo")
							.setParameter("indentNo", kitIssueDto.getIndentNo());
					@SuppressWarnings("unused")
					int result=query.executeUpdate();
				}
				if( totalPartiallyIssuedKitCount > 0 )
				{
					Query query=session.createQuery("UPDATE KitIndent set status='Partially Issued' WHERE indentNo=:indentNo")
							.setParameter("indentNo", kitIssueDto.getIndentNo());
					@SuppressWarnings("unused")
					int result=query.executeUpdate();
				}
								
			}
			else if(kitIssueDto.getWithIndent() == 3 && kitIssueDto.getRequestNo() != null)
			{
				Query query=session.createQuery("UPDATE SterilizationRequest set status='Closed' WHERE requestNo=:requestNo")
						.setParameter("requestNo", kitIssueDto.getRequestNo());
				@SuppressWarnings("unused")
				int result=query.executeUpdate();
				
			}

			return new Response(SUCCESS, "200","Kit issued Successfully.", null, null);
			
		} catch (Exception e) {
			logger.error("createKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response getKitIssueByMultipleFields(Date fromDate,Date toDate,Integer fromStore,Integer toStore,Integer issueNo,String status,Integer organizationId,Integer unitId) {
		try {
			
			StringBuilder condition=new StringBuilder();
			
			if(fromStore!=null)
			{
				condition.append(" AND kitIssue.fromStoreId=:fromStore ");
			}
			if(toStore!=null)
			{
				condition.append(" AND kitIssue.toStoreId=:toStore ");
			}
			if(issueNo!=null)
			{
				condition.append(" AND kitIssue.issueNo=:issueNo ");
			}
			if(status!=null)
			{
				condition.append(" AND kitIssue.status=:status ");
			}
			
			String query = "SELECT kitIssue.issueNo as issueNo, "
					+ "to_char(kitIssue.createdDate,'dd/MM/yyyy') as createdDate, "
					+ "kitIssue.indentNo as indentNo, "
					+ "kitIssue.requestNo as requestNo, "
					+ "kitIssue.status as status,fromStore.storeName as fromStore,"
					+ "kitIssue.fromStoreId as fromStoreId,kitIssue.toStoreId as toStoreId,"
					+ "toStore.storeName as toStore,kitIssue.remark as remark,kitIssue.cancelReasonId as cancelReasonId,"
					+ "kitIssue.cancelReasonNote as cancelReasonNote,kitIssue.withIndent as withIndent FROM KitIssue kitIssue "
					+ "INNER JOIN kitIssue.fromStore as fromStore "
					+ "INNER JOIN kitIssue.toStore as toStore "
					+ "WHERE kitIssue.createdDate BETWEEN :fromDate AND :toDate "
					+ "AND kitIssue.organizationId=:organizationId AND kitIssue.unitId=:unitId "+condition;
			
			StatelessSession statelessSession = sessionFactory.openStatelessSession();
			Query createQuery= statelessSession
						.createQuery(query)
						.setParameter("fromDate", fromDate)
						.setParameter("toDate", toDate)
						.setParameter("organizationId", organizationId)
						.setParameter("unitId", unitId);


			if(fromStore!=null)
			{
				createQuery.setParameter("fromStore",fromStore);
			}
			if(toStore!=null)
			{
				createQuery.setParameter("toStore", toStore);
			}
			if(issueNo!=null)
			{
				createQuery.setParameter("indentNo", issueNo);
			}
			if(status!=null)
			{
				createQuery.setParameter("status", status);
			}

			List<KitIssueDto> kitIssueDtoList  =createQuery.setResultTransformer(Transformers.aliasToBean(KitIssueDto.class)).list();
			
			if(!kitIssueDtoList.isEmpty())
			{
				for (KitIssueDto kitIssueDto : kitIssueDtoList) {
					
					if(kitIssueDto.getIndentNo() != null)
					{
						kitIssueDto.setIndentDate((String)statelessSession
								.createQuery( "select to_char(kitIndent.indentDate,'dd/MM/yyyy') from KitIndent kitIndent where kitIndent.indentNo=:indentNo")
								.setParameter("indentNo", kitIssueDto.getIndentNo())
								.uniqueResult());
					}
					if(kitIssueDto.getRequestNo() != null)
					{
						kitIssueDto.setRequestDate((String)statelessSession
								.createQuery( "select to_char(sterReq.createdDate,'dd/MM/yyyy') from SterilizationRequest sterReq where sterReq.requestNo=:requestNo")
								.setParameter("requestNo", kitIssueDto.getRequestNo())
								.uniqueResult());
					}
								
					List<KitIssueKitMapperDto> kitIssueKitMapperDtoList = statelessSession
							.getNamedQuery("GET_KIT_LIST_BY_ISSUE_NO")
							.setParameter("issueNo", kitIssueDto.getIssueNo())
							.setResultTransformer(Transformers.aliasToBean(KitIssueKitMapperDto.class)).list();
					
					if(!kitIssueKitMapperDtoList.isEmpty())
					{
						for (KitIssueKitMapperDto kitIssueKitMapperDto : kitIssueKitMapperDtoList) {
							List<KitInstrumentMapperDto> kitInstrumentList = statelessSession
									.getNamedQuery("GET_INSTRUMENT_LIST_BY_KIT_ID")
									.setParameter("kitId", kitIssueKitMapperDto.getKitId())
									.setResultTransformer(Transformers.aliasToBean(KitInstrumentMapperDto.class)).list();
							kitIssueKitMapperDto.setKitInstrumentList(kitInstrumentList);
							
							List<IssuedKitTransitionMapperDto> subKitList = statelessSession
									.createQuery("SELECT loadBatch.batchNumber as batchNumber, to_char(loadBatch.autoclaveDate,'dd/MM/yyyy') as autoclaveDate,"
											+ "loadBatch.autoclaveNumber as autoclaveNumber, preSterilizationProcess.transitionNumber as transitionNumber,"
											+ "to_char(preSterilizationProcess.expiryDate,'dd/MM/yyyy') as expiryDate, "
											+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as kitCode, " 
											+ "kitMaster.name as kitName, preSterilizationProcess.isAccepted as isAccepted, "
											+ "preSterilizationProcess.isRejected as isRejected "
											+ "FROM LoadBatchKitMapper mapper INNER JOIN mapper.loadBatch as loadBatch "
											+ "INNER JOIN mapper.preSterilizationProcess as preSterilizationProcess "
											+ "INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
											+ "WHERE  "
											+ "preSterilizationProcess.transitionNumber IN (select transitionMapper.transitionNumber from IssuedKitTransitionMapper transitionMapper "
											+ "where transitionMapper.kitId=:kitId and transitionMapper.issueNo=:issueNo ) "
											+ "order by preSterilizationProcess.expiryDate ")
									.setParameter("kitId", kitIssueKitMapperDto.getKitId())
									.setParameter("issueNo",kitIssueDto.getIssueNo())
									.setResultTransformer(Transformers.aliasToBean(IssuedKitTransitionMapperDto.class)).list();
							
							//get list of kits that are sterilized from cssd opening stock
							List<IssuedKitTransitionMapperDto> subKitList1 = statelessSession
									.createQuery("SELECT  "
											+ " preSterilizationProcess.transitionNumber as transitionNumber,"
											+ "to_char(preSterilizationProcess.expiryDate,'dd/MM/yyyy') as expiryDate, "
											+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as kitCode, " 
											+ "kitMaster.name as kitName, preSterilizationProcess.isAccepted as isAccepted, "
											+ "preSterilizationProcess.isRejected as isRejected "
											+ "FROM PreSterilizationProcess preSterilizationProcess  "
											//+ "INNER JOIN mapper.preSterilizationProcess as preSterilizationProcess "
											+ "INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
											+ "WHERE preSterilizationProcess.isSterilizeFromOpeningStock='Y' and "
											+ "preSterilizationProcess.transitionNumber IN (select transitionMapper.transitionNumber from IssuedKitTransitionMapper transitionMapper "
											+ "where transitionMapper.kitId=:kitId and transitionMapper.issueNo=:issueNo ) "
											+ "order by preSterilizationProcess.expiryDate ")
									.setParameter("kitId", kitIssueKitMapperDto.getKitId())
									.setParameter("issueNo",kitIssueDto.getIssueNo())
									.setResultTransformer(Transformers.aliasToBean(IssuedKitTransitionMapperDto.class)).list();
							
							if(!subKitList1.isEmpty())
								subKitList.addAll(subKitList1);
							Integer issuedQuantity = subKitList.size(); Integer acceptedQuantity = 0; Integer rejectedQuantity = 0;
							
							for (IssuedKitTransitionMapperDto issuedKitTransitionMapperDto : subKitList) {
								if(issuedKitTransitionMapperDto.getIsAccepted())
								{
									acceptedQuantity++;	
								}
								if(issuedKitTransitionMapperDto.getIsRejected())
								{
									rejectedQuantity++;
								}
							}
							
							kitIssueKitMapperDto.setIssuedQuantity(issuedQuantity);
							kitIssueKitMapperDto.setAcceptedQuantity(acceptedQuantity);
							kitIssueKitMapperDto.setRejectedQuantity(rejectedQuantity);
							
							kitIssueKitMapperDto.setSubKitList(subKitList);
						}
					}
					
					kitIssueDto.setKitList(kitIssueKitMapperDtoList);
				}
			}
			statelessSession.close();
			return new Response(SUCCESS, null, null, kitIssueDtoList, null);
		} catch (Exception e) {
			logger.error("getKitIssueByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	@Override
	public Response getKitIssueByMultipleFieldsForAcceptance(Date fromDate,Date toDate,Integer fromStore,Integer toStore,Integer issueNo,String status,Integer organizationId,Integer unitId) {
		try {
			
			StringBuilder condition=new StringBuilder();
			
			if(fromStore!=null)
			{
				condition.append(" AND kitIssue.fromStoreId=:fromStore ");
			}
			if(toStore!=null)
			{
				condition.append(" AND kitIssue.toStoreId=:toStore ");
			}
			if(issueNo!=null)
			{
				condition.append(" AND kitIssue.issueNo=:issueNo ");
			}
			if(status!=null)
			{
				condition.append(" AND kitIssue.status=:status ");
			}
			//get user storelist by userId
			  List<Integer> userStoreList=sessionFactory.getCurrentSession().createQuery(" SELECT userStore.store.storeId as storeId  FROM UserStoreMapper userStore "
					                           + " where userStore.userId=:userId")
			                                  .setParameter("userId", sessionService.getUserId()).list();
			  //System.out.println("Current user Id:"+sessionService.getUserId());
			  if(userStoreList.isEmpty()) {
				  userStoreList.add(0);
			  }
			  if(toStore==null && fromStore==null)
				  condition.append(" AND kitIssue.toStoreId in(:userStoreListParam)");
			  
			
			String query = "SELECT kitIssue.issueNo as issueNo, "
					+ "to_char(kitIssue.createdDate,'dd/MM/yyyy') as createdDate, "
					+ "kitIssue.indentNo as indentNo, "
					+ "kitIssue.requestNo as requestNo, "
					+ "kitIssue.status as status,fromStore.storeName as fromStore,"
					+ "kitIssue.fromStoreId as fromStoreId,kitIssue.toStoreId as toStoreId,"
					+ "toStore.storeName as toStore,kitIssue.remark as remark,kitIssue.cancelReasonId as cancelReasonId,"
					+ "kitIssue.cancelReasonNote as cancelReasonNote,kitIssue.withIndent as withIndent FROM KitIssue kitIssue "
					+ "INNER JOIN kitIssue.fromStore as fromStore "
					+ "INNER JOIN kitIssue.toStore as toStore "
					+ "WHERE kitIssue.createdDate BETWEEN :fromDate AND :toDate "
					+ "AND kitIssue.organizationId=:organizationId AND kitIssue.unitId=:unitId AND kitIssue.status != 'New' "+condition;
		
			StatelessSession statelessSession = sessionFactory.openStatelessSession();
			List<KitIssueDto> kitIssueDtoList;
			if(toStore==null && fromStore==null) {
		       Query createQuery  = statelessSession
						.createQuery(query)
						.setParameter("fromDate", fromDate)
						.setParameter("toDate", toDate)
						.setParameter("organizationId", organizationId)
						.setParameter("unitId", unitId)
						.setParameterList("userStoreListParam", userStoreList);


				if(fromStore!=null)
				{
					createQuery.setParameter("fromStore", fromStore);
				}
				if(toStore!=null)
				{
					createQuery.setParameter("toStore", toStore);
				}
				if(issueNo!=null)
				{
					createQuery.setParameter("indentNo", issueNo);
				}
				if(status!=null)
				{
					createQuery.setParameter("status", status);
				}


				kitIssueDtoList  =createQuery.setResultTransformer(Transformers.aliasToBean(KitIssueDto.class)).list();
			}else {
				Query createQuery  = statelessSession
						.createQuery(query)
						.setParameter("fromDate", fromDate)
						.setParameter("toDate", toDate)
						.setParameter("organizationId", organizationId)
						.setParameter("unitId", unitId);

				if(fromStore!=null)
				{
					createQuery.setParameter("fromStore",fromStore);
				}
				if(toStore!=null)
				{
					createQuery.setParameter("toStore", toStore);
				}
				if(issueNo!=null)
				{
					createQuery.setParameter("indentNo", issueNo);
				}
				if(status!=null)
				{
					createQuery.setParameter("status", status);
				}

				kitIssueDtoList=createQuery.setResultTransformer(Transformers.aliasToBean(KitIssueDto.class)).list();
			}
			
			if(!kitIssueDtoList.isEmpty())
			{
				for (KitIssueDto kitIssueDto : kitIssueDtoList) {
					
					if(kitIssueDto.getIndentNo() != null)
					{
						kitIssueDto.setIndentDate((String)statelessSession
								.createQuery( "select to_char(kitIndent.indentDate,'dd/MM/yyyy') from KitIndent kitIndent where kitIndent.indentNo=:indentNo")
								.setParameter("indentNo", kitIssueDto.getIndentNo())
								.uniqueResult());
					}
					if(kitIssueDto.getRequestNo() != null)
					{
						kitIssueDto.setRequestDate((String)statelessSession
								.createQuery( "select to_char(sterReq.createdDate,'dd/MM/yyyy') from SterilizationRequest sterReq where sterReq.requestNo=:requestNo")
								.setParameter("requestNo", kitIssueDto.getRequestNo())
								.uniqueResult());
					}
								
					List<KitIssueKitMapperDto> kitIssueKitMapperDtoList = statelessSession
							.getNamedQuery("GET_KIT_LIST_BY_ISSUE_NO")
							.setParameter("issueNo", kitIssueDto.getIssueNo())
							.setResultTransformer(Transformers.aliasToBean(KitIssueKitMapperDto.class)).list();
					
					if(!kitIssueKitMapperDtoList.isEmpty())
					{
						for (KitIssueKitMapperDto kitIssueKitMapperDto : kitIssueKitMapperDtoList) {
							List<KitInstrumentMapperDto> kitInstrumentList = statelessSession
									.getNamedQuery("GET_INSTRUMENT_LIST_BY_KIT_ID")
									.setParameter("kitId", kitIssueKitMapperDto.getKitId())
									.setResultTransformer(Transformers.aliasToBean(KitInstrumentMapperDto.class)).list();
							kitIssueKitMapperDto.setKitInstrumentList(kitInstrumentList);
							
							List<IssuedKitTransitionMapperDto> subKitList = statelessSession
									.createQuery("SELECT loadBatch.batchNumber as batchNumber, to_char(loadBatch.autoclaveDate,'dd/MM/yyyy') as autoclaveDate,"
											+ "loadBatch.autoclaveNumber as autoclaveNumber, preSterilizationProcess.transitionNumber as transitionNumber,"
											+ "to_char(preSterilizationProcess.expiryDate,'dd/MM/yyyy') as expiryDate, "
											+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as kitCode, " 
											+ "kitMaster.name as kitName, preSterilizationProcess.isAccepted as isAccepted, "
											+ "preSterilizationProcess.isRejected as isRejected "
											+ "FROM LoadBatchKitMapper mapper INNER JOIN mapper.loadBatch as loadBatch "
											+ "INNER JOIN mapper.preSterilizationProcess as preSterilizationProcess "
											+ "INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
											+ "WHERE "
											+ "preSterilizationProcess.transitionNumber IN (select transitionMapper.transitionNumber from IssuedKitTransitionMapper transitionMapper "
											+ "where transitionMapper.kitId=:kitId and transitionMapper.issueNo=:issueNo ) "
											+ "order by preSterilizationProcess.expiryDate ")
									.setParameter("kitId", kitIssueKitMapperDto.getKitId())
									.setParameter("issueNo",kitIssueDto.getIssueNo())
									.setResultTransformer(Transformers.aliasToBean(IssuedKitTransitionMapperDto.class)).list();
							
							//get list of kits that are sterilized from cssd opening stock
							List<IssuedKitTransitionMapperDto> subKitList1 = statelessSession
									.createQuery("SELECT  "
											+ " preSterilizationProcess.transitionNumber as transitionNumber,"
											+ "to_char(preSterilizationProcess.expiryDate,'dd/MM/yyyy') as expiryDate, "
											+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as kitCode, " 
											+ "kitMaster.name as kitName, preSterilizationProcess.isAccepted as isAccepted, "
											+ "preSterilizationProcess.isRejected as isRejected "
											+ "FROM PreSterilizationProcess preSterilizationProcess  "
											//+ "INNER JOIN mapper.preSterilizationProcess as preSterilizationProcess "
											+ "INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
											+ "WHERE preSterilizationProcess.isSterilizeFromOpeningStock='Y' and "
											+ "preSterilizationProcess.transitionNumber IN (select transitionMapper.transitionNumber from IssuedKitTransitionMapper transitionMapper "
											+ "where transitionMapper.kitId=:kitId and transitionMapper.issueNo=:issueNo ) "
											+ "order by preSterilizationProcess.expiryDate ")
									.setParameter("kitId", kitIssueKitMapperDto.getKitId())
									.setParameter("issueNo",kitIssueDto.getIssueNo())
									.setResultTransformer(Transformers.aliasToBean(IssuedKitTransitionMapperDto.class)).list();
							
							if(!subKitList1.isEmpty())
								subKitList.addAll(subKitList1);
							
							Integer issuedQuantity = subKitList.size(); Integer acceptedQuantity = 0; Integer rejectedQuantity = 0;
							
							for (IssuedKitTransitionMapperDto issuedKitTransitionMapperDto : subKitList) {
								if(issuedKitTransitionMapperDto.getIsAccepted())
								{
									acceptedQuantity++;	
								}
								if(issuedKitTransitionMapperDto.getIsRejected())
								{
									rejectedQuantity++;
								}
							}
							
							kitIssueKitMapperDto.setIssuedQuantity(issuedQuantity);
							kitIssueKitMapperDto.setAcceptedQuantity(acceptedQuantity);
							kitIssueKitMapperDto.setRejectedQuantity(rejectedQuantity);
							
							kitIssueKitMapperDto.setSubKitList(subKitList);
						}
					}
					
					kitIssueDto.setKitList(kitIssueKitMapperDtoList);
				}
			}
			statelessSession.close();
			return new Response(SUCCESS, null, null, kitIssueDtoList, null);
		} catch (Exception e) {
			logger.error("getKitIssueByMultipleFieldsForAcceptance failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response getKitIssueByIssueNo(Integer issueNo,Integer organizationId,Integer unitId) {
		try {
			
			String query = "SELECT kitIssue.issueNo as issueNo, "
					+ "to_char(kitIssue.createdDate,'dd/MM/yyyy') as createdDate, "
					+ "kitIssue.indentNo as indentNo,"
					+ "kitIssue.requestNo as requestNo, "
					+ "kitIssue.status as status,fromStore.storeName as fromStore,"
					+ "kitIssue.fromStoreId as fromStoreId,kitIssue.toStoreId as toStoreId,"
					+ "toStore.storeName as toStore,kitIssue.remark as remark,kitIssue.cancelReasonId as cancelReasonId,"
					+ "kitIssue.cancelReasonNote as cancelReasonNote,kitIssue.withIndent as withIndent FROM KitIssue kitIssue "
					+ "INNER JOIN kitIssue.fromStore as fromStore "
					+ "INNER JOIN kitIssue.toStore as toStore "
					+ "WHERE kitIssue.issueNo=:issueNo "
					+ "AND kitIssue.organizationId=:organizationId AND kitIssue.unitId=:unitId ";
			
			StatelessSession statelessSession = sessionFactory.openStatelessSession();
			KitIssueDto kitIssueDto  = (KitIssueDto) statelessSession
						.createQuery(query)
						.setParameter("issueNo", issueNo)
						.setParameter("organizationId", organizationId)
						.setParameter("unitId", unitId)
						.setResultTransformer(Transformers.aliasToBean(KitIssueDto.class)).uniqueResult();
			
			
			if( kitIssueDto != null )
			{
	
				if(kitIssueDto.getIndentNo() != null)
				{
					kitIssueDto.setIndentDate((String)statelessSession
							.createQuery( "select to_char(kitIndent.indentDate,'dd/MM/yyyy') from KitIndent kitIndent where kitIndent.indentNo=:indentNo")
							.setParameter("indentNo", kitIssueDto.getIndentNo())
							.uniqueResult());
				}
				if(kitIssueDto.getRequestNo() != null)
				{
					kitIssueDto.setRequestDate((String)statelessSession
							.createQuery( "select to_char(sterReq.createdDate,'dd/MM/yyyy') from SterilizationRequest sterReq where sterReq.requestNo=:requestNo")
							.setParameter("requestNo", kitIssueDto.getRequestNo())
							.uniqueResult());
				}
					List<KitIssueKitMapperDto> kitIssueKitMapperDtoList = statelessSession
							.getNamedQuery("GET_KIT_LIST_BY_ISSUE_NO")
							.setParameter("issueNo", kitIssueDto.getIssueNo())
							.setResultTransformer(Transformers.aliasToBean(KitIssueKitMapperDto.class)).list();
					
					if(!kitIssueKitMapperDtoList.isEmpty())
					{
						for (KitIssueKitMapperDto kitIssueKitMapperDto : kitIssueKitMapperDtoList) {
							List<KitInstrumentMapperDto> kitInstrumentList = statelessSession
									.getNamedQuery("GET_INSTRUMENT_LIST_BY_KIT_ID")
									.setParameter("kitId", kitIssueKitMapperDto.getKitId())
									.setResultTransformer(Transformers.aliasToBean(KitInstrumentMapperDto.class)).list();
							kitIssueKitMapperDto.setKitInstrumentList(kitInstrumentList);
							
							List<IssuedKitTransitionMapperDto> subKitList = statelessSession
									.createQuery("SELECT loadBatch.batchNumber as batchNumber, to_char(loadBatch.autoclaveDate,'dd/MM/yyyy') as autoclaveDate,"
											+ "loadBatch.autoclaveNumber as autoclaveNumber, preSterilizationProcess.transitionNumber as transitionNumber,"
											+ "to_char(preSterilizationProcess.expiryDate,'dd/MM/yyyy') as expiryDate, "
											+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as kitCode, " 
											+ "kitMaster.name as kitName,preSterilizationProcess.isAccepted as isAccepted, "
											+ "preSterilizationProcess.isRejected as isRejected "
											+ "FROM LoadBatchKitMapper mapper INNER JOIN mapper.loadBatch as loadBatch "
											+ "INNER JOIN mapper.preSterilizationProcess as preSterilizationProcess "
											+ "INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
											+ "WHERE  "
											+ "preSterilizationProcess.transitionNumber IN (select transitionMapper.transitionNumber from IssuedKitTransitionMapper transitionMapper "
											+ "where transitionMapper.kitId=:kitId and transitionMapper.issueNo=:issueNo ) "
											+ "order by preSterilizationProcess.expiryDate ")
									.setParameter("kitId", kitIssueKitMapperDto.getKitId())
									.setParameter("issueNo",kitIssueDto.getIssueNo())
									.setResultTransformer(Transformers.aliasToBean(IssuedKitTransitionMapperDto.class)).list();
							
							
							//get list of kits that are sterilized from cssd opening stock
							List<IssuedKitTransitionMapperDto> subKitList1 = statelessSession
									.createQuery("SELECT  "
											+ " preSterilizationProcess.transitionNumber as transitionNumber,"
											+ "to_char(preSterilizationProcess.expiryDate,'dd/MM/yyyy') as expiryDate, "
											+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as kitCode, " 
											+ "kitMaster.name as kitName, preSterilizationProcess.isAccepted as isAccepted, "
											+ "preSterilizationProcess.isRejected as isRejected "
											+ "FROM PreSterilizationProcess preSterilizationProcess  "
											//+ "INNER JOIN mapper.preSterilizationProcess as preSterilizationProcess "
											+ "INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
											+ "WHERE preSterilizationProcess.isSterilizeFromOpeningStock='Y' and "
											+ "preSterilizationProcess.transitionNumber IN (select transitionMapper.transitionNumber from IssuedKitTransitionMapper transitionMapper "
											+ "where transitionMapper.kitId=:kitId and transitionMapper.issueNo=:issueNo ) "
											+ "order by preSterilizationProcess.expiryDate ")
									.setParameter("kitId", kitIssueKitMapperDto.getKitId())
									.setParameter("issueNo",kitIssueDto.getIssueNo())
									.setResultTransformer(Transformers.aliasToBean(IssuedKitTransitionMapperDto.class)).list();
							
							if(!subKitList1.isEmpty())
								subKitList.addAll(subKitList1);
							
							Integer issuedQuantity = subKitList.size(); Integer acceptedQuantity = 0; Integer rejectedQuantity = 0;
							
							for (IssuedKitTransitionMapperDto issuedKitTransitionMapperDto : subKitList) {
								if(issuedKitTransitionMapperDto.getIsAccepted())
								{
									acceptedQuantity++;	
								}
								if(issuedKitTransitionMapperDto.getIsRejected())
								{
									rejectedQuantity++;
								}
							}
							
							kitIssueKitMapperDto.setIssuedQuantity(issuedQuantity);
							kitIssueKitMapperDto.setAcceptedQuantity(acceptedQuantity);
							kitIssueKitMapperDto.setRejectedQuantity(rejectedQuantity);
							
							kitIssueKitMapperDto.setSubKitList(subKitList);
						}
					}
					
					kitIssueDto.setKitList(kitIssueKitMapperDtoList);
				
			}
			statelessSession.close();
			return new Response(SUCCESS, null, null, null,kitIssueDto);
		} catch (Exception e) {
			logger.error("getKitIssueByIssueNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response updateKitIssue(KitIssueDto kitIssueDto ) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			KitIssue kitIssue = (KitIssue)session.get(KitIssue.class, kitIssueDto.getIssueNo());
			
			kitIssue.setCarrier(kitIssueDto.getCarrier());
			kitIssue.setFromStoreId(kitIssueDto.getFromStoreId());
			kitIssue.setRemark(kitIssueDto.getRemark());
			kitIssue.setToStoreId(kitIssueDto.getToStoreId());
			kitIssue.setUpdatedBy(kitIssueDto.getUpdatedBy());
			kitIssue.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
			kitIssue.setWithIndent(kitIssueDto.getWithIndent());
			if(kitIssueDto.getWithIndent() == 1)
			{
				kitIssue.setIndentNo(kitIssueDto.getIndentNo());
			}
			
			session.update(kitIssue);
			
			String issuedKitTransitionMapperDeleteQuery = "delete from IssuedKitTransitionMapper mapper where mapper.issueNo=:issueNo";
			String kitIssueKitMapperDeleteQuery = "delete from KitIssueKitMapper mapper where mapper.issueNo=:issueNo";
			
			session.createQuery(issuedKitTransitionMapperDeleteQuery)
			.setParameter("issueNo", kitIssueDto.getIssueNo())
			.executeUpdate();
			session.createQuery(kitIssueKitMapperDeleteQuery)
			.setParameter("issueNo", kitIssueDto.getIssueNo())
			.executeUpdate();
		
			
			List<KitIssueKitMapperDto> kikmList=kitIssueDto.getKitList();
			if( kikmList != null && !kikmList.isEmpty())
			{
				for (KitIssueKitMapperDto kitIssueKitMapperDto : kikmList) {
					KitIssueKitMapper mapper=new KitIssueKitMapper();
					mapper.setIssueNo(kitIssueDto.getIssueNo());
					mapper.setKitId(kitIssueKitMapperDto.getKitId());
					mapper.setQuantity(kitIssueKitMapperDto.getQuantity());
					session.save(mapper);
					
					List<IssuedKitTransitionMapperDto> transitionList=kitIssueKitMapperDto.getSubKitList();
					if(transitionList != null && !transitionList.isEmpty())
					{
						for (IssuedKitTransitionMapperDto issuedKitTransitionMapperDto : transitionList) {
							IssuedKitTransitionMapper transitionMapper = new IssuedKitTransitionMapper();
							transitionMapper.setIssueNo(kitIssueDto.getIssueNo());
							transitionMapper.setKitId(kitIssueKitMapperDto.getKitId());
							transitionMapper.setTransitionNumber(issuedKitTransitionMapperDto.getTransitionNumber());
							session.save(transitionMapper);
						}
					}
				}
				
			}
			
			
			return new Response(SUCCESS, "200","Kit issue updated Successfully.", null, null);
			
		} catch (Exception e) {
			logger.error("updateKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response updateKitIssueStatus(List<KitIssueDto> kitIssueDtoList ) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			
			for (KitIssueDto kitIssueDto : kitIssueDtoList) {
				
				KitIssue kitIssue = (KitIssue) session.get(KitIssue.class, kitIssueDto.getIssueNo());
				kitIssue.setStatus("Pending");
				session.update(kitIssue);
			}
			
			return new Response(SUCCESS, "200","Kit issues updates successfully.", null, null);
			
		} catch (Exception e) {
			logger.error("updateKitIssueStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response cancelKitIssue(KitIssueDto kitIssueDto ) {
		try {
			
			StatelessSession statelessSession=sessionFactory.openStatelessSession();
			
			KitIssueDto tempkitIssueDto = (KitIssueDto)getKitIssueByIssueNo(kitIssueDto.getIssueNo(),kitIssueDto.getOrganizationId(),kitIssueDto.getUnitId()).getObject();
			
			List<KitIssueKitMapperDto> kitList = tempkitIssueDto.getKitList();
			
			for (KitIssueKitMapperDto kitIssueKitMapperDto : kitList) {
				List<IssuedKitTransitionMapperDto> subKitList = kitIssueKitMapperDto.getSubKitList();
				for (IssuedKitTransitionMapperDto issuedKitTransitionMapperDto : subKitList) {
					
					PreSterilizationProcess preSterilizationProcess = (PreSterilizationProcess)statelessSession.get(PreSterilizationProcess.class,issuedKitTransitionMapperDto.getTransitionNumber());
					preSterilizationProcess.setIsIssued(false);
					statelessSession.update(preSterilizationProcess);
					
				}
			}
			
				KitIssue kitIssue = (KitIssue) statelessSession.get(KitIssue.class, kitIssueDto.getIssueNo());
				kitIssue.setStatus("Cancelled");
				kitIssue.setCancelReasonId(kitIssueDto.getCancelReasonId());
				kitIssue.setCancelReasonNote(kitIssueDto.getCancelReasonNote());
				statelessSession.update(kitIssue);
			
				statelessSession.close();
			return new Response(SUCCESS, "200","Issue cancelled Successfully.", null, null);
			
		} catch (Exception e) {
			logger.error("cancelKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response acceptKitIssue(Integer organizationId,Integer unitId,Integer issueNo,Integer transitionNumber) {
		try {
			StatelessSession statelessSession=sessionFactory.openStatelessSession();
			if(transitionNumber != null)
			{
				
				PreSterilizationProcess preSterilizationProcess = (PreSterilizationProcess)statelessSession.get(PreSterilizationProcess.class,transitionNumber);
				preSterilizationProcess.setIsAccepted(true);
				statelessSession.update(preSterilizationProcess);
				statelessSession.close();
				return new Response(SUCCESS, null, "Kit accepted successfully.", null, null);
			}
			else
			{
				return new Response(ERROR, null, "Something went wrong.", null, null);
			}
			
		} catch (Exception e) {
			logger.error("acceptKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response rejectKitIssue(Integer organizationId,Integer unitId,Integer issueNo,Integer transitionNumber,Integer rejectReasonId,Integer quantity,String rejectionNote) {
		try {
			Session session=sessionFactory.getCurrentSession();
			if(transitionNumber != null)
			{
				PreSterilizationProcess preSterilizationProcess = (PreSterilizationProcess)session.get(PreSterilizationProcess.class,transitionNumber);
				preSterilizationProcess.setIsRejected(true);
				preSterilizationProcess.setRejectReasonId(rejectReasonId);
				preSterilizationProcess.setRejectionNote(rejectionNote);
				session.update(preSterilizationProcess);
				
				return new Response(SUCCESS, null, "Kit rejected successfully.", null, null);
			}
			else
			{
				return new Response(ERROR, null, "Something went wrong.", null, null);
			}
			
			
		} catch (Exception e) {
			logger.error("rejectKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	
	@Override
	public void changeKitIssueStatus(Integer orgId,Integer unitId,Integer issueNo) {
		
		StatelessSession statelessSession=sessionFactory.openStatelessSession();
		
		KitIssueDto kitIssueDto = (KitIssueDto)getKitIssueByIssueNo(issueNo,orgId,unitId).getObject();
		
		Boolean flag = false;
		
		for (KitIssueKitMapperDto kitIssueKitMapperDto : kitIssueDto.getKitList()) {
			
			if (kitIssueKitMapperDto.getIssuedQuantity() != (kitIssueKitMapperDto.getAcceptedQuantity()
					+ kitIssueKitMapperDto.getRejectedQuantity())) {
				flag = true;
				break;

			}
		}
		
		if(flag == false)
		{
			KitIssue kitIssue = (KitIssue)statelessSession.get(KitIssue.class, kitIssueDto.getIssueNo());
			kitIssue.setStatus("Closed");
			statelessSession.update(kitIssue);
		}
		statelessSession.close();
	}
	
	
	
		
	
	@Override
	public Response getKitIndentByMultipleFieldsForKitIssue(Date fromDate,Date toDate,Integer fromStore,Integer toStore,Integer indentNo,Integer organizationId,Integer unitId) {
		try {
			
			StringBuilder condition=new StringBuilder();
			
			if(fromStore!=null)
			{
				condition.append(" AND kitIndent.fromStoreId=:fromStore ");
			}
			if(toStore!=null)
			{
				condition.append(" AND kitIndent.toStoreId=:toStore ");
			}
			if(indentNo!=null)
			{
				condition.append(" AND kitIndent.indentNo=:indentNo ");
			}
						
			String query = "SELECT kitIndent.indentNo as indentNo, "
					+ "to_char(kitIndent.indentDate,'dd-MM-yyyy') as indentDate, "
					+ "kitIndent.status as status,fromStore.storeName as fromStore,"
					+ "toStore.storeName as toStore,userCreatedBy.userName as userName,priority.priorityDesc as priorityDesc FROM KitIndent kitIndent "
					+ "INNER JOIN kitIndent.fromStore as fromStore "
					+ "INNER JOIN kitIndent.toStore as toStore "
					+ "INNER JOIN kitIndent.userCreatedBy as userCreatedBy "
					+ "INNER JOIN kitIndent.priority as priority "
					+ "WHERE kitIndent.indentDate BETWEEN :fromDate AND :toDate "
					+ "AND kitIndent.organizationId=:organizationId AND kitIndent.unitId=:unitId AND "
					+ "(kitIndent.status='Approved' OR kitIndent.status='Partially Issued') "+condition;
			
			StatelessSession statelessSession = sessionFactory.openStatelessSession();
			  Query createQuery= statelessSession
						.createQuery(query)
						.setParameter("fromDate", fromDate)
						.setParameter("toDate", toDate)
						.setParameter("organizationId", organizationId)
						.setParameter("unitId", unitId);

			if(fromStore!=null)
			{
				createQuery.setParameter("fromStore",fromStore);
			}
			if(toStore!=null)
			{
				createQuery.setParameter("toStore",toStore);
			}
			if(indentNo!=null)
			{
				createQuery.setParameter("indentNo",indentNo);
			}

			List<KitIndentDto> kitIndentDtoList=createQuery.setResultTransformer(Transformers.aliasToBean(KitIndentDto.class)).list();

			if(!kitIndentDtoList.isEmpty())
			{
				for (KitIndentDto kitIndentDto : kitIndentDtoList) {
					List<KitIndentKitMapperDto> kitIndentKitMapperDtoList = statelessSession
							.getNamedQuery("GET_KIT_LIST_BY_INDENT_NO_FOR_KIT_ISSUE")
							.setParameter("indentNo", kitIndentDto.getIndentNo())
							.setParameter("organizationId", organizationId)
							.setParameter("unitId", unitId)
							.setResultTransformer(Transformers.aliasToBean(KitIndentKitMapperDto.class)).list();
					
					if(!kitIndentKitMapperDtoList.isEmpty())
					{
						for (KitIndentKitMapperDto kitIndentKitMapperDto : kitIndentKitMapperDtoList) {
							List<KitInstrumentMapperDto> kitInstrumentList = statelessSession
									.getNamedQuery("GET_INSTRUMENT_LIST_BY_KIT_ID")
									.setParameter("kitId", kitIndentKitMapperDto.getKitId())
									.setResultTransformer(Transformers.aliasToBean(KitInstrumentMapperDto.class)).list();
							kitIndentKitMapperDto.setKitInstrumentList(kitInstrumentList);
						}
					}
					
					kitIndentDto.setKitList(kitIndentKitMapperDtoList);
				}
			}
			statelessSession.close();
			return new Response(SUCCESS, null, null, kitIndentDtoList, null);
		} catch (Exception e) {
			logger.error("getKitIndentByMultipleFieldsForKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	

	@Override
	public Response getSterilizationRequestByMultipleFieldsForKitIssue(Date fromDate,Date toDate,Integer requestingStoreId,Integer issueingStoreId,Integer requestNo,Integer organizationId,Integer unitId) {
		try {
			
			StringBuilder condition=new StringBuilder();
			
			if(requestingStoreId!=null)
			{
				condition.append(" AND sterReq.requestingStoreId=:requestingStoreId ");
			}
			if(issueingStoreId!=null)
			{
				condition.append(" AND sterReq.issueingStoreId=:issueingStoreId ");
			}
			if(requestNo!=null)
			{
				condition.append(" AND sterReq.requestNo=:requestNo ");
			}
						
			String query = "SELECT sterReq.requestNo as requestNo, "
					+ "to_char(sterReq.createdDate,'dd-MM-yyyy') as createdDate, "
					+ "sterReq.status as status,requestingStore.storeName as requestingStore,"
					+ "issueingStore.storeName as issueingStore FROM SterilizationRequest sterReq "
					+ "INNER JOIN sterReq.requestingStore as requestingStore "
					+ "INNER JOIN sterReq.issueingStore as issueingStore "
					+ "WHERE sterReq.createdDate BETWEEN :fromDate AND :toDate "
					+ "AND sterReq.organizationId=:organizationId AND sterReq.unitId=:unitId AND "
					+ "sterReq.status='Accepted' "+condition;
			
			StatelessSession statelessSession = sessionFactory.openStatelessSession();
			 Query createQuery = statelessSession
						.createQuery(query)
						.setParameter("fromDate", fromDate)
						.setParameter("toDate", toDate)
						.setParameter("organizationId", organizationId)
						.setParameter("unitId", unitId);


			if(requestingStoreId!=null)
			{
				createQuery.setParameter("requestingStoreId",requestingStoreId);
			}
			if(issueingStoreId!=null)
			{
				createQuery.setParameter("issueingStoreId",issueingStoreId);
			}
			if(requestNo!=null)
			{
				createQuery.setParameter("requestNo",requestNo);
			}


			List<SterilizationRequestDto> sterilizationRequestDtoList=createQuery.setResultTransformer(Transformers.aliasToBean(SterilizationRequestDto.class)).list();
			
			if(!sterilizationRequestDtoList.isEmpty())
			{
				for (SterilizationRequestDto sterilizationRequestDto : sterilizationRequestDtoList) {
					
					List<SterilizationRequestTransitionMapperDto> kitList = statelessSession
							.createQuery("SELECT loadBatch.batchNumber as batchNumber, to_char(loadBatch.autoclaveDate,'dd/MM/yyyy') as autoclaveDate,"
									+ "loadBatch.autoclaveNumber as autoclaveNumber, preSterilizationProcess.transitionNumber as transitionNumber,"
									+ "to_char(preSterilizationProcess.expiryDate,'dd/MM/yyyy') as expiryDate, "
									+ "to_char(preSterilizationProcess.formation_date,'dd/MM/yyyy') as formationDate, "
									+ "preSterilizationProcess.kitId as kitId, kitMaster.code as kitCode, kitMaster.name as kitName "
									+ "FROM LoadBatchKitMapper mapper INNER JOIN mapper.loadBatch as loadBatch "
									+ "INNER JOIN mapper.preSterilizationProcess as preSterilizationProcess "
									+ "INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
									+ "WHERE preSterilizationProcess.transitionNumber IN "
									+ "(select transitionMapper.transitionNumber from SterilizationRequestTransitionMapper transitionMapper "
									+ "where transitionMapper.requestNo=:requestNo and transitionMapper.status='Accepted' ) ")
							.setParameter("requestNo",sterilizationRequestDto.getRequestNo())
							.setResultTransformer(Transformers.aliasToBean(SterilizationRequestTransitionMapperDto.class)).list();
					
					for (SterilizationRequestTransitionMapperDto sterilizationRequestTransitionMapperDto : kitList) {
						
						SterilizationRequestTransitionMapperDto transitionMapperDto = (SterilizationRequestTransitionMapperDto)statelessSession
								.createQuery("select mapper.id as id,mapper.status as status,mapper.rejectReasonId as rejectReasonId,"
										+ " rejectReasonNote as rejectReasonNote from SterilizationRequestTransitionMapper mapper "
										+ " where mapper.transitionNumber=:transitionNumber and mapper.requestNo=:requestNo and mapper.status='Accepted' ")
								.setParameter("transitionNumber",sterilizationRequestTransitionMapperDto.getTransitionNumber())
								.setParameter("requestNo",sterilizationRequestDto.getRequestNo())
								.setResultTransformer(Transformers.aliasToBean(SterilizationRequestTransitionMapperDto.class)).uniqueResult();
						
						if(transitionMapperDto != null)
						{
							sterilizationRequestTransitionMapperDto.setId(transitionMapperDto.getId());
							if(transitionMapperDto.getRejectReasonId() != null)
							{
								sterilizationRequestTransitionMapperDto.setReject(true);
								sterilizationRequestTransitionMapperDto.setRejectReasonId(transitionMapperDto.getRejectReasonId());
								sterilizationRequestTransitionMapperDto.setRejectReasonNote(transitionMapperDto.getRejectReasonNote());
							}else
							{
								sterilizationRequestTransitionMapperDto.setReject(false);
							}
							
						}
						sterilizationRequestTransitionMapperDto.setRequestNo(sterilizationRequestDto.getRequestNo());
						
						List<PreSterilizationProcessInstrumentDto> kitInstrumentList = statelessSession
								.createQuery("SELECT preSterilizationProcessInstrument.id as id, "
										+ "preSterilizationProcessInstrument.kitId as kitId, "
										+ "preSterilizationProcessInstrument.instrumentId as instrumentId,"
										+ "instrumentMaster.code as instrumentCode,"
										+ "instrumentMaster.name as instrumentName," 
										+ "preSterilizationProcessInstrument.status as status, "
										+ "kitInstrumentMapper.quantity as quantity " 
										+ "FROM PreSterilizationProcessInstrument preSterilizationProcessInstrument "
										+ "INNER JOIN preSterilizationProcessInstrument.kitMaster as kitMaster "
										+ "INNER JOIN preSterilizationProcessInstrument.instrumentMaster as instrumentMaster "
										+ "INNER JOIN preSterilizationProcessInstrument.kitInstrumentMapper as kitInstrumentMapper "
										+ "WHERE  preSterilizationProcessInstrument.transitionNumber=:transitionNumber")
								.setParameter("transitionNumber", sterilizationRequestTransitionMapperDto.getTransitionNumber())
								
								.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessInstrumentDto.class)).list();
						
						sterilizationRequestTransitionMapperDto.setKitInstrumentList(kitInstrumentList);
					}
				sterilizationRequestDto.setKitList(kitList);
				}
			}
			statelessSession.close();
			return new Response(SUCCESS, null, null, sterilizationRequestDtoList, null);
		} catch (Exception e) {
			logger.error("getSterilizationRequestByMultipleFieldsForKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*@Override
	public Response rejectKitIssue(Integer organizationId,Integer unitId,Integer issueNo,Integer kitId,Integer rejectReasonId,Integer quantity,String rejectionNote) {
		try {
			Session session=sessionFactory.getCurrentSession();
			Response resp = getKitIssueByIssueNo(issueNo,organizationId,unitId);
			KitIssueDto kitIssueDto = (KitIssueDto)resp.getObject();
			
			if(kitIssueDto != null)
			{
				List<KitIssueKitMapperDto> kitList = kitIssueDto.getKitList();
				if(kitList != null)
				{
					for (KitIssueKitMapperDto kitIssueKitMapperDto : kitList) {
						if(kitIssueKitMapperDto.getKitId() == kitId)
						{
							List<IssuedKitTransitionMapperDto> subKitList = kitIssueKitMapperDto.getSubKitList();
							if(subKitList != null && quantity <= subKitList.size())
							{
								for (int i = 0; i < subKitList.size();i++) {
									if(i < quantity)
									{
										PreSterilizationProcess preSterilizationProcess = (PreSterilizationProcess)session.get(PreSterilizationProcess.class,subKitList.get(i).getTransitionNumber());
										preSterilizationProcess.setIsRejected(true);
										preSterilizationProcess.setRejectReasonId(rejectReasonId);
										preSterilizationProcess.setRejectionNote(rejectionNote);
										session.update(preSterilizationProcess);
									}
									else
									{
										PreSterilizationProcess preSterilizationProcess = (PreSterilizationProcess)session.get(PreSterilizationProcess.class,subKitList.get(i).getTransitionNumber());
										preSterilizationProcess.setIsAccepted(true);
										session.update(preSterilizationProcess);
									}
									
								}
								
							}
							
						}
					}
				}
			}
			
			return new Response(SUCCESS, null, "Kit(s) rejected successfully.", null, null);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, "500", "Internal Server Error", null, null);
	}*/
	
	
	@Override
	public Response getKitIssueList() {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<KitIssueDto> kitIssueDtoList = statelessSession
					.getNamedQuery("GET_KIT_ISSUE_LIST")
					.setResultTransformer(Transformers.aliasToBean(KitIssueDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, kitIssueDtoList, null);
		} catch (Exception e) {
			logger.error("getKitIssueList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response getKitListByIssueNo(Integer issueNo) {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<KitIssueKitMapperDto> kitIssueKitMapperDtoList = statelessSession
					.getNamedQuery("GET_KIT_LIST_BY_ISSUE_NO")
					.setParameter("issueNo", issueNo)
					.setResultTransformer(Transformers.aliasToBean(KitIssueKitMapperDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, kitIssueKitMapperDtoList, null);
		} catch (Exception e) {
			logger.error("getKitListByIssueNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response getKitListForKitIssue() {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<KitIssueKitMapperDto> kitIssueKitMapperDtoList = statelessSession
					.getNamedQuery("GET_KIT_LIST_FOR_KIT_ISSUE")
					.setResultTransformer(Transformers.aliasToBean(KitIssueKitMapperDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, kitIssueKitMapperDtoList, null);
		} catch (Exception e) {
			logger.error("getKitListForKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getKitIssueByIssueNo(Integer issueNo) {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<KitIssueDto> kitIssueDtoList = statelessSession
					.getNamedQuery("GET_KIT_ISSUE_BY_ISSUE_NO")
					.setParameter("issueNo", issueNo)
					.setResultTransformer(Transformers.aliasToBean(KitIssueDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, kitIssueDtoList, null);
		} catch (Exception e) {
			logger.error("getKitIssueByIssueNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/*@Override
	public Response getKitIssueByMultipleFields(Date fromDate,Date toDate,Integer fromStore,Integer toStore,Integer indentNo,String status) {
		try {
			
			Session session = sessionFactory.getCurrentSession();

			List<KitIssueDto> kitIssueDtoList = session
					.getNamedQuery("GET_KIT_ISSUE_BY_MULTIPLE_FIELDS")
					.setParameter("fromDate", fromDate)
					.setParameter("toDate", toDate)
					.setParameter("fromStore", fromStore)
					.setParameter("toStore", toStore)
					.setParameter("indentNo", indentNo)
					.setParameter("status", status)
					.setResultTransformer(Transformers.aliasToBean(KitIssueDto.class)).list();
			
			return new Response(SUCCESS, null, null, kitIssueDtoList, null);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, "500", "Internal Server Error", null, null);
	}
*/
	
	@Override
	public Response stopIssue(Integer batchNo) {
		try {
			Session session = sessionFactory.getCurrentSession();

			LoadBatch loadBatch=(LoadBatch)session.get(LoadBatch.class, batchNo);
			if(loadBatch!=null)
			{
				if(loadBatch.getStatus().equals("batchloaded"))
				{
					loadBatch.setStatus("cannotissue");
					session.update(loadBatch);
					return new Response(SUCCESS, null,"Issue stopped.", null, null);
				}
				
			}
			return new Response(ERROR, null,"Invalid batch.", null, null);
			
			
		} catch (Exception e) {
			logger.error("stopIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response acceptKitIssue(Integer issueNo) {
		try {
			Session session=sessionFactory.getCurrentSession();
			KitIssue kitIssue=(KitIssue)session.get(KitIssue.class, issueNo);
			if(kitIssue!=null)
			{
				if(kitIssue.getStatus().equals("issued"))
				{
					kitIssue.setStatus("accepted");
					session.update(kitIssue);
					return new Response(SUCCESS, "200", "Kit Issue accepted.",null, null);
				}
				else if(kitIssue.getStatus().equals("accepted"))
				{
					return new Response(ERROR, null, "Kit Issue already accepted.", null, null);
				}
				else
				{
					return new Response(ERROR, null, "Invalid Kit Issue.", null, null);
				}
			}else
			{
				return new Response(ERROR, null, "Kit Issue Not available .", null, null);
			}
		} catch (Exception e) {
			logger.error("acceptKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response rejectKitIssue(Integer issueNo,String reason) {
		try {
			Session session=sessionFactory.getCurrentSession();
			KitIssue kitIssue=(KitIssue)session.get(KitIssue.class, issueNo);
			if(kitIssue!=null)
			{
				if(kitIssue.getStatus().equals("issued"))
				{
					kitIssue.setStatus("rejected");
					kitIssue.setReason(reason);
					session.update(kitIssue);
					return new Response(SUCCESS, "200", "Kit Issue rejected.",null, null);
				}
				else if(kitIssue.getStatus().equals("rejected"))
				{
					return new Response(ERROR, null, "Kit Issue already rejected.", null, null);
				}
				else
				{
					return new Response(ERROR, null, "Invalid Kit Issue.", null, null);
				}
			}else
			{
				return new Response(ERROR, null, "Kit Issue Not available.", null, null);
			}
		} catch (Exception e) {
			logger.error("rejectKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}

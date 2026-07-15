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
import com.param.CSSD.dto.KitReturnDto;
import com.param.CSSD.dto.KitReturnKitMapperDto;
import com.param.CSSD.dto.PreSterilizationProcessInstrumentDto;
import com.param.CSSD.dto.ReturnedKitTransitionMapperDto;
import com.param.CSSD.model.KitReturn;
import com.param.CSSD.model.PreSterilizationProcess;
import com.param.CSSD.model.ReturnedKitTransitionMapper;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class KitReturnDaoImpl extends GenericDao<KitReturn, Integer>
		implements IKitReturnDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(KitReturnDaoImpl.class);


	public KitReturnDaoImpl() {
		super(KitReturn.class);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unused")
	@Autowired
	private DozerBeanMapper mapper;


	@Override
	public Response getKitListForKitReturn(Integer organizationId,Integer unitId,Integer fromStoreId,Integer toStoreId,String name) {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<IssuedKitTransitionMapperDto> subKitList = statelessSession
						.createQuery("SELECT loadBatch.batchNumber as batchNumber, to_char(loadBatch.autoclaveDate,'dd/MM/yyyy') as autoclaveDate,"
									+ "loadBatch.autoclaveNumber as autoclaveNumber, preSterilizationProcess.transitionNumber as transitionNumber,"
									+ "to_char(preSterilizationProcess.expiryDate,'dd/MM/yyyy') as expiryDate, "
									+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as kitCode, " + "kitMaster.name as kitName "
									+ "FROM LoadBatchKitMapper mapper INNER JOIN mapper.loadBatch as loadBatch "
									+ "INNER JOIN mapper.preSterilizationProcess as preSterilizationProcess "
									+ "INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
									+ "WHERE kitMaster.is_active=true AND kitMaster.is_deleted=false AND preSterilizationProcess.isAccepted=true "
									+ "AND preSterilizationProcess.sterilizationRequested=false AND preSterilizationProcess.isReturned=false "
									+ "AND lower(kitMaster.name) LIKE :name "
									+ "AND preSterilizationProcess.organizationId=:organizationId AND preSterilizationProcess.unitId=:unitId "
									+ "AND preSterilizationProcess.transitionNumber "
									+ "IN (select transitionMapper.transitionNumber from IssuedKitTransitionMapper transitionMapper "
									+ "INNER JOIN transitionMapper.kitIssue as kitIssue where kitIssue.fromStoreId=:toStoreId "
									+ "and kitIssue.toStoreId=:fromStoreId ) "
									+ "order by preSterilizationProcess.expiryDate ")
						.setParameter("name","%"+name.toLowerCase()+"%")
						.setParameter("fromStoreId", fromStoreId)
						.setParameter("toStoreId",toStoreId)
						.setParameter("organizationId",organizationId)
						.setParameter("unitId",unitId)
						.setResultTransformer(Transformers.aliasToBean(IssuedKitTransitionMapperDto.class)).list();
			
			List<IssuedKitTransitionMapperDto> subKitList1 = statelessSession
					.createQuery("SELECT 0 as batchNumber, '-' as autoclaveDate,"
								+ "0 as autoclaveNumber, preSterilizationProcess.transitionNumber as transitionNumber,"
								+ "to_char(preSterilizationProcess.expiryDate,'dd/MM/yyyy') as expiryDate, "
								+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as kitCode, " + "kitMaster.name as kitName "
								+ "FROM PreSterilizationProcess preSterilizationProcess "
								+ "INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
								+ "WHERE kitMaster.is_active=true AND kitMaster.is_deleted=false AND preSterilizationProcess.isAccepted=true "
								+ "AND preSterilizationProcess.sterilizationRequested=false AND preSterilizationProcess.isReturned=false "
								+ "AND lower(kitMaster.name) LIKE :name "
								+ "AND preSterilizationProcess.organizationId=:organizationId AND preSterilizationProcess.unitId=:unitId "
								+ "AND preSterilizationProcess.transitionNumber "
								+ "IN (select transitionMapper.transitionNumber from IssuedKitTransitionMapper transitionMapper "
								+ "INNER JOIN transitionMapper.kitIssue as kitIssue where kitIssue.fromStoreId=:toStoreId "
								+ "and kitIssue.toStoreId=:fromStoreId ) "
								+ "order by preSterilizationProcess.expiryDate ")
					.setParameter("name","%"+name.toLowerCase()+"%")
					.setParameter("fromStoreId", fromStoreId)
					.setParameter("toStoreId",toStoreId)
					.setParameter("organizationId",organizationId)
					.setParameter("unitId",unitId)
					.setResultTransformer(Transformers.aliasToBean(IssuedKitTransitionMapperDto.class)).list();
			
			if(!subKitList1.isEmpty()) {
				subKitList.addAll(subKitList1);
			}
			statelessSession.close();
			return new Response(SUCCESS, null, null, subKitList, null);
		} catch (Exception e) {
			logger.error("getKitListForKitReturn failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	@Override
	public Response createKitReturn(KitReturnDto kitReturnDto ) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			KitReturn kitReturn=new KitReturn();
			kitReturn.setCarrier(kitReturnDto.getCarrier());
			kitReturn.setFromStoreId(kitReturnDto.getFromStoreId());
			kitReturn.setToStoreId(kitReturnDto.getToStoreId());
			kitReturn.setRemark(kitReturnDto.getRemark());
			kitReturn.setCreatedBy(kitReturnDto.getCreatedBy());
			kitReturn.setUpdatedBy(kitReturnDto.getUpdatedBy());
			
			kitReturn.setCreatedDate(TimeZoneComponent.getDateByzone(kitReturnDto.getUnitId()));
			kitReturn.setUpdatedDate(TimeZoneComponent.getDateByzone(kitReturnDto.getUnitId()));
			kitReturn.setStatus("New");
			kitReturn.setOrganizationId(kitReturnDto.getOrganizationId());
			kitReturn.setUnitId(kitReturnDto.getUnitId());
			
			Integer returnNo=(Integer)session.save(kitReturn);
			
				List<ReturnedKitTransitionMapperDto> subKitList = kitReturnDto.getSubKitList();
				
				for (ReturnedKitTransitionMapperDto returnedKitTransitionMapperDto : subKitList) {
					
					ReturnedKitTransitionMapper transitionMapper = new ReturnedKitTransitionMapper();
					transitionMapper.setReturnNo(returnNo);
					transitionMapper.setKitId(returnedKitTransitionMapperDto.getKitId());
					transitionMapper.setTransitionNumber(returnedKitTransitionMapperDto.getTransitionNumber());
					session.save(transitionMapper);
					
					PreSterilizationProcess preSterilizationProcess = (PreSterilizationProcess)session
							.get(PreSterilizationProcess.class, returnedKitTransitionMapperDto.getTransitionNumber());
					preSterilizationProcess.setIsReturned(true);
					preSterilizationProcess.setReasonForReturn(returnedKitTransitionMapperDto.getReasonForReturn());
					
					
					List<PreSterilizationProcessInstrumentDto> kitItemList=sessionFactory.getCurrentSession().createQuery("Select "
							+ " pre.transitionNumber as id,pre.instrumentId as instrumentId,pre.physicalQuantity as physicalQuantity,pre.storeId as storeId"
							+ "  from PreSterilizationProcessInstrument pre  where pre.transitionNumber=:transitionNo)")
							.setParameter("transitionNo", returnedKitTransitionMapperDto.getTransitionNumber())
							.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessInstrumentDto.class)).list();
				
					//Update sterilized stock against item
					for(PreSterilizationProcessInstrumentDto kitItem:kitItemList) {
						if(preSterilizationProcess.getTransition().equals("Unsterilized")) {
					sessionFactory.getCurrentSession().createQuery(" update CssdStockTransactionLog log "
							+ " set log.notReturnedQuantity=log.notReturnedQuantity-(:sterQty),"
							+ " log.updatedBy=:updatedBy,log.updatedDate=:updatedDate"
							+ " where log.storeId=:storeId and log.itemId=:itemId and log.organizationId=:orgId and log.unitId=:unitId ")
					.setParameter("sterQty", kitItem.getPhysicalQuantity())
					.setParameter("updatedBy", kitReturnDto.getUpdatedBy())
					.setParameter("updatedDate", TimeZoneComponent.getDateByzone(kitReturnDto.getUnitId()))
					.setParameter("storeId", kitItem.getStoreId())
					.setParameter("itemId", kitItem.getInstrumentId())
					.setParameter("orgId",kitReturnDto.getOrganizationId())
					.setParameter("unitId", kitReturnDto.getUnitId())
					.executeUpdate();
					}else {
						sessionFactory.getCurrentSession().createQuery(" update CssdStockTransactionLog log "
								+ " set log.sterilizedQuantity=log.sterilizedQuantity-(:sterQty),"
								+ " log.updatedBy=:updatedBy,log.updatedDate=:updatedDate"
								+ " where log.storeId=:storeId and log.itemId=:itemId and log.organizationId=:orgId and log.unitId=:unitId ")
						.setParameter("sterQty", kitItem.getPhysicalQuantity())
						.setParameter("updatedBy", kitReturnDto.getUpdatedBy())
						.setParameter("updatedDate", TimeZoneComponent.getDateByzone(kitReturnDto.getUnitId()))
						.setParameter("storeId", kitItem.getStoreId())
						.setParameter("itemId", kitItem.getInstrumentId())
						.setParameter("orgId",kitReturnDto.getOrganizationId())
						.setParameter("unitId", kitReturnDto.getUnitId())
						.executeUpdate();
					}
				}
					if(preSterilizationProcess.getTransition().equals("KitFormed")) 
						preSterilizationProcess.setTransition("Unsterilized");
					session.update(preSterilizationProcess);
				}
			
			return new Response(SUCCESS, "200","Kit Return created Successfully.", null, null);
			
		} catch (Exception e) {
			logger.error("createKitReturn failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getKitReturnByMultipleFields(Date fromDate,Date toDate,Integer fromStore,Integer toStore,Integer returnNo,String status,Integer organizationId,Integer unitId) {
		try {
			
			StringBuilder condition=new StringBuilder();
			
			if(fromStore!=null)
			{
				condition.append(" AND kitReturn.fromStoreId=:fromStore ");
			}
			if(toStore!=null)
			{
				condition.append(" AND kitReturn.toStoreId=:toStore ");
			}
			if(returnNo!=null)
			{
				condition.append(" AND kitReturn.returnNo=:returnNo ");
			}
			if(status!=null)
			{
				condition.append(" AND kitReturn.status=:status ");
			}
			
			String query = "SELECT kitReturn.returnNo as returnNo, "
					+ "to_char(kitReturn.createdDate,'dd/MM/yyyy') as createdDate, "
					+ "kitReturn.status as status,fromStore.storeName as fromStore,"
					+ "kitReturn.fromStoreId as fromStoreId,kitReturn.toStoreId as toStoreId,"
					+ "toStore.storeName as toStore,kitReturn.remark as remark,userCreatedBy.userName as userName "
					+ "FROM KitReturn kitReturn "
					+ "INNER JOIN kitReturn.fromStore as fromStore "
					+ "INNER JOIN kitReturn.toStore as toStore "
					+ "INNER JOIN kitReturn.userCreatedBy as userCreatedBy "
					+ "WHERE kitReturn.createdDate BETWEEN :fromDate AND :toDate "
					+ "AND kitReturn.organizationId=:organizationId AND kitReturn.unitId=:unitId "+condition;
			
			StatelessSession statelessSession = sessionFactory.openStatelessSession();
			Query createQuery = statelessSession
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
			if(returnNo!=null)
			{
				createQuery.setParameter("returnNo",returnNo);
			}
			if(status!=null)
			{
				createQuery.setParameter("status",status);
			}


			List<KitReturnDto> kitReturnDtoList=createQuery.setResultTransformer(Transformers.aliasToBean(KitReturnDto.class)).list();
			
			if(!kitReturnDtoList.isEmpty())
			{
				for (KitReturnDto kitReturnDto : kitReturnDtoList) {
					List<ReturnedKitTransitionMapperDto> subKitList = statelessSession
							.createQuery("SELECT loadBatch.batchNumber as batchNumber, to_char(loadBatch.autoclaveDate,'dd/MM/yyyy') as autoclaveDate,"
									+ "loadBatch.autoclaveNumber as autoclaveNumber, preSterilizationProcess.transitionNumber as transitionNumber,"
									+ "to_char(preSterilizationProcess.expiryDate,'dd/MM/yyyy') as expiryDate, "
									+ "preSterilizationProcess.kitId as kitId,preSterilizationProcess.reasonForReturn as reasonForReturn, " 
									+ "kitMaster.code as kitCode, " + "kitMaster.name as kitName "
									+ "FROM LoadBatchKitMapper mapper INNER JOIN mapper.loadBatch as loadBatch "
									+ "INNER JOIN mapper.preSterilizationProcess as preSterilizationProcess "
									+ "INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
									+ "WHERE kitMaster.is_active=true AND kitMaster.is_deleted=false AND preSterilizationProcess.isReturned=true AND "
									+ "preSterilizationProcess.transitionNumber IN (select transitionMapper.transitionNumber from ReturnedKitTransitionMapper transitionMapper "
									+ "where transitionMapper.returnNo=:returnNo ) "
									+ "order by preSterilizationProcess.expiryDate ")
							.setParameter("returnNo",kitReturnDto.getReturnNo())
							.setResultTransformer(Transformers.aliasToBean(ReturnedKitTransitionMapperDto.class)).list();
					
					
					for (ReturnedKitTransitionMapperDto returnedKitTransitionMapperDto : subKitList) {
						
						if (returnedKitTransitionMapperDto != null) {
							
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
											+ "WHERE kitMaster.is_active=true AND kitMaster.is_deleted=false AND preSterilizationProcessInstrument.transitionNumber=:transitionNumber")
									.setParameter("transitionNumber", returnedKitTransitionMapperDto.getTransitionNumber())
									
									.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessInstrumentDto.class)).list();
							
							returnedKitTransitionMapperDto.setKitInstrumentList(kitInstrumentList);
							
						}
						
						
					}
					
					kitReturnDto.setSubKitList(subKitList);
				
					}
			}
			statelessSession.close();
			return new Response(SUCCESS, null, null, kitReturnDtoList, null);
		} catch (Exception e) {
			logger.error("getKitReturnByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getKitReturnByReturnNo(Integer returnNo,Integer organizationId,Integer unitId) {
		try {
			String query = "SELECT kitReturn.returnNo as returnNo, "
					+ "to_char(kitReturn.createdDate,'dd/MM/yyyy') as createdDate, "
					+ "kitReturn.status as status,fromStore.storeName as fromStore,"
					+ "kitReturn.fromStoreId as fromStoreId,kitReturn.toStoreId as toStoreId,"
					+ "toStore.storeName as toStore,kitReturn.remark as remark,userCreatedBy.userName as userName "
					+ "FROM KitReturn kitReturn "
					+ "INNER JOIN kitReturn.fromStore as fromStore "
					+ "INNER JOIN kitReturn.toStore as toStore "
					+ "INNER JOIN kitReturn.userCreatedBy as userCreatedBy "
					+ "WHERE kitReturn.returnNo=:returnNo "
					+ "AND kitReturn.organizationId=:organizationId AND kitReturn.unitId=:unitId ";
			
			StatelessSession statelessSession = sessionFactory.openStatelessSession();
			KitReturnDto kitReturnDto  = (KitReturnDto)statelessSession
						.createQuery(query)
						.setParameter("returnNo", returnNo)
						.setParameter("organizationId", organizationId)
						.setParameter("unitId", unitId)
						.setResultTransformer(Transformers.aliasToBean(KitReturnDto.class)).uniqueResult();
			
			if(kitReturnDto != null)
			{
				
					List<ReturnedKitTransitionMapperDto> subKitList = statelessSession
							.createQuery("SELECT loadBatch.batchNumber as batchNumber, to_char(loadBatch.autoclaveDate,'dd/MM/yyyy') as autoclaveDate,"
									+ "loadBatch.autoclaveNumber as autoclaveNumber, preSterilizationProcess.transitionNumber as transitionNumber,"
									+ "to_char(preSterilizationProcess.expiryDate,'dd/MM/yyyy') as expiryDate, "
									+ "preSterilizationProcess.kitId as kitId,preSterilizationProcess.reasonForReturn as reasonForReturn, " 
									+ "kitMaster.code as kitCode, " + "kitMaster.name as kitName "
									+ "FROM LoadBatchKitMapper mapper INNER JOIN mapper.loadBatch as loadBatch "
									+ "INNER JOIN mapper.preSterilizationProcess as preSterilizationProcess "
									+ "INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
									+ "WHERE kitMaster.is_active=true AND kitMaster.is_deleted=false AND preSterilizationProcess.isReturned=true AND "
									+ "preSterilizationProcess.transitionNumber IN (select transitionMapper.transitionNumber from ReturnedKitTransitionMapper transitionMapper "
									+ "where transitionMapper.returnNo=:returnNo ) "
									+ "order by preSterilizationProcess.expiryDate ")
							.setParameter("returnNo",kitReturnDto.getReturnNo())
							.setResultTransformer(Transformers.aliasToBean(ReturnedKitTransitionMapperDto.class)).list();
					
					for (ReturnedKitTransitionMapperDto returnedKitTransitionMapperDto : subKitList) {
						
						if (returnedKitTransitionMapperDto != null) {
							
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
											+ "WHERE kitMaster.is_active=true AND kitMaster.is_deleted=false AND preSterilizationProcessInstrument.transitionNumber=:transitionNumber")
									.setParameter("transitionNumber", returnedKitTransitionMapperDto.getTransitionNumber())
									
									.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessInstrumentDto.class)).list();
							
							returnedKitTransitionMapperDto.setKitInstrumentList(kitInstrumentList);
							
						}
						
						
					}
					
					kitReturnDto.setSubKitList(subKitList);
				
			}
			statelessSession.close();
			return new Response(SUCCESS, null, null, null, kitReturnDto);
		} catch (Exception e) {
			logger.error("getKitReturnByReturnNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response updateKitReturn(KitReturnDto kitReturnDto) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			KitReturn kitReturn = (KitReturn)session.get(KitReturn.class, kitReturnDto.getReturnNo());
			kitReturn.setCarrier(kitReturnDto.getCarrier());
			kitReturn.setFromStoreId(kitReturnDto.getFromStoreId());
			kitReturn.setToStoreId(kitReturnDto.getToStoreId());
			kitReturn.setReason(kitReturnDto.getReason());
			kitReturn.setRemark(kitReturnDto.getRemark());
			kitReturn.setUpdatedBy(kitReturnDto.getUpdatedBy());
			
			kitReturn.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
			session.update(kitReturn);
			
			String updateIsReturned = "update PreSterilizationProcess preSterilizationProcess set isReturned=false,reasonForReturn='' where preSterilizationProcess.transitionNumber IN "
					+ "(select mapper.transitionNumber from ReturnedKitTransitionMapper mapper where mapper.returnNo=:returnNo ) ";
			session.createQuery(updateIsReturned)
			.setParameter("returnNo", kitReturnDto.getReturnNo())
			.executeUpdate();
			
			String returnedKitTransitionMapperDeleteQuery = "delete from ReturnedKitTransitionMapper mapper where mapper.returnNo=:returnNo";
			String kitReturnKitMapperDeleteQuery = "delete from KitReturnKitMapper mapper where mapper.returnNo=:returnNo";
			
			session.createQuery(returnedKitTransitionMapperDeleteQuery)
			.setParameter("returnNo", kitReturnDto.getReturnNo())
			.executeUpdate();
			session.createQuery(kitReturnKitMapperDeleteQuery)
			.setParameter("returnNo", kitReturnDto.getReturnNo())
			.executeUpdate();
		
			
			List<ReturnedKitTransitionMapperDto> subKitList = kitReturnDto.getSubKitList();
			
			for (ReturnedKitTransitionMapperDto returnedKitTransitionMapperDto : subKitList) {
				
				ReturnedKitTransitionMapper transitionMapper = new ReturnedKitTransitionMapper();
				transitionMapper.setReturnNo(kitReturnDto.getReturnNo());
				transitionMapper.setKitId(returnedKitTransitionMapperDto.getKitId());
				transitionMapper.setTransitionNumber(returnedKitTransitionMapperDto.getTransitionNumber());
				session.save(transitionMapper);
				
				PreSterilizationProcess preSterilizationProcess = (PreSterilizationProcess)session
						.get(PreSterilizationProcess.class, returnedKitTransitionMapperDto.getTransitionNumber());
				preSterilizationProcess.setIsReturned(true);
				preSterilizationProcess.setReasonForReturn(returnedKitTransitionMapperDto.getReasonForReturn());
				session.update(preSterilizationProcess);
			}
			
			return new Response(SUCCESS, "200","Kit issue updated Successfully.", null, null);
			
		} catch (Exception e) {
			logger.error("updateKitReturn failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response updateKitReturnStatus(List<KitReturnDto> kitReturnDtoList ) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			
			for (KitReturnDto kitReturnDto : kitReturnDtoList) {
				
				KitReturn kitReturn = (KitReturn) session.get(KitReturn.class, kitReturnDto.getReturnNo());
				kitReturn.setStatus("Pending");
				session.update(kitReturn);
			}
			
			return new Response(SUCCESS, "200","Kit return updated successfully.", null, null);
			
		} catch (Exception e) {
			logger.error("updateKitReturnStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response cancelKitReturn(KitReturnDto kitReturnDto ) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			
				KitReturn kitReturn = (KitReturn) session.get(KitReturn.class, kitReturnDto.getReturnNo());
				kitReturn.setStatus("Cancelled");
				kitReturn.setCancelReasonId(kitReturnDto.getCancelReasonId());
				kitReturn.setCancelReasonNote(kitReturnDto.getCancelReasonNote());
				session.update(kitReturn);
			
			
			return new Response(SUCCESS, "200","Issue cancelled Successfully.", null, null);
			
		} catch (Exception e) {
			logger.error("cancelKitReturn failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*-----------------------------------------------------------------------------------------------------------------------------------------------------------------*/	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public Response getKitReturnList() {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<KitReturnDto> kitReturnDtoList = statelessSession
					.getNamedQuery("GET_KIT_RETURN_LIST")
					.setResultTransformer(Transformers.aliasToBean(KitReturnDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, kitReturnDtoList, null);
		} catch (Exception e) {
			logger.error("getKitReturnList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
		
	@Override
	public Response getKitListByReturnNo(Integer returnNo) {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<KitReturnKitMapperDto> kitReturnKitMapperDtoList = statelessSession
					.getNamedQuery("GET_KIT_LIST_BY_RETURN_NO")
					.setParameter("returnNo", returnNo)
					.setResultTransformer(Transformers.aliasToBean(KitReturnKitMapperDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, kitReturnKitMapperDtoList, null);
		} catch (Exception e) {
			logger.error("getKitListByReturnNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	/*@Override
	public Response getKitReturnByReturnNo(Integer returnNo,Integer organizationId,Integer unitId) {
		try {
			String query = "SELECT kitReturn.returnNo as returnNo, "
					+ "to_char(kitReturn.createdDate,'dd/MM/yyyy') as createdDate, "
					+ "kitReturn.status as status,fromStore.storeName as fromStore,"
					+ "kitReturn.fromStoreId as fromStoreId,kitReturn.toStoreId as toStoreId,"
					+ "toStore.storeName as toStore,kitReturn.remark as remark,userCreatedBy.userName as userName "
					+ "FROM KitReturn kitReturn "
					+ "INNER JOIN kitReturn.fromStore as fromStore "
					+ "INNER JOIN kitReturn.toStore as toStore "
					+ "INNER JOIN kitReturn.userCreatedBy as userCreatedBy "
					+ "WHERE kitReturn.returnNo=:returnNo "
					+ "AND kitReturn.organizationId=:organizationId AND kitReturn.unitId=:unitId ";
			
			StatelessSession statelessSession = sessionFactory.openStatelessSession();
			KitReturnDto kitReturnDto  = (KitReturnDto)statelessSession
						.createQuery(query)
						.setParameter("returnNo", returnNo)
						.setParameter("organizationId", organizationId)
						.setParameter("unitId", unitId)
						.setResultTransformer(Transformers.aliasToBean(KitReturnDto.class)).uniqueResult();
			
			if(kitReturnDto != null)
			{
					List<KitReturnKitMapperDto> kitReturnKitMapperDtoList = statelessSession
							.getNamedQuery("GET_KIT_LIST_BY_RETURN_NO")
							.setParameter("returnNo", kitReturnDto.getReturnNo())
							.setResultTransformer(Transformers.aliasToBean(KitReturnKitMapperDto.class)).list();
					
					if(!kitReturnKitMapperDtoList.isEmpty())
					{
						for (KitReturnKitMapperDto kitReturnKitMapperDto : kitReturnKitMapperDtoList) {
							List<KitInstrumentMapperDto> kitInstrumentList = statelessSession
									.getNamedQuery("GET_INSTRUMENT_LIST_BY_KIT_ID")
									.setParameter("kitId", kitReturnKitMapperDto.getKitId())
									.setResultTransformer(Transformers.aliasToBean(KitInstrumentMapperDto.class)).list();
							kitReturnKitMapperDto.setKitInstrumentList(kitInstrumentList);
							
							List<ReturnedKitTransitionMapperDto> subKitList = statelessSession
									.createQuery("SELECT loadBatch.batchNumber as batchNumber, to_char(loadBatch.autoclaveDate,'dd/MM/yyyy') as autoclaveDate,"
											+ "loadBatch.autoclaveNumber as autoclaveNumber, preSterilizationProcess.transitionNumber as transitionNumber,"
											+ "to_char(preSterilizationProcess.expiryDate,'dd/MM/yyyy') as expiryDate, "
											+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as kitCode, " + "kitMaster.name as kitName "
											+ "FROM LoadBatchKitMapper mapper INNER JOIN mapper.loadBatch as loadBatch "
											+ "INNER JOIN mapper.preSterilizationProcess as preSterilizationProcess "
											+ "INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
											+ "WHERE kitMaster.is_active=true AND kitMaster.is_deleted=false AND preSterilizationProcess.isReturned=true AND "
											+ "preSterilizationProcess.transitionNumber IN (select transitionMapper.transitionNumber from ReturnedKitTransitionMapper transitionMapper "
											+ "where transitionMapper.kitId=:kitId and transitionMapper.returnNo=:returnNo ) "
											+ "order by preSterilizationProcess.expiryDate ")
									.setParameter("kitId", kitReturnKitMapperDto.getKitId())
									.setParameter("returnNo",kitReturnDto.getReturnNo())
									.setResultTransformer(Transformers.aliasToBean(ReturnedKitTransitionMapperDto.class)).list();
							kitReturnKitMapperDto.setSubKitList(subKitList);
							
						}
					}
					
					kitReturnDto.setKitList(kitReturnKitMapperDtoList);
				
			}
			statelessSession.close();
			return new Response(SUCCESS, null, null, null, kitReturnDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, "500", "Internal Server Error", null, null);
	}
*/
}

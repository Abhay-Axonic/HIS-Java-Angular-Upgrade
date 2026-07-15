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

import com.param.CSSD.dto.KitIndentDto;
import com.param.CSSD.dto.KitIndentKitMapperDto;
import com.param.CSSD.dto.StoreDto;
import com.param.CSSD.model.KitIndent;
import com.param.CSSD.model.KitIndentKitMapper;
import com.param.CSSDMaster.dto.KitInstrumentMapperDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.common.dto.PriorityMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class KitIndentDaoImpl extends GenericDao<KitIndent, Integer>
		implements IKitIndentDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(KitIndentDaoImpl.class);


	public KitIndentDaoImpl() {
		super(KitIndent.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private DozerBeanMapper mapper;


	@Override
	public Response createKitIndent(KitIndentDto kitIndentDto ) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			KitIndent kitIndent=mapper.map(kitIndentDto, KitIndent.class,"KitIndentDto_to_KitIndent");
			
			kitIndent.setIndentDate(TimeZoneComponent.getDateByzone(kitIndentDto.getUnitId()));
			kitIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(kitIndentDto.getUnitId()));
			kitIndent.setStatus("New");
			Integer indentNo=(Integer)session.save(kitIndent);
			
			List<KitIndentKitMapperDto> kikmList=kitIndentDto.getKitList();
			for (KitIndentKitMapperDto kitIndentKitMapperDto : kikmList) {
				KitIndentKitMapper mapper=new KitIndentKitMapper();
				mapper.setIndentNo(indentNo);
				mapper.setKitId(kitIndentKitMapperDto.getKitId());
				mapper.setQuantity(kitIndentKitMapperDto.getQuantity());
				mapper.setStatus("New");
				session.save(mapper);
			}
			
			
			return new Response(SUCCESS, "200","Indent created Successfully.", null, null);
			
		} catch (Exception e) {
			logger.error("createKitIndent failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response updateKitIndent(KitIndentDto kitIndentDto ) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			KitIndent kitIndent=(KitIndent)session.get(KitIndent.class, kitIndentDto.getIndentNo());
			
			kitIndent.setFromStoreId(kitIndent.getFromStoreId());
			kitIndent.setToStoreId(kitIndentDto.getToStoreId());
			kitIndent.setRemarks(kitIndentDto.getRemarks());
			kitIndent.setUpdatedBy(kitIndentDto.getUpdatedBy());
			
			kitIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(kitIndentDto.getUnitId()));
			
			session.update(kitIndent);
			
			Query query = session.createQuery("delete KitIndentKitMapper mapper where mapper.indentNo = :indentNo");
			query.setParameter("indentNo", kitIndent.getIndentNo());

			@SuppressWarnings("unused")
			int result = query.executeUpdate();
			
			if (!kitIndentDto.getKitList().isEmpty()) {
				for (KitIndentKitMapperDto kitIndentKitMapperDto : kitIndentDto.getKitList()) {
						KitIndentKitMapper mapper=new KitIndentKitMapper();
						mapper.setIndentNo(kitIndentDto.getIndentNo());
						mapper.setKitId(kitIndentKitMapperDto.getKitId());
						mapper.setQuantity(kitIndentKitMapperDto.getQuantity());
						session.save(mapper);
				}
			}
			
			
			return new Response(SUCCESS, "200","Indent created Successfully.", null, null);
			
		} catch (Exception e) {
			logger.error("updateKitIndent failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
		
	
	@Override
	public Response updateKitIndentStatus(List<KitIndentDto> kitIndentDtoList ) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			
			for (KitIndentDto kitIndentDto : kitIndentDtoList) {
				
				KitIndent kitIndent = (KitIndent) session.get(KitIndent.class, kitIndentDto.getIndentNo());
				kitIndent.setStatus("Pending");
				session.update(kitIndent);
			}
			
			return new Response(SUCCESS, "200","Indents saved Successfully.", null, null);
			
		} catch (Exception e) {
			logger.error("updateKitIndentStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response cancelKitIndent(KitIndentDto kitIndentDto ) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			
				KitIndent kitIndent = (KitIndent) session.get(KitIndent.class, kitIndentDto.getIndentNo());
				kitIndent.setStatus("Cancelled");
				kitIndent.setCancelReasonId(kitIndentDto.getCancelReasonId());
				kitIndent.setCancelReasonNote(kitIndentDto.getCancelReasonNote());
				session.update(kitIndent);
			
			
			return new Response(SUCCESS, "200","Indent cancelled Successfully.", null, null);
			
		} catch (Exception e) {
			logger.error("cancelKitIndent failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response getKitIndentList(Integer organizationId,Integer unitId) {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<KitIndentDto> kitIndentDtoList = statelessSession
					.getNamedQuery("GET_KIT_INDENT_LIST")
					.setParameter("organizationId",organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(KitIndentDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, kitIndentDtoList, null);
		} catch (Exception e) {
			logger.error("getKitIndentList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	@Override
	public Response getKitIndentByMultipleFields(Date fromDate,Date toDate,Integer fromStore,Integer toStore,Integer indentNo,String status,Integer organizationId,Integer unitId) {
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
			if(status!=null)
			{
				condition.append(" AND kitIndent.status=:status ");
			}
			
			String query = "SELECT kitIndent.indentNo as indentNo, "
					+ "to_char(kitIndent.indentDate,'dd/MM/yyyy') as indentDate, "
					+ "kitIndent.status as status,fromStore.storeName as fromStore,"
					+ "toStore.storeName as toStore,userCreatedBy.userName as userName,priority.priorityDesc as priorityDesc FROM KitIndent kitIndent "
					+ "INNER JOIN kitIndent.fromStore as fromStore "
					+ "INNER JOIN kitIndent.toStore as toStore "
					+ "INNER JOIN kitIndent.userCreatedBy as userCreatedBy "
					+ "INNER JOIN kitIndent.priority as priority "
					+ "WHERE kitIndent.indentDate BETWEEN :fromDate AND :toDate "
					+ "AND kitIndent.organizationId=:organizationId AND kitIndent.unitId=:unitId "+condition;
			
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
				createQuery.setParameter("toStore", toStore);
			}
			if(indentNo!=null)
			{
				createQuery.setParameter("indentNo", indentNo);
			}
			if(status!=null)
			{
				createQuery.setParameter("status", status);
			}

			List<KitIndentDto> kitIndentDtoList =createQuery.setResultTransformer(Transformers.aliasToBean(KitIndentDto.class)).list();
			
			if(!kitIndentDtoList.isEmpty())
			{
				for (KitIndentDto kitIndentDto : kitIndentDtoList) {
					List<KitIndentKitMapperDto> kitIndentKitMapperDtoList = statelessSession
							.getNamedQuery("GET_KIT_LIST_BY_INDENT_NO")
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
			logger.error("getKitIndentByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getKitIndentApprovalByMultipleFields(Date fromDate,Date toDate,Integer fromStore,Integer toStore,Integer indentNo,String status,Integer organizationId,Integer unitId) {
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
			if(status!=null)
			{
				condition.append(" AND kitIndent.status=:status ");
			}
			
			String query = "SELECT kitIndent.indentNo as indentNo, "
					+ "to_char(kitIndent.indentDate,'dd/MM/yyyy') as indentDate, "
					+ "kitIndent.status as status,fromStore.storeName as fromStore,"
					+ "toStore.storeName as toStore,userCreatedBy.userName as userName FROM KitIndent kitIndent "
					+ "INNER JOIN kitIndent.fromStore as fromStore "
					+ "INNER JOIN kitIndent.toStore as toStore "
					+ "INNER JOIN kitIndent.userCreatedBy as userCreatedBy "
					+ "WHERE kitIndent.status!='New' AND kitIndent.indentDate BETWEEN :fromDate AND :toDate "
					+ "AND kitIndent.organizationId=:organizationId AND kitIndent.unitId=:unitId "+condition;
					
			
			
			StatelessSession statelessSession = sessionFactory.openStatelessSession();
			Query cQuery=statelessSession
						.createQuery(query)
						.setParameter("fromDate", fromDate)
						.setParameter("toDate", toDate)
						.setParameter("organizationId", organizationId)
						.setParameter("unitId", unitId);

			if(fromStore!=null)
			{
				cQuery.setParameter("fromStore",fromStore);
			}
			if(toStore!=null)
			{
				cQuery.setParameter("toStore", toStore);
			}
			if(indentNo!=null)
			{
				cQuery.setParameter("indentNo", indentNo);
			}
			if(status!=null)
			{
				cQuery.setParameter("status", status);
			}

			List<KitIndentDto> kitIndentDtoList  = cQuery.setResultTransformer(Transformers.aliasToBean(KitIndentDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, kitIndentDtoList, null);
		} catch (Exception e) {
			logger.error("getKitIndentApprovalByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response getKitListByIndentNo(Integer organizationId,Integer unitId,Integer indentNo) {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<KitIndentKitMapperDto> kitIndentKitMapperDtoList = statelessSession
					.getNamedQuery("GET_KIT_LIST_BY_INDENT_NO")
					.setParameter("indentNo", indentNo)
					.setParameter("organizationId",organizationId)
					.setParameter("unitId",unitId)
					.setResultTransformer(Transformers.aliasToBean(KitIndentKitMapperDto.class)).list();
			
			
			if (!kitIndentKitMapperDtoList.isEmpty()) {
				for (KitIndentKitMapperDto kitIndentKitMapperDto : kitIndentKitMapperDtoList) {
					if(kitIndentKitMapperDto.getStatus()!=null)
					{
						if(kitIndentKitMapperDto.getStatus().equals("Rejected"))
						{
							String reason = (String)statelessSession
									.createQuery("select reason from RejectReason rejectReason where rejectReason.id =:id ")
									.setParameter("id", kitIndentKitMapperDto.getRejectReasonId())
									.uniqueResult();
							kitIndentKitMapperDto.setReason(reason);
						}
						
					}
				}
			}

			
			if (!kitIndentKitMapperDtoList.isEmpty()) {
				for (KitIndentKitMapperDto kitIndentKitMapperDto : kitIndentKitMapperDtoList) {

					List<KitInstrumentMapperDto> kitInstrumentList = statelessSession
							.getNamedQuery("GET_INSTRUMENT_LIST_BY_KIT_ID").setParameter("kitId", kitIndentKitMapperDto.getKitId())
							.setResultTransformer(Transformers.aliasToBean(KitInstrumentMapperDto.class)).list();
					kitIndentKitMapperDto.setKitInstrumentList(kitInstrumentList);
				}
			}
			statelessSession.close();
			return new Response(SUCCESS, null, null, kitIndentKitMapperDtoList, null);
		} catch (Exception e) {
			logger.error("getKitListByIndentNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response getKitIndentByIndentNo(Integer organizationId,Integer unitId,Integer indentNo) {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			KitIndentDto kitIndentDto =(KitIndentDto) statelessSession
					.getNamedQuery("GET_KIT_INDENT_BY_INDENT_NO")
					.setParameter("indentNo", indentNo)
					.setParameter("organizationId",organizationId)
					.setParameter("unitId",unitId)
					.setResultTransformer(Transformers.aliasToBean(KitIndentDto.class)).uniqueResult();
			
			
			if(kitIndentDto != null)
			{
					List<KitIndentKitMapperDto> kitIndentKitMapperDtoList = statelessSession
							.getNamedQuery("GET_KIT_LIST_BY_INDENT_NO")
							.setParameter("indentNo", kitIndentDto.getIndentNo())
							.setParameter("organizationId", organizationId)
							.setParameter("unitId", unitId)
							.setResultTransformer(Transformers.aliasToBean(KitIndentKitMapperDto.class)).list();
					kitIndentDto.setKitList(kitIndentKitMapperDtoList);
				
			}
			statelessSession.close();
			return new Response(SUCCESS, null, null, null, kitIndentDto);
		} catch (Exception e) {
			logger.error("getKitIndentByIndentNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getKitIndentByIndentNoForKitIssue(Integer organizationId,Integer unitId,Integer indentNo) {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			KitIndentDto kitIndentDto =(KitIndentDto) statelessSession
					.getNamedQuery("GET_KIT_INDENT_BY_INDENT_NO")
					.setParameter("indentNo", indentNo)
					.setParameter("organizationId",organizationId)
					.setParameter("unitId",unitId)
					.setResultTransformer(Transformers.aliasToBean(KitIndentDto.class)).uniqueResult();
			
			
			if(kitIndentDto != null)
			{
					List<KitIndentKitMapperDto> kitIndentKitMapperDtoList = statelessSession
							.getNamedQuery("GET_KIT_LIST_BY_INDENT_NO_FOR_KIT_ISSUE")
							.setParameter("indentNo", kitIndentDto.getIndentNo())
							.setParameter("organizationId", organizationId)
							.setParameter("unitId", unitId)
							.setResultTransformer(Transformers.aliasToBean(KitIndentKitMapperDto.class)).list();
					kitIndentDto.setKitList(kitIndentKitMapperDtoList);
				
			}
			statelessSession.close();
			return new Response(SUCCESS, null, null, null, kitIndentDto);
		} catch (Exception e) {
			logger.error("getKitIndentByIndentNoForKitIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response approveKit(KitIndentKitMapperDto kitMapperDto) {
		try {
			Session session=sessionFactory.getCurrentSession();
			if(kitMapperDto!=null)
			{
					KitIndentKitMapper kitIndentKitMapper=(KitIndentKitMapper)session.get(KitIndentKitMapper.class, kitMapperDto.getId());
					if(kitIndentKitMapper!=null)
					{
						kitIndentKitMapper.setStatus("Accepted");
						session.update(kitIndentKitMapper);
						
						
						Long totalKitCount = (Long)session
								.createQuery("SELECT count(*) FROM KitIndentKitMapper mapper WHERE mapper.indentNo=:indentNo")
								.setParameter("indentNo", kitIndentKitMapper.getIndentNo())
								.uniqueResult();
						
						Long acceptKitCount = (Long)session
								.createQuery("SELECT count(*) FROM KitIndentKitMapper mapper WHERE mapper.indentNo=:indentNo AND mapper.status='Accepted'")
								.setParameter("indentNo", kitIndentKitMapper.getIndentNo())
								.uniqueResult();
						
						Long acceptRejectKitCount = (Long)session
								.createQuery("SELECT count(*) FROM KitIndentKitMapper mapper WHERE mapper.indentNo=:indentNo AND (mapper.status='Accepted' "
										+ "OR mapper.status='Rejected') ")
								.setParameter("indentNo", kitIndentKitMapper.getIndentNo())
								.uniqueResult();
						
						if(totalKitCount == acceptRejectKitCount)
						{
							Query query=session.createQuery("UPDATE KitIndent set status='Approved' WHERE indentNo=:indentNo")
									.setParameter("indentNo", kitIndentKitMapper.getIndentNo());
							@SuppressWarnings("unused")
							int result=query.executeUpdate();
						}
						else if(acceptKitCount > 0)
						{
							Query query=session.createQuery("UPDATE KitIndent set status='Partially Approved' WHERE indentNo=:indentNo")
									.setParameter("indentNo", kitIndentKitMapper.getIndentNo());
							@SuppressWarnings("unused")
							int result=query.executeUpdate();
						}
							
						
						return new Response(SUCCESS, "200", "Kit Accepted successfully.",null, null);
					}
		}
			return new Response(ERROR, null, "No Kit Available .", null, null);
		} catch (Exception e) {
			logger.error("approveKit failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response rejectKit(KitIndentKitMapperDto kitMapperDto) {
		try {
			Session session=sessionFactory.getCurrentSession();
			if(kitMapperDto!=null)
			{
					KitIndentKitMapper kitIndentKitMapper=(KitIndentKitMapper)session.get(KitIndentKitMapper.class, kitMapperDto.getId());
					if(kitIndentKitMapper!=null)
					{
						kitIndentKitMapper.setStatus("Rejected");
						kitIndentKitMapper.setRejectReasonId(kitMapperDto.getRejectReasonId());
						kitIndentKitMapper.setRejectReasonNote(kitMapperDto.getRejectReasonNote());
						session.update(kitIndentKitMapper);
						
						Long totalKitCount = (Long)session
								.createQuery("SELECT count(*) FROM KitIndentKitMapper mapper WHERE mapper.indentNo=:indentNo")
								.setParameter("indentNo", kitIndentKitMapper.getIndentNo())
								.uniqueResult();
						
						Long rejectKitCount = (Long)session
								.createQuery("SELECT count(*) FROM KitIndentKitMapper mapper WHERE mapper.indentNo=:indentNo AND mapper.status='Rejected'")
								.setParameter("indentNo", kitIndentKitMapper.getIndentNo())
								.uniqueResult();
						
						Long acceptRejectKitCount = (Long)session
								.createQuery("SELECT count(*) FROM KitIndentKitMapper mapper WHERE mapper.indentNo=:indentNo AND (mapper.status='Accepted' "
										+ "OR mapper.status='Rejected') ")
								.setParameter("indentNo", kitIndentKitMapper.getIndentNo())
								.uniqueResult();
						
						
						if(totalKitCount == rejectKitCount)
						{
							Query query=session.createQuery("UPDATE KitIndent set status='Rejected' WHERE indentNo=:indentNo")
									.setParameter("indentNo", kitIndentKitMapper.getIndentNo());
							@SuppressWarnings("unused")
							int result=query.executeUpdate();
						}
						else if(totalKitCount == acceptRejectKitCount)
						{
							Query query=session.createQuery("UPDATE KitIndent set status='Approved' WHERE indentNo=:indentNo")
									.setParameter("indentNo", kitIndentKitMapper.getIndentNo());
							@SuppressWarnings("unused")
							int result=query.executeUpdate();
						}
						else if(rejectKitCount > 0)
						{
							Query query=session.createQuery("UPDATE KitIndent set status='Partially Approved' WHERE indentNo=:indentNo")
									.setParameter("indentNo", kitIndentKitMapper.getIndentNo());
							@SuppressWarnings("unused")
							int result=query.executeUpdate();
						}
						
						return new Response(SUCCESS, "200", "Kit Rejected successfully.",null, null);
					}
				
			}
			return new Response(ERROR, null, "No Kit Available .", null, null);
		} catch (Exception e) {
			logger.error("rejectKit failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response getKitIndentFromStore(Integer organizationId,Integer unitId,Integer userId) {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<StoreDto> storeList = statelessSession
					.getNamedQuery("GET_FROM_STORE_LIST_BY_USER_ID")
					.setParameter("userId", userId)
					.setParameter("organizationId",organizationId)
					.setParameter("unitId",unitId)
					.setResultTransformer(Transformers.aliasToBean(StoreDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, storeList, null);
			
			
			
		} catch (Exception e) {
			logger.error("getKitIndentFromStore failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getCssdStoreListByUserId(Integer organizationId,Integer unitId,Integer userId) {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<StoreDto> storeList = statelessSession
					.getNamedQuery("GET_CSSD_STORE_LIST_BY_USER_ID")
					.setParameter("userId", userId)
					.setParameter("organizationId",organizationId)
					.setParameter("unitId",unitId)
					.setResultTransformer(Transformers.aliasToBean(StoreDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, storeList, null);
			
			
			
		} catch (Exception e) {
			logger.error("getCssdStoreListByUserId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	
	
	
	
	
	
	@Override
	public Response getKitIndentToStore(Integer organizationId,Integer unitId) {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<StoreDto> storeList = statelessSession
					.getNamedQuery("GET_CSSD_STORE_LIST")
					.setParameter("organizationId",organizationId)
					.setParameter("unitId",unitId)
					.setResultTransformer(Transformers.aliasToBean(StoreDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, storeList, null);
		} catch (Exception e) {
			logger.error("getKitIndentToStore failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getPriorityList(Integer organizationId,Integer unitId) {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<PriorityMasterDto> priorityList = statelessSession
					.getNamedQuery("GET_PRIORITY_MASTER_LIST")
					//.setParameter("orgId", organizationId)
					.setResultTransformer(Transformers.aliasToBean(PriorityMasterDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, priorityList, null);
		} catch (Exception e) {
			logger.error("getPriorityList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}

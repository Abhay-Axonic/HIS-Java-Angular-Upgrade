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

import com.param.CSSD.dto.KitReturnDto;
import com.param.CSSD.dto.KitReturnKitMapperDto;
import com.param.CSSD.dto.PreSterilizationProcessDto;
import com.param.CSSD.dto.PreSterilizationProcessInstrumentDto;
import com.param.CSSD.dto.SterilizationRequestDto;
import com.param.CSSD.dto.SterilizationRequestTransitionMapperDto;
import com.param.CSSD.model.KitMovementRecord;
import com.param.CSSD.model.PreSterilizationProcess;
import com.param.CSSD.model.PreSterilizationProcessInstrument;
import com.param.CSSD.model.SterilizationRequest;
import com.param.CSSD.model.SterilizationRequestTransitionMapper;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SterilizationRequestDaoImpl extends GenericDao<SterilizationRequest, Integer>
		implements ISterilizationRequestDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(SterilizationRequestDaoImpl.class);


	public SterilizationRequestDaoImpl() {
		super(SterilizationRequest.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private IPreSterilizationProcessDao iPreSterilizationProcessDao;
	
	@SuppressWarnings("unused")
	@Autowired
	private DozerBeanMapper mapper;


	@Override
	public Response getKitListForSterilizationRequest(Integer organizationId,Integer unitId,Integer requestingStoreId,Integer issueingStoreId,String name) {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<SterilizationRequestTransitionMapperDto> kitListForSterilizationRequest = statelessSession
						.createQuery("SELECT loadBatch.batchNumber as batchNumber, to_char(loadBatch.autoclaveDate,'dd/MM/yyyy') as autoclaveDate,"
									+ "loadBatch.autoclaveNumber as autoclaveNumber, preSterilizationProcess.transitionNumber as transitionNumber,"
									+ "to_char(preSterilizationProcess.expiryDate,'dd/MM/yyyy') as expiryDate, "
									+ "to_char(preSterilizationProcess.formation_date,'dd/MM/yyyy') as formationDate, "
									+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as kitCode, " + "kitMaster.name as kitName "
									+ "FROM LoadBatchKitMapper mapper INNER JOIN mapper.loadBatch as loadBatch "
									+ "INNER JOIN mapper.preSterilizationProcess as preSterilizationProcess "
									+ "INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
									+ "WHERE kitMaster.is_active=true AND kitMaster.is_deleted=false AND preSterilizationProcess.isAccepted=true "
									+ "AND preSterilizationProcess.isReturned=false AND lower(kitMaster.name) LIKE :name "
									+ "AND preSterilizationProcess.organizationId=:organizationId AND preSterilizationProcess.unitId=:unitId "
									+ "AND preSterilizationProcess.status = 'Unsterilized' AND preSterilizationProcess.sterilizationRequested = false "
									+ "AND preSterilizationProcess.transitionNumber "
									+ "IN (select transitionMapper.transitionNumber from IssuedKitTransitionMapper transitionMapper "
									+ "INNER JOIN transitionMapper.kitIssue as kitIssue where kitIssue.fromStoreId=:toStoreId "
									+ "and kitIssue.toStoreId=:fromStoreId ) "
									+ "order by preSterilizationProcess.expiryDate ")
						.setParameter("name","%"+name.toLowerCase()+"%")
						.setParameter("fromStoreId", requestingStoreId)
						.setParameter("toStoreId",issueingStoreId)
						.setParameter("organizationId",organizationId)
						.setParameter("unitId",unitId)
						.setResultTransformer(Transformers.aliasToBean(SterilizationRequestTransitionMapperDto.class)).list();
			
			for (SterilizationRequestTransitionMapperDto sterilizationRequestTransitionMapperDto : kitListForSterilizationRequest) {
				
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
						.setParameter("transitionNumber", sterilizationRequestTransitionMapperDto.getTransitionNumber())
						.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessInstrumentDto.class)).list();
				
				sterilizationRequestTransitionMapperDto.setKitInstrumentList(kitInstrumentList);
			}
			statelessSession.close();
			return new Response(SUCCESS, null, null, kitListForSterilizationRequest, null);
		} catch (Exception e) {
			logger.error("getKitListForSterilizationRequest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	@Override
	public Response createSterilizationRequest(SterilizationRequestDto sterilizationRequestDto ) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			SterilizationRequest sterilizationRequest = new SterilizationRequest();
			sterilizationRequest.setCarrier(sterilizationRequestDto.getCarrier());
			sterilizationRequest.setRequestingStoreId(sterilizationRequestDto.getRequestingStoreId());
			sterilizationRequest.setIssueingStoreId(sterilizationRequestDto.getIssueingStoreId());
			sterilizationRequest.setRemark(sterilizationRequestDto.getRemark());
			sterilizationRequest.setCreatedBy(sterilizationRequestDto.getCreatedBy());
			sterilizationRequest.setUpdatedBy(sterilizationRequestDto.getUpdatedBy());
			
			sterilizationRequest.setCreatedDate(TimeZoneComponent.getDateByzone(sterilizationRequestDto.getUnitId()));
			sterilizationRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(sterilizationRequestDto.getUnitId()));
			sterilizationRequest.setStatus("New");
			sterilizationRequest.setOrganizationId(sterilizationRequestDto.getOrganizationId());
			sterilizationRequest.setUnitId(sterilizationRequestDto.getUnitId());
			
			Integer requestNo=(Integer)session.save(sterilizationRequest);
			
				List<SterilizationRequestTransitionMapperDto> kitList = sterilizationRequestDto.getKitList();
				
				for (SterilizationRequestTransitionMapperDto sterilizationRequestTransitionMapperDto : kitList) {
					
					SterilizationRequestTransitionMapper transitionMapper = new SterilizationRequestTransitionMapper();
					transitionMapper.setRequestNo(requestNo);
					transitionMapper.setTransitionNumber(sterilizationRequestTransitionMapperDto.getTransitionNumber());
					session.save(transitionMapper);
					
					PreSterilizationProcess preSterilizationProcess = (PreSterilizationProcess)session
							.get(PreSterilizationProcess.class, sterilizationRequestTransitionMapperDto.getTransitionNumber());
					preSterilizationProcess.setSterilizationRequested(true);
				}
			
			return new Response(SUCCESS, "200","Sterilization Request created Successfully.", null, null);
			
		} catch (Exception e) {
			logger.error("createSterilizationRequest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getSterilizationRequestByMultipleFields(Date fromDate,Date toDate,Integer requestingStoreId,Integer issueingStoreId,Integer requestNo,String status,Integer organizationId,Integer unitId) {
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
			if(status!=null)
			{
				condition.append(" AND sterReq.status=:status ");
			}
			
			String query = "SELECT sterReq.requestNo as requestNo, "
					+ "to_char(sterReq.createdDate,'dd/MM/yyyy') as createdDate, "
					+ "sterReq.status as status,requestingStore.storeName as requestingStore,"
					+ "sterReq.requestingStoreId as requestingStoreId,sterReq.issueingStoreId as issueingStoreId,"
					+ "issueingStore.storeName as issueingStore,sterReq.remark as remark,sterReq.carrier as carrier "
					+ "FROM SterilizationRequest sterReq "
					+ "INNER JOIN sterReq.requestingStore as requestingStore "
					+ "INNER JOIN sterReq.issueingStore as issueingStore "
					+ "WHERE sterReq.createdDate BETWEEN :fromDate AND :toDate "
					+ "AND sterReq.organizationId=:organizationId AND sterReq.unitId=:unitId "+condition;
			
			StatelessSession statelessSession = sessionFactory.openStatelessSession();
			Query createQuery= statelessSession
						.createQuery(query)
						.setParameter("fromDate", fromDate)
						.setParameter("toDate", toDate)
						.setParameter("organizationId", organizationId)
						.setParameter("unitId", unitId);

			if(requestingStoreId!=null)
			{
				createQuery.setParameter("requestingStoreId", requestingStoreId);
			}
			if(issueingStoreId!=null)
			{
				createQuery.setParameter("issueingStoreId", issueingStoreId);
			}
			if(requestNo!=null)
			{
				createQuery.setParameter("requestNo", requestNo);
			}
			if(status!=null)
			{
				createQuery.setParameter("status", status);
			}



			List<SterilizationRequestDto> sterilizationRequestDtoList  =		createQuery.setResultTransformer(Transformers.aliasToBean(SterilizationRequestDto.class)).list();
			
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
									+ "(select distinct transitionMapper.transitionNumber from SterilizationRequestTransitionMapper transitionMapper "
									+ "where transitionMapper.requestNo=:requestNo ) ")
							.setParameter("requestNo",sterilizationRequestDto.getRequestNo())
							.setResultTransformer(Transformers.aliasToBean(SterilizationRequestTransitionMapperDto.class)).list();
					
					for (SterilizationRequestTransitionMapperDto sterilizationRequestTransitionMapperDto : kitList) {
						
						SterilizationRequestTransitionMapperDto transitionMapperDto = (SterilizationRequestTransitionMapperDto)statelessSession
								.createQuery("select mapper.id as id,mapper.status as status,mapper.rejectReasonId as rejectReasonId,"
										+ " rejectReasonNote as rejectReasonNote from SterilizationRequestTransitionMapper mapper "
										+ " where mapper.transitionNumber=:transitionNumber and mapper.requestNo=:requestNo ")
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
										+ "WHERE kitMaster.is_active=true AND kitMaster.is_deleted=false AND preSterilizationProcessInstrument.transitionNumber=:transitionNumber")
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
			logger.error("getSterilizationRequestByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response getSterilizationRequestForAcceptanceByMultipleFields(Date fromDate,Date toDate,Integer requestingStoreId,Integer issueingStoreId,Integer requestNo,String status,Integer organizationId,Integer unitId) {
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
			if(status!=null)
			{
				condition.append(" AND sterReq.status=:status ");
			}
			
			String query = "SELECT sterReq.requestNo as requestNo, "
					+ "to_char(sterReq.createdDate,'dd/MM/yyyy') as createdDate, "
					+ "sterReq.status as status,requestingStore.storeName as requestingStore,"
					+ "sterReq.requestingStoreId as requestingStoreId,sterReq.issueingStoreId as issueingStoreId,"
					+ "issueingStore.storeName as issueingStore,sterReq.remark as remark,sterReq.carrier as carrier "
					+ "FROM SterilizationRequest sterReq "
					+ "INNER JOIN sterReq.requestingStore as requestingStore "
					+ "INNER JOIN sterReq.issueingStore as issueingStore "
					+ "WHERE sterReq.createdDate BETWEEN :fromDate AND :toDate "
					+ "AND sterReq.organizationId=:organizationId AND sterReq.unitId=:unitId AND sterReq.status!='Cancelled' "+condition;
			
			StatelessSession statelessSession = sessionFactory.openStatelessSession();
		 Query createQuery=statelessSession
						.createQuery(query)
						.setParameter("fromDate", fromDate)
						.setParameter("toDate", toDate)
						.setParameter("organizationId", organizationId)
						.setParameter("unitId", unitId);


			if(requestingStoreId!=null)
			{
				createQuery.setParameter("requestingStoreId", requestingStoreId);
			}
			if(issueingStoreId!=null)
			{
				createQuery.setParameter("issueingStoreId", issueingStoreId);
			}
			if(requestNo!=null)
			{
				createQuery.setParameter("requestNo", requestNo);
			}
			if(status!=null)
			{
				createQuery.setParameter("status", status);
			}


			List<SterilizationRequestDto> sterilizationRequestDtoList  =createQuery.setResultTransformer(Transformers.aliasToBean(SterilizationRequestDto.class)).list();
			
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
									+ "(select distinct transitionMapper.transitionNumber from SterilizationRequestTransitionMapper transitionMapper "
									+ "where transitionMapper.requestNo=:requestNo ) ")
							.setParameter("requestNo",sterilizationRequestDto.getRequestNo())
							.setResultTransformer(Transformers.aliasToBean(SterilizationRequestTransitionMapperDto.class)).list();
					
					for (SterilizationRequestTransitionMapperDto sterilizationRequestTransitionMapperDto : kitList) {
						
						SterilizationRequestTransitionMapperDto transitionMapperDto = (SterilizationRequestTransitionMapperDto)statelessSession
								.createQuery("select mapper.id as id,mapper.status as status,mapper.rejectReasonId as rejectReasonId,"
										+ " rejectReasonNote as rejectReasonNote from SterilizationRequestTransitionMapper mapper "
										+ " where mapper.transitionNumber=:transitionNumber and mapper.requestNo=:requestNo ")
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
										+ "WHERE kitMaster.is_active=true AND kitMaster.is_deleted=false AND preSterilizationProcessInstrument.transitionNumber=:transitionNumber")
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
			logger.error("getSterilizationRequestForAcceptanceByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	
	@Override
	public Response getSterilizationRequestByRequestNo(Integer requestNo,Integer organizationId,Integer unitId) {
		try {
			String query = "SELECT sterReq.requestNo as requestNo, "
					+ "to_char(sterReq.createdDate,'dd/MM/yyyy') as createdDate, "
					+ "sterReq.status as status,requestingStore.storeName as requestingStore,"
					+ "sterReq.requestingStoreId as requestingStoreId,sterReq.issueingStoreId as issueingStoreId,"
					+ "issueingStore.storeName as issueingStore,sterReq.remark as remark,sterReq.carrier as carrier "
					+ "FROM SterilizationRequest sterReq "
					+ "INNER JOIN sterReq.requestingStore as requestingStore "
					+ "INNER JOIN sterReq.issueingStore as issueingStore "
					+ "WHERE sterReq.requestNo=:requestNo "
					+ "AND sterReq.organizationId=:organizationId AND sterReq.unitId=:unitId ";
			
			StatelessSession statelessSession = sessionFactory.openStatelessSession();
			SterilizationRequestDto sterilizationRequestDto  = (SterilizationRequestDto)statelessSession
						.createQuery(query)
						.setParameter("requestNo", requestNo)
						.setParameter("organizationId", organizationId)
						.setParameter("unitId", unitId)
						.setResultTransformer(Transformers.aliasToBean(SterilizationRequestDto.class)).uniqueResult();
			
			if(sterilizationRequestDto != null)
			{
				
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
								+ "(select distinct transitionMapper.transitionNumber from SterilizationRequestTransitionMapper transitionMapper "
								+ "where transitionMapper.requestNo=:requestNo ) ")
						.setParameter("requestNo",sterilizationRequestDto.getRequestNo())
						.setResultTransformer(Transformers.aliasToBean(SterilizationRequestTransitionMapperDto.class)).list();
				
				for (SterilizationRequestTransitionMapperDto sterilizationRequestTransitionMapperDto : kitList) {
					
					SterilizationRequestTransitionMapperDto transitionMapperDto = (SterilizationRequestTransitionMapperDto)statelessSession
							.createQuery("select mapper.id as id,mapper.status as status,mapper.rejectReasonId as rejectReasonId,"
									+ " rejectReasonNote as rejectReasonNote from SterilizationRequestTransitionMapper mapper "
									+ " where mapper.transitionNumber=:transitionNumber and mapper.requestNo=:requestNo ")
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
									+ "WHERE kitMaster.is_active=true AND kitMaster.is_deleted=false AND preSterilizationProcessInstrument.transitionNumber=:transitionNumber")
							.setParameter("transitionNumber", sterilizationRequestTransitionMapperDto.getTransitionNumber())
							
							.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessInstrumentDto.class)).list();
					
					sterilizationRequestTransitionMapperDto.setKitInstrumentList(kitInstrumentList);
				}
			sterilizationRequestDto.setKitList(kitList);
			}
					
			statelessSession.close();
			return new Response(SUCCESS, null, null, null, sterilizationRequestDto);
		} catch (Exception e) {
			logger.error("getSterilizationRequestByRequestNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response updateSterilizationRequest(SterilizationRequestDto sterilizationRequestDto ) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			SterilizationRequest sterilizationRequest = (SterilizationRequest)session.get(SterilizationRequest.class, sterilizationRequestDto.getRequestNo());
			sterilizationRequest.setCarrier(sterilizationRequestDto.getCarrier());
			sterilizationRequest.setRequestingStoreId(sterilizationRequestDto.getRequestingStoreId());
			sterilizationRequest.setIssueingStoreId(sterilizationRequestDto.getIssueingStoreId());
			sterilizationRequest.setRemark(sterilizationRequestDto.getRemark());
			sterilizationRequest.setUpdatedBy(sterilizationRequestDto.getUpdatedBy());
			
			sterilizationRequest.setUpdatedDate(TimeZoneComponent.getDateByzone(sterilizationRequestDto.getUnitId()));
			session.update(sterilizationRequest);
						
			String sterilizationRequestTransitionMapperDeleteQuery = "delete from SterilizationRequestTransitionMapper mapper where mapper.requestNo=:requestNo";
			
			session.createQuery(sterilizationRequestTransitionMapperDeleteQuery)
			.setParameter("requestNo", sterilizationRequestDto.getRequestNo())
			.executeUpdate();
				
			List<SterilizationRequestTransitionMapperDto> kitList = sterilizationRequestDto.getKitList();
			
			for (SterilizationRequestTransitionMapperDto sterilizationRequestTransitionMapperDto : kitList) {
				
				SterilizationRequestTransitionMapper transitionMapper = new SterilizationRequestTransitionMapper();
				transitionMapper.setRequestNo(sterilizationRequestDto.getRequestNo());
				transitionMapper.setTransitionNumber(sterilizationRequestTransitionMapperDto.getTransitionNumber());
				session.save(transitionMapper);
			}
			
			return new Response(SUCCESS, "200","Sterilization Request updated Successfully.", null, null);
			
		} catch (Exception e) {
			logger.error("updateSterilizationRequest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response updateSterilizationRequestStatus(List<SterilizationRequestDto> sterilizationRequestDtoList ) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			
			for (SterilizationRequestDto sterilizationRequestDto : sterilizationRequestDtoList) {
				
				SterilizationRequest sterilizationRequest = (SterilizationRequest) session.get(SterilizationRequest.class, sterilizationRequestDto.getRequestNo());
				sterilizationRequest.setStatus("Pending");
				session.update(sterilizationRequest);
			}
			
			return new Response(SUCCESS, "200","Sterilization Request updated successfully.", null, null);
			
		} catch (Exception e) {
			logger.error("updateSterilizationRequestStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response cancelSterilizationRequest(SterilizationRequestDto sterilizationRequestDto ) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			
				SterilizationRequest sterilizationRequest = (SterilizationRequest) session.get(SterilizationRequest.class, sterilizationRequestDto.getRequestNo());
				sterilizationRequest.setStatus("Cancelled");
				sterilizationRequest.setCancelReasonId(sterilizationRequestDto.getCancelReasonId());
				sterilizationRequest.setCancelReasonNote(sterilizationRequestDto.getCancelReasonNote());
				session.update(sterilizationRequest);
				
				Response resp = this.getSterilizationRequestByRequestNo(sterilizationRequest.getRequestNo(), sterilizationRequest.getOrganizationId(), sterilizationRequest.getUnitId());
				
				SterilizationRequestDto newSterilizationRequestDto = (SterilizationRequestDto) resp.getObject();
				List<SterilizationRequestTransitionMapperDto> kitList = newSterilizationRequestDto.getKitList();
				for (SterilizationRequestTransitionMapperDto sterilizationRequestTransitionMapperDto : kitList) {
					
					PreSterilizationProcess preSterilizationProcess = (PreSterilizationProcess)session.get(PreSterilizationProcess.class,sterilizationRequestTransitionMapperDto.getTransitionNumber());
					preSterilizationProcess.setSterilizationRequested(false);
					preSterilizationProcess.setSterilizationRequestNo(null);
					session.update(preSterilizationProcess);
					
				}
			
			
			return new Response(SUCCESS, "200","Issue cancelled Successfully.", null, null);
			
		} catch (Exception e) {
			logger.error("cancelSterilizationRequest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response acceptSterilizationRequest(SterilizationRequestDto sterilizationRequestDto){
	try {
			Session session=sessionFactory.getCurrentSession();
			
			
			SterilizationRequest sterilizationRequest = (SterilizationRequest) session.get(SterilizationRequest.class, sterilizationRequestDto.getRequestNo());
			sterilizationRequest.setStatus("Accepted");
			session.update(sterilizationRequest);
			
			List<SterilizationRequestTransitionMapperDto> kitList = sterilizationRequestDto.getKitList();
			
			for (SterilizationRequestTransitionMapperDto sterilizationRequestTransitionMapperDto : kitList) {
				
				if(sterilizationRequestTransitionMapperDto.getReject())
				{
					SterilizationRequestTransitionMapper transitionMapper = (SterilizationRequestTransitionMapper)
							session.get(SterilizationRequestTransitionMapper.class,sterilizationRequestTransitionMapperDto.getId());
					transitionMapper.setRejectReasonId(sterilizationRequestTransitionMapperDto.getRejectReasonId());
					transitionMapper.setRejectReasonNote(sterilizationRequestTransitionMapperDto.getRejectReasonNote());
					transitionMapper.setStatus("Rejected");
					session.update(transitionMapper);
				}
				else
				{
					SterilizationRequestTransitionMapper transitionMapper = (SterilizationRequestTransitionMapper)
							session.get(SterilizationRequestTransitionMapper.class,sterilizationRequestTransitionMapperDto.getId());
					transitionMapper.setStatus("Accepted");
					session.update(transitionMapper);
					
					PreSterilizationProcess preSterilizationProcess = (PreSterilizationProcess)session
							.get(PreSterilizationProcess.class, sterilizationRequestTransitionMapperDto.getTransitionNumber());
					
					preSterilizationProcess.setSterilizationRequestNo(sterilizationRequestDto.getRequestNo());
					preSterilizationProcess.setSterilizationRequested(true);
					
					session.update(preSterilizationProcess);
					
					/***********************************Start For Kit Movement Record**********************************/
					
					KitMovementRecord kitMovementRecord = new KitMovementRecord();
					kitMovementRecord.setTransitionNumber(sterilizationRequestTransitionMapperDto.getTransitionNumber());
					kitMovementRecord.setFromStoreId(sterilizationRequest.getRequestingStoreId());
					kitMovementRecord.setToStoreId(sterilizationRequest.getIssueingStoreId());
					kitMovementRecord.setMovedBy(sterilizationRequestDto.getCreatedBy());
					kitMovementRecord.setMovementDate(TimeZoneComponent.getDateByzone(sterilizationRequestDto.getUnitId()));
					kitMovementRecord.setMovementType("Request For Sterilization");
					kitMovementRecord.setOrganizationId(sterilizationRequestDto.getOrganizationId());
					kitMovementRecord.setUnitId(sterilizationRequestDto.getUnitId());
					
					session.save(kitMovementRecord);
					
					/***********************************End For Kit Movement Record**********************************/
					
					Response resp = iPreSterilizationProcessDao.getPreSterilizationProcessByTransitionNumber(sterilizationRequestDto.getOrganizationId(),sterilizationRequestDto.getUnitId(), sterilizationRequestTransitionMapperDto.getTransitionNumber());
					
					PreSterilizationProcessDto preSterilizationProcessDto = (PreSterilizationProcessDto) resp.getObject();
					
					PreSterilizationProcess preSterilizationProcessNew=new PreSterilizationProcess();
					preSterilizationProcessNew.setCreated_by(preSterilizationProcessDto.getCreated_by());
					preSterilizationProcessNew.setUpdated_by(preSterilizationProcessDto.getUpdated_by());
					preSterilizationProcessNew.setCreated_date(TimeZoneComponent.getDateByzone(sterilizationRequestDto.getUnitId()));
					preSterilizationProcessNew.setUpdated_date(TimeZoneComponent.getDateByzone(sterilizationRequestDto.getUnitId()));
					preSterilizationProcessNew.setStatus("Pending");
					preSterilizationProcessNew.setTransition("Cleaned");
					preSterilizationProcessNew.setKitId(preSterilizationProcessDto.getKitId());
					preSterilizationProcessNew.setOrganizationId(preSterilizationProcessDto.getOrganizationId());
					preSterilizationProcessNew.setUnitId(preSterilizationProcessDto.getUnitId());
					preSterilizationProcessNew.setIsIssued(false);
					preSterilizationProcessNew.setIsSterilized(false);
					preSterilizationProcessNew.setIsAccepted(false);
					preSterilizationProcessNew.setIsRejected(false);
					preSterilizationProcessNew.setIsReturned(false);
					preSterilizationProcessNew.setSterilizationRequested(false);
					preSterilizationProcessNew.setStoreId(sterilizationRequest.getIssueingStoreId());
					
					
					Integer transitionNumber =(Integer)session.save(preSterilizationProcessNew);
					
					
					if(!preSterilizationProcessDto.getKitInstrumentList().isEmpty())
					{
						for (PreSterilizationProcessInstrumentDto instrumentDto : preSterilizationProcessDto.getKitInstrumentList()) {
						
							PreSterilizationProcessInstrument preSterilizationProcessEquipment=new PreSterilizationProcessInstrument();
							preSterilizationProcessEquipment.setInstrumentId(instrumentDto.getInstrumentId());
							preSterilizationProcessEquipment.setKitId(preSterilizationProcessDto.getKitId());
							preSterilizationProcessEquipment.setTransitionNumber(transitionNumber);
							preSterilizationProcessEquipment.setKitInstrumentId(instrumentDto.getKitInstrumentId());
							preSterilizationProcessEquipment.setStatus(instrumentDto.getStatus());
							preSterilizationProcessEquipment.setCreated_by(preSterilizationProcessDto.getCreated_by());
							preSterilizationProcessEquipment.setUpdated_by(preSterilizationProcessDto.getUpdated_by());
							preSterilizationProcessEquipment.setCreated_date(TimeZoneComponent.getDateByzone(sterilizationRequestDto.getUnitId()));
							preSterilizationProcessEquipment.setUpdated_date(TimeZoneComponent.getDateByzone(sterilizationRequestDto.getUnitId()));
							session.save(preSterilizationProcessEquipment);
							
						}
					}	
				}
				
				
			}
			
			return new Response(SUCCESS, "200","Sterilization Request Accepted Successfully.", null, null);
			
		} catch (Exception e) {
			logger.error("acceptSterilizationRequest failed", e);
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

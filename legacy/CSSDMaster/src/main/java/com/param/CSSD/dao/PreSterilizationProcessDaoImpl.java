package com.param.CSSD.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.CSSD.dto.PreSterilizationProcessDto;
import com.param.CSSD.dto.PreSterilizationProcessInstrumentDto;
import com.param.CSSD.model.CssdOpeningBalanceDetailStock;
import com.param.CSSD.model.CssdStockTransactionLog;
import com.param.CSSD.model.PreSterilizationProcess;
import com.param.CSSD.model.PreSterilizationProcessInstrument;
import com.param.CSSD.service.IPreSterilizationProcessService;
import com.param.CSSDMaster.dto.CssdKitDto;
import com.param.CSSDMaster.dto.WrapperExpiryMasterDto;
import com.param.CSSDMaster.model.KitInstrumentMapper;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.SlotMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PreSterilizationProcessDaoImpl extends GenericDao<PreSterilizationProcess, Integer>
		implements IPreSterilizationProcessDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(PreSterilizationProcessDaoImpl.class);


	public PreSterilizationProcessDaoImpl() {
		super(PreSterilizationProcess.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private DozerBeanMapper mapper;
	
	@Autowired
	private IPreSterilizationProcessService iPreSterilizationProcessService;

	@Override
	public Response saveCleanedWorklist(PreSterilizationProcessDto preSterilizationProcessDto) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			
			Session session=sessionFactory.getCurrentSession();
			
			PreSterilizationProcess preSterilizationProcess=new PreSterilizationProcess();
			preSterilizationProcess.setCreated_by(preSterilizationProcessDto.getCreated_by());
			preSterilizationProcess.setUpdated_by(preSterilizationProcessDto.getUpdated_by());
			preSterilizationProcess.setCreated_date(TimeZoneComponent.getDateByzone(preSterilizationProcessDto.getUnitId()));
			preSterilizationProcess.setUpdated_date(TimeZoneComponent.getDateByzone(preSterilizationProcessDto.getUnitId()));
			preSterilizationProcess.setStatus("Completed");
			preSterilizationProcess.setTransition("Cleaned");
			preSterilizationProcess.setKitId(preSterilizationProcessDto.getKitId());
			preSterilizationProcess.setOrganizationId(preSterilizationProcessDto.getOrganizationId());
			preSterilizationProcess.setUnitId(preSterilizationProcessDto.getUnitId());
			preSterilizationProcess.setStoreId(preSterilizationProcessDto.getStoreId());
			preSterilizationProcess.setIsIssued(false);
			preSterilizationProcess.setIsSterilized(false);
			preSterilizationProcess.setIsAccepted(false);
			preSterilizationProcess.setIsRejected(false);
			preSterilizationProcess.setIsReturned(false);
			preSterilizationProcess.setSterilizationRequested(false);
			preSterilizationProcess.setIsSterilizeFromOpeningStock('N');
			
			
			Integer transitionNumber =(Integer)session.save(preSterilizationProcess);
			
			
			if(!preSterilizationProcessDto.getKitInstrumentList().isEmpty())
			{
				for (PreSterilizationProcessInstrumentDto instrumentDto : preSterilizationProcessDto.getKitInstrumentList()) {
				
					PreSterilizationProcessInstrument preSterilizationProcessEquipment=new PreSterilizationProcessInstrument();
					preSterilizationProcessEquipment.setInstrumentId(instrumentDto.getInstrumentId());
					preSterilizationProcessEquipment.setKitId(preSterilizationProcessDto.getKitId());
					preSterilizationProcessEquipment.setStoreId(preSterilizationProcessDto.getStoreId());
					preSterilizationProcessEquipment.setTransitionNumber(transitionNumber);
					preSterilizationProcessEquipment.setKitInstrumentId(instrumentDto.getKitInstrumentId());
					preSterilizationProcessEquipment.setStatus(instrumentDto.getStatus());
					preSterilizationProcessEquipment.setCreated_by(preSterilizationProcessDto.getCreated_by());
					preSterilizationProcessEquipment.setUpdated_by(preSterilizationProcessDto.getUpdated_by());
					preSterilizationProcessEquipment.setCreated_date(TimeZoneComponent.getDateByzone(preSterilizationProcessDto.getUnitId()));
					preSterilizationProcessEquipment.setUpdated_date(TimeZoneComponent.getDateByzone(preSterilizationProcessDto.getUnitId()));
					session.save(preSterilizationProcessEquipment);
					
				}
			}
			statelessSession.close();
			return new Response(SUCCESS, null, "Saved Successfully.", null, null);
		} catch (HibernateException he) {
			logger.error("saveCleanedWorklist failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveCleanedWorklist failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}
	
	
	@Override
	public Response getKitInstrumentId(Integer kitId, Integer instrumentId) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();

			String query = "SELECT mapper.kitInstrumentId as id from KitInstrumentMapper mapper where mapper.kitId=:kitId and mapper.instrumentId=:instId";
			
			Integer kitInstId = (Integer)session
					.createQuery(query)
					.setParameter("kitId", kitId)
					.setParameter("instId", instrumentId)
					.uniqueResult();
			
			
			return new Response(SUCCESS, null, "Saved Successfully.", null, kitInstId);
		} catch (HibernateException he) {
			logger.error("getKitInstrumentId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getKitInstrumentId failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}
	
	@Override
	public Response getCleanedWorklistByMultipleFields(Date from,Date to,String name,String status,Integer organizationId,Integer unitId,Integer storeId) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			StringBuilder condition=new StringBuilder();
			
			if(name!=null)
			{
				condition.append(" AND kitMaster.name=:name ");
			}
			if(status!=null)
			{
				condition.append(" AND preSterilizationProcess.status=:status ");
			}
			
			String query = "SELECT preSterilizationProcess.transitionNumber as transitionNumber, "
					+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as code, " + "kitMaster.name as name, "
					+ "to_char(preSterilizationProcess.created_date,'dd/MM/yyyy') as created_date, preSterilizationProcess.status as status " 
					+ "FROM PreSterilizationProcess preSterilizationProcess INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
					+ "WHERE  "
					//+ "preSterilizationProcess.created_date BETWEEN :from AND :to  AND  preSterilizationProcess.status='Completed' "
					+ " to_char(preSterilizationProcess.created_date,'yyyy-MM-dd') BETWEEN :from AND :to  "
					+ "AND preSterilizationProcess.transition='Cleaned' "
					+ "and preSterilizationProcess.organizationId=:organizationId and preSterilizationProcess.unitId=:unitId "
					+ "and preSterilizationProcess.storeId=:storeId "+condition;
					
			
			Query createQuery=statelessSession
					.createQuery(query)
					.setParameter("from",GlobalCommonDateUtils.getStringDate(from, "yyyy-M-dd") )
					.setParameter("to",GlobalCommonDateUtils.getStringDate(to, "yyyy-M-dd") )
					//.setParameter("to", to)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setParameter("storeId", storeId);

			if(name!=null)
			{
				createQuery.setParameter("name", name);
			}
			if(status!=null)
			{
				createQuery.setParameter("status", status);
			}


			List<PreSterilizationProcessDto> preSterilizationProcessDtoList = createQuery.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class)).list();
			
			for (PreSterilizationProcessDto preSterilizationProcessDto : preSterilizationProcessDtoList) {
			
				List<PreSterilizationProcessInstrumentDto> kitInstrumentList = statelessSession
						.createQuery("SELECT preSterilizationProcessInstrument.id as id, "
								+ "preSterilizationProcessInstrument.kitId as kitId, "
								+ "preSterilizationProcessInstrument.instrumentId as instrumentId,"
								+ "iteam.code as instrumentCode,"
								+ "iteam.name as instrumentName," 
								+ "preSterilizationProcessInstrument.status as status, "
								+ "kitInstrumentMapper.quantity as quantity " 
								+ "FROM PreSterilizationProcessInstrument preSterilizationProcessInstrument "
								+ "INNER JOIN preSterilizationProcessInstrument.kitMaster as kitMaster "
								+ "INNER JOIN preSterilizationProcessInstrument.instrumentMaster as iteam "
								+ "INNER JOIN preSterilizationProcessInstrument.kitInstrumentMapper as kitInstrumentMapper "
								+ "WHERE preSterilizationProcessInstrument.transitionNumber=:transitionNumber")
						.setParameter("transitionNumber", preSterilizationProcessDto.getTransitionNumber())
						
						.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessInstrumentDto.class)).list();
				
				preSterilizationProcessDto.setKitInstrumentList(kitInstrumentList);
				
			}
			
			statelessSession.close();
			return new Response(SUCCESS, null, null, preSterilizationProcessDtoList, null);
		} catch (HibernateException he) {
			logger.error("getCleanedWorklistByMultipleFields failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getCleanedWorklistByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}
	
	@Override
	public Response getPendingForCleaningWorklistByMultipleFields(Date from,Date to,String name,Integer organizationId,Integer unitId,Integer storeId) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			StringBuilder condition=new StringBuilder();
			
			if(name!=null)
			{
				condition.append(" AND kitMaster.name=:name ");
			}
			
			String query = "SELECT preSterilizationProcess.transitionNumber as transitionNumber, "
					+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as code, " + "kitMaster.name as name, "
					+ "to_char(preSterilizationProcess.created_date,'yyyy-MM-dd') as created_date, preSterilizationProcess.status as status " 
					+ "FROM PreSterilizationProcess preSterilizationProcess INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
					+ "WHERE  "
					+ "to_char(preSterilizationProcess.created_date,'dd-MM-yyyy') BETWEEN :from AND :to  AND "
					+ "preSterilizationProcess.status='Pending' AND preSterilizationProcess.transition='Cleaned' "
					+ "and preSterilizationProcess.organizationId=:organizationId and preSterilizationProcess.unitId=:unitId "
					+ "and preSterilizationProcess.storeId=:storeId "+condition;
					
			
		Query createQuery= statelessSession
					.createQuery(query)
					//.setParameter("from", from)
					//.setParameter("to", to)
					.setParameter("from",GlobalCommonDateUtils.getStringDate(from, "yyyy-M-dd") )
					.setParameter("to",GlobalCommonDateUtils.getStringDate(to, "yyyy-M-dd") )
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setParameter("storeId", storeId);

			if(name!=null)
			{
				createQuery.setParameter("name", name);
			}


			List<PreSterilizationProcessDto> preSterilizationProcessDtoList =createQuery.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class)).list();
			
			for (PreSterilizationProcessDto preSterilizationProcessDto : preSterilizationProcessDtoList) {
			
				List<PreSterilizationProcessInstrumentDto> kitInstrumentList = statelessSession
						.createQuery("SELECT preSterilizationProcessInstrument.id as id, "
								+ "preSterilizationProcessInstrument.kitId as kitId, "
								+ "preSterilizationProcessInstrument.instrumentId as instrumentId,"
								+ "iteam.code as instrumentCode,"
								+ "iteam.name as instrumentName," 
								+ "preSterilizationProcessInstrument.status as status, "
								+ "kitInstrumentMapper.quantity as quantity " 
								+ "FROM PreSterilizationProcessInstrument preSterilizationProcessInstrument "
								+ "INNER JOIN preSterilizationProcessInstrument.kitMaster as kitMaster "
								+ "INNER JOIN preSterilizationProcessInstrument.instrumentMaster as iteam "
								+ "INNER JOIN preSterilizationProcessInstrument.kitInstrumentMapper as kitInstrumentMapper "
								+ "WHERE  preSterilizationProcessInstrument.transitionNumber=:transitionNumber")
						.setParameter("transitionNumber", preSterilizationProcessDto.getTransitionNumber())
						
						.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessInstrumentDto.class)).list();
				
				preSterilizationProcessDto.setKitInstrumentList(kitInstrumentList);
				
			}
			
			statelessSession.close();
			return new Response(SUCCESS, null, null, preSterilizationProcessDtoList, null);
		} catch (HibernateException he) {
			logger.error("getPendingForCleaningWorklistByMultipleFields failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPendingForCleaningWorklistByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}
	
	@Override
	public Response getInspectedWorklistByMultipleFields(Date from,Date to,String name,String status,Integer organizationId,Integer unitId,Integer storeId) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			StringBuilder condition=new StringBuilder();
			
			if(name!=null)
			{
				condition.append(" AND kitMaster.name=:name ");
			}
			if(status!=null)
			{
				
				if(status.equals("Pending"))
				{
					condition.append(" AND ( preSterilizationProcess.transition='Inspected' AND preSterilizationProcess.status=:status ) OR "
									+" (preSterilizationProcess.status='Completed' AND preSterilizationProcess.transition='Cleaned' )");
				}
				else
				{
					condition.append(" AND (preSterilizationProcess.transition='Inspected' AND preSterilizationProcess.status=:status )");
				}
			}
			if(status == null)
			{
				condition.append(" AND ( ( preSterilizationProcess.transition='Inspected' AND preSterilizationProcess.status='Completed') "
						+ " OR (preSterilizationProcess.status='Completed' AND preSterilizationProcess.transition='Cleaned')"
						+ " OR  (preSterilizationProcess.status='Pending' AND preSterilizationProcess.transition='Inspected') ) ");
			}
			
			String query = "SELECT preSterilizationProcess.transitionNumber as transitionNumber, "
					+ "preSterilizationProcess.kitId as kitId,preSterilizationProcess.transition as transition, " + "kitMaster.code as code, " + "kitMaster.name as name, "
					+ "to_char(preSterilizationProcess.created_date,'dd/MM/yyyy') as created_date, preSterilizationProcess.status as status " 
					+ "FROM PreSterilizationProcess preSterilizationProcess INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
					+ "WHERE  "
					+ "preSterilizationProcess.created_date BETWEEN :from AND :to "
					+ "and preSterilizationProcess.organizationId=:organizationId and preSterilizationProcess.unitId=:unitId and preSterilizationProcess.storeId=:storeId "+condition;
			
			Query createQuery= statelessSession
					.createQuery(query)
					.setParameter("from", from)
					.setParameter("to", to)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setParameter("storeId", storeId);

			if(name!=null)
			{
				createQuery.setParameter("name", name);
			}
			if(status!=null)
			{
				if(status.equals("Pending"))
				{
					createQuery.setParameter("status", status);
				}
				else
				{
					createQuery.setParameter("status", status);
				}
			}


			List<PreSterilizationProcessDto> preSterilizationProcessDtoList =createQuery.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class)).list();
			
			for (PreSterilizationProcessDto preSterilizationProcessDto : preSterilizationProcessDtoList) {
			
				List<PreSterilizationProcessInstrumentDto> kitInstrumentList = statelessSession
						.createQuery("SELECT preSterilizationProcessInstrument.id as id, "
								+ "preSterilizationProcessInstrument.kitId as kitId, "
								+ "preSterilizationProcessInstrument.instrumentId as instrumentId,"
								+ "iteam.code as instrumentCode,"
								+ "iteam.name as instrumentName," 
								+ "preSterilizationProcessInstrument.status as status, "
								+ "kitInstrumentMapper.quantity as quantity " 
								+ "FROM PreSterilizationProcessInstrument preSterilizationProcessInstrument "
								+ "INNER JOIN preSterilizationProcessInstrument.kitMaster as kitMaster "
								+ "INNER JOIN preSterilizationProcessInstrument.instrumentMaster as iteam "
								+ "INNER JOIN preSterilizationProcessInstrument.kitInstrumentMapper as kitInstrumentMapper "
								+ "WHERE preSterilizationProcessInstrument.transitionNumber=:transitionNumber")
						.setParameter("transitionNumber", preSterilizationProcessDto.getTransitionNumber())
						
						.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessInstrumentDto.class)).list();
				
				preSterilizationProcessDto.setKitInstrumentList(kitInstrumentList);
				
			}
		
			for (PreSterilizationProcessDto preSterilizationProcessDto : preSterilizationProcessDtoList) {
					if(preSterilizationProcessDto != null && preSterilizationProcessDto.getTransition().equals("Cleaned"))
					{
						preSterilizationProcessDto.setStatus("Pending");
					}
				}
		
				
			statelessSession.close();
			return new Response(SUCCESS, null, null, preSterilizationProcessDtoList, null);
		} catch (HibernateException he) {
			logger.error("getInspectedWorklistByMultipleFields failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getInspectedWorklistByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}
	
	@Override
	public Response getDisinfectedWorklistByMultipleFields(Date from,Date to,String name,String status,Integer organizationId,Integer unitId,Integer storeId) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			StringBuilder condition=new StringBuilder();
			
			if(name!=null)
			{
				condition.append(" AND kitMaster.name=:name ");
			}
			if(status!=null)
			{
				if(status.equals("Pending"))
				{

					condition.append(" AND ( (preSterilizationProcess.transition = 'Disinfected' AND preSterilizationProcess.status = :status) " +
							"OR (preSterilizationProcess.status = 'Completed' AND preSterilizationProcess.transition = 'Inspected') )");

				}
				else
				{
					condition.append(" AND (preSterilizationProcess.transition='Disinfected' AND preSterilizationProcess.status=:status )" );
				}
			}
			
			if(status == null)
			{
				condition.append(" AND ( ( preSterilizationProcess.transition='Disinfected' AND preSterilizationProcess.status='Completed') "
						+ " OR (preSterilizationProcess.status='Completed' AND preSterilizationProcess.transition='Inspected')"
						+ " OR  (preSterilizationProcess.status='Pending' AND preSterilizationProcess.transition='Disinfected') ) ");
			}
			
			String query = "SELECT preSterilizationProcess.transitionNumber as transitionNumber, "
					+ "preSterilizationProcess.kitId as kitId,preSterilizationProcess.transition as transition, " + "kitMaster.code as code, " + "kitMaster.name as name, "
					+ "to_char(preSterilizationProcess.created_date,'dd/MM/yyyy') as created_date, preSterilizationProcess.status as status " 
					+ "FROM PreSterilizationProcess preSterilizationProcess INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
					+ "WHERE "
					+ "preSterilizationProcess.created_date BETWEEN :from AND :to  "
					+ "and preSterilizationProcess.organizationId=:organizationId and preSterilizationProcess.unitId=:unitId and preSterilizationProcess.storeId=:storeId "+condition;
					
			
			Query createQuery = statelessSession
					.createQuery(query)
					.setParameter("from", from)
					.setParameter("to", to)
					.setParameter("organizationId",organizationId)
					.setParameter("unitId", unitId)
					.setParameter("storeId", storeId);

			if(name!=null)
			{
				createQuery.setParameter("name", name);
			}
			if(status!=null)
			{
				if(status.equals("Pending"))
				{
					createQuery.setParameter("status", status);
				}
				else
				{
					createQuery.setParameter("status", status);
				}
			}

			List<PreSterilizationProcessDto> preSterilizationProcessDtoList =createQuery.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class)).list();
			
			for (PreSterilizationProcessDto preSterilizationProcessDto : preSterilizationProcessDtoList) {
			
				List<PreSterilizationProcessInstrumentDto> kitInstrumentList = statelessSession
						.createQuery("SELECT preSterilizationProcessInstrument.id as id, "
								+ "preSterilizationProcessInstrument.kitId as kitId, "
								+ "preSterilizationProcessInstrument.instrumentId as instrumentId,"
								+ "iteam.code as instrumentCode,"
								+ "iteam.name as instrumentName," 
								+ "preSterilizationProcessInstrument.status as status, "
								+ "kitInstrumentMapper.quantity as quantity " 
								+ "FROM PreSterilizationProcessInstrument preSterilizationProcessInstrument "
								+ "INNER JOIN preSterilizationProcessInstrument.kitMaster as kitMaster "
								+ "INNER JOIN preSterilizationProcessInstrument.instrumentMaster as iteam "
								+ "INNER JOIN preSterilizationProcessInstrument.kitInstrumentMapper as kitInstrumentMapper "
								+ "WHERE  preSterilizationProcessInstrument.transitionNumber=:transitionNumber")
						.setParameter("transitionNumber", preSterilizationProcessDto.getTransitionNumber())
						
						.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessInstrumentDto.class)).list();
				
				preSterilizationProcessDto.setKitInstrumentList(kitInstrumentList);
				
			}
			
			for (PreSterilizationProcessDto preSterilizationProcessDto : preSterilizationProcessDtoList) {
				if(preSterilizationProcessDto != null && preSterilizationProcessDto.getTransition().equals("Inspected"))
				{
					preSterilizationProcessDto.setStatus("Pending");
				}
			}
			
			statelessSession.close();
			return new Response(SUCCESS, null, null, preSterilizationProcessDtoList, null);
		} catch (HibernateException he) {
			logger.error("getDisinfectedWorklistByMultipleFields failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getDisinfectedWorklistByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}
	
	@Override
	public Response getPreSterilizationProcessByTransitionNumber(Integer organizationId,Integer unitId,Integer transitionNumber) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			
			String query = " SELECT preSterilizationProcess.transitionNumber as transitionNumber, "
					+ " preSterilizationProcess.kitId as kitId,preSterilizationProcess.created_by as created_by,"
					+ " preSterilizationProcess.updated_by as updated_by, preSterilizationProcess.organizationId as organizationId,"
					+ " preSterilizationProcess.unitId as unitId, " 
					+ " kitMaster.code as code, " + "kitMaster.name as name, "
					+ " to_char(preSterilizationProcess.created_date,'dd/MM/yyyy') as created_date, preSterilizationProcess.status as status,"
					+ " preSterilizationProcess.transition as transition " 
					+ " FROM PreSterilizationProcess preSterilizationProcess "
					+ "INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
					+ " WHERE "
					+ " preSterilizationProcess.transitionNumber=:transitionNumber "
					+ " and preSterilizationProcess.organizationId=:organizationId and preSterilizationProcess.unitId=:unitId ";
			PreSterilizationProcessDto preSterilizationProcessDto = (PreSterilizationProcessDto)statelessSession
					.createQuery(query)
					.setParameter("transitionNumber",transitionNumber)
					.setParameter("organizationId",organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class)).uniqueResult();
			
			if (preSterilizationProcessDto != null) {
			
				List<PreSterilizationProcessInstrumentDto> kitInstrumentList = statelessSession
						.createQuery("SELECT preSterilizationProcessInstrument.id as id, "
								+ "preSterilizationProcessInstrument.kitId as kitId, "
								+ "preSterilizationProcessInstrument.instrumentId as instrumentId,"
								+ "preSterilizationProcessInstrument.kitInstrumentId as kitInstrumentId,"
								+ "iteam.code as instrumentCode,"
								+ "iteam.name as instrumentName," 
								+ "preSterilizationProcessInstrument.status as status, "
								+ "kitInstrumentMapper.quantity as quantity " 
								+ "FROM PreSterilizationProcessInstrument preSterilizationProcessInstrument "
								+ "INNER JOIN preSterilizationProcessInstrument.kitMaster as kitMaster "
								+ "INNER JOIN preSterilizationProcessInstrument.instrumentMaster as iteam "
								+ "INNER JOIN preSterilizationProcessInstrument.kitInstrumentMapper as kitInstrumentMapper "
								+ "WHERE preSterilizationProcessInstrument.transitionNumber=:transitionNumber")
						.setParameter("transitionNumber", preSterilizationProcessDto.getTransitionNumber())
						
						.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessInstrumentDto.class)).list();
				
				preSterilizationProcessDto.setKitInstrumentList(kitInstrumentList);
				
			}
			
			statelessSession.close();
			return new Response(SUCCESS, null, null, null, preSterilizationProcessDto);
		} catch (HibernateException he) {
			logger.error("getPreSterilizationProcessByTransitionNumber failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPreSterilizationProcessByTransitionNumber failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}
	
	
	@Override
	public Response formKit(PreSterilizationProcessDto preSterilizationProcessDto) {
		try {
			Session session=sessionFactory.getCurrentSession();
			PreSterilizationProcess preSterilizationProcess=(PreSterilizationProcess)session.get(PreSterilizationProcess.class, preSterilizationProcessDto.getTransitionNumber());
			preSterilizationProcess.setWrapperId(preSterilizationProcessDto.getWrapperId());
			preSterilizationProcess.setWrapperQuantity(preSterilizationProcessDto.getWrapperQuantity());
			preSterilizationProcess.setStatus("Completed");
			preSterilizationProcess.setTransition("KitFormed");
			preSterilizationProcess.setFormation_date(TimeZoneComponent.getDateByzone(preSterilizationProcessDto.getUnitId()!=null? preSterilizationProcessDto.getUnitId() :0));
			session.update(preSterilizationProcess);
			
			List<PreSterilizationProcessInstrumentDto> instrumentList=preSterilizationProcessDto.getKitInstrumentList();
			if(!instrumentList.isEmpty())
			{
				for (PreSterilizationProcessInstrumentDto preSterilizationProcessEquipmentDto : instrumentList) {
					PreSterilizationProcessInstrument preSterilizationProcessEquipment =(PreSterilizationProcessInstrument)session.get(PreSterilizationProcessInstrument.class,preSterilizationProcessEquipmentDto.getId());
					preSterilizationProcessEquipment.setPhysicalQuantity(preSterilizationProcessEquipmentDto.getPhysicalQuantity());
					preSterilizationProcessEquipment.setStatus("KitFormed");
					session.update(preSterilizationProcessEquipment);
				}
			}
			
			
			return new Response(SUCCESS, SUCCESS_CODE, "Kit Formed Successfully.", null, null);
		} catch (Exception e) {
			logger.error("formKit failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getKitFormationWorklistByMultipleFields(Date from,Date to,String name,String status,Integer organizationId,Integer unitId,Integer storeId) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			StringBuilder condition=new StringBuilder();
			
			if(name!=null)
			{
				condition.append(" AND kitMaster.name=:name ");
			}
			if(status!=null)
			{
				
				if(status.equals("Pending"))
				{
					condition.append(" AND ( preSterilizationProcess.transition='KitFormed' AND preSterilizationProcess.status=:status ) OR "
									+" (preSterilizationProcess.status='Completed' AND preSterilizationProcess.transition='Disinfected' )");
				}
				else
				{
					condition.append(" AND (preSterilizationProcess.transition='KitFormed' AND preSterilizationProcess.status=:status )");
				}
			}
			if(status == null)
			{
				/*condition.append(" AND ( ( preSterilizationProcess.transition='KitFormed' AND preSterilizationProcess.status='Completed') "
						+ " OR (preSterilizationProcess.status='Completed' AND preSterilizationProcess.transition='Disinfected')"
						+ " OR  (preSterilizationProcess.status='Pending' AND preSterilizationProcess.transition='Inspected') ) ");*/
				
				condition.append(" AND ( ( preSterilizationProcess.transition='KitFormed' AND preSterilizationProcess.status='Completed') "
						+ " OR (preSterilizationProcess.status='Completed' AND preSterilizationProcess.transition='Disinfected')"
						+ "  ) ");
			}
			
			String query = "SELECT preSterilizationProcess.transitionNumber as transitionNumber, "
					+ "preSterilizationProcess.kitId as kitId,preSterilizationProcess.transition as transition, " + "kitMaster.code as code, " + "kitMaster.name as name, "
					+ "to_char(preSterilizationProcess.created_date,'dd/MM/yyyy') as created_date,preSterilizationProcess.wrapperId as wrapperId, "
					+ "preSterilizationProcess.wrapperQuantity as wrapperQuantity, "
					+ "preSterilizationProcess.status as status, to_char(preSterilizationProcess.formation_date,'dd/MM/yyyy') as formation_date " 
					+ "FROM PreSterilizationProcess preSterilizationProcess INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
					+ "WHERE  "
					+ "preSterilizationProcess.created_date BETWEEN :from AND :to "
					+ "and preSterilizationProcess.organizationId=:organizationId and preSterilizationProcess.unitId=:unitId "
					+ "and preSterilizationProcess.storeId=:storeId and preSterilizationProcess.isIssued=false "
					+ "and preSterilizationProcess.isSterilized=false "+condition;
			
			 Query createQuery=statelessSession
					.createQuery(query)
					.setParameter("from", from)
					.setParameter("to", to)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setParameter("storeId", storeId);

			if(name!=null)
			{
				createQuery.setParameter("name", name);
			}
			if(status!=null)
			{

				if(status.equals("Pending")) {
					createQuery.setParameter("status", status);
				}
				else
				{
					createQuery.setParameter("status", status);
				}
			}




			List<PreSterilizationProcessDto> preSterilizationProcessDtoList = createQuery.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class)).list();
			
			for (PreSterilizationProcessDto preSterilizationProcessDto : preSterilizationProcessDtoList) {
			
				List<PreSterilizationProcessInstrumentDto> kitInstrumentList = statelessSession
						.createQuery("SELECT preSterilizationProcessInstrument.id as id, "
								+ "preSterilizationProcessInstrument.kitId as kitId, "
								+ "preSterilizationProcessInstrument.instrumentId as instrumentId,"
								+ "iteam.code as instrumentCode,"
								+ "iteam.name as instrumentName," 
								+ "preSterilizationProcessInstrument.status as status, "
								+ "kitInstrumentMapper.quantity as quantity " 
								+ "FROM PreSterilizationProcessInstrument preSterilizationProcessInstrument "
								+ "INNER JOIN preSterilizationProcessInstrument.kitMaster as kitMaster "
								+ "INNER JOIN preSterilizationProcessInstrument.instrumentMaster as iteam "
								+ "INNER JOIN preSterilizationProcessInstrument.kitInstrumentMapper as kitInstrumentMapper "
								+ "WHERE  preSterilizationProcessInstrument.transitionNumber=:transitionNumber")
						.setParameter("transitionNumber", preSterilizationProcessDto.getTransitionNumber())
						
						.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessInstrumentDto.class)).list();
				
				preSterilizationProcessDto.setKitInstrumentList(kitInstrumentList);
				
				
				if(preSterilizationProcessDto.getWrapperId() != null )
				{
					WrapperExpiryMasterDto wrapper = (WrapperExpiryMasterDto)statelessSession
							.createQuery("SELECT wrapperExpiryMaster.id as id, "
									+ "wrapperExpiryMaster.code as code,wrapperExpiryMaster.status as status, " + "wrapperExpiryMaster.name as name, "
									+ "wrapperExpiryMaster.duration as duration " 
									+ "FROM WrapperExpiryMaster as wrapperExpiryMaster "
									+ "WHERE wrapperExpiryMaster.id=:wrapperId  ")
							.setParameter("wrapperId",preSterilizationProcessDto.getWrapperId())
							.setResultTransformer(Transformers.aliasToBean(WrapperExpiryMasterDto.class)).uniqueResult();
					
					wrapper.setWrapperQuantity(preSterilizationProcessDto.getWrapperQuantity());
					preSterilizationProcessDto.setWrapper(wrapper);
							
				}
				
				
			}
		
			for (PreSterilizationProcessDto preSterilizationProcessDto : preSterilizationProcessDtoList) {
					if(preSterilizationProcessDto != null && preSterilizationProcessDto.getTransition().equals("Disinfected"))
					{
						preSterilizationProcessDto.setStatus("Pending");
					}
				}
		
				
			statelessSession.close();
			return new Response(SUCCESS, null, null, preSterilizationProcessDtoList, null);
		} catch (HibernateException he) {
			logger.error("getKitFormationWorklistByMultipleFields failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getKitFormationWorklistByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}
	
	
	@Override
	public Response getKitFormationWorklistByTransitionNumber(Integer organizationId,Integer unitId,Integer storeId,Integer transitionNumber) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			
			String query = "SELECT preSterilizationProcess.transitionNumber as transitionNumber, "
					+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as code, " + "kitMaster.name as name, "
					+ "to_char(preSterilizationProcess.created_date,'dd/MM/yyyy') as created_date, preSterilizationProcess.status as status, "
					+ "preSterilizationProcess.wrapperId as wrapperId, preSterilizationProcess.wrapperQuantity as wrapperQuantity, "
					+ "preSterilizationProcess.transition as transition,to_char(preSterilizationProcess.formation_date,'dd/MM/yyyy') as formation_date " 
					+ "FROM PreSterilizationProcess preSterilizationProcess INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
					+ "WHERE "
					+ "preSterilizationProcess.transitionNumber=:transitionNumber "
					+ "and preSterilizationProcess.organizationId=:organizationId and preSterilizationProcess.unitId=:unitId and preSterilizationProcess.storeId=:storeId ";
			PreSterilizationProcessDto preSterilizationProcessDto = (PreSterilizationProcessDto)statelessSession
					.createQuery(query)
					.setParameter("transitionNumber",transitionNumber)
					.setParameter("organizationId",organizationId)
					.setParameter("unitId", unitId)
					.setParameter("storeId", storeId)
					.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class)).uniqueResult();
			
			if (preSterilizationProcessDto != null) {
			
				List<PreSterilizationProcessInstrumentDto> kitInstrumentList = statelessSession
						.createQuery("SELECT preSterilizationProcessInstrument.id as id, "
								+ "preSterilizationProcessInstrument.kitId as kitId, "
								+ "preSterilizationProcessInstrument.instrumentId as instrumentId,"
								+ "iteam.code as instrumentCode,"
								+ "iteam.name as instrumentName," 
								+ "preSterilizationProcessInstrument.status as status, "
								+ "preSterilizationProcessInstrument.physicalQuantity as physicalQuantity, "
								+ "kitInstrumentMapper.quantity as quantity "
								+ "FROM PreSterilizationProcessInstrument preSterilizationProcessInstrument "
								+ "INNER JOIN preSterilizationProcessInstrument.kitMaster as kitMaster "
								+ "INNER JOIN preSterilizationProcessInstrument.instrumentMaster as iteam "
								+ "INNER JOIN preSterilizationProcessInstrument.kitInstrumentMapper as kitInstrumentMapper "
								+ "WHERE  preSterilizationProcessInstrument.transitionNumber=:transitionNumber")
						.setParameter("transitionNumber", preSterilizationProcessDto.getTransitionNumber())
						
						.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessInstrumentDto.class)).list();
				
				preSterilizationProcessDto.setKitInstrumentList(kitInstrumentList);
				
				if(preSterilizationProcessDto.getWrapperId() != null )
				{
					WrapperExpiryMasterDto wrapper = (WrapperExpiryMasterDto)statelessSession
							.createQuery("SELECT wrapperExpiryMaster.id as id, "
									+ "wrapperExpiryMaster.code as code,wrapperExpiryMaster.status as status, " + "wrapperExpiryMaster.name as name, "
									+ "wrapperExpiryMaster.duration as duration " 
									+ "FROM WrapperExpiryMaster as wrapperExpiryMaster "
									+ "WHERE wrapperExpiryMaster.id=:wrapperId  ")
							.setParameter("wrapperId",preSterilizationProcessDto.getWrapperId())
							.setResultTransformer(Transformers.aliasToBean(WrapperExpiryMasterDto.class)).uniqueResult();
					
					wrapper.setWrapperQuantity(preSterilizationProcessDto.getWrapperQuantity());
					preSterilizationProcessDto.setWrapper(wrapper);
							
				}
				
				
				
				
			}
			
			statelessSession.close();
			return new Response(SUCCESS, null, null, null, preSterilizationProcessDto);
		} catch (HibernateException he) {
			logger.error("getKitFormationWorklistByTransitionNumber failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getKitFormationWorklistByTransitionNumber failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}
	
	
	
	
	@Override
	public Response replaceInstrument(Integer toBeReplaced,Integer replaceWith) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();

			if( toBeReplaced != null && replaceWith != null )
			{
				PreSterilizationProcessInstrument preSterilizationProcessEquipment =(PreSterilizationProcessInstrument)session.get(PreSterilizationProcessInstrument.class,toBeReplaced);
				preSterilizationProcessEquipment.setInstrumentId(replaceWith);
				preSterilizationProcessEquipment.setUpdated_date(TimeZoneComponent.getDateByzone(0));
				session.update(preSterilizationProcessEquipment);
				return new Response(SUCCESS, null, "Replaced successfully.", null, null);
			}
			else
			{
				return new Response(SUCCESS, null, "Unable to replace.", null, null);
			}
		} catch (HibernateException he) {
			logger.error("replaceInstrument failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("replaceInstrument failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}
	
	
	@Override
	public Response getSterilizedKitsByKitId(Integer organizationId,Integer unitId,Integer storeId,Integer kitId,Integer limit) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();
		
			Query query = statelessSession.createQuery("SELECT loadBatch.batchNumber as batchNumber, to_char(loadBatch.autoclaveDate,'dd/MM/yyyy') as autoclaveDate,"
					+ "loadBatch.autoclaveNumber as autoclaveNumber, preSterilizationProcess.transitionNumber as transitionNumber,"
					+ "to_char(preSterilizationProcess.expiryDate,'dd/MM/yyyy') as expiryDate, "
					+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as code, " + "kitMaster.name as name "
					+ "FROM LoadBatchKitMapper mapper INNER JOIN mapper.loadBatch as loadBatch "
					+ "INNER JOIN mapper.preSterilizationProcess as preSterilizationProcess "
					+ "INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
					+ "WHERE "
					+ "preSterilizationProcess.kitId=:kitId and preSterilizationProcess.isIssued=false and preSterilizationProcess.isSterilized=true "
					+ "and preSterilizationProcess.isAccepted=false and preSterilizationProcess.isRejected=false "
					+ "and preSterilizationProcess.organizationId=:organizationId and preSterilizationProcess.unitId=:unitId and preSterilizationProcess.storeId=:storeId "
					+ "order by preSterilizationProcess.expiryDate ")
					.setParameter("kitId",kitId)
					.setParameter("organizationId",organizationId)
					.setParameter("unitId", unitId)
					.setParameter("storeId", storeId)
					.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class));
			
			//get list sterilized stock entered from cssd opening stock	
			
			List<PreSterilizationProcessDto> sterilizedOpeningStockList=sessionFactory.getCurrentSession().createQuery("SELECT preObj.transitionNumber as transitionNumber,"
					+ " to_char(preObj.expiryDate,'dd/MM/yyyy') as expiryDate, "
					+ " preObj.kitId as kitId, " + "kitMaster.code as code, " + "kitMaster.name as name "
					+ " from PreSterilizationProcess preObj"
					+ " INNER JOIN preObj.kitMaster as kitMaster "
					+ " where preObj.isSterilizeFromOpeningStock='Y' and preObj.kitId=:kitId and preObj.storeId=:storeId and preObj.organizationId=:orgId"
					+ " and  preObj.unitId=:unitId and preObj.isSterilized=true and preObj.isIssued=false and preObj.isAccepted=false and preObj.isRejected=false ")
					.setParameter("kitId", kitId)
					.setParameter("storeId", storeId)
					.setParameter("orgId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class)).list();
			
			if(limit != null )
			{
				query.setMaxResults(limit);
			}
			
			
			List<PreSterilizationProcessDto> preSterilizationProcessDtoList = query.list();
			if(!sterilizedOpeningStockList.isEmpty())
			preSterilizationProcessDtoList.addAll(sterilizedOpeningStockList);
			
			if ( !preSterilizationProcessDtoList.isEmpty() ) {
			
				for (PreSterilizationProcessDto preSterilizationProcessDto : preSterilizationProcessDtoList) {
				
					List<PreSterilizationProcessInstrumentDto> kitInstrumentList = statelessSession
							.createQuery("SELECT preSterilizationProcessInstrument.id as id, "
									+ "preSterilizationProcessInstrument.kitId as kitId, "
									+ "preSterilizationProcessInstrument.instrumentId as instrumentId,"
									+ "iteam.code as instrumentCode,"
									+ "iteam.name as instrumentName," 
									+ "preSterilizationProcessInstrument.status as status, "
									+ "kitInstrumentMapper.quantity as quantity " 
									+ "FROM PreSterilizationProcessInstrument preSterilizationProcessInstrument "
									+ "INNER JOIN preSterilizationProcessInstrument.kitMaster as kitMaster "
									+ "INNER JOIN preSterilizationProcessInstrument.instrumentMaster as iteam "
									+ "INNER JOIN preSterilizationProcessInstrument.kitInstrumentMapper as kitInstrumentMapper "
									+ "WHERE preSterilizationProcessInstrument.transitionNumber=:transitionNumber")
							.setParameter("transitionNumber", preSterilizationProcessDto.getTransitionNumber())
							.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessInstrumentDto.class))
							.list();
					
					preSterilizationProcessDto.setKitInstrumentList(kitInstrumentList);
				}
				
				
			}
			
			statelessSession.close();
			return new Response(SUCCESS, null, null, preSterilizationProcessDtoList, null);
		} catch (HibernateException he) {
			logger.error("getSterilizedKitsByKitId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getSterilizedKitsByKitId failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public Response getPreSterilizationProcessList() throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<PreSterilizationProcessDto> preSterilizationProcessDtoList = statelessSession
					.getNamedQuery("GET_PRE_STERILIZATION_PROCESS_LIST")
					.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, preSterilizationProcessDtoList, null);
		} catch (HibernateException he) {
			logger.error("getPreSterilizationProcessList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPreSterilizationProcessList failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}
	
	@Override
	public Response searchPreSterilizationProcessList(String name) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<PreSterilizationProcessDto> preSterilizationProcessDtoList = statelessSession
					.getNamedQuery("SEARCH_PRE_STERILIZATION_PROCESS_LIST")
					.setParameter("name", "%" + name.toLowerCase() + "%")
					.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, preSterilizationProcessDtoList, null);
		} catch (HibernateException he) {
			logger.error("searchPreSterilizationProcessList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("searchPreSterilizationProcessList failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}
	
	@Override
	public Response getKitListPendingForInspection() throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<PreSterilizationProcessDto> preSterilizationProcessDtoList = statelessSession
					.getNamedQuery("GET_KIT_LIST_PENDING_FOR_INSPECTION")
					.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, preSterilizationProcessDtoList, null);
		} catch (HibernateException he) {
			logger.error("getKitListPendingForInspection failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getKitListPendingForInspection failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}
	
	@Override
	public Response getKitListPendingForDisInfection() throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<PreSterilizationProcessDto> preSterilizationProcessDtoList = statelessSession
					.getNamedQuery("GET_KIT_LIST_PENDING_FOR_DIS_INFECTION")
					.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, preSterilizationProcessDtoList, null);
		} catch (HibernateException he) {
			logger.error("getKitListPendingForDisInfection failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getKitListPendingForDisInfection failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}
	
	@Override
	public Response getKitListPendingForKitFormation() throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<PreSterilizationProcessDto> preSterilizationProcessDtoList = statelessSession
					.getNamedQuery("GET_KIT_LIST_PENDING_FOR_KIT_FORMATION")
					.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, preSterilizationProcessDtoList, null);
		} catch (HibernateException he) {
			logger.error("getKitListPendingForKitFormation failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getKitListPendingForKitFormation failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}

	@Override
	public Response getFormedKitList() throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<PreSterilizationProcessDto> preSterilizationProcessDtoList = statelessSession
					.getNamedQuery("GET_FORMED_KIT_LIST")
					.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, preSterilizationProcessDtoList, null);
		} catch (HibernateException he) {
			logger.error("getFormedKitList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getFormedKitList failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}
	
	@Override
	public Response updatePreSterilizationProcess(PreSterilizationProcessDto preSterilizationProcessDto) {
		try {
			PreSterilizationProcess preSterilizationProcess = mapper.map(preSterilizationProcessDto, PreSterilizationProcess.class,
					"PreSterilizationProcessDto_to_PreSterilizationProcess");
			preSterilizationProcess.setCreated_date(TimeZoneComponent.getDateByzone(preSterilizationProcessDto.getUnitId()!=null ? preSterilizationProcessDto.getUnitId() :0));
			preSterilizationProcess.setUpdated_date(TimeZoneComponent.getDateByzone(preSterilizationProcessDto.getUnitId()!=null ? preSterilizationProcessDto.getUnitId() :0));
			preSterilizationProcess = update(preSterilizationProcess);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, preSterilizationProcess);
		} catch (Exception e) {
			logger.error("updatePreSterilizationProcess failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	@Override
	public Response createPreSterilizationProcess(PreSterilizationProcess preSterilizationProcess) {
		try {
			
			preSterilizationProcess = save(preSterilizationProcess);
			return new Response(SUCCESS, "201","Created", null, null);
		} catch (Exception e) {
			logger.error("createPreSterilizationProcess failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response getPreSterilizationProcessKitById(Integer kitId) throws ApplicationException {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<PreSterilizationProcessDto> preSterilizationProcessDtoList = statelessSession
					.getNamedQuery("GET_PRE_STERILIZATION_PROCESS_KIT_BY_ID")
					.setParameter("kitId", kitId)
					.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, null, preSterilizationProcessDtoList.getFirst());
		} catch (HibernateException he) {
			logger.error("getPreSterilizationProcessKitById failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getPreSterilizationProcessKitById failed", e);
			throw new ServiceException(e.getMessage());
		} finally {
		}
	}


	@Override
	public Response getCssdStoreList(PreSterilizationProcessDto preSterilizationProcessDto) {
	try {
		List<PreSterilizationProcessDto> cssdStoreList=sessionFactory.getCurrentSession().createQuery(" SELECT s.storeId as storeId,s.storeName as storeName From Store s "
				+ " inner join s.userStoreMappersList userStore "
				+ " where s.organization.organizationId=:orgId and s.unit.unitId=:unitId and s.isDeleted = false AND s.isActive = true AND s.isCSSD=true "
				+ " and userStore.isDeleted = false  AND userStore.isActive = true  and userStore.userId=:userId")
				.setParameter("orgId", preSterilizationProcessDto.getOrganizationId())
				.setParameter("unitId", preSterilizationProcessDto.getUnitId())
				.setParameter("userId", preSterilizationProcessDto.getUserId())
				.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class)).list();
				
		return new Response(SUCCESS, null, null, cssdStoreList, null);
	} catch (HibernateException he) {
		logger.error("getCssdStoreList failed", he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error("getCssdStoreList failed", e);
		throw new ServiceException(e.getMessage());
	} finally {
	}
	}


	@Override
	public Response cssdStockLogUpdate(PreSterilizationProcessDto preSterilizationProcessDto) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Response checkStoreStockByItemId(PreSterilizationProcessDto preSterilizationProcessDto) {
		try {
			List<PreSterilizationProcessInstrumentDto> itemList=new ArrayList<PreSterilizationProcessInstrumentDto>();
			itemList=preSterilizationProcessDto.getKitInstrumentList();
			Boolean flag=false;
			for(PreSterilizationProcessInstrumentDto dto:itemList) {
				
				PreSterilizationProcessDto batchStock=(PreSterilizationProcessDto)sessionFactory.getCurrentSession()
						.createSQLQuery("select sum(batch.quantity) as \"batchQuantity\" from inventory.t_batch batch  " 
								+"inner join inventory.t_store_stock stock on batch.id=stock.batch_id " 
								+"where batch.item_id=:itemId and stock.store_id=:storeId "
								+ "and batch.organization_id=:orgId and batch.unit_id=:unitId ")
						.setParameter("itemId", dto.getInstrumentId())
						.setParameter("storeId", preSterilizationProcessDto.getStoreId())
						.setParameter("orgId", preSterilizationProcessDto.getOrganizationId())
						.setParameter("unitId", preSterilizationProcessDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class))
						.uniqueResult();
				//System.out.println("Total item Quantity for item id:"+dto.getInstrumentId()+" is:"+batchStock.getBatchQuantity());
				PreSterilizationProcessDto sterilizedStock=(PreSterilizationProcessDto)sessionFactory.getCurrentSession()
						.createSQLQuery("select stockLog.sterilized_quantity as \"sterilizedStockQty\","
								+ " stockLog.in_transit_quantity as \"inTransitQty\" from cssd.t_cssd_stock_transaction_log stockLog " 
								+"where stockLog.item_id=:itemId and stockLog.store_id=:storeId and stockLog.organization_id=:orgId "
								+ "and stockLog.unit_id=:unitId and stockLog.status='A' ")
						.setParameter("itemId", dto.getInstrumentId())
						.setParameter("storeId", preSterilizationProcessDto.getStoreId())
						.setParameter("orgId", preSterilizationProcessDto.getOrganizationId())
						.setParameter("unitId", preSterilizationProcessDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class))
						.uniqueResult();
				
				if(batchStock.getBatchQuantity()==null) {
					flag=true;
				}else {
					if(sterilizedStock!=null) {
					Integer totalBatchQty=batchStock.getBatchQuantity().intValue();
					Integer sterilizedQty=(sterilizedStock.getSterilizedStockQty()!=null ? sterilizedStock.getSterilizedStockQty() : 0);
					Integer inTransitQty=(sterilizedStock.getInTransitQty()!=null ? sterilizedStock.getInTransitQty() : 0);
				
					if((sterilizedQty+inTransitQty)+(dto.getQuantity()) <= totalBatchQty ) {
						//allow
					}else {
						flag=true;
						break;
					}
				
					}	
			}
			}	
			
			if(flag.equals(true)) {
				//No stock available
				return new Response(ERROR, null, "No stock availbale", null, null);
			}else {
				return new Response(SUCCESS, null, "Stock available", null, null);
			}
	} catch (HibernateException he) {
		logger.error("checkStoreStockByItemId failed", he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error("checkStoreStockByItemId failed", e);
		throw new ServiceException(e.getMessage());
	} finally {
	}
	}


	@Override
	public Response updateInTransitStock(PreSterilizationProcessDto preSterilizationProcessDto) {
		try {
			List<PreSterilizationProcessInstrumentDto> itemList=new ArrayList<PreSterilizationProcessInstrumentDto>();
			itemList=preSterilizationProcessDto.getKitInstrumentList();
			
			for(PreSterilizationProcessInstrumentDto dto:itemList) {
			CssdStockTransactionLog log=(CssdStockTransactionLog)sessionFactory.getCurrentSession()
					.createQuery("SELECT log FROM CssdStockTransactionLog log where log.storeId=:storeId and log.itemId=:itemId "
							+ " and log.organizationId=:orgId and log.unitId=:unitId ")
					.setParameter("storeId", preSterilizationProcessDto.getStoreId())
					.setParameter("itemId",dto.getInstrumentId())
					.setParameter("orgId", preSterilizationProcessDto.getOrganizationId())
					.setParameter("unitId", preSterilizationProcessDto.getUnitId())
					.uniqueResult();
			//if not exit then save
			if(log==null) {
				//save
				CssdStockTransactionLog cssdStockTransactionLog=new CssdStockTransactionLog();
				cssdStockTransactionLog.setStoreId(preSterilizationProcessDto.getStoreId());
				cssdStockTransactionLog.setItemId(dto.getInstrumentId());
				cssdStockTransactionLog.setSterilizedQuantity(0);
				cssdStockTransactionLog.setNotReturnedQuantity(0);
				cssdStockTransactionLog.setInTransitQuantity(dto.getQuantity());
				cssdStockTransactionLog.setOrganizationId(preSterilizationProcessDto.getOrganizationId());
				cssdStockTransactionLog.setUnitId(preSterilizationProcessDto.getUnitId());
				cssdStockTransactionLog.setCreatedBy(preSterilizationProcessDto.getCreated_by());
				cssdStockTransactionLog.setCreatedDate(TimeZoneComponent.getDateByzone(preSterilizationProcessDto.getUnitId()));
				cssdStockTransactionLog.setUpdatedBy(preSterilizationProcessDto.getUpdated_by());
				cssdStockTransactionLog.setUpdatedDate(TimeZoneComponent.getDateByzone(preSterilizationProcessDto.getUnitId()));
				cssdStockTransactionLog.setStatus('A');
				sessionFactory.getCurrentSession().save(cssdStockTransactionLog);
			}else {
				//for update 
				sessionFactory.getCurrentSession().createQuery(" update CssdStockTransactionLog log "
						+ " set log.inTransitQuantity=log.inTransitQuantity+(:inTransitQty),"
						+ " log.updatedBy=:updatedBy,log.updatedDate=:updatedDate"
						+ " where log.storeId=:storeId and log.itemId=:itemId and log.organizationId=:orgId and log.unitId=:unitId ")
				.setParameter("inTransitQty", dto.getQuantity())
				.setParameter("updatedBy", preSterilizationProcessDto.getUpdated_by())
				.setParameter("updatedDate", TimeZoneComponent.getDateByzone(preSterilizationProcessDto.getUnitId()))
				.setParameter("storeId", preSterilizationProcessDto.getStoreId())
				.setParameter("itemId", dto.getInstrumentId())
				.setParameter("orgId", preSterilizationProcessDto.getOrganizationId())
				.setParameter("unitId", preSterilizationProcessDto.getUnitId())
				.executeUpdate();
			}
		
			}
		  return new Response(SUCCESS, SUCCESS_CODE, "updated Successfully", null, null);
		
		} catch (HibernateException he) {
			logger.error("updateInTransitStock failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateInTransitStock failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response saveSterilizedStockForIssue(CssdOpeningBalanceDetailStock details, Integer storeId) {
		try {
			/*PreSterilizationProcessDto itemDetails=sessionFactory.getCurrentSession().createQuery("SELECT item.name as name,item.code as code from Item item where item.id=:id")
					                              .setParameter("id", details.getInstrumentId())
					                              .setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class))
					      						  .uniqueResult();*/
			PreSterilizationProcessDto preSterilizationProcessDto=new PreSterilizationProcessDto();
			preSterilizationProcessDto.setName(details.getItem().getName());
			preSterilizationProcessDto.setCode(details.getItem().getCode());
			preSterilizationProcessDto.setStoreId(storeId);
			preSterilizationProcessDto.setIsInstrument(true);
             preSterilizationProcessDto.setCreated_by(details.getAddedBy());
             preSterilizationProcessDto.setUpdated_by(details.getUpdatedBy());
             preSterilizationProcessDto.setOrganizationId(details.getOrganizationId());
             preSterilizationProcessDto.setUnitId(details.getUnitId());
			
             List<PreSterilizationProcessInstrumentDto> itemList=new ArrayList<PreSterilizationProcessInstrumentDto>();
             PreSterilizationProcessInstrumentDto itemDto=new PreSterilizationProcessInstrumentDto();
             itemDto.setInstrumentId(details.getInstrumentId());
             itemDto.setQuantity(details.getInstrumentQuantity());
             itemList.add(itemDto);
             preSterilizationProcessDto.setKitInstrumentList(itemList);
             Response resp = iPreSterilizationProcessService.saveInstrumentAsKit(preSterilizationProcessDto);
			 PreSterilizationProcessDto dto = (PreSterilizationProcessDto)(resp.getObject());
			 Response resp1 = iPreSterilizationProcessService.getKitInstrumentId(dto.getKitId(),preSterilizationProcessDto.getKitInstrumentList().getFirst().getInstrumentId());
			 Integer kitInstrumentId = (Integer) resp1.getObject();
				      				
			PreSterilizationProcess preSterilizationProcess=new PreSterilizationProcess();
			preSterilizationProcess.setCreated_by(details.getAddedBy());
			preSterilizationProcess.setUpdated_by(details.getUpdatedBy());
			preSterilizationProcess.setCreated_date(TimeZoneComponent.getDateByzone(details.getUnitId()));
			preSterilizationProcess.setUpdated_date(TimeZoneComponent.getDateByzone(details.getUnitId()));
			preSterilizationProcess.setStatus("Completed");
			preSterilizationProcess.setTransition("KitFormed");
			preSterilizationProcess.setKitId(dto.getKitId());
			preSterilizationProcess.setOrganizationId(details.getOrganizationId());
			preSterilizationProcess.setUnitId(details.getUnitId());
			preSterilizationProcess.setStoreId(storeId);
			preSterilizationProcess.setIsIssued(false);
			preSterilizationProcess.setIsSterilized(true);
			preSterilizationProcess.setIsAccepted(false);
			preSterilizationProcess.setIsRejected(false);
			preSterilizationProcess.setIsReturned(false);
			preSterilizationProcess.setSterilizationRequested(false);
			preSterilizationProcess.setFormation_date(TimeZoneComponent.getDateByzone(details.getUnitId()));
			preSterilizationProcess.setExpiryDate(details.getSterilizationExpiryDate());
			preSterilizationProcess.setIsSterilizeFromOpeningStock('Y');
			Integer transitionNumber =(Integer)sessionFactory.getCurrentSession().save(preSterilizationProcess);
					PreSterilizationProcessInstrument preSterilizationProcessEquipment=new PreSterilizationProcessInstrument();
					preSterilizationProcessEquipment.setInstrumentId(details.getInstrumentId());
					preSterilizationProcessEquipment.setKitId(dto.getKitId());
					preSterilizationProcessEquipment.setStoreId(storeId);
					preSterilizationProcessEquipment.setTransitionNumber(transitionNumber);
					preSterilizationProcessEquipment.setKitInstrumentId(kitInstrumentId);
					preSterilizationProcessEquipment.setStatus("KitFormed");
					preSterilizationProcessEquipment.setCreated_by(details.getAddedBy());
					preSterilizationProcessEquipment.setUpdated_by(details.getUpdatedBy());
					preSterilizationProcessEquipment.setCreated_date(TimeZoneComponent.getDateByzone(details.getUnitId()));
					preSterilizationProcessEquipment.setUpdated_date(TimeZoneComponent.getDateByzone(details.getUnitId()));
					sessionFactory.getCurrentSession().save(preSterilizationProcessEquipment);
					
				
			 return new Response(SUCCESS, SUCCESS_CODE, "saved Successfully", null, null);
		}catch (HibernateException he) {
			logger.error("saveSterilizedStockForIssue failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveSterilizedStockForIssue failed", e);
			throw new ServiceException(e.getMessage());
		}
	}



	@Override
	public Response saveSterilizedKitStockForIssue(Integer storeId,
			Map<Integer, List<CssdOpeningBalanceDetailStock>> map) {
	try {
		   for (Map.Entry<Integer, List<CssdOpeningBalanceDetailStock>> currentObj : map.entrySet()) {
			   PreSterilizationProcess preSterilizationProcess=new PreSterilizationProcess();
				preSterilizationProcess.setCreated_by(currentObj.getValue().getFirst().getAddedBy());
				preSterilizationProcess.setUpdated_by(currentObj.getValue().getFirst().getUpdatedBy());
				preSterilizationProcess.setCreated_date(TimeZoneComponent.getDateByzone(currentObj.getValue().getFirst().getUnitId()));
				preSterilizationProcess.setUpdated_date(TimeZoneComponent.getDateByzone(currentObj.getValue().getFirst().getUnitId()));
				preSterilizationProcess.setStatus("Completed");
				preSterilizationProcess.setTransition("KitFormed");
				preSterilizationProcess.setKitId(currentObj.getKey());
				preSterilizationProcess.setOrganizationId(currentObj.getValue().getFirst().getOrganizationId());
				preSterilizationProcess.setUnitId(currentObj.getValue().getFirst().getUnitId());
				preSterilizationProcess.setStoreId(storeId);
				preSterilizationProcess.setIsIssued(false);
				preSterilizationProcess.setIsSterilized(true);
				preSterilizationProcess.setIsAccepted(false);
				preSterilizationProcess.setIsRejected(false);
				preSterilizationProcess.setIsReturned(false);
				preSterilizationProcess.setSterilizationRequested(false);
				preSterilizationProcess.setIsSterilizeFromOpeningStock('Y');
				preSterilizationProcess.setFormation_date(TimeZoneComponent.getDateByzone(currentObj.getValue().getFirst().getUnitId()));
				preSterilizationProcess.setExpiryDate(currentObj.getValue().getFirst().getSterilizationExpiryDate());
				Integer transitionNumber =(Integer)sessionFactory.getCurrentSession().save(preSterilizationProcess);
				
				for(CssdOpeningBalanceDetailStock dto:currentObj.getValue()) {
						PreSterilizationProcessInstrument preSterilizationProcessEquipment=new PreSterilizationProcessInstrument();
						preSterilizationProcessEquipment.setInstrumentId(dto.getItem().getId());
						preSterilizationProcessEquipment.setKitId(currentObj.getKey());
						preSterilizationProcessEquipment.setStoreId(storeId);
						preSterilizationProcessEquipment.setTransitionNumber(transitionNumber);
						//get kitInstrument Id
						KitInstrumentMapper dto2=(KitInstrumentMapper)sessionFactory.getCurrentSession()
								.createQuery("select kitIns.kitInstrumentId as kitInstrumentId from KitInstrumentMapper kitIns where kitIns.kitId=:kitId and kitIns.instrumentId=:itemId ")
								.setParameter("kitId", currentObj.getKey())
								.setParameter("itemId",dto.getItem().getId())
								.setResultTransformer(Transformers.aliasToBean(KitInstrumentMapper.class))
								.uniqueResult();
						//kitInstrument id is Required
						preSterilizationProcessEquipment.setKitInstrumentId(dto2.getKitInstrumentId());
						preSterilizationProcessEquipment.setStatus("KitFormed");
						preSterilizationProcessEquipment.setCreated_by(dto.getAddedBy());
						preSterilizationProcessEquipment.setUpdated_by(dto.getUpdatedBy());
						preSterilizationProcessEquipment.setCreated_date(TimeZoneComponent.getDateByzone(currentObj.getValue().getFirst().getUnitId()));
						preSterilizationProcessEquipment.setUpdated_date(TimeZoneComponent.getDateByzone(currentObj.getValue().getFirst().getUnitId()));
						sessionFactory.getCurrentSession().save(preSterilizationProcessEquipment);
				}
			   
		   }
		 return new Response(SUCCESS, SUCCESS_CODE, "saved Successfully", null, null);
	}catch (HibernateException he) {
		logger.error("saveSterilizedKitStockForIssue failed", he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error("saveSterilizedKitStockForIssue failed", e);
		throw new ServiceException(e.getMessage());
	}
	}


	@Override
	public Response updateSterilizationStock(List<Integer> unitIds) {
		try {
			Date date = TimeZoneComponent.getDateByzone(0);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = formatter.format(date);

			//update cyclic appointment quantity
			LocalDate dateC=new LocalDate();
			LocalDate date2=dateC.minusDays(1);
			String previousDate = date2.toString();
			//System.out.println("previous date"+previousDate);
			//get missed cyclic appointments
			List<SlotMasterDto> appointmentList=sessionFactory.getCurrentSession().createQuery("select app.appointmentId as appointmentId "
					+ " From AppointmentMaster app where to_char(app.appointmentDate,'YYYY-MM-DD')=:appDate and app.isCyclicAppointment='Y' "
					+ " and app.unitId in (:unitIds) ")
					.setParameter("appDate", previousDate)
					.setParameterList("unitIds", unitIds)
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			
			for(SlotMasterDto appointmentDto:appointmentList) {
			sessionFactory.getCurrentSession().createQuery("update CyclicAppointmentMapper cam set cam.availabelQuantity=0 "
					+ " where cam.appointmentId=:appId")
			        .setParameter("appId", appointmentDto.getAppointmentId()).executeUpdate();
			}
			
			//end of cyclic appointment
			
			List<PreSterilizationProcess> kitList = sessionFactory.getCurrentSession().createQuery("Select "
					+ " pre.transitionNumber as transitionNumber,pre.storeId as storeId,"
					+ " pre.unitId as unitId,pre.organizationId as organizationId,pre.isIssued as isIssued, pre.isAccepted as isAccepted,pre.isReturned as isReturned"
					+ "  from PreSterilizationProcess pre "
					//+ " where pre.isIssued=true and pre.isAccepted=true and
					// to_char(pre.expiryDate,'YYYY-MM-DD')<:currentDate ")
					+ "   where  to_char(pre.expiryDate,'YYYY-MM-DD')<:currentDate and pre.transition='KitFormed'  and pre.unitId in (:unitIds)")
					.setParameter("currentDate", currentDate)
					.setParameterList("unitIds", unitIds)
					.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcess.class)).list();

			for (PreSterilizationProcess preSterilizationProcess : kitList) {
				// update status of kit
				sessionFactory.getCurrentSession().createQuery(
						"UPDATE PreSterilizationProcess preObj set preObj.transition='Unsterilized',preObj.isSterilized=false "
								+ "where preObj.transitionNumber=:tranNo")
						.setParameter("tranNo", preSterilizationProcess.getTransitionNumber()).executeUpdate();

				List<PreSterilizationProcessInstrument> kitItemList = sessionFactory.getCurrentSession()
						.createQuery("Select "
								+ " preProcess.transitionNumber as transitionNumber,preProcess.instrumentId as instrumentId,preProcess.physicalQuantity as physicalQuantity"
								+ "  from PreSterilizationProcessInstrument preProcess"
								+ "  where preProcess.transitionNumber=:transitionId ")
						.setParameter("transitionId", preSterilizationProcess.getTransitionNumber())
						.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessInstrument.class)).list();

				// Update sterilized stock against item
				for (PreSterilizationProcessInstrument kitItem : kitItemList) {
					if(preSterilizationProcess.getIsReturned().equals(false) && preSterilizationProcess.getIsAccepted().equals(true)) {
						//update not return quantity
					sessionFactory.getCurrentSession().createQuery(" update CssdStockTransactionLog log "
							+ " set log.notReturnedQuantity=log.notReturnedQuantity+(:sterQty),log.sterilizedQuantity=log.sterilizedQuantity-(:sterQty),"
							+ " log.updatedDate=:updatedDate"
							+ " where log.storeId=:storeId and log.itemId=:itemId and log.organizationId=:orgId and log.unitId=:unitId ")
							.setParameter("sterQty", kitItem.getPhysicalQuantity())
							.setParameter("updatedDate", TimeZoneComponent.getDateByzone(preSterilizationProcess.getUnitId()))
							.setParameter("storeId", preSterilizationProcess.getStoreId())
							.setParameter("itemId", kitItem.getInstrumentId())
							.setParameter("orgId", preSterilizationProcess.getOrganizationId())
							.setParameter("unitId", preSterilizationProcess.getUnitId()).executeUpdate();
					}else {
						//update sterilize quantity
						sessionFactory.getCurrentSession().createQuery(" update CssdStockTransactionLog log "
								+ " set log.sterilizedQuantity=log.sterilizedQuantity-(:sterQty),"
								+ " log.updatedDate=:updatedDate"
								+ " where log.storeId=:storeId and log.itemId=:itemId and log.organizationId=:orgId and log.unitId=:unitId ")
								.setParameter("sterQty", kitItem.getPhysicalQuantity())
								.setParameter("updatedDate", TimeZoneComponent.getDateByzone(preSterilizationProcess.getUnitId()))
								.setParameter("storeId", preSterilizationProcess.getStoreId())
								.setParameter("itemId", kitItem.getInstrumentId())
								.setParameter("orgId", preSterilizationProcess.getOrganizationId())
								.setParameter("unitId", preSterilizationProcess.getUnitId()).executeUpdate();
					}
				}
			}
			 return new Response(SUCCESS, SUCCESS_CODE, "Stock Updated!!", null, null);	
		} catch (HibernateException he) {
			logger.error("updateSterilizationStock failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateSterilizationStock failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getSterilizedKitByKitName(CssdKitDto kitDto) {
		try {
			List<CssdKitDto> kitList=sessionFactory.getCurrentSession().createSQLQuery("select distinct kitMaster.code as \"kitCode\"," 
					+"	kitMaster.name as \"kitName\",kitMaster.kit_id as \"kitId\" "  
					+"	from cssd.t_pre_sterilize_process pre " 
					+"	inner join cssd.t_load_batch_kit_mapper batchKitMapp  on batchKitMapp.transition_number=pre.id" 
					+"	inner join cssd.t_load_batch batch on batch.load_batch_id=batchKitMapp.load_batch_id" 
					+"	inner join cssd.m_kit_master kitMaster on kitMaster.kit_id = pre.kit_id"  
					+"	inner join cssd.t_issued_kit_transition_mapper kitTranMapp on kitTranMapp.transition_number=pre.id" 
					+"	inner join cssd.t_kit_issue kitIssue on kitIssue.id=kitTranMapp.issue_no"  
					+"	where pre.is_sterilized=true and pre.is_issued=true and pre.is_accepted=true"  
					+"	and kitIssue.to_store_id=:storeId " 
					+"	and (lower(kitMaster.name) like :searchKey or lower(kitMaster.code) like :searchKey)" 
					+"	and pre.organization_id=:orgId and pre.unit_id=:unitId")
					.setParameter("orgId", kitDto.getOrganizationId())
					.setParameter("unitId", kitDto.getUnitId())
					.setParameter("storeId", kitDto.getStoreId())
					.setParameter("searchKey", "%"+kitDto.getSearchKeyword().toLowerCase()+"%")
					.setResultTransformer(Transformers.aliasToBean(CssdKitDto.class))
					.list();
		 return new Response(SUCCESS, SUCCESS_CODE, null, kitList, null);	
	} catch (HibernateException he) {
		logger.error("getSterilizedKitByKitName failed", he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error("getSterilizedKitByKitName failed", e);
		throw new ServiceException(e.getMessage());
	}
	}


	@Override
	public Response getSterilizedKitBatchList(CssdKitDto kitDto) {
		try {
			List<CssdKitDto> kitBatchList=sessionFactory.getCurrentSession().createSQLQuery("select pre.id as \"kitTransitionId\",kitMaster.code as \"kitCode\","
					+ "kitMaster.kit_id as \"kitId\","  
					+"  kitMaster.name as \"kitName\",to_char(pre.expiry_date,'dd-mm-yyyy') as \"kitExpiryDate\",concat('B-',batch.load_batch_id)as \"batchNumber\" "  
					+"  from cssd.t_pre_sterilize_process pre "  
					+"  inner join cssd.t_load_batch_kit_mapper batchKitMapp  on batchKitMapp.transition_number=pre.id"  
					+"  inner join cssd.t_load_batch batch on batch.load_batch_id=batchKitMapp.load_batch_id"  
					+"  inner join cssd.m_kit_master kitMaster on kitMaster.kit_id = pre.kit_id"  
					+"  inner join cssd.t_issued_kit_transition_mapper kitTranMapp on kitTranMapp.transition_number=pre.id"  
					+"  inner join cssd.t_kit_issue kitIssue on kitIssue.id=kitTranMapp.issue_no"  
					+"  where pre.is_sterilized=true and pre.is_issued=true and pre.is_accepted=true"  
					+"  and kitIssue.to_store_id=:storeId and kitMaster.kit_id=:kitId"
					+"	and pre.organization_id=:orgId and pre.unit_id=:unitId"
					+ " and kitTranMapp.is_kit_used='N' ")
					.setParameter("orgId", kitDto.getOrganizationId())
					.setParameter("unitId", kitDto.getUnitId())
					.setParameter("storeId", kitDto.getStoreId())
					.setParameter("kitId", kitDto.getKitId())
					.setResultTransformer(Transformers.aliasToBean(CssdKitDto.class))
					.list();
		 return new Response(SUCCESS, SUCCESS_CODE, null, kitBatchList, null);	
	} catch (HibernateException he) {
		logger.error("getSterilizedKitBatchList failed", he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error("getSterilizedKitBatchList failed", e);
		throw new ServiceException(e.getMessage());
	}
	}


}


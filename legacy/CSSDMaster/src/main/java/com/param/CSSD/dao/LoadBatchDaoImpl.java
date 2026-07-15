package com.param.CSSD.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.CSSD.dto.KitByBatchNoDto;
import com.param.CSSD.dto.LoadBatchDto;
import com.param.CSSD.dto.PreSterilizationProcessDto;
import com.param.CSSD.dto.PreSterilizationProcessInstrumentDto;
import com.param.CSSD.model.AutoclaveLog;
import com.param.CSSD.model.LoadBatch;
import com.param.CSSD.model.LoadBatchKitMapper;
import com.param.CSSD.model.PreSterilizationProcess;
import com.param.CSSDMaster.dto.WrapperExpiryMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

import static org.apache.log4j.xml.DOMConfigurator.setParameter;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class LoadBatchDaoImpl extends GenericDao<LoadBatch, Integer>
		implements ILoadBatchDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(LoadBatchDaoImpl.class);


	public LoadBatchDaoImpl() {
		super(LoadBatch.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private DozerBeanMapper mapper;

	
	@Override
	public Response searchKitsToLoadBatch(Integer organizationId,Integer unitId,Integer storeId,String name ) {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			String query = "SELECT preSterilizationProcess.transitionNumber as transitionNumber, "
					+ "preSterilizationProcess.kitId as kitId, " + "kitMaster.code as code, " 
					+ "kitMaster.name as name, to_char(preSterilizationProcess.created_date,'dd/MM/yyyy') as created_date,"
					+ "preSterilizationProcess.status as status, preSterilizationProcess.wrapperId as wrapperId, wrapper.name as wrapperName,"
					+ "wrapper.duration as expiryDuration, preSterilizationProcess.transition as transition,"
					+ "to_char(preSterilizationProcess.formation_date,'dd/MM/yyyy') as formation_date " 
					+ "FROM PreSterilizationProcess preSterilizationProcess INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
					+ "INNER JOIN preSterilizationProcess.wrapperExpiryMaster as wrapper "
					+ "WHERE  "
					+ "( lower(kitMaster.code) LIKE :name OR lower(kitMaster.name) LIKE :name ) and preSterilizationProcess.transition='KitFormed' "
					+ "and preSterilizationProcess.status='Completed' and preSterilizationProcess.organizationId=:organizationId "
					+ "and preSterilizationProcess.unitId=:unitId and preSterilizationProcess.storeId=:storeId "
					+ "and preSterilizationProcess.isSterilized=false and preSterilizationProcess.isIssued=false ";
			List<PreSterilizationProcessDto> preSterilizationProcessDtoList = statelessSession
					.createQuery(query)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setParameter("storeId", storeId)
					.setParameter("name", "%" + name.trim().toLowerCase() + "%")
					.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class)).list();
			statelessSession.close();
			List<PreSterilizationProcessDto> finalListForLoadBatch=new ArrayList<PreSterilizationProcessDto>();
			//list for load batch
			for(PreSterilizationProcessDto dto:preSterilizationProcessDtoList) {
				List<PreSterilizationProcessDto> list=sessionFactory.getCurrentSession().createQuery("Select loadBatch.status as status from LoadBatchKitMapper loadBatchKit"
						+ " INNER JOIN loadBatchKit.loadBatch loadBatch"
						+ " where loadBatchKit.transitionNumber=:transitionNo  order by loadBatch.batchNumber desc")
				.setParameter("transitionNo", dto.getTransitionNumber())
				.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class)).list();
				
				if(!list.isEmpty()) {
					if(list.get(0).getStatus().equals("Unsterilized")) {
						finalListForLoadBatch.add(dto);
					}
				}else {
					finalListForLoadBatch.add(dto);
				}
			}
			return new Response(SUCCESS, null, null, finalListForLoadBatch, null);
		} catch (Exception e) {
			logger.error("searchKitsToLoadBatch failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getRecentBioIndiDate(Integer organizationId,Integer unitId,Integer storeId,Integer autoclaveId ) {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

				String queryDate = "select max(autoLog.bioIndiDate) from AutoclaveLog autoLog "
						+ "where autoLog.autoclaveNumber=:autoclaveId and autoLog.organizationId=:organizationId and autoLog.unitId=:unitId )";
				Date dueDate = (Date)statelessSession
						.createQuery(queryDate)
						.setParameter("organizationId", organizationId)
						.setParameter("unitId", unitId)
						.setParameter("autoclaveId", autoclaveId)
						.uniqueResult();
				
				statelessSession.close();
				return new Response(SUCCESS, null, null, null, dueDate);	
	
		//	return new Response(SUCCESS, null, null, null, dueDate);
		} catch (Exception e) {
			logger.error("getRecentBioIndiDate failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response loadBatch(LoadBatchDto loadBatchDto ) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			
			LoadBatch loadBatch = mapper.map(loadBatchDto, LoadBatch.class,
					"LoadBatchDto_to_LoadBatch");
			loadBatch.setAutoclaveDate(TimeZoneComponent.getDateByzone(loadBatchDto.getUnitId()));
			loadBatch.setCreated_date(TimeZoneComponent.getDateByzone(loadBatchDto.getUnitId()));
			loadBatch.setUpdated_date(TimeZoneComponent.getDateByzone(loadBatchDto.getUnitId()));
			loadBatch.setStatus("Batch Loaded");
			
			Integer loadBatchId=(Integer)session.save(loadBatch);
			
			List<PreSterilizationProcessDto> preSterilizationProcessDtoList=loadBatchDto.getKitList();
			for (PreSterilizationProcessDto preSterilizationProcessDto : preSterilizationProcessDtoList) {
				LoadBatchKitMapper loadBatchKitMapper=new LoadBatchKitMapper();
				loadBatchKitMapper.setBatchNumber(loadBatchId);
				loadBatchKitMapper.setTransitionNumber(preSterilizationProcessDto.getTransitionNumber());
				session.save(loadBatchKitMapper);
				
				PreSterilizationProcess preSterilizationProcess = (PreSterilizationProcess) session.get(PreSterilizationProcess.class, preSterilizationProcessDto.getTransitionNumber());
				preSterilizationProcess.setExpiryDate(new SimpleDateFormat("E MMM dd yyyy HH:mm:ss").parse(preSterilizationProcessDto.getExpiryDate()));
				preSterilizationProcess.setUpdated_date(TimeZoneComponent.getDateByzone(loadBatchDto.getUnitId()));
				session.update(preSterilizationProcess);
				
			}
			if(loadBatchDto.getBiologicalIndicator() != null && loadBatchDto.getBiologicalIndicator() )
			{
				AutoclaveLog autoLog = new AutoclaveLog ();
				autoLog.setAutoclaveNumber(loadBatchDto.getAutoclaveNumber());
				autoLog.setControl(loadBatchDto.getControl());
				autoLog.setTest(loadBatchDto.getTest());
				autoLog.setCreated_date(TimeZoneComponent.getDateByzone(loadBatchDto.getUnitId()));
				autoLog.setUpdated_date(TimeZoneComponent.getDateByzone(loadBatchDto.getUnitId()));
				autoLog.setBioIndiDate(TimeZoneComponent.getDateByzone(loadBatchDto.getUnitId()));
				autoLog.setCreated_by(loadBatchDto.getCreated_by());
				autoLog.setUpdated_by(loadBatchDto.getCreated_by());
				autoLog.setOrganizationId(loadBatchDto.getOrganizationId());
				autoLog.setUnitId(loadBatchDto.getUnitId());
				
				session.save(autoLog);
				
			}
			
			
			
			return new Response(SUCCESS, "200","Batch Loaded.", null, null);
		} catch (Exception e) {
			logger.error("loadBatch failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response getLoadBatchListByMultipleFields(Integer organizationId,Integer unitId,Integer storeId,Date from,Date to,Integer batchNo,Boolean biologicalIndicator,String status) {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			StringBuilder condition=new StringBuilder();
			
			if(batchNo != null)
			{
				condition.append(" AND loadBatch.batchNumber=:batchNo ");
			}
			if(status != null)
			{
				condition.append(" AND loadBatch.status=:status ");
			}
			if(biologicalIndicator != null)
			{
				condition.append(" AND loadBatch.biologicalIndicator=:biologicalIndicator ");
			}
			Query createQUery = statelessSession
					.createQuery("SELECT loadBatch.batchNumber as batchNumber, "
							+ "loadBatch.autoclaveNumber as autoclaveNumber,to_char(loadBatch.autoclaveDate,'dd/MM/yyyy') as autoclaveDate, " 
							/*+ "to_char(autoclaveMaster.updated_date + autoclaveMaster.biological_indicator_day,'dd/MM/yyyy') as expiryDate, "*/
							+ "loadBatch.status as status,loadBatch.isSterilized as isSterilized FROM LoadBatch loadBatch INNER JOIN loadBatch.autoclaveMaster as autoclaveMaster "
							+ "WHERE loadBatch.organizationId=:organizationId AND loadBatch.unitId=:unitId AND loadBatch.storeId=:storeId AND loadBatch.created_date BETWEEN :from AND :to "
							+condition )
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setParameter("storeId", storeId)
					.setParameter("from", from)
					.setParameter("to", to);

			if(batchNo != null)
			{
				createQUery.setParameter("batchNo", batchNo);
			}
			if(status != null)
			{
				createQUery.setParameter("status", status);
			}
			if(biologicalIndicator != null)
			{
				createQUery.setParameter("biologicalIndicator", biologicalIndicator);
			}


			List<LoadBatchDto> loadBatchDtoList =createQUery.setResultTransformer(Transformers.aliasToBean(LoadBatchDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, loadBatchDtoList, null);
		} catch (Exception e) {
			logger.error("getLoadBatchListByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response savePendingIndicators(LoadBatchDto loadBatchDto ) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			
			LoadBatch loadBatch = (LoadBatch)session.get(LoadBatch.class, loadBatchDto.getBatchNumber());
					
			//loadBatch.setExpiryDate(TimeZoneComponent.getDateByzone(unitId));
			loadBatch.setColor(loadBatchDto.getColor());
			loadBatch.setSterilizationTemperature(loadBatchDto.getSterilizationTemperature());
			loadBatch.setPressure(loadBatchDto.getPressure());
			loadBatch.setUpdated_by(loadBatchDto.getUpdated_by());
			loadBatch.setUpdated_date(TimeZoneComponent.getDateByzone(loadBatchDto.getUnitId()));
			if(loadBatchDto.getIsSterilized())
			{
				loadBatch.setStatus("Sterilized");
				loadBatch.setIsSterilized(loadBatchDto.getIsSterilized());
				
				List<WrapperExpiryMasterDto> transitionNumberList=sessionFactory.getCurrentSession().createQuery("Select "
						+ " pre.transitionNumber as id,(select wrapper.duration From WrapperExpiryMaster wrapper where wrapper.id=pre.wrapperId) as duration"
						+ "  from PreSterilizationProcess pre  where pre.transitionNumber IN " 
						+ "(select transitionNumber from LoadBatchKitMapper lbkm where lbkm.batchNumber=:batchNumber)")
						.setParameter("batchNumber", loadBatchDto.getBatchNumber())
						.setResultTransformer(Transformers.aliasToBean(WrapperExpiryMasterDto.class)).list();
					
				   for(WrapperExpiryMasterDto dto:transitionNumberList) {
						//LocalDate date2 =  LocalDate.now().plusDays(dto.getDuration());
						/*LocalDateTime kitExpiryDate=LocalDateTime.now().plusDays(dto.getDuration());
						 session.createQuery("update PreSterilizationProcess pre set pre.expiryDate='"+kitExpiryDate+"' where pre.transitionNumber=:id ")
								.setParameter("id", dto.getId()).executeUpdate();*/
						 
							
							List<PreSterilizationProcessInstrumentDto> kitItemList=sessionFactory.getCurrentSession().createQuery("Select "
									+ " pre.transitionNumber as id,pre.instrumentId as instrumentId,pre.physicalQuantity as physicalQuantity,pre.storeId as storeId"
									+ "  from PreSterilizationProcessInstrument pre  where pre.transitionNumber=:transitionNo)")
									.setParameter("transitionNo", dto.getId())
									.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessInstrumentDto.class)).list();
						
							//Update sterilized stock against item
							for(PreSterilizationProcessInstrumentDto kitItem:kitItemList) {
							sessionFactory.getCurrentSession().createQuery(" update CssdStockTransactionLog log "
									+ " set log.sterilizedQuantity=log.sterilizedQuantity+(:sterQty),log.inTransitQuantity=log.inTransitQuantity-(:sterQty),"
									+ " log.updatedBy=:updatedBy,log.updatedDate=:updatedDate"
									+ " where log.storeId=:storeId and log.itemId=:itemId and log.organizationId=:orgId and log.unitId=:unitId ")
							.setParameter("sterQty", kitItem.getPhysicalQuantity())
							.setParameter("updatedBy", loadBatchDto.getUpdated_by())
							.setParameter("updatedDate", TimeZoneComponent.getDateByzone(loadBatchDto.getUnitId()))
							.setParameter("storeId", kitItem.getStoreId())
							.setParameter("itemId", kitItem.getInstrumentId())
							.setParameter("orgId",loadBatch.getOrganizationId())
							.setParameter("unitId", loadBatch.getUnitId())
							.executeUpdate();
						}
					}
						                                              
				Integer count = session.createQuery("update PreSterilizationProcess pre set pre.isSterilized=true where pre.transitionNumber IN "
						+ "(select transitionNumber from LoadBatchKitMapper lbkm where lbkm.batchNumber=:batchNumber)")
						.setParameter("batchNumber", loadBatchDto.getBatchNumber()).executeUpdate();
				//System.out.println("Count for update : "+count);
				
			}else
			{
				loadBatch.setStatus("Unsterilized");
			}
			
			
			session.update(loadBatch);
			
			return new Response(SUCCESS, "200","Pending indicators saved.", null, null);
		} catch (Exception e) {
			logger.error("savePendingIndicators failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getBatchDetailsByBatchNumber(Integer organizationId,Integer unitId,Integer storeId,Integer batchNo) {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			LoadBatchDto loadBatchDto = (LoadBatchDto)statelessSession
					.createQuery("SELECT loadBatch.batchNumber as batchNumber,"
							+ "loadBatch.autoclaveNumber as autoclaveNumber,autoM.machine_name as autoclaveName,autoM.sterilization_color_id as sterilizationColorId, "
							+ "sterilizationTypeMaster.name as sterilizationTypeDesc, "
							+ "loadBatch.control as control, loadBatch.test as test, loadBatch.sterilizationTemperature as sterilizationTemperature,"
							+ "loadBatch.pressure as pressure, loadBatch.color as color, loadBatch.status as status,loadBatch.isSterilized as isSterilized "
							+ "FROM LoadBatch loadBatch "
							+ "INNER JOIN loadBatch.autoclaveMaster as autoM "
							+ "INNER JOIN autoM.sterilizationTypeMaster as sterilizationTypeMaster "
							+ "WHERE loadBatch.batchNumber=:batchNo and loadBatch.organizationId=:organizationId and loadBatch.unitId=:unitId and loadBatch.storeId=:storeId ")
					.setParameter("batchNo", batchNo)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setParameter("storeId", storeId)
					.setResultTransformer(Transformers.aliasToBean(LoadBatchDto.class)).uniqueResult();
			
			List<PreSterilizationProcessDto> kitList = statelessSession
					.createQuery("SELECT kitMaster.kitId as kitId,"
							+ "kitMaster.code as code,kitMaster.name as name, wrapperExpiryMaster.name as wrapperName,"
							+ "wrapperExpiryMaster.duration as expiryDuration, "
							+ "to_char(preSterilizationProcess.formation_date,'dd/MM/yyyy') as formation_date "
							+ "FROM LoadBatchKitMapper mapper "
							+ "INNER JOIN mapper.preSterilizationProcess as preSterilizationProcess "
							+ "INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
							+ "INNER JOIN preSterilizationProcess.wrapperExpiryMaster as wrapperExpiryMaster "
							+ "WHERE mapper.batchNumber=:batchNo ")
					.setParameter("batchNo", batchNo)
					.setResultTransformer(Transformers.aliasToBean(PreSterilizationProcessDto.class)).list(); 
				if(loadBatchDto != null)
				{
					loadBatchDto.setKitList(kitList);
				}
			
			
			statelessSession.close();
			return new Response(SUCCESS, null, null, null, loadBatchDto);
		} catch (Exception e) {
			logger.error("getBatchDetailsByBatchNumber failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getLoadBatchListToSendToMicrobiologyByMultipleFields(Integer organizationId,Integer unitId,Integer storeId,Date from,Date to,Integer batchNo,String status) {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			StringBuilder condition=new StringBuilder();
			
			if(batchNo != null)
			{
				condition.append(" AND loadBatch.batchNumber=:batchNo ");
			}
			if(status != null)
			{
				condition.append(" AND loadBatch.status=:status ");
			}
			
			Query createQuery= statelessSession
					.createQuery("SELECT loadBatch.batchNumber as batchNumber, "
							+ "loadBatch.autoclaveNumber as autoclaveNumber,to_char(loadBatch.autoclaveDate,'dd/MM/yyyy') as autoclaveDate, " 
							/*+ "to_char(autoclaveMaster.updated_date + autoclaveMaster.biological_indicator_day,'dd/MM/yyyy') as expiryDate, "*/
							+ "loadBatch.status as status,loadBatch.isSterilized as isSterilized FROM LoadBatch loadBatch INNER JOIN loadBatch.autoclaveMaster as autoclaveMaster "
							+ "WHERE loadBatch.biologicalIndicator=true AND loadBatch.organizationId=:organizationId AND loadBatch.unitId=:unitId AND loadBatch.storeId=:storeId "
							+ "AND loadBatch.created_date BETWEEN :from AND :to "
							+condition )
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setParameter("storeId", storeId)
					.setParameter("from", from)
					.setParameter("to", to);


			if(batchNo != null)
			{
				createQuery.setParameter("batchNo", batchNo);
			}
			if(status != null)
			{
				createQuery.setParameter("status", status);

			}

			List<LoadBatchDto> loadBatchDtoList =createQuery.setResultTransformer(Transformers.aliasToBean(LoadBatchDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, loadBatchDtoList, null);
		} catch (Exception e) {
			logger.error("getLoadBatchListToSendToMicrobiologyByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response sendToMicrobiology(LoadBatchDto loadBatchDto ) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			
			LoadBatch loadBatch = (LoadBatch)session.get(LoadBatch.class, loadBatchDto.getBatchNumber());
			
			if(loadBatch.getBiologicalIndicator())
			{
				loadBatch.setUpdated_date(TimeZoneComponent.getDateByzone(loadBatchDto.getUnitId()!=null ? loadBatchDto.getUnitId() :0));
				loadBatch.setStatus("Sent To Microbiology");
				session.update(loadBatch);
				return new Response(SUCCESS, "200","Sent to Microbiology Successfully.", null, null);
			}
			else
			{
				return new Response(ERROR, null,"Biological Indicator not required.", null, null);
			}
		} catch (Exception e) {
			logger.error("sendToMicrobiology failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*@Override
	public Response loadBatch(LoadBatchDto loadBatchDto ) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			
			LoadBatch loadBatch = mapper.map(loadBatchDto, LoadBatch.class,
					"LoadBatchDto_to_LoadBatch");
			loadBatch.setAutoclaveDate(TimeZoneComponent.getDateByzone(unitId));
			loadBatch.setCreated_date(TimeZoneComponent.getDateByzone(unitId));
			loadBatch.setUpdated_date(TimeZoneComponent.getDateByzone(unitId));
			loadBatch.setStatus("batchloaded");
			
			Integer loadBatchId=(Integer)session.save(loadBatch);
			
			List<LoadBatchKitMapperDto> loadBatchKitMapperDtoList=loadBatchDto.getKitList();
			for (LoadBatchKitMapperDto loadBatchKitMapperDto : loadBatchKitMapperDtoList) {
				LoadBatchKitMapper loadBatchKitMapper=new LoadBatchKitMapper();
				loadBatchKitMapper.setBatchNumber(loadBatchId);
				loadBatchKitMapper.setTransitionNumber(loadBatchKitMapperDto.getTransitionNumber());
				session.save(loadBatchKitMapper);
			}
			
			return new Response(SUCCESS, "200","Batch Loaded.", null, null);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, "500", "Internal Server Error", null, null);
	}*/
	
	
	
	@Override
	public Response getBatchListToSendToMicrobiology() {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<LoadBatchDto> loadBatchDtoList = statelessSession
					.getNamedQuery("GET_BATCH_LIST_TO_SEND_TO_MICROBIOLOGY")
					.setResultTransformer(Transformers.aliasToBean(LoadBatchDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, loadBatchDtoList, null);
		} catch (Exception e) {
			logger.error("getBatchListToSendToMicrobiology failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	@Override
	public Response getLoadBatchList() {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<LoadBatchDto> loadBatchDtoList = statelessSession
					.getNamedQuery("GET_LOADED_BATCH_LIST")
					.setResultTransformer(Transformers.aliasToBean(LoadBatchDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, loadBatchDtoList, null);
		} catch (Exception e) {
			logger.error("getLoadBatchList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getKitListByBatchNumber(Integer batchNo) {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<KitByBatchNoDto> kitByBatchNoDtoList = statelessSession
					.getNamedQuery("GET_KIT_LIST_BY_BATCH_NO")
					.setParameter("batchNo", batchNo)
					.setResultTransformer(Transformers.aliasToBean(KitByBatchNoDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, kitByBatchNoDtoList, null);
		} catch (Exception e) {
			logger.error("getKitListByBatchNumber failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	@Override
	public Response getLoadBatchListByMultipleFields(Date from,Date to,Integer batchNo, String status) {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<LoadBatchDto> loadBatchDtoList = statelessSession
					.getNamedQuery("GET_LOADED_BATCH_LIST_BY_MULTIPLE_FIELDS")
					.setParameter("batchNo", batchNo)
					.setParameter("from", from)
					.setParameter("to", to)
					.setParameter("status", status)
					.setResultTransformer(Transformers.aliasToBean(LoadBatchDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, loadBatchDtoList, null);
		} catch (Exception e) {
			logger.error("getLoadBatchListByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	public Response getBatchNoListForControl() {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<String> batchNoListForControl = statelessSession
					.getNamedQuery("GET_BATCH_NO_LIST_FOR_CONTROL")
					.list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, batchNoListForControl, null);
		} catch (Exception e) {
			logger.error("getBatchNoListForControl failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getBatchNoListForTest() {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<String> batchNoListForTest = statelessSession
					.getNamedQuery("GET_BATCH_NO_LIST_FOR_TEST")
					.list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, batchNoListForTest, null);
		} catch (Exception e) {
			logger.error("getBatchNoListForTest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getBatchListForApplication(Integer batchNo,Date fromDate,Date toDate,Integer autoclaveNo,String status) {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<LoadBatchDto> loadBatchDtoList = statelessSession
					.getNamedQuery("GET_LOADED_BATCH_LIST_FOR_APPLICATION")
					.setParameter("batchNo", batchNo)
					.setParameter("from", fromDate)
					.setParameter("to", toDate)
					.setParameter("autoclaveNo", autoclaveNo)
					.setParameter("status", status)
					.setResultTransformer(Transformers.aliasToBean(LoadBatchDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null, null, loadBatchDtoList, null);
		} catch (Exception e) {
			logger.error("getBatchListForApplication failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	

}

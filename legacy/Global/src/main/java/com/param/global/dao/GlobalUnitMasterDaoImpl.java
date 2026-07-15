package com.param.global.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.param.billing.global.model.DepositMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.dto.UserMasterDto;
import com.param.global.model.UnitSubscriptionConfig;
import com.param.global.model.UnitSubscriptionDetails;
import com.param.global.org.dto.UnitSubscriptionConfigDto;
import com.param.global.org.dto.UnitSubscriptionDetailsDto;
import com.param.global.service.OrderMasterServiceImpl;
import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.master.global.model.UnitMaster;
import com.param.entity.model.master.Organization;
import com.param.entity.model.master.PrefixRunningNumber;
import com.param.entity.model.master.Screen;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Unit;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.doctor.dto.ScreenNumberGenerationDto;
import com.param.global.dto.InvestigationOrderMasterNewDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.sync.ScheduleAppointmentReqDto;
import com.param.global.dto.sync.UnitMasterDtoSync;
import com.param.global.org.dto.UnitMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class GlobalUnitMasterDaoImpl extends GenericDao<UnitMaster, Integer> implements IGlobalUnitMasterDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(GlobalUnitMasterDaoImpl.class);
	
	public GlobalUnitMasterDaoImpl() {
		super(UnitMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private SessionService sessionService;


	@Override
	public Response getUnitMasterForSyncById(Integer unitId) throws ApplicationException {
		try {
			UnitMasterDtoSync unit = (UnitMasterDtoSync) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_UNITMASTERRECORD_FORSYNC_BY_ID").setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UnitMasterDtoSync.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, unit);
		} catch (HibernateException he) {
			logger.error("Error while fetching unit master for sync by id", he);
		} catch (Exception e) {
			logger.error("Error while fetching unit master for sync by id", e);
		}
		return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
	}

	@Override
	public Response saveUnitMaster(UnitMasterDto unitMasterDto) throws ApplicationException {
		try {
			UnitMaster unitMaster = new UnitMaster();
				unitMaster.setOrganizationId(unitMasterDto.getOrganizationId());
				unitMaster.setUnitCode(unitMasterDto.getUnitCode());
				unitMaster.setCreatedBy(1);
				unitMaster.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(unitMasterDto.getUnitId()), "dd-M-yyyy"));
				unitMaster.setPostCode(unitMasterDto.getPostCode());
				unitMaster.setUnitName(unitMasterDto.getUnitName());
				unitMaster.setUnitCode(unitMasterDto.getUnitCode());
				unitMaster.setUnitCountryId(unitMasterDto.getUnitCountryId());
				unitMaster.setUnitStateId(unitMasterDto.getUnitStateId());
				unitMaster.setUnitCityId(unitMasterDto.getUnitCityId());
				unitMaster.setUnitDistrictId(unitMasterDto.getUnitDistrictId());
				unitMaster.setUnitAreaId(unitMasterDto.getUnitAreaId());
				unitMaster.setUnitAddress(unitMasterDto.getUnitAddress());
				unitMaster.setUnitEmailId(unitMasterDto.getUnitEmailId());
				unitMaster.setUnitLogo(unitMasterDto.getUnitLogo());
				unitMaster.setUnitFolder(unitMasterDto.getUnitFolder());
				unitMaster.setStatus(unitMasterDto.getStatus());
				unitMaster.setUnitDescription(unitMasterDto.getUnitDescription());
				unitMaster.setUnitContact(unitMasterDto.getUnitContact());
				unitMaster.setRegistrationNumber(unitMasterDto.getRegistrationNumber());
	            unitMaster.setApplicationUrl(unitMasterDto.getApplicationUrl());
	            unitMaster.setUnitTimeZone(unitMasterDto.getUnitTimeZone()!=null ? unitMasterDto.getUnitTimeZone():"Asia/Calcutta");
			unitMaster = save(unitMaster);

			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, unitMaster);

		} catch (HibernateException he) {
			logger.error("Error while saving unit master", he);
		} catch (Exception e) {
			logger.error("Error while saving unit master", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateUnitMaster(UnitMasterDto unitMasterDto) throws ApplicationException {
		try {

			UnitMaster unitMaster = (UnitMaster)sessionFactory.getCurrentSession().get(UnitMaster.class, unitMasterDto.getUnitId());
				//unitMaster.setUnitId(unitMasterDto.getUnitId());
				unitMaster.setOrganizationId(unitMasterDto.getOrganizationId());
				unitMaster.setUnitCode(unitMasterDto.getUnitCode());
				unitMaster.setPostCode(unitMasterDto.getPostCode());
				unitMaster.setUnitName(unitMasterDto.getUnitName());
				unitMaster.setUnitCode(unitMasterDto.getUnitCode());
				unitMaster.setUnitCountryId(unitMasterDto.getUnitCountryId());
				unitMaster.setUnitStateId(unitMasterDto.getUnitStateId());
				unitMaster.setUnitCityId(unitMasterDto.getUnitCityId());
				unitMaster.setUnitDistrictId(unitMasterDto.getUnitDistrictId());
				unitMaster.setUnitAreaId(unitMasterDto.getUnitAreaId());
				unitMaster.setUnitAddress(unitMasterDto.getUnitAddress());
				unitMaster.setUnitEmailId(unitMasterDto.getUnitEmailId());
				unitMaster.setUnitLogo(unitMasterDto.getUnitLogo());
				unitMaster.setUnitFolder(unitMasterDto.getUnitFolder());
				unitMaster.setStatus(unitMasterDto.getStatus());
				unitMaster.setUnitDescription(unitMasterDto.getUnitDescription());
				unitMaster.setUnitContact(unitMasterDto.getUnitContact());
				unitMaster.setRegistrationNumber(unitMasterDto.getRegistrationNumber());
	            unitMaster.setApplicationUrl(unitMasterDto.getApplicationUrl());
	            unitMaster.setUnitTimeZone(unitMasterDto.getUnitTimeZone()!=null ? unitMasterDto.getUnitTimeZone():"Asia/Calcutta");
	            unitMaster.setProcessingUnit(unitMasterDto.getProcessingUnit());
			unitMaster = update(unitMaster);

			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, unitMaster);
		} catch (HibernateException he) {
			logger.error("Error while updating unit master", he);
		} catch (Exception e) {
			logger.error("Error while updating unit master", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getUnitById(Integer unitId) throws ApplicationException {
		try {
			UnitMasterDto unitMasterDto = (UnitMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_UNIT_MASTER_BY_ID").setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UnitMasterDto.class)).uniqueResult();
			/*if (unitId != null) {
				List<OrganizationUnitLicenceDetailsDto> listOrganizationUnitLicenceDetailsDto = null;
				String query = sessionFactory.getCurrentSession().getNamedQuery("GET_LICENCE_DETAILS").getQueryString();
				query = (query + " WHERE licence.unit_id =" + unitId + " and licence.is_unit='A' ");
				listOrganizationUnitLicenceDetailsDto = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setResultTransformer(Transformers.aliasToBean(OrganizationUnitLicenceDetailsDto.class)).list();
			}*/
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, unitMasterDto);
		} catch (HibernateException he) {
			logger.error("Error while fetching unit by id", he);
		} catch (Exception e) {
			logger.error("Error while fetching unit by id", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getUnitByName(UnitMasterDto unitMasterDto) throws ApplicationException {
		try {
			List<UnitMasterDto> listUnitMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_UNIT_BY_NAME")
					.setParameter("unitName", unitMasterDto.getUnitName().toLowerCase().trim())
					.setResultTransformer(Transformers.aliasToBean(UnitMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listUnitMasterDto, null);
		} catch (HibernateException he) {
			logger.error("Error while fetching unit by name", he);
		} catch (Exception e) {
			logger.error("Error while fetching unit by name", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getUnitByNameAndNotById(UnitMasterDto unitMasterDto) throws ApplicationException {
		try {
			List<UnitMasterDto> listUnitMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_UNIT_BY_NAME_AND_NOT_BY_ID")
					.setParameter("unitName", unitMasterDto.getUnitName().toLowerCase().trim())
					.setParameter("unitId", unitMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(UnitMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listUnitMasterDto, null);
		} catch (HibernateException he) {
			logger.error("Error while fetching unit by name excluding id", he);
		} catch (Exception e) {
			logger.error("Error while fetching unit by name excluding id", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateUnitStatusById(UnitMasterDto unitMasterDto) throws ApplicationException {
		try {
			Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_UNIT_STATUS_BY_ID")
					.setParameter("unitId", unitMasterDto.getUnitId()).setCharacter("status", unitMasterDto.getStatus())
					.executeUpdate();
			if (result != null) {
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}

		} catch (HibernateException he) {
			logger.error("Error while updating unit status by id", he);
		} catch (Exception e) {
			logger.error("Error while updating unit status by id", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getAllUnitList(UnitMasterDto unitMasterDto) throws ApplicationException {
		try {
			List<UnitMasterDto> listUnitMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALL_UNIT_LIST")
					// .setParameter("organizationId",
					// unitMasterDto.getOrganizationId()).
					.setFirstResult(unitMasterDto.getOffSet() != null ? unitMasterDto.getOffSet() : 0)
					.setMaxResults(
							unitMasterDto.getNoOfRecordsPerPage() != null ? unitMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(UnitMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listUnitMasterDto, null);
		} catch (HibernateException he) {
			logger.error("Error while fetching all unit list", he);
		} catch (Exception e) {
			logger.error("Error while fetching all unit list", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getUnitTotalCount(int orgId) throws ApplicationException {
		try {
			UnitMaster un;
			Session session = sessionFactory.getCurrentSession();
			Criteria criteria = session.createCriteria(UnitMaster.class);
			// criteria.add(Restrictions.eq("organizationId", orgId));//minesh
			criteria.setProjection(Projections.rowCount());
			Long count = (Long) criteria.uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, count);
		} catch (HibernateException he) {
			logger.error("Error while fetching unit total count", he);
		} catch (Exception e) {
			logger.error("Error while fetching unit total count", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateFolderPath(UnitMasterDto unitMasterDto) throws ApplicationException {
		try {
			Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_UNIT_FOLDER_PATH")
					.setParameter("unitId", unitMasterDto.getUnitId())
					.setParameter("unitFolder", unitMasterDto.getUnitFolder()).executeUpdate();
			if (result != null) {
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}

		} catch (HibernateException he) {
			logger.error("Error while updating unit folder path", he);
		} catch (Exception e) {
			logger.error("Error while updating unit folder path", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateImagePath(UnitMasterDto unitMasterDto) throws ApplicationException {
		try {
			Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_UNIT_IMAGE_PATH")
					.setParameter("unitId", unitMasterDto.getUnitId()).setBinary("image", unitMasterDto.getImage())
					.setParameter("unitLogo", unitMasterDto.getUnitLogo()).executeUpdate();
			if (result != null) {
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}

		} catch (HibernateException he) {
			logger.error("Error while updating unit image path", he);
		} catch (Exception e) {
			logger.error("Error while updating unit image path", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getUnitFolderPath(int unitId) throws ApplicationException {
		try {
			UnitMasterDto unitMasterDto = (UnitMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_UNIT_MASTER_BY_ID").setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UnitMasterDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, unitMasterDto);
		} catch (HibernateException he) {
			logger.error("Error while fetching unit folder path", he);
		} catch (Exception e) {
			logger.error("Error while fetching unit folder path", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

@Override
public  String getGeneratedNo(int storeId, int unitId, int screenId) throws ApplicationException {
	 try{
		
		 	Integer p_running_number=0;
		 	String p_new_running_number;
		 	
		 	StringBuilder query = new StringBuilder("SELECT cast(prn.id as int4) as \"p_prefix_id\", "
		 			+ " prn.running_number as \"p_running_number\", "
		 			+ " prn.reset_id as \"p_reset_id\" , "
		 			+ " prn.length as \"p_length\", "
		 			+ " prn.format_id as \"p_format_id\", "
		 			+ " prn.generated_code as \"p_generated_code\", "
		 			+ " prn.organization_id \"p_organization_id\", "
		 			+ " unit.unit_code as \"p_unit_code\", "
		 			+ " coalesce((SELECT m_store.store_code "
					+ " FROM public.m_store ");
		 	if(storeId > 0 )
		 		query.append(" WHERE m_store.store_id ="+storeId+" ");
		 	else
		 		query.append(" WHERE m_store.store_id is null ");
				    
		 		query.append( " AND m_store.unit_id="+unitId+")) as \"p_store_code\" "
		 			+" FROM  public.m_prefix_configuration  prn  "
		 			+ " inner join m_screen scr on " 
		 			 + " 	scr.id = prn.screen_id " 
		 			+ "INNER JOIN m_unit_master unit on unit.unit_id = prn.unit_id "
		 			+" WHERE  prn.screen_id = "+screenId+" "
		 			+ "AND prn.is_active = true "
		 			+ "AND prn.is_deleted = false "
		 			+ "AND scr.is_global = false "
		 			+ "AND prn.unit_id ="+unitId+" "
		 			+" LIMIT 1 ");
		 	
		 	
		 	ScreenNumberGenerationDto dto = (ScreenNumberGenerationDto) sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setResultTransformer(Transformers.aliasToBean(ScreenNumberGenerationDto.class)).uniqueResult();
		 
		 	
		 if(dto!=null && dto.getP_prefix_id()!=null && dto.getP_prefix_id()>0)
		 {
			 
			 StringBuilder string2 = new StringBuilder(" SELECT cast(id as int4) as \"p_prefix_running_id\", "
					 + "current_running as \"p_current_running_number\" "
				     +" FROM  public.t_prefix_running_number AS prn ");
			 if(storeId > 0)
			 {
				 string2.append(" WHERE prn.store_id="+storeId);
				 string2.append(" AND prn.screen_id = "+screenId);
			 }
			 else
			 {
				 string2.append(" WHERE prn.screen_id = "+screenId);
			 }
			 string2.append(" AND prn.is_active = true AND prn.is_deleted = false AND prn.unit_id = "+unitId+" 	"
						 +"LIMIT 1 for update");
			 
			 ScreenNumberGenerationDto dto2 = (ScreenNumberGenerationDto) sessionFactory.getCurrentSession().createSQLQuery(string2.toString())
				.setResultTransformer(Transformers.aliasToBean(ScreenNumberGenerationDto.class))
			 	.uniqueResult();
			 
			 if(dto2!=null && dto2.getP_prefix_running_id()!=null && dto2.getP_prefix_running_id() > 0)
			 {
				 if(dto2.getP_current_running_number()!=null && dto2.getP_current_running_number()>0)
				 {
					 p_running_number = dto2.getP_current_running_number()+1;

						 sessionFactory.getCurrentSession().createQuery(" UPDATE PrefixRunningNumber "
									+ "SET currentRunning="+p_running_number+" "
									+" WHERE id="+dto2.getP_prefix_running_id())
									.executeUpdate();
						 
						 logger.info("getGeneratedNo prefix running number ID " + dto2.getP_prefix_running_id() + "prefix running number "+ p_running_number);
					
				 }
			 }else
			 {
				 p_running_number = dto.getP_running_number()!=null ? dto.getP_running_number() : 1;
				
					 PrefixRunningNumber prefixRunningNumber = new PrefixRunningNumber();
						 Organization org = new Organization();
						 	org.setOrganizationId(dto.getP_organization_id());
						 prefixRunningNumber.setOrganizationId(dto.getP_organization_id());
						 Unit unit = new Unit();
						 	unit.setUnitId(unitId);
						 prefixRunningNumber.setUnitId(unitId);
						 if(storeId>0)
						 {
							 Store store = new Store();
							 store.setStoreId(storeId);
							 prefixRunningNumber.setStore(store);
						 }
						 Screen screen = new Screen();
						 	screen.setId(screenId);
						 
						 prefixRunningNumber.setScreen(screen);
						 prefixRunningNumber.setCurrentRunning(p_running_number);
						 prefixRunningNumber.setIsActive(true);
						 prefixRunningNumber.setIsDeleted(false);
					 sessionFactory.getCurrentSession().save(prefixRunningNumber);
					 
			 }
			 
			 Date newDate = TimeZoneComponent.getDateByzone(unitId);
			 
			 String currentDate = GlobalCommonDateUtils.getStringDate(newDate , "yyyy-MM-dd HH:mm:ss");
			 
			 String currentDate2 = GlobalCommonDateUtils.getStringDate(newDate , "MMM");
				
			 String currentDate3 = GlobalCommonDateUtils.getStringDate(newDate , "MMMM");
			 
			 
			 p_new_running_number = dto.getP_generated_code();
			 
			 p_new_running_number=p_new_running_number.replace("[YYYY]", currentDate.substring(0, 4));
			 p_new_running_number=p_new_running_number.replace("[YY]", currentDate.substring(2, 4));
			 p_new_running_number=p_new_running_number.replace("[MM]", currentDate.substring(5, 7));
			 p_new_running_number=p_new_running_number.replace("[MMM]", currentDate2);
			 p_new_running_number=p_new_running_number.replace("[MMMM]", currentDate3);
			 p_new_running_number=p_new_running_number.replace("[DD]", currentDate.substring(8, 10));
			 p_new_running_number=p_new_running_number.replace("[/]","/");
			 p_new_running_number=p_new_running_number.replace("[-]","-");
			 p_new_running_number=p_new_running_number.replace("[SF]",p_running_number.toString());
			 p_new_running_number=p_new_running_number.replace("[UC]",dto.getP_unit_code()!=null ? dto.getP_unit_code():"");
			 p_new_running_number=p_new_running_number.replace("[SC]",dto.getP_store_code()!=null ? dto.getP_store_code():"");
			 p_new_running_number=p_new_running_number.replace("|","");
			 p_new_running_number=p_new_running_number.replace("{","");
			 p_new_running_number=p_new_running_number.replace("}","");
			 
			 return p_new_running_number;
			 }
		 	else{

		 		return null;
		 }

	  }catch(HibernateException he)
	  {
		  logger.error("Error while generating running number", he);
	  }catch(Exception e){
		  logger.error("Error while generating running number", e);
	  }
	 return null;
  }


@Override
public  String getGeneratedGlobalNo(int storeId, int orgId,int unitId, int screenId) throws ApplicationException {
	 try{
		
		 	Integer p_running_number=0;
		 	String p_new_running_number;
		 	StringBuilder query = null;
		 	StringBuilder query2 = null;
//		 	if(unitId == 0) {
		 		 query = new StringBuilder( " select " 
			 			 + " 	cast(prn.id as int4) as \"p_prefix_id\", " 
			 			 + " 	prn.running_number as \"p_running_number\", " 
			 			 + " 	prn.reset_id as \"p_reset_id\" , " 
			 			 + " 	prn.length as \"p_length\", " 
			 			 + " 	prn.format_id as \"p_format_id\", " 
			 			 + " 	prn.generated_code as \"p_generated_code\", " 
			 			 + " 	prn.organization_id \"p_organization_id\", " 
			 			 + " 	org.organization_code as \"p_unit_code\" " 
			 			 + " 	from public.m_prefix_configuration prn " 
			 			 + " inner join m_screen scr on " 
			 			 + " 	scr.id = prn.screen_id "
			 			 + " inner join m_organization_master org on "
			 			 + " 	org.organization_id =  prn.organization_id "
			 			 + " where " 
			 			 + " 	prn.screen_id =:screenId  "
			 			 + " 	and prn.is_active = true " 
			 			 + " 	and prn.is_deleted = false " 
			 			 + " 	and scr.is_global = true "
			 			 + "	and prn.organization_id=:orgId "
			 			 + " order by 1 limit 1 " );
//		 	}
		 	
		 		ScreenNumberGenerationDto dto = (ScreenNumberGenerationDto) sessionFactory.getCurrentSession()
						.createSQLQuery(query.toString())
						.setParameter("screenId",screenId)
						.setParameter("orgId",orgId)
						.setResultTransformer(Transformers.aliasToBean(ScreenNumberGenerationDto.class)).uniqueResult();
		 	
		 	if(unitId != 0) {
		 		 query2 = new StringBuilder( " select " 
			 			 + " unit.unit_code as \"p_unit_code\" " 
			 			 + " from m_unit_master unit "
			 			 + " where	unit.unit_id =:unitId "
			 			 + " order by 1 limit 1 " );
		 		ScreenNumberGenerationDto dto2 = (ScreenNumberGenerationDto) sessionFactory.getCurrentSession()
						.createSQLQuery(query2.toString())
						.setParameter("unitId",unitId)
						.setResultTransformer(Transformers.aliasToBean(ScreenNumberGenerationDto.class)).uniqueResult();
		 		 if(dto2!=null && dto != null) {
		 			dto.setP_unit_code(dto2.getP_unit_code());
		 		 }
			 
		 	}
		 
		 	
		 	
		 	
		 	
		 	
		 	
		 if(dto!=null && dto.getP_prefix_id()!=null && dto.getP_prefix_id()>0)
		 {
			 
			 StringBuilder string2 = new StringBuilder( " select " 
					 + " 	cast(id as int4) as \"p_prefix_running_id\", " 
					 + " 	current_running as \"p_current_running_number\" " 
					 + " from " 
					 + " 	public.t_prefix_running_number as prn " 
					 + " where " 
					 + " 	prn.screen_id ="+screenId+" " 
					 + " 	and prn.is_active = true " 
					 + " 	and prn.is_deleted = false " 
					 + " 	and prn.organization_id =:orgId "
					 + " limit 1 for update");
			 
			 ScreenNumberGenerationDto dto2 = (ScreenNumberGenerationDto) sessionFactory.getCurrentSession()
					 .createSQLQuery(string2.toString())
					 .setParameter("orgId",orgId)
				.setResultTransformer(Transformers.aliasToBean(ScreenNumberGenerationDto.class))
			 	.uniqueResult();
			 
			 if(dto2!=null && dto2.getP_prefix_running_id()!=null && dto2.getP_prefix_running_id() > 0)
			 {
				 if(dto2.getP_current_running_number()!=null && dto2.getP_current_running_number()>0)
				 {
					 p_running_number = dto2.getP_current_running_number()+1;
					 
					 sessionFactory.getCurrentSession().createQuery(" UPDATE PrefixRunningNumber "
																	+ "SET currentRunning="+p_running_number+" "
																	+" WHERE id="+dto2.getP_prefix_running_id())
																	.executeUpdate();
					 logger.info("getGeneratedGlobalNo prefix running number ID " + dto2.getP_prefix_running_id() + "prefix running number "+ p_running_number);
				 }
			 }else
			 {
				 p_running_number = dto.getP_running_number()!=null ? dto.getP_running_number() : 1;
				 
				 /*String insertQuery = "INSERT INTO public.t_prefix_running_number "
				 +" (organization_id, screen_id, current_running, is_active, is_deleted) "
				 +" VALUES("+orgId+", "+screenId+", "+p_running_number+", true, false)" ; 
				 sessionFactory.getCurrentSession().createSQLQuery(insertQuery)
				 .executeUpdate();*/
				 
				 PrefixRunningNumber prefixRunningNumber = new PrefixRunningNumber();
					 Organization org = new Organization();
					 	org.setOrganizationId(dto.getP_organization_id());
					 prefixRunningNumber.setOrganization(org);
					 prefixRunningNumber.setOrganizationId(dto.getP_organization_id());
					 Screen screen = new Screen();
					 	screen.setId(screenId);
					 
					 prefixRunningNumber.setScreen(screen);
					 prefixRunningNumber.setCurrentRunning(p_running_number);
					 prefixRunningNumber.setIsActive(true);
					 prefixRunningNumber.setIsDeleted(false);
				 sessionFactory.getCurrentSession().save(prefixRunningNumber);
			 }
			 
			 Date newDate = TimeZoneComponent.getDateByzone(0);
			 
			 String currentDate = GlobalCommonDateUtils.getStringDate(newDate , "yyyy-MM-dd HH:mm:ss");
			 
			 String currentDate2 = GlobalCommonDateUtils.getStringDate(newDate , "MMM");
				
			 String currentDate3 = GlobalCommonDateUtils.getStringDate(newDate , "MMMM");
			 
			 
			 p_new_running_number = dto.getP_generated_code();
			 
			 p_new_running_number=p_new_running_number.replace("[YYYY]", currentDate.substring(0, 4));
			 p_new_running_number=p_new_running_number.replace("[YY]", currentDate.substring(2, 4));
			 p_new_running_number=p_new_running_number.replace("[MM]", currentDate.substring(5, 7));
			 p_new_running_number=p_new_running_number.replace("[MMM]", currentDate2);
			 p_new_running_number=p_new_running_number.replace("[MMMM]", currentDate3);
			 p_new_running_number=p_new_running_number.replace("[DD]", currentDate.substring(8, 10));
			 p_new_running_number=p_new_running_number.replace("[/]","/");
			 p_new_running_number=p_new_running_number.replace("[-]","-");
			 p_new_running_number=p_new_running_number.replace("[SF]",p_running_number.toString());
			 p_new_running_number=p_new_running_number.replace("[UC]",dto.getP_unit_code()!=null ? dto.getP_unit_code():"");
			 p_new_running_number=p_new_running_number.replace("[SC]",dto.getP_store_code()!=null ? dto.getP_store_code():"");
			 p_new_running_number=p_new_running_number.replace("|","");
			 p_new_running_number=p_new_running_number.replace("{","");
			 p_new_running_number=p_new_running_number.replace("}","");
			 
			 return p_new_running_number;
			 }
		 	else{

		 		return null;
		 }

	  }catch(HibernateException he)
	  {
		  logger.error("Error while generating global running number", he);
	  }catch(Exception e){
		  logger.error("Error while generating global running number", e);
	  }
	 return null;
  }


	@Override
	public String getUnitImage(int unitId) throws ApplicationException {
		try {
			String unitImage = (String) sessionFactory.getCurrentSession().getNamedQuery("GET_UNIT_IMAGE")
					.setParameter("unitId", unitId).uniqueResult();
			return unitImage;
		} catch (HibernateException he) {
			logger.error("Error while fetching unit image", he);
		} catch (Exception e) {
			logger.error("Error while fetching unit image", e);
		}
		return null;
	}

	@Override
	public Response getUnitByOrganisation(int orgId) throws ApplicationException {
		try {
			List<UnitMasterDto> listUnitMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_UNIT_BY_ORG")
					.setParameter("orgId", orgId).setResultTransformer(Transformers.aliasToBean(UnitMasterDto.class))
					.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listUnitMasterDto, null);
		} catch (HibernateException he) {
			logger.error("Error while fetching unit by organisation", he);
		} catch (Exception e) {
			logger.error("Error while fetching unit by organisation", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateGlobalUnitId(Integer localUnitId, Integer globalUnitId) {
		try {

			Integer result = sessionFactory.getCurrentSession()
					.createSQLQuery("update m_unit_master set global_unit_id=:globalUnitId where unit_id=:unitId ")
					.setParameter("unitId", localUnitId)
					.setParameter("globalUnitId", globalUnitId)
					.executeUpdate();
			if (result != null) {
				return new Response<>(SUCCESS, null, null, null, null);
			}
		} catch (HibernateException he) {
			logger.error("Error while updating global unit id", he);
		} catch (Exception e) {
			logger.error("Error while updating global unit id", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveUnitListForFolderCreation() throws ApplicationException {
		try {
			List<UnitMasterDto> listUnitMasterDto = sessionFactory.getCurrentSession()
					.createSQLQuery("select unit.unit_id as \"unitId\",unit.unit_name as \"unitName\","
							+ " unit.unit_folder as \"unitFolder\",org.organization_folder as \"organizationFolder\" "
							+ " from m_unit_master unit \r\n" 
							+" inner join m_organization_master org "  
							+" on org.organization_id=unit.organization_id "
							+ " where unit.status='A' order by unit.unit_id ")
					.setResultTransformer(Transformers.aliasToBean(UnitMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listUnitMasterDto, null);
		} catch (HibernateException he) {
			logger.error("Error while fetching active unit list for folder creation", he);
		} catch (Exception e) {
			logger.error("Error while fetching active unit list for folder creation", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public UnitMasterDto getUnitsAddress(Integer unitId) throws ApplicationException {
		try {
				UnitMasterDto unitMasterDto = (UnitMasterDto)sessionFactory.getCurrentSession()
					.createSQLQuery(" select " 
							 + " 	unit_country_id as \"unitCountryId\", " 
							 + " 	unit_state_id as \"unitStateId\", " 
							 + " 	unit_district_id as \"unitDistrictId\", " 
							 + " 	unit_city_id as \"unitCityId\", " 
							 + " 	unit_area_id as \"unitAreaId\", " 
							 + " 	mcm.country_calling_code as \"countryCallingCode\", "
							 + "	mam.area_code as \"postCode\" " 
							 + " from " 
							 + " 	m_unit_master mum " 
							 + " inner join m_country_master mcm on " 
							 + " 	mum.unit_country_id = mcm.country_id " 
							 + " inner join m_area_master mam  on "
							 + " 	mam.area_id = unit_area_id "
							 + " where 	 " 
							 + " 	mum.unit_id =:unitId ")
						.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UnitMasterDto.class)).uniqueResult();
			return unitMasterDto;
		} catch (HibernateException he) {
			logger.error("Error while fetching unit address", he);
		} catch (Exception e) {
			logger.error("Error while fetching unit address", e);
		}
		return null;
	}

	@Override
	public Response getAllLocalIdsByGlobalIds(ScheduleAppointmentReqDto scheduleAppointmentReqDto)
			throws ApplicationException {
		try{
			ScheduleAppointmentReqDto localIdsDto=(ScheduleAppointmentReqDto)sessionFactory.getCurrentSession()
					.createSQLQuery("select tpr.patient_id as \"patientId\"," 
							+"mom.organization_id as \"hospitalId\",mum.unit_id as \"unitId\"," 
							+"(select mdm.doctor_id from doctor.m_doctor_master mdm where mdm.global_doc_id =:globalDocId) as \"doctorId\"  "  
							+"from patient.t_patient_registration tpr " 
							+"inner join public.m_organization_master mom  on mom.organization_id =tpr.organization_id " 
							+"inner join public.m_unit_master mum on mum.unit_id =tpr.unit_id "  
							+"where tpr.uuid =:uuid "  
							+"and mom.global_org_id =:globalOrgId "  
							+"and mum.global_unit_id =:globalUnitId ")
					.setParameter("uuid", scheduleAppointmentReqDto.getUuid())
					.setParameter("globalOrgId",scheduleAppointmentReqDto.getGlobalOrgId())
					.setParameter("globalUnitId",scheduleAppointmentReqDto.getGlobalUnitId())
					.setParameter("globalDocId",scheduleAppointmentReqDto.getDoctorId())
					.setResultTransformer(Transformers.aliasToBean(ScheduleAppointmentReqDto.class))
					.uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, localIdsDto);
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getInvestigationsListForCoversheet(Integer patientId, boolean flag, Integer unitId) {
		//StatelessSession statlessSession = sessionFactory.openStatelessSession();
		/*StringBuilder strBlr=new StringBuilder(statlessSession.getNamedQuery("GET_INVESTIGATIONS_LIST_FOR_COVERSHEET").getQueryString());
		
		List<InvestigationOrderDto> investigationDTOsList=statlessSession.createSQLQuery(strBlr.toString())
				.setParameter("patientId", patientId)
				.setParameter("organizationId", organizationId)
				.setResultTransformer(Transformers.aliasToBean(InvestigationOrderDto.class)).list();
		statlessSession.close();
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, investigationDTOsList, null);	
	}*/
		/*StringBuilder strBlr=new StringBuilder(statlessSession.getNamedQuery("GET_CUEERENT_ORDERED_INVESTIGATION").getQueryString());
			strBlr.append(" where inv.\"patientId\"="+patient_id);
			strBlr.append(" order by inv.\"createdDateNew\" desc");
		List<InvestigationOrderDto> investigationDTOsList=statlessSession.createSQLQuery(strBlr.toString()).setResultTransformer(Transformers.aliasToBean(InvestigationOrderDto.class)).list();*/
		
		
		StringBuilder sb = new  StringBuilder();
		sb.append("(select "  + 
				" ord_mst.order_id as \"orderId\", " + 
				" service_mst.speciality_id as \"specialityId\"," + 
				" service_mst.sub_speciality_id as \"subDeptId\"," + 
				" service_mst.sub_speciality_id as \"subSpecialityId\"," + 
				"ord_dtls.is_procedure_service as \"isProcedureService\", " + 
				" ord_mst.admission_id as \"admissionId\", " + 
				" ord_mst.encounter_id as \"encounterId\", " + 
				" ord_mst.patient_id as \"patientId\", " + 
				" ord_dtls.order_details_id as \"orderDetailsId\", " + 
				" ord_dtls.service_id as \"serviceMasterId\", " + 
				" ord_dtls.order_set_id as \"orderSetId\", " + 
				" ord_dtls.priority_id as \"priorityId\", " + 
				" ord_dtls.ord_remarks as \"ordRemarks\", " + 
				" to_char(ord_dtls.order_sch_date,'dd/MM/yyyy') as \"orderSchDate\", " + 
				" ord_dtls.order_sch_date as \"orderSchDateNew\", " + 
				" service_mst.service_standard_name as \"serviceStandardName\", " + 
				" service_mst.service_standard_code as \"serviceStandardCode\", " + 
				" speciality_mst.speciality_name as \"specialityName\", " + 
				" sample_dtls.lab_sample_dtls_id as \"labSampleDtlsId\", " + 
				" service_dtls.is_panel as \"isPanel\", " + 
				" lab_res.lab_test_res_id  as \"labTestResId\", " + 
				" cast( " + 
				" 	null as integer " + 
				" ) as \"panelId\", " + 
				" (case when sample_dtls.micro_template_status_id is not null then test_status2.status_name else test_status.status_name end) as \"statusName\", " + 
				" (case when sample_dtls.micro_template_status_id is not null then test_status2.test_status_id else test_status.test_status_id end) as \"sampleStatusId\", " + 
				" to_char( " + 
				" 	ord_dtls.created_date, " + 
				" 	'dd/MM/yyyy HH24:MI' " + 
				" ) as \"createdDate\", " + 
				" to_char( " + 
				" 	sample_dtls.sample_gen_datetime, " + 
				" 	'dd/MM/yyyy HH24:MI' " + 
				" ) as \"sampleGenDatetime\", " + 
				" 	ord_dtls.created_date as \"createdDateNew\", " + 
				" ord_dtls.service_rendered as \"serviceRendered\", " + 
				" ord_dtls.is_package_service as \"isPackageService\", " + 
				" templatetr12_.template_transaction_id as \"templateTransactionId\"," + 
//				" (case when (sample_dtls.micro_template_status_id is not null and sample_dtls.micro_template_status_id=41)" + 
//				" then microTemp.template_object else templatetr12_.template_object end) as \"templateObject\", " + 
				" ord_dtls.service_is_billed as \"serviceIsBilled\", " + 
				" ord_dtls.status as \"orderStatus\", " + 
//				" am.appointment_id as \"appointmentId\", " + 
//				" am.slot_id as \"slotId\", " + 
				 " sample_dtls.micro_template_status_id as \"microTemplateStatusId\" , "
				 + " (case  when empmst1.employee_id is not null then concat(empmst1.first_name, ' ', empmst1.last_name) " + 
				 " when docmst1.doctor_id is not null then concat('Dr. ', docmst1.first_name, ' ', docmst1.last_name) "
				 + "  when sample_dtls.lab_sample_dtls_id is  null then concat(' ') "
				 + " else 'Super Admin' end) as \"collectedUserName\","
//				 +" concat(empmstLab.first_name, ' ', empmstLab.last_name)  as \"collectedUserName\"," 
				 
				+" (case " + 
				" when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
				" when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name)  " + 
				" else 'Admin' end) as \"userName\"," + 
				" ord_dtls.package_id as \"packageId\" " + 
				" from 	public.t_order_master ord_mst " + 
				" 		inner join public.t_order_details ord_dtls on " + 
				" ord_mst.order_id = ord_dtls.order_id " + 
				" 		inner join service.t_unit_service_mapper service_mppr on " + 
				" service_mppr.service_id = ord_dtls.service_id " + 
				" and service_mppr.unit_id = ord_dtls.org_unit_id " + 
				" 		inner join service.t_unit_service_master_details service_dtls on " + 
				" service_dtls.service_master_id = service_mppr.service_id " + 
				" and service_dtls.unit_id = ord_dtls.org_unit_id " + 
				" 		inner join service.m_service_master service_mst on " + 
				" service_mst.service_master_id = ord_dtls.service_id " + 
				" 		left join doctor.m_speciality_master speciality_mst on " + 
				" speciality_mst.speciality_id = service_mst.speciality_id " + 
//				" 		left join t_unit_speciality_mapper unitSplMpr on " + 
//				" unitSplMpr.speciality_id = service_mst.speciality_id " + 
//				" and unitSplMpr.unit_id = service_mppr.unit_id " + 
				" 		left join lab.t_lab_sample_dtls sample_dtls on " + 
				" sample_dtls.order_details_id = ord_dtls.order_details_id " + 
				" and sample_dtls.sample_status_id <> 4 " + 
				" 		left join lab.m_test_status_master test_status on " + 
				" test_status.test_status_id = sample_dtls.sample_status_id " + 
				"        	left join lab.m_test_status_master test_status2 on " + 
				" test_status2.test_status_id = sample_dtls.micro_template_status_id 	" + 
				" 		left join lab.t_lab_test_result lab_res on " + 
				" lab_res.lab_sample_dtls_id = sample_dtls.lab_sample_dtls_id " + 
				" and lab_res.sample_status_id in( 8, 9,6,17) AND  lab_res.status = 'A' " + 
				" 		left outer join public.t_template_transaction templatetr12_ on " + 
				" ord_dtls.order_details_id = templatetr12_.order_details_id " + 
				" and( " + 
				" 	templatetr12_.status = 'A' " + 
				" ) " + 
//				" 		left join opd.m_appointment_master am on " + 
//				" am.order_details_id = ord_dtls.order_details_id " + 
//				" 		left join t_unit_application_billing_configuration uabc on " + 
//				" uabc.unit_id = ord_dtls.org_unit_id and uabc.status='A' " + 
//				" left join public.m_user_master um on ord_mst.created_by = um.user_id " + 
				" left join public.m_employee_master empmst on ord_mst.created_by = empmst.user_id " + 
				" left join doctor.m_doctor_master docmst on ord_mst.created_by = docmst.user_id " + 
				" left join lab.t_microbiology_template microTemp on microTemp.lab_sample_dtls_id=sample_dtls.lab_sample_dtls_id " + 
				 " left join public.m_employee_master empmst1 on sample_dtls.sample_collection_by = empmst1.user_id " + 
	    		 " left join doctor.m_doctor_master docmst1 on sample_dtls.sample_collection_by = docmst1.user_id " + 
				" 		where " + 
				" service_mst.status = 'A' " + 
				" and ord_dtls.old_ord_dtl_id is null " + 
				" and service_dtls.is_package = 'I' " + 
				" and ord_dtls.nursing_service_order_flag = 'N' " + 
				" and service_dtls.is_panel = 'N' " + 
				 " and speciality_mst.speciality_id  in( select speciality_id from t_unit_speciality_mapper where unit_id ="+unitId+" and is_rendering_speciality = 'Y' ) " 
//				" and ord_dtls.is_procedure_service = 'N' "
				);   
       
			
			sb.append("and  ord_mst.patient_id  =:patientId  order by ord_dtls.created_date desc )");
        
     sb.append(" union all"+
    		 " (select " + 
    		 "ord_mst.order_id as \"orderId\", " + 
    		 "service_mst.speciality_id as \"specialityId\"," + 
    		 "service_mst.sub_speciality_id as \"subDeptId\"," + 
    		 "service_mst.sub_speciality_id as \"subSpecialityId\"," + 
    		 "ord_dtls.is_procedure_service as \"isProcedureService\", " + 
    		 "ord_mst.admission_id as \"admissionId\", " + 
    		 "ord_mst.encounter_id as \"encounterId\", " + 
    		 "ord_mst.patient_id as \"patientId\", " + 
    		 "ord_dtls.order_details_id as \"orderDetailsId\", " + 
    		 "ord_dtls.service_id as \"serviceMasterId\", " + 
    		 "	ord_dtls.order_set_id as \"orderSetId\", " + 
    		 "	ord_dtls.priority_id as \"priorityId\", " + 
    		 "	ord_dtls.ord_remarks as \"ordRemarks\", " + 
    		 "  to_char(ord_dtls.order_sch_date,'dd/MM/yyyy') as \"orderSchDate\", " + 
    		 "  ord_dtls.order_sch_date as \"orderSchDateNew\", " + 
    		 "service_mst.service_standard_name as \"serviceStandardName\", " + 
    		 "service_mst.service_standard_code as \"serviceStandardCode\", " + 
    		 "speciality_mst.speciality_name as \"specialityName\", " + 
    		 "cast( " + 
    		 "	null as integer " + 
    		 ") as \"labSampleDtlsId\", " + 
    		 "service_dtls.is_panel as \"isPanel\", " + 
    		 "cast( " + 
    		 "	null as integer " + 
    		 ") as \"labTestResId\", " + 
    		 "panel.panel_id as \"panelId\", " + 
    		 "null as \"statusName\", " + 
    		 "0 as \"sampleStatusId\", " + 
    		 "to_char( " + 
    		 "	ord_dtls.created_date, " + 
    		 "	'dd/MM/yyyy HH24:MI' " + 
    		 ") as \"createdDate\", " + 
    		 " '' as \"sampleGenDatetime\", " + 
    		 "		ord_dtls.created_date as \"createdDateNew\", " + 
    		 "ord_dtls.service_rendered as \"serviceRendered\", " + 
    		 "ord_dtls.is_package_service as \"isPackageService\", " + 
    		 "templatetr12_.template_transaction_id as \"templateTransactionId\"," + 
//    		 " templatetr12_.template_object as \"templateObject\", " + 
    		 "ord_dtls.service_is_billed as \"serviceIsBilled\", " + 
    		 "ord_dtls.status as \"orderStatus\", " + 
//    		 "am.appointment_id as \"appointmentId\", " + 
//    		 "am.slot_id as \"slotId\", " + 
    		 " 0 as \"microTemplateStatusId\" , "
    		 +"'' as \"collectedUserName\"," 
    		+ "(case " + 
    		 "when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
    		 "when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " + 
    		 "else 'Admin' end) as \"userName\"," + 
    		 "ord_dtls.package_id as \"packageId\" " + 
    		 "	from " + 
    		 "public.t_order_master ord_mst " + 
    		 "	inner join public.t_order_details ord_dtls on " + 
    		 "ord_mst.order_id = ord_dtls.order_id " + 
    		 "	inner join service.t_unit_service_mapper service_mppr on " + 
    		 "service_mppr.service_id = ord_dtls.service_id " + 
    		 "and service_mppr.unit_id = ord_dtls.org_unit_id " + 
    		 "	inner join service.t_unit_service_master_details service_dtls on " + 
    		 "service_dtls.service_master_id = service_mppr.service_id " + 
    		 "and service_dtls.unit_id = ord_dtls.org_unit_id " + 
    		 "	inner join service.m_service_master service_mst on " + 
    		 "service_mst.service_master_id = ord_dtls.service_id " + 
    		 "	left join doctor.m_speciality_master speciality_mst on " + 
    		 "speciality_mst.speciality_id = service_mst.speciality_id " + 
//    		 "	left join t_unit_speciality_mapper unitSplMpr on " + 
//    		 "unitSplMpr.speciality_id = service_mst.speciality_id " + 
//    		 "and unitSplMpr.unit_id = service_mppr.unit_id " + 
    		 "	inner join lab.t_panel_master panel on " + 
    		 "panel.service_id = service_mst.service_master_id " + 
    		 "and panel.org_unit_id = service_mppr.unit_id " + 
    		 "	left outer join public.t_template_transaction templatetr12_ on " + 
    		 "ord_dtls.order_details_id = templatetr12_.order_details_id " + 
    		 "and( " + 
    		 "	templatetr12_.status = 'A' " + 
    		 ") " + 
//    		 "	left join opd.m_appointment_master am on " + 
//    		 "am.order_details_id = ord_dtls.order_details_id " + 
//    		 "	left join t_unit_application_billing_configuration uabc on " + 
//    		 "	uabc.unit_id = ord_dtls.org_unit_id and uabc.status='A'	 " + 
				/* "left join public.m_user_master um on ord_mst.created_by = um.user_id " + */
    		 "left join public.m_employee_master empmst on ord_mst.created_by = empmst.user_id " + 
    		 "left join doctor.m_doctor_master docmst on ord_mst.created_by = docmst.user_id " + 
    		 " where " + 
    		 "service_mst.status = 'A' " + 
    		 "and ord_dtls.old_ord_dtl_id is null " + 
    		 "and service_dtls.is_package = 'I' " + 
    		 "and service_dtls.is_panel = 'Y' " + 
    		 " and speciality_mst.speciality_id  in( select speciality_id from t_unit_speciality_mapper where unit_id ="+unitId+" and is_rendering_speciality = 'Y' ) " + 
    		 "	and ord_dtls.nursing_service_order_flag = 'N' " 
//    		 " and ord_dtls.is_procedure_service = 'N' " 
    		 );
       
     		sb.append("and  ord_mst.patient_id  =:patientId  order by ord_dtls.created_date desc )");
     		
        	Query createdQuery=sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		          createdQuery.setParameter("patientId",patientId);
        	List<InvestigationOrderMasterNewDto> investigationDTOsList = createdQuery
        	.setResultTransformer(Transformers.aliasToBean(InvestigationOrderMasterNewDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, investigationDTOsList, null);	
	}

	@Override
	public OrderMasterDto getPatientClassByVisitForNewCoversheet(OrderMasterDto orderMasterDto) {
		try {
	    	
    	    String query = null;
    	    if(orderMasterDto.getVisitTypeId().intValue()==OP)
    	    {
    	    	query = " select "
    	    			+ " 	enc.patient_class_id as \"patientClassId\", "
    	    			+ " 	trd.referral_id as \"referralId\" "
    	    			+ " from  "
    	    			+ " 	opd.t_encounter_master enc "
    	    			+ " left join t_referal_details trd on "
    	    			+ " 	trd.encounter_id = enc.encounter_id and  trd.status = 'A' "
        	    		+ "	where enc.encounter_id =:encounterId ";
    	    }
    	    else {
    	    	 query = " select "
    	    			+ " 	adm.patient_class_id as \"patientClassId\", "
    	    			+ " 	trd.referral_id as \"referralId\" "
    	    			+ " from  "
    	    			+ " 	adt.t_admission adm "
    	    			+ " left join t_referal_details trd on  "
    	    			+ " 	trd.admission_id = adm.admission_id and  trd.status = 'A' "
    	    	    	+ "	where	adm.admission_id =:admissionId ";
    	       }
    	    		
    	    		
    	  Query createQuery= sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString());
			if(orderMasterDto.getVisitTypeId().intValue()==OP)
			{
				createQuery.setParameter("encounterId",orderMasterDto.getEncounterId());
			}else{
				createQuery.setParameter("admissionId",orderMasterDto.getAdmissionId());
			}
			OrderMasterDto dto = (OrderMasterDto)createQuery.setResultTransformer(Transformers.aliasToBean(OrderMasterDto.class))
					.uniqueResult();
    	    
    	    if(dto!=null)
    	    {
    	    	if(dto.getPatientClassId()!=null)
    	    		orderMasterDto.setPatientClassId(dto.getPatientClassId());
    	    	if(dto.getReferralId()!=null)
    	    		orderMasterDto.setReferralId(dto.getReferralId());
    	    }
    	    return orderMasterDto;
    	    
			}catch(Exception e){
				
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response addUpdateSubscriptionConfig(UnitSubscriptionConfigDto config) throws ApplicationException {
		try{

			if(config.getSubscriptionConfigId()!=null && config.getSubscriptionConfigId()>0) {
				UnitSubscriptionConfig entity = (UnitSubscriptionConfig) sessionFactory.getCurrentSession().get(UnitSubscriptionConfig.class,config.getSubscriptionConfigId());
				entity.setUnitId(config.getUnitId());
				entity.setOrganizationId(config.getOrganizationId());
				entity.setSubscriptionAmount(config.getSubscriptionAmount());
				entity.setSubscriptionDiscount(config.getSubscriptionDiscount());
				entity.setSubscriptionNetAmount(config.getSubscriptionNetAmount());
				entity.setSubscriptionTax(config.getSubscriptionTax());
				entity.setStatus('A');
				entity.setSubscriptionType(config.getSubscriptionType());
				entity.setCreatedBy(sessionService.getUserId());
				entity.setUpdatedBy(sessionService.getUserId());
				entity.setCreatedDate(TimeZoneComponent.getDateByzone(config.getUnitId()));
				entity.setUpdatedDate(TimeZoneComponent.getDateByzone(config.getUnitId()));
				sessionFactory.getCurrentSession().update(entity);
				return new Response<>(SUCCESS, SUCCESS_CODE, "Subscription configuration updated successfully.", null, entity);
			}else{
				UnitSubscriptionConfig entity = new UnitSubscriptionConfig();
				entity.setUnitId(config.getUnitId());
				entity.setOrganizationId(config.getOrganizationId());
				entity.setSubscriptionAmount(config.getSubscriptionAmount());
				entity.setSubscriptionDiscount(config.getSubscriptionDiscount());
				entity.setSubscriptionNetAmount(config.getSubscriptionNetAmount());
				entity.setSubscriptionTax(config.getSubscriptionTax());
				entity.setStatus('A');
				entity.setSubscriptionType(config.getSubscriptionType());
				entity.setCreatedBy(sessionService.getUserId());
				entity.setUpdatedBy(sessionService.getUserId());
				entity.setCreatedDate(TimeZoneComponent.getDateByzone(config.getUnitId()));
				entity.setUpdatedDate(TimeZoneComponent.getDateByzone(config.getUnitId()));
				Integer id =  (Integer) sessionFactory.getCurrentSession().save(entity);
				entity.setSubscriptionConfigId(id);
				return new Response<>(SUCCESS, SUCCESS_CODE, "Subscription configuration added successfully.", null, entity);
			}
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){

			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getSubScriptionConfig(UnitMasterDto dto) throws ApplicationException {
		try {
			UnitSubscriptionConfigDto config = (UnitSubscriptionConfigDto) sessionFactory.getCurrentSession()
					.createSQLQuery("select "
							+ "	config.subscription_config_id as \"subscriptionConfigId\", "
							+ "	config.unit_id as \"unitId\", "
							+ "	config.organization_id as \"organizationId\", "
							+ "	config.subscription_amount as \"subscriptionAmount\", "
							+ "	config.subscription_discount as \"subscriptionDiscount\", "
							+ "	config.subscription_net_amount as \"subscriptionNetAmount\", "
							+ "	config.subscription_tax as \"subscriptionTax\", "
							+ "	config.subscription_type as \"subscriptionType\", "
							+ "	unit.unit_code as \"unitCode\", "
							+ "	unit.unit_name as \"unitName\", "
							+ "	unit.unit_address as \"unitAddress\", "
							+ "	unit.unit_email_id as \"unitEmailId\", "
							+ "	unit.unit_contact as \"unitContact\", "
							+ "	unit.post_code as \"postCode\" "
							+ "from "
							+ "	public.m_unit_subscription_config config "
							+ "    inner join public.m_unit_master unit on unit.unit_id = config.unit_id "
							+ "    where config.unit_id = :unitId "
							+ "    and config.status='A' "
							+ "    and unit.status ='A' limit 1")
					.setParameter("unitId", dto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(UnitSubscriptionConfigDto.class)).uniqueResult();
			if(config!=null){
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, config);
			}
			return new Response<>(ERROR, ERROR_CODE, "Subscription is not configured..Please contact administrator.",null , null);
		} catch (HibernateException he) {
			logger.error("Error while fetching subscription config", he);
		} catch (Exception e) {
			logger.error("Error while fetching subscription config", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response checkSubscriptionExist(UnitSubscriptionDetailsDto dto) throws ApplicationException {
		try{
			//change
			LocalDate previousDate = LocalDate.now().minusDays(1);
			SimpleDateFormat dtfDate = new SimpleDateFormat("yyyy-MM-dd");
			String sStartDate =  dtfDate.format(dto.getSubscriptionStartDate());
			String sEndDate =  dtfDate.format(dto.getSubscriptionEndDate());
			StringBuilder query  = new StringBuilder("SELECT "
					+ "    count(tusd.subscription_id) "
					+ "FROM "
					+ "    t_unit_subscription_details tusd "
					+ "    where tusd.unit_id = :unitId and tusd.subscription_status =:subScriptionStatus AND ( "
					+ "        ( pgp_sym_decrypt(cast(tusd.subscription_start_date as bytea),:key)>= :sStartDate AND  pgp_sym_decrypt(cast(tusd.subscription_start_date as bytea),:key) <=:sEndDate) "
					+ "     or  ( pgp_sym_decrypt(cast(tusd.subscription_end_date as bytea),:key) >= :sStartDate AND  pgp_sym_decrypt(cast(tusd.subscription_end_date as bytea),:key) <=:sEndDate) "
					+ "     or  ( pgp_sym_decrypt(cast(tusd.subscription_start_date as bytea),:key) <= :sStartDate AND  pgp_sym_decrypt(cast(tusd.subscription_end_date as bytea),:key) >= :sEndDate))");

			query.append(dto.getSubscriptionId()!=null && dto.getSubscriptionId()>0?
					" and tusd.subscription_id !=:subscriptionId ":"");
			Query createQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("sStartDate", sStartDate)
					.setParameter("sEndDate", sEndDate)
					.setParameter("subScriptionStatus", ICommonConstants.SUBSCRIPTION_ACTIVE)
					.setParameter("key", ENCRYPTIONKEY)
					.setParameter("unitId", dto.getUnitId());

			if(dto.getSubscriptionId()!=null && dto.getSubscriptionId()>0){
				createQuery.setParameter("subscriptionId",dto.getSubscriptionId());
			}
			BigInteger subcriptionExistCount = (BigInteger)	createQuery.uniqueResult();
			if(subcriptionExistCount!=null && subcriptionExistCount.intValue()>0){
				return new Response(ERROR, null, "Active subscription is already exists.", null, subcriptionExistCount);
			}
			return new Response(SUCCESS, null, null, null, null);
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}

	}

	@Override
	public Response addSubScrtionDetails(UnitSubscriptionDetailsDto subscriptionDetails) throws ApplicationException {
		try{

			SimpleDateFormat dtfDate = new SimpleDateFormat("yyyy-MM-dd");
			String sStartDate =  dtfDate.format(subscriptionDetails.getSubscriptionStartDate());
			String sEndDate =  dtfDate.format(subscriptionDetails.getSubscriptionEndDate());
			if(subscriptionDetails.getSubscriptionId()!=null && subscriptionDetails.getSubscriptionId()>0){
				UnitSubscriptionDetails entity = (UnitSubscriptionDetails) sessionFactory.getCurrentSession().get(UnitSubscriptionDetails.class,subscriptionDetails.getSubscriptionId());
				entity.setSubscriptionType(subscriptionDetails.getSubscriptionType());
				entity.setSubscriptionAmount(subscriptionDetails.getSubscriptionAmount().toString());
				entity.setSubscriptionDiscount(subscriptionDetails.getSubscriptionDiscount().toString());
				entity.setSubscriptionNetAmount(subscriptionDetails.getSubscriptionNetAmount().toString());
				entity.setSubscriptionTax(subscriptionDetails.getSubscriptionTax().toString());
				entity.setPaidAmount(subscriptionDetails.getPaidAmount().toString());
				entity.setAmountPaidStatus(subscriptionDetails.getAmountPaidStatus());
				entity.setSubscriptionStartDate(sStartDate);
				entity.setSubscriptionEndDate(sEndDate);
				entity.setPaymentOrderId(subscriptionDetails.getPaymentOrderId());
				entity.setSubscriptionStatus(subscriptionDetails.getSubscriptionStatus());
				entity.setPaymentType(subscriptionDetails.getPaymentType());
				entity.setFinalReminderCount(subscriptionDetails.getFinalReminderCount());
				entity.setReminderCount(subscriptionDetails.getReminderCount());
				entity.setCreatedBy(sessionService.getUserId());
				entity.setUpdatedBy(sessionService.getUserId());
				entity.setStatus('A');
				entity.setCreatedDate(TimeZoneComponent.getDateByzone(subscriptionDetails.getUnitId()));
				entity.setUpdatedDate(TimeZoneComponent.getDateByzone(subscriptionDetails.getUnitId()));
				entity.setOrganizationId(subscriptionDetails.getOrganizationId());
				entity.setUnitId(subscriptionDetails.getUnitId());
				entity.setRemark(subscriptionDetails.getRemark());
				sessionFactory.getCurrentSession().update(entity);
				return new Response<>(SUCCESS, SUCCESS_CODE, "Subscription updated successfully.", null, entity);
			}
			else{
				UnitSubscriptionDetails entity = new UnitSubscriptionDetails();
				entity.setSubscriptionConfigId(subscriptionDetails.getSubscriptionConfigId());
				entity.setSubscriptionType(subscriptionDetails.getSubscriptionType());
				entity.setSubscriptionAmount(subscriptionDetails.getSubscriptionAmount().toString());
				entity.setSubscriptionDiscount(subscriptionDetails.getSubscriptionDiscount().toString());
				entity.setSubscriptionNetAmount(subscriptionDetails.getSubscriptionNetAmount().toString());
				entity.setSubscriptionTax(subscriptionDetails.getSubscriptionTax().toString());
				entity.setPaidAmount(subscriptionDetails.getPaidAmount().toString());
				entity.setAmountPaidStatus(subscriptionDetails.getAmountPaidStatus());
				entity.setSubscriptionStartDate(sStartDate);
				entity.setSubscriptionEndDate(sEndDate);
				entity.setPaymentOrderId(subscriptionDetails.getPaymentOrderId());
				entity.setSubscriptionStatus(subscriptionDetails.getSubscriptionStatus());
				entity.setPaymentType(subscriptionDetails.getPaymentType());
				entity.setFinalReminderCount(subscriptionDetails.getFinalReminderCount());
				entity.setReminderCount(subscriptionDetails.getReminderCount());
				entity.setCreatedBy(sessionService.getUserId());
				entity.setUpdatedBy(sessionService.getUserId());
				entity.setStatus('A');
				entity.setCreatedDate(TimeZoneComponent.getDateByzone(subscriptionDetails.getUnitId()));
				entity.setUpdatedDate(TimeZoneComponent.getDateByzone(subscriptionDetails.getUnitId()));
				entity.setOrganizationId(subscriptionDetails.getOrganizationId());
				entity.setUnitId(subscriptionDetails.getUnitId());
				entity.setRemark(subscriptionDetails.getRemark());
				Integer id= (Integer) sessionFactory.getCurrentSession().save(entity);;
				return new Response<>(SUCCESS, SUCCESS_CODE, "Subscription added successfully.", null, entity);
			}

		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getSubcriptionDetails(UnitSubscriptionDetailsDto dto) throws ApplicationException {
		try {
			List<UnitSubscriptionDetailsDto> detailsDtoList = sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT "
							+ "    tusd.subscription_id AS \"subscriptionId\", "
							+ "    tusd.subscription_config_id AS \"subscriptionConfigId\", "
							+ "    tusd.subscription_type AS \"subscriptionType\", "
							+ "    cast(pgp_sym_decrypt(cast(tusd.subscription_amount as bytea),:key) as DOUBLE PRECISION) AS \"subscriptionAmount\", "
							+ "    cast(pgp_sym_decrypt(cast(tusd.subscription_discount as bytea),:key) as DOUBLE PRECISION) AS \"subscriptionDiscount\", "
							+ "    cast(pgp_sym_decrypt(cast(tusd.subscription_net_amount as bytea),:key) as DOUBLE PRECISION) AS \"subscriptionNetAmount\", "
							+ "    cast(pgp_sym_decrypt(cast(tusd.subscription_tax as bytea),:key) as DOUBLE PRECISION) AS \"subscriptionTax\", "
							+ "    cast(pgp_sym_decrypt(cast(tusd.paid_amount as bytea),:key) as DOUBLE PRECISION) AS \"paidAmount\", "
							+ "    tusd.amount_paid_status as \"amountPaidStatus\", "
							+ "    to_char(cast(pgp_sym_decrypt(cast(tusd.subscription_start_date as bytea),:key) as date),coalesce(upper(tuac.date_format),'DD-MM-YYYY')) AS \"subscriptionStartDateString\", "
							+ "    to_char(cast(pgp_sym_decrypt(cast(tusd.subscription_end_date as bytea),:key)as date),coalesce(upper(tuac.date_format),'DD-MM-YYYY')) AS \"subscriptionEndDateString\", "
							+ "    tusd.payment_order_id AS \"paymentOrderId\", "
							+ "    tusd.subscription_status AS \"subscriptionStatus\", "
							+ "    tusd.payment_type AS \"paymentType\", "
							+ "    tusd.remark AS \"remark\", "
							+ "    to_char(tusd.created_date,coalesce(upper(tuac.date_format),'DD-MM-YYYY')) AS \"createdDate\" "
							+ "FROM "
							+ "    t_unit_subscription_details tusd "
							+ "  inner join public.t_unit_application_billing_configuration tuac on tusd.unit_id = tuac.unit_id and tuac.status ='A' "
							+ "    where tusd.unit_id = :unitId")
					.setParameter("unitId", dto.getUnitId())
					.setParameter("key", ENCRYPTIONKEY)
					.setResultTransformer(Transformers.aliasToBean(UnitSubscriptionDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, detailsDtoList, null);
		} catch (HibernateException he)
		{
		logger.error("getSubcriptionDetails failed", he);
		} catch (Exception e)
		{
		logger.error("getSubcriptionDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}



	@Override
	public Response inactivateSubcriptions() throws ApplicationException {
		try {
			LocalDate currentDate = LocalDate.now();
			DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String date = dtfDate.format(currentDate);
			sessionFactory.getCurrentSession().createSQLQuery("update "
							+ "	public.t_unit_subscription_details "
							+ "set "
							+ "	subscription_status =:subScriptionStatus "
							+ "where subscription_id in (  select t.subscription_id from public.t_unit_subscription_details t where pgp_sym_decrypt(cast(t.subscription_end_date as bytea),:key) < :date and t.subscription_status =:subScriptionStatusActive ) ")
					.setParameter("subScriptionStatus", ICommonConstants.SUBSCRIPTION_EXPIRED)
					.setParameter("subScriptionStatusActive", ICommonConstants.SUBSCRIPTION_ACTIVE)
					.setParameter("date",date)
					.setParameter("key", ENCRYPTIONKEY)
					.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE,COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
		logger.error("inactivateSubcriptions failed", he);
		} catch (Exception e) {
		logger.error("inactivateSubcriptions failed", e);
		} return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response findSubscriptionsExpiringInDays() throws ApplicationException {
		try {
			LocalDate currentDate = LocalDate.now();
			DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String currDateString = dtfDate.format(currentDate);
			List<UnitSubscriptionDetailsDto> detailsDtoList = sessionFactory.getCurrentSession()
					.createSQLQuery("with res as( "
							+ "select "
							+ "	tusd.unit_id, "
							+ "	MAX(cast(pgp_sym_decrypt(cast(tusd.subscription_end_date as bytea),:key) as date)) as subscription_end_date "
							+ "from "
							+ "	t_unit_subscription_details tusd "
							+ "where "
							+ "	tusd.subscription_status =:subScriptionStatus "
							+ " and  pgp_sym_decrypt(cast(tusd.subscription_start_date as bytea),:key) >= :currDateString "
							+ "	and DATE_PART('day',age(cast(pgp_sym_decrypt(cast(tusd.subscription_end_date as bytea),:key) as date), "
							+ "	cast(:currDateString as date)))<15 "
							+ "group by "
							+ "	tusd.unit_id) "
							+ "    select "
							+ "	r.unit_id as \"unitId\", "
							+ "	unit.organization_id as \"organizationId\", "
							+ "	r.subscription_end_date as \"subscriptionEndDateString\", "
							+ "	unit.unit_code as \"unitCode\", "
							+ "	unit.unit_name as \"unitName\", "
							+ "	unit.unit_address as \"unitAddress\", "
							+ "	unit.unit_email_id as \"unitEmailId\", "
							+ "	unit.unit_contact as \"unitContact\", "
							+ "	unit.post_code as \"postCode\", "
							+ "	(SELECT tusd.subscription_id from t_unit_subscription_details tusd where cast(pgp_sym_decrypt(cast(tusd.subscription_end_date as bytea),:key) as date) =  cast(r.subscription_end_date as date) and unit.unit_id = tusd.unit_id ) AS \"subscriptionId\" , "
							+ "	 ( CASE "
							+ "    WHEN (select count(d.subscription_id) from t_unit_subscription_details d where d.unit_id = r.unit_id and  pgp_sym_decrypt(cast(d.subscription_start_date as bytea),:key) = to_char(cast(r.subscription_end_date  as date) + INTERVAL '1 day','YYYY-MM-DD'))>0   THEN true "
							+ "    ELSE false "
							+ " end) AS  \"futureSubscriptionTaken\" "
							+ "	from "
							+ "	res r "
							+ "inner join public.m_unit_master unit on "
							+ "	unit.unit_id = r.unit_id "
							+ "where "
							+ "	unit.status = 'A'")
					.setParameter("currDateString", currDateString)
					.setParameter("subScriptionStatus", ICommonConstants.SUBSCRIPTION_ACTIVE)
					.setParameter("key", ENCRYPTIONKEY)
					.setResultTransformer(Transformers.aliasToBean(UnitSubscriptionDetailsDto.class)).list();
			detailsDtoList = detailsDtoList.stream().filter(e-> !e.getFutureSubscriptionTaken()).collect(Collectors.toList());
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, detailsDtoList, null);
		} catch (HibernateException he) {
		logger.error("findSubscriptionsExpiringInDays failed", he);
		} catch (Exception e) {
		logger.error("findSubscriptionsExpiringInDays failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getAciveSubscriptionByUnit(UnitMasterDto dto) throws ApplicationException {
		try {
			LocalDate currentDate = TimeZoneComponent.getLocalDateByZone(dto.getUnitId());
			DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String curDate = dtfDate.format(currentDate);
			UnitSubscriptionDetailsDto detailsDto = (UnitSubscriptionDetailsDto) sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT "
							+ "    tusd.subscription_id AS \"subscriptionId\", "
							+ "    tusd.subscription_config_id AS \"subscriptionConfigId\", "
							+ "    tusd.subscription_type AS \"subscriptionType\", "
							+ "    cast(pgp_sym_decrypt(cast(tusd.subscription_amount as bytea),:key) as DOUBLE PRECISION) AS \"subscriptionAmount\", "
							+ "    cast(pgp_sym_decrypt(cast(tusd.subscription_discount as bytea),:key) as DOUBLE PRECISION) AS \"subscriptionDiscount\", "
							+ "    cast(pgp_sym_decrypt(cast(tusd.subscription_net_amount as bytea),:key) as DOUBLE PRECISION) AS \"subscriptionNetAmount\", "
							+ "    cast(pgp_sym_decrypt(cast(tusd.paid_amount as bytea),:key) as DOUBLE PRECISION) AS \"paidAmount\", "
							+ "    tusd.amount_paid_status as \"amountPaidStatus\", "
							+ "    pgp_sym_decrypt(cast(tusd.subscription_start_date as bytea),:key) AS \"subscriptionStartDateString\", "
							+ "    pgp_sym_decrypt(cast(tusd.subscription_end_date as bytea),:key) AS \"subscriptionEndDateString\", "
							+ "    tusd.payment_order_id AS \"paymentOrderId\", "
							+ "    tusd.subscription_status AS \"subscriptionStatus\", "
							+ "    tusd.payment_type AS \"paymentType\", "
							+ "    tusd.unit_id AS \"unitId\", "
							+ "    to_char(tusd.created_date,'YYYY-MM-dd HH:MI:SS') AS \"createdDate\", "
							+ "   ( CASE "
							+ "    WHEN (select count(d.subscription_id) from t_unit_subscription_details d where d.unit_id = tusd.unit_id and  pgp_sym_decrypt(cast(d.subscription_start_date as bytea),:key) = to_char(cast(pgp_sym_decrypt(cast(tusd.subscription_end_date as bytea),:key) as date) + INTERVAL '1 day','YYYY-MM-DD'))>0   THEN true "
							+ "    ELSE false "
							+ " end) AS  \"futureSubscriptionTaken\" "
							+ "     FROM "
							+ "    t_unit_subscription_details tusd "
							+ "    where tusd.unit_id = :unitId "
							+ "      and  pgp_sym_decrypt(cast(tusd.subscription_start_date as bytea),:key) <= :currentDate "
							+ "      and  pgp_sym_decrypt(cast(tusd.subscription_end_date as bytea),:key) >= :currentDate "
							+ "     order by tusd.subscription_end_date desc limit 1")
					.setParameter("unitId", dto.getUnitId())
					.setParameter("currentDate", curDate)
					.setParameter("key", ENCRYPTIONKEY)
					.setResultTransformer(Transformers.aliasToBean(UnitSubscriptionDetailsDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, detailsDto);
		} catch (HibernateException he) {
		logger.error("getAciveSubscriptionByUnit failed", he);
		} catch (Exception e) {
		logger.error("getAciveSubscriptionByUnit failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getSubscriptionDetailsByROrderId(String razorPayOrderId) throws ApplicationException {
		try {
			LocalDate currentDate = LocalDate.now();
			DateTimeFormatter dtfDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String curDate = dtfDate.format(currentDate);
			UnitSubscriptionDetailsDto detailsDto = (UnitSubscriptionDetailsDto) sessionFactory.getCurrentSession()
					.createSQLQuery("select "
							+ "	config.subscription_config_id as \"subscriptionConfigId\", "
							+ "	config.unit_id as \"unitId\", "
							+ "	config.organization_id as \"organizationId\", "
							+ "	config.subscription_amount as \"subscriptionAmount\", "
							+ "	config.subscription_discount as \"subscriptionDiscount\", "
							+ "	config.subscription_net_amount as \"subscriptionNetAmount\", "
							+ "	config.subscription_tax as \"subscriptionTax\", "
							+ "	config.subscription_type as \"subscriptionType\", "
							+ "	(select to_char(max(cast(pgp_sym_decrypt(cast(s.subscription_end_date as bytea),:key) as date)),'YYYY-MM-dd') "
							+ "	from t_unit_subscription_details s where s.unit_id = logdt.unit_id and upper(s.subscription_status) = 'ACTIVE' "
							+ "	 and pgp_sym_decrypt(cast(s.subscription_end_date as bytea),:key) >=:currentDate)   as \"lastSubscriptionEndDate\", "
							+ "	3 as \"reminderCount\", "
							+ "	3 as \"finalReminderCount\", "
							+ "	'ACTIVE' as \"subscriptionStatus\", "
							+ "	'RAZORPAY' as \"paymentType\" "
							+ "from "
							+ "    m_razorpay_order_log_details logdt "
							+ "	inner join public.m_unit_subscription_config config  on (logdt.unit_id  = config.unit_id "
							+ "	and logdt.organization_id = config.organization_id ) "
							+ "    where  config.status ='A' and logdt.order_id = :razorPayOrderId")
					.setParameter("razorPayOrderId", razorPayOrderId)
					.setParameter("key", ENCRYPTIONKEY)
					.setParameter("currentDate", curDate)
					.setResultTransformer(Transformers.aliasToBean(UnitSubscriptionDetailsDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, detailsDto);
		} catch (HibernateException he) {
		logger.error("getSubscriptionDetailsByROrderId failed", he);
		} catch (Exception e) {
		logger.error("getSubscriptionDetailsByROrderId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getSubscriptionById(UnitSubscriptionDetailsDto dto) throws ApplicationException {
		try {
			UnitSubscriptionDetails entity = (UnitSubscriptionDetails) sessionFactory.getCurrentSession().get(UnitSubscriptionDetails.class,dto.getSubscriptionId());
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, entity);
		} catch (HibernateException he) {
		logger.error("getSubscriptionById failed", he);
		} catch (Exception e) {
		logger.error("getSubscriptionById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

}

package com.param.global.unit.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.model.ButtonMaster;
import com.param.global.model.ButtonMasterConfig;
import com.param.global.model.LetterAttachmentMapper;
import com.param.global.model.UserRoleMaster;
import com.param.global.model.VaccinationRoleMapper;
import com.param.global.unit.dto.ButtonMasterDto;
import com.param.global.unit.dto.LetterAttachmentMapperDto;
import com.param.global.unit.dto.UserRoleMasterDto;
import com.param.global.unit.dto.VaccinationRoleMapperDto;
import com.param.global.unit.service.IUserRoleMasterService;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UserRoleMasterDaoImpl extends GenericDao<UserRoleMaster, Integer>
		implements IUserRoleMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(UserRoleMasterDaoImpl.class);


	public UserRoleMasterDaoImpl() {
		super(UserRoleMaster.class);

	}

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	IUserRoleMasterService iUserRoleMasterService;
	
	@Override
	public Response saveUserRoleMaster(UserRoleMasterDto userRoleMasterDto) throws ApplicationException {
		try {

			UserRoleMaster userRoleMaster = new UserRoleMaster();
			userRoleMaster.setRoleName(userRoleMasterDto.getRoleName());
			userRoleMaster.setDashboardId(userRoleMasterDto.getDashboardId());
			userRoleMaster.setOrganisationId(userRoleMasterDto.getOrganisationId());
			userRoleMaster.setIsCounterApplicable(userRoleMasterDto.getIsCounterApplicable());
			userRoleMaster.setUnitId(userRoleMasterDto.getUnitId());
			userRoleMaster.setCreatedBy(userRoleMasterDto.getCreatedBy());
			userRoleMaster.setCreatedDate(TimeZoneComponent.getDateByzone(userRoleMasterDto.getUnitId()));
			userRoleMaster.setUpdatedBy(userRoleMasterDto.getUpdatedBy());
			userRoleMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(userRoleMasterDto.getUnitId()));
			userRoleMaster.setStatus(userRoleMasterDto.getStatus());
			userRoleMaster.setProphylacticVaccinationApplication(userRoleMasterDto.getProphylacticVaccinationApplication() != null ? userRoleMasterDto.getProphylacticVaccinationApplication() : 'N');
			userRoleMaster.setRoleTypeId(userRoleMasterDto.getRoleTypeId() != null ? userRoleMasterDto.getRoleTypeId() : 3);
//			userRoleMaster.setProphylacticVaccinationApplication(userRoleMasterDto.getProphylacticVaccinationApplication());

			if(userRoleMasterDto.getVaccination() != null) {
				List<VaccinationRoleMapper> vaccinationRoleMappers = new ArrayList<>();
				for (Integer vaccinationId : userRoleMasterDto.getVaccination()) {
					VaccinationRoleMapper vaccinationRoleMapper = new VaccinationRoleMapper();

					vaccinationRoleMapper.setUserRoleMaster(userRoleMaster);
					vaccinationRoleMapper.setVaccinationId(vaccinationId);
					vaccinationRoleMapper.setOrganizationId(userRoleMasterDto.getOrganisationId());
					vaccinationRoleMapper.setUnitId(userRoleMasterDto.getUnitId());
					vaccinationRoleMapper.setStatus(userRoleMasterDto.getStatus());
					vaccinationRoleMapper.setCreatedBy(userRoleMasterDto.getCreatedBy());
					vaccinationRoleMapper.setCreatedDate(TimeZoneComponent.getDateByzone(userRoleMasterDto.getUnitId()));
					vaccinationRoleMapper.setUpdatedBy(userRoleMasterDto.getUpdatedBy());
					vaccinationRoleMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(userRoleMasterDto.getUnitId()));

					vaccinationRoleMappers.add(vaccinationRoleMapper);
				}

				userRoleMaster.setVaccinationRoleMapper(vaccinationRoleMappers);
			}
			userRoleMaster = save(userRoleMaster);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, userRoleMaster.getRoleId());
		} catch (HibernateException he) {
		logger.error("saveUserRoleMaster failed", he);
		} catch (Exception e) {
		logger.error("saveUserRoleMaster failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateUserRoleMaster(UserRoleMasterDto userRoleMasterDto) throws ApplicationException {
		try {
			UserRoleMaster userRoleMaster = findById(userRoleMasterDto.getRoleId());
			userRoleMaster.setIsCounterApplicable(userRoleMasterDto.getIsCounterApplicable());
			userRoleMaster.setDashboardId(userRoleMasterDto.getDashboardId());
			userRoleMaster.setRoleName(userRoleMasterDto.getRoleName());
			userRoleMaster.setOrganisationId(userRoleMasterDto.getOrganisationId());
			userRoleMaster.setUnitId(userRoleMasterDto.getUnitId());
			userRoleMaster.setUpdatedBy(userRoleMasterDto.getUpdatedBy());
			userRoleMaster.setRoleTypeId(userRoleMasterDto.getRoleTypeId());
			userRoleMaster
					.setProphylacticVaccinationApplication(userRoleMasterDto.getProphylacticVaccinationApplication());

			/*
			 * if(userRoleMasterDto.getDoctorTypeId()!=null)
			 * userRoleMaster.setDoctorTypeId(userRoleMasterDto.getDoctorTypeId(
			 * ));
			 */
			userRoleMaster
					.setProphylacticVaccinationApplication(userRoleMasterDto.getProphylacticVaccinationApplication());

			List<VaccinationRoleMapper> vaccinationRoleMappers = new ArrayList<>();
			for (Integer vaccinationId : userRoleMasterDto.getVaccination()) {
				VaccinationRoleMapper vaccinationRoleMapper = new VaccinationRoleMapper();

				vaccinationRoleMapper.setUserRoleMaster(userRoleMaster);
				vaccinationRoleMapper.setVaccinationId(vaccinationId);
				vaccinationRoleMapper.setOrganizationId(userRoleMasterDto.getOrganisationId());
				vaccinationRoleMapper.setUnitId(userRoleMasterDto.getUnitId());
				vaccinationRoleMapper.setStatus(userRoleMasterDto.getStatus());
				vaccinationRoleMapper.setCreatedBy(userRoleMasterDto.getCreatedBy());
				vaccinationRoleMapper.setCreatedDate(TimeZoneComponent.getDateByzone(userRoleMasterDto.getUnitId()));
				vaccinationRoleMapper.setUpdatedBy(userRoleMasterDto.getUpdatedBy());
				vaccinationRoleMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(userRoleMasterDto.getUnitId()));

				vaccinationRoleMappers.add(vaccinationRoleMapper);
			}

			userRoleMaster.setVaccinationRoleMapper(vaccinationRoleMappers);

			userRoleMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(userRoleMasterDto.getUpdatedDate(), "dd-MM-yyyy HH:mm:ss"));

			userRoleMaster = save(userRoleMaster);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, userRoleMaster.getRoleId());
		//} catch (HibernateException he) {
		} catch (Exception e) {
		logger.error("updateUserRoleMaster failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateUserRoleMasterStatus(UserRoleMasterDto userRoleMasterDto) throws ApplicationException {
		try {
			UserRoleMaster userRoleMaster = findById(userRoleMasterDto.getRoleId());
			userRoleMaster.setUpdatedBy(userRoleMasterDto.getUpdatedBy());
			userRoleMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(userRoleMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			userRoleMaster.setStatus(userRoleMasterDto.getStatus());

			update(userRoleMaster);

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateUserRoleMasterStatus failed", he);
		} catch (Exception e) {
		logger.error("updateUserRoleMasterStatus failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserRoleMasterList(UserRoleMasterDto userRoleMasterDto) throws ApplicationException {
		try {

			List<UserRoleMasterDto> list2 = new ArrayList<>();
			List<UserRoleMasterDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_USER_ROLE_MASTER_LIST")
					.setParameter("unitId", userRoleMasterDto.getUnitId())
					.setParameter("organisationId", userRoleMasterDto.getOrganisationId())
					.setFirstResult(userRoleMasterDto.getOffset() != null ? userRoleMasterDto.getOffset() : 0)
					.setMaxResults(userRoleMasterDto.getNoOfRecordsPerPage() != null
							? userRoleMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(UserRoleMasterDto.class)).list();
			for(UserRoleMasterDto userRoleMaster : list) {
				List<VaccinationRoleMapperDto> vaccinations = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_VACCINATION_MAPPER_LIST_BY_ROLE_ID")
						.setParameter("roleId", userRoleMaster.getRoleId())
						.setParameter("unitId", userRoleMaster.getUnitId())
						.setParameter("organizationId", userRoleMaster.getOrganisationId())
						.setResultTransformer(Transformers.aliasToBean(VaccinationRoleMapperDto.class))
						.list();
				userRoleMaster.setVaccinations(vaccinations);
				list2.add(userRoleMaster);
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, list2, null);
		} catch (HibernateException he) {
		logger.error("getUserRoleMasterList failed", he);
		} catch (Exception e) {
		logger.error("getUserRoleMasterList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserRoleMasterById(Integer roleId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try {
			List<UserRoleMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_USER_ROLE_MASTER_BY_ID").setParameter("roleId", roleId)
					.setParameter("unitId", unitId).setParameter("organisationId", organisationId)
					.setResultTransformer(Transformers.aliasToBean(UserRoleMasterDto.class)).list();
			for(UserRoleMasterDto userRoleMaster : list) {
				List<VaccinationRoleMapperDto> vaccinations = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_VACCINATION_MAPPER_LIST_BY_ROLE_ID")
						.setParameter("roleId", roleId)
						.setParameter("unitId", unitId)
						.setParameter("organizationId", organisationId)
						.setResultTransformer(Transformers.aliasToBean(VaccinationRoleMapperDto.class))
						.list();
				userRoleMaster.setVaccinations(vaccinations);
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getUserRoleMasterById failed", he);
		} catch (Exception e) {
		logger.error("getUserRoleMasterById failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveUserRoleMasterList(UserRoleMasterDto userRoleMasterDto) throws ApplicationException {
		try {
			List<UserRoleMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_USER_ROLE_MASTER_LIST")
					.setParameter("unitId", userRoleMasterDto.getUnitId())
					.setParameter("organisationId", userRoleMasterDto.getOrganisationId())
					.setResultTransformer(Transformers.aliasToBean(UserRoleMasterDto.class)).list();
			for(UserRoleMasterDto userRoleMaster : list) {
				List<VaccinationRoleMapperDto> vaccinations = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_VACCINATION_MAPPER_LIST_BY_ROLE_ID")
						.setParameter("roleId", userRoleMaster.getRoleId())
						.setParameter("unitId", userRoleMaster.getUnitId())
						.setParameter("organizationId", userRoleMaster.getOrganisationId())
						.setResultTransformer(Transformers.aliasToBean(VaccinationRoleMapperDto.class))
						.list();
				userRoleMaster.setVaccinations(vaccinations);
			}

			String query = "select admin_role_id from t_unit_application_billing_configuration where unit_id="
					+ userRoleMasterDto.getUnitId() + "";
			Integer roleId = (Integer) sessionFactory.getCurrentSession().createSQLQuery(query).uniqueResult();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, roleId);
		} catch (HibernateException he) {
		logger.error("getActiveUserRoleMasterList failed", he);
		} catch (Exception e) {
		logger.error("getActiveUserRoleMasterList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserRoleMasterCount(UserRoleMasterDto userRoleMasterDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(UserRoleMaster.class);
			crit.add(Restrictions.eq("organisationId", userRoleMasterDto.getOrganisationId()));
			crit.add(Restrictions.eq("unitId", userRoleMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();

			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
		} catch (HibernateException he) {
		logger.error("getUserRoleMasterCount failed", he);
		} catch (Exception e) {
		logger.error("getUserRoleMasterCount failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserRoleMasterByName(UserRoleMasterDto userRoleMasterDto) throws ApplicationException {
		try {
			List<UserRoleMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_USER_ROLE_MASTER_BY_NAME")
					.setParameter("roleName", userRoleMasterDto.getRoleName().toLowerCase())
					.setParameter("unitId", userRoleMasterDto.getUnitId())
					.setParameter("orgId", userRoleMasterDto.getOrganisationId())
					.setResultTransformer(Transformers.aliasToBean(UserRoleMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getUserRoleMasterByName failed", he);
		} catch (Exception e) {
		logger.error("getUserRoleMasterByName failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserRoleMasterByNameNotId(UserRoleMasterDto userRoleMasterDto) throws ApplicationException {
		try {
			List<UserRoleMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_USER_ROLE_MASTER_BY_NAME_NOT_ID")
					.setParameter("roleId", userRoleMasterDto.getRoleId())
					.setParameter("unitId", userRoleMasterDto.getUnitId())
					.setParameter("orgId", userRoleMasterDto.getOrganisationId())
					.setParameter("roleName", userRoleMasterDto.getRoleName().toLowerCase())
					.setParameter("unitId", userRoleMasterDto.getUnitId())
					.setParameter("orgId", userRoleMasterDto.getOrganisationId())
					.setResultTransformer(Transformers.aliasToBean(UserRoleMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getUserRoleMasterByNameNotId failed", he);
		} catch (Exception e) {
		logger.error("getUserRoleMasterByNameNotId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response searchRoleByName(UserRoleMasterDto userRoleMasterDto) throws ApplicationException {
		try {
			List<UserRoleMasterDto> list = sessionFactory.getCurrentSession().getNamedQuery("SEARCH_USER_ROLE_BY_NAME")
					.setParameter("roleName", "%" + userRoleMasterDto.getRoleName().toLowerCase() + "%")
					.setParameter("unitId", userRoleMasterDto.getUnitId())
					.setParameter("orgId", userRoleMasterDto.getOrganisationId())
					.setResultTransformer(Transformers.aliasToBean(UserRoleMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("searchRoleByName failed", he);
		} catch (Exception e) {
		logger.error("searchRoleByName failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAiButtonConfigMaster(ButtonMasterDto buttonMasterDto) {
		    String queryString=" select combined_ai_json from public.m_unit_master where unit_id=:unitId ";
		    String res = (String) sessionFactory.getCurrentSession().createSQLQuery(queryString).setParameter("unitId", buttonMasterDto.getUnitId()).uniqueResult();
			return new Response(SUCCESS, null, null, null,res);
	}

	@Override
	public Response saveButtonMaster(ButtonMasterDto buttonMasterDto) {
		try{
			Integer tabId = null;
			ButtonMaster buttonMaster=new ButtonMaster();
			buttonMaster.setLabel(buttonMasterDto.getLabel());
			buttonMaster.setUserPrompt(buttonMasterDto.getUserPrompt());
			buttonMaster.setSystemPrompt(buttonMasterDto.getSystemPrompt());
			buttonMaster.setCombinedAIJson(buttonMasterDto.getJsonObject());
			buttonMaster.setOrganizationId(buttonMasterDto.getHospitalId());
			buttonMaster.setUnitId(buttonMasterDto.getUnitId());
			buttonMaster.setStatus('A');
			String queryString=" select max(tab_id) from  public.button_master ";
			tabId = (Integer) sessionFactory.getCurrentSession().createSQLQuery(queryString).uniqueResult();
			buttonMaster.setTabId((tabId!=null ? tabId :0)+1);
			sessionFactory.getCurrentSession().save(buttonMaster);
			return new Response(SUCCESS, null, null, null,null);
		}catch(HibernateException he){
		logger.error("saveButtonMaster failed", he);
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	public Response getButtonMaster(ButtonMasterDto buttonMasterDto) {
		try{
			List<ButtonMasterDto> buttonMasterDtoRes = null;
			buttonMasterDtoRes = sessionFactory.getCurrentSession()
					.createSQLQuery(" SELECT id as \"buttonId\", system_prompt as \"systemPrompt\",user_prompt as \"userPrompt\",label as \"label\",tab_id as \"tabId\",case when status='A' then true else false end as \"status\", combined_ai_json as \"jsonObject\"  FROM    public.button_master where unit_id=:unitId and organization_id=:hospitalId ")
					.setParameter("hospitalId", buttonMasterDto.getHospitalId()).setParameter("unitId", buttonMasterDto.getUnitId()).setResultTransformer(Transformers.aliasToBean(ButtonMasterDto.class)).list();
			return new Response(SUCCESS, null, null, buttonMasterDtoRes,null);
			
		}catch(HibernateException he){
		logger.error("getButtonMaster failed", he);
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	public Response updateButtonMaster(ButtonMasterDto buttonMasterDto) {
		try{
			Integer buttonMasterDtoRes = null;
			String queryString=" update public.button_master set label=:label,system_prompt=:systemPrompt,user_prompt=:userPrompt, "
					+ " tab_id=:tabId,combined_ai_json=:jsonObject where id=:id ";
			buttonMasterDtoRes = sessionFactory.getCurrentSession().createSQLQuery(queryString)
			.setParameter("label", buttonMasterDto.getLabel())
			.setParameter("systemPrompt",buttonMasterDto.getSystemPrompt())
			.setParameter("userPrompt", buttonMasterDto.getUserPrompt())
			.setParameter("tabId", buttonMasterDto.getTabId())
			.setParameter("id", buttonMasterDto.getButtonId())
			.setParameter("jsonObject", buttonMasterDto.getJsonObject()).executeUpdate();
			return new Response(SUCCESS, null, null,null,null);
			
		}catch(HibernateException he){
		logger.error("updateButtonMaster failed", he);
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	public Response updateButtonMasterStatus(ButtonMasterDto buttonMasterDto) {
		try{
			Integer buttonMasterDtoRes = null;
			Character status=null;
			if(buttonMasterDto.getIsStatus()==true)
			{
				status='A';
			}
			else
			{
				status='I';
			}
			String queryString=" update public.button_master set status=:status where id=:id ";
			buttonMasterDtoRes = sessionFactory.getCurrentSession().createSQLQuery(queryString)
			.setParameter("status", status)
			.setParameter("id", buttonMasterDto.getButtonId()).executeUpdate();
			return new Response(SUCCESS, null, null,null,null);
			
		}catch(HibernateException he){
		logger.error("updateButtonMasterStatus failed", he);
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	public Response getDoctorMasterByUnitId(DoctorMasterDto doctorMasterDto) {
		try
		{
			String sql="select  doctor_id as \"doctorId\", "
					+ " concat(coalesce(first_name  ,''),' ',coalesce(middle_name  ,'') ,' ',coalesce(last_name  ,'') ,' ')  as \"doctorName\" "
					+ " from doctor.m_doctor_master where organization_id=:hospitalId and unit_id=:unitId and status='A' "; 
			List<DoctorMasterDto> listDoctorMasterDto = new ArrayList<DoctorMasterDto>();
			listDoctorMasterDto = sessionFactory.getCurrentSession().createSQLQuery(sql).setParameter("hospitalId", doctorMasterDto.getOrganizationId()).setParameter("unitId", doctorMasterDto.getUnitId()).setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).list();
			return new Response(SUCCESS, null, null,listDoctorMasterDto,null);
		
		}catch(HibernateException he){
		logger.error("getDoctorMasterByUnitId failed", he);
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}catch (Exception e) {
		logger.error("getDoctorMasterByUnitId failed", e);
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}finally{

		}
	}

	@Override
	public Response getDoctorMasterByUnitIdForSpeciality(DoctorMasterDto doctorMasterDto) {
		List<DoctorMasterDto> listDoctorMasterDto = new ArrayList<DoctorMasterDto>();
        String queryString=" select distinct msm.speciality_id as \"doctorId\",msm.speciality_name as \"firstName\" "
        		+ " from doctor.m_speciality_master msm inner join t_unit_speciality_mapper usm on usm.speciality_id=msm.speciality_id "
        		+ " where msm.organization_id=:organizationId  and usm.unit_id=:unitId   and msm.status='A' ";
	    listDoctorMasterDto = sessionFactory.getCurrentSession().createSQLQuery(queryString)
				.setParameter("organizationId",doctorMasterDto.getOrganizationId())
				.setParameter("unitId",doctorMasterDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(DoctorMasterDto.class)).list();
	    return new Response(SUCCESS, null, null,listDoctorMasterDto,null);	
	}

	@Override
	public Response getAiButtonConfigMasterForDocAndSpeciality(ButtonMasterDto buttonMasterDto) {
		String queryString=" select combined_ai_json from button_master where organization_id=:orgId and id=:buttonId ";
	    String res = (String) sessionFactory.getCurrentSession().createSQLQuery(queryString).setParameter("buttonId", buttonMasterDto.getButtonId()).setParameter("orgId", buttonMasterDto.getHospitalId()).uniqueResult();
			return new Response(SUCCESS, null, null, null,res);
	}

	@Override
	public Response saveConfig(ButtonMasterDto buttonMasterDto) {
		try{
			ButtonMasterConfig buttonMasterConfig=new ButtonMasterConfig();
			buttonMasterConfig.setOrganizationId(buttonMasterDto.getHospitalId());
			buttonMasterConfig.setDoctorId(buttonMasterDto.getDoctorId());
			buttonMasterConfig.setSpecialityId(buttonMasterDto.getSpecialityId());
			buttonMasterConfig.setJson_configuration(buttonMasterDto.getJsonObject());
			buttonMasterConfig.setDoctorName(buttonMasterDto.getDoctorName());
			buttonMasterConfig.setSpecialityName(buttonMasterDto.getSpecialityName());
			buttonMasterConfig.setButtonName(buttonMasterDto.getLabel());
			buttonMasterConfig.setStatus('A');
			buttonMasterConfig.setButtonId(buttonMasterDto.getButtonId());
			buttonMasterConfig.setUnitId(buttonMasterDto.getUnitId());
			String message=null;
			if(buttonMasterDto.getConfigId()!=null)
			{
				message="Updated successfully";
				buttonMasterConfig.setConfigId(buttonMasterDto.getConfigId());
				sessionFactory.getCurrentSession().update(buttonMasterConfig);
			}
			else
			{
				Integer doctorCount=(Integer) sessionFactory.getCurrentSession().createSQLQuery(" select cast(count(*) as int) from public.button_master_config where doctor_id="+buttonMasterDto.getDoctorId()+" and lower(button_name)='"+buttonMasterDto.getLabel().toLowerCase()+"' ").uniqueResult();
				if(doctorCount>0)
				{
					return new Response(SUCCESS,"1","Already exists",null,null);
				}
				Integer specialityCount=(Integer) sessionFactory.getCurrentSession().createSQLQuery(" select cast(count(*) as int) from public.button_master_config where speciality_id="+buttonMasterDto.getSpecialityId()+" and lower(button_name)='"+buttonMasterDto.getLabel().toLowerCase()+"' ").uniqueResult();
				if(specialityCount>0)
				{
					return new Response(SUCCESS,"1","Already exists",null,null);
				}
				message="Saved successfully";
				sessionFactory.getCurrentSession().saveOrUpdate(buttonMasterConfig);
			}
			return new Response(SUCCESS,null,message,null,null);
			
		  }catch(HibernateException he){
			he.printStackTrace();
			throw he;
			
		}
	}

	@Override
	public Response getSavedConfig(ButtonMasterDto buttonMasterDto) {
		try {
			String queryString=" select config_id as \"configId\",doctor_id as \"doctorId\",speciality_id as \"specialityId\",json_configuration as \"jsonObject\", "
					+ " doctor_name as \"doctorName\" ,speciality_name as \"specialityName\",button_name as \"label\", "
					+ " button_id as \"buttonId\",case when status='A' then true else false end as \"status\" from public.button_master_config where organization_id=:orgId and unit_id=:unitId ";

		    List<ButtonMasterDto> result = (List<ButtonMasterDto>) sessionFactory.getCurrentSession().createSQLQuery(queryString)
		    		.setParameter("orgId", buttonMasterDto.getHospitalId()).setParameter("unitId", buttonMasterDto.getUnitId()).setResultTransformer(Transformers.aliasToBean(ButtonMasterDto.class)).list();
				return new Response(SUCCESS, null, null, result,null);
			}catch (Exception e) {
			logger.error("getSavedConfig failed", e);
			}
			return new Response(ERROR, null, null, null,null);
	}

	@Override
	public Response getActiveButtonMaster(ButtonMasterDto buttonMasterDto) {
		try{
			List<ButtonMasterDto> buttonMasterDtoRes = null;
			buttonMasterDtoRes = sessionFactory.getCurrentSession()
					.createSQLQuery(" SELECT id as \"buttonId\", system_prompt as \"systemPrompt\",user_prompt as \"userPrompt\",label as \"label\",tab_id as \"tabId\",case when status='A' then true else false end as \"status\",combined_ai_json as \"jsonObject\"  FROM    public.button_master where status='A' and organization_id=:organizationId  and unit_id=:unitId  ")
					.setParameter("organizationId",buttonMasterDto.getHospitalId())
					.setParameter("unitId",buttonMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ButtonMasterDto.class)).list();
			Response<ButtonMasterDto, Integer> ab=iUserRoleMasterService.getActiveSavedConfig(buttonMasterDto);

			 List<ButtonMasterDto> buttonMasterDtoResConfig=ab.getListObject();
		String disclaimer=(String) sessionFactory.getCurrentSession()
				.createSQLQuery(" select disclaimer from public.m_unit_master where organization_id=:organizationId  and unit_id=:unitId ")
				.setParameter("organizationId",buttonMasterDto.getHospitalId())
				.setParameter("unitId",buttonMasterDto.getUnitId())
				.setMaxResults(1).uniqueResult();
			return new Response(SUCCESS, null, null, buttonMasterDtoRes,disclaimer,null,buttonMasterDtoResConfig);
			
		}catch(HibernateException he){
		logger.error("getActiveButtonMaster failed", he);
		return new Response<DoctorMasterDto, Integer>(ERROR, null, null, null,null);
		}
	}

	@Override
	public Response saveLetterAttachment(LetterAttachmentMapperDto letterattachmentMapperDto) {
		try{
			
			LetterAttachmentMapper  mapper=new  LetterAttachmentMapper();
			mapper.setAppointmentId(letterattachmentMapperDto.getAppointmentId());
			mapper.setLetterId(letterattachmentMapperDto.getLetterId());
			mapper.setPatientId(letterattachmentMapperDto.getPatientId());
			mapper.setUnitId(letterattachmentMapperDto.getUnitId());
			mapper.setOrganizationId(letterattachmentMapperDto.getOrganizationId());
			mapper.setStatus(ICommonConstants.ACTIVE);
			mapper.setAiLetter(letterattachmentMapperDto.getAiLetter());
			mapper.setTabId(letterattachmentMapperDto.getTabId());
			Serializable save = sessionFactory.getCurrentSession().save(mapper);
			return new Response(SUCCESS, null, null, null,save);
			
		}catch(HibernateException he){
		logger.error("saveLetterAttachment failed", he);
		return new Response(ERROR, null, null, null,null);
		}catch(Exception e){
		logger.error("saveLetterAttachment failed", e);
		return new Response(ERROR, null, null, null,null);
		}

	}

	@Override
	public Response updateButtonMasterConfigStatus(ButtonMasterDto buttonMasterDto) {
        try{
            Integer buttonMasterDtoRes = null;
            Character status=null;
            if(buttonMasterDto.getIsStatus()==true)
            {
                status='A';
            }
            else
            {
                status='I';
            }
            String queryString=" update public.button_master_config set status=:status where config_id=:id ";
            buttonMasterDtoRes = sessionFactory.getCurrentSession().createSQLQuery(queryString)
            .setParameter("status", status)
            .setParameter("id", buttonMasterDto.getButtonId()).executeUpdate();
            return new Response(SUCCESS, null, null, null,null);
            
        }catch(HibernateException he){
        logger.error("updateButtonMasterConfigStatus failed", he);
        return new Response(ERROR, null, null, null,null);
        }
        }

	@Override
	public Response getActiveSavedConfig(ButtonMasterDto buttonMasterDto) {
		try {
	        String queryString=" select config_id as \"configId\",doctor_id as \"doctorId\",speciality_id as \"specialityId\",json_configuration as \"jsonObject\", "
	                + " doctor_name as \"doctorName\" ,speciality_name as \"specialityName\",button_name as \"label\", "
	                + " button_id as \"buttonId\",case when status='A' then true else false end as \"status\" from public.button_master_config where organization_id=:orgId and unit_id=:unitId and status='A' ";
	        List<ButtonMasterDto> result = (List<ButtonMasterDto>) sessionFactory.getCurrentSession().createSQLQuery(queryString)
	                .setParameter("orgId", buttonMasterDto.getHospitalId())
	                .setParameter("unitId", buttonMasterDto.getUnitId()).setResultTransformer(Transformers.aliasToBean(ButtonMasterDto.class)).list();
	            return new Response(SUCCESS, null, null, result,null);
	        }catch (Exception e) {
	        logger.error("getActiveSavedConfig failed", e);
	        return new Response(ERROR, null, null, null,null);
	        }
	}

	@Override
	public Response getSingleButtonById(ButtonMasterDto buttonMasterDto) {
		try {

			String sqlQueryForButtonId = "SELECT id AS \"buttonId\" from button_master where tab_id = :tabId and organization_id = :orgId";

			Integer buttonId = (Integer) sessionFactory.getCurrentSession().createSQLQuery(sqlQueryForButtonId)
					.setParameter("tabId", buttonMasterDto.getTabId())
					.setParameter("orgId", buttonMasterDto.getHospitalId())
					.uniqueResult();

			String sqlQuery = """
					WITH DoctorConfig AS (
					    SELECT json_configuration
					    FROM public.button_master_config
					    WHERE doctor_id =:doctorId\s
					    AND button_id =:buttonId\s
					    AND organization_id =:OrgId\
					    AND status = 'A'\
					),
					SpecialtyConfig AS (
					    SELECT json_configuration
					    FROM public.button_master_config
					    WHERE speciality_id =:specialityId
					    AND button_id =:buttonId\s
					    AND organization_id =:OrgId \
					    AND status = 'A'\
					),
					MasterConfig AS (
					    SELECT combined_ai_json,
					           system_prompt,
					           user_prompt
					    \
					    FROM public.button_master
					    WHERE tab_id =:tabId
					    AND organization_id =:OrgId \
					    AND status = 'A'\
					)
					SELECT COALESCE(dc.json_configuration, sc.json_configuration, mc.combined_ai_json) AS "jsonConfiguration", \
					 mc.system_prompt AS "systemPrompt", \
					 mc.user_prompt AS "userPrompt" \
					FROM MasterConfig mc
					LEFT JOIN DoctorConfig dc ON 1 = 1
					LEFT JOIN SpecialtyConfig sc ON 1 = 1 \
					LIMIT 1""";

			ButtonMasterDto result = (ButtonMasterDto) sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
					.setParameter("OrgId", buttonMasterDto.getHospitalId())
					.setParameter("doctorId", buttonMasterDto.getDoctorId())
					.setParameter("specialityId", buttonMasterDto.getSpecialityId())
					.setParameter("buttonId", buttonId)
					.setParameter("tabId", buttonMasterDto.getTabId())
					.setResultTransformer(Transformers.aliasToBean(ButtonMasterDto.class)).uniqueResult();
			result.setTabId(buttonMasterDto.getTabId());
			String strUserPrompt =result.getUserPrompt().replace("\n", "").replace("\r", "").replace("\"", "").replace("\\", "\\\\").replaceAll("[\\x00-\\x1F\\x7F]", "");
			String strSystemPrompt =result.getSystemPrompt().replace("\n", "").replace("\r", "").replace("\"", "").replace("\\", "\\\\").replaceAll("[\\x00-\\x1F\\x7F]", "");
			String replaceString = result.getJsonConfiguration().replace("@User_Prompt", strUserPrompt).replace("@System_prompt", result.getSystemPrompt());
			result.setJsonConfiguration(replaceString);
			return new Response(SUCCESS, null, null, null,result);
		} catch (Exception e) {
			String errorMsg =   UserRoleMasterDaoImpl.class.getName() + " : "+ e.getMessage();
			logger.error(e.getMessage(), e);
			return new Response(ERROR, null, errorMsg, null,null);
		}
	}

}

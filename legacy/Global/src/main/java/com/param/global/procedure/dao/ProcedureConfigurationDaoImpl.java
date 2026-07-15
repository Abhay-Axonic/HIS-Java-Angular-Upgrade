package com.param.global.procedure.dao;

import java.util.List;

import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.dozer.DozerFactory;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.otims.dto.ProcedureConfigurationNoteDto;
import com.param.global.procedure.dto.ProcedureConfigurationDto;
import com.param.otims.model.ProcedureConfiguration;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class ProcedureConfigurationDaoImpl extends GenericDao<ProcedureConfiguration, Integer> implements IProcedureConfigurationDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(ProcedureConfigurationDaoImpl.class);


	public ProcedureConfigurationDaoImpl() {
		super(ProcedureConfiguration.class);
	}

	@Autowired
	private DozerFactory factory;
	
	@Override
	public Integer saveProcedureConfiguration(ProcedureConfigurationDto dto) throws ApplicationException {
		try{
			ProcedureConfiguration config = factory.map(dto, ProcedureConfiguration.class, null);
			config.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			config = save(config);
			return config.getProcedureConfigId();
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getProcedureConfigList(int orgId, int unitId) throws ApplicationException {
		try{
			List<ProcedureConfigurationDto> listProcedureConfigurationDto = sessionFactory.getCurrentSession().getNamedQuery("GET_PROCEDURE_CONFIGURATION_LIST").setParameter("orgId", orgId)
						.setParameter("unitId", unitId).setResultTransformer(Transformers.aliasToBean(ProcedureConfigurationDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listProcedureConfigurationDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public boolean validProcConfByServiceOrgUnit(int serviceId, int orgId, int unitId) throws ApplicationException {
		try{
			return (boolean)sessionFactory.getCurrentSession().getNamedQuery("VALID_PROCEDURE_CONFIG_BY_PROCID_ORG_UNIT").setParameter("serviceId", serviceId)
						.setParameter("orgId", orgId).setParameter("unitId", unitId).uniqueResult();
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateProcedureConfigStatus(int id, char status) throws ApplicationException {
		try{
			int result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_PROC_CONF_STATUS_BY_ID").setParameter("id", id).setParameter("status", status).executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, result);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Integer updateProcedureConfiguration(ProcedureConfigurationDto dto) throws ApplicationException {
		try{
			ProcedureConfiguration config = factory.map(dto, ProcedureConfiguration.class, null);
			config.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			config.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			config = update(config);
			return config.getProcedureConfigId();
			//return new CommonResponse(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public boolean validProcConfByServiceOrgUnitConfigId(int serviceId, int orgId, int unitId, int configId)throws ApplicationException {
		try{
			return (boolean)sessionFactory.getCurrentSession().getNamedQuery("VALID_PROCEDURE_CONFIG_BY_PROCID_ORG_UNIT_CONFIG").setParameter("serviceId", serviceId)
					.setParameter("orgId", orgId).setParameter("unitId", unitId).setParameter("configId", configId).uniqueResult();
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public String getProcedureNameById(Integer id) {
		try{
			return (String) sessionFactory.getCurrentSession().createSQLQuery(
					"select procedure_type_name \"procedureTypeName\" "
							+ "from otims.m_procedure_type_master "
							+ "where procedure_type_id=:id  "
						)
					.setParameter("id",id)
					.uniqueResult();
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public String getSpecialityNameById(Integer specialityId) {
		try{
			return (String) sessionFactory.getCurrentSession().createSQLQuery(
					"select speciality_name as \"specialityName\" "
							+ "from doctor.m_speciality_master "
							+ "where speciality_id=:specialityId "
						)
					.setParameter("specialityId",specialityId)
					.uniqueResult();
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getFuturedOrderProcedureByEncounter(int patientId, int doctorId) throws ApplicationException {
		try
        {
            List<Integer> list = (List<Integer>)sessionFactory.getCurrentSession().getNamedQuery("GET_ORDERED_PROCEDURE_FOR_OPD")
                    .setParameter("patientId", patientId)
                    .setParameter("doctorId", doctorId)
                    .setParameter("dateTime", TimeZoneComponent.getDateByzone(0))
                    .list();
            return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
        } catch (Exception e)
        {
        logger.error("getFuturedOrderProcedureByEncounter failed", e);
        }
        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response convertProcedure(int admissionId, int visitTypeId, int proReqId) throws ApplicationException {
		try
        {
            int count = sessionFactory.getCurrentSession().getNamedQuery("CONVERT_PROCEDURES")
                    .setParameter("admissionId", admissionId)
                    .setParameter("visitTypeId", visitTypeId)
                    .setParameter("proReqId", proReqId)
                    .executeUpdate();
            return new Response<>(SUCCESS, SUCCESS_CODE, null, null, count);
        } catch (Exception e)
        {
        logger.error("convertProcedure failed", e);
        }
        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getorderDetailsIdByProcedureReqId(Integer proReqId) throws ApplicationException {
		try{
			List<OrderDetailsMasterDto> listProcedureConfigurationDto = sessionFactory.getCurrentSession().getNamedQuery("GET_ORDER_DETAILS_IDS_LIST")
						.setParameter("proReqId", proReqId).setResultTransformer(Transformers.aliasToBean(OrderDetailsMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listProcedureConfigurationDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getFuturedOrderProcedureByAdmission(Integer admissionId) throws ApplicationException {
		try
        {
            List<Integer> list = (List<Integer>)sessionFactory.getCurrentSession().getNamedQuery("GET_ORDERED_PROCEDURE_FOR_IPD")
                    .setParameter("admissionId", admissionId)
                    .setParameter("dateTime", TimeZoneComponent.getDateByzone(0))
                    .list();
            return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
        } catch (Exception e)
        {
        logger.error("getFuturedOrderProcedureByAdmission failed", e);
        }
        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getNotesListByProConfigId(Integer orgId, Integer unitId, Integer proConfigId)
			throws ApplicationException {
		try{
			List<ProcedureConfigurationNoteDto> listProcedureConfigurationDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NOTELIST_BY_PROCONFIG_ID")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setParameter("proConfigId", proConfigId)
					.setResultTransformer(Transformers.aliasToBean(ProcedureConfigurationNoteDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listProcedureConfigurationDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	
	
	

	
}

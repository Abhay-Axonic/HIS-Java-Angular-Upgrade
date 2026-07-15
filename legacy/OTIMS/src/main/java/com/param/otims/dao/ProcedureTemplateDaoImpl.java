package com.param.otims.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.dozer.DozerFactory;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.OTTDetailsVO;
import com.param.global.procedure.dto.ProcedureConfigurationDto;
import com.param.otims.model.ProcedureConfiguration;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class ProcedureTemplateDaoImpl extends GenericDao<ProcedureConfiguration, Integer> implements IProcedureTemplateDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(ProcedureTemplateDaoImpl.class);

	public ProcedureTemplateDaoImpl() {
		super(ProcedureConfiguration.class);
	}
	@Autowired
	private DozerFactory factory;
	
	@Override
	public Response saveProcedureConfigTemplate(ProcedureConfigurationDto reqDto) throws ApplicationException {
		try{
			ProcedureConfiguration config = factory.map(reqDto, ProcedureConfiguration.class, null);
			config.setCreatedDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()!=null ? reqDto.getUnitId(): null));
			config.setUpdatedDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()!=null ? reqDto.getUnitId(): null));
			if(reqDto.getProcedureConfigId()!=null) {
				config = update(config);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, config);
			}else {
				config = save(config);
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, config);
			}

		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public boolean validProcConfByServiceOrgUnitConfigId(Integer serviceId, Integer orgId, Integer unitId,
			int procedureConfigId) throws ApplicationException {
		try{
			return (boolean)sessionFactory.getCurrentSession().getNamedQuery("VALID_PROCEDURE_CONFIG_BY_PROCID_ORG_UNIT_CONFIG").setParameter("serviceId", serviceId)
					.setParameter("orgId", orgId).setParameter("unitId", unitId).setParameter("configId", procedureConfigId).uniqueResult();
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getProcedureConfigList(int orgId, int unitId) throws ApplicationException {
		try{
			List<ProcedureConfigurationDto> listProcedureConfigurationDto = sessionFactory.getCurrentSession().getNamedQuery("GET_PROCEDURE_CONFIG_LIST_ON_TEMPLATE").setParameter("orgId", orgId)
						.setParameter("unitId", unitId).setResultTransformer(Transformers.aliasToBean(ProcedureConfigurationDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listProcedureConfigurationDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getOTT(Integer procedureConfigId) throws ApplicationException {
		try{
			ProcedureConfigurationDto listProcedureConfigurationDto = (ProcedureConfigurationDto) sessionFactory.getCurrentSession().
					getNamedQuery("GET_PROCEDURE_CONFIG_OTT").setParameter("procedureConfigId", procedureConfigId)
					.setResultTransformer(Transformers.aliasToBean(ProcedureConfigurationDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, listProcedureConfigurationDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public boolean validProcConfByServiceOrgUnitServiceId(Integer serviceId, Integer orgId, Integer unitId) {
		try{
			return (boolean)sessionFactory.getCurrentSession().getNamedQuery("VALID_PROCEDURE_CONFIG_BY_PROCID_ORG_UNIT_SERVICEID").setParameter("serviceId", serviceId)
					.setParameter("orgId", orgId).setParameter("unitId", unitId).uniqueResult();
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getOTTThetreAndTableByServiceId(int serviceId, int orgId, int unitId) {
		 try{
			 OTTDetailsVO OTTDetailsObj = (OTTDetailsVO)sessionFactory.getCurrentSession().createSQLQuery(
					"select operation_theatre_type_id as \"theatreTypeId\","
				    +"cpt_description as \"theatreTypeName\"  "
					+" from otims.t_procedure_configuration where service_id ="+serviceId+" and status = 'A' and org_id ="+orgId+" and unit_id = "+unitId)
					.setResultTransformer(Transformers.aliasToBean(OTTDetailsVO.class)).uniqueResult();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, OTTDetailsObj);
	        } catch (Exception e)
	        {
	            logger.error(e.getMessage(), e);
	            throw new ServiceException(e.getMessage());
	        }
	}

}

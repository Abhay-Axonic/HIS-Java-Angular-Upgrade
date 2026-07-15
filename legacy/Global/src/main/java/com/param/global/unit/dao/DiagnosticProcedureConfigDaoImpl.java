package com.param.global.unit.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.master.unit.model.DiagnosticProcedureConfig;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.DiagnosticProcedureConfigDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DiagnosticProcedureConfigDaoImpl extends GenericDao<DiagnosticProcedureConfig, Integer>
		implements IDiagnosticProcedureConfigDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(DiagnosticProcedureConfigDaoImpl.class);


	public DiagnosticProcedureConfigDaoImpl() {
		super(DiagnosticProcedureConfig.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveDiagnosticProcedureConfig(DiagnosticProcedureConfigDto diagnosticProcedureConfigDto)
			throws ApplicationException {
		try {
			DiagnosticProcedureConfig diagnosticProcedureConfig = new DiagnosticProcedureConfig();

			diagnosticProcedureConfig.setSpecialityId(diagnosticProcedureConfigDto.getSpecialityId());
			diagnosticProcedureConfig.setProcedureServiceId(diagnosticProcedureConfigDto.getProcedureServiceId());
			diagnosticProcedureConfig.setCptCode(diagnosticProcedureConfigDto.getCptCode());
			diagnosticProcedureConfig.setCptDescription(diagnosticProcedureConfigDto.getCptDescription());
			diagnosticProcedureConfig.setProcedureDuration(diagnosticProcedureConfigDto.getProcedureDuration());
			//diagnosticProcedureConfig.setIsPACSRequired(diagnosticProcedureConfigDto.getIsPACSRequired());
			diagnosticProcedureConfig.setPatientInstruction(diagnosticProcedureConfigDto.getPatientInstruction());
			diagnosticProcedureConfig.setStatus(diagnosticProcedureConfigDto.getStatus());
			diagnosticProcedureConfig.setUnitId(diagnosticProcedureConfigDto.getUnitId());
			diagnosticProcedureConfig.setOrganizationId(diagnosticProcedureConfigDto.getOrganizationId());
			diagnosticProcedureConfig.setCreatedBy(diagnosticProcedureConfigDto.getCreatedBy());
			diagnosticProcedureConfig.setCreatedDate(
					GlobalCommonDateUtils.getDate(diagnosticProcedureConfigDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			diagnosticProcedureConfig.setUpdatedBy(diagnosticProcedureConfigDto.getUpdatedBy());
			diagnosticProcedureConfig.setUpdatedDate(
					GlobalCommonDateUtils.getDate(diagnosticProcedureConfigDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			sessionFactory.getCurrentSession().save(diagnosticProcedureConfig);
			return new Response(SUCCESS, SUCCESS_CODE, "Diagnostics Procedure Save Successfully", null, null);
		} catch (HibernateException he) {
		logger.error("saveDiagnosticProcedureConfig failed", he);
		} catch (Exception e) {
		logger.error("saveDiagnosticProcedureConfig failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDiagnosticProcedureConfigList(DiagnosticProcedureConfigDto diagnosticProcedureConfigDto)
			throws ApplicationException {
		try {
			List<DiagnosticProcedureConfigDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DIAGNOSTIC_PROCEDURE_CONFIG_MASTER_LIST")
					.setParameter("orgId", diagnosticProcedureConfigDto.getOrganizationId())
					.setParameter("unitId", diagnosticProcedureConfigDto.getUnitId())
					.setFirstResult(diagnosticProcedureConfigDto.getOffset() != null ? diagnosticProcedureConfigDto.getOffset()  : 0)
					.setMaxResults(diagnosticProcedureConfigDto.getNoOfRecordsPerPage() != null ? diagnosticProcedureConfigDto.getNoOfRecordsPerPage()  : 10)
			.setResultTransformer(Transformers.aliasToBean(DiagnosticProcedureConfigDto.class)).list();
				return new Response<>(SUCCESS, null, null, list, null);
			
		}catch (HibernateException he) {
		logger.error("getDiagnosticProcedureConfigList failed", he);
		} catch (Exception e) {
		logger.error("getDiagnosticProcedureConfigList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response updateDiagnosticProcedureConfig(DiagnosticProcedureConfigDto diagnosticProcedureConfigDto)
			throws ApplicationException {
		try {
			DiagnosticProcedureConfig diagnosticProcedureConfig = new DiagnosticProcedureConfig();
			
			diagnosticProcedureConfig.setDiagnosticProcedureConfigId(diagnosticProcedureConfigDto.getDiagnosticProcedureConfigId());
			diagnosticProcedureConfig.setSpecialityId(diagnosticProcedureConfigDto.getSpecialityId());
			diagnosticProcedureConfig.setProcedureServiceId(diagnosticProcedureConfigDto.getProcedureServiceId());
			diagnosticProcedureConfig.setCptCode(diagnosticProcedureConfigDto.getCptCode());
			diagnosticProcedureConfig.setCptDescription(diagnosticProcedureConfigDto.getCptDescription());
			diagnosticProcedureConfig.setIsPACSRequired(diagnosticProcedureConfigDto.getIsPACSRequired());
			diagnosticProcedureConfig.setPatientInstruction(diagnosticProcedureConfigDto.getPatientInstruction());
			diagnosticProcedureConfig.setProcedureDuration(diagnosticProcedureConfigDto.getProcedureDuration());
			diagnosticProcedureConfig.setStatus(diagnosticProcedureConfigDto.getStatus());
			diagnosticProcedureConfig.setUnitId(diagnosticProcedureConfigDto.getUnitId());
			diagnosticProcedureConfig.setOrganizationId(diagnosticProcedureConfigDto.getOrganizationId());
			diagnosticProcedureConfig.setUpdatedBy(diagnosticProcedureConfigDto.getUpdatedBy());
			diagnosticProcedureConfig.setUpdatedDate(
					GlobalCommonDateUtils.getDate(diagnosticProcedureConfigDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			sessionFactory.getCurrentSession().update(diagnosticProcedureConfig);
			return new Response(SUCCESS, SUCCESS_CODE, "Diagnostics Procedure Update Successfully", null, null);
		} catch (HibernateException he) {
		logger.error("updateDiagnosticProcedureConfig failed", he);
		} catch (Exception e) {
		logger.error("updateDiagnosticProcedureConfig failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDiagnosticProcedureConfigStatus(DiagnosticProcedureConfigDto diagnosticProcedureConfigDto)
			throws ApplicationException {
		try {
			DiagnosticProcedureConfig diagnosticProcedureConfig=findById(diagnosticProcedureConfigDto.getDiagnosticProcedureConfigId());
			diagnosticProcedureConfig.setDiagnosticProcedureConfigId(diagnosticProcedureConfigDto.getDiagnosticProcedureConfigId());
			diagnosticProcedureConfig.setStatus(diagnosticProcedureConfigDto.getStatus());
			diagnosticProcedureConfig.setUpdatedBy(diagnosticProcedureConfigDto.getUpdatedBy());
			diagnosticProcedureConfig.setUpdatedDate(GlobalCommonDateUtils.getDate(diagnosticProcedureConfigDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 update(diagnosticProcedureConfig);
			 
			return new Response(SUCCESS, SUCCESS_CODE, "Diagnostics Procedure Status Updated Successfully", null, null);
		}catch (HibernateException he) {
		logger.error("updateDiagnosticProcedureConfigStatus failed", he);
		} catch (Exception e) {
		logger.error("updateDiagnosticProcedureConfigStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDiagnosticProcedureConfigCount(DiagnosticProcedureConfigDto diagnosticProcedureConfigDto)
			throws ApplicationException {
		 try {
				Session session = sessionFactory.getCurrentSession();
				Criteria crit = session.createCriteria(DiagnosticProcedureConfig.class);
				crit.add(Restrictions.eq("organizationId", diagnosticProcedureConfigDto.getOrganizationId()));
				crit.add(Restrictions.eq("unitId", diagnosticProcedureConfigDto.getUnitId()));
				crit.setProjection(Projections.rowCount());
				Long count = (Long) crit.uniqueResult();
				
				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
					
			} catch (HibernateException he) {
			logger.error("getDiagnosticProcedureConfigCount failed", he);
			} catch (Exception e) {
			logger.error("getDiagnosticProcedureConfigCount failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDiagnosticProcedureConfigById(Integer diagnosticProcedureConfigId, Integer organizationId,
			Integer unitId) throws ApplicationException {
		try {
			DiagnosticProcedureConfigDto dto=(DiagnosticProcedureConfigDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DIAGNOSTIC_PROCEDURE_CONFIG_MASTER_BY_ID")
					.setParameter("id", diagnosticProcedureConfigId)
					.setParameter("orgId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DiagnosticProcedureConfigDto.class)).uniqueResult();
			 return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);
		} catch (HibernateException he) {
		logger.error("getDiagnosticProcedureConfigById failed", he);
		} catch (Exception e) {
		logger.error("getDiagnosticProcedureConfigById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getProcedureConfigByServiceId(DiagnosticProcedureConfigDto diagnosticProcedureConfigDto) {
		try {
			DiagnosticProcedureConfigDto dto=(DiagnosticProcedureConfigDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DIAGNOSTIC_PROCEDURE_CONFIG_MASTER_BY_SERVICE_ID")
					.setParameter("specId", diagnosticProcedureConfigDto.getSpecialityId())
					.setParameter("procedureServiceId", diagnosticProcedureConfigDto.getProcedureServiceId())
					.setParameter("orgId", diagnosticProcedureConfigDto.getOrganizationId())
					.setParameter("unitId", diagnosticProcedureConfigDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DiagnosticProcedureConfigDto.class)).uniqueResult();
			if(dto!=null)
			 return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);
			else 
			 return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("getProcedureConfigByServiceId failed", he);
		} catch (Exception e) {
		logger.error("getProcedureConfigByServiceId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getProcedureConfigByNameNotId(DiagnosticProcedureConfigDto diagnosticProcedureConfigDto) {
		try {
			DiagnosticProcedureConfigDto dto=(DiagnosticProcedureConfigDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DIAGNOSTIC_PROCEDURE_CONFIG_MASTER_BY_NAME_NOT_ID")
					.setParameter("procedureServiceId", diagnosticProcedureConfigDto.getProcedureServiceId())
					.setParameter("specId", diagnosticProcedureConfigDto.getSpecialityId())
					.setParameter("diagnosticProcedureConfigId", diagnosticProcedureConfigDto.getDiagnosticProcedureConfigId())
					.setParameter("orgId", diagnosticProcedureConfigDto.getOrganizationId())
					.setParameter("unitId", diagnosticProcedureConfigDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DiagnosticProcedureConfigDto.class)).uniqueResult();
			 if(dto!=null) {
				 return new Response(SUCCESS, SUCCESS_CODE, null, null,null);
			 }
			 else {
				 return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			 }
		} catch (HibernateException he) {
		logger.error("getProcedureConfigByNameNotId failed", he);
		} catch (Exception e) {
		logger.error("getProcedureConfigByNameNotId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	

}

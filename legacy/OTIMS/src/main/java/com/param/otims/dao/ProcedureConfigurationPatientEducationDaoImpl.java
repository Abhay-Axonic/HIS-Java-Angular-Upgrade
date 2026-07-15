package com.param.otims.dao;

import com.param.global.otims.dto.ProcedureConfigurationNoteDto;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.ProcedureConfigurationPatientEducationDto;
import com.param.global.procedure.dto.ProcedureConfigurationDto;
import com.param.otims.model.ProcedureConfigurationPatientEducation;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import java.util.List;

@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class ProcedureConfigurationPatientEducationDaoImpl extends GenericDao<ProcedureConfigurationPatientEducation, Integer> implements IProcedureConfigurationPatientEducationDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(ProcedureConfigurationPatientEducationDaoImpl.class);

	public ProcedureConfigurationPatientEducationDaoImpl() {
		super(ProcedureConfigurationPatientEducation.class);
	}

	@Override
	public Response saveProcedureConfigPatientEducation(ProcedureConfigurationPatientEducationDto patientEduObj,
			int procedureConfigId) throws ApplicationException{
		try {
			
			ProcedureConfigurationPatientEducation  mstr =  new ProcedureConfigurationPatientEducation();
			mstr.setPatientEducationTempale(patientEduObj.getPatientEducationTempale());
			mstr.setCreatedBy(patientEduObj.getCreatedBy());
			mstr.setCreatedDate(GlobalCommonDateUtils.getDate(patientEduObj.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setOrganizationId(patientEduObj.getOrganizationId());
			mstr.setProcedureConfigId(procedureConfigId);
			mstr.setStatus(patientEduObj.getStatus());
			mstr.setUnitId(patientEduObj.getUnitId());
			mstr.setUpdatedBy(patientEduObj.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(patientEduObj.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			if(patientEduObj.getProcedureConfigPatientEducationId()!=null) {
				mstr.setProcedureConfigPatientEducationId(patientEduObj.getProcedureConfigPatientEducationId());
				update(mstr);
			}else {
				save(mstr);
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getPatientEducationByProcedureConfigId(ProcedureConfigurationDto reqDto)
			throws ApplicationException {
		try {
            String query = sessionFactory.getCurrentSession()
                    .getNamedQuery("GET_PATIENTEDU_BY_PROCEDURE_CONFIG_ID")
                    .getQueryString();
            
            StringBuffer subQuery = new StringBuffer();
            if(reqDto.getProcedureConfigId()!=null){
                subQuery.append(" AND mstr.procedureConfigId=:procedureConfigId ");
            }
            
            if(reqDto.getServiceId()!=null ){
                subQuery.append(" AND procedureConfiguration.serviceId=:serviceId ");
            }
            
            query = query.concat(subQuery.toString());
           Query createQuery=sessionFactory.getCurrentSession().createQuery(query)
                    .setParameter("organizationId", reqDto.getOrgId())
                    .setParameter("unitId", reqDto.getUnitId());

			if(reqDto.getProcedureConfigId()!=null){
				createQuery.setParameter("procedureConfigId", reqDto.getProcedureConfigId());
			}
			if(reqDto.getServiceId()!=null ){
				createQuery.setParameter("serviceId", reqDto.getServiceId());
			}
			ProcedureConfigurationPatientEducationDto obj = (ProcedureConfigurationPatientEducationDto) createQuery
					.setResultTransformer(Transformers.aliasToBean(ProcedureConfigurationPatientEducationDto.class))
					.uniqueResult();
          return new Response(SUCCESS, SUCCESS_CODE, null, null, obj);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}

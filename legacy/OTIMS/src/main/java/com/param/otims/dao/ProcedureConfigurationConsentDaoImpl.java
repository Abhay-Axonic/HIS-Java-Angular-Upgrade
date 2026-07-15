package com.param.otims.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.ProcedureConfigurationConsentDto;
import com.param.global.procedure.dto.ProcedureConfigurationDto;
import com.param.otims.model.ProcedureConfigurationConsent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class ProcedureConfigurationConsentDaoImpl extends GenericDao<ProcedureConfigurationConsent, Integer> implements IProcedureConfigurationConsentDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(ProcedureConfigurationConsentDaoImpl.class);

	public ProcedureConfigurationConsentDaoImpl() {
		super(ProcedureConfigurationConsent.class);
	}

	@Override
	public Response saveProcedureConfigConsent(List<ProcedureConfigurationConsentDto> consentDetailList,
			int procedureConfigId) throws ApplicationException {
		try {
			
				for (ProcedureConfigurationConsentDto dto : consentDetailList) {
					ProcedureConfigurationConsent mstr = new ProcedureConfigurationConsent();
					
					mstr.setConsentId(dto.getConsentId());
					mstr.setCreatedBy(dto.getCreatedBy());
					mstr.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
					mstr.setOrganizationId(dto.getOrganizationId());
					mstr.setProcedureConfigId(procedureConfigId);
					mstr.setStatus(dto.getStatus());
					mstr.setUnitId(dto.getUnitId());
					mstr.setUpdatedBy(dto.getUpdatedBy());
					mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
					if(dto.getProcedureConfigConsentId()!=null) {
						mstr.setProcedureConfigConsentId(dto.getProcedureConfigConsentId());
						update(mstr);
					}else {
					save(mstr);
					}
				}
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getConsentListByProcedureConfigId(ProcedureConfigurationDto dto)
			throws ApplicationException {
		try {
            String query = sessionFactory.getCurrentSession()
                    .getNamedQuery("GET_CONSENT_LIST_BY_PROCEDURE_CONFIG_ID")
                    .getQueryString();
            StringBuffer subQuery = new StringBuffer();
            if(dto.getProcedureConfigId()!=null){
                subQuery.append(" AND mstr.procedureConfigId=:procedureConfigId ");
            }
            if(dto.getServiceId()!=null ){
                subQuery.append(" AND procedureConfiguration.serviceId=:serviceId ");
            }
            query = query.concat(subQuery.toString());
            Query createQuery=sessionFactory.getCurrentSession().createQuery(query)
                    .setParameter("organizationId", dto.getOrgId())
                    .setParameter("unitId", dto.getUnitId());

			if(dto.getProcedureConfigId()!=null){
				createQuery.setParameter("procedureConfigId", dto.getProcedureConfigId());
			}

			if(dto.getServiceId()!=null ){
				createQuery.setParameter("serviceId", dto.getServiceId());
			}

			List<ProcedureConfigurationConsentDto> list = createQuery .setResultTransformer(Transformers.aliasToBean(ProcedureConfigurationConsentDto.class)).list();
            
            return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}

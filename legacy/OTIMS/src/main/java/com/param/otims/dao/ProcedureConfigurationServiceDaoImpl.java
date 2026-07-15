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
import com.param.global.otims.dto.ProcedureConfigurationServiceDto;
import com.param.global.procedure.dto.ProcedureConfigurationDto;
import com.param.otims.model.ProcedureConfigurationService;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class ProcedureConfigurationServiceDaoImpl extends GenericDao<ProcedureConfigurationService, Integer> implements IProcedureConfigurationServiceDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(ProcedureConfigurationServiceDaoImpl.class);

	public ProcedureConfigurationServiceDaoImpl() {
		super(ProcedureConfigurationService.class);
	}

	@Override
	public Response saveProcedureConfigService(List<ProcedureConfigurationServiceDto> serviceList,
			int procedureConfigId) throws ApplicationException {
		try {
			
			for (ProcedureConfigurationServiceDto dto : serviceList) {
				
				ProcedureConfigurationService  mstr =  new ProcedureConfigurationService();
				mstr.setServiceId(dto.getServiceId());
				mstr.setCreatedBy(dto.getCreatedBy());
				mstr.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
				mstr.setOrganizationId(dto.getOrganizationId());
				mstr.setProcedureConfigId(procedureConfigId);
				mstr.setStatus(dto.getStatus());
				mstr.setUnitId(dto.getUnitId());
				mstr.setUpdatedBy(dto.getUpdatedBy());
				mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
				if(dto.getProcedureConfigServiceId()!=null) {
					mstr.setProcedureConfigServiceId(dto.getProcedureConfigServiceId());
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
	public Response getServiceListByProcedureConfigId(ProcedureConfigurationDto dto) throws ApplicationException {
		try {
            String query = sessionFactory.getCurrentSession()
                    .getNamedQuery("GET_SERVICE_LIST_BY_PROCEDURE_CONFIG_ID")
                    .getQueryString();
            
            StringBuffer subQuery = new StringBuffer();
            if(dto.getProcedureConfigId()!=null){
                subQuery.append(" AND mstr.procedureConfigId=:procedureConfigId ");
            }
            
            if(dto.getServiceId()!=null ){
                subQuery.append(" AND procedureConfiguration.serviceId=:serviceId ");
            }
            
            query = query.concat(subQuery.toString());
           Query createQuery= sessionFactory.getCurrentSession().createQuery(query)
                    .setParameter("organizationId", dto.getOrgId())
                    .setParameter("unitId", dto.getUnitId());


			if(dto.getProcedureConfigId()!=null){
				createQuery.setParameter("procedureConfigId", dto.getProcedureConfigId());
			}
			if(dto.getServiceId()!=null ){
				createQuery.setParameter("serviceId", dto.getServiceId());
			}


			List<ProcedureConfigurationServiceDto> list =createQuery.setResultTransformer(Transformers.aliasToBean(ProcedureConfigurationServiceDto.class)).list();
           
            return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}

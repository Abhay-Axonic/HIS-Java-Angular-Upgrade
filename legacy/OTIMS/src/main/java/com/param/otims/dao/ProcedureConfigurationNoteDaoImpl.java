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
import com.param.global.otims.dto.ProcedureConfigurationNoteDto;
import com.param.global.procedure.dto.ProcedureConfigurationDto;
import com.param.otims.model.ProcedureConfigurationNote;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class ProcedureConfigurationNoteDaoImpl extends GenericDao<ProcedureConfigurationNote, Integer> implements IProcedureConfigurationNoteDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(ProcedureConfigurationNoteDaoImpl.class);

	public ProcedureConfigurationNoteDaoImpl() {
		super(ProcedureConfigurationNote.class);
	}

	@Override
	public Response saveProcedureConfigNote(List<ProcedureConfigurationNoteDto> noteList, int procedureConfigId)
			throws ApplicationException {
try {
			
			for (ProcedureConfigurationNoteDto dto : noteList) {
				ProcedureConfigurationNote mstr = new ProcedureConfigurationNote();
				mstr.setNoteMasterId(dto.getNoteMasterId());
				mstr.setNoteTypeId(dto.getNoteType());
				mstr.setCreatedBy(dto.getCreatedBy());
				mstr.setTemplate(dto.getTemplate());
				mstr.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
				mstr.setOrganizationId(dto.getOrganizationId());
				mstr.setProcedureConfigId(procedureConfigId);
				mstr.setStatus(dto.getStatus());
				mstr.setUnitId(dto.getUnitId());
				mstr.setUpdatedBy(dto.getUpdatedBy());
				mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
				if(dto.getProcedureConfigNoteId()!=null) {
					mstr.setProcedureConfigNoteId(dto.getProcedureConfigNoteId());
					update(mstr);
				}else {
				save(mstr);
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response getNoteByProcedureConfigId(ProcedureConfigurationDto reqDto) throws ApplicationException {
		try {
            String query = sessionFactory.getCurrentSession()
                    .getNamedQuery("GET_NOTE_BY_PROCEDURE_CONFIG_ID")
                    .getQueryString();
            
            StringBuffer subQuery = new StringBuffer();
            if(reqDto.getProcedureConfigId()!=null){
                subQuery.append(" AND mstr.procedureConfigId=:procedureConfigId ");
            }
            
            if(reqDto.getServiceId()!=null ){
                subQuery.append(" AND procedureConfiguration.serviceId=:serviceId ");
            }
            
            query = query.concat(subQuery.toString());
        Query createQuery= sessionFactory.getCurrentSession().createQuery(query)
                    .setParameter("organizationId", reqDto.getOrgId())
                    .setParameter("unitId", reqDto.getUnitId());

			if(reqDto.getProcedureConfigId()!=null){
				createQuery.setParameter("procedureConfigId", reqDto.getProcedureConfigId());
			}
			if(reqDto.getServiceId()!=null ){
				createQuery.setParameter("serviceId", reqDto.getServiceId());
			}

			List<ProcedureConfigurationNoteDto> list =  createQuery.setResultTransformer(Transformers.aliasToBean(ProcedureConfigurationNoteDto.class)).list();
            
            return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}

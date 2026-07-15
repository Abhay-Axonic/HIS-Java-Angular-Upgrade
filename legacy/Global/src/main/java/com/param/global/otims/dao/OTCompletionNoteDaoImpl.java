package com.param.global.otims.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.OTCompletionNoteDto;
import com.param.otims.model.OTCompletionNote;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class OTCompletionNoteDaoImpl extends GenericDao<OTCompletionNote, Integer> implements IOTCompletionNoteDao, ICommonConstants{

	public OTCompletionNoteDaoImpl() {
		super(OTCompletionNote.class);
	}

	@Override
	public Response saveOtCompletionNote(OTCompletionNoteDto dto) throws ApplicationException {
		try{
			
			if(dto.getOtCompletionNoteId()!=null) {
				OTCompletionNote master = findById(dto.getOtCompletionNoteId());
				master.setTemplate(dto.getTemplate());
				master.setStatus(dto.getStatus());
				master = update(master);
				
			}else {
				OTCompletionNote master = new OTCompletionNote();
				master.setProcedureOrderId(dto.getProcedureOrderId()!=null? dto.getProcedureOrderId(): null);
				master.setNoteMasterId(dto.getNoteMasterId());
				master.setNoteTypeId(dto.getNoteTypeId());
				master.setTemplate(dto.getTemplate());
				master.setStatus('A');
				master.setOrganizationId(dto.getOrganizationId());
				master.setUnitId(dto.getUnitId());
				master.setCreatedBy(dto.getCreatedBy());
				master.setProReqId(dto.getProReqId());
				master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				master.setUpdatedBy(dto.getUpdatedBy());
				master.setTemplateTransactionId(dto.getTemplateTransactionId());
				master.setTemplateFormatId(dto.getTemplateFormatId());
				master.setTemplateTypeId(dto.getTemplateTypeId());
				master.setTemplateId(dto.getTemplateId());
				master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				master = save(master);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}	
	}

	@Override
	public Response getOtNoteList(int proReqId) throws ApplicationException {
		try{
			List<OTCompletionNoteDto> listOTNoteDto = sessionFactory.getCurrentSession().getNamedQuery("GET_OT_NOTE_BY_PRO_REQ_ID").setParameter("id", proReqId).setResultTransformer(Transformers.aliasToBean(OTCompletionNoteDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listOTNoteDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

}

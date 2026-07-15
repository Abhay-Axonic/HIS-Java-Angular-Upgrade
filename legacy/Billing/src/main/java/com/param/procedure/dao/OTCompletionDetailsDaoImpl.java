package com.param.procedure.dao;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.otims.model.OTCompletionDetails;
import com.param.procedure.dto.OTCompletionDetailsDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes"})
public class OTCompletionDetailsDaoImpl  extends GenericDao<OTCompletionDetails, Integer> implements IOTCompletionDetailsDao, ICommonConstants{

	public OTCompletionDetailsDaoImpl() {
		super(OTCompletionDetails.class);
	}

	@Override
	public Response saveOtCompletionDetails(OTCompletionDetailsDto dto) throws ApplicationException {
		try{
			if(dto.getOtDate()!=null) {
			
			if(dto.getOtDate() == "Invalid Date") {
				dto.setOtDate( GlobalCommonDateUtils.getAvalDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			}
			}
			OTCompletionDetails master = new OTCompletionDetails();
			master.setBillingBedCategoryId(dto.getBillingBedCategoryId());
			master.setAnesthesiaTypeId(dto.getAnesthesiaTypeId());
			master.setOtTableId(dto.getOtTableId());
			master.setTotalHrs(dto.getDuration());
			master.setOtSheetNo(dto.getOtSheetNo());
			master.setRemark(dto.getRemark());
			master.setEmergancy(dto.getEmergancy());
			master.setOtDate(dto.getOtDate() != null  && !dto.getOtDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getOtDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setOrganizationId(dto.getOrganizationId());
			master.setUnitId(dto.getUnitId());
			master.setCreatedBy(dto.getCreatedBy());
			master.setProReqId(dto.getProReqId());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setStatus(dto.getStatus());
			master.setEncounterId(dto.getEncounterId());
			master.setEncounterNumber(dto.getEncounterNumber());
			master.setOtEmergancyFlow(dto.getOtEmergancyFlow());
			if(dto.getOtCompletionDetailsId()!=null) {
				master.setOtCompletionDetailsId(dto.getOtCompletionDetailsId());
				master = update(master);
				
			}else {
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
	public Response getOtCompletionDetials(int proReqId) throws ApplicationException {
		try{
			OTCompletionDetailsDto obj = (OTCompletionDetailsDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OT_COMPLETION_DETAILS_PRO_REQ_ID").setParameter("id", proReqId)
					.setResultTransformer(Transformers.aliasToBean(OTCompletionDetailsDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, obj);
		}catch(Exception e){
			
			throw e;
		}
	}

}

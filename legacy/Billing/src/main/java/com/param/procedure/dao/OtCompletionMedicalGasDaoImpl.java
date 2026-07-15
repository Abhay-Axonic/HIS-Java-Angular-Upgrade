package com.param.procedure.dao;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.procedure.dto.OtCompletionMedicalGasDto;
import com.param.otims.model.OtCompletionMedicalGas;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class OtCompletionMedicalGasDaoImpl extends GenericDao<OtCompletionMedicalGas, Integer> implements IOtCompletionMedicalGasDao, ICommonConstants{

	public OtCompletionMedicalGasDaoImpl() {
		super(OtCompletionMedicalGas.class);
	}

	@Override
	public Response saveOtCompletionMedicalGas(OtCompletionMedicalGasDto medicalGas) throws ApplicationException {
		try{


			LocalTime fromTime1 = LocalTime.parse(medicalGas.getFromTime());
			LocalDate today = LocalDate.now();
			Instant fromTime = fromTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

			LocalTime toTime1 = LocalTime.parse(medicalGas.getToTime());
			Instant toTime = toTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

			if(medicalGas.getOtCompletionMedicalGasId()!=null) {
				OtCompletionMedicalGas master = findById(medicalGas.getOtCompletionMedicalGasId());
				master.setStatus(medicalGas.getStatus());
				master.setProcedureOrderId(medicalGas.getProcedureOrderId()!=null? medicalGas.getProcedureOrderId(): null);
				master.setFromTime(fromTime);
				master.setToTime(toTime);
				master.setServiceId(medicalGas.getServiceId());
				master.setFromDate(medicalGas.getFromDate() != null && !medicalGas.getFromDate().isEmpty() ? GlobalCommonDateUtils.getDate(medicalGas.getFromDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(medicalGas.getUnitId()));
				master.setToDate(medicalGas.getToDate() != null && !medicalGas.getToDate().isEmpty() ? GlobalCommonDateUtils.getDate(medicalGas.getToDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(medicalGas.getUnitId()));
				master = update(master);
				
			}else {
				
				OtCompletionMedicalGas master = new OtCompletionMedicalGas();
				master.setProcedureOrderId(medicalGas.getProcedureOrderId()!=null? medicalGas.getProcedureOrderId(): null);
				master.setFromTime(fromTime);
				master.setToTime(toTime);
				master.setServiceId(medicalGas.getServiceId());
				master.setMrp(0);
				master.setOrgId(medicalGas.getOrgId());
				master.setFromDate(medicalGas.getFromDate() != null && !medicalGas.getFromDate().isEmpty() ? GlobalCommonDateUtils.getDate(medicalGas.getFromDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(medicalGas.getUnitId()));
				master.setToDate(medicalGas.getToDate() != null && !medicalGas.getToDate().isEmpty() ? GlobalCommonDateUtils.getDate(medicalGas.getToDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(medicalGas.getUnitId()));
				master.setStatus('A');
				master.setUnitId(medicalGas.getUnitId());
				master.setCreatedBy(medicalGas.getCreatedBy());
				master.setProReqId(medicalGas.getProReqId());
				master.setCreatedDate(medicalGas.getCreatedDate() != null && !medicalGas.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(medicalGas.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(medicalGas.getUnitId()));
				master.setUpdatedBy(medicalGas.getUpdatedBy());
				master.setUpdatedDate(medicalGas.getUpdatedDate() != null && !medicalGas.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(medicalGas.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(medicalGas.getUnitId()));
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
	public Response getOTMedicalGas(int proReqId) throws ApplicationException {
		try{
			List<OtCompletionMedicalGasDto> listOTNoteDto = sessionFactory.getCurrentSession().getNamedQuery("GET_OT_MEDICAL_GAS_BY_PRO_REQ_ID")
					.setParameter("id", proReqId).setResultTransformer(Transformers.aliasToBean(OtCompletionMedicalGasDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listOTNoteDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

}

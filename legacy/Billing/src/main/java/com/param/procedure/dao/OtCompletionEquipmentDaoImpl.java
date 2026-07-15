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
import com.param.otims.model.OtCompletionEquipment;
import com.param.procedure.dto.OtCompletionEquipmentDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class OtCompletionEquipmentDaoImpl extends GenericDao<OtCompletionEquipment, Integer> implements IOtCompletionEquipmentDao, ICommonConstants{

	public OtCompletionEquipmentDaoImpl() {
		super(OtCompletionEquipment.class);
	}

	@Override
	public Response saveOtCompletionEquipment(OtCompletionEquipmentDto equipment) throws ApplicationException {
		try{
			
			if(equipment.getOtCompletionEquipmentId()!=null) {
				OtCompletionEquipment master = findById(equipment.getOtCompletionEquipmentId());
				master.setStatus(equipment.getStatus());
				master.setProcedureOrderId(equipment.getProcedureOrderId()!=null? equipment.getProcedureOrderId(): null);
				master.setAmount(equipment.getAmount());
				master = update(master);
				
			}else {
				LocalTime fromTime1 = LocalTime.parse(equipment.getFromTime());
				LocalDate today = LocalDate.now();
				Instant fromTime = fromTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

				LocalTime toTime1 = LocalTime.parse(equipment.getToTime());
				Instant toTime = toTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

				OtCompletionEquipment master = new OtCompletionEquipment();
				master.setProcedureOrderId(equipment.getProcedureOrderId()!=null? equipment.getProcedureOrderId(): null);
				master.setServiceId(equipment.getServiceId());
				master.setBatchId(equipment.getBatchId());
				master.setFromTime(fromTime);
				master.setToTime(toTime);
				master.setFromDate(GlobalCommonDateUtils.getDate(equipment.getFromDate(),"yyyy-MM-dd") );
				master.setToDate(GlobalCommonDateUtils.getDate(equipment.getToDate(),"yyyy-MM-dd") );
				master.setEquipmentTypeId(equipment.getEquipmentTypeId());
				master.setEquipmentId(equipment.getItemId());
				master.setAmount(equipment.getAmount());
				master.setStatus('A');
				master.setOrgId(equipment.getOrganizationId());
				master.setUnitId(equipment.getUnitId());
				master.setCreatedBy(equipment.getCreatedBy());
				master.setProReqId(equipment.getProReqId());
				master.setCreatedDate(equipment.getCreatedDate() != null && !equipment.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(equipment.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(equipment.getUnitId()));
				master.setUpdatedBy(equipment.getUpdatedBy());
				master.setUpdatedDate(equipment.getUpdatedDate() != null && !equipment.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(equipment.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(equipment.getUnitId()));
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
	public Response getOtCompletionEquipment(int proReqId) throws ApplicationException {
		try{
			List<OtCompletionEquipmentDto> listOTNoteDto = sessionFactory.getCurrentSession().getNamedQuery("GET_OT_EQUIPMENT_BY_PRO_REQ_ID")
					.setParameter("id", proReqId).setResultTransformer(Transformers.aliasToBean(OtCompletionEquipmentDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listOTNoteDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

}

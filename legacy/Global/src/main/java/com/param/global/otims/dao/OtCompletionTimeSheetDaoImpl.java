package com.param.global.otims.dao;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.OtCompletionTimeSheetDto;
import com.param.otims.model.OtCompletionTimeSheet;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes"})
public class OtCompletionTimeSheetDaoImpl extends GenericDao<OtCompletionTimeSheet, Integer> implements IOtCompletionTimeSheetDao, ICommonConstants{

	public OtCompletionTimeSheetDaoImpl() {
		super(OtCompletionTimeSheet.class);
	}
	
	@Override
	public Response saveOrUpdateOtCompletionTimeSheet(OtCompletionTimeSheetDto dto) throws ApplicationException {
		try{
			OtCompletionTimeSheet timeSheetNew = new OtCompletionTimeSheet();
			if(dto.getOtCompletionTimeSheetId()!=null) {
				timeSheetNew =  findById(dto.getOtCompletionTimeSheetId());
			}
			//OtCompletionTimeSheet timeSheet = new OtCompletionTimeSheet();
			timeSheetNew.setActualDischarge(dto.getActualDischarge() != null && !dto.getActualDischarge().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getActualDischarge(), "dd-M-yyyy HH:mm:ss") : (timeSheetNew.getActualDischarge()!=null ?timeSheetNew.getActualDischarge() : null ));
			timeSheetNew.setAnesthesiaTime(dto.getAnesthesiaTime() != null && !dto.getAnesthesiaTime().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getAnesthesiaTime(), "dd-M-yyyy HH:mm:ss") : (timeSheetNew.getAnesthesiaTime()!=null ? timeSheetNew.getAnesthesiaTime() : null));
			timeSheetNew.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			timeSheetNew.setProcedureEndTime(dto.getProcedureEndTime() != null && !dto.getProcedureEndTime().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getProcedureEndTime(), "dd-M-yyyy HH:mm:ss") : (timeSheetNew.getProcedureEndTime()!=null ? timeSheetNew.getProcedureEndTime() : null));
			timeSheetNew.setProcedureStartTime(dto.getProcedureStartTime() != null && !dto.getProcedureStartTime().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getProcedureStartTime(), "dd-M-yyyy HH:mm:ss") : (timeSheetNew.getProcedureStartTime()!=null ? timeSheetNew.getProcedureStartTime() : null));
			timeSheetNew.setScheduleDischarge(dto.getScheduleDischarge() != null && !dto.getScheduleDischarge().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getScheduleDischarge(), "dd-M-yyyy HH:mm:ss") : (timeSheetNew.getScheduleDischarge()!=null ? timeSheetNew.getScheduleDischarge() : null));
			timeSheetNew.setTableOnTime(dto.getTableOnTime() != null && !dto.getTableOnTime().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getTableOnTime(), "dd-M-yyyy HH:mm:ss") : (timeSheetNew.getTableOnTime()!=null ? timeSheetNew.getTableOnTime() : null));
			timeSheetNew.setTableOutTime(dto.getTableOutTime() != null && !dto.getTableOutTime().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getTableOutTime(), "dd-M-yyyy HH:mm:ss") : (timeSheetNew.getTableOnTime()!=null ? timeSheetNew.getTableOnTime() : null));
			timeSheetNew.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			timeSheetNew.setWheelInTime(dto.getWheelInTime() != null && !dto.getWheelInTime().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getWheelInTime(), "dd-M-yyyy HH:mm:ss") : (timeSheetNew.getWheelInTime()!=null ?timeSheetNew.getWheelInTime() : null));
			timeSheetNew.setWheelOutTime(dto.getWheelOutTime() != null && !dto.getWheelOutTime().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getWheelOutTime(), "dd-M-yyyy HH:mm:ss") : (timeSheetNew.getWheelOutTime()!=null ? timeSheetNew.getWheelOutTime() : null));
			timeSheetNew.setProReqId(dto.getProReqId());
			timeSheetNew.setStatus('A');
			timeSheetNew.setOrgId(dto.getOrgId()!=null ? dto.getOrgId() : timeSheetNew.getOrgId());
			timeSheetNew.setUnitId(dto.getUnitId()!=null ? dto.getUnitId() : timeSheetNew.getUnitId());
			timeSheetNew.setCreatedBy(dto.getCreatedBy()!=null ? dto.getCreatedBy() : timeSheetNew.getCreatedBy());
			timeSheetNew.setUpdatedBy(dto.getCreatedBy()!=null ? dto.getCreatedBy() : timeSheetNew.getCreatedBy());
			if(dto.getOtCompletionTimeSheetId()!=null) {
				timeSheetNew.setOtCompletionTimeSheetId(dto.getOtCompletionTimeSheetId());
				sessionFactory.getCurrentSession().update(timeSheetNew);
			}else {
				sessionFactory.getCurrentSession().save(timeSheetNew);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, timeSheetNew);
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response getOtTimeSheetByProReq(int id) throws ApplicationException {
		try{
			OtCompletionTimeSheetDto dto = (OtCompletionTimeSheetDto)sessionFactory.getCurrentSession().getNamedQuery("GET_OT_TIME_SHEETS_BY_PRO_REQ_ID").setParameter("id", id).setResultTransformer(Transformers.aliasToBean(OtCompletionTimeSheetDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(Exception e){
			throw e;
		}
	}

	
}
package com.param.global.doctor.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.ScheduleMasterDto;
import com.param.global.model.DoctorSchedule;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IDoctorScheduleDao extends IGenericDao<DoctorSchedule, Integer>{
	public Response getDoctorScheduleForSync(int id)throws ApplicationException;
	public Response getDoctorChargesForSyncByDoctorId(Integer recordId) throws ApplicationException;
	Response saveDoctorSchedule(List<ScheduleMasterDto> scheduleMasterDtosList, String action)throws ApplicationException;
}

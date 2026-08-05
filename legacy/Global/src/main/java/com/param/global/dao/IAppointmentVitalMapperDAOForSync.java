package com.param.global.dao;

import com.param.global.common.Response;
import com.param.opd.coversheet.model.AppointmentVitalMapper;

import in.param.common.dao.IGenericDao;
@SuppressWarnings("rawtypes")
public interface IAppointmentVitalMapperDAOForSync extends IGenericDao<AppointmentVitalMapper, Integer>{

	Response getVitalByPatientIdAndVitalIdForSync(Integer recordId);

	Response getVitalForSyncById(Integer recordId, Integer organizationId, Integer unitId);

}

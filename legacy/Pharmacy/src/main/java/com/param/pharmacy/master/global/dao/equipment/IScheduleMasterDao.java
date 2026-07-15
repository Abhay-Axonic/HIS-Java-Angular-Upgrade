package com.param.pharmacy.master.global.dao.equipment;



import com.param.entity.model.pharmacy.ScheduleMaster;
import com.param.global.common.Response;
import com.param.pharmacy.master.global.dto.equipment.ScheduleMasterDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IScheduleMasterDao extends IGenericDao<ScheduleMaster, Integer> {

	public Response saveScheduleMaster(ScheduleMasterDto serologyTestMasterDto) throws ApplicationException;

	public Response getScheduleMasterById(Integer orgId, Integer unitId, Integer scheduleId)
			throws ApplicationException;

	public Response updateScheduleMaster(ScheduleMasterDto seroTestMasterDto) throws ApplicationException;

	public Response ActivateInactivateScheduleMaster(Integer orgId, Integer scheduleId, Character scheduleStatus)
			throws ApplicationException;

	public Response listScheduleMaster(Integer orgId, Integer unitId, Integer offset, Integer recordPerPage)
			throws ApplicationException;

	public Response getToTalScheduleMasterRecord(Integer orgId, Integer unitId) throws ApplicationException;

}

package com.param.pharmacy.master.global.service.equipment;



import com.param.global.common.Response;
import com.param.pharmacy.master.global.dto.equipment.GenericScheduleMasterDto;
import com.param.pharmacy.master.global.dto.equipment.ScheduleMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IScheduleMasterService {

	public Response saveScheduleMaster(ScheduleMasterDto scheduleMasterDto) throws ApplicationException;

	public Response getScheduleMasterById(Integer orgId, Integer unitId, Integer scheduleId)
			throws ApplicationException;

	public Response updateScheduleMaster(ScheduleMasterDto scheduleMasterDto) throws ApplicationException;

	public Response ActivateInactivateScheduleMaster(Integer orgId, Integer scheduleId, Character scheduleStatus)
			throws ApplicationException;

	public Response listScheduleMaster(Integer orgId, Integer unitId, Integer offset, Integer recordPerPage)
			throws ApplicationException;

	public Response getToTalScheduleMasterRecord(Integer orgId, Integer unitId) throws ApplicationException;

	

}

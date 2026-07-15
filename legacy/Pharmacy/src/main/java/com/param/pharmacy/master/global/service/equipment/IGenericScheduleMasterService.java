package com.param.pharmacy.master.global.service.equipment;



import com.param.global.common.Response;
import com.param.global.dto.ServiceMasterDto;
import com.param.pharmacy.master.global.dto.equipment.GenericScheduleMasterDto;
import com.param.pharmacy.master.global.dto.equipment.ScheduleMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IGenericScheduleMasterService {

    public Response listScheduleDesc(Integer orgId, Integer unitId) throws ApplicationException;

    public Response searchServicesByNameAndCode(ServiceMasterDto serviceMasterDto)throws ApplicationException;

    public Response saveGenericScheduleMaster(GenericScheduleMasterDto genericScheduleMasterDto)throws ApplicationException;

	public Response getGenericScheduleMasterById(Integer orgId, Integer unitId,
			Integer genericScheduleId)throws ApplicationException;
	
	public Response updateGenericScheduleMaster(
			GenericScheduleMasterDto genericScheduleMasterDto)throws ApplicationException;
	
	public Response activeInactiveGenericSchedule(Integer orgId, Integer genericScheduleId,
			Character status)throws ApplicationException;
	
	public Response listGenericScheduleMaster(Integer orgId, Integer unitId,
			Integer offset, Integer recordPerPage)throws ApplicationException;
	
	public Response getTotalGenericScheduleMasterRecord(Integer orgId, Integer unitId)
		throws ApplicationException;

	public Response listserviceCode(Integer orgId, Integer unitId)
			throws ApplicationException;

	public Response getServicecodeList(Integer genericScheduleId, Integer orgId,
			Integer unitId)throws ApplicationException;



}

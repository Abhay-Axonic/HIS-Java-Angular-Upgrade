package com.param.service.services;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.service.dto.MPeriodicityServiceConfigDto;

@SuppressWarnings({"rawtypes"})
public interface IMPeriodicityServiceConfigService {
	public Response saveMPeriodicityServiceConfig(MPeriodicityServiceConfigDto dto)throws ServiceException;
	public Response getMPeriodicityList(int orgId, int unitId)throws ServiceException;
	public Response updateMPeriodicityStatusById(int id, char status)throws ServiceException;
	public Response updateMPeriodicityServiceConfig(MPeriodicityServiceConfigDto dto)throws ServiceException;
	public Response searchPatientForPServiceOrder(int visitTypeId, int orgId, int unitId, String name)throws ServiceException;
	public Response validatePServiceConfigAvail(int serviceId, int orgId, int unitId,int visitTypeId)throws ServiceException;
	public Response getRatePServiceConfig(int serviceId, int orgId, int unitId, int visitTypeId,
			int periodicityServiceConfigId)throws ServiceException;
}

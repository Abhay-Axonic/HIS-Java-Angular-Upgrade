package com.param.service.dao;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.service.dto.MPeriodicityServiceConfigDto;
import com.param.service.global.model.MPeriodicityServiceConfig;

import in.param.common.dao.IGenericDao;

@SuppressWarnings({"rawtypes"})
public interface IMPeriodicityServiceConfigDao extends IGenericDao<MPeriodicityServiceConfig, Integer>{
	public Response saveMPeriodicityServiceConfig(MPeriodicityServiceConfigDto dto)throws ServiceException;
	public Response getMPeriodicityList(int orgId, int unitId)throws ServiceException;
	public Response updateMPeriodicityStatusById(int id, char status)throws ServiceException;
	public Response updateMPeriodicityServiceConfig(MPeriodicityServiceConfigDto dto)throws ServiceException;
	public Response searchPatientForPServiceOrder(int visitTypeId, int orgId, int unitId, String name)throws ServiceException;
	public Response validatePServiceConfigAvail(MPeriodicityServiceConfigDto dto)throws ServiceException;
	public boolean checkExistsPServConfigUpdate(int serviceId, int orgId, int unitId, int configId, int visitTypeId)throws ServiceException;
	public Response validatePServiceConfigAvailUpdate(int serviceId, int organisationId, int unitId,
			int visitTypeId, int periodicityServiceConfigId)throws ServiceException;
}

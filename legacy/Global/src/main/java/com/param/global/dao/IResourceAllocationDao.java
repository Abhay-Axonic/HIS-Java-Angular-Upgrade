package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.ResourceAllocationDto;
import com.param.global.model.ResourceAllocation;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IResourceAllocationDao extends IGenericDao<ResourceAllocation, Integer>{

	
	Response getResourceAllocationByName(ResourceAllocationDto resourceAllocationDto)throws ApplicationException;

	Response saveResourceAllocation(ResourceAllocationDto resourceAllocationDto)throws ApplicationException;

	Response getResourceAllocationList(ResourceAllocationDto resourceAllocationDto)throws ApplicationException;
	
	Response getResourceAllocationActiveList(ResourceAllocationDto resourceAllocationDto) throws ApplicationException;

	Response getSingleResourceAllocation(ResourceAllocationDto resourceAllocationDto)throws ApplicationException;

	Response updateResourceAllocation(ResourceAllocationDto resourceAllocationDto) throws ApplicationException;

	Response getResourceAllocationByID(ResourceAllocationDto resourceAllocationDto) throws ApplicationException;

	Response changeResourceAllocationStatus(ResourceAllocationDto resourceAllocationDto) throws ApplicationException;

	Response getResourceAllocationListCount(ResourceAllocationDto resourceAllocationDto)throws ApplicationException;



	

}

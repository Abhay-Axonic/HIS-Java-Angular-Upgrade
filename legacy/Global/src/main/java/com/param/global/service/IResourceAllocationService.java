package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.ResourceAllocationDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IResourceAllocationService {
	@Transactional
	Response saveResourceAllocation(ResourceAllocationDto resourceAllocationDto) throws ApplicationException;

	@Transactional
	Response getResourceAllocationList(ResourceAllocationDto resourceAllocationDto) throws ApplicationException;

	@Transactional
	Response getResourceAllocationActiveList(ResourceAllocationDto resourceAllocationDto) throws ApplicationException;

	@Transactional
	Response getSingleResourceAllocation(ResourceAllocationDto resourceAllocationDto) throws ApplicationException;

	@Transactional
	Response updateResourceAllocation(ResourceAllocationDto resourceAllocationDto) throws ApplicationException;

	@Transactional
	Response changeResourceAllocationStatus(ResourceAllocationDto resourceAllocationDto) throws ApplicationException;

	@Transactional
	Response getResourceAllocationListCount(ResourceAllocationDto resourceAllocationDto)throws ApplicationException;


	



}


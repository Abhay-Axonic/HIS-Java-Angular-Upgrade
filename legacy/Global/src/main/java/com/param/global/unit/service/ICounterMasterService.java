package com.param.global.unit.service;

import javax.transaction.Transactional;

import org.springframework.web.bind.annotation.PathVariable;

import com.param.global.common.Response;
import com.param.global.unit.dto.CounterMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
@Transactional
public interface ICounterMasterService {

	public Response saveCounterMaster(CounterMasterDto counterMasterDto) throws ApplicationException;
	
	public Response updateCounterMaster(CounterMasterDto counterMasterDto) throws ApplicationException;
	
	public Response updateCounterMasterStatus(CounterMasterDto counterMasterDto) throws ApplicationException;
	
	public Response getCounterMasterById(Integer counterId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getCounterMasterList(CounterMasterDto counterMasterDto) throws ApplicationException;
	
	public Response getCounterMasterCount(CounterMasterDto counterMasterDto) throws ApplicationException;
	
	public Response getActiveCounterMaster(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getCountersList(Integer organizationId,Integer unitId) throws ApplicationException;
	
	public Response getCounterMasterByName(CounterMasterDto counterMasterDto) throws ApplicationException;
	
	public Response getCounterMasterByNameNotId(CounterMasterDto counterMasterDto) throws ApplicationException;
}

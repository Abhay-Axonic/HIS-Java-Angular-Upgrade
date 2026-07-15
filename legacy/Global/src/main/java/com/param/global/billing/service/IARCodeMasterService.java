package com.param.global.billing.service;

import javax.transaction.Transactional;

import in.param.exception.ApplicationException;

import com.param.global.billing.dto.ARCodeMasterDto;
import com.param.global.common.Response;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IARCodeMasterService {
	
	public Response saveARCodeMaster(ARCodeMasterDto aRCodeMasterDto) throws ApplicationException;
	
	public Response updateARCodeMaster(ARCodeMasterDto aRCodeMasterDto) throws ApplicationException;
	
	public Response updateARCodeMasterStatus(ARCodeMasterDto aRCodeMasterDto) throws ApplicationException;
	
	public Response getARCodeMasterList(Integer orgId, Integer unitId, Integer offset, Integer noOfRecordsPerPage) throws ApplicationException;
	
	public Response getARCodeMasterById(Integer aRCodeId, Integer orgId, Integer unitId) throws ApplicationException;
	
	public Response getARCodeMasterCount(Integer orgId, Integer unitId) throws ApplicationException;
	
	public Response getActiveARCodeMaster(Integer orgId, Integer unitId) throws ApplicationException;
	
	public Response getARCodeMasterByName(ARCodeMasterDto aRCodeMasterDto) throws ApplicationException;
	
	public Response getARCodeMasterByNameNotId(ARCodeMasterDto aRCodeMasterDto) throws ApplicationException;
}

package com.param.global.billing.dao;

import in.param.exception.ApplicationException;

import com.param.global.billing.dto.APCodeMasterDto;
import com.param.global.common.Response;
@SuppressWarnings({"rawtypes"})

public interface IAPCodeMasterDao {
	
	public Response saveAPCodeMaster(APCodeMasterDto aPCodeMasterDto) throws ApplicationException;
	
	public Response updateAPCodeMaster(APCodeMasterDto aPCodeMasterDto) throws ApplicationException;
	
	public Response updateAPCodeMasterStatus(APCodeMasterDto aPCodeMasterDto) throws ApplicationException;
	
	public Response getAPCodeMasterList(Integer orgId, Integer unitId,  Integer offset, Integer noOfRecordsPerPage) throws ApplicationException;
	
	public Response getAPCodeMasterById(Integer aPCodeId ,Integer orgId, Integer unitId) throws ApplicationException;
	
	public Response getAPCodeMasterCount(Integer orgId, Integer unitId) throws ApplicationException;
	
	public Response getActiveAPCodeMaster(Integer orgId, Integer unitId) throws ApplicationException;
	
	public Response getAPCodeMasterByName(APCodeMasterDto aPCodeMasterDto) throws ApplicationException;
	
	public Response getAPCodeMasterByNameNotId(APCodeMasterDto aPCodeMasterDto) throws ApplicationException;
}

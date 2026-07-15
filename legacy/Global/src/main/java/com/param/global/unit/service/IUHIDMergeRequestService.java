package com.param.global.unit.service;


import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.global.unit.dto.CoversheetAccessMasterDto;
import com.param.global.unit.dto.UHIDMergeDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})

public interface IUHIDMergeRequestService {

	public Response getCabinMasterList(CabinMasterDto cabinMasterDto) throws ApplicationException;//Dummy

	public Response getUserUHIDMergeList(UHIDMergeDto uHIDMergeDto) throws ApplicationException;

	public Response saveUHIDMergeMaster(UHIDMergeDto uHIDMergeDto);
	

}

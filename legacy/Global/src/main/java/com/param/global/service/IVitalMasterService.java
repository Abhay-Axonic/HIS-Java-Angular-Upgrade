package com.param.global.service;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.dto.VitalMasterDto;

public interface IVitalMasterService {

	public Response getVitalMasterList(VitalMasterDto vitalMasterDto)throws ApplicationException;
	public Response getVitalsForNursing(int orgId,int unitId)throws ApplicationException;
	
}

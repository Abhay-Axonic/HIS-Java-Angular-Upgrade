package com.param.lis.global.service;

import in.param.exception.ApplicationException;

import com.param.lis.global.common.Response;
import com.param.lis.global.dto.MicrobiologyStainMasterDto;
import com.param.lis.global.dto.StainigMasterDto;

@SuppressWarnings("rawtypes")
public interface IMicrobiologyStainigMasterService {
	
	public Response addMicrobStainigMaster(MicrobiologyStainMasterDto microbstainigMasterDto)throws ApplicationException;
	public Response updateMicrobStainigMaster(MicrobiologyStainMasterDto microbstainigMasterDto) throws ApplicationException;
	public Response ActivateInactivateMicrobStainigMaster(Integer orgId, Integer  microStainId, Character  organismStatus) throws ApplicationException;
	public Response listMicrobStainigMaster(Integer orgId, Integer offset, Integer recordPerPage) throws ApplicationException;
	public Response getToTalMicrobStainigMaster(Integer orgId) throws ApplicationException;
	public Response getMicrobStainigMasterMasterById(Integer orgId, Integer  microStainId)
			throws ApplicationException;
}

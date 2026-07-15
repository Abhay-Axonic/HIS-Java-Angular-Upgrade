package com.param.bloodBank.dao;

import com.param.bloodBank.dto.ReasonOfDiscardMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})

public interface IReasonOfDiscardMasterDao {

public Response saveReasonOfDiscardMaster(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto) throws ApplicationException;
	
	public Response updateReasonOfDiscardMaster(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto) throws ApplicationException;
	
	public Response updateReasonOfDiscardMasterStatus(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto) throws ApplicationException;
	
	public Response getReasonOfDiscardMasterList(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto) throws ApplicationException;
	
	public Response getReasonOfDiscardMasterById(Integer reasonOfDiscardId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveReasonOfDiscardMaster(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getReasonOfDiscardMasterCount(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto) throws ApplicationException;
	
	public Response getReasonOfDiscardMasterByName(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto) throws ApplicationException;
	
	public Response getReasonOfDiscardMasterByNameNotId(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto) throws ApplicationException;

	public Response getAllReasonOfDiscardMasterList(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto) throws ApplicationException;
}

package com.param.bloodBank.service;

import javax.transaction.Transactional;

import com.param.bloodBank.dto.DonorRejectionMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IDonorRejectionMasterService {
	
	public Response saveDonorRejectionMaster(DonorRejectionMasterDto reasonOfDiscardMasterDto) throws ApplicationException;
	
	public Response updateDonorRejectionMaster(DonorRejectionMasterDto reasonOfDiscardMasterDto) throws ApplicationException;
	
	public Response updateDonorRejectionMasterStatus(DonorRejectionMasterDto reasonOfDiscardMasterDto) throws ApplicationException;
	
	public Response getDonorRejectionMasterList(DonorRejectionMasterDto reasonOfDiscardMasterDto) throws ApplicationException;
	
	public Response getDonorRejectionMasterById(Integer reasonOfDiscardId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveDonorRejectionMaster(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getDonorRejectionMasterCount(DonorRejectionMasterDto reasonOfDiscardMasterDto) throws ApplicationException;
	
	public Response getDonorRejectionMasterByName(DonorRejectionMasterDto reasonOfDiscardMasterDto) throws ApplicationException;
	
	public Response getDonorRejectionMasterByNameNotId(DonorRejectionMasterDto reasonOfDiscardMasterDto) throws ApplicationException;
}

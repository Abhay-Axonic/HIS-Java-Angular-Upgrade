package com.param.bloodBank.service;

import javax.transaction.Transactional;

import com.param.bloodBank.dto.BloodDonorTypeMasterDto;
import com.param.global.common.Response;
import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})

public interface IBloodDonorTypeMasterService {
	
	public Response saveBloodDonorTypeMaster(BloodDonorTypeMasterDto bloodDonorTypeMasterDto) throws ApplicationException;
	
	public Response updateBloodDonorTypeMaster(BloodDonorTypeMasterDto bloodDonorTypeMasterDto) throws ApplicationException;
	
	public Response updateBloodDonorTypeMasterStatus(BloodDonorTypeMasterDto bloodDonorTypeMasterDto) throws ApplicationException;
	
	public Response getBloodDonorTypeMasterList(BloodDonorTypeMasterDto bloodDonorTypeMasterDto) throws ApplicationException;
	
	public Response getBloodDonorTypeMasterById(Integer bloodDonorTypeId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveBloodDonorTypeMaster(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getBloodDonorTypeMasterCount(BloodDonorTypeMasterDto bloodDonorTypeMasterDto) throws ApplicationException;
	
	public Response getBloodDonorTypeMasterByName(BloodDonorTypeMasterDto bloodDonorTypeMasterDto) throws ApplicationException;
	
	public Response getBloodDonorTypeMasterByNameNotId(BloodDonorTypeMasterDto bloodDonorTypeMasterDto) throws ApplicationException;
}

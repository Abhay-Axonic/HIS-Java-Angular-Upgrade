package com.param.bloodBank.dao;

import com.param.bloodBank.dto.BloodDonorTypeMasterDto;
import com.param.global.common.Response;
import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})

public interface IBloodDonorTypeMasterDao {
	
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

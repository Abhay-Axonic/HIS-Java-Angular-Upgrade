package com.param.bloodBank.dao;

import java.util.List;

import com.param.bloodBank.dto.BloodGroupMatchingMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IBloodGroupMatchingMasterDao {
	
	public Response saveBloodGroupMatchingMaster(List<BloodGroupMatchingMasterDto> bloodBagMatchingMasterDto)throws ApplicationException;

	public Response getBloodGroupMatchingMasterById(Integer componentId, Integer organizationId, Integer unitId)throws ApplicationException;

	public Response getBloodGroupsByComponentAndDonorBloodGroupId(BloodGroupMatchingMasterDto bloodGroupMatchingMasterDto) throws ApplicationException;

}

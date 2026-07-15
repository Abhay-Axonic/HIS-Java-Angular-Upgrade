package com.param.bloodBank.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.bloodBank.dto.BloodGroupMatchingMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes"})
@Transactional
public interface IBloodGroupMatchingMasterService {
	
		public Response saveBloodGroupMatchingMaster(List<BloodGroupMatchingMasterDto> bloodGroupMatchingMasterDto)throws ApplicationException;

		public Response getBloodGroupMatchingMasterById(Integer componentId, Integer organizationId, Integer unitId)throws ApplicationException;

		public Response getBloodGroupsByComponentAndDonorBloodGroupId(BloodGroupMatchingMasterDto bloodGroupMatchingMasterDto) throws ApplicationException;

	

}

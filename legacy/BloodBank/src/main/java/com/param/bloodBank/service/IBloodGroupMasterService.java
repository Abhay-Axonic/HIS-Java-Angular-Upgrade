package com.param.bloodBank.service;

import javax.transaction.Transactional;

import com.param.bloodBank.dto.BloodGroupMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IBloodGroupMasterService {
	public Response saveBloodGroupMaster(BloodGroupMasterDto bloodGroupMasterDto) throws ApplicationException;
	
	public Response updateBloodGroupMaster(BloodGroupMasterDto bloodGroupMasterDto) throws ApplicationException;
	
	public Response updateBloodGroupMasterStatus(BloodGroupMasterDto bloodGroupMasterDto) throws ApplicationException;
	
	public Response getBloodGroupMasterList(BloodGroupMasterDto bloodGroupMasterDto) throws ApplicationException;
	
	public Response getBloodGroupMasterById(Integer bloodGroupId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveBloodGroupMaster(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getBloodGroupMasterCount(BloodGroupMasterDto bloodGroupMasterDto) throws ApplicationException;
	
	
}

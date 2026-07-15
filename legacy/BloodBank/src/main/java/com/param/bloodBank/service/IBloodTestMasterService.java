package com.param.bloodBank.service;

import javax.transaction.Transactional;

import com.param.bloodBank.dto.BloodTestMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IBloodTestMasterService {
	
	public Response saveBloodTestMaster(BloodTestMasterDto bloodTestMasterDto) throws ApplicationException;
	
	public Response updateBloodTestMaster(BloodTestMasterDto bloodTestMasterDto) throws ApplicationException;
	
	public Response updateBloodTestMasterStatus(BloodTestMasterDto bloodTestMasterDto) throws ApplicationException;
	
	public Response getBloodTestMasterList(BloodTestMasterDto bloodTestMasterDto) throws ApplicationException;
	
	public Response getBloodTestMasterById(Integer bloodTestId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveBloodTestMaster(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getBloodTestMasterCount(BloodTestMasterDto bloodTestMasterDto) throws ApplicationException;
	
	public Response getBloodTestMasterByName(BloodTestMasterDto bloodTestMasterDto) throws ApplicationException;
	
	public Response getBloodTestMasterByNameNotId(BloodTestMasterDto bloodTestMasterDto) throws ApplicationException;
}

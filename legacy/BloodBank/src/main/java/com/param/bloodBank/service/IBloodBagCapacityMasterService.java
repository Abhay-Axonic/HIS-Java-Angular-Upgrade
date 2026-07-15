package com.param.bloodBank.service;

import javax.transaction.Transactional;

import com.param.bloodBank.dto.BloodBagCapacityMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IBloodBagCapacityMasterService {
	public Response saveBloodBagCapacityMaster(BloodBagCapacityMasterDto bloodBagCapacityMasterDto) throws ApplicationException;
	
	public Response updateBloodBagCapacityMaster(BloodBagCapacityMasterDto bloodBagCapacityMasterDto) throws ApplicationException;
	
	public Response updateBloodBagCapacityMasterStatus(BloodBagCapacityMasterDto bloodBagCapacityMasterDto) throws ApplicationException;
	
	public Response getBloodBagCapacityMasterList(BloodBagCapacityMasterDto bloodBagCapacityMasterDto) throws ApplicationException;
	
	public Response getBloodBagCapacityMasterById(Integer bloodBagCapacityId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveBloodBagCapacityMaster(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getBloodBagCapacityMasterCount(BloodBagCapacityMasterDto bloodBagCapacityMasterDto) throws ApplicationException;
	
	public Response getBloodBagCapacityMasterByName(BloodBagCapacityMasterDto bloodBagCapacityMasterDto) throws ApplicationException;
	
	public Response getBloodBagCapacityMasterByNameNotId(BloodBagCapacityMasterDto bloodBagCapacityMasterDto) throws ApplicationException;
}

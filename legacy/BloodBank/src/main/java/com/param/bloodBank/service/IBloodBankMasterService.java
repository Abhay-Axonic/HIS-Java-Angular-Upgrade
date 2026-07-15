package com.param.bloodBank.service;

import javax.transaction.Transactional;

import com.param.bloodBank.dto.BloodBankMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IBloodBankMasterService {
	
	public Response saveBloodBankMaster(BloodBankMasterDto bloodBankMasterDto) throws ApplicationException;
	
	public Response updateBloodBankMaster(BloodBankMasterDto bloodBankMasterDto) throws ApplicationException;
	
	public Response updateBloodBankMasterStatus(BloodBankMasterDto bloodBankMasterDto) throws ApplicationException;
	
	public Response getBloodBankMasterList(BloodBankMasterDto bloodBankMasterDto) throws ApplicationException;
	
	public Response getBloodBankMasterById(Integer bloodBankId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveBloodBankMaster(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getBloodBankMasterCount(BloodBankMasterDto bloodBankMasterDto) throws ApplicationException;
	
	public Response getBloodBankMasterByName(BloodBankMasterDto bloodBankMasterDto) throws ApplicationException;
	
	public Response getBloodBankMasterByNameNotId(BloodBankMasterDto bloodBankMasterDto) throws ApplicationException;
}
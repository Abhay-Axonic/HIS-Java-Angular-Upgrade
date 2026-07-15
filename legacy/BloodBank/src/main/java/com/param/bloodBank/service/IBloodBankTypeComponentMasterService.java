package com.param.bloodBank.service;

import javax.transaction.Transactional;

import in.param.exception.ApplicationException;

import com.param.bloodBank.dto.BloodBankTypeComponentMasterDto;
import com.param.global.common.Response;

@SuppressWarnings({"rawtypes"})
@Transactional
public interface IBloodBankTypeComponentMasterService {
	
	public Response saveBloodBankTypeComponentMaster(BloodBankTypeComponentMasterDto bloodBankTypeComponentMasterDto) throws ApplicationException;
	
	public Response updateBloodBankTypeComponentMaster(BloodBankTypeComponentMasterDto bloodBankTypeComponentMasterDto) throws ApplicationException;
	
	public Response updateBloodBankTypeComponentMasterStatus(Integer bloodBagTypeId, Integer organizationId, Character status, Integer unitId) throws ApplicationException;
	
	public Response getBloodBankTypeComponentMasterList(Integer offset, Integer noOfRecordsPerPage) throws ApplicationException;
	
	public Response getBloodBankTypeComponentMasterById(Integer bloodBagTypeId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveBloodBankTypeComponentMaster(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getBloodBankTypeComponentMasterCount(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response checkActiveBloodBankTypeComponentMasterAvaiable(BloodBankTypeComponentMasterDto bloodBankTypeComponentMasterDto)throws ApplicationException;
	
	public Response getBloodBagList(Integer organizationId, Integer unitId)throws ApplicationException;
	
	public Response getBloodComponentList(Integer organizationId, Integer unitId)throws ApplicationException;

	public Response getDetailedComponentList(Integer bloodBagTypeId, Integer organizationId, Integer unitId) throws ApplicationException;

	
}

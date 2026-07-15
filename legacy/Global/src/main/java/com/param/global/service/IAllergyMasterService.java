package com.param.global.service;

import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.AllergyMasterDto;
@SuppressWarnings("rawtypes")
@Transactional
public interface IAllergyMasterService {

	
	public Response getListOfAllergyMaster(AllergyMasterDto allergyMasterDto)throws ApplicationException;

	public Response saveAllergyMaster(AllergyMasterDto allergyMasterDto) throws ApplicationException;

	public Response updateAllergyMaster(AllergyMasterDto allergyMasterDto) throws ApplicationException;

	public Response updateAllergyMasterStatus(AllergyMasterDto allergyMasterDto) throws ApplicationException; 

	public Response getAllergyMasterList(AllergyMasterDto allergyMasterDto) throws ApplicationException;

	public Response getAllergyMasterListCount(AllergyMasterDto allergyMasterDto) throws ApplicationException;

	public Response getAllergyMasterListById(Integer allergyId, int orgId, int unitId) throws ApplicationException;
}

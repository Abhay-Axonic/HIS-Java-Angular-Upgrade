package com.param.global.service;

import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.AllergyTypeMasterDto;
@SuppressWarnings("rawtypes")
@Transactional
public interface IAllergyTypeMasterService {
	
	public Response getListOfAllergyTypeMaster(AllergyTypeMasterDto allergyTypeMasterDto) throws ApplicationException;

	public Response saveAllergyTypeMaster(AllergyTypeMasterDto allergyTypeMasterDto) throws ApplicationException;

	public Response updateAllergyTypeMaster(AllergyTypeMasterDto allergyTypeMasterDto) throws ApplicationException;

	public Response updateAllergyTypeMasterStatus(AllergyTypeMasterDto allergyTypeMasterDto) throws ApplicationException;

	public Response getAllergyTypeMasterList(AllergyTypeMasterDto allergyTypeMasterDto) throws ApplicationException;

	public Response getAllergyTypeMasterListCount(AllergyTypeMasterDto allergyTypeMasterDto) throws ApplicationException;

	public Response getAllergyTypeMasterListById(Integer allergyTypeId, int orgId, int unitId)throws ApplicationException;
}

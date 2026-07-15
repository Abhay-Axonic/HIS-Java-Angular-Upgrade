package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.AllergyTypeMasterDto;
import com.param.global.model.AllergyTypeMaster;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IAllergyTypeMasterDao extends IGenericDao<AllergyTypeMaster, Integer>{

	
	public Response getListOfAllergyTypeMaster(AllergyTypeMasterDto allergyTypeMasterDto)throws ApplicationException;
	
	public Response saveAllergyTypeMaster(AllergyTypeMasterDto allergyTypeMasterDto) throws ApplicationException;

	public Response updateAllergyTypeMaster(AllergyTypeMasterDto allergyTypeMasterDto) throws ApplicationException;

	public Response updateAllergyTypeMasterStatus(AllergyTypeMasterDto allergyTypeMasterDto) throws ApplicationException;

	public Response getAllergyTypeMasterList(AllergyTypeMasterDto allergyTypeMasterDto) throws ApplicationException;

	public Response getAllergyTypeMasterListCount(AllergyTypeMasterDto allergyTypeMasterDto) throws ApplicationException;

	public Response getAllergyTypeMasterListById(Integer allergyTypeId, int orgId, int unitId)throws ApplicationException;
	
	public Response getAllergyTypeMasterListByName(AllergyTypeMasterDto allergyTypeMasterDto) throws ApplicationException;
	
	public Response getAllergyTypeMasterListByNameNotById(AllergyTypeMasterDto allergyTypeMasterDto) throws ApplicationException;

	public Response getAllergyTypeMasterCheckIsDrugById(AllergyTypeMasterDto allergyTypeMasterDto)throws ApplicationException;

	public Response getAllergyTypeMasterCheckIsDrug(AllergyTypeMasterDto allergyTypeMasterDto)throws ApplicationException;
}

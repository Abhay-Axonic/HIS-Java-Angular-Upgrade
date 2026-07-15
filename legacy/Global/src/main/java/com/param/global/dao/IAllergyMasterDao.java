package com.param.global.dao;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.dto.AllergyMasterDto;
import com.param.global.model.AllergyMaster;
@SuppressWarnings("rawtypes")
public interface IAllergyMasterDao extends IGenericDao<AllergyMaster, Integer>{
	
	
	public Response getListOfAllergyMaster(AllergyMasterDto allergyMasterDto)throws ApplicationException;
	
	public Response saveAllergyMaster(AllergyMasterDto allergyMasterDto) throws ApplicationException;

	public Response updateAllergyMaster(AllergyMasterDto allergyMasterDto) throws ApplicationException;

	public Response updateAllergyMasterStatus(AllergyMasterDto allergyMasterDto) throws ApplicationException; 

	public Response getAllergyMasterList(AllergyMasterDto allergyMasterDto) throws ApplicationException;

	public Response getAllergyMasterListCount(AllergyMasterDto allergyMasterDto) throws ApplicationException;

	public Response getAllergyMasterListById(Integer allergyId, int orgId, int unitId) throws ApplicationException;
	
	public Response getAllergyMasterListByName(AllergyMasterDto allergyMasterDto) throws ApplicationException;
	
	public Response getAllergyMasterListByNameNotById(AllergyMasterDto allergyMasterDto) throws ApplicationException;
}

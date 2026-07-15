package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.CustomHistoryConfigurationDetailsDto;
import com.param.global.dto.CustomHistoryConfigurationDto;
import com.param.global.dto.SpecialityMasterDto;
import com.param.global.model.SpecialityMaster;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface ISpecialityMasterDao extends IGenericDao<SpecialityMaster, Integer>
{
	
	public Response getSpecialityMasterList(SpecialityMasterDto SpecialityMasterDto)throws ApplicationException;
	
	public Response addSpecialityMaster(SpecialityMasterDto SpecialityMasterDto)throws ApplicationException;
	
	public Response editSpecialityMaster(SpecialityMasterDto SpecialityMasterDto)throws ApplicationException;
	
	public Response updateSpecialityMaster(SpecialityMasterDto SpecialityMasterDto)throws ApplicationException;
	
	public Response getSpecialityByNameNotId(SpecialityMasterDto SpecialityMasterDto)throws ApplicationException;	
	
	public Response getActiveSpecialityList(SpecialityMasterDto SpecialityMasterDto)throws ApplicationException;

	public Response getSpecialityById(SpecialityMasterDto specialityMasterDto) throws ApplicationException;

	public Response updateSpecialityStatus(SpecialityMasterDto specialityMasterDto);

	public Response getSpecialityByName(SpecialityMasterDto SpecialityMasterDto) throws ApplicationException;

	public Response getActiveSpecialityMasterList()throws ApplicationException;

	public Response getSpecialityListForSubSpeciality(int orgId) throws ApplicationException;

	public Response getCount(SpecialityMasterDto SpecialityMasterDto) throws ApplicationException;

	public Response getOrgActiveSpecialityList(SpecialityMasterDto specialityMasterDto) throws ApplicationException;

	public Response getSpecialityNameById(SpecialityMasterDto specialityMasterDto) throws ApplicationException;

	public Response getStoreBySpecialtyId(SpecialityMasterDto specialityMasterDto)throws ApplicationException;

	public Response getSpecialityListOfActiveInactive(SpecialityMasterDto specialityMasterDto)throws ApplicationException;

	public Response departmentMasterUniversalSearch(SpecialityMasterDto specialityMasterDto)throws ApplicationException;

	public Response getSpecialityCheckWithUnitMapper(SpecialityMasterDto specialityMasterDto) throws ApplicationException;

	public Response getActiveSpecialityListForModality(SpecialityMasterDto specialityMasterDto)throws ApplicationException;
}

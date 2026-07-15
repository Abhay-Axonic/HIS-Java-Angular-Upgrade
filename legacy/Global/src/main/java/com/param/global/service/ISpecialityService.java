package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.CustomHistoryConfigurationDetailsDto;
import com.param.global.dto.CustomHistoryConfigurationDto;
import com.param.global.dto.SpecialityMasterDto;
import com.param.global.dto.SubSpecialityMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface ISpecialityService 
{

	//public Response getSpecialityByUnitIdAndDoctorId(DoctorUnitSpecialityMapperDto doctorUnitSpecialityMapperDto)throws ApplicationException;
	//public Response getApiSpecialityListByDoctor(DoctorUnitSpecialityMapperDto doctorUnitSpecialityMapperDto)throws ApplicationException;
	
	public Response getSpecialityMasterList(SpecialityMasterDto specialityMasterDto)throws ApplicationException;
	
	public Response addSpecialityMaster(SpecialityMasterDto SpecialityMasterDto)throws ApplicationException;
	
	public Response updateSpecialityMaster(SpecialityMasterDto SpecialityMasterDto)throws ApplicationException;
	
	public Response getSpecialityByName(SpecialityMasterDto SpecialityMasterDto)throws ApplicationException;
	
	public Response getSpecialityById(SpecialityMasterDto specialityMasterDto)throws ApplicationException;

	public Response updateStatus(SpecialityMasterDto specialityMasterDto)throws ApplicationException;

	public Response getActiveSpecialityMasterList(SpecialityMasterDto specialityMasterDto)throws ApplicationException;

	public Response getSpecialityListForSubSpeciality(int orgId) throws ApplicationException;

	public Response getSpecialityCount(SpecialityMasterDto specialityMasterDto)throws ApplicationException;

	public Response getOrgActiveSpecialityList(SpecialityMasterDto specialityMasterDto) throws ApplicationException;

	public Response getSpecialityNameById(SpecialityMasterDto specialityMasterDto) throws ApplicationException;
	
	@Transactional
	public Response getStoreBySpecialtyId(SpecialityMasterDto specialityMasterDto) throws ApplicationException;
	@Transactional
	public Response getSpecialityListOfActiveInactive(SpecialityMasterDto specialityMasterDto)throws ApplicationException;

	public Response departmentMasterUniversalSearch(SpecialityMasterDto specialityMasterDto) throws ApplicationException;

	public Response getActiveSpecialityListForModality(SpecialityMasterDto specialityMasterDto) throws ApplicationException;

	public void invalidateSpecialityMasterCache(Integer unitId) throws ApplicationException;
}

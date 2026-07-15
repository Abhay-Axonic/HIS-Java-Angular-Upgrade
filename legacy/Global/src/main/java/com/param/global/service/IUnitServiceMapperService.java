package com.param.global.service;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.AreaMasterDto;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.dto.ServiceSearchReqDto;
import com.param.global.dto.UnitServiceMapperDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IUnitServiceMapperService {
	public Response saveUnitServiceMapper(UnitServiceMapperDto unitServiceMapperDto)throws ApplicationException;
	public Response searchServiceMasterBySpecialitySubSplecialityUnitAndOrg(String searchKeyword, Integer specialityId, Integer subSpecialityId, Integer unitId, Integer orgId)throws ApplicationException;
	public Response getMappedServicesListByUnit(UnitServiceMapperDto unitServiceMapperDto)throws ApplicationException;
	public Response getCountOfMappedServicesListByUnit(UnitServiceMapperDto unitServiceMapperDto)throws ApplicationException;
	public Response updateUnitServiceMapperStatus(UnitServiceMapperDto unitServiceMapperDto)throws ApplicationException;
	public Response searchServicesByNameAndCode(ServiceMasterDto serviceMasterDto)throws ApplicationException;
	public Response searchUnitServiceByMultipleSpecialityAndSubSpeciality(UnitServiceMapperDto unitServiceMapperDto)throws ApplicationException;
	public Response searchActiveServices(ServiceSearchReqDto serviceSearchReqDto)throws ApplicationException;
	
	public Response searchProcedure(int orgId,int unitId,int specialityId, String searchKeyWord)throws ApplicationException;
	public Response searchUnitServiceMapperWithoutSpecialitySubSpeciality(String searchKeyword, Integer unitId,
			Integer orgId)throws ApplicationException;
	public Response searchServiceMasterBySpecialitySubSplecialityOrgUnitAndGender(String searchKeyword,
			Integer specialityId, Integer subSplecialityId, Integer unitId, Integer orgId, Integer gender)throws ApplicationException;
	public Response getMappedServicesListByUnitSearch(UnitServiceMapperDto unitServiceMapperDto)throws ApplicationException;
	
	
	public Response checkServiceMappedWithTest(ServiceMasterDto dto,Integer labSpecialityId)throws ApplicationException;
	public Response searchItemByNameAndCode(ServiceMasterDto serviceMasterDto)throws ApplicationException;
	public Response searchServiceByNameOrCode(UnitServiceMapperDto unitServiceMapperDto)throws ApplicationException;
	public Response serviceMasterForUnitApplicationService(ServiceMasterDto serviceMasterDto)throws ApplicationException;
	public Response serviceAreaBysearchKeyword(AreaMasterDto areaMasterDto)throws ApplicationException;
	public Response checkServiceWithTestParameter(ServiceMasterDto dto)throws ApplicationException;


	Response searchServicesByNameAndCodeForEasyEMR(ServiceMasterDto serviceMasterDto) throws ApplicationException;
}


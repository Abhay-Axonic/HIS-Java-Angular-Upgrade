package com.param.global.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.*;
import com.param.global.model.UnitServiceMapper;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IUnitServiceMapperDao extends IGenericDao<UnitServiceMapper,Integer>{
	public Response saveUnitServiceMapper(UnitServiceMapperDto unitServiceMapperDto)throws ApplicationException;
	public Response searchServiceMasterBySpecialitySubSplecialityUnitAndOrg(String searchKeyword, Integer specialityId, Integer subSpecialityId, Integer unitId, Integer orgId)throws ApplicationException;
	public Response getMappedServicesListByUnit(UnitServiceMapperDto unitServiceMapperDto)throws ApplicationException;
	public Response getCountOfMappedServicesListByUnit(UnitServiceMapperDto unitServiceMapperDto)throws ApplicationException;
	public Response updateUnitServiceMapperStatus(UnitServiceMapperDto unitServiceMapperDto)throws ApplicationException;
	public Response searchServicesByNameAndCode(ServiceMasterDto serviceMasterDto)throws ApplicationException;
	public Response searchUnitServiceByMultipleSpecialityAndSubSpeciality(UnitServiceMapperDto unitServiceMapperDto)throws ApplicationException;
	public Response searchActiveServices(ServiceSearchReqDto serviceSearchReqDto)throws ApplicationException;
	public Response getServiceByIdForPackageValid(int serviceId,Integer unitId)throws ApplicationException;
	public void saveUnitServiceBasePrice(UnitServiceMapperDto mapperDto)throws ApplicationException;
	public Response searchProcedure(int orgId,int unitId, int specialityId,String searchKeyWord)throws ApplicationException;
	public Response searchServiceMasterByWithoutSpecialitySubSpeciality(String searchKeyword, Integer unitId,
			Integer orgId)throws ApplicationException;
	public Response searchServiceMasterBySpecialitySubSplecialityOrgUnitAndGender(String searchKeyword,
			Integer specialityId, Integer subSplecialityId, Integer unitId, Integer orgId, Integer gender)throws ApplicationException;
	public Response getMappedServicesListByUnitSearch(UnitServiceMapperDto unitServiceMapperDto)throws ApplicationException;
	
	public Response checkServiceMappedWithTest(ServiceMasterDto dto)throws ApplicationException;
	public Response searchItemByNameAndCode(ServiceMasterDto serviceMasterDto)throws ApplicationException;
	public Response searchServiceByNameOrCode(UnitServiceMapperDto unitServiceMapperDto)throws ApplicationException;
	public Response serviceMasterForUnitApplicationService(ServiceMasterDto serviceMasterDto)throws ApplicationException;
	public Response serviceAreaBysearchKeyword(AreaMasterDto areaMasterDto)throws ApplicationException;
	public Response getServiceListBySubSpecialityIdNotByTariff(List<Integer> serviceIds, Integer unitId)throws ApplicationException;
	public Response saveUnitServiceMapperfromUnit(UnitServiceMapperDto mapperDto)throws ApplicationException;
	public Response checkServiceWithTestParameter(ServiceMasterDto dto)throws ApplicationException;

	Response searchServicesByNameAndCodeForEasyEMR(ServiceMasterDto serviceMasterDto) throws ApplicationException;

}

package com.param.global.dao;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.global.common.Response;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.model.ServiceMaster;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IServiceMasterDao extends IGenericDao<ServiceMaster, Integer>{
	public Response saveOrganizationServiceMaster(ServiceMasterDto xServiceMasterDto)throws ApplicationException;
	public Response changeOrganizationServiceMasterStatus(ServiceMasterDto xServiceMasterDto)throws ApplicationException;
	public Response listOrganizationServiceMasterMaster(Integer orgId, Integer offset, Integer recordPerPage) throws ApplicationException;
	public Response getOrganizationServiceMasterTotalCount(Integer orgId)throws ApplicationException;
	public Response getOrganizationServiceMasterById(ServiceMasterDto xServiceMasterDto)throws ApplicationException;
	public Response updateOrganizationServiceMaster(ServiceMasterDto xServiceMasterDto)throws ApplicationException;
	public Response getOrganizationServiceMasterBySpecialityAndSubSpeciality(ServiceMasterDto xServiceMasterDto)throws ApplicationException;

	public Response listServiceMasterWithBasePriceByVisitType(Integer orgId, Integer unitId, Integer visitTypeId, Integer offset, Integer recordPerPage) throws ApplicationException;
	public Response listServiceMasterByServiceId(Integer orgId, Integer unitId, List<Integer> listServiceId) throws ApplicationException;
	public Response getServiceDetailsByServiceId(ServiceMasterDto serviceMasterDto)throws ApplicationException;
	public Response getConsultationService(Integer orgId, Integer unitId, Integer splId, Integer subSplId)throws ApplicationException;
	public Response getAutoCompleteServicesLab(int orgId,int orgUnitId,int subDeptId,String serviceName)throws ApplicationException;

	public Response getListOfServiceMaster(Integer orgId, Integer unitId)throws ApplicationException;


	public Response readCSVFile(List<ServiceMasterDto> services)throws ApplicationException;
	public Response getServiceDetailsById(ServiceMasterDto serviceMasterDto)throws ApplicationException;
	public Response getSurchargeServiceDetails(ServiceMasterDto serviceMasterDto)throws ApplicationException;
	public Response getCancelServices(Integer admissionId, Integer visitTypeId, Integer orgId)throws ApplicationException;
	public Response getAutoRenderedServiceDetails(Integer orgId, Integer unitId)throws ApplicationException;
	public Response getConsultationDeptByServiceId(Integer serviceId, Integer unitId)throws ApplicationException;
	public Response serviceMasterUniversalSearch(ServiceMasterDto serviceMasterDto)throws ApplicationException;
	public CommonResponse checkForUniqueServiceCode(ServiceMasterDto xServiceMasterDto)throws ApplicationException;
	public CommonResponse checkForUniqueServiceName(ServiceMasterDto xServiceMasterDto)throws ApplicationException;
	Response getAllServicesByOrgId(Integer orgId) throws ApplicationException;
	public Response listOfServicesForWebAppIntegration(Integer orgId, Integer unitId)throws ApplicationException;
	Response getOutSourcedService(Integer unitId,String servcieName) throws ApplicationException;

}

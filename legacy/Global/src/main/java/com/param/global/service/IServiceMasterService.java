package com.param.global.service;


import javax.transaction.Transactional;

import com.param.common.dto.CommonResponse;
import com.param.global.common.Response;
import com.param.global.dto.ServiceMasterDto;
import com.param.module.service.ServiceItemMapperDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
@Transactional
public interface IServiceMasterService {
	public CommonResponse saveOrganizationServiceMaster(ServiceMasterDto xServiceMasterDto)throws ApplicationException;
	public Response changeOrganizationServiceMasterStatus(ServiceMasterDto xServiceMasterDto)throws ApplicationException;
	public Response listOrganizationServiceMasterMaster(Integer orgId, Integer offset, Integer recordPerPage) throws ApplicationException;
	public Response getOrganizationServiceMasterTotalCount(Integer orgId)throws ApplicationException;
	public Response getOrganizationServiceMasterById(ServiceMasterDto xServiceMasterDto)throws ApplicationException;
	public CommonResponse updateOrganizationServiceMaster(ServiceMasterDto xServiceMasterDto)throws ApplicationException;
	public Response getOrganizationServiceMasterBySpecialityAndSubSpeciality(ServiceMasterDto xServiceMasterDto)throws ApplicationException;
	public Response getServiceDetailsByServiceId(ServiceMasterDto serviceMasterDto)throws ApplicationException;
	public Response getConsultationService(Integer orgId, Integer unitId, Integer splId, Integer subSplId)throws ApplicationException;
	public Response getListOfServiceMaster(Integer orgId, Integer unitId)throws ApplicationException;

	public Response getAutoCompleteServicesLab(int orgId,int orgUnitId,int subDeptId,String serviceName)throws ApplicationException;
	public Response readCSVFile(ServiceMasterDto serviceMasterDto)throws ApplicationException;
	public Response getSurchargeServiceDetails(ServiceMasterDto serviceMasterDto)throws ApplicationException;
	public Response getCancelServices(Integer admissionId, Integer visitTypeId, Integer orgId)throws ApplicationException;
	public Response getAutoRenderedServiceDetails(Integer orgId, Integer unitId)throws ApplicationException;
	public Response getConsultationDeptByServiceId(Integer serviceId, Integer unitId)throws ApplicationException;
	public Response serviceMasterUniversalSearch(ServiceMasterDto serviceMasterDto)throws ApplicationException;
	public Response getServiceItemMapperDetails(ServiceItemMapperDto serviceItemMapperDto)throws ApplicationException;
	public Response getServiceItemMapperDetailsByServiceId(ServiceItemMapperDto serviceItemMapperDto)throws ApplicationException;
	public Response getBatchDetailsByItemList(ServiceItemMapperDto serviceItemMapperDto)throws ApplicationException;
	Response getAllServicesByOrgId(Integer orgId) throws ApplicationException;
	public Response listOfServicesForWebAppIntegration(Integer orgId, Integer unitId)throws ApplicationException;
	Response getOutsourcedServices(Integer unitId,String servcieName) throws ApplicationException;
}

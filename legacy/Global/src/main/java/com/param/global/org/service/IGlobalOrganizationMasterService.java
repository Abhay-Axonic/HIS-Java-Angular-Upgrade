package com.param.global.org.service;

import in.param.exception.ApplicationException;
import org.springframework.web.multipart.MultipartFile;

import com.param.common.dto.CommonResponse;
import com.param.global.common.Response;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.org.dto.OrganizationUnitLicenceDetailsDto;

@SuppressWarnings("rawtypes")
public interface IGlobalOrganizationMasterService {
	
	public CommonResponse saveOrganizationMaster(OrganizationMasterDto organizationMasterDto)throws ApplicationException;
	public CommonResponse updateOrganizationMaster(OrganizationMasterDto organizationMasterDto)throws ApplicationException;
	public Response getOrganizationById(int organizationId)throws ApplicationException;
	public Response getAllOrganizationMasterList(OrganizationMasterDto organizationMasterDto)throws ApplicationException;
	public Response getOrganizationCount()throws ApplicationException;
	public Response updateOrganizationStatus(OrganizationMasterDto organizationMasterDto)throws ApplicationException;
	public Response getLicenceDetailsByOrgUnitId(OrganizationUnitLicenceDetailsDto organizationUnitLicenceDetailsDto)throws ApplicationException;
	public Response getActiveOrganizationList() throws ApplicationException;
	
	public Response saveOrganizationImage(int organizationId,MultipartFile inputFile)throws ApplicationException;
	public Response updateOrganizationFolderPath(OrganizationMasterDto organizationMasterDto)throws ApplicationException;
	public Response getOrganizationListByOrgName(String orgName)throws ApplicationException;
}

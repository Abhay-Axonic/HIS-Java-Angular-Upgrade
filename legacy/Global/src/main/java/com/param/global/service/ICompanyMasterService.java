package com.param.global.service;


import org.springframework.web.multipart.MultipartFile;

import com.param.global.common.Response;
import com.param.global.dto.CompanyMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")

public interface ICompanyMasterService {
	public Response getActiveCompanyMasterList(CompanyMasterDto companyMasterDto)throws ApplicationException;

	public Response saveCompanyMaster(CompanyMasterDto companyMasterDto) throws ApplicationException;


	public Response getCompanyMasterListById(Integer unitId, Integer orgId, Integer companyId) throws ApplicationException;

	public Response updateCompanyMaster(CompanyMasterDto companyMasterDto) throws ApplicationException;

	public Response countOfCompanyMasterList(CompanyMasterDto companyMasterDto) throws ApplicationException;

	public Response updateCompanyMasterStatus(CompanyMasterDto companyMasterDto) throws ApplicationException;
	
	public Response getCompanyMasterListByName(CompanyMasterDto companyMasterDto) throws ApplicationException;
	
	public Response getCompanyMasterListByNameNotById(CompanyMasterDto companyMasterDto) throws ApplicationException;
	
	public Response getCompanyContractGradeByCompanyId(CompanyMasterDto companyMasterDto) throws ApplicationException;

	public Response getCompanyMasterList(CompanyMasterDto companyMasterDto) throws ApplicationException;

	public Response getActiveTPA(CompanyMasterDto companyMasterDto) throws ApplicationException;

	public Response saveCompanyImage(int orgId, int unitId, int companyId, MultipartFile inputFile)throws ApplicationException;
	
}

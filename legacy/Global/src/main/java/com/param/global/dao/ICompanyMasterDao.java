package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.CompanyMasterDto;
import com.param.global.model.CompanyMaster;
import com.param.service.global.model.CompanyContarctGradeMapper;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface ICompanyMasterDao extends IGenericDao<CompanyMaster, Integer>{
	
	public Response getActiveCompanyMasterList(CompanyMasterDto companyMasterDto)throws ApplicationException;

	public Response saveCompanyMaster(CompanyMasterDto companyMasterDto) throws ApplicationException;


	public Response getCompanyMasterListById(Integer unitId, Integer orgId, Integer companyId) throws ApplicationException;

	public Response updateCompanyMaster(CompanyMasterDto companyMasterDto) throws ApplicationException;

	public Response countOfCompanyMasterList(CompanyMasterDto companyMasterDto) throws ApplicationException;
	
	public Response updateCompanyMasterStatus(CompanyMasterDto companyMasterDto) throws ApplicationException;

	public Response updateStatusOfCompanyContarctGradeMapper(CompanyMasterDto companyMasterDto) throws ApplicationException;

	public Response saveCompanyContarctGradeMapper(CompanyContarctGradeMapper companyContarctGradeMapper) throws ApplicationException;

	public Response getCompanyMasterListByName(CompanyMasterDto companyMasterDto) throws ApplicationException;

	public Response getCompanyMasterListByNameNotById(CompanyMasterDto companyMasterDto) throws ApplicationException;

	public Response getCompanyContractGradeByCompanyId(CompanyMasterDto companyMasterDto)throws ApplicationException;

	public Response getCompanyMasterList(CompanyMasterDto companyMasterDto) throws ApplicationException;

	public Response getActiveTPA(CompanyMasterDto companyMasterDto) throws ApplicationException;

	public Response updateCompanyImage(CompanyMasterDto dto)throws ApplicationException;

	Integer addDefaultEntry(Integer unitId, Integer orgId) throws ApplicationException;

}

package com.param.global.otims.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.otims.dto.SubSequentProcedureConfigDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface ISubSequentProcedureConfigService {

	public Response saveSubSequentProcedureConfig(SubSequentProcedureConfigDto subSequentProcedureConfigDto) throws ApplicationException;
	
	public Response updateSubSequentProcedureConfig(SubSequentProcedureConfigDto subSequentProcedureConfigDto) throws ApplicationException;
	
	public Response updateSubSequentProcedureConfigStatus(SubSequentProcedureConfigDto subSequentProcedureConfigDto) throws ApplicationException;
	
	public Response getSubSequentProcedureConfigList(SubSequentProcedureConfigDto subSequentProcedureConfigDto) throws ApplicationException;
	
	public Response getSubSequentProcedureConfigById(Integer subSequentProcConfigId, Integer orgId, Integer unitId) throws ApplicationException;
	
	public Response getActiveSubSequentProcedureConfigList(SubSequentProcedureConfigDto subSequentProcedureConfigDto) throws ApplicationException;
	
	public Response getSubSequentProcedureConfigCount(SubSequentProcedureConfigDto subSequentProcedureConfigDto) throws ApplicationException;
	
	public Response getSubSequentProcedureConfigByIdNotName(SubSequentProcedureConfigDto subSequentProcedureConfigDto) throws ApplicationException;

	public Response getSubSequentDetailsListById(SubSequentProcedureConfigDto subSequentProcedureConfigDto)throws ApplicationException;

	public Response getSubSequentDetailsListBySpecialityId(SubSequentProcedureConfigDto subSequentProcedureConfigDto)throws ApplicationException;

	public Response getSubSequentDetailsAndInfoListBySpecialityId(
			SubSequentProcedureConfigDto subSequentProcedureConfigDto)throws ApplicationException;
	
	
}

package com.param.global.nabhprocess.dao;

import com.param.global.common.Response;
import com.param.global.nabhprocess.dto.NabhProcessMasterDto;
import com.param.global.nabhprocess.dto.NabhProcessRoleMapperDto;
import com.param.global.nabhprocess.dto.NabhProcessTemplateMapperDto;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface INabhProcessMasterDao {

	public Response saveNabhProcessMaster(NabhProcessMasterDto nabhProcessMasterDto) throws ApplicationException;

	public Response updateNabhProcessMaster(NabhProcessMasterDto nabhProcessMasterDto) throws ApplicationException;
//
//	public Response getNabhProcessMasterById(NabhProcessMasterDto nabhProcessMasterDto) throws ApplicationException;
//
	public Response getNabhProcessMasterList(NabhProcessMasterDto nabhProcessMasterDto) throws ApplicationException;
//
//	public Response getTemplateByNabhProcessId(NabhProcessMasterDto nabhProcessMasterDto) throws ApplicationException;
//
//	public Response getRoleByNabhProcessId(NabhProcessMasterDto nabhProcessMasterDto) throws ApplicationException;
//	
//	public Response checknabhProcessMasterAvailableByName(NabhProcessMasterDto nabhProcessMasterDto) throws ApplicationException;
//	
//	public Response checknabhProcessMasterAvailableByNameNotId(NabhProcessMasterDto nabhProcessMasterDto) throws ApplicationException;
//
//	public Response saveNabhProcessRoleMapper(NabhProcessRoleMapperDto dtoRoleList) throws ApplicationException;
//
//	public Response saveNabhProcessTemplateMapper(NabhProcessTemplateMapperDto dtoTemplateList)throws ApplicationException;
//
//	public Response updateNabhProcessRoleMapper(NabhProcessRoleMapperDto dtoRoleList)throws ApplicationException;
//
//	public Response updateNabhProcessTemplateMapper(NabhProcessTemplateMapperDto dtoTemplateList)throws ApplicationException;
//
	public Response updateNabhProcessMasterStatus(NabhProcessMasterDto nabhProcessMasterDto)throws ApplicationException;

	public Response updateNabhProcessRoleMasterStatus(NabhProcessMasterDto nabhProcessMasterDto)throws ApplicationException;

	public Response updateNabhProcessTemplateMasterStatus(NabhProcessMasterDto nabhProcessMasterDto)throws ApplicationException;
//
	public Response getActiveNabhProcessMasterList(NabhProcessMasterDto nabhProcessMasterDto)throws ApplicationException;
//
//	public Response getNabhProcessMasterCount(NabhProcessMasterDto nabhProcessMasterDto)throws ApplicationException;
//
//	public Response checkNabhProcessTemplateAvailableByTemplateId(NabhProcessMasterDto nabhProcessMasterDto)throws ApplicationException;
//

}

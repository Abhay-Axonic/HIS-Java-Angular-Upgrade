package com.param.global.nabhprocess.service;

import com.param.global.common.Response;
import com.param.global.nabhprocess.dto.NabhProcessMasterDto;
import com.param.global.unit.dto.IncidentReportMasterDto;

import in.param.exception.ApplicationException;

public interface INabhProcessMasterService {

	Response saveNabhProcessMaster(NabhProcessMasterDto nabhProcessMasterDto);
	
//	Response updateNabhProcessMaster(NabhProcessMasterDto nabhProcessMasterDto) throws ApplicationException;;

	public Response getNabhProcessMasterById(NabhProcessMasterDto nabhProcessMasterDto) throws ApplicationException;
//
	public Response getNabhProcessMasterList(NabhProcessMasterDto nabhProcessMasterDto) throws ApplicationException;
//
//	public Response getTemplateByNabhProcessId(NabhProcessMasterDto nabhProcessMasterDto) throws ApplicationException;
//
//	public Response getRoleByNabhProcessId(NabhProcessMasterDto nabhProcessMasterDto) throws ApplicationException;
//
	public Response updateNabhProcessMasterStatus(NabhProcessMasterDto nabhProcessMasterDto) throws ApplicationException;
//
	public Response getActiveNabhProcessMasterList(NabhProcessMasterDto nabhProcessMasterDto) throws ApplicationException;
//
//	public Response getNabhReportMasterCount(NabhProcessMasterDto nabhProcessMasterDto) throws ApplicationException;
	

}

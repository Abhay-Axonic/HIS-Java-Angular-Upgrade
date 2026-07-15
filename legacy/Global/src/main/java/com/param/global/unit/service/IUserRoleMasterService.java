package com.param.global.unit.service;

import javax.transaction.Transactional;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.unit.dto.ButtonMasterDto;
import com.param.global.unit.dto.LetterAttachmentMapperDto;
import com.param.global.unit.dto.UserRoleMasterDto;
@Transactional
@SuppressWarnings({"rawtypes"})
public interface IUserRoleMasterService {
	
	public Response saveUserRoleMaster(UserRoleMasterDto userRoleMasterDto) throws ApplicationException;
	
	public Response updateUserRoleMaster(UserRoleMasterDto userRoleMasterDto) throws ApplicationException;
	
	public Response updateUserRoleMasterStatus(UserRoleMasterDto userRoleMasterDto) throws ApplicationException;
	
	public Response getUserRoleMasterList(UserRoleMasterDto userRoleMasterDto) throws ApplicationException;
	
	public Response getUserRoleMasterById(Integer roleId, Integer organisationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveUserRoleMasterList(UserRoleMasterDto userRoleMasterDto) throws ApplicationException;
	
	public Response getUserRoleMasterCount(UserRoleMasterDto userRoleMasterDto) throws ApplicationException;
	
	public Response getUserRoleMasterByName(UserRoleMasterDto userRoleMasterDto) throws ApplicationException;
	
	public Response getUserRoleMasterByNameNotId(UserRoleMasterDto userRoleMasterDto) throws ApplicationException;

	public Response searchRoleByName(UserRoleMasterDto userRoleMasterDto) throws ApplicationException;

	public Response getAiButtonConfigMaster(ButtonMasterDto buttonMasterDto);

	public Response saveButtonMaster(ButtonMasterDto buttonMasterDto);

	public Response getButtonMaster(ButtonMasterDto buttonMasterDto);

	public Response updateButtonMaster(ButtonMasterDto buttonMasterDto);

	public Response updateButtonMasterStatus(ButtonMasterDto buttonMasterDto);

	public Response getDoctorMasterByUnitId(DoctorMasterDto doctorMasterDto);

	public Response getDoctorMasterByUnitIdForSpeciality(DoctorMasterDto doctorMasterDto);

	public Response getAiButtonConfigMasterForDocAndSpeciality(ButtonMasterDto buttonMasterDto);

	public Response saveConfig(ButtonMasterDto buttonMasterDto);

	public Response getSavedConfig(ButtonMasterDto buttonMasterDto);

	public Response getActiveButtonMaster(ButtonMasterDto buttonMasterDto);

	public Response saveLetterAttachment(LetterAttachmentMapperDto letterattachmentMapperDto);
	public Response updateButtonMasterConfigStatus(ButtonMasterDto buttonMasterDto);

	public Response getActiveSavedConfig(ButtonMasterDto buttonMasterDto);

	public Response getSingleButtonById(ButtonMasterDto buttonMasterDto);

	
}

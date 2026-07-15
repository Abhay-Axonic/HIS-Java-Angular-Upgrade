package com.param.global.unit.service;

import javax.transaction.Transactional;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.unit.dto.OtimsRoleMasterDto;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IOtimsRoleCreationMasterService {

	Response saveOtimsRoleMaster(OtimsRoleMasterDto otimsRoleMasterDto) throws ApplicationException;

	Response getAllOtimsRoleMasterList(OtimsRoleMasterDto otimsRoleMasterDto) throws ApplicationException;

	Response updateOtimsRoleMaster(OtimsRoleMasterDto otimsRoleMasterDto) throws ApplicationException;

	Response updateOtimsRoleMasterStatus(OtimsRoleMasterDto otimsRoleMasterDto) throws ApplicationException;

}

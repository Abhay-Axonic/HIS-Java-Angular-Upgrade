package com.param.global.unit.dao;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.unit.dto.OtimsRoleMasterDto;

@SuppressWarnings({"rawtypes"})
public interface IOtimsRoleCreationMasterDao {

	Response saveOtimsRoleMaster(OtimsRoleMasterDto otimsRoleMasterDto) throws ApplicationException;

	Response getAllOtimsRoleMasterList(OtimsRoleMasterDto otimsRoleMasterDto) throws ApplicationException;

	Response updateOtimsRoleMaster(OtimsRoleMasterDto otimsRoleMasterDto) throws ApplicationException;

	Response updateOtimsRoleMasterStatus(OtimsRoleMasterDto otimsRoleMasterDto) throws ApplicationException;

}

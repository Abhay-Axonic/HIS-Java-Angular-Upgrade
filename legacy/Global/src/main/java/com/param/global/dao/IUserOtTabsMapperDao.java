package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.common.UserOtTabsMapperDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IUserOtTabsMapperDao {

	Response getUserByRoleType(UserOtTabsMapperDto userOtTabsMapperDto)throws ApplicationException;

	Response getUserOtTabsByUserId(Integer userId)throws ApplicationException;

}

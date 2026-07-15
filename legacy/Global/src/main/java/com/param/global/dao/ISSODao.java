package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.UserSSODto;
import com.param.global.dto.UserSSORequestDto;

import in.param.exception.ApplicationException;


@SuppressWarnings("rawtypes")
public interface ISSODao {
	
	Response validateToken(UserSSORequestDto userSSORequestDto) throws ApplicationException;
	
	Response clearSSODetails(UserSSORequestDto userSSORequestDto) throws ApplicationException;
	
}

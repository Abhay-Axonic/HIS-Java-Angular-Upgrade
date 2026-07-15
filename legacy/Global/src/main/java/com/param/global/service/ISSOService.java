package com.param.global.service;

import com.param.global.common.Response;
import com.param.global.dto.UserSSORequestDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface ISSOService 
{
	public Response validateToken(UserSSORequestDto userSSORequestDto)throws ApplicationException;
	
	public Response clearSSODetails(UserSSORequestDto userSSORequestDto)throws ApplicationException;
	
}

package com.param.duoAuthentication.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
@Transactional
public interface IDuoAuthenticationService {

	public Response getApplicationPropertyData(Integer organizationId, Integer unitId);

}

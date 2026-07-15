package com.param.duoAuthentication.dao;

import com.param.global.common.Response;

public interface IDuoAuthenticationDao {

	public Response getApplicationPropertyData(Integer organizationId, Integer unitId);
}

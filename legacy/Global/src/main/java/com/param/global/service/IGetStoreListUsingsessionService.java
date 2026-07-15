package com.param.global.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IGetStoreListUsingsessionService {
	
	@Transactional
	Response getStoreListUsingSession(Integer userId, Integer unitId) throws ApplicationException;

	Cookie setStoreIdToSession(HttpServletRequest request, Integer storeId);
}

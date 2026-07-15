package com.param.duoAuthentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.duoAuthentication.dao.IDuoAuthenticationDao;
import com.param.global.common.Response;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DuoAuthenticationServiceImpl implements IDuoAuthenticationService{
	@Autowired
	IDuoAuthenticationDao iDuoAuthenticationDao; 
	
	@Override
	public Response getApplicationPropertyData(Integer organizationId, Integer unitId) {
		// TODO Auto-generated method stub
		return iDuoAuthenticationDao.getApplicationPropertyData(organizationId, unitId);
	}

}

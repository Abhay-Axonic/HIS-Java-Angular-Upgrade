package com.param.global.service;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IUserLoginConstants;
import com.param.global.common.Response;
import com.param.global.dao.ISSODao;
import com.param.global.dto.UserSSORequestDto;

import in.param.exception.ApplicationException;


import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class SSOServiceImpl implements ISSOService,ICommonConstants,IUserLoginConstants{
	final static Logger logger = Logger.getLogger(SSOServiceImpl.class);


	@Autowired
	private ISSODao iSSODao;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional
	public Response validateToken(UserSSORequestDto userSSORequestDto) throws ApplicationException {
		try{
			return iSSODao.validateToken(userSSORequestDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}
	
	@Override
	@Transactional
	public Response clearSSODetails(UserSSORequestDto userSSORequestDto) throws ApplicationException {
		try{
			return iSSODao.clearSSODetails(userSSORequestDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}
	
	}

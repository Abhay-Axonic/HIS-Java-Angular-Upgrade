package com.param.procedure.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.procedure.dao.IOTServiceDao;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes"})
public class OtServicesServiceImpl implements IOtServicesService, ICommonConstants{

	@Autowired
	private IOTServiceDao iOTServiceDao;
	
	@Override
	@Transactional
	public Response getOtServicesByProReqId(int id) throws ApplicationException {
		try{
			return iOTServiceDao.getOtServicesByProReqId(id);
		}catch(Exception e){
			
			throw e;
		}
	}

}

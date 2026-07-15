package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.INewBornApgarDao;
import com.param.global.dto.NewBornApgarDto;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class NewBornApgarServiceImpl implements INewBornApgarService, ICommonConstants {
	final static Logger logger = Logger.getLogger(NewBornApgarServiceImpl.class);


	@Autowired
	private INewBornApgarDao iNewBornApgarDao;
		
	
	@Override
	public Response getNewBornApgarList(NewBornApgarDto dto) throws ApplicationException {
		try {
			return iNewBornApgarDao.getNewBornApgarList(dto);
			}catch (Exception e) {
			logger.error("getNewBornApgarList failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}
	
	@Override
	public Response updateNewBornApgar(NewBornApgarDto dto) throws ApplicationException {
		try {
			return iNewBornApgarDao.updateNewBornApgar(dto);
			}catch (Exception e) {
			logger.error("updateNewBornApgar failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}
	
	@Override
	public Response saveSingleApgarScore(NewBornApgarDto dto) throws ApplicationException {
		try {
			return iNewBornApgarDao.saveSingleApgarScore(dto);
			}catch (Exception e) {
			logger.error("saveSingleApgarScore failed", e);
			}
			return new Response(ERROR, null, null, null, null);
		}

}

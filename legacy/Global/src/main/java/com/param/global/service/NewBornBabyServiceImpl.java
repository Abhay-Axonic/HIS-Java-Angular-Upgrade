package com.param.global.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.INewBornBabyDao;
import com.param.global.dto.NewBornBabyDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class NewBornBabyServiceImpl implements INewBornBabyService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(NewBornBabyServiceImpl.class);

	@Autowired
	private INewBornBabyDao iNewBornBabyDao;

	@Override
	public Response saveNewBornBabyReg(NewBornBabyDto newBornBabyDto) throws ApplicationException {
		try {
//			Response deptResponse = iNewBornBabyDao.getBabyDetailsByName(newBornBabyDto);
//			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
//				return new Response(ERROR, null, ALREADY_EXIST, null, null);
//			} else {
				iNewBornBabyDao.saveNewBornBabyReg(newBornBabyDto);
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
//			}
		} catch (Exception e) {
			logger.error("saveNewBornBabyReg failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getBabyDetailsByParentId(NewBornBabyDto newBornBabyDto) throws ApplicationException {
		try {
			return iNewBornBabyDao.getBabyDetailsByParentId(newBornBabyDto);
		} catch (Exception e) {
			logger.error("getBabyDetailsByParentId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	
}

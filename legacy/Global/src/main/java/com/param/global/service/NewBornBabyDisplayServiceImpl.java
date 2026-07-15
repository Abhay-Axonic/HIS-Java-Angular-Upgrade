package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.INewBornBabyDisplayDao;
import com.param.global.dto.BabyStillAssmntDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class NewBornBabyDisplayServiceImpl implements INewBornBabyDisplayService, ICommonConstants {
	final static Logger logger = Logger.getLogger(NewBornBabyDisplayServiceImpl.class);


	@Autowired
	private INewBornBabyDisplayDao iNewBornBabyDisplayDao;
	
	@Override
	public Response saveStillBirthAssessment(BabyStillAssmntDto babyStillAssmntDto) throws ApplicationException {
		try {
				iNewBornBabyDisplayDao.saveStillBirthAssessment(babyStillAssmntDto);
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
		} catch (Exception e) {
		logger.error("saveStillBirthAssessment failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getStillBabyDetails(BabyStillAssmntDto babyStillAssmntDto) throws ApplicationException {
		try {
			return iNewBornBabyDisplayDao.getStillBabyDetails(babyStillAssmntDto);
		} catch (Exception e) {
		logger.error("getStillBabyDetails failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getSingleStillBaby(BabyStillAssmntDto babyStillAssmntDto) throws ApplicationException {
		try {
			return iNewBornBabyDisplayDao.getSingleStillBaby(babyStillAssmntDto);
		} catch (Exception e) {
		logger.error("getSingleStillBaby failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response updateStillBirthAssessment(BabyStillAssmntDto babyStillAssmntDto) throws ApplicationException {
		try {
				iNewBornBabyDisplayDao.updateStillBirthAssessment(babyStillAssmntDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
		} catch (Exception e) {
		logger.error("updateStillBirthAssessment failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
}

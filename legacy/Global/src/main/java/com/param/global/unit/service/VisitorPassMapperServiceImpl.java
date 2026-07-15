package com.param.global.unit.service;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.unit.dao.IVisitorPassMapperDao;
import com.param.global.unit.dto.VisitorPassMapperDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings("rawtypes")
public class VisitorPassMapperServiceImpl implements IVisitorPassMapperService, ICommonConstants {
	final static Logger logger = Logger.getLogger(VisitorPassMapperServiceImpl.class);


	@Autowired 
	IVisitorPassMapperDao iVisitorPassMapperDao;
	@Override
	public Response saveVisitorPassMapper(VisitorPassMapperDto dto) throws ApplicationException {
		try {
			Response res=iVisitorPassMapperDao.checkVisitorPassMapperAlreadyExistOrNot(dto);
			if(res.getListObject()!=null && res.getListObject().size()>0){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iVisitorPassMapperDao.saveVisitorPassMapper(dto);
			}
			
		} catch (HibernateException he) {
		logger.error("saveVisitorPassMapper failed", he);
		} catch (Exception e) {
		logger.error("saveVisitorPassMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateVisitorPassMapper(VisitorPassMapperDto dto) throws ApplicationException {
		try {
			Response res=iVisitorPassMapperDao.checkVisitorPassMapperAlreadyExistOrNotById(dto);
			if(res.getListObject()!=null && res.getListObject().size()>0){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iVisitorPassMapperDao.updateVisitorPassMapper(dto);
			}
			
		} catch (Exception e) {
		logger.error("updateVisitorPassMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateVisitorPassMapperStatus(VisitorPassMapperDto dto) throws ApplicationException {
		try {
			return iVisitorPassMapperDao.updateVisitorPassMapperStatus(dto);
		} catch (Exception e) {
		logger.error("updateVisitorPassMapperStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getVisitorPassMapperList(VisitorPassMapperDto dto) throws ApplicationException {
		try {
			return iVisitorPassMapperDao.getVisitorPassMapperList(dto);
		} catch (Exception e) {
		logger.error("getVisitorPassMapperList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getVisitorPassMapperCount(VisitorPassMapperDto dto) throws ApplicationException {
		try {
			return iVisitorPassMapperDao.getVisitorPassMapperCount(dto);
		} catch (Exception e) {
		logger.error("getVisitorPassMapperCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	
	@Override
	public Response getVisitorPassMapperById(VisitorPassMapperDto dto) throws ApplicationException {
		try {
			return iVisitorPassMapperDao.getVisitorPassMapperById(dto);
		} catch (Exception e) {
		logger.error("getVisitorPassMapperById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getVisitorPassCountByBedCategoryByVisitorPassIdByPassId(VisitorPassMapperDto dto)
			throws ApplicationException {
		try{
			return iVisitorPassMapperDao.getVisitorPassCountByBedCategoryByVisitorPassIdByPassId(dto);
		}catch(Exception e){
		logger.error("getVisitorPassCountByBedCategoryByVisitorPassIdByPassId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getVisitorPassMapperListByBedCategoryNVisitType(VisitorPassMapperDto dto)
			throws ApplicationException {
		try{
			return iVisitorPassMapperDao.getVisitorPassMapperListByBedCategoryNVisitType(dto);
		}catch(Exception e){
		logger.error("getVisitorPassMapperListByBedCategoryNVisitType failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

}

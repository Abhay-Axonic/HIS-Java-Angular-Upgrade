package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IBedsideImagingMasterDao;
import com.param.global.dto.BedsideImagingMasterDto;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class BedsideImagingMasterServiceImpl implements IBedsideImagingMasterService, ICommonConstants{
	final static Logger logger = Logger.getLogger(BedsideImagingMasterServiceImpl.class);


	@Autowired
	IBedsideImagingMasterDao iBedsideImagingMasterDao;

	@Override
	public Response saveBedsideImagingMaster(BedsideImagingMasterDto bedsideImagingMasterDto)
			throws ApplicationException {
		Response res=iBedsideImagingMasterDao.getBedsideImagingMasterByName(bedsideImagingMasterDto);
		try {
				if(res.getListObject()!=null && res.getListObject().size()>0){
					
					return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
				}else{
					return iBedsideImagingMasterDao.saveBedsideImagingMaster(bedsideImagingMasterDto);
				}

		} catch (Exception e) {
		logger.error("saveBedsideImagingMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response updateBedsideImagingMaster(BedsideImagingMasterDto bedsideImagingMasterDto)
			throws ApplicationException {
		Response res=iBedsideImagingMasterDao.getBedsideImagingMasterByNameNotId(bedsideImagingMasterDto);
		try {
				if(res.getListObject()!=null && res.getListObject().size()>0){
					
					return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
				}else{
					return iBedsideImagingMasterDao.updateBedsideImagingMaster(bedsideImagingMasterDto);
				}

		} catch (Exception e) {
		logger.error("updateBedsideImagingMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateBedsideImagingMasterStatus(BedsideImagingMasterDto bedsideImagingMasterDto)
			throws ApplicationException {
		try {
			return iBedsideImagingMasterDao.updateBedsideImagingMasterStatus(bedsideImagingMasterDto);

		} catch (Exception e) {
		logger.error("updateBedsideImagingMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBedsideImagingMasterList(BedsideImagingMasterDto bedsideImagingMasterDto)
			throws ApplicationException {
		try {
			return iBedsideImagingMasterDao.getBedsideImagingMasterList(bedsideImagingMasterDto);

		} catch (Exception e) {
		logger.error("getBedsideImagingMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveBedsideImagingMasterList(BedsideImagingMasterDto bedsideImagingMasterDto)
			throws ApplicationException {
		try {
			return iBedsideImagingMasterDao.getActiveBedsideImagingMasterList(bedsideImagingMasterDto);

		} catch (Exception e) {
		logger.error("getActiveBedsideImagingMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getBedsideImagingMasterByName(BedsideImagingMasterDto bedsideImagingMasterDto)
			throws ApplicationException {
		try {
				return iBedsideImagingMasterDao.getBedsideImagingMasterByName(bedsideImagingMasterDto);

				} catch (Exception e) {
				logger.error("getBedsideImagingMasterByName failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
	
	@Override
	public Response getBedsideImagingMasterByNameNotId(BedsideImagingMasterDto bedsideImagingMasterDto)
			throws ApplicationException {
		try {
				return iBedsideImagingMasterDao.getBedsideImagingMasterByNameNotId(bedsideImagingMasterDto);

			} catch (Exception e) {
			logger.error("getBedsideImagingMasterByNameNotId failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

}

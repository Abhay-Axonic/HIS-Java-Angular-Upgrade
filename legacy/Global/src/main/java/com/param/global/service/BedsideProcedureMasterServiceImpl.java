package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IBedsideProcedureMasterDao;
import com.param.global.dto.BedsideProcedureMasterDto;

import org.apache.log4j.Logger;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes"})
public class BedsideProcedureMasterServiceImpl  implements IBedsideProcedureMasterService, ICommonConstants{

	private static final Logger logger = Logger.getLogger(BedsideProcedureMasterServiceImpl.class);

	@Autowired
	IBedsideProcedureMasterDao iBedsideProcedureMasterDao;

	@Override
	public Response saveBedsideProcedureMaster(BedsideProcedureMasterDto bedsideProcedureMasterDto)
			throws ApplicationException {
		Response res=iBedsideProcedureMasterDao.getBedsideProcedureMasterByName(bedsideProcedureMasterDto);
		try {
				if(res.getListObject()!=null && res.getListObject().size()>0){
					
					return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
				}else{
					return iBedsideProcedureMasterDao.saveBedsideProcedureMaster(bedsideProcedureMasterDto);
				}

		} catch (Exception e) {
			logger.error("saveBedsideProcedureMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response updateBedsideProcedureMaster(BedsideProcedureMasterDto bedsideProcedureMasterDto)
			throws ApplicationException {
		Response res=iBedsideProcedureMasterDao.getBedsideProcedureMasterByNameNotId(bedsideProcedureMasterDto);
		try {
				if(res.getListObject()!=null && res.getListObject().size()>0){
					
					return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
				}else{
					return iBedsideProcedureMasterDao.updateBedsideProcedureMaster(bedsideProcedureMasterDto);
				}

		} catch (Exception e) {
			logger.error("updateBedsideProcedureMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateBedsideProcedureMasterStatus(BedsideProcedureMasterDto bedsideProcedureMasterDto)
			throws ApplicationException {
		try {
			return iBedsideProcedureMasterDao.updateBedsideProcedureMasterStatus(bedsideProcedureMasterDto);

		} catch (Exception e) {
			logger.error("updateBedsideProcedureMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBedsideProcedureMasterList(BedsideProcedureMasterDto bedsideProcedureMasterDto)
			throws ApplicationException {
		try {
			return iBedsideProcedureMasterDao.getBedsideProcedureMasterList(bedsideProcedureMasterDto);

		} catch (Exception e) {
			logger.error("getBedsideProcedureMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveBedsideProcedureMasterList(BedsideProcedureMasterDto bedsideProcedureMasterDto)
			throws ApplicationException {
		try {
			return iBedsideProcedureMasterDao.getActiveBedsideProcedureMasterList(bedsideProcedureMasterDto);

		} catch (Exception e) {
			logger.error("getActiveBedsideProcedureMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getBedsideProcedureMasterByName(BedsideProcedureMasterDto bedsideProcedureMasterDto)
			throws ApplicationException {
		try {
				return iBedsideProcedureMasterDao.getBedsideProcedureMasterByName(bedsideProcedureMasterDto);

				} catch (Exception e) {
					logger.error("getBedsideProcedureMasterByName failed", e);
				}
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
	
	@Override
	public Response getBedsideProcedureMasterByNameNotId(BedsideProcedureMasterDto bedsideProcedureMasterDto)
			throws ApplicationException {
		try {
				return iBedsideProcedureMasterDao.getBedsideProcedureMasterByNameNotId(bedsideProcedureMasterDto);

			} catch (Exception e) {
				logger.error("getBedsideProcedureMasterByNameNotId failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

}

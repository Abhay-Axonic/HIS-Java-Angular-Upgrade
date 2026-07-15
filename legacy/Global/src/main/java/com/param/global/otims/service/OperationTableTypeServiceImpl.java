package com.param.global.otims.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dao.IOperationTableTypeDao;
import com.param.global.otims.dto.OperationTableTypeDto;
import com.param.otims.model.OperationTableType;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings("rawtypes")
public class OperationTableTypeServiceImpl implements IOperationTableTypeService, ICommonConstants {
	final static Logger logger = Logger.getLogger(OperationTableTypeServiceImpl.class);


	@Autowired
	IOperationTableTypeDao iOperationTableTypeDao;

	@Autowired
	private IOtimsService iOtimsService;
	
	@Override
	public Response saveOperationTableType(OperationTableTypeDto operationTableTypeDto) throws ApplicationException {
		Response res = iOperationTableTypeDao.getOperationTableTypeListByName(operationTableTypeDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				Response response = iOperationTableTypeDao.saveOperationTableType(operationTableTypeDto);

				if(response.getObject()!=null) {
				OperationTableType dto = new OperationTableType();
				
				dto = (OperationTableType) response.getObject();
				operationTableTypeDto.setOperationTableTypeId(dto.getOperationTableTypeId());
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("create");
				payload.setData(operationTableTypeDto);
				request.setKey(MasterKeyConstants.Operation_Table_Type.toString());
				request.setPayload(payload);
				//iOtimsService.publish(request);
				
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				}
			}
			
		} catch (Exception e) {
		logger.error("saveOperationTableType failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateOperationTableType(OperationTableTypeDto operationTableTypeDto) throws ApplicationException {
		Response res = iOperationTableTypeDao.getOperationTableTypeListByNameNotById(operationTableTypeDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				Response response =  iOperationTableTypeDao.updateOperationTableType(operationTableTypeDto);
				
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(operationTableTypeDto);
				request.setKey(MasterKeyConstants.Operation_Table_Type.toString());
				request.setPayload(payload);
				//iOtimsService.publish(request);
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
			logger.error("updateOperationTableType failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateOperationTableTypeStatus(OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			return iOperationTableTypeDao.updateOperationTableTypeStatus(operationTableTypeDto);
		} catch (Exception e) {
			logger.error("updateOperationTableTypeStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTableTypeList(OperationTableTypeDto operationTableTypeDto) throws ApplicationException {
		try {
			return iOperationTableTypeDao.getOperationTableTypeList(operationTableTypeDto);
		} catch (Exception e) {
			logger.error("getOperationTableTypeList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTableTypeListById(Integer operationTableTypeId) throws ApplicationException {
		try {
			return iOperationTableTypeDao.getOperationTableTypeListById(operationTableTypeId);
		} catch (Exception e) {
			logger.error("getOperationTableTypeListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTableTypeListCount(OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			return iOperationTableTypeDao.getOperationTableTypeListCount(operationTableTypeDto);
		} catch (Exception e) {
			logger.error("getOperationTableTypeListCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveOperationTableTypeList(OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			return iOperationTableTypeDao.getActiveOperationTableTypeList(operationTableTypeDto);
		} catch (Exception e) {
			logger.error("getActiveOperationTableTypeList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTableTypeListByName(OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			return iOperationTableTypeDao.getOperationTableTypeListByName(operationTableTypeDto);
		} catch (Exception e) {
			logger.error("getOperationTableTypeListByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTableTypeListByNameNotById(OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			return iOperationTableTypeDao.getOperationTableTypeListByNameNotById(operationTableTypeDto);
		} catch (Exception e) {
			logger.error("getOperationTableTypeListByNameNotById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}

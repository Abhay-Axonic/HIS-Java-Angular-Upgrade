package com.param.global.otims.service;

import java.util.ArrayList;
import java.util.List;

import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dao.IOperationTheatreDao;
import com.param.global.otims.dao.IOperationTheatreTypeDao;
import com.param.global.otims.dto.OperationTheatreDto;
import com.param.global.otims.dto.OperationTheatreTypeDto;
import com.param.otims.model.OperationTheatre;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class OperationTheatreServiceImpl implements IOperationTheatreService, ICommonConstants{
	final static Logger logger = Logger.getLogger(OperationTheatreServiceImpl.class);

	
	
	@Autowired
	IOperationTheatreDao iOperationTheatreDao; 
	
	@Autowired
	IOperationTheatreTypeDao iOperationTheatreTypeDao; 
	
	
	@Autowired
	IOtimsService iOtimsService;
	
	
	@Override
	public Response saveOperationTheatre(OperationTheatreDto operationTheatreDto) throws ApplicationException {
		try {
			Response response=iOperationTheatreDao.getOperationTheatreByName(operationTheatreDto);
			
			if(response.getListObject().size()>0 && response.getListObject()!=null){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				Response res =  iOperationTheatreDao.saveOperationTheatre(operationTheatreDto);
				
				if(res.getObject()!=null) {
					
					OperationTheatre dto =new OperationTheatre();
					dto = (OperationTheatre) res.getObject();
					operationTheatreDto.setOperationTheatreId(dto.getOperationTheatreId());
					
					Response type = iOperationTheatreTypeDao.getOperationTheatreTypeById(operationTheatreDto.getTheatreTypeId(), operationTheatreDto.getOrgId(), operationTheatreDto.getUnitId());
					List<OperationTheatreTypeDto> typeDto = new ArrayList<>();
					typeDto.addAll(type.getListObject());
					operationTheatreDto.setTheatreTypeName(typeDto.get(0).getTheatreTypeName());
					
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(operationTheatreDto);
					request.setKey(MasterKeyConstants.Operation_Theatre.toString());
					request.setPayload(payload);
					//iOtimsService.publish(request);
					
					}
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				
			}
		} catch (Exception e) {
		logger.error("saveOperationTheatre failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateOperationTheatre(OperationTheatreDto operationTheatreDto) throws ApplicationException {
		try {
			Response response=iOperationTheatreDao.getOperationTheatreByNameNotId(operationTheatreDto);
			
			if(response.getListObject().size()>0 && response.getListObject()!=null){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				Response res = iOperationTheatreDao.updateOperationTheatre(operationTheatreDto);
				
				Response type = iOperationTheatreTypeDao.getOperationTheatreTypeById(operationTheatreDto.getTheatreTypeId(), operationTheatreDto.getOrgId(), operationTheatreDto.getUnitId());
				List<OperationTheatreTypeDto> typeDto = new ArrayList<>();
				typeDto.addAll(type.getListObject());
				operationTheatreDto.setTheatreTypeName(typeDto.get(0).getTheatreTypeName());
				
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(operationTheatreDto);
				request.setKey(MasterKeyConstants.Operation_Theatre.toString());
				request.setPayload(payload);
				//iOtimsService.publish(request);
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
		logger.error("updateOperationTheatre failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@Override
	public Response updateOperationTheatreStatus(OperationTheatreDto operationTheatreDto) throws ApplicationException {
		try {
			return iOperationTheatreDao.updateOperationTheatreStatus(operationTheatreDto);
		
			} catch (Exception e) {
			logger.error("updateOperationTheatreStatus failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	
	
	@Override
	public Response getOperationTheatreById(Integer operationTheatreId, Integer orgId, Integer unitId) throws ApplicationException {
		try {
				return iOperationTheatreDao.getOperationTheatreById(operationTheatreId, orgId, unitId);
			
			} catch (Exception e) {
			logger.error("getOperationTheatreById failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	

	@Override
	public Response getOperationTheatreList(OperationTheatreDto operationTheatreDto) throws ApplicationException {
		try {
			return iOperationTheatreDao.getOperationTheatreList(operationTheatreDto);
				
			} catch (Exception e) {
			logger.error("getOperationTheatreList failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getActiveOperationTheatreList(OperationTheatreDto operationTheatreDto) throws ApplicationException {
		try {
			return iOperationTheatreDao.getActiveOperationTheatreList(operationTheatreDto);
		
			} catch (Exception e) {
			logger.error("getActiveOperationTheatreList failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getOperationTheatreCount(OperationTheatreDto operationTheatreDto) throws ApplicationException {
		try {
			return iOperationTheatreDao.getOperationTheatreCount(operationTheatreDto);
		
			} catch (Exception e) {
			logger.error("getOperationTheatreCount failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getOperationTheatreByName(OperationTheatreDto operationTheatreDto) throws ApplicationException {
		try {
			return iOperationTheatreDao.getOperationTheatreByName(operationTheatreDto);
		
			} catch (Exception e) {
			logger.error("getOperationTheatreByName failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getOperationTheatreByNameNotId(OperationTheatreDto operationTheatreDto) throws ApplicationException {
		try {
				return iOperationTheatreDao.getOperationTheatreByNameNotId(operationTheatreDto);
				
			} catch (Exception e) {
			logger.error("getOperationTheatreByNameNotId failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}


	@Override
	public Response getOperationTheatreByTypeId(Integer operationTheatreTypeId, Integer orgId, Integer unitId)
			throws ApplicationException {
		try {
			return iOperationTheatreDao.getOperationTheatreByTypeId(operationTheatreTypeId, orgId, unitId);
		
		} catch (Exception e) {
		logger.error("getOperationTheatreByTypeId failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
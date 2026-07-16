package com.param.global.otims.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dao.IOperationTableDao;
import com.param.global.otims.dao.IOperationTableTypeDao;
import com.param.global.otims.dao.IOperationTheatreDao;
import com.param.global.otims.dto.OperationTableTypeDto;
import com.param.global.otims.dto.OperationTheatreDto;
import com.param.otims.model.OperationTable;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes","unchecked"})
@Service
public class OperationTableServiceImpl implements IOperationTableService, ICommonConstants {
	final static Logger logger = Logger.getLogger(OperationTableServiceImpl.class);


	@Autowired
	IOperationTableDao iOperationTableDao;

	@Autowired
	IOtimsService iOtimsService;
	
	@Autowired
	IOperationTableTypeDao iOperationTableTypeDao;
	
	@Autowired
	IOperationTheatreDao iOperationTheatreDao; 
	
	@Override
	public Response saveOperationTable(OperationTableTypeDto operationTableTypeDto) throws ApplicationException {
		Response res = iOperationTableDao.getOperationTableListByName(operationTableTypeDto);
		try {
			if (res.getListObject() != null && res.getListObject().size() > 0) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				Response response =  iOperationTableDao.saveOperationTable(operationTableTypeDto);
				if(response.getObject()!=null) {
					List<OperationTableTypeDto> tableTypeList = new ArrayList<>();
					List<OperationTheatreDto> operationTheatreList = new ArrayList<>();
					
					OperationTable dto = new OperationTable();
					dto = (OperationTable) response.getObject();
					//get table type name by id
					Response tableTypeRes = iOperationTableTypeDao.getOperationTableTypeListById(dto.getTableTypeId());
					if(tableTypeRes.getListObject()!=null && tableTypeRes.getListObject().size()>0)
					{
						tableTypeList.addAll(tableTypeRes.getListObject());
						operationTableTypeDto.setTableTypeName(tableTypeList.getFirst().getTableDesc());
					}
					
					//get  Operation theatre name  by id
					Response operationTheatreRes = iOperationTheatreDao.getOperationTheatreById(operationTableTypeDto.getOperationTheatreId(), operationTableTypeDto.getOrgId(), operationTableTypeDto.getUnitId());
					if(operationTheatreRes.getListObject()!=null && operationTheatreRes.getListObject().size()>0)
					{
						operationTheatreList.addAll(operationTheatreRes.getListObject());
						operationTableTypeDto.setTheatreName(operationTheatreList.getFirst().getTheatreName());
					}
					
					operationTableTypeDto.setOperationTableId(dto.getOperationTableId());
					operationTableTypeDto.setOperationTableTypeId(operationTableTypeDto.getTableTypeId());
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(operationTableTypeDto);
					request.setKey(MasterKeyConstants.Operation_Table.toString());
					request.setPayload(payload);
					//iOtimsService.publish(request);
					
					}
			//	return iOperationTableDao.saveOperationTable(operationTableTypeDto);
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}

		} catch (Exception e) {
		logger.error("saveOperationTable failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateOperationTable(OperationTableTypeDto operationTableTypeDto) throws ApplicationException {
		Response res = iOperationTableDao.getOperationTableListByNameNotById(operationTableTypeDto);
		try {
			if (res.getListObject() != null && res.getListObject().size() > 0) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				Response response =  iOperationTableDao.updateOperationTable(operationTableTypeDto);
				
				
				List<OperationTableTypeDto> tableTypeList = new ArrayList<>();
				List<OperationTheatreDto> operationTheatreList = new ArrayList<>();
				
				//get table type name by id
				Response tableTypeRes = iOperationTableTypeDao.getOperationTableTypeListById(operationTableTypeDto.getTableTypeId());
				if(tableTypeRes.getListObject()!=null && tableTypeRes.getListObject().size()>0)
				{
					tableTypeList.addAll(tableTypeRes.getListObject());
					operationTableTypeDto.setTableTypeName(tableTypeList.getFirst().getTableDesc());
				}
				
				//get  Operation theatre name  by id
				Response operationTheatreRes = iOperationTheatreDao.getOperationTheatreById(operationTableTypeDto.getOperationTheatreId(), operationTableTypeDto.getOrgId(), operationTableTypeDto.getUnitId());
				if(operationTheatreRes.getListObject()!=null && operationTheatreRes.getListObject().size()>0)
				{
					operationTheatreList.addAll(operationTheatreRes.getListObject());
					operationTableTypeDto.setTheatreName(operationTheatreList.getFirst().getTheatreName());
				}
				
				operationTableTypeDto.setOperationTableTypeId(operationTableTypeDto.getTableTypeId());
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(operationTableTypeDto);
				request.setKey(MasterKeyConstants.Operation_Table.toString());
				request.setPayload(payload);
				//iOtimsService.publish(request);
				
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
		logger.error("updateOperationTable failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateOperationTableStatus(OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			return iOperationTableDao.updateOperationTableStatus(operationTableTypeDto);
		} catch (Exception e) {
		logger.error("updateOperationTableStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTableList(OperationTableTypeDto operationTableTypeDto) throws ApplicationException {
		try {
			return iOperationTableDao.getOperationTableList(operationTableTypeDto);
		} catch (Exception e) {
		logger.error("getOperationTableList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTableListById(Integer operationTableId) throws ApplicationException {
		try {
			return iOperationTableDao.getOperationTableListById(operationTableId);
		} catch (Exception e) {
		logger.error("getOperationTableListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTableListCount(OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			return iOperationTableDao.getOperationTableListCount(operationTableTypeDto);
		} catch (Exception e) {
		logger.error("getOperationTableListCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveOperationTableList(OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			return iOperationTableDao.getActiveOperationTableList(operationTableTypeDto);
		} catch (Exception e) {
		logger.error("getActiveOperationTableList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTableListByName(OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			return iOperationTableDao.getOperationTableListByName(operationTableTypeDto);
		} catch (Exception e) {
		logger.error("getOperationTableListByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTableListByNameNotById(OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			return iOperationTableDao.getOperationTableListByNameNotById(operationTableTypeDto);
		} catch (Exception e) {
		logger.error("getOperationTableListByNameNotById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveOperationTableListByTheatreId(OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			return iOperationTableDao.getActiveOperationTableListByTheatreId(operationTableTypeDto);
		} catch (Exception e) {
		logger.error("getActiveOperationTableListByTheatreId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}

package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IAmbulanceTarrifDao;
import com.param.global.dao.IAmbulanceTypeDao;
import com.param.global.dao.IResourceAllocationDao;
import com.param.global.dto.ResourceAllocationDto;
import com.param.global.model.AmbulanceType;
import com.param.global.model.CountryMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Service

public class ResourceAllocationServiceImpl implements IResourceAllocationService, ICommonConstants {
	final static Logger logger = Logger.getLogger(ResourceAllocationServiceImpl.class);


	@Autowired
	private IResourceAllocationDao iResourceAllocationDao;
	

	@Override
	public Response saveResourceAllocation(ResourceAllocationDto resourceAllocationDto) throws ApplicationException {
		try {

			Response deptResponse = iResourceAllocationDao.getResourceAllocationByName(resourceAllocationDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				iResourceAllocationDao.saveResourceAllocation(resourceAllocationDto);
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
		logger.error("saveResourceAllocation failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getResourceAllocationList(ResourceAllocationDto resourceAllocationDto) throws ApplicationException {
		try {
			return iResourceAllocationDao.getResourceAllocationList(resourceAllocationDto);
		} catch (Exception e) {
		logger.error("getResourceAllocationList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getResourceAllocationListCount(ResourceAllocationDto resourceAllocationDto)
			throws ApplicationException {
		try {
			return iResourceAllocationDao.getResourceAllocationListCount(resourceAllocationDto);
		} catch (Exception e) {
		logger.error("getResourceAllocationListCount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getResourceAllocationActiveList(ResourceAllocationDto resourceAllocationDto) throws ApplicationException {
		try {
			return iResourceAllocationDao.getResourceAllocationActiveList(resourceAllocationDto);
		} catch (Exception e) {
		logger.error("getResourceAllocationActiveList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSingleResourceAllocation(ResourceAllocationDto resourceAllocationDto) throws ApplicationException {
		try {
			return iResourceAllocationDao.getSingleResourceAllocation(resourceAllocationDto);
		} catch (Exception e) {
		logger.error("getSingleResourceAllocation failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateResourceAllocation(ResourceAllocationDto resourceAllocationDto) throws ApplicationException {
		try {
			Response deptResponse = iResourceAllocationDao.getResourceAllocationByName(resourceAllocationDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				iResourceAllocationDao.updateResourceAllocation(resourceAllocationDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
		logger.error("updateResourceAllocation failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response changeResourceAllocationStatus(ResourceAllocationDto resourceAllocationDto) throws ApplicationException {
		try {
			Response deptResponse = iResourceAllocationDao.getResourceAllocationByID(resourceAllocationDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				
				iResourceAllocationDao.changeResourceAllocationStatus(resourceAllocationDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}
		} catch (Exception e) {
		logger.error("changeResourceAllocationStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	

	

}

package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IAmbulanceMasterDao;
import com.param.global.dto.AmbulanceMasterDto;
import com.param.global.dto.AmbulanceTypeDto;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Service

public class AmbulanceMasterServiceImpl implements IAmbulanceMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(AmbulanceMasterServiceImpl.class);


	@Autowired
	private IAmbulanceMasterDao iAmbulanceMasterDao;
	
	@Override
	public Response saveAmbulanceMasterData(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException {
		try {
			
			Response deptResponse = iAmbulanceMasterDao.getAmbulanceMasterByName(ambulanceMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				 iAmbulanceMasterDao.saveAmbulanceMasterData(ambulanceMasterDto);
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
			
			
		} catch (Exception e) {
		logger.error("saveAmbulanceMasterData failed", e);
		}
		return new Response(ERROR, null, null, null, null);
}
	
	
	@Override
	public Response getAmbulanceMasterDataList(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException {
		try {
			return iAmbulanceMasterDao.getAmbulanceMasterDataList(ambulanceMasterDto);
		} catch (Exception e) {
		logger.error("getAmbulanceMasterDataList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getAmbulanceMasterDataActiveList(AmbulanceMasterDto ambulanceMasterDto)
			throws ApplicationException {
		try {
			return iAmbulanceMasterDao.getAmbulanceMasterDataActiveList(ambulanceMasterDto);
		} catch (Exception e) {
		logger.error("getAmbulanceMasterDataActiveList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getSingleAmbulanceMasterData(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException {
		try {
			return iAmbulanceMasterDao.getSingleAmbulanceMasterData(ambulanceMasterDto);
		} catch (Exception e) {
		logger.error("getSingleAmbulanceMasterData failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response updateAmbulanceMasterData(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException {
		try {
			return iAmbulanceMasterDao.updateAmbulanceMasterData(ambulanceMasterDto);
		} catch (Exception e) {
		logger.error("updateAmbulanceMasterData failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response changeAmbulanceMasterDataStatus(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException {
		try {
			return iAmbulanceMasterDao.changeAmbulanceMasterDataStatus(ambulanceMasterDto);
		} catch (Exception e) {
		logger.error("changeAmbulanceMasterDataStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

}

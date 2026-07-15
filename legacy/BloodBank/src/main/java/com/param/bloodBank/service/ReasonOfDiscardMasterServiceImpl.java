package com.param.bloodBank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IReasonOfDiscardMasterDao;
import com.param.bloodBank.dto.ReasonOfDiscardMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})

public class ReasonOfDiscardMasterServiceImpl implements IReasonOfDiscardMasterService, ICommonConstants{

	final static Logger logger = Logger.getLogger(ReasonOfDiscardMasterServiceImpl.class);

	@Autowired
	IReasonOfDiscardMasterDao iReasonOfDiscardMasterDao;

	@Override
	public Response saveReasonOfDiscardMaster(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto)
			throws ApplicationException {
		try {
			Response response=iReasonOfDiscardMasterDao.getReasonOfDiscardMasterByName(reasonOfDiscardMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iReasonOfDiscardMasterDao.saveReasonOfDiscardMaster(reasonOfDiscardMasterDto);
			}
		} catch (Exception e) {
			logger.error("saveReasonOfDiscardMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateReasonOfDiscardMaster(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto)
			throws ApplicationException {
		try {
			Response response=iReasonOfDiscardMasterDao.getReasonOfDiscardMasterByNameNotId(reasonOfDiscardMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iReasonOfDiscardMasterDao.updateReasonOfDiscardMaster(reasonOfDiscardMasterDto);
			}
		} catch (Exception e) {
			logger.error("updateReasonOfDiscardMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateReasonOfDiscardMasterStatus(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto)
			throws ApplicationException {
		try {
			
				return iReasonOfDiscardMasterDao.updateReasonOfDiscardMasterStatus(reasonOfDiscardMasterDto);
			
		} catch (Exception e) {
			logger.error("updateReasonOfDiscardMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getReasonOfDiscardMasterList(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto)
			throws ApplicationException {
		try {

				return iReasonOfDiscardMasterDao.getReasonOfDiscardMasterList(reasonOfDiscardMasterDto);
			
		} catch (Exception e) {
			logger.error("getReasonOfDiscardMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getReasonOfDiscardMasterById(Integer reasonOfDiscardId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {

			return iReasonOfDiscardMasterDao.getReasonOfDiscardMasterById(reasonOfDiscardId, organizationId, unitId);
		
		} catch (Exception e) {
			logger.error("getReasonOfDiscardMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveReasonOfDiscardMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {

			return iReasonOfDiscardMasterDao.getActiveReasonOfDiscardMaster(organizationId, unitId);
		
	} catch (Exception e) {
		logger.error("getActiveReasonOfDiscardMaster failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getReasonOfDiscardMasterCount(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto)
			throws ApplicationException {
		try {

			return iReasonOfDiscardMasterDao.getReasonOfDiscardMasterCount(reasonOfDiscardMasterDto);
		
	} catch (Exception e) {
		logger.error("getReasonOfDiscardMasterCount failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getReasonOfDiscardMasterByName(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto)
			throws ApplicationException {
		try {

			return iReasonOfDiscardMasterDao.getReasonOfDiscardMasterByName(reasonOfDiscardMasterDto);
		
	} catch (Exception e) {
		logger.error("getReasonOfDiscardMasterByName failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getReasonOfDiscardMasterByNameNotId(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto)
			throws ApplicationException {
		try {

			return iReasonOfDiscardMasterDao.getReasonOfDiscardMasterByNameNotId(reasonOfDiscardMasterDto);
		
	} catch (Exception e) {
		logger.error("getReasonOfDiscardMasterByNameNotId failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getAllReasonOfDiscardMasterList(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto)
			throws ApplicationException {
		try {

			return iReasonOfDiscardMasterDao.getAllReasonOfDiscardMasterList(reasonOfDiscardMasterDto);
		
	} catch (Exception e) {
		logger.error("getAllReasonOfDiscardMasterList failed", e);
		throw new ServiceException(e.getMessage());
	}
	}

}


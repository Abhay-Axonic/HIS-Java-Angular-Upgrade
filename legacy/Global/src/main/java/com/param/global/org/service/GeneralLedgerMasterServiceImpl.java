package com.param.global.org.service;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.dao.IGeneralLedgerMasterServiceDao;
import com.param.global.org.dto.GeneralLedgerMasterDto;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GeneralLedgerMasterServiceImpl implements IGeneralLedgerMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(GeneralLedgerMasterServiceImpl.class);


	@Autowired
	IGeneralLedgerMasterServiceDao iGeneralLedgerMasterServiceDao;

	@Override
	public Response saveGeneralLedgerMaster(GeneralLedgerMasterDto generalLedgerMasterDto) throws ApplicationException {
		try {
			Response response = iGeneralLedgerMasterServiceDao.getGeneralLedgerMasterByName(generalLedgerMasterDto);
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				return iGeneralLedgerMasterServiceDao.saveGeneralLedgerMaster(generalLedgerMasterDto);
			}

		} catch (Exception e) {
		logger.error("saveGeneralLedgerMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getGeneralLedgerMasterById(int lederId, int orgId) throws ApplicationException {
		try {
			return iGeneralLedgerMasterServiceDao.getGeneralLedgerMasterById(lederId, orgId);
		} catch (HibernateException he) {
		logger.error("getGeneralLedgerMasterById failed", he);
		} catch (Exception e) {
		logger.error("getGeneralLedgerMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getGeneralLedgerMasterList(int orgId,int offset,int noOfRecordsPerPage) throws ApplicationException {
		try {
			return iGeneralLedgerMasterServiceDao.getGeneralLedgerMasterList(orgId,offset,noOfRecordsPerPage);
		} catch (Exception e) {
		logger.error("getGeneralLedgerMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateGeneralLedgerMaster(GeneralLedgerMasterDto generalLedgerMasterDto)
			throws ApplicationException {
		try {
			Response deptResponse = iGeneralLedgerMasterServiceDao.getLedgerByNameNotId(generalLedgerMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}else{
				return iGeneralLedgerMasterServiceDao.updateGeneralLedgerMaster(generalLedgerMasterDto);
			}
			
		} catch (Exception e) {
		logger.error("updateGeneralLedgerMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateGeneralLedgerMasterStatus(GeneralLedgerMasterDto generalLedgerMasterDto)
			throws ApplicationException {
		try {
			return iGeneralLedgerMasterServiceDao.updateGeneralLedgerMasterStatus(generalLedgerMasterDto);
		} catch (Exception e) {
		logger.error("updateGeneralLedgerMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getGeneralLedgerMasterActiveList(int orgId,String generalLedgerName) throws ApplicationException {
		try {
			return iGeneralLedgerMasterServiceDao.getGeneralLedgerMasterActiveList(orgId, generalLedgerName);
		} catch (Exception e) {
		logger.error("getGeneralLedgerMasterActiveList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getGeneralLedgerMasterCount(GeneralLedgerMasterDto generalLedgerMasterDto)
			throws ApplicationException {
		try {
			return iGeneralLedgerMasterServiceDao.getGeneralLedgerMasterCount(generalLedgerMasterDto);
		} catch (Exception e) {
		logger.error("getGeneralLedgerMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

}

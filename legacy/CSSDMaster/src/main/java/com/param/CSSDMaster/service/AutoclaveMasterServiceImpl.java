package com.param.CSSDMaster.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.Response;
import com.param.CSSDMaster.dao.IAutoclaveMasterDao;
import com.param.CSSDMaster.dto.AutoclaveMasterDto;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AutoclaveMasterServiceImpl implements IAutoclaveMasterService {

	final static Logger logger = Logger.getLogger(AutoclaveMasterServiceImpl.class);


	@Autowired
	private IAutoclaveMasterDao iMasterDao;

	@Override
	@Transactional
	public Response updateMaster(AutoclaveMasterDto masterDto) {
		try {
			return iMasterDao.updateAutoclave(masterDto);
		} catch (Exception e) {
			logger.error("updateMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response updateAutoclaveStatus(AutoclaveMasterDto masterDto) {
		try {
			return iMasterDao.updateAutoclaveStatus(masterDto);
		} catch (Exception e) {
			logger.error("updateAutoclaveStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getAllMasterList(Integer organizationId,Integer unitId,Integer offset,Integer noOfRecordsPerPage) {
		try {
			return iMasterDao.getAllAutoclaveList(organizationId,unitId,offset,noOfRecordsPerPage);
		} catch (Exception e) {
			logger.error("getAllMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getActiveMasterList(Integer organizationId,Integer unitId) {
		try {
			return iMasterDao.getActiveAutoclaveList(organizationId,unitId);
		} catch (Exception e) {
			logger.error("getActiveMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response createMaster(AutoclaveMasterDto masterDto) {
		try {
			return iMasterDao.createAutoclave(masterDto);
		} catch (Exception e) {
			logger.error("createMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getAutoclavesListcount(AutoclaveMasterDto autoclaveMasterDto) {
		try {
			return iMasterDao.getAutoclavesListcount(autoclaveMasterDto);
		} catch (Exception e) {
			logger.error("getAutoclavesListcount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getActiveSterilizationColorList(AutoclaveMasterDto autoclaveMasterDto) {
		try {
			return iMasterDao.getActiveSterilizationColorList(autoclaveMasterDto);
		} catch (Exception e) {
			logger.error("getActiveSterilizationColorList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}

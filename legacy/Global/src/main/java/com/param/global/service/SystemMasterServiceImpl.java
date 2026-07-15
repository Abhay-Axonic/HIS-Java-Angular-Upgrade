package com.param.global.service;

import javax.transaction.Transactional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.param.exception.ApplicationException;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.ISystemMasterDao;
import com.param.global.dao.ISystemObservationPropertyDao;
import com.param.global.dto.MasterOfSytemDto;
import com.param.global.dto.SystemMasterDto;
import com.param.global.dto.SystemObservationMasterDto;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SystemMasterServiceImpl implements ISystemMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(SystemMasterServiceImpl.class);


	@Autowired
	private ISystemMasterDao iSystemMasterDao;
	
	@Autowired
	private ISystemObservationPropertyDao iSystemObservationPropertyDao;

	@Override
	@Transactional
	public Response getListOfSystemMaster(SystemMasterDto systemMasterDto) throws ApplicationException {
		return iSystemMasterDao.getListOfSystemMaster(systemMasterDto);
	}

	@Override
	@Transactional
	public Response saveSystemMaster(MasterOfSytemDto masterOfSytemDto) throws ApplicationException {
		return iSystemMasterDao.saveSystemMaster(masterOfSytemDto);
	}

	@Override
	@Transactional
	public Response getSystemMaster(MasterOfSytemDto masterOfSytemDto) throws ApplicationException {
		return iSystemMasterDao.getSystemMaster(masterOfSytemDto);
	}

	@Override
	@Transactional
	public Response updateSystemMasterStatus(MasterOfSytemDto masterOfSytemDto) throws ApplicationException {
		
		try {
			Response res=iSystemMasterDao.updateSystemMasterStatus(masterOfSytemDto);
			if(masterOfSytemDto.getStatus()=='I')
			{
				iSystemObservationPropertyDao.updateStatusOfSystemObservationMaster(masterOfSytemDto);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	

	// added by dinesh jagatap on 21-04-18 Start
	@Override
	@Transactional
	public Response getListOfSystemMasterById(MasterOfSytemDto masterOfSytemDto) throws ApplicationException {
		try {
			return iSystemMasterDao.getListOfSystemMasterById(masterOfSytemDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updateSystemMaster(MasterOfSytemDto masterOfSytemDto) throws ApplicationException {
		try {
			return iSystemMasterDao.updateSystemMaster(masterOfSytemDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getListOfSystemMasterByType(MasterOfSytemDto masterOfSytemDto) throws ApplicationException {
		try {
				return iSystemMasterDao.getListOfSystemMasterByType(masterOfSytemDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response updateStatusOfSystemObservationMaster(MasterOfSytemDto masterOfSytemDto)
			throws ApplicationException {
		return iSystemObservationPropertyDao.updateStatusOfSystemObservationMaster(masterOfSytemDto);

	}

	@Override
	@Transactional
	public Response getSystemGenderListById(MasterOfSytemDto masterOfSytemDto) throws ApplicationException {
		try {
			return iSystemMasterDao.getSystemGenderListById(masterOfSytemDto);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
}

	

}

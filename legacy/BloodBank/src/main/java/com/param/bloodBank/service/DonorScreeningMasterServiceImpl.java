package com.param.bloodBank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.global.model.PatientTypeMaster;
import com.param.bloodBank.dao.DonorScreeningMasterDto;
import com.param.bloodBank.dao.IDonorScreeningMasterDao;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings("rawtypes")
public class DonorScreeningMasterServiceImpl implements IDonorScreeningMasterService,ICommonConstants{

	final static Logger logger = Logger.getLogger(DonorScreeningMasterServiceImpl.class);


	@Autowired IDonorScreeningMasterDao iDonorScreeningMasterDao;

	@Override
	public Response saveDonorScreenigMaster(DonorScreeningMasterDto donorScreeningMasterDto)
			throws ApplicationException {
		try{
			return iDonorScreeningMasterDao.saveDonorScreenigMaster(donorScreeningMasterDto);
		}catch(Exception e){
			logger.error("saveDonorScreenigMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAllDonorScreeningList(DonorScreeningMasterDto donorScreeningMasterDto)
			throws ApplicationException {
		try{
			return iDonorScreeningMasterDao.getAllDonorScreeningList(donorScreeningMasterDto);
		}catch(Exception e){
			logger.error("getAllDonorScreeningList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDonorScreeningMaster(DonorScreeningMasterDto donorScreeningMasterDto)
			throws ApplicationException {
		try{
			return iDonorScreeningMasterDao.updateDonorScreeningMaster(donorScreeningMasterDto);
		}catch(Exception e){
			logger.error("updateDonorScreeningMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDonorScreeningMasterStatus(DonorScreeningMasterDto dto)
			throws ApplicationException {
		try{
			return iDonorScreeningMasterDao.updateDonorScreeningMasterStatus(dto);
		}catch(Exception e){
			logger.error("updateDonorScreeningMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDonorScreeningListByGenderId(DonorScreeningMasterDto donorScreeningMasterDto)
			throws ApplicationException {
		try{
			return iDonorScreeningMasterDao.getDonorScreeningListByGenderId(donorScreeningMasterDto);
		}catch(Exception e){
			logger.error("getDonorScreeningListByGenderId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDefferedTypeMaster() throws ApplicationException {
		try{
			return iDonorScreeningMasterDao.getDefferedTypeMaster();
		}catch(Exception e){
			logger.error("getDefferedTypeMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAllDonorScreeningListPaginationList(DonorScreeningMasterDto donorScreeningMasterDto)
			throws ApplicationException {
		try {
			
			return iDonorScreeningMasterDao.getAllDonorScreeningListPaginationList(donorScreeningMasterDto);
	
	} catch (Exception e) {
		logger.error("getAllDonorScreeningListPaginationList failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getAllDonorScreeningListCount(DonorScreeningMasterDto donorScreeningMasterDto)
			throws ApplicationException {
		try {
			
			return iDonorScreeningMasterDao.getAllDonorScreeningListCount(donorScreeningMasterDto);
	
	} catch (Exception e) {
		logger.error("getAllDonorScreeningListCount failed", e);
		throw new ServiceException(e.getMessage());
	}
}
}

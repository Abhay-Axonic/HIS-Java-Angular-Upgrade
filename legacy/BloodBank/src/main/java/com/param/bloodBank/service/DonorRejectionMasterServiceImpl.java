package com.param.bloodBank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IDonorRejectionMasterDao;
import com.param.bloodBank.dto.DonorRejectionMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class DonorRejectionMasterServiceImpl implements IDonorRejectionMasterService, ICommonConstants{

	final static Logger logger = Logger.getLogger(DonorRejectionMasterServiceImpl.class);

	@Autowired
	IDonorRejectionMasterDao iDonorRejectionMasterDao;

	@Override
	public Response saveDonorRejectionMaster(DonorRejectionMasterDto donorRejectionMasterDto)
			throws ApplicationException {
		try {
			Response response=iDonorRejectionMasterDao.getDonorRejectionMasterByName(donorRejectionMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iDonorRejectionMasterDao.saveDonorRejectionMaster(donorRejectionMasterDto);
			}
		} catch (Exception e) {
			logger.error("saveDonorRejectionMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDonorRejectionMaster(DonorRejectionMasterDto donorRejectionMasterDto)
			throws ApplicationException {
		try {
			Response response=iDonorRejectionMasterDao.getDonorRejectionMasterByNameNotId(donorRejectionMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				return iDonorRejectionMasterDao.updateDonorRejectionMaster(donorRejectionMasterDto);
			}
		} catch (Exception e) {
			logger.error("updateDonorRejectionMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDonorRejectionMasterStatus(DonorRejectionMasterDto donorRejectionMasterDto)
			throws ApplicationException {
		try {
			
				return iDonorRejectionMasterDao.updateDonorRejectionMasterStatus(donorRejectionMasterDto);
			
		} catch (Exception e) {
			logger.error("updateDonorRejectionMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getDonorRejectionMasterList(DonorRejectionMasterDto donorRejectionMasterDto)
			throws ApplicationException {
		try {

				return iDonorRejectionMasterDao.getDonorRejectionMasterList(donorRejectionMasterDto);
			
		} catch (Exception e) {
			logger.error("getDonorRejectionMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getDonorRejectionMasterById(Integer donorRejectionId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {

			return iDonorRejectionMasterDao.getDonorRejectionMasterById(donorRejectionId, organizationId, unitId);
		
		} catch (Exception e) {
			logger.error("getDonorRejectionMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveDonorRejectionMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {

			return iDonorRejectionMasterDao.getActiveDonorRejectionMaster(organizationId, unitId);
		
	} catch (Exception e) {
		logger.error("getActiveDonorRejectionMaster failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getDonorRejectionMasterCount(DonorRejectionMasterDto donorRejectionMasterDto)
			throws ApplicationException {
		try {

			return iDonorRejectionMasterDao.getDonorRejectionMasterCount(donorRejectionMasterDto);
		
	} catch (Exception e) {
		logger.error("getDonorRejectionMasterCount failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getDonorRejectionMasterByName(DonorRejectionMasterDto donorRejectionMasterDto)
			throws ApplicationException {
		try {

			return iDonorRejectionMasterDao.getDonorRejectionMasterByName(donorRejectionMasterDto);
		
	} catch (Exception e) {
		logger.error("getDonorRejectionMasterByName failed", e);
		throw new ServiceException(e.getMessage());
	}
}

	@Override
	public Response getDonorRejectionMasterByNameNotId(DonorRejectionMasterDto donorRejectionMasterDto)
			throws ApplicationException {
		try {

			return iDonorRejectionMasterDao.getDonorRejectionMasterByNameNotId(donorRejectionMasterDto);
		
	} catch (Exception e) {
		logger.error("getDonorRejectionMasterByNameNotId failed", e);
		throw new ServiceException(e.getMessage());
	}
}

}


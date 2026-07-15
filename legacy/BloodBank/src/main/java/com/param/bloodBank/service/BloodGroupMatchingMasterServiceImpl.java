package com.param.bloodBank.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IBloodGroupMatchingMasterDao;
import com.param.bloodBank.dto.BloodGroupMatchingMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes"})
@Service
public class BloodGroupMatchingMasterServiceImpl implements IBloodGroupMatchingMasterService , ICommonConstants {

	final static Logger logger = Logger.getLogger(BloodGroupMatchingMasterServiceImpl.class);


	
	@Autowired
	private IBloodGroupMatchingMasterDao iBloodBagMatchingMasterDao;
	
	@Override
	@Transactional
	public Response saveBloodGroupMatchingMaster(List<BloodGroupMatchingMasterDto> bloodBagMatchingMasterDto) {
		try {
		return iBloodBagMatchingMasterDao.saveBloodGroupMatchingMaster(bloodBagMatchingMasterDto);
			
		} catch (Exception e) {
			logger.error("saveBloodGroupMatchingMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBloodGroupMatchingMasterById(Integer componentId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			return iBloodBagMatchingMasterDao.getBloodGroupMatchingMasterById(componentId,organizationId,unitId);
				
			} catch (Exception e) {
				logger.error("getBloodGroupMatchingMasterById failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	@Transactional
	public Response getBloodGroupsByComponentAndDonorBloodGroupId(BloodGroupMatchingMasterDto bloodGroupMatchingMasterDto) throws ApplicationException {
		try {
			return iBloodBagMatchingMasterDao.getBloodGroupsByComponentAndDonorBloodGroupId(bloodGroupMatchingMasterDto);
				
			} catch (Exception e) {
				logger.error("getBloodGroupsByComponentAndDonorBloodGroupId failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	
}

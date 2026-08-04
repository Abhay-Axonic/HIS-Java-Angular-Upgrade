package com.param.global.org.common.service;

import com.param.global.service.IUnknownPatientRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.common.dao.IPriorityMasterDao;
import com.param.global.org.common.dto.PriorityMasterDto;
import com.param.global.service.VerifiedIdentityService;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings("rawtypes")
public class PriorityMasterServiceImpl implements IPriorityMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(PriorityMasterServiceImpl.class);


	@Autowired
	IPriorityMasterDao iPriorityMasterDao;

	@Autowired
	private IUnknownPatientRegistrationService iUnknownPatientRegistrationService;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	@Override
	public Response savePriorityMasterMaster(PriorityMasterDto priorityMasterDto) throws ApplicationException {
		// DTO org/unit are never trusted — bind the JWT-verified tenant first
		priorityMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		priorityMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		Response response = iPriorityMasterDao.getPriorityMasterByName(priorityMasterDto);
		try {
			if (response.getListObject() != null && response.getListObject().size() > 0) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				Response saveRes = iPriorityMasterDao.savePriorityMasterMaster(priorityMasterDto);
				if(saveRes.getStatus().equals(SUCCESS)){
					iUnknownPatientRegistrationService.invalidatePriorityMasterCache();
				}
				return saveRes;
			}
		} catch (Exception e) {
		logger.error("savePriorityMasterMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPriorityMasterById(int priorityId, int orgId) throws ApplicationException {
		try {
			// by-id read binds the JWT-verified org AND unit, never the client params
			return iPriorityMasterDao.getPriorityMasterById(priorityId,
					verifiedIdentityService.getVerifiedOrganizationId(),
					verifiedIdentityService.getVerifiedUnitId());
		} catch (Exception e) {
		logger.error("getPriorityMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPriorityMasterList(PriorityMasterDto priorityMasterDto) throws ApplicationException {
		try {
			priorityMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			priorityMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
			return iPriorityMasterDao.getPriorityMasterList(priorityMasterDto);
		} catch (Exception e) {
		logger.error("getPriorityMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updatePriorityMaster(PriorityMasterDto priorityMasterDto) throws ApplicationException {
		priorityMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		priorityMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
		Response response = iPriorityMasterDao.getPriorityMasterByNameNotById(priorityMasterDto);

		try {
			if (response.getListObject() != null && response.getListObject().size() > 0) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				Response res = iPriorityMasterDao.updatePriorityMaster(priorityMasterDto);
				if(res.getStatus().equals(SUCCESS)){
					iUnknownPatientRegistrationService.invalidatePriorityMasterCache();
				}
				return res;
			}

		} catch (Exception e) {
		logger.error("updatePriorityMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updatePriorityMasterStatus(PriorityMasterDto priorityMasterDto) throws ApplicationException {
		try {
			priorityMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			priorityMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
			Response saveRes = iPriorityMasterDao.updatePriorityMasterStatus(priorityMasterDto);
			if(saveRes.getStatus().equals(SUCCESS)){
				iUnknownPatientRegistrationService.invalidatePriorityMasterCache();
			}
			return saveRes;
		} catch (Exception e) {
		logger.error("updatePriorityMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPriorityMasterCount(PriorityMasterDto priorityMasterDto) throws ApplicationException {
		try {
			priorityMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			priorityMasterDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
			return iPriorityMasterDao.getPriorityMasterCount(priorityMasterDto);
		} catch (Exception e) {
		logger.error("getPriorityMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}

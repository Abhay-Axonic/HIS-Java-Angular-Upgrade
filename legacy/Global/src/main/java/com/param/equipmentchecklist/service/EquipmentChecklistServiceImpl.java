package com.param.equipmentchecklist.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.equipmentchecklist.Dto.EquipmentChecklistDto;
import com.param.equipmentchecklist.dao.IEquipmentChecklistDao;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.service.VerifiedIdentityService;
import com.param.otims.model.ChecklistMaster;

import in.param.exception.ApplicationException;

/**
 * BR-121 / docs/DECISION_NEEDED_OrganizationIdNotCryptographicallyVerified.md
 * -- every method here used to pass the client-supplied
 * organizationId/unitId straight through to the DAO, live-confirmed as a
 * cross-tenant IDOR on read, write, and status-toggle (a forged
 * organizationId/unitId in the request body was honored outright). Fixed by
 * overwriting both fields on the incoming DTO with the JWT-verified values
 * before it ever reaches the DAO, at this single shared layer, so all 6
 * endpoints are covered by one change rather than six.
 */
@Service
@SuppressWarnings({"rawtypes"})
public class EquipmentChecklistServiceImpl  implements IEquipmentChecklistService ,ICommonConstants{

	private static final Logger logger = Logger.getLogger(EquipmentChecklistServiceImpl.class);

	@Autowired
	IEquipmentChecklistDao iEquipmentChecklistDao;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	private void applyVerifiedIdentity(EquipmentChecklistDto dto) {
		dto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
		dto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
	}

	@Override
	public Response saveEquipmentChecklist(EquipmentChecklistDto equipmentChecklistDto) throws ApplicationException{
		try {
			applyVerifiedIdentity(equipmentChecklistDto);
			return iEquipmentChecklistDao.saveEquipmentChecklist(equipmentChecklistDto); //added by minesh for response message(save/update/alredy exist) and comment below code.

			/*Response res =  iEquipmentChecklistDao.saveEquipmentChecklist(equipmentChecklistDto);
			if(res.getStatus().equals(SUCCESS)) {
						return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}else {
						return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}*/

	} catch (Exception e) {
		logger.error("Error while saving equipment checklist", e);
	}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getAllEquipmentChecklistMasterList(EquipmentChecklistDto equipmentChecklistDto)
			throws ApplicationException {
		try {
			applyVerifiedIdentity(equipmentChecklistDto);
			return iEquipmentChecklistDao.getAllEquipmentChecklistMasterList(equipmentChecklistDto);

		} catch (Exception e) {
			logger.error("Error while fetching the equipment checklist master list", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateChecklistEquipmentMasterStatus(EquipmentChecklistDto equipmentChecklistDto)
			throws ApplicationException {
		try {
			applyVerifiedIdentity(equipmentChecklistDto);
			return iEquipmentChecklistDao.updateChecklistEquipmentMasterStatus(equipmentChecklistDto);

		} catch (Exception e) {
			logger.error("Error while updating checklist equipment master status", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getEquipmentChecklistNameListById(EquipmentChecklistDto equipmentChecklistDto)
			throws ApplicationException {
		try {
			applyVerifiedIdentity(equipmentChecklistDto);
			return iEquipmentChecklistDao.getEquipmentChecklistNameListById(equipmentChecklistDto);

		} catch (Exception e) {
			logger.error("Error while fetching the equipment checklist name list by id", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllEquipmentChecklistMasterListPaginationList(EquipmentChecklistDto equipmentChecklistDto)
			throws ApplicationException {
		try {
			applyVerifiedIdentity(equipmentChecklistDto);
			return iEquipmentChecklistDao.getAllMealMasterListPaginationList(equipmentChecklistDto);

	} catch (Exception e) {
		logger.error("Error while fetching the equipment checklist master pagination list", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getAllEquipmentChecklistMasterListPaginationListCount(EquipmentChecklistDto equipmentChecklistDto)
			throws ApplicationException {

		try {
			applyVerifiedIdentity(equipmentChecklistDto);
			return iEquipmentChecklistDao.getAllMealMasterListPaginationListCount(equipmentChecklistDto);

		} catch (Exception e) {
			logger.error("Error while fetching the equipment checklist master pagination list count", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	
	
	
	
}

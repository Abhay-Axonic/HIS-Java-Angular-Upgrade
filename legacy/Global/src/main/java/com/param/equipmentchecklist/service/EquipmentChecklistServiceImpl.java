package com.param.equipmentchecklist.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.equipmentchecklist.Dto.EquipmentChecklistDto;
import com.param.equipmentchecklist.dao.IEquipmentChecklistDao;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.otims.model.ChecklistMaster;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes"})
public class EquipmentChecklistServiceImpl  implements IEquipmentChecklistService ,ICommonConstants{

	private static final Logger logger = Logger.getLogger(EquipmentChecklistServiceImpl.class);

	@Autowired
	IEquipmentChecklistDao iEquipmentChecklistDao;

	@Override
	public Response saveEquipmentChecklist(EquipmentChecklistDto equipmentChecklistDto) throws ApplicationException{
		try {
			
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
			return iEquipmentChecklistDao.getAllMealMasterListPaginationListCount(equipmentChecklistDto);

		} catch (Exception e) {
			logger.error("Error while fetching the equipment checklist master pagination list count", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	
	
	
	
}

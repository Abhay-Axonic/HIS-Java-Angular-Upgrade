package com.param.equipmentchecklist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.equipmentchecklist.Dto.EquipmentChecklistMapperDto;
import com.param.equipmentchecklist.dao.IEquipmentChecklistMapperDao;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class EquipmentChecklistMapperServiceImpl  implements IEquipmentChecklistMapperService ,ICommonConstants{
	final static Logger logger = Logger.getLogger(EquipmentChecklistMapperServiceImpl.class);

	
	@Autowired
	IEquipmentChecklistMapperDao iEquipmentChecklistMapperDao;

	@Override
	public Response saveChecklistEquipmentMapperMaster(EquipmentChecklistMapperDto equipmentChecklistMapperDto) throws ApplicationException{
		try {
			
			return iEquipmentChecklistMapperDao.saveChecklistEquipmentMapperMaster(equipmentChecklistMapperDto); //added by minesh for response message(save/update/alredy exist) and comment below code.
			
			/*Response res =  iEquipmentChecklistMapperDao.saveChecklistEquipmentMapperMaster(equipmentChecklistMapperDto);
			if(res.getStatus().equals(SUCCESS)) {
						return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}else {
						return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}*/
			
	} catch (Exception e) {
	logger.error("saveChecklistEquipmentMapperMaster failed", e);
	}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getAllEquipmentChecklistMapperMasterList(EquipmentChecklistMapperDto equipmentChecklistMapperDto)
			throws ApplicationException {
		try {

			return iEquipmentChecklistMapperDao.getAllEquipmentChecklistMapperMasterList(equipmentChecklistMapperDto);

		} catch (Exception e) {
		logger.error("getAllEquipmentChecklistMapperMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateChecklistEquipmentMapperStatus(EquipmentChecklistMapperDto equipmentChecklistMapperDto)
			throws ApplicationException {
		try {

			return iEquipmentChecklistMapperDao.updateChecklistEquipmentMapperStatus(equipmentChecklistMapperDto);

		} catch (Exception e) {
		logger.error("updateChecklistEquipmentMapperStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	/*@Override
	public Response getEquipmentChecklistNameListById(EquipmentChecklistMapperDto equipmentChecklistMapperDto)
			throws ApplicationException {
		try {

			return iEquipmentChecklistMapperDao.getEquipmentChecklistNameListById(equipmentChecklistMapperDto);

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/
	
	
	
	
	
	
}

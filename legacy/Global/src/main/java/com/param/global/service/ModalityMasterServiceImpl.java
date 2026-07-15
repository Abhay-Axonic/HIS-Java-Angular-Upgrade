package com.param.global.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ScheduleActions;
import com.param.global.common.ScheduleTables;
import com.param.global.dao.IModalityMasterDao;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dto.ModalityMasterDto;
import com.param.global.dto.ScheduleLogsDto;
import com.param.global.model.ModalityMaster;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ModalityMasterServiceImpl implements IModalityMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(ModalityMasterServiceImpl.class);


	@Autowired
	IModalityMasterDao iModalityMasterDao;

	@Autowired
	IScheduleLogsDao iScheduleLogsDao;

	@Override
	public Response saveModalityMaster(ModalityMasterDto modalityMasterDto) throws ApplicationException {
		try {
			Response res = iModalityMasterDao.getModalityByName(modalityMasterDto);
			if (res.getListObject() != null && res.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				Response response = iModalityMasterDao.saveModalityMaster(modalityMasterDto);
				//modality Entry in log table start
				if (response.getStatus().equalsIgnoreCase(SUCCESS) && response.getObject() != null) {
					ModalityMaster modalityMaster = (ModalityMaster) response.getObject();
					String [] tableName=modalityMaster.getClass().getName().split("\\.");
					//scheduleLogsDto.setTableName(tableName[tableName.length-1]);
				}
				//modality Entry in log table end
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
		logger.error("saveModalityMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getModalityMasterList(ModalityMasterDto modalityMasterDto) throws ApplicationException {
		try {
			return iModalityMasterDao.getModalityMasterList(modalityMasterDto);
		} catch (Exception e) {
		logger.error("getModalityMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateModalityMaster(ModalityMasterDto modalityMasterDto) throws ApplicationException {
		try {
			Response res = iModalityMasterDao.getModalityByNameNotId(modalityMasterDto);
			if (res.getListObject() != null && res.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				iModalityMasterDao.updateModalityMaster(modalityMasterDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
		logger.error("updateModalityMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForModality(ModalityMasterDto modalityMasterDto) throws ApplicationException {
		try {
			Response res = iModalityMasterDao.getModalityById(modalityMasterDto);
			if (res.getListObject() != null && res.getListObject().size() > 0) {
				iModalityMasterDao.updateModalityStatus(modalityMasterDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			}
		} catch (Exception e) {
		logger.error("updateStatusForModality failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveModalityList(ModalityMasterDto modalityMasterDto) throws ApplicationException {
		try {
			return iModalityMasterDao.getActiveModalityList(modalityMasterDto);
		} catch (Exception e) {
		logger.error("getActiveModalityList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getModalityCount(ModalityMasterDto modalityMasterDto) throws ApplicationException {
		try {
			return iModalityMasterDao.getModalityCount(modalityMasterDto);
		} catch (Exception e) {
		logger.error("getModalityCount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getModalityById(ModalityMasterDto modalityMasterDto) throws ApplicationException {
		try {
			return iModalityMasterDao.getModalityById(modalityMasterDto);
		} catch (Exception e) {
		logger.error("getModalityById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getModalityBySubSpecialityId(ModalityMasterDto modalityMasterDto) throws ApplicationException {
		try {
			return iModalityMasterDao.getModalityBySubSpecialityId(modalityMasterDto);
		} catch (Exception e) {
		logger.error("getModalityBySubSpecialityId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getModalityBySubSpecialityArray(ModalityMasterDto modalityMasterDto) throws ApplicationException {
		try {
			return iModalityMasterDao.getModalityBySubSpecialityArray(modalityMasterDto);
		} catch (Exception e) {
		logger.error("getModalityBySubSpecialityArray failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getEquipments(Integer unitId) throws ApplicationException {
		try {
			return iModalityMasterDao.getEquipments(unitId);
		} catch (Exception e) {
		logger.error("getEquipments failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getEquipmentsChargable(Integer unitId,Integer productCategoryId) throws ApplicationException {
		try {
			return iModalityMasterDao.getEquipmentsChargable(unitId,productCategoryId);
		} catch (Exception e) {
		logger.error("getEquipmentsChargable failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

}

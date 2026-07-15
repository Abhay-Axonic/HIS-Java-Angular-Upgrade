package com.param.otims.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.ChecklistItemMasterDto;
import com.param.otims.dao.IChecklistItemMasterDao;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class ChecklistItemMasterServiceImpl implements IChecklistItemMstrService ,ICommonConstants {

	private static final Logger logger = Logger.getLogger(ChecklistItemMasterServiceImpl.class);

	@Autowired
	IChecklistItemMasterDao iChecklistItemMasterDao;

	@Override
	public Response saveChecklistItemMaster(ChecklistItemMasterDto checklistItemMasterdto) throws ApplicationException {
		try {

			return iChecklistItemMasterDao.saveChecklistItemMaster(checklistItemMasterdto);

	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}

	}

	@Override
	public Response updateChecklistItemMasterStatus(ChecklistItemMasterDto checklistItemMasterdto)
			throws ApplicationException {
		try {

			return iChecklistItemMasterDao.updateChecklistItemMasterStatus(checklistItemMasterdto);

	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response getChecklistItemMasterList(ChecklistItemMasterDto checklistItemMasterdto)
			throws ApplicationException {
		try {

			Response res =  iChecklistItemMasterDao.getChecklistItemMasterList(checklistItemMasterdto);
			if(res.getStatus().equals(SUCCESS)) {
				Response res1 = iChecklistItemMasterDao.getChecklistItemMasterListCount(checklistItemMasterdto);
				if(res1.getStatus().equals(SUCCESS)) {
					res.setObject(res1.getObject());
				}
				return res;
			}
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response searchChecklistItemName(ChecklistItemMasterDto checklistItemMasterdto) throws ApplicationException {
		try {

			return iChecklistItemMasterDao.searchChecklistItemName(checklistItemMasterdto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}

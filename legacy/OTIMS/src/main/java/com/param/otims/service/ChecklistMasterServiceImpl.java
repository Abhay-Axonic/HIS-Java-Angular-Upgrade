package com.param.otims.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.ChecklistMasterDetailsDto;
import com.param.global.otims.dto.ChecklistMasterDto;
import com.param.otims.dao.IChecklistMasterDao;
import com.param.otims.dao.IChecklistMasterDetailsDao;
import com.param.otims.model.ChecklistMaster;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class ChecklistMasterServiceImpl implements IChecklistMasterService ,ICommonConstants {

	private static final Logger logger = Logger.getLogger(ChecklistMasterServiceImpl.class);

	@Autowired
	IChecklistMasterDao iChecklistMasterDao;
	
	@Autowired
	IChecklistMasterDetailsDao iChecklistMasterDetailsDao;

	@Override
	public Response saveChecklistMaster(ChecklistMasterDto checklistMasterDto) throws ApplicationException {
		try {
			
			Response res =  iChecklistMasterDao.saveChecklistMaster(checklistMasterDto);
			if(res.getObject()!=null)
			{
				ChecklistMaster mstr = (ChecklistMaster)res.getObject();
				
				Response res1 =  iChecklistMasterDetailsDao.updateChecklistMasterDetails(mstr.getChecklistMasterId());
				if(res1.getStatus().equals(SUCCESS)) {
				
					Response res2 = iChecklistMasterDetailsDao.saveChecklistMasterDetails(checklistMasterDto.getChecklistMasterDetails(),mstr.getChecklistMasterId());
					
					if(res2.getStatus().equals(SUCCESS)) {
					
						if(checklistMasterDto.getChecklistMasterId()!=null) {
							return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
						}else {
						return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
						}
					}else {
						
						return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
					}
				
				}else {
					return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
				}
			}else {

				return res;
			}

	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}

	}

	@Override
	public Response updateChecklistMasterStatus(ChecklistMasterDto checklistMasterDto) throws ApplicationException {
		try {

			return iChecklistMasterDao.updateChecklistMasterStatus(checklistMasterDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAllChecklistMasterList(ChecklistMasterDto checklistMasterDto) throws ApplicationException {
		try {

			Response res =  iChecklistMasterDao.getAllChecklistMasterList(checklistMasterDto);
			if(res.getStatus().equals(SUCCESS)) {
				Response res1 =  iChecklistMasterDao.getAllChecklistMasterListCount(checklistMasterDto);
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
	public Response getChecklistMasterDetailsByMasterId(ChecklistMasterDetailsDto dto) throws ApplicationException {
		try {

			return iChecklistMasterDetailsDao.getChecklistMasterDetailsByMasterId(dto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getChecklistMasterByStageId(ChecklistMasterDto dto) throws ApplicationException {
		try {

			return iChecklistMasterDao.getChecklistMasterByStageId(dto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}

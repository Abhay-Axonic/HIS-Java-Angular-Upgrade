package com.param.global.unit.service;

import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dao.IActionMasterDaoGlobal;
import com.param.global.unit.dto.ActionMasterDto;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes"})
@Service
public class ActionMasterServiceGlobalImpl implements IActionMasterServiceGlobal, ICommonConstants{
	final static Logger logger = Logger.getLogger(ActionMasterServiceGlobalImpl.class);


	@Autowired
	IActionMasterDaoGlobal iActionMasterDao;
	
	@Override
	public Response saveActionMaster(ActionMasterDto actionMasterDto) throws ApplicationException {
		try {
			Response response=iActionMasterDao.getActionMasterByName(actionMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				if(actionMasterDto.getIsChild()=='Y'){
//				    	Response response1=iActionMasterDao.getActionId(actionMasterDto);
//						actionMasterDto.setParentId((Integer)response1.getObject());
					
					}else{
						actionMasterDto.setParentId(0);
					}
				    
				return iActionMasterDao.saveActionMaster(actionMasterDto);
			}
		} catch (Exception e) {
		logger.error("saveActionMaster failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@Override
	public Response updateActionMaster(ActionMasterDto actionMasterDto) throws ApplicationException {
		try {
			Response response=iActionMasterDao.getActionMasterByNameNotId(actionMasterDto);
			
			if(response.getListObject()!=null && response.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				if(actionMasterDto.getIsChild()=='Y'){
					
		    	//Response response1=iActionMasterDao.getActionId(actionMasterDto);
				//actionMasterDto.setParentId((Integer)response1.getObject());
					actionMasterDto.setParentId(actionMasterDto.getParentId());
			}else{
				actionMasterDto.setParentId(0);
			}
				return iActionMasterDao.updateActionMaster(actionMasterDto);
			}
		} catch (Exception e) {
		logger.error("updateActionMaster failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response updateActionMasterStatus(ActionMasterDto actionMasterDto) throws ApplicationException {
		try {
			
			return iActionMasterDao.updateActionMasterStatus(actionMasterDto);
			
		} catch (Exception e) {
		logger.error("updateActionMasterStatus failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@Override
	public Response getActionMasterList(Integer offset, Integer noOfRecordsPerPage) throws ApplicationException {
		try {
			
			return iActionMasterDao.getActionMasterList(offset, noOfRecordsPerPage);
			
		} catch (Exception e) {
		logger.error("getActionMasterList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@Override
	public Response getActionMasterById(Integer actionId) throws ApplicationException {
		try {
			
			return iActionMasterDao.getActionMasterById(actionId);
			
		} catch (Exception e) {
		logger.error("getActionMasterById failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@Override
	public Response getActionMasterCount() throws ApplicationException {
		try {
			
			return iActionMasterDao.getActionMasterCount();
			
		} catch (Exception e) {
		logger.error("getActionMasterCount failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response getActiveActionMaster() throws ApplicationException {
		try {
			
			return iActionMasterDao.getActiveActionMaster();
			
		} catch (Exception e) {
		logger.error("getActiveActionMaster failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@Override
	public Response getActionMasterByName(ActionMasterDto actionMasterDto) throws ApplicationException {
		try {
			
			return iActionMasterDao.getActionMasterByName(actionMasterDto);
			
		} catch (Exception e) {
		logger.error("getActionMasterByName failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@Override
	public Response getActionMasterByNameNotId(ActionMasterDto actionMasterDto) throws ApplicationException {
		try {
			
			return iActionMasterDao.getActionMasterByNameNotId(actionMasterDto);
			
		} catch (Exception e) {
		logger.error("getActionMasterByNameNotId failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getActionId(ActionMasterDto actionMasterDto) throws ApplicationException {
	try {
			
			return iActionMasterDao.getActionId(actionMasterDto);
			
		} catch (Exception e) {
		logger.error("getActionId failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response searchActionMasterByParentActionName(ActionMasterDto actionMasterDto) throws ApplicationException {
		try {
			
			return iActionMasterDao.searchActionMasterByParentActionName(actionMasterDto);
			
		} catch (Exception e) {
		logger.error("searchActionMasterByParentActionName failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getParentId() throws ApplicationException {
    try 
       {
			return iActionMasterDao.getParentId();
			
		} catch (Exception e) {
		logger.error("getParentId failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	
	}

}

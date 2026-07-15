package com.param.pharmacy.qms.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.pharmacy.qms.dao.IQMSCounterDao;
import com.param.pharmacy.qms.dao.IQMSCounterMasterDao;
import com.param.pharmacy.qms.dto.QMSCounterMasterDto;
import com.param.qms.model.QMSCounter;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings("rawtypes")
public class QMSCounterMasterServiceImpl implements IQMSCounterMasterService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(QMSCounterMasterServiceImpl.class);

	@Autowired
	IQMSCounterMasterDao iQMSCounterMasterDao;
	
	@Autowired
	IQMSCounterDao iQMSCounterDao;
	
	@Override
	public Response saveQMSCounterMaster(QMSCounterMasterDto dto) throws ApplicationException {
		try {
			Response res=iQMSCounterMasterDao.getQMSCounterMasterListByName(dto);
			if(res.getListObject()!=null && res.getListObject().size()>0){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				
				Response resCounter= iQMSCounterDao.saveQMSCounter(dto);
				
				if(resCounter.getObject() != null) {
					
					QMSCounter master = (QMSCounter)resCounter.getObject();
					
					for (QMSCounterMasterDto qmsDeptMasterId : dto.getDepartmentList()) {

						dto.setQmsDeptMasterId(qmsDeptMasterId.getId());
						dto.setQmsCounterId(master.getQmsCounterId());
						
						 iQMSCounterMasterDao.saveQMSCounterMaster(dto);
					}
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
				}else {
					return new Response<>(ERROR, ERROR_CODE, null, null, null);
				}
				
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateQMSCounterMaster(QMSCounterMasterDto dto) throws ApplicationException {
		try {
			/*
			 * Response res=iQMSCounterMasterDao.getQMSCounterMasterListByNameNotById(dto);
			 * if(res.getListObject()!=null && res.getListObject().size()>0){ return new
			 * Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null); }else{
			 */
				 iQMSCounterDao.saveQMSCounter(dto);
				 	iQMSCounterDao.inactiveMaster(dto);
				
					for (QMSCounterMasterDto qmsDeptMasterId : dto.getDepartmentList()) {

						dto.setQmsDeptMasterId(qmsDeptMasterId.getId());
						dto.setQmsCounterId(dto.getQmsCounterId());
						
						 iQMSCounterMasterDao.saveQMSCounterMaster(dto);
					}
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				
			//}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateQMSCounterMasterStatus(QMSCounterMasterDto dto) throws ApplicationException {
		try {
			
			 		 iQMSCounterDao.updateQMSCounterStatus(dto);
			
			 return  iQMSCounterMasterDao.updateQMSCounterMasterStatus(dto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getQMSCounterMasterList(QMSCounterMasterDto dto) throws ApplicationException {
		try {
			return iQMSCounterMasterDao.getQMSCounterMasterList(dto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getQMSCounterMasterCount(QMSCounterMasterDto dto) throws ApplicationException {
		try {
			return iQMSCounterMasterDao.getQMSCounterMasterCount(dto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getQMSCounterMasterById(QMSCounterMasterDto dto) throws ApplicationException {
		try {
			return iQMSCounterMasterDao.getQMSCounterMasterById(dto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getQMSCounterListByDeptId(QMSCounterMasterDto dto) throws ApplicationException {
		try {
			return iQMSCounterMasterDao.getQMSCounterListByDeptId(dto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}

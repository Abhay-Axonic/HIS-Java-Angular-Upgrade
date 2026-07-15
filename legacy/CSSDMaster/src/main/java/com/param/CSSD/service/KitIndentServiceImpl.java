package com.param.CSSD.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.CSSD.dao.IKitIndentDao;
import com.param.CSSD.dto.KitIndentDto;
import com.param.CSSD.dto.KitIndentKitMapperDto;
import com.param.global.common.Response;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class KitIndentServiceImpl implements IKitIndentService {

	final static Logger logger = Logger.getLogger(KitIndentServiceImpl.class);


	@Autowired
	private IKitIndentDao iKitIndentDao;

	@Override
	@Transactional
	public Response createKitIndent(KitIndentDto kitIndentDto) {
		try {
			return iKitIndentDao.createKitIndent(kitIndentDto);
		} catch (Exception e) {
			logger.error("createKitIndent failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response updateKitIndent(KitIndentDto kitIndentDto) {
		try {
			return iKitIndentDao.updateKitIndent(kitIndentDto);
		} catch (Exception e) {
			logger.error("updateKitIndent failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	@Override
	@Transactional
	public Response updateKitIndentStatus(List<KitIndentDto> kitIndentDtoList) {
		try {
			return iKitIndentDao.updateKitIndentStatus(kitIndentDtoList);
		} catch (Exception e) {
			logger.error("updateKitIndentStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response cancelKitIndent(KitIndentDto kitIndentDto) {
		try {
			return iKitIndentDao.cancelKitIndent(kitIndentDto);
		} catch (Exception e) {
			logger.error("cancelKitIndent failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getKitIndentList(Integer organizationId,Integer unitId) {
		try {
			return iKitIndentDao.getKitIndentList(organizationId,unitId);
		} catch (Exception e) {
			logger.error("getKitIndentList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getKitListByIndentNo(Integer organizationId,Integer unitId,Integer indentNo) {
		try {
			return iKitIndentDao.getKitListByIndentNo(organizationId,unitId,indentNo);
		} catch (Exception e) {
			logger.error("getKitListByIndentNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getKitIndentByIndentNo(Integer organizationId,Integer unitId,Integer indentNo) {
		try {
			return iKitIndentDao.getKitIndentByIndentNo(organizationId,unitId,indentNo);
		} catch (Exception e) {
			logger.error("getKitIndentByIndentNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response approveKit(KitIndentKitMapperDto kitMapperDto) {
		try {
			return iKitIndentDao.approveKit(kitMapperDto);
		} catch (Exception e) {
			logger.error("approveKit failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response rejectKit(KitIndentKitMapperDto kitMapperDto) {
		try {
			return iKitIndentDao.rejectKit(kitMapperDto);
		} catch (Exception e) {
			logger.error("rejectKit failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	@Override
	@Transactional
	public Response getKitIndentByMultipleFields(Date fromDate,Date toDate,Integer fromStore,Integer toStore,Integer indentNo,String status,Integer organizationId,Integer unitId) {
		try {
			return iKitIndentDao.getKitIndentByMultipleFields(fromDate,toDate,fromStore,toStore,indentNo,status,organizationId,unitId);
		} catch (Exception e) {
			logger.error("getKitIndentByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getKitIndentApprovalByMultipleFields(Date fromDate,Date toDate,Integer fromStore,Integer toStore,Integer indentNo,String status,Integer organizationId,Integer unitId) {
		try {
			return iKitIndentDao.getKitIndentApprovalByMultipleFields(fromDate,toDate,fromStore,toStore,indentNo,status,organizationId,unitId);
		} catch (Exception e) {
			logger.error("getKitIndentApprovalByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getKitIndentFromStore(Integer organizationId,Integer unitId,Integer userId) {
		try {
			return iKitIndentDao.getKitIndentFromStore(organizationId,unitId,userId);
		} catch (Exception e) {
			logger.error("getKitIndentFromStore failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getCssdStoreListByUserId(Integer organizationId,Integer unitId,Integer userId) {
		try {
			return iKitIndentDao.getCssdStoreListByUserId(organizationId,unitId,userId);
		} catch (Exception e) {
			logger.error("getCssdStoreListByUserId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getKitIndentToStore(Integer organizationId,Integer unitId) {
		try {
			return iKitIndentDao.getKitIndentToStore(organizationId,unitId);
		} catch (Exception e) {
			logger.error("getKitIndentToStore failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getPriorityList(Integer organizationId,Integer unitId) {
		try {
			return iKitIndentDao.getPriorityList(organizationId,unitId);
		} catch (Exception e) {
			logger.error("getPriorityList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
}
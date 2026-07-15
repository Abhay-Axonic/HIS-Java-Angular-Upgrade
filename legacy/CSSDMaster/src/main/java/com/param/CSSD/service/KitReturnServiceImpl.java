package com.param.CSSD.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.Response;
import com.param.CSSD.dao.IKitReturnDao;
import com.param.CSSD.dto.KitReturnDto;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class KitReturnServiceImpl implements IKitReturnService {

	final static Logger logger = Logger.getLogger(KitReturnServiceImpl.class);


	@Autowired
	private IKitReturnDao iKitReturnDao;

	
	@Override
	@Transactional
	public Response getKitListForKitReturn(Integer organizationId,Integer unitId,Integer fromStoreId,Integer toStoreId,String name) {
		try {
			return iKitReturnDao.getKitListForKitReturn(organizationId,unitId,fromStoreId,toStoreId,name);
		} catch (Exception e) {
			logger.error("getKitListForKitReturn failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response createKitReturn(KitReturnDto kitReturnDto) {
		try {
			return iKitReturnDao.createKitReturn(kitReturnDto);
		} catch (Exception e) {
			logger.error("createKitReturn failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getKitReturnByMultipleFields(Date fromDate,Date toDate,Integer fromStore,Integer toStore,Integer returnNo,String status,Integer organizationId,Integer unitId) {
		try {
			return iKitReturnDao.getKitReturnByMultipleFields(fromDate,toDate,fromStore,toStore,returnNo,status,organizationId,unitId);
		} catch (Exception e) {
			logger.error("getKitReturnByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getKitReturnByReturnNo(Integer returnNo,Integer organizationId,Integer unitId) {
		try {
			return iKitReturnDao.getKitReturnByReturnNo(returnNo,organizationId,unitId);
		} catch (Exception e) {
			logger.error("getKitReturnByReturnNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
		
	@Override
	@Transactional
	public Response updateKitReturn(KitReturnDto kitReturnDto) {
		try {
			return iKitReturnDao.updateKitReturn(kitReturnDto);
		} catch (Exception e) {
			logger.error("updateKitReturn failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response updateKitReturnStatus(List<KitReturnDto> kitReturnDtoList ) {
		try {
			return iKitReturnDao.updateKitReturnStatus(kitReturnDtoList);
		} catch (Exception e) {
			logger.error("updateKitReturnStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	@Override
	@Transactional
	public Response cancelKitReturn(KitReturnDto kitReturnDto ) {
		try {
			return iKitReturnDao.cancelKitReturn(kitReturnDto);
		} catch (Exception e) {
			logger.error("cancelKitReturn failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	/*-----------------------------------------------------------------------------------------------------------------------------------------------------------------*/	
	
	
	
	
	
	
	
	
	
	
	@Override
	@Transactional
	public Response getKitReturnList() {
		try {
			return iKitReturnDao.getKitReturnList();
		} catch (Exception e) {
			logger.error("getKitReturnList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getKitListByReturnNo(Integer ReturnNo) {
		try {
			return iKitReturnDao.getKitListByReturnNo(ReturnNo);
		} catch (Exception e) {
			logger.error("getKitListByReturnNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
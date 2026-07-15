package com.param.CSSD.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.CSSD.dao.ISterilizationRequestDao;
import com.param.CSSD.dto.SterilizationRequestDto;
import com.param.global.common.Response;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SterilizationRequestServiceImpl implements ISterilizationRequestService {

	final static Logger logger = Logger.getLogger(SterilizationRequestServiceImpl.class);


	@Autowired
	private ISterilizationRequestDao iSterilizationRequestDao;

	
	@Override
	@Transactional
	public Response getKitListForSterilizationRequest(Integer organizationId,Integer unitId,Integer requestingStoreId,Integer issueingStoreId,String name) {
		try {
			return iSterilizationRequestDao.getKitListForSterilizationRequest(organizationId,unitId,requestingStoreId,issueingStoreId,name);
		} catch (Exception e) {
			logger.error("getKitListForSterilizationRequest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response createSterilizationRequest(SterilizationRequestDto sterilizationRequestDto) {
		try {
			return iSterilizationRequestDao.createSterilizationRequest(sterilizationRequestDto);
		} catch (Exception e) {
			logger.error("createSterilizationRequest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getSterilizationRequestByMultipleFields(Date fromDate,Date toDate,Integer requestingStoreId,Integer issueingStoreId,Integer requestNo,String status,Integer organizationId,Integer unitId) {
		try {
			return iSterilizationRequestDao.getSterilizationRequestByMultipleFields(fromDate,toDate,requestingStoreId,issueingStoreId,requestNo,status,organizationId,unitId);
		} catch (Exception e) {
			logger.error("getSterilizationRequestByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getSterilizationRequestForAcceptanceByMultipleFields(Date fromDate,Date toDate,Integer requestingStoreId,Integer issueingStoreId,Integer requestNo,String status,Integer organizationId,Integer unitId) {
		try {
			return iSterilizationRequestDao.getSterilizationRequestForAcceptanceByMultipleFields(fromDate,toDate,requestingStoreId,issueingStoreId,requestNo,status,organizationId,unitId);
		} catch (Exception e) {
			logger.error("getSterilizationRequestForAcceptanceByMultipleFields failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getSterilizationRequestByRequestNo(Integer requestNo,Integer organizationId,Integer unitId) {
		try {
			return iSterilizationRequestDao.getSterilizationRequestByRequestNo(requestNo,organizationId,unitId);
		} catch (Exception e) {
			logger.error("getSterilizationRequestByRequestNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
		
	@Override
	@Transactional
	public Response updateSterilizationRequest(SterilizationRequestDto sterilizationRequestDto ) {
		try {
			return iSterilizationRequestDao.updateSterilizationRequest(sterilizationRequestDto);
		} catch (Exception e) {
			logger.error("updateSterilizationRequest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response updateSterilizationRequestStatus(List<SterilizationRequestDto> sterilizationRequestDtoList ) {
		try {
			return iSterilizationRequestDao.updateSterilizationRequestStatus(sterilizationRequestDtoList);
		} catch (Exception e) {
			logger.error("updateSterilizationRequestStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	@Override
	@Transactional
	public Response cancelSterilizationRequest(SterilizationRequestDto sterilizationRequestDto ) {
		try {
			return iSterilizationRequestDao.cancelSterilizationRequest(sterilizationRequestDto);
		} catch (Exception e) {
			logger.error("cancelSterilizationRequest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	@Override
	@Transactional
	public Response acceptSterilizationRequest(SterilizationRequestDto sterilizationRequestDto ) {
		try {
			return iSterilizationRequestDao.acceptSterilizationRequest(sterilizationRequestDto);
		} catch (Exception e) {
			logger.error("acceptSterilizationRequest failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	/*-----------------------------------------------------------------------------------------------------------------------------------------------------------------*/	
	
	
	
	
	
	
	
	
	
	
	@Override
	@Transactional
	public Response getKitReturnList() {
		try {
			return iSterilizationRequestDao.getKitReturnList();
		} catch (Exception e) {
			logger.error("getKitReturnList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public Response getKitListByReturnNo(Integer ReturnNo) {
		try {
			return iSterilizationRequestDao.getKitListByReturnNo(ReturnNo);
		} catch (Exception e) {
			logger.error("getKitListByReturnNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
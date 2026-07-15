package com.param.bloodBank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.bloodBank.dao.IBloodBagIssueAcceptanceDao;
import com.param.bloodBank.dto.BloodBagIssueAcceptanceDto;
import com.param.bloodBank.dto.IntraTransfusionMonitoringMapper;
import com.param.bloodBank.dto.TransfusionDetailsMasterDto;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class BloodBagIssueAcceptanceServiceImpl implements IBloodBagIssueAcceptanceService,ICommonConstants,IBillingCommonConstants{

	final static Logger logger = Logger.getLogger(BloodBagIssueAcceptanceServiceImpl.class);


	
	@Autowired
	IBloodBagIssueAcceptanceDao iBloodBagIssueAcceptanceDao;
	
	@Override
	public Response saveBloodBagIssueAcceptance(List<BloodBagIssueAcceptanceDto> bloodBagIssueAcceptanceDto)
			throws ApplicationException {
		try {
			return iBloodBagIssueAcceptanceDao.saveBloodBagIssueAcceptance(bloodBagIssueAcceptanceDto);
		
	} catch (Exception e) {
		logger.error("saveBloodBagIssueAcceptance failed", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response getListOnPendingTransfusion(BloodBagIssueAcceptanceDto bloodBagIssueAcceptanceDto)
			throws ApplicationException {
		try {
			return iBloodBagIssueAcceptanceDao.getListOnPendingTransfusion(bloodBagIssueAcceptanceDto);
		
	} catch (Exception e) {
		logger.error("getListOnPendingTransfusion failed", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	@Transactional
	public Response saveTransfusionDetails(TransfusionDetailsMasterDto transfusionDetailsMasterDto)
			throws ApplicationException {
		try {
			
			Response res=iBloodBagIssueAcceptanceDao.saveTransfusionDetails(transfusionDetailsMasterDto);
			if(res.getObject()!=null ) {
				transfusionDetailsMasterDto=(TransfusionDetailsMasterDto)res.getObject();
				Response response= iBloodBagIssueAcceptanceDao.saveIntraTransfusionMonitoringMapper(transfusionDetailsMasterDto);

				return response;
			}
			
		} catch (Exception e) {
			throw e;
//			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getTransfusionDetailsList(Integer patientId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			TransfusionDetailsMasterDto response = iBloodBagIssueAcceptanceDao.getTransfusionDetailsList(patientId,organizationId,unitId);
			List<IntraTransfusionMonitoringMapper> intraList =  iBloodBagIssueAcceptanceDao.getintraTransfusionDetailsList(patientId,organizationId,unitId);
			response.setIntraTransfusionMonitoringMapperlist(intraList);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, response);		
			
			
	} catch (Exception e) {
		throw e;
	}
//	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}

package com.param.opd.coversheet.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.param.exception.ApplicationException;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dao.IPatientFamilyHistoryDao;
import com.param.opd.coversheet.dto.PatientFamilyHistoryDto;
import com.param.opd.coversheet.model.PatientFamilyHistory;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
public class patientFamilyHistoryServiceImpl implements IpatientFamilyHistoryService , ICommonConstants {
	final static Logger logger = Logger.getLogger(patientFamilyHistoryServiceImpl.class);


	@Autowired
	private IPatientFamilyHistoryDao iPatientFamilyHistoryDao;
	
	@Override
	@Transactional
	public Response savePatientFamilyHistory(PatientFamilyHistoryDto patientFamilyHistoryDto)throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientFamilyHistoryDao.savePatientFamilyHistory(patientFamilyHistoryDto);
	}

	

	@Override
	@Transactional
	public Response getOldPatientFamilyHistory(
			PatientFamilyHistoryDto patientFamilyHistoryDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientFamilyHistoryDao.getOldPatientFamilyHistory(patientFamilyHistoryDto);
	}

	@Override
	@Transactional
	public Response getCurrentPatientFamilyHistory(
			PatientFamilyHistoryDto patientFamilyHistoryDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientFamilyHistoryDao.getCurrentPatientFamilyHistory(patientFamilyHistoryDto);
	}



	@Override
	@Transactional
	public Response updatePatientFamilyHistory(
			PatientFamilyHistoryDto patientFamilyHistoryDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientFamilyHistoryDao.updatePatientFamilyHistory(patientFamilyHistoryDto);
	}



	@Override
	@Transactional
	public Response updateStatusEnterInError(
			PatientFamilyHistoryDto patientFamilyHistoryDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientFamilyHistoryDao.updateStatusEnterInError(patientFamilyHistoryDto);
	}



	@Override
	@Transactional
	public Response updateSaveNurseReviewedFlag(
			PatientFamilyHistory patientFamilyHistory)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientFamilyHistoryDao.updateSaveNurseReviewedFlag(patientFamilyHistory);
	}



	@Override
	public Response getCurrentPatientFamilyHistoryForCoversheet(PatientFamilyHistoryDto patientFamilyHistoryDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientFamilyHistoryDao.getCurrentPatientFamilyHistoryForCoversheet(patientFamilyHistoryDto);
	}



	@Override
	public Response getPatientFamilyHistoryForBookView(PatientFamilyHistoryDto patientFamilyHistoryDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientFamilyHistoryDao.getPatientFamilyHistoryForBookView(patientFamilyHistoryDto);
	}



	@Override
	public Response updateStatusRemoveFamilyHistory(PatientFamilyHistoryDto patientFamilyHistoryDto)
			throws ApplicationException {
		try {
			
			return iPatientFamilyHistoryDao.updateStatusRemoveFamilyHistory(patientFamilyHistoryDto);
	
	} catch (Exception e) {
	logger.error("updateStatusRemoveFamilyHistory failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



	@Override
	public Response getPatientFamilyHistory(PatientFamilyHistoryDto patientFamilyHistoryDto)
			throws ApplicationException {
		try {
			return iPatientFamilyHistoryDao.getPatientFamilyHistory(patientFamilyHistoryDto);
		} catch (Exception e) {
		logger.error("getPatientFamilyHistory failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}

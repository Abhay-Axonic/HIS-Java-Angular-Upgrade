package com.param.opd.coversheet.service;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.param.exception.ApplicationException;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.opd.coversheet.dao.IPatientMedicalHistoryDao;
import com.param.opd.coversheet.dto.PatientMedicalHistoryDto;
import com.param.opd.coversheet.model.PatientMedicalHistory;

@Service
@SuppressWarnings("rawtypes")
public class PatientMedicalHistoryServiceImpl implements IPatientMedicalHistoryService ,ICommonConstants {

	private static final Logger logger = Logger.getLogger(PatientMedicalHistoryServiceImpl.class);

	@Autowired
	private IPatientMedicalHistoryDao iPatientMedicalHistoryDao;

	
	@Override
	@Transactional
	public Response savePatientMedicalHistory(PatientMedicalHistoryDto patientMedicalHistoryDto)throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientMedicalHistoryDao.savePatientMedicalHistory(patientMedicalHistoryDto);
	}

	
	@Override
	@Transactional
	public Response getOldPatientMedicalHistory(
			PatientMedicalHistoryDto patientMedicalHistoryDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientMedicalHistoryDao.getOldPatientMedicalHistory(patientMedicalHistoryDto);
	}

	@Override
	@Transactional
	public Response getCurrentPatientMedicalHistory(
			PatientMedicalHistoryDto patientMedicalHistoryDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientMedicalHistoryDao.getCurrentPatientMedicalHistory(patientMedicalHistoryDto);
	}

	@Override
	@Transactional
	public Response updatePatientMedicalHistory(
			PatientMedicalHistoryDto patientMedicalHistoryDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientMedicalHistoryDao.updatePatientMedicalHistory(patientMedicalHistoryDto);
	}

	@Override
	@Transactional
	public Response updateSaveNurseReviewedFlag(
			PatientMedicalHistory patientMedicalHistory)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientMedicalHistoryDao.updateSaveNurseReviewedFlag(patientMedicalHistory);
	}

	@Override
	@Transactional
	public Response updateStatusEnterInError(
			PatientMedicalHistoryDto patientMedicalHistoryDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientMedicalHistoryDao.updateStatusEnterInError(patientMedicalHistoryDto);
	}


	@Override
	public Response getCurrentPatientMedicalHistoryDetailsForCoversheet(
			PatientMedicalHistoryDto patientMedicalHistoryDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientMedicalHistoryDao.getCurrentPatientMedicalHistoryDetailsForCoversheet(patientMedicalHistoryDto);
	}


	@Override
	public Response getPatientMedicalHistoryForBookView(PatientMedicalHistoryDto patientMedicalHistoryDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientMedicalHistoryDao.getPatientMedicalHistoryForBookView(patientMedicalHistoryDto);
	}


	@Override
	public Response updateStatusRemoveMedicalHistory(PatientMedicalHistoryDto patientMedicalHistoryDto)
			throws ApplicationException {
		try {

			return iPatientMedicalHistoryDao.updateStatusRemoveMedicalHistory(patientMedicalHistoryDto);

	} catch (Exception e) {
		logger.error("updateStatusRemoveMedicalHistory failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getPatientDataForBookView(PatientMedicalHistoryDto patientMedicalHistoryDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientMedicalHistoryDao.getPatientDataForBookView(patientMedicalHistoryDto);
	}


	@Override
	public Response getPatientMedicalHistory(PatientMedicalHistoryDto patientMedicalHistoryDto)
			throws ApplicationException {
		return iPatientMedicalHistoryDao.getPatientMedicalHistory(patientMedicalHistoryDto);
	}

}

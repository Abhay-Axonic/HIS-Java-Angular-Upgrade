package com.param.opd.coversheet.service;

import javax.transaction.Transactional;

import in.param.exception.ApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dao.IPatientSurgicalHistoryDao;
import com.param.opd.coversheet.dto.PatientSurgicalHistoryDto;
import com.param.opd.coversheet.model.PatientFamilyHistory;
import com.param.opd.coversheet.model.PatientSurgicalHistory;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
public class PatientSurgicalHistoryServiceImpl implements IPatientSurgicalHistoryService , ICommonConstants{
	final static Logger logger = Logger.getLogger(PatientSurgicalHistoryServiceImpl.class);


	@Autowired
	private IPatientSurgicalHistoryDao iPatientSurgicalHistoryDao;
	
	@Override
	@Transactional
	public Response savePatientSurgicalHistory(PatientSurgicalHistoryDto patientSurgicalHistoryDto)throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientSurgicalHistoryDao.savePatientSurgicalHistory(patientSurgicalHistoryDto);
	}

	
	@Override
	@Transactional
	public Response getOldListOfPatientSurgicalHistory(
			PatientSurgicalHistoryDto patientSurgicalHistoryDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientSurgicalHistoryDao.getOldListOfPatientSurgicalHistory(patientSurgicalHistoryDto);
	}

	@Override
	@Transactional
	public Response getCurrentListOfPatientSurgicalHistory(
			PatientSurgicalHistoryDto patientSurgicalHistoryDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientSurgicalHistoryDao.getCurrentListOfPatientSurgicalHistory(patientSurgicalHistoryDto);
	}


	@Override
	@Transactional
	public Response updatePatientSurgicalHistory(
			PatientSurgicalHistoryDto patientSurgicalHistoryDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientSurgicalHistoryDao.updatePatientSurgicalHistory(patientSurgicalHistoryDto);
	}


	@Override
	@Transactional
	public Response updateStatusEnterInError(
			PatientSurgicalHistoryDto patientSurgicalHistoryDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientSurgicalHistoryDao.updateStatusEnterInError(patientSurgicalHistoryDto);
	}


	@Override
	@Transactional
	public Response updateSaveNurseReviewedFlag(
			PatientSurgicalHistory patientSurgicalHistory)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientSurgicalHistoryDao.updateSaveNurseReviewedFlag(patientSurgicalHistory);
	}


	@Override
	public Response getCurrentPatientSurgicalHistoryForCoversheet(PatientSurgicalHistoryDto patientSurgicalHistoryDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientSurgicalHistoryDao.getCurrentPatientSurgicalHistoryForCoversheet(patientSurgicalHistoryDto);
	}


	@Override
	public Response getPatientSurgicalHistoryForBookView(PatientSurgicalHistoryDto patientSurgicalHistoryDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientSurgicalHistoryDao.getPatientSurgicalHistoryForBookView(patientSurgicalHistoryDto);
	}


	@Override
	public Response getCompletedProcedureListForSurgicalHistory(PatientSurgicalHistoryDto patientSurgicalHistoryDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientSurgicalHistoryDao.getCompletedProcedureListForSurgicalHistory(patientSurgicalHistoryDto);
	}


	@Override
	public Response updateStatusRemoveSurgicalHistory(PatientSurgicalHistoryDto patientSurgicalHistoryDto)
			throws ApplicationException {try {
				
				return iPatientSurgicalHistoryDao.updateStatusRemoveSurgicalHistory(patientSurgicalHistoryDto);
		
		} catch (Exception e) {
		logger.error("updateStatusRemoveSurgicalHistory failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);}


	@Override
	public Response getPatientSurgicalHistory(PatientSurgicalHistoryDto patientSurgicalHistoryDto)
			throws ApplicationException {
		return iPatientSurgicalHistoryDao.getPatientSurgicalHistory(patientSurgicalHistoryDto);
	}


	@Override
	public Response getMostUsedSurgicalHistory(PatientSurgicalHistoryDto patientSurgicalHistoryDto)
			throws ApplicationException {
		return iPatientSurgicalHistoryDao.getMostUsedSurgicalHistory(patientSurgicalHistoryDto);
	}

}

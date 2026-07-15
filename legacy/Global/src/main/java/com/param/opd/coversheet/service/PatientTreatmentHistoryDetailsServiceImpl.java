package com.param.opd.coversheet.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dao.IPatientTreatmentHistoryDetailsDao;
import com.param.opd.coversheet.dto.PatientTreatmentHistoryDetailsDto;
import com.param.opd.coversheet.model.PatientTreatmentHistoryDetails;

import in.param.exception.ApplicationException;


import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
public class PatientTreatmentHistoryDetailsServiceImpl implements IPatientTreatmentHistoryDetailsService , ICommonConstants{
	final static Logger logger = Logger.getLogger(PatientTreatmentHistoryDetailsServiceImpl.class);


	@Autowired
	private IPatientTreatmentHistoryDetailsDao iPatientTreatmentHistoryDetailsDao;

	@Override
	public Response getOldListOfPatientTreatmentHistoryDetails(PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) {
		
		return iPatientTreatmentHistoryDetailsDao.getOldListOfPatientTreatmentHistoryDetails(patientTreatmentHistoryDetailsDto);
	}

	@Override
	public Response getCurrentListOfPatientTreatmentHistoryDetails(PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) {
		
		return iPatientTreatmentHistoryDetailsDao.getCurrentListOfPatientTreatmentHistoryDetails(patientTreatmentHistoryDetailsDto);
	}

	@Override
	public Response savePatientTreatmentHistoryDetails(PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) {
		
		return iPatientTreatmentHistoryDetailsDao.savePatientTreatmentHistoryDetails(patientTreatmentHistoryDetailsDto);
	}

	@Override
	public Response updatePatientTreatmentHistoryDetails(PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) {
		
		return iPatientTreatmentHistoryDetailsDao.updatePatientTreatmentHistoryDetails(patientTreatmentHistoryDetailsDto);
		}

	@Override
	public Response updateEnterErrorStatusPatientTreatmentHistoryDetails(PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) {
		
		return iPatientTreatmentHistoryDetailsDao.updateEnterErrorStatusPatientTreatmentHistoryDetails(patientTreatmentHistoryDetailsDto);
	}

	@Override
	public Response updateSaveNurseReviewedFlag(PatientTreatmentHistoryDetails patientTreatmentHistoryDetails)
			throws ApplicationException {

		return iPatientTreatmentHistoryDetailsDao.updateSaveNurseReviewedFlag(patientTreatmentHistoryDetails);
	}

	@Override
	public Response getCurrentPatientTreatmentHistoryDetailsForCoversheet(
			PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientTreatmentHistoryDetailsDao.getCurrentPatientTreatmentHistoryDetailsForCoversheet(patientTreatmentHistoryDetailsDto);
	}

	@Override
	public Response getPatientTreatmentHistoryForBookView(
			PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientTreatmentHistoryDetailsDao.getPatientTreatmentHistoryForBookView(patientTreatmentHistoryDetailsDto);
	}

	@Override
	public Response getCompletedDiagnosticsProcedures(
			PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientTreatmentHistoryDetailsDao.getCompletedDiagnosticsProcedures(patientTreatmentHistoryDetailsDto);
	}

	@Override
	public Response getCompletedMedicalProcedures(PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientTreatmentHistoryDetailsDao.getCompletedMedicalProcedures(patientTreatmentHistoryDetailsDto);
	}

	@Override
	public Response updateStatusRemoveTreatmentHistory(
			PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) throws ApplicationException {try {
				
				return iPatientTreatmentHistoryDetailsDao.updateStatusRemoveMedicalHistory(patientTreatmentHistoryDetailsDto);
		
		} catch (Exception e) {
		logger.error("updateStatusRemoveTreatmentHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getPatientTreatmentHistoryForDischargeSummary(
			PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) throws ApplicationException {
			return iPatientTreatmentHistoryDetailsDao.getPatientTreatmentHistoryForDischargeSummary(patientTreatmentHistoryDetailsDto);
	}

	@Override
	public Response getInvestigationHistory(PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto)
			throws ApplicationException {
		return iPatientTreatmentHistoryDetailsDao.getInvestigationHistory(patientTreatmentHistoryDetailsDto);
	}
	
	

}

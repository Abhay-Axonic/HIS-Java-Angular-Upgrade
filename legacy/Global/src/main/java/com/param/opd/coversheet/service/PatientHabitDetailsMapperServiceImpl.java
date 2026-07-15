package com.param.opd.coversheet.service;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.param.exception.ApplicationException;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.opd.coversheet.dao.IPatientHabitDetailsMapperDao;
import com.param.opd.coversheet.dto.PatientHabitDetailsDto;
import com.param.opd.coversheet.model.PatientHabitDetails;

@Service
@SuppressWarnings({ "unused", "unchecked" })
public class PatientHabitDetailsMapperServiceImpl implements IPatientHabitDetailsMapperService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(PatientHabitDetailsMapperServiceImpl.class);

	@Autowired
	private IPatientHabitDetailsMapperDao iPatientHabitDetailsMapperDao;

	@Override
	
	public Response savePatientHabitDetailsMapper(PatientHabitDetailsDto patientHabitDetailsDto)throws ApplicationException {
		// TODO Auto-generated method stub
		try
		{
			Response patientHabitDetailsMapperResponse = iPatientHabitDetailsMapperDao.updatePatientHabitDetailsMapper(patientHabitDetailsDto);
			return iPatientHabitDetailsMapperDao.savePatientHabitDetailsMapper(patientHabitDetailsDto);
			
			
		}catch(Exception e)
		{
			logger.error("savePatientHabitDetailsMapper failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	@Transactional
	public Response getListOfPatientHabitDetailsMapper(PatientHabitDetailsDto patientHabitDetailsDto)throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientHabitDetailsMapperDao.getListOfPatientHabitDetailsMapper(patientHabitDetailsDto);
	}

	@Override
	@Transactional
	public Response getOldPatientHabitDetails(
			PatientHabitDetailsDto patientHabitDetailsDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientHabitDetailsMapperDao.getOldPatientHabitDetails(patientHabitDetailsDto);
	}

	@Override
	@Transactional
	public Response getCurrentPatientHabitDetails(
			PatientHabitDetailsDto patientHabitDetailsDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientHabitDetailsMapperDao.getCurrentPatientHabitDetails(patientHabitDetailsDto);
	}

	@Override
	@Transactional
	public Response updatePatientHabitDetails(
			PatientHabitDetailsDto patientHabitDetailsDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientHabitDetailsMapperDao.updatePatientHabitDetails(patientHabitDetailsDto);
	}

	@Override
	@Transactional
	public Response updateSaveNurseReviewedFlag(
			PatientHabitDetails patientHabitDetails)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientHabitDetailsMapperDao.updateSaveNurseReviewedFlag(patientHabitDetails);
	}

	@Override
	@Transactional
	public Response updateStatusEnterInError(
			PatientHabitDetailsDto patientHabitDetailsDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientHabitDetailsMapperDao.updateStatusEnterInError(patientHabitDetailsDto);
	}

	@Override
	public Response getPatientHabitsForBookView(PatientHabitDetailsDto patientHabitDetailsDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientHabitDetailsMapperDao.getPatientHabitsForBookView(patientHabitDetailsDto);
	}

	@Override
	public Response getPatientHabitsForNewCov(PatientHabitDetailsDto patientHabitDetailsDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientHabitDetailsMapperDao.getPatientHabitsForNewCov(patientHabitDetailsDto);
	}

	@Override
	public Response getSearchedHabits(PatientHabitDetailsDto patientHabitDetailsDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientHabitDetailsMapperDao.getSearchedHabits(patientHabitDetailsDto);
	}

	@Override
	public Response removePatientHabits(PatientHabitDetailsDto patientHabitDetailsDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientHabitDetailsMapperDao.removePatientHabits(patientHabitDetailsDto);
	}

	@Override
	public Response savePatientHabitsDetails(PatientHabitDetailsDto patientHabitDetailsDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientHabitDetailsMapperDao.savePatientHabitsDetails(patientHabitDetailsDto);
	}

	@Override
	public Response removePatientHabitsDetails(PatientHabitDetailsDto patientHabitDetailsDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientHabitDetailsMapperDao.removePatientHabitsDetails(patientHabitDetailsDto);
	}

	@Override
	public Response updatePatientHabitRemarkForNewCov(PatientHabitDetailsDto patientHabitDetailsDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iPatientHabitDetailsMapperDao.updatePatientHabitRemarkForNewCov(patientHabitDetailsDto);
	}

}

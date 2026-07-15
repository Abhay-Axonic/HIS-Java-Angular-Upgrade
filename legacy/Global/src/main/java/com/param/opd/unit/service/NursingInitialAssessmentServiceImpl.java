package com.param.opd.unit.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.Response;
import com.param.global.dto.NursingInitialAssessmentDto;
import com.param.opd.unit.dao.INursingInitialAssessmentDao;

import in.param.exception.ApplicationException;


@Service
@SuppressWarnings({"rawtypes"})
public class NursingInitialAssessmentServiceImpl implements INursingInitialAssessmentService {
	@Autowired
	private INursingInitialAssessmentDao iNursingInitialAssessmentDao;
	

	@Override
	public Response saveNursingInitialAssessment(NursingInitialAssessmentDto nursingInitialAssessmentDto) throws ApplicationException{
		return iNursingInitialAssessmentDao.saveAppointmentVitalMapper(nursingInitialAssessmentDto);

	}


	@Override
	public Response getNursingInitialAssessmentList(NursingInitialAssessmentDto nursingInitialAssessmentDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iNursingInitialAssessmentDao.getNursingInitialAssessmentList(nursingInitialAssessmentDto);
	}

}

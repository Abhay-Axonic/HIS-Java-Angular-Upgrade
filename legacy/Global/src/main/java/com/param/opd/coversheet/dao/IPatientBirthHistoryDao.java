package com.param.opd.coversheet.dao;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.PatientBirthHistoryDTO;
import com.param.opd.coversheet.dto.PatientTravelHistoryDetailsDto;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IPatientBirthHistoryDao {

    Response savePatientBirthHistory(PatientBirthHistoryDTO patientBirthHistoryDTO)throws ApplicationException;

    Response getPatientBirthHistory(Integer patientId)throws ApplicationException;


}

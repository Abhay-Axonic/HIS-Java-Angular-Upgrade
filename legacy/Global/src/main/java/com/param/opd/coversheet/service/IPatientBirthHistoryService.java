package com.param.opd.coversheet.service;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.PatientBirthHistoryDTO;
import com.param.opd.coversheet.dto.PatientTravelHistoryDetailsDto;
import in.param.exception.ApplicationException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface IPatientBirthHistoryService {

    Response savePatientBirthHistory(PatientBirthHistoryDTO patientBirthHistoryDTO)throws ApplicationException;

    Response getPatientBirthHistory(Integer patientId)throws ApplicationException;


}

package com.param.global.otims.service;


import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.otims.dto.OtcCompletionInvMedicationDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
@Transactional
public interface IOtcCompletionInvMedicationService {

	public Response saveInvMedications(OtcCompletionInvMedicationDto otcCompletionInvMedicationDto) throws ApplicationException;

}

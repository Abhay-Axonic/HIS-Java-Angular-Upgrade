package com.param.global.doctor.service;

import com.param.global.common.Response;
import com.param.global.doctor.dto.OutPatientFeedbackDTO;

import javax.transaction.Transactional;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IOutPatientFeedbackService {

    public Response saveOutPatientFeedback(OutPatientFeedbackDTO outPatientFeedbackDTO);

    public Response getOutPatientFeedbackList(Integer unitId, Integer organizationId, Integer patientId, String visitType, Integer visitTypeId, Integer encounterId);

}

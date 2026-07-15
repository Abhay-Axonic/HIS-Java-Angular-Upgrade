package com.param.global.doctor.service;

import com.param.global.common.Response;
import com.param.global.doctor.dto.OutPatientFeedbackDTO;

import javax.transaction.Transactional;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IInPatientFeedbackService {

    public Response saveInPatientFeedback(OutPatientFeedbackDTO outPatientFeedbackDTO);

    public Response getInPatientFeedbackList(Integer unitId, Integer organizationId, Integer patientId, String visitType, Integer visitTypeId, Integer admissionId);

}

package com.param.global.doctor.dao;

import com.param.global.common.Response;
import com.param.global.doctor.dto.OutPatientFeedbackDTO;

@SuppressWarnings("rawtypes")
public interface IInPatientFeedbackDao {

    public Response saveInPatientFeedback(OutPatientFeedbackDTO outPatientFeedbackDTO);

    public Response getInPatientFeedbackList(Integer unitId, Integer organizationId, Integer patientId, String visitType, Integer visitTypeId, Integer admissionId);

}

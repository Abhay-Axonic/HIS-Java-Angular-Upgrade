package com.param.global.doctor.dao;

import com.param.global.common.Response;
import com.param.global.doctor.dto.OutPatientFeedbackDTO;

@SuppressWarnings("rawtypes")
public interface IOutPatientFeedbackDao {

    public Response saveOutPatientFeedback(OutPatientFeedbackDTO outPatientFeedbackDTO);

    public Response getOutPatientFeedbackList(Integer unitId, Integer organizationId, Integer patientId, String visitType, Integer visitTypeId, Integer encounterId);

}

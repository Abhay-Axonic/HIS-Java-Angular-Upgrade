package com.param.global.unit.service;

import com.param.global.common.Response;
import com.param.global.unit.dto.AnesthesiaAssessmentDTO;
import com.param.global.unit.dto.InitialAssessmentICUAndERDTO;

import javax.transaction.Transactional;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IAnesthesiaAssessmentService {

    public Response saveAnesthesiaAssessment(AnesthesiaAssessmentDTO anesthesiaAssessmentDTO);

    public Response getAnesthesiaAssessment(Integer unitId, Integer organizationId, Integer patientId);


}

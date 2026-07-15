package com.param.global.unit.dao;

import com.param.global.common.Response;
import com.param.global.unit.dto.AnesthesiaAssessmentDTO;
import com.param.global.unit.dto.InitialAssessmentICUAndERDTO;
import com.param.global.unit.dto.InitialAssessmentICUAndERReturnDTO;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface IAnesthesiaAssessmentDao {

    public Response saveAnesthesiaAssessment(AnesthesiaAssessmentDTO anesthesiaAssessmentDTO);

    public Response getAnesthesiaAssessment(Integer unitId, Integer organizationId, Integer patientId);

    public Response inactiveAnesthesiaAssessment(AnesthesiaAssessmentDTO anesthesiaAssessmentDTO);

}

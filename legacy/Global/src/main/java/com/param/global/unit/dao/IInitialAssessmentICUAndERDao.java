package com.param.global.unit.dao;

import com.param.global.common.Response;
import com.param.global.unit.dto.InitialAssessmentICUAndERDTO;
import com.param.global.unit.dto.InitialAssessmentICUAndERReturnDTO;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface IInitialAssessmentICUAndERDao {

    public Response saveInitialAssessmentICUAndER(InitialAssessmentICUAndERDTO initialAssessmentICUAndERDTO);

    public Response getInitialAssessmentICUAndERList(Integer unitId, Integer organizationId, Integer admissionId);

    public Response getInitialAssessmentICUAndERActiveList(Integer unitId, Integer organizationId, Integer admissionId);

    public Response inactiveInitialAssessmentICUAndERActiveList(List<InitialAssessmentICUAndERReturnDTO> patientFallReturnReportDtos);



}

package com.param.global.unit.dao;

import com.param.global.common.Response;
import com.param.global.unit.dto.InitialAssessmentICUAndERDTO;
import com.param.global.unit.dto.InitialAssessmentICUAndERMapperDTO;
import com.param.global.unit.dto.InitialAssessmentICUAndERReturnDTO;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface IInitialAssessmentICUAndERMapperDao {

    public Response saveInitialAssessmentICUAndERMapper(InitialAssessmentICUAndERDTO initialAssessmentICUAndERDTO);

    public Response getInitialAssessmentICUAndERList(Integer unitId, Integer organizationId, Integer admissionId);

    public Response getInitialAssessmentICUAndERMapperActiveList(Integer unitId, Integer organizationId, Integer admissionId);

    public Response inactiveInitialAssessmentICUAndERMapperActiveList(List<InitialAssessmentICUAndERReturnDTO> patientFallReturnReportDtos);


}

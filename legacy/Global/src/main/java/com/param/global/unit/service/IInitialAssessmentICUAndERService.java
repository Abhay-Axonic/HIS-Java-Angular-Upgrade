package com.param.global.unit.service;

import com.param.global.common.Response;
import com.param.global.unit.dto.InitialAssessmentICUAndERDTO;

import javax.transaction.Transactional;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IInitialAssessmentICUAndERService {

    public Response saveInitialAssessmentICUAndER(InitialAssessmentICUAndERDTO initialAssessmentICUAndERDTO);

    public Response getInitialAssessmentICUAndERList(Integer unitId, Integer organizationId, Integer admissionId);


}

package com.param.adt.admission.service;

import com.param.adt.admission.dto.HandHygieneScoreDTO;
import com.param.adt.admission.dto.NutritionalAssessmentScoreDTO;
import com.param.adt.common.Response;

import javax.transaction.Transactional;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IHandHygieneScoreService {

    public Response saveAsDraftHandHygieneScore(HandHygieneScoreDTO handHygieneScoreDTO);

    public Response saveHandHygieneScore(HandHygieneScoreDTO handHygieneScoreDTO);

    public Response getSaveAsDraftHandHygieneScore(Integer unitId, Integer organizationId, Integer wardId);

}

package com.param.adt.admission.service;

import com.param.adt.admission.dto.NutritionalAssessmentScoreDTO;
import com.param.adt.common.Response;

import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface INutritionalAssessmentScoreService {

    public Response saveNutritionalAssessmentScore(NutritionalAssessmentScoreDTO nutritionalAssessmentScoreDTO);

    public Response getSaveAsDraftNutritionalAssessmentScore(Integer unitId, Integer organizationId, Integer admissionId);

	public com.param.global.common.Response getNutritionalIntailAnethesthiaAssessmentScore(Integer admissionId)throws ApplicationException;

}

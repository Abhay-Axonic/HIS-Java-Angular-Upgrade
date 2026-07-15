package com.param.adt.admission.dao;

import java.util.List;

import com.param.adt.admission.dto.NabhAssessmentDTO;
import com.param.adt.admission.dto.NutritionalAssessmentScoreDTO;
import com.param.adt.common.Response;

@SuppressWarnings("rawtypes")
public interface INutritionalAssessmentScoreDao {

    public Response saveAsDraftNutritionalAssessmentScore(NutritionalAssessmentScoreDTO nutritionalAssessmentScoreDTO);

    public Response getSaveAsDraftNutritionalAssessmentScore(Integer unitId, Integer organizationId, Integer admissionId);

    public Response inactiveSavedDraftScoreList(List<NabhAssessmentDTO> getNabhAssessmentDTO);

	public com.param.global.common.Response getNutritionalIntailAnethesthiaAssessmentScore(Integer admissionId);



}

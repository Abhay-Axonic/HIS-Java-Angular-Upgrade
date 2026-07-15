package com.param.adt.admission.dao;

import com.param.adt.admission.dto.GcsDraftScaleScoreDTO;
import com.param.adt.admission.dto.GcsScaleScoreDTO;
import com.param.adt.common.Response;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface IBradenDraftScaleScoreDao {
    public Response saveAsDraftBradenScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO);

    public Response getAllBradenDraftScaleScores(Integer unitId, Integer organizationId, Integer admissionId);

    public Response inactiveSavedDraftScoreList(List<GcsDraftScaleScoreDTO> getGcsDraftScaleScores);

}

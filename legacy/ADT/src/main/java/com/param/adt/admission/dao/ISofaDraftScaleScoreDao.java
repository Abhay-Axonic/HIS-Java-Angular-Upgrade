package com.param.adt.admission.dao;

import com.param.adt.admission.dto.GcsDraftScaleScoreDTO;
import com.param.adt.admission.dto.GcsScaleScoreDTO;
import com.param.adt.common.Response;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface ISofaDraftScaleScoreDao {

    public Response saveAsDraftSofaScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO);

    public Response getAllAsDraftSofaScaleScore(Integer unitId, Integer organizationId, Integer admissionId);

    public Response inactiveSavedSofaDraftScoreList(List<GcsDraftScaleScoreDTO> getGcsDraftScaleScores);


}

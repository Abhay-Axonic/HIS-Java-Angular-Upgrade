package com.param.adt.admission.dao;

import com.param.adt.admission.dto.GcsDraftScaleScoreDTO;
import com.param.adt.admission.dto.GcsScaleScoreDTO;
import com.param.adt.common.Response;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface IApacheDraftScaleScoreDao {

    public Response saveAsDraftApacheScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO);

    public Response getAllApacheDraftScaleScores(Integer unitId, Integer organizationId, Integer admissionId);

    public Response inactiveSavedApacheDraftScoreList(List<GcsDraftScaleScoreDTO> getGcsDraftScaleScores);


}

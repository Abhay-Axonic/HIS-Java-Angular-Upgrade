package com.param.adt.admission.dao;

import com.param.adt.admission.dto.GcsScaleScoreDTO;
import com.param.adt.common.Response;

@SuppressWarnings("rawtypes")
public interface INpuapScaleScoreDao {

    public Response saveNpuapScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO);

    public Response getNpuapScaleScoreById(Integer unitId, Integer organizationId, Integer admissionId);

}

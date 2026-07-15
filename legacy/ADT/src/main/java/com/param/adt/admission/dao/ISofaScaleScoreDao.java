package com.param.adt.admission.dao;

import com.param.adt.admission.dto.GcsScaleScoreDTO;
import com.param.adt.common.Response;

@SuppressWarnings("rawtypes")
public interface ISofaScaleScoreDao {

    public Response saveSofaScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO);

    public Response getSofaScaleScoreById(Integer unitId, Integer organizationId, Integer admissionId);

}

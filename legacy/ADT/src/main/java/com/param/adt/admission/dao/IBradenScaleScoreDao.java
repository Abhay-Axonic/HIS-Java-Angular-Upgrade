package com.param.adt.admission.dao;

import com.param.adt.admission.dto.GcsScaleScoreDTO;
import com.param.adt.common.Response;

@SuppressWarnings("rawtypes")
public interface IBradenScaleScoreDao {

    public Response saveBradenScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO);

    public Response getBradenScaleScoreById(Integer unitId, Integer organizationId, Integer admissionId);

}
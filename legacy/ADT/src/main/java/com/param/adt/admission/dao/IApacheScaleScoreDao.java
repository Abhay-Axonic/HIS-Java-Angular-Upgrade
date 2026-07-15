package com.param.adt.admission.dao;

import com.param.adt.admission.dto.GcsScaleScoreDTO;
import com.param.adt.common.Response;

@SuppressWarnings("rawtypes")
public interface IApacheScaleScoreDao {

    public Response saveApacheScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO);

    public Response getApacheScaleScoreById(Integer unitId, Integer organizationId, Integer admissionId);


}

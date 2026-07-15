package com.param.adt.admission.dao;

import com.param.adt.admission.dto.GcsDraftScaleScoreDTO;
import com.param.adt.admission.dto.GcsScaleScoreDTO;
import com.param.adt.admission.dto.NursingScaleScoreDTO;
import com.param.adt.common.Response;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface IGcsScaleScoreDao {

    public Response saveGcsScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO);

    public Response getGcsScaleScoreById(Integer unitId, Integer organizationId, Integer admissionId);

}


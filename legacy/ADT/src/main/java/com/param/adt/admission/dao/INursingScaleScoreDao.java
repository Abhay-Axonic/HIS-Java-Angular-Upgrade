package com.param.adt.admission.dao;

import com.param.adt.admission.dto.GcsScaleScoreDTO;
import com.param.adt.common.Response;

@SuppressWarnings("rawtypes")
public interface INursingScaleScoreDao {

    public Response saveNursingScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO);

    public Response getAllNursingScaleScore(Integer unitId, Integer organizationId, Integer admissionId, Integer encounterId);

    public Response getAllNursingScoreById(Integer unitId, Integer organizationId, Integer admissionId, Integer tabNumber);

    public Response updateNursingScaleScoreDaoById(Integer nursingScaleScoreId, GcsScaleScoreDTO gcsScaleScoreDTO);

    public Response getSpecialityMappedNursingScoreTabs(Integer unitId, Integer organizationId, Integer doctorId, Integer specialityId);
}

package com.param.adt.admission.dao;

import com.param.adt.admission.dto.AldreteScaleScoreDto;
import com.param.adt.admission.dto.BishopScaleScoreDTO;
import com.param.adt.common.Response;
import in.param.exception.ApplicationException;

import java.util.List;

public interface IAldreteScaleScoreDao {
    Response getAldreteDraftScaleScores(Integer unitId, Integer organizationId, Integer admissionId, Integer encounterId) throws ApplicationException;

    Response inactiveDraftedAldreteScores(List<AldreteScaleScoreDto> aldreteScaleScoreDto) throws ApplicationException;

    void saveAldreteScore(AldreteScaleScoreDto aldreteScaleScoreDto) throws ApplicationException;

    Response updateAldreteScaleScores(Integer aldreteScoreId, AldreteScaleScoreDto aldreteScaleScoreDto) throws ApplicationException;
}

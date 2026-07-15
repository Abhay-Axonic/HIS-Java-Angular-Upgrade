package com.param.adt.admission.dao;

import com.param.adt.admission.dto.BishopScaleScoreDTO;
import com.param.adt.common.Response;
import in.param.exception.ApplicationException;

import java.util.List;

public interface IBishopScaleScoreDao {
    void saveDraftedBishopScore(BishopScaleScoreDTO bishopScaleScoreDTO) throws ApplicationException;

    Response inactiveDraftedBishopScores(List<BishopScaleScoreDTO> bishopScaleScoreDTO)throws ApplicationException;

    Response getAllBishopDraftScaleScores(Integer unitId, Integer organizationId, Integer admissionId, Integer encounterId) throws ApplicationException;

    Response updateBishopScaleScores(Integer Id, BishopScaleScoreDTO bishopScaleScoreDTO) throws ApplicationException;
}

package com.param.adt.admission.dao;

import com.param.adt.admission.dto.APGARScaleScoreDto;
import com.param.adt.common.Response;
import in.param.exception.ApplicationException;

import java.util.List;

public interface IAPGARScaleScoreDao {
    Response getAPGARDraftScaleScores(Integer unitId, Integer organizationId, Integer admissionId, Integer encounterId) throws ApplicationException;

    Response inactiveDraftedAPGARScores(List<APGARScaleScoreDto> listObject) throws ApplicationException;

    void saveApgarScore(APGARScaleScoreDto apgarScaleScoreDto) throws ApplicationException;

    Response updateAldreteScaleScores(Integer apgarScoreId, APGARScaleScoreDto apgarScaleScoreDto) throws ApplicationException;
}

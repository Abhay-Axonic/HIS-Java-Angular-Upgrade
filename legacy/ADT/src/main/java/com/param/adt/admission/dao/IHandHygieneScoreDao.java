package com.param.adt.admission.dao;

import com.param.adt.admission.dto.*;
import com.param.adt.common.Response;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface IHandHygieneScoreDao {

    public Response saveAsDraftHandHygieneScore(HandHygieneScoreDTO handHygieneScoreDTO);

    public Response saveHandHygieneScore(HandHygieneScoreDTO handHygieneScoreDTO);

    public Response getSaveAsDraftHandHygieneScore(Integer unitId, Integer organizationId, Integer wardId);

    public Response inactiveSavedDraftScoreList(List<HandHygieneReturnScoreDTO> handHygieneReturnScoreDTOS);

}

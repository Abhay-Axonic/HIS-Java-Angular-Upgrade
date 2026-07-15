package com.param.adt.admission.dao;

import com.param.adt.admission.dto.HandHygieneReturnScoreDTO;
import com.param.adt.admission.dto.HandHygieneScoreDTO;
import com.param.adt.admission.dto.LaboratoryInspectionScoreDTO;
import com.param.adt.admission.dto.LaboratoryInspectionScoreReturnDTO;
import com.param.adt.admission.model.LaboratoryInspectionScore;
import com.param.adt.common.Response;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface ILaboratoryInspectionScoreDao {

    public Response saveAsDraftLaboratoryInspectionScore(LaboratoryInspectionScoreDTO laboratoryInspectionScoreDTO);

    public Response saveLaboratoryInspectionScore(LaboratoryInspectionScore laboratoryInspectionScore);

    public Response getSaveAsDraftLaboratoryInspectionScore(Integer unitId, Integer organizationId, Integer departmentId);

    public Response inactiveSavedDraftScoreList(List<LaboratoryInspectionScoreReturnDTO> laboratoryInspectionScores);

}

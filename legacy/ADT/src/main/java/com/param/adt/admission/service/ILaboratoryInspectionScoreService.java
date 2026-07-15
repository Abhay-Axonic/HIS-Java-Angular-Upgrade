package com.param.adt.admission.service;

import com.param.adt.admission.dto.HandHygieneScoreDTO;
import com.param.adt.admission.dto.LaboratoryInspectionListDTO;
import com.param.adt.admission.dto.LaboratoryInspectionScoreDTO;
import com.param.adt.admission.model.LaboratoryInspectionScore;
import com.param.adt.common.Response;

import javax.transaction.Transactional;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface ILaboratoryInspectionScoreService {

    public Response saveAsDraftLaboratoryInspectionScore(LaboratoryInspectionScoreDTO laboratoryInspectionScoreDTO);

    public Response saveLaboratoryInspectionScore(LaboratoryInspectionScoreDTO laboratoryInspectionScore);

    public Response getSaveAsDraftLaboratoryInspectionScore(Integer unitId, Integer organizationId, Integer departmentId);

}

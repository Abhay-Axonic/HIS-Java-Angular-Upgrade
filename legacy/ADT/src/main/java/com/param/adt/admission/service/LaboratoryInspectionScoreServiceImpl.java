package com.param.adt.admission.service;

import com.param.adt.admission.dao.IHandHygieneScoreDao;
import com.param.adt.admission.dao.ILaboratoryInspectionScoreDao;
import com.param.adt.admission.dto.LaboratoryInspectionScoreDTO;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({"rawtypes"})
public class LaboratoryInspectionScoreServiceImpl implements ILaboratoryInspectionScoreService, ICommonConstants {

    @Autowired
    private ILaboratoryInspectionScoreDao iLaboratoryInspectionScoreDao;

    @Override
    public Response saveAsDraftLaboratoryInspectionScore(LaboratoryInspectionScoreDTO laboratoryInspectionScoreDTO) {
        try {
            Response getListResponse = iLaboratoryInspectionScoreDao.getSaveAsDraftLaboratoryInspectionScore(laboratoryInspectionScoreDTO.getUnitId(), laboratoryInspectionScoreDTO.getOrganizationId(), laboratoryInspectionScoreDTO.getDepartmentId());

            if (!getListResponse.getListObject().isEmpty()) {
                iLaboratoryInspectionScoreDao.inactiveSavedDraftScoreList(getListResponse.getListObject());
            }

            return iLaboratoryInspectionScoreDao.saveAsDraftLaboratoryInspectionScore(laboratoryInspectionScoreDTO);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response saveLaboratoryInspectionScore(LaboratoryInspectionScoreDTO laboratoryInspectionScore) {
        return null;
    }

    @Override
    public Response getSaveAsDraftLaboratoryInspectionScore(Integer unitId, Integer organizationId, Integer departmentId) {
        try {
            return iLaboratoryInspectionScoreDao.getSaveAsDraftLaboratoryInspectionScore(unitId, organizationId, departmentId);
        } catch (Exception e) {
            throw e;
        }
    }
}

package com.param.adt.admission.service;

import com.param.adt.admission.dao.INutritionalAssessmentScoreDao;
import com.param.adt.admission.dao.IPatientFallReportDao;
import com.param.adt.admission.dto.NutritionalAssessmentScoreDTO;
import com.param.adt.admission.dto.PatientFallReportDto;
import com.param.adt.common.Response;
import com.param.global.common.ICommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({"rawtypes"})
public class PatientFallReportServiceImpl  implements IPatientFallReportService, ICommonConstants {

    @Autowired
    private IPatientFallReportDao iPatientFallReportDao;

    @Override
    public Response saveAsDraftPatientFallReport(PatientFallReportDto patientFallReportDto) {
        try{

            Response getListResponse = iPatientFallReportDao.getSaveAsDraftPatientFallReporting(patientFallReportDto.getUnitId(), patientFallReportDto.getOrganizationId(), patientFallReportDto.getAdmissionId());

            if(!getListResponse.getListObject().isEmpty()){
                iPatientFallReportDao.inactiveSavedDraftScoreList(getListResponse.getListObject());
            }

            return iPatientFallReportDao.saveAsDraftPatientFallReporting(patientFallReportDto);
        }catch(Exception e){
            throw e;
        }
    }

    @Override
    public Response savePatientFallReport(PatientFallReportDto patientFallReportDto) {
        return null;
    }

    @Override
    public Response getSaveAsDraftPatientFallReport(Integer unitId, Integer organizationId, Integer admissionId) {
        try{
            return iPatientFallReportDao.getSaveAsDraftPatientFallReporting(unitId, organizationId, admissionId);
        }catch (Exception e){
            throw e;
        }
    }
}

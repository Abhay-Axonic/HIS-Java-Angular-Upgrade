package com.param.adt.admission.dao;

import com.param.adt.admission.dto.*;
import com.param.adt.admission.model.NutritionalAssessmentScore;
import com.param.adt.admission.model.PatientFallReport;
import com.param.adt.common.Response;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import in.param.common.dao.GenericDao;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class PatientFallReportDaoImpl  extends GenericDao<PatientFallReport, Integer> implements IPatientFallReportDao, ICommonConstants {

    @Autowired
    SessionFactory sessionFactory;

    public PatientFallReportDaoImpl() {
        super(PatientFallReport.class);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Response saveAsDraftPatientFallReporting(PatientFallReportDto patientFallReportDto) {
        try{
            for(PatientFallReportListDto patientReportList: patientFallReportDto.getAssessmentScoreNamesList()){
                PatientFallReport patientFallReport = new PatientFallReport();

                patientFallReport.setAdmissionId(patientFallReportDto.getAdmissionId());
                patientFallReport.setFallReportingId(patientReportList.getFallReportingId());
                patientFallReport.setFallReportingName(patientReportList.getFallReportingName());
                patientFallReport.setFallReportingNameId(patientReportList.getFallReportingNameId());
                patientFallReport.setSelectedCheckBox(patientFallReportDto.getSelectedCheckBox());
                patientFallReport.setGcsScoreBefore(patientFallReportDto.getGcsScoreBefore());
                patientFallReport.setGcsScoreAfter(patientFallReportDto.getGcsScoreAfter());
                patientFallReport.setDiagnosisName(patientFallReportDto.getDiagnosisName());
                patientFallReport.setAssessmentOtherRemarkBefore(patientFallReportDto.getAssessmentOtherRemarkBefore());
                patientFallReport.setAssessmentOtherRemarkAfter(patientFallReportDto.getAssessmentOtherRemarkAfter());
                patientFallReport.setFactorOtherText(patientFallReportDto.getFactorOtherText());
                patientFallReport.setIncidentDateTime(GlobalCommonDateUtils.getDate(patientFallReportDto.getIncidentDateTime(), "dd-M-yyyy HH:mm:ss"));
                patientFallReport.setIncidentHour(patientFallReportDto.getIncidentHour());
                patientFallReport.setIncidentMinute(patientFallReportDto.getIncidentMinute());
                patientFallReport.setSaveAsDraft(patientFallReportDto.getSaveAsDraft());
                patientFallReport.setUnitId(patientFallReportDto.getUnitId());
                patientFallReport.setOrganizationId(patientFallReportDto.getOrganizationId());
                patientFallReport.setStatus('A');
                patientFallReport.setCreatedBy(patientFallReportDto.getCreatedBy());
                patientFallReport.setUpdatedBy(patientFallReportDto.getUpdatedBy());
                patientFallReport.setCreatedDate(GlobalCommonDateUtils.getDate(patientFallReportDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
                patientFallReport.setUpdatedDate(GlobalCommonDateUtils.getDate(patientFallReportDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().save(patientFallReport);
            }
            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Response savePatientFallReporting(PatientFallReportDto patientFallReportDto) {
        return null;
    }

    @Override
    public Response getSaveAsDraftPatientFallReporting(Integer unitId, Integer organizationId, Integer admissionId) {
        try{
            List<PatientFallReturnReportDto> getAllFallReportList = sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_FALL_REPORT_BY_ADMISSION_ID")
                    .setParameter("unitId",unitId)
                    .setParameter("organizationId",organizationId)
                    .setParameter("admissionId",admissionId)
                    .setResultTransformer(Transformers.aliasToBean(PatientFallReturnReportDto.class)).list();

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getAllFallReportList, null);

        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Response inactiveSavedDraftScoreList(List<PatientFallReturnReportDto> patientFallReturnReportDtos) {
        try {

            for (PatientFallReturnReportDto item : patientFallReturnReportDtos) {
                PatientFallReport patientFallReport = findById(item.getPatientFallReportId());
                patientFallReport.setStatus('I');
                patientFallReport.setUpdatedBy(item.getUpdatedBy());
                patientFallReport.setUpdatedDate(GlobalCommonDateUtils.getDate(item.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().update(patientFallReport);
            }

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
        } catch (Exception e) {
            throw e;
        }
    }
}

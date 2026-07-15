package com.param.opd.unit.dao;

import com.param.entity.model.opd.AbcdeAssessmentModel;
import com.param.entity.model.opd.NursingInitialAssessmentModel;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AbcdeAssessmentDto;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.dto.ERPatientDashboard;
import com.param.utility.TimeZoneComponent;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
public class AbcdeAssessmentDaoImpl  extends GenericDao<AbcdeAssessmentModel, Integer> implements IAbcdeAssessmentDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(AbcdeAssessmentDaoImpl.class);


    public AbcdeAssessmentDaoImpl() {
        super(AbcdeAssessmentModel.class);
    }

    @Override
    public Response saveABCDAssesment(AbcdeAssessmentDto abcdeAssessmentDto) throws ApplicationException {
       try{
           AbcdeAssessmentModel abcdeAssessmentModel=new AbcdeAssessmentModel();
           abcdeAssessmentModel.setAbcdeAssessmentDesc(abcdeAssessmentDto.getAbcdeAssessmentDesc());
           abcdeAssessmentModel.setPatientId(abcdeAssessmentDto.getPatientId());
           abcdeAssessmentModel.setEncounterId(abcdeAssessmentDto.getEncounterId());
           abcdeAssessmentModel.setAdmissionId(abcdeAssessmentDto.getAdmissionId());
           abcdeAssessmentModel.setStatus('A');
           abcdeAssessmentModel.setCreatedBy(abcdeAssessmentDto.getCreatedBy());
           abcdeAssessmentModel.setCreatedDate(abcdeAssessmentDto.getCreatedDate());
           abcdeAssessmentModel.setUpdatedDate(abcdeAssessmentDto.getUpdatedDate());
           abcdeAssessmentModel.setUpdatedBy(abcdeAssessmentDto.getUpdatedBy());
           abcdeAssessmentModel.setDoctorId(abcdeAssessmentDto.getDoctorId());
           abcdeAssessmentModel.setOrganizationId(abcdeAssessmentDto.getOrganizationId());
           abcdeAssessmentModel.setUnitId(abcdeAssessmentDto.getUnitId());
           save(abcdeAssessmentModel);
           return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        } catch (HibernateException he) {
        logger.error("saveABCDAssesment failed", he);
        } catch (Exception e) {
        logger.error("saveABCDAssesment failed", e);
        }
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
      }



    @Override
    public Response getABCDEAssessmentList(AbcdeAssessmentDto abcdeAssessmentDto) throws ApplicationException {
        try{
//            String query = " select concat('Dr. '|| dm.first_name || ' ' || COALESCE(dm.middle_name || ' ', '') || dm.last_name) AS \"doctorName\"," +
//                    " abcde_assessment_id as \"abcdeAssessmentId\",abcde_assessment_desc as \"abcdeAssessmentDesc\"," +
//                    " TO_CHAR(abcde.created_date, 'HH24:MI') AS \"assesmentTime\",abcde.patient_id as \"patientId\"," +
//                    " abcde.organization_id as \"organizationId\",abcde.unit_id as \"unitId\" from opd.t_ABCDE_assessment " +
//                    " abcde inner join doctor.m_doctor_master dm on dm.doctor_id=abcde.doctor_id  where abcde.patient_id= :patientId " +
//                    " and abcde.organization_id= :organizationId and abcde.unit_id= :unitId order by abcde.created_date desc ";

            String query = " select concat('Dr. '|| dm.first_name || ' ' || COALESCE(dm.middle_name || ' ', '') || dm.last_name) AS \"doctorName\"," +
                    " abcde_assessment_id as \"abcdeAssessmentId\",abcde_assessment_desc as \"abcdeAssessmentDesc\"," +
                    "  abcde.created_date AS \"assesmentTime\",abcde.patient_id as \"patientId\"," +
                    " abcde.organization_id as \"organizationId\",abcde.unit_id as \"unitId\" from opd.t_ABCDE_assessment " +
                    " abcde inner join doctor.m_doctor_master dm on dm.doctor_id=abcde.doctor_id  where abcde.patient_id= :patientId " +
                    " and abcde.organization_id= :organizationId and abcde.unit_id= :unitId order by abcde.created_date desc ";
            List<AbcdeAssessmentDto> abcdeAssessmentDtoList = sessionFactory.getCurrentSession()
                    .createSQLQuery(query)
                    .setParameter("organizationId", abcdeAssessmentDto.getOrganizationId())
                    .setParameter("unitId", abcdeAssessmentDto.getUnitId())
                    .setParameter("patientId", abcdeAssessmentDto.getPatientId())
                    .setResultTransformer(Transformers.aliasToBean(AbcdeAssessmentDto.class)).list();

            return new Response(SUCCESS, null, null, abcdeAssessmentDtoList, null);



        } catch (HibernateException he) {
        logger.error("getABCDEAssessmentList failed", he);
        } catch (Exception e) {
        logger.error("getABCDEAssessmentList failed", e);
        }
        return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }

    @Override
    public Response getERPatientDashBoardData(ERPatientDashboard eRPatientDashboard) throws ApplicationException {
        try{
//            String query = " SELECT DISTINCT ON (am.admission_id) am.admission_id AS \"admissionId\",history.current_triage_id AS \"currentTriage\",  " +
//                    "  dm.code as \"code\",dm.is_myocardial_infarction as \"stemi\",dm.is_acute_ischemic_stroke as \"stroke\",dm.is_trauma as \"truma\",dm.is_sepsis as \"sepsis\",dm.is_shock as \"shock\" , "+
//                    "        concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, tpr.dob))) as \"ageString\","+
//                    "        concat(tpr.first_name,' ',COALESCE(tpr.middle_name, ''),' ',tpr.last_name,' |',mgm.gender_name) " +
//                    "        AS \"patientName\", tpr.uhid_number AS \"uhid\", to_char(am.created_date,'DD-MM-YYYY HH24:MI:SS') " +
//                    "        AS \"arrivalTime\", ml.modeof_arrival AS \"modeOfArrival\", CASE WHEN ad.is_medico_legal = 'N' THEN" +
//                    "        'No' ELSE 'Yes' END AS \"mlc\", rm.room_code AS \"zone\", bm.bed_number AS \"bedNumber\", " +
//                    "        ad.triage_category_id AS \"triageCategoryId\", CAST(FLOOR(EXTRACT(EPOCH FROM (am.updated_date - am.created_date)) / 60) AS integer)  as \"doorToTraige\"," +
//                    "        CASE WHEN am.door_to_doctor_time IS NOT NULL THEN CAST(FLOOR(EXTRACT(EPOCH FROM (am.door_to_doctor_time - am.created_date)) / 60) AS integer) " +
//                    "        ELSE NULL END AS \"doorToDoctor\", abcde.abcde_assessment_desc AS \"abcdeDesc\", NULL AS \"pocTest\", " +
//                    "        dm.diagnosis_name AS \"diagnosis\", NULL AS \"flag\", ref.referrals AS \"referrals\", " +
//                    "        COALESCE(NULLIF(dism.discharge_type_code, ''), 'Still on Bed') AS \"disposition\", " +
//                    "        CASE WHEN td.discharge_status_id = 5 AND td.updated_date IS NOT NULL THEN CAST(FLOOR(EXTRACT" +
//                    "        (EPOCH FROM (td.updated_date - am.created_date)) / 60) AS integer) ELSE NULL END AS \"lengthOfStay\", " +
//                    "         tpr.patient_id as \"patientId\"  " +
//                    "        FROM adt.t_admission am INNER JOIN patient.t_patient_registration tpr ON am.patient_id = tpr.patient_id " +
//                    "        INNER JOIN adt.t_admission_details ad ON ad.admission_id = am.admission_id LEFT JOIN m_gender_master mgm ON " +
//                    "        mgm.gender_id = tpr.gender_id LEFT JOIN adt.t_medico_legal ml ON ml.admission_id = am.admission_id LEFT " +
//                    "        JOIN adt.m_room_master rm ON rm.room_id = ad.room_id LEFT JOIN adt.m_bed_master bm ON bm.bed_id = ad.bed_id " +
//                    "        LEFT JOIN opd.t_ABCDE_assessment abcde ON abcde.admission_id = am.admission_id LEFT JOIN " +
//                    "        patient.t_patient_diagnosis_appo_mapper pdm ON pdm.admission_id = am.admission_id LEFT JOIN emr.m_diagnosis_master dm " +
//                    "        ON dm.diagnosis_id = pdm.diagnosis_id LEFT JOIN adt.t_discharge td ON td.admission_id = am.admission_id LEFT " +
//                    "        JOIN adt.m_reason_for_discharge_master disM ON disM.discharge_type_id = td.discharge_type_id AND " +
//                    "        td.discharge_status_id = 5 LEFT JOIN t_referal_details rrd ON rrd.admission_id = am.admission_id LEFT " +
//                    "        JOIN adt.m_referral_master rmmm ON rrd.referral_id = rmmm.referral_id LEFT JOIN (SELECT cd.admission_id, " +
//                    "        STRING_AGG(DISTINCT concat('Dr. ', mdm.first_name,' ',COALESCE(mdm.middle_name,''),' ',mdm.last_name,' | ',msm.speciality_name),'#')" +
//                    "        AS referrals FROM opd.t_consult_doctor cd JOIN doctor.m_doctor_master mdm ON mdm.doctor_id = cd.ref_to_doctor_id LEFT JOIN " +
//                    "        doctor.m_speciality_master msm ON msm.speciality_id = cd.ref_to_speciality_id GROUP BY cd.admission_id) " +
//                    "        ref ON ref.admission_id = am.admission_id    LEFT JOIN ( " +
//                    "          SELECT DISTINCT ON (admission_details_id)  " +
//                    "           admission_details_id, " +
//                    "           current_triage_id " +
//                    "    FROM adt.t_admission_triage_history " +
//                    "    ORDER BY admission_details_id, updated_date DESC " +
//                    "    ) history  " +
//                    "ON ad.admission_details_id = history.admission_details_id  WHERE am.visit_type_id = 4 AND am.organization_id = :organizationId AND " +
//                    "        am.unit_id = :unitId AND am.created_date BETWEEN :fromDate AND :toDate ORDER BY am.admission_id DESC ";

            String query = " SELECT DISTINCT ON (am.admission_id) am.admission_id AS \"admissionId\",history.current_triage_id AS \"currentTriage\",  " +
                    "  dm.code as \"code\",dm.is_myocardial_infarction as \"stemi\",dm.is_acute_ischemic_stroke as \"stroke\",dm.is_trauma as \"truma\",dm.is_sepsis as \"sepsis\",dm.is_shock as \"shock\" , "+
                    "        concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, tpr.dob))) as \"ageString\","+
                    "        concat(tpr.first_name,' ',COALESCE(tpr.middle_name, ''),' ',tpr.last_name,' |',mgm.gender_name) " +
                    "        AS \"patientName\", tpr.uhid_number AS \"uhid\", to_char(am.created_date,'DD-MM-YYYY HH24:MI:SS') " +
                    "        AS \"arrivalTime\", ml.modeof_arrival AS \"modeOfArrival\", CASE WHEN ad.is_medico_legal = 'N' THEN" +
                    "        'No' ELSE 'Yes' END AS \"mlc\", rm.room_code AS \"zone\", bm.bed_number AS \"bedNumber\", " +
                    "        ad.triage_category_id AS \"triageCategoryId\", CAST(\n" +
                    "  EXTRACT(EPOCH FROM (date_trunc('minute', am.updated_date) - date_trunc('minute', am.created_date))) / 60 " +
                    "   AS INTEGER) as \"doorToTraige\"," +
                    "        CASE WHEN am.door_to_doctor_time IS NOT NULL THEN CAST(FLOOR(EXTRACT(EPOCH FROM (am.door_to_doctor_time - am.created_date)) / 60) AS integer) " +
                    "        ELSE NULL END AS \"doorToDoctor\", abcde.abcde_assessment_desc AS \"abcdeDesc\", NULL AS \"pocTest\", " +
                    "        dm.diagnosis_name AS \"diagnosis\", NULL AS \"flag\", ref.referrals AS \"referrals\", " +
                    "        COALESCE(NULLIF(dism.discharge_type_code, ''), 'Still on Bed') AS \"disposition\", " +
                    "        CASE WHEN td.discharge_status_id = 5 AND td.updated_date IS NOT NULL THEN CAST(FLOOR(EXTRACT" +
                    "        (EPOCH FROM (td.updated_date - am.created_date)) / 60) AS integer) ELSE NULL END AS \"lengthOfStay\", " +
                    "         tpr.patient_id as \"patientId\"  " +
                    "        FROM adt.t_admission am INNER JOIN patient.t_patient_registration tpr ON am.patient_id = tpr.patient_id " +
                    "        INNER JOIN adt.t_admission_details ad ON ad.admission_id = am.admission_id LEFT JOIN m_gender_master mgm ON " +
                    "        mgm.gender_id = tpr.gender_id LEFT JOIN adt.t_medico_legal ml ON ml.admission_id = am.admission_id LEFT " +
                    "        JOIN adt.m_room_master rm ON rm.room_id = ad.room_id LEFT JOIN adt.m_bed_master bm ON bm.bed_id = ad.bed_id " +
                    "        LEFT JOIN opd.t_ABCDE_assessment abcde ON abcde.admission_id = am.admission_id LEFT JOIN " +
                    "        patient.t_patient_diagnosis_appo_mapper pdm ON pdm.admission_id = am.admission_id LEFT JOIN emr.m_diagnosis_master dm " +
                    "        ON dm.diagnosis_id = pdm.diagnosis_id LEFT JOIN adt.t_discharge td ON td.admission_id = am.admission_id LEFT " +
                    "        JOIN adt.m_reason_for_discharge_master disM ON disM.discharge_type_id = td.discharge_type_id AND " +
                    "        td.discharge_status_id = 5 LEFT JOIN t_referal_details rrd ON rrd.admission_id = am.admission_id LEFT " +
                    "        JOIN adt.m_referral_master rmmm ON rrd.referral_id = rmmm.referral_id LEFT JOIN (SELECT cd.admission_id, " +
                    "        STRING_AGG(DISTINCT concat('Dr. ', mdm.first_name,' ',COALESCE(mdm.middle_name,''),' ',mdm.last_name,' | ',msm.speciality_name),'#')" +
                    "        AS referrals FROM opd.t_consult_doctor cd JOIN doctor.m_doctor_master mdm ON mdm.doctor_id = cd.ref_to_doctor_id LEFT JOIN " +
                    "        doctor.m_speciality_master msm ON msm.speciality_id = cd.ref_to_speciality_id GROUP BY cd.admission_id) " +
                    "        ref ON ref.admission_id = am.admission_id    LEFT JOIN ( " +
                    "          SELECT DISTINCT ON (admission_details_id)  " +
                    "           admission_details_id, " +
                    "           current_triage_id " +
                    "    FROM adt.t_admission_triage_history " +
                    "    ORDER BY admission_details_id, updated_date DESC " +
                    "    ) history  " +
                    "ON ad.admission_details_id = history.admission_details_id  WHERE am.visit_type_id = 4 AND am.organization_id = :organizationId AND " +
                    "        am.unit_id = :unitId AND am.created_date BETWEEN :fromDate AND :toDate ORDER BY am.admission_id DESC ";


            Date fromDate = eRPatientDashboard.getFromDate() != null
                            ? TimeZoneComponent.getDateBywithoutZone(
                            eRPatientDashboard.getFromDate() + " 00:00:00")
                            : TimeZoneComponent.getDateBywithoutZone(
                            LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " 00:00:00");

                    Date toDate = eRPatientDashboard.getToDate() != null
                            ? TimeZoneComponent.getDateBywithoutZone(
                            eRPatientDashboard.getToDate() + " 23:59:59")
                            : TimeZoneComponent.getDateByzoneWithDayEndTime(eRPatientDashboard.getUnitId());


            List<ERPatientDashboard> rRPatientDashBoardData = sessionFactory.getCurrentSession()
                    .createSQLQuery(query)
                    .setParameter("organizationId", eRPatientDashboard.getOrganizationId())
                    .setParameter("unitId", eRPatientDashboard.getUnitId())
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .setResultTransformer(Transformers.aliasToBean(ERPatientDashboard.class)).list();

            return new Response(SUCCESS, null, null, rRPatientDashBoardData, null);



        } catch (HibernateException he) {
        logger.error("getERPatientDashBoardData failed", he);
        } catch (Exception e) {
        logger.error("getERPatientDashBoardData failed", e);
        }
        return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }
}

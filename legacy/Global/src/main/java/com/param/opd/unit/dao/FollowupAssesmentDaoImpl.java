package com.param.opd.unit.dao;

import com.param.entity.model.opd.FollowUpAssesmentModel;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.FollowupAssesmentDto;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FollowupAssesmentDaoImpl  extends GenericDao<FollowUpAssesmentModel, Integer> implements IFollowupAssesmentDao, ICommonConstants {

    private static final Logger logger = Logger.getLogger(FollowupAssesmentDaoImpl.class);

    public FollowupAssesmentDaoImpl() {
        super(FollowUpAssesmentModel.class);
    }

    @Override
    public Response saveFollowUpAssesment(FollowupAssesmentDto followupAssesmentDto) throws ApplicationException {
        try{
            FollowUpAssesmentModel followUpAssesmentModel=new FollowUpAssesmentModel();
            followUpAssesmentModel.setFollowUpAssessmentDesc(followupAssesmentDto.getFollowUpAssessmentDesc());
            followUpAssesmentModel.setPatientId(followupAssesmentDto.getPatientId());
            followUpAssesmentModel.setEncounterId(followupAssesmentDto.getEncounterId());
            followUpAssesmentModel.setAdmissionId(followupAssesmentDto.getAdmissionId());
            followUpAssesmentModel.setStatus('A');
            followUpAssesmentModel.setCreatedBy(followupAssesmentDto.getCreatedBy());
            followUpAssesmentModel.setCreatedDate(followupAssesmentDto.getCreatedDate());
            followUpAssesmentModel.setUpdatedDate(followupAssesmentDto.getUpdatedDate());
            followUpAssesmentModel.setUpdatedBy(followupAssesmentDto.getUpdatedBy());
            followUpAssesmentModel.setDoctorId(followupAssesmentDto.getDoctorId());
            followUpAssesmentModel.setOrganizationId(followupAssesmentDto.getOrganizationId());
            followUpAssesmentModel.setUnitId(followupAssesmentDto.getUnitId());
            followUpAssesmentModel.setIsDraft(followupAssesmentDto.getIsDraft());
            followUpAssesmentModel.setAssessmentTime(followupAssesmentDto.getAssessmentTime());
            if(followupAssesmentDto.getFollowUpAssessmentId()!=null && followupAssesmentDto.getFollowUpAssessmentId()>0){
                followUpAssesmentModel.setFollowUpAssessmentId(followupAssesmentDto.getFollowUpAssessmentId());
                update(followUpAssesmentModel);
            }else{
                save(followUpAssesmentModel);
            }
            return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        } catch (HibernateException he) {
            logger.error("saveFollowUpAssesment failed", he);
        } catch (Exception e) {
            logger.error("saveFollowUpAssesment failed", e);
        }
        return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }

    @Override
    public Response getFollowUpAssessmentList(FollowupAssesmentDto followupAssesmentDto) throws ApplicationException {
        try{
            String query =
                    " select concat('Dr. ' || dm.first_name || ' ' || " +
                            " COALESCE(dm.middle_name || ' ', '') || dm.last_name) AS \"doctorName\", " +
                            " fua.follow_up_assessment_id as \"followUpAssessmentId\", " +
                            " fua.follow_up_assessment_desc as \"followUpAssessmentDesc\", " +
                            " fua.created_date as \"assessmentTime\", " +
                            " fua.patient_id as \"patientId\", " +
                            " fua.organization_id as \"organizationId\", " +
                            " fua.unit_id as \"unitId\",fua.is_draft as \"isDraft\" " +
                            " from opd.t_follow_up_assessment fua " +
                            " inner join doctor.m_doctor_master dm on dm.doctor_id = fua.doctor_id " +
                            " where fua.patient_id = :patientId " +
                            " and fua.organization_id = :organizationId " +
                            " and fua.unit_id = :unitId " +
                            " and fua.admission_id = :admissionId " +
                            " order by fua.created_date desc ";


            List<FollowupAssesmentDto> followupAssesmentDtoList = sessionFactory.getCurrentSession()
                    .createSQLQuery(query)
                    .setParameter("organizationId", followupAssesmentDto.getOrganizationId())
                    .setParameter("unitId", followupAssesmentDto.getUnitId())
                    .setParameter("patientId", followupAssesmentDto.getPatientId())
                    .setParameter("admissionId", followupAssesmentDto.getAdmissionId())
                    .setResultTransformer(Transformers.aliasToBean(FollowupAssesmentDto.class)).list();

            return new Response(SUCCESS, null, null, followupAssesmentDtoList, null);

        } catch (HibernateException he) {
            logger.error("getFollowUpAssessmentList failed", he);
        } catch (Exception e) {
            logger.error("getFollowUpAssessmentList failed", e);
        }
        return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }

}

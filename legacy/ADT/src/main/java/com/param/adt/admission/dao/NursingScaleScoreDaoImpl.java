package com.param.adt.admission.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.admission.dto.GcsScaleScoreDTO;
import com.param.adt.admission.dto.NursingScaleScoreDTO;
import com.param.adt.admission.model.NursingScaleScore;
import com.param.adt.common.Response;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;

import in.param.common.dao.GenericDao;
import scala.collection.mutable.StringBuilder;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class NursingScaleScoreDaoImpl extends GenericDao<NursingScaleScore, Integer> implements INursingScaleScoreDao, ICommonConstants {

    @Autowired
    SessionFactory sessionFactory;

    public NursingScaleScoreDaoImpl() {
        super(NursingScaleScore.class);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Response saveNursingScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO) {

        try {
            NursingScaleScore nursingScaleScore = new NursingScaleScore();

            nursingScaleScore.setEncounterId(gcsScaleScoreDTO.getEncounterId());
            nursingScaleScore.setAdmissionId(gcsScaleScoreDTO.getAdmissionId());
            nursingScaleScore.setNursingScoreName(gcsScaleScoreDTO.getNursingScoreName());
            nursingScaleScore.setNursingScaleTotalScore(gcsScaleScoreDTO.getTotalGcsScore());
            nursingScaleScore.setSaveAsDraft(gcsScaleScoreDTO.getSaveAsDraft());
            nursingScaleScore.setTabNumber(gcsScaleScoreDTO.getTabNumber());
            nursingScaleScore.setUnitId(gcsScaleScoreDTO.getUnitId());
            nursingScaleScore.setOrganizationId(gcsScaleScoreDTO.getOrganizationId());
            nursingScaleScore.setStatus('A');
            nursingScaleScore.setCreatedBy(gcsScaleScoreDTO.getCreatedBy());
            nursingScaleScore.setUpdatedBy(gcsScaleScoreDTO.getUpdatedBy());
            nursingScaleScore.setCreatedDate(GlobalCommonDateUtils.getDate(gcsScaleScoreDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
            nursingScaleScore.setUpdatedDate(GlobalCommonDateUtils.getDate(gcsScaleScoreDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
            nursingScaleScore.setIsScoreWith3(gcsScaleScoreDTO.getIsScoreWith3());
            sessionFactory.getCurrentSession().save(nursingScaleScore);

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, nursingScaleScore);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getAllNursingScaleScore(Integer unitId, Integer organizationId, Integer admissionId, Integer encounterId) {
        try{

            StringBuilder query=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_LATEST_NURSING_SCALE_SCORE_BY_ADMISSION_ID").getQueryString());
//            if(admissionId != null && admissionId >0){
//                query.append(" AND nursingScaleScore.admission_id= "+admissionId+" ");
//            }
//            if(encounterId != null && encounterId >0){
//                query.append(" AND nursingScaleScore.encounter_id="+encounterId+" ");
//            }
//
//            query.append(" ORDER BY nursingScaleScore.updated_date desc ");

            List<NursingScaleScoreDTO> getAllNursingScaleScore = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
                    .setParameter("unitId", unitId)
                    .setParameter("organizationId", organizationId)
                    .setParameter("admissionId", admissionId)
                    .setResultTransformer(Transformers.aliasToBean(NursingScaleScoreDTO.class))
                    .list();

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getAllNursingScaleScore, null);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getAllNursingScoreById(Integer unitId, Integer organizationId, Integer admissionId, Integer tabNumber) {
        try{

            StringBuilder query=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_ALL_NURSING_SCALE_SCORE_BY_ADMISSION_ID").getQueryString());

            List<NursingScaleScoreDTO> getAllNursingScaleScore = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
                    .setParameter("unitId", unitId)
                    .setParameter("organizationId", organizationId)
                    .setParameter("admissionId", admissionId)
                    .setParameter("tabNumber", tabNumber)
                    .setResultTransformer(Transformers.aliasToBean(NursingScaleScoreDTO.class))
                    .list();

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getAllNursingScaleScore, null);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response updateNursingScaleScoreDaoById(Integer nursingScaleScoreId, GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {

            NursingScaleScore nursingScaleScore = findById(nursingScaleScoreId);
            nursingScaleScore.setNursingScoreName(gcsScaleScoreDTO.getNursingScoreName());
            nursingScaleScore.setNursingScaleTotalScore(gcsScaleScoreDTO.getTotalGcsScore());
            nursingScaleScore.setUpdatedBy(gcsScaleScoreDTO.getUpdatedBy());
            nursingScaleScore.setUpdatedDate(GlobalCommonDateUtils.getDate(gcsScaleScoreDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

            sessionFactory.getCurrentSession().update(nursingScaleScore);

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

        } catch (Exception e) {
            throw e;
        }
    }


    @Override
    public Response getSpecialityMappedNursingScoreTabs(Integer unitId, Integer organizationId, Integer doctorId, Integer specialityId) {
        try{
            List<Integer> specialityIds = new ArrayList<>();
            if((specialityId == null || specialityId == 0) && (doctorId != null && doctorId != 0)){
                String specialityQuery = """
                        SELECT
                            DISTINCT(specMppr.speciality_id)
                            FROM doctor.t_doctor_speciality_mapper specMppr
                            INNER JOIN doctor.m_doctor_master docMst on
                            docMst.doctor_id = specMppr.doctor_id
                            INNER JOIN doctor.m_speciality_master specMst on
                            specMst.speciality_id = specMppr.speciality_id
                            WHERE
                                specMppr.doctor_id = :doctorId
                                AND docMst.organization_id = :orgId
                                AND specMst.organization_id = :orgId
                                AND docMst.unit_id = :unitId
                                AND specMppr.status = 'A'
                                AND docMst.status = 'A'
                                AND specMst.status = 'A'
                        """;

                specialityIds = sessionFactory.getCurrentSession().createSQLQuery(specialityQuery)
                        .setParameter("unitId", unitId)
                        .setParameter("orgId", organizationId)
                        .setParameter("doctorId", doctorId)
                        .list();
            }

            StringBuilder query=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_SPECIALITY_MAPPED_NURSING_SCORE").getQueryString());

            if(specialityId != null && specialityId > 0){
                specialityIds.add(specialityId);
            }

            Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
                    .setParameter("unitId", unitId)
                    .setParameter("organizationId", organizationId)
                    .setParameterList("specialityIds", specialityIds);

            List<NursingScaleScoreDTO> specialityMappedNursingScoreTabs = sqlQuery
                    .setResultTransformer(Transformers.aliasToBean(NursingScaleScoreDTO.class))
                    .list();

            return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, specialityMappedNursingScoreTabs, null);
        } catch (Exception e) {
            throw e;
        }
    }

}

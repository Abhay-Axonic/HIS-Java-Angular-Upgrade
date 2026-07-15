package com.param.adt.admission.dao;

import com.param.adt.admission.dto.GcsScaleScoreDTO;
import com.param.adt.admission.dto.NursingScaleScoreDTO;
import com.param.adt.admission.model.ApacheScaleScore;
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
public class ApacheScaleScoreDaoImpl extends GenericDao<ApacheScaleScore, Integer> implements IApacheScaleScoreDao, ICommonConstants {

    @Autowired
    SessionFactory sessionFactory;

    public ApacheScaleScoreDaoImpl() {
        super(ApacheScaleScore.class);
        // TODO Auto-generated constructor stub
    }


    @Override
    public Response saveApacheScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {
            ApacheScaleScore apacheScaleScore = new ApacheScaleScore();

            apacheScaleScore.setAdmissionId(gcsScaleScoreDTO.getAdmissionId());
            apacheScaleScore.setEncounterId(gcsScaleScoreDTO.getEncounterId());
            apacheScaleScore.setUnitId(gcsScaleScoreDTO.getUnitId());
            apacheScaleScore.setOrganizationId(gcsScaleScoreDTO.getOrganizationId());
            apacheScaleScore.setTotalApacheScore(Integer.parseInt(gcsScaleScoreDTO.getTotalGcsScore()));
            apacheScaleScore.setSaveAsDraft(gcsScaleScoreDTO.getSaveAsDraft());
            apacheScaleScore.setStatus('A');
            apacheScaleScore.setCreatedBy(gcsScaleScoreDTO.getCreatedBy());
            apacheScaleScore.setUpdatedBy(gcsScaleScoreDTO.getUpdatedBy());
            apacheScaleScore.setCreatedDate(GlobalCommonDateUtils.getDate(gcsScaleScoreDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
            apacheScaleScore.setUpdatedDate(GlobalCommonDateUtils.getDate(gcsScaleScoreDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

            sessionFactory.getCurrentSession().save(apacheScaleScore);

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, apacheScaleScore);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getApacheScaleScoreById(Integer unitId, Integer organizationId, Integer admissionId) {
        try{
            List<NursingScaleScoreDTO> getTotalApacheScaleScore = sessionFactory.getCurrentSession().getNamedQuery("GET_APACHE_BY_ADMISSION_ID")
                    .setParameter("unitId",unitId)
                    .setParameter("organizationId",organizationId)
                    .setParameter("admissionId",admissionId)
                    .setResultTransformer(Transformers.aliasToBean(NursingScaleScoreDTO.class)).list();

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getTotalApacheScaleScore, null);
        }catch (Exception e){
            throw e;
        }
    }
}

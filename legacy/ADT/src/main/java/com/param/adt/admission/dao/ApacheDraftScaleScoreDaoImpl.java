package com.param.adt.admission.dao;

import com.param.adt.admission.dto.ApacheDraftScaleScoreDTO;
import com.param.adt.admission.dto.GcsDraftScaleScoreDTO;
import com.param.adt.admission.dto.GcsScaleScoreDTO;
import com.param.adt.admission.dto.NursingParamScaleScores;
import com.param.adt.admission.model.ApacheDraftScaleScore;
import com.param.adt.admission.model.GcsDraftScaleScore;
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
public class ApacheDraftScaleScoreDaoImpl extends GenericDao<ApacheDraftScaleScore, Integer> implements IApacheDraftScaleScoreDao, ICommonConstants {


    @Autowired
    SessionFactory sessionFactory;

    public ApacheDraftScaleScoreDaoImpl() {
        super(ApacheDraftScaleScore.class);
        // TODO Auto-generated constructor stub
    }


    @Override
    public Response saveAsDraftApacheScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {

            for (NursingParamScaleScores item : gcsScaleScoreDTO.getNursingParamScaleScores()) {
                ApacheDraftScaleScore apacheDraftScaleScore = new ApacheDraftScaleScore();

                apacheDraftScaleScore.setEncounterId(gcsScaleScoreDTO.getEncounterId());
                apacheDraftScaleScore.setAdmissionId(gcsScaleScoreDTO.getAdmissionId());
                apacheDraftScaleScore.setTotalApacheScore(Integer.parseInt(gcsScaleScoreDTO.getTotalGcsScore()));
                apacheDraftScaleScore.setSaveAsDraft('Y');
                apacheDraftScaleScore.setFindingId(item.getFindingId());
                apacheDraftScaleScore.setFindingValue(item.getFindingValue());
                apacheDraftScaleScore.setFindingPoints(item.getFindingPoints());
                apacheDraftScaleScore.setParameterScaleId(item.getParameterScaleId());
                apacheDraftScaleScore.setParameterName(item.getParameterName());
                apacheDraftScaleScore.setUnitId(gcsScaleScoreDTO.getUnitId());
                apacheDraftScaleScore.setOrganizationId(gcsScaleScoreDTO.getOrganizationId());
                apacheDraftScaleScore.setStatus('A');
                apacheDraftScaleScore.setCreatedBy(gcsScaleScoreDTO.getCreatedBy());
                apacheDraftScaleScore.setUpdatedBy(gcsScaleScoreDTO.getUpdatedBy());
                apacheDraftScaleScore.setCreatedDate(GlobalCommonDateUtils.getDate(gcsScaleScoreDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
                apacheDraftScaleScore.setUpdatedDate(GlobalCommonDateUtils.getDate(gcsScaleScoreDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().save(apacheDraftScaleScore);

            }

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getAllApacheDraftScaleScores(Integer unitId, Integer organizationId, Integer admissionId) {
        try {
            List<GcsDraftScaleScoreDTO> getAllDraftApacheScaleScore = sessionFactory.getCurrentSession().getNamedQuery("GET_AS_DRAFT_APACHE_BY_ADMISSION_ID")
                    .setParameter("unitId", unitId)
                    .setParameter("organizationId", organizationId)
                    .setParameter("admissionId", admissionId)
                    .setResultTransformer(Transformers.aliasToBean(GcsDraftScaleScoreDTO.class)).list();

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getAllDraftApacheScaleScore, null);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response inactiveSavedApacheDraftScoreList(List<GcsDraftScaleScoreDTO> getGcsDraftScaleScores) {
        try {

            for (GcsDraftScaleScoreDTO item : getGcsDraftScaleScores) {
                ApacheDraftScaleScore apacheDraftScaleScore = findById(item.getGcsDraftScaleScoreId());
                apacheDraftScaleScore.setStatus('I');
                apacheDraftScaleScore.setUpdatedBy(item.getUpdatedBy());
                apacheDraftScaleScore.setUpdatedDate(GlobalCommonDateUtils.getDate(item.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().update(apacheDraftScaleScore);
            }

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
        } catch (Exception e) {
            throw e;
        }
    }
}

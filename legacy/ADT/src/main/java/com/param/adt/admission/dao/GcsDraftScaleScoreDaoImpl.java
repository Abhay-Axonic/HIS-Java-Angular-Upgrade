package com.param.adt.admission.dao;

import com.param.adt.admission.dto.GcsDraftScaleScoreDTO;
import com.param.adt.admission.dto.GcsScaleScoreDTO;
import com.param.adt.admission.dto.NursingParamScaleScores;
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
public class GcsDraftScaleScoreDaoImpl extends GenericDao<GcsDraftScaleScore, Integer> implements IGcsDraftScaleScoreDao, ICommonConstants {

    @Autowired
    SessionFactory sessionFactory;

    public GcsDraftScaleScoreDaoImpl() {
        super(GcsDraftScaleScore.class);
        // TODO Auto-generated constructor stub
    }


    @Override
    public Response saveAsDraftGcsScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO) {

        try {

            for (NursingParamScaleScores item : gcsScaleScoreDTO.getNursingParamScaleScores()) {
                GcsDraftScaleScore gcsDraftScaleScore = new GcsDraftScaleScore();

                gcsDraftScaleScore.setEncounterId(gcsScaleScoreDTO.getEncounterId());
                gcsDraftScaleScore.setAdmissionId(gcsScaleScoreDTO.getAdmissionId());
                gcsDraftScaleScore.setTotalGcsScore(Integer.parseInt(gcsScaleScoreDTO.getTotalGcsScore()));
                gcsDraftScaleScore.setSaveAsDraft('Y');
                gcsDraftScaleScore.setFindingId(item.getFindingId());
                gcsDraftScaleScore.setFindingValue(item.getFindingValue());
                gcsDraftScaleScore.setFindingPoints(item.getFindingPoints());
                gcsDraftScaleScore.setParameterScaleId(item.getParameterScaleId());
                gcsDraftScaleScore.setParameterName(item.getParameterName());
                gcsDraftScaleScore.setUnitId(gcsScaleScoreDTO.getUnitId());
                gcsDraftScaleScore.setOrganizationId(gcsScaleScoreDTO.getOrganizationId());
                gcsDraftScaleScore.setStatus('A');
                gcsDraftScaleScore.setCreatedBy(gcsScaleScoreDTO.getCreatedBy());
                gcsDraftScaleScore.setUpdatedBy(gcsScaleScoreDTO.getUpdatedBy());
                gcsDraftScaleScore.setCreatedDate(GlobalCommonDateUtils.getDate(gcsScaleScoreDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
                gcsDraftScaleScore.setUpdatedDate(GlobalCommonDateUtils.getDate(gcsScaleScoreDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().save(gcsDraftScaleScore);

            }

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getAllGcsDraftScaleScores(Integer unitId, Integer organizationId, Integer admissionId) {
        try {
            List<GcsDraftScaleScoreDTO> getAllDraftGcsScaleScore = sessionFactory.getCurrentSession().getNamedQuery("GET_AS_DRAFT_GCS_BY_ADMISSION_ID")
                    .setParameter("unitId", unitId)
                    .setParameter("organizationId", organizationId)
                    .setParameter("admissionId", admissionId)
                    .setResultTransformer(Transformers.aliasToBean(GcsDraftScaleScoreDTO.class)).list();

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getAllDraftGcsScaleScore, null);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response inactiveSavedDraftScoreList(List<GcsDraftScaleScoreDTO> getGcsDraftScaleScores) {
        try {

            for (GcsDraftScaleScoreDTO item : getGcsDraftScaleScores) {
                GcsDraftScaleScore gcsDraftScaleScore = findById(item.getGcsDraftScaleScoreId());
                gcsDraftScaleScore.setStatus('I');
                gcsDraftScaleScore.setUpdatedBy(item.getUpdatedBy());
                gcsDraftScaleScore.setUpdatedDate(GlobalCommonDateUtils.getDate(item.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().update(gcsDraftScaleScore);
            }

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
        } catch (Exception e) {
            throw e;
        }
    }


}

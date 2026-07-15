package com.param.adt.admission.dao;

import com.param.adt.admission.dto.GcsDraftScaleScoreDTO;
import com.param.adt.admission.dto.GcsScaleScoreDTO;
import com.param.adt.admission.dto.NursingParamScaleScores;
import com.param.adt.admission.model.ApacheDraftScaleScore;
import com.param.adt.admission.model.BradenDraftScaleScore;
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
public class BradenDraftScaleScoreDaoImpl extends GenericDao<BradenDraftScaleScore, Integer> implements IBradenDraftScaleScoreDao, ICommonConstants {

    @Autowired
    SessionFactory sessionFactory;

    public BradenDraftScaleScoreDaoImpl() {
        super(BradenDraftScaleScore.class);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Response saveAsDraftBradenScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {

            for (NursingParamScaleScores item : gcsScaleScoreDTO.getNursingParamScaleScores()) {
                BradenDraftScaleScore bradenDraftScaleScore = new BradenDraftScaleScore();

                bradenDraftScaleScore.setEncounterId(gcsScaleScoreDTO.getEncounterId());
                bradenDraftScaleScore.setAdmissionId(gcsScaleScoreDTO.getAdmissionId());
                bradenDraftScaleScore.setTotalBradenScore(Integer.parseInt(gcsScaleScoreDTO.getTotalGcsScore()));
                bradenDraftScaleScore.setSaveAsDraft('Y');
                bradenDraftScaleScore.setFindingId(item.getFindingId());
                bradenDraftScaleScore.setFindingValue(item.getFindingValue());
                bradenDraftScaleScore.setFindingPoints(item.getFindingPoints());
                bradenDraftScaleScore.setFindingDescription(item.getFindingDescription());
                bradenDraftScaleScore.setParameterScaleId(item.getParameterScaleId());
                bradenDraftScaleScore.setParameterName(item.getParameterName());
                bradenDraftScaleScore.setUnitId(gcsScaleScoreDTO.getUnitId());
                bradenDraftScaleScore.setOrganizationId(gcsScaleScoreDTO.getOrganizationId());
                bradenDraftScaleScore.setStatus('A');
                bradenDraftScaleScore.setCreatedBy(gcsScaleScoreDTO.getCreatedBy());
                bradenDraftScaleScore.setUpdatedBy(gcsScaleScoreDTO.getUpdatedBy());
                bradenDraftScaleScore.setCreatedDate(GlobalCommonDateUtils.getDate(gcsScaleScoreDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
                bradenDraftScaleScore.setUpdatedDate(GlobalCommonDateUtils.getDate(gcsScaleScoreDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().save(bradenDraftScaleScore);

            }

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getAllBradenDraftScaleScores(Integer unitId, Integer organizationId, Integer admissionId) {
        try {
            List<GcsDraftScaleScoreDTO> getAllDraftBradenScaleScore = sessionFactory.getCurrentSession().getNamedQuery("GET_AS_DRAFT_BRADEN_BY_ADMISSION_ID")
                    .setParameter("unitId", unitId)
                    .setParameter("organizationId", organizationId)
                    .setParameter("admissionId", admissionId)
                    .setResultTransformer(Transformers.aliasToBean(GcsDraftScaleScoreDTO.class)).list();

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getAllDraftBradenScaleScore, null);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response inactiveSavedDraftScoreList(List<GcsDraftScaleScoreDTO> getGcsDraftScaleScores) {
        try {
            for (GcsDraftScaleScoreDTO item : getGcsDraftScaleScores) {
                BradenDraftScaleScore bradenDraftScaleScore = findById(item.getGcsDraftScaleScoreId());
                bradenDraftScaleScore.setStatus('I');
                bradenDraftScaleScore.setUpdatedBy(item.getUpdatedBy());
                bradenDraftScaleScore.setUpdatedDate(GlobalCommonDateUtils.getDate(item.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().update(bradenDraftScaleScore);
            }

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
        } catch (Exception e) {
            throw e;
        }
    }
}

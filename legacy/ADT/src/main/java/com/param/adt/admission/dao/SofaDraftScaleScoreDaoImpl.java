package com.param.adt.admission.dao;

import com.param.adt.admission.dto.GcsDraftScaleScoreDTO;
import com.param.adt.admission.dto.GcsScaleScoreDTO;
import com.param.adt.admission.dto.NursingParamScaleScores;
import com.param.adt.admission.model.ApacheDraftScaleScore;
import com.param.adt.admission.model.SofaDraftScaleScore;
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
public class SofaDraftScaleScoreDaoImpl extends GenericDao<SofaDraftScaleScore, Integer> implements ISofaDraftScaleScoreDao, ICommonConstants {

    @Autowired
    SessionFactory sessionFactory;

    public SofaDraftScaleScoreDaoImpl() {
        super(SofaDraftScaleScore.class);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Response saveAsDraftSofaScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {

            for (NursingParamScaleScores item : gcsScaleScoreDTO.getNursingParamScaleScores()) {
                SofaDraftScaleScore sofaDraftScaleScore = new SofaDraftScaleScore();

                sofaDraftScaleScore.setEncounterId(gcsScaleScoreDTO.getEncounterId());
                sofaDraftScaleScore.setAdmissionId(gcsScaleScoreDTO.getAdmissionId());
                sofaDraftScaleScore.setTotalSofaScore(Integer.parseInt(gcsScaleScoreDTO.getTotalGcsScore()));
                sofaDraftScaleScore.setSaveAsDraft('Y');
                sofaDraftScaleScore.setFindingId(item.getFindingId());
                sofaDraftScaleScore.setFindingValue(item.getFindingValue());
                sofaDraftScaleScore.setFindingPoints(item.getFindingPoints());
                sofaDraftScaleScore.setParameterScaleId(item.getParameterScaleId());
                sofaDraftScaleScore.setParameterName(item.getParameterName());
                sofaDraftScaleScore.setParameterDesc(item.getParameterDesc());
                sofaDraftScaleScore.setUnitId(gcsScaleScoreDTO.getUnitId());
                sofaDraftScaleScore.setOrganizationId(gcsScaleScoreDTO.getOrganizationId());
                sofaDraftScaleScore.setStatus('A');
                sofaDraftScaleScore.setCreatedBy(gcsScaleScoreDTO.getCreatedBy());
                sofaDraftScaleScore.setUpdatedBy(gcsScaleScoreDTO.getUpdatedBy());
                sofaDraftScaleScore.setCreatedDate(GlobalCommonDateUtils.getDate(gcsScaleScoreDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
                sofaDraftScaleScore.setUpdatedDate(GlobalCommonDateUtils.getDate(gcsScaleScoreDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().save(sofaDraftScaleScore);

            }

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getAllAsDraftSofaScaleScore(Integer unitId, Integer organizationId, Integer admissionId) {
        try {
            List<GcsDraftScaleScoreDTO> getAllSofaApacheScaleScore = sessionFactory.getCurrentSession().getNamedQuery("GET_AS_DRAFT_SOFA_BY_ADMISSION_ID")
                    .setParameter("unitId", unitId)
                    .setParameter("organizationId", organizationId)
                    .setParameter("admissionId", admissionId)
                    .setResultTransformer(Transformers.aliasToBean(GcsDraftScaleScoreDTO.class)).list();

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getAllSofaApacheScaleScore, null);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response inactiveSavedSofaDraftScoreList(List<GcsDraftScaleScoreDTO> getGcsDraftScaleScores) {
        try {

            for (GcsDraftScaleScoreDTO item : getGcsDraftScaleScores) {
                SofaDraftScaleScore sofaDraftScaleScore = findById(item.getGcsDraftScaleScoreId());
                sofaDraftScaleScore.setStatus('I');
                sofaDraftScaleScore.setUpdatedBy(item.getUpdatedBy());
                sofaDraftScaleScore.setUpdatedDate(GlobalCommonDateUtils.getDate(item.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().update(sofaDraftScaleScore);
            }

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
        } catch (Exception e) {
            throw e;
        }
    }
}

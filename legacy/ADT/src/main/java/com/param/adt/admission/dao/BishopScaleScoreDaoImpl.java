package com.param.adt.admission.dao;

import com.param.adt.admission.dto.BishopScaleScoreDTO;
import com.param.adt.admission.model.BishopScaleScore;
import com.param.adt.common.Response;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import scala.collection.mutable.StringBuilder;

import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class BishopScaleScoreDaoImpl extends GenericDao<BishopScaleScore, Integer> implements IBishopScaleScoreDao, ICommonConstants {

    @Autowired
    SessionFactory sessionFactory;

    public BishopScaleScoreDaoImpl() {
        super(BishopScaleScore.class);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void saveDraftedBishopScore(BishopScaleScoreDTO bishopScaleScoreDTO) throws ApplicationException {
        try {
                BishopScaleScore bishopDraftedScores = new BishopScaleScore();

                bishopDraftedScores.setEncounterId(bishopScaleScoreDTO.getEncounterId() != null ? bishopScaleScoreDTO.getEncounterId():0);
                bishopDraftedScores.setAdmissionId(bishopScaleScoreDTO.getAdmissionId()!=null ? bishopScaleScoreDTO.getAdmissionId():0);
                bishopDraftedScores.setTotalBishopScore(bishopScaleScoreDTO.getTotalBishopScore());
                bishopDraftedScores.setDilationId(bishopScaleScoreDTO.getDilationId());
                bishopDraftedScores.setLengthId(bishopScaleScoreDTO.getLengthId());
                bishopDraftedScores.setStationId(bishopScaleScoreDTO.getStationId());
                bishopDraftedScores.setConsistencyId(bishopScaleScoreDTO.getConsistencyId());
                bishopDraftedScores.setPositionId(bishopScaleScoreDTO.getPositionId());
                bishopDraftedScores.setUnitId(bishopScaleScoreDTO.getUnitId());
                bishopDraftedScores.setOrganizationId(bishopScaleScoreDTO.getOrganizationId());
                bishopDraftedScores.setStatus('A');
                bishopDraftedScores.setSaveAsDraft(bishopScaleScoreDTO.getSaveAsDraft());
                bishopDraftedScores.setFinalSave(bishopScaleScoreDTO.getFinalSave());
                bishopDraftedScores.setCreatedBy(bishopScaleScoreDTO.getCreatedBy());
                bishopDraftedScores.setUpdatedBy(bishopScaleScoreDTO.getUpdatedBy());
                bishopDraftedScores.setCreatedDate(GlobalCommonDateUtils.getDate(bishopScaleScoreDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
                bishopDraftedScores.setUpdatedDate(GlobalCommonDateUtils.getDate(bishopScaleScoreDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().save(bishopDraftedScores);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response inactiveDraftedBishopScores(List<BishopScaleScoreDTO> bishopScaleScoreDTO) throws ApplicationException {
        try {

            for (BishopScaleScoreDTO item : bishopScaleScoreDTO) {
                BishopScaleScore bishopScaleScore = findById(item.getBishopScoreId());
                bishopScaleScore.setStatus('I');
                bishopScaleScore.setUpdatedBy(item.getUpdatedBy());
                bishopScaleScore.setCreatedBy(item.getUpdatedBy());
                bishopScaleScore.setUpdatedDate(GlobalCommonDateUtils.getDate(item.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
                bishopScaleScore.setCreatedDate(GlobalCommonDateUtils.getDate(item.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().update(bishopScaleScore);
            }

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getAllBishopDraftScaleScores(Integer unitId, Integer organizationId, Integer admissionId, Integer encounterId) throws ApplicationException {
        try {
            StringBuilder query=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_DRAFTED_BISHOP_SCORES_BY_ID").getQueryString());
            if(admissionId != null && admissionId >0){
                query.append(" AND bishopDraftedScores.admission_id= "+admissionId+" ");
            }
            if(encounterId != null && encounterId >0){
                query.append(" AND bishopDraftedScores.encounter_id="+encounterId+" ");
            }
            query.append(" ORDER BY bishopDraftedScores.bishop_score_id desc limit 1");
            List<BishopScaleScoreDTO> getAllDraftBishopScaleScore = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
                    .setParameter("unitId", unitId)
                    .setParameter("organizationId", organizationId)
                    .setResultTransformer(Transformers.aliasToBean(BishopScaleScoreDTO.class)).list();

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getAllDraftBishopScaleScore, null);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response updateBishopScaleScores(Integer Id, BishopScaleScoreDTO bishopScaleScoreDTO) throws ApplicationException {
        try {
                BishopScaleScore bishopScaleScore = findById(Id);
                bishopScaleScore.setTotalBishopScore(bishopScaleScoreDTO.getTotalBishopScore());
                bishopScaleScore.setDilationId(bishopScaleScoreDTO.getDilationId());
                bishopScaleScore.setLengthId(bishopScaleScoreDTO.getLengthId());
                bishopScaleScore.setStationId(bishopScaleScoreDTO.getStationId());
                bishopScaleScore.setConsistencyId(bishopScaleScoreDTO.getConsistencyId());
                bishopScaleScore.setPositionId(bishopScaleScoreDTO.getPositionId());
                bishopScaleScore.setUpdatedBy(bishopScaleScoreDTO.getUpdatedBy());
                bishopScaleScore.setSaveAsDraft(bishopScaleScoreDTO.getSaveAsDraft());
                bishopScaleScore.setFinalSave(bishopScaleScoreDTO.getFinalSave());
                bishopScaleScore.setUpdatedDate(GlobalCommonDateUtils.getDate(bishopScaleScoreDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
                sessionFactory.getCurrentSession().update(bishopScaleScore);

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
        } catch (Exception e) {
            throw e;
        }
    }
}

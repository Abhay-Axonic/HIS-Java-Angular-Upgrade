package com.param.adt.admission.dao;

import com.param.adt.admission.dto.AldreteScaleScoreDto;
import com.param.adt.admission.model.AldreteScaleScore;
import com.param.adt.common.Response;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import scala.collection.mutable.StringBuilder;

import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class AldreteScaleScoreDaoIpml extends GenericDao<AldreteScaleScore, Integer> implements IAldreteScaleScoreDao, ICommonConstants {

    @Autowired
    SessionFactory sessionFactory;

    public AldreteScaleScoreDaoIpml() {
        super(AldreteScaleScore.class);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void saveAldreteScore(AldreteScaleScoreDto aldreteScaleScoreDto) throws ApplicationException {
        try {
            AldreteScaleScore aldreteScaleScores = new AldreteScaleScore();

            aldreteScaleScores.setEncounterId(aldreteScaleScoreDto.getEncounterId() != null ? aldreteScaleScoreDto.getEncounterId():0);
            aldreteScaleScores.setAdmissionId(aldreteScaleScoreDto.getAdmissionId()!=null ? aldreteScaleScoreDto.getAdmissionId():0);
            aldreteScaleScores.setTotalAldreteScore(aldreteScaleScoreDto.getTotalAldreteScore());
            aldreteScaleScores.setActivityId(aldreteScaleScoreDto.getActivityId());
            aldreteScaleScores.setRespirationId(aldreteScaleScoreDto.getRespirationId());
            aldreteScaleScores.setCirculationId(aldreteScaleScoreDto.getCirculationId());
            aldreteScaleScores.setConsciousnessId(aldreteScaleScoreDto.getConsciousnessId());
            aldreteScaleScores.setOxygenSaturationId(aldreteScaleScoreDto.getOxygenSaturationId());
            aldreteScaleScores.setUnitId(aldreteScaleScoreDto.getUnitId());
            aldreteScaleScores.setOrganizationId(aldreteScaleScoreDto.getOrganizationId());
            aldreteScaleScores.setStatus('A');
            aldreteScaleScores.setSaveAsDraft(aldreteScaleScoreDto.getSaveAsDraft());
            aldreteScaleScores.setFinalSave(aldreteScaleScoreDto.getFinalSave());
            aldreteScaleScores.setCreatedBy(aldreteScaleScoreDto.getCreatedBy());
            aldreteScaleScores.setUpdatedBy(aldreteScaleScoreDto.getUpdatedBy());
            aldreteScaleScores.setCreatedDate(GlobalCommonDateUtils.getDate(aldreteScaleScoreDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
            aldreteScaleScores.setUpdatedDate(GlobalCommonDateUtils.getDate(aldreteScaleScoreDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

            sessionFactory.getCurrentSession().save(aldreteScaleScores);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response updateAldreteScaleScores(Integer aldreteScoreId, AldreteScaleScoreDto aldreteScaleScoreDto) throws ApplicationException {

        AldreteScaleScore aldreteScaleScores = findById(aldreteScoreId);
        aldreteScaleScores.setTotalAldreteScore(aldreteScaleScoreDto.getTotalAldreteScore());
        aldreteScaleScores.setActivityId(aldreteScaleScoreDto.getActivityId());
        aldreteScaleScores.setRespirationId(aldreteScaleScoreDto.getRespirationId());
        aldreteScaleScores.setCirculationId(aldreteScaleScoreDto.getCirculationId());
        aldreteScaleScores.setConsciousnessId(aldreteScaleScoreDto.getConsciousnessId());
        aldreteScaleScores.setOxygenSaturationId(aldreteScaleScoreDto.getOxygenSaturationId());
        aldreteScaleScores.setUpdatedBy(aldreteScaleScoreDto.getUpdatedBy());
        aldreteScaleScores.setSaveAsDraft(aldreteScaleScoreDto.getSaveAsDraft());
        aldreteScaleScores.setFinalSave(aldreteScaleScoreDto.getFinalSave());
        aldreteScaleScores.setUpdatedDate(GlobalCommonDateUtils.getDate(aldreteScaleScoreDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
        sessionFactory.getCurrentSession().update(aldreteScaleScores);

        return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
    }

    @Override
    public Response inactiveDraftedAldreteScores(List<AldreteScaleScoreDto> aldreteScaleScoreDto) throws ApplicationException {
        try {

            for (AldreteScaleScoreDto item : aldreteScaleScoreDto) {
                AldreteScaleScore aldreteScaleScore = findById(item.getAldreteScoreId());
                aldreteScaleScore.setStatus('I');
                aldreteScaleScore.setUpdatedBy(item.getUpdatedBy());
                aldreteScaleScore.setCreatedBy(item.getUpdatedBy());
                aldreteScaleScore.setUpdatedDate(GlobalCommonDateUtils.getDate(item.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
                aldreteScaleScore.setCreatedDate(GlobalCommonDateUtils.getDate(item.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().update(aldreteScaleScore);
            }

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getAldreteDraftScaleScores(Integer unitId, Integer organizationId, Integer admissionId, Integer encounterId) throws ApplicationException {
        try {
            StringBuilder query=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_DRAFTED_ALDRTE_SCORES_BY_ID").getQueryString());
            if(admissionId != null && admissionId >0){
                query.append(" AND aldreteDraftedScores.admission_id=:admissionId ");
            }
            if(encounterId != null && encounterId >0){
                query.append(" AND aldreteDraftedScores.encounter_id=:encounterId ");
            }
            query.append(" ORDER BY aldreteDraftedScores.aldrete_score_id desc limit 1");
            Query sql = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
                    .setParameter("unitId", unitId)
                    .setParameter("organizationId", organizationId);
            if(admissionId != null && admissionId >0){
                sql.setParameter("admissionId", admissionId);
            }
            if(encounterId != null && encounterId >0){
                sql.setParameter("encounterId", encounterId);
            }
            List<AldreteScaleScoreDto> getDraftAldreteScaleScore = sql.setResultTransformer(Transformers.aliasToBean(AldreteScaleScoreDto.class)).list();

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getDraftAldreteScaleScore, null);
        } catch (Exception e) {
            throw e;
        }
    }

}

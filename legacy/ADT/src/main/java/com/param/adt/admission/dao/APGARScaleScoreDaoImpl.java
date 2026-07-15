package com.param.adt.admission.dao;

import com.param.adt.admission.dto.APGARScaleScoreDto;
import com.param.adt.admission.model.APGARScaleScore;
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
public class APGARScaleScoreDaoImpl extends GenericDao<APGARScaleScore, Integer> implements IAPGARScaleScoreDao, ICommonConstants {

    @Autowired
    SessionFactory sessionFactory;

    public APGARScaleScoreDaoImpl() {
        super(APGARScaleScore.class);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void saveApgarScore(APGARScaleScoreDto apgarScaleScoreDto) throws ApplicationException {
        try {
            APGARScaleScore apgarScaleScores = new APGARScaleScore();

            apgarScaleScores.setEncounterId(apgarScaleScoreDto.getEncounterId() != null ? apgarScaleScoreDto.getEncounterId():0);
            apgarScaleScores.setAdmissionId(apgarScaleScoreDto.getAdmissionId()!=null ? apgarScaleScoreDto.getAdmissionId():0);
            apgarScaleScores.setTotalApgarScore(apgarScaleScoreDto.getTotalApgarScore());
            apgarScaleScores.setActivityId(apgarScaleScoreDto.getActivityId());
            apgarScaleScores.setRespirationId(apgarScaleScoreDto.getRespirationId());
            apgarScaleScores.setGrimaceId(apgarScaleScoreDto.getGrimaceId());
            apgarScaleScores.setPulseId(apgarScaleScoreDto.getPulseId());
            apgarScaleScores.setAppearanceId(apgarScaleScoreDto.getAppearanceId());
            apgarScaleScores.setUnitId(apgarScaleScoreDto.getUnitId());
            apgarScaleScores.setOrganizationId(apgarScaleScoreDto.getOrganizationId());
            apgarScaleScores.setStatus('A');
            apgarScaleScores.setSaveAsDraft(apgarScaleScoreDto.getSaveAsDraft());
            apgarScaleScores.setFinalSave(apgarScaleScoreDto.getFinalSave());
            apgarScaleScores.setCreatedBy(apgarScaleScoreDto.getCreatedBy());
            apgarScaleScores.setUpdatedBy(apgarScaleScoreDto.getUpdatedBy());
            apgarScaleScores.setCreatedDate(GlobalCommonDateUtils.getDate(apgarScaleScoreDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
            apgarScaleScores.setUpdatedDate(GlobalCommonDateUtils.getDate(apgarScaleScoreDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

            sessionFactory.getCurrentSession().save(apgarScaleScores);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response updateAldreteScaleScores(Integer apgarScoreId, APGARScaleScoreDto apgarScaleScoreDto) throws ApplicationException {
        
        APGARScaleScore apgarScaleScores = findById(apgarScoreId);
        apgarScaleScores.setTotalApgarScore(apgarScaleScoreDto.getTotalApgarScore());
        apgarScaleScores.setActivityId(apgarScaleScoreDto.getActivityId());
        apgarScaleScores.setRespirationId(apgarScaleScoreDto.getRespirationId());
        apgarScaleScores.setGrimaceId(apgarScaleScoreDto.getGrimaceId());
        apgarScaleScores.setPulseId(apgarScaleScoreDto.getPulseId());
        apgarScaleScores.setAppearanceId(apgarScaleScoreDto.getAppearanceId());
        apgarScaleScores.setUpdatedBy(apgarScaleScoreDto.getUpdatedBy());
        apgarScaleScores.setSaveAsDraft(apgarScaleScoreDto.getSaveAsDraft());
        apgarScaleScores.setFinalSave(apgarScaleScoreDto.getFinalSave());
        apgarScaleScores.setUpdatedDate(GlobalCommonDateUtils.getDate(apgarScaleScoreDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
        sessionFactory.getCurrentSession().update(apgarScaleScores);

        return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
    }

    @Override
    public Response inactiveDraftedAPGARScores(List<APGARScaleScoreDto> apgarScaleScoreDto) throws ApplicationException {
        try {

            for (APGARScaleScoreDto item : apgarScaleScoreDto) {
                APGARScaleScore apgarScaleScores = findById(item.getApgarScoreId());
                apgarScaleScores.setStatus('I');
                apgarScaleScores.setUpdatedBy(item.getUpdatedBy());
                apgarScaleScores.setCreatedBy(item.getUpdatedBy());
                apgarScaleScores.setUpdatedDate(GlobalCommonDateUtils.getDate(item.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
                apgarScaleScores.setCreatedDate(GlobalCommonDateUtils.getDate(item.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().update(apgarScaleScores);
            }

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getAPGARDraftScaleScores(Integer unitId, Integer organizationId, Integer admissionId, Integer encounterId) throws ApplicationException {
        try {
            StringBuilder query=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_DRAFTED_APGAR_SCORES_BY_ID").getQueryString());
            if(admissionId != null && admissionId >0){
                query.append(" AND agparDraftedScores.admission_id=:admissionId ");
            }
            if(encounterId != null && encounterId >0){
                query.append(" AND agparDraftedScores.encounter_id=:encounterId ");
            }
            query.append(" ORDER BY agparDraftedScores.apgar_score_id desc limit 1");
            Query sql = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
                    .setParameter("unitId", unitId)
                    .setParameter("organizationId", organizationId);
            if(admissionId != null && admissionId >0){
                sql.setParameter("admissionId", admissionId);
            }
            if(encounterId != null && encounterId >0){
                sql.setParameter("encounterId", encounterId);
            }
            List<APGARScaleScoreDto> getDraftApgarScaleScores = sql.setResultTransformer(Transformers.aliasToBean(APGARScaleScoreDto.class)).list();

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getDraftApgarScaleScores, null);
        } catch (Exception e) {
            throw e;
        }
    }

}

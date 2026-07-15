package com.param.adt.admission.dao;

import com.param.adt.admission.dto.GcsScaleScoreDTO;
import com.param.adt.admission.dto.NursingScaleScoreDTO;
import com.param.adt.admission.model.GcsDraftScaleScore;
import com.param.adt.admission.model.GcsScaleScore;
import com.param.adt.admission.model.SofaScaleScore;
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
public class SofaScaleScoreDaoImpl extends GenericDao<SofaScaleScore, Integer> implements ISofaScaleScoreDao, ICommonConstants {

    @Autowired
    SessionFactory sessionFactory;

    public SofaScaleScoreDaoImpl() {
        super(SofaScaleScore.class);
        // TODO Auto-generated constructor stub
    }


    @Override
    public Response saveSofaScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {
            SofaScaleScore sofaScaleScore = new SofaScaleScore();

            sofaScaleScore.setAdmissionId(gcsScaleScoreDTO.getAdmissionId());
            sofaScaleScore.setEncounterId(gcsScaleScoreDTO.getEncounterId());
            sofaScaleScore.setUnitId(gcsScaleScoreDTO.getUnitId());
            sofaScaleScore.setOrganizationId(gcsScaleScoreDTO.getOrganizationId());
            sofaScaleScore.setTotalSofaScore(Integer.parseInt(gcsScaleScoreDTO.getTotalGcsScore()));
            sofaScaleScore.setSaveAsDraft(gcsScaleScoreDTO.getSaveAsDraft());
            sofaScaleScore.setStatus('A');
            sofaScaleScore.setCreatedBy(gcsScaleScoreDTO.getCreatedBy());
            sofaScaleScore.setUpdatedBy(gcsScaleScoreDTO.getUpdatedBy());
            sofaScaleScore.setCreatedDate(GlobalCommonDateUtils.getDate(gcsScaleScoreDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
            sofaScaleScore.setUpdatedDate(GlobalCommonDateUtils.getDate(gcsScaleScoreDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

            sessionFactory.getCurrentSession().save(sofaScaleScore);

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, sofaScaleScore);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getSofaScaleScoreById(Integer unitId, Integer organizationId, Integer admissionId) {
        try {
            List<NursingScaleScoreDTO> getTotalSofaScaleScore = sessionFactory.getCurrentSession().getNamedQuery("GET_SOFA_BY_ADMISSION_ID")
                    .setParameter("unitId", unitId)
                    .setParameter("organizationId", organizationId)
                    .setParameter("admissionId", admissionId)
                    .setResultTransformer(Transformers.aliasToBean(NursingScaleScoreDTO.class)).list();

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getTotalSofaScaleScore, null);
        } catch (Exception e) {
            throw e;
        }
    }

}

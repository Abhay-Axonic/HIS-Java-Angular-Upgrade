package com.param.adt.admission.dao;

import com.param.adt.admission.dto.GcsScaleScoreDTO;
import com.param.adt.admission.dto.NursingScaleScoreDTO;
import com.param.adt.admission.model.ApacheScaleScore;
import com.param.adt.admission.model.BradenScaleScore;
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
public class BradenScaleScoreDaoImpl extends GenericDao<BradenScaleScore, Integer> implements IBradenScaleScoreDao, ICommonConstants {

    @Autowired
    SessionFactory sessionFactory;

    public BradenScaleScoreDaoImpl() {
        super(BradenScaleScore.class);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Response saveBradenScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {
            BradenScaleScore bradenScaleScore = new BradenScaleScore();

            bradenScaleScore.setEncounterId(gcsScaleScoreDTO.getEncounterId());
            bradenScaleScore.setAdmissionId(gcsScaleScoreDTO.getAdmissionId());
            bradenScaleScore.setTotalBradenScore(Integer.parseInt(gcsScaleScoreDTO.getTotalGcsScore()));
            bradenScaleScore.setSaveAsDraft(gcsScaleScoreDTO.getSaveAsDraft());
            bradenScaleScore.setUnitId(gcsScaleScoreDTO.getUnitId());
            bradenScaleScore.setOrganizationId(gcsScaleScoreDTO.getOrganizationId());
            bradenScaleScore.setStatus('A');
            bradenScaleScore.setCreatedBy(gcsScaleScoreDTO.getCreatedBy());
            bradenScaleScore.setUpdatedBy(gcsScaleScoreDTO.getUpdatedBy());
            bradenScaleScore.setCreatedDate(GlobalCommonDateUtils.getDate(gcsScaleScoreDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
            bradenScaleScore.setUpdatedDate(GlobalCommonDateUtils.getDate(gcsScaleScoreDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

            sessionFactory.getCurrentSession().save(bradenScaleScore);

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, bradenScaleScore);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getBradenScaleScoreById(Integer unitId, Integer organizationId, Integer admissionId) {
        try{
            List<NursingScaleScoreDTO> getTotalBradenScaleScore = sessionFactory.getCurrentSession().getNamedQuery("GET_BRADEN_BY_ADMISSION_ID")
                    .setParameter("unitId",unitId)
                    .setParameter("organizationId",organizationId)
                    .setParameter("admissionId",admissionId)
                    .setResultTransformer(Transformers.aliasToBean(NursingScaleScoreDTO.class)).list();

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getTotalBradenScaleScore, null);
        }catch (Exception e){
            throw e;
        }
    }
}

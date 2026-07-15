package com.param.adt.admission.dao;

import com.param.adt.admission.dto.GcsDraftScaleScoreDTO;
import com.param.adt.admission.dto.GcsScaleScoreDTO;
import com.param.adt.admission.dto.NursingScaleScoreDTO;
import com.param.adt.admission.model.GcsScaleScore;
import com.param.adt.common.Response;
import com.param.adt.discharge.dto.DischargeSummaryFieldDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.utility.TimeZoneComponent;
import in.param.common.dao.GenericDao;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class GcsScaleScoreDaoImpl extends GenericDao<GcsScaleScore, Integer> implements IGcsScaleScoreDao, ICommonConstants {

    @Autowired
    SessionFactory sessionFactory;

    public GcsScaleScoreDaoImpl() {
        super(GcsScaleScore.class);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Response saveGcsScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {
            GcsScaleScore gcsScaleScore = new GcsScaleScore();

            gcsScaleScore.setAdmissionId(gcsScaleScoreDTO.getAdmissionId());
            gcsScaleScore.setEncounterId(gcsScaleScoreDTO.getEncounterId());
            gcsScaleScore.setUnitId(gcsScaleScoreDTO.getUnitId());
            gcsScaleScore.setOrganizationId(gcsScaleScoreDTO.getOrganizationId());
            gcsScaleScore.setTotalGcsScore(Integer.parseInt(gcsScaleScoreDTO.getTotalGcsScore()));
            gcsScaleScore.setSaveAsDraft(gcsScaleScoreDTO.getSaveAsDraft());
            gcsScaleScore.setStatus('A');
            gcsScaleScore.setCreatedBy(gcsScaleScoreDTO.getCreatedBy());
            gcsScaleScore.setUpdatedBy(gcsScaleScoreDTO.getUpdatedBy());
            gcsScaleScore.setCreatedDate(GlobalCommonDateUtils.getDate(gcsScaleScoreDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
            gcsScaleScore.setUpdatedDate(GlobalCommonDateUtils.getDate(gcsScaleScoreDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

            sessionFactory.getCurrentSession().save(gcsScaleScore);

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, gcsScaleScore);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getGcsScaleScoreById(Integer unitId, Integer organizationId, Integer admissionId) {

        try{
            List<NursingScaleScoreDTO> getTotalGcsScaleScore = sessionFactory.getCurrentSession().getNamedQuery("GET_GCS_BY_ADMISSION_ID")
                    .setParameter("unitId",unitId)
                    .setParameter("organizationId",organizationId)
                    .setParameter("admissionId",admissionId)
                    .setResultTransformer(Transformers.aliasToBean(NursingScaleScoreDTO.class)).list();

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getTotalGcsScaleScore, null);
        }catch (Exception e){
            throw e;
        }

    }


}

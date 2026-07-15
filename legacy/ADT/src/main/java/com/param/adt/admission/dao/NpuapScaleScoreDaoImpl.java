package com.param.adt.admission.dao;

import com.param.adt.admission.dto.GcsScaleScoreDTO;
import com.param.adt.admission.model.BradenScaleScore;
import com.param.adt.admission.model.NpuapScaleScore;
import com.param.adt.common.Response;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import in.param.common.dao.GenericDao;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class NpuapScaleScoreDaoImpl extends GenericDao<NpuapScaleScore, Integer> implements INpuapScaleScoreDao, ICommonConstants {

    @Autowired
    SessionFactory sessionFactory;

    public NpuapScaleScoreDaoImpl() {
        super(NpuapScaleScore.class);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Response saveNpuapScaleScore(GcsScaleScoreDTO gcsScaleScoreDTO) {
        try {


            NpuapScaleScore npuapScaleScore = new NpuapScaleScore();

            npuapScaleScore.setAdmissionId(gcsScaleScoreDTO.getAdmissionId());
            npuapScaleScore.setEncounterId(gcsScaleScoreDTO.getEncounterId());
            npuapScaleScore.setNpuap(gcsScaleScoreDTO.getNpuap());
            npuapScaleScore.setEpuap(gcsScaleScoreDTO.getEpuap());
            npuapScaleScore.setTotalNpuapScore(gcsScaleScoreDTO.getTotalGcsScore());
            npuapScaleScore.setDefiningCharacteristics(gcsScaleScoreDTO.getDefiningCharacteristics());
            npuapScaleScore.setUnitId(gcsScaleScoreDTO.getUnitId());
            npuapScaleScore.setOrganizationId(gcsScaleScoreDTO.getOrganizationId());
            npuapScaleScore.setStatus('A');
            npuapScaleScore.setCreatedBy(gcsScaleScoreDTO.getCreatedBy());
            npuapScaleScore.setUpdatedBy(gcsScaleScoreDTO.getUpdatedBy());
            npuapScaleScore.setCreatedDate(GlobalCommonDateUtils.getDate(gcsScaleScoreDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
            npuapScaleScore.setUpdatedDate(GlobalCommonDateUtils.getDate(gcsScaleScoreDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

            sessionFactory.getCurrentSession().save(npuapScaleScore);

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, npuapScaleScore);

        }catch (Exception e){
            throw e;
        }

    }

    @Override
    public Response getNpuapScaleScoreById(Integer unitId, Integer organizationId, Integer admissionId) {
        return null;
    }
}

package com.param.adt.admission.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.admission.dto.FallRiskScoreDto;
import com.param.adt.admission.model.FallRiskScore;
import com.param.adt.common.Response;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
@Repository
public class FallRiskScoreDaoImpl extends GenericDao<FallRiskScore, Integer> implements IFallRiskScoreDao, ICommonConstants {

    @Autowired
    SessionFactory sessionFactory;

    public FallRiskScoreDaoImpl() {
        super(FallRiskScore.class);
    }

	
	@Override
	public Response saveFallRiskScore(FallRiskScoreDto dto) throws ApplicationException {


        try {
        	FallRiskScore nursingScaleScore = new FallRiskScore();

            nursingScaleScore.setEncounterId(dto.getEncounterId());
            nursingScaleScore.setAdmissionId(dto.getAdmissionId());
            nursingScaleScore.setTotalFallRiskScore(dto.getTotalFallRiskScore());
            nursingScaleScore.setUnitId(dto.getUnitId());
            nursingScaleScore.setOrganizationId(dto.getOrganizationId());
            nursingScaleScore.setStatus('A');
            nursingScaleScore.setCreatedBy(dto.getCreatedBy());
            nursingScaleScore.setUpdatedBy(dto.getUpdatedBy());
            nursingScaleScore.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
            nursingScaleScore.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

            sessionFactory.getCurrentSession().save(nursingScaleScore);

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, nursingScaleScore);

        } catch (Exception e) {
            throw e;
        }
    
	}

}

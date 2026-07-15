package com.param.adt.admission.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.admission.dto.NutritionalAssessmentScoreDTO;
import com.param.adt.admission.model.NutritionalAssessmentScoreMaster;
import com.param.adt.common.Response;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings("rawtypes")
public class NutritionalAssessmentScoreMasterDaoImpl extends GenericDao<NutritionalAssessmentScoreMaster, Integer> implements INutritionalAssessmentScoreMasterDao, ICommonConstants {

    @Autowired
    SessionFactory sessionFactory;

    public NutritionalAssessmentScoreMasterDaoImpl() {
        super(NutritionalAssessmentScoreMaster.class);
    }

	
	@Override
	public Response saveOrUpdateNutritionalAssessmentScoreMaster(
			NutritionalAssessmentScoreDTO nutritionalAssessmentScoreDTO) throws ApplicationException {

        try{

                NutritionalAssessmentScoreMaster nutritionalAssessmentScore = new NutritionalAssessmentScoreMaster();

                nutritionalAssessmentScore.setEncounterId(nutritionalAssessmentScoreDTO.getEncounterId());
                nutritionalAssessmentScore.setAdmissionId(nutritionalAssessmentScoreDTO.getAdmissionId());
                nutritionalAssessmentScore.setPatientWeight(nutritionalAssessmentScoreDTO.getPatientWeight());
                nutritionalAssessmentScore.setPatientHeight(nutritionalAssessmentScoreDTO.getPatientHeight());
                nutritionalAssessmentScore.setPatientBmi(nutritionalAssessmentScoreDTO.getPatientBmi());
                nutritionalAssessmentScore.setPatientKneeHeight(nutritionalAssessmentScoreDTO.getPatientKneeHeight());
                nutritionalAssessmentScore.setUnitId(nutritionalAssessmentScoreDTO.getUnitId());
                nutritionalAssessmentScore.setOrganizationId(nutritionalAssessmentScoreDTO.getOrganizationId());
                nutritionalAssessmentScore.setStatus('A');
                nutritionalAssessmentScore.setCreatedBy(nutritionalAssessmentScoreDTO.getCreatedBy());
                nutritionalAssessmentScore.setTotalNutritionalAssessmentScore(nutritionalAssessmentScoreDTO.getTotalNutritionalAssessmentScore());
                nutritionalAssessmentScore.setUpdatedBy(nutritionalAssessmentScoreDTO.getUpdatedBy());
                nutritionalAssessmentScore.setCreatedDate(GlobalCommonDateUtils.getDate(nutritionalAssessmentScoreDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
                nutritionalAssessmentScore.setUpdatedDate(GlobalCommonDateUtils.getDate(nutritionalAssessmentScoreDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

                if(nutritionalAssessmentScoreDTO.getNutritionalAssessmentScoreMasterId() != null) {
                	nutritionalAssessmentScore.setNutritionalAssessmentScoreMasterId(nutritionalAssessmentScoreDTO.getNutritionalAssessmentScoreMasterId());
                	
                	nutritionalAssessmentScore = update(nutritionalAssessmentScore);
                	
                }else {
                	
                	nutritionalAssessmentScore = save(nutritionalAssessmentScore);
                	
                }
                
                
                
            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, nutritionalAssessmentScore);

        }catch (Exception e){
            throw e;
        }
    
	}
    
    
}

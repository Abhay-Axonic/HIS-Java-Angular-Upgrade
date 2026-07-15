package com.param.adt.admission.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.param.adt.admission.dao.INutritionalAssessmentScoreDao;
import com.param.adt.admission.dao.INutritionalAssessmentScoreMasterDao;
import com.param.adt.admission.dto.NutritionalAssessmentScoreDTO;
import com.param.adt.admission.model.NutritionalAssessmentScoreMaster;
import com.param.adt.common.Response;
import com.param.global.common.ICommonConstants;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes"})
public class NutritionalAssessmentScoreServiceImpl  implements INutritionalAssessmentScoreService, ICommonConstants {

    @Autowired
    private INutritionalAssessmentScoreDao iNutritionalAssessmentScoreDao;
    
    @Autowired
    INutritionalAssessmentScoreMasterDao iNutritionalAssessmentScoreMasterDao;


    @SuppressWarnings("unchecked")
	public Response saveNutritionalAssessmentScore(NutritionalAssessmentScoreDTO nutritionalAssessmentScoreDTO) {
        try{
            Response getListResponse = iNutritionalAssessmentScoreDao.getSaveAsDraftNutritionalAssessmentScore(nutritionalAssessmentScoreDTO.getUnitId(), nutritionalAssessmentScoreDTO.getOrganizationId(), nutritionalAssessmentScoreDTO.getAdmissionId());

            if(!getListResponse.getListObject().isEmpty()){
                iNutritionalAssessmentScoreDao.inactiveSavedDraftScoreList(getListResponse.getListObject());
            }

            Response res =   iNutritionalAssessmentScoreMasterDao.saveOrUpdateNutritionalAssessmentScoreMaster(nutritionalAssessmentScoreDTO);
            
            if(res.getObject() != null) {
            	 NutritionalAssessmentScoreMaster nutritionalAssessmentScore =  (NutritionalAssessmentScoreMaster) res.getObject();
            	 nutritionalAssessmentScoreDTO.setNutritionalAssessmentScoreMasterId(nutritionalAssessmentScore.getNutritionalAssessmentScoreMasterId());
            }
           
            return iNutritionalAssessmentScoreDao.saveAsDraftNutritionalAssessmentScore(nutritionalAssessmentScoreDTO);

        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Response getSaveAsDraftNutritionalAssessmentScore(Integer unitId, Integer organizationId, Integer admissionId) {
        try{
            return iNutritionalAssessmentScoreDao.getSaveAsDraftNutritionalAssessmentScore(unitId, organizationId, admissionId);
        }catch (Exception e){
            throw e;
        }
    }

	@Override
	public com.param.global.common.Response getNutritionalIntailAnethesthiaAssessmentScore(Integer admissionId) throws ApplicationException {
		try{
		 return iNutritionalAssessmentScoreDao.getNutritionalIntailAnethesthiaAssessmentScore(admissionId);
	}catch (Exception e){
        throw e;
    }
  }
	
}

package com.param.adt.admission.service;

import com.param.adt.admission.dao.IHandHygieneScoreDao;
import com.param.adt.admission.dao.INutritionalAssessmentScoreDao;
import com.param.adt.admission.dto.HandHygieneScoreDTO;
import com.param.adt.admission.dto.NutritionalAssessmentScoreDTO;
import com.param.adt.common.Response;
import com.param.global.common.ICommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({"rawtypes"})
public class HandHygieneScoreServiceImpl implements IHandHygieneScoreService, ICommonConstants {

    @Autowired
    private IHandHygieneScoreDao iHandHygieneScoreDao;


    @Override
    public Response saveAsDraftHandHygieneScore(HandHygieneScoreDTO handHygieneScoreDTO) {
        try{
            Response getListResponse = iHandHygieneScoreDao.getSaveAsDraftHandHygieneScore(handHygieneScoreDTO.getUnitId(),
                    handHygieneScoreDTO.getOrganizationId(),handHygieneScoreDTO.getWardId());

            if(!getListResponse.getListObject().isEmpty()){
                iHandHygieneScoreDao.inactiveSavedDraftScoreList(getListResponse.getListObject());
            }

            return iHandHygieneScoreDao.saveAsDraftHandHygieneScore(handHygieneScoreDTO);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Response saveHandHygieneScore(HandHygieneScoreDTO handHygieneScoreDTO) {
        try{
            Response getListResponse = iHandHygieneScoreDao.getSaveAsDraftHandHygieneScore(handHygieneScoreDTO.getUnitId(),
                    handHygieneScoreDTO.getOrganizationId(),handHygieneScoreDTO.getWardId());

            if(!getListResponse.getListObject().isEmpty()){
                iHandHygieneScoreDao.inactiveSavedDraftScoreList(getListResponse.getListObject());
            }

            return iHandHygieneScoreDao.saveAsDraftHandHygieneScore(handHygieneScoreDTO);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Response getSaveAsDraftHandHygieneScore(Integer unitId, Integer organizationId, Integer wardId) {
        try{
            return iHandHygieneScoreDao.getSaveAsDraftHandHygieneScore(unitId, organizationId, wardId);
        }catch (Exception e){
            throw e;
        }
    }
}

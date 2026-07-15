package com.param.adt.admission.dao;

import com.param.adt.admission.dto.HandHygieneReturnScoreDTO;
import com.param.adt.admission.dto.HandHygieneScoreDTO;
import com.param.adt.admission.dto.HandHygieneScoreListDTO;
import com.param.adt.admission.dto.NabhAssessmentDTO;
import com.param.adt.admission.model.HandHygieneScore;
import com.param.adt.admission.model.NutritionalAssessmentScore;
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
public class HandHygieneScoreDaoImpl extends GenericDao<HandHygieneScore, Integer> implements IHandHygieneScoreDao, ICommonConstants {

    @Autowired
    SessionFactory sessionFactory;

    public HandHygieneScoreDaoImpl() {
        super(HandHygieneScore.class);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Response saveAsDraftHandHygieneScore(HandHygieneScoreDTO handHygieneScoreDTO) {
        try{

            for(HandHygieneScoreListDTO handHygieneScoreListDTO: handHygieneScoreDTO.getListOfSelectedCheckbox()){
                HandHygieneScore handHygieneScore = new HandHygieneScore();

                handHygieneScore.setWardId(handHygieneScoreDTO.getWardId());
                handHygieneScore.setSaveAsDraft(handHygieneScoreDTO.getSaveAsDraft());
                handHygieneScore.setQuestionId(handHygieneScoreListDTO.getQuestionId());
                handHygieneScore.setOptionId(handHygieneScoreListDTO.getOptionId());
                handHygieneScore.setUnitId(handHygieneScoreDTO.getUnitId());
                handHygieneScore.setOrganizationId(handHygieneScoreDTO.getOrganizationId());
                handHygieneScore.setStatus('A');
                handHygieneScore.setCreatedBy(handHygieneScoreDTO.getCreatedBy());
                handHygieneScore.setUpdatedBy(handHygieneScoreDTO.getUpdatedBy());
                handHygieneScore.setCreatedDate(GlobalCommonDateUtils.getDate(handHygieneScoreDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
                handHygieneScore.setUpdatedDate(GlobalCommonDateUtils.getDate(handHygieneScoreDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().save(handHygieneScore);
            }

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Response saveHandHygieneScore(HandHygieneScoreDTO handHygieneScoreDTO) {
        return null;
    }

    @Override
    public Response getSaveAsDraftHandHygieneScore(Integer unitId, Integer organizationId, Integer wardId) {
        try{
            List<HandHygieneReturnScoreDTO> getAllSaveAsDraftHandHygieneScore = sessionFactory.getCurrentSession().getNamedQuery("GET_HAND_HYGIENE_SCORE_BY_WARD_ID")
                    .setParameter("unitId",unitId)
                    .setParameter("organizationId",organizationId)
                    .setParameter("wardId",wardId)
                    .setResultTransformer(Transformers.aliasToBean(HandHygieneReturnScoreDTO.class)).list();

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getAllSaveAsDraftHandHygieneScore, null);

        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Response inactiveSavedDraftScoreList(List<HandHygieneReturnScoreDTO> handHygieneReturnScoreDTOS) {
        try{

            for(HandHygieneReturnScoreDTO item: handHygieneReturnScoreDTOS){
                HandHygieneScore handHygieneScore = findById(item.getHandHygieneScoreId());
                handHygieneScore.setStatus('I');
                handHygieneScore.setUpdatedBy(item.getUpdatedBy());
                handHygieneScore.setUpdatedDate(GlobalCommonDateUtils.getDate(item.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().update(handHygieneScore);
            }

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

        }catch (Exception e){
            throw e;
        }
    }
}

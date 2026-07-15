package com.param.adt.admission.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.admission.dto.NabhAssessmentDTO;
import com.param.adt.admission.dto.NutritionalAssessmentScoreDTO;
import com.param.adt.admission.dto.NutritionalAssessmentScoreNames;
import com.param.adt.admission.model.NutritionalAssessmentScore;
import com.param.adt.common.Response;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.unit.dto.AnesthesiaAssessmentDTO;
import com.param.global.unit.dto.InitialAssessmentICUAndERDTO;

import in.param.common.dao.GenericDao;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class NutritionalAssessmentScoreDaoImpl extends GenericDao<NutritionalAssessmentScore, Integer> implements INutritionalAssessmentScoreDao, ICommonConstants {

    @Autowired
    SessionFactory sessionFactory;

    public NutritionalAssessmentScoreDaoImpl() {
        super(NutritionalAssessmentScore.class);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Response saveAsDraftNutritionalAssessmentScore(NutritionalAssessmentScoreDTO nutritionalAssessmentScoreDTO) {
        try{

            for(NutritionalAssessmentScoreNames nutritionalAssessmentScoreObj: nutritionalAssessmentScoreDTO.getNutritionalAssessmentScoreNamesList()){
                NutritionalAssessmentScore nutritionalAssessmentScore = new NutritionalAssessmentScore();

                nutritionalAssessmentScore.setEncounterId(nutritionalAssessmentScoreDTO.getEncounterId());
                nutritionalAssessmentScore.setAdmissionId(nutritionalAssessmentScoreDTO.getAdmissionId());
                nutritionalAssessmentScore.setNutritionalAssessmentName(nutritionalAssessmentScoreObj.getNutritionalAssessmentName());
                nutritionalAssessmentScore.setNutritionalAssessmentId(nutritionalAssessmentScoreObj.getNutritionalAssessmentId());
                nutritionalAssessmentScore.setNutritionalAssessmentNameId(nutritionalAssessmentScoreObj.getNutritionalAssessmentNameId());
                nutritionalAssessmentScore.setNutritionalAssessmentNamePoint(nutritionalAssessmentScoreObj.getNutritionalAssessmentNamePoint());
                nutritionalAssessmentScore.setSaveAsDraft(nutritionalAssessmentScoreDTO.getSaveAsDraft());
                nutritionalAssessmentScore.setConsumptionMarkerFlag(nutritionalAssessmentScoreObj.getConsumptionMarkerFlag());
                nutritionalAssessmentScore.setUnitId(nutritionalAssessmentScoreDTO.getUnitId());
                nutritionalAssessmentScore.setOrganizationId(nutritionalAssessmentScoreDTO.getOrganizationId());
                nutritionalAssessmentScore.setStatus('A');
                nutritionalAssessmentScore.setCreatedBy(nutritionalAssessmentScoreDTO.getCreatedBy());
                nutritionalAssessmentScore.setUpdatedBy(nutritionalAssessmentScoreDTO.getUpdatedBy());
                nutritionalAssessmentScore.setCreatedDate(GlobalCommonDateUtils.getDate(nutritionalAssessmentScoreDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
                nutritionalAssessmentScore.setUpdatedDate(GlobalCommonDateUtils.getDate(nutritionalAssessmentScoreDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
                nutritionalAssessmentScore.setNutritionalAssessmentScoreMasterId(nutritionalAssessmentScoreDTO.getNutritionalAssessmentScoreMasterId());
                sessionFactory.getCurrentSession().save(nutritionalAssessmentScore);
            }
            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        }catch (Exception e){
            throw e;
        }
    }

 

    @Override
    public Response getSaveAsDraftNutritionalAssessmentScore(Integer unitId, Integer organizationId, Integer admissionId) {
        try{
            List<NabhAssessmentDTO> getAllSaveAsDraftNutritionalAssessmentScore = sessionFactory.getCurrentSession().getNamedQuery("GET_NUTRITIONAL_ASSESSMENT_SCORE_BY_ADMISSION_ID")
                    .setParameter("unitId",unitId)
                    .setParameter("organizationId",organizationId)
                    .setParameter("admissionId",admissionId)
                    .setResultTransformer(Transformers.aliasToBean(NabhAssessmentDTO.class)).list();
            
            if(getAllSaveAsDraftNutritionalAssessmentScore != null && getAllSaveAsDraftNutritionalAssessmentScore.size()>0) {
            	
            	String query = "  select  " + 
            			" 	nutritional_assessment_score_master_id as \"nutritionalAssessmentScoreMasterId\", " + 
            			"    patient_weight as \"patientWeight\", " + 
            			"    patient_height as \"patientHeight\", " + 
            			"    patient_bmi as \"patientBmi\", " + 
            			"    patient_knee_height as \"patientKneeHeight\", " + 
            			"    total_nutritional_assessment_score as \"totalNutritionalAssessmentScore\" " + 
            			" from adt.t_nutritional_assessment_score_master  where nutritional_assessment_score_master_id = :NutritionalAssessmentScoreMasterId " ;
    			
    			NabhAssessmentDTO obj = (NabhAssessmentDTO)sessionFactory.getCurrentSession().createSQLQuery(query)
    					.setParameter("NutritionalAssessmentScoreMasterId",getAllSaveAsDraftNutritionalAssessmentScore.get(0).getNutritionalAssessmentScoreMasterId())
    					.setResultTransformer(Transformers.aliasToBean(NabhAssessmentDTO.class)).uniqueResult();
    			return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getAllSaveAsDraftNutritionalAssessmentScore, obj);
            }

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getAllSaveAsDraftNutritionalAssessmentScore, null);

        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Response inactiveSavedDraftScoreList(List<NabhAssessmentDTO> getNabhAssessmentDTO) {
        try {

            for (NabhAssessmentDTO item : getNabhAssessmentDTO) {
                NutritionalAssessmentScore nutritionalDraftScaleScore = findById(item.getNutritionalAssessmentScoreId());
                nutritionalDraftScaleScore.setStatus('I');
                nutritionalDraftScaleScore.setUpdatedBy(item.getUpdatedBy());
                nutritionalDraftScaleScore.setUpdatedDate(GlobalCommonDateUtils.getDate(item.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().update(nutritionalDraftScaleScore);
            }

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
        } catch (Exception e) {
            throw e;
        }
    }

	@Override
	public   com.param.global.common.Response getNutritionalIntailAnethesthiaAssessmentScore(Integer admissionId) {
		try
		{
		 String sql = "select " + 
		 		"	tnasm.nutritional_assessment_score_master_id  as \"nutritionalAssessmentScoreMasterId\"," + 
		 		"	to_char(tnasm.created_date, 'DD-MM-YYYY HH24:MI:SS') as \"createdDate\" " + 
		 		" from adt.t_nutritional_assessment_score_master tnasm  where admission_id  = :admissionId";

		 List<NabhAssessmentDTO> obj = sessionFactory.getCurrentSession().createSQLQuery(sql)
					.setParameter("admissionId",admissionId)
					.setResultTransformer(Transformers.aliasToBean(NabhAssessmentDTO.class)).list();
		 
		 String sql1 = "select " + 
			 		"	initial_assessment_id   as \"initialAssessmentId\"," + 
			 		"	to_char(tnasm.created_date, 'DD-MM-YYYY HH24:MI:SS') as \"createdDate\" " + 
			 		" from t_initial_assessment  tnasm  where admission_id  = :admissionId and  tnasm.status  = 'A' ";

		 InitialAssessmentICUAndERDTO  obj1 = (InitialAssessmentICUAndERDTO) sessionFactory.getCurrentSession().createSQLQuery(sql1)
						.setParameter("admissionId",admissionId)
						.setResultTransformer(Transformers.aliasToBean(InitialAssessmentICUAndERDTO.class)).uniqueResult();
		 
		 String sql2 = "select " + 
			 		"	anesthesia_assessment_id    as \"anesthesiaAssessmentId\"," + 
			 		"	to_char(tnasm.created_date, 'DD-MM-YYYY HH24:MI:SS') as \"createdDate\" " + 
			 		" from t_anesthesia_assessments   tnasm  where admission_id  = :admissionId and  tnasm.status  = 'A' ";

		 List<AnesthesiaAssessmentDTO> obj2 = sessionFactory.getCurrentSession().createSQLQuery(sql2)
						.setParameter("admissionId",admissionId)
						.setResultTransformer(Transformers.aliasToBean(AnesthesiaAssessmentDTO.class)).list();
		 
				return new com.param.global.common.Response<>(SUCCESS, SUCCESS_MESSAGE,SUCCESS_CODE, obj, obj1,obj2);
		 
	}catch (Exception e){
        throw e;
    }
 }

	
}

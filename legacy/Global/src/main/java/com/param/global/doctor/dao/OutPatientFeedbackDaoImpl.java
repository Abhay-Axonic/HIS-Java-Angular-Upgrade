package com.param.global.doctor.dao;

import com.param.adt.admission.model.NutritionalAssessmentScore;
import com.param.entity.model.master.OutPatientFeedback;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.doctor.dto.OutPatientFeedbackDTO;
import com.param.global.doctor.dto.OutPatientFeedbackListDTO;
import in.param.common.dao.GenericDao;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class OutPatientFeedbackDaoImpl extends GenericDao<OutPatientFeedback, Integer> implements IOutPatientFeedbackDao, ICommonConstants {

    @Autowired
    SessionFactory sessionFactory;

    public OutPatientFeedbackDaoImpl() {
        super(OutPatientFeedback.class);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Response saveOutPatientFeedback(OutPatientFeedbackDTO outPatientFeedbackDTO) {
        try{
            for(OutPatientFeedbackListDTO idx: outPatientFeedbackDTO.getFeedbackSelectedList()){
                OutPatientFeedback outPatientFeedback = new OutPatientFeedback();

                outPatientFeedback.setPatientId(outPatientFeedbackDTO.getPatientId());
                outPatientFeedback.setVisitType(outPatientFeedbackDTO.getVisitType());
                outPatientFeedback.setVisitTypeId(outPatientFeedbackDTO.getVisitTypeId());
                outPatientFeedback.setFeedbackKey(idx.getKey());
                outPatientFeedback.setFeedbackValue(idx.getValue());
                outPatientFeedback.setUnitId(outPatientFeedbackDTO.getUnitId());
                outPatientFeedback.setOrganizationId(outPatientFeedbackDTO.getOrganizationId());
                outPatientFeedback.setStatus('A');
                outPatientFeedback.setEncounterId(outPatientFeedbackDTO.getEncounterId());
                outPatientFeedback.setCreatedDate(GlobalCommonDateUtils.getDate(outPatientFeedbackDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
                outPatientFeedback.setUpdatedDate(GlobalCommonDateUtils.getDate(outPatientFeedbackDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
                
                sessionFactory.getCurrentSession().save(outPatientFeedback);
            }

            return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
        }catch(Exception e){
            throw e;
        }
    }

    @Override
    public Response getOutPatientFeedbackList(Integer unitId, Integer organizationId, Integer patientId, String visitType, Integer visitTypeId , Integer encounterId) {
        try{
            List<OutPatientFeedbackDTO> getOutPatientFeedbackList = sessionFactory.getCurrentSession().getNamedQuery("GET_OUT_PATIENT_FEEDBACK_BY_PATIENT_ID")
                    .setParameter("unitId",unitId)
                    .setParameter("organizationId",organizationId)
                    .setParameter("patientId",patientId)
                    .setParameter("visitType",visitType)
                    .setParameter("visitTypeId",visitTypeId)
                    .setResultTransformer(Transformers.aliasToBean(OutPatientFeedbackDTO.class)).list();

            return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getOutPatientFeedbackList, encounterId);

        }catch (Exception e){
            throw e;
        }
    }
}

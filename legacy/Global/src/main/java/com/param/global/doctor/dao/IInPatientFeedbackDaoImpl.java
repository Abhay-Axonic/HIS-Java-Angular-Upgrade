package com.param.global.doctor.dao;

import com.param.entity.model.master.InPatientFeedback;
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
public class IInPatientFeedbackDaoImpl  extends GenericDao<InPatientFeedback, Integer> implements IInPatientFeedbackDao, ICommonConstants {

    @Autowired
    SessionFactory sessionFactory;

    public IInPatientFeedbackDaoImpl() {
        super(InPatientFeedback.class);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Response saveInPatientFeedback(OutPatientFeedbackDTO outPatientFeedbackDTO) {
        try{
            for(OutPatientFeedbackListDTO idx: outPatientFeedbackDTO.getFeedbackSelectedList()){
                InPatientFeedback inPatientFeedback = new InPatientFeedback();

                inPatientFeedback.setPatientId(outPatientFeedbackDTO.getPatientId());
                inPatientFeedback.setVisitType(outPatientFeedbackDTO.getVisitType());
                inPatientFeedback.setVisitTypeId(outPatientFeedbackDTO.getVisitTypeId());
                inPatientFeedback.setFeedbackKey(idx.getKey());
                inPatientFeedback.setFeedbackValue(idx.getValue());
                inPatientFeedback.setUnitId(outPatientFeedbackDTO.getUnitId());
                inPatientFeedback.setOrganizationId(outPatientFeedbackDTO.getOrganizationId());
                inPatientFeedback.setStatus('A');
                inPatientFeedback.setAdmissionId(outPatientFeedbackDTO.getAdmissionId());
                inPatientFeedback.setCreatedDate(GlobalCommonDateUtils.getDate(outPatientFeedbackDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
                inPatientFeedback.setUpdatedDate(GlobalCommonDateUtils.getDate(outPatientFeedbackDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().save(inPatientFeedback);
            }

            return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
        }catch(Exception e){
            throw e;
        }
    }

    @Override
    public Response getInPatientFeedbackList(Integer unitId, Integer organizationId, Integer patientId, String visitType, Integer visitTypeId, Integer admissionId) {
        try{
            List<OutPatientFeedbackDTO> getOutPatientFeedbackList = sessionFactory.getCurrentSession().getNamedQuery("GET_IN_PATIENT_FEEDBACK_BY_PATIENT_ID")
                    .setInteger("unitId",unitId)
                    .setInteger("organizationId",organizationId)
                    .setInteger("patientId",patientId)
                    .setString("visitType",visitType)
                    .setInteger("visitTypeId",visitTypeId)
                    .setResultTransformer(Transformers.aliasToBean(OutPatientFeedbackDTO.class)).list();

            return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getOutPatientFeedbackList, admissionId);

        }catch (Exception e){
            throw e;
        }
    }
}

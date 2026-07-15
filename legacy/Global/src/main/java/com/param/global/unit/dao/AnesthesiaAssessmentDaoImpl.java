package com.param.global.unit.dao;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.AnesthesiaAssessment;
import com.param.global.model.InitialAssessmentICUAndER;
import com.param.global.model.InitialAssessmentICUAndERMapper;
import com.param.global.unit.dto.AnesthesiaAssessmentDTO;
import com.param.global.unit.dto.InitialAssessmentICUAndERReturnDTO;
import in.param.common.dao.GenericDao;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class AnesthesiaAssessmentDaoImpl extends GenericDao<AnesthesiaAssessment, Integer> implements IAnesthesiaAssessmentDao, ICommonConstants {

    public AnesthesiaAssessmentDaoImpl() {
        super(AnesthesiaAssessment.class);
        // TODO Auto-generated constructor stub
    }

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Response saveAnesthesiaAssessment(AnesthesiaAssessmentDTO anesthesiaAssessmentDTO) {

        try {
            AnesthesiaAssessment anesthesiaAssessment = new AnesthesiaAssessment();

            anesthesiaAssessment.setPatientId(anesthesiaAssessmentDTO.getPatientId());
            anesthesiaAssessment.setIsPreviousSurgery(anesthesiaAssessmentDTO.getIsPreviousSurgery());
            anesthesiaAssessment.setSelectedAssessment(anesthesiaAssessmentDTO.getSelectedAssessment());
            anesthesiaAssessment.setAnesthesiaPlan(anesthesiaAssessmentDTO.getAnesthesiaPlan());
            anesthesiaAssessment.setSaveAsDraft(anesthesiaAssessmentDTO.getSaveAsDraft());
            anesthesiaAssessment.setUnitId(anesthesiaAssessmentDTO.getUnitId());
            anesthesiaAssessment.setOrganizationId(anesthesiaAssessmentDTO.getOrganizationId());
            anesthesiaAssessment.setStatus('A');
            anesthesiaAssessment.setUpdatedBy(anesthesiaAssessmentDTO.getUpdatedBy());
            anesthesiaAssessment.setCreatedBy(anesthesiaAssessmentDTO.getCreatedBy());
            anesthesiaAssessment.setCreatedBy(anesthesiaAssessmentDTO.getCreatedBy());
            anesthesiaAssessment.setCreatedDate(GlobalCommonDateUtils.getDate(anesthesiaAssessmentDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
            anesthesiaAssessment.setUpdatedDate(GlobalCommonDateUtils.getDate(anesthesiaAssessmentDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
            anesthesiaAssessment.setAdmissionId(anesthesiaAssessmentDTO.getAdmissionId() != null ? anesthesiaAssessmentDTO.getAdmissionId() : null);
            anesthesiaAssessment.setEncounterId(anesthesiaAssessmentDTO.getEncounterId() != null ? anesthesiaAssessmentDTO.getEncounterId() : null);
            anesthesiaAssessment.setPreviousSurgery(anesthesiaAssessmentDTO.getPreviousSurgeryDesc() != null ? anesthesiaAssessmentDTO.getPreviousSurgeryDesc() : null);
            sessionFactory.getCurrentSession().save(anesthesiaAssessment);

            return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getAnesthesiaAssessment(Integer unitId, Integer organizationId, Integer patientId) {
        try{

            AnesthesiaAssessmentDTO anesthesiaAssessment = (AnesthesiaAssessmentDTO) sessionFactory.getCurrentSession()
                    .getNamedQuery("GET_ANESTHESIA_ASSESSMENT_LIST_BY_PATIENT_ID")
                    .setParameter("unitId", unitId)
                    .setParameter("organizationId", organizationId)
                    .setParameter("patientId", patientId)
                    .setResultTransformer(Transformers.aliasToBean(AnesthesiaAssessmentDTO.class))
                    .uniqueResult();

            return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, anesthesiaAssessment);

        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Response inactiveAnesthesiaAssessment(AnesthesiaAssessmentDTO anesthesiaAssessmentDTO) {
        try {
            AnesthesiaAssessment anesthesiaAssessment = findById(anesthesiaAssessmentDTO.getAnesthesiaAssessmentId());
            anesthesiaAssessment.setStatus('I');
            anesthesiaAssessment.setUpdatedBy(anesthesiaAssessmentDTO.getUpdatedBy());
            anesthesiaAssessment.setUpdatedDate(GlobalCommonDateUtils.getDate(anesthesiaAssessmentDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

            sessionFactory.getCurrentSession().update(anesthesiaAssessment);

            return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
        } catch (Exception e) {
            throw e;
        }
    }
}

package com.param.global.unit.dao;

import com.param.adt.admission.model.NutritionalAssessmentScore;
import com.param.adt.admission.model.PatientFallReport;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.InitialAssessmentICUAndER;
import com.param.global.unit.dto.InitialAssessmentICUAndERDTO;
import com.param.global.unit.dto.InitialAssessmentICUAndERReturnDTO;
import com.param.global.unit.service.IInitialAssessmentICUAndERService;
import com.param.utility.TimeZoneComponent;
import in.param.common.dao.GenericDao;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class InitialAssessmentICUAndERDaoImpl extends GenericDao<InitialAssessmentICUAndER, Integer> implements IInitialAssessmentICUAndERDao, ICommonConstants {

    @Autowired
    SessionFactory sessionFactory;

    public InitialAssessmentICUAndERDaoImpl() {
        super(InitialAssessmentICUAndER.class);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Response saveInitialAssessmentICUAndER(InitialAssessmentICUAndERDTO initialAssessmentICUAndERDTO) {
        try{

            InitialAssessmentICUAndER initialAssessmentICUAndER = new InitialAssessmentICUAndER();

            initialAssessmentICUAndER.setEncounterId(initialAssessmentICUAndERDTO.getEncounterId());
            initialAssessmentICUAndER.setAdmissionId(initialAssessmentICUAndERDTO.getAdmissionId());
            initialAssessmentICUAndER.setUhid(initialAssessmentICUAndERDTO.getUhid());
            initialAssessmentICUAndER.setInitialAssessmentStatusId(initialAssessmentICUAndERDTO.getInitialAssessmentStatusId());
            initialAssessmentICUAndER.setInfusions(initialAssessmentICUAndERDTO.getInfusions());
            initialAssessmentICUAndER.setSaveAsDraft(initialAssessmentICUAndERDTO.getSaveAsDraft());
            initialAssessmentICUAndER.setUnitId(initialAssessmentICUAndERDTO.getUnitId());
            initialAssessmentICUAndER.setOrganizationId(initialAssessmentICUAndERDTO.getOrganizationId());
            initialAssessmentICUAndER.setStatus('A');
            initialAssessmentICUAndER.setCreatedBy(initialAssessmentICUAndERDTO.getCreatedBy());
            initialAssessmentICUAndER.setUpdatedBy(initialAssessmentICUAndERDTO.getUpdatedBy());
            initialAssessmentICUAndER.setCreatedDate(GlobalCommonDateUtils.getDate(initialAssessmentICUAndERDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
            initialAssessmentICUAndER.setUpdatedDate(GlobalCommonDateUtils.getDate(initialAssessmentICUAndERDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
            initialAssessmentICUAndER.setStartDateTime(TimeZoneComponent.getDateByzone(initialAssessmentICUAndERDTO.getStartDateTime(), initialAssessmentICUAndERDTO.getUnitId()));
            initialAssessmentICUAndER = save(initialAssessmentICUAndER);

            return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, initialAssessmentICUAndER);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Response getInitialAssessmentICUAndERList(Integer unitId, Integer organizationId, Integer admissionId) {
        try{
            Long count = (Long) sessionFactory.getCurrentSession()
                    .createQuery("SELECT COUNT(*) FROM InitialAssessmentICUAndER WHERE admissionId = :admissionId and saveAsDraft != 'Y' and status = 'A'")
                    .setParameter("admissionId", admissionId)
                    .uniqueResult();

                if (count != null && count > 0) {
                    return new Response<>(ERROR, ERROR_CODE, "Assessment with this patient already exists.", null, null);
                }
                
            List<InitialAssessmentICUAndERReturnDTO> getInitialAssessmentICUAndERList = sessionFactory.getCurrentSession().getNamedQuery("GET_INITIAL_ASSESSMENT_BY_ADMISSION_ID")
                    .setParameter("unitId",unitId)
                    .setParameter("organizationId",organizationId)
                    .setParameter("admissionId",admissionId)
                    .setResultTransformer(Transformers.aliasToBean(InitialAssessmentICUAndERReturnDTO.class)).list();

            return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getInitialAssessmentICUAndERList, getInitialAssessmentICUAndERList !=null && getInitialAssessmentICUAndERList.size()>0 ?getInitialAssessmentICUAndERList.getFirst().getStartDateTime():null );

        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Response getInitialAssessmentICUAndERActiveList(Integer unitId, Integer organizationId, Integer admissionId) {
        try{
            List<InitialAssessmentICUAndERReturnDTO> getInitialAssessmentICUAndERList = sessionFactory.getCurrentSession().getNamedQuery("GET_INITIAL_ASSESSMENT_LIST_BY_ADMISSION_ID")
                    .setParameter("unitId",unitId)
                    .setParameter("organizationId",organizationId)
                    .setParameter("admissionId",admissionId)
                    .setResultTransformer(Transformers.aliasToBean(InitialAssessmentICUAndERReturnDTO.class)).list();

            return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getInitialAssessmentICUAndERList, null);

        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Response inactiveInitialAssessmentICUAndERActiveList(List<InitialAssessmentICUAndERReturnDTO> patientFallReturnReportDtos) {
        try {

            for (InitialAssessmentICUAndERReturnDTO item : patientFallReturnReportDtos) {
                InitialAssessmentICUAndER initialAssessmentICUAndER = findById(item.getInitialAssessmentId());
                initialAssessmentICUAndER.setStatus('I');
                initialAssessmentICUAndER.setUpdatedBy(item.getUpdatedBy());
                initialAssessmentICUAndER.setUpdatedDate(GlobalCommonDateUtils.getDate(item.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().update(initialAssessmentICUAndER);
            }

            return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
        } catch (Exception e) {
            throw e;
        }
    }
}

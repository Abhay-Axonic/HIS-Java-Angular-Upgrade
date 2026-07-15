package com.param.global.unit.dao;

import com.param.adt.admission.model.NutritionalAssessmentScore;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.InitialAssessmentICUAndER;
import com.param.global.model.InitialAssessmentICUAndERMapper;
import com.param.global.unit.dto.InitialAssessmentICUAndERDTO;
import com.param.global.unit.dto.InitialAssessmentICUAndERMapperDTO;
import com.param.global.unit.dto.InitialAssessmentICUAndERReturnDTO;
import com.param.utility.TimeZoneComponent;
import in.param.common.dao.GenericDao;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class InitialAssessmentICUAndERMapperDaoImpl  extends GenericDao<InitialAssessmentICUAndERMapper, Integer> implements IInitialAssessmentICUAndERMapperDao, ICommonConstants {

    @Autowired
    SessionFactory sessionFactory;

    public InitialAssessmentICUAndERMapperDaoImpl() {
        super(InitialAssessmentICUAndERMapper.class);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Response saveInitialAssessmentICUAndERMapper(InitialAssessmentICUAndERDTO initialAssessmentICUAndERDTO) {

        try{
            for(InitialAssessmentICUAndERMapperDTO idx: initialAssessmentICUAndERDTO.getSelectedKeyValueList()){
                InitialAssessmentICUAndERMapper initialAssessmentICUAndERMapper = new InitialAssessmentICUAndERMapper();

                initialAssessmentICUAndERMapper.setAdmissionId(initialAssessmentICUAndERDTO.getAdmissionId());
                initialAssessmentICUAndERMapper.setAssessmentId(idx.getAssessmentId());
                initialAssessmentICUAndERMapper.setAssessment(idx.getAssessment());
                initialAssessmentICUAndERMapper.setIndexId(idx.getIndexId());
                initialAssessmentICUAndERMapper.setAssessmentKey(idx.getAssessmentKey());
                initialAssessmentICUAndERMapper.setAssessmentValueId(idx.getAssessmentValueId());
                initialAssessmentICUAndERMapper.setAssessmentValue(idx.getAssessmentValue());
                initialAssessmentICUAndERMapper.setSaveAsDraft(initialAssessmentICUAndERDTO.getSaveAsDraft());
                initialAssessmentICUAndERMapper.setStatus('A');
                initialAssessmentICUAndERMapper.setUnitId(initialAssessmentICUAndERDTO.getUnitId());
                initialAssessmentICUAndERMapper.setOrganizationId(initialAssessmentICUAndERDTO.getOrganizationId());
                initialAssessmentICUAndERMapper.setCreatedBy(initialAssessmentICUAndERDTO.getCreatedBy());
                initialAssessmentICUAndERMapper.setUpdatedBy(initialAssessmentICUAndERDTO.getUpdatedBy());
                initialAssessmentICUAndERMapper.setCreatedDate(GlobalCommonDateUtils.getDate(initialAssessmentICUAndERDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
                initialAssessmentICUAndERMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(initialAssessmentICUAndERDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
                initialAssessmentICUAndERMapper.setInitialAssessmentId(initialAssessmentICUAndERDTO.getInitialAssessmentId());
                initialAssessmentICUAndERMapper.setStartDateTime(TimeZoneComponent.getDateByzone(initialAssessmentICUAndERDTO.getStartDateTime(), initialAssessmentICUAndERDTO.getUnitId()));

                sessionFactory.getCurrentSession().save(initialAssessmentICUAndERMapper);

            }
            return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

        }catch (Exception e){
            throw e;
        }

    }

    @Override
    public Response getInitialAssessmentICUAndERList(Integer unitId, Integer organizationId, Integer admissionId) {
        return null;
    }

    @Override
    public Response getInitialAssessmentICUAndERMapperActiveList(Integer unitId, Integer organizationId, Integer admissionId) {
        try{
            List<InitialAssessmentICUAndERReturnDTO> getInitialAssessmentICUAndERMapperList = sessionFactory.getCurrentSession().getNamedQuery("GET_INITIAL_ASSESSMENT_MAPPER_BY_ADMISSION_ID")
                    .setParameter("unitId",unitId)
                    .setParameter("organizationId",organizationId)
                    .setParameter("admissionId",admissionId)
                    .setResultTransformer(Transformers.aliasToBean(InitialAssessmentICUAndERReturnDTO.class)).list();

            return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getInitialAssessmentICUAndERMapperList, null);

        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Response inactiveInitialAssessmentICUAndERMapperActiveList(List<InitialAssessmentICUAndERReturnDTO> patientFallReturnReportDtos) {
        try {

            for (InitialAssessmentICUAndERReturnDTO item : patientFallReturnReportDtos) {
                InitialAssessmentICUAndERMapper initialAssessmentICUAndERMapper = findById(item.getInitialAssessmentMapperId());
                initialAssessmentICUAndERMapper.setStatus('I');
                initialAssessmentICUAndERMapper.setUpdatedBy(item.getUpdatedBy());
                initialAssessmentICUAndERMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(item.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().update(initialAssessmentICUAndERMapper);
            }

            return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
        } catch (Exception e) {
            throw e;
        }
    }
}

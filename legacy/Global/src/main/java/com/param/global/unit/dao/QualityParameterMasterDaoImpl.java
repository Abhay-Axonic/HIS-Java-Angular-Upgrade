package com.param.global.unit.dao;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AdmissionNoteDto;
import com.param.global.model.IncidentReportMaster;
import com.param.global.model.QualityParameterMaster;
import com.param.global.unit.dto.QualityParameterMasterDTO;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class QualityParameterMasterDaoImpl extends GenericDao<QualityParameterMaster, Integer> implements IQualityParameterMasterDao, ICommonConstants {

    public QualityParameterMasterDaoImpl() {
        super(QualityParameterMaster.class);
    }

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Response saveQualityParameterMaster(QualityParameterMasterDTO qualityParameterMasterDTO) throws ApplicationException {
        try{
            QualityParameterMaster qualityParameterMaster = new QualityParameterMaster();

            qualityParameterMaster.setUnitId(qualityParameterMasterDTO.getUnitId());
            qualityParameterMaster.setOrganizationId(qualityParameterMasterDTO.getOrganizationId());
            qualityParameterMaster.setModuleId(qualityParameterMasterDTO.getModuleId());
            qualityParameterMaster.setModuleName(qualityParameterMasterDTO.getModuleName());
            qualityParameterMaster.setActivityId(qualityParameterMasterDTO.getActivityId());
            qualityParameterMaster.setActivityName(qualityParameterMasterDTO.getActivityName());
            qualityParameterMaster.setParameterId(qualityParameterMasterDTO.getParameterId());
            qualityParameterMaster.setStandard(qualityParameterMasterDTO.getStandard());
            qualityParameterMaster.setUomId(qualityParameterMasterDTO.getUomId());
            qualityParameterMaster.setUomDesc(qualityParameterMasterDTO.getUomDesc());
            qualityParameterMaster.setThresholdPercentage(qualityParameterMasterDTO.getThresholdPercentage());
            qualityParameterMaster.setTargetPercentage(qualityParameterMasterDTO.getTargetPercentage());
            qualityParameterMaster.setStatus('A');
            qualityParameterMaster.setCreatedBy(qualityParameterMasterDTO.getCreatedBy());
            qualityParameterMaster.setUpdatedBy(qualityParameterMasterDTO.getUpdatedBy());
            qualityParameterMaster.setCreatedDate(GlobalCommonDateUtils.getDate(qualityParameterMasterDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
            qualityParameterMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(qualityParameterMasterDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

            sessionFactory.getCurrentSession().save(qualityParameterMaster);
            return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, qualityParameterMaster);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Response updateQualityParameterMaster(QualityParameterMasterDTO qualityParameterMasterDTO) throws ApplicationException {
        try{
            QualityParameterMaster qualityParameterMaster = findById(qualityParameterMasterDTO.getQualityParameterMasterId());

            qualityParameterMaster.setModuleId(qualityParameterMasterDTO.getModuleId());
            qualityParameterMaster.setModuleName(qualityParameterMasterDTO.getModuleName());
            qualityParameterMaster.setActivityId(qualityParameterMasterDTO.getActivityId());
            qualityParameterMaster.setActivityName(qualityParameterMasterDTO.getActivityName());
            qualityParameterMaster.setParameterId(qualityParameterMasterDTO.getParameterId());
            qualityParameterMaster.setStandard(qualityParameterMasterDTO.getStandard());
            qualityParameterMaster.setUomId(qualityParameterMasterDTO.getUomId());
            qualityParameterMaster.setUomDesc(qualityParameterMasterDTO.getUomDesc());
            qualityParameterMaster.setThresholdPercentage(qualityParameterMasterDTO.getThresholdPercentage());
            qualityParameterMaster.setTargetPercentage(qualityParameterMasterDTO.getTargetPercentage());
            qualityParameterMaster.setStatus(qualityParameterMasterDTO.getStatus());
            qualityParameterMaster.setUpdatedBy(qualityParameterMasterDTO.getUpdatedBy());
            qualityParameterMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(qualityParameterMasterDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

            sessionFactory.getCurrentSession().update(qualityParameterMaster);

            return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, qualityParameterMaster);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Response getQualityParameterMasterById(QualityParameterMasterDTO qualityParameterMasterDTO) throws ApplicationException {
        try{
            QualityParameterMasterDTO dto = (QualityParameterMasterDTO)sessionFactory.getCurrentSession().getNamedQuery("GET_QUALITY_PARAMETER_MASTER_BY_ID")
                    .setParameter("unitId",qualityParameterMasterDTO.getUnitId())
                    .setParameter("organizationId", qualityParameterMasterDTO.getOrganizationId())
                    .setParameter("activityId", qualityParameterMasterDTO.getActivityId())
                    .setResultTransformer(Transformers.aliasToBean(QualityParameterMasterDTO.class)).uniqueResult();
//            return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, incidentReportMasterDto);
            return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);

        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Response getQualityParameterMasterList(Integer unitId, Integer organizationId) throws ApplicationException {
        try{
            List<QualityParameterMasterDTO> getList = sessionFactory.getCurrentSession().getNamedQuery("GET_QUALITY_PARAMETER_MASTER_LIST")
                    .setParameter("unitId",unitId)
                    .setParameter("organizationId",organizationId)
                    .setResultTransformer(Transformers.aliasToBean(QualityParameterMasterDTO.class)).list();

            return new Response(SUCCESS, SUCCESS_CODE, null, getList, null);

        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Response updateStatus(QualityParameterMasterDTO qualityParameterMasterDTO) throws ApplicationException {
        try{
            QualityParameterMaster qualityParameterMaster = findById(qualityParameterMasterDTO.getQualityParameterMasterId());

            qualityParameterMaster.setStatus((qualityParameterMasterDTO.getStatus()=='A')?'A':'I');
            qualityParameterMaster.setUpdatedBy(qualityParameterMasterDTO.getUpdatedBy());
            qualityParameterMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(qualityParameterMasterDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

            sessionFactory.getCurrentSession().update(qualityParameterMaster);

            return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, qualityParameterMaster);
        }catch (Exception e){
            throw e;
        }
    }
}

package com.param.adt.admission.dao;

import com.param.adt.admission.dto.HandHygieneReturnScoreDTO;
import com.param.adt.admission.dto.LaboratoryInspectionListDTO;
import com.param.adt.admission.dto.LaboratoryInspectionScoreDTO;
import com.param.adt.admission.dto.LaboratoryInspectionScoreReturnDTO;
import com.param.adt.admission.model.HandHygieneScore;
import com.param.adt.admission.model.LaboratoryInspectionScore;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.global.common.GlobalCommonDateUtils;
import in.param.common.dao.GenericDao;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.param.global.common.ICommonConstants.SUCCESS_MESSAGE;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class LaboratoryInspectionScoreDaoImpl extends GenericDao<LaboratoryInspectionScore, Integer> implements ILaboratoryInspectionScoreDao, ICommonConstants {

    public LaboratoryInspectionScoreDaoImpl() {
        super(LaboratoryInspectionScore.class);
        // TODO Auto-generated constructor stub
    }

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Response saveAsDraftLaboratoryInspectionScore(LaboratoryInspectionScoreDTO laboratoryInspectionScoreDTO) {
        try {
            String pattern = "dd-M-yyyy";
            String curDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));

            for (LaboratoryInspectionListDTO laboratoryInspectionListDTO : laboratoryInspectionScoreDTO.getLabInspectionSelectedList()) {
                LaboratoryInspectionScore laboratoryInspectionScore = new LaboratoryInspectionScore();

                laboratoryInspectionScore.setUnitId(laboratoryInspectionScoreDTO.getUnitId());
                laboratoryInspectionScore.setOrganizationId(laboratoryInspectionScoreDTO.getOrganizationId());
                laboratoryInspectionScore.setDepartmentId(laboratoryInspectionScoreDTO.getDepartmentId());
                laboratoryInspectionScore.setSaveAsDraft(laboratoryInspectionScoreDTO.getSaveAsDraft());
                laboratoryInspectionScore.setLabInspectionId(laboratoryInspectionListDTO.getLabInspectionId());
                laboratoryInspectionScore.setLabInspectionName(laboratoryInspectionListDTO.getLabInspectionName());
                laboratoryInspectionScore.setLabInspectionNameId(laboratoryInspectionListDTO.getLabInspectionNameId());
                laboratoryInspectionScore.setLabInspectionNameFlag(laboratoryInspectionListDTO.getLabInspectionNameFlag());

                if (laboratoryInspectionScoreDTO.getSaveAsDraft() == 'Y') {
                    laboratoryInspectionScore.setLabInspectionFromDate(GlobalCommonDateUtils.getDate(laboratoryInspectionScoreDTO.getLabInspectionDate(), "dd-M-yyyy"));
                } else {
                    laboratoryInspectionScore.setLabInspectionFromDate(GlobalCommonDateUtils.getDate(laboratoryInspectionScoreDTO.getLabInspectionDate(), "dd-M-yyyy"));
                    laboratoryInspectionScore.setLabInspectionToDate(GlobalCommonDateUtils.getDate(curDate, "dd-M-yyyy"));
                }

                laboratoryInspectionScore.setStatus('A');
                laboratoryInspectionScore.setCreatedBy(laboratoryInspectionScoreDTO.getCreatedBy());
                laboratoryInspectionScore.setUpdatedBy(laboratoryInspectionScoreDTO.getUpdatedBy());
                laboratoryInspectionScore.setCreatedDate(GlobalCommonDateUtils.getDate(laboratoryInspectionScoreDTO.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
                laboratoryInspectionScore.setUpdatedDate(GlobalCommonDateUtils.getDate(laboratoryInspectionScoreDTO.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().save(laboratoryInspectionScore);
            }

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response saveLaboratoryInspectionScore(LaboratoryInspectionScore laboratoryInspectionScore) {
        return null;
    }

    @Override
    public Response getSaveAsDraftLaboratoryInspectionScore(Integer unitId, Integer organizationId, Integer departmentId) {
        try {
            List<LaboratoryInspectionScoreReturnDTO> getAllSaveAsDraftScores = sessionFactory.getCurrentSession().getNamedQuery("GET_LAB_INSPECTION_SCORE_BY_DEPARTMENT_ID")
                    .setParameter("unitId", unitId)
                    .setParameter("organizationId", organizationId)
                    .setParameter("departmentId", departmentId)
                    .setResultTransformer(Transformers.aliasToBean(LaboratoryInspectionScoreReturnDTO.class)).list();

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getAllSaveAsDraftScores, null);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response inactiveSavedDraftScoreList(List<LaboratoryInspectionScoreReturnDTO> laboratoryInspectionScores) {
        try {
            for (LaboratoryInspectionScoreReturnDTO item : laboratoryInspectionScores) {
                LaboratoryInspectionScore laboratoryInspectionScore = findById(item.getLaboratoryInspectionId());
                laboratoryInspectionScore.setStatus('I');
                laboratoryInspectionScore.setUpdatedBy(item.getUpdatedBy());
                laboratoryInspectionScore.setUpdatedDate(GlobalCommonDateUtils.getDate(item.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().update(laboratoryInspectionScore);
            }
            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

        } catch (Exception e) {
            throw e;
        }
    }

}

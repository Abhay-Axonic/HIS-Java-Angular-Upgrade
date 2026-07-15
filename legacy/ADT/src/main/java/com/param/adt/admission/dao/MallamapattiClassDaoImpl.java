package com.param.adt.admission.dao;

import com.param.adt.admission.dto.MallamapattiClassDto;
import com.param.adt.admission.model.MallamapattiClass;
import com.param.adt.common.Response;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import scala.collection.mutable.StringBuilder;

import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class MallamapattiClassDaoImpl extends GenericDao<MallamapattiClass, Integer> implements IMallamapattiClassDao, ICommonConstants {

    @Autowired
    SessionFactory sessionFactory;

    public MallamapattiClassDaoImpl() {
        super(MallamapattiClass.class);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void saveMallampattiClass(MallamapattiClassDto mallamapattiClassDto) throws ApplicationException {
        try {
            MallamapattiClass mallamapattiClass = new MallamapattiClass();

            mallamapattiClass.setEncounterId(mallamapattiClassDto.getEncounterId() != null ? mallamapattiClassDto.getEncounterId():0);
            mallamapattiClass.setAdmissionId(mallamapattiClassDto.getAdmissionId()!=null ? mallamapattiClassDto.getAdmissionId():0);
            mallamapattiClass.setTotalMallampattiScore(mallamapattiClassDto.getTotalMallampattiScore());
            mallamapattiClass.setMallampattiClassId(mallamapattiClassDto.getMallampattiClassId());
            mallamapattiClass.setUnitId(mallamapattiClassDto.getUnitId());
            mallamapattiClass.setOrganizationId(mallamapattiClassDto.getOrganizationId());
            mallamapattiClass.setStatus('A');
            mallamapattiClass.setSaveAsDraft(mallamapattiClassDto.getSaveAsDraft());
            mallamapattiClass.setFinalSave(mallamapattiClassDto.getFinalSave());
            mallamapattiClass.setCreatedBy(mallamapattiClassDto.getCreatedBy());
            mallamapattiClass.setUpdatedBy(mallamapattiClassDto.getUpdatedBy());
            mallamapattiClass.setCreatedDate(GlobalCommonDateUtils.getDate(mallamapattiClassDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
            mallamapattiClass.setUpdatedDate(GlobalCommonDateUtils.getDate(mallamapattiClassDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

            sessionFactory.getCurrentSession().save(mallamapattiClass);

        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getDraftedMallampattiClass(Integer unitId, Integer organizationId, Integer admissionId, Integer encounterId) throws ApplicationException {
        try {
            StringBuilder query=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_DRAFTED_MALLAMPATTI_SCORES_BY_ID").getQueryString());
            if(admissionId != null && admissionId >0){
                query.append(" AND tms.admission_id=:admissionId ");
            }
            if(encounterId != null && encounterId >0){
                query.append(" AND tms.encounter_id=:encounterId ");
            }
            query.append(" ORDER BY tms.mallampatti_score_id desc limit 1");
            Query sql = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
                    .setParameter("unitId", unitId)
                    .setParameter("organizationId", organizationId);
            if(admissionId != null && admissionId >0){
                sql.setParameter("admissionId", admissionId);
            }
            if(encounterId != null && encounterId >0){
                sql.setParameter("encounterId", encounterId);
            }
            List<MallamapattiClassDto> getDraftMallamapattiClass= sql.setResultTransformer(Transformers.aliasToBean(MallamapattiClassDto.class)).list();

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, getDraftMallamapattiClass, null);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response inactiveDraftedMallampattiClass(List<MallamapattiClassDto> mallamapattiClassDto) throws ApplicationException {
        try {

            for (MallamapattiClassDto item : mallamapattiClassDto) {
                MallamapattiClass mallamapattiClass = findById(item.getMallampattiScoreId());
                mallamapattiClass.setStatus('I');
                mallamapattiClass.setUpdatedBy(item.getUpdatedBy());
                mallamapattiClass.setCreatedBy(item.getUpdatedBy());
                mallamapattiClass.setUpdatedDate(GlobalCommonDateUtils.getDate(item.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
                mallamapattiClass.setCreatedDate(GlobalCommonDateUtils.getDate(item.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));

                sessionFactory.getCurrentSession().update(mallamapattiClass);
            }

            return new com.param.adt.common.Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
        } catch (Exception e) {
            throw e;
        }
    }
}

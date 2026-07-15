package com.param.opd.coversheet.dao;

import com.param.entity.model.master.DifferentTypesScreenImage;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.common.CommonDateUtils;
import com.param.opd.coversheet.dto.PatientBirthHistoryDTO;
import com.param.opd.coversheet.model.PatientBirthHistory;
import com.param.opd.coversheet.model.PatientTravelHistoryDetails;
import com.param.utility.TimeZoneComponent;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
@SuppressWarnings({ "unused", "unchecked" })
public class PatientBirthHistoryDaoImpl extends GenericDao<PatientBirthHistory, Integer> implements IPatientBirthHistoryDao, ICommonConstants {


    public PatientBirthHistoryDaoImpl() {
        super(PatientBirthHistory.class);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Response savePatientBirthHistory(PatientBirthHistoryDTO patientBirthHistoryDTO) throws ApplicationException {
        try{
            Response response = getPatientBirthHistory(patientBirthHistoryDTO.getPatientId());
            PatientBirthHistory findPatientBirthHistory = (PatientBirthHistory) response.getObject();

            if(response != null && response.getObject() != null && response.getObject() != ""){

                findPatientBirthHistory.setDescription(patientBirthHistoryDTO.getDescription());
                findPatientBirthHistory.setUpdatedBy(patientBirthHistoryDTO.getUpdatedBy());
                findPatientBirthHistory.setUpdatedDate(TimeZoneComponent.getDateByzone(patientBirthHistoryDTO.getUnitId()).getTime());

                update(findPatientBirthHistory);

                return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, patientBirthHistoryDTO.getDescription(), null);

            }else {

                PatientBirthHistory patientBirthHistory = new PatientBirthHistory();
                patientBirthHistory.setDescription(patientBirthHistoryDTO.getDescription());
                patientBirthHistory.setPatientId(patientBirthHistoryDTO.getPatientId());
                patientBirthHistory.setEncounterId(patientBirthHistoryDTO.getEncounterId());
                patientBirthHistory.setAdmissionId(patientBirthHistoryDTO.getAdmissionId());
                patientBirthHistory.setUnitId(patientBirthHistoryDTO.getUnitId());
                patientBirthHistory.setOrganizationId(patientBirthHistoryDTO.getOrganizationId());
                patientBirthHistory.setCreatedBy(patientBirthHistoryDTO.getCreatedBy());
                patientBirthHistory.setUpdatedBy(patientBirthHistoryDTO.getUpdatedBy());
                patientBirthHistory.setCreatedDate(TimeZoneComponent.getDateByzone(patientBirthHistoryDTO.getUnitId()).getTime());
                patientBirthHistory.setUpdatedDate(TimeZoneComponent.getDateByzone(patientBirthHistoryDTO.getUnitId()).getTime());
                patientBirthHistory.setStatus('A');

                save(patientBirthHistory);

                return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, patientBirthHistoryDTO.getDescription(), null);
            }

        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Response getPatientBirthHistory(Integer patientId) throws ApplicationException {
        try{
            Session session=sessionFactory.getCurrentSession();

            String hql = "FROM PatientBirthHistory pbh WHERE pbh.patientId = :patientId";
            Query query = session.createQuery(hql);
            query.setParameter("patientId", patientId);

            PatientBirthHistory result = (PatientBirthHistory) query.uniqueResult();

            return new Response(SUCCESS, SUCCESS_CODE, null, null, result, null);
        }catch (Exception e){
            throw e;
        }
    }
}

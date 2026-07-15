package com.param.adt.admission.dao;
import com.param.adt.admission.model.AdmittedPatientOrdersWorkbench;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.global.dto.AdmissionDto;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public class AdmittedPatientOrdersWorkbenchDaoImpl extends GenericDao<AdmittedPatientOrdersWorkbench, Integer> implements IAdmittedPatientOrdersWorkbench, ICommonConstants {

    public AdmittedPatientOrdersWorkbenchDaoImpl() {
        super(AdmittedPatientOrdersWorkbench.class);
    }

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public AdmittedPatientOrdersWorkbench savePatientOrdersWorkbench(AdmittedPatientOrdersWorkbench admittedPatientOrdersWorkbench) throws ApplicationException {
        try {
            Serializable id = sessionFactory.getCurrentSession().save (admittedPatientOrdersWorkbench);
            Integer patientWorkbenchId = (Integer) id;
            admittedPatientOrdersWorkbench.setPatientWorkbenchId(patientWorkbenchId);
            return admittedPatientOrdersWorkbench;
        }catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    @Override
    public Response checkPatientOrdersWorkbenchExist(AdmissionDto admissionDto) throws ApplicationException {
        try {
            Session session=sessionFactory.getCurrentSession();
            Integer id = (Integer) session.createSQLQuery(
                            "SELECT patient_workbench_id " +
                                    "FROM adt.t_admitted_patient_orders_workbench kin " +
                                    "WHERE kin.organization_id = :organizationId " +
                                    "AND kin.unit_id = :unitId " +
                                    "AND kin.record_id = :recordId " +
                                    "AND kin.tab_id = :tabId")
                    .setParameter("organizationId", admissionDto.getOrganizationId())
                    .setParameter("unitId", admissionDto.getUnitId())
                    .setParameter("recordId", admissionDto.getRecordId())
                    .setParameter("tabId", admissionDto.getTabId())
                    .uniqueResult();
            if(id==null){
                return new Response(SUCCESS,null, null, null, null);
            }else {
                return new Response(SUCCESS, ALREADY_EXIST, null, null, id);
            }
        }catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    @Override
    public AdmittedPatientOrdersWorkbench updatePatientOrdersWorkbench(AdmittedPatientOrdersWorkbench admittedPatientOrdersWorkbench) throws ApplicationException {
        try {
            Session session=sessionFactory.getCurrentSession();
            session.createSQLQuery(
                       "UPDATE adt.t_admitted_patient_orders_workbench " +
                                  "SET result_id = :resultId, " +
                                  "workbench_added_date = :workbenchAddedDate, " +
                                  "workbench_ended_date = :workbenchEndDate, " +
                                  "updated_date = :updatedDate, " +
                                  "updated_by = :updatedBy, " +
                                  "remark = :remark " +
                                  "WHERE patient_workbench_id = :patientWorkbenchId")
                    .setParameter("resultId", admittedPatientOrdersWorkbench.getResultId())
                    .setParameter( "workbenchAddedDate", admittedPatientOrdersWorkbench.getWorkbenchAddedDate(),StandardBasicTypes.TIMESTAMP)
                    .setParameter( "workbenchEndDate", admittedPatientOrdersWorkbench.getWorkbenchEndDate(),StandardBasicTypes.TIMESTAMP)
                    .setParameter("updatedDate", admittedPatientOrdersWorkbench.getUpdatedDate())
                    .setParameter("updatedBy", admittedPatientOrdersWorkbench.getUpdatedBy())
                    .setParameter("remark", admittedPatientOrdersWorkbench.getRemark())
                    .setParameter("patientWorkbenchId", admittedPatientOrdersWorkbench.getPatientWorkbenchId())
                    .executeUpdate();
            return admittedPatientOrdersWorkbench;
        }catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }
}

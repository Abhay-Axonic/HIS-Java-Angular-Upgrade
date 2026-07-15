package com.param.lis.transaction.dao;

import com.param.entity.lis.transaction.ClinicalInterventionProcedure;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ServiceException;
import com.param.lis.global.common.*;
import com.param.utility.TimeZoneComponent;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked","unused" })
public class ClinicalInterventionProcedureDaoImpl extends GenericDao<ClinicalInterventionProcedure, Integer>
        implements IClinicalInterventionProcedureDao, ICommonConstants, ITransactionConstants, IError {

    @Autowired
    private SessionFactory sessionFactory;

    public ClinicalInterventionProcedureDaoImpl() {
        super(ClinicalInterventionProcedure.class);
    }

    @Autowired
    private DozerBeanMapper mapper;
    final static Logger logger = Logger.getLogger(ClinicalInterventionProcedureDaoImpl.class);

    @Override
        public Response saveIntervention(ClinicalInterventionProcedureDto clinicalInterventionProcedureDto) throws ApplicationException {
        try{

            Date date =
                    TimeZoneComponent.getDateByzone(
                            clinicalInterventionProcedureDto.getUnitId()
                    );
            ClinicalInterventionProcedure clinicalInterventionProcedureSave = new ClinicalInterventionProcedure();

            clinicalInterventionProcedureSave.setPatientId(clinicalInterventionProcedureDto.getPatientId());
            clinicalInterventionProcedureSave.setLabSampleDtlsId(clinicalInterventionProcedureDto.getLabSampleDtlsId());
            clinicalInterventionProcedureSave.setIsClinicalInterventionRequired(clinicalInterventionProcedureDto.getIsClinicalInterventionRequired());
            clinicalInterventionProcedureSave.setInterventionRequestedNurseId(clinicalInterventionProcedureDto.getInterventionRequestedNurseId());
            clinicalInterventionProcedureSave.setStatus('A');
            clinicalInterventionProcedureSave.setUnitId(clinicalInterventionProcedureDto.getUnitId());
            clinicalInterventionProcedureSave.setOrganizationId(clinicalInterventionProcedureDto.getOrganizationId());
            clinicalInterventionProcedureSave.setCreatedBy(clinicalInterventionProcedureDto.getCreatedBy());
//            clinicalInterventionProcedureSave.setCreatedDate(date);
//            clinicalInterventionProcedureSave.setInterventionStartDateTime(date);
            clinicalInterventionProcedureSave.setCreatedDate(GlobalCommonDateUtils.getDate(clinicalInterventionProcedureDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
            clinicalInterventionProcedureSave.setInterventionStartDateTime(GlobalCommonDateUtils.getDate(clinicalInterventionProcedureDto.getInterventionStartDate(), "dd-M-yyyy HH:mm:ss"));

//            ClinicalInterventionProcedure clinicalInterventionProcedure = mapper.map(clinicalInterventionProcedureDto, ClinicalInterventionProcedure.class,
//                    "ClinicalInterventionProcedureDtoToClinicalInterventionProcedure");

            ClinicalInterventionProcedure clinicalInterventionProcedure = save(clinicalInterventionProcedureSave);
            return new Response<>(SUCCESS, SUCCESS_200, COMMON_SUCCESS, null, clinicalInterventionProcedure);
        }
        catch (Exception e){
            logger.error("Exception in saveIntervention", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Response updateInterventionForCompleteProcedure(ClinicalInterventionProcedureDto clinicalInterventionProcedureDto) throws ApplicationException {
        try {
            Integer count = sessionFactory.getCurrentSession()
                    .createSQLQuery(
                            " update public.t_clinical_intervention_procedure "
                                +   "   set intervention_processed_doctor_id =:doctorId, "
                                +   "   intervention_completed_date_time =:completedDateTime, "
                                +   "   updated_by =:doctorId, "
                                +   "   updated_date =:updatedDate, "
                                +   "   intervention_remark =:remarks "
                                +   " where clinical_intervention_procedure_id =:interventionId "
                                +   "   and patient_id =:patientId "
                                +   "   and unit_id =:unitId "
                                +   "   and organization_id =:orgId")
                    .setParameter("interventionId", clinicalInterventionProcedureDto.getClinicalInterventionProcedureId())
                    .setParameter("doctorId", clinicalInterventionProcedureDto.getInterventionCompletedDoctorId())
                    .setParameter("completedDateTime", GlobalCommonDateUtils.getDate(clinicalInterventionProcedureDto.getInterventionEndDate(), "dd-M-yyyy HH:mm:ss"))
                    .setParameter("remarks", clinicalInterventionProcedureDto.getRemarks())
                    .setParameter("patientId", clinicalInterventionProcedureDto.getPatientId())
                    .setParameter("unitId", clinicalInterventionProcedureDto.getUnitId())
                    .setParameter("orgId", clinicalInterventionProcedureDto.getOrganizationId())
                    .setParameter("updatedDate", GlobalCommonDateUtils.getDate(clinicalInterventionProcedureDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))
                    .executeUpdate();
            return new Response(SUCCESS, SUCCESS_200, null, null, count);
        } catch (Exception e) {
            logger.error("Exception in updateInterventionForCompleteProcedure", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Response getInterventionDetails(ClinicalInterventionProcedureDto clinicalInterventionProcedureDto) throws ApplicationException {
        try {
            StringBuilder sb = new StringBuilder(" select clinical_intervention_procedure_id as \"clinicalInterventionProcedureId\", "
                    +   " lab_sample_dtls_id as \"labSampleDtlsId\", "
                    +   " is_clinical_intervention_required as \"isClinicalInterventionRequired\", "
                    +   " intervention_processed_doctor_id as \"interventionCompletedDoctorId\", "
                    +   " patient_id as \"patientId\", "
                    +   " intervention_completed_date_time as \"interventionEndDate\" "
                    +   " from public.t_clinical_intervention_procedure "
                    +   " where "
                    +   "   unit_id =:unitId "
                    +   "   and organization_id =:orgId ");

            if(clinicalInterventionProcedureDto.getPatientId() != null && clinicalInterventionProcedureDto.getPatientId() >0){
                sb.append("and patient_id = :patientId ");
            }
            if(clinicalInterventionProcedureDto.getPatientIds() != null && !clinicalInterventionProcedureDto.getPatientIds().isEmpty()){
                sb.append("and patient_id in (:patientIds) ");
            }
            if(clinicalInterventionProcedureDto.getLabSampleDtlsId() != null && clinicalInterventionProcedureDto.getLabSampleDtlsId() >0){
                sb.append("and lab_sample_dtls_id = :labSampleDtlId ");
            }
            if(clinicalInterventionProcedureDto.getLabSampleDtlsIds() != null && !clinicalInterventionProcedureDto.getLabSampleDtlsIds().isEmpty()){
                sb.append("and lab_sample_dtls_id in (:labSampleDtlIds) ");
            }

            Query sqlQuery = sessionFactory.getCurrentSession()
                    .createSQLQuery(sb.toString())
                    .setParameter("unitId", clinicalInterventionProcedureDto.getUnitId())
                    .setParameter("orgId", clinicalInterventionProcedureDto.getOrganizationId());

            if(clinicalInterventionProcedureDto.getPatientId() != null && clinicalInterventionProcedureDto.getPatientId() >0){
                sqlQuery.setParameter("patientId", clinicalInterventionProcedureDto.getPatientId());
            }
            if(clinicalInterventionProcedureDto.getPatientIds() != null && !clinicalInterventionProcedureDto.getPatientIds().isEmpty()){
                sqlQuery.setParameterList("patientIds", clinicalInterventionProcedureDto.getPatientIds());
            }
            if(clinicalInterventionProcedureDto.getLabSampleDtlsId() != null && clinicalInterventionProcedureDto.getLabSampleDtlsId() >0){
                sqlQuery.setParameter("labSampleDtlId", clinicalInterventionProcedureDto.getLabSampleDtlsId());
            }
            if(clinicalInterventionProcedureDto.getLabSampleDtlsIds() != null && !clinicalInterventionProcedureDto.getLabSampleDtlsIds().isEmpty()){
                sqlQuery.setParameterList("labSampleDtlIds", clinicalInterventionProcedureDto.getLabSampleDtlsIds());
            }

            List<ClinicalInterventionProcedureDto> resList = sqlQuery
                    .setResultTransformer(Transformers.aliasToBean(ClinicalInterventionProcedureDto.class)).list();
            return new Response(SUCCESS, SUCCESS_200, null, resList, null);
        } catch (Exception e) {
            logger.error("Exception in getInterventionDetails", e);
            throw new ServiceException(e.getMessage());
        }
    }
}

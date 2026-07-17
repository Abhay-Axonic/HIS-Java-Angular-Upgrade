package com.param.hl7.dao;


import com.param.global.billing.controller.BillingException;
import com.param.global.dto.SlotMasterDto;
import com.param.global.model.Hl7MessageQueue;
import com.param.hl7.dto.*;
import in.param.common.dao.GenericDao;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@Repository
@Transactional
public class Hl7MessageDaoImpl extends GenericDao<Hl7MessageQueue, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(Hl7MessageDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;


    public Hl7MessageDaoImpl() {
        super(Hl7MessageQueue.class);
    }

    public Hl7MessageQueue saveMessage(Hl7MessageQueueDto dto) throws BillingException {
        try {
            Hl7MessageQueue master = new Hl7MessageQueue();
            master.setCode(dto.getCode());
            master.setMessage(dto.getMessage());
            master.setMessageType(dto.getMessageType());
            master.setTriggerEvent(dto.getTriggerEvent());
            master.setStatus("PENDING");
            master.setPatientId(dto.getPatientId());
            master.setEncounterId(dto.getEncounterId());
            master.setAdmissionId(dto.getAdmissionId());
            master.setUnitId(dto.getUnitId());
            master.setOrgId(dto.getOrgId());
            master.setResponseCode("0");
            master.setGenerateFor(dto.getGenerateFor());
            master = save(master);
            return master;

        } catch (HibernateException he) {
            throw he;
        } catch (Exception e) {
            
            throw e;
        }
    }

    public Integer checkPatientIsInICUOrOT(Integer patientId) throws BillingException {
        try {

            BigInteger count  = BigInteger.ZERO;
            String Query = "select "
                    + "	count(ta.admission_id) "
                    + "from "
                    + "	adt.t_admission ta "
                    + "inner join adt.t_admission_details tad on "
                    + "	tad.admission_id = ta.admission_id "
                    + "inner join adt.m_bed_category_master bedcat on "
                    + "	tad.bed_category_id = bedcat.bed_category_id "
                    + "left join otims.t_transfer_request otreq on "
                    + "	ta.admission_id = otreq.admission_id "
                    + "left join billing.m_patient_type pt on "
                    + "	pt.patient_type_id = ta.patient_class_id "
                    + "left join adt.m_admission_type_master admtype on "
                    + "	admtype.admission_type_id = tad.admission_type_id "
                    + "where "
                    + "	bedcat.is_icu = true "
                    + "	or (otreq.ot_transfer_status = 8 "
                    + "	and otreq.status = 'A') "
                    + "	and ta.patient_id = :patientId "
                    + "	and tad.status = 'A' "
                    + "	and ta.status = 'A' "
                    + " group by ta.admission_id order by "
                    + "	ta.admission_id desc "
                    + "limit 1";
            count = (BigInteger) sessionFactory.getCurrentSession()
                    .createSQLQuery(Query)
                    .setInteger("patientId", patientId).uniqueResult();
            count = count != null ? count : BigInteger.ZERO;
            return count.intValue();
        } catch (Exception e) {
            
            throw e;
        }
    }

    public PidDto getPID(Integer patientId) throws BillingException {
        try {

            String Query = ""
                    + "select "
                    + "	tpr.patient_id as \"patientId\", "
                    + "	tpr.email as \"emailId\", "
                    + "	tpr.uhid_number as \"patientIdentifire\", "
                    + "	tpr.first_name as \"givenName\", "
                    + "	tpr.last_name as \"familyName\", "
                    + "	to_char(tpr.dob, 'YYYYMMDD') as \"dob\", "
                    + "	cast(mgm.gender_prefix as varchar) as \"sex\", "
                    + "	city.city_code as \"cityCode\", "
                    + "	city.city_name as \"cityName\", "
                    + "	(case when country.country_calling_code is null then " +
                    " (select mcm1.country_calling_code from  public.m_country_master mcm1 where mcm1.country_id  = mom.country_id limit 1 ) " +
                    "  else country.country_calling_code end) as \"countryCode\",  "
                    + "	country.country_name as \"countryName\", "
                    + "	'N' as \"multipleBirthIndicator\", "
                    + "	 tpd.zip_code as \"zipCode\", "
                    + "  pfx.prefix_desc as \"prefixDesc\", "
                    + "  tpr.organization_id as \"hospitalId\", "
                    + "  tpr.unit_id as \"unitId\", "
                    + "  mom.organization_name as \"organizationName\", "
                    + "  mum.unit_name as \"unitName\", "
                    + "  state.state_name as \"stateName\", "
                    + "	 concat(tpr.mobile_number) as \"mobileNumber\"," +
                    "    tpd.address as \"address\"  "
                    + "from "
                    + "	patient.t_patient_registration tpr"
                    + " inner join public.m_organization_master mom on mom.organization_id= tpr.organization_id "
                    + " inner join public.m_unit_master mum  on mum.unit_id = tpr.unit_id "
                    + "left join patient.t_patient_details tpd on "
                    + "	tpr.patient_id = tpd.patient_id "
                    + "left join public.m_gender_master mgm on "
                    + "	mgm.gender_id = tpr.gender_id "
                    + "left join public.m_prefix_master pfx on "
                    + "	pfx.prefix_id = tpr.prefix_id "
                    + "left join public.m_country_master country on "
                    + "	country.country_id = tpd.country_id "
                    + "left join public.m_city_master city on "
                    + "	city.city_id = tpd.city_id "
                    + " left join public.m_state_master state on"
                    + " state.state_id = tpd.state_id "
                    + "  where tpr.patient_id  = :patientId limit 1";
            PidDto dto = (PidDto) sessionFactory.getCurrentSession()
                    .createSQLQuery(Query)
                    .setInteger("patientId", patientId).setResultTransformer(Transformers.aliasToBean(PidDto.class)).uniqueResult();

            return dto;
        } catch (Exception e) {
            
            throw e;
        }
    }

    public Pv1_2Dto getPv1_2(Integer admissionId) throws BillingException {
        try {

            if(admissionId != null && admissionId > 0) {
                String Query = "select "
                        + "	cast(ta.admission_id as varchar) as \"admissionId\" , "
                        + "	ta.patient_id as \"patientId\", "
                        + "	pt.description as \"patientType\" , "
                        + "	ta.admission_number as \"visitNumber\", "
                        + "	tad.bed_category_id as \"bedCategoryId\", "
                        + "	to_char(tad.doa , 'yyyymmddHHmmss') as \"admissionDate\", "
                        + "	'C' as \"admissionType\", "
                        + "	'C' as \"hospitalType\", "
                        + "	'M' as \"hospitalService\", "
                        + "	bed.bed_number as \"bedNo\", "
                        + "	ward.ward_name as \"wardName\", "
                        + "	room.room_desc as \"roomNo\", "
                        + "	mrm.reason_desc as \"reasonForAdmission\", "
                        + "	to_char(tad.pdd , 'yyyymmddHHmmss') as \"dischargeDate\" "
                        + "from "
                        + "	adt.t_admission ta "
                        + "inner join adt.t_admission_details tad on "
                        + "	tad.admission_id = ta.admission_id "
                        + "inner join adt.m_bed_category_master bedcat on "
                        + "	tad.bed_category_id = bedcat.bed_category_id "
                        + "left join otims.t_transfer_request otreq "
                        + "on ta.admission_id  = otreq.admission_id "
                        + "left join billing.m_patient_type pt on "
                        + "	pt.patient_type_id = ta.patient_class_id "
                        + "left join adt.m_admission_type_master admtype on "
                        + "	admtype.admission_type_id = tad.admission_type_id "
                        + "left join adt.m_bed_master bed on "
                        + "	tad.bed_id = bed.bed_id "
                        + "left join adt.m_ward_master ward on "
                        + "	tad.ward_id = ward.ward_id "
                        + "left join adt.m_room_master room on "
                        + "	tad.room_id = room.room_id "
                        + "left join adt.m_reason_master mrm on mrm.reason_master_id = ta.reason_id "
                        + "where "
                        + "	bedcat.is_icu  = true or (otreq.ot_transfer_status = 8 and otreq.status ='A') "
                        + "	and	ta.admission_id = :admissionId "
                        + "	and tad.status = 'A' limit 1";
                Pv1_2Dto dto = (Pv1_2Dto) sessionFactory.getCurrentSession()
                        .createSQLQuery(Query)
                        .setInteger("admissionId", admissionId).setResultTransformer(Transformers.aliasToBean(Pv1_2Dto.class)).uniqueResult();
                return dto;
            }
        } catch (Exception e) {
            
            throw e;
        }
         return null;
    }

    public List<OrcDto> getOrcDetails(Integer orderDetailsId) throws BillingException {
        try {
            if(orderDetailsId != null && orderDetailsId > 0) {
                String Query = "select "
                        + "	cast(tod.order_details_id as varchar) as \"orderDetailsId\", "
                        + "	to_char(tod.order_date,'yyyymmddHHmmss') as \"orderDate\", "
                        + "	tom.order_no as \"orderNo\" "
                        + " from "
                        + "	public.t_order_master tom "
                        + " inner join public.t_order_details tod on "
                        + "	tom.order_id  = tod.order_id "
                        + " inner join service.m_service_master service on "
                        + "	service.service_master_id  = tod.service_id "
                        + " inner join lab.m_test_master test on "
                        + "	tod.service_id  = test.service_id "
                        + "	and test.org_unit_id  = tod.org_unit_id "
                        + "	where cast (tod.order_details_id as varchar)  = :orderDetailsId  ";
                List<OrcDto> orcList = sessionFactory.getCurrentSession()
                        .createSQLQuery(Query)
                        .setString("orderDetailsId", String.valueOf(orderDetailsId)).setResultTransformer(Transformers.aliasToBean(OrcDto.class)).list();
                return orcList;
            }
        } catch (Exception e) {
            
            throw e;
        }
        return null;
    }
        public List<ObxDto> getObxDetails(String orderDetailsId,Integer bedCategoryId) throws BillingException {
            try {
                if(orderDetailsId != null && !orderDetailsId.isEmpty()) {
                    String Query = "select "
                            + "    cast(details.lab_res_dtls_id as text) as \"labResDtlsId\", "
                            + "	details.param_unit as \"paramUnit\", "
                            + "	details.param_name as \"paramName\", "
                            + "	cast(coalesce(details.is_abnormal,'N') as varchar) as \"abnormalFlag\", "
                            + "	concat(coalesce(details.parameter_min,'0'),'-',coalesce(details.parameter_max,'0')) as \"refRange\", "
                            + "	details.final_result as \"obrValue\", "
                            + "	'P' as \"obrResult\", "
                            + "	'NM' as \"refRangeType\", "
                            + "	 param.parameter_code as \"paramCode\" "
                            + "from "
                            + "	lab.t_lab_sample_dtls tlsd "
                            + "inner join public.t_order_details tod on "
                            + "	tlsd.order_details_id = tod.order_details_id "
                            + "inner join lab.t_lab_test_result tltr on "
                            + "	tltr.lab_sample_dtls_id = tlsd.lab_sample_dtls_id "
                            + "inner join lab.t_lab_res_dtls details on "
                            + "	details.lab_result_id  = tltr.lab_test_res_id "
                            + "inner join adt.m_bed_category_master mbcm on "
                            + "	mbcm.billing_bed_category_id = tod.bed_category_id "
                            + "inner join lab.m_parameter_master param on "
                            + "	param.parameter_id  = details.parameter_id "
                            + "	where cast (tlsd.order_details_id as varchar) =:orderDetailsId and tltr.sample_status_id  = 8 "
                            + "	and mbcm.bed_category_id =:bedCategoryId";
                    List<ObxDto> obxList = sessionFactory.getCurrentSession()
                            .createSQLQuery(Query)
                            .setString("orderDetailsId", orderDetailsId)
                            .setInteger("bedCategoryId", bedCategoryId)
                            .setResultTransformer(Transformers.aliasToBean(ObxDto.class)).list();
                    return obxList;
                }
            } catch (Exception e) {
                
                throw e;
            }
            return null;
    }

    public List<ObxDto> getObxDetails(Integer admissionId,Integer bedCategoryId) throws BillingException {
        try {
            if(admissionId != null && admissionId >0 && bedCategoryId != null && bedCategoryId >0) {
                String Query = "select "
                        + "	distinct(test.test_code) as \"testCode\" "
                        + "from "
                        + "	lab.t_lab_sample_master master "
                        + "inner join lab.t_lab_sample_dtls details on "
                        + "	details.lab_sample_id = details.lab_sample_id "
                        + "inner join public.t_order_details ord_details on "
                        + "	ord_details.order_details_id = details.order_details_id "
                        + "inner join lab.m_test_master test on "
                        + "	details.test_id = test.test_id "
                        + "inner join adt.m_bed_category_master mbcm on "
                        + "	mbcm.billing_bed_category_id = ord_details.bed_category_id "
                        + "where "
                        + "	master.admission_id = :admissionId "
                        + "	and mbcm.bed_category_id = :bedCategoryId "
                        + "	and ord_details.status  = 'A'";
                List<ObxDto> obxList = sessionFactory.getCurrentSession()
                        .createSQLQuery(Query)
                        .setInteger("admissionId", admissionId)
                        .setInteger("bedCategoryId", bedCategoryId)
                        .setResultTransformer(Transformers.aliasToBean(ObxDto.class)).list();
                return obxList;
            }
        } catch (Exception e) {
            
            throw e;
        }
        return null;
    }

    public List<OrcDto> getProcedureOrderDetails(Integer proReqId, Integer admissionId) throws BillingException {
        try {
            if(proReqId != null && proReqId > 0) {
                String Query = "select "
                        + "	cast(proc.procedure_order_id as varchar) as \"procedureOrderId\", "
                        + "	to_char(tprm.request_date ,'yyyymmddHHmmss') as \"orderDate\", "
                        + "	tom.order_no as \"orderNo\", "
                        + "	config.cpt_description as \"procedureName\", "
                        + "	cast(doctor.doctor_id as varchar) as \"oderProviderIdNumber\", "
                        + "	doctor.last_name as \"oderProviderFamilyName\", "
                        + "	to_char(cast(concat(to_char(tprm.prefered_date_time,'yyyy-mm-dd'),' ',to_char(tprm.prefered_time ,'HH24:mm:ss') ) as timestamp) ,'yyyymmddHHmmss') as \"observationStartTime\", "
                        + "	to_char(cast(concat(to_char(tprm.prefered_date_time,'yyyy-mm-dd'),' ',to_char(tprm.prefered_time ,'HH24:mm:ss') ) as timestamp) ,'yyyymmddHHmmss') as \"observationEndTime\" "
                        + "from "
                        + "	otims.t_procedure_req_mst tprm "
                        + "left join adt.t_admission ta on "
                        + "	tprm.admission_id = ta.admission_id "
                        + "left join doctor.m_doctor_master doctor on "
                        + "	ta.doctor_id = doctor.doctor_id "
                        + "left join otims.t_procedure_order_master proc on "
                        + "	proc.pro_req_id = tprm.pro_req_id "
                        + "left join otims.t_procedure_configuration config on "
                        + "	config.service_id = proc.service_id "
                        + "left join public.t_order_details tod on "
                        + "	tod.service_id = proc.service_id "
                        + "	and tod.is_procedure = 'Y' "
                        + "	and tod.status = 'A' "
                        + "left join public.t_order_master tom on "
                        + "	tod.order_id = tom.order_id "
                        + "where "
                        + "	tprm.admission_id = :admissionId "
                        + "	and tprm.pro_req_id =:proReqId";
                List<OrcDto> orcDtoList =  sessionFactory.getCurrentSession()
                        .createSQLQuery(Query)
                        .setInteger("proReqId", proReqId)
                        .setInteger("admissionId", admissionId)
                        .setResultTransformer(Transformers.aliasToBean(OrcDto.class)).list();
                return orcDtoList;
            }
        } catch (Exception e) {
            
            throw e;
        }
        return null;
    }

    public List<Al1Dto> getAllergyDetails(Integer admissionId) throws BillingException {
        try {
            if(admissionId != null && admissionId > 0) {
                String Query = "select "
                        + "	allergy.allergy_desc as \"allergyCode\" "
                        + "from "
                        + "	patient.t_allergy_patient_mapper tapm "
                        + "inner join emr.m_allergy_master allergy on "
                        + "	tapm.allergy_id = allergy.allergy_id "
                        + "where "
                        + "	tapm.admission_id =:admissionId";
                List<Al1Dto> al1DtoList = sessionFactory.getCurrentSession()
                        .createSQLQuery(Query)
                        .setInteger("admissionId", admissionId).setResultTransformer(Transformers.aliasToBean(Al1Dto.class)).list();
                return al1DtoList;
            }
        } catch (Exception e) {
            
            throw e;
        }
        return null;
    }

    public List<Dg1Dto> getDiagnosisDetails(Integer admissionId) throws BillingException {
        try {
            if(admissionId != null && admissionId > 0) {
                String Query = "select "
                        + "    cast(diag.patient_diagnosis_id as varchar)  as \"diagnosisId\", "
                        + "	master.code as \"icd10Code\", "
                        + "	typeid.diagnosis_type_name as \"diagnosisType\", "
                        + "	master.code as \"diagnosisCode\", "
                        + "	master.diagnosis_desc as \"diagnosisDescription\", "
                        + "	to_char(diag.created_date ,'yyyymmddHHmmss') as \"diagnosisDate\" "
                        + "from "
                        + "	patient.t_patient_diagnosis_appo_mapper diag "
                        + "inner join emr.m_diagnosis_master master on "
                        + "	diag.diagnosis_id = master.diagnosis_id "
                        + "inner join emr.m_diagnosis_type_master typeid on "
                        + "	typeid.diagnosis_type_id = diag.diagnosis_type "
                        + "where "
                        + "	diag.admission_id =:admissionId";
                List<Dg1Dto> dg1DtoList = sessionFactory.getCurrentSession()
                        .createSQLQuery(Query)
                        .setInteger("admissionId", admissionId).setResultTransformer(Transformers.aliasToBean(Dg1Dto.class)).list();
                return dg1DtoList;
            }
        } catch (Exception e) {
            
            throw e;
        }
        return null;
    }

    public List<Pr1Dto> getProcedureDetails(Integer admissionId) throws BillingException {
        try {
            if(admissionId != null && admissionId > 0) {
                String Query = "select "
                        + "	cast(proc.procedure_order_id as varchar) as \"procedureOrderId\", "
                        + "	config.cpt_code as \"procedureCode\", "
                        + "	config.cpt_description as \"procedureDescription\", "
                        + "	service.codification_service_name as \"codingMethod\", "
                        + "	proctype.procedure_type_desc as \"procedureType\", "
                        + "	to_char(cast(concat(to_char(tprm.prefered_date_time,'yyyy-mm-dd'),' ',to_char(tprm.prefered_time ,'HH24:mm:ss') ) as timestamp) ,'yyyymmddHHmmss') as \"procedureDate\" "
                        + "from "
                        + "	otims.t_procedure_req_mst tprm "
                        + "left join adt.t_admission ta on "
                        + "	tprm.admission_id = ta.admission_id "
                        + "left join otims.t_procedure_order_master proc on "
                        + "	proc.pro_req_id = tprm.pro_req_id "
                        + "left join otims.t_procedure_configuration config on "
                        + "	config.service_id = proc.service_id "
                        + "left join otims.m_procedure_type_master proctype on "
                        + "	config.procedure_type_id = proctype.procedure_type_id "
                        + "left join service.m_service_master service on "
                        + "	service.service_master_id = config.service_id "
                        + "where "
                        + "	tprm.admission_id = :admissionId";
                List<Pr1Dto> pr1DtoList = sessionFactory.getCurrentSession()
                        .createSQLQuery(Query)
                        .setInteger("admissionId", admissionId).setResultTransformer(Transformers.aliasToBean(Pr1Dto.class)).list();
                return pr1DtoList;
            }
        } catch (Exception e) {
            
            throw e;
        }
        return null;
    }

    public PidDto getAdmissionDetails(Integer admissionId) {

        try {
            if(admissionId != null && admissionId > 0) {

                String Query = """
                        select
                        \tto_char(sm.appointment_date,
                        \t'dd/MM/yyyy') as "appointmentDate",
                        \tam.appointment_id as "appointmentId",
                        \tsm.slot_id as "slotId",
                        \tconcat(to_char(sm.from_time,
                        \t'HH24:MI')) as "fromTimeString",
                        \tconcat(to_char(sm.to_time,
                        \t'HH24:MI')) as "toTimeString",
                        \tam.remark as "remark",
                        \tsm.doctor_id as "doctorId",
                        \tam.appointment_type_id as "appointmentTypeId",
                        \tam.appointment_status_id as "appointmentStatusId",
                        \tasm.appointment_status_desc as "appointmentStatusDesc",
                        \tconcat('Dr. ',
                        \tcoalesce(doc.first_name),
                        \t' ',
                        \tcoalesce(doc.middle_name),
                        \t' ',
                        \tcoalesce(doc.last_name)) as "doctorName",
                        \tsm.speciality_id as "specialityId",
                        \tspeciaityMaster.speciality_name as "specialityName",
                        \tconcat(coalesce(patient.first_name) ,
                        \t' ',
                        \tcoalesce(patient.middle_name) ,
                        \t' ',
                        \tcoalesce(patient.last_name)) as "patientName",
                        \tconcat(to_char(extract(year
                        from
                        \tAGE(CURRENT_TIMESTAMP,
                        \tpatient.dob)),
                        \t'999'),
                        \t'Y') as "age",
                        \tpatient.mobile_number as "mobileNumber",
                        \tpatient.email as "email",
                        \tpatient.patient_id as "patientId",
                        \tconcat(extract(year
                        from
                        \tAGE(current_timestamp,
                        \tpatient.dob))) as "ageYear",
                        \tpatient.uhid_number as "uhidNumber",
                        \tpatient.prefix_id as "prefixId",
                        \tprefix.prefix_code as "prefixCode",
                        \tprefix.prefix_desc as "prefixDesc",
                        \tpatient.first_name as "firstName",
                        \tpatient.middle_name as "middleName",
                        \tpatient.last_name as "lastName",
                        \tpatient.gender_id as "genderId",
                        \tpatientDetails.country_id as "countryId",
                        \tpatientDetails.state_id as "stateId",
                        \tpatientDetails.district_id as "districtId",
                        \tpatientDetails.city_id as "cityId",
                        \tpatientDetails.area_id as "areaId",
                        \tpatientDetails.zip_code as "zipCode",
                        \tpatientDetails.address as "address",
                        \tpatientDetails.marital_status_id as "maritalStatusId",
                        \tgender.gender_code as "genderCode",
                        \tto_char(patient.dob,
                        \t'DD/MM/YYYY') as "dob",
                        \tsm.modality_id as "modalityId",
                        \tmodality.modality_desc as "modalityDesc",
                        \tadm.admission_id as "admissionId",
                        \tslud.reason_id as "reasonId",
                        \treason.reason_desc as "reasonDesc",
                        \tslud.remark as "blockedRemark",
                        \tto_char(slud.created_date,
                        \t'dd/mm/yyyy HH:MI') as "createdDate",
                        \tto_char(am.arrival_time,
                        \t'HH:MI') as "arrivalTime",
                        \tam.token_number as "tokenNumber",
                        \tam.call_count as "count"
                        from
                        \topd.m_appointment_master am
                        left join opd.m_slot_master sm on
                        \tam.slot_id = sm.slot_id
                        inner join patient.t_patient_registration patient on
                        \tam.patient_id = patient.patient_id
                        inner join opd.m_appointment_status_master asm on
                        \tam.appointment_status_id = asm.appointment_status_id
                        left outer join adt.t_admission adm on
                        \tadm.patient_id = am.patient_id
                        \tand am.appointment_status_id = 1
                        \tand adm.status = 'A'
                        left join opd.t_encounter_master enc on
                        \tenc.appointment_id = am.appointment_id
                        left join opd.t_encounter_details enDet on
                        \tenDet.encounter_id = enc.encounter_id
                        left outer join doctor.m_speciality_master speciaityMaster on
                        \tsm.speciality_id = speciaityMaster.speciality_id
                        left outer join doctor.m_doctor_master doc on
                        \tsm.doctor_id = doc.doctor_id
                        left outer join opd.t_slot_block_unblock_detail slud on
                        \tslud.slot_id = sm.slot_id
                        left outer join adt.m_reason_master reason on
                        \tslud.reason_id = reason.reason_master_id
                        left outer join public.m_modality_master modality on
                        \tsm.modality_id = modality.modality_id
                        inner join public.m_gender_master gender on
                        \tpatient.gender_id = gender.gender_id
                        inner join public.m_prefix_master prefix on
                        \tpatient.prefix_id = prefix.prefix_id
                        inner join patient.t_patient_details patientDetails on
                        \tpatient.patient_id = patientDetails.patient_id
                        where
                        \tam.organization_id =2
                        \tand am.unit_id =2
                        \tand am.appointment_id=199222
                        order by
                        \tsm.appointment_date + sm.from_time desc\s\
                        """;

                PidDto pr1Dto = (PidDto) sessionFactory.getCurrentSession()
                        .createSQLQuery(Query)
                        .setParameter("admissionId", admissionId).setResultTransformer(Transformers.aliasToBean(PidDto.class)).uniqueResult();
                return pr1Dto;
            }
        } catch (Exception e) {

            throw e;
        }
        return null;
    }

    public SlotMasterDto getAdmissionDetailsForHl7(Integer appointmentId, Integer organizationId, Integer unitId) throws BillingException {
        try {
            String query = ""
                    + "SELECT "
                    + "    TO_CHAR(sm.appointment_date, 'dd-MM-yyyy') AS \"appointmentDate\", "
                    + "    am.appointment_id AS \"appointmentId\", "
                    + "    sm.slot_id AS \"slotId\", "
                    + "    TO_CHAR(sm.from_time, 'HH24:MI') AS \"fromTimeString\", "
                    + "    TO_CHAR(sm.to_time, 'HH24:MI') AS \"toTimeString\", "
                    + "    am.remark AS \"remark\", "
                    + "    sm.doctor_id AS \"doctorId\", "
                    + "    am.appointment_type_id AS \"appointmentTypeId\", "
                    + "    am.appointment_status_id AS \"appointmentStatusId\", "
                    + "    asm.appointment_status_desc AS \"appointmentStatusDesc\", "
                    + "    CONCAT(COALESCE(doc.first_name, ''), ' ', COALESCE(doc.last_name, '')) AS \"doctorName\", "
                    + "    sm.speciality_id AS \"specialityId\", "
                    + "    speciaityMaster.speciality_name AS \"specialityName\", "
                    + "    CONCAT(COALESCE(patient.first_name, ''), ' ', COALESCE(patient.middle_name, ''), ' ', COALESCE(patient.last_name, '')) AS \"patientName\", "
                    + "    CONCAT(EXTRACT(YEAR FROM AGE(CURRENT_TIMESTAMP, patient.dob)), 'Y') AS \"age\", "
                    + "    doc.mobile_number AS \"mobileNumber\", "
                    + "    doc.email AS \"email\", "
                    + "    patient.patient_id AS \"patientId\", "
                    + "    CONCAT(EXTRACT(YEAR FROM AGE(CURRENT_TIMESTAMP, patient.dob)), 'Y') AS \"ageYear\", "
                    + "    patient.uhid_number AS \"uhidNumber\", "
                    + "    patient.prefix_id AS \"prefixId\", "
                    + "    prefix.prefix_code AS \"prefixCode\", "
                    + "    prefix.prefix_desc AS \"prefixDesc\", "
                    + "    patient.first_name AS \"firstName\", "
                    + "    patient.middle_name AS \"middleName\", "
                    + "    patient.last_name AS \"lastName\", "
                    + "    patient.gender_id AS \"genderId\", "
                    + "    patientDetails.country_id AS \"countryId\", "
                    + "    patientDetails.state_id AS \"stateId\", "
                    + "    patientDetails.district_id AS \"districtId\", "
                    + "    patientDetails.city_id AS \"cityId\", "
                    + "    patientDetails.area_id AS \"areaId\", "
                    + "    patientDetails.zip_code AS \"zipCode\", "
                    + "    patientDetails.address AS \"address\", "
                    + "    patientDetails.marital_status_id AS \"maritalStatusId\", "
                    + "    gender.gender_code AS \"genderCode\", "
                    + "    TO_CHAR(patient.dob, 'DD/MM/YYYY') AS \"dob\", "
                    + "    TO_CHAR(am.arrival_time, 'HH:MI') AS \"arrivalTime\", "
                    + "    am.token_number AS \"tokenNumber\", "
                    + "    am.call_count AS \"count\", "
                    + "    mum.user_name AS \"userName\"," +
                    "   reason_desc as \"reasonDesc\"  "
                    + "FROM opd.m_appointment_master am "
                    + "LEFT JOIN opd.m_slot_master sm ON am.slot_id = sm.slot_id "
                    + "INNER JOIN patient.t_patient_registration patient ON am.patient_id = patient.patient_id "
                    + "INNER JOIN opd.m_appointment_status_master asm ON am.appointment_status_id = asm.appointment_status_id " +
                    " left join  adt.m_reason_master mrm  on mrm.reason_master_id  =am.reason_for_appointment_id  "
                    + "LEFT JOIN opd.t_encounter_master enc ON enc.appointment_id = am.appointment_id "
                    + "LEFT JOIN opd.t_encounter_details enDet ON enDet.encounter_id = enc.encounter_id "
                    + "LEFT JOIN doctor.m_speciality_master speciaityMaster ON sm.speciality_id = speciaityMaster.speciality_id "
                    + "left JOIN doctor.m_doctor_master doc ON sm.doctor_id = doc.doctor_id "
                    + "LEFT JOIN opd.t_slot_block_unblock_detail slud ON slud.slot_id = sm.slot_id "
                    + "INNER JOIN public.m_gender_master gender ON patient.gender_id = gender.gender_id "
                    + "INNER JOIN public.m_prefix_master prefix ON patient.prefix_id = prefix.prefix_id "
                    + "INNER JOIN patient.t_patient_details patientDetails ON patient.patient_id = patientDetails.patient_id "
                    + "INNER JOIN public.m_user_master mum ON mum.user_id = am.created_by "
                    + "WHERE am.organization_id = :organizationId "
                    + "AND am.unit_id = :unitId "
                    + "AND am.appointment_id = :appointmentId "
                    + "ORDER BY sm.appointment_date + sm.from_time DESC";


            SlotMasterDto dto = (SlotMasterDto) sessionFactory.getCurrentSession()
                    .createSQLQuery(query)
                    .setParameter("appointmentId", appointmentId)
                    .setParameter("organizationId", organizationId)
                    .setParameter("unitId", unitId)
                    .setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class))
                    .uniqueResult();

            return dto;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BillingException(e.getMessage());
        }
    }


    public SlotMasterDto getCancelledForHl7(Integer appointmentId, Integer organizationId, Integer unitId) {
        try {
            String query = ""
                    + " SELECT "
                    + "  reason_desc as \"reasonDesc\"  "
                    + " FROM opd.m_appointment_master am "
                    + " Inner join  adt.m_reason_master mrm  on mrm.reason_master_id  = am.cancel_reason_id "
                    + " WHERE am.organization_id = :organizationId "
                    + "AND am.unit_id = :unitId "
                    + "AND am.appointment_id = :appointmentId ";

            SlotMasterDto dto = (SlotMasterDto) sessionFactory.getCurrentSession()
                    .createSQLQuery(query)
                    .setParameter("appointmentId", appointmentId)
                    .setParameter("organizationId", organizationId)
                    .setParameter("unitId", unitId)
                    .setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class))
                    .uniqueResult();

            return dto;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BillingException(e.getMessage());
        }
    }

    public void updateAppointmentFlag(Integer appointmentId) {
        sessionFactory.getCurrentSession().createSQLQuery(" update opd.m_appointment_master set hl7_mesg_send = true where appointment_id  ="+appointmentId)
                .executeUpdate();
    }

    public Boolean checkAppointmentFlag(Integer appointmentIdOld) {
        String query = "SELECT hl7_mesg_send FROM opd.m_appointment_master WHERE appointment_id = :appointmentId";

        Object result = sessionFactory.getCurrentSession()
                .createSQLQuery(query)
                .setParameter("appointmentId", appointmentIdOld)
                .uniqueResult();

        // Convert safely to Boolean
        if (result instanceof Boolean boolean1) {
            return boolean1;
        } else if (result instanceof Number number) {
            return number.intValue() == 1;
        } else if (result != null) {
            return Boolean.parseBoolean(result.toString());
        }

        return false;
    }

}

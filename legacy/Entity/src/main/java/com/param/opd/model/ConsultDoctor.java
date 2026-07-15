package com.param.opd.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.admission.model.AdmissionDetails;
import com.param.adt.master.global.model.UserMaster;
import com.param.global.model.DoctorMaster;
import com.param.global.model.OutsideHospitalDoctorMapper;
import com.param.global.model.OutsideHospitalMaster;
import com.param.global.model.PatientRegistration;
import com.param.global.model.SpecialityMaster;
import com.param.global.model.VisitTypeMaster;
import com.param.opd.coversheet.model.EncounterMaster;

/*
 * 
 * Created by Tushar
 * 
 */

@NamedQueries({
	
	@NamedQuery(name="CONSULT_DOCTOR_LIST_BY_ENCOUNTER_ID",query="SELECT consultDoc.consultDoctorId as consultDoctorId, "
			+ "consultDoc.refByDoctorId as refByDoctorId, outSideHospMapper.hospDoctorName as outsideHospitalDoctorName, "
			+ "consultDoc.referredUnitId as referredUnitId, "
			+ "consultDoc.outsideHospitalId as outsideHospitalId, "
			+ " outSide.outsideHospitalDesc as outsideHospitalDesc, "
			+ "consultDoc.priority as priority, "
			+ "concat('Dr. ',coalesce(refByDoc.firstName),' ',coalesce(refByDoc.middleName),' ',coalesce(refByDoc.lastName)) as refByDoctor, "
			+ "consultDoc.refToDoctorId as refToDoctorId, "
			+ "concat('Dr. ',coalesce(refToDoc.firstName),' ',coalesce(refToDoc.middleName),' ',coalesce(refToDoc.lastName)) as refToDoctor, "
			+ "consultDoc.refToSpecialityId as refToSpecialityId, "
			+ "speciality.specialityName as specialityName, "
			+ "consultDoc.patientId as patientId, "
			+ "patient.uhidNumber as uhidNumber, "
			+ "concat(coalesce(prefix.prefixCode,'') ,' ',coalesce(patient.firstName,'') ,' ', coalesce(patient.middleName,'') ,' ', coalesce(patient.lastName,'')) as patientName, "
			+ "consultDoc.encounterId as encounterId, "
			+ "consultDoc.unitId as unitId, "
			+ "consultDoc.organizationId as organizationId, "
			+ " (case "
			+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
			+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) "
			+ " else 'Admin' end) as userName,"
			+ "consultDoc.notes as notes "
			+ "FROM ConsultDoctor consultDoc "
			+ "LEFT JOIN consultDoc.specialityMaster speciality "
			+ "INNER JOIN consultDoc.refByDoctorMaster refByDoc "
			+ "LEFT JOIN consultDoc.refToDoctorMaster refToDoc "
			+ "INNER JOIN consultDoc.patientRegistration patient "
			+ "INNER JOIN patient.prefixMaster as prefix "
			+ " LEFT JOIN consultDoc.outsideHospitalMaster outSide "
			+ " LEFT JOIN consultDoc.outsideHospitalDoctorMapper outSideHospMapper "

			+ " LEFT JOIN consultDoc.userMaster um "
			+ " LEFT JOIN um.employeeMastersList empmst "
			+ " LEFT JOIN um.doctorMastersList docmst "
			+ "WHERE consultDoc.status='A' "
			+ "AND consultDoc.encounterId=:encounterId "),
	
	@NamedQuery(name="GET_CONSULT_DOCTOR_LIST_BY_ADMISSION_ID",query="SELECT consultDoc.consultDoctorId as consultDoctorId, "
			+ "consultDoc.refByDoctorId as refByDoctorId, "
			+ "consultDoc.referredUnitId as referredUnitId, outSideHospMapper.hospDoctorName as outsideHospitalDoctorName, "
			+ "consultDoc.outsideHospitalId as outsideHospitalId, "
			+ " outSide.outsideHospitalDesc as outsideHospitalDesc, "
			+ "consultDoc.priority as priority, "
			+ "concat('Dr. ',coalesce(refByDoc.firstName),' ',coalesce(refByDoc.middleName),' ',coalesce(refByDoc.lastName)) as refByDoctor, "
			+ "consultDoc.refToDoctorId as refToDoctorId, "
			+ "concat('Dr. ',coalesce(refToDoc.firstName),' ',coalesce(refToDoc.middleName),' ',coalesce(refToDoc.lastName)) as refToDoctor, "
			+ "consultDoc.refToSpecialityId as refToSpecialityId, "
			+ "speciality.specialityName as specialityName, "
			+ " to_char(consultDoc.createdDate, 'DD/MM/YYYY') as createdDate, "
			+ "consultDoc.patientId as patientId, "
			+ "patient.uhidNumber as uhidNumber, "
			+ "concat(coalesce(prefix.prefixCode,'') ,' ',coalesce(patient.firstName,'') ,' ', coalesce(patient.middleName,'') ,' ', coalesce(patient.lastName,'')) as patientName, "
			+ "consultDoc.encounterId as encounterId, "
			+ "consultDoc.admissionId as admissionId, "
			+ "consultDoc.unitId as unitId, "
			+ "consultDoc.organizationId as organizationId, "
			+ " (case "
			+ " when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) "
			+ " when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) "
			+ " else 'Admin' end) as userName,"
			+ "consultDoc.notes as notes "
			+ "FROM ConsultDoctor consultDoc "
			+ "LEFT JOIN consultDoc.specialityMaster speciality "
			+ "LEFT JOIN consultDoc.refByDoctorMaster refByDoc "
			+ "LEFT JOIN consultDoc.refToDoctorMaster refToDoc "
			+ "INNER JOIN consultDoc.patientRegistration patient "
			+ "INNER JOIN patient.prefixMaster as prefix  "
			+ " LEFT JOIN consultDoc.userMaster um "
			+ " LEFT JOIN um.employeeMastersList empmst "
			+ " LEFT JOIN um.doctorMastersList docmst "
			+ " LEFT JOIN consultDoc.outsideHospitalMaster outSide "
			+ " LEFT JOIN consultDoc.outsideHospitalDoctorMapper outSideHospMapper "
			+ "WHERE consultDoc.status='A' "
			+ "AND consultDoc.admissionId=:admissionId "
//			+ "AND consultDoc.organizationId=:orgId "
//			+ "AND consultDoc.unitId=:unitId "
			),
	
	@NamedQuery(name="CONSULT_DOCTOR_LIST_FOR_BOOK_VIEW",query="SELECT consultDoc.consultDoctorId as consultDoctorId, "
			+ "consultDoc.refByDoctorId as refByDoctorId, "
			+ "consultDoc.referredUnitId as referredUnitId, "
			+ "consultDoc.outsideHospitalId as outsideHospitalId, "
			+ " outSide.outsideHospitalDesc as outsideHospitalDesc, "
			+ "consultDoc.priority as priority, "
			+ "concat('Dr. ',coalesce(refByDoc.firstName),' ',coalesce(refByDoc.middleName),' ',coalesce(refByDoc.lastName)) as refByDoctor, "
			+ "consultDoc.refToDoctorId as refToDoctorId, "
			+ "concat('Dr. ',coalesce(refToDoc.firstName),' ',coalesce(refToDoc.middleName),' ',coalesce(refToDoc.lastName)) as refToDoctor, "
			+ "consultDoc.refToSpecialityId as refToSpecialityId, "
			+ "speciality.specialityName as specialityName, "
			+ "consultDoc.patientId as patientId, "
			+ "patient.uhidNumber as uhidNumber, "
			+ "concat(concat(coalesce(prefix.prefixCode,'') ,' ',coalesce(patient.firstName,'') ,' ', coalesce(patient.middleName,'') ,' ', coalesce(patient.lastName,''))) as patientName, "
			+ "consultDoc.encounterId as encounterId, "
			+ "consultDoc.admissionId as admissionId, "
			+ "consultDoc.unitId as unitId, "
			+ "consultDoc.organizationId as organizationId, "
			+ "consultDoc.notes as notes "
			+ "FROM ConsultDoctor consultDoc "
			+ "LEFT JOIN consultDoc.specialityMaster speciality "
			+ "LEFT JOIN consultDoc.refByDoctorMaster refByDoc "
			+ "LEFT JOIN consultDoc.refToDoctorMaster refToDoc "
			+ "INNER JOIN consultDoc.patientRegistration patient "
			+ "INNER JOIN patient.prefixMaster as prefix  "
			+ " LEFT JOIN consultDoc.outsideHospitalMaster outSide "
			+ "WHERE consultDoc.status='A' "
			+ "AND consultDoc.patientId=:patientId "
			+ " AND to_char(consultDoc.createdDate , 'YYYY-MM-DD') BETWEEN :fromDate " 
			+ " AND :toDate "
			),
	
/*	@NamedQuery(name="PATIENT_LIST_BY_DOC_ORG_UNIT_ID_FOR_DOC_DESK",query="SELECT consultDoc.consultDoctorId as consultDoctorId, "
			+ "consultDoc.refByDoctorId as refByDoctorId, "
			+ "consultDoc.priority as priority, "
			+ "consultDoc.referredStatus as referredStatus, "
			+ "concat('Dr. ',coalesce(refByDoc.firstName),' ',coalesce(refByDoc.middleName),' ',coalesce(refByDoc.lastName)) as refByDoctor, "
			+ "consultDoc.refToDoctorId as refToDoctorId, "
			+ "concat('Dr. ',coalesce(refToDoc.firstName),' ',coalesce(refToDoc.middleName),' ',coalesce(refToDoc.lastName)) as refToDoctor, "
//			+ "consultDoc.refToSpecialityId as refToSpecialityId, "
			+ "consultDoc.refBySpecialityId as refBySpecialityId, "
//			+ "speciality.specialityName as specialityName, "
			+ "specialityRef.specialityName as specialityName, "
			+ "consultDoc.patientId as patientId, "
			+ "patient.uhidNumber as uhidNumber, "
			+ "patient.mpshahPatientId as mpshahPatientId, "
			+ "concat(coalesce(patient.firstName,'') ,' ', coalesce(patient.middleName,'') ,' ', coalesce(patient.lastName,'')) as patientName, "
			+ "consultDoc.encounterId as encounterId, "
			+ "consultDoc.unitId as unitId, "
			+ "consultDoc.organizationId as organizationId, "
			+ "admissionDetail.admissionId as admissionId, "
			+ "admissionDetail.wardId as wardId, "
			+ "wardMaster.wardName as wardName, "
			+ "bedMaster.bedNumber as bedNumber, "
			+ "bedMaster.bedId as bedId, "
			+ "to_char(encounterMaster.encounterDate, 'dd/mm/yyyy') as createdDate, "
			+ "to_char(encounterMaster.encounterTime, 'HH:mm') as createdTime, "
			+ "visitTypeMaster.visitTypeCode as visitTypeCode, "
			+ "encounterMaster.encounterId as encounterId, "
			+ "consultDoc.notes as notes "
			+ "FROM ConsultDoctor consultDoc "
			+ "LEFT JOIN consultDoc.specialityMaster speciality "
			+ "INNER JOIN consultDoc.specialityMasterByRefId specialityRef "
			+ "LEFT JOIN consultDoc.refByDoctorMaster refByDoc "
			+ "INNER JOIN consultDoc.refToDoctorMaster refToDoc "
			+ "INNER JOIN consultDoc.patientRegistration patient "
			+ "LEFT JOIN consultDoc.encounterMaster encounterMaster "
			+ "LEFT JOIN consultDoc.admissionDetail admissionDetail "
			+ "LEFT JOIN consultDoc.visitTypeMaster visitTypeMaster "
			+ "LEFT JOIN admissionDetail.bedMaster bedMaster "
			+ "LEFT JOIN admissionDetail.wardMaster wardMaster "
			+ "WHERE consultDoc.status='A' "
			+ "AND consultDoc.referredStatus='Pending' "
			+ "AND consultDoc.organizationId=:orgId "
			+ "AND consultDoc.unitId=:unitId "
			+ "AND consultDoc.refToDoctorId=:docId ")*/
})

@NamedNativeQueries({	
	@NamedNativeQuery(name="PATIENT_LIST_BY_DOC_ORG_UNIT_ID_FOR_DOC_DESK", 
	query=  "select " 
			 + " 	consultDoc.consult_doctor_id as \"consultDoctorId\", " 
			 + " 	consultDoc.ref_by_doctor_id as \"refByDoctorId\", " 
			 + " 	consultDoc.priority as \"priority\", " 
			 + " 	consultDoc.referred_status as \"referredStatus\", " 
			 + " 	(coalesce(refByDoc.first_name)|| ' ' || coalesce(refByDoc.middle_name)|| ' ' || coalesce(refByDoc.last_name)) as \"refByDoctor\", " 
			 + " 	consultDoc.ref_to_doctor_id as \"refToDoctorId\", " 
			 + " 	(coalesce(refToDoc.first_name)|| ' ' || coalesce(refToDoc.middle_name)|| ' ' || coalesce(refToDoc.last_name)) as \"refToDoctor\", " 
			 + " 	consultDoc.ref_by_speciality_id as \"refBySpecialityId\", " 
			 + " 	consultDoc.ref_to_speciality_id as \"refToSpecialityId\", "
			 + " 	speciality2_.speciality_name as \"specialityName\", " 
			 + " 	consultDoc.patient_id as \"patientId\", " 
			 + " 	to_char(admissionDetail.doa, 'DD/MM/YYYY') as \"doa\", "
			 + " 	to_char(admissionDetail.pdd, 'DD/MM/YYYY') as \"pdd\", "
			 + " 	patient.mpshah_patient_id as \"mpshahPatientId\", " 
			 + " 	consultDoc.encounter_id as \"encounterId\", " 
			 + " 	consultDoc.unit_id as \"unitId\", " 
			 + " 	consultDoc.organization_id as \"organizationId\", " 
			 + " 	consultDoc.admission_id as \"admissionId\", " 
			 + " 	0 as \"wardId\", " 
			 + " 	'-' as \"wardName\", " 
			 + " 	'-' as \"bedNumber\", " 
			 + " 	0 as \"bedId\", " 
			 + " 	unmst.unit_name as \"unitName\", " 
			 + " 	visitTypeMaster.visit_type_code as \"visitTypeCode\", " 
			 + " 	visitTypeMaster.visit_type_id as \"visitTypeId\", " 
			 + " 	consultDoc.notes as \"notes\", " 
			 + " 	to_char(consultDoc.created_date, 'dd/mm/yyyy') as \"createdDate\", " 
			 + " 	consultDoc.created_date as \"createdDateNew\", " 
			 + " 	to_char(consultDoc.created_date, 'HH:MI') as \"createdTime\", " 
			 + "	spd.payment_entitlement_id as \"paymentEntitlementId\", "
			 + " 	case " 
			 + " 		when patient.organization_id =:orgId then patient.uhid_number " 
			 + " 	end as \"uhidNumber\", " 
			 + " 	patient.is_vip as \"isVip\", " 
			 + " 	(case " 
			 + " 		when patient.is_vip = 'Y' then coalesce(patient.alise_name, patient.first_name) " 
			 + " 		else (coalesce(pre.prefix_code,'')|| ' ' ||coalesce(patient.first_name, '')|| ' ' || coalesce(patient.middle_name, '')|| ' ' || coalesce(patient.last_name, '')) " 
			 + " 	end) as \"patientName\", " 
			 + " 	(case " 
			 + " 		when encounterMaster.is_critical = 'Y' then 1 " 
			 + " 		else 0 " 
			 + " 	end) as \"isCriticalOrder\", "
			 + " 0  as \"bedCategoryId\", "
			 + " '-' as \"bedCategoryDecs\","
			 + " 'N' as \"isFinalBill\", "
			 + " admissionDetail.billing_bed_category_id as \"billingBedCategoryid\", "
			 + " to_char(patient.dob,'DD/MM/YYYY') AS \"dob\", "
			 +  " patient.gender_id as \"genderId\", "
			 +  " patient.patient_category_id as \"patientCategoryId\", " 
			 +  " patient.prefix_id as \"prefixId\", "
			 + " patient.unit_id as \"unitIdp\", "
			 + " patient.image_path as \"imagePath\", " 
			 + " patient.organization_id as \"organizationIdp\", "
			 + " uabc.default_self_tariff_id as \"defaultSelfTariffId\","
			 +  " gender.gender_code as \"genderCode\","
			 + " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, patient.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, patient.dob)),'D ') as \"age\" "
			 + " from " 
			 + " 	opd.t_consult_doctor consultDoc " 
			 + " inner join doctor.m_speciality_master speciality on " 
			 + " 	consultDoc.ref_to_speciality_id = speciality.speciality_id " 
			 + " inner join doctor.m_speciality_master speciality2_ on " 
			 + " 	consultDoc.ref_by_speciality_id = speciality2_.speciality_id " 
			 + " inner join doctor.m_doctor_master refByDoc on " 
			 + " 	consultDoc.ref_by_doctor_id = refByDoc.doctor_id " 
			 + " inner join doctor.m_doctor_master refToDoc on " 
			 + " 	consultDoc.ref_to_doctor_id = refToDoc.doctor_id " 
			 + " inner join patient.t_patient_registration patient on " 
			 + " 	consultDoc.patient_id = patient.patient_id " 
			 + " left join m_prefix_master pre on "
			 + "	pre.prefix_id = patient.prefix_id "
			 + " LEFT join opd.t_encounter_master encounterMaster on " 
			 + " 	consultDoc.encounter_id = encounterMaster.encounter_id " 
			 + " left join public.m_unit_master unmst on " 
			 + " 	consultDoc.unit_id = unmst.unit_id " 
			 + " LEFT join adt.t_admission admission on " 
			 + " 	consultDoc.admission_id = admission.admission_id " 
			 + " LEFT join adt.t_admission_details admissionDetail on " 
			 + " 	consultDoc.admission_id = admissionDetail.admission_id " 
			 + " inner join public.m_visit_type_master visitTypeMaster on " 
			 + " 	consultDoc.visit_type_id = visitTypeMaster.visit_type_id " 
			/* + " left join adt.m_bed_category_master bedategorymastr on bedMaster.bed_category_id = bedategorymastr.bed_category_id " */
			 + " inner join public.m_gender_master gender on gender.gender_id = patient.gender_id " 
			 + " left join opd.t_patient_sponsor_details spd on "
			 + " spd.admission_id = admissionDetail.admission_id "
			 + " and spd.status = 'A' and spd.priority_id = 1 "
			 + " left join public.m_payment_entitlement_master pe on "
			 + " spd.payment_entitlement_id = pe.payment_entitlement_id "
			 + "  inner join public.t_unit_application_billing_configuration uabc on "
			 + " uabc.unit_id = encounterMaster.unit_id"
			 + " where " 
			 + " 	consultDoc.status = 'A' " 
			 + " 	and consultDoc.referred_status = 'Pending' " 
			 + " 	and consultDoc.organization_id =:orgId " 
//			 + " 	and consultDoc.referred_unit_id =:unitId " 
			 + " 	and consultDoc.ref_to_doctor_id =:docId " 
			 + " 	and encounterMaster.status_id in (5,6 ) " 
			 + " union " 
			 + " select " 
			 + " 	consultDoc.consult_doctor_id as \"consultDoctorId\", " 
			 + " 	consultDoc.ref_by_doctor_id as \"refByDoctorId\", " 
			 + " 	consultDoc.priority as \"priority\", " 
			 + " 	consultDoc.referred_status as \"referredStatus\", " 
			 + " 	(coalesce(refByDoc.first_name)|| ' ' || coalesce(refByDoc.middle_name)|| ' ' || coalesce(refByDoc.last_name)) as \"refByDoctor\", " 
			 + " 	consultDoc.ref_to_doctor_id as \"refToDoctorId\", " 
			 + " 	(coalesce(refToDoc.first_name)|| ' ' || coalesce(refToDoc.middle_name)|| ' ' || coalesce(refToDoc.last_name)) as \"refToDoctor\", " 
			 + " 	consultDoc.ref_by_speciality_id as \"refBySpecialityId\", " 
			 + " 	consultDoc.ref_to_speciality_id as \"refToSpecialityId\", "
			 + " 	speciality2_.speciality_name as \"specialityName\", " 
			 + " 	consultDoc.patient_id as \"patientId\", " 
			 + " 	to_char(admissionDetail.doa, 'DD/MM/YYYY') as \"doa\", "
			 + " 	to_char(admissionDetail.pdd, 'DD/MM/YYYY') as \"pdd\", "
			 + " 	patient.mpshah_patient_id as \"mpshahPatientId\", " 
			 + " 	consultDoc.encounter_id as \"encounterId\", " 
			 + " 	consultDoc.unit_id as \"unitId\", " 
			 + " 	consultDoc.organization_id as \"organizationId\", " 
			 + " 	admissionDetail.admission_id as \"admissionId\", " 
			 + " 	admissionDetail.ward_id as \"wardId\", " 
			 + " 	wardMaster.ward_name as \"wardName\", " 
			 + " 	bedMaster.bed_number as \"bedNumber\", " 
			 + " 	bedMaster.bed_id as \"bedId\", " 
			 + " 	unmst.unit_name as \"unitName\", " 
			 + " 	visitTypeMaster.visit_type_code as \"visitTypeCode\", " 
			 + " 	visitTypeMaster.visit_type_id as \"visitTypeId\", " 
			 + " 	consultDoc.notes as \"notes\", " 
			 + " 	to_char(consultDoc.created_date, 'dd/mm/yyyy') as \"createdDate\", " 
			 + " 	consultDoc.created_date as \"createdDateNew\", " 
			 + " 	to_char(consultDoc.created_date, 'HH:MI') as \"createdTime\", "
			 + "	spd.payment_entitlement_id as \"paymentEntitlementId\", "
			 + " 	case " 
			 + " 		when patient.organization_id =:orgId then patient.uhid_number " 
			 + " 	end as \"uhidNumber\", " 
			 + " 	patient.is_vip as \"isVip\", " 
			 + " 	(case " 
			 + " 		when patient.is_vip = 'Y' then coalesce(patient.alise_name, patient.first_name) " 
			 + " 		else (coalesce(pre.prefix_code,'')|| ' ' ||coalesce(patient.first_name, '')|| ' ' || coalesce(patient.middle_name, '')|| ' ' || coalesce(patient.last_name, '')) " 
			 + " 	end) as \"patientName\", " 
		     + " 0  as \"isCriticalOrder\", "
			 + " 0  as \"bedCategoryId\", "
			 + " '-' as \"bedCategoryDecs\", "
			 + " admission.is_final_bill as \"isFinalBill\", "
			 + " admissionDetail.billing_bed_category_id as \"billingBedCategoryid\", "
			 + " to_char(patient.dob,'DD/MM/YYYY') AS \"dob\", "
			 +  " patient.gender_id as \"genderId\", "
			 +  " patient.patient_category_id as \"patientCategoryId\", " 
			 +  " patient.prefix_id as \"prefixId\", "
			 + " patient.unit_id as \"unitIdp\", "
			 + " patient.image_path as \"imagePath\", " 
			 + " patient.organization_id as \"organizationIdp\", "
			 + " uabc.default_self_tariff_id as \"defaultSelfTariffId\","
			 +  " gender.gender_code as \"genderCode\","
			 + " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, patient.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, patient.dob)),'D ') as \"age\" "
			 + " from " 
			 + " 	opd.t_consult_doctor consultDoc " 
			 + " inner join doctor.m_speciality_master speciality on " 
			 + " 	consultDoc.ref_to_speciality_id = speciality.speciality_id " 
			 + " inner join doctor.m_speciality_master speciality2_ on " 
			 + " 	consultDoc.ref_by_speciality_id = speciality2_.speciality_id " 
			 + " inner join doctor.m_doctor_master refByDoc on " 
			 + " 	consultDoc.ref_by_doctor_id = refByDoc.doctor_id " 
			 + " inner join doctor.m_doctor_master refToDoc on " 
			 + " 	consultDoc.ref_to_doctor_id = refToDoc.doctor_id " 
			 + " inner join patient.t_patient_registration patient on " 
			 + " 	consultDoc.patient_id = patient.patient_id " 
			 + " left join m_prefix_master pre on "
			 + "	pre.prefix_id = patient.prefix_id "
			 + " LEFT join adt.t_admission admission on " 
			 + " 	consultDoc.admission_id = admission.admission_id " 
			 + " LEFT join adt.t_admission_details admissionDetail on " 
			 + " 	consultDoc.admission_id = admissionDetail.admission_id "
			 + " inner join adt.m_bed_master bedMaster on " 
			 + " 	admissionDetail.bed_id = bedMaster.bed_id " 
			 + " inner join adt.m_ward_master wardMaster on " 
			 + " 	admissionDetail.ward_id = wardMaster.ward_id " 
			 + " inner join public.m_visit_type_master visitTypeMaster on " 
			 + " 	consultDoc.visit_type_id = visitTypeMaster.visit_type_id " 
			 + " left join public.m_unit_master unmst on " 
			 + " 	consultDoc.unit_id = unmst.unit_id " 
			 + " left join adt.m_bed_category_master bedategorymastr on bedMaster.bed_category_id = bedategorymastr.bed_category_id " 
			 + " inner join public.m_gender_master gender on gender.gender_id = patient.gender_id "
			 + " left join opd.t_patient_sponsor_details spd on "
			 + " spd.admission_id = admissionDetail.admission_id "
			 + " and spd.status = 'A' and spd.priority_id = 1 "
			 + " left join public.m_payment_entitlement_master pe on "
			 + " spd.payment_entitlement_id = pe.payment_entitlement_id"
			 + " inner join public.t_unit_application_billing_configuration uabc on "
			 + " uabc.unit_id = admissionDetail.unit_id"
			 + " where " 
			 + " 	consultDoc.status = 'A' " 
			 + " 	and (consultDoc.referred_status = 'Pending' or consultDoc.referred_status = 'Completed') " 
			 + " 	and consultDoc.organization_id =:orgId " 
//			 + " 	and consultDoc.referred_unit_id =:unitId " 
			 + " 	and consultDoc.ref_to_doctor_id =:docId " 
			 + " 	and admissionDetail.status = 'A' "
			 + " 	and admissionDetail.nursing_acceptance = 'Y' " 
			 + "    order by \"isCriticalOrder\" desc, \"consultDoctorId\" desc "),

     @NamedNativeQuery(
			name = "GET_WORKBENCH_DETAILS_FOR_CONSULT",
			query = "SELECT workbench.patient_workbench_id AS \"patientWorkbenchId\", "
					+ " workbench.record_id AS \"consultDoctorId\", "
					+ " workbench.result_id AS \"resultId\", "
					+ " workbench.workbench_added_date AS \"workbenchAddedDate\", "
					+ " workbench.remark AS \"workbenchRemark\" "
					+ " FROM adt.t_admitted_patient_orders_workbench workbench "
					+ " WHERE workbench.record_id IN (:recordIds) "
					+ " AND workbench.tab_id = 6 "
					+ " AND workbench.patient_id = :patientId "
					+ " AND workbench.unit_id = :unitId "
					+ " AND workbench.organization_id = :organizationId" ),

	@NamedNativeQuery(name="PATIENT_LIST_BY_DOC_ORG_UNIT_ID_FOR_DOC_DESK_BY_RMO_MAPPED_DOCTOR_LIST", 
	query=   "select " 
			 + " 	consultDoc.consult_doctor_id as \"consultDoctorId\", " 
			 + " 	consultDoc.ref_by_doctor_id as \"refByDoctorId\", " 
			 + " 	consultDoc.priority as \"priority\", " 
			 + " 	consultDoc.referred_status as \"referredStatus\", " 
			 + " 	(coalesce(refByDoc.first_name)|| ' ' || coalesce(refByDoc.middle_name)|| ' ' || coalesce(refByDoc.last_name)) as \"refByDoctor\", " 
			 + " 	consultDoc.ref_to_doctor_id as \"refToDoctorId\", " 
			 + " 	(coalesce(refToDoc.first_name)|| ' ' || coalesce(refToDoc.middle_name)|| ' ' || coalesce(refToDoc.last_name)) as \"refToDoctor\", " 
			 + " 	consultDoc.ref_by_speciality_id as \"refBySpecialityId\", " 
			 + " 	consultDoc.ref_to_speciality_id as \"refToSpecialityId\", "
			 + " 	speciality2_.speciality_name as \"specialityName\", " 
			 + " 	consultDoc.patient_id as \"patientId\", " 
			 + " 	to_char(admissionDetail.doa, 'DD/MM/YYYY') as \"doa\", "
			 + " 	to_char(admissionDetail.pdd, 'DD/MM/YYYY') as \"pdd\", "
			 + " 	patient.mpshah_patient_id as \"mpshahPatientId\", " 
			 + " 	consultDoc.encounter_id as \"encounterId\", " 
			 + " 	consultDoc.unit_id as \"unitId\", " 
			 + " 	consultDoc.organization_id as \"organizationId\", " 
			 + " 	consultDoc.admission_id as \"admissionId\", " 
			 + " 	0 as \"wardId\", " 
			 + " 	'-' as \"wardName\", " 
			 + " 	'-' as \"bedNumber\", " 
			 + " 	0 as \"bedId\", " 
			 + " 	unmst.unit_name as \"unitName\", " 
			 + " 	visitTypeMaster.visit_type_code as \"visitTypeCode\", " 
			 + " 	visitTypeMaster.visit_type_id as \"visitTypeId\", " 
			 + " 	consultDoc.notes as \"notes\", " 
			 + " 	to_char(consultDoc.created_date, 'dd/mm/yyyy') as \"createdDate\", " 
			 + " 	consultDoc.created_date as \"createdDateNew\", " 
			 + " 	to_char(consultDoc.created_date, 'HH:MI') as \"createdTime\", " 
			 + " 	case " 
			 + " 		when patient.organization_id =:orgId then patient.uhid_number " 
			 + " 	end as \"uhidNumber\", " 
			 + " 	patient.is_vip as \"isVip\", " 
			 + " 	(case " 
			 + " 		when patient.is_vip = 'Y' then coalesce(patient.alise_name, patient.first_name) " 
			 + " 		else (coalesce(pre.prefix_code,'')|| ' ' ||coalesce(patient.first_name, '')|| ' ' || coalesce(patient.middle_name, '')|| ' ' || coalesce(patient.last_name, '')) " 
			 + " 	end) as \"patientName\", " 
			 + " 	(case " 
			 + " 		when encounterMaster.is_critical = 'Y' then 1 " 
			 + " 		else 0 " 
			 + " 	end) as \"isCriticalOrder\", "
			 + " 0  as \"bedCategoryId\", "
			 + " '-' as \"bedCategoryDecs\", "
			 + " admissionDetail.billing_bed_category_id as \"billingBedCategoryid\", "
			 + " to_char(patient.dob,'DD/MM/YYYY') AS \"dob\", "
			 +  " patient.gender_id as \"genderId\", "
			 +  " patient.patient_category_id as \"patientCategoryId\", " 
			 +  " patient.prefix_id as \"prefixId\", "
			 + " spd.payment_entitlement_id as \"paymentEntitlementId\", "
			 + " patient.unit_id as \"unitIdp\", "
			 + " patient.organization_id as \"organizationIdp\", "
			 + " patient.image_path as \"imagePath\", " 
			 + " uabc.default_self_tariff_id as \"defaultSelfTariffId\","
			 +  " gender.gender_code as \"genderCode\",  " 
			 + " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, patient.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, patient.dob)),'D ') as \"age\" "
			 + " from " 
			 + " 	opd.t_consult_doctor consultDoc " 
			 + " inner join doctor.m_speciality_master speciality on " 
			 + " 	consultDoc.ref_to_speciality_id = speciality.speciality_id " 
			 + " inner join doctor.m_speciality_master speciality2_ on " 
			 + " 	consultDoc.ref_by_speciality_id = speciality2_.speciality_id " 
			 + " inner join doctor.m_doctor_master refByDoc on " 
			 + " 	consultDoc.ref_by_doctor_id = refByDoc.doctor_id " 
			 + " inner join doctor.m_doctor_master refToDoc on " 
			 + " 	consultDoc.ref_to_doctor_id = refToDoc.doctor_id " 
			 + " inner join patient.t_patient_registration patient on " 
			 + " 	consultDoc.patient_id = patient.patient_id "
			 + " left join m_prefix_master pre on "
			 + "	pre.prefix_id = patient.prefix_id "
			 + " LEFT join adt.t_admission_details admissionDetail on " 
			 + " 	consultDoc.admission_id = admissionDetail.admission_id " 
			 + " LEFT join opd.t_encounter_master encounterMaster on " 
			 + " 	consultDoc.encounter_id = encounterMaster.encounter_id " 
			 + " left join public.m_unit_master unmst on " 
			 + " 	consultDoc.unit_id = unmst.unit_id " 
			 + " inner join public.m_visit_type_master visitTypeMaster on " 
			 + " 	consultDoc.visit_type_id = visitTypeMaster.visit_type_id "
		//	 + " left join adt.m_bed_category_master bedategorymastr on bedMaster.bed_category_id = bedategorymastr.bed_category_id " 
			 + " inner join public.m_gender_master gender on gender.gender_id = patient.gender_id " 
			 + " left join opd.t_patient_sponsor_details spd on "
			 + " spd.admission_id = admissionDetail.admission_id "
			 + " and spd.status = 'A'"
			 + " left join public.m_payment_entitlement_master pe on "
			 + " spd.payment_entitlement_id = pe.payment_entitlement_id "
			 + "  inner join public.t_unit_application_billing_configuration uabc on "
			 + " uabc.unit_id = encounterMaster.unit_id"
			 + " where " 
			 + " 	consultDoc.status = 'A' " 
			 + " 	and consultDoc.referred_status = 'Pending' " 
			 + " 	and consultDoc.organization_id =:orgId " 
//			 + " 	and consultDoc.referred_unit_id =:unitId " 
			 + "	and consultDoc.ref_to_doctor_id IN (:doctorIdList) " 
			 + " 	and encounterMaster.status_id in (5,6) " 
			 + " union " 
			 + " select " 
			 + " 	consultDoc.consult_doctor_id as \"consultDoctorId\", " 
			 + " 	consultDoc.ref_by_doctor_id as \"refByDoctorId\", " 
			 + " 	consultDoc.priority as \"priority\", " 
			 + " 	consultDoc.referred_status as \"referredStatus\", " 
			 + " 	(coalesce(refByDoc.first_name)|| ' ' || coalesce(refByDoc.middle_name)|| ' ' || coalesce(refByDoc.last_name)) as \"refByDoctor\", " 
			 + " 	consultDoc.ref_to_doctor_id as \"refToDoctorId\", " 
			 + " 	(coalesce(refToDoc.first_name)|| ' ' || coalesce(refToDoc.middle_name)|| ' ' || coalesce(refToDoc.last_name)) as \"refToDoctor\", " 
			 + " 	consultDoc.ref_by_speciality_id as \"refBySpecialityId\", " 
			 + " 	consultDoc.ref_to_speciality_id as \"refToSpecialityId\", "
			 + " 	speciality2_.speciality_name as \"specialityName\", " 
			 + " 	consultDoc.patient_id as \"patientId\", " 
			 + " 	to_char(admissionDetail.doa, 'DD/MM/YYYY') as \"doa\", "
			 + " 	to_char(admissionDetail.pdd, 'DD/MM/YYYY') as \"pdd\", "
			 + " 	patient.mpshah_patient_id as \"mpshahPatientId\", " 
			 + " 	consultDoc.encounter_id as \"encounterId\", " 
			 + " 	consultDoc.unit_id as \"unitId\", " 
			 + " 	consultDoc.organization_id as \"organizationId\", " 
			 + " 	admissionDetail.admission_id as \"admissionId\", " 
			 + " 	admissionDetail.ward_id as \"wardId\", " 
			 + " 	wardMaster.ward_name as \"wardName\", " 
			 + " 	bedMaster.bed_number as \"bedNumber\", " 
			 + " 	bedMaster.bed_id as \"bedId\", " 
			 + " 	unmst.unit_name as \"unitName\", " 
			 + " 	visitTypeMaster.visit_type_code as \"visitTypeCode\", " 
			 + " 	visitTypeMaster.visit_type_id as \"visitTypeId\", " 
			 + " 	consultDoc.notes as \"notes\", " 
			 + " 	to_char(consultDoc.created_date, 'dd/mm/yyyy') as \"createdDate\", " 
			 + " 	consultDoc.created_date as \"createdDateNew\", " 
			 + " 	to_char(consultDoc.created_date, 'HH:MI') as \"createdTime\", " 
			 + " 	case " 
			 + " 		when patient.organization_id =:orgId then patient.uhid_number " 
			 + " 	end as \"uhidNumber\", " 
			 + " 	patient.is_vip as \"isVip\", " 
			 + " 	(case " 
			 + " 		when patient.is_vip = 'Y' then coalesce(patient.alise_name, patient.first_name) " 
			 + " 		else (coalesce(pre.prefix_code,'')|| ' ' ||coalesce(patient.first_name, '')|| ' ' || coalesce(patient.middle_name, '')|| ' ' || coalesce(patient.last_name, '')) " 
			 + " 	end) as \"patientName\", " 
			 + " 	0 as \"isCriticalOrder\","
			 + " bedMaster.bed_category_id  as \"bedCategoryId\", "
			 + " bedategorymastr.bed_category_desc as \"bedCategoryDecs\", "
			 + " admissionDetail.billing_bed_category_id as \"billingBedCategoryid\", "
			 + " to_char(patient.dob,'DD/MM/YYYY') AS \"dob\", "
			 +  " patient.gender_id as \"genderId\", "
			 +  " patient.patient_category_id as \"patientCategoryId\", " 
			 +  " patient.prefix_id as \"prefixId\", "
			 + " spd.payment_entitlement_id as \"paymentEntitlementId\", "
			 + " patient.unit_id as \"unitIdp\", "
			 + " patient.organization_id as \"organizationIdp\", "
			 + " patient.image_path as \"imagePath\", " 
			 + " uabc.default_self_tariff_id as \"defaultSelfTariffId\","
			 +  " gender.gender_code as \"genderCode\", " 
			 + " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, patient.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, patient.dob)),'D ') as \"age\" "
			 + " from " 
			 + " 	opd.t_consult_doctor consultDoc " 
			 + " inner join doctor.m_speciality_master speciality on " 
			 + " 	consultDoc.ref_to_speciality_id = speciality.speciality_id " 
			 + " inner join doctor.m_speciality_master speciality2_ on " 
			 + " 	consultDoc.ref_by_speciality_id = speciality2_.speciality_id " 
			 + " inner join doctor.m_doctor_master refByDoc on " 
			 + " 	consultDoc.ref_by_doctor_id = refByDoc.doctor_id " 
			 + " inner join doctor.m_doctor_master refToDoc on " 
			 + " 	consultDoc.ref_to_doctor_id = refToDoc.doctor_id " 
			 + " inner join patient.t_patient_registration patient on " 
			 + " 	consultDoc.patient_id = patient.patient_id " 
			 + " left join m_prefix_master pre on "
			 + "	pre.prefix_id = patient.prefix_id "
			 + " LEFT join adt.t_admission_details admissionDetail on " 
			 + " 	consultDoc.admission_id = admissionDetail.admission_id " 
			 + " inner join adt.m_bed_master bedMaster on " 
			 + " 	admissionDetail.bed_id = bedMaster.bed_id " 
			 + " inner join adt.m_ward_master wardMaster on " 
			 + " 	admissionDetail.ward_id = wardMaster.ward_id " 
			 + " inner join public.m_visit_type_master visitTypeMaster on " 
			 + " 	consultDoc.visit_type_id = visitTypeMaster.visit_type_id " 
			 + " left join public.m_unit_master unmst on " 
			 + " 	consultDoc.unit_id = unmst.unit_id "
			 + "left join adt.m_bed_category_master bedategorymastr on bedMaster.bed_category_id = bedategorymastr.bed_category_id " 
			 + " inner join public.m_gender_master gender on gender.gender_id = patient.gender_id " 
			 + " left join opd.t_patient_sponsor_details spd on "
			 + " spd.admission_id = admissionDetail.admission_id "
			 + " and spd.status = 'A'"
			 + " left join public.m_payment_entitlement_master pe on "
			 + " spd.payment_entitlement_id = pe.payment_entitlement_id"
			 + " inner join public.t_unit_application_billing_configuration uabc on "
			 + " uabc.unit_id = admissionDetail.unit_id"
			 + " where " 
			 + " 	consultDoc.status = 'A' " 
			 + " 	and (consultDoc.referred_status = 'Pending' or consultDoc.referred_status = 'Complete') " 
			 + " 	and consultDoc.organization_id =:orgId " 
//			 + " 	and consultDoc.referred_unit_id =:unitId " 
			 + " 	and admissionDetail.status = 'A' " 
			 + " 	and admissionDetail.nursing_acceptance = 'Y' "
			 + "	and consultDoc.ref_to_doctor_id IN (:doctorIdList) "
			 + "    order by \"isCriticalOrder\" desc, \"consultDoctorId\" desc ")
})




@Entity
@Table(name="t_consult_doctor",schema="opd")
@SequenceGenerator(name = "consult_doctor_seq", sequenceName = "opd.consult_doctor_seq", allocationSize = 1)
public class ConsultDoctor {
     
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consult_doctor_seq")
	@Column(name = "consult_doctor_id")
	private Integer consultDoctorId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="ref_by_doctor_id")
	private Integer refByDoctorId;
	
	@Column(name="ref_to_doctor_id")
	private Integer refToDoctorId;
	
	@Column(name="ref_to_speciality_id")
	private Integer refToSpecialityId;
	
	@Column(name="ref_by_speciality_id")
	private Integer refBySpecialityId;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	
	@Column(name="visit_type_id")
	private Integer visitTypeId;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="notes")
	private String notes;
	
	@Column(name="priority")
	private String priority;
	
	@Column(name="referred_status")
	private String referredStatus;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	
	@Column(name = "referred_unit_id")
	private Integer referredUnitId;
	
	@Column(name = "outside_hospital_id")
	private Integer outsideHospitalId;
	
	@Column(name = "outside_hos_doc_id")
	private Integer outsideHosDocId;
	
	@Column(name="cov_template_id")
	private Integer covTemplateId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ref_to_speciality_id", insertable = false, updatable = false)
	private SpecialityMaster specialityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ref_by_speciality_id", insertable = false, updatable = false)
	private SpecialityMaster specialityMasterByRefId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ref_by_doctor_id", insertable = false, updatable = false)
	private DoctorMaster refByDoctorMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ref_to_doctor_id", insertable = false, updatable = false)
	private DoctorMaster refToDoctorMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "encounter_id", insertable = false, updatable = false)
	private EncounterMaster encounterMaster ;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admission_id", insertable = false, updatable = false)
	private AdmissionDetails admissionDetail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "visit_type_id", insertable = false, updatable = false)
	private VisitTypeMaster visitTypeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, updatable = false)
	private PatientRegistration patientRegistration;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", insertable = false, updatable = false)
	private UserMaster userMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "outside_hospital_id", insertable = false, updatable = false)
	private OutsideHospitalMaster outsideHospitalMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "outside_hos_doc_id", insertable = false, updatable = false)
	private OutsideHospitalDoctorMapper outsideHospitalDoctorMapper;
	
	public Integer getConsultDoctorId() {
		return consultDoctorId;
	}

	public void setConsultDoctorId(Integer consultDoctorId) {
		this.consultDoctorId = consultDoctorId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getRefByDoctorId() {
		return refByDoctorId;
	}

	public void setRefByDoctorId(Integer refByDoctorId) {
		this.refByDoctorId = refByDoctorId;
	}

	public Integer getRefToDoctorId() {
		return refToDoctorId;
	}

	public void setRefToDoctorId(Integer refToDoctorId) {
		this.refToDoctorId = refToDoctorId;
	}

	public Integer getRefToSpecialityId() {
		return refToSpecialityId;
	}

	public void setRefToSpecialityId(Integer refToSpecialityId) {
		this.refToSpecialityId = refToSpecialityId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getRefBySpecialityId() {
		return refBySpecialityId;
	}

	public void setRefBySpecialityId(Integer refBySpecialityId) {
		this.refBySpecialityId = refBySpecialityId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getReferredStatus() {
		return referredStatus;
	}

	public void setReferredStatus(String referredStatus) {
		this.referredStatus = referredStatus;
	}

	public Integer getReferredUnitId() {
		return referredUnitId;
	}

	public void setReferredUnitId(Integer referredUnitId) {
		this.referredUnitId = referredUnitId;
	}

	public Integer getOutsideHospitalId() {
		return outsideHospitalId;
	}

	public void setOutsideHospitalId(Integer outsideHospitalId) {
		this.outsideHospitalId = outsideHospitalId;
	}

	public Integer getOutsideHosDocId() {
		return outsideHosDocId;
	}

	public void setOutsideHosDocId(Integer outsideHosDocId) {
		this.outsideHosDocId = outsideHosDocId;
	}

	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}
	
}

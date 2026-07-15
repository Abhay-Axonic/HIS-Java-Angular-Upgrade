package com.param.adt.admission.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.discharge.model.Discharge;
import com.param.adt.master.global.model.OrganizationMaster;
import com.param.adt.master.global.model.UnitMaster;
import com.param.adt.transfer.model.Transfer;
import com.param.adt.transfer.model.TransferRequest;
import com.param.billing.global.model.TPeriodicityServiceOrders;
import com.param.global.model.DoctorMaster;
import com.param.global.model.PatientRegistration;
import com.param.global.model.PatientSponsorDetails;
import com.param.global.model.SpecialityMaster;
import com.param.global.model.UnknownPatientRegistration;
import com.param.global.model.VisitTypeMaster;

@NamedQueries({

		

		@NamedQuery(name = "GET_ER_OTHER_ADMISSION_LIST", query = "SELECT admission.admissionId as admissionId, "
				+ "admission.tPatientId as patientId, " 
				+ "admission.patientId as patientId, "
				+ "admission.admissionNumber as admissionNumber, " 
				+ "admission.visitTypeId as visitTypeId, "
				+ "admission.doctorId as doctorId, " 
				+ "pr.tUhid as UHIDNumber, " 
				+ "pr.patientName as pFirstName, "
				+ "pr.genderId as genderId, " 
				+ "pr.age as age, " 
				+ "pr.ageFormat as ageFormat, "
				+ "gm.code as genderCode, " 
				+ "COALESCE(doc.firstName,'') as dFirstName, " 
				+ "COALESCE(doc.lastName,'') as dLastName, "
				+ "COALESCE(doc.middleName,'') as dMiddleName, " 
				+ "adl.admissionId as admissionId, "
				+ "to_char(adl.doa,'DD/MM/YYYY') as doa, " 
				+ "to_char(adl.pdd,'DD/MM/YYYY') as pdd, "
				+ "bm.bedNumber as bedNumber, " 
				+ "bm.floorId as floorId, " 
				+ "bm.bedId as bedId "
				+ "FROM Admission admission " 
				+ "INNER JOIN admission.unknownPatientRegistration pr "
				+ "INNER JOIN pr.genderMaster gm " 
				+ "INNER JOIN admission.doctorMaster doc "
				+ "INNER JOIN admission.admissionDetailsList adl " 
				+ "INNER JOIN adl.bedMaster bm "
				+ "WHERE admission.status='A' " 
				+ "AND admission.organizationId=:organizationId "
				+ "AND admission.unitId=:unitId " 
				+ "AND admission.visitTypeId=4 " 
				+ "AND adl.erBedTypeAllocation>0 "
				+ "AND adl.bedId>0 "),



		@NamedQuery(name = "GET_BED_RESERVATION_LIST_DETAILS", query = "SELECT admissionNote.admissionNoteId as admissionNoteId, "
				+ "admissionNote.visitTypeId as visitTypeId, " 
				+ "admissionNote.patientId as patientId, "
				+ "admissionNote.id as id, " 
				+ "admissionNote.reasonId as reasonId, "
				+ "admissionNote.requestToDoctorId as requestToDoctorId, "
				+ "admissionNote.treatingDoctorId as treatingDoctorId, "
				+ "admissionNote.treatingDoctorSpecialityId as treatingDoctorSpecialityId, "
				+ "admissionNote.doctorSpecialityId as doctorSpecialityId, "
				+ "admissionNote.isFlexiableDate as isFlexiableDate, "
				+ "admissionNote.admissionNoteNumber as admissionNoteNumber, " 
				+ "coalesce(admissionNote.depositAmount,0) as depositAmount, "
				+ "coalesce(admissionNote.depositPending,0) as depositPending, " 
				+ "coalesce(admissionNote.depositCollected ,0) as depositCollected, " 
				+ "coalesce(admissionNote.depositStatus,1) as depositStatus, " 
				+ "COALESCE(doctor.firstName,'') as dFirstName, "
				+ "COALESCE(doctor.middleName,'') as dMiddleName, " 
				+ "COALESCE(doctor.lastName,'') as dLastName, "
				+ "speciality.specialityName as specialityName, " 
				+ "visitType.visitTypeName as visitTypeName, "
				+ "patient.uhidNumber as UHIDNumber, " 
				+ "COALESCE(patient.firstName,'') as pFirstName, "
				+ "COALESCE(patient.middleName,'') as pMiddleName, " 
				+ "COALESCE(patient.lastName,'') as pLastName, "
				+ "COALESCE(patient.firstName,'') as firstName, "
				+ "COALESCE(patient.middleName,'') as middleName, " 
				+ "COALESCE(patient.lastName,'') as lastName, "
				+ "patient.registrationTypeId as registrationTypeId, "
				+ "patient.prefixId as prefixId, "
				+ "patient.mobileNumber as mobileNumber, "
				+ "patient.email as email, "
				+ "admissionNote.admissionTypeId as admissionTypeId, "
				+ "to_char(patient.dob,'DD/MM/YYYY') as dob, "
				+ "concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, patient.dob)),'M ') as age, "
				+ "gender.code as code, "
				+ "reason.reasonDesc as reasonDesc, " 
				+ "mapper.bedCategoryId as bedCategoryId, "
				+ "mapper.paymentEntitlementId as paymentEntitlementId, "
				+ "mapper.patientCategoryId as patientCategoryId, " 
				+ "to_char(mapper.doa,'DD/MM/YYYY') as doa, "
				+ "to_char(mapper.pdd,'DD/MM/YYYY') as pdd, " 
				+ "to_char(mapper.doa,'DD/MM/YYYY') as oldDoa, "
				+ "to_char(mapper.pdd,'DD/MM/YYYY') as oldPdd, "
				+ "bedCatMst.bedCategoryDesc as bedCategoryDesc, "
				+ "patCategoryMst.patientCategory as patientCategory, "
				+ "payEn.paymentEntitlementDesc as paymentEntitlementDesc, "
				+ "bedCatMst.billingBedCategoryId as billingBedCategoryId, "
				+ "billBed.billingBedDesc as billingBedDesc " 
				+ "FROM AdmissionNote as admissionNote "
				+ "INNER JOIN admissionNote.doctorMaster as doctor "
				+ "INNER JOIN admissionNote.visitTypeMaster as visitType "
				+ "INNER JOIN admissionNote.patientRegistration as patient "
				+ "INNER JOIN admissionNote.genderMaster as gender "
				+ "INNER JOIN admissionNote.reasonMaster as reason "
				+ "INNER JOIN admissionNote.specialityMaster as speciality "
				+ "INNER JOIN admissionNote.admissionPatientMappersList as mapper "
				+ "INNER JOIN mapper.patientCategoryMaster patCategoryMst "
				+ "INNER JOIN mapper.bedCategoryMaster bedCatMst "
				+ "INNER JOIN bedCatMst.billingBedCategoryMaster billBed " 
				+ "INNER JOIN mapper.paymentEntitlementMaster payEn "
				+ "WHERE admissionNote.admissionStatus='R' " 
				+ "AND mapper.activeStatus='R' " 
				+ "AND mapper.status='A' "
				+ "AND admissionNote.organizationId=:organizationId " 
				+ "AND admissionNote.unitId=:unitId"),
		
})

@NamedNativeQueries({
	
	
	
	@NamedNativeQuery(name = "GET_ADMITTED_PATIENT_LIST", 
	query ="SELECT "
			+ "  admission.admission_id as \"admissionId\", "
			+ "  admission.patient_id as \"patientId\", "
			+ "  admission.t_patient_id as \"tPatientId\", "
			+ "  admission.admission_number as \"admissionNumber\", "
			+ "  admission.visit_type_id as \"visitTypeId\", "
			+ "  admission.doctor_id as \"doctorId\", "
			+ "  admission.speciality_id as \"specialityId\", "
			+ "  (select wardMast.is_labor_ward from adt.m_ward_master wardMast "
			+ " where wardMast.ward_id=(select ward_id from adt.t_admission_details addet "  
			+ "  where addet.admission_id= admission.admission_id and status='A' order by 1 desc limit 1)) as \"isLaborWard\","
			+ "  (select obgyn_speciality_id from t_unit_application_billing_configuration appunit where appunit.obgyn_speciality_id=admission.speciality_id AND admission.status = 'A' AND admission.organization_id = :organizationId AND admission.unit_id = :unitId and appunit.unit_id =:unitId and appunit.organization_id = :organizationId) as \"obgynSpecialityId\","
			+ "  admission.delivery_flag  as \"deliveryFlag\", "
			+ "  sp.speciality_name as \"specialityName\", "
			+ "  pr.uhid_number as \"UHIDNumber\", "
			+ "	 pr.prefix_id as \"prefixId\", "
			+ "  prefix.prefix_code as \"prefixCode\", "
			+ "  pr.gender_id as \"genderId\", "
			+ "  gm.gender_code as \"genderCode\", "
			+ "  to_char(pr.dob, 'MM/DD/YYYY') as \"dob\", "
			+ "  concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))) as \"ageYear\", "
			+ "  concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
			+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"doa\", "
			+ "  to_char(adl.pdd, 'DD/MM/YYYY') as \"pdd\", "
			+ "  spd.payment_entitlement_id as \"paymentEntitlementId\", "
			+ "  spd.contract_id as \"contractId\", "
			+ "  spd.payee_id as \"payeeId\", "
			+ "  pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
			+ "  bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
			+ "  bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
			+ "  adl.admission_type_id as \"admissionTypeId\", "
			+ "  bedCatMst.bed_category_id as \"bedCategoryId\", "
			+ "  bm.floor_id as \"floorId\", "
			+ "  bm.bed_number as \"bedNumber\", "
			+ "  adl.bed_id as \"bedId\", "
			+ "  adl.ward_id as \"wardId\", "
			+ "  adl.room_id as \"roomId\", "
			+ "  ward.ward_name as \"wardName\", "
			+ "  adl.is_short_leave as \"isShortLeave\", "
			+ "  pr.patient_category_id as \"patientCategoryId\", "
			+ "  admission.concent as \"concent\", "
			+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"createdDate\", "
			+ "  to_char(adl.doa, 'HH:MI AM') as \"createdTime\", "
			+ "  pr.is_vip as \"isVip\", "
			+ "  (case when pr.is_vip='Y' then "
			+ "    coalesce(pr.alise_name,pr.first_name) "
			+ "    else "
			+ "    concat(coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name)) "
			+ "  end) as \"pFirstName\", "
			+ "  (case when (select total_score from adt.t_mews_mapper where admission_id = admission.admission_id and status='A') > 6 then "
			+ "  	1 "
			+ "   else "
			+ "     0 end) as \"isCriticalOrder\" "
			+ "FROM "
			+ "  adt.t_admission admission "
			+ "  INNER JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id "
			+ "  INNER JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
			+ "  INNER JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
			+ "  INNER JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id "
			+ "  INNER JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id "
			+ "  INNER JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id "
			+ "  INNER JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
			+ "  INNER JOIN  public.m_prefix_master prefix ON  prefix.prefix_id = pr.prefix_id "
			+ "  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 AND spd.status='A' "
			+ "  INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
			+ "  INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
			+ "WHERE "
			+ "  admission.status = 'A' "
			+ " AND adl.status='A' "
			+ " AND adl.nursing_acceptance = 'Y' "
			+ "  AND admission.organization_id = :organizationId "
			+ "  AND admission.unit_id = :unitId "
			+ " order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc "
		),
	
	@NamedNativeQuery(name = "GET_ADMITTED_PATIENT_LIST_NEW_BORN", 
	query ="SELECT "
			+ "  admission.admission_id as \"admissionId\", "
			+ "  admission.patient_id as \"patientId\", "
			+ "  admission.t_patient_id as \"tPatientId\", "
			+ "  admission.admission_number as \"admissionNumber\", "
			+ "  admission.visit_type_id as \"visitTypeId\", "
			+ "  admission.doctor_id as \"doctorId\", "
			+ "  admission.speciality_id as \"specialityId\", "
			+ "  (select wardMast.is_labor_ward from adt.m_ward_master wardMast "
			+ " where wardMast.ward_id=(select ward_id from adt.t_admission_details addet "  
			+ "  where addet.admission_id= admission.admission_id and status='A' and wardMast.is_labor_ward='Y' order by 1 desc limit 1)) as \"isLaborWard\","
			+ "  (select obgyn_speciality_id from t_unit_application_billing_configuration appunit where appunit.obgyn_speciality_id=admission.speciality_id AND admission.status = 'A' AND admission.organization_id = :organizationId AND admission.unit_id = :unitId and appunit.unit_id =:unitId and appunit.organization_id = :organizationId) as \"obgynSpecialityId\","
			+ "  admission.delivery_flag  as \"deliveryFlag\", "
			+ "  sp.speciality_name as \"specialityName\", "
			+ "  pr.uhid_number as \"UHIDNumber\", "
			+ "	 pr.prefix_id as \"prefixId\", "
			+ "  prefix.prefix_code as \"prefixCode\", "
			+ "  pr.gender_id as \"genderId\", "
			+ "  gm.gender_code as \"genderCode\", "
			+ "  to_char(pr.dob, 'MM/DD/YYYY') as \"dob\", "
			+ "  concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))) as \"ageYear\", "
			+ "  concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
			+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"doa\", "
			+ "  to_char(adl.pdd, 'DD/MM/YYYY') as \"pdd\", "
			+ "  spd.payment_entitlement_id as \"paymentEntitlementId\", "
			+ "  spd.contract_id as \"contractId\", "
			+ "  spd.payee_id as \"payeeId\", "
			+ "  pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
			+ "  bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
			+ "  bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
			+ "  adl.admission_type_id as \"admissionTypeId\", "
			+ "  bedCatMst.bed_category_id as \"bedCategoryId\", "
			+ "  bm.floor_id as \"floorId\", "
			+ "  bm.bed_number as \"bedNumber\", "
			+ "  adl.bed_id as \"bedId\", "
			+ "  adl.ward_id as \"wardId\", "
			+ "  adl.room_id as \"roomId\", "
			+ "  ward.ward_name as \"wardName\", "
			+ "  adl.is_short_leave as \"isShortLeave\", "
			+ "  pr.patient_category_id as \"patientCategoryId\", "
			+ "  admission.concent as \"concent\", "
			+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"createdDate\", "
			+ "  to_char(adl.doa, 'HH:MI AM') as \"createdTime\", "
			+ "  pr.is_vip as \"isVip\", "
			+ "  (case when pr.is_vip='Y' then "
			+ "    coalesce(pr.alise_name,pr.first_name) "
			+ "    else "
			+ "    concat(coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name)) "
			+ "  end) as \"pFirstName\", "
			+ "  (case when (select total_score from adt.t_mews_mapper where admission_id = admission.admission_id and status='A') > 6 then "
			+ "  	1 "
			+ "   else "
			+ "     0 end) as \"isCriticalOrder\" "
			+ "FROM "
			+ "  adt.t_admission admission "
			+ "  INNER JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id "
			+ "  INNER JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
			+ "  INNER JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
			+ "  INNER JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id "
			+ "  INNER JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id "
			+ "  INNER JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id "
			+ "  INNER JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
			+ "  INNER JOIN  public.m_prefix_master prefix ON  prefix.prefix_id = pr.prefix_id "
			+ "  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 AND spd.status='A' "
			+ "  INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
			+ "  INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
			+ "WHERE "
			+ "  admission.status = 'A' "
			+ " AND adl.status='A' "
			+ " AND adl.nursing_acceptance = 'Y' "
			+ "  AND admission.organization_id = :organizationId "
			+ "  AND admission.unit_id = :unitId "
			+ " AND admission.delivery_flag = 1"
			+ " AND pr.gender_id= 2"
			+ " order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc "
		),
		@NamedNativeQuery(name = "GET_ADMITTED_PATIENT_LIST_DOCTOR_WISE_SERACH",
				query ="SELECT "
						+ "  admission.admission_id as \"admissionId\", "
						+ " (select coalesce(bm.bill_status_id,0) from billing.m_billing_master bm where bm.is_interim_or_final_bill = 2 and  bm.admission_id =admission.admission_id and bm.bill_cancelled = 'N' order by bm.billing_master_id limit 1  ) as \"billStatusId\" ,"
						+ "  admission.patient_id as \"patientId\", "
						+ "  admission.t_patient_id as \"tPatientId\", "
						+ "  admission.admission_number as \"admissionNumber\", "
						+ "  admission.visit_type_id as \"visitTypeId\", "
						+ "  admission.doctor_id as \"doctorId\", "
						+ "  admission.speciality_id as \"specialityId\", "
						+ "  admission.is_final_bill as \"isFinalBill\", "
						+ "  sp.speciality_name as \"specialityName\", "
						+ "	 pr.prefix_id as \"prefixId\", "
						+ "  pr.uhid_number as \"UHIDNumber\", "
						+ "  pr.is_vip as \"isVip\", "
						+ "  (case when pr.is_vip='Y' then "
						+ "  coalesce(pr.alise_name,pr.first_name) "
						+ "  else "
						+ "  concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name)) "
						+ "  end) as \"pFirstName\", "
						+ "  (case when pr.is_vip='Y' then "
						+ "  coalesce(pr.alise_name,pr.first_name) "
						+ "  else "
						+ "  concat(coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name)) "
						+ "  end) as \"firstName\", "
						+ "  pr.gender_id as \"genderId\", "
						+ "  pr.mpshah_patient_id as \"mpshahPatientId\", "
						+ "  gm.gender_code as \"genderCode\", "
						+ "  to_char(pr.dob, 'MM/DD/YYYY') as \"dob\", "
						+ "  to_char(pr.dob, 'DD/MM/YYYY') as \"dobNeuron\", "
						+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))) as \"ageYear\", "
						+ "  concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
						+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"doa\", "
						+ "  to_char(adl.pdd, 'DD/MM/YYYY hh:mm') as \"pdd\", "
						+ "  adl.pdd as \"pddNew\", "
						+ "  spd.payment_entitlement_id as \"paymentEntitlementId\", "
						+ "  spd.contract_id as \"contractId\", "
						+ "  spd.payee_id as \"payeeId\", "
						+ "  pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
						+ "  bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
						+ "  adl.billing_bed_category_id as \"billingBedCategoryId\", "
						+ "  bedCatMst.bed_category_id as \"bedCategoryId\", "
						+ "  bm.floor_id as \"floorId\", "
						+ "  bm.bed_number as \"bedNumber\", "
						+ "  adl.bed_id as \"bedId\", "
						+ "  adl.ward_id as \"wardId\", "
						+ "  adl.room_id as \"roomId\", "
						+ "  ward.ward_name as \"wardName\","
						+ "  floor.floor_name as \"floorName\", "
						+ "  adl.is_short_leave as \"isShortLeave\", "
						+ "  pr.patient_category_id as \"patientCategoryId\", "
						+ "  pr.uuid as \"uuid\", "
						+ "  pdl.address as \"address\", "
						+ "  cm.city_name as \"cityName\", "
						+ "  admission.concent as \"concent\", "
						+ "  disc.discharge_id as \"dischargeId\", "
						+ "  usermst.user_name as \"userName\", "
						+ "  disc.discharge_status_id as \"dischargeStatusId\", "
						+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"createdDate\", "
						+ "  to_char(adl.doa, 'HH:MI AM') as \"createdTime\", "
						+ "  medleg.medico_legal_id as \"medicoLegalId\", "
						+ " pr.email as \"email\","
						+ " pr.image_path as \"imagePath\","
						+ " bedCatMst.is_icu as \"isIcuu\", "
						+ "  (case when (select total_score from adt.t_mews_mapper where admission_id = admission.admission_id and status='A') > 6 then "
						+ "  	1 "
						+ "   else "
						+ "     0 end) as \"isCriticalOrder\", "
						+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\" "

						+ "FROM "
						+ "  adt.t_admission admission "
						+ "  INNER JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id "
						+ "  INNER JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
						+ "  left join m_prefix_master pre on pre.prefix_id = pr.prefix_id "
						+ "  INNER JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
						+ "  INNER JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id "
						+ "  INNER JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id "
						+ "  INNER JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id "
						+ "  INNER JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
						+ "  INNER JOIN adt.m_floor_master floor on ward.floor_id = floor.floor_id "
						+ "  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 and spd.status='A' "
						+ "  left join patient.t_patient_details pdl on admission.patient_id = pdl.patient_id "
						+ "  LEFT JOIN adt.t_discharge disc on admission.admission_id = disc.admission_id and disc.status = 'A' "
						+ "  left join adt.t_medico_legal medleg on admission.admission_id = medleg.admission_id "
						+ "  INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
						+ "  INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
						+ "  LEFT JOIN public.m_user_master usermst on adl.created_by = usermst.user_id "
						+ "  LEFT JOIN public.m_city_master cm on cm.city_id = pdl.city_id "
						+ "WHERE "
						+ "  admission.status = 'A' "
						+ "  AND adl.status='A' "
						+ "  AND adl.nursing_acceptance = 'Y' "
						+ "  AND admission.organization_id = :organizationId "
						+ "  AND admission.unit_id = :unitId "
						+ "  AND admission.doctor_id=:docId "
						+ "  AND admission.visit_type_id =:visitTypeId "
						//+ " order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc "
		),

	@NamedNativeQuery(name = "GET_ADMITTED_PATIENT_LIST_DOCTOR_WISE",
	query ="SELECT "
			+ "  admission.admission_id as \"admissionId\", "
			+ " (select coalesce(bm.bill_status_id,0) from billing.m_billing_master bm where bm.is_interim_or_final_bill = 2 and  bm.admission_id =admission.admission_id and bm.bill_cancelled = 'N' order by bm.billing_master_id limit 1  ) as \"billStatusId\" ,"
			+ "  admission.patient_id as \"patientId\", "
			+ "  admission.t_patient_id as \"tPatientId\", "
			+ "  admission.admission_number as \"admissionNumber\", "
			+ "  admission.visit_type_id as \"visitTypeId\", "
			+ "  admission.doctor_id as \"doctorId\", "
			+ "  admission.speciality_id as \"specialityId\", "
			+ "  admission.is_final_bill as \"isFinalBill\", "
			+ "  sp.speciality_name as \"specialityName\", "
			+ "	 pr.prefix_id as \"prefixId\", "
			+ "  pr.uhid_number as \"UHIDNumber\", "
			+ "  pr.is_vip as \"isVip\", "
			+ "  (case when pr.is_vip='Y' then "
			+ "  coalesce(pr.alise_name,pr.first_name) "
			+ "  else "
			+ "  concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name)) "
			+ "  end) as \"pFirstName\", "
			+ "  (case when pr.is_vip='Y' then "
			+ "  coalesce(pr.alise_name,pr.first_name) "
			+ "  else "
			+ "  concat(coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name)) "
			+ "  end) as \"firstName\", "
			+ "  pr.gender_id as \"genderId\", "
			+ "  pr.mpshah_patient_id as \"mpshahPatientId\", "
			+ "  gm.gender_code as \"genderCode\", "
			+ "  to_char(pr.dob, 'MM/DD/YYYY') as \"dob\", "
			+ "  to_char(pr.dob, 'DD/MM/YYYY') as \"dobNeuron\", "
			+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))) as \"ageYear\", "
			+ "  concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
			+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"doa\", "
			+ "  to_char(adl.pdd, 'DD/MM/YYYY hh:mm') as \"pdd\", "
			+ "  adl.pdd as \"pddNew\", "
			+ "  spd.payment_entitlement_id as \"paymentEntitlementId\", "
			+ "  spd.contract_id as \"contractId\", "
			+ "  spd.payee_id as \"payeeId\", "
			+ "  pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
			+ "  bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
			+ "  adl.billing_bed_category_id as \"billingBedCategoryId\", "
			+ "  bedCatMst.bed_category_id as \"bedCategoryId\", "
			+ "  bm.floor_id as \"floorId\", "
			+ "  bm.bed_number as \"bedNumber\", "
			+ "  adl.bed_id as \"bedId\", "
			+ "  adl.ward_id as \"wardId\", "
			+ "  adl.room_id as \"roomId\", "
			+ "  ward.ward_name as \"wardName\","
			+ "  floor.floor_name as \"floorName\", "
			+ "  adl.is_short_leave as \"isShortLeave\", "
			+ "  pr.patient_category_id as \"patientCategoryId\", "
			+ "  pr.uuid as \"uuid\", "
			+ "  pdl.address as \"address\", "
			+ "  cm.city_name as \"cityName\", "
			+ "  admission.concent as \"concent\", "
			+ "  disc.discharge_id as \"dischargeId\", "
			+ "  usermst.user_name as \"userName\", "
			+ "  disc.discharge_status_id as \"dischargeStatusId\", "
			+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"createdDate\", "
			+ "  to_char(adl.doa, 'HH:MI AM') as \"createdTime\", "
			+ "  medleg.medico_legal_id as \"medicoLegalId\", "
			+ " pr.email as \"email\","
			+ " pr.image_path as \"imagePath\","
            + " bedCatMst.is_icu as \"isIcuu\", "
			+ "  (case when (select total_score from adt.t_mews_mapper where admission_id = admission.admission_id and status='A') > 6 then "
			+ "  	1 "
			+ "   else "
			+ "     0 end) as \"isCriticalOrder\", "
			+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\" "

			+ "FROM "
			+ "  adt.t_admission admission "
			+ "  INNER JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id "
			+ "  INNER JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
			+ "  left join m_prefix_master pre on pre.prefix_id = pr.prefix_id "
			+ "  INNER JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
			+ "  INNER JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id "
			+ "  INNER JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id "
			+ "  INNER JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id "
			+ "  INNER JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
			+ "  INNER JOIN adt.m_floor_master floor on ward.floor_id = floor.floor_id "
			+ "  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 and spd.status='A' "
			+ "  left join patient.t_patient_details pdl on admission.patient_id = pdl.patient_id "
			+ "  LEFT JOIN adt.t_discharge disc on admission.admission_id = disc.admission_id and disc.status = 'A' "
			+ "  left join adt.t_medico_legal medleg on admission.admission_id = medleg.admission_id "
			+ "  INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
			+ "  INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
			+ "  LEFT JOIN public.m_user_master usermst on adl.created_by = usermst.user_id "
			+ "  LEFT JOIN public.m_city_master cm on cm.city_id = pdl.city_id "
			+ "WHERE "
			+ "  admission.status = 'A' "
			+ "  AND adl.status='A' "
			+ "  AND adl.nursing_acceptance = 'Y' "
			+ "  AND admission.organization_id = :organizationId "
			+ "  AND admission.unit_id = :unitId "
			+ "  AND admission.doctor_id=:docId "
			+ "  AND admission.visit_type_id =:visitTypeId "
			+ " order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc " 
		),
	//
	@NamedNativeQuery(name = "GET_ADMITTED_PATIENT_LIST_NEPHROLOGY_DOCTOR_WISE", 
	query ="SELECT "
			+ "  admission.admission_id as \"admissionId\", "
			+ " (select coalesce(bm.bill_status_id,0) from billing.m_billing_master bm where bm.is_interim_or_final_bill = 2 and  bm.admission_id =admission.admission_id and bm.bill_cancelled = 'N' order by bm.billing_master_id limit 1  ) as \"billStatusId\" ,"
			+ "  admission.patient_id as \"patientId\", "
			+ "  admission.t_patient_id as \"tPatientId\", "
			+ "  admission.admission_number as \"admissionNumber\", "
			+ "  admission.visit_type_id as \"visitTypeId\", "
			+ "  admission.doctor_id as \"doctorId\", "
			+ "  admission.speciality_id as \"specialityId\", "
			+ "  sp.speciality_name as \"specialityName\", "
			+ "	 pr.prefix_id as \"prefixId\", "
			+ "  pr.uhid_number as \"UHIDNumber\", "
			+ "  concat(coalesce(pre.prefix_code,''),' ',COALESCE(pr.first_name,''),' ',COALESCE(pr.middle_name,''),' ',COALESCE(pr.last_name,'')) as \"pFirstName\", "
			+ "  pr.gender_id as \"genderId\", "
			+ "  pr.mpshah_patient_id as \"mpshahPatientId\", "
			+ "  gm.gender_code as \"genderCode\", "
			+ "  to_char(pr.dob, 'MM/DD/YYYY') as \"dob\", "
			+ "  concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
			+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"doa\", "
			+ "  to_char(adl.pdd, 'DD/MM/YYYY') as \"pdd\", "
			+ "  spd.payment_entitlement_id as \"paymentEntitlementId\", "
			+ "  spd.contract_id as \"contractId\", "
			+ "  spd.payee_id as \"payeeId\", "
			+ "  pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
			+ "  bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
			+ "  bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
			+ "  bedCatMst.bed_category_id as \"bedCategoryId\", "
			+ "  bm.floor_id as \"floorId\", "
			+ "  bm.bed_number as \"bedNumber\", "
			+ "  adl.bed_id as \"bedId\", "
			+ "  adl.ward_id as \"wardId\", "
			+ "  adl.room_id as \"roomId\", "
			+ "  ward.ward_name as \"wardName\", "
			+ "  adl.is_short_leave as \"isShortLeave\", "
			+ "  pr.patient_category_id as \"patientCategoryId\", "
			+ "  admission.concent as \"concent\", "
			+ "  disc.discharge_id as \"dischargeId\", "
			+ "  disc.discharge_status_id as \"dischargeStatusId\", "
			+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"createdDate\", "
			+ "  to_char(adl.doa, 'HH:MI AM') as \"createdTime\", "
			+ "medleg.medico_legal_id as \"medicoLegalId\", " 
			+ "  ( "
			+ "			with labct as (select "
			+ "	count(ord_details.order_details_id) as ct "
			+ "from "
			+ "	public.t_order_master ord_mst "
			+ "    inner join public.t_order_details ord_details on ord_details.order_id = ord_mst.order_id "
			+ " inner join service.t_unit_service_master_details service_dtls on "
			+ "	  ord_details.service_id = service_dtls.service_master_id and service_dtls.status = 'A' "
			+ "	  and service_dtls.is_panel = 'Y' "
			+ " inner join lab.t_panel_details pd on "
			+ "	ord_details.service_id = pd.service_id "
			+ "	and pd.is_deleted = 'N' "
			+ "	and pd.test_id not in (select coalesce(test_id,0) from lab.t_lab_sample_dtls lsd where lsd.profile_id =pd.panel_id and lsd.order_details_id = ord_details.order_details_id) "
			+ " left join lab.t_lab_sample_dtls smpdtls on "
			+ "	 ord_details.order_details_id = smpdtls.order_details_id "
			+ "	 and smpdtls.sample_barcode is null "
			+ "where "
			+ "	 ord_mst.org_unit_id =:unitId "
			+ "	 and ord_mst.patient_id =  admission.patient_id "
			+ "     and ord_mst.visit_type_id = admission.visit_type_id "
			+ "     and ord_mst.admission_id =  admission.admission_id "
			+ "     and ord_details.ord_cancelled = 'N' "
			+"      and ord_details.is_from_addservice = 'N' "
			+"      and ord_details.debit_note_service = false  "
			+ "union "
			+ " select "
			+ "      count(*) as ct "
			+ "    from "
			+ "      public.t_order_master ord_mst "
			+ "      inner join public.t_order_details ord_details on ord_details.order_id = ord_mst.order_id "
			+ "      inner join service.t_unit_service_master_details service_dtls on "
			+ "	  ord_details.service_id = service_dtls.service_master_id and service_dtls.status = 'A' "
			+ "      inner join public.t_lab_configuration lab_config on lab_config.unit_id  = :unitId "
			+ "    where "
			+ "      ord_mst.patient_id =  admission.patient_id "
			+ "      and ord_mst.visit_type_id = admission.visit_type_id "
			+ "      and ord_mst.admission_id =  admission.admission_id "
			+ "      and ord_details.service_rendered = 0 "
			+ "      and ord_details.ord_cancelled = 'N' "
			+"       and ord_details.is_from_addservice = 'N' "
			+"       and ord_details.debit_note_service = false  "
			+ "      and ord_details.service_rendering_deptid in (lab_config.biochemistry_id,lab_config.hematology_id,lab_config.microbiology_id,lab_config.serology_id,lab_config.clinical_pathology_id) "
			+ "  ) select cast(sum(a.ct) as text) as \"labOrder\" from labct a"
			+ "  ) "
			+ "FROM "
			+ "  adt.t_admission admission "
			+ "  INNER JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id "
			+ "  INNER JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
			+ "  left join m_prefix_master pre on pre.prefix_id = pr.prefix_id "
			+ "  INNER JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
			+ "  INNER JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id "
			+ "  INNER JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id "
			+ "  INNER JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id "
			+ "  INNER JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
			+ "  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 and spd.status='A' "
			+ "  LEFT JOIN adt.t_discharge disc on admission.admission_id = disc.admission_id and disc.status = 'A' "
			+ "  left join adt.t_medico_legal medleg on admission.admission_id = medleg.admission_id "
			+ "  INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
			+ "  INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
			+ "WHERE "
			+ "  admission.status = 'A' "
			+ "  AND adl.status='A' "
			+ "  AND adl.nursing_acceptance = 'Y' "
			+ "  AND admission.organization_id = :organizationId "
			+ "  AND admission.unit_id = :unitId "
			+ "  AND admission.doctor_id=:docId "
			+ "  AND  admission.speciality_id=(select hemodialysis_speciality_id from t_unit_application_billing_configuration where unit_id = :unitId and organization_id = :organizationId and status='A')  "
			+ " order by admission.admission_id desc " 
		),
	@NamedNativeQuery(name = "GET_ADMITTED_PATIENT_LIST_DOCTOR_WISE_FOR_EMR", 
	query ="SELECT "
			+ "  admission.admission_id as \"admissionId\", "
			+ "  admission.patient_id as \"patientId\", "
			+ "  admission.t_patient_id as \"tPatientId\", "
			+ "  admission.admission_number as \"admissionNumber\", "
			+ "  admission.visit_type_id as \"visitTypeId\", "
			+ "  admission.doctor_id as \"doctorId\", "
			+ "  admission.speciality_id as \"specialityId\", "
			+ "  sp.speciality_name as \"specialityName\", "
			+ "  pr.uhid_number as \"UHIDNumber\", "
			+ "  concat(coalesce(pre.prefix_code,''),' ',COALESCE(pr.first_name,''),' ',COALESCE(pr.middle_name,''),' ',COALESCE(pr.last_name,'')) as \"pFirstName\", "
			+ "  pr.gender_id as \"genderId\", "
			+ "  gm.gender_code as \"genderCode\", "
			+ "  to_char(pr.dob, 'MM/DD/YYYY') as \"dob\", "
			+ "  concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)),'Y ') as \"ageString\", "
			+ "  concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
			+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"doa\", "
			+ "  to_char(adl.pdd, 'DD/MM/YYYY') as \"pdd\", "
			+ "  spd.payment_entitlement_id as \"paymentEntitlementId\", "
			+ "  bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
			+ "  bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
			+ "  bedCatMst.bed_category_id as \"bedCategoryId\", "
			+ "  bm.floor_id as \"floorId\", "
			+ "  bm.bed_number as \"bedNumber\", "
			+ "  adl.bed_id as \"bedId\", "
			+ "  adl.ward_id as \"wardId\", "
			+ "  adl.room_id as \"roomId\", "
			+ "  ward.ward_name as \"wardName\", "
			+ "  pr.patient_category_id as \"patientCategoryId\", "
			+ "(select dia.diagnosis_desc "
			+ "from patient.t_patient_diagnosis_appo_mapper padam "
			+ "inner join emr.m_diagnosis_master dia on "
			+ "dia.diagnosis_id = padam.diagnosis_id "
			+ "where admission_id = admission.admission_id "
			+ " order by 1 desc limit 1) as \"primaryDiagnosis\" "
			+ "FROM "
			+ "  adt.t_admission admission "
			+ "  INNER JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id "
			+ "  INNER JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
			+ "  left join m_prefix_master pre on pre.prefix_id = pr.prefix_id "
			+ "  INNER JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
			+ "  INNER JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id "
			+ "  INNER JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id "
			+ "  INNER JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id "
			+ "  INNER JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
			+ "  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 and spd.status='A' "
			+ "  INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
			+ "  INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
			+ "  WHERE  admission.status = 'A' "
			+ "  AND adl.status='A' "
			+ " AND adl.nursing_acceptance = 'Y' "
			+ "  AND admission.organization_id = :organizationId "
			+ "  AND admission.unit_id = :unitId "
			+ "  AND admission.doctor_id=:docId "
		),
	
	
	
	@NamedNativeQuery(name = "GET_ADMITTED_PATIENT_LIST_FOR_DISCHARGE", 
	query = 
				"SELECT admission.admission_id as \"admissionId\", "
				+ "admission.patient_id as \"patientId\", "
				+ "admission.t_patient_id as \"tPatientId\", "
				+ "admission.admission_number as \"admissionNumber\", "
				+ "admission.visit_type_id as \"visitTypeId\", " 
				+ "admission.doctor_id as \"doctorId\", "
				+ "admission.speciality_id as \"specialityId\", " 
				+ "sp.speciality_name as \"specialityName\", "
				+ "pr.uhid_number as \"UHIDNumber\", " 
				+ "  pr.is_vip as \"isVip\", "
				+ "  (case when (select total_score from adt.t_mews_mapper where admission_id = admission.admission_id and status='A') > 6 then "
				+ "  	1 "
				+ "   else "
				+ "     0 end) as \"isCriticalOrder\", "
				+ "concat(COALESCE(pr.first_name,''),' ',COALESCE(pr.middle_name,''),' ',COALESCE(pr.last_name,'')) as \"pFirstName\" , " 
				+ "pr.gender_id as \"genderId\", " 
				+ "gm.gender_code as \"genderCode\", "
				+ "to_char(pr.dob,'MM/DD/YYYY') as \"dob\", " 
				+ "  concat(COALESCE(doc.first_name,''),' ', COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
				+ "to_char(adl.doa,'DD/MM/YYYY') as \"doa\", " 
				+ "to_char(adl.pdd,'DD/MM/YYYY') as \"pdd\", "
				+ "to_char(adl.pdd,'HH24:MI') as \"pddTime\", "
				+ "spd.payment_entitlement_id as \"paymentEntitlementId\", "
				+ "pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
				+ "usermst.user_name as \"userName\", "
				+ "bedCatMst.bed_category_desc as \"bedCategoryDesc\", " 
				+ "bm.bed_number as \"bedNumber\", "
				+ "adl.bed_id as \"bedId\", "
				+ "adl.ward_id as \"wardId\","
				+ "adl.room_id as \"roomId\","
				+ "ward.ward_name as \"wardName\", "
                + " prefxmstr.prefix_desc as \"prefixDesc\",pr.image_path as \"imagePath\",  "
                + " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\" "

				+ "FROM adt.t_admission admission " 
				+ "INNER JOIN patient.t_patient_registration pr on admission.patient_id=pr.patient_id "
				+ "INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id " 
                + "left join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
				+ "INNER JOIN doctor.m_doctor_master doc on admission.doctor_id=doc.doctor_id "
				+ "INNER JOIN doctor.m_speciality_master sp on admission.speciality_id=sp.speciality_id " 
				+ "INNER JOIN adt.t_admission_details adl on adl.admission_id=admission.admission_id "
				+ "INNER JOIN adt.m_bed_master bm on adl.bed_id=bm.bed_id "
				+ "INNER JOIN adt.m_ward_master ward on bm.ward_id=ward.ward_id "
				+ "INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 and spd.status='A' "
				+ "INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
				+ "INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id " 
				+ "INNER JOIN public.m_user_master usermst on adl.created_by = usermst.user_id  "
				+ "WHERE admission.status='A' "
				+ "AND adl.is_discharge='N' "
				+ "AND adl.pdd<=:pdd "
				+ "AND adl.status='A' "
				+ "AND admission.organization_id=:organizationId " 
				+ "AND admission.unit_id=:unitId "
				+ " AND adl.nursing_acceptance = 'Y'"
				+ " AND admission.doctor_id=:doctorId "
				),

		@NamedNativeQuery(name = "GET_ER_BED_TYPE_ALLOCATION_LIST", query = "SELECT ad.admission_id as \"admissionId\",  "
				+ "ad.patient_id as \"patientId\", ast.speciality_triage as \"specialityTriage\",ast.triage_details as \"triageDetails\", ast.speciality_triage_id as \"specialityTriageId\", "
				+ "ad.t_patient_id as \"tPatientId\", " 
				+ "ad.visit_type_id as \"visitTypeId\", "
				+ "ad.doctor_id as \"doctorId\", " 
				+ "gn.gender_code as \"genderName\", "
				+ "pt.uhid_number as \"UHIDNumber\", "
				+ "concat(coalesce(pre.prefix_code,''),' ',COALESCE(pt.first_name,''),' ',COALESCE(pt.middle_name,''),' ',COALESCE(pt.last_name,'')) as \"patientName\", "
				+ "pt.gender_id as \"genderId\", " 
				+ "to_char(pt.dob,'DD/MM/YYYY') as \"dob\", "
				+ "concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pt.dob)), 'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pt.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pt.dob)),'D ' ) as \"ageString\", "
				+ "adtails.admission_details_id as \"admissionDetailsId\", "
				+ "to_char(ad.created_date,'dd-MM-yyyy HH:mi:ss') as \"createdDate\", "
				+ "to_char(ad.created_date,'DD/MM/YYYY HH:mi:ss') as \"encounterDate\", "
				+ "ad.admission_number as \"admissionNumber\", " 
				+ "ad.created_by as \"createdBy\", "
				+ "pri.priority_desc as \"priorityDesc\" "
				+ "FROM adt.t_admission  ad "
				+ "INNER JOIN patient.t_patient_registration AS pt ON ad.patient_id = pt.patient_id "
				+ "INNER JOIN public.m_gender_master gn on pt.gender_id = gn.gender_id "
				+ "left join m_prefix_master pre on pre.prefix_id = pt.prefix_id "
				+ "INNER JOIN adt.t_admission_details adtails on ad.admission_id = adtails.admission_id "
				+ "LEFT JOIN adt.t_medico_legal medi on ad.admission_id=medi.admission_id "
				+ "LEFT JOIN adt.m_priority_master pri on medi.priority_id=pri.priority_id "
				+ " LEFT JOIN adt.speciality_triage ast on ast.admission_details_id=adtails.admission_details_id "
				+ "WHERE ad.visit_type_id=4 " 
				+ "AND ad.organization_id=:orgId " 
				+ "AND ad.unit_id=:unitId "
				+ "AND adtails.admission_type_id=4 "
				+ "AND coalesce(adtails.er_bed_type_allocation,0) = 0 "
				+ " ORDER BY ad.admission_id desc "
				),

		// ------------------------------------------------------------------
		@NamedNativeQuery(name = "GET_ER_ADMISSION_LIST", query = "select admission.admission_id as \"admissionId\", "
				+ "admissionDetails.admission_details_id as \"admissionDetailsId\", "
				+ "admission.patient_id as \"patientId\", " 
				+ "admission.t_patient_id as \"tPatientId\", "
				+ "admission.admission_number as \"admissionNumber\", " 
				+ "admission.doctor_id as \"doctorId\", "
				+ "to_char(admissionDetails.pdd,'dd/mm/yyyy')  as \"pdd\", "
//				+ "to_char(admissionDetails.pdd,'HH24:MI') as \"pddTime\", "
				+" to_char(admissionDetails.admission_date_time + interval '4 hours', 'DD/MM/YYYY HH24:MI') as \"pddTime\", "
				+ "bedMaster.bed_number as \"bedNumber\", "
				+ "usermst.user_name as \"userName\", " 
				+ "patient.uhid_number as \"UHIDNumber\", "
				+ "concat(COALESCE(patient.first_name,''),' ',COALESCE(patient.middle_name,''),' ',COALESCE(patient.last_name,'')) as \"patientName\", "
				+ "to_char(patient.dob,'DD/MM/YYYY') as \"dob\", " 
				+ "gender.gender_code as \"genderName\", "
				+ "oum.number_of_hours as \"noOfHours\", "
				+ "bedMaster.bed_id as \"bedId\" ,"
				+" prefxmstr.prefix_code as \"prefixDesc\",patient.image_path as \"imagePath\" ,"
		        + " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP,patient.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, patient.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, patient.dob)),'D ') as \"ageStr\" "

				+ "FROM adt.t_admission admission "
				+ "inner join patient.t_patient_registration patient on admission.patient_id=patient.patient_id "
				+ "inner join public.m_gender_master gender on patient.gender_id=gender.gender_id "
				+ "inner join adt.t_admission_details admissionDetails on admission.admission_id=admissionDetails.admission_id "
				+ "left join adt.m_bed_master bedMaster on admissionDetails.bed_id=bedMaster.bed_id "
				+ "left join adt.m_bed_category_master bc on bedMaster.bed_category_id=bc.bed_category_id "
				+ "left join public.m_occupancy_unit_master oum on bc.occupancy_unit_id=oum.occupancy_unit_id "
				+ "left join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = patient.prefix_id "
				+ " INNER JOIN public.m_user_master usermst on admissionDetails.created_by = usermst.user_id "
				+ "where admission.status='A' "
				+ "and admission.visit_type_id=4 "
				+ "and admissionDetails.er_bed_type_allocation>0 " + "and admission.patient_id > 0 "
				+ "AND admission.organization_id=:orgId " 
				+ "AND admission.unit_id=:unitId " 
				+ "AND admissionDetails.is_discharge='N' "
				),
		
		@NamedNativeQuery(name="GET_ER_PATIENT_LIST", query="select admission.admission_id as \"admissionId\", "
				+ "admission.patient_id as \"patientId\", "
				+ "admission.t_patient_id as \"tPatientId\", "
				+ "concat(coalesce(pre.prefix_code,''),' ',COALESCE(pat.first_name,''),' ',COALESCE(pat.middle_name,''),' ',COALESCE(pat.last_name,'')) as \"patientName\", "
				+ "pat.uhid_number as \"UHIDNumber\","
				+  "pat.patient_category_id as \"patientCategoryId\" "
				+" from adt.t_admission admission "
				+" inner join patient.t_patient_registration pat on admission.patient_id=pat.patient_id " 
				+ "  left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
				+" inner join adt.t_admission_details admdtls on admission.admission_id=admdtls.admission_id " 
				+" WHERE admdtls.er_bed_type_allocation>0 "
				+" AND (LOWER(concat(pat.first_name,' ',pat.middle_name,' ',pat.last_name)) ILIKE :firstName OR LOWER(pat.uhid_number) ILIKE :firstName) "
				+" AND admission.organization_id=:orgId "
				+" AND admission.unit_id=:unitId "),
		
		@NamedNativeQuery(name = "GET_ER_ADMISSION_LIST_BY_MUL_CRI", query = "select admission.admission_id as \"admissionId\", "
				+ "admissionDetails.admission_details_id as \"admissionDetailsId\", "
				+ "admission.patient_id as \"patientId\", " 
				+ "admission.t_patient_id as \"tPatientId\", "
				+ "admission.admission_number as \"admissionNumber\", " 
				+ "admission.doctor_id as \"doctorId\", "
				+ "concat(COALESCE(doctor.first_name,''),' ',COALESCE(doctor.middle_name,''),' ',COALESCE(doctor.last_name,'')) as \"dFirstName\", "
				+ "to_char(admissionDetails.pdd,'dd/mm/yyyy HH:mm:ss')  as \"pdd\", "
				+ "bedMaster.bed_number as \"bedNumber\", " 
				+ "patient.uhid_number as \"UHIDNumber\", "
				+ "concat(coalesce(pre.prefix_code,''),' ',COALESCE(patient.first_name,''),' ',COALESCE(patient.middle_name,''),' ',COALESCE(patient.last_name,'')) as \"patientName\", "
				+ "to_char(patient.dob,'DD/MM/YYYY') as \"dob\", " 
				+ "gender.gender_code as \"genderName\", "
				+ "bedMaster.bed_id as \"bedId\" " 
				+ "FROM adt.t_admission admission "
				+ "inner join patient.t_patient_registration patient on admission.patient_id=patient.patient_id "
				+ "inner join public.m_gender_master gender on patient.gender_id=gender.gender_id "
				+ "  left join m_prefix_master pre on pre.prefix_id = patient.prefix_id "
				+ "inner join doctor.m_doctor_master doctor on admission.doctor_id=doctor.doctor_id "
				+ "inner join adt.t_admission_details admissionDetails on admission.admission_id=admissionDetails.admission_id "
				+ "left join adt.m_bed_master bedMaster on admissionDetails.bed_id=bedMaster.bed_id "
				+ "where admission.status='A' "
				+ "and admission.visit_type_id=4 "
				+ "and admissionDetails.er_bed_type_allocation>0 " 
				+ "and admission.patient_id > 0 "
				+ "AND admission.organization_id=:orgId " 
				+ "AND admission.unit_id=:unitId " ),
		
		@NamedNativeQuery(name="GET_ADMITTED_PATIENT_LIST_LIKE" , query= 
				"SELECT admission.admission_id as \"admissionId\", "
				+ "admission.patient_id as \"patientId\", " 
				+ "admission.t_patient_id as \"tPatientId\", "
				+ "admission.doctor_id as \"doctorId\", "
				+ "concat(coalesce(pre.prefix_code,''),' ',COALESCE(patient.first_name,''),' ',COALESCE(patient.last_name,'')) as \"patientName\", "
				+ "to_char(patient.dob,'DD/MM/YYYY') as \"dob\", " 
				+ "gender.gender_code as \"genderName\", "
				+ "patient.uhid_number as \"UHIDNumber\", "
				+ "bm.bed_number as \"bedNumber\", " 
				+ "bm.bed_id as \"bedId\", "
				+ "admissionDetails.bed_id as \"bedId\", "
				+ "admissionDetails.ward_id as \"wardId\", "
				+ "admissionDetails.room_id as \"roomId\", "
				+ "admissionDetails.bed_category_id as \"bedCategoryId\", "
				+ "admissionDetails.billing_bed_category_id as \"billingBedCategoryId\", "
				+ "ward.ward_name as \"wardName\", "
				+ "concat(COALESCE(doctor.first_name,''),' ',COALESCE(doctor.middle_name,''),' ',COALESCE(doctor.last_name,'')) as \"dFirstName\" "
				+ "FROM  adt.t_admission admission "
				+ "INNER join patient.t_patient_registration patient on admission.patient_id=patient.patient_id "
				+ "INNER join public.m_gender_master gender on patient.gender_id=gender.gender_id "
				+ "  left join m_prefix_master pre on pre.prefix_id = patient.prefix_id "
				+ "INNER join doctor.m_doctor_master doctor on admission.doctor_id=doctor.doctor_id "
				+ "INNER join adt.t_admission_details admissionDetails on admission.admission_id=admissionDetails.admission_id "
				+ "INNER join adt.m_bed_master bm on admissionDetails.bed_id=bm.bed_id "
				+ "INNER JOIN adt.m_ward_master ward on bm.ward_id=ward.ward_id "
				+ "WHERE (LOWER(patient.first_name) LIKE :firstName OR LOWER(patient.last_name) LIKE :firstName OR LOWER(patient.uhid_number) LIKE :firstName) "
				+ "AND admission.status='A' "
				+ "AND admissionDetails.status='A' "
				+ "AND admissionDetails.nursing_acceptance = 'Y' "
				+ "AND admission.unit_id=:unitId "
				+ "AND admission.organization_id=:organizationId "
				+ " order by admission.admission_id desc "),

		@NamedNativeQuery(name = "GET_ADM_VISIT_NOS_BY_PATIENT_ID_FOR_DEPOSIT",
						  query= "select adm.doctor_id as \"doctorId\", adm.admission_id as \"encounterId\", adm.admission_number as \"visitNo\" "
								  + " from adt.t_admission adm "
								  + " inner join adt.t_admission_details dtls on adm.admission_id = dtls.admission_id "
								  + " where adm.visit_type_id =:visitTypeId "
								  + " and adm.patient_id = :patientId "
								  + " and adm.organization_id =:orgId "
								  + " and adm.unit_id =:unitId "
								  + " and dtls.status = 'A'"),
		
		@NamedNativeQuery(name = "GET_ADMISSION_LIST_FOR_IPD_CHARGES_BY_PATIENT",
						 query = " select "
								 + " 	distinct adm.admission_id as \"admissionId\" , "
								 + "	adm.visit_type_id as \"visitTypeId\", "
								 + " 	adm.admission_number as \"admissionNumber\", "
								 + " 	to_char(admdt.doa, 'DD/MM/YYYY') as \"doa\", "
								 + " 	admdt.doa as \"doaActual\", "
								 + " 	patcat.patient_category as \"patientCategory\", "
								 + " 	patcat.patient_category_id as \"patientCategoryId\", "
								 + " 	adm.patient_id as \"patientId\", "
								 + " 	adm.patient_class_id as \"patientClassId\", "
								 + " 	spc.speciality_name as \"department\", "
								 + " 	spc.speciality_id as \"departmentId\", "
								 + " 	doc.doctor_id as \"doctorId\", "
								 + "    admdt.meal_pref_id as \"preferenceMealId\", "
								 + "    mpfm.meal_preference_desc as \"preferenceMealName\", "
								 + "    admdt.diet_type as \"dietType\", "
								 + "    mdtm.diet_type_description as \"dietTypeDescription\", "
								 +"     to_char(pat.dob,'MM/DD/YYYY') as \"dob\",   "
								 + " 	to_char(admdt.pdd, 'DD/MM/YYYY') as \"pdd\", "
								 + " 	admdt.pdd as \"pddActual\", "
								 + " 	admdt.billing_bed_category_id as \"bedBillingCategoryId\", "
								 + " 	admdt.remark as \"remark\", "
								 + " 	bedcat.bed_category_id as \"bedCategoryId\", "
								 + " 	concat(coalesce(doc.first_name, ''), ' ', coalesce(doc.middle_name, ''), ' ', coalesce(doc.last_name, '')) as \"doctorName\", "
								 + " 	bedbill.billing_bed_desc as \"bedBillingCategory\", "
								 + " 	bedcat.bed_category_desc as \"bedCategory\", "
								 + " 	payent.payment_entitlement_desc as \"paymentEntitlement\", "
								 + " 	concat(coalesce(pre.prefix_code,''),' ',coalesce(pat.first_name, ''), ' ', coalesce(pat.middle_name, ''), ' ', coalesce(pat.last_name, '')) as \"patientName\", "
								 + " 	pat.uhid_number as \"uhidNumber\", "
								 + " 	pat.gender_id as \"genderId\", "
								 + " 	ward.ward_code as \"wardNo\","
								 + "    ward.ward_id as \"wardId\", "
								 + " 	bedm.bed_number as \"bedNo\", "
								 + " 	bedm.is_moving_bed as \"isMovingBed\", "
								 + "    bedm.bed_id as \"bedId\","
								 + "    bedm.room_id as \"roomId\","
								 + "    bedm.floor_id as \"floorId\","
								 + " 	payent.payment_entitlement_id as \"paymentEntitlementId\", "
								 + " 	spd.payee_id as \"payeeId\", "
								 + "	billConfig.self_payee_id as \"selfPayeeId\", "
								 + " 	spd.contract_id as \"contractId\", "
								 + " 	comp.contract_name as \"contractName\", "
								 + " 	company.company_desc as \"companyDesc\", "
								 + " 	to_char(spd.valid_to, 'DD/MM/YYYY') as \"validTo\", "
								 + " 	dis.discharge_status_id as \"dischargeStatusId\", "
								 + " 	adm.is_final_bill as \"isFinalBill\", "
								 + " 	refrlTypeMst.referral_type_name as \"referralTypeName\", "
								 + " 	reflMst.referral_id as \"referralId\", "
								 + " 	reflMst.referral_name as \"referralName\", "
								 + " 	refrDtls.referral_remark as \"referralRemark\" ,"
								 + " 	coalesce(comp.is_tariff_rate_applicable,'Y') as \"isTariffRateApplicable\", "
								 + "	billConfig.default_self_tariff_id as \"defaultSelfTariffId\" "
								 + " from "
								 + " 	adt.t_admission adm "
								 + " inner join adt.t_admission_details admdt on "
								 + " 	admdt.admission_id = adm.admission_id "
								 + " left join adt.m_meal_preference_master mpfm on "
								 + " 	admdt.meal_pref_id = mpfm.meal_preference_id "
								 + " left join kitchen.m_diet_type_master mdtm on "
								 + " 	admdt.diet_type = mdtm.diet_type_id "
								 + " inner join opd.t_patient_sponsor_details spd on "
								 + " 	spd.admission_id = adm.admission_id "
								 + " 	and spd.priority_id = 1 "
								 + " 	and spd.status = 'A' "
								 + " left join doctor.m_speciality_master spc on "
								 + " 	spc.speciality_id = adm.speciality_id "
								 + " left join doctor.m_doctor_master doc on "
								 + " 	doc.doctor_id = adm.doctor_id "
								 + " inner join adt.m_bed_master bedm on "
								 + " 	bedm.bed_id = admdt.bed_id "
								 + " inner join adt.m_bed_category_master bedcat on "
								 + " 	bedcat.bed_category_id = admdt.bed_category_id "
								 + " inner join adt.m_billing_bed_category_master bedbill on "
								 + " 	bedbill.billing_bed_category_id = admdt.billing_bed_category_id "
								 + " inner join public.m_payment_entitlement_master payent on "
								 + " 	payent.payment_entitlement_id = spd.payment_entitlement_id "
								 + " inner join patient.t_patient_registration pat on "
								 + " 	pat.patient_id = adm.patient_id "
								 + "  left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
								 + " inner join public.m_patient_category_master patcat on "
								 + " 	patcat.patient_category_id = pat.patient_category_id "
								 + " left join adt.m_ward_master ward on "
								 + " 	ward.ward_id = bedm.ward_id "
								 + " left join service.m_company_contract_master comp on "
								 + " 	spd.contract_id = comp.contract_id "
								 + " left join public.t_company_master company on "
								 + " 	company.company_id = spd.payee_id "
								 + " left join adt.t_discharge dis on "
								 + " 	dis.admission_id = adm.admission_id AND dis.status='A' "
								 + " left join public.t_referal_details refrDtls on "
								 + " 	refrDtls.admission_id = adm.admission_id "
								 + " left join adt.m_referral_type_master refrlTypeMst on "
								 + " 	refrlTypeMst.referral_type_id = refrDtls.referral_type_id "
								 + " left join adt.m_referral_master reflMst on "
								 + " 	reflMst.referral_id = refrDtls.referral_id "
								 + "  left join t_unit_application_billing_configuration billConfig on "
								 + " 	billConfig.unit_id = adm.unit_id "
								 + " where "
								 + " 	adm.admission_id =:admissionId "
								 + " 	and admdt.nursing_acceptance = 'Y' "
								 + " 	and admdt.status = 'A' "
								 + " order by "
								 + " 	company.company_desc ASC "
								 + " limit 1 "),
		
		@NamedNativeQuery(name = "GET_ADMISSION_LIST_FOR_IPD_CHARGES_BY_ADMISSION_ID",
		 query = " select "
				 + " 	distinct adm.admission_id as \"admissionId\",  "
				 + " 	admdt.billing_bed_category_id as \"bedBillingCategoryId\"  "
				 + " from "
				 + " 	adt.t_admission adm "
				 + " inner join adt.t_admission_details admdt on "
				 + " 	admdt.admission_id = adm.admission_id "
				 + " where "
				 + " 	adm.admission_id =:admissionId "
				 + " order by "
				 + " 	adm.admission_id desc "
				 + " limit 1 "),
		
		
		@NamedNativeQuery(name = "GET_ADM_DTLS_CANCELLED_BILLS",
		query =    " select " 
				 + " 	adm.admission_id as \"admissionId\" , " 
				 + " 	adm.visit_type_id as \"visitTypeId\", " 
				 + " 	adm.admission_number as \"admissionNumber\", " 
				 + " 	to_char(admdt.doa, 'DD/MM/YYYY') as \"doa\", " 
				 + " 	patcat.patient_category as \"patientCategory\", " 
				 + " 	patcat.patient_category_id as \"patientCategoryId\", " 
				 + " 	adm.patient_id as \"patientId\", " 
				 + " 	adm.patient_class_id as \"patientClassId\", " 
				 + " 	spc.speciality_name as \"department\", " 
				 + " 	spc.speciality_id as \"departmentId\", " 
				 + " 	doc.doctor_id as \"doctorId\", " 
				 + " 	to_char(pat.dob, 'MM/DD/YYYY') as \"dob\", " 
				 + " 	to_char(admdt.pdd, 'DD/MM/YYYY') as \"pdd\", " 
				 + " 	admdt.billing_bed_category_id as \"bedBillingCategoryId\", " 
				 + " 	bedcat.bed_category_id as \"bedCategoryId\", " 
				 + " 	concat(coalesce(doc.first_name, ''), ' ', coalesce(doc.middle_name, ''), ' ', coalesce(doc.last_name, '')) as \"doctorName\", " 
				 + " 	bedbill.billing_bed_desc as \"bedBillingCategory\", " 
				 + " 	bedcat.bed_category_desc as \"bedCategory\", " 
				 + " 	payent.payment_entitlement_desc as \"paymentEntitlement\", " 
				 + " 	concat(coalesce(pre.prefix_code,''),' ',coalesce(pat.first_name, ''), ' ', coalesce(pat.middle_name, ''), ' ', coalesce(pat.last_name, '')) as \"patientName\", " 
				 + " 	pat.uhid_number as \"uhidNumber\", " 
				 + " 	ward.ward_code as \"wardNo\", " 
				 + " 	ward.ward_id as \"wardId\", " 
				 + " 	bedm.bed_number as \"bedNo\", " 
				 + " 	bedm.is_moving_bed as \"isMovingBed\", " 
				 + " 	bedm.bed_id as \"bedId\", " 
				 + " 	bedm.room_id as \"roomId\", " 
				 + " 	bedm.floor_id as \"floorId\", " 
				 + " 	payent.payment_entitlement_id as \"paymentEntitlementId\", " 
				 + " 	spd.payee_id as \"payeeId\", " 
				 + " 	billConfig.self_payee_id as \"selfPayeeId\", " 
				 + " 	spd.contract_id as \"contractId\", " 
				 + " 	comp.contract_name as \"contractName\", " 
				 + " 	company.company_desc as \"companyDesc\", " 
				 + " 	to_char(spd.valid_to, 'DD/MM/YYYY') as \"validTo\", " 
				 + " 	dis.discharge_status_id as \"dischargeStatusId\", " 
				 + " 	adm.is_final_bill as \"isFinalBill\", " 
				 + " 	refrlTypeMst.referral_type_name as \"referralTypeName\", " 
				 + " 	reflMst.referral_id as \"referralId\", " 
				 + " 	reflMst.referral_name as \"referralName\", " 
				 + " 	refrDtls.referral_remark as \"referralRemark\" , " 
				 + " 	coalesce(comp.is_tariff_rate_applicable, 'Y') as \"isTariffRateApplicable\", " 
				 + " 	billConfig.default_self_tariff_id as \"defaultSelfTariffId\" " 
				 + " from " 
				 + " 	adt.t_admission adm " 
				 + " inner join adt.t_admission_details admdt on " 
				 + " 	admdt.admission_id = adm.admission_id  " 
				 + " inner join opd.t_patient_sponsor_details spd on " 
				 + " 	spd.admission_id = adm.admission_id " 
				 + " 	and spd.priority_id = 1 " 
				 + " 	and spd.status = 'A' " 
				 + " left join doctor.m_speciality_master spc on " 
				 + " 	spc.speciality_id = adm.speciality_id " 
				 + " left join doctor.m_doctor_master doc on " 
				 + " 	doc.doctor_id = adm.doctor_id " 
				 + " inner join adt.m_bed_master bedm on " 
				 + " 	bedm.bed_id = admdt.bed_id " 
				 + " inner join adt.m_bed_category_master bedcat on " 
				 + " 	bedcat.bed_category_id = admdt.bed_category_id " 
				 + " inner join adt.m_billing_bed_category_master bedbill on " 
				 + " 	bedbill.billing_bed_category_id = admdt.billing_bed_category_id " 
				 + " inner join public.m_payment_entitlement_master payent on " 
				 + " 	payent.payment_entitlement_id = spd.payment_entitlement_id " 
				 + " inner join patient.t_patient_registration pat on " 
				 + " 	pat.patient_id = adm.patient_id " 
				 + "  left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
				 + " inner join public.m_patient_category_master patcat on " 
				 + " 	patcat.patient_category_id = pat.patient_category_id " 
				 + " left join adt.m_ward_master ward on " 
				 + " 	ward.ward_id = bedm.ward_id " 
				 + " left join service.m_company_contract_master comp on " 
				 + " 	spd.contract_id = comp.contract_id " 
				 + " left join public.t_company_master company on " 
				 + " 	company.company_id = spd.payee_id " 
				 + " left join adt.t_discharge dis on " 
				 + " 	dis.admission_id = adm.admission_id " 
				 + " left join public.t_referal_details refrDtls on " 
				 + " 	refrDtls.admission_id = adm.admission_id " 
				 + " left join adt.m_referral_type_master refrlTypeMst on " 
				 + " 	refrlTypeMst.referral_type_id = refrDtls.referral_type_id " 
				 + " left join adt.m_referral_master reflMst on " 
				 + " 	reflMst.referral_id = refrDtls.referral_id " 
				 + " left join t_unit_application_billing_configuration billConfig on " 
				 + " 	billConfig.unit_id = adm.unit_id " 
				 + " where " 
				 + " 	adm.admission_id =:admissionId "
				 + " order by " 
				 + " 	admdt.admission_details_id desc  " 
				 + " limit 1 " ),
		
		@NamedNativeQuery(name = "GET_ADMISSION_LIST_FOR_IPD_CHARGES_BY_PATIENT_FOR_BILLOF_MATERIAL",
		 query = " select "
				 + " 	distinct adm.admission_id as \"admissionId\" , "
				 + "	adm.visit_type_id as \"visitTypeId\", "
				 + " 	adm.admission_number as \"admissionNumber\", "
				 + " 	to_char(admdt.doa, 'DD/MM/YYYY') as \"doa\", "
				 + " 	patcat.patient_category as \"patientCategory\", "
				 + " 	patcat.patient_category_id as \"patientCategoryId\", "
				 + " 	adm.patient_id as \"patientId\", "
				 + " 	adm.patient_class_id as \"patientClassId\", "
				 + " 	spc.speciality_name as \"department\", "
				 + " 	spc.speciality_id as \"departmentId\", "
				 + " 	doc.doctor_id as \"doctorId\", "
				 +"     to_char(pat.dob,'MM/DD/YYYY') as \"dob\",   "
				 + " 	to_char(admdt.pdd, 'DD/MM/YYYY') as \"pdd\", "
				 + " 	admdt.billing_bed_category_id as \"bedBillingCategoryId\", "
				 + " 	bedcat.bed_category_id as \"bedCategoryId\", "
				 + " 	concat('Dr. ',' ',coalesce(doc.first_name, ''), ' ', coalesce(doc.middle_name, ''), ' ', coalesce(doc.last_name, '')) as \"doctorName\", "
				 + " 	bedbill.billing_bed_desc as \"bedBillingCategory\", "
				 + " 	bedcat.bed_category_desc as \"bedCategory\", "
				 + " 	payent.payment_entitlement_desc as \"paymentEntitlement\", "
				 + " 	concat(coalesce(pre.prefix_code,''),' ',coalesce(pat.first_name, ''), ' ', coalesce(pat.middle_name, ''), ' ', coalesce(pat.last_name, '')) as \"patientName\", "
				 + " 	pat.uhid_number as \"uhidNumber\", "
				 + " 	ward.ward_code as \"wardNo\","
				 + "    ward.ward_id as \"wardId\", "
				 + " 	bedm.bed_number as \"bedNo\", "
				 + " 	bedm.is_moving_bed as \"isMovingBed\", "
				 + "    bedm.bed_id as \"bedId\","
				 + "    bedm.room_id as \"roomId\","
				 + "    bedm.floor_id as \"floorId\","
				 + " 	payent.payment_entitlement_id as \"paymentEntitlementId\", "
				 + " 	spd.payee_id as \"payeeId\", "
				 + "	billConfig.self_payee_id as \"selfPayeeId\", "
				 + " 	spd.contract_id as \"contractId\", "
				 + " 	comp.contract_name as \"contractName\", "
				 + " 	company.company_desc as \"companyDesc\", "
				 + " 	to_char(spd.valid_to, 'DD/MM/YYYY') as \"validTo\", "
				 + " 	dis.discharge_status_id as \"dischargeStatusId\", "
				 + " 	adm.is_final_bill as \"isFinalBill\", "
				 + " 	refrlTypeMst.referral_type_name as \"referralTypeName\", "
				 + " 	reflMst.referral_id as \"referralId\", "
				 + " 	reflMst.referral_name as \"referralName\", "
				 + " 	refrDtls.referral_remark as \"referralRemark\" ,"
				 + " 	coalesce(comp.is_tariff_rate_applicable,'Y') as \"isTariffRateApplicable\", "
				 + "	billConfig.default_self_tariff_id as \"defaultSelfTariffId\" "
				 + " from "
				 + " 	adt.t_admission adm "
				 + " inner join adt.t_admission_details admdt on "
				 + " 	admdt.admission_id = adm.admission_id "
				 + " inner join opd.t_patient_sponsor_details spd on "
				 + " 	spd.admission_id = adm.admission_id "
				 + " 	and spd.priority_id = 1 "
				 + " 	and spd.status = 'A' "
				 + " left join doctor.m_speciality_master spc on "
				 + " 	spc.speciality_id = adm.speciality_id "
				 + " left join doctor.m_doctor_master doc on "
				 + " 	doc.doctor_id = adm.doctor_id "
				 + " inner join adt.m_bed_master bedm on "
				 + " 	bedm.bed_id = admdt.bed_id "
				 + " inner join adt.m_bed_category_master bedcat on "
				 + " 	bedcat.bed_category_id = admdt.bed_category_id "
				 + " inner join adt.m_billing_bed_category_master bedbill on "
				 + " 	bedbill.billing_bed_category_id = admdt.billing_bed_category_id "
				 + " inner join public.m_payment_entitlement_master payent on "
				 + " 	payent.payment_entitlement_id = spd.payment_entitlement_id "
				 + " inner join patient.t_patient_registration pat on "
				 + " 	pat.patient_id = adm.patient_id "
				 + "  left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
				 + " inner join public.m_patient_category_master patcat on "
				 + " 	patcat.patient_category_id = pat.patient_category_id "
				 + " left join adt.m_ward_master ward on "
				 + " 	ward.ward_id = bedm.ward_id "
				 + " left join service.m_company_contract_master comp on "
				 + " 	spd.contract_id = comp.contract_id "
				 + " left join public.t_company_master company on "
				 + " 	company.company_id = spd.payee_id "
				 + " left join adt.t_discharge dis on "
				 + " 	dis.admission_id = adm.admission_id "
				 + " left join public.t_referal_details refrDtls on "
				 + " 	refrDtls.admission_id = adm.admission_id "
				 + " left join adt.m_referral_type_master refrlTypeMst on "
				 + " 	refrlTypeMst.referral_type_id = refrDtls.referral_type_id "
				 + " left join adt.m_referral_master reflMst on "
				 + " 	reflMst.referral_id = refrDtls.referral_id "
				 + "  left join t_unit_application_billing_configuration billConfig on "
				 + " 	billConfig.unit_id = adm.unit_id "
				 + " where "
				 + " 	adm.admission_id =:admissionId "
				 + " 	and admdt.status = 'A' "
				 + " order by "
				 + " 	adm.admission_id desc "
				 + " limit 1 "),
		
		@NamedNativeQuery(name = "GET_ADMISSION_LIST_FOR_IPD_CHARGES_BY_PATIENT_FOR_UPDATE_PAYEE",
		 query = "select "
			+ " distinct adm.admission_id as \"admissionId\" ,"
			+ " adm.admission_number as \"admissionNumber\","
			+ " to_char(admdt.doa,'DD/MM/YYYY') as \"doa\","
			+ " patcat.patient_category as \"patientCategory\","
			+ " patcat.patient_category_id as \"patientCategoryId\","
			+ " adm.patient_id as \"patientId\","
			+ " adm.patient_class_id as \"patientClassId\","
			+ " spc.speciality_name as \"department\","
			+ " spc.speciality_id as \"departmentId\","
			+ " doc.doctor_id as \"doctorId\","
			+ " to_char(admdt.pdd,'DD/MM/YYYY') as \"pdd\","
			+ " admdt.billing_bed_category_id as \"bedBillingCategoryId\","
			+ " bedcat.bed_category_id as \"bedCategoryId\","
			+ " concat(COALESCE(doc.first_name,''), ' ', COALESCE(doc.middle_name,''), ' ', COALESCE(doc.last_name,'')) as \"doctorName\","
			+ " bedbill.billing_bed_desc as \"bedBillingCategory\","
			+ " bedcat.bed_category_desc as \"bedCategory\","
			+ " payent.payment_entitlement_desc as \"paymentEntitlement\","
			+ " concat(coalesce(pre.prefix_code,''),' ',COALESCE(pat.first_name,''), ' ', COALESCE(pat.middle_name,''), ' ', COALESCE(pat.last_name,'')) as \"patientName\","
			+ " pat.uhid_number as \"uhidNumber\","
			+ " ward.ward_code as \"wardNo\","
			+ " bedm.bed_number as \"bedNo\","
			+ " payent.payment_entitlement_id as \"paymentEntitlementId\","
			+ " spd.payee_id as \"payeeId\","
			+ " spd.contract_id as \"contractId\","
			+ " comp.contract_name as \"contractName\","
			+ " company.company_desc as \"companyDesc\","
			+ " to_char(spd.valid_to,'DD/MM/YYYY') as \"validTo\", "
			+ " dis.discharge_status_id as \"dischargeStatusId\", "
			+ " adm.is_final_bill as \"isFinalBill\", "
			+ " referral.referral_id as \"referralId\" "
			+ " from adt.t_admission adm "
			+ " inner join adt.t_admission_details admdt on "
			+ " 	admdt.admission_id = adm.admission_id "
			+ " inner join opd.t_patient_sponsor_details spd on "
			+ " 	spd.admission_id = adm.admission_id "
			+ " 	and spd.priority_id = 1 and spd.status='A' "
			+ " left join doctor.m_speciality_master spc on "
			+ " 	spc.speciality_id = adm.speciality_id "
			+ " left join doctor.m_doctor_master doc on "
			+ " 	doc.doctor_id = adm.doctor_id "
			+ " inner join adt.m_bed_master bedm on "
			+ " 	bedm.bed_id = admdt.bed_id "
			+ " inner join adt.m_bed_category_master bedcat on "
			+ " 	bedcat.bed_category_id = admdt.bed_category_id "
			+ " inner join adt.m_billing_bed_category_master bedbill on "
			+ " 	bedbill.billing_bed_category_id = admdt.billing_bed_category_id "
			+ " inner join public.m_payment_entitlement_master payent on "
			+ " 	payent.payment_entitlement_id = spd.payment_entitlement_id "
			+ " inner join patient.t_patient_registration pat on "
			+ " 	pat.patient_id = adm.patient_id "
			+ "  left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
			+ " inner join public.m_patient_category_master patcat on "
			+ " 	patcat.patient_category_id = pat.patient_category_id "
			+ " inner join adt.m_ward_master ward on "
			+ " 	ward.ward_id = bedm.ward_id "
			+ " left join service.m_company_contract_master comp on "
			+ "		spd.contract_id=comp.contract_id  "
			+ " left join public.t_company_master company on "
			+ "		company.company_id=spd.payee_id "
			+ "left join adt.t_discharge dis on "
			+ "		dis.admission_id = adm.admission_id "
			+ " left join t_referal_details referral on "
			+ " 	referral.admission_id = adm.admission_id "
			+ " where "
				+ " adm.admission_id =:admissionId "
				+ " AND admdt.status = 'A' "
			+ " order by "
				+ " adm.admission_id desc limit 1 "),
		

		@NamedNativeQuery(name = "GET_ADMISSION_DETAILS_BY_ID_FOR_POSTING_PERIODICSERVICES_THROUGH_BATCH",
		 query = "select adm.admission_id as \"admissionId\", adm.unit_id as \"unitId\", adm.organization_id as \"organizationId\", adm.admission_number as \"admissionNumber\", "
				 + " admdt.billing_bed_category_id as \"billingBedCategoryId\", admdt.bed_category_id as \"bedCategoryId\", psd.payment_entitlement_id as \"paymentEntitlementId\",  "
				 + " psd.tariff_id as \"tariffId\",adm.visit_type_id as \"visitTypeId\", pat.patient_category_id as \"patientTypeId\", adm.patient_id as \"patientId\", adm.doctor_id as \"doctorId\","
				 + " admdt.bed_id as \"bedId\", admdt.room_id as \"roomId\", admdt.ward_id as \"wardId\" "
				 + " from adt.t_admission adm  "
				 + " inner join adt.t_admission_details admdt "
				 + " on admdt.admission_id = adm.admission_id "
				 + " inner join opd.t_patient_sponsor_details psd "
				 + " on psd.admission_id = adm.admission_id "
				 + " inner join patient.t_patient_registration pat "
				 + " on pat.patient_id = adm.patient_id "
				 + " where admdt.is_discharge = 'N' "
				 + " and adm.admission_id =:admissionId "),
		
		@NamedNativeQuery(name="GET_PATIENT_ACCEPTANCE_LIST_FOR_NURSING",query="SELECT "
			+ "  admission.admission_id as \"admissionId\", "
			+ "  adl.admission_details_id as \"admissionDetailsId\", "
			+ "  admission.patient_id as \"patientId\", "
			+ "  admission.admission_number as \"admissionNumber\", "
			+ "  admission.visit_type_id as \"visitTypeId\", "
			+ "  admission.doctor_id as \"doctorId\", "
			+ "  admission.speciality_id as \"specialityId\", "
			+ "  sp.speciality_name as \"specialityName\", "
			+ "  pr.uhid_number as \"uhidNumber\", "
			+ "  pr.gender_id as \"genderId\", "
			+ "  gm.gender_code as \"genderCode\", "
			+ "  to_char(pr.dob, 'MM/DD/YYYY') as \"dob\", "
			+ "  concat('Dr. ',' ',COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"doctorName\", "
			+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"doa\", "
			+ "  to_char(adl.pdd, 'DD/MM/YYYY') as \"pdd\", "
			+ "  spd.payment_entitlement_id as \"paymentEntitlementId\", "
			+ "  spd.contract_id as \"contractId\", "
			+ "  spd.payee_id as \"payeeId\", "
			+ "  pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
			+ "  bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
			+ "  bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
			+ "  bedCatMst.bed_category_id as \"bedCategoryId\", "
			+ "  bm.floor_id as \"floorId\", "
			+ "  bm.bed_number as \"bedNumber\", "
			+ "  bm.bed_id as \"bedId\", "
			+ "  bm.ward_id as \"wardId\", "
			+ "  ward.ward_name as \"wardName\", "
            + "  floor.floor_name as \"floorName\", "
			+ "  adl.is_short_leave as \"isShortLeave\", "
			+ "  pr.patient_category_id as \"patientCategoryId\", "
			+ "  disc.discharge_id as \"dischargeId\", "
			+ "  disc.discharge_status_id as \"dischargeStatusId\", "
            + "  uabc.bucket_flag as \"bucketFlag\", "
			+ "  pr.is_vip as \"isVip\", "
			 + " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\", "
            + " prefxmstr.prefix_code as \"prefixDesc\",pr.image_path as \"imagePath\" , "
			+ "  (case when pr.is_vip='Y' then "
			+ "    coalesce(pr.alise_name,pr.first_name) "
			+ "    else "
			+ "    concat(coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name)) "
			+ "  end) as \"patientName\", "
			+ "  (case when (select total_score from adt.t_mews_mapper where admission_id = admission.admission_id and status='A') > 6 then "
			+ "  	1 "
			+ "   else "
			+ "     0 end) as \"isCriticalOrder\","
			+ " note.encounter_id as \"encounterId\", "
			+ " note.visit_type_id as \"admissionNoteVisitType\" " 
			+ "FROM "
			+ "  adt.t_admission admission "
			+ "  INNER JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id "
            + "  inner join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
			+ "  INNER JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
			+ "  INNER JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
			+ "  INNER JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id "
            + "  INNER JOIN public.t_unit_application_billing_configuration uabc on admission.unit_id = uabc.unit_id "                
			+ "  INNER JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id "
			+ "  INNER JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id "
			+ "  INNER JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
            + "  INNER JOIN adt.m_floor_master floor on ward.floor_id = floor.floor_id "
			+ "  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 and spd.status='A' "
			+ "  LEFT JOIN adt.t_discharge disc on admission.admission_id = disc.admission_id "
			+ "  INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
			+ "  INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
			+ " left join adt.t_admission_note note on note.admission_id = adl.admission_id "
			+ "WHERE "
			+ "  admission.status = 'A' "
			+ " AND adl.status='A' "
			+ " AND adl.nursing_acceptance = 'P' "
			+ "  AND admission.organization_id = :orgId "
			+ "  AND admission.unit_id = :unitId "
			+ "	 AND adl.ward_id IN (:wardList) "),
		
		@NamedNativeQuery(name = "GET_ADMITTED_PATIENT_LIST_FOR_IPD_NURSING_WORKLIST", 
		query ="SELECT "
				+ "  admission.admission_id as \"admissionId\", "
				+ "  admission.patient_id as \"patientId\", "
				+ "  admission.t_patient_id as \"tPatientId\", "
				+ "  admission.admission_number as \"admissionNumber\", "
				+ "  admission.visit_type_id as \"visitTypeId\", "
				+ "  admission.doctor_id as \"doctorId\", "
				+ "  admission.speciality_id as \"specialityId\", "
				+ "  sp.speciality_name as \"specialityName\", "
				+ "  pr.uhid_number as \"UHIDNumber\", "
				+ "  concat(coalesce(pre.prefix_code,''),' ',COALESCE(pr.first_name,''),' ',COALESCE(pr.middle_name,''),' ',COALESCE(pr.last_name,'')) as \"pFirstName\", "
				+ "  pr.gender_id as \"genderId\", "
				+ "  gm.gender_code as \"genderCode\", "
				+ "  to_char(pr.dob, 'MM/DD/YYYY') as \"dob\", "
				+ "  concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
				+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"doa\", "
				+ "  to_char(adl.pdd, 'DD/MM/YYYY') as \"pdd\", "
				+ "  spd.payment_entitlement_id as \"paymentEntitlementId\", "
				+ "  spd.contract_id as \"contractId\", "
				+ "  spd.payee_id as \"payeeId\", "
				+ "  pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
				+ "  bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
				+ "  bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
				+ "  bedCatMst.bed_category_id as \"bedCategoryId\", "
				+ "  bm.floor_id as \"floorId\", "
				+ "  bm.bed_number as \"bedNumber\", "
				+ "  adl.bed_id as \"bedId\", "
				+ "  adl.ward_id as \"wardId\", "
				+ "  adl.room_id as \"roomId\", "
				+ "  ward.ward_name as \"wardName\", "
				+ "  adl.is_short_leave as \"isShortLeave\", "
				+ "  pr.patient_category_id as \"patientCategoryId\", "
				+ "  admission.concent as \"concent\", "
				+ "  disc.discharge_status_id as \"dischargeStatusId\" "
				+ "FROM "
				+ "  adt.t_admission admission "
				+ "  INNER JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id "
				+ "  left join m_prefix_master pre on pre.prefix_id = pr.prefix_id "
				+ "  INNER JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
				+ "  INNER JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
				+ "  INNER JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id "
				+ "  INNER JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id "
				+ "  INNER JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id "
				+ "  INNER JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
				+ "  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 and spd.status='A' "
				+ "  INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
				+ "  INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
				+ "  LEFT JOIN adt.t_discharge disc on admission.admission_id = disc.admission_id "
				+ "WHERE "
				+ "  admission.status = 'A' "
				+ " AND adl.status='A' "
				+ " AND adl.nursing_acceptance = 'Y' "
				+ "  AND admission.organization_id = :organizationId "
				+ "  AND admission.unit_id = :unitId "
				+ " AND bm.ward_id IN (:wardList) " 
				+ "UNION "
				+ "SELECT "
				+ "  admission.admission_id as \"admissionId\", "
				+ "  admission.patient_id as \"patientId\", "
				+ "  admission.t_patient_id as \"tPatientId\", "
				+ "  admission.admission_number as \"admissionNumber\", "
				+ "  admission.visit_type_id as \"visitTypeId\", "
				+ "  admission.doctor_id as \"doctorId\", "
				+ "  admission.speciality_id as \"specialityId\", "
				+ "  sp.speciality_name as \"specialityName\", "
				+ "  pr.t_uhid as \"UHIDNumber\", "
				+ "  pr.patient_name as \"pFirstName\", "
				+ "  pr.gender_id as \"genderId\", "
				+ "  gm.gender_code as \"genderCode\", "
				+ "  concat(to_char(pr.age, '999'), ' ', pr.age_format) as \"dob\", "
				+ "  concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
				+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"doa\", "
				+ "  to_char(adl.pdd, 'DD/MM/YYYY') as \"pdd\", "
				+ "  spd.payment_entitlement_id as \"paymentEntitlementId\", "
				+ "  spd.contract_id as \"contractId\", "
				+ "  spd.payee_id as \"payeeId\", "
				+ "  pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
				+ "  bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
				+ "  bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
				+ "  bedCatMst.bed_category_id as \"bedCategoryId\", "
				+ "  bm.floor_id as \"floorId\", "
				+ "  bm.bed_number as \"bedNumber\", "
				+ "  adl.bed_id as \"bedId\", "
				+ "  adl.ward_id as \"wardId\", "
				+ "  adl.room_id as \"roomId\", "
				+ "  ward.ward_name as \"wardName\", "
				+ "  adl.is_short_leave as \"isShortLeave\", "
				+ "  1 as \"patientCategoryId\", "
				+ "  admission.concent as \"concent\", "
				+ "  disc.discharge_status_id as \"dischargeStatusId\" "
				+ "FROM "
				+ "  adt.t_admission admission "
				+ "  INNER JOIN patient.m_unknown_patient_registration pr on admission.t_patient_id = pr.unknown_patient_id "
				+ "  INNER JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
				+ "  INNER JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
				+ "  INNER JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id "
				+ "  INNER JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id "
				+ "  INNER JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id "
				+ "  INNER JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
				+ "  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 and spd.status='A' "
				+ "  INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
				+ "  INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
				+ "  LEFT JOIN adt.t_discharge disc on admission.admission_id = disc.admission_id "
				+ "WHERE "
				+ "  admission.status = 'A' "
				+ " AND adl.status='A' "
				+ " AND adl.nursing_acceptance = 'Y' "
				+ "  AND admission.organization_id = :organizationId "
				+ "  AND admission.unit_id = :unitId "
				+" AND bm.ward_id IN (:wardList) "
				+ "order by \"admissionId\" desc "),
		
		@NamedNativeQuery(name = "GET_ADMITTED_PATIENT_LIST_FOR_IPD_NURSING_WORKLIST_FOR_NURSING_PAGE", 
		query ="SELECT "
				+ "  admission.admission_id as \"admissionId\", "
				+ "  admission.patient_id as \"patientId\", "
				+ "  admission.t_patient_id as \"tPatientId\", "
				+ "  admission.admission_number as \"admissionNumber\", "
				+ "  admission.visit_type_id as \"visitTypeId\", "
	            + "  admission.is_final_bill as \"isFinalBill\", "
				+ "  admission.doctor_id as \"doctorId\", "
				+ "  admission.speciality_id as \"specialityId\", "
				+ "  sp.speciality_name as \"specialityName\", "
				+ "  pr.uhid_number as \"UHIDNumber\", "
				+ "  pr.gender_id as \"genderId\", "
				+ "  gm.gender_code as \"genderCode\", "
				+ "  to_char(pr.dob, 'MM/DD/YYYY') as \"dob\", "
				+ "  concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
				+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"doa\", "
				+ "  to_char(adl.pdd, 'DD/MM/YYYY') as \"pdd\", "
				+ "  spd.payment_entitlement_id as \"paymentEntitlementId\", "
				+ "  spd.contract_id as \"contractId\", "
				+ "  spd.payee_id as \"payeeId\", "
				+ "  pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
				+ "  bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
				+ "  bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
				+ "  bedCatMst.bed_category_id as \"bedCategoryId\", "
				+ "  bm.floor_id as \"floorId\", "
				+ "  bm.bed_number as \"bedNumber\", "
				+ "  to_char(adl.doa, 'HH:MI AM') as \"createdTime\","
				+ "  adl.bed_id as \"bedId\", "
				+ "  adl.ward_id as \"wardId\", "
				+ "  adl.room_id as \"roomId\", "
				+ "  ward.ward_name as \"wardName\", "
                + "  floor.floor_name as \"floorName\", "
				+ "  adl.is_short_leave as \"isShortLeave\", "
				+ "  pr.patient_category_id as \"patientCategoryId\", "
				+ "  admission.concent as \"concent\", "
				+ "  medic.medico_legal_id as \"medicoLegalId\","
				+ "  disc.discharge_status_id as \"dischargeStatusId\", "
				+ "  disc.discharge_id as \"dischargeId\", "
				+ "  pr.is_vip as \"isVip\", "
				+ "  pr.uuid as \"uuid\", "
				+ "  uabc.bucket_flag as \"bucketFlag\", "
				+ "  prefxmstr.prefix_id as \"prefixId\" ,"
				 + " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\", "
				+ "  prefxmstr.prefix_desc as \"prefixDesc\",pr.image_path as \"imagePath\" , "
				+ "  (case when pr.is_vip='Y' then "
				+ "  coalesce(pr.alise_name,pr.first_name) "
				+ "  else "
				+ "  concat(coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name)) "
				+ "  end) as \"pFirstName\", "
				+ "  (case when (select total_score from adt.t_mews_mapper where admission_id = admission.admission_id and status='A') > 6 then "
				+ "  	1 "
				+ "   else "
				+ "     0 end) as \"isCriticalOrder\","
				+ " admission.reference_number as \"referenceNumber\","
				+ " bedCatMst.is_icu as \"isIcuu\" "
				+ "FROM "
				+ "  adt.t_admission admission "
				+ "  INNER JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id"
				+ "  INNER JOIN public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
				+ "  INNER JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
				+ "  INNER JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
				+ "  INNER JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id "
				+ "  INNER JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id "
				+ "  INNER JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id "
				+ "  INNER JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
	            + "  INNER JOIN adt.m_floor_master floor on ward.floor_id = floor.floor_id "
	            + "  INNER JOIN public.t_unit_application_billing_configuration uabc on admission.unit_id = uabc.unit_id "	            
				+ "  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 and spd.status='A' "
				+ "  INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
				+ "  INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
				+ "  LEFT JOIN adt.t_discharge disc on admission.admission_id = disc.admission_id and disc.status='A' "
				+ "  LEFT JOIN adt.t_medico_legal medic on admission.admission_id = medic.admission_id "
				+ "WHERE "
				+ "  admission.status = 'A' "
				+ " AND adl.status='A' "
				+ " AND adl.nursing_acceptance = 'Y' "
				+ "  AND admission.organization_id = :organizationId "
				+ "  AND admission.unit_id = :unitId "
				+ " AND admission.visit_type_id=:visitTypeId "
				+ " AND bm.ward_id IN (:wardList) " 
				+ " order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc "),
		
		@NamedNativeQuery(name = "GET_PATIENT_DETAILS_FOR_WORKLIST_FOR_WEB_PAGE_ADMISSION", 
		query ="SELECT "
				+ "  admission.admission_id as \"admissionId\", "
				+ "  admission.patient_id as \"patientId\", "
				+ "  admission.t_patient_id as \"tPatientId\", "
				+ "  admission.admission_number as \"admissionNumber\", "
				+ "  admission.visit_type_id as \"visitTypeId\", "
				+ "  admission.doctor_id as \"doctorId\", "
				+ "  admission.speciality_id as \"specialityId\", "
				+ "  sp.speciality_name as \"specialityName\", "
				+ "  pr.uhid_number as \"UHIDNumber\", "
				+ "  pr.gender_id as \"genderId\", "
				+ "  gm.gender_code as \"genderCode\", "
				+ "  to_char(pr.dob, 'MM/DD/YYYY') as \"dob\", "
				+ "  concat('Dr. ',COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
				+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"doa\", "
				+ "  to_char(adl.pdd, 'DD/MM/YYYY') as \"pdd\", "
				+ "  spd.payment_entitlement_id as \"paymentEntitlementId\", "
				+ "  spd.contract_id as \"contractId\", "
				+ "  spd.payee_id as \"payeeId\", "
				+ "  pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
				+ "  bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
				+ "  bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
				+ "  bedCatMst.bed_category_id as \"bedCategoryId\", "
				+ "  bm.floor_id as \"floorId\", "
				+ "  bm.bed_number as \"bedNumber\", "
				+ "  to_char(adl.doa, 'HH:MI AM') as \"createdTime\","
				+ "  adl.bed_id as \"bedId\", "
				+ "  adl.ward_id as \"wardId\", "
				+ "  adl.room_id as \"roomId\", "
				+ "  ward.ward_name as \"wardName\", "
                + "  floor.floor_name as \"floorName\", "
				+ "  adl.is_short_leave as \"isShortLeave\", "
				+ "  pr.patient_category_id as \"patientCategoryId\", "
				+ "  admission.concent as \"concent\", "
				+ "  medic.medico_legal_id as \"medicoLegalId\","
				+ "  disc.discharge_status_id as \"dischargeStatusId\", "
				+ "  disc.discharge_id as \"dischargeId\", "
				+ "  pr.is_vip as \"isVip\", "
				+ "  pr.mobile_number as \"mobileNumber\", " 
				+ "  pr.uuid as \"uuid\", "
				 + " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\", "
				+ "  prefxmstr.prefix_desc as \"prefixDesc\",pr.image_path as \"imagePath\" , "
				+ "  (case when pr.is_vip='Y' then "
				+ "  coalesce(pr.alise_name,pr.first_name) "
				+ "  else "
				+ "  concat(coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name)) "
				+ "  end) as \"pFirstName\", "
				+ "  (case when (select total_score from adt.t_mews_mapper where admission_id = admission.admission_id and status='A') > 6 then "
				+ "  	1 "
				+ "   else "
				+ "     0 end) as \"isCriticalOrder\","
				+ " admission.reference_number as \"referenceNumber\","
				+ " bedCatMst.is_icu as \"isIcuu\" "
				+ "FROM "
				+ "  adt.t_admission admission "
				+ "  INNER JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id"
				+ "  INNER JOIN public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
				+ "  INNER JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
				+ "  INNER JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
				+ "  INNER JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id "
				+ "  INNER JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id "
				+ "  INNER JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id "
				+ "  INNER JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
	            + "  INNER JOIN adt.m_floor_master floor on ward.floor_id = floor.floor_id "
				+ "  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 and spd.status='A' "
				+ "  INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
				+ "  INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
				+ "  LEFT JOIN adt.t_discharge disc on admission.admission_id = disc.admission_id and disc.status='A' "
				+ "  LEFT JOIN adt.t_medico_legal medic on admission.admission_id = medic.admission_id "
				+ "WHERE "
				+ "  admission.status = 'A' "
				+ " AND adl.status='A' "
				+ " AND adl.nursing_acceptance = 'Y' "
				+ " AND admission.organization_id = :organizationId "
				+ " AND admission.unit_id = :unitId "
				+ " AND admission.visit_type_id=:visitTypeId "
				+ " AND admission.patient_id = :patientId "
				+ " AND admission.admission_id = :admissionId "
				+ " AND admission.doctor_id = :doctorId "),
		
		@NamedNativeQuery(name = "GET_LIST_OF_DAY_CARE_PATIENTS_FOR_NURSING", 
		query ="SELECT "
				+ "  admission.admission_id as \"admissionId\", "
				+ "  adl.admission_details_id as \"admissionDetailsId\", "
				+ "  admission.patient_id as \"patientId\", "
				+ "  admission.t_patient_id as \"tPatientId\", "
				+ "  admission.admission_number as \"admissionNumber\", "
				+ "  admission.visit_type_id as \"visitTypeId\", "
				+ "  admission.doctor_id as \"doctorId\", "
				+ "  admission.speciality_id as \"specialityId\", "
				+ "  sp.speciality_name as \"specialityName\", "
				+ "	 pr.prefix_id as \"prefixId\", "
				+ "  pr.uhid_number as \"UHIDNumber\", "
				+ "  concat(COALESCE(pr.first_name,''),' ',COALESCE(pr.middle_name,''),' ',COALESCE(pr.last_name,'')) as \"pFirstName\", "
				+ "  pr.gender_id as \"genderId\", "
				+ "  gm.gender_code as \"genderCode\", "
				+ "  to_char(pr.dob, 'MM/DD/YYYY') as \"dob\", "
				+ "  concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
				+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"doa\", "
				+ "  to_char(adl.pdd, 'DD/MM/YYYY') as \"pdd\", "
				+ "  spd.payment_entitlement_id as \"paymentEntitlementId\", "
				+ "  spd.contract_id as \"contractId\", "
				+ "  spd.payee_id as \"payeeId\", "
				+ "  pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
				+ "  bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
				+ "  bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
				+ "  bedCatMst.bed_category_id as \"bedCategoryId\", "
				+ "  bm.floor_id as \"floorId\", "
				+ "  bm.bed_number as \"bedNumber\", "
				+ "  adl.bed_id as \"bedId\", "
				+ "  adl.ward_id as \"wardId\", "
				+ "  adl.room_id as \"roomId\", "
				+ "  ward.ward_name as \"wardName\", "
				+ "  adl.is_short_leave as \"isShortLeave\", "
				+ "  pr.patient_category_id as \"patientCategoryId\", "
				+ "  admission.concent as \"concent\", "
				+ "  disc.discharge_id as \"dischargeId\", "
				+ "  disc.discharge_status_id as \"dischargeStatusId\", "
				+ " note.encounter_id as \"encounterId\", "
				+ " note.visit_type_id as \"admissionNoteVisitType\","
				+" prefxmstr.prefix_code as \"prefixDesc\",pr.image_path as \"imagePath\" ,"
	            + " concat(to_char(extract(year from age(current_timestamp, pr.dob)), '999'), 'Y') as \"ageStr\", "
	            + " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageString\" "
				+ "FROM "
				+ "  adt.t_admission admission "
				+ "  INNER JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id "
				+ "  INNER JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
				+ "  INNER JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
				+ "  INNER JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id "
				+ "  INNER JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id "
				+ "  INNER JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id "
				+ "  INNER JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
				+ "  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 and spd.status='A' "
				+ "  LEFT JOIN adt.t_discharge disc on admission.admission_id = disc.admission_id "
				+ "  INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
				+ "  INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
				+ " left join adt.t_admission_note note on note.admission_id = adl.admission_id "
				 + "  left join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
				+ "WHERE "
				+ "  admission.status = 'A' "
				+ " AND adl.status='A' "
			    + " AND adl.nursing_acceptance = 'P' "
				+ "  AND admission.organization_id = :organizationId "
				+ "  AND admission.unit_id = :unitId "
				+ "  and admission.visit_type_id=3 "
				+ " AND adl.ward_id IN (:wardList) " 
				+ " order by admission.admission_id desc " 
			),
		
		@NamedNativeQuery(name = "GET_LIST_OF_DAY_CARE_NURSING_ACCEPTED", 
		query ="SELECT "
				+ "  admission.admission_id as \"admissionId\", "
				+ "  admission.patient_id as \"patientId\", "
				+ "  admission.t_patient_id as \"tPatientId\", "
				+ "  admission.admission_number as \"admissionNumber\", "
				+ "  admission.visit_type_id as \"visitTypeId\", "
				+ "  admission.doctor_id as \"doctorId\", "
				+ "  admission.speciality_id as \"specialityId\", "
				+ "  sp.speciality_name as \"specialityName\", "
//				+ "	 pr.prefix_id as \"prefixId\", "
				+ "	 pr.prefix_id as \"prefixId\", "
				+ "  pr.uhid_number as \"UHIDNumber\", "
				+ "  concat(COALESCE(pr.first_name,''),' ',COALESCE(pr.middle_name,''),' ',COALESCE(pr.last_name,'')) as \"pFirstName\", "
				+ "  pr.gender_id as \"genderId\", "
				+ "  gm.gender_code as \"genderCode\", "
				+ "  to_char(pr.dob, 'MM/DD/YYYY') as \"dob\", "
				+ "  concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
				+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"doa\", "
				+ "  to_char(adl.pdd, 'DD/MM/YYYY') as \"pdd\", "
				+ "  spd.payment_entitlement_id as \"paymentEntitlementId\", "
				+ "  spd.contract_id as \"contractId\", "
				+ "  spd.payee_id as \"payeeId\", "
				+ "  pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
				+ "  bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
				+ "  bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
				+ "  bedCatMst.bed_category_id as \"bedCategoryId\", "
				+ "  bm.floor_id as \"floorId\", "
				+ "  bm.bed_number as \"bedNumber\", "
				+ "  adl.bed_id as \"bedId\", "
				+ "  adl.ward_id as \"wardId\", "
				+ "  adl.room_id as \"roomId\", "
				+ "  ward.ward_name as \"wardName\", "
				+ "  adl.is_short_leave as \"isShortLeave\", "
				+ "  pr.patient_category_id as \"patientCategoryId\", "
				+ "  admission.concent as \"concent\", "
				+ "  disc.discharge_id as \"dischargeId\", "
				+ "  disc.discharge_status_id as \"dischargeStatusId\","
				+ " medic.medico_legal_id as \"medicoLegalId\","
	            +" prefxmstr.prefix_code as \"prefixDesc\",pr.image_path as \"imagePath\" ,"
	            + " concat(to_char(extract(year from age(current_timestamp, pr.dob)), '999'), 'Y') as \"ageStr\", "
	            + " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageString\" "

				+ "FROM "
				+ "  adt.t_admission admission "
				+ "  INNER JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id "
				+ "  INNER JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
				+ "  INNER JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
				+ "  INNER JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id "
				+ "  INNER JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id "
				+ "  INNER JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id "
				+ "  INNER JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
				+ "  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 and spd.status='A' "
				+ "  LEFT JOIN adt.t_discharge disc on admission.admission_id = disc.admission_id "
				+ "  INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
				+ "  INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
				+ "   LEFT JOIN adt.t_medico_legal medic on admission.admission_id = medic.admission_id "
	            + "  left join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "

				+ "WHERE "
				+ "  admission.status = 'A' "
				+ " AND adl.status='A' "
				+ " AND adl.nursing_acceptance = 'Y' "
				+ "  AND admission.organization_id = :organizationId "
				+ "  AND admission.unit_id = :unitId "
				+ "  and admission.visit_type_id=3 "
				+ " AND adl.ward_id IN (:wardList) " 
				+ " order by admission.admission_id desc " 
			),
		@NamedNativeQuery(name = "GET_ADMITTED_PATIENT_LIST_FOR_PAGINATION", 
		query ="SELECT "
				+ "  admission.admission_id as \"admissionId\", "
				+ "  admission.patient_id as \"patientId\", "
				+ "  admission.t_patient_id as \"tPatientId\", "
				+ "  admission.admission_number as \"admissionNumber\", "
				+ "  admission.visit_type_id as \"visitTypeId\", "
				+ "  admission.doctor_id as \"doctorId\", "
				+ "  admission.speciality_id as \"specialityId\", "
				+ "  (select obgyn_speciality_id from t_unit_application_billing_configuration appunit where appunit.obgyn_speciality_id=admission.speciality_id and appunit.unit_id = :unitId ) as \"obgynSpecialityId\","
				+ "  sp.speciality_name as \"specialityName\", "
				+ "  pr.uhid_number as \"uhidnumber\", "
				+ "	 pr.prefix_id as \"prefixId\", "
				+ "  prefix.prefix_code as \"prefixCode\", "
				+ "  pr.gender_id as \"genderId\", "
				+ "  gm.gender_code as \"genderCode\", "
				+ "  to_char(pr.dob, 'MM/DD/YYYY') as \"dob\", "
				+ "  concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"doctorFullName\", "
				+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"datepicker\", "
				+ "  to_char(adl.pdd, 'DD/MM/YYYY') as \"datepickerPdd\", "
				+ "  spd.payment_entitlement_id as \"paymentEntitlementId\", "
				+ "  spd.contract_id as \"contractId\", "
				+ "  spd.payee_id as \"payeeId\", "
				+ "  pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
				+ "  bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
				+ "  bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
				+ "  bedCatMst.bed_category_id as \"bedCategoryId\", "
				+ "  bm.floor_id as \"floorId\", "
				+ "  bm.bed_number as \"bedNumber\", "
				+ "  adl.bed_id as \"bedId\", "
				+ "  adl.ward_id as \"wardId\", "
				+ "  adl.room_id as \"roomId\", "
				+ "  adl.diet_type as \"dietTypeId\", "
				+ "  adl.meal_pref_id as \"mealPrefId\", "
				+ "  adl.remark_for_diet as \"dietRemarkDesc\", "
				+ "  ward.ward_name as \"wardName\", "
	            + "  floor.floor_name as \"floorName\", "
				+ "  adl.is_short_leave as \"isShortLeave\", "
				+ "  pr.patient_category_id as \"patientCategoryId\", "
				+ "  admission.concent as \"concent\", "
				+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"createdDate\", "
				+ "  to_char(adl.doa, 'HH:MI AM') as \"createdTime\","
				+ "  false as \"isBarCodeSelected\" ,"
				+ "  adl.admission_type_id as \"admissionTypeId\", "
				+ "  pr.is_vip as \"isVip\", "
				+ "  concat(coalesce(prefix.prefix_code,''),' ',coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name)) as \"patientFullName\", "
				+ "  (case when (select total_score from adt.t_mews_mapper where admission_id = admission.admission_id and status='A') > 6 then "
				+ "  	1 "
				+ "   else "
				+ "     0 end) as \"isCriticalOrder\", "
                + " admission.reference_number as \"referenceNumber\",com.company_desc as \"companyName\","
                + " tdd.discharge_id  as \"dischargeId\" "
				+ "FROM "
				+ "  adt.t_admission admission "
				+ "  INNER JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id "
				+ "  INNER JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
				+ "  INNER JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
				+ "  INNER JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id "
				+ "  INNER JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id "
				+ "  INNER JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id "
				+ "  INNER JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
	            + "  INNER JOIN adt.m_floor_master floor on ward.floor_id = floor.floor_id "
				+ "  INNER JOIN  public.m_prefix_master prefix ON  prefix.prefix_id = pr.prefix_id "
				+ "  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 AND spd.status='A' "
				+ "  INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
				+ "  INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id"
				+ "  LEFT JOIN public.t_company_master com on com.company_id=spd.payee_id  "
				+ " left join adt.t_discharge tdd on tdd.admission_id =  admission.admission_id and tdd.status = 'A' "
				+ "WHERE "
				+ "  admission.status = 'A' "
				+ "  AND adl.status='A' "
				+ "  AND adl.nursing_acceptance = 'Y' "
				+ "  AND admission.organization_id = :organizationId "
				+ "  AND admission.unit_id = :unitId "
				+ " order by \"admissionId\" desc " 
			),
		
		@NamedNativeQuery(name = "GET_ADMITTED_DETAILS_PATIENT_GET_BY_ID", 
		query = " select " + 
				"	distinct(admission.admission_id)as \"admissionId\", " + 
				"	admission.reason_id as \"reasonId\", " + 
				"	admission.patient_class_id as \"patientClassId\", " + 
				"	to_char(adl.pdd,'DD/MM/YYYY') as \"datepickerPdd\", " + 
				"	adl.admission_type_id as \"admissionTypeId\", " + 
				"	adl.meal_pref_id as \"mealPrefId\", " + 
				"	adl.attendent_meal_pref_id as \"attendentMealPrefId\", " + 
				"	adl.admission_details_id as \"admissionDetailsId\", " + 
				"	admission.reference_number as \"referenceNumber\", " + 
				"	refdet.referal_details_id as \"referalDetailsId\", " + 
				"	refdet.referral_type_id as \"referralTypeId\", " + 
				"	refdet.referral_id as \"referralId\", " + 
				"	refdet.referral_remark  as \"referralRemark\", " + 
				"	refmast.referral_name as \"referralName\", " + 
				"   (doc.first_name || ' ' || doc.middle_name || ' ' || doc.last_name) as \"treatingDoctorName\" , "+
				"   (docc.first_name || ' ' || docc.middle_name || ' ' || docc.last_name) as \"admittingDoctorName\", "+
	            "   spec.speciality_name as \"specialityName\", "+
				"   specc.speciality_name as \"admittingSpecialityName\", "+
				"   doc.doctor_id as \"treatingDoctorId\" , "+    
				"   admission.admitting_doctor_id as \"admittingDoctorId\" , "+
				"   spec.speciality_id as \"treatingDoctorSpecialityId\", "+
				"   admission.admitting_speciality_id as \"admittingSpecialityId\" "+
				" from " +   
				"	adt.t_admission admission "  + 
				" inner join patient.t_patient_registration pr on " + 
				"	admission.patient_id = pr.patient_id " + 
				" inner join public.m_gender_master gm on " + 
				"	pr.gender_id = gm.gender_id " + 
				" inner join doctor.m_doctor_master doc on " + 
				"	admission.doctor_id = doc.doctor_id " + 
				" inner join doctor.m_speciality_master spec on " +
				" admission.speciality_id = spec.speciality_id  " +
				" left join doctor.m_speciality_master specc on "+
				" admission.admitting_speciality_id = specc.speciality_id "+
				" left join doctor.m_doctor_master docc on  " +
				" admission.admitting_doctor_id = docc.doctor_id " +
				" inner join adt.t_admission_details adl on " + 
				"	adl.admission_id = admission.admission_id " + 
				" left join public.t_referal_details refdet on " + 
				"	refdet.admission_id = admission.admission_id "
				+ " and refdet.status = 'A'" + 
				" left join adt.m_referral_master refmast on " + 
				"	refmast.referral_id = refdet.referral_id " 
				+ " and refmast.status ='A'"+ 
				" where " + 
				"	admission.status = 'A' " + 
				"	and adl.status = 'A' " + 
				"	and adl.nursing_acceptance = 'Y' " + 
				"	and admission.organization_id =:organizationId " + 
				"	and admission.unit_id =:unitId" + 
				"	and admission.admission_id =:admissionId"  
				+ " order by \"admissionId\" desc " 
			),
		
		@NamedNativeQuery(name = "GET_PAGINATED_ADMITTED_PATIENT_DETAILS_LIST_SEARCH", 
		query ="SELECT "
				+ "  admission.admission_id as \"admissionId\", "
				+ "  admission.patient_id as \"patientId\", "
				+ "  admission.t_patient_id as \"tPatientId\", "
				+ "  admission.admission_number as \"admissionNumber\", "
				+ "  admission.visit_type_id as \"visitTypeId\", "
				+ "  admission.doctor_id as \"doctorId\", "
				+ "  admission.speciality_id as \"specialityId\", "
	            + " admission.is_final_bill as \"isFinalBill\", "
				+ "  sp.speciality_name as \"specialityName\", "
				+ "  pr.uhid_number as \"UHIDNumber\", "
				+ "  pr.gender_id as \"genderId\", "
				+ "  gm.gender_code as \"genderCode\", "
				+ "  to_char(pr.dob, 'MM/DD/YYYY') as \"dob\", "
				+ " bedCatMst.is_icu as \"isIcuu\", "
				+ "  concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
				+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"doa\", "
				+ "  to_char(adl.pdd, 'DD/MM/YYYY') as \"pdd\", "
				+ "  spd.payment_entitlement_id as \"paymentEntitlementId\", "
				+ "  spd.contract_id as \"contractId\", "
				+ "  spd.payee_id as \"payeeId\", "
				+ "  pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
				+ "  bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
				+ "  bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
				+ "  bedCatMst.bed_category_id as \"bedCategoryId\", "
				+ "  bm.floor_id as \"floorId\", "
				+ "  bm.bed_number as \"bedNumber\", "
				+ "  adl.bed_id as \"bedId\", "
				+ "  adl.ward_id as \"wardId\", "
				+ "  adl.room_id as \"roomId\", "
				+ "  to_char(adl.doa, 'HH:MI AM') as \"createdTime\","
				+ "  ward.ward_name as \"wardName\", "
				+ "  floor.floor_name as \"floorName\", "
				+ "  adl.is_short_leave as \"isShortLeave\", "
				+ "  pr.patient_category_id as \"patientCategoryId\", "
				+ "  pr.uuid as \"uuid\", "
				+ "  admission.concent as \"concent\", "
				+ "  disc.discharge_status_id as \"dischargeStatusId\", "
				+ "  pr.is_vip as \"isVip\", "
				+ " medleg.medico_legal_id as \"medicoLegalId\", "
				+ "  (case when pr.is_vip='Y' then "
				+ "  coalesce(pr.alise_name,pr.first_name) "
				+ "  else "
				+ "  concat(coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name)) "
				+ "  end) as \"pFirstName\", "
				+ "  (case when (select total_score from adt.t_mews_mapper where admission_id = admission.admission_id and status='A') > 6 then "
				+ "  	1 "
				+ "   else "
				+ "     0 end) as \"isCriticalOrder\",disc.discharge_id as \"dischargeId\", "
				+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\", "
	            + " prefxmstr.prefix_code as \"prefixDesc\",pr.image_path as \"imagePath\" , "
                + " admission.reference_number as \"referenceNumber\" "
				+ "FROM "
				+ "  adt.t_admission admission "
				+ "  INNER JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id "
	            + "  left join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
				+ "  INNER JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
				+ "  INNER JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
				+ "  INNER JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id "
				+ "  INNER JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id "
				+ "  INNER JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id "
				+ "  INNER JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
				+ "  INNER JOIN adt.m_floor_master floor on ward.floor_id = floor.floor_id "
				+ "  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 and spd.status='A' "
				+ "  INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
				+ "  INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
				+ "  left join adt.t_medico_legal medleg on " + 
				"	admission.admission_id = medleg.admission_id"
				+ "  LEFT JOIN adt.t_discharge disc on admission.admission_id = disc.admission_id and disc.status='A' "
				+ "WHERE "
				+ "  admission.status = 'A' "
				+ " AND adl.status='A' "
				+ " AND adl.nursing_acceptance = 'Y' "
				+ "  AND admission.organization_id = :organizationId "
				+ "  AND admission.unit_id = :unitId "
//				+ " AND admission.visit_type_id=2 "
				+ " AND bm.ward_id IN (:wardList) " 
				+ "  AND (lower(pr.uhid_number) like :patientName "
				+ "  OR lower(pr.first_name) like :patientName   OR lower(pr.middle_name) like :patientName OR lower(pr.last_name) like :patientName "
				+ "  OR lower(doc.first_name) like :patientName 	  OR lower(doc.middle_name) like :patientName OR lower(doc.last_name) like :patientName "
				+ "  OR lower(bm.bed_number) like :patientName "
				+ "  OR lower(to_char(adl.doa, 'DD/MM/YYYY') ) like :patientName "
				+ "  OR lower(to_char(adl.pdd, 'DD/MM/YYYY')) like :patientName "
				+ "  OR lower(pe.payment_entitlement_desc) like :patientName )"
				//+ " order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc "
			),

		@NamedNativeQuery(name = "GET_PAGINATED_ADMITTED_PATIENT_DETAILS_LIST_SEARCH_FOR_DIET_ORDER", 
		query ="SELECT "
				+ "  admission.admission_id as \"admissionId\", "
				+ "  admission.patient_id as \"patientId\", "
				+ "  admission.t_patient_id as \"tPatientId\", "
				+ "  admission.admission_number as \"admissionNumber\", "
				+ "  admission.visit_type_id as \"visitTypeId\", "
				+ "  admission.doctor_id as \"doctorId\", "
				+ "  admission.speciality_id as \"specialityId\", "
	            + " admission.is_final_bill as \"isFinalBill\", "
				+ "  sp.speciality_name as \"specialityName\", "
				+ "  pr.uhid_number as \"UHIDNumber\", "
				+ "  pr.gender_id as \"genderId\", "
				+ "  gm.gender_code as \"genderCode\", "
				+ "  to_char(pr.dob, 'MM/DD/YYYY') as \"dob\", "
				+ " bedCatMst.is_icu as \"isIcuu\", "
				+ "  concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
				+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"doa\", "
				+ "  to_char(adl.pdd, 'DD/MM/YYYY') as \"pdd\", "
				+ "  spd.payment_entitlement_id as \"paymentEntitlementId\", "
				+ "  spd.contract_id as \"contractId\", "
				+ "  spd.payee_id as \"payeeId\", "
				+ "  pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
				+ "  bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
				+ "  bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
				+ "  bedCatMst.bed_category_id as \"bedCategoryId\", "
				+ "  bm.floor_id as \"floorId\", "
				+ "  bm.bed_number as \"bedNumber\", "
				+ "  adl.bed_id as \"bedId\", "
				+ "  adl.ward_id as \"wardId\", "
				+ "  adl.room_id as \"roomId\", "
				+ "  to_char(adl.doa, 'HH:MI AM') as \"createdTime\","
				+ "  ward.ward_name as \"wardName\", "
				+ "  floor.floor_name as \"floorName\", "
				+ "  adl.is_short_leave as \"isShortLeave\", "
				+ "  pr.patient_category_id as \"patientCategoryId\", "
				+ "  pr.uuid as \"uuid\", "
				+ "  admission.concent as \"concent\", "
				+ "  disc.discharge_status_id as \"dischargeStatusId\", "
				+ "  pr.is_vip as \"isVip\", "
				+ " medleg.medico_legal_id as \"medicoLegalId\", "
				+ "  (case when pr.is_vip='Y' then "
				+ "  coalesce(pr.alise_name,pr.first_name) "
				+ "  else "
				+ "  concat(coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name)) "
				+ "  end) as \"pFirstName\", "
				+ "  (case when (select total_score from adt.t_mews_mapper where admission_id = admission.admission_id and status='A') > 6 then "
				+ "  	1 "
				+ "   else "
				+ "     0 end) as \"isCriticalOrder\",disc.discharge_id as \"dischargeId\", "
				+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\", "
	            + " prefxmstr.prefix_code as \"prefixDesc\",pr.image_path as \"imagePath\" , "
                + " admission.reference_number as \"referenceNumber\" "
				+ "FROM "
				+ "  adt.t_admission admission "
				+ "  INNER JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id "
	            + "  left join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
				+ "  INNER JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
				+ "  LEFT JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
				+ "  LEFT JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id "
				+ "  INNER JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id "
				+ "  INNER JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id "
				+ "  INNER JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
				+ "  INNER JOIN adt.m_floor_master floor on ward.floor_id = floor.floor_id "
				+ "  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 and spd.status='A' "
				+ "  INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
				+ "  INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
				+ "  left join adt.t_medico_legal medleg on " + 
				"	admission.admission_id = medleg.admission_id"
				+ "  LEFT JOIN adt.t_discharge disc on admission.admission_id = disc.admission_id and disc.status='A' "
				+ "WHERE "
				+ "  admission.status = 'A' "
				+ " AND adl.status='A' "
				+ " AND adl.nursing_acceptance = 'Y' "
				+ "  AND admission.organization_id = :organizationId "
				+ "  AND admission.unit_id = :unitId "
				+ " AND bm.ward_id IN (:wardList) " 
				+ "AND (lower(pr.uhid_number) LIKE lower(:patientName)     "
				+ "OR lower(pr.first_name) LIKE lower(:patientName)     "
				+ "OR lower(pr.middle_name) LIKE lower(:patientName)     "
				+ "OR lower(pr.last_name) LIKE lower(:patientName)     "
				+ "OR lower(doc.first_name) LIKE lower(:patientName)     "
				+ "OR lower(doc.middle_name) LIKE lower(:patientName)     "
				+ "OR lower(doc.last_name) LIKE lower(:patientName)     "
				+ "OR lower(bm.bed_number) LIKE lower(:patientName)     "
				+ "OR lower(to_char(adl.doa, 'DD/MM/YYYY')) LIKE lower(:patientName)     "
				+ "OR lower(to_char(adl.pdd, 'DD/MM/YYYY')) LIKE lower(:patientName)     "
				+ "OR lower(pe.payment_entitlement_desc) LIKE lower(:patientName) )"
			),
		
		
		@NamedNativeQuery(name="UNIVERSAL_SEARCH_FOR_PATIENT_ACCEPTANCE_LIST_FOR_NURSING",query="SELECT "
				+ "  admission.admission_id as \"admissionId\", "
				+ "  adl.admission_details_id as \"admissionDetailsId\", "
				+ "  admission.patient_id as \"patientId\", "
				+ "  admission.admission_number as \"admissionNumber\", "
				+ "  admission.visit_type_id as \"visitTypeId\", "
				+ "  admission.doctor_id as \"doctorId\", "
				+ "  admission.speciality_id as \"specialityId\", "
				+ "  sp.speciality_name as \"specialityName\", "
				+ "  pr.uhid_number as \"uhidNumber\", "
				+ "  pr.gender_id as \"genderId\", "
				+ "  gm.gender_code as \"genderCode\", "
				+ "  to_char(pr.dob, 'MM/DD/YYYY') as \"dob\", "
				+ "  concat('Dr. ',' ',COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"doctorName\", "
				+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"doa\", "
				+ "  to_char(adl.pdd, 'DD/MM/YYYY') as \"pdd\", "
				+ "  spd.payment_entitlement_id as \"paymentEntitlementId\", "
				+ "  spd.contract_id as \"contractId\", "
				+ "  spd.payee_id as \"payeeId\", "
				+ "  pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
				+ "  bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
				+ "  bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
				+ "  bedCatMst.bed_category_id as \"bedCategoryId\", "
				+ "  bm.floor_id as \"floorId\", "
				+ "  bm.bed_number as \"bedNumber\", "
				+ "  adl.bed_id as \"bedId\", "
				+ "  adl.ward_id as \"wardId\", "
				//+ "  adl.room_id as \"roomId\", "
				+ "  ward.ward_name as \"wardName\", "
	            + "  floor.floor_name as \"floorName\", "
				+ "  adl.is_short_leave as \"isShortLeave\", "
				+ "  pr.patient_category_id as \"patientCategoryId\", "
				+ "  disc.discharge_id as \"dischargeId\", "
				+ "  disc.discharge_status_id as \"dischargeStatusId\", "
				+ "  pr.is_vip as \"isVip\", "
				+ "  (case when pr.is_vip='Y' then "
				+ "    coalesce(pr.alise_name,pr.first_name) "
				+ "    else "
				+ "    concat(coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name)) "
				+ "  end) as \"patientName\", "
				+ "  (case when (select total_score from adt.t_mews_mapper where admission_id = admission.admission_id and status='A') > 6 then "
				+ "  	1 "
				+ "   else "
				+ "     0 end) as \"isCriticalOrder\","
				+ " note.encounter_id as \"encounterId\", "
				+ " note.visit_type_id as \"admissionNoteVisitType\", "
				+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\", "
	            + " prefxmstr.prefix_code as \"prefixDesc\",pr.image_path as \"imagePath\"  "

				+ "FROM "
				+ "  adt.t_admission admission "
				+ "  INNER JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id "
	            + "  left join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
				+ "  INNER JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
				+ "  INNER JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
				+ "  INNER JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id "
				+ "  INNER JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id "
				+ "  INNER JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id "
				+ "  INNER JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
	            + "  INNER JOIN adt.m_floor_master floor on ward.floor_id = floor.floor_id "
				+ "  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 and spd.status='A' "
				+ "  LEFT JOIN adt.t_discharge disc on admission.admission_id = disc.admission_id "
				+ "  INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
				+ "  INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
				+ " left join adt.t_admission_note note on note.admission_id = adl.admission_id "
				+ "WHERE "
				+ "  admission.status = 'A' "
				+ " AND adl.status='A' "
				+ " AND adl.nursing_acceptance = 'P' "
				+ "  AND admission.organization_id = :orgId "
				+ "  AND admission.unit_id = :unitId "
				+ "	 AND adl.ward_id IN (:wardList) "
				+ "  AND (lower(pr.uhid_number) like :patientName "
				+ "  OR lower(pr.first_name) like :patientName   OR lower(pr.middle_name) like :patientName OR lower(pr.last_name) like :patientName "
				+ "  OR lower(doc.first_name) like :patientName 	  OR lower(doc.middle_name) like :patientName OR lower(doc.last_name) like :patientName "
				+ "  OR lower(bm.bed_number) like :patientName "
				+ "  OR lower(ward.ward_name) like :patientName "
				+ "  OR lower(bedCatMst.bed_category_desc) like :patientName "
				+ "  OR lower(pe.payment_entitlement_desc) like :patientName )"),
		
		@NamedNativeQuery(name="GET_ADMITTED_PATIENT_LIST_FOR_VITAL_UPLOADER",query="SELECT " + 
				"  admission.admission_id as \"admissionId\","+
			    "  admission.organization_id as \"organizationId\",admission.unit_id as \"unitId\"," + 
				"  admission.patient_id as \"patientId\", " + 
				"  admission.admission_number as \"admissionNumber\", " + 
				"  admission.visit_type_id as \"visitTypeId\", " + 
				"  admission.doctor_id as \"doctorId\", " + 
				"  admission.speciality_id as \"specialityId\", " + 
				"  sp.speciality_name as \"specialityName\", " + 
				"	 pr.prefix_id as \"prefixId\", " + 
				"  pr.uhid_number as \"uhidnumber\", " + 
				"  pr.is_vip as \"isVip\", " + 
				"  (case when pr.is_vip='Y' then " + 
				"  coalesce(pr.alise_name,pr.first_name) " + 
				"  else " + 
				"  concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name)) " + 
				"  end) as \"pFirstName\", " + 
				"  pr.gender_id as \"genderId\", " + 
				"  gm.gender_code as \"genderCode\", " + 
				"  to_char(pr.dob, 'MM/DD/YYYY') as \"dob\", " + 
				" concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))) as \"ageYear\", " + 
				"  concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", " + 
				"  to_char(adl.doa, 'DD/MM/YYYY') as \"doa\", " + 
				"  to_char(adl.pdd, 'DD/MM/YYYY') as \"pdd\", " + 
				"  spd.payment_entitlement_id as \"paymentEntitlementId\", " + 
				"  spd.payee_id as \"payeeId\", " + 
				"  pe.payment_entitlement_desc as \"paymentEntitlementDesc\", " + 
				"  bedCatMst.bed_category_desc as \"bedCategoryDesc\", " + 
				"  adl.billing_bed_category_id as \"billingBedCategoryId\", " + 
				"  bedCatMst.bed_category_id as \"bedCategoryId\", " + 
				"  bm.floor_id as \"floorId\", " + 
				"  bm.bed_number as \"bedNumber\", " + 
				"  bm.bed_id as \"bedId\", " + 
				"  bm.ward_id as \"wardId\", " + 
				"  ward.ward_name as \"wardName\", " + 
				"  adl.is_short_leave as \"isShortLeave\", " + 
				"  pr.patient_category_id as \"patientCategoryId\", " + 
				"  disc.discharge_id as \"dischargeId\", " + 
				"  disc.discharge_status_id as \"dischargeStatusId\", " + 
				"  to_char(adl.doa, 'DD/MM/YYYY') as \"createdDate\", " + 
				"  to_char(adl.doa, 'HH:MI AM') as \"createdTime\", " + 
				"  (case when (select total_score from adt.t_mews_mapper where admission_id = admission.admission_id and status='A') > 6 then " + 
				"  	1 " + 
				"   else " + 
				"     0 end) as \"isCriticalOrder\", " + 
				" concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\" " + 
				"FROM " + 
				"  adt.t_admission admission " + 
				"  INNER JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id " + 
				"  INNER JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id " + 
				 "  left join m_prefix_master pre on pre.prefix_id = pr.prefix_id " +
				"  left JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id " + 
				"  left JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id " + 
				"  INNER JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id " + 
				"  INNER JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id " + 
				"  INNER JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id " + 
				"  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 and spd.status='A' " + 
				"  LEFT JOIN adt.t_discharge disc on admission.admission_id = disc.admission_id and disc.status = 'A' " + 
				"  left join adt.t_medico_legal medleg on admission.admission_id = medleg.admission_id " + 
				"  INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id " + 
				"  INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id " + 
				"WHERE " + 
				"  admission.status = 'A' " + 
				"  AND adl.status='A' " + 
				"  AND adl.nursing_acceptance = 'Y' " + 
				"  AND admission.organization_id = :organizationId " + 
				"  AND admission.unit_id = :unitId "),
				//"  AND admission.doctor_id=:docId " + 
				/*" and (admission.doctor_id =:docId " + 
				" OR (((case when (select specMa.speciality_id from doctor.t_doctor_speciality_mapper specMa where specMa.doctor_id=:docId " + 
				" and  specMa.speciality_id=( " + 
				" select app.er_speciality_id from t_unit_application_billing_configuration app "
				+ " where app.organization_id=:organizationId and app.unit_id=:unitId and status='A' ))>0 " + 
				" then true else false end) and admission.visit_type_id=4))) "+*/
				//"  AND admission.doctor_id=:docId " + 
				//" order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc "),
		
		@NamedNativeQuery(name = "GET_ADMITTED_PATIENT_LIST_DOCTOR_WISE_FOR_DOCTOR_APP", 
		query ="SELECT "
				+ "  admission.admission_id as \"admissionId\", "
				+ " (select coalesce(bm.bill_status_id,0) from billing.m_billing_master bm where bm.is_interim_or_final_bill = 2 and  bm.admission_id =admission.admission_id and bm.bill_cancelled = 'N' order by bm.billing_master_id limit 1  ) as \"billStatusId\" ,"
				+ "  admission.patient_id as \"patientId\", "
				+ "  admission.t_patient_id as \"tPatientId\", "
				+ "  admission.admission_number as \"admissionNumber\", "
				+ "  admission.visit_type_id as \"visitTypeId\", "
				+ "  admission.doctor_id as \"doctorId\", "
				+ "  admission.speciality_id as \"specialityId\", "
				+ "  sp.speciality_name as \"specialityName\", "
				+ "	 pr.prefix_id as \"prefixId\", "
				+ "  pr.uhid_number as \"UHIDNumber\", "
				+ "  pr.is_vip as \"isVip\", "
				+ "  (case when pr.is_vip='Y' then "
				+ "  coalesce(pr.alise_name,pr.first_name) "
				+ "  else "
				+ "  concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name)) "
				+ "  end) as \"pFirstName\", "
				+ "  pr.gender_id as \"genderId\", "
				+ "  pr.mpshah_patient_id as \"mpshahPatientId\", "
				+ "  gm.gender_code as \"genderCode\", "
				+ "  to_char(pr.dob, 'MM/DD/YYYY') as \"dob\", "
				+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))) as \"ageYear\", "
				+ "  concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
				+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"doa\", "
				+ "  to_char(adl.pdd, 'DD/MM/YYYY') as \"pdd\", "
				+ "  spd.payment_entitlement_id as \"paymentEntitlementId\", "
				+ "  spd.contract_id as \"contractId\", "
				+ "  spd.payee_id as \"payeeId\", "
				+ "  pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
				+ "  bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
				+ "  adl.billing_bed_category_id as \"billingBedCategoryId\", "
				+ "  bedCatMst.bed_category_id as \"bedCategoryId\", "
				+ "  bm.floor_id as \"floorId\", "
				+ "  bm.bed_number as \"bedNumber\", "
				+ "  adl.bed_id as \"bedId\", "
				+ "  adl.ward_id as \"wardId\", "
				+ "  adl.room_id as \"roomId\", "
				+ "  ward.ward_name as \"wardName\", "
				+ "  adl.is_short_leave as \"isShortLeave\", "
				+ "  pr.patient_category_id as \"patientCategoryId\", "
				+ "  pr.uuid as \"uuid\", "
				+ "  admission.concent as \"concent\", "
				+ "  disc.discharge_id as \"dischargeId\", "
				+ "  disc.discharge_status_id as \"dischargeStatusId\", "
				+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"createdDate\", "
				+ "  to_char(adl.doa, 'HH:MI AM') as \"createdTime\", "
				+ "  medleg.medico_legal_id as \"medicoLegalId\", "
				+ "pr.email as \"email\","
				+ "  (case when (select total_score from adt.t_mews_mapper where admission_id = admission.admission_id and status='A') > 6 then "
				+ "  	1 "
				+ "   else "
				+ "     0 end) as \"isCriticalOrder\", "
				+ " 	pr.mobile_number as \"mobileNo\", " 
				+ " 	pr.image_path as \"imagePath\", " 
				+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\" "

				+ "FROM "
				+ "  adt.t_admission admission "
				+ "  INNER JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id "
				+ "  INNER JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
				+ "  left join m_prefix_master pre on pre.prefix_id = pr.prefix_id "
				+ "  INNER JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
				+ "  INNER JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id "
				+ "  INNER JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id "
				+ "  INNER JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id "
				+ "  INNER JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
				+ "  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 and spd.status='A' "
				+ "  LEFT JOIN adt.t_discharge disc on admission.admission_id = disc.admission_id and disc.status = 'A' "
				+ "  left join adt.t_medico_legal medleg on admission.admission_id = medleg.admission_id "
				+ "  INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
				+ "  INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
				+ "WHERE "
				+ "  admission.status = 'A' "
				+ "  AND adl.status='A' "
				+ "  AND adl.nursing_acceptance = 'Y' "
				+ "  AND admission.organization_id = :organizationId "
				+ "  AND admission.unit_id = :unitId "
				+ "  AND admission.doctor_id=:docId "
			)
})

@Entity
@Table(name = "t_admission", schema = "adt")
@SequenceGenerator(name = "admission_seq", sequenceName = "adt.admission_seq", allocationSize = 1)
public class Admission {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admission_seq")
	@Column(name = "admission_id")
	private Integer admissionId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "patient_id")
	private Integer patientId;

	@Column(name = "t_patient_id")
	private Integer tPatientId;

	@Column(name = "admission_number")
	private String admissionNumber;

	@Column(name = "visit_type_id")
	private Integer visitTypeId;

	@Column(name = "against_visit_id")
	private Integer againstVisitId;

	@Column(name = "doctor_id")
	private Integer doctorId;

	@Column(name = "speciality_id")
	private Integer specialityId;

	@Column(name = "status")
	private Character status;

	@Column(name = "created_date",updatable = false)
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name="kin_id")
	private Integer kinDetailsId;
	
	@Column(name="is_financial_creance")
	private Character isFinancialCreance;
	
	@Column(name = "admitting_doctor_id")
	private Integer admittingDoctorId;

	@Column(name = "admitting_speciality_id")
	private Integer admittingSpecialityId;
	
	@Column(name="reason_id")
	private Integer reasonId;
	
	@Column(name = "concent")
	private String concent;
	
	@Column(name="is_final_bill")
	private Character isFinalBill;
	
	@Column(name="patient_class_id")
	private Integer patientClassId;
	
	@Column(name="delivery_flag")
	private Integer deliveryFlag;
	
	@Column(name="ot_status")
	private Integer otStatus;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", insertable = false, nullable = false, updatable = false)
	private OrganizationMaster organizationMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unit_id", insertable = false, nullable = false, updatable = false)
	private UnitMaster unitMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, nullable = false, updatable = false)
	private PatientRegistration patientRegistration;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "t_patient_id", insertable = false, nullable = false, updatable = false)
	private UnknownPatientRegistration unknownPatientRegistration;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "visit_type_id", insertable = false, nullable = false, updatable = false)
	private VisitTypeMaster visitTypeMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "against_visit_id", insertable = false, nullable = false, updatable = false)
	private VisitTypeMaster againstVisitTypeMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id", insertable = false, nullable = false, updatable = false)
	private DoctorMaster doctorMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "speciality_id", insertable = false, nullable = false, updatable = false)
	private SpecialityMaster specialityMaster;
	
	@Column(name = "reference_number")
	private String referenceNumber;
	
	@Column(name = "payment_entitlement_id")
	private Integer paymentEntitlementId;
	
	@Column(name = "is_admission_cancelled")
	private Character isAdmissionCancelled;

	@Column(name = "admission_cancelled_by")
	private Integer admissionCancelledBy;

	@Column(name = "admission_cancelled_date_time")
	private Date admissionCancelledDateTime;

	@Column(name = "legacy_visit_id")
	private String legacyVisitId;


	@OneToMany(fetch = FetchType.LAZY, mappedBy = "admission", orphanRemoval = true)
	private List<AdmissionDetails> admissionDetailsList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "admission", orphanRemoval = true)
	private List<AdmissionNote> admissionNotesList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "admission", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VisitorAgainstPatient> visitorAgainstPatientsList;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "admission", orphanRemoval = true)
	private List<Transfer> transferslist;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "admission", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TransferRequest> transferRequestslist;

	@OneToMany(fetch = FetchType.LAZY,mappedBy = "admission", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Discharge> dischargesList;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "admission", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MedicoLeagal> medicoLeagalsList;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "admission", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TPeriodicityServiceOrders> listTPeriodicityServiceOrders;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "admission", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<PatientSponsorDetails> patientSponsorDetailsList;
	
	public Integer getPatientClassId() {
		return patientClassId;
	}

	public void setPatientClassId(Integer patientClassId) {
		this.patientClassId = patientClassId;
	}

	public Character getIsFinalBill() {
		return isFinalBill;
	}

	public void setIsFinalBill(Character isFinalBill) {
		this.isFinalBill = isFinalBill;
	}

	
	public Integer getAdmittingDoctorId() {
		return admittingDoctorId;
	}

	public void setAdmittingDoctorId(Integer admittingDoctorId) {
		this.admittingDoctorId = admittingDoctorId;
	}

	public Integer getAdmittingSpecialityId() {
		return admittingSpecialityId;
	}

	public void setAdmittingSpecialityId(Integer admittingSpecialityId) {
		this.admittingSpecialityId = admittingSpecialityId;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}
	
	public Character getIsFinancialCreance() {
		return isFinancialCreance;
	}

	public void setIsFinancialCreance(Character isFinancialCreance) {
		this.isFinancialCreance = isFinancialCreance;
	}

	public Integer getKinDetailsId() {
		return kinDetailsId;
	}

	public void setKinDetailsId(Integer kinDetailsId) {
		this.kinDetailsId = kinDetailsId;
	}

	public List<TPeriodicityServiceOrders> getListTPeriodicityServiceOrders() {
		return listTPeriodicityServiceOrders;
	}

	public void setListTPeriodicityServiceOrders(List<TPeriodicityServiceOrders> listTPeriodicityServiceOrders) {
		this.listTPeriodicityServiceOrders = listTPeriodicityServiceOrders;
	}

	public Integer gettPatientId() {
		return tPatientId;
	}

	public void settPatientId(Integer tPatientId) {
		this.tPatientId = tPatientId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getAdmissionNumber() {
		return admissionNumber;
	}

	public void setAdmissionNumber(String admissionNumber) {
		this.admissionNumber = admissionNumber;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getAgainstVisitId() {
		return againstVisitId;
	}

	public void setAgainstVisitId(Integer againstVisitId) {
		this.againstVisitId = againstVisitId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = (status == null) ? 'A' : status;
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

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public OrganizationMaster getOrganizationMaster() {
		return organizationMaster;
	}

	public void setOrganizationMaster(OrganizationMaster organizationMaster) {
		this.organizationMaster = organizationMaster;
	}

	public UnitMaster getUnitMaster() {
		return unitMaster;
	}

	public void setUnitMaster(UnitMaster unitMaster) {
		this.unitMaster = unitMaster;
	}

	public DoctorMaster getDoctorMaster() {
		return doctorMaster;
	}

	public void setDoctorMaster(DoctorMaster doctorMaster) {
		this.doctorMaster = doctorMaster;
	}

	public String getConcent() {
		return concent;
	}

	public void setConcent(String concent) {
		this.concent = concent;
	}

	public Integer getDeliveryFlag() {
		return deliveryFlag;
	}

	public void setDeliveryFlag(Integer deliveryFlag) {
		this.deliveryFlag = deliveryFlag==null ? 0:deliveryFlag;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Character getIsAdmissionCancelled() {
		return isAdmissionCancelled;
	}

	public void setIsAdmissionCancelled(Character isAdmissionCancelled) {
		this.isAdmissionCancelled = isAdmissionCancelled;
	}

	public Integer getAdmissionCancelledBy() {
		return admissionCancelledBy;
	}

	public void setAdmissionCancelledBy(Integer admissionCancelledBy) {
		this.admissionCancelledBy = admissionCancelledBy;
	}

	public Date getAdmissionCancelledDateTime() {
		return admissionCancelledDateTime;
	}

	public void setAdmissionCancelledDateTime(Date admissionCancelledDateTime) {
		this.admissionCancelledDateTime = admissionCancelledDateTime;
	}

	public Integer getOtStatus() {
		return otStatus;
	}

	public void setOtStatus(Integer otStatus) {
		this.otStatus = otStatus;
	}

	public String getLegacyVisitId() {
		return legacyVisitId;
	}

	public void setLegacyVisitId(String legacyVisitId) {
		this.legacyVisitId = legacyVisitId;
	}
}

package com.param.opd.appointment.model;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.model.PatientRegistration;
import com.param.opd.coversheet.model.EncounterMaster;
import com.param.opd.unit.model.SlotMaster;


@NamedNativeQueries({
	
	@NamedNativeQuery(name="GET_SCHEDULED_APPOINTMENTS_SLOTS_LIST",query=
			" select " 
					 + " 	to_char(sm.appointment_date, 'dd/MM/yyyy') as \"appointmentDate\", " 
					 + " 	sm.appointment_date as \"appointmentDateNew\", " 
					 + " 	to_char(sm.appointment_date, 'yyyy-MM-dd') as \"appointmentDate2\", " 
					 + " 	am.appointment_id as \"appointmentId\","
					 + " 	am.order_details_id as \"orderDetailsId\","
					// + "    BSI.bill_cancelled as \"billCancelled\", " 
					// + "	BSI.bill_status_id as \"billStatusId\","
					 + " 	sm.slot_id as \"slotId\", " 
					 + " 	concat(to_char(sm.from_time, 'HH24:MI')) as \"fromTimeString\", " 
					 + " 	concat(to_char(sm.to_time, 'HH24:MI')) as \"toTimeString\", " 
					 + " 	concat(to_char(sm.from_time, 'HH:MI AM')) as \"fromTimeString2\", " 
					 + " 	am.remark as \"remark\", " 
					 + " 	enc.remark as \"encRemark\", " 
					 + " 	sm.doctor_id as \"doctorId\", " 
					 + " 	doc.allowed_over_booking_slots as \"allowedOverBookingSlots\", " 
					 + " 	am.counter_id as \"counterId\", " 
					 + " 	am.appointment_type_id as \"appointmentTypeId\", " 
					 + " 	am.front_desk_arrival as \"frontDeskArrival\", " 
					 + " 	am.appointment_status_id as \"appointmentStatusId\", " 
					 + " 	asm.appointment_status_desc as \"appointmentStatusDesc\", " 
					 + " 	(case " 
					 + " 		when sm.doctor_id is not null then concat('Dr. ', coalesce(doc.first_name), ' ', coalesce(doc.last_name)) " 
					 + " 		when unitSpeciaity.dept_wise_appointment = 'Y' then concat('Dr. ', coalesce(doc2.first_name), ' ', coalesce(doc2.last_name)) end) as \"doctorName\", " 
					 + " 	(case " 
					 + " 		when sm.doctor_id is not null then doc.registration_number " 
					 + " 		when unitSpeciaity.dept_wise_appointment = 'Y' then doc2.registration_number end) as \"registrationNumber\", " 
					 + " 	sm.speciality_id as \"specialityId\", " 
					 + " 	speciaityMaster.speciality_name as \"specialityName\", " 
					 + " 	unitSpeciaity.dept_wise_appointment as \"deptWiseAppointment\", " 
					 + " 	patient.is_vip as \"isVip\", " 
					 + "    patient.video_call_url as \"videoCallUrl\", " 
					 + " 	patient.file_sequence_number as \"fileSequenceNumber\", " 
					 + " 	(case " 
					 + " 		when patient.is_vip = 'Y' then coalesce(patient.alise_name, patient.first_name) " 
					 + " 		else concat(coalesce(patient.first_name) , ' ', coalesce(patient.middle_name) , ' ', coalesce(patient.last_name)) end ) as \"patientName\", " 
					 + " 	concat(to_char(extract(year from age(current_timestamp, patient.dob)), '999'), 'Y') as \"age\", " 
					 + " 	patient.mobile_number as \"mobileNumber\", " 
					 + " 	patient.email as \"email\", " 
					 + " 	patient.patient_id as \"patientId\", " 
					 + " 	patient.country_calling_code as \"countryCallingCode\", " 
					 + " 	concat(extract(year from age(current_timestamp, patient.dob))) as \"ageYear\", " 
					 + " 	patient.uhid_number as \"uhidNumber\", "
					 + "	patient.uuid as \"uuid\", " 
					 + " 	patient.image_path as \"imagePath\", " 
					 + " 	patient.prefix_id as \"prefixId\", " 
					 + " 	patient.patient_category_id as \"patientCategoryId\", " 
					 + " 	coalesce(prefix.prefix_code) as \"prefixCode\", " 
					 + " 	coalesce(prefix.prefix_desc) as \"prefixDesc\", " 
					 + " 	patient.first_name as \"firstName\", " 
					 + " 	patient.middle_name as \"middleName\", " 
					 + " 	patient.last_name as \"lastName\", " 
					 + "    patient.blood_group as \"bloodGroup\", "
					 + " 	patient.gender_id as \"genderId\", "
					 + "    patient.identification_type_id as \"identificationTypeId\", patient.identification_number as \"identificationNumber\"," 
 				     + " 	identi.is_expiry_required as \"isExpiryRequired\", " 
					 + " 	patientDetails.country_id as \"countryId\", " 
					 + " 	patientDetails.state_id as \"stateId\", " 
					 + " 	patientDetails.district_id as \"districtId\", " 
					 + " 	patientDetails.city_id as \"cityId\", " 
					 + " 	patientDetails.area_id as \"areaId\", " 
					 + " 	patientDetails.zip_code as \"zipCode\", " 
					 + " 	patientDetails.address as \"address\", " 
					 + " 	patientDetails.marital_status_id as \"maritalStatusId\", " 
					 + " 	gender.gender_code as \"genderCode\", " 
					 + " 	patient.registration_type_id as \"registrationTypeId\", " 
					 + " 	to_char(patient.dob, 'MM/DD/YYYY') as \"dob\", " 
					 + " 	sm.modality_id as \"modalityId\", " 
					 + " 	modality.modality_desc as \"modalityDesc\", " 
					 + " 	modality.equipment_id as \"equipmentId\", " 
					 + " 	modality.sub_speciality_id as \"modalitySubSpecialityId\", " 
					 + " 	modality.speciality_id as \"modalitySpecialityId\", " 
					 + " 	enc.preferred_doc_id as \"preferredDoctorId\", " 
					 + " 	to_char(am.arrival_time, 'HH24:MI') as \"arrivalTime\", " 
					 + " 	subSpeciaityMaster.sub_speciality_name as \"subSpecialityName\", " 
					 + " 	enc.encounter_id as \"encounterId\", "
					 + "	enc.patient_class_id as \"patientClassId\", " 
					 + "	enc.source_id as \"sourceId\", "
					 + "	refdet.referal_details_id as \"referalDetailsId\", "
					 + "   refdet.referral_type_id as \"referralTypeId\", "
					 + "	refdet.referral_id as \"referralId\", " 
					 + "	refdet.referral_remark as \"referralRemark\", "
					 + "   refmast.referral_name  as \"referralDesc\", " 
					 + " 	am.token_number as \"tokenNumber\", " 
					 + " 	package_service.service_standard_name as \"packageName\", " 
					 + " 	coalesce(am.service_id) as \"serviceId\", " 
					 + " 	(case " 
					 + " 		when (enc.is_critical = 'Y' " 
					 + " 		or enDet.is_priority = 'Y') then 1 " 
					 + " 		when (enc.is_critical = 'N' " 
					 + " 		or enDet.is_priority is null) then 2 end) as \"isCriticalOrder\", " 
					 + " 	bedMaster.bed_id as \"bedId\", " 
					 + " 	am.is_paid_appointment as \"isPaidAppointment\", " 
					 + " 	am.reason_for_appointment_id as \"reasonForAppointmentId\", " 
					 + " 	reason.reason_desc as \"reasonDesc\","
					 + "    patient.whatsapp_calling_code as \"whatsAppCallingCode\","
					 + "    patient.whatsapp_no as \"whatsAppCallingNo\","
					 + "    patient.skype_id as \"skypeId\","
					 + "	am.is_teleconsultation_apppointment as \"isTeleconsultationAppointment\", " 
					 + "  to_char(am.appointment_date, 'dd/MM/yyyy') as \"hosPitalVisitAppointmentDate\", "
					 + "  am.appointment_date as \"hosPitalVisitAppointmentDateNew\", "
					/* + " (	select to_char(appointment_date, 'dd/MM/yyyy')  " 
					 + "	from opd.m_appointment_master  " 
					 + "	where patient_id = patient.patient_id " 
					 + "	order by appointment_id desc limit 1) as \"scheduledDate\", "*/
					 + " to_char(am.appointment_date, 'dd/MM/yyyy') as \"scheduledDate\",  "
					 /*+ " (select " 
					 + "	concat(to_char(sm.from_time, 'HH24:MI')) as \"scheduledDateTime\" "  
					 + "	from opd.m_appointment_master am " 
					 + "	inner join opd.m_slot_master sm on " 
					 + "	am.slot_id = sm.slot_id "  
					 + "	where am.patient_id = patient.patient_id "  
					 + "	 order by appointment_id desc limit 1) as \"scheduledDateTime\", "*/
					 + " concat(to_char(sm.from_time, 'HH24:MI')) as \"scheduledDateTime\", "
					 + "  enc.encounter_number as \"encounterNumber\" "
					 + " from " 
					 + " 	opd.m_appointment_master am " 
					 + " left join opd.m_slot_master sm on " 
					 + " 	am.slot_id = sm.slot_id " 
					 + " left outer join doctor.m_speciality_master speciaityMaster on " 
					 + " 	sm.speciality_id = speciaityMaster.speciality_id " 
					 + " left outer join t_unit_speciality_mapper unitSpeciaity on " 
					 + " 	sm.speciality_id = unitSpeciaity.speciality_id	and unitSpeciaity.unit_id = am.unit_id  " 
					 + " left outer join doctor.m_sub_speciality_master subSpeciaityMaster on " 
					 + " 	sm.sub_speciality_id = subSpeciaityMaster.sub_speciality_id " 
					 + " left outer join doctor.m_doctor_master doc on " 
					 + " 	sm.doctor_id = doc.doctor_id " 
					 + " left outer join public.m_modality_master modality on " 
					 + " 	sm.modality_id = modality.modality_id " 
					 + " left join patient.t_patient_registration patient on " 
					 + " 	am.patient_id = patient.patient_id " 
					 + " left join patient.t_patient_details patientDetails on " 				
					 + " 	patient.patient_id = patientDetails.patient_id " 
					 + " left outer join adt.m_identification_master identi on " 
					 + " 	patient.identification_type_id = identi.identification_id " 
					 + " left join public.m_gender_master gender on " 
					 + " 	patient.gender_id = gender.gender_id " 
					 + " left join public.m_prefix_master prefix on " 
					 + " 	patient.prefix_id = prefix.prefix_id " 
					 + " inner join opd.m_appointment_status_master asm on " 
					 + " 	am.appointment_status_id = asm.appointment_status_id " 
					 + " left outer join opd.t_encounter_master enc on " 
					 + " 	enc.appointment_id = am.appointment_id " 
					 + " left join opd.t_encounter_details enDet on " 
					 + " 	enDet.encounter_id = enc.encounter_id " 
					 + " left outer join doctor.m_doctor_master doc2 on " 
					 + " 	enc.preferred_doc_id = doc2.doctor_id " 
					 + " left outer join service.m_service_master package_service on " 
					 + " 	am.service_id = package_service.service_master_id " 
					 + " left outer join service.m_parent_package_master ppm on " 
					 + " 	package_service.service_master_id = ppm.service_id " 
					 + " left join adt.m_bed_master bedMaster on " 
					 + " 	bedMaster.bed_id = sm.bed_id " 
					 + " left join adt.m_reason_master reason on " 
					 + " 	reason.reason_master_id = am.reason_for_appointment_id "
					 + " left join public.t_referal_details refdet on "
					 + "   refdet.encounter_id = enc.encounter_id"
					 + " and refdet.status ='A' "
					 + " left join adt.m_referral_master refmast on " 
					 + "	refmast.referral_id = refdet.referral_id "
			     ),
				
	
	
	@NamedNativeQuery(name="GET_SCHEDULED_APPOINTMENTS_SLOTS_LIST_FOR_IS_APPOINTMENT_FALSE",query=
			   " 	select " 
			 + " 	am.appointment_id as \"appointmentId\", " 
			 + " 	am.order_details_id as \"orderDetailsId\","
			// + "    BSI.bill_cancelled as \"billCancelled\", " 
			// + " 	BSI.bill_status_id as \"billStatusId\", " 
		/*	 + " ( " 
			 + "	select "  
			 + "		bill.billing_master_id " 
			 + "	from "   
			 + "		billing.m_billing_master bill "   
			 + "		inner join billing.t_billing_details bhh on "  
			 + "		bhh.billing_master_id  = bill.billing_master_id " 
			 + "		left join billing.t_bill_payee_details billPayee on" 
			 + "		bill.billing_master_id = billPayee.bill_id "  
			 + "		and billPayee.payee_id =(	SELECT uabc.self_payee_id  FROM  public.t_unit_application_billing_configuration uabc WHERE uabc.status = 'A' AND  uabc.unit_id =:unitId) "  
			 + "		inner join public.t_order_details ordDet on "   
			 + "		ordDet.order_details_id  = bhh.order_details_id    "  
			 + "	where "  
			 + "		bill.bill_cancelled = 'N' "  
			 + "		and bill.is_dr_note = 'N' "  
			 + "		and bill.encounter_id = enc.encounter_id "   
			 +  "		and bill.bill_status_id in (1,2) "    
			 + "		and bill.total_self_payable > 0 "  
			 + "		and billPayee.due_amount > 0 "   
			 + "		and bill.outstanding > 0 "   
			 + "		and coalesce(ordDet.is_consultation_service,'N' ) = 'Y' "   
			 +  "	order by "   
			 + "		bill.billing_master_id desc "  
			 + "	limit 1) as \"billId\" ,"
			 + " ( select bill.bill_cancelled from billing.m_billing_master bill where  bill.is_dr_note = 'N' and bill.encounter_id = enc.encounter_id order by bill.billing_master_id desc limit 1) as \"billCancelled\"  , " 
			 + " ( select bill.bill_status_id from billing.m_billing_master bill where bill.bill_cancelled = 'N' and bill.is_dr_note = 'N' and bill.encounter_id = enc.encounter_id order by bill.billing_master_id desc limit 1) as \"billStatusId\"  , "
			 + "( select    " + 
			 "		billPayee.payment_entitlement_id    " + 
			 "	from    " + 
			 "		billing.m_billing_master bill    " + 
			 "	left join billing.t_bill_payee_details billPayee on    " + 
			 "		bill.billing_master_id = billPayee.bill_id    " + 
			 "		and billPayee.priority_id = (    " + 
			 "		select    " + 
			 "			min(bpd.priority_id)    " + 
			 "		from    " + 
			 "			billing.t_bill_payee_details bpd    " + 
			 "		where    " + 
			 "			bill.billing_master_id = bpd.bill_id)    " + 
			 "	where    " + 
			 "		bill.bill_cancelled = 'N'    " + 
			 "		and bill.is_dr_note = 'N'    " + 
			 "		and bill.encounter_id = enc.encounter_id    " + 
			 "	order by    " + 
			 "		bill.billing_master_id desc     " + 
			 "	limit 1) as \"paymentEntitlementId\" ,    " + 
			 "		(    " + 
			 "	select    " + 
			 "		(case    " + 
			 "		when billPayee.associate_company_id > 0 then insurance.insurance_desc    " + 
			 "		else tcm.company_desc    " + 
			 "	end )     " + 
			 "	from    " + 
			 "		billing.m_billing_master bill    " + 
			 "	left join billing.t_bill_payee_details billPayee on    " + 
			 "		bill.billing_master_id = billPayee.bill_id    " + 
			 "		and billPayee.priority_id = (    " + 
			 "		select    " + 
			 "			min(bpd.priority_id)    " + 
			 "		from    " + 
			 "			billing.t_bill_payee_details bpd    " + 
			 "		where    " + 
			 "			bill.billing_master_id = bpd.bill_id)    " + 
			 "			left join adt.t_insurance insurance on    " + 
			 "	insurance.insurance_id = billPayee.associate_company_id    " + 
			 " left join public.t_company_master tcm on    " + 
			 "	tcm.company_id = billPayee.payee_id    " + 
			 "	where    " + 
			 "		bill.bill_cancelled = 'N'    " + 
			 "		and bill.is_dr_note = 'N'    " + 
			 "		and bill.encounter_id = enc.encounter_id    " + 
			 "	order by    " + 
			 "		bill.billing_master_id desc     " + 
			 "	limit 1) as \"associateCompanyDesc\","*/
			 + " 	am.remark as \"remark\", " 
			 + " 	enc.remark as \"encRemark\", " 
//			 + " 	sm.doctor_id as \"doctorId\", " 
			 + "(case "
		     + " when enc.doctor_id is not null then enc.doctor_id "
		     + " when sm.doctor_id is not null then sm.doctor_id "
	         + " end) as \"doctorId\","
			 + " 	doc.allowed_over_booking_slots as \"allowedOverBookingSlots\", " 
			 + " 	am.counter_id as \"counterId\", " 
			 + " 	am.appointment_type_id as \"appointmentTypeId\", " 
			 + " 	am.front_desk_arrival as \"frontDeskArrival\", " 
			 + " 	am.appointment_status_id as \"appointmentStatusId\", " 
			 + "	am.is_teleconsultation_apppointment as \"isTeleconsultationAppointment\", "
			 + " 	asm.appointment_status_desc as \"appointmentStatusDesc\", " 
			 + "  (case "  
			 + "	 when enc.doctor_id  is not null then concat('Dr. ', coalesce(doc.first_name), ' ', coalesce(doc.last_name)) " 
			 + "	 when sm.doctor_id  is not null  then concat('Dr. ', coalesce(doc2.first_name), ' ', coalesce(doc2.last_name)) "  
			 + "				end) as \"doctorName\", " 
			 + "    doc.registration_number as \"registrationNumber\","
			 + " (case "  
			 + "		when enc.dept_id is not null then enc.dept_id " 
			 + "		when sm.speciality_id is not null  then sm.speciality_id "  
			 + "	end) as \"specialityId\","
			 + " (case "  
			 + "		when speciaityMaster.speciality_name is not null then speciaityMaster.speciality_name" 
			 + "		when speciaityMaster2.speciality_name is not null  then speciaityMaster2.speciality_name "  
			 + "	end) as \"specialityName\","
			 + " 	unitSpeciaity.dept_wise_appointment as \"deptWiseAppointment\", " 
			 + " 	patient.is_vip as \"isVip\", " 
			 + " 	patient.file_sequence_number as \"fileSequenceNumber\", " 
			 + " 	(case " 
			 + " 		when patient.is_vip = 'Y' then coalesce(patient.alise_name, patient.first_name) " 
			 + " 		else concat(coalesce(patient.first_name) , ' ', coalesce(patient.middle_name) , ' ', coalesce(patient.last_name)) " 
			 + " 	end ) as \"patientName\", " 
			 + " 	concat(to_char(extract(year from age(current_timestamp, patient.dob)), '999'), 'Y') as \"age\", " 
			 + " 	patient.mobile_number as \"mobileNumber\", " 
			 + " 	patient.email as \"email\", " 
			 + " 	patient.patient_id as \"patientId\", " 
			 + " 	patient.country_calling_code as \"countryCallingCode\", " 
			 + " 	concat(extract(year from age(current_timestamp, patient.dob))) as \"ageYear\", " 
			 + " 	patient.uhid_number as \"uhidNumber\", " 
			 + " 	patient.image_path as \"imagePath\", " 
			 + " 	patient.prefix_id as \"prefixId\", " 
			 + " 	patient.patient_category_id as \"patientCategoryId\", " 
			 + " 	coalesce(prefix.prefix_code) as \"prefixCode\", " 
			 + " 	coalesce(prefix.prefix_desc) as \"prefixDesc\", " 
			 + " 	patient.first_name as \"firstName\", " 
			 + " 	patient.middle_name as \"middleName\", " 
			 + " 	patient.last_name as \"lastName\", " 
			 + "	patient.blood_group as \"bloodGroup\", "	
			 + " 	patient.gender_id as \"genderId\","
			 + "    patient.video_call_url as \"videoCallUrl\", " 
			 + " 	patientDetails.country_id as \"countryId\", " 
			 + " 	patientDetails.state_id as \"stateId\", " 
			 + " 	patientDetails.district_id as \"districtId\", " 
			 + " 	patientDetails.city_id as \"cityId\", " 
			 + " 	patientDetails.area_id as \"areaId\", " 
			 + " 	patientDetails.zip_code as \"zipCode\", " 
			 + " 	patientDetails.address as \"address\", " 
			 + " 	patientDetails.marital_status_id as \"maritalStatusId\", " 
			 + " 	gender.gender_code as \"genderCode\", " 
			 + " 	patient.registration_type_id as \"registrationTypeId\", patient.identification_type_id as \"identificationTypeId\", patient.identification_number as \"identificationNumber\" ,"

 			 + " 	identi.is_expiry_required as \"isExpiryRequired\", " 
			 + " 	to_char(patient.dob, 'MM/DD/YYYY') as \"dob\", " 
			 + " 	modality.modality_desc as \"modalityDesc\", " 
			 + " 	modality.equipment_id as \"equipmentId\", " 
			 + " 	modality.sub_speciality_id as \"modalitySubSpecialityId\", " 
			 + " 	modality.speciality_id as \"modalitySpecialityId\", " 
			// + " 	adm.admission_id as \"admissionId\", " 
			 + " 	to_char(am.arrival_time, 'HH24:MI') as \"arrivalTime\", " 
			 + " 	subSpeciaityMaster.sub_speciality_name as \"subSpecialityName\", " 
			 + " 	enc.encounter_id as \"encounterId\", " 
			 + " 	am.token_number as \"tokenNumber\", "
			 + "	enc.patient_class_id as \"patientClassId\", " 
			 + "	enc.source_id as \"sourceId\", "
			 + "	refdet.referal_details_id as \"referalDetailsId\", "
			 + "   refdet.referral_type_id as \"referralTypeId\", "
			 + "	refdet.referral_id as \"referralId\", " 
			 + "	refdet.referral_remark as \"referralRemark\", "
			 + "   refmast.referral_name  as \"referralDesc\", " 
			 + " 	package_service.service_standard_name as \"packageName\", " 
			 + " 	coalesce(am.service_id) as \"serviceId\", " 
			 + " 	(case " 
			 + " 		when (enc.is_critical = 'Y' " 
			 + " 		or enDet.is_priority = 'Y') then 1 " 
			 + " 		when (enc.is_critical = 'N' " 
			 + " 		or enDet.is_priority is null) then 2 " 
			 + " end) as \"isCriticalOrder\", " 
			 + "  to_char(am.appointment_date, 'dd/MM/yyyy') as \"hosPitalVisitAppointmentDate\","
			 + "  am.appointment_date as \"hosPitalVisitAppointmentDateNew\","
			/* + " (	select to_char(appointment_date, 'dd/MM/yyyy')  " 
			 + "	from opd.m_appointment_master  " 
			 + "	where patient_id = patient.patient_id " 
			 + "	 order by appointment_id desc limit 1) as \"scheduledDate\","*/
			 + " to_char(am.appointment_date, 'dd/MM/yyyy') as \"scheduledDate\",  "
			/* + " (select " 
			 + "	concat(to_char(sm.from_time, 'HH24:MI')) as \"scheduledDateTime\" "  
			 + "	from opd.m_appointment_master am " 
			 + "	inner join opd.m_slot_master sm on " 
			 + "	am.slot_id = sm.slot_id "  
			 + "	where am.patient_id = patient.patient_id "  
			 + "	 order by appointment_id desc limit 1) as \"scheduledDateTime\", "*/
			 + "	concat(to_char(sm.from_time, 'HH24:MI')) as \"scheduledDateTime\", "  
			 + "  enc.encounter_number  as \"encounterNumber\" "
			 + " from " 
			 + " 	opd.m_appointment_master am " 
			 + " left join opd.t_encounter_master enc on " 
			 + " 	enc.appointment_id = am.appointment_id " 
			 + " left join opd.t_encounter_details enDet on " 
			 + " 	enDet.encounter_id = enc.encounter_id " 
			 + " left join opd.m_slot_master sm on " 
			 + " 	am.slot_id = sm.slot_id " 
			 + " left outer join doctor.m_speciality_master speciaityMaster on " 
			 + " 	enc.dept_id = speciaityMaster.speciality_id "
			 + "left outer join doctor.m_speciality_master speciaityMaster2 on "
			 + "	sm.speciality_id = speciaityMaster2.speciality_id" 
			 + " left outer join t_unit_speciality_mapper unitSpeciaity on " 
			 + " 	sm.speciality_id = unitSpeciaity.speciality_id	and unitSpeciaity.unit_id = am.unit_id  "
			 + " left outer join doctor.m_sub_speciality_master subSpeciaityMaster on " 
			 + " 	enc.sub_dept_id = subSpeciaityMaster.sub_speciality_id " 
			 + " left outer join doctor.m_doctor_master doc on " 
			 + " 	enc.doctor_id = doc.doctor_id " 
			 + " left outer join doctor.m_doctor_master doc2 on " 
			 + " 	sm.doctor_id = doc2.doctor_id " 
			 + " left outer join public.m_modality_master modality on " 
			 + " 	enc.modality_id = modality.modality_id " 
			 + " left join patient.t_patient_registration patient on " 
			 + " 	am.patient_id = patient.patient_id " 
			 + " left join patient.t_patient_details patientDetails on " 
			 + " 	patient.patient_id = patientDetails.patient_id " 
			 + " left outer join adt.m_identification_master identi on " 
			 + " 	patient.identification_type_id = identi.identification_id " 
			 + " left join public.m_gender_master gender on " 
			 + " 	patient.gender_id = gender.gender_id " 
			 + " left join public.m_prefix_master prefix on " 
			 + " 	patient.prefix_id = prefix.prefix_id " 
			 + " inner join opd.m_appointment_status_master asm on " 
			 + " 	am.appointment_status_id = asm.appointment_status_id " 
				/*
				 * + " left outer join adt.t_admission adm on " +
				 * " 	adm.patient_id = am.patient_id " +
				 * " 	and am.appointment_status_id = 1 " + " 	and adm.status = 'A' "
				 */
			 + " left outer join service.m_service_master package_service on " 
			 + " 	am.service_id = package_service.service_master_id " 
			 + " left outer join service.m_parent_package_master ppm on " 
			 + " 	package_service.service_master_id = ppm.service_id "
			 + " left join public.t_referal_details refdet on "
			 + "   refdet.encounter_id = enc.encounter_id"
			 + " and refdet.status ='A' "
			 + " left join adt.m_referral_master refmast on " 
			 + "	refmast.referral_id = refdet.referral_id "
		       ),
			 /*"      LEFT JOIN billing.t_bill_payee_details billPayee ON bill.billing_master_id = billPayee.bill_id and billPayee.priority_id = (   " + 
			 "    		select   " + 
			 "    			min(bpd.priority_id)   " + 
			 "    		from   " + 
			 "    			billing.t_bill_payee_details bpd   " + 
			 "    		where   " + 
			 "    			bill.billing_master_id = bpd.bill_id)   " + */
			
	
	@NamedNativeQuery(name="GET_SCHEDULED_APPOINTMENTS_SLOTS_LIST_BY_PATIENTID",query=""
			+ "SELECT to_char(sm.appointment_date,'dd/MM/yyyy') as \"appointmentDate\", "
			+ "to_char(sm.appointment_date,'yyyy-MM-dd') as \"appointmentDate2\", "
			+ "am.appointment_id as \"appointmentId\", "
			+ "am.service_id as \"serviceId\", "
			+ "sm.slot_id as \"slotId\", "
			+ "concat(to_char(sm.from_time,'HH24:MI')) as \"fromTimeString\", "
			+ "concat(to_char(sm.to_time,'HH24:MI')) as \"toTimeString\", "
			+ "concat(to_char(sm.from_time, 'HH:MI AM')) as \"fromTimeString2\","
			+ "am.remark as \"remark\", "
			+ "sm.doctor_id as \"doctorId\", "
			+ "am.counter_id as \"counterId\", "
			+ "am.appointment_type_id as \"appointmentTypeId\", "
			+ "am.front_desk_arrival as \"frontDeskArrival\", "
			+ "am.appointment_status_id as \"appointmentStatusId\", "
			+ "asm.appointment_status_desc as \"appointmentStatusDesc\", "
			+ "concat('Dr. ',coalesce(doc.first_name),' ',coalesce(doc.last_name)) as \"doctorName\", "
			+ "sm.speciality_id as \"specialityId\", "
			+ "speciaityMaster.speciality_name as \"specialityName\", "
			+ "unitSpeciaity.dept_wise_appointment as \"deptWiseAppointment\", "
			+ "patient.is_vip as \"isVip\", "
			+ "(case when patient.is_vip='Y' then "
			+ "coalesce(patient.alise_name,patient.first_name) "
			+ "else "
			+ "concat(coalesce(patient.first_name) , ' ', coalesce(patient.middle_name) , ' ', coalesce(patient.last_name)) "
			+ "end	) as \"patientName\", "
			+ "concat(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)),'999'), 'Y') as \"age\", "
			+ "patient.mobile_number as \"mobileNumber\", "
			+ "patient.email as \"email\", "
			+ "patient.blood_group as \"bloodGroup\", "
			+ "patient.patient_id as \"patientId\", "
			+ "patient.country_calling_code as \"countryCallingCode\", "
			+ "concat(extract(year from AGE(current_timestamp, patient.dob))) as \"ageYear\", "
			+ "patient.uhid_number as \"uhidNumber\", "
			+ "patient.image_path as \"imagePath\", "
			+ "patient.prefix_id as \"prefixId\", "
			+ "coalesce(prefix.prefix_code) as \"prefixCode\", " 
			+ "coalesce(prefix.prefix_desc) as \"prefixDesc\", " 
			+ "patient.first_name as \"firstName\", "
			+ "patient.middle_name as \"middleName\", "
			+ "patient.last_name as \"lastName\", "
			+ "patient.gender_id as \"genderId\", "
			+ "patientDetails.country_id as \"countryId\", "
			+ "patientDetails.state_id as \"stateId\", "
			+ "patientDetails.district_id as \"districtId\", "
			+ "patientDetails.city_id as \"cityId\", "
			+ "patientDetails.area_id as \"areaId\", "
			+ "patientDetails.zip_code as \"zipCode\", "
			+ "patientDetails.address as \"address\", "
			+ "patientDetails.marital_status_id as \"maritalStatusId\", "
			+ "gender.gender_code as \"genderCode\", "
			+ "patient.registration_type_id as \"registrationTypeId\", "
			+ "to_char(patient.dob,'MM/DD/YYYY') as \"dob\", "
			+ "sm.modality_id as \"modalityId\", "
			+ "modality.modality_desc as \"modalityDesc\", "
			//+ "sm.modality_type_id as \"modalityTypeId\", "
			//+ "modalityType.modality_type_desc as \"modalityTypeDesc\", "
			//+ "modalityType.speciality_id as \"modalitySpecialityId\", "
			+ "modality.equipment_id as \"equipmentId\", "
			+ "modality.sub_speciality_id as \"modalitySubSpecialityId\", "
			+ "modality.speciality_id as \"modalitySpecialityId\", "
			+ "adm.admission_id as \"admissionId\", "
			+ "patient.patient_category_id as \"patientCategoryId\", "
			+ "to_char(am.arrival_time,'HH24:MI') as \"arrivalTime\","
			+ "subSpeciaityMaster.sub_speciality_name as \"subSpecialityName\", "
			+ "enc.encounter_id as \"encounterId\", "
			//+ "sm.item_id as\"itemId\",item.name as \"itemName\", "
			+ " am.token_number as \"tokenNumber\" "
			+ "FROM opd.m_appointment_master am "
			+ "LEFT JOIN opd.m_slot_master sm on am.slot_id=sm.slot_id "
			+ "LEFT OUTER JOIN doctor.m_speciality_master speciaityMaster on sm.speciality_id=speciaityMaster.speciality_id "
			+ " left outer join t_unit_speciality_mapper unitSpeciaity on sm.speciality_id = unitSpeciaity.speciality_id and unitSpeciaity.unit_id = am.unit_id  "
			+ "LEFT OUTER JOIN doctor.m_sub_speciality_master subSpeciaityMaster on sm.sub_speciality_id=subSpeciaityMaster.sub_speciality_id "
			+ "LEFT OUTER JOIN doctor.m_doctor_master doc on sm.doctor_id=doc.doctor_id "
			//+ "LEFT OUTER JOIN public.m_modality_type_master modalityType on sm.modality_type_id=modalityType.modality_type_id "
			+ "LEFT OUTER JOIN public.m_modality_master modality on sm.modality_id=modality.modality_id "
			+ "LEFT JOIN patient.t_patient_registration patient on am.patient_id=patient.patient_id "
			+ "LEFT JOIN patient.t_patient_details patientDetails on patient.patient_id=patientDetails.patient_id "
			+ "LEFT JOIN public.m_gender_master gender on patient.gender_id=gender.gender_id "
			+ "LEFT JOIN public.m_prefix_master prefix on patient.prefix_id=prefix.prefix_id "
			//+ "LEFT JOIN public.m_item item on sm.item_id=item.id "
			+ "INNER JOIN opd.m_appointment_status_master asm on am.appointment_status_id=asm.appointment_status_id "
			+ "LEFT OUTER JOIN adt.t_admission adm on adm.patient_id = am.patient_id AND am.appointment_status_id=1 AND adm.status='A' "
			+ "LEFT OUTER JOIN opd.t_encounter_master enc on enc.appointment_id = am.appointment_id "
			/*+ "WHERE am.organization_id=:orgId "
			+ "AND am.unit_id=:unitId "
			+ "AND am.appointment_type_id=:appointmentTypeId "*/),
	
	@NamedNativeQuery(name="GET_SCHEDULED_CYCLIC_APPOINTMENTS_SLOTS_LIST_BY_PATIENTID",query=""
			+ "SELECT to_char(am.appointment_date,'DD-MM-YYYY') as \"appointmentDate\", "
			//+ " (case when (am.slot_id is null) then (to_char(cam.appointment_time,'HH24:MI')) else (to_char(sm.from_time,'HH24:MI')) end) as \"appointmentTime\","
			+ "to_char(cam.appointment_time,'HH24:MI') as \"appointmentTime\","
			+ "cam.available_quantity as \"availableQuantity\","
			+ "am.appointment_id as \"appointmentId\", "
			+ "am.appointment_type_id as \"appointmentTypeId\", "
			+ "am.front_desk_arrival as \"frontDeskArrival\", "
			+ "am.appointment_status_id as \"appointmentStatusId\", "
			+ "asm.appointment_status_desc as \"appointmentStatusDesc\", "
			+ "patient.is_vip as \"isVip\", "
			+ "(case when patient.is_vip='Y' then "
			+ "coalesce(patient.alise_name,patient.first_name) "
			+ "else "
			+ "concat(coalesce(patient.first_name) , ' ', coalesce(patient.middle_name) , ' ', coalesce(patient.last_name)) "
			+ "end	) as \"patientName\", "
			+ "concat(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)),'999'), 'Y') as \"age\", "
			+ "patient.mobile_number as \"mobileNumber\", "
			+ "patient.email as \"email\", "
			+ "patient.patient_id as \"patientId\", "
			+ "patient.country_calling_code as \"countryCallingCode\", "
			+ "concat(extract(year from AGE(current_timestamp, patient.dob))) as \"ageYear\", "
			+ "patient.uhid_number as \"uhidNumber\", "
			+ "patient.image_path as \"imagePath\", "
			+ "patient.prefix_id as \"prefixId\", "
			+ "coalesce(prefix.prefix_code) as \"prefixCode\", " 
			+ "coalesce(prefix.prefix_desc) as \"prefixDesc\", " 
			+ "patient.first_name as \"firstName\", "
			+ "patient.middle_name as \"middleName\", "
			+ "patient.last_name as \"lastName\", "
			+ "patient.gender_id as \"genderId\", "
			+ "patientDetails.country_id as \"countryId\", "
			+ "patientDetails.state_id as \"stateId\", "
			+ "patientDetails.district_id as \"districtId\", "
			+ "patientDetails.city_id as \"cityId\", "
			+ "patientDetails.area_id as \"areaId\", "
			+ "patientDetails.zip_code as \"zipCode\", "
			+ "patientDetails.address as \"address\", "
			+ "patientDetails.marital_status_id as \"maritalStatusId\", "
			+ "gender.gender_code as \"genderCode\", "
			+ "patient.registration_type_id as \"registrationTypeId\", "
			+ "to_char(patient.dob,'MM/DD/YYYY') as \"dob\", "
			+ "patient.patient_category_id as \"patientCategoryId\","
			+ "msm.service_standard_code as \"serviceCode\", "  
			+ " msm.service_standard_name as \"serviceStandardName\" "
			+ "FROM opd.m_appointment_master am "
			+ "LEFT JOIN patient.t_patient_registration patient on am.patient_id=patient.patient_id "
			+ "LEFT JOIN patient.t_patient_details patientDetails on patient.patient_id=patientDetails.patient_id "
			+ "LEFT JOIN public.m_gender_master gender on patient.gender_id=gender.gender_id "
			+ "LEFT JOIN public.m_prefix_master prefix on patient.prefix_id=prefix.prefix_id "
			+ "INNER JOIN opd.m_appointment_status_master asm on am.appointment_status_id=asm.appointment_status_id "
			+ "LEFT OUTER JOIN adt.t_admission adm on adm.patient_id = am.patient_id AND am.appointment_status_id=1 AND adm.status='A'"
			+ " inner join service.m_service_master as msm on msm.service_master_id = am.service_id "
			+ "LEFT JOIN opd.m_slot_master sm on am.slot_id=sm.slot_id "
			+ " left join opd.t_cyclic_appointment_mapper cam on cam.appointment_id=am.appointment_id"
			+" INNER join public.t_order_details ord on ord.order_details_id=cam.order_detail_id "
			//+ "LEFT join billing.m_billing_master as mbm on mbm.billing_master_id=cam.bill_id and mbm.bill_cancelled = 'N' "
			+ " left join billing.t_billing_details tbd on   tbd.order_details_id=ord.order_details_id "
			+ " left join billing.m_billing_master as mbm on mbm.billing_master_id = tbd.billing_master_id and tbd.service_id=msm.service_master_id"
			+ " inner join service.t_unit_service_master_details usmd on usmd.service_master_id=msm.service_master_id and usmd.unit_id=ord.org_unit_id and usmd.status='A' "
			+ " LEFT join public.t_order_master ordM on ordM.order_id=ord.order_id"
			+ " left join t_unit_application_billing_configuration appConfig on "
			+ " appConfig.unit_id = ordM.org_unit_id  "	
			),
	
	@NamedNativeQuery(name="GET_BLOCKED_SCHEDULED_APPOINTMENTS_SLOTS_LIST",query=""
			+ "SELECT to_char(sm.appointment_date,'dd/MM/yyyy') as \"appointmentDate\", "
			+ "am.appointment_id as \"appointmentId\", "
			+ "sm.slot_id as \"slotId\", "
			+ "concat(to_char(sm.from_time,'HH24:MI')) as \"fromTimeString\", "
			+ "concat(to_char(sm.to_time,'HH24:MI')) as \"toTimeString\", "
			+ "am.remark as \"remark\", "
			+ "sm.doctor_id as \"doctorId\", "
			+ "am.appointment_type_id as \"appointmentTypeId\", "
			+ "am.appointment_status_id as \"appointmentStatusId\", "
			+ "asm.appointment_status_desc as \"appointmentStatusDesc\", "
			+ "concat('Dr. ',coalesce(doc.first_name),' ',coalesce(doc.middle_name),' ',coalesce(doc.last_name)) as \"doctorName\", "
			+ "sm.speciality_id as \"specialityId\", "
			+ "speciaityMaster.speciality_name as \"specialityName\", "
			+ "concat(coalesce(patient.first_name) ,' ', coalesce(patient.middle_name) ,' ', coalesce(patient.last_name)) as \"patientName\", "
			+ "concat(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)),'999'), 'Y') as \"age\", "
			+ "patient.mobile_number as \"mobileNumber\", "
			+ "patient.email as \"email\", "
			+ "patient.patient_id as \"patientId\", "
			+ "concat(extract(year from AGE(current_timestamp, patient.dob))) as \"ageYear\", "
			+ "patient.uhid_number as \"uhidNumber\", "
			+ "patient.prefix_id as \"prefixId\", "
			+ "prefix.prefix_code as \"prefixCode\", " 
			+ "prefix.prefix_desc as \"prefixDesc\", " 
			+ "patient.first_name as \"firstName\", "
			+ "patient.middle_name as \"middleName\", "
			+ "patient.last_name as \"lastName\", "
			+ "patient.gender_id as \"genderId\", "
			+ "patientDetails.country_id as \"countryId\", "
			+ "patientDetails.state_id as \"stateId\", "
			+ "patientDetails.district_id as \"districtId\", "
			+ "patientDetails.city_id as \"cityId\", "
			+ "patientDetails.area_id as \"areaId\", "
			+ "patientDetails.zip_code as \"zipCode\", "
			+ "patientDetails.address as \"address\", "
			+ "patientDetails.marital_status_id as \"maritalStatusId\", "
			+ "gender.gender_code as \"genderCode\", "
			+ "to_char(patient.dob,'DD/MM/YYYY') as \"dob\", "
			+ "sm.modality_id as \"modalityId\", "
			+ "modality.modality_desc as \"modalityDesc\", "
			+ "adm.admission_id as \"admissionId\", "
			+ "slud.reason_id as \"reasonId\", "
			+ "reason.reason_desc as \"reasonDesc\", "
			+ "slud.remark as \"blockedRemark\", "
			+ "to_char(slud.created_date,'dd/mm/yyyy HH:MI') as \"createdDate\", "
			+ "to_char(am.arrival_time,'HH:MI') as \"arrivalTime\", "
			+ "am.token_number as \"tokenNumber\", "
			+ "am.call_count as \"count\" "
			+ "FROM opd.m_appointment_master am "
			+ "LEFT JOIN opd.m_slot_master sm on am.slot_id=sm.slot_id "
			+ "INNER JOIN patient.t_patient_registration patient on am.patient_id=patient.patient_id "
			+ "INNER JOIN opd.m_appointment_status_master asm on am.appointment_status_id=asm.appointment_status_id "
			+ "LEFT OUTER JOIN adt.t_admission adm on adm.patient_id = am.patient_id AND am.appointment_status_id=1 AND adm.status='A' "
			+ " LEFT join opd.t_encounter_master enc on enc.appointment_id = am.appointment_id " 
			+ " LEFT join opd.t_encounter_details enDet on enDet.encounter_id = enc.encounter_id " 
			+ "LEFT OUTER JOIN doctor.m_speciality_master speciaityMaster on sm.speciality_id=speciaityMaster.speciality_id "
			+ "LEFT OUTER JOIN doctor.m_doctor_master doc on sm.doctor_id=doc.doctor_id "
			+ "LEFT OUTER JOIN opd.t_slot_block_unblock_detail slud on slud.slot_id=sm.slot_id "
			+ "LEFT OUTER JOIN adt.m_reason_master reason on slud.reason_id=reason.reason_master_id "
			+ "LEFT OUTER JOIN public.m_modality_master modality on sm.modality_id=modality.modality_id "
			+ "INNER JOIN public.m_gender_master gender on patient.gender_id=gender.gender_id "
			+ "INNER JOIN public.m_prefix_master prefix on patient.prefix_id=prefix.prefix_id "
			+ "INNER JOIN patient.t_patient_details patientDetails on patient.patient_id=patientDetails.patient_id "
			+ "WHERE am.organization_id=:orgId "
			+ "AND am.unit_id=:unitId "
			+ "AND am.appointment_type_id=:appointmentTypeId "
			+ "AND sm.is_blocked=1 "),
	
	@NamedNativeQuery(name = "GET_DOCTOR_APPOINTMENT_TO_SYNC_BY_ID",
					 query = " select " 
							 + " 	doc.global_doc_id as \"doctorId\", " 
							 + " 	slot.doctor_id as \"doctorLocalId\", " 
							 + " 	app.appointment_id as \"appointmentId\", " 
							 + " 	app.is_direct_encounter as \"directEncounter\", " 
							 + " 	app.organization_id as \"hospitalId\", " 
							 + " 	app.unit_id as \"unitId\", " 
                             + " 	app.is_appointment_from_my_life as \"isAppointmentFromMylife\", " 
							 + " 	app.appointment_status_id as \"appointmentStatusId\", " 
							 + " 	to_char(slot.appointment_date, 'DD-MM-YYYY') as \"appointmentDate\", " 
							 + " 	to_char(slot.from_time, 'HH24:MI') as \"appointmentTime\", " 
							 + " 	to_char(slot.to_time, 'HH24:MI') as \"appointmentEndTime\", " 
							 + " 	app.patient_id as \"patientId\", " 
							 + " 	2 as \"applicationId\", " 
							 + " 	slot.speciality_id as \"departmentId\", " 
							 + " 	spc.speciality_name as \"departmentName\", " 
							 + " 	concat(doc.first_name, ' ', doc.last_name) as \"doctorName\", " 
							 + " 	1 as \"visitTypeId\", " 
							 + " 	org.global_org_id as \"globalOrgId\", " 
                             + " 	org.channel_id as \"channelId\", "
							 + " 	unit.global_unit_id as \"globalUnitId\", " 
							 + " 	pr.uuid as \"uuid\",app.is_teleconsultation_apppointment as \"isVideo\" " 
							 + " from " 
							 + " 	opd.m_appointment_master app " 
							 + " inner join opd.m_slot_master slot on " 
							 + " 	slot.slot_id = app.slot_id " 
							 + " left join opd.t_encounter_master enc on " 
							 + " 	enc.appointment_id = app.appointment_id " 
							 + " inner join doctor.m_doctor_master doc on " 
							 + " 	doc.doctor_id = slot.doctor_id " 
							 + " inner join doctor.m_speciality_master spc on " 
							 + " 	spc.speciality_id = slot.speciality_id " 
							 + " inner join public.m_organization_master org on " 
							 + " 	org.organization_id = app.organization_id " 
							 + " inner join public.m_unit_master unit on " 
							 + " 	unit.unit_id = app.unit_id " 
							 + " inner join patient.t_patient_registration pr on " 
							 + " 	pr.patient_id = app.patient_id  " 
							 + " where " 
							 + " 	app.appointment_id = :id " 
							 + " 	and app.appointment_type_id = 1 "
							 + "	and doc.global_doc_id is not null "
							 + "	and pr.uuid is not null " ),
	
	@NamedNativeQuery(name = "GET_APPOINTMENT_DETAILS_TO_SYNC_BY_ID",
	 query = "select enc.encounter_id  as \"encounterId\", enc.appointment_id as \"appointmentId\","
			 + " app.organization_id as \"hospitalId\", app.unit_id as \"unitId\", "
			 + " to_char(slot.appointment_date,'DD-MM-YYYY') as \"appointmentDate\",  "
			 + "  2 as \"applicationId\", "
			 + "  concat(doc.first_name,' ',doc.last_name) as \"doctorName\",app.coversheet_ml_file_path as \"coversheetMLFilePath\" "
			 + " from opd.m_appointment_master app "
			 + " inner join opd.m_slot_master slot "
			 + " on slot.slot_id = app.slot_id "
			 + " left join opd.t_encounter_master enc "
			 + " on enc.appointment_id = app.appointment_id "
			 + " inner join doctor.m_doctor_master doc "
			 + " on doc.doctor_id = slot.doctor_id "
			/* + " inner join doctor.t_doctor_speciality_mapper mppr "
			 + " on mppr.doctor_id = doc.doctor_id "*/
			 + " inner join doctor.m_speciality_master spc "
			 + " on spc.speciality_id = slot.speciality_id "
			 + " where app.appointment_id = :id  "),//and mppr.status='A'
	
	@NamedNativeQuery(name = "GET_IP_PATIENT_APPOINTMENT_TO_SYNC_BY_ID",
	 query = "SELECT DISTINCT(discharge.admission_id) as \"admissionId\",  2 as \"applicationId\", "
			    + " cast(admission.patient_id as varchar) as \"localUhid\", " 
				+ " admission.admission_number as \"admissionNumber\",  "
				+ " to_char(admissionDetails.doa,'dd-mm-yyyy') as \"dateOfAdmission\",  "
				+ " bcm.bed_category_desc as \"bedCategory\",ward.ward_name as \"ward\",bm.bed_number as \"room\",  "
				+ " admission.doctor_id as \"doctorId\", concat(COALESCE(doc.first_name,''),' ', COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as  \"doctorName\", "
				+ " sm.speciality_name as \"speciality\", "
				+ " discharge.discharge_id as \"dischargeId\",dsumm.discharge_summary_id as \"dischargeSummaryId\" ," 
				+ " dtm.discharge_type_name as \"dischargeType\", "
	            + " to_char(discharge.discharge_time,'dd-mm-yyyy') as \"dischargeDate\","
				+ " disStatus.discharge_desc as \"dischargeRemark\", "
				+ " discharge.organization_id  as \"hospitalId\",discharge.unit_id as \"unitId\","
				+ " pr.uuid as \"uuid\",orgMas.global_org_id as \"globalOrgId\",unitMas.global_unit_id as \"globalUnitId\",dsumm.discharge_pdf_path \"filePath\","
				+ " (case when admission.visit_type_id =2 then 'In Patient' when admission.visit_type_id =3 then 'Day Care' else 'Emergency' end) \"visitType\" "
				+ " FROM adt.t_discharge discharge  "
				+ " INNER JOIN adt.m_discharge_status_master disStatus on discharge.discharge_status_id=disStatus.discharge_status_id "
				+ " INNER JOIN adt.m_reason_for_discharge_master dtm on discharge.discharge_type_id=dtm.discharge_type_id "
				+ " INNER JOIN adt.t_admission as admission on discharge.admission_id=admission.admission_id "
				+ " inner join patient.t_patient_registration pr on admission.patient_id=pr.patient_id "
				+ " inner join public.m_gender_master gm on gm.gender_id=pr.gender_id  "
				+ " inner join adt.t_admission_details admissionDetails on admission.admission_id=admissionDetails.admission_id  "
				+ " LEFT join adt.m_bed_master bm on admissionDetails.bed_id=bm.bed_id "
				+ " inner join adt.m_bed_category_master bcm on bcm.bed_category_id=bm.bed_category_id"
				+ " LEFT join adt.m_ward_master as ward on bm.ward_id=ward.ward_id "
				+ " LEFT JOIN adt.t_nursing_station_ward_mapper nur on nur.ward_id= ward.ward_id "
				+ " LEFT JOIN adt.m_floor_master floor on floor.floor_id=bm.floor_id "
				+ " INNER JOIN doctor.m_doctor_master doc on discharge.doctor_id =doc.doctor_id "
				+ " LEFT JOIN doctor.m_speciality_master sm on admission.speciality_id=sm.speciality_id "
				+ " INNER JOIN adt.m_discharge_summary dsumm on dsumm.admission_id=discharge.admission_id "
				+ " INNER JOIN public.m_organization_master orgMas on orgMas.organization_id=discharge.organization_id "
				+ " INNER JOIN public.m_unit_master unitMas on unitMas.unit_id=discharge.unit_id "
				+ " WHERE discharge.admission_id=:admissionId and dsumm.status='A' order by discharge.discharge_id desc limit 1" ),
	
	
	@NamedNativeQuery(name = "GET_APPOINTMENT_BASIC_DETAILS_TO_SYNC",
					 query = "select cast(app.patient_id as varchar)  as \"localUhid\", app.organization_id as \"hospitalId\", 2 as \"applicationId\", "
							 + " app.unit_id as \"unitId\",patReg.uuid as \"uuid\",org.global_org_id as \"globalOrgId\",unit.global_unit_id as \"globalUnitId\" "
							 + " from opd.m_appointment_master app "
							 + "  inner join public.m_organization_master org " 
							 + " on org.organization_id=app.organization_id "  
							 + " inner join public.m_unit_master unit " 
							 + "  on unit.unit_id=app.unit_id " 
							 + " inner join patient.t_patient_registration patReg  " 
							 + " on patReg.patient_id=app.patient_id "
							 + " where app.appointment_id =:id and patReg.uuid is not null "),
	//cast(pat.patient_id as varchar) as \"localUhid\" trim(to_char(app.patient_id,'999'))
	@NamedNativeQuery(name = "GET_APPOINTMENT_SERVICE_DETAILS_TO_SYNC",
					 query = "select  enc.appointment_id as \"appointmentId\",  cast(srv.speciality_id as varchar) as \"groupType\", srv.service_standard_name as \"serviceName\", "
							 + " srv.service_master_id as \"tariffServiceId\" "
							 + " from public.t_order_details ordt  "
							 + " inner join public.t_order_master ordm "
							 + " on ordm.order_id = ordt.order_id "
							 + " inner join opd.t_encounter_master enc "
							 + " on enc.encounter_id = ordm.encounter_id "
							 + " inner join service.m_service_master srv "
							 + " on srv.service_master_id = ordt.service_id "
							 //+ " where enc.appointment_id =:id and srv.speciality_id!=8 and srv.speciality_id!=1000 "),
	                         + " where enc.appointment_id =:id "
	                         + " and srv.speciality_id!=(select tlc.lab_speciality_id from t_lab_configuration tlc"
	                         + " where tlc.unit_id =ordt.org_unit_id and tlc.status='A') "),
	
	@NamedNativeQuery(name = "GET_PATHALOGY_RELEASED_REPORT",
/*	 query = " select  test_mst.test_desc AS \"serviceName\", labresdet.final_result as \"sampleresult\", "
	 		+ " cast(labresmst.patient_id as varchar) as \"localUhid\",labresmst.lab_test_res_id as \"labResultId\","
	 		+ " cast(lbsampledtls.dept_id  as varchar) as \"groupType\", 2 as \"applicationId\","
	 		+ " lbsampledtls.org_unit_id as \"unitId\",lbsampledtls.org_id as \"hospitalId\" "
			 + "  from lab.t_lab_sample_dtls lbsampledtls  "
			 + " INNER JOIN lab.t_lab_sample_master sample_mst ON lbsampledtls.lab_sample_id = sample_mst.lab_sample_id "
			 + " INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id  "
			 + " LEFT JOIN lab.t_lab_test_result labresmst ON labresmst.lab_sample_dtls_id = lbsampledtls.lab_sample_dtls_id "
			 + " INNER JOIN lab.t_lab_sample_master labsammst ON labsammst.lab_sample_id = lbsampledtls.lab_sample_id "
			 + " INNER JOIN public.t_order_master ord_mst ON ord_mst.order_id = lbsampledtls.order_id "
			 + " LEFT JOIN doctor.m_doctor_master doct_mst ON lbsampledtls.doctor_id = doct_mst.doctor_id"
			 + " LEFT JOIN lab.m_priority_master pri_mst ON lbsampledtls.priority_id = pri_mst.priority_id"
			 + " LEFT JOIN doctor.m_sub_speciality_master subspe_mst ON subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id"
			 + " INNER join lab.t_lab_res_dtls labresdet on labresdet.lab_result_id=labresmst.lab_test_res_id "
			 + " where labresmst.sample_status_id=8  and labresmst.my_life_sync_flag='N' "*/
			query =" select test_mst.test_desc AS \"serviceName\"," 
					+" cast(labresmst.patient_id as varchar) as \"localUhid\",labresmst.lab_test_res_id as \"labResultId\"," 
					+" cast(lbsampledtls.dept_id as varchar) as \"groupType\", 2 as \"applicationId\","  
					+" lbsampledtls.org_unit_id as \"unitId\",lbsampledtls.org_id as \"hospitalId\"," 
					+" tpr.uuid as \"uuid\",mom.global_org_id as \"globalOrgId\",mum.global_unit_id as \"globalUnitId\" " 
					+" from  lab.t_lab_test_result  labresmst  " 
					+" INNER JOIN lab.t_lab_sample_dtls lbsampledtls  on labresmst.lab_sample_dtls_id = lbsampledtls.lab_sample_dtls_id " 
					+" INNER JOIN lab.t_lab_sample_master labsammst ON labsammst.lab_sample_id = lbsampledtls.lab_sample_id " 
					+" INNER JOIN lab.m_test_master test_mst ON lbsampledtls.test_id = test_mst.test_id " 
					+" INNER JOIN patient.t_patient_registration tpr on tpr.patient_id =labresmst.patient_id " 
					+" INNER JOIN public.m_organization_master mom on mom.organization_id =lbsampledtls.org_id " 
					+" INNER JOIN public.m_unit_master mum on  mum.unit_id =lbsampledtls.org_unit_id "  
					+" where labresmst.sample_status_id=8 and labresmst.my_life_sync_flag='N' and tpr.uuid is not null " 
			),
	
	@NamedNativeQuery(name = "GET_APPOINTMENT_VITAL_DETAILS_TO_SYNC",
    query = "select enc.appointment_id as \"appointmentId\", "
          + " vtl.is_self_triage as \"isSelfTriage\", "
          + " vtl.status as \"status\",  "
          + " vtl.value as \"value\", "
          + " vtlm.my_life_vital_id as \"vitalId\", "
          + " vtlm.vital_name as \"vitalName\", "
          + " to_char(vtl.created_date,'DD-MM-YYYY HH:mm:ss') as \"createdDate\" "
          + " from patient.t_appointment_vital_mapper vtl "
          + " inner join opd.t_encounter_master enc "
          + " on enc.encounter_id = vtl.encounter_id "
          + " inner join emr.m_vital_master vtlm "
          + " on vtlm.vital_id = vtl.vital_id "
          + " where enc.appointment_id = :id and vtl.value <> '0' "),
         //+ " and vtl.vital_id between 1 and 8 "),
	
	
	@NamedNativeQuery(name = "GET_MEDICATIONS_TO_SYNC",
					 query = "select em.appointment_id as \"appointmentId\", to_char(pfd.stop_date,'DD-MM-YYYY') as \"endDate\", fm.description as \"frequency\", "
							 + "  gene.name as \"genericName\", item.name as \"medicationName\", coalesce (inst.instruction_desc,'') as \"remark\", "
							 + "   to_char(pfd.start_date, 'DD-MM-YYYY') as \"startDate\"  "
							 + "  from pharmacy.t_prescription p "
							 + " inner join pharmacy.t_prescription_detail pd on pd.prescription_id = p.id "
							 + " inner join pharmacy.t_prescription_frequency_detail pfd on pfd.prescription_detail_id = pd.id  "
							 + " inner join public.m_frequency fm on fm.id = pfd.frequency_id "
							 + " inner join public.m_item item on item.id = pd.item_id "
							 + " inner join public.m_generic gene on gene.id = item.generic_id "
							 + " left join public.m_instruction inst on inst.instruction_id = pd.instruction_id "
							 + " inner join opd.t_encounter_master em on em.encounter_id = p.encounter_id "
							 + " where em.appointment_id = :id "),
	
	@NamedNativeQuery(name = "GET_DIAGNOSIS_DETAILS_TO_SYNC",
					 query = "select em.appointment_id as \"appointmentId\",  patientDiag.diagnosis_id as \"diagnosisId\",   "
							 + " to_char(patientDiag.created_date, 'DD-MM-YYYY') as \"createdDate\", to_char(patientDiag.created_date, 'DD-MM-YYYY') as \"diagnosisDate\", "
							 + " cast(patientDiag.type as varchar) as \"diagnosisType\", "
							 + " dm.diagnosis_desc as \"diagnosis\", patientDiag.remark as \"remark\", patientDiag.status as \"status\" "
							 + " from patient.t_patient_diagnosis_appo_mapper patientDiag "
							 + " inner join emr.m_diagnosis_master dm on dm.diagnosis_id = patientDiag.diagnosis_id "
							 + " inner join opd.t_encounter_master em on patientDiag.encounter_id = em.encounter_id "
							 + " where em.appointment_id = :id "),

	@NamedNativeQuery(name="GET_SCHEDULED_CYCLIC_APPOINTMENTS_WORKLIST",query=""
			+ "SELECT to_char(am.appointment_date,'DD/MM/YYYY') as \"appointmentDate\", "
			//+ " (case when (am.slot_id is null) then (to_char(cam.appointment_time,'HH24:MI')) else (to_char(sm.from_time,'HH24:MI')) end) as \"appointmentTime\","
			+ "to_char(cam.appointment_time,'HH24:MI') as \"appointmentTime\",admDet.billing_bed_category_id as \"bedId\","//here bedId is billingbedCategoryId
			+ "cam.available_quantity as \"availableQuantity\","
			+ "ordM.encounter_id as \"encounterId\","
			+ "ord.service_id as \"serviceId\","
			+ "ord.order_details_id as \"orderDetailsId\","
			+ "am.appointment_id as \"appointmentId\",am.admission_id as \"admissionId\", "
			+ "am.appointment_type_id as \"appointmentTypeId\", "
			+ "am.front_desk_arrival as \"frontDeskArrival\", "
			+ "am.appointment_status_id as \"appointmentStatusId\", "
			+ "asm.appointment_status_desc as \"appointmentStatusDesc\", "
			+ "patient.is_vip as \"isVip\", "
			+ "(case when patient.is_vip='Y' then "
			+ "coalesce(patient.alise_name,patient.first_name) "
			+ "else "
			+ "concat(coalesce(patient.first_name) , ' ', coalesce(patient.middle_name) , ' ', coalesce(patient.last_name)) "
			+ "end	) as \"patientName\", "
			+ "concat(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)),'999'), 'Y') as \"age\", "
			+ "patient.mobile_number as \"mobileNumber\", "
			+ "patient.email as \"email\", "
			+ "patient.patient_id as \"patientId\", "
			+ "patient.country_calling_code as \"countryCallingCode\", "
			+ "concat(extract(year from AGE(current_timestamp, patient.dob))) as \"ageYear\", "
			+ "patient.uhid_number as \"uhidNumber\", "
			+ "patient.image_path as \"imagePath\", "
			+ "patient.prefix_id as \"prefixId\", "
			+ "coalesce(prefix.prefix_code) as \"prefixCode\", " 
			+ "coalesce(prefix.prefix_desc) as \"prefixDesc\", " 
			+ "patient.first_name as \"firstName\", "
			+ "patient.middle_name as \"middleName\", "
			+ "patient.last_name as \"lastName\", "
			+ "patient.gender_id as \"genderId\", "
			+ "patientDetails.country_id as \"countryId\", "
			+ "patientDetails.state_id as \"stateId\", "
			+ "patientDetails.district_id as \"districtId\", "
			+ "patientDetails.city_id as \"cityId\", "
			+ "patientDetails.area_id as \"areaId\", "
			+ "patientDetails.zip_code as \"zipCode\", "
			+ "patientDetails.address as \"address\", "
			+ "patientDetails.marital_status_id as \"maritalStatusId\", "
			+ "gender.gender_code as \"genderCode\", "
			+ "patient.registration_type_id as \"registrationTypeId\", "
			+ "to_char(patient.dob,'MM/DD/YYYY') as \"dob\", "
			+ "patient.patient_category_id as \"patientCategoryId\","
			+ "msm.service_standard_code as \"serviceCode\", "  
			+ " msm.service_standard_name as \"serviceStandardName\","
			+ " to_char(mbm.bill_date_time,'DD/MM/YYYY') as \"billDate\"," 
			+" mbm.billing_master_id as \"billId\" ," 
			+" mbm.bill_number as \"billNumber\",adm.visit_type_id as \"visitTypeId\", "
			+ "ord.ord_doc_id as \"doctorId\",ord.ord_doc_spl_id as \"specialityId\",usmd.is_doc_req as \"isDoctorShareReq\","
			+ "msm.speciality_id as \"serviceSpecialityId\" "
			+ "FROM opd.m_appointment_master am "
			+ "LEFT JOIN patient.t_patient_registration patient on am.patient_id=patient.patient_id "
			+ "LEFT JOIN patient.t_patient_details patientDetails on patient.patient_id=patientDetails.patient_id "
			+ "LEFT JOIN public.m_gender_master gender on patient.gender_id=gender.gender_id "
			+ "LEFT JOIN public.m_prefix_master prefix on patient.prefix_id=prefix.prefix_id "
			+ "INNER JOIN opd.m_appointment_status_master asm on am.appointment_status_id=asm.appointment_status_id "
			+ "LEFT OUTER JOIN adt.t_admission adm on adm.admission_id = am.admission_id AND am.appointment_status_id=1 AND adm.status='A' "
			+ "LEFT join adt.t_admission_details admDet on admDet.admission_id =am.admission_id and admDet.status='A' "
			+ "inner join service.m_service_master as msm on msm.service_master_id = am.service_id "
			+ "LEFT JOIN opd.m_slot_master sm on am.slot_id=sm.slot_id "
			+ "INNER join opd.t_cyclic_appointment_mapper cam on cam.appointment_id=am.appointment_id "
			+" INNER join public.t_order_details ord on ord.order_details_id=cam.order_detail_id "
			+ "LEFT join billing.t_billing_details tbd on " 
			+"  tbd.order_details_id=ord.order_details_id "
			+"LEFT join billing.m_billing_master as mbm on "
			+" mbm.billing_master_id = tbd.billing_master_id and mbm.bill_cancelled = 'N' "
			+ " inner join service.t_unit_service_master_details usmd on usmd.service_master_id=msm.service_master_id and usmd.unit_id=ord.org_unit_id and usmd.status='A' "
			//+ " INNER join opd.m_appointment_master enc on enc.appointment_id=am.appointment_id " 
			+ " LEFT join public.t_order_master ordM on ordM.order_id=ord.order_id "
			+ " LEFT join t_unit_application_billing_configuration appConfig on "
			+ " appConfig.unit_id = ordM.org_unit_id  "
			),
	
	@NamedNativeQuery(name = "GET_MEDICATION_MAPPER_DETAILS_BY_ADMISSION_ID_FOR_MY_LIFE",
	 query = "select adt.admission_id as \"admissionId\", to_char(pfd.stop_date,'DD-MM-YYYY') as \"endDate\", fm.description as \"frequency\", "
			 + "  gene.name as \"genericName\", item.name as \"medicationName\", inst.instruction_desc as \"remark\", "
			 + "   to_char(pfd.start_date, 'DD-MM-YYYY') as \"startDate\", "
			 + "cast((case when (select mh.medication_history_id from opd.t_medication_history mh where mh.admission_id = adt.admission_id limit 1 ) is null " + 
			 "   then  'N' " + 
			 "	 else " + 
			 "	 'Y' " + 
			 "	 end ) as char)  as \"historyFlag\","
			 + " p.unit_id as \"unitId\" "
			 + "  from pharmacy.t_prescription p "
			 + " inner join pharmacy.t_prescription_detail pd on pd.prescription_id = p.id "
			 + " inner join pharmacy.t_prescription_frequency_detail pfd on pfd.prescription_detail_id = pd.id  "
			 + " inner join public.m_frequency fm on fm.id = pfd.frequency_id "
			 + " inner join public.m_item item on item.id = pd.item_id "
			 + " left join public.m_generic gene on gene.id = item.generic_id "
			 + " left join public.m_instruction inst on inst.instruction_id = pd.instruction_id "
			 + "  inner join adt.t_admission adt on adt.admission_id = p.admission_id  "
			 + " where adt.admission_id = :admissionId "),
	
	@NamedNativeQuery(name = "GET_SERVICE_DETAILS_BY_ADMISSION_ID_FOR_MY_LIFE",
	 query = "select  adt.admission_id as \"admissionId\",  cast(srv.speciality_id as varchar) as \"groupType\", srv.service_standard_name as \"serviceName\", "
			 + " srv.service_master_id as \"tariffServiceId\","
			 + " ordt.order_id as \"serviceOrderId\","
			// + " ordt.tariff_id as \"tariffServiceId\","
			 + " labsam.sample_status_id as \"sampleStatusId\","
			 + " ordt.org_unit_id as \"unitId\", "
			 + " srv.service_standard_name  as \"tariffService\" "
			 + " from public.t_order_details ordt  "
			 + " inner join public.t_order_master ordm on ordm.order_id = ordt.order_id "
			 + " inner join adt.t_admission adt  on adt.admission_id = ordm.admission_id "
			 + " inner join service.m_service_master srv on srv.service_master_id = ordt.service_id "
			 + " left join lab.t_lab_sample_dtls labsam on labsam.order_details_id = ordt.order_details_id "
			// + " inner join service.m_tariff_master tariff on tariff.tariff_id = ordt.tariff_id "
             + " where adt.admission_id =:admissionId and srv.speciality_id!=8 "),
	
	@NamedNativeQuery(name = "GET_VITAL_DETAILS_BY_ADMISSION_ID_FOR_MY_LIFE",
	 query = "select adt.admission_id as \"admissionId\", vtl.is_self_triage as \"isSelfTriage\", vtl.status as \"status\",  "
			 + " vtl.value as \"value\", vtl.vital_id as \"vitalId\", vtlm.vital_name as \"vitalName\","
			 + " vtl.unit_id  as \"unitId\" "
			 + " from patient.t_appointment_vital_mapper vtl "
			 + " inner join adt.t_admission adt  on adt.admission_id = vtl.admission_id "
			 + " inner join emr.m_vital_master vtlm on vtlm.vital_id = vtl.vital_id "
			 + " where adt.admission_id = :admissionId and cast(vtl.value as float) <> 0 "),
	
	@NamedNativeQuery(name = "GET_DIAGNOSIS_DETAILS_BY_ADMISSION_ID_FOR_MY_LIFE",
	 query = "select adt.admission_id as \"admissionId\",  patientDiag.diagnosis_id as \"diagnosisId\",   "
			 + " to_char(patientDiag.created_date, 'DD-MM-YYYY') as \"createdDate\", to_char(patientDiag.created_date, 'DD-MM-YYYY') as \"diagnosisDate\", "
			 + " cast(patientDiag.type as varchar) as \"diagnosisType\", "
			 + " dm.diagnosis_desc as \"diagnosis\", patientDiag.remark as \"remark\", patientDiag.status as \"status\","
			 + " patientDiag.resolved_remark as \"comment\",  patientDiag.unit_id as \"unitId\" "
			 + " from patient.t_patient_diagnosis_appo_mapper patientDiag "
			 + " inner join emr.m_diagnosis_master dm on dm.diagnosis_id = patientDiag.diagnosis_id "
			 + " inner join adt.t_admission adt on patientDiag.admission_id = adt.admission_id "
			 + " where adt.admission_id = :admissionId "),
			
})


@Entity
@Table(name="m_appointment_master", schema="opd")
@SequenceGenerator(name = "appointment_scheduling_master_seq", sequenceName = "opd.appointment_scheduling_master_seq", allocationSize = 1)
public class AppointmentMaster {
	@Id
	@Column(name="appointment_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointment_scheduling_master_seq")
	private int appointmentId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name="slot_id")
	private Integer slotId;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="appointment_status_id")
	private Integer appointmentStatusId;
	
	@Column(name="cancel_reason_id")
	private Integer cancelReasonId;
	
	@Column(name="cancelled_by")
	private Character cancelledBy;
	
	@Column(name="reschedule_reason_id")
	private Integer rescheduleReasonId;
	
	@Column(name="rescheduled_by")
	private Character rescheduledBy;
	
	@Column(name="appointment_type_id")
	private Integer appointmentTypeId;
	
	@Column(name="priority_id")
	private Integer priorityId;
	
	@Column(name="ehc_package_id")
	private Integer ehcPackageId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "counter_id")
	private Integer counterId;
	
	@Column(name="arrival_time")
	private Date arrivalTime;
	
	@Column(name="appointment_date")
	private Date appointmentDate;
	
	@Column(name="token_number")
	private Integer tokenNumber;
	
	@Column(name="dummy_token_number")
	private Integer dummyTokenNumber;
	
	@Column(name="call_count")
	private Integer callCount;
	
	@Column(name="is_direct_encounter")
	private Character isDirectEncounter;
	
	@Column(name="front_desk_arrival")
	private Character frontDeskArrival;
	
	@Column(name="order_details_id")
	private Integer orderDetailsId;
	
	@Column(name = "encounter_id")
	private Integer encounterId;
	
	@Column(name="is_appointment_from_my_life")
	private Character isAppointmentFromMyLife;
	
	@Column(name="is_paid_appointment")
	private Character isPaidAppointment;
	
	@Column(name="service_id")
	private Integer serviceId;
	
	@Column(name="is_cyclic_appointment")
	private Character isCyclicAppointment;
	
	@Column(name="reason_for_appointment_id")
	private Integer reasonForAppointmentId;
	
	@Column(name="is_from_discharge_summary")
	private Character isFromDischargeSummary;
	
	@Column(name="against_admission_id")
	private Integer againstAdmissionId;
	
	@Column(name="app_date_time")
	private Date appDateTime;
	
	@Column(name="is_teleconsultation_apppointment")
	private Integer isTeleconsultationAppointment;
	
	@Column(name="coversheet_ml_file_path")
	private String coversheetMlFilePath;
	
	@Column(name="appointment_unique_id")
	private Integer appointmentUniqueId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "slot_id", insertable = false, nullable = false, updatable = false)
	private SlotMaster slotMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, nullable = false, updatable = false)
	private PatientRegistration patientRegistration;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "appointment_status_id", insertable = false, nullable = false, updatable = false)
	private AppointmentStatusMaster appointmentStatusMaster;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "appointmentMaster", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<EncounterMaster> encounterMasterList;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_details_id" , insertable = false , updatable = false , nullable = false)
	private OrderDetailsMaster orderDetailsMaster;
	
	public Date getAppDateTime() {
		return appDateTime;
	}

	public void setAppDateTime(Date appDateTime) {
		this.appDateTime = appDateTime;
	}

	public Integer getAgainstAdmissionId() {
		return againstAdmissionId;
	}

	public void setAgainstAdmissionId(Integer againstAdmissionId) {
		this.againstAdmissionId = againstAdmissionId;
	}

	public Character getIsFromDischargeSummary() {
		return isFromDischargeSummary;
	}

	public void setIsFromDischargeSummary(Character isFromDischargeSummary) {
		this.isFromDischargeSummary = isFromDischargeSummary;
	}

	public Integer getReasonForAppointmentId() {
		return reasonForAppointmentId;
	}

	public void setReasonForAppointmentId(Integer reasonForAppointmentId) {
		this.reasonForAppointmentId = reasonForAppointmentId;
	}

	public Character getIsPaidAppointment() {
		return isPaidAppointment;
	}

	public void setIsPaidAppointment(Character isPaidAppointment) {
		this.isPaidAppointment = isPaidAppointment;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Character getFrontDeskArrival() {
		return frontDeskArrival;
	}

	public void setFrontDeskArrival(Character frontDeskArrival) {
		this.frontDeskArrival = frontDeskArrival;
	}

	public Character getIsDirectEncounter() {
		return isDirectEncounter;
	}

	public void setIsDirectEncounter(Character isDirectEncounter) {
		this.isDirectEncounter = isDirectEncounter;
	}

	public Integer getDummyTokenNumber() {
		return dummyTokenNumber;
	}

	public void setDummyTokenNumber(Integer dummyTokenNumber) {
		this.dummyTokenNumber = dummyTokenNumber;
	}

	public Integer getCallCount() {
		return callCount;
	}

	public void setCallCount(Integer callCount) {
		this.callCount = callCount;
	}

	public Integer getTokenNumber() {
		return tokenNumber;
	}

	public void setTokenNumber(Integer tokenNumber) {
		this.tokenNumber = tokenNumber;
	}

	public Date getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(Date appointmentDate) {
		
		String date = GlobalCommonDateUtils.getStringDate(appointmentDate, "yyyy-MM-dd");
		this.appointmentDate = GlobalCommonDateUtils.getDate(date,"yyyy-MM-dd");
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEhcPackageId() {
		return ehcPackageId;
	}

	public void setEhcPackageId(Integer ehcPackageId) {
		this.ehcPackageId = ehcPackageId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Integer getAppointmentTypeId() {
		return appointmentTypeId;
	}

	public void setAppointmentTypeId(Integer appointmentTypeId) {
		this.appointmentTypeId = appointmentTypeId;
	}

	public Integer getRescheduleReasonId() {
		return rescheduleReasonId;
	}

	public void setRescheduleReasonId(Integer rescheduleReasonId) {
		this.rescheduleReasonId = rescheduleReasonId;
	}

	public Character getRescheduledBy() {
		return rescheduledBy;
	}

	public void setRescheduledBy(Character rescheduledBy) {
		this.rescheduledBy = rescheduledBy;
	}

	public int getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
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

	public Integer getSlotId() {
		return slotId;
	}

	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public Integer getAppointmentStatusId() {
		return appointmentStatusId;
	}

	public void setAppointmentStatusId(Integer appointmentStatusId) {
		this.appointmentStatusId = appointmentStatusId;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public Character getCancelledBy() {
		return cancelledBy;
	}

	public void setCancelledBy(Character cancelledBy) {
		this.cancelledBy = cancelledBy;
	}

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Character getIsAppointmentFromMyLife() {
		return isAppointmentFromMyLife;
	}

	public void setIsAppointmentFromMyLife(Character isAppointmentFromMyLife) {
		this.isAppointmentFromMyLife = isAppointmentFromMyLife;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Character getIsCyclicAppointment() {
		return isCyclicAppointment;
	}

	public void setIsCyclicAppointment(Character isCyclicAppointment) {
		this.isCyclicAppointment=isCyclicAppointment;
	}

	public OrderDetailsMaster getOrderDetailsMaster() {
		return orderDetailsMaster;
	}

	public void setOrderDetailsMaster(OrderDetailsMaster orderDetailsMaster) {
		this.orderDetailsMaster = orderDetailsMaster;
	}

	public Integer getIsTeleconsultationAppointment() {
		return isTeleconsultationAppointment;
	}

	public void setIsTeleconsultationAppointment(Integer isTeleconsultationAppointment) {
		this.isTeleconsultationAppointment = isTeleconsultationAppointment;
	}

	public String getCoversheetMlFilePath() {
		return coversheetMlFilePath;
	}

	public void setCoversheetMlFilePath(String coversheetMlFilePath) {
		this.coversheetMlFilePath = coversheetMlFilePath;
	}

	public Integer getAppointmentUniqueId() {
		return appointmentUniqueId;
	}

	public void setAppointmentUniqueId(Integer appointmentUniqueId) {
		this.appointmentUniqueId = appointmentUniqueId;
	}


	

	
	/*public List<ComplaintAppointmentMapper> getComplaintAppointmentMapperList() {
		return ComplaintAppointmentMapperList;
	}

	public void setComplaintAppointmentMapperList(
			List<ComplaintAppointmentMapper> complaintAppointmentMapperList) {
		ComplaintAppointmentMapperList = complaintAppointmentMapperList;
	}*/

	/*public List<PatientFamilyHistory> getListPatientFamilyHistory() {
		return listPatientFamilyHistory;
	}

	public void setListPatientFamilyHistory(
			List<PatientFamilyHistory> listPatientFamilyHistory) {
		this.listPatientFamilyHistory = listPatientFamilyHistory;
	}
	*/
	
	
}

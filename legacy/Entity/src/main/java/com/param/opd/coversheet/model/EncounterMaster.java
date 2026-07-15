package com.param.opd.coversheet.model;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
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

import com.param.billing.global.transaction.model.BillingMaster;
import com.param.global.common.DateConverter;
import com.param.global.common.LocalTimeConverter;
import com.param.global.model.ClinicMaster;
import com.param.global.model.DoctorMaster;
import com.param.global.model.KinDetails;
import com.param.global.model.ModalityMaster;
import com.param.global.model.OrderMaster;
import com.param.global.model.PatientRegistration;
import com.param.global.model.PatientSponsorDetails;
import com.param.global.model.PaymentEntitlementMaster;
import com.param.global.model.ReferalDetails;
import com.param.global.model.SpecialityMaster;
import com.param.global.model.SubSpecialityMaster;
import com.param.opd.appointment.model.AppointmentMaster;
import com.param.global.common.InstantConverter;

@NamedNativeQueries({
	@NamedNativeQuery(name="GET_ENCOUNTER_DETAILS_BY_ID", 
			query="SELECT encounter.encounter_id as \"encounterId\", "
					+ "encounter.patient_id as \"patientId\", "
					+ "encounter.user_defined_visit_type_id as \"userDefinedVisitTypeId\", "
					+ " encounter.doctor_id as \"doctorId\", "
					+ "encounter.encounter_date as \"encounterDateTime\", "
					+ "encounter.dept_id as \"deptId\","
					+ " encounter.encounter_number as \"encounterNumber\","
					+ "	concat(coalesce(doctor.first_name,''),' ',coalesce(doctor.middle_name,''),' ',coalesce(doctor.last_name,'')) as \"doctorName\", "
					+ " speciality.speciality_name as \"specialityName\""
					+ " FROM opd.t_encounter_master encounter "
					+ " LEFT JOIN doctor.m_doctor_master doctor "
					+ " 	ON doctor.doctor_id = encounter.doctor_id "
					/*+ " LEFT JOIN doctor.t_doctor_speciality_mapper mapper "
					+ " 	ON encounter.doctor_id = doctor.doctor_id "*/
					+ " LEFT JOIN doctor.m_speciality_master speciality"
					+ "		ON speciality.speciality_id = encounter.dept_id "
					+ " WHERE encounter.encounter_id=:encounterId" ),
					
					
							@NamedNativeQuery(name="GET_SEARCH_ENCOUNTERED_LIST",query=
							" select " 
							 + " 	enMst.encounter_id as \"encounterId\", " 
							 + " 	(case " 
							 + " 		when enMst.encounter_type_id = 6 then enMst.review_against_enc_id " 
							 + " 		when enMst.against_enc_id is not null then enMst.against_enc_id " 
							 + " 		else enMst.encounter_id " 
							 + " 	end) as \"openCoversheetWithEncounterId\", " 
							 + " 	enMst.against_enc_id as \"againstEncId\", " 
							 + " 	enMst.encounter_number as \"encounterNumber\", " 
							 + " 	enMst.encounter_date as \"encounterDate\", " 
							 + " 	(case " 
							 + " 		when am.is_paid_appointment = 'Y' then to_char(am.arrival_time, 'YYYY-MM-DD HH24:MI') " 
							 + " 		else to_char(enMst.encounter_date, 'YYYY-MM-DD HH24:MI') " 
							 + " 	end) as \"encounterDateWithTime\", " 
							 + " 	(case " 
							 + " 		when am.is_paid_appointment = 'Y' then to_char(am.arrival_time, 'DD/MM/YYYY HH24:MI:SS') " 
							 + " 		else to_char(enMst.encounter_date, 'DD/MM/YYYY HH24:MI:SS') " 
							 + " 	end) as \"encounterDateWithTimeString\", " 
							 + " 	(case " 
							 + " 		when am.is_paid_appointment = 'Y' then to_char(am.arrival_time, 'HH24:MI:SS') " 
							 + " 		else to_char(enMst.encounter_date, 'HH24:MI:SS') " 
							 + " 	end) as \"encounterTime\", " 
							 + " 	enMst.user_defined_visit_type_id as \"userDefinedVisitTypeId\" , " 
							 + " 	enMst.status as \"status\", " 
							 + " 	enMst.status_id as \"statusId\", " 
							 + " 	enMst.is_called as \"isCalled\", " 
							 + " 	enMst.call_count as \"callCount\", " 
							 + " 	enMst.cabin_id as \"cabinId\", " 
							 + " 	enMst.doctor_desk_arrival as \"doctorDeskArrival\", " 
							 + " 	patient.is_vip as \"isVip\", " 
							 + " 	patient.first_name as \"firstName\", " 
							 + " 	patient.middle_name as \"middleName\", " 
							 + " 	patient.uuid as \"uuid\", " 
							 + " 	patient.last_name as \"lastName\", " 
							 + " 	patient.health_number as \"healthNumber\", " 
							 + " 	patient.health_id as \"healthId\", "
							 + " 	(case " 
							 + " 		when patient.is_vip = 'Y' then coalesce(patient.alise_name, patient.first_name) " 
							 + " 		else concat(coalesce(patient.first_name) , ' ', coalesce(patient.middle_name) , ' ', coalesce(patient.last_name)) " 
							 + " 	end ) as \"patientName\", " 
							 + " 	to_char(sm.appointment_date, 'dd/MM/yyyy') as \"appointmentDate\", " 
							 + " 	am.appointment_id as \"appointmentId\", " 
							 + " 	sm.slot_id as \"slotId\", " 
							 + " 	concat(to_char(sm.from_time, 'HH24:MI')) as \"fromTimeString\", " 
							 + " 	concat(to_char(sm.to_time, 'HH24:MI')) as \"toTimeString\", " 
							 + " 	am.remark as \"remark\", " 
							 + " 	enMst.doctor_id as \"doctorId\", " 
							 + " 	enMst.payment_entitlement_type_id as \"paymentEntitlementTypeId\", " 
							 + " 	am.appointment_type_id as \"appointmentTypeId\", " 
							 + " 	enMst.encounter_type_id as \"encounterTypeId\", " 
							 + " 	am.appointment_status_id as \"appointmentStatusId\", " 
							 + " 	asm.appointment_status_desc as \"appointmentStatusDesc\", " 
							 + " 	concat('Dr. ', coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name)) as \"doctorName\", " 
							 + " 	enMst.preferred_doc_id as \"preferredDocId\", " 
							 + " 	concat('Dr. ', coalesce(prefDoc.first_name), ' ', coalesce(prefDoc.middle_name), ' ', coalesce(prefDoc.last_name)) as \"preferedDocName\", " 
							 + " 	concat('Dr. ', coalesce(doc.first_name), ' ' , coalesce(doc.last_name)) as \"docNameTemp\", " 
							 + " 	enMst.dept_id as \"specialityId\", " 
							 + " 	speciaityMaster.speciality_name as \"specialityName\", " 
							 + " 	concat(extract(year from age(current_timestamp, patient.dob)) , 'Y ', extract(month from age(current_timestamp, patient.dob)) , 'M ' , extract(day from age(current_timestamp, patient.dob)), 'D ') as \"age\", " 
							 + " 	concat(extract(year from age(current_timestamp, patient.dob))) as \"ageYear\", " 
							 + " 	patient.mobile_number as \"mobileNumber\", " 
							 + " 	patient.email as \"email\", "
		                     + "    patient.blood_group as \"bloodGroup\", "
							 + "    patient.video_call_url as \"videoCallUrl\", " 
							 + " 	doc.email as \"emailDoctor\", " 
							 + " 	patient.patient_id as \"patientId\", " 
							 + " 	patient.uhid_number as \"uhidNumber\", " 
							 + " 	patient.prefix_id as \"prefixId\", " 
							 + " 	prefix.prefix_code as \"prefixCode\", " 
							 + " 	patient.gender_id as \"genderId\", " 
							 + " 	patient.image_path as \"imagePath\", " 
							 + " 	patient.patient_category_id as \"patientCategoryId\", " 
							 + " 	patient.mpshah_patient_id as \"mpshahPatientId\", " 
							 + " 	tom.visit_type_id as \"visitTypeId\", "
							 + " 	visitTypeMaster.visit_type_code as \"visitTypeName\", "
							 + " 	gender.gender_code as \"genderCode\", "
							 + " 	gender.gender_name as \"genderDec\", " 
							 + " 	to_char(patient.dob, 'DD/MM/YYYY') as \"dob\", " 
							 + " 	to_char(patient.dob, 'MM/DD/YYYY') as \"dobForDatePicker\", " 
							 + " 	sm.modality_id as \"modalityId\", " 
							 + " 	modality.modality_desc as \"modalityDesc\", " 
							 + " 	enDet.is_assignment_comp_status as \"isAssignmentCompStatus\", " 
							 + " 	enDet.is_consultation_status as \"isConsultationStatus\", " 
							 + " 	to_char(enDet.consultation_start_time, 'HH24:MI:SS') as \"consultationStartTime\", " 
							 + " 	to_char(enDet.consultation_start_time, 'YYYY-MM-DD HH24:MI') as \"consultationStartTimeWithTime\", " 
							 + " 	to_char(enDet.consultation_stop_time, 'HH24:MI:SS') as \"consultationStopTime\", " 
							 + " 	to_char(enDet.consultation_start_time, 'dd-MM-yyyy HH:MM:SS') as \"consultationStartTimeDate\", " 
							 + " 	to_char(enDet.consultation_stop_time, 'dd-MM-yyyy HH:MM:SS') as \"consultationStopTimeDate\", " 
							/* + " 	refrlTypeMst.referral_type_name as \"referralTypeName\", " 
							 + " 	reflMst.referral_name as \"referralName\", "
							 + "	reflMst.referral_id as \"referralId\", " 
							 + " 	refrDtls.referral_remark as \"referralRemark\", " */
							 + " 	to_char(enDet.assignment_start_time, 'HH:MM:SS') as \"assignmentStartTime\", " 
							 + " 	to_char(enDet.assignment_start_time, 'dd-MM-yyyy HH:MM:SS') as \"assignmentStartTimeDate\", " 
							 + " 	to_char(enDet.assignment_stop_time, 'HH:MM:SS') as \"assignmentStopTime\", " 
							 + " 	to_char(enDet.assignment_stop_time, 'dd-MM-yyyy HH:MM:SS') as \"assignmentStopTimeDate\", " 
							 + " 	enDet.assignment_start_by as \"assignmentStartBy\", " 
							 + " 	enDet.assignment_stop_by as \"assignmentStopBy\", " 
							 + " 	enDet.assignment_start_role_id as \"assignmentStartRoleId\", " 
							 + " 	enDet.assignment_stop_role_id as \"assignmentStopRoleId\", " 
							 + " 	enDet.consultation_start_by as \"consultationStartBy\", " 
							 + " 	enDet.consultation_start_role_id as \"consultationStartRoleId\", " 
							 + " 	enDet.encounter_details_mapper_id as \"encounterDetailsMapperId\", " 
							 + " 	spd.contract_id as \"contractId\", " 
							 + " 	spd.payee_id as \"payeeId\", " 
							 + " 	enMst.doc_queue_number as \"queueNumber\", " 
							 + " 	enMst.unit_token_number as \"tokenNumber\", " 
							 + " 	enDet.is_priority as \"isPriority\", " 
							 + " 	enMst.is_critical as \"isCritical\", " 
							 + " 	doc.allowed_over_booking_slots as \"allowedOverBookingSlots\", " 
							 + " 	enMst.speciality_queue_number as \"specialityQueueNumber\", " 
							 + " 	speciaitymapper.dept_wise_appointment as \"deptWiseAppointment\", " 
							 + " cast(mbm.billing_master_id as integer) as \"billId\", "
							 + " 	enMst.old_doc_id as \"oldDocId\", " 
							 + " 	(case " 
							 + " 		when (enMst.is_critical = 'Y' " 
							 + " 		or enDet.is_priority = 'Y') then 1 " 
							 + " 		when (enMst.is_critical = 'N' " 
							 + " 		or enDet.is_priority is null) then 2 " 
							 + " 	end) as \"isCriticalOrder\", " 
							 + " 	(case " 
							 + " 		when enDet.is_priority = 'Y' then 1 " 
							 + " 		when enDet.is_priority is null then 2 " 
							 + " 	end) as \"isPriorityOrder\", " 
//							 + " 	(case "
//							 + " 		when am.is_cyclic_appointment = 'N' then coalesce((select nursing_acceptance from adt.t_admission_details where admission_id =( select admission_id from adt.t_admission where patient_id = enMst.patient_id and status = 'A' order by 1 desc limit 1 ) order by 1 desc limit 1 ), 'N') "
//							 + " 		else 'N' "
//							 + " 	end) as \"nursingAccepatance\", "
							 + "	(case when am.is_cyclic_appointment = 'N' then coalesce(nursing.nursing_acceptance, 'N') else 'N' end) as \"nursingAccepatance\","
							 + " 	tod.service_rendered as \"serviceRenderId\", " 
							 + " 	tod.order_details_id as \"orderDetailsId\", " 
							 + " 	patient.whatsapp_no as \"whatsAppCallingNo\", "
							 + " 	patient.whatsapp_calling_code as \"whatsAppCallingCode\", " 
							 + " 	patient.skype_id as \"skypeId\", "
							 + " enMst.latest_call as \"latestCall\" ," 
							 + " 	am.is_teleconsultation_apppointment as \"isTeleconsultationAppointment\", " 
							 + " 	mbm.bill_status_id as \"billStatusId\", "
							 + "    ucdet.user_chat_details_id as \"userChatDetailsId\", "
							 + "	ucdet.appointment_id_my_life as \"appointmentIdMyLife\", "
							 + " mnsm.nursing_station_desc as \"nursingStationDesc\" "
						
							// + " 	temp.otherConsultants as \"otherConsultants\" "
							 + " from " 
							 + " 	opd.t_encounter_master enMst " 
							 //New Added End
							 + " left join adt.m_nursing_station_master mnsm on mnsm.nursing_station_id  = enMst.nursing_station_id " 
							 + " left join opd.t_encounter_details enDet on  " + 
							 "	enDet.encounter_id = enMst.encounter_id  " + 
							 "inner join opd.m_appointment_master am on  " + 
							 "	am.appointment_id = enMst.appointment_id  " + 
							 "inner join t_order_master tom on  " + 
							 "	tom.encounter_id = enMst.encounter_id  " + 
							 " inner join t_order_details tod on  " + 
							 "	tod.order_id = tom.order_id  " +
							 "	and tod.is_consultation_service = 'Y'  " + 
							 "	and tod.status = 'A'  " +
							 "inner join public.m_visit_type_master visitTypeMaster on  " +
							 "	tom.visit_type_id = visitTypeMaster.visit_type_id  " +
							 "inner join billing.t_billing_details bdtls on  " +
							 "	bdtls.order_details_id = tod.order_details_id  " + 
							 "	and bdtls.status = 'A'  " + 
							 "inner join billing.m_billing_master mbm on  " + 
							 "	mbm.billing_master_id = bdtls.billing_master_id  " + 
							 "	and mbm.status = 'A'  " + 
							 "inner join opd.t_patient_sponsor_details spd on  " + 
							 "	spd.encounter_id = enMst.encounter_id  " + 
							 "	and spd.priority_id = 1  " + 
							 "	and spd.status = 'A'  " + 
							 "left join doctor.m_speciality_master speciaityMaster on  " + 
							 "	enMst.dept_id = speciaityMaster.speciality_id  " + 
							 "inner join t_unit_speciality_mapper speciaityMapper on  " + 
							 "	enMst.dept_id = speciaityMapper.speciality_id  " + 
							 "	and speciaityMapper.unit_id = enMst.unit_id  " + 
							 "left join doctor.m_doctor_master doc on  " + 
							 "	enMst.doctor_id = doc.doctor_id  " + 
							 "inner join patient.t_patient_registration patient on  " + 
							 "	enMst.patient_id = patient.patient_id  " +
							 " LEFT JOIN LATERAL (" +
							 "    SELECT ad.nursing_acceptance " +
							 "    FROM adt.t_admission ta " +
							 "    JOIN adt.t_admission_details ad " +
							 "        ON ad.admission_id = ta.admission_id " +
							 "    WHERE ta.patient_id = enMst.patient_id " +
							 "      AND ta.status = 'A' " +
							 "    ORDER BY ta.admission_id DESC " +
							 "    LIMIT 1 " +
							 ") nursing ON true "+
							 "inner join public.m_gender_master gender on  " + 
							 "	patient.gender_id = gender.gender_id  " + 
							 "inner join public.m_prefix_master prefix on  " + 
							 "	patient.prefix_id = prefix.prefix_id  " + 
//							 "inner join (  " +
//							 "	select  " +
//							 "		patient_id ,  " +
//							 "		to_char(encounter_date, 'dd-MM-yyyy') as ddate,  " +
//							 "		min(encounter_id) FirstEntID  " +
//							 "	from  " +
//							 "		opd.t_encounter_master tem  " +
//							 "	where  " +
//							 "		status_id in (5, 6, 9)  " +
//							 "	group by  " +
//							 "		patient_id ,  " +
//							 "		to_char(encounter_date, 'dd-MM-yyyy') ) FirstEncountfoday on  " +
							 " inner join ( " +
							 "    select " +
							 "        patient_id, " +
							 "        CAST(encounter_date AS DATE) enc_date, " +
							 "        min(encounter_id) FirstEntID " +
							 "    from opd.t_encounter_master tem " +
							 "    where status_id in (5,6,9) " +
							 "      and encounter_date >= CAST(:fromDate AS DATE) " +
							 "      and encounter_date < CAST(:toDate AS DATE) + INTERVAL '1 day' " +
							 "    group by " +
							 "        patient_id, " +
							 "        CAST(encounter_date AS DATE) " +
							 "	) FirstEncountfoday on" +
							 "	FirstEncountfoday.patient_id = enMst.patient_id  " +
//							 "	and FirstEncountfoday.ddate = to_char(enMst.encounter_date, 'dd-MM-yyyy')  " +
							 "	and FirstEncountfoday.enc_date =  CAST(enMst.encounter_date AS DATE)  " +
							 "	inner join opd.t_encounter_master Firstentmst on " +
							 " Firstentmst.encounter_id = FirstEncountfoday.FirstEntID " +
						/*	 "left join public.t_referal_details refrDtls on  " +
							 "	(case  " + 
							 "		when enMst.against_enc_id is not null then refrDtls.encounter_id = enMst.against_enc_id  " + 
							 "		else refrDtls.encounter_id = enMst.encounter_id  " + 
							 "	end)  " + 
							 "	and refrDtls.status = 'A'  " + 
							 "left join adt.m_referral_type_master refrlTypeMst on  " + 
							 "	refrlTypeMst.referral_type_id = refrDtls.referral_type_id  " + 
							 "left join adt.m_referral_master reflMst on  " + 
							 "	reflMst.referral_id = refrDtls.referral_id  " + 
							 "	and reflMst.status = 'A'  " + */
							 "left join opd.m_slot_master sm on " +
							  " am.slot_id = sm.slot_id " +
							 "left join doctor.m_doctor_master prefDoc on  " +
							 "	enMst.preferred_doc_id = prefDoc.doctor_id  " +
							 "left join public.m_modality_master modality on  " + 
							 "	sm.modality_id = modality.modality_id  " + 
							 "left join opd.m_appointment_status_master asm on  " + 
							 "	asm.appointment_status_id = enMst.status_id  " + 
							 "left join billing.t_credit_note_detail tcnd on   " + 
							 "		tcnd.order_detail_id = tod.order_details_id  " + 
							 "		and tcnd.status = 'A'  " + 
							 "		and tcnd.cancel_or_discount = 1   " + 
							/* "left join (select  " + 
							 "	concat('Dr. ', coalesce(doc.first_name), ' ', coalesce(doc.last_name)) as otherConsultants, mpr.encounter_id, mpr.doctor_id   " + 
							 "from  " + 
							 "	opd.t_encounter_doctor_consultants_mapper mpr  " + 
							 "inner join doctor.m_doctor_master doc on  " + 
							 "	doc.doctor_id = mpr.doctor_id) temp on temp.encounter_id = enMst.encounter_id  " + 
							 "	and temp.doctor_id != enMst.doctor_id  " + */
							 "left join public.m_clinic_master clinMst on  " + 
							 "	clinMst.clinic_master_id = enMst.clinic_id  " + 
							 "left join opd.t_nursing_station_clinic_mapper nurStatClinMapp on  " + 
							 "	nurStatClinMapp.clinic_id = clinMst.clinic_master_id "+
							 " left join public.user_chat_details ucdet on  " + 
							 "	patient.uuid = ucdet.uuid  " 
							 + " where " 
							 + " 	enMst.organization_id =:orgId " 
							 + " 	and enMst.unit_id =:unitId " 
							 + " 	and (enMst.status_id in (5,6,9) " 
							 + " 	and mbm.bill_status_id in (2,3) " 
							 + " 	and mbm.status = 'A' " 
							 + " 	or (am.is_teleconsultation_apppointment = 1 " 
							 + " 	and mbm.bill_status_id = 3  and mbm.status = 'A'  and enMst.status_id in (5,6,9))) " 
							 + " 	and patient.registration_type_id in (1,3,4,6) " 
							 + " 	and (tod.status = 'A' and tcnd.credit_note_detail_id is null) " 
							 + " 	and am.appointment_type_id in (1,5,6,9) " ),

					
							
							@NamedNativeQuery(name="GET_SEARCH_ENCOUNTERED_LIST_FOR_VIDEO",query=
							" select " 
							 + " 	enMst.encounter_id as \"encounterId\", " 
							 + " 	(case " 
							 + " 		when enMst.encounter_type_id = 6 then enMst.review_against_enc_id " 
							 + " 		when enMst.against_enc_id is not null then enMst.against_enc_id " 
							 + " 		else enMst.encounter_id " 
							 + " 	end) as \"openCoversheetWithEncounterId\", " 
							 + " 	enMst.against_enc_id as \"againstEncId\", " 
							 + " 	enMst.encounter_number as \"encounterNumber\", " 
							 + " 	enMst.encounter_date as \"encounterDate\", " 
							 + " 	(case " 
							 + " 		when am.is_paid_appointment = 'Y' then to_char(am.arrival_time, 'YYYY-MM-DD HH24:MI') " 
							 + " 		else to_char(enMst.encounter_date, 'YYYY-MM-DD HH24:MI') " 
							 + " 	end) as \"encounterDateWithTime\", " 
							 + " 	(case " 
							 + " 		when am.is_paid_appointment = 'Y' then to_char(am.arrival_time, 'DD/MM/YYYY HH24:MI:SS') " 
							 + " 		else to_char(enMst.encounter_date, 'DD/MM/YYYY HH24:MI:SS') " 
							 + " 	end) as \"encounterDateWithTimeString\", " 
							 + " 	(case " 
							 + " 		when am.is_paid_appointment = 'Y' then to_char(am.arrival_time, 'HH24:MI:SS') " 
							 + " 		else to_char(enMst.encounter_date, 'HH24:MI:SS') " 
							 + " 	end) as \"encounterTime\", " 
							 + " 	enMst.user_defined_visit_type_id as \"userDefinedVisitTypeId\" , " 
							 + " 	enMst.status as \"status\", " 
							 + " 	enMst.status_id as \"statusId\", " 
							 + " 	enMst.is_called as \"isCalled\", " 
							 + " 	enMst.call_count as \"callCount\", " 
							 + " 	enMst.cabin_id as \"cabinId\", " 
							 + " 	enMst.doctor_desk_arrival as \"doctorDeskArrival\", " 
							 + " 	patient.is_vip as \"isVip\", " 
							 + " 	patient.first_name as \"firstName\", " 
							 + " 	patient.middle_name as \"middleName\", " 
							 + " 	patient.uuid as \"uuid\", " 
							 + " 	patient.last_name as \"lastName\", " 
							 + " 	(case " 
							 + " 		when patient.is_vip = 'Y' then coalesce(patient.alise_name, patient.first_name) " 
							 + " 		else concat(coalesce(patient.first_name) , ' ', coalesce(patient.middle_name) , ' ', coalesce(patient.last_name)) " 
							 + " 	end ) as \"patientName\", " 
							 + " 	to_char(sm.appointment_date, 'dd/MM/yyyy') as \"appointmentDate\", " 
							 + " 	am.appointment_id as \"appointmentId\", " 
							 + " 	sm.slot_id as \"slotId\", " 
							 + " 	concat(to_char(sm.from_time, 'HH24:MI')) as \"fromTimeString\", " 
							 + " 	concat(to_char(sm.to_time, 'HH24:MI')) as \"toTimeString\", " 
							 + " 	am.remark as \"remark\", " 
							 + " 	enMst.doctor_id as \"doctorId\", " 
							 + " 	enMst.payment_entitlement_type_id as \"paymentEntitlementTypeId\", " 
							 + " 	am.appointment_type_id as \"appointmentTypeId\", " 
							 + " 	enMst.encounter_type_id as \"encounterTypeId\", " 
							 + " 	am.appointment_status_id as \"appointmentStatusId\", " 
							 + " 	asm.appointment_status_desc as \"appointmentStatusDesc\", " 
							 + " 	concat('Dr. ', coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name)) as \"doctorName\", " 
							 + " 	enMst.preferred_doc_id as \"preferredDocId\", " 
							 + " 	concat('Dr. ', coalesce(prefDoc.first_name), ' ', coalesce(prefDoc.middle_name), ' ', coalesce(prefDoc.last_name)) as \"preferedDocName\", " 
							 + " 	concat('Dr. ', coalesce(doc.first_name), ' ' , coalesce(doc.last_name)) as \"docNameTemp\", " 
							 + " 	enMst.dept_id as \"specialityId\", " 
							 + " 	speciaityMaster.speciality_name as \"specialityName\", " 
							 + " 	concat(extract(year from age(current_timestamp, patient.dob)) , 'Y ', extract(month from age(current_timestamp, patient.dob)) , 'M ' , extract(day from age(current_timestamp, patient.dob)), 'D ') as \"age\", " 
							 + " 	concat(extract(year from age(current_timestamp, patient.dob))) as \"ageYear\", " 
							 + " 	patient.mobile_number as \"mobileNumber\", " 
							 + " 	patient.email as \"email\", "
		                     + "    patient.blood_group as \"bloodGroup\", "
							 + "    patient.video_call_url as \"videoCallUrl\", " 
							 + " 	doc.email as \"emailDoctor\", " 
							 + " 	patient.patient_id as \"patientId\", " 
							 + " 	patient.uhid_number as \"uhidNumber\", " 
							 + " 	patient.prefix_id as \"prefixId\", " 
							 + " 	prefix.prefix_code as \"prefixCode\", " 
							 + " 	patient.gender_id as \"genderId\", " 
							 + " 	patient.image_path as \"imagePath\", " 
							 + " 	patient.patient_category_id as \"patientCategoryId\", " 
							 + " 	patient.mpshah_patient_id as \"mpshahPatientId\", " 
							 + " 	tom.visit_type_id as \"visitTypeId\", "
							 + " 	visitTypeMaster.visit_type_code as \"visitTypeName\", "
							 + " 	gender.gender_code as \"genderCode\", "
							 + " 	gender.gender_name as \"genderDec\", " 
							 + " 	to_char(patient.dob, 'DD/MM/YYYY') as \"dob\", " 
							 + " 	to_char(patient.dob, 'MM/DD/YYYY') as \"dobForDatePicker\", " 
							 + " 	sm.modality_id as \"modalityId\", " 
							 + " 	modality.modality_desc as \"modalityDesc\", " 
							 + " 	enDet.is_assignment_comp_status as \"isAssignmentCompStatus\", " 
							 + " 	enDet.is_consultation_status as \"isConsultationStatus\", " 
							 + " 	to_char(enDet.consultation_start_time, 'HH24:MI:SS') as \"consultationStartTime\", " 
							 + " 	to_char(enDet.consultation_start_time, 'YYYY-MM-DD HH24:MI') as \"consultationStartTimeWithTime\", " 
							 + " 	to_char(enDet.consultation_stop_time, 'HH24:MI:SS') as \"consultationStopTime\", " 
							 + " 	to_char(enDet.consultation_start_time, 'dd-MM-yyyy HH:MM:SS') as \"consultationStartTimeDate\", " 
							 + " 	to_char(enDet.consultation_stop_time, 'dd-MM-yyyy HH:MM:SS') as \"consultationStopTimeDate\", " 
							/* + " 	refrlTypeMst.referral_type_name as \"referralTypeName\", " 
							 + " 	reflMst.referral_name as \"referralName\", "
							 + "	reflMst.referral_id as \"referralId\", " 
							 + " 	refrDtls.referral_remark as \"referralRemark\", " */
							 + " 	to_char(enDet.assignment_start_time, 'HH:MM:SS') as \"assignmentStartTime\", " 
							 + " 	to_char(enDet.assignment_start_time, 'dd-MM-yyyy HH:MM:SS') as \"assignmentStartTimeDate\", " 
							 + " 	to_char(enDet.assignment_stop_time, 'HH:MM:SS') as \"assignmentStopTime\", " 
							 + " 	to_char(enDet.assignment_stop_time, 'dd-MM-yyyy HH:MM:SS') as \"assignmentStopTimeDate\", " 
							 + " 	enDet.assignment_start_by as \"assignmentStartBy\", " 
							 + " 	enDet.assignment_stop_by as \"assignmentStopBy\", " 
							 + " 	enDet.assignment_start_role_id as \"assignmentStartRoleId\", " 
							 + " 	enDet.assignment_stop_role_id as \"assignmentStopRoleId\", " 
							 + " 	enDet.consultation_start_by as \"consultationStartBy\", " 
							 + " 	enDet.consultation_start_role_id as \"consultationStartRoleId\", " 
							 + " 	enDet.encounter_details_mapper_id as \"encounterDetailsMapperId\", " 
							 + " 	spd.contract_id as \"contractId\", " 
							 + " 	spd.payee_id as \"payeeId\", " 
							 + " 	enMst.doc_queue_number as \"queueNumber\", " 
							 + " 	enMst.unit_token_number as \"tokenNumber\", " 
							 + " 	enDet.is_priority as \"isPriority\", " 
							 + " 	enMst.is_critical as \"isCritical\", " 
							 + " 	doc.allowed_over_booking_slots as \"allowedOverBookingSlots\", " 
							 + " 	enMst.speciality_queue_number as \"specialityQueueNumber\", " 
							 + " 	speciaitymapper.dept_wise_appointment as \"deptWiseAppointment\", " 
							 + " cast(mbm.billing_master_id as integer) as \"billId\", "
							 + " 	enMst.old_doc_id as \"oldDocId\", " 
							 + " 	(case " 
							 + " 		when (enMst.is_critical = 'Y' " 
							 + " 		or enDet.is_priority = 'Y') then 1 " 
							 + " 		when (enMst.is_critical = 'N' " 
							 + " 		or enDet.is_priority is null) then 2 " 
							 + " 	end) as \"isCriticalOrder\", " 
							 + " 	(case " 
							 + " 		when enDet.is_priority = 'Y' then 1 " 
							 + " 		when enDet.is_priority is null then 2 " 
							 + " 	end) as \"isPriorityOrder\", " 
							 + " 	(case " 
							 + " 		when am.is_cyclic_appointment = 'N' then coalesce((select nursing_acceptance from adt.t_admission_details where admission_id =( select admission_id from adt.t_admission where patient_id = enMst.patient_id and status = 'A' order by 1 desc limit 1 ) order by 1 desc limit 1 ), 'N') " 
							 + " 		else 'N' " 
							 + " 	end) as \"nursingAccepatance\", " 
							 + " 	tod.service_rendered as \"serviceRenderId\", " 
							 + " 	tod.order_details_id as \"orderDetailsId\", " 
							 + " 	patient.whatsapp_no as \"whatsAppCallingNo\", " 
							 + " 	patient.whatsapp_calling_code as \"whatsAppCallingCode\", " 
							 + " 	patient.skype_id as \"skypeId\", "
							 + " enMst.latest_call as \"latestCall\" ," 
							 + " 	am.is_teleconsultation_apppointment as \"isTeleconsultationAppointment\", " 
							 + " 	mbm.bill_status_id as \"billStatusId\", "
							 + " mnsm.nursing_station_desc as \"nursingStationDesc\" "
						
							// + " 	temp.otherConsultants as \"otherConsultants\" "
							 + " from " 
							 + " 	opd.t_encounter_master enMst " 
							 //New Added End
							 + " left join adt.m_nursing_station_master mnsm on mnsm.nursing_station_id  = enMst.nursing_station_id " 
							 + " left join opd.t_encounter_details enDet on  " + 
							 "	enDet.encounter_id = enMst.encounter_id  " + 
							 "inner join opd.m_appointment_master am on  " + 
							 "	am.appointment_id = enMst.appointment_id  " + 
							 "inner join t_order_master tom on  " + 
							 "	tom.encounter_id = enMst.encounter_id  " + 
							 " inner join t_order_details tod on  " + 
							 "	tod.order_id = tom.order_id  " +
							 "	and tod.is_consultation_service = 'Y'  " + 
							 "	and tod.status = 'A'  " +
							 "inner join public.m_visit_type_master visitTypeMaster on  " +
							 "	tom.visit_type_id = visitTypeMaster.visit_type_id  " +
							 "inner join billing.t_billing_details bdtls on  " +
							 "	bdtls.order_details_id = tod.order_details_id  " + 
							 "	and bdtls.status = 'A'  " + 
							 "inner join billing.m_billing_master mbm on  " + 
							 "	mbm.billing_master_id = bdtls.billing_master_id  " + 
							 "	and mbm.status = 'A'  " + 
							 "inner join opd.t_patient_sponsor_details spd on  " + 
							 "	spd.encounter_id = enMst.encounter_id  " + 
							 "	and spd.priority_id = 1  " + 
							 "	and spd.status = 'A'  " + 
							 "left join doctor.m_speciality_master speciaityMaster on  " + 
							 "	enMst.dept_id = speciaityMaster.speciality_id  " + 
							 "inner join t_unit_speciality_mapper speciaityMapper on  " + 
							 "	enMst.dept_id = speciaityMapper.speciality_id  " + 
							 "	and speciaityMapper.unit_id = enMst.unit_id  " + 
							 "left join doctor.m_doctor_master doc on  " + 
							 "	enMst.doctor_id = doc.doctor_id  " + 
							 "inner join patient.t_patient_registration patient on  " + 
							 "	enMst.patient_id = patient.patient_id  " + 
							 "inner join public.m_gender_master gender on  " + 
							 "	patient.gender_id = gender.gender_id  " + 
							 "inner join public.m_prefix_master prefix on  " + 
							 "	patient.prefix_id = prefix.prefix_id  " + 
							 "inner join (  " + 
							 "	select  " + 
							 "		patient_id ,  " + 
							 "		to_char(encounter_date, 'dd-MM-yyyy') as ddate,  " + 
							 "		min(encounter_id) FirstEntID  " + 
							 "	from  " + 
							 "		opd.t_encounter_master tem  " + 
							 "	where  " + 
							 "		status_id in (5, 6, 9)  " + 
							 "	group by  " + 
							 "		patient_id ,  " + 
							 "		to_char(encounter_date, 'dd-MM-yyyy') ) FirstEncountfoday on  " + 
							 "	FirstEncountfoday.patient_id = enMst.patient_id  " + 
							 "	and FirstEncountfoday.ddate = to_char(enMst.encounter_date, 'dd-MM-yyyy')  " + 
							 "inner join opd.t_encounter_master Firstentmst on  " + 
							 "	Firstentmst.encounter_id = FirstEncountfoday.FirstEntID	  " + 
							 "left join opd.m_slot_master sm on  " + 
							 "	am.slot_id = sm.slot_id  " + 
						/*	 "left join public.t_referal_details refrDtls on  " + 
							 "	(case  " + 
							 "		when enMst.against_enc_id is not null then refrDtls.encounter_id = enMst.against_enc_id  " + 
							 "		else refrDtls.encounter_id = enMst.encounter_id  " + 
							 "	end)  " + 
							 "	and refrDtls.status = 'A'  " + 
							 "left join adt.m_referral_type_master refrlTypeMst on  " + 
							 "	refrlTypeMst.referral_type_id = refrDtls.referral_type_id  " + 
							 "left join adt.m_referral_master reflMst on  " + 
							 "	reflMst.referral_id = refrDtls.referral_id  " + 
							 "	and reflMst.status = 'A'  " + */
							 "left join doctor.m_doctor_master prefDoc on  " + 
							 "	enMst.preferred_doc_id = prefDoc.doctor_id  " + 
							 "left join public.m_modality_master modality on  " + 
							 "	sm.modality_id = modality.modality_id  " + 
							 "left join opd.m_appointment_status_master asm on  " + 
							 "	asm.appointment_status_id = enMst.status_id  " + 
							 "left join billing.t_credit_note_detail tcnd on   " + 
							 "		tcnd.order_detail_id = tod.order_details_id  " + 
							 "		and tcnd.status = 'A'  " + 
							 "		and tcnd.cancel_or_discount = 1   " + 
							 "left join public.m_clinic_master clinMst on  " + 
							 "	clinMst.clinic_master_id = enMst.clinic_id  " + 
							 "left join opd.t_nursing_station_clinic_mapper nurStatClinMapp on  " + 
							 "	nurStatClinMapp.clinic_id = clinMst.clinic_master_id"
							 + " where " 
							 + " 	 enMst.unit_id =:unitId " 
							 + " 	and (enMst.status_id in (5,6,9) " 
							 + " 	and mbm.bill_status_id in (2,3) " 
							 + " 	and mbm.status = 'A' " 
							 + " 	or (am.is_teleconsultation_apppointment = 1 " 
							 + " 	and mbm.bill_status_id = 3  and mbm.status = 'A'  and enMst.status_id in (5,6,9))) " 
							 + " 	and patient.registration_type_id in (1,3,4,6) " 
							 + " 	and (tod.status = 'A' and tcnd.credit_note_detail_id is null) " 
							 + " 	and am.appointment_type_id in (1,5,6,9) " ),
							
					@NamedNativeQuery(name="GET_SEARCH_ENCOUNTERED_LIST_NURSING_ASS_TOKEN",query=
					" select " 
					 + " 	enMst.encounter_id as \"encounterId\", " 
					 + " 	(case " 
					 + " 		when enMst.encounter_type_id = 6 then enMst.review_against_enc_id " 
					 + " 		when enMst.against_enc_id is not null then enMst.against_enc_id " 
					 + " 		else enMst.encounter_id " 
					 + " 	end) as \"openCoversheetWithEncounterId\", " 
					 + " 	enMst.against_enc_id as \"againstEncId\", " 
					 + " 	enMst.encounter_number as \"encounterNumber\", " 
					 + " 	enMst.encounter_date as \"encounterDate\", " 
					 + " 	(case " 
					 + " 		when am.is_paid_appointment = 'Y' then to_char(am.arrival_time, 'YYYY-MM-DD HH24:MI') " 
					 + " 		else to_char(enMst.encounter_date, 'YYYY-MM-DD HH24:MI') " 
					 + " 	end) as \"encounterDateWithTime\", " 
					 + " 	(case " 
					 + " 		when am.is_paid_appointment = 'Y' then to_char(am.arrival_time, 'DD/MM/YYYY HH24:MI:SS') " 
					 + " 		else to_char(enMst.encounter_date, 'DD/MM/YYYY HH24:MI:SS') " 
					 + " 	end) as \"encounterDateWithTimeString\", " 
					 + " 	(case " 
					 + " 		when am.is_paid_appointment = 'Y' then to_char(am.arrival_time, 'HH24:MI:SS') " 
					 + " 		else to_char(enMst.encounter_date, 'HH24:MI:SS') " 
					 + " 	end) as \"encounterTime\", " 
					 + " 	enMst.user_defined_visit_type_id as \"userDefinedVisitTypeId\" , " 
					 + " 	enMst.status as \"status\", " 
					 + " 	enMst.status_id as \"statusId\", " 
					 + " 	enMst.is_called as \"isCalled\", " 
					 + " 	enMst.call_count as \"callCount\", " 
					 + " 	enMst.cabin_id as \"cabinId\", " 
					 + " 	enMst.doctor_desk_arrival as \"doctorDeskArrival\", " 
					 + " 	patient.is_vip as \"isVip\", " 
					 + " 	patient.first_name as \"firstName\", " 
					 + " 	patient.middle_name as \"middleName\", " 
					 + " 	patient.uuid as \"uuid\", " 
					 + " 	patient.last_name as \"lastName\", " 
					 + " 	(case " 
					 + " 		when patient.is_vip = 'Y' then coalesce(patient.alise_name, patient.first_name) " 
					 + " 		else concat(coalesce(patient.first_name) , ' ', coalesce(patient.middle_name) , ' ', coalesce(patient.last_name)) " 
					 + " 	end ) as \"patientName\", " 
					 + " 	to_char(sm.appointment_date, 'dd/MM/yyyy') as \"appointmentDate\", " 
					 + " 	am.appointment_id as \"appointmentId\", " 
					 + " 	sm.slot_id as \"slotId\", " 
					 + " 	concat(to_char(sm.from_time, 'HH24:MI')) as \"fromTimeString\", " 
					 + " 	concat(to_char(sm.to_time, 'HH24:MI')) as \"toTimeString\", " 
					 + " 	am.remark as \"remark\", " 
					 + " 	enMst.doctor_id as \"doctorId\", " 
					 + " 	enMst.payment_entitlement_type_id as \"paymentEntitlementTypeId\", " 
					 + " 	am.appointment_type_id as \"appointmentTypeId\", " 
					 + " 	enMst.encounter_type_id as \"encounterTypeId\", " 
					 + " 	am.appointment_status_id as \"appointmentStatusId\", " 
					 + " 	asm.appointment_status_desc as \"appointmentStatusDesc\", " 
					 + " 	concat('Dr. ', coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name)) as \"doctorName\", " 
					 + " 	enMst.preferred_doc_id as \"preferredDocId\", " 
					 + " 	concat('Dr. ', coalesce(prefDoc.first_name), ' ', coalesce(prefDoc.middle_name), ' ', coalesce(prefDoc.last_name)) as \"preferedDocName\", " 
					 + " 	concat('Dr. ', coalesce(doc.first_name), ' ' , coalesce(doc.last_name)) as \"docNameTemp\", " 
					 + " 	enMst.dept_id as \"specialityId\", " 
					 + " 	speciaityMaster.speciality_name as \"specialityName\", " 
					 + " 	concat(extract(year from age(current_timestamp, patient.dob)) , 'Y ', extract(month from age(current_timestamp, patient.dob)) , 'M ' , extract(day from age(current_timestamp, patient.dob)), 'D ') as \"age\", " 
					 + " 	concat(extract(year from age(current_timestamp, patient.dob))) as \"ageYear\", " 
					 + " 	patient.mobile_number as \"mobileNumber\", " 
					 + " 	patient.email as \"email\", "
					 + "    patient.video_call_url as \"videoCallUrl\", " 
					 + " 	doc.email as \"emailDoctor\", " 
					 + " 	patient.patient_id as \"patientId\", " 
					 + " 	patient.uhid_number as \"uhidNumber\", " 
					 + " 	patient.prefix_id as \"prefixId\", " 
					 + " 	prefix.prefix_code as \"prefixCode\", " 
					 + " 	patient.gender_id as \"genderId\", " 
					 + " 	patient.image_path as \"imagePath\", " 
					 + " 	patient.patient_category_id as \"patientCategoryId\", " 
					 + " 	patient.mpshah_patient_id as \"mpshahPatientId\", " 
					 + " 	gender.gender_code as \"genderCode\", " 
					 + " 	gender.gender_name as \"genderDec\", " 
					 + " 	to_char(patient.dob, 'DD/MM/YYYY') as \"dob\", " 
					 + " 	to_char(patient.dob, 'MM/DD/YYYY') as \"dobForDatePicker\", " 
					 + " 	sm.modality_id as \"modalityId\", " 
					 + " 	modality.modality_desc as \"modalityDesc\", " 
					 + " 	enDet.is_assignment_comp_status as \"isAssignmentCompStatus\", " 
					 + " 	enDet.is_consultation_status as \"isConsultationStatus\", " 
					 + " 	to_char(enDet.consultation_start_time, 'HH24:MI:SS') as \"consultationStartTime\", " 
					 + " 	to_char(enDet.consultation_start_time, 'YYYY-MM-DD HH24:MI') as \"consultationStartTimeWithTime\", " 
					 + " 	to_char(enDet.consultation_stop_time, 'HH24:MI:SS') as \"consultationStopTime\", " 
					 + " 	to_char(enDet.consultation_start_time, 'dd-MM-yyyy HH:MM:SS') as \"consultationStartTimeDate\", " 
					 + " 	to_char(enDet.consultation_stop_time, 'dd-MM-yyyy HH:MM:SS') as \"consultationStopTimeDate\", " 
					 + " 	to_char(enDet.assignment_start_time, 'HH:MM:SS') as \"assignmentStartTime\", " 
					 + " 	to_char(enDet.assignment_start_time, 'dd-MM-yyyy HH:MM:SS') as \"assignmentStartTimeDate\", " 
					 + " 	to_char(enDet.assignment_stop_time, 'HH:MM:SS') as \"assignmentStopTime\", " 
					 + " 	to_char(enDet.assignment_stop_time, 'dd-MM-yyyy HH:MM:SS') as \"assignmentStopTimeDate\", " 
					 + " 	enDet.assignment_start_by as \"assignmentStartBy\", " 
					 + " 	enDet.assignment_stop_by as \"assignmentStopBy\", " 
					 + " 	enDet.assignment_start_role_id as \"assignmentStartRoleId\", " 
					 + " 	enDet.assignment_stop_role_id as \"assignmentStopRoleId\", " 
					 + " 	enDet.consultation_start_by as \"consultationStartBy\", " 
					 + " 	enDet.consultation_start_role_id as \"consultationStartRoleId\", " 
					 + " 	enDet.encounter_details_mapper_id as \"encounterDetailsMapperId\", " 
					 + " 	spd.contract_id as \"contractId\", " 
					 + " 	spd.payee_id as \"payeeId\", " 
					 + " 	enMst.doc_queue_number as \"queueNumber\", " 
					 + " 	enMst.unit_token_number as \"tokenNumber\", " 
					 + " 	enDet.is_priority as \"isPriority\", " 
					 + " 	enMst.is_critical as \"isCritical\", " 
					 + " 	doc.allowed_over_booking_slots as \"allowedOverBookingSlots\", " 
					 + " 	enMst.speciality_queue_number as \"specialityQueueNumber\", " 
					 + " 	speciaitymapper.dept_wise_appointment as \"deptWiseAppointment\", " 
					 + " cast(mbm.billing_master_id as integer) as \"billId\", "
					 + " 	enMst.old_doc_id as \"oldDocId\", " 
					 + " 	(case " 
					 + " 		when (enMst.is_critical = 'Y' " 
					 + " 		or enDet.is_priority = 'Y') then 1 " 
					 + " 		when (enMst.is_critical = 'N' " 
					 + " 		or enDet.is_priority is null) then 2 " 
					 + " 	end) as \"isCriticalOrder\", " 
					 + " 	(case " 
					 + " 		when enDet.is_priority = 'Y' then 1 " 
					 + " 		when enDet.is_priority is null then 2 " 
					 + " 	end) as \"isPriorityOrder\", " 
					 + " 	(case " 
					 + " 		when am.is_cyclic_appointment = 'N' then coalesce((select nursing_acceptance from adt.t_admission_details where admission_id =( select admission_id from adt.t_admission where patient_id = enMst.patient_id and status = 'A' order by 1 desc limit 1 ) order by 1 desc limit 1 ), 'N') " 
					 + " 		else 'N' " 
					 + " 	end) as \"nursingAccepatance\", " 
					 + " 	tod.service_rendered as \"serviceRenderId\", " 
					 + " 	tod.order_details_id as \"orderDetailsId\", " 
					 + " 	patient.whatsapp_no as \"whatsAppCallingNo\", " 
					 + " 	patient.whatsapp_calling_code as \"whatsAppCallingCode\", " 
					 + " 	patient.skype_id as \"skypeId\", "
					 + " enMst.latest_call as \"latestCall\" ," 
					 + " 	am.is_teleconsultation_apppointment as \"isTeleconsultationAppointment\", " 
					 + " 	mbm.bill_status_id as \"billStatusId\", "
					 + " mnsm.nursing_station_desc as \"nursingStationDesc\" "
					 + " from " 
					 + " 	opd.t_encounter_master enMst " 
					 //New Added End
					 + " left join adt.m_nursing_station_master mnsm on mnsm.nursing_station_id  = enMst.nursing_station_id " 
					 + " left join opd.t_encounter_details enDet on  " + 
					 "	enDet.encounter_id = enMst.encounter_id  " + 
					 "inner join opd.m_appointment_master am on  " + 
					 "	am.appointment_id = enMst.appointment_id  " + 
					 "inner join t_order_master tom on  " + 
					 "	tom.encounter_id = enMst.encounter_id  " + 
					 " inner join t_order_details tod on  " + 
					 "	tod.order_id = tom.order_id  " + 
					 "	and tod.is_consultation_service = 'Y'  " + 
					 "	and tod.status = 'A'  " + 
					 "inner join billing.t_billing_details bdtls on  " + 
					 "	bdtls.order_details_id = tod.order_details_id  " + 
					 "	and bdtls.status = 'A'  " + 
					 "inner join billing.m_billing_master mbm on  " + 
					 "	mbm.billing_master_id = bdtls.billing_master_id  " + 
					 "	and mbm.status = 'A'  " + 
					 "inner join opd.t_patient_sponsor_details spd on  " + 
					 "	spd.encounter_id = enMst.encounter_id  " + 
					 "	and spd.priority_id = 1  " + 
					 "	and spd.status = 'A'  " + 
					 "left join doctor.m_speciality_master speciaityMaster on  " + 
					 "	enMst.dept_id = speciaityMaster.speciality_id  " + 
					 "inner join t_unit_speciality_mapper speciaityMapper on  " + 
					 "	enMst.dept_id = speciaityMapper.speciality_id  " + 
					 "	and speciaityMapper.unit_id = enMst.unit_id  " + 
					 "left join doctor.m_doctor_master doc on  " + 
					 "	enMst.doctor_id = doc.doctor_id  " + 
					 "inner join patient.t_patient_registration patient on  " + 
					 "	enMst.patient_id = patient.patient_id  " + 
					 "inner join public.m_gender_master gender on  " + 
					 "	patient.gender_id = gender.gender_id  " + 
					 "inner join public.m_prefix_master prefix on  " + 
					 "	patient.prefix_id = prefix.prefix_id  " + 
					 "left join opd.m_slot_master sm on  " + 
					 "	am.slot_id = sm.slot_id  " + 
					 "left join doctor.m_doctor_master prefDoc on  " + 
					 "	enMst.preferred_doc_id = prefDoc.doctor_id  " + 
					 "left join public.m_modality_master modality on  " + 
					 "	sm.modality_id = modality.modality_id  " + 
					 "left join opd.m_appointment_status_master asm on  " + 
					 "	asm.appointment_status_id = enMst.status_id  " + 
					 "left join billing.t_credit_note_detail tcnd on   " + 
					 "		tcnd.order_detail_id = tod.order_details_id  " + 
					 "		and tcnd.status = 'A'  " + 
					 "		and tcnd.cancel_or_discount = 1   " + 
					 "left join public.m_clinic_master clinMst on  " + 
					 "	clinMst.clinic_master_id = enMst.clinic_id  " + 
					 "left join opd.t_nursing_station_clinic_mapper nurStatClinMapp on  " + 
					 "	nurStatClinMapp.clinic_id = clinMst.clinic_master_id"
					 + " where " 
					 + " 	enMst.organization_id =:orgId " 
					 + " 	and enMst.unit_id =:unitId " 
					 + " 	and (enMst.status_id in (5,6,9) " 
					 + " 	and mbm.bill_status_id in (2,3) " 
					 + " 	and mbm.status = 'A' " 
					 + " 	or (am.is_teleconsultation_apppointment = 1 " 
					 + " 	and mbm.bill_status_id = 3  and mbm.status = 'A'  and enMst.status_id in (5,6,9))) " 
					 + " 	and patient.registration_type_id in (1,3,4,6) " 
					 + " 	and (tod.status = 'A' and tcnd.credit_note_detail_id is null) " 
					 + " 	and am.appointment_type_id in (1,5,6,9) " ),
					
					@NamedNativeQuery(name="GET_ENCOUNTER_DATA_FOR_EMR",query=
							" select "
							+ " 	enMst.encounter_id as \"encounterId\", "
							+ " 	enMst.encounter_number as \"encounterNumber\", "
							+ " 	enMst.encounter_date as \"encounterDate\", "
							+ " 	enMst.user_defined_visit_type_id as \"userDefinedVisitTypeId\" , "
							+ " 	enMst.status_id as \"statusId\", "
							+ " 	enMst.cabin_id as \"cabinId\", "
							+ " 	clinMst.clinic_description as \"clinicDescription\", "
							+ " 	concat(coalesce(patient.first_name) , ' ', coalesce(patient.middle_name) , ' ', coalesce(patient.last_name)) as \"patientName\", "
							+ " 	to_char(enMst.encounter_date, 'dd/MM/yyyy') as \"appointmentDate\", "
							+ " 	to_char(enMst.encounter_date, 'HH:mm:ss') as \"appointmentTime\", "
							+ " 	am.appointment_id as \"appointmentId\", "
							+ " 	sm.slot_id as \"slotId\", "
							+ " 	concat(to_char(sm.from_time, 'HH24:MI')) as \"fromTimeString\", "
							+ " 	concat(to_char(sm.to_time, 'HH24:MI')) as \"toTimeString\", "
							+ " 	am.remark as \"remark\", "
							+ " 	enMst.doctor_id as \"doctorId\", "
							+ " 	enMst.payment_entitlement_type_id as \"paymentEntitlementTypeId\", "
							+ " 	am.appointment_type_id as \"appointmentTypeId\", "
							+ " 	am.appointment_status_id as \"appointmentStatusId\", "
							+ " 	asm.appointment_status_desc as \"appointmentStatusDesc\", "
							+ " 	concat('Dr. ', coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name)) as \"doctorName\", "
							+ " 	enMst.dept_id as \"specialityId\", "
							+ " 	speciaityMaster.speciality_name as \"specialityName\", "
							+ " 	concat(extract(year from age(current_timestamp, patient.dob)) , ' Y', '/', gender.gender_code ) as \"age\", "
							+ " 	patient.mobile_number as \"mobileNumber\", "
							+ "		gender.gender_code as \"genderCode\", "
							+ " 	patient.email as \"email\", "
							+ " 	patient.patient_id as \"patientId\", "
							+ " 	patient.uhid_number as \"uhidNumber\", "
							+ " 	patient.prefix_id as \"prefixId\", "
							+ " 	prefix.prefix_code as \"prefixCode\", "
							+ " 	patient.gender_id as \"genderId\", "
							+ " 	patient.image_path as \"imagePath\", "
							+ " 	patient.patient_category_id as \"patientCategoryId\", "
							+ " 	to_char(patient.dob, 'DD/MM/YYYY') as \"dob\", "
							+ " 	sm.modality_id as \"modalityId\", "
							+ " 	refrlTypeMst.referral_type_name as \"referralTypeName\", "
							+ " 	reflMst.referral_name as \"referralName\", "
							+ " 	refrDtls.referral_remark as \"referralRemark\", "
							+ " 	adm.admission_id as \"admissionId\", "
							+ " 	enMst.doc_queue_number as \"queueNumber\", "
							+ " 	enMst.unit_token_number as \"tokenNumber\", "
							+ " 	(case "
							+ " 		when enMst.is_critical = 'Y' then 1 "
							+ " 		when enMst.is_critical = 'N' then 2 "
							+ " 	end) as \"isCriticalOrder\", "
							+ " 	(case "
							+ " 		when enDet.is_priority = 'Y' then 1 "
							+ " 		when enDet.is_priority is null then 2 "
							+ " 	end) as \"isPriorityOrder\", "
							+ "(select to_char(encounter_date,'DD-MM-YYYY') from opd.t_encounter_master where patient_id=enMst.patient_id and encounter_date<enMst.encounter_date order by 1 desc limit 1 ) as \"lastVisitDate\" "
							+ " from "
							+ " 	opd.t_encounter_master enMst "
							+ " left join opd.t_encounter_details enDet on "
							+ " 	enDet.encounter_id = enMst.encounter_id "
							+ " left join opd.m_appointment_master am on "
							+ " 	am.appointment_id = enMst.appointment_id "
							+ " left join opd.m_slot_master sm on "
							+ " 	am.slot_id = sm.slot_id "
							+ " left join public.t_referal_details refrDtls on "
							+ " 	refrDtls.encounter_id = enMst.encounter_id "
							+ " left join adt.m_referral_type_master refrlTypeMst on "
							+ " 	refrlTypeMst.referral_type_id = refrDtls.referral_type_id "
							+ " left join adt.m_referral_master reflMst on "
							+ " 	reflMst.referral_id = refrDtls.referral_id "
							+ " left join public.m_clinic_master clinMst on "
							+ " 	clinMst.clinic_master_id = enMst.clinic_id "
							+ " left outer join doctor.m_speciality_master speciaityMaster on "
							+ " 	enMst.dept_id = speciaityMaster.speciality_id "
							+ " left outer join doctor.m_doctor_master doc on "
							+ " 	enMst.doctor_id = doc.doctor_id "
							+ " left join patient.t_patient_registration patient on "
							+ " 	enMst.patient_id = patient.patient_id "
							+ " left join public.m_gender_master gender on "
							+ " 	patient.gender_id = gender.gender_id "
							+ " left join public.m_prefix_master prefix on "
							+ " 	patient.prefix_id = prefix.prefix_id "
							+ " left join opd.m_appointment_status_master asm on "
							+ " 	asm.appointment_status_id = enMst.status_id "
							+ " left outer join adt.t_admission adm on "
							+ " 	adm.admission_id = am.admission_id "
							+ " 	and adm.status = 'A' "
							+ " where "
							+ " 	enMst.organization_id =:orgId "
							+ " 	and enMst.unit_id =:unitId "
							+ "		and enMst.doctor_id=:docId "
							+ " 	and patient.registration_type_id in (1,3,4,6) "
							+ " 	and am.appointment_type_id in (1) "),
					
					
					@NamedNativeQuery(name="GET_SEARCH_ENCOUNTERED_LIST_FOR_CASUALTY",query=""
							+ " SELECT  "
							+ "	enMst.encounter_id as \"encounterId\", enMst.encounter_number as \"encounterNumber\", "
							+ " enMst.encounter_date as \"encounterDate\", "
							+ " enMst.user_defined_visit_type_id as \"userDefinedVisitTypeId\" ,"
							+ " enMst.status as \"status\", "
							+ " enMst.status_id as \"statusId\", "
							+ " enMst.is_called as \"isCalled\", "
							+ " enMst.cabin_id as \"cabinId\", "
							+ " enMst.doctor_desk_arrival as \"doctorDeskArrival\", "
							+ " enMst.call_count as \"callCount\", "
							+ " concat(prefix.prefix_code,' ',coalesce(patient.first_name) ,' ', coalesce(patient.middle_name) ,' ', coalesce(patient.last_name)) as \"patientName\", "
							+ "am.appointment_id as \"appointmentId\", "
							+ "am.remark as \"remark\", "
							+ "enMst.doctor_id as \"doctorId\", "
							+ "enMst.payment_entitlement_type_id as \"paymentEntitlementTypeId\", "
							+ "am.appointment_type_id as \"appointmentTypeId\", "
							+ "am.appointment_status_id as \"appointmentStatusId\", "
							+ "asm.appointment_status_desc as \"appointmentStatusDesc\", "
							+ "concat('Dr. ',coalesce(doc.first_name),' ',coalesce(doc.middle_name),' ',coalesce(doc.last_name)) as \"doctorName\", "
							+ "enMst.dept_id as \"specialityId\", "
							+ "speciaityMaster.speciality_name as \"specialityName\", "
							+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, patient.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, patient.dob)),'D ') as \"age\", "
							+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob))) as \"ageYear\", "
							+ "patient.mobile_number as \"mobileNumber\", "
							+ "patient.email as \"email\", "
							+ "patient.patient_id as \"patientId\", "
							+ "patient.uhid_number as \"uhidNumber\", "
							+ "patient.prefix_id as \"prefixId\", "
							+ "prefix.prefix_code as \"prefixCode\", " 
							+ "patient.gender_id as \"genderId\", "
							+ "patient.image_path as \"imagePath\", "
							+ "patient.patient_category_id as \"patientCategoryId\", "
							+ "gender.gender_code as \"genderCode\", "
							+ "gender.gender_name as \"genderDec\", "
							+ "to_char(patient.dob,'DD/MM/YYYY') as \"dob\", "
							+ " enDet.is_assignment_comp_status as \"isAssignmentCompStatus\","
							+ " enDet.is_consultation_status as \"isConsultationStatus\", "
							+ " to_char(enDet.consultation_start_time,'HH:MM:SS') as \"consultationStartTime\", "
							+ " to_char(enDet.consultation_stop_time,'HH:MM:SS') as \"consultationStopTime\", "
							+ " to_char(enDet.consultation_start_time,'dd-MM-yyyy HH:MM:SS') as \"consultationStartTimeDate\", "
							+ " to_char(enDet.consultation_stop_time,'dd-MM-yyyy HH:MM:SS') as \"consultationStopTimeDate\", "
							+ " to_char(enDet.assignment_start_time,'HH:MM:SS') as \"assignmentStartTime\", "
							+ " to_char(enDet.assignment_start_time,'dd-MM-yyyy HH:MM:SS') as \"assignmentStartTimeDate\", "
							+ " to_char(enDet.assignment_stop_time,'HH:MM:SS') as \"assignmentStopTime\", "
							+ " to_char(enDet.assignment_stop_time,'dd-MM-yyyy HH:MM:SS') as \"assignmentStopTimeDate\", "
							+ " enDet.assignment_start_by as \"assignmentStartBy\", "
							+ " enDet.assignment_stop_by as \"assignmentStopBy\", "
							+ " enDet.assignment_start_role_id as \"assignmentStartRoleId\", "
							+ " enDet.assignment_stop_role_id as \"assignmentStopRoleId\", "
							+ " enDet.consultation_start_by as \"consultationStartBy\", "
							+ " enDet.consultation_start_role_id as \"consultationStartRoleId\", "
							+ " enDet.encounter_details_mapper_id as \"encounterDetailsMapperId\", "
							+ " spd.contract_id as \"contractId\", "
							+ " spd.payee_id as \"payeeId\", "
							+ " enMst.doc_queue_number as \"queueNumber\", "
							+ " enMst.unit_token_number as \"tokenNumber\", "
							+ " enDet.is_priority as \"isPriority\", "
							+ " enMst.is_critical as \"isCritical\", "
							+ " doc.allowed_over_booking_slots as \"allowedOverBookingSlots\", "
							+ " enMst.speciality_queue_number as \"specialityQueueNumber\",  "
							+ "enMst.old_doc_id as \"oldDocId\", "
							+ "(case "
							+ "  when enMst.is_critical = 'Y' then 1 "
							+ "  when enMst.is_critical = 'N' then 2 "
							+ " end) as \"isCriticalOrder\", "
							+ " (case "
							+ "  when enDet.is_priority = 'Y' then 1 "
							+ "  when enDet.is_priority is null then 2 "
							+ " end) as \"isPriorityOrder\""
							+ " FROM opd.t_encounter_master enMst"
							+ " INNER JOIN opd.t_encounter_details enDet on enDet.encounter_id=enMst.encounter_id "
							+ " INNER JOIN opd.m_appointment_master am on am.appointment_id=enMst.appointment_id "
							+ " INNER JOIN opd.t_patient_sponsor_details spd on spd.encounter_id=enMst.encounter_id AND spd.priority_id=1 and spd.status='A' "
							+ " LEFT OUTER JOIN doctor.m_speciality_master speciaityMaster on enMst.dept_id=speciaityMaster.speciality_id "
							+ " LEFT OUTER JOIN doctor.m_doctor_master doc on enMst.doctor_id=doc.doctor_id "
							+ " INNER JOIN patient.t_patient_registration patient on enMst.patient_id=patient.patient_id "
							+ " LEFT JOIN public.m_gender_master gender on patient.gender_id=gender.gender_id "
							+ " LEFT JOIN public.m_prefix_master prefix on patient.prefix_id=prefix.prefix_id "
							+ " LEFT JOIN opd.m_appointment_status_master asm on asm.appointment_status_id=enMst.status_id "
							+ " WHERE enMst.organization_id=:orgId "
							+ " AND enMst.unit_id=:unitId "
							+ " AND patient.registration_type_id IN (1,3,4,6) "
							+ " AND am.appointment_type_id IN (5) "),
							
							@NamedNativeQuery(name="GET_LIST_ENCOUNTER_MASTER",query=""
									+ " SELECT  "
									+ "	enMst.encounter_id as \"encounterId\", enMst.encounter_number as \"encounterNumber\", "
									+ " enMst.encounter_date as \"encounterDate\", "
									+ " enMst.user_defined_visit_type_id as \"userDefinedVisitTypeId\" ,"
									+ " enMst.status as \"status\", "
									+ " enMst.status_id as \"statusId\", "
									+ " enMst.is_called as \"isCalled\", "
									+ " enMst.call_count as \"callCount\", "
									+ " enMst.cabin_id as \"cabinId\", "
									+ " concat(prefix.prefix_code,' ',coalesce(patient.first_name) ,' ', coalesce(patient.middle_name) ,' ', coalesce(patient.last_name)) as \"patientName\", "
									+ "	to_char(sm.appointment_date,'dd/MM/yyyy HH:MM:SS') as \"appointmentDate\", "
									+ "am.appointment_id as \"appointmentId\", "
									+ "sm.slot_id as \"slotId\", "
									+ "concat(to_char(sm.from_time,'HH24:MI')) as \"fromTimeString\", "
									+ "concat(to_char(sm.to_time,'HH24:MI')) as \"toTimeString\", "
									+ "am.remark as \"remark\", "
									+ "enMst.doctor_id as \"doctorId\", "
									+ "enMst.payment_entitlement_type_id as \"paymentEntitlementTypeId\", "
									+ "am.appointment_type_id as \"appointmentTypeId\", "
									+ "am.appointment_status_id as \"appointmentStatusId\", "
									+ "asm.appointment_status_desc as \"appointmentStatusDesc\", "
									+ "concat('Dr. ',coalesce(doc.first_name),' ',coalesce(doc.middle_name),' ',coalesce(doc.last_name)) as \"doctorName\", "
									+ "enMst.dept_id as \"specialityId\", "
									+ "speciaityMaster.speciality_name as \"specialityName\", "
									+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, patient.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, patient.dob)),'D ') as \"age\", "
									+ "patient.mobile_number as \"mobileNumber\", "
									+ "patient.email as \"email\", "
									+ "patient.patient_id as \"patientId\", "
									+ "patient.uhid_number as \"uhidNumber\", "
									+ "patient.prefix_id as \"prefixId\", "
									+ "prefix.prefix_code as \"prefixCode\", " 
									+ "patient.gender_id as \"genderId\", "
									+ "gender.gender_code as \"genderCode\", "
									+ "gender.gender_name as \"genderDec\", "
									+ "patient.patient_category_id as \"patientCategoryId\", "
									+ "to_char(patient.dob,'DD/MM/YYYY') as \"dob\", "
									+ "sm.modality_id as \"modalityId\", "
									+ "modality.modality_desc as \"modalityDesc\", "
									+ " enDet.is_assignment_comp_status as \"isAssignmentCompStatus\","
									+ " enDet.is_consultation_status as \"isConsultationStatus\", "
									+ " to_char(enDet.consultation_start_time,'HH:MM:SS') as \"consultationStartTime\", "
									+ " to_char(enDet.consultation_stop_time,'HH:MM:SS') as \"consultationStopTime\", "
									+ " to_char(enDet.consultation_start_time,'dd-MM-yyyy HH:MM:SS') as \"consultationStartTimeDate\", "
									+ " to_char(enDet.consultation_stop_time,'dd-MM-yyyy HH:MM:SS') as \"consultationStopTimeDate\", "
									+ " refrlTypeMst.referral_type_name as \"referralTypeName\", "
									+ " reflMst.referral_name as \"referralName\", "
									+ " refrDtls.referral_remark as \"referralRemark\", "
									//+ "sm.modality_type_id as \"modalityTypeId\", "
									//+ "modalityType.modality_type_desc as \"modalityTypeDesc\", "
									//+ "modalityType.speciality_id as \"modalitySpecialityId\", "
									+ " adm.admission_id as \"admissionId\", "
									+ " to_char(enDet.assignment_start_time,'HH:MM:SS') as \"assignmentStartTime\", "
									+ " to_char(enDet.assignment_start_time,'dd-MM-yyyy HH:MM:SS') as \"assignmentStartTimeDate\", "
									+ " to_char(enDet.assignment_stop_time,'HH:MM:SS') as \"assignmentStopTime\", "
									+ " to_char(enDet.assignment_stop_time,'dd-MM-yyyy HH:MM:SS') as \"assignmentStopTimeDate\", "
									+ " enDet.assignment_start_by as \"assignmentStartBy\", "
									+ " enDet.assignment_stop_by as \"assignmentStopBy\", "
									+ " enDet.assignment_start_role_id as \"assignmentStartRoleId\", "
									+ " enDet.assignment_stop_role_id as \"assignmentStopRoleId\",  "
									+ " enDet.consultation_start_by as \"consultationStartBy\", "
									+ " enDet.consultation_start_role_id as \"consultationStartRoleId\", "
									+ " spd.contract_id as \"contractId\", "
									+ " enMst.doc_queue_number as \"queueNumber\", "
									+ " enMst.unit_token_number as \"tokenNumber\", "
									+ " enDet.is_priority as \"isPriority\" "
									+ " FROM opd.t_encounter_master enMst "
									+ " LEFT JOIN opd.t_encounter_details enDet on enDet.encounter_id=enMst.encounter_id "
									+ " LEFT JOIN opd.m_appointment_master am on am.appointment_id=enMst.appointment_id "
									+ " LEFT JOIN opd.m_slot_master sm on am.slot_id=sm.slot_id "
									
									+" LEFT JOIN public.t_referal_details refrDtls on refrDtls.encounter_id=enMst.encounter_id "
					  	            +"  LEFT JOIN adt.m_referral_type_master refrlTypeMst on refrlTypeMst.referral_type_id=refrDtls.referral_type_id "
					  	            +"  LEFT JOIN adt.m_referral_master reflMst on reflMst.referral_id=refrDtls.referral_id "
									
									+ " LEFT JOIN public.m_clinic_master clinMst on  clinMst.clinic_master_id=enMst.clinic_id "
									+ " LEFT JOIN opd.t_nursing_station_clinic_mapper nurStatClinMapp on nurStatClinMapp.clinic_id=clinMst.clinic_master_id "
									+ " LEFT OUTER JOIN doctor.m_speciality_master speciaityMaster on sm.speciality_id=speciaityMaster.speciality_id "
									+ " LEFT OUTER JOIN doctor.m_doctor_master doc on enMst.doctor_id=doc.doctor_id "
									//+ "LEFT OUTER JOIN public.m_modality_type_master modalityType on sm.modality_type_id=modalityType.modality_type_id "
									+ " LEFT OUTER JOIN public.m_modality_master modality on sm.modality_id=modality.modality_id "
									+ " LEFT JOIN patient.t_patient_registration patient on enMst.patient_id=patient.patient_id "
									+ " LEFT JOIN public.m_gender_master gender on patient.gender_id=gender.gender_id "
									+ " LEFT JOIN public.m_prefix_master prefix on patient.prefix_id=prefix.prefix_id "
									+ " LEFT JOIN opd.m_appointment_status_master asm on asm.appointment_status_id=enMst.status_id "
									+ " LEFT OUTER JOIN adt.t_admission adm on adm.patient_id = am.patient_id  AND adm.status='A' "
									+ " WHERE enMst.organization_id=:orgId "
									+ " AND enMst.unit_id=:unitId "
									+ " AND patient.registration_type_id IN (1,3,4,6) "
									+ " AND am.appointment_type_id=1"),
							
									
		@NamedNativeQuery(name="GET_ENCOUNTER_BY_ID_FOR_GLOBAL_SCHEDULING_SYNC",
							query="select encounter.doctor_id as \"doctorId\" , encounter.encounter_id as \"appointmentId\" ,"
									+ "encounter.organization_id  as \"hospitalId\" , encounter.unit_id as \"unitId\" ,"
									+ "encounter.patient_id as \"patientId\" , encounter.status_id as \"appointmentStatusId\" ,"
									+ "to_char(encounter.encounter_date,'DD-MM-YYYY') as \"appointmentDate\" ,"
									+ "to_char(slots.from_time , 'HH24:MI') as \"appointmentTime\" , 2 as \"applicationId\" ,"
									+ "encounter.dept_id as \"departmentId\" , speciality.speciality_name as \"departmentName\" ,"
									+ "concat(doctor.first_name,' ',doctor.last_name) as \"doctorName\",to_char(encounter.created_date,'DD-MM-YYYY') as \"createdDate\" ,"
									+ "encounter.created_by as \"createdBy\" "
									+ "from opd.t_encounter_master encounter "
									+ "inner join opd.m_appointment_master appointment "
									+ "on appointment.appointment_id = encounter.appointment_id "
									+ "inner join opd.m_slot_master slots "
									+ "on slots.slot_id = appointment.slot_id "
									+ "inner join doctor.m_speciality_master speciality "
									+ "on speciality.speciality_id = encounter.dept_id "
									+ "inner join doctor.m_doctor_master doctor "
									+ "on doctor.doctor_id = encounter.doctor_id "
									+ "where encounter.encounter_id = :encounterId"),
		

	@NamedNativeQuery(name="GET_PRE_AUTHORIZATION_REQ_LIST_BY_ENCOUNTER_ID_AND_ADMISSION_ID", 
	query="select"
			+ "   adm.admission_id as \"admissionId\","
			+ "   adm.visit_type_id as \"visitTypeId\","
			+ "   visitmst.visit_type_code as \"visitTypeCode\","
			+ "   pem.payment_entitlement_desc as \"paymentEntitlementDesc\","
			+ "   patSp.payment_entitlement_id as \"paymentEntitlementId\","
			+ "   to_char(admdt.doa,'DD/MM/YYYY')as \"visitDate\","
			+ "   adm.admission_number as \"admissionNo\","
			+ "   concat(coalesce(pre.prefix_code,''),' ',"
			+ "     pat.first_name,"
			+ "     ' ',"
			+ "     pat.middle_name,"
			+ "     ' ',"
			+ "     pat.last_name"
			+ "   ) as \"patientName\","
			+ "   pat.patient_id AS \"patientId\","
			+ "   to_char(pat.dob,'DD-MM-YYYY HH:mm:ss') as \"dob\","
			+ "   pat.uhid_number as \"uhidNumber\", "
			+ "   patSp.patient_sponsor_details_id AS \"patientSponsorDetailsId\","	
			+ "   patSp.payee_id AS \"payeeId\","
			+ "   patSp.contract_id as \"contractId\","
			+ "   patSp.insurance_number as \"insuranceNumber\","
			+ "   to_char(patSp.valid_from,'DD/MM/YYYY') as \"validFrom\","
			+ "   to_char(patSp.valid_to,'DD/MM/YYYY')as \"validTo\","
			+ "   comp.company_desc as \"companyDesc\","
			+ "   comp.company_type_id  as \"companyTypeId\","
			+ "   mccm.contract_name as \"contractName\","
			+ "    gender.gender_code as \"genderCode\", "
			+ "    gender.gender_name as \"genderDec\" "
			+ " from"
			+ "   adt.t_admission adm "
			+ "   inner join adt.t_admission_details admdt on admdt.admission_id = adm.admission_id and admdt.status = 'A' "
			+ "   inner join patient.t_patient_registration pat on pat.patient_id = adm.patient_id"
			+ "   left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
			+ "   inner join opd.t_patient_sponsor_details patSp on patSp.admission_id = adm.admission_id "
			+ "  and patSp.payee_id <> (select config.self_payee_id from public.t_unit_application_billing_configuration config where config.unit_id  =adm.unit_id) and patSp.status='A' "
			+ "   left join public.t_company_master comp on comp.company_id = patSp.payee_id"
			+ "   inner join public.m_visit_type_master visitmst on visitmst.visit_type_id = adm.visit_type_id"
			+ "   inner join public.m_payment_entitlement_master pem on pem.payment_entitlement_id = patSp.payment_entitlement_id"
			+ "   left join service.m_company_contract_master mccm on mccm.contract_id = patSp.contract_id "
			+ " left join public.m_gender_master gender on gender.gender_id= pat.gender_id "
			+ "  where pem.payment_entitlement_id=:paymentEntitlementId and visitmst.visit_type_id=:visitTypeId and comp.company_id =:companyId " //and adm.admission_number=:admissionNo
			+ " UNION ALL"
			+ " select"
			+ "   enmst.encounter_id as \"admissionId\","
			+ "   1 as visit_type_id ,"
			+ "   visitmst.visit_type_code as \"visitTypeCode\","
			+ "   pem.payment_entitlement_desc as \"paymentEntitlementDesc\","
			+ "   patSp.payment_entitlement_id as \"paymentEntitlementId\","
			+ "   to_char(enmst.encounter_date,'DD/MM/YYYY') as \"visitDate\","
			+ "   enmst.encounter_number as \"admissionNo\","
			+ "   concat(coalesce(pre.prefix_code,''),' ',"
			+ "     pat.first_name,"
			+ "     ' ',"
			+ "     pat.middle_name,"
			+ "     ' ',"
			+ "     pat.last_name"
			+ "   ) as \"patientName\","
			+ "   pat.patient_id AS \"patientId\","
			+ "   to_char(pat.dob,'DD-MM-YYYY HH:mm:ss') as \"dob\","
			+ "   pat.uhid_number as \"uhidNumber\", "
			+ "   patSp.patient_sponsor_details_id AS \"patientSponsorDetailsId\","	
			+ "   patSp.payee_id AS \"payeeId\","
			+ "   patSp.contract_id as \"contractId\","
			+ "   patSp.insurance_number as \"insuranceNumber\","
			+ "   to_char(patSp.valid_from,'DD/MM/YYYY') as \"validFrom\","
			+ "   to_char(patSp.valid_to,'DD/MM/YYYY')as \"validTo\","
			+ "   comp.company_desc as \"companyDesc\","
			+ "   comp.company_type_id  as \"companyTypeId\","
			+ "   mccm.contract_name as \"contractName\","
			+ "    gender.gender_code as \"genderCode\", "
			+ "    gender.gender_name as \"genderDec\" "
			+ " from"
			+ "   opd.t_encounter_master enmst"
			+ "   inner join patient.t_patient_registration pat on pat.patient_id = enmst.patient_id"
			+ "   left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
			+ "   inner join opd.t_patient_sponsor_details patSp on patSp.encounter_id = enmst.encounter_id and patSp.status='A' "
			+ "   left join public.t_company_master comp on comp.company_id = patSp.payee_id"
			+ "   inner join public.m_visit_type_master visitmst on visitmst.visit_type_id = 1"
			+ "   inner join public.m_payment_entitlement_master pem on pem.payment_entitlement_id = patSp.payment_entitlement_id"
			+ "   left join service.m_company_contract_master mccm on mccm.contract_id = patSp.contract_id "
			+ "   left join public.m_gender_master gender on gender.gender_id= pat.gender_id "
			+ "  where pem.payment_entitlement_id=:paymentEntitlementId and visitmst.visit_type_id=:visitTypeId and comp.company_id =:companyId"//and enmst.encounter_number=:admissionNo
),

		@NamedNativeQuery(name="GET_ENCOUNTER_DOC_DETAILS",
		query="select "
				+ "	doc_type_mst.document_type_name as \"documentName\", "
				+ "	doc_dtls.document_path as \"documentPath\", "
				+ "	doc_dtls.file_name as \"fileName\", "
                + " doc_dtls.remark as \"remark\", "
				+ " doc_dtls.document_type_id as \"documentTypeId\", "
				+ " u_app.bucket_flag as \"bucketFlag\", "
				+ " to_char(doc_dtls.created_date,'DD-MM-YYYY') as \"createdDate\", "
				+ " to_char(doc_dtls.upload_doccument_date,'DD-MM-YYYY') as \"uploadDoccumentDate\" ,"
				+ " doc_dtls.encounter_doc_id as \"encounterDocId\", "
				+ " doc_dtls.document_analyzed_path as \"videolink\", "
				+ " doc_dtls.document_analyzed_date as \"analyzedDocDate\" "
				+ "from "
				+ "	opd.t_encounter_doc_details doc_dtls "
				+ "inner join public.m_document_type_master doc_type_mst on "
				+ "	doc_dtls.document_type_id = doc_type_mst.document_type_id "
				+ "inner join t_unit_application_billing_configuration u_app on "
				+ "	doc_dtls.unit_id = u_app.unit_id "
				+ "where "
				+ "	 doc_dtls.organisation_id = :orgId "
				+ "	and doc_dtls.unit_id = :unitId "
				+ "and doc_dtls.status = 'A' "),
		
		@NamedNativeQuery(name="GET_PREVIOUS_DOC_QUEUE_NUMBER",query="SELECT MAX(doc_queue_number) as \"docQueueNumber\" "
				+ "FROM opd.t_encounter_master "
				+ "WHERE unit_id=:unitId "
				+ "AND doctor_id=:doctorId "
				+ "AND to_char(encounter_date,'YYYY-MM-DD')=:encounterDate "
				+ "AND organization_id=:orgId "),
				//+ "ORDER BY doc_queue_number DESC limit 1"),
		
		@NamedNativeQuery(name="GET_PREVIOUS_UNIT_TOKEN_NUMBER",query="SELECT MAX(unit_token_number) as \"unitTokenNumber\" "
				+ "FROM opd.t_encounter_master "
				+ "WHERE unit_id=:unitId "
				+ "AND to_char(encounter_date,'YYYY-MM-DD')=:encounterDate "
				+ "AND organization_id=:orgId "),
				
		
		@NamedNativeQuery(name="GET_DOCTOR_TOKEN_LIST",
		query="select dm.doctor_id as \"doctorId\", "
				+ " pr.patient_id as \"patientId\", "
				+ " (case when pr.is_vip='Y' then "
				+ "  coalesce(pr.alise_name,pr.first_name) "
				+ "  else "
				+ "  concat(coalesce(pre.prefix_code,''),'   ',coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name)) "
				+ "  end	) as \"patientName\", "
				+ " (case when pr.is_vip='Y' then "
				+ "  coalesce(pr.alise_name,pr.first_name) "
				+ "  else "
				+ "  concat(coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name)) "
				+ "  end	) as \"patientFullName\", "
				+ " concat(dm.first_name,' ',dm.middle_name,' ',dm.last_name) as \"doctorName\", "
				+ " em.unit_token_number as \"unitTokenNumber\", "
				+ " em.doctor_desk_arrival as \"doctorDeskArrival\", "
				+ " pr.uhid_number as \"uhidNumber\", "
				+ " em.doc_queue_number as \"docQueueNumber\", "
				+ " em.speciality_queue_number as \"specialityQueueNumber\", "
				+ " to_char(ed.consultation_start_time,'HH:MM:SS') as \"consultationStartTime\", "
				+ " to_char(ed.consultation_stop_time,'HH:MM:SS') as \"consultationStopTime\", "
				+ " am.appointment_type_id as \"appointmentTypeId\", "
				+ " cabin.cabin_desc as \"cabinDesc\","
				+ "em.doctor_latest_call as \"doctorLatestCall\", "
				+ " spl.speciality_name as \"specialityName\" "
				+ "from opd.t_encounter_master em "
				+ "left join opd.t_encounter_details ed on em.encounter_id=ed.encounter_id "
				+ "inner join doctor.m_doctor_master dm on em.doctor_id=dm.doctor_id "
				+" inner join patient.t_patient_registration pr on em.patient_id=pr.patient_id "
				+ "   left join m_prefix_master pre on pre.prefix_id = pr.prefix_id "
				+ "inner join opd.m_appointment_master am on am.appointment_id=em.appointment_id "
				+ "inner join doctor.m_speciality_master spl on spl.speciality_id=em.dept_id "
				+ "left join public.m_cabin_master cabin on cabin.cabin_id=em.cabin_id "
				+" where em.organization_id=:orgId"
				+ " AND em.unit_id=:unitId"
				+ " AND to_char(encounter_date,'YYYY-MM-DD')=:encounterDate "
				+ " AND ed.encounter_details_mapper_id is not null "
				+ " AND ed.consultation_stop_time is null "),
		
		
		@NamedNativeQuery(name="GET_DOCTOR_TOKEN_LIST_DOCTOR_ID",
        query="select dm.doctor_id as \"doctorId\", "
                + " pr.patient_id as \"patientId\", "
                + " (case when pr.is_vip='Y' then "
                + "  coalesce(pr.alise_name,pr.first_name) "
                + "  else "
                + "  concat(coalesce(pre.prefix_code,''),'   ',coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name)) "
                + "  end    ) as \"patientName\", "
                + " (case when pr.is_vip='Y' then "
                + "  coalesce(pr.alise_name,pr.first_name) "
                + "  else "
                + "  concat(coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name)) "
                + "  end    ) as \"patientFullName\", "
                + " concat(dm.first_name,' ',dm.middle_name,' ',dm.last_name) as \"doctorName\", "
                + " em.unit_token_number as \"unitTokenNumber\", "
                + " em.doctor_desk_arrival as \"doctorDeskArrival\", "
                + " pr.uhid_number as \"uhidNumber\", "
                + " em.doc_queue_number as \"docQueueNumber\", "
                + " em.speciality_queue_number as \"specialityQueueNumber\", "
                + " to_char(ed.consultation_start_time,'HH:MM:SS') as \"consultationStartTime\", "
                + " to_char(ed.consultation_stop_time,'HH:MM:SS') as \"consultationStopTime\", "
                + " am.appointment_type_id as \"appointmentTypeId\", "
                + " cabin.cabin_desc as \"cabinDesc\","
                + "em.doctor_latest_call as \"doctorLatestCall\", "
                + " spl.speciality_name as \"specialityName\" "
                + "from opd.t_encounter_master em "
                + "left join opd.t_encounter_details ed on em.encounter_id=ed.encounter_id "
                + "inner join doctor.m_doctor_master dm on em.doctor_id=dm.doctor_id "
                +" inner join patient.t_patient_registration pr on em.patient_id=pr.patient_id "
                + "   left join m_prefix_master pre on pre.prefix_id = pr.prefix_id "
                + "inner join opd.m_appointment_master am on am.appointment_id=em.appointment_id "
                + "inner join doctor.m_speciality_master spl on spl.speciality_id=em.dept_id "
                + "left join public.m_cabin_master cabin on cabin.cabin_id=em.cabin_id "
                +" where em.organization_id=:orgId"
                + " AND em.unit_id=:unitId"
                + " AND to_char(encounter_date,'YYYY-MM-DD')=:encounterDate "
                + " AND ed.encounter_details_mapper_id is not null "
                + " AND ed.consultation_stop_time is null "
                + "and dm.doctor_id =:doctorId"),

		@NamedNativeQuery(name="GET_TOKEN_LIST_NURSING",
		query="select pr.patient_id as \"patientId\", concat(coalesce(pre.prefix_code,''),' ',pr.first_name,' ',pr.middle_name,' ',pr.last_name) as \"patientName\", "
				+ " em.counter_id as \"counterId\", "
				+ "counter.counter_desc as \"counterDesc\", "
				+ " pr.uhid_number as \"uhidNumber\" "
				+ "from opd.m_appointment_master em "
				+ " left join opd.m_slot_master sm on sm.slot_id=em.slot_id "
				+" left join patient.t_patient_registration pr on em.patient_id=pr.patient_id "
				+ "   left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
				+ "inner join public.m_counter_master counter on counter.counter_id=em.counter_id "
				+" where em.organization_id=:orgId"
				+ " and em.unit_id=:unitId "
				+ " and em.appointment_type_id=1 "
				+ " AND (to_char(sm.appointment_date,'YYYY-MM-DD')=:encounterDate OR to_char(em.appointment_date,'YYYY-MM-DD')=:encounterDate ) "),
		
		
		@NamedNativeQuery(name="GET_TOKEN_LIST",
		query="select pr.patient_id as \"patientId\", concat(coalesce(pre.prefix_code,''),' ',pr.first_name,' ',pr.middle_name,' ',pr.last_name) as \"patientName\", "
				+ " em.counter_id as \"counterId\", "
				+ "counter.counter_desc as \"counterDesc\", "
				+ " pr.uhid_number as \"uhidNumber\", "
				+ "em.front_desk_arrival as \"frontDeskArrival\", "
				+ "em.token_number as \"unitTokenNumber\" "
				+ "from opd.m_appointment_master em  "
				+ "left join opd.m_slot_master sm on sm.slot_id=em.slot_id "
				+ " left join patient.t_patient_registration pr on em.patient_id=pr.patient_id "
				+ "   left join m_prefix_master pre on pre.prefix_id = pr.prefix_id "
				+ "inner join public.m_counter_master counter on counter.counter_id=em.counter_id "
				+ " where em.organization_id=:orgId "
				+ " and em.unit_id=:unitId "
				+ " and em.appointment_type_id=1 "
				+ " and em.counter_id is not null "
				+ " and em.appointment_status_id=1 "
				+ " AND (to_char(sm.appointment_date,'YYYY-MM-DD')=:encounterDate OR to_char(em.appointment_date,'YYYY-MM-DD')=:encounterDate ) "),
				
	@NamedNativeQuery(name="GET_PATIENT_CHECKED_IN_LIST_BYDOCTORID",
	query="select enc.encounter_id as \"encounterId\",pr.patient_id as \"patientId\", concat(coalesce(pre.prefix_code,''),' ',pr.first_name,' ',pr.middle_name,' ',pr.last_name) as \"patientName\", "
			+ " pr.uhid_number as \"mrnNumber\",pr.mobile_number as \"mobileNumber\","
			+ " pd.address as \"addressDesc\",gm.gender_name as \"genderDesc\","
			+ " CAST(enc.appointment_id as varchar ) as \"appointmentId\", "
			+ " pr.image_path as \"patientPhoto\", "
			+ " doctor.doctor_id as  \"doctorId\",enc.organization_id as \"organizationId\",enc.unit_id as \"unitId\", "
			+ " enc.created_by as \"createdBy\","
			+ " concat('Dr. ',coalesce(doctor.first_name,''),' ',coalesce(doctor.middle_name,''),' ',coalesce(doctor.last_name,'')) as \"doctorName\","
			+ " concat(extract(year from age(current_timestamp, pr.dob)) , 'Y ' , extract(month from age(current_timestamp, pr.dob)), 'M ' ,"  
			+"	 	extract(days from age(current_timestamp, pr.dob)), 'D ') as \"age\","
			+ "  to_char(enc.encounter_date, 'dd/mm/yyyy') as \"visitDate\",specMas.speciality_name as \"specialityName\" "
			+ " from doctor.m_doctor_master doctor "
			+ " inner join m_user_master usm on usm.user_id=doctor.user_id "
			+ "inner join opd.t_encounter_master enc on enc.doctor_id=doctor.doctor_id "
			+" inner join patient.t_patient_registration pr on enc.patient_id=pr.patient_id "
			+ "   left join m_prefix_master pre on pre.prefix_id = pr.prefix_id "
			+ "inner join patient.t_patient_details pd on pd.patient_id=pr.patient_id "
			+ "inner join m_gender_master gm on gm.gender_id=pr.gender_id"
			+ " inner join doctor.m_speciality_master specMas on specMas.speciality_id=enc.dept_id "
			+" where enc.organization_id=:orgId"
			+ " and enc.unit_id=:unitId and doctor.user_id=:userId "
			+ " and to_char(enc.encounter_date,'YYYY-MM-DD')=:updateDate and enc.status_id=5 "),
	
	@NamedNativeQuery(name="GET_PATIENT_CHECKED_IN_LIST_OF_NURSE",
	query="select enc.encounter_id as \"encounterId\",pr.patient_id as \"patientId\", concat(coalesce(pre.prefix_code,''),' ',pr.first_name,' ',pr.middle_name,' ',pr.last_name) as \"patientName\", "
			+ " pr.uhid_number as \"mrnNumber\",pr.mobile_number as \"mobileNumber\","
			+ " pd.address as \"addressDesc\",gm.gender_name as \"genderDesc\","
			+ " CAST(enc.appointment_id as varchar ) as \"appointmentId\", "
			+ " pr.image_path as \"patientPhoto\", "
			+ " enc.doctor_id as  \"doctorId\",enc.organization_id as \"organizationId\",enc.unit_id as \"unitId\","
			+ " enc.created_by as \"createdBy\", "
			+ " concat('Dr. ',coalesce(doctor.first_name,''),' ',coalesce(doctor.middle_name,''),' ',coalesce(doctor.last_name,'')) as \"doctorName\","
			+ " concat(extract(year from age(current_timestamp, pr.dob)) , 'Y ' , extract(month from age(current_timestamp, pr.dob)), 'M ' ,"  
			+"	 	extract(days from age(current_timestamp, pr.dob)), 'D ') as \"age\","
			+ "  to_char(enc.encounter_date, 'dd/mm/yyyy') as \"visitDate\",specMas.speciality_name as \"specialityName\" "
			+ " from opd.t_encounter_master enc "
			+" inner join patient.t_patient_registration pr on enc.patient_id=pr.patient_id "
			+ "   left join m_prefix_master pre on pre.prefix_id = pr.prefix_id "
			+ "inner join patient.t_patient_details pd on pd.patient_id=pr.patient_id "
			+ "inner join m_gender_master gm on gm.gender_id=pr.gender_id"
			+ " inner join doctor.m_doctor_master doctor on doctor.doctor_id=enc.doctor_id"
			+ " inner join doctor.m_speciality_master specMas on specMas.speciality_id=enc.dept_id "
			+" where enc.dept_id in ("
			+ "select dnsm.department_id from adt.t_department_nursing_station_mapper dnsm"
			+ " where dnsm.nursing_station_id in ("
			+ "select unsm.nursing_station_id from adt.t_user_nursing_station_mapper unsm where unsm.user_id =:userId ) )"
			+ " and enc.organization_id=:orgId"
			+ " and enc.unit_id=:unitId "
			+ " and to_char(enc.encounter_date,'YYYY-MM-DD')=:updateDate and enc.status_id=5 "),
	
	@NamedNativeQuery(name="GET_TOKEN_MESSAGE_LIST",
	query="select em.id as \"id\", "
			+ " em.message as \"message\" "
			+ "from opd.t_broadcast_msg em  "
			+ " where em.organization_id=:orgId "
			+ " and em.unit_id=:unitId "
			+ " and em.type=:type and em.status='A' "),
	
	@NamedNativeQuery(name="GET_STORE_ID_FOR_NURSING_ENCOUNTER_ID" ,
    query=" SELECT strmppr.store_id as \"storeId\" "
	    		+ " FROM pharmacy.t_dept_store_mapper strmppr "
	    		+ " where strmppr.dept_id=(select enc.dept_id as \"specialityId\" from  opd.t_encounter_master enc " + 
	    		"		where enc.patient_id=:patientId and enc.encounter_id=:encounterId "
	    		+ "and enc.organization_id=:organizationId "
	    		+ "and enc.unit_id=:unitId) "
    		) ,
	
	@NamedNativeQuery(name="GET_PATIENT_SERVICES_FOR_FUTURE_DATE",
	query = " select details.order_details_id as \"orderDetailsId\", "
			+ "orderm.order_id as \"orderId\", "
			+ "service.service_master_id as \"serviceMasterId\", "
			+ "sm.speciality_name as \"specialityName\", "
			+ " service.service_standard_name as \"serviceStandardName\", "
			+ " service.service_standard_code as \"serviceStandardCode\","
			+ " to_char(orderm.order_date,'yyyy-mm-dd') as \"orderDateString\", "
			+ " to_char(details.order_sch_date,'yyyy-mm-dd') as \"orderSchDate\", "
			+ " details.order_date as \"orderDate\", details.package_id as \"packageMasterId\", "
			+ " CAST ('0' AS DOUBLE PRECISION) as \"concession\", "
			+ " CAST ('1' AS DOUBLE PRECISION) as \"quantity\" , "
			+ " serdtl.is_tax_applicable as \"isTaxApplicable\","
			+ " tax.id as \"taxId\", "
			+ " tax.tax_percentage as \"taxPercentage\", "
			+ " tax.name as \"taxName\", "
			+ " details.ord_doc_id as \"doctorId\", "
			+ " details.ord_doc_spl_id as \"specialityId\", "
			+ " details.is_investigation_order as \"isInvestigationOrder\"," 
			+ " (case when details.service_rendering_deptid is not null then "
			+ " details.service_rendering_deptid "
			+ "else "
			+ " service.speciality_id end) as \"serviceRenderingDeptId\" "
			+ " FROM public.t_order_details details "
			+ " INNER JOIN public.t_order_master orderm on details.order_id = orderm.order_id "
			+ " INNER JOIN service.m_service_master service  on service.service_master_id = details.service_id "
			+ " INNER JOIN service.t_unit_service_master_details serdtl on service.service_master_id = serdtl.service_master_id "
			+ " INNER JOIN doctor.m_speciality_master sm on service.speciality_id = sm.speciality_id "
			+ " LEFT JOIN public.m_tax tax on tax.id=serdtl.gst_type_id "
			+ " left join opd.t_cyclic_appointment_mapper cam on cam.order_detail_id=details.order_details_id "
			+ " WHERE orderm.patient_id=:patientId "
			+ " and details.service_is_billed = 0" 
			//+ " and details.is_investigation_order = 'Y' "
			+ "	and orderm.org_unit_id =serdtl.unit_id" 
			+ "	and orderm.org_id =serdtl.organization_id" 
			+ "	and serdtl.organization_id =:organizationId" 
			+ "	and serdtl.unit_id =:unitId" 
			+ "	and orderm.encounter_id is null AND orderm.admission_id is null " 
			+ "	and details.order_sch_date is not null "
			+ " and cam.id is null "
			+ " and orderm.encounter_id is null "
			+ " and orderm.admission_id is null "
			+ " and details.status='A' "  ),
	
	@NamedNativeQuery(name="GET_STORE_ID_FOR_NURSING_ADMISSION_ID" ,
    query=" SELECT strmppr.store_id as \"storeId\" "
	    		+ " FROM pharmacy.t_dept_store_mapper strmppr "
	    		+ " where strmppr.dept_id=(select admm.speciality_id as \"specialityId\"  from adt.t_admission admm  " + 
	    		"		where admm.patient_id=:patientId and admm.admission_id=:admissionId "
	    		+ "and admm.organization_id=:organizationId "
	    		+ "and admm.unit_id=:unitId) "
    		),
	
	
	
	
	@NamedNativeQuery(name="GET_REIVIEW_APPOINTMENT_SERVICES_AGAINST_PATIENT" ,
    query= "with review_service AS (select "
    		+ "	em.encounter_id as \"encounterId\", "
    		+ "	sm.service_master_id as \"serviceId\", "
    		+ "	sm.service_standard_name as \"serviceStandardName\", "
    		+ "	spem.speciality_name as \"specialityName\", "
    		+ "	subspe.sub_speciality_name as \"subSpecialityName\", "
    		+ "	od.order_details_id as \"orderDetailsId\", "
    		+ "	em.encounter_number as \"encounterNumber\" "
    		+ "from "
    		+ "	public.t_order_details od "
    		+ "inner join public.t_order_master om on "
    		+ "	om.order_id = od.order_id "
    		+ "inner join opd.t_encounter_master em on "
    		+ "	om.encounter_id = em.encounter_id "
    		+ "inner join service.t_unit_service_master_details usmd on "
    		+ "	od.service_id = usmd.service_master_id and od.status = 'A' and om.org_unit_id = usmd.unit_id "
    		+ "inner join service.m_service_master sm on "
    		+ "	od.service_id = sm.service_master_id "
    		+ "inner join doctor.m_speciality_master spem on "
    		+ "	sm.speciality_id = spem.speciality_id "
    		+ "inner join doctor.m_sub_speciality_master subspe on "
    		+ "	sm.sub_speciality_id = subspe.sub_speciality_id "
    		+ "inner join lab.t_lab_sample_dtls lab_sample_dtls on "
    		+ "	od.order_details_id = lab_sample_dtls.order_details_id "
    		+ "	and lab_sample_dtls.sample_status_id = 8 "
    		+ "inner join lab.t_lab_test_result rest_res on "
    		+ "	lab_sample_dtls.lab_sample_dtls_id = rest_res.lab_sample_dtls_id "
    		+ "	and rest_res.sample_status_id = 8 "
    		+ "where "
    		+ "	em.patient_id = :patientId "
    		+ "	and usmd.is_review_applicable = 'Y' "
    		//+ " and od.is_investigation_order='Y' "
    		+ "	and od.is_service_reviewed = false "
    		+ "	and od.status = 'A' "
    		+ "	and em.encounter_type_id = 1 "
    		+ "	and cast(EXTRACT(EPOCH FROM now()-rest_res.result_authorised_datetime)/3600 as integer) <usmd.review_time "
    		+  " union "
    		+ "select "
    		+ "	em.encounter_id as \"encounterId\", "
    		+ "	sm.service_master_id as \"serviceId\", "
    		+ "	sm.service_standard_name as \"serviceStandardName\", "
    		+ "	spem.speciality_name as \"specialityName\", "
    		+ "	subspe.sub_speciality_name as \"subSpecialityName\", "
    		+ "	od.order_details_id as \"orderDetailsId\", "
    		+ "	em.encounter_number as \"encounterNumber\" "
    		+ "from "
    		+ "	public.t_order_details od "
    		+ "inner join public.t_order_master om on "
    		+ "	om.order_id = od.order_id "
    		+ "inner join opd.t_encounter_master em on "
    		+ "	om.encounter_id = em.encounter_id "
    		+ "inner join service.t_unit_service_master_details usmd on "
    		+ "	od.service_id = usmd.service_master_id and od.status = 'A' and om.org_unit_id = usmd.unit_id "
    		+ "inner join service.m_service_master sm on "
    		+ "	od.service_id = sm.service_master_id "
    		+ "inner join doctor.m_speciality_master spem on "
    		+ "	sm.speciality_id = spem.speciality_id "
    		+ "inner join doctor.m_sub_speciality_master subspe on "
    		+ "	sm.sub_speciality_id = subspe.sub_speciality_id "
    		+ "inner join public.t_template_transaction tem_tra on "
    		+ "	od.order_details_id = tem_tra.order_details_id "
    		+ "where "
    		+ "	em.patient_id = :patientId "
    		+ "	and usmd.is_review_applicable = 'Y' "
    		//+ " and od.is_investigation_order='Y' "
    		+ "	and od.is_service_reviewed = false "
    		+ "	and od.status = 'A' "
    		+ "	and em.encounter_type_id = 1 "
    		+ "	and tem_tra.is_final_release = 'Y' "
    		+ "	and cast(EXTRACT(EPOCH FROM now()-tem_tra.report_release_datetime)/3600 as integer) <usmd.review_time "
    		+ ") "
    		+ "select * from  review_service rv order by rv.\"encounterId\""
    		) ,
	
	
	@NamedNativeQuery(name="GET_EHC_CONSULTATION_BY_ENCOUNTER_ID" ,
/*    query="select "
    		+ "	psd.service_id as \"serviceId\", "
    		+ "	psd.consultation_speciality_id as \"specialityId\" "
    		+ "from "
    		+ "	service.t_package_services_details psd "
    		+ "inner join service.m_service_master servcie_mst on "
    		+ "	psd.service_id = servcie_mst.service_master_id "
    		+ "	and servcie_mst.status = 'A' "
    		+ "inner join doctor.m_speciality_master spe_mst on "
    		+ "	servcie_mst.speciality_id = spe_mst.speciality_id "
    		+ "inner join public.t_unit_application_billing_configuration config on "
    		+ "	config.unit_id = psd.unit_id "
    		+ "	and spe_mst.speciality_id = config.consultation_speciality_id "
    		+ "where "
    		+ "	psd.package_id =:packageId"*/
	    query="select tod.service_id as \"serviceId\",tod.ord_doc_spl_id as \"specialityId\",tod.order_details_id as \"orderDetailsId\" "
	    		+ " from public.t_order_details as tod "
	    		+ " where package_id=:packageId "
	    		+ " and service_rendering_deptid=(select tuabc.consultation_sub_speciality_id from public.t_unit_application_billing_configuration tuabc where tuabc.unit_id=:unitId and tuabc.organization_id=:orgId) "
	    		+ " and status='A' "
	    		+ " and order_id in (select order_id from public.t_order_master as tom where encounter_id=:encounterId) "
	    		+ " order by tod.service_id desc"
    		
			),
	
	
	@NamedNativeQuery(name="GET_EHC_CONSULTATION_BY_ENCOUNTER_ID_FOR_APPOINTMENT" ,
    query= " select "
    		+ "	mppr.service_id as \"serviceId\", "
    		+ "	mppr.consultation_mppr_id as \"consultationMpprId\", "
//    		+ "	mppr.consultation_speciality_id as \"specialityId\", "
    		+" (case when mppr.consultation_speciality_id is not null then mppr.consultation_speciality_id else od.ord_doc_spl_id end) as \"specialityId\", "
//    		+ "	mppr.doctor_id as \"doctorId\", "
    		+" (case when  mppr.doctor_id is not null then mppr.doctor_id else od.ord_doc_id end) as \"doctorId\","
            + " TO_CHAR(slot_mst.from_time,'HH24:MI:SS') as \"encounterTime\", "
    		+ "	mppr.order_details_id as \"orderDetailsId\", "
    		+ "	mppr.unit_id as \"unitId\", "
    		+ "	service_mst.service_standard_name as \"serviceStandardName\", "
    		+ "	service_mst.service_standard_code as \"serviceStandardCode\", "
    		+ "	spe_mst.speciality_name as \"specialityName\", "
    		+ "	mppr.appointment_status_id as \"appointmentStatusId\",mppr.package_id as \"packageId\" "
    		+ " from "
	        + "	opd.t_encounter_ehc_consultation_master ehcmst inner join opd.t_encounter_master enc_mst on ehcmst.encounter_id = enc_mst.encounter_id "
    		+ " inner join opd.t_encounter_ehc_consultation_mppr mppr "
    		+ " on ehcmst.consultation_master_id  = mppr.consultation_master_id "
    		+ " inner join service.m_service_master service_mst "
    		+ "   on mppr.service_id = service_mst.service_master_id "
    		+ " inner join doctor.m_speciality_master spe_mst on "
    		+ "	service_mst.speciality_id = spe_mst.speciality_id "
    		+ " left join opd.m_slot_master slot_mst on "
    		+ "	mppr.slot_id = slot_mst.slot_id "
    		+ " left join public.t_order_details od on mppr.order_details_id=od.order_details_id "
    		+ " where "
    		+ "	enc_mst.encounter_id = :encounterId and ehcmst.consultation_master_id =:consultationMasterId order by mppr.consultation_mppr_id " ),
	
	@NamedNativeQuery(name="GET_LIST_OF_REDIOLOGY_TESTS",
	query=" select " 
			 + " 	appMstr.admission_id as \"admissionIdFrAppointment\", " 
			 + " 	appMstr.order_details_id as \"orderDetailsIdForAppoitment\", " 
			 + " 	orderDetails.order_details_id as \"orderDetailsId\", " 
			 + " 	patientReg.patient_id as \"patientId\", " 
			 + " 	patientReg.uhid_number as \"uhidNumber\", " 
			 + " 	patientReg.gender_id as \"genderId\", " 
			 + " 	genderMstr.gender_code as \"genderCode\", " 
			 + " 	to_char( patientReg.dob, 'DD-MM-YYYY HH:mm:ss' ) as \"dob\", " 
			 + " 	trim( to_char( extract( year from age( current_timestamp, patientReg.dob )), '999' )) as \"ageString\", " 
			 + " 	admissionMstr.admission_id as \"admissionId\", " 
			 + " 	serviceMastr.service_standard_name as \"serviceName\", " 
			 + " 	serviceMastr.service_standard_code as \"serviceCode\", " 
			 + " 	serviceMastr.speciality_id as \"specialityId\", " 
			 + " 	serviceMastr.sub_speciality_id as \"subSpecialityId\", " 
			 + " 	orderDetails.priority_id as \"priorityId\", " 
			 + " 	orderMstr.order_no as \"orderNo\", " 
			 + " 	to_char( orderMstr.order_date, 'dd/mm/yyyy' ) as \"orderDate\", " 
			 + " 	( prefixMstr.prefix_code || ' ' || coalesce(patientReg.first_name)|| ' ' || coalesce(patientReg.middle_name)|| ' ' || coalesce(patientReg.last_name)) as \"patientName\" " 
			 + " from " 
			 + " 	public.t_order_details orderDetails " 
			 + " inner join public.t_order_master orderMstr on " 
			 + " 	orderDetails.order_id = orderMstr.order_id " 
			 + " inner join patient.t_patient_registration patientReg on " 
			 + " 	orderMstr.patient_id = patientReg.patient_id " 
			 + " inner join public.m_prefix_master prefixMstr on " 
			 + " 	patientReg.prefix_id = prefixMstr.prefix_id " 
			 + " inner join public.m_gender_master genderMstr on " 
			 + " 	patientReg.gender_id = genderMstr.gender_id " 
			 + " left outer join adt.t_admission admissionMstr on " 
			 + " 	orderMstr.admission_id = admissionMstr.admission_id " 
			 + " left outer join doctor.m_doctor_master docMstr on " 
			 + " 	admissionMstr.doctor_id = docMstr.doctor_id " 
			 + " left outer join doctor.m_speciality_master specialityMstr on " 
			 + " 	admissionMstr.speciality_id = specialityMstr.speciality_id " 
			 + " inner join service.m_service_master serviceMastr on " 
			 + " 	orderDetails.service_id = serviceMastr.service_master_id " 
			 + " inner join service.t_unit_service_master_details unitserMstr on " 
			 + " 	serviceMastr.service_master_id = unitserMstr.service_master_id " 
			 + " left join opd.m_appointment_master appMstr on " 
			 + " 	appMstr.order_details_id = orderDetails.order_details_id " 
			 + "   left join billing.t_credit_note_detail tcnd on   " 
			 + "   	tcnd.order_detail_id = orderDetails.order_details_id   " 
			 + "   	and tcnd.status = 'A'   " 
			 + "   	and tcnd.cancel_or_discount = 1	 " 
			 + " where " 
			 + " 	orderMstr.admission_id =:admissionId " 
			 + " 	and orderDetails.service_rendered = 0 " 
			 + " 	and orderMstr.org_id =:orgId " 
			 + " 	and orderMstr.org_unit_id =:orgUnitId " 
			 + " 	and unitserMstr.is_appointable = 'Y' " 
			 + " 	and (orderDetails.status = 'A' and tcnd.credit_note_detail_id is null) " 
			 + " 	and unitserMstr.unit_id =:orgUnitId "),
	
	@NamedNativeQuery(name="GET_OLD_ENCOUNTER_DETAILS",query="select " 
			 + " 	encounterMaster.encounter_id as \"encounterId\", " 
			 + " 	encounterMaster.dept_id as \"deptId\", " 
			 + " 	encounterMaster.user_defined_visit_type_id as \"userDefinedVisitTypeId\", " 
			 + " 	to_char(encounterMaster.encounter_date, 'yyyy-mm-dd') as \"encounterDateString\", " 
			 + " 	to_char(encounterMaster.encounter_date, 'dd/mm/yyyy') as \"encounterDateString2\", " 
			 + " 	encounterMaster.encounter_number as \"encounterNumber\", " 
			 + " 	encounterMaster.doctor_id as \"doctorId\", " 
			 + " 	encounterMaster.patient_id as \"patientId\", " 
			 + " 	encounterMaster.patient_class_id as \"patientClassId\", " 
			 + " 	coalesce(('Dr. ' || coalesce(doctorMaster.first_name)|| ' ' || coalesce(doctorMaster.middle_name)|| ' ' || coalesce(doctorMaster.last_name)), '-') as \"doctorName\", " 
			 + " 	encounterMaster.dept_id as \"specialityId\", " 
			 + " 	coalesce(specialityMaster.speciality_name, '-') as \"specialityName\", " 
			 + " 	billMst.net_amount as \"totalBillAmount\", " 
			 + " 	billMst.outstanding as \"outstanding\", " 
			 + " 	billMst.bill_number as \"billNumber\", " 
			 + " 	encounterMaster.unit_id as \"unitId\", " 
			 + " 	billMst.billing_master_id as \"billingMasterId\", " 
			 + " 	billMst.total_self_payable as \"selfPayable\", " 
			 + " 	billMst.total_credit_payable as \"creditPayable\", " 
			 + " 	encounterMaster.encounter_type_id as \"encounterTypeId\", " 
			 + " 	billPayee.due_amount as \"selfDueAmount\" " 
			 + " from " 
			 + " 	opd.t_encounter_master encounterMaster " 
			 + " left outer join doctor.m_doctor_master doctorMaster on " 
			 + " 	encounterMaster.doctor_id = doctorMaster.doctor_id " 
			 + " left outer join doctor.m_speciality_master specialityMaster on " 
			 + " 	encounterMaster.dept_id = specialityMaster.speciality_id " 
			 + " left outer join billing.m_billing_master billMst on " 
			 + " 	encounterMaster.encounter_id = billMst.encounter_id and billMst.status='A' " 
			 + " left outer join billing.t_bill_payee_details billPayee on " 
			 + " 	billPayee.bill_id = billMst.billing_master_id " 
			 + " 	and billPayee.payee_id = ( " 
			 + " 	select " 
			 + " 		tuabc.self_payee_id " 
			 + " 	from " 
			 + " 		t_unit_application_billing_configuration tuabc " 
			 + " 	where " 
			 + " 		tuabc.unit_id = encounterMaster.unit_id " 
			 + " 		and status = 'A') and billPayee.status='A'" 
			 + " where " 
			 + " 	encounterMaster.organization_id = :orgId " 
			 + " 	and encounterMaster.unit_id = encounterMaster.unit_id " 
			 + " 	and (encounterMaster.status_id in (6 , " 
			 + " 	9, 5)) " 
			 + " 	and encounterMaster.patient_id = :patientId " 
			 + " order by " 
			 + " 	billMst.outstanding desc, " 
			 + " 	encounterMaster.encounter_date desc " ),
	
	@NamedNativeQuery(name="GET_WARD_FLOOR_AND_CONSULTING_DR_DETAILS_BY_LAB_SAMPLE_DETAILS_ID",query=" select distinct " 
			+ " mwm.ward_name as \"wardName\", mfm.floor_name as \"floorName\" , mbm.bed_number as \"bedNumber\", " 
			+ " (case  when orderDoc.doctor_id is not null then concat('Dr. ', orderDoc.first_name, ' ', orderDoc.last_name) " 
			+ " else (case when mdmenco.doctor_id is not null then concat('Dr. ', mdmenco.first_name, ' ', mdmenco.last_name) " 
			+ " when mdmadmi.doctor_id is not null then concat('Dr. ', mdmadmi.first_name, ' ', mdmadmi.last_name)  end) end)as \"doctorName\" " 
			+ " from t_order_details tod " 
			+ " left join public.t_order_master tom on tom.order_id  = tod.order_id " 
			+ " left join adt.t_admission ta on ta.admission_id = tom.admission_id  and ta.status ='A' " 
			+ " left join adt.t_admission_details tad on tad.admission_id = ta.admission_id   and tad.status ='A' " 
			+ " left join adt.m_ward_master mwm on tad.ward_id = mwm.ward_id " 
			+ " left join adt.m_floor_master mfm on mfm.floor_id = tad.floor_id " 
			+ " left join adt.m_bed_master mbm on mbm.bed_id = tad.bed_id " 
			+ " left join opd.t_encounter_master tem on tom.encounter_id = tem.encounter_id " 
			+ " left join doctor.m_doctor_master orderDoc on orderDoc.doctor_id = tod.ord_doc_id  " 
			+ " left join doctor.m_doctor_master mdmenco on mdmenco.doctor_id = tem.doctor_id  "
			+ " left join doctor.m_doctor_master mdmadmi on mdmadmi.doctor_id = ta.doctor_id " 
			+ " where tod.order_details_id  = :orderDetailsId " ),
	
	@NamedNativeQuery(name="GET_PATIENT_CLASS_ID_BY_ENCOUNTER",query="select mpt.patient_type_id as \"patientClassId\"" + 
			" from opd.t_encounter_master tem" + 
			" left join billing.m_patient_type mpt on tem.patient_class_id = mpt.patient_type_id " + 
			" where tem.encounter_id = :encounterId" + 
			" union all" + 
			" select mpt.patient_type_id as \"patientClassId\"" + 
			" from adt.t_admission ta" + 
			" left join billing.m_patient_type mpt on ta.patient_class_id = mpt.patient_type_id" + 
			" where ta.admission_id =:admissionId" ),
	

})
@NamedQueries({
	
	@NamedQuery(name="GET_OLD_ENCOUNTER_DETAILS_FOR_COVERSHEET",query="SELECT encounter.encounterId as encounterId, "
			+ "encounter.deptId as deptId,"
			+ "encounter.userDefinedVisitTypeId as userDefinedVisitTypeId, "
			+ "to_char(encounter.encounterDate,'yyyy-mm-dd') as encounterDateString, "
			+ "to_char(encounter.encounterDate,'dd/mm/yyyy') as encounterDateString2, "
			+ "encounter.encounterNumber as encounterNumber, "
			+ "encounter.doctorId as doctorId, "
			+ "encounter.patientClassId as patientClassId, "
			+ "concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName, "
			+ "encounter.deptId as specialityId, "
			+ "speciality.specialityName as specialityName "
//			+ "bill.netAmount as totalBillAmount, "
//			+ "bill.outstanding as outstanding, "
//			+ "bill.billNumber as billNumber "
			+ "FROM EncounterMaster encounter "
			+ "INNER JOIN encounter.doctorMaster doc "
			//+ "INNER JOIN doc.docotrSpecialityMapperslist docSpec with docSpec.status='A'"
			+ "INNER JOIN encounter.specialityMaster speciality "
//			+ "LEFT JOIN encounter.billingMastersList bill "
			+ "WHERE encounter.organizationId=:orgId "
			//+ "AND encounter.unitId=:unitId "
			//+ "AND docSpec.status='A' "
			+ "AND encounter.patientId=:patientId "
			+ " AND encounter.statusId in (6, 9) "
//			+ "AND to_char(encounter.createdDate,'YYYY-MM-DD') <= :createdDate "
			+ "ORDER BY encounter.encounterDate "),
	
		@NamedQuery(name = "GET_ENCOUNTER_DETAILS_FOR_VISIT_TYPE_SERVICE_BY_DOCTOR_AND_PATIENT_ID", query = ""
			+ "	SELECT "
			+ "		encounter.encounterId as encounterId,"
			+ " 	encounter.userDefinedVisitTypeId as userDefinedVisitTypeId, "
			+ " 	to_char(encounter.encounterDate,'yyyy-mm-dd') as encounterDateString "
			+ "	FROM EncounterMaster encounter " 
			+ " WHERE encounter.organizationId=:orgId "
			+ " 	AND encounter.unitId=:unitId " 
			+ " 	AND encounter.userDefinedVisitTypeId is not null "
			+ " 	AND encounter.patientId=:patientId "
			+ " 	AND encounter.status='A' " 
			//+ "ORDER BY encounter.encounterDate DESC "
			),
				
		@NamedQuery(name = "GET_ENC_VISIT_NOS_BY_PATIENT_ID_FOR_DEPOSIT",
			query= "SELECT		enc.doctorId as doctorId,  enc.encounterId as encounterId, "
					+ "enc.encounterNumber as visitNo "
				 + "FROM		EncounterMaster enc "
				 + "WHERE		enc.patientId =:patientId "
				 + "and			enc.statusId in (1,5,6,8) "
				 + "and			enc.status ='A' "
				 + "and			enc.organizationId =:orgId "
				 + "and 		enc.unitId =:unitId "),
		
		@NamedQuery(name="GET_ENCOUNTER_DETAILS_BY_ENCOUNTER_ID",
		
		query=  "SELECT encounter.encounterId as encounterId, "
				+ "encounter.patientId as patientId, "
				+ "encounter.patientClassId as patientClassId, "
				+ "encounter.modalityId as modalityId, "
				+ "encounter.doctorId as doctorId, "
				+ "to_char(encounter.encounterDate,'yyyy/mm/dd') as encounterDateString, "
				+ "to_char(encounter.encounterDate,'dd/mm/yyyy') as encounterDateString2, "
				+ "encounter.encounterNumber as encounterNumber, "
				+ "speciality.specialityName as specialityName, "
				+ "concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName, "
                + "patSponDtl.paymentEntitlementId as paymentEntitlementTypeId, "
				+ "referalDetsList.referralId as referralId, "
				+ "refMaster.desc as referralDesc, "
				+ "patSponDtl.payeeId as payeeId, "
				+ "patSponDtl.contractId as contractId, "
				+ "company.companyDesc as companyDesc,"
				+ " paymentEntitlementMaster.paymentEntitlementDesc as paymentEntitlementDesc, "
				+ " patientCat.patientCategory as patientCategory, "
				+ " patient.patientCategoryId as patientCategoryId, "
				+ " encounter.subDeptId as subSpecialityId, "
				+ " speciality.specialityName as specialityName, "
				+ " speciality.specialityId as specialityId, "
				+ " subSpeciality.subSpecialityMasterName as subSpecialityName, "
				+ " encounter.modalityId as modalityId, "
				+ " modality.modalityDesc as modalityDesc,  "
				+ " encounter.patientClassId as patientClassId,"
				+ " encounter.advisedById as advisedById,  "
				+ " encounter.advisedByName as advisedByName, "
				+ " coalesce(companyContract.isTariffRateApplicable,'Y') as isTariffRateApplicable,  "
				+ "	(case when (select count(*) from TContractCapDetails tccd where tccd.contractId = patSponDtl.contractId) > 0 then true else false end) as capExist "
				+ " FROM EncounterMaster encounter "
				+ " LEFT JOIN encounter.doctorMaster doc "
				+ " LEFT JOIN encounter.subSpecialityMaster subSpeciality "
				+ " LEFT JOIN encounter.modalityMaster modality "
				+ " LEFT JOIN encounter.specialityMaster speciality "
				+ " LEFT JOIN encounter.referalDetailsList referalDetsList WITH referalDetsList.status = 'A'"
				+ " LEFT JOIN referalDetsList.referralMaster refMaster "
				+ " INNER JOIN encounter.patientRegistration patient "
				+ " INNER JOIN patient.patientCategoryMaster patientCat "
				+ " INNER JOIN encounter.patientSponsorDetailsList patSponDtl "
				+ " INNER JOIN encounter.paymentEntitlementMaster paymentEntitlementMaster "
				+ " INNER JOIN patSponDtl.companyMaster company "
				+ " LEFT JOIN patSponDtl.mCompanyContractMaster companyContract "
				+ " WHERE encounter.encounterId=:encounterId  AND patSponDtl.status  = 'A'"
				+ " AND patSponDtl.priorityId=1 "),
		
		@NamedQuery(name="GET_LIST_OF_ENCOUNTERS_BY_PATIENT_ID", query=" SELECT "
			   + " distinct(encounter.encounterId) as encounterId, "
			   + " encounter.encounterNumber as encounterNumber, "
			   + " encounter.doctorId as doctorId, "
			   + " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName, "
			   + " encounter.deptId as specialityId, "
			   + " encounter.subDeptId as subSpecialityId, "
			   + " speciality.specialityName as specialityName, "
			   + " subSpeciality.subSpecialityMasterName as subSpecialityName, "
			   + " encounter.modalityId as modalityId, "
			   + " modality.modalityDesc as modalityDesc,  "
			   + " appoint.ehcPackageId as ehcPackageId, "
			   + " appoint.appointmentTypeId as appointmentTypeId,"
			   + " pati.email as email "
			   + " FROM EncounterMaster encounter "
			   + " inner join encounter.patientRegistration pati"
			   + " LEFT JOIN encounter.appointmentMaster appoint "
			   + " LEFT JOIN encounter.doctorMaster doc "
			   + " LEFT JOIN encounter.subSpecialityMaster subSpeciality "
			   + " LEFT JOIN encounter.modalityMaster modality "
			   + " LEFT JOIN 	encounter.specialityMaster speciality "
			   //+ " LEFT JOIN 	encounter.orderMastersList ord "
			   //+ " LEFT JOIN 	ord.listOrderDetailsMaster ordls "
			   + " WHERE encounter.organizationId=:orgId "
			   + " AND encounter.unitId=:unitId "
			   + " AND encounter.patientId=:patientId "
			   + " AND encounter.statusId IN (5,8,6)"
			   + " AND encounter.againstEncId IS NULL"
			   + " ORDER BY encounterId desc "
			  // + " AND encounter.encounterId IN "
			 /*  + "(SELECT encounterId FROM EncounterMaster WHERE patientId=:patientId "
			   + "AND encounterId NOT IN (SELECT encounterId FROM BillingMaster WHERE patientId=:patientId)) "*/
			   //+ " AND ordls.serviceIsBilled=0 "
			   //+ " AND ordls.status='A' "
			   ),
		
		@NamedQuery(name="GET_ENC_DTLS_FOR_CANCELLED_BILLS)", query=" SELECT "
				   + " distinct(encounter.encounterId) as encounterId, "
				   + " encounter.encounterNumber as encounterNumber, "
				   + " encounter.doctorId as doctorId, "
				   + " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName, "
				   + " encounter.deptId as specialityId, "
				   + " encounter.subDeptId as subSpecialityId, "
				   + " speciality.specialityName as specialityName, "
				   + " subSpeciality.subSpecialityMasterName as subSpecialityName, "
				   + " encounter.modalityId as modalityId, "
				   + " modality.modalityDesc as modalityDesc,  "
				   + " appoint.ehcPackageId as ehcPackageId, "
				   + " appoint.appointmentTypeId as appointmentTypeId "
				   + " FROM EncounterMaster encounter "
				   + " LEFT JOIN encounter.appointmentMaster appoint "
				   + " LEFT JOIN encounter.doctorMaster doc "
				   + " LEFT JOIN encounter.subSpecialityMaster subSpeciality "
				   + " LEFT JOIN encounter.modalityMaster modality "
				   + " LEFT JOIN 	encounter.specialityMaster speciality "
				   + " WHERE encounter.encounterId=:encounterId "
				   + " AND encounter.againstEncId IS NULL"
				   + " ORDER BY encounterId desc "
				   ),
		
		@NamedQuery(name="GET_REDIOLOGY_LIST", query= "SELECT orderDetails.orderDetailsId as orderDetailsId, "
			 + "patient.patientId as patientId, "
			 + "patient.uhidNumber as uhidNumber, "
			 + "patient.genderId as genderId, "
			 + "gender.code as genderCode, "
			 + "to_char(patient.dob,'DD-MM-YYYY HH:mm:ss') as dob, "
			 + "trim(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)),'999')) as ageString, "
			 + "encounter.encounterId as encounterId, "
			 + "encounter.modalityId as modalityId, "
			 + "modality.modalityDesc as modalityDesc, "
			 + "encounter.doctorId as doctorId, "
			 + "concat('Dr. ',coalesce(doctorM.firstName),' ',coalesce(doctorM.middleName),' ',coalesce(doctorM.lastName)) as doctorName, "
			 + "encounter.deptId as specialityId, "
			 + "speciality.specialityName as specialityName, "
			 + "service.serviceStandardName as serviceName, "
			 + "service.serviceMasterId as serviceId, "
			 + "service.serviceStandardCode as serviceCode, "
			 + "orderDetails.priorityId as priorityId, "
			 + "orderM.orderNo as orderNo, "
			 + "to_char(orderM.orderDate,'dd/mm/yyyy')as orderDate, "
			 + "concat(prefix.prefixCode,' ',coalesce(patient.firstName) ,' ', coalesce(patient.middleName) ,' ', coalesce(patient.lastName)) as patientName, "
			 +" list.templateTransactionId as templateTransactionId,"
			 +" list.templateObject as templateObject ,"
			 +" templateMst.templateFormatId as templateFormatId "
			 + "FROM OrderDetailsMaster orderDetails "
			 + "INNER JOIN orderDetails.orderMaster orderM "
			 + "INNER JOIN orderM.patientRegistration patient "
			 + "INNER JOIN patient.prefixMaster prefix "
			 + "INNER JOIN patient.genderMaster gender "
			 + "LEFT JOIN orderM.encounterMaster encounter "
			 + "LEFT JOIN orderM.admission admission "
			 + "LEFT JOIN orderDetails.billingDetailsList billDtls "
			 + "LEFT JOIN billDtls.billingMaster billingM "
			 + "LEFT JOIN orderDetails.doctorMaster doctorM "
			 + "LEFT JOIN orderDetails.specialityMaster speciality "
			 + "LEFT JOIN encounter.modalityMaster modality "
			 + "LEFT JOIN orderDetails.templateTransactionList list WITH list.status = 'A' "
			 + "LEFT JOIN list.templateMaster templateMst WITH templateMst.templateTypeId = 13 "
			 + "INNER JOIN orderDetails.serviceMaster service "
			 + "WHERE service.specialityId=:radiologySpecialityId "
			 + "AND orderDetails.serviceRendered=0 "
			 + "AND orderM.orgId=:orgId "
			 + "AND orderM.orgUnitId=:unitId "
			 + "AND ((orderM.visitTypeId=1 AND orderDetails.isAppointableRadiology='N') OR (orderM.visitTypeId=2 AND orderDetails.isAppointableRadiology='Y'))"),
		
		@NamedQuery(name="GET_REDIOLOGY_LIST_FOR_SAMPLE_COLLECTION",query="SELECT orderDetails.orderDetailsId as orderDetailsId, "
			 + "patient.patientId as patientId, "
			 + "patient.uhidNumber as uhidNumber, "
			 + "patient.genderId as genderId, "
			 + "gender.code as genderCode, "
			 + "to_char(patient.dob,'DD-MM-YYYY HH:mm:ss') as dob, "
			 + "trim(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)),'999')) as ageString, "
			 + "admission.admissionId as admissionId, "
			// + "admission.doctorId as doctorId, "
			// + "concat('Dr. ',coalesce(doctorM.firstName),' ',coalesce(doctorM.middleName),' ',coalesce(doctorM.lastName)) as doctorName, "
			// + "admission.specialityId as specialityId, "
			// + "speciality.specialityName as specialityName, "
			 + "service.serviceStandardName as serviceName, "
			 + "service.serviceStandardCode as serviceCode, "
			 + "service.specialityId as specialityId, "
			 + "service.subSpecialityId as subSpecialityId, "
			 + "orderDetails.priorityId as priorityId, "
			 + "orderM.orderNo as orderNo, "
			 + "to_char(orderM.orderDate,'dd/mm/yyyy')as orderDate, "
			 + "concat(prefix.prefixCode,' ',coalesce(patient.firstName) ,' ', coalesce(patient.middleName) ,' ', coalesce(patient.lastName)) as patientName "
			 + "FROM OrderDetailsMaster orderDetails "
			 + "INNER JOIN orderDetails.orderMaster orderM "
			 + "INNER JOIN orderM.patientRegistration patient "
			 + "INNER JOIN patient.prefixMaster prefix "
			 + "INNER JOIN patient.genderMaster gender "
			 + "LEFT JOIN orderM.admission admission "
			 + "LEFT JOIN admission.doctorMaster doctorM "
			 + "LEFT JOIN admission.specialityMaster speciality "
			 + "INNER JOIN orderDetails.serviceMaster service "
			 + "INNER JOIN service.unitServiceMasterDetailsList unitSerMst "
			 + "WHERE service.specialityId=:radiologySpecialityId "
			 + "AND orderDetails.serviceRendered=0 "
			 + "AND orderM.orgId=:orgId "
			 + "AND orderM.orgUnitId=:unitId "
			 + "AND unitSerMst.isAppointable='Y' "),
		
		/*@NamedQuery(name="GET_LIST_OF_REDIOLOGY_TESTS",
		query="SELECT orderDetails.orderDetailsId as orderDetailsId,appMstr.admissionId as admissionId, "
				 + "patient.patientId as patientId, "
				 + "patient.uhidNumber as uhidNumber, "
				 + "patient.genderId as genderId, "
				 + "gender.code as genderCode, "
				 + "to_char(patient.dob,'DD-MM-YYYY HH:mm:ss') as dob, "
				 + "trim(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)),'999')) as ageString, "
				 + "admission.admissionId as admissionId, "
				 + "service.serviceStandardName as serviceName, "
				 + "service.serviceStandardCode as serviceCode, "
				 + "service.specialityId as specialityId, "
				 + "service.subSpecialityId as subSpecialityId, "
				 + "orderDetails.priorityId as priorityId, "
				 + "orderM.orderNo as orderNo, "
				 + "to_char(orderM.orderDate,'dd/mm/yyyy')as orderDate, "
				 + "concat(prefix.prefixCode,' ',coalesce(patient.firstName) ,' ', coalesce(patient.middleName) ,' ', coalesce(patient.lastName)) as patientName "
				 + "FROM OrderDetailsMaster orderDetails "
				 + "INNER JOIN orderDetails.orderMaster orderM "
				 + "INNER JOIN orderM.patientRegistration patient "
				 + "INNER JOIN patient.prefixMaster prefix "
				 + "INNER JOIN patient.genderMaster gender "
				 + "LEFT JOIN orderM.admission admission "
				 + "LEFT JOIN admission.doctorMaster doctorM "
				 + "LEFT JOIN admission.specialityMaster speciality "
				 + "INNER JOIN orderDetails.serviceMaster service "
				 + "INNER JOIN service.unitServiceMasterDetailsList unitSerMst "
				 + " LEFT JOIN orderM.appointmentMaster appMstr "
				 + "WHERE orderM.patientId=:patientId "
				 + "AND orderDetails.serviceRendered=0 "
				 + "AND orderM.orgId=:orgId "
				 + "AND orderM.orgUnitId=:orgUnitId "
				 + "AND unitSerMst.isAppointable='Y' "),*/
		
		@NamedQuery(name="GET_EHC_PATIENTS_LIST",query="SELECT "
			 + "patient.patientId as patientId, "
			 + "concat(prefix.prefixCode,' ',coalesce(patient.firstName) ,' ', coalesce(patient.middleName) ,' ', coalesce(patient.lastName)) as patientName, "
			 + "patient.uhidNumber as uhidNumber, "
			 + "patient.genderId as genderId, "
			 + "gender.code as genderCode, "
			 + "to_char(patient.dob,'DD-MM-YYYY HH:mm:ss') as dob, "
			 + "trim(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)),'999')) as ageString, "
			 + "encounter.encounterId as encounterId, "
			 + "encounter.encounterNumber as encounterNumber, "
			 + "to_char(encounter.encounterDate,'dd/mm/yyyy') as encounterDateString, "
			 + "encounter.statusId as statusId "
			 + "FROM EncounterMaster encounter "
			 + "INNER JOIN encounter.patientRegistration patient "
			 + "INNER JOIN patient.genderMaster gender "
			 + "INNER JOIN patient.prefixMaster prefix "
			 + "INNER JOIN encounter.appointmentMaster appointment "
			 + "WHERE appointment.appointmentTypeId=3 "
			 + "AND encounter.organizationId=:orgId "
			 + "AND encounter.unitId=:unitId "
			 +" AND encounter.statusId IN (5,8,6)"
			 + "AND encounter.status='A' "),
		
		@NamedQuery(name = "GET_PENDING_SERVICES_BY_ENCOUNTER_ID", 
		query = " SELECT orderDetails.orderDetailsId as orderDetailsId, "
			+ "orderDetails.orgId as orgId, "
			+ "ord.orderNo as orderNo, "
			+ "orderDetails.orgUnitId as orgUnitId, "
			+ "orderDetails.orderId as orderId, "
			+ "service.serviceStandardName as serviceStandardName, "
			+ "orderDetails.priorityId as priorityId, "
			+ "to_char(orderDetails.orderDate,'yyyy/mm/dd') as orderDateString, "
			+ "orderDetails.ordDocSplId as specialityId, "
			+ "service.specialityId as serviceSpecialityId, "
			+ "service.subSpecialityId as serviceSubSpecialityId, "
			+ "orderDetails.oldOrdDtlId as oldOrdDtlId, "
			+ "orderDetails.serviceRendered as serviceRendered,"
			+ "orderDetails.serviceIsBilled as serviceIsBilled, "
			+ "orderDetails.status as status, "
			+ "orderDetails.contractId as contractId, "
			+ "speciality.specialityName as specialityName, "
			+ "subSpec.subSpecialityMasterName as subSpecialityName, "
			+ "concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName,orderDetails.isPackage as isPackage "
			+ "FROM OrderDetailsMaster orderDetails "
			+ "INNER JOIN orderDetails.orderMaster ord "
			+ "LEFT JOIN orderDetails.serviceMaster service "
			+ "LEFT JOIN service.specialityMaster speciality "
			+ "LEFT JOIN service.subSpecialityMaster subSpec "
			+ "LEFT JOIN orderDetails.doctorMaster doc "
			+ "WHERE orderDetails.serviceIsBilled=0 "
			+ "AND  orderDetails.serviceRendered=0 "
			+ "AND  orderDetails.status='A' "
			+ "AND ord.encounterId=:encounterId "),
		
		@NamedQuery(name="GET_IS_CRITICAL_BY_PATIENT_ID",query="SELECT "
				 + "encounter.encounterId as encounterId, "
				 + "encounter.isCritical as isCritical "
				 + "FROM EncounterMaster encounter "
				 + "WHERE encounter.organizationId=:orgId "
				 //+ "AND encounter.unitId=:unitId "
				 + "AND encounter.encounterId=:encounterId"),
		
		
})






@Entity
@Table(name="t_encounter_master", schema="opd")
@SequenceGenerator(name = "encounter_master_seq", sequenceName = "opd.encounter_master_seq", allocationSize = 1)
public class EncounterMaster {
	@Id
	@Column(name="encounter_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "encounter_master_seq")
	private int encounterId;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="modality_id")
	private Integer modalityId;
	
	@Column(name="system_generated_visit_type_id")
	private Integer systemGeneratedVisitTypeId;
	
	@Column(name="user_defined_visit_type_id")
	private Integer userDefinedVisitTypeId;
	
	@Column(name="reason_id_for_changing_visit_type")
	private Integer reasonIdForChangingVisitType;

	@Column(name="remark_for_changing_visit_type")
	private String remarkForChangingVisitType;
	
	@Column(name="doctor_id")
	private Integer doctorId;
	
	@Column(name="source_id")
	private Integer sourceId;
	
	@Column(name="encounter_date")
	@Convert(converter = DateConverter.class)
	private Long encounterDate;
	
	@Column(name="encounter_time")
	@Convert(converter = InstantConverter.class)
	private Instant encounterTime;
	
	@Column(name="is_referal")
	private Character isReferal;
	
	@Column(name="dept_id")
	private Integer deptId;
	
	@Column(name="sub_dept_id")
	private Integer subDeptId;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="kin_details_id")
	private Integer kinDetailsId;
	
	@Column(name="payment_entitlement_type_id")
	private Integer paymentEntitlementTypeId;
	
	@Column(name="encounter_number")
	private String encounterNumber;
	
	@Column(name="created_date")
	@Convert(converter = DateConverter.class)
	private Long createdDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_date")
	@Convert(converter = DateConverter.class)
	private Long updatedDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="appointment_id")
	private Integer appointmentId;
	
	@Column(name="clinic_id")
	private Integer clinicId;
	
	@Column(name="status_id")
	private Integer statusId;
	
	@Column(name="counter_id")
	private Integer counterId;
	
	@Column(name="doc_queue_number")
	private Integer docQueueNumber;
	
	@Column(name="unit_token_number")
	private Integer unitTokenNumber;
	
	@Column(name="speciality_queue_number")
	private Integer specialityQueueNumber ;
	
	@Column(name="is_critical ")
	private Character isCritical ;
	
	@Column(name="is_called")
	private Character isCalled;
	
	@Column(name="cabin_id")
	private Integer cabinId;
	
	@Column(name="dummy_token_number")
	private Integer dummyTokenNumber;
	
	@Column(name="call_count")
	private Integer callCount;
	
	@Column(name="old_doc_id")
	private Integer oldDocId;
	
	@Column(name="reason_id_for_doc_change")
	private Integer reasonIdForDocChange;
	
	@Column(name="patient_class_id")
	private Integer patientClassId;
	
	@Column(name="doctor_desk_arrival")
	private Character doctorDeskArrival;
	
	@Column(name="encounter_type_id")
	private Integer encounterTypeId;
	
	@Column(name="review_against_enc_id")
	private Integer reviewAgainstAncId;
	
	@Column(name="against_enc_id")
	private Integer againstEncId;
	
	@Column(name="preferred_doc_id")
	private Integer preferredDocId;
	
	
	@Column(name="pat_history")
	private String patHistory;
	
	@Column(name="feedback_form_submitted")
	private boolean feedbackFormSubmitted;
	
	@Column(name = "follow_up_date")
	private Date followUpDate;
	
	@Column(name="checkin_from_doc_app")
	private Character checkInFromDoctorApp;
	
	@Column(name="is_financial_clearance")
	private Character isFinancialClearance;
	
	@Column(name="advised_by_id")
	private Integer advisedById;
	
	@Column(name="advised_by_name")
	private String advisedByName;
	
    @Column(name="nursing_station_id")
    private Integer nursingStationId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id", insertable = false, nullable = false, updatable = false)
	private DoctorMaster doctorMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modality_id", insertable = false, nullable = false, updatable = false)
	private ModalityMaster modalityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dept_id", insertable = false, nullable = false, updatable = false)
	private SpecialityMaster specialityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sub_dept_id", insertable = false, nullable = false, updatable = false)
	private SubSpecialityMaster subSpecialityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, nullable = false, updatable = false)
	private PatientRegistration patientRegistration;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kin_details_id", insertable = false, nullable = false, updatable = false)
	private KinDetails kinDetails;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clinic_id", insertable = false, nullable = false, updatable = false)
	private ClinicMaster clinicMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "appointment_id", insertable = false, nullable = false, updatable = false)
	private AppointmentMaster appointmentMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_entitlement_type_id", insertable = false, nullable = false, updatable = false)
	private PaymentEntitlementMaster paymentEntitlementMaster;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "encounterMaster", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<ComplaintAppointmentMapper> ComplaintAppointmentMapperList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "encounterMaster", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<ReferalDetails> referalDetailsList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "encounterMaster", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<PatientSponsorDetails> patientSponsorDetailsList;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "encounterMaster")
	private List<OrderMaster> orderMastersList;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "encounter")
	private List<BillingMaster> billingMastersList;
	
	
	public Character getIsFinancialClearance() {
		return isFinancialClearance;
	}

	public void setIsFinancialClearance(Character isFinancialClearance) {
		this.isFinancialClearance = isFinancialClearance;
	}

	public String getPatHistory() {
		return patHistory;
	}

	public void setPatHistory(String patHistory) {
		this.patHistory = patHistory;
	}

	public Integer getPreferredDocId() {
		return preferredDocId;
	}

	public void setPreferredDocId(Integer preferredDocId) {
		this.preferredDocId = preferredDocId;
	}

	public Integer getEncounterTypeId() {
		return encounterTypeId;
	}

	public void setEncounterTypeId(Integer encounterTypeId) {
		this.encounterTypeId = encounterTypeId;
	}

	public Character getDoctorDeskArrival() {
		return doctorDeskArrival;
	}

	public void setDoctorDeskArrival(Character doctorDeskArrival) {
		this.doctorDeskArrival = doctorDeskArrival;
	}

	public Integer getPatientClassId() {
		return patientClassId;
	}

	public void setPatientClassId(Integer patientClassId) {
		this.patientClassId = patientClassId;
	}

	public Integer getOldDocId() {
		return oldDocId;
	}

	public void setOldDocId(Integer oldDocId) {
		this.oldDocId = oldDocId;
	}

	public Integer getReasonIdForDocChange() {
		return reasonIdForDocChange;
	}

	public void setReasonIdForDocChange(Integer reasonIdForDocChange) {
		this.reasonIdForDocChange = reasonIdForDocChange;
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

	public Integer getCabinId() {
		return cabinId;
	}

	public void setCabinId(Integer cabinId) {
		this.cabinId = cabinId;
	}

	public Character getIsCalled() {
		return isCalled;
	}

	public void setIsCalled(Character isCalled) {
		this.isCalled = isCalled;
	}

	public Character getIsCritical() {
		return isCritical;
	}

	public void setIsCritical(Character isCritical) {
		this.isCritical = isCritical;
	}

	public Integer getSpecialityQueueNumber() {
		return specialityQueueNumber;
	}

	public void setSpecialityQueueNumber(Integer specialityQueueNumber) {
		this.specialityQueueNumber = specialityQueueNumber;
	}

	public Integer getDocQueueNumber() {
		return docQueueNumber;
	}

	public void setDocQueueNumber(Integer docQueueNumber) {
		this.docQueueNumber = docQueueNumber;
	}

	public Integer getUnitTokenNumber() {
		return unitTokenNumber;
	}

	public void setUnitTokenNumber(Integer unitTokenNumber) {
		this.unitTokenNumber = unitTokenNumber;
	}

	public Integer getSubDeptId() {
		return subDeptId;
	}

	public void setSubDeptId(Integer subDeptId) {
		this.subDeptId = subDeptId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public void setClinicId(Integer clinicId) {
		this.clinicId = clinicId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public int getClinicId() {
		return clinicId;
	}

	public void setClinicId(int clinicId) {
		this.clinicId = clinicId;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public int getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Integer getUserDefinedVisitTypeId() {
		return userDefinedVisitTypeId;
	}

	public void setUserDefinedVisitTypeId(Integer userDefinedVisitTypeId) {
		this.userDefinedVisitTypeId = userDefinedVisitTypeId;
	}

	public Integer getReasonIdForChangingVisitType() {
		return reasonIdForChangingVisitType;
	}

	public void setReasonIdForChangingVisitType(Integer reasonIdForChangingVisitType) {
		this.reasonIdForChangingVisitType = reasonIdForChangingVisitType;
	}
	
	public Integer getSystemGeneratedVisitTypeId() {
		return systemGeneratedVisitTypeId;
	}

	public void setSystemGeneratedVisitTypeId(Integer systemGeneratedVisitTypeId) {
		this.systemGeneratedVisitTypeId = systemGeneratedVisitTypeId;
	}

	public String getRemarkForChangingVisitType() {
		return remarkForChangingVisitType;
	}

	public void setRemarkForChangingVisitType(String remarkForChangingVisitType) {
		this.remarkForChangingVisitType = remarkForChangingVisitType;
	}

	public Integer getModalityId() {
		return modalityId;
	}

	public void setModalityId(Integer modalityId) {
		this.modalityId = modalityId;
	}

	public int getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(int encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getSourceId() {
		return sourceId;
	}

	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

	public Long getEncounterDate() {
		return encounterDate;
	}

	public void setEncounterDate(Long encounterDate) {
		this.encounterDate = encounterDate;
	}

	public Instant getEncounterTime() {
		return encounterTime;
	}

	public void setEncounterTime(Instant encounterTime) {
		this.encounterTime = encounterTime;
	}

	public Character getIsReferal() {
		return isReferal;
	}

	public void setIsReferal(Character isReferal) {
		this.isReferal = isReferal;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getKinDetailsId() {
		return kinDetailsId;
	}

	public void setKinDetailsId(Integer kinDetailsId) {
		this.kinDetailsId = kinDetailsId;
	}

	public Integer getPaymentEntitlementTypeId() {
		return paymentEntitlementTypeId;
	}

	public void setPaymentEntitlementTypeId(Integer paymentEntitlementTypeId) {
		this.paymentEntitlementTypeId = paymentEntitlementTypeId;
	}

	public String getEncounterNumber() {
		return encounterNumber;
	}

	public void setEncounterNumber(String encounterNumber) {
		this.encounterNumber = encounterNumber;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public Integer getReviewAgainstAncId() {
		return reviewAgainstAncId;
	}

	public void setReviewAgainstAncId(Integer reviewAgainstAncId) {
		this.reviewAgainstAncId = reviewAgainstAncId;
	}

	public Integer getAgainstEncId() {
		return againstEncId;
	}

	public void setAgainstEncId(Integer againstEncId) {
		this.againstEncId = againstEncId;
	}

	public boolean isFeedbackFormSubmitted() {
		return feedbackFormSubmitted;
	}

	public void setFeedbackFormSubmitted(boolean feedbackFormSubmitted) {
		this.feedbackFormSubmitted = feedbackFormSubmitted;
	}

	public Date getFollowUpDate() {
		return followUpDate;
	}

	public void setFollowUpDate(Date followUpDate) {
		this.followUpDate = followUpDate;
	}

	public Character getCheckInFromDoctorApp() {
		return checkInFromDoctorApp;
	}

	public void setCheckInFromDoctorApp(Character checkInFromDoctorApp) {
		this.checkInFromDoctorApp = checkInFromDoctorApp;
	}

	public Integer getAdvisedById() {
		return advisedById;
	}

	public void setAdvisedById(Integer advisedById) {
		this.advisedById = advisedById;
	}

	public String getAdvisedByName() {
		return advisedByName;
	}

	public void setAdvisedByName(String advisedByName) {
		this.advisedByName = advisedByName;
	}

	public Integer getNursingStationId() {
		return nursingStationId;
	}

	public void setNursingStationId(Integer nursingStationId) {
		this.nursingStationId = nursingStationId;
	}
	

}

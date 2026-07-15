package com.param.otims.model;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.bloodBank.model.BloodComponentOrderMaster;
import com.param.global.common.LocalTimeConverter;
import com.param.global.model.ServiceMaster;
import com.param.global.common.InstantConverter;

@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_PROCEDURE_ORDER_DETAILS_BY_ID",
					query = "select preg.patient_id as \"patientId\", "
							+ "preg.uhid_number as \"uhidNumber\", "
							+ "concat(coalesce(pre.prefix_code,''),' ',preg.first_name,' ',preg.middle_name,' ',preg.last_name) as \"patientName\","
							+ "prord.is_primary_secondary as \"isPrimarySecondary\", "
							+ " to_char(preg.dob,'DD-MM-YYYY') as \"dob\", "
							+ "prconf.cpt_code as \"cptCode\","
							+ "sm.service_standard_name as \"service\", "
							+ "prtype.procedure_type_name as \"procedureType\", "
							+ "prord.org_id as \"orgId\", prord.unit_id as \"unitId\", "
							+ " to_char(prord.prefered_date_time,'DD-MM-YYYY HH24:MI:SS') as \"preferedDateTime\","
							+ " to_char(proreq.request_date,'YYYY-MM-DD HH24:MI:SS') as \"orderDateTime\","
							+ " prord.created_by as \"createdBy\", prord.procedure_order_id as \"procedureOrderId\",prord.pro_req_id as \"proReqId\", "
							+ " gen.gender_code as \"genderCode\", concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, preg.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, preg.dob)),'M ') as \"age\", "
							+ " prconf.duration as \"duration\", proreq.visit_type_id as \"visitTypeId\", prord.amount as \"amount\", spc.speciality_name as \"speciality\", " 
							+ " preg.patient_category_id as \"patientTypeId\", prord.service_id as \"serviceId\", sm.service_standard_code as \"serviceCode\", "
							+ " sm.speciality_id as \"specialityId\",subsp.sub_speciality_id as \"subSpecialityId\","
							+ " prord.amount as \"rate\", "
							+ "adm.admission_id as \"admissionId\", "
							+ "dm.doctor_id as \"doctorId\","
							+ "proreq.encounter_id as \"encounterId\","
							+ "prord.updated_by as \"updatedBy\","
						//	+ "to_char(prord.status) as \"status\","
							+ "to_char(prord.created_date,'YYYY-MM-DD HH24:MI:SS') as \"createdDate\","
							+ "to_char(prord.updated_date,'YYYY-MM-DD HH24:MI:SS') as \"updatedDate\","
							+ "prconf.procedure_type_id as \"procedureTypeId\", "
							+ "  spd.payment_entitlement_id as \"paymentEntitlementId\","
							+ "  spd.payee_id as \"payeeId\","
							+ " proreq.procedure_status_id as \"procedureStatusId\","
							+ " prord.procedure_sequence as \"procedureSequence\","
							+ " admdtls.billing_bed_category_id as \"billingBedCategoryId\" "
							+ " from otims.t_procedure_order_master prord "
							+ " inner join otims.t_procedure_req_mst proreq on proreq.pro_req_id = prord.pro_req_id "
							+ " inner join otims.t_procedure_configuration prconf on prord.service_id = prconf.service_id "
							+ " inner join service.m_service_master sm on sm.service_master_id = prconf.service_id "
							+ " inner join patient.t_patient_registration preg on preg.patient_id = proreq.patient_id "
							+ " left join m_prefix_master pre on pre.prefix_id = preg.prefix_id "
							+ " inner join otims.m_procedure_type_master prtype on prtype.procedure_type_id = prconf.procedure_type_id "
							//+ " inner join adt.t_admission adm on adm.admission_id = proreq.admission_id "
							+" left join adt.t_admission adm on adm.admission_id = proreq.admission_id "
							//+ " left join adt.t_admission_note admnote on admnote.admission_id = adm.admission_id "
							//+ " left join adt.t_admission_patient_mapping mapper on mapper.admission_note_id = admnote.admission_note_id and mapper.status = 'A' "
							+ "  left JOIN opd.t_patient_sponsor_details spd on spd.admission_id=adm.admission_id AND spd.priority_id=1 and spd.status='A' "
							+ " left join adt.t_admission_details admdtls on admdtls.admission_id = adm.admission_id and admdtls.status='A' "
							+ " left join doctor.m_speciality_master spc on spc.speciality_id = sm.speciality_id "
							+" left join doctor.m_doctor_master dm on  proreq.created_by = dm.user_id  " 
							+ " left join doctor.m_sub_speciality_master subsp on subsp.sub_speciality_id = sm.sub_speciality_id "
							+ " left join public.m_gender_master gen on gen.gender_id = preg.gender_id "
							+ " where  prord.pro_req_id = :orderId and prord.status='A' "),
	
	
	@NamedNativeQuery(name = "GET_PROCEDURE_ORDER_MASTER_BY_PRO_REQ_ID",
	query = "select "
			+ "prord.is_primary_secondary as \"isPrimarySecondary\", "
			+ " prord.procedure_order_id as \"procedureOrderId\",prord.pro_req_id as \"proReqId\", "
			+ " prord.is_blood_component_required  as \"isBloodComponentRequired\"," + 
			"   prord.is_icu_required as \"isIcuRequired\","
			+ " prord.request_reason as \"requestReason\", " + 
			"   prord.is_pac_required  as \"isPacRequired\" "
			+ " from otims.t_procedure_order_master prord "
			+ " where  prord.pro_req_id = :orderId and prord.status='A' "),
	
	@NamedNativeQuery(name = "GET_PRE_CHECKLIST_BY_PRO_REQ_ID",
	query = " select mcm.description  as \"description\" , mcm.is_who as \"isWho\" ," + 
			" mcm.checklist_master_id  as \"checklistMasterId\" " + 
			" from otims.t_procedure_order_checklist tpoc " + 
			" inner join  otims.m_checklist_master mcm  on tpoc.checklist_id  = mcm.checklist_master_id  " + 
			" where procedure_request_id = :proReqId and mcm.stage_id  in(1,2)  and tpoc.status = 'A'  and mcm.status = 'A' "),
	
	@NamedNativeQuery(name = "GET_WHEEL_IN_DATA_BY_PRO_REQ_ID",
	query = " select timeSheet.ot_completion_time_sheet_id  as \"otCompletionTimeSheetId\" " + 
			" from  otims.t_ot_completion_time_sheet  timeSheet " + 
			" where timeSheet.pro_req_id = :proReqId  and timeSheet.status = 'A' "),
	
	
	@NamedNativeQuery(name = "GET_PROCEDURE_REQ_DETAILS_BY_ID",
					 query = "select preg.patient_id as \"patientId\", preg.uhid_number as \"uhidNumber\", concat(coalesce(pre.prefix_code,''),' ',preg.first_name,' ',preg.middle_name,' ',preg.last_name) as \"patientName\", "
							 + " to_char(preg.dob,'DD-MM-YYYY') as \"dob\", prconf.cpt_code as \"cptCode\",sm.service_standard_name as \"service\", prtype.procedure_type_name as \"procedureType\", proreq.org_id as \"orgId\", proreq.unit_id as \"unitId\",  "  
							 + " to_char(proreq.prefered_date_time,'DD-MM-YYYY HH24:MI:SS') as \"preferedDateTime\", proreq.created_by as \"createdBy\", proreq.pro_req_id as \"proReqId\",  "
							 + " gen.gender_code as \"genderCode\", concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, preg.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, preg.dob)),'M ') as \"age\",  "
							 + " prconf.duration as \"duration\",to_char(proreq.request_date,'DD-MM-YYYY HH24:MI:SS') as \"orderDateTime\","
							 + "spd.payment_entitlement_id as \"paymentEntitlementId\", adm.visit_type_id as \"visitTypeId\", spc.speciality_name as \"speciality\",   "
							 + " preg.patient_category_id as \"patientTypeId\" ,proreq.primary_procedure_id as \"serviceId\", sm.service_standard_code as \"serviceCode\",  "
							 +" to_char(preg.dob,'DD-MM-YYYY') as \"birthDate\", "
							 +"  to_char(admdtls.doa, 'dd/MM/yyyy') as \"doa\", "
							 +"  to_char(admdtls.pdd, 'dd/MM/yyyy') as \"pdd\", "
							 +"  bm.bed_number as \"bedNumber\", "
							 +"  ward.ward_name as \"wardName\", "
							 + " sm.speciality_id as \"specialityId\",subsp.sub_speciality_id as \"subSpecialityId\",proreq.admission_id as \"admissionId\" ,spd.payee_id as \"payeeId\" ,  spd.contract_id as \"contractId\" , preg.patient_category_id as \"patientCategoryId\" ,  admdtls.billing_bed_category_id as \"billingBedCategoryId\" "
							 + " from otims.t_procedure_req_mst proreq  "
							 + " inner join otims.t_procedure_configuration prconf on proreq.primary_procedure_id = prconf.service_id  "
							 + " inner join service.m_service_master sm on sm.service_master_id = prconf.service_id  "
							 + " inner join patient.t_patient_registration preg on preg.patient_id = proreq.patient_id  "
							 + " left join m_prefix_master pre on pre.prefix_id = preg.prefix_id "
							 + " inner join otims.m_procedure_type_master prtype on prtype.procedure_type_id = prconf.procedure_type_id  "
							 + " inner join adt.t_admission adm on adm.admission_id = proreq.admission_id  "
							 + " left join adt.t_admission_details ad_details on adm.admission_id = ad_details.admission_id and ad_details.status='A'"
							 + " left JOIN adt.m_bed_master bm on ad_details.bed_id = bm.bed_id  "
							 + " left JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
							// + " left join adt.t_admission_note admnote on admnote.admission_id = adm.admission_id  "
							// + " left join adt.t_admission_patient_mapping mapper on mapper.admission_note_id = admnote.admission_note_id and mapper.status = 'A'  "
							+ "  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=adm.admission_id AND spd.priority_id=1 " 
							+ " left join adt.t_admission_details admdtls on admdtls.admission_id = adm.admission_id  and admdtls.status = 'A' "
							 + " inner join doctor.m_speciality_master spc on spc.speciality_id = sm.speciality_id  "
							 + " inner join doctor.m_sub_speciality_master subsp on subsp.sub_speciality_id = sm.sub_speciality_id  "
							 + " left join public.m_gender_master gen on gen.gender_id = preg.gender_id  "
							 + " where  proreq.pro_req_id = :id "
							 + "union all" + 
							 " select preg.patient_id as \"patientId\", preg.uhid_number as \"uhidNumber\", concat(coalesce(pre.prefix_code,''),' ',preg.first_name,' ',preg.middle_name,' ',preg.last_name) as \"patientName\", " + 
							 " to_char(preg.dob,'DD-MM-YYYY') as \"dob\", prconf.cpt_code as \"cptCode\",sm.service_standard_name as \"service\", prtype.procedure_type_name as \"procedureType\", proreq.org_id as \"orgId\", proreq.unit_id as \"unitId\",  " + 
							 " to_char(proreq.prefered_date_time,'DD-MM-YYYY HH24:MI:SS') as \"preferedDateTime\", proreq.created_by as \"createdBy\", proreq.pro_req_id as \"proReqId\",  " + 
							 " gen.gender_code as \"genderCode\", concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, preg.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, preg.dob)),'M ') as \"age\",  " + 
							 " prconf.duration as \"duration\",to_char(proreq.request_date,'DD-MM-YYYY HH24:MI:SS') as \"orderDateTime\"," + 
							 " spd.payment_entitlement_id as \"paymentEntitlementId\", 1 as \"visitTypeId\", spc.speciality_name as \"speciality\",   " + 
							 " preg.patient_category_id as \"patientTypeId\" ,proreq.primary_procedure_id as \"serviceId\", sm.service_standard_code as \"serviceCode\",  " + 
							 " ' ' as \"birthDate\", "
							 +" ' ' as \"doa\", "
							 +" ' ' as \"pdd\", "
							 +"  ' ' as \"bedNumber\", "
							 +"  ' ' as \"wardName\", "+
							 " sm.speciality_id as \"specialityId\",subsp.sub_speciality_id as \"subSpecialityId\",spd.admission_id as \"admissionId\" ,spd.payee_id as \"payeeId\" ,  spd.contract_id as \"contractId\" , preg.patient_category_id as \"patientCategoryId\" ,  1 as \"billingBedCategoryId\" " + 
							 " from otims.t_procedure_req_mst proreq  " + 
							 " inner join otims.t_procedure_configuration prconf on proreq.primary_procedure_id = prconf.service_id  " + 
							 " inner join service.m_service_master sm on sm.service_master_id = prconf.service_id  " + 
							 " inner join patient.t_patient_registration preg on preg.patient_id = proreq.patient_id  " + 
							 " left join m_prefix_master pre on pre.prefix_id = preg.prefix_id " +
							 " inner join otims.m_procedure_type_master prtype on prtype.procedure_type_id = prconf.procedure_type_id  " + 
							 " inner join opd.t_encounter_master  enctr on enctr.encounter_id = proreq.encounter_id" + 
							 " INNER JOIN opd.t_patient_sponsor_details spd on  enctr.encounter_id = spd.encounter_id AND spd.priority_id=1 " + 
							 " inner join doctor.m_speciality_master spc on spc.speciality_id = sm.speciality_id  " + 
							 " inner join doctor.m_sub_speciality_master subsp on subsp.sub_speciality_id = sm.sub_speciality_id  " + 
							 " left join public.m_gender_master gen on gen.gender_id = preg.gender_id  " + 
							 " where  proreq.pro_req_id = :id"),
	/*@NamedNativeQuery(name = "GET_PROCEDURE_REQ_DETAILS_BY_ID",
	 query = "select distinct preg.patient_id as \"patientId\", preg.uhid_number as \"uhidNumber\", concat(preg.first_name,' ',preg.middle_name,' ',preg.last_name) as \"patientName\", "
			 + " to_char(preg.dob,'DD-MM-YYYY') as \"dob\", prconf.cpt_code as \"cptCode\",sm.service_standard_name as \"service\", prtype.procedure_type_name as \"procedureType\", proreq.org_id as \"orgId\", proreq.unit_id as \"unitId\",  "  
			 + " to_char(proreq.prefered_date_time,'DD-MM-YYYY HH24:MI:SS') as \"preferedDateTime\", proreq.created_by as \"createdBy\", proreq.pro_req_id as \"proReqId\",  "
			 + " gen.gender_code as \"genderCode\", concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, preg.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, preg.dob)),'M ') as \"age\",  "
			 + " prconf.duration as \"duration\", "
			 + "spd.payment_entitlement_id as \"paymentEntitlementId\", adm.visit_type_id as \"visitTypeId\", spc.speciality_name as \"speciality\",   "
			 + " preg.patient_category_id as \"patientTypeId\" ,proreq.primary_procedure_id as \"serviceId\", sm.service_standard_code as \"serviceCode\",  "
			 + " sm.speciality_id as \"specialityId\",subsp.sub_speciality_id as \"subSpecialityId\",proreq.admission_id as \"admissionId\" ,spd.payee_id as \"payeeId\" ,  spd.contract_id as \"contractId\" , preg.patient_category_id as \"patientCategoryId\" ,  admdtls.billing_bed_category_id as \"billingBedCategoryId\" "
			 + " from otims.t_procedure_req_mst proreq  "
			 + " inner join otims.t_procedure_configuration prconf on proreq.primary_procedure_id = prconf.service_id  "
			 + "right join otims.t_procedure_order_master pom on proreq.pro_req_id = pom.pro_req_id "
			 + " inner join service.m_service_master sm on sm.service_master_id = pom.service_id  "
			 + " inner join patient.t_patient_registration preg on preg.patient_id = proreq.patient_id  "
			 + " inner join otims.m_procedure_type_master prtype on prtype.procedure_type_id = prconf.procedure_type_id  "
			 + " inner join adt.t_admission adm on preg.patient_id = adm.patient_id  "
			// + " left join adt.t_admission_note admnote on admnote.admission_id = adm.admission_id  "
			// + " left join adt.t_admission_patient_mapping mapper on mapper.admission_note_id = admnote.admission_note_id and mapper.status = 'A'  "
			+ "  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=adm.admission_id AND spd.priority_id=1 " 
			+ " left join adt.t_admission_details admdtls on admdtls.admission_id = adm.admission_id  "
			 + " inner join doctor.m_speciality_master spc on spc.speciality_id = sm.speciality_id  "
			 + " inner join doctor.m_sub_speciality_master subsp on subsp.sub_speciality_id = sm.sub_speciality_id  "
			 + " left join public.m_gender_master gen on gen.gender_id = preg.gender_id  "
			 + " where  proreq.pro_req_id = :id "), */
							
	@NamedNativeQuery(name="GET_PROCEDURE_ORDER_LIST",
						query="select preg.patient_id as \"patientId\", proreq.admission_id as \"admissionId\",  "
								+ " preg.uhid_number as \"uhidNumber\", concat(coalesce(pre.prefix_code,''),' ',preg.first_name,' ',preg.middle_name,' ',preg.last_name) as \"patientName\",  "
								+ " to_char(proreq.prefered_date_time, 'dd-mm-yyyy') as \"preferedDateTime\", proreq.pro_req_id as \"proReqId\",  "
								+ " prconf.procedure_type_id as \"procedureTypeId\" , prtype.procedure_type_name as \"procedureTypeName\",  "
								+ " sm.service_standard_name as \"serviceName\" ,pordst.status as \"procedureStatus\"  "
								+ " from otims.t_procedure_req_mst proreq  "
								+ " left join otims.t_procedure_configuration prconf on proreq.primary_procedure_id = prconf.service_id  "
								+ " left join service.m_service_master sm on sm.service_master_id = prconf.service_id  "
								+ " inner join patient.t_patient_registration preg on preg.patient_id = proreq.patient_id  "
								+ " left join m_prefix_master pre on pre.prefix_id = preg.prefix_id "
								+ " left join otims.m_procedure_type_master prtype on prtype.procedure_type_id = prconf.procedure_type_id  "
								+ " left join otims.m_procedure_order_status pordst on pordst.id = proreq.procedure_status_id  "),
	
	@NamedNativeQuery(name="GET_PROCEDURE_BY_DATE",
	query="select "
			+ "	proc_mst.procedure_order_id as \"procedureOrderId\", "
			+ "	proc_mst.service_id as \"serviceId\", "
			+ "	proc_mst.unit_id as \"unitId\", "
			+ "	proc_mst.org_id as \"orgId\", "
			+ "	req_mst.primary_procedure_id as \"primaryProcedureId\", "
			+ "	req_mst.patient_id as \"patientId\", "
			+ "	req_mst.admission_id as \"admissionId\", "
			+ "	req_mst.encounter_id as \"encounterId\", "
			+ "	to_char(req_mst.prefered_date_time, "
			+ "	'MM-DD-YYYY HH:MI:SS') as \"preferedDateTime\", "
			+ "	to_char(req_mst.request_date, "
			+ "	'MM-DD-YYYY HH:MI:SS') as \"requestDate\", "
			+ "	req_mst.request_by as \"requestBy\", "
			+ "	proc_mst.is_blood_component_required as \"isBloodComponentRequired\", "
			+ "	service_mst.service_standard_name as \"procedureName\", "
			+ "	proc_mst.is_primary_secondary as \"isPrimarySecondary\", "
			+ "	prefix_mst.prefix_code || ' ' || pati_reg.first_name || ' ' || pati_reg.last_name as \"patientName\", "
			+ "	pati_reg.uhid_number as \"uhid\" "
			+ "from "
			+ "	otims.t_procedure_req_mst req_mst "
			+ "inner join otims.t_procedure_order_master proc_mst on "
			+ "	req_mst.pro_req_id = proc_mst.pro_req_id "
			+ "inner join service.m_service_master service_mst on "
			+ "	proc_mst.service_id = service_mst.service_master_id "
			+ "inner join patient.t_patient_registration pati_reg on "
			+ "	req_mst.patient_id = pati_reg.patient_id "
			+ "inner join public.m_gender_master gender_mst on "
			+ "	pati_reg.gender_id = gender_mst.gender_id "
			+ "inner join public.m_prefix_master prefix_mst on "
			+ "	pati_reg.prefix_id = prefix_mst.prefix_id "
			+ "where "
			+ "	proc_mst.exported_to_otims=FALSE "
			),
	

	@NamedNativeQuery(name="GET_ROL_LIST_PROCEDURE",
	query="SELECT"
			+" procrolemst.procedure_role_id AS \"id\","
			+" procrolemst.procedure_role_name AS \"name\" "
			+"FROM "
			+" otims.m_procedure_role_master procrolemst "
			+"WHERE "
			+" procrolemst.status = 'A' "
			+" AND procrolemst.org_id =:orgId "
			+" AND procrolemst.unit_id =:unitId "),
	
	/*@NamedNativeQuery(name="GET_LIST_OF_PROCEDURE_ORDER",
	query=" select spl.speciality_name as \"specialityName\" , "
				+ "pom.procedure_order_id as \"procedureOrderId\", "
				+ "pom.urgency as \"urgency\", "
				+ "service_standard_name \"procedureName\", "
				+ "to_char(pom.prefered_date_time,'dd/mm/yyyy') as \"preferedDateTime\","
				+ "to_char(pom.prefered_time,'HH:MI') as \"preferedTime\", "
				+ "pom.request_reason as \"requestReason\","
				+ "pom.is_icu_required as \"isIcuRequired\","
				+ "pom.is_primary_secondary as \"isPrimarySecondary\","
				+ "pom.is_blood_component_required as  \"isBloodComponentRequired\","
				+ "pom.is_pac_required as \"isPacRequired\" "
				+"FROM otims.t_procedure_req_mst prm "
				+"inner join otims.t_procedure_order_master pom on prm.pro_req_id=pom.pro_req_id "
				+"inner join service.m_service_master ser on ser.service_master_id= pom.service_id "
				+"inner join doctor.m_speciality_master spl on spl.speciality_id=ser.speciality_id "
				+"where prm.encounter_id=:encounterId "),*/
	
	@NamedNativeQuery(name="GET_LIST_OF_PROCEDURE_ORDER",
	query=" select spl.speciality_name as \"specialityName\" , "
				+ "pom.procedure_order_id as \"procedureOrderId\", "
				+ "pom.urgency as \"urgency\", "
				+ "	prefix_mst.prefix_code || ' ' || preg.first_name || ' ' || preg.last_name as \"patientName\", "
				+ "service_standard_name \"procedureName\", "
				+ "prm.patient_id as \"patientId\", "
				+ "prm.admission_id as \"admissionId\", "
				+ "prm.encounter_id as \"encounterId\", "
				+ "to_char(pom.prefered_date_time,'dd/mm/yyyy') as \"preferedDateTime\","
				+ "to_char(pom.prefered_time,'HH:MI') as \"preferedTime\", "
				+ "pom.request_reason as \"requestReason\","
				+ "pom.is_icu_required as \"isIcuRequired\","
				+ "pom.is_primary_secondary as \"isPrimarySecondary\","
				+ "pom.is_blood_component_required as  \"isBloodComponentRequired\","
				+ "pom.is_pac_required as \"isPacRequired\", "
				+ "preg.image_path as \"imagePath\", "
				+ "preg.uhid_number as \"uhidNumber\", "
				+ "concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, preg.dob)) , 'Y ') as \"age\" ,"
				+ "gen.gender_code as \"genderCode\", "
				+ "dm.doctor_id as \"doctorId\",  "
				+  "dm.first_name || ' ' || dm.last_name as \"doctorName\", "
				+  "(select dia.diagnosis_desc "
				+  "from patient.t_patient_diagnosis_appo_mapper pdm "
				+ "inner join emr.m_diagnosis_master dia on "
				+ "dia.diagnosis_id = pdm.diagnosis_id "
				+ "where pdm.encounter_id = prm.encounter_id "
				+ " order by 1 desc limit 1) as \"primaryDiagnosis\" "
				+ "FROM otims.t_procedure_req_mst prm "
				+ "inner join otims.t_procedure_order_master pom on prm.pro_req_id=pom.pro_req_id "
				+ "inner join service.m_service_master ser on ser.service_master_id= pom.service_id "
				+ "inner join doctor.m_speciality_master spl on spl.speciality_id=ser.speciality_id "
				+ "inner join patient.t_patient_registration preg on preg.patient_id = prm.patient_id  "
				+ "inner join public.m_gender_master gen on preg.gender_id = gen.gender_id  "
				+ "inner join doctor.m_doctor_master dm on  prm.created_by = dm.user_id  "
				+ " LEFT JOIN public.m_prefix_master prefix_mst on preg.prefix_id=prefix_mst.prefix_id "
				+ "where prm.encounter_id=:encounterId "),
	
	@NamedNativeQuery(name="GET_LIST_OF_PROCEDURE_ORDER_BY_ADMISSION_ID",
	query=" select spl.speciality_name as \"specialityName\" , "
			  	+ "spl.speciality_id as \"specialityId\", "
				+ "pom.procedure_order_id as \"procedureOrderId\", "
				+ "pom.service_id as \"serviceId\", "
				+ "pom.urgency as \"urgency\", "
				+ "service_standard_name \"procedureName\", "
				+ "to_char(pom.prefered_date_time,'dd-MM-yyyy') as \"preferedDateTime\","
				+ "to_char(pom.prefered_time,'hh24:mi') as \"preferedTime\","
				+ "prm.pro_req_id as \"proReqId\", "
				+ "prm.procedure_status_id as \"procedureStatusId\", "
				+ "pom.request_reason as \"requestReason\","
				+ "to_char(pom.prefered_date_time,'yyyy') as \"year\","
				+ "to_char(pom.prefered_date_time,'month') as \"month\","
				+ "to_char(pom.prefered_date_time,'dd/MM/yyyy') as \"orderDateTime\","
				+ "pom.created_by as \"createdBy\","
				+ " to_char(pom.created_date, 'dd-MM-yyyy HH:MI:SS')as \"createdDate\", "
				+ " to_char(pom.updated_date, 'dd-MM-yyyy HH:MI:SS')as \"updatedDate\", "
				+ "pom.updated_by as \"updatedBy\","
				+ "pom.amount as \"amount\","
				+ "pom.is_icu_required as \"isIcuRequired\","
				+ "pom.is_primary_secondary as \"isPrimarySecondary\","
				+ "pom.is_blood_component_required as  \"isBloodComponentRequired\","
				+ "prm.org_id as \"orgId\", "
				+ "CAST(RTRIM(pom.status) AS VARCHAR(10)) as \"status\", "
				+ "prm.unit_id as \"unitId\",  "
				+ "pom.is_pac_required as \"isPacRequired\" "
				+"FROM otims.t_procedure_req_mst prm "
				+"inner join otims.t_procedure_order_master pom on prm.pro_req_id=pom.pro_req_id "
				+"inner join service.m_service_master ser on ser.service_master_id= pom.service_id "
				+"inner join doctor.m_speciality_master spl on spl.speciality_id=ser.speciality_id "
				+"where  "
				+ " pom.status='A' "),
	
	@NamedNativeQuery(name="GET_LIST_OF_PROCEDURE_ORDER_BY_ADMISSION_ID_ENCOUNTER_ID",
	query=" select spl.speciality_name as \"specialityName\" , "
			  	+ "spl.speciality_id as \"specialityId\", "
				+ "pom.procedure_order_id as \"procedureOrderId\", "
				+ "pom.service_id as \"serviceId\", "
				+ "pom.urgency as \"urgency\", "
				+ "service_standard_name \"procedureName\", "
				+ "to_char(pom.prefered_date_time,'dd-MM-yyyy') as \"preferedDateTime\","
				+ "to_char(pom.prefered_time,'hh24:mi') as \"preferedTime\","
				+ "prm.pro_req_id as \"proReqId\", "
				+ "prm.procedure_status_id as \"procedureStatusId\", "
				+ "pom.request_reason as \"requestReason\","
				+ "to_char(pom.prefered_date_time,'yyyy') as \"year\","
				+ "to_char(pom.prefered_date_time,'month') as \"month\","
				+ "to_char(pom.prefered_date_time,'dd/MM/yyyy') as \"orderDateTime\","
				+ "pom.created_by as \"createdBy\","
				+ " to_char(pom.created_date, 'dd-MM-yyyy HH:MI:SS')as \"createdDate\", "
				+ " to_char(pom.updated_date, 'dd-MM-yyyy HH:MI:SS')as \"updatedDate\", "
				+ "pom.updated_by as \"updatedBy\","
				+ "pom.amount as \"amount\","
				+ "pom.is_icu_required as \"isIcuRequired\","
				+ "pom.is_primary_secondary as \"isPrimarySecondary\","
				+ "pom.is_blood_component_required as  \"isBloodComponentRequired\","
				+ "prm.org_id as \"orgId\", "
				+ "CAST(RTRIM(pom.status) AS VARCHAR(10)) as \"status\", "
				+ "prm.unit_id as \"unitId\",  "
				+ "pom.is_pac_required as \"isPacRequired\", "
				+ "pom.prefered_date_time as \"preferedDateTimeNew\""
				+"FROM otims.t_procedure_req_mst prm "
				+"inner join otims.t_procedure_order_master pom on prm.pro_req_id=pom.pro_req_id "
				+"inner join service.m_service_master ser on ser.service_master_id= pom.service_id "
				+"inner join doctor.m_speciality_master spl on spl.speciality_id=ser.speciality_id "
				+"where  "
				+ " pom.status='A' "
				+ "AND prm.procedure_status_id = 2"),
	
	@NamedNativeQuery(name="SEARCH_PATIENT_AND_PROCEDURE_DETAILS",
	query=" select distinct pom.procedure_order_id as\"procedureOrderId\", prm.visit_type_id as \"visitTypeId\",  " + 
			" spl.speciality_name as \"specialityName\", pom.org_id as \"organizationId\", pom.unit_id as \"unitId\", " + 
			" pom.urgency as \"urgency\", pom.created_by as \"createdBy\", service_standard_name as \"procedureName\",  " + 
			" concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pregi.dob)) , 'Y ' ,  " + 
			" EXTRACT(month from AGE(CURRENT_TIMESTAMP, pregi.dob)),'M ') as \"age\",  " + 
			" prm.patient_id as \"patientId\", pr.uhid_number as \"uhidNumber\", pr.patientname as \"patientName\", " + 
			" dm.first_name || ' ' || dm.last_name as \"doctorName\", dm.doctor_id as  \"doctorId\", pr.gender_name as \"genderName\",  " + 
			" prm.encounter_id as \"encounterId\", admsn.admission_id as \"admissionId\" ,"
		//	+ " ad_details.ward_id as \"ward\", " 
		//	+ " ad_details.bed_id as \"bed\", "
			+" to_char(pregi.dob,'DD-MM-YYYY') as \"birthDate\", "
			+"  to_char(ad_details.doa, 'dd/MM/yyyy') as \"doa\", "
			+"  to_char(ad_details.pdd, 'dd/MM/yyyy') as \"pdd\", "
			+" bm.bed_id as \"bed\"," 
			+"	ward.ward_id as \"ward\", "
			+"  bm.bed_number as \"bedNumber\", "
			+" ward.ward_name as \"wardName\", "
			+"	pregi.gender_id as \"genderId\", "
			+"	pregi.image_path as \"imagePath\", "
			+"  gender.gender_code as \"genderCode\", "
			+ " to_char(pom.prefered_time,' HH24:MI:SS') as \"preferTime\", " 
			+ "to_char(pom.prefered_date_time ,'DD-MM-YYYY') as \"procedureDate\" " + 
			" from otims.t_procedure_req_mst prm  " + 
			" inner join otims.t_procedure_order_master pom on prm.pro_req_id=pom.pro_req_id  " + 
			" inner join service.m_service_master ser on ser.service_master_id= pom.service_id  " + 
			" inner join doctor.m_speciality_master spl on spl.speciality_id=ser.speciality_id  " + 
			" inner join patient.patientinformation pr on pr.patient_id=prm.patient_id  " + 
			" inner join doctor.m_doctor_master dm on  prm.created_by = dm.user_id  " + 
			" inner join patient.t_patient_registration pregi on pr.patient_id=pregi.patient_id  "+ 
			"  inner join public.m_gender_master gender on pregi.gender_id = gender.gender_id "+ 
			" left join otims.t_medication_details medication on pom.procedure_order_id = medication.procedure_order_id " + 
			" left join otims.t_implant_details implant on pom.procedure_order_id = implant.procedure_order_id  " + 
			" left join public.m_item itm on medication.item_id = itm.id  " + 
			" left join public.m_kit kit on medication.kit_id = kit.id  " + 
			" left join adt.t_admission  admsn on prm.admission_id = admsn.admission_id " + 
			" left join adt.t_admission_details ad_details on admsn.admission_id = ad_details.admission_id and ad_details.status='A'"
			+ " left JOIN adt.m_bed_master bm on ad_details.bed_id = bm.bed_id  "
			+" left JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "),
	
	@NamedNativeQuery(name="GET_PROCEDURE_ORDER_ID",
	query="select "
			+ "	proc_mst.procedure_order_id as \"procedureOrderId\", "
			+ "	proc_mst.service_id as \"serviceId\", "
			+ "	proc_mst.unit_id as \"unitId\", "
			+ "	proc_mst.org_id as \"orgId\", "
			+ "	proc_mst.created_by as \"createdBy\", "
			+ "	proc_mst.updated_by as \"updatedBy\", "
			+ "	to_char(proc_mst.prefered_date_time, "
			+ "	'MM-DD-YYYY HH:MI:SS') as \"preferedDateTime\", "
			+ "	to_char(proc_mst.created_date, "
			+ "	'MM-DD-YYYY HH:MI:SS') as \"createdDate\", "
			+ "	to_char(proc_mst.updated_date, "
			+ "	'MM-DD-YYYY HH:MI:SS') as \"updatedDate\", "
			+ "	proc_mst.amount as \"amount\", "
			+ "	proc_mst.is_blood_component_required as \"isBloodComponentRequired\", "
			+ "	proc_mst.urgency as \"urgency\", "
			+ "	proc_mst.request_reason as \"requestReason\", "
			+ "	proc_mst.is_icu_required as \"isPacRequired\", "
			+ "	proc_mst.exported_to_otims as \"exportedToOtims\", "
			+ "	proc_mst.pro_req_id as \"proReqId\", "
			+ "	proc_mst.is_primary_secondary as \"isPrimarySecondary\" "
			+ "from "
			+ "	otims.t_procedure_order_master proc_mst "
			+ "where "
			+ "	proc_mst.procedure_order_id= :procedureOrderId "
			),
	
	@NamedNativeQuery(name = "GET_PROCEDURE_REQUEST_ID_BY_ORDER_ID",
	query = "select "
			+ "	proc_mst.procedure_order_id as \"procedureOrderId\", "
			+ "	proc_mst.service_id as \"serviceId\", "
			+ "	proc_mst.unit_id as \"unitId\", "
			+ "	proc_mst.org_id as \"orgId\", "
			+ "	proc_mst.created_by as \"createdBy\", "
			+ "	proc_mst.updated_by as \"updatedBy\", "
			+ "	to_char(proc_mst.prefered_date_time, "
			+ "	'MM-DD-YYYY HH:MI:SS') as \"preferedDateTime\", "
			+ "	to_char(proc_mst.created_date, "
			+ "	'MM-DD-YYYY HH:MI:SS') as \"createdDate\", "
			+ "	to_char(proc_mst.updated_date, "
			+ "	'MM-DD-YYYY HH:MI:SS') as \"updatedDate\", "
			+ "	proc_mst.amount as \"amount\", "
			+ "	proc_mst.is_blood_component_required as \"isBloodComponentRequired\", "
			+ "	proc_mst.urgency as \"urgency\", "
			+ "	proc_mst.request_reason as \"requestReason\", "
			+ "	proc_mst.is_icu_required as \"isPacRequired\", "
			+ "	proc_mst.exported_to_otims as \"exportedToOtims\", "
			+ "	proc_mst.pro_req_id as \"proReqId\", "
			+ "	proc_mst.is_primary_secondary as \"isPrimarySecondary\" "
			+ "from "
			+ "	otims.t_procedure_order_master proc_mst "
			+ "where "
			+ "	proc_mst.procedure_order_id= :procedureOrderId "),
	
    @NamedNativeQuery(name = "GET_LIST_OF_MEDICATION_BY_PROCEDURE_REQUEST_ID",
    query = "select medication.otc_completion_inv_medication_id as \"otcCompletionInvMedicationId\", "
            +     "medication.item_id as \"itemId\", "
            +     "medication.procedure_request_id as \"procedureRequestId\", "
            +     "itm.name as \"itemName\", "
            +     "itm.is_implant as \"isImplant\", "
            +     "itm.is_consumable as \"isConsumable\", "
            +     "itm.code as \"itemCode\", "
            +     "medication.booked_qty as \"bookedQty\", "
            +     "medication.used_qty as \"usedQty\", "
            +     "medication.mrp as \"mrp\", "
            +     "medication.tax_percentage as \"taxPercentage\", "
            +     "medication.remaining_qty as \"remainingQty\", "
            +     "medication.store_id as \"storeId\", "
            +     "medication.total_amount  as \"totalAmount\", "
            +    "medication.batch_id as \"batchId\", "
            +    "batch.batch_no as \"batchNo\", "
            +    "umt.type as \"uomType\", "
            +    "unt.code as \"uomUnit\", "
            +     "medication.uom_type_id as \"uomTypeId\", "
            +     "medication.issue_details_batch_mapper_id as \"issueDetailsBatchMpprId\" "
            +     "FROM otims.t_otc_completion_inv_medication medication "
            +    "inner join public.m_item itm on medication.item_id = itm.id "
            +    "left join public.m_uom_type umt on medication.uom_type_id = umt.id "
            +    "left join public.m_uom_unit unt on medication.uom_unit_id = unt.id "
            +    "inner join public.m_store st on medication.store_id = st.store_id "
            +    "inner join inventory.t_batch batch on medication.batch_id = batch.id "
            +     "where medication.procedure_request_id= :procedureRequestId"),
    
    @NamedNativeQuery(name = "GET_LIST_OF_IMPLANT_BY_PROCEDURE_REQUEST_ID",
    query = "select implant.ot_completion_inv_implants_id as \"otcCompletionInvImplantsId\", "
            +     "implant.item_id as \"itemId\", "
            +     "itm.name as \"itemName\", "
            +     "itm.code as \"itemCode\", "
            +     "itm.is_implant as \"isImplant\", "
            +     "itm.is_consumable as \"isConsumable\", "
            +     "implant.booked_qty as \"bookedQty\", "
            +     "implant.used_qty as \"usedQty\", "
            +     "implant.mrp as \"mrp\", "
            +     "implant.tax_percentage as \"taxPercentage\", "
            +     "implant.remaining_qty as \"remainingQty\", "
            +     "implant.store_id as \"storeId\", "
            +     "implant.total_amount  as \"totalAmount\", "
            +    "implant.batch_id as \"batchId\", "
            +    "batch.batch_no as \"batchNo\", "
            +     "implant.issue_details_batch_mapper_id as \"issueDetailsBatchMpprId\", "
            +    "umt.type as \"uomType\", "
            +    "unt.code as \"uomUnit\", "
            +     "implant.uom_type_id as \"uomTypeId\", "
            +     "implant.uom_unit_id as \"uomUnitId\" "
            +     "FROM otims.t_ot_completion_inv_implants implant "
            +    "inner join public.m_item itm on implant.item_id = itm.id "
            +    "inner join public.m_uom_type umt on implant.uom_type_id = umt.id "
            +    "inner join public.m_uom_unit unt on implant.uom_unit_id = unt.id "
            +    "inner join public.m_store st on implant.store_id = st.store_id "
            +    "inner join inventory.t_batch batch on implant.batch_id = batch.id "
            +     "where implant.procedure_request_id= :procedureRequestId"),
    
    
    @NamedNativeQuery(name = "GET_PROCEDURE_DETAILS_BY_ADMISSION",
    query =  "select "
    		+ "	ord_mst.order_no as \"orderNo\", "
    		+ "	t_ord_dtls.order_qty as \"ordQty\", "
    		+ "	to_char(ord_mst.order_date, 'DD-MM-YYYY HH24:MI:SS') as \"orderDate\", "
    		+ " to_char(req_mst.request_date, 'DD-MM-YYYY HH24:MI:SS')  as \"procedureDate\" ,"
    		+ "	service_mst.service_standard_code as \"procedureCode\", "
    		+ "	service_mst.service_standard_name as \"procedureName\", "
    		+ "	emp_mst.first_name||' '||emp_mst.last_name as \"orderBy\" ,"
    		+ " proce_type_mst.procedure_type_name as \"grade\" "
    		+ "from "
    		+ "	otims.t_procedure_req_mst req_mst "
    		+ "inner join otims.t_procedure_order_master p_ord_mst on "
    		+ "	req_mst.pro_req_id = p_ord_mst.pro_req_id "
    		+ "inner join otims.t_procedure_configuration proc_config on "
    		+ "	proc_config.service_id = p_ord_mst.service_id "
    		+ "inner join otims.m_procedure_type_master proce_type_mst on "
    		+ "	proc_config.procedure_type_id = proce_type_mst.procedure_type_id "
    		+ "inner join public.t_order_master ord_mst on "
    		+ "	ord_mst.admission_id =req_mst.admission_id "
    		+ "inner join public.t_order_details  t_ord_dtls on "
    		+ "	t_ord_dtls.service_id =p_ord_mst.service_id "
    		+ "	and ord_mst.order_id = t_ord_dtls.order_id "
    		+ "	and t_ord_dtls.is_procedure = 'Y' "
    		+ "inner join service.m_service_master service_mst on "
    		+ "	service_mst.service_master_id = p_ord_mst.service_id "
    		+ "left join public.m_employee_master emp_mst on "
    		+ "	emp_mst.user_id = req_mst.created_by "
    		+ "where "
    		+ "	req_mst.admission_id = :admissionId " ),
    
    @NamedNativeQuery(name="GET_PROCEDURE_ORDER_LIST_BY_DOCTOR_FOR_EMR",
	query=" select spl.speciality_name as \"specialityName\" , "
				+ "pom.procedure_order_id as \"procedureOrderId\", "
				+ "pom.urgency as \"urgency\", "
				+ "	prefix_mst.prefix_code || ' ' || preg.first_name || ' ' || preg.last_name as \"patientName\", "
				+ "service_standard_name \"procedureName\", "
				+ "prm.patient_id as \"patientId\", "
				+ "prm.admission_id as \"admissionId\", "
				+ "prm.encounter_id as \"encounterId\", "
				+ "to_char(pom.prefered_date_time,'dd/mm/yyyy') as \"preferedDateTime\","
				+ "to_char(pom.prefered_time,'HH:MI') as \"preferedTime\", "
				+ "pom.request_reason as \"requestReason\","
				+ "pom.is_icu_required as \"isIcuRequired\","
				+ "pom.is_primary_secondary as \"isPrimarySecondary\","
				+ "pom.is_blood_component_required as  \"isBloodComponentRequired\","
				+ "pom.is_pac_required as \"isPacRequired\", "
				+ "preg.image_path as \"imagePath\", "
				+ "preg.uhid_number as \"uhidNumber\", "
				+ "preg.gender_id as \"genderId\", "
				+ "to_char(preg.dob, 'dd/mm/yyyy') as \"dob\", "
				+ "concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, preg.dob)) , 'Y ') as \"age\" ,"
				+ "gen.gender_code as \"genderCode\", "
				+ "dm.doctor_id as \"doctorId\",  "
				+  "dm.first_name || ' ' || dm.last_name as \"doctorName\", "
				+  "(select dia.diagnosis_desc "
				+  "from patient.t_patient_diagnosis_appo_mapper pdm "
				+ "inner join emr.m_diagnosis_master dia on "
				+ "dia.diagnosis_id = pdm.diagnosis_id "
				+ "where pdm.encounter_id = prm.encounter_id "
				+ " order by 1 desc limit 1) as \"primaryDiagnosis\" "
				+ "FROM otims.t_procedure_req_mst prm "
				+ "inner join otims.t_procedure_order_master pom on prm.pro_req_id=pom.pro_req_id "
				+ "inner join service.m_service_master ser on ser.service_master_id= pom.service_id "
				+ "inner join doctor.m_speciality_master spl on spl.speciality_id=ser.speciality_id "
				+ "inner join patient.t_patient_registration preg on preg.patient_id = prm.patient_id  "
				+ " left join m_prefix_master pre on pre.prefix_id = preg.prefix_id "
				+ "inner join public.m_gender_master gen on preg.gender_id = gen.gender_id  "
				+ "inner join doctor.m_doctor_master dm on  prm.created_by = dm.user_id  "
				+ " LEFT JOIN public.m_prefix_master prefix_mst on preg.prefix_id=prefix_mst.prefix_id "
				+ "where dm.doctor_id=:doctorId "
				+ "AND prm.org_id=:orgId "
				+ "AND prm.unit_id=:orgUnitId "),
    
    @NamedNativeQuery(name="GET_PROCEDURE_ORDER_LIST_FOR_BOOK_VIEW",
	query=" select spl.speciality_name as \"specialityName\" , "
			+ "pom.procedure_order_id as \"procedureOrderId\", "
			+ "pom.urgency as \"urgency\", "
			+ "	prefix_mst.prefix_code || ' ' || preg.first_name || ' ' || preg.last_name as \"patientName\", "
			+ "service_standard_name \"procedureName\", "
			+ "prm.patient_id as \"patientId\", "
			+ "prm.admission_id as \"admissionId\", "
			+ "prm.encounter_id as \"encounterId\", "
			+ "to_char(pom.prefered_date_time,'dd/mm/yyyy') as \"preferedDateTime\","
			+ "to_char(pom.prefered_time,'HH:MI') as \"preferedTime\", "
			+ "pom.request_reason as \"requestReason\","
			+ "pom.is_icu_required as \"isIcuRequired\","
			+ "pom.is_primary_secondary as \"isPrimarySecondary\","
			+ "pom.is_blood_component_required as  \"isBloodComponentRequired\","
			+ "pom.is_pac_required as \"isPacRequired\", "
			+ "preg.image_path as \"imagePath\", "
			+ "preg.uhid_number as \"uhidNumber\", "
			+ "concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, preg.dob)) , 'Y ') as \"age\" ,"
			+ "gen.gender_code as \"genderCode\", "
			+ "dm.doctor_id as \"doctorId\",  "
			+  "dm.first_name || ' ' || dm.last_name as \"doctorName\", "
			+  "(select dia.diagnosis_desc "
			+  "from patient.t_patient_diagnosis_appo_mapper pdm "
			+ "inner join emr.m_diagnosis_master dia on "
			+ "dia.diagnosis_id = pdm.diagnosis_id "
			+ "where pdm.encounter_id = prm.encounter_id "
			+ " order by 1 desc limit 1) as \"primaryDiagnosis\" "
			+ "FROM otims.t_procedure_req_mst prm "
			+ "inner join otims.t_procedure_order_master pom on prm.pro_req_id=pom.pro_req_id "
			+ "inner join service.m_service_master ser on ser.service_master_id= pom.service_id "
			+ "inner join doctor.m_speciality_master spl on spl.speciality_id=ser.speciality_id "
			+ "inner join patient.t_patient_registration preg on preg.patient_id = prm.patient_id  "
			+ " left join m_prefix_master pre on pre.prefix_id = preg.prefix_id "
			+ "inner join public.m_gender_master gen on preg.gender_id = gen.gender_id  "
			+ "inner join doctor.m_doctor_master dm on  prm.created_by = dm.user_id  "
			+ " LEFT JOIN public.m_prefix_master prefix_mst on preg.prefix_id=prefix_mst.prefix_id "
			+ "where prm.patient_id=:patientId "
			+ "and prm.org_id=:organizationId "
			+ "and pom.status='A'"
			+ " and to_char(pom.created_date, 'YYYY-MM-DD')  >=  :fromDate "
			+ " AND to_char(pom.created_date, 'YYYY-MM-DD')  <=   :toDate "
			),
    

    @NamedNativeQuery(name="SEARCH_PLANNED_PROCEDURE_BY_VISIT_TYPE_ID",
	query="select  distinct ON(prm.pro_req_id)prm.pro_req_id as \"proReqId\", " 
			+" pr.uhid_number as \"uhidNumber\","
			+ " otres.ot_reservation_id as \"otReservationId\", "
			+ " otres.ot_reservation_no as \"otReservationNo\", " + 
            "(case when otres.visit_type_id is not null "
            + "then  otres.visit_type_id else (case when prm.admission_id is not null then pdmin.visit_type_id " + 
            "            else 1 " + 
            "        end " + 
            "   ) end ) as \"visitTypeId\","+
			//"  otres.visit_type_id as \"visitTypeId\", " + 
			"  otres.schedule_id as \"scheduleId\", " + 
			"	otres.reservation_status_id as \"reservationStatusId\", " + 
			"	otres.reason_note as \"reasonNote\", " + 
			"	otres.reason_id as \"reasonId\", " + 
			"	otres.organization_id as \"organizationId\", " + 
			"	otres.unit_id as \"unitId\", " + 
			"	otres.created_by as \"createdBy\", " + 
			"	otres.updated_by as \"updatedBy\", " + 
			"	to_char(otres.created_date,'dd-MM-yyyy HH:ss:mm') as \"createdDate\", " + 
			"	to_char(otres.updated_date,'dd-MM-yyyy HH:ss:mm') as \"updatedDate\", " + 
			"	otres.status as \"status\", " + 
			" 	prefix_mst.prefix_code as \"prefixCode\", " + 
			" 	pr.email as \"emailId\", " + 
			"  	to_char(pr.dob, 'dd/MM/yyyy') as \"dob\", " + 
			"	pr.patient_category_id as \"patientCategoryId\", " + 
			"	pr.registration_type_id as \"registrationTypeId\", " + 
			" 	proConfig.operation_theatre_type_id as \"operationTheatreTypeId\", " + 
			" 	otTable.table_name as \"tableName\", " 
			+" concat(coalesce(prefix_mst.prefix_code),' ',coalesce(pr.first_name),' ',coalesce(pr.last_name)) as \"patientName\"," + 
			//" concat(coalesce(doc.first_name,''),' ',coalesce(doc.middle_name,''),' ',coalesce(doc.last_name,'')) as \"doctorName\", " +
			"  (case when admDoc.doctor_id is not null then concat(coalesce(admDoc.first_name,''),' ',coalesce(admDoc.middle_name,''),' ',coalesce(admDoc.last_name,'')) " + 
			" else " + 
			" concat(coalesce(encounterDoc.first_name,''),' ',coalesce(encounterDoc.middle_name,''),' ',coalesce(encounterDoc.last_name,'')) " + 
			" end " + 
			" ) as \"doctorName\", "+
			" pr.patient_id as \"patientId\"," + 
			" prm.admission_id as \"admissionId\"," + 
			" prm.encounter_id as \"encounterId\"," + 
			 "  to_char(adtDtls.doa, 'dd/MM/yyyy') as \"doa\", "+
			 "  to_char(adtDtls.pdd, 'dd/MM/yyyy') as \"pdd\", "+
			 "  to_char(pr.dob, 'dd/MM/yyyy') as \"birthDate\", "+
			 " bm.bed_number as \"bedNumber\", "+
			" to_char(prm.request_date,'MM-DD-YYYY HH:MI:SS') as \"requestDate\"," + 
			" to_char(schedule.prefer_time,'hh24:mi:ss') as \"preferTime\"," + 
			" schedule.procedure_schedule_id as \"procedureScheduleId\"," + 
			" schedule.procedure_name as \"procedureName\" ," + 
			" schedule.procedure_type as \"procedureType\"," + 
			" to_char(schedule.schedule_date,'YYYY-MM-DD') as \"scheduleDate\"," + 
			" schedule.total_duration as \"totalDuration\"," + 
			" schedule.department as \"department\"," + 
			" schedule.department_id as \"departmentId\"," + 
			"  gender.gender_code as \"genderCode\", " +
			"	pr.gender_id as \"genderId\", "+
			"	pr.image_path as \"imagePath\", "+
			"	(extract(year from age(current_timestamp,	pr.dob))|| 'Y ' || extract(month from age(current_timestamp,pr.dob))|| 'M ') as \"age\", "+
			" schedule.proceduer_status_id as \"proceduerStatusId\"," + 
			" schedule.ot_type as \"otType\"," + 
			" to_char(pdmin.created_date,'MM-DD-YYYY HH:MI:SS') as \"admintionDate\",  " +
			" ward.ward_name as \"wardName\" "+
			" from otims.t_procedure_schedule schedule " + 
			" inner join otims.t_procedure_req_mst prm on schedule.pro_req_id = prm.pro_req_id " + 
			" left join adt.t_admission pdmin on pdmin.admission_id = prm.admission_id " + 
			 "left join adt.t_admission_details adtDtls on pdmin.admission_id = adtDtls.admission_id and adtDtls.status='A' "+
			" left JOIN adt.m_bed_master bm on adtDtls.bed_id = bm.bed_id " +
			 " 	left join adt.m_ward_master ward on ward.ward_id = bm.ward_id "+
			//"left join doctor.m_doctor_master doc on doc.doctor_id = pdmin.doctor_id " +
			" left join opd.t_encounter_master encounter on encounter.encounter_id = prm.encounter_id "+
			" left join doctor.m_doctor_master admDoc on admDoc.doctor_id = pdmin.doctor_id " + 
			" left join doctor.m_doctor_master encounterDoc on encounterDoc.doctor_id = encounter.doctor_id "+
			" inner join patient.t_patient_registration pr on prm.patient_id = pr.patient_id " + 
			" inner join public.m_prefix_master prefix_mst on pr.prefix_id = prefix_mst.prefix_id "
		  + "  left join otims.t_ot_reservation otres on otres.pro_req_id = prm.pro_req_id " + 
			"  inner join public.m_gender_master gender on pr.gender_id = gender.gender_id "
		  + "  inner join otims.t_procedure_ot_schedule ot on 	ot.procedure_schedule_id = schedule.procedure_schedule_id " + 
			"  inner join otims.m_operation_table otTable on 	ot.table_id = otTable.operation_table_id  " + 
			"  inner join otims.t_procedure_configuration proConfig on prm.primary_procedure_id = proConfig.service_id " +
			" where  prm.procedure_status_id = 3 " 
			+ " and schedule.organization_id = :organizationId "
			+ " and schedule.unit_id = :unitId "
			+ " and schedule.status = 'A' "
			),
    @NamedNativeQuery(name = "GET_PROCEDURE_ORDER_LIST_BY_PROREQ_ID",
	 query = "select distinct chekmstr.checklist_master_id as \"checklistId\","
	 		  + " chekmstr.description as \"description\", "
	 		  + " chekmstr.organization_id as \"organizationId\","
	 		  + " chekmstr.unit_id as \"unitId\","
	 		  + " chekmstr.stage_id as \"stageId\","
	 		  + " chekmstr.role_id as \"roleId\"  "
			  + " from otims.t_procedure_order_checklist prochek  "
			  + " inner join otims.t_procedure_order_master pom on  prochek.procedure_order_id = pom.procedure_order_id "
			  + " inner join otims.m_checklist_master chekmstr on prochek.checklist_id = chekmstr.checklist_master_id"
			  + " where  prochek.procedure_request_id = :orderId and pom.status = 'A' "),
			 // + " AND chekmstr.role_id in (8,9,10,11,12) "
    
    @NamedNativeQuery(name="GET_PROCEDURE_SERVICE_LIST_BY_SERVICENAME_OR_CODE", 
	query="select service.service_master_id as \"serviceMasterId\", "
			+ "service.service_standard_name as \"serviceStandardName\" ,"
			+ "service.service_standard_code as \"serviceStandardCode\", "
            + "procedure_config.cpt_code as \"cptCode\", "
			+ "CAST ('0' AS DOUBLE PRECISION) as \"concession\", "
			+ " speciality.speciality_id as \"specialityId\", "
			+ " speciality.speciality_name as \"specialityName\","
			+ " sub_speciality.sub_speciality_id as \"subSpecialityId\", "
			+ " sub_speciality.sub_speciality_name as \"subSpecialityName\", "
			+ " details.is_rate_editable as \"isRateEditable\","
			+ " details.rate_editble_min_value as \"minRateEditable\", "
			+ " details.rate_editble_min_value as \"maxRateEditable\", "
			+ " details.is_tax_applicable as \"isTaxApplicable\","
			+ " details.is_discount as \"isDiscountApplicable\","
			+ " details.discount_value as \"discount\", "
			+ " details.gst_type_id as \"taxId\", "
			+ " details.is_ref_doctor_share as \"isRefDoctorShare\", " 
			+ " details.is_doc_req as \"isDoctorRequired\", "
			+ " tax.name as \"taxName\", "
			+ " tax.tax_percentage as \"taxPercentage\", "
			+ " details.is_allow_multiple_quantity as \"isQuantityEditable\", "
			+ " procedure_config.duration as \"procedureDuration\","
			+ " details.procedure_type_id as \"procedureTypeId\","
			+ " procedure_config.procedure_config_id as \"procedureConfigurationId\" "
			+ " FROM service.t_unit_service_mapper mapper " 
			+ " inner join service.m_service_master service on service.service_master_id  = mapper.service_id"
			+ " inner join service.t_unit_service_master_details details on details.service_master_id = service.service_master_id"
			+ " inner JOIN service.m_service_base_price_master bpm ON bpm.service_master_id = mapper.service_id "
			+ " inner join doctor.m_speciality_master speciality on service.speciality_id = speciality.speciality_id"
			+ " inner join doctor.m_sub_speciality_master sub_speciality on service.sub_speciality_id = sub_speciality.sub_speciality_id "
			+ " left join otims.t_procedure_configuration procedure_config on service.service_master_id = procedure_config.service_id "
			+ " left join public.m_tax tax on tax.id=details.gst_type_id " 
			+ " WHERE mapper.unit_id = details.unit_id "
			+ " AND bpm.unit_id = mapper.unit_id "
			+ " AND mapper.unit_id =:unitId "
			+ " AND mapper.orgnisation_id=:orgId "
			+ " AND service.status='A' " 
			+ " AND details.is_procedure = 'Y'"
			+ " AND details.procedure_type_id is not null "
			+ " AND(lower(service.service_standard_name )like :searchKeyword OR lower(service.service_standard_code) like :searchKeyword)"),

    
})
@Entity
@Table(name = "t_procedure_order_master" , schema = "otims")
@SequenceGenerator(name = "t_procedure_order_master_seq" , sequenceName = "otims.t_procedure_order_master_seq" , allocationSize = 1)
public class ProcedureOrderMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_procedure_order_master_seq")
	@Column(name = "procedure_order_id")
	private Integer procedureOrderId;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "prefered_date_time")
	private Date preferedDateTime;
	
	@Column(name = "prefered_time")
	@Convert(converter = InstantConverter.class)
	private Instant preferedTime;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "is_primary_secondary")
	private char isPrimarySecondary;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "amount")
	private double amount;
	
	@Column(name = "urgency")
	private Integer urgency;
	
	@Column(name = "request_reason")
	private String requestReason;
	
	@Column(name = "is_icu_required")
	private char isIcuRequired;
	
	@Column(name = "is_blood_component_required")
	private char isBloodComponentRequired;
	
	@Column(name = "is_pac_required")
	private char isPacRequired;
	
	@Column(name = "pro_req_id")
	private Integer proReqId;
	
	@Column(name = "reason_for_request")
	private String reasonForRequest;
	
	@Column(name = "exported_to_otims")
	private Boolean exportedToOtims;
	
	@Column(name = "emergency")
	private boolean emergency;
	
	@Column(name = "reason_id")
	private Integer reasonId ;
	
	@Column(name = "reason_note")
	private String reasonNote ;
	
	@Column(name = "procedure_sequence")
	private Integer procedureSequence;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "procedureOrderMaster", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<BloodComponentOrderMaster> listBloodComponentOrderMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="service_id", insertable=false, updatable=false)
	private ServiceMaster serviceMaster;
	
	public String getReasonForRequest() {
		return reasonForRequest;
	}

	public void setReasonForRequest(String reasonForRequest) {
		this.reasonForRequest = reasonForRequest;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Date getPreferedDateTime() {
		return preferedDateTime;
	}

	public void setPreferedDateTime(Date preferedDateTime) {
		this.preferedDateTime = preferedDateTime;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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

	public char getIsPrimarySecondary() {
		return isPrimarySecondary;
	}

	public void setIsPrimarySecondary(char isPrimarySecondary) {
		this.isPrimarySecondary = (isPrimarySecondary != '\u0000' ? isPrimarySecondary : 'N');
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Integer getUrgency() {
		return urgency;
	}

	public void setUrgency(Integer urgency) {
		this.urgency = urgency;
	}

	public String getRequestReason() {
		return requestReason;
	}

	public void setRequestReason(String requestReason) {
		this.requestReason = requestReason;
	}

	public char getIsIcuRequired() {
		return isIcuRequired;
	}

	public void setIsIcuRequired(char isIcuRequired) {
		this.isIcuRequired = (isIcuRequired != '\u0000' ? isIcuRequired : 'N');
	}

	public char getIsBloodComponentRequired() {
		return isBloodComponentRequired;
	}

	public void setIsBloodComponentRequired(char isBloodComponentRequired) {
		this.isBloodComponentRequired = (isBloodComponentRequired != '\u0000' ? isBloodComponentRequired : 'N');
	}

	public char getIsPacRequired() {
		return isPacRequired;
	}

	public void setIsPacRequired(char isPacRequired) {
		this.isPacRequired = (isPacRequired != '\u0000' ? isPacRequired : 'N');
	}

	public Instant getPreferedTime() {
		return preferedTime;
	}

	public void setPreferedTime(Instant preferedTime) {
		this.preferedTime = preferedTime;
	}

	public Boolean getExportedToOtims() {
		return exportedToOtims;
	}

	public void setExportedToOtims(Boolean exportedToOtims) {
		this.exportedToOtims = exportedToOtims;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public boolean getEmergency() {
		return emergency;
	}

	public void setEmergency(boolean emergency) {
		this.emergency = emergency;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public String getReasonNote() {
		return reasonNote;
	}

	public void setReasonNote(String reasonNote) {
		this.reasonNote = reasonNote;
	}

	public Integer getProcedureSequence() {
		return procedureSequence;
	}

	public void setProcedureSequence(Integer procedureSequence) {
		this.procedureSequence = procedureSequence;
	}
	
	
}

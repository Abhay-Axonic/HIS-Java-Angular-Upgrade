package com.param.adt.transfer.model;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.admission.model.Admission;
import com.param.adt.master.global.model.BedMaster;
import com.param.adt.master.global.model.OrganizationMaster;
import com.param.adt.master.global.model.UnitMaster;
import com.param.adt.master.unit.model.BillingBedCategoryMaster;
import com.param.adt.master.unit.model.RoomMaster;
import com.param.adt.master.unit.model.WardMaster;
import com.param.global.model.BedCategoryMaster;

@NamedNativeQueries({
	
	@NamedNativeQuery(name = "GET_TRANSFER_REQ_LIST", 
			query = 
			"SELECT admission.admission_id as \"admissionId\", "
			+ "transfer.transfer_id as \"transferId\", "
			+ "traReq.transfer_request_id as \"transferRequestId\", "
			+ "adl.admission_details_id as \"admissionDetailsId\", "
			+ "admission.patient_id as \"patientId\", "
			+ "admission.t_patient_id as \"tPatientId\", "
			+ "admission.doctor_id as \"doctorId\", "
			+ "admission.visit_type_id as \"visitTypeId\", " 
			+ "pr.uhid_number as \"UHIDNumber\", " 
			+ "concat(COALESCE(pr.first_name,''),' ',COALESCE(pr.middle_name,''),' ',COALESCE(pr.last_name,'')) as \"pFirstName\" , " 
			+ "pr.gender_id as \"genderId\", " 
			+ "gm.gender_code as \"genderCode\", "
			+ "to_char(pr.dob,'MM/dd/yyyy') as \"dob\", " 
			+ "bcm1.bed_category_desc as \"bedCategoryDesc\", "
			+ "bcm2.bed_category_desc as \"toBedCategoryDesc\", " 
			+ "bm.bed_number as \"bedNumber\", "
			+ "bm.bed_category_id as \"bedCategoryId\", "
			+ "adl.ward_id as \"wardId\", "
			+ "ward.ward_name as \"wardName\", "
			+ " transfer.transfer_status_id as \"transferStatusId\", "
			+ "tsm.transfer_status_desc as \"transferStatusDesc\", "
			+ "bcm.hierarchy_id as \"hierarchyId\", "
			+ " traReq.to_bed_category_id as \"toBedCategoryId\", "
			+ " traReq.reason_id as \"reasonId\", "
			+ "reason.reason_desc as \"reasonDesc\", "
			+ " bcm2.hierarchy_id as \"toHierarchyId\", "
			+ " traReq.note as \"note\","
			+ "transfer.to_bed_id as \"toBedId\","
			+ "transfer.from_bed_id as \"fromBedId\", "
			+ "transfer.from_ward_id as \"fromWardId\", "
			+ "transfer.from_room_id as \"fromRoomId\", "
			+ " bm.billing_bed_category_id as \"fromBillingBedCategoryId\", "
			+ "transfer.to_room_id as \"toRoomId\", "
			+ "bm2.billing_bed_category_id as \"billingBedCategoryId\", "
			+ "bm2.floor_id as \"toFloorId\", "
			+ "bm.floor_id as \"fromFloorId\", "
			+ "transfer.to_ward_id as \"toWardId\","
			+ "bm2.bed_number as \"toBedNumber\","
			+ "ward2.ward_name as \"toWardname\", "
			+ "traReq.doctor_reason_id as \"doctorReasonId\", "
			+ "reason2.reason_desc as \"doctorReasonDesc\", "
			+ "traReq.adt_reason_id as \"adtReasonId\", "
			+ "reason3.reason_desc as \"adtReasonDesc\", "
			+ "traReq.doctor_note as \"doctorNote\", "
			+ "traReq.adt_note as \"adtNote\", "
			+ "traReq.doctor_reject_reason as \"doctorRejectReason\", "
			+ "traReq.adt_reject_reason as \"adtRejectReason\","
			+ "traReq.treating_doctor_id  as \"treatingDoctorId\", "
			+" concat('Dr. ',doc.first_name,' ',doc.middle_name,' ',doc.last_name) as \"doctorName\", "
			+ "traReq.transfer_type_id as \"transferTypeId\" ,"
		    +" prefxmstr.prefix_code as \"prefixDesc\",pr.image_path as \"imagePath\" ,"
            + " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\" "
			+ "FROM adt.t_admission admission " 
			+ "INNER JOIN patient.t_patient_registration pr on admission.patient_id=pr.patient_id "
			+ "INNER JOIN adt.t_transfer transfer on transfer.admission_id=admission.admission_id "
			+ "INNER JOIN adt.m_transfer_status_master tsm on tsm.transfer_status_id=transfer.transfer_status_id "
			+ "LEFT JOIN adt.t_transfer_request traReq on traReq.transfer_request_id=transfer.transfer_request_id "
            + " left join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
			+ "INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id " 
			+ "INNER JOIN adt.t_admission_details adl on adl.admission_id=admission.admission_id "
			+ "LEFT JOIN adt.m_bed_master bm on transfer.from_bed_id=bm.bed_id "
			+ "INNER JOIN adt.m_ward_master ward on transfer.from_ward_id=ward.ward_id " 
			+ "INNER JOIN adt.m_bed_category_master bcm on adl.bed_category_id=bcm.bed_category_id "
			+ "INNER JOIN doctor.m_doctor_master doc on traReq.treating_doctor_id=doc.doctor_id "
			+ "LEFT JOIN adt.m_ward_master ward2 on transfer.to_ward_id=ward2.ward_id "
			+ "LEFT JOIN adt.m_bed_master bm2 on transfer.to_bed_id=bm2.bed_id "
			+" LEFT JOIN adt.m_bed_category_master bcm1 on transfer.from_bed_category_id=bcm1.bed_category_id  "
			+" LEFT JOIN adt.m_bed_category_master bcm2 on transfer.to_bed_category_id=bcm2.bed_category_id  "
			+ "LEFT JOIN adt.m_reason_master reason on traReq.reason_id =reason.reason_master_id "
			+ "LEFT JOIN adt.m_reason_master reason2 on traReq.doctor_reason_id =reason2.reason_master_id "
			+ "LEFT JOIN adt.m_reason_master reason3 on traReq.adt_reason_id =reason3.reason_master_id "
			+ " WHERE admission.status='A' "
			+ " AND adl.status='A' "
			//+ " AND transfer.status='A' "
			+ " AND adl.nursing_acceptance = 'Y' "
			+ "AND admission.organization_id=:organizationId " 
			+ "AND admission.unit_id=:unitId "
			+ " AND traReq.transfer_type_id IN (1,2) "
			+ "AND transfer.from_ward_id IN :wards "),
	
	@NamedNativeQuery(name = "GET_TRANSFER_REQ_LIST_BY_ADMISSION_ID_FOR_B2B", 
	query = 
	"SELECT admission.admission_id as \"admissionId\", "
	+ "transfer.transfer_id as \"transferId\", "
	+ "traReq.transfer_request_id as \"transferRequestId\", "
	+ "adl.admission_details_id as \"admissionDetailsId\", "
	+ "admission.patient_id as \"patientId\", "
	+ "admission.t_patient_id as \"tPatientId\", "
	+ "admission.doctor_id as \"doctorId\", "
	+ "admission.visit_type_id as \"visitTypeId\", " 
	+ "pr.uhid_number as \"UHIDNumber\", " 
	+ "concat(COALESCE(pr.first_name,''),' ',COALESCE(pr.middle_name,''),' ',COALESCE(pr.last_name,'')) as \"pFirstName\" , " 
	+ "pr.gender_id as \"genderId\", " 
	+ "gm.gender_code as \"genderCode\", "
	+ "to_char(pr.dob,'MM/dd/yyyy') as \"dob\", " 
	+ "bcm1.bed_category_desc as \"bedCategoryDesc\", "
	+ "bcm2.bed_category_desc as \"toBedCategoryDesc\", " 
	+ "bm.bed_number as \"bedNumber\", "
	+ "bm.bed_category_id as \"bedCategoryId\", "
	+ "adl.ward_id as \"wardId\", "
	+ "ward.ward_name as \"wardName\", "
	+ " transfer.transfer_status_id as \"transferStatusId\", "
	+ "tsm.transfer_status_desc as \"transferStatusDesc\", "
	+ "bcm.hierarchy_id as \"hierarchyId\", "
	+ " traReq.to_bed_category_id as \"toBedCategoryId\", "
	+ " traReq.reason_id as \"reasonId\", "
	+ "reason.reason_desc as \"reasonDesc\", "
	+ " bcm2.hierarchy_id as \"toHierarchyId\", "
	+ " traReq.note as \"note\","
	+ "transfer.to_bed_id as \"toBedId\","
	+ "transfer.from_bed_id as \"fromBedId\", "
	+ "transfer.to_room_id as \"toRoomId\", "
	+ "bm2.billing_bed_category_id as \"billingBedCategoryId\", "
	+ "bm2.floor_id as \"toFloorId\", "
	+ "bm.floor_id as \"fromFloorId\", "
	+ "transfer.to_ward_id as \"toWardId\","
	+ "bm2.bed_number as \"toBedNumber\","
	+ "ward2.ward_name as \"toWardname\", "
	+ "traReq.doctor_reason_id as \"doctorReasonId\", "
	+ "reason2.reason_desc as \"doctorReasonDesc\", "
	+ "traReq.adt_reason_id as \"adtReasonId\", "
	+ "reason3.reason_desc as \"adtReasonDesc\", "
	+ "traReq.doctor_note as \"doctorNote\", "
	+ "traReq.adt_note as \"adtNote\", "
	+ "prifix.prefix_code as \"prefixDesc\", "
	+ "traReq.doctor_reject_reason as \"doctorRejectReason\", "
	+ "traReq.adt_reject_reason as \"adtRejectReason\","
	+ "traReq.treating_doctor_id  as \"treatingDoctorId\", "
	+" concat('Dr. ',COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"doctorName\", "
	+ "traReq.transfer_type_id as \"transferTypeId\" "
	+ "FROM adt.t_admission admission " 
	+ "INNER JOIN patient.t_patient_registration pr on admission.patient_id=pr.patient_id "
	+ "INNER JOIN public.m_prefix_master prifix on prifix.prefix_id=pr.prefix_id "
	+ "INNER JOIN adt.t_transfer transfer on transfer.admission_id=admission.admission_id "
	+ "INNER JOIN adt.m_transfer_status_master tsm on tsm.transfer_status_id=transfer.transfer_status_id "
	+ "LEFT JOIN adt.t_transfer_request traReq on traReq.transfer_request_id=transfer.transfer_request_id "
	+ "INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id " 
	+ "INNER JOIN adt.t_admission_details adl on adl.admission_id=admission.admission_id "
	+ "LEFT JOIN adt.m_bed_master bm on transfer.from_bed_id=bm.bed_id "
	+ "INNER JOIN adt.m_ward_master ward on transfer.from_ward_id=ward.ward_id " 
	+ "INNER JOIN adt.m_bed_category_master bcm on adl.bed_category_id=bcm.bed_category_id "
	+ "INNER JOIN doctor.m_doctor_master doc on traReq.treating_doctor_id=doc.doctor_id "
	+ "LEFT JOIN adt.m_ward_master ward2 on transfer.to_ward_id=ward2.ward_id "
	+ "LEFT JOIN adt.m_bed_master bm2 on transfer.to_bed_id=bm2.bed_id "
	+" LEFT JOIN adt.m_bed_category_master bcm1 on transfer.from_bed_category_id=bcm1.bed_category_id  "
	+" LEFT JOIN adt.m_bed_category_master bcm2 on transfer.to_bed_category_id=bcm2.bed_category_id  "
	+ "LEFT JOIN adt.m_reason_master reason on traReq.reason_id =reason.reason_master_id "
	+ "LEFT JOIN adt.m_reason_master reason2 on traReq.doctor_reason_id =reason2.reason_master_id "
	+ "LEFT JOIN adt.m_reason_master reason3 on traReq.adt_reason_id =reason3.reason_master_id "
	+ " WHERE admission.status='A' "
	+ " AND adl.status='A' "
	+ " AND adl.nursing_acceptance = 'Y' "
	+ " AND transfer.status='A' "
	+ "AND admission.organization_id=:organizationId " 
	+ "AND admission.unit_id=:unitId "
	+ " AND traReq.transfer_type_id IN (1,2) "
	+ "AND admission.admission_id =:admissionId "),
	
	@NamedNativeQuery(name = "GET_INITIATE_TRANSFER_REQ_LIST", 
	query = 
	"SELECT admission.admission_id as \"admissionId\", "
	+ "transfer.transfer_id as \"transferId\", "
	+ "adl.admission_details_id as \"admissionDetailsId\", "
	+ "admission.patient_id as \"patientId\", "
	+ "admission.t_patient_id as \"tPatientId\", "
	+ "admission.doctor_id as \"doctorId\", "
	+ "admission.visit_type_id as \"visitTypeId\", " 
	+ "pr.uhid_number as \"UHIDNumber\", " 
	+ "concat(coalesce(pr.first_name),' ',coalesce(pr.middle_name),' ',coalesce(pr.last_name)) as \"pFirstName\" , " 
	+ "pr.gender_id as \"genderId\", " 
	+ "gm.gender_code as \"genderCode\", "
	+ "to_char(pr.dob,'MM/dd/yyyy') as \"dob\", " 
	+ "bcm1.bed_category_desc as \"bedCategoryDesc\", "
	+ "bm.bed_number as \"bedNumber\", "
	+ "bm.bed_category_id as \"bedCategoryId\", "
	+ "bm.room_id as \"roomId\", " 
	+ "bm.bed_id as \"fromBedId\", "  
	+ "bm.billing_bed_category_id as \"fromBillingBedCategoryId\","
	+" prefxmstr.prefix_code as \"prefixDesc\",pr.image_path as \"imagePath\" ,"
	+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))) as \"ageStr\", "
	+ "adl.ward_id as \"wardId\", "
	+ "ward.ward_name as \"wardName\", "
	+ " transfer.transfer_status_id as \"transferStatusId\", "
	+ "tsm.transfer_status_desc as \"transferStatusDesc\", "
	+ "bcm.hierarchy_id as \"hierarchyId\","
	+ "adl.billing_bed_category_id  as \"toBillingBedCategoryId\" "
	+ "FROM adt.t_admission admission " 
	+ "INNER JOIN patient.t_patient_registration pr on admission.patient_id=pr.patient_id "
	+ "INNER JOIN adt.t_transfer transfer on transfer.admission_id=admission.admission_id "
	+ "INNER JOIN adt.m_transfer_status_master tsm on tsm.transfer_status_id=transfer.transfer_status_id "
	+ "INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id " 
	+ "INNER JOIN adt.t_admission_details adl on adl.admission_id=admission.admission_id "
	+ "LEFT JOIN adt.m_bed_master bm on transfer.from_bed_id=bm.bed_id "
	+ "INNER JOIN adt.m_ward_master ward on transfer.from_ward_id=ward.ward_id " 
	+ "INNER JOIN adt.m_bed_category_master bcm on adl.bed_category_id=bcm.bed_category_id "
	+" LEFT JOIN adt.m_bed_category_master bcm1 on transfer.from_bed_category_id=bcm1.bed_category_id  "
    + " left join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
	+ " WHERE admission.status='A' "
	+ " AND adl.status='A' "
	+ " AND adl.nursing_acceptance = 'Y' "
	+ " AND transfer.status='A' "
	+ "AND admission.organization_id=:organizationId " 
	+ "AND admission.unit_id=:unitId "
	+ "AND transfer.transfer_status_id=1 "
	+ "AND transfer.from_ward_id IN :wards "),

	@NamedNativeQuery(name = "GET_INITIATE_TRANSFER_REQ_COUNT",
	query =
			"SELECT count(*) "
					+ "FROM adt.t_admission admission "
					+ "INNER JOIN patient.t_patient_registration pr on admission.patient_id=pr.patient_id "
					+ "INNER JOIN adt.t_transfer transfer on transfer.admission_id=admission.admission_id "
					+ "INNER JOIN adt.m_transfer_status_master tsm on tsm.transfer_status_id=transfer.transfer_status_id "
					+ "INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id "
					+ "INNER JOIN adt.t_admission_details adl on adl.admission_id=admission.admission_id "
					+ "LEFT JOIN adt.m_bed_master bm on transfer.from_bed_id=bm.bed_id "
					+ "INNER JOIN adt.m_ward_master ward on transfer.from_ward_id=ward.ward_id "
					+ "INNER JOIN adt.m_bed_category_master bcm on adl.bed_category_id=bcm.bed_category_id "
					+" LEFT JOIN adt.m_bed_category_master bcm1 on transfer.from_bed_category_id=bcm1.bed_category_id  "
					+ " left join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
					+ " WHERE admission.status='A' "
					+ " AND adl.status='A' "
					+ " AND adl.nursing_acceptance = 'Y' "
					+ " AND transfer.status='A' "
					+ "AND admission.organization_id=:organizationId "
					+ "AND admission.unit_id=:unitId "
					+ "AND transfer.transfer_status_id=1 "
					+ "AND transfer.from_ward_id IN :wards "),
	
	@NamedNativeQuery(name = "GET_TRANSFER_REQ_LIST_BY_ADMISSION_ID", 
	query = 
	"SELECT admission.admission_id as \"admissionId\", "
	+ "transfer.transfer_id as \"transferId\", "
	+ "adl.admission_details_id as \"admissionDetailsId\", "
	+ "admission.patient_id as \"patientId\", "
	+ "admission.t_patient_id as \"tPatientId\", "
	+ "admission.doctor_id as \"doctorId\", "
	+ "admission.visit_type_id as \"visitTypeId\", " 
	+ "pr.uhid_number as \"UHIDNumber\", " 
	+ "concat(coalesce(pr.first_name),' ',coalesce(pr.middle_name),' ',coalesce(pr.last_name)) as \"pFirstName\" , " 
	+ "pr.gender_id as \"genderId\", " 
	+ "gm.gender_code as \"genderCode\", "
	+ "to_char(pr.dob,'MM/dd/yyyy') as \"dob\", " 
	+ "bcm1.bed_category_desc as \"bedCategoryDesc\", "
	+ "bm.bed_number as \"bedNumber\", "
	+ "bm.bed_category_id as \"bedCategoryId\", "
	+ "bm.room_id as \"roomId\", " 
	+ "bm.bed_id as \"fromBedId\", "  
	+ "bm.billing_bed_category_id as \"fromBillingBedCategoryId\","
	+" prefxmstr.prefix_code as \"prefixDesc\",pr.image_path as \"imagePath\" ,"
	+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))) as \"ageStr\", "
	+ "adl.ward_id as \"wardId\", "
	+ "ward.ward_name as \"wardName\", "
	+ " transfer.transfer_status_id as \"transferStatusId\", "
	+ "tsm.transfer_status_desc as \"transferStatusDesc\", "
	+ "bcm.hierarchy_id as \"hierarchyId\" "
	+ "FROM adt.t_admission admission " 
	+ "INNER JOIN patient.t_patient_registration pr on admission.patient_id=pr.patient_id "
	+ "INNER JOIN adt.t_transfer transfer on transfer.admission_id=admission.admission_id "
	+ "INNER JOIN adt.m_transfer_status_master tsm on tsm.transfer_status_id=transfer.transfer_status_id "
	+ "INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id " 
	+ "INNER JOIN adt.t_admission_details adl on adl.admission_id=admission.admission_id "
	+ "LEFT JOIN adt.m_bed_master bm on transfer.from_bed_id=bm.bed_id "
	+ "INNER JOIN adt.m_ward_master ward on transfer.from_ward_id=ward.ward_id " 
	+ "INNER JOIN adt.m_bed_category_master bcm on adl.bed_category_id=bcm.bed_category_id "
	+" LEFT JOIN adt.m_bed_category_master bcm1 on transfer.from_bed_category_id=bcm1.bed_category_id  "
    + " left join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
	+ " WHERE admission.status='A' "
	+ " AND adl.status='A' "
	+ " AND adl.nursing_acceptance = 'Y' "
	+ " AND transfer.status='A' "
	+ "AND transfer.transfer_status_id=1 "
	+ "AND admission.organization_id=:organizationId " 
	+ "AND admission.unit_id=:unitId "
	+ " AND admission.admission_id=:admissionId "),
	
	
	@NamedNativeQuery(name="GET_PENDING_TRANSFER_REQ_LIST_FOR_DOCTOR",
	query="SELECT transfer.transfer_id as \"transferId\", "
			+" transfer.transfer_request_id as \"transferRequestId\", "
			+ "admission.patient_id as \"patientId\", "
			+ "admission.t_patient_id as \"tPatientId\", "
			+ "transfer.from_bed_category_id as \"fromBedCategoryId\", "
			+ "bcm1.bed_category_desc as \"fromBedCategoryDesc\", "
			+ "transfer.to_bed_category_id as \"toBedCategoryId\", "
			+ "bcm2.bed_category_desc as \"toBedCategoryDesc\", "
			+" pr.uhid_number as \"UHIDNumber\", "
			+" concat(coalesce(prifix.prefix_code,''),' ',coalesce(pr.first_name),' ',coalesce(pr.middle_name),' ',coalesce(pr.last_name)) as \"patientName\",  " 
			+" pr.gender_id as \"genderId\",   "
			+" gm.gender_code as \"genderCode\",  "
			+" to_char(pr.dob,'MM/dd/yyyy') as \"dob\",   "
			+" transfer.from_ward_id as \"fromWardId\", "
			+" ward.ward_name as \"wardName\", "
			+" bm.floor_id as \"floorId\", "
			+" floor.floor_name as \"floorName\", "
			+" transfer.from_bed_id as \"fromBedId\", " 
			+" bm.bed_number as \"bedNumber\", "
			+" traReq.treating_doctor_id as \"doctorId\", "
			+" concat('Dr. ',doc.first_name,' ',doc.middle_name,' ',doc.last_name) as \"doctorName\", "
			+" traReq.reason_id as \"reasonId\",  "
			+" reason.reason_desc as \"reasonDesc\", "
			+" traReq.note as \"note\", "
			+" traReq.doctor_note as \"doctorNote\","
			+ "traReq.transfer_type_id as \"transferTypeId\", "
			+ "admission.admission_id as \"admissionId\" "
			+" FROM adt.t_transfer transfer "
			+" INNER JOIN adt.t_admission admission on admission.admission_id=transfer.admission_id   "
			+" INNER JOIN patient.t_patient_registration pr on admission.patient_id=pr.patient_id  "
			+" INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id   "
			+ "INNER JOIN public.m_prefix_master prifix on prifix.prefix_id=pr.prefix_id "
			+" LEFT JOIN adt.m_bed_master bm on transfer.from_bed_id=bm.bed_id   "
			+" LEFT JOIN adt.m_floor_master floor on bm.floor_id=floor.floor_id  "
			+" INNER JOIN adt.m_bed_category_master bcm1 on transfer.from_bed_category_id=bcm1.bed_category_id  "
			+" LEFT JOIN adt.m_bed_category_master bcm2 on transfer.to_bed_category_id=bcm2.bed_category_id  "
			+" INNER JOIN adt.m_ward_master ward on transfer.from_ward_id=ward.ward_id "
			+" INNER JOIN adt.t_transfer_request traReq on transfer.transfer_request_id=traReq.transfer_request_id  "
			+" INNER JOIN doctor.m_doctor_master doc on traReq.treating_doctor_id=doc.doctor_id   "
			+" INNER JOIN adt.m_reason_master reason on traReq.reason_id=reason.reason_master_id "
			+" WHERE transfer.transfer_status_id=3 "
			+ "AND traReq.transfer_status_id=3 "
			+ "AND transfer.status='A' "
			+ " AND admission.status='A' "
			+ "AND transfer.organization_id=:organizationId "
			+ "AND transfer.unit_id=:unitId "
			+ "AND traReq.treating_doctor_id=:docId "),
	
	@NamedNativeQuery(name="GET_PENDING_TRANSFER_REQ_LIST_FOR_ADT",
	query="SELECT transfer.transfer_id as \"transferId\", "
			+" transfer.transfer_request_id as \"transferRequestId\", "
			+ "admission.patient_id as \"patientId\", "
			+ "admission.t_patient_id as \"tPatientId\", "
			+ "transfer.from_bed_category_id as \"fromBedCategoryId\", "
			+ "transfer.from_billing_bed_category_id as \"fromBillingBedCategoryId\", "
			+ "bcm1.bed_category_desc as \"fromBedCategoryDesc\", "
			+ "transfer.to_bed_category_id as \"toBedCategoryId\", "
			+ "bcm2.bed_category_desc as \"toBedCategoryDesc\", "
			+" pr.uhid_number as \"UHIDNumber\", "
			+" concat(coalesce(pr.first_name),' ',coalesce(pr.middle_name),' ',coalesce(pr.last_name)) as \"patientName\",  " 
			+" pr.gender_id as \"genderId\",   "
			+" gm.gender_code as \"genderCode\",  "
			+" to_char(pr.dob,'MM/dd/yyyy') as \"dob\",   "
			+" transfer.from_ward_id as \"fromWardId\", "
			+" transfer.from_room_id as \"fromRoomId\", "
			+" transfer.admission_id as \"admissionId\", "
			+" ward.ward_name as \"wardName\", "
			+" bm.floor_id as \"floorId\", "
			+" floor.floor_name as \"floorName\", "
			+" transfer.from_bed_id as \"fromBedId\", "
			+" transfer.to_bed_id as \"toBedId\", " 
			+" bm.bed_number as \"bedNumber\", "
			+" traReq.treating_doctor_id as \"doctorId\", "
			+" concat('Dr. ',COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"doctorName\", "
			+" traReq.reason_id as \"reasonId\",  "
			+" reason.reason_desc as \"reasonDesc\", "
			+" traReq.note as \"note\", "
			+" traReq.doctor_note as \"doctorNote\", "
			+" to_char(traReq.created_date,'MM/dd/yyyy') as \"createdDate\",   "
			+ "traReq.transfer_type_id as \"transferTypeId\", "
            +" prefxmstr.prefix_code as \"prefixDesc\",pr.image_path as \"imagePath\" ,"
            + " transfer.to_billing_bed_category_id as \"toBillingBedCategoryId\", "
            + " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\" "

			+" FROM adt.t_transfer transfer "
			+" INNER JOIN adt.t_admission admission on admission.admission_id=transfer.admission_id   "
			+" INNER JOIN patient.t_patient_registration pr on admission.patient_id=pr.patient_id  "
			+" INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id   "
			+" LEFT JOIN adt.m_bed_master bm on transfer.from_bed_id=bm.bed_id   "
			+" LEFT JOIN adt.m_floor_master floor on bm.floor_id=floor.floor_id  "
			+" INNER JOIN adt.m_bed_category_master bcm1 on transfer.from_bed_category_id=bcm1.bed_category_id  "
			+" INNER JOIN adt.m_bed_category_master bcm2 on transfer.to_bed_category_id=bcm2.bed_category_id  "
			+" INNER JOIN adt.m_ward_master ward on transfer.from_ward_id=ward.ward_id "
			+" INNER JOIN adt.t_transfer_request traReq on transfer.transfer_request_id=traReq.transfer_request_id  "
			+" INNER JOIN doctor.m_doctor_master doc on traReq.treating_doctor_id=doc.doctor_id   "
			+" INNER JOIN adt.m_reason_master reason on traReq.reason_id=reason.reason_master_id "
            + " left join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "

			+" WHERE transfer.transfer_status_id=4 "
			+ "AND traReq.transfer_status_id=4 "
			+ "AND transfer.status='A' "
			+ " AND admission.status='A' "
			+ "AND transfer.organization_id=:organizationId "
			+ "AND transfer.unit_id=:unitId "),
	

	@NamedNativeQuery(name="GET_PENDING_TRANSFER_REQ_LIST_FOR_LABOR_BED",
	query="SELECT transfer.transfer_id as \"transferId\", "
			+ " adddetail.admission_details_id as \"admissionDetailsId\","
			+" transfer.transfer_request_id as \"transferRequestId\", "
			+ "admission.patient_id as \"patientId\", "
			+ "admission.t_patient_id as \"tPatientId\", "
			+ "transfer.from_bed_category_id as \"fromBedCategoryId\", "
			+ "transfer.from_billing_bed_category_id as \"fromBillingBedCategoryId\", "
			+ "bcm1.bed_category_desc as \"fromBedCategoryDesc\", "
			+ "transfer.to_bed_category_id as \"toBedCategoryId\", "
			+ "bcm2.bed_category_desc as \"toBedCategoryDesc\", "
			+" pr.uhid_number as \"UHIDNumber\", "
			+" concat(coalesce(pr.first_name),' ',coalesce(pr.middle_name),' ',coalesce(pr.last_name)) as \"patientName\",  " 
			+" pr.gender_id as \"genderId\",   "
			+" gm.gender_code as \"genderCode\", prefix.prefix_code as \"prefixDesc\", pr.image_path as \"imagePath\","
			+" to_char(pr.dob,'MM/dd/yyyy') as \"dob\",   "
			+" transfer.from_ward_id as \"fromWardId\", "
			+" transfer.from_room_id as \"fromRoomId\", "
			+" transfer.admission_id as \"admissionId\", "
			+" ward.ward_name as \"wardName\","
			+ " ward.is_labor_ward as \"islaborWard\","
			+" bm.floor_id as \"floorId\", "
			+" floor.floor_name as \"floorName\", "
			+" transfer.from_bed_id as \"fromBedId\", "
			+" transfer.to_bed_id as \"toBedId\", " 
			+" bm.bed_number as \"bedNumber\", "
			+" traReq.treating_doctor_id as \"doctorId\","
			+ " to_char(traReq.created_date,'DD-MM-YYYY HH24:MI:ss') as \"createdDate\", "
			+" concat('Dr. ',COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"doctorName\", "
			+" traReq.reason_id as \"reasonId\",  "
			+" reason.reason_desc as \"reasonDesc\", "
			+" traReq.note as \"note\", "
			+" traReq.doctor_note as \"doctorNote\", "
			+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\", "
			+ "traReq.transfer_type_id as \"transferTypeId\" "
			+" FROM adt.t_transfer transfer "
			+" INNER JOIN adt.t_admission admission on admission.admission_id=transfer.admission_id "
			+ " INNER JOIN adt.t_admission_details adddetail on adddetail.admission_id=admission.admission_id  "
			+" INNER JOIN patient.t_patient_registration pr on admission.patient_id=pr.patient_id  "
			+" INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id   "
			+ "INNER JOIN  public.m_prefix_master prefix ON  prefix.prefix_id = pr.prefix_id "
			+" LEFT JOIN adt.m_bed_master bm on transfer.from_bed_id=bm.bed_id   "
			+" LEFT JOIN adt.m_floor_master floor on bm.floor_id=floor.floor_id  "
			+" LEFT JOIN adt.m_bed_category_master bcm1 on transfer.from_bed_category_id=bcm1.bed_category_id  "
			+" LEFT JOIN adt.m_bed_category_master bcm2 on transfer.to_bed_category_id=bcm2.bed_category_id  "
			+" INNER JOIN adt.m_ward_master ward on transfer.from_ward_id=ward.ward_id "
			+" INNER JOIN adt.t_transfer_request traReq on transfer.transfer_request_id=traReq.transfer_request_id  "
			+" INNER JOIN doctor.m_doctor_master doc on traReq.treating_doctor_id=doc.doctor_id   "
			+" LEFT JOIN adt.m_reason_master reason on traReq.reason_id=reason.reason_master_id "
			+" WHERE transfer.transfer_status_id=4 "
			+ "AND traReq.transfer_status_id=4 "
			+ "AND transfer.status='A' "
			+ " AND admission.status='A' "
			+ "AND transfer.organization_id=:organizationId "
			+ "AND transfer.unit_id=:unitId  and traReq.transfer_type_id=6 and adddetail.status='A' "),
	
	@NamedNativeQuery(name="GET_PENDING_TRANSFER_REQ_LIST_FOR_HEMODIALYSIS_BED",
	query="SELECT transfer.transfer_id as \"transferId\", "
			+ " adddetail.admission_details_id as \"admissionDetailsId\","
			+" transfer.transfer_request_id as \"transferRequestId\", "
			+ "admission.patient_id as \"patientId\", "
			+ "admission.t_patient_id as \"tPatientId\", "
			+ "transfer.from_bed_category_id as \"fromBedCategoryId\", "
			+ "transfer.from_billing_bed_category_id as \"fromBillingBedCategoryId\", "
			+ "bcm1.bed_category_desc as \"fromBedCategoryDesc\", "
			+ "transfer.to_bed_category_id as \"toBedCategoryId\", "
			+ "bcm2.bed_category_desc as \"toBedCategoryDesc\", "
			+" pr.uhid_number as \"UHIDNumber\", "
			+" concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name),' ',coalesce(pr.middle_name),' ',coalesce(pr.last_name)) as \"patientName\",  " 
			+" pr.gender_id as \"genderId\",   "
			+" gm.gender_code as \"genderCode\",  "
			+" to_char(pr.dob,'MM/dd/yyyy') as \"dob\",   "
			+" transfer.from_ward_id as \"fromWardId\", "
			+" transfer.from_room_id as \"fromRoomId\", "
			+" transfer.admission_id as \"admissionId\", "
			+" ward.ward_name as \"wardName\","
			+ " ward.is_labor_ward as \"islaborWard\","
			+" bm.floor_id as \"floorId\", "
			+" floor.floor_name as \"floorName\", "
			+" transfer.from_bed_id as \"fromBedId\", "
			+" transfer.to_bed_id as \"toBedId\", " 
			+" bm.bed_number as \"bedNumber\", "
			+" traReq.treating_doctor_id as \"doctorId\","
			+ " to_char(traReq.created_date,'DD-MM-YYYY HH24:MI:ss') as \"createdDate\", "
			+" concat('Dr. ',COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"doctorName\", "
			+" traReq.reason_id as \"reasonId\",  "
			+" reason.reason_desc as \"reasonDesc\", "
			+" traReq.note as \"note\", "
			+" traReq.doctor_note as \"doctorNote\", "
			+ "traReq.transfer_type_id as \"transferTypeId\" "
			+" FROM adt.t_transfer transfer "
			+" INNER JOIN adt.t_admission admission on admission.admission_id=transfer.admission_id "
			+ " INNER JOIN adt.t_admission_details adddetail on adddetail.admission_id=admission.admission_id  "
			+" INNER JOIN patient.t_patient_registration pr on admission.patient_id=pr.patient_id  "
			+" INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id   "
			+ " left join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
			+" LEFT JOIN adt.m_bed_master bm on transfer.from_bed_id=bm.bed_id   "
			+" LEFT JOIN adt.m_floor_master floor on bm.floor_id=floor.floor_id  "
			+" LEFT JOIN adt.m_bed_category_master bcm1 on transfer.from_bed_category_id=bcm1.bed_category_id  "
			+" LEFT JOIN adt.m_bed_category_master bcm2 on transfer.to_bed_category_id=bcm2.bed_category_id  "
			+" INNER JOIN adt.m_ward_master ward on transfer.from_ward_id=ward.ward_id "
			+" INNER JOIN adt.t_transfer_request traReq on transfer.transfer_request_id=traReq.transfer_request_id  "
			+" INNER JOIN doctor.m_doctor_master doc on traReq.treating_doctor_id=doc.doctor_id   "
			+" LEFT JOIN adt.m_reason_master reason on traReq.reason_id=reason.reason_master_id "
			+" WHERE transfer.transfer_status_id=4 "
			+ "AND traReq.transfer_status_id=4 "
			+ "AND transfer.status='A' "
			+ " AND admission.status='A' "
			+ "AND transfer.organization_id=:organizationId "
			+ "AND transfer.unit_id=:unitId  and traReq.transfer_type_id=7 and adddetail.status='A'"),
	
	@NamedNativeQuery(name="GET_FINAL_TRANSFER_REQUEST_LIST",query=
			"SELECT transfer.transfer_id as \"transferId\", "
			+" transfer.transfer_request_id as \"transferRequestId\", "
			+ "admission.admission_id as \"admissionId\", "		
			+ "  admission.visit_type_id as \"visitTypeId\", "
			+ "admission.patient_id as \"patientId\", "
			+ "admission.t_patient_id as \"tPatientId\", "
			+" pr.uhid_number as \"UHIDNumber\", "
			+" concat(coalesce(pr.first_name),' ',coalesce(pr.middle_name),' ',coalesce(pr.last_name)) as \"patientName\",  " 
			+" pr.gender_id as \"genderId\",   "
			+" gm.gender_code as \"genderCode\",  "
			+" to_char(pr.dob,'MM/dd/yyyy') as \"dob\",   "
			+" transfer.from_ward_id as \"fromWardId\", "
			+" ward.ward_name as \"fromWardName\", "
			+" transfer.from_bed_id as \"fromBedId\", " 
			+" bm.bed_number as \"fromBedNumber\", "
			+" transfer.from_bed_category_id as \"fromBedCategoryId\", "
			+" bcm1.bed_category_desc as \"fromBedCategoryDesc\", "
			+" bm.floor_id as \"fromFloorId\", "
			+" bm.room_id as \"fromRoomId\", "
			+" transfer.to_ward_id as \"toWardId\", "
			+" ward2.ward_name as \"toWardName\", "
			+" transfer.to_bed_id as \"toBedId\", " 
			+" bm2.bed_number as \"toBedNumber\", "
			+" transfer.to_bed_category_id as \"toBedCategoryId\", "
			+" bcm2.bed_category_desc as \"toBedCategoryDesc\", "
			+" bm2.floor_id as \"toFloorId\", "
			+" bm2.room_id as \"toRoomId\", "
			+" traReq.treating_doctor_id as \"doctorId\", "
			+" concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"doctorName\", "
			+" traReq.reason_id as \"reasonId\",  "
			+" reason.reason_desc as \"reasonDesc\", "
			+" traReq.note as \"note\", "
			+" traReq.final_note as \"finalNote\","
            + "to_char(traReq.created_date,'DD-MM-YYYY HH24:MI:ss') as \"createdDate\","
			+ "bm.billing_bed_category_id as \"fromBillingBedCategoryId\", "
			+ "bm2.billing_bed_category_id as \"toBillingBedCategoryId\", "
            + " prefxmstr.prefix_code as \"prefixDesc\",pr.image_path as \"imagePath\",  "
//            + " concat(to_char(extract(year from age(current_timestamp, pr.dob)), '999'), 'Y') as \"ageStr\" " 
           + " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\" "

			+" FROM adt.t_transfer transfer "
			+" INNER JOIN adt.t_admission admission on admission.admission_id=transfer.admission_id   "
			+" INNER JOIN patient.t_patient_registration pr on admission.patient_id=pr.patient_id  "
            + " inner join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
			+" INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id   "
			+" LEFT JOIN adt.m_bed_master bm on transfer.from_bed_id=bm.bed_id   "
			+" LEFT JOIN adt.m_bed_master bm2 on transfer.to_bed_id=bm2.bed_id   "
			+" LEFT JOIN adt.m_floor_master floor on bm.floor_id=floor.floor_id  "
			+" INNER JOIN adt.m_bed_category_master bcm1 on transfer.from_bed_category_id=bcm1.bed_category_id  "
			+" INNER JOIN adt.m_bed_category_master bcm2 on transfer.to_bed_category_id=bcm2.bed_category_id  "
			+" INNER JOIN adt.m_ward_master ward on transfer.from_ward_id=ward.ward_id "
			+" INNER JOIN adt.m_ward_master ward2 on transfer.to_ward_id=ward2.ward_id "
			+" INNER JOIN adt.t_transfer_request traReq on transfer.transfer_request_id=traReq.transfer_request_id  "
			+" INNER JOIN doctor.m_doctor_master doc on traReq.treating_doctor_id=doc.doctor_id   "
			+" INNER JOIN adt.m_reason_master reason on traReq.reason_id=reason.reason_master_id "
			+" WHERE transfer.transfer_status_id=7 "
			+" AND traReq.transfer_status_id=7 "
			+" AND transfer.status='A' "
			+" AND admission.status='A' "
			+" AND transfer.organization_id=:organizationId "
			+" AND transfer.unit_id=:unitId "
			//+" AND transfer.from_ward_id IN (:wardList) "),
	        +" AND transfer.to_ward_id IN (:wardList) "),
	
	@NamedNativeQuery(name="GET_FINAL_TRANSFER_REQUEST_LIST_BY_ADMISSION_ID",query=
	"SELECT transfer.transfer_id as \"transferId\", "
	+" transfer.transfer_request_id as \"transferRequestId\", "
	+ "admission.admission_id as \"admissionId\", "		
	+ "admission.patient_id as \"patientId\", "
	+ "admission.t_patient_id as \"tPatientId\", "
	+" pr.uhid_number as \"UHIDNumber\", "
	+" concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name),' ',coalesce(pr.middle_name),' ',coalesce(pr.last_name)) as \"patientName\",  " 
	+" pr.gender_id as \"genderId\",   "
	+" gm.gender_code as \"genderCode\",  "
	+" to_char(pr.dob,'MM/dd/yyyy') as \"dob\",   "
	+" transfer.from_ward_id as \"fromWardId\", "
	+" ward.ward_name as \"fromWardName\", "
	+" transfer.from_bed_id as \"fromBedId\", " 
	+" bm.bed_number as \"fromBedNumber\", "
	+" transfer.from_bed_category_id as \"fromBedCategoryId\", "
	+" bcm1.bed_category_desc as \"fromBedCategoryDesc\", "
	+" bm.floor_id as \"fromFloorId\", "
	+" bm.room_id as \"fromRoomId\", "
	+" transfer.to_ward_id as \"toWardId\", "
	+" ward2.ward_name as \"toWardName\", "
	+" transfer.to_bed_id as \"toBedId\", " 
	+" bm2.bed_number as \"toBedNumber\", "
	+" transfer.to_bed_category_id as \"toBedCategoryId\", "
	+" bcm2.bed_category_desc as \"toBedCategoryDesc\", "
	+" bm2.floor_id as \"toFloorId\", "
	+" bm2.room_id as \"toRoomId\", "
	+" traReq.treating_doctor_id as \"doctorId\", "
	+" concat(doc.first_name,' ',doc.middle_name,' ',doc.last_name) as \"doctorName\", "
	+" traReq.reason_id as \"reasonId\",  "
	+" reason.reason_desc as \"reasonDesc\", "
	+" traReq.note as \"note\", "
	+" traReq.final_note as \"finalNote\","
	+ "bm.billing_bed_category_id as \"fromBillingBedCategoryId\", "
	+ "bm2.billing_bed_category_id as \"toBillingBedCategoryId\" "
	+" FROM adt.t_transfer transfer "
	+" INNER JOIN adt.t_admission admission on admission.admission_id=transfer.admission_id   "
	+" INNER JOIN patient.t_patient_registration pr on admission.patient_id=pr.patient_id  "
	+" INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id   "
	 + " left join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
	+" LEFT JOIN adt.m_bed_master bm on transfer.from_bed_id=bm.bed_id   "
	+" LEFT JOIN adt.m_bed_master bm2 on transfer.to_bed_id=bm2.bed_id   "
	+" LEFT JOIN adt.m_floor_master floor on bm.floor_id=floor.floor_id  "
	+" INNER JOIN adt.m_bed_category_master bcm1 on transfer.from_bed_category_id=bcm1.bed_category_id  "
	+" INNER JOIN adt.m_bed_category_master bcm2 on transfer.to_bed_category_id=bcm2.bed_category_id  "
	+" INNER JOIN adt.m_ward_master ward on transfer.from_ward_id=ward.ward_id "
	+" INNER JOIN adt.m_ward_master ward2 on transfer.to_ward_id=ward2.ward_id "
	+" INNER JOIN adt.t_transfer_request traReq on transfer.transfer_request_id=traReq.transfer_request_id  "
	+" INNER JOIN doctor.m_doctor_master doc on traReq.treating_doctor_id=doc.doctor_id   "
	+" INNER JOIN adt.m_reason_master reason on traReq.reason_id=reason.reason_master_id "
	+" WHERE transfer.transfer_status_id=7 "
	+" AND traReq.transfer_status_id=7 "
	+" AND transfer.status='A' "
	+" AND admission.status='A' "
	+" AND transfer.organization_id=:organizationId "
	+" AND transfer.unit_id=:unitId "
	+ " AND transfer.admission_id=:admissionId " ),
	
	
	@NamedNativeQuery(name="GET_ICU_TRANSFER_REQ_LIST",query=
	"SELECT transfer.transfer_id as \"transferId\", "
	+" transfer.transfer_request_id as \"transferRequestId\", "
	+ "admission.admission_id as \"admissionId\", "
	+ "adl.admission_details_id as \"admissionDetailsId\", "		
	+ "admission.patient_id as \"patientId\", "
	+ "admission.t_patient_id as \"tPatientId\", "
	+" pr.uhid_number as \"UHIDNumber\", "
	+" concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name),' ',coalesce(pr.middle_name),' ',coalesce(pr.last_name)) as \"patientName\",  " 
	+" pr.gender_id as \"genderId\",   "
	+" gm.gender_code as \"genderCode\",  "
	+" to_char(pr.dob,'MM/dd/yyyy') as \"dob\",   "
	+" transfer.from_ward_id as \"fromWardId\", "
	+" ward.ward_name as \"fromWardName\", "
	+" transfer.from_bed_id as \"fromBedId\", " 
	+" bm.bed_number as \"fromBedNumber\", "
	+" transfer.from_bed_category_id as \"fromBedCategoryId\", "
	+" bcm1.bed_category_desc as \"fromBedCategoryDesc\", "
	+" bm.floor_id as \"fromFloorId\", "
	+" bm.room_id as \"fromRoomId\", "
	+" transfer.to_ward_id as \"toWardId\", "
	+" ward2.ward_name as \"toWardName\", "
	+" transfer.to_bed_id as \"toBedId\", " 
	+" bm2.bed_number as \"toBedNumber\", "
	+" transfer.to_bed_category_id as \"toBedCategoryId\", "
	+" bcm2.bed_category_desc as \"toBedCategoryDesc\", "
	+" bm2.floor_id as \"toFloorId\", "
	+" bm2.room_id as \"toRoomId\", "
	+" traReq.treating_doctor_id as \"doctorId\", "
	+" concat(coalesce(doc.first_name),' ',coalesce(doc.middle_name),' ',coalesce(doc.last_name)) as \"doctorName\", "
	+" traReq.reason_id as \"reasonId\",  "
	+" reason.reason_desc as \"reasonDesc\", "
	+" traReq.note as \"note\", "
	+ "traReq.icu_type_id as \"icuTypeId\", "
	+ "icu.icu_type as \"icuType\", "
	+ "traReq.transfer_status_id as \"transferStatusId\", "
	+" traReq.final_note as \"finalNote\", "
	+ "bm.billing_bed_category_id as \"fromBillingBedCategoryId\" "
	//+ "bm2.billing_bed_category_id as \"toBillingBedCategoryId\" "
	+" FROM adt.t_transfer transfer "
	+" INNER JOIN adt.t_admission admission on admission.admission_id=transfer.admission_id "
	+ "INNER JOIN adt.t_admission_details adl on admission.admission_id=adl.admission_id "
	+" INNER JOIN patient.t_patient_registration pr on admission.patient_id=pr.patient_id  "
	+" INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id   "
	 + " left join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
	+" INNER JOIN adt.m_bed_master bm on transfer.from_bed_id=bm.bed_id   "
	+" LEFT JOIN adt.m_bed_master bm2 on transfer.to_bed_id=bm2.bed_id   "
	+" INNER JOIN adt.m_floor_master floor on bm.floor_id=floor.floor_id  "
	+" INNER JOIN adt.m_bed_category_master bcm1 on transfer.from_bed_category_id=bcm1.bed_category_id  "
	+" LEFT JOIN adt.m_bed_category_master bcm2 on transfer.to_bed_category_id=bcm2.bed_category_id  "
	+" INNER JOIN adt.m_ward_master ward on transfer.from_ward_id=ward.ward_id "
	+" LEFT JOIN adt.m_ward_master ward2 on transfer.to_ward_id=ward2.ward_id "
	+" INNER JOIN adt.t_transfer_request traReq on transfer.transfer_request_id=traReq.transfer_request_id  "
	+" INNER JOIN doctor.m_doctor_master doc on traReq.treating_doctor_id=doc.doctor_id   "
	+" INNER JOIN adt.m_reason_master reason on traReq.reason_id=reason.reason_master_id "
	+ "INNER JOIN adt.m_icu_type_master icu on traReq.icu_type_id=icu.icu_type_id "
	+" WHERE transfer.transfer_status_id IN (10,3) "
	+" AND traReq.transfer_status_id IN (10,3) "
	+" AND transfer.status='A' "
	+" AND admission.status='A' "
	+ " AND adl.status='A' "
	+ " AND adl.nursing_acceptance = 'Y' "
	+" AND transfer.organization_id=:organizationId "
	+" AND transfer.unit_id=:unitId "
	+ "AND traReq.transfer_type_id=3 "
	+ "AND transfer.from_ward_id IN (:wardList) "),
	
@NamedNativeQuery(name="GET_ICU_TRANSFER_REQ_LIST_BY_ADMISSION_ID",query=
	"SELECT transfer.transfer_id as \"transferId\", "
	+" transfer.transfer_request_id as \"transferRequestId\", "
	+ "admission.admission_id as \"admissionId\", "
	+ "adl.admission_details_id as \"admissionDetailsId\", "		
	+ "admission.patient_id as \"patientId\", "
	+ "admission.t_patient_id as \"tPatientId\", "
	+" pr.uhid_number as \"UHIDNumber\", "
	+" concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name),' ',coalesce(pr.middle_name),' ',coalesce(pr.last_name)) as \"patientName\",  " 
	+" pr.gender_id as \"genderId\",   "
	+" gm.gender_code as \"genderCode\",  "
	+" to_char(pr.dob,'MM/dd/yyyy') as \"dob\",   "
	+" transfer.from_ward_id as \"fromWardId\", "
	+" ward.ward_name as \"fromWardName\", "
	+" transfer.from_bed_id as \"fromBedId\", " 
	+" bm.bed_number as \"fromBedNumber\", "
	+" transfer.from_bed_category_id as \"fromBedCategoryId\", "
	+" bcm1.bed_category_desc as \"fromBedCategoryDesc\", "
	+" bm.floor_id as \"fromFloorId\", "
	+" bm.room_id as \"fromRoomId\", "
	+" transfer.to_ward_id as \"toWardId\", "
	+" ward2.ward_name as \"toWardName\", "
	+" transfer.to_bed_id as \"toBedId\", " 
	+" bm2.bed_number as \"toBedNumber\", "
	+" transfer.to_bed_category_id as \"toBedCategoryId\", "
	+" bcm2.bed_category_desc as \"toBedCategoryDesc\", "
	+" bm2.floor_id as \"toFloorId\", "
	+" bm2.room_id as \"toRoomId\", "
	+" traReq.treating_doctor_id as \"doctorId\", "
	+" concat(coalesce(doc.first_name),' ',coalesce(doc.middle_name),' ',coalesce(doc.last_name)) as \"doctorName\", "
	+" traReq.reason_id as \"reasonId\",  "
	+" reason.reason_desc as \"reasonDesc\", "
	+" traReq.note as \"note\", "
	+ "traReq.icu_type_id as \"icuTypeId\", "
	+ "icu.icu_type as \"icuType\", "
	+ "traReq.transfer_status_id as \"transferStatusId\", "
	+" traReq.final_note as \"finalNote\", "
	+ "bm.billing_bed_category_id as \"fromBillingBedCategoryId\" "
	//+ "bm2.billing_bed_category_id as \"toBillingBedCategoryId\" "
	+" FROM adt.t_transfer transfer "
	+" INNER JOIN adt.t_admission admission on admission.admission_id=transfer.admission_id "
	+ "INNER JOIN adt.t_admission_details adl on admission.admission_id=adl.admission_id "
	+" INNER JOIN patient.t_patient_registration pr on admission.patient_id=pr.patient_id  "
	+" INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id   "
	+ " left join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
	+" INNER JOIN adt.m_bed_master bm on transfer.from_bed_id=bm.bed_id   "
	+" LEFT JOIN adt.m_bed_master bm2 on transfer.to_bed_id=bm2.bed_id   "
	+" INNER JOIN adt.m_floor_master floor on bm.floor_id=floor.floor_id  "
	+" INNER JOIN adt.m_bed_category_master bcm1 on transfer.from_bed_category_id=bcm1.bed_category_id  "
	+" LEFT JOIN adt.m_bed_category_master bcm2 on transfer.to_bed_category_id=bcm2.bed_category_id  "
	+" INNER JOIN adt.m_ward_master ward on transfer.from_ward_id=ward.ward_id "
	+" LEFT JOIN adt.m_ward_master ward2 on transfer.to_ward_id=ward2.ward_id "
	+" INNER JOIN adt.t_transfer_request traReq on transfer.transfer_request_id=traReq.transfer_request_id  "
	+" INNER JOIN doctor.m_doctor_master doc on traReq.treating_doctor_id=doc.doctor_id   "
	+" INNER JOIN adt.m_reason_master reason on traReq.reason_id=reason.reason_master_id "
	+ "INNER JOIN adt.m_icu_type_master icu on traReq.icu_type_id=icu.icu_type_id "
	+" WHERE transfer.transfer_status_id IN (10,3) "
	+" AND traReq.transfer_status_id IN (10,3) "
	+" AND transfer.status='A' "
	+" AND admission.status='A' "
	+ " AND adl.status='A' "
	+ " AND adl.nursing_acceptance = 'Y' "
	+" AND transfer.organization_id=:organizationId "
	+" AND transfer.unit_id=:unitId "
	+ "AND traReq.transfer_type_id=3 "
	+ " AND transfer.admission_id=:admissionId "),
	
	@NamedNativeQuery(name="GET_B2B_TRANSFER_DETAILS_BY_ADMISSIONID",
	query="SELECT trans.transfer_id as \"transferId\", trans.admission_id as \"admissionId\", to_char(trans.from_date, 'dd/MM/yyyy') as \"fromDate\", to_char(trans.from_time, 'HH24:mm') as \"fromTime\","
			+ " trans.from_ward_id as \"fromWardId\", fwm.ward_name as \"fromWardName\", frm.room_desc as \"fromRoomDesc\","
			+ " bbcmfrom.billing_bed_desc as \"fromBillingBedDesc\","
			+ " fbm.bed_desc as \"fromBedDesc\", trans.to_ward_id as \"toWardId\", to_char(trans.to_date, 'dd/MM/yyyy') as \"toDate\",to_char(trans.updated_date, 'dd/MM/yyyy') as \"updatedDate\","
			+ " to_char(trans.to_time, 'HH24:mm') as \"toTime\", twm.ward_name as \"toWardName\", trm.room_desc as \"toRoomDesc\","
			+ " bbcmto.billing_bed_desc as \"toBillingBedDesc\","
			+ " tbm.bed_desc as \"toBedDesc\",reamst.reason_desc as \"reasonDesc\", "
			+ " trans.from_date as \"fromDateNew\","
			+ " trans.updated_date as \"updatedDateNew\""
			+ " FROM adt.t_transfer trans"
			+ " INNER JOIN adt.t_transfer_request trreq ON trreq.transfer_request_id = trans.transfer_request_id"
			+ " LEFT JOIN adt.m_reason_master reamst ON reamst.reason_master_id = trreq.reason_id"
			+ " LEFT JOIN adt.m_ward_master fwm ON fwm.ward_id = trans.from_ward_id"
			+ " LEFT JOIN adt.m_ward_master twm ON twm.ward_id = trans.to_ward_id"
			+ " LEFT JOIN adt.m_bed_master fbm ON fbm.bed_id = trans.from_bed_id"
			+ " LEFT JOIN adt.m_bed_master tbm ON tbm.bed_id = trans.to_bed_id"
			+ " LEFT JOIN adt.m_room_master frm ON frm.room_id = trans.from_room_id"
			+ " LEFT JOIN adt.m_room_master trm ON trm.room_id = trans.to_room_id"
			+ " LEFT JOIN adt.m_billing_bed_category_master bbcmfrom ON "
			+ " bbcmfrom.billing_bed_category_id = trans.from_billing_bed_category_id "
			+ " left join adt.m_billing_bed_category_master bbcmto on "
			+ " bbcmto.billing_bed_category_id = trans.to_billing_bed_category_id "
			+ " WHERE trans.admission_id = :admissionId "
			+ " AND trans.transfer_status_id=9 "
			//+ " AND trans.status='I' " removed this statement for showing ICU transfers
			),
	
	@NamedNativeQuery(name = "ACCEPTED_TRANSFER_OF_CARE_LIST", 
	query =    "select "
			   + " 	tcr.transfer_of_care_id as \"transferOfCareId\", "
			   + " 	tcr.discharge_summary_note as \"dischargeSummaryNote\", "
			   + " 	admission.patient_id as \"patientId\", "
			   + " 	admission.t_patient_id as \"tPatientId\", "
			   + " 	tcr.request_to as \"requestTo\", "
			   + " 	tcr.request_by as \"requestFrom\", "
			   + " 	tcr.speciality_id as \"specialityId\", "
			   + " 	sp.speciality_name as \"specialityName\", "
			   + " 	pr.uhid_number as \"UHIDNumber\", "
			   + " 	admission.admission_number as \"admissionNumber\", "
			   + " 	pr.gender_id as \"genderId\", "
			   + " 	pr.image_path as \"imagePath\", "
			   + " 	gm.gender_code as \"genderCode\", "
			   + " 	to_char(pr.dob, 'MM/dd/yyyy') as \"dob\", "
			   + " 	concat(doc.first_name, ' ', doc.middle_name, ' ', doc.last_name) as \"doctorNameTo\", "
			   + " 	concat(docFrom.first_name, ' ', docFrom.middle_name, ' ', docFrom.last_name) as \"doctorNameFrom\", "
			   + " 	bm.bed_number as \"bedNumber\", "
			   + " 	bm.bed_id as \"bedId\", "
			   + " 	tcr.reason_id as \"reasonId\", "
			   + " 	reason.reason_desc as \"reasonDesc\", "
			   + " 	tcr.note as \"note\", "
			   + " 	tcr.transfer_status_id as \"transferStatusId\", "
			   + " 	trastat.transfer_status_desc as \"transferStatusDesc\", "
			   + " 	tcr.remark as \"remark\", "
			   + " 	tcr.reject_reason_id as \"rejectReasonId\", "
			   + " 	reason2.reason_desc as \"rejectReasonDesc\", "
			   + " 	discharge.discharge_status_id as \"dischargeStatusId\", "
			   + " 	dischargeStatus.discharge_desc as \"dischargeStatusDesc\", "
			   + " 	pr.is_vip as \"isVip\", "
			   + " 	(case "
			   + " 		when pr.is_vip = 'Y' then coalesce(pr.alise_name, pr.first_name) "
			   + " 		else (coalesce(prefxmstr.prefix_code,'')|| ' ' ||coalesce(pr.first_name, '')|| ' ' || coalesce(pr.middle_name, '')|| ' ' || coalesce(pr.last_name, '')) "
			   + " 	end) as \"patientName\", "
			   + " 	(case "
			   + " 		when ((( "
			   + " 		select "
			   + " 			total_score "
			   + " 		from "
			   + " 			adt.t_mews_mapper "
			   + " 		where "
			   + " 			admission_id = admission.admission_id "
			   + " 			and status = 'A') > 6)) then 1 "
			   + " 		else 0 "
			   + " 	end) as \"isCriticalOrder\", "
			   + " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"age\" "

			   + " from "
			   + " 	adt.t_transfer_of_care_request tcr "
			   + " inner join adt.t_admission admission on "
			   + " 	tcr.admission_id = admission.admission_id "
			   + " inner join patient.t_patient_registration pr on "
			   + " 	admission.patient_id = pr.patient_id "
			   + " inner join public.m_gender_master gm on "
			   + " 	pr.gender_id = gm.gender_id "
			   + " left join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
			   + " inner join doctor.m_doctor_master doc on "
			   + " 	tcr.request_to = doc.doctor_id "
			   + " inner join doctor.m_doctor_master docFrom on "
			   + " 	tcr.request_by = docFrom.doctor_id "
			   + " inner join doctor.m_speciality_master sp on "
			   + " 	tcr.speciality_id = sp.speciality_id "
			   + " inner join adt.t_admission_details adl on "
			   + " 	adl.admission_id = admission.admission_id "
			   + " inner join adt.m_bed_master bm on "
			   + " 	adl.bed_id = bm.bed_id "
			   + " inner join adt.m_reason_master reason on "
			   + " 	tcr.reason_id = reason.reason_master_id "
			   + " left join adt.m_reason_master reason2 on "
			   + " 	tcr.reject_reason_id = reason2.reason_master_id "
			   + " inner join adt.m_transfer_status_master trastat on "
			   + " 	tcr.transfer_status_id = trastat.transfer_status_id "
			   + " left join adt.t_discharge discharge on "
			   + " 	discharge.admission_id = admission.admission_id "
			   + " left join adt.m_discharge_status_master dischargeStatus on "
			   + " 	dischargeStatus.discharge_status_id = discharge.discharge_status_id "
			   + " where "
			   + " 	tcr.transfer_type_id = 4 "
			   + " 	and tcr.organization_id =:orgId "
			   + " 	and tcr.unit_id =:unitId "
			   + " 	and tcr.transfer_status_id = '11' "
			   + " 	and adl.status = 'A' "
			   + " 	and docFrom.doctor_id =:doctorId "
			   + " 	order by \"isCriticalOrder\" desc, EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60, tcr.transfer_of_care_id desc "
	),
	
		@NamedNativeQuery(name = "GET_PREVIOUS_ALLOCATED_BED", 
		query = 
		"SELECT tran.from_ward_id as \"toWardId\",tran.from_room_id as \"toRoomId\",tran.from_bed_id as \"toBedId\" ,  "
		+ " from_bed_category_id  as \"toBedCategoryId\",from_billing_bed_category_id as \"toBillingBedCategoryId\","
		+ " tran.treating_doctor_id  as \"treatingDoctorId\",tran.treating_doctor_speciality_id as \"treatingDoctorSpecialityId\"  "
		+ " from adt.t_transfer tran "
		+ " where  tran.transfer_id=(select max(transfer_id) from adt.t_transfer where status='I' and admission_id=:admissionId "
		+ " and organization_id=:organizationId and unit_id=:unitId ) "

		),
		
		@NamedNativeQuery(name="UNIVERSAL_SEARCH_FOR_TRANSFER_REQUEST_LIST",query=
		"SELECT transfer.transfer_id as \"transferId\", "
		+" transfer.transfer_request_id as \"transferRequestId\", "
		+ "admission.admission_id as \"admissionId\", "		
		+ "  admission.visit_type_id as \"visitTypeId\", "
		+ "admission.patient_id as \"patientId\", "
		+ "admission.t_patient_id as \"tPatientId\", "
		+" pr.uhid_number as \"UHIDNumber\", "
		+" concat(coalesce(pr.first_name),' ',coalesce(pr.middle_name),' ',coalesce(pr.last_name)) as \"patientName\",  " 
		+" pr.gender_id as \"genderId\",   "
		+" gm.gender_code as \"genderCode\",  "
		+" to_char(pr.dob,'MM/dd/yyyy') as \"dob\",   "
		+" transfer.from_ward_id as \"fromWardId\", "
		+" ward.ward_name as \"fromWardName\", "
		+" transfer.from_bed_id as \"fromBedId\", " 
		+" bm.bed_number as \"fromBedNumber\", "
		+" transfer.from_bed_category_id as \"fromBedCategoryId\", "
		+" bcm1.bed_category_desc as \"fromBedCategoryDesc\", "
		+" bm.floor_id as \"fromFloorId\", "
		+" bm.room_id as \"fromRoomId\", "
		//+ "nurWard.nursing_station_id as \"nursingStationId\", "
		+" transfer.to_ward_id as \"toWardId\", "
		+" ward2.ward_name as \"toWardName\", "
		+" transfer.to_bed_id as \"toBedId\", " 
		+" bm2.bed_number as \"toBedNumber\", "
		+" transfer.to_bed_category_id as \"toBedCategoryId\", "
		+" bcm2.bed_category_desc as \"toBedCategoryDesc\", "
		+" bm2.floor_id as \"toFloorId\", "
		+" bm2.room_id as \"toRoomId\", "
		+" traReq.treating_doctor_id as \"doctorId\", "
        + "to_char(traReq.created_date,'DD-MM-YYYY HH24:MI:ss') as \"createdDate\","
		+" concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"doctorName\", "
		+" traReq.reason_id as \"reasonId\",  "
		+" reason.reason_desc as \"reasonDesc\", "
		+" traReq.note as \"note\", "
		+" traReq.final_note as \"finalNote\","
		+ "bm.billing_bed_category_id as \"fromBillingBedCategoryId\", "
		+ "bm2.billing_bed_category_id as \"toBillingBedCategoryId\" ,"
        + " prefxmstr.prefix_desc as \"prefixDesc\",pr.image_path as \"imagePath\" , "
//        + " concat(to_char(extract(year from age(current_timestamp, pr.dob)), '999'), 'Y') as \"ageStr\" " 
		+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\" "

		+" FROM adt.t_transfer transfer "
		+" INNER JOIN adt.t_admission admission on admission.admission_id=transfer.admission_id   "
		+" INNER JOIN patient.t_patient_registration pr on admission.patient_id=pr.patient_id  "
        + " left join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
		+" INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id   "
		+" LEFT JOIN adt.m_bed_master bm on transfer.from_bed_id=bm.bed_id   "
		+" LEFT JOIN adt.m_bed_master bm2 on transfer.to_bed_id=bm2.bed_id   "
		+" LEFT JOIN adt.m_floor_master floor on bm.floor_id=floor.floor_id  "
		+" INNER JOIN adt.m_bed_category_master bcm1 on transfer.from_bed_category_id=bcm1.bed_category_id  "
		+" INNER JOIN adt.m_bed_category_master bcm2 on transfer.to_bed_category_id=bcm2.bed_category_id  "
		+" INNER JOIN adt.m_ward_master ward on transfer.from_ward_id=ward.ward_id "
		//+" LEFT JOIN adt.t_nursing_station_ward_mapper nurWard on ward.ward_id = nurWard.ward_id "
		+" INNER JOIN adt.m_ward_master ward2 on transfer.to_ward_id=ward2.ward_id "
		+" INNER JOIN adt.t_transfer_request traReq on transfer.transfer_request_id=traReq.transfer_request_id  "
		+" INNER JOIN doctor.m_doctor_master doc on traReq.treating_doctor_id=doc.doctor_id   "
		+" INNER JOIN adt.m_reason_master reason on traReq.reason_id=reason.reason_master_id "
		+" WHERE transfer.transfer_status_id=7 "
		+" AND traReq.transfer_status_id=7 "
		+" AND transfer.status='A' "
		+" AND admission.status='A' "
		+" AND transfer.organization_id=:organizationId "
		+" AND transfer.unit_id=:unitId "
		+" AND transfer.to_ward_id IN (:wardList) "
		+ "  AND (lower(pr.uhid_number) like :patientName "
		+ "  OR lower(pr.first_name) like :patientName   OR lower(pr.middle_name) like :patientName OR lower(pr.last_name) like :patientName "
		+ "  OR lower(doc.first_name) like :patientName 	  OR lower(doc.middle_name) like :patientName OR lower(doc.last_name) like :patientName "
		+ "  OR lower(bm.bed_number) like :patientName "
		+ "  OR lower(bm2.bed_number) like :patientName "
		+ "  OR lower(ward.ward_name) like :patientName "
		+ "  OR lower(ward2.ward_name) like :patientName "
		+ "  OR lower(bcm1.bed_category_desc) like :patientName "
		+ "  OR lower(bcm2.bed_category_desc) like :patientName "
		+ "  OR lower(reason.reason_desc) like :patientName ) "),


})

@Entity
@Table(name="t_transfer",schema="adt")
@SequenceGenerator(name="transfer_seq",sequenceName="adt.transfer_seq",allocationSize=1)
public class Transfer 
{
	@Id
	@Column(name="transfer_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="transfer_seq")
	private int transferId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="admission_id")
	private Integer admissionId;
	
	@Column(name="from_bed_category_id")
	private Integer fromBedCategoryId;
	
	@Column(name="from_billing_bed_category_id")
	private Integer fromBillingBedCategoryId;
	
	@Column(name="from_ward_id")
	private Integer fromWardId;
	
	@Column(name="from_room_id")
	private Integer fromRoomId;
	
	@Column(name="from_bed_id")
	private Integer fromBedId;
	
	@Column(name="from_date")
	private Date fromDate;
	
	@Column(name="from_time")
	private Date fromTime;
	
	@Column(name="to_bed_category_id")
	private Integer toBedCategoryId;
	
	@Column(name="to_billing_bed_category_id")
	private Integer toBillingBedCategoryId;
	
	@Column(name="to_ward_id")
	private Integer toWardId;
	
	@Column(name="to_room_id")
	private Integer toRoomId;
	
	@Column(name="to_bed_id")
	private Integer toBedId;
	
	@Column(name="to_date")
	private Date toDate;
	
	@Column(name="to_time")
	private Date toTime;
	
	@Column(name="is_primary")
	private char isPrimary;
	
	@Column(name="transfer_request_id")
	private Integer transferRequestId;
	
	@Column(name="created_by")
	private int createdBy;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_by")
	private int updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="status")
	private char status;
	
	@Column(name="transfer_status_id")
	private Integer transferStatusId;
	
	
	@Column(name="treating_doctor_id")
	private Integer treatingDoctorId;
	
	@Column(name="treating_doctor_speciality_id")
	private Integer treatingDoctorSpecialityId;
	
	
	@Column(name="old_transfer_id")
	private Integer oldTransferId;
	

	public Integer getOldTransferId() {
		return oldTransferId;
	}

	public void setOldTransferId(Integer oldTransferId) {
		this.oldTransferId = oldTransferId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", insertable = false, nullable = false, updatable = false)
	private OrganizationMaster organizationMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unit_id", insertable = false, nullable = false, updatable = false)
	private UnitMaster unitMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admission_id", insertable = false, nullable = false, updatable = false)
	private Admission admission;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_bed_category_id", insertable = false, nullable = false, updatable = false)
	private BedCategoryMaster bedCategoryMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_billing_bed_category_id", insertable = false, nullable = false, updatable = false)
	private BillingBedCategoryMaster billingBedCategoryMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_ward_id", insertable = false, nullable = false, updatable = false)
	private WardMaster wardMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_room_id", insertable = false, nullable = false, updatable = false)
	private RoomMaster roomMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_bed_id", insertable = false, nullable = false, updatable = false)
	private BedMaster bedMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_bed_category_id", insertable = false, nullable = false, updatable = false)
	private BedCategoryMaster bedCategoryMaster2;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_billing_bed_category_id", insertable = false, nullable = false, updatable = false)
	private BillingBedCategoryMaster billingBedCategoryMaster2;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_ward_id", insertable = false, nullable = false, updatable = false)
	private WardMaster wardMaster2;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_room_id", insertable = false, nullable = false, updatable = false)
	private RoomMaster roomMaster2;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_bed_id", insertable = false, nullable = false, updatable = false)
	private BedMaster bedMaster2;
	
	
	

	public Integer getTransferStatusId() {
		return transferStatusId;
	}

	public void setTransferStatusId(Integer transferStatusId) {
		this.transferStatusId = transferStatusId;
	}

	public int getTransferId() {
		return transferId;
	}

	public void setTransferId(int transferId) {
		this.transferId = transferId;
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

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getFromBedCategoryId() {
		return fromBedCategoryId;
	}

	public void setFromBedCategoryId(Integer fromBedCategoryId) {
		this.fromBedCategoryId = fromBedCategoryId;
	}

	public Integer getFromBillingBedCategoryId() {
		return fromBillingBedCategoryId;
	}

	public void setFromBillingBedCategoryId(Integer fromBillingBedCategoryId) {
		this.fromBillingBedCategoryId = fromBillingBedCategoryId;
	}

	public Integer getFromWardId() {
		return fromWardId;
	}

	public void setFromWardId(Integer fromWardId) {
		this.fromWardId = fromWardId;
	}

	public Integer getFromRoomId() {
		return fromRoomId;
	}

	public void setFromRoomId(Integer fromRoomId) {
		this.fromRoomId = fromRoomId;
	}

	public Integer getFromBedId() {
		return fromBedId;
	}

	public void setFromBedId(Integer fromBedId) {
		this.fromBedId = fromBedId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getFromTime() {
		return fromTime;
	}

	public void setFromTime(Date fromTime) {
		this.fromTime = fromTime;
	}

	

	public Integer getToBedCategoryId() {
		return toBedCategoryId;
	}

	public void setToBedCategoryId(Integer toBedCategoryId) {
		this.toBedCategoryId = toBedCategoryId;
	}

	public Integer getToBillingBedCategoryId() {
		return toBillingBedCategoryId;
	}

	public void setToBillingBedCategoryId(Integer toBillingBedCategoryId) {
		this.toBillingBedCategoryId = toBillingBedCategoryId;
	}

	public Integer getToWardId() {
		return toWardId;
	}

	public void setToWardId(Integer toWardId) {
		this.toWardId = toWardId;
	}

	public Integer getToRoomId() {
		return toRoomId;
	}

	public void setToRoomId(Integer toRoomId) {
		this.toRoomId = toRoomId;
	}

	public Integer getToBedId() {
		return toBedId;
	}

	public void setToBedId(Integer toBedId) {
		this.toBedId = toBedId;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Date getToTime() {
		return toTime;
	}

	public void setToTime(Date toTime) {
		this.toTime = toTime;
	}

	public char getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(char isPrimary) {
		this.isPrimary = (isPrimary == '\u0000') ? 'N' : isPrimary;
	}

	public Integer getTransferRequestId() {
		return transferRequestId;
	}

	public void setTransferRequestId(Integer transferRequestId) {
		this.transferRequestId = transferRequestId;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'A' : status;
	}

	public Integer getTreatingDoctorId() {
		return treatingDoctorId;
	}

	public void setTreatingDoctorId(Integer treatingDoctorId) {
		this.treatingDoctorId = treatingDoctorId;
	}

	public Integer getTreatingDoctorSpecialityId() {
		return treatingDoctorSpecialityId;
	}

	public void setTreatingDoctorSpecialityId(Integer treatingDoctorSpecialityId) {
		this.treatingDoctorSpecialityId = treatingDoctorSpecialityId;
	}

	
	
	
	
}



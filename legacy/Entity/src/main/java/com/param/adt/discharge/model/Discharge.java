package com.param.adt.discharge.model;

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

import com.param.adt.admission.model.Admission;
import com.param.adt.master.global.model.BedMaster;
import com.param.adt.master.global.model.DischargeTypeMaster;
import com.param.adt.master.global.model.OrganizationMaster;
import com.param.adt.master.global.model.ReasonMaster;
import com.param.adt.master.global.model.UnitMaster;
import com.param.global.model.DoctorMaster;

@NamedNativeQueries({

		@NamedNativeQuery(name = "GET_ER_DISCHARGE_LIST", query =" select "
				+ " 	dis.discharge_id as \"dischargeId\", "
				+ " 	dis.discharge_type_id as \"dischargeTypeId\", "
				+ " 	disStatus.discharge_desc as \"dischargeDesc\", "
				+ " 	dis.discharge_status_id as \"dischargeStatusId\", "
				+ " 	admission.admission_id as \"admissionId\", "
				+ " 	admission.patient_id as \"patientId\", "
				+ " 	admission.t_patient_id as \"tPatientId\", "
				+ " 	admission.admission_number as \"admissionNumber\", "
				+ " 	admission.visit_type_id as \"visitTypeId\", "
				+ " 	admission.doctor_id as \"doctorId\", "
				+ " 	pr.uhid_number as \"UHIDNumber\", "
				+ " 	concat(coalesce(pr.first_name, ''), ' ', coalesce(pr.middle_name, ''), ' ', coalesce(pr.last_name, '')) as \"pFirstName\", "
				+ " 	pr.mobile_number as \"mobileNo\"," 
				+ "     pr.gender_id as \"genderId\", "
				+ " 	gm.gender_code as \"genderCode\", "
				+ "     gm.gender_name as \"genderName\","
				+ " 	to_char(pr.dob, "
				+ " 	'MM/dd/yyyy') as \"dob\", "
				+ " 	to_char(adl.doa, "
				+ " 	'dd/MM/yyyy') as \"doa\", "
				+ " 	to_char(adl.pdd, "
				+ " 	'dd/MM/yyyy') as \"pdd\", "
				+ " 	bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
				+ " 	bm.bed_number as \"bedNumber\", "
				+ " 	bm.bed_id as \"bedId\", "
				+ " 	wm.ward_id as \"wardId\", "
				+ " 	wm.ward_name as \"wardName\", "
				+ " 	dis.remark_discharge as \"remarkDischarge\", "
				+ " 	dtm.discharge_type_name as \"dischargeTypeName\",disCharge.discharge_summary_id as \"dischargeSummaryId\", "
			    +"      prefxmstr.prefix_code as \"prefixDesc\",pr.image_path as \"imagePath\" ,"
			    + "     concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\"  "

				+ " from "
				+ " 	adt.t_discharge dis "
				+ " inner join adt.m_discharge_status_master disStatus on "
				+ " 	dis.discharge_status_id = disStatus.discharge_status_id "
				+ " inner join adt.m_reason_for_discharge_master dtm on "
				+ " 	dis.discharge_type_id = dtm.discharge_type_id "
				+ " inner join adt.t_admission admission on "
				+ " 	admission.admission_id = dis.admission_id "
				+ " inner join patient.t_patient_registration pr on "
				+ " 	admission.patient_id = pr.patient_id "
				+ " inner join public.m_gender_master gm on "
				+ " 	pr.gender_id = gm.gender_id "
				+ " inner join adt.t_admission_details adl on "
				+ " 	adl.admission_id = admission.admission_id "
				+ " left join adt.m_bed_master bm on "
				+ " 	adl.bed_id = bm.bed_id "
				+ " left join adt.m_ward_master wm on "
				+ " 	adl.ward_id = wm.ward_id "
				+ " left join adt.m_bed_category_master bedCatMst on "
				+ " 	adl.bed_category_id = bedCatMst.bed_category_id "
				+ "	left join adt.m_discharge_summary disCharge on disCharge.admission_id=admission.admission_id and disCharge.status='A' "
	            + " left join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "

				+ " where "
				+ " 	admission.status = 'A' "
				+ " 	and adl.status = 'A' "
				+ " 	and dis.status = 'A'  "
				+ " 	and admission.organization_id = :organizationId "
				+ " 	and admission.unit_id = :unitId "
				+ " 	and dis.visit_type_id = 4 "
				+ "		and dis.discharge_status_id in (1,2,3) "
				+ "		AND bm.ward_id IN (:wardList) "
				+ "		order by dis.discharge_id desc "),

		@NamedNativeQuery(name = "GET_DISCHARGE_PATIENT_LIST_FOR_NURSING", query = "SELECT  dis.discharge_id as \"dischargeId\", "
				+ "dis.discharge_type_id as \"dischargeTypeId\", " 
				+ "disStatus.discharge_desc as \"dischargeDesc\", "
				+ "dis.discharge_status_id as \"dischargeStatusId\", "
				+ "to_char(dis.discharge_date,'dd/MM/yyyy') as \"futureDischargeDate\", "
				+ "admission.admission_id as \"admissionId\", " 
				+ "admission.patient_id as \"patientId\", "
				+ "admission.t_patient_id as \"tPatientId\", " 
				+ "admission.admission_number as \"admissionNumber\", "
				+ "admission.visit_type_id as \"visitTypeId\", " 
				+ "admission.doctor_id as \"doctorId\", "
				+ "admission.speciality_id as \"specialityId\", " 
				+ "sp.speciality_name as \"specialityName\", "
				+ "pr.uhid_number as \"UHIDNumber\", " 
				+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'Y ' ,EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)),'M ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageString\", "
				+ "concat(COALESCE(pr.first_name,''),' ',COALESCE(pr.middle_name,''),' ',COALESCE(pr.last_name,'')) as \"pFirstName\" , "
				+ "pr.gender_id as \"genderId\", " 
				+ "gm.gender_code as \"genderCode\", "
				+ "to_char(pr.dob,'MM/dd/yyyy') as \"dob\", "
				+ " concat(COALESCE(doc.first_name,''),' ', COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
				+ "to_char(adl.doa,'dd/MM/yyyy') as \"doa\", " 
				+ "to_char(adl.pdd,'dd/MM/yyyy') as \"pdd\", "
				+ "spd.payment_entitlement_id as \"paymentEntitlementId\", "
				+ "pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
				+ "bedCatMst.bed_category_desc as \"bedCategoryDesc\", " 
				+ "bm.bed_number as \"bedNumber\", " 
				+ "wm.ward_name as \"wardName\", "
				+ "fm.floor_name as \"floorName\", "
				+ " admission.is_financial_creance as \"isFinancialClearance\", "
				+ "bm.bed_id as \"bedId\", "
				+ "dtm.discharge_type_name as \"dischargeTypeName\" ,disCharge.discharge_summary_id as \"dischargeSummaryId\", "
	            +" prefxmstr.prefix_code as \"prefixDesc\",pr.image_path as \"imagePath\" "

				+ "FROM adt.t_discharge dis "
				+ "INNER JOIN adt.m_discharge_status_master disStatus on dis.discharge_status_id=disStatus.discharge_status_id "
				+ "INNER JOIN adt.m_reason_for_discharge_master dtm on dis.discharge_type_id=dtm.discharge_type_id "
				+ "INNER JOIN adt.t_admission admission on admission.admission_id = dis.admission_id "
				+ "INNER JOIN patient.t_patient_registration pr on admission.patient_id=pr.patient_id "
				+ "INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id "
				+ "INNER JOIN doctor.m_doctor_master doc on admission.doctor_id=doc.doctor_id "
				+ "INNER JOIN doctor.m_speciality_master sp on admission.speciality_id=sp.speciality_id  "
				+ "INNER JOIN adt.t_admission_details adl on adl.admission_id=admission.admission_id  "
				+ "INNER JOIN adt.m_bed_master bm on adl.bed_id=bm.bed_id "
				+ "LEFT JOIN adt.m_ward_master wm on bm.ward_id=wm.ward_id "
				+ "LEFT JOIN adt.m_floor_master fm on wm.floor_id=fm.floor_id "
				+ "INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 AND spd.status='A' "
				+ "INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
				+ "INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
				+ "	left join adt.m_discharge_summary disCharge on disCharge.admission_id=admission.admission_id and disCharge.status='A' " 
	            + " left join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "

				+ " WHERE admission.status='A' " 
				+ " AND adl.status='A' "
				+ " AND adl.nursing_acceptance = 'Y' "
				+ "and dis.status = 'A'   " 
				+ "AND admission.organization_id=:orgId  " 
				+ "AND admission.unit_id=:unitId "
				+ "and dis.status='A' and dis.discharge_status_id in (1,2,3) "
				+ "AND (bm.ward_id IN (:wardList) OR bm.is_moving_bed='Y' ) "
				+ " order by dis.discharge_id desc "),
		
		@NamedNativeQuery(name = "GET_DISCHARGE_PATIENT_LIST_FOR_NURSING_VISIT_TYPE_ID", query = "SELECT  dis.discharge_id as \"dischargeId\", "
				+ "dis.discharge_type_id as \"dischargeTypeId\", " 
				+ "disStatus.discharge_desc as \"dischargeDesc\", "
				+ "dis.discharge_status_id as \"dischargeStatusId\", "
				+ "to_char(dis.discharge_date,'dd/MM/yyyy') as \"futureDischargeDate\", "
				+ "admission.admission_id as \"admissionId\", " 
				+ "admission.patient_id as \"patientId\", "
				+ "admission.t_patient_id as \"tPatientId\", " 
				+ "admission.admission_number as \"admissionNumber\", "
				+ "admission.visit_type_id as \"visitTypeId\", " 
				+ "admission.doctor_id as \"doctorId\", "
				+ "admission.speciality_id as \"specialityId\", " 
				+ "sp.speciality_name as \"specialityName\", "
				+ "pr.uhid_number as \"UHIDNumber\", " 
				+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'Y ' ,EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)),'M ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageString\", "
				+ "concat(COALESCE(pr.first_name,''),' ',COALESCE(pr.middle_name,''),' ',COALESCE(pr.last_name,'')) as \"pFirstName\" , "
				+ "pr.gender_id as \"genderId\", " 
				+ "gm.gender_code as \"genderCode\", "
				+ "to_char(pr.dob,'MM/dd/yyyy') as \"dob\", "
				+ " concat(COALESCE(doc.first_name,''),' ', COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
				+ "to_char(adl.doa,'dd/MM/yyyy') as \"doa\", " 
				+ "to_char(adl.pdd,'dd/MM/yyyy') as \"pdd\", "
				+ "spd.payment_entitlement_id as \"paymentEntitlementId\", "
				+ "pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
				+ "bedCatMst.bed_category_desc as \"bedCategoryDesc\", " 
				+ "bm.bed_number as \"bedNumber\", " 
				+ "wm.ward_name as \"wardName\", "
				+ "fm.floor_name as \"floorName\", "
				+ " admission.is_financial_creance as \"isFinancialClearance\", "
				+ "bm.bed_id as \"bedId\", "
				+ "dtm.discharge_type_name as \"dischargeTypeName\" ,disCharge.discharge_summary_id as \"dischargeSummaryId\", "
	            +" prefxmstr.prefix_code as \"prefixDesc\",pr.image_path as \"imagePath\", "
				+" com.company_desc as \"companyName\" "

				+ "FROM adt.t_discharge dis "
				+ "INNER JOIN adt.m_discharge_status_master disStatus on dis.discharge_status_id=disStatus.discharge_status_id "
				+ "INNER JOIN adt.m_reason_for_discharge_master dtm on dis.discharge_type_id=dtm.discharge_type_id "
				+ "INNER JOIN adt.t_admission admission on admission.admission_id = dis.admission_id "
				+ "INNER JOIN patient.t_patient_registration pr on admission.patient_id=pr.patient_id "
				+ "INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id "
				+ "INNER JOIN doctor.m_doctor_master doc on admission.doctor_id=doc.doctor_id "
				+ "INNER JOIN doctor.m_speciality_master sp on admission.speciality_id=sp.speciality_id  "
				+ "INNER JOIN adt.t_admission_details adl on adl.admission_id=admission.admission_id  "
				+ "INNER JOIN adt.m_bed_master bm on adl.bed_id=bm.bed_id "
				+ "LEFT JOIN adt.m_ward_master wm on bm.ward_id=wm.ward_id "
				+ "LEFT JOIN adt.m_floor_master fm on wm.floor_id=fm.floor_id "
				+ "INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 AND spd.status='A' "
				+ "INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
				+ "INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
				+ "	left join adt.m_discharge_summary disCharge on disCharge.admission_id=admission.admission_id"
				+ " and disCharge.status='A' " 
	            + " left join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
				+ " left join public.t_company_master com on "
				+ " com.company_id = spd.payee_id"
				+ " WHERE admission.status='A' " 
				+ " AND adl.status='A' "
				+ " AND adl.nursing_acceptance = 'Y' "
				+ "and dis.status = 'A'" 
				+ "AND admission.organization_id=:orgId  " 
				+ "AND admission.unit_id=:unitId "
				+ " AND admission.visit_type_id =:visitTypeId "
				+ "and dis.status='A' and dis.discharge_status_id in (1,2,3) "
				+ "AND (bm.ward_id IN (:wardList) OR bm.is_moving_bed='Y' ) "
				+ "order by dis.discharge_id desc "),
		
		
//		@NamedNativeQuery(name = "GET_PATIENT_READY_FOR_BILLING",
//		query = "select "
//				 + " 	distinct(discharge.admission_id) as \"admissionId\", "
//				 + " 	admission.patient_id as \"patientId\", "
//				 + " 	admission.t_patient_id as \"tPatientId\", "
//				 + " 	admission.admission_number as \"admissionNumber\", "
//				 + " 	admission.visit_type_id as \"visitTypeId\", "
//				 + " 	admission.doctor_id as \"doctorId\", "
//				 + " 	admission.speciality_id as \"specialityId\", "
//				 + " 	sm.speciality_name as \"specialityName\", "
//				 + " 	pr.uhid_number as \"UHIDNumber\", "
//				 + " 	prefxmstr.prefix_code as \"prefixDesc\", "
//				 + " 	pr.image_path as \"imagePath\" , "
//				 + " 	concat(extract(year from age(current_timestamp, pr.dob)) , 'Y ', extract(month from age(current_timestamp, pr.dob)) , 'M ' , extract(day from age(current_timestamp, pr.dob)), 'D ') as \"ageStr\", "
//				 + " 	gm.gender_code as \"genderCode\", "
//				 + " 	concat(coalesce(pr.first_name, ''), ' ', coalesce(pr.middle_name, ''), ' ', coalesce(pr.last_name, '')) as \"pFirstName\" , "
//				 + " 	to_char(pr.dob, 'MM/dd/yyyy') as \"dob\", "
//				 + " 	concat(coalesce(doc.first_name, ''), ' ', coalesce(doc.middle_name, ''), ' ', coalesce(doc.last_name, '')) as \"dFirstName\", "
//				 + " 	bm.bed_number as \"bedNumber\", "
//				 + " 	bm.floor_id as \"floorId\", "
//				 + " 	coalesce(floor.floor_name, '') as \"floorName\", "
//				 + " 	discharge.discharge_id as \"dischargeId\", "
//				 + " 	discharge.discharge_type_id as \"dischargeTypeId\", "
//				 + " 	discharge.discharge_status_id as \"dischargeStatusId\", "
//				 + " 	disStatus.discharge_desc as \"dischargeDesc\", "
//				 + " 	dtm.discharge_type_name as \"dischargeTypeName\", "
//				 + " 	admissionDetails.er_bed_type_allocation as \"erBedTypeAllocation\", "
//				 /*+ " 	( "
//				 + " 	select "
//				 + " 		count(od.order_details_id) "
//				 + " 	from "
//				 + " 		public.t_order_master om "
//				 + " 	inner join public.t_order_details od on "
//				 + " 		om.order_id = od.order_id "
//				 + " 	inner join service.m_service_master ser on "
//				 + " 		od.service_id = ser.service_master_id "
//				 + " 	where "
//				 + " 		om.admission_id = discharge.admission_id "
//				 + " 		and ser.speciality_id = :labDeptId "
//				 + " 		and od.service_rendered = 1 "
//				 + " 		and od.service_is_billed = 0 "
//				 + " 		and od.status = 'A')as \"labCountGreen\", "*/
//				 + " 	( "
//				 + " 	select "
//				 + " 		count(od.order_details_id) "
//				 + " 	from "
//				 + " 		public.t_order_master om "
//				 + " 	inner join public.t_order_details od on "
//				 + " 		om.order_id = od.order_id "
//				 + " 	inner join service.m_service_master ser on "
//				 + " 		od.service_id = ser.service_master_id "
//				 + " 	where "
//				 + " 		om.admission_id = discharge.admission_id "
//				 + " 		and ser.speciality_id = :labDeptId "
//				 + " 		and od.service_rendered = 0 "
//				 + " 		and od.service_is_billed = 0 "
//				 + " 		and od.status = 'A')as \"labCountRed\", "
//				 + " 	( "
//				 + " 	select "
//				 + " 		count(od.order_details_id) "
//				 + " 	from "
//				 + " 		public.t_order_master om "
//				 + " 	inner join public.t_order_details od on "
//				 + " 		om.order_id = od.order_id "
//				 + " 	inner join service.m_service_master ser on "
//				 + " 		od.service_id = ser.service_master_id "
//				 + " 	where "
//				 + " 		om.admission_id = discharge.admission_id "
//				 + " 		and ser.speciality_id = :labDeptId "
//				 + " 		and od.status = 'A')as \"labCountYellow\", "
//				/* + " 	( "
//				 + " 	select "
//				 + " 		count(od.order_details_id) "
//				 + " 	from "
//				 + " 		public.t_order_master om "
//				 + " 	inner join public.t_order_details od on "
//				 + " 		om.order_id = od.order_id "
//				 + " 	inner join service.m_service_master ser on "
//				 + " 		od.service_id = ser.service_master_id "
//				 + " 	where "
//				 + " 		om.admission_id = discharge.admission_id "
//				 + " 		and ser.speciality_id = :radDeptId "
//				 + " 		and od.service_rendered = 1 "
//				 + " 		and od.service_is_billed = 0 "
//				 + " 		and od.status = 'A')as \"radiologyCountGreen\", "*/
//				 + " 	( "
//				 + " 	select "
//				 + " 		count(od.order_details_id) "
//				 + " 	from "
//				 + " 		public.t_order_master om "
//				 + " 	inner join public.t_order_details od on "
//				 + " 		om.order_id = od.order_id "
//				 + " 	inner join service.m_service_master ser on "
//				 + " 		od.service_id = ser.service_master_id "
//				 + " 	where "
//				 + " 		om.admission_id = discharge.admission_id "
//				 + " 		and ser.speciality_id = :radDeptId "
//				 + " 		and od.service_rendered = 0 "
//				 + " 		and od.service_is_billed = 0 "
//				 + " 		and od.status = 'A')as \"radiologyCountRed\", "
//				 + " 	( "
//				 + " 	select "
//				 + " 		count(od.order_details_id) "
//				 + " 	from "
//				 + " 		public.t_order_master om "
//				 + " 	inner join public.t_order_details od on "
//				 + " 		om.order_id = od.order_id "
//				 + " 	inner join service.m_service_master ser on "
//				 + " 		od.service_id = ser.service_master_id "
//				 + " 	where "
//				 + " 		om.admission_id = discharge.admission_id "
//				 + " 		and ser.speciality_id = :radDeptId "
//				 + " 		and od.status = 'A')as \"radiologyCountYellow\", "
//				 /*+ " 	( "
//				 + " 	select "
//				 + " 		count(od.order_details_id) "
//				 + " 	from "
//				 + " 		public.t_order_master om "
//				 + " 	inner join public.t_order_details od on "
//				 + " 		om.order_id = od.order_id "
//				 + " 	inner join service.m_service_master ser on "
//				 + " 		od.service_id = ser.service_master_id "
//				 + " 	where "
//				 + " 		om.admission_id = discharge.admission_id "
//				 + " 		and ser.speciality_id not in (:radDeptId, "
//				 + " 		:labDeptId) "
//				 + " 		and od.service_rendered = 1 "
//				 + " 		and od.service_is_billed = 0 "
//				 + " 		and od.status = 'A')as \"otherSerCountGreen\", " */
//				 + " 	( "
//				 + " 	select "
//				 + " 		count(od.order_details_id) "
//				 + " 	from "
//				 + " 		public.t_order_master om "
//				 + " 	inner join public.t_order_details od on "
//				 + " 		om.order_id = od.order_id "
//				 + " 	inner join service.m_service_master ser on "
//				 + " 		od.service_id = ser.service_master_id "
//				 + " 	where "
//				 + " 		om.admission_id = discharge.admission_id "
//				 + " 		and ser.speciality_id not in (:radDeptId, "
//				 + " 		:labDeptId) "
//				 + " 		and od.service_rendered = 0 "
//				 + " 		and od.service_is_billed = 0 "
//				 + " 		and od.status = 'A')as \"otherSerCountRed\", "
//				 + " 	( "
//				 + " 	select "
//				 + " 		count(od.order_details_id) "
//				 + " 	from "
//				 + " 		public.t_order_master om "
//				 + " 	inner join public.t_order_details od on "
//				 + " 		om.order_id = od.order_id "
//				 + " 	inner join service.m_service_master ser on "
//				 + " 		od.service_id = ser.service_master_id "
//				 + " 	where "
//				 + " 		om.admission_id = discharge.admission_id "
//				 + " 		and ser.speciality_id not in (:radDeptId, "
//				 + " 		:labDeptId) "
//				 + " 		and od.status = 'A')as \"otherSerCountYellow\", "
//				 + " 	( "
//				 + " 	select "
//				 + " 		count(pir.id) "
//				 + " 	from "
//				 + " 		pharmacy.t_patient_issue_return pir "
//				 + " 	where "
//				 + " 		(admission_id = discharge.admission_id) "
//				 + " 		and ((pir.status_id = 2 "
//				 + " 		or pir.status_id = 1) "
//				 + " 		and pir.status_id != 9)) as \"salesRed\", "
//				/* + " 	( "
//				 + " 	select "
//				 + " 		count(pir.id) "
//				 + " 	from "
//				 + " 		pharmacy.t_patient_issue_return pir "
//				 + " 	where "
//				 + " 		(admission_id = discharge.admission_id) "
//				 + " 		and ((pir.status_id = 4 "
//				 + " 		or pir.status_id = 8) "
//				 + " 		and pir.status_id != 9)) as \"salesGreen\", "*/
//				 + " 	( "
//				 + " 	select "
//				 + " 		count(pir.id) "
//				 + " 	from "
//				 + " 		pharmacy.t_patient_issue_return pir "
//				 + " 	where "
//				 + " 		(admission_id = discharge.admission_id)) as \"salesYellow\", "
//				 + " 	( "
//				 + " 	select "
//				 + " 		count(om.pro_req_id) "
//				 + " 	from "
//				 + " 		otims.t_procedure_req_mst om "
//				 + " 	where "
//				 + " 		admission_id = discharge.admission_id "
//				 + " 		and procedure_status_id not in (2,5,6,7,8,9)) as \"procedureRed\", "
//				 /*+ " 	( "
//				 + " 	select "
//				 + " 		count(om.pro_req_id) "
//				 + " 	from "
//				 + " 		otims.t_procedure_req_mst om "
//				 + " 	where "
//				 + " 		admission_id = discharge.admission_id "
//				 + " 		and procedure_status_id = 3) as \"procedureGreen\", " */
//				 + " 	( "
//				 + " 	select "
//				 + " 		count(om.pro_req_id) "
//				 + " 	from "
//				 + " 		otims.t_procedure_req_mst om "
//				 + " 	where "
//				 + " 		om.admission_id = discharge.admission_id AND om.procedure_status_id not in (9)) as \"procedureYellow\", "
//				 + "	( "
//				 + "	 	select "
//				 + "	 		count(pir.id) "
//				 + "		 	from "
//				 + "	 		pharmacy.t_issue pir "
//				 + "	 	inner join pharmacy.t_issue_detail id on "
//				 + "	 		id.issue_id = pir.id "
//				 + "	 	inner join pharmacy.t_prescription_detail pd on "
//				 + "		 		id.prescription_detail_id = pd.id "
//				 + "	 	where "
//				 + "	 		pir.admission_id = discharge.admission_id "
//				 + " 			and pir.status_id in (2,1) "
//				 + " 			and pir.status_id not in (9,4) "
//				 + "			and pd.is_discharge_medication = false) as \"pharmecyRed\", "
//				/* + " 	( "
//				 + " 	select "
//				 + " 		count(pir.id) "
//				 + " 	from "
//				 + " 		pharmacy.t_issue pir "
//				 + " 	where "
//				 + " 		pir.admission_id = discharge.admission_id "
//				 + " 		and ((pir.status_id = 8 "
//				 + " 		or pir.status_id = 4) "
//				 + " 		and pir.status_id != 9)) as \"pharmecyGreen\", " */
//				 + " 	( "
//				 + " 	select "
//				 + " 		count(pir.id) "
//				 + " 	from "
//				 + " 		pharmacy.t_issue pir "
//				 + " 	inner join pharmacy.t_issue_detail id on "
//				 + " 		id.issue_id = pir.id "
//				 + " 	inner join pharmacy.t_prescription_detail pd on "
//				 + " 		id.prescription_detail_id = pd.id "
//				 + " 	where "
//				 + " 		pir.admission_id = discharge.admission_id "
//				 + " 		and pd.is_discharge_medication = false) as \"pharmecyYellow\", "
//				/* + " 	( "
//				 + " 	select "
//				 + " 		count(vp.visitor_patient_id) "
//				 + " 	from "
//				 + " 		adt.t_visitor_against_patient vp "
//				 + " 	inner join adt.t_visitor_patient_mapper vpm on "
//				 + " 		vp.visitor_patient_id = vpm.visitor_patient_id "
//				 + " 	where "
//				 + " 		vp.admission_id = discharge.admission_id "
//				 + " 		and ((vpm.visitor_pass_status = 'A') "
//				 + " 		and vpm.status = 'A')) as \"visitorPassGreen\", "*/
//				 + " 	( "
//				 + " 	select "
//				 + " 		count(vp.visitor_patient_id) "
//				 + " 	from "
//				 + " 		adt.t_visitor_against_patient vp "
//				 + " 	inner join adt.t_visitor_patient_mapper vpm on "
//				 + " 		vp.visitor_patient_id = vpm.visitor_patient_id "
//				 + " 	where "
//				 + " 		vp.admission_id = discharge.admission_id "
//				 + " 		and ((vpm.visitor_pass_status = 'I') "
//				 + " 		and vpm.status = 'A')) as \"visitorPassRed\", "
//				 + " 	( "
//				 + " 	select "
//				 + " 		count(vp.visitor_patient_id) "
//				 + " 	from "
//				 + " 		adt.t_visitor_against_patient vp "
//				 + " 	where "
//				 + " 		vp.admission_id = discharge.admission_id) as \"visitorPassYellow\", "
//				 + " 	( "
//				 + " 	select "
//				 + " 		discharge_summary_id "
//				 + " 	from "
//				 + " 		adt.m_discharge_summary mds "
//				 + " 	where "
//				 + " 		is_report_release = 'Y' and status='A' "
//				 + " 		and admission_id = admission.admission_id order by discharge_summary_id desc limit 1) as \"dischargeSummaryGreen\", "
//				 + "    ( "
//				 + " 	select "
//				 + " 		count(pir.id) "
//				 + " 	from "
//				 + " 		pharmacy.t_issue pir "
//				 + " 	inner join pharmacy.t_issue_detail id on "
//				 + " 		id.issue_id = pir.id "
//				 + " 	inner join pharmacy.t_prescription_detail pd on "
//				 + " 		id.prescription_detail_id = pd.id "
//				 + " 	where "
//				 + " 		pir.admission_id = discharge.admission_id "
//				 + " 		and pir.status_id in (2,1) "
//				 + " 		and pir.status_id not in (9,4) "
//				 + " 		and pd.is_discharge_medication = true) as \"dischargeMedicationRed\", "
//				/* + " 	( "
//				 + " 	select "
//				 + " 		count(pir.id) "
//				 + " 	from "
//				 + " 		pharmacy.t_issue pir "
//				 + " 	inner join pharmacy.t_issue_detail id on "
//				 + " 		id.issue_id = pir.id "
//				 + " 	inner join pharmacy.t_prescription_detail pd on "
//				 + " 		id.prescription_detail_id = pd.id "
//				 + " 	where "
//				 + " 		pir.admission_id = discharge.admission_id "
//				 + " 		and ((pir.status_id = 8 "
//				 + " 		or pir.status_id = 4) "
//				 + " 		and pir.status_id != 9) "
//				 + " 	and pd.is_discharge_medication = true) as \"dischargeMedicationGreen\", " */
//				 + " 	( "
//				 + " 	select "
//				 + " 		count(pir.id) "
//				 + " 	from "
//				 + " 		pharmacy.t_issue pir "
//				 + " 	inner join pharmacy.t_issue_detail id on "
//				 + " 		id.issue_id = pir.id "
//				 + " 	inner join pharmacy.t_prescription_detail pd on "
//				 + " 		id.prescription_detail_id = pd.id "
//				 + " 	where "
//				 + " 		pir.admission_id = discharge.admission_id "
//				 + " 		and pd.is_discharge_medication = true) as \"dischargeMedicationYellow\" "
//				 + " from "
//				 + " 	adt.t_discharge discharge "
//				 + " inner join adt.m_discharge_status_master disStatus on "
//				 + " 	discharge.discharge_status_id = disStatus.discharge_status_id "
//				 + " inner join adt.m_reason_for_discharge_master dtm on "
//				 + " 	discharge.discharge_type_id = dtm.discharge_type_id "
//				 + " inner join adt.t_admission as admission on "
//				 + " 	discharge.admission_id = admission.admission_id "
//				 + " inner join patient.t_patient_registration pr on "
//				 + " 	admission.patient_id = pr.patient_id "
//				 + " left join public.m_prefix_master prefxmstr on "
//				 + " 	prefxmstr.prefix_id = pr.prefix_id "
//				 + " inner join public.m_gender_master gm on "
//				 + " 	gm.gender_id = pr.gender_id "
//				 + " inner join public.m_gender_master gender on "
//				 + " 	pr.gender_id = gender.gender_id "
//				 + " inner join adt.t_admission_details admissionDetails on "
//				 + " 	admission.admission_id = admissionDetails.admission_id "
//				 + " inner join adt.m_bed_master bm on "
//				 + " 	admissionDetails.bed_id = bm.bed_id "
//				 + " left join adt.m_floor_master floor on "
//				 + " 	floor.floor_id = bm.floor_id "
//				 + " left join doctor.m_doctor_master doc on "
//				 + " 	discharge.doctor_id = doc.doctor_id "
//				 + " left join doctor.m_speciality_master sm on "
//				 + " 	admission.speciality_id = sm.speciality_id "
//				 + " where "
//				 + " 	discharge.organization_id =:orgId "
//				 + " 	and discharge.unit_id =:unitId "
//				 + " 	and admission.status = 'A' "
//				 + " 	and discharge.status = 'A' "
//				 + " 	and admissionDetails.status = 'A' "
//				 + "	and admission.visit_type_id=:visitTypeId "
//				 + " 	and admissionDetails.nursing_acceptance = 'Y' "
//				 + " 	and (discharge.discharge_status_id = 1 "
//				 + " 	or discharge.discharge_status_id = 2 ) "
//				 + "	and (bm.ward_id in (:wardList) "
//				 + " 	or bm.is_moving_bed = 'Y' ) "
//				 + " order by "
//				 + " 	discharge.admission_id desc " ),

		@NamedNativeQuery(name = "GET_PATIENT_READY_FOR_BILLING",
				query ="""
				select
					discharge.admission_id as "admissionId",
					admission.patient_id as "patientId",
					admission.t_patient_id as "tPatientId",
					admission.admission_number as "admissionNumber",
					admission.visit_type_id as "visitTypeId",
					admission.doctor_id as "doctorId",
					admission.speciality_id as "specialityId",
					sm.speciality_name as "specialityName",
					pr.uhid_number as "UHIDNumber",
					prefxmstr.prefix_code as "prefixDesc",
					pr.image_path as "imagePath",
					concat(
						extract(year from age(current_timestamp, pr.dob)),'Y ',
						extract(month from age(current_timestamp, pr.dob)),'M ',
						extract(day from age(current_timestamp, pr.dob)),'D '
					) as "ageStr",
					gm.gender_code as "genderCode",
					concat(coalesce(pr.first_name,''),' ',coalesce(pr.middle_name,''),' ',coalesce(pr.last_name,'')) as "pFirstName",
					to_char(pr.dob,'MM/dd/yyyy') as "dob",
					concat(coalesce(doc.first_name,''),' ',coalesce(doc.middle_name,''),' ',coalesce(doc.last_name,'')) as "dFirstName",
					bm.bed_number as "bedNumber",
					bm.floor_id as "floorId",
					coalesce(floor.floor_name,'') as "floorName",
					discharge.discharge_id as "dischargeId",
					discharge.discharge_type_id as "dischargeTypeId",
					discharge.discharge_status_id as "dischargeStatusId",
					disStatus.discharge_desc as "dischargeDesc",
					dtm.discharge_type_name as "dischargeTypeName",
					admissionDetails.er_bed_type_allocation as "erBedTypeAllocation",
					oc."labCountRed",
					oc."labCountYellow",
					oc."radiologyCountRed",
					oc."radiologyCountYellow",
					oc."otherSerCountRed",
					oc."otherSerCountYellow",
					sc."salesRed",
					sc."salesYellow",
					pc."procedureRed",
					pc."procedureYellow",
					ph."pharmecyRed",
					ph."pharmecyYellow",
					vc."visitorPassRed",
					vc."visitorPassYellow",
					ph."dischargeMedicationRed",
					ph."dischargeMedicationYellow"
					from adt.t_discharge discharge
					join adt.m_discharge_status_master disStatus
						on discharge.discharge_status_id = disStatus.discharge_status_id
					join adt.m_reason_for_discharge_master dtm
						on discharge.discharge_type_id = dtm.discharge_type_id
					join adt.t_admission admission
						on discharge.admission_id = admission.admission_id
					join patient.t_patient_registration pr
						on admission.patient_id = pr.patient_id
					left join public.m_prefix_master prefxmstr
						on prefxmstr.prefix_id = pr.prefix_id
					join public.m_gender_master gm
						on gm.gender_id = pr.gender_id
					join adt.t_admission_details admissionDetails
						on admission.admission_id = admissionDetails.admission_id
					join adt.m_bed_master bm
						on admissionDetails.bed_id = bm.bed_id
					left join adt.m_floor_master floor
						on floor.floor_id = bm.floor_id
					left join doctor.m_doctor_master doc
						on discharge.doctor_id = doc.doctor_id
					left join doctor.m_speciality_master sm
						on admission.speciality_id = sm.speciality_id
					left join lateral (
						select
							count(*) filter (
								where ser.speciality_id = :labDeptId
								and od.service_rendered = 0
								and od.service_is_billed = 0
								and od.status = 'A'
							) as "labCountRed",
							count(*) filter (
								where ser.speciality_id = :labDeptId
								and od.status = 'A'
							) as "labCountYellow",
							count(*) filter (
								where ser.speciality_id = :radDeptId
								and od.service_rendered = 0
								and od.service_is_billed = 0
								and od.status = 'A'
							) as "radiologyCountRed",
							count(*) filter (
								where ser.speciality_id = :radDeptId
								and od.status = 'A'
							) as "radiologyCountYellow",
							count(*) filter (
								where ser.speciality_id not in (:radDeptId,:labDeptId)
								and od.service_rendered = 0
								and od.service_is_billed = 0
								and od.status = 'A'
							) as "otherSerCountRed",
							count(*) filter (
								where ser.speciality_id not in (:radDeptId,:labDeptId)
								and od.status = 'A'
							) as "otherSerCountYellow"
						from public.t_order_master om
						join public.t_order_details od
							on om.order_id = od.order_id
						join service.m_service_master ser
							on od.service_id = ser.service_master_id
						where om.admission_id = discharge.admission_id
					) oc on true
					left join lateral (
						select
							count(*) filter (
								where status_id in (1,2) and status_id != 9
							) as "salesRed",
							count(*) as "salesYellow"
						from pharmacy.t_patient_issue_return pir
						where pir.admission_id = discharge.admission_id
					) sc on true
					left join lateral (
						select
							count(*) filter (
								where procedure_status_id not in (2,5,6,7,8,9)
							) as "procedureRed",
							count(*) filter (
								where procedure_status_id != 9
							) as "procedureYellow"
						from otims.t_procedure_req_mst om
						where om.admission_id = discharge.admission_id
					) pc on true
					left join lateral (
						select
							count(*) filter (
								where pir.status_id in (1,2)
								and pir.status_id not in (9,4)
								and pd.is_discharge_medication = false
							) as "pharmecyRed",
							count(*) filter (
								where pd.is_discharge_medication = false
							) as "pharmecyYellow",
							count(*) filter (
								where pir.status_id in (1,2)
								and pir.status_id not in (9,4)
								and pd.is_discharge_medication = true
							) as "dischargeMedicationRed",
							count(*) filter (
								where pd.is_discharge_medication = true
							) as "dischargeMedicationYellow"
						from pharmacy.t_issue pir
						join pharmacy.t_issue_detail id
							on id.issue_id = pir.id
						join pharmacy.t_prescription_detail pd
							on id.prescription_detail_id = pd.id
						where pir.admission_id = discharge.admission_id
					) ph on true
					left join lateral (
						select
							count(*) filter (
								where vpm.visitor_pass_status = 'I'
								and vpm.status = 'A'
							) as "visitorPassRed",
							count(*) as "visitorPassYellow"
						from adt.t_visitor_against_patient vp
						left join adt.t_visitor_patient_mapper vpm
							on vp.visitor_patient_id = vpm.visitor_patient_id
						where vp.admission_id = discharge.admission_id
					) vc on true
					where discharge.organization_id = :orgId
					and discharge.unit_id = :unitId
					and admission.status = 'A'
					and discharge.status = 'A'
					and admissionDetails.status = 'A'
					and admission.visit_type_id = :visitTypeId
					and admissionDetails.nursing_acceptance = 'Y'
					and discharge.discharge_status_id in (1,2)
					and (bm.ward_id in (:wardList) 
					         or bm.is_moving_bed = 'Y')
					order by discharge.admission_id desc"""),
		

		@NamedNativeQuery(name = "GET_PATIENT_FINAL_DISCHARGE_LIST", query = "SELECT DISTINCT(discharge.admission_id) as \"admissionId\", "
				+ " admission.patient_id as \"patientId\", " 
				+ " admission.t_patient_id as \"tPatientId\", "
				+ " admission.admission_number as \"admissionNumber\",  "
				+ " admission.visit_type_id as \"visitTypeId\", " 
				+ " admission.doctor_id as \"doctorId\",  "
				+ " admission.speciality_id as \"specialityId\", " 
				+ " admission.is_final_bill as \"isFinalBill\", " 
				+ " sm.speciality_name as \"specialityName\", "
				+ " pr.uhid_number as \"UHIDNumber\",  " 
				+ " gm.gender_code as \"genderCode\",  "
				+ " concat(COALESCE(pr.first_name,''),' ',COALESCE(pr.middle_name,''),' ',COALESCE(pr.last_name,'')) as \"pFirstName\" , "
				+ " to_char(pr.dob,'MM/dd/yyyy') as \"dob\",  "
				+ " concat(COALESCE(doc.first_name,''),' ', COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\","
				+ "  prefxmstr.prefix_code as \"prefixDesc\",pr.image_path as \"imagePath\" ," 
			 //   + " concat(to_char(extract(year from age(current_timestamp, pr.dob)), '999'), 'Y' , to_char(extract(year from age(current_timestamp, pr.dob)), '999'), 'M' , to_char(extract(year from age(current_timestamp, pr.dob)), '999'), 'D') as \"ageStr\", "
			    + " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\",  "
			    + " bm.bed_id as \"bedId\", " 
				+ " bm.bed_number as \"bedNumber\", " 
				+ " bm.floor_id as \"floorId\", "
				+ " bm.room_id as \"roomId\", "
				+ " bm.ward_id as \"wardId\", "
				+ " ward.ward_name as \"wardName\", "
				+ " ward.is_icu as \"isIcu\", "
				+ " bm.bed_category_id as \"bedCategoryId\", "
				//+ " nur.nursing_station_id as \"nursingStationId\", "
				+ " COALESCE(floor.floor_name,'') as \"floorName\",  " 
				+ " discharge.discharge_id as \"dischargeId\"," 
				+ " discharge.discharge_type_id as \"dischargeTypeId\", "
				+ " discharge.discharge_status_id as \"dischargeStatusId\", "
				+ " disStatus.discharge_desc as \"dischargeDesc\", "
				+ " dtm.discharge_type_name as \"dischargeTypeName\", "
				+ " admissionDetails.er_bed_type_allocation as \"erBedTypeAllocation\", "
				+ " admission.is_financial_creance as \"isFinancialClearance\" "
				//+ " (select referral_id from t_referal_details where admission_id = admission.admission_id and referral_type_id = 1) as \"referalDocId\", "
				//+ " coalesce(A.billStatusCount,0) as \"billStatusCount\" ,"
				//+ " coalesce(B.orderDetailsCount,0) as \"orderDetailsCount\"  "
				/*
				 * +
				 * " (select count(*) from billing.m_billing_master bill where admission_id=admission.admission_id and bill.bill_status_id = 1) as \"billStatusCount\", "
				 * +
				 * " (select count(*) from public.t_order_master oms INNER JOIN public.t_order_details odd on oms.order_id = odd.order_id where oms.admission_id=admission.admission_id and odd.service_is_billed = 0 and oms.visit_type_id = 2 and odd.ord_cancelled = 'N' ) as \"orderDetailsCount\" "
				 */+ " FROM adt.t_discharge discharge  "
				+ " INNER JOIN adt.m_discharge_status_master disStatus on discharge.discharge_status_id=disStatus.discharge_status_id "
				+ " INNER JOIN adt.m_reason_for_discharge_master dtm on discharge.discharge_type_id=dtm.discharge_type_id "
				+ " INNER JOIN adt.t_admission as admission on discharge.admission_id=admission.admission_id "
				+ " inner join patient.t_patient_registration pr on admission.patient_id=pr.patient_id "
                + " inner join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
				+ " inner join public.m_gender_master gm on gm.gender_id=pr.gender_id  "
				+ " inner join adt.t_admission_details admissionDetails on admission.admission_id=admissionDetails.admission_id "
				+ " LEFT join adt.m_bed_master bm on admissionDetails.bed_id=bm.bed_id "
				+ " LEFT join adt.m_ward_master as ward on bm.ward_id=ward.ward_id "
				//+ " LEFT JOIN adt.t_nursing_station_ward_mapper nur on nur.ward_id= ward.ward_id "
				+ " LEFT JOIN adt.m_floor_master floor on floor.floor_id=bm.floor_id "
				+ " LEFT JOIN doctor.m_doctor_master doc on discharge.doctor_id =doc.doctor_id "
				+ " LEFT JOIN doctor.m_speciality_master sm on admission.speciality_id=sm.speciality_id "
				/*+ " left join ( " + 
				"	select " + 
				"		count(*) billStatusCount , bill.admission_id  " + 
				"	from " + 
				"		billing.m_billing_master bill  " + 
				"		join adt.t_admission ta on bill.admission_id = ta.admission_id " + 
				"	where bill.bill_status_id = 3 " + 
				"	group by bill.admission_id " + 
				"	) as A on A.admission_id = admission.admission_id " + */
				/*" left join ( " + 
				"	select " + 
				"		count(*) orderDetailsCount , oms.admission_id " + 
				"	from " + 
				"		public.t_order_master oms " + 
				"		join public.t_order_details odd on oms.order_id = odd.order_id " + 
				"		join adt.t_admission ta on  oms.admission_id = ta.admission_id " + 
				"	where " + 
				"		odd.service_is_billed = 1 " + 
				"		and oms.visit_type_id = 2 " + 
				"		and odd.ord_cancelled = 'N'  " + 
				"		group by oms.admission_id " + 
				"		) as B on B.admission_id = admission.admission_id "*/
//				+ " WHERE discharge.admission_id NOT IN(select distinct sero.admission_id FROM adt.t_discharge dis "
//				+ " INNER JOIN public.t_order_master sero on dis.admission_id=sero.admission_id "
//				+ " INNER JOIN public.t_order_details od on sero.order_id = od.order_id "
//				+ " WHERE dis.status='A' "
//				+ " AND dis.unit_id =:unitId "
//				+ " AND od.service_rendered = 0 "
//				+ " AND od.service_is_billed = 0 "
//				+ " AND od.status='A' )  "
				
				+ " WHERE NOT EXISTS ( " + 
				"        SELECT " + 
				"            sero.admission_id " + 
				"        FROM " + 
				"            adt.t_discharge dis " + 
				"        INNER JOIN public.t_order_master sero ON " + 
				"            dis.admission_id = sero.admission_id " + 
				"        INNER JOIN public.t_order_details od ON  " + 
				"            sero.order_id = od.order_id  " + 
				"        WHERE  " + 
				"            dis.status = 'A'" + 
				"            AND dis.unit_id =:unitId" + 
				"            AND od.service_rendered = 0  " + 
				"            AND od.service_is_billed = 0 " + 
				"            AND od.status = 'A' " + 
				"            AND dis.admission_id = discharge.admission_id) " 
				+ " AND discharge.organization_id =:orgId "
				+ " AND discharge.status='A' "
				+ " AND discharge.unit_id =:unitId " 
				+ " AND discharge.discharge_status_id = 3 " 
				+ " AND admissionDetails.status='A' "
//				+ " AND  admission.visit_type_id = :visitTypeId "
				+ " AND admissionDetails.nursing_acceptance = 'Y' "
				+ " AND (bm.ward_id IN (:wardList) OR bm.is_moving_bed='Y' ) "
				+ " ORDER BY discharge.discharge_id desc "
				),
		
		
		@NamedNativeQuery(name = "GET_PATIENT_FINAL_DISCHARGE_LIST_NEW_FOR_LIST_AND_COUNT", query = "SELECT DISTINCT(discharge.admission_id) as \"admissionId\", "
				+ " admission.patient_id as \"patientId\", " 
				+ " admission.t_patient_id as \"tPatientId\", "
				+ " admission.admission_number as \"admissionNumber\",  "
				+ " admission.visit_type_id as \"visitTypeId\", " 
				+ " visitTypeMaster.visit_type_code as \"visitTypeName\", "
				+ " admission.doctor_id as \"doctorId\",  "
				+ " admission.speciality_id as \"specialityId\", " 
				+ " admission.is_final_bill as \"isFinalBill\", " 
				+ " sm.speciality_name as \"specialityName\", "
				+ " pr.uhid_number as \"UHIDNumber\",  " 
				+ " gm.gender_code as \"genderCode\",  "
				+ " concat(COALESCE(pr.first_name,''),' ',COALESCE(pr.middle_name,''),' ',COALESCE(pr.last_name,'')) as \"pFirstName\" , "
				+ " to_char(pr.dob,'MM/dd/yyyy') as \"dob\",  "
				+ " concat(COALESCE(doc.first_name,''),' ', COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\","
				+ "  prefxmstr.prefix_code as \"prefixDesc\",pr.image_path as \"imagePath\" ," 
			 //   + " concat(to_char(extract(year from age(current_timestamp, pr.dob)), '999'), 'Y' , to_char(extract(year from age(current_timestamp, pr.dob)), '999'), 'M' , to_char(extract(year from age(current_timestamp, pr.dob)), '999'), 'D') as \"ageStr\", "
			    + " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\",  "
			    + " bm.bed_id as \"bedId\", " 
				+ " bm.bed_number as \"bedNumber\", " 
				+ " bm.floor_id as \"floorId\", "
				+ " bm.room_id as \"roomId\", "
				+ " bm.ward_id as \"wardId\", "
				+ " ward.ward_name as \"wardName\", "
				+ " ward.is_icu as \"isIcu\", "
				+ " bm.bed_category_id as \"bedCategoryId\", "
				//+ " nur.nursing_station_id as \"nursingStationId\", "
				+ " COALESCE(floor.floor_name,'') as \"floorName\",  " 
				+ " discharge.discharge_id as \"dischargeId\"," 
				+ " discharge.discharge_type_id as \"dischargeTypeId\", "
				+ " discharge.discharge_status_id as \"dischargeStatusId\", "
				//+ " disStatus.discharge_desc as \"dischargeDesc\", "
				+ " dtm.discharge_type_name as \"dischargeTypeName\", "
				+ " admissionDetails.er_bed_type_allocation as \"erBedTypeAllocation\", "
				+ " admission.is_financial_creance as \"isFinancialClearance\" "
				//+ " (select referral_id from t_referal_details where admission_id = admission.admission_id and referral_type_id = 1) as \"referalDocId\", "//HIS-32077 Query Optimization
				//+ " coalesce(A.billStatusCount,0) as \"billStatusCount\" ,"//HIS-32077 Query Optimization
				//+ " coalesce(B.orderDetailsCount,0) as \"orderDetailsCount\"  "//HIS-32077 Query Optimization
				/*
				 * +
				 * " (select count(*) from billing.m_billing_master bill where admission_id=admission.admission_id and bill.bill_status_id = 1) as \"billStatusCount\", "
				 * +
				 * " (select count(*) from public.t_order_master oms INNER JOIN public.t_order_details odd on oms.order_id = odd.order_id where oms.admission_id=admission.admission_id and odd.service_is_billed = 0 and oms.visit_type_id = 2 and odd.ord_cancelled = 'N' ) as \"orderDetailsCount\" "
				 */+ " FROM adt.t_discharge discharge  "
				 + " INNER JOIN adt.t_admission as admission on discharge.admission_id=admission.admission_id "
				 + " INNER JOIN public.m_visit_type_master as visitTypeMaster on admission.visit_type_id=visitTypeMaster.visit_type_id "
				 + " inner join adt.t_admission_details admissionDetails on admission.admission_id=admissionDetails.admission_id "
				 + " inner join patient.t_patient_registration pr on admission.patient_id=pr.patient_id "
				//+ " INNER JOIN adt.m_discharge_status_master disStatus on discharge.discharge_status_id=disStatus.discharge_status_id "
				+ " INNER JOIN adt.m_reason_for_discharge_master dtm on discharge.discharge_type_id=dtm.discharge_type_id "
				/*" left join ( " + 
				"	select " + 
				"		count(*) orderDetailsCount , oms.admission_id " + //HIS-32077 Query Optimization
				"	from " + 
				"		public.t_order_master oms " + 
				"		join public.t_order_details odd on oms.order_id = odd.order_id " + 
				"		join adt.t_admission ta on  oms.admission_id = ta.admission_id " + 
				"	where " + 
				"		odd.service_is_billed = 1 " + 
				"		and oms.visit_type_id = 2 " + 
				"		and odd.ord_cancelled = 'N'  " + 
				"		group by oms.admission_id " + 
				"		) as B on B.admission_id = admission.admission_id "*/
				+ " LEFT join adt.m_bed_master bm on admissionDetails.bed_id=bm.bed_id "
				+ " LEFT join adt.m_ward_master as ward on bm.ward_id=ward.ward_id "
				//+ " LEFT JOIN adt.t_nursing_station_ward_mapper nur on nur.ward_id= ward.ward_id "
				+ " LEFT JOIN adt.m_floor_master floor on floor.floor_id=bm.floor_id "
				+ " LEFT JOIN doctor.m_doctor_master doc on discharge.doctor_id =doc.doctor_id "
				+ " LEFT JOIN doctor.m_speciality_master sm on admission.speciality_id=sm.speciality_id  "
				/*+ " left join ( " + 
				"	select " + 
				"		count(*) billStatusCount , bill.admission_id  " + 
				"	from " + 
				"		billing.m_billing_master bill  " + 
				//"		join adt.t_admission ta on bill.admission_id = ta.admission_id " + 
				"	where bill.bill_status_id = 3 " + 
				"	group by bill.admission_id " + 
				"	) as A on A.admission_id = admission.admission_id " */ //HIS-32077 Query Optimization
				+ " inner join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
				+ " inner join public.m_gender_master gm on gm.gender_id=pr.gender_id  "
				//+ " WHERE dis.status='A' "
			/*	+ " WHERE discharge.admission_id NOT IN(select distinct sero.admission_id FROM adt.t_discharge dis "
				+ " INNER JOIN public.t_order_master sero on dis.admission_id=sero.admission_id "
				+ " INNER JOIN public.t_order_details od on sero.order_id = od.order_id "
				+ " WHERE dis.status='A' "
				+ " AND dis.unit_id =:unitId "
				+ " AND od.service_rendered = 0 "
				+ " AND od.service_is_billed = 0 "
				+ " AND od.status='A' )  " */
				+ " WHERE NOT EXISTS ( " + 
				"        SELECT " + 
				"            sero.admission_id " + 
				"        FROM " + 
				"            adt.t_discharge dis " + 
				"        INNER JOIN public.t_order_master sero ON " + 
				"            dis.admission_id = sero.admission_id " + 
				"        INNER JOIN public.t_order_details od ON  " + 
				"            sero.order_id = od.order_id  " + 
				"        WHERE  " + 
				"            dis.status = 'A'" + 
				"            AND dis.unit_id =:unitId" + 
				"            AND od.service_rendered = 0  " + 
				"            AND od.service_is_billed = 0 " + 
				"            AND od.status = 'A' " + 
				"            AND dis.admission_id = discharge.admission_id) " + 
				"         "  
				+ " AND discharge.organization_id =:orgId "
				+ " AND discharge.status='A' "
				+ " AND discharge.unit_id =:unitId " 
				+ " AND discharge.discharge_status_id = 3 " 
				+ " AND admissionDetails.status='A' "
				+ " AND  admission.visit_type_id = :visitTypeId "
				+ " AND admissionDetails.nursing_acceptance = 'Y' "
				+ " AND (bm.ward_id IN (:wardList) OR bm.is_moving_bed='Y' ) "
				+ " ORDER BY discharge.discharge_id desc "
				),


		@NamedNativeQuery(name = "GET_DISCHARGED_PATIENT_LIST_AND_COUNT", query = "SELECT DISTINCT(discharge.admission_id) as \"admissionId\", "
				+ " admission.patient_id as \"patientId\", " 
				+ " admission.t_patient_id as \"tPatientId\", "
				+ " admission.admission_number as \"admissionNumber\",  "
				+ " admission.visit_type_id as \"visitTypeId\", " 
				+ " admission.doctor_id as \"doctorId\",  "
				+ " admission.speciality_id as \"specialityId\", " 
				+ " admission.is_final_bill as \"isFinalBill\", " 
				+ " sm.speciality_name as \"specialityName\", "
				+ " pr.uhid_number as \"UHIDNumber\",  " 
				+ " gm.gender_code as \"genderCode\",  "
				+ " gm.gender_id as \"genderId\",  "
				+ " concat(COALESCE(pr.first_name,''),' ',COALESCE(pr.middle_name,''),' ',COALESCE(pr.last_name,'')) as \"pFirstName\" , "
				+ " to_char(pr.dob,'MM/dd/yyyy') as \"dob\",  "
				+ "to_char(admissionDetails.doa,'dd/MM/yyyy') as \"doa\", " 
				+ "to_char(admissionDetails.pdd,'dd/MM/yyyy') as \"pdd\", "
				+ " concat(COALESCE(doc.first_name,''),' ', COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\","
				+ "  prefxmstr.prefix_code as \"prefixDesc\",pr.image_path as \"imagePath\" ," 
			    + " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\",  "
			    + " bm.bed_id as \"bedId\", " 
				+ " bm.bed_number as \"bedNumber\", " 
				+ " bm.floor_id as \"floorId\", "
				+ " bm.room_id as \"roomId\", "
				+ " bm.ward_id as \"wardId\", "
				+ " ward.ward_name as \"wardName\", "
				+ " ward.is_icu as \"isIcu\", "
				+ " bm.bed_category_id as \"bedCategoryId\", "
				+ " COALESCE(floor.floor_name,'') as \"floorName\",  " 
				+ " discharge.discharge_id as \"dischargeId\"," 
				+ " discharge.discharge_type_id as \"dischargeTypeId\", "
				+ " discharge.discharge_status_id as \"dischargeStatusId\", "
				+ " disStatus.discharge_desc as \"dischargeDesc\", "
				+ " dtm.discharge_type_name as \"dischargeTypeName\", "
				+ " admissionDetails.er_bed_type_allocation as \"erBedTypeAllocation\", "
				+ " admission.is_financial_creance as \"isFinancialClearance\","
				+ " disSumm.discharge_summary_id as \"dischargeSummaryId\",disSumm.discharge_template_id as \"dischargeTemplateId\","
				+ " disTemp.is_html as \"isHtml\" "
				+ " FROM adt.t_discharge discharge  "
				+ " INNER JOIN adt.m_discharge_status_master disStatus on discharge.discharge_status_id=disStatus.discharge_status_id "
				+ " INNER JOIN adt.m_reason_for_discharge_master dtm on discharge.discharge_type_id=dtm.discharge_type_id "
				+ " INNER JOIN adt.t_admission as admission on discharge.admission_id=admission.admission_id "
				+ " inner join patient.t_patient_registration pr on admission.patient_id=pr.patient_id "
                + " inner join public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
				+ " inner join public.m_gender_master gm on gm.gender_id=pr.gender_id  "
				+ " inner join adt.t_admission_details admissionDetails on admission.admission_id=admissionDetails.admission_id "
				+ " LEFT join adt.m_bed_master bm on admissionDetails.bed_id=bm.bed_id "
				+ " LEFT join adt.m_ward_master as ward on bm.ward_id=ward.ward_id "
				+ " LEFT JOIN adt.m_floor_master floor on floor.floor_id=bm.floor_id "
				+ " LEFT JOIN doctor.m_doctor_master doc on discharge.doctor_id =doc.doctor_id "
				+ " LEFT JOIN doctor.m_speciality_master sm on admission.speciality_id=sm.speciality_id  "
				+ "	left join adt.m_discharge_summary disSumm on disSumm.admission_id=admission.admission_id AND disSumm.status='A' "
				+ " left join adt.m_discharge_summary_template_master disTemp on disTemp.discharge_id = disSumm.discharge_template_id " 
				+ " WHERE discharge.organization_id =:orgId "
				+ " AND discharge.unit_id =:unitId "
				+ " AND discharge.status ='A'"
				//+ "  " 
				//+ " AND discharge.discharge_status_id = 5 " 
				+ " AND admissionDetails.admission_details_id=(select max(tad.admission_details_id) from adt.t_admission_details tad  where tad.admission_id =admission.admission_id ) "
			//	+ " ORDER BY discharge.discharge_id desc "
				),
		
@NamedNativeQuery(name = "UPDATE_DISCHARGE_STATUS_BY_ADMISSION_ID_IF_FINAL_BILL",
				 query = "update adt.t_discharge set discharge_status_id =:statusId "
				 		+"where admission_id =:admissionId "),

@NamedNativeQuery(name="GET_LIST_OF_ER_PATIENTS",
query=" SELECT dmmm.code as \"code\",dmmm.is_myocardial_infarction as \"stemi\",dmmm.is_acute_ischemic_stroke "
		+ " as \"stroke\",dmmm.is_trauma as \"truma\",dmmm.is_sepsis as \"sepsis\",dmmm.is_shock "
		+ " as \"shock\" ,history.current_triage_id AS \"currentTriage\","
		+ " adl.admission_details_id as \"admissionDetailsId\",admission.admission_id as \"admissionId\",  "
		+ "admission.patient_id as \"patientId\",  "
		+ "admission.t_patient_id as \"tPatientId\",  "
		+ "admission.admission_number as \"admissionNumber\",  "
		+ "admission.visit_type_id as \"visitTypeId\",  " 
		+ "admission.doctor_id as \"doctorId\",   "
        + " admission.is_final_bill as \"isFinalBill\", "
		+ "pr.uhid_number as \"UHIDNumber\",  "
		+ "pr.image_path as \"imagePath\", "
		+ "concat(coalesce(pre.prefix_code,''),' ',COALESCE(pr.first_name,''),' ',COALESCE(pr.middle_name,''),' ',COALESCE(pr.last_name,'')) as \"pFirstName\",  "
		+ "pr.gender_id as \"genderId\",   " + "gm.gender_code as \"genderCode\",  "
		+ "to_char(pr.dob,'MM/dd/yyyy') as \"dob\",  "
		+ "concat( extract( year from AGE( current_timestamp, pr.dob )), 'Y ', extract( month from AGE( current_timestamp, pr.dob )), 'M ', extract( day from AGE( current_timestamp, pr.dob )), 'D ' ) as \"ageStr\","
		//+ "concat(COALESCE(doc.first_name,''),' ', COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\",  "
		+ "to_char(adl.admission_date_time,'dd/MM/yyyy HH24:MI') as \"doa\",  "
//
		+ "to_char(adl.admission_date_time + interval '4 hours', 'DD/MM/YYYY HH24:MI') as \"pdd\",  "
		+ "bedCatMst.bed_category_desc as \"bedCategoryDesc\", " 
		+ "bm.bed_number as \"bedNumber\",  "
		+ "pr.prefix_id as \"prefixId\", "
		+ "pr.uuid as \"uuid\", "
		+ "disc.discharge_status_id as \"dischargeStatusId\", "
		+ "medleg.medico_legal_id as \"medicoLegalId\", " 
		+ "bm.bed_id as \"bedId\", "
		+ "bm.bed_code as \"bedCode\", "
		+" wrdmst.ward_name as \"wardName\", "
		+" wrdmst.ward_id as \"wardId\", "
		+ "psd.contract_id as \"contractId\", "
		+" rmmst.room_id as \"roomId\", "
		+" rmmst.er_category_id as \"erCategoryId\", "
		+" rmmst.room_desc as \"roomDesc\",  "
		+ "adl.triage_category_id as \"triageCategoryId\" ,pr.patient_category_id as \"patientCategoryId\"," 
        +" bedCatMst.is_icu as \"isIcuu\", "
		+" bedCatMst.billing_bed_category_id as \"billingBedCategoryId\" , psd.payment_entitlement_id as \"paymentEntitlementId\", "
		+" 	id.identification_name as \"identificationName\", "
		+" 	coalesce(pr.identification_number, '') as \"identificationNo\", "
		+ " 	pr.mobile_number as \"mobileNo\", "
		+ "to_char(pr.dob,'dd/MM/yyyy') as \"dateOfBirth\",  "
		+ " 	gm.gender_name as \"genderName\" "
//		+ "	(case when id.is_expiry_required='Y' then "
//		+ "		to_char(pr.identification_expiry_date, 'YYYY-MM-DD') end) as \"identificationExpiryDate\" "
		//+ "doc.speciality_id as \"specialityId\" "
		+ "FROM adt.t_admission admission  "
		+ "INNER JOIN patient.t_patient_registration pr on admission.patient_id=pr.patient_id  "
		+ "left join m_prefix_master pre on pre.prefix_id = pr.prefix_id "
		+ "INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id  "
		//+ "INNER JOIN doctor.m_doctor_master doc on admission.doctor_id=doc.doctor_id  "
		+ "INNER JOIN adt.t_admission_details adl on adl.admission_id=admission.admission_id   "
		+" LEFT join  patient.t_patient_diagnosis_appo_mapper pda on pda.admission_id=adl.admission_id  left join emr.m_diagnosis_master dmmm on "
		+" pda.diagnosis_id=dmmm.diagnosis_id "
		+ "LEFT JOIN adt.m_bed_master bm on adl.bed_id=bm.bed_id  "
		+ "LEFT JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
		+ "LEFT join adt.t_discharge disc on admission.admission_id = disc.admission_id "
		+ "left join adt.t_medico_legal medleg on admission.admission_id = medleg.admission_id " 
		+ "left join adt.m_ward_master wrdmst on adl.ward_id=wrdmst.ward_id "
		+ "left join adt.m_room_master rmmst on rmmst.room_id= adl.room_id "
		+ " left join opd.t_patient_sponsor_details psd on psd.admission_id = admission.admission_id and psd.priority_id=1 and psd.status='A' "
		+ " left join adt.m_identification_master as id on "
		+ " 	id.identification_id = pr.identification_type_id "
		+ "   LEFT JOIN (" +
		"    SELECT DISTINCT ON (admission_details_id) " +
		"           admission_details_id, " +
		"           current_triage_id " +
		"    FROM adt.t_admission_triage_history " +
		"    ORDER BY admission_details_id, updated_date DESC " +
		" ) history  " +
		"  ON adl.admission_details_id = history.admission_details_id "
		+ "WHERE admission.status='A'  " 
		+ "AND adl.status='A'  " 
		+ "AND admission.organization_id=:organizationId " + "AND admission.unit_id=:unitId "
		+ "AND admission.visit_type_id=4  "
		+ " and bm.bed_number  is not null "
		+" AND admission.t_patient_id is null "
		),
@NamedNativeQuery(name = "GET_PATIENT_DISCHARGED_LIST", query = "SELECT DISTINCT(discharge.admission_id) as \"admissionId\"," 
+   " admission.patient_id as \"patientId\",  " + 
		" admission.t_patient_id as \"tPatientId\",  " + 
		" admission.admission_number as \"admissionNumber\",   " + 
		" admission.visit_type_id as \"visitTypeId\",  " + 
		" admission.doctor_id as \"doctorId\",   " + 
		" admission.speciality_id as \"specialityId\",  " + 
		" admission.is_final_bill as \"isFinalBill\",  " + 
		" sm.speciality_name as \"specialityName\",  " + 
		" pr.uhid_number as \"UHIDNumber\",   " + 
		" gm.gender_code as \"genderCode\",   " + 
		" concat(coalesce(pre.prefix_code,''),' ',COALESCE(pr.first_name,''),' ',COALESCE(pr.middle_name,''),' ',COALESCE(pr.last_name,'')) as \"pFirstName\" ,  " + 
		" to_char(pr.dob,'MM/dd/yyyy') as \"dob\",   " + 
		" concat(COALESCE(doc.first_name,''),' ', COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\",  " + 
		" bm.bed_id as \"bedId\",  " + 
		" bm.bed_number as \"bedNumber\",  " + 
		" bm.floor_id as \"floorId\",  " + 
		" bm.room_id as \"roomId\",  " + 
		" bm.ward_id as \"wardId\",  " + 
		" ward.ward_name as \"wardName\",  " + 
		" ward.is_icu as \"isIcu\",  " + 
		" dissum.discharge_summary_id as \"dischargeSummaryId\","+
		" bm.bed_category_id as \"bedCategoryId\",  " + 
		" COALESCE(floor.floor_name,'') as \"floorName\",   " + 
		" discharge.discharge_id as \"dischargeId\", " + 
		" discharge.discharge_type_id as \"dischargeTypeId\",  " + 
		" discharge.discharge_status_id as \"dischargeStatusId\",  " + 
		" disStatus.discharge_desc as \"dischargeDesc\",  " + 
		" dtm.discharge_type_name as \"dischargeTypeName\",  " + 
		" admission.is_financial_creance as \"isFinancialClearance\",  " + 
		" (select referral_id from t_referal_details where admission_id = admission.admission_id and referral_type_id = 1) as \"referalDocId\",  " + 
		" (select count(*) from billing.m_billing_master bill where admission_id=admission.admission_id and bill.bill_status_id = 1) as \"billStatusCount\",  " + 
		" (select count(*) from public.t_order_master oms INNER JOIN public.t_order_details odd on oms.order_id = odd.order_id where oms.admission_id=admission.admission_id and odd.service_is_billed = 0 and oms.visit_type_id = 2 and odd.ord_cancelled = 'N' ) as \"orderDetailsCount\"  " + 
		" FROM adt.t_discharge discharge   " + 
		" INNER JOIN adt.m_discharge_status_master disStatus on discharge.discharge_status_id=disStatus.discharge_status_id  " + 
		" INNER JOIN adt.m_reason_for_discharge_master dtm on discharge.discharge_type_id=dtm.discharge_type_id  " + 
		" INNER JOIN adt.t_admission as admission on discharge.admission_id=admission.admission_id  " + 
		" inner join patient.t_patient_registration pr on admission.patient_id=pr.patient_id  " + 
		" left join m_prefix_master pre on pre.prefix_id = pr.prefix_id " +
		" inner join public.m_gender_master gm on gm.gender_id=pr.gender_id   " + 
		" inner join adt.t_admission_details admissionDetails on admission.admission_id=admissionDetails.admission_id  " + 
		" LEFT join adt.m_bed_master bm on bm.bed_id = (select bed_id from adt.t_admission_details where admission_id=discharge.admission_id order by 1 desc limit 1) " + 
		" LEFT join adt.m_ward_master as ward on bm.ward_id=ward.ward_id  " + 
		" LEFT JOIN adt.m_floor_master floor on floor.floor_id=bm.floor_id  " + 
		" LEFT JOIN doctor.m_doctor_master doc on discharge.doctor_id =doc.doctor_id  " + 
		" LEFT JOIN doctor.m_speciality_master sm on admission.speciality_id=sm.speciality_id   "+
	    " left join adt.m_discharge_summary dissum on admission.admission_id = dissum.admission_id AND dissum.status='A' " + 
		" WHERE discharge.organization_id =:orgId  " + 
		" AND discharge.unit_id =:unitId " + 
		" AND discharge.discharge_status_id = 5  " + 
		" and discharge.created_date between :fromDate and :toDate " 
	
		),

@NamedNativeQuery(name = "GET_PATIENT_DISCHARGED_LIST_FOR_PRINT", query = "SELECT DISTINCT(discharge.admission_id) as \"admissionId\"," 
+   " admission.patient_id as \"patientId\",  " + 
		" admission.t_patient_id as \"tPatientId\",  " + 
		" admission.admission_number as \"admissionNumber\",   " + 
		" admission.visit_type_id as \"visitTypeId\",  " + 
		" admission.doctor_id as \"doctorId\",   " + 
		" admission.speciality_id as \"specialityId\",  " + 
		" admission.is_final_bill as \"isFinalBill\",  " + 
		" sm.speciality_name as \"specialityName\",  " + 
		" pr.uhid_number as \"UHIDNumber\",   " + 
		" gm.gender_code as \"genderCode\",   " + 
		" concat(coalesce(pre.prefix_code,''),' ',COALESCE(pr.first_name,''),' ',COALESCE(pr.middle_name,''),' ',COALESCE(pr.last_name,'')) as \"pFirstName\" ,  " + 
		" to_char(pr.dob,'MM/dd/yyyy') as \"dob\",   " + 
		" concat(COALESCE(doc.first_name,''),' ', COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\",  " + 
		" bm.bed_id as \"bedId\",  " + 
		" bm.bed_number as \"bedNumber\",  " + 
		" bm.floor_id as \"floorId\",  " + 
		" bm.room_id as \"roomId\",  " + 
		" bm.ward_id as \"wardId\",  " + 
		" ward.ward_name as \"wardName\",  " + 
		" ward.is_icu as \"isIcu\",  " + 
		" dissum.discharge_summary_id as \"dischargeSummaryId\","+
		" bm.bed_category_id as \"bedCategoryId\",  " + 
		" COALESCE(floor.floor_name,'') as \"floorName\",   " + 
		" discharge.discharge_id as \"dischargeId\", " + 
		" discharge.discharge_type_id as \"dischargeTypeId\",  " + 
		" discharge.discharge_status_id as \"dischargeStatusId\",  " + 
		" disStatus.discharge_desc as \"dischargeDesc\",  " + 
		" dtm.discharge_type_name as \"dischargeTypeName\",  " + 
		" admission.is_financial_creance as \"isFinancialClearance\",  " + 
		" (select referral_id from t_referal_details where admission_id = admission.admission_id and referral_type_id = 1) as \"referalDocId\",  " + 
		" (select count(*) from billing.m_billing_master bill where admission_id=admission.admission_id and bill.bill_status_id = 1) as \"billStatusCount\",  " + 
		" (select count(*) from public.t_order_master oms INNER JOIN public.t_order_details odd on oms.order_id = odd.order_id where oms.admission_id=admission.admission_id and odd.service_is_billed = 0 and oms.visit_type_id = 2 and odd.ord_cancelled = 'N' ) as \"orderDetailsCount\"  " + 
		" FROM adt.t_discharge discharge   " + 
		" INNER JOIN adt.m_discharge_status_master disStatus on discharge.discharge_status_id=disStatus.discharge_status_id  " + 
		" INNER JOIN adt.m_reason_for_discharge_master dtm on discharge.discharge_type_id=dtm.discharge_type_id  " + 
		" INNER JOIN adt.t_admission as admission on discharge.admission_id=admission.admission_id  " + 
		" inner join patient.t_patient_registration pr on admission.patient_id=pr.patient_id  " + 
		" left join m_prefix_master pre on pre.prefix_id = pr.prefix_id " +
		" inner join public.m_gender_master gm on gm.gender_id=pr.gender_id   " + 
		" inner join adt.t_admission_details admissionDetails on admission.admission_id=admissionDetails.admission_id  " + 
		" LEFT join adt.m_bed_master bm on bm.bed_id = (select bed_id from adt.t_admission_details where admission_id=discharge.admission_id order by 1 desc limit 1) " + 
		" LEFT join adt.m_ward_master as ward on bm.ward_id=ward.ward_id  " + 
		" LEFT JOIN adt.m_floor_master floor on floor.floor_id=bm.floor_id  " + 
		" LEFT JOIN doctor.m_doctor_master doc on discharge.doctor_id =doc.doctor_id  " + 
		" LEFT JOIN doctor.m_speciality_master sm on admission.speciality_id=sm.speciality_id   "+
	    " left join adt.m_discharge_summary dissum on admission.admission_id = dissum.admission_id and dissum.status='A'  " + 
		" WHERE discharge.organization_id =:orgId  " + 
		" AND discharge.unit_id =:unitId  " + 
		" AND discharge.discharge_status_id = 5  " + 
		" and discharge.created_date between :fromDate and :toDate " + 
	    "AND admission.visit_type_id IN (:visitTypeIds)  "
	
		),
	
     @NamedNativeQuery(name = "GET_PATIENT_LIST_FOR_DISCHARGE_SUMMARY", query = "SELECT  dis.discharge_id as \"dischargeId\", "
				+ "dis.discharge_type_id as \"dischargeTypeId\", " 
				+ "disStatus.discharge_desc as \"dischargeDesc\", "
				+ "dis.discharge_status_id as \"dischargeStatusId\", "
				+ "to_char(dis.discharge_date,'dd/MM/yyyy') as \"futureDischargeDate\", "
				+ "admission.admission_id as \"admissionId\", " 
				+ "admission.patient_id as \"patientId\", "
				+ "admission.t_patient_id as \"tPatientId\", " 
				+ "admission.admission_number as \"admissionNumber\", "
				+ "admission.visit_type_id as \"visitTypeId\", " 
				+ "admission.doctor_id as \"doctorId\", "
				+ "admission.speciality_id as \"specialityId\", " 
				+ "sp.speciality_name as \"specialityName\", "
				+ "pr.uhid_number as \"UHIDNumber\", " 
				+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as  \"ageString\", "
				+ "concat(coalesce(pre.prefix_code,''),' ',COALESCE(pr.first_name,''),' ',COALESCE(pr.middle_name,''),' ',COALESCE(pr.last_name,'')) as \"pFirstName\" , "
				+ "pr.gender_id as \"genderId\", "
				+ "pr.image_path as \"imagePath\", "
				+ "gm.gender_code as \"genderCode\", "
				+ "to_char(pr.dob,'MM/dd/yyyy') as \"dob\", "
				+ "  pr.is_vip as \"isVip\", "
				+ " concat(COALESCE(doc.first_name,''),' ', COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
				+ "to_char(adl.doa,'dd/MM/yyyy') as \"doa\", " 
				+ "to_char(adl.pdd,'dd/MM/yyyy') as \"pdd\", "
				+ "spd.payment_entitlement_id as \"paymentEntitlementId\", "
				+ "pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
				+ "bedCatMst.bed_category_desc as \"bedCategoryDesc\", " 
				+ "bm.bed_number as \"bedNumber\", " 
				+ "wm.ward_name as \"wardName\", "
				+ "fm.floor_name as \"floorName\", "
				+ "bm.bed_id as \"bedId\", "
				+ "dtm.discharge_type_name as \"dischargeTypeName\" ,disCharge.discharge_summary_id as \"dischargeSummaryId\","
				+ "  (case when (select total_score from adt.t_mews_mapper where admission_id = admission.admission_id and status='A') > 6 then "
				+ "  	1 "
				+ "   else "
				+ "     0 end) as \"isCriticalOrder\" "
				//+ "FROM adt.t_discharge dis "
				//changed as per nived requirement discharge initiate not required
				+" FROM adt.t_admission admission "
				+ "LEFT JOIN adt.t_discharge dis on dis.admission_id=admission.admission_id and dis.status='A' "
				+ "LEFT JOIN adt.m_discharge_status_master disStatus on dis.discharge_status_id=disStatus.discharge_status_id "
				+ "LEFT JOIN adt.m_reason_for_discharge_master dtm on dis.discharge_type_id=dtm.discharge_type_id "
				//+ "INNER JOIN adt.t_admission admission on admission.admission_id = dis.admission_id "
				+ "INNER JOIN patient.t_patient_registration pr on admission.patient_id=pr.patient_id "
				+ "INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id "
				+ " left join m_prefix_master pre on pre.prefix_id = pr.prefix_id "
				//+ "LEFT JOIN doctor.m_doctor_master doc on admission.doctor_id=doc.doctor_id "
				+ " LEFT JOIN doctor.m_doctor_master doc on doc.doctor_id=(case when (admission.doctor_id is null or admission.doctor_id=0) then :doctorId else  admission.doctor_id end )"
				//+ "LEFT JOIN doctor.m_speciality_master sp on admission.speciality_id=sp.speciality_id"
				+ " LEFT JOIN doctor.m_speciality_master sp on sp.speciality_id=(case when admission.speciality_id is null then " 
				+"(select app.er_speciality_id from t_unit_application_billing_configuration app where app.organization_id=:orgId and app.unit_id=:unitId)  else admission.speciality_id end) "
				+ "INNER JOIN adt.t_admission_details adl on adl.admission_id=admission.admission_id  "
				+ "LEFT JOIN adt.m_bed_master bm on adl.bed_id=bm.bed_id "
				+ "LEFT JOIN adt.m_ward_master wm on bm.ward_id=wm.ward_id "
				+ "LEFT JOIN adt.m_floor_master fm on wm.floor_id=fm.floor_id "
				+ "LEFT JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 AND spd.status='A' "
				+ "LEFT JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
				+ "LEFT JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
				+ "	left join adt.m_discharge_summary disCharge on disCharge.admission_id=admission.admission_id and disCharge.status='A' " 
				+ " WHERE admission.status='A' " 
				+ " AND adl.status='A' "
				//+ "and disCharge.status='A' "
				+ " AND adl.nursing_acceptance = 'Y' "
				+ "AND admission.organization_id=:orgId  " 
				+ "AND admission.unit_id=:unitId "
				//+ "and dis.status='A' and dis.discharge_status_id in (1,2,3) "
				//+ "AND (bm.ward_id IN (:wardList) OR bm.is_moving_bed='Y' ) "
				//+ "AND (bm.ward_id IN (:wardList) "
				//+ " order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc "
				//+ "order by dis.discharge_id desc "
     )
})

@NamedQueries({
	
	@NamedQuery(name="GET_PATIENT_DISCHARGE_OR_NOT",query="SELECT count(*) as dischCount "
			+ "FROM Discharge adm "
			+ "WHERE adm.admissionId=:admissionId "
			+ "AND adm.status='A' "
			+ "AND adm.unitId=:unitId "
			+ "AND adm.organizationId=:organizationId "
			+ "AND adm.dischargeStatusId !=5 "),
	
	@NamedQuery(name="CHECK_PATIENT_DISCHARGE_OR_NOT",query="SELECT count(*) as dischCount "
			+ "FROM Discharge adm "
			+ "WHERE adm.admissionId=:admissionId "
			+ "AND adm.status='A' "
			+ "AND adm.unitId=:unitId "
			+ "AND adm.organizationId=:organizationId ")
})


@Entity
@Table(name = "t_discharge", schema = "adt")
@SequenceGenerator(name = "discharge_seq", sequenceName = "adt.discharge_seq", allocationSize = 1)
public class Discharge {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discharge_seq")
	@Column(name = "discharge_id")
	private int dischargeId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "admission_id")
	private Integer admissionId;

	@Column(name = "doctor_id")
	private Integer doctorId;

	@Column(name = "discharge_type_id")
	private Integer dischargeTypeId;

	@Column(name = "reason_id")
	private Integer reasonId;

	@Column(name = "destination_id")
	private Integer destinationId;

	@Column(name = "discharge_date")
	private Date dischargeDate;

	@Column(name = "discharge_time")
	private Date dischargeTime;

	@Column(name = "is_death")
	private char isDeath;

	@Column(name = "remark_discharge")
	private String remarkDischarge;

	@Column(name = "cancel_reason_id")
	private Integer cancelReasonId;

	@Column(name = "note_cancel")
	private String noteCancel;

	@Column(name = "status")
	private char status;

	@Column(name = "discharge_status_id")
	private Integer dischargeStatusId;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private int createdBy;

	@Column(name = "updated_by")
	private int updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "visit_type_id")
	private Integer visitTypeId;

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
	@JoinColumn(name = "doctor_id", insertable = false, nullable = false, updatable = false)
	private DoctorMaster doctor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "discharge_type_id", insertable = false, nullable = false, updatable = false)
	private DischargeTypeMaster dischargeTypeMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reason_id", insertable = false, nullable = false, updatable = false)
	private ReasonMaster reason;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "destination_id", insertable = false, nullable = false, updatable = false)
	private BedMaster bedMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id", insertable = false, nullable = false, updatable = false)
	private ReasonMaster reason2;

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public int getDischargeId() {
		return dischargeId;
	}

	public void setDischargeId(int dischargeId) {
		this.dischargeId = dischargeId;
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

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getDischargeTypeId() {
		return dischargeTypeId;
	}

	public void setDischargeTypeId(Integer dischargeTypeId) {
		this.dischargeTypeId = dischargeTypeId;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public Integer getDestinationId() {
		return destinationId;
	}

	public void setDestinationId(Integer destinationId) {
		this.destinationId = destinationId;
	}

	public Date getDischargeDate() {
		return dischargeDate;
	}

	public void setDischargeDate(Date dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	public Date getDischargeTime() {
		return dischargeTime;
	}

	public void setDischargeTime(Date dischargeTime) {
		this.dischargeTime = dischargeTime;
	}

	public char getIsDeath() {
		return isDeath;
	}

	public void setIsDeath(char isDeath) {
		this.isDeath = (isDeath == '\u0000') ? 'N' : isDeath;
	}

	public Integer getDischargeStatusId() {
		return dischargeStatusId;
	}

	public void setDischargeStatusId(Integer dischargeStatusId) {
		this.dischargeStatusId = dischargeStatusId;
	}

	public String getRemarkDischarge() {
		return remarkDischarge;
	}

	public void setRemarkDischarge(String remarkDischarge) {
		this.remarkDischarge = remarkDischarge;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public String getNoteCancel() {
		return noteCancel;
	}

	public void setNoteCancel(String noteCancel) {
		this.noteCancel = noteCancel;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'A' : status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
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

}

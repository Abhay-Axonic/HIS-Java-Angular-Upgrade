package com.param.radiology.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@NamedNativeQueries({
	@NamedNativeQuery(name="GET_RADIOLOGY_WORKLIST",
			query= " select " 
					 + " 	distinct (orderdetails.order_details_id) as \"orderDetailsId\", " 
					 + " 	rmppr.is_cancel as \"isCancel\", " 
					 + " 	orderdetails.status as \"status\", " 
					 + " 	to_char(appMstr.app_date_time, 'HH24:MI') as \"appointmentTime1\", " 
					 + " 	to_char(cam.appointment_time, 'HH24:MI') as \"appointmentTime\", " 
					 + " 	to_char(appMstr.appointment_date, 'DD/MM/YYYY') as \"appointmentDate\", " 
					 + " 	templatemstr.is_html_print as \"isHtmlPrint\", " 
					 + " 	orderdetails.service_rendered as \"serviceRendered\", " 
					 + " 	orderdetails.ord_remarks as \"ordRemarks\", " 
					 + " 	patientreg.patient_id as \"patientId\", " 
					 + " 	patientreg.uhid_number as \"uhidNumber\", " 
					 + " 	patientreg.gender_id as \"genderId\", " 
					 + " 	patientreg.mobile_number as \"mobileNumber\", " 
					 + " 	patientreg.email as \"patientMail\", " 
					 + " 	gendermaster.gender_code as \"genderCode\", " 
					 + " 	prefixmaster.prefix_code as \"prefixCode\", " 
					 + " 	to_char(patientreg.dob, 'DD-MM-YYYY') as \"dob\", " 
					 + " 	concat(to_char(extract(year from age(current_timestamp, patientreg.dob)), '999'), ' Y') as \"ageString\", " 
					 + " 	encountermstr.encounter_id as \"encounterId\", " 
					 + " 	encountermstr.appointment_id as \"appointmentId\", " 
					 + " 	modalitymstr.modality_id as \"modalityId\", " 
					 + " 	modalitymstr.modality_desc as \"modalityDesc\", " 
					 + " 	encountermstr.doctor_id as \"doctorId\", " 
					 + " 	orderdetails.ord_doc_id as \"ordDoctorId\", " 
					 + " 	orderdetails.order_date as \"orderDateLong\", " 
					 + " 	('Dr. ' || coalesce(doctormstr.first_name)|| ' ' || coalesce(doctormstr.middle_name)|| ' ' || coalesce(doctormstr.last_name)) as \"doctorName\", " 
					 + " 	servicemstr.speciality_id as \"specialityId\", " 
					 + " 	servicemstr.sub_speciality_id as \"subSpecialityId\", " 
					 + " 	specialitymstr.speciality_name as \"specialityName\", " 
					 + " 	servicemstr.service_standard_name as \"serviceName\", " 
					 + " 	servicemstr.service_master_id as \"serviceId\", " 
					 + " 	servicemstr.service_standard_code as \"serviceCode\", " 
					 + " 	orderdetails.priority_id as \"priorityId\", " 
					 + " 	ordermaster.order_no as \"orderNo\", "
					+ " 	ordermaster.order_id as \"orderId\", "
					+ " 	to_char(ordermaster.order_date, 'dd/mm/yyyy') as \"orderDate\", "
					 + " 	to_char(ordermaster.order_date, 'dd/MM/yyyy HH12:MI AM') as \"orderDateTime\", " 
					 + " 	encountermstr.encounter_type_id as \"encounterTypeId\", " 
					 + " 	coalesce(encountermstr.is_critical, 'N') as \"isCritical\" , " 
					 + " 	patientreg.is_vip as \"isVip\", " 
					 + " 	patientreg.image_path as \"imagePath\" , " 
					 + " 	(case " 
					 + " 		when patientreg.is_vip = 'Y' then coalesce(patientreg.alise_name, patientreg.first_name) " 
					 + " 		else concat(coalesce(patientreg.first_name) , ' ', coalesce(patientreg.middle_name) , ' ', coalesce(patientreg.last_name)) " 
					 + " 	end) as \"patientName\", " 
					 + " 	templatetxn.template_transaction_id as \"templateTransactionId\", " 
					 + " 	templatetxn.template_object as \"templateObject\", " 
					 + " 	templatemstr.template_format_id as \"templateFormatId\" , " 
					 + " 	orderdetails.accession_number as \"accessionNumber\" , " 
					 + " 	ordermaster.visit_type_id as \"visitTypeId\", " 
					 + " 	(case " 
					 + " 		when ordermaster.visit_type_id != 1 then bedMstr.bed_number " 
					 + " 		else '-' " 
					 + " 	end) as \"bedNumber\", " 
					 + " 	(case " 
					 + " 		when ordermaster.visit_type_id != 1 then bedMstr.billing_bed_category_id " 
					 + " 		else '0' " 
					 + " 	end) as \"billingBedCategoryId\", " 
					 + " 	patientreg.identification_number as \"identificationNumber\", " 
					 + " 	vm.visit_type_name as \"visitName\", "
					+" (case when ordermaster.encounter_id > 0 then " +
					" ( tpsd.payee_id ) " +
					" else ( tpsd2.payee_id ) end ) as \"payeeId\", "

					+ " 	pcm.patient_category as \"patientCategory\" , "
					 + " 	(case " 
					 + " 		when ordermaster.visit_type_id = 1 then encountermstr.encounter_number " 
					 + " 		else admission.admission_number " 
					 + " 	end ) as \"visitNumber\", " 
					 + " 	(case " 
					 + " 		when ordermaster.encounter_id > 0 then ( to_char(encountermstr.encounter_date, 'dd/MM/yyyy HH12:MI AM')) " 
					 + " 		else ( to_char(dtl.doa, 'dd/MM/yyyy HH12:MI AM') ) " 
					 + " 	end ) as \"registerDate\", " 
					 + " 	tcm.address as \"comAddress\" , " 
					 + " 	tcm.company_desc as \"companyDesc\" , " 
					 + " 	cm.country_code as \"countryCode\", " 
					 + " 	pm.priority_desc as \"priorityName\" , " 
					 + " 	templatemstr.template_type_id as \"templateTypeId\", " 
					 + " 	subspe.sub_speciality_name as \"deptName\", " 
					 + " 	templatetxn.title as \"title\" , " 
					 + " 	templatetxn.is_provisional_release as \"isProvisionalRelease\" , " 
					 + " 	templatetxn.is_final_release as \"isFinalRelease\", " 
					 + " 	to_char(dtl.doa, 'dd/mm/yyyy') as \"doa\", " 
					 + " 	to_char(dtl.pdd, 'dd/mm/yyyy') as \"pdd\", " 
					 + " 	templatetxn.template_object as \"templateContent\", " 
					 + " 	patientreg.email as \"email\", " 
					 + " 	templatetxn.template_doctor_id as \"templateDoctorId\", " 
					 + " 	coalesce((case when app.is_pre_or_post_billing = 'Y' then tbpd.due_amount = 0 when app.is_pre_or_post_billing = 'N' then true end), false) as \"billSettled\" , " 
					 + " 	orderdetails.p_image as \"pImage\", " 
					 + " 	orderdetails.p_image_name as \"fileName\", " 
					 + " 	( " 
					 + " 	case " 
					 + " 		when ordermaster.encounter_id is not null then spd2.payment_entitlement_id " 
					 + " 		else spd.payment_entitlement_id " 
					 + " 	end ) as \"paymentEntitlementId\", " 
					 + " 	(case " 
					 + " 		when reportdoctor.first_name is null then ((concat(coalesce(mem.first_name) , ' ', coalesce(mem.middle_name) , ' ', coalesce(mem.last_name)))) " 
					 + " 		else (concat(coalesce(reportdoctor.first_name) , ' ', coalesce(reportdoctor.middle_name) , ' ', coalesce(reportdoctor.last_name))) " 
					 + " 	end ) as \"reportEntryByName\", " 
					 + " 	(case " 
					 + " 		when reportvalidatedoctor.first_name is null then ((concat(coalesce(memvalidate.first_name) , ' ', coalesce(memvalidate.middle_name) , ' ', coalesce(memvalidate.last_name)))) " 
					 + " 		else (concat(coalesce(reportvalidatedoctor.first_name) , ' ', coalesce(reportvalidatedoctor.middle_name) , ' ', coalesce(reportvalidatedoctor.last_name))) " 
					 + " 	end ) as \"reportvalidateByName\", " 
					 + " 	billingmstr.billing_master_id as \"billingMasterId\", " 
					 + " 	admission.admission_id as \"admissionId\", " 
					 + " 	orderdetails.ord_discount as \"discount\", " 
					 + " 	orderdetails.ord_concession as \"concession\", " 
					 + " 	orderdetails.net_amount as \"ordTotalAmount\", " 
					 + " 	orderdetails.tariff_id as \"tariffId\", " 
					 + " 	orderdetails.discount_ownership as \"discountOwnership\", " 
					 + " 	orderdetails.redo_count as \"redoCount\", "
					 + " 	orderdetails.redo_reason as \"redoReason\", "
					 + " 	usmd.is_ref_doctor_share as \"isRefDoctorShare\", "
					 + " 	usmd.is_doc_req as \"isDoctorRequired\", " 
					 + " 	patientreg.patient_category_id as \"patientTypeId\", "
					 + " 	billingmstr.bill_number as \"billNo\", " 
					 + " 	orderdetails.is_dummy_service as \"isDummyService\","
					 + "  subspe.is_mri_modality as \"isMriModality\" ," 
					 + "  subspe.is_ct_modality as \"isCTModality\", "
					 + "  templatetxn.template_id as \"templateId\", "
					 + "  usmd.is_form_f_applicable as \"isFormFApplicable\" "
					 + " from "
					 + " 	public.t_order_details orderdetails "
					+ " left join public.t_radiology_mapper rmppr on "
					+ " 	rmppr.order_details_id = orderdetails.order_details_id "
					 + " inner join public.t_order_master ordermaster on " 
					 + " 	orderdetails.order_id = ordermaster.order_id " 
					 + " inner join patient.t_patient_registration patientreg on " 
					 + " 	ordermaster.patient_id = patientreg.patient_id " 
					 + " inner join public.m_prefix_master prefixmaster on " 
					 + " 	patientreg.prefix_id = prefixmaster.prefix_id " 
					 + " inner join public.m_gender_master gendermaster on " 
					 + " 	patientreg.gender_id = gendermaster.gender_id " 
					 + " inner join patient.t_patient_details patdtl on " 
					 + " 	patdtl.patient_id = patientreg.patient_id " 
					 + " inner join public.m_visit_type_master vm on " 
					 + " 	vm.visit_type_id = ordermaster.visit_type_id " 
					 + " 	and vm.status = 'A' " 
					 + " inner join service.m_service_master servicemstr on " 
					 + " 	orderdetails.service_id = servicemstr.service_master_id " 
					 + " inner join t_unit_application_billing_configuration app on " 
					 + " 	app.unit_id = ordermaster.org_unit_id " 
                     + " left outer join opd.t_encounter_master encountermstr on " 
                     + " 	ordermaster.encounter_id = encountermstr.encounter_id " 
                     + " left join opd.m_appointment_master appMstr on " 
					 + " 	appMstr.order_details_id = orderdetails.order_details_id " 
					 + " 	or (appMstr.appointment_id = encountermstr.appointment_id " 
					 + " 		and appMstr.appointment_type_id = 2) " 
					 + " left join opd.t_cyclic_appointment_mapper cam on " 
					 + " 	cam.appointment_id = appMstr.appointment_id " 
					 + " left join opd.m_slot_master sm on " 
					 + " 	sm.slot_id = appMstr.slot_id " 
					 + " left join public.m_modality_master modalitymstr on " 
					 + " 	sm.modality_id = modalitymstr.modality_id " 
					 + " left join billing.t_billing_details tbd on " 
					 + " 	orderdetails.order_details_id = tbd.order_details_id " 
					 + " 	and tbd.status = 'A' " 
					 + " left join billing.m_billing_master mbm on " 
					 + " 	mbm.billing_master_id = tbd.billing_master_id " 
					 + " left join billing.t_bill_payee_details tbpd on " 
					 + " 	tbpd.bill_id = mbm.billing_master_id " 
					 + " 	and tbpd.payee_id = app.self_payee_id " 
					 + " left join public.m_patient_category_master pcm on " 
					 + " 	pcm.patient_category_id = patientreg.patient_category_id " 
					 + " 	and pcm.status = 'A' " 
					 + " left join public.m_country_master cm on " 
					 + " 	cm.country_id = patdtl.country_id " 
					 + " left outer join adt.t_admission admission on " 
					 + " 	ordermaster.admission_id = admission.admission_id " 
					 + " left join adt.t_admission_details dtl on " 
					 + " 	dtl.admission_id = admission.admission_id " 
					 + " 	and admission.status = 'A' " 
					 + " 	and dtl.status = 'A' " 
					 + " left join adt.m_bed_master bedMstr on " 
					 + " 	dtl.bed_id = bedMstr.bed_id " 
					 + " left join opd.t_patient_sponsor_details spd on " 
					 + " 	spd.admission_id = admission.admission_id " 
					 + " 	and spd.priority_id = 1 " 
					 + " 	and spd.status = 'A' " 
					 + " left join opd.t_patient_sponsor_details spd2 on " 
					 + " 	spd2.encounter_id = encountermstr.encounter_id " 
					 + " 	and spd2.priority_id = 1 " 
					 + " 	and spd2.status = 'A' " 
					 + " left join billing.t_billing_details billingdtls on " 
					 + " 	orderdetails.order_details_id = billingdtls.order_details_id " 
					 + " 	and billingdtls.status = 'A' " 
					 + " left join billing.m_billing_master billingmstr on " 
					 + " 	billingdtls.billing_master_id = billingmstr.billing_master_id " 
					 + " 	and billingmstr.bill_cancelled = 'N' " 
					 + " left outer join doctor.m_doctor_master doctormstr on " 
					 + " 	orderdetails.ord_doc_id = doctormstr.doctor_id " 
					 + " left outer join doctor.m_speciality_master specialitymstr on " 
					 + " 	ordermaster.ord_doc_spl_id = specialitymstr.speciality_id " 
					 + " left join doctor.m_sub_speciality_master subspe on " 
					 + " 	subspe.sub_speciality_id = orderdetails.service_rendering_deptid "
					+ " left join opd.t_patient_sponsor_details tpsd "
					+ " on ordermaster.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
					+ " left join opd.t_patient_sponsor_details tpsd2 "
					+ " on tpsd2.admission_id = ordermaster.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
					 + " left outer join public.t_template_transaction templatetxn on " 
					 + " 	orderdetails.order_details_id = templatetxn.order_details_id " 
					 + " 	and (templatetxn.status = 'A') " 
					 + " left outer join doctor.m_doctor_master reportdoctor on " 
					 + " 	reportdoctor.user_id = templatetxn.report_entry_by " 
					 + " left outer join m_employee_master mem on " 
					 + " 	mem.user_id = templatetxn.report_entry_by " 
					 + " left outer join doctor.m_doctor_master reportvalidatedoctor on " 
					 + " 	reportvalidatedoctor.user_id = templatetxn.report_validate_by " 
					 + " left outer join m_employee_master memvalidate on " 
					 + " 	memvalidate.user_id = templatetxn.report_validate_by " 
					 + " left outer join public.t_template_master templatemstr on " 
					 + " 	templatetxn.template_id = templatemstr.template_id " 
					 + " 	and (templatemstr.template_type_id = 13) " 
					 + " left join adt.m_priority_master pm on " 
					 + " 	pm.priority_id = orderdetails.priority_id " 
					 + " left join billing.t_bill_payee_details tbpd2 on " 
					 + " 	tbpd2.bill_id = billingmstr.billing_master_id " 
					 + " 	and tbpd2.priority_id = 1 " 
//					 + " left join public.t_radiology_mapper rmppr on "
//					 + " 	rmppr.order_details_id = orderdetails.order_details_id "
					 + " left join public.t_company_master tcm on " 
					 + " 	tcm.company_id = tbpd2.payee_id " 
					 + " left join billing.t_credit_note_detail tcnd on " 
					 + " 	tcnd.order_detail_id = orderdetails.order_details_id " 
					 + " 	and tcnd.status = 'A' " 
					 + " 	and tcnd.cancel_or_discount = 1 " 
					 + " left join service.t_unit_service_master_details usmd on " 
					 + " 	usmd.service_master_id = orderdetails.service_id " 
					 + " 	and usmd.unit_id =:unitId "
//	Join commented for --> HIS-41720 Duplicate entries on RIS screens
//					 + " inner join service.t_unit_service_master_details servicemstrdtls on "
//					 + " 	orderdetails.service_id = servicemstrdtls.service_master_id "
					 + " where " 
					 + " 	servicemstr.speciality_id = :radiologySpecialityId " 
					 + " 	and ordermaster.org_id =:orgId " 
					 + " 	and ordermaster.org_unit_id =:unitId " 
					 + " 	and ((ordermaster.visit_type_id = 1 " 
					 + " 		and billingmstr.bill_status_id != 1 " 
					 + " 		and app.is_pre_or_post_billing = 'Y') " 
					 + " 	or (ordermaster.visit_type_id = 1 " 
					 + " 		and app.is_pre_or_post_billing = 'N') " 
					 + " 	or (ordermaster.visit_type_id != 1)) " 
					 + " 	and (orderdetails.status = 'A' " 
					 + " 		and tcnd.credit_note_detail_id is null) ") ,

		@NamedNativeQuery(name = "GET_COMPANY_ID_BY_ORDER_ID_RADIOLOGY", query = "select " +
				" (case when tom.encounter_id > 0 then  ( tpsd.payee_id ) else ( tpsd2.payee_id ) end ) as \"payeeId\", "
				+ "(case when tom.encounter_id > 0 then  ( tpsd.insurance_id ) else ( tpsd2.insurance_id ) end ) as \"insuranceId\"," +
				" (case when tom.encounter_id > 0 then " +
				" ( tpsd.payment_entitlement_id )  " +
				" else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\","
				+ "tom.visit_type_id as \"visitTypeId\","
				+ "unitpp.block_report_print as \"blockReportPrint\","
				+ "tom.encounter_id as \"encounterId\" " +
				" from t_order_master tom  " +
				" left join opd.t_patient_sponsor_details tpsd on  " +
				" tom.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A'  " +
				" left join opd.t_patient_sponsor_details tpsd2 on " +
				" tom.admission_id = tpsd2.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A'  "
				+ "inner join t_unit_application_billing_configuration unitpp on unitpp.unit_id = tom.org_unit_id" +
				" where tom.order_id = :orderId"),

})
@Entity
@Table(name="t_radiology_mapper",schema = "public")
@SequenceGenerator(sequenceName = "public.t_radiology_mapper_seq",name = "t_radiology_mapper_seq",allocationSize = 1)
public class RadiologyMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_radiology_mapper_seq")
	@Column(name="mapper_id")
	Integer mapperId;
	
	@Column(name="encounter_id")
	private Integer encounterId ;
	
	@Column(name="admission_id")
	private Integer admissionId ;
	
	@Column(name="order_details_id")
	private Integer orderDetailsId ;
	
	@Column(name="reason_id")
	private Integer reasonId ;
	
	@Column(name="status")
	private Character status ;
	
	@Column(name="is_cancel")
	private Character isCancel ;
	
	@Column(name="unit_id")
	private Integer unitId ;
	
	@Column(name="organization_id")
	private Integer organizationId ;
	
	@Column(name="created_by")
	private Integer createdBy ;
	
	@Column(name="updated_by")
	private Integer updatedBy ;
	
	@Column(name="created_date")
	private Date createdDate ;
	
	@Column(name="updated_date")
	private Date updatedDate;

	public Integer getMapperId() {
		return mapperId;
	}

	public void setMapperId(Integer mapperId) {
		this.mapperId = mapperId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Character getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(Character isCancel) {
		this.isCancel = isCancel;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}
		

}

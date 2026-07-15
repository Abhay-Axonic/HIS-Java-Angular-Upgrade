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

import com.param.global.common.ICommonConstants;

@NamedNativeQueries({
	@NamedNativeQuery(name="GET_DIAGNOSIS_PROCEDURE_WORKLIST",
			query="select "
			+ "	distinct (orderdetails.order_details_id) as \"orderDetailsId\", "
			+ " dmppr.is_cancel as \"isCancel\", "
			+ "	orderdetails.status as \"status\", "
            + " templatemstr.is_html_print as \"isHtmlPrint\", "
			+ "	orderdetails.service_rendered as \"serviceRendered\", "
			+ "	patientreg.patient_id as \"patientId\", "
			+ "	patientreg.uhid_number as \"uhidNumber\", "
			+ "	patientreg.gender_id as \"genderId\", "
			+ "	gendermaster.gender_code as \"genderCode\", "
			+ "	to_char(patientreg.dob, 'DD-MM-YYYY') as \"dob\", "
			+ "concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patientreg.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, patientreg.dob)),'M ',EXTRACT(day from AGE(CURRENT_TIMESTAMP, patientreg.dob)),'D ') as \"ageString\", "
			+ "	encountermstr.doctor_id as \"doctorId\", "
			+ "	('Dr. ' || coalesce(doctormstr.first_name)|| ' ' || coalesce(doctormstr.middle_name)|| ' ' || coalesce(doctormstr.last_name)) as \"doctorName\", "
			+ "	servicemstr.speciality_id as \"specialityId\", "
			+ "	servicemstr.sub_speciality_id as \"subSpecialityId\", "
			+ "	specialitymstr.speciality_name as \"specialityName\", "
			+ "	servicemstr.service_standard_name as \"serviceName\", "
			+ "	servicemstr.service_master_id as \"serviceId\", "
			+ "	servicemstr.service_standard_code as \"serviceCode\", "
			+ "	ordermaster.order_no as \"orderNo\", "
			+ "	to_char(ordermaster.order_date, 'dd/mm/yyyy') as \"orderDate\", "
			+ "	to_char(ordermaster.order_date, 'dd/MM/yyyy HH12:MI AM') as \"orderDtTime\","
			+ "	encountermstr.encounter_type_id as \"encounterTypeId\", "
			+ "	coalesce(encountermstr.is_critical, 'N') as \"isCritical\" , "
			+ "prefixmaster.prefix_code as \"prefixCode\", "
			+ "	patientreg.is_vip as \"isVip\", "
			+ "	(case "
			+ "		when patientreg.is_vip = 'Y' then coalesce(patientreg.alise_name, patientreg.first_name) "
			+ "		else concat(coalesce(patientreg.first_name) , ' ', coalesce(patientreg.middle_name) , ' ', coalesce(patientreg.last_name)) "
			+ "	end) as \"patientName\" , "
			+" patientreg.image_path as \"imagePath\","
			+ "	(case when encountermstr.encounter_id > 0 then '-' "
			+ "	when admission.admission_id > 0 then bedMstr.bed_number end) "  
			+ "	 as \"bedNumber\" , "
			+ "	orderdetails.accession_number as \"accessionNumber\" , "
			+ "	ordermaster.visit_type_id as \"visitTypeId\", "
			+ "	patientreg.identification_number as \"identificationNumber\", "
			+ "	vm.visit_type_name as \"visitName\", "
			+ "	pcm.patient_category as \"patientCategory\" , "
			+ "	encountermstr.encounter_id as \"encounterId\", "
			+ "	admission.admission_id as \"admissonId\", "
			+ "	(case when encountermstr.encounter_id > 0 then "
			+ "		encountermstr.encounter_number "
			+ "	when admission.admission_id > 0 then "
			+ "		admission.admission_number end) "
			+ "	as \"visitNumber\","
			+" 	modalitymstr.modality_id as \"modalityId\", "
			+ "	to_char(dtl.doa, 'dd/mm/yyyy') as \"doa\", "
			+ "	to_char(dtl.pdd, 'dd/mm/yyyy') as \"pdd\", "
			+" 	modalitymstr.modality_desc  as \"modalityDesc\", unitDlts.is_appointable as \"isAppointableRadiology\" ,"
			+ " templatetxn.template_transaction_id as \"templateTransactionId\",templatetxn.template_object as \"templateObject\","
			+ " templatetxn.template_object as  \"templateContent\"," 
			+ " templatemstr.template_format_id as \"templateFormatId\"," 
			+ " templatetxn.title as \"title\", templatetxn.is_provisional_release as \"isProvisionalRelease\" ," 
			+ " templatetxn.is_final_release as \"isFinalRelease\" ,templatemstr.template_type_id as \"templateTypeId\",subspe.sub_speciality_name as \"deptName\","
			+ " templatetxn.template_doctor_id as \"templateDoctorId\",  "
			+ " billingmstr.bill_number as \"billNo\", "
			+ " billingmstr.billing_master_id as \"billingMasterId\", "
			+ " orderdetails.service_amt as \"serviceAmt\", "
			+ "	('Dr. ' || coalesce(templateDoctor.first_name)|| ' ' || coalesce(templateDoctor.middle_name)|| ' ' || coalesce(templateDoctor.last_name)) as \"templateDoctorName\" ,"
            + "orderdetails.is_dummy_service as \"isDummyService\" "
			+"  from "
			+ "	public.t_order_details orderdetails inner join public.t_order_master ordermaster on orderdetails.order_id = ordermaster.order_id "
			+ " inner join patient.t_patient_registration patientreg on ordermaster.patient_id = patientreg.patient_id "
			+ " inner join public.m_prefix_master prefixmaster 		on 	patientreg.prefix_id = prefixmaster.prefix_id "
			+ " inner join public.m_gender_master gendermaster 		on 	patientreg.gender_id = gendermaster.gender_id "
			+ " inner join patient.t_patient_details patdtl   		on  patdtl.patient_id = patientreg.patient_id "
			+ " inner join public.m_visit_type_master vm       		on  vm.visit_type_id = ordermaster.visit_type_id 	and vm.status = 'A' "
			+ " left join public.m_patient_category_master pcm 		on 	pcm.patient_category_id = patientreg.patient_category_id and pcm.status = 'A' "
			+ " left outer join opd.t_encounter_master encountermstr on ordermaster.encounter_id = encountermstr.encounter_id "
			+ " left outer join adt.t_admission admission 			on  ordermaster.admission_id = admission.admission_id "
			+ " left join adt.t_admission_details dtl 				on  dtl.admission_id=admission.admission_id and admission.status='A' and dtl.status='A' " 
			+ "	left join adt.m_bed_master bedMstr 					on  dtl.bed_id=bedMstr.bed_id "
			+ " left join billing.t_billing_details billingdtls 	on 	orderdetails.order_details_id = billingdtls.order_details_id  "
			+ " left join billing.m_billing_master billingmstr 		on 	billingdtls.billing_master_id = billingmstr.billing_master_id and billingmstr.bill_cancelled ='N' "
			+ " left outer join doctor.m_doctor_master doctormstr 	on 	ordermaster.ord_doctor_id = doctormstr.doctor_id "
			+ " left outer join doctor.m_speciality_master specialitymstr 	on 	ordermaster.ord_doc_spl_id = specialitymstr.speciality_id "
			+ " left join 	doctor.m_sub_speciality_master subspe   on  subspe.sub_speciality_id=orderdetails.service_rendering_deptid "
			+ " left join opd.m_appointment_master appMstr 			on 	appMstr.order_details_id = orderdetails.order_details_id "
			+ " inner join service.m_service_master servicemstr 	on 	orderdetails.service_id = servicemstr.service_master_id "
			+ " inner join service.t_unit_service_master_details unitDlts 	on unitDlts.service_master_id = servicemstr.service_master_id and unitDlts.procedure_type_id = 1 "
			+ "	and unitDlts.is_procedure = 'Y' "
			+ " inner join t_unit_application_billing_configuration config 	on config.unit_id=orderdetails.org_unit_id "
			+ " left join opd.m_slot_master sm                    on sm.slot_id = appMstr.slot_id " 
			+ " left  join public.m_modality_master modalitymstr  on sm.modality_id = modalitymstr.modality_id "
			+ " left join  public.t_diagnostic_procedure_mapper dmppr     on dmppr.order_details_id = orderdetails.order_details_id "
			+ " left outer join public.t_template_transaction templatetxn on orderdetails.order_details_id = templatetxn.order_details_id and templatetxn.status = 'A' and templatetxn.is_questionary='N' "
			+ " left outer join public.t_template_master templatemstr     on templatetxn.template_id = templatemstr.template_id and (templatemstr.template_type_id = 20) "
			+ "left outer join doctor.m_doctor_master templateDoctor 	   on 	templatetxn.template_doctor_id = templateDoctor.doctor_id "
			+ " where "
			+ " ordermaster.org_id =:orgId "
			+ "	and ordermaster.org_unit_id =:unitId "
			+ "	and ((ordermaster.visit_type_id = 1	and billingmstr.bill_status_id != 1 and config.is_pre_or_post_billing='Y') "
			+ "	or  (ordermaster.visit_type_id = 1 and config.is_pre_or_post_billing='N') or (ordermaster.visit_type_id != 1))"
			+ " and(  case when ordermaster.visit_type_id != 1 and unitDlts.is_appointable='Y' then sm.modality_id is not null "
			+ " else orderdetails.status = 'A' end) ")
})
@Entity
@Table(name="t_diagnostic_procedure_mapper",schema = "public")
@SequenceGenerator(sequenceName = "public.t_diagnostic_procedure_mapper_seq",name = "t_diagnostic_procedure_mapper_seq",allocationSize = 1)
public class DiagnosticProcedureMapper implements ICommonConstants {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_diagnostic_procedure_mapper_seq")
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

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
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
	
	
	
}

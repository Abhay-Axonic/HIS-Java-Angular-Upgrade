package com.param.global.model;

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

@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_TEMPLATE_TRANSACTION",
					 query = "select tmp.template_transaction_id as \"templateTransactionId\", tmp.template_id as \"templateId\", tmp.title as \"title\",  tmp.speciality_id as \"specialityId\", "
							 + " tmp.sub_speciality_id as \"subSpecialityId\", tmp.template_object as \"templateObject\", tmp.admission_id as \"admissionId\", tmp.encounter_id as \"encounterId\", "
							 + " tmp.doctor_id as \"doctorId\", tmp.patient_id as \"patientId\", tmp.status as \"status\", tmp.signed_by as \"signedBy\", to_char(tmp.signed_date,'DD-MM-YYYY HH24:MI:SS') as \"signedDate\", "
							 + " tmp.json_object as \"jsonObject\", tmp.org_id as \"orgId\", tmp.unit_id as \"unitId\", tmp.created_by as \"createdBy\", to_char(tmp.created_date,'DD-MM-YYYY HH24:MI:SS') as \"createdDate\", "
							 + " tmp.updated_by as \"updatedBy\", to_char(tmp.updated_date,'DD-MM-YYYY HH24:MI:SS') as \"updatedDate\" "
							 + " from public.t_template_transaction tmp "
							 + " where tmp.status = 'A' "),
	
	@NamedNativeQuery(name = "GET_TEMPLATE_BY_CONVERSATION_ID",
	 query =  "select "
			 + "	master.encounter_id as \"encounterId\", "
			 + "	master.admission_id as \"admissionId\", "
			 + "	master.template_id as \"templateId\", "
			 + "	master.form_template_id as \"formTemplateId\", "
			 + "	master.form_url as \"formUrl\", "
			 + "	master.template_type_id as \"templateTypeId\", "
			 + "	master.org_id as \"orgId\", "
			 + "	master.unit_id as \"unitId\", "
			 + "	master.conversation_id as \"conversationId\", "
			 + "	master.doctor_id as \"doctorId\", "
			 + "	master.patient_id as \"patientId\", "
			 + "	master.diagnosis_id as \"diagnosisId\", "
			 + "	master.order_details_id as \"orderDetailsId\", "
			 + "	master.m_icpc_id as \"mIcpcId\", "
			 + "	master.m_icpc_system_id as \"mIcpcSystemId\", "
			 + "	master.is_provisional_release as \"isProvisionalRelease\", "
			 + "	master.is_final_release as \"isFinalRelease\", "
			 + "	to_char(master.review_time,'DD-MM-YYYY HH:mm:ss') as \"reviewTime\", "
			 + "	master.is_questionary as \"isQuestionary\", "
			 + "	master.t_sub_speciman_id as \"tSubSpecimanId\", "
			 + "	master.report_no as \"reportNo\", "
			 + "	master.staining_id as \"stainingId\", "
			 + "	master.lab_sample_dtls_id as \"labSampleDtlsId\", "
			 + "	master.template_res_id as \"templateResId\", "
			 + "	master.template_transaction_id as \"templateTransactionId\", "
			 + "	master.sample_status_id as \"sampleStatusId\" , "
			 + "	master.appointment_id as \"appointmentId\", "
			 + "	master.adm_note_id as \"admNoteId\" , "
			 + "    ttm.speciality_id  as \"specialityId\", "
			 + "	ttm.sub_speciality_id  as \"subSpecialityId\" , "
			 + "    ttm.title as \"title\" , "
			 + "	master.created_by as \"createdBy\", "
			 + "	concat(coalesce(prfx.prefix_code,''),' ',coalesce( pr.first_name,''),' ',coalesce( pr.middle_name,'' ),' ',coalesce( pr.last_name,'' )) as \"patientName\", "
			 + "	mdm.email  as \"dEmail\" ,"
			 + "    mum.unit_email_id  as \"unitEmailId\" "
			 + "from "
			 + "	public.forms_engine_transaction_details_master master "
			 + "	inner join public.t_template_master ttm on ttm.template_id  = master.template_id "
			 +"	    inner join public.m_unit_master mum on master.unit_id  = mum.unit_id  "
			 + "	left join patient.t_patient_registration pr  on master.patient_id  = pr.patient_id "
			 + "	left join public.m_prefix_master prfx on prfx.prefix_id = pr.prefix_id "
			 + "	left join doctor.m_doctor_master mdm on master.doctor_id  = mdm.doctor_id "
			 + "where "
			 + "	master.conversation_id = :conversationId "
			 + "    and master.status = 'A' "
			 + "    and ttm.unit_id  = master.unit_id")
})
@Entity
@Table(name = "t_template_transaction" , schema = "public")
@SequenceGenerator(name = "t_template_transaction_seq" , sequenceName = "public.t_template_transaction_seq" , allocationSize = 1)
public class TemplateTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_template_transaction_seq")
	@Column(name = "template_transaction_id")
	private int templateTransactionId;
	
	@Column(name = "template_id")
	private Integer templateId;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "speciality_id")
	private Integer specialityId;
	
	@Column(name = "sub_speciality_id")
	private Integer subSpecialityId;
	
	@Column(name = "template_object")
	private String templateObject;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "encounter_id")
	private Integer encounterId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "signed_by")
	private Integer signedBy;
	
	@Column(name = "signed_date")
	private Date signedDate;
	
	@Column(name = "json_object")
	private String jsonObject;
	
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
	
	@Column(name = "diagnosis_id")
	private Integer diagnosisId;
	
	@Column(name = "order_details_id")
	private Integer orderDetailsId;
	
	@Column(name = "m_icpc_system_id")
	private Integer mIcpcSystemId;
	
	@Column(name = "m_icpc_id")
	private Integer mIcpcId;
	
	@Column(name = "is_provisional_release")
	private Character isProvisionalRelease;
	
	@Column(name = "is_final_release")
	private Character isFinalRelease;
	
	@Column(name = "is_report_release")
	private Character isReportRelease;
	
	@Column(name = "report_release_datetime")
	private Date reportReleaseDatetime;	
	
	@Column(name = "review_time")
	private Date reviewTime;
	
	@Column(name = "is_questionary")
	private Character isQuestionary;
	
	@Column(name = "template_doctor_id")
	private Integer templateDoctorId;
	
	@Column(name = "radiology_report_link")
	private String radiologyReportLink;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_details_id", insertable = false, updatable = false, nullable = false)
	private OrderDetailsMaster orderDetailsMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "template_id", insertable = false, updatable = false, nullable = false)
	private TemplateMaster templateMaster;
	
	@Column(name = "conversation_id")
	private String conversationId;
	
	@Column(name = "postback_url")
	private String postbackURL;
	
	@Column(name = "appointment_id")
	private Integer appointmentId;
	
	@Column(name = "adm_note_id")
	private Integer admNoteId;
	
	@Column(name = "pdf_byte")
	private byte[]  pdfByte;
	
	@Column(name = "report_entry_by")
	private Integer reportEntryBy;
	
	@Column(name = "report_validate_by")
	private Integer reportValidateby;
	
	@Column(name = "template_html")
	private String templateHtml;
	
	@Column(name = "radiology_report_url")
	private String radiologyReportUrl;
	
	@Column(name = "form_data")
	private String formData;
	
	@Column(name = "template_url")
	private String templateUrl;

	@Column(name = "report_entry_datetime")
	private Date reportEntryDatetime;
	
	public Integer getTemplateDoctorId() {
		return templateDoctorId;
	}

	public void setTemplateDoctorId(Integer templateDoctorId) {
		this.templateDoctorId = templateDoctorId;
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

	public int getTemplateTransactionId() {
		return templateTransactionId;
	}

	public void setTemplateTransactionId(int templateTransactionId) {
		this.templateTransactionId = templateTransactionId;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public String getTemplateObject() {
		return templateObject;
	}

	public void setTemplateObject(String templateObject) {
		this.templateObject = templateObject;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'I');
	}

	public Integer getSignedBy() {
		return signedBy;
	}

	public void setSignedBy(Integer signedBy) {
		this.signedBy = signedBy;
	}

	public Date getSignedDate() {
		return signedDate;
	}

	public void setSignedDate(Date signedDate) {
		this.signedDate = signedDate;
	}

	public String getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(String jsonObject) {
		this.jsonObject = jsonObject;
	}

	public Integer getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public OrderDetailsMaster getOrderDetailsMaster() {
		return orderDetailsMaster;
	}

	public void setOrderDetailsMaster(OrderDetailsMaster orderDetailsMaster) {
		this.orderDetailsMaster = orderDetailsMaster;
	}

	public Integer getmIcpcSystemId() {
		return mIcpcSystemId;
	}

	public void setmIcpcSystemId(Integer mIcpcSystemId) {
		this.mIcpcSystemId = mIcpcSystemId;
	}

	public Integer getmIcpcId() {
		return mIcpcId;
	}

	public void setmIcpcId(Integer mIcpcId) {
		this.mIcpcId = mIcpcId;
	}

	public Character getIsProvisionalRelease() {
		return isProvisionalRelease;
	}

	public void setIsProvisionalRelease(Character isProvisionalRelease) {
		this.isProvisionalRelease = isProvisionalRelease;
	}

	public Character getIsFinalRelease() {
		return isFinalRelease;
	}

	public void setIsFinalRelease(Character isFinalRelease) {
		this.isFinalRelease = isFinalRelease;
	}

	public Character getIsReportRelease() {
		return isReportRelease;
	}

	public void setIsReportRelease(Character isReportRelease) {
		this.isReportRelease = isReportRelease;
	}

	public Date getReportReleaseDatetime() {
		return reportReleaseDatetime;
	}

	public void setReportReleaseDatetime(Date reportReleaseDatetime) {
		this.reportReleaseDatetime = reportReleaseDatetime;
	}

	
	public Date getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}

	public Character getIsQuestionary() {
		return isQuestionary;
	}

	public void setIsQuestionary(Character isQuestionary) {
		this.isQuestionary = isQuestionary;
	}

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}

	public String getPostbackURL() {
		return postbackURL;
	}

	public void setPostbackURL(String postbackURL) {
		this.postbackURL = postbackURL;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Integer getAdmNoteId() {
		return admNoteId;
	}

	public void setAdmNoteId(Integer admNoteId) {
		this.admNoteId = admNoteId;
	}

	public byte[] getPdfByte() {
		return pdfByte;
	}

	public void setPdfByte(byte[] pdfByte) {
		this.pdfByte = pdfByte;
	}

	public Integer getReportEntryBy() {
		return reportEntryBy;
	}

	public void setReportEntryBy(Integer reportEntryBy) {
		this.reportEntryBy = reportEntryBy;
	}

	public Integer getReportValidateby() {
		return reportValidateby;
	}

	public void setReportValidateby(Integer reportValidateby) {
		this.reportValidateby = reportValidateby;
	}

	public String getTemplateHtml() {
		return templateHtml;
	}

	public void setTemplateHtml(String templateHtml) {
		this.templateHtml = templateHtml;
	}

	public String getRadiologyReportUrl() {
		return radiologyReportUrl;
	}

	public void setRadiologyReportUrl(String radiologyReportUrl) {
		this.radiologyReportUrl = radiologyReportUrl;
	}

	public String getFormData() {
		return formData;
	}

	public void setFormData(String formData) {
		this.formData = formData;
	}

	public String getTemplateUrl() {
		return templateUrl;
	}

	public void setTemplateUrl(String templateUrl) {
		this.templateUrl = templateUrl;
	}

	public String getRadiologyReportLink() {
		return radiologyReportLink;
	}

	public void setRadiologyReportLink(String radiologyReportLink) {
		this.radiologyReportLink = radiologyReportLink;
	}

	public Date getReportEntryDatetime() {
		return reportEntryDatetime;
	}

	public void setReportEntryDatetime(Date reportEntryDatetime) {
		this.reportEntryDatetime = reportEntryDatetime;
	}
	
}

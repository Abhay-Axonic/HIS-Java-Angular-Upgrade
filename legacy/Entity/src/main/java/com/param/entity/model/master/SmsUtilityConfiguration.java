package com.param.entity.model.master;

import java.io.Serial;
import java.io.Serializable;
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

@NamedNativeQueries(
{
	@NamedNativeQuery(name = "GET_SMS_UTILITY_CONFIG_MASTER_LIST",
				query = " SELECT suc.sms_utility_id as \"smsUtilityId\", suc.trigger_id as\"triggerId\", "
				+ " suc.template_description as \"templateDescription\", suc.category_id as \"categoryId\", suc.org_id as \"orgId\", suc.unit_id as \"unitId\", "
				+ " suc.status as \"status\", suc.created_by as \"createdBy\", suc.updated_by as \"updatedBy\",cm.category_name as \"categoryName\","
				+ " tm.trigger_name as \"triggerName\", suc.is_email as \"isEmail\", suc.mail_subject as \"mailSubject\", suc.is_SMS_Send as \"isSMSSend\",suc.is_whatsapp_send as \"isWhatsappSend\","
				+ " suc.user_defined_mail as \"userDefinedMail\", "
				+ " suc.schedule_type_id as \"scheduleTypeId\", "
				+ " suc.critical_report as \"criticalReport\", wtm.template_desc as \"whatsAppTemplateDesc\", "
				+ " suc.type as  \"type\", suc.whatsapp_template_master_id as \"whatsAppTemplateMasterId\" "
				+ " FROM public.m_sms_utility_cofiguration suc "
				+ " join public.m_sms_trigger_master tm on suc.trigger_id =tm.trigger_id "
				+ " left join public.m_whatsapp_template_master wtm on suc.whatsapp_template_master_id =wtm.whatsapp_template_master_id "
				+ " join public.m_sms_category_master cm on suc.category_id=cm.category_id "
				+ " Where suc.org_id = :organizationId " 
				+ " AND  suc.unit_id = :unitId "),
	
	
	@NamedNativeQuery(name = "GET_SMS_UTILITY_CONFIG_MASTER_LIST_BY_TRIGGERID_AND_CATEGORY_ID",
		query =  "SELECT suc.sms_utility_id as \"smsUtilityId\", "
				 +" 	suc.trigger_id as\"triggerId\", "
				 +"  	suc.template_description as \"templateDescription\", "
				 +" 	suc.category_id as \"categoryId\", "
				 +" 	suc.org_id as \"orgId\", "
				 +" 	suc.unit_id as \"unitId\", "
				 +"  	suc.status as \"status\", "
				 +" 	suc.created_by as \"createdBy\", "
				 +" 	suc.updated_by as \"updatedBy\","
				 +" 	cm.category_name as \"categoryName\","
				 +"  	tm.trigger_name as \"triggerName\","
				 +"  	(select sender_id from t_unit_application_billing_configuration where unit_id=:unitId) as \"senderId\","
				 +"  	(select concat('Dr. ',coalesce(first_name)) from doctor.m_doctor_master where doctor_id=:docId limit 1) as \"docName\","
				 +"   	(select mobile_number from doctor.m_doctor_master where doctor_id=:docId limit 1) as \"docMobNo\","
				 +"  	(select mobile_number from patient.t_patient_registration where patient_id=:patientId limit 1) as \"patMobNo\","
				 +"  	(select uhid_number from patient.t_patient_registration where patient_id=:patientId limit 1) as \"patUhid\","
				 +"   	(select concat(coalesce(pre.prefix_code),'. ',coalesce(first_name)) "
				 +"   		from patient.t_patient_registration pat "
				 +"   		inner join m_prefix_master pre on pre.prefix_id= pat.prefix_id "
				 +"   		where patient_id=:patientId limit 1) as \"patName\" "
				 +"  	FROM public.m_sms_utility_cofiguration suc "
				 +"  	join public.m_sms_trigger_master tm on suc.trigger_id =tm.trigger_id "
				 +"  	join public.m_sms_category_master cm on suc.category_id=cm.category_id "
				 +"  	AND  suc.unit_id = :unitId "
				 +"  	AND suc.trigger_id = :triggerId "),
	@NamedNativeQuery(name="GET_ALL_DETAILS_FOR_SENDDING_BATCH_SMS",
	query="")
	
	
		
})

@Entity
@Table(name = "m_sms_utility_cofiguration", schema = "public")
@SequenceGenerator(name = "m_sms_utility_cofiguration_seq", sequenceName = "public.m_sms_utility_cofiguration_seq", allocationSize = 1)
public class SmsUtilityConfiguration implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_sms_utility_cofiguration_seq")
	@Column(name = "sms_utility_id", unique = true, nullable = false)
	private Integer smsUtilityId;

	@Column(name = "trigger_id")
	private Integer triggerId;

	@Column(name = "template_description", length = 500)
	private String templateDescription;

	@Column(name = "category_id")
	private Integer categoryId;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "unit_id")
	private Integer unitId;

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
	
	@Column(name = "schedule_type_id")
	private Integer scheduleTypeId;
	
	@Column(name = "is_email")
	private Boolean isEmail;
	
	@Column(name = "mail_subject")
	private String mailSubject;
	
	@Column(name = "is_whatsapp_send")
	private Character isWhatsappSend;
	
	@Column(name = "user_defined_mail")
	private String userDefinedMail;
	
	@Column(name = "is_SMS_send")
	private Character isSMSSend;
	
	@Column(name = "type")
	private String type;

	@Column(name = "critical_report")
	private Character criticalReport;

	@Column(name = "whatsapp_template_master_id")
	private Integer whatsAppTemplateMasterId;

	public Integer getSmsUtilityId() {
		return smsUtilityId;
	}

	public void setSmsUtilityId(Integer smsUtilityId) {
		this.smsUtilityId = smsUtilityId;
	}

	public Integer getTriggerId() {
		return triggerId;
	}

	public String getUserDefinedMail() {
		return userDefinedMail;
	}

	public void setUserDefinedMail(String userDefinedMail) {
		this.userDefinedMail = userDefinedMail;
	}

	public void setTriggerId(Integer triggerId) {
		this.triggerId = triggerId;
	}

	public String getTemplateDescription() {
		return templateDescription;
	}

	public void setTemplateDescription(String templateDescription) {
		this.templateDescription = templateDescription;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
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

	public Integer getScheduleTypeId() {
		return scheduleTypeId;
	}

	public void setScheduleTypeId(Integer scheduleTypeId) {
		this.scheduleTypeId = scheduleTypeId;
	}

	public Boolean getIsEmail() {
		return isEmail;
	}

	public void setIsEmail(Boolean isEmail) {
		this.isEmail = isEmail;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public Character getIsWhatsappSend() {
		return isWhatsappSend;
	}

	public void setIsWhatsappSend(Character isWhatsappSend) {
		this.isWhatsappSend = isWhatsappSend;
	}
	
	public Character getIsSMSSend() {
		return isSMSSend;
	}

	public void setIsSMSSend(Character isSMSSend) {
		this.isSMSSend = isSMSSend;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Character getCriticalReport() {
		return criticalReport;
	}

	public void setCriticalReport(Character criticalReport) {
		this.criticalReport = criticalReport;
	}

	public Integer getWhatsAppTemplateMasterId() {
		return whatsAppTemplateMasterId;
	}

	public void setWhatsAppTemplateMasterId(Integer whatsAppTemplateMasterId) {
		this.whatsAppTemplateMasterId = whatsAppTemplateMasterId;
	}
}

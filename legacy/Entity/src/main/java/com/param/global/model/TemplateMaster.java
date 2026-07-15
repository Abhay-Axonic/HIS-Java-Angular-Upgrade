package com.param.global.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(name = "GET_TEMPLATE_BY_ID",
				query= "SELECT	tmp.templateId as templateId, tmp.title as title, tmp.specialityId as specialityId, tmp.templateObject as templateObject, tmp.status as status,tmp.templateFormatId as templateFormatId ,tmp.isHtmlPrint as isHtmlPrint , "
					 + "		tmp.orgId as orgId, tmp.unitId as unitId, tmp.doctorId as doctorId, tmp.subSpecialityId as subSpecialityId, tmp.createdBy as createdBy, tmp.updatedBy as updatedBy,"
					 + " 		to_char(tmp.createdDate,'DD-MM-YYYY HH24:MI:SS') as createdDate, to_char(tmp.updatedDate,'DD-MM-YYYY HH24:MI:SS') as updatedDate, tmp.templateTypeId as templateTypeId , tmp.formTemplateId as formTemplateId "
					 + "FROM	TemplateMaster tmp "
					 + "WHERE	tmp.templateId =:templateId ")
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_TEMPLATE_LIST",
					 query = "select "
							 + "	tmp.template_id as \"templateId\", "
							 + "	tmp.title as \"title\", "
							 + "	tmp.speciality_id as \"specialityId\", "
							 + "	tmp.sub_speciality_id as \"subSpecialityId\", "
							 + "	tmp.template_format_id as \"templateFormatId\" , "
							 + "	tmp.template_object as \"templateObject\", "
							 + "	tmp.status as \"status\", "
							 + "	tmp.created_by as \"createdBy\", "
							 + "	to_char(tmp.created_date, 'DD-M-YYYY HH24:MI:SS') as \"createdDate\", "
							 + "	tmp.org_id as \"orgId\", "
							 + "	tmp.unit_id as \"unitId\", "
							 + "	tmp.doctor_id as \"doctorId\", "
							 + "	spc.speciality_name as \"speciality\", "
							 + "	subspc.sub_speciality_name as \"subSpeciality\", "
							 + "	coalesce(concat(doc.first_name, ' ', doc.middle_name, ' ', doc.last_name), null) as \"doctor\", "
							 + "	template_type.template_type_name as \"templateType\" ,"
							 + " 	tmp.form_template_keywords  as \"formTemplateKeywords\"," 
							 +"	    tmp.form_template_id as \"formTemplateId\""
							 + " from "
							 + "	public.t_template_master tmp "
							 + "left join public.m_template_type template_type on "
							 + "	tmp.template_type_id = template_type.template_type_id "
							 + "left join doctor.m_speciality_master spc on "
							 + "	spc.speciality_id = tmp.speciality_id "
							 + "left join doctor.m_sub_speciality_master subspc on "
							 + "	subspc.sub_speciality_id = tmp.sub_speciality_id "
							 + "left join doctor.m_doctor_master doc on "
							 + "	doc.doctor_id = tmp.doctor_id "
							 + "where "
							 + "	tmp.org_id =:orgId "
							 + "	and tmp.unit_id =:unitId "
							 + "	and tmp.status = 'A' "
							 + "	order by 1 "),
	
	@NamedNativeQuery(name = "UPDATE_TEMPLATE_STATUS_BY_ID",
					query = " update public.t_template_master set status =:status "
						  + " where template_id =:id "),
	
	@NamedNativeQuery(name = "GET_TEMPLATES_FOR_COVERSHEET_BY_DOCTOR_AND_SEPCIALTY",
					 query = "select tmp.template_id as \"templateId\", tmp.title as \"title\", tmp.speciality_id as \"specialityId\", tmp.sub_speciality_id as \"subSpecialityId\", "
							 + " tmp.template_object as \"templateObject\", tmp.status as \"status\", tmp.doctor_id as \"doctorId\","
							 + " tmp.template_type_id as \"templateTypeId\" ,"
							 +"	 tmp.form_template_id as \"formTemplateId\" "
							 + " from public.t_template_master tmp "
							 + " where (tmp.speciality_id is null and tmp.sub_speciality_id is null and tmp.doctor_id is null) "
							 + " or (tmp.doctor_id = :doctorId ) "
							 + " or (tmp.speciality_id = :specialtyId ) "
							 + " and tmp.status = 'A' "
							 + " and tmp.org_id = :orgId and tmp.unit_id = :unitId "),
	
	@NamedNativeQuery(name = "GET_QUESTINAAIRE_TEMPLATE",
	 query = "select tmp.template_id as \"templateId\", tmp.title as \"title\", tmp.speciality_id as \"specialityId\", tmp.sub_speciality_id as \"subSpecialityId\", "
			 + " tmp.template_object as \"templateObject\", tmp.status as \"status\", tmp.doctor_id as \"doctorId\","
			 + " tmp.template_type_id as \"templateTypeId\" ,"
			 +"	 tmp.form_template_id as \"formTemplateId\" "
			 + " from public.t_template_master tmp "
			 + " where "
			 + " tmp.status = 'A' "
			 + " and tmp.org_id = :orgId and tmp.unit_id = :unitId "
			 + " and tmp.speciality_id = :specialityId and tmp.doctor_id = :doctorId "),
	
	
	@NamedNativeQuery(name = "GET_LAB_TEMPLATES",
     query =  " select  "
    		 + "	template_mst.template_id as \"templateId\",  "
    		 + "	template_mst.title as \"title\",  "
    		 + "	template_mst.template_object as \"templateObject\",  "
    		 + "	template_mst.template_format_id as \"templateFormatId\" , "
    		 +"	    template_mst.form_template_id as \"formTemplateId\" "
    		 + "from  "
    		 + "	lab.m_test_master test_mst  "
    		 + "inner join lab.t_lab_sample_dtls smp_dtls on  "
    		 + "	smp_dtls.test_id = test_mst.test_id  "
    		 + "inner join public.t_lab_configuration lab_config on  "
    		 + "	lab_config.histopathology_id = test_mst.dept_id  "
    		 + "	and lab_config.unit_id = smp_dtls.org_unit_id  "
    		 + "inner join lab.t_template_service_mapper tem_ser_mper on  "
    		 + "	 test_mst.service_id = tem_ser_mper.service_id  "
    		 + "	 and lab_config.histopathology_id = tem_ser_mper.sub_speciality_id  "
    		 + "inner join public.t_template_master template_mst on  "
    		 + "	template_mst.template_id = tem_ser_mper.template_id  "
    		 + "where  "
    		 + "	smp_dtls.lab_sample_dtls_id = :labSampleDtlsId  "
    		 + "	and smp_dtls.org_id = :orgId  "
    		 + "    and smp_dtls.org_unit_id = tem_ser_mper.unit_id  "
    		 + "	and smp_dtls.org_unit_id =:unitId  "
    		 + "	and test_mst.test_status = 'A'  "
    		 + "	and tem_ser_mper.is_deleted = 'N'  "
    		 + "	and tem_ser_mper.status = 'A'  "),
	
	

	
})
@Entity
@Table(name = "t_template_master" , schema = "public")
@SequenceGenerator(name = "t_template_master_seq" , sequenceName = "public.t_template_master_seq" , allocationSize = 1)
public class TemplateMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_template_master_seq")
	@Column(name = "template_id")
	private int templateId;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "speciality_id")
	private Integer specialityId;
	
	@Column(name = "sub_speciality_id")
	private Integer subSpecialityId;
	
	@Column(name = "template_object")
	private String templateObject;
	
	@Column(name = "status")
	private char status;
	
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
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "template_type_id")
	private Integer templateTypeId;
	
	@Column(name = "template_format_id")
	private Integer templateFormatId;

	@Column(name = "template_object_html")  //dilip
	private String templateObjectHtml;
	
	@Column(name = "form_template_id")
	private String formTemplateId;
	
	@Column(name = "form_template_keywords")
	private String formTemplateKeywords;
	
	@Column(name = "is_html_print")
	private Boolean isHtmlPrint;
	
	public Boolean getIsHtmlPrint() {
		return isHtmlPrint;
	}

	public void setIsHtmlPrint(Boolean isHtmlPrint) {
		this.isHtmlPrint = isHtmlPrint;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "templateMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TemplateDiagnosisMapper> TemplateDiagnosisMapperList;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "templateMaster")
	private List<TemplateTransaction> listTemplateTransaction;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "templateMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ICPCTemplateMapping> listICPCTemplateMapping;
	
	public Integer getTemplateTypeId() {
		return templateTypeId;
	}

	public void setTemplateTypeId(Integer templateTypeId) {
		this.templateTypeId = templateTypeId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'I');
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


	public List<TemplateDiagnosisMapper> getTemplateDiagnosisMapperList() {
		return TemplateDiagnosisMapperList;
	}

	public void setTemplateDiagnosisMapperList(List<TemplateDiagnosisMapper> templateDiagnosisMapperList) {
		TemplateDiagnosisMapperList = templateDiagnosisMapperList;
	}

	public Integer getTemplateFormatId() {
		return templateFormatId;
	}

	public void setTemplateFormatId(Integer templateFormatId) {
		this.templateFormatId = templateFormatId;
	}
	
	public String getTemplateObjectHtml() { //dilip
		return templateObjectHtml;
	}

	public void setTemplateObjectHtml(String templateObjectHtml) { //Dilip
		this.templateObjectHtml = templateObjectHtml;
	}

	public String getFormTemplateId() {
		return formTemplateId;
	}

	public void setFormTemplateId(String formTemplateId) {
		this.formTemplateId = formTemplateId;
	}

	public String getFormTemplateKeywords() {
		return formTemplateKeywords;
	}

	public void setFormTemplateKeywords(String formTemplateKeywords) {
		this.formTemplateKeywords = formTemplateKeywords;
	}

	
}

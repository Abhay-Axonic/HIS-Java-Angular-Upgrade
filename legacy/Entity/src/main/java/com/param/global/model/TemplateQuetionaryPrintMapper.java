package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_template_quetionary_print_mapper" , schema = "public")
@SequenceGenerator(name = "t_template_quetionary_print_mapper_seq" , sequenceName = "public.t_template_quetionary_print_mapper_seq" , allocationSize = 1)
public class TemplateQuetionaryPrintMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_template_quetionary_print_mapper_seq")
	@Column(name = "temp_que_print_mppr_id")
	
	private Integer tempQuePrintMpprId;
	
	@Column(name = "template_transaction_id")
	private Integer templateTransactionId;
	
	@Column(name = "summary_for_print")
	private String summaryForPrint;
	
	@Column(name = "queu_id")
	private String queuId;
	
	@Column(name = "que_title")
	private String queTitle;
	
	@Column(name = "que_type")
	private String queType;
	
	@Column(name = "answer")
	private String answer;
	
	@Column(name = "slug_value")
	private String slugValue;
	
	@Column(name = "slug_value_new")
	private String slugValueNew;
	
	@Column(name = "section_title")
	private String sectionTitle;
	
	@Column(name = "allow_to_print")
	private boolean allowToPrint;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "added_by")
	private Integer addedBy;
	
	@Column(name = "added_date")
	private Date addedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;

	public Integer getTempQuePrintMpprId() {
		return tempQuePrintMpprId;
	}

	public void setTempQuePrintMpprId(Integer tempQuePrintMpprId) {
		this.tempQuePrintMpprId = tempQuePrintMpprId;
	}

	public Integer getTemplateTransactionId() {
		return templateTransactionId;
	}

	public void setTemplateTransactionId(Integer templateTransactionId) {
		this.templateTransactionId = templateTransactionId;
	}

	public String getSummaryForPrint() {
		return summaryForPrint;
	}

	public void setSummaryForPrint(String summaryForPrint) {
		this.summaryForPrint = summaryForPrint;
	}

	public String getQueuId() {
		return queuId;
	}

	public void setQueuId(String queuId) {
		this.queuId = queuId;
	}

	public String getQueTitle() {
		return queTitle;
	}

	public void setQueTitle(String queTitle) {
		this.queTitle = queTitle;
	}

	public String getQueType() {
		return queType;
	}

	public void setQueType(String queType) {
		this.queType = queType;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getSlugValue() {
		return slugValue;
	}

	public void setSlugValue(String slugValue) {
		this.slugValue = slugValue;
	}

	public String getSlugValueNew() {
		return slugValueNew;
	}

	public void setSlugValueNew(String slugValueNew) {
		this.slugValueNew = slugValueNew;
	}

	public String getSectionTitle() {
		return sectionTitle;
	}

	public void setSectionTitle(String sectionTitle) {
		this.sectionTitle = sectionTitle;
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

	public Integer getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(Integer addedBy) {
		this.addedBy = addedBy;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
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

	public boolean isAllowToPrint() {
		return allowToPrint;
	}

	public void setAllowToPrint(boolean allowToPrint) {
		this.allowToPrint = allowToPrint;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}
	
	
}

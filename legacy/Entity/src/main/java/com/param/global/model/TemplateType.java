package com.param.global.model;

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
	@NamedNativeQuery(name = "GET_TEMPLATE_TYPES",
					 query = "select tt.template_type_id as id, tt.template_type_name as name "
					 		+" from public.m_template_type tt "
					 		+ "where tt.status = 'A' "
					 		+ " ORDER BY tt.template_type_name ASC ")
})
@Entity
@Table(name = "m_template_type" , schema = "public")
@SequenceGenerator(name = "m_template_type_seq" , sequenceName = "public.m_template_type_seq" , allocationSize = 1)
public class TemplateType {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "m_template_type_seq")
	@Column(name = "template_type_id")
	private int templateTypeId;
	
	@Column(name = "template_type_name")
	private String templateTypeName;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;

	public int getTemplateTypeId() {
		return templateTypeId;
	}

	public void setTemplateTypeId(int templateTypeId) {
		this.templateTypeId = templateTypeId;
	}

	public String getTemplateTypeName() {
		return templateTypeName;
	}

	public void setTemplateTypeName(String templateTypeName) {
		this.templateTypeName = templateTypeName;
	}

	public char getStatus() {
		return (status != '\u0000' ? status : 'A');
	}

	public void setStatus(char status) {
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
}

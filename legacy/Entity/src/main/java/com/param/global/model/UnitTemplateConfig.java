package com.param.global.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "unit_template_config_master", schema = "public")
@SequenceGenerator(name = "unit_template_config_master_seq", sequenceName = "public.unit_template_config_master_seq", allocationSize = 1)
public class UnitTemplateConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unit_template_config_master_seq")
	@Column(name = "master_id")
	private Integer masterId;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "api_key")
	private String apiKey;

	@Column(name = "status")
	private Character status;

	@Column(name = "created_by", updatable = false)
	private Integer createdBy;

	@Column(name = "created_date", updatable = false)
	@CreationTimestamp
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	@UpdateTimestamp
	private Date updatedDate;

	@Column(name = "template_url")
	private String templateUrl;
	
	@Column(name = "template_form")
	private String templateForm;

	
	public Integer getMasterId() {
		return masterId;
	}

	public void setMasterId(Integer masterId) {
		this.masterId = masterId;
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

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
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

	public String getTemplateUrl() {
		return templateUrl;
	}

	public void setTemplateUrl(String templateUrl) {
		this.templateUrl = templateUrl;
	}

	public String getTemplateForm() {
		return templateForm;
	}

	public void setTemplateForm(String templateForm) {
		this.templateForm = templateForm;
	}
        	
}

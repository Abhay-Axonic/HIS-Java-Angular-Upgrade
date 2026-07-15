package com.param.global.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.model.base.CommonEntity;
@Entity
@Table(name="button_master", schema="public")
@SequenceGenerator(name="button_master_seq",sequenceName = "public.button_master_seq", allocationSize = 1)
public class ButtonMaster{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "button_master_seq")
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "system_prompt")
	private String systemPrompt;
	
	@Column(name = "user_prompt")
	private String userPrompt;
	
	@Column(name = "label")
	private String label;
	
	@Column(name = "tab_id")
	private Integer tabId;
	

	@Column(name = "combined_ai_json")
	private String combinedAIJson;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "status")
	private Character status;
	
	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public String getCombinedAIJson() {
		return combinedAIJson;
	}

	public void setCombinedAIJson(String combinedAIJson) {
		this.combinedAIJson = combinedAIJson;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSystemPrompt() {
		return systemPrompt;
	}

	public void setSystemPrompt(String systemPrompt) {
		this.systemPrompt = systemPrompt;
	}

	public String getUserPrompt() {
		return userPrompt;
	}

	public void setUserPrompt(String userPrompt) {
		this.userPrompt = userPrompt;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getTabId() {
		return tabId;
	}

	public void setTabId(Integer tabId) {
		this.tabId = tabId;
	}
	
}
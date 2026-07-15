package com.param.opd.coversheet.dto;

import java.util.Date;

/*
 * 
 * Created by TUSHAR
 */

public class GroupMasterDTO {

	private Integer group_id;
	private String group_name;
	private String group_desc; 
	private String ref_entity;
	private String ledger_name;
	private Character status;
	private Integer created_by; 
	private Date created_date;
	private Integer hospital_id;
	private Integer updated_by;
	private Date updated_date;
	private Integer unit_id;
	private Integer organization_id;
	
	public Integer getGroup_id() {
		return group_id;
	}
	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public String getGroup_desc() {
		return group_desc;
	}
	public void setGroup_desc(String group_desc) {
		this.group_desc = group_desc;
	}
	public String getRef_entity() {
		return ref_entity;
	}
	public void setRef_entity(String ref_entity) {
		this.ref_entity = ref_entity;
	}
	public String getLedger_name() {
		return ledger_name;
	}
	public void setLedger_name(String ledger_name) {
		this.ledger_name = ledger_name;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	public Integer getCreated_by() {
		return created_by;
	}
	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	public Integer getHospital_id() {
		return hospital_id;
	}
	public void setHospital_id(Integer hospital_id) {
		this.hospital_id = hospital_id;
	}
	public Integer getUpdated_by() {
		return updated_by;
	}
	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
	}
	public Date getUpdated_date() {
		return updated_date;
	}
	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}
	public Integer getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(Integer unit_id) {
		this.unit_id = unit_id;
	}
	public Integer getOrganization_id() {
		return organization_id;
	}
	public void setOrganization_id(Integer organization_id) {
		this.organization_id = organization_id;
	}
}

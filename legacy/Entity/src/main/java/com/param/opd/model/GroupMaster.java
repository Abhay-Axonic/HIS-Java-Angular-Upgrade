package com.param.opd.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/*
 * 
 * Created by TUSHAR
 */
@Entity
@Table(name="m_group_master",schema="doctor")
@SequenceGenerator(name = "group_master_seq", sequenceName = "doctor.group_master_seq", allocationSize = 1)
public class GroupMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_master_seq")
	@Column(name = "group_id", unique = true, nullable = false)
	private Integer group_id;
	
	@Column(name = "group_name",nullable = false)
	private String group_name;
	
	@Column(name = "group_desc", nullable = false)
	private String group_desc;
	
	@Column(name = "ref_entity",nullable = false)
	private String ref_entity;
	
	@Column(name = "ledger_name", nullable = false)
	private String ledger_name;
	
	@Column(name = "status", nullable = false)
	private Character status;
	
	@Column(name = "created_by",nullable = false)
	private Integer created_by; 
	
	@Column(name = "created_date",nullable = false)
	private Date created_date;
	
	@Column(name = "group_episod_time_span",nullable = false)
	private Double group_episod_time_span;
	
	@Column(name = "hospital_id", nullable = false)
	private Integer hospital_id;
	
	@Column(name = "updated_by",nullable = false)
	private Integer updated_by; 
	
	@Column(name = "updated_date",nullable = false)
	private Date updated_date;
	
	@Column(name = "unit_id",nullable = false)
	private Integer unit_id;
	
	@Column(name = "organization_id",nullable = false)
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
	public Double getGroup_episod_time_span() {
		return group_episod_time_span;
	}
	public void setGroup_episod_time_span(Double group_episod_time_span) {
		this.group_episod_time_span = group_episod_time_span;
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

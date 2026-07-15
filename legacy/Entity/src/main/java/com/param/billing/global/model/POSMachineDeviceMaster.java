package com.param.billing.global.model;

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


@Entity
@Table(name = "m_pos_machine_device_master", schema = "billing")
@SequenceGenerator(name = "m_pos_machine_device_master_seq", sequenceName = "billing.m_pos_machine_device_master_seq", allocationSize = 1)
public class POSMachineDeviceMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_pos_machine_device_master_seq")
	@Column(name = "pos_machine_device_id")
	private int posMachineDeviceId;
	
	@Column(name = "device_name")
	private String deviceName;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "status")
	private char status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "machine_config_details")
	private String machineConfigDetails;

	@Column(name = "upload_link")
	private String uploadLink;

	public int getPosMachineDeviceId() {
		return posMachineDeviceId;
	}

	public void setPosMachineDeviceId(int posMachineDeviceId) {
		this.posMachineDeviceId = posMachineDeviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getMachineConfigDetails() {
		return machineConfigDetails;
	}

	public void setMachineConfigDetails(String machineConfigDetails) {
		this.machineConfigDetails = machineConfigDetails;
	}

	public String getUploadLink() {
		return uploadLink;
	}

	public void setUploadLink(String uploadLink) {
		this.uploadLink = uploadLink;
	}

	
}

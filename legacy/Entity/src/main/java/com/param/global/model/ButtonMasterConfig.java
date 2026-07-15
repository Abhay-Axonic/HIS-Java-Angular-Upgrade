package com.param.global.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@Entity
@Table(name="button_master_config", schema="public")
@SequenceGenerator(name="button_master_config_seq",sequenceName = "public.button_master_config_seq", allocationSize = 1)
public class ButtonMasterConfig {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "button_master_config_seq")
	@Column(name = "config_id")
	private Integer configId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "speciality_id")
	private Integer specialityId;
	
	@Column(name = "button_id")
	private Integer buttonId;
	
	public Integer getButtonId() {
		return buttonId;
	}

	public void setButtonId(Integer buttonId) {
		this.buttonId = buttonId;
	}

	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "json_configuration")
	private String json_configuration;

	@Column(name = "status")
	private Character status;
	
	@Column(name = "doctor_name")
	private String doctorName;
	
	@Column(name = "speciality_name")
	private String specialityName;
	
	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getButtonName() {
		return buttonName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}

	@Column(name = "button_name")
	private String buttonName;
	
	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getConfigId() {
		return configId;
	}

	public void setConfigId(Integer configId) {
		this.configId = configId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
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

	public String getJson_configuration() {
		return json_configuration;
	}

	public void setJson_configuration(String json_configuration) {
		this.json_configuration = json_configuration;
	}
}

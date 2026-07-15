package com.param.CSSDMaster.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AutoclaveMasterDto {
	private Integer id;

	private String machine_name;
	private String machine_description;
	private String sterilization_type;
	private Integer sterilization_type_id;
	private Integer biological_indicator_day;
	private Integer sterilization_temperature;
	private Integer sterilization_pressure;
	private Integer organizationId;
	private Integer unitId;
	private Boolean is_active;
	private Boolean is_deleted;

	private Integer created_by;
	private Integer updated_by;
	private String created_date;
	private String updated_date;
	private Character status;
	private Integer sterilization_color_id;
	private String colorName;
	private Integer colorId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMachine_name() {
		return machine_name;
	}
	public void setMachine_name(String machine_name) {
		this.machine_name = machine_name;
	}
	public String getMachine_description() {
		return machine_description;
	}
	public void setMachine_description(String machine_description) {
		this.machine_description = machine_description;
	}
	public String getSterilization_type() {
		return sterilization_type;
	}
	public void setSterilization_type(String sterilization_type) {
		this.sterilization_type = sterilization_type;
	}
	public Integer getSterilization_type_id() {
		return sterilization_type_id;
	}
	public void setSterilization_type_id(Integer sterilization_type_id) {
		this.sterilization_type_id = sterilization_type_id;
	}
	public Integer getBiological_indicator_day() {
		return biological_indicator_day;
	}
	public void setBiological_indicator_day(Integer biological_indicator_day) {
		this.biological_indicator_day = biological_indicator_day;
	}
	public Integer getSterilization_temperature() {
		return sterilization_temperature;
	}
	public void setSterilization_temperature(Integer sterilization_temperature) {
		this.sterilization_temperature = sterilization_temperature;
	}
	public Integer getSterilization_pressure() {
		return sterilization_pressure;
	}
	public void setSterilization_pressure(Integer sterilization_pressure) {
		this.sterilization_pressure = sterilization_pressure;
	}
	public Boolean getIs_active() {
		return is_active;
	}
	public void setIs_active(Boolean is_active) {
		this.is_active = is_active;
	}
	public Boolean getIs_deleted() {
		return is_deleted;
	}
	public void setIs_deleted(Boolean is_deleted) {
		this.is_deleted = is_deleted;
	}
	public Integer getCreated_by() {
		return created_by;
	}
	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}
	public Integer getUpdated_by() {
		return updated_by;
	}
	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	public String getUpdated_date() {
		return updated_date;
	}
	public void setUpdated_date(String updated_date) {
		this.updated_date = updated_date;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
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
	public Integer getSterilization_color_id() {
		return sterilization_color_id;
	}
	public void setSterilization_color_id(Integer sterilization_color_id) {
		this.sterilization_color_id = sterilization_color_id;
	}
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	public Integer getColorId() {
		return colorId;
	}
	public void setColorId(Integer colorId) {
		this.colorId = colorId;
	}

		
	

}

package com.param.pharmacy.master.global.dto.routeOfAdministration;

import java.util.Date;

public class RouteOfAdministrationRabbitDto {

	private Integer id;
	
	private String code;
	
	private String description;
	
	private Integer organization_id;
	
	private Integer unit_id;
	
	private Date updated_date;
	
	private Integer updated_by;
	
	private Date added_date;
	
	private Integer added_by;
	
	private Boolean is_deleted;
	
	private Boolean is_active;
	
	public RouteOfAdministrationRabbitDto() {
		super();
	}
	
	public RouteOfAdministrationRabbitDto(Integer id, String code, String description,
			Integer added_by,Date added_date, Integer updated_by, Date updated_date,
			Boolean is_deleted, Boolean is_active,Integer organization_id,Integer unit_id) {
		super();
		this.id = id;
		this.code = code;
		this.description = description;
		this.organization_id = organization_id;
		this.unit_id = unit_id;
		this.updated_date = updated_date;
		this.updated_by = updated_by;
		this.added_date = added_date;
		this.added_by = added_by;
		this.is_deleted = is_deleted;
		this.is_active = is_active;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getOrganization_id() {
		return organization_id;
	}

	public void setOrganization_id(Integer organization_id) {
		this.organization_id = organization_id;
	}

	public Integer getUnit_id() {
		return unit_id;
	}

	public void setUnit_id(Integer unit_id) {
		this.unit_id = unit_id;
	}

	public Date getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}

	public Integer getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
	}

	public Date getAdded_date() {
		return added_date;
	}

	public void setAdded_date(Date added_date) {
		this.added_date = added_date;
	}

	public Integer getAdded_by() {
		return added_by;
	}

	public void setAdded_by(Integer added_by) {
		this.added_by = added_by;
	}

	public Boolean getIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(Boolean is_deleted) {
		this.is_deleted = is_deleted;
	}

	public Boolean getIs_active() {
		return is_active;
	}

	public void setIs_active(Boolean is_active) {
		this.is_active = is_active;
	}
	
}

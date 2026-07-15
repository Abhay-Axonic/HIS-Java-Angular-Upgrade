package com.param.pharmacy.master.global.dto.strengthUnit;

import java.util.Date;

public class RabbitStrengthUnitDto {

	private Integer id;

	private String code;

	private String units;
	
	private Integer organization_id;
	
	private Integer unit_id;
	
	private Boolean is_active;
	
	private Boolean is_deleted;
	
	private Integer added_by;
	
	private Date added_date;
	
	private Integer updated_by;
	
	private Date updated_date;

	public RabbitStrengthUnitDto() {
		super();
	}
	
	public RabbitStrengthUnitDto(Integer id, String code, String units, Integer organization_id, Integer unit_id,
			Boolean is_active, Boolean is_deleted, Integer added_by, Date added_date, Integer updated_by,
			Date updated_date) {
		super();
		this.id = id;
		this.code = code;
		this.units = units;
		this.organization_id = organization_id;
		this.unit_id = unit_id;
		this.is_active = is_active;
		this.is_deleted = is_deleted;
		this.added_by = added_by;
		this.added_date = added_date;
		this.updated_by = updated_by;
		this.updated_date = updated_date;
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

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
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

	public Integer getAdded_by() {
		return added_by;
	}

	public void setAdded_by(Integer added_by) {
		this.added_by = added_by;
	}

	public Date getAdded_date() {
		return added_date;
	}

	public void setAdded_date(Date added_date) {
		this.added_date = added_date;
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
	
}

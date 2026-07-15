package com.param.pharmacy.master.global.dto.generic;

import java.util.Date;

public class GenericRabbitDto {

	private Integer id;

	private String code;

	private String name;

	private Integer product_category_id;
	
	private Integer organization_id;
	
	private Integer unit_id;
	
	private Date updated_date;
	
	private Integer updated_by;
	
	private Date added_date;
	
	private Integer added_by;
	
	private Boolean is_deleted;
	
	private Boolean is_active;
	
	public GenericRabbitDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GenericRabbitDto(Integer id, String code, String name, Integer product_category_id,Integer added_by, Date added_date,
			 Integer updated_by, Date updated_date,Boolean is_deleted, Boolean is_active, Integer organization_id,
			Integer unit_id
			) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.product_category_id = product_category_id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getProduct_category_id() {
		return product_category_id;
	}

	public void setProduct_category_id(Integer product_category_id) {
		this.product_category_id = product_category_id;
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

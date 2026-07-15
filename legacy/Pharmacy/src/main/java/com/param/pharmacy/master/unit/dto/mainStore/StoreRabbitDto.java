package com.param.pharmacy.master.unit.dto.mainStore;

import java.util.Date;

public class StoreRabbitDto {

	private Integer store_id;
	
	private String store_code;
	
	private String store_name;
	
	private Integer organization_id;
	
	private Integer unit_id;
	
	private Date updated_date;
	
	private Integer updated_by;
	
	private Date added_date;
	
	private Integer added_by;
	
	private Boolean is_deleted;
	
	private Boolean is_active;
	
	private StoreTypeRabbitDto storeType;
	
	private VisitTypeRabbitDto visitType;
	
	public StoreRabbitDto() {
		super();
	}
	
	public StoreRabbitDto(Integer store_id, String store_code, String store_name,  Integer added_by,Date added_date,
			Integer updated_by,Date updated_date, Boolean is_deleted, Boolean is_active,
			Integer organization_id,Integer unit_id, StoreTypeRabbitDto storeType, VisitTypeRabbitDto visitType) {
		super();
		this.store_id = store_id;
		this.store_code = store_code;
		this.store_name = store_name;
		this.organization_id = organization_id;
		this.unit_id = unit_id;
		this.updated_date = updated_date;
		this.updated_by = updated_by;
		this.added_date = added_date;
		this.added_by = added_by;
		this.is_deleted = is_deleted;
		this.is_active = is_active;
		this.storeType = storeType;
		this.visitType = visitType;
	}

	public StoreRabbitDto(Integer store_id, String store_code, String store_name,  Integer added_by,Date added_date,
						  Integer updated_by,Date updated_date, Boolean is_deleted, Boolean is_active,
						  Integer organization_id,Integer unit_id, StoreTypeRabbitDto storeType) {
		super();
		this.store_id = store_id;
		this.store_code = store_code;
		this.store_name = store_name;
		this.organization_id = organization_id;
		this.unit_id = unit_id;
		this.updated_date = updated_date;
		this.updated_by = updated_by;
		this.added_date = added_date;
		this.added_by = added_by;
		this.is_deleted = is_deleted;
		this.is_active = is_active;
		this.storeType = storeType;
	}

	public Integer getStore_id() {
		return store_id;
	}

	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}

	public String getStore_code() {
		return store_code;
	}

	public void setStore_code(String store_code) {
		this.store_code = store_code;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public StoreTypeRabbitDto getStoreType() {
		return storeType;
	}

	public void setStoreType(StoreTypeRabbitDto storeType) {
		this.storeType = storeType;
	}

	public VisitTypeRabbitDto getVisitType() {
		return visitType;
	}

	public void setVisitType(VisitTypeRabbitDto visitType) {
		this.visitType = visitType;
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

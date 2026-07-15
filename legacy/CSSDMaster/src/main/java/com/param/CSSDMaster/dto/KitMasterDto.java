package com.param.CSSDMaster.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class KitMasterDto {
	private Integer kitId;
	private String code;
	private String name;
	private Integer surgeon;
	private String surgeonName;
	private Integer storeId;
	private Integer organizationId;
	private Integer unitId;
	private Boolean is_active;
	private Boolean is_deleted;
	private Integer created_by;
	private Integer updated_by;
	private String created_date;
	private String updated_date;
	private Character status;
	private List<KitInstrumentMapperDto> kitInstrumentList;
	private Boolean isInstrument;

	public Integer getKitId() {
		return kitId;
	}

	public void setKitId(Integer kitId) {
		this.kitId = kitId;
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

	public Integer getSurgeon() {
		return surgeon;
	}

	public void setSurgeon(Integer surgeon) {
		this.surgeon = surgeon;
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

	

	public List<KitInstrumentMapperDto> getKitInstrumentList() {
		return kitInstrumentList;
	}

	public void setKitInstrumentList(List<KitInstrumentMapperDto> kitInstrumentList) {
		this.kitInstrumentList = kitInstrumentList;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getSurgeonName() {
		return surgeonName;
	}

	public void setSurgeonName(String surgeonName) {
		this.surgeonName = surgeonName;
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

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Boolean getIsInstrument() {
		return isInstrument;
	}

	public void setIsInstrument(Boolean isInstrument) {
		this.isInstrument = isInstrument;
	}
	
	

	
}

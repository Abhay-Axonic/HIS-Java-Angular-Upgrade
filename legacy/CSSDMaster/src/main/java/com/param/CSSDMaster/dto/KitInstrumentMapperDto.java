package com.param.CSSDMaster.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.EquipmentMasterDto;

@JsonInclude(Include.NON_NULL)
public class KitInstrumentMapperDto {

	private Integer id;
	private Integer kitInstrumentId;
	private Integer kitId;
	private Integer instrumentId;
	private String instrumentName;
	private String instrumentCode;
	private Integer quantity;

	private Integer created_by;
	private Integer updated_by;
	private String created_date;
	private String updated_date;
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private KitMasterDto kitMasterDto;
	private EquipmentMasterDto equipmentMasterDto;

	
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public KitMasterDto getKitMasterDto() {
		return kitMasterDto;
	}

	public void setKitMasterDto(KitMasterDto kitMasterDto) {
		this.kitMasterDto = kitMasterDto;
	}

	public EquipmentMasterDto getEquipmentMasterDto() {
		return equipmentMasterDto;
	}

	public void setEquipmentMasterDto(EquipmentMasterDto equipmentMasterDto) {
		this.equipmentMasterDto = equipmentMasterDto;
	}

	public Integer getKitId() {
		return kitId;
	}

	public void setKitId(Integer kitId) {
		this.kitId = kitId;
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

	public Integer getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(Integer instrumentId) {
		this.instrumentId = instrumentId;
	}

	public String getInstrumentName() {
		return instrumentName;
	}

	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}

	public String getInstrumentCode() {
		return instrumentCode;
	}

	public void setInstrumentCode(String instrumentCode) {
		this.instrumentCode = instrumentCode;
	}

	public Integer getKitInstrumentId() {
		return kitInstrumentId;
	}

	public void setKitInstrumentId(Integer kitInstrumentId) {
		this.kitInstrumentId = kitInstrumentId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
}

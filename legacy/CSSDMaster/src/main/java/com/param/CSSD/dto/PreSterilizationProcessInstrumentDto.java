package com.param.CSSD.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.entity.model.master.Store;

@JsonInclude(Include.NON_NULL)
public class PreSterilizationProcessInstrumentDto {
	
	
	private Integer id;
	private Integer instrumentId;
	private Integer kitId;
	private Integer kitInstrumentId;
	private Integer quantity;
	private Integer physicalQuantity;
	private String code;
	private String instrumentCode;
	private String instrumentName;
	private String status;
	private Integer transitionNumber;
	private Integer storeId;
	private Store store;
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getInstrumentId() {
		return instrumentId;
	}
	public void setInstrumentId(Integer instrumentId) {
		this.instrumentId = instrumentId;
	}
	public Integer getKitId() {
		return kitId;
	}
	public void setKitId(Integer kitId) {
		this.kitId = kitId;
	}
	public Integer getKitInstrumentId() {
		return kitInstrumentId;
	}
	public void setKitInstrumentId(Integer kitInstrumentId) {
		this.kitInstrumentId = kitInstrumentId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getPhysicalQuantity() {
		return physicalQuantity;
	}
	public void setPhysicalQuantity(Integer physicalQuantity) {
		this.physicalQuantity = physicalQuantity;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getInstrumentName() {
		return instrumentName;
	}
	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInstrumentCode() {
		return instrumentCode;
	}
	public void setInstrumentCode(String instrumentCode) {
		this.instrumentCode = instrumentCode;
	}
	public Integer getTransitionNumber() {
		return transitionNumber;
	}
	public void setTransitionNumber(Integer transitionNumber) {
		this.transitionNumber = transitionNumber;
	}
	public Integer getStoreId() {
		return storeId;
	}
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	
	

}

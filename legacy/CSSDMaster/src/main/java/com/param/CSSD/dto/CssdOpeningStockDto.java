package com.param.CSSD.dto;

import java.util.Date;
import java.util.List;

public class CssdOpeningStockDto {
	   
	  private String code;
	   
	   private Integer created_by;
	   
	   private String created_date;
	   
	   private Boolean isInstrument;
	   
	   private Character isKit;
	   
	   private Boolean isSelected;
	   
	   private Boolean isSterilize;
	   
	   private Integer kitId;
	   
	   private Integer kitQty;
	   
	   private String name;
	   
	   private String sterilizedExpiryDate;
	   
	   private Integer surgeon;
	   
	   private Integer updated_by;
	   
	   private String updated_date;
	   
	   private String instrumentCode;
	   
	   private Integer instrumentId;
	   
	   private String  instrumentName;
	   
	   private Integer kitInstrumentId;
	   
	   private Integer quantity;
	   
	   private Integer storeId;
		
	   private Integer organizationId;
	   
	   private Integer unitId;
	   
		protected Date opbDate;

		protected String opbNo;

		protected String storeName;

		protected String requestedBy;

		protected Integer statusId;

		protected String status;
		
		private Integer cssdOpeningBalanceId;
		
		private Integer originalInstrumentqty;
		
	  // private List<PreSterilizationProcessInstrumentDto> kitInstrumentList;
	   
	   private List<CssdOpeningStockDto> kitInstrumentList;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getCreated_by() {
		return created_by;
	}

	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}

	public String getCreated_date() {
		return created_date;
	}

	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}

	public Boolean getIsInstrument() {
		return isInstrument;
	}

	public void setIsInstrument(Boolean isInstrument) {
		this.isInstrument = isInstrument;
	}

	public Character getIsKit() {
		return isKit;
	}

	public void setIsKit(Character isKit) {
		this.isKit = isKit;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Boolean getIsSterilize() {
		return isSterilize;
	}

	public void setIsSterilize(Boolean isSterilize) {
		this.isSterilize = isSterilize;
	}

	public Integer getKitId() {
		return kitId;
	}

	public void setKitId(Integer kitId) {
		this.kitId = kitId;
	}

	public Integer getKitQty() {
		return kitQty;
	}

	public void setKitQty(Integer kitQty) {
		this.kitQty = kitQty;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSterilizedExpiryDate() {
		return sterilizedExpiryDate;
	}

	public void setSterilizedExpiryDate(String sterilizedExpiryDate) {
		this.sterilizedExpiryDate = sterilizedExpiryDate;
	}

	public Integer getSurgeon() {
		return surgeon;
	}

	public void setSurgeon(Integer surgeon) {
		this.surgeon = surgeon;
	}

	public Integer getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
	}

	public String getUpdated_date() {
		return updated_date;
	}

	public void setUpdated_date(String updated_date) {
		this.updated_date = updated_date;
	}

	public String getInstrumentCode() {
		return instrumentCode;
	}

	public void setInstrumentCode(String instrumentCode) {
		this.instrumentCode = instrumentCode;
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


	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
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


	public Integer getCssdOpeningBalanceId() {
		return cssdOpeningBalanceId;
	}

	public void setCssdOpeningBalanceId(Integer cssdOpeningBalanceId) {
		this.cssdOpeningBalanceId = cssdOpeningBalanceId;
	}

	public Date getOpbDate() {
		return opbDate;
	}

	public void setOpbDate(Date opbDate) {
		this.opbDate = opbDate;
	}

	public String getOpbNo() {
		return opbNo;
	}

	public void setOpbNo(String opbNo) {
		this.opbNo = opbNo;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<CssdOpeningStockDto> getKitInstrumentList() {
		return kitInstrumentList;
	}

	public void setKitInstrumentList(List<CssdOpeningStockDto> kitInstrumentList) {
		this.kitInstrumentList = kitInstrumentList;
	}

	public Integer getOriginalInstrumentqty() {
		return originalInstrumentqty;
	}

	public void setOriginalInstrumentqty(Integer originalInstrumentqty) {
		this.originalInstrumentqty = originalInstrumentqty;
	}
	   
	   
	   
	   
}

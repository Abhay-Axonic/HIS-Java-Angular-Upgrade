package com.param.CSSD.dto;


import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.CSSDMaster.dto.WrapperExpiryMasterDto;
import com.param.entity.model.master.Store;


public class PreSterilizationProcessDto {
	
	private Integer transitionNumber;
	private Integer kitId;
	private Integer wrapperId;
	private Integer storeId;
	private Store store;
	private Integer wrapperQuantity;
	private String wrapperName;
	private Integer expiryDuration;
	private WrapperExpiryMasterDto wrapper;
	private Integer physicalQuantity;
	private String code;
	private String name;
	private String status;
	private String transition;
	private Integer created_by;
	private Integer updated_by;
	private String created_date;
	private String formation_date;
	private String expiryDate;
	private Integer batchNumber;
	private Integer autoclaveNumber;
	private String autoclaveDate;
	private String updated_date;
	private List<PreSterilizationProcessInstrumentDto> kitInstrumentList;
	private Integer organizationId;
	private Integer unitId;
	private String reasonForReturn;
	private Boolean isInstrument;
	private BigInteger batchQuantity;
	private Integer sterilizedStockQty;
	private Integer inTransitQty;
	private String storeName;
	private Integer userId;
	
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
	public Integer getTransitionNumber() {
		return transitionNumber;
	}
	public void setTransitionNumber(Integer transitionNumber) {
		this.transitionNumber = transitionNumber;
	}
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public Integer getWrapperId() {
		return wrapperId;
	}
	public void setWrapperId(Integer wrapperId) {
		this.wrapperId = wrapperId;
	}
	public Integer getPhysicalQuantity() {
		return physicalQuantity;
	}
	public void setPhysicalQuantity(Integer physicalQuantity) {
		this.physicalQuantity = physicalQuantity;
	}
	
	public List<PreSterilizationProcessInstrumentDto> getKitInstrumentList() {
		return kitInstrumentList;
	}
	public void setKitInstrumentList(List<PreSterilizationProcessInstrumentDto> kitInstrumentList) {
		this.kitInstrumentList = kitInstrumentList;
	}
	public String getFormation_date() {
		return formation_date;
	}
	public void setFormation_date(String formation_date) {
		this.formation_date = formation_date;
	}
	public String getTransition() {
		return transition;
	}
	public void setTransition(String transition) {
		this.transition = transition;
	}
	public Integer getWrapperQuantity() {
		return wrapperQuantity;
	}
	public void setWrapperQuantity(Integer wrapperQuantity) {
		this.wrapperQuantity = wrapperQuantity;
	}
	public WrapperExpiryMasterDto getWrapper() {
		return wrapper;
	}
	public void setWrapper(WrapperExpiryMasterDto wrapper) {
		this.wrapper = wrapper;
	}
	public String getWrapperName() {
		return wrapperName;
	}
	public void setWrapperName(String wrapperName) {
		this.wrapperName = wrapperName;
	}
	public Integer getExpiryDuration() {
		return expiryDuration;
	}
	public void setExpiryDuration(Integer expiryDuration) {
		this.expiryDuration = expiryDuration;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public Integer getBatchNumber() {
		return batchNumber;
	}
	public void setBatchNumber(Integer batchNumber) {
		this.batchNumber = batchNumber;
	}
	public Integer getAutoclaveNumber() {
		return autoclaveNumber;
	}
	public void setAutoclaveNumber(Integer autoclaveNumber) {
		this.autoclaveNumber = autoclaveNumber;
	}
	public String getAutoclaveDate() {
		return autoclaveDate;
	}
	public void setAutoclaveDate(String autoclaveDate) {
		this.autoclaveDate = autoclaveDate;
	}
	public String getReasonForReturn() {
		return reasonForReturn;
	}
	public void setReasonForReturn(String reasonForReturn) {
		this.reasonForReturn = reasonForReturn;
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
	public Boolean getIsInstrument() {
		return isInstrument;
	}
	public void setIsInstrument(Boolean isInstrument) {
		this.isInstrument = isInstrument;
	}
	
	public Integer getSterilizedStockQty() {
		return sterilizedStockQty;
	}
	public void setSterilizedStockQty(Integer sterilizedStockQty) {
		this.sterilizedStockQty = sterilizedStockQty;
	}
	public Integer getInTransitQty() {
		return inTransitQty;
	}
	public void setInTransitQty(Integer inTransitQty) {
		this.inTransitQty = inTransitQty;
	}
	public BigInteger getBatchQuantity() {
		return batchQuantity;
	}
	public void setBatchQuantity(BigInteger batchQuantity) {
		this.batchQuantity = batchQuantity;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	

}

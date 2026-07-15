package com.param.CSSD.dto;

import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.CSSDMaster.dto.KitInstrumentMapperDto;


/*@JsonInclude(Include.NON_NULL)*/
public class IssuedKitTransitionMapperDto {

	private Integer id;
	
	private Integer issueNo;
	
	private Integer kitId;
	
	private Integer transitionNumber;

	private String kitCode;
	
	private String kitName;
	
	private Integer quantity;
	
	private Integer autoclaveNumber;
	
	private String autoclaveDate;
	
	private String expiryDate;
	
	private Integer batchNumber;

	private String code;
	
	private String name;
	
	private Boolean isAccepted;
	
	private Boolean isRejected;
	
	private Integer rejectReasonId;
	
	private String rejectReasonNote;
	
	private Integer wrapperId;
	
	private Integer storeId;
	
	private String  store;
	
	private Integer  wrapperQuantity;
	
	private String  wrapperName;
	
	private String wrapper;
	
	private String updated_date;
	
	private Integer updated_by;
	
	private Integer unitId;
	
	private String transition;
	
	private Character status;
	
	private String reasonForReturn;
	
	private Integer physicalQuantity;
	
	private Integer organizationId;
	
	private Boolean	isInstrument;
	
	private String formation_date;
	
	private Integer expiryDuration;
	
	private String created_date;
	
	private Integer created_by;
	
	private BigInteger batchQuantity;
	private Integer sterilizedStockQty;
	private Integer inTransitQty;
	private String storeName;
	
	private Integer userId;
	
	private List<KitInstrumentMapperDto> kitInstrumentList;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getIssueNo() {
		return issueNo;
	}
	public void setIssueNo(Integer issueNo) {
		this.issueNo = issueNo;
	}
	public Integer getKitId() {
		return kitId;
	}
	public void setKitId(Integer kitId) {
		this.kitId = kitId;
	}
	public Integer getTransitionNumber() {
		return transitionNumber;
	}
	public void setTransitionNumber(Integer transitionNumber) {
		this.transitionNumber = transitionNumber;
	}
	public String getKitCode() {
		return kitCode;
	}
	public void setKitCode(String kitCode) {
		this.kitCode = kitCode;
	}
	public String getKitName() {
		return kitName;
	}
	public void setKitName(String kitName) {
		this.kitName = kitName;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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
	public List<KitInstrumentMapperDto> getKitInstrumentList() {
		return kitInstrumentList;
	}
	public void setKitInstrumentList(List<KitInstrumentMapperDto> kitInstrumentList) {
		this.kitInstrumentList = kitInstrumentList;
	}
	public Boolean getIsAccepted() {
		return isAccepted;
	}
	public void setIsAccepted(Boolean isAccepted) {
		this.isAccepted = isAccepted;
	}
	public Boolean getIsRejected() {
		return isRejected;
	}
	public void setIsRejected(Boolean isRejected) {
		this.isRejected = isRejected;
	}
	public Integer getRejectReasonId() {
		return rejectReasonId;
	}
	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}
	public String getRejectReasonNote() {
		return rejectReasonNote;
	}
	public void setRejectReasonNote(String rejectReasonNote) {
		this.rejectReasonNote = rejectReasonNote;
	}
	public Integer getWrapperId() {
		return wrapperId;
	}
	public void setWrapperId(Integer wrapperId) {
		this.wrapperId = wrapperId;
	}
	public Integer getStoreId() {
		return storeId;
	}
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public Integer getWrapperQuantity() {
		return wrapperQuantity;
	}
	public void setWrapperQuantity(Integer wrapperQuantity) {
		this.wrapperQuantity = wrapperQuantity;
	}
	public String getWrapperName() {
		return wrapperName;
	}
	public void setWrapperName(String wrapperName) {
		this.wrapperName = wrapperName;
	}
	public String getWrapper() {
		return wrapper;
	}
	public void setWrapper(String wrapper) {
		this.wrapper = wrapper;
	}
	public String getUpdated_date() {
		return updated_date;
	}
	public void setUpdated_date(String updated_date) {
		this.updated_date = updated_date;
	}
	public Integer getUpdated_by() {
		return updated_by;
	}
	public void setUpdated_by(Integer updated_by) {
		this.updated_by = updated_by;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public String getTransition() {
		return transition;
	}
	public void setTransition(String transition) {
		this.transition = transition;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	public String getReasonForReturn() {
		return reasonForReturn;
	}
	public void setReasonForReturn(String reasonForReturn) {
		this.reasonForReturn = reasonForReturn;
	}
	public Integer getPhysicalQuantity() {
		return physicalQuantity;
	}
	public void setPhysicalQuantity(Integer physicalQuantity) {
		this.physicalQuantity = physicalQuantity;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	public Boolean getIsInstrument() {
		return isInstrument;
	}
	public void setIsInstrument(Boolean isInstrument) {
		this.isInstrument = isInstrument;
	}
	public String getFormation_date() {
		return formation_date;
	}
	public void setFormation_date(String formation_date) {
		this.formation_date = formation_date;
	}
	public Integer getExpiryDuration() {
		return expiryDuration;
	}
	public void setExpiryDuration(Integer expiryDuration) {
		this.expiryDuration = expiryDuration;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	public Integer getCreated_by() {
		return created_by;
	}
	public void setCreated_by(Integer created_by) {
		this.created_by = created_by;
	}
	public BigInteger getBatchQuantity() {
		return batchQuantity;
	}
	public void setBatchQuantity(BigInteger batchQuantity) {
		this.batchQuantity = batchQuantity;
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

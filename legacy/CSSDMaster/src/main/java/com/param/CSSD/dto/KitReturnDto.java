package com.param.CSSD.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.CSSDMaster.dto.KitInstrumentMapperDto;


@JsonInclude(Include.NON_NULL)
public class KitReturnDto {


	private Integer returnNo;

	private String returnDate;
	
	private String createdDate;
	
	private String updatedDate;
	
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private Integer fromStoreId;
	
	private Integer toStoreId;
	
	private String status;
	
	private String reason;
	
	private String type;
	
	private String remark;
	
	private String carrier;
	
	private String fromStore;
	
	private String toStore;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer cancelReasonId;
	
	private String cancelReasonNote;
	
	private String userName;

	private List<KitReturnKitMapperDto> kitList;
	
	private List<ReturnedKitTransitionMapperDto> subKitList;
	
	private List<KitInstrumentMapperDto> kitInstrumentList;

	public Integer getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(Integer returnNo) {
		this.returnNo = returnNo;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getFromStoreId() {
		return fromStoreId;
	}

	public void setFromStoreId(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
	}

	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public List<KitReturnKitMapperDto> getKitList() {
		return kitList;
	}

	public void setKitList(List<KitReturnKitMapperDto> kitList) {
		this.kitList = kitList;
	}

	public String getFromStore() {
		return fromStore;
	}

	public void setFromStore(String fromStore) {
		this.fromStore = fromStore;
	}

	public String getToStore() {
		return toStore;
	}

	public void setToStore(String toStore) {
		this.toStore = toStore;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public List<KitInstrumentMapperDto> getKitInstrumentList() {
		return kitInstrumentList;
	}

	public void setKitInstrumentList(List<KitInstrumentMapperDto> kitInstrumentList) {
		this.kitInstrumentList = kitInstrumentList;
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

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public String getCancelReasonNote() {
		return cancelReasonNote;
	}

	public void setCancelReasonNote(String cancelReasonNote) {
		this.cancelReasonNote = cancelReasonNote;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<ReturnedKitTransitionMapperDto> getSubKitList() {
		return subKitList;
	}

	public void setSubKitList(List<ReturnedKitTransitionMapperDto> subKitList) {
		this.subKitList = subKitList;
	}
	
	
	
	
		
}

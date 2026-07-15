package com.param.CSSD.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.CSSDMaster.dto.KitInstrumentMapperDto;

/*@JsonInclude(Include.NON_NULL)*/
public class KitIssueKitMapperDto {


	private Integer id;

	private Integer issueNo;
	
	private Integer kitId;
	
	private String kitCode;
	
	private String kitName;
	
	private Integer quantity;
	
	private Integer autoclaveNumber;
	
	private String autoclaveDate;
	
	private String expiryDate;
	
	private String formationDate;
	
	private Integer batchNo;
	
	private Integer batchNumber;
	
	private Integer transitionNumber;
	
	private String code;
	
	private String name;
	
	private String status;
	
	private Integer surgeon;
	
	private Boolean is_active;
	
	private Boolean is_deleted;
	
	private Boolean  isInstrument;

	private String created_date;
	
	private String updated_date;
	
	private Integer created_by;
	
	private Integer updated_by;
	
	private Integer rejectReasonId;
	
	private String rejectReasonNote;
	
	private Integer availableStock;
	
	private Integer issuedQuantity;
	
	private Integer acceptedQuantity;
	
	private Integer rejectedQuantity;
	
	private Boolean reject;
	
	private Character isSelected;
	private Boolean isAccepted;
	private Boolean isRejected;
	
	private Integer requestNo;
	
	private List<KitInstrumentMapperDto> kitInstrumentList;
	
	private List<IssuedKitTransitionMapperDto> subKitList;

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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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

	public Integer getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(Integer batchNo) {
		this.batchNo = batchNo;
	}

	

	public Integer getTransitionNumber() {
		return transitionNumber;
	}

	public void setTransitionNumber(Integer transitionNumber) {
		this.transitionNumber = transitionNumber;
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

	public List<KitInstrumentMapperDto> getKitInstrumentList() {
		return kitInstrumentList;
	}

	public void setKitInstrumentList(List<KitInstrumentMapperDto> kitInstrumentList) {
		this.kitInstrumentList = kitInstrumentList;
	}

	public List<IssuedKitTransitionMapperDto> getSubKitList() {
		return subKitList;
	}

	public void setSubKitList(List<IssuedKitTransitionMapperDto> subKitList) {
		this.subKitList = subKitList;
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

	public Integer getAvailableStock() {
		return availableStock;
	}

	public void setAvailableStock(Integer availableStock) {
		this.availableStock = availableStock;
	}

	public Integer getIssuedQuantity() {
		return issuedQuantity;
	}

	public void setIssuedQuantity(Integer issuedQuantity) {
		this.issuedQuantity = issuedQuantity;
	}

	public Integer getAcceptedQuantity() {
		return acceptedQuantity;
	}

	public void setAcceptedQuantity(Integer acceptedQuantity) {
		this.acceptedQuantity = acceptedQuantity;
	}

	public Integer getRejectedQuantity() {
		return rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
	}

	public Boolean getReject() {
		return reject;
	}

	public void setReject(Boolean reject) {
		this.reject = reject;
	}

	public String getFormationDate() {
		return formationDate;
	}

	public void setFormationDate(String formationDate) {
		this.formationDate = formationDate;
	}

	public Integer getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(Integer requestNo) {
		this.requestNo = requestNo;
	}

	public Integer getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(Integer batchNumber) {
		this.batchNumber = batchNumber;
	}


	public Boolean getIsInstrument() {
		return isInstrument;
	}

	public void setIsInstrument(Boolean isInstrument) {
		this.isInstrument = isInstrument;
	}

	public Character getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Character isSelected) {
		this.isSelected = isSelected;
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

		
}

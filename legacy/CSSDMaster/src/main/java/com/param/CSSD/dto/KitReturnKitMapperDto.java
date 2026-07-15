package com.param.CSSD.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.CSSDMaster.dto.KitInstrumentMapperDto;

@JsonInclude(Include.NON_NULL)
public class KitReturnKitMapperDto {

	private Integer id;

	private Integer returnNo;
	
	private Integer kitId;
	
	private Integer batchNumber;
	
	private String kitCode;
	
	private String kitName;
	
	private String expiryDate;
	
	private Integer returnQuantity;
	
	private String reasonForReturn;

	private Integer issueNo;
	
	private Integer quantity;
	
	private Integer autoclaveNumber;
	
	private String autoclaveDate;
	
	private Integer batchNo;
	
	private Integer transtionNumber;
	
	private String code;
	
	private String name;
	
	private String status;
	
	private Integer surgeon;
	
	private Boolean is_active;
	
	private Boolean is_deleted;
	
	private String created_date;
	
	private String updated_date;
	
	private Integer created_by;
	
	private Integer updated_by;
	
	private Integer rejectReasonId;
	
	private String rejectReasonNote;
	
	private Integer availableStock;
	
	
	private List<KitInstrumentMapperDto> kitInstrumentList;
	
	private List<ReturnedKitTransitionMapperDto> subKitList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(Integer returnNo) {
		this.returnNo = returnNo;
	}

	public Integer getKitId() {
		return kitId;
	}

	public void setKitId(Integer kitId) {
		this.kitId = kitId;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public String getReasonForReturn() {
		return reasonForReturn;
	}

	public void setReasonForReturn(String reasonForReturn) {
		this.reasonForReturn = reasonForReturn;
	}

	public Integer getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(Integer batchNumber) {
		this.batchNumber = batchNumber;
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

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(Integer issueNo) {
		this.issueNo = issueNo;
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

	public Integer getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(Integer batchNo) {
		this.batchNo = batchNo;
	}

	public Integer getTranstionNumber() {
		return transtionNumber;
	}

	public void setTranstionNumber(Integer transtionNumber) {
		this.transtionNumber = transtionNumber;
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

	public List<KitInstrumentMapperDto> getKitInstrumentList() {
		return kitInstrumentList;
	}

	public void setKitInstrumentList(List<KitInstrumentMapperDto> kitInstrumentList) {
		this.kitInstrumentList = kitInstrumentList;
	}

	public List<ReturnedKitTransitionMapperDto> getSubKitList() {
		return subKitList;
	}

	public void setSubKitList(List<ReturnedKitTransitionMapperDto> subKitList) {
		this.subKitList = subKitList;
	}

	
	
		
}

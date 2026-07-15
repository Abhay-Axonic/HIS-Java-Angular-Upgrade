package com.param.procurement.quotation.dto.physicalInspection;

import java.util.List;

import com.param.entity.model.procurement.GrnBonusBatchExpiry;

public class PhysicalInspectionSaveItem
{
	private Integer id;
	
    private Integer approvedQuantity;
    private Integer bonusApprovedQuantity;
    private Integer rejectQuantity;
    	
    private Integer rejectReasonId;
    
    private String rejectNote;
    
    private Integer statusId;
    private Double lcop;
    private List<GrnBonusBatchExpiry> grnBonusBatchExpiry;
    
    public Integer getBonusApprovedQuantity() {
		return bonusApprovedQuantity;
	}

	public void setBonusApprovedQuantity(Integer bonusApprovedQuantity) {
		this.bonusApprovedQuantity = bonusApprovedQuantity;
	}

	public Double getLcop() {
		return lcop;
	}

	public void setLcop(Double lcop) {
		this.lcop = lcop;
	}

	private List<StorageDetails> storageDetails;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public List<StorageDetails> getStorageDetails() {
		return storageDetails;
	}

	public void setStorageDetails(List<StorageDetails> storageDetails) {
		this.storageDetails = storageDetails;
	}

	public List<GrnBonusBatchExpiry> getGrnBonusBatchExpiry() {
		return grnBonusBatchExpiry;
	}

	public void setGrnBonusBatchExpiry(List<GrnBonusBatchExpiry> grnBonusBatchExpiry) {
		this.grnBonusBatchExpiry = grnBonusBatchExpiry;
	}
	
}
package com.param.pharmacy.ip.dto.assignPicker;

public class AssignPickerDetailItem {
	private Long prescriptionDetailId;

	private Long batchId;

	private Integer uomTypeId;

	private Integer uomUnitId;

	private Integer quanityIssued;

	private Integer id;
	private Integer itemId;
	private Integer batchid;

	private Integer statusId;
	private Integer parentAssignPickerId;
	private Integer assignPickerId;
	private Boolean isActive;
	private Boolean isDeleted;
	private Integer pickedQuantity;
	private Integer issueDetailId;
	private Integer prescriptionDetailid;
	
	private Integer parentItemId;
	
	private Boolean isNarcotics;
	
	private PrescriptionDetailsDto prescriptionDetailsDto;
	
	private AssignPickerDetailItem assignPickerDetailItemDto;

	public Long getPrescriptionDetailId() {
		return prescriptionDetailId;
	}

	public void setPrescriptionDetailId(Long prescriptionDetailId) {
		this.prescriptionDetailId = prescriptionDetailId;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}

	public Integer getQuanityIssued() {
		return quanityIssued;
	}

	public void setQuanityIssued(Integer quanityIssued) {
		this.quanityIssued = quanityIssued;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getParentAssignPickerId() {
		return parentAssignPickerId;
	}

	public void setParentAssignPickerId(Integer parentAssignPickerId) {
		this.parentAssignPickerId = parentAssignPickerId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getAssignPickerId() {
		return assignPickerId;
	}

	public void setAssignPickerId(Integer assignPickerId) {
		this.assignPickerId = assignPickerId;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getPickedQuantity() {
		return pickedQuantity;
	}

	public void setPickedQuantity(Integer pickedQuantity) {
		this.pickedQuantity = pickedQuantity;
	}

	public Integer getIssueDetailId() {
		return issueDetailId;
	}

	public void setIssueDetailId(Integer issueDetailId) {
		this.issueDetailId = issueDetailId;
	}

	public Integer getPrescriptionDetailid() {
		return prescriptionDetailid;
	}

	public void setPrescriptionDetailid(Integer prescriptionDetailid) {
		this.prescriptionDetailid = prescriptionDetailid;
	}

	public Integer getBatchid() {
		return batchid;
	}

	public void setBatchid(Integer batchid) {
		this.batchid = batchid;
	}

	public Integer getParentItemId() {
		return parentItemId;
	}

	public void setParentItemId(Integer parentItemId) {
		this.parentItemId = parentItemId;
	}

	public Boolean getIsNarcotics() {
		return isNarcotics;
	}

	public void setIsNarcotics(Boolean isNarcotics) {
		this.isNarcotics = isNarcotics;
	}

	public PrescriptionDetailsDto getPrescriptionDetailsDto() {
		return prescriptionDetailsDto;
	}

	public void setPrescriptionDetailsDto(PrescriptionDetailsDto prescriptionDetailsDto) {
		this.prescriptionDetailsDto = prescriptionDetailsDto;
	}

	public AssignPickerDetailItem getAssignPickerDetailItemDto() {
		return assignPickerDetailItemDto;
	}

	public void setAssignPickerDetailItemDto(AssignPickerDetailItem assignPickerDetailItemDto) {
		this.assignPickerDetailItemDto = assignPickerDetailItemDto;
	}


}
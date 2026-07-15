package com.param.assetManagement.fixedAssetOpeningBalance.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AccessoriesDetails {

	protected Integer id;

	protected Integer itemId;

	protected String itemCode;

	protected String itemName;

	protected Long batchId;

	protected String batchNo;

	protected String serialNumber;

	protected Date warrantyFrom;

	protected Date warrantyTo;

	protected String specification;

	protected Integer quantity;

	protected Integer approvedQuantity;

	protected Integer workingConditionId;

	protected String workingCondition;

	protected Integer rejectQuantity;

	protected Integer rejectReasonId;

	protected String rejectNote;
	// start code for resolved HIS-9716 Fixed asset opening balance - Pending quantity column not visible
	protected Long maxOrderQuantity;

	public Long getMaxOrderQuantity() {
		return maxOrderQuantity;
	}

	public void setMaxOrderQuantity(Long maxOrderQuantity) {
		this.maxOrderQuantity = maxOrderQuantity;
	}
	// end code for resolved HIS-9716 Fixed asset opening balance - Pending quantity column not visible

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Date getWarrantyFrom() {
		return warrantyFrom;
	}

	public void setWarrantyFrom(Date warrantyFrom) {
		this.warrantyFrom = warrantyFrom;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Date getWarrantyTo() {
		return warrantyTo;
	}

	public void setWarrantyTo(Date warrantyTo) {
		this.warrantyTo = warrantyTo;
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

	public AccessoriesDetails() {
		super();
	}

	public AccessoriesDetails(Integer id, Integer itemId, String itemCode, String itemName, String serialNumber,
			Date warrantyFrom, Date warrantyTo, String specification, Integer quantity, Integer workingConditionId,
			String workingCondition) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.serialNumber = serialNumber;
		this.warrantyFrom = warrantyFrom;
		this.warrantyTo = warrantyTo;
		this.specification = specification;
		this.quantity = quantity;
		this.workingConditionId = workingConditionId;
		this.workingCondition = workingCondition;
	}

	public AccessoriesDetails(Integer id, Integer itemId, String itemCode, String itemName, String specification,
			Integer quantity, Integer approvedQuantity) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.specification = specification;
		this.quantity = quantity;
		this.approvedQuantity = approvedQuantity;
	}

	public AccessoriesDetails(Integer id, Integer itemId, String itemCode, String itemName, String specification,
			Integer quantity, Integer approvedQuantity, Long batchId, String batchNo) {
		this(id, itemId, itemCode, itemName, specification, quantity, approvedQuantity);
		this.batchId = batchId;
		this.batchNo = batchNo;
	}

	public AccessoriesDetails(Integer id, Integer itemId, String itemCode, String itemName, Long batchId,
			String batchNo, String specification, Integer quantity, Integer approvedQuantity) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.specification = specification;
		this.quantity = quantity;
		this.approvedQuantity = approvedQuantity;
	}

	public Integer getWorkingConditionId() {
		return workingConditionId;
	}

	public void setWorkingConditionId(Integer workingConditionId) {
		this.workingConditionId = workingConditionId;
	}

	public String getWorkingCondition() {
		return workingCondition;
	}

	public void setWorkingCondition(String workingCondition) {
		this.workingCondition = workingCondition;
	}
	
	// start code for resolved HIS-9716 Fixed asset opening balance - Pending quantity column not visible
	public AccessoriesDetails(Integer id, Integer itemId, String itemCode, String itemName, String serialNumber,
			Date warrantyFrom, Date warrantyTo, String specification, Integer quantity, Integer workingConditionId,
			String workingCondition,Long maxOrderQuantity) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.serialNumber = serialNumber;
		this.warrantyFrom = warrantyFrom;
		this.warrantyTo = warrantyTo;
		this.specification = specification;
		this.quantity = quantity;
		this.workingConditionId = workingConditionId;
		this.workingCondition = workingCondition;
		this.maxOrderQuantity = maxOrderQuantity;
	}
	// end code for resolved HIS-9716 Fixed asset opening balance - Pending quantity column not visible

}
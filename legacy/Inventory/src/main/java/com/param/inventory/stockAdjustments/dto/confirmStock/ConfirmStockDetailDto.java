package com.param.inventory.stockAdjustments.dto.confirmStock;

import java.util.Date;

public class ConfirmStockDetailDto {
	
	private Integer id;
	
	private Integer itemId;
	
	private String itemName;
	
	private String itemCode;
	
	private String productCategory;
	
	private String uomType;
	
	private String uomUnit;
	
	private Long batchId;
	
	private String batchNo;
	
	private Date expiryDate;
	
	private Double cop;
	
	private Double mrp;
	
	private Long quantity;
	
	private Integer uploadedQuantity;
	
	private Date newExpiryDate;
	
	private Double newMrp;
	
	private Integer approvalQuantity;
	
	private Integer rejectQunatity;
	
	private Integer rejectReasonId;
	
	private String rejectReason;
	
	private String Status; 
	
	private Integer avialableQuantity;

	public ConfirmStockDetailDto() {
		super();
	}

	public ConfirmStockDetailDto(Integer id, Integer itemId, String itemName, String itemCode, String productCategory,
			String uomType, String uomUnit, Long batchId, String batchNo, Date expiryDate, Double cop, Double mrp,
			Long quantity, Integer uploadedQuantity, Date newExpiryDate, Double newMrp, Integer approvalQuantity,
			Integer rejectQunatity, Integer rejectReasonId, String rejectReason) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.productCategory = productCategory;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.cop = cop;
		this.mrp = mrp;
		this.quantity = quantity;
		this.uploadedQuantity = uploadedQuantity;
		this.newExpiryDate = newExpiryDate;
		this.newMrp = newMrp;
		this.approvalQuantity = approvalQuantity;
		this.rejectQunatity = rejectQunatity;
		this.rejectReasonId = rejectReasonId;
		this.rejectReason = rejectReason;
	}


	public ConfirmStockDetailDto(Integer id, Integer itemId, String itemName, String itemCode, String productCategory,
			String uomType, String uomUnit, Long batchId, String batchNo, Date expiryDate, Double cop, Double mrp,
			Long quantity, Integer uploadedQuantity, Date newExpiryDate, Double newMrp, Integer approvalQuantity,
			Integer rejectQunatity,String rejectReason,String Status) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.productCategory = productCategory;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.cop = cop;
		this.mrp = mrp;
		this.quantity = quantity;
		this.uploadedQuantity = uploadedQuantity;
		this.newExpiryDate = newExpiryDate;
		this.newMrp = newMrp;
		this.approvalQuantity = approvalQuantity;
		this.rejectQunatity = rejectQunatity;		
		this.rejectReason = rejectReason;
		this.Status = Status;
	} 


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

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getUomType() {
		return uomType;
	}

	public void setUomType(String uomType) {
		this.uomType = uomType;
	}

	public String getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
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

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Integer getUploadedQuantity() {
		return uploadedQuantity;
	}

	public void setUploadedQuantity(Integer uploadedQuantity) {
		this.uploadedQuantity = uploadedQuantity;
	}

	public Date getNewExpiryDate() {
		return newExpiryDate;
	}

	public void setNewExpiryDate(Date newExpiryDate) {
		this.newExpiryDate = newExpiryDate;
	}

	public Double getNewMrp() {
		return newMrp;
	}

	public void setNewMrp(Double newMrp) {
		this.newMrp = newMrp;
	}

	public Integer getApprovalQuantity() {
		return approvalQuantity;
	}

	public void setApprovalQuantity(Integer approvalQuantity) {
		this.approvalQuantity = approvalQuantity;
	}

	public Integer getRejectQunatity() {
		return rejectQunatity;
	}

	public void setRejectQunatity(Integer rejectQunatity) {
		this.rejectQunatity = rejectQunatity;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}	
	
	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}
}

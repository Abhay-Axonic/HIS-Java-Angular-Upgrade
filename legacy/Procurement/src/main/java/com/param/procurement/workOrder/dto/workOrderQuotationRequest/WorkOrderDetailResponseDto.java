package com.param.procurement.workOrder.dto.workOrderQuotationRequest;

public class WorkOrderDetailResponseDto {

	private Integer id;

	private Integer itemId;

	private String itemCode;

	private String itemName;

	private Integer quantity;
	
	private Integer currentVendorId;
	
	private String currentVendor;
	
	private Long batchId;
	
	private String batchNo;
	

	public WorkOrderDetailResponseDto() {
		super();
	}

	public WorkOrderDetailResponseDto(Integer id, Integer itemId, String itemCode, String itemName, Integer quantity,
			Integer currentVendorId,String currentVendor,Long batchId,String batchNo) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.quantity = quantity;
		this.currentVendorId = currentVendorId;
		this.currentVendor = currentVendor;
		this.batchId = batchId;
		this.batchNo = batchNo;
		
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getCurrentVendorId() {
		return currentVendorId;
	}

	public void setCurrentVendorId(Integer currentVendorId) {
		this.currentVendorId = currentVendorId;
	}

	public String getCurrentVendor() {
		return currentVendor;
	}

	public void setCurrentVendor(String currentVendor) {
		this.currentVendor = currentVendor;
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
	
}

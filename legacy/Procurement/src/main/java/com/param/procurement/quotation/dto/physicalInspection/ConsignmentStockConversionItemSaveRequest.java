package com.param.procurement.quotation.dto.physicalInspection;

public class ConsignmentStockConversionItemSaveRequest {
	
	private Integer id;

	private Integer itemId;

	private String itemCode;

	private String itemName;

	private String batchNo;

	private Integer grnUomTypeId;

	private String grnUomType;

	private String lotNo;

	private Integer statusId;

	private String status;

	/* added by Minesh */
	private Integer approvedQuantity;
	private Integer totalConsignmentStock;
	private Integer availableConsignmentStock;
	private Integer convertedConsignmentStock;
	private Integer conversionQuantity;
	private Integer availableConvertedStock;
	private Integer receivedQuantity;
	private Long batchId;
	
	public Integer getReceivedQuantity() {
		return receivedQuantity;
	}

	public void setReceivedQuantity(Integer receivedQuantity) {
		this.receivedQuantity = receivedQuantity;
	}
	
	public Integer getTotalConsignmentStock() {
		return totalConsignmentStock;
	}

	public void setTotalConsignmentStock(Integer totalConsignmentStock) {
		this.totalConsignmentStock = totalConsignmentStock;
	}

	public Integer getAvailableConsignmentStock() {
		return availableConsignmentStock;
	}

	public void setAvailableConsignmentStock(Integer availableConsignmentStock) {
		this.availableConsignmentStock = availableConsignmentStock;
	}

	public Integer getConvertedConsignmentStock() {
		return convertedConsignmentStock;
	}

	public void setConvertedConsignmentStock(Integer convertedConsignmentStock) {
		this.convertedConsignmentStock = convertedConsignmentStock;
	}

	public Integer getConversionQuantity() {
		return conversionQuantity;
	}

	public void setConversionQuantity(Integer conversionQuantity) {
		this.conversionQuantity = conversionQuantity;
	}

	public Integer getAvailableConvertedStock() {
		return availableConvertedStock;
	}

	public void setAvailableConvertedStock(Integer availableConvertedStock) {
		this.availableConvertedStock = availableConvertedStock;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	/* end added by Minesh */

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

	

	public Integer getGrnUomTypeId() {
		return grnUomTypeId;
	}

	public void setGrnUomTypeId(Integer grnUomTypeId) {
		this.grnUomTypeId = grnUomTypeId;
	}

	public String getGrnUomType() {
		return grnUomType;
	}

	public void setGrnUomType(String grnUomType) {
		this.grnUomType = grnUomType;
	}

	

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	
	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ConsignmentStockConversionItemSaveRequest() {
		super();
	}

	public ConsignmentStockConversionItemSaveRequest(Integer id) {
		super();
		this.id = id;
	}

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}
}
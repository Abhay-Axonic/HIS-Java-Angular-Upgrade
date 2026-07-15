package com.param.global.dto.items;

public class BatchAccessoriesMapperDto {

	private Integer id;
	
	private String itemCode;
	
	private String itemName;
	
	private Long batchId;
	
	private String batchNo;
	
	private Integer manufactureYear;
	
	private Integer quantity;

	public BatchAccessoriesMapperDto() {
		super();
	}

	public BatchAccessoriesMapperDto(Integer id, String itemCode, String itemName, Long batchId, String batchNo,
			Integer manufactureYear, Integer quantity) {
		super();
		this.id = id;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.manufactureYear = manufactureYear;
		this.quantity = quantity;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getManufactureYear() {
		return manufactureYear;
	}

	public void setManufactureYear(Integer manufactureYear) {
		this.manufactureYear = manufactureYear;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
}

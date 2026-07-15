package com.param.assetManagement.nonReturnableGatepass.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.BatchAccessoriesMapperDto;
@JsonInclude(Include.NON_NULL)
public class NonReturnableGatepssResponseItem {

	protected Integer id;
	
	protected Integer itemId;
	
	protected String itemName;
	
	protected String itemCode;
	
	protected String serialNo;
	
	protected Integer quantity;
	
	protected Double amount;
	
	protected String remark;
	
	protected Integer auctionQuantity;
	
	protected Integer pendingAuctionQuantity;
	
	protected Double auctionAmount;
	
	protected Long batchId;
	
	protected Integer statusId;
	
	protected String status;
	
	protected String batchNo;
	
	protected Integer auctionRequisitionDetailId;
	
	protected List<NonReturnableGatepssResponseItem> accessorriesList;
	
	protected List<BatchAccessoriesMapperDto> accessories;

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

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<NonReturnableGatepssResponseItem> getAccessorriesList() {
		return accessorriesList;
	}

	public void setAccessorriesList(List<NonReturnableGatepssResponseItem> accessorriesList) {
		this.accessorriesList = accessorriesList;
	}

	public List<BatchAccessoriesMapperDto> getAccessories() {
		return accessories;
	}

	public void setAccessories(List<BatchAccessoriesMapperDto> accessories) {
		this.accessories = accessories;
	}

	public Integer getAuctionQuantity() {
		return auctionQuantity;
	}

	public void setAuctionQuantity(Integer auctionQuantity) {
		this.auctionQuantity = auctionQuantity;
	}

	public Integer getPendingAuctionQuantity() {
		return pendingAuctionQuantity;
	}

	public void setPendingAuctionQuantity(Integer pendingAuctionQuantity) {
		this.pendingAuctionQuantity = pendingAuctionQuantity;
	}

	public Double getAuctionAmount() {
		return auctionAmount;
	}

	public void setAuctionAmount(Double auctionAmount) {
		this.auctionAmount = auctionAmount;
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

	public Integer getAuctionRequisitionDetailId() {
		return auctionRequisitionDetailId;
	}

	public void setAuctionRequisitionDetailId(Integer auctionRequisitionDetailId) {
		this.auctionRequisitionDetailId = auctionRequisitionDetailId;
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

	public NonReturnableGatepssResponseItem() {
		super();
	}

	public NonReturnableGatepssResponseItem(Integer id, Integer itemId, String itemName, String itemCode,
			Integer auctionQuantity, Integer pendingAuctionQuantity, Double auctionAmount, Long batchId, String batchNo,
			Integer auctionRequisitionDetailId) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.auctionQuantity = auctionQuantity;
		this.pendingAuctionQuantity = pendingAuctionQuantity;
		this.auctionAmount = auctionAmount;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.auctionRequisitionDetailId = auctionRequisitionDetailId;
	}

	public NonReturnableGatepssResponseItem(Integer id, Integer itemId, String itemName, String itemCode,
			String serialNo, Integer quantity) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.serialNo = serialNo;
		this.quantity = quantity;
	}

	public NonReturnableGatepssResponseItem(Integer id, Integer itemId, String itemName, String itemCode,
			String serialNo, Integer quantity, Long batchId) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.itemCode = itemCode;
		this.serialNo = serialNo;
		this.quantity = quantity;
		this.batchId = batchId;
	}
}
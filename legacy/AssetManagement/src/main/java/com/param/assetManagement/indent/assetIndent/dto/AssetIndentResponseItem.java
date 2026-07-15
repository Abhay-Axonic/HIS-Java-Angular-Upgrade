package com.param.assetManagement.indent.assetIndent.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.AccessoriesDetails;
@JsonInclude(Include.NON_NULL)
public class AssetIndentResponseItem {

	protected Integer id;

	protected Integer itemId;
	
	protected Integer quantity;
	
	protected Integer approvedQuantity;
	
	protected Integer rejectedQuantity;
	
	protected Integer rejectReasonId;
	
	protected String rejectNote;
	
	protected Integer statusId;
	
	protected String status;
	
	protected String itemCode;
	
	protected String itemName;
	
	protected Long stockAvailable;
	
    protected Long stockInTransit;
	
    protected String specification;
    
    protected Long maxOrderQuantity;
    
    protected List<AccessoriesDetails> accessories;

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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
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

	public Long getStockAvailable() {
		return stockAvailable;
	}

	public void setStockAvailable(Long stockAvailable) {
		this.stockAvailable = stockAvailable;
	}

	public Long getStockInTransit() {
		return stockInTransit;
	}

	public void setStockInTransit(Long stockInTransit) {
		this.stockInTransit = stockInTransit;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public List<AccessoriesDetails> getAccessories() {
		return accessories;
	}

	public void setAccessories(List<AccessoriesDetails> accessories) {
		this.accessories = accessories;
	}

	

	public Integer getRejectedQuantity() {
		return rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
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

	public AssetIndentResponseItem() {
		super();
	}

	public Long getMaxOrderQuantity() {
		return maxOrderQuantity;
	}

	public void setMaxOrderQuantity(Long maxOrderQuantity) {
		this.maxOrderQuantity = maxOrderQuantity;
	}

	public AssetIndentResponseItem(Integer id, Integer itemId, Integer quantity, Integer approvedQuantity, Integer statusId, String status, String itemCode,
			String itemName, Long stockAvailable, Long stockInTransit, String specification) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.quantity = quantity;
		this.approvedQuantity = approvedQuantity;
		this.statusId = statusId;
		this.status = status;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.stockAvailable = stockAvailable;
		this.stockInTransit = stockInTransit;
		this.specification = specification;
	}
	
	public AssetIndentResponseItem(Integer id, Integer itemId, Integer quantity, Integer approvedQuantity, Integer statusId, String status, String itemCode,
			String itemName, Long stockAvailable, Long stockInTransit, String specification, Long maxOrderQuantity) {
	this(id, itemId, quantity, approvedQuantity, statusId, status, itemCode, itemName, stockAvailable, stockInTransit, specification);
	this.maxOrderQuantity = maxOrderQuantity;
	}
	
}
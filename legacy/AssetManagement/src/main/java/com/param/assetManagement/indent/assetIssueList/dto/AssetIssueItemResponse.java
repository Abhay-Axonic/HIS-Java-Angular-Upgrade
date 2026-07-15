package com.param.assetManagement.indent.assetIssueList.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.AsseccoriesResponse;

@JsonInclude(Include.NON_NULL)
public class AssetIssueItemResponse {

	protected Integer id;

	protected Integer itemId;

	protected String itemCode;

	protected String itemName;

	protected String genericName;

	protected Long stockAvailable;
	protected Long availableQuantity;

	public Long getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(Long availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	protected Long batchId;

	protected String batchNo;

	protected Date expiryDate;

	protected Integer indentQuantity;

	protected Integer pendingQuantity;

	protected Integer issueQuantity;

	protected Long maxOrderQuantity;

	protected Integer returnQuantity;

	protected String returnReason;

	protected Integer batchMapperId;

	protected String specification;

	protected List<AsseccoriesResponse> asseccories;
	protected Integer acceptedQuantity;
	protected Integer rejectQuantity;
	protected Integer approvedQuantity;
	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public Integer getAcceptedQuantity() {
		return acceptedQuantity;
	}

	public void setAcceptedQuantity(Integer acceptedQuantity) {
		this.acceptedQuantity = acceptedQuantity;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public AssetIssueItemResponse() {
		super();
	}

	public AssetIssueItemResponse(Integer id, Integer itemId, String itemCode, String itemName, String genericName,
			Long batchId, String batchNo, Date expiryDate, Integer indentQuantity, Integer pendingQuantity,
			Integer issueQuantity, Long maxOrderQuantity) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.indentQuantity = indentQuantity;
		this.pendingQuantity = pendingQuantity;
		this.issueQuantity = issueQuantity;
		this.maxOrderQuantity = maxOrderQuantity;
	}

	public AssetIssueItemResponse(Integer id, Integer itemId, String itemCode, String itemName, String genericName,
			Long batchId, String batchNo, Date expiryDate, Integer indentQuantity, Integer pendingQuantity,
			Integer issueQuantity, Long maxOrderQuantity, List<AsseccoriesResponse> asseccories) {
		this(id, itemId, itemCode, itemName, genericName, batchId, batchNo, expiryDate, indentQuantity, pendingQuantity,
				issueQuantity, maxOrderQuantity);
		this.asseccories = asseccories;
	}

	public AssetIssueItemResponse(Integer id, Integer itemId, String itemCode, String itemName, String genericName,
			Long batchId, String batchNo, Date expiryDate, Integer indentQuantity, Integer pendingQuantity,
			Integer issueQuantity, Long maxOrderQuantity, Integer batchMapperId, String specification,
			List<AsseccoriesResponse> asseccories) {
		this(id, itemId, itemCode, itemName, genericName, batchId, batchNo, expiryDate, indentQuantity, pendingQuantity,
				issueQuantity, maxOrderQuantity);
		this.specification = specification;
		this.batchMapperId = batchMapperId;
		this.asseccories = asseccories;
	}
	
	public AssetIssueItemResponse(Integer id, Integer itemId, String itemCode, String itemName, String genericName,
			 Integer indentQuantity, Integer pendingQuantity,Integer issueQuantity, Long maxOrderQuantity, Integer batchMapperId, String specification) {
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.indentQuantity = indentQuantity;
		this.pendingQuantity = pendingQuantity;
		this.issueQuantity = issueQuantity;
		this.maxOrderQuantity = maxOrderQuantity;
		this.specification = specification;
		this.batchMapperId = batchMapperId;
		
	}

	public AssetIssueItemResponse(Integer id, Integer itemId, String itemCode, String itemName, String genericName,
			Long batchId, String batchNo, Date expiryDate, Integer returnQuantity, String returnReason) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.returnQuantity = returnQuantity;
		this.returnReason = returnReason;
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

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public Long getStockAvailable() {
		return stockAvailable;
	}

	public void setStockAvailable(Long stockAvailable) {
		this.stockAvailable = stockAvailable;
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

	public Integer getIndentQuantity() {
		return indentQuantity;
	}

	public void setIndentQuantity(Integer indentQuantity) {
		this.indentQuantity = indentQuantity;
	}

	public Integer getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	public Integer getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
	}

	public Long getMaxOrderQuantity() {
		return maxOrderQuantity;
	}

	public void setMaxOrderQuantity(Long maxOrderQuantity) {
		this.maxOrderQuantity = maxOrderQuantity;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public List<AsseccoriesResponse> getAsseccories() {
		return asseccories;
	}

	public void setAsseccories(List<AsseccoriesResponse> asseccories) {
		this.asseccories = asseccories;
	}

	public Integer getBatchMapperId() {
		return batchMapperId;
	}

	public void setBatchMapperId(Integer batchMapperId) {
		this.batchMapperId = batchMapperId;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

}

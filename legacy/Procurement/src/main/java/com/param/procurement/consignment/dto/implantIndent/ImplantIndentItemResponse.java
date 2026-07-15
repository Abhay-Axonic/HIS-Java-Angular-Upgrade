package com.param.procurement.consignment.dto.implantIndent;

import java.util.Date;
import java.util.List;

public class ImplantIndentItemResponse extends ImplantIndentRequestItems {

	protected Integer id;

	protected Integer itemId;

	protected String itemCode;

	protected String itemName;

	protected String genericName;

	protected String uomType;

	protected String uomUnit;

	protected Integer pendingQuantity;

	protected Integer uomTypeId;

	protected Integer uomUnitId;

	protected Long stockAvailable;

	protected Long batchId;

	protected String batchNo;

	protected Date expiryDate;

	protected Integer indentQuantity;

	protected Integer issueQuantity;

	protected Long maxOrderQuantity;

	protected Integer batchMapperId;

	protected Integer acceptQuantity;

	protected Integer rejectQuantity;

	protected String rejectNote;

	protected String rejectReason;
	
	protected Integer assetCategoryId;

	protected List<com.param.global.dto.items.UomResponse> uom;
	
	

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
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

	public Integer getBatchMapperId() {
		return batchMapperId;
	}

	public void setBatchMapperId(Integer batchMapperId) {
		this.batchMapperId = batchMapperId;
	}

	public Integer getAcceptQuantity() {
		return acceptQuantity;
	}

	public void setAcceptQuantity(Integer acceptQuantity) {
		this.acceptQuantity = acceptQuantity;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public List<com.param.global.dto.items.UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<com.param.global.dto.items.UomResponse> uom) {
		this.uom = uom;
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

	public Integer getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	public ImplantIndentItemResponse() {
		super();
	}

	public ImplantIndentItemResponse(Integer itemId, Integer uomTypeId, Integer uomUnitId, Integer indentQuantity,
			Integer statusId, Integer id, String itemCode, String itemName, String genericName, String uomType,
			String uomUnit, Integer pendingQuantity, List<com.param.global.dto.items.UomResponse> populateUom) {
		
		this.id = id;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.uom = populateUom;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.pendingQuantity = pendingQuantity;
		this.itemId = itemId;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.indentQuantity = indentQuantity;
		this.statusId = statusId;
	}
	
	public ImplantIndentItemResponse(Integer itemId, Integer uomTypeId, Integer uomUnitId, Integer indentQuantity,
			Integer statusId, Integer id, String itemCode, String itemName, String genericName, String uomType,
			String uomUnit, Integer pendingQuantity, Integer assetCategoryId,List<com.param.global.dto.items.UomResponse> populateUom) {
		
		this.id = id;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.uom = populateUom;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.pendingQuantity = pendingQuantity;
		this.itemId = itemId;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.indentQuantity = indentQuantity;
		this.statusId = statusId;
		this.assetCategoryId=assetCategoryId;
	}
	
	public ImplantIndentItemResponse(Integer itemId, Integer uomTypeId, Integer uomUnitId, Integer indentQuantity,
			Integer statusId, Integer id, String itemCode, String itemName, String genericName, String uomType,
			String uomUnit, Integer pendingQuantity, Integer assetCategoryId,List<com.param.global.dto.items.UomResponse> populateUom,Long stockAvailable) {
		
		this.id = id;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.uom = populateUom;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.pendingQuantity = pendingQuantity;
		this.itemId = itemId;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.indentQuantity = indentQuantity;
		this.statusId = statusId;
		this.assetCategoryId=assetCategoryId;
		this.stockAvailable=stockAvailable;
	}

	public ImplantIndentItemResponse(Integer id, Integer itemId, String itemCode, String itemName, String genericName,
			String uomType, String uomUnit, Integer pendingQuantity, Integer uomTypeId, Integer uomUnitId,
			Long stockAvailable, Long batchId, String batchNo, Date expiryDate, Integer indentQuantity,
			Integer issueQuantity, Long maxOrderQuantity, Integer batchMapperId, Integer acceptQuantity,
			Integer rejectQuantity) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.pendingQuantity = pendingQuantity;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.stockAvailable = stockAvailable;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.indentQuantity = indentQuantity;
		this.issueQuantity = issueQuantity;
		this.maxOrderQuantity = maxOrderQuantity;
		this.batchMapperId = batchMapperId;
		this.acceptQuantity = acceptQuantity;
		this.rejectQuantity = rejectQuantity;
	}

}

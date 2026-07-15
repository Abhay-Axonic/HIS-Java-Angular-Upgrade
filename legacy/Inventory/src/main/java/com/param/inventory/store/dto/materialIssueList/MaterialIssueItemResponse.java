package com.param.inventory.store.dto.materialIssueList;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.UomResponse;
import com.param.inventory.store.dto.materialPicker.SubstituteMaterialIssueDetailDto;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.SubstitutePatientConsumableDto;

@JsonInclude(Include.NON_NULL)
public class MaterialIssueItemResponse {

	protected Integer id;

	protected Integer itemId;

	protected String itemCode;

	protected String itemName;

	protected String genericName;

	protected Integer uomTypeId;

	protected String uomType;

	protected Integer uomUnitId;

	protected String uomUnit;

	protected Long stockAvailable;

	protected Long batchId;

	protected String batchNo;

	protected Date expiryDate;

	protected Integer indentQuantity;

	protected Integer pendingQuantity;

	protected Integer issueQuantity;

	protected Long maxOrderQuantity;

	protected Integer returnQuantity; /* For reject material */

	protected String returnReason; /* For reject material */

	protected Integer batchMapperId;

	protected Integer returnDetailId;

	protected Integer acceptQuantity;

	protected Integer rejectQuantity;

	protected Integer rejectedRejectQuantity;
	
	protected Integer status;

	protected String rejectNote;

	protected String rejectReason;
	
	protected List<UomResponse> uom;

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}
	
    private boolean isThreeUOMApplicable;
    
    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }
	
	protected Boolean isConsignment;
	protected List<SubstituteMaterialIssueDetailDto> selectedItems;
	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public MaterialIssueItemResponse(Integer id, Integer itemId, String itemCode, String itemName, String genericName,
			Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit, Integer indentQuantity,
			Integer pendingQuantity, Integer issueQuantity, Long stockAvailable, Long batchId, String batchNo,
			Date expiryDate, Long maxOrderQuantity, boolean isThreeUOMApplicable, List<UomResponse> uom) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.indentQuantity = indentQuantity;
		this.pendingQuantity = pendingQuantity;
		this.issueQuantity = issueQuantity;
		this.stockAvailable = stockAvailable;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.maxOrderQuantity = maxOrderQuantity;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
		this.uom = uom;
	}

	public MaterialIssueItemResponse(Integer id, Integer itemId, String itemCode, String itemName, String genericName,
			Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit, Integer indentQuantity,
			Integer pendingQuantity, Integer issueQuantity, Long stockAvailable, Long batchId, String batchNo,
			Date expiryDate, Long maxOrderQuantity, Integer batchMapperId) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.indentQuantity = indentQuantity;
		this.pendingQuantity = pendingQuantity;
		this.issueQuantity = issueQuantity;
		this.stockAvailable = stockAvailable;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.maxOrderQuantity = maxOrderQuantity;
		this.batchMapperId = batchMapperId;
	}

	public MaterialIssueItemResponse(Integer id, Integer itemId, String itemCode, String itemName, String genericName,
			Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit, Integer indentQuantity,
			Integer pendingQuantity, Integer issueQuantity, Long stockAvailable, Long batchId, String batchNo,
			Date expiryDate, Long maxOrderQuantity, Integer batchMapperId, Integer acceptQuantity,
			Integer rejectQuantity) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.indentQuantity = indentQuantity;
		this.pendingQuantity = pendingQuantity;
		this.issueQuantity = issueQuantity;
		this.stockAvailable = stockAvailable;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.maxOrderQuantity = maxOrderQuantity;
		this.batchMapperId = batchMapperId;
		this.acceptQuantity = acceptQuantity;
		this.rejectQuantity = rejectQuantity;
	}
	
	public MaterialIssueItemResponse(Integer id, Integer itemId, String itemCode, String itemName, String genericName,
			Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit, Integer indentQuantity,
			Integer pendingQuantity, Integer issueQuantity, Long stockAvailable, Long batchId, String batchNo,
			Date expiryDate, Long maxOrderQuantity, Integer batchMapperId, Integer acceptQuantity,
			Integer rejectQuantity,Integer status, boolean isThreeUOMApplicable, List<UomResponse> uom) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.indentQuantity = indentQuantity;
		this.pendingQuantity = pendingQuantity;
		this.issueQuantity = issueQuantity;
		this.stockAvailable = stockAvailable;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.maxOrderQuantity = maxOrderQuantity;
		this.batchMapperId = batchMapperId;
		this.acceptQuantity = acceptQuantity;
		this.rejectQuantity = rejectQuantity;
		this.status = status;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
		this.uom=uom;
	}

	/*
	 * public MaterialIssueItemResponse(Integer id, String itemCode, String
	 * itemName, String genericName, Integer uomTypeId, String uomType, Integer
	 * uomUnitId, String uomUnit, Long batchId, String batchNo, Date expiryDate,
	 * Integer returnDetailId, Integer acceptQuantity, Integer rejectQuantity) {
	 * super(); this.id = id; this.itemCode = itemCode; this.itemName = itemName;
	 * this.genericName = genericName; this.uomTypeId = uomTypeId; this.uomType =
	 * uomType; this.uomUnitId = uomUnitId; this.uomUnit = uomUnit; this.batchId =
	 * batchId; this.batchNo = batchNo; this.expiryDate = expiryDate;
	 * this.returnDetailId = returnDetailId; this.acceptQuantity = acceptQuantity;
	 * this.rejectQuantity = rejectQuantity; }
	 */

	public MaterialIssueItemResponse(Integer id, String itemCode, String itemName, String genericName,
			Integer batchMapperId, Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit, Long batchId,
			String batchNo, Date expiryDate, Integer returnQuantity, String returnReason) {
		super();
		this.id = id;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.batchMapperId = batchMapperId;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.returnQuantity = returnQuantity;
		this.returnReason = returnReason;
	}

	public MaterialIssueItemResponse(Integer id, String itemCode, String itemName, String genericName,
			Integer batchMapperId, Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit, Long batchId,
			String batchNo, Date expiryDate, Integer acceptQuantity, Integer rejectQuantity, String rejectNote,
			Integer rejectedRejectQuantity, String rejectReason) {
		super();
		this.id = id;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.batchMapperId = batchMapperId;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.acceptQuantity = acceptQuantity;
		this.rejectQuantity = rejectQuantity;
		this.rejectNote = rejectNote;
		this.rejectedRejectQuantity = rejectedRejectQuantity;
		this.rejectReason = rejectReason;
	}
	
	public MaterialIssueItemResponse(Integer id, String itemCode, String itemName, String genericName,
			Integer batchMapperId, Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit, Long batchId,
			String batchNo, Date expiryDate, Integer acceptQuantity, Integer rejectQuantity, String rejectNote,
			Integer rejectedRejectQuantity, String rejectReason,Integer itemId, boolean isThreeUOMApplicable, List<UomResponse> uom) {
		super();
		this.id = id;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.batchMapperId = batchMapperId;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.acceptQuantity = acceptQuantity;
		this.rejectQuantity = rejectQuantity;
		this.rejectNote = rejectNote;
		this.rejectedRejectQuantity = rejectedRejectQuantity;
		this.rejectReason = rejectReason;
		this.itemId = itemId;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
		this.uom = uom;
	}

	/*
	 * public MaterialIssueItemResponse(Integer id, String itemCode, String
	 * itemName, String genericName, Integer uomTypeId, String uomType, Integer
	 * uomUnitId, String uomUnit, Long batchId, String batchNo, Date expiryDate,
	 * Integer returnDetailId, Integer acceptQuantity, Integer rejectQuantity,
	 * Integer returnQuantity,String rejectNote) { super(); this.id = id;
	 * this.itemCode = itemCode; this.itemName = itemName; this.genericName =
	 * genericName; this.uomTypeId = uomTypeId; this.uomType = uomType;
	 * this.uomUnitId = uomUnitId; this.uomUnit = uomUnit; this.batchId = batchId;
	 * this.batchNo = batchNo; this.expiryDate = expiryDate; this.returnDetailId =
	 * returnDetailId; this.acceptQuantity = acceptQuantity; this.rejectQuantity =
	 * rejectQuantity; this.returnQuantity = returnQuantity; this.rejectNote =
	 * rejectNote; }
	 */
	public MaterialIssueItemResponse(Integer id, String itemCode, String itemName, String genericName,
			Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit, Long batchId, String batchNo,
			Date expiryDate, Integer returnDetailId, Integer acceptQuantity, Integer rejectQuantity, String rejectNote,
			Integer rejectedRejectQuantity, String rejectReason) {
		super();
		this.id = id;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.returnDetailId = returnDetailId;
		this.acceptQuantity = acceptQuantity;
		this.rejectQuantity = rejectQuantity;
		this.rejectNote = rejectNote;
		this.rejectedRejectQuantity = rejectedRejectQuantity;
		this.rejectReason = rejectReason;
	}
	
	public MaterialIssueItemResponse(Integer id, String itemCode, String itemName, String genericName,
			Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit, Long batchId, String batchNo,
			Date expiryDate, Integer returnDetailId, Integer acceptQuantity, Integer rejectQuantity, String rejectNote,
			Integer rejectedRejectQuantity, String rejectReason,Integer itemId, boolean isThreeUOMApplicable, List<UomResponse> uom) {
		super();
		this.id = id;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericName = genericName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.returnDetailId = returnDetailId;
		this.acceptQuantity = acceptQuantity;
		this.rejectQuantity = rejectQuantity;
		this.rejectNote = rejectNote;
		this.rejectedRejectQuantity = rejectedRejectQuantity;
		this.rejectReason = rejectReason;
		this.itemId = itemId;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
		this.uom = uom;
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

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public String getUomType() {
		return uomType;
	}

	public void setUomType(String uomType) {
		this.uomType = uomType;
	}

	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}

	public String getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
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

	public Long getMaxOrderQuantity() {
		return maxOrderQuantity;
	}

	public void setMaxOrderQuantity(Long maxOrderQuantity) {
		this.maxOrderQuantity = maxOrderQuantity;
	}

	public Integer getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
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

	public Integer getBatchMapperId() {
		return batchMapperId;
	}

	public void setBatchMapperId(Integer batchMapperId) {
		this.batchMapperId = batchMapperId;
	}

	public Integer getReturnDetailId() {
		return returnDetailId;
	}

	public void setReturnDetailId(Integer returnDetailId) {
		this.returnDetailId = returnDetailId;
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

	public Integer getRejectedRejectQuantity() {
		return rejectedRejectQuantity;
	}

	public void setRejectedRejectQuantity(Integer rejectedRejectQuantity) {
		this.rejectedRejectQuantity = rejectedRejectQuantity;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	

}

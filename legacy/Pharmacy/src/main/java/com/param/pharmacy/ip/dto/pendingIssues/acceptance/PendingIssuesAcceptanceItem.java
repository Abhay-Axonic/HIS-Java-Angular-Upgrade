package com.param.pharmacy.ip.dto.pendingIssues.acceptance;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.UomResponse;

@JsonInclude(Include.NON_NULL)
public class PendingIssuesAcceptanceItem
{
	private Integer id;
	
	private String itemCode;
	
	private String itemName;
	
	private Integer itemId;
	
	private String genericName;
	
	private Integer genericId;
	
	private String manufacturerName;
	
	private Integer manufacturerId;
	
	private boolean issueAcceptanceflag;
	
	private String uom;
	
	private String uomUnit;
	
	private Integer uomUnitId;
	
	private String batchNo;
	
	private Date expiryDate;
	
	private Double rate;
	
	private Integer quantityIssued;
	
	private Integer acceptQuantity;
	
	private Integer rejectQuantity;
	
	private Integer qty;
	
	private Integer batchMapperId;
	
	private Integer rejectReasonId;
	
	private String rejectNote;

	private Integer statusId;
	   
	private String status;
	private Integer uomTypeId;
	private List<UomResponse> uomResponse;
	private Integer priorityId;
	private Integer orderQty;

	public List<UomResponse> getUomResponse() {
		return uomResponse;
	}

	public void setUomResponse(List<UomResponse> uomResponse) {
		this.uomResponse = uomResponse;
	}

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
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

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public Integer getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public boolean isIssueAcceptanceflag() {
		return issueAcceptanceflag;
	}

	public void setIssueAcceptanceflag(boolean issueAcceptanceflag) {
		this.issueAcceptanceflag = issueAcceptanceflag;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
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

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Integer getQuantityIssued() {
		return quantityIssued;
	}

	public void setQuantityIssued(Integer quantityIssued) {
		this.quantityIssued = quantityIssued;
	}
	
	public Integer getBatchMapperId() {
		return batchMapperId;
	}

	public void setBatchMapperId(Integer batchMapperId) {
		this.batchMapperId = batchMapperId;
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

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public PendingIssuesAcceptanceItem() {
		super();
	}

	public PendingIssuesAcceptanceItem(Integer id, String itemCode, String itemName, Integer itemId, String genericName,
			Integer genericId, String manufacturerName, Integer manufacturerId, boolean issueAcceptanceflag, String uom,
			String uomUnit, Integer uomUnitId, String batchNo, Date expiryDate, Double rate, Integer quantityIssued,
			Integer batchMapperId, Integer statusId, String status, Integer acceptQuantity, Integer rejectQuantity,
			Integer rejectReasonId, String rejectNote, Integer qty) {
		super();
		this.id = id;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.itemId = itemId;
		this.genericName = genericName;
		this.genericId = genericId;
		this.manufacturerName = manufacturerName;
		this.manufacturerId = manufacturerId;
		this.issueAcceptanceflag = issueAcceptanceflag;
		this.uom = uom;
		this.uomUnit = uomUnit;
		this.uomUnitId = uomUnitId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.rate = rate;
		this.quantityIssued = quantityIssued;
		this.batchMapperId = batchMapperId;
		this.statusId = statusId;
		this.status = status;
		this.acceptQuantity = acceptQuantity;
		this.rejectQuantity = rejectQuantity;
		this.rejectReasonId = rejectReasonId;
		this.rejectNote = rejectNote;
		this.qty = qty;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Integer getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}
	
}
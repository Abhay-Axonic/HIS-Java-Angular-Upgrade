package com.param.inventory.store.dto.materialIssuePending;

import java.util.Date;
import java.util.List;

import com.param.global.dto.items.UomResponse;

public class IssueBatch {

	protected Integer id;

	protected Long batchId;

	protected Integer uomTypeId;

	protected Integer uomUnitId;

	protected Integer issueQuantity;	
	protected String batchNo;
	protected Date expiry;
	protected List<UomResponse> uom;
	protected Integer availableStockQuantity;
	protected Integer pickerQty;

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}

	public Integer getAvailableStockQuantity() {
		return availableStockQuantity;
	}

	public void setAvailableStockQuantity(Integer availableStockQuantity) {
		this.availableStockQuantity = availableStockQuantity;
	}

	public Integer getPickerQty() {
		return pickerQty;
	}

	public void setPickerQty(Integer pickerQty) {
		this.pickerQty = pickerQty;
	}

	public IssueBatch() {
		super();
	}

	public IssueBatch(Integer id, Long batchId, Integer uomTypeId, Integer uomUnitId, Integer issueQuantity) {
		super();
		this.id = id;
		this.batchId = batchId;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.issueQuantity = issueQuantity;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
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

	public Integer getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
	}

}

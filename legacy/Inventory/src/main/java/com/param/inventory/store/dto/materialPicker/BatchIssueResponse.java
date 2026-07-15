package com.param.inventory.store.dto.materialPicker;

import java.util.Date;
import java.util.List;

import com.param.global.dto.items.UomResponse;

public class BatchIssueResponse {

	protected Long batchId;

	protected String batchNo;

	protected Date expiry;

	private Integer availableStockQuantity;
	
	protected List<UomResponse> uom;


	public BatchIssueResponse(Long batchId, String batchNo, Date expiry, Integer availableStockQuantity,
			List<UomResponse> uom) {
		super();
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiry = expiry;
		this.availableStockQuantity = availableStockQuantity;
		this.uom = uom;
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

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public Integer getAvailableStockQuantity() {
		return availableStockQuantity;
	}

	public void setAvailableStockQuantity(Integer availableStockQuantity) {
		this.availableStockQuantity = availableStockQuantity;
	}

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}

}

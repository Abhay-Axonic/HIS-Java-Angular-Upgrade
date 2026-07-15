package com.param.global.otims.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class BatchDto {
	
	private Integer batchId;
	
	private String batchNo;
	
	private String batchDetails;
	
//	private String expiry;
	
	private Double sellingPrice;
	
	private Double markup;
	
	private Double rate;
	
	private List<UomDto> uom;
	
	private boolean isSelected;
	
	private Integer selectedBatch;
	
	private Date expiry;
	
    private Integer availableStockQuantity;
    
    private BigInteger longBatchId;

	private String startDate;
	private String stopDate;
    private Integer issueId;
    

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getBatchDetails() {
		return batchDetails;
	}

	public void setBatchDetails(String batchDetails) {
		this.batchDetails = batchDetails;
	}

	public Double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(Double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public List<UomDto> getUom() {
		return uom;
	}

	public void setUom(List<UomDto> uom) {
		this.uom = uom;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Integer getSelectedBatch() {
		return selectedBatch;
	}

	public void setSelectedBatch(Integer selectedBatch) {
		this.selectedBatch = selectedBatch;
	}

	public Double getMarkup() {
		return markup;
	}

	public void setMarkup(Double markup) {
		this.markup = markup;
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

	public BigInteger getLongBatchId() {
		return longBatchId;
	}

	public void setLongBatchId(BigInteger longBatchId) {
		this.longBatchId = longBatchId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStopDate() {
		return stopDate;
	}

	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}

	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	
	
}

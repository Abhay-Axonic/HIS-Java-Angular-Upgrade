package com.param.pharmacy.op.dto.salesWorkList;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.UomResponse;

public class BatchResponse1 {
	 private Long batchId;
	 
     private String batchNo;
     
     private Date expiry;
     
     private Double sellingPrice;
     
     private List<UomResponse> uom;
     
     @JsonInclude(Include.NON_NULL)
     private Integer stock;

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

	public Double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(Double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}

	public BatchResponse1(Long batchId, String batchNo, Date expiry, Double sellingPrice, List<UomResponse> uom) {
		super();
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiry = expiry;
		this.sellingPrice = sellingPrice;
		this.uom = uom;
	}

	public BatchResponse1(Long batchId, String batchNo, Date expiry, Double sellingPrice, List<UomResponse> uom,Integer stock) {
		super();
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiry = expiry;
		this.sellingPrice = sellingPrice;
		this.uom = uom;
		this.stock = stock;
	}
	
}
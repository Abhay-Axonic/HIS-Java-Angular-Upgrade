package com.param.procurement.quotation.dto.physicalInspection;

import java.math.BigInteger;
import java.util.Date;

import com.param.service.global.model.CookieObjectDto;

public class PhysicalInspectionRequest extends CookieObjectDto
{
	
	private Date fromDate;
	
	private Date toDate;
	
	private Integer storeId;
	
	private Integer assetType;
	
	private Integer statusId;
	
	private Integer id;
	private Integer rejectedQty;
	
	private BigInteger stockTransactionId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getAssetType() {
		return assetType;
	}

	public void setAssetType(Integer assetType) {
		this.assetType = assetType;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public BigInteger getStockTransactionId() {
		return stockTransactionId;
	}

	public void setStockTransactionId(BigInteger stockTransactionId) {
		this.stockTransactionId = stockTransactionId;
	}

	public Integer getRejectedQty() {
		return rejectedQty;
	}

	public void setRejectedQty(Integer rejectedQty) {
		this.rejectedQty = rejectedQty;
	}
	
}
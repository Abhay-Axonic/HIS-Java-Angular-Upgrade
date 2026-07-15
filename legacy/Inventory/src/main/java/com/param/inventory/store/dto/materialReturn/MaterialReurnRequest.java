package com.param.inventory.store.dto.materialReturn;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.param.inventory.store.dto.storeIndent.StoreIndentRequest;

public class MaterialReurnRequest extends StoreIndentRequest {

	protected Date fromDate;
	
	private BigInteger stockTransactionId;
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

	protected Date toDate;
	protected Integer assetTypeId;

	protected Integer carrierId;

	protected Boolean isConsignment;

	protected String returnNo;

	protected List<Integer> returnId;

	protected Integer mReturnId;

	protected Boolean nearByExpiry;

	/*
	 * protected Integer returnQuantity;
	 * 
	 * protected Integer returnReasonId;
	 * 
	 * protected Integer uomTypeId;
	 * 
	 * protected Integer uomUnitId;
	 */
	protected Long batchId;

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public Integer getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Integer carrierId) {
		this.carrierId = carrierId;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public String getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}

	public List<Integer> getReturnId() {
		return returnId;
	}

	public void setReturnId(List<Integer> returnId) {
		this.returnId = returnId;
	}

	public Boolean getNearByExpiry() {
		return nearByExpiry;
	}

	public void setNearByExpiry(Boolean nearByExpiry) {
		this.nearByExpiry = nearByExpiry;
	}

	public Integer getmReturnId() {
		return mReturnId;
	}

	public void setmReturnId(Integer mReturnId) {
		this.mReturnId = mReturnId;
	}

	public BigInteger getStockTransactionId() {
		return stockTransactionId;
	}

	public void setStockTransactionId(BigInteger stockTransactionId) {
		this.stockTransactionId = stockTransactionId;
	}

}

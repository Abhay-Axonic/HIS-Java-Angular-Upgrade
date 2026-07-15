package com.param.global.dto.global;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.param.service.global.model.CookieObjectDto;

public class AgainstGrnRequest extends CookieObjectDto{

	protected Date fromDate;

	protected Date toDate;

	protected Integer storeId;

	protected Integer vendorId;
	
	protected Integer assetTypeID;
	protected String grnNo;
	
	
	 public String getGrnNo() {
		return grnNo;
	}

	public void setGrnNo(String grnNo) {
		this.grnNo = grnNo;
	}

	@JsonCreator
	    public AgainstGrnRequest(@JsonProperty("fromDate") Date fromDate, @JsonProperty("toDate") Date toDate, @JsonProperty("storeId")Integer storeId,
	            @JsonProperty("assetTypeID") Integer assetTypeID) {
	        this.fromDate = fromDate;
	        this.toDate = toDate;
	        this.storeId = storeId;
	        this.assetTypeID = assetTypeID;
	    }

	public Integer getAssetTypeID() {
		return assetTypeID;
	}

	public void setAssetTypeID(Integer assetTypeID) {
		this.assetTypeID = assetTypeID;
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

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}
}

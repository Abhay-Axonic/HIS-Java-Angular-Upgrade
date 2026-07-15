package com.param.procurement.quotation.dto.consignmentPOInvoice;

import java.util.Date;

import com.param.service.global.model.CookieObjectDto;

public class ConsignmentStockPORequestDto extends CookieObjectDto{
	
	private Date fromDate;
	private Date toDate;
	private Integer storeId;
	private Integer assetTypeId;
	private Integer statusId;
	private Integer vendorId;
	private String vendorName;
	private String poNumber;
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
	public Integer getAssetTypeId() {
		return assetTypeId;
	}
	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}
	public Integer getStatusId() {
		return statusId;
	}
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	public Integer getVendorId() {
		return vendorId;
	}
	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	
	/*start code for use as get consignment po invoice list*/
	private Integer requestingStoreId;
	private Integer issuingStoreId;
	public Integer getRequestingStoreId() {
		return requestingStoreId;
	}
	public void setRequestingStoreId(Integer requestingStoreId) {
		this.requestingStoreId = requestingStoreId;
	}
	public Integer getIssuingStoreId() {
		return issuingStoreId;
	}
	public void setIssuingStoreId(Integer issuingStoreId) {
		this.issuingStoreId = issuingStoreId;
	}
	
	
	/*end code for use as get consignment po invoice list*/
	
}

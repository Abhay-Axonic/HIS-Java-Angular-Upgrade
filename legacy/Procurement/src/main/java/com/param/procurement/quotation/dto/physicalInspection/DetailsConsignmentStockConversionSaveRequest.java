package com.param.procurement.quotation.dto.physicalInspection;

import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class DetailsConsignmentStockConversionSaveRequest extends CookieObjectDto {

	private Integer id;

	private Integer grnTypeId;

	private String grnType;

	private Integer storeId;

	private String storeName;

	private Integer vendorId;

	private String vendor;

	private Integer assetTypeId;

	private String assetType;

	private Integer statusId;

	private String status;
	
	private Integer transactionDetailsId;
	
	private Integer transactionId;
	
	private List<ConsignmentStockConversionItemSaveRequest> items;
	private String remarks;
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGrnTypeId() {
		return grnTypeId;
	}

	public void setGrnTypeId(Integer grnTypeId) {
		this.grnTypeId = grnTypeId;
	}

	public String getGrnType() {
		return grnType;
	}

	public void setGrnType(String grnType) {
		this.grnType = grnType;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
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

	public List<ConsignmentStockConversionItemSaveRequest> getItems() {
		return items;
	}

	public void setItems(List<ConsignmentStockConversionItemSaveRequest> items) {
		this.items = items;
	}

	public Integer getTransactionDetailsId() {
		return transactionDetailsId;
	}

	public void setTransactionDetailsId(Integer transactionDetailsId) {
		this.transactionDetailsId = transactionDetailsId;
	}

	public Integer getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}
	

}
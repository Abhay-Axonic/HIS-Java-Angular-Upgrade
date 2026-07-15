package com.param.global.dto.items;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.GoodReceiptNoteDetailDto;
import com.param.global.dto.global.PickerResponse;
import com.param.global.dto.global.TaxDropDown;

@JsonInclude(Include.NON_NULL)
public class ItemsDetailsListResponse implements Serializable{

	protected Long stockAvailable;

	protected Long stockInTransit;

	protected Long lastMonthSale;

	protected Long currentMonthSale;

	protected Long maxOrderQuantity;

	protected Integer maxStoreQuantity;

	protected List<UomResponse> uom;

	protected List<BatchResponse> batch;
	
	List<TaxDropDown> taxList;
	
	protected Integer itemId;

	protected String itemCode;

	protected String itemName;
	
	protected Integer taxId;

	protected String taxName;

	protected Double taxPercent;

	protected Boolean isOtc;
	
	protected Integer genericId;

	protected String genericName;

	protected Integer manufacturerId;

	protected String manufacturerName;
	
	protected Boolean isBatchRequired;
	
	protected boolean isThreeUOMApplicable;
	
	protected Integer assetCategoryId;

	List<RackResponse> racks;

	protected String assetCategory;

	protected Integer productCategoryId;

	protected String productCategory;
	
	protected Integer issueQuantity;
	
	protected BigInteger batchId;
	
	protected String equipmentName;
	
	protected Boolean lotNo;
	protected List<PickerResponse> carriers;
	protected String colorCode;
	protected GoodReceiptNoteDetailDto goodReceiptNoteDetailDto;
	
	public List<PickerResponse> getCarriers() {
		return carriers;
	}

	public void setCarriers(List<PickerResponse> carriers) {
		this.carriers = carriers;
	}

	public Boolean getLotNo() {
		return lotNo;
	}

	public void setLotNo(Boolean lotNo) {
		this.lotNo = lotNo;
	}

	public Boolean getIsBatchRequired() {
		return this.isBatchRequired;
	}

	public void setIsBatchRequired(Boolean isBatchRequired) {
		this.isBatchRequired = isBatchRequired;
	}

	public boolean getIsThreeUOMApplicable() {
		return isThreeUOMApplicable;
	}

	public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}

	protected Integer currentVendorId;

	protected String currentVendorName;


	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
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

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public Double getTaxPercent() {
		return taxPercent;
	}

	public void setTaxPercent(Double taxPercent) {
		this.taxPercent = taxPercent;
	}

	public Boolean getIsOtc() {
		return isOtc;
	}

	public void setIsOtc(Boolean isOtc) {
		this.isOtc = isOtc;
	}

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public Integer getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public List<TaxDropDown> getTaxList() {
		return taxList;
	}

	public void setTaxList(List<TaxDropDown> taxList) {
		this.taxList = taxList;
	}

	public ItemsDetailsListResponse() {
		super();
	}

	public ItemsDetailsListResponse(Integer itemId, List<BatchResponse> batch) {
		this.itemId = itemId;
		this.batch = batch;
	}

	public ItemsDetailsListResponse(Integer itemId, String itemCode, String itemName, Integer genericId,
			String genericName, Integer manufacturerId, String manufacturerName, Integer taxId, String taxName,
			Double taxPercent, Boolean isOtc, Long stockAvailable, Long stockInTransit, Long lastMonthSale,
			Long currentMonthSale, Long maxOrderQuantity, List<UomResponse> uom) {
		
		this.stockAvailable = stockAvailable;
		this.stockInTransit = stockInTransit;
		this.lastMonthSale = lastMonthSale;
		this.currentMonthSale = currentMonthSale;
		this.maxOrderQuantity = maxOrderQuantity;
		this.uom = uom;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericId = genericId;
		this.genericName = genericName;
		this.manufacturerId = manufacturerId;
		this.manufacturerName = manufacturerName;
		this.taxId = taxId;
		this.taxName = taxName;
		this.taxPercent = taxPercent;
		this.isOtc = isOtc;
	}

	public Long getStockAvailable() {
		return stockAvailable;
	}

	public void setStockAvailable(Long stockAvailable) {
		this.stockAvailable = stockAvailable;
	}

	public Long getStockInTransit() {
		return stockInTransit;
	}

	public void setStockInTransit(Long stockInTransit) {
		this.stockInTransit = stockInTransit;
	}

	public Long getLastMonthSale() {
		return lastMonthSale;
	}

	public void setLastMonthSale(Long lastMonthSale) {
		this.lastMonthSale = lastMonthSale;
	}

	public Long getCurrentMonthSale() {
		return currentMonthSale;
	}

	public void setCurrentMonthSale(Long currentMonthSale) {
		this.currentMonthSale = currentMonthSale;
	}

	public Long getMaxOrderQuantity() {
		return maxOrderQuantity;
	}

	public void setMaxOrderQuantity(Long maxOrderQuantity) {
		this.maxOrderQuantity = maxOrderQuantity;
	}

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}

	public List<BatchResponse> getBatch() {
		return batch;
	}

	public void setBatch(List<BatchResponse> batch) {
		this.batch = batch;
	}

	public Integer getMaxStoreQuantity() {
		return maxStoreQuantity;
	}

	public void setMaxStoreQuantity(Integer maxStoreQuantity) {
		this.maxStoreQuantity = maxStoreQuantity;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public String getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(String assetCategory) {
		this.assetCategory = assetCategory;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public Integer getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
	}
	public void addBatch(BatchResponse batch) {
		if (this.batch == null) {
			this.batch = new ArrayList<BatchResponse>();
		}

		this.batch.add(batch);
	}

	public void removeBatch(BatchResponse batch) {
		this.batch.remove(batch);
	}

	public BigInteger getBatchId() {
		return batchId;
	}

	public void setBatchId(BigInteger batchId) {
		this.batchId = batchId;
	}

	public List<RackResponse> getRacks() {
		return racks;
	}

	public void setRacks(List<RackResponse> racks) {
		this.racks = racks;
	}

	public Integer getCurrentVendorId() {
		return currentVendorId;
	}

	public void setCurrentVendorId(Integer currentVendorId) {
		this.currentVendorId = currentVendorId;
	}

	public String getCurrentVendorName() {
		return currentVendorName;
	}

	public void setCurrentVendorName(String currentVendorName) {
		this.currentVendorName = currentVendorName;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public GoodReceiptNoteDetailDto getGoodReceiptNoteDetailDto() {
		return goodReceiptNoteDetailDto;
	}

	public void setGoodReceiptNoteDetailDto(GoodReceiptNoteDetailDto goodReceiptNoteDetailDto) {
		this.goodReceiptNoteDetailDto = goodReceiptNoteDetailDto;
	}

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}
	
	
}
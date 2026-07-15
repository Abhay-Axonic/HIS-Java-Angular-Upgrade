package com.param.global.dto.global;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.param.global.dto.GoodReceiptNoteDetailDto;
import com.param.global.dto.items.UomResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurchaseRequestDetailResponse {

	private Integer id;

	private Integer itemId;
	
	private Integer storeId;

	private String itemCode;
	private Integer pendingQuantity;

	private String itemName;

	private Integer uomTypeId;

	private String uomType;

	private Integer uomUnitId;

	private String uomUnit;

	private Integer prQuantity;
	
	private Integer genericId;

	private String genericName;

	private Integer manufacturerId;

	private String manufacturerName;

	private Integer taxId;

	private String taxName;

	private Double taxPercent;
	
	private List<UomResponse> uom;
	
	private Long maxStoreQuantity;
	
	private Long maxOrderQuantity;
	
	private Integer stockAvailable;
	
	private Integer leaseQuantity;
	
	private boolean isThreeUOMApplicable;
	
	public boolean getIsThreeUOMApplicable() {
		return isThreeUOMApplicable;
	}

	public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}
	
	private Integer pendingPOqty;
	private GoodReceiptNoteDetailDto goodReceiptNoteDetailDto;
	
	public GoodReceiptNoteDetailDto getGoodReceiptNoteDetailDto() {
		return goodReceiptNoteDetailDto;
	}

	public void setGoodReceiptNoteDetailDto(GoodReceiptNoteDetailDto goodReceiptNoteDetailDto) {
		this.goodReceiptNoteDetailDto = goodReceiptNoteDetailDto;
	}

	public PurchaseRequestDetailResponse() {
		super();
		
	}

	public PurchaseRequestDetailResponse(Integer id, Integer itemId, String itemCode, String itemName,
			Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit, Integer prQuantity) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.prQuantity = prQuantity;
	}

	public PurchaseRequestDetailResponse(Integer id, Integer itemId, String itemCode, String itemName,
			Integer genericId, String genericName, Integer manufacturerId, String manufacturerName, Integer taxId,
			String taxName, Double taxPercent, List<UomResponse> uom) {
		super();
		this.id = id;
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
		this.uom = uom;
	}
	
	public PurchaseRequestDetailResponse(Integer id, Integer itemId, String itemCode, String itemName,
			Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit, Integer genericId, String genericName,
			Integer manufacturerId, String manufacturerName, Integer taxId, String taxName, Double taxPercent) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.genericId = genericId;
		this.genericName = genericName;
		this.manufacturerId = manufacturerId;
		this.manufacturerName = manufacturerName;
		this.taxId = taxId;
		this.taxName = taxName;
		this.taxPercent = taxPercent;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public String getUomType() {
		return uomType;
	}

	public void setUomType(String uomType) {
		this.uomType = uomType;
	}

	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}

	public String getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public Integer getPrQuantity() {
		return prQuantity;
	}

	public void setPrQuantity(Integer prQuantity) {
		this.prQuantity = prQuantity;
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

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Long getMaxStoreQuantity() {
		return maxStoreQuantity;
	}

	public void setMaxStoreQuantity(Long maxStoreQuantity) {
		this.maxStoreQuantity = maxStoreQuantity;
	}

	public Long getMaxOrderQuantity() {
		return maxOrderQuantity;
	}

	public void setMaxOrderQuantity(Long maxOrderQuantity) {
		this.maxOrderQuantity = maxOrderQuantity;
	}

	public Integer getStockAvailable() {
		return stockAvailable;
	}

	public void setStockAvailable(Integer stockAvailable) {
		this.stockAvailable = stockAvailable;
	}

	public Integer getLeaseQuantity() {
		return leaseQuantity;
	}

	public void setLeaseQuantity(Integer leaseQuantity) {
		this.leaseQuantity = leaseQuantity;
	}

	public Integer getPendingPOqty() {
		return pendingPOqty;
	}

	public void setPendingPOqty(Integer pendingPOqty) {
		this.pendingPOqty = pendingPOqty;
	}

	public Integer getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}
	
}

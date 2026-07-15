package com.param.global.dto.global;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.param.global.dto.items.UomResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuotationRequestDetailResponse {
	
	private Integer id;

	private Integer itemId;
	
	private Integer storeId;

	private String itemCode;

	private String itemName;
	
	private Integer qrQuantity;
	private Integer vendorCop;
	
	public Integer getVendorCop() {
		return vendorCop;
	}

	public void setVendorCop(Integer vendorCop) {
		this.vendorCop = vendorCop;
	}

	private Integer uomTypeId;
	
	private String uomType;
	
	private Integer uomUnitId;
	
	private String uomUnit;
	
	private Integer pendingPOqty;

	private List<UomResponse> uom;
	
	private Long maxStoreQuantity;
	
	private Long maxOrderQuantity;
	
	private Integer genericId;
	
	private String genericName;

	private Integer manufacturerId;

	private String manufacturerName;
	
	private Integer taxId;

	private String taxName;

	private Double taxPercent;
	
	private Long stockAvailable;
	
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

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
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

	public Integer getPendingPOqty() {
		return pendingPOqty;
	}

	public void setPendingPOqty(Integer pendingPOqty) {
		this.pendingPOqty = pendingPOqty;
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

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
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

	
	
	public Integer getQrQuantity() {
		return qrQuantity;
	}

	public void setQrQuantity(Integer qrQuantity) {
		this.qrQuantity = qrQuantity;
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

	public QuotationRequestDetailResponse()
	{
		super();
	}

	public QuotationRequestDetailResponse(Integer id, Integer itemId, String itemCode, String itemName, Integer qrQquantity) {
		
		this.id=id;
		this.itemId=itemId;
		this.itemCode=itemCode;
		this.itemName=itemName;
		this.qrQuantity=qrQquantity;
	
	}
	
	public QuotationRequestDetailResponse(Integer id, Integer itemId, String itemCode, String itemName,
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
		this.qrQuantity = prQuantity;
	}

	public Long getStockAvailable() {
		return stockAvailable;
	}

	public void setStockAvailable(Long stockAvailable) {
		this.stockAvailable = stockAvailable;
	}


}

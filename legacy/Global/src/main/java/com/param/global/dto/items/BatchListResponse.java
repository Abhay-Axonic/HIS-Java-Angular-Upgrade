package com.param.global.dto.items;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class BatchListResponse extends ItemListResponse {
	
	protected Integer itemId;
	
	protected Long batchId;

	protected String batchNo;

	protected Date expiry;

	protected Integer uomTypeId;

	protected String uomType;

	protected Integer uomUnitId;

	protected String uomUnit;

	protected Long availableQuantity;

	protected Double mrp;
	
	protected Double cop;

	protected String lotNo;
	
	protected Integer taxId;
	
	protected String taxName;
	
	protected Double taxPercentage; 
	
	protected Integer vendorId;
	
	protected String vendor;
	
	protected String genericName;
	
	protected Integer manufactureYear;
	
	protected Integer itemid;
	
	protected String assetcategory;
	
	protected Double discount;

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	protected String productcategory;
	protected Double rate;
	protected Integer assetCategoryId;
	protected Date  warrantyFrom;
	protected Date  warrantyTo;
	protected String pharmacyPRNFormat;
	public String getPharmacyPRNFormat() {
		return pharmacyPRNFormat;
	}

	public void setPharmacyPRNFormat(String pharmacyPRNFormat) {
		this.pharmacyPRNFormat = pharmacyPRNFormat;
	}

	public Date getWarrantyFrom() {
		return warrantyFrom;
	}

	public void setWarrantyFrom(Date warrantyFrom) {
		this.warrantyFrom = warrantyFrom;
	}

	public Date getWarrantyTo() {
		return warrantyTo;
	}

	public void setWarrantyTo(Date warrantyTo) {
		this.warrantyTo = warrantyTo;
	}

	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	protected Integer productCategoryId;

	protected String itemname;

	protected String itemcode;
	
	protected BigInteger batchid;

	protected String batchno;

	protected BigInteger availablequantity;

	protected String lotno;

	protected Integer taxid;
	
	protected String taxname;
	
	protected Double taxpercentage; 

	protected Integer manufactureyear;

	protected Integer vendorid;
	
	protected Boolean isBatchRequired;
	
	protected Integer conversion;
	
	Double patientClassPercentage;
	
	boolean markupStatus;

    protected Integer unitId;

    protected Integer organizationId;

    protected Integer storeId;

	public Double getPatientClassPercentage() {
		return patientClassPercentage;
	}

	public void setPatientClassPercentage(Double patientClassPercentage) {
		this.patientClassPercentage = patientClassPercentage;
	}

	public boolean isMarkupStatus() {
		return markupStatus;
	}

	public void setMarkupStatus(boolean markupStatus) {
		this.markupStatus = markupStatus;
	}

	public Integer getConversion() {
		return conversion;
	}

	public void setConversion(Integer conversion) {
		this.conversion = conversion;
	}

	public Boolean getIsBatchRequired() {
		return isBatchRequired;
	}

	public void setIsBatchRequired(Boolean isBatchRequired) {
		this.isBatchRequired = isBatchRequired;
	}

	public Integer getProdCatId() {
		return prodCatId;
	}

	public void setProdCatId(Integer prodCatId) {
		this.prodCatId = prodCatId;
	}

	protected Integer prodCatId;
	
	protected List<BatchAccessoriesMapperDto> accessories;
	
	protected List<UomResponse> uomResponses;
	
	protected Integer markUpDown;
 	
 	public Integer getMarkUpDown() {
		return markUpDown;
	}

	public void setMarkUpDown(Integer markUpDown) {
		this.markUpDown = markUpDown;
	}

	public Double getMarkUpDownPer() {
		return markUpDownPer;
	}

	public void setMarkUpDownPer(Double markUpDownPer) {
		this.markUpDownPer = markUpDownPer;
	}

	protected Double markUpDownPer;

	public List<UomResponse> getUomResponses() {
		return uomResponses;
	}

	public void setUomResponses(List<UomResponse> uomResponses) {
		this.uomResponses = uomResponses;
	}

	public BatchListResponse() {
		super();
	}

	public void setUom(Integer uomTypeId, String uomType, Integer uomUnitId, String uomUnit) {
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

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

	public Long getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(Long availableQuantity) {
		this.availableQuantity = availableQuantity;
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

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
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

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
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

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public Integer getManufactureYear() {
		return manufactureYear;
	}

	public void setManufactureYear(Integer manufactureYear) {
		this.manufactureYear = manufactureYear;
	}

	public List<BatchAccessoriesMapperDto> getAccessories() {
		return accessories;
	}

	public void setAccessories(List<BatchAccessoriesMapperDto> accessories) {
		this.accessories = accessories;
	}

	public Integer getItemid() {
		return itemid;
	}

	public void setItemid(Integer itemid) {
		this.itemid = itemid;
	}

	public String getAssetcategory() {
		return assetcategory;
	}

	public void setAssetcategory(String assetcategory) {
		this.assetcategory = assetcategory;
	}

	public String getProductcategory() {
		return productcategory;
	}

	public void setProductcategory(String productcategory) {
		this.productcategory = productcategory;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

	
	public BigInteger getBatchid() {
		return batchid;
	}

	public void setBatchid(BigInteger batchid) {
		this.batchid = batchid;
	}

	public String getBatchno() {
		return batchno;
	}

	public void setBatchno(String batchno) {
		this.batchno = batchno;
	}

	public BigInteger getAvailablequantity() {
		return availablequantity;
	}

	public void setAvailablequantity(BigInteger availablequantity) {
		this.availablequantity = availablequantity;
	}

	public String getLotno() {
		return lotno;
	}

	public void setLotno(String lotno) {
		this.lotno = lotno;
	}

	public Integer getTaxid() {
		return taxid;
	}

	public void setTaxid(Integer taxid) {
		this.taxid = taxid;
	}

	public String getTaxname() {
		return taxname;
	}

	public void setTaxname(String taxname) {
		this.taxname = taxname;
	}

	public Double getTaxpercentage() {
		return taxpercentage;
	}

	public void setTaxpercentage(Double taxpercentage) {
		this.taxpercentage = taxpercentage;
	}

	public Integer getManufactureyear() {
		return manufactureyear;
	}

	public void setManufactureyear(Integer manufactureyear) {
		this.manufactureyear = manufactureyear;
	}

	public Integer getVendorid() {
		return vendorid;
	}

	public void setVendorid(Integer vendorid) {
		this.vendorid = vendorid;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }
}
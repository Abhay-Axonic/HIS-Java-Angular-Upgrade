package com.param.inventory.stock.dto.openingBalance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.param.entity.model.master.Manufacturer;
import com.param.global.dto.global.TaxDropDown;
import com.param.global.dto.items.UomResponse;

public class ItemResponse {

	protected Integer id;

	protected Integer itemId;

	protected String itemCode;

	protected String itemName;

	protected Integer uomTypeId;

	protected String uomType;

	protected Integer uomUnitId;

	protected String uomUnit;
	
	protected Integer mrpUomTypeId;
	protected String uomDetails;

	protected List<com.param.global.dto.items.UomResponse> uom;

	protected String batchNo;
	protected Long stockAvailable;

	public Long getStockAvailable() {
		return stockAvailable;
	}

	public void setStockAvailable(Long stockAvailable) {
		this.stockAvailable = stockAvailable;
	}

	protected Date expiryDate;

	protected Integer quantity;

	protected Double cop;
	
	protected Double lcop;
	
	protected String lotNo;
	
	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public Boolean getIsBatchReq() {
		return isBatchReq;
	}

	public void setIsBatchReq(Boolean isBatchReq) {
		this.isBatchReq = isBatchReq;
	}

	protected Boolean isBatchReq;

	public Double getLcop() {
		return lcop;
	}

	public void setLcop(Double lcop) {
		this.lcop = lcop;
	}

	protected Double mrp;

	protected Double copAmount;

	protected Double markup;
	
	protected Double markupPercentage;

	protected Integer taxId;

	protected String taxName;

	protected Double taxPercent;

	protected Double taxAmount;

	protected String remark;

	protected Integer statusId;

	protected Long maxOrderQuantity;
	
	protected Integer manufacturerId;
	
    private boolean isThreeUOMApplicable;
    
    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }

	
	List<RackResponse> racks;

	List<TaxDropDown> taxList;

	public List<TaxDropDown> getTaxList() {
		return taxList;
	}

	public List<StorageDetails> getStorageDetails() {
		return storageDetails;
	}

	public void setStorageDetails(List<StorageDetails> storageDetails) {
		this.storageDetails = storageDetails;
	}
	

	public List<StorageDetails> getStorageLocationDetails() {
		return storageLocationDetails;
	}

	public void setStorageLocationDetails(List<StorageDetails> storageLocationDetails) {
		this.storageLocationDetails = storageLocationDetails;
	}

	public void setTaxList(List<TaxDropDown> taxList) {
		this.taxList = taxList;
	}
	
	public ItemResponse() {
		super();
	}

    List<StorageDetails> storageDetails;

    List<StorageDetails> storageLocationDetails;
	public ItemResponse(Integer id, Integer itemId, String itemCode, String itemName, Integer uomTypeId, String uomType,
			Integer uomUnitId, String uomUnit, Integer mrpUomTypeId, List<com.param.global.dto.items.UomResponse> list, 
			String batchNo, Date expiryDate, Integer quantity, Double cop, Double mrp, Double copAmount, Double markup, 
			Integer taxId, String taxName, Double taxPercent, Double taxAmount, String remark, Integer statusId, List<TaxDropDown> taxList,List<RackResponse> racks) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.mrpUomTypeId = mrpUomTypeId;
		this.uom = list;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.quantity = quantity;
		this.cop = cop;
		this.mrp = mrp;
		this.copAmount = copAmount;
		this.markup = markup;
		this.taxId = taxId;
		this.taxName = taxName;
		this.taxPercent = taxPercent;
		this.taxAmount = taxAmount;
		this.remark = remark;
		this.statusId = statusId;
		this.taxList = taxList;
		this.racks = racks;
	}
	
	public ItemResponse(Integer id, Integer itemId, String itemCode, String itemName, Integer uomTypeId, String uomType,
			Integer uomUnitId, String uomUnit, Integer mrpUomTypeId, List<com.param.global.dto.items.UomResponse> list, 
			String batchNo, Date expiryDate, Integer quantity, Double cop, Double mrp, Double copAmount, Double markup,
			Integer taxId, String taxName, Double taxPercent, Double taxAmount, String remark, Integer statusId, List<TaxDropDown> taxList,List<RackResponse> racks , Double lcop, List<StorageDetails> storageLocationDetails) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.mrpUomTypeId = mrpUomTypeId;
		this.uom = list;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.quantity = quantity;
		this.cop = cop;
		this.mrp = mrp;
		this.copAmount = copAmount;
		this.markup = markup;
		this.taxId = taxId;
		this.taxName = taxName;
		this.taxPercent = taxPercent;
		this.taxAmount = taxAmount;
		this.remark = remark;
		this.statusId = statusId;
		this.taxList = taxList;
		this.racks = racks;
		this.lcop = lcop;
		this.storageDetails= storageLocationDetails;
	}
	
	public ItemResponse(Integer id, Integer itemId, String itemCode, String itemName, Integer uomTypeId, String uomType,
			Integer uomUnitId, String uomUnit, Integer mrpUomTypeId, List<com.param.global.dto.items.UomResponse> list, 
			String batchNo, Date expiryDate, Integer quantity, Double cop, Double mrp, Double copAmount, Double markup, 
			Integer taxId, String taxName, Double taxPercent, Double taxAmount, String remark, Integer statusId, List<TaxDropDown> taxList,List<RackResponse> racks , 
			Double lcop, List<StorageDetails> storageLocationDetails,
			Boolean isBatchReq, String lotNo ) {
				super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.mrpUomTypeId = mrpUomTypeId;
		this.uom = list;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.quantity = quantity;
		this.cop = cop;
		this.mrp = mrp;
		this.copAmount = copAmount;
		this.markup = markup;
		this.taxId = taxId;
		this.taxName = taxName;
		this.taxPercent = taxPercent;
		this.taxAmount = taxAmount;
		this.remark = remark;
		this.statusId = statusId;
		this.taxList = taxList;
		this.racks = racks;
		this.lcop = lcop;
		this.storageDetails= storageLocationDetails;
		this.isBatchReq = isBatchReq;
		this.lotNo = lotNo;
		this.storageLocationDetails= storageLocationDetails;
	}
	
	//Opening Balance Implant Details Constructor
	public ItemResponse(Integer id, Integer itemId, String itemCode, String itemName,String batchNo, Date expiryDate, Integer quantity, Double cop, Double mrp, Double copAmount,
			Integer taxId, String taxName, Double taxPercent, Double taxAmount,  Double markupPercentage,Integer manufacturerId ,String remark, Integer statusId, List<TaxDropDown> taxList, Double lcop) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName=itemName;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.quantity = quantity;
		this.cop = cop;
		this.mrp = mrp;
		this.copAmount = copAmount;
		this.taxId = taxId;
		this.taxName = taxName;
		this.taxPercent = taxPercent;
		this.taxAmount = taxAmount;
		this.markupPercentage=markupPercentage;
		this.manufacturerId=manufacturerId;
		this.remark = remark;
		this.statusId = statusId;
		this.taxList = taxList;
		this.lcop=lcop;
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

	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}

	public Integer getMrpUomTypeId() {
		return mrpUomTypeId;
	}

	public void setMrpUomTypeId(Integer mrpUomTypeId) {
		this.mrpUomTypeId = mrpUomTypeId;
	}

	public List<com.param.global.dto.items.UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<com.param.global.dto.items.UomResponse> uom) {
		this.uom = uom;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Double getCopAmount() {
		return copAmount;
	}

	public void setCopAmount(Double copAmount) {
		this.copAmount = copAmount;
	}

	public Double getMarkup() {
		return markup;
	}

	public void setMarkup(Double markup) {
		this.markup = markup;
	}
	
	public Double getMarkupPercentage() {
		return markupPercentage;
	}

	public void setMarkupPercentage(Double markupPercentage) {
		this.markupPercentage = markupPercentage;
	}

	public Integer getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
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

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getUomType() {
		return uomType;
	}

	public void setUomType(String uomType) {
		this.uomType = uomType;
	}

	public String getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public Long getMaxOrderQuantity() {
		return maxOrderQuantity;
	}

	public void setMaxOrderQuantity(Long maxOrderQuantity) {
		this.maxOrderQuantity = maxOrderQuantity;
	}
		
	public List<RackResponse> getRacks() {
		return racks;
	}

	public void setRacks(List<RackResponse> racks) {
		this.racks = racks;
	}

	public void addItems(com.param.global.dto.items.UomResponse uom) {
		if (this.uom == null)
			this.uom = new ArrayList<com.param.global.dto.items.UomResponse>();

		this.uom.add(uom);
	}

	public String getUomDetails() {
		return uomDetails;
	}

	public void setUomDetails(String uomDetails) {
		this.uomDetails = uomDetails;
	}
	
}

/*
 * "id": 1, "itemId": 1, "itemCode": "ITE-1001", "itemName": "Crocin",
 * "uomTypeId": 1, "uomUnitId": 1,
 */

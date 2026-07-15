package com.param.inventory.stockAdjustments.dto.uploadStock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.UomResponse;
import com.param.inventory.stockAdjustments.dto.varianceApproval.BatchResponse;

@JsonInclude(Include.NON_NULL)
public class UploadStockResponseItem {

	private Integer id;

	private Integer assetCategoryId;

	private String assetCategory;

	private Integer productCategoryId;

	private String productCategory;

	private Integer itemId;

	private String itemCode;

	private String itemName;

	private Integer uomTypeId;

	private String uom;
	private String uomType;
	
    private List<UomResponse> uomList;

	public List<UomResponse> getUomList() {
	  return uomList;
	}

	public void setUomList(List<UomResponse> uomList) {
	  this.uomList = uomList;
	}
	
    private boolean isThreeUOMApplicable;
    
    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }

	private Integer varianceQty; 
	private Double varianceCop; 
	private Double varianceMrp;
	public Integer getVarianceQty() {
		return varianceQty;
	}

	public void setVarianceQty(Integer varianceQty) {
		this.varianceQty = varianceQty;
	}

	public Double getVarianceCop() {
		return varianceCop;
	}

	public void setVarianceCop(Double varianceCop) {
		this.varianceCop = varianceCop;
	}

	public Double getVarianceMrp() {
		return varianceMrp;
	}

	public void setVarianceMrp(Double varianceMrp) {
		this.varianceMrp = varianceMrp;
	}

	public String getUomType() {
		return uomType;
	}

	public void setUomType(String uomType) {
		this.uomType = uomType;
	}

	private Integer uomUnitId;

	private String uomUnit;

	private Long batchId;

	private String batchNo;

	private Date expiryDate;

	private Double mrp;

	private Double cop;

	private Long qty;

	private Integer quantity;

	private Integer uploadedQuantity;

	private String status;

	private Integer statusId;
	
	private Integer stopTransactionId; 

	private List<BatchResponse> batches;
	
	private Integer assetTypeId;

	public UploadStockResponseItem() {
		super();
	}

	public UploadStockResponseItem(Integer id, Integer assetCategoryId, String assetCategory, Integer productCategoryId,
			String productCategory, Integer itemId, String itemCode, String itemName, Integer uomTypeId, String uom,
			Integer uomUnitId, String uomUnit, Long batchId, String batchNo, Date expiryDate, Double mrp) {
		super();
		this.id = id;
		this.assetCategoryId = assetCategoryId;
		this.assetCategory = assetCategory;
		this.productCategoryId = productCategoryId;
		this.productCategory = productCategory;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uom = uom;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.mrp = mrp;
	}
	
	public UploadStockResponseItem(Integer id, Integer assetCategoryId, String assetCategory, Integer productCategoryId,
			String productCategory, Integer itemId, String itemCode, String itemName, Integer uomTypeId, String uom,
			Integer uomUnitId, String uomUnit, Long batchId, String batchNo, Date expiryDate, Double mrp,Integer quantity,Integer uploadedQuantity, Double cop ) {
		super();
		this.id = id;
		this.assetCategoryId = assetCategoryId;
		this.assetCategory = assetCategory;
		this.productCategoryId = productCategoryId;
		this.productCategory = productCategory;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uom = uom;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.mrp = mrp;
		this.quantity = quantity;
		this.uploadedQuantity = uploadedQuantity;
		this.cop =cop;
	} 

	public UploadStockResponseItem(Integer id, Integer assetCategoryId, String assetCategory, Integer productCategoryId,
			String productCategory, Integer itemId, String itemCode, String itemName, Integer uomTypeId, String uom,
			Integer uomUnitId, String uomUnit, Long batchId, String batchNo, Date expiryDate, Double mrp,
			Integer quantity, Integer statusId, String status,Integer uploadedQuantity,Integer stopTransactionId,Double cop, boolean isThreeUOMApplicable, List<UomResponse> uomList) {
		super();
		this.id = id;
		this.assetCategoryId = assetCategoryId;
		this.assetCategory = assetCategory;
		this.productCategoryId = productCategoryId;
		this.productCategory = productCategory;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uom = uom;
		this.uomType=uom;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.mrp = mrp;
		this.quantity = quantity;
		this.statusId = statusId;
		this.status = status;
		this.uploadedQuantity= uploadedQuantity;
		this.stopTransactionId = stopTransactionId; 
		this.cop = cop;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
		this.uomList = uomList;
	}

	public UploadStockResponseItem(Integer id, Integer assetCategoryId, String assetCategory, Integer productCategoryId,
			String productCategory, Integer itemId, String itemCode, String itemName, Integer uomTypeId, String uom,
			Integer uomUnitId, String uomUnit, Long batchId, String batchNo, Date expiryDate, Double mrp, Double cop,
			Integer quantity, Integer uploadedQuantity, Integer statusId, String status, boolean isThreeUOMApplicable, List<UomResponse> uomList) {
		super();
		this.id = id;
		this.assetCategoryId = assetCategoryId;
		this.assetCategory = assetCategory;
		this.productCategoryId = productCategoryId;
		this.productCategory = productCategory;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uom = uom;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.mrp = mrp;
		this.cop = cop;
		this.quantity = quantity;
		this.uploadedQuantity = uploadedQuantity;
		this.statusId = statusId;
		this.status = status;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
		this.uomList = uomList;
	}

	public UploadStockResponseItem(Integer id, Integer productCategoryId, String productCategory, Integer itemId,
			String itemCode, String itemName, Integer uomTypeId, String uom, Integer uomUnitId, String uomUnit,
			Double mrp, Integer uploadedQuantity) {
		super();
		this.id = id;
		this.productCategoryId = productCategoryId;
		this.productCategory = productCategory;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uom = uom;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.mrp = mrp;
		this.uploadedQuantity = uploadedQuantity;
	}

	public UploadStockResponseItem(Integer id, Integer productCategoryId, String productCategory, Integer itemId,
			String itemCode, String itemName,Long batchId,String batchNo,Double mrp, Double cop,Integer quantity, Integer uploadedQuantity) {
		super();
		this.id = id;
		this.productCategoryId = productCategoryId;
		this.productCategory = productCategory;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;		
		this.batchId = batchId;
		this.batchNo = batchNo;
		this.mrp = mrp;
		this.cop = cop;	
		this.quantity = quantity;
		this.uploadedQuantity = uploadedQuantity;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
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

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
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

	public Long getQty() {
		return qty;
	}

	public void setQty(Long qty) {
		this.qty = qty;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getUploadedQuantity() {
		return uploadedQuantity;
	}

	public void setUploadedQuantity(Integer uploadedQuantity) {
		this.uploadedQuantity = uploadedQuantity;
	}

	public List<BatchResponse> getBatches() {
		return batches;
	}

	public void setBatches(List<BatchResponse> batches) {
		this.batches = batches;
	}

	public void addBatches(BatchResponse batch) {
		if (this.batches == null)
			this.batches = new ArrayList<BatchResponse>();

		this.batches.add(batch);
	}
	
	
	public Integer getStopTransactionId() {
		return stopTransactionId;
	}

	public void setStopTransactionId(Integer stopTransactionId) {
		this.stopTransactionId = stopTransactionId;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

}

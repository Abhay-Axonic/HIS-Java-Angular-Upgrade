package com.param.pharmacy.nursing.crashCart.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.BatchResponse;
import com.param.global.dto.items.UomResponse;

@JsonInclude(Include.NON_NULL)
public class ItemResponse {

	private Integer id;

	private Integer itemId;

	private String itemCode;

	private String itemName;

	private Integer genericId;

	private String genericName;

	private String uomType;

	private String uomUnit;

	private Integer uomTypeId;

	private Integer uomUnitId;

	private Integer requiredQuantity;

	private Integer approvedQuantity;

	private List<BatchResponse> batch;

	private Integer statusId;
	
    Double patientClassPercentage;
	
	boolean markupStatus;
	
	Integer priorityId;
	
	Integer prodCatId;
	
	 Integer markUpDown;

	 Integer productCategoryId;
	 
	 protected Double markUpDownPer;
	
	public Integer getProdCatId() {
		return prodCatId;
	}

	public void setProdCatId(Integer prodCatId) {
		this.prodCatId = prodCatId;
	}

	public Integer getAssetCatId() {
		return assetCatId;
	}

	public void setAssetCatId(Integer assetCatId) {
		this.assetCatId = assetCatId;
	}

	Integer assetCatId;

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

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}
	
    private List<UomResponse> uom;

    public List<UomResponse> getUom() {
        return uom;
    }

    public void setUom(List<UomResponse> uom) {
        this.uom = uom;
    }
	
    private boolean isThreeUOMApplicable;
    
    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }

	public ItemResponse() {
		super();
	}

	public ItemResponse(Integer id, Integer itemId, String itemCode, String itemName, Integer genericId,
			String genericName, String uomType, String uomUnit, Integer requiredQuantity, boolean isThreeUOMApplicable, List<UomResponse> uom) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericId = genericId;
		this.genericName = genericName;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.requiredQuantity = requiredQuantity;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
		this.uom = uom;
	}

	public ItemResponse(Integer id, Integer itemId, String itemCode, String itemName, Integer genericId,
			String genericName, Integer requiredQuantity, Integer approvedQuantity, List<BatchResponse> batch, boolean isThreeUOMApplicable) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.genericId = genericId;
		this.genericName = genericName;
		this.requiredQuantity = requiredQuantity;
		this.approvedQuantity = approvedQuantity;
		this.batch = batch;
        this.isThreeUOMApplicable = isThreeUOMApplicable;
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

	public Integer getRequiredQuantity() {
		return requiredQuantity;
	}

	public void setRequiredQuantity(Integer requiredQuantity) {
		this.requiredQuantity = requiredQuantity;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public List<BatchResponse> getBatch() {
		return batch;
	}

	public void setBatch(List<BatchResponse> batch) {
		this.batch = batch;
	}

	public Integer getMarkUpDown() {
		return markUpDown;
	}

	public void setMarkUpDown(Integer markUpDown) {
		this.markUpDown = markUpDown;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Double getMarkUpDownPer() {
		return markUpDownPer;
	}

	public void setMarkUpDownPer(Double markUpDownPer) {
		this.markUpDownPer = markUpDownPer;
	}

}

package com.param.inventory.nursing.dto.nonBillableConsumption;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.BatchListResponse;
import com.param.global.dto.items.UomResponse;

@JsonInclude(Include.NON_NULL)
public class BatchResponse extends BatchRequest {
	
	protected String itemCode;

	protected String itemName;

	protected String uomType;

	protected String uomUnit;
	
	protected Integer itemId;
	
	protected Integer availableQuantity;
	
	List<UomResponse> uom;
	
	protected Integer organizationId;
	/* start code for resolved HIS-10494 While Updating consumption then batch number is not getting display in non billable consumption screen */
	protected List<BatchListResponse> batches;
	public List<BatchListResponse> getBatches() {
		return batches;
	}
	public void setBatches(List<BatchListResponse> batches) {
		this.batches = batches;
	}
	/* end code for resolved HIS-10494 While Updating consumption then batch number is not getting display in non billable consumption screen */

    private boolean isThreeUOMApplicable;
    
    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
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

	public Integer getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(Integer stockAvailable) {
		this.availableQuantity = stockAvailable;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	
	public List<UomResponse> getUom() {
		return uom;
	}
	
	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}
}

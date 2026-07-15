package com.param.procurement.purchase.dto.purchaseRequest;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.param.global.dto.items.UomResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemResponse {
    private Integer id;
    private Integer itemId;
    private String itemCode;
    private String itemName;
    private Integer uomTypeId;
    private String uomType;
    private Integer uomUnitId;
    private String uomUnit;
    private Long availableQuantity;
    private Integer quantity;
    private Integer indentQuantity;
    private boolean isThreeUOMApplicable;
    private Integer pendingQuantity;
    public Integer getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	public boolean getIsThreeUOMApplicable() {
		return isThreeUOMApplicable;
	}

	public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}


	private Long maxOrderQuantity;
    private Integer maxStoreQuantity;
    private List<Integer> indentId;
    private String specification;
    private List<UomResponse> uom;

    public ItemResponse() {
        super();
    }

    public ItemResponse(Integer id, Integer itemId, String itemCode, String itemName, Integer uomTypeId, String uomType, Integer uomUnitId,
                        String uomUnit, Long availableQuantity, Integer quantity, Integer maxStoreQuantity, Long maxOrderQuantity,String specification, boolean isThreeUOMApplicable, List<UomResponse> uom) {
        this.id = id;
        this.itemId = itemId;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.uomTypeId = uomTypeId;
        this.uomType = uomType;
        this.uomUnitId = uomUnitId;
        this.uomUnit = uomUnit;
        this.availableQuantity = availableQuantity;
        this.quantity = quantity;
        this.maxStoreQuantity = maxStoreQuantity;
        this.maxOrderQuantity = maxOrderQuantity;
        this.specification = specification;
        this.isThreeUOMApplicable = isThreeUOMApplicable;
        this.uom = uom;
    }


    public ItemResponse(Integer id, Integer itemId, String itemCode, String itemName, Long availableQuantity, Integer quantity, Integer maxStoreQuantity,Long maxOrderQuantity,String specification) {
        this.id = id;
        this.itemId = itemId;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.availableQuantity = availableQuantity;
        this.quantity = quantity;
        this.maxStoreQuantity = maxStoreQuantity;
        this.maxOrderQuantity = maxOrderQuantity;       
        this.specification = specification;
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

    public Long getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Long availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getIndentQuantity() {
        return indentQuantity;
    }

    public void setIndentQuantity(Integer indentQuantity) {
        this.indentQuantity = indentQuantity;
    }

    public List<Integer> getIndentId() {
        return indentId;
    }

    public void setIndentId(List<Integer> indentId) {
        this.indentId = indentId;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public List<UomResponse> getUom() {
        return uom;
    }

    public void setUom(List<UomResponse> uom) {
        this.uom = uom;
    }
    
    public Long getMaxOrderQuantity() {
		return maxOrderQuantity;
	}

	public void setMaxOrderQuantity(Long maxOrderQuantity) {
		this.maxOrderQuantity = maxOrderQuantity;
	}

	public Integer getMaxStoreQuantity() {
		return maxStoreQuantity;
	}

	public void setMaxStoreQuantity(Integer maxStoreQuantity) {
		this.maxStoreQuantity = maxStoreQuantity;
	}

	@Override
    public String toString() {
        return "ItemResponse{" +
                "id=" + id +
                ", itemId=" + itemId +
                ", itemCode='" + itemCode + '\'' +
                ", itemName='" + itemName + '\'' +
                ", uomTypeId=" + uomTypeId +
                ", uomType='" + uomType + '\'' +
                ", uomUnitId=" + uomUnitId +
                ", uomUnit='" + uomUnit + '\'' +
                ", availableQuantity=" + availableQuantity +
                ", quantity=" + quantity +
                ", indentQuantity=" + indentQuantity +
                ", indentId=" + indentId +
                ", specification='" + specification + '\'' +
                ", uom=" + uom +
                '}';
    }
}

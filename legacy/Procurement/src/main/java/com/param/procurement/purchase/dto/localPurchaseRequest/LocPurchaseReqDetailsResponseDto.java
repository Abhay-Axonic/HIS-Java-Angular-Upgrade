package com.param.procurement.purchase.dto.localPurchaseRequest;

import java.util.List;

import com.param.global.dto.items.UomResponse;

public class LocPurchaseReqDetailsResponseDto {

	private Integer id;

	private Integer itemId;

	private String itemCode;

	private String itemName;

	private Short uomTypeId;

	private String uomType;

	private Short uomUnitId;

	private String uomUnit;

	protected List<UomResponse> uom;

	private Integer quantity;

	private Double cop;

	private Long availableQuantity;
	
	private Integer rejectQuantity;

	private Double copAmount;

	private String specifications;

	private Integer indentQuantity;

	private Integer approvedQuantity;
	
	private Long maxOrderQuantity;
	
    private Integer maxStoreQuantity;
	
    private Integer statusId;
	
	private String status;
	
	private boolean isThreeUOMApplicable;

	public boolean getIsThreeUOMApplicable() {
		return isThreeUOMApplicable;
	}

	public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}

	List<Integer> indentId;
	
	private Integer pendingQuantity;
	public Integer getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}
	
	public LocPurchaseReqDetailsResponseDto(Integer id, Integer itemId, String itemCode, String itemName,
			Short uomTypeId, String uomType, Short uomUnitId, String uomUnit, List<UomResponse> uom,
			Integer quantity, Double cop, Long availableQuantity,Integer rejectQuantity, Double copAmount, String specifications,
			Integer indentQuantity, Integer approvedQuantity,Integer maxStoreQuantity,Long maxOrderQuantity,Integer statusId,String status, List<Integer> indentId, boolean isThreeUOMApplicable) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomTypeId = uomTypeId;
		this.uomType = uomType;
		this.uomUnitId = uomUnitId;
		this.uomUnit = uomUnit;
		this.uom = uom;
		this.quantity = quantity;
		this.cop = cop;
		this.availableQuantity = availableQuantity;
		this.rejectQuantity = rejectQuantity;
		this.copAmount = copAmount;
		this.specifications = specifications;
		this.indentQuantity = indentQuantity;
		this.approvedQuantity = approvedQuantity;
		this.maxStoreQuantity = maxStoreQuantity;
		this.maxOrderQuantity = maxOrderQuantity;
		this.statusId = statusId;
		this.status = status;
		this.indentId = indentId;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}
	
	public LocPurchaseReqDetailsResponseDto(Integer id, Integer itemId, String itemCode, String itemName,
			Long availableQuantity,Integer approvedQuantity,Integer maxStoreQuantity,Long maxOrderQuantity,Integer rejectQuantity,Double copAmount,Double cop,Integer quantity,
			Integer indentQuantity,List<Integer> indentId,String specifications,Integer statusId,String status, boolean isThreeUOMApplicable) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.availableQuantity = availableQuantity;
		this.approvedQuantity = approvedQuantity;
		this.maxStoreQuantity = maxStoreQuantity;
		this.maxOrderQuantity = maxOrderQuantity;
		this.rejectQuantity = rejectQuantity;
		this.copAmount = copAmount;
		this.cop = cop;
		this.quantity = quantity;
		this.indentQuantity = indentQuantity;
		this.indentId = indentId;
		this.specifications = specifications;
		this.statusId = statusId;
		this.status = status;
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

	public Short getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Short uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public String getUomType() {
		return uomType;
	}

	public void setUomType(String uomType) {
		this.uomType = uomType;
	}

	public Short getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Short uomUnitId) {
		this.uomUnitId = uomUnitId;
	}

	public String getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Long getAvailableQuantity() {
		return availableQuantity;
	}

	public void setAvailableQuantity(Long availableQuantity) {
		this.availableQuantity = availableQuantity;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Double getCopAmount() {
		return copAmount;
	}

	public void setCopAmount(Double copAmount) {
		this.copAmount = copAmount;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public Integer getIndentQuantity() {
		return indentQuantity;
	}

	public void setIndentQuantity(Integer indentQuantity) {
		this.indentQuantity = indentQuantity;
	}

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public List<Integer> getIndentId() {
		return indentId;
	}

	public void setIndentId(List<Integer> indentId) {
		this.indentId = indentId;
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

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
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
	
}

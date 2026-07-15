package com.param.procurement.purchase.dto.purchaseRequestApproval;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.param.global.dto.items.UomResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemResponse {

	private Integer id;
	private Integer itemId;
	private String itemCode;
	private String itemName;
	private String uomType;
	private String uomUnit;
	private Long availableQuantity;
	private Integer quantity;
	private Integer approvedQuantity;
	private Long maxOrderQuantity;
	private Integer maxStoreQuantity;
	private Integer rejectQuantity;
	private Integer rejectReasonId;
	private String rejectNote;
	private String specification;
	private String status;
	private Integer pendingQuantity;
	private Integer indentQuantity;
	private boolean isThreeUOMApplicable;

	public boolean isThreeUOMApplicable() {
		return isThreeUOMApplicable;
	}

	public void setThreeUOMApplicable(boolean isThreeUOMApplicable) {
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}

	public Integer getIndentQuantity() {
		return indentQuantity;
	}

	public void setIndentQuantity(Integer indentQuantity) {
		this.indentQuantity = indentQuantity;
	}

	public Integer getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	private Integer statusId;
	private List<UomResponse> uom;

	public ItemResponse() {
		super();
	}

	public ItemResponse(Integer id, Integer itemId, String itemCode, String itemName, String uomType, String uomUnit,
			Long availableQuantity, Integer quantity, Integer approvedQuantity, Integer maxStoreQuantity,
			Long maxOrderQuantity, Integer rejectQuantity, Integer rejectReasonId, String rejectNote,
			String specification, String status, Integer statusId, List<UomResponse> uom) {
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.availableQuantity = availableQuantity;
		this.maxStoreQuantity = maxStoreQuantity;
		this.maxOrderQuantity = maxOrderQuantity;
		this.quantity = quantity;
		this.approvedQuantity = approvedQuantity;
		this.rejectQuantity = rejectQuantity;
		this.rejectReasonId = rejectReasonId;
		this.rejectNote = rejectNote;
		this.specification = specification;
		this.status = status;
		this.statusId = statusId;
		this.uom = uom;
	}

	public ItemResponse(Integer id, Integer itemId, String itemCode, String itemName, String uomType, String uomUnit,
			Long availableQuantity, Integer quantity, Integer approvedQuantity, Integer maxStoreQuantity,
			Long maxOrderQuantity, Integer rejectQuantity, Integer rejectReasonId, String rejectNote,
			String specification, String status, Integer statusId, List<UomResponse> uom, Integer pendingQuantity, boolean isThreeUOMApplicable) {
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.availableQuantity = availableQuantity;
		this.maxStoreQuantity = maxStoreQuantity;
		this.maxOrderQuantity = maxOrderQuantity;
		this.quantity = quantity;
		this.approvedQuantity = approvedQuantity;
		this.rejectQuantity = rejectQuantity;
		this.rejectReasonId = rejectReasonId;
		this.rejectNote = rejectNote;
		this.specification = specification;
		this.status = status;
		this.statusId = statusId;
		this.uom = uom;
		this.pendingQuantity = pendingQuantity;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}

	public ItemResponse(Integer id, Integer itemId, String itemCode, String itemName, Long availableQuantity,
			Integer quantity, Integer approvedQuantity, Integer maxStoreQuantity, Long maxOrderQuantity,
			Integer rejectQuantity, String specification, String status, Integer statusId) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.availableQuantity = availableQuantity;
		this.quantity = quantity;
		this.approvedQuantity = approvedQuantity;
		this.maxStoreQuantity = maxStoreQuantity;
		this.maxOrderQuantity = maxOrderQuantity;
		this.rejectQuantity = rejectQuantity;
		this.specification = specification;
		this.status = status;
		this.statusId = statusId;
	}
	public ItemResponse(Integer id, Integer itemId, String itemCode, String itemName, Long availableQuantity,
			Integer quantity, Integer approvedQuantity, Integer maxStoreQuantity, Long maxOrderQuantity,
			Integer rejectQuantity, String specification, String status, Integer statusId ,Integer pendingQuantity, boolean isThreeUOMApplicable) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.availableQuantity = availableQuantity;
		this.quantity = quantity;
		this.approvedQuantity = approvedQuantity;
		this.maxStoreQuantity = maxStoreQuantity;
		this.maxOrderQuantity = maxOrderQuantity;
		this.rejectQuantity = rejectQuantity;
		this.specification = specification;
		this.status = status;
		this.statusId = statusId;
		this.pendingQuantity= pendingQuantity;
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

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
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

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}

	@Override
	public String toString() {
		return "ItemResponse{" + "id=" + id + ", itemId=" + itemId + ", itemCode='" + itemCode + '\'' + ", itemName='"
				+ itemName + '\'' + ", uomType='" + uomType + '\'' + ", uomUnit='" + uomUnit + '\''
				+ ", availableQuantity=" + availableQuantity + ", quantity=" + quantity + ", approvedQuantity="
				+ approvedQuantity + ", rejectQuantity=" + rejectQuantity + ", rejectReasonId=" + rejectReasonId
				+ ", rejectNote='" + rejectNote + '\'' + ", specification='" + specification + '\'' + ", status='"
				+ status + '\'' + ", statusId=" + statusId + '}';
	}
}

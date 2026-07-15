package com.param.inventory.store.dto.storeIndentPending;

import com.param.entity.model.master.RejectReason;
import com.param.inventory.store.dto.storeIndentApproval.ItemApprovalRequest;

public class ItemPendingRequest extends ItemApprovalRequest {

	protected Integer pendingQuantity;

	public ItemPendingRequest() {
		super();
	}

	public ItemPendingRequest(Integer id, Integer itemId, Integer uomTypeId, Integer uomUnitId, Integer quantity,
			Integer statusId, String itemCode, String itemName, String generic, String manufacturer, String uomType,
			String uomUnit, Long stockAvailable, Long stockInTransit, Long lastMonthSale, Long currentMonthSale,
			Long maxOrderQuantity, Integer approvedQuantity, Integer rejectQuantity, RejectReason rejectReason,
			String rejectNote, Integer pendingQuantity, boolean isThreeUOMApplicable) {
		super(id, itemId, uomTypeId, uomUnitId, quantity, statusId, itemCode, itemName, generic, manufacturer, uomType,
				uomUnit, stockAvailable, stockInTransit, lastMonthSale, currentMonthSale, maxOrderQuantity,
				approvedQuantity, rejectQuantity, rejectReason, rejectNote, isThreeUOMApplicable);
		this.pendingQuantity = pendingQuantity;
	}

	public Integer getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

}

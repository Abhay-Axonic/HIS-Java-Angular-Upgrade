package com.param.inventory.store.dto.storeIndentApproval;

import com.param.entity.model.master.RejectReason;
import com.param.inventory.store.dto.storeIndent.ItemResponse;

public class ItemApprovalRequest extends ItemResponse {
	protected Integer approvedQuantity;

	protected Integer rejectQuantity;

	protected Integer rejectReasonId;

	protected String rejectNote;

	public ItemApprovalRequest() {
		super();
	}

	public ItemApprovalRequest(Integer id, Integer itemId, Integer uomTypeId, Integer uomUnitId, Integer quantity,
			Integer statusId, String itemCode, String itemName, String generic, String manufacturer, String uomType,
			String uomUnit, Long stockAvailable, Long stockInTransit, Long lastMonthSale, Long currentMonthSale,
			Long maxOrderQuantity, Integer approvedQuantity, Integer rejectQuantity, RejectReason rejectReason,
			String rejectNote, boolean isThreeUOMApplicable) {
		super(id, itemId, uomTypeId, uomUnitId, quantity, statusId, itemCode, itemName, generic, manufacturer, uomType,
				uomUnit, stockAvailable, stockInTransit, lastMonthSale, currentMonthSale, maxOrderQuantity, null, isThreeUOMApplicable);
		this.approvedQuantity = approvedQuantity;
		this.rejectQuantity = rejectQuantity;
		this.rejectNote = rejectNote;
		
		if (rejectReason != null)
			this.rejectReasonId = rejectReason.getId();
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

	public void setRejectReasonId(Integer rejecteReasonId) {
		this.rejectReasonId = rejecteReasonId;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}
}

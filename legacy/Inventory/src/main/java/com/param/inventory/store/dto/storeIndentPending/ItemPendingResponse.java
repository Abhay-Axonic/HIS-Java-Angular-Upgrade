package com.param.inventory.store.dto.storeIndentPending;

import java.util.List;

import com.param.entity.model.master.RejectReason;
import com.param.global.dto.items.UomResponse;

public class ItemPendingResponse extends ItemPendingRequest {

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	protected List<UomResponse> uom;

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}

	public Integer getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
	}

	protected String status;

	protected Integer issueQuantity;

	public ItemPendingResponse(Integer id, Integer itemId, Integer uomTypeId, Integer uomUnitId, Integer quantity,
			Integer statusId, String itemCode, String itemName, String generic, String manufacturer, String uomType,
			String uomUnit, Long stockAvailable, Long stockInTransit, Long lastMonthSale, Long currentMonthSale,
			Long maxOrderQuantity, Integer approvedQuantity, Integer rejectQuantity, RejectReason rejectReason,
			String status, String rejectNote, Integer pendingQuantity, boolean isThreeUOMApplicable,List<UomResponse> uom) {
		super(id, itemId, uomTypeId, uomUnitId, quantity, statusId, itemCode, itemName, generic, manufacturer, uomType,
				uomUnit, stockAvailable, stockInTransit, lastMonthSale, currentMonthSale, maxOrderQuantity,
				approvedQuantity, rejectQuantity, rejectReason, rejectNote, pendingQuantity, isThreeUOMApplicable);
		this.uom = uom;
		this.status = status;
	}

}

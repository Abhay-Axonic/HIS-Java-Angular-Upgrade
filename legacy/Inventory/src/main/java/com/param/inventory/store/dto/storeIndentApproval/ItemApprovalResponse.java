package com.param.inventory.store.dto.storeIndentApproval;

import java.util.List;

import com.param.entity.model.master.RejectReason;
import com.param.global.dto.items.UomResponse;

public class ItemApprovalResponse extends ItemApprovalRequest {

	protected List<UomResponse> uom;

	public List<UomResponse> getUom() {
		return uom;
	}

	public void setUom(List<UomResponse> uom) {
		this.uom = uom;
	}

	protected Long maxStoreQuantity;

	public ItemApprovalResponse() {
		super();
	}

	public ItemApprovalResponse(Integer id, Integer itemId, Integer uomTypeId, Integer uomUnitId, Integer quantity,
			Integer statusId, String itemCode, String itemName, String generic, String manufacturer, String uomType,
			String uomUnit, Long stockAvailable, Long stockInTransit, Long lastMonthSale, Long currentMonthSale,
			Long maxOrderQuantity, Integer approvedQuantity, Integer rejectQuantity, RejectReason rejectReason,
			String rejectNote, Long maxStoreQuantity,List<UomResponse> uom, boolean isThreeUOMApplicable) {
		super(id, itemId, uomTypeId, uomUnitId, quantity, statusId, itemCode, itemName, generic, manufacturer, uomType,
				uomUnit, stockAvailable, stockInTransit, lastMonthSale, currentMonthSale, maxOrderQuantity,
				approvedQuantity, rejectQuantity, rejectReason, rejectNote, isThreeUOMApplicable);
		
		this.uom = uom;
		this.maxStoreQuantity = maxStoreQuantity;
	}

	public Long getMaxStoreQuantity() {
		return maxStoreQuantity;
	}

	public void setMaxStoreQuantity(Long maxStoreQuantity) {
		this.maxStoreQuantity = maxStoreQuantity;
	}
}

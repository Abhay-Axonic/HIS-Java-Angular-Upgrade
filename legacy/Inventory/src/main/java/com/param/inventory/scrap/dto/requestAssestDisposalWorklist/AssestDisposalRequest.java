package com.param.inventory.scrap.dto.requestAssestDisposalWorklist;

import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class AssestDisposalRequest extends CookieObjectDto{

	protected Integer id;

	protected Integer scrapRequestId;

	protected Integer statusId;

	protected Integer witnessId;
	protected Integer approvedDisposedQty;

	

	protected List<ItemRequest> items;

	protected Integer assetDisposalWorklistRequestId;
	
	// start code for HIS-8553 Scrap witness approval does not reduce the quantity on store stock dashboard
	protected Integer storeId;
	public Integer getStoreId() {
		return storeId;
	}
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	// end code for HIS-8553 Scrap witness approval does not reduce the quantity on store stock dashboard

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getScrapRequestId() {
		return scrapRequestId;
	}

	public void setScrapRequestId(Integer scrapRequestId) {
		this.scrapRequestId = scrapRequestId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getWitnessId() {
		return witnessId;
	}

	public void setWitnessId(Integer witnessId) {
		this.witnessId = witnessId;
	}

	public Integer getApprovedDisposedQty() {
		return approvedDisposedQty;
	}

	public void setApprovedDisposedQty(Integer approvedDisposedQty) {
		this.approvedDisposedQty = approvedDisposedQty;
	}
	public List<ItemRequest> getItems() {
		return items;
	}

	public void setItems(List<ItemRequest> items) {
		this.items = items;
	}

	public Integer getAssetDisposalWorklistRequestId() {
		return assetDisposalWorklistRequestId;
	}

	public void setAssetDisposalWorklistRequestId(Integer assetDisposalWorklistRequestId) {
		this.assetDisposalWorklistRequestId = assetDisposalWorklistRequestId;
	}

}

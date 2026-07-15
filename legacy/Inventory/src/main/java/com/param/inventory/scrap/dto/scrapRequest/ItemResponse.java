package com.param.inventory.scrap.dto.scrapRequest;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.UomResponse;

@JsonInclude(Include.NON_NULL)
public class ItemResponse extends com.param.inventory.store.dto.storeIndent.ItemResponse {

	protected Integer scrapQuantity;

	protected Integer scrapReasonId;

	protected String scrapReason;

	protected Integer approvedQuantity;

	protected Integer acceptedQuantity;

	protected Integer rejectQuantity;

	protected Integer rejectReasonId;

	protected String rejectNote;

	protected String rejectReason;

	protected Date expiry;

	protected Long availableQuantity;

	protected Double cop;

	protected String lotNo;

	protected Double amountCop;

	protected Integer disposeQuantity;
	
    private boolean isThreeUOMApplicable;
    
    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }

	protected Integer approvedDisposedQty;
	protected List<com.param.global.dto.items.UomResponse> uomList;

	public List<com.param.global.dto.items.UomResponse> getUomList() {
		return uomList;
	}

	public void setUomList(List<com.param.global.dto.items.UomResponse> uomList) {
		this.uomList = uomList;
	}

	public ItemResponse() {
		super();
	}

	public ItemResponse(Integer id, Integer itemId, Integer uomTypeId, Integer uomUnitId, String itemCode,
			String itemName, String genericName, String uomType, String uomUnit, String batchNo, Date expiry,
			Integer scrapQuantity, Integer scrapReasonId, String scrapReason, String lotNo, Double cop,
			Long availableQuantity, Double amountCop, boolean isThreeUOMApplicable) {
		super(id, itemId, uomTypeId, uomUnitId, itemCode, itemName, genericName, uomType, uomUnit, batchNo);
		this.scrapQuantity = scrapQuantity;
		this.scrapReasonId = scrapReasonId;
		this.scrapReason = scrapReason;
		this.expiry = expiry;
		this.lotNo = lotNo;
		this.cop = cop;
		this.availableQuantity = availableQuantity;
		this.amountCop = amountCop;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}
	public ItemResponse(Integer id, Integer itemId, Integer uomTypeId, Integer uomUnitId, String itemCode,
			String itemName, String genericName, String uomType, String uomUnit, String batchNo, Date expiry,
			Integer scrapQuantity, Integer scrapReasonId, String scrapReason, String lotNo, Double cop,
			Long availableQuantity, Double amountCop,List<UomResponse> uomList, boolean isThreeUOMApplicable) {
		super(id, itemId, uomTypeId, uomUnitId, itemCode, itemName, genericName, uomType, uomUnit, batchNo);
		this.scrapQuantity = scrapQuantity;
		this.scrapReasonId = scrapReasonId;
		this.scrapReason = scrapReason;
		this.expiry = expiry;
		this.lotNo = lotNo;
		this.cop = cop;
		this.availableQuantity = availableQuantity;
		this.amountCop = amountCop;
		this.uomList=uomList;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}
	public ItemResponse(Integer id, Integer itemId, Integer uomTypeId, Integer uomUnitId, String itemCode,
			String itemName, String genericName, String uomType, String uomUnit, String batchNo, Date expiry,
			Integer scrapQuantity, Integer scrapReasonId, String scrapReason, String lotNo, Double cop,
			Long availableQuantity, Double amountCop, Integer acceptedQuantity, Integer rejectQuantity,
			Integer rejectReasonId, String rejectReason, String rejectNote, boolean isThreeUOMApplicable, List<UomResponse> uomList) {
		super(id, itemId, uomTypeId, uomUnitId, itemCode, itemName, genericName, uomType, uomUnit, batchNo);
		this.scrapQuantity = scrapQuantity;
		this.scrapReasonId = scrapReasonId;
		this.scrapReason = scrapReason;
		this.lotNo = lotNo;
		this.cop = cop;
		this.expiry = expiry;
		this.availableQuantity = availableQuantity;
		this.amountCop = amountCop;
		this.acceptedQuantity = acceptedQuantity;
		this.rejectQuantity = rejectQuantity;
		this.rejectReasonId = rejectReasonId;
		this.rejectReason = rejectReason;
		this.rejectNote = rejectNote;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
		this.uomList = uomList;
	}

	public Integer getAcceptedQuantity() {
		return acceptedQuantity;
	}

	public void setAcceptedQuantity(Integer acceptedQuantity) {
		this.acceptedQuantity = acceptedQuantity;
	}

	public Integer getScrapQuantity() {
		return scrapQuantity;
	}

	public void setScrapQuantity(Integer scrapQuantity) {
		this.scrapQuantity = scrapQuantity;
	}

	public Integer getScrapReasonId() {
		return scrapReasonId;
	}

	public void setScrapReasonId(Integer scrapReasonId) {
		this.scrapReasonId = scrapReasonId;
	}

	public String getScrapReason() {
		return scrapReason;
	}

	public void setScrapReason(String scrapReason) {
		this.scrapReason = scrapReason;
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

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
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

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public Double getAmountCop() {
		return amountCop;
	}

	public void setAmountCop(Double amountCop) {
		this.amountCop = amountCop;
	}

	public Integer getDisposeQuantity() {
		return disposeQuantity;
	}

	public void setDisposeQuantity(Integer disposeQuantity) {
		this.disposeQuantity = disposeQuantity;
	}

	public Integer getApprovedDisposedQty() {
		return approvedDisposedQty;
	}

	public void setApprovedDisposedQty(Integer approvedDisposedQty) {
		this.approvedDisposedQty = approvedDisposedQty;
	}
}


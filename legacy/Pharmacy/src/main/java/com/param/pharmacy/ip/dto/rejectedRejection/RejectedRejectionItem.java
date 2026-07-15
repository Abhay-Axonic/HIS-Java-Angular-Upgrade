package com.param.pharmacy.ip.dto.rejectedRejection;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.dto.items.UomResponse;

@JsonInclude(Include.NON_NULL)
public class RejectedRejectionItem {
	private Integer id;

	private Integer itemId;

	private String itemName;

	private String genericName;
	private Integer genericId;

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	private boolean rejectionFlag;

	private String uom;

	private String uomUnit;

	private String batchNo;

	private Date expiryDate;

	private Double price;

	private Integer rejectedQuantity;

	private Integer rejectedRejectQuantity;

	private Integer rejectQuantity;

	private Integer acceptQuantity;

	private String rejectNote;

	private Integer rejectReasonId;

	private Integer qty;

	private String reasonForReject;

	private Integer batchMapperId;

	private Integer statusId;

	private String status;
	
    private boolean isThreeUOMApplicable;
    
    private Integer MarkUpDown;
    
	private Double MarkUpDownPer;

    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }
    
    protected List<UomResponse> uomList;

    public List<UomResponse> getUomList() {
        return uomList;
    }

    public void setUomList(List<UomResponse> uomList) {
        this.uomList = uomList;
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

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public boolean isRejectionFlag() {
		return rejectionFlag;
	}

	public void setRejectionFlag(boolean rejectionFlag) {
		this.rejectionFlag = rejectionFlag;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public String getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(String uomUnit) {
		this.uomUnit = uomUnit;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getReasonForReject() {
		return reasonForReject;
	}

	public void setReasonForReject(String reasonForReject) {
		this.reasonForReject = reasonForReject;
	}

	public Integer getBatchMapperId() {
		return batchMapperId;
	}

	public void setBatchMapperId(Integer batchMapperId) {
		this.batchMapperId = batchMapperId;
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

	public Integer getAcceptQuantity() {
		return acceptQuantity;
	}

	public void setAcceptQuantity(Integer acceptQuantity) {
		this.acceptQuantity = acceptQuantity;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public Integer getRejectedQuantity() {
		return rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Integer getRejectedRejectQuantity() {
		return rejectedRejectQuantity;
	}

	public void setRejectedRejectQuantity(Integer rejectedRejectQuantity) {
		this.rejectedRejectQuantity = rejectedRejectQuantity;
	}

	public RejectedRejectionItem(Integer id, Integer itemId, String itemName, String genericName, boolean rejectionFlag,
			String uom, String uomUnit, String batchNo, Date expiryDate, Double price, Integer rejectedQuantity,
			String reasonForReject, Integer batchMapperId, boolean isThreeUOMApplicable, List<UomResponse> uomList) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.genericName = genericName;
		this.rejectionFlag = rejectionFlag;
		this.uom = uom;
		this.uomUnit = uomUnit;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.price = price;
		this.rejectedQuantity = rejectedQuantity;
		this.reasonForReject = reasonForReject;
		this.batchMapperId = batchMapperId;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
		this.uomList = uomList;
	}

	public RejectedRejectionItem(Integer id, Integer itemId, Integer batchMapperId, String itemName, String genericName, boolean rejectionFlag,
			String uom, String uomUnit, String batchNo, Date expiryDate, Double price, Integer rejectedRejectQuantity,
			String reasonForReject, boolean isThreeUOMApplicable, List<UomResponse> uomList) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.genericName = genericName;
		this.rejectionFlag = rejectionFlag;
		this.uom = uom;
		this.uomUnit = uomUnit;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.price = price;
		this.rejectedRejectQuantity = rejectedRejectQuantity;
		this.reasonForReject = reasonForReject;
		this.batchMapperId = batchMapperId;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
		this.uomList = uomList;
	}

	public Integer getMarkUpDown() {
		return MarkUpDown;
	}

	public void setMarkUpDown(Integer markUpDown) {
		MarkUpDown = markUpDown;
	}

	public Double getMarkUpDownPer() {
		return MarkUpDownPer;
	}

	public void setMarkUpDownPer(Double markUpDownPer) {
		MarkUpDownPer = markUpDownPer;
	}

}
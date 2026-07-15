package com.param.pharmacy.ip.dto.returnAcceptance;

import java.util.Date;
import java.util.List;

import com.param.global.dto.items.BatchResponse;
import com.param.global.dto.items.UomResponse;

public class ReturnAcceptanceItem
{
	private Integer id;
	
	private Integer itemId;
	
	private String itemName;
	
	private String genericName;
	
	private String batchNo;
	
	private Date expiry;
	
	private String uom;
	private List<BatchResponse> batch;
	private String uomUnit;
	private long batchId;
	public long getBatchId() {
		return batchId;
	}

	public void setBatchId(long batchId) {
		this.batchId = batchId;
	}
	private Double rate;
	private Double mrp;
	private Integer returnQuantity;
	public Integer getReturnAcceptedQuantity() {
		return returnAcceptedQuantity;
	}

	public void setReturnAcceptedQuantity(Integer returnAcceptedQuantity) {
		this.returnAcceptedQuantity = returnAcceptedQuantity;
	}
	private Integer returnAcceptedQuantity;
	private Integer rejectedQuantity;
	private Boolean isReturnAcceptance;
	private Integer acceptedQuantity;
	private Double actualLendingPrice;
	
    private boolean isThreeUOMApplicable;

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
	
	public Integer getAcceptedQuantity() {
		return acceptedQuantity;
	}

	public void setAcceptedQuantity(Integer acceptedQuantity) {
		this.acceptedQuantity = acceptedQuantity;
	}

	public Boolean getIsReturnAcceptance() {
		return isReturnAcceptance;
	}

	public void setIsReturnAcceptance(Boolean isReturnAcceptance) {
		this.isReturnAcceptance = isReturnAcceptance;
	}

	public Integer getRejectedQuantity() {
		return rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
	}

	public Integer getAcceptReturnQuantity() {
		return acceptReturnQuantity;
	}

	public void setAcceptReturnQuantity(Integer acceptReturnQuantity) {
		this.acceptReturnQuantity = acceptReturnQuantity;
	}
	private Integer acceptReturnQuantity;
	private Integer acceptQty;
	public Integer getAcceptQty() {
		return acceptQty;
	}

	public void setAcceptQty(Integer acceptQty) {
		this.acceptQty = acceptQty;
	}
	private String reasonForReturn;
	
	private Integer issueDetailBatchMapper;

	private Integer indentDetailId;
	
	private Integer statusId;
	   
	private String status;

	private Integer genericId;
	
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

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
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

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public String getReasonForReturn() {
		return reasonForReturn;
	}

	public void setReasonForReturn(String reasonForReturn) {
		this.reasonForReturn = reasonForReturn;
	}

	public Integer getIssueDetailBatchMapper() {
		return issueDetailBatchMapper;
	}

	public void setIssueDetailBatchMapper(Integer issueDetailBatchMapper) {
		this.issueDetailBatchMapper = issueDetailBatchMapper;
	}

	public Integer getIndentDetailId() {
		return indentDetailId;
	}

	public void setIndentDetailId(Integer indentDetailId) {
		this.indentDetailId = indentDetailId;
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

	
	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public ReturnAcceptanceItem(Integer id, Integer itemId, String itemName, String genericName, String batchNo, Date expiry,
			String uom, String uomUnit, Double rate, Integer returnQuantity, String reasonForReturn,
			Integer issueDetailBatchMapper, Integer genericId, boolean isThreeUOMApplicable, List<UomResponse> uomList) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.genericName = genericName;
		this.batchNo = batchNo;
		this.expiry = expiry;
		this.uom = uom;
		this.uomUnit = uomUnit;
		this.rate = rate;
		this.returnQuantity = returnQuantity;
		this.returnAcceptedQuantity = returnQuantity;
		this.reasonForReturn = reasonForReturn;
		this.issueDetailBatchMapper = issueDetailBatchMapper;
		this.genericId = genericId;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
		this.uomList = uomList;
	}
	public ReturnAcceptanceItem(Integer id, Integer itemId, String itemName, String genericName, String batchNo, Date expiry,
			String uom, String uomUnit, Double rate, Integer returnQuantity, String reasonForReturn,
			Integer issueDetailBatchMapper, Integer acceptQty, Integer genericId, boolean isThreeUOMApplicable) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.itemName = itemName;
		this.genericName = genericName;
		this.batchNo = batchNo;
		this.expiry = expiry;
		this.uom = uom;
		this.uomUnit = uomUnit;
		this.rate = rate;
		this.returnQuantity = returnQuantity;
		this.reasonForReturn = reasonForReturn;
		this.issueDetailBatchMapper = issueDetailBatchMapper;
		this.acceptQty = acceptQty;
		this.genericId = genericId;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
	}

	public Double getActualLendingPrice() {
		return actualLendingPrice;
	}

	public void setActualLendingPrice(Double actualLendingPrice) {
		this.actualLendingPrice = actualLendingPrice;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public List<BatchResponse> getBatch() {
		return batch;
	}

	public void setBatch(List<BatchResponse> batch) {
		this.batch = batch;
	}
}
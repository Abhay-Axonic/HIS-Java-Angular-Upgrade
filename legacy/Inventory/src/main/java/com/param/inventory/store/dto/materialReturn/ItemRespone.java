package com.param.inventory.store.dto.materialReturn;

import java.util.Date;
import java.util.List;

import com.param.global.dto.items.ItemsResponse;
import com.param.global.dto.items.UomResponse;

public class ItemRespone extends ItemsResponse {

	protected Integer id;
	protected Integer issueQty;
	protected Integer comsumedQty;
	protected Integer returnQty;
	protected Integer issuedetailsid;
	
	/* start code for resolved HIS-9214 Return billable consumption to have issues, consumed and returned columns */
	protected Integer totalComsumedQty;
	protected Integer totalReturnedQty;
	protected Integer totalTransitQty;
	protected Integer totalTransitInQty;
	public Integer getTotalTransitInQty() {
		return totalTransitInQty;
	}
	public void setTotalTransitInQty(Integer totalTransitInQty) {
		this.totalTransitInQty = totalTransitInQty;
	}
	public Integer getTotalTransitQty() {
		return totalTransitQty;
	}
	public void setTotalTransitQty(Integer totalTransitQty) {
		this.totalTransitQty = totalTransitQty;
	}
	public Integer getTotalComsumedQty() {
		return totalComsumedQty;
	}
	public void setTotalComsumedQty(Integer totalComsumedQty) {
		this.totalComsumedQty = totalComsumedQty;
	}
	public Integer getTotalReturnedQty() {
		return totalReturnedQty;
	}
	public void setTotalReturnedQty(Integer totalReturnedQty) {
		this.totalReturnedQty = totalReturnedQty;
	}
	/* end code for resolved HIS-9214 Return billable consumption to have issues, consumed and returned columns */	

	public Integer getIssuedetailsid() {
		return issuedetailsid;
	}

	public void setIssuedetailsid(Integer issuedetailsid) {
		this.issuedetailsid = issuedetailsid;
	}

	public Integer getReturnQty() {
		return returnQty;
	}

	public void setReturnQty(Integer returnQty) {
		this.returnQty = returnQty;
	}

	public Integer getComsumedQty() {
		return comsumedQty;
	}

	public void setComsumedQty(Integer comsumedQty) {
		this.comsumedQty = comsumedQty;
	}

	public Integer getIssueQty() {
		return issueQty;
	}

	public void setIssueQty(Integer issueQty) {
		this.issueQty = issueQty;
	}

	protected Integer returnReasonId;

	protected Integer returnQuantity;

	protected String returnReason;

	protected String batchNo;

	protected String genericName;

	protected Integer acceptQuantity;

	protected Integer rejectQuantity;
	// protected Integer issueQty;
	// protected Integer comsumedQty;
	protected Integer itemId;

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	protected Integer statusId;

	protected String status;
	protected String issueNo;
	protected Date issueDate;
	protected String issueno;
	protected Date issuedate;
	
    private boolean isThreeUOMApplicable;
    
    public boolean getIsThreeUOMApplicable() {
        return isThreeUOMApplicable;
    }

    public void setIsThreeUOMApplicable(boolean isThreeUOMApplicable) {
        this.isThreeUOMApplicable = isThreeUOMApplicable;
    }

	public String getIssueno() {
		return issueno;
	}

	public void setIssueno(String issueno) {
		this.issueno = issueno;
	}

	public Date getIssuedate() {
		return issuedate;
	}

	public void setIssuedate(Date issuedate) {
		this.issuedate = issuedate;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public ItemRespone() {
		super();
	}

	public ItemRespone(Integer itemId, String itemCode, String itemName, Long batchId, Integer uomTypeId,
			String uomType, Integer uomUnitId, String uomUnit, Date expiry, Long availableQuantity, Integer id,
			Integer returnReasonId, Integer returnQuantity, String returnReason, String batchNo, String genericName) {
		super(itemId, itemCode, itemName, batchId, uomTypeId, uomType, uomUnitId, uomUnit, expiry, availableQuantity);
		this.id = id;
		this.returnReasonId = returnReasonId;
		this.returnQuantity = returnQuantity;
		this.returnReason = returnReason;
		this.batchNo = batchNo;
		this.genericName = genericName;
	}

	public ItemRespone(Integer itemId, String itemCode, String itemName, Long batchId, Integer uomTypeId,
			String uomType, Integer uomUnitId, String uomUnit, Date expiry, Long availableQuantity, Integer id,
			Integer returnReasonId, Integer returnQuantity, String returnReason, String batchNo, String genericName,
			Integer issueQty, Integer comsumedQty, Integer itemId1) {
		super(itemId, itemCode, itemName, batchId, uomTypeId, uomType, uomUnitId, uomUnit, expiry, availableQuantity);
		this.id = id;
		this.returnReasonId = returnReasonId;
		this.returnQuantity = returnQuantity;
		this.returnReason = returnReason;
		this.batchNo = batchNo;
		this.genericName = genericName;
		this.issueQty = issueQty;
		this.comsumedQty = comsumedQty;
		this.itemId = itemId1;
	}
	/*
	public ItemRespone(Integer itemId, String itemCode, String itemName, Long batchId, Integer uomTypeId,
			String uomType, Integer uomUnitId, String uomUnit, Date expiry, Long availableQuantity, Integer id,
			Integer returnReasonId, Integer returnQuantity, String returnReason, String batchNo, String genericName,
			Integer issueQty, Integer comsumedQty, Integer itemId1, String issueNo, Date issueDate) {
		super(itemId, itemCode, itemName, batchId, uomTypeId, uomType, uomUnitId, uomUnit, expiry, availableQuantity);
		this.id = id;
		this.returnReasonId = returnReasonId;
		this.returnQuantity = returnQuantity;
		this.returnReason = returnReason;
		this.batchNo = batchNo;
		this.genericName = genericName;
		this.issueQty = issueQty;
		this.comsumedQty = comsumedQty;
		this.itemId = itemId1;
		this.issueNo =issueNo;
		this.issueDate = issueDate;
	}*/
	public ItemRespone(Integer itemId, String itemCode, String itemName, Long batchId, Integer uomTypeId,
			String uomType, Integer uomUnitId, String uomUnit, Date expiry, Long availableQuantity, Integer id,
			Integer returnReasonId, Integer returnQuantity, String returnReason, String batchNo, String genericName,
			Integer issueQty, Integer comsumedQty, Integer itemId1, String issueno, Date issuedate, boolean isThreeUOMApplicable, List<UomResponse> uom) {
		super(itemId, itemCode, itemName, batchId, uomTypeId, uomType, uomUnitId, uomUnit, expiry, availableQuantity);
		this.id = id;
		this.returnReasonId = returnReasonId;
		this.returnQuantity = returnQuantity;
		this.returnReason = returnReason;
		this.batchNo = batchNo;
		this.genericName = genericName;
		this.issueQty = issueQty;
		this.comsumedQty = comsumedQty;
		this.itemcode=itemCode;
		this.itemId = itemId1;
		this.issueNo =issueno;
		this.issueDate = issuedate;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
		this.uom = uom;
	}
	public ItemRespone(Integer itemId, String itemCode, String itemName, Long batchId, Integer uomTypeId,
			String uomType, Integer uomUnitId, String uomUnit, Date expiry, Long availableQuantity, Integer id,
			Integer returnReasonId, Integer returnQuantity, String returnReason, String batchNo, String genericName,
			Integer acceptQuantity, Integer rejectQuantity, Integer statusId, String status) {
		super(itemId, itemCode, itemName, batchId, uomTypeId, uomType, uomUnitId, uomUnit, expiry, availableQuantity);
		this.id = id;
		this.returnReasonId = returnReasonId;
		this.returnQuantity = returnQuantity;
		this.returnReason = returnReason;
		this.batchNo = batchNo;
		this.genericName = genericName;
		this.acceptQuantity = acceptQuantity;
		this.rejectQuantity = rejectQuantity;
		this.statusId = statusId;
		this.status = status;
		
	}
	
	public ItemRespone(Integer itemId, String itemCode, String itemName, Long batchId, Integer uomTypeId,
			String uomType, Integer uomUnitId, String uomUnit, Date expiry, Long availableQuantity, Integer id,
			Integer returnReasonId, Integer returnQuantity, String returnReason, String batchNo, String genericName,
			Integer acceptQuantity, Integer rejectQuantity, Integer statusId, String status, String issueNo, Date issueDate, boolean isThreeUOMApplicable, List<UomResponse> uom,Integer totalReturnedQty) {
		super(itemId, itemCode, itemName, batchId, uomTypeId, uomType, uomUnitId, uomUnit, expiry, availableQuantity);
		this.id = id;
		this.returnReasonId = returnReasonId;
		this.returnQuantity = returnQuantity;
		this.returnReason = returnReason;
		this.batchNo = batchNo;
		this.genericName = genericName;
		this.acceptQuantity = acceptQuantity;
		this.rejectQuantity = rejectQuantity;
		this.statusId = statusId;
		this.status = status;
		this.issueNo =issueNo;
		this.issueDate = issueDate;
		this.isThreeUOMApplicable = isThreeUOMApplicable;
		this.uom = uom;
		this.totalReturnedQty=totalReturnedQty;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReturnReasonId() {
		return returnReasonId;
	}

	public void setReturnReasonId(Integer returnReasonId) {
		this.returnReasonId = returnReasonId;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public Integer getAcceptQuantity() {
		return acceptQuantity;
	}

	public void setAcceptQuantity(Integer acceptQuantity) {
		this.acceptQuantity = acceptQuantity;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
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

	
}

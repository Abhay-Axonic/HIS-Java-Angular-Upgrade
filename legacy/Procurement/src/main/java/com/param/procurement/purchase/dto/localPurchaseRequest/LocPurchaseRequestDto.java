package com.param.procurement.purchase.dto.localPurchaseRequest;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.param.procurement.purchase.dto.localPurchaseRequest.ItemRequest;
import com.param.service.global.model.CookieObjectDto;

public class LocPurchaseRequestDto extends CookieObjectDto{
	
	private Long id;

	private Integer organisationId;

	private Integer unitId;

	private Integer screenId;

	private Integer storeId;
	private Integer requestingStoreId;

	public Integer getRequestingStoreId() {
		return requestingStoreId;
	}

	public void setRequestingStoreId(Integer requestingStoreId) {
		this.requestingStoreId = requestingStoreId;
	}


	private Integer assetTypeId;

	private Integer prTypeId;

	private Integer statusId;

	private Integer approvalStatusId;

	private Integer priorityId;

	private String purchaseNo;

	private Integer cancelReasonId;

	private String cancelNote;

	private String remark;

	private Boolean isActive;
	
	private Date fromDate;
	
	private Date toDate;
	
	private Boolean isDeleted;

	private Long addedBy;

	private Date addedDate;

	private Long updatedBy;

	private Date updatedDate;
	
	private List<ItemRequest> items;
	
	private String validationMessage;
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getScreenId() {
		return screenId;
	}

	public void setScreenId(Integer screenId) {
		this.screenId = screenId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public Integer getPrTypeId() {
		return prTypeId;
	}

	public void setPrTypeId(Integer prTypeId) {
		this.prTypeId = prTypeId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public String getPurchaseNo() {
		return purchaseNo;
	}

	public void setPurchaseNo(String purchaseNo) {
		this.purchaseNo = purchaseNo;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Long getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(Long addedBy) {
		this.addedBy = addedBy;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public List<ItemRequest> getItems() {
		return items;
	}

	public void setItems(List<ItemRequest> items) {
		this.items = items;
	}

	public String getValidationMessage() {
		return validationMessage;
	}

	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}

	public Boolean ValidateSave() {
	    	Boolean result = true;
	    	this.validationMessage = "Please fill below required fields. | ";
	    	if(this.storeId == null || this.storeId < 1 ) {
	    		result = false;
	    		this.validationMessage += "storeId |";
	    	}
	    	
	    	if(this.priorityId == null || this.priorityId < 1 ) {
	    		result = false;
	    		this.validationMessage += "priorityId |";
	    	}

	    	if(this.assetTypeId == null || this.assetTypeId < 1 ) {
	    		result = false;
	    		this.validationMessage += "assetTypeId |";
	    	}

	    	if(this.prTypeId == null || this.prTypeId < 1 ) {
	    		result = false;
	    		this.validationMessage += "prTypeId |";
	    	}

	    	if(this.statusId == null || this.statusId < 1 ) {
	    		result = false;
	    		this.validationMessage += "statusId |";
	    	}

	    	if(this.approvalStatusId == null || this.approvalStatusId < 1 ) {
	    		result = false;
	    		this.validationMessage += "approvalStatusId |";
	    	}
	    	if(this.items == null || this.items.isEmpty()) {
	    		result = false;
	    		this.validationMessage += "items |";
	    	}
	    	else if(this.items!=null && !this.items.isEmpty()) {
	    		
	    			//result = false;
	    			for(ItemRequest item : items) {
	    				item.ValidateSave(this.assetTypeId, this.prTypeId);
	    			}
	    		}
	    	
	    	return result;
	    }
	    

	    public Boolean ValidateUpdate() {
	    	Boolean result = true;
	    	this.validationMessage = "Please fill below required fields. | ";
	    	if(this.id == null ) {
	    		result = false;
	    		this.validationMessage += "id |";
	    	}
	    	if(this.priorityId == null || this.priorityId < 1 ) {
	    		result = false;
	    		this.validationMessage += "priorityId |";
	    	}
	    	
	    	if(this.items == null || this.items.isEmpty()) {
	    		result = false;
	    		this.validationMessage += "items |";
	    	}
	    	else if(this.items!=null && !this.items.isEmpty()) {
	    		for(ItemRequest item : items) {
	    				item.validateUpdate(this.assetTypeId, this.prTypeId);
	    			}
	    		}

	    	return result;
	    }
	

}

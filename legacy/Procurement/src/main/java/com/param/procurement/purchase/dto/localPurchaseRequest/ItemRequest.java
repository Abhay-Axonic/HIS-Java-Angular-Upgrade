package com.param.procurement.purchase.dto.localPurchaseRequest;

import java.util.Date;
import java.util.List;

import com.param.common.constants.Constants;
import com.param.entity.model.master.RejectReason;

public class ItemRequest {
	
	private Integer id;
	
	private Integer itemId;
	
	private Integer uomTypeId;
	
	private Integer uomUnitId;
	
	private Integer quantity;
	
	private Integer indentQuantity;
	
	private Double cop ;
	
	private Double amountCop;
	
	private String specification;
	
	private Integer statusId;
	
	private Integer approvedQuantity;
	
	private Date approvedDate;
	
	private Integer rejectQuantity;
	
	private Integer rejectReasonId;
	
	private String rejectNote;
	
	private List<Integer> indentId;
	private  Integer indentDetailsId;
	
	public Integer getIndentDetailsId() {
		return indentDetailsId;
	}

	public void setIndentDetailsId(Integer indentDetailsId) {
		this.indentDetailsId = indentDetailsId;
	}

	private String validationMessage;
	private Integer pendingQuantity;
	public Integer getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	public ItemRequest() {
		super();
	}

	
	public ItemRequest(Integer id, Integer itemId, Integer uomTypeId, Integer uomUnitId, Integer quantity,
			Integer indentQuantity, Double cop, Double amountCop, String specification, Integer statusId,
			Integer approvedQuantity, Date approvedDate, Integer rejectQuantity, Integer rejectReasonId, String rejectNote,
			List<Integer> indentId) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.uomTypeId = uomTypeId;
		this.uomUnitId = uomUnitId;
		this.quantity = quantity;
		this.indentQuantity = indentQuantity;
		this.cop = cop;
		this.amountCop = amountCop;
		this.specification = specification;
		this.statusId = statusId;
		this.approvedQuantity = approvedQuantity;
		this.approvedDate = approvedDate;
		this.rejectQuantity = rejectQuantity;
		this.rejectReasonId = rejectReasonId;
		this.rejectNote = rejectNote;
		this.indentId = indentId;
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

	public Integer getUomTypeId() {
		return uomTypeId;
	}

	public void setUomTypeId(Integer uomTypeId) {
		this.uomTypeId = uomTypeId;
	}

	public Integer getUomUnitId() {
		return uomUnitId;
	}

	public void setUomUnitId(Integer uomUnitId) {
		this.uomUnitId = uomUnitId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}


	public Integer getIndentQuantity() {
		return indentQuantity;
	}


	public void setIndentQuantity(Integer indentQuantity) {
		this.indentQuantity = indentQuantity;
	}


	public Double getCop() {
		return cop;
	}


	public void setCop(Double cop) {
		this.cop = cop;
	}


	public Double getAmountCop() {
		return amountCop;
	}


	public void setAmountCop(Double amountCop) {
		this.amountCop = amountCop;
	}


	public String getSpecification() {
		return specification;
	}


	public void setSpecification(String specification) {
		this.specification = specification;
	}


	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}


	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}


	public Date getApprovedDate() {
		return approvedDate;
	}


	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

    public Integer getRejectQuantity() {
		return rejectQuantity;
	}


	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
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


	public List<Integer> getIndentId() {
		return indentId;
	}


	public void setIndentId(List<Integer> indentId) {
		this.indentId = indentId;
	}
	
	public String getValidationMessage() {
		return validationMessage;
	}


	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}


	public Boolean ValidateSave(Integer assetTypeId, Integer prTypeId) {
    	Boolean result = true;
    	this.validationMessage = "Please fill below required fields. | ";
    	if(this.itemId == null || this.itemId < 1 ) {
    		result = false;
    		this.validationMessage += "itemId |";
    	}
    	
    	if(this.quantity == null || this.quantity < 1 ) {
    		result = false;
    		this.validationMessage += "quantity |";
    	}
    	
    	if(this.cop == null || this.cop < 1 ) {
    		result = false;
    		this.validationMessage += "cop |";
    	}   	
    	    	
    	if(this.amountCop == null || this.amountCop < 1 ) {
    		result = false;
    		this.validationMessage += "amountCop |";
    	}

    	if(this.statusId == null || this.statusId < 1 ) {
    		result = false;
    		this.validationMessage += "statusId |";
    	}
    	if(assetTypeId.equals(Constants.getConsumableId())){
    		if(this.uomTypeId == null || this.uomTypeId < 1) {
    			result = false;
    			this.validationMessage += "uomTypeId |";
        	}
    		if(this.uomUnitId == null || this.uomUnitId < 1) {
    			result = false;
    			this.validationMessage += "uomUnitId |";
        	}
    	}
    	if(prTypeId.equals(com.param.common.constants.RequestType.AgainstIndent.getRequestType())) {
    		result = false;
    		if(this.indentId == null || this.indentId.isEmpty()) {
    			this.validationMessage += "indentId |";
    		}
    	}

    	return result;
    }
	
	public Boolean validateUpdate(Integer assetTypeId, Integer prTypeId) {
		Boolean result = true;
    	this.validationMessage = "Please fill below required fields. | ";
    	
    	if(this.quantity == null || this.quantity < 1 ) {
    		result = false;
    		this.validationMessage += "quantity |";
    	}
    	
    	if(this.statusId == null || this.statusId < 1 ) {
    		result = false;
    		this.validationMessage += "statusId |";
    	}
    	
    	//for adding new item in same PR
    	if(this.id == null || this.id < 1) {    		
    		if(this.itemId == null || this.itemId < 1 ) {
    			ValidateSave(assetTypeId,prTypeId);
    		}
    		else {
    			result = false;
    			this.validationMessage += "id |";
    		}
    			
    	}
    	
    	return result;
	}
	
	
}
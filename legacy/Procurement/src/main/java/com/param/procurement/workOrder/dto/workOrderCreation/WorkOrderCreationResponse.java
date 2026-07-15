package com.param.procurement.workOrder.dto.workOrderCreation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.param.global.dto.global.TaxDropDown;
import com.param.procurement.workOrder.dto.workOrderQuotationRequest.WorkOrderQRDetailResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkOrderCreationResponse extends WorkOrderCreationRequest{
	
	private String approvedBy;

	private Date approvedOn;
	
	private String approvalStatus;
	
	private String contractType;

	private String requestType;

	private String status;

	private String store;

	private String vendor;
	
	private String woCreationNumber;
	
	private Date woCreationDate;
	
	private String addedBy;
	
	private List<WorkOrderCreationDetailResponse> details;
	
	protected List<WOCreationResponse> POCreationDetailList;
    
    protected List<TaxDropDown> taxList;

	

	public WorkOrderCreationResponse() {
		super();
	}
	
	
	public WorkOrderCreationResponse(Integer id, Integer approvalStatusId, Double netAmount, String remark,
			Integer contractTypeId, Integer requestTypeId, Integer statusId, Integer storeId, Double totalAmount,
			Double totalDiscount, Double totalTax, Integer vendorId, Integer workOrderRequestId, String workOrderNo,
			Integer workOrderQuotationId,String approvedBy, Date approvedOn, String approvalStatus, String contractType,
			String requestType, String status, String store, String vendor,String woCreationNumber,Date woCreationDate,String addedBy) {
		super( id,  approvalStatusId,  netAmount,  remark,
				 contractTypeId,  requestTypeId,  statusId,  storeId,  totalAmount,
				 totalDiscount,  totalTax,  vendorId,  workOrderRequestId,  workOrderNo,
				 workOrderQuotationId);
		this.approvedBy = approvedBy;
		this.approvedOn = approvedOn;
		this.approvalStatus = approvalStatus;
		this.contractType = contractType;
		this.requestType = requestType;
		this.status = status;
		this.store = store;
		this.vendor = vendor;
		this.woCreationNumber = woCreationNumber;
		this.woCreationDate = woCreationDate;
		this.addedBy = addedBy;
		
	}



	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	
	public String getWoCreationNumber() {
		return woCreationNumber;
	}
	
	public void setWoCreationNumber(String woCreationNumber) {
		this.woCreationNumber = woCreationNumber;
	}
	
	public Date getWoCreationDate() {
		return woCreationDate;
	}
	
	public void setWoCreationDate(Date woCreationDate) {
		this.woCreationDate = woCreationDate;
	}
	
	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public List<WorkOrderCreationDetailResponse> getDetails() {
		return details;
	}

	public void setDetails(List<WorkOrderCreationDetailResponse> details) {
		this.details = details;
	}
	
	public void addItems(WorkOrderCreationDetailResponse item) {
		if (this.details == null)
			this.details = new ArrayList<WorkOrderCreationDetailResponse>();

		this.details.add(item);
	}


	public List<WOCreationResponse> getPOCreationDetailList() {
		return POCreationDetailList;
	}


	public void setPOCreationDetailList(List<WOCreationResponse> pOCreationDetailList) {
		POCreationDetailList = pOCreationDetailList;
	}


	public List<TaxDropDown> getTaxList() {
		return taxList;
	}


	public void setTaxList(List<TaxDropDown> taxList) {
		this.taxList = taxList;
	}
	

}

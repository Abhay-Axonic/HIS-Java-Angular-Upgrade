package com.param.linenLaundry.linenIndent.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.linenLaundry.linenIssue.dto.Carrier;

@JsonInclude(Include.NON_NULL)
public class LinenResponse {
	
	protected Integer id;
	
	protected Date indentDate;
	
	protected String indentNo;
	
	protected Date issueDate;
	
	protected String issueNo;
	
	protected Integer fromStoreId;
	
	protected String fromStoreName;
	
	protected Integer toStoreId;
	
	protected Integer priorityId;
	
	protected String priorityName;
	
	protected String toStoreName;
	
	protected String requestedBy;
	
	protected Integer requestedById;
	
	protected String authorizedBy;
	
	protected Date authorizedDate;
	
	protected String remark;
	
	protected Integer statusId;
	
	protected String status;
	
	protected Integer approvalStatusId;
	
	protected String approvalStatus;
	
	protected Integer carrierId;
	
	protected String carrierName;
	
	protected List<ItemResponse> items;
	
	protected List<Carrier> carrier;
	
    protected String returnNo;
	
    protected Date returnDate;
	
    protected String returnBy;
    
    protected Integer returnById;
	
	public LinenResponse() {
		super();
	}

	public LinenResponse(Integer id, Date indentDate, String indentNo, Integer fromStoreId, String fromStoreName,
			Integer toStoreId, Integer priorityId, String priorityName, String toStoreName, String requestedBy,
			Integer requestedById, String remark, Integer statusId, String status) {
		super();
		this.id = id;
		this.indentDate = indentDate;
		this.indentNo = indentNo;
		this.fromStoreId = fromStoreId;
		this.fromStoreName = fromStoreName;
		this.toStoreId = toStoreId;
		this.priorityId = priorityId;
		this.priorityName = priorityName;
		this.toStoreName = toStoreName;
		this.requestedBy = requestedBy;
		this.requestedById = requestedById;
		this.remark = remark;
		this.statusId = statusId;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getIndentDate() {
		return indentDate;
	}

	public void setIndentDate(Date indentDate) {
		this.indentDate = indentDate;
	}

	public String getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(String indentNo) {
		this.indentNo = indentNo;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getAuthorizedBy() {
		return authorizedBy;
	}

	public void setAuthorizedBy(String authorizedBy) {
		this.authorizedBy = authorizedBy;
	}

	public Date getAuthorizedDate() {
		return authorizedDate;
	}

	public void setAuthorizedDate(Date authorizedDate) {
		this.authorizedDate = authorizedDate;
	}

	public List<ItemResponse> getItems() {
		return items;
	}

	public void setItems(List<ItemResponse> items) {
		this.items = items;
	}	
	
	public Integer getFromStoreId() {
		return fromStoreId;
	}

	public void setFromStoreId(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
	}

	public String getFromStoreName() {
		return fromStoreName;
	}

	public void setFromStoreName(String fromStoreName) {
		this.fromStoreName = fromStoreName;
	}

	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public String getPriorityName() {
		return priorityName;
	}

	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}

	public String getToStoreName() {
		return toStoreName;
	}

	public void setToStoreName(String toStoreName) {
		this.toStoreName = toStoreName;
	}

	public Integer getRequestedById() {
		return requestedById;
	}

	public void setRequestedById(Integer requestedById) {
		this.requestedById = requestedById;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public List<Carrier> getCarrier() {
		return carrier;
	}

	public void setCarrier(List<Carrier> carrier) {
		this.carrier = carrier;
	}

	public Integer getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Integer carrierId) {
		this.carrierId = carrierId;
	}

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}
	
	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public String getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getReturnBy() {
		return returnBy;
	}

	public void setReturnBy(String returnBy) {
		this.returnBy = returnBy;
	}

	public Integer getReturnById() {
		return returnById;
	}

	public void setReturnById(Integer returnById) {
		this.returnById = returnById;
	}
	
	public LinenResponse(Integer id, Integer fromStoreId, String fromStoreName, Integer toStoreId, String toStoreName,
			String remark, Integer statusId, String status, String returnNo, java.util.Date returnDate, String returnBy,
			Integer returnById) {
		super();
		this.id = id;
		this.fromStoreId = fromStoreId;
		this.fromStoreName = fromStoreName;
		this.toStoreId = toStoreId;
		this.toStoreName = toStoreName;
		this.remark = remark;
		this.statusId = statusId;
		this.status = status;
		this.returnNo = returnNo;
		this.returnDate = returnDate;
		this.returnBy = returnBy;
		this.returnById = returnById;
	}

	public LinenResponse(Integer id, Integer fromStoreId, String fromStoreName, Integer toStoreId, String toStoreName,
			String remark, Integer statusId, String status, Integer approvalStatusId, String approvalStatus,
			String returnNo, Date returnDate, String returnBy, Integer returnById, Integer carrierId, String carrierName) {
		super();
		this.id = id;
		this.fromStoreId = fromStoreId;
		this.fromStoreName = fromStoreName;
		this.toStoreId = toStoreId;
		this.toStoreName = toStoreName;
		this.remark = remark;
		this.statusId = statusId;
		this.status = status;
		this.approvalStatusId = approvalStatusId;
		this.approvalStatus = approvalStatus;
		this.returnNo = returnNo;
		this.returnDate = returnDate;
		this.returnBy = returnBy;
		this.returnById = returnById;
		this.carrierId = carrierId;
		this.carrierName = carrierName;
	}

	public LinenResponse(Integer id, java.util.Date issueDate, String issueNo, Integer fromStoreId,
			String fromStoreName, Integer toStoreId, Integer priorityId, String priorityName, String toStoreName,
			String remark, Integer statusId, String status) {
		super();
		this.id = id;
		this.issueDate = issueDate;
		this.issueNo = issueNo;
		this.fromStoreId = fromStoreId;
		this.fromStoreName = fromStoreName;
		this.toStoreId = toStoreId;
		this.priorityId = priorityId;
		this.priorityName = priorityName;
		this.toStoreName = toStoreName;
		this.remark = remark;
		this.statusId = statusId;
		this.status = status;
	}
	
	
	public LinenResponse(Integer id, Integer fromStoreId, String fromStoreName, Integer toStoreId, String toStoreName,
			Integer statusId, String status, String returnNo, java.util.Date returnDate) {
		super();
		this.id = id;
		this.fromStoreId = fromStoreId;
		this.fromStoreName = fromStoreName;
		this.toStoreId = toStoreId;
		this.toStoreName = toStoreName;
		this.statusId = statusId;
		this.status = status;
		this.returnNo = returnNo;
		this.returnDate = returnDate;
	}
	
	public void addItems(ItemResponse item) {
		if(this.items == null)
			this.items = new ArrayList<ItemResponse>();
		
		this.items.add(item);
	}	
}
package com.param.procurement.quotation.dto.technicalApproval;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class TechnicalApprovalRequestDto extends CookieObjectDto{
	
	private Integer id;

	private Date fromDate;

	private Date toDate;

	private String qrNumber;

	private Integer storeId;

	private Integer statusId;
	
	private Integer approvalStatusId;
	
	private Integer qrId;
	
    private Integer cancelReasonId;
	
	private String cancelNote;
	
	private List<TechnicalApprovalDetailsResponse> items;
	
	
	public TechnicalApprovalRequestDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getQrNumber() {
		return qrNumber;
	}

	public void setQrNumber(String qrNumber) {
		this.qrNumber = qrNumber;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getQrId() {
		return qrId;
	}

	public void setQrId(Integer qrId) {
		this.qrId = qrId;
	}
	
	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
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

	public List<TechnicalApprovalDetailsResponse> getItems() {
		return items;
	}

	public void setItems(List<TechnicalApprovalDetailsResponse> items) {
		this.items = items;
	}

}

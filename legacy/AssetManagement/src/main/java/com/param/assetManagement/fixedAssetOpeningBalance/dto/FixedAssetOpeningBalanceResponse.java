package com.param.assetManagement.fixedAssetOpeningBalance.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class FixedAssetOpeningBalanceResponse {

	protected Integer id;

	protected Date opbDate;

	protected String opbNo;

	protected String storeName;

	protected String requestedBy;

	protected Integer statusId;

	protected String status;

	public FixedAssetOpeningBalanceResponse() {
		super();
	}

	public FixedAssetOpeningBalanceResponse(Integer id, Date opbDate, String opbNo, String storeName, String requestedBy,
			Integer statusId, String status) {
		super();
		this.id = id;
		this.opbDate = opbDate;
		this.opbNo = opbNo;
		this.storeName = storeName;
		this.requestedBy = requestedBy;
		this.statusId = statusId;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getOpbDate() {
		return opbDate;
	}

	public void setOpbDate(Date opbDate) {
		this.opbDate = opbDate;
	}

	public String getOpbNo() {
		return opbNo;
	}

	public void setOpbNo(String opbNo) {
		this.opbNo = opbNo;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
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
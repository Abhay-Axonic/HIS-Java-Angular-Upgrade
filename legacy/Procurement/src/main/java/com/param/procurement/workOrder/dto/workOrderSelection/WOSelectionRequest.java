package com.param.procurement.workOrder.dto.workOrderSelection;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class WOSelectionRequest extends CookieObjectDto{
	
	private Integer id;
	
	private Date fromDate;
	
	private Date toDate;

	private Integer quotationRequestId;

	private Integer statusId;
	
	private Integer storeId;
	
	private String qrNo;
	
	private List<WOSelectionDetailRequest> items;

	public WOSelectionRequest() {
		super();
	}

	public WOSelectionRequest(Integer id, Integer statusId) {
		super();
		this.id = id;
		this.statusId = statusId;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuotationRequestId() {
		return quotationRequestId;
	}

	public void setQuotationRequestId(Integer quotationRequestId) {
		this.quotationRequestId = quotationRequestId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public List<WOSelectionDetailRequest> getItems() {
		return items;
	}

	public void setItems(List<WOSelectionDetailRequest> items) {
		this.items = items;
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

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getQrNo() {
		return qrNo;
	}

	public void setQrNo(String qrNo) {
		this.qrNo = qrNo;
	}
	
	
	
}

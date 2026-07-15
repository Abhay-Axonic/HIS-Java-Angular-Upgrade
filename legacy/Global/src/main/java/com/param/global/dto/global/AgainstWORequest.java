package com.param.global.dto.global;

import java.util.Date;
import java.util.List;


public class AgainstWORequest {
	
private Integer id;
	
	private Date fromDate;

	private Date toDate;
	
	private Integer contractTypeId;
	
	private Integer requestTypeId;

	private Integer statusId;
	
	private Integer storeId;
	
	private Integer vendorId;
	
	private String wrNo;
	
	private List<AgainstWORequestDetail> items;
	
	private Integer screenId;
	
	
	
	public Integer getScreenId() {
		return screenId;
	}

	public void setScreenId(Integer screenId) {
		this.screenId = screenId;
	}

	public String getWrNo() {
		return wrNo;
	}

	public void setWrNo(String wrNo) {
		this.wrNo = wrNo;
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

	public Integer getContractTypeId() {
		return contractTypeId;
	}

	public void setContractTypeId(Integer contractTypeId) {
		this.contractTypeId = contractTypeId;
	}

	public Integer getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(Integer requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public List<AgainstWORequestDetail> getItems() {
		return items;
	}

	public void setItems(List<AgainstWORequestDetail> items) {
		this.items = items;
	}
	
	public AgainstWORequest()
	{
		super();
	}

}

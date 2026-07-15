package com.param.CSSD.dto;

import java.util.Date;
import java.util.List;

public class CssdOpeningBalanceRequest {
	   


	private Integer id;

	private Date fromDate;

	private Date toDate;

	private Integer storeId;

	private Integer statusId;


	private List<Integer> openingBalanceId;
	
	private List<Integer> openingBalanceImplantId;
	
	private List<Integer> openingBalanceImplantDetailId;
	
	private List<Integer> openingBalanceDetailId;

	private Integer cancelReasonId;

	private String cancelNote;
	
	private String remark;
	

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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public List<Integer> getOpeningBalanceId() {
		return openingBalanceId;
	}

	public void setOpeningBalanceId(List<Integer> openingBalanceId) {
		this.openingBalanceId = openingBalanceId;
	}

	public List<Integer> getOpeningBalanceDetailId() {
		return openingBalanceDetailId;
	}

	public void setOpeningBalanceDetailId(List<Integer> openingBalanceDetailId) {
		this.openingBalanceDetailId = openingBalanceDetailId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<Integer> getOpeningBalanceImplantId() {
		return openingBalanceImplantId;
	}

	public void setOpeningBalanceImplantId(List<Integer> openingBalanceImplantId) {
		this.openingBalanceImplantId = openingBalanceImplantId;
	}
	public List<Integer> getOpeningBalanceImplantDetailId() {
		return openingBalanceImplantDetailId;
	}

	public void setOpeningBalanceImplantDetailId(List<Integer> openingBalanceImplantDetailId) {
		this.openingBalanceImplantDetailId = openingBalanceImplantDetailId;
	}


}

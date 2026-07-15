package com.param.procurement.loanInOut.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.service.global.model.CookieObjectDto;

@JsonInclude(Include.NON_NULL)
public class LoanInOutRequest extends CookieObjectDto {
	
	private Integer id;

	private List<Integer> loanInId;

	private List<Integer> loanOutId;

	private List<Integer> loanInOutId;
	
	private List<Integer> loanOutReceiveId;

	private Date fromDate;

	private Date toDate;

	private Date loanInDate;
	
	private Date loanOutDate;

	private String loanInNo;

	private Integer loanoutId;

	private Integer storeId;

	private Integer vendorId;

	private String vendorName;
	
	private Integer assetTypeId;

	private Integer inWardById;
	
	private String inWardBy;

	private Integer outWardById;
	
	private String outWardBy;

	private Integer statusId;

	private String inwardNo;

	private String remark;

	private Double totalAmount;

	private String dcNo;

	private Date dcDate;

	private Integer cancelReasonId;

	private String cancelNote;
	
	private String searchKeyword;

	private List<LoanInOutRequestItem> items;

	public List<Integer> getLoanInId() {
		return loanInId;
	}

	public void setLoanInId(List<Integer> loanInId) {
		this.loanInId = loanInId;
	}

	public List<Integer> getLoanOutId() {
		return loanOutId;
	}

	public void setLoanOutId(List<Integer> loanOutId) {
		this.loanOutId = loanOutId;
	}

	public List<Integer> getLoanInOutId() {
		return loanInOutId;
	}

	public void setLoanInOutId(List<Integer> loanInOutId) {
		this.loanInOutId = loanInOutId;
	}

	public List<Integer> getLoanOutReceiveId() {
		return loanOutReceiveId;
	}

	public void setLoanOutReceiveId(List<Integer> loanOutReceiveId) {
		this.loanOutReceiveId = loanOutReceiveId;
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
	
	public Integer getLoanoutId() {
		return loanoutId;
	}

	public void setLoanoutId(Integer loanoutId) {
		this.loanoutId = loanoutId;
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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public Integer getInWardById() {
		return inWardById;
	}

	public void setInWardById(Integer inWardById) {
		this.inWardById = inWardById;
	}

	public String getInwardNo() {
		return inwardNo;
	}

	public void setInwardNo(String inwardNo) {
		this.inwardNo = inwardNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getDcNo() {
		return dcNo;
	}

	public void setDcNo(String dcNo) {
		this.dcNo = dcNo;
	}

	public Date getDcDate() {
		return dcDate;
	}

	public void setDcDate(Date dcDate) {
		this.dcDate = dcDate;
	}

	public List<LoanInOutRequestItem> getItems() {
		return items;
	}

	public void setItems(List<LoanInOutRequestItem> items) {
		this.items = items;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOutWardById() {
		return outWardById;
	}

	public void setOutWardById(Integer outWardById) {
		this.outWardById = outWardById;
	}

	public Date getLoanInDate() {
		return loanInDate;
	}

	public void setLoanInDate(Date loanInDate) {
		this.loanInDate = loanInDate;
	}

	public Date getLoanOutDate() {
		return loanOutDate;
	}

	public void setLoanOutDate(Date loanOutDate) {
		this.loanOutDate = loanOutDate;
	}

	public String getLoanInNo() {
		return loanInNo;
	}

	public void setLoanInNo(String loanInNo) {
		this.loanInNo = loanInNo;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getInWardBy() {
		return inWardBy;
	}

	public void setInWardBy(String inWardBy) {
		this.inWardBy = inWardBy;
	}

	public String getOutWardBy() {
		return outWardBy;
	}

	public void setOutWardBy(String outWardBy) {
		this.outWardBy = outWardBy;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

}
package com.param.procurement.loanInOut.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class LoanInOutResponse {
	private Integer id;

	private String loanInNo;

	private String loanOutNo;

	private String loanOutReceiveNo;

	private Date loanInDate;

	private Date loanOutDate;

	private Date loanOutReceiveDate;

	private String dcNo;

	private Date dcDate;

	private String loanInOutNo;

	private Date loanInOutDate;

	private String store;

	private Integer storeId;

	private String vendor;

	private Integer vendorId;

	private String assetType;

	private Integer assetTypeId;

	private String remark;

	private String inwardBy;

	private Integer inwardById;

	private String outwardBy;

	private Integer outwardById;

	private String status;

	private Integer statusId;

	private Double totalAmount;

	private List<LoanInOutResponseItem> items;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLoanInNo() {
		return loanInNo;
	}

	public void setLoanInNo(String loanInNo) {
		this.loanInNo = loanInNo;
	}

	public String getLoanOutNo() {
		return loanOutNo;
	}

	public void setLoanOutNo(String loanOutNo) {
		this.loanOutNo = loanOutNo;
	}

	public String getLoanOutReceiveNo() {
		return loanOutReceiveNo;
	}

	public void setLoanOutReceiveNo(String loanOutReceiveNo) {
		this.loanOutReceiveNo = loanOutReceiveNo;
	}

	public Date getLoanOutDate() {
		return loanOutDate;
	}

	public void setLoanOutDate(Date loanOutDate) {
		this.loanOutDate = loanOutDate;
	}

	public Date getLoanInDate() {
		return loanInDate;
	}

	public void setLoanInDate(Date loanInDate) {
		this.loanInDate = loanInDate;
	}

	public Date getLoanOutReceiveDate() {
		return loanOutReceiveDate;
	}

	public void setLoanOutReceiveDate(Date loanOutReceiveDate) {
		this.loanOutReceiveDate = loanOutReceiveDate;
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

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getInwardBy() {
		return inwardBy;
	}

	public void setInwardBy(String inwardBy) {
		this.inwardBy = inwardBy;
	}

	public Integer getInwardById() {
		return inwardById;
	}

	public void setInwardById(Integer inwardById) {
		this.inwardById = inwardById;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getLoanInOutNo() {
		return loanInOutNo;
	}

	public void setLoanInOutNo(String loanInOutNo) {
		this.loanInOutNo = loanInOutNo;
	}

	public Date getLoanInOutDate() {
		return loanInOutDate;
	}

	public void setLoanInOutDate(Date loanInOutDate) {
		this.loanInOutDate = loanInOutDate;
	}

	public String getOutwardBy() {
		return outwardBy;
	}

	public void setOutwardBy(String outwardBy) {
		this.outwardBy = outwardBy;
	}

	public Integer getOutwardById() {
		return outwardById;
	}

	public void setOutwardById(Integer outwardById) {
		this.outwardById = outwardById;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<LoanInOutResponseItem> getItems() {
		return items;
	}

	public void setItems(List<LoanInOutResponseItem> items) {
		this.items = items;
	}

	public LoanInOutResponse() {
		super();
	}

	public LoanInOutResponse(Integer id, String loanInNo, Date loanInDate, String store, Integer storeId, String vendor,
			Integer vendorId, String assetType, Integer assetTypeId, String remark, String inwardBy, Integer inwardById,
			String status, Integer statusId) {
		super();
		this.id = id;
		this.loanInNo = loanInNo;
		this.loanInDate = loanInDate;
		this.store = store;
		this.storeId = storeId;
		this.vendor = vendor;
		this.vendorId = vendorId;
		this.assetType = assetType;
		this.assetTypeId = assetTypeId;
		this.remark = remark;
		this.inwardBy = inwardBy;
		this.inwardById = inwardById;
		this.status = status;
		this.statusId = statusId;
	}

	public LoanInOutResponse(Integer id, String loanOutNo, Date loanOutDate, String store, Integer storeId,
			String vendor, Integer vendorId, String assetType, Integer assetTypeId, String remark, String outwardBy,
			Integer outwardById, String status, Integer statusId, Double totalAmount) {
		super();
		this.loanOutNo = loanOutNo;
		this.loanOutDate = loanOutDate;
		this.store = store;
		this.storeId = storeId;
		this.vendor = vendor;
		this.vendorId = vendorId;
		this.assetType = assetType;
		this.assetTypeId = assetTypeId;
		this.remark = remark;
		this.outwardBy = outwardBy;
		this.outwardById = outwardById;
		this.status = status;
		this.statusId = statusId;
		this.totalAmount = totalAmount;
	}

	public LoanInOutResponse(Integer id, String loanOutReceiveNo, Date loanOutReceiveDate, String store,
			Integer storeId, String vendor, Integer vendorId, String assetType, Integer assetTypeId, String remark,
			Double totalAmount, String inwardBy, Integer inwardById, String status, Integer statusId) {
		super();
		this.id = id;
		this.loanOutReceiveNo = loanOutReceiveNo;
		this.loanOutReceiveDate = loanOutReceiveDate;
		this.store = store;
		this.storeId = storeId;
		this.vendor = vendor;
		this.vendorId = vendorId;
		this.assetType = assetType;
		this.assetTypeId = assetTypeId;
		this.remark = remark;
		this.totalAmount = totalAmount;
		this.inwardBy = inwardBy;
		this.inwardById = inwardById;
		this.status = status;
		this.statusId = statusId;

	}

	public LoanInOutResponse(Integer id, String loanInOutNo, Date loanInOutDate, String store, Integer storeId,
			String vendor, Integer vendorId, String assetType, Integer assetTypeId, String remark, String status,
			Integer statusId) {
		super();
		this.id = id;
		this.loanInOutNo = loanInOutNo;
		this.loanInOutDate = loanInOutDate;
		this.store = store;
		this.storeId = storeId;
		this.vendor = vendor;
		this.vendorId = vendorId;
		this.assetType = assetType;
		this.assetTypeId = assetTypeId;
		this.remark = remark;
		this.status = status;
		this.statusId = statusId;
	}

	public LoanInOutResponse(Integer id, String loanInNo, Date loanInDate, String store, Integer storeId, String vendor,
			Integer vendorId, String assetType, Integer assetTypeId) {
		super();
		this.id = id;
		this.loanInNo = loanInNo;
		this.loanInDate = loanInDate;
		this.store = store;
		this.storeId = storeId;
		this.vendor = vendor;
		this.vendorId = vendorId;
		this.assetType = assetType;
		this.assetTypeId = assetTypeId;
	}

	public LoanInOutResponse(Integer id, String loanOutNo, Date loanOutDate, Integer storeId, String store,
			Integer assetTypeId, String assetType, Integer vendorId, String vendor) {
		super();
		this.id = id;
		this.loanOutNo = loanOutNo;
		this.loanOutDate = loanOutDate;
		this.storeId = storeId;
		this.store = store;
		this.assetTypeId = assetTypeId;
		this.assetType = assetType;
		this.vendorId = vendorId;
		this.vendor = vendor;
	}

}
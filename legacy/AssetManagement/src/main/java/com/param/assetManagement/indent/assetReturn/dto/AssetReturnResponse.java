package com.param.assetManagement.indent.assetReturn.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AssetReturnResponse {

	protected Integer id;

	protected Date returnDate;

	protected String returnNo;

	protected Integer fromStoreId;

	protected String fromStore;

	protected Integer toStoreId;

	protected String toStore;

	protected String returnBy;

	protected String remark;

	protected Integer statusId;

	protected String status;

	protected Integer approvalStatusId;

	protected String approvalStatus;

	protected Integer carrierId;

	protected String carrierName;

	protected List<ItemResponse> item;

	public AssetReturnResponse() {
		super();
	}

	public AssetReturnResponse(Integer id, Date returnDate, String returnNo, Integer fromStoreId, String fromStore,
			Integer toStoreId, String toStore, String returnBy, String remark, Integer statusId, String status,
			Integer carrierId, String carrierName) {
		super();
		this.id = id;
		this.returnDate = returnDate;
		this.returnNo = returnNo;
		this.fromStoreId = fromStoreId;
		this.fromStore = fromStore;
		this.toStoreId = toStoreId;
		this.toStore = toStore;
		this.returnBy = returnBy;
		this.remark = remark;
		this.statusId = statusId;
		this.status = status;
		this.carrierId = carrierId;
		this.carrierName = carrierName;
	}

	public AssetReturnResponse(Integer id, Date returnDate, String returnNo, Integer fromStoreId, String fromStore,
			Integer toStoreId, String toStore, String returnBy, String remark, Integer statusId, String status,
			String approvalStatus, Integer approvalStatusId) {
		super();
		this.id = id;
		this.returnDate = returnDate;
		this.returnNo = returnNo;
		this.fromStoreId = fromStoreId;
		this.fromStore = fromStore;
		this.toStoreId = toStoreId;
		this.toStore = toStore;
		this.returnBy = returnBy;
		this.remark = remark;
		this.statusId = statusId;
		this.status = status;
		this.approvalStatusId = approvalStatusId;
		this.approvalStatus = approvalStatus;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}

	public Integer getFromStoreId() {
		return fromStoreId;
	}

	public void setFromStoreId(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
	}

	public String getFromStore() {
		return fromStore;
	}

	public void setFromStore(String fromStore) {
		this.fromStore = fromStore;
	}

	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	public String getToStore() {
		return toStore;
	}

	public void setToStore(String toStore) {
		this.toStore = toStore;
	}

	public String getReturnBy() {
		return returnBy;
	}

	public void setReturnBy(String returnBy) {
		this.returnBy = returnBy;
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

	public List<ItemResponse> getItem() {
		return item;
	}

	public void setItem(List<ItemResponse> item) {
		this.item = item;
	}

	public void addItems(ItemResponse item) {
		if (this.item == null)
			this.item = new ArrayList<ItemResponse>();

		this.item.add(item);
	}
	/*
	 * protected Boolean type;
	 * 
	 * protected Integer carrierId;
	 * 
	 * protected String carrierName;
	 */

}

package com.param.inventory.store.dto.materialReturn;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MaterialResponse {

	protected Integer id;

	protected Date returnDate;

	protected String returnNo;

	protected Integer fromStoreId;

	protected String fromStore;

	protected Integer toStoreId;

	protected String toStore;

	protected Boolean type;

	protected String returnBy;

	protected Boolean isConsignment;

	protected String remark;

	protected Integer statusId;

	protected String status;

	protected Integer approvalStatusId;
	
	protected String approvalStatus;

	protected Integer carrierId;

	protected String carrierName;

	protected List<ItemRespone> items;

	protected Boolean nearByExpiry;

	public MaterialResponse() {
		super();
	}

	public MaterialResponse(Integer id, Date returnDate, String returnNo, Integer fromStoreId, String fromStore,
			Integer toStoreId, String toStore, Boolean type, String returnBy, Boolean isConsignment, String remark,
			String status) {
		super();
		this.id = id;
		this.returnDate = returnDate;
		this.returnNo = returnNo;
		this.fromStoreId = fromStoreId;
		this.fromStore = fromStore;
		this.toStoreId = toStoreId;
		this.toStore = toStore;
		this.type = type;
		this.returnBy = returnBy;
		this.isConsignment = isConsignment;
		this.remark = remark;
		this.status = status;
	}

	public MaterialResponse(Integer id, Date returnDate, String returnNo, Integer fromStoreId, String fromStore,
			Integer toStoreId, String toStore, String returnBy, Boolean isConsignment, String remark, Integer statusId,
			String status, Boolean nearByExpiry, Integer approvalStatusId, String approvalStatus) {
		super();
		this.id = id;
		this.returnDate = returnDate;
		this.returnNo = returnNo;
		this.fromStoreId = fromStoreId;
		this.fromStore = fromStore;
		this.toStoreId = toStoreId;
		this.toStore = toStore;
		this.returnBy = returnBy;
		this.isConsignment = isConsignment;
		this.remark = remark;
		this.statusId = statusId;
		this.status = status;
		this.nearByExpiry = nearByExpiry;
		this.approvalStatusId = approvalStatusId;
		this.approvalStatus = approvalStatus;
	}
	
	public MaterialResponse(Integer id, Date returnDate, String returnNo, Integer fromStoreId, String fromStore,
			Integer toStoreId, String toStore, String returnBy, Boolean isConsignment, String remark, Integer statusId,
			String status, Integer carrierId, String carrierName, Boolean nearByExpiry) {
		super();
		this.id = id;
		this.returnDate = returnDate;
		this.returnNo = returnNo;
		this.fromStoreId = fromStoreId;
		this.fromStore = fromStore;
		this.toStoreId = toStoreId;
		this.toStore = toStore;
		this.returnBy = returnBy;
		this.isConsignment = isConsignment;
		this.remark = remark;
		this.statusId = statusId;
		this.status = status;
		this.carrierId = carrierId;
		this.carrierName = carrierName;
		this.nearByExpiry = nearByExpiry;
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

	public Boolean getType() {
		return type;
	}

	public void setType(Boolean type) {
		this.type = type;
	}

	public String getReturnBy() {
		return returnBy;
	}

	public void setReturnBy(String returnBy) {
		this.returnBy = returnBy;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
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

	public void addItems(ItemRespone item) {
		if (this.items == null)
			this.items = new ArrayList<ItemRespone>();

		this.items.add(item);
	}

	public Boolean getNearByExpiry() {
		return nearByExpiry;
	}

	public void setNearByExpiry(Boolean nearByExpiry) {
		this.nearByExpiry = nearByExpiry;
	}

	public List<ItemRespone> getItems() {
		return items;
	}

	public void setItems(List<ItemRespone> items) {
		this.items = items;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

}

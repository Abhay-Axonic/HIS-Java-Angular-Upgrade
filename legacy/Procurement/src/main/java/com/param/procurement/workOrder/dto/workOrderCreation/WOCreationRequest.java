package com.param.procurement.workOrder.dto.workOrderCreation;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class WOCreationRequest extends CookieObjectDto {
	
	private Integer id;
	
	private Date fromDate;
	
	private Date toDate;
	
	private Integer contractTypeId;
	
	private Integer storeId;
	
	private Integer statusId;
	
	private Integer woAgainstTypeId;
	
	private Integer vendorId;
	
	private Integer qrId;
	
	private Integer wrId;
	
	private Integer woReqOrWoQuotId;
	
	private Double total_amount;
	
	private Double totalDiscount;
	
	private Double totalTax;
	
	private Double netAmount;
	
	private Integer approvalStatusId;
	
	private String remark;
	
	private String wrNo;
	
	private String woNo;
	
	private List<Integer> itemIds;
	
	public String getWoNo() {
		return woNo;
	}

	public void setWoNo(String woNo) {
		this.woNo = woNo;
	}

	private List<Integer> woCreationIds;
	
	private Integer cancelReasonId;
	
	private String cancelNote;
	
	
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

	public List<Integer> getWoCreationIds() {
		return woCreationIds;
	}

	public void setWoCreationIds(List<Integer> woCreationIds) {
		this.woCreationIds = woCreationIds;
	}

	public String getWrNo() {
		return wrNo;
	}

	public void setWrNo(String wrNo) {
		this.wrNo = wrNo;
	}

	private List<WOCreationDetailRequest> items;
	
	

	public Double getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(Double total_amount) {
		this.total_amount = total_amount;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Double getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(Double totalTax) {
		this.totalTax = totalTax;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<WOCreationDetailRequest> getItems() {
		return items;
	}

	public void setItems(List<WOCreationDetailRequest> items) {
		this.items = items;
	}

	public Integer getWoReqOrWoQuotId() {
		return woReqOrWoQuotId;
	}

	public void setWoReqOrWoQuotId(Integer woReqOrWoQuotId) {
		this.woReqOrWoQuotId = woReqOrWoQuotId;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
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

	
	
	public Integer getWoAgainstTypeId() {
		return woAgainstTypeId;
	}

	public void setWoAgainstTypeId(Integer woAgainstTypeId) {
		this.woAgainstTypeId = woAgainstTypeId;
	}

	public WOCreationRequest() {
		super();
	}

	public Integer getQrId() {
		return qrId;
	}

	public void setQrId(Integer qrId) {
		this.qrId = qrId;
	}

	public Integer getWrId() {
		return wrId;
	}

	public void setWrId(Integer wrId) {
		this.wrId = wrId;
	}

	public List<Integer> getItemIds() {
		return itemIds;
	}

	public void setItemIds(List<Integer> itemIds) {
		this.itemIds = itemIds;
	}
	

}

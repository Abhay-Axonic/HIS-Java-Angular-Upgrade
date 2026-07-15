package com.param.procurement.purchase.dto.goodReceiptNote;

import java.util.Date;
import java.util.List;

import com.param.service.global.model.CookieObjectDto;

public class GRNRequest  extends CookieObjectDto{

	protected Integer id;
	protected Integer orgId;
	protected Integer unitId;
	protected Integer deliveryStoreId;
	protected Integer itemId;
	protected boolean isFromApprovalEdit;
	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getDeliveryStoreId() {
		return deliveryStoreId;
	}

	public void setDeliveryStoreId(Integer deliveryStoreId) {
		this.deliveryStoreId = deliveryStoreId;
	}

	protected Integer addedById;
	protected Integer updatedById;

	public Integer getAddedById() {
		return addedById;
	}

	public void setAddedById(Integer addedById) {
		this.addedById = addedById;
	}

	public Integer getUpdatedById() {
		return updatedById;
	}

	public void setUpdatedById(Integer updatedById) {
		this.updatedById = updatedById;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	protected Date fromDate;
	protected Double totalCreditNoteAmount;

	public Double getTotalCreditNoteAmount() {
		return totalCreditNoteAmount;
	}

	public void setTotalCreditNoteAmount(Double totalCreditNoteAmount) {
		this.totalCreditNoteAmount = totalCreditNoteAmount;
	}

	protected Date toDate;
	protected List<GRNOtherChargesMapper> otherCharges;
	protected List<GRNOtherChargesMapper> grnOtherChargesMapper;
	
	
	public List<GRNOtherChargesMapper> getGrnOtherChargesMapper() {
		return grnOtherChargesMapper;
	}

	public void setGrnOtherChargesMapper(List<GRNOtherChargesMapper> grnOtherChargesMapper) {
		this.grnOtherChargesMapper = grnOtherChargesMapper;
	}

	protected List<CreditNoteAmountList> creditNoteAmountList;

	public List<CreditNoteAmountList> getCreditNoteAmountList() {
		return creditNoteAmountList;
	}

	public void setCreditNoteAmountList(List<CreditNoteAmountList> creditNoteAmountList) {
		this.creditNoteAmountList = creditNoteAmountList;
	}

	public List<GRNOtherChargesMapper> getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(List<GRNOtherChargesMapper> otherCharges) {
		this.otherCharges = otherCharges;
	}

	protected Integer statusId;
	protected Integer paymentStatuses;

	public Integer getPaymentStatuses() {
		return paymentStatuses;
	}

	public void setPaymentStatuses(Integer paymentStatuses) {
		this.paymentStatuses = paymentStatuses;
	}

	protected String billTypeName;

	protected Integer approvalStatusId;

	public String getBillTypeName() {
		return billTypeName;
	}

	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}

	protected Integer storeId;
	protected Integer requestingStoreId;

	public Integer getRequestingStoreId() {
		return requestingStoreId;
	}

	public void setRequestingStoreId(Integer requestingStoreId) {
		this.requestingStoreId = requestingStoreId;
	}

	protected Integer assetTypeId;

	protected Integer grnTypeId;

	protected Date dcDate;

	protected String dcNumber;

	protected Double discount;

	protected Integer discountTypeId;

	protected String grnNumber;

	protected Date invoiceDate;

	protected String invoiceNumber;
	protected String receiptNumber;
	protected String receiptNo;
	protected Double roundOffAmount;
	protected Double totalNetAmount;
	protected Integer grnQtyUomTypeWise;
	
	public Double getTotalNetAmount() {
		return totalNetAmount;
	}

	public void setTotalNetAmount(Double totalNetAmount) {
		this.totalNetAmount = totalNetAmount;
	}

	public Double getRoundOffAmount() {
		return roundOffAmount;
	}

	public void setRoundOffAmount(Double roundOffAmount) {
		this.roundOffAmount = roundOffAmount;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	protected String dueDate;
	
	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	protected Date inwardDate;

	protected String inwardNumber;

	protected Boolean isConsignment;

	protected Double netAmount;

	protected Double roundOff;

	protected Double totalAmount;

	protected Double totalDiscount;

	protected Double totalOtherAmount;

	protected Double totalTaxAmount;

	protected Integer vendorId;

	protected Integer poId;

	protected String vendorName;

	protected String remark;

	protected String cancelNote;

	protected Integer cancelReasonId;

	/*
	 * added by minesh protected Double totalCreditNoteAmount; protected
	 * List<CreditNoteAmountList> creditNoteAmountList; protected
	 * List<GRNOtherChargesMapper> otherCharges; public Double
	 * getTotalCreditNoteAmount() { return totalCreditNoteAmount; }
	 * 
	 * public void setTotalCreditNoteAmount(Double totalCreditNoteAmount) {
	 * this.totalCreditNoteAmount = totalCreditNoteAmount; }
	 * 
	 * public List<CreditNoteAmountList> getCreditNoteAmountList() { return
	 * creditNoteAmountList; }
	 * 
	 * public void setCreditNoteAmountList(List<CreditNoteAmountList>
	 * creditNoteAmountList) { this.creditNoteAmountList = creditNoteAmountList; }
	 * 
	 * public List<GRNOtherChargesMapper> getOtherCharges() { return otherCharges; }
	 * 
	 * public void setOtherCharges(List<GRNOtherChargesMapper> otherCharges) {
	 * this.otherCharges = otherCharges; } end code by minesh
	 */

	protected List<GRNDetailsRequest> items;

	public GRNRequest() {
		super();
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

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
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

	public Date getDcDate() {
		return dcDate;
	}

	public void setDcDate(Date dcDate) {
		this.dcDate = dcDate;
	}

	public String getDcNumber() {
		return dcNumber;
	}

	public void setDcNumber(String dcNumber) {
		this.dcNumber = dcNumber;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getDiscountTypeId() {
		return discountTypeId;
	}

	public void setDiscountTypeId(Integer discountTypeId) {
		this.discountTypeId = discountTypeId;
	}

	public String getGrnNumber() {
		return grnNumber;
	}

	public void setGrnNumber(String grnNumber) {
		this.grnNumber = grnNumber;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Date getInwardDate() {
		return inwardDate;
	}

	public void setInwardDate(Date inwardDate) {
		this.inwardDate = inwardDate;
	}

	public String getInwardNumber() {
		return inwardNumber;
	}

	public void setInwardNumber(String inwardNumber) {
		this.inwardNumber = inwardNumber;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public void setRoundOff(Double roundOff) {
		this.roundOff = roundOff;
	}

	public Double getRoundOff() {
		return roundOff;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Double getTotalOtherAmount() {
		return totalOtherAmount;
	}

	public void setTotalOtherAmount(Double totalOtherAmount) {
		this.totalOtherAmount = totalOtherAmount;
	}

	public Double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(Double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<GRNDetailsRequest> getItems() {
		return items;
	}

	public void setItems(List<GRNDetailsRequest> items) {
		this.items = items;
	}

	public Integer getGrnTypeId() {
		return grnTypeId;
	}

	public void setGrnTypeId(Integer grnTypeId) {
		this.grnTypeId = grnTypeId;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public boolean getIsFromApprovalEdit() {
		return isFromApprovalEdit;
	}

	public void setIsFromApprovalEdit(boolean isFromApprovalEdit) {
		this.isFromApprovalEdit = isFromApprovalEdit;
	}

	public Integer getGrnQtyUomTypeWise() {
		return grnQtyUomTypeWise;
	}

	public void setGrnQtyUomTypeWise(Integer grnQtyUomTypeWise) {
		this.grnQtyUomTypeWise = grnQtyUomTypeWise;
	}
}

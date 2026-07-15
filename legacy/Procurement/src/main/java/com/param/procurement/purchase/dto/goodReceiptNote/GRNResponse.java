package com.param.procurement.purchase.dto.goodReceiptNote;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.param.global.dto.global.OtherChargesResponse;
import com.param.global.dto.global.TaxDropDown;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class GRNResponse {

	protected Integer id;

	protected Integer assetTypeId;

	protected String assetType;

	protected Integer grnTypeId;

	protected String grnType;
	protected String grnNumber;
	protected String billTypeName;
	protected Double totalCreditNoteAmount;
	protected Double paidAmt;
	protected Boolean isSkipPhysicalInspection;
	
	protected String dueDate;
	
	protected boolean isPRNRequired;
	
	protected String pharmacyPRNFormat;

	public String getDueDate() {
		return dueDate;
	}

	public boolean getIsPRNRequired() {
		return isPRNRequired;
	}

	public void setIsPRNRequired(boolean isPRNRequired) {
		this.isPRNRequired = isPRNRequired;
	}

	public String getPharmacyPRNFormat() {
		return pharmacyPRNFormat;
	}

	public void setPharmacyPRNFormat(String pharmacyPRNFormat) {
		this.pharmacyPRNFormat = pharmacyPRNFormat;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public Double getPaidAmt() {
		return paidAmt;
	}

	public void setPaidAmt(Double paidAmt) {
		this.paidAmt = paidAmt;
	}

	public Double getTotalCreditNoteAmount() {
		return totalCreditNoteAmount;
	}

	public void setTotalCreditNoteAmount(Double totalCreditNoteAmount) {
		this.totalCreditNoteAmount = totalCreditNoteAmount;
	}

	public String getBillTypeName() {
		return billTypeName;
	}

	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}

	protected Date grnDate;

	protected Integer discountTypeId;

	protected String discountType;

	protected Integer statusId;

	protected String status;
	protected String receiptNumber;
	protected Date receiptDate;

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	protected Integer approvalStatusId;

	protected String approvalStatus;

	protected Integer storeId;

	protected String StoreName;

	protected Integer requestingStoreId;

	protected String requestingStoreName;
	protected Double roundOffAmount;
	protected Double totalNetAmount;
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

	public Integer getRequestingStoreId() {
		return requestingStoreId;
	}

	public void setRequestingStoreId(Integer requestingStoreId) {
		this.requestingStoreId = requestingStoreId;
	}

	public String getRequestingStoreName() {
		return requestingStoreName;
	}

	public void setRequestingStoreName(String requestingStoreName) {
		this.requestingStoreName = requestingStoreName;
	}

	protected Date dcDate;

	protected String dcNumber;

	protected Double discount;

	protected Date invoiceDate;

	protected String invoiceNumber;

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

	protected String vendorName;

	protected String remark;

	protected String addedBy;

	protected List<GRNDetailResponse> items;

	protected List<OtherChargesResponse> otherChargesList;

	protected List<TaxDropDown> taxList;
	protected List<CreditNoteAmountList> creditNoteAmountList;
	protected List<GRNOtherChargesMapper> grnOtherChargesMapper;
	public List<GRNOtherChargesMapper> getGrnOtherChargesMapper() {
		return grnOtherChargesMapper;
	}

	public void setGrnOtherChargesMapper(List<GRNOtherChargesMapper> grnOtherChargesMapper) {
		this.grnOtherChargesMapper = grnOtherChargesMapper;
	}

	public List<CreditNoteAmountList> getCreditNoteAmountList() {
		return creditNoteAmountList;
	}

	public void setCreditNoteAmountList(List<CreditNoteAmountList> creditNoteAmountList) {
		this.creditNoteAmountList = creditNoteAmountList;
	}

	public GRNResponse() {
		super();
	}

	public GRNResponse(Integer id, Integer assetTypeId, String assetType, Integer grnTypeId, String grnType,
			String grnNumber, Date grnDate, Integer discountTypeId, String discountType, Integer statusId,
			String status, Integer approvalStatusId, String approvalStatus, Integer storeId, String storeName,
			Date dcDate, String dcNumber, Double discount, Date invoiceDate, String invoiceNumber, Date inwardDate,
			String inwardNumber, Boolean isConsignment, Double netAmount, Double roundOff, Double totalAmount,
			Double totalDiscount, Double totalOtherAmount, Double totalTaxAmount, Integer vendorId, String vendorName,
			String remark, String addedBy, Integer requestingStoreId, String requestingStoreName) {
		super();
		this.id = id;
		this.assetTypeId = assetTypeId;
		this.assetType = assetType;
		this.grnTypeId = grnTypeId;
		this.grnType = grnType;
		this.grnNumber = grnNumber;
		this.grnDate = grnDate;
		this.discountTypeId = discountTypeId;
		this.discountType = discountType;
		this.statusId = statusId;
		this.status = status;
		this.approvalStatusId = approvalStatusId;
		this.approvalStatus = approvalStatus;
		this.storeId = storeId;
		this.StoreName = storeName;
		this.dcDate = dcDate;
		this.dcNumber = dcNumber;
		this.discount = discount;
		this.invoiceDate = invoiceDate;
		this.invoiceNumber = invoiceNumber;
		this.inwardDate = inwardDate;
		this.inwardNumber = inwardNumber;
		this.isConsignment = isConsignment;
		this.netAmount = netAmount;
		this.roundOff = roundOff;
		this.totalAmount = totalAmount;
		this.totalDiscount = totalDiscount;
		this.totalOtherAmount = totalOtherAmount;
		this.totalTaxAmount = totalTaxAmount;
		this.vendorId = vendorId;
		this.vendorName = vendorName;
		this.remark = remark;
		this.addedBy = addedBy;
		this.requestingStoreId = requestingStoreId;
		this.requestingStoreName = requestingStoreName;
	}

	public GRNResponse(String grnNumber, Date grnDate, Date invoiceDate, String invoiceNumber, Double totalAmount) {
		super();
		this.grnNumber = grnNumber;
		this.grnDate = grnDate;
		this.invoiceDate = invoiceDate;
		this.invoiceNumber = invoiceNumber;
		this.totalAmount = totalAmount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAssetTypeId() {
		return assetTypeId;
	}

	public void setAssetTypeId(Integer assetTypeId) {
		this.assetTypeId = assetTypeId;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public Integer getGrnTypeId() {
		return grnTypeId;
	}

	public void setGrnTypeId(Integer grnTypeId) {
		this.grnTypeId = grnTypeId;
	}

	public String getGrnType() {
		return grnType;
	}

	public void setGrnType(String grnType) {
		this.grnType = grnType;
	}

	public String getGrnNumber() {
		return grnNumber;
	}

	public void setGrnNumber(String grnNumber) {
		this.grnNumber = grnNumber;
	}

	public Integer getDiscountTypeId() {
		return discountTypeId;
	}

	public void setDiscountTypeId(Integer discountTypeId) {
		this.discountTypeId = discountTypeId;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
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

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return StoreName;
	}

	public void setStoreName(String storeName) {
		StoreName = storeName;
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

	public Double getRoundOff() {
		return roundOff;
	}

	public void setRoundOff(Double roundOff) {
		this.roundOff = roundOff;
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

	public List<GRNDetailResponse> getItems() {
		return items;
	}

	public void setItems(List<GRNDetailResponse> items) {
		this.items = items;
	}

	public Date getGrnDate() {
		return grnDate;
	}

	public void setGrnDate(Date grnDate) {
		this.grnDate = grnDate;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public List<OtherChargesResponse> getOtherChargesList() {
		return otherChargesList;
	}

	public void setOtherChargesList(List<OtherChargesResponse> otherChargesList) {
		this.otherChargesList = otherChargesList;
	}

	public List<TaxDropDown> getTaxList() {
		return taxList;
	}

	public void setTaxList(List<TaxDropDown> taxList) {
		this.taxList = taxList;
	}

	public void addItems(GRNDetailResponse item) {
		if (this.items == null)
			this.items = new ArrayList<GRNDetailResponse>();

		this.items.add(item);
	}

	public Boolean getIsSkipPhysicalInspection() {
		return isSkipPhysicalInspection;
	}

	public void setIsSkipPhysicalInspection(Boolean isSkipPhysicalInspection) {
		this.isSkipPhysicalInspection = isSkipPhysicalInspection;
	}
}

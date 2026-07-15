package com.param.procurement.purchase.purchaseOrder.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.param.global.dto.global.OtherChargesResponse;
import com.param.global.dto.global.TaxDropDown;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocPurchaseReqDetailsResponseDto;
import com.param.procurement.purchase.dto.purchaseOrder.PoOtherChargesMapperDto;

public class PurchaseOrderMasterDetailsDto {

	public PurchaseOrderMasterDetailsDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	private Integer id;

	private String poNumber;

	private Integer assetTypeId;

	private String assetType;

	private Integer poTypeId;

	private String purchaseType;

	private Integer discountTypeId;

	private String discountType;

	private Double discount;

	private Boolean isConsignment;

	private Double netAmount;

	private Double roundOffAmount;

	private Double totalAmount;

	private Double totalDiscount;

	private Double totalOtherAmount;

	private Double totalTaxAmount;

	private Integer statusId;

	private String status;

	private Integer approvalStatusId;

	private String approvalStatus;

	private Integer storeId;

	private String StoreName;
	private Integer requestingStoreId;

	private String requestingStoreName;
	private Integer  reviewedBy;
	private Double  totalNetAmount;

	public Integer getReviewedBy() {
		return reviewedBy;
	}

	public void setReviewedBy(Integer reviewedBy) {
		this.reviewedBy = reviewedBy;
	}

	private Integer vendorId;
	private List<PoOtherChargesMapperDto> otherCharges;

	public List<PoOtherChargesMapperDto> getOtherCharges() {
		return otherCharges;
	}

	public void setOtherCharges(List<PoOtherChargesMapperDto> otherCharges) {
		this.otherCharges = otherCharges;
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

	private String vendorName;

	private String paymentTerm;

	private String remark;

	private String addedBy;

	private Date addedDate;

	private List<PurchaseOrderDetailsRespDto> items;

	private List<OtherChargesResponse> otherChargesList;

	private List<TaxDropDown> taxList;

	public PurchaseOrderMasterDetailsDto(Integer id, String poNumber, Integer assetTypeId, String assetType,
			Integer poTypeId, String purchaseType, Integer discountTypeId, String discountType, Double discount,
			Boolean isConsignment, Double netAmount, Double roundOffAmount, Double totalAmount, Double totalDiscount,
			Double totalOtherAmount, Double totalTaxAmount, Integer statusId, String status, Integer approvalStatusId,
			String approvalStatus, Integer storeId, String storeName, Integer vendorId, String vendorName,
			String paymentTerm, String remark, String addedBy, Date addedDate,
			List<PurchaseOrderDetailsRespDto> items, Integer requestingStoreId , String requestingStoreName) {
		this.id = id;
		this.poNumber = poNumber;
		this.assetTypeId = assetTypeId;
		this.assetType = assetType;
		this.poTypeId = poTypeId;
		this.purchaseType = purchaseType;
		this.discountTypeId = discountTypeId;
		this.discountType = discountType;
		this.discount = discount;
		this.isConsignment = isConsignment;
		this.netAmount = netAmount;
		this.roundOffAmount = roundOffAmount;
		this.totalAmount = totalAmount;
		this.totalDiscount = totalDiscount;
		this.totalOtherAmount = totalOtherAmount;
		this.totalTaxAmount = totalTaxAmount;
		this.statusId = statusId;
		this.status = status;
		this.approvalStatusId = approvalStatusId;
		this.approvalStatus = approvalStatus;
		this.storeId = storeId;
		this.StoreName = storeName;
		this.vendorId = vendorId;
		this.vendorName = vendorName;
		this.paymentTerm = paymentTerm;
		this.remark = remark;
		this.addedBy = addedBy;
		this.addedDate = addedDate;
		this.items = items;
		this.requestingStoreId = requestingStoreId;
		this.requestingStoreName = requestingStoreName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
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

	public Integer getPoTypeId() {
		return poTypeId;
	}

	public void setPoTypeId(Integer poTypeId) {
		this.poTypeId = poTypeId;
	}

	public String getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
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

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
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

	public Double getRoundOffAmount() {
		return roundOffAmount;
	}

	public void setRoundOffAmount(Double roundOffAmount) {
		this.roundOffAmount = roundOffAmount;
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

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public List<TaxDropDown> getTaxList() {
		return taxList;
	}

	public void setTaxList(List<TaxDropDown> taxList) {
		this.taxList = taxList;
	}

	public List<OtherChargesResponse> getOtherChargesList() {
		return otherChargesList;
	}

	public void setOtherChargesList(List<OtherChargesResponse> otherChargesList) {
		this.otherChargesList = otherChargesList;
	}

	public List<PurchaseOrderDetailsRespDto> getItems() {
		return items;
	}

	public void setItems(List<PurchaseOrderDetailsRespDto> items) {
		this.items = items;
	}

	public void addItems(PurchaseOrderDetailsRespDto item) {
		if (this.items == null)
			this.items = new ArrayList<PurchaseOrderDetailsRespDto>();

		this.items.add(item);
	}

	public Double getTotalNetAmount() {
		return totalNetAmount;
	}

	public void setTotalNetAmount(Double totalNetAmount) {
		this.totalNetAmount = totalNetAmount;
	}

	private String invoiceDate;
	
	private String dueDate;

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	
	
	
}

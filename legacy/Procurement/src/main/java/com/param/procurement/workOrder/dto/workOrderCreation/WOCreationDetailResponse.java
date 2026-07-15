package com.param.procurement.workOrder.dto.workOrderCreation;

import java.util.Date;
import java.util.List;

import com.param.global.dto.global.TaxDropDown;
import com.param.global.dto.items.BatchResponse;

public class WOCreationDetailResponse {
	
private Integer id;
	
	private Integer itemId;
	
	private String itemName;
	
	private String itemCode;
	
	private Long batchId;
	
	private String batchNo;
	
	private Integer quantity;
	
	private Integer woQty;
	
	private Double amount;
	
	private Double woAmount;
	
	private Integer currentVendorId;
	
	private String currentVendor;
	
	private Integer statusId;
	
	private Integer workOrderRequestDetailId;
	
	private Integer workOrderDetailId;
	
	

	private String specification;
	
	private String inclusionDetails;
	
	private String exclusionDetails;
	
	private String paymentTerm;
	
	private String remark;
	
	private String batchDetails;
	
	private Date expiry;
	
	private Integer taxId;
	
	private String taxName;
	
	private Double taxPercent;
	
	private Double totalAmount;
	
	private Double discountPercentage;
	
	private String qrNo;
	
	protected List<WOCreationResponse> POCreationDetailList;
    
    protected List<TaxDropDown> taxList;
    
    
	

	public Integer getWoQty() {
		return woQty;
	}

	public void setWoQty(Integer woQty) {
		this.woQty = woQty;
	}

	public Double getWoAmount() {
		return woAmount;
	}

	public void setWoAmount(Double woAmount) {
		this.woAmount = woAmount;
	}

	public List<WOCreationResponse> getPOCreationDetailList() {
		return POCreationDetailList;
	}

	public void setPOCreationDetailList(List<WOCreationResponse> pOCreationDetailList) {
		POCreationDetailList = pOCreationDetailList;
	}

	public List<TaxDropDown> getTaxList() {
		return taxList;
	}

	public void setTaxList(List<TaxDropDown> taxList) {
		this.taxList = taxList;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	private Double discountAmount;
	
	private Double netAmount;
	
	private Double taxAmount;
	
	
	
	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public Double getTaxPercent() {
		return taxPercent;
	}

	public void setTaxPercent(Double taxPercent) {
		this.taxPercent = taxPercent;
	}

	private List<BatchResponse> batchList;

	public Integer getWorkOrderDetailId() {
		return workOrderDetailId;
	}

	public void setWorkOrderDetailId(Integer workOrderDetailId) {
		this.workOrderDetailId = workOrderDetailId;
	}
	public String getBatchDetails() {
		return batchDetails;
	}

	public void setBatchDetails(String batchDetails) {
		this.batchDetails = batchDetails;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getCurrentVendorId() {
		return currentVendorId;
	}

	public void setCurrentVendorId(Integer currentVendorId) {
		this.currentVendorId = currentVendorId;
	}

	public String getCurrentVendor() {
		return currentVendor;
	}

	public void setCurrentVendor(String currentVendor) {
		this.currentVendor = currentVendor;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getWorkOrderRequestDetailId() {
		return workOrderRequestDetailId;
	}

	public void setWorkOrderRequestDetailId(Integer workOrderRequestDetailId) {
		this.workOrderRequestDetailId = workOrderRequestDetailId;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getInclusionDetails() {
		return inclusionDetails;
	}

	public void setInclusionDetails(String inclusionDetails) {
		this.inclusionDetails = inclusionDetails;
	}

	public String getExclusionDetails() {
		return exclusionDetails;
	}

	public void setExclusionDetails(String exclusionDetails) {
		this.exclusionDetails = exclusionDetails;
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

	public List<BatchResponse> getBatchList() {
		return batchList;
	}

	public void setBatchList(List<BatchResponse> batchList) {
		this.batchList = batchList;
	}
	
	
	
	public Double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public String getQrNo() {
		return qrNo;
	}

	public void setQrNo(String qrNo) {
		this.qrNo = qrNo;
	}

	public WOCreationDetailResponse()
	{
		super();
	}

	public WOCreationDetailResponse(Integer id, Integer itemId, String itemCode, String itemName, Long batchId, String batchNo,
			Double amount, Integer venderId, String venderName, Integer statusId, Date expiryDate, Integer workOrderRequestDetailId, String specification,
			String inclusionDetails, String exclusionDetails, String paymentTerm, String remark) {
		
		this.id=id;
		this.itemId=itemId;
		this.itemCode=itemCode;
		this.itemName=itemName;
		this.batchId=batchId;
		this.batchNo=batchNo;
		this.amount=amount;
		this.currentVendorId=venderId;
		this.currentVendor=venderName;
		this.statusId=statusId;
		this.expiry=expiryDate;
		this.workOrderRequestDetailId=workOrderRequestDetailId;
		this.specification=specification;
		this.inclusionDetails=inclusionDetails;
		this.exclusionDetails=exclusionDetails;
		this.paymentTerm=paymentTerm;
		this.remark=remark;
		this.batchDetails=batchNo + " | " + expiryDate;
		
	
	}
	
	public WOCreationDetailResponse(Integer id, Integer itemId, String itemCode, String itemName, Long batchId, String batchNo,
			 Integer venderId, String venderName, Integer statusId, Date expiryDate, Integer workOrderRequestDetailId, String specification,
			 String remark,Double amount,Integer quantity) {
		
		this.id=id;
		this.itemId=itemId;
		this.itemCode=itemCode;
		this.itemName=itemName;
		this.batchId=batchId;
		this.batchNo=batchNo;
		this.amount=amount;
		this.currentVendorId=venderId;
		this.currentVendor=venderName;
		this.statusId=statusId;
		this.expiry=expiryDate;
		this.workOrderRequestDetailId=workOrderRequestDetailId;
		this.specification=specification;
		this.remark=remark;
		this.batchDetails=batchNo + " | " + expiryDate;
		this.quantity=quantity;
		
	
	}
	
	public WOCreationDetailResponse(Integer id, Integer itemId, String itemCode, String itemName, Long batchId, String batchNo,
			 Integer venderId, String venderName, Integer statusId, Date expiryDate, Integer workOrderRequestDetailId, String specification,
			 String remark,Integer taxId,String taxName,Double taxPercent,Double amount,Integer quantity) {
		
		this.id=id;
		this.itemId=itemId;
		this.itemCode=itemCode;
		this.itemName=itemName;
		this.batchId=batchId;
		this.batchNo=batchNo;
		this.amount=amount;
		this.currentVendorId=venderId;
		this.currentVendor=venderName;
		this.statusId=statusId;
		this.expiry=expiryDate;
		this.workOrderRequestDetailId=workOrderRequestDetailId;
		this.specification=specification;
		this.remark=remark;
		this.batchDetails=batchNo + " | " + expiryDate;
		this.taxId=taxId;
		this.taxName=taxName;
		this.taxPercent=taxPercent;
		this.quantity=quantity;
	
	}
	
	public WOCreationDetailResponse(Integer id, Integer itemId, String itemCode, String itemName, Long batchId, String batchNo,
			 Integer venderId, String venderName, Integer statusId, Date expiryDate, Integer workOrderRequestDetailId, String specification,
			 String remark,Integer taxId,String taxName,Double taxPercent,Double amount,Integer quantity,String qrNo) {
		
		this.id=id;
		this.itemId=itemId;
		this.itemCode=itemCode;
		this.itemName=itemName;
		this.batchId=batchId;
		this.batchNo=batchNo;
		this.amount=amount;
		this.currentVendorId=venderId;
		this.currentVendor=venderName;
		this.statusId=statusId;
		this.expiry=expiryDate;
		this.workOrderRequestDetailId=workOrderRequestDetailId;
		this.specification=specification;
		this.remark=remark;
		this.batchDetails=batchNo + " | " + expiryDate;
		this.taxId=taxId;
		this.taxName=taxName;
		this.taxPercent=taxPercent;
		this.quantity=quantity;
		this.qrNo=qrNo;
	
	}
	
	public WOCreationDetailResponse(Integer id, Integer itemId, String itemCode, String itemName, Long batchId, String batchNo,
			Double amount, Integer venderId, String venderName, Integer statusId, Date expiryDate, String specification,
			String inclusionDetails, String exclusionDetails, String paymentTerm, String remark, Integer workOrderDetailId) {
		
		this.id=id;
		this.itemId=itemId;
		this.itemCode=itemCode;
		this.itemName=itemName;
		this.batchId=batchId;
		this.batchNo=batchNo;
		this.amount=amount;
		this.currentVendorId=venderId;
		this.currentVendor=venderName;
		this.statusId=statusId;
		this.expiry=expiryDate;
		this.workOrderDetailId=workOrderDetailId;
		this.specification=specification;
		this.inclusionDetails=inclusionDetails;
		this.exclusionDetails=exclusionDetails;
		this.paymentTerm=paymentTerm;
		this.remark=remark;
		this.batchDetails=batchNo + " | " + expiryDate;
		
	
	}
	public WOCreationDetailResponse(Integer id, Integer itemId, String itemCode, String itemName, Long batchId, String batchNo,
			 Integer venderId, String venderName, Integer statusId, Date expiryDate, String specification,
			 String remark, Integer workOrderDetailId,Integer quantity) {
		
		this.id=id;
		this.itemId=itemId;
		this.itemCode=itemCode;
		this.itemName=itemName;
		this.batchId=batchId;
		this.batchNo=batchNo;
		this.currentVendorId=venderId;
		this.currentVendor=venderName;
		this.statusId=statusId;
		this.expiry=expiryDate;
		this.workOrderDetailId=workOrderDetailId;
		this.specification=specification;
		this.remark=remark;
		this.batchDetails=batchNo + " | " + expiryDate;
		this.quantity=quantity;
	
	}
	
	public WOCreationDetailResponse(Integer id, Integer itemId, String itemCode, String itemName, Long batchId, String batchNo,
			 Integer venderId, String venderName, Integer statusId, Date expiryDate, String specification,
			 String remark, Integer workOrderDetailId,Integer taxId,String taxName,Double taxPercent,Integer quantity) {
		
		this.id=id;
		this.itemId=itemId;
		this.itemCode=itemCode;
		this.itemName=itemName;
		this.batchId=batchId;
		this.batchNo=batchNo;
		this.currentVendorId=venderId;
		this.currentVendor=venderName;
		this.statusId=statusId;
		this.expiry=expiryDate;
		this.workOrderDetailId=workOrderDetailId;
		this.specification=specification;
		this.remark=remark;
		this.batchDetails=batchNo + " | " + expiryDate;
		this.taxId=taxId;
		this.taxName=taxName;
		this.taxPercent=taxPercent;
		this.quantity=quantity;
	
	}
	
	public WOCreationDetailResponse(Integer id, Integer itemId, String itemCode, String itemName, Long batchId, String batchNo,
			Double amount, Integer venderId, String venderName, Integer statusId, Date expiryDate, String specification,
			String inclusionDetails, String exclusionDetails, String paymentTerm, String remark,
			Integer taxId, String taxName, Double taxPercentage, Double totalAmount,
			Double discountAmount, Double netAmount, Double taxAmount, Integer workOrderDetailId) {
		
		this.id=id;
		this.itemId=itemId;
		this.itemCode=itemCode;
		this.itemName=itemName;
		this.batchId=batchId;
		this.batchNo=batchNo;
		this.amount=amount;
		this.currentVendorId=venderId;
		this.currentVendor=venderName;
		this.statusId=statusId;
		this.expiry=expiryDate;
		this.workOrderDetailId=workOrderDetailId;
		this.specification=specification;
		this.inclusionDetails=inclusionDetails;
		this.exclusionDetails=exclusionDetails;
		this.paymentTerm=paymentTerm;
		this.remark=remark;
		this.batchDetails=batchNo + " | " + expiryDate;
		
	
	}
	
	//for WR
	public WOCreationDetailResponse(Integer id, Integer itemId, String itemCode, String itemName, Long batchId, String batchNo,
			Double amount, Integer venderId, String venderName, Integer statusId, Date expiryDate, String specification,
			String inclusionDetails, String exclusionDetails, String paymentTerm, String remark, Integer workOrderDetailId,
			Integer taxId, String taxName, Double taxPercentage, Double totalAmount,
			Double discountAmount, Double netAmount, Double taxAmount,Integer woQty,Integer quantity,Double discountPercentage) {
		
		this.id=id;
		this.itemId=itemId;
		this.itemCode=itemCode;
		this.itemName=itemName;
		this.batchId=batchId;
		this.batchNo=batchNo;
		this.woAmount=amount;
		this.currentVendorId=venderId;
		this.currentVendor=venderName;
		this.statusId=statusId;
		this.expiry=expiryDate;
		this.workOrderDetailId=workOrderDetailId;
		this.specification=specification;
		this.inclusionDetails=inclusionDetails;
		this.exclusionDetails=exclusionDetails;
		this.paymentTerm=paymentTerm;
		this.remark=remark;
		this.batchDetails=batchNo + " | " + expiryDate;
		this.taxId=taxId;
		this.taxName=taxName;
		this.taxPercent=taxPercentage;
		this.totalAmount=totalAmount;
		this.discountAmount=discountAmount;
		this.netAmount=netAmount;
		this.taxAmount=taxAmount;
		this.quantity=quantity;
		this.woQty=woQty;
		this.discountPercentage=discountPercentage;
	
	}
	
	public WOCreationDetailResponse(Integer id, Integer itemId, String itemCode, String itemName, Long batchId, String batchNo,
			Double woAmount, Integer venderId, String venderName, Integer statusId, Date expiryDate, Integer workOrderRequestDetailId, String specification,
			String inclusionDetails, String exclusionDetails, String paymentTerm, String remark,
			Integer taxId, String taxName, Double taxPercentage, Double totalAmount,
			Double discountAmount, Double netAmount, Double taxAmount,Integer quantity,Double amount,Integer woQty ,Double discountPercentage) {
		
		this.id=id;
		this.itemId=itemId;
		this.itemCode=itemCode;
		this.itemName=itemName;
		this.batchId=batchId;
		this.batchNo=batchNo;
		this.woQty=woQty;
		this.woAmount=woAmount;
		this.currentVendorId=venderId;
		this.currentVendor=venderName;
		this.statusId=statusId;
		this.expiry=expiryDate;
		this.workOrderRequestDetailId=workOrderRequestDetailId;
		this.specification=specification;
		this.inclusionDetails=inclusionDetails;
		this.exclusionDetails=exclusionDetails;
		this.paymentTerm=paymentTerm;
		this.remark=remark;
		this.batchDetails=batchNo + " | " + expiryDate;
		this.taxId=taxId;
		this.taxName=taxName;
		this.taxPercent=taxPercentage;
		this.totalAmount=totalAmount;
		this.discountAmount=discountAmount;
		this.netAmount=netAmount;
		this.taxAmount=taxAmount;
		this.quantity=quantity;
		this.amount=amount;
		this.discountPercentage=discountPercentage;
		
	
	}

	public WOCreationDetailResponse(Integer id, Integer itemId, String itemCode, String itemName, Long batchId, String batchNo,
			Double woAmount, Integer venderId, String venderName, Integer statusId, Date expiryDate,  String specification,
			String inclusionDetails, String exclusionDetails, String paymentTerm, String remark,
			Integer taxId, String taxName, Double taxPercentage, Double totalAmount,
			Double discountAmount, Double netAmount, Double taxAmount,Double discountPercentage,List<BatchResponse> batchList,Integer woQty) {
		
		this.id=id;
		this.itemId=itemId;
		this.itemCode=itemCode;
		this.itemName=itemName;
		this.batchId=batchId;
		this.batchNo=batchNo;
		this.woAmount=woAmount;
		this.currentVendorId=venderId;
		this.currentVendor=venderName;
		this.statusId=statusId;
		this.expiry=expiryDate;
		this.specification=specification;
		this.inclusionDetails=inclusionDetails;
		this.exclusionDetails=exclusionDetails;
		this.paymentTerm=paymentTerm;
		this.remark=remark;
		this.batchDetails=batchNo + " | " + expiryDate;
		this.taxId=taxId;
		this.taxName=taxName;
		this.taxPercent=taxPercentage;
		this.totalAmount=totalAmount;
		this.discountAmount=discountAmount;
		this.netAmount=netAmount;
		this.taxAmount=taxAmount;
		this.batchList=batchList;
		this.discountPercentage=discountPercentage;
		this.woQty=woQty;
	
	}

	
	

}

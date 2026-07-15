package com.param.billing.global.transaction.dto;

import java.math.BigDecimal;
import java.util.List;

import com.param.global.dto.PayeeRefundMpprDto;

public class BillListDetailsCNDto {

	private Integer serviceMasterId;

	private Integer billingMasterId;

	private Integer billingDetailsId;

	private Integer orderDetailsId;
	
	private Integer visitTypeId;
	
	private Integer specialityId;
	
	private String specialityName;
	
	private Integer subSpecialityMasterId;
	
	private String subSpecialityMasterName;

	private Integer orderId;

	private String orderNo;

	private String serviceStandardName;

	private Integer doctorId;

	private String doctorName;

	private Double quantity;

	private Double concession;

	private Double grossAmount;

	private Double unitPrice;

	private Double previousDiscount;

	private Double previousDiscountToadd;

	private Double discount;

	private Double discountPercentage;

	private Integer taxId;

	private String taxName;

	private Double taxPercentage;

	private Double taxAmount;
	
	private Double crTaxToadd;

	private Double netAmt;

	private Double crAmount;

	private Double refundAmount;

	private Double balanceAmount;

	private Double outstanding;

	private Double totalBillAmount;

	private Double payeeDueAmount;

	private Double billAmount;
	
	private Double previousDiscountPer;

	private BigDecimal basePrice;

	private String orderDateString;

	private Integer discountCategoryId;

	private Integer orderDetailId;

	private String isSelected;

	private Character isRateEditable;

	private Character discountDisabledFlag;

	private BigDecimal minRateEditable;

	private BigDecimal maxRateEditable;

	private Character isTaxApplicable;

	private Character isRefDoctorShare;

	private Character isDiscountApplicable;

	private BigDecimal discountValue;

	private Integer gstTypeId;

	private Integer otcGstTypeId;

	private Character isQuantityEditable;

	private Character isPackageService;

	private Integer serviceRendered;

	private Character isAutorendered;

	private List<PayeeRefundMpprDto> payeeRefundMppr;

	private Integer cancelOrDiscount;

	/*** Credit Note */
	private Double crTaxAmount;

	private Double crNetAmount;

	private Double billDiscount;
	private Double billDiscountPer;

	private Integer associateCompanyId;

	private List<CreditNoteDiscountDetailsDto> discountDetailsList;


	private Double remainingRefundAmt;
	
	private BigDecimal selfPayable;
	
	private BigDecimal creditPayable;
	
	private BigDecimal packageConsumptionAmt;
	
	private boolean isPackage;
	
	private Integer packageId;
	
	private Character ordCancelled;
	
	private String serviceStandardCode;
	
	private Character creditApproved;
	
    private Integer packageTypeId;
    
    private BigDecimal docSharePer;
    
    private BigDecimal hospitalShare;
    
    private Integer discountOwnership;
    
    private BigDecimal refDocSharePer;
    
    private Integer tariffId;
    
    private Character isDoctorRequired;
    
    private Integer doctorSpecialityId;
    
    private Long orderDate;
    
    private boolean isDummyService;
    
    private Integer billStatusId;
    
    public Integer getBillStatusId() {
		return billStatusId;
	}

	public void setBillStatusId(Integer billStatusId) {
		this.billStatusId = billStatusId;
	}

	public boolean getIsDummyService() {
		return isDummyService;
	}

	public void setIsDummyService(boolean isDummyService) {
		this.isDummyService = isDummyService;
	}

	public Integer getPackageTypeId() {
        return packageTypeId;
    }

    public void setPackageTypeId(Integer packageTypeId) {
        this.packageTypeId = packageTypeId;
    }

	
	public Character getOrdCancelled() {
		return ordCancelled;
	}

	public void setOrdCancelled(Character ordCancelled) {
		this.ordCancelled = ordCancelled;
	}

	public BigDecimal getSelfPayable() {
		return selfPayable;
	}

	public void setSelfPayable(BigDecimal selfPayable) {
		this.selfPayable = selfPayable;
	}

	public BigDecimal getCreditPayable() {
		return creditPayable;
	}

	public void setCreditPayable(BigDecimal creditPayable) {
		this.creditPayable = creditPayable;
	}

	public BigDecimal getPackageConsumptionAmt() {
		return packageConsumptionAmt;
	}

	public void setPackageConsumptionAmt(BigDecimal packageConsumptionAmt) {
		this.packageConsumptionAmt = packageConsumptionAmt;
	}

	public Double getRemainingRefundAmt() {
		return remainingRefundAmt;
	}

	public void setRemainingRefundAmt(Double remainingRefundAmt) {
		this.remainingRefundAmt = remainingRefundAmt;
	}

	public Integer getBillingMasterId() {
		return billingMasterId;
	}

	public void setBillingMasterId(Integer billingMasterId) {
		this.billingMasterId = billingMasterId;
	}

	public Integer getBillingDetailsId() {
		return billingDetailsId;
	}

	public void setBillingDetailsId(Integer billingDetailsId) {
		this.billingDetailsId = billingDetailsId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getServiceStandardName() {
		return serviceStandardName;
	}

	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getConcession() {
		return concession;
	}

	public void setConcession(Double concession) {
		this.concession = concession;
	}

	public Double getGrossAmount() {
		return grossAmount;
	}

	public void setGrossAmount(Double grossAmount) {
		this.grossAmount = grossAmount;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

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

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getNetAmt() {
		return netAmt;
	}

	public void setNetAmt(Double netAmt) {
		this.netAmt = netAmt;
	}

	public String getOrderDateString() {
		return orderDateString;
	}

	public void setOrderDateString(String orderDateString) {
		this.orderDateString = orderDateString;
	}

	public Integer getDiscountCategoryId() {
		return discountCategoryId;
	}

	public void setDiscountCategoryId(Integer discountCategoryId) {
		this.discountCategoryId = discountCategoryId;
	}

	public String getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}

	public Integer getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Character getIsRateEditable() {
		return isRateEditable;
	}

	public void setIsRateEditable(Character isRateEditable) {
		this.isRateEditable = isRateEditable;
	}

	public BigDecimal getMinRateEditable() {
		return minRateEditable;
	}

	public void setMinRateEditable(BigDecimal minRateEditable) {
		this.minRateEditable = minRateEditable;
	}

	public BigDecimal getMaxRateEditable() {
		return maxRateEditable;
	}

	public void setMaxRateEditable(BigDecimal maxRateEditable) {
		this.maxRateEditable = maxRateEditable;
	}

	public Character getIsTaxApplicable() {
		return isTaxApplicable;
	}

	public void setIsTaxApplicable(Character isTaxApplicable) {
		this.isTaxApplicable = isTaxApplicable;
	}

	public Character getIsRefDoctorShare() {
		return isRefDoctorShare;
	}

	public void setIsRefDoctorShare(Character isRefDoctorShare) {
		this.isRefDoctorShare = isRefDoctorShare;
	}

	public Character getIsDiscountApplicable() {
		return isDiscountApplicable;
	}

	public void setIsDiscountApplicable(Character isDiscountApplicable) {
		this.isDiscountApplicable = isDiscountApplicable;
	}

	public BigDecimal getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(BigDecimal discountValue) {
		this.discountValue = discountValue;
	}

	public Integer getGstTypeId() {
		return gstTypeId;
	}

	public void setGstTypeId(Integer gstTypeId) {
		this.gstTypeId = gstTypeId;
	}

	public Integer getOtcGstTypeId() {
		return otcGstTypeId;
	}

	public void setOtcGstTypeId(Integer otcGstTypeId) {
		this.otcGstTypeId = otcGstTypeId;
	}

	public Character getIsQuantityEditable() {
		return isQuantityEditable;
	}

	public void setIsQuantityEditable(Character isQuantityEditable) {
		this.isQuantityEditable = isQuantityEditable;
	}

	public Double getCrAmount() {
		return crAmount;
	}

	public void setCrAmount(Double crAmount) {
		this.crAmount = crAmount;
	}

	public Double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public Double getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(Double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public BigDecimal getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}

	public Character getIsPackageService() {
		return isPackageService;
	}

	public void setIsPackageService(Character isPackageService) {
		this.isPackageService = isPackageService;
	}

	public Integer getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(Integer serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}

	public List<CreditNoteDiscountDetailsDto> getDiscountDetailsList() {
		return discountDetailsList;
	}

	public void setDiscountDetailsList(List<CreditNoteDiscountDetailsDto> discountDetailsList) {
		this.discountDetailsList = discountDetailsList;
	}


	public Character getDiscountDisabledFlag() {
		return discountDisabledFlag;
	}

	public void setDiscountDisabledFlag(Character discountDisabledFlag) {
		this.discountDisabledFlag = discountDisabledFlag;
	}

	public Double getCrTaxAmount() {
		return crTaxAmount;
	}

	public void setCrTaxAmount(Double crTaxAmount) {
		this.crTaxAmount = crTaxAmount;
	}

	public Double getCrNetAmount() {
		return crNetAmount;
	}

	public void setCrNetAmount(Double crNetAmount) {
		this.crNetAmount = crNetAmount;
	}

	public Integer getServiceRendered() {
		return serviceRendered;
	}

	public void setServiceRendered(Integer serviceRendered) {
		this.serviceRendered = serviceRendered;
	}

	public Double getOutstanding() {
		return outstanding;
	}

	public void setOutstanding(Double outstanding) {
		this.outstanding = outstanding;
	}

	public double getTotalBillAmount() {
		return totalBillAmount;
	}

	public void setTotalBillAmount(double totalBillAmount) {
		this.totalBillAmount = totalBillAmount;
	}

	public Double getPayeeDueAmount() {
		return payeeDueAmount;
	}

	public void setPayeeDueAmount(Double payeeDueAmount) {
		this.payeeDueAmount = payeeDueAmount;
	}

	public Double getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}

	public void setTotalBillAmount(Double totalBillAmount) {
		this.totalBillAmount = totalBillAmount;
	}

	public Character getIsAutorendered() {
		return isAutorendered;
	}

	public void setIsAutorendered(Character isAutorendered) {
		this.isAutorendered =  (isAutorendered==null||isAutorendered == '\u0000') ? 'N' : isAutorendered;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public Integer getSubSpecialityMasterId() {
		return subSpecialityMasterId;
	}

	public void setSubSpecialityMasterId(Integer subSpecialityMasterId) {
		this.subSpecialityMasterId = subSpecialityMasterId;
	}

	public String getSubSpecialityMasterName() {
		return subSpecialityMasterName;
	}

	public void setSubSpecialityMasterName(String subSpecialityMasterName) {
		this.subSpecialityMasterName = subSpecialityMasterName;
	}

	public Double getPreviousDiscountToadd() {
		return previousDiscountToadd;
	}

	public void setPreviousDiscountToadd(Double previousDiscountToadd) {
		this.previousDiscountToadd = previousDiscountToadd;
	}
	public Double getPreviousDiscount() {
		
		return previousDiscount+((previousDiscountToadd==null)?0:previousDiscountToadd);
	}

	public void setPreviousDiscount(Double previousDiscount) {
		this.previousDiscount = previousDiscount;
	}

	public Double getCrTaxToadd() {
		return crTaxToadd;
	}

	public void setCrTaxToadd(Double crTaxToadd) {
		this.crTaxToadd = (crTaxToadd==null)?0:crTaxToadd;
	}

	public Double getPreviousDiscountPer() {
		return previousDiscountPer;
	}

	public void setPreviousDiscountPer(Double previousDiscountPer) {
		this.previousDiscountPer = previousDiscountPer;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public boolean isPackage() {
		return isPackage;
	}

	public void setPackage(boolean isPackage) {
		this.isPackage = isPackage;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public String getServiceStandardCode() {
		return serviceStandardCode;
	}

	public void setServiceStandardCode(String serviceStandardCode) {
		this.serviceStandardCode = serviceStandardCode;
	}

	public Character getCreditApproved() {
		return creditApproved;
	}

	public void setCreditApproved(Character creditApproved) {
		this.creditApproved = creditApproved;
	}

	public List<PayeeRefundMpprDto> getPayeeRefundMppr() {
		return payeeRefundMppr;
	}

	public void setPayeeRefundMppr(List<PayeeRefundMpprDto> payeeRefundMppr) {
		this.payeeRefundMppr = payeeRefundMppr;
	}

	public Double getBillDiscount() {
		return billDiscount;
	}

	public void setBillDiscount(Double billDiscount) {
		this.billDiscount = billDiscount;
	}

	public Double getBillDiscountPer() {
		return billDiscountPer;
	}

	public void setBillDiscountPer(Double billDiscountPer) {
		this.billDiscountPer = billDiscountPer;
	}

	public Integer getAssociateCompanyId() {
		return associateCompanyId;
	}

	public void setAssociateCompanyId(Integer associateCompanyId) {
		this.associateCompanyId = associateCompanyId;
	}

	public Integer getCancelOrDiscount() {
		return cancelOrDiscount!=null ? cancelOrDiscount : 0;
	}

	public void setCancelOrDiscount(Integer cancelOrDiscount) {
		this.cancelOrDiscount = cancelOrDiscount;
	}

	public BigDecimal getDocSharePer() {
		return docSharePer;
	}

	public void setDocSharePer(BigDecimal docSharePer) {
		this.docSharePer = docSharePer;
	}

	public BigDecimal getHospitalShare() {
		return hospitalShare;
	}

	public void setHospitalShare(BigDecimal hospitalShare) {
		this.hospitalShare = hospitalShare;
	}

	public Integer getDiscountOwnership() {
		return discountOwnership;
	}

	public void setDiscountOwnership(Integer discountOwnership) {
		this.discountOwnership = discountOwnership;
	}

	public BigDecimal getRefDocSharePer() {
		return refDocSharePer;
	}

	public void setRefDocSharePer(BigDecimal refDocSharePer) {
		this.refDocSharePer = refDocSharePer;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public Character getIsDoctorRequired() {
		return isDoctorRequired;
	}

	public void setIsDoctorRequired(Character isDoctorRequired) {
		this.isDoctorRequired = isDoctorRequired;
	}

	public Integer getDoctorSpecialityId() {
		return doctorSpecialityId;
	}

	public void setDoctorSpecialityId(Integer doctorSpecialityId) {
		this.doctorSpecialityId = doctorSpecialityId;
	}

	public Long getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Long orderDate) {
		this.orderDate = orderDate;
	}
}

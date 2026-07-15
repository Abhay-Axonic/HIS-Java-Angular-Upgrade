package com.param.billing.global.transaction.dto;

import java.math.BigDecimal;
import java.util.List;

import com.param.global.dto.OrderDetailsPackageMapperDto;

public class BillDetailsByBillIdDto {

	private Integer billingMasterId;
	
	private Integer billingDetailsId;
	
	private String orderNo;
	
	private Integer serviceId;

	private Integer orderId;
	
	private String serviceStandardName;
	
	private Integer doctorId;
	
	private String doctorName; 
	
	private Double quantity;
	
	private Double concession;
	
	private Double grossAmount;
	
	private Double unitPrice;
	
	private Double discount;
	
	private Double discountPercentage;
	
	private Double concessionPercentage;
	
	private Integer taxId;
	
	private String taxName; 
	
	private Double taxPercentage;
	
	private Double taxAmount;
	
	private Double netAmt ;
	
	private String orderDateString;
	
	private String orderByUserName;

	private Double selfPayable;
	
	private Double creditPayable;
	
	private Integer payeeId;
	
	private Integer specialityId;
	
	private String specialityName;
	
	private Integer orderDetailsId;//Ganesh

	private Integer saleId;
	
	private Integer docSpecialityId;
	
	private BigDecimal packageConsumptionAmt;
	
	private String billNumber;
	
	private String patientName;
	
	private Double coPayPer;
	
	private Character isDiscountApplicable;
	
	private String serviceStandardCode;
	
	private String uhidNumber;
	
	private Integer serviceRendered;
	
	private char isPackageService;

    private Integer appointmentId;
	
	
	public String getServiceStandardCode() {
		return serviceStandardCode;
	}

	public void setServiceStandardCode(String serviceStandardCode) {
		this.serviceStandardCode = serviceStandardCode;
	}

	public Character getIsDiscountApplicable() {
		return isDiscountApplicable;
	}

	public void setIsDiscountApplicable(Character isDiscountApplicable) {
		this.isDiscountApplicable = isDiscountApplicable;
	}

	public Double getCoPayPer() {
		return coPayPer;
	}

	public void setCoPayPer(Double coPayPer) {
		this.coPayPer = coPayPer;
	}

	private List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtosList;
	
	public Double getConcessionPercentage() {
		return concessionPercentage;
	}

	public void setConcessionPercentage(Double concessionPercentage) {
		this.concessionPercentage = concessionPercentage;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Integer getDocSpecialityId() {
		return docSpecialityId;
	}

	public void setDocSpecialityId(Integer docSpecialityId) {
		this.docSpecialityId = docSpecialityId;
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

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Double getSelfPayable() {
		return selfPayable;
	}

	public void setSelfPayable(Double selfPayable) {
		this.selfPayable = selfPayable;
	}

	public Double getCreditPayable() {
		return creditPayable;
	}

	public void setCreditPayable(Double creditPayable) {
		this.creditPayable = creditPayable;
	}

	private Double roundOff ;


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

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
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

	public String getOrderByUserName() {
		return orderByUserName;
	}

	public void setOrderByUserName(String orderByUserName) {
		this.orderByUserName = orderByUserName;
	}

	public Double getRoundOff() {
		return roundOff;
	}

	public void setRoundOff(Double roundOff) {
		this.roundOff = roundOff;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public List<OrderDetailsPackageMapperDto> getOrderDetailsPackageMapperDtosList() {
		return orderDetailsPackageMapperDtosList;
	}

	public void setOrderDetailsPackageMapperDtosList(List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtosList) {
		this.orderDetailsPackageMapperDtosList = orderDetailsPackageMapperDtosList;
	}

	public BigDecimal getPackageConsumptionAmt() {
		return packageConsumptionAmt;
	}

	public void setPackageConsumptionAmt(BigDecimal packageConsumptionAmt) {
		this.packageConsumptionAmt = packageConsumptionAmt;
	}

	public Integer getSaleId() {
		return saleId;
	}

	public void setSaleId(Integer saleId) {
		this.saleId = saleId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public Integer getServiceRendered() {
		return serviceRendered;
	}

	public void setServiceRendered(Integer serviceRendered) {
		this.serviceRendered = serviceRendered;
	}

	public char getIsPackageService() {
		return isPackageService;
	}

	public void setIsPackageService(char isPackageService) {
		this.isPackageService = isPackageService;
	}

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }
}

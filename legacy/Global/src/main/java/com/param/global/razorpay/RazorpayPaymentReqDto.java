package com.param.global.razorpay;

public class RazorpayPaymentReqDto {

	private Integer amount;
	
	private String currency;
	
	private String apiKey;
	
	private String apiSecretKey;

	private Integer unitId;
	
	private Integer organizationId;
	
	private String orderId;
	
	private Integer appointmentId;
	
    private Integer visitTypeId;
    
    private Integer razorPayBillAmount;
    
    private String mobileNo;
    
    private String name;
    
    private String email;
    
    private String callbackUrl;
    
	private String uniqueId;
	
	private Boolean isUsed;
	
	private String paid;
	
	private Double razorAmount;
	
	private Integer razorpayOrderDetId;
	
	private String shortUrl;
	
	private String createdDate;
	
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiSecretKey() {
		return apiSecretKey;
	}

	public void setApiSecretKey(String apiSecretKey) {
		this.apiSecretKey = apiSecretKey;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getRazorPayBillAmount() {
		return razorPayBillAmount;
	}

	public void setRazorPayBillAmount(Integer razorPayAmount) {
		this.razorPayBillAmount = razorPayAmount;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Boolean getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}

	public String getPaid() {
		return paid;
	}

	public void setPaid(String paid) {
		this.paid = paid;
	}

	public Double getRazorAmount() {
		return razorAmount;
	}

	public void setRazorAmount(Double razorAmount) {
		this.razorAmount = razorAmount;
	}

	public Integer getRazorpayOrderDetId() {
		return razorpayOrderDetId;
	}

	public void setRazorpayOrderDetId(Integer razorpayOrderDetId) {
		this.razorpayOrderDetId = razorpayOrderDetId;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	
	
}

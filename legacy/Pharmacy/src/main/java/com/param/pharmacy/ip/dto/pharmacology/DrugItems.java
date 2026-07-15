 package com.param.pharmacy.ip.dto.pharmacology;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DrugItems
{
	private Integer id;
	
	private Long saleId;
	
	private BigInteger salesId;
	
	private Integer doctorId;
	
	private String doctorName;
	
	private String issueNo;
	
	private Date issueDate;
	
	private String itemCode;	
	
	private String itemName;
	
	private Double packageConsumptionAmt;
	
	private Integer quantity;
	
	private Double totalAmount;
	
	private String issueFrom;
	
	private Integer issueFromId;
	
	private Double unitPrice;
	
	private Double netAmount;
	
	private Double ordConcession;
	
	private Double ordDiscount;
	
	private Double selfPayable;
	
	private Double creditPayable;
	
	private Double taxAmount;
	
	private Integer orderId;
	
	private String issueDateString;
	
	private boolean isDummyService;
	
	private List<ReturnDetails> returnDetails;
	
	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Double getOrdConcession() {
		return ordConcession;
	}

	public void setOrdConcession(Double ordConcession) {
		this.ordConcession = ordConcession;
	}

	public Double getOrdDiscount() {
		return ordDiscount;
	}

	public void setOrdDiscount(Double ordDiscount) {
		this.ordDiscount = ordDiscount;
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

	public BigInteger getSalesId() {
		return salesId;
	}

	public void setSalesId(BigInteger salesId) {
		this.salesId = salesId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getIssueFrom() {
		return issueFrom;
	}

	public void setIssueFrom(String issueFrom) {
		this.issueFrom = issueFrom;
	}

	public Integer getIssueFromId() {
		return issueFromId;
	}

	public void setIssueFromId(Integer issueFromId) {
		this.issueFromId = issueFromId;
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

	public List<ReturnDetails> getReturnDetails() {
		return returnDetails;
	}

	public void setReturnDetails(List<ReturnDetails> returnDetails) {
		this.returnDetails = returnDetails;
	}

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

	public DrugItems() {
		super();
	}

	public DrugItems(Integer id, Long saleId, Integer doctorId, String doctorName, String issueNo, Date issueDate, String itemCode, String itemName, Double unitPrice,
			Integer quantity, Double totalAmount, String issueFrom, Integer issueFromId) {
		super();
		this.id = id;
		this.saleId = saleId;
		this.doctorId = doctorId;
		this.doctorName = doctorName;
		this.issueNo = issueNo;
		this.issueDate = issueDate;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.unitPrice = unitPrice;
		this.quantity = quantity;
		this.totalAmount = totalAmount;
		this.issueFrom = issueFrom;
		this.issueFromId = issueFromId;
	}

	public Double getPackageConsumptionAmt() {
		return packageConsumptionAmt;
	}

	public void setPackageConsumptionAmt(Double packageConsumptionAmt) {
		this.packageConsumptionAmt = packageConsumptionAmt;
	}

	public String getIssueDateString() {
		return issueDateString;
	}

	public void setIssueDateString(String issueDateString) {
		this.issueDateString = issueDateString;
	}

	public boolean getIsDummyService() {
		return isDummyService;
	}

	public void setIsDummyService(boolean isDummyService) {
		this.isDummyService = isDummyService;
	}
	
}
package com.param.entity.model.mpesa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@NamedQueries({
	
	@NamedQuery(name="GET_MPESA_DETAILS_BY_UHID",
			query="SELECT mstr.mpesaTransactionId as mpesaTransactionId, "
					+ " mstr.checkoutRequestId as checkoutRequestId, "
					+ " mstr.amount as amount, "
					+ " mstr.responseCode as responseCode, "
					+ " mstr.responseDescription as responseDescription,"
					+ " mstr.isUsed as isUsed "
					+ " FROM MpesaTransactionDetails mstr "
					+ " WHERE mstr.organizationId=:organizationId "
					+ " AND mstr.unitId=:unitId "
					+ " AND mstr.isUsed = false "
					+ " AND (mstr.uhidNumber=:uhid or mstr.checkoutRequestId=:checkoutRequestId) order by 1 desc"),
	
	@NamedQuery(name="CHECK_UNIQUE_MPESA_TRANSCODE_IN_TRANSACTION_DETAILS",
	query="SELECT mstr.mpesaTransactionId as mpesaTransactionId, "
			+ " mstr.checkoutRequestId as checkoutRequestId, "
			+ " mstr.amount as amount, "
			+ " mstr.responseCode as responseCode, "
			+ " mstr.responseDescription as responseDescription,"
			+ " mstr.isUsed as isUsed "
			+ " FROM MpesaTransactionDetails mstr "
			+ " WHERE mstr.organizationId=:orgId "
			+ " AND mstr.unitId=:unitId "
			+ " AND mstr.transactionId=:checkoutReqId "),
	
	@NamedQuery(name = "UPDATE_MPESA_TRANSCODE_IN_TRANSACTION_DETAILS", query = "UPDATE MpesaTransactionDetails mstr"
			+ "	SET mstr.isUsed= true " 
			+ " WHERE mstr.organizationId=:orgId "
			+ " AND mstr.unitId=:unitId "
			+ " AND mstr.transactionId=:checkoutReqId "),
	
})

@Entity
@Table(name = "t_mpesa_transaction_details", schema = "billing")
@SequenceGenerator(name = "t_mpesa_transaction_details_seq", sequenceName = "billing.t_mpesa_transaction_details_seq", allocationSize = 1)
public class MpesaTransactionDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_mpesa_transaction_details_seq")
	@Column(name = "mpesa_transaction_id")
	private Integer mpesaTransactionId;
	
	@Column(name = "amount ")
	private Double  amount ;
	
	@Column(name = "phone_number")
	private String phoneNumber;
	
	@Column(name = "checkout_request_id")
	private String checkoutRequestId;
	
	@Column(name = "merchant_request_id")
	private String merchantRequestId;
	
	@Column(name = "party_a")
	private String partyA;
	
	@Column(name = "response_code")
	private String responseCode;
	
	@Column(name = "customer_message")
	private String customerMessage;
	
	@Column(name = "response_description")
	private String responseDescription;
	
	@Column(name = "result_code")
	private String resultCode;
	
	@Column(name = "result_desc")
	private String resultDesc;
	
	@Column(name = "uhid_number")
	private String uhidNumber;
	
	@Column(name = "is_used")
	private boolean  isUsed;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="status")
	private char status;
	
	@Column(name = "transaction_id")
	private String transactionId;

	public Integer getMpesaTransactionId() {
		return mpesaTransactionId;
	}

	public void setMpesaTransactionId(Integer mpesaTransactionId) {
		this.mpesaTransactionId = mpesaTransactionId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCheckoutRequestId() {
		return checkoutRequestId;
	}

	public void setCheckoutRequestId(String checkoutRequestId) {
		this.checkoutRequestId = checkoutRequestId;
	}

	public String getMerchantRequestId() {
		return merchantRequestId;
	}

	public void setMerchantRequestId(String merchantRequestId) {
		this.merchantRequestId = merchantRequestId;
	}

	public String getPartyA() {
		return partyA;
	}

	public void setPartyA(String partyA) {
		this.partyA = partyA;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getCustomerMessage() {
		return customerMessage;
	}

	public void setCustomerMessage(String customerMessage) {
		this.customerMessage = customerMessage;
	}

	public String getResponseDescription() {
		return responseDescription;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	
}

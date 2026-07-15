package com.param.billing.global.transaction.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.DateConverter;

@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_PAYMENT_RECEIPTS_FOR_ADDENDUM_BY_BILL",
					 query = "select prm.payment_reciept_id as \"paymentRecieptId\", prm.orgnization_id as \"orgnizationId\", prm.unit_id as \"unitId\", "
							 + " prm.billing_master_id as \"billingMasterId\", prm.amount as \"amount\", prm.reciept_number as \"recieptNumber\",  "
							 + " prm.payee_id as \"payeeId\" "
							 + " from billing.t_payment_reciept_master prm "
							 + " where prm.billing_master_id = :billId "),
	
	@NamedNativeQuery(name = "UPDATE_PAYMENT_RECEIPT_AS_CANCEL_FOR_ADDENDUM_BILL",
					 query = "update billing.t_payment_reciept_master "
					 		+ "set is_cancelled = 'Y', status = 'I', cancelled_by=:cancelledBy, cancel_remarks=:remarks "
					 		+ "where payment_reciept_id =:receiptId "),
	
	@NamedNativeQuery(name="GET_CHEQUE_LIST",
	 query ="select prm.reciept_number as \"recieptNumber\", "
	 		+"prd.payment_reciept_details_id as \"paymentRecieptDetailsId\", "
	 		+ "to_char(prm.created_date,'dd/mm/yyyy') as \"createdDate\", "
			//+ "to_char(prm.created_date,'YYYY-MM-DD') as \"createdDate\", "
			 + "concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name), coalesce(pr.last_name)) as \"patientName\", "
			 + "pr.uhid_number as \"uhidNumber\", "
			 + "prd.amount as \"amount\", "
			 + "prd.check_number as \"chequeNumber\", "
			 + "to_char(prd.cheque_date,'dd/mm/yyyy') as \"chequeDate\", "
			 + "prd.bank_id as \"bankId\", "
			 + "bnkm.bank_name as \"bankName\" "
			 +"from billing.t_payment_reciept_master prm "
			 + "inner join billing.t_payment_reciept_details prd on prm.payment_reciept_id=prd.payment_receipt_master_id "
			 + "inner join billing.m_billing_master bm on prm.billing_master_id=bm.billing_master_id and bm.bill_cancelled = 'N' "
			 + "inner join patient.t_patient_registration pr on bm.patient_id=pr.patient_id "
			 + " left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
			 + "left join billing.m_bank_master bnkm on bnkm.bank_id=prd.bank_id "
			 + "where prd.payment_mode_id=2 and prd.interim_cheque_status='A' and prm.date_of_reciept BETWEEN :cashOpeingDate AND :currentDate "
			 +"and prm.orgnization_id= :organizationId and prm.unit_id= :unitId "
			 +"and prm.created_by= :createdBy order by reciept_number "
		//	 + "where prd.payment_mode_id=2 "
		//	 +"and prd.interim_cheque_status='A' "
			// +"and prm.date_of_reciept= :createdDate "
		//	 +"order by reciept_number"
			),
	
	@NamedNativeQuery(name="GET_CARD_TRANSACTION_LIST",
	 query ="select prd.card_number as \"cardNumber\", "
	 		+"prd.payment_reciept_details_id as \"paymentRecieptDetailsId\", "
			 +"ctm.card_type_name as \"cardName\", "
			 +"prd.transaction_no as \"transactionNo\", "
			 +"concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name), coalesce(pr.last_name)) as \"patientName\", "
			 +"pr.uhid_number as \"uhidNumber\", prd.amount as \"amount\" "
			 +"from billing.t_payment_reciept_master prm "
			 +"inner join billing.t_payment_reciept_details prd on prm.payment_reciept_id=prd.payment_receipt_master_id "
			 +"inner join billing.m_billing_master bm on prm.billing_master_id=bm.billing_master_id and bm.bill_cancelled = 'N' "
			 +"inner join patient.t_patient_registration pr on bm.patient_id=pr.patient_id "
			 + "left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
			 +"left join billing.m_card_type_master ctm on ctm.card_type_id=prd.card_type_id "
			 + "where prd.payment_mode_id=3 and prd.interim_cheque_status='A' and prm.date_of_reciept BETWEEN :cashOpeingDate AND :currentDate "
			 +"and prm.orgnization_id= :organizationId and prm.unit_id= :unitId "
			 +"and prm.created_by= :createdBy order by reciept_number "),
			// +"where prd.payment_mode_id=3 and prd.interim_card_status='A'-- and prm.created_date ='2018-08-24 18:08:09'  order by prd.transaction_no"),
	
	@NamedNativeQuery(name="UPDATE_INTERIM_CHEQUE_STATUS",
	 query ="UPDATE billing.t_payment_reciept_details "
	 		+"SET interim_cheque_status= 'D' "
	 		+"WHERE payment_reciept_details_id= :paymentRecieptDetailsId "
	 		+ "AND orgnization_id= :orgnizationId "
	 		+"AND unit_id= :unitId"),
	
	@NamedNativeQuery(name="UPDATE_PAYMENT_RECIEIPT_BY_ID",
	 query ="UPDATE billing.t_payment_reciept_details "
	 		+"SET exported_to_shift_close= true "
	 		+"WHERE payment_reciept_details_id= :paymentRecieptDetailsId "
	 		+"AND orgnization_id= :orgnizationId "
	 		+"AND unit_id= :unitId "),
	
	@NamedNativeQuery(name="UPDATE_INTERIM_CARD_STATUS",
	 query ="UPDATE billing.t_payment_reciept_details "
	 		+"SET interim_card_status= 'D' "
	 		+"WHERE payment_reciept_details_id= :paymentRecieptDetailsId "
	 		+"AND orgnization_id= :orgnizationId "
	 		+"AND unit_id= :unitId"),
	
	@NamedNativeQuery(name="GET_PAYMENT_RECIEPTS_AGAINST_BILL",
	 query ="SELECT prm.payment_reciept_id as \"receiptId\" , "
	 		+ "bm.billing_master_id as \"billingMasterId\" , "
	 		+ "bm.bill_number as \"billNumber\", "
	 		+ "pdc.dep_consumption_id as \"depositConsumptionId\", "
	 		+ "reciept_number as \"receiptNumber\", "
	 		+ "prm.amount as \"amount\", "
	 		+ "pdc.deposite_consumption_number as \"depositeConsumptionNumber\", "
	 		+ "pdc.consumed_amount as \"consumedAmount\",  "
	 		+ " bm.net_amount as \"totalBillAmount\", "
	 		+ "to_char(prm.date_of_reciept,'dd/mm/yyyy') as \"dateOfReciept\", "
	 		+ "	prm.is_print as \"isPrint\" " 
			+"FROM billing.m_billing_master bm  "
			+"left join billing.t_payment_reciept_master prm on bm.billing_master_id=prm.billing_master_id "
			+"left join billing.t_payment_deposite_consumption pdc on prm.deposite_consumption_id=pdc.dep_consumption_id "
			+"where bm.billing_master_id=:billId and prm.status ='A' "),
	
	
	@NamedNativeQuery(name="GET_PHARMACY_PAYMENT_RECIEPTS_AGAINST_BILL",
	 query ="select tspm.amount as \"amount\", "  + 
	 		"  to_char(tspm.added_date,'dd/mm/yyyy')  as \"dateOfReciept\", "  + 
	 		"tspm.id as \"paymentRecieptId\", "  + 
	 		"ts.bill_no as \"billNumber\", "  + 
	 		"ts.net_amount as \"totalBillAmount\", "
	 		+ " 1 as \"receiptType\","  + 
	 		"'' as \"depositeConsumptionNumber\" ,"
	 		+ " tspm.is_deleted as \"delete\" ,"  + 
	 		" 'A' as \"status\" "+
	 		"from pharmacy.t_sale_payment_mode tspm  "  + 
	 		"inner join pharmacy.t_sale ts  on ts.id = tspm .sale_id  "  + 
	 		"where ts.id = :saleId  "  + 
	 		" union   "  + 
	 		"select "  + 
	 		"tpdc.consumed_amount as \"amount\", "  + 
	 		" to_char(tpdc.dep_consumption_date,'dd/mm/yyyy')  as \"dateOfReciept\", "  + 
	 		"tpdc.dep_consumption_id as \"paymentRecieptId\", "  + 
	 		"ts.bill_no as \"billNumber\", "  + 
	 		"ts.net_amount as \"totalBillAmount\", "   
	 		+ "2 as \"receiptType\","  + 
	 		"tpdc.deposite_consumption_number as \"depositeConsumptionNumber\", "  + 
	 		" false as \"delete\","
	 		+ " tpdc.status as \"status\" "  + 
	 		"from billing.t_payment_deposite_consumption tpdc  "  + 
	 		"inner join pharmacy.t_sale ts  on ts.id = tpdc.sale_id  "  + 
	 		"where ts.id = :saleId  ")
	
	
})

@Entity
@Table(name="t_payment_reciept_master", schema="billing")
@SequenceGenerator(name="payment_reciept_master_seq", sequenceName="billing.payment_reciept_master_seq", allocationSize=1)
public class PaymentRecieptMaster {
	
	@Id
	@Column(name = "payment_reciept_id")
 	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="payment_reciept_master_seq")
	private Integer paymentRecieptId;
	
	@Column(name = "orgnization_id")
	private Integer orgnizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "billing_master_id")
	private Integer billingMasterId;
	  
	@Column(name="amount")
	private Double amount;
	
	@Column(name = "reciept_number")
	private String recieptNumber;
	
	@Column(name = "date_of_reciept")
	private Date dateOfReciept;
	
	@Column(name="receipt_type")
	private Integer receiptType;
	 
	@Column(name="counter_id")
	private Integer counterId;
	
	@Column(name="settlement_id")
	private Integer settlementId;
	
	@Column(name="payee_id")
	private Integer payeeId;
	
	@Column(name="received_from")
	private String receivedFrom;
	
	@Column(name="identification_number")
	private String identificationNumber;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="is_printed")
	private Character isPrinted;
	
	@Column(name="is_financial_clearance")
	private Character isFinancialClearance;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "is_cancelled")
	private Character isCancelled;
	
	@Column(name = "cancelled_by")
	private Integer cancelledBy;
	
	@Column(name = "cancelled_reason_id")
	private Integer cancelledReasonId;
	
	@Column(name = "cancel_remarks")
	private String cancelRemarks;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "deposite_consumption_id")
	private Integer depositeConsumptionId;
	
	@Column(name = "signeture_image")
	private String signetureImage;
	
	@Column(name = "signature")
	private byte[] signature;
	
	@Column(name = "against_disallowed_amount")
	private Character againstDisallowedAmount;
	
	@Column(name="company_invoice_id ")
	private Integer companyInvoiceId;
	
	@Column(name = "reciept_cancelled")
	private Character recieptCancelled;
	
	@Column(name = "reciept_cancel_reason_id")
	private Integer recieptCancelReasonId;

	@Column(name = "reciept_cancel_datetime")
	@Convert(converter = DateConverter.class)
	private Long recieptCancelDatetime;

	@Column(name = "reciept_cancel_by")
	private Integer recieptCancelBy;

	@Column(name = "reciept_cancel_remark")
	private String recieptCancelRemark;
	
	public Character getRecieptCancelled() {
		return recieptCancelled;
	}

	public void setRecieptCancelled(Character recieptCancelled) {
		this.recieptCancelled = recieptCancelled;
	}

	public Integer getRecieptCancelReasonId() {
		return recieptCancelReasonId;
	}

	public void setRecieptCancelReasonId(Integer recieptCancelReasonId) {
		this.recieptCancelReasonId = recieptCancelReasonId;
	}

	public Long getRecieptCancelDatetime() {
		return recieptCancelDatetime;
	}

	public void setRecieptCancelDatetime(Long recieptCancelDatetime) {
		this.recieptCancelDatetime = recieptCancelDatetime;
	}

	public Integer getRecieptCancelBy() {
		return recieptCancelBy;
	}

	public void setRecieptCancelBy(Integer recieptCancelBy) {
		this.recieptCancelBy = recieptCancelBy;
	}

	public String getRecieptCancelRemark() {
		return recieptCancelRemark;
	}

	public void setRecieptCancelRemark(String recieptCancelRemark) {
		this.recieptCancelRemark = recieptCancelRemark;
	}

	public Integer getCompanyInvoiceId() {
		return companyInvoiceId;
	}

	public void setCompanyInvoiceId(Integer companyInvoiceId) {
		this.companyInvoiceId = companyInvoiceId;
	}

	public Character getAgainstDisallowedAmount() {
		return againstDisallowedAmount;
	}

	public void setAgainstDisallowedAmount(Character againstDisallowedAmount) {
		this.againstDisallowedAmount = againstDisallowedAmount;
	}

	public Integer getDepositeConsumptionId() {
		return depositeConsumptionId;
	}

	public void setDepositeConsumptionId(Integer depositeConsumptionId) {
		this.depositeConsumptionId = depositeConsumptionId;
	}

	public Integer getPaymentRecieptId() {
		return paymentRecieptId;
	}

	public void setPaymentRecieptId(Integer paymentRecieptId) {
		this.paymentRecieptId = paymentRecieptId;
	}

	public Integer getOrgnizationId() {
		return orgnizationId;
	}

	public void setOrgnizationId(Integer orgnizationId) {
		this.orgnizationId = orgnizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getBillingMasterId() {
		return billingMasterId;
	}

	public void setBillingMasterId(Integer billingMasterId) {
		this.billingMasterId = billingMasterId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getRecieptNumber() {
		return recieptNumber;
	}

	public void setRecieptNumber(String recieptNumber) {
		this.recieptNumber = recieptNumber;
	}

	public Date getDateOfReciept() {
		return dateOfReciept;
	}

	public void setDateOfReciept(Date dateOfReciept) {
		this.dateOfReciept = dateOfReciept;
	}

	public Integer getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Integer receiptType) {
		this.receiptType = receiptType;
	}

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public Integer getSettlementId() {
		return settlementId;
	}

	public void setSettlementId(Integer settlementId) {
		this.settlementId = settlementId;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public String getReceivedFrom() {
		return receivedFrom;
	}

	public void setReceivedFrom(String receivedFrom) {
		this.receivedFrom = receivedFrom;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Character getIsPrinted() {
		return isPrinted;
	}

	public void setIsPrinted(Character isPrinted) {
		this.isPrinted = isPrinted;
	}

	public Character getIsFinancialClearance() {
		return isFinancialClearance;
	}

	public void setIsFinancialClearance(Character isFinancialClearance) {
		this.isFinancialClearance = isFinancialClearance;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Character getIsCancelled() {
		return isCancelled;
	}

	public void setIsCancelled(Character isCancelled) {
		this.isCancelled = isCancelled;
	}

	public Integer getCancelledBy() {
		return cancelledBy;
	}

	public void setCancelledBy(Integer cancelledBy) {
		this.cancelledBy = cancelledBy;
	}

	public Integer getCancelledReasonId() {
		return cancelledReasonId;
	}

	public void setCancelledReasonId(Integer cancelledReasonId) {
		this.cancelledReasonId = cancelledReasonId;
	}

	public String getCancelRemarks() {
		return cancelRemarks;
	}

	public void setCancelRemarks(String cancelRemarks) {
		this.cancelRemarks = cancelRemarks;
	}

	public String getSignetureImage() {
		return signetureImage;
	}

	public void setSignetureImage(String signetureImage) {
		this.signetureImage = signetureImage;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}


}

package com.param.billing.global.model;


import com.param.adt.master.global.model.PaymentModeMaster;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@NamedNativeQueries({


        @NamedNativeQuery(name = "GET_FRANCISE_DEPOSIT_DETAILS", query = "select "
                + " 	paymentmode.payment_mode_desc as \"paymentMode\", "
                + " 	bankmaster.bank_name as \"bankName\", "
                + " 	paymentdetails.cheque_number as \"chequeNumber\", "
                + " 	paymentdetails.card_no as \"cardNo\", "
                + " 	cast(coalesce(paymentdetails.amount,0) as numeric(10,2)) as \"totalAmount\" "
                + " from "
                + " 	billing.t_franchise_deposit_details paymentdetails "
                + " inner join public.m_payment_mode_master paymentmode on "
                + " 	paymentdetails.payment_mode_id = paymentmode.payment_mode_id "
                + " left outer join billing.m_bank_master bankmaster on "
                + " 	paymentdetails.bank_id = bankmaster.bank_id "
                + " where "
                + " 	paymentdetails.fr_deposit_id =:frDepositId "
                + "	ORDER BY paymentdetails.fr_deposit_detail_id ASC"),
})

@Entity
@Table(name = "t_franchise_deposit_details" , schema = "billing")
@SequenceGenerator(name = "t_franchise_deposit_details_seq" , sequenceName = "billing.t_franchise_deposit_details_seq" , allocationSize = 1)
public class FranchiseDepositDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_franchise_deposit_details_seq")
    @Column(name = "fr_deposit_detail_id")
    private int frDepositDetailId;

    @Column(name = "fr_deposit_id")
    private Integer frDepositId;

    @Column(name = "payment_mode_id")
    private Integer paymentModeId;

    @Column(name = "amount")
    private double amount;

    @Column(name = "bank_id")
    private Integer bankId;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "cheque_number")
    private String chequeNumber;

    @Column(name = "card_no")
    private String cardNo;

    @Column(name = "card_holder_name")
    private String cardHolderName;

    @Column(name = "expiry_date")
    private Date expiryDate;

    @Column(name = "card_type_id")
    private Integer cardTypeId;

    @Column(name = "cheque_date")
    private Date chequeDate;

    @Column(name="consumed_amount")
    private Double consumedAmount;

    @Column(name="balance_amount")
    private Double balanceAmount;

    @Column(name = "refund_amount")
    private double refundAmount;

    @CreationTimestamp
    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "created_by")
    private Integer createdBy;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "hospital_account_id")
    private Integer hospitalAccountId;

    @Column(name = "payment_reciept_details_id")
    private Integer paymentRecieptDetailsId;

    @Column(name = "transaction_type_id")
    private Integer transactionTypeId;

    @Column(name = "transaction_no")
    private String transactionNo;

    @Column(name = "trans_transaction_date")
    private Date transTransactionDate;

    @Column(name="is_mpesa_validation")
    private boolean skipMpesaValidation;

    @Column(name="device_name")
    private String deviceName;

    @Column(name="transaction_details")
    private String transactionDetails;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deposit_id" , insertable = false , updatable = false , nullable = false)
    private DepositMaster depositMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_mode_id" , insertable = false , updatable = false , nullable = false)
    private PaymentModeMaster paymentModeMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id" , insertable = false , updatable = false , nullable = false)
    private BankMaster bankMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_type_id" , insertable = false , updatable = false , nullable = false)
    private CardTypeMaster cardTypeMaster;

    public int getFrDepositDetailId() {
        return frDepositDetailId;
    }

    public void setFrDepositDetailId(int frDepositDetailId) {
        this.frDepositDetailId = frDepositDetailId;
    }

    public Integer getFrDepositId() {
        return frDepositId;
    }

    public void setFrDepositId(Integer frDepositId) {
        this.frDepositId = frDepositId;
    }

    public Integer getPaymentModeId() {
        return paymentModeId;
    }

    public void setPaymentModeId(Integer paymentModeId) {
        this.paymentModeId = paymentModeId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getChequeNumber() {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(Integer cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    public Date getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(Date chequeDate) {
        this.chequeDate = chequeDate;
    }

    public Double getConsumedAmount() {
        return consumedAmount;
    }

    public void setConsumedAmount(Double consumedAmount) {
        this.consumedAmount = consumedAmount;
    }

    public Double getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(Double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
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

    public Integer getHospitalAccountId() {
        return hospitalAccountId;
    }

    public void setHospitalAccountId(Integer hospitalAccountId) {
        this.hospitalAccountId = hospitalAccountId;
    }

    public Integer getPaymentRecieptDetailsId() {
        return paymentRecieptDetailsId;
    }

    public void setPaymentRecieptDetailsId(Integer paymentRecieptDetailsId) {
        this.paymentRecieptDetailsId = paymentRecieptDetailsId;
    }

    public Integer getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(Integer transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public Date getTransTransactionDate() {
        return transTransactionDate;
    }

    public void setTransTransactionDate(Date transTransactionDate) {
        this.transTransactionDate = transTransactionDate;
    }

    public boolean isSkipMpesaValidation() {
        return skipMpesaValidation;
    }

    public void setSkipMpesaValidation(boolean skipMpesaValidation) {
        this.skipMpesaValidation = skipMpesaValidation;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public DepositMaster getDepositMaster() {
        return depositMaster;
    }

    public void setDepositMaster(DepositMaster depositMaster) {
        this.depositMaster = depositMaster;
    }

    public PaymentModeMaster getPaymentModeMaster() {
        return paymentModeMaster;
    }

    public void setPaymentModeMaster(PaymentModeMaster paymentModeMaster) {
        this.paymentModeMaster = paymentModeMaster;
    }

    public BankMaster getBankMaster() {
        return bankMaster;
    }

    public void setBankMaster(BankMaster bankMaster) {
        this.bankMaster = bankMaster;
    }

    public CardTypeMaster getCardTypeMaster() {
        return cardTypeMaster;
    }

    public void setCardTypeMaster(CardTypeMaster cardTypeMaster) {
        this.cardTypeMaster = cardTypeMaster;
    }

	public String getTransactionDetails() {
		return transactionDetails;
	}

	public void setTransactionDetails(String transactionDetails) {
		this.transactionDetails = transactionDetails;
	}
    
    
}

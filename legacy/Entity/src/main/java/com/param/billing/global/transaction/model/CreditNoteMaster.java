package com.param.billing.global.transaction.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedNativeQueries({
		@NamedNativeQuery(name = "GENERATE_CREDIT_NOTE_NO", query = "select nextval('billing.seq_credit_note_no');"),
		@NamedNativeQuery(name = "GET_CREDIT_NOTE_LIST_FOR_REFUND", query = "select "
				+ "  crnotemst.credit_note_id as \"creditNoteId\", " 
				+ "  crnotemst.bill_id as \"billMasterId\", "
				+ "  crnotemst.bill_payee_id as \"billPayeeId\", " 
				+ "  crnotemst.credit_note_remark as \"creditNoteRemark\", " 
				+ "  billmst.patient_id as \"patientId\", "
				+ "  crnotemst.credit_note_no as \"creditNoteNo\", "
				+ "  to_char(crnotemst.credit_note_date,'DD-MM-YYYY HH24:MI:SS') as \"creditNoteDate\", "
				+ "  pat.uhid_number as \"uhidNumber\", " 
				+ "  billmst.visit_type_id as \"visitTypeId\", "
				+ "  billmst.encounter_id as \"encounterId\", " 
				+ "  billmst.admission_id as \"admissionId\", "
				+ "  bill_mst.bill_number as \"invoiceNo\", "
				+ "  crnotemst.cr_net_amount as \"crAmount\", " 
				+ "  coalesce(crnotemst.cr_deu_amount,0) as \"crDueAmount\", "
				+ "  concat(pat.first_name, ' ', pat.last_name) as \"patientName\", "
				+ "  pat.mobile_number as \"mobileNo\", "
				+ "  crnotemst.is_approved as \"isApproved\", "
				+ "  crnotemst.cancel_or_discount as \"cancelOrDiscount\", "
				+ "  concat(coalesce(empPre.prefix_code,''),' ',emp_mst.first_name, ' ', emp_mst.last_name) as \"userName\", " 
				+ "  coalesce( "
				+ "    case when billmst.visit_type_id = 1 then( " 
				+ "      select " + "        enc.encounter_number "
				+ "      from " + "        opd.t_encounter_master enc " 
				+ "      where "
				+ "        enc.encounter_id = billmst.encounter_id " 
				+ "    ) end, "
				+ "    case when billmst.visit_type_id = 2 " 
				+ "    or billmst.visit_type_id = 3 "
				+ "    or billmst.visit_type_id = 4 then( "
				+ "      select " 
				+ "        adm.admission_number " 
				+ "      from " 
				+ "        adt.t_admission adm "
				+ "      where " 
				+ "        adm.admission_id = billmst.admission_id "
				+ "    ) end,'-') as \"visitNo\", "
				+ "(case "
				+ "		when (coalesce(crnotemst.cr_net_amount,	0) = coalesce(crnotemst.cr_deu_amount,0) and crnotemst.status = 'A') then 1 "
				+ "		when (cast(coalesce(crnotemst.cr_deu_amount, 0) as numeric(20,2)) = 0 and crnotemst.status = 'A') then 3 "
				+ "		when  crnotemst.status = 'I' then 4 else 2 "
				+ "	end) as \"status\", "
				+ "  crnotemst.self_payable as \"selfPayable\", "
				+ "  crnotemst.credit_payable as \"creditPayable\", "
				+ " (case when (coalesce(tbpd.bill_amount-tbpd.paid_amount-tbpd.cr_net_amt, 0)) < 0 then false else true end) as \"disableCancellation\" "
				+ " from " 
				+ "  billing.m_credit_note_master crnotemst "
				+ "  inner join billing.m_billing_master billmst on billmst.billing_master_id = crnotemst.bill_id and billmst.bill_cancelled = 'N' "
				+ "  inner join patient.t_patient_registration pat on pat.patient_id = billmst.patient_id "
				+ "  left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
				+ "  left join billing.m_billing_master bill_mst on bill_mst.billing_master_id = crnotemst.bill_id and bill_mst.bill_cancelled = 'N' "
				+ "  left join public.m_employee_master emp_mst on emp_mst.user_id = crnotemst.created_by " 
				+ "  left join m_prefix_master empPre on empPre.prefix_id = emp_mst.prefix_id "
				+ "	 inner join billing.t_bill_payee_details tbpd on tbpd.bill_payee_details_id = crnotemst.bill_payee_details_id "
				+ " where "
				+ "  crnotemst.organisation_id = :orgId " 
				+ "  and crnotemst.unit_id = :unitId "
				+ " and crnotemst.is_approved != 'R'"
				),

		@NamedNativeQuery(name = "UPDATE_CR_DUE_AMOUNT_BY_CREDIT_NOTE", query = "update billing.m_credit_note_master "
				+ " set cr_deu_amount = coalesce(cr_deu_amount,0) - :crDeuAmount, "
				+ " self_payable = coalesce(self_payable,0) - :crDeuAmount,received_from=:receivedFrom "
				+ " where credit_note_id=:creditNoteId "),
		
		@NamedNativeQuery(name = "GET_CREDIT_NOTES_BY_BILL_FOR_ADDENDUM",
						 query = "select cnm.credit_note_no as \"creditNoteNo\", to_char(cnm.credit_note_date,'DD-MM-YYYY') as \"creditNoteDate\", cnm.credit_note_id as \"creditNoteId\", "
								 + " cnm.bill_id as \"billId\", cnm.cr_net_amount as \"creditNoteAmount\", (select coalesce(sum(prm.amount_refunded),0) from billing.t_payment_refund_master prm  "
								 + " where prm.credit_note_id = cnm.credit_note_id) as \"amountPaid\" "
								 + " from billing.m_credit_note_master cnm "
								 + " where cnm.bill_id = :billId ")

})
@Entity
@Table(name = "m_credit_note_master", schema = "billing")
@SequenceGenerator(name = "credit_note_master_seq", sequenceName = "billing.credit_note_master_seq", allocationSize = 1)
public class CreditNoteMaster {

	@Id
	@Column(name = "credit_note_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credit_note_master_seq")
	private int creditNoteId;

	@Column(name = "credit_note_date")
	private Date creditNoteDate;

	@Column(name = "bill_id")
	private Integer billId;

	@Column(name = "bill_payee_id")
	private Integer billPayeeId;

	@Column(name = "cr_note_against")
	private Integer crNoteAgainst;

	@Column(name = "credit_note_no")
	private String creditNoteNo;

	@Column(name = "cr_total_amount")
	private double crTotalAmount;

	@Column(name = "cr_tax_amount")
	private double crTaxAmount;

	@Column(name = "cr_net_amount")
	private double crNetAmount;

	@Column(name = "cr_out_adjustment_amount")
	private double crOutAdjustmentAmount;

	@Column(name = "cr_deu_amount")
	private double crDeuAmount;

	@Column(name = "cr_reason_id")
	private Integer crReasonId;

	@Column(name = "ce_remark")
	private String ceRemark;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organisation_id")
	private Integer orgnisationId;

	@Column(name = "cancel_or_discount")
	private Integer cancelOrDiscount;

	@Column(name = "status")
	private char status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "counter_id")
	private Integer counterId;
	
	@Column(name = "is_approved")
	private char isApproved;
	
	@Column(name = "discount_roundoff")
	private double discountRoundoff;
	
	@Column(name = "self_payable")
    private BigDecimal selfPayable;
	
	@Column(name = "credit_payable")
	private BigDecimal creditPayable;
	
	@Column(name = "package_consumption_amt")
	private BigDecimal packageConsumptionAmt;
	
	@Column(name="credit_note_remark")
	private String creditNoteRemark;
	
	@Column(name="bill_payee_details_id")
	private Integer billPayeeDetailsId;

	@Column(name = "sign_roundoff")
	private Integer signRoundOff;

	@Column(name="associate_company_id")
	private Integer associateCompanyId;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "creditNoteMaster")
	private List<ActiveDepositeCreditNoteMapper> listActiveDepositeCreditNoteMapper;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "creditNoteMaster")
	private List<CreditNoteDetails> listCreditNoteDetails;

	public Integer getBillPayeeDetailsId() {
		return billPayeeDetailsId;
	}

	public void setBillPayeeDetailsId(Integer billPayeeDetailsId) {
		this.billPayeeDetailsId = billPayeeDetailsId;
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

	public int getCreditNoteId() {
		return creditNoteId;
	}

	public void setCreditNoteId(int creditNoteId) {
		this.creditNoteId = creditNoteId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrgnisationId() {
		return orgnisationId;
	}

	public void setOrgnisationId(Integer orgnisationId) {
		this.orgnisationId = orgnisationId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'A' : status;
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

	public List<ActiveDepositeCreditNoteMapper> getListActiveDepositeCreditNoteMapper() {
		return listActiveDepositeCreditNoteMapper;
	}

	public void setListActiveDepositeCreditNoteMapper(
			List<ActiveDepositeCreditNoteMapper> listActiveDepositeCreditNoteMapper) {
		this.listActiveDepositeCreditNoteMapper = listActiveDepositeCreditNoteMapper;
	}

	public String getCreditNoteNo() {
		return creditNoteNo;
	}

	public void setCreditNoteNo(String creditNoteNo) {
		this.creditNoteNo = creditNoteNo;
	}

	public Date getCreditNoteDate() {
		return creditNoteDate;
	}

	public void setCreditNoteDate(Date creditNoteDate) {
		this.creditNoteDate = creditNoteDate;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Integer getBillPayeeId() {
		return billPayeeId;
	}

	public void setBillPayeeId(Integer billPayeeId) {
		this.billPayeeId = billPayeeId;
	}

	public Integer getCrNoteAgainst() {
		return crNoteAgainst;
	}

	public void setCrNoteAgainst(Integer crNoteAgainst) {
		this.crNoteAgainst = crNoteAgainst;
	}

	public double getCrTotalAmount() {
		return crTotalAmount;
	}

	public void setCrTotalAmount(double crTotalAmount) {
		this.crTotalAmount = crTotalAmount;
	}

	public double getCrTaxAmount() {
		return crTaxAmount;
	}

	public void setCrTaxAmount(double crTaxAmount) {
		this.crTaxAmount = crTaxAmount;
	}

	public double getCrNetAmount() {
		return crNetAmount;
	}

	public void setCrNetAmount(double crNetAmount) {
		this.crNetAmount = crNetAmount;
	}

	public double getCrOutAdjustmentAmount() {
		return crOutAdjustmentAmount;
	}

	public void setCrOutAdjustmentAmount(double crOutAdjustmentAmount) {
		this.crOutAdjustmentAmount = crOutAdjustmentAmount;
	}

	public double getCrDeuAmount() {
		return crDeuAmount;
	}

	public void setCrDeuAmount(double crDeuAmount) {
		this.crDeuAmount = crDeuAmount;
	}

	public Integer getCrReasonId() {
		return crReasonId;
	}

	public void setCrReasonId(Integer crReasonId) {
		this.crReasonId = crReasonId;
	}

	public String getCeRemark() {
		return ceRemark;
	}

	public void setCeRemark(String ceRemark) {
		this.ceRemark = ceRemark;
	}

	public List<CreditNoteDetails> getListCreditNoteDetails() {
		return listCreditNoteDetails;
	}

	public void setListCreditNoteDetails(List<CreditNoteDetails> listCreditNoteDetails) {
		this.listCreditNoteDetails = listCreditNoteDetails;
	}

	public Integer getCancelOrDiscount() {
		return cancelOrDiscount;
	}

	public void setCancelOrDiscount(Integer cancelOrDiscount) {
		this.cancelOrDiscount = cancelOrDiscount;
	}

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public char getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(char isApproved) {
		this.isApproved = (isApproved == '\u0000') ? 'N' : isApproved;
	}

	public double getDiscountRoundoff() {
		return discountRoundoff;
	}

	public void setDiscountRoundoff(double discountRoundoff) {
		this.discountRoundoff = discountRoundoff;
	}

	public String getCreditNoteRemark() {
		return creditNoteRemark;
	}

	public void setCreditNoteRemark(String creditNoteRemark) {
		this.creditNoteRemark = creditNoteRemark;
	}

	public Integer getSignRoundOff() {
		return signRoundOff;
	}

	public void setSignRoundOff(Integer signRoundOff) {
		this.signRoundOff = signRoundOff;
	}

	public Integer getAssociateCompanyId() {
		return associateCompanyId;
	}

	public void setAssociateCompanyId(Integer associateCompanyId) {
		this.associateCompanyId = associateCompanyId;
	}
}

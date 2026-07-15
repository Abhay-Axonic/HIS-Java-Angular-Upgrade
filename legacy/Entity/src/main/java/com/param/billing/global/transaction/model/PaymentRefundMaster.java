package com.param.billing.global.transaction.model;

import java.util.Date;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.PatientRegistration;

@NamedNativeQueries({
	 @NamedNativeQuery(name = "GENERATE_REFUND_VOUCHER_NO", query = "select nextval('billing.t_refund_voucher_no');"),
	 
	 
	 @NamedNativeQuery(name = "GET_REFUND_MASTER_LIST", query = "select "
				+ "  paymnt_refnd_mast.payment_refund_id as \"paymentRefundVoucherId\", "
				+ "  paymnt_refnd_mast.t_refund_cn_id as \"tRefundCnId\", "
				+ "  refund_cn.deposit_id as \"depositId\", "
				+ "  paymnt_refnd_mast.payment_refund_voucher_no as \"paymentRefundVoucherNo\", "
				+ "  to_char(paymnt_refnd_mast.date_of_refund,'DD-MM-YYYY HH24:MI:SS' "
				+ "  ) as \"dateOfRefund\", "
				+ "  refund_cn.refund_cn_no as \"refundCnNo\", "
				+ "  to_char(refund_cn.refund_cn_date,'DD-MM-YYYY HH24:MI:SS' "
				+ "  ) as \"refundCnDate\", "
				+ "  pat.uhid_number as \"uhidNumber\", "
				+ "  depositmst.visit_type_id as \"visitTypeId\", "
				+ "  depositmst.deposit_type_id as \"depositTypeId\", "
				+ "  depositmst.encounter_id as \"encounterId\", "
				+ "  depositmst.admission_id as \"admissionId\", "
				+ "  paymnt_refnd_mast.amount_refunded as \"amountRefunded\", "
				+ "  refund_cn.refund_amount as \"refundAmount\", "
				+ "  concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.last_name) as \"patientName\", "
				+ "  pat.mobile_number as \"mobileNo\", "
				+ "  concat(emp_mst.first_name, ' ', emp_mst.last_name) as \"userName\", "
				+ "  coalesce( "
				+ "    case when depositmst.visit_type_id = 1 then( "
				+ "      select "
				+ "        enc.encounter_number "
				+ "      from "
				+ "        opd.t_encounter_master enc "
				+ "      where "
				+ "        enc.encounter_id = depositmst.encounter_id "
				+ "    ) end, "
				+ "    case when depositmst.visit_type_id = 2 "
				+ "    or depositmst.visit_type_id = 3 then( "
				+ "      select "
				+ "        adm.admission_number "
				+ "      from "
				+ "        adt.t_admission adm "
				+ "      where "
				+ "        adm.admission_id = depositmst.admission_id "
				+ "    ) end, "
				+ "    '-' "
				+ "  ) as \"visitNo\" "
				+ "from "
				+ "  billing.t_payment_refund_master paymnt_refnd_mast "
				+ "  inner join billing.t_refund_cn refund_cn on paymnt_refnd_mast.t_refund_cn_id = refund_cn.t_refund_cn_id "
				+ "  inner join billing.m_deposit_master depositmst on depositmst.deposit_master_id = refund_cn.deposit_id "
				+ "  inner join patient.t_patient_registration pat on pat.patient_id = paymnt_refnd_mast.patient_id "
				+ "	 left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
				+ "  left join public.m_employee_master emp_mst on emp_mst.user_id = paymnt_refnd_mast.created_by "
				+ "where "
				+ "  paymnt_refnd_mast.orgnisation_id = :orgId "
				+ "  and paymnt_refnd_mast.unit_id = :unitId "
				+ "  and paymnt_refnd_mast.status = 'A' "
				+ "  and paymnt_refnd_mast.is_cn_or_deposit = 0"
				 ),
	 
	 
	 
	 @NamedNativeQuery(name = "GET_REFUND_MASTER_CR_LIST", query = "select "
			    + "  paymnt_refnd_mast.payment_refund_id as \"paymentRefundVoucherId\", "
				+ "  crnotemst.credit_note_no as \"creditNoteNo\", "
				+ "  to_char(crnotemst.credit_note_date,'DD-MM-YYYY HH24:MI:SS') as \"creditNoteDate\", "
				+ "  paymnt_refnd_mast.payment_refund_voucher_no as \"paymentRefundVoucherNo\", "
				+ "  to_char(paymnt_refnd_mast.date_of_refund,'DD-MM-YYYY HH24:MI:SS' "
				+ "  ) as \"dateOfRefund\", "
				+ "  pat.uhid_number as \"uhidNumber\", "
				+ "  billmst.visit_type_id as \"visitTypeId\", "
				+ "  billmst.encounter_id as \"encounterId\", "
				+ "  billmst.admission_id as \"admissionId\", "
				+ "  bill_mst.bill_number as \"invoiceNo\", "
				+ "  crnotemst.cr_net_amount as \"crAmount\", "
				+ "  paymnt_refnd_mast.amount_refunded as \"amountRefunded\", "
				+ "  concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.last_name) as \"patientName\", "
				+ "  pat.mobile_number as \"mobileNo\", "
				+ "  concat(emp_mst.first_name, ' ', emp_mst.last_name) as \"userName\", "
				+ "  coalesce( "
				+ "    case when billmst.visit_type_id = 1 then( "
				+ "      select "
				+ "        enc.encounter_number "
				+ "      from "
				+ "        opd.t_encounter_master enc "
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
				+ "    ) end,'-') as \"visitNo\",  "
				+ "  crnotemst.self_payable as \"selfPayable\", "
				+ "  crnotemst.credit_payable as \"creditPayable\" "
				+ " from "
				+ "  billing.t_payment_refund_master paymnt_refnd_mast "
				+ "  inner join billing.m_credit_note_master crnotemst on paymnt_refnd_mast.credit_note_id = crnotemst.credit_note_id "
				+ "  inner join billing.m_billing_master billmst on billmst.billing_master_id = crnotemst.bill_id and billmst.bill_cancelled = 'N' "
				+ "  inner join patient.t_patient_registration pat on pat.patient_id = billmst.patient_id "
				+ "  left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
				+ "  left join billing.m_billing_master bill_mst on bill_mst.billing_master_id = crnotemst.bill_id and bill_mst.bill_cancelled = 'N' "
				+ "  left join public.m_employee_master emp_mst on emp_mst.user_id = crnotemst.created_by "
				+ "where "
				+ "  paymnt_refnd_mast.orgnisation_id = :orgId "
				+ "  and paymnt_refnd_mast.is_cn_or_deposit = 1"
				+ "  and paymnt_refnd_mast.unit_id = :unitId "
				+ "  and paymnt_refnd_mast.status = 'A' "
				 ),
	 

	 @NamedNativeQuery(name = "GET_BANK_DETAILS_BY_VOUCHER_ID", query = "select "
			 + "  refund_cn.account_holder_name as \"accountHolderName\", "
			 + "  refund_cn.account_no  as \"accountNo\", "
			 + "  bank_mst.bank_name as \"bankName\", "
			 + "  refund_cn.ifsc_code as \"ifscCode\", "
			 + "  bank_branch_mst.branch_desc as \"branchName\" "
			 + "  from billing.t_refund_cn refund_cn "
			 + "  left join billing.m_bank_master bank_mst "
			 + "  on bank_mst.bank_id = refund_cn.bank_id "
			 + "  left join billing.m_bank_branch_master bank_branch_mst "
			 + "  on bank_branch_mst.branch_id = refund_cn.branch_id "
			 + "  where refund_cn.t_refund_cn_id = :tRefundCnId "
			 + "  and refund_cn.organization_id = :orgId "
			 + "  and refund_cn.unit_id = :unitId" ),
	 
	 @NamedNativeQuery(name="UPDATE_PAYMENT_REFUND_BY_ID",
	 query ="UPDATE billing.t_payment_refund_master "
	 		+"SET exported_to_shift_close= true "
	 		+"WHERE payment_refund_id= :paymentRefundId "
	 		+"AND orgnisation_id= :orgnizationId "
	 		+"AND unit_id= :unitId "),
})

@Entity
@Table(name = "t_payment_refund_master", schema = "Billing")
@SequenceGenerator(name = "t_payment_refund_master_seq", sequenceName = "billing.t_payment_refund_master_seq", allocationSize = 1)
public class PaymentRefundMaster {
	@Id
	@Column(name = "payment_refund_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_payment_refund_master_seq")
	private int paymentRefundVoucherId;

	@Column(name = "payment_refund_voucher_no")
	private String paymentRefundVoucherNo;

	@Column(name = "date_of_refund")
	private Date dateOfRefund;

	@Column(name = "is_cn_or_deposit")
	private Integer isCnOrDeposit;

	@Column(name = "t_refund_cn_id")
	private Integer tRefundCnId;

	@Column(name = "credit_note_id")
	private Integer creditNoteId;

	@Column(name = "t_pharmacy_return_id")
	private Integer tPharmacyReturnId;

	@Column(name = "amount_refunded")
	private double amountRefunded;

	@Column(name = "patient_id")
	private Integer patientId;

	@Column(name = "received_from")
	private String receivedFrom;

	@Column(name = "identification_no")
	private String identificationNo;

	@Column(name = "name_of_person")
	private String nameOfPerson;

	@Column(name = "status")
	private char status;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "orgnisation_id")
	private Integer orgnisationId;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name="counter_id")
	private Integer counterId;
	
	@Column(name="exported_to_shift_close")
	private boolean exportedToShiftClose;
	
	@Column(name = "sale_return_id")
	private Integer saleReturnId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "t_refund_cn_id", insertable = false, updatable = false, nullable = false)
	private RefundCNMaster refundCNMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, updatable = false, nullable = false)
	private PatientRegistration patientRegistration;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "paymentRefundMaster")
	private List<PaymentRefundDetails> listPaymentRefundDetails;

	public int getPaymentRefundVoucherId() {
		return paymentRefundVoucherId;
	}

	public void setPaymentRefundVoucherId(int paymentRefundVoucherId) {
		this.paymentRefundVoucherId = paymentRefundVoucherId;
	}

	public String getPaymentRefundVoucherNo() {
		return paymentRefundVoucherNo;
	}

	public void setPaymentRefundVoucherNo(String paymentRefundVoucherNo) {
		this.paymentRefundVoucherNo = paymentRefundVoucherNo;
	}

	public Date getDateOfRefund() {
		return dateOfRefund;
	}

	public void setDateOfRefund(Date dateOfRefund) {
		this.dateOfRefund = dateOfRefund;
	}

	public Integer getIsCnOrDeposit() {
		return isCnOrDeposit;
	}

	public void setIsCnOrDeposit(Integer isCnOrDeposit) {
		this.isCnOrDeposit = isCnOrDeposit;
	}

	public Integer gettRefundCnId() {
		return tRefundCnId;
	}

	public void settRefundCnId(Integer tRefundCnId) {
		this.tRefundCnId = tRefundCnId;
	}

	public double getAmountRefunded() {
		return amountRefunded;
	}

	public void setAmountRefunded(double amountRefunded) {
		this.amountRefunded = amountRefunded;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getReceivedFrom() {
		return receivedFrom;
	}

	public void setReceivedFrom(String receivedFrom) {
		this.receivedFrom = receivedFrom;
	}

	public String getIdentificationNo() {
		return identificationNo;
	}

	public void setIdentificationNo(String identificationNo) {
		this.identificationNo = identificationNo;
	}

	public String getNameOfPerson() {
		return nameOfPerson;
	}

	public void setNameOfPerson(String nameOfPerson) {
		this.nameOfPerson = nameOfPerson;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'A' : status;
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

	public RefundCNMaster getRefundCNMaster() {
		return refundCNMaster;
	}

	public void setRefundCNMaster(RefundCNMaster refundCNMaster) {
		this.refundCNMaster = refundCNMaster;
	}

	public PatientRegistration getPatientRegistration() {
		return patientRegistration;
	}

	public void setPatientRegistration(PatientRegistration patientRegistration) {
		this.patientRegistration = patientRegistration;
	}

	public List<PaymentRefundDetails> getListPaymentRefundDetails() {
		return listPaymentRefundDetails;
	}

	public void setListPaymentRefundDetails(List<PaymentRefundDetails> listPaymentRefundDetails) {
		this.listPaymentRefundDetails = listPaymentRefundDetails;
	}

	public Integer getCreditNoteId() {
		return creditNoteId;
	}

	public void setCreditNoteId(Integer creditNoteId) {
		this.creditNoteId = creditNoteId;
	}

	public Integer gettPharmacyReturnId() {
		return tPharmacyReturnId;
	}

	public void settPharmacyReturnId(Integer tPharmacyReturnId) {
		this.tPharmacyReturnId = tPharmacyReturnId;
	}

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public boolean isExportedToShiftClose() {
		return exportedToShiftClose;
	}

	public void setExportedToShiftClose(boolean exportedToShiftClose) {
		this.exportedToShiftClose = exportedToShiftClose;
	}

	public Integer getSaleReturnId() {
		return saleReturnId;
	}

	public void setSaleReturnId(Integer saleReturnId) {
		this.saleReturnId = saleReturnId;
	}

	
}

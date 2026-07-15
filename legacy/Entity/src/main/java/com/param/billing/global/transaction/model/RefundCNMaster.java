package com.param.billing.global.transaction.model;

import java.math.BigDecimal;
import java.util.Date;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.billing.global.model.BankBranchMaster;
import com.param.billing.global.model.BankMaster;
import com.param.billing.global.model.DepositMaster;
import com.param.global.model.CompanyMaster;
import com.param.global.model.VisitTypeMaster;

@NamedNativeQueries({
		@NamedNativeQuery(name = "GENERATE_REFUND_CN_NO", query = "select nextval('billing.seq_refunc_cn_no');"),
		
		
		
		@NamedNativeQuery(name = "SEARCH_REFUND_CN", query = "select "
				+ "  refund_cn.t_refund_cn_id as \"tRefundCnId\", "
				+ "  depositmst.patient_id as \"patientId\", "
				+ "  depositmst.deposit_master_id as \"depositId\", "
				+ "  refund_cn.refund_cn_no as \"refundCnNo\", "
				+ "  to_char(refund_cn.refund_cn_date, 'DD-MM-YYYY HH24:MI:SS') as \"refundCnDate\", "
				+ "  pat.uhid_number as \"uhidNumber\", "
				+ "  depositmst.visit_type_id as \"visitTypeId\", "
				+ "  depositmst.deposit_type_id as \"depositTypeId\", "
				+ "  depositmst.encounter_id as \"encounterId\", "
				+ "  depositmst.admission_id as \"admissionId\", "
				+ "  refund_cn.balance_amount as \"balanceAmount\", "
				+ "  refund_cn.remark as \"remark\", "
				+ "  refund_cn.refund_amount as \"refundAmount\", "
				+ "  refund_cn.is_approved as \"isApproved\", "
				+ "  concat(coalesce(pre.prefix_code,''),' ',pat.first_name,' ',pat.last_name) as \"patientName\", "
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
				+ "    or depositmst.visit_type_id = 3 or depositmst.visit_type_id = 4 then( "
				+ "      select "
				+ "        adm.admission_number "
				+ "      from "
				+ "        adt.t_admission adm "
				+ "      where "
				+ "        adm.admission_id = depositmst.admission_id "
				+ "    ) end, "
				+ "    '-' "
				+ "  ) as \"visitNo\", "
				+ "	(case "
				+ "		when coalesce(refund_cn.refund_amount,0) = coalesce(refund_cn.refunded_amount,0) then 3 "
				+ "		when coalesce(refund_cn.refunded_amount,0) = 0 then 1 "
				+ "		else 2 "
				+ "	end) as \"status\""
				+ "from  billing.t_refund_cn refund_cn "
				+ "  inner join billing.m_deposit_master depositmst on depositmst.deposit_master_id = refund_cn.deposit_id "
				+ "  inner join patient.t_patient_registration pat on pat.patient_id = depositmst.patient_id "
				+ "	 left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
				+ "  left join public.m_employee_master emp_mst on emp_mst.user_id = refund_cn.created_by "
				+ "where "
				+ "  refund_cn.organization_id =:orgId "
				+ "  and refund_cn.unit_id =:unitId"
//				+ "  and refund_cn.refund_cn_date >= :fromDate "
//				+ "  and refund_cn.refund_cn_date <= :toDate"
				+ ""),
		
})

@Entity
@Table(name = "t_refund_cn", schema = "billing")
@SequenceGenerator(name = "seq_t_refund_cn", sequenceName = "billing.seq_t_refund_cn", allocationSize = 1)
public class RefundCNMaster {
	@Id
	@Column(name = "t_refund_cn_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_t_refund_cn")
	private int tRefundCnId;

	@Column(name = "refund_cn_no")
	private String refundCnNo;

	@Column(name = "refund_cn_date")
	private Date refundCnDate;

	@Column(name = "payee_id")
	private Integer payeeId;

	@Column(name = "deposit_id")
	private Integer depositId;

	@Column(name = "refund_against_id")
	private Integer refundAgainstId;

	@Column(name = "visit_type_id")
	private Integer visitTypeId;

	@Column(name = "encounter_id")
	private Integer encounterId;

	@Column(name = "admission_id")
	private Integer admissionId;

	@Column(name = "refund_amount")
	private BigDecimal refundAmount;

	@Column(name = "refunded_amount")
	private BigDecimal refundedAmount;

	@Column(name = "balance_amount")
	private BigDecimal balanceAmount;

	@Column(name = "account_holder_name")
	private String accountHolderName;

	@Column(name = "bank_id")
	private Integer bankId;

	@Column(name = "branch_id")
	private Integer branchId;

	@Column(name = "ifsc_code")
	private String ifscCode;

	@Column(name = "account_no")
	private String accountNo;

	@Column(name = "bank_request_refund_amount")
	private BigDecimal bankRequestRefundAmount;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "remark")
	private String remark;

	@Column(name = "counter_id")
	private Integer counterId;
	
	@Column(name = "is_approved")
	private Character isApproved;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payee_id", insertable = false, updatable = false, nullable = false)
	private CompanyMaster companyMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bank_id", insertable = false, updatable = false, nullable = false)
	private BankMaster bankMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "branch_id", insertable = false, updatable = false)
	BankBranchMaster branchMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "visit_type_id", insertable = false, updatable = false, nullable = false)
	private VisitTypeMaster visitTypeMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deposit_id", insertable = false, updatable = false, nullable = false)
	private DepositMaster depositMaster;

	public int gettRefundCnId() {
		return tRefundCnId;
	}

	public void settRefundCnId(int tRefundCnId) {
		this.tRefundCnId = tRefundCnId;
	}

	public String getRefundCnNo() {
		return refundCnNo;
	}

	public void setRefundCnNo(String refundCnNo) {
		this.refundCnNo = refundCnNo;
	}

	public Date getRefundCnDate() {
		return refundCnDate;
	}

	public void setRefundCnDate(Date refundCnDate) {
		this.refundCnDate = refundCnDate;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer getDepositId() {
		return depositId;
	}

	public void setDepositId(Integer depositId) {
		this.depositId = depositId;
	}

	public Integer getRefundAgainstId() {
		return refundAgainstId;
	}

	public void setRefundAgainstId(Integer refundAgainstId) {
		this.refundAgainstId = refundAgainstId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public BigDecimal getRefundedAmount() {
		return refundedAmount;
	}

	public void setRefundedAmount(BigDecimal refundedAmount) {
		this.refundedAmount = refundedAmount;
	}

	public BigDecimal getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(BigDecimal balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public void setAccountHolderName(String accountHolderName) {
		this.accountHolderName = accountHolderName;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public BigDecimal getBankRequestRefundAmount() {
		return bankRequestRefundAmount;
	}

	public void setBankRequestRefundAmount(BigDecimal bankRequestRefundAmount) {
		this.bankRequestRefundAmount = bankRequestRefundAmount;
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

	public CompanyMaster getCompanyMaster() {
		return companyMaster;
	}

	public void setCompanyMaster(CompanyMaster companyMaster) {
		this.companyMaster = companyMaster;
	}

	public BankMaster getBankMaster() {
		return bankMaster;
	}

	public void setBankMaster(BankMaster bankMaster) {
		this.bankMaster = bankMaster;
	}

	public BankBranchMaster getBranchMaster() {
		return branchMaster;
	}

	public void setBranchMaster(BankBranchMaster branchMaster) {
		this.branchMaster = branchMaster;
	}

	public VisitTypeMaster getVisitTypeMaster() {
		return visitTypeMaster;
	}

	public void setVisitTypeMaster(VisitTypeMaster visitTypeMaster) {
		this.visitTypeMaster = visitTypeMaster;
	}

	public DepositMaster getDepositMaster() {
		return depositMaster;
	}

	public void setDepositMaster(DepositMaster depositMaster) {
		this.depositMaster = depositMaster;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public Character getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Character isApproved) {
		this.isApproved = (isApproved == null) ? 'N' : isApproved;
	}

}

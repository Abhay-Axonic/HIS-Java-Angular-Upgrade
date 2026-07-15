package com.param.billing.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;



@NamedNativeQueries({
	
	@NamedNativeQuery(name="GET_COMPANY_RECEIPTS_EXPENSE_LIST",query=
			" select " 
					 + " 	tme.company_receipts_expense_id as \"companyReceiptsExpenseId\", " 
					 + " 	tme.bill_number as \"billNumber\", " 
					 + " 	to_char(tme.bill_date_time, 'DD/MM/YYYY') as \"billDateTime\", " 
					 + " 	tme.amount as \"amount\", " 
					 + " 	tme.name as \"name\", "
					 + " 	tme.remark as \"remark\", "
					 + " 	tme.company_type_id as \"companyTypeId\", "
					 + " 	tme.bill_status as \"billStatus\", "
					 + " 	tme.company_id as \"companyId\", "
					 + " 	tme.insurance_company_id as \"insuranceCompanyId\", "
					 + " 	reqDis.request_sent_to as \"requestSentTo\", "
					 + " 	reqDis.req_status as \"reqStatus\", " 
					 + "   comp.company_desc as \"companyName\", "
					 + " 	(case " 
					 + " 		when employeema.first_name is not null then  " 
					 + " 			(employeema.first_name || ' ' || employeema.middle_name || ' ' || employeema.last_name) " 
					 + " 		when doctor.doctor_id is not null then   " 
					 + " 			(concat('Dr. ', coalesce(doctor.first_name), ' ', coalesce(doctor.middle_name), ' ', coalesce(doctor.last_name))) end )  " 
					 + " 	as \"requestSentToEmployee\", "
					 + " 	( " 
					 + " 	select " 
					 + " 		sum(amount) " 
					 + " 	from " 
					 + " 		billing.t_company_receipts_expense_reciept " 
					 + " 	where " 
					 + " 		company_receipts_expense_id  = tme.company_receipts_expense_id " 
					 + " 		and status = 'A') as \"receivedAmount\" , "
					 + " cast(cast(coalesce(tme.amount,0)-coalesce(tme.balance_amount,0) as numeric(10,2)) as float8) as \"consumedAmount\" " 
					 + " from " 
					 + " 	billing.t_company_receipts_expense tme " 
					 + " left join billing.t_request_discount reqDis on " 
					 + " 	reqDis.company_receipts_expense_id  = tme.company_receipts_expense_id " 
					 + " left join public.m_employee_master employeema on " 
					 + " 	reqDis.request_sent_to = employeema.user_id " 
					 + " left join doctor.m_doctor_master doctor on " 
					 + " 	reqDis.request_sent_to = doctor.user_id " 
					 + " inner join public.t_company_master comp  on " 
					 + " 	comp.company_id = tme.company_id "
					 + " where " 
					 + " 	tme.org_id =:orgId " 
					 + " 	and tme.unit_id =:unitId "  ),
	
	@NamedNativeQuery(name="GET_COMPANY_RECEIPTS_EXPENSE_RECIEPT_LIST",query=
			 " select " 
			 + " 	tmer.company_receipts_expense_reciept_id as \"companyReceiptsExpenseRecieptId\", " 
			 + " 	amount as \"amount\", " 
			 + " 	to_char(date_of_reciept, 'DD/MM/YYYY') as \"dateOfReciept\" , " 
			 + " 	reciept_number as \"recieptNumber\", "
			 + " 	company_receipts_expense_id as \"companyReceiptsExpenseId\" "
			 + " from " 
			 + " 	billing.t_company_receipts_expense_reciept tmer " 
			 + " where " 
			 + " 	tmer.company_receipts_expense_id =:expenseId "
			 + "	and tmer.status='A' "),
	
	@NamedNativeQuery(name="GET_COMPANY_RECEIPTS_EXPENSE_RECIEPT_DETAILS_LIST",query=
			"select " 
			 + " 	company_receipts_expense_reciept_details_id as \"companyRecieptDetailsId\", " 
			 + " 	company_receipts_expense_reciept_id as \"companyRecieptId\", " 
			 + " 	payment_mode_id as \"paymentModeId\", " 
			 + " 	amount as \"amount\", " 
			 + " 	merm.bank_id as \"bankId\", "
			 + "	bm.bank_name as \"bankName\", "  
			 + " 	check_number as \"chequeNumber\", " 
			 + " 	to_char(cheque_date,'DD/MM/YYYY')  as \"chequeDate\", " 
			 + " 	card_holder_name as \"cardHolderName\", " 
			 + " 	card_number as \"cardNumber\", " 
			 + " 	transaction_no as \"transactionNo\", " 
			 + "    to_char(trans_transaction_date ,'DD/MM/YYYY')  as \"transTransactionDate\" " 
			 + " from " 
			 + " 	billing.t_company_receipts_expense_reciept_details merm " 
			 + " left join billing.m_bank_master bm "
			 + "		on bm.bank_id = merm.bank_id  " 
			 + " where " 
			 + " 	merm.company_receipts_expense_reciept_id = :expenseRecieptId "),
	
})


@Entity
@Table(name = "t_company_receipts_expense", schema = "billing")
public class CompanyReceiptsExpense {

	@Id
	@Column(name = "company_receipts_expense_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer companyReceiptsExpenseId;
	
	@Column(name = "bill_number")
	private String billNumber;

	@Column(name = "bill_date_time")
	private Date billDateTime;

	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "company_type_id")
	private Integer companyTypeId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "company_id")
	private Integer companyId;
	
	@Column(name = "insurance_company_id")
	private Integer insuranceCompanyId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "status")
	private Character status;

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
	
	@Column(name = "bill_status")
	private Integer billStatus;
	
	@Column(name = "balance_amount")
    private Double balanceAmount;
	
	@Column(name = "credit_note_id")
	private Integer creditNoteId;
	
	public Integer getCreditNoteId() {
		return creditNoteId;
	}

	public void setCreditNoteId(Integer creditNoteId) {
		this.creditNoteId = creditNoteId;
	}

	public Double getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(Double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }
	

	public Integer getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(Integer billStatus) {
		this.billStatus = billStatus;
	}

	public Integer getCompanyReceiptsExpenseId() {
		return companyReceiptsExpenseId;
	}

	public void setCompanyReceiptsExpenseId(Integer companyReceiptsExpenseId) {
		this.companyReceiptsExpenseId = companyReceiptsExpenseId;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public Date getBillDateTime() {
		return billDateTime;
	}

	public void setBillDateTime(Date billDateTime) {
		this.billDateTime = billDateTime;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getCompanyTypeId() {
		return companyTypeId;
	}

	public void setCompanyTypeId(Integer companyTypeId) {
		this.companyTypeId = companyTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getInsuranceCompanyId() {
		return insuranceCompanyId;
	}

	public void setInsuranceCompanyId(Integer insuranceCompanyId) {
		this.insuranceCompanyId = insuranceCompanyId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}

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
	
	@NamedNativeQuery(name="GET_EXPENSE_LIST",query=
			" select " 
					 + " 	tme.expense_id as \"expenseId\", " 
					 + " 	tme.bill_number as \"billNumber\", " 
					 + " 	tme.system_gen_bill_number as \"systemGenBillNumber\", " 
					 + " 	to_char(tme.bill_date_time, 'DD/MM/YYYY') as \"billDateTime\", " 
					 + " 	to_char(tme.bill_date_time, 'DD-MM-YYYY HH24:MI:SS') as \"createdDatetime\", " 
					 + " 	tme.bill_status as \"billStatus\", " 
					 + " 	tme.bill_type as \"billType\", " 
					 + " 	tme.amount as \"amount\", " 
					 + " 	tme.name as \"name\", "
					 + " 	tme.remark as \"remark\", "
					 + " 	reqDis.request_sent_to as \"requestSentTo\", " 
					 + " 	tme.account_head as \"accountHead\", " 
					 + " 	( " 
					 + " 	select " 
					 + " 		sum(amount) " 
					 + " 	from " 
					 + " 		billing.t_miscellaneous_expense_reciept " 
					 + " 	where " 
					 + " 		expense_id = tme.expense_id " 
					 + " 		and status = 'A') as \"receivedAmount\", " 
					 + " 	reqDis.req_status as \"reqStatus\", " 
					 + " 	ledger.general_ledger_name as \"accountHeadDesc\", " 
					 + " 	(case " 
					 + " 		when employeema.first_name is not null then  " 
					 + " 			(employeema.first_name || ' ' || employeema.middle_name || ' ' || employeema.last_name) " 
					 + " 		when doctor.doctor_id is not null then   " 
					 + " 			(concat('Dr. ', coalesce(doctor.first_name), ' ', coalesce(doctor.middle_name), ' ', coalesce(doctor.last_name))) end )  " 
					 + " 	as \"requestSentToEmployee\" " 
					 + " from " 
					 + " 	billing.t_miscellaneous_expense tme " 
					 + " left join billing.t_request_discount reqDis on " 
					 + " 	reqDis.expense_id = tme.expense_id " 
					 + " left join m_general_ledger_master ledger on " 
					 + " 	ledger.general_ledger_id = tme.account_head " 
					 + " left join public.m_employee_master employeema on " 
					 + " 	reqDis.request_sent_to = employeema.user_id " 
					 + " left join doctor.m_doctor_master doctor on " 
					 + " 	reqDis.request_sent_to = doctor.user_id " 
					 + " where " 
					 + " 	tme.org_id =:orgId " 
					 + " 	and tme.unit_id =:unitId "  ),
	
	@NamedNativeQuery(name="GET_EXPENSE_RECIEPT_LIST",query=
			 " select " 
			 + " 	tmer.expense_reciept_id as \"expenseRecieptId\", " 
			 + " 	amount as \"amount\", " 
			 + " 	to_char(date_of_reciept, 'DD/MM/YYYY') as \"dateOfReciept\" , " 
			 + " 	reciept_number as \"recieptNumber\", "
			 + " 	expense_id as \"expenseId\" "
			 + " from " 
			 + " 	billing.t_miscellaneous_expense_reciept tmer " 
			 + " where " 
			 + " 	tmer.expense_id =:expenseId "
			 + "	and tmer.status='A' "),
	
	@NamedNativeQuery(name="GET_EXPENSE_RECIEPT_DETAILS_LIST",query=
			"select " 
			 + " 	expense_reciept_details_id as \"expenseRecieptDetailsId\", " 
			 + " 	expense_reciept_id as \"expenseRecieptId\", " 
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
			 + " 	billing.t_miscellaneous_expense_reciept_details merm " 
			 + " left join billing.m_bank_master bm "
			 + "		on bm.bank_id = merm.bank_id  " 
			 + " where " 
			 + " 	merm.expense_reciept_id = :expenseRecieptId ")
	
})

@Entity
@Table(name = "t_miscellaneous_expense", schema = "billing")
public class MiscellaneousExpense {

	@Id
	@Column(name = "expense_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer expenseId;
	
	@Column(name = "bill_number")
	private String billNumber;

	@Column(name = "bill_date_time")
	private Date billDateTime;

	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "bill_type")
	private Integer billType;
	
	@Column(name = "bill_status")
	private Integer billStatus;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "account_head")
	private Integer accountHead;
	
	@Column(name = "is_cancelled")
	private Character isCancelled;
	
	@Column(name = "cancelled_reason_id")
	private Integer cancelledReasonId;

	@Column(name = "bill_cancel_datetime")
	private Date billCancelDatetime;

	@Column(name = "cancelled_by")
	private Integer cancelledBy;

	@Column(name = "cancel_remarks")
	private String cancelRemark;
	
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
	
	@Column(name = "system_gen_bill_number")
	private String systemGenBillNumber;
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(Integer expenseId) {
		this.expenseId = expenseId;
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

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public Integer getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(Integer billStatus) {
		this.billStatus = billStatus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAccountHead() {
		return accountHead;
	}

	public void setAccountHead(Integer accountHead) {
		this.accountHead = accountHead;
	}

	public Character getIsCancelled() {
		return isCancelled;
	}

	public void setIsCancelled(Character isCancelled) {
		this.isCancelled = isCancelled;
	}

	public Integer getCancelledReasonId() {
		return cancelledReasonId;
	}

	public void setCancelledReasonId(Integer cancelledReasonId) {
		this.cancelledReasonId = cancelledReasonId;
	}

	public Date getBillCancelDatetime() {
		return billCancelDatetime;
	}

	public void setBillCancelDatetime(Date billCancelDatetime) {
		this.billCancelDatetime = billCancelDatetime;
	}

	public Integer getCancelledBy() {
		return cancelledBy;
	}

	public void setCancelledBy(Integer cancelledBy) {
		this.cancelledBy = cancelledBy;
	}

	public String getCancelRemark() {
		return cancelRemark;
	}

	public void setCancelRemark(String cancelRemark) {
		this.cancelRemark = cancelRemark;
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

	public String getSystemGenBillNumber() {
		return systemGenBillNumber;
	}

	public void setSystemGenBillNumber(String systemGenBillNumber) {
		this.systemGenBillNumber = systemGenBillNumber;
	}
	
}


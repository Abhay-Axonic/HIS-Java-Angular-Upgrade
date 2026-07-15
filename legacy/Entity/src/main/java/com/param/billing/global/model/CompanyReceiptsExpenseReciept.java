package com.param.billing.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_company_receipts_expense_reciept", schema = "billing")
public class CompanyReceiptsExpenseReciept {


	@Id
	@Column(name = "company_receipts_expense_reciept_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer companyReceiptsExpenseRecieptId;
	
	@Column(name = "company_receipts_expense_id")
	private Integer companyReceiptsExpenseId;
	
	@Column(name = "reciept_number")
	private String recieptNumber;

	@Column(name = "date_of_reciept")
	private Date dateOfReciept;

	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "reciept_cancelled")
	private Character recieptCancelled;
	
	@Column(name = "reciept_cancel_reason_id")
	private Integer recieptCancelReasonId;

	@Column(name = "reciept_cancel_datetime")
	private Date recieptCancelDatetime;

	@Column(name = "reciept_cancel_by")
	private Integer recieptCancelBy;

	@Column(name = "reciept_cancel_remark")
	private String recieptCancelRemark;
	
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

	public Integer getCompanyReceiptsExpenseRecieptId() {
		return companyReceiptsExpenseRecieptId;
	}

	public void setCompanyReceiptsExpenseRecieptId(Integer companyReceiptsExpenseRecieptId) {
		this.companyReceiptsExpenseRecieptId = companyReceiptsExpenseRecieptId;
	}

	public Integer getCompanyReceiptsExpenseId() {
		return companyReceiptsExpenseId;
	}

	public void setCompanyReceiptsExpenseId(Integer companyReceiptsExpenseId) {
		this.companyReceiptsExpenseId = companyReceiptsExpenseId;
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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

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

	public Date getRecieptCancelDatetime() {
		return recieptCancelDatetime;
	}

	public void setRecieptCancelDatetime(Date recieptCancelDatetime) {
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
	
	

}

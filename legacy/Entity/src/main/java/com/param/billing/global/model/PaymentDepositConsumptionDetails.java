package com.param.billing.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="t_payment_deposite_consumption_details",schema="billing")
@SequenceGenerator(sequenceName="billing.payment_deposite_consumption_details_seq",name="payment_deposite_consumption_details_seq",allocationSize=1)
public class PaymentDepositConsumptionDetails {

	@Id
	@Column(name="payment_depo_consumption_details_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="payment_deposite_consumption_details_seq")
	private Integer paymentDepoConsumptionDetailsId;
	
	@Column(name="dep_consumption_id")
	private Integer depConsumptionId;
	
	@Column(name="deposit_id")
	private Integer depositId;
	
	@Column(name="deposit_details_id")
	private Integer depositDetailsId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="consumed_amount")
	private Double consumedAmount;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="remark")
	private String remark;
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name="company_receipts_expense_id")
    private Integer companyReceiptsExpenseId;
    

    public Integer getCompanyReceiptsExpenseId() {
        return companyReceiptsExpenseId;
    }

    public void setCompanyReceiptsExpenseId(Integer companyReceiptsExpenseId) {
        this.companyReceiptsExpenseId = companyReceiptsExpenseId;
    }

	public Integer getPaymentDepoConsumptionDetailsId() {
		return paymentDepoConsumptionDetailsId;
	}

	public void setPaymentDepoConsumptionDetailsId(Integer paymentDepoConsumptionDetailsId) {
		this.paymentDepoConsumptionDetailsId = paymentDepoConsumptionDetailsId;
	}

	public Integer getDepConsumptionId() {
		return depConsumptionId;
	}

	public void setDepConsumptionId(Integer depConsumptionId) {
		this.depConsumptionId = depConsumptionId;
	}

	public Integer getDepositId() {
		return depositId;
	}

	public void setDepositId(Integer depositId) {
		this.depositId = depositId;
	}

	public Integer getDepositDetailsId() {
		return depositDetailsId;
	}

	public void setDepositDetailsId(Integer depositDetailsId) {
		this.depositDetailsId = depositDetailsId;
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

	public Double getConsumedAmount() {
		return consumedAmount;
	}

	public void setConsumedAmount(Double consumedAmount) {
		this.consumedAmount = consumedAmount;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}
	
}
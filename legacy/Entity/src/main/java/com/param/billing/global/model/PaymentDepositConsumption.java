package com.param.billing.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.billing.global.transaction.model.BillingMaster;

@Entity
@Table(name = "t_payment_deposite_consumption", schema = "billing")
@SequenceGenerator(name = "payment_deposite_consumption_seq", sequenceName = "billing.payment_deposite_consumption_seq", allocationSize = 1)
public class PaymentDepositConsumption {

	@Id
	@GeneratedValue(generator = "payment_deposite_consumption_seq", strategy = GenerationType.SEQUENCE)
	@Column(name = "dep_consumption_id")
	private Integer depConsumptionId;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "dep_consumption_date")
	private Date depConsumptionDate;

	@Column(name = "deposite_consumption_number")
	private String depositeConsumptionNumber;

	@Column(name = "bill_id")
	private Integer billId;

	@Column(name = "sale_id")
	private Integer saleId;

	@Column(name = "bill_payee_details_id")
	private Integer billPayeeDetailsId;

	@Column(name = "consumed_amount")
	private Double consumedAmount;

	@Column(name = "consumption_against")
	private Integer consumptionAgainst;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "status")
	private Character status;

	@Column(name = "counter_id")
	private Integer counterId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bill_id", insertable = false, updatable = false, nullable = false)
	private BillingMaster billingMaster;

	public String getDepositeConsumptionNumber() {
		return depositeConsumptionNumber;
	}

	public void setDepositeConsumptionNumber(String depositeConsumptionNumber) {
		this.depositeConsumptionNumber = depositeConsumptionNumber;
	}

	public Integer getBillPayeeDetailsId() {
		return billPayeeDetailsId;
	}

	public void setBillPayeeDetailsId(Integer billPayeeDetailsId) {
		this.billPayeeDetailsId = billPayeeDetailsId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getDepConsumptionId() {
		return depConsumptionId;
	}

	public void setDepConsumptionId(Integer depConsumptionId) {
		this.depConsumptionId = depConsumptionId;
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

	public Date getDepConsumptionDate() {
		return depConsumptionDate;
	}

	public void setDepConsumptionDate(Date depConsumptionDate) {
		this.depConsumptionDate = depConsumptionDate;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Double getConsumedAmount() {
		return consumedAmount;
	}

	public void setConsumedAmount(Double consumedAmount) {
		this.consumedAmount = consumedAmount;
	}

	public Integer getConsumptionAgainst() {
		return consumptionAgainst;
	}

	public void setConsumptionAgainst(Integer consumptionAgainst) {
		this.consumptionAgainst = consumptionAgainst;
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

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public Integer getSaleId() {
		return saleId;
	}

	public void setSaleId(Integer saleId) {
		this.saleId = saleId;
	}

}
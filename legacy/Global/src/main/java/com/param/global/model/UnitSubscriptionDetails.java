package com.param.global.model;

import com.param.global.common.ICommonConstants;
import org.hibernate.annotations.ColumnTransformer;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="t_unit_subscription_details", schema="public")
public class UnitSubscriptionDetails implements ICommonConstants {


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unit_subscription_details_seq")
	@SequenceGenerator(name = "unit_subscription_details_seq", sequenceName = "public.t_unit_subscription_details_seq", allocationSize = 1)
	@Column(name = "subscription_id")
	private Integer subscriptionId;

	@Column(name = "subscription_config_id")
	private Integer subscriptionConfigId;

	@Column(name = "subscription_type")
	private String subscriptionType;

	@Column(name = "subscription_amount")
	@ColumnTransformer(read = "pgp_sym_decrypt(subscriptionAmount, '"+ENCRYPTIONKEY+"')", write = "pgp_sym_encrypt(?, '"+ENCRYPTIONKEY+"')")
	private String subscriptionAmount;

	@Column(name = "subscription_discount")
	@ColumnTransformer(read = "pgp_sym_decrypt(subscriptionDiscount, '"+ENCRYPTIONKEY+"')", write = "pgp_sym_encrypt(?, '"+ENCRYPTIONKEY+"')")
	private String subscriptionDiscount;

	@Column(name = "subscription_tax")
	@ColumnTransformer(read = "pgp_sym_decrypt(subscriptionTax, '"+ENCRYPTIONKEY+"')", write = "pgp_sym_encrypt(?, '"+ENCRYPTIONKEY+"')")
	private String subscriptionTax;

	@Column(name = "subscription_net_amount")
	@ColumnTransformer(read = "pgp_sym_decrypt(subscriptionNetAmount, '"+ENCRYPTIONKEY+"')", write = "pgp_sym_encrypt(?, '"+ENCRYPTIONKEY+"')")
	private String subscriptionNetAmount;

	@Column(name = "paid_amount")
	@ColumnTransformer(read = "pgp_sym_decrypt(paidAmount, '"+ENCRYPTIONKEY+"')", write = "pgp_sym_encrypt(?, '"+ENCRYPTIONKEY+"')")
	private String paidAmount;

	@Column(name = "amount_paid_status")
	private String amountPaidStatus;

	@Column(name = "subscription_start_date")
	@ColumnTransformer(read = "pgp_sym_decrypt(subscriptionStartDate, '"+ENCRYPTIONKEY+"')", write = "pgp_sym_encrypt(?, '"+ENCRYPTIONKEY+"')")
	private String subscriptionStartDate;

	@Column(name = "subscription_end_date")
	@ColumnTransformer(read = "pgp_sym_decrypt(subscriptionEndDate, '"+ENCRYPTIONKEY+"')", write = "pgp_sym_encrypt(?, '"+ENCRYPTIONKEY+"')")
	private String subscriptionEndDate;

	@Column(name = "payment_order_id")
	private String paymentOrderId;

	@Column(name = "subscription_status")
	private String subscriptionStatus;

	@Column(name = "payment_type")
	private String paymentType;

	@Column(name = "final_reminder_count")
	private Integer finalReminderCount;

	@Column(name = "reminder_count")
	private Integer reminderCount;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "status")
	private Character status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "remark")
	private String remark;

    public Integer getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(Integer subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public Integer getSubscriptionConfigId() {
		return subscriptionConfigId;
	}

	public void setSubscriptionConfigId(Integer subscriptionConfigId) {
		this.subscriptionConfigId = subscriptionConfigId;
	}

	public String getSubscriptionType() {
		return subscriptionType;
	}

	public void setSubscriptionType(String subscriptionType) {
		this.subscriptionType = subscriptionType;
	}

	public String getSubscriptionAmount() {
		return subscriptionAmount;
	}

	public void setSubscriptionAmount(String subscriptionAmount) {
		this.subscriptionAmount = subscriptionAmount;
	}

	public String getSubscriptionDiscount() {
		return subscriptionDiscount;
	}

	public void setSubscriptionDiscount(String subscriptionDiscount) {
		this.subscriptionDiscount = subscriptionDiscount;
	}

	public String getSubscriptionNetAmount() {
		return subscriptionNetAmount;
	}

	public void setSubscriptionNetAmount(String subscriptionNetAmount) {
		this.subscriptionNetAmount = subscriptionNetAmount;
	}

	public String getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(String paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getAmountPaidStatus() {
		return amountPaidStatus;
	}

	public void setAmountPaidStatus(String amountPaidStatus) {
		this.amountPaidStatus = amountPaidStatus;
	}

	public String getSubscriptionStartDate() {
		return subscriptionStartDate;
	}

	public void setSubscriptionStartDate(String subscriptionStartDate) {
		this.subscriptionStartDate = subscriptionStartDate;
	}

	public String getSubscriptionEndDate() {
		return subscriptionEndDate;
	}

	public void setSubscriptionEndDate(String subscriptionEndDate) {
		this.subscriptionEndDate = subscriptionEndDate;
	}

	public String getPaymentOrderId() {
		return paymentOrderId;
	}

	public void setPaymentOrderId(String paymentOrderId) {
		this.paymentOrderId = paymentOrderId;
	}

	public String getSubscriptionStatus() {
		return subscriptionStatus;
	}

	public void setSubscriptionStatus(String subscriptionStatus) {
		this.subscriptionStatus = subscriptionStatus;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Integer getFinalReminderCount() {
		return finalReminderCount;
	}

	public void setFinalReminderCount(Integer finalReminderCount) {
		this.finalReminderCount = finalReminderCount;
	}

	public Integer getReminderCount() {
		return reminderCount;
	}

	public void setReminderCount(Integer reminderCount) {
		this.reminderCount = reminderCount;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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

	public String getSubscriptionTax() {
		return subscriptionTax;
	}

	public void setSubscriptionTax(String subscriptionTax) {
		this.subscriptionTax = subscriptionTax;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}

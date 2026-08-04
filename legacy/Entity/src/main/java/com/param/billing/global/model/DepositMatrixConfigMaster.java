package com.param.billing.global.model;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.param.adt.master.unit.model.BillingBedCategoryMaster;
import com.param.global.model.BedCategoryMaster;
import com.param.global.model.PatientCategoryMaster;
import com.param.global.model.PaymentEntitlementMaster;

@NamedQueries({ 
	
	@NamedQuery(name = "GET_DEPOSIT_MATRIX_LIST", 
			query = "SELECT  depositMatrixConfigMaster.mDepositMatrixId as mDepositMatrixId, "
					+ " depositMatrixConfigMaster.status AS status, "
					+ " billingCategory.billingBedDesc AS billingCategory, "
					+ " payementEntilement.paymentEntitlementDesc AS patientEntitleMent, "
					+ " patiCagegory.patientCategory AS patientType, "
					+ " depositMatrixConfigMaster.againstDeposit AS againstDeposit, "
					+ " depositMatrixConfigMaster.surgical AS surgical, "
					+ " depositMatrixConfigMaster.nonSurgical AS nonSurgical "
					+ " FROM DepositMatrixConfigMaster depositMatrixConfigMaster"
					+ " LEFT JOIN depositMatrixConfigMaster.patientCategoryMaster AS patiCagegory "
					+ " LEFT JOIN depositMatrixConfigMaster.billingBedCaegoryMaster AS billingCategory  "
					+ " LEFT JOIN depositMatrixConfigMaster.paymentEntitlementMaster AS payementEntilement  "
					+ " WHERE depositMatrixConfigMaster.unitId=:unitId "
					+ " AND depositMatrixConfigMaster.organisationId=:orgId"
					+ " ORDER BY depositMatrixConfigMaster.mDepositMatrixId DESC"    ),

	@NamedQuery(name = "GET_DEPOSIT_MATRIX_EXIST_OR_NOT",
			query = "SELECT COUNT(*)"
					+ " FROM DepositMatrixConfigMaster depositMatrixConfigMaster"
					+ " WHERE depositMatrixConfigMaster.unitId=:unitId "
					+ " AND depositMatrixConfigMaster.organisationId=:orgId"
	                + " AND depositMatrixConfigMaster.patientTypeId=:patientTypeId"
					+ " AND depositMatrixConfigMaster.paymentEntitlementId=:paymentEntitlementId"
					+ " AND depositMatrixConfigMaster.bedCategoryId=:bedCategoryId"
					+ " AND depositMatrixConfigMaster.againstDeposit=:againstDeposit"
						/*
						 * + " AND depositMatrixConfigMaster.surgical=:surgical" +
						 * " AND depositMatrixConfigMaster.nonSurgical=:nonsurgical"
						 */
					+ " AND depositMatrixConfigMaster.mDepositMatrixId!=:mDepositMatrixId"),
	
			
	@NamedQuery(name = "GET_DEPOSIT_MATRIX_CONFIG_COUNT",
			query = "SELECT COUNT(*)"
					+ " FROM DepositMatrixConfigMaster depositMatrixConfigMaster"
					+ " WHERE depositMatrixConfigMaster.unitId=:unitId "
					+ " and depositMatrixConfigMaster.organisationId=:orgId"),
	
	@NamedQuery(name = "GET_DEPOSIT_MATRIX_CONFIG_BY_ID",
			query = "SELECT "
					+ " depositMatrixConfigMaster.mDepositMatrixId as mDepositMatrixId,"
					+ " depositMatrixConfigMaster.patientTypeId as patientTypeId,"
					+ " depositMatrixConfigMaster.paymentEntitlementId as paymentEntitlementId,"
					+ " depositMatrixConfigMaster.bedCategoryId as bedCategoryId,"
					+ " depositMatrixConfigMaster.againstDeposit as againstDeposit,"
					+ " depositMatrixConfigMaster.surgical as surgical,"
					+ " depositMatrixConfigMaster.nonSurgical as nonSurgical,"
					+ " depositMatrixConfigMaster.status as status"
					+ " FROM DepositMatrixConfigMaster depositMatrixConfigMaster"
					+ " WHERE depositMatrixConfigMaster.mDepositMatrixId =:mDepositMatrixId"),
			
	@NamedQuery(name = "UPDATE_DEPOSIT_MATRIX_STATUS",
			query = "UPDATE DepositMatrixConfigMaster "
					+ " SET status =:status"
					+ " WHERE mDepositMatrixId =:mDepositMatrixId "
					+ " AND organisationId =:orgId AND unitId =:unitId"
					)
	})

@Entity
@Table(name = "m_deposit_matrix_config", schema = "billing")
@SequenceGenerator(name = "seq_m_deposit_matrix_config", sequenceName = "billing.seq_m_deposit_matrix_config", allocationSize = 1)
public class DepositMatrixConfigMaster {
	


	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}

	public BillingBedCategoryMaster getBillingBedCaegoryMaster() {
		return billingBedCaegoryMaster;
	}

	public void setBillingBedCaegoryMaster(BillingBedCategoryMaster billingBedCaegoryMaster) {
		this.billingBedCaegoryMaster = billingBedCaegoryMaster;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_deposit_matrix_config")
	@Column(name = "m_deposit_matrix_id")
	private int mDepositMatrixId;

	@Column(name = "patient_type_id")
	private Integer patientTypeId;

	@Column(name = "payment_entitlement_id")
	private Integer paymentEntitlementId;

	@Column(name = "bed_category_id")
	private Integer bedCategoryId;

	@Column(name = "against_deposit")
	private Integer againstDeposit;
	
	@Column(name = "surgical")
	private BigDecimal surgical;

	@Column(name = "non_surgical")
	private BigDecimal nonSurgical;

	@Column(name = "status")
	private Character status;

	@Column(name = "organisation_id")
	private Integer organisationId;

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
	
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "patient_type_id", insertable = false, updatable = false, nullable = false)
	private PatientCategoryMaster patientCategoryMaster;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "bed_category_id", insertable = false, updatable = false, nullable = false)
	private BillingBedCategoryMaster billingBedCaegoryMaster;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "payment_entitlement_id", insertable = false, updatable = false, nullable = false)
	private PaymentEntitlementMaster paymentEntitlementMaster;

	public int getmDepositMatrixId() {
		return mDepositMatrixId;
	}

	public void setmDepositMatrixId(int mDepositMatrixId) {
		this.mDepositMatrixId = mDepositMatrixId;
	}

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getAgainstDeposit() {
		return againstDeposit;
	}

	public void setAgainstDeposit(Integer againstDeposit) {
		this.againstDeposit = againstDeposit;
	}

	public BigDecimal getSurgical() {
		return surgical;
	}

	public void setSurgical(BigDecimal surgical) {
		this.surgical = surgical;
	}

	public BigDecimal getNonSurgical() {
		return nonSurgical;
	}

	public void setNonSurgical(BigDecimal nonSurgical) {
		this.nonSurgical = nonSurgical;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
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

	public PatientCategoryMaster getPatientCategoryMaster() {
		return patientCategoryMaster;
	}

	public void setPatientCategoryMaster(PatientCategoryMaster patientCategoryMaster) {
		this.patientCategoryMaster = patientCategoryMaster;
	}


	public PaymentEntitlementMaster getPaymentEntitlementMaster() {
		return paymentEntitlementMaster;
	}

	public void setPaymentEntitlementMaster(PaymentEntitlementMaster paymentEntitlementMaster) {
		this.paymentEntitlementMaster = paymentEntitlementMaster;
	}

}

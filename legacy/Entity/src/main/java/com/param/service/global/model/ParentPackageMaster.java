package com.param.service.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedNativeQueries({

		@NamedNativeQuery(name = "GET_PARENT_PACAKGE_BY_ID", query = "select "
				+ "	parent.parent_package_id as \"parentPackageId\", "
				+ "	parent.package_type_id as \"packageTypeId\", " + "	parent.sex_id as \"sexId\", "
				+ "	to_char(parent.validity_from_date, 'DD-MM-YYYY') as \"validityFromDate\", "
				+ "	to_char(parent.validity_to_date, 'DD-MM-YYYY') as \"validityToDate\", "
				+ "	parent.status as \"status\", " + "	parent.min_age as \"minAge\", "
				+ "	parent.max_age as \"maxAge\", " + "	parent.service_id as \"serviceId\", "
				+ "	parent.no_of_encounters as \"noOfEncounters\", "
				+ "	srv.service_standard_name as \"packageName\", " + "	parent.unit_id as \"unitId\", "
				+ "	parent.org_id as \"orgId\" " + "from " + "	service.m_parent_package_master parent "
				+ "inner join service.m_service_master srv on " + "	parent.service_id = srv.service_master_id "
				+ "where " + "	parent.status = 'A' " + "	and parent.parent_package_id = :parentPackageId"),

		
		@NamedNativeQuery(name = "GET_PACAKGE_BY_PARENT_ID", query = "select "
				+ "	pack.package_master_id as \"packageMasterId\", " + "	pack.patient_type_id as \"patientTypeId\", "
				+ "	pack.package_type_id as \"packageTypeId\", "
				+ "	pack.billing_bed_category_id as \"billingBedCategoryId\", "
				+ "	pack.visit_type_id as \"visitTypeId\", " + "	parent.sex_id as \"sexId\", "
				+ "	pack.payment_entitlement_type_id as \"paymentEntitlementTypeId\", "
				+ "	to_char(pack.validity_from_date, 'DD-MM-YYYY') as \"validityFromDate\", "
				+ "	to_char(pack.validity_to_date, 'DD-MM-YYYY') as \"validityToDate\", "
				+ "	pack.status as \"status\", " + "	pack.package_cost as \"packageCost\", "
				+ "	pack.markup_down_in_percentage as \"markupDownInPercentage\", "
				+ "	pack.package_price as \"packagePrice\", "
				+ "	pack.is_manual_rounding_is_allow as \"isManualRoundingIsAllow\", "
				+ "	pack.manual_roundoff_amount as \"manualRoundoffAmount\", "
				+ "	pack.validity_period_indays as \"validityPeriodIndays\", " + "	pack.service_id as \"serviceId\", "
				+ "	pack.tariff_id as \"tariffId\", " + "	pack.is_markup_down as \"isMarkupDown\", "
				+ "	pack.round_off_posit_neg as \"roundOffPositNeg\", "
				+ "	pack.no_of_encounters as \"noOfEncounters\", " + "	srv.service_standard_name as \"packageName\", "
				+ "	peent.payment_entitlement_desc as \"paymentEntitlement\", "
                + "	pcgr.patient_category as \"patientType\", " + "	bbcm.billing_bed_desc as \"billingBedCategory\", "
                + "	srv.speciality_id as \"specialityId\", " + "	srv.sub_speciality_id as \"subSpecialityId\" "
				+ "from " + "	service.m_parent_package_master parent "
				+ "inner join service.m_package_master pack on "
				+ "	parent.parent_package_id = pack.parent_package_id " + "inner join service.m_service_master srv on "
				+ "	parent.service_id = srv.service_master_id " + "left join public.m_patient_category_master pcgr on "
				+ "	pack.patient_type_id = pcgr.patient_category_id "
				+ "left join public.m_payment_entitlement_master peent on "
				+ "	pack.payment_entitlement_type_id = peent.payment_entitlement_id "
				+ "left join adt.m_billing_bed_category_master bbcm on "
				+ "	pack.billing_bed_category_id = bbcm.billing_bed_category_id " + "where " + "	pack.status = 'A' "
				+ "	and pack.parent_package_id = :parentPackageId") })

@Entity
@Table(name = "m_parent_package_master", schema = "service")
@SequenceGenerator(name = "seq_parent_package_master", sequenceName = "service.seq_parent_package_master", allocationSize = 1)
public class ParentPackageMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_parent_package_master")
	@Column(name = "parent_package_id")
	private int parentPackageId;

	@Column(name = "package_type_id")
	private Integer packageTypeId;

	@Column(name = "sex_id")
	private Integer sexId;

	@Column(name = "validity_from_date")
	private Date validityFromDate;

	@Column(name = "validity_to_date")
	private Date validityToDate;

	@Column(name = "status")
	private char status;

	@Column(name = "validity_period_indays")
	private int validityPeriodIndays;

	@Column(name = "no_of_encounters")
	private Integer noOfEncounters;

	@Column(name = "min_age")
	private Integer minAge;

	@Column(name = "max_age")
	private Integer maxAge;

	@Column(name = "service_id")
	private Integer serviceId;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "version_id")
	private Integer versionId;

	public int getParentPackageId() {
		return parentPackageId;
	}

	public void setParentPackageId(int parentPackageId) {
		this.parentPackageId = parentPackageId;
	}

	public Integer getSexId() {
		return sexId;
	}

	public void setSexId(Integer sexId) {
		this.sexId = sexId;
	}

	public Date getValidityFromDate() {
		return validityFromDate;
	}

	public void setValidityFromDate(Date validityFromDate) {
		this.validityFromDate = validityFromDate;
	}

	public Date getValidityToDate() {
		return validityToDate;
	}

	public void setValidityToDate(Date validityToDate) {
		this.validityToDate = validityToDate;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'A');
	}

	public int getValidityPeriodIndays() {
		return validityPeriodIndays;
	}

	public void setValidityPeriodIndays(int validityPeriodIndays) {
		this.validityPeriodIndays = validityPeriodIndays;
	}

	public Integer getNoOfEncounters() {
		return noOfEncounters;
	}

	public void setNoOfEncounters(Integer noOfEncounters) {
		this.noOfEncounters = noOfEncounters;
	}

	public Integer getMinAge() {
		return minAge;
	}

	public void setMinAge(Integer minAge) {
		this.minAge = minAge;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
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

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	public Integer getVersionId() {
		return versionId;
	}

	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}

}

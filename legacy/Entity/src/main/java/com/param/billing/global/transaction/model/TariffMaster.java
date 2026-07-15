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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.otims.model.SurgeryResourceConfig;
import com.param.service.global.model.MCompanyContractMaster;
import com.param.service.global.model.MPackageMaster;

@NamedQueries({
					
	@NamedQuery(name="GET_TARIFF_MASTER_LIST_BY_NAME",query="SELECT tariff.tariffId as tariffId "
			+ "FROM TariffMaster tariff "
			+ "WHERE tariff.organizationId=:orgId "
			+ "AND tariff.unitId=:unitId "
			+ "AND tariff.tariffDesc=:tariffDesc "),
	
	@NamedQuery(name="GET_TARIFF_MASTER_LIST",query="SELECT tariff.tariffDesc as tariffDesc,"
			+ "tariff.tariffId as tariffId, "
			+ "tariff.tariffCode as tariffCode, "
			+ "to_char(tariff.validFrom,'dd/MM/yyyy') as validFrom, "
			+ "to_char(tariff.validTo,'dd/MM/yyyy') as validTo, "
			+ "tariff.status as status "
			+ "FROM TariffMaster tariff "
			+ "WHERE tariff.organizationId=:orgId "
			+ "AND tariff.unitId=:unitId "),
	
	@NamedQuery(name="GET_TARIFF_MASTER_LIST_OPD_BILLING",query="SELECT tariff.tariffDesc as tariffDesc,"
			+ "tariff.tariffId as tariffId, "
			+ "tariff.tariffCode as tariffCode, "
			+ "to_char(tariff.validFrom,'dd/MM/yyyy') as validFrom, "
			+ "to_char(tariff.validTo,'dd/MM/yyyy') as validTo, "
			+ "tariff.status as status "
			+ "FROM TariffMaster tariff "
			+ "WHERE tariff.organizationId=:orgId "
			+ "AND tariff.unitId=:unitId AND tariff.status='A'"
			+" ORDER BY tariff.tariffDesc ASC "),
	
	@NamedQuery(name="GET_PAYMENT_ENTITLEMENT_LIST_BY_TARIFF_ID",query="SELECT tariffPayment.tariffPaymentEntitlementMapperId as tariffPaymentEntitlementMapperId, "
			+ "tariffPayment.tariffId as tariffId, "
			+ "tariffPayment.paymentEntitlementId as applicablePaymentEntitlementId, "
			+ "paymentEntitlement.paymentEntitlementDesc as paymentEntitlementDesc, "
			+ "tariffPayment.status as status "
			+ "FROM TariffPaymentEntitlementMapper tariffPayment "
			+ "INNER JOIN tariffPayment.paymentEntitlementMaster paymentEntitlement "
			+ "WHERE tariffPayment.tariffId=:tariffId "
			+ "AND tariffPayment.status='A' "),
	
	@NamedQuery(name="GET_PAYMENT_ENTITLEMENT_LIST_BY_TARIFF_ID_LIST",query="SELECT tariffPayment.tariffPaymentEntitlementMapperId as tariffPaymentEntitlementMapperId, "
			+ "tariffPayment.tariffId as tariffId, "
			+ "tariffPayment.paymentEntitlementId as applicablePaymentEntitlementId, "
			+ "paymentEntitlement.paymentEntitlementDesc as paymentEntitlementDesc, "
			+ "tariffPayment.status as status "
			+ "FROM TariffPaymentEntitlementMapper tariffPayment "
			+ "INNER JOIN tariffPayment.paymentEntitlementMaster paymentEntitlement "
			+ "WHERE tariffPayment.tariffId IN (:tariffIdList) "
			+ "AND tariffPayment.status='A' "),
	
	@NamedQuery(name="GET_TARIFF_MASTER_LIST_BY_ID",query="SELECT tariff.tariffDesc as tariffDesc,"
			+ "tariff.tariffId as tariffId, "
			+ "tariff.tariffCode as tariffCode, "
			+ "to_char(tariff.validFrom,'MM/dd/yyyy') as validFrom, "
			+ "to_char(tariff.validTo,'MM/dd/yyyy') as validTo, "
			+ "tariff.status as status "
			+ "FROM TariffMaster tariff "
			+ "WHERE tariff.tariffId=:tariffId "),
	
	@NamedQuery(name="GET_TARIFF_MASTER_LIST_BY_NAME_NOT_ID",query="SELECT tariff.tariffDesc as tariffDesc,"
			+ "tariff.tariffId as tariffId, "
			+ "tariff.tariffCode as tariffCode, "
			+ "to_char(tariff.validFrom,'MM/dd/yyyy') as validFrom, "
			+ "to_char(tariff.validTo,'MM/dd/yyyy') as validTo, "
			+ "tariff.status as status "
			+ "FROM TariffMaster tariff "
			+ "WHERE tariff.tariffId!=:tariffId "
			+ "AND tariff.tariffDesc=:tariffDesc "
			+ "AND tariff.organizationId=:orgId "
			+ "AND tariff.unitId=:unitId "),
	
	@NamedQuery(name="GET_PAYMENT_ENTITLEMENT_LIST_BY_MAPPER_ID",query="SELECT tariffPayment.tariffPaymentEntitlementMapperId as tariffPaymentEntitlementMapperId "
			+ "FROM TariffPaymentEntitlementMapper tariffPayment "
			+ "WHERE tariffPayment.tariffPaymentEntitlementMapperId=:tariffPaymentEntitlementMapperId "),
	
	@NamedQuery(name="GET_ACTIVE_TARIFF_MASTER_LIST",query="SELECT tariff.tariffId as tariffId, "
			+ "tariff.tariffDesc as tariffDesc, "
			+ "tariff.organizationId as organizationId, "
			+ "tariff.unitId as unitId "
			+ "FROM TariffMaster tariff "
			+ "WHERE "
			+ " tariff.organizationId=:orgId "
			+ " AND tariff.unitId=:unitId "
			+ " AND tariff.status='A' "
			+ " ORDER BY tariff.tariffDesc ASC "),
	
	@NamedQuery(name="GET_VISIT_TYPE_LIST_BY_TARIFF_ID_LIST",
	query="SELECT tariffVisit.tariffVisitTypeMapperId as tariffVisitTypeMapperId, "
			+ "tariffVisit.tariffId as tariffId, "
			+ "tariffVisit.visitTypeId as visitTypeId, "
			+ "visitTypeMstr.visitTypeCode as visitTypeName, "
			+ "tariffVisit.status as status "
			+ "FROM TariffVisitTypeMapper tariffVisit "
			+ "INNER JOIN tariffVisit.visitTypeMaster visitTypeMstr "
			+ "WHERE tariffVisit.tariffId IN (:tariffIdList) "
			+ "AND tariffVisit.status='A' "
			+ " ORDER BY visitTypeMstr.visitTypeCode ASC "
		 ),
	
	@NamedQuery(name="GET_PATIENT_TYPE_LIST_BY_TARIFF_ID_LIST",
	query="SELECT tariffPatientMpper.tariffPatientTypeMapperId as tariffPatientTypeMapperId, "
			+ "tariffPatientMpper.tariffId as tariffId, "
			+ "tariffPatientMpper.patientTypeId as patientTypeId, "
			+ "patientCategoryMstr.patientCategory as patientCategory, "
			+ "tariffPatientMpper.status as status "
			+ "FROM TariffPatientTypeMapper tariffPatientMpper "
			+ "INNER JOIN tariffPatientMpper.patientCategoryMaster patientCategoryMstr "
			+ "WHERE tariffPatientMpper.tariffId IN (:tariffIdList) "
			+ "AND tariffPatientMpper.status='A' "),
	
	@NamedQuery(name="GET_BILLING_BED_CATEGORY_BY_TARIFFID_LIST",
	query="SELECT tariffBillingBedMpper.tariffBillingBedCategoryMapperId as tariffBillingBedCategoryMapperId, "
			+ "tariffBillingBedMpper.tariffId as tariffId, "
			+ "tariffBillingBedMpper.billingBedCategoryId as billingBedCategoryId, "
			+ "billingBedCatMstr.billingBedDesc as billingBedDesc, "
			+ "tariffBillingBedMpper.status as status "
			+ "FROM TariffBillingBedCategoryMapper tariffBillingBedMpper "
			+ "INNER JOIN tariffBillingBedMpper.billingBedCategoryMaster billingBedCatMstr "
			+ "WHERE tariffBillingBedMpper.tariffId IN (:tariffIdList) "
			+ "AND tariffBillingBedMpper.status='A' "),
	
	@NamedQuery(name="GET_VISIT_TYPE_LIST_BY_TARIFF_ID",
	query="SELECT tariffVisit.tariffVisitTypeMapperId as tariffVisitTypeMapperId, "
			+ "tariffVisit.tariffId as tariffId, "
			+ "tariffVisit.visitTypeId as visitTypeId, "
			+ "visitMstr.visitTypeCode as visitTypeName, "
			+ "tariffVisit.status as status "
			+ "FROM TariffVisitTypeMapper tariffVisit "
			+ "INNER JOIN tariffVisit.visitTypeMaster visitMstr "
			+ "WHERE tariffVisit.tariffId=:tariffId "
			+ "AND tariffVisit.status='A' "),
	
	@NamedQuery(name="GET_PATIENT_TYPE_LIST_BY_TARIFF_ID",
	query="SELECT tariffPatientType.tariffPatientTypeMapperId as tariffPatientTypeMapperId, "
			+ "tariffPatientType.tariffId as tariffId, "
			+ "tariffPatientType.patientTypeId as patientTypeId, "
			+ "patientCatMstr.patientCategory as patientCategory, "
			+ "tariffPatientType.status as status "
			+ "FROM TariffPatientTypeMapper tariffPatientType "
			+ "INNER JOIN tariffPatientType.patientCategoryMaster patientCatMstr "
			+ "WHERE tariffPatientType.tariffId=:tariffId "
			+ "AND tariffPatientType.status='A' "),
	
	@NamedQuery(name="GET_BILLING_BED_CATEGORY_LIST_BY_TARIFF_ID",
	query="SELECT tariffBillingBed.tariffBillingBedCategoryMapperId as tariffBillingBedCategoryMapperId, "
			+ "tariffBillingBed.tariffId as tariffId, "
			+ "tariffBillingBed.billingBedCategoryId as billingBedCategoryId, "
			+ "billingBedMstr.billingBedDesc as billingBedDesc, "
			+ "tariffBillingBed.status as status "
			+ "FROM TariffBillingBedCategoryMapper tariffBillingBed "
			+ "INNER JOIN tariffBillingBed.billingBedCategoryMaster billingBedMstr "
			+ "WHERE tariffBillingBed.tariffId=:tariffId "
			+ "AND tariffBillingBed.status='A' ")
	
	
	
}) 


@Entity
@Table(name="m_tariff_master",schema="service")
@SequenceGenerator(name="tariff_master_seq",sequenceName="service.tariff_master_seq",allocationSize=1)
public class TariffMaster {

	@Id
	@Column(name="tariff_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="tariff_master_seq")
	private Integer tariffId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="tariff_code")
	private String tariffCode;
	
	@Column(name="tariff_desc")
	private String tariffDesc;
	
	@Column(name="valid_from")
	private Date validFrom;
	
	@Column(name="valid_to")
	private Date validTo;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="status")
	private Character status;

	@Column(name="letter_head_image_tariff")
	private Character letterHeadImageTariff;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tariffMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TariffPaymentEntitlementMapper> tariffPaymentEntitlementMappersList;
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "tariffMaster")
	private List<MCompanyContractMaster> listMCompanyContractMaster;
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "tariffMaster")
	private List<MPackageMaster> listMPackageMaster;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tariffMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TariffVisitTypeMapper> tariffVisitTypeMapperList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tariffMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TariffPatientTypeMapper> tariffPatientTypeMapperList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tariffMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<TariffBillingBedCategoryMapper> tariffBillingBedCategoryMapperList;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "tariffMaster")
	private List<SurgeryResourceConfig> listResourceConfig;
	
	public List<TariffPaymentEntitlementMapper> getTariffPaymentEntitlementMappersList() {
		return tariffPaymentEntitlementMappersList;
	}

	public void setTariffPaymentEntitlementMappersList(
			List<TariffPaymentEntitlementMapper> tariffPaymentEntitlementMappersList) {
		this.tariffPaymentEntitlementMappersList = tariffPaymentEntitlementMappersList;
	}

	public List<MPackageMaster> getListMPackageMaster() {
		return listMPackageMaster;
	}

	public void setListMPackageMaster(List<MPackageMaster> listMPackageMaster) {
		this.listMPackageMaster = listMPackageMaster;
	}

	public List<MCompanyContractMaster> getListMCompanyContractMaster() {
		return listMCompanyContractMaster;
	}

	public void setListMCompanyContractMaster(List<MCompanyContractMaster> listMCompanyContractMaster) {
		this.listMCompanyContractMaster = listMCompanyContractMaster;
	}
	
	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
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

	public String getTariffCode() {
		return tariffCode;
	}

	public void setTariffCode(String tariffCode) {
		this.tariffCode = tariffCode;
	}

	public String getTariffDesc() {
		return tariffDesc;
	}

	public void setTariffDesc(String tariffDesc) {
		this.tariffDesc = tariffDesc;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
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

	public List<TariffVisitTypeMapper> getTariffVisitTypeMapperList() {
		return tariffVisitTypeMapperList;
	}

	public void setTariffVisitTypeMapperList(List<TariffVisitTypeMapper> tariffVisitTypeMapperList) {
		this.tariffVisitTypeMapperList = tariffVisitTypeMapperList;
	}

	public List<TariffPatientTypeMapper> getTariffPatientTypeMapperList() {
		return tariffPatientTypeMapperList;
	}

	public void setTariffPatientTypeMapperList(List<TariffPatientTypeMapper> tariffPatientTypeMapperList) {
		this.tariffPatientTypeMapperList = tariffPatientTypeMapperList;
	}

	public List<TariffBillingBedCategoryMapper> getTariffBillingBedCategoryMapperList() {
		return tariffBillingBedCategoryMapperList;
	}

	public void setTariffBillingBedCategoryMapperList(
			List<TariffBillingBedCategoryMapper> tariffBillingBedCategoryMapperList) {
		this.tariffBillingBedCategoryMapperList = tariffBillingBedCategoryMapperList;
	}

	public List<SurgeryResourceConfig> getListResourceConfig() {
		return listResourceConfig;
	}

	public void setListResourceConfig(List<SurgeryResourceConfig> listResourceConfig) {
		this.listResourceConfig = listResourceConfig;
	}
	
	
}


/*
 * "applicablePaymentEntitlementIdList":  
 * "createdBy" : 1 
 * "createdDate" : "08-05-2018 13:51:57"
 * "organizationId" : 1 
 * "status" : "A" 
 * "tariffCode" : "test" 
 * "tariffDesc" : "yesy"
 * "unitId" : 1 
 * "updatedBy" : 1 
 * "updatedDate" : "08-05-2018 13:51:57" 
 * "validFrom" : "06-05-2018 00:00:00" 
 * "validTo" : "08-05-2018 00:00:00"
 */
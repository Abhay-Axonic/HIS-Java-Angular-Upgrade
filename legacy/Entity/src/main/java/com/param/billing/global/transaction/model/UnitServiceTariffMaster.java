package com.param.billing.global.transaction.model;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.unit.model.BillingBedCategoryMaster;
import com.param.global.model.PatientCategoryMaster;
import com.param.global.model.PaymentEntitlementMaster;
import com.param.global.model.ServiceMaster;
import com.param.global.model.VisitTypeMaster;

@NamedQueries({
	
	@NamedQuery(name="GET_BASE_PRICE_OF_SERVICE_FROM_TARIFF",query="SELECT ustm.unitServiceTriffId as unitServiceTriffId, "
			+ "ustm.rate as rate " 
			+ "FROM UnitServiceTariffMaster ustm "
			+ "WHERE ustm.serviceId=:serviceId "
			+ "AND ustm.tariffId=:tariffId "
			+ "AND ustm.paymentEntitlementId=:paymentEntitlementId "
			+ "AND ustm.visitTypeId=:visitTypeId "
			+ "AND ustm.patientTypeId=:patientTypeId "
			+ "AND ustm.billingBedCategoryId=:billingBedCategoryId "
			+ "AND ustm.organizationId=:organizationId "
			+ "AND ustm.unitId=:unitId "
			+ "AND ustm.effectiveDate<:orderDate "),
	
	@NamedQuery(name="GET_UNIT_SERVICE_TARIFF_MASTER_LIST",query="SELECT ustm.tariffId as tariffId, "
			+ "tariffMaster.tariffDesc as tariffDesc, "
			+ "ustm.visitTypeId as visitTypeId, "
			+ "visitTypeMaster.visitTypeName as visitTypeDesc, "
			+ "ustm.paymentEntitlementId as paymentEntitlementId, "
			+ "paymentEntitlementMaster.paymentEntitlementDesc as paymentEntitlementDesc, "
			+ "ustm.patientTypeId as patientTypeId, "
			+ "patientCategoryMaster.patientCategory as patientTypeDesc, "
			+ "ustm.billingBedCategoryId as billingBedCategoryId, "
			+ "billingBedCategoryMaster.billingBedDesc as billingBedCatDesc, "
			+ "ustm.serviceId as serviceId, "
			+ "serviceMaster.serviceStandardName as serviceDesc, "
			+ "ustm.rate as rate, "
			+ "to_char(ustm.statPer,'999D9') as statPer, "
			+ "to_char(ustm.effectiveDate,'dd/mm/yyyy') as effectiveDate, "
			+ "serviceMaster.specialityId as specialityId, "
			+ "speciality.specialityName as specialityDesc, "
			+ "serviceMaster.subSpecialityId as subSpecialityId, "
			+ "subSpeciality.subSpecialityMasterName as subSpecialityDesc, "
			+ "ustm.serviceCode as serviceCode  "
			+ "FROM UnitServiceTariffMaster ustm "
			+ "INNER JOIN ustm.serviceMaster serviceMaster "
			+ "INNER JOIN serviceMaster.specialityMaster speciality "
			+ "INNER JOIN serviceMaster.subSpecialityMaster subSpeciality "
			+ "INNER JOIN ustm.tariffMaster tariffMaster "
			+ "INNER JOIN ustm.visitTypeMaster visitTypeMaster "
			+ "INNER JOIN ustm.patientCategoryMaster patientCategoryMaster "
			+ "INNER JOIN ustm.paymentEntitlementMaster paymentEntitlementMaster "
			+ "INNER JOIN ustm.billingBedCategoryMaster as billingBedCategoryMaster "
			+ "WHERE ustm.organizationId=:organizationId "
			+ "AND ustm.unitId=:unitId AND ustm.status='A' "),

		@NamedQuery(name="GET_UNIT_SERVICE_TARIFF_MASTER_LIST_FOR_WEB",query="SELECT ustm.tariffId as tariffId, "
				+ "tariffMaster.tariffDesc as tariffDesc, "
				+ "ustm.visitTypeId as visitTypeId, "
				+ "visitTypeMaster.visitTypeName as visitTypeDesc, "
				+ "ustm.paymentEntitlementId as paymentEntitlementId, "
				+ "paymentEntitlementMaster.paymentEntitlementDesc as paymentEntitlementDesc, "
				+ "ustm.patientTypeId as patientTypeId, "
				+ "patientCategoryMaster.patientCategory as patientTypeDesc, "
				+ "ustm.billingBedCategoryId as billingBedCategoryId, "
				+ "billingBedCategoryMaster.billingBedDesc as billingBedCatDesc, "
				+ "ustm.serviceId as serviceId, "
				+ "serviceMaster.serviceStandardName as serviceDesc, "
				+ "ustm.rate as rate, "
				+ "to_char(ustm.statPer,'999D9') as statPer, "
				+ "to_char(ustm.effectiveDate,'dd/mm/yyyy') as effectiveDate, "
				+ "serviceMaster.specialityId as specialityId, "
				+ "speciality.specialityName as specialityDesc, "
				+ "serviceMaster.subSpecialityId as subSpecialityId, "
				+ "subSpeciality.subSpecialityMasterName as subSpecialityDesc, "
				+ "ustm.serviceCode as serviceCode  "
				+ "FROM UnitServiceTariffMaster ustm "
				+ "INNER JOIN ustm.serviceMaster serviceMaster "
				+ "INNER JOIN serviceMaster.specialityMaster speciality "
				+ "INNER JOIN serviceMaster.subSpecialityMaster subSpeciality "
				+ "INNER JOIN ustm.tariffMaster tariffMaster "
				+ "INNER JOIN ustm.visitTypeMaster visitTypeMaster "
				+ "INNER JOIN ustm.patientCategoryMaster patientCategoryMaster "
				+ "INNER JOIN ustm.paymentEntitlementMaster paymentEntitlementMaster "
				+ "INNER JOIN ustm.billingBedCategoryMaster as billingBedCategoryMaster "
				+ "WHERE ustm.organizationId=:organizationId "
				+ "AND ustm.unitId=:unitId AND ustm.status='A' and serviceMaster.status='A' and speciality.status='A' and subSpeciality.status='A' "),
	
	/*@NamedQuery(name="GET_UNIT_PROFIT_MARGIN_LIST",query="SELECT ustm.visitTypeId as visitTypeId, "
			+ "visitTypeMaster.visitTypeName as visitTypeDesc, "
			+ "ustm.paymentEntitlementId as paymentEntitlementId, "
			+ "paymentEntitlementMaster.paymentEntitlementDesc as paymentEntitlementDesc, "
			+ "ustm.patientTypeId as patientTypeId, "
			+ "patientCategoryMaster.patientCategory as patientTypeDesc, "
			+ "ustm.billingBedCategoryId as billingBedCategoryId, "
			+ "billingBedCategoryMaster.billingBedDesc as billingBedCatDesc, "
			+ "ustm.serviceId as serviceId, "
			+ "ustm.rate as rate, "
			+ "serviceMaster.serviceStandardName as serviceDesc, "
			+ "serviceMaster.specialityId as specialityId, "
			+ "speciality.specialityName as specialityDesc, "
			+ "serviceMaster.subSpecialityId as subSpecialityId, "
			+ "subSpeciality.subSpecialityMasterName as subSpecialityDesc "
			+ "FROM UnitServiceTariffMaster ustm "
			+ "INNER JOIN ustm.serviceMaster serviceMaster "
			+ "INNER JOIN serviceMaster.specialityMaster speciality "
			+ "INNER JOIN serviceMaster.subSpecialityMaster subSpeciality "
			+ "INNER JOIN ustm.visitTypeMaster visitTypeMaster "
			+ "INNER JOIN ustm.patientCategoryMaster patientCategoryMaster "
			+ "INNER JOIN ustm.paymentEntitlementMaster paymentEntitlementMaster "
			+ "INNER JOIN ustm.billingBedCategoryMaster as billingBedCategoryMaster "
			+ "WHERE ustm.organizationId=:organizationId "
			+ "AND ustm.unitId=:unitId "),*/
	
	/*@NamedQuery(name="GET_UNIT_PROFIT_MARGIN_LIST",query="select spm.speciality_name  as specialityDesc, "
			+ "spm.speciality_id as specialityId, "
			+ "sspm.sub_speciality_id as subSpecialityId, "
			+ "sspm.sub_speciality_name as specialityDesc,"
			+ "ustm.patient_type_id as patientTypeId,"
			+ "ustm.service_id as serviceId ," + 
			"ustm.visit_type_id as visitTypeId,"
			+ "ustm.billing_bed_category_id as billingBedCategoryId,"
			+ "visitTypeMaster.visit_type_name as visitTypeDesc,,"
			+ "pcm.patient_category as patientTypeDesc," 
			+  "ustm.payment_entitlement_id as paymentEntitlementId,"
			+ "pem.payment_entitlement_desc as paymentEntitlementDesc,"
			+ "ustm.rate as rate," + 
			"(select sum(tcma.\"cost\") from lab.m_test_costing_master tcm " + 
			"inner join lab.m_test_costing_mapper tcma on tcma.test_costing_master_id=tcm.test_costing_master_id where tcm.service_id=ustm.service_id ) as serviceCost " + 
			"from service.m_unit_service_tariff_master ustm " + 
			"inner join service.m_service_master sm on ustm.service_id=sm.service_master_id " + 
			"inner join doctor.m_speciality_master spm on spm.speciality_id=sm.speciality_id " + 
			"inner join doctor.m_sub_speciality_master sspm on sspm.sub_speciality_id=sm.sub_speciality_id " + 
			"INNER JOIN public.m_visit_type_master visitTypeMaster on visitTypeMaster.visit_type_id=ustm.visit_type_id " + 
			"inner join public.m_patient_category_master pcm on pcm.patient_category_id=ustm.patient_type_id " + 
			"inner join public.m_payment_entitlement_master pem on pem.payment_entitlement_id=ustm.payment_entitlement_id"
			+ " WHERE ustm.organizationId=:organizationId " + 
			"	AND ustm.unitId=:unitId "),*/
	
	/*@NamedQuery(name="GET_UNIT_PROFIT_MARGIN_LIST",query="select spm.speciality_name,"
			+ "sspm.sub_speciality_id,"
			+ "sspm.sub_speciality_name,"
			+ "ustm.patient_type_id,"
			+ "ustm.visit_type_id,"
			+ "ustm.billing_bed_category_id,"
			+ "visitTypeMaster.visit_type_name,"
			+ "pcm.patient_category,"
			+ "ustm.payment_entitlement_id,"
			+ "pem.payment_entitlement_desc" 
			+ "from service.m_unit_service_tariff_master ustm"
			+ "inner join service.m_service_master sm on ustm.service_id=sm.service_master_id" 
			+ "inner join doctor.m_speciality_master spm on spm.speciality_id=sm.speciality_id" 
			+ "inner join doctor.m_sub_speciality_master sspm on sspm.sub_speciality_id=sm.sub_speciality_id" 
			+ "INNER JOIN public.m_visit_type_master visitTypeMaster on visitTypeMaster.visit_type_id=ustm.visit_type_id" 
			+ "inner join public.m_patient_category_master pcm on pcm.patient_category_id=ustm.patient_type_id"
			+ "inner join public.m_payment_entitlement_master pem on pem.payment_entitlement_id=ustm.payment_entitlement_id" 
			+ "where ustm.service_id=1 and spm.speciality_id=1 and sspm.sub_speciality_id=1 and  ustm.payment_entitlement_id=1" 
			+ "and ustm.visit_type_id=1 and ustm.billing_bed_category_id=1 and ustm.patient_type_id=1")*/
	/*@NamedQuery(name="GET_SERVICELIST_BY_SUBSPECIALITYID",
	query="SELECT "
			+ "ustm.serviceId as serviceId, "
			+ "serviceMaster.serviceStandardName as serviceDesc, "
			+ "serviceMaster.serviceStandardCode as serviceCode, "
			+ "ustm.rate as rate, "
			+ "serviceMaster.specialityId as specialityId, "
			+ "speciality.specialityName as specialityDesc, "
			+ "serviceMaster.subSpecialityId as subSpecialityId, "
			+ "subSpeciality.subSpecialityMasterName as subSpecialityDesc "
			+ "FROM UnitServiceTariffMaster ustm "
			+ "INNER JOIN ustm.serviceMaster serviceMaster "
			+ "INNER JOIN serviceMaster.specialityMaster speciality "
			+ "INNER JOIN serviceMaster.subSpecialityMaster subSpeciality "
			+ "WHERE ustm.organizationId=:organizationId "
			+ "AND ustm.unitId=:unitId "
			+ "AND ustm.tariffId=:tariffId "
			+ "AND ustm.patientTypeId=:patientTypeId "
			+ "AND ustm.billingBedCategoryId=:billingBedCategoryId "
			+ "AND ustm.visitTypeId=:visitTypeId "
			+ "AND ustm.paymentEntitlementId=:paymentEntitlementId "
			+ "AND ustm.effectiveDate<=:effectiveDate "
			+ "AND serviceMaster.subSpecialityId IN (:subSpecialityList) "),*/
	
	
})

@NamedNativeQueries({
	@NamedNativeQuery(name="GET_UNIT_PROFIT_MARGIN_LIST",
			query="select spm.speciality_name  as \"specialityDesc\", sm.service_standard_name as \"serviceDesc\","
					+ "spm.speciality_id as \"specialityId\", sspm.sub_speciality_id as \"subSpecialityId\",sspm.sub_speciality_name as \"suSpecialityDesc\",ustm.patient_type_id as \"patientTypeId\", "
					+ "ustm.service_id as \"serviceId\",ustm.visit_type_id as \"visitTypeId\",ustm.billing_bed_category_id as \"billingBedCategoryId\",bbcm.billing_bed_desc as \"billingBedCatDesc\",visitTypeMaster.visit_type_name as \"visitTypeDesc\","
					+ "pcm.patient_category as \"patientTypeDesc\",ustm.payment_entitlement_id as \"paymentEntitlementId\",pem.payment_entitlement_desc as \"paymentEntitlementDesc\",ustm.rate as \"rate\","
					+ "(select sum(tcma.cost) from lab.m_test_costing_master tcm " 
					+" inner join lab.m_test_costing_mapper tcma on tcma.test_costing_master_id=tcm.test_costing_master_id where tcm.service_id=ustm.service_id ) as \"serviceCost\" "
					+ " from service.m_unit_service_tariff_master ustm "
					+"	inner join service.m_service_master sm on ustm.service_id=sm.service_master_id "
					+"	inner join doctor.m_speciality_master spm on spm.speciality_id=sm.speciality_id " 
					+"  inner join adt.m_billing_bed_category_master bbcm on bbcm.billing_bed_category_id= ustm.billing_bed_category_id "
					+"	inner join doctor.m_sub_speciality_master sspm on sspm.sub_speciality_id=sm.sub_speciality_id " 
					+"	INNER JOIN public.m_visit_type_master visitTypeMaster on visitTypeMaster.visit_type_id=ustm.visit_type_id " 
					+"	inner join public.m_patient_category_master pcm on pcm.patient_category_id=ustm.patient_type_id " 
					+"	inner join public.m_payment_entitlement_master pem on pem.payment_entitlement_id=ustm.payment_entitlement_id "
					+ " WHERE ustm.organization_id=:organizationId AND ustm.unit_id=:unitId  "),

			@NamedNativeQuery(name="GET_SERVICELIST_BY_SUBSPECIALITYID",
			query="select "
			+ "ustm.service_id as \"serviceId\", "
			+ "serviceMaster.service_standard_name as  \"serviceDesc\", "
			+ "serviceMaster.service_standard_code as  \"serviceCode\", "
			+ "ustm.rate as  \"serviceRate\", "
			+ "serviceMaster.speciality_id as  \"specialityId\", "
			+ "speciality.speciality_name as  \"specialityDesc\", "
			+ "serviceMaster.sub_speciality_id as  \"subSpecialityId\", "
			+ "subSpeciality.sub_speciality_name as  \"subSpecialityDesc\", "
			+ "details.gst_type_id as \"taxId\"," 
			+ "tax.tax_percentage as \"taxPercentage\", "
			+ "(ustm.rate+(ustm.rate*(tax.tax_percentage*0.01))) as \"rate\" "
			+ "from "
			+ "service.m_unit_service_tariff_master ustm "
			+ "inner join service.m_service_master serviceMaster on "
			+ "ustm.service_id = serviceMaster.service_master_id "
			+ "inner join service.t_unit_service_master_details details on "
			+ "ustm.service_id = details.service_master_id "
			+ "inner join m_tax tax on "
			+ "	tax.id = details.gst_type_id "
			+ "inner join doctor.m_speciality_master speciality on "
			+ "serviceMaster.speciality_id = speciality.speciality_id "
			+ "inner join doctor.m_sub_speciality_master subSpeciality on "
			+ "serviceMaster.sub_speciality_id = subSpeciality.sub_speciality_id "
			+ "where "
			+ "ustm.organization_id =:organizationId "
			+ "AND ustm.unit_id=:unitId "
			+ "AND details.unit_id=ustm.unit_id "
			+ "AND ustm.tariff_id=:tariffId "
			+ "AND ustm.patient_type_id=:patientTypeId "
			+ "AND ustm.billing_bed_category_id=:billingBedCategoryId "
			+ "AND ustm.visit_type_id=:visitTypeId "
			+ "AND ustm.payment_entitlement_id=:paymentEntitlementId "
			+ "and ustm.effective_date =( "
			+ "select "
			+ "MAX(ustm.effective_date) "
			+ "from "
			+ "service.m_unit_service_tariff_master ustm "
			+ "inner join service.m_service_master serviceMaster on "
			+ "ustm.service_id = serviceMaster.service_master_id "
			+ "inner join service.t_unit_service_master_details details on "
			+ "ustm.service_id = details.service_master_id "
			+ "inner join m_tax tax on "
			+ "	tax.id = details.gst_type_id "
			+ "inner join doctor.m_speciality_master speciality on "
			+ "serviceMaster.speciality_id = speciality.speciality_id "
			+ "inner join doctor.m_sub_speciality_master subSpeciality on "
			+ "serviceMaster.sub_speciality_id = subSpeciality.sub_speciality_id "
			+ "where "
			+ "ustm.organization_id =:organizationId "
			+ "AND details.unit_id=ustm.unit_id "
			+ "AND ustm.unit_id=:unitId "
			+ "AND ustm.tariff_id=:tariffId "
			+ "AND ustm.patient_type_id=:patientTypeId "
			+ "AND ustm.billing_bed_category_id=:billingBedCategoryId "
			+ "AND ustm.visit_type_id=:visitTypeId "
			+ "AND ustm.payment_entitlement_id=:paymentEntitlementId "
			+ "and ustm.effective_date <=:effectiveDate "
			+ "and (serviceMaster.sub_speciality_id IN (:subSpecialityList)) "
			+ ") "
			+ "and (serviceMaster.sub_speciality_id IN (:subSpecialityList)) "),
			
			
			@NamedNativeQuery(name="GET_SERVICELIST_BY_SUBSPECIALITYID_NOT_BY_TARIFF",
			query="	select "
					 + " 	serviceMaster.service_master_id as \"serviceId\", "
					 + " 	serviceMaster.service_standard_name as \"serviceDesc\", "
					 + " 	serviceMaster.service_standard_code as \"serviceCode\", "
					 + " 	basePrice.base_price as \"serviceRate\", "
					 + " 	serviceMaster.speciality_id as \"specialityId\", "
					 + " 	speciality.speciality_name as \"specialityDesc\", "
					 + " 	serviceMaster.sub_speciality_id as \"subSpecialityId\", "
					 + " 	subSpeciality.sub_speciality_name as \"subSpecialityDesc\", "
					 + " 	details.gst_type_id as \"taxId\", "
					 + " 	coalesce(tax.tax_percentage,0) as \"taxPercentage\", "
					 + " 	(basePrice.base_price +(basePrice.base_price * (coalesce(tax.tax_percentage,0)*0.01))) as \"rate\" "
					 + " from "
					 + " 	service.m_service_master serviceMaster "
					 + " inner join service.t_unit_service_master_details details on "
					 + " 	serviceMaster.service_master_id = details.service_master_id "
					 + " inner join service.m_service_base_price_master basePrice on "
					 + " 	basePrice.service_master_id = serviceMaster.service_master_id "
					 + " left join m_tax tax on "
					 + " 	tax.id = details.gst_type_id "
					 + " inner join doctor.m_speciality_master speciality on "
					 + " 	serviceMaster.speciality_id = speciality.speciality_id "
					 + " inner join doctor.m_sub_speciality_master subSpeciality on "
					 + " 	serviceMaster.sub_speciality_id = subSpeciality.sub_speciality_id "
					 + " where "
					 + " 	details.organization_id =:organizationId "
					 + " 	and details.unit_id =:unitId "
					 + "	and details.is_periodicity='N' "
					 + "	and details.is_package='I' "
					 + " 	and details.unit_id = basePrice.unit_id "
					// + " 	and :date between basePrice.from_date and basePrice.to_date "
					 + " 	and (serviceMaster.sub_speciality_id in (:subSpecialityList)) ")
})


@Entity
@Table(name="m_unit_service_tariff_master",schema="service")
@SequenceGenerator(name="unit_service_tariff_master_seq",sequenceName="service.unit_service_tariff_master_seq",allocationSize=1)
public class UnitServiceTariffMaster {

	@Id
	@Column(name="unit_service_tariff_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="unit_service_tariff_master_seq")
	private Integer unitServiceTriffId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="service_id")
	private Integer serviceId;
	
	@Column(name="tariff_id")
	private Integer tariffId;
	
	@Column(name="payment_entitlement_id")
	private Integer paymentEntitlementId;
	
	@Column(name="visit_type_id")
	private Integer visitTypeId;
	
	@Column(name="patient_type_id")
	private Integer patientTypeId;
	
	@Column(name="billing_bed_category_id")
	private Integer billingBedCategoryId;
	
	@Column(name="rate")
	private Double rate;
	
	@Column(name="stat_per")
	private Double statPer;
	
	@Column(name="effective_date")
	private Date effectiveDate;
	
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
	
	@Column(name="service_code")
	private String serviceCode;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", insertable = false, nullable = false, updatable = false)
	private ServiceMaster serviceMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tariff_id", insertable = false, nullable = false, updatable = false)
	private TariffMaster tariffMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "visit_type_id", insertable = false, nullable = false, updatable = false)
	private VisitTypeMaster visitTypeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_type_id", insertable = false, nullable = false, updatable = false)
	private PatientCategoryMaster patientCategoryMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "billing_bed_category_id", insertable = false, nullable = false, updatable = false)
	private BillingBedCategoryMaster billingBedCategoryMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_entitlement_id", insertable = false, nullable = false, updatable = false)
	private PaymentEntitlementMaster paymentEntitlementMaster;

	public Integer getUnitServiceTriffId() {
		return unitServiceTriffId;
	}

	public void setUnitServiceTriffId(Integer unitServiceTriffId) {
		this.unitServiceTriffId = unitServiceTriffId;
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

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getStatPer() {
		return statPer;
	}

	public void setStatPer(Double statPer) {
		this.statPer = statPer;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
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

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	
}


/*CREATE TABLE service.m_unit_service_tariff_master
(
  unit_service_tariff_id integer NOT NULL DEFAULT nextval('service.unit_service_tariff_master_seq'::regclass),
  organization_id integer,
  unit_id integer,
  service_id integer,
  tariff_id integer ,
  payment_entitlement_id integer, 	
  visit_type_id integer,
  patient_type_id integer,
  billing_bed_category_id integer,
  rate double precision,
  stat_per double precision,
  effective_date timestamp without time zone,
  created_by integer,
  created_date timestamp without time zone DEFAULT now(),
  status character(1) DEFAULT 'A'::"char",
  updated_by integer,
  updated_date timestamp without time zone DEFAULT now(),
  CONSTRAINT pk_unit_service_tariff_id PRIMARY KEY (unit_service_tariff_id)
)*/
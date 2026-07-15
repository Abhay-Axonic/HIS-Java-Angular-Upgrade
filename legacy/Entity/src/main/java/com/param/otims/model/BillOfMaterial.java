package com.param.otims.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.ServiceMaster;
@NamedQueries({
	
	@NamedQuery(name="GET_BILL_OF_MATERIAL_LIST",
		query=" SELECT  distinct mstr.billOfMaterialId as billOfMaterialId ,"
				+ " mstr.procedureId as procedureId,"
				+ " mstr.serviceId as serviceId,"
				+ " serviceMaster.specialityId as specialityId,"
				+ " mstr.billingBedCatId as billingBedCatId,"
				+ " mstr.tariffId as tariffId,"
				+ " mstr.visitTypeId as visitTypeId,"
				+ " mstr.paymentEntitlementId as paymentEntitlementId,"
				+ " mstr.patientCategoryId as patientCategoryId,"
				+ " mstr.amount as amount,"
				+ " mstr.amount as originalAmount,"
				+ " mstr.status as status,"
				+ " mstr.organizationId as organizationId,"
				+ " unitswerv.isDocReq as isRefDoctorShare, "
				+ " mstr.unitId as unitId,"
				+ " mstr.createdBy as createdBy,"
				+ " to_char(mstr.createdDate, 'dd-MM-yyyy HH:ss:mm')as createdDate, "
				+ " to_char(mstr.updatedDate, 'dd-MM-yyyy HH:ss:mm')as updatedDate, "
				+ " mstr.updatedBy as updatedBy ,"
				+ " serviceMaster.serviceStandardName as serviceName "
				+ " FROM BillOfMaterial mstr "
				+ "inner join mstr.serviceMaster serviceMaster "
				+ "inner join serviceMaster.unitServiceMasterDetailsList unitswerv "
				+ " WHERE mstr.status = 'A'"
//				+ " AND mstr.procedureId = :procedureId "
				+ " AND mstr.organizationId= :organizationId"
				+ " AND mstr.unitId= :unitId "
                + " AND unitswerv.organizationId = :organizationId "
                + " AND unitswerv.unitId = :unitId "
				),
	@NamedQuery(name="GET_BILL_OF_MATERIAL_LIST_ALL",
	query=" SELECT  distinct mstr.billOfMaterialId as billOfMaterialId ,"
			+ " mstr.procedureId as procedureId,"
			+ " mstr.serviceId as serviceId,"
			+ " mstr.billingBedCatId as billingBedCatId,"
			+ " mstr.visitTypeId as visitTypeId,"
			+ " mstr.amount as amount,"
			+ " mstr.amount as originalAmount,"
			+ " mstr.status as status,"
			+ " mstr.organizationId as organizationId,"
			+ " unitswerv.isDocReq as isRefDoctorShare, "
			+ " mstr.unitId as unitId,"
			+ " mstr.createdBy as createdBy,"
			+ " to_char(mstr.createdDate, 'dd-MM-yyyy HH:ss:mm')as createdDate, "
			+ " to_char(mstr.updatedDate, 'dd-MM-yyyy HH:ss:mm')as updatedDate, "
			+ " mstr.updatedBy as updatedBy ,"
			+ " serviceMaster.serviceStandardName as serviceName "
			+ " FROM BillOfMaterial mstr "
			+ "inner join mstr.serviceMaster serviceMaster "
			+ "inner join serviceMaster.unitServiceMasterDetailsList unitswerv "
			+ " WHERE "
			+ "  mstr.organizationId= :organizationId"
			+ " AND mstr.unitId= :unitId "
            + " AND unitswerv.organizationId = :organizationId "
            + " AND unitswerv.unitId = :unitId "
			),
})

@NamedNativeQuery(name="GET_BILL_OF_MATERIAL_PROCEDURE_LIST",
query=" select\r\n" + 
		"	distinct billofmate0_.procedure_id as \"procedureId\", " + 
		"	billofmate0_.surgery_grade_id as \"surgeryGradeId\", " + 
	//	"	billofmate0_.bill_of_material_id as \"billOfMaterialId\", " + 
		"	billofmate0_.is_grade as \"isGrade\", " + 
		"	billofmate0_.status as \"status\", " + 
		"	billofmate0_.organization_id as \"organizationId\", "+
		"   billofmate0_.visit_type_id as \"visitTypeId\", " + 
		"	billofmate0_.tariff_id as \"tariffId\", " + 
		"	tm.tariff_desc as \"tariffDesc\", " + 
		"	billofmate0_.payment_entitlement_id as \"paymentEntitlementId\", " + 
		"	billofmate0_.patient_category_id as \"patientCategoryId\", " + 
		"	pcm.patient_category as \"patientCategory\","+
		"   vtm.visit_type_name as \"visitTypeName\", " + 
		"	pem.payment_entitlement_desc as \"paymentEntitlementDesc\", " + 
		"	billofmate0_.unit_id as \"unitId\", " + 
		"	servicemas1_.service_standard_name as \"serviceName\", " + 
		"	servicemas1_.service_standard_code as \"serviceCode\", " + 
		"	sgm.surgery_grade as \"surgeryGrade\", " + 
		"	sgm.surgery_grade_code as \"surgeryGradeCode\" " + 
		" from " + 
		"	otims.t_bill_of_material billofmate0_ " + 
		"  join service.m_service_master servicemas1_ on " + 
		"	billofmate0_.procedure_id = servicemas1_.service_master_id " + 
		" left join public.m_surgery_grade_master sgm on " + 
		"	billofmate0_.surgery_grade_id = sgm.surgery_grade_id "+
		" left join service.m_tariff_master tm on " + 
		"    tm.tariff_id = billofmate0_.tariff_id " + 
		" left join public.m_patient_category_master  pcm on " + 
		"    pcm.patient_category_id = billofmate0_.patient_category_id "+
		" left join public.m_visit_type_master vtm on " + 
		"    vtm.visit_type_id = billofmate0_.visit_type_id " + 
		" left join public.m_payment_entitlement_master pem on " + 
		"    pem.payment_entitlement_id = billofmate0_.payment_entitlement_id " + 
		" where " + 
		"	 billofmate0_.organization_id =:organizationId" + 
		"	and billofmate0_.unit_id =:unitId"
		)

@Entity
@Table(name = "t_bill_of_material" , schema = "otims")
@SequenceGenerator(name = "bill_of_material_seq" , sequenceName = "otims.bill_of_material_seq" , allocationSize = 1)
public class BillOfMaterial {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "bill_of_material_seq")
	@Column(name = "bill_of_material_id")
	private Integer billOfMaterialId;
	
	@Column(name = "procedure_id")
	private Integer procedureId;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "billing_bed_cat_id")
	private Integer billingBedCatId;
	
	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "is_grade")
	private boolean isGrade;
	
	@Column(name = "surgery_grade_id")
	private Integer surgeryGradeId;
	
	@Column(name = "tariff_id")
	private Integer tariffId;
	
	@Column(name = "visit_type_id")
	private Integer visitTypeId;
	
	@Column(name = "payment_entitlement_id")
	private Integer paymentEntitlementId;
	
	@Column(name = "patient_category_id")
	private Integer patientCategoryId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="service_id", insertable=false, updatable=false)
	private ServiceMaster serviceMaster;

	public Integer getBillOfMaterialId() {
		return billOfMaterialId;
	}

	public void setBillOfMaterialId(Integer billOfMaterialId) {
		this.billOfMaterialId = billOfMaterialId;
	}

	public Integer getProcedureId() {
		return procedureId;
	}

	public void setProcedureId(Integer procedureId) {
		this.procedureId = procedureId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getBillingBedCatId() {
		return billingBedCatId;
	}

	public void setBillingBedCatId(Integer billingBedCatId) {
		this.billingBedCatId = billingBedCatId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public boolean getIsGrade() {
		return isGrade;
	}

	public void setIsGrade(boolean isGrade) {
		this.isGrade = isGrade;
	}

	public Integer getSurgeryGradeId() {
		return surgeryGradeId;
	}

	public void setSurgeryGradeId(Integer surgeryGradeId) {
		this.surgeryGradeId = surgeryGradeId;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	
	
	
	
}

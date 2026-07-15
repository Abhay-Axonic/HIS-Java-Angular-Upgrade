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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.unit.model.BillingBedCategoryMaster;
import com.param.entity.model.master.PatientCategory;
import com.param.global.model.PaymentEntitlementMaster;
import com.param.global.model.ServiceMaster;
import com.param.global.model.SpecialityMaster;
import com.param.global.model.VisitTypeMaster;

@NamedQueries({
	
	@NamedQuery(name="GET_SERVICE_WISE_GLOBAL_DOCTOR_SHARE_BY_DEPARTMENT_ID_AND_GRADE_ID",
		query="SELECT tGlobalDocShareServiceWise.specialityId as specialityId,"
					+ " sm.specialityName as specialityName,bbcm.billingBedDesc as billingBedDesc,"
					+ " pem.paymentEntitlementDesc as paymentEntitlementDesc,serviceMst.serviceStandardName as serviceName,"
					+ " vtm.visitTypeName as visitTypeName,tGlobalDocShareServiceWise.docShare as docShare,tGlobalDocShareServiceWise.amount as amount, "
					+ " to_char(tGlobalDocShareServiceWise.effectiveDate,'dd/MM/yyyy') as globalSharEffectiveDate, "
					+ " pc.patientCategory as patientCategory"
					+ " FROM TGlobalDocShareServiceWise tGlobalDocShareServiceWise"
					+ " INNER JOIN tGlobalDocShareServiceWise.specialityMaster sm "
					+ " INNER JOIN tGlobalDocShareServiceWise.billingBedCategoryMaster bbcm"
					+ " INNER JOIN tGlobalDocShareServiceWise.paymentEntitlementMaster pem"
					+ " LEFT JOIN tGlobalDocShareServiceWise.visitTypeMaster vtm"
					+ " INNER JOIN tGlobalDocShareServiceWise.patientCategory pc"
					+ " INNER JOIN tGlobalDocShareServiceWise.serviceMaster serviceMst"
					+ " WHERE tGlobalDocShareServiceWise.specialityId= :departmentId"
					+ " AND tGlobalDocShareServiceWise.orgId= :orgId"
					+ " AND tGlobalDocShareServiceWise.unitId= :orgUnitId"
					+ " AND tGlobalDocShareServiceWise.status= 'A' "
					+ " ORDER BY tGlobalDocShareServiceWise.globalShareWise_id desc "
					)
	
	})

@Entity
@Table(name = "t_global_doc_share_service_wise" , schema = "billing")
@SequenceGenerator(name = "t_seq_global_doc_share_service_wise" , sequenceName = "billing.t_seq_global_doc_share_service_wise" , allocationSize = 1)
public class TGlobalDocShareServiceWise {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_seq_global_doc_share_service_wise")
	@Column(name = "global_share_wise_id")
	private int globalShareWise_id;
	
	@Column(name = "speciality_id")
	private Integer specialityId;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "org_unit_id")
	private Integer unitId;
	
	@Column(name = "payment_entitlement_id")
	private Integer paymentEntitlementId;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "doc_share")
	private Double docShare;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "effective_date")
	private Date effectiveDate;
	
	@Column(name = "grade_id")
	private Integer gradeId;
	
	@Column(name = "billing_bed_category_id")
	private Integer billingBedCategoryId;
	
	@Column(name = "visit_type_id")
	private Integer visitTypeId;
	
	@Column(name = "patient_type_id")
	private Integer patientTypeId;

	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "tariff_id")
	private Integer tariffId;
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name  = "speciality_id" , insertable = false , updatable = false , nullable = false)
	private SpecialityMaster specialityMaster;
	
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name  = "sub_department_id" , insertable = false , updatable = false , nullable = false)
	private SubSpecialityMaster subSpecialityMaster;*/
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name  = "billing_bed_category_id" , insertable = false , updatable = false , nullable = false)
	private BillingBedCategoryMaster billingBedCategoryMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name  = "payment_entitlement_id" , insertable = false , updatable = false , nullable = false)
	private PaymentEntitlementMaster paymentEntitlementMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name  = "visit_type_id" , insertable = false , updatable = false , nullable = false)
	private VisitTypeMaster visitTypeMaster;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name  = "patient_type_id" , insertable = false , updatable = false , nullable = false)
	private PatientCategory patientCategory;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name  = "service_id" , insertable = false , updatable = false , nullable = false)
	private ServiceMaster serviceMaster;

	
	
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public int getGlobalShareWise_id() {
		return globalShareWise_id;
	}

	public void setGlobalShareWise_id(int globalShareWise_id) {
		this.globalShareWise_id = globalShareWise_id;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
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

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Double getDocShare() {
		return docShare;
	}

	public void setDocShare(Double docShare) {
		this.docShare = docShare;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Integer getGradeId() {
		return gradeId;
	}

	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public SpecialityMaster getSpecialityMaster() {
		return specialityMaster;
	}

	public void setSpecialityMaster(SpecialityMaster specialityMaster) {
		this.specialityMaster = specialityMaster;
	}

	public BillingBedCategoryMaster getBillingBedCategoryMaster() {
		return billingBedCategoryMaster;
	}

	public void setBillingBedCategoryMaster(BillingBedCategoryMaster billingBedCategoryMaster) {
		this.billingBedCategoryMaster = billingBedCategoryMaster;
	}

	public PaymentEntitlementMaster getPaymentEntitlementMaster() {
		return paymentEntitlementMaster;
	}

	public void setPaymentEntitlementMaster(PaymentEntitlementMaster paymentEntitlementMaster) {
		this.paymentEntitlementMaster = paymentEntitlementMaster;
	}

	public VisitTypeMaster getVisitTypeMaster() {
		return visitTypeMaster;
	}

	public void setVisitTypeMaster(VisitTypeMaster visitTypeMaster) {
		this.visitTypeMaster = visitTypeMaster;
	}
	

}

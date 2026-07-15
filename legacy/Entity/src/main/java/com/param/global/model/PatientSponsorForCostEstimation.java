package com.param.global.model;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.adt.admission.model.Admission;
import com.param.opd.coversheet.model.EncounterMaster;
import com.param.service.global.model.MCompanyContractMaster;

/*@NamedQueries({
	@NamedQuery(name="GET_SPONSOR_DETAILS_BY_ENCOUNTER_ID",query="SELECT patientSponsor.patientSponsorDetailsId as patientSponsorDetailsId, "
			+ "patientSponsor.encounterId as encounterId, "
			+ "patientSponsor.paymentEntitlementId as paymentEntitlementId, "
			+ "paymentEnt.paymentEntitlementDesc as paymentEntitlementDesc, "
			+ "patientSponsor.payeeId as payeeId, "
			+ "company.companyDesc as companyDesc, "
			+ "patientSponsor.priorityId as priorityId, "
			+ "patientSponsor.tariffId as tariffId, "
			+ "patientSponsor.contractId as contractId, "
			+ "comapnyContract.contractName as contractName, "
			+ "to_char(comapnyContract.validityFrom,'dd-mm-yyyy') as validityFrom, "
			+ "to_char(comapnyContract.validityTo,'dd-mm-yyyy') as validityTo,"
			+ "patientReg.genderId as genderId, patientReg.patientCategoryId as patientTypeId "
			+ "FROM PatientSponsorForCostEstimation patientSponsor "
			+ "INNER JOIN patientSponsor.paymentEntitlementMaster paymentEnt "
			+ "INNER JOIN patientSponsor.companyMaster company "
			+ "LEFT JOIN patientSponsor.mCompanyContractMaster comapnyContract"
			+ " INNER JOIN patientSponsor.encounterMaster.patientRegistration patientReg "
			+ "WHERE patientSponsor.encounterId=:encounterId ")
	
})
@NamedNativeQueries(
{ @NamedNativeQuery(name="GET_SPONSOR_DETAILS_BY_VISIT_TYPE_ID",
	query="SELECT"
			+ "   em.encounter_number as \"encounterNo\","
			+ "   em.encounter_id as \"encounterId\","
			+ "   pem.payment_entitlement_desc as \"paymentEntitlementDesc\","
			+ "   mtm.tariff_desc as \"tariffDesc\","
			+ "   psd.payee_id as \"payeeId\","
			+ "   ccm.contract_name as \"contractName\","
			+ "   tcm.company_desc as \"companyName\","
			+ "   psd.priority_id as \"priorityId\","
			+ "   mtm.tariff_Id as \"tariffId\","
			+ "   pem.payment_entitlement_id as \"companyId\","
			+ "   ccm.contract_id as \"contractId\","
			+ "  psd.patient_sponsor_details_id as \"patientSponsorDetailsId\","
			+ "  to_char(ccm.validity_from,'dd-mm-yyyy') as \"validityFrom\","
			+ "  to_char(ccm.validity_to,'dd-mm-yyyy') as \"validityTo\""
			+ " FROM"
			+ "   opd.t_patient_sponsor_details_cost_estimation psd"
			+ "   INNER JOIN opd.t_encounter_master em on em.encounter_id = psd.encounter_id"
			+ "   INNER JOIN public.m_payment_entitlement_master pem on pem.payment_entitlement_id = psd.payment_entitlement_id"
			+ "   LEFT JOIN service.m_company_contract_master ccm on ccm.contract_id = psd.contract_id"
			+ "     LEFT JOIN public.t_company_master tcm on tcm.company_id= ccm.company_id "
			+ "   LEFT JOIN service.m_tariff_master mtm on mtm.tariff_id = psd.tariff_id"
			+ " WHERE"
			+ "   psd.visit_type_id =:visitTypeId"
			+ "   AND patient_id =:patientId"
			+ "   AND psd.priority_id =1"
			+ " ORDER BY"
			+ "   em.encounter_id DESC,"
			+ "   psd.created_date DESC "),
	
	@NamedNativeQuery(name="GET_SPONSOR_DETAILS_BY_ADMISSION",
	query= " select  "
			 + "	en_mst.payment_entitlement_desc as \"paymentEntitlementDesc\",  "
			 + "	dtls.insurance_number as \"insuranceNumber\",  "
			 + "	cast(dtls.insured_amount as bigint) as \"insuredAmount\",  "
			 + "	to_char(dtls.valid_from,'DD-MM-YYYY') as \"validityFrom\",  "
			 + "	to_char(dtls.valid_to,'DD-MM-YYYY') as \"validityTo\",  "
			 + "	cast(contra_mst.co_share_percentage as double precision) as \"coSharePer\"	,  "
			 + "	comp_mst.company_desc as \"tpa\",  "
			 + "	insurance.insurance_desc as \"companyDesc\",contra_mst.contract_name as \"contractName\",contra_mst.contract_id as \"contractId\" "
			 + "from  "
			 + "	opd.t_patient_sponsor_details_cost_estimation dtls  "
			 + "left join public.m_payment_entitlement_master en_mst on  "
			 + "	dtls.payment_entitlement_id = en_mst.payment_entitlement_id  "
			 + "left join adt.t_insurance insurance on  "
			 + "	 dtls.insurance_id	= insurance.insurance_id   "
			 + "left join public.t_company_master comp_mst on  "
			 + "	comp_mst.company_id = dtls.payee_id  "
			 + "left join service.m_company_contract_master contra_mst on  "
			 + "	contra_mst.contract_id = dtls.contract_id  "
			 + "where  "
			 + "	dtls.admission_id = :admissionId  "
			 + "	and dtls.payment_entitlement_id = :paymentEntitlementId  "),
	
	
	@NamedNativeQuery(name="GET_SPONSOR_DETAILS_BY_ADMISSION_FOR_UPDATE_PAYEE",
	query= " select   " 
			 + " 	dtls.admission_id as \"admissionId\", " 
			 + " 	dtls.contract_id as \"contractId\", " 
			 + " 	dtls.insurance_id as \"insuranceId\", " 
			 + " 	dtls.payment_entitlement_id as \"paymentEntitlementId\", " 
			 + " 	en_mst.payment_entitlement_desc as \"paymentEntitlementDesc\",   " 
			 + " 	dtls.insurance_number as \"insuranceNumber\",   " 
			 + " 	dtls.payee_id as \"payeeId\", " 
			 + " 	dtls.priority_id as \"priorityId\", " 
			 + " 	dtls.tariff_id as \"tariffId\", " 
			 + " 	cast(dtls.insured_amount as bigint) as \"insuredAmount\",   " 
			 + " 	to_char(dtls.valid_from,'DD-MM-YYYY') as \"validityFrom\",   " 
			 + " 	to_char(dtls.valid_to,'DD-MM-YYYY') as \"validityTo\",   " 
			 + " 	comp_mst.company_desc as \"companyDesc\",   " 
			 + " 	comp_mst.company_id as \"companyId\",   "
			 + " 	insurance.insurance_desc as \"insuranceDesc\", " 
			 + " 	contra_mst.contract_name as \"contractName\", "
			 + "	tariff.tariff_desc as \"tariffDesc\", "
			 + "	contra_mst.billing_bed_category_id as \"billingBedCategoryId\" " 
			 + " from   " 
			 + " 	opd.t_patient_sponsor_details_cost_estimation dtls   " 
			 + " left join public.m_payment_entitlement_master en_mst on   " 
			 + " 	dtls.payment_entitlement_id = en_mst.payment_entitlement_id   " 
			 + " left join adt.t_insurance insurance on   " 
			 + " 	 dtls.insurance_id	= insurance.insurance_id    " 
			 + " left join public.t_company_master comp_mst on   " 
			 + " 	comp_mst.company_id = dtls.payee_id   " 
			 + " left join service.m_company_contract_master contra_mst on   " 
			 + " 	contra_mst.contract_id = dtls.contract_id   "
			 + " left join service.m_tariff_master tariff on  "
			 + " 	tariff.tariff_id = contra_mst.tariff_id	" 
			 + " where  dtls.status='A' " 
			  )
	
})*/


@Entity
@Table(name="t_patient_sponsor_details_cost_estimation",schema="opd")
public class PatientSponsorForCostEstimation {

	@Id
	@Column(name="patient_sponsor_details_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer patientSponsorDetailsId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="visit_type_id")
	private Integer visitTypeId;
	
	@Column(name="admission_id")
	private Integer admissionId;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="payment_entitlement_id")
	private Integer paymentEntitlementId;
	
	@Column(name="payee_id")
	private Integer payeeId;
	
	@Column(name="priority_id")
	private Integer priorityId;
	
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
	
	@Column(name="is_contract_changed")
	private Character isContractChanged;
	
	@Column(name="is_payee_updated")
	private Character isPayeeUpdated;
	
	@Column(name="tariff_id")
	private Integer tariffId;
	
	@Column(name="contract_id")
	private Integer contractId;
	
	@Column(name="insurance_number")
	private String insuranceNumber;
	
	@Column(name="insured_amount")
	private BigInteger insuredAmount;
	
	@Column(name="valid_from")
	private Date validityFrom;
	
	@Column(name="valid_to")
	private Date validityTo;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "visit_type_id", insertable = false, nullable = false, updatable = false)
	private VisitTypeMaster visitTypeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_entitlement_id", insertable = false, nullable = false, updatable = false)
	private PaymentEntitlementMaster paymentEntitlementMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "priority_id", insertable = false, nullable = false, updatable = false)
	private PriorityMaster priorityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admission_id", insertable = false, nullable = false, updatable = false)
	private Admission admission;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "encounter_id", insertable = false, nullable = false, updatable = false)
	private EncounterMaster encounterMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payee_id", insertable = false, nullable = false, updatable = false)
	private CompanyMaster companyMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contract_id", insertable = false, nullable = false, updatable = false)
	private MCompanyContractMaster mCompanyContractMaster;
	
	@Column(name="insurance_id")
	private Integer insuranceId;
	
	@Column(name="corporate_emp_note")
	private String corporateEmpNote;

	public String getInsuranceNumber() {
		return insuranceNumber;
	}

	public void setInsuranceNumber(String insuranceNumber) {
		this.insuranceNumber = insuranceNumber;
	}

	public BigInteger getInsuredAmount() {
		return insuredAmount;
	}

	public void setInsuredAmount(BigInteger insuredAmount) {
		this.insuredAmount = insuredAmount;
	}

	public Date getValidityFrom() {
		return validityFrom;
	}

	public void setValidityFrom(Date validityFrom) {
		this.validityFrom = validityFrom;
	}

	public Date getValidityTo() {
		return validityTo;
	}

	public void setValidityTo(Date validityTo) {
		this.validityTo = validityTo;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
	
	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getPatientSponsorDetailsId() {
		return patientSponsorDetailsId;
	}

	public void setPatientSponsorDetailsId(Integer patientSponsorDetailsId) {
		this.patientSponsorDetailsId = patientSponsorDetailsId;
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

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
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

	public Integer getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(Integer insuranceId) {
		this.insuranceId = insuranceId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getCorporateEmpNote() {
		return corporateEmpNote;
	}

	public void setCorporateEmpNote(String corporateEmpNote) {
		this.corporateEmpNote = corporateEmpNote;
	}

	public Character getIsContractChanged() {
		return isContractChanged;
	}

	public void setIsContractChanged(Character isContractChanged) {
		this.isContractChanged = (isContractChanged == null) ? 'N' : isContractChanged;
	}

	public Character getIsPayeeUpdated() {
		return isPayeeUpdated;
	}

	public void setIsPayeeUpdated(Character isPayeeUpdated) {
		this.isPayeeUpdated = (isPayeeUpdated == null) ? 'N' : isPayeeUpdated;
	}
	
	
}

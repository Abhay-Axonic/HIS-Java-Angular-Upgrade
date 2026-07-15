package com.param.service.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@NamedQueries({
	@NamedQuery(name="GET_CORPORATE_STATEMENT_MASTER_BY_ID",
			query="SELECT csm.corporateStatementId AS corporateStatementId,"
				    	+ " csm.visitTypeId AS visitTypeId,"
				    	+ " csm.invoiceNumber AS invoiceNumber,"
				    	+ " csm.billId AS billId,"
				    	+ "  to_char(csm.referenceDate,'DD-MM-YYYY') AS referenceDate,"
				    	+ " csm.firstName AS firstName,"
				    	+ " csm.fatherName AS fatherName,"
				    	+ " csm.investigation AS investigation,"
				    	+ "  to_char(csm.doneOnDate,'DD-MM-YYYY') AS doneOnDate,"
				    	+ " csm.consultant AS consultant,"
				    	+ " csm.orgName AS orgName,"
				    	+ " csm.disease AS disease,"
				    	+ " csm.hospitalTreatment AS hospitalTreatment,"
				    	+ "  to_char(csm.visitfromDate,'DD-MM-YYYY') AS visitfromDate,"
				    	+ "  to_char(csm.visittoDate,'DD-MM-YYYY') AS visittoDate,"
				    	+ "  to_char(csm.treatfromDate,'DD-MM-YYYY') AS treatfromDate,"
				    	+ "  to_char(csm.treattoDate,'DD-MM-YYYY') AS treattoDate,"
				    	+ "  to_char(csm.consDate,'DD-MM-YYYY') AS consDate,"
				    	+ " csm.employeeName AS employeeName,"
				    	+ " csm.hospitalName AS hospitalName,"
				    	+ " csm.relationId AS relationId,"
				    	+ " csm.relation AS relation,"
				    	+ " csm.consAmount AS consAmount,"
				    	+ " csm.labAmount AS labAmount,"
				    	+ " csm.pharmacyAmount AS pharmacyAmount,"
				    	+ " csm.address1 AS address1,"
				    	+ " csm.address2 AS address2,"
				    	+ " csm.medicalCardNo AS medicalCardNo,"
				    	+ " csm.authorizationLetterNo AS authorizationLetterNo,"
				    	+ " csm.letterType AS letterType,"
						+ " csm.organizationId AS organizationId, "
						+ "csm.unitId AS unitId, "
						+ "csm.status AS status"
						+ " FROM CorporateStatmentMaster csm"
						+ " WHERE csm.billId= :billId"
						+ " AND csm.organizationId= :organizationId"
						+ " AND csm.unitId= :unitId"
						),
	 @NamedQuery(name="GET_CHECK",
	query="SELECT csm.corporateStatementId AS corporateStatementId"
				+ " FROM CorporateStatmentMaster csm"
				+ " WHERE csm.billId= :billId"
				+ " AND csm.invoiceNumber= :invoiceNumber"
				)
})
@Entity
@Table(name = "m_corporate_statement_master" , schema = "billing")
@SequenceGenerator(name = "m_corporate_statement_master_seq" , sequenceName = "billing.m_corporate_statement_master_seq" , allocationSize = 1)
public class CorporateStatmentMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "m_corporate_statement_master_seq")
	@Column(name = "corporate_statement_id")
	private int corporateStatementId;
	
	@Column(name = "visit_type_id")
	private int visitTypeId;

	@Column(name = "invoice_number")
	private String invoiceNumber;
	
	@Column(name = "bill_id")
	private int billId;
	
	@Column(name = "reference_date")
	private Date referenceDate;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "father_name")
	private String fatherName;
	
	@Column(name = "investigation")
	private String investigation;
	
	@Column(name = "done_on_date")
	private Date doneOnDate;
	
	@Column(name = "consultant")
	private String consultant;
	
	@Column(name = "org_name")
	private String orgName;

	@Column(name = "disease")
	private String disease;
	
	@Column(name = "hospital_treatment")
	private String hospitalTreatment;
	
	@Column(name = "hospital_name")
	private String hospitalName;
	
	@Column(name = "visitfromDate")
	private Date visitfromDate;
	
	@Column(name = "visitto_date")
	private Date visittoDate;
	
	@Column(name = "treatfrom_date")
	private Date treatfromDate;
	
	@Column(name = "treatto_date")
	private Date treattoDate;
	
	@Column(name = "cons_date")
	private Date consDate;
	
	@Column(name = "employeeName")
	private String employeeName;
	
	@Column(name = "relation_id")
	private int relationId;
	
	@Column(name = "cons_amount")
	private int consAmount;
	
	@Column(name = "lab_amount")
	private int labAmount;
	
	@Column(name = "pharmacy_amount")
	private int pharmacyAmount;
	
	@Column(name = "address1")
	private String address1;
	
	@Column(name = "address2")
	private String address2;
	
	@Column(name = "letter_type")
	private int letterType;
	
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	public String getMedicalCardNo() {
		return medicalCardNo;
	}

	public void setMedicalCardNo(String medicalCardNo) {
		this.medicalCardNo = medicalCardNo;
	}

	public String getAuthorizationLetterNo() {
		return authorizationLetterNo;
	}

	public void setAuthorizationLetterNo(String authorizationLetterNo) {
		this.authorizationLetterNo = authorizationLetterNo;
	}

	@Column(name = "medical_card_no")
	private String medicalCardNo;
	
	@Column(name = "authorization_letter_no")
	private String authorizationLetterNo;
	
	@Column(name = "relation")
	private String relation;

	public int getCorporateStatementId() {
		return corporateStatementId;
	}

	public void setCorporateStatementId(int corporateStatementId) {
		this.corporateStatementId = corporateStatementId;
	}

	public int getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(int visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public int getBillId() {
		return billId;
	}

	public void setBillId(int billId) {
		this.billId = billId;
	}

	public Date getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(Date referenceDate) {
		this.referenceDate = referenceDate;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getInvestigation() {
		return investigation;
	}

	public void setInvestigation(String investigation) {
		this.investigation = investigation;
	}

	public Date getDoneOnDate() {
		return doneOnDate;
	}

	public void setDoneOnDate(Date doneOnDate) {
		this.doneOnDate = doneOnDate;
	}

	public String getConsultant() {
		return consultant;
	}

	public void setConsultant(String consultant) {
		this.consultant = consultant;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getDisease() {
		return disease;
	}

	public void setDisease(String disease) {
		this.disease = disease;
	}

	public String getHospitalTreatment() {
		return hospitalTreatment;
	}

	public void setHospitalTreatment(String hospitalTreatment) {
		this.hospitalTreatment = hospitalTreatment;
	}

	public Date getVisitfromDate() {
		return visitfromDate;
	}

	public void setVisitfromDate(Date visitfromDate) {
		this.visitfromDate = visitfromDate;
	}

	public Date getVisittoDate() {
		return visittoDate;
	}

	public void setVisittoDate(Date visittoDate) {
		this.visittoDate = visittoDate;
	}

	public Date getTreatfromDate() {
		return treatfromDate;
	}

	public void setTreatfromDate(Date treatfromDate) {
		this.treatfromDate = treatfromDate;
	}

	public Date getTreattoDate() {
		return treattoDate;
	}

	public void setTreattoDate(Date treattoDate) {
		this.treattoDate = treattoDate;
	}

	public Date getConsDate() {
		return consDate;
	}

	public void setConsDate(Date consDate) {
		this.consDate = consDate;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public int getRelationId() {
		return relationId;
	}

	public void setRelationId(int relationId) {
		this.relationId = relationId;
	}

	public int getConsAmount() {
		return consAmount;
	}

	public void setConsAmount(int consAmount) {
		this.consAmount = consAmount;
	}

	public int getLabAmount() {
		return labAmount;
	}

	public void setLabAmount(int labAmount) {
		this.labAmount = labAmount;
	}

	public int getPharmacyAmount() {
		return pharmacyAmount;
	}

	public void setPharmacyAmount(int pharmacyAmount) {
		this.pharmacyAmount = pharmacyAmount;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public int getLetterType() {
		return letterType;
	}

	public void setLetterType(int letterType) {
		this.letterType = letterType;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}
	
	
	

}

package com.param.global.model;

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

import com.param.adt.master.global.model.OccupationMaster;
@NamedQueries({
	@NamedQuery(name="GET_CORPORATE_EMPLOYEE_DEPENDANTS_DETAILS",
			query="select prefxMstr.prefixDesc as prefixDesc ,dtls.prefixId as prefixId, genMstr.desc as desc,dtls.genderId as genderId,"
					+ "dtls.identificationTypeId as identificationTypeId,idenMstr.identificationName as identificationName,dtls.relationId as relationId,relMstr.desc as ralationDesc,"
					+ "dtls.occupationId as occupationId ,occMstr.desc as occupationDesc ,dtls.dependentDetailsId as dependentDetailsId,dtls.corporateEmployeeId as corporateEmployeeId,"
					+ " dtls.firstName as firstName,dtls.middleName as middleName,dtls.lastName as lastName,dtls.mobileNo as mobileNo,dtls.identificationNumber as identificationNumber,"
					+ " to_char(dtls.dobKin,'DD/MM/YYYY') as dobKin,dtls.isNok as isNok,dtls.status as status "
					+ " from CorporateEmpDependentDetails dtls inner join dtls.prefixMaster prefxMstr "
					+ " inner join dtls.genderMaster genMstr inner join dtls.identificationMaster idenMstr "
					+ " inner join dtls.relationMaster relMstr inner join dtls.occupationMaster occMstr "
					+ " where dtls.organizationId=:orgId "
					+ " and dtls.unitId=:unitId "
					+ " and dtls.corporateEmployeeId=:corporateEmployeeId "
					+ " and dtls.status='A'"),
			@NamedQuery(name="VERIFY_CORPORATE_EMPLOYEE_DEPENDANTE_DETAILS",
			query="select prefxMstr.prefixDesc as prefixDesc ,dtls.prefixId as prefixId, genMstr.desc as desc,dtls.genderId as genderId,"
					+ "dtls.identificationTypeId as identificationTypeId,idenMstr.identificationName as identificationName,dtls.relationId as relationId,relMstr.desc as ralationDesc,"
					+ "dtls.occupationId as occupationId ,occMstr.desc as occupationDesc ,dtls.dependentDetailsId as dependentDetailsId,dtls.corporateEmployeeId as corporateEmployeeId,"
					+ " concat(coalesce(empMstr.firstName,''), ' ' ,coalesce(empMstr.middleName,''),' ',coalesce(empMstr.lastName,'')) as empName, empMstr.employeeNumber as employeeNumber , coalesce( dtls.firstName,'') as firstName,coalesce( dtls.middleName,'') as middleName,coalesce( dtls.lastName,'') as lastName,dtls.mobileNo as mobileNo,dtls.identificationNumber as identificationNumber,"
					+ " to_char(dtls.dobKin,'DD/MM/YYYY') as dobKin,dtls.isNok as isNok,dtls.status as status "
					+ " from CorporateEmployeeMaster empMstr "
					+ " inner join empMstr.employeeDetailsList dtls "
					+ " inner join dtls.corporateEmployeeMaster  "
					+ " inner join dtls.prefixMaster prefxMstr "
					+ " inner join dtls.genderMaster genMstr inner join dtls.identificationMaster idenMstr "
					+ " inner join dtls.relationMaster relMstr inner join dtls.occupationMaster occMstr "
					+ " where dtls.organizationId=:orgId "
					+ " and dtls.unitId=:unitId "
					+ " and empMstr.employeeNumber=:empNo "
					+ " and dtls.status='A'"
					+ " and empMstr.corporateId=:companyId ")
})
@Entity
@Table(name = "t_corporate_emp_dependent_details", schema = "public")
@SequenceGenerator(name = "t_corporate_emp_dependent_details_seq", sequenceName = "public.t_corporate_emp_dependent_details_seq", allocationSize = 1)
public class CorporateEmpDependentDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_corporate_emp_dependent_details_seq")
	@Column(name = "dependent_details_id")
	private Integer dependentDetailsId;
	
	@Column(name = "corporate_employee_id")
	private Integer corporateEmployeeId;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;		

	@Column(name = "prefix_id")
	private Integer prefixId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "relation_id")
	private Integer relationId;

	@Column(name = "mobile_no")
	private String mobileNo;

	@Column(name = "gender_id")
	private Integer genderId;

	@Column(name = "identification_number")
	private String identificationNumber;

	@Column(name = "identification_type_id")
	private Integer identificationTypeId;
	
	@Column(name = "occupation_id")
	private Integer occupationId;

	@Column(name = "dob_kin")
	private Date dobKin;

	@Column(name = "is_nok")
	private Character isNok;

	@Column(name = "status")
	private Character status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prefix_id", insertable = false, updatable = false)
	private PrefixMaster prefixMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gender_id", insertable = false, updatable = false)
	private GenderMaster genderMaster;	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "identification_type_id", insertable = false, updatable = false)
	private IdentificationMaster identificationMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "relation_id", insertable = false, updatable = false)
	private RelationMaster relationMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "corporate_employee_id", insertable = false, updatable = false)
	private CorporateEmployeeMaster corporateEmployeeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "occupation_id", insertable = false, updatable = false)
	private OccupationMaster occupationMaster;

	public Integer getDependentDetailsId() {
		return dependentDetailsId;
	}


	public void setDependentDetailsId(Integer dependentDetailsId) {
		this.dependentDetailsId = dependentDetailsId;
	}


	public Integer getCorporateEmployeeId() {
		return corporateEmployeeId;
	}


	public void setCorporateEmployeeId(Integer corporateEmployeeId) {
		this.corporateEmployeeId = corporateEmployeeId;
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


	public Integer getPrefixId() {
		return prefixId;
	}


	public void setPrefixId(Integer prefixId) {
		this.prefixId = prefixId;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getMiddleName() {
		return middleName;
	}


	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public Integer getRelationId() {
		return relationId;
	}


	public void setRelationId(Integer relationId) {
		this.relationId = relationId;
	}


	public String getMobileNo() {
		return mobileNo;
	}


	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}


	public Integer getGenderId() {
		return genderId;
	}


	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}


	public String getIdentificationNumber() {
		return identificationNumber;
	}


	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}


	public Integer getIdentificationTypeId() {
		return identificationTypeId;
	}


	public void setIdentificationTypeId(Integer identificationTypeId) {
		this.identificationTypeId = identificationTypeId;
	}


	public Integer getOccupationId() {
		return occupationId;
	}


	public void setOccupationId(Integer occupationId) {
		this.occupationId = occupationId;
	}


	public Date getDobKin() {
		return dobKin;
	}


	public void setDobKin(Date dobKin) {
		this.dobKin = dobKin;
	}


	public Character getIsNok() {
		return isNok;
	}


	public void setIsNok(Character isNok) {
		this.isNok = isNok;
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
	
	
}

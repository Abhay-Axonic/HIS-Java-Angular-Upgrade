package com.param.bloodBank.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.param.global.model.GenderMaster;
import com.param.global.model.IdentificationMaster;
import com.param.global.model.PatientCategoryMaster;
import com.param.global.model.PrefixMaster;

@NamedQueries({
	@NamedQuery(name="GET_DONOR_DETAILS_BY_ID",
			query = "SELECT donor.donorId as donorId, "
					+ "donor.donorNumber as donorNumber, "
					+ "donor.prefixId as prefixId, "
					+ "concat(coalesce(donor.firstName,'') ,' ', coalesce(donor.middleName,'') ,' ', coalesce(donor.lastName,'')) as donorName, "
					+ "donor.genderId as genderId, "
					+ "donor.mobileNumber as mobileNumber, "
					+ "to_char(donor.dob, 'DD-MM-YYYY') as dob,"
					+ " donor.patientCategoryId as patientCategoryId,"
					+ " donor.registrationTypeId as registrationTypeId,"
					+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, donor.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, donor.dob)),'M ') as age, "
					+ " genderMaster.code as genderCode "
					+ " FROM DonorRegistrationMaster donor "
					+ " INNER JOIN donor.genderMaster genderMaster"
					+ " WHERE donor.donorId=:donorId"),
	
			@NamedQuery(name="GET_DONOR_DETAILS",
			query = "SELECT donor.donorId as donorId, "
					+ "donor.donorNumber as donorNumber, "
					+ "donor.prefixId as prefixId, "
					+ "concat(coalesce(donor.firstName,'') ,' ', coalesce(donor.middleName,'') ,' ', coalesce(donor.lastName,'')) as donorName, "
					+ "donor.genderId as genderId, "
					+ "donor.mobileNumber as mobileNumber, "
					+ "to_char(donor.dob, 'dd/MM/yyyy') as dob,"
					+ " genderMaster.code as genderCode "
					+ " FROM DonorRegistrationMaster donor "
					+ " INNER JOIN donor.genderMaster genderMaster"
					+ " WHERE donor.unitId=:unitId "
					+ " AND donor.organizationId=:orgId"),
	
			@NamedQuery(name="CHECK_UNIQUE_DONOR",
				query="SELECT donor.donorId as donorId "
					+ "FROM DonorRegistrationMaster donor "
					+ "WHERE donor.dob=:dob "
					+ "AND donor.genderId=:genderId "
					+ "AND donor.mobileNumber=:mobileNumber "
					+ "AND donor.organizationId=:orgId "
					+ "AND donor.unitId=:unitId "),
			
		/*	@NamedQuery(name="DONOR_AUTO_FILL_SEARCH" , query= "SELECT patient.donorId as donorId, "
					+ "concat(coalesce(patient.firstName) ,' ', coalesce(patient.middleName) ,' ', coalesce(patient.lastName)) as patientName, "
					+ "patient.uhidNumber as uhidNumber, "
					+ "patient.mobileNumber as mobileNumber "
					+ "FROM AppointmentMaster appointment "
					+ "INNER JOIN appointment.patientRegistration patient "
					+ "WHERE (LOWER(patient.firstName) LIKE :patientName "
					+ "OR LOWER(patient.lastName) LIKE :patientName "
					+ "OR LOWER(patient.uhidNumber) LIKE :patientName "
					+ "OR LOWER(patient.mobileNumber) LIKE :patientName ) "
					+ "AND appointment.status='A' "
					+ "AND patient.status='A' "
					+ "AND appointment.unitId=:unitId "
					+ "AND appointment.organizationId=:organizationId "),*/
			
			@NamedQuery(name="GET_DONOR_DATA",query=
					 "SELECT donor.donorId as donorId, "
					 + "donor.donorNumber as donorNumber, "
					 + "donor.prefixId as prefixId,donor.bloodGroupId as bloodGroupId,donor.donoTypeId as donoTypeId , bloodDonorTypeMaster.bloodDonorTypeDesc as donorType,"
					 + "prefix.prefixCode as prefixCode, "
					 + "coalesce(donor.firstName,'') as firstName, "
					 + "coalesce(donor.middleName,'') as middleName, "
					 + "coalesce(donor.lastName,'') as lastName, "
					 + "donor.genderId as genderId, "
					 + "donor.exportedToScreening as exportedToScreening, "
					 + "donor.imagePath as imagePath, "
					 + "gender.code as genderCode, "
					 + "gender.desc as genderName, "
					 + "to_char(donor.dob,'MM/DD/YYYY') as dob, "
					 + "to_char(donor.dob,'DD/MM/YYYY') as birthDate, "
					 + "trim(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, donor.dob)),'999')) as ageString, "
					 + "donor.mobileNumber as mobileNumber, "
					 + "donor.uhidNumber as uhidNumber, "
					 + "donor.countryCallingCode as countryCallingCode, "
					 + "donor.identificationTypeId as identificationTypeId, "
					 + "coalesce(identification.identificationName,'') as identificationName, "
					 + "identification.isExpiryRequired as isExpiryRequired, "
					 + "donor.identificationNumber as identificationNumber, " 
					 + "donor.email as email, "
					 + "donor.isAutologous as isAutologous, "
					 + "donor.patientId as patientId, "
					 + "donor.status as status, "
					 + "to_char(donor.identificationExpiryDate,'MM/DD/YYYY') as identificationExpiryDate, "
					 + "donor.occupationId as occupationId, "
					 + "donorDetails.donorDetailsId as donorDetailsId,"
					 + "donorDetails.nationalityId as nationalityId, "
					 + "donorDetails.raceId as raceId, "
					 + "donorDetails.maritalStatusId as maritalStatusId, "
					 + "donorDetails.address as address, "
					 + "donorDetails.countryId as countryId, " 
					 + "donorDetails.stateId as stateId, "
					 + "donorDetails.districtId as districtId, "
					 + "donorDetails.cityId as cityId, "
					 + "donorDetails.areaId as areaId, "
					 + "donorDetails.zipCode as zipCode, "
					 + "donorDetails.phoneCode as phoneCode, "
					 + "donorDetails.phoneNumber as phoneNumber, "
					 + "donorDetails.permanentAddress as permanentAddress, "
					 + "donorDetails.permanentCountryId as permanentCountryId, "
					 + "donorDetails.permanentStateId as permanentStateId, "
					 + "donorDetails.permanentDistrictId as permanentDistrictId, "
					 + "donorDetails.permanentCityId as permanentCityId, "
					 + "donorDetails.permanentAreaId as permanentAreaId, "
					 + "donorDetails.permanentZipCode as permanentZipCode, "
					 + "donorDetails.permanentPhoneCode as permanentPhoneCode, "
					 + "bloodGroupMaster.bloodGroupDesc as bloodGroup, "
					 + "donorDetails.permanentPhoneNumber as permanentPhoneNumber "
					 + "FROM DonorRegistrationMaster donor "
					 + "INNER JOIN donor.donorDetailsList donorDetails "
					+ " INNER JOIN donor.bloodDonorTypeMaster bloodDonorTypeMaster "
					 + "LEFT JOIN donor.prefixMaster prefix "
					 + "INNER JOIN donor.genderMaster gender "
					 + "INNER JOIN donor.bloodGroupMaster bloodGroupMaster "
					 + "LEFT JOIN donor.identificationMaster identification "
					 + "WHERE donor.organizationId=:orgId "
					 + "AND donor.unitId=:unitId "
					 + "AND donor.status='A' "
					 + "AND donorDetails.status='A' "
					 ),
			
			
			@NamedQuery(name="GET_DONOR_DATA_FOR_SELF_REGISTRATION",query=
			 "SELECT donor.donorId as donorId, "
			 + "donor.donorNumber as donorNumber, "
			 + "donor.prefixId as prefixId, "
			 + "prefix.prefixCode as prefixCode, "
			 + "coalesce(donor.firstName,'') as firstName, "
			 + "coalesce(donor.middleName,'') as middleName, "
			 + "coalesce(donor.lastName,'') as lastName, "
			 + "donor.genderId as genderId, "
			 + "donor.imagePath as imagePath, "
			 + "donor.patientCategoryId as patientCategoryId,"
			 + "gender.code as genderCode, "
			 + "to_char(donor.dob,'MM/DD/YYYY') as dob, "
			 + "to_char(donor.dob,'DD/MM/YYYY') as birthDate, "
			 + "trim(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, donor.dob)),'999')) as ageString, "
			 + "donor.mobileNumber as mobileNumber, "
			 + "donor.countryCallingCode as countryCallingCode, "
			 + "donor.registrationTypeId as typeId, "
			 + "donor.email as email "
			 + "FROM DonorRegistrationMaster donor "
			 + "INNER JOIN donor.prefixMaster prefix "
			 + "INNER JOIN donor.genderMaster gender "
			 + "WHERE donor.organizationId=:orgId "
			 + "AND donor.unitId=:unitId "
			 + "AND donor.status='A' "
			 + "AND donor.isPrimary='Y' "
			 + "AND donor.registrationTypeId IN (1,3,4,6) "
			 ),
			
   @NamedQuery(name = "UPDATE_DONOR_IMAGE_PATH",
    query = "UPDATE DonorRegistrationMaster SET imagePath=:imagePath"
        + " WHERE donorId=:donorId " + " AND organizationId =:orgId "
        + " AND unitId =:unitId "),
  
	@NamedQuery(name="CHECK_UNIQUE_PATIENT_ID",
	query="SELECT donor.donorId as donorId "
		+ "FROM DonorRegistrationMaster donor "
		+ "WHERE donor.patientId=:patientId "
		+ "AND donor.organizationId=:orgId "
		+ "AND donor.unitId=:unitId "),
  
	@NamedQuery(name="SEARCH_REGISTRERED_DONOR_BY_MULIPLE_CRITERIA",
	query="SELECT donorRegi.donorId as donorId, "
			    + " donorRegi.bloodGroupId as bloodGroupId, "
			    + " donorRegi.status as status,"
			    + " donorRegi.isAutologous as isAutologous,"
			    + " donorRegi.createdBy as createdBy,"
			    + " to_char(donorRegi.createdDate,'dd/mm/yyyy') as createdDate,"
			    + " donorRegi.updatedBy as updatedBy,"
			    + " to_char(donorRegi.updatedDate,'dd/mm/yyyy') as updatedDate,"
			    + " donorRegi.organizationId as organizationId,"
			    + " concat(coalesce(donorRegi.firstName,'') ,' ', coalesce(donorRegi.middleName,'') ,' ', coalesce(donorRegi.lastName,'')) as donorName, "
			    + " donorRegi.donorNumber as donorNumber, "
			    + " genMstr.desc as gender, "
			    + " bloodGroupMaster.bloodGroupDesc as bloodGroup, "
			    + " donorRegi.unitId as unitId "
				+ " FROM DonorRegistrationMaster donorRegi "
				+ " INNER JOIN donorRegi.genderMaster  genMstr "
				+ " INNER JOIN donorRegi.bloodGroupMaster bloodGroupMaster "
				+ " WHERE donorRegi.organizationId= :organizationId "
				+ " AND donorRegi.unitId= :unitId "
				+ " AND donorRegi.isAutologous= true "),
	
	@NamedQuery(name="SEARCH_REGISTRERED_DONOR_BY_EXPORTED_TO_SCREENING_STATUS",query=
	 "SELECT donor.donorId as donorId, "
	 + " donor.donorNumber as donorNumber, "
	 + " donor.bloodGroupId as bloodGroupId,"
	 + " concat(coalesce(donor.firstName,'') ,' ', coalesce(donor.middleName,'') ,' ', coalesce(donor.lastName,'')) as donorName, "
	 + " donor.genderId as genderId, "
	 + " gender.desc as gender, "
	 + " to_char(donor.dob,'MM/DD/YYYY') as dob, "
	 + " donor.mobileNumber as mobileNumber, "
	 + " donor.identificationTypeId as identificationTypeId, "
	 + " coalesce(identification.identificationName,'') as identificationName, "
	 + " donor.identificationNumber as identificationNumber, " 
	 + " donor.isAutologous as isAutologous, "
	 + " donor.patientId as patientId, "
	 + " donor.status as status, "
	 + " bloodGroupMaster.bloodGroupDesc as bloodGroup "
	 + " FROM DonorRegistrationMaster donor "
	 + " INNER JOIN donor.donorDetailsList donorDetails "
	 + " INNER JOIN donor.bloodDonorTypeMaster bloodDonorTypeMaster "
	 + " INNER JOIN donor.genderMaster gender "
	 + " INNER JOIN donor.bloodGroupMaster bloodGroupMaster "
	 + " LEFT JOIN donor.identificationMaster identification "
	 + " WHERE donor.organizationId=:organizationId "
	 + " AND donor.unitId=:unitId "
	 + " AND donor.status='A' "
	 + " AND donorDetails.status='A' "
	 + " AND donor.exportedToScreening = false"
	 + " AND donor.isAutologous= false " 
	 )
})


@Entity
@Table(name = "t_donor_registration", schema = "blood_bank")
@SequenceGenerator(name="donor_registration_seq", sequenceName="blood_bank.donor_registration_seq", allocationSize=1)
public class DonorRegistrationMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "donor_registration_seq")
	@Column(name="donor_id")
	private Integer donorId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	 
	@Column(name="donor_number")
	private String donorNumber;
	
	@Column(name="uhid_number")
	private String uhidNumber;
	
	@Column(name="prefix_id")
	private Integer prefixId;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="middle_name")
	private String middleName;
	
	@Column(name="last_name")
	private String lastName;
	
	@Column(name="gender_id")
	private Integer genderId;
	
	@Column(name="dob")
	private Date dob;
	
	@Column(name="mobile_number")
	private String mobileNumber;
	
	@Column(name="country_calling_code")
	private String countryCallingCode;

	@Column(name="identification_type_id")
	private Integer identificationTypeId;
	
	@Column(name="identification_number")
	private String identificationNumber;
	
	@Column(name="email")
	private String email;
	
	@Column(name="patient_category_id")
	private Integer patientCategoryId;
	
	@Column(name="identification_expiry_date")
	private Date identificationExpiryDate;

	@Column(name="occupation_id")
	private Integer occupationId;
	
	@Column(name = "registration_type_id")
	private Integer registrationTypeId;

	@Column(name="emp_doc_id")
	private Integer empDocId;
	
	@Column(name="emp_dep_type_id")
	private Integer empDepTypeId;
	
	@Column(name="emp_doc_dep_id")
	private Integer empDocDepId;
	
	@Column(name="status")
	private char status;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
    @Column(name = "image_path")
    private String imagePath;
    
    @Column(name="is_primary")
    private Character isPrimary;
   
    @Column(name = "donor_type_id")
    private Integer donoTypeId;
    
    @Column(name = "blood_group_id")
    private Integer bloodGroupId;
    
    @Column(name = "patient_id")
    private Integer patientId;
    
    @Column(name = "is_autologous")
    private Boolean isAutologous;
    
    @Column(name = "exported_to_screening")
    private Boolean exportedToScreening;
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prefix_id", insertable = false, updatable = false)
	private PrefixMaster prefixMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gender_id", insertable = false, updatable = false)
	private GenderMaster genderMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_category_id", insertable = false, updatable = false)
	private PatientCategoryMaster patientCategoryMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "identification_type_id", insertable = false, updatable = false)
	private IdentificationMaster identificationMaster;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "donorRegistration", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DonorDetail> donorDetailsList;
	
	@OneToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="donor_type_id", insertable=false, updatable=false)
	private BloodDonorTypeMaster bloodDonorTypeMaster;
	
	@OneToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="blood_group_id", insertable=false, updatable=false)
	private BloodGroupMaster bloodGroupMaster;
	

	public Character getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(Character isPrimary) {
		this.isPrimary = (isPrimary == '\u0000') ? 'Y' : isPrimary;
	}

	public Integer getEmpDocId() {
		return empDocId;
	}

	public void setEmpDocId(Integer empDocId) {
		this.empDocId = empDocId;
	}

	public Integer getEmpDepTypeId() {
		return empDepTypeId;
	}

	public void setEmpDepTypeId(Integer empDepTypeId) {
		this.empDepTypeId = empDepTypeId;
	}

	public Integer getEmpDocDepId() {
		return empDocDepId;
	}

	public void setEmpDocDepId(Integer empDocDepId) {
		this.empDocDepId = empDocDepId;
	}

	public IdentificationMaster getIdentificationMaster() {
		return identificationMaster;
	}

	public void setIdentificationMaster(IdentificationMaster identificationMaster) {
		this.identificationMaster = identificationMaster;
	}

	public List<DonorDetail> getPatientDetailslist() {
		return donorDetailsList;
	}

	public void setPatientDetailslist(List<DonorDetail> donorDetailsList) {
		this.donorDetailsList = donorDetailsList;
	}

	public Integer getRegistrationTypeId() {
		return registrationTypeId;
	}

	public void setRegistrationTypeId(Integer registrationTypeId) {
		this.registrationTypeId = registrationTypeId;
	}


	public Date getIdentificationExpiryDate() {
		return identificationExpiryDate;
	}

	public void setIdentificationExpiryDate(Date identificationExpiryDate) {
		this.identificationExpiryDate = identificationExpiryDate;
	}

	public Integer getOccupationId() {
		return occupationId;
	}

	public void setOccupationId(Integer occupationId) {
		this.occupationId = occupationId;
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

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}


	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getCountryCallingCode() {
		return countryCallingCode;
	}

	public void setCountryCallingCode(String countryCallingCode) {
		this.countryCallingCode = countryCallingCode;
	}
	public Integer getIdentificationTypeId() {
		return identificationTypeId;
	}

	public void setIdentificationTypeId(Integer identificationTypeId) {
		this.identificationTypeId = identificationTypeId;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'A' : status;
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

	public PrefixMaster getPrefixMaster() {
		return prefixMaster;
	}

	public void setPrefixMaster(PrefixMaster prefixMaster) {
		this.prefixMaster = prefixMaster;
	}

	public GenderMaster getGenderMaster() {
		return genderMaster;
	}

	public void setGenderMaster(GenderMaster genderMaster) {
		this.genderMaster = genderMaster;
	}

	public PatientCategoryMaster getPatientCategoryMaster() {
		return patientCategoryMaster;
	}

	public void setPatientCategoryMaster(PatientCategoryMaster patientCategoryMaster) {
		this.patientCategoryMaster = patientCategoryMaster;
	}


  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

public Integer getDonoTypeId() {
	return donoTypeId;
}

public void setDonoTypeId(Integer donoTypeId) {
	this.donoTypeId = donoTypeId;
}

public List<DonorDetail> getDonorDetailsList() {
	return donorDetailsList;
}

public void setDonorDetailsList(List<DonorDetail> donorDetailsList) {
	this.donorDetailsList = donorDetailsList;
}

public Integer getDonorId() {
	return donorId;
}

public void setDonorId(Integer donorId) {
	this.donorId = donorId;
}

public Integer getPatientId() {
	return patientId;
}

public void setPatientId(Integer patientId) {
	this.patientId = patientId;
}

public BloodDonorTypeMaster getBloodDonorTypeMaster() {
	return bloodDonorTypeMaster;
}

public void setBloodDonorTypeMaster(BloodDonorTypeMaster bloodDonorTypeMaster) {
	this.bloodDonorTypeMaster = bloodDonorTypeMaster;
}

public Boolean getIsAutologous() {
	return isAutologous;
}

public void setIsAutologous(Boolean isAutologous) {
	this.isAutologous = isAutologous;
}

public Integer getBloodGroupId() {
	return bloodGroupId;
}

public void setBloodGroupId(Integer bloodGroupId) {
	this.bloodGroupId = bloodGroupId;
}

public BloodGroupMaster getBloodGroupMaster() {
	return bloodGroupMaster;
}

public void setBloodGroupMaster(BloodGroupMaster bloodGroupMaster) {
	this.bloodGroupMaster = bloodGroupMaster;
}

public Boolean getExportedToScreening() {
	return exportedToScreening;
}

public void setExportedToScreening(Boolean exportedToScreening) {
	this.exportedToScreening = exportedToScreening;
}

public String getDonorNumber() {
	return donorNumber;
}

public void setDonorNumber(String donorNumber) {
	this.donorNumber = donorNumber;
}

public String getUhidNumber() {
	return uhidNumber;
}

public void setUhidNumber(String uhidNumber) {
	this.uhidNumber = uhidNumber;
}

}

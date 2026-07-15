package com.param.pharmacy.narcotics.issueWorklist.dto;

import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class NarcoticsResponse
{
	private String admissionUhid;
			
	private String encounterUhid;
	
	private String admissionPatDtls;
	
	private String encounterPatDtls;
	
	private Timestamp admissionDob;
	
	private Timestamp encounterDob;
	
	private Timestamp admissionDate;
	
	private Timestamp encounterDate;
	
	private String specialityName;
	private Integer prescriptionId;
	 private String patientName;
	    private String ageString;
	    private String genderCode;
	    private Integer genderId;
		private String imagePath;
	    private Integer medicationType;
	    private Integer addedByUser;
	    private String userName;
	    private String entitlementDesc;
	    private String contractName;      
	    private Integer admissionId;  
	public Timestamp getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(Timestamp admissionDate) {
		this.admissionDate = admissionDate;
	}

	public Timestamp getEncounterDate() {
		return encounterDate;
	}

	public void setEncounterDate(Timestamp encounterDate) {
		this.encounterDate = encounterDate;
	}

	private Integer doctorId;
	
	private Integer id;
 	
 	private String visitType;
 	
 	private Date visitDate;
 	
 	private Date prescriptionDate;
 	
    private String uhid;
    
    private String patientDetail;
    
    private String doctorDetail;
    
    private String wardName;
    
    private String bedNo;
    
    private String priority;
    
    private String status;
    
    private Integer statusId;
    
    private Integer issueId;
    
    private Date issueDate;
    
    private String issueNo;
    
    private String carrierName;
    
    
	public String getAdmissionUhid() {
		return admissionUhid;
	}

	public void setAdmissionUhid(String admissionUhid) {
		this.admissionUhid = admissionUhid;
	}

	public String getEncounterUhid() {
		return encounterUhid;
	}

	public void setEncounterUhid(String encounterUhid) {
		this.encounterUhid = encounterUhid;
	}

	public String getAdmissionPatDtls() {
		return admissionPatDtls;
	}

	public void setAdmissionPatDtls(String admissionPatDtls) {
		this.admissionPatDtls = admissionPatDtls;
	}

	public String getEncounterPatDtls() {
		return encounterPatDtls;
	}

	public void setEncounterPatDtls(String encounterPatDtls) {
		this.encounterPatDtls = encounterPatDtls;
	}

	public Timestamp getAdmissionDob() {
		return admissionDob;
	}

	public void setAdmissionDob(Timestamp admissionDob) {
		this.admissionDob = admissionDob;
	}

	public Timestamp getEncounterDob() {
		return encounterDob;
	}

	public void setEncounterDob(Timestamp encounterDob) {
		this.encounterDob = encounterDob;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public Date getPrescriptionDate() {
		return prescriptionDate;
	}

	public void setPrescriptionDate(Date prescriptionDate) {
		this.prescriptionDate = prescriptionDate;
	}

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
	}

	public String getPatientDetail() {
		return patientDetail;
	}

	public void setPatientDetail(String patientDetail) {
		this.patientDetail = patientDetail;
	}

	public String getDoctorDetail() {
		return doctorDetail;
	}

	public void setDoctorDetail(String doctorDetail) {
		this.doctorDetail = doctorDetail;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}
	
	

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public NarcoticsResponse(Integer id, String visitType, Date visitDate, Date prescriptionDate, String uhid,
			String patientDetail, String doctorDetail, String priority, String status, Integer statusId) {
		super();
		this.id = id;
		this.visitType = visitType;
		this.visitDate = visitDate;
		this.prescriptionDate = prescriptionDate;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.priority = priority;
		this.status = status;
		this.statusId = statusId;
	}

	public NarcoticsResponse(Long id, String visitType, Date visitDate, String uhid,
			String patientDetail, String doctorDetail, String status, Integer statusId,
			Integer issueId, Date issueDate, String issueNo) {
		super();
		this.id = id.intValue();
		this.visitType = visitType;
		this.visitDate = visitDate;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.status = status;
		this.statusId = statusId;
		this.issueId = issueId;
		this.issueDate = issueDate;
		this.issueNo = issueNo;
	}
	
	public NarcoticsResponse() {
		super();
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public Integer getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(Integer prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getAgeString() {
		return ageString;
	}

	public void setAgeString(String ageString) {
		this.ageString = ageString;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Integer getMedicationType() {
		return medicationType;
	}

	public void setMedicationType(Integer medicationType) {
		this.medicationType = medicationType;
	}

	public Integer getAddedByUser() {
		return addedByUser;
	}

	public void setAddedByUser(Integer addedByUser) {
		this.addedByUser = addedByUser;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEntitlementDesc() {
		return entitlementDesc;
	}

	public void setEntitlementDesc(String entitlementDesc) {
		this.entitlementDesc = entitlementDesc;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	

	
}
 package com.param.pharmacy.ip.dto.pharmacology;

import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PharmacologyValidationSearchResult
 {
	 	private Long id;
	 	
	 	private Integer admissionId;
	 	
	 	private Integer assignPickerId;
	 	
	 	private Integer issueId;
	 	
	 	private Long prescriptionId;
	 	
	 	private String visitType;
	 	
	 	private Date visitDate;
	 	
	 	private Date prescriptionDate;
	 	
	    private String uhid;
	    
	    private String patientDetail;
	    
	    private String doctorDetail;
	    
	    private String wardName;
	    
	    private String bedNo;
	    
	    private String priority;
	    
	    private Integer priorityId;
	    
	    private String status;
	    
	    private Integer statusId;
	    
	    private String paymentStatus;
	    
		private String dispensedBy;
		
		private Integer doctorId;
		
		private Timestamp patientDob;
		
		private Integer patientId;
		private Integer age;

		private String specialityName;
		
	    private String patientName;
	    private String ageString;
	    private String genderCode;
	    private Integer genderId;
		private String imagePath;
		private String encounterNumber;
		private String prescriptionNo;
		private String entitlementDesc;
	    private Integer medicationType;
	    private String userName;
	    private Integer addedByUser;
	    private String contractName;
	    private Integer isFinalDischarge;
	    private Integer visitTypeId;
	    public String getEntitlementDesc() {
			return entitlementDesc;
		}

		public void setEntitlementDesc(String entitlementDesc) {
			this.entitlementDesc = entitlementDesc;
		}

		public PharmacologyValidationSearchResult()
	    {
	    	super();
	    }

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
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

		public Long getPrescriptionId() {
			return prescriptionId;
		}

		public void setPrescriptionId(Long prescriptionId) {
			this.prescriptionId = prescriptionId;
		}

		public Integer getStatusId() {
			return statusId;
		}

		public void setStatusId(Integer statusId) {
			this.statusId = statusId;
		}
		
		public String getPaymentStatus() {
			return paymentStatus;
		}

		public void setPaymentStatus(String paymentStatus) {
			this.paymentStatus = paymentStatus;
		}

		public String getDispensedBy() {
			return dispensedBy;
		}

		public void setDispensedBy(String dispensedBy) {
			this.dispensedBy = dispensedBy;
		}

		/*public PharmacologyValidationSearchResult(Long id, String visitType, Date visitDate, Date prescriptionDate,
				String uhid, String patientDetail, String doctorDetail, String wardName, String bedNo, String priority,
				String status) {
			super();
			this.id = id;
			this.visitType = visitType;
			this.visitDate = visitDate;
			this.prescriptionDate = prescriptionDate;
			this.uhid = uhid;
			this.patientDetail = patientDetail;
			this.doctorDetail = doctorDetail;
			this.wardName = wardName;
			this.bedNo = bedNo;
			this.priority = priority;
			this.status = status;
		}*/

		public Integer getAssignPickerId() {
			return assignPickerId;
		}

		public void setAssignPickerId(Integer assignPickerId) {
			this.assignPickerId = assignPickerId;
		}

		public Integer getAdmissionId() {
			return admissionId;
		}

		public void setAdmissionId(Integer admissionId) {
			this.admissionId = admissionId;
		}

		public PharmacologyValidationSearchResult(Long id, String visitType, Date visitDate,
				Date prescriptionDate, String uhid, String patientDetail, String doctorDetail, String priority,
				String status) {
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
			
		}
		public PharmacologyValidationSearchResult(Long id, String visitType, Date visitDate,
				Date prescriptionDate, String uhid, String patientDetail, String doctorDetail, String priority,
				String status, String patientName , Integer age) {
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
			this.patientName = patientName;
			this.age = age;
		}

		public PharmacologyValidationSearchResult(Long id, String visitType, Date visitDate, Date prescriptionDate,
				String uhid, String patientDetail, String doctorDetail, String priority,
				String status, Integer statusId) {
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
		
		public PharmacologyValidationSearchResult(Long id, String visitType, Date visitDate, Date prescriptionDate,
				String uhid, String patientDetail, String doctorDetail, String priority,
				String status, Integer statusId, String patientName, Integer age) {
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
			this.patientName = patientName;
			this.age = age;
		}

		public Integer getDoctorId() {
			return doctorId;
		}

		public void setDoctorId(Integer doctorId) {
			this.doctorId = doctorId;
		}

		public Timestamp getPatientDob() {
			return patientDob;
		}

		public void setPatientDob(Timestamp patientDob) {
			this.patientDob = patientDob;
		}

		public Integer getPatientId() {
			return patientId;
		}

		public void setPatientId(Integer patientId) {
			this.patientId = patientId;
		}

		public Integer getIssueId() {
			return issueId;
		}

		public void setIssueId(Integer issueId) {
			this.issueId = issueId;
		}

		public String getEncounterNumber() {
			return encounterNumber;
		}

		public void setEncounterNumber(String encounterNumber) {
			this.encounterNumber = encounterNumber;
		}

		public String getPrescriptionNo() {
			return prescriptionNo;
		}

		public void setPrescriptionNo(String prescriptionNo) {
			this.prescriptionNo = prescriptionNo;
		}

		public String getSpecialityName() {
			return specialityName;
		}

		public void setSpecialityName(String specialityName) {
			this.specialityName = specialityName;
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

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		public Integer getMedicationType() {
			return medicationType;
		}

		public void setMedicationType(Integer medicationType) {
			this.medicationType = medicationType;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public Integer getAddedByUser() {
			return addedByUser;
		}

		public void setAddedByUser(Integer addedByUser) {
			this.addedByUser = addedByUser;
		}

		public Integer getPriorityId() {
			return priorityId;
		}

		public void setPriorityId(Integer priorityId) {
			this.priorityId = priorityId;
		}

		public String getContractName() {
			return contractName;
		}

		public void setContractName(String contractName) {
			this.contractName = contractName;
		}

		public Integer getIsFinalDischarge() {
			return isFinalDischarge;
		}

		public void setIsFinalDischarge(Integer isFinalDischarge) {
			this.isFinalDischarge = isFinalDischarge;
		}

		public Integer getVisitTypeId() {
			return visitTypeId;
		}

		public void setVisitTypeId(Integer visitTypeId) {
			this.visitTypeId = visitTypeId;
		}

		
		
 }
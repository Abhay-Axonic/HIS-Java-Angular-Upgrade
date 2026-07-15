package com.param.pharmacy.ip.dto.patientIssueRejection;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PatientIssueRejectionSearchResult
{
	private Integer id;
	
	private Integer issueId;
	
	private Integer doctorId;
	
	private String issueRejectNo;
	
	private Date issueRejectDate;
	
	private String indentNo;
	
	private String issueNo;
	
	private String uhid;
	
	private String patientDetail;
	
	private String doctorDetail;
	
	private String visitNo;
	
	private String visitType;
	
	private String store;
	private String toStore;
	private Integer storeId;
	private Integer medicationType;
	private Integer patientId;

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getToStore() {
		return toStore;
	}

	public void setToStore(String toStore) {
		this.toStore = toStore;
	}

	private String wardName;
	
	private Integer wardId;
	
	private String bedNo;
	
	private Integer statusId;
	   
	private String status;
	
	private Integer admissionId;
	
	private Integer encounterId;
	
	private String admittedPatientDetails;
	
	private String encounterPatientDetails;
	
	private String admittedPatientUhid;
	
	private String encounterPatientUhid;
	
	private Timestamp admissionPatDob;
	
	private Timestamp patientEncounterDob;
	private Integer genderId;
	private String imagePath;   
	private String patientName;
	private String ageStringDet;
//	    private String ageString;
	    private String genderCode; 

	public Timestamp getAdmissionPatDob() {
		return admissionPatDob;
	}

	public void setAdmissionPatDob(Timestamp admissionPatDob) {
		this.admissionPatDob = admissionPatDob;
	}

	public Timestamp getPatientEncounterDob() {
		return patientEncounterDob;
	}

	public void setPatientEncounterDob(Timestamp patientEncounterDob) {
		this.patientEncounterDob = patientEncounterDob;
	}

	private List<PatientIssueRejectionItem> items;

	private Integer ageString;
	
	
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

	public String getAdmittedPatientDetails() {
		return admittedPatientDetails;
	}

	public void setAdmittedPatientDetails(String admittedPatientDetails) {
		this.admittedPatientDetails = admittedPatientDetails;
	}

	public String getEncounterPatientDetails() {
		return encounterPatientDetails;
	}

	public void setEncounterPatientDetails(String encounterPatientDetails) {
		this.encounterPatientDetails = encounterPatientDetails;
	}

	public String getAdmittedPatientUhid() {
		return admittedPatientUhid;
	}

	public void setAdmittedPatientUhid(String admittedPatientUhid) {
		this.admittedPatientUhid = admittedPatientUhid;
	}

	public String getEncounterPatientUhid() {
		return encounterPatientUhid;
	}

	public void setEncounterPatientUhid(String encounterPatientUhid) {
		this.encounterPatientUhid = encounterPatientUhid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	public String getIssueRejectNo() {
		return issueRejectNo;
	}

	public void setIssueRejectNo(String issueRejectNo) {
		this.issueRejectNo = issueRejectNo;
	}

	public Date getIssueRejectDate() {
		return issueRejectDate;
	}

	public void setIssueRejectDate(Date issueRejectDate) {
		this.issueRejectDate = issueRejectDate;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
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

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
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
	
	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}
	
	public String getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(String indentNo) {
		this.indentNo = indentNo;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public List<PatientIssueRejectionItem> getItems() {
		return items;
	}

	public void setItems(List<PatientIssueRejectionItem> items) {
		this.items = items;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public PatientIssueRejectionSearchResult()
	{
		super();
	}
	
	public PatientIssueRejectionSearchResult(Integer id, Integer issueId, String issueRejectNo, Date issueRejectDate, String issueNo,
			String uhid, String patientDetail, String doctorDetail, String store) {
		super();
		this.id = id;
		this.issueId = issueId;
		this.issueRejectNo = issueRejectNo;
		this.issueRejectDate = issueRejectDate;
		this.issueNo = issueNo;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.store = store;
	}

	public PatientIssueRejectionSearchResult(Integer id, String issueRejectNo, Date issueRejectDate, String indentNo,
			String uhid, String patientDetail, String doctorDetail, String visitNo, String visitType, String store, String patientName, Integer ageString) {
		super();
		this.id = id;
		this.issueRejectNo = issueRejectNo;
		this.issueRejectDate = issueRejectDate;
		this.indentNo = indentNo;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.visitNo = visitNo;
		this.visitType = visitType;
		this.store = store;
		this.patientName = patientName;
		this.ageString = ageString;
	}

	public PatientIssueRejectionSearchResult(Integer id, Integer issueId, String issueRejectNo, Date issueRejectDate,
			String indentNo, String issueNo, String uhid, String patientDetail, String doctorDetail, String visitNo,
			String visitType) {
		super();
		this.id = id;
		this.issueId = issueId;
		this.issueRejectNo = issueRejectNo;
		this.issueRejectDate = issueRejectDate;
		this.indentNo = indentNo;
		this.issueNo = issueNo;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.visitNo = visitNo;
		this.visitType = visitType;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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

//	public String getAgeString() {
//		return ageString;
//	}
//
//	public void setAgeString(String ageString) {
//		this.ageString = ageString;
//	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public Integer getAgeString() {
		return ageString;
	}

	public void setAgeString(Integer ageString) {
		this.ageString = ageString;
	}
	
	public String getAgeStringDet() {
		return ageStringDet;
		}

		public void setAgeStringDet(String ageStringDet) {
		this.ageStringDet = ageStringDet;
		}

		public Integer getMedicationType() {
			return medicationType;
		}

		public void setMedicationType(Integer medicationType) {
			this.medicationType = medicationType;
		}

		public Integer getPatientId() {
			return patientId;
		}

		public void setPatientId(Integer patientId) {
			this.patientId = patientId;
		}
		
		
}
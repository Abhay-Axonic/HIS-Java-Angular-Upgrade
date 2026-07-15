package com.param.pharmacy.nursing.pharmacy.dto.patientIndent;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PatientIndentDetails {
	private Integer id;

	private Integer visitTypeId;

	private String visitType;

	private Integer wardId;

	private String wardName;

	private Integer bedId;

	private String bedNo;

	private Integer storeId;

	private String store;
	private Date doa;
	private Date dob;

	private Integer doctoreId;

	private String doctorDetail;

	private Date pdd;

	private Integer issuingStoreId;
	private Integer genderId;
	
	private String imagePath;

	private String issuingStore;

	private boolean consignment;

	public Date getDoa() {
		return doa;
	}

	public void setDoa(Date doa) {
		this.doa = doa;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Integer getDoctoreId() {
		return doctoreId;
	}

	public void setDoctoreId(Integer doctoreId) {
		this.doctoreId = doctoreId;
	}

	public String getDoctorDetail() {
		return doctorDetail;
	}

	public void setDoctorDetail(String doctorDetail) {
		this.doctorDetail = doctorDetail;
	}

	public Date getPdd() {
		return pdd;
	}

	public void setPdd(Date pdd) {
		this.pdd = pdd;
	}

	private String visitNo;

	private Integer patientId;

	private String patientName;

	private String uhid;

	private Integer doctorId;

	private Integer admissionId;

	private Integer encounterId;

	private String remark;

	private List<PatientIndentItem> items;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
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

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public Integer getIssuingStoreId() {
		return issuingStoreId;
	}

	public void setIssuingStoreId(Integer issuingStoreId) {
		this.issuingStoreId = issuingStoreId;
	}

	public String getIssuingStore() {
		return issuingStore;
	}

	public void setIssuingStore(String issuingStore) {
		this.issuingStore = issuingStore;
	}

	public boolean isConsignment() {
		return consignment;
	}

	public void setConsignment(boolean consignment) {
		this.consignment = consignment;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
	}

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<PatientIndentItem> getItems() {
		return items;
	}

	public void setItems(List<PatientIndentItem> items) {
		this.items = items;
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

	public PatientIndentDetails(Integer id, Integer visitTypeId, String visitType, Integer storeId, String store,
			Integer issuingStoreId, String issuingStore, boolean consignment, String visitNo, Integer patientId,
			String patientName, String uhid, Integer doctorId ) {
		super();
		this.id = id;
		this.visitTypeId = visitTypeId;
		this.visitType = visitType;
		this.storeId = storeId;
		this.store = store;
		this.issuingStoreId = issuingStoreId;
		this.issuingStore = issuingStore;
		this.consignment = consignment;
		this.visitNo = visitNo;
		this.patientId = patientId;
		this.patientName = patientName;
		this.uhid = uhid;
		this.doctorId = doctorId;
		
	}

	public PatientIndentDetails() {
		super();
	}

}

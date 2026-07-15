package com.param.pharmacy.nursing.crashCart.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CrashCartResponse {

	private Integer id;

	private String crashCartNo;

	private Date crashCartDate;

	private Date codeBlueDateTime;

	private String visitType;

	private String store;
	//added storeId code for resolved HIS-10354 Crash Cart Request Approval - 
	private Integer storeId;

	private String uhid;

	private String patientDetail;

	private String doctorDetail;

	private String visitNo;

	private String wardName;

	private String bedNo;

	private String requestedBy;

	private String returnBy;

	private Integer statusId;

	private String status;

	private String remark;

	private Date pdd;

	private Date doa;

	private Date dob;

	private List<ItemResponse> items;
	
	private Integer visitTypeId;
	
	private Integer patientId;
	
	private Integer admissionId;
	
	private Integer encounterId;

	private Integer genderId;

	private String genderCode;

	private String imagePath;

	public CrashCartResponse() {
		super();
	}

	public CrashCartResponse(Integer id, String crashCartNo, Date crashCartDate, Date codeBlueDateTime,
			String visitType, String store, String uhid, String patientDetail, String doctorDetail, String wardName,
			String bedNo, String requestedBy, Integer statusId, String status) {
		super();
		this.id = id;
		this.crashCartNo = crashCartNo;
		this.crashCartDate = crashCartDate;
		this.codeBlueDateTime = codeBlueDateTime;
		this.visitType = visitType;
		this.store = store;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.wardName = wardName;
		this.bedNo = bedNo;
		this.requestedBy = requestedBy;
		this.statusId = statusId;
		this.status = status;
	}

	public CrashCartResponse(Integer id, String crashCartNo, Date crashCartDate, String visitType, String store,
			String uhid, String patientDetail, String doctorDetail, String visitNo, String wardName, String bedNo,
			Date pdd, Date doa, Date dob, String remark) {
		super();
		this.id = id;
		this.crashCartNo = crashCartNo;
		this.crashCartDate = crashCartDate;
		this.visitType = visitType;
		this.store = store;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.visitNo = visitNo;
		this.wardName = wardName;
		this.bedNo = bedNo;
		this.pdd = pdd;
		this.doa = doa;
		this.dob = dob;
		this.remark = remark;
	}
	
	public CrashCartResponse(Integer id, String crashCartNo, Date crashCartDate, String visitType, String store,
			String uhid, String patientDetail, String doctorDetail, String visitNo, String wardName, String bedNo,
			Date pdd, Date doa, Date dob, String remark, Integer statusId,String status, Integer genderId, String genderCode, String imagePath) {
		super();
		this.id = id;
		this.crashCartNo = crashCartNo;
		this.crashCartDate = crashCartDate;
		this.visitType = visitType;
		this.store = store;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.visitNo = visitNo;
		this.wardName = wardName;
		this.bedNo = bedNo;
		this.pdd = pdd;
		this.doa = doa;
		this.dob = dob;
		this.remark = remark;
		this.statusId = statusId;
		this.status = status;
		this.genderId = genderId;
		this.genderCode = genderCode;
		this.imagePath = imagePath;
	}

	public CrashCartResponse(Integer id, String crashCartNo, Date crashCartDate, String visitType, String store, String uhid, String patientDetail,
			String doctorDetail, String visitNo, Date dob, String remark, Integer genderId, String genderCode, String imagePath ) {
		super();
		this.id = id;
		this.crashCartNo = crashCartNo;
		this.crashCartDate = crashCartDate;
		this.visitType = visitType;
		this.store = store;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.visitNo = visitNo;
		this.dob = dob;
		this.remark = remark;
		this.genderId = genderId;
		this.genderCode = genderCode;
		this.imagePath = imagePath;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCrashCartNo() {
		return crashCartNo;
	}

	public void setCrashCartNo(String crashCartNo) {
		this.crashCartNo = crashCartNo;
	}

	public Date getCrashCartDate() {
		return crashCartDate;
	}

	public void setCrashCartDate(Date crashCartDate) {
		this.crashCartDate = crashCartDate;
	}

	public Date getCodeBlueDateTime() {
		return codeBlueDateTime;
	}

	public void setCodeBlueDateTime(Date codeBlueDateTime) {
		this.codeBlueDateTime = codeBlueDateTime;
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

	/* start added storeId code for resolved HIS-10354 Crash Cart Request Approval - */
	public Integer getStoreId() {
		return storeId;
	}
	
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}	
	/* end added storeId code for resolved HIS-10354 Crash Cart Request Approval - */

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

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
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

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getReturnBy() {
		return returnBy;
	}

	public void setReturnBy(String returnBy) {
		this.returnBy = returnBy;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getPdd() {
		return pdd;
	}

	public void setPdd(Date pdd) {
		this.pdd = pdd;
	}

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

	public List<ItemResponse> getItems() {
		return items;
	}

	public void setItems(List<ItemResponse> items) {
		this.items = items;
	}

	public void addItems(ItemResponse item) {
		if (this.items == null)
			this.items = new ArrayList<ItemResponse>();

		this.items.add(item);
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
}

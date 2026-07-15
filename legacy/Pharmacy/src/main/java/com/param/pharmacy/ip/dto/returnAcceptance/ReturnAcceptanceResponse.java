package com.param.pharmacy.ip.dto.returnAcceptance;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class ReturnAcceptanceResponse
{
	private Integer id;
	
	private String returnNo;
	
	private Date returnDate;
	
	private String uhid;
	
	private String patientDetail;
	
	private String doctorDetail;
	
	private String visitType;
	
	private String store;
	
	private String wardName;
	
	private String bedNo;
	private Integer storeId;
	private Integer issueId;
	private boolean atCOP;
	 public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	private Integer genderId;
		private String imagePath;   
		    private String ageStringDet;
		    private String genderCode; 

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	private Integer wardId;
	
	private String indentNo;
	
	private String visitNo;
	
	private Integer indentId;
	
	private Integer statusId;
	   
	private String status;
	private String patientName;
	private Integer ageString;
	private Integer patientId;
	
	private List<ReturnAcceptanceItem> items;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
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

	public Integer getIndentId() {
		return indentId;
	}

	public void setIndentId(Integer indentId) {
		this.indentId = indentId;
	}

	public List<ReturnAcceptanceItem> getItems() {
		return items;
	}

	public void setItems(List<ReturnAcceptanceItem> items) {
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

	
	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public ReturnAcceptanceResponse() {
		super();
	}

	public ReturnAcceptanceResponse(Integer id, String returnNo, Date returnDate, String uhid, String patientDetail,
			String doctorDetail, String visitType, String store, Integer patientId) {
		super();
		this.id = id;
		this.returnNo = returnNo;
		this.returnDate = returnDate;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.visitType = visitType;
		this.store = store;
		this.patientId=patientId;
	
}
	public ReturnAcceptanceResponse(Integer id, String returnNo, Date returnDate, String uhid, String patientDetail,
			String doctorDetail, String visitType, String store, Integer patientId, String patientName, Integer ageString) {
		super();
		this.id = id;
		this.returnNo = returnNo;
		this.returnDate = returnDate;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.visitType = visitType;
		this.store = store;
		this.patientId=patientId;
		this.patientName=patientName;
		this.ageString=ageString;
}
	public ReturnAcceptanceResponse(Integer id, String returnNo, Date returnDate, String uhid, String patientDetail,
			String doctorDetail, String visitType, String store, String indentNo) {
		super();
		this.id = id;
		this.returnNo = returnNo;
		this.returnDate = returnDate;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.visitType = visitType;
		this.store = store;
		this.indentNo = indentNo;
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

	public String getAgeStringDet() {
		return ageStringDet;
	}

	public void setAgeStringDet(String ageStringDet) {
		this.ageStringDet = ageStringDet;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Integer getAgeString() {
		return ageString;
	}

	public void setAgeString(Integer ageString) {
		this.ageString = ageString;
	}

	public boolean getAtCOP() {
		return atCOP;
	}

	public void setAtCOP(boolean atCOP) {
		this.atCOP = atCOP;
	}

	
	
	
}
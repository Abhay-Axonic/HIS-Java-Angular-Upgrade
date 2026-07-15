package com.param.pharmacy.nursing.pharmacy.dto.billableConsumption;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class BillableConsumptionResponse {
	private Integer id;

	private String consumptionNo;

	private Date consumptionDate;

	private String returnNo;

	private Date returnDate;

	private String visitType;

	private String visitNo;

	private String store;

	private String uhid;

	private String patientDetail;

	private String wardName;

	private String bedNo;

	private String issuedBy;

	private String returnBy;

	private Integer age;
	private String genderCode;
	private String imagePath;
	private Integer statusId;
	private String status;
	private Integer patientId;

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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConsumptionNo() {
		return consumptionNo;
	}

	public void setConsumptionNo(String consumptionNo) {
		this.consumptionNo = consumptionNo;
	}

	public Date getConsumptionDate() {
		return consumptionDate;
	}

	public void setConsumptionDate(Date consumptionDate) {
		this.consumptionDate = consumptionDate;
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

	public String getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
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

	public String getReturnBy() {
		return returnBy;
	}

	public void setReturnBy(String returnBy) {
		this.returnBy = returnBy;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
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

	public BillableConsumptionResponse() {
		super();
	}

	public BillableConsumptionResponse(Integer id, String consumptionNo, Date consumptionDate, String visitType,
			String store, String uhid, String patientDetail, Integer age, String genderCode, String imagePath,
			String issuedBy, String wardName, String bedNo) {
		super();
		this.id = id;
		this.consumptionNo = consumptionNo;
		this.consumptionDate = consumptionDate;
		this.visitType = visitType;
		this.store = store;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.age = age;
		this.genderCode = genderCode;
		this.imagePath = imagePath;
		this.issuedBy = issuedBy;
		this.wardName = wardName;
		this.bedNo = bedNo;
	}

	public BillableConsumptionResponse(Integer id, String returnNo, Date returnDate, String visitType, String store,
			String uhid, String patientDetail, Integer age, String genderCode, String imagePath, String returnBy) {
		super();
		this.id = id;
		this.returnNo = returnNo;
		this.returnDate = returnDate;
		this.visitType = visitType;
		this.store = store;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.age = age;
		this.genderCode = genderCode;
		this.imagePath = imagePath;
		this.returnBy = returnBy;
	}

	public BillableConsumptionResponse(Integer id, String returnNo, Date returnDate, String visitType, String store,
			String uhid, String patientDetail, Integer age, String genderCode, String imagePath, String returnBy,
			String visitNo) {
		this(id, returnNo, returnDate, visitType, store, uhid, patientDetail, age, genderCode, imagePath, returnBy);
		this.visitNo = visitNo;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

}
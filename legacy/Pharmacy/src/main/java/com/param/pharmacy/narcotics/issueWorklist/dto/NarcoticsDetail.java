package com.param.pharmacy.narcotics.issueWorklist.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.pharmacy.op.dto.salesWorkList.DiscountCategoryResponse;

@JsonInclude(Include.NON_NULL)
public class NarcoticsDetail
{
	 private Integer id;
	 
	 private Integer storeId;
	 
	 private Integer orderId;
	 
	 private Integer visiType;
	 
	 private Integer admissionId;
	 
	 private Integer encounterId;
	 
	 private String remark;
	 
	 private Integer patientId;
	 
	 private String patientDetail;
	 
	 private String dob;
	 
	 private Integer doctoreId;
	 
	 private String doctorDetail;
	 
	 private String wardName;
	 
	 private String bedNo;
	 
	 private String uhid;
	 
	 private List<DiscountCategoryResponse> discountCategory;
	  
	 private List<NarcoticsItem> items;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getVisiType() {
		return visiType;
	}

	public void setVisiType(Integer visiType) {
		this.visiType = visiType;
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

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getPatientDetail() {
		return patientDetail;
	}

	public void setPatientDetail(String patientDetail) {
		this.patientDetail = patientDetail;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
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

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
	}

	public List<DiscountCategoryResponse> getDiscountCategory() {
		return discountCategory;
	}

	public void setDiscountCategory(List<DiscountCategoryResponse> discountCategory) {
		this.discountCategory = discountCategory;
	}

	public List<NarcoticsItem> getItems() {
		return items;
	}

	public void setItems(List<NarcoticsItem> items) {
		this.items = items;
	}

	public NarcoticsDetail(Integer id, Integer storeId, Integer orderId, Integer visiType, String remark,
			Integer patientId, String patientDetail, String dob, Integer doctoreId, String doctorDetail, String uhid) {
		super();
		this.id = id;
		this.storeId = storeId;
		this.orderId = orderId;
		this.visiType = visiType;
		this.remark = remark;
		this.patientId = patientId;
		this.patientDetail = patientDetail;
		this.dob = dob;
		this.doctoreId = doctoreId;
		this.doctorDetail = doctorDetail;
		this.uhid = uhid;
	}
	 
}
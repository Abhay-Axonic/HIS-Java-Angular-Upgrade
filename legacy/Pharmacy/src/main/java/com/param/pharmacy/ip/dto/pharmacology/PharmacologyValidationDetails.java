 package com.param.pharmacy.ip.dto.pharmacology;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.pharmacy.op.dto.salesWorkList.DiscountCategoryResponse;

@JsonInclude(Include.NON_NULL)
public class PharmacologyValidationDetails
 {
  private Long id;
  
  private Long prescriptionId;
  
  private Integer issueId;
  
  private Integer assignPickerId;
  
  private Integer patientId;
  
  private Integer tempPatientId;
  
  private String patientDetail;
 
  private Date dob;
  
  private Integer doctorId;
  
  private Integer tempDoctorId;
  
  private String doctorDetail;
  
  private Date doa;
  
  private Date pdd;
  
  private String wardName;
  
  private String bedNo;
  
  private String itemName;
  
  private String genericName;
  
  private Double dose;
  
  private String picker;
  
  private Integer pickerId;
  
  private String uhid;
  
  private  String prescriptionNo;
  
  private Integer storeId;
  
  private Integer orderId;
  
  private Integer visitType;
  
  private Integer admissionId;
  
  private Integer encounterId;
  
  private String remark;
  
  private Integer statusId;
  
  private String status;
  
  private Timestamp patientDob;
  
  private List<DiscountCategoryResponse> discountCategory;
  
  private List<PharmacologyItem> items;
  private String specialityName;
  private Integer genderId;
  private String imagePath;
	
  public PharmacologyValidationDetails(List<PharmacologyItem> items) {
		this.items = items;
	}

	 public PharmacologyValidationDetails() {

	 }

	 public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
	
	public Date getDob() {
		return dob;
	}
	
	public void setDob(Date dob) {
		this.dob = dob;
	}
	
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public String getUhid() {
		return uhid;
	}
	public void setUhid(String uhid) {
		this.uhid = uhid;
	}
	public String getDoctorDetail() {
		return doctorDetail;
	}
	
	public void setDoctorDetail(String doctorDetail) {
		this.doctorDetail = doctorDetail;
	}
	
	public Date getDoa() {
		return doa;
	}
	
	public void setDoa(Date doa) {
		this.doa = doa;
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
	
	public List<PharmacologyItem> getItems() {
		return items;
	}
	
	public void setItems(List<PharmacologyItem> items) {
		this.items = items;
	}
	
	public Date getPdd() {
		return pdd;
	}
	public void setPdd(Date pdd) {
		this.pdd = pdd;
	}
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getGenericName() {
		return genericName;
	}
	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}
	public Double getDose() {
		return dose;
	}
	public void setDose(Double dose) {
		this.dose = dose;
	}
	public String getPicker() {
		return picker;
	}
	public void setPicker(String picker) {
		this.picker = picker;
	}
	public Integer getPickerId() {
		return pickerId;
	}
	public void setPickerId(Integer pickerId) {
		this.pickerId = pickerId;
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
	
	public Integer getVisitType() {
		return visitType;
	}
	public void setVisitType(Integer visitType) {
		this.visitType = visitType;
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
	
	public Integer getAssignPickerId() {
		return assignPickerId;
	}
	public void setAssignPickerId(Integer assignPickerId) {
		this.assignPickerId = assignPickerId;
	}
	
	public Integer getIssueId() {
		return issueId;
	}
	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}
	public PharmacologyValidationDetails(Long id, Integer patientId, String patientDetail, Date dob, Integer doctorId,
			String doctorDetail, List<PharmacologyItem> items) {
		super();
		this.id = id;
		this.patientId = patientId;
		this.patientDetail = patientDetail;
		this.dob = dob;
		this.doctorId = doctorId;
		this.doctorDetail = doctorDetail;
		this.items = items;
	}
	
	public PharmacologyValidationDetails(Long id, Integer patientId, String patientDetail, Date dob, Integer doctorId,
			String doctorDetail, Date doa, String wardName, String bedNo, List<PharmacologyItem> items,Date pdd, String uhid, List<DiscountCategoryResponse> discountCategory) {
		super();
		this.id = id;
		this.patientId = patientId;
		this.patientDetail = patientDetail;
		this.dob = dob;
		this.doctorId = doctorId;
		this.doctorDetail = doctorDetail;
		this.doa = doa;
		this.items = items;
		this.pdd = pdd;
		this.uhid = uhid;
		this.discountCategory = discountCategory;
	}
	
	public PharmacologyValidationDetails(Long id, Integer patientId, String patientDetail, Date dob, Integer doctorId,
			String doctorDetail, Date doa, Date pdd,String uhid) {
		super();
		this.id = id;
		this.patientId = patientId;
		this.patientDetail = patientDetail;
		this.dob = dob;
		this.doctorId = doctorId;
		this.doctorDetail = doctorDetail;
		this.doa = doa;
		this.pdd = pdd;
		this.uhid = uhid;
	}
	
	
	public PharmacologyValidationDetails(Long id, Integer patientId, String patientDetail,
			String doctorDetail, Date doa, Date pdd) {
		super();
		this.id = id;
		this.tempPatientId = patientId;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.doa = doa;
		this.pdd = pdd;
	}
	
	public PharmacologyValidationDetails(Long id, Integer patientId, String patientDetail, Date dob, Integer doctorId,
			String doctorDetail, Date doa, Date pdd, Integer pickerId, String picker) {
		super();
		this.id = id;
		this.patientId = patientId;
		this.patientDetail = patientDetail;
		this.dob = dob;
		this.doctorId = doctorId;
		this.doctorDetail = doctorDetail;
		this.doa = doa;
		this.pdd = pdd;
		this.pickerId = pickerId;
		this.picker = picker;
	}
	public PharmacologyValidationDetails(Long id, Integer patientId, String patientDetail, Date dob, Integer doctorId,
			String doctorDetail, List<PharmacologyItem> items,Integer pickerId, String picker) {
		super();
		this.id = id;
		this.patientId = patientId;
		this.patientDetail = patientDetail;
		this.dob = dob;
		this.doctorId = doctorId;
		this.doctorDetail = doctorDetail;
		this.items = items;
		
	}
	public PharmacologyValidationDetails(String itemName, String genericName, Double dose,
			List<PharmacologyItem> items) {
		super();
		this.itemName = itemName;
		this.genericName = genericName;
		this.dose = dose;
		this.items = items;
	}
	
	public PharmacologyValidationDetails(Long id, Integer patientId, String patientDetail, Date dob, Integer doctorId,
			String doctorDetail, String uhid, Integer visitType,
			List<DiscountCategoryResponse> discountCategory, List<PharmacologyItem> items) {
		super();
		this.id = id;
		this.patientId = patientId;
		this.patientDetail = patientDetail;
		this.dob = dob;
		this.doctorId = doctorId;
		this.doctorDetail = doctorDetail;
		this.uhid = uhid;
		this.visitType = visitType;
		this.discountCategory = discountCategory;
		this.items = items;
	}
	
	public PharmacologyValidationDetails(Long id, Integer patientId, String patientDetail, Date dob, Integer doctorId,
			String doctorDetail, String uhid, Integer visitType,  List<PharmacologyItem> items) {
		super();
		this.id = id;
		this.patientId = patientId;
		this.patientDetail = patientDetail;
		this.dob = dob;
		this.doctorId = doctorId;
		this.doctorDetail = doctorDetail;
		this.uhid = uhid;
		this.visitType = visitType; 
		this.items = items;
	}
	public List<DiscountCategoryResponse> getDiscountCategory() {
		return discountCategory;
	}
	public void setDiscountCategory(List<DiscountCategoryResponse> discountCategory) {
		this.discountCategory = discountCategory;
	}
	public Long getPrescriptionId() {
		return prescriptionId;
	}
	public void setPrescriptionId(Long prescriptionId) {
		this.prescriptionId = prescriptionId;
	}
	public Timestamp getPatientDob() {
		return patientDob;
	}
	public void setPatientDob(Timestamp patientDob) {
		this.patientDob = patientDob;
	}
	public Integer getTempPatientId() {
		return tempPatientId;
	}
	public void setTempPatientId(Integer tempPatientId) {
		this.tempPatientId = tempPatientId;
	}
	public Integer getTempDoctorId() {
		return tempDoctorId;
	}
	public void setTempDoctorId(Integer tempDoctorId) {
		this.tempDoctorId = tempDoctorId;
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
	  
	
}
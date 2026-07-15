package com.param.pharmacy.op.dto.salesWorkList;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrescriptionDetailResponse {

	private Long id;

	private String uhid;

	private String patientDetail;

	private String doctorDetail;

	private String wardName;
	
	private String remark;

	private String bedNo;

	private Date pdd;

	private Date doa;

	private Integer statusId;

	private String status;

	private List<DiscountCategoryResponse> discountCategory;

	private List<PrescriptionDetailItemResponse> items;

	private Integer patientId;

	private Integer doctorId;

	private Date prescriptionDate;

	private String prescriptionNo;

	private Integer encounterId;

	private Long saleId;

	private String billNo;

	private Date saleDate;
	
	private Timestamp patientDob;
	
	private Integer patientCategoryId;
	private String specialityName;

	private Integer genderId;
	
	private String imagePath;
	
	private String dob;
	
	private Integer organizationId;
	
	private Integer billId;
	
	private String billNumber;
	private String instructionDesc;
	private String frequencyDesc;
	private Date startDate;
	private Integer specialityId;
	private boolean cop;
	
	public String getInstructionDesc() {
		return instructionDesc;
	}

	public void setInstructionDesc(String instructionDesc) {
		this.instructionDesc = instructionDesc;
	}

	public String getFrequencyDesc() {
		return frequencyDesc;
	}

	public void setFrequencyDesc(String frequencyDesc) {
		this.frequencyDesc = frequencyDesc;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<PrescriptionDetailItemResponse> getItems() {
		return items;
	}

	public void setItems(List<PrescriptionDetailItemResponse> items) {
		this.items = items;
	}

	public List<DiscountCategoryResponse> getDiscountCategory() {
		return discountCategory;
	}

	public void setDiscountCategory(List<DiscountCategoryResponse> discountCategory) {
		this.discountCategory = discountCategory;
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

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public String getPrescriptionNo() {
		return prescriptionNo;
	}

	public void setPrescriptionNo(String prescriptionNo) {
		this.prescriptionNo = prescriptionNo;
	}

	public PrescriptionDetailResponse() {

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

	public Date getPrescriptionDate() {
		return prescriptionDate;
	}

	public void setPrescriptionDate(Date prescriptionDate) {
		this.prescriptionDate = prescriptionDate;
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

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Date getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	public PrescriptionDetailResponse(String uhid, String patientDetail, Long id, String doctorDetail,
			List<DiscountCategoryResponse> discountCategory, Integer patientId, Integer doctorId, Date prescriptionDate,
			String prescriptionNo) {
		super();
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.id = id;
		this.doctorDetail = doctorDetail;
		this.discountCategory = discountCategory;
		this.patientId = patientId;
		this.doctorId = doctorId;
		this.prescriptionNo = prescriptionNo;
		this.prescriptionDate = prescriptionDate;
	}

	public PrescriptionDetailResponse(String uhid, String patientDetail, Long id, String doctorDetail,
			List<DiscountCategoryResponse> discountCategory, Integer patientId, Integer doctorId, Date prescriptionDate,
			String prescriptionNo, Integer statusId, String status, Integer encounterId, Long saleId, String billNo,
			Date saleDate) {

		this(uhid, patientDetail, id, doctorDetail, discountCategory, patientId, doctorId, prescriptionDate,
				prescriptionNo);

		this.statusId = statusId;
		this.status = status;
		this.encounterId = encounterId;
		this.saleId = saleId;
		this.billNo = billNo;
		this.saleDate = saleDate;
	}

	public Timestamp getPatientDob() {
		return patientDob;
	}

	public void setPatientDob(Timestamp patientDob) {
		this.patientDob = patientDob;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
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

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean getCop() {
		return cop;
	}

	public void setCop(boolean cop) {
		this.cop = cop;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

}

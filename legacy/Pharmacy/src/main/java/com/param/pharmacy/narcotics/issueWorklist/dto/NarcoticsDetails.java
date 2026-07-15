package com.param.pharmacy.narcotics.issueWorklist.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.Carrier;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyItem;

@JsonInclude(Include.NON_NULL)
public class NarcoticsDetails
{
	private Long id;
	
    private String uhid;
    
    private String patientDetail;
    
    private String wardName;
    
    private String bedNo;
    
	private Date doa;
	
	private Date pdd;
	
	private Date dob;
	
	private String doctorDetail;
	
    private List<PharmacologyItem> items;
    
    private List<Carrier> carriers;
    
    private Integer statusId;
    
    private String status;
    
    private String carrierName;

    private Integer carrierId;

    private Integer genderId;
    
    private Integer patientId;
    
    private String imagePath;

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

	public Date getDoa() {
		return doa;
	}

	public void setDoa(Date doa) {
		this.doa = doa;
	}

	public Date getPdd() {
		return pdd;
	}

	public void setPdd(Date pdd) {
		this.pdd = pdd;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getDoctorDetail() {
		return doctorDetail;
	}

	public void setDoctorDetail(String doctorDetail) {
		this.doctorDetail = doctorDetail;
	}
	
	public List<Carrier> getCarriers() {
		return carriers;
	}

	public void setCarriers(List<Carrier> carriers) {
		this.carriers = carriers;
	}

	public List<PharmacologyItem> getItems() {
		return items;
	}

	public void setItems(List<PharmacologyItem> items) {
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
	

	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	public Integer getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Integer carrierId) {
		this.carrierId = carrierId;
	}

	public NarcoticsDetails() {
		super();
	}

	public NarcoticsDetails(Long id, String uhid, String patientDetail, Date dob,
			String doctorDetail) {
		super();
		this.id = id;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.dob = dob;
		this.doctorDetail = doctorDetail;
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

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	
	
	
}
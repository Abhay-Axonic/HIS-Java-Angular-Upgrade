 package com.param.pharmacy.ip.dto.handoverWorklist;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.Carrier;

@JsonInclude(Include.NON_NULL)
public class HandoverDetails
{

	private Long id;
	
	private Integer patientId;
	
    private String patientDetail;
    
    private Date dob;
    
    private Integer doctoreId;
    
    private String doctorDetail;
    
    private String wardName;
    
    private String bedNo;
    
    private String uhid;
    
    private Date doa;
    
    private Date pdd;
    
    private Integer statusId;
	   
	private String status;
	
	private Integer receiverId;
	
	private String receiverName;
	   
	private List<HandoverDetailItem> items;
	
	private Integer admissionId;
	
	private Timestamp patientDob;
	
	private Integer doctorId;
	
	private Integer genderId;
	
	private String imagePath;
	
	private List<Carrier> carrier;

	public HandoverDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Timestamp getPatientDob() {
		return patientDob;
	}

	public void setPatientDob(Timestamp patientDob) {
		this.patientDob = patientDob;
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

	public List<HandoverDetailItem> getItems() {
		return items;
	}

	public void setItems(List<HandoverDetailItem> items) {
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

	public Integer getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Integer receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public HandoverDetails(Long id, Integer patientId, String patientDetail, Date dob, Integer doctoreId,
			String doctorDetail, String uhid, Date doa, Date pdd,
			List<HandoverDetailItem> items) {
		super();
		this.id = id;
		this.patientId = patientId;
		this.patientDetail = patientDetail;
		this.dob = dob;
		this.doctoreId = doctoreId;
		this.doctorDetail = doctorDetail;
		this.uhid = uhid;
		this.doa = doa;
		this.pdd = pdd;
		this.items = items;
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

	public List<Carrier> getCarrier() {
		return carrier;
	}

	public void setCarrier(List<Carrier> carrier) {
		this.carrier = carrier;
	}
	
}
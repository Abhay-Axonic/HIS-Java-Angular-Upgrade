package com.param.pharmacy.ip.dto.pendingIssues.acceptance;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PendingIssuesAcceptanceDetails
{
	private Integer id;
	
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
    
	private String issueNo;
	
	private Date issueDate;
	
	private String indentNo;
	
	private String store;
	
	private Integer statusId;
	   
	private String status;
	
	private List<PendingIssuesAcceptanceItem> items;
	
	private Integer genderId;
	
	private String imagePath;
	
	private String carrier;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(String indentNo) {
		this.indentNo = indentNo;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public List<PendingIssuesAcceptanceItem> getItems() {
		return items;
	}

	public void setItems(List<PendingIssuesAcceptanceItem> items) {
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

	public void addItems(PendingIssuesAcceptanceItem item) {
		if (this.items == null)
			this.items = new ArrayList<PendingIssuesAcceptanceItem>();
		
		this.items.add(item);
	}

	public PendingIssuesAcceptanceDetails()
	{
		super();
	}
	public PendingIssuesAcceptanceDetails(Integer id, Integer patientId, String patientDetail, Date dob,
			Integer doctoreId, String doctorDetail, String uhid,
			String issueNo, Date issueDate, String indentNo, String store) {
		super();
		this.id = id;
		this.patientId = patientId;
		this.patientDetail = patientDetail;
		this.dob = dob;
		this.doctoreId = doctoreId;
		this.doctorDetail = doctorDetail;
		this.uhid = uhid;
		this.issueNo = issueNo;
		this.issueDate = issueDate;
		this.indentNo = indentNo;
		this.store = store;
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

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}
	
}
package com.param.pharmacy.nursing.workstation.dto.patientIssueReturn;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class PatientAgainstIssueResponse
{
	private Integer id;
	
	private String issueNo;
	
	private Date issueDate;
	
	private String patientDetail;
	
	private String uhid;
	
	private Date visitDate;
	
	private String visitNo;
	
	private String indentNo;
	private String storeName;
	private Integer storeId;
	private Integer toStoreId;
	private String toStoreName;

	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	public String getToStoreName() {
		return toStoreName;
	}

	public void setToStoreName(String toStoreName) {
		this.toStoreName = toStoreName;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getPatientDetail() {
		return patientDetail;
	}

	public void setPatientDetail(String patientDetail) {
		this.patientDetail = patientDetail;
	}

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}
	
	public String getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(String indentNo) {
		this.indentNo = indentNo;
	}

	public PatientAgainstIssueResponse(Integer id, String issueNo, Date issueDate, String patientDetail, String uhid,
			Date visitDate, String visitNo) {
		super();
		this.id = id;
		this.issueNo = issueNo;
		this.issueDate = issueDate;
		this.patientDetail = patientDetail;
		this.uhid = uhid;
		this.visitDate = visitDate;
		this.visitNo = visitNo;
	}

}
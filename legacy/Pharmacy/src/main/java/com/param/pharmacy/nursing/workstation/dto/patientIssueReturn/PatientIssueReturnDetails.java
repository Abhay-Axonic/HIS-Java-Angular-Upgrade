package com.param.pharmacy.nursing.workstation.dto.patientIssueReturn;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class PatientIssueReturnDetails
{
	private Integer id;
	
	private String visitType;
	
	private Integer patientId;
	
	private String uhid;
	
	private String wardName;
	
	private String bedNo;
	
	private String visitNo;
	
	private String store;
	
	private String returnNo;
	
	private String patientDetail;
	
	private List<PatientIssueReturnDetailItem> items;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
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

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(String returnNo) {
		this.returnNo = returnNo;
	}

	public String getPatientDetail() {
		return patientDetail;
	}

	public void setPatientDetail(String patientDetail) {
		this.patientDetail = patientDetail;
	}

	public List<PatientIssueReturnDetailItem> getItems() {
		return items;
	}

	public void setItems(List<PatientIssueReturnDetailItem> items) {
		this.items = items;
	}

	public PatientIssueReturnDetails()
	{
		super();
	}
	public PatientIssueReturnDetails(Integer id, String visitType, Integer patientId, String uhid, String store,
			String returnNo, String patientDetail) {
		super();
		this.id = id;
		this.visitType = visitType;
		this.patientId = patientId;
		this.uhid = uhid;
		this.store = store;
		this.returnNo = returnNo;
		this.patientDetail = patientDetail;
	}
	
	
	
}
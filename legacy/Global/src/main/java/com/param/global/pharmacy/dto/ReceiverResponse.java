package com.param.global.pharmacy.dto;

public class ReceiverResponse {

	private Integer id;
	private Integer prescriptionid;
	private String	relationname;
	private Integer relationid;
	private String contactdetail;
	private String receivername;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPrescriptionid() {
		return prescriptionid;
	}
	public void setPrescriptionid(Integer prescriptionid) {
		this.prescriptionid = prescriptionid;
	}
	public String getRelationname() {
		return relationname;
	}
	public void setRelationname(String relationname) {
		this.relationname = relationname;
	}
	public Integer getRelationid() {
		return relationid;
	}
	public void setRelationid(Integer relationid) {
		this.relationid = relationid;
	}
	public String getContactdetail() {
		return contactdetail;
	}
	public void setContactdetail(String contactdetail) {
		this.contactdetail = contactdetail;
	}
	public String getReceivername() {
		return receivername;
	}
	public void setReceivername(String receivername) {
		this.receivername = receivername;
	}

}

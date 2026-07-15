package com.param.procurement.consignment.dto.implantReturnAcceptance;

import java.util.Date;
import java.util.List;

public class ImplantReturnAcceptanceResponse
{
	private Integer id;
	
	private String returnNo;
	
	private Date returnDate;
	
	private String uhid;
	
	private String patientDetail;
	
	private String doctorDetail;
	
	private String visitType;
	
	private String store;
	
	private String wardName;
	
	private String bedNo;
	
	private Integer wardId;
	
	private String indentNo;
	
	private String visitNo;
	
	private Integer indentId;
	
	private Integer statusId;
	   
	private String status;
	
	private List<ImplantReturnAcceptanceResponseItems> items;
	

	public ImplantReturnAcceptanceResponse() 
	{
		super();
	}
	
	public ImplantReturnAcceptanceResponse(Integer id, String returnNo, Date returnDate, String uhid,
			String patientDetail, String doctorDetail, String visitType, String store)
	{
		super();
		this.id = id;
		this.returnNo = returnNo;
		this.returnDate = returnDate;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.visitType = visitType;
		this.store = store;
		

	}
	public ImplantReturnAcceptanceResponse(Integer id,String visitNo, String returnNo, Date returnDate, String uhid,
			String patientDetail, String doctorDetail, String visitType, String store)
	{
		super();
		this.id = id;
		this.visitNo=visitNo;
		this.returnNo = returnNo;
		this.returnDate = returnDate;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.visitType = visitType;
		this.store = store;
		

	}
	
	public ImplantReturnAcceptanceResponse(Integer id, String returnNo, Date returnDate, String uhid,
			String patientDetail, String doctorDetail, String visitType, String store,String indentNo)
	{
		super();
		this.id = id;
		this.returnNo = returnNo;
		this.returnDate = returnDate;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.visitType = visitType;
		this.store = store;
		this.indentNo=indentNo;
		

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public String getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(String indentNo) {
		this.indentNo = indentNo;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public Integer getIndentId() {
		return indentId;
	}

	public void setIndentId(Integer indentId) {
		this.indentId = indentId;
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

	public List<ImplantReturnAcceptanceResponseItems> getItems() {
		return items;
	}

	public void setItems(List<ImplantReturnAcceptanceResponseItems> items) {
		this.items = items;
	}
}

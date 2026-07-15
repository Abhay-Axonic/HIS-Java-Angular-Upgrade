package com.param.procurement.consignment.dto.patientConsumption;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PatientConsumtionIssueListResponse extends PatientConsumptionResponse {
	
	private String issueNo;

	private Date issueDate;

	private PatientConsumptionResponse patientConsumptionResponse;
	public PatientConsumtionIssueListResponse()
	{
		super();
	}
	public PatientConsumtionIssueListResponse(Integer id, String indentNo, Date indentDate, String visitType,
			String visitNo, String uhid, String patientDetail, String doctorDetail, String storeName, Integer statusId,
			String status) {
		super();
		this.id = id;
		this.indentNo = indentNo;
		this.indentDate = indentDate;
		this.visitType = visitType;
		this.visitNo = visitNo;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.storeName = storeName;
		this.statusId = statusId;
		this.status = status;

	}
	
	public PatientConsumtionIssueListResponse(Integer id,  String visitType,
			String visitNo, String uhid, String patientDetail, String doctorDetail, String storeName, Integer statusId,
			String status, String indentNo, Date indentDate,String issueNo) {
		super();
		this.id = id;
		this.indentNo = indentNo;
		this.indentDate = indentDate;
		this.visitType = visitType;
		this.visitNo = visitNo;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.storeName = storeName;
		this.statusId = statusId;
		this.status = status;
		this.consumptionNo = indentNo;
		this.consumptionDate = indentDate;
		this.issueNo=issueNo;

	}
	public PatientConsumtionIssueListResponse(Integer id, String issueNo, Date indentDate, String indentNo,
			Integer visitTypeId, String visitNo, String visitType, Integer storeId, String storeName,
			String uhid, String patientDetail, String doctorDetail, Integer statusId, String status)
	{
		super();
		this.id = id;
		this.issueNo=issueNo;
		this.indentDate = indentDate;
		this.indentNo = indentNo;
		this.visitTypeId=visitTypeId;
		this.visitNo=visitNo;
		this.visitType=visitType;
		this.storeId=storeId;
		this.storeName = storeName;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.statusId = statusId;
		this.status = status;
					
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
	
}

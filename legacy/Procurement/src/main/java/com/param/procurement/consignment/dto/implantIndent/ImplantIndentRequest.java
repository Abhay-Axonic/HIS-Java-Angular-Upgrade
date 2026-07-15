package com.param.procurement.consignment.dto.implantIndent;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.service.global.model.CookieObjectDto;

@JsonInclude(Include.NON_NULL)
public class ImplantIndentRequest extends CookieObjectDto{

	protected Integer id;

	protected Date fromDate;

	protected Date toDate;

	protected String uhid;

	protected String visitNo;

	protected Integer store;

	protected Integer visitType;

	protected Integer patientId;

	protected String patientName;

	protected String doctorName;

	protected Integer indentTypeId;

	protected Integer priorityId;

	protected Integer fromStoreId;

	protected Integer storeId;

	protected Boolean isConsignment;

	protected Boolean isImplant;

	protected String indentNo;
	
	protected String issueNo;


	protected Boolean isKit;

	protected String remark;

	protected Integer statusId;

	protected Integer approvalStatusId;

	protected Integer doctorId;

	protected Integer admissionId;

	protected Integer encounterId;

	private Integer cancelReasonId;

	private String cancelNote;
	
	private String implantIndentNo;
	
	protected Integer cookieStoreId;
	

	private List<Integer> impIndId;

	protected List<ImplantIndentRequestItems> items;
	

	public String getIssueNo() {
		return issueNo;
	}


	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}


	public ImplantIndentRequest() {
		super();
	}

	
	public String getImplantIndentNo() {
		return implantIndentNo;
	}


	public void setImplantIndentNo(String implantIndentNo) {
		this.implantIndentNo = implantIndentNo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
	}

	public Integer getVisitType() {
		return visitType;
	}

	public void setVisitType(Integer visitType) {
		this.visitType = visitType;
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

	public Integer getStore() {
		return store;
	}

	public void setStore(Integer store) {
		this.store = store;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Integer getIndentTypeId() {
		return indentTypeId;
	}

	public void setIndentTypeId(Integer indentTypeId) {
		this.indentTypeId = indentTypeId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public Boolean getIsImplant() {
		return isImplant;
	}

	public void setIsImplant(Boolean isImplant) {
		this.isImplant = isImplant;
	}

	public Boolean getIsKit() {
		return isKit;
	}

	public void setIsKit(Boolean isKit) {
		this.isKit = isKit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public List<Integer> getImpIndId() {
		return impIndId;
	}

	public void setImpIndId(List<Integer> impIndId) {
		this.impIndId = impIndId;
	}

	public List<ImplantIndentRequestItems> getItems() {
		return items;
	}

	public void setItems(List<ImplantIndentRequestItems> items) {
		this.items = items;
	}

	public Integer getFromStoreId() {
		return fromStoreId;
	}

	public void setFromStoreId(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
	}


	public Integer getCookieStoreId() {
		return cookieStoreId;
	}


	public void setCookieStoreId(Integer cookieStoreId) {
		this.cookieStoreId = cookieStoreId;
	}

	
}

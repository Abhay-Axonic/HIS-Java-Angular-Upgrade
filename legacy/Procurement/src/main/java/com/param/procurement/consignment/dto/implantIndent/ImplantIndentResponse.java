package com.param.procurement.consignment.dto.implantIndent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.entity.model.inventory.IndentType;
import com.param.entity.model.master.User;
import com.param.entity.model.master.VisitType;
import com.param.global.dto.global.ItemResponse;
import com.param.global.dto.global.Priority;
import com.param.global.dto.items.UomResponse;

@JsonInclude(Include.NON_NULL)
public class ImplantIndentResponse {

	protected Integer id;
	
	protected String issueNo;

	protected Date issueDate;

	protected String indentNo;
	
	protected String firstName;
	
	protected String middleName;
	
	protected String lastName;
	

	protected Date indentDate;
	
	private String indentType;
	
	private Integer indentTypeId;
	
	private String requestedBy;
	
	private Integer visitedTypeId;
	
	protected String visitType;

	protected String visitNo;

	protected String uhid;

	private Integer patientId;

	private String patientName;

	protected String patientDetail;

	protected Integer doctorId;

	protected String doctorDetail;

	protected Integer storeId;

	protected String store;

	protected String status;

	protected Integer statusId;

	protected String approvalStatus;

	protected Integer approvalStatusId;

	protected Integer priorityId;

	protected String priority;

	protected Boolean isImplant;
	
	protected Boolean isConsignmnt;
	
	protected Boolean isKit;
	
	protected String admissionNumber;
	
	private Integer admissionId;

	private Integer encounterId;

	private String remark;
	
	protected Boolean isConsignment;
	
	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}


	protected List<UomResponse> uomList;	
	
	private List<ImplantIndentItemResponse> items;
	
	public ImplantIndentResponse() {
		super();
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

	public String getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(String indentNo) {
		this.indentNo = indentNo;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getIndentDate() {
		return indentDate;
	}

	public void setIndentDate(Date indentDate) {
		this.indentDate = indentDate;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
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

	public String getPatientDetail() {
		return patientDetail;
	}

	public void setPatientDetail(String patientDetail) {
		this.patientDetail = patientDetail;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorDetail() {
		return doctorDetail;
	}

	public void setDoctorDetail(String doctorDetail) {
		this.doctorDetail = doctorDetail;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Integer getApprovalStatusId() {
		return approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<ImplantIndentItemResponse> getItems() {
		return items;
	}

	public void setItems(List<ImplantIndentItemResponse> items) {
		this.items = items;
	}

	public ImplantIndentResponse(Integer id, String indentNo, Date indentDate, String visitType, String visitNo, String indentType,
			Integer indentTypeId,String priority,Integer priorityId,String requestedBy,String uhid, String patientDetail,
			String doctorDetail, Integer storeId, String store, String status,Integer statusId, String approvalStatus, Integer approvalStatusId)
	{
		super();
		this.id = id;
		this.indentNo = indentNo;
		this.indentDate = indentDate;
		this.visitType = visitType;
		this.visitNo = visitNo;
		this.indentType=indentType;
		this.indentTypeId=indentTypeId;
		this.priority=priority;
		this.priorityId=priorityId;
		this.requestedBy=requestedBy;
		this.uhid = uhid;
		this.patientDetail = patientDetail;
		this.doctorDetail = doctorDetail;
		this.storeId = storeId;
		this.store = store;
		this.status = status;
		this.statusId = statusId;
		this.approvalStatus = approvalStatus;
		this.approvalStatusId = approvalStatusId;
	}
	
	public ImplantIndentResponse(Integer id, String indentNo, Date indentDate, String indentType, Integer indentTypeId,
			String status, Integer statusId, String approvalStatus, Integer approvalStatusId, Integer priorityId,
			String priority, Boolean isImplant, Boolean isConsignmnt, Boolean isKit, String remark) {
		super();
		this.id = id;
		this.indentNo = indentNo;
		this.indentDate = indentDate;
		this.indentType = indentType;
		this.indentTypeId = indentTypeId;
		this.status = status;
		this.statusId = statusId;
		this.approvalStatus = approvalStatus;
		this.approvalStatusId = approvalStatusId;
		this.priorityId = priorityId;
		this.priority = priority;
		this.isImplant = isImplant;
		this.isConsignmnt = isConsignmnt;
		this.isKit = isKit;
		this.remark = remark;
	}
	public ImplantIndentResponse(Integer id, String indentNo, Date indentDate, String indentType, Integer indentTypeId,
			String status, Integer statusId, String approvalStatus, Integer approvalStatusId, Integer priorityId,
			String priority, Boolean isImplant, Boolean isConsignmnt, Boolean isKit, String remark,
			Date issueDate,String issueNo,Integer storeId,String store) {
		super();
		this.id = id;
		this.indentNo = indentNo;
		this.indentDate = indentDate;
		this.indentType = indentType;
		this.indentTypeId = indentTypeId;
		this.status = status;
		this.statusId = statusId;
		this.approvalStatus = approvalStatus;
		this.approvalStatusId = approvalStatusId;
		this.priorityId = priorityId;
		this.priority = priority;
		this.isImplant = isImplant;
		this.isConsignmnt = isConsignmnt;
		this.isKit = isKit;
		this.remark = remark;
		this.issueDate=issueDate;
		this.issueNo=issueNo;
		this.storeId=storeId;
		this.store=store;
	}

	public ImplantIndentResponse(Integer id,String indentType,Integer indentTypeId,String indentNo, Date indentDate, String visitType,Integer visitedTypeId,
			String visitNo,String uhid,String admissionNumber, Integer patientId, String patientName, Integer doctorId, Integer storeId, String store,
			String status, Integer statusId, String approvalStatus, Integer approvalStatusId, Integer priorityId,
			String priority,Boolean isImplant,Boolean isConsignmnt,Boolean isKit,String remark) {
		super();
		this.id = id;
		this.indentType=indentType;
		this.indentTypeId=indentTypeId;
		this.indentNo = indentNo;
		this.indentDate = indentDate;
		this.visitType = visitType;
		this.visitedTypeId=visitedTypeId;
		this.visitNo = visitNo;
		this.uhid = uhid;
		this.admissionNumber=admissionNumber;
		this.patientId = patientId;
		this.patientName = patientName;
		this.doctorId = doctorId;
		this.storeId = storeId;
		this.store = store;
		this.status = status;
		this.statusId = statusId;
		this.approvalStatus = approvalStatus;
		this.approvalStatusId = approvalStatusId;
		this.priorityId = priorityId;
		this.priority = priority;
		this.isImplant=isImplant;
		this.isConsignmnt=isConsignmnt;
		this.isKit=isKit;
		this.remark = remark;
	}
	public ImplantIndentResponse(Integer id,String indentType,Integer indentTypeId,String indentNo, Date indentDate, String visitType,Integer visitedTypeId,
			String visitNo,String uhid, Integer patientId, String patientName, Integer doctorId, Integer storeId, String store,
			String status, Integer statusId, String approvalStatus, Integer approvalStatusId, Integer priorityId,
			String priority,Boolean isImplant,Boolean isConsignmnt,Boolean isKit,String remark) {
		super();
		this.id = id;
		this.indentType=indentType;
		this.indentTypeId=indentTypeId;
		this.indentNo = indentNo;
		this.indentDate = indentDate;
		this.visitType = visitType;
		this.visitedTypeId=visitedTypeId;
		this.visitNo = visitNo;
		this.uhid = uhid;
		this.admissionNumber=admissionNumber;
		this.patientId = patientId;
		this.patientName = patientName;
		this.doctorId = doctorId;
		this.storeId = storeId;
		this.store = store;
		this.status = status;
		this.statusId = statusId;
		this.approvalStatus = approvalStatus;
		this.approvalStatusId = approvalStatusId;
		this.priorityId = priorityId;
		this.priority = priority;
		this.isImplant=isImplant;
		this.isConsignmnt=isConsignmnt;
		this.isKit=isKit;
		this.remark = remark;
	}
	

	public String getIndentType() {
		return indentType;
	}

	public void setIndentType(String indentType) {
		this.indentType = indentType;
	}

	public Integer getIndentTypeId() {
		return indentTypeId;
	}

	public void setIndentTypeId(Integer indentTypeId) {
		this.indentTypeId = indentTypeId;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public Integer getVisitedTypeId() {
		return visitedTypeId;
	}

	public void setVisitedTypeId(Integer visitedTypeId) {
		this.visitedTypeId = visitedTypeId;
	}

	public Boolean getIsImplant() {
		return isImplant;
	}

	public void setIsImplant(Boolean isImplant) {
		this.isImplant = isImplant;
	}

	public Boolean getIsConsignmnt() {
		return isConsignmnt;
	}

	public void setIsConsignmnt(Boolean isConsignmnt) {
		this.isConsignmnt = isConsignmnt;
	}

	public Boolean getIsKit() {
		return isKit;
	}

	public void setIsKit(Boolean isKit) {
		this.isKit = isKit;
	}

	public String getAdmissionNumber() {
		return admissionNumber;
	}

	public void setAdmissionNumber(String admissionNumber) {
		this.admissionNumber = admissionNumber;
	}

	public List<UomResponse> getUomList() {
		return uomList;
	}

	public void setUomList(List<UomResponse> uomList) {
		this.uomList = uomList;
	}

	
	public void addItems(ImplantIndentItemResponse item) {
		if (this.items == null)
			this.items = new ArrayList<ImplantIndentItemResponse>();

		this.items.add(item);
	}
	
}

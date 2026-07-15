package com.param.billing.global.transaction.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.DateConverter;
@NamedNativeQueries({
@NamedNativeQuery(name="UPDATE_AUTHORIZATION_DETAILS_BY_PREAUTH_REQUEST_ID",
query="update"
		+ "   billing.m_preauth_request_master preauth"
		+ " set"
		+ "   authorization_no = :authorizationNo,"
		+ "    authorization_amount = :authorizationAmount,"
		+ "    authorization_date = :authorizationDate,"
		+ "    received_through = :receivedThrough,"
		+ "    received_by = :receivedBy,"
		+ "    updated_by = :updatedBy,"
		+ "    updated_date = :updatedDate,"
		+ "    auth_status = :authStatusId,"
		+ "    is_final_submit = :isFinalSubmit,"
		+ "    auth_remark = :authRemark"
		+ " where"
		+ "   preauth.preauth_request_id = :preauthRequestId"
		+ "   and preauth.organisation_id = :organisationId"
		+ "   and preauth.unit_id = :unitId"
),

@NamedNativeQuery(name="GET_AUTHORIZATION_DETAILS_BY_PAYEE_ID",
 query="SELECT"
		+ "   mprm.preauth_request_id as \"preauthRequestId\","
		+ "   mprm.request_no as \"requestNo\","
		+ "   mprm.request_type_id as \"requestTypeId\","
		+ "   reqmst.request_type_desc as \"requestTypeName\","
		+ "   sendthmst.send_throug_desc as \"requestSendThroughName\","
		+ "   to_char(mprm.request_date, 'DD-MM-YYYY') as \"requestDate\","
		+ "   mprm.patient_id as \"patientId\","
		+ "   mprm.request_send_through as \"requestSendThrough\","
		+ "   mprm.request_amount as \"requestAmount\","
		+ "   mprm.authorization_no as \"authorizationNo\","
		+ "   to_char(mprm.authorization_date, 'DD-MM-YYYY') as \"authDate\","
		+ "   mprm.authorization_amount as \"authorizationAmount\","
		+ "   mprm.received_through as \"authRequestSendThroughId\","
		+ "   mprm.received_by as \"receivedby\","
		+ "   receivedmst.send_throug_desc as \"authRequestSendThroughName\","
		+ "   concat(coalesce(pre.prefix_code,''),' ',coalesce(pat.first_name) ,' ', coalesce(pat.middle_name) ,' ', coalesce(pat.last_name)) as \"patientName\", "
		+ "   concat(coalesce(pat.first_name) ,' ', coalesce(pat.middle_name) ,' ', coalesce(pat.last_name)) as \"requestSendByName\", "
		+ "    pat.uhid_number as \"uhidNumber\","
		+ "    gender.gender_code as \"genderCode\","
		+ "    gender.gender_name as \"genderDec\","
		+ "    mprm.auth_status \"authorizationStatus\"," 
		+ "    mprm.is_final_submit \"isFinalSubmit\"," 
		+ "    mprm.request_remark \"requestRemark\"," 
		+ "    mprm.auth_remark \"authRemark\"," 
		+ "    usermst.user_name \"receivedByName\"," 
		+ "    authstatusdmst.auth_status_desc as \"authorizationStatusName\" "  
		+ " FROM"
		+ "   billing.m_preauth_request_master mprm"
		+ "   inner join billing.m_request_type_master reqmst on reqmst.request_type_id = mprm.request_type_id"
		+ "   left join lab.m_send_throgh_master sendthmst on sendthmst.send_through_id = mprm.request_send_through"
		+ "   left join lab.m_send_throgh_master receivedmst on receivedmst.send_through_id = CAST(mprm.received_through AS INTEGER)"
		+"    left join patient.t_patient_registration pat on pat.patient_id = mprm.patient_id "
		+"	  left join public.m_gender_master gender on gender.gender_id= pat.gender_id "
		+"	  left join m_prefix_master pre on pre.prefix_id = pat.prefix_id "
		+ "   left join billing.m_authorization_status_master authstatusdmst on authstatusdmst.auth_status_id  = mprm.auth_status"
		+ "   left join public.m_user_master usermst on usermst.user_id  = mprm.received_by"
		+ " WHERE"
		+ "  mprm.payee_id =:payeeId "
		+ "  and mprm.organisation_id =:organisationId "
		+ "  and mprm.unit_id=:unitId"
		+ "  and mprm.patient_id=:patientId"
		
)
})
@Entity
@Table(name = "m_preauth_request_master", schema = "billing")
@SequenceGenerator(name = "m_preauth_request_master_seq", sequenceName = "billing.m_preauth_request_master_seq", allocationSize = 1)
public class MPreauthRequestMaster {
	@Id
	@Column(name = "preauth_request_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_preauth_request_master_seq")
	private Integer preauthRequestId;

	@Column(name = "patient_id")
	private Integer patientId;

	@Column(name = "visit_type")
	private Integer visitType;

	@Column(name = "payee_id")
	private Integer payeeId;
	
	@Column(name = "request_no")
	private String requestNo;
	
	@Column(name = "request_type_id")
	private Integer requestTypeId;
	
	@Column(name = "request_date")
	private Date requesDate;

	@Column(name = "request_send_by")
	private Integer requestSendBy;
	
	@Column(name = "request_send_through")
	private Integer requestSendThrough;
	
	@Column(name = "request_amount")
	private Double requestAmount;
		
	@Column(name = "authorization_amount")
	private Double authorizationAmount;
	
	@Column(name = "utilized_amount")
	private Double utilizedAmount;
	
	@Column(name = "authorization_no")
	private String authorizationNo;

	@Column(name = "authorization_date")
	private Date authorizationDate;

	@Column(name = "received_through")
	private Integer receivedThrough;
	
	@Column(name = "request_remark")
	private String requestRemark;
	
	@Column(name = "auth_remark")
	private String authRemark;

	@Column(name = "received_by")
	private Integer receivedBy;
	
	@Column(name = "patient_sponsor_id")
	private Integer patientSponsorId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organisation_id")
	private Integer organisationId;

	@Column(name = "status")
	private Character status;
	
	@Column(name = "is_split_status")
	private Character isSplitStatus;
	
	@Column(name = "is_auth_deleted")
	private Character isAuthDeleted;

	@Column(name = "auth_status")
	private Integer authStatusId;

	@Column(name = "created_date")
	@Convert(converter = DateConverter.class)
	private Long createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	@Convert(converter = DateConverter.class)
	private Long updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "is_final_submit")
	private Character isFinalSubmit;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "encounter_id")
	private Integer encounterId;
	
	@Column(name = "contract_id")
	private Integer contractId;
	
	@Column(name = "document_type_id")
	private Integer docTypeId;
	
	@Column(name = "document_name")
	private String documentName;
	
	public Double getUtilizedAmount() {
		return utilizedAmount;
	}

	public void setUtilizedAmount(Double utilizedAmount) {
		this.utilizedAmount = utilizedAmount;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getPreauthRequestId() {
		return preauthRequestId;
	}

	public void setPreauthRequestId(Integer preauthRequestId) {
		this.preauthRequestId = preauthRequestId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getVisitType() {
		return visitType;
	}

	public void setVisitType(Integer visitType) {
		this.visitType = visitType;
	}


	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public Integer getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(Integer requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public Date getRequesDate() {
		return requesDate;
	}

	public void setRequesDate(Date requesDate) {
		this.requesDate = requesDate;
	}

	public Integer getRequestSendBy() {
		return requestSendBy;
	}

	public void setRequestSendBy(Integer requestSendBy) {
		this.requestSendBy = requestSendBy;
	}

	public Integer getRequestSendThrough() {
		return requestSendThrough;
	}

	public void setRequestSendThrough(Integer requestSendThrough) {
		this.requestSendThrough = requestSendThrough;
	}

	public Double getRequestAmount() {
		return requestAmount;
	}

	public void setRequestAmount(Double requestAmount) {
		this.requestAmount = requestAmount;
	}

	public Double getAuthorizationAmount() {
		return authorizationAmount;
	}

	public void setAuthorizationAmount(Double authorizationAmount) {
		this.authorizationAmount = authorizationAmount;
	}

	public String getAuthorizationNo() {
		return authorizationNo;
	}

	public void setAuthorizationNo(String authorizationNo) {
		this.authorizationNo = authorizationNo;
	}

	public Date getAuthorizationDate() {
		return authorizationDate;
	}

	public void setAuthorizationDate(Date authorizationDate) {
		this.authorizationDate = authorizationDate;
	}

	public Integer getReceivedThrough() {
		return receivedThrough;
	}

	public void setReceivedThrough(Integer receivedThrough) {
		this.receivedThrough = receivedThrough;
	}

	public String getRequestRemark() {
		return requestRemark;
	}

	public void setRequestRemark(String requestRemark) {
		this.requestRemark = requestRemark;
	}

	public String getAuthRemark() {
		return authRemark;
	}

	public void setAuthRemark(String authRemark) {
		this.authRemark = authRemark;
	}

	public Integer getReceivedBy() {
		return receivedBy;
	}

	public void setReceivedBy(Integer receivedBy) {
		this.receivedBy = receivedBy;
	}

	public Integer getPatientSponsorId() {
		return patientSponsorId;
	}

	public void setPatientSponsorId(Integer patientSponsorId) {
		this.patientSponsorId = patientSponsorId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Character getIsAuthDeleted() {
		return isAuthDeleted;
	}

	public void setIsAuthDeleted(Character isAuthDeleted) {
		this.isAuthDeleted = isAuthDeleted;
	}

	public Integer getAuthStatusId() {
		return authStatusId;
	}

	public void setAuthStatusId(Integer authStatusId) {
		this.authStatusId = authStatusId;
	}

	public Character getIsFinalSubmit() {
		return isFinalSubmit;
	}

	public void setIsFinalSubmit(Character isFinalSubmit) {
		this.isFinalSubmit = isFinalSubmit;
	}

	public Character getIsSplitStatus() {
		return isSplitStatus;
	}

	public void setIsSplitStatus(Character isSplitStatus) {
		this.isSplitStatus = isSplitStatus;
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

	public Integer getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Integer docTypeId) {
		this.docTypeId = docTypeId;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	

}

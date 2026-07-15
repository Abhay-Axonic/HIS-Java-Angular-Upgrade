package com.param.qms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedNativeQueries({
	@NamedNativeQuery(name="GET_QMS_TOKEN_LIST",
			query="select logMstr.qms_counter_master_id as \"qmsCounterMasterId\",logMstr.qms_dept_master_id as \"qmsDeptMasterId\","
			+" tokenMstr.qms_kiosk_master_id as \"qmsKioskMasterId\",tokenMstr.qms_token_master_id as \"qmsTokenMasterId\","
			+" concat(tokenMstr.start_letter,'-',tokenMstr.token_number ) as \"tokenNumberWithStartLetter\",to_char(tokenMstr.served_time,'dd-mm-yyyy') as \"servedTime\","
			+ " tokenMstr.is_cancel as \"isCancel\",tokenMstr.start_letter as \"startLetter\",tokenMstr.token_number as  \"tokenNumber\",tokenMstr.is_transfer as \"isTransfer\",logMstr.call_log_status as \"callLogStatus\","
			+ "logMstr.qms_token_call_log_id as \"qmsTokenCallLogId\", logMstr.status as \"status\" ,tokenMstr.is_call as \"isCall\",tokenMstr.organisation_id as \"organisationId\",tokenMstr.unit_id as \"unitId\" "
			+" from pharmacy.m_qms_token_master tokenMstr "
			+ "inner join pharmacy.t_qms_token_call_log logMstr on tokenMstr.qms_token_master_id=logMstr.qms_token_master_id "
			+ "inner join pharmacy.t_qms_token_sequence seqMstr on tokenMstr.qms_token_master_id=seqMstr.qms_token_master_id "
			+" where tokenMstr.unit_id=:unitId"
			+"  and tokenMstr.organisation_id=:orgId"
			+"  and tokenMstr.qms_kiosk_master_id=:kioskId"
			+"  and logMstr.qms_dept_master_id=:deptId "
			+ " and seqMstr.qms_dept_master_id=:deptId"
			+ " and seqMstr.status='A' "
			+ " and logMstr.status = 'A' "
			+ " and to_char(logMstr.created_date,'YYYY-MM-DD')=:createdDate"
			//+ " order by logMstr.qms_token_call_log_id asc" 
			+ " order by seqMstr.qms_token_seq_id asc"
			
//			+"  and tokenMstr.served_time is null"
			),
	@NamedNativeQuery(name="GET_TOKEN_LIST_FOR_DISPLAY",
	query="SELECT concat(tokenMstr.start_letter,'-',tokenMstr.token_number) as \"tokenNumberWithStartLetter\",  "
+"  	logMstr.qms_dept_master_id as \"assignedDeptId\",tokenMstr.qms_kiosk_master_id as \"qmsKioskMasterId\","
+ "tokenMstr.is_call as \"isCall\" ,"
+ " logMstr.call_log_status as \"callLogStatus\","
+ "mqcm.qms_counter_desc as \"qmsCounterDesc\"  "
+"  	FROM pharmacy.m_qms_token_master tokenMstr	 "
+"  	 inner join  pharmacy.t_qms_token_call_log logMstr on  tokenMstr.qms_token_master_id=logMstr.qms_token_master_id"
+ " left join  pharmacy.m_qms_counter_master mqcm on mqcm.qms_counter_master_id = logMstr.qms_counter_master_id  "
+"  	WHERE   "
+"  	  tokenMstr.qms_kiosk_master_id=:kioskId  "
+"  	AND tokenMstr.unit_id=:unitId  "
+"  	AND tokenMstr.organisation_id=:orgId  "
+"  	AND to_char(logMstr.created_date,'YYYY-MM-DD')=:createdDate  "
+"  	and  logMstr.status='A'  "
+ "      and logMstr.call_log_status in (0,1) order by logMstr.qms_token_call_log_id  asc "
//+"  	and logMstr.call_log_status=1 "
	),
	@NamedNativeQuery(name="GET_TOKEN_DISPLAY_MESSAGE",
		query="select msg.broadcast_qms_msg_id as \"broadcastQmsMsgId\",msg.qms_kiosk_master_id as \"qmsKioskMasterId\", "
		+" msg.qms_dept_master_id as \"qmsDeptMasterId\",msg.message as \"qmsCallMassage\" "
		+" FROM pharmacy.t_broadcast_qms_msg msg "
		+" WHERE msg.organisation_id=:orgId "
		+" AND msg.unit_id=:unitId "
		+" AND msg.qms_kiosk_master_id=:kioskId "
		+ " AND msg.status='A' "
	),
	@NamedNativeQuery(name="GET_TOKEN_DETAILS_BY_TOKENID",
	query="	select mstr.qms_token_master_id as \"qmsTokenMasterId\",mstr.qms_kiosk_master_id as \"qmsKioskMasterId\",mstr.assigned_dept_id as \"assignedDeptId\", "
			+" 	mstr.token_number as \"tokenNumber\" ,mstr.sr_no as \"srNo\" "
			+" 	from  pharmacy.m_qms_token_master mstr "
			+" 	where mstr.qms_kiosk_master_id=:kioskId "
//			+" 	and mstr.assigned_dept_id=:deptId "
			+" 	and mstr.token_number=:tokenNumber "
			+" 	and to_char(mstr.created_date,'YYYY-MM-DD')=:createdDate"
	),
	@NamedNativeQuery(name="CHECKING_IS_CALLED_OR_NOT",
	query="	select "
+" 	logMstr.qms_counter_master_id as \"qmsCounterMasterId\", "
+" 	logMstr.call_log_status as \"callLogStatus\", "
+" 	tokenMstr.qms_dept_master_id as \"qmsDeptMasterId\", "
+" 	tokenMstr.qms_kiosk_master_id as \"qmsKioskMasterId\", "
+" 	tokenMstr.qms_token_master_id as \"qmsTokenMasterId\", "
+" 	concat( tokenMstr.start_letter, '-', tokenMstr.token_number ) as \"tokenNumberWithStartLetter\", "
+" 	tokenMstr.start_letter as \"startLetter\", "
+" 	tokenMstr.token_number as \"tokenNumber\",	 "
+" 	logMstr.qms_token_call_log_id as \"qmsTokenCallLogId\" "
+" 	 "
+" from "
+" 	pharmacy.m_qms_token_master tokenMstr "
+" inner join pharmacy.t_qms_token_call_log logMstr on "
+" 	tokenMstr.qms_token_master_id = logMstr.qms_token_master_id "
+" inner join pharmacy.t_qms_token_sequence seqMstr on "
+" 	tokenMstr.qms_token_master_id = seqMstr.qms_token_master_id "
+" where "
+" 	tokenMstr.unit_id =:unitId "
+" 	and tokenMstr.organisation_id =:orgId "
+" 	and tokenMstr.qms_kiosk_master_id =:kioskId "
+" 	and logMstr.qms_dept_master_id =:deptId "
+" 	and seqMstr.qms_dept_master_id =:deptId "
+" 	and seqMstr.status = 'A' "
+" 	and logMstr.status='A' "
+" 	and logMstr.qms_token_master_id=:tokenId "
+" 	and to_char( logMstr.created_date, "
+" 	'YYYY-MM-DD' )=:createdDate "
+" order by "
+" 	seqMstr.qms_token_seq_id asc; "
	)
})
@Entity
@Table(name="m_qms_token_master",schema="pharmacy")
@SequenceGenerator(name = "m_qms_token_master_seq",sequenceName="pharmacy.m_qms_token_master_seq",allocationSize=1)
public class QMSTokenMaster {

	@Id
	@Column(name="qms_token_master_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="m_qms_token_master_seq")
	private Integer qmsTokenMasterId ;
	
	@Column(name="qms_kiosk_master_id")
	private Integer qmsKioskMasterId;
	
	@Column(name="qms_counter_master_id")
	private Integer qmsCounterMasterId;
	
	@Column(name="qms_dept_master_id")
	private Integer qmsDeptMasterId;
	
	@Column(name="token_number")
	private Integer tokenNumber;
	
	@Column(name="dummy_token_number")
	private Integer dummyTokenNumber ;
	
	@Column(name="call_count")
	private Integer callCount ;
	
	@Column(name="start_letter")
	private String startLetter;
	
	@Column(name = "status")
	private Character status;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name="organisation_id")
	private Integer organisationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="call_time")
	private Date callTime;
	
	@Column(name="served_time")
	private Date servedTime;
	
	@Column(name="is_cancel")
	private Character isCancel;
	
	@Column(name="is_transfer")
	private Character isTransfer;
	
	@Column(name="is_call")
	private Character isCall;
	
	@Column(name="assigned_dept_id")
	private Integer assignedDeptId;
	
	@Column(name="sr_no")
	private Integer srNo;
	

	public Integer getQmsTokenMasterId() {
		return qmsTokenMasterId;
	}

	public void setQmsTokenMasterId(Integer qmsTokenMasterId) {
		this.qmsTokenMasterId = qmsTokenMasterId;
	}

	public Integer getQmsKioskMasterId() {
		return qmsKioskMasterId;
	}

	public void setQmsKioskMasterId(Integer qmsKioskMasterId) {
		this.qmsKioskMasterId = qmsKioskMasterId;
	}

	public Integer getQmsCounterMasterId() {
		return qmsCounterMasterId;
	}

	public void setQmsCounterMasterId(Integer qmsCounterMasterId) {
		this.qmsCounterMasterId = qmsCounterMasterId;
	}

	public Integer getQmsDeptMasterId() {
		return qmsDeptMasterId;
	}

	public void setQmsDeptMasterId(Integer qmsDeptMasterId) {
		this.qmsDeptMasterId = qmsDeptMasterId;
	}

	public Integer getTokenNumber() {
		return tokenNumber;
	}

	public void setTokenNumber(Integer tokenNumber) {
		this.tokenNumber = tokenNumber;
	}

	public String getStartLetter() {
		return startLetter;
	}

	public void setStartLetter(String startLetter) {
		this.startLetter = startLetter;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Date getCallTime() {
		return callTime;
	}

	public void setCallTime(Date callTime) {
		this.callTime = callTime;
	}

	public Date getServedTime() {
		return servedTime;
	}

	public void setServedTime(Date servedTime) {
		this.servedTime = servedTime;
	}

	public Character getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(Character isCancel) {
		this.isCancel = isCancel;
	}

	public Character getIsTransfer() {
		return isTransfer;
	}

	public void setIsTransfer(Character isTransfer) {
		this.isTransfer = isTransfer;
	}

	public Integer getDummyTokenNumber() {
		return dummyTokenNumber;
	}

	public void setDummyTokenNumber(Integer dummyTokenNumber) {
		this.dummyTokenNumber = dummyTokenNumber;
	}

	public Integer getCallCount() {
		return callCount;
	}

	public void setCallCount(Integer callCount) {
		this.callCount = callCount;
	}

	public Character getIsCall() {
		return isCall;
	}

	public void setIsCall(Character isCall) {
		this.isCall = isCall;
	}

	public Integer getAssignedDeptId() {
		return assignedDeptId;
	}

	public void setAssignedDeptId(Integer assignedDeptId) {
		this.assignedDeptId = assignedDeptId;
	}

	public Integer getSrNo() {
		return srNo;
	}

	public void setSrNo(Integer srNo) {
		this.srNo = srNo;
	}	
		
}



package com.param.CSSD.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "t_sterilization_request_transition_mapper", schema = "cssd")
@SequenceGenerator(name = "t_sterilization_request_transition_mapper_id_seq", sequenceName = "cssd.t_sterilization_request_transition_mapper_id_seq", allocationSize = 1)
public class SterilizationRequestTransitionMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_sterilization_request_transition_mapper_id_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "request_no")
	private Integer requestNo;
	
	@Column(name = "transition_number")
	private Integer transitionNumber;
	
	@Column(name = "status")
	private String status;

	@Column(name = "reject_reason_id")
	private Integer rejectReasonId;
	
	@Column(name = "reject_reason_note")
	private String rejectReasonNote;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transition_number", insertable = false, updatable = false)
	private PreSterilizationProcess preSterilizationProcess;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "request_no", insertable = false, updatable = false)
	private SterilizationRequest  sterilizationRequest;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(Integer requestNo) {
		this.requestNo = requestNo;
	}

	public Integer getTransitionNumber() {
		return transitionNumber;
	}

	public void setTransitionNumber(Integer transitionNumber) {
		this.transitionNumber = transitionNumber;
	}

	public PreSterilizationProcess getPreSterilizationProcess() {
		return preSterilizationProcess;
	}

	public void setPreSterilizationProcess(PreSterilizationProcess preSterilizationProcess) {
		this.preSterilizationProcess = preSterilizationProcess;
	}

	public SterilizationRequest getSterilizationRequest() {
		return sterilizationRequest;
	}

	public void setSterilizationRequest(SterilizationRequest sterilizationRequest) {
		this.sterilizationRequest = sterilizationRequest;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public String getRejectReasonNote() {
		return rejectReasonNote;
	}

	public void setRejectReasonNote(String rejectReasonNote) {
		this.rejectReasonNote = rejectReasonNote;
	}
	
	
	
}


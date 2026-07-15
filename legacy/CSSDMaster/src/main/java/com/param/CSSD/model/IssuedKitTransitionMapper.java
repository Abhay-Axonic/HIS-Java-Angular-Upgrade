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

import com.param.CSSDMaster.model.KitMaster;


@Entity
@Table(name = "t_issued_kit_transition_mapper", schema = "cssd")
@SequenceGenerator(name = "t_issued_kit_transition_mapper_id_seq", sequenceName = "cssd.t_issued_kit_transition_mapper_id_seq", allocationSize = 1)
public class IssuedKitTransitionMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_issued_kit_transition_mapper_id_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "issue_no")
	private Integer issueNo;
	
	@Column(name = "kit_id")
	private Integer kitId;
	
	@Column(name = "transition_number")
	private Integer transitionNumber;
	
	@Column(name = "is_kit_used")
	private Character isKitUsed;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transtion_number", insertable = false, updatable = false)
	private PreSterilizationProcess preSterilizationProcess;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_no", insertable = false, updatable = false)
	private KitIssue  kitIssue;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kit_id", insertable = false, updatable = false)
	private KitMaster  kitMaster;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(Integer issueNo) {
		this.issueNo = issueNo;
	}

	public Integer getKitId() {
		return kitId;
	}

	public void setKitId(Integer kitId) {
		this.kitId = kitId;
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

	public KitIssue getKitIssue() {
		return kitIssue;
	}

	public void setKitIssue(KitIssue kitIssue) {
		this.kitIssue = kitIssue;
	}

	public KitMaster getKitMaster() {
		return kitMaster;
	}

	public void setKitMaster(KitMaster kitMaster) {
		this.kitMaster = kitMaster;
	}

	public Character getIsKitUsed() {
		return isKitUsed;
	}

	public void setIsKitUsed(Character isKitUsed) {
		this.isKitUsed = isKitUsed;
	}

	
		
}

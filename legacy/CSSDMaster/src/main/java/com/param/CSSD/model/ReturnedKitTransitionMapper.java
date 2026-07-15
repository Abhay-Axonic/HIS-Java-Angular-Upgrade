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
@Table(name = "t_returned_kit_transition_mapper", schema = "cssd")
@SequenceGenerator(name = "t_returned_kit_transition_mapper_id_seq", sequenceName = "cssd.t_returned_kit_transition_mapper_id_seq", allocationSize = 1)
public class ReturnedKitTransitionMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_returned_kit_transition_mapper_id_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "return_no")
	private Integer returnNo;
	
	@Column(name = "kit_id")
	private Integer kitId;
	
	@Column(name = "transition_number")
	private Integer transitionNumber;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transtion_number", insertable = false, updatable = false)
	private PreSterilizationProcess preSterilizationProcess;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "return_no", insertable = false, updatable = false)
	private KitReturn  kitReturn;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kit_id", insertable = false, updatable = false)
	private KitMaster  kitMaster;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(Integer returnNo) {
		this.returnNo = returnNo;
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

	public KitReturn getKitReturn() {
		return kitReturn;
	}

	public void setKitReturn(KitReturn kitReturn) {
		this.kitReturn = kitReturn;
	}

	public KitMaster getKitMaster() {
		return kitMaster;
	}

	public void setKitMaster(KitMaster kitMaster) {
		this.kitMaster = kitMaster;
	}

	
}

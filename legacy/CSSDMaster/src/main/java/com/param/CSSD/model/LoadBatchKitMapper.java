package com.param.CSSD.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({

		@NamedQuery(name = "GET_KIT_LIST_FOR_KIT_ISSUE", query = "SELECT kitMaster.kitId as kitId, "
				+ "kitMaster.code as kitCode,kitMaster.name as kitName, loadBatch.batchNumber as batchNo, "
				+ "loadBatch.autoclaveNumber as autoclaveNumber,to_char(loadBatch.autoclaveDate,'dd-MM-yyyy') as autoclaveDate, "
				+ "to_char(autoclaveMaster.updated_date + autoclaveMaster.biological_indicator_day,'dd-MM-yyyy') as expiryDate  "
				+ "FROM LoadBatchKitMapper mapper "
				+ "INNER JOIN mapper.preSterilizationProcess as preSterilizationProcess "
				+ "INNER JOIN mapper.loadBatch as loadBatch "
				+ "INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
				+ "INNER JOIN loadBatch.autoclaveMaster as autoclaveMaster " + "WHERE loadBatch.status='sterilized'"),

		@NamedQuery(name = "GET_KIT_LIST_BY_BATCH_NO", query = "SELECT kitMaster.kitId as kitId,"
				+ "kitMaster.code as kitCode,kitMaster.name as kitName, wrapperExpiryMaster.name as wrapperName,"
				+ "to_char(preSterilizationProcess.formation_date,'dd-MM-yyyy') as date,"
				+ "preSterilizationProcess.status as status FROM LoadBatchKitMapper mapper "
				+ "INNER JOIN mapper.preSterilizationProcess as preSterilizationProcess "
				+ "INNER JOIN preSterilizationProcess.kitMaster as kitMaster "
				+ "INNER JOIN preSterilizationProcess.wrapperExpiryMaster as wrapperExpiryMaster "
				+ "WHERE mapper.batchNumber=:batchNo") })

@Entity
@Table(name = "t_load_batch_kit_mapper", schema = "cssd")
@SequenceGenerator(name = "t_load_batch_kit_mapper_id_seq", sequenceName = "cssd.t_load_batch_kit_mapper_id_seq", allocationSize = 1)
public class LoadBatchKitMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_load_batch_kit_mapper_id_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "load_batch_id")
	private Integer batchNumber;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "load_batch_id", insertable = false, updatable = false)
	private LoadBatch  loadBatch;
	
	@Column(name = "transition_number")
	private Integer transitionNumber;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transition_number", insertable = false, updatable = false)
	private PreSterilizationProcess  preSterilizationProcess;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(Integer batchNumber) {
		this.batchNumber = batchNumber;
	}

	public LoadBatch getLoadBatch() {
		return loadBatch;
	}

	public void setLoadBatch(LoadBatch loadBatch) {
		this.loadBatch = loadBatch;
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
	
	
}

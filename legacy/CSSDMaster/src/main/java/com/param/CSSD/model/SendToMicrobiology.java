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
@Table(name = "t_send_to_microbiology", schema = "cssd")
@SequenceGenerator(name = "t_send_to_microbiology_id_seq", sequenceName = "cssd.t_send_to_microbiology_id_seq", allocationSize = 1)
public class SendToMicrobiology {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_send_to_microbiology_id_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "batch_no")
	private Integer batchNumber;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_number", insertable = false, updatable = false)
	private LoadBatch  loadBatch;
	
	@Column(name = "status")
	private String status;
	
	
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}

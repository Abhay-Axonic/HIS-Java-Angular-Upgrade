package com.param.opd.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/*
 * 
 * Created by Tushar;
 */
@Entity
@Table(name="m_reason_for_admission",schema="opd")
@SequenceGenerator(name = "m_seq_reason_for_admission", sequenceName = "opd.m_seq_reason_for_admission", allocationSize = 1)
public class ReasonForAdmissionMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_seq_reason_for_admission")
	@Column(name = "reason_id")
	private int reason_id;
	
	@Column(name = "reason_for_admission")
	private String reason_for_admission;

	public int getReason_id() {
		return reason_id;
	}

	public void setReason_id(int reason_id) {
		this.reason_id = reason_id;
	}

	public String getReason_for_admission() {
		return reason_for_admission;
	}

	public void setReason_for_admission(String reason_for_admission) {
		this.reason_for_admission = reason_for_admission;
	}

}

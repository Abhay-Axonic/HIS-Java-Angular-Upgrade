package com.param.opd.model;

import java.util.List;

/*
 * Created by Tushar
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.model.master.Item;
import com.param.entity.model.opd.MedicationHistory;

@Entity
@Table(name="m_frequency_master",schema="opd")
@SequenceGenerator(name = "m_seq_frequency_master", sequenceName = "opd.m_seq_frequency_master", allocationSize = 1)
public class FrequencyMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_seq_frequency_master")
	@Column(name = "frequency_id", unique = true, nullable = false)
	private int frequency_id;
	
	@Column(name = "frequency", nullable = false)
	private String frequency;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "frequencyMaster")
	private List<MedicationHistory> frequencyMasterList;

	public int getFrequency_id() {
		return frequency_id;
	}

	public void setFrequency_id(int frequency_id) {
		this.frequency_id = frequency_id;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	

}

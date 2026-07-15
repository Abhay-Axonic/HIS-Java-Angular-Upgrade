package com.param.global.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="t_vital_details",schema="public")
@SequenceGenerator(name="seq_t_vital_details",sequenceName="public.seq_t_vital_details",allocationSize=1)
public class TVitalDetails {
	
	@Id
	@Column(name="t_vital_details_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_t_vital_details")
	private int tVitalDetailsId;
	
	@Column(name="t_vital_id")
	private int tVitalId;

	@Column(name="vital_id")
	private Integer vitalId;
	
	@Column(name="observation")
	private String observation;
	
	@Column(name="type_id")
	private Integer typeId;


	public int gettVitalId() {
		return tVitalId;
	}

	public void settVitalId(int tVitalId) {
		this.tVitalId = tVitalId;
	}

	public Integer getVitalId() {
		return vitalId;
	}

	public void setVitalId(Integer vitalId) {
		this.vitalId = vitalId;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public int gettVitalDetailsId() {
		return tVitalDetailsId;
	}

	public void settVitalDetailsId(int tVitalDetailsId) {
		this.tVitalDetailsId = tVitalDetailsId;
	}
	
	
	
}

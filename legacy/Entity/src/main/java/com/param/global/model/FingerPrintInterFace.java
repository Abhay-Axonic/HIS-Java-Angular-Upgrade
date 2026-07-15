package com.param.global.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_fingerprint_interface", schema = "patient")
/*@SequenceGenerator(name = "seq_t_fingerprint_interface", sequenceName = "patient.seq_t_fingerprint_interface", allocationSize = 1)*/
public class FingerPrintInterFace {
	
	@Id
	@Column(name = "uuid")
	/*@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_t_fingerprint_interface")*/
	private int uuid;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "thumb_image")
	private String thumbImage;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "organisation_id")
	private Integer orgnisationId;
	
	@Column(name = "unit_id")
	private Integer unitId;


	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}


	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getOrgnisationId() {
		return orgnisationId;
	}

	public void setOrgnisationId(Integer orgnisationId) {
		this.orgnisationId = orgnisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public int getUuid() {
		return uuid;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public String getThumbImage() {
		return thumbImage;
	}

	public void setThumbImage(String thumbImage) {
		this.thumbImage = thumbImage;
	}
	
	

}

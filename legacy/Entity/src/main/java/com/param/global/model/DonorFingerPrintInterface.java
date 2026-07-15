package com.param.global.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_donor_fingerprint_interface", schema = "blood_bank")
public class DonorFingerPrintInterface {
	
	@Id
	@Column(name = "uuid")
	private int uuid;
	
	@Column(name = "donor_id")
	private Integer donorId;
	
	@Column(name = "thumb_image")
	private String thumbImage;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "organisation_id")
	private Integer orgnisationId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	public int getUuid() {
		return uuid;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public Integer getDonorId() {
		return donorId;
	}

	public void setDonorId(Integer donorId) {
		this.donorId = donorId;
	}

	public String getThumbImage() {
		return thumbImage;
	}

	public void setThumbImage(String thumbImage) {
		this.thumbImage = thumbImage;
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
	
	

}

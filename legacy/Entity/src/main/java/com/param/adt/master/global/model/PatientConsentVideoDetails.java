package com.param.adt.master.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@NamedNativeQueries({

@NamedNativeQuery(name="GET_CONSENT_VIDEO_LIST_BY_MAPPER_ID",
query="select dtls.video_details_id as \"videoDetailsId\" ," + 
		"dtls.video_name as \"videoName\", " + 
		"dtls.video_path  as \"videoPath\", " + 
		"to_char(dtls.created_date,'DD-MM-YYYY HH:mm:ss') as \"createdDate\"  " + 
		"from public.t_patient_consent_video_details dtls " + 
		"where dtls.consent_transaction_id = :consentTransactionId " + 
		"and dtls.organization_id = :orgId " + 
		"and dtls.unit_id = :unitId "
		+ "and dtls.status = 'A' "),
})

@Entity
@Table(name = "t_patient_consent_video_details" , schema = "public")
@SequenceGenerator(name = "t_patient_consent_video_details_seq" , sequenceName = "public.t_patient_consent_video_details_seq" , allocationSize = 1)
public class PatientConsentVideoDetails {
	
	@Id
	@Column(name = "video_details_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_patient_consent_video_details_seq")
	private Integer videoDetailsId;
	
	@Column(name="consent_transaction_id")
	private Integer consentTransactionId;

	@Column(name="video_path")
	private String videoPath;
	
	@Column(name="video_name")
	private String videoName;
	
	@Column(name = "status")
	private Character status;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;

	public Integer getVideoDetailsId() {
		return videoDetailsId;
	}

	public void setVideoDetailsId(Integer videoDetailsId) {
		this.videoDetailsId = videoDetailsId;
	}

	public Integer getConsentTransactionId() {
		return consentTransactionId;
	}

	public void setConsentTransactionId(Integer consentTransactionId) {
		this.consentTransactionId = consentTransactionId;
	}

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	
}

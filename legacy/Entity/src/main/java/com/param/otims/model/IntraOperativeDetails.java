package com.param.otims.model;

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

@Entity
@Table(name = "t_intra_operative_details" , schema = "otims")
@SequenceGenerator(name = "intra_operative_details_seq" , sequenceName = "otims.intra_operative_details_seq" , allocationSize = 1)

@NamedNativeQueries({
	
	@NamedNativeQuery(name = "GET_INTRA_OPERATIVE_DATA",
			query = " select intra_operative_id  as \"intraOperativeId\", "
					+ " who_checklist_status  as \"whoChecklistStatus\" "
					+ " from otims.t_intra_operative_details where pro_req_id = :proReqId  ")
	
	
})


public class IntraOperativeDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "intra_operative_details_seq")
	@Column(name = "intra_operative_id")
	private Integer intraOperativeId;
	
	@Column(name = "pro_req_id")
	private Integer proReqId;
	
	@Column(name = "video_url")
	private String videoUrl;
	
	@Column(name = "who_checklist_status")
	private Integer whoChecklistStatus;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;

	public Integer getIntraOperativeId() {
		return intraOperativeId;
	}

	public void setIntraOperativeId(Integer intraOperativeId) {
		this.intraOperativeId = intraOperativeId;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public Integer getWhoChecklistStatus() {
		return whoChecklistStatus;
	}

	public void setWhoChecklistStatus(Integer whoChecklistStatus) {
		this.whoChecklistStatus = whoChecklistStatus;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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
	
	
	
}

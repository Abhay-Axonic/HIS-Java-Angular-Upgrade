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


@NamedNativeQueries({
	
	@NamedNativeQuery(name="GET_NOTELIST_BY_PRO_REQ_AND_CONFIG_ID",
			query="SELECT tpcn.note_master_id AS \"noteMasterId\", " + 
					" tpcn.note_type_id AS \"noteTypeId\", " + 
					" tpcn.template AS \"template\", " + 
					" noteMstr.description AS \"description\" " + 
					" from otims.t_procedure_config_note tpcn  " + 
					" inner  join otims.t_procedure_configuration tpc on tpc.procedure_config_id  = tpcn.procedure_config_id and tpc.org_id  = :orgId and tpc.unit_id  = :unitId " + 
					" inner join otims.t_procedure_req_mst tprm   on tprm.primary_procedure_id  = tpc .service_id  "  
					+ " INNER JOIN otims.m_note_master noteMstr on noteMstr.note_master_id = tpcn.note_master_id "
					+ " WHERE tpcn.organization_id = :orgId "
					+ " AND tprm .pro_req_id  =  :proReqId  "
					+ " AND tpcn.unit_id= :unitId "
					+ " AND tpcn.note_type_id in (1)  "
					+ " AND tpcn.status = 'A' "
				),
	@NamedNativeQuery(name="GET_POST_CARE_DATA_BY_PRO_REQ_ID",
	query="select post.post_operative_care_id as \"postOperativeCareId\" " + 
			"from otims.t_post_operative_care  post " + 
			"where post.pro_req_id = :proReqId"
		)
	
})


@Entity
@Table(name = "t_post_operative_care" , schema = "otims")
@SequenceGenerator(name = "post_operative_care_seq" , sequenceName = "otims.post_operative_care_seq" , allocationSize = 1)
public class PostOperativeCare {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "post_operative_care_seq")
	@Column(name = "post_operative_care_id")
	private Integer postOperativeCareId;
	
	@Column(name = "pro_req_id")
	private Integer proReqId;
	
	@Column(name = "follow_up_note")
	private String followUpNote;
	
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

	public Integer getPostOperativeCareId() {
		return postOperativeCareId;
	}

	public void setPostOperativeCareId(Integer postOperativeCareId) {
		this.postOperativeCareId = postOperativeCareId;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public String getFollowUpNote() {
		return followUpNote;
	}

	public void setFollowUpNote(String followUpNote) {
		this.followUpNote = followUpNote;
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

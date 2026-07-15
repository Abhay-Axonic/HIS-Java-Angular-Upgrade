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

	  @NamedNativeQuery(name="GET_POSTOPERATIVE_DETAILS_BY_PRO_REQ_ID",
			query="  SELECT post_operative_id AS \"postOperativeId\", " + 
					" post_op_note  as \"postOpNote\"," + 
					" pro_req_id AS \"proReqId\" , " + 
					" created_by AS \"createdBy\" , " + 
					" status AS \"status\"," + 
					" to_char(created_date,'YYYY-MM-DD') as \"createdDate\", " + 
					" to_char(updated_date,'YYYY-MM-DD') as \"updatedDate\", " + 
					" updated_by AS \"updatedBy\", " + 
					" unit_id AS \"unitId\", " + 
					" organization_id AS \"organizationId\" " + 
					" FROM otims.t_post_operative_details " + 
					" WHERE pro_req_id = :proReqId  " +
					" order by post_operative_id desc limit 1" ),

})




@Entity
@Table(name = "t_post_operative_details" , schema = "otims")
@SequenceGenerator(name = "post_operative_details_seq" , sequenceName = "otims.post_operative_details_seq" , allocationSize = 1)
public class PostOperativeDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "post_operative_details_seq")
	@Column(name = "post_operative_id")
	private Integer postOperativeId;
	
	@Column(name = "pro_req_id")
	private Integer proReqId;
	
	@Column(name = "post_op_note")
	private String postOpNote;
	
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

	public Integer getPostOperativeId() {
		return postOperativeId;
	}

	public void setPostOperativeId(Integer postOperativeId) {
		this.postOperativeId = postOperativeId;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public String getPostOpNote() {
		return postOpNote;
	}

	public void setPostOpNote(String postOpNote) {
		this.postOpNote = postOpNote;
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

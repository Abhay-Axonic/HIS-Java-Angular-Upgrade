package com.param.CSSD.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.UserMaster;
import com.param.entity.model.master.Store;


@NamedQueries({

	
	
	})




@Entity
@Table(name = "t_sterilization_request", schema = "cssd")
@SequenceGenerator(name = "t_sterilization_request_id_seq", sequenceName = "cssd.t_sterilization_request_id_seq", allocationSize = 1)
public class SterilizationRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_sterilization_request_id_seq")
	@Column(name = "id")
	private Integer requestNo;

	@Column(name = "requesting_store_id")
	private Integer requestingStoreId;
	
	@Column(name = "issueing_store_id")
	private Integer issueingStoreId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "requesting_store_id", insertable = false, updatable = false)
	private Store requestingStore;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issueing_store_id", insertable = false, updatable = false)
	private Store issueingStore;
	
	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "created_by")
	private Integer createdBy;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", insertable = false, updatable = false)
	private UserMaster userCreatedBy;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "carrier")
	private String carrier;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "cancel_reason_id")
	private Integer cancelReasonId;
	
	@Column(name = "cancel_reason_note")
	private String cancelReasonNote;
	
	public Integer getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(Integer requestNo) {
		this.requestNo = requestNo;
	}

	public Integer getRequestingStoreId() {
		return requestingStoreId;
	}

	public void setRequestingStoreId(Integer requestingStoreId) {
		this.requestingStoreId = requestingStoreId;
	}

	public Integer getIssueingStoreId() {
		return issueingStoreId;
	}

	public void setIssueingStoreId(Integer issueingStoreId) {
		this.issueingStoreId = issueingStoreId;
	}

	public Store getRequestingStore() {
		return requestingStore;
	}

	public void setRequestingStore(Store requestingStore) {
		this.requestingStore = requestingStore;
	}

	public Store getIssueingStore() {
		return issueingStore;
	}

	public void setIssueingStore(Store issueingStore) {
		this.issueingStore = issueingStore;
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

	public UserMaster getUserCreatedBy() {
		return userCreatedBy;
	}

	public void setUserCreatedBy(UserMaster userCreatedBy) {
		this.userCreatedBy = userCreatedBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public String getCancelReasonNote() {
		return cancelReasonNote;
	}

	public void setCancelReasonNote(String cancelReasonNote) {
		this.cancelReasonNote = cancelReasonNote;
	}
	
	
	
}

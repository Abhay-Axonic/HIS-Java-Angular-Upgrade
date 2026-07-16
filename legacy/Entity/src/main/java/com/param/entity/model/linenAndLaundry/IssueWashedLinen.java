package com.param.entity.model.linenAndLaundry;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the t_issue_washed_linen database table.
 * 
 */
@Entity
@Table(name="t_issue_washed_linen", schema="linen_and_laundry")
public class IssueWashedLinen implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Column(name="accepted_by")
	private Integer acceptedBy;

	@Column(name="accepted_date")
	private Timestamp acceptedDate;

	@Column(name="added_by")
	private Integer addedBy;

	@Column(name="added_date")
	private Timestamp addedDate;

	@Column(name="from_store_id")
	private Integer fromStoreId;

	@Column(name="is_active")
	private Boolean isActive;

	@Column(name="is_deleted")
	private Boolean isDeleted;

	@Column(name="issue_no")
	private String issueNo;

	@Column(name="organization_id")
	private Integer organizationId;

	@Column(name="priority_id")
	private Integer priorityId;

	private String remark;

	@Column(name="status_id")
	private Integer statusId;

	@Column(name="to_store_id")
	private Integer toStoreId;

	@Column(name="unit_id")
	private Integer unitId;

	@Column(name="updated_by")
	private Integer updatedBy;

	@Column(name="updated_date")
	private Timestamp updatedDate;

	@ManyToOne
	@JoinColumn(name="issue_dirty_linen_id")
	private IssueDirtyLinen issueDirtyLinen;

	@OneToMany(mappedBy="issueWashedLinen", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<IssueWashedLinenDetail> issueWashedLinenDetails;

	public IssueWashedLinen() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAcceptedBy() {
		return this.acceptedBy;
	}

	public void setAcceptedBy(Integer acceptedBy) {
		this.acceptedBy = acceptedBy;
	}

	public Timestamp getAcceptedDate() {
		return this.acceptedDate;
	}

	public void setAcceptedDate(Timestamp acceptedDate) {
		this.acceptedDate = acceptedDate;
	}

	public Integer getAddedBy() {
		return this.addedBy;
	}

	public void setAddedBy(Integer addedBy) {
		this.addedBy = addedBy;
	}

	public Timestamp getAddedDate() {
		return this.addedDate;
	}

	public void setAddedDate(Timestamp addedDate) {
		this.addedDate = addedDate;
	}

	public Integer getFromStoreId() {
		return this.fromStoreId;
	}

	public void setFromStoreId(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getIssueNo() {
		return this.issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public Integer getOrganizationId() {
		return this.organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getPriorityId() {
		return this.priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatusId() {
		return this.statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getToStoreId() {
		return this.toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	public Integer getUnitId() {
		return this.unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public IssueDirtyLinen getIssueDirtyLinen() {
		return issueDirtyLinen;
	}

	public void setIssueDirtyLinen(IssueDirtyLinen issueDirtyLinen) {
		this.issueDirtyLinen = issueDirtyLinen;
	}

	public List<IssueWashedLinenDetail> getIssueWashedLinenDetails() {
		return issueWashedLinenDetails;
	}

	public void setIssueWashedLinenDetails(List<IssueWashedLinenDetail> issueWashedLinenDetails) {
		this.issueWashedLinenDetails = issueWashedLinenDetails;
	}

	public IssueWashedLinenDetail addIssueWashedLinenDetail(IssueWashedLinenDetail issueWashedLinenDetail) {
		getIssueWashedLinenDetails().add(issueWashedLinenDetail);
		issueWashedLinenDetail.setIssueWashedLinen(this);

		return issueWashedLinenDetail;
	}

	public IssueWashedLinenDetail removeIssueWashedLinenDetail(IssueWashedLinenDetail issueWashedLinenDetail) {
		getIssueWashedLinenDetails().remove(issueWashedLinenDetail);
		issueWashedLinenDetail.setIssueWashedLinen(null);

		return issueWashedLinenDetail;
	}

}
package com.param.entity.model.pharmacy;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Status;

@Entity(name = "ReturnOfEmptyAmpoulesDetail")
@Table(name = "t_return_of_empty_ampoules_detail", schema = "pharmacy")
public class ReturnOfEmptyAmpoulesDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "return_of_empty_ampoules_id")
	private ReturnOfEmptyAmpoules returnOfEmptyAmpoules;

	@Column(name = "return_qty")
	private Integer returnQuantity;

	@Column(name = "accept_quantity")
	private Integer acceptQuantity;
	
	@Column(name = "rejected_quantity")
	private Integer rejectedQuantity;
	
	public Integer getRejectedQuantity() {
		return rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
	}

	@Column(name = "returned_quantity")
	private Integer returnedQuantity;
	

	public Integer getReturnedQuantity() {
		return returnedQuantity;
	}

	public void setReturnedQuantity(Integer returnedQuantity) {
		this.returnedQuantity = returnedQuantity;
	}

	@Column(name = "issue_quantity")
	private Integer issueQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_batch_mapper_id")
	private IssueDetailBatchMapper issueBatchMapper;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ReturnOfEmptyAmpoules getReturnOfEmptyAmpoules() {
		return returnOfEmptyAmpoules;
	}

	public void setReturnOfEmptyAmpoules(ReturnOfEmptyAmpoules returnOfEmptyAmpoules) {
		this.returnOfEmptyAmpoules = returnOfEmptyAmpoules;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public IssueDetailBatchMapper getIssueBatchMapper() {
		return issueBatchMapper;
	}

	public void setIssueBatchMapper(IssueDetailBatchMapper issueBatchMapper) {
		this.issueBatchMapper = issueBatchMapper;
	}

	public Integer getAcceptQuantity() {
		return acceptQuantity;
	}

	public void setAcceptQuantity(Integer acceptQuantity) {
		this.acceptQuantity = acceptQuantity;
	}

	public Integer getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public ReturnOfEmptyAmpoulesDetail(Integer returnQuantity, IssueDetailBatchMapper issueBatchMapper) {
		super();
		this.returnQuantity = returnQuantity;
		this.issueBatchMapper = issueBatchMapper;
	}

	public ReturnOfEmptyAmpoulesDetail() {
		super();
	}

	public ReturnOfEmptyAmpoulesDetail(ReturnOfEmptyAmpoules returnOfEmptyAmpoules, Integer issueQuantity,
			Status status, IssueDetailBatchMapper issueBatchMapper) {
		super();
		this.returnOfEmptyAmpoules = returnOfEmptyAmpoules;
		this.issueQuantity = issueQuantity;
		this.status = status;
		this.issueBatchMapper = issueBatchMapper;
	}

	public void updateReturnOfEmptyAmpoulesDetail(Integer returnQuantity, Status status) {
		this.returnQuantity = returnQuantity;
		this.status = status;
	}
}
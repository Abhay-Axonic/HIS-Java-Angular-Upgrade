package com.param.entity.model.inventory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.RequestType;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.User;

/**
 * The persistent class for the t_material_inwards database table.
 * 
 */
@Entity
@Table(name = "t_drug_recall", schema = "inventory")
public class DrugRecall extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "recall_no")
	private String recallNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "request_type_id", nullable = false)
	private RequestType requestType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = true)
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;

	@Column(name = "cancel_note")
	private String cancelNote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	protected User approvedBy;

	@Column(name = "approved_on")
	private Date approvedDate;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "DrugRecall", cascade = CascadeType.ALL)
	private List<DrugRecallDetail> drugRecallDetailList;

	public DrugRecall() {
		super();
	}
	
	public DrugRecall(Integer id) {
		super();
		this.id = id;
	}

	public DrugRecall(String recallNo, RequestType requestTypeId, Status status) {
		this.recallNo = recallNo;
		this.requestType = requestTypeId;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRecallNo() {
		return recallNo;
	}

	public void setRecallNo(String recallNo) {
		this.recallNo = recallNo;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public CancelReason getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(CancelReason cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	public List<DrugRecallDetail> getDrugRecallDetailList() {
		return drugRecallDetailList;
	}

	public void setDrugRecallDetailList(List<DrugRecallDetail> drugRecallDetailList) {
		this.drugRecallDetailList = drugRecallDetailList;
	}

	public DrugRecallDetail addDrugRecallDetailList(DrugRecallDetail mrugRecallDetail) {
		if (getDrugRecallDetailList() == null)
			this.drugRecallDetailList = new ArrayList<DrugRecallDetail>();

		getDrugRecallDetailList().add(mrugRecallDetail);
		mrugRecallDetail.setDrugRecall(this);

		return mrugRecallDetail;
	}

}

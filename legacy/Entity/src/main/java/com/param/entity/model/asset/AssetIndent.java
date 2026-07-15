package com.param.entity.model.asset;

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

import com.param.entity.model.adt.Priority;
import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.inventory.IndentType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;

@Entity(name = "AssetIndent")
@Table(name = "t_asset_indent", schema = "assets")
public class AssetIndent extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "indent_no")
	private String indentNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_store_id", nullable = false)
	private Store fromStore;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_store_id", nullable = false)
	private Store toStore;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "indent_type_id", nullable = false)
	private IndentType indentType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "priority_id", nullable = false)
	private Priority priority;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approval_status_id", nullable = false)
	private Status approvalStatus;
	
	@Column(name = "remark")
	private String remark;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private User approvedBy;
	
	@Column(name = "approved_on")
	private Date appprovedOn;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;
	
	@Column(name = "cancel_note")
	private String cancelNote;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "indent", cascade = CascadeType.ALL)
	private List<AssetIndentDetail> assetIndentDetail;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(String indentNo) {
		this.indentNo = indentNo;
	}

	public Store getFromStore() {
		return fromStore;
	}

	public void setFromStore(Store fromStore) {
		this.fromStore = fromStore;
	}

	public Store getToStore() {
		return toStore;
	}

	public void setToStore(Store toStore) {
		this.toStore = toStore;
	}

	public IndentType getIndentType() {
		return indentType;
	}

	public void setIndentType(IndentType indentType) {
		this.indentType = indentType;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Status getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Status approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getAppprovedOn() {
		return appprovedOn;
	}

	public void setAppprovedOn(Date appprovedOn) {
		this.appprovedOn = appprovedOn;
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

	public List<AssetIndentDetail> getAssetIndentDetail() {
		return assetIndentDetail;
	}

	public void setAssetIndentDetail(List<AssetIndentDetail> assetIndentDetail) {
		this.assetIndentDetail = assetIndentDetail;
	}

	public AssetIndentDetail addAssetIndentDetail(AssetIndentDetail assetIndentDetail) {
		if (getAssetIndentDetail() == null)
			this.assetIndentDetail = new ArrayList<AssetIndentDetail>();
		
		getAssetIndentDetail().add(assetIndentDetail);
		assetIndentDetail.setIndent(this);

		return assetIndentDetail;
	}
	
	public AssetIndent() {
		super();
	}

	public AssetIndent(Integer id, String indentNo, Store fromStore, Store toStore, IndentType indentType,
			Priority priority, Status status, Status approvalStatus, String remark, User approvedBy, Date appprovedOn,
			CancelReason cancelReason, String cancelNote) {
		super();
		this.id = id;
		this.indentNo = indentNo;
		this.fromStore = fromStore;
		this.toStore = toStore;
		this.indentType = indentType;
		this.priority = priority;
		this.status = status;
		this.approvalStatus = approvalStatus;
		this.remark = remark;
		this.approvedBy = approvedBy;
		this.appprovedOn = appprovedOn;
		this.cancelReason = cancelReason;
		this.cancelNote = cancelNote;
	}

	public AssetIndent(String indentNo, Store fromStore, Store toStore, IndentType indentType, Priority priority,
			Status status, Status approvalStatus, String remark) {
		super();
		this.indentNo = indentNo;
		this.fromStore = fromStore;
		this.toStore = toStore;
		this.indentType = indentType;
		this.priority = priority;
		this.status = status;
		this.approvalStatus = approvalStatus;
		this.remark = remark;
	}
	
}
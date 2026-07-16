package com.param.entity.model.asset;

import java.io.Serial;
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
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;

import io.swagger.annotations.ApiModelProperty;

@Entity(name = "AssetIssue")
@Table(name = "t_asset_issue", schema = "assets")
public class AssetIssue extends BaseEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accepted_by")
	private User acceptedBy;

	@Column(name = "accepted_date")
	private Date acceptedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_indent_id", nullable = true)
	private AssetIndent assetIndent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "carrier_id")
	private User carrier;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_store_id")
	private Store fromStore;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_store_id")
	private Store toStore;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "indent_type_id")
	private IndentType indentType;

	@Column(name = "issue_no")
	private String issueNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "priority_id")
	private Priority priority;

	private String remark;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assetIssue", cascade = CascadeType.ALL)
	private List<AssetIssueDetail> assetIssueDetail;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assetIssue")
	private List<RejectedAssetAcceptance> rejectedAssetAcceptances;
	@Column(name = "is_return")
	protected Boolean isReturn;
	public Boolean getIsReturn() {
		return isReturn;
	}

	public void setIsReturn(Boolean isReturn) {
		this.isReturn = isReturn;
	}

	public AssetIssue() {
		super();
	}

	public AssetIssue(Integer id) {
		super();
		this.id = id;
	}

	public AssetIssue(AssetIndent assetIndent, Store fromStore, Store toStore, IndentType indentType, Priority priority,
			Status status) {
		this.assetIndent = assetIndent;
		this.fromStore = fromStore;
		this.toStore = toStore;
		this.indentType = indentType;
		this.priority = priority;
		this.status = status;
	}

	public AssetIssue(AssetIndent assetIndent, Store fromStore, Store toStore, IndentType indentType, String issueNo,
			Priority priority, Status status) {
		super();
		this.assetIndent = assetIndent;
		this.fromStore = fromStore;
		this.toStore = toStore;
		this.indentType = indentType;
		this.issueNo = issueNo;
		this.priority = priority;
		this.status = status;
	}

	public AssetIssue(User carrier, Store fromStore, Store toStore, String issueNo, String remark, Status status) {
		super();
		this.carrier = carrier;
		this.fromStore = fromStore;
		this.toStore = toStore;
		this.issueNo = issueNo;
		this.remark = remark;
		this.status = status;
	}

	public void updateAssetIssue(User carrier, String remark, Status status) {
		this.carrier = carrier;
		this.remark = remark;
		this.status = status;
	//	this.fromStore = fromStore;
	//	this.toStore = toStore;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getAcceptedBy() {
		return acceptedBy;
	}

	public void setAcceptedBy(User acceptedBy) {
		this.acceptedBy = acceptedBy;
	}

	public Date getAcceptedDate() {
		return acceptedDate;
	}

	public void setAcceptedDate(Date acceptedDate) {
		this.acceptedDate = acceptedDate;
	}

	public AssetIndent getAssetIndent() {
		return assetIndent;
	}

	public void setAssetIndent(AssetIndent assetIndent) {
		this.assetIndent = assetIndent;
	}

	public User getCarrier() {
		return carrier;
	}

	public void setCarrier(User carrier) {
		this.carrier = carrier;
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

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<AssetIssueDetail> getAssetIssueDetail() {
		return assetIssueDetail;
	}

	public void setAssetIssueDetail(List<AssetIssueDetail> assetIssueDetail) {
		this.assetIssueDetail = assetIssueDetail;
	}

	public AssetIssueDetail addAssetIssueDetail(AssetIssueDetail assetIssueDetail) {
		if (getAssetIssueDetail() == null)
			this.assetIssueDetail = new ArrayList<AssetIssueDetail>();

		getAssetIssueDetail().add(assetIssueDetail);
		assetIssueDetail.setAssetIssue(this);

		return assetIssueDetail;
	}

}

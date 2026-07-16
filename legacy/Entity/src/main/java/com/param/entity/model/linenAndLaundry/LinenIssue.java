package com.param.entity.model.linenAndLaundry;

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
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;

@Entity(name = "LinenIssue")
@Table(name = "t_linen_issue", schema = "linen_and_laundry")
public class LinenIssue extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "issue_no")
	private String issueNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_store_id", nullable = false)
	private Store fromStore;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_store_id", nullable = false)
	private Store toStore;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "linen_indent_id")
	private LinenIndent linenIndent;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "priority_id")
	private Priority priority;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = true)
	private Status status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "carrier_id")
	private User carrier;
	
	@Column(name = "remark")
	private String remark;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accepted_by")
	protected User acceptedBy;
	
	@Column(name = "accepted_date")
	private Date acceptedDate;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "linenIssue", cascade = CascadeType.ALL)
	private List<LinenIssueDetail> linenIssueDetail;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
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

	public LinenIndent getLinenIndent() {
		return linenIndent;
	}

	public void setLinenIndent(LinenIndent linenIndent) {
		this.linenIndent = linenIndent;
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

	public User getCarrier() {
		return carrier;
	}

	public void setCarrier(User carrier) {
		this.carrier = carrier;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public List<LinenIssueDetail> getLinenIssueDetail() {
		return linenIssueDetail;
	}

	public void setLinenIssueDetail(List<LinenIssueDetail> linenIssueDetail) {
		this.linenIssueDetail = linenIssueDetail;
	}

	public LinenIssueDetail addLinenIssueDetail(LinenIssueDetail linenIssueDetail) {
		if (getLinenIssueDetail() == null)
			this.linenIssueDetail = new ArrayList<LinenIssueDetail>();
		
		getLinenIssueDetail().add(linenIssueDetail);
		linenIssueDetail.setLinenIssue(this);
	
		return linenIssueDetail;
	}

	public LinenIssue() {
		super();
	}

	public LinenIssue(String issueNo, Store fromStore, Store toStore, LinenIndent linenIndent, Priority priority,
			Status status) {
		super();
		this.issueNo = issueNo;
		this.fromStore = fromStore;
		this.toStore = toStore;
		this.linenIndent = linenIndent;
		this.priority = priority;
		this.status = status;
	}
	
	public LinenIssue(String issueNo, Store fromStore, Store toStore, Status status, User carrier, String remark, Priority priority) {
		super();
		this.issueNo = issueNo;
		this.fromStore = fromStore;
		this.toStore = toStore;
		this.status = status;
		this.carrier = carrier;
		this.remark = remark;
		this.priority = priority;
	}

	public void updateLinenIssue(Status status, User carrier, String remark) {
		this.status = status;
		this.carrier = carrier;
		this.remark = remark;
	}

	public LinenIssue(Integer id) {
		super();
		this.id = id;
	}
	
}

package com.param.entity.model.procurement;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.User;

import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the t_wo_quotation_entry database table.
 * 
 */
@Entity
@Table(name="t_wo_quotation_entry", schema = "procurement")
public class WoQuotationEntry extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private User approvedBy;

	@Column(name = "approved_on")
	private Date approvedDate;	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approval_status_id")
	private Status approvalStatus;

	@Column(name="cancel_note")
	private String cancelNote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;

	@Temporal(TemporalType.DATE)
	@Column(name="reference_date")
	private Date referenceDate;

	@Column(name="reference_no")
	private String referenceNo;

	private String remark;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="vendor_quotation_id")
	private WoQuotationVendor vendorQuotation;

	//bi-directional many-to-one association to TWoQuotationEntryDetail
	@OneToMany(fetch = FetchType.LAZY,mappedBy="woQuotationEntry", cascade = CascadeType.ALL)
	private List<WoQuotationEntryDetail> woQuotationEntryDetailList;

	public WoQuotationEntry() {
		super();
	}

	public WoQuotationEntry(Date referenceDate, String referenceNo, String remark, Status status,Status approvalStatus,
			WoQuotationVendor vendorQuotation) {
		super();
		this.referenceDate = referenceDate;
		this.referenceNo = referenceNo;
		this.remark = remark;
		this.status = status;
		this.approvalStatus = approvalStatus;
		this.vendorQuotation = vendorQuotation;
	}
	
	public void updateWoQuotationEntry(String remark) {
		this.remark = remark;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Status getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Status approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public CancelReason getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(CancelReason cancelReason) {
		this.cancelReason = cancelReason;
	}

	public Date getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(Date referenceDate) {
		this.referenceDate = referenceDate;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
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

	public WoQuotationVendor getVendorQuotation() {
		return vendorQuotation;
	}

	public void setVendorQuotation(WoQuotationVendor vendorQuotation) {
		this.vendorQuotation = vendorQuotation;
	}

	public List<WoQuotationEntryDetail> getWoQuotationEntryDetailList() {
		return woQuotationEntryDetailList;
	}

	public void setWoQuotationEntryDetailList(List<WoQuotationEntryDetail> woQuotationEntryDetailList) {
		this.woQuotationEntryDetailList = woQuotationEntryDetailList;
	}

	public WoQuotationEntryDetail addWoQuotationEntryDetailList(WoQuotationEntryDetail woQuotationEntryDetail) {
		if (getWoQuotationEntryDetailList() == null)
			this.woQuotationEntryDetailList = new ArrayList<WoQuotationEntryDetail>();

		getWoQuotationEntryDetailList().add(woQuotationEntryDetail);
		woQuotationEntryDetail.setWoQuotationEntry(this);

		return woQuotationEntryDetail;
	}

	public WoQuotationEntryDetail removeWoQuotationEntryDetail(WoQuotationEntryDetail woQuotationEntryDetail) {
		getWoQuotationEntryDetailList().remove(woQuotationEntryDetail);
		woQuotationEntryDetail.setWoQuotationEntry(this);

		return woQuotationEntryDetail;
	}
}
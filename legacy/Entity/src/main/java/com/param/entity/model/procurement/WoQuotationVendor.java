package com.param.entity.model.procurement;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;

/**
 * The persistent class for the t_wo_quotation_send_to_vendor database table.
 * 
 */
@Entity
@Table(name = "t_wo_quotation_send_to_vendor", schema = "procurement")
public class WoQuotationVendor extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approval_status_id")
	private Status approvalStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private User approvedBy;

	@Column(name = "approved_on")
	private Date approvedDate;	
	
	@Column(name = "quotation_vendor_no")
	private String qvNo;

	@Column(name = "cancel_note")
	private String cancelNote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;

	@Temporal(TemporalType.DATE)
	@Column(name = "due_date")
	private Date dueDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quotation_request_id")
	private WorkOrderQuotationRequest WorkOrderQR;

	private String remark;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id")
	private Vendor vendor;

	// bi-directional many-to-one association to TWoQuotationSendToVendorDetail
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "WoQuotationVendor", cascade = CascadeType.ALL)
	private List<WoQuotationVendorDetail> woQuotationVendorDetail;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vendorQuotation", cascade = CascadeType.ALL)
	private List<WoQuotationEntry> woQuotationEntry;

	
	public WoQuotationVendor() {
		super();
	}
	
	public List<WoQuotationEntry> getWoQuotationEntry() {
		return woQuotationEntry;
	}

	public void setWoQuotationEntry(List<WoQuotationEntry> woQuotationEntry) {
		this.woQuotationEntry = woQuotationEntry;
	}

	public WoQuotationVendor(Integer id) {
		this.id = id;
	}
	
	public WoQuotationVendor(Status approvalStatus, String qvNo, Date dueDate, WorkOrderQuotationRequest workOrderQR,
			String remark, Status status, Vendor vendor) {
		super();
		this.approvalStatus = approvalStatus;
		this.qvNo = qvNo;
		this.dueDate = dueDate;
		this.WorkOrderQR = workOrderQR;
		this.remark = remark;
		this.status = status;
		this.vendor = vendor;
	}
	
	public WoQuotationVendor(Status statusId, WorkOrderQuotationRequest workOrderQR) {
		this.status=statusId;
		this.WorkOrderQR = workOrderQR;
	}

	public void updateWoQuotationVendor(String remark) {
		this.remark = remark;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Status getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Status approvalStatus) {
		this.approvalStatus = approvalStatus;
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

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public WorkOrderQuotationRequest getWorkOrderQR() {
		return WorkOrderQR;
	}

	public void setWorkOrderQR(WorkOrderQuotationRequest workOrderQR) {
		WorkOrderQR = workOrderQR;
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

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public String getQvNo() {
		return qvNo;
	}

	public void setQvNo(String qvNo) {
		this.qvNo = qvNo;
	}

	public List<WoQuotationVendorDetail> getWoQuotationVendorDetail() {
		return woQuotationVendorDetail;
	}

	public void setWoQuotationVendorDetail(List<WoQuotationVendorDetail> woQuotationVendorDetail) {
		this.woQuotationVendorDetail = woQuotationVendorDetail;
	}

	public WoQuotationVendorDetail addWoQuotationSendToVendorDetailList(WoQuotationVendorDetail WoQuotationVendorDetail) {
		if (getWoQuotationVendorDetail() == null)
			this.woQuotationVendorDetail = new ArrayList<WoQuotationVendorDetail>();

		getWoQuotationVendorDetail().add(WoQuotationVendorDetail);
		WoQuotationVendorDetail.setWoQuotationVendor(this);

		return WoQuotationVendorDetail;
	}

	public WoQuotationVendorDetail removeWoQuotationSendToVendorDetail(WoQuotationVendorDetail WoQuotationVendorDetail) {
		getWoQuotationVendorDetail().remove(WoQuotationVendorDetail);
		WoQuotationVendorDetail.setWoQuotationVendor(this);

		return WoQuotationVendorDetail;
	}

}
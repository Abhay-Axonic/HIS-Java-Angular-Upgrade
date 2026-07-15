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

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;

@Entity(name = "ConsignmentInward")
@Table(name = "t_consignment_inward", schema = "procurement")
public class ConsignmentInward extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "inward_number")
	private String inwardNumber;

	@Column(name = "dc_date")
	private Date dcDate;

	@Column(name = "dc_number")
	private String dcNumber;

	@Column(name = "net_amount")
	private Double netAmount;

	private String remark;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

	@Column(name = "total_amount")
	private Double totalAmount;

	@Column(name = "total_discount")
	private Double totalDiscount;

	@Column(name = "total_tax")
	private Double totalTax;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id")
	private Vendor vendor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private User approvedBy;

	@Column(name = "approved_date")
	private Date approvalDate;

	@Column(name = "cancel_note", length = 100)
	private String cancelNote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;

	// bi-directional many-to-one association to ConsignmentInwardDetail
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "consignmentInward", cascade = CascadeType.ALL)
	private List<ConsignmentInwardDetail> consignmentInwardDetails;

	public ConsignmentInward() {
		super();
	}

	public ConsignmentInward(String inwardNumber, Date dcDate, String dcNumber, Double netAmount, String remark,
			Store store, Double totalAmount, Double totalDiscount, Double totalTax, Vendor vendor, Status status) {
		super();
		this.inwardNumber=inwardNumber;
		this.dcDate = dcDate;
		this.dcNumber = dcNumber;
		this.netAmount = netAmount;
		this.remark = remark;
		this.store = store;
		this.totalAmount = totalAmount;
		this.totalDiscount = totalDiscount;
		this.totalTax = totalTax;
		this.vendor = vendor;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInwardNumber() {
		return inwardNumber;
	}

	public void setInwardNumber(String inwardNumber) {
		this.inwardNumber = inwardNumber;
	}

	public Date getDcDate() {
		return dcDate;
	}

	public void setDcDate(Date dcDate) {
		this.dcDate = dcDate;
	}

	public String getDcNumber() {
		return dcNumber;
	}

	public void setDcNumber(String dcNumber) {
		this.dcNumber = dcNumber;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Double getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(Double totalTax) {
		this.totalTax = totalTax;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
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

	public List<ConsignmentInwardDetail> getConsignmentInwardDetails() {
		return consignmentInwardDetails;
	}

	public void setConsignmentInwardDetails(List<ConsignmentInwardDetail> consignmentInwardDetails) {
		this.consignmentInwardDetails = consignmentInwardDetails;
	}

	public ConsignmentInwardDetail addConsignmentInwardDetail(ConsignmentInwardDetail consignmentInwardDetail) {
		if (getConsignmentInwardDetails() == null)
			this.consignmentInwardDetails = new ArrayList<ConsignmentInwardDetail>();
		getConsignmentInwardDetails().add(consignmentInwardDetail);
		consignmentInwardDetail.setConsignmentInward(this);

		return consignmentInwardDetail;
	}

	public ConsignmentInwardDetail removeConsignmentInwardDetail(ConsignmentInwardDetail consignmentInwardDetail) {
		getConsignmentInwardDetails().remove(consignmentInwardDetail);
		consignmentInwardDetail.setConsignmentInward(null);

		return consignmentInwardDetail;
	}

}

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

/**
 * The persistent class for the t_consignment_outward database table.
 * 
 */
@Entity(name = "ConsignmentOutward")
@Table(name = "t_consignment_outward", schema = "procurement")
public class ConsignmentOutward extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "dc_date")
	private Date dcDate;

	@Column(name = "outward_number")
	private String outwardNumber;

	@Column(name = "dc_number")
	private String dcNumber;

	@Column(name = "total_amount")
	private double totalAmount;

	@Column(name = "total_tax")
	private double totalTax;

	@Column(name = "net_amount")
	private double netAmount;

	@Column(name = "remark")
	private String remark;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

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
	
	
	@Column(name = "canceled_date")
	private Date canceledDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "canceled_by")
	private User canceledBy;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "canceled_reason_id")
	private CancelReason cancelReason;
	
	// bi-directional many-to-one association to ConsignmentOutwardDetail
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "consignmentOutward", cascade = CascadeType.ALL)
	private List<ConsignmentOutwardDetail> consignmentOutwardDetails;

	public ConsignmentOutward() {
		super();
	}

	public ConsignmentOutward(String consignmentOutNumber, Store store, Vendor vendor, Date dcDate, String dcNumber,
			Status status, Double totalAmount, Double totalTax, Double netAmount, String remark) {
		super();
		this.outwardNumber=consignmentOutNumber;
		this.store = store;
		this.vendor = vendor;
		this.dcDate = dcDate;
		this.dcNumber = dcNumber;
		this.status = status;
		this.netAmount = netAmount;
		this.totalAmount = totalAmount;
		this.totalTax = totalTax;
		this.remark = remark;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDcDate() {
		return this.dcDate;
	}

	public void setDcDate(Date dcDate) {
		this.dcDate = dcDate;
	}

	public String getDcNumber() {
		return this.dcNumber;
	}

	public void setDcNumber(String dcNumber) {
		this.dcNumber = dcNumber;
	}

	public double getTotalAmount() {
		return this.totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getTotalTax() {
		return this.totalTax;
	}

	public void setTotalTax(double totalTax) {
		this.totalTax = totalTax;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<ConsignmentOutwardDetail> getConsignmentOutwardDetails() {
		return this.consignmentOutwardDetails;
	}

	public void setConsignmentOutwardDetails(List<ConsignmentOutwardDetail> consignmentOutwardDetails) {
		this.consignmentOutwardDetails = consignmentOutwardDetails;
	}

	public ConsignmentOutwardDetail addConsignmentOutwardDetail(ConsignmentOutwardDetail consignmentOutwardDetail) {
		if (getConsignmentOutwardDetails() == null)
			this.consignmentOutwardDetails = new ArrayList<ConsignmentOutwardDetail>();
		getConsignmentOutwardDetails().add(consignmentOutwardDetail);
		consignmentOutwardDetail.setConsignmentOutward(this);

		return consignmentOutwardDetail;
	}

	public ConsignmentOutwardDetail removeTConsignmentOutwardDetail(ConsignmentOutwardDetail consignmentOutwardDetail) {
		getConsignmentOutwardDetails().remove(consignmentOutwardDetail);
		consignmentOutwardDetail.setConsignmentOutward(null);

		return consignmentOutwardDetail;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getOutwardNumber() {
		return outwardNumber;
	}

	public void setOutwardNumber(String outwardNumber) {
		this.outwardNumber = outwardNumber;
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

	public Date getCanceledDate() {
		return canceledDate;
	}

	public void setCanceledDate(Date canceledDate) {
		this.canceledDate = canceledDate;
	}

	public User getCanceledBy() {
		return canceledBy;
	}

	public void setCanceledBy(User canceledBy) {
		this.canceledBy = canceledBy;
	}


	/*public void setCancelReason(CancelReason cancelReason) {
		// TODO Auto-generated method stub
		
	}*/
}

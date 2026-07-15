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
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.DiscountType;
import com.param.entity.model.master.RequestType;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;

@Entity(name = "ConsignmentStockReturn")
@Table(name = "t_consignment_return", schema = "procurement")
public class ConsignmentStockReturn extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "grn_id")
	private GoodReceiptNote goodReceiptNote;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approve_status_id")
	private Status approvalStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private User approvedBy;

	@Column(name = "approved_date")
	private Date approvedDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_type_id")
	private AssetType assetType;

	@Column(name = "cancel_note")
	private String cancelNote;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;

	@Column(name = "cr_number")
	private String crNumber;
	
	@Column(name = "total_amount")
	private Double totalAmount;
	
	@Column(name = "total_tax_amount")
	private Double totalTaxAmount;
	
	@Column(name = "total_net_amount")
	private Double totalNetAmount;

	@Column(name = "is_consignment")
	private Boolean isConsignment;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;
	@Column(name = "remark")
	private String remark;
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id")
	private Vendor vendor;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "consignmentStockReturn", cascade = CascadeType.ALL)
	private List<ConsignmentStockReturnDetail> consignmentStockReturnDetailsList;
	
	public ConsignmentStockReturn() {
		super();
	}

	public ConsignmentStockReturn(Integer id) {
		super();
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public GoodReceiptNote getGoodReceiptNote() {
		return goodReceiptNote;
	}

	public void setGoodReceiptNote(GoodReceiptNote goodReceiptNote) {
		this.goodReceiptNote = goodReceiptNote;
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

	public AssetType getAssetType() {
		return assetType;
	}

	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
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

	public String getCrNumber() {
		return crNumber;
	}

	public void setCrNumber(String crNumber) {
		this.crNumber = crNumber;
	}
	
	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public Double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(Double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public Double getTotalNetAmount() {
		return totalNetAmount;
	}

	public void setTotalNetAmount(Double totalNetAmount) {
		this.totalNetAmount = totalNetAmount;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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

	public List<ConsignmentStockReturnDetail> getConsignmentStockReturnDetailsList() {
		return consignmentStockReturnDetailsList;
	}

	public void setConsignmentStockReturnDetailsList(List<ConsignmentStockReturnDetail> consignmentStockReturnDetailsList) {
		this.consignmentStockReturnDetailsList = consignmentStockReturnDetailsList;
	}

	public ConsignmentStockReturn(Status approvalStatus, AssetType assetType, String crNumber,
			Boolean isConsignment, Status status, Store store, Vendor vendor,GoodReceiptNote goodReceiptNote,Double totalAmount,Double totalTaxAmount,Double totalNetAmount ) {
		super();
		this.approvalStatus = approvalStatus;
		this.assetType = assetType;
		this.crNumber = crNumber;
		this.isConsignment = isConsignment;
		this.status = status;
		this.store = store;
		this.vendor = vendor;
		this.goodReceiptNote = goodReceiptNote;
		this.totalAmount = totalAmount;
		this.totalTaxAmount = totalTaxAmount;
		this.totalNetAmount = totalNetAmount;
	}
	
	

	public ConsignmentStockReturnDetail addConsignmentStockReturnDetailList(ConsignmentStockReturnDetail consignmentStockReturnDetail) {
		if (getConsignmentStockReturnDetailsList() == null)
			this.consignmentStockReturnDetailsList = new ArrayList<ConsignmentStockReturnDetail>();

		getConsignmentStockReturnDetailsList().add(consignmentStockReturnDetail);
		consignmentStockReturnDetail.setConsignmentStockReturn(this);

		return consignmentStockReturnDetail;
	}

}
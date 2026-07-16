package com.param.entity.model.procurement;

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

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.DiscountType;
import com.param.entity.model.master.RequestType;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;


/**
 * The persistent class for the t_purchase_order database table.
 * 
 */
@Entity(name = "ConsignmentStockPO")
@Table(name="t_consignment_stock_purchase_order",schema="procurement")
public class ConsignmentStockPO extends BaseEntity implements Serializable{
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "approve_status_id")
	private Status approvalStatus;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private User approvedBy;


	@Column(name="approved_date")
	private Date approvedDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "asset_type_id")
	private AssetType assetType;

	@Column(name="cancel_note")
	private String cancelNote;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "discount_type_id")
	private DiscountType discountType;

	@Column(name = "discount")
	private Double discount;

	@Column(name="is_consignment")
	private Boolean isConsignment;

	@Column(name="net_amount")
	private Double netAmount;

	@Column(name="po_number")
	private String poNumber;

	@Column(name = "remark")
	private String remark;

	@Column(name="round_off_amount")
	private Double roundOffAmount;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "requesting_store_id")
	private Store requestingStore;
	
	public Store getRequestingStore() {
		return requestingStore;
	}

	public void setRequestingStore(Store requestingStore) {
		this.requestingStore = requestingStore;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "po_type_id")
	private RequestType requestType;
	
	@Column(name="total_amount")
	private Double totalAmount;

	@Column(name="total_discount")
	private Double totalDiscount;

	@Column(name="total_other_amount")
	private Double totalOtherAmount;

	@Column(name="total_tax_amount")
	private Double totalTaxAmount;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "vendor_id")
	private Vendor vendor;
	
	@Column(name="payment_term")
	private String paymentTerm;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "consignmentPurchaseOrder", cascade = CascadeType.ALL)
	private List<ConsignmentStockPODetail> consignmentPurchaseOrderDetailList;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "consignmentPurchaseOrder", cascade = CascadeType.ALL)
	private List<ConsignmentStockPOOtherChargesMapper> consignmentPOOtherChargesList;
	

	public List<ConsignmentStockPOOtherChargesMapper> getPoOtherChargesList() {
		return consignmentPOOtherChargesList;
	}

	public void setPoOtherChargesList(List<ConsignmentStockPOOtherChargesMapper> poOtherChargesList) {
		this.consignmentPOOtherChargesList = poOtherChargesList;
	}

	public ConsignmentStockPO() {
		super();
	}
	
	public ConsignmentStockPO(Integer id) {
		this.id = id;
	}
	
	public ConsignmentStockPO(Status approvalStatus, AssetType assetType, 
			 Boolean isConsignment, Double netAmount, String poNumber, String remark,
			 Double roundOffAmount, Status status, Store store, RequestType requestType, Double totalAmount,
			Double totalDiscount, Double totalOtherAmount, Double totalTaxAmount, Vendor vendor,String paymentTerm , Store requestingStore) {
		super();
		this.approvalStatus = approvalStatus;
		this.assetType = assetType;
		this.isConsignment = isConsignment;
		this.netAmount = netAmount;
		this.poNumber = poNumber;
		this.remark = remark;
		this.roundOffAmount = roundOffAmount;
		this.status = status;
		this.store = store;
		this.requestType = requestType;
		this.totalAmount = totalAmount;
		this.totalDiscount = totalDiscount;
		this.totalOtherAmount = totalOtherAmount;
		this.totalTaxAmount = totalTaxAmount;
		this.vendor = vendor;
		this.paymentTerm = paymentTerm;
		this.requestingStore = requestingStore;
	}
	
	public void updatePurchaseOrder(DiscountType discountType, Double discount,Double netAmount,
			String remark,Double roundOffAmount,Double totalAmount,Double totalDiscount, Double totalOtherAmount, Double totalTaxAmount,String paymentTerm) {
		this.discountType = discountType;
		this.discount = discount;
		this.netAmount = netAmount;
		this.remark = remark;
		this.roundOffAmount = roundOffAmount;
		this.totalAmount = totalAmount;
		this.totalDiscount = totalDiscount;
		this.totalOtherAmount = totalOtherAmount;
		this.totalTaxAmount = totalTaxAmount;
		this.paymentTerm = paymentTerm;
	}
	
	public void approvePurchaseOrder(String remark,String paymentTerm) {
		this.remark = remark;
		this.paymentTerm = paymentTerm;
	}
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCancelNote() {
		return this.cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsConsignment() {
		return this.isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public Boolean getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Double getNetAmount() {
		return this.netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public String getPoNumber() {
		return this.poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getRoundOffAmount() {
		return this.roundOffAmount;
	}

	public void setRoundOffAmount(Double roundOffAmount) {
		this.roundOffAmount = roundOffAmount;
	}

	public Double getTotalAmount() {
		return this.totalAmount;
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

	public Double getTotalOtherAmount() {
		return this.totalOtherAmount;
	}

	public void setTotalOtherAmount(Double totalOtherAmount) {
		this.totalOtherAmount = totalOtherAmount;
	}

	public Double getTotalTaxAmount() {
		return this.totalTaxAmount;
	}

	public void setTotalTaxAmount(Double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
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

	public CancelReason getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(CancelReason cancelReason) {
		this.cancelReason = cancelReason;
	}

	public DiscountType getDiscountType() {
		return discountType;
	}

	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
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

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}
	
	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public List<ConsignmentStockPODetail> getPurchaseOrderDetailList() {
		return consignmentPurchaseOrderDetailList;
	}

	public void setPurchaseOrderDetailList(List<ConsignmentStockPODetail> purchaseOrderDetailList) {
		this.consignmentPurchaseOrderDetailList = purchaseOrderDetailList;
	}
	
	public ConsignmentStockPODetail addPurchaseOrderDetailList(ConsignmentStockPODetail purchaseOrderDetail) {
		if (getPurchaseOrderDetailList() == null)
			this.consignmentPurchaseOrderDetailList = new ArrayList<ConsignmentStockPODetail>();
		
		getPurchaseOrderDetailList().add(purchaseOrderDetail);
		purchaseOrderDetail.setPurchaseOrder(this);

		return purchaseOrderDetail;
	}
	public ConsignmentStockPOOtherChargesMapper addPoOtherChargesMapperList(ConsignmentStockPOOtherChargesMapper poOtherChargesMapper) {
		if (getPoOtherChargesList() == null)
			this.consignmentPOOtherChargesList = new ArrayList<ConsignmentStockPOOtherChargesMapper>();
		
		getPoOtherChargesList().add(poOtherChargesMapper);
		poOtherChargesMapper.setPurchaseOrder(this);

		return poOtherChargesMapper;
	}
	
}
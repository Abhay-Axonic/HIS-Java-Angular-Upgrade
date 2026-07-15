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
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.DiscountType;
import com.param.entity.model.master.PaymentMode;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;


	
@Entity(name = "ConsignmentPurchaseOrder")
@Table(name = "t_consignment_purchase_order", schema = "procurement")
public class ConsignmentPurchaseOrder extends BaseEntity implements Serializable {
		private static final long serialVersionUID = 1L;

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(unique = true, nullable = false)
		private Integer id;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "vendor_id", nullable = false)
		private Vendor vendor;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "store_id", nullable = false)
		private Store store;
		
		@Column(name = "po_number", nullable = false)
		private String poNumber;
		
				
		@Column(name = "total_amount", nullable = false)
		private Double totalAmount;
		
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name = "discount_type_id")
		private DiscountType discountType;
		
		@Column(name = "discount")
		private Integer discount;
		
		@Column(name = "total_discount", nullable = false)
		private Double totalDiscount;
		
		@Column(name = "total_tax_amount", nullable = false)
		private Double totalTaxAmount;
		
		@Column(name = "total_other_amount")
		private Double totalOtherAmount;
		
		@Column(name = "net_amount", nullable = false)
		private Double netAmount;
		
		@Column(name = "round_off_amount", nullable = false)
		private Double roundOffAmount;
		
		@Column(name = "is_consignment", nullable = false)
		private Boolean isConsignment;
		
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name = "status_id", nullable = false)
		private Status status;
		
		@ManyToOne(fetch=FetchType.LAZY)
		@JoinColumn(name = "approve_status_id")
		private Status approveStatusId;
		
		@Column(name = "remark")
		private String remark;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "cancel_reason_id")
		private CancelReason cancelReason;
		
		@Column(name = "cancel_note")
		private String cancelNote;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "approved_by")
		private User approvedBy;
		
		@Column(name = "approved_date")
		private Date approvedDate;
		
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "payment_mode_id")
		private PaymentMode paymentMode;
		
		@Column(name = "payment_term")
		private String paymentTerm;
		
		@Column(name = "is_implant", nullable = false)
		private Boolean isImplant;
				
		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "grn_id")
		private GoodReceiptNote goodReceiptNote;
				
		@OneToMany(fetch = FetchType.LAZY, mappedBy = "consignmentPurchaseOrder", cascade = CascadeType.ALL)
		private List<ConsignmentPurchaseOrderDetail> consignmentPurchaseOrderDetail;

		public ConsignmentPurchaseOrder() {
			super();
		}

		public ConsignmentPurchaseOrder(Vendor vendorId, Store storeId, String consignmentPONumber, Integer discount,
				double totalDiscount,double totalAmount, double totalTaxAmount, double netAmount, double roundOffamount,
				Boolean isConsignment, Status status, Boolean isImplant,Status approvalStatusId) {
			super();
			this.vendor=vendorId;
			this.store=storeId;
			this.poNumber=consignmentPONumber;
			this.discount=discount;
			this.totalDiscount=totalDiscount;
			this.totalAmount=totalAmount;
			this.totalTaxAmount=totalTaxAmount;
			this.netAmount=netAmount;
			this.roundOffAmount=roundOffamount;
			this.isConsignment=isConsignment;
			this.status=status;
			this.isImplant=isImplant;
			this.approveStatusId=approvalStatusId;
		}

		public ConsignmentPurchaseOrder(Integer id) {
			this.id=id;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public Vendor getVendor() {
			return vendor;
		}

		public void setVendor(Vendor vendor) {
			this.vendor = vendor;
		}

		public Store getStore() {
			return store;
		}

		public void setStore(Store store) {
			this.store = store;
		}

		

		public String getPoNumber() {
			return poNumber;
		}

		public void setPoNumber(String poNumber) {
			this.poNumber = poNumber;
		}

		

		
		public Double getTotalAmount() {
			return totalAmount;
		}

		public void setTotalAmount(Double totalAmount) {
			this.totalAmount = totalAmount;
		}

		public DiscountType getDiscountType() {
			return discountType;
		}

		public void setDiscountType(DiscountType discountType) {
			this.discountType = discountType;
		}

		public Integer getDiscount() {
			return discount;
		}

		public void setDiscount(Integer discount) {
			this.discount = discount;
		}

		public Double getTotalDiscount() {
			return totalDiscount;
		}

		public void setTotalDiscount(Double totalDiscount) {
			this.totalDiscount = totalDiscount;
		}

		public Double getTotalTaxAmount() {
			return totalTaxAmount;
		}

		public void setTotalTaxAmount(Double totalTaxAmount) {
			this.totalTaxAmount = totalTaxAmount;
		}

		public Double getTotalOtherAmount() {
			return totalOtherAmount;
		}

		public void setTotalOtherAmount(Double totalOtherAmount) {
			this.totalOtherAmount = totalOtherAmount;
		}

		public Double getNetAmount() {
			return netAmount;
		}

		public void setNetAmount(Double netAmount) {
			this.netAmount = netAmount;
		}

		public Double getRoundOffAmount() {
			return roundOffAmount;
		}

		public void setRoundOffAmount(Double roundOffAmount) {
			this.roundOffAmount = roundOffAmount;
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

		public Status getApproveStatusId() {
			return approveStatusId;
		}

		public void setApproveStatusId(Status approveStatusId) {
			this.approveStatusId = approveStatusId;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
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

		public PaymentMode getPaymentMode() {
			return paymentMode;
		}

		public void setPaymentMode(PaymentMode paymentMode) {
			this.paymentMode = paymentMode;
		}

		public String getPaymentTerm() {
			return paymentTerm;
		}

		public void setPaymentTerm(String paymentTerm) {
			this.paymentTerm = paymentTerm;
		}

		

		public Boolean getIsImplant() {
			return isImplant;
		}

		public void setIsImplant(Boolean isImplant) {
			this.isImplant = isImplant;
		}

		public List<ConsignmentPurchaseOrderDetail> getConsignmentPurchaseOrderDetail() {
			return consignmentPurchaseOrderDetail;
		}

		public void setConsignmentPurchaseOrderDetail(List<ConsignmentPurchaseOrderDetail> consignmentPurchaseOrderDetail) {
			this.consignmentPurchaseOrderDetail = consignmentPurchaseOrderDetail;
		}
		
		public GoodReceiptNote getGoodReceiptNote() {
			return goodReceiptNote;
		}

		public void setGoodReceiptNote(GoodReceiptNote goodReceiptNote) {
			this.goodReceiptNote = goodReceiptNote;
		}
		
		
		public ConsignmentPurchaseOrderDetail addConsignmentPurchaseOrderDetail(ConsignmentPurchaseOrderDetail consignmentPurchaseOrderDetail) {
			if (getConsignmentPurchaseOrderDetail() == null)
				this.consignmentPurchaseOrderDetail = new ArrayList<ConsignmentPurchaseOrderDetail>();
			getConsignmentPurchaseOrderDetail().add(consignmentPurchaseOrderDetail);
			consignmentPurchaseOrderDetail.setConsignmentPurchaseOrder(this);

			return consignmentPurchaseOrderDetail;
		}
		
		

}

package com.param.procurement.purchase.purchaseOrder.model;

import java.util.Date;

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

import org.springframework.context.annotation.Import;

import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.DiscountType;
import com.param.entity.model.master.RequestType;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;
import com.param.procurement.common.model.AbstractEntity;
import com.wordnik.swagger.annotations.ApiModelProperty;

@Entity(name = "LocPurchaseOrder")
@Table(name = "t_purchase_order", schema = "procurement")
public class LocPurchaseOrder extends AbstractEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "organization_id")
	@ApiModelProperty(value = "OrganisationId Of StoreIndentMaster ")
	private Integer organizationId;

	@Column(name = "unit_id")
	@ApiModelProperty(value = "UnitId Of StoreIndentMaster")
	private Integer unitId;
	
	@Column(name = "is_active")
	@ApiModelProperty(value = "isActive Of LocalPurchaseRequest")
	private Boolean isActive;


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
	private double discount;

	@Column(name="is_consignment")
	private Boolean isConsignment;

	@Column(name="net_amount")
	private double netAmount;

	@Column(name="po_number")
	private String poNumber;

	@Column(name = "remark")
	private String remark;

	@Column(name="round_off_amount")
	private double roundOffAmount;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "store_id")	
	private Store store;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "po_type_id")
	private RequestType requestType;
	
	@Column(name="total_amount")
	private double totalAmount;

	@Column(name="total_discount")
	private double totalDiscount;

	@Column(name="total_other_amount")
	private double totalOtherAmount;

	@Column(name="total_tax_amount")
	private double totalTaxAmount;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "vendor_id")
	private Vendor vendor;

	public LocPurchaseOrder() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public LocPurchaseOrder(Integer id, Integer organizationId, Integer unitId, Status approvalStatus, User approvedBy,
			Date approvedDate, AssetType assetType, String cancelNote, CancelReason cancelReason,
			DiscountType discountType, double discount, Boolean isConsignment, double netAmount, String poNumber,
			String remark, double roundOffAmount, Status status, Store store, RequestType requestType,
			double totalAmount, double totalDiscount, double totalOtherAmount, double totalTaxAmount, Vendor vendor) {
		super();
		this.id = id;
		this.organizationId = organizationId;
		this.unitId = unitId;
		this.approvalStatus = approvalStatus;
		this.approvedBy = approvedBy;
		this.approvedDate = approvedDate;
		this.assetType = assetType;
		this.cancelNote = cancelNote;
		this.cancelReason = cancelReason;
		this.discountType = discountType;
		this.discount = discount;
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
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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

	public DiscountType getDiscountType() {
		return discountType;
	}

	public void setDiscountType(DiscountType discountType) {
		this.discountType = discountType;
	}

	public Boolean getIsConsignment() {
		return isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment;
	}

	public double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public double getRoundOffAmount() {
		return roundOffAmount;
	}

	public void setRoundOffAmount(double roundOffAmount) {
		this.roundOffAmount = roundOffAmount;
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

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getTotalOtherAmount() {
		return totalOtherAmount;
	}

	public void setTotalOtherAmount(double totalOtherAmount) {
		this.totalOtherAmount = totalOtherAmount;
	}

	public double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	
	

}

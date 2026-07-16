package com.param.entity.model.inventory;

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
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.GoodReceiptNoteDetail;
import com.param.utility.TimeZoneComponent;

@Entity(name = "ReturnableGatepassDetail")
@Table(name = "t_returnable_gatepass_detail", schema = "inventory")
public class ReturnableGatepassDetail extends BaseEntity implements Serializable {


	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	protected User approvedBy;

	@Column(name = "approved_on")
	private Date approvedOn;

	@Column(name = "approved_qty")
	private Integer approvedQty = 0;

	@Column(name = "net_amount", nullable = false)
	private Double netAmount = 0.0;

	@Column(name = "reject_note", length = 100)
	private String rejectNote;

	@Column(name = "reject_qty")
	private Integer rejectQty = 0;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reject_reason_id")
	private RejectReason rejectReason;

	@Column(name = "return_qty", nullable = false)
	private Integer returnQty = 0;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@Column(name = "tax_percentage")
	private Double taxPercentage = 0.0;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_id")
	private Tax tax;

	@Column(name = "total_amt", nullable = false)
	private Double totalAmt = 0.0;
	
	
	@Column(name = "lcop", nullable = false)
	private Double lcop = 0.0;
	
	
	public Double getLcop() {
		return lcop;
	}

	public void setLcop(Double lcop) {
		this.lcop = lcop;
	}
	@Column(name = "total_tax_amount", nullable = false)
	private Double totalTaxAmount = 0.0;
	
	@Column(name = "total_discount", nullable = false)
	private Double totalDiscount = 0.0;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_type_id")
	private UomType uomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_unit_id")
	private UomUnit uomUnit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id")
	private Vendor vendor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id", nullable = false)
	private Batch batch;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "grn_detail_id")
	private GoodReceiptNoteDetail goodReceiptNoteDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "returnable_gatepass_id", nullable = false)
	private ReturnableGatepass returnableGatepass;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "returnableGatepassDetail", cascade = CascadeType.ALL)
	private List<ReturnableGatepassTaxComponent> returnableGatepassTaxComponents;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "returnableGatepassDetail", cascade = CascadeType.ALL)
	private List<ReturnableGatepassAccessories> returnableGatepassAccessoriesList;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bonus_uom_type_id")
	private UomType bonusUomType;
	

	public ReturnableGatepassDetail() {
		super();
	}
	
	public List<ReturnableGatepassAccessories> getReturnableGatepassAccessoriesList() {
		return returnableGatepassAccessoriesList;
	}

	public void setReturnableGatepassAccessoriesList(
			List<ReturnableGatepassAccessories> returnableGatepassAccessoriesList) {
		this.returnableGatepassAccessoriesList = returnableGatepassAccessoriesList;
	}

	public ReturnableGatepassDetail(Integer id) {
		super();
		this.id = id;
	}

	public ReturnableGatepassDetail(Batch batch, Vendor vendor, UomType uomType, UomUnit uomUnit, Integer returnQty,
			Tax tax, Double taxPercentage, Double totalAmt, Double totalTaxAmount, Double netAmount, Status status,
			GoodReceiptNoteDetail goodReceiptNoteDetail) {
		super();
		this.netAmount = netAmount;
		this.returnQty = returnQty;
		this.status = status;
		this.taxPercentage = taxPercentage;
		this.tax = tax;
		this.totalAmt = totalAmt;
		this.totalTaxAmount = totalTaxAmount;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.vendor = vendor;
		this.batch = batch;
		this.goodReceiptNoteDetail = goodReceiptNoteDetail;
	}
	
	public ReturnableGatepassDetail(Batch batch, Vendor vendor, UomType uomType, UomUnit uomUnit, Integer returnQty,
			Tax tax, Double taxPercentage, Double totalAmt, Double totalTaxAmount, Double netAmount, Status status,
			GoodReceiptNoteDetail goodReceiptNoteDetail , Double lcop) {
		super();
		this.netAmount = netAmount;
		this.returnQty = returnQty;
		this.status = status;
		this.taxPercentage = taxPercentage;
		this.tax = tax;
		this.totalAmt = totalAmt;
		this.totalTaxAmount = totalTaxAmount;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.vendor = vendor;
		this.batch = batch;
		this.goodReceiptNoteDetail = goodReceiptNoteDetail;
		this.lcop = lcop;
	}

	public ReturnableGatepassDetail(Batch batch, Vendor vendor, Integer returnQty,
			Tax tax, Double taxPercentage, Double totalAmt, Double totalTaxAmount, Double netAmount, Status status,
			GoodReceiptNoteDetail goodReceiptNoteDetail) {
		super();
		this.netAmount = netAmount;
		this.returnQty = returnQty;
		this.status = status;
		this.taxPercentage = taxPercentage;
		this.tax = tax;
		this.totalAmt = totalAmt;
		this.totalTaxAmount = totalTaxAmount;
		
		this.vendor = vendor;
		this.batch = batch;
		this.goodReceiptNoteDetail = goodReceiptNoteDetail;
	}
	
	public ReturnableGatepassDetail(Batch batch, Vendor vendor, Integer returnQty,
			  Double totalAmt, Double totalTaxAmount, Double netAmount, Status status,
			GoodReceiptNoteDetail goodReceiptNoteDetail) {
		super();
		this.netAmount = netAmount;
		this.returnQty = returnQty;
		this.status = status;
		 
		this.totalAmt = totalAmt;
		this.totalTaxAmount = totalTaxAmount;
		
		this.vendor = vendor;
		this.batch = batch;
		this.goodReceiptNoteDetail = goodReceiptNoteDetail;
	}
	public ReturnableGatepassDetail(Batch batch, Vendor vendor, UomType uomType, UomUnit uomUnit, Integer returnQty,
			Tax tax, Double taxPercentage, Double totalAmt, Double totalTaxAmount, Double netAmount, Status status) {
		this.netAmount = netAmount;
		this.returnQty = returnQty;
		this.status = status;
		this.taxPercentage = taxPercentage;
		this.tax = tax;
		this.totalAmt = totalAmt;
		this.totalTaxAmount = totalTaxAmount;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.vendor = vendor;
		this.batch = batch;
	}
	public ReturnableGatepassDetail(Integer id,Batch batch, Vendor vendor, UomType uomType, UomUnit uomUnit, Integer returnQty,
			Tax tax, Double taxPercentage, Double totalAmt, Double totalTaxAmount, Double netAmount, Status status) {
		this.id=id;
		this.netAmount = netAmount;
		this.returnQty = returnQty;
		this.status = status;
		this.taxPercentage = taxPercentage;
		this.tax = tax;
		this.totalAmt = totalAmt;
		this.totalTaxAmount = totalTaxAmount;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.vendor = vendor;
		this.batch = batch;
	}

	public ReturnableGatepassDetail(Batch batch, Vendor vendor,  Integer returnQty,
			Tax tax, Double taxPercentage, Double totalAmt, Double totalTaxAmount, Double netAmount, Status status) {
		
		this.netAmount = netAmount;
		this.returnQty = returnQty;
		this.status = status;
		this.taxPercentage = taxPercentage;
		this.tax = tax;
		this.totalAmt = totalAmt;
		this.totalTaxAmount = totalTaxAmount;
		this.vendor = vendor;
		this.batch = batch;
	}

	
	public void updateReturnableGatepassDetail(Integer returnQty, Double totalAmt, Double totalTaxAmount,
			Double netAmount , Integer userId) {
		this.netAmount = netAmount;
		this.returnQty = returnQty;
		this.totalAmt = totalAmt;
		this.totalTaxAmount = totalTaxAmount;
	//	this.updatedBy = this.getUser();
		this.updatedByUser =userId;
		this.updatedDate = TimeZoneComponent.getDateByzone(0);
	}

	public void approve(Integer approvedQty, Integer rejectQty, String rejectNote, Status status,
			RejectReason rejectReason , Integer userId) {
		this.approvedQty = approvedQty;
		this.rejectNote = rejectNote;
		this.rejectQty = rejectQty;
		this.rejectReason = rejectReason;
		this.status = status;
		this.approvedBy = new User(userId);
		this.approvedOn = TimeZoneComponent.getDateByzone(0);
	//	this.updatedBy = this.getUser();
		this.updatedByUser =userId;
		this.updatedDate = TimeZoneComponent.getDateByzone(0);
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

	public Date getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(Date approvedOn) {
		this.approvedOn = approvedOn;
	}

	public Integer getApprovedQty() {
		return approvedQty;
	}

	public void setApprovedQty(Integer approvedQty) {
		this.approvedQty = approvedQty;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public Integer getRejectQty() {
		return rejectQty;
	}

	public void setRejectQty(Integer rejectQty) {
		this.rejectQty = rejectQty;
	}

	public RejectReason getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(RejectReason rejectReason) {
		this.rejectReason = rejectReason;
	}

	public Integer getReturnQty() {
		return returnQty;
	}

	public void setReturnQty(Integer returnQty) {
		this.returnQty = returnQty;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Tax getTax() {
		return tax;
	}

	public void setTax(Tax tax) {
		this.tax = tax;
	}

	public Double getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(Double totalAmt) {
		this.totalAmt = totalAmt;
	}

	public Double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(Double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public UomType getUomType() {
		return uomType;
	}

	public void setUomType(UomType uomType) {
		this.uomType = uomType;
	}

	public UomUnit getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(UomUnit uomUnit) {
		this.uomUnit = uomUnit;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public ReturnableGatepass getReturnableGatepass() {
		return returnableGatepass;
	}

	public void setReturnableGatepass(ReturnableGatepass returnableGatepass) {
		this.returnableGatepass = returnableGatepass;
	}
	
	
	

	public ReturnableGatepassTaxComponent addReturnableGatepassTaxCom(ReturnableGatepassTaxComponent taxComponent) {
		if (this.returnableGatepassTaxComponents == null)
			this.returnableGatepassTaxComponents = new ArrayList<ReturnableGatepassTaxComponent>();

		this.returnableGatepassTaxComponents.add(taxComponent);
		taxComponent.setReturnableGatepassDetail(this);

		return taxComponent;
	}
	
	
	
	public ReturnableGatepassAccessories addReturnableGatepassAccessories(ReturnableGatepassAccessories returnableGatepassAccessories) {

		if (returnableGatepassAccessoriesList == null)
			this.returnableGatepassAccessoriesList = new ArrayList<ReturnableGatepassAccessories>();
		returnableGatepassAccessoriesList.add(returnableGatepassAccessories);
		returnableGatepassAccessories.setReturnableGatepassDetail(this);

		return returnableGatepassAccessories;
	}
	
	public ReturnableGatepassAccessories updateReturnableGatepassAccessories(ReturnableGatepassAccessories returnableGatepassAccessories) {

		if (returnableGatepassAccessoriesList == null)
			this.returnableGatepassAccessoriesList = new ArrayList<ReturnableGatepassAccessories>();
		returnableGatepassAccessoriesList.add(returnableGatepassAccessories);
		returnableGatepassAccessories.setReturnableGatepassDetail(this);

		return returnableGatepassAccessories;
	}
	public ReturnableGatepassDetail(Integer returnQty, Double totalAmt, Double totalTaxAmount,Double netAmount , Integer userId) {
		this.netAmount = netAmount;
		this.returnQty = returnQty;
		this.totalAmt = totalAmt;
		this.totalTaxAmount = totalTaxAmount;
		//this.updatedBy = this.getUser();
		this.updatedByUser =userId;
		this.updatedDate = TimeZoneComponent.getDateByzone(0);
	}

	public GoodReceiptNoteDetail getGoodReceiptNoteDetail() {
		return goodReceiptNoteDetail;
	}

	public void setGoodReceiptNoteDetail(GoodReceiptNoteDetail goodReceiptNoteDetail) {
		this.goodReceiptNoteDetail = goodReceiptNoteDetail;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public UomType getBonusUomType() {
		return bonusUomType;
	}

	public void setBonusUomType(UomType bonusUomType) {
		this.bonusUomType = bonusUomType;
	}

	

	
	
}
package com.param.entity.model.inventory;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;

@Entity(name = "OpeningBalanceImplantDetail")
@Table(name = "t_opening_balance_implant_detail", schema = "inventory")
public class OpeningBalanceImplantDetail  extends BaseEntity {
	
	public Double getLcop() {
		return lcop;
	}
	public void setLcop(Double lcop) {
		this.lcop = lcop;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "amount_cop")
	private Double amountCop;
	@Column(name = "lcop")
	private Double lcop;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="approved_by")
	private User approvedBy;

	@Column(name = "approved_date")
	private Date approvedDate;

	@Column(name = "approved_quantity")
	private Integer approvedQuantity;

	@Column(name = "batch_no", length = 100)
	private String batchNo;

	private Double cop;

	@Temporal(TemporalType.DATE)
	@Column(name = "expiry_date")
	private Date expiryDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	private Double mrp;

	private Integer quantity;

	@Column(name = "reject_note", length = 100)
	private String rejectNote;

	@Column(name = "reject_quantity")
	private Integer rejectQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reject_reason_id")
	private RejectReason rejectReason;

	@Column(length = 200)
	private String remark;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "status_id")
	private Status status;

	@Column(name = "tax_amount")
	private Double taxAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_id")
	private Tax tax;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manufacture_id")
	private Manufacturer manufacturer;

	@Column(name = "tax_percentage")
	private Double taxPercentage;
	
	@Column(name = "markup_percentage")
	private Double markupPercentage;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mrp_uom_type_id")
	private UomType mrpUomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_type_id")
	private UomType uomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_unit_id")
	private UomUnit uomUnit;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "opening_balance_implant_id")
	private OpeningBalanceImplant openingBalanceImplant;
	
	public OpeningBalanceImplantDetail()
	{
		super();
	}
	//for getById Details Constructor
	public OpeningBalanceImplantDetail(Double amountCop, String batchNo, Double cop, Date expiryDate, Item item, Double mrp, Integer quantity, String remark, 
			Status status, Double taxAmount, Tax tax,Double taxPercentage,Double markupPercentage,Manufacturer manufacturer)
	{
		super();
		this.item = item;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.cop = cop;
		this.amountCop = amountCop;
		this.mrp = mrp;
		this.quantity = quantity;
		this.remark = remark;
		this.status = status;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.markupPercentage=markupPercentage;
		this.manufacturer=manufacturer;

	}
	public OpeningBalanceImplantDetail(Double amountCop, String batchNo, Double cop, Date expiryDate, Item item, Double mrp, Integer quantity, String remark, 
			Status status, Double taxAmount, Tax tax,Double taxPercentage,Double markupPercentage,Manufacturer manufacturer, Double lcop)
	{
		super();
		this.item = item;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.cop = cop;
		this.amountCop = amountCop;
		this.mrp = mrp;
		this.quantity = quantity;
		this.remark = remark;
		this.status = status;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.markupPercentage=markupPercentage;
		this.manufacturer=manufacturer;
		this.lcop= lcop;

	}
	public OpeningBalanceImplantDetail(Double amountCop, Double cop, Item item, Double mrp, Integer quantity, String remark, 
			Status status, Double taxAmount, Tax tax,Double taxPercentage,Double markupPercentage,Manufacturer manufacturer)
	{
		super();
		this.item = item;
		this.cop = cop;
		this.amountCop = amountCop;
		this.mrp = mrp;
		this.quantity = quantity;
		this.remark = remark;
		this.status = status;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.markupPercentage=markupPercentage;
		this.manufacturer=manufacturer;

	}
	
	//for update id is Not null Details Constructor
	public void updateOpeningBalaceImplantDetails(Item item,String batchNo, Date expiryDate,Integer quantity, Double cop, Double mrp, Double amountCop,
													Tax tax, Double taxPercentage,Double taxAmount,Double markupPercentage,String remark, Status status,Manufacturer manufacturer, Double lcop)
	{
		
		this.item=item;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.quantity = quantity;
		this.cop = cop;
		this.mrp = mrp;
		this.amountCop = amountCop;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.taxAmount = taxAmount;
		this.markupPercentage=markupPercentage;
		this.remark = remark;
		this.status = status;
		this.manufacturer=manufacturer;
		this.lcop= lcop;
	}
	
	//for update id is null Details Constructor
	public OpeningBalanceImplantDetail(Item item,String batchNo, Date expiryDate,Integer quantity, Double cop, Double mrp, Double amountCop,
										Tax tax, Double taxPercentage,Double taxAmount,Double markupPercentage, String remark, Status status,Manufacturer manufacturer, Double lcop)
	{
		this.item=item;
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.quantity = quantity;
		this.cop = cop;
		this.mrp = mrp;
		this.amountCop = amountCop;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.taxAmount = taxAmount;
		this.markupPercentage=markupPercentage;
		this.remark = remark;
		this.status = status;
		this.manufacturer=manufacturer;
		this.lcop= lcop;
	}

	public OpeningBalanceImplant getOpeningBalanceImplant() {
		return openingBalanceImplant;
	}

	public void setOpeningBalanceImplant(OpeningBalanceImplant openingBalanceImplant) {
		this.openingBalanceImplant = openingBalanceImplant;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getAmountCop() {
		return amountCop;
	}

	public void setAmountCop(Double amountCop) {
		this.amountCop = amountCop;
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

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public RejectReason getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(RejectReason rejectReason) {
		this.rejectReason = rejectReason;
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

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Tax getTax() {
		return tax;
	}

	public void setTax(Tax tax) {
		this.tax = tax;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public UomType getMrpUomType() {
		return mrpUomType;
	}

	public void setMrpUomType(UomType mrpUomType) {
		this.mrpUomType = mrpUomType;
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

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Double getMarkupPercentage() {
		return markupPercentage;
	}

	public void setMarkupPercentage(Double markupPercentage) {
		this.markupPercentage = markupPercentage;
	}

	}

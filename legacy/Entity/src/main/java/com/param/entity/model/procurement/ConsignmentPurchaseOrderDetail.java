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
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;

@Entity(name = "ConsignmentPurchaseOrderDetail")
@Table(name = "t_consignment_purchase_order_detail", schema = "procurement")
public class ConsignmentPurchaseOrderDetail extends BaseEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="purchase_order_id", nullable = false)
	private ConsignmentPurchaseOrder consignmentPurchaseOrder;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;
	
	@Column(name = "po_quantity", nullable = false)
	private Integer poQuantity;
	
	
	@Column(name = "cop", nullable = false)
	private Double cop;

	@Column(name = "mrp", nullable = false)
	private Double mrp;
	
	@Column(name = "discount")
	private Double discount;
	
	@Column(name = "tax_amount", nullable = false)
	private Double taxAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_id", nullable = false)
	private Tax tax;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id", nullable = false)
	private Batch batch;
	
	@Column(name = "tax_percentage", nullable = false)
	private Double taxPercentage;
	
	@Column(name = "total_amount", nullable = false)
	private Double totalAmount;

	@Column(name = "total_discount")
	private Double totalDiscount;
	
	@Column(name = "net_amount", nullable = false)
	private Double netAmount;
	
	@Column(name = "specification")
	private String specification;
	
	@Column(name = "remark")
	private String remark;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "po_uom_type_id")
	private UomType uomType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "po_uom_unit_id")
	private UomUnit uomUnit;
	
	@Column(name = "po_approved_quantity")
	private Integer poApprovedQuantity;
	
	@Column(name = "bonus_approved_quantity")
	private Integer bonusApprovedQuantity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private User approvedBy;
	
	@Column(name = "approved_date")
	private Date approvedDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;
	
	@Column(name = "rejected_quantity")
	private Integer rejectedQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reject_reason_id")
	private RejectReason rejectReason;

	@Column(name = "reject_note")
	private String rejectNote;
	
	@Column(name = "po_lease_quantity")
	private Integer poLeaseQuantity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "grn_detail_id")
	private GoodReceiptNoteDetail goodReceiptNoteDetail;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "consignmentPurchaseOrderDetail", cascade = CascadeType.ALL)
	private List<ConsignmentPurchaseOrderPatient> consignmentPurchaseOrderPatient;

	
	public List<ConsignmentPurchaseOrderPatient> getConsignmentPurchaseOrderPatient() {
		return consignmentPurchaseOrderPatient;
	}

	public void setConsignmentPurchaseOrderPatient(List<ConsignmentPurchaseOrderPatient> consignmentPurchaseOrderPatient) {
		this.consignmentPurchaseOrderPatient = consignmentPurchaseOrderPatient;
	}

	public ConsignmentPurchaseOrderDetail() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ConsignmentPurchaseOrderDetail(Item itemId, Integer poQuantoty, Double cop, Double mrp, Tax taxId,
			Double taxPercentage, double totalAmount, double totalDiscount, double taxAmount, double netAmount,  UomType uomTypeId,
			UomUnit uomUnitId,Batch batch) {
		this.item=itemId;
		this.poQuantity=poQuantoty;
		this.cop=cop;
		this.mrp=mrp;
		this.tax=taxId;
		this.taxPercentage=taxPercentage;
		this.totalAmount=totalAmount;
		this.totalDiscount=totalDiscount;
		this.taxAmount=taxAmount;
		this.netAmount=netAmount;
		this.uomType=uomTypeId;
		this.uomUnit=uomUnitId;
		this.batch=batch;
		
	}

	public ConsignmentPurchaseOrderDetail(Integer id) {
		this.id=id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ConsignmentPurchaseOrder getConsignmentPurchaseOrder() {
		return consignmentPurchaseOrder;
	}

	public void setConsignmentPurchaseOrder(ConsignmentPurchaseOrder consignmentPurchaseOrder) {
		this.consignmentPurchaseOrder = consignmentPurchaseOrder;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Integer getPoQuantity() {
		return poQuantity;
	}

	public void setPoQuantity(Integer poQuantity) {
		this.poQuantity = poQuantity;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
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

	

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Integer getPoApprovedQuantity() {
		return poApprovedQuantity;
	}

	public void setPoApprovedQuantity(Integer poApprovedQuantity) {
		this.poApprovedQuantity = poApprovedQuantity;
	}

	public Integer getBonusApprovedQuantity() {
		return bonusApprovedQuantity;
	}

	public void setBonusApprovedQuantity(Integer bonusApprovedQuantity) {
		this.bonusApprovedQuantity = bonusApprovedQuantity;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getRejectedQuantity() {
		return rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
	}

	public RejectReason getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(RejectReason rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public Integer getPoLeaseQuantity() {
		return poLeaseQuantity;
	}

	public void setPoLeaseQuantity(Integer poLeaseQuantity) {
		this.poLeaseQuantity = poLeaseQuantity;
	}

	public GoodReceiptNoteDetail getGoodReceiptNoteDetail() {
		return goodReceiptNoteDetail;
	}

	public void setGoodReceiptNoteDetail(GoodReceiptNoteDetail goodReceiptNoteDetail) {
		this.goodReceiptNoteDetail = goodReceiptNoteDetail;
	}
	
	
	 
	  public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	 public ConsignmentPurchaseOrderPatient addConsignmentPurchaseOrderPatient(ConsignmentPurchaseOrderPatient consignmentPurchaseOrderPatient) {
			if (getConsignmentPurchaseOrderPatient() == null)
				this.consignmentPurchaseOrderPatient = new ArrayList<ConsignmentPurchaseOrderPatient>();
			getConsignmentPurchaseOrderPatient().add(consignmentPurchaseOrderPatient);
			consignmentPurchaseOrderPatient.setConsignmentPurchaseOrderDetail(this);

			return consignmentPurchaseOrderPatient;
		}

}

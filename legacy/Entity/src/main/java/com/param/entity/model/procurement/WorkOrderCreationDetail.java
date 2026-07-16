package com.param.entity.model.procurement;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Tax;

import java.sql.Timestamp;


/**
 * The persistent class for the t_work_order_creation_detail database table.
 * 
 */
@Entity
@Table(name="t_work_order_creation_detail", schema = "procurement")
public class WorkOrderCreationDetail extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	private Double amount;

	@Column(name="discount_amount")
	private Double discountAmount;

	@Column(name="discount_percentage")
	private Double discountPercentage;

	@Column(name="exclusion_details")
	private String exclusionDetails;

	@Column(name="inclusion_details")
	private String inclusionDetails;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	@Column(name="net_amount")
	private Double netAmount;

	@Column(name="payment_term")
	private String paymentTerm;

	@Column(name="qo_bonus_quantity")
	private Integer qoBonusQuantity;

	private String remark;

	private String specification;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@Column(name="tax_amount")
	private Double taxAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="tax_id")
	private Tax tax;

	@Column(name="tax_percentage")
	private Double taxPercentage;

	@Column(name="total_amount")
	private Double totalAmount;

	@Column(name="wo_quantity")
	private Integer woQuantity;

	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="work_order_detail_id")
	private WorkOrderRequestDetail workOrderDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="work_order_quotation_detail_id")
	private WorkOrderQuotationRequestDetail workOrderQuotationDetail;

	//bi-directional many-to-one association to TWorkOrderCreation
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="wo_creation_id")
	private WorkOrderCreation workOrderCreation;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id", nullable = false)
	private Batch batch;

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public WorkOrderCreationDetail() {
		super();
	}
	
	public WorkOrderCreationDetail(Double amount, Double discountAmount, Double discountPercentage,
			String exclusionDetails, String inclusionDetails, Item item, Double netAmount, String paymentTerm,
			Integer qoBonusQuantity, String remark, String specification, Status status, Double taxAmount, Tax tax,
			Double taxPercentage, Double totalAmount, Integer woQuantity, WorkOrderRequestDetail workOrderDetail,
			WorkOrderQuotationRequestDetail workOrderQuotationDetail) {
		super();
		this.amount = amount;
		this.discountAmount = discountAmount;
		this.discountPercentage = discountPercentage;
		this.exclusionDetails = exclusionDetails;
		this.inclusionDetails = inclusionDetails;
		this.item = item;
		this.netAmount = netAmount;
		this.paymentTerm = paymentTerm;
		this.qoBonusQuantity = qoBonusQuantity;
		this.remark = remark;
		this.specification = specification;
		this.status = status;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.totalAmount = totalAmount;
		this.woQuantity = woQuantity;
		this.workOrderDetail = workOrderDetail;
		this.workOrderQuotationDetail = workOrderQuotationDetail;
	}
	
	
	//for work order by QR
	public WorkOrderCreationDetail(Item itemId, Batch batch, WorkOrderQuotationRequestDetail woOrQrId, Integer woQty, Integer bonusQty,
			Double amount, Double disPercentage, Double disAmount, Tax taxId, Double taxPercentage,
			Double taxAmount,Double totalAmount, Double netAmount, String inclusionDetails, String exclusionDetails,
			String paymentTerms, String remark, Status status) {
		this.item = itemId;
		this.batch= batch;
		this.workOrderQuotationDetail = woOrQrId;
		this.woQuantity = woQty;
		this.qoBonusQuantity = bonusQty;
		this.amount = amount;
		this.discountPercentage = disPercentage;
		this.discountAmount = disAmount;
		this.tax = taxId;
		this.taxPercentage = taxPercentage;
		this.totalAmount = totalAmount;
		this.taxAmount = taxAmount;
		this.netAmount = netAmount;
		this.exclusionDetails = exclusionDetails;
		this.inclusionDetails = inclusionDetails;
		this.paymentTerm = paymentTerms;
		this.remark = remark;
		this.status = status;
	}
	
	//for work order by WR
		public WorkOrderCreationDetail(Item itemId, Batch batch, WorkOrderRequestDetail woOrQrId, Integer woQty, Integer bonusQty,
				Double amount, Double disPercentage, Double disAmount, Tax taxId, Double taxPercentage,
				Double taxAmount,Double totalAmount, Double netAmount, String inclusionDetails, String exclusionDetails,
				String paymentTerms, String remark, Status status) {
			this.item = itemId;
			this.batch= batch;
			this.workOrderDetail = woOrQrId;
			this.woQuantity = woQty;
			this.qoBonusQuantity = bonusQty;
			this.amount = amount;
			this.discountPercentage = disPercentage;
			this.discountAmount = disAmount;
			this.tax = taxId;
			this.taxPercentage = taxPercentage;
			this.totalAmount = totalAmount;
			this.taxAmount = taxAmount;
			this.netAmount = netAmount;
			this.exclusionDetails = exclusionDetails;
			this.inclusionDetails = inclusionDetails;
			this.paymentTerm = paymentTerms;
			this.remark = remark;
			this.status = status;
			this.totalAmount=totalAmount;
		}
		
		//for work order by Direct Request
				public WorkOrderCreationDetail(Item itemId, Batch batch, Integer woQty, Integer bonusQty,
						Double amount, Double disPercentage, Double disAmount, Tax taxId, Double taxPercentage,
						Double taxAmount,Double totalAmount, Double netAmount, String inclusionDetails, String exclusionDetails,
						String paymentTerms, String remark, Status status) {
					this.item = itemId;
					this.batch= batch;
					this.woQuantity = woQty;
					this.qoBonusQuantity = bonusQty;
					this.amount = amount;
					this.discountPercentage = disPercentage;
					this.discountAmount = disAmount;
					this.tax = taxId;
					this.taxPercentage = taxPercentage;
					this.totalAmount = totalAmount;
					this.taxAmount = taxAmount;
					this.netAmount = netAmount;
					this.exclusionDetails = exclusionDetails;
					this.inclusionDetails = inclusionDetails;
					this.paymentTerm = paymentTerms;
					this.remark = remark;
					this.status = status;
					this.totalAmount=totalAmount;
				}

	public void updateWorkOrderCreationDetail(Double amount, Double discountAmount, Double discountPercentage,
			String exclusionDetails, String inclusionDetails, Double netAmount, String paymentTerm,
			Integer qoBonusQuantity, String remark, String specification,Double taxAmount, 
			Double taxPercentage, Double totalAmount, Integer woQuantity) {
		this.amount = amount;
		this.discountAmount = discountAmount;
		this.discountPercentage = discountPercentage;
		this.exclusionDetails = exclusionDetails;
		this.inclusionDetails = inclusionDetails;
		this.netAmount = netAmount;
		this.paymentTerm = paymentTerm;
		this.qoBonusQuantity = qoBonusQuantity;
		this.remark = remark;
		this.specification = specification;
		this.taxAmount = taxAmount;
		this.taxPercentage = taxPercentage;
		this.totalAmount = totalAmount;
		this.woQuantity = woQuantity;
	}




	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public String getExclusionDetails() {
		return exclusionDetails;
	}

	public void setExclusionDetails(String exclusionDetails) {
		this.exclusionDetails = exclusionDetails;
	}

	public String getInclusionDetails() {
		return inclusionDetails;
	}

	public void setInclusionDetails(String inclusionDetails) {
		this.inclusionDetails = inclusionDetails;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public String getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public Integer getQoBonusQuantity() {
		return qoBonusQuantity;
	}

	public void setQoBonusQuantity(Integer qoBonusQuantity) {
		this.qoBonusQuantity = qoBonusQuantity;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
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

	public double getTaxPercentage() {
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

	public Integer getWoQuantity() {
		return woQuantity;
	}

	public void setWoQuantity(Integer woQuantity) {
		this.woQuantity = woQuantity;
	}

	public WorkOrderRequestDetail getWorkOrderDetail() {
		return workOrderDetail;
	}

	public void setWorkOrderDetail(WorkOrderRequestDetail workOrderDetail) {
		this.workOrderDetail = workOrderDetail;
	}

	public WorkOrderQuotationRequestDetail getWorkOrderQuotationDetail() {
		return workOrderQuotationDetail;
	}

	public void setWorkOrderQuotationDetail(WorkOrderQuotationRequestDetail workOrderQuotationDetail) {
		this.workOrderQuotationDetail = workOrderQuotationDetail;
	}

	public WorkOrderCreation getWorkOrderCreation() {
		return workOrderCreation;
	}

	public void setWorkOrderCreation(WorkOrderCreation workOrderCreation) {
		this.workOrderCreation = workOrderCreation;
	}

	
	
}
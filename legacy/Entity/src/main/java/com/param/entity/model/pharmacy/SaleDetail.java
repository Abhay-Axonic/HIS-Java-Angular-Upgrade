package com.param.entity.model.pharmacy;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
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
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.service.global.model.SaleDetailPackageMapper;

@Entity(name = "SaleDetail")
@Table(name = "t_sale_detail", schema = "pharmacy")
public class SaleDetail extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id", nullable = false)
	private Batch batch;

	@Column(name = "discount_amount")
	private Double discountAmount;

	@Column(name = "lease_quantity")
	private Integer leaseQuantity;

	@Column(name = "net_amount")
	private Double netAmount;

	@Column(name = "credit_payable")
	private Double creditPayable;

	@Column(name = "self_payable")
	private Double selfPayable;

	@Column(name = "payee_id")
	private Integer payeeId;

	@Column(name = "order_detail_id")
	private Integer orderDetailId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prescription_detail_id")
	private PrescriptionDetail prescriptionDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	private Integer quantity;

	private Double rate;

	@Column(name = "item_id")
	private Integer itemId;

	@Column(name = "tax_amount", nullable = false)
	private Double taxAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_id")
	private Tax tax;

	@Column(name = "tax_percentage")
	private Double taxPercentage;

	@Column(name = "total_amount")
	private Double totalAmount;

	@Column(name = "unit_discount_amount")
	private Double unitDiscountAmount;

	@Column(name = "package_consumption_amt")
	private Double packageConsumptionAmt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_unit_id")
	private UomUnit uomUnit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_type_id")
	private UomType uomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sale_id")
	private Sale sale;

	@Column(name = "product_category_id")
	private Integer productCategoryId;

	@Column(name = "consession_amount")
	private Double consessionAmount;

	@Column(name = "unit_consession_amount")
	private Double unitConsessionAmount;
	
	@Column(name = "disallowed_amount")
	private Double disallowedAmount;
	
	@Column(name = "consumed_item_id")
	private Integer consumedItemId;
	
	@Column(name = "consumed_pcategroy_id")
	private Integer consumedPcategroyId;

	@Column(name = "disallowed_against_invoice_item_dtl_id")
	private Integer disallowedAgainstInvoiceItemDtlId;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "saleDetail", cascade = CascadeType.ALL)
	private List<SaleDetailTaxComponenet> saleDetailTaxComponenets;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "saleDetail", cascade = CascadeType.ALL)
	private List<SaleDetailPayeeMapper> saleDetailPayeeMapperList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "saleDetail", cascade = CascadeType.ALL)
	private List<SaleDetailPackageMapper> SaleDetailPackageMapperList;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id",insertable = false, nullable = false, updatable = false)
	private Item item;

	@Column(name = "consession_percentage")
	private Double consessionPercentage;
	
	@Column(name = "discount_percentage")
	private Double discountPercentage;
	
	@Column(name = "instruction_id")
	private Integer instructionId;
	
	@Column(name = "instruction_desc")
	private String instructionDesc;
	
	@Column(name = "old_discount_percentage")
	private Double oldDiscountPercentage;
	
	@Column(name = "at_cop")
	private boolean atCop;
	
	@Column(name = "is_dummy_service")
	private boolean isDummyService;

    @Column(name = "grn_id")
    private Integer grnId;

    @Column (name = "grn_number")
    private String grnNumber;

    @Column (name = "itemNetAmount")
    private Double itemNetAmount;
	
	public Double getOldDiscountPercentage() {
		return oldDiscountPercentage;
	}

	public void setOldDiscountPercentage(Double oldDiscountPercentage) {
		this.oldDiscountPercentage = oldDiscountPercentage;
	}

	public Double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public Double getConsessionPercentage() {
		return consessionPercentage == null ? 0.00 : consessionPercentage;
	}

	public void setConsessionPercentage(Double consessionPercentage) {
		this.consessionPercentage = consessionPercentage == null ? 0.00 : consessionPercentage;
	}

	public Double getConsessionAmount() {
		return consessionAmount == null ? 0.00 : consessionAmount;
	}

	public void setConsessionAmount(Double consessionAmount) {
		this.consessionAmount = consessionAmount == null ? 0.00 : consessionAmount;
	}

	public Double getUnitConsessionAmount() {
		return unitConsessionAmount == null ? 0.00 : unitConsessionAmount;
	}

	public void setUnitConsessionAmount(Double unitConsessionAmount) {
		this.unitConsessionAmount = unitConsessionAmount == null ? 0.00 : unitConsessionAmount;
	}

	public SaleDetail() {
		super();
	}

	public SaleDetail(Long id) {
		super();
		this.id = id;
	}

	public List<SaleDetailTaxComponenet> getSaleDetailTaxComponenets() {
		return saleDetailTaxComponenets;
	}

	public void setSaleDetailTaxComponenets(List<SaleDetailTaxComponenet> saleDetailTaxComponenets) {
		this.saleDetailTaxComponenets = saleDetailTaxComponenets;
	}

	public SaleDetail(Batch batch, Integer leaseQuantity, Double netAmount, Status status, Integer quantity,
			Double rate, Double taxAmount, Tax tax, Double taxPercentage, Double totalAmount, Sale sale,
			UomType uomType, UomUnit uomUnit, Double selfPayable, Double creditPayable, Integer payeeId,
			Integer productCategoryId, Double packageConsumptionAmt) {
		super();
		this.batch = batch;
		this.leaseQuantity = leaseQuantity;
		this.netAmount = netAmount;
		this.status = status;
		this.quantity = quantity;
		this.rate = rate;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.totalAmount = totalAmount;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.selfPayable = selfPayable;
		this.creditPayable = creditPayable;
		this.payeeId = payeeId;
		this.productCategoryId = productCategoryId;
		this.packageConsumptionAmt = packageConsumptionAmt;
	}

	public SaleDetail(Batch batch, Integer leaseQuantity, Double netAmount, Integer itemId, Status status,
			Integer quantity, Double rate, Double taxAmount, Tax tax, Double taxPercentage, Double totalAmount,
			Sale sale, UomType uomType, UomUnit uomUnit, Double selfPayable, Double creditPayable, Integer payeeId,
			Integer productCategoryId, Double packageConsumptionAmt) {
		super();
		this.batch = batch;
		this.leaseQuantity = leaseQuantity;
		this.netAmount = netAmount;
		this.itemId = itemId;
		this.status = status;
		this.quantity = quantity;
		this.rate = rate;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.totalAmount = totalAmount;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.selfPayable = selfPayable;
		this.creditPayable = creditPayable;
		this.payeeId = payeeId;
		this.productCategoryId = productCategoryId;
		this.packageConsumptionAmt = packageConsumptionAmt;
	}

	// Ganesh
	public SaleDetail(Batch batch, Double discountAmount, Integer leaseQuantity, Double netAmount,
			Integer orderDetailId, PrescriptionDetail prescriptionDetail, Integer quantity, Double rate,
			Double taxAmount, Tax tax, Double taxPercentage, Double totalAmount, Double unitDiscountAmount,
			UomType uomType, UomUnit uomUnit, Status status, Double selfPayable, Double creditPayable, Integer payeeId,
			Integer productCategoryId, Double packageConsumptionAmt) {
		super();
		this.batch = batch;
		this.discountAmount = discountAmount;
		this.leaseQuantity = leaseQuantity;
		this.netAmount = netAmount;
		this.orderDetailId = orderDetailId;
		this.prescriptionDetail = prescriptionDetail;
		this.quantity = quantity;
		this.rate = rate;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.totalAmount = totalAmount;
		this.unitDiscountAmount = unitDiscountAmount;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.status = status;
		this.selfPayable = selfPayable;
		this.creditPayable = creditPayable;
		this.payeeId = payeeId;
		this.productCategoryId = productCategoryId;
		this.packageConsumptionAmt = packageConsumptionAmt;
	}

	public SaleDetail(Long id, Batch batch, Double discountAmount, Integer leaseQuantity, Double netAmount,
			Integer orderDetailId, PrescriptionDetail prescriptionDetail, Integer quantity, Double rate,
			Double taxAmount, Tax tax, Double taxPercentage, Double totalAmount, Double unitDiscountAmount,
			UomType uomType, UomUnit uomUnit, Status status, Double selfPayable, Double creditPayable, Integer payeeId,
			Integer productCategoryId, Sale sale, Double packageConsumptionAmt) {
		this.id = id;
		this.batch = batch;
		this.discountAmount = discountAmount;
		this.leaseQuantity = leaseQuantity;
		this.netAmount = netAmount;
		this.orderDetailId = orderDetailId;
		this.prescriptionDetail = prescriptionDetail;
		this.quantity = quantity;
		this.rate = rate;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.totalAmount = totalAmount;
		this.unitDiscountAmount = unitDiscountAmount;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.status = status;
		this.selfPayable = selfPayable;
		this.creditPayable = creditPayable;
		this.payeeId = payeeId;
		this.productCategoryId = productCategoryId;
		this.sale = sale;
		this.packageConsumptionAmt = packageConsumptionAmt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public Double getDiscountAmount() {
		return discountAmount == null ? 0 : discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount == null ? 0 : discountAmount;
	}

	public Integer getLeaseQuantity() {
		return leaseQuantity;
	}

	public void setLeaseQuantity(Integer leaseQuantity) {
		this.leaseQuantity = leaseQuantity;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Integer getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public PrescriptionDetail getPrescriptionDetail() {
		return prescriptionDetail;
	}

	public void setPrescriptionDetail(PrescriptionDetail prescriptionDetail) {
		this.prescriptionDetail = prescriptionDetail;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getTaxAmount() {
		return this.taxAmount;
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
		return taxPercentage==null?0.00:taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage==null?0.00:taxPercentage;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getUnitDiscountAmount() {
		return unitDiscountAmount == null ? 0.00 : unitDiscountAmount;
	}

	public void setUnitDiscountAmount(Double unitDiscountAmount) {
		this.unitDiscountAmount = unitDiscountAmount == null ? 0.00 : unitDiscountAmount;
	}

	public UomUnit getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(UomUnit uomUnit) {
		this.uomUnit = uomUnit;
	}

	public Sale getSale() {
		return sale;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	public UomType getUomType() {
		return uomType;
	}

	public void setUomType(UomType uomType) {
		this.uomType = uomType;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public SaleDetailTaxComponenet addSaleDetailTaxComponenet(SaleDetailTaxComponenet saleDetailTaxComponenet) {
		if (this.saleDetailTaxComponenets == null)
			this.saleDetailTaxComponenets = new ArrayList<SaleDetailTaxComponenet>();

		this.saleDetailTaxComponenets.add(saleDetailTaxComponenet);
		saleDetailTaxComponenet.setSaleDetail(this);
		return saleDetailTaxComponenet;
	}

	public SaleDetailPayeeMapper addSaleDetailPayeeMapper(SaleDetailPayeeMapper saleDetailPayeeMapper) {
		if (this.saleDetailPayeeMapperList == null)
			this.saleDetailPayeeMapperList = new ArrayList<SaleDetailPayeeMapper>();

		getSaleDetailPayeeMapperList().add(saleDetailPayeeMapper);
		saleDetailPayeeMapper.setSaleDetail(this);
		return saleDetailPayeeMapper;
	}

	public SaleDetailPayeeMapper removeSaleDetailPayeeMapper(SaleDetailPayeeMapper saleDetailPayeeMapper) {
		getSaleDetailPayeeMapperList().remove(saleDetailPayeeMapper);
		saleDetailPayeeMapper.setSaleDetail(null);
		return saleDetailPayeeMapper;
	}

	public SaleDetailPackageMapper removeSaleDetailPackageMapper(SaleDetailPackageMapper saleDetailPackageMapper) {
		getSaleDetailPackageMapperList().remove(saleDetailPackageMapper);
		saleDetailPackageMapper.setSaleDetail(null);
		return saleDetailPackageMapper;
	}

	public SaleDetailPackageMapper addSaleDetailPackageMapper(SaleDetailPackageMapper saleDetailPackageMapper) {
		if (this.SaleDetailPackageMapperList == null)
			this.SaleDetailPackageMapperList = new ArrayList<SaleDetailPackageMapper>();

		getSaleDetailPackageMapperList().add(saleDetailPackageMapper);
		saleDetailPackageMapper.setSaleDetail(this);
		return saleDetailPackageMapper;
	}

	public Double getCreditPayable() {
		return creditPayable;
	}

	public void setCreditPayable(Double creditPayable) {
		this.creditPayable = creditPayable;
	}

	public Double getSelfPayable() {
		return selfPayable;
	}

	public void setSelfPayable(Double selfPayable) {
		this.selfPayable = selfPayable;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public List<SaleDetailPayeeMapper> getSaleDetailPayeeMapperList() {
		return saleDetailPayeeMapperList;
	}

	public void setSaleDetailPayeeMapperList(List<SaleDetailPayeeMapper> saleDetailPayeeMapperList) {
		this.saleDetailPayeeMapperList = saleDetailPayeeMapperList;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public List<SaleDetailPackageMapper> getSaleDetailPackageMapperList() {
		return SaleDetailPackageMapperList;
	}

	public void setSaleDetailPackageMapperList(List<SaleDetailPackageMapper> saleDetailPackageMapperList) {
		SaleDetailPackageMapperList = saleDetailPackageMapperList;
	}

	public Double getPackageConsumptionAmt() {
		return packageConsumptionAmt;
	}

	public void setPackageConsumptionAmt(Double packageConsumptionAmt) {
		this.packageConsumptionAmt = packageConsumptionAmt;
	}

	public Integer getDisallowedAgainstInvoiceItemDtlId() {
		return disallowedAgainstInvoiceItemDtlId;
	}

	public void setDisallowedAgainstInvoiceItemDtlId(Integer disallowedAgainstInvoiceItemDtlId) {
		this.disallowedAgainstInvoiceItemDtlId = disallowedAgainstInvoiceItemDtlId;
	}

	public Double getDisallowedAmount() {
		return disallowedAmount;
	}

	public void setDisallowedAmount(Double disallowedAmount) {
		this.disallowedAmount = disallowedAmount;
	}

	public Integer getConsumedItemId() {
		return consumedItemId;
	}

	public void setConsumedItemId(Integer consumedItemId) {
		this.consumedItemId = consumedItemId;
	}

	public Integer getConsumedPcategroyId() {
		return consumedPcategroyId;
	}

	public void setConsumedPcategroyId(Integer consumedPcategroyId) {
		this.consumedPcategroyId = consumedPcategroyId;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Integer getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(Integer instructionId) {
		this.instructionId = instructionId;
	}

	public String getInstructionDesc() {
		return instructionDesc;
	}

	public void setInstructionDesc(String instructionDesc) {
		this.instructionDesc = instructionDesc;
	}

	public boolean getAtCop() {
		return atCop;
	}

	public void setAtCop(boolean atCop) {
		this.atCop = atCop;
	}

	public boolean getIsDummyService() {
		return isDummyService;
	}

	public void setIsDummyService(boolean isDummyService) {
		this.isDummyService = isDummyService;
	}

    public Integer getGrnId() {
        return grnId;
    }

    public void setGrnId(Integer grnId) {
        this.grnId = grnId;
    }

    public String getGrnNumber() {
        return grnNumber;
    }

    public void setGrnNumber(String grnNumber) {
        this.grnNumber = grnNumber;
    }

    public Double getItemNetAmount() {
        return itemNetAmount;
    }

    public void setItemNetAmount(Double itemNetAmount) {
        this.itemNetAmount = itemNetAmount;
    }
}
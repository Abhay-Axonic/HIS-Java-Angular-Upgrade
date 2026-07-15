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
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;

@Entity(name = "ConsignmentStockPODetail")
@Table(name = "t_consignment_stock_purchase_order_detail", schema = "procurement")
public class ConsignmentStockPODetail extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "purchase_order_id")
	private ConsignmentStockPO consignmentPurchaseOrder;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	@Column(name = "po_quantity")
	private Integer poQuantity;

	@Column(name = "bonus_quantity")
	private Integer bonusQuantity;

	@Column(name = "cop")
	private Double cop;

	@Column(name = "mrp")
	private Double mrp;

	@Column(name = "discount")
	private Double discount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_id", nullable = false)
	private Tax tax;

	@Column(name = "tax_percentage")
	private Double taxPercentage;

	@Column(name = "total_amount")
	private Double totalAmount;
	
	@Column(name = "total_discount")
	private Double totalDiscount;

	@Column(name = "tax_amount")
	private Double taxAmount;

	@Column(name = "total_other_amount")
	private Double totalOtherAmount;

	@Column(name = "net_amount")
	private Double netAmount;	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manufacturer_id")
	private Manufacturer manufacturer;

	@Column(name = "specification")
	private String specification;

	@Column(name = "remark")
	private String remark;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "po_uom_type_id")
	private UomType poUomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "po_uom_unit_id")
	private UomUnit poUomUnit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bonus_uom_type_id")
	private UomType bonusUomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bonus_uom_unit_id")
	private UomUnit bonusUomUnit;

	@Column(name = "po_approved_quantity")
	private Integer poApprovedQuantity;
	
	@Column(name = "bonus_approved_quantity")
	private Integer bonusApprovedQuantity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private User approvedBy;
	
	@Column(name="approved_date")
	private Date approvedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;
	
	@Column(name = "rejected_quantity")
	private Integer rejectedQuantity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reject_reason_id")
	private RejectReason rejectReason;
	
	@Column(name = "reject_note")
	private String rejectNote;
	
	@Column(name = "warrenty_period")
	private String warrantyPeriod;
	
	@Column(name = "support_terms")
	private String supportTerms;
	
	@Column(name = "technical_specification")
	private String technicalSpecification;
	
	@Column(name = "expected_payment")
	private String expectedPayment;
	
	@Column(name="warrenty_date")
	private Date warrantyDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_provider_id")
	private Vendor serviceProvider;
	
	@Column(name = "po_lease_quantity")
	private Integer poLeaseQuantity;
	
	@Column(name = "bonus_lease_quantity")
	private Integer bonusLeaseQuantity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "grn_detail_id")
	private GoodReceiptNoteDetail grnDetail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pr_detail_id")
	private PurchaseRequestDetail putchRequestDetail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qr_detail_id")
	private QuotationRequestDetail quotationRequestDetail;
	
	public PurchaseRequestDetail getPutchRequestDetail() {
		return putchRequestDetail;
	}

	public void setPutchRequestDetail(PurchaseRequestDetail putchRequestDetail) {
		this.putchRequestDetail = putchRequestDetail;
	}

	/*@OneToMany(fetch = FetchType.LAZY, mappedBy = "purchaseOrderDetail", cascade = CascadeType.ALL)
	private List<PurchaseOrderUomMapper> purchaseOrderUomMapperList;*/
	
	/*@OneToMany(fetch = FetchType.LAZY, mappedBy = "purchaseOrderDetail", cascade = CascadeType.ALL)
	private List<BonusUomMapper> bonusUomMapperList;*/
	
	/*@OneToMany(fetch = FetchType.LAZY, mappedBy = "purchaseOrderDetail", cascade = CascadeType.ALL)
	private List<PurchaseOrderOtherChargesMapper> poOtherChargesList;*/
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "consignmentPurchaseOrderDetail", cascade = CascadeType.ALL)
	private List<ConsignmentStockPOStaged> consignmentPurchaseOrderStagedList;
	
	/*@OneToMany(fetch = FetchType.LAZY, mappedBy = "purchaseOrderDetail", cascade = CascadeType.ALL)
	private List<PurchaseRequestDetail> purchaseRequestDetailList;*/
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "consignmentPurchaseOrderDetail", cascade = CascadeType.ALL)
	private List<ConsignmentStockPOAccessoriesMapper> consignmentPOAccessoriesMapperList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "consignmentPurchaseOrderDetail", cascade = CascadeType.ALL)
	private List<GoodReceiptNoteDetail> grnDetailsList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "purchaseOrderDetail", cascade = CascadeType.ALL)
	private List<GRNStagedQuantity> grnStagedQuantities;

	public ConsignmentStockPODetail() {
		super();
		
	}
	
	public ConsignmentStockPODetail(Integer id) {
		this.id = id;
	}
	
	

	public ConsignmentStockPODetail(Item item, Integer poQuantity, Integer bonusQuantity,
			Double cop, Double mrp, Double discount, Tax tax, Double taxPercentage, Double totalAmount,
			Double totalDiscount, Double taxAmount, Double totalOtherAmount, Double netAmount,
			Manufacturer manufacturer, String specification, String remark, UomType poUomType, UomUnit poUomUnit,
			UomType bonusUomType, UomUnit bonusUomUnit, Status status) {
		super();
		this.item = item;
		this.poQuantity = poQuantity;
		this.bonusQuantity = bonusQuantity;
		this.cop = cop;
		this.mrp = mrp;
		this.discount = discount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.totalAmount = totalAmount;
		this.totalDiscount = totalDiscount;
		this.taxAmount = taxAmount;
		this.totalOtherAmount = totalOtherAmount;
		this.netAmount = netAmount;
		this.manufacturer = manufacturer;
		this.specification = specification;
		this.remark = remark;
		this.poUomType = poUomType;
		this.poUomUnit = poUomUnit;
		this.bonusUomType = bonusUomType;
		this.bonusUomUnit = bonusUomUnit;
		this.status = status;
	}
	
	public ConsignmentStockPODetail(Item item, Integer poQuantity, Integer bonusQuantity, Double cop, Double mrp,
			Double discount, Tax tax, Double taxPercentage, Double totalAmount, Double totalDiscount, Double taxAmount,
			Double totalOtherAmount, Double netAmount, Manufacturer manufacturer, String specification, String remark,
			Status status, String warrantyPeriod, String supportTerms, String technicalSpecification,
			String expectedPayment,Date warrantyDate,Vendor serviceProvider) {
		super();
		this.item = item;
		this.poQuantity = poQuantity;
		this.bonusQuantity = bonusQuantity;
		this.cop = cop;
		this.mrp = mrp;
		this.discount = discount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.totalAmount = totalAmount;
		this.totalDiscount = totalDiscount;
		this.taxAmount = taxAmount;
		this.totalOtherAmount = totalOtherAmount;
		this.netAmount = netAmount;
		this.manufacturer = manufacturer;
		this.specification = specification;
		this.remark = remark;
		this.status = status;
		this.warrantyPeriod = warrantyPeriod;
		this.supportTerms = supportTerms;
		this.technicalSpecification = technicalSpecification;
		this.expectedPayment = expectedPayment;
		this.warrantyDate = warrantyDate;
		this.serviceProvider= serviceProvider;
	}

	public List<GRNStagedQuantity> getGrnStagedQuantities() {
		return grnStagedQuantities;
	}

	public void setGrnStagedQuantities(List<GRNStagedQuantity> grnStagedQuantities) {
		this.grnStagedQuantities = grnStagedQuantities;
	}

	public void updatePurchaseOrderDetail(Item item, Integer poQuantity, Integer bonusQuantity,
			Double cop, Double mrp, Double discount, Double taxPercentage, Double totalAmount,
			Double totalDiscount, Double taxAmount, Double totalOtherAmount, Double netAmount,
			Manufacturer manufacturer, String specification, String remark, UomType poUomType, UomUnit poUomUnit,
			UomType bonusUomType, UomUnit bonusUomUnit,Integer poLeaseQuantity,Integer bonusLeaseQuantity, Integer poApprovedQuantity) {
		
		this.item = item;
		this.poQuantity = poQuantity;
		this.bonusQuantity = bonusQuantity;
		this.cop = cop;
		this.mrp = mrp;
		this.discount = discount;
		this.taxPercentage = taxPercentage;
		this.totalAmount = totalAmount;
		this.totalDiscount = totalDiscount;
		this.taxAmount = taxAmount;
		this.totalOtherAmount = totalOtherAmount;
		this.netAmount = netAmount;
		this.manufacturer = manufacturer;
		this.specification = specification;
		this.remark = remark;
		this.poUomType = poUomType;
		this.poUomUnit = poUomUnit;
		this.bonusUomType = bonusUomType;
		this.bonusUomUnit = bonusUomUnit;
		this.poLeaseQuantity = poLeaseQuantity;
		this.bonusLeaseQuantity = bonusLeaseQuantity;
		this.poApprovedQuantity =  poApprovedQuantity;
		
	}
	
	public void updatePurchaseOrderDetail(Item item, Integer poQuantity, Integer bonusQuantity,
			Double cop, Double mrp, Double discount, Double taxPercentage, Double totalAmount,
			Double totalDiscount, Double taxAmount, Double totalOtherAmount, Double netAmount,
			Manufacturer manufacturer, String specification, String remark, String warrantyPeriod, String supportTerms, String technicalSpecification,
			String expectedPayment,Date warrantyDate,Vendor serviceProvider, Integer poApprovedQuantity) {
		
		this.item = item;
		this.poQuantity = poQuantity;
		this.bonusQuantity = bonusQuantity;
		this.cop = cop;
		this.mrp = mrp;
		this.discount = discount;
		this.taxPercentage = taxPercentage;
		this.totalAmount = totalAmount;
		this.totalDiscount = totalDiscount;
		this.taxAmount = taxAmount;
		this.totalOtherAmount = totalOtherAmount;
		this.netAmount = netAmount;
		this.manufacturer = manufacturer;
		this.specification = specification;
		this.remark = remark;
		this.warrantyPeriod = warrantyPeriod;
		this.supportTerms = supportTerms;
		this.technicalSpecification = technicalSpecification;
		this.expectedPayment = expectedPayment;
		this.warrantyDate = warrantyDate;
		this.serviceProvider= serviceProvider;
		this.poApprovedQuantity = poApprovedQuantity;
		
	}
	
	
	
	public void approvePurchaseOrderDetail( Integer poApprovedQuantity,
			Integer bonusApprovedQuantity, Integer rejectedQuantity, RejectReason rejectReason, String rejectNote,Status status,
			User approvedBy, Date approvedDate,String remark,Integer poLeaseQuantity,Integer bonusLeaseQuantity) {		
		this.poApprovedQuantity = poApprovedQuantity;
		this.bonusApprovedQuantity = bonusApprovedQuantity;		
		this.rejectedQuantity = rejectedQuantity;
		this.rejectReason = rejectReason;
		this.rejectNote = rejectNote;
		this.status = status;
		this.approvedBy = approvedBy;
		this.approvedDate = approvedDate;
		this.remark = remark;
		this.poLeaseQuantity = poLeaseQuantity;
		this.bonusLeaseQuantity = bonusLeaseQuantity;
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ConsignmentStockPO getPurchaseOrder() {
		return consignmentPurchaseOrder;
	}

	public void setPurchaseOrder(ConsignmentStockPO purchaseOrder) {
		this.consignmentPurchaseOrder = purchaseOrder;
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

	public Integer getBonusQuantity() {
		return bonusQuantity;
	}

	public void setBonusQuantity(Integer bonusQuantity) {
		this.bonusQuantity = bonusQuantity;
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

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
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

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
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

	public UomType getPoUomType() {
		return poUomType;
	}

	public void setPoUomType(UomType poUomType) {
		this.poUomType = poUomType;
	}

	public UomUnit getPoUomUnit() {
		return poUomUnit;
	}

	public void setPoUomUnit(UomUnit poUomUnit) {
		this.poUomUnit = poUomUnit;
	}

	public UomType getBonusUomType() {
		return bonusUomType;
	}

	public void setBonusUomType(UomType bonusUomType) {
		this.bonusUomType = bonusUomType;
	}

	public UomUnit getBonusUomUnit() {
		return bonusUomUnit;
	}

	public void setBonusUomUnit(UomUnit bonusUomUnit) {
		this.bonusUomUnit = bonusUomUnit;
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
	
	public String getwarrantyPeriod() {
		return warrantyPeriod;
	}

	public void setwarrantyPeriod(String warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}

	public String getSupportTerms() {
		return supportTerms;
	}

	public void setSupportTerms(String supportTerms) {
		this.supportTerms = supportTerms;
	}

	public String getTechnicalSpecification() {
		return technicalSpecification;
	}

	public void setTechnicalSpecification(String technicalSpecification) {
		this.technicalSpecification = technicalSpecification;
	}

	public String getExpectedPayment() {
		return expectedPayment;
	}
	
	public Date getwarrantyDate() {
		return warrantyDate;
	}

	public void setwarrantyDate(Date warrantyDate) {
		this.warrantyDate = warrantyDate;
	}

	public Vendor getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(Vendor serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public void setExpectedPayment(String expectedPayment) {
		this.expectedPayment = expectedPayment;
	}
	
	/*public List<PurchaseOrderUomMapper> getPurchaseOrderUomMapperList() {
		return purchaseOrderUomMapperList;
	}
	public void setPurchaseOrderUomMapperList(List<PurchaseOrderUomMapper> purchaseOrderUomMapperList) {
		this.purchaseOrderUomMapperList = purchaseOrderUomMapperList;
	}
	public List<BonusUomMapper> getBonusUomMapperList() {
		return bonusUomMapperList;
	}
	public void setBonusUomMapperList(List<BonusUomMapper> bonusUomMapperList) {
		this.bonusUomMapperList = bonusUomMapperList;
	}
		
	public List<PurchaseRequestDetail> getPurchaseRequestDetailList() {
		return purchaseRequestDetailList;
	}

	public void setPurchaseRequestDetailList(List<PurchaseRequestDetail> purchaseRequestDetailList) {
		this.purchaseRequestDetailList = purchaseRequestDetailList;
	}
	*/
	public List<ConsignmentStockPOStaged> getPurchaseOrderStagedList() {
		return consignmentPurchaseOrderStagedList;
	}
	public void setPurchaseOrderStagedList(List<ConsignmentStockPOStaged> purchaseOrderStagedList) {
		this.consignmentPurchaseOrderStagedList = purchaseOrderStagedList;
	}

	public List<ConsignmentStockPOAccessoriesMapper> getPoAccessoriesMapperList() {
		return consignmentPOAccessoriesMapperList;
	}

	public void setPoAccessoriesMapperList(List<ConsignmentStockPOAccessoriesMapper> poAccessoriesMapperList) {
		this.consignmentPOAccessoriesMapperList = poAccessoriesMapperList;
	}

	public List<GoodReceiptNoteDetail> getGrnDetailsList() {
		return grnDetailsList;
	}

	public void setGrnDetailsList(List<GoodReceiptNoteDetail> grnDetailsList) {
		this.grnDetailsList = grnDetailsList;
	}

	public Integer getPoLeaseQuantity() {
		return poLeaseQuantity;
	}

	public void setPoLeaseQuantity(Integer poLeaseQuantity) {
		this.poLeaseQuantity = poLeaseQuantity;
	}

	public Integer getBonusLeaseQuantity() {
		return bonusLeaseQuantity;
	}

	public void setBonusLeaseQuantity(Integer bonusLeaseQuantity) {
		this.bonusLeaseQuantity = bonusLeaseQuantity;
	}
	
	public GoodReceiptNoteDetail getGrnDetail() {
		return grnDetail;
	}

	public void setGrnDetail(GoodReceiptNoteDetail grnDetail) {
		this.grnDetail = grnDetail;
	}
	
	public QuotationRequestDetail getQuotationRequestDetail() {
		return quotationRequestDetail;
	}

	public void setQuotationRequestDetail(QuotationRequestDetail quotationRequestDetail) {
		this.quotationRequestDetail = quotationRequestDetail;
	}

	public ConsignmentStockPOAccessoriesMapper addPoAccessoriesMapper(ConsignmentStockPOAccessoriesMapper poAccessoriesMapper) {
		if (getPoAccessoriesMapperList() == null)
			this.consignmentPOAccessoriesMapperList = new ArrayList<ConsignmentStockPOAccessoriesMapper>();

		getPoAccessoriesMapperList().add(poAccessoriesMapper);
		poAccessoriesMapper.setPurchaseOrderDetail(this);
		return poAccessoriesMapper;
	}
	
	public ConsignmentStockPOStaged addPurchaseOrderStaged(ConsignmentStockPOStaged purchaseOrderStaged) {
		if (getPurchaseOrderStagedList() == null)
			this.consignmentPurchaseOrderStagedList = new ArrayList<ConsignmentStockPOStaged>();

		getPurchaseOrderStagedList().add(purchaseOrderStaged);
		purchaseOrderStaged.setPurchaseOrderDetail(this);
		return purchaseOrderStaged;
	}
	

	/*public PurchaseRequestDetail addPurchaseRequestDetailList(PurchaseRequestDetail purchaseRequestDetail) {
		if (getPurchaseRequestDetailList() == null)
			this.purchaseRequestDetailList = new ArrayList<PurchaseRequestDetail>();

		getPurchaseRequestDetailList().add(purchaseRequestDetail);
		purchaseRequestDetail.setPurchaseOrderDetail(this);

		return purchaseRequestDetail;
	}*/
	
	/*public PurchaseOrderUomMapper addPurchaseOrderUomMapperList(PurchaseOrderUomMapper purchaseOrderUomMapper) {
		if (getPurchaseOrderUomMapperList() == null)
			this.purchaseOrderUomMapperList = new ArrayList<PurchaseOrderUomMapper>();		
		getPurchaseOrderUomMapperList().add(purchaseOrderUomMapper);
		purchaseOrderUomMapper.setPurchaseOrderDetail(this);

		return purchaseOrderUomMapper;
	}*/
	
	/*public BonusUomMapper addBonusUomMapperList(BonusUomMapper bonusUomMapper) {
		if (getBonusUomMapperList() == null)
			this.bonusUomMapperList = new ArrayList<BonusUomMapper>();
		
		getBonusUomMapperList().add(bonusUomMapper);
		bonusUomMapper.setPurchaseOrderDetail(this);

		return bonusUomMapper;
	}*/
	
	
}

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
import javax.persistence.Transient;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;
@Entity(name = "GoodReceiptNoteDetailLog")
@Table(name = "t_good_receipt_note_detail_log", schema = "procurement")
public class GoodReceiptNoteDetailLog extends BaseEntity implements Serializable  {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "batch_no")
	private String batchNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private User approvedBy;

	@Column(name = "approved_date")
	private Date approvedDate;

	@Column(name = "grn_approved_quantity")
	private Integer grnApprovedQuantity;
	
	@Column(name = "pending_quantity")
	private Integer pendingQuantity;

	public Integer getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	@Column(name = "bonus_approved_quantity")
	private Integer bonusApprovedQuantity;

	@Column(name = "grn_received_quantity")
	private Integer grnReceivedQuantity;
	@Column(name = "lcop")
	private Double lcop;

	public Double getLcop() {
		return lcop;
	}

	public void setLcop(Double lcop) {
		this.lcop = lcop;
	}

	@Column(name = "po_details_id")
	private Integer poDetailsId;

	@Column(name = "bonus_received_quantity")
	private Integer bonusReceivedQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bonus_uom_type_id")
	private UomType bonusUomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bonus_uom_unit_id")
	private UomUnit bonusUomUnit;

	private Double cop;

	

	@Temporal(TemporalType.DATE)
	@Column(name = "expiry_date")
	private Date expiryDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "grn_uom_type_id")
	private UomType grnUomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "grn_uom_unit_id")
	private UomUnit grnUomUnit;

	@Column(name = "lot_number")
	private String lotNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manufacturer_id")
	private Manufacturer manufacturer;

	private Double mrp;

	@Column(name = "net_amount")
	private Double netAmount;

	private String remark;

	private String specification;

	@Column(name = "status_id")
	private Integer statusId;

	@Column(name = "tax_amount")
	private Double taxAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_id", nullable = false)
	private Tax tax;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id")
	private Batch batch;

	@Column(name = "tax_percentage")
	private Double taxPercentage;

	@Column(name = "total_amount")
	private Double totalAmount;

	@Column(name = "total_discount")
	private Double totalDiscount;
	
	@Column(name = "discount")
	private Double discount;
	
	@Column(name = "credit_note_amount")
	private Double creditNoteAmount;

	@Column(name = "item_id")
	private Integer itemId;
	
	public Double getCreditNoteAmount() {
		return creditNoteAmount;
	}

	public void setCreditNoteAmount(Double creditNoteAmount) {
		this.creditNoteAmount = creditNoteAmount;
	}

	@Column(name = "total_other_amount")
	private Double totalOtherAmount;

	@Column(name = "rejected_quantity")
	private Integer rejectedQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reject_reason_id")
	private RejectReason rejectReason;

	@Column(name = "reject_note")
	private String rejectNote;

	@Column(name = "grn_id")
	private Integer grnId;

	@Column(name = "grn_lease_quantity")
	private Integer grnLeaseQuantity;

	@Column(name = "bonus_lease_quantity")
	private Integer bonusLeaseQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "po_detail_id")
	private PurchaseOrderDetail purchaseOrderDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "consignment_po_detail_id")
	private ConsignmentPurchaseOrderDetail consignmentPurchaseOrderDetail;
	
	@Column(name = "model_number", length = 50)
	private String modelNumber;

	@Column(name = "warrenty_period")
	private String warrentyPeriod;

	@Column(name = "support_term")
	private String supportTerm;

	@Column(name = "technical_specification")
	private String technicalSpec;

	@Column(name = "expected_payment_terms")
	private String expectedPayment;
	
	@Column(name = "round_off_amount")
	private Double roundOffAmount;

	
	@Column(name = "total_net_amount")
	private Double totalNetAmount;
	
	public Double getTotalNetAmount() {
		return totalNetAmount;
	}

	public void setTotalNetAmount(Double totalNetAmount) {
		this.totalNetAmount = totalNetAmount;
	}

	public Double getRoundOffAmount() {
		return roundOffAmount;
	}

	public void setRoundOffAmount(Double roundOffAmount) {
		this.roundOffAmount = roundOffAmount;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_provider_id")
	private Vendor vendor;

	@Temporal(TemporalType.DATE)
	@Column(name = "warrenty_date_to")
	private Date warrantyDateTo;
	
	
	@Temporal(TemporalType.DATE)
	@Column(name = "warrenty_date")
	private Date warrentyDate;

	public Date getWarrantyDateTo() {
		return warrantyDateTo;
	}

	public void setWarrantyDateTo(Date warrantyDateTo) {
		this.warrantyDateTo = warrantyDateTo;
	}

	@Column(name = "manufacture_year")
	private Integer manufactureYear;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "goodReceiptNoteDetail", cascade = CascadeType.ALL)
	private List<GrnBonusUomMapper> grnBonusUomMappersList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "goodReceiptNoteDetail", cascade = CascadeType.ALL)
	private List<GrnUomMapper> grnUomMappersList;

	/*@OneToMany(fetch = FetchType.LAZY, mappedBy = "goodReceiptNoteDetail", cascade = CascadeType.ALL)
	private List<GrnOtherChargesMapper> grnOtherChargesMapperList;*/

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "goodReceiptNoteDetail", cascade = CascadeType.ALL)
	private List<GRNStagedQuantity> grnStagedQuantityList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "grnDetail", cascade = CascadeType.ALL)
	private List<GrnAccessory> grnAccessoryList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "grnDetail", cascade = CascadeType.ALL)
	private List<PurchaseOrderDetail> purchaseOrderList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "goodReceiptNoteDetail", cascade = CascadeType.ALL)
	private List<PurchaseRequestDetail> purchaseRequestDetailList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "goodReceiptNoteDetail", cascade = CascadeType.ALL)
	private List<PhysicalInspectionDetail> physicalInspectionDetail;

	@Column(name = "grn_detail_id")
	private Integer grnDetailId;

	public List<PhysicalInspectionDetail> getPhysicalInspectionDetail() {
		return physicalInspectionDetail;
	}

	public void setPhysicalInspectionDetail(List<PhysicalInspectionDetail> physicalInspectionDetail) {
		this.physicalInspectionDetail = physicalInspectionDetail;
	}

	


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
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

	public Integer getGrnReceivedQuantity() {
		return grnReceivedQuantity;
	}

	public void setGrnReceivedQuantity(Integer grnReceivedQuantity) {
		this.grnReceivedQuantity = grnReceivedQuantity;
	}

	public Integer getBonusReceivedQuantity() {
		return bonusReceivedQuantity;
	}

	public void setBonusReceivedQuantity(Integer bonusReceivedQuantity) {
		this.bonusReceivedQuantity = bonusReceivedQuantity;
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

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public UomType getGrnUomType() {
		return grnUomType;
	}

	public void setGrnUomType(UomType grnUomType) {
		this.grnUomType = grnUomType;
	}

	public UomUnit getGrnUomUnit() {
		return grnUomUnit;
	}

	public void setGrnUomUnit(UomUnit grnUomUnit) {
		this.grnUomUnit = grnUomUnit;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
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

	public Date getWarrentyDate() {
		return warrentyDate;
	}

	public void setWarrentyDate(Date warrentyDate) {
		this.warrentyDate = warrentyDate;
	}

	public Integer getManufactureYear() {
		return manufactureYear;
	}

	public void setManufactureYear(Integer manufactureYear) {
		this.manufactureYear = manufactureYear;
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

	public Double getTotalOtherAmount() {
		return totalOtherAmount;
	}

	public void setTotalOtherAmount(Double totalOtherAmount) {
		this.totalOtherAmount = totalOtherAmount;
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

	public String getWarrentyPeriod() {
		return warrentyPeriod;
	}

	public void setWarrentyPeriod(String warrentyPeriod) {
		this.warrentyPeriod = warrentyPeriod;
	}

	public String getSupportTerm() {
		return supportTerm;
	}

	public void setSupportTerm(String supportTerm) {
		this.supportTerm = supportTerm;
	}

	public String getTechnicalSpec() {
		return technicalSpec;
	}

	public void setTechnicalSpec(String technicalSpec) {
		this.technicalSpec = technicalSpec;
	}

	public String getExpectedPayment() {
		return expectedPayment;
	}

	public void setExpectedPayment(String expectedPayment) {
		this.expectedPayment = expectedPayment;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public List<GrnBonusUomMapper> getGrnBonusUomMappersList() {
		return grnBonusUomMappersList;
	}

	public void setGrnBonusUomMappersList(List<GrnBonusUomMapper> grnBonusUomMappersList) {
		this.grnBonusUomMappersList = grnBonusUomMappersList;
	}

	public List<GrnUomMapper> getGrnUomMappersList() {
		return grnUomMappersList;
	}

	public void setGrnUomMappersList(List<GrnUomMapper> grnUomMappersList) {
		this.grnUomMappersList = grnUomMappersList;
	}

	public List<GRNStagedQuantity> getGrnStagedQuantityList() {
		return grnStagedQuantityList;
	}

	public void setGrnStagedQuantityList(List<GRNStagedQuantity> grnStagedQuantityList) {
		this.grnStagedQuantityList = grnStagedQuantityList;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public Integer getGrnApprovedQuantity() {
		return grnApprovedQuantity;
	}

	public void setGrnApprovedQuantity(Integer grnApprovedQuantity) {
		this.grnApprovedQuantity = grnApprovedQuantity;
	}

	public Integer getBonusApprovedQuantity() {
		return bonusApprovedQuantity;
	}

	public void setBonusApprovedQuantity(Integer bonusApprovedQuantity) {
		this.bonusApprovedQuantity = bonusApprovedQuantity;
	}

	public PurchaseOrderDetail getPurchaseOrderDetail() {
		return purchaseOrderDetail;
	}

	public void setPurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail) {
		this.purchaseOrderDetail = purchaseOrderDetail;
	}

	public String getModelNumber() {
		return modelNumber;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public List<GrnAccessory> getGrnAccessoryList() {
		return grnAccessoryList;
	}

	public void setGrnAccessoryList(List<GrnAccessory> grnAccessoryList) {
		this.grnAccessoryList = grnAccessoryList;
	}

	public ConsignmentPurchaseOrderDetail getConsignmentPurchaseOrderDetail() {
		return consignmentPurchaseOrderDetail;
	}

	public void setConsignmentPurchaseOrderDetail(ConsignmentPurchaseOrderDetail consignmentPurchaseOrderDetail) {
		this.consignmentPurchaseOrderDetail = consignmentPurchaseOrderDetail;
	}
	
	public List<PurchaseOrderDetail> getPurchaseOrderList() {
		return purchaseOrderList;
	}

	public void setPurchaseOrderList(List<PurchaseOrderDetail> purchaseOrderList) {
		this.purchaseOrderList = purchaseOrderList;
	}

	public List<PurchaseRequestDetail> getPurchaseRequestDetailList() {
		return purchaseRequestDetailList;
	}

	public void setPurchaseRequestDetailList(List<PurchaseRequestDetail> purchaseRequestDetailList) {
		this.purchaseRequestDetailList = purchaseRequestDetailList;
	}

	public Integer getGrnLeaseQuantity() {
		return grnLeaseQuantity;
	}

	public void setGrnLeaseQuantity(Integer grnLeaseQuantity) {
		this.grnLeaseQuantity = grnLeaseQuantity;
	}

	public Integer getBonusLeaseQuantity() {
		return bonusLeaseQuantity;
	}

	public void setBonusLeaseQuantity(Integer bonusLeaseQuantity) {
		this.bonusLeaseQuantity = bonusLeaseQuantity;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getGrnDetailId() {
		return grnDetailId;
	}

	public void setGrnDetailId(Integer grnDetailId) {
		this.grnDetailId = grnDetailId;
	}
	public void UpdateGoodReceiptNoteDetailLog(String batchNo, Integer grnReceivedQuantity, Integer bonusReceivedQuantity,
			UomType bonusUomType, UomUnit bonusUomUnit, Double cop, Double discount, Date expiryDate,
			UomType grnUomType, UomUnit grnUomUnit, Double mrp, Double netAmount, String remark, String specification,
			Double taxAmount, Tax tax, Double taxPercentage, Double totalAmount, Double totalDiscount,
			Double totalOtherAmount) {
		this.batchNo = batchNo;
		this.grnReceivedQuantity = grnReceivedQuantity;
		this.bonusReceivedQuantity = bonusReceivedQuantity;
		this.bonusUomType = bonusUomType;
		this.bonusUomUnit = bonusUomUnit;
		this.cop = cop;
		this.discount = discount;
		this.expiryDate = expiryDate;
		this.grnUomType = grnUomType;
		this.grnUomUnit = grnUomUnit;
		this.mrp = mrp;
		this.netAmount = netAmount;
		this.remark = remark;
		this.specification = specification;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.totalAmount = totalAmount;
		this.totalDiscount = totalDiscount;
		this.totalOtherAmount = totalOtherAmount;
	}
	public void UpdateGoodReceiptNoteDetailLogForBonus(String batchNo, Integer grnReceivedQuantity, Integer bonusReceivedQuantity,
			UomType bonusUomType, UomUnit bonusUomUnit, Double cop, Double discount, Date expiryDate,
			Double mrp, Double netAmount, String remark, String specification,
			Double taxAmount, Tax tax, Double taxPercentage, Double totalAmount, Double totalDiscount,
			Double totalOtherAmount) {
		this.batchNo = batchNo;
		this.grnReceivedQuantity = grnReceivedQuantity;
		this.bonusReceivedQuantity = bonusReceivedQuantity;
		this.bonusUomType = bonusUomType;
		this.bonusUomUnit = bonusUomUnit;
		this.cop = cop;
		this.discount = discount;
		this.expiryDate = expiryDate;
		this.mrp = mrp;
		this.netAmount = netAmount;
		this.remark = remark;
		this.specification = specification;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.totalAmount = totalAmount;
		this.totalDiscount = totalDiscount;
		this.totalOtherAmount = totalOtherAmount;
	}
	public Integer getGrnId() {
		return grnId;
	}

	public void setGrnId(Integer grnId) {
		this.grnId = grnId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
}

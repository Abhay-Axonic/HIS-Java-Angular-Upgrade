package com.param.entity.model.inventory;

import java.sql.Timestamp;
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

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;

@Entity(name = "OpeningBalanceDetail")
@Table(name = "t_opening_balance_detail", schema = "inventory")
public class OpeningBalanceDetail extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "amount_cop")
	private Double amountCop;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
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

	public List<OpeningBalanceSpare> getSpares() {
		return spares;
	}

	public void setSpares(List<OpeningBalanceSpare> spares) {
		this.spares = spares;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	private Double markup;

	private Double mrp;

	private Integer quantity;

	@Column(name = "lot_number", length = 100)
	private String lotNumber;

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
	
	@Column(name = "lcop")
	private Double lcop;

	@Column(name = "batch_id")
	private Integer batchId;
	
	public Double getLcop() {
		return lcop;
	}

	public void setLcop(Double lcop) {
		this.lcop = lcop;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_id")
	private Tax tax;

	@Column(name = "tax_percentage")
	private Double taxPercentage;

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
	@JoinColumn(name = "opening_balance_id")
	private OpeningBalance openingBalance;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "working_condition_id")
	private WorkingCondition workingCondition;

	@Column(name = "serial_number", length = 50)
	private String serialNumber;

	@Column(name = "year_of_manufacture")
	private Integer yearOfManufacture;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id")
	private Vendor vendor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_provider_id")
	private Vendor serviceProvider;

	@Column(name = "warranty_from")
	private Date warrantyFrom;

	@Column(name = "warranty_to")
	private Date warrantyTo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pms_frequency_id")
	private PMSFrequency pmsFrequency;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "openingBalanceDetail", cascade = CascadeType.ALL)
	private List<OpbUomMapper> uomList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "openingBalanceDetail", cascade = CascadeType.ALL)
	private List<OpeningBalanceAccessories> accessories;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "openingBalanceDetail", cascade = CascadeType.ALL)
	private List<OpeningBalanceSpare> spares;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "openingBalanceDetail", cascade = CascadeType.ALL)
	private List<OpeningBalanceInsurance> insurance;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "openingBalanceDetail", cascade = CascadeType.ALL)
	private List<OpbTaxComponent> opbTaxCom;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "openingBalanceDetail", cascade = CascadeType.ALL)
	private List<OpeningBalShelfItem> openingBalShelfItem;

	public List<OpeningBalShelfItem> getOpeningBalShelfItem() {
		return openingBalShelfItem;
	}

	public void setOpeningBalShelfItem(List<OpeningBalShelfItem> openingBalShelfItem) {
		this.openingBalShelfItem = openingBalShelfItem;
	}

	public OpeningBalanceDetail() {
		super();
	}

	public OpeningBalanceDetail(Integer id) {
		super();
		this.id = id;
	}

	/*
	 * public OpeningBalanceDetail(Double amountCop, String batchNo, Double cop,
	 * Date expiryDate, Item item, Double markup, Double mrp, Integer quantity,
	 * String remark, Status status, Double taxAmount, Tax tax, Double
	 * taxPercentage, UomType uomType, UomUnit uomUnit) { super(); this.amountCop =
	 * amountCop; this.batchNo = batchNo; this.cop = cop; this.expiryDate =
	 * expiryDate; this.item = item; this.markup = markup; this.mrp = mrp;
	 * this.quantity = quantity; this.remark = remark; this.status = status;
	 * this.taxAmount = taxAmount; this.tax = tax; this.taxPercentage =
	 * taxPercentage; this.uomType = uomType; this.uomUnit = uomUnit; }
	 */


	public OpeningBalanceDetail(Double amountCop, String batchNo, Double cop, Date expiryDate, Item item, Double markup,
			Double mrp, Integer quantity, String remark, Status status, Double taxAmount, Tax tax, Double taxPercentage,
			UomType uomType, UomUnit uomUnit, UomType mrpUomType) {
		super();
		this.amountCop = amountCop;
		this.batchNo = batchNo;
		this.cop = cop;
		this.expiryDate = expiryDate;
		this.item = item;
		this.markup = markup;
		this.mrp = mrp;
		this.quantity = quantity;
		this.remark = remark;
		this.status = status;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.mrpUomType = mrpUomType;
	}

	public OpeningBalanceDetail(Double amountCop, String batchNo, Double cop, Date expiryDate, Item item, Double markup,
			Double mrp, Integer quantity, String remark, Status status, Double taxAmount, Tax tax, Double taxPercentage,
			UomType uomType, UomUnit uomUnit, UomType mrpUomType, Double lcop) {
		super();
		this.amountCop = amountCop;
		this.batchNo = batchNo;
		this.cop = cop;
		this.expiryDate = expiryDate;
		this.item = item;
		this.markup = markup;
		this.mrp = mrp;
		this.quantity = quantity;
		this.remark = remark;
		this.status = status;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.mrpUomType = mrpUomType;
		this.lcop = lcop;
	}

	/*
	 * public OpeningBalanceDetail(Double amountCop, String batchNo, Double cop,
	 * Date expiryDate, Integer itemId, Integer markup, Double mrp, Integer
	 * quantity, String remark, Status status, Double taxAmount, Integer taxId,
	 * Double taxPercentage, Integer uomTypeId, Integer uomUnitId) { super();
	 * this.amountCop = amountCop; this.batchNo = batchNo; this.cop = cop;
	 * this.expiryDate = expiryDate; this.itemId = itemId; this.markup = markup;
	 * this.mrp = mrp; this.quantity = quantity; this.remark = remark; this.status =
	 * status; this.taxAmount = taxAmount; this.taxId = taxId; this.taxPercentage =
	 * taxPercentage; this.uomTypeId = uomTypeId; this.uomUnitId = uomUnitId; }
	 */

	public OpeningBalanceDetail(Integer id, Double amountCop, User approvedBy, Timestamp approvedDate,
			Integer approvedQuantity, String batchNo, Double cop, Date expiryDate, Item item, Double markup, Double mrp,
			Integer quantity, String rejectNote, Integer rejectQuantity, RejectReason rejectReason, String remark,
			Status status, Double taxAmount, Tax tax, Double taxPercentage, UomType uomType, UomUnit uomUnit,
			OpeningBalance openingBalance, List<OpbUomMapper> uomList) {
		super();
		this.id = id;
		this.amountCop = amountCop;
		this.approvedBy = approvedBy;
		this.approvedDate = approvedDate;
		this.approvedQuantity = approvedQuantity;
		this.batchNo = batchNo;
		this.cop = cop;
		this.expiryDate = expiryDate;
		this.item = item;
		this.markup = markup;
		this.mrp = mrp;
		this.quantity = quantity;
		this.rejectNote = rejectNote;
		this.rejectQuantity = rejectQuantity;
		this.rejectReason = rejectReason;
		this.remark = remark;
		this.status = status;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.openingBalance = openingBalance;
		this.uomList = uomList;
	}

	public OpeningBalanceDetail(Item item, Integer quantity, String remark, Status status, String serialNumber,
			Integer yearOfManufacture, Vendor vendor, Vendor serviceProvider, Date warrantyFrom, Date warrantyTo,
			PMSFrequency pmsFrequency, WorkingCondition workingCondition) {
		super();
		this.item = item;
		this.quantity = quantity;
		this.remark = remark;
		this.status = status;
		this.serialNumber = serialNumber;
		this.yearOfManufacture = yearOfManufacture;
		this.vendor = vendor;
		this.serviceProvider = serviceProvider;
		this.warrantyFrom = warrantyFrom;
		this.warrantyTo = warrantyTo;
		this.pmsFrequency = pmsFrequency;
		this.workingCondition = workingCondition;
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

	/*
	 * public Integer getApprovedBy() { return approvedBy; }
	 * 
	 * public void setApprovedBy(Integer approvedBy) { this.approvedBy = approvedBy;
	 * }
	 */

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

	public Double getMarkup() {
		return markup;
	}

	public void setMarkup(Double markup) {
		this.markup = markup;
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

	/*
	 * public String getRemark() { return remark; }
	 * 
	 * public void setRemark(String remark) { this.remark = remark; }
	 */
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public OpeningBalance getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(OpeningBalance openingBalance) {
		this.openingBalance = openingBalance;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public List<OpbUomMapper> getUomList() {
		return uomList;
	}

	public void setUomList(List<OpbUomMapper> uomList) {
		this.uomList = uomList;
	}

	public List<OpeningBalanceAccessories> getAccessories() {
		return accessories;
	}

	public void setAccessories(List<OpeningBalanceAccessories> accessories) {
		this.accessories = accessories;
	}

	public List<OpeningBalanceInsurance> getInsurance() {
		return insurance;
	}

	public void setInsurance(List<OpeningBalanceInsurance> insurance) {
		this.insurance = insurance;
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

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Integer getYearOfManufacture() {
		return yearOfManufacture;
	}

	public void setYearOfManufacture(Integer yearOfManufacture) {
		this.yearOfManufacture = yearOfManufacture;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Vendor getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(Vendor serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public Date getWarrantyFrom() {
		return warrantyFrom;
	}

	public void setWarrantyFrom(Date warrantyFrom) {
		this.warrantyFrom = warrantyFrom;
	}

	public Date getwarrantyTo() {
		return warrantyTo;
	}

	public void setwarrantyTo(Date warrantyTo) {
		this.warrantyTo = warrantyTo;
	}

	public PMSFrequency getPmsFrequency() {
		return pmsFrequency;
	}

	public void setPmsFrequency(PMSFrequency pmsFrequency) {
		this.pmsFrequency = pmsFrequency;
	}

	public OpbUomMapper addUom(OpbUomMapper uom) {
		if (this.uomList == null)
			this.uomList = new ArrayList<OpbUomMapper>();

		this.uomList.add(uom);
		uom.setOpeningBalanceDetail(this);

		return uom;
	}

	public OpbTaxComponent addOpbTaxCom(OpbTaxComponent opbTaxCom) {
		if (this.opbTaxCom == null)
			this.opbTaxCom = new ArrayList<OpbTaxComponent>();

		this.opbTaxCom.add(opbTaxCom);
		opbTaxCom.setOpeningBalanceDetail(this);

		return opbTaxCom;
	}

	public OpbUomMapper removeUom(OpbUomMapper uom) {
		this.uomList.remove(uom);
		uom.setOpeningBalanceDetail(null);

		return uom;
	}

	public OpeningBalanceAccessories addAccessories(OpeningBalanceAccessories accessories) {
		if (this.accessories == null)
			this.accessories = new ArrayList<OpeningBalanceAccessories>();

		this.accessories.add(accessories);
		accessories.setOpeningBalanceDetail(this);

		return accessories;
	}

	public OpeningBalanceSpare addSpare(OpeningBalanceSpare spares) {
		if (this.spares == null)
			this.spares = new ArrayList<OpeningBalanceSpare>();

		this.spares.add(spares);
		spares.setOpeningBalanceDetail(this);

		return spares;
	}

	public OpeningBalanceInsurance addInsurance(OpeningBalanceInsurance insurance) {
		if (this.insurance == null)
			this.insurance = new ArrayList<OpeningBalanceInsurance>();

		this.insurance.add(insurance);
		insurance.setOpeningBalanceDetail(this);

		return insurance;
	}

	public void updateOpeningBalaceDetails(Item item, Integer quantity, String remark, Status status,
			String serialNumber, Integer yearOfManufacture, Vendor vendor, Vendor serviceProvider, Date warrantyFrom,
			Date warrantyTo, PMSFrequency pmsFrequency, WorkingCondition workingCondition) {
		this.item = item;
		this.quantity = quantity;
		this.remark = remark;
		this.status = status;
		this.serialNumber = serialNumber;
		this.yearOfManufacture = yearOfManufacture;
		this.vendor = vendor;
		this.serviceProvider = serviceProvider;
		this.warrantyFrom = warrantyFrom;
		this.warrantyTo = warrantyTo;
		this.pmsFrequency = pmsFrequency;
		this.workingCondition = workingCondition;
	}

	public void updateOpeningBalaceDetails(String batchNo, Date expiryDate, UomType uomType, UomUnit uomUnit,
			UomType mrpUomType, Integer quantity, Double cop, Double mrp, Double amountCop, Double markup, Tax tax,
			Double taxPercentage, Double taxAmount, String remark, Status status) {

		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.mrpUomType = mrpUomType;
		this.quantity = quantity;
		this.cop = cop;
		this.mrp = mrp;
		this.amountCop = amountCop;
		this.markup = markup;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.taxAmount = taxAmount;
		this.remark = remark;
		this.status = status;
	}

	public OpbUomMapper addOpbUomMapperList(OpbUomMapper opbUomMapper) {
		if (getUomList() == null)
			this.uomList = new ArrayList<OpbUomMapper>();

		getUomList().add(opbUomMapper);
		opbUomMapper.setOpeningBalanceDetail(this);

		return opbUomMapper;
	}

	public WorkingCondition getWorkingCondition() {
		return workingCondition;
	}

	public void setWorkingCondition(WorkingCondition workingCondition) {
		this.workingCondition = workingCondition;
	}

	public UomType getMrpUomType() {
		return mrpUomType;
	}

	public void setMrpUomType(UomType mrpUomType) {
		this.mrpUomType = mrpUomType;
	}

	public Date getWarrantyTo() {
		return warrantyTo;
	}

	public void setWarrantyTo(Date warrantyTo) {
		this.warrantyTo = warrantyTo;
	}

	public List<OpbTaxComponent> getOpbTaxCom() {
		return opbTaxCom;
	}

	public void setOpbTaxCom(List<OpbTaxComponent> opbTaxCom) {
		this.opbTaxCom = opbTaxCom;
	}

	public OpeningBalShelfItem addOpeningBalanceShelfItemList(OpeningBalShelfItem openingBalShelfItem) {
		if (getOpeningBalShelfItem() == null)
			this.openingBalShelfItem = new ArrayList<OpeningBalShelfItem>();

		getOpeningBalShelfItem().add(openingBalShelfItem);
		openingBalShelfItem.setOpeningBalanceDetail(this);

		return openingBalShelfItem;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	

}
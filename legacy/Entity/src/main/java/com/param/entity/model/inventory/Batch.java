package com.param.entity.model.inventory;

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
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.GoodReceiptNoteDetail;
import com.param.entity.model.procurement.ShelfItem;
import com.param.global.common.ICommonConstants;
import com.param.utility.TimeZoneComponent;

@Entity(name = "Batch")
@Table(name = "t_batch", schema = "inventory")
public class Batch extends BaseEntity implements Serializable,ICommonConstants,Cloneable {
	private static final long serialVersionUID = 1L;
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;

	@Column(name = "actual_lending_price")
	private Double actualLendingPrice;

	@Column(length = 100)
	private String barcode;

	@Column(name = "batch_no", length = 100,updatable = false)
	private String batchNo;

	private Double cop;
	public Double getLcop() {
		return lcop;
	}

	public void setLcop(Double lcop) {
		this.lcop = lcop;
	}

	@Column(name = "lcop")
	private Double lcop;

	@Column(name = "current_quantity", nullable = false, columnDefinition = "int default 0")
	private Integer currentQuantity ;

	@Temporal(TemporalType.DATE)
	@Column(name = "expiry_date")
	private Date expiryDate;

	@Column(name = "is_opb")
	private Boolean isOpb;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@Column(name = "lending_price")
	private Double lendingPrice;

	@Column(name = "lot_no", length = 100)
	private String lotNo;

	@Column(name = "model_number", length = 50)
	private String modelNumber;

	private Double markup;

	@Column(name = "quantity")
	private Integer quantity;

	@Column(name = "rate", updatable = false)
	private Double rate;
	
	@Column(name = "selling_price",updatable = false)
	private Double sellingPrice;

	@Column(name = "tax_amount")
	private Double taxAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_id")
	private Tax tax;

	@Column(name = "tax_percentage")
	private Double taxPercentage;

	@Column(name = "barcode_print_count")
	private Integer barcodePrintCount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_unit_id")
	private UomUnit uomUnit;

	@Column(name = "is_quarantine", nullable = false)
	private Boolean isQuarantine = false;
	
	@Column(name = "quarantine_quantity")
	private Integer quarantineQuantity;
	
	public Integer getQuarantineQuantity() {
		return quarantineQuantity;
	}

	public void setQuarantineQuantity(Integer quarantineQuantity) {
		this.quarantineQuantity = quarantineQuantity;
	}

	@Column(name = "is_batch_required")
	private Boolean isBatchRequired ;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id")
	private Vendor vendor;
	
	@Transient
	private Boolean isOldBatch ;

	public Boolean getIsOldBatch() {
		return isOldBatch;
	}

	public void setIsOldBatch(Boolean isOldBatch) {
		this.isOldBatch = isOldBatch;
	}

	public Boolean getIsBatchRequired() {
		return isBatchRequired;
	}

	public void setIsBatchRequired(Boolean isBatchRequired) {
		this.isBatchRequired = isBatchRequired;
	}

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

	@Column(name = "year_of_manufacture")
	private Integer yearOfManufacture;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "working_condition_id")
	private WorkingCondition workingCondition;

	@Column(name = "is_pharmacy")
	private Boolean isPharmacy = false;

	@Column(name = "is_indent")
	private Boolean isIndent = false;

	@Column(name = "is_return")
	private Boolean isReturn = false;

	@Column(name = "rejected_quantity")
	private Integer rejectedQuantity;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "batch", cascade = CascadeType.ALL)
	private List<StoreStock> storeStockList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "batch", cascade = CascadeType.ALL)
	private List<BatchUomMapper> batchUomList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "batch", cascade = CascadeType.ALL)
	private List<StockTransaction> stockTransactionList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "batch")
	private List<GoodReceiptNoteDetail> goodReceiptNoteDetail;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "batch", cascade = CascadeType.ALL)
	private List<ShelfItem> shelfItemList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "batch", cascade = CascadeType.ALL)
	private List<BatchAccessoriesMapper> batchAccessoriesMapperList;

	/* added by minesh for convert consignment Stock to normal Stock*/
	@Column(name = "is_consignment")
	private Boolean isConsignment;	
	
	@Column(name = "pre_printed_barcode")
	private String prePrintedBarcode;

	@Column(name = "printed_count")
	private Integer printedCount;

	public Boolean getIsConsignment() {
		return isConsignment==null?false:isConsignment;
	}

	public void setIsConsignment(Boolean isConsignment) {
		this.isConsignment = isConsignment==null?false:isConsignment;
	}

	public Batch(Long id, Double actualLendingPrice, String barcode, String batchNo, Double cop,
			Integer currentQuantity, Date expiryDate, Boolean isOpb, Item item, Double lendingPrice, String lotNo,
			String modelNumber, Double markup, Integer quantity, Double rate, Double sellingPrice, Double taxAmount,
			Tax tax, Double taxPercentage, Integer barcodePrintCount, UomUnit uomUnit, Boolean isQuarantine,
			Vendor vendor, Vendor serviceProvider, Date warrantyFrom, Date warrantyTo, PMSFrequency pmsFrequency,
			Integer yearOfManufacture, WorkingCondition workingCondition, Boolean isPharmacy, Boolean isIndent,
			Boolean isReturn, Integer rejectedQuantity, List<StoreStock> storeStockList,
			List<BatchUomMapper> batchUomList, List<StockTransaction> stockTransactionList,
			List<GoodReceiptNoteDetail> goodReceiptNoteDetail, List<ShelfItem> shelfItemList,
			List<BatchAccessoriesMapper> batchAccessoriesMapperList, Boolean isConsignment) {
		super();
		this.id = id;
		this.actualLendingPrice = actualLendingPrice;
		this.barcode = barcode;
		this.batchNo = batchNo;
		this.cop = cop;
		this.currentQuantity = currentQuantity;
		this.expiryDate = expiryDate;
		this.isOpb = isOpb;
		this.item = item;
		this.lendingPrice = lendingPrice;
		this.lotNo = lotNo;
		this.modelNumber = modelNumber;
		this.markup = markup;
		this.quantity = quantity;
		this.rate = rate;
		this.sellingPrice = sellingPrice;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.barcodePrintCount = barcodePrintCount;
		this.uomUnit = uomUnit;
		this.isQuarantine = isQuarantine;
		this.vendor = vendor;
		this.serviceProvider = serviceProvider;
		this.warrantyFrom = warrantyFrom;
		this.warrantyTo = warrantyTo;
		this.pmsFrequency = pmsFrequency;
		this.yearOfManufacture = yearOfManufacture;
		this.workingCondition = workingCondition;
		this.isPharmacy = isPharmacy;
		this.isIndent = isIndent;
		this.isReturn = isReturn;
		this.rejectedQuantity = rejectedQuantity;
		this.storeStockList = storeStockList;
		this.batchUomList = batchUomList;
		this.stockTransactionList = stockTransactionList;
		this.goodReceiptNoteDetail = goodReceiptNoteDetail;
		this.shelfItemList = shelfItemList;
		this.batchAccessoriesMapperList = batchAccessoriesMapperList;
		this.isConsignment = isConsignment;
	}
	
	public Batch(Long id, Double actualLendingPrice, String barcode, String batchNo, Double cop,
			Integer currentQuantity, Date expiryDate, Boolean isOpb, Item item, Double lendingPrice, String lotNo,
			String modelNumber, Double markup, Integer quantity, Double rate, Double sellingPrice, Double taxAmount,
			Tax tax, Double taxPercentage, Integer barcodePrintCount, UomUnit uomUnit, Boolean isQuarantine,
			Vendor vendor, Vendor serviceProvider, Date warrantyFrom, Date warrantyTo, PMSFrequency pmsFrequency,
			Integer yearOfManufacture, WorkingCondition workingCondition, Boolean isPharmacy, Boolean isIndent,
			Boolean isReturn, Integer rejectedQuantity, List<StoreStock> storeStockList,
			List<BatchUomMapper> batchUomList, List<StockTransaction> stockTransactionList,
		    List<ShelfItem> shelfItemList,
			List<BatchAccessoriesMapper> batchAccessoriesMapperList, Boolean isConsignment) {
		this.id = id;
		this.actualLendingPrice = actualLendingPrice;
		this.barcode = barcode;
		this.batchNo = batchNo;
		this.cop = cop;
		this.currentQuantity = currentQuantity;
		this.expiryDate = expiryDate;
		this.isOpb = isOpb;
		this.item = item;
		this.lendingPrice = lendingPrice;
		this.lotNo = lotNo;
		this.modelNumber = modelNumber;
		this.markup = markup;
		this.quantity = quantity;
		this.rate = rate;
		this.sellingPrice = sellingPrice;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.barcodePrintCount = barcodePrintCount;
		this.uomUnit = uomUnit;
		this.isQuarantine = isQuarantine;
		this.vendor = vendor;
		this.serviceProvider = serviceProvider;
		this.warrantyFrom = warrantyFrom;
		this.warrantyTo = warrantyTo;
		this.pmsFrequency = pmsFrequency;
		this.yearOfManufacture = yearOfManufacture;
		this.workingCondition = workingCondition;
		this.isPharmacy = isPharmacy;
		this.isIndent = isIndent;
		this.isReturn = isReturn;
		this.rejectedQuantity = rejectedQuantity;
		this.storeStockList = storeStockList;
		this.batchUomList = batchUomList;
		this.stockTransactionList = stockTransactionList;
		this.shelfItemList = shelfItemList;
		this.batchAccessoriesMapperList = batchAccessoriesMapperList;
		this.isConsignment = isConsignment;
	}
	
	/* end added by minesh for convert consignment Stock to normal Stock*/	
	
	/* added by minesh for return quantity*/
	@Column(name = "returned_quantity")
	private Integer returnedQuantity;

	public Integer getReturnedQuantity() {
		return  returnedQuantity!=null?returnedQuantity:0;
	}

	public void setReturnedQuantity(Integer returnedQuantity) {
		this.returnedQuantity = returnedQuantity;
	}
	/*end added by minesh for return quantity*/

	public Batch() {
		super();
	}

	public Batch(List<Long> list) {
		super();

	}

	public Batch(Long id) {
		this.id = id;
	}

	public Batch(Long id, Integer currentQuantity) {
		this(id);
		this.currentQuantity = currentQuantity;
	}

	public Batch(String batchNo, Double cop, Integer currentQuantity, Date expiryDate, Boolean isOpb, Item item,
			Integer quantity, Double rate, UomUnit uomUnit, Boolean isQuarantine, Vendor vendor) {
		super();
		this.batchNo = batchNo;
		this.cop = cop;
		this.currentQuantity = currentQuantity;
		this.expiryDate = expiryDate;
		this.isOpb = isOpb;
		this.item = item;
		this.quantity = quantity;
		this.rate = rate;
		this.sellingPrice = rate;
		this.uomUnit = uomUnit;
		this.isQuarantine = isQuarantine;
		this.vendor = vendor;
	}

	public Batch(String batchNo, Double cop, Integer currentQuantity, Date expiryDate, Boolean isOpb, Item item,
			Integer quantity, Double rate, UomUnit uomUnit, Boolean isQuarantine, Vendor vendor, Tax tax) {
		super();
		this.batchNo = batchNo;
		this.cop = cop;
		this.currentQuantity = currentQuantity;
		this.expiryDate = expiryDate;
		this.isOpb = isOpb;
		this.item = item;
		this.quantity = quantity;
		this.rate = rate;
		this.sellingPrice = rate;
		this.uomUnit = uomUnit;
		this.isQuarantine = isQuarantine;
		this.vendor = vendor;
		this.tax = tax;
	}

	public Batch(String batchNo, Double cop, Integer currentQuantity, Date expiryDate, Boolean isOpb, Item item,
			Integer quantity, Double rate, Boolean isQuarantine, Vendor vendor) {
		super();
		this.batchNo = batchNo;
		this.cop = cop;
		this.currentQuantity = currentQuantity;
		this.expiryDate = expiryDate;
		this.isOpb = isOpb;
		this.item = item;
		this.quantity = quantity;
		this.rate = rate;
		this.isQuarantine = isQuarantine;
		this.vendor = vendor;
	}

	public Batch(Double actualLendingPrice, String batchNo, Double cop, Integer currentQuantity, Date expiryDate,
			Item item, Double lendingPrice, String lotNo, Double markup, Integer quantity, Double rate,
			Double sellingPrice, Double taxAmount, Tax tax, Double taxPercentage, UomUnit uomUnit, Boolean isOpb) {
		this.actualLendingPrice = actualLendingPrice;
		this.batchNo = batchNo;
		this.cop = cop;
		this.currentQuantity = currentQuantity;
		this.expiryDate = expiryDate;
		this.item = item;
		this.lendingPrice = lendingPrice;
		this.lotNo = lotNo;
		this.markup = markup;
		this.quantity = quantity;
		this.rate = rate;
		this.sellingPrice = sellingPrice;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.uomUnit = uomUnit;
		this.isOpb = isOpb;
	}

	public Batch(Double actualLendingPrice, String batchNo, Double cop, Integer currentQuantity, Date expiryDate,
			Item item, Double lendingPrice, String lotNo, Double markup, Integer quantity, Double rate,
			Double sellingPrice, Double taxAmount, Tax tax, Double taxPercentage, Boolean isOpb) {
		this.actualLendingPrice = actualLendingPrice;
		this.batchNo = batchNo;
		this.cop = cop;
		this.currentQuantity = currentQuantity;
		this.expiryDate = expiryDate;
		this.item = item;
		this.lendingPrice = lendingPrice;
		this.lotNo = lotNo;
		this.markup = markup;
		this.quantity = quantity;
		this.rate = rate;
		this.sellingPrice = sellingPrice;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.isOpb = isOpb;
	}

	public Batch(String batchNo, Integer currentQuantity, boolean isOpb, Item item, Integer quantity,
			boolean isQuarantine) {
		super();
		this.batchNo = batchNo;
		this.currentQuantity = currentQuantity;
		this.isOpb = isOpb;
		this.item = item;
		this.quantity = quantity;
		this.isQuarantine = isQuarantine;
	}

	public Batch(String batchNo, Integer currentQuantity, Boolean isOpb, Item item, Integer quantity,
			Boolean isQuarantine, Vendor serviceProvider, Date warrantyFrom, Date warrantyTo, PMSFrequency pmsFrequency,
			Integer yearOfManufacture, WorkingCondition workingCondition) {
		super();
		this.batchNo = batchNo;
		this.currentQuantity = currentQuantity;
		this.isOpb = isOpb;
		this.item = item;
		this.quantity = quantity;
		this.isQuarantine = isQuarantine;
		this.serviceProvider = serviceProvider;
		this.warrantyFrom = warrantyFrom;
		this.warrantyTo = warrantyTo;
		this.pmsFrequency = pmsFrequency;
		this.yearOfManufacture = yearOfManufacture;
		this.workingCondition = workingCondition;
	}

	public Batch(Double actualLendingPrice, String batchNo, Double cop, Integer currentQuantity, Date expiryDate,
			Item item, Double lendingPrice, String lotNo, Double markup, Integer quantity, Double rate,
			Double sellingPrice, Double taxAmount, Tax tax, Double taxPercentage, UomUnit uomUnit, Boolean isOpb,
			Vendor vendor, Boolean isQuarantine) {
		this(actualLendingPrice, batchNo, cop, currentQuantity, expiryDate, item, lendingPrice, lotNo, markup, quantity,
				rate, sellingPrice, taxAmount, tax, taxPercentage, uomUnit, isOpb);
		this.vendor = vendor;
		this.isQuarantine = isQuarantine;
	}

//Constructor For Opening Balance Implant Approve 
	public Batch(Double rate, String batchNo, Integer quantity, Double cop, Date expiryDate, Item item, Double markup,
			Integer currentQuantity, Double lendingPrice, Double taxAmount, Tax tax, Double taxPercentage,
			Boolean opb) {
		super();
		this.rate = rate;
		this.sellingPrice = rate;
		this.batchNo = batchNo;
		this.quantity = quantity;
		this.cop = cop;
		this.expiryDate = expiryDate;
		this.item = item;
		this.markup = markup;
		this.currentQuantity = currentQuantity;
		this.lendingPrice = lendingPrice;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.isOpb = opb;

	}

	public void updateBatchChange(String batchNo, Date expiryDate, Double rate , Integer userId) {
		this.batchNo = batchNo;
		this.expiryDate = expiryDate;
		this.sellingPrice = rate;
		this.rate = rate;
		this.updatedDate = TimeZoneComponent.getDateByzone(0);
		this.updatedByUser =userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getActualLendingPrice() {
		return actualLendingPrice;
	}

	public void setActualLendingPrice(Double actualLendingPrice) {
		this.actualLendingPrice = actualLendingPrice;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getBatchNo() {
		//return batchNo!=null && batchNo.equals(ICommonConstants.NOT_APPLICABLE)?null:batchNo;
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo!=null && batchNo.equals(ICommonConstants.NOT_APPLICABLE)?null:batchNo;
		this.batchNo = batchNo;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Integer getCurrentQuantity() {
		return currentQuantity;
	}

	public void setCurrentQuantity(Integer currentQuantity) {
		this.currentQuantity = currentQuantity;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Boolean getIsOpb() {
		return isOpb;
	}

	public void setIsOpb(Boolean isOpb) {
		this.isOpb = isOpb;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Double getLendingPrice() {
		return lendingPrice;
	}

	public void setLendingPrice(Double lendingPrice) {
		this.lendingPrice = lendingPrice;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public Double getMarkup() {
		return markup;
	}

	public void setMarkup(Double markup) {
		this.markup = markup;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getRate() {
		return rate!=null?rate:0;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getSellingPrice() {
		return  sellingPrice!=null?sellingPrice:0;
	}

	public void setSellingPrice(Double sellingPrice) {
		this.sellingPrice = sellingPrice;
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

	public UomUnit getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(UomUnit uomUnit) {
		this.uomUnit = uomUnit;
	}

	public List<StoreStock> getStoreStockList() {
		return storeStockList;
	}

	public void setStoreStockList(List<StoreStock> storeStockList) {
		this.storeStockList = storeStockList;
	}

	public List<BatchUomMapper> getBatchUomList() {
		return batchUomList;
	}

	public void setBatchUomList(List<BatchUomMapper> batchUomList) {
		this.batchUomList = batchUomList;
	}

	public Integer getBarcodePrintCount() {
		return barcodePrintCount;
	}

	public void setBarcodePrintCount(Integer barcodePrintCount) {
		this.barcodePrintCount = barcodePrintCount;
	}

	public Boolean isQuarantine() {
		return isQuarantine;
	}

	public void setQuarantine(Boolean isQuarantine) {
		this.isQuarantine = isQuarantine;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public BatchUomMapper addBatchUom(BatchUomMapper batchUomMapper) {
		if (this.batchUomList == null)
			this.batchUomList = new ArrayList<BatchUomMapper>();

		this.batchUomList.add(batchUomMapper);
		batchUomMapper.setBatch(this);

		return batchUomMapper;
	}

	public BatchAccessoriesMapper addBatchAccessories(BatchAccessoriesMapper batchAccessoriesMapper) {
		if (this.batchAccessoriesMapperList == null)
			this.batchAccessoriesMapperList = new ArrayList<BatchAccessoriesMapper>();

		this.batchAccessoriesMapperList.add(batchAccessoriesMapper);
		batchAccessoriesMapper.setBatch(this);

		return batchAccessoriesMapper;
	}

	public StoreStock addStoreStock(StoreStock storeStock) {
		if (this.storeStockList == null)
			this.storeStockList = new ArrayList<StoreStock>();

		this.storeStockList.add(storeStock);
		storeStock.setBatch(this);

		return storeStock;
	}

	public List<StockTransaction> getStockTransactionList() {
		return stockTransactionList;
	}

	public void setStockTransactionList(List<StockTransaction> stockTransactionList) {
		this.stockTransactionList = stockTransactionList;
	}

	public List<GoodReceiptNoteDetail> getGoodReceiptNoteDetail() {
		return goodReceiptNoteDetail;
	}

	public void setGoodReceiptNoteDetail(List<GoodReceiptNoteDetail> goodReceiptNoteDetail) {
		this.goodReceiptNoteDetail = goodReceiptNoteDetail;
	}

	public List<ShelfItem> getShelfItemList() {
		return shelfItemList;
	}

	public void setShelfItemList(List<ShelfItem> shelfItemList) {
		this.shelfItemList = shelfItemList;
	}

	public Boolean getIsQuarantine() {
		return isQuarantine;
	}

	public void setIsQuarantine(Boolean isQuarantine) {
		this.isQuarantine = isQuarantine;
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

	public PMSFrequency getPmsFrequency() {
		return pmsFrequency;
	}

	public void setPmsFrequency(PMSFrequency pmsFrequency) {
		this.pmsFrequency = pmsFrequency;
	}

	public Integer getYearOfManufacture() {
		return yearOfManufacture;
	}

	public void setYearOfManufacture(Integer yearOfManufacture) {
		this.yearOfManufacture = yearOfManufacture;
	}

	public WorkingCondition getWorkingCondition() {
		return workingCondition;
	}

	public void setWorkingCondition(WorkingCondition workingCondition) {
		this.workingCondition = workingCondition;
	}

	public String getModelNumber() {
		return modelNumber;
	}

	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}

	public Date getWarrantyTo() {
		return warrantyTo;
	}

	public void setWarrantyTo(Date warrantyTo) {
		this.warrantyTo = warrantyTo;
	}

	public Boolean getIsPharmacy() {
		return isPharmacy;
	}

	public void setIsPharmacy(Boolean isPharmacy) {
		this.isPharmacy = isPharmacy;
	}

	public Boolean getIsIndent() {
		return isIndent;
	}

	public void setIsIndent(Boolean isIndent) {
		this.isIndent = isIndent;
	}

	public Boolean getIsReturn() {
		return isReturn;
	}

	public void setIsReturn(Boolean isReturn) {
		this.isReturn = isReturn;
	}

	public List<BatchAccessoriesMapper> getBatchAccessoriesMapperList() {
		return batchAccessoriesMapperList;
	}

	public void setBatchAccessoriesMapperList(List<BatchAccessoriesMapper> batchAccessoriesMapperList) {
		this.batchAccessoriesMapperList = batchAccessoriesMapperList;
	}

	public StockTransaction addStockTransaction(StockTransaction stockTransaction) {
		if (this.stockTransactionList == null)
			this.stockTransactionList = new ArrayList<StockTransaction>();

		this.stockTransactionList.add(stockTransaction);
		stockTransaction.setBatch(this);

		return stockTransaction;
	}

	public ShelfItem addShelfItem(ShelfItem shelfItem) {
		if (this.shelfItemList == null)
			this.shelfItemList = new ArrayList<ShelfItem>();

		this.shelfItemList.add(shelfItem);
		shelfItem.setBatch(this);

		return shelfItem;
	}

	public BatchAccessoriesMapper addAccessories(BatchAccessoriesMapper accessoriesMapper) {
		if (this.batchAccessoriesMapperList == null)
			this.batchAccessoriesMapperList = new ArrayList<BatchAccessoriesMapper>();

		this.batchAccessoriesMapperList.add(accessoriesMapper);
		accessoriesMapper.setBatch(this);

		return accessoriesMapper;
	}

	public Integer getRejectedQuantity() {
		return rejectedQuantity!=null?rejectedQuantity:0;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
	}

	public String getPrePrintedBarcode() {
		return prePrintedBarcode;
	}

	public void setPrePrintedBarcode(String prePrintedBarcode) {
		this.prePrintedBarcode = prePrintedBarcode;
	}

	public Integer getPrintedCount() {
		return printedCount;
	}

	public void setPrintedCount(Integer printedCount) {
		this.printedCount = printedCount;
	}
}
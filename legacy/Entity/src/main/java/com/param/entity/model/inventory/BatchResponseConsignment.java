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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.GoodReceiptNoteDetail;
import com.param.entity.model.procurement.ShelfItem;

public class BatchResponseConsignment extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private Long id;

	private Double actuallendingprice;

	private String barcode;

	private String batchno;

	private Double cop;

	private Integer currentquantity;

	private Date expirydate;

	private Boolean isopb;

	private Item item;

	private Double lendingprice;

	private String lotno;

	private String modelnumber;

	private Double markup;

	private Integer quantity;

	private Double rate;

	private Double sellingprice;

	private Double taxamount;

	private Tax tax;

	private Double taxpercentage;

	private Integer barcodeprintcount;
	
	private UomUnit uomUnit;

	private Boolean isquarantine = false;

	private Vendor vendor;

	private Vendor serviceprovider;

	private Date warrantyfrom;

	private Date warrantyto;

	private PMSFrequency pmsfrequency;
	
	private Integer yearofmanufacture;

	private WorkingCondition workingcondition;

	private Boolean ispharmacy = false;
	
	private Boolean isindent = false;

	private Boolean isreturn = false;

	private Integer rejectedquantity;

	private List<StoreStock> storeStockList;

	private List<BatchUomMapper> batchUomList;

	private List<StockTransaction> stockTransactionList;

	private GoodReceiptNoteDetail goodReceiptNoteDetail;
	
	private List<ShelfItem> shelfItemList;

	private List<BatchAccessoriesMapper> batchAccessoriesMapperList;

	private Boolean isconsignment;	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getActuallendingprice() {
		return actuallendingprice;
	}

	public void setActuallendingprice(Double actuallendingprice) {
		this.actuallendingprice = actuallendingprice;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getBatchno() {
		return batchno;
	}

	public void setBatchno(String batchno) {
		this.batchno = batchno;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Integer getCurrentquantity() {
		return currentquantity;
	}

	public void setCurrentquantity(Integer currentquantity) {
		this.currentquantity = currentquantity;
	}

	public Date getExpirydate() {
		return expirydate;
	}

	public void setExpirydate(Date expirydate) {
		this.expirydate = expirydate;
	}

	public Boolean getIsopb() {
		return isopb;
	}

	public void setIsopb(Boolean isopb) {
		this.isopb = isopb;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Double getLendingprice() {
		return lendingprice;
	}

	public void setLendingprice(Double lendingprice) {
		this.lendingprice = lendingprice;
	}

	public String getLotno() {
		return lotno;
	}

	public void setLotno(String lotno) {
		this.lotno = lotno;
	}

	public String getModelnumber() {
		return modelnumber;
	}

	public void setModelnumber(String modelnumber) {
		this.modelnumber = modelnumber;
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
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getSellingprice() {
		return sellingprice;
	}

	public void setSellingprice(Double sellingprice) {
		this.sellingprice = sellingprice;
	}

	public Double getTaxamount() {
		return taxamount;
	}

	public void setTaxamount(Double taxamount) {
		this.taxamount = taxamount;
	}

	public Tax getTax() {
		return tax;
	}

	public void setTax(Tax tax) {
		this.tax = tax;
	}

	public Double getTaxpercentage() {
		return taxpercentage;
	}

	public void setTaxpercentage(Double taxpercentage) {
		this.taxpercentage = taxpercentage;
	}

	public Integer getBarcodeprintcount() {
		return barcodeprintcount;
	}

	public void setBarcodeprintcount(Integer barcodeprintcount) {
		this.barcodeprintcount = barcodeprintcount;
	}

	public UomUnit getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(UomUnit uomUnit) {
		this.uomUnit = uomUnit;
	}

	public Boolean getIsquarantine() {
		return isquarantine;
	}

	public void setIsquarantine(Boolean isquarantine) {
		this.isquarantine = isquarantine;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Vendor getServiceprovider() {
		return serviceprovider;
	}

	public void setServiceprovider(Vendor serviceprovider) {
		this.serviceprovider = serviceprovider;
	}

	public Date getWarrantyfrom() {
		return warrantyfrom;
	}

	public void setWarrantyfrom(Date warrantyfrom) {
		this.warrantyfrom = warrantyfrom;
	}

	public Date getWarrantyto() {
		return warrantyto;
	}

	public void setWarrantyto(Date warrantyto) {
		this.warrantyto = warrantyto;
	}

	public PMSFrequency getPmsfrequency() {
		return pmsfrequency;
	}

	public void setPmsfrequency(PMSFrequency pmsfrequency) {
		this.pmsfrequency = pmsfrequency;
	}

	public Integer getYearofmanufacture() {
		return yearofmanufacture;
	}

	public void setYearofmanufacture(Integer yearofmanufacture) {
		this.yearofmanufacture = yearofmanufacture;
	}

	public WorkingCondition getWorkingcondition() {
		return workingcondition;
	}

	public void setWorkingcondition(WorkingCondition workingcondition) {
		this.workingcondition = workingcondition;
	}

	public Boolean getIspharmacy() {
		return ispharmacy;
	}

	public void setIspharmacy(Boolean ispharmacy) {
		this.ispharmacy = ispharmacy;
	}

	public Boolean getIsindent() {
		return isindent;
	}

	public void setIsindent(Boolean isindent) {
		this.isindent = isindent;
	}

	public Boolean getIsreturn() {
		return isreturn;
	}

	public void setIsreturn(Boolean isreturn) {
		this.isreturn = isreturn;
	}

	public Integer getRejectedquantity() {
		return rejectedquantity;
	}

	public void setRejectedquantity(Integer rejectedquantity) {
		this.rejectedquantity = rejectedquantity;
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

	public List<StockTransaction> getStockTransactionList() {
		return stockTransactionList;
	}

	public void setStockTransactionList(List<StockTransaction> stockTransactionList) {
		this.stockTransactionList = stockTransactionList;
	}

	public GoodReceiptNoteDetail getGoodReceiptNoteDetail() {
		return goodReceiptNoteDetail;
	}

	public void setGoodReceiptNoteDetail(GoodReceiptNoteDetail goodReceiptNoteDetail) {
		this.goodReceiptNoteDetail = goodReceiptNoteDetail;
	}

	public List<ShelfItem> getShelfItemList() {
		return shelfItemList;
	}

	public void setShelfItemList(List<ShelfItem> shelfItemList) {
		this.shelfItemList = shelfItemList;
	}

	public List<BatchAccessoriesMapper> getBatchAccessoriesMapperList() {
		return batchAccessoriesMapperList;
	}

	public void setBatchAccessoriesMapperList(List<BatchAccessoriesMapper> batchAccessoriesMapperList) {
		this.batchAccessoriesMapperList = batchAccessoriesMapperList;
	}

	public Boolean getIsconsignment() {
		return isconsignment;
	}

	public void setIsconsignment(Boolean isconsignment) {
		this.isconsignment = isconsignment;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BatchResponseConsignment() {
		super();
	}

	public BatchResponseConsignment(List<Long> list) {
		super();

	}

	public BatchResponseConsignment(Long id) {
		this.id = id;
	}

	public BatchResponseConsignment(Long id, Integer currentQuantity) {
		this(id);
		this.currentquantity = currentQuantity;
	}

	
}
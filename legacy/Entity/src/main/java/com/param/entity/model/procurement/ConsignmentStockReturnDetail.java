package com.param.entity.model.procurement;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

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
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;

@Entity(name = "ConsignmentStockReturnDetail")
@Table(name = "t_consignment_return_detail", schema = "procurement")
public class ConsignmentStockReturnDetail extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "grn_detail_id")
	private GoodReceiptNoteDetail goodReceiptNoteDetail;

	@Column(name = "batch_no")
	private String batchNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private User approvedBy;

	@Column(name = "approved_date")
	private Date approvedDate;

	@Column(name = "return_quantity")
	private Integer returnQuantity;
	
	@Column(name = "total_amount")
	private Double totalAmount;
	
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	@Column(name = "lot_number")
	private String lotNumber;

	private Double mrp;

	private String remark;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id")
	private Batch batch;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cr_id")
	private ConsignmentStockReturn consignmentStockReturn;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manufacturer_id")
	private Manufacturer manufacturer;
	
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id")
	private Vendor vendor;*/
	
	/*@OneToMany(fetch = FetchType.LAZY, mappedBy = "goodReceiptNoteDetail", cascade = CascadeType.ALL)
	private List<GrnUomMapper> grnUomMappersList;*/
		
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public GoodReceiptNoteDetail getGoodReceiptNoteDetail() {
		return goodReceiptNoteDetail;
	}

	public void setGoodReceiptNoteDetail(GoodReceiptNoteDetail goodReceiptNoteDetail) {
		this.goodReceiptNoteDetail = goodReceiptNoteDetail;
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

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
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

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}	
	
	/*public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}*/

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}
	
	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public ConsignmentStockReturn getConsignmentStockReturn() {
		return consignmentStockReturn;
	}

	public void setConsignmentStockReturn(ConsignmentStockReturn consignmentStockReturn) {
		this.consignmentStockReturn = consignmentStockReturn;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}	
	
	/*public List<GrnUomMapper> getGrnUomMappersList() {
		return grnUomMappersList;
	}

	public void setGrnUomMappersList(List<GrnUomMapper> grnUomMappersList) {
		this.grnUomMappersList = grnUomMappersList;
	}*/

	public ConsignmentStockReturnDetail() {
		super();
	}

	public ConsignmentStockReturnDetail(Integer id) {
		super();
		this.id = id;
	}
	
	public ConsignmentStockReturnDetail(String batchNo, Double totalAmount, Integer returnQuantity, Double cop, Date expiryDate,
			UomType grnUomType, UomUnit grnUomUnit, Item item, String lotNumber, Double mrp, String remark, Status status) {
		super();
		this.batchNo = batchNo;
		this.totalAmount = totalAmount;
		this.returnQuantity = returnQuantity;
		this.cop = cop;
		this.expiryDate = expiryDate;
		this.grnUomType = grnUomType;
		this.grnUomUnit = grnUomUnit;
		this.item = item;
		this.lotNumber = lotNumber;
		this.mrp = mrp;
		this.remark = remark;
		this.status = status;
	}
	
	public ConsignmentStockReturnDetail(Batch batch, String batchNo, Double totalAmount, Integer returnQuantity, Double cop, Date expiryDate,
			UomType grnUomType, UomUnit grnUomUnit, Item item, String lotNumber, Double mrp, String remark, Status status,Manufacturer manufacturer,GoodReceiptNoteDetail goodReceiptNoteDetail) {
		super();
		this.batch=batch;
		this.batchNo = batchNo;
		this.totalAmount = totalAmount;
		this.returnQuantity = returnQuantity;
		this.cop = cop;
		this.expiryDate = expiryDate;
		this.grnUomType = grnUomType;
		this.grnUomUnit = grnUomUnit;
		this.item = item;
		this.lotNumber = lotNumber;
		this.mrp = mrp;
		this.remark = remark;
		this.status = status;
		this.manufacturer = manufacturer;
		this.goodReceiptNoteDetail = goodReceiptNoteDetail;
	}
	
}
package com.param.entity.model.pharmacy;

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

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;

@Entity(name = "BillableConsumptionDetail")
@Table(name = "t_billable_consumption_detail", schema = "pharmacy")
public class BillableConsumptionDetail extends BaseEntity implements Serializable {
	public SaleDetail getSaleDetail() {
		return saleDetail;
	}

	public void setSaleDetail(SaleDetail saleDetail) {
		this.saleDetail = saleDetail;
	}

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "billable_consumption_id")
	private BillableConsumption billableConsumption;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id")
	private Batch batch;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_type_id")
	private UomType uomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_unit_id")
	private UomUnit uomUnit;

	@Column(name = "consumable_quantity")
	private Integer consumableQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_id")
	private Tax tax;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sale_detail_id")
	private SaleDetail saleDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@Column(name = "total_amount")
	private Double totalAmount;

	@Column(name = "tax_amount")
	private Double taxAmount;

	@Column(name = "net_amount")
	private Double netAmount;

	@Column(name = "administration_date")
	private Date dateAdministrationDate;

	@Column(name = "administration_time")
	private String administrationTime;

	@Column(name = "administration_remark")
	private String remark;

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Tax getTax() {
		return tax;
	}

	public void setTax(Tax tax) {
		this.tax = tax;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Date getDateAdministrationDate() {
		return dateAdministrationDate;
	}

	public void setDateAdministrationDate(Date dateAdministrationDate) {
		this.dateAdministrationDate = dateAdministrationDate;
	}

	public String getAdministrationTime() {
		return administrationTime;
	}

	public void setAdministrationTime(String administrationTime) {
		this.administrationTime = administrationTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BillableConsumption getBillableConsumption() {
		return billableConsumption;
	}

	public void setBillableConsumption(BillableConsumption billableConsumption) {
		this.billableConsumption = billableConsumption;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
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

	public Integer getConsumableQuantity() {
		return consumableQuantity;
	}

	public void setConsumableQuantity(Integer consumableQuantity) {
		this.consumableQuantity = consumableQuantity;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public BillableConsumptionDetail() {
		super();
	}

	public BillableConsumptionDetail(Integer id) {
		super();
		this.id = id;
	}

	public BillableConsumptionDetail(Batch batch, UomType uomType, UomUnit uomUnit, Integer consumableQuantity,
			Status status) {
		super();
		this.batch = batch;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.consumableQuantity = consumableQuantity;
		this.status = status;
	}
}
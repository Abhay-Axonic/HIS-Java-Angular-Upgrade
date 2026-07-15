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
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;

@Entity(name = "ConsignmentInwardDetail")
@Table(name = "t_consignment_inward_detail", schema = "procurement")
public class ConsignmentInwardDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "batch_number")
	private String batchNumber;

	private Double cop;

	@Column(name = "discount_amount")
	private Double discountAmount;

	@Column(name = "discount_percentage")
	private Double discountPercentage;

	@Column(name = "expiry_date")
	private Date expiryDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@Column(name = "net_amount")
	private Double netAmount;

	private Integer quantity;
	@Column(name = "lcop")
	private Double lcop;
	public Double getLcop() {
		return lcop;
	}

	public void setLcop(Double lcop) {
		this.lcop = lcop;
	}

	@Column(name = "tax_amount")
	private Double taxAmount;

	@Column(name = "total_amount")
	private Double totalAmount;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_id")
	private Tax tax;

	@Column(name = "tax_percentage")
	private Double taxPercentage;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_type_id")
	private UomType uomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_unit_id")
	private UomUnit uomUnit;

	// bi-directional many-to-one association to ConsignmentInward
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inward_id")
	private ConsignmentInward consignmentInward;

	// bi-directional many-to-one association to ConsignmentInwardUomMapper
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "consignmentInwardDetail", cascade = CascadeType.ALL)
	private List<ConsignmentInwardUomMapper> consignmentInwardUomMappers;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "consignmentInwardDetail", cascade = CascadeType.ALL)
	private List<ConsignmentInwardTaxComponent> consignmentInwardTaxComponents;

	public ConsignmentInwardDetail() {
		super();
	}

	public ConsignmentInwardDetail(Integer id) {
		super();
		this.id = id;
	}

	public ConsignmentInwardDetail(String batchNumber, Double cop, Double discountAmount, Double discountPercentage,
			Date expiryDate, Item item, Double netAmount, Integer quantity, Double taxAmount, Tax tax,
			Double taxPercentage, UomType uomType, UomUnit uomUnit, Double totalAmount) {
		this.batchNumber = batchNumber;
		this.cop = cop;
		this.discountAmount = discountAmount;
		this.discountPercentage = discountPercentage;
		this.expiryDate = expiryDate;
		this.item = item;
		this.netAmount = netAmount;
		this.quantity = quantity;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.totalAmount=totalAmount;
	}

	public ConsignmentInwardDetail(String batchNumber, Double cop, Double discountAmount, Double discountPercentage,
			Date expiryDate, Item item, Double netAmount, Integer quantity, Double taxAmount, Tax tax,
			Double taxPercentage, UomType uomType, UomUnit uomUnit, Double totalAmount , Double lcop) {
		this.batchNumber = batchNumber;
		this.cop = cop;
		this.discountAmount = discountAmount;
		this.discountPercentage = discountPercentage;
		this.expiryDate = expiryDate;
		this.item = item;
		this.netAmount = netAmount;
		this.quantity = quantity;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.totalAmount=totalAmount;
		this.lcop= lcop;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
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

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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

	public ConsignmentInward getConsignmentInward() {
		return consignmentInward;
	}

	public void setConsignmentInward(ConsignmentInward consignmentInward) {
		this.consignmentInward = consignmentInward;
	}

	public List<ConsignmentInwardUomMapper> getConsignmentInwardUomMappers() {
		return consignmentInwardUomMappers;
	}

	public void setConsignmentInwardUomMappers(List<ConsignmentInwardUomMapper> consignmentInwardUomMappers) {
		this.consignmentInwardUomMappers = consignmentInwardUomMappers;
	}

	public List<ConsignmentInwardTaxComponent> getConsignmentInwardTaxComponents() {
		return consignmentInwardTaxComponents;
	}

	public void setConsignmentInwardTaxComponents(List<ConsignmentInwardTaxComponent> consignmentInwardTaxComponents) {
		this.consignmentInwardTaxComponents = consignmentInwardTaxComponents;
	}

	
	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public ConsignmentInwardUomMapper addConsignmentInwardUomMapper(
			ConsignmentInwardUomMapper consignmentInwardUomMapper) {
		if (getConsignmentInwardUomMappers() == null)
			this.consignmentInwardUomMappers = new ArrayList<ConsignmentInwardUomMapper>();
		getConsignmentInwardUomMappers().add(consignmentInwardUomMapper);
		consignmentInwardUomMapper.setConsignmentInwardDetail(this);

		return consignmentInwardUomMapper;
	}

	public ConsignmentInwardUomMapper removeConsignmentInwardUomMapper(
			ConsignmentInwardUomMapper consignmentInwardUomMapper) {
		getConsignmentInwardUomMappers().remove(consignmentInwardUomMapper);
		consignmentInwardUomMapper.setConsignmentInwardDetail(null);

		return consignmentInwardUomMapper;
	}

	public ConsignmentInwardTaxComponent addConsignmentInwardTaxCom(ConsignmentInwardTaxComponent taxComponent) {
		if (this.consignmentInwardTaxComponents == null)
			this.consignmentInwardTaxComponents = new ArrayList<ConsignmentInwardTaxComponent>();

		this.consignmentInwardTaxComponents.add(taxComponent);
		taxComponent.setConsignmentInwardDetail(this);

		return taxComponent;
	}

	public void updateConsignmentInwardDetail(String batchNumber, Double cop, Double discountAmount,
			Double discountPercentage, Date expiryDate, Item item, Double netAmount, Integer quantity, Double taxAmount,
			Tax tax, Double taxPercentage, UomType uomType, UomUnit uomUnit,Double totalamount) {
		this.batchNumber = batchNumber != null ? batchNumber : this.batchNumber;
		this.cop = cop;
		this.discountAmount = discountAmount;
		this.discountPercentage = discountPercentage;
		this.expiryDate = expiryDate;
		this.item = item;
		this.netAmount = netAmount;
		this.quantity = quantity;
		this.taxAmount = taxAmount;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.totalAmount=totalamount;
	}
}

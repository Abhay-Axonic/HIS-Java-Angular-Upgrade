package com.param.entity.model.procurement;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.procurement.ConsignmentOutward;


/* The persistent class for the t_consignment_outward_detail database table.
* 
*/
@Entity(name = "ConsignmentOutwardDetail")
@Table(name = "t_consignment_outward_detail", schema = "procurement")
public class ConsignmentOutwardDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id")
	private Batch batch;

	private double cop;

	@Column(name = "return_qunatity")
	private Integer returnQunatity;

	public double getLcop() {
		return lcop;
	}

	public void setLcop(double lcop) {
		this.lcop = lcop;
	}

	@Column(name = "total_amount")
	private double totalAmount;
	@Column(name = "lcop")
	private double lcop;

	@Column(name = "tax_amount")
	private double taxAmount;

	@Column(name = "net_amount")
	private double netAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_id")
	private Tax tax;

	@Column(name = "tax_percentage")
	private double taxPercentage;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_type_id")
	private UomType uomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_unit_id")
	private UomUnit uomUnit;

	// bi-directional many-to-one association to ConsignmentOutward

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "outward_id")
	private ConsignmentOutward consignmentOutward;

	public ConsignmentOutwardDetail() {
		super();
	}

	public ConsignmentOutwardDetail(Batch batch, Integer returnQunatity, Tax tax, Double taxPercentage,
			Double taxAmount, Double netAmount, Double cop, UomUnit uomUnit, UomType uomType) {
		this.batch = batch;
		this.returnQunatity = returnQunatity;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.taxAmount = taxAmount;
		this.netAmount = netAmount;
		this.cop = cop;
		this.uomUnit = uomUnit;
		this.uomType = uomType;

	}
	public ConsignmentOutwardDetail(Batch batch, Integer returnQunatity, Tax tax, Double taxPercentage,
			Double taxAmount, Double netAmount, Double cop, UomUnit uomUnit, UomType uomType, Double lcop) {
		this.batch = batch;
		this.returnQunatity = returnQunatity;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.taxAmount = taxAmount;
		this.netAmount = netAmount;
		this.cop = cop;
		this.uomUnit = uomUnit;
		this.uomType = uomType;
		this.lcop = lcop;

	}

	public ConsignmentOutwardDetail(Integer id, Batch batch, Integer returnQunatity, UomType uomType, UomUnit uomUnit,
			Double cop, Tax tax, Double taxPercentage, Double totalAmount, Double taxAmount,Double netAmount)
	{
		this.id=id;
		this.batch=batch;
		this.returnQunatity = returnQunatity;
		this.uomType=uomType;
		this.uomUnit = uomUnit;
		this.cop = cop;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.totalAmount=totalAmount;
		this.taxAmount = taxAmount;
		this.netAmount = netAmount;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public double getCop() {
		return this.cop;
	}

	public void setCop(double cop) {
		this.cop = cop;
	}

	public Integer getReturnQunatity() {
		return this.returnQunatity;
	}

	public void setReturnQunatity(Integer returnQunatity) {
		this.returnQunatity = returnQunatity;
	}

	public double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	public double getTaxAmount() {
		return this.taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public double getTaxPercentage() {
		return this.taxPercentage;
	}

	public void setTaxPercentage(double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public ConsignmentOutward getConsignmentOutward() {
		return this.consignmentOutward;
	}

	public void setConsignmentOutward(ConsignmentOutward consignmentOutward) {
		this.consignmentOutward = consignmentOutward;
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

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public Tax getTax() {
		return tax;
	}

	public void setTax(Tax tax) {
		this.tax = tax;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void updateConsignmentOutwardDetail(Integer id, Batch batch, Integer returnQunatity, UomType uomType,UomUnit uomUnit,
			Double cop, Tax tax, Double taxPercentage, Double totalAmount, Double taxAmount,
			Double netAmount) 
	{
		
		this.id=id;
		this.batch=batch;
		this.returnQunatity = returnQunatity;
		this.uomType=uomType;
		this.uomUnit = uomUnit;
		this.cop = cop;
		this.tax = tax;
		this.taxPercentage = taxPercentage;
		this.totalAmount=totalAmount;
		this.taxAmount = taxAmount;
		this.netAmount = netAmount;
	}


}

	
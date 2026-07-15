package com.param.entity.model.procurement;

import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.TaxComponent;

/**
 * The persistent class for the t_consignment_outward_detail database table.
 * 
 */
@Entity(name = "ConsignmentOutwardTaxComponent")
@Table(name="t_consignment_outward_tax_component", schema = "procurement")
public class ConsignmentOutwardTaxComponent extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name="tax_amount")
	private double taxAmount;
	
	@Column(name="percentage")
	private double percentage;

	
	@Column(name="amount")
	private double amount;

	//bi-directional many-to-one association to ConsignmentOutward
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="outward_detail_id")
	private ConsignmentOutwardDetail consignmentOutwardDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_component_id")
	private TaxComponent taxComponent;
	public ConsignmentOutwardTaxComponent() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public double getTaxAmount() {
		return this.taxAmount;
	}

	public TaxComponent getTaxComponent() {
		return taxComponent;
	}

	public void setTaxComponent(TaxComponent taxComponent) {
		this.taxComponent = taxComponent;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}
	
	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

    public ConsignmentOutwardDetail getConsignmentOutwardDetail() {
		return this.consignmentOutwardDetail;
	}

	public void setConsignmentOutwardDetail(ConsignmentOutwardDetail consignmentOutwardDetail) {
		this.consignmentOutwardDetail = consignmentOutwardDetail;
	}

}
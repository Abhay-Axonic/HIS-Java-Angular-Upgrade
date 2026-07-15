package com.param.entity.model.inventory;

import java.io.Serializable;

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
import com.param.entity.model.master.TaxComponent;

@Entity
@Table(name = "t_returnable_gatepass_tax_component", schema = "inventory")
public class ReturnableGatepassTaxComponent extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	private double amount;

	private double percentage;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "returnable_gatepass_detail_id")
	private ReturnableGatepassDetail returnableGatepassDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_component_id")
	private TaxComponent taxComponent;

	public ReturnableGatepassTaxComponent() {
		super();
	}

	public ReturnableGatepassTaxComponent(double amount, double percentage,
			ReturnableGatepassDetail returnableGatepassDetail, TaxComponent taxComponent) {
		super();
		this.amount = amount;
		this.percentage = percentage;
		this.returnableGatepassDetail = returnableGatepassDetail;
		this.taxComponent = taxComponent;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public ReturnableGatepassDetail getReturnableGatepassDetail() {
		return returnableGatepassDetail;
	}

	public void setReturnableGatepassDetail(ReturnableGatepassDetail returnableGatepassDetail) {
		this.returnableGatepassDetail = returnableGatepassDetail;
	}

	public TaxComponent getTaxComponent() {
		return taxComponent;
	}

	public void setTaxComponent(TaxComponent taxComponent) {
		this.taxComponent = taxComponent;
	}

}
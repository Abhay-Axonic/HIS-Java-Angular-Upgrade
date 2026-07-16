package com.param.entity.model.inventory;

import java.io.Serial;
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

@Entity(name = "OpbTaxComponent")
@Table(name = "t_opb_tax_component", schema = "inventory")
public class OpbTaxComponent extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	private double amount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "opb_detail_id")
	private OpeningBalanceDetail openingBalanceDetail;

	private double percentage;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_component_id")
	private TaxComponent taxComponent;

	public OpbTaxComponent() {
	}

	public OpbTaxComponent(double amount, OpeningBalanceDetail openingBalanceDetail, double percentage,
			TaxComponent taxComponent) {
		super();
		this.amount = amount;
		this.openingBalanceDetail = openingBalanceDetail;
		this.percentage = percentage;
		this.taxComponent = taxComponent;
	}

	public Integer getId() {
		return id;
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

	public OpeningBalanceDetail getOpeningBalanceDetail() {
		return openingBalanceDetail;
	}

	public void setOpeningBalanceDetail(OpeningBalanceDetail openingBalanceDetail) {
		this.openingBalanceDetail = openingBalanceDetail;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public TaxComponent getTaxComponent() {
		return taxComponent;
	}

	public void setTaxComponent(TaxComponent taxComponent) {
		this.taxComponent = taxComponent;
	}

}

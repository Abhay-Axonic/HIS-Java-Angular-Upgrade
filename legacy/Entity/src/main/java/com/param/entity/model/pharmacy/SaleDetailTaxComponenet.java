package com.param.entity.model.pharmacy;

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

@Entity
@Table(name = "t_sale_detail_tax_componenet", schema = "pharmacy")
public class SaleDetailTaxComponenet extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;

	private double amount;

	private double percentage;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sale_detail_id")
	private SaleDetail saleDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_component_id")
	private TaxComponent taxComponent;

	public SaleDetailTaxComponenet() {
	}
	
	public SaleDetailTaxComponenet(double amount, SaleDetail saleDetail, double percentage,
			TaxComponent taxComponent) {
		super();
		this.amount = amount;
		this.saleDetail = saleDetail;
		this.percentage = percentage;
		this.taxComponent = taxComponent;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public SaleDetail getSaleDetail() {
		return saleDetail;
	}

	public void setSaleDetail(SaleDetail saleDetail) {
		this.saleDetail = saleDetail;
	}

	public TaxComponent getTaxComponent() {
		return taxComponent;
	}

	public void setTaxComponent(TaxComponent taxComponent) {
		this.taxComponent = taxComponent;
	}

}

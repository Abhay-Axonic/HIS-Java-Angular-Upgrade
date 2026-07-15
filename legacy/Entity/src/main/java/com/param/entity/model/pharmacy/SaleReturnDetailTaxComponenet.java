package com.param.entity.model.pharmacy;

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
@Table(name = "t_sale_return_detail_tax_componenet", schema = "pharmacy")
public class SaleReturnDetailTaxComponenet extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;

	private double amount;

	private double percentage;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sale_return_detail_id")
	private ReturnDetail returnDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_component_id")
	private TaxComponent taxComponent;

	public SaleReturnDetailTaxComponenet() {
	}
	
	public SaleReturnDetailTaxComponenet(double amount, ReturnDetail returnDetail, double percentage,
			TaxComponent taxComponent) {
		super();
		this.amount = amount;
		this.returnDetail = returnDetail;
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

	public ReturnDetail getReturnDetail() {
		return returnDetail;
	}

	public void setReturnDetail(ReturnDetail returnDetail) {
		this.returnDetail = returnDetail;
	}

	public TaxComponent getTaxComponent() {
		return taxComponent;
	}

	public void setTaxComponent(TaxComponent taxComponent) {
		this.taxComponent = taxComponent;
	}

}

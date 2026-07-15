package com.param.entity.model.procurement;

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

@Entity(name = "ConsignmentInwardTaxComponent")
@Table(name = "t_consignment_inward_tax_component", schema = "procurement")
public class ConsignmentInwardTaxComponent extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	private Double amount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inward_detail_id")
	private ConsignmentInwardDetail consignmentInwardDetail;

	private Double percentage;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_component_id")
	private TaxComponent taxComponent;

	public ConsignmentInwardTaxComponent() {
	}

	public ConsignmentInwardTaxComponent(Double amount, ConsignmentInwardDetail consignmentInwardDetail,
			Double percentage, TaxComponent taxComponent) {
		super();
		this.amount = amount;
		this.consignmentInwardDetail = consignmentInwardDetail;
		this.percentage = percentage;
		this.taxComponent = taxComponent;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public ConsignmentInwardDetail getConsignmentInwardDetail() {
		return consignmentInwardDetail;
	}

	public void setConsignmentInwardDetail(ConsignmentInwardDetail consignmentInwardDetail) {
		this.consignmentInwardDetail = consignmentInwardDetail;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public TaxComponent getTaxComponent() {
		return taxComponent;
	}

	public void setTaxComponent(TaxComponent taxComponent) {
		this.taxComponent = taxComponent;
	}

}
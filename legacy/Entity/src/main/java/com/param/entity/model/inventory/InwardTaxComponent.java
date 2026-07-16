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

@Entity(name = "InwardTaxComponent")
@Table(name = "t_inward_tax_component", schema = "inventory")
public class InwardTaxComponent extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	private double amount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inward_detail_id")
	private MaterialInwardDetail materialInwardDetail;

	private double percentage;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_component_id")
	private TaxComponent taxComponent;

	public InwardTaxComponent() {
		super();
	}

	public InwardTaxComponent(Integer id) {
		super();
		this.id = id;
	}

	public InwardTaxComponent(double amount) {
		super();
		this.amount = amount;
	}

	public InwardTaxComponent(double amount, MaterialInwardDetail materialInwardDetail, double percentage,
			TaxComponent taxComponent) {
		super();
		this.amount = amount;
		this.materialInwardDetail = materialInwardDetail;
		this.percentage = percentage;
		this.taxComponent = taxComponent;
	}

	public Integer getId() {
		return this.id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public MaterialInwardDetail getMaterialInwardDetail() {
		return materialInwardDetail;
	}

	public void setMaterialInwardDetail(MaterialInwardDetail materialInwardDetail) {
		this.materialInwardDetail = materialInwardDetail;
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

	public void setId(Integer id) {
		this.id = id;
	}

}

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
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;

@Entity(name = "LoanOutDetail")
@Table(name = "t_loan_out_detail", schema = "procurement")
public class LoanOutDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id")
	private Batch batch;

	@Column(name = "loan_out_quantity")
	private Integer loanOutQuantity;

	private String remark;
	
	@Column(name = "cop")
	private Double cop;

	@Column(name = "total_amount")
	private Double totalAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_type_id")
	private UomType uomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_unit_id")
	private UomUnit uomUnit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loan_out_id")
	private LoanOut loanOut;
	
	public LoanOutDetail() {
	}

	public LoanOutDetail(Integer id) {
		super();
		this.id = id;
	}

	public LoanOutDetail(Batch batch, Integer loanOutQuantity, String remark, Double totalAmount, UomType uomType,
			UomUnit uomUnit, Double cop) {
		super();
		this.batch = batch;
		this.loanOutQuantity = loanOutQuantity;
		this.remark = remark;
		this.totalAmount = totalAmount;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.cop = cop;
	}
	
	public LoanOutDetail(Batch batch, Integer loanOutQuantity, String remark, Double totalAmount, Double cop) {
		super();
		this.batch = batch;
		this.loanOutQuantity = loanOutQuantity;
		this.remark = remark;
		this.totalAmount = totalAmount;
		this.cop = cop;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public Integer getLoanOutQuantity() {
		return loanOutQuantity;
	}

	public void setLoanOutQuantity(Integer loanOutQuantity) {
		this.loanOutQuantity = loanOutQuantity;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
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

	public LoanOut getLoanOut() {
		return loanOut;
	}

	public void setLoanOut(LoanOut loanOut) {
		this.loanOut = loanOut;
	}

	public void updateLoanOutDetail(Batch batch, Integer loanOutQuantity, String remark, Double cop,
			Double totalAmount, UomType uomType, UomUnit uomUnit) {
		this.batch = batch;
		this.loanOutQuantity = loanOutQuantity;
		this.remark = remark;
		this.cop = cop;
		this.totalAmount = totalAmount;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
	}
	
	public void updateLoanOutDetail(Batch batch, Integer loanOutQuantity, String remark, Double cop,
			Double totalAmount) {
		this.batch = batch;
		this.loanOutQuantity = loanOutQuantity;
		this.remark = remark;
		this.cop = cop;
		this.totalAmount = totalAmount;
	}
}
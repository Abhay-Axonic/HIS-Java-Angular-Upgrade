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
import com.param.entity.model.master.Item;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;

@Entity(name = "LoanOutReceiveDetail")
@Table(name = "t_loanout_receive_detail", schema = "procurement")
public class LoanOutReceiveDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id")
	private Batch batch;
    /*
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;*/

	private String remark;

	@Column(name = "receive_quantity")
	private Integer receiveQuantity;

	@Column(name = "total_amount")
	private Double totalAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_type_id")
	private UomType uomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_unit_id")
	private UomUnit uomUnit;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loan_out_detail_id")
	private LoanOutDetail loanOutDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loanout_receive_id")
	private LoanOutReceive loanOutReceive;

	public LoanOutReceiveDetail() {
	}

	public LoanOutReceiveDetail(Batch batch, String remark, Integer receiveQuantity, UomType uomType,
			UomUnit uomUnit, LoanOutDetail loanOutDetail, Double totalAmount) {
		super();
		this.batch = batch;
		this.remark = remark;
		this.receiveQuantity = receiveQuantity;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.loanOutDetail = loanOutDetail;
		this.totalAmount = totalAmount;
	}
	
	public LoanOutReceiveDetail(Batch batch, String remark, Integer receiveQuantity, LoanOutDetail loanOutDetail, Double totalAmount) {
		super();
		this.batch = batch;
		this.remark = remark;
		this.receiveQuantity = receiveQuantity;
		this.loanOutDetail = loanOutDetail;
		this.totalAmount = totalAmount;
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

	/*public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
*/
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getReceiveQuantity() {
		return receiveQuantity;
	}

	public void setReceiveQuantity(Integer receiveQuantity) {
		this.receiveQuantity = receiveQuantity;
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

	public LoanOutDetail getLoanOutDetail() {
		return loanOutDetail;
	}

	public void setLoanOutDetail(LoanOutDetail loanOutDetail) {
		this.loanOutDetail = loanOutDetail;
	}

	public LoanOutReceive getLoanOutReceive() {
		return loanOutReceive;
	}

	public void setLoanOutReceive(LoanOutReceive loanOutReceive) {
		this.loanOutReceive = loanOutReceive;
	}

	public void updateLoanOutReceiveDetail(Batch batch, Integer receiveQuantity, String remark,
			Double totalAmount, UomType uomType, UomUnit uomUnit) {
		this.batch = batch;
		this.receiveQuantity = receiveQuantity;
		this.remark = remark;
		this.totalAmount = totalAmount;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
	}
	
	public void updateLoanOutReceiveDetail(Batch batch, Integer receiveQuantity, String remark,
			Double totalAmount) {
		this.batch = batch;
		this.receiveQuantity = receiveQuantity;
		this.remark = remark;
		this.totalAmount = totalAmount;
	}

}
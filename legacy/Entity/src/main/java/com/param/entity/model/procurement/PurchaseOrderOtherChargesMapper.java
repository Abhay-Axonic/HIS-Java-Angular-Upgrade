package com.param.entity.model.procurement;

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
import com.param.entity.model.master.OtherCharge;


@Entity(name = "PurchaseOrderOtherChargesMapper")
@Table(name = "t_other_charges_po_mapper", schema = "procurement")
public class PurchaseOrderOtherChargesMapper extends BaseEntity implements Serializable{

	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "purchase_order_id")
	private PurchaseOrder purchaseOrder;
	
	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "other_charges_id")
	private OtherCharge otherCharge;
	
	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "is_from_po")
	private boolean isFromPo;
	
	public PurchaseOrderOtherChargesMapper() {
		super();
	}

	public PurchaseOrderOtherChargesMapper(OtherCharge otherCharge, Double amount) {
		super();
		this.otherCharge = otherCharge;
		this.amount = amount;
	}
	
	public void updatePoOtherChargesMapper(OtherCharge otherCharge, Double amount) {
		this.otherCharge = otherCharge;
		this.amount = amount;
	}
	public void updatePoOtherChargesMapper(OtherCharge otherCharge, Double amount,boolean isFromPo) {
		this.otherCharge = otherCharge;
		this.amount = amount;
		this.isFromPo=isFromPo;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public OtherCharge getOtherCharge() {
		return otherCharge;
	}

	public void setOtherCharge(OtherCharge otherCharge) {
		this.otherCharge = otherCharge;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public boolean getIsFromPo() {
		return isFromPo;
	}

	public void setIsFromPo(boolean isFromPo) {
		this.isFromPo = isFromPo;
	}
	

	
}

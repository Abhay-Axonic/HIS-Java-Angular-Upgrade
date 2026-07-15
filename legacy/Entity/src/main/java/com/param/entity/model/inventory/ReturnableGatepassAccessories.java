package com.param.entity.model.inventory;

import java.util.ArrayList;

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

@Entity(name = "ReturnableGatepassAccessories")
@Table(name = "t_returnable_gatepass_accessories", schema = "inventory")
public class ReturnableGatepassAccessories extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "returnable_gatepass_detail_id")
	private ReturnableGatepassDetail returnableGatepassDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id")
	private Batch batch;

	@Column(name = "approved_quantity")
	private Integer approvedQty;

	@Column(name = "specification")
	private String specification;

	@Column(name = "quantity")
	private Integer quantity = 0;

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public ReturnableGatepassAccessories() {
		super();
	}

	public ReturnableGatepassAccessories(Integer id) {
		super();
		this.id = id;
	}

	public ReturnableGatepassAccessories(Integer quantity, Integer approvedQty) {
		super();
		this.quantity = quantity;
		this.approvedQty = approvedQty;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public ReturnableGatepassDetail getReturnableGatepassDetail() {
		return returnableGatepassDetail;
	}

	public void setReturnableGatepassDetail(ReturnableGatepassDetail returnableGatepassDetail) {
		this.returnableGatepassDetail = returnableGatepassDetail;
	}

	public Integer getApprovedQty() {
		return approvedQty;
	}

	public void setApprovedQty(Integer approvedQty) {
		this.approvedQty = approvedQty;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch=batch;
	}


	public ReturnableGatepassAccessories(Batch batch, Integer quantity, String specification) {
		this.quantity = quantity;
		this.batch = batch;
		this.specification = specification;

	}
	
	public ReturnableGatepassAccessories(Integer id,Batch batch, Integer quantity, String specification) {
		this.id=id;
		this.quantity = quantity;
		this.batch = batch;
		this.specification = specification;

	}
	
	public ReturnableGatepassAccessories(Integer quantity, String specification) {
		this.quantity = quantity;
		this.specification = specification;

	}

	
	
	

}

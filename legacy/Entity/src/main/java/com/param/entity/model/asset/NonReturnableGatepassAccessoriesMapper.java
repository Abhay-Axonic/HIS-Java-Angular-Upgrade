package com.param.entity.model.asset;

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

@Entity(name = "NonReturnableGatepassAccessoriesMapper")
@Table(name = "t_nrg_accessories_mapper", schema = "assets")
public class NonReturnableGatepassAccessoriesMapper extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "non_returnable_gatepass_detail_id", nullable = false)
	private NonReturnableGatepassDetail nonReturnableGatepassDetail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id", nullable = false)
	private Batch batch;
	
	@Column(name = "quantity")
	private Integer quantity;

	public Integer getId() {
		return id;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public NonReturnableGatepassDetail getNonReturnableGatepassDetail() {
		return nonReturnableGatepassDetail;
	}

	public void setNonReturnableGatepassDetail(NonReturnableGatepassDetail nonReturnableGatepassDetail) {
		this.nonReturnableGatepassDetail = nonReturnableGatepassDetail;
	}

	public NonReturnableGatepassAccessoriesMapper() {
		super();
	}

	public NonReturnableGatepassAccessoriesMapper(Batch batch, Integer quantity) {
		super();
		this.batch = batch;
		this.quantity = quantity;
	}
	
	public void updateNonReturnableGatepassAccessoriesMapper(Batch batch, Integer quantity) {
		this.batch = batch;
		this.quantity = quantity;
	}
}
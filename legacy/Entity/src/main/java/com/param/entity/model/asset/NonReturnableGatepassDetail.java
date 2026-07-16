package com.param.entity.model.asset;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.Status;

@Entity(name = "NonReturnableGatepassDetail")
@Table(name = "t_non_returnable_gatepass_detail", schema = "assets")
public class NonReturnableGatepassDetail extends BaseEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "non_returnable_gatepass_id", nullable = false)
	private NonReturnableGatepass nonReturnableGatepass;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "auction_requisition_detail_id")
	private AuctionRequisitionDetail auctionRequisitionDetail;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "nonReturnableGatepassDetail", cascade = CascadeType.ALL)
	private List<NonReturnableGatepassAccessoriesMapper> nonReturnableGatepassAccessoriesMapper;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id", nullable = false)
	private Batch batch;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "remark")
	private String remark;

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

	public AuctionRequisitionDetail getAuctionRequisitionDetail() {
		return auctionRequisitionDetail;
	}

	public void setAuctionRequisitionDetail(AuctionRequisitionDetail auctionRequisitionDetail) {
		this.auctionRequisitionDetail = auctionRequisitionDetail;
	}

	public List<NonReturnableGatepassAccessoriesMapper> getNonReturnableGatepassAccessoriesMapper() {
		return nonReturnableGatepassAccessoriesMapper;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setNonReturnableGatepassAccessoriesMapper(
			List<NonReturnableGatepassAccessoriesMapper> nonReturnableGatepassAccessoriesMapper) {
		this.nonReturnableGatepassAccessoriesMapper = nonReturnableGatepassAccessoriesMapper;
	}

	public NonReturnableGatepassAccessoriesMapper addNonReturnableGatepassAccessoriesMapper(NonReturnableGatepassAccessoriesMapper nonReturnableGatepassAccessoriesMapper) {
		if (getNonReturnableGatepassAccessoriesMapper() == null)
			this.nonReturnableGatepassAccessoriesMapper = new ArrayList<NonReturnableGatepassAccessoriesMapper>();

		getNonReturnableGatepassAccessoriesMapper().add(nonReturnableGatepassAccessoriesMapper);
		nonReturnableGatepassAccessoriesMapper.setNonReturnableGatepassDetail(this);

		return nonReturnableGatepassAccessoriesMapper;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public NonReturnableGatepass getNonReturnableGatepass() {
		return nonReturnableGatepass;
	}

	public void setNonReturnableGatepass(NonReturnableGatepass nonReturnableGatepass) {
		this.nonReturnableGatepass = nonReturnableGatepass;
	}

	public NonReturnableGatepassDetail() {
		super();
	}

	public NonReturnableGatepassDetail(Batch batch, Integer quantity, Double amount, String remark, Status status) {
		super();
		this.batch = batch;
		this.quantity = quantity;
		this.amount = amount;
		this.remark = remark;
		this.status = status;
	}

	public void updateNonReturnableGatepassDetail(Integer quantity, Double amount, String remark) {
		this.quantity = quantity;
		this.amount = amount;
		this.remark = remark;
	}
}
	
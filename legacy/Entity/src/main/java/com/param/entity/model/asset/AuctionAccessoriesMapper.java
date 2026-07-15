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
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;

@Entity(name = "AuctionAccessoriesMapper")
@Table(name = "t_auction_accessories_mapper", schema = "assets")
public class AuctionAccessoriesMapper extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id")
	private Batch batch;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auction_requisition_detail_id")
	private AuctionRequisitionDetail auctionRequisitionDetail;

	@Column(name = "auction_quantity")
	private Integer auctionQuantity;

	@Column(name = "approved_auction_quantity")
	private Integer approvedAuctionQuantity;
	
	@Column(name = "reject_auction_quantity")
	private Integer rejectAuctionQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reject_reason_id")
	private RejectReason rejectReason;

	@Column(name = "reject_note")
	private String rejectNote;
	
	@Column(name = "specification")
	private String specification;

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

	public AuctionAccessoriesMapper() {
		super();
	}

	public AuctionAccessoriesMapper(Integer id, Batch batch, AuctionRequisitionDetail auctionRequisitionDetail) {
		super();
		this.id = id;
		this.batch = batch;
		this.auctionRequisitionDetail = auctionRequisitionDetail;
	}

	public AuctionAccessoriesMapper(Batch batch) {
		this.batch = batch;
	}

	public AuctionAccessoriesMapper(Batch batch, Integer auctionQuantity, String specification) {
		super();
		this.batch = batch;
		this.auctionQuantity = auctionQuantity;
		this.specification = specification;
	}

	public void updateAuctionAccessoriesMapper(Batch batch,  Integer auctionQuantity, String specification) {
		this.batch = batch;
	}

	public Integer getAuctionQuantity() {
		return auctionQuantity;
	}

	public void setAuctionQuantity(Integer auctionQuantity) {
		this.auctionQuantity = auctionQuantity;
	}

	public Integer getApprovedAuctionQuantity() {
		return approvedAuctionQuantity;
	}

	public void setApprovedAuctionQuantity(Integer approvedAuctionQuantity) {
		this.approvedAuctionQuantity = approvedAuctionQuantity;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public void approveAuctionAccessoriesMapper(Integer approvedAuctionQuantity, Integer rejectAuctionQuantity,
			RejectReason rejectReason, String rejectNote) {
		this.approvedAuctionQuantity = approvedAuctionQuantity;
		this.rejectAuctionQuantity = rejectAuctionQuantity;
		this.rejectReason = rejectReason;
		this.rejectNote = rejectNote;
	}

}
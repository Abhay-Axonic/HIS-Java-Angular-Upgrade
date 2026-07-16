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
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;

@Entity(name = "AuctionRequisitionDetail")
@Table(name = "t_auction_requisition_detail", schema = "assets")
public class AuctionRequisitionDetail extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auction_requisition_id")
	private AuctionRequisition auctionRequisition;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id")
	private Batch batch;

	@Column(name = "auction_quantity")
	private Integer auctionQuantity;

	@Column(name = "approved_quantity")
	private Integer approvedQuantity;

	@Column(name = "reject_quantity")
	private Integer rejectQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reject_reason_id")
	private RejectReason rejectReason;

	@Column(name = "reject_note")
	private String rejectNote;

	@Column(name = "remark")
	private String remark;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = true)
	private Status status;

	@Column(name = "auction_price")
	private Double auctionPrice;

	@Column(name = "total_auction_price")
	private Double totalAuctionPrice;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "auctionRequisitionDetail", cascade = CascadeType.ALL)
	private List<AuctionAccessoriesMapper> auctionAccessoriesMapper;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "auctionRequisitionDetail", cascade = CascadeType.ALL)
	private List<ItemAuctionDetail> itemAuctionDetails;

	public AuctionRequisitionDetail() {
		super();
	}

	public AuctionRequisitionDetail(Integer id) {
		super();
		this.id = id;
	}

	public AuctionRequisitionDetail(Batch batch, String remark, Integer auctionQty, Status status) {
		this.batch = batch;
		this.remark = remark;
		this.auctionQuantity = auctionQty;
		this.status = status;
	}

	public AuctionRequisitionDetail(Integer id, Integer auctionQuantity, Double auctionPrice, Double totalAuctionPrice,
			Integer rejectQuantity, RejectReason rejectReason, Status status) {
		this.id = id;
		this.auctionQuantity = auctionQuantity;
		this.auctionPrice = auctionPrice;
		this.totalAuctionPrice = totalAuctionPrice;
		this.rejectQuantity = rejectQuantity;
		this.rejectReason = rejectReason;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AuctionRequisition getAuctionRequisition() {
		return auctionRequisition;
	}

	public void setAuctionRequisition(AuctionRequisition auctionRequisition) {
		this.auctionRequisition = auctionRequisition;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public Integer getAuctionQuantity() {
		return auctionQuantity;
	}

	public void setAuctionQuantity(Integer auctionQuantity) {
		this.auctionQuantity = auctionQuantity;
	}

	public Integer getApprovedQuantity() {
		return approvedQuantity;
	}

	public void setApprovedQuantity(Integer approvedQuantity) {
		this.approvedQuantity = approvedQuantity;
	}

	public RejectReason getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(RejectReason rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<AuctionAccessoriesMapper> getAuctionAccessoriesMapper() {
		return auctionAccessoriesMapper;
	}

	public void setAuctionAccessoriesMapper(List<AuctionAccessoriesMapper> auctionAccessoriesMapper) {
		this.auctionAccessoriesMapper = auctionAccessoriesMapper;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public AuctionAccessoriesMapper addAuctionAccessoriesMapper(AuctionAccessoriesMapper auctionAccessoriesMapper) {
		if (this.auctionAccessoriesMapper == null)
			this.auctionAccessoriesMapper = new ArrayList<AuctionAccessoriesMapper>();

		this.auctionAccessoriesMapper.add(auctionAccessoriesMapper);
		auctionAccessoriesMapper.setAuctionRequisitionDetail(this);

		return auctionAccessoriesMapper;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Double getAuctionPrice() {
		return auctionPrice;
	}

	public void setAuctionPrice(Double auctionPrice) {
		this.auctionPrice = auctionPrice;
	}

	public Double getTotalAuctionPrice() {
		return totalAuctionPrice;
	}

	public void setTotalAuctionPrice(Double totalAuctionPrice) {
		this.totalAuctionPrice = totalAuctionPrice;
	}

	public void approveAuctionRequisition(Integer approvedQuantity, Double auctionPrice, Double totalAuctionPrice,
			Integer rejectQuantity, RejectReason rejectReason, Status status, String rejectNote) {
		this.approvedQuantity = approvedQuantity;
		this.auctionPrice = auctionPrice;
		this.totalAuctionPrice = totalAuctionPrice;
		this.rejectQuantity = rejectQuantity;
		this.rejectReason = rejectReason;
		this.status = status;
		this.rejectNote = rejectNote;
	}

	public List<ItemAuctionDetail> getItemAuctionDetails() {
		return itemAuctionDetails;
	}

	public void setItemAuctionDetails(List<ItemAuctionDetail> itemAuctionDetails) {
		this.itemAuctionDetails = itemAuctionDetails;
	}

	public void updateAuctionRequisitionDetail(Batch batch, Integer auctionQuantity, String remark) {
		this.batch = batch;
		this.auctionQuantity = auctionQuantity;
		this.remark = remark;
	}

}

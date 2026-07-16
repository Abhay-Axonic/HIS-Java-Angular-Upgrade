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
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.procurement.PurchaseRequestDetail;

@Entity(name = "AssetIndentDetail")
@Table(name = "t_asset_indent_detail", schema = "assets")
public class AssetIndentDetail extends BaseEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "indent_id", nullable = false)
	private AssetIndent indent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	@Column(name = "indent_quantity")
	private Integer indentQuantity;

	@Column(name = "pending_quantity")
	private Integer pendingQuantity;

	@Column(name = "approved_indent_quantity")
	private Integer approvedIndentQuantity;

	@Column(name = "reject_indent_quantity")
	private Integer rejectIndentQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

	@Column(name = "specification")
	private String specification;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reject_reason_id")
	private RejectReason rejectReason;

	@Column(name = "reject_note")
	private String rejectNote;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "indentDetail", cascade = CascadeType.ALL)
	private List<AssetIndentAccessories> assetIndentAccessories;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "purchase_request_detail_id")
	private PurchaseRequestDetail purchaseRequestDetail;

	public PurchaseRequestDetail getPurchaseRequestDetail() {
		return purchaseRequestDetail;
	}

	public void setPurchaseRequestDetail(PurchaseRequestDetail purchaseRequestDetail) {
		this.purchaseRequestDetail = purchaseRequestDetail;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AssetIndent getIndent() {
		return indent;
	}

	public void setIndent(AssetIndent indent) {
		this.indent = indent;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Integer getIndentQuantity() {
		return indentQuantity;
	}

	public void setIndentQuantity(Integer indentQuantity) {
		this.indentQuantity = indentQuantity;
	}

	public Integer getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	public Integer getApprovedIndentQuantity() {
		return approvedIndentQuantity;
	}

	public void setApprovedIndentQuantity(Integer approvedIndentQuantity) {
		this.approvedIndentQuantity = approvedIndentQuantity;
	}

	public Integer getRejectIndentQuantity() {
		return rejectIndentQuantity;
	}

	public void setRejectIndentQuantity(Integer rejectIndentQuantity) {
		this.rejectIndentQuantity = rejectIndentQuantity;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
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

	public List<AssetIndentAccessories> getAssetIndentAccessories() {
		return assetIndentAccessories;
	}

	public void setAssetIndentAccessories(List<AssetIndentAccessories> assetIndentAccessories) {
		this.assetIndentAccessories = assetIndentAccessories;
	}

	public AssetIndentAccessories addAssetIndentAccessories(AssetIndentAccessories assetIndentAccessories) {
		if (getAssetIndentAccessories() == null)
			this.assetIndentAccessories = new ArrayList<AssetIndentAccessories>();

		getAssetIndentAccessories().add(assetIndentAccessories);
		assetIndentAccessories.setIndentDetail(this);

		return assetIndentAccessories;
	}

	public AssetIndentDetail() {
		super();
	}

	public AssetIndentDetail(Integer id) {
		super();
		this.id = id;
	}

	public AssetIndentDetail(Integer id, AssetIndent indent, Item item, Integer indentQuantity,
			Integer approvedIndentQuantity, Integer rejectIndentQuantity, Status status, String specification,
			RejectReason rejectReason, String rejectNote) {
		super();
		this.id = id;
		this.indent = indent;
		this.item = item;
		this.indentQuantity = indentQuantity;
		this.approvedIndentQuantity = approvedIndentQuantity;
		this.rejectIndentQuantity = rejectIndentQuantity;
		this.status = status;
		this.specification = specification;
		this.rejectReason = rejectReason;
		this.rejectNote = rejectNote;
	}

	public AssetIndentDetail(Item item, Integer indentQuantity, Status status, String specification) {
		super();
		this.item = item;
		this.indentQuantity = indentQuantity;
		this.status = status;
		this.specification = specification;
	}

	public void updateAssetIndentDetail(Item item, Integer indentQuantity, Status status, String specification) {
		this.item = item;
		this.indentQuantity = indentQuantity;
		this.status = status;
		this.specification = specification;
	}

	public void updateAssetIndentDetail(Integer approvedIndentQuantity, Integer rejectIndentQuantity,
			RejectReason rejectReason, String rejectNote, Status status) {
		this.approvedIndentQuantity = approvedIndentQuantity;
		this.rejectIndentQuantity = rejectIndentQuantity;
		this.rejectReason = rejectReason;
		this.rejectNote = rejectNote;
		this.status = status;
	}

}
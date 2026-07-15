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
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RejectReason;

@Entity(name = "AssetIndentAccessories")
@Table(name = "t_asset_indent_accessories", schema = "assets")
public class AssetIndentAccessories extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "indent_detail_id", nullable = false)
	private AssetIndentDetail indentDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	@Column(name = "indent_quantity")
	private Integer indentQuantity;

	@Column(name = "approved_indent_quantity")
	private Integer approvedIndentQuantity;

	@Column(name = "reject_indent_quantity")
	private Integer rejectIndentQuantity;

	@Column(name = "specification")
	private String specification;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reject_reason_id")
	private RejectReason rejectReason;

	@Column(name = "reject_note")
	private String rejectNote;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AssetIndentDetail getIndentDetail() {
		return indentDetail;
	}

	public void setIndentDetail(AssetIndentDetail indentDetail) {
		this.indentDetail = indentDetail;
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

	public AssetIndentAccessories() {
		super();
	}

	public AssetIndentAccessories(Integer id, AssetIndentDetail indentDetail, Item item, Integer indentQuantity,
			Integer approvedIndentQuantity, Integer rejectIndentQuantity, String specification,
			RejectReason rejectReason, String rejectNote) {
		super();
		this.id = id;
		this.indentDetail = indentDetail;
		this.item = item;
		this.indentQuantity = indentQuantity;
		this.approvedIndentQuantity = approvedIndentQuantity;
		this.rejectIndentQuantity = rejectIndentQuantity;
		this.specification = specification;
		this.rejectReason = rejectReason;
		this.rejectNote = rejectNote;
	}

	public AssetIndentAccessories(Item item, Integer indentQuantity, String specification) {
		super();
		this.item = item;
		this.indentQuantity = indentQuantity;
		this.specification = specification;
	}

	public void updateAssetIndentAccessories(Item item, Integer indentQuantity, String specification) {
		this.item = item;
		this.indentQuantity = indentQuantity;
		this.specification = specification;
	}

	public void updateAssetIndentAccessories(Integer approvedIndentQuantity, Integer rejectIndentQuantity,
			RejectReason rejectReason, String rejectNote) {
		this.approvedIndentQuantity = approvedIndentQuantity;
		this.rejectIndentQuantity = rejectIndentQuantity;
		this.rejectReason = rejectReason;
		this.rejectNote = rejectNote;
	}

}
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

@Entity(name = "AssetIssueDetail")
@Table(name = "t_asset_issue_detail", schema = "assets")
public class AssetIssueDetail extends BaseEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_indent_detail_id", nullable = true)
	private AssetIndentDetail assetIndentDetail;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "asset_issue_id", nullable = true)
	private AssetIssue assetIssue;

	@Column(name = "issue_quantity")
	private Integer issueQuantity = 0;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "assetIssueDetail", cascade = CascadeType.ALL)
	private List<AssetIssueBatchMapper> assetIssueBatchMappers;

	public AssetIssueDetail() {
		super();
	}

	public AssetIssueDetail(Integer id) {
		super();
		this.id = id;
	}

	public AssetIssueDetail(AssetIndentDetail assetIndentDetail, Item item) {
		super();
		this.assetIndentDetail = assetIndentDetail;
		this.item = item;
	}

	public AssetIssueDetail(Integer issueQuantity, Item item) {
		super();
		this.issueQuantity = issueQuantity;
		this.item = item;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AssetIndentDetail getAssetIndentDetail() {
		return assetIndentDetail;
	}

	public void setAssetIndentDetail(AssetIndentDetail assetIndentDetail) {
		this.assetIndentDetail = assetIndentDetail;
	}

	public AssetIssue getAssetIssue() {
		return assetIssue;
	}

	public void setAssetIssue(AssetIssue assetIssue) {
		this.assetIssue = assetIssue;
	}

	public Integer getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public void addBatch(AssetIssueBatchMapper batchMapper) {
		if (this.assetIssueBatchMappers == null)
			this.assetIssueBatchMappers = new ArrayList<AssetIssueBatchMapper>();

		this.assetIssueBatchMappers.add(batchMapper);
		batchMapper.setAssetIssueDetail(this);
	}

	public List<AssetIssueBatchMapper> getAssetIssueBatchMappers() {
		return this.assetIssueBatchMappers;
	}

	public void setAssetIssueBatchMappers(List<AssetIssueBatchMapper> assetIssueBatchMappers) {
		this.assetIssueBatchMappers = assetIssueBatchMappers;
	}

	public AssetIssueBatchMapper addAssetIssueBatchMapper(AssetIssueBatchMapper assetIssueBatchMapper) {
		getAssetIssueBatchMappers().add(assetIssueBatchMapper);
		assetIssueBatchMapper.setAssetIssueDetail(this);

		return assetIssueBatchMapper;
	}

	public AssetIssueBatchMapper removeAssetIssueBatchMapper(AssetIssueBatchMapper assetIssueBatchMapper) {
		getAssetIssueBatchMappers().remove(assetIssueBatchMapper);
		assetIssueBatchMapper.setAssetIssueDetail(null);

		return assetIssueBatchMapper;
	}

}

package com.param.entity.model.linenAndLaundry;

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
import com.param.entity.model.inventory.MaterialIssueBatchMapper;
import com.param.entity.model.master.Item;

@Entity(name = "LinenIssueDetail")
@Table(name = "t_linen_issue_detail", schema = "linen_and_laundry")
public class LinenIssueDetail extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "linen_issue_id", nullable = false)
	private LinenIssue linenIssue;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "linen_indent_detail_id")
	private LinenIndentDetail linenIndentDetail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;
	
	@Column(name = "issue_quantity")
	private Integer issueQuantity;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "linenIssueDetail", cascade = CascadeType.ALL)
	private List<LinenIssueBatchMapper> linenIssueBatchMapper;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LinenIssue getLinenIssue() {
		return linenIssue;
	}

	public void setLinenIssue(LinenIssue linenIssue) {
		this.linenIssue = linenIssue;
	}

	public LinenIndentDetail getLinenIndentDetail() {
		return linenIndentDetail;
	}

	public void setLinenIndentDetail(LinenIndentDetail linenIndentDetail) {
		this.linenIndentDetail = linenIndentDetail;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Integer getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
	}

	public List<LinenIssueBatchMapper> getLinenIssueBatchMapper() {
		return linenIssueBatchMapper;
	}

	public void setLinenIssueBatchMapper(List<LinenIssueBatchMapper> linenIssueBatchMapper) {
		this.linenIssueBatchMapper = linenIssueBatchMapper;
	}

	public void addBatch(LinenIssueBatchMapper batchMapper) {
		if (this.linenIssueBatchMapper == null)
			this.linenIssueBatchMapper = new ArrayList<LinenIssueBatchMapper>();

		this.linenIssueBatchMapper.add(batchMapper);
		batchMapper.setLinenIssueDetail(this);
	}
	
	public LinenIssueDetail() {
		super();
	}

	public LinenIssueDetail(Item item) {
		super();
		this.item = item;
	}

	public LinenIssueDetail(LinenIndentDetail linenIndentDetail, Item item) {
		super();
		this.linenIndentDetail = linenIndentDetail;
		this.item = item;
	}
	
}
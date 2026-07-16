package com.param.entity.model.linenAndLaundry;

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
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;

@Entity(name = "LinenIndentDetail")
@Table(name = "t_linen_indent_detail", schema = "linen_and_laundry")
public class LinenIndentDetail extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "linen_indent_id", nullable = false)
	private LinenIndent linenIndent;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;
	
	@Column(name = "indent_quantity")
	private Integer indentQuantity;
	
	@Column(name = "approved_indent_quantity")
	private Integer approvedIndentQuantity;
	
	@Column(name = "reject_indent_quantity")
	private Integer rejectIndentQuantity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = true)
	private Status status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reject_reason_id")
	private RejectReason rejectReason;
	
	@Column(name = "specification")
	private String specification;
	
	@Column(name = "reject_note")
	private String rejectNote;
	
	@Column(name = "pending_quantity")
	private Integer pendingQuantity;

	public LinenIndentDetail() {
		super();
	}

	public LinenIndentDetail(Integer id) {
		super();
		this.id = id;
	}

	public void updateLinenIndentDetail(Integer approvedIndentQuantity, Integer rejectIndentQuantity, Status status,
			RejectReason rejectReason, String specification, String rejectNote) {
		this.approvedIndentQuantity = approvedIndentQuantity;
		this.rejectIndentQuantity = rejectIndentQuantity;
		this.status = status;
		this.rejectReason = rejectReason;
		this.specification = specification;
		this.rejectNote = rejectNote;
	}

	public LinenIndentDetail(Item item, Integer indentQuantity, Status status, String specification, Integer pendingQuantity) {
		super();
		this.item = item;
		this.indentQuantity = indentQuantity;
		this.status = status;
		this.specification = specification;
		this.pendingQuantity = pendingQuantity;
	}

	public void updateLinenIndentDetail(Item item, Integer indentQuantity, String specification) {
		this.item = item;
		this.indentQuantity = indentQuantity;
		this.specification = specification;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LinenIndent getLinenIndent() {
		return linenIndent;
	}

	public void setLinenIndent(LinenIndent linenIndent) {
		this.linenIndent = linenIndent;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public RejectReason getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(RejectReason rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public Integer getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}
	
}

package com.param.entity.model.procurement;

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
import com.param.entity.model.master.Status;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;

@Entity(name = "ImplantIndentDetail")
@Table(name = "t_implant_indent_detail", schema = "theatre_management")
public class ImplantIndentDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "indent_quantity")
	private Integer indentQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@Column(name = "pending_quantity")
	private Integer pendingQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = false)
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_type_id")
	private UomType uomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_unit_id")
	private UomUnit uomUnit;

	// bi-directional many-to-one association to ImplantIndent
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "implant_indent_id")
	private ImplantIndent implantIndent;

	public ImplantIndentDetail() {
		super();
	}

	public ImplantIndentDetail(Integer id) {
		this.id = id;
	}

	public ImplantIndentDetail(Integer indentQuantity, Item item, Integer pendingQuantity, Status status,
			UomType uomType, UomUnit uomUnit) {
		super();
		this.indentQuantity = indentQuantity;
		this.item = item;
		this.pendingQuantity = pendingQuantity;
		this.status = status;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIndentQuantity() {
		return indentQuantity;
	}

	public void setIndentQuantity(Integer indentQuantity) {
		this.indentQuantity = indentQuantity;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Integer getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public UomType getUomType() {
		return uomType;
	}

	public void setUomType(UomType uomType) {
		this.uomType = uomType;
	}

	public UomUnit getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(UomUnit uomUnit) {
		this.uomUnit = uomUnit;
	}

	public ImplantIndent getImplantIndent() {
		return implantIndent;
	}

	public void setImplantIndent(ImplantIndent implantIndent) {
		this.implantIndent = implantIndent;
	}

	public void updateIndentDetail(Integer indentQuantity, Item item) {
		this.indentQuantity = indentQuantity;
		this.item = item;
	}

}

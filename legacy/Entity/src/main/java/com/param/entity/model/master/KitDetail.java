package com.param.entity.model.master;

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

@Entity(name = "KitDetail")
@Table(name = "m_kit_detail", schema = "public")
public class KitDetail extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_type_id")
	private UomType uomType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_unit_id")
	private UomUnit uomUnit;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kit_id")
	private Kit kitDetail;
	
	public KitDetail()
	{
		super();
	}

	public KitDetail(Item item) {
		this.item=item;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Kit getKitDetail() {
		return kitDetail;
	}

	public void setKitDetail(Kit kitDetail) {
		this.kitDetail = kitDetail;
	}
	
	public UomType getUomType() {
		return uomType;
	}

	public void setUomType(UomType uomType) {
		this.uomType = uomType;
	}
	
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public KitDetail(Item item,UomUnit uomUnitId,UomType uomTypeId,Integer quantity) {
		this.item=item;
		this.quantity=quantity;
		this.uomUnit=uomUnitId;
		this.uomType=uomTypeId;
	}
	public KitDetail(Item item,Integer quantity) {
		this.item=item;
		this.quantity=quantity;
	}
	public KitDetail(UomUnit uomUnitId,UomType uomTypeId)
	{
		this.uomUnit=uomUnitId;
		this.uomType=uomTypeId;
	}
	
	
	public KitDetail(Item item,UomUnit uomUnitId,UomType uomTypeId) {
		this.item=item;
		this.uomUnit=uomUnitId;
		this.uomType=uomTypeId;
	}

	public KitDetail(Integer id,Item item, UomUnit uomUnitId, UomType uomTypeId) {
		this.id=id;
		this.item=item;
		this.uomUnit=uomUnitId;
		this.uomType=uomTypeId;
	}

	

	public KitDetail(Integer id) {
		this.id=id;
	}

	public UomUnit getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(UomUnit uomUnit) {
		this.uomUnit = uomUnit;
	}

	

}

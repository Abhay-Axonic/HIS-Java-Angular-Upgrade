package com.param.entity.model.master;

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

@Entity(name = "UnitOfMeasurement")
@Table(name = "m_unit_of_measurement", schema = "public")
public class UnitOfMeasurement extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private Item item;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_type_id", nullable = false)
	private UomType uomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_unit_id", nullable = false)
	private UomUnit uomUnit;

	@Column(nullable = false)
	private Integer conversions;

	@Column(name = "ip_uom")
	private Boolean ipUom;

	@Column(name = "op_uom")
	private Boolean opUom;

	@Column(name = "store_uom")
	private Boolean storeUom;
	
	public UnitOfMeasurement() {
		super();
	}
	
	public UnitOfMeasurement(Item item, UomType uomType, UomUnit uomUnit, Boolean ipUom, Boolean opUom,
			Integer conversion, Boolean storeUom, Boolean status) {
		super();
		this.item = item;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.conversions = conversion;
		this.storeUom = storeUom;
		this.isActive = status;
	}
	
	public void updateunitOfMeasurement(Integer id, UomType uomType, UomUnit uomUnit, Boolean ipUom, Boolean opUom,
			Integer conversion, Boolean storeUom, Boolean status) {
	
		this.id=id;
		
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.conversions = conversion;
		this.storeUom = storeUom;
		this.isActive = status;
	}

	public void updateUOM(UomType uomType, UomUnit uomUnit, Integer conversions, Boolean ipUom, Boolean opUom,
			Boolean storeUom) {
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.conversions = conversions;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
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

	public Integer getConversions() {
		return conversions;
	}

	public void setConversions(Integer conversions) {
		this.conversions = conversions;
	}

	public Boolean getIpUom() {
		return ipUom;
	}

	public void setIpUom(Boolean ipUom) {
		this.ipUom = ipUom;
	}

	public Boolean getOpUom() {
		return opUom;
	}

	public void setOpUom(Boolean opUom) {
		this.opUom = opUom;
	}

	public Boolean getStoreUom() {
		return storeUom;
	}

	public void setStoreUom(Boolean storeUom) {
		this.storeUom = storeUom;
	}
}

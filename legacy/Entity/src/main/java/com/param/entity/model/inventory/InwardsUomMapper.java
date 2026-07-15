package com.param.entity.model.inventory;

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
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;

@Entity(name = "InwardsUomMapper")
@Table(name = "t_inwards_uom_mapper", schema = "inventory")
public class InwardsUomMapper extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "conversion")
	private Integer conversion;

	@Column(name = "ip_uom")
	private Boolean ipUom;

	@Column(name = "op_uom")
	private Boolean opUom;

	@Column(name = "store_uom")
	private Boolean storeUom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_type_id")
	private UomType uomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_unit_id")
	private UomUnit uomUnit;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "material_inwards_detail_id")
	private MaterialInwardDetail materialInwardDetail;
	/*
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name="returnable_gatepass_detail_id") private
	 * ReturnableGatepassDetail returnableGatepassDetail;
	 */

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getConversion() {
		return conversion;
	}

	public void setConversion(Integer conversion) {
		this.conversion = conversion;
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

	public MaterialInwardDetail getMaterialInwardDetail() {
		return materialInwardDetail;
	}

	public void setMaterialInwardDetail(MaterialInwardDetail materialInwardDetail) {
		this.materialInwardDetail = materialInwardDetail;
	}

	public InwardsUomMapper(Integer id, Integer conversion, Boolean ipUom, Boolean opUom, Boolean storeUom,
			UomType uomType, UomUnit uomUnit) {
		super();
		this.id = id;
		this.conversion = conversion;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
	}

	public InwardsUomMapper() {
		super();
	}

	public InwardsUomMapper(UomType uomType, UomUnit uomUnit, Integer conversion, Boolean ipUom, Boolean opUom,
			Boolean storeUom) {
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.conversion = conversion;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
	}

	@Override
	public String toString() {
		return "InwardsUomMapper [id=" + id + ", conversion=" + conversion + ", ipUom=" + ipUom + ", opUom=" + opUom
				+ ", storeUom=" + storeUom + ", uomType=" + uomType + ", uomUnit=" + uomUnit + ", materialInwardDetail="
				+ materialInwardDetail + "]";
	}

}

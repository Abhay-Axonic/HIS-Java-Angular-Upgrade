package com.param.entity.model.procurement;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.entity.model.base.UomEntity;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;

@Entity(name = "ConsignmentInwardUomMapper")
@Table(name = "t_consignment_inward_uom_mapper", schema = "procurement")
public class ConsignmentInwardUomMapper extends UomEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	// bi-directional many-to-one association to ConsignmentInwardDetail
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inward_detail_id")
	private ConsignmentInwardDetail consignmentInwardDetail;
	

	public ConsignmentInwardUomMapper() {
		super();
	}

	public ConsignmentInwardUomMapper(Integer conversions, Boolean ipUom, Boolean opUom, Boolean storeUom,
			UomType uomType, UomUnit uomUnit) {
		super();
		this.conversions = conversions;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
	}

	public void updateConsignmentInwardUomMapper(Integer conversions, Boolean ipUom, Boolean opUom, Boolean storeUom,
			UomType uomType, UomUnit uomUnit) {
		this.conversions = conversions;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public ConsignmentInwardDetail getConsignmentInwardDetail() {
		return consignmentInwardDetail;
	}

	public void setConsignmentInwardDetail(ConsignmentInwardDetail consignmentInwardDetail) {
		this.consignmentInwardDetail = consignmentInwardDetail;
	}

	}

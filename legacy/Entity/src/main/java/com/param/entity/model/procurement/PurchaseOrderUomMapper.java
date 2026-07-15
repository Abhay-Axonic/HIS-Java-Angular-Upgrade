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
import com.param.entity.model.base.UomEntity;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;


@Entity(name = "PurchaseOrderUomMapper")
@Table(name = "t_purchase_order_uom_mapper", schema = "procurement")
public class PurchaseOrderUomMapper extends UomEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "purchase_order_detail_id")
	private PurchaseOrderDetail purchaseOrderDetail;
	
	public PurchaseOrderUomMapper() {
		super();
	}
	
	public PurchaseOrderUomMapper(UomType uomType, UomUnit uomUnit, Integer conversions, Boolean ipUom,
			Boolean opUom, Boolean storeUom) {
		super(conversions, ipUom, opUom, storeUom, uomType, uomUnit);
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.conversions = conversions;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
	}

	public void updatePurchaseOrderUomMapper(UomType uomType, UomUnit uomUnit, Integer conversions, Boolean ipUom,
			Boolean opUom, Boolean storeUom) {
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.conversions = conversions;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
	}
	
	public PurchaseOrderDetail getPurchaseOrderDetail() {
		return purchaseOrderDetail;
	}

	public void setPurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail) {
		this.purchaseOrderDetail = purchaseOrderDetail;
	}

}

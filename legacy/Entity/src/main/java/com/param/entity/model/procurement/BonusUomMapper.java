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

@Entity(name = "BonusUomMapper")
@Table(name = "t_bonus_uom_mapper", schema = "procurement")
public class BonusUomMapper extends UomEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "purchase_order_detail_id")
	private PurchaseOrderDetail purchaseOrderDetail;

	public BonusUomMapper() {
		super();
	}

	public BonusUomMapper(UomType uomType, UomUnit uomUnit, Integer conversions, Boolean ipUom, Boolean opUom,
			Boolean storeUom) {
		super(conversions, ipUom, opUom, storeUom, uomType, uomUnit);
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.conversions = conversions;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
	}

	public void updateBonusUomMapper(UomType uomType, UomUnit uomUnit, Integer conversions, Boolean ipUom,
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

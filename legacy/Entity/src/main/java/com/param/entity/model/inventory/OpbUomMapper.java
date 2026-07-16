package com.param.entity.model.inventory;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.entity.model.base.UomEntity;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;

@Entity(name = "OpbUomMapper")
@Table(name = "t_opb_uom_mapper", schema = "inventory")
public class OpbUomMapper extends UomEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "opb_detail_id", nullable = false)
	private OpeningBalanceDetail openingBalanceDetail;

	public OpbUomMapper() {
		super();
	}

	public OpbUomMapper(Integer conversions, Boolean ipUom, Boolean opUom, Boolean storeUom, UomType uomType,
			UomUnit uomUnit) {
		super(conversions, ipUom, opUom, storeUom, uomType, uomUnit);
	}

	public OpeningBalanceDetail getOpeningBalanceDetail() {
		return this.openingBalanceDetail;
	}

	public void setOpeningBalanceDetail(OpeningBalanceDetail openingBalanceDetail) {
		this.openingBalanceDetail = openingBalanceDetail;
	}
	
	public void UpdateOpbUomMapper(Integer conversions, Boolean ipUom, Boolean opUom, Boolean storeUom, UomType uomType,
			UomUnit uomUnit, Date updatedDate, User updatedUser) {
		this.conversions = conversions;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.updatedDate = updatedDate;
		this.updatedBy = updatedUser;
	}
}
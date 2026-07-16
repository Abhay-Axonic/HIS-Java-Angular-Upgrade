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

@Entity(name = "LoanInUomMapper")
@Table(name = "t_loan_in_uom_mapper", schema = "procurement")
public class LoanInUomMapper extends UomEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loan_in_detail_id")
	private LoanInDetail loanInDetail;

	public LoanInDetail getLoanInDetail() {
		return loanInDetail;
	}

	public void setLoanInDetail(LoanInDetail loanInDetail) {
		this.loanInDetail = loanInDetail;
	}

	public LoanInUomMapper() {
		super();
	}

	public LoanInUomMapper(Integer id, Integer conversions, Boolean ipUom, LoanInDetail loanInDetail, Boolean opUom,
			Boolean storeUom, UomType uomType, UomUnit uomUnit) {
		super();
		this.id = id;
		this.conversions = conversions;
		this.ipUom = ipUom;
		this.loanInDetail = loanInDetail;
		this.opUom = opUom;
		this.storeUom = storeUom;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
	}

	public LoanInUomMapper(Integer conversions, Boolean ipUom, Boolean opUom, Boolean storeUom, UomType uomType, UomUnit uomUnit) {
		super();
		this.conversions = conversions;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
	}

	public void updateLoanInUomMapper(Integer conversions, Boolean ipUom, Boolean opUom, Boolean storeUom, UomType uomType, UomUnit uomUnit) {
		this.conversions = conversions;
		this.ipUom = ipUom;
		this.opUom = opUom;
		this.storeUom = storeUom;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
	}
}
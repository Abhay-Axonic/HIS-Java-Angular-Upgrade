package com.param.pharmacy.master.global.dao.otherCharges;

import java.util.List;

import com.param.entity.model.master.OtherCharge;

public interface IOtherChargesDao {

	List<OtherCharge> getAll(Integer orgId);

	OtherCharge getById(Integer id);

	Integer create(OtherCharge otherCharge);

	Integer update(OtherCharge otherCharge);

}

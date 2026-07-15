package com.param.pharmacy.master.global.dao.uomUnit;

import java.util.List;

import com.param.entity.model.master.UomUnit;

public interface IUomUnitDao {

	List<UomUnit> getAll(Integer orgId);

	UomUnit getById(Integer id);

	Integer create(UomUnit uomUnit);

	Integer update(UomUnit uomUnit);

	Long getUnitOfMesurementCountFromUnit(Integer id, Integer orgId);

}

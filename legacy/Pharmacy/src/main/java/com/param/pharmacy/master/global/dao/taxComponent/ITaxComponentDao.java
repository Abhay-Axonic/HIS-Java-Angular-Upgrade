package com.param.pharmacy.master.global.dao.taxComponent;

import java.util.List;

import com.param.entity.model.master.TaxComponent;

public interface ITaxComponentDao {

	List<TaxComponent> getAll(Integer orgId);

	TaxComponent getById(Integer id, Integer orgId);

	Integer create(TaxComponent taxComponent);

	Integer update(TaxComponent taxComponent);

	List<TaxComponent> getTaxComponentList();

	Long getTaxComponentFromTax(Integer id,Integer orgId);

}

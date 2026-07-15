package com.param.pharmacy.master.global.dao.tax;

import java.util.List;

import com.param.entity.model.master.Tax;
import com.param.entity.model.master.TaxComponent;
import com.param.entity.model.master.TaxDetail;

public interface ITaxDao {

	List<Tax> getAll(Integer orgId);

	TaxComponent getTaxComponent(Integer id);

	Tax saveTax(Tax tax);

	Tax getTax(Integer id);

	List<TaxDetail> getTaxDetail(Integer id);

	List<Tax> getTaxList(Integer orgId);

	Tax getById(Integer id);

	Tax updateTax(Tax tax);

	Integer update(Tax tax);

	Long getTaxFromItem(Integer id);

	Long getTaxFromTaxComponent(Integer id);
	
}

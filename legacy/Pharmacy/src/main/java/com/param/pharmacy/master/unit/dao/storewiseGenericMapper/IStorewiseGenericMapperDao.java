package com.param.pharmacy.master.unit.dao.storewiseGenericMapper;

import java.util.List;

import com.param.entity.model.master.Generic;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.StorewiseGenericMapper;
import com.param.entity.model.master.Ved;

public interface IStorewiseGenericMapperDao {

	List<StorewiseGenericMapper> getAll(Integer unitId);

	/**
	 * BR-121: id-only lookup with no ownership check at all previously --
	 * any authenticated user could read/write any other unit's row by id.
	 * unitId here is always the JWT-verified value, never client-supplied.
	 */
	StorewiseGenericMapper getById(Integer id, Integer unitId);

	Integer create(StorewiseGenericMapper storewiseGenericMapper);

	Integer update(StorewiseGenericMapper storewiseGenericMapper);

	Store getStore(Integer storeId);

	Generic getGeneric(Integer genericId);

	Ved getVed(Integer vedId);

	List<Ved> getVedList();

	List<Generic> getGenericList(String genericCode, Integer unitId, Integer orgId);

	Long getStorewiseGenericFromGeneric(Integer id);
}

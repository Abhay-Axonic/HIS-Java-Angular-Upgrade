package com.param.pharmacy.master.unit.dao.storewiseGenericMapper;

import java.util.List;

import com.param.entity.model.master.Generic;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.StorewiseGenericMapper;
import com.param.entity.model.master.Ved;

public interface IStorewiseGenericMapperDao {

	List<StorewiseGenericMapper> getAll(Integer unitId);

	StorewiseGenericMapper getById(Integer id);

	Integer create(StorewiseGenericMapper storewiseGenericMapper);

	Integer update(StorewiseGenericMapper storewiseGenericMapper);

	Store getStore(Integer storeId);

	Generic getGeneric(Integer genericId);

	Ved getVed(Integer vedId);

	List<Ved> getVedList();

	List<Generic> getGenericList(String genericCode, Integer unitId, Integer orgId);

	Long getStorewiseGenericFromGeneric(Integer id);
}

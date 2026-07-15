package com.param.pharmacy.master.global.dao.generic;

import java.util.List;

import com.param.entity.model.master.CombinationGeneric;
import com.param.entity.model.master.Generic;
import com.param.pharmacy.master.global.dto.generic.GenericListResponse;

public interface IGenericDao {

	List<Generic> getAll();
	
	Generic getById(Integer id, Integer orgId);
	
	List<CombinationGeneric> getCombinationDetailsById(Integer id);
	
	Integer create(Generic generic);
	
	Integer update(Generic generic);

	Long getGenericCountFromItem(Integer productCategoryId);

	Long getGenericCountFromStoreWiseMapper(Integer id);

	Long getGenericCountFromProductCategory(Integer id);

	List<GenericListResponse> getAllGenericMasters(Integer orgId);
	
	List<GenericListResponse> getAllIsCombinationGeneric(Boolean isCombination);

}

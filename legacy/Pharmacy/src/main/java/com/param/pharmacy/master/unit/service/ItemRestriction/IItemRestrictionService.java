package com.param.pharmacy.master.unit.service.ItemRestriction;

import java.util.List;

import com.param.pharmacy.master.unit.dto.itemRestriction.ItemRestrictionRequest;
import com.param.pharmacy.master.unit.dto.itemRestriction.ItemRestrictionResponse;

public interface IItemRestrictionService {

	List<ItemRestrictionResponse> getAll();

	Object saveStoreItemRestriction(ItemRestrictionRequest request);

	Object updateStoreItemRestriction(ItemRestrictionRequest request);
}

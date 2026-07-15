package com.param.pharmacy.master.unit.dao.itemRestriction;

import java.math.BigInteger;
import java.util.List;

import com.param.entity.model.master.StoreItemRestriction;

public interface IItemRestrictionDao {

    List<StoreItemRestriction> getAll();
	
	StoreItemRestriction saveItemRestriction(StoreItemRestriction itemRestriction);

	StoreItemRestriction updateStoreItemRestriction(StoreItemRestriction storeItemRestriction);

	StoreItemRestriction getStoreItemRestriction(BigInteger id);

}

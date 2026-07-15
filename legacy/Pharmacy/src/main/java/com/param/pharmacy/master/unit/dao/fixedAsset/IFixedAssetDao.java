package com.param.pharmacy.master.unit.dao.fixedAsset;

import java.util.List;

import com.param.entity.model.master.Item;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.ProductCategory;
import com.param.entity.model.procurement.ItemRequestDetail;

public interface IFixedAssetDao {

	List<Item> getAll();

	Item getById(Integer id);

	Integer create(Item item);

	ProductCategory getProductCategory(Integer productCategoryId);

	Manufacturer getManufacturer(Integer manufactureId);

	Integer update(Item item);
	
	ItemRequestDetail getItemRequestDetail(Integer id);

	Integer updateItemRequestDetail(ItemRequestDetail itemRequestDetail);

	List<Item> getAssetNameListByAssetCategoryId(Integer assetCategoryId);


}

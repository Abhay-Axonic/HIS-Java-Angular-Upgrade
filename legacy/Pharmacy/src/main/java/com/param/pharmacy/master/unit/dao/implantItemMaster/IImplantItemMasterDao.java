package com.param.pharmacy.master.unit.dao.implantItemMaster;

import java.util.List;

import com.param.entity.model.master.Item;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.ProductCategory;
import com.param.entity.model.master.StorageUnit;
import com.param.entity.model.master.Tax;

public interface IImplantItemMasterDao
{
	List<Item> getAll(Integer unitId);
	
	Item getById(Integer id);
	
	Item implantItemMasterSave(Item implantItemMaster);
	
	Item updateItem(Item item);//update
	
	//	Integer update(Item item);//update

	 Item getItem(Integer id);

   ProductCategory getProductCategory(Integer productCategoryId);

   Manufacturer getManufacture(Integer manufactureId);

   StorageUnit getStorageUnit(Integer storageUnitId);

   Tax getTax(Integer getsTaxId);

Long getproductCategoryCount(Integer id);

Long getmanufactoreCount(Integer id);

Long getstorageUnitCount(Integer id);

Long gettaxCount(Integer id);

	
}


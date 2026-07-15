package com.param.pharmacy.master.unit.dto.implantItemMaster;

import java.util.ArrayList;
import java.util.List;

import com.param.global.dto.generic.StorageUnit;
import com.param.global.dto.generic.Tax;
import com.param.global.dto.global.AssetCategory;
import com.param.global.dto.global.AssetType;

public class ImplantItemMasterDropdown {
	
	protected List<StorageUnit> storageUnit;

	protected List<Tax> tax;
	
	public void addStorageUnit(StorageUnit storageUnit) {
		if (this.storageUnit == null) {
			this.storageUnit = new ArrayList<StorageUnit>();
		}
		this.storageUnit.add(storageUnit);
	}
	
	public void addTax(Tax tax) {
		if (this.tax == null) {
			this.tax = new ArrayList<Tax>();
		}
		this.tax.add(tax);
	}

	public List<StorageUnit> getStorageUnit() {
		return storageUnit;
	}

	public void setStorageUnit(List<StorageUnit> storageUnit) {
		this.storageUnit = storageUnit;
	}

	public List<Tax> getTax() {
		return tax;
	}

	public void setTax(List<Tax> tax) {
		this.tax = tax;
	}

}

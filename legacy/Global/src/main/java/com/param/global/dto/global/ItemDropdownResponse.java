package com.param.global.dto.global;

import java.util.ArrayList;
import java.util.List;

import com.param.global.dto.global.AssetType;
import com.param.global.dto.global.FormulationType;
import com.param.global.dto.generic.Manufacturer;
import com.param.global.dto.generic.PharmacologicalClassification;
import com.param.global.dto.generic.StorageUnit;
import com.param.global.dto.generic.StrengthUnit;
import com.param.global.dto.generic.Tax;

public class ItemDropdownResponse {

	protected List<AssetType> assetType;

	protected List<PharmacologicalClassification> pharmacologicalClassification;

	protected List<Manufacturer> manufacturer;

	protected List<StrengthUnit> strengthUnit;

	protected List<StorageUnit> storageUnit;

	protected List<Tax> tax;

	protected List<FormulationType> formulationType;

	public List<AssetType> getAssetType() {
		return assetType;
	}

	public void setAssetType(List<AssetType> assetType) {
		this.assetType = assetType;
	}

	public List<PharmacologicalClassification> getPharmacologicalClassification() {
		return pharmacologicalClassification;
	}

	public void setPharmacologicalClassification(List<PharmacologicalClassification> pharmacologicalClassification) {
		this.pharmacologicalClassification = pharmacologicalClassification;
	}

	public List<Manufacturer> getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(List<Manufacturer> manufacturer) {
		this.manufacturer = manufacturer;
	}

	public List<StrengthUnit> getStrengthUnit() {
		return strengthUnit;
	}

	public void setStrengthUnit(List<StrengthUnit> strengthUnit) {
		this.strengthUnit = strengthUnit;
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

	public List<FormulationType> getFormulationType() {
		return formulationType;
	}

	public void setFormulationType(List<FormulationType> formulationType) {
		this.formulationType = formulationType;
	}

	public void addAssetType(AssetType assetType) {
		if (this.assetType == null)
			this.assetType = new ArrayList<AssetType>();

		this.assetType.add(assetType);
	}

	public void addPharmacologicalClassification(PharmacologicalClassification pharmacologicalClassification) {
		if (this.pharmacologicalClassification == null)
			this.pharmacologicalClassification = new ArrayList<PharmacologicalClassification>();

		this.pharmacologicalClassification.add(pharmacologicalClassification);
	}

	public void addManufacturer(Manufacturer manufacturer) {
		if (this.manufacturer == null)
			this.manufacturer = new ArrayList<Manufacturer>();

		this.manufacturer.add(manufacturer);
	}

	public void addStrengthUnit(StrengthUnit strengthUnit) {
		if (this.strengthUnit == null)
			this.strengthUnit = new ArrayList<StrengthUnit>();

		this.strengthUnit.add(strengthUnit);
	}

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

	public void addFormulationType(FormulationType formulationType) {
		if (this.formulationType == null) {
			this.formulationType = new ArrayList<FormulationType>();
		}
		this.formulationType.add(formulationType);
	}

}

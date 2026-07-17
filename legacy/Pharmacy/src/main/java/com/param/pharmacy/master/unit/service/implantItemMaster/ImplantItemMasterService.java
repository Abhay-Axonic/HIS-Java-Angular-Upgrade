package com.param.pharmacy.master.unit.service.implantItemMaster;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.ImplantSpeciality;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.ProductCategory;
import com.param.entity.model.master.StorageUnit;
import com.param.entity.model.master.Tax;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.model.SpecialityMaster;
import com.param.global.service.global.IGlobalService;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.unit.dao.implantItemMaster.IImplantItemMasterDao;
import com.param.pharmacy.master.unit.dto.implantItemMaster.GetItemResponse;
import com.param.pharmacy.master.unit.dto.implantItemMaster.ImplantItemMasterDropdown;
import com.param.pharmacy.master.unit.dto.implantItemMaster.ImplantItemMasterListResponse;
import com.param.pharmacy.master.unit.dto.implantItemMaster.ImplantItemMasterRequest;
import com.param.pharmacy.master.unit.dto.implantItemMaster.ImplantItemMasterRequestItems;
import com.param.pharmacy.master.unit.dto.implantItemMaster.ImplantItemMasterResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("ImplantItemMasterService")

public class ImplantItemMasterService implements IImplantItemMasterService

{
	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IImplantItemMasterDao ImlantItemMasterDao;

	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public ImplantItemMasterDropdown getDropdowns(Integer unitId, Integer orgId) {

		ImplantItemMasterDropdown response = new ImplantItemMasterDropdown();

		for (StorageUnit storageUnit : GlobalDao.getStorageUnitList(orgId)) {
			response.addStorageUnit(
					new com.param.global.dto.generic.StorageUnit(storageUnit.getId(), storageUnit.getUnitName()));
		}

		for (Tax tax : GlobalDao.getTaxList(orgId)) {
			response.addTax(new com.param.global.dto.generic.Tax(tax.getId(), tax.getName()));
		}

		return response;
	}

	@Override
	@Transactional
	public List<ImplantItemMasterResponse> getAll(Integer unitId) {
		List<ImplantItemMasterResponse> responseList = new ArrayList<ImplantItemMasterResponse>();

		for (Item item : ImlantItemMasterDao.getAll(unitId)) {
			ImplantItemMasterResponse itemResponse = new ImplantItemMasterResponse(item.getId(),
					item.getProductCategory().getAssetCategory().getId(),
					item.getProductCategory().getAssetCategory().getCategory(), item.getProductCategory().getId(),
					item.getProductCategory().getCategory(), item.getManufacturer().getId(),
					item.getManufacturer().getName(), item.getCode(), item.getProductCode(), item.getName(),
					item.getSalesTax().getId(), item.getPurchaseTax().getId(), item.getSalesTax().getName(),
					item.getPurchaseTax().getName(), item.getStorageUnit().getId(), item.getStorageUnit().getUnitName(),
					item.getIsActive(), item.getIsConsignment(), item.getIsSterilize());
			itemResponse.setIsBatchRequired(item.getIsBatchRequired());
			for (ImplantSpeciality implantSpeciality : item.getImplantSpecialityList()) {
				if (!implantSpeciality.getIsDeleted()) {
					itemResponse.addItems(new ImplantItemMasterListResponse(implantSpeciality.getId(),
							implantSpeciality.getSpecialityMaster().getSpecialityName()));
				}
				/*
				 * else if(implantSpeciality.getIsDeleted()) { itemResponse.addItems(new
				 * ImplantItemMasterListResponse(implantSpeciality.getId(),implantSpeciality.
				 * getSpecialityMaster().getSpecialityName())); }
				 */
			}
			responseList.add(itemResponse);
		}
		return responseList;
	}

	@Override
	@Transactional
	public GetItemResponse getById(Integer id) {
		GetItemResponse responseList = new GetItemResponse();

		Item item = ImlantItemMasterDao.getById(id);

		if (item != null) {

			responseList = new GetItemResponse(item.getId(), item.getProductCategory().getAssetCategory().getId(),
					item.getProductCategory().getAssetCategory().getCategory(), item.getProductCategory().getId(),
					item.getProductCategory().getCategory(), item.getManufacturer().getId(),
					item.getManufacturer().getName(), item.getCode(), item.getProductCode(), item.getName(),
					item.getPurchaseTax().getId(), item.getSalesTax().getId(),item.getStorageUnit().getId(),
					item.getStorageUnit().getCode(), item.getIsActive(), item.getIsConsignment(),
					item.getIsSterilize());
			responseList.setIsBatchRequired(item.getIsBatchRequired());
			for (ImplantSpeciality implantSpeciality : item.getImplantSpecialityList()) {
				if (!implantSpeciality.getIsDeleted()) {
					responseList.addItems(new ImplantItemMasterRequestItems(implantSpeciality.getSpecialityMaster().getSpecialityId(),
							implantSpeciality.getSpecialityMaster().getSpecialityName()));
				} else if (implantSpeciality.getIsDeleted()) {
					responseList.addItems(new ImplantItemMasterRequestItems(implantSpeciality.getSpecialityMaster().getSpecialityId(),
							implantSpeciality.getSpecialityMaster().getSpecialityName()));
				}
			}
			responseList.add(responseList);
		} else {
		}
		return responseList;
	}

	@Override
	@Transactional
	public GetItemResponse update(ImplantItemMasterRequest itemRequest) throws CustomeException {

		Item item = ImlantItemMasterDao.getItem(itemRequest.getId());// ok

		if (itemRequest.getProductCategoryId() != null && itemRequest.getProductCategoryId() > 0) {
			ProductCategory productCategory = ImlantItemMasterDao
					.getProductCategory(itemRequest.getProductCategoryId());
			item.setProductCategory(productCategory);
		}

		if (itemRequest.getManufactureId() != null && itemRequest.getManufactureId() > 0) {
			Manufacturer manufacturer = ImlantItemMasterDao.getManufacture(itemRequest.getManufactureId());
			item.setManufacturer(manufacturer);
		}

		if (itemRequest.getStorageUnitId() != null && itemRequest.getStorageUnitId() > 0) {
			StorageUnit storageUnit = ImlantItemMasterDao.getStorageUnit(itemRequest.getStorageUnitId());
			item.setStorageUnit(storageUnit);
		}

		if (itemRequest.getsTaxId() != null && itemRequest.getsTaxId() > 0) {
			Tax sTax = ImlantItemMasterDao.getTax(itemRequest.getsTaxId());
			item.setSalesTax(sTax);
		}

		if (itemRequest.getpTaxId() != null && itemRequest.getpTaxId() > 0) {
			Tax pTax = ImlantItemMasterDao.getTax(itemRequest.getpTaxId());
			item.setPurchaseTax(pTax);
		}

		if (itemRequest.getItemCode() != null && itemRequest.getItemCode() != "") {
			item.setCode(itemRequest.getItemCode());
		}

		if (itemRequest.getProductCode() != null && itemRequest.getProductCode() != "") {
			item.setProductCode(itemRequest.getProductCode());
		}

		if (itemRequest.getIsConsignment() != null) {
			item.setIsConsignment(itemRequest.getIsConsignment());
		}

		if (itemRequest.getIsSterilize() != null) {
			item.setIsSterilize(itemRequest.getIsSterilize());
		}

		if (itemRequest.getStatus() != null) {
			item.setIsActive(itemRequest.getStatus());
		}
		if (itemRequest.getIsBatchRequired() != null) {
			item.setIsBatchRequired(itemRequest.getIsBatchRequired());
		}
		if (itemRequest.getItemName() != null) {
			item.setName(itemRequest.getItemName()); 
		}

		if (itemRequest.getSpecialeety() != null) {
			for (ImplantSpeciality detail : item.getImplantSpecialityList()) {
				Integer id = null;
				for (ImplantItemMasterRequestItems itemss : itemRequest.getSpecialeety()) {
					if (itemss.getId() != null && itemss.getId().equals(detail.getId())) {
						id = itemss.getId();
						detail.updateImplantSpeciality(new SpecialityMaster(itemss.getId()), new Item(item.getId()));
						break;
					}
				}
				if (id == null) {
					detail.setIsDeleted(true);
				} else {
					detail.setIsDeleted(false);
				}
				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(item.getUnitId()));
				detail.setUpdatedByUser(itemRequest.getUserId()!=null ? itemRequest.getUserId() : sessionService.getUserId());
			}
			if (itemRequest.getSpeciality() != null) {
				for (ImplantItemMasterRequestItems itemss : itemRequest.getSpecialeety()) {
					if (itemss.getId() == null) {
						item.addImplantItemMAsterDetail(
								new ImplantSpeciality(new SpecialityMaster(itemss.getId()), new Item(item.getId())));
					}
				}
			}
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Item updated successfully");
		

		Long productCategoryCount = Long.valueOf(0);
		Long manufactoreCount = Long.valueOf(0);
		Long storageUnitCount = Long.valueOf(0);
		Long taxCount = Long.valueOf(0);

		if (item.getIsActive()) {
			productCategoryCount = ImlantItemMasterDao.getproductCategoryCount(itemRequest.getId());
			manufactoreCount = ImlantItemMasterDao.getmanufactoreCount(itemRequest.getId());
			storageUnitCount = ImlantItemMasterDao.getstorageUnitCount(itemRequest.getId());
			taxCount = ImlantItemMasterDao.gettaxCount(itemRequest.getId());
		}
		Boolean UnableToUpdateStatus = false;
		if (productCategoryCount <= 0 && manufactoreCount <= 0 && storageUnitCount <= 0 && taxCount <= 0) {
			if (itemRequest.getStatus() != null) {
				UnableToUpdateStatus = false;
				item.setUpdatedDate(TimeZoneComponent.getDateByzone(item.getUnitId()));
				item.setUpdatedByUser(itemRequest.getUserId()!=null ? itemRequest.getUserId() : sessionService.getUserId());
				item.setIsActive(itemRequest.getStatus());
			}
			response.setMessage("Item Updated Successfully");
			UnableToUpdateStatus = false;
		} else if (productCategoryCount > 0) {
			UnableToUpdateStatus = true;
			item.setIsActive(false);
			response.setMessage("Product Category is not Active");
		} else if (manufactoreCount > 0) {
			UnableToUpdateStatus = true;
			item.setIsActive(false);
			response.setMessage("Manufactorer is not Active");
		} else if (storageUnitCount > 0) {
			UnableToUpdateStatus = true;
			item.setIsActive(false);
			response.setMessage("Storage is not Active");
		} else if (taxCount > 0) {
			UnableToUpdateStatus = true;
			item.setIsActive(false);
			response.setMessage("Tax is not Active");
		}

		Item updatedItem = ImlantItemMasterDao.updateItem(item);
		
		GetItemResponse updatedResponse = new GetItemResponse(updatedItem.getId(), updatedItem.getProductCategory().getAssetCategory().getId(),
				updatedItem.getProductCategory().getAssetCategory().getCategory(), updatedItem.getProductCategory().getId(),
				updatedItem.getProductCategory().getCategory(), updatedItem.getManufacturer().getId(),
				updatedItem.getManufacturer().getName(), updatedItem.getCode(), updatedItem.getProductCode(), updatedItem.getName(),
				updatedItem.getPurchaseTax().getId(), updatedItem.getSalesTax().getId(),updatedItem.getStorageUnit().getId(),
				updatedItem.getStorageUnit().getCode(), updatedItem.getIsActive(), updatedItem.getIsConsignment(),
				updatedItem.getIsSterilize());
		updatedResponse.setIsBatchRequired(updatedItem.getIsBatchRequired());
		response.setObject(updatedItem);
		if (UnableToUpdateStatus) {
			CustomeException customeException = new CustomeException(response.getId(), response.getMessage(),
					item.getIsActive());
			throw customeException;
		}
		return updatedResponse;
	}

	@Override
	@Transactional
	public CommonResponse implantItemMasterSave(ImplantItemMasterRequest request) {
		Item item = new Item(new ProductCategory(request.getProductCategoryId()), request.getItemCode(),
				request.getProductCode(), request.getItemName(), new Manufacturer(request.getManufactureId()),
				new Tax(request.getpTaxId()), new Tax(request.getsTaxId()), new StorageUnit(request.getStorageUnitId()),
				request.getIsConsignment(), request.getIsSterilize());
		item.setIsBatchRequired(request.getIsBatchRequired());
        if(request.getSpecialeety()!=null && request.getSpecialeety().size()>0) {
			for (ImplantItemMasterRequestItems items : request.getSpecialeety()) {
				ImplantSpeciality implantSpeciality = new ImplantSpeciality(new SpecialityMaster(items.getId()),
						new Item(item.getId()));
				//sessionService.setEntityDefaults(implantSpeciality);
				implantSpeciality.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

				item.addImplantItemMAsterDetail(implantSpeciality);
			}
        }
		item.setIsImplant(true);
	//	sessionService.setEntityDefaults(item);
		item.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		Item itmeResponse = ImlantItemMasterDao.implantItemMasterSave(item);

		CommonResponse response = new CommonResponse();
		response.setId(itmeResponse.getId().longValue());
		response.setMessage("Implant Item Master saved successfully");

		return response;
	}

}
package com.param.pharmacy.master.unit.service.fixedAsset;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.FixedAsset;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.ProductCategory;
import com.param.entity.model.master.Status;
import com.param.pharmacy.master.unit.dao.fixedAsset.IFixedAssetDao;
import com.param.pharmacy.master.unit.dto.fixedAsset.FixedAssetListResponse;
import com.param.pharmacy.master.unit.dto.fixedAsset.FixedAssetRequest;
import com.param.pharmacy.master.unit.dto.fixedAsset.FixedAssetResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("FixedAssetService")
public class FixedAssetService implements IFixedAssetService {

	@Autowired
	IFixedAssetDao FixedAssetDao;
	
	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public List<FixedAssetListResponse> getAll() {
		List<FixedAssetListResponse> list = new ArrayList<FixedAssetListResponse>();

		for (Item item : FixedAssetDao.getAll()) {
			FixedAssetListResponse fixedAssetResponse = new FixedAssetListResponse();
			fixedAssetResponse.setId((Integer) item.getId());
			fixedAssetResponse
					.setAssetTypeId((Integer) item.getProductCategory().getAssetCategory().getAssetType().getId());
			fixedAssetResponse
					.setAssetType((String) item.getProductCategory().getAssetCategory().getAssetType().getType());
			fixedAssetResponse.setAssetCategoryId((Integer) item.getProductCategory().getAssetCategory().getId());
			fixedAssetResponse.setAssetCategory((String) item.getProductCategory().getAssetCategory().getCategory());
			fixedAssetResponse.setProductCategoryId((Integer) item.getProductCategory().getId());
			fixedAssetResponse.setProductCategory((String) item.getProductCategory().getCategory());
			fixedAssetResponse.setItemCode((String) item.getCode());
			fixedAssetResponse.setProductCode((String) item.getProductCode());
			fixedAssetResponse.setName((String) item.getName());
			fixedAssetResponse.setManufactureId((Integer) item.getManufacturer().getId());
			fixedAssetResponse.setManufactureName((String) item.getManufacturer().getName());
			fixedAssetResponse.setStatus((Boolean) item.getIsActive());
			fixedAssetResponse.setIsCssd((Boolean) item.getIsCssd());
			fixedAssetResponse.setIsModality((Boolean) item.getIsModality());
			fixedAssetResponse.setIsChargeableEqupiment((Boolean) item.getIsChargeableEqupiment());
			fixedAssetResponse.setIsAccessories((Boolean) item.getIsAccessories());
			fixedAssetResponse.setIsSpares((Boolean) item.getIsSpares());
			fixedAssetResponse.setIsDialyser((Boolean) item.getIsDialyser());
			list.add(fixedAssetResponse);
		}
		return list;
	}

	@Override
	@Transactional
	public FixedAssetResponse getById(Integer id) {
		FixedAssetResponse response = new FixedAssetResponse();

		Item item = FixedAssetDao.getById(id);

		if (item != null) {

			response.setId((Integer) item.getId());
			response.setAssetTypeId((Integer) item.getProductCategory().getAssetCategory().getAssetType().getId());
			response.setAssetType((String) item.getProductCategory().getAssetCategory().getAssetType().getType());
			response.setAssetCategoryId((Integer) item.getProductCategory().getAssetCategory().getId());
			response.setAssetCategory((String) item.getProductCategory().getAssetCategory().getCategory());
			response.setProductCategoryId((Integer) item.getProductCategory().getId());
			response.setProductCategory((String) item.getProductCategory().getCategory());
			response.setItemCode((String) item.getCode());
			response.setProductCode((String) item.getProductCode());
			response.setName((String) item.getName());
			response.setManufactureId((Integer) item.getManufacturer().getId());
			response.setManufactureName((String) item.getManufacturer().getName());
			response.setStatus((Boolean) item.getIsActive());
			response.setIsCssd((Boolean) item.getIsCssd());
			response.setIsModality((Boolean) item.getIsModality());
			response.setIsChargeableEqupiment((Boolean) item.getIsChargeableEqupiment());
			response.setIsAccessories((Boolean) item.getIsAccessories());
			response.setIsSpares((Boolean) item.getIsSpares());
			response.setIsDialyser((Boolean) item.getIsDialyser());
			response.setIsCritical((Boolean)item.getIsCritical());
		} else {

		}
		return response;
	}

	@Override
	@Transactional
	public CommonResponse create(FixedAssetRequest fixedAssetRequest) {

		ProductCategory productCategory = new ProductCategory();
		//sessionService.setEntityDefaults(productCategory);
		productCategory.setCreateDefaults(fixedAssetRequest.getOrganizationId()!=null ? fixedAssetRequest.getOrganizationId() : sessionService.getOrganizationId(),fixedAssetRequest.getUnitId() !=null ? fixedAssetRequest.getUnitId() :sessionService.getUnitId() ,fixedAssetRequest.getUserId()!=null ? fixedAssetRequest.getUserId() : sessionService.getUserId());

		
		productCategory.setId(fixedAssetRequest.getProductCategoryId());

		Manufacturer manufacturer = new Manufacturer();
	//	sessionService.setEntityDefaults(manufacturer);
		manufacturer.setCreateDefaults(fixedAssetRequest.getOrganizationId()!=null ? fixedAssetRequest.getOrganizationId() : sessionService.getOrganizationId(),fixedAssetRequest.getUnitId() !=null ? fixedAssetRequest.getUnitId() :sessionService.getUnitId() ,fixedAssetRequest.getUserId()!=null ? fixedAssetRequest.getUserId() : sessionService.getUserId());

		manufacturer.setId(fixedAssetRequest.getManufactureId());

		Item item = new Item();
	//	sessionService.setEntityDefaults(item);
		item.setCreateDefaults(fixedAssetRequest.getOrganizationId()!=null ? fixedAssetRequest.getOrganizationId() : sessionService.getOrganizationId(),fixedAssetRequest.getUnitId() !=null ? fixedAssetRequest.getUnitId() :sessionService.getUnitId() ,fixedAssetRequest.getUserId()!=null ? fixedAssetRequest.getUserId() : sessionService.getUserId());

		item.setCode(fixedAssetRequest.getItemCode());
		item.setProductCode(fixedAssetRequest.getProductCode());
		item.setName(fixedAssetRequest.getName());
		item.setProductCategory(productCategory);
		item.setManufacturer(manufacturer);
		item.setIsActive(fixedAssetRequest.getStatus());
		item.setIsCssd(fixedAssetRequest.getIsCssd()!=null ? fixedAssetRequest.getIsCssd():null);
		item.setIsModality(fixedAssetRequest.getIsModality()!=null ? fixedAssetRequest.getIsModality():null);
		item.setIsChargeableEqupiment(fixedAssetRequest.getIsChargeableEqupiment()!=null ? fixedAssetRequest.getIsChargeableEqupiment():null);
		item.setIsAccessories(fixedAssetRequest.getIsAccessories()!=null ? fixedAssetRequest.getIsAccessories():null); 
		item.setIsSpares(fixedAssetRequest.getIsSpares()!=null ? fixedAssetRequest.getIsSpares():null);
		item.setIsDialyser(fixedAssetRequest.getIsDialyser()!=null ? fixedAssetRequest.getIsDialyser():null);
		item.setIsCritical(fixedAssetRequest.getIsCritical() != null? fixedAssetRequest.getIsCritical() : null);
		//item.setIsAccessories(fixedAssetRequest.getIsAccessories()); 
		//item.setIsSpares(fixedAssetRequest.getIsSpares());
		
		CommonResponse response = new CommonResponse();
		response.setId(FixedAssetDao.create(item).longValue());
		response.setMessage("Fixed Asset Inserted Successfully");
		if (fixedAssetRequest.getItemRequestId() != null && fixedAssetRequest.getItemRequestId() > 0)
		{
			com.param.entity.model.procurement.ItemRequestDetail itemRequestDetail = FixedAssetDao.getItemRequestDetail(fixedAssetRequest.getItemRequestId());
			itemRequestDetail.setFixedAsset(new FixedAsset(itemRequestDetail.getId()));
			itemRequestDetail.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
			
			if(itemRequestDetail.getItemRequest().getItemRequestDetailList().stream()
					.filter(req -> !req.getStatus().getId().equals(com.param.common.constants.Status.Closed.getStatus())).count() == 0) {
					itemRequestDetail.getItemRequest().setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
				}	

			FixedAssetDao.updateItemRequestDetail(itemRequestDetail);
						
		}
		return response;
	}

	@Override
	@Transactional
	public CommonResponse update(FixedAssetRequest fixedAssetRequest) {

		Item fixedAsset = FixedAssetDao.getById(fixedAssetRequest.getId());

		if (fixedAssetRequest.getProductCategoryId() != null && fixedAssetRequest.getProductCategoryId() > 0) {
			ProductCategory productCategory = FixedAssetDao
					.getProductCategory(fixedAssetRequest.getProductCategoryId());
			fixedAsset.setProductCategory(productCategory);
		}

		if (fixedAssetRequest.getManufactureId() != null && fixedAssetRequest.getManufactureId() > 0) {
			Manufacturer manufacturer = FixedAssetDao.getManufacturer(fixedAssetRequest.getManufactureId());
			fixedAsset.setManufacturer(manufacturer);
		}

		if (fixedAssetRequest.getIsCssd() != null) {
			fixedAsset.setIsCssd(fixedAssetRequest.getIsCssd());
		}
		
		if (fixedAssetRequest.getIsModality() != null) {
			fixedAsset.setIsModality(fixedAssetRequest.getIsModality());
		}
		
		if (fixedAssetRequest.getIsChargeableEqupiment() != null) {
			fixedAsset.setIsChargeableEqupiment(fixedAssetRequest.getIsChargeableEqupiment());
		}
		
		if (fixedAssetRequest.getItemCode() != null && fixedAssetRequest.getItemCode() != "") {
			fixedAsset.setCode(fixedAssetRequest.getItemCode());
		}

		if (fixedAssetRequest.getProductCode() != null && fixedAssetRequest.getProductCode() != "") {
			fixedAsset.setProductCode(fixedAssetRequest.getProductCode());
		}

		if (fixedAssetRequest.getName() != null && fixedAssetRequest.getName() != "") {
			fixedAsset.setName(fixedAssetRequest.getName());
		}

		if (fixedAssetRequest.getStatus() != null) {
			fixedAsset.setIsActive(fixedAssetRequest.getStatus());
		}
		
		
		if (fixedAssetRequest.getIsAccessories() != null) {
			fixedAsset.setIsAccessories(fixedAssetRequest.getIsAccessories());
		}
		if (fixedAssetRequest.getIsSpares() != null) {
			fixedAsset.setIsSpares(fixedAssetRequest.getIsSpares());
		}
		
		if (fixedAssetRequest.getIsDialyser() != null) {
			fixedAsset.setIsDialyser(fixedAssetRequest.getIsDialyser());
		}
		
		fixedAsset.setIsCritical(fixedAssetRequest.getIsCritical() != null? fixedAssetRequest.getIsCritical() : null);
		
		fixedAsset.setUpdatedDate(TimeZoneComponent.getDateByzone(fixedAsset.getUnitId()));
		fixedAsset.setUpdatedByUser(fixedAssetRequest.getUserId()!=null ? fixedAssetRequest.getUserId() : sessionService.getUserId());

		CommonResponse response = new CommonResponse();
		response.setId(FixedAssetDao.update(fixedAsset).longValue());
		response.setMessage("Fixed Asset Updated Successfully");

		return response;
	}

	@Override
	@Transactional
	public List<FixedAssetListResponse> getAssetNameListByAssetCategoryId(Integer assetCategoryId) {
		List<FixedAssetListResponse> list = new ArrayList<FixedAssetListResponse>();

		for (Item item : FixedAssetDao.getAssetNameListByAssetCategoryId(assetCategoryId)) {
			FixedAssetListResponse fixedAssetResponse = new FixedAssetListResponse();
			fixedAssetResponse.setId((Integer) item.getId());
			fixedAssetResponse
					.setAssetTypeId((Integer) item.getProductCategory().getAssetCategory().getAssetType().getId());
			fixedAssetResponse
					.setAssetType((String) item.getProductCategory().getAssetCategory().getAssetType().getType());
			fixedAssetResponse.setAssetCategoryId((Integer) item.getProductCategory().getAssetCategory().getId());
			fixedAssetResponse.setAssetCategory((String) item.getProductCategory().getAssetCategory().getCategory());
			fixedAssetResponse.setProductCategoryId((Integer) item.getProductCategory().getId());
			fixedAssetResponse.setProductCategory((String) item.getProductCategory().getCategory());
			fixedAssetResponse.setItemCode((String) item.getCode());
			fixedAssetResponse.setProductCode((String) item.getProductCode());
			fixedAssetResponse.setName((String) item.getName());
			fixedAssetResponse.setManufactureId((Integer) item.getManufacturer().getId());
			fixedAssetResponse.setManufactureName((String) item.getManufacturer().getName());
			fixedAssetResponse.setStatus((Boolean) item.getIsActive());
			fixedAssetResponse.setIsCssd((Boolean) item.getIsCssd());
			fixedAssetResponse.setIsModality((Boolean) item.getIsModality());
			fixedAssetResponse.setIsChargeableEqupiment((Boolean) item.getIsChargeableEqupiment());
			fixedAssetResponse.setIsAccessories((Boolean) item.getIsAccessories());
			fixedAssetResponse.setIsSpares((Boolean) item.getIsSpares());
			fixedAssetResponse.setIsDialyser((Boolean) item.getIsDialyser());
			list.add(fixedAssetResponse);
		}
		return list;

	}

}

package com.param.pharmacy.master.global.service.item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.FormulationType;
import com.param.entity.model.master.Generic;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.PharmacologicalClassification;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.StorageUnit;
import com.param.entity.model.master.StoreItemMapper;
import com.param.entity.model.master.StrengthUnit;
import com.param.entity.model.master.Tax;
import com.param.entity.model.procurement.ItemRequestDetail;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.ItemsRequest;
import com.param.global.dto.items.MultiItemsRequest;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dao.formulationType.IFormulationTypeDao;
import com.param.pharmacy.master.global.dao.generic.IGenericDao;
import com.param.pharmacy.master.global.dao.item.IItemDao;
import com.param.pharmacy.master.global.dao.manufacturer.IManufacturerDao;
import com.param.pharmacy.master.global.dao.productCategory.IProductCategoryDao;
import com.param.pharmacy.master.global.dao.strengthUnit.IStrengthUnitDao;
import com.param.pharmacy.master.global.dto.formulationType.FormulationTypeListResponse;
import com.param.pharmacy.master.global.dto.generic.GenericListResponse;
import com.param.pharmacy.master.global.dto.item.ItemDetailsResponse;
import com.param.pharmacy.master.global.dto.item.ItemDropdownResponse;
import com.param.pharmacy.master.global.dto.item.ItemListResponse;
import com.param.pharmacy.master.global.dto.item.ItemRabbitDto;
import com.param.pharmacy.master.global.dto.item.ItemRequest;
import com.param.pharmacy.master.global.dto.item.ItemSearchByNameResponse;
import com.param.pharmacy.master.global.dto.manufacturer.ManufacturerListResponse;
import com.param.pharmacy.master.global.dto.productCategory.ProductCategoryListResponse;
import com.param.pharmacy.master.global.dto.storageUnit.StorageUnitListResponse;
import com.param.pharmacy.master.global.dto.strengthUnit.StrengthUnitListResponse;
import com.param.pharmacy.master.global.dto.tax.GetTaxResponse;
import com.param.pharmacy.master.global.dto.unitOfMeasurement.UOMResponse;
import com.param.pharmacy.master.global.dto.unitOfMeasurement.UnitOfMeasurementListResponse;
import com.param.pharmacy.master.global.dto.unitOfMeasurement.UnitOfMeasurementRequest;
import com.param.pharmacy.master.global.service.storageUnit.IStorageUnitService;
import com.param.pharmacy.master.global.service.tax.ITaxService;
import com.param.pharmacy.master.global.service.unitOfMeasurement.IUnitOfMeasurementService;
import com.param.pharmacy.master.unit.dao.assetCategory.IAssetCategoryDao;
import com.param.pharmacy.master.unit.dao.assetType.IAssetTypeDao;
import com.param.pharmacy.master.unit.dao.pharmacologicalClassification.IPharmacologicalClassificationDao;
import com.param.pharmacy.master.unit.dao.storeItemMapper.IStoreItemMapperDao;
import com.param.pharmacy.master.unit.dto.pharmacologicalClassification.PharmacologicalClassificationListResponse;
import com.param.pharmacy.master.unit.dto.storeItemMapper.GetStoreItemResponse;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("ItemService")
public class ItemService implements IItemService, ICommonConstants,IBillingCommonConstants {
	
	
	private static final Logger log = Logger.getLogger(ItemService.class);
	
	@Autowired
	private SessionService sessionService;
	

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IOtimsService iOtimsService;

	@Autowired
	IItemDao ItemDao;

	@Autowired
	IGenericDao GenericDao;

	@Autowired
	IManufacturerDao ManufacturerDao;

	@Autowired
	IEmrService EmrService;
	
	@Autowired
	IAssetTypeDao AssetTypeDao;
	
	@Autowired
	IAssetCategoryDao AssetCategoryDao; 
	
	@Autowired
	IProductCategoryDao ProductCategoryDao;
	
	@Autowired
	IFormulationTypeDao FormulationTypeDao;
	
	@Autowired
	IStrengthUnitDao StrengthUnitDao;
	
	@Autowired
	IPharmacologicalClassificationDao PharmacologicalClassificationDao;
	
	@Autowired
	ITaxService TaxService;
	
	@Autowired
	IStorageUnitService StorageUnitService;
	
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	IUnitOfMeasurementService unitOfMeasurementService;
	
	@Autowired
	IStoreItemMapperDao StoreItemMapperDao;
	
	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;

	@Override
	@Transactional
	public ItemDropdownResponse getDropdowns(Integer orgId, Integer unitId) {

		ItemDropdownResponse response = new ItemDropdownResponse();

		response.setAssetType(GlobalDao.getAssetTypeListPT());
		
		response.setPharmacologicalClassification(GlobalDao.getPharmacologicalClassificationListPT(orgId,null));

		response.setManufacturer(GlobalDao.getManufacturerListPT(orgId,null));
		
		response.setStrengthUnit(GlobalDao.getStrengthUnitListPT(orgId));
		
		response.setStorageUnit(GlobalDao.getStorageUnitListPT(orgId));
		
		response.setTax(GlobalDao.getTaxListPT());
		
		response.setFormulationType(GlobalDao.getFormulationTypeListPT(orgId));
		
		return response;
	}

	@Override
	@Transactional
	public List<com.param.global.dto.global.FormulationType> getFormulationTypeList(Integer orgId) {
		List<com.param.global.dto.global.FormulationType> response = new ArrayList<com.param.global.dto.global.FormulationType>();
		for (FormulationType formulationType : GlobalDao.getFormulationTypeList(orgId)) {
			response.add(new com.param.global.dto.global.FormulationType(formulationType.getId(),
					formulationType.getType()));
		}
		return response;
	}

	@Override
	@Transactional
	public List<com.param.global.dto.generic.Generic> getGenericList(Integer productCategoryId,Integer orgId, String searchGenericName) {
		List<com.param.global.dto.generic.Generic> response = new ArrayList<com.param.global.dto.generic.Generic>();
		for (Generic generic : GlobalDao.getGenericList(productCategoryId,orgId,searchGenericName)) {
			response.add(new com.param.global.dto.generic.Generic(generic.getId(), generic.getName(), generic.getIsCombination()));
		}
		return response;
	}

	@Override
	@Transactional
	public List<ItemListResponse> getAll(Integer orgId,Integer offset,Integer noOfRecordsPerPage,String searchKeyward) throws Throwable {
		
		return ItemDao.getAllItems(orgId, offset, noOfRecordsPerPage, searchKeyward);
		
		//List<ItemListResponse> list = new ArrayList<ItemListResponse>();
		/*
		 * for (Item item : ItemDao.getAll()) { ItemListResponse itemResponse = new
		 * ItemListResponse(); itemResponse.setId((Integer) item.getId());
		 * itemResponse.setFormulationTypeId((Integer)
		 * item.getFormulationType().getId()); itemResponse.setFormulationType((String)
		 * item.getFormulationType().getType()); itemResponse.setGenericId((Integer)
		 * item.getGeneric().getId()); itemResponse.setGenericName((String)
		 * item.getGeneric().getName()); itemResponse.setProductCategoryId((Integer)
		 * item.getGeneric().getProductCategory().getId());
		 * itemResponse.setProductCategory((String)
		 * item.getGeneric().getProductCategory().getCategory());
		 * itemResponse.setAssetTypeId( (Integer)
		 * item.getGeneric().getProductCategory().getAssetCategory().getAssetType().
		 * getId()); itemResponse.setAssetType( (String)
		 * item.getGeneric().getProductCategory().getAssetCategory().getAssetType().
		 * getType()); itemResponse .setAssetCategoryId((Integer)
		 * item.getGeneric().getProductCategory().getAssetCategory().getId());
		 * itemResponse .setAssetCategory((String)
		 * item.getGeneric().getProductCategory().getAssetCategory().getCategory());
		 * itemResponse.setStrengthUnitId((Integer) item.getStrengthUnit().getId());
		 * itemResponse.setStrengthUnit((String) item.getStrengthUnit().getUnits());
		 * itemResponse.setCode((String) item.getCode()); itemResponse.setName((String)
		 * item.getName()); itemResponse.setProductCode((String) item.getProductCode());
		 * itemResponse.setStatus((Boolean) item.getIsActive());
		 * itemResponse.setStrength((Double) item.getStrength());
		 * itemResponse.setIsReusable((Boolean) item.getIsReusable());
		 * itemResponse.setIsVaccine((Boolean) item.getIsVaccin());
		 * itemResponse.setIsOtc((Boolean) item.getIsOtc());
		 * itemResponse.setIsCombination((Boolean) item.getIsCombination());
		 * itemResponse.setIsBatchRequired((Boolean) item.getIsBatchRequired());
		 * itemResponse.setIsHighrisk((Boolean) item.getIsHighRiskProduct());
		 * itemResponse.setIsPsychotropic((Boolean) item.getIsPsychotropic());
		 * itemResponse.setIsKit((Boolean) item.getIsKit());
		 * itemResponse.setIsInfusion((Boolean) item.getIsInfusion());
		 * itemResponse.setIsNarcotic((Boolean) item.getIsNarcotic());
		 * itemResponse.setPurchaseTax((String) item.getPurchaseTax().getName());
		 * itemResponse.setSaleTax((String) item.getSalesTax().getName());
		 * itemResponse.setPharmalogicalClassification( (String)
		 * item.getPharmacologicalClassification().getClassification());
		 * itemResponse.setManufacturer((String) item.getManufacturer().getName());
		 * itemResponse.setHsnCode((String) item.getHsnCode()); //
		 * itemResponse.setStorageUnit((Integer) item.getStorageUnit().getId());
		 * itemResponse.setStorageUnitName((String)
		 * item.getStorageUnit().getUnitName());
		 * itemResponse.setIsHospitalFormulary(item.getIsHospitalFormulary());
		 * itemResponse.setIsConsumable(item.getIsConsumable());
		 * itemResponse.setIsImplant(item.getIsImplant());
		 * itemResponse.setIsSterilize(item.getIsSterilize());
		 * itemResponse.setIsConsignment(item.getIsConsignment());
		 * list.add(itemResponse); }
		 * 
		 * return list1;
		 */
	}

	@Override
	@Transactional
	public ItemDetailsResponse getById(Integer id, Integer orgId) {
		ItemDetailsResponse response = new ItemDetailsResponse();

		Item item = ItemDao.getById(id,orgId);

		List<UnitOfMeasurementListResponse> uomRes  = unitOfMeasurementService.getUomByItemId(item.getId().intValue(),orgId);
		response.setUnitOfMeasurementDetailsResponse(uomRes);
		
		if (item != null) {
			Integer productCategoryId = null;
			response.setId((Integer) item.getId());
			response.setFormulationTypeId((Integer) item.getFormulationType().getId());
			response.setFormulationType((String) item.getFormulationType().getType());
			response.setGenericId((Integer) item.getGeneric().getId());
			response.setGenericName((String) item.getGeneric().getName());
			
			if(item.getGeneric()!=null && item.getGeneric().getProductCategory()!=null)
			{
				productCategoryId = item.getGeneric().getProductCategory().getId();
			}
			else{
				productCategoryId = item.getProductCategory().getId();
			}
			response.setProductCategoryId(productCategoryId);
			response.setProductCategory((String) item.getGeneric().getProductCategory().getCategory());
			response.setAssetTypeId(
					(Integer) item.getGeneric().getProductCategory().getAssetCategory().getAssetType().getId());
			response.setAssetType(
					(String) item.getGeneric().getProductCategory().getAssetCategory().getAssetType().getType());
			response.setAssetCategoryId((Integer) item.getGeneric().getProductCategory().getAssetCategory().getId());
			response.setAssetCategory((String) item.getGeneric().getProductCategory().getAssetCategory().getCategory());
			response.setStrengthUnitId( item.getStrengthUnit()!=null ? (Integer)item.getStrengthUnit().getId():null);
			response.setStrengthUnit(item.getStrengthUnit()!=null ? (String) item.getStrengthUnit().getUnits():null);
			response.setCode((String) item.getCode());
			response.setName((String) item.getName());
			response.setProductCode((String) item.getProductCode());
			response.setStatus((Boolean) item.getIsActive());
			response.setStrength((Double) item.getStrength());
			response.setIsReusable((Boolean) item.getIsReusable());
			response.setIsVaccine((Boolean) item.getIsVaccin());
			//response.setIsOtc((Boolean) item.getIsOtc());
			response.setIsCombination((Boolean) item.getIsCombination());
			response.setIsBatchRequired((Boolean) item.getIsBatchRequired());
			response.setIsThreeUOMApplicable((Boolean) item.getIsThreeUOMApplicable());
			response.setIsHighrisk((Boolean) item.getIsHighRiskProduct());
			response.setIsPsychotropic((Boolean) item.getIsPsychotropic());
			response.setIsKit((Boolean) item.getIsKit());
			response.setIsInfusion((Boolean) item.getIsInfusion());
			response.setIsNarcotic((Boolean) item.getIsNarcotic());
			response.setPurchaseTaxId((Integer) item.getPurchaseTax().getId());
			response.setSaleTaxId((Integer) item.getSalesTax().getId());
			response.setPharmalogicalClassificationId((Integer) item.getPharmacologicalClassification().getId());
			response.setManufacturerId((Integer) item.getManufacturer().getId());
			response.setManufacturer(item.getManufacturer().getName());
			response.setHsnCode((String) item.getHsnCode());
			response.setCimsCode((String) item.getCimsCode());
			response.setCimsType((String) item.getCimsType());
			response.setStorageUnit((Integer) item.getStorageUnit().getId());
			response.setIsHospitalFormulary(item.getIsHospitalFormulary());
			response.setIsConsumable(item.getIsConsumable());
			response.setIsImplant(item.getIsImplant());
			response.setIsSterilize(item.getIsSterilize());
			response.setIsConsignment(item.getIsConsignment());
			response.setIsInjection(item.getIsInjection());
			response.setReusableCount(item.getReusableCount()!=null ? item.getReusableCount() :null);
			response.setReusablePercentage1(item.getReusablePercentage()!=null ? item.getReusablePercentage():null);
			response.setIsDoseCalculationApplicable(item.getIsDoseCalculationApplicable());
			response.setDoseCalculationApplicableData(item.getDoseCalculationApplicableData());
			response.setStoreItemMapperListResponse(StoreItemMapperDao.getStoreItemMappedListForItemMasterNew(item.getId(), item.getUnitId()));
			response.setIsOtc(item.getIsOtc());
			response.setIsEmergencyDrug(item.getIsEmergencyDrug());
			response.setIsLasa(item.getIsLasa() != null ? item.getIsLasa() : false);

		}

		return response;
	}


	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public CommonResponse create(ItemRequest itemRequest) {
		
		
		 CommonResponse itemNameResponse = ItemDao.checkForUniqueItemName(itemRequest);
		 if(itemNameResponse.getListObject().size() > 0 && itemNameResponse.getListObject() != null) {
				return new CommonResponse<>(ERROR, ALREADY_EXIST_CODE, "Item Name is Already Exists.", null, null);
			}
		 
 		FormulationType formulationType = new FormulationType();
 		formulationType.setCreateDefaults(itemRequest.getOrganizationId()!=null ? itemRequest.getOrganizationId() : sessionService.getOrganizationId(),itemRequest.getUnitId() !=null ? itemRequest.getUnitId() :sessionService.getUnitId() ,itemRequest.getUserId()!=null ? itemRequest.getUserId() : sessionService.getUserId());
		formulationType.setId(itemRequest.getFormulationTypeId());
		Generic generic = GenericDao.getById(itemRequest.getGenericId(),itemRequest.getOrganizationId());

		StrengthUnit strengthUnit = new StrengthUnit();
		strengthUnit.setCreateDefaults(itemRequest.getOrganizationId()!=null ? itemRequest.getOrganizationId() : sessionService.getOrganizationId(),itemRequest.getUnitId() !=null ? itemRequest.getUnitId() :sessionService.getUnitId() ,itemRequest.getUserId()!=null ? itemRequest.getUserId() : sessionService.getUserId());
		
		strengthUnit.setId(itemRequest.getStrengthUnitId()!=null ? itemRequest.getStrengthUnitId(): null);
		StrengthUnit strengthUnitObj = new StrengthUnit();
		if(strengthUnit.getId()!=null) {
		 strengthUnitObj = ItemDao.getStrengthUnit(strengthUnit.getId());
		}
		strengthUnit.setUnits(strengthUnitObj.getUnits());
		strengthUnit.setCode(strengthUnitObj.getCode());

		PharmacologicalClassification pharmacologicalClassification = new PharmacologicalClassification();
		pharmacologicalClassification.setCreateDefaults(itemRequest.getOrganizationId()!=null ? itemRequest.getOrganizationId() : sessionService.getOrganizationId(),itemRequest.getUnitId() !=null ? itemRequest.getUnitId() :sessionService.getUnitId() ,itemRequest.getUserId()!=null ? itemRequest.getUserId() : sessionService.getUserId());
		
		pharmacologicalClassification.setId(itemRequest.getPharmacologicalClassificationId());

		Manufacturer manufacturer = new Manufacturer();
		manufacturer.setCreateDefaults(itemRequest.getOrganizationId()!=null ? itemRequest.getOrganizationId() : sessionService.getOrganizationId(),itemRequest.getUnitId() !=null ? itemRequest.getUnitId() :sessionService.getUnitId() ,itemRequest.getUserId()!=null ? itemRequest.getUserId() : sessionService.getUserId());
		
		manufacturer.setId(itemRequest.getManufacturerId());

		Tax sTax = new Tax();
		sTax.setCreateDefaults(itemRequest.getOrganizationId()!=null ? itemRequest.getOrganizationId() : sessionService.getOrganizationId(),itemRequest.getUnitId() !=null ? itemRequest.getUnitId() :sessionService.getUnitId() ,itemRequest.getUserId()!=null ? itemRequest.getUserId() : sessionService.getUserId());
		
		sTax.setId(itemRequest.getsTaxId());

		Tax pTax = new Tax();
		pTax.setCreateDefaults(itemRequest.getOrganizationId()!=null ? itemRequest.getOrganizationId() : sessionService.getOrganizationId(),itemRequest.getUnitId() !=null ? itemRequest.getUnitId() :sessionService.getUnitId() ,itemRequest.getUserId()!=null ? itemRequest.getUserId() : sessionService.getUserId());
		
		pTax.setId(itemRequest.getpTaxId());

		StorageUnit storageUnit = new StorageUnit();
		storageUnit.setCreateDefaults(itemRequest.getOrganizationId()!=null ? itemRequest.getOrganizationId() : sessionService.getOrganizationId(),itemRequest.getUnitId() !=null ? itemRequest.getUnitId() :sessionService.getUnitId() ,itemRequest.getUserId()!=null ? itemRequest.getUserId() : sessionService.getUserId());
		
		storageUnit.setId(itemRequest.getStorageUnitId());

		Item item = new Item();
		item.setCreateDefaults(itemRequest.getOrganizationId()!=null ? itemRequest.getOrganizationId() : sessionService.getOrganizationId(),itemRequest.getUnitId() !=null ? itemRequest.getUnitId() :sessionService.getUnitId() ,itemRequest.getUserId()!=null ? itemRequest.getUserId() : sessionService.getUserId());
	
		String ItemCode = iGlobalUnitMasterDao.getGeneratedGlobalNo(0,itemRequest.getOrganizationId(),itemRequest.getUnitId(), ITEM_MASTER);
		
		if(ItemCode == null ){
			ItemCode = ItemDao.getupdateItemCode(itemRequest.getOrganizationId());
		}
		
		item.setCode(ItemCode);
		item.setName(itemRequest.getName());
		item.setProductCode(itemRequest.getProductCode()!= null ?itemRequest.getProductCode(): "");
		item.setGeneric(generic);
		item.setStrength(itemRequest.getStrength()!=null ? itemRequest.getStrength():null);
		item.setHsnCode(itemRequest.getHsnCode());
		item.setStorageUnit(storageUnit);

		item.setFormulationType(formulationType);
		item.setIsBatchRequired(true);
		item.setPurchaseTax(pTax);
		item.setSalesTax(sTax);
		item.setHsnCode(itemRequest.getHsnCode());
		item.setCimsCode(itemRequest.getCimsCode());
		item.setCimsType(itemRequest.getCimsType());
		item.setManufacturer(manufacturer);
		if(strengthUnit.getId()!=null) {
			item.setStrengthUnit(strengthUnit);
		}
		item.setPharmacologicalClassification(pharmacologicalClassification);
		item.setPurchaseTax(pTax);
		item.setIsReusable(itemRequest.getIsReusable());
		item.setIsVaccin(itemRequest.getIsVaccine());
		item.setIsCombination(itemRequest.getIsCombination());
		item.setIsBatchRequired(itemRequest.getIsBatchRequired());
		item.setIsHighRiskProduct(itemRequest.getIsHighrisk());
		item.setIsPsychotropic(itemRequest.getIsPsychotropic());
		item.setIsKit(itemRequest.getIsKit());
		item.setIsInfusion(itemRequest.getIsInfusion());
		item.setIsThreeUOMApplicable(itemRequest.getIsThreeUOMApplicable());
		item.setIsNarcotic(itemRequest.getIsNarcotic());
		item.setIsHospitalFormulary(itemRequest.getIsHospitalFormulary());
		item.setIsConsumable(itemRequest.getIsConsumable());
		item.setIsConsignment(itemRequest.getIsConsignment());
		item.setIsInjection(itemRequest.getIsInjection());
		item.setSalesTax(sTax);
		item.setIsActive(itemRequest.getStatus());
		item.setIsDoseCalculationApplicable(itemRequest.getIsDoseCalculationApplicable());
		item.setIsOtc(itemRequest.getIsOtc());
		item.setIsEmergencyDrug(itemRequest.getIsEmergencyDrug());
		item.setIsLasa(itemRequest.getIsLasa() != null ? itemRequest.getIsLasa() : false);
		item.setReusableCount(itemRequest.getReusableCount()!=null ? itemRequest.getReusableCount() :null);
		try {
			item.setReusablePercentage(itemRequest.getReusablePercentage()!=null ? new ObjectMapper().writeValueAsString(itemRequest.getReusablePercentage()) :null);
			item.setDoseCalculationApplicableData(itemRequest.getDoseCalculationApplicableData1() != null ? new ObjectMapper().writeValueAsString(itemRequest.getDoseCalculationApplicableData1()) : null);

		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		Long itemId = ItemDao.create(item).longValue();
		if(itemRequest.getUnitOfMeasurementRequest()!=null )
		{
			UnitOfMeasurementRequest unitOfMeasurementRequest = itemRequest.getUnitOfMeasurementRequest();
			
			Optional<UOMResponse> matchingObject = unitOfMeasurementRequest.getUomList().stream().
				    filter(p -> p.getConversion()==null || p.getConversion()==0).
				    findAny();
			
			unitOfMeasurementRequest.setOrganizationId(itemRequest.getOrganizationId());
			unitOfMeasurementRequest.setUnitId(itemRequest.getUnitId());
			unitOfMeasurementRequest.setUserId(itemRequest.getUserId());
			if(!matchingObject.isPresent())
			{
				unitOfMeasurementRequest.setItemId(itemId.intValue());
				unitOfMeasurementService.create(Arrays.asList(unitOfMeasurementRequest),itemRequest.getUserId(),itemRequest.getOrganizationId());
			}
			
		}
		
		if(itemRequest.getStoreIds()!=null && !itemRequest.getStoreIds().isEmpty())
		{
			for(GetStoreItemResponse store: itemRequest.getStoreIds())
			{
				StoreItemMapper storeItemMapper = new StoreItemMapper();
				storeItemMapper.setCreateDefaults(itemRequest.getOrganizationId()!=null ? itemRequest.getOrganizationId() : sessionService.getOrganizationId(),itemRequest.getUnitId() !=null ? itemRequest.getUnitId() :sessionService.getUnitId() ,itemRequest.getUserId()!=null ? itemRequest.getUserId() : sessionService.getUserId());
				 
				storeItemMapper.setStore(new com.param.entity.model.master.Store(store.getStoreId())); 
				storeItemMapper.setItem(new Item(itemId.intValue()));
				storeItemMapper.setMinQty(store.getMinQty());
				storeItemMapper.setMaxQty(store.getMaxQty());
				storeItemMapper.setIsSellable(false);
				StoreItemMapperDao.create(storeItemMapper).longValue();	
			}	
		}
		
		
		
		
		CommonResponse response = new CommonResponse();
		response.setId(itemId);
		response.setMessage("Item Inserted Successfully");

		
		if (itemRequest.getItemRequestId() != null && itemRequest.getItemRequestId() > 0)
		{
			ItemRequestDetail itemRequestDetail = ItemDao.getItemRequestDetail(itemRequest.getItemRequestId());
			itemRequestDetail.setItem(item);
			itemRequestDetail.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
			
			if(itemRequestDetail.getItemRequest().getItemRequestDetailList().stream()
				.filter(req -> !req.getStatus().getId().equals(8)).count() == 0) {
				itemRequestDetail.getItemRequest().setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
			}			
			
			ItemDao.updateItemRequestDetail(itemRequestDetail);
		}

		return response;
	}

	@Override
	@Transactional
	public CommonResponse update(ItemRequest itemRequest) throws CustomeException {

		
		if(itemRequest.getCode()!=null  && itemRequest.getId()!=null)
		{
			 CommonResponse itemNameResponse = ItemDao.checkForUniqueItemName(itemRequest);
			 if(itemNameResponse.getListObject().size() > 0 && itemNameResponse.getListObject() != null) {
					return new CommonResponse<>(ERROR, ALREADY_EXIST_CODE, "Item Name is Already Exists.", null, null);
				}
		}
		 
		
		Item item = ItemDao.getById(itemRequest.getId(),itemRequest.getOrganizationId());
		
		item.setIsDoseCalculationApplicable(itemRequest.getIsDoseCalculationApplicable());
		item.setIsThreeUOMApplicable(itemRequest.getIsThreeUOMApplicable());
		if (itemRequest.getFormulationTypeId() != null && itemRequest.getFormulationTypeId() > 0) {
			FormulationType formulationType = ItemDao.getFormulationtype(itemRequest.getFormulationTypeId());
			item.setFormulationType(formulationType);
		}

		if (itemRequest.getGenericId() != null && itemRequest.getGenericId() > 0) {
			Generic generic = ItemDao.getGeneric(itemRequest.getGenericId());
			item.setGeneric(generic);
		}

		if (itemRequest.getManufacturerId() != null && itemRequest.getManufacturerId() > 0) {
			Manufacturer manufacturer = ItemDao.getManufacturer(itemRequest.getManufacturerId());
			item.setManufacturer(manufacturer);
		}

		if (itemRequest.getStrengthUnitId() != null && itemRequest.getStrengthUnitId() > 0) {
			StrengthUnit strengthUnit = ItemDao.getStrengthUnit(itemRequest.getStrengthUnitId());
			item.setStrengthUnit(strengthUnit);
		}

		if (itemRequest.getStorageUnitId() != null && itemRequest.getStorageUnitId() > 0) {
			StorageUnit storageUnit = ItemDao.getStorageUnit(itemRequest.getStorageUnitId());
			item.setStorageUnit(storageUnit);
		}

		if (itemRequest.getPharmacologicalClassificationId() != null
				&& itemRequest.getPharmacologicalClassificationId() > 0) {
			PharmacologicalClassification pharmacologicalClassification = ItemDao
					.getPharmacologicalClassification(itemRequest.getPharmacologicalClassificationId());
			item.setPharmacologicalClassification(pharmacologicalClassification);
		}

		if (itemRequest.getsTaxId() != null && itemRequest.getsTaxId() > 0) {
			Tax sTax = ItemDao.getTax(itemRequest.getsTaxId());
			item.setSalesTax(sTax);
		}

		if (itemRequest.getpTaxId() != null && itemRequest.getpTaxId() > 0) {
			Tax pTax = ItemDao.getTax(itemRequest.getpTaxId());
			item.setPurchaseTax(pTax);
		}

		if (itemRequest.getCode() != null && itemRequest.getCode() != "") {
			item.setCode(itemRequest.getCode());
		}

		if (itemRequest.getHsnCode() != null && itemRequest.getHsnCode() != "") {
			item.setHsnCode(itemRequest.getHsnCode());
		}
		
		if (itemRequest.getCimsCode() != null && itemRequest.getCimsCode() != "") {
		   item.setCimsCode(itemRequest.getCimsCode());
		}
		
		if (itemRequest.getCimsType() != null && itemRequest.getCimsType() != "") {
			   item.setCimsType(itemRequest.getCimsType());
			}
		
		if (itemRequest.getIsBatchRequired() != null) {
			item.setIsBatchRequired(itemRequest.getIsBatchRequired());
		}

		if (itemRequest.getIsCombination() != null) {
			item.setIsCombination(itemRequest.getIsCombination());
		}
		if (itemRequest.getIsConsignment() != null) {
			item.setIsConsignment(itemRequest.getIsConsignment());
		}

		if (itemRequest.getIsConsumable() != null) {
			item.setIsConsumable(itemRequest.getIsConsumable());
		}

		if (itemRequest.getIsImplant() != null) {
			item.setIsImplant(itemRequest.getIsImplant());
		}

		if (itemRequest.getIsSterilize() != null) {
			item.setIsSterilize(itemRequest.getIsSterilize());
		}

		if (itemRequest.getIsHighrisk() != null) {
			item.setIsHighRiskProduct(itemRequest.getIsHighrisk());
		}

		if (itemRequest.getIsHospitalFormulary() != null) {
			item.setIsHospitalFormulary(itemRequest.getIsHospitalFormulary());
		}

		if (itemRequest.getIsInfusion() != null) {
			item.setIsInfusion(itemRequest.getIsInfusion());
		}
		
		if(itemRequest.getIsThreeUOMApplicable() != false) {
			item.setIsThreeUOMApplicable(itemRequest.getIsThreeUOMApplicable());
		}

		if (itemRequest.getIsKit() != null) {
			item.setIsKit(itemRequest.getIsKit());
		}

		if (itemRequest.getIsNarcotic() != null) {
			item.setIsNarcotic(itemRequest.getIsNarcotic());
		}

		if (itemRequest.getIsPsychotropic() != null) {
			item.setIsPsychotropic(itemRequest.getIsPsychotropic());
		}

		if (itemRequest.getIsReusable() != null) {
			item.setIsReusable(itemRequest.getIsReusable());
		}

		if (itemRequest.getIsVaccine() != null) {
			item.setIsVaccin(itemRequest.getIsVaccine());
		}

		if (itemRequest.getStrength() != null) {
			item.setStrength(itemRequest.getStrength());
		}

		if (itemRequest.getName() != null && itemRequest.getGenericName() != "") {
			item.setName(itemRequest.getName());
		}

		if (itemRequest.getProductCode() != null) {
			item.setProductCode(itemRequest.getProductCode());
		}

		if (itemRequest.getStatus() != null) {
			item.setIsActive(itemRequest.getStatus());
		}
		if (itemRequest.getIsInjection() != null) {
			item.setIsInjection(itemRequest.getIsInjection());
		}
		if (itemRequest.getReusableCount() != null) {
			item.setReusableCount(itemRequest.getReusableCount());
		}
		if (itemRequest.getReusablePercentage() != null) {
			try {
				item.setReusablePercentage(new ObjectMapper().writeValueAsString(itemRequest.getReusablePercentage()));
			} catch (JsonProcessingException e) {
				log.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}
		}
		item.setIsLasa(itemRequest.getIsLasa() != null ? itemRequest.getIsLasa() : false);
		if(itemRequest.getDoseCalculationApplicableData1() != null) {
				try {
					
					item.setDoseCalculationApplicableData(new ObjectMapper().writeValueAsString(itemRequest.getDoseCalculationApplicableData1()));

				} catch (Exception e) {
					log.error(e.getMessage(), e);
					throw new ServiceException(e.getMessage());
				}
		}
		
		if(item.getCode() == null){
			
			String ItemCode = iGlobalUnitMasterDao.getGeneratedGlobalNo(0,itemRequest.getOrganizationId(),itemRequest.getUnitId(), ITEM_MASTER);
			
			if(ItemCode == null ){
				ItemCode = ItemDao.getupdateItemCode(itemRequest.getOrganizationId());
			}
			
			item.setCode(ItemCode);
		}
		if(itemRequest.getIsOtc()!=null){
			item.setIsOtc(itemRequest.getIsOtc());
		}
		item.setIsEmergencyDrug(itemRequest.getIsEmergencyDrug() !=  null ? itemRequest.getIsEmergencyDrug() : false);
		CommonResponse response = new CommonResponse();

		List<Batch> batchList = item.getBatchList();
		List<Long> batchIds = new ArrayList<Long>();
		if(!batchList.isEmpty()){
			batchList.forEach((e) -> batchIds.add(e.getId()));
			Long avaialableStoreStock = GlobalDao.getStoreStockByBatchList(sessionService.getStoreId(), batchIds);
			if(avaialableStoreStock > 0 && itemRequest.getStatus().equals(false)){
				item.setIsActive(true);
				response.setMessage("Item mapped to store, Unable to deactivate item");
				response.setId(item.getId().longValue());
				response.setCode(HttpStatus.NOT_ACCEPTABLE.toString());
				return response;
			}
		}
		
		if(itemRequest.getUnitOfMeasurementRequest()!=null)
		{
			UnitOfMeasurementRequest unitOfMeasurementRequest = itemRequest.getUnitOfMeasurementRequest();
			Optional<UOMResponse> matchingObject = unitOfMeasurementRequest.getUomList().stream().
				    filter(p -> p.getConversion()==null || p.getConversion()==0).
				    findAny();
			if(!matchingObject.isPresent())
			{
				unitOfMeasurementRequest.setItemId(itemRequest.getId());
				unitOfMeasurementService.update1(unitOfMeasurementRequest,item);
			}
		}
		response.setMessage("Item Updated Successfully");
		Long productCategoryCount = new Long(0);
		Long genericCount = new Long(0);
		Long storeWiseCount = new Long(0);
		Long formulationTypeCount = new Long(0);
		Long manufactoreCount = new Long(0);
		Long storageUnitCount = new Long(0);
		Long strengthUnitCount = new Long(0);
		Long taxCount = new Long(0);
		if (item.getIsActive()) {
			productCategoryCount = ItemDao.getproductCategoryCount(itemRequest.getId());
			genericCount = ItemDao.getgenericCount(itemRequest.getId());
			storeWiseCount = ItemDao.getstoreWiseCount(itemRequest.getId());
			formulationTypeCount = ItemDao.getformulationTypeCount(itemRequest.getId());
			manufactoreCount = ItemDao.getmanufactoreCount(itemRequest.getId());
			storageUnitCount = ItemDao.getstorageUnitCount(itemRequest.getId());
			strengthUnitCount = ItemDao.getstrengthUnitCount(itemRequest.getId());
			taxCount = ItemDao.gettaxCount(itemRequest.getId());
		}
		Boolean UnableToUpdateStatus = false;
		if (productCategoryCount <= 0 && genericCount <= 0 && storeWiseCount <= 0 && formulationTypeCount <= 0
				&& manufactoreCount <= 0 && storageUnitCount <= 0 && strengthUnitCount < 0 && taxCount <= 0) {
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
		} else if (genericCount > 0) {
			UnableToUpdateStatus = true;
			item.setIsActive(false);
			response.setMessage("Generic is not Active");
		} else if (storeWiseCount > 0) {
			UnableToUpdateStatus = true;
			item.setIsActive(false);
			response.setMessage("Store Wise Generic is not Active");
		} else if (formulationTypeCount > 0) {
			UnableToUpdateStatus = true;
			item.setIsActive(false);
			response.setMessage("Formulation Type is not Active");
		} else if (manufactoreCount > 0) {
			UnableToUpdateStatus = true;
			item.setIsActive(false);
			response.setMessage("Manufactorer is not Active");
		} else if (storageUnitCount > 0) {
			UnableToUpdateStatus = true;
			item.setIsActive(false);
			response.setMessage("Storage is not Active");
		} else if (strengthUnitCount > 0) {
			UnableToUpdateStatus = true;
			item.setIsActive(false);
			response.setMessage("Streng is not Active");
		} else if (taxCount > 0) {
			UnableToUpdateStatus = true;
			item.setIsActive(false);
			response.setMessage("Tax is not Active");
		}

		Generic gen = ItemDao.getGeneric(item.getGeneric().getId());
		Manufacturer manufact = ManufacturerDao.getById(item.getManufacturer().getId(),itemRequest.getOrganizationId());

		response.setId(ItemDao.update(item).longValue());
		
		if(itemRequest.getStoreIds()!=null && !itemRequest.getStoreIds().isEmpty())
		{
			for(GetStoreItemResponse store: itemRequest.getStoreIds())
			{
				if(store.getIsActive()!=null && store.getIsActive().equals(true) && store.getId()!=null)
				{
					StoreItemMapper storeItemMapper = StoreItemMapperDao.getById(store.getId());
					storeItemMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(item.getUnitId()));
					storeItemMapper.setUpdatedByUser(itemRequest.getUserId()!=null ? itemRequest.getUserId() : sessionService.getUserId());
					storeItemMapper.setStore(new com.param.entity.model.master.Store(store.getStoreId())); 
					storeItemMapper.setItem(new Item(item.getId()));
					storeItemMapper.setMinQty(store.getMinQty());
					storeItemMapper.setMaxQty(store.getMaxQty());
					storeItemMapper.setIsSellable(false);
					storeItemMapper.setIsActive(true);
					storeItemMapper.setIsDeleted(false);
					StoreItemMapperDao.update(storeItemMapper);
				}
				else if( store.getId()!=null && store.getIsActive().equals(false))
				{
					StoreItemMapper storeItemMapper = StoreItemMapperDao.getById(store.getId());
					storeItemMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(item.getUnitId()));
					storeItemMapper.setUpdatedByUser(itemRequest.getUserId()!=null ? itemRequest.getUserId() : sessionService.getUserId());
					storeItemMapper.setIsActive(false);
					storeItemMapper.setIsDeleted(true);
					StoreItemMapperDao.update(storeItemMapper);
				}
				else if( store.getId()==null)
				{
					StoreItemMapper storeItemMapper = new StoreItemMapper();
					storeItemMapper.setCreateDefaults(itemRequest.getOrganizationId()!=null ? itemRequest.getOrganizationId() : sessionService.getOrganizationId(),itemRequest.getUnitId() !=null ? itemRequest.getUnitId() :sessionService.getUnitId() ,itemRequest.getUserId()!=null ? itemRequest.getUserId() : sessionService.getUserId());
					
					storeItemMapper.setStore(new com.param.entity.model.master.Store(store.getStoreId())); 
					storeItemMapper.setItem(new Item(item.getId()));
					storeItemMapper.setMinQty(store.getMinQty());
					storeItemMapper.setMaxQty(store.getMaxQty());
					storeItemMapper.setIsSellable(false);
					storeItemMapper.setIsActive(true);
					storeItemMapper.setIsDeleted(false);
					StoreItemMapperDao.create(storeItemMapper);
				}
			}	
		}
		
		if (UnableToUpdateStatus) {
			CustomeException customeException = new CustomeException(response.getId(), response.getMessage(),
					item.getIsActive());
			throw customeException;
		}
		return response;
	}

	@Override
	@Transactional
	public List<ItemListResponse> itemsList(ItemsRequest itemRequest) {
		if (itemRequest.getAssetTypeId() == 1)
		{
			List<ItemListResponse> listResponse=ItemDao.itemsList(itemRequest);
			//System.out.println(listResponse.size());
			for(ItemListResponse itemListResponse : listResponse)
			{
				itemListResponse.setItemId(itemListResponse.getItemid());
				itemListResponse.setAssetCategory(itemListResponse.getAssetcategory());
				itemListResponse.setItemCode(itemListResponse.getItemcode());
				itemListResponse.setItemName(itemListResponse.getItemname());
				itemListResponse.setProductCategory(itemListResponse.getProductcategory());
			}
			return listResponse;

		} else if (itemRequest.getAssetTypeId() == 2) 
		{
			return ItemDao.assetItemsList(itemRequest);
		}
		return null;
	}
	
	/*
	 * @Override public Response getStoreFromSession( patientRegistrationDto) { try
	 * {
	 * 
	 * SessionRequest patientRegistrationDtosList = sessionFactory.getCurrentSession().getNamedQuery("GET_STORE_LIST_LIKE")
	 * .setParameter("storeId", patientRegistrationDto.getStoreId())
	 * .setResultTransformer(Transformers.aliasToBean(SessionRequest.class)).list();
	 * return new Response(SUCCESS, null, null, patientRegistrationDtosList, null);
	 * } catch (HibernateException he) { h
	 * 
	 * } catch (Exception e) {  } return new Response(ERROR,
	 * null, COMMON_ERROR_MESSAGE, null, null); }
	 */
	
	@Override
	@Transactional
	public Object itemSearch(String name)
	{
		List<ItemSearchByNameResponse> itemnameLsit =ItemDao.getItemSearchForAutoComplete(name);
//		for (Item item : ItemDao.getItemSearch(name))
//		{
//			if (item.getIsActive() == true && item.getIsDeleted() == false  && ditem.getIsImplant() == true )
//			{
//				ItemSearchByNameResponse itemResponse = new ItemSearchByNameResponse(item.getId(),item.getCode() + "|" + item.getName(),
//						item.getCode(),item.getName());
//				itemnameLsit.add(itemResponse);
//			}
//			else
//			{
//				break;
//			}
//		}
		return itemnameLsit;
	}

	@Override
	@Transactional
	public List<ItemListResponse> getIsVaccineEnabledItems() {
		//List<ItemListResponse> list = new ArrayList<ItemListResponse>();

		/*for (Item item : ItemDao.getIsEnabledVaccinationList()) {
			ItemListResponse itemResponse = new ItemListResponse();
			itemResponse.setId((Integer) item.getId());
			itemResponse.setCode((String) item.getCode());
			itemResponse.setName((String) item.getName());
			itemResponse.setProductCode((String) item.getProductCode());
			itemResponse.setStatus((Boolean) item.getIsActive());
			itemResponse.setStrength((Double) item.getStrength());
			itemResponse.setIsReusable((Boolean) item.getIsReusable());
			itemResponse.setIsVaccine((Boolean) item.getIsVaccin());
			itemResponse.setIsOtc((Boolean) item.getIsOtc());
			itemResponse.setIsCombination((Boolean) item.getIsCombination());
			itemResponse.setIsBatchRequired((Boolean) item.getIsBatchRequired());
			itemResponse.setIsHighrisk((Boolean) item.getIsHighRiskProduct());
			itemResponse.setIsPsychotropic((Boolean) item.getIsPsychotropic());
			itemResponse.setIsKit((Boolean) item.getIsKit());
			itemResponse.setIsInfusion((Boolean) item.getIsInfusion());
			itemResponse.setIsNarcotic((Boolean) item.getIsNarcotic());
			itemResponse.setPurchaseTax((String) item.getPurchaseTax().getName());
			itemResponse.setSaleTax((String) item.getSalesTax().getName());
			itemResponse.setPharmalogicalClassification(
					(String) item.getPharmacologicalClassification().getClassification());
			itemResponse.setManufacturer((String) item.getManufacturer().getName());
			itemResponse.setHsnCode((String) item.getHsnCode());
			// itemResponse.setStorageUnit((Integer) item.getStorageUnit().getId());
			itemResponse.setStorageUnitName((String) item.getStorageUnit().getUnitName());
			itemResponse.setIsHospitalFormulary(item.getIsHospitalFormulary());
			itemResponse.setIsConsumable(item.getIsConsumable());
			itemResponse.setIsConsignment(item.getIsConsignment());
			list.add(itemResponse);
		}*/
		return ItemDao.getIsEnabledVaccinationList();
	}

	@Override
	@Transactional
	public List<ItemListResponse> multipleStoreList(MultiItemsRequest itemRequest) {
		/*
		 * //System.out.println("Asset Type Id In Service: "+itemRequest.getAssetTypeId())
		 * ; if (itemRequest.getAssetTypeId() == 1) { return
		 * ItemDao.MultiItemsList(itemRequest); } else if (itemRequest.getAssetTypeId()
		 * == 2) { return ItemDao.MultiAssetItemsList(itemRequest); }
		 */
		
        List<ItemListResponse> listResponse= new ArrayList<ItemListResponse>();
        if (itemRequest.getAssetTypeId() == 1)
        {
	        listResponse=ItemDao.MultiItemsList(itemRequest);
	
	        for(ItemListResponse itemListResponse : listResponse)
	        {
	            itemListResponse.setItemId(itemListResponse.getItemid());
	            itemListResponse.setAssetCategory(itemListResponse.getAssetcategory());
	            itemListResponse.setItemCode(itemListResponse.getItemcode());
	            itemListResponse.setItemName(itemListResponse.getItemname());
	            itemListResponse.setProductCategory(itemListResponse.getProductcategory());
	        }
	        
	    }else if (itemRequest.getAssetTypeId() == 2) 
	    {
	        return ItemDao.MultiAssetItemsList(itemRequest);
	    }
	    return listResponse;	
	}

	@Override
	@Transactional
	public List<ItemListResponse> itemTypeaheadSearch(MultiItemsRequest itemsRequest) {
		return ItemDao.itemTypeaheadSearch(itemsRequest);
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	@Override
	@Transactional
	public Response uploadItemMaster(ItemRequest dto) {
		try {
			
			List<Item> itemMaster = new ArrayList<>();
			List<ItemRequest> failItemMaster = new ArrayList<>();
			InputStreamReader reader=new InputStreamReader(dto.getFile().getInputStream());
		    BufferedReader br =new BufferedReader(reader);  
		    String line="";
		   
		    List<ProductCategoryListResponse> productCatList =  ProductCategoryDao.getAll(dto.getOrganizationId()); 
		    List<FormulationTypeListResponse> formulationTypeList = FormulationTypeDao.getAllFormulation(dto.getOrganizationId());
		    List<GenericListResponse> genericList =  GenericDao.getAllGenericMasters(dto.getOrganizationId()); 
		    List<StrengthUnitListResponse> strengthUnitList = StrengthUnitDao.getStrengthUnitList(dto.getOrganizationId());
		    List<PharmacologicalClassificationListResponse> pharmacologicalClassificationList = PharmacologicalClassificationDao.getAllPharmacology(dto.getOrganizationId());
		    List<ManufacturerListResponse> manufacturerList = ManufacturerDao.getAll();
		    List<GetTaxResponse>  taxList = TaxService.getAll(dto.getOrganizationId());
		    List<StorageUnitListResponse> StorageUnitList = StorageUnitService.getAll(dto.getOrganizationId());
		    
		    //count variable
		    int count = 0;
		    Integer successCount=0,failureCount=0;
		    int failCount = 0;
		    //read line by line.
		    while((line = br.readLine())!=null) 
		    {
		    	
		    		StringBuilder remark = new StringBuilder();
				    List<ProductCategoryListResponse> productCatfilter = new ArrayList<>();
				    List<FormulationTypeListResponse> formulationTypeListfilter = new ArrayList<>();
				    List<GenericListResponse> genericListfilter = new ArrayList<>();
				    List<StrengthUnitListResponse> strengthUnitListfilter = new ArrayList<>();
				    List<PharmacologicalClassificationListResponse> pharmacologicalClassificationListfilter = new ArrayList<>();
				    List<ManufacturerListResponse> manufacturerListfilter = new ArrayList<>();
				    List<GetTaxResponse>  staxListfilter = new ArrayList<>();
				    List<GetTaxResponse>  ptaxListfilter = new ArrayList<>();
				    List<StorageUnitListResponse> StorageUnitListfilter = new ArrayList<>();
		    	
				  //after reading line at once, split by , bz, csv format & store in String[].
				    String [] stritem=line.trim().split(",");
				    
		    	
		    	//count++
		    	count++;
		    	
		    	if(count>1) 
		    	{
		    		dto.setCode(stritem[4].trim());
		    		if(dto.getCode()!= null && dto.getCode() != "") {
		    			CommonResponse itemResponse = ItemDao.checkForUniqueItemCode(dto);
						if (itemResponse.getListObject().size() > 0 && itemResponse.getListObject() != null) {
							failCount =1;
							if(remark.length()==0) {
			    				remark.append(" Item code already exist");
			    			}else {
			    				remark.append(", Item code already exist");
							}
						}
		    		}else {
		    			failCount =1;
		    			if(remark.length()==0) {
		    				remark.append(" Item code not exist");
		    			}else {
		    				remark.append(", Item code not exist");
						}
		    		}
					
		    		dto.setName(stritem[5].trim());
		    		if(dto.getName()!= null && dto.getName() != "") {
		    			CommonResponse itemNameResponse = ItemDao.checkForUniqueItemName(dto);
						if (itemNameResponse.getListObject().size() > 0 && itemNameResponse.getListObject() != null) {
							//return new Response<>(ERROR, ALREADY_EXIST_CODE, "Item Name is Already Exists.", null, null);
							failCount =1;
							if(remark.length()==0) {
			    				remark.append(" Item name already exist");
			    			}else {
			    				remark.append(", Item name already exist");
							}
						}
		    		}else {
		    			failCount =1;
		    			if(remark.length()==0) {
		    				remark.append(" Item name not exist");
		    			}else {
		    				remark.append(", Item name not exist");
						}
		    		}
		    		
					
					
					Stream<FormulationTypeListResponse> formulationTypStream = formulationTypeList.stream();
					formulationTypeListfilter = formulationTypStream.filter(str -> str.getType().toLowerCase().equals(stritem[3].trim().toLowerCase())).collect(Collectors.toList());
		    		
					FormulationType formulationType = new FormulationType();
					if(formulationTypeListfilter != null && formulationTypeListfilter.size()>0) {
						formulationType.setCreateDefaults(
								dto.getOrganizationId() != null ? dto.getOrganizationId()
										: sessionService.getOrganizationId(),
								dto.getUnitId() != null ? dto.getUnitId() : sessionService.getUnitId(),
								dto.getUserId() != null ? dto.getUserId() : sessionService.getUserId());
						formulationType.setId(formulationTypeListfilter.get(0).getId());
					}else {
						failCount =1;
						if(remark.length()==0) {
		    				remark.append(" Item formulation Type not exist");
		    			}else {
		    				remark.append(", Item formulation Type not exist");
						}
					}
					
					Stream<GenericListResponse> genericListStream = genericList.stream();
					genericListfilter = genericListStream.filter(str -> str.getName().toLowerCase().equals(stritem[7].trim().toLowerCase())).collect(Collectors.toList());
		    		
					Generic generic = new Generic();
					if(genericListfilter != null && genericListfilter.size()>0) {
						 generic = GenericDao.getById(genericListfilter.get(0).getId(), dto.getOrganizationId());
					}else {
						failCount =1;
						if(remark.length()==0) {
		    				remark.append(" Item Generic not exist");
		    			}else {
		    				remark.append(", Item Generic not exist");
						}
					}
					
					

					StrengthUnit strengthUnit = new StrengthUnit();
					
					Stream<StrengthUnitListResponse> strengthUnitListStream = strengthUnitList.stream();
					strengthUnitListfilter = strengthUnitListStream.filter(str -> str.getUnit().toLowerCase().equals(stritem[9].trim().toLowerCase())).collect(Collectors.toList());
		    		if(strengthUnitListfilter != null && strengthUnitListfilter.size()>0) {
		    			
		    			strengthUnit.setCreateDefaults(
								dto.getOrganizationId() != null ? dto.getOrganizationId()
										: sessionService.getOrganizationId(),
								dto.getUnitId() != null ? dto.getUnitId() : sessionService.getUnitId(),
								dto.getUserId() != null ? dto.getUserId() : sessionService.getUserId());

						strengthUnit.setId(strengthUnitListfilter.get(0).getId());
						StrengthUnit strengthUnitObj = new StrengthUnit();
						if (strengthUnit.getId() != null) {
							strengthUnitObj = ItemDao.getStrengthUnit(strengthUnit.getId());
						}
						strengthUnit.setUnits(strengthUnitObj.getUnits());
						strengthUnit.setCode(strengthUnitObj.getCode());
		    			
		    		}
					
		    		PharmacologicalClassification pharmacologicalClassification = new PharmacologicalClassification();
		    		
		    		Stream<PharmacologicalClassificationListResponse> pharmacologicalClassificationListStream = pharmacologicalClassificationList.stream();
		    		pharmacologicalClassificationListfilter = pharmacologicalClassificationListStream.filter(str -> str.getClassification().toLowerCase().equals(stritem[10].trim().toLowerCase())).collect(Collectors.toList());
		    		
					if(pharmacologicalClassificationListfilter != null && pharmacologicalClassificationListfilter.size()>0) {
						
						pharmacologicalClassification.setCreateDefaults(
								dto.getOrganizationId() != null ? dto.getOrganizationId()
										: sessionService.getOrganizationId(),
								dto.getUnitId() != null ? dto.getUnitId() : sessionService.getUnitId(),
								dto.getUserId() != null ? dto.getUserId() : sessionService.getUserId());

						pharmacologicalClassification.setId(pharmacologicalClassificationListfilter.get(0).getId());
						
					}else {
						failCount =1;
						if(remark.length()==0) {
		    				remark.append(" Item Pharmacological Classification not exist");
		    			}else {
		    				remark.append(", Item Pharmacological Classification not exist");
						}
					}
				
					
					Stream<ManufacturerListResponse> manufacturerListStream = manufacturerList.stream();
					manufacturerListfilter = manufacturerListStream.filter(str -> str.getName().toLowerCase().equals(stritem[11].trim().toLowerCase())).collect(Collectors.toList());
					Manufacturer manufacturer = new Manufacturer();
					if(manufacturerListfilter != null && manufacturerListfilter.size()>0) {
						manufacturer.setCreateDefaults(
								dto.getOrganizationId() != null ? dto.getOrganizationId()
										: sessionService.getOrganizationId(),
								dto.getUnitId() != null ? dto.getUnitId() : sessionService.getUnitId(),
								dto.getUserId() != null ? dto.getUserId() : sessionService.getUserId());

						manufacturer.setId(manufacturerListfilter.get(0).getId());
					}else {
						failCount =1;
						if(remark.length()==0) {
		    				remark.append(" Item manufacturer not exist");
		    			}else {
		    				remark.append(", Item manufacturer not exist");
						}
					}
					

					Stream<GetTaxResponse> taxListStream = taxList.stream();
					staxListfilter = taxListStream.filter(str -> str.getName().toLowerCase().equals(stritem[12].trim().toLowerCase())).collect(Collectors.toList());
					Tax sTax = new Tax();
					if(staxListfilter != null && staxListfilter.size()>0) {
						sTax.setCreateDefaults(
								dto.getOrganizationId() != null ? dto.getOrganizationId()
										: sessionService.getOrganizationId(),
								dto.getUnitId() != null ? dto.getUnitId() : sessionService.getUnitId(),
								dto.getUserId() != null ? dto.getUserId() : sessionService.getUserId());

						sTax.setId(staxListfilter.get(0).getId());
					}else {
						failCount =1;
						if(remark.length()==0) {
		    				remark.append(" Item sale tax not exist");
		    			}else {
		    				remark.append(", Item sale tax not exist");
						}
					}
				

					Stream<GetTaxResponse> ptaxListStream = taxList.stream();
					ptaxListfilter = ptaxListStream.filter(str -> str.getName().toLowerCase().equals(stritem[13].trim().toLowerCase())).collect(Collectors.toList());
					Tax pTax = new Tax();
					if(ptaxListfilter != null && ptaxListfilter.size()>0) {
						pTax.setCreateDefaults(
								dto.getOrganizationId() != null ? dto.getOrganizationId()
										: sessionService.getOrganizationId(),
								dto.getUnitId() != null ? dto.getUnitId() : sessionService.getUnitId(),
								dto.getUserId() != null ? dto.getUserId() : sessionService.getUserId());

						pTax.setId(ptaxListfilter.get(0).getId());
					}else {
						failCount =1;
						if(remark.length()==0) {
		    				remark.append(" Item purches tax not exist");
		    			}else {
		    				remark.append(", Item purches tax not exist");
						}
					}
					
					
					Stream<StorageUnitListResponse> StorageUnitListStream = StorageUnitList.stream();
					StorageUnitListfilter = StorageUnitListStream.filter(str -> str.getUnitName().toLowerCase().equals(stritem[15].trim().toLowerCase())).collect(Collectors.toList());
		    		
					StorageUnit storageUnit = new StorageUnit();
					if(StorageUnitListfilter != null && StorageUnitListfilter.size()>0) {
						storageUnit.setCreateDefaults(
								dto.getOrganizationId() != null ? dto.getOrganizationId()
										: sessionService.getOrganizationId(),
								dto.getUnitId() != null ? dto.getUnitId() : sessionService.getUnitId(),
								dto.getUserId() != null ? dto.getUserId() : sessionService.getUserId());

						storageUnit.setId(StorageUnitListfilter.get(0).getId());
					}else {
						failCount =1;
						if(remark.length()==0) {
		    				remark.append(" Item storage unit not exist");
		    			}else {
		    				remark.append(", Item storage unit not exist");
						}
					}
					

					Item item = new Item();
					item.setCreateDefaults(
							dto.getOrganizationId() != null ? dto.getOrganizationId()
									: sessionService.getOrganizationId(),
							dto.getUnitId() != null ? dto.getUnitId() : sessionService.getUnitId(),
							dto.getUserId() != null ? dto.getUserId() : sessionService.getUserId());

					item.setCode(stritem[4].trim());
					item.setName(stritem[6].trim());
					if(stritem[5].trim() != null && stritem[5].trim() != "") {
						item.setProductCode(stritem[5].trim());
					}else {
						failCount =1;
						if(remark.length()==0) {
		    				remark.append(" Item product code not exist");
		    			}else {
		    				remark.append(", Item product code not exist");
						}
					}
					
					item.setGeneric(generic);
					item.setStrength((stritem[8].trim() != null && stritem[8].trim() !="") ? Double.parseDouble(stritem[8].trim()) : null);
					
					if(stritem[14].trim() != null && stritem[14].trim() != ""){
						item.setHsnCode(stritem[14].trim());
					}else {
						failCount =1;
						if(remark.length()==0) {
		    				remark.append(" Item HSN code not exist");
		    			}else {
		    				remark.append(", Item HSN code not exist");
						}
					}
					
					item.setStorageUnit(storageUnit);
					item.setFormulationType(formulationType);
					item.setIsBatchRequired(true);
					item.setPurchaseTax(pTax);
					item.setSalesTax(sTax);
					item.setManufacturer(manufacturer);
					if (strengthUnit.getId() != null) {
						item.setStrengthUnit(strengthUnit);
					}
					item.setPharmacologicalClassification(pharmacologicalClassification);
					item.setPurchaseTax(pTax);
					
					item.setIsReusable((stritem[16].trim() != null && stritem[16].trim() !="") ? Boolean.parseBoolean(stritem[16].trim()) : false);
					item.setIsVaccin((stritem[17].trim() != null && stritem[17].trim() !="") ? Boolean.parseBoolean(stritem[17].trim()) : false);
					item.setIsCombination((stritem[18].trim() != null && stritem[18].trim() !="") ? Boolean.parseBoolean(stritem[18].trim()) : false);
					item.setIsBatchRequired((stritem[19].trim() != null && stritem[19].trim() !="") ? Boolean.parseBoolean(stritem[19].trim()) : false);
					item.setIsHighRiskProduct((stritem[20].trim() != null && stritem[20].trim() !="") ? Boolean.parseBoolean(stritem[20].trim()) : false);
					item.setIsPsychotropic((stritem[21].trim() != null && stritem[21].trim() !="") ? Boolean.parseBoolean(stritem[21].trim()) : false);
					item.setIsKit((stritem[22].trim() != null && stritem[22].trim() !="") ? Boolean.parseBoolean(stritem[22].trim()) : false);
					item.setIsInfusion((stritem[23].trim() != null && stritem[23].trim() !="") ? Boolean.parseBoolean(stritem[23].trim()) : false);
					item.setIsThreeUOMApplicable((stritem[28].trim() != null && stritem[28].trim() !="") ? Boolean.parseBoolean(stritem[28].trim()) : false);
					item.setIsNarcotic((stritem[24].trim() != null && stritem[24].trim() !="") ? Boolean.parseBoolean(stritem[24].trim()) : false);
					item.setIsHospitalFormulary((stritem[25].trim() != null && stritem[25].trim() !="") ? Boolean.parseBoolean(stritem[25].trim()) : false);
					item.setIsConsumable((stritem[26].trim() != null && stritem[26].trim() !="") ? Boolean.parseBoolean(stritem[26].trim()) : false);
					item.setIsInjection((stritem[27].trim() != null && stritem[27].trim() !="") ? Boolean.parseBoolean(stritem[27].trim()) : false);
					item.setSalesTax(sTax);
					item.setIsActive(true);

					//ItemDao.create(item);
					if(failCount == 0) {
						
						itemMaster.add(item);
						successCount++;
					}else {
						
						ItemRequest failDto = generateDto(stritem);
						failDto.setFailedRemark(remark.toString());
						failItemMaster.add(failDto);
						failureCount++;
					}
					failCount = 0;
			    		
		    	}
		    }
		    
		    
		    //***********Save Item master call****************
		    
		   if(!itemMaster.isEmpty()) {
			 ItemDao.saveItemMaster(itemMaster);
		   }
	     
		   
		   if(!failItemMaster.isEmpty()) 
		    {
			    Map<String,Object> mapList=new HashMap<>();
			    mapList.put("faultyItemList",failItemMaster);
			    HSSFWorkbook workbook=new HSSFWorkbook();
			    HttpServletRequest req = null;
			    HttpServletResponse res = null;
			    buildExcelDocument(mapList,workbook, req,res);
		    }
		   
	       ItemRequest finalDto=new ItemRequest();
	       finalDto.setSuccessCount(successCount);
	       finalDto.setFailureCount(failureCount);
	       
	       
		   return new Response("success","200", "Successfully Uploaded", null, finalDto);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	private void buildExcelDocument(Map<String, Object> mapList, HSSFWorkbook workbook, HttpServletRequest req,
			HttpServletResponse res) throws FileNotFoundException, IOException {
		// get data model which is passed by the Spring container
					List<ItemRequest> finalFaultyServiceList = (List<ItemRequest>) mapList.get("faultyItemList");
				
					// create a new Excel sheet
					HSSFSheet sheet = workbook.createSheet("Faulty_Entries_Book");
			        sheet.setDefaultColumnWidth(30);
			        //searching for any method...sheet.
			        // create style for header cells
			        CellStyle style = workbook.createCellStyle();
			        Font font = workbook.createFont();
			        font.setFontName("Arial");
			        style.setFillForegroundColor(HSSFColor.WHITE.index);
			        //style.setFillPattern(CellStyle.SOLID_FOREGROUND);//erases cell border..req here to skip...
			        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			        font.setColor(HSSFColor.BLACK.index);
			        style.setFont(font);
		        
		        
			     	// create header row
			        HSSFRow header = sheet.createRow(0);
			         
			        header.createCell(0).setCellValue("AssetType");
			        header.getCell(0).setCellStyle(style);
			         
			        header.createCell(1).setCellValue("AssetCategory");
			        header.getCell(1).setCellStyle(style);
			         
			        header.createCell(2).setCellValue("ProductCategory");
			        header.getCell(2).setCellStyle(style);
			         
			        header.createCell(3).setCellValue("FormulationType");
			        header.getCell(3).setCellStyle(style);
					
			        header.createCell(4).setCellValue("ProductCode");
			        header.getCell(4).setCellStyle(style);
			        
			        header.createCell(5).setCellValue("ItemName");
			        header.getCell(5).setCellStyle(style);
			        
			        header.createCell(6).setCellValue("GenericName");
			        header.getCell(6).setCellStyle(style);
			        
			        header.createCell(7).setCellValue("Strength");
			        header.getCell(7).setCellStyle(style);
			        
			        header.createCell(8).setCellValue("UnitName");
			        header.getCell(8).setCellStyle(style);
			        
			        header.createCell(9).setCellValue("ClassificationName");
			        header.getCell(9).setCellStyle(style);

			        header.createCell(10).setCellValue("ManufactureName");
			        header.getCell(10).setCellStyle(style);
			        

			        header.createCell(11).setCellValue("PurchaseTax");
			        header.getCell(11).setCellStyle(style);
			        

			        header.createCell(12).setCellValue("SaleTax");
			        header.getCell(12).setCellStyle(style);
			        

			        header.createCell(13).setCellValue("HsnCode");
			        header.getCell(13).setCellStyle(style);
			        

			        header.createCell(14).setCellValue("StorageUnit");
			        header.getCell(14).setCellStyle(style);
			        

			        header.createCell(15).setCellValue("ItemReusable");
			        header.getCell(15).setCellStyle(style);
			        

			        header.createCell(16).setCellValue("Is Vaccine");
			        header.getCell(16).setCellStyle(style);

			        header.createCell(17).setCellValue("Is Combination");
			        header.getCell(17).setCellStyle(style);

			        header.createCell(18).setCellValue("Batch Required");
			        header.getCell(18).setCellStyle(style);

			        header.createCell(19).setCellValue("High Risk");
			        header.getCell(19).setCellStyle(style);

			        header.createCell(20).setCellValue("Psychotropic");
			        header.getCell(20).setCellStyle(style);
			        
			        header.createCell(21).setCellValue("Is Kit");
			        header.getCell(21).setCellStyle(style);
			        
			        header.createCell(22).setCellValue("Is Infusion");
			        header.getCell(22).setCellStyle(style);
			        
			        header.createCell(23).setCellValue("Is Narcotic");
			        header.getCell(23).setCellStyle(style);
			        
			        header.createCell(24).setCellValue("Hospital Formulary");
			        header.getCell(24).setCellStyle(style);
			        
			        header.createCell(25).setCellValue("Is Consumable");
			        header.getCell(25).setCellStyle(style);
			        
			        header.createCell(26).setCellValue("Is Injection");
			        header.getCell(26).setCellStyle(style);
			        
			        header.createCell(27).setCellValue("Failed Remark");
			        header.getCell(27).setCellStyle(style);
			        
			        // create data rows
			        int rowCount = 1;
		         
			        for (ItemRequest sMasterDto : finalFaultyServiceList) 
			        {
			           
			        	//actual row creation from for each object.
			        	HSSFRow aRow = sheet.createRow(rowCount++);
			            aRow.createCell(0).setCellValue(sMasterDto.getAssetType());
			            aRow.createCell(1).setCellValue(sMasterDto.getAssetCategory());
			            aRow.createCell(2).setCellValue(sMasterDto.getProductCategory());
			            aRow.createCell(3).setCellValue(sMasterDto.getFormulationType());
			            aRow.createCell(4).setCellValue(sMasterDto.getProductCode());
			            aRow.createCell(5).setCellValue(sMasterDto.getName());
			            aRow.createCell(6).setCellValue(sMasterDto.getGenericName());
			            aRow.createCell(7).setCellValue(sMasterDto.getStrength());
			            aRow.createCell(8).setCellValue(sMasterDto.getStrengthUnit());
			            aRow.createCell(9).setCellValue(sMasterDto.getPharmalogicalClassification());
			            aRow.createCell(10).setCellValue(sMasterDto.getManufacturer());
			            aRow.createCell(11).setCellValue(sMasterDto.getPurchaseTax());
			            aRow.createCell(12).setCellValue(sMasterDto.getSaleTax());
			            aRow.createCell(13).setCellValue(sMasterDto.getHsnCode());
			            aRow.createCell(14).setCellValue(sMasterDto.getStorageUnitName());
			            aRow.createCell(15).setCellValue(sMasterDto.getIsReusable());
			            aRow.createCell(16).setCellValue(sMasterDto.getIsVaccine());
			            aRow.createCell(17).setCellValue(sMasterDto.getIsCombination());
			            aRow.createCell(18).setCellValue(sMasterDto.getIsBatchRequired());
			            aRow.createCell(19).setCellValue(sMasterDto.getIsHighrisk()!=null ? sMasterDto.getIsHighrisk() : false);
			            aRow.createCell(20).setCellValue(sMasterDto.getIsPsychotropic());
			            aRow.createCell(21).setCellValue(sMasterDto.getIsKit());
			            aRow.createCell(22).setCellValue(sMasterDto.getIsInfusion());
			            aRow.createCell(23).setCellValue(sMasterDto.getIsNarcotic());
			            aRow.createCell(24).setCellValue(sMasterDto.getIsHospitalFormulary());
			            aRow.createCell(25).setCellValue(sMasterDto.getIsConsumable());
			            aRow.createCell(26).setCellValue(sMasterDto.getIsInjection());
			            aRow.createCell(27).setCellValue(sMasterDto.getFailedRemark());
			
			        }
		        
		            String realPath = servletContext.getRealPath("/")+"resources"+ File.separator + "item-master-failed-records.xls";
		        	File file = new File(realPath);//write this workbook to an Outputstream.
		        	if(file.exists()==false) {
		        		//file.mkdir();
		        		workbook.write(new FileOutputStream(file));
		        	}else {
		        		workbook.write(new FileOutputStream(file));
		        	}
		
	}

	private ItemRequest generateDto(String[] stritem) {
		
		
		ItemRequest item = new ItemRequest();
		
		item.setAssetType(stritem[0].trim());
		item.setAssetCategory(stritem[1].trim());
		item.setProductCategory(stritem[2].trim());
		item.setCode(stritem[4].trim());
		item.setName(stritem[6].trim());
		if(stritem[5].trim() != null && stritem[5].trim() != "") {
			item.setProductCode(stritem[5].trim());
		}
		
		item.setGenericName(stritem[7].trim());
		item.setStrength((stritem[8].trim() != null && stritem[8].trim() !="") ? Double.parseDouble(stritem[8].trim()) : null);
		
		if(stritem[14].trim() != null && stritem[14].trim() != ""){
			item.setHsnCode(stritem[14].trim());
		}
		
		item.setStorageUnitName(stritem[15].trim());
		item.setFormulationType(stritem[3].trim());
		item.setIsBatchRequired(true);
		item.setPurchaseTax(stritem[12].trim());
		item.setSaleTax(stritem[13].trim());
		item.setManufacturer(stritem[11].trim());
		item.setStrengthUnit(stritem[9].trim());
		item.setPharmalogicalClassification(stritem[10].trim());
		item.setIsVaccine((stritem[17].trim() != null && stritem[17].trim() !="") ? Boolean.parseBoolean(stritem[17].trim()) : false);
		item.setIsReusable((stritem[16].trim() != null && stritem[16].trim() !="") ? Boolean.parseBoolean(stritem[16].trim()) : false);
		item.setIsCombination((stritem[18].trim() != null && stritem[18].trim() !="") ? Boolean.parseBoolean(stritem[18].trim()) : false);
		item.setIsBatchRequired((stritem[19].trim() != null && stritem[19].trim() !="") ? Boolean.parseBoolean(stritem[19].trim()) : false);
		item.setIsPsychotropic((stritem[21].trim() != null && stritem[21].trim() !="") ? Boolean.parseBoolean(stritem[21].trim()) : false);
		item.setIsKit((stritem[22].trim() != null && stritem[22].trim() !="") ? Boolean.parseBoolean(stritem[22].trim()) : false);
		item.setIsInfusion((stritem[23].trim() != null && stritem[23].trim() !="") ? Boolean.parseBoolean(stritem[23].trim()) : false);
		item.setIsThreeUOMApplicable((stritem[28].trim() != null && stritem[28].trim() !="") ? Boolean.parseBoolean(stritem[28].trim()) : false);
		item.setIsNarcotic((stritem[24].trim() != null && stritem[24].trim() !="") ? Boolean.parseBoolean(stritem[24].trim()) : false);
		item.setIsHospitalFormulary((stritem[25].trim() != null && stritem[25].trim() !="") ? Boolean.parseBoolean(stritem[25].trim()) : false);
		item.setIsConsumable((stritem[26].trim() != null && stritem[26].trim() !="") ? Boolean.parseBoolean(stritem[26].trim()) : false);
		item.setIsInjection((stritem[27].trim() != null && stritem[27].trim() !="") ? Boolean.parseBoolean(stritem[27].trim()) : false);
		
		return item;
	}

	@Override
	@Transactional
	public Response updatePreviousItemInActive(ItemRequest itemRequest) {
		// TODO Auto-generated method stub
		return ItemDao.updatePreviousItemInActive(itemRequest);
	}

	@Override
	@Transactional
	public ItemDropdownResponse searchClassificationName(Integer orgId, Integer unitId,
			String searchClassificationName) {
		ItemDropdownResponse response = new ItemDropdownResponse();
		response.setPharmacologicalClassification(GlobalDao.getPharmacologicalClassificationListPT(orgId,searchClassificationName));
		return response;
	}

	@Override
	@Transactional
	public ItemDropdownResponse searchManufactureName(Integer orgId, Integer unitId, String searchManufactureName) {
		ItemDropdownResponse response = new ItemDropdownResponse();
		response.setManufacturer(GlobalDao.getManufacturerListPT(orgId,searchManufactureName));
		return response;
	}

	@Override
	@Transactional
	public CommonResponse checkForUniqueItemName(ItemRequest dto) {
		try {
			return ItemDao.checkForUniqueItemName(dto);
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}

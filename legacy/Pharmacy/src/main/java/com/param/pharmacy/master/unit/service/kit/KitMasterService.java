package com.param.pharmacy.master.unit.service.kit;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Kit;
import com.param.entity.model.master.KitDetail;
import com.param.entity.model.master.KitType;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.pharmacy.PharmacyTariff;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IOTCompletionDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.PatientConsession;
import com.param.global.dto.items.ItemsSearchResponse;
import com.param.global.otims.dto.BatchDto;
import com.param.global.otims.dto.MedicationDetailsDto;
import com.param.pharmacy.master.unit.dao.Kit.IKitTypeMasterDao;
import com.param.pharmacy.master.unit.dto.kit.GetKitMasterResponseListByName;
import com.param.pharmacy.master.unit.dto.kit.ItemKitMasterRequest;
import com.param.pharmacy.master.unit.dto.kit.KitItemMasterResponse;
import com.param.pharmacy.master.unit.dto.kit.KitMasterDropdownResponse;
import com.param.pharmacy.master.unit.dto.kit.KitMasterResponseList;
import com.param.pharmacy.master.unit.dto.kit.KitTypeMasterRequest;
import com.param.pharmacy.master.unit.dto.kit.getKitMasterResponseByName;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service("KitMasterService")
public class KitMasterService implements IKitMasterService,ICommonConstants{
	
	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IKitTypeMasterDao KitTypeMasterDao;
	
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	IOTCompletionDao OTCompletionDao;

	private static final Logger logger = Logger.getLogger(KitMasterService.class);

	@Override
	@Transactional
	public KitMasterDropdownResponse getDropdowns() {

		KitMasterDropdownResponse response = new KitMasterDropdownResponse();
		
		for (KitType kitType : GlobalDao.getKitTypeList()) {
			response.addKitType(new com.param.global.dto.global.KitType(kitType.getId(), kitType.getDescription()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object saveKitMaster(KitTypeMasterRequest request) {

		Kit kit = new Kit(new KitType(request.getKitTypeId()),request.getCode(),request.getDescription());
		if(request.getStatus() != null)
		{
		kit.setIsActive(request.getStatus());
		}

		KitDetail kitDetail = null;
		for (ItemKitMasterRequest item : request.getItems()) {
			if (item.getUomUnitId()!=null)
			{
				KitDetail kitDetail2=new KitDetail(new Item(item.getId()),new UomUnit(item.getUomUnitId()),
						new UomType(item.getUomTypeId()),item.getQuantity());
			//	sessionService.setEntityDefaults(kitDetail2);
				kitDetail2.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

				kitDetail = kit.addKitDetailList(kitDetail2);
			//	sessionService.setEntityDefaults(kitDetail);
				kitDetail.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

			}
			else
			{
				KitDetail kitDetail2=new KitDetail(new Item(item.getId()),item.getQuantity());
				//sessionService.setEntityDefaults(kitDetail2);
				kitDetail2.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

				kitDetail = kit.addKitDetailList(kitDetail2);
				//sessionService.setEntityDefaults(kitDetail);
				kitDetail.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

			}


		}
	//	sessionService.setEntityDefaults(kit);	
		kit.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		KitTypeMasterDao.saveKitMaster(kit);
		
		CommonResponse response = new CommonResponse();
		response.setId(kit.getId().longValue());
		response.setMessage("Kit Master saved successfully");

		return response;
	}
	
	@Override
	@Transactional
	public List<KitItemMasterResponse> getAll(Integer orgId , Integer unitId) {
		List<KitItemMasterResponse> responseList = new ArrayList<KitItemMasterResponse>();

		for (Kit kit : KitTypeMasterDao.getAll(orgId , unitId)) 
		{
			KitItemMasterResponse itemResponse = new KitItemMasterResponse(kit.getId(),kit.getCode(),kit.getDescription(),kit.getKitType().getId(),
					kit.getKitType().getDescription(),kit.getIsActive());
				
					for (KitDetail items : kit.getKitDetailList())
					{
						if (!items.getIsDeleted()) 
						{
							itemResponse.addItems(new KitMasterResponseList(items.getId(),items.getItem().getId(),items.getItem().getName(),items.getItem().getCode(),
									items.getItem().getGeneric() != null ? items.getItem().getGeneric().getName() : "-",
									items.getItem().getManufacturer() != null ? items.getItem().getManufacturer().getName() : "-",
									items.getUomUnit() != null ? items.getUomUnit().getId() : null,
									items.getUomUnit() != null ? items.getUomUnit().getUnits() : null,
									items.getUomType() != null ? items.getUomType().getId() : null,
									items.getUomType() != null ? items.getUomType().getType() : null,
									items.getIsActive(),items.getQuantity()));
						}
				   }
				responseList.add(itemResponse);
		}
		return responseList;
	}
	
	@Override
	@Transactional
	public KitItemMasterResponse getByKitMasterId(Integer id) {
		
		KitItemMasterResponse responseList = new KitItemMasterResponse();

		Kit kit = KitTypeMasterDao.getByKitMasterId(id);

		if (kit != null) {

			responseList = new KitItemMasterResponse(kit.getId(),kit.getCode(),kit.getDescription(),kit.getKitType().getId(),
					kit.getKitType().getDescription(),kit.getIsActive());
				
			for (KitDetail items : kit.getKitDetailList())
			{
				if (!items.getIsDeleted()) 
				{
					responseList.addItems(new KitMasterResponseList(items.getId(),items.getItem().getId(),items.getItem().getName(),items.getItem().getCode(),
							items.getItem().getGeneric().getName(),	items.getItem().getManufacturer().getName(),items.getUomUnit().getId(),
							items.getUomUnit().getUnits(),items.getUomType().getId(),items.getUomType().getType(),
							items.getIsActive(),items.getQuantity()));
				}
		   }
			responseList.add(responseList);
		} else {
		}
		return responseList;
	}
	
	@Override
	@Transactional
	public Object getKitMasterByCodeAndDecriptionSearch(String name) 
	{
		List<getKitMasterResponseByName> responseList = new ArrayList<getKitMasterResponseByName>();
		for(Kit kit : KitTypeMasterDao.getKitMasterByCodeAndDecriptionSearch(name))
		{
				getKitMasterResponseByName response= new getKitMasterResponseByName(kit.getId(),kit.getCode(),kit.getDescription(),kit.getKitType().getId(),
				kit.getKitType().getDescription(),kit.getIsActive());
				
			for (KitDetail items : kit.getKitDetailList())
			{
					GetKitMasterResponseListByName response1 = new GetKitMasterResponseListByName(items.getId(),items.getItem().getId(),items.getItem().getName(),items.getItem().getCode(),
							items.getItem().getGeneric().getName(),	items.getItem().getManufacturer().getName(),items.getUomUnit().getId(),
							items.getUomUnit().getUnits(),items.getUomType().getId(),items.getUomType().getType(),
							items.getIsActive(),items.getQuantity());
							response.add(response1);	
			 }
			responseList.add(response);
		}
		return responseList;
	}
	@Override
	@Transactional
	public CommonResponse update(KitTypeMasterRequest request) {
		
		Kit kit = KitTypeMasterDao.getKitMasterById(request.getId(),request.getUnitId());

		/* kit.updateKitMaster(request.getCode(), request.getDescription()); */
		
		if(request.getCode() != null)
		{
			kit.setCode(request.getCode());
		}
		if(request.getDescription() != null)
		{
			kit.setDescription(request.getDescription());
		}
		if(request.getStatus() != null)
		{
			
			
			kit.setIsActive(request.getStatus());
		}
		if(request.getItems() != null)
		{
			
		
				KitDetail kitDetail = null;
				
				for (ItemKitMasterRequest item : request.getItems()) {
					Integer id = null;
					if (item.getId() != null) {
						
						KitDetail KitDtl=new KitDetail();
						KitDtl.setId(item.getId());
						KitDtl.setUpdatedDate(TimeZoneComponent.getDateByzone(kit.getUnitId()));
						KitDtl.setAddedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
						KitDtl.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
									
						kitDetail = kit.updateKitDetailList(KitDtl);
					 
						if(item.getItemId() != null)
						{
							kitDetail.setItem(new Item(item.getItemId()));
						}
						if(item.getUomUnitId() != null)
						{
							kitDetail.setUomUnit(new UomUnit(item.getUomUnitId()));
						}
						if(item.getUomTypeId()!= null)
						{
							kitDetail.setUomType(new UomType(item.getUomTypeId()));
						}
						if(item.getQuantity() != null && item.getQuantity() > 0)
						{
							
							kitDetail.setQuantity(item.getQuantity());
						}
						if(item.getStatus() != null)
						{
							kitDetail.setIsActive(item.getStatus());
						}
						kit.setUpdatedDate(TimeZoneComponent.getDateByzone(kit.getUnitId()));
						kit.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
					}
					if(item.getId() == null)
						
					{
						KitDetail kitDetail2=new KitDetail(new Item(item.getItemId()),new UomUnit(item.getUomUnitId()),
								new UomType(item.getUomTypeId()));
						kitDetail2.setUpdatedDate(TimeZoneComponent.getDateByzone(kit.getUnitId()));
						kitDetail2.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
						//sessionService.setEntityDefaults(kitDetail2);	
						kitDetail2.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),
								request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
						
						kitDetail= kit.updateKitDetailList(kitDetail2);
						
						
						if(item.getQuantity() != null && item.getQuantity() > 0)
						{
							kitDetail.setQuantity(item.getQuantity());
						}
						if(request.getStatus() != null)
						{
							kitDetail.setIsActive(item.getStatus());
						}
						kit.setUpdatedDate(TimeZoneComponent.getDateByzone(kit.getUnitId()));
						kit.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
					}
					
					
					
				}
		}
		kit.setKitType(new KitType(request.getKitTypeId()));
		kit.setUpdatedDate(TimeZoneComponent.getDateByzone(kit.getUnitId()));
		kit.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		KitTypeMasterDao.updateKitDetailMaster(kit);

		CommonResponse response = new CommonResponse();
		response.setMessage("Kit Master updated successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getMedicationKitByCodeAndDecriptionSearch(String name) {
		List<getKitMasterResponseByName> responseList = new ArrayList<getKitMasterResponseByName>();
		for(Kit kit : KitTypeMasterDao.getMedicationKitByCodeAndDecriptionSearch(name))
		{
				getKitMasterResponseByName response= new getKitMasterResponseByName(kit.getId(),kit.getCode(),kit.getDescription(),kit.getKitType().getId(),
				kit.getKitType().getDescription(),kit.getIsActive());
				
			for (KitDetail items : kit.getKitDetailList())
			{
					GetKitMasterResponseListByName response1 = new GetKitMasterResponseListByName(items.getId(),items.getItem().getId(),items.getItem().getName(),items.getItem().getCode(),
							items.getItem().getGeneric().getName(),	items.getItem().getManufacturer().getName(),items.getUomUnit().getId(),
							items.getUomUnit().getUnits(),items.getUomType().getId(),items.getUomType().getType(),
							items.getIsActive(),items.getQuantity());
							response.add(response1);	
			 }
			responseList.add(response);
		}
		return responseList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional
	public Response getKitDetailsByIdAndType(Integer storeId, Integer kitId, Integer kitTypeId,
			Integer orgId, Integer unitId,Integer paymentEntitlementId,Integer admissionId,Integer encounterId,Integer patientTypeId) throws ApplicationException {
		try
		{
		List<MedicationDetailsDto> itemList  = new ArrayList();
		Response res =  KitTypeMasterDao.getKitDetailsByIdAndType(storeId,kitId,kitTypeId,orgId,unitId);
		if(res.getStatus().equals(SUCCESS)) {
		 itemList = res.getListObject();
			for (MedicationDetailsDto dto : itemList) {
				
				Response res1 = KitTypeMasterDao.getBatchListByItemId(storeId,dto.getItemId(),orgId,unitId);
				if(res1.getStatus().equals(SUCCESS)) {
					List<BatchDto> batchList = res1.getListObject();
					for (BatchDto  batchDto : batchList) {
						Response res2 = KitTypeMasterDao.getUOMListByBatchId(batchDto.getBatchId(),orgId,unitId);
						if(res2.getStatus().equals(SUCCESS)) {
							batchDto.setUom(res2.getListObject());
						}
					}
					dto.setBatch(batchList);
				}
				
				List<Integer> prodCatId = new ArrayList<Integer>();
				prodCatId.add(dto.getProductCategoryId());
				List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatId, paymentEntitlementId);
				if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
					PharmacyTariff pharmacyTariff = pharmacyTariffs.getFirst();
					dto.setMarkUpDown(pharmacyTariff.getMarkUpDown());
					dto.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
				}
				
				PatientConsession patientConsession = null;
				if(encounterId != null && encounterId > 0) {
					patientConsession = GlobalDao.getPatientConsessionEncounter(dto.getItemId(), 
							encounterId, 
							patientTypeId, 
							dto.getAssetCategoryId(),
							dto.getProductCategoryId(),null,null);
				}
				else if(admissionId != null && admissionId > 0) {
					patientConsession = GlobalDao.getPatientConsessionAdmission(dto.getItemId(), 
							admissionId, 
							patientTypeId, 
							dto.getAssetCategoryId(),
							dto.getProductCategoryId());
				}
				
				if(patientConsession != null && patientConsession.isMarkupStatus()==false) {
					dto.setPatientClassPercentage(patientConsession.getPatientClassPercentage());
					dto.setMarkupStatus(patientConsession.isMarkupStatus());
				}
				else {
					dto.setPatientClassPercentage(0.00);
					dto.setMarkupStatus(false);
				}
			}
		}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, itemList, null);
		}
		 catch (Exception e)
			{
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	@Transactional
	public Response searchItems(boolean isImplant, String search,Integer paymentEntitlementId,Integer admissionId,Integer encounterId,Integer patientTypeId) throws ApplicationException {
		 List<ItemsSearchResponse>  itemList = 	OTCompletionDao.itemsSearch(isImplant, search, sessionService.getStoreId() ,sessionService.getUnitId());
		 
		 if(itemList != null && itemList.size()>0 ) {
			
			 for (ItemsSearchResponse dto : itemList) {
					List<Integer> prodCatId = new ArrayList<Integer>();
					prodCatId.add(dto.getProductCategoryId());
					List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatId, paymentEntitlementId);
					if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
						PharmacyTariff pharmacyTariff = pharmacyTariffs.getFirst();
						dto.setMarkUpDown(pharmacyTariff.getMarkUpDown());
						dto.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
					}
					
					PatientConsession patientConsession = null;
					if(encounterId != null && encounterId > 0) {
						patientConsession = GlobalDao.getPatientConsessionEncounter(dto.getId(), 
								encounterId, 
								patientTypeId, 
								dto.getAssetCategoryId(),
								dto.getProductCategoryId(),null,null);
					}
					else if(admissionId != null && admissionId > 0) {
						patientConsession = GlobalDao.getPatientConsessionAdmission(dto.getId(), 
								admissionId, 
								patientTypeId, 
								dto.getAssetCategoryId(),
								dto.getProductCategoryId());
					}
					
					if(patientConsession != null && patientConsession.isMarkupStatus()==false) {
						dto.setPatientClassPercentage(patientConsession.getPatientClassPercentage());
						dto.setMarkupStatus(patientConsession.isMarkupStatus());
					}
					else {
						dto.setPatientClassPercentage(0);
						dto.setMarkupStatus(false);
					}
			}
		 }
		 return new Response<>(SUCCESS, SUCCESS_CODE, null, itemList, null);
	}

	@Override
	@Transactional
	public Object updateStatus(KitTypeMasterRequest request) {


		KitTypeMasterDao.updateStatus(request);
		CommonResponse response = new CommonResponse();
		response.setMessage("Kit master updated successfully");

		return response;
	
	}

	@Override
	@Transactional
	public Response getKitDetails(Integer kitId, Integer storeId, Integer unitId) {
		return KitTypeMasterDao.getKitDetails(kitId,storeId,unitId);
	}


}

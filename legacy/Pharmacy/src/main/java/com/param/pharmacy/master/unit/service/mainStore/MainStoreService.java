package com.param.pharmacy.master.unit.service.mainStore;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.ProductCategory;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.StoreProductCategoryMapper;
import com.param.entity.model.master.StoreType;
import com.param.entity.model.master.VisitType;
import com.param.global.dao.global.IGlobalDao;
import com.param.pharmacy.master.global.dao.productCategory.IProductCategoryDao;
import com.param.pharmacy.master.global.dto.equipment.DepartmentStoreMapperDto;
import com.param.pharmacy.master.global.dto.equipment.WardStoreMapperDto;
import com.param.pharmacy.master.global.dto.productCategory.ProductCategoryListResponse;
import com.param.pharmacy.master.global.service.equipment.IDepartmentStoreMapperService;
import com.param.pharmacy.master.unit.dao.mainStore.IMainStoreDao;
import com.param.pharmacy.master.unit.dao.productRestriction.IProductRestrictionDao;
import com.param.pharmacy.master.unit.dao.storeItemMapper.IStoreItemMapperDao;
import com.param.pharmacy.master.unit.dto.MultipleStoreRequest;
import com.param.pharmacy.master.unit.dto.mainStore.MainStoreDetailsResponse;
import com.param.pharmacy.master.unit.dto.mainStore.MainStoreListResponse;
import com.param.pharmacy.master.unit.dto.mainStore.MainStoreRequest;
import com.param.pharmacy.master.unit.dto.mainStore.MainStoreRequestNew;
import com.param.pharmacy.master.unit.dto.mainStore.StoreRabbitDto;
import com.param.pharmacy.master.unit.dto.mainStore.StoreTypeRabbitDto;
import com.param.pharmacy.master.unit.dto.mainStore.VisitTypeRabbitDto;
import com.param.pharmacy.master.unit.dto.storeItemMapper.StoreItemMapperListResponse;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("MainStoreService")
public class MainStoreService implements IMainStoreService {

	@Autowired
	IMainStoreDao MainStoreDao;

	@Autowired
	IEmrService EmrService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IStoreItemMapperDao StoreItemMapperDao;

	@Autowired
	private IDepartmentStoreMapperService iDepartmentStoreMapperService;
	@Autowired
	IProductCategoryDao ProductCategoryDao;
	@Autowired
	IProductRestrictionDao ProductRestrictionDao;

	@Override
	@Transactional
	public List<MainStoreListResponse> getAll(Integer unitId, Integer orgId) {

		List<MainStoreListResponse> list = new ArrayList<MainStoreListResponse>();

		for (Store mainStore : MainStoreDao.getAll(unitId, orgId)) {
			MainStoreListResponse mainStoreRes = new MainStoreListResponse();
			mainStoreRes.setId((Integer) mainStore.getStoreId());
			mainStoreRes.setStoreTypeId((Integer) mainStore.getStoreType().getId());
			mainStoreRes.setStoreType((String) mainStore.getStoreType().getType());
			mainStoreRes.setStoreCode((String) mainStore.getStoreCode());
			mainStoreRes.setStoreName((String) mainStore.getStoreName());
			mainStoreRes.setStatus((Boolean) mainStore.getIsActive());
			mainStoreRes.setIsLinen((Boolean) mainStore.getIsLinen());
			mainStoreRes.setIsPharmacy((Boolean) mainStore.getIsPharmacy());
			mainStoreRes.setIsNarcotic((Boolean) mainStore.getIsNarcotic());
			mainStoreRes.setIsCSSD((Boolean) mainStore.getIsCSSD());
			mainStoreRes.setIsLaundry((Boolean) mainStore.getIsLaundry());
			mainStoreRes.setIsMaintenance((Boolean) mainStore.getIsMaintenance());
			mainStoreRes.setGeneralLedgerId(mainStore.getGeneralLedgerId());

			list.add(mainStoreRes);
		}
		return list;
	}

	@Override
	@Transactional
	public MainStoreDetailsResponse getById(Integer id) {
		MainStoreDetailsResponse response = new MainStoreDetailsResponse();

		Store mainStore = MainStoreDao.getById(id);

		if (mainStore != null) {
			response.setId((Integer) mainStore.getStoreId());
			response.setStoreTypeId((Integer) mainStore.getStoreType().getId());
			response.setStoreType((String) mainStore.getStoreType().getType());
			response.setStoreCode((String) mainStore.getStoreCode());
			response.setStoreName((String) mainStore.getStoreName());
			response.setVisitTypeId((Integer) mainStore.getVisitType().getVisitTypeId());
			response.setStatus((Boolean) mainStore.getIsActive());
			response.setIsLinen((Boolean) mainStore.getIsLinen());
			response.setIsPharmacy((Boolean) mainStore.getIsPharmacy());
			response.setIsNarcotic((Boolean) mainStore.getIsNarcotic());
			response.setIsCSSD((Boolean) mainStore.getIsCSSD());
			response.setIsLaundry((Boolean) mainStore.getIsLaundry());
			response.setIsMaintenance((Boolean) mainStore.getIsMaintenance());
			response.setGeneralLedgerId(mainStore.getGeneralLedgerId());

		} else {

		}
		return response;
	}

	@Override
	@Transactional
	public CommonResponse create(MainStoreRequest mainStoreRequest) {
		Store store = new Store();
		// sessionService.setEntityDefaults(store);
		store.setCreateDefaults(
				mainStoreRequest.getOrganizationId() != null ? mainStoreRequest.getOrganizationId()
						: sessionService.getOrganizationId(),
				mainStoreRequest.getUnitId() != null ? mainStoreRequest.getUnitId() : sessionService.getUnitId(),
				mainStoreRequest.getUserId() != null ? mainStoreRequest.getUserId() : sessionService.getUserId());

		StoreType storeType = new StoreType();
		// sessionService.setEntityDefaults(storeType);
		storeType.setCreateDefaults(
				mainStoreRequest.getOrganizationId() != null ? mainStoreRequest.getOrganizationId()
						: sessionService.getOrganizationId(),
				mainStoreRequest.getUnitId() != null ? mainStoreRequest.getUnitId() : sessionService.getUnitId(),
				mainStoreRequest.getUserId() != null ? mainStoreRequest.getUserId() : sessionService.getUserId());

		storeType.setId(mainStoreRequest.getStoreTypeId());
		StoreType storeTyp = MainStoreDao.getStoretype(storeType.getId());
		storeType.setType(storeTyp.getType());

		store.setStoreCode(mainStoreRequest.getStoreCode());
		store.setStoreName(mainStoreRequest.getStoreName());
		store.setIsActive(mainStoreRequest.getStatus());
		// 4 new field added
		store.setIsLinen(mainStoreRequest.getIsLinen());
		store.setIsPharmacy(mainStoreRequest.getIsPharmacy());
		store.setIsNarcotic(mainStoreRequest.getIsNarcotic());
		store.setIsCSSD(mainStoreRequest.getIsCSSD());
		store.setIsMaintenance(mainStoreRequest.getIsMaintenance());
		// 1 new field added
		store.setIsLaundry(mainStoreRequest.getIsLaundry());

		VisitType visitType = new VisitType();
		visitType.setVisitTypeId(mainStoreRequest.getVisitTypeId());
		VisitType visitTyp = MainStoreDao.getVisitType(visitType.getVisitTypeId());
		visitType.setVisitTypeName(visitTyp.getVisitTypeName());
		store.setVisitType(visitType);

		store.setStoreType(storeType);

		CommonResponse response = new CommonResponse();
		store.setGeneralLedgerId(mainStoreRequest.getGeneralLedgerId());
		Integer storeId = MainStoreDao.create(store);
		response.setId(storeId.longValue());
		response.setMessage("Store Inserted Successfully");
		List<ProductCategoryListResponse> data = ProductCategoryDao
				.getAll(mainStoreRequest.getOrganizationId() != null ? mainStoreRequest.getOrganizationId()
						: sessionService.getOrganizationId());
		for (ProductCategoryListResponse p : data) {
			StoreProductCategoryMapper storeProductCategoryMapper = new StoreProductCategoryMapper();
			// sessionService.setEntityDefaults(storeProductCategoryMapper);
			storeProductCategoryMapper.setCreateDefaults(
					mainStoreRequest.getOrganizationId() != null ? mainStoreRequest.getOrganizationId()
							: sessionService.getOrganizationId(),
					mainStoreRequest.getUnitId() != null ? mainStoreRequest.getUnitId() : sessionService.getUnitId(),
					mainStoreRequest.getUserId() != null ? mainStoreRequest.getUserId() : sessionService.getUserId());

			storeProductCategoryMapper.setIsActive(true);
			storeProductCategoryMapper.setProductCategory(new ProductCategory(p.getId()));
			storeProductCategoryMapper.setStore(new com.param.entity.model.master.Store(storeId));
			ProductRestrictionDao.create(storeProductCategoryMapper);
		}

		StoreRabbitDto storeRabbitDto = new StoreRabbitDto(response.getId().intValue(), store.getStoreCode(),
				store.getStoreName(), mainStoreRequest.getUserId(), store.getAddedDate(), store.getUpdatedByUser(),
				store.getUpdatedDate(), store.getIsDeleted(), store.getIsActive(), mainStoreRequest.getOrganizationId(),
				mainStoreRequest.getUnitId(),
				new StoreTypeRabbitDto(storeType.getId(), storeType.getCode(), storeType.getType()),
				new VisitTypeRabbitDto(visitType.getVisitTypeId(), visitType.getVisitTypeName(),
						visitType.getVisitTypeCode()));

		Request request = new Request();
		Payload payload = new Payload();
		payload.setActionType("create");
		payload.setData(storeRabbitDto);
		request.setKey(MasterKeyConstants.StoreMasterHISKey.toString());
		request.setPayload(payload);
		// EmrService.publish(request);
		return response;
	}

	@Override
	@Transactional
	public CommonResponse update(MainStoreRequest mainStoreRequest) {

		Store store = MainStoreDao.getById(mainStoreRequest.getId());
		StoreType storeType = new StoreType();
		// sessionService.setEntityDefaults(storeType);
		storeType.setCreateDefaults(
				mainStoreRequest.getOrganizationId() != null ? mainStoreRequest.getOrganizationId()
						: sessionService.getOrganizationId(),
				mainStoreRequest.getUnitId() != null ? mainStoreRequest.getUnitId() : sessionService.getUnitId(),
				mainStoreRequest.getUserId() != null ? mainStoreRequest.getUserId() : sessionService.getUserId());

		if (mainStoreRequest.getStoreTypeId() != null && mainStoreRequest.getStoreTypeId() > 0) {
			storeType = MainStoreDao.getStoretype(mainStoreRequest.getStoreTypeId());
			store.setStoreType(storeType);
		}

		if (mainStoreRequest.getStoreCode() != null && mainStoreRequest.getStoreCode() != "") {
			store.setStoreCode(mainStoreRequest.getStoreCode());
		}

		if (mainStoreRequest.getStoreName() != null && mainStoreRequest.getStoreName() != "") {
			store.setStoreName(mainStoreRequest.getStoreName());
		}

		VisitType visitType = new VisitType();
		if (mainStoreRequest.getVisitTypeId() != null && mainStoreRequest.getVisitTypeId() > 0) {
			visitType = MainStoreDao.getVisitType(mainStoreRequest.getVisitTypeId());
			store.setVisitType(visitType);
		}

		if (mainStoreRequest.getStatus() != null) {
			store.setIsActive(mainStoreRequest.getStatus());
		}
		if (mainStoreRequest.getIsLinen() != null) {
			store.setIsLinen(mainStoreRequest.getIsLinen());
		}
		if (mainStoreRequest.getIsPharmacy() != null) {
			store.setIsPharmacy(mainStoreRequest.getIsPharmacy());
		}
		if (mainStoreRequest.getIsNarcotic() != null) {
			store.setIsNarcotic(mainStoreRequest.getIsNarcotic());
		}
		if (mainStoreRequest.getIsCSSD() != null) {
			store.setIsCSSD(mainStoreRequest.getIsCSSD());
		}

		if (mainStoreRequest.getIsLaundry() != null) {
			store.setIsLaundry(mainStoreRequest.getIsLaundry());
		}

		if (mainStoreRequest.getIsMaintenance() != null) {
			store.setIsMaintenance(mainStoreRequest.getIsMaintenance());
		}

		store.setUpdatedDate(TimeZoneComponent.getDateByzone(store.getUnitId()));
		store.setUpdatedByUser(
				mainStoreRequest.getUserId() != null ? mainStoreRequest.getUserId() : sessionService.getUserId());
		CommonResponse response = new CommonResponse();
		store.setGeneralLedgerId(mainStoreRequest.getGeneralLedgerId());
		response.setId(MainStoreDao.update(store).longValue());
		response.setMessage("Store Updated Successfully");

		StoreRabbitDto storeRabbitDto = new StoreRabbitDto(response.getId().intValue(), store.getStoreCode(),
				store.getStoreName(), store.getAddedBy().getUserId(), store.getAddedDate(),
				store.getUpdatedBy().getUserId(), store.getUpdatedDate(), store.getIsDeleted(), store.getIsActive(),
				store.getOrganization().getOrganizationId(), store.getUnit().getUnitId(),
				new StoreTypeRabbitDto(storeType.getId(), storeType.getCode(), storeType.getType()),
				new VisitTypeRabbitDto(visitType.getVisitTypeId(), visitType.getVisitTypeName(),
						visitType.getVisitTypeCode()));

		Request request = new Request();
		Payload payload = new Payload();
		payload.setActionType("update");
		payload.setData(storeRabbitDto);
		request.setKey(MasterKeyConstants.StoreMasterHISKey.toString());
		request.setPayload(payload);
		// EmrService.publish(request);

		return response;
	}

	@Override
	@Transactional
	public CommonResponse updateStatus(Integer storeId, Boolean status, Integer orgId, Integer unitId, Integer userId) {

		Store store = MainStoreDao.getById(storeId);
		StoreType storeType = new StoreType();
		//// System.out.println("Store Id : "+store.getStoreId());
		// sessionService.setEntityDefaults(storeType);
		storeType.setCreateDefaults(orgId != null ? orgId : sessionService.getOrganizationId(),
				unitId != null ? unitId : sessionService.getUnitId(),
				userId != null ? userId : sessionService.getUserId());

		if (status) {
			store.setIsActive(true);
			//// System.out.println("True Status Updated");
			return getCommonResponse(status, store, storeType, "Store Updated Successfully",
					userId != null ? userId : sessionService.getUserId());
		} else {
			if (!checkUser(storeId)) {
				if (!checkItem(storeId)) {
					if (!checkDepartment(storeId)) {
						if (!checkWard(storeId)) {
							//// System.out.println("Returned Flag : "+checkDepartment(storeId,
							//// false,store,storeType));
							store.setIsActive(false);
							//// System.out.println("False Status Updated");
							return getCommonResponse(false, store, storeType, "Store Updated Successfully",
									userId != null ? userId : sessionService.getUserId());
						} else {
							CommonResponse response = new CommonResponse();
							response.setStatus(HttpStatus.CONFLICT.toString());
							response.setMessage("Store is Mapped to Ward");
							//// System.out.println("Store is Mapped to Ward");
							return response;
						}
					} else {
						CommonResponse response = new CommonResponse();
						response.setStatus(HttpStatus.CONFLICT.toString());
						response.setMessage("Store is Mapped to Department");
						//// System.out.println("Store is Mapped to Department");
						return response;
					}
				} else {
					CommonResponse response = new CommonResponse();
					response.setStatus(HttpStatus.CONFLICT.toString());
					response.setMessage("Store is Mapped to Item");
					//// System.out.println("Store is Mapped to Item");
					return response;
				}

			} else {
				CommonResponse response = new CommonResponse();
				response.setStatus(HttpStatus.CONFLICT.toString());
				response.setMessage("Store is Mapped to user");
				//// System.out.println("Store is Mapped to user");
				return response;
			}
		}
	}

	private boolean checkUser(Integer storeId) {
		List<Store> userStores = new ArrayList<>();
		userStores = GlobalDao.getUserActiveStoreList(storeId, null, null);
		boolean updateFlag = false;
		//// System.out.println("Users Length : "+userStores.size());
		//// System.out.println("Users Flag : "+userStores.isEmpty());
		if (!userStores.isEmpty()) {
			updateFlag = true;
		}

		return updateFlag;
	}

	private boolean checkItem(Integer storeId) {
		List<MultipleStoreRequest> request = new ArrayList<>();
		MultipleStoreRequest req = new MultipleStoreRequest();
		req.setId(storeId);
		request.add(req);
		StoreItemMapperListResponse  request1  = new StoreItemMapperListResponse();
		request1.setMultipleStoreRequest(request);
		request1.setOffset(0);
		request1.setNoOfRecordsPerPage(10000);
		CommonResponse res  =  StoreItemMapperDao.GetMultipleStore(request1);
		boolean updateFlag = false;

		if (res.getListObject() != null && res.getListObject().size()>0) {
			updateFlag = true;
		}

		return updateFlag;
	}

	private boolean checkWard(Integer storeId) {
		List<WardStoreMapperDto> wardStores;
		WardStoreMapperDto dto = new WardStoreMapperDto();
		dto.setOrgId(sessionService.getOrganizationId());
		dto.setUnitId(sessionService.getUnitId());
		dto.setStoreId(storeId);
		wardStores = iDepartmentStoreMapperService.getWardStoremappedListForStoreUpdate(dto);
		boolean updateFlag = false;
		//// System.out.println("Wards Length : "+wardStores.size());
		//// System.out.println("Wards Flag : "+wardStores.isEmpty());
		if (!wardStores.isEmpty()) {
			updateFlag = true;
		}
		return updateFlag;
	}

	private boolean checkDepartment(Integer storeId) {
		List<DepartmentStoreMapperDto> departmentStores;
		DepartmentStoreMapperDto dto = new DepartmentStoreMapperDto();
		dto.setOrgId(sessionService.getOrganizationId());
		dto.setUnitId(sessionService.getUnitId());
		dto.setStoreId(storeId);
		departmentStores = iDepartmentStoreMapperService.getDeptStoremappedListForStoreUpdate(dto);
		boolean updateFlag = false;
		//// System.out.println("Departments Length : "+departmentStores.size());
		//// System.out.println("Departments Flag : "+departmentStores.isEmpty());
		if (!departmentStores.isEmpty()) {
			updateFlag = true;
		}
		return updateFlag;
	}

	private CommonResponse getCommonResponse(Boolean status, Store store, StoreType storeType, String msg,
			Integer userId) {
		store.setIsActive(status);
		store.setUpdatedDate(TimeZoneComponent.getDateByzone(store.getUnitId()));
		store.setUpdatedByUser(userId != null ? userId : sessionService.getUserId());
		CommonResponse response = new CommonResponse();
		response.setId(MainStoreDao.update(store).longValue());
		response.setMessage(msg);
		response.setStatus(HttpStatus.OK.toString());

		StoreRabbitDto storeRabbitDto = new StoreRabbitDto(response.getId().intValue(), store.getStoreCode(),
				store.getStoreName(), store.getAddedBy().getUserId(), store.getAddedDate(),
				store.getUpdatedBy().getUserId(), store.getUpdatedDate(), store.getIsDeleted(), store.getIsActive(),
				store.getOrganization().getOrganizationId(), store.getUnit().getUnitId(),
				new StoreTypeRabbitDto(storeType.getId(), storeType.getCode(), storeType.getType()));

		Request request = new Request();
		Payload payload = new Payload();
		payload.setActionType("update");
		payload.setData(storeRabbitDto);
		request.setKey(MasterKeyConstants.StoreMasterHISKey.toString());
		request.setPayload(payload);
		// EmrService.publish(request);

		return response;
	}

	@Override
	@Transactional
	public CommonResponse updateNew(MainStoreRequestNew mainStoreRequest) {
		Store store = MainStoreDao.getById(mainStoreRequest.getId());
		StoreType storeType = new StoreType();
		// sessionService.setEntityDefaults(storeType);
		storeType.setCreateDefaults(
				mainStoreRequest.getOrganizationId() != null ? mainStoreRequest.getOrganizationId()
						: sessionService.getOrganizationId(),
				mainStoreRequest.getUnitId() != null ? mainStoreRequest.getUnitId() : sessionService.getUnitId(),
				mainStoreRequest.getUserId() != null ? mainStoreRequest.getUserId() : sessionService.getUserId());

		if (mainStoreRequest.getStoreTypeId() != null && mainStoreRequest.getStoreTypeId() > 0) {
			storeType = MainStoreDao.getStoretype(mainStoreRequest.getStoreTypeId());
			store.setStoreType(storeType);
		}

		if (mainStoreRequest.getStoreCode() != null && mainStoreRequest.getStoreCode() != "") {
			store.setStoreCode(mainStoreRequest.getStoreCode());
		}

		if (mainStoreRequest.getStoreName() != null && mainStoreRequest.getStoreName() != "") {
			store.setStoreName(mainStoreRequest.getStoreName());
		}

		List<VisitType> visitTypes = new ArrayList<>();
		if (mainStoreRequest.getVisitTypeId() != null) {
			VisitType visitType = new VisitType();
			for (int i = 0; i < mainStoreRequest.getVisitTypeId().length; i++) {
				visitType = MainStoreDao.getVisitType(mainStoreRequest.getVisitTypeId()[i]);
				store.setVisitType(visitType);
			}
			visitTypes.add(visitType);
		}

		if (mainStoreRequest.getStatus() != null) {
			store.setIsActive(mainStoreRequest.getStatus());
		}
		if (mainStoreRequest.getIsLinen() != null) {
			store.setIsLinen(mainStoreRequest.getIsLinen());
		}
		if (mainStoreRequest.getIsPharmacy() != null) {
			store.setIsPharmacy(mainStoreRequest.getIsPharmacy());
		}
		if (mainStoreRequest.getIsNarcotic() != null) {
			store.setIsNarcotic(mainStoreRequest.getIsNarcotic());
		}
		if (mainStoreRequest.getIsCSSD() != null) {
			store.setIsCSSD(mainStoreRequest.getIsCSSD());
		}

		if (mainStoreRequest.getIsLaundry() != null) {
			store.setIsLaundry(mainStoreRequest.getIsLaundry());
		}

		if (mainStoreRequest.getIsMaintenance() != null) {
			store.setIsMaintenance(mainStoreRequest.getIsMaintenance());
		}

		store.setUpdatedDate(TimeZoneComponent.getDateByzone(store.getUnitId()));
		store.setUpdatedByUser(
				mainStoreRequest.getUserId() != null ? mainStoreRequest.getUserId() : sessionService.getUserId());
		CommonResponse response = new CommonResponse();
		for (int i = 0; i < visitTypes.size(); i++) {
			response.setId(MainStoreDao.update(store).longValue());
			response.setMessage("Store Updated Successfully");

			StoreRabbitDto storeRabbitDto = new StoreRabbitDto(response.getId().intValue(), store.getStoreCode(),
					store.getStoreName(), store.getAddedBy().getUserId(), store.getAddedDate(),
					store.getUpdatedBy().getUserId(), store.getUpdatedDate(), store.getIsDeleted(), store.getIsActive(),
					store.getOrganization().getOrganizationId(), store.getUnit().getUnitId(),
					new StoreTypeRabbitDto(storeType.getId(), storeType.getCode(), storeType.getType()),
					new VisitTypeRabbitDto(visitTypes.get(i).getVisitTypeId(), visitTypes.get(i).getVisitTypeName(),
							visitTypes.get(i).getVisitTypeCode()));

			Request request = new Request();
			Payload payload = new Payload();
			payload.setActionType("update");
			payload.setData(storeRabbitDto);
			request.setKey(MasterKeyConstants.StoreMasterHISKey.toString());
			request.setPayload(payload);
			// EmrService.publish(request);

		}

		return response;
	}

}

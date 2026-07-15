package com.param.assetManagement.fixedAssetOpeningBalance.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.assetManagement.fixedAssetOpeningBalance.dao.IFixedAssetOpeningBalanceDao;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.AccessoriesDetails;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceDetailsResponse;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceDropdown;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceRequest;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceResponse;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.InsuranceDetails;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.ItemRequest;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.ItemResponse;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.PMSFrequencies;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.SpareDetails;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.WorkingConditions;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchAccessoriesMapper;
import com.param.entity.model.inventory.OpeningBalance;
import com.param.entity.model.inventory.OpeningBalanceAccessories;
import com.param.entity.model.inventory.OpeningBalanceDetail;
import com.param.entity.model.inventory.OpeningBalanceInsurance;
import com.param.entity.model.inventory.OpeningBalanceSpare;
import com.param.entity.model.inventory.PMSFrequency;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.inventory.WorkingCondition;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Company;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.service.global.IGlobalService;
import com.param.global.service.items.CustomException;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("FixedAssetOpeningBalanceService")
public class FixedAssetOpeningBalanceService implements IFixedAssetOpeningBalanceService {

	private Integer screenId = Screen.AddNewFixedAsset.getScreen();

	@Autowired
	IFixedAssetOpeningBalanceDao fixedAssetOpeningBalanceDao;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;
	
	@Autowired
	IItemsDao ItemsDao;

	

@Autowired
private SessionService sessionService;

	@Override
	@Transactional
	public FixedAssetOpeningBalanceDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId) {
		FixedAssetOpeningBalanceDropdown response = new FixedAssetOpeningBalanceDropdown();

		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(
					new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}

		for (PMSFrequency PMSFrequency : GlobalDao.getPMSFrequency(orgId)) {
			response.addPMSFrequency(
					new PMSFrequencies(PMSFrequency.getId(), PMSFrequency.getCode(), PMSFrequency.getDescription()));
		}

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (WorkingCondition workingCondition : GlobalDao.getWorkingConditions())
		{
			WorkingConditions workingConditions=new WorkingConditions(workingCondition.getId(), workingCondition.getCode(),
					workingCondition.getDescription());
			response.addWorkingConditions(workingConditions);
		}

		return response;
	}

	@Override
	@Transactional
	public Object getOpeningBalanceList(FixedAssetOpeningBalanceRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<FixedAssetOpeningBalanceResponse> response = new ArrayList<FixedAssetOpeningBalanceResponse>();

		for (OpeningBalance openingBalance : fixedAssetOpeningBalanceDao.searchOpeningBalance(request, screenId)) {
			FixedAssetOpeningBalanceResponse result = new FixedAssetOpeningBalanceResponse(openingBalance.getId(),
					openingBalance.getAddedDate(), openingBalance.getOpbNo(), openingBalance.getStore().getStoreName(),
					openingBalance.getAddedBy().getUserName(), openingBalance.getStatus().getId(),
					openingBalance.getStatus().getStatus());

			response.add(result);
		}

		return response;
	}

	@Override
	@Transactional
	public Object getOpeningBalance(Integer unitId, Integer orgId, Integer id) {
		OpeningBalance openingBalance = fixedAssetOpeningBalanceDao.getOpeningBalace(id);

		FixedAssetOpeningBalanceDetailsResponse response = new FixedAssetOpeningBalanceDetailsResponse(
				openingBalance.getId(), openingBalance.getAddedDate(), openingBalance.getOpbNo(),
				openingBalance.getStore().getStoreId(), openingBalance.getStore().getStoreName(),
				openingBalance.getAddedBy().getUserName(), openingBalance.getStatus().getStatus(),
				openingBalance.getStatus().getId());

		for (OpeningBalanceDetail detail : fixedAssetOpeningBalanceDao.getOpeningbalaceDetail(id)) {
			//detail.getAccessories()
			List<OpeningBalanceAccessories> accessoriesData=fixedAssetOpeningBalanceDao.getAccessoriesData(detail.getId());
			
			//start code for resolved HIS-9716 Fixed asset opening balance - Pending quantity column not visible	
			ItemResponse item = new ItemResponse(detail.getId(), detail.getItem().getId(), detail.getItem().getCode(),
					detail.getItem().getName(), detail.getSerialNumber(), detail.getYearOfManufacture(),
					(detail.getVendor() != null) ? detail.getVendor().getName() : null,
					(detail.getServiceProvider() != null) ? detail.getServiceProvider().getName() : null,
					detail.getWarrantyFrom(), detail.getwarrantyTo(),
					(detail.getPmsFrequency() != null) ? detail.getPmsFrequency().getId() : null,
					(detail.getPmsFrequency() != null) ? detail.getPmsFrequency().getDescription() : null,
					populateAccessories(accessoriesData,orgId,unitId), populateInsurance(detail.getInsurance()),
					detail.getRemark(),
					(detail.getWorkingCondition() != null) ? detail.getWorkingCondition().getId() : null,
					(detail.getWorkingCondition() != null) ? detail.getWorkingCondition().getDescription() : null,
					detail.getQuantity(), populateSpares(detail.getSpares(),orgId,unitId), 
					getItemPendingQuantity(openingBalance.getStore().getStoreId(),detail.getItem().getId(),orgId,unitId));
			item.setVendorId(detail.getVendor().getId());
			item.setServiceProviderId(detail.getServiceProvider().getId());
			item.setCop(detail.getCop()!=null ? detail.getCop() :0);
			item.setMrp(detail.getMrp()!=null ? detail.getMrp() :0);
			response.addItems(item);
			//end code for resolved HIS-9716 Fixed asset opening balance - Pending quantity column not visible
			
		}
		return response;
	}

	

	@Override
	@Transactional
	public Object saveOpeningBalance(FixedAssetOpeningBalanceRequest request) throws CustomException
	{
		/*Integer storeId = request.getStoreId();
		Store storeNew = GlobalDao.getStore(storeId);
		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/
		String grnNo = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId(), screenId);
		if(grnNo==null)
		{
			throw new CustomException("Please configure prefix number");	
		}
		OpeningBalance openingBalance = new OpeningBalance(grnNo, new Status(request.getStatusId()),
				new Store(request.getStoreId()));
		openingBalance.setObType("Fixed");
		
		for (ItemRequest item : request.getItems()) 
		{

			OpeningBalanceDetail detail = new OpeningBalanceDetail(new Item(item.getItemId()), item.getQuantity(),
					item.getSpecification(), new Status(item.getStatusId()), item.getSerialNumber(),
					item.getYearOfManufacture(), new Vendor(item.getVendorId()),
					new Vendor(item.getServiceProviderId()), item.getWarrantyFrom(), item.getWarrantyTo(),
					new PMSFrequency(item.getPmsFrequencyId()), new WorkingCondition(item.getWorkingConditionId()));
			detail.setCop(item.getCop()!=null ? item.getCop() :0);
			detail.setMrp(item.getMrp()!=null ? item.getMrp() :0);
			for (AccessoriesDetails accessory : item.getAccessories())
			{
				OpeningBalanceAccessories openingBalanceAccessories=new OpeningBalanceAccessories(new Item(accessory.getItemId()), accessory.getSerialNumber(),
						accessory.getWarrantyFrom(), accessory.getWarrantyTo(), accessory.getSpecification(),
						accessory.getQuantity(), new WorkingCondition(accessory.getWorkingConditionId()));
//				sessionService.setEntityDefaults(openingBalanceAccessories);
				openingBalanceAccessories.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				detail.addAccessories(openingBalanceAccessories);
			}
			
			for (SpareDetails spare : item.getSpares())
			{
				OpeningBalanceSpare openingBalanceSpare=new OpeningBalanceSpare(new Item(spare.getItemId()), spare.getSerialNumber(),
						spare.getWarrantyFrom(), spare.getWarrantyTo(), spare.getSpecification(),
						spare.getQuantity(), new WorkingCondition(spare.getWorkingConditionId()));
//				sessionService.setEntityDefaults(openingBalanceSpare);	
				openingBalanceSpare.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				detail.addSpare(openingBalanceSpare);
			}

			InsuranceDetails insurance = item.getInsurance();
			if(insurance.getInsuranceCompanyId() != null)
			{
				OpeningBalanceInsurance openingBalanceInsurance=	new OpeningBalanceInsurance(new Company(insurance.getInsuranceCompanyId()),
						insurance.getPolicyNumber(), insurance.getPolicyFromDate(), insurance.getPolicyToDate(),
						insurance.getAmountInsured(), insurance.getRemark());
//				sessionService.setEntityDefaults(openingBalanceInsurance);	
				openingBalanceInsurance.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				detail.addInsurance(openingBalanceInsurance);
			}
//			sessionService.setEntityDefaults(detail);
			detail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			openingBalance.addOpeningBalanceDetail(detail);
		}
//		sessionService.setEntityDefaults(openingBalance);
		openingBalance.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		fixedAssetOpeningBalanceDao.saveOpeningBalance(openingBalance);

		CommonResponse response = new CommonResponse();
		response.setId(openingBalance.getId().longValue());
		response.setMessage("Fixed Asset Opening Balance saved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelOpeningBalance(FixedAssetOpeningBalanceRequest request) {
		OpeningBalance openingBalance = fixedAssetOpeningBalanceDao.getOpeningBalace(request.getId());

		for (OpeningBalanceDetail detail : openingBalance.getOpeningBalanceDetailList()) {
			detail.setStatus(new Status(request.getStatusId()));
//			detail.setUpdatedDate(new Date());
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(openingBalance.getUnitId()));
//			detail.setUpdatedBy(openingBalance.getUser());
			for (OpeningBalanceAccessories accessory : detail.getAccessories()) {
				accessory.setIsActive(false);
				accessory.setIsDeleted(true);
			}
			for (OpeningBalanceInsurance insurance : detail.getInsurance()) {
				insurance.setIsActive(false);
				insurance.setIsDeleted(true);
			}
		}
		openingBalance.setCancelReason(new CancelReason(request.getCancelReasonId()));
		openingBalance.setCancelNote(request.getCancelNote());

		openingBalance.setStatus(new Status(request.getStatusId()));
		openingBalance.setUpdatedDate(TimeZoneComponent.getDateByzone(openingBalance.getUnitId()));
		//		openingBalance.setUpdatedBy(openingBalance.getUser());
		openingBalance.setUpdatedByUser(request.getUserId());
		fixedAssetOpeningBalanceDao.updateOpeningBalance(openingBalance);

		CommonResponse response = new CommonResponse();
		response.setMessage("Fixed Asset Opening Balance cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Object approveOpeningBalanceDetail(FixedAssetOpeningBalanceRequest request) {
		OpeningBalance openingBalance = fixedAssetOpeningBalanceDao.getOpeningBalace(request.getId());

		Integer storeId = openingBalance.getStore().getStoreId();

		for (OpeningBalanceDetail details : openingBalance.getOpeningBalanceDetailList()) {
			for (Integer id : request.getOpeningBalanceDetailId()) {
				if (details.getId().equals(id)
						&& !details.getStatus().getId().equals(com.param.common.constants.Status.Closed.getStatus())) {
					Batch batch = fixedAssetOpeningBalanceDao.saveBatch(InsertApprovedItems(details, storeId,request));

					details.setStatus(new Status(request.getStatusId()));
					details.setApprovedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
					User user=new User(request.getUserId());
					details.setApprovedBy(user);

					for (OpeningBalanceAccessories accessory : details.getAccessories()) {
						if (accessory.getIsActive() && !accessory.getIsDeleted()) {
							Batch batchAccessory = fixedAssetOpeningBalanceDao.saveBatch(InsertApprovedAccessories(accessory, storeId,request));
							BatchAccessoriesMapper batchAccessoriesMapper=new BatchAccessoriesMapper(
									new Batch(batch.getId()), new Batch(batchAccessory.getId()));
//							sessionService.setEntityDefaults(batchAccessoriesMapper);	
							batchAccessoriesMapper.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
									request.getUserId());
							fixedAssetOpeningBalanceDao.saveBatchAccessoriesMapper(batchAccessoriesMapper);
						}
					}
					break;
				}
			}

			if (!details.getStatus().getId().equals(com.param.common.constants.Status.Closed.getStatus())) {
				details.setStatus(new Status(request.getStatusId()));
			}

			details.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
			//			details.setUpdatedBy(details.getUser());
			details.setUpdatedByUser(request.getUserId());
		}

		openingBalance.setStatus(new Status(request.getStatusId()));
		openingBalance.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		//		openingBalance.setUpdatedBy(openingBalance.getUser());
		openingBalance.setUpdatedByUser(request.getUserId()!=null?request.getUserId():sessionService.getUserId());

		fixedAssetOpeningBalanceDao.updateOpeningBalance(openingBalance);

		CommonResponse response = new CommonResponse();
		response.setMessage("Fixed Asset Opening Balance approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object approveOpeningBalance(FixedAssetOpeningBalanceRequest request) {

		for (Integer opbId : request.getOpeningBalanceId()) {

			OpeningBalance openingBalance = fixedAssetOpeningBalanceDao.getOpeningBalace(opbId);

			Integer storeId = openingBalance.getStore().getStoreId();

			for (OpeningBalanceDetail details : openingBalance.getOpeningBalanceDetailList()) {
				if (details.getIsActive() && !details.getIsDeleted()) {
					Batch batch = fixedAssetOpeningBalanceDao.saveBatch(InsertApprovedItems(details, storeId,request));
					Integer updateBatchVendorId=fixedAssetOpeningBalanceDao.updateBatchVendorId(batch,details.getVendor().getId());
					details.setStatus(new Status(request.getStatusId()));
					details.setApprovedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
					User user=new User(request.getUserId());
					details.setApprovedBy(user);

					details.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
//					details.setUpdatedBy(details.getUser());
					details.setUpdatedByUser(request.getUserId());
					
					for (OpeningBalanceAccessories accessory : details.getAccessories()) {
						if (accessory.getIsActive() && !accessory.getIsDeleted()) {
							Batch batchAccessory = fixedAssetOpeningBalanceDao.saveBatch(InsertApprovedAccessories(accessory, storeId,request));
							BatchAccessoriesMapper batchAccessoriesMapper = new BatchAccessoriesMapper(
												new Batch(batch.getId()), new Batch(batchAccessory.getId()));
							batchAccessoriesMapper.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
						//	fixedAssetOpeningBalanceDao.saveBatchAccessoriesMapper(new BatchAccessoriesMapper(
						//			new Batch(batch.getId()), new Batch(batchAccessory.getId())));
							fixedAssetOpeningBalanceDao.saveBatchAccessoriesMapper(batchAccessoriesMapper);
						}
					}
				}
			}

			openingBalance.setStatus(new Status(request.getStatusId()));
			openingBalance.setApprovedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
			User user=new User(request.getUserId());
			openingBalance.setApprovedBy(user);

			openingBalance.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
//			openingBalance.setUpdatedBy(openingBalance.getUser());
			openingBalance.setUpdatedByUser(request.getUserId()!=null?request.getUserId():sessionService.getUserId());
			fixedAssetOpeningBalanceDao.updateOpeningBalance(openingBalance);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Fixed asset opening balance approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object updateOpeningBalance(FixedAssetOpeningBalanceRequest request) {
		OpeningBalance openingBalance = fixedAssetOpeningBalanceDao.getOpeningBalace(request.getId());

		for (OpeningBalanceDetail detail : openingBalance.getOpeningBalanceDetailList()) {
			Integer id = null;

			for (ItemRequest item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();

					detail.updateOpeningBalaceDetails(new Item(item.getItemId()), item.getQuantity(),
							item.getSpecification(), new Status(item.getStatusId()), item.getSerialNumber(),
							item.getYearOfManufacture(), null, null, item.getWarrantyFrom(), item.getWarrantyTo(),
							new PMSFrequency(item.getPmsFrequencyId()),
							new WorkingCondition(item.getWorkingConditionId()));
					detail.setCop(item.getCop()!=null ? item.getCop() :0);
					detail.setMrp(item.getMrp()!=null ? item.getMrp() :0);
					if (item.getVendorId() != null) {
						detail.setVendor(new Vendor(item.getVendorId()));
					}
					if (item.getServiceProviderId() != null) {
						detail.setServiceProvider(new Vendor(item.getServiceProviderId()));
					}
					if (detail.getAccessories() != null) {
						for (OpeningBalanceAccessories opbAccessory : detail.getAccessories()) {
							Integer accessoryId = null;
							if(item.getAccessories() != null) {
								for (AccessoriesDetails accessory : item.getAccessories()) {
									if (accessory.getId() != null && accessory.getId().equals(opbAccessory.getId())) {
										accessoryId = accessory.getId();
										opbAccessory.updateAccessories(new Item(accessory.getItemId()),
												accessory.getSerialNumber(), accessory.getWarrantyFrom(),
												accessory.getWarrantyTo(), accessory.getSpecification(),
												accessory.getQuantity(),
												new WorkingCondition(accessory.getWorkingConditionId()));
									}
	
								}
							}
							if (accessoryId == null) {
								opbAccessory.setIsDeleted(true);
							}

							opbAccessory.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
							opbAccessory.setUpdatedByUser(request.getUserId());
						}
					}

					if(item.getAccessories() != null) {
						for (AccessoriesDetails accessory : item.getAccessories()) {
							if (accessory.getId() == null) {

								OpeningBalanceAccessories openingBalanceAccessories = new OpeningBalanceAccessories(
										new Item(accessory.getItemId()), accessory.getSerialNumber(),
										accessory.getWarrantyFrom(), accessory.getWarrantyTo(),
										accessory.getSpecification(), accessory.getQuantity(),
										new WorkingCondition(accessory.getWorkingConditionId()));

								detail.addAccessories(openingBalanceAccessories);
							}
						}
					}
					
					for (OpeningBalanceInsurance opbInsurance : detail.getInsurance()) {
						Integer insuranceId = null;
						InsuranceDetails insurance = item.getInsurance();

						if (insurance.getId() != null && insurance.getId().equals(opbInsurance.getId())) {
							insuranceId = insurance.getId();
							opbInsurance.updateInsurance(new Company(insurance.getInsuranceCompanyId()),
									insurance.getPolicyNumber(), insurance.getPolicyFromDate(),
									insurance.getPolicyToDate(), insurance.getAmountInsured(), insurance.getRemark());
						}

						if (insuranceId == null) {
							opbInsurance.setIsDeleted(true);
						}

						opbInsurance.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
						opbInsurance.setUpdatedByUser(request.getUserId());
					}

					break;
				}
			}
			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
			detail.setUpdatedByUser(request.getUserId());
		}

		for (ItemRequest item : request.getItems()) {

			if (item.getId() == null) {
				OpeningBalanceDetail detail = new OpeningBalanceDetail(new Item(item.getItemId()), item.getQuantity(),
						item.getSpecification(), new Status(item.getStatusId()), item.getSerialNumber(),
						item.getYearOfManufacture(), new Vendor(item.getVendorId()),
						new Vendor(item.getServiceProviderId()), item.getWarrantyFrom(), item.getWarrantyTo(),
						new PMSFrequency(item.getPmsFrequencyId()), new WorkingCondition(item.getWorkingConditionId()));
				detail.setCop(item.getCop()!=null ? item.getCop() :0);
				detail.setMrp(item.getMrp()!=null ? item.getMrp() :0);
				if (item.getAccessories() != null) {
					for (AccessoriesDetails accessory : item.getAccessories()) {
						OpeningBalanceAccessories openingBalanceAccessories = new OpeningBalanceAccessories(
								new Item(accessory.getItemId()), accessory.getSerialNumber(),
								accessory.getWarrantyFrom(), accessory.getWarrantyTo(), accessory.getSpecification(),
								accessory.getQuantity(), new WorkingCondition(accessory.getWorkingConditionId()));
//						sessionService.setEntityDefaults(openingBalanceAccessories);
						openingBalanceAccessories.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
								request.getUserId());
						detail.addAccessories(openingBalanceAccessories);
					}
				}

				InsuranceDetails insurance = item.getInsurance();

				OpeningBalanceInsurance openingBalanceInsurance = new OpeningBalanceInsurance(
						new Company(insurance.getInsuranceCompanyId()), insurance.getPolicyNumber(),
						insurance.getPolicyFromDate(), insurance.getPolicyToDate(), insurance.getAmountInsured(),
						insurance.getRemark());
//				sessionService.setEntityDefaults(openingBalanceInsurance);
				openingBalanceInsurance.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				detail.addInsurance(openingBalanceInsurance);
//				sessionService.setEntityDefaults(detail);
				detail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				openingBalance.addOpeningBalanceDetail(detail);
			}
		}
//		sessionService.setEntityDefaults(openingBalance);
		openingBalance.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		fixedAssetOpeningBalanceDao.updateOpeningBalance(openingBalance);

		CommonResponse response = new CommonResponse();
		response.setId(openingBalance.getId().longValue());
		response.setMessage("Fixed Asset opening balance saved successfully");

		return response;
	}

	private List<AccessoriesDetails> populateAccessories(List<OpeningBalanceAccessories> accessories,Integer orgId, Integer unitId) {
		List<AccessoriesDetails> response = new ArrayList<AccessoriesDetails>();

		for (OpeningBalanceAccessories accessory : accessories) {

			
			/*response.add(new AccessoriesDetails(accessory.getId(), accessory.getItem().getId(),
					accessory.getItem().getCode(), accessory.getItem().getName(), accessory.getSerialNumber(),
					accessory.getWarrantyFrom(), accessory.getwarrantyTo(), accessory.getSpecification(),
					accessory.getQuantity(), accessory.getWorkingCondition().getId(),
					accessory.getWorkingCondition().getDescription()));*/
			
			//start code for resolved HIS-9716 Fixed asset opening balance - Pending quantity column not visible
			response.add(new AccessoriesDetails(accessory.getId(), accessory.getItem().getId(),
					accessory.getItem().getCode(), accessory.getItem().getName(), accessory.getSerialNumber(),
					accessory.getWarrantyFrom(), accessory.getwarrantyTo(), accessory.getSpecification(),
					accessory.getQuantity(), accessory.getWorkingCondition().getId(),
					accessory.getWorkingCondition().getDescription(),
					getItemPendingQuantity(accessory.getOpeningBalanceDetail().getOpeningBalance().getStore().getStoreId(),accessory.getItem().getId(),orgId,unitId)));
			//end code for resolved HIS-9716 Fixed asset opening balance - Pending quantity column not visible
		}
		return response;
	}
	private List<SpareDetails> populateSpares(List<OpeningBalanceSpare> spares,Integer orgId,Integer unitId) {

		List<SpareDetails> response = new ArrayList<SpareDetails>();

		for (OpeningBalanceSpare sp : spares) {
			SpareDetails d=new SpareDetails();
			d.setItemId(sp.getItem().getId());
			d.setSerialNumber(sp.getSerialNumber());
			d.setWarrantyFrom(sp.getWarrantyFrom());
			d.setWarrantyTo(sp.getwarrantyTo());
			d.setSpecification(sp.getSpecification());
			d.setQuantity(sp.getQuantity());
			d.setWorkingConditionId(sp.getWorkingCondition().getId());
			d.setItemCode(sp.getItem().getCode());
			d.setItemName(sp.getItem().getName());
			//start code for resolved HIS-9716 Fixed asset opening balance - Pending quantity column not visible
			d.setMaxOrderQuantity(getItemPendingQuantity(sp.getOpeningBalanceDetail().getOpeningBalance().getStore().getStoreId(),sp.getItem().getId(),orgId,unitId));
			//end code for resolved HIS-9716 Fixed asset opening balance - Pending quantity column not visible
			response.add(d);
		}
		return response;
	
	}
	private InsuranceDetails populateInsurance(List<OpeningBalanceInsurance> insurances) {
		InsuranceDetails response = new InsuranceDetails();

		for (OpeningBalanceInsurance insurance : insurances) {

			response = new InsuranceDetails(insurance.getId(), insurance.getInsuranceCompany().getCompanyDesc(),
					insurance.getInsuranceCompany().getCompanyId(), insurance.getPolicyNumber(),
					insurance.getPolicyFromDate(), insurance.getPolicyToDate(), insurance.getAmountInsured(),
					insurance.getRemark());
		}
		return response;
	}

	private Batch InsertApprovedItems(OpeningBalanceDetail detail, Integer storeId,FixedAssetOpeningBalanceRequest request) {

		Batch batch = new Batch(detail.getSerialNumber(), detail.getQuantity(), true,
				new Item(detail.getItem().getId()), detail.getQuantity(), false,
				new Vendor(detail.getServiceProvider().getId()), detail.getWarrantyFrom(), detail.getwarrantyTo(),
				new PMSFrequency(detail.getPmsFrequency().getId()), detail.getYearOfManufacture(),
				new WorkingCondition(detail.getWorkingCondition().getId()));
		batch.setLcop(detail.getLcop() != null ? detail.getLcop() : 0);
		batch.setCop(detail.getCop() != null ? detail.getCop() : 0);
		batch.setActualLendingPrice(detail.getLcop() != null ? detail.getLcop() : 0);
		batch.setRate(detail.getMrp() != null ? detail.getMrp() : 0);
		batch.setSellingPrice(detail.getMrp() != null ? detail.getMrp() : 0);
		//start code for HIS-9206 Fixed asset opening balance showing available quantity as 0 and consignment flag as blank
		batch.setIsConsignment(false);
		//end code for HIS-9206 Fixed asset opening balance showing available quantity as 0 and consignment flag as blank
		StoreStock storeStock = new StoreStock(detail.getQuantity(),
				new Store(detail.getOpeningBalance().getStore().getStoreId()), 0, new Batch(batch.getId()));
		storeStock.setTotalQty(detail.getQuantity());
		storeStock.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
		batch.addStoreStock(storeStock);

		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);

		StockTransaction stockTransaction = new StockTransaction(detail.getQuantity(), new com.param.entity.model.master.Screen(screen.getId()),
				screen.getCode(), detail.getQuantity(), new Store(storeId), new Batch(batch.getId()),detail.getId());
				stockTransaction.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
				stockTransaction.setInQty(detail.getQuantity());
		batch.addStockTransaction(stockTransaction);
		batch.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
		return batch;
	}

	private Batch InsertApprovedAccessories(OpeningBalanceAccessories accessory, Integer storeId,FixedAssetOpeningBalanceRequest request) {

		Batch batch = new Batch(accessory.getSerialNumber(), accessory.getQuantity(), true,
				new Item(accessory.getItem().getId()), accessory.getQuantity(), false,
				new Vendor(accessory.getOpeningBalanceDetail().getServiceProvider().getId()),
				accessory.getOpeningBalanceDetail().getWarrantyFrom(),
				accessory.getOpeningBalanceDetail().getwarrantyTo(),
				new PMSFrequency(accessory.getOpeningBalanceDetail().getPmsFrequency().getId()),
				accessory.getOpeningBalanceDetail().getYearOfManufacture(),
				new WorkingCondition(accessory.getOpeningBalanceDetail().getWorkingCondition().getId()));

		StoreStock storeStock = new StoreStock(accessory.getQuantity(),
				new Store(accessory.getOpeningBalanceDetail().getOpeningBalance().getStore().getStoreId()), 0,
				new Batch(batch.getId()));
		storeStock.setTotalQty(accessory.getQuantity());
		storeStock.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
		batch.addStoreStock(storeStock);

		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);

		StockTransaction stockTransaction = new StockTransaction(accessory.getQuantity(), new com.param.entity.model.master.Screen(screen.getId()),
				screen.getCode(), accessory.getQuantity(), new Store(storeId), new Batch(batch.getId()),accessory.getOpeningBalanceDetail().getId());
				stockTransaction.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
		batch.addStockTransaction(stockTransaction);
		batch.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
		return batch;
	}
	
	//start code for resolved HIS-9716 Fixed asset opening balance - Pending quantity column not visible
	private Long getItemPendingQuantity(Integer storeId, Integer itemId,Integer orgId,Integer unitId) {
		Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(storeId, itemId,orgId);
		Long storeStock = ItemsDao.getStockAvailable(storeId,itemId,unitId);
		Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(storeId, itemId);
		Long approvedPRAndPOQuantity = ItemsDao.getApprovedPRandPOQuantity(storeId, itemId,unitId);		
		Long approvedPOQuantity = ItemsDao.getApprovedPurchaseOrderQuantity(storeId, itemId,unitId);
		Long maxOrderQuantity = GlobalDao.getMaxOrderQuantity(maxStoreQuantity,storeStock,stockInTransit , approvedPRAndPOQuantity , approvedPOQuantity);
		return maxOrderQuantity;
	}
	//end code for resolved HIS-9716 Fixed asset opening balance - Pending quantity column not visible

}

package com.param.inventory.store.service.storeIndent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Priority;
import com.param.entity.model.inventory.IndentType;
import com.param.entity.model.inventory.StoreIndent;
import com.param.entity.model.inventory.StoreIndentDetail;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.dto.items.UomResponse;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.inventory.store.dao.storeIndent.IStoreIndentDao;
import com.param.inventory.store.dto.storeIndent.ItemRequest;
import com.param.inventory.store.dto.storeIndent.ItemResponse;
import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;
import com.param.inventory.store.dto.storeIndent.StoreIndentRequest;
import com.param.inventory.store.dto.storeIndent.StoreIndentResponse;
import com.param.pharmacy.master.global.dao.unitOfMeasurement.UnitOfMeasurementDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("StoreIndentService")
public class StoreIndentService implements IStoreIndentService {

	private Integer screenId = Screen.StoreIndent.getScreen();

	@Autowired
	private SessionService sessionService;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IStoreIndentDao StoreIndentDao;
	
	@Autowired
	IGlobalService GlobalService;
	
	@Autowired
	UnitOfMeasurementDao unitOfMeasurementDao;
	@Autowired
	IItemsDao ItemsDao;
	@Override
	@Transactional
	public StoreIndentDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		StoreIndentDropdownResponse response = new StoreIndentDropdownResponse();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addToStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName(),
					store.getStoreType().getCode().equals("MS")));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		for (IndentType indentType : GlobalDao.getIndentTypeList(orgId,unitId)) {
			response.addIndentType(
					new com.param.global.dto.global.IndentType(indentType.getId(), indentType.getType()));
		}

		for (Priority priority : GlobalDao.getPriorityList()) {
			response.addPriority(
					new com.param.global.dto.global.Priority(priority.getPriorityId(), priority.getPriorityDesc()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object getStoreIndentList(StoreIndentRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<StoreIndentResponse> response = new ArrayList<StoreIndentResponse>();

		for (StoreIndent storeIndent : StoreIndentDao.searchStoreIndentScreen(request, screenId)) {
			StoreIndentResponse result = new StoreIndentResponse(storeIndent.getId(), storeIndent.getAddedDate(),
					storeIndent.getIndentNo(), storeIndent.getFromStore().getStoreId(),
					storeIndent.getFromStore().getStoreName(), storeIndent.getToStore() != null ? storeIndent.getToStore().getStoreId() : null,
					storeIndent.getToStore() != null ? storeIndent.getToStore().getStoreName() : null, storeIndent.getPriority().getPriorityId(),
					storeIndent.getPriority().getPriorityDesc(), storeIndent.getIndentType().getType(),
					storeIndent.getIsConsignment(), storeIndent.getAddedBy().getUserName(), storeIndent.getRemark(),
					storeIndent.getStatus().getStatus(), storeIndent.getStatus().getId(),
					storeIndent.getApprovalStatus().getStatus(), storeIndent.getApprovalStatus().getId());
			result.setForceClose(storeIndent.getForceClose()!= null ? storeIndent.getForceClose()  : null);
					response.add(result);
		}
		return response;
	}

	@Override
	@Transactional
	public Object saveStoreIndent(StoreIndentRequest request) {
		if(request.getToStoreId() == null){
			throw new IllegalArgumentException("Issuing Store is required");
		}
		
		/*Integer storeId = request.getFromStoreId();
		//System.out.println("storeId:-"+storeId);
		Store storeNew = GlobalDao.getStore(storeId);
		//System.out.println("Store :-"+storeNew.getStoreId()+" "+storeNew.getStoreName());
		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
		//System.out.println("Unit :-"+unit.getUnitId()+" "+unit.getUnitName());
		//System.out.println("Screen Id  :-"+screenId);
*/		String indentNumber = GlobalService.getBillNoByGeneratedCode(request.getFromStoreId(), request.getUnitId(), screenId);
		//System.out.println("Function Called ");
		//System.out.println("Indent No : "+indentNumber);
		StoreIndent storeIndent = new StoreIndent(new Status(request.getApprovalStatusId()),
				new Store(request.getFromStoreId()), indentNumber, request.getIsConsignment(),
				new Priority(request.getPriorityId()), request.getRemark(), new Status(request.getStatusId()),
				new Store(request.getToStoreId()), new IndentType(request.getIndentTypeId()));
	//	sessionService.setEntityDefaults(storeIndent);	
		storeIndent.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		for (ItemRequest item : request.getItems()) {
			
			//Long storeStock = GlobalDao.getStoreStockByItem(storeIndent.getFromStore().getStoreId(),item.getItemId());
			Integer pendingQuantity = Util.GetPendingIndentQuantityLeaseQuantityStoreIndent(item.getQuantity(),unitOfMeasurementDao.getAllByItemId(item.getItemId(),request.getOrganizationId()), item.getUomTypeId());
			
			
			

			StoreIndentDetail storeIndentDetail=	new StoreIndentDetail(item.getQuantity(), new Item(item.getItemId()),
					pendingQuantity, new Status(item.getStatusId()), new UomType(item.getUomTypeId()),
					new UomUnit(item.getUomUnitId()));
		//	sessionService.setEntityDefaults(storeIndentDetail);	
			storeIndentDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			storeIndent.addStoreIndentDetailList(storeIndentDetail);
		}

		StoreIndentDao.saveStoreIndent(storeIndent);

		CommonResponse response = new CommonResponse();
		response.setId(storeIndent.getId().longValue());
		response.setMessage("Store indent saved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object updateStoreIndent(StoreIndentRequest request) {
		StoreIndent storeIndent = StoreIndentDao.getStoreIndent(request.getId());

		if(storeIndent.getToStore() == null){
			if(request.getToStoreId() == null) {
				throw new IllegalArgumentException("Issuing Store is required");
			} else{
				storeIndent.setToStore(new Store(request.getToStoreId()));
			}
		}

		storeIndent.updateStoreIndent(new Priority(request.getPriorityId()), request.getIsConsignment(),
				request.getRemark());

		for (StoreIndentDetail detail : storeIndent.getStoreIndentDetailList()) {
			Integer id = null;

			for (ItemRequest item : request.getItems())
			{
				if (item.getId() != null && item.getId().equals(detail.getId())) 
				{
					id = item.getId();
					//Integer pendingQuantity = Util.GetPendingIndentQuantityLeaseQuantityStoreIndent(item.getQuantity(),unitOfMeasurementDao.getAllByItemId(item.getItemId()), item.getUomTypeId());
					
					detail.updateStoreIndentDetail(new Item(item.getItemId()), item.getQuantity());
					detail.setUomType(new UomType(item.getUomTypeId()));
					detail.setUomUnit(new UomUnit(item.getUomUnitId()));
					break;
				}
			}

			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(storeIndent.getUnitId()));
		//	detail.setUpdatedBy(detail.getUser());
			detail.setUpdatedByUser(request.getUserId());
		}

		for (ItemRequest item : request.getItems()) 
		{
			//Long storeStock = GlobalDao.getStoreStockByItem(storeIndent.getFromStore().getStoreId(),item.getItemId());
		//Integer pendingQuantity = Util.GetPendingIndentQuantityLeaseQuantity(storeStock,unitOfMeasurementDao.getAllByItemId(item.getItemId()), item.getUomTypeId());

			Integer pendingQuantity = Util.GetPendingIndentQuantityLeaseQuantity(unitOfMeasurementDao.getAllByItemId(item.getItemId(),request.getOrganizationId()), item.getUomTypeId());

			if (item.getId() == null) {
				StoreIndentDetail storeIndentDetail=	new StoreIndentDetail(item.getQuantity(),
						new Item(item.getItemId()), pendingQuantity, new Status(item.getStatusId()),
						new UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId()));
			//	sessionService.setEntityDefaults(storeIndentDetail);	
				storeIndentDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
				storeIndent.addStoreIndentDetailList(storeIndentDetail);
			}
		}

		storeIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(storeIndent.getUnitId()));
	//	storeIndent.setUpdatedBy(storeIndent.getUser());
		storeIndent.setUpdatedByUser(request.getUserId());

		StoreIndentDao.updateStoreIndent(storeIndent);

		CommonResponse response = new CommonResponse();
		response.setMessage("Store indent updated successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getStoreIndent(Integer id,Integer orgId, Integer unitId) {
		StoreIndent storeIndent = StoreIndentDao.getStoreIndent(id);

		StoreIndentResponse response = new StoreIndentResponse(storeIndent.getId(), storeIndent.getAddedDate(),
				storeIndent.getIndentNo(), storeIndent.getFromStore().getStoreId(),
				storeIndent.getFromStore().getStoreName(), storeIndent.getToStore() != null ? storeIndent.getToStore().getStoreId() : null,
				storeIndent.getToStore() != null ? storeIndent.getToStore().getStoreName() : null, storeIndent.getPriority().getPriorityId(),
				storeIndent.getPriority().getPriorityDesc(), storeIndent.getIndentType().getType(),
				storeIndent.getIsConsignment(), storeIndent.getAddedBy().getUserName(), storeIndent.getRemark(),
				storeIndent.getStatus().getStatus(), storeIndent.getStatus().getId(),
				storeIndent.getApprovalStatus().getStatus(), storeIndent.getApprovalStatus().getId());
		List<UomResponse> uomListAll=GlobalDao.getUnitOfMeasurementList(orgId);
		List<UomResponse> storeStockListAll=GlobalDao.getStoreStockAll(storeIndent.getFromStore().getStoreId(),unitId);
		List<UomResponse> stockInTransitAll=GlobalDao.getstockInTransitAll(storeIndent.getFromStore().getStoreId(),unitId);
		List<UomResponse> lastMonSaleAll=GlobalDao.getLastMonthSaleNewAll(storeIndent.getFromStore().getStoreId(),unitId);
		List<UomResponse> currentMonSaleAll=GlobalDao.getCurrentMonthSaleAll(storeIndent.getFromStore().getStoreId(),unitId);
		List<UomResponse> maxStoreQuantityAll=GlobalDao.getStoreMaxQuantityAll(storeIndent.getFromStore().getStoreId(),orgId);
		List<UomResponse> approvedPRAndPOQuantityAll=ItemsDao.getApprovedPRandPOQuantityAll(storeIndent.getFromStore().getStoreId(),unitId);
		List<UomResponse> approvedPOQuantityAll=ItemsDao.getApprovedPurchaseOrderQuantityAll(storeIndent.getFromStore().getStoreId(),unitId);
		List<StoreIndentDetail> indentList=StoreIndentDao.getStoreIndentDetail(id);
		for (StoreIndentDetail detail : indentList) {
            Stream<UomResponse> uomListStream = uomListAll.stream();
            List<UomResponse> uomList=uomListStream
            .filter(str -> str.getItemId()!=null && str.getItemId().equals(detail.getItem().getId()))
            .collect(Collectors.toList());
            
            Stream<UomResponse> storeStockListAllStream = storeStockListAll.stream();
            List<UomResponse> storeStockList=storeStockListAllStream
            .filter(str -> str.getItemId()!=null && str.getItemId().equals(detail.getItem().getId()))
            .collect(Collectors.toList());
            Long storeStock = (long) 0;
            if(storeStockList!=null && storeStockList.size()>0)
            {
            	storeStock = storeStockList.getFirst().getStockAvailable();
            }
			Long availableStock = Util.ConvertQuantityByUom(storeStock, detail.getItem().getUnitOfMeasurementList(), detail.getUomType());
			
			Stream<UomResponse> stockInTransitStream = stockInTransitAll.stream();
            List<UomResponse> stockInTransitQty=stockInTransitStream
            .filter(str -> str.getItemId()!=null && str.getItemId().equals(detail.getItem().getId()) && str.getStoreId().equals(storeIndent.getFromStore().getStoreId()) && str.getUnitId().equals(unitId))
            .collect(Collectors.toList());
            Long stockInTransit = (long) 0;
            if(stockInTransitQty!=null && stockInTransitQty.size()>0 )
            {
            	stockInTransit = stockInTransitQty.getFirst().getStockInTransitQty();
            }
			Long tQuantity = Util.ConvertQuantityByUom(stockInTransit, detail.getItem().getUnitOfMeasurementList(), detail.getUomType());
			
			Stream<UomResponse> lastMonSaleStream = lastMonSaleAll.stream();
            List<UomResponse> lastMonSaleQty=lastMonSaleStream
            .filter(str -> str.getItemId()!=null && str.getItemId().equals(detail.getItem().getId()))
            .collect(Collectors.toList());
            Long lastMonSale = (long) 0;
            if(lastMonSaleQty!=null && lastMonSaleQty.size()>0)
            {
            	lastMonSale = lastMonSaleQty.getFirst().getLastMonthSale();
            }
			Long lastMonthSale = Util.ConvertQuantityByUom(lastMonSale, detail.getItem().getUnitOfMeasurementList(), detail.getUomType());
			
			Stream<UomResponse> currentMonSaleStream = currentMonSaleAll.stream();
            List<UomResponse> currentMonSaleQty=currentMonSaleStream
            .filter(str -> str.getItemId()!=null && str.getItemId().equals(detail.getItem().getId()))
            .collect(Collectors.toList());
            Long currentMonSale = (long) 0;
            if(currentMonSaleQty!=null && currentMonSaleQty.size()>0)
            {
            	currentMonSale = currentMonSaleQty.getFirst().getCurrentMonthSale();
            }
			Long currentMonthSale = Util.ConvertQuantityByUom(currentMonSale, detail.getItem().getUnitOfMeasurementList(), detail.getUomType());
			
			Stream<UomResponse> maxStoreQuantityStream = maxStoreQuantityAll.stream();
            List<UomResponse> maxStoreQuantityitemWise=maxStoreQuantityStream
            .filter(str -> str.getItemId()!=null && str.getItemId().equals(detail.getItem().getId()) && str.getStoreId().equals(storeIndent.getFromStore().getStoreId()) && str.getOrgId().equals(orgId))
            .collect(Collectors.toList());
            Integer maxStoreQuantity =  0;
            if(maxStoreQuantityitemWise!=null && maxStoreQuantityitemWise.size()>0)
            {
            	maxStoreQuantity = maxStoreQuantityitemWise.getFirst().getMaxStoreQty();
            }
			
			Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit);
			
			Stream<UomResponse> approvedPRAndPOQuantityStream = approvedPRAndPOQuantityAll.stream();
            List<UomResponse> approvedPRAndPOQuantityitemWise=approvedPRAndPOQuantityStream
            .filter(str -> str.getItemId()!=null && str.getItemId().equals(detail.getItem().getId()))
            .collect(Collectors.toList());
            Long approvedPRAndPOQuantity =  (long)0;
            if(approvedPRAndPOQuantityitemWise!=null && approvedPRAndPOQuantityitemWise.size()>0)
            {
            	approvedPRAndPOQuantity = approvedPRAndPOQuantityitemWise.getFirst().getApprovedPRAndPOQty();
            }
			// System.out.println("approvedPRAndPOQuantity:-" + approvedPRAndPOQuantity);

            Stream<UomResponse> approvedPOQuantityStream = approvedPOQuantityAll.stream();
            List<UomResponse> approvedPOQuantityitemWise=approvedPOQuantityStream
            .filter(str -> str.getItemId()!=null && str.getItemId().equals(detail.getItem().getId()))
            .collect(Collectors.toList());
            Long approvedPOQuantity =  (long)0;
            if(approvedPOQuantityitemWise!=null && approvedPOQuantityitemWise.size()>0)
            {
            	approvedPOQuantity = approvedPOQuantityitemWise.getFirst().getApprovedPOQty();
            }
			
			Long maxOrderQuantity1= GlobalDao.getMaxOrderQuantity(maxStoreQuantity, storeStock, stockInTransit, approvedPRAndPOQuantity, approvedPOQuantity);
			
			
			Long maxStoreQty = Util.ConvertQuantityByUom(maxOrderQuantity1, detail.getItem().getUnitOfMeasurementList(), detail.getUomType());

			response.addItems(new ItemResponse(detail.getId(), detail.getItem().getId(), detail.getUomType().getId(),
					detail.getUomUnit().getId(), detail.getIndentQuantity(), detail.getStatus().getId(),
					detail.getItem().getCode(), detail.getItem().getName(), detail.getItem().getGeneric().getName(),
					detail.getItem().getManufacturer().getName(), detail.getUomType().getType(),
					detail.getUomUnit().getUnits(), availableStock, tQuantity, lastMonthSale, currentMonthSale,
					maxStoreQty, uomList, detail.getItem().getIsThreeUOMApplicable()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object approveStoreIndent(StoreIndentRequest request) {

		for (Integer id : request.getIndentId()) {
			StoreIndent storeIndent = StoreIndentDao.getStoreIndent(id);

			if(storeIndent.getToStore() == null){
				throw new IllegalArgumentException("Issuing Store is required for indent : " + storeIndent.getIndentNo());
			}

			storeIndent.setStatus(new Status(request.getStatusId()));
			storeIndent.setApprovalStatus(new Status(request.getApprovalStatusId()));

			for (StoreIndentDetail detail : storeIndent.getStoreIndentDetailList()) {
				detail.setStatus(new Status(request.getApprovalStatusId()));

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(storeIndent.getUnitId()));
				//detail.setUpdatedBy(detail.getUser());
				detail.setUpdatedByUser(request.getUserId());
			}

			storeIndent.setApprovedDate(TimeZoneComponent.getDateByzone(storeIndent.getUnitId()));
			storeIndent.setApprovedBy(request.getUserId() != null ?  new User(request.getUserId()) : new User(sessionService.getUserId()));

			storeIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(storeIndent.getUnitId()));
		//	storeIndent.setUpdatedBy(storeIndent.getUser());
			storeIndent.setUpdatedByUser(request.getUserId());
			StoreIndentDao.updateStoreIndent(storeIndent);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Store indent Submitted successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelStoreIndent(StoreIndentRequest request) {

		StoreIndent storeIndent = StoreIndentDao.getStoreIndent(request.getId());

		for (StoreIndentDetail detail : storeIndent.getStoreIndentDetailList()) {
			detail.setStatus(new Status(request.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(storeIndent.getUnitId()));
		//	detail.setUpdatedBy(storeIndent.getUser());
			detail.setUpdatedByUser(request.getUserId());
		}

		storeIndent.setCancelReason(new CancelReason(request.getCancelReasonId()));
		storeIndent.setCancelNote(request.getCancelNote());

		storeIndent.setStatus(new Status(request.getStatusId()));
		storeIndent.setApprovalStatus(new Status(request.getApprovalStatusId()));

		storeIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(storeIndent.getUnitId()));
	//	storeIndent.setUpdatedBy(storeIndent.getUser());
		storeIndent.setUpdatedByUser(request.getUserId());

		StoreIndentDao.updateStoreIndent(storeIndent);

		CommonResponse response = new CommonResponse();
		response.setMessage("Store indent cancelled successfully");

		return response;
	}
}
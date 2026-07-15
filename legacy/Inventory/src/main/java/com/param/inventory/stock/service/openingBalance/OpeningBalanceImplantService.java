package com.param.inventory.stock.service.openingBalance;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.OpeningBalanceImplant;
import com.param.entity.model.inventory.OpeningBalanceImplantDetail;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.Organization;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.Unit;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.dto.global.TaxDropDown;
import com.param.global.dto.items.ItemListResponse;
import com.param.global.dto.items.ItemsRequest;
import com.param.global.service.global.IGlobalService;
import com.param.inventory.stock.dao.openingBalance.IOpeningBalanceImplantDao;
import com.param.inventory.stock.dto.openingBalance.ItemRequest;
import com.param.inventory.stock.dto.openingBalance.ItemResponse;
import com.param.inventory.stock.dto.openingBalance.OpeningBalanceDetailsResponse;
import com.param.inventory.stock.dto.openingBalance.OpeningBalanceRequest;
import com.param.inventory.stock.dto.openingBalance.OpeningBalanceResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;
@Service
public class OpeningBalanceImplantService  implements IOpeningBalanceImplantService {
	
	private Integer screenId = Screen.OpeningBalanceImplant.getScreen();
	
	@Autowired
	IOpeningBalanceImplantDao iOpeningBalanceImplantDao;

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
	public Object getOpeningBalanceImplantList(OpeningBalanceRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<OpeningBalanceResponse> response = new ArrayList<OpeningBalanceResponse>();

		for (OpeningBalanceImplant openingBalanceimplant : iOpeningBalanceImplantDao.searchOpeningBalanceImplant(request))
		{
			OpeningBalanceResponse result = new OpeningBalanceResponse(openingBalanceimplant.getId(),
					openingBalanceimplant.getAddedDate(), openingBalanceimplant.getOpbNo(), openingBalanceimplant.getStore().getStoreName(),
					openingBalanceimplant.getAddedBy().getUserName(), openingBalanceimplant.getStatus().getId(),
					openingBalanceimplant.getStatus().getStatus());

			response.add(result);
		}

		return response;
	}
	
	//For Popup Item List Get ITems List
	@Override
	@Transactional
	public List<ItemListResponse> implantItemsList(ItemsRequest itemRequest)
	{
			List<ItemListResponse> listResponse=iOpeningBalanceImplantDao.implantItemsList(itemRequest);
		    // List<ItemListResponse> listResponse = ItemsDao.geStoreWiseItemsList(itemRequest);
			for(ItemListResponse itemListResponse : listResponse)
			{
				itemListResponse.setItemId(itemListResponse.getItemid());
				itemListResponse.setAssetCategory(itemListResponse.getAssetcategory());
				itemListResponse.setItemCode(itemListResponse.getItemcode());
				itemListResponse.setItemName(itemListResponse.getItemname());
				itemListResponse.setProductCategory(itemListResponse.getProductcategory());
			}
			return listResponse;
	}
	
	@Override
	@Transactional
	public List<ItemListResponse> consignmentImplantItemsList(ItemsRequest itemRequest)
	{
			List<ItemListResponse> listResponse=iOpeningBalanceImplantDao.consignmentImplantItemsList(itemRequest);
			for(ItemListResponse itemListResponse : listResponse)
			{
				itemListResponse.setItemId(itemListResponse.getItemid());
				itemListResponse.setAssetCategory(itemListResponse.getAssetcategory());
				itemListResponse.setItemCode(itemListResponse.getItemcode());
				itemListResponse.setItemName(itemListResponse.getItemname());
				itemListResponse.setProductCategory(itemListResponse.getProductcategory());
			}
			return listResponse;
	}
	
	//For Save
	@Override
	@Transactional
	public Object saveOpeningBalanceImplant(OpeningBalanceRequest request)
	{
		/*Integer storeId = request.getStoreId();
		Store storeNew = GlobalDao.getStore(storeId);
		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
		//System.out.println("screenId:-"+screenId);
*/		String opbINumber = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId()!=null ? request.getUnitId(): sessionService.getUnitId(), screenId);
		OpeningBalanceImplant openingBalanceImplant = new OpeningBalanceImplant(opbINumber, request.getRemark(),new Status(request.getStatusId()),new Store(request.getStoreId()));
	//	sessionService.setEntityDefaults(openingBalanceImplant);	
		openingBalanceImplant.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		OpeningBalanceImplantDetail detail = null;
		for (ItemRequest item : request.getItems())
		{
			OpeningBalanceImplantDetail openingBalanceImplantDetail=	new OpeningBalanceImplantDetail(item.getCopAmount(), item.getBatchNo(),
					item.getCop(), item.getExpiryDate(), new Item(item.getItemId()),item.getMrp(),item.getQuantity(),
					item.getRemark(), new Status(item.getStatusId()),item.getTaxAmount(),
					new Tax(item.getTaxId()), item.getTaxPercent(),item.getMarkupPercent() != null ? item.getMarkupPercent() : null,
					new Manufacturer(item.getManufacturerId()), item.getLcop());
			openingBalanceImplantDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			detail = openingBalanceImplant.addOpeningBalanceImplantDetail(openingBalanceImplantDetail);
		}
		iOpeningBalanceImplantDao.saveOpeningBalanceImplant(openingBalanceImplant);
		CommonResponse response = new CommonResponse();
		response.setId(openingBalanceImplant.getId().longValue());
		response.setMessage("Opening balance Implant saved successfully");
		return response;
	}
	
	//For GetById
	@Override
	@Transactional
	public Object getOpeningBalanceImplant(Integer id)
	{
		OpeningBalanceImplant openingBalanceImplant = iOpeningBalanceImplantDao.getOpeningBalaceImplant(id);
		OpeningBalanceDetailsResponse response = new OpeningBalanceDetailsResponse(openingBalanceImplant.getId(),
				openingBalanceImplant.getAddedDate(), openingBalanceImplant.getOpbNo(),
				openingBalanceImplant.getStore().getStoreId(), openingBalanceImplant.getStore().getStoreName(),
				openingBalanceImplant.getAddedBy().getUserName(), openingBalanceImplant.getStatus().getStatus(),
				openingBalanceImplant.getStatus().getId());

		List<OpeningBalanceImplantDetail> details = iOpeningBalanceImplantDao.getOpeningbalaceImplantDetail(id);
		if (details != null && !details.isEmpty()) {
			for (OpeningBalanceImplantDetail detail : details) {
				response.addItems(new ItemResponse(detail.getId(), detail.getItem().getId(), detail.getItem().getCode(),
						detail.getItem().getName(), detail.getBatchNo(), detail.getExpiryDate(), detail.getQuantity(),
						detail.getCop(), detail.getMrp(), detail.getAmountCop(), detail.getTax().getId(),
						detail.getTax().getName(), detail.getTaxPercentage(), detail.getTaxAmount(),
						detail.getMarkupPercentage() != null ? detail.getMarkupPercentage() : null,
						detail.getManufacturer().getId(), detail.getRemark(), detail.getStatus().getId(),
						getTaxList(openingBalanceImplant.getOrganization().getOrganizationId()), detail.getLcop()));
			}
		}

		return response;
	}
	
	
	  private List<TaxDropDown> getTaxList(Integer orgId) 
	  { 
		  List<TaxDropDown> taxList = new
		  ArrayList<TaxDropDown>(); for (Tax tax : GlobalDao.getTaxList(orgId)) 
		  {
			  	  	TaxDropDown taxDto = new TaxDropDown(tax.getId(), tax.getName(),
				  tax.getTaxPercentage()); taxList.add(taxDto); 
		  }
		  return taxList; 
	}
	 
	//For Update
	@Override
	@Transactional
	public Object updateOpeningBalanceImplant(OpeningBalanceRequest request)
	{
		OpeningBalanceImplant openingBalanceImplant = iOpeningBalanceImplantDao.getOpeningBalaceImplant(request.getId());
		for (OpeningBalanceImplantDetail detail : openingBalanceImplant.getOpeningBalanceImplantDetailList()) 
		{
			Integer id = null;
			for (ItemRequest item : request.getItems())
			{
				if (item.getId() != null && item.getId().equals(detail.getOpeningBalanceImplant().getId()))
				{
					id = item.getId();
					detail.updateOpeningBalaceImplantDetails(new Item(item.getItemId()),item.getBatchNo(), item.getExpiryDate(),item.getQuantity(),
							item.getCop(), item.getMrp(), item.getCopAmount(),new Tax(item.getTaxId()),item.getTaxPercent(), item.getTaxAmount(),
							item.getMarkupPercent() != null ? item.getMarkupPercent() : null,
							item.getRemark(),new Status(item.getStatusId()),new Manufacturer(item.getManufacturerId()), item.getLcop());
					break;
				}
			}
			/*
			 * if (id == null) { detail.setIsDeleted(true); }
			 */
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
			//detail.setUpdatedBy(detail.getUser());
			detail.setUpdatedByUser(request.getUserId());
		}

		for (ItemRequest item : request.getItems())
		{
			if (item.getId() == null) 
			{
				OpeningBalanceImplantDetail detail = new OpeningBalanceImplantDetail(new Item(item.getItemId()),item.getBatchNo(), item.getExpiryDate(),item.getQuantity(),
						item.getCop(), item.getMrp(), item.getCopAmount(),new Tax(item.getTaxId()),	item.getTaxPercent(), item.getTaxAmount(),
						item.getMarkupPercent() != null ? item.getMarkupPercent() : null,
						item.getRemark(),new Status(item.getStatusId()),new Manufacturer(item.getManufacturerId()), item.getLcop());
			//	sessionService.setEntityDefaults(detail);	
			//	detail.setAddedBy(new User(request.getUserId()));
			//	detail.setUpdatedBy(new User(request.getUserId()));
			/*	detail.setAddedByUser(request.getUserId());
				detail.setUpdatedByUser(request.getUserId());
				detail.setAddedDate(TimeZoneComponent.getDateByzone(unitId));
				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
				detail.setOrganization(new Organization(request.getOrganizationId()));
				detail.setUnit(new Unit(request.getUnitId()));*/
				detail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
				
				openingBalanceImplant.addOpeningBalanceImplantDetail(detail);
			}
		}
		
		openingBalanceImplant.setStatus(new Status(request.getStatusId()));
		openingBalanceImplant.setUpdateDefaults(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId(), openingBalanceImplant.getUnitId());
		iOpeningBalanceImplantDao.updateOpeningBalanceImplant(openingBalanceImplant);
		CommonResponse response = new CommonResponse();
		response.setId(openingBalanceImplant.getId().longValue());
		response.setMessage("Opening balance Implant updated successfully");

		return response;
	}
	
	//for Approve
	@Override
	@Transactional
	public Object approveOpeningBalanceImplant(OpeningBalanceRequest request) {

		for (Integer opbId : request.getOpeningBalanceImplantId()) {

			OpeningBalanceImplant openingBalanceImplant = iOpeningBalanceImplantDao.getOpeningBalaceImplant(opbId);

			Integer storeId = openingBalanceImplant.getStore().getStoreId();

			for (OpeningBalanceImplantDetail details : openingBalanceImplant.getOpeningBalanceImplantDetailList())
			{
				if (details.getIsActive() && !details.getIsDeleted()) {
					iOpeningBalanceImplantDao.saveBatch(InsertApprovedItems(details, storeId, request));

					details.setStatus(new Status(request.getStatusId()));
					details.setApprovedDate(TimeZoneComponent.getDateByzone(openingBalanceImplant.getUnitId()));
				//	details.setApprovedBy(details.getUser());
					details.setApprovedBy(new User(request.getUserId()!=null ?request.getUserId() : sessionService.getUserId() ));
					details.setApprovedQuantity(details.getQuantity());

					details.setUpdatedDate(TimeZoneComponent.getDateByzone(openingBalanceImplant.getUnitId()));
					//details.setUpdatedBy(details.getUser());
					details.setUpdatedByUser(request.getUserId()!=null ?request.getUserId() : sessionService.getUserId());
				}
			}
			openingBalanceImplant.setStatus(new Status(request.getStatusId()));
			openingBalanceImplant.setApprovedDate(TimeZoneComponent.getDateByzone(openingBalanceImplant.getUnitId()));
		//	openingBalanceImplant.setApprovedBy(openingBalanceImplant.getUser());
			openingBalanceImplant.setApprovedBy(new User(request.getUserId()!=null ?request.getUserId() : sessionService.getUserId()));
			
		//	openingBalanceImplant.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
		//	openingBalanceImplant.setUpdatedBy(openingBalanceImplant.getUser());
			openingBalanceImplant.setUpdateDefaults(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId(),openingBalanceImplant.getUnitId());
			iOpeningBalanceImplantDao.updateOpeningBalanceImplant(openingBalanceImplant);
		}
		CommonResponse response = new CommonResponse();
		response.setMessage("Opening balance implant approved successfully");

		return response;
	}
	
	//for Approve Detail
	 /* @Override
	  @Transactional public Object  approveOpeningBalanceImplantDetail(OpeningBalanceRequest request)
	  {
		  		OpeningBalanceImplant openingBalanceImplant =iOpeningBalanceImplantDao.getOpeningBalaceImplant(request.getId());
		  		Integer storeId = openingBalanceImplant.getStore().getStoreId();
		  		for(OpeningBalanceImplantDetail details :openingBalanceImplant.getOpeningBalanceImplantDetailList())
		  		{
		  			for(Integer id: request.getOpeningBalanceDetailId())
		  			{
		  				if(details.getId().equals(id) && !details.getStatus().getId().equals(com.param.common.constants.Status.Closed.getStatus())) 
		  				{
		  						iOpeningBalanceImplantDao.saveBatch(InsertApprovedItems(details, storeId));
		  						details.setStatus(new Status(request.getStatusId()));
		  						details.setApprovedDate(TimeZoneComponent.getDateByzone(unitId));
		  						details.setApprovedBy(details.getUser());
		  						details.setApprovedQuantity(details.getQuantity());
		  						break; 
		  				}
		  			}
		  			if(!details.getStatus().getId().equals(com.param.common.constants.Status.Closed.getStatus()))
		  			{
		  				details.setStatus(new Status(request.getStatusId()));
		  			}
		  			details.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId)); details.setUpdatedBy(details.getUser());
		  		}
		  		openingBalanceImplant.setStatus(new Status(request.getStatusId()));
		  		openingBalanceImplant.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
		  		openingBalanceImplant.setUpdatedBy(openingBalanceImplant.getUser());
		  		iOpeningBalanceImplantDao.updateOpeningBalanceImplant(openingBalanceImplant);
		  		CommonResponse response = new CommonResponse();
		  		response.setMessage("Opening balance implant ById approved  successfully");
		  		return response; 
	  }*/
	
	//For Cancel
	@Override
	@Transactional
	public Object cancelOpeningBalanceImplant(OpeningBalanceRequest request)
	{
		OpeningBalanceImplant openingBalanceImplant = iOpeningBalanceImplantDao.getOpeningBalaceImplant(request.getId());

		for (OpeningBalanceImplantDetail detail : openingBalanceImplant.getOpeningBalanceImplantDetailList())
		{
			detail.setStatus(new Status(request.getStatusId()));
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(openingBalanceImplant.getUnitId()));
		//	detail.setUpdatedBy(openingBalanceImplant.getUser());
			detail.setUpdatedByUser(request.getUserId()!=null ?request.getUserId() : sessionService.getUserId());
		}
		openingBalanceImplant.setCancelReason(new CancelReason(request.getCancelReasonId()));
		openingBalanceImplant.setCancelNote(request.getCancelNote());

		openingBalanceImplant.setStatus(new Status(request.getStatusId()));
		//openingBalanceImplant.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
		//openingBalanceImplant.setUpdatedBy(openingBalanceImplant.getUser());
		openingBalanceImplant.setUpdateDefaults(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId(),openingBalanceImplant.getUnitId());
		iOpeningBalanceImplantDao.updateOpeningBalanceImplant(openingBalanceImplant);

		CommonResponse response = new CommonResponse();
		response.setMessage("Opening balance implant cancelled successfully");

		return response;
	}
	
	private Batch InsertApprovedItems(OpeningBalanceImplantDetail detail, Integer storeId, OpeningBalanceRequest request) 
	{

		Batch batch = new Batch(detail.getMrp(), detail.getBatchNo(),detail.getQuantity(), detail.getCop(),detail.getExpiryDate(),new Item(detail.getItem().getId()),
								detail.getMarkupPercentage(),detail.getQuantity(),detail.getMrp(), detail.getTaxAmount(),new Tax(detail.getTax().getId()),detail.getTaxPercentage(),true);
	//	sessionService.setEntityDefaults(batch);
		
	/*	batch.setAddedBy(new User(request.getUserId()));
		batch.setUpdatedBy(new User(request.getUserId()));*/
		batch.setAddedByUser(request.getUserId());
		batch.setUpdatedByUser(request.getUserId());
		batch.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		batch.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		batch.setOrganization(new Organization(request.getOrganizationId()));
		batch.setUnit(new Unit(request.getUnitId()));
		StoreStock storeStock=	new StoreStock(detail.getQuantity(), new Store(detail.getOpeningBalanceImplant().getStore().getStoreId()),0, new Batch(batch.getId()));
		//sessionService.setEntityDefaults(storeStock);
		/*batch.setAddedBy(new User(request.getUserId()));
		batch.setUpdatedBy(new User(request.getUserId()));*/
		
		storeStock.setAddedByUser(request.getUserId());
		storeStock.setUpdatedByUser(request.getUserId());
		storeStock.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		storeStock.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		storeStock.setOrganizationId(request.getOrganizationId());
		storeStock.setUnitId(request.getUnitId());
		storeStock.setTotalQty(detail.getQuantity());
		batch.setAddedByUser(request.getUserId());
		batch.setUpdatedByUser(request.getUserId());
		batch.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		batch.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		batch.setOrganizationId(request.getOrganizationId());
		batch.setUnitId(request.getUnitId());
		batch.addStoreStock(storeStock);

		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);

		StockTransaction stockTransaction=	new StockTransaction(detail.getCop(), detail.getQuantity(),
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), detail.getMrp(),
				detail.getQuantity(), detail.getMrp(), new Store(storeId), new Batch(batch.getId()),detail.getId());
		//sessionService.setEntityDefaults(stockTransaction);	
		/*stockTransaction.setAddedBy(new User(request.getUserId()));
		stockTransaction.setUpdatedBy(new User(request.getUserId()));*/
		stockTransaction.setAddedByUser(request.getUserId());
		stockTransaction.setUpdatedByUser(request.getUserId());
		stockTransaction.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		stockTransaction.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		stockTransaction.setOrganizationId(request.getOrganizationId());
		stockTransaction.setUnitId(request.getUnitId());
		batch.addStockTransaction(stockTransaction);
		return batch;
	}
	


}

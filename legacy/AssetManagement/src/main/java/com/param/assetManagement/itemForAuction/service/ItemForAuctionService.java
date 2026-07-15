package com.param.assetManagement.itemForAuction.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceDropdown;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceRequest;
import com.param.assetManagement.itemForAuction.dao.IItemForAuctionDao;
import com.param.assetManagement.itemForAuction.dto.ItemAuctionAccessories;
import com.param.assetManagement.itemForAuction.dto.ItemAuctionDetails;
import com.param.assetManagement.itemForAuction.dto.ItemAuctionResponse;
import com.param.assetManagement.itemForAuction.dto.ItemForAuctionDetailRequest;
import com.param.assetManagement.itemForAuction.dto.ItemForAuctionRequest;
import com.param.assetManagement.itemForAuction.dto.VendorDetails;
import com.param.assetManagement.itemForAuction.dto.VendorDetailsResponse;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.asset.AuctionAccessoriesMapper;
import com.param.entity.model.asset.AuctionRequisitionDetail;
import com.param.entity.model.asset.AuctionVendorApproval;
import com.param.entity.model.asset.AuctionVendorApprovalDetail;
import com.param.entity.model.asset.ItemAuction;
import com.param.entity.model.asset.ItemAuctionDetail;
import com.param.entity.model.asset.VendorApprovalAuctionMapper;
import com.param.entity.model.asset.VendorAuctionMapper;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;
import com.param.global.dao.global.IGlobalDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("ItemForAuctionService")
public class ItemForAuctionService implements IItemForAuctionService {

	private Integer screenId = Screen.ItemAuction.getScreen();

	@Autowired
	IItemForAuctionDao itemForAuctionDao;

	@Autowired
	IGlobalDao GlobalDao;
	

@Autowired
private SessionService sessionService;


	@Override
	@Transactional
	public FixedAssetOpeningBalanceDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId)
	{
		FixedAssetOpeningBalanceDropdown response = new FixedAssetOpeningBalanceDropdown();

		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}

		for(Store store : GlobalDao.getUserStoreList(unitId,orgId,userId))
		{
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}
		
		return response;
	}
	
	@Override
	@Transactional
	public Object getItemAuctionList(FixedAssetOpeningBalanceRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<ItemAuctionResponse> response = new ArrayList<ItemAuctionResponse>();
		for (ItemAuction itemAuction : itemForAuctionDao.searchItemAuction(request, screenId)) {
			ItemAuctionResponse item = new ItemAuctionResponse(itemAuction.getId(), itemAuction.getAddedDate(), itemAuction.getRequestNo(), itemAuction.getAuctionRequision().getAuctionRequisitionNo(), itemAuction.getStore().getStoreId(),
					itemAuction.getStore().getStoreName(), itemAuction.getAddedBy().getUserName(), itemAuction.getStatus().getId(), itemAuction.getStatus().getStatus());
			response.add(item);
		}

		return response;
	}
	
	@Override
	@Transactional
	public Object getItemAuctionById(Integer id)
	{
		ItemAuction itemAuction = itemForAuctionDao.getItemAuctionById(id);

		ItemAuctionResponse response = new ItemAuctionResponse(itemAuction.getId(), itemAuction.getAddedDate(), itemAuction.getRequestNo(), itemAuction.getAuctionRequision().getAuctionRequisitionNo(), itemAuction.getStore().getStoreId(),
				itemAuction.getStore().getStoreName(), itemAuction.getAddedBy().getUserName(), itemAuction.getStatus().getId(), itemAuction.getStatus().getStatus());
	
		List<ItemAuctionDetails> items = new ArrayList<ItemAuctionDetails>();
		for (ItemAuctionDetails detail : itemForAuctionDao.getItemAuctionDetail(id))
		{
			AuctionRequisitionDetail auctionRequisitionDetail = itemForAuctionDao.getAuctionRequisitionDetail(detail.getAuctionRequisitionDetailId());
			
			ItemAuctionDetail itemAuctionDetail = itemForAuctionDao.getItemAuctionDetailById(detail.getId());
			List<VendorDetailsResponse> vendorDetails = new ArrayList<VendorDetailsResponse>();
			for(VendorAuctionMapper vendorAuctionMapper : itemAuctionDetail.getVendorAuctionMapper())
			{
				VendorDetailsResponse vendorDetail = new VendorDetailsResponse(vendorAuctionMapper.getId(), vendorAuctionMapper.getItemAuctionDetail().getAuctionRequisitionDetail().getAuctionQuantity(),
						vendorAuctionMapper.getItemAuctionDetail().getAuctionRequisitionDetail().getApprovedQuantity(), vendorAuctionMapper.getVendor().getId(), vendorAuctionMapper.getVendor().getName(),
						vendorAuctionMapper.getVendorAmount(), vendorAuctionMapper.getPaymentTerms(), vendorAuctionMapper.getReferenceNumber(),
						vendorAuctionMapper.getReferenceDate());
				vendorDetails.add(vendorDetail);
			}
			List<ItemAuctionAccessories> accessories = new ArrayList<ItemAuctionAccessories>();
			for(AuctionAccessoriesMapper accessoryDetail : auctionRequisitionDetail.getAuctionAccessoriesMapper())
			{
				ItemAuctionAccessories accessory = new ItemAuctionAccessories(accessoryDetail.getId(), accessoryDetail.getBatch().getItem().getId(),
						accessoryDetail.getBatch().getItem().getCode(), accessoryDetail.getBatch().getItem().getName(),
						accessoryDetail.getBatch().getBatchNo(), accessoryDetail.getBatch().getYearOfManufacture());
				
				accessories.add(accessory);
			}
			detail.setVendorDetails(vendorDetails);
			detail.setAccessories(accessories);
			items.add(detail);
		}

		response.setItems(items);
		return response;
	}
	
	@Override
	@Transactional
	public Object saveItemAuction(ItemForAuctionRequest request)
	{
		
		ItemAuction itemAuction = itemForAuctionDao.getItemAuctionById(request.getId());

		for (ItemForAuctionDetailRequest item : request.getItems()) {
			
			ItemAuctionDetail itemAuctionDetail = itemForAuctionDao.getItemAuctionDetailById(item.getId());
			for(VendorDetails vendorDetails : item.getVendorDetails())
			{
				if(vendorDetails.getId() != null)
				{
					VendorAuctionMapper vendorAuctionMapper = itemForAuctionDao.getVendorAuctionMapper(vendorDetails.getId());
					vendorAuctionMapper.updateVendorAuctionMapper((vendorDetails.getVendorId() != null) ? new Vendor(vendorDetails.getVendorId()) : null, vendorDetails.getVendorAmount(), 
							vendorDetails.getPaymentTerms(), vendorDetails.getReferenceNumber(), vendorDetails.getReferenceDate());
				}
				else
				{
					VendorAuctionMapper vendorAuctionMapper=new VendorAuctionMapper((vendorDetails.getVendorId() != null) ? new Vendor(vendorDetails.getVendorId()) : null, vendorDetails.getVendorAmount(), 
							vendorDetails.getPaymentTerms(), vendorDetails.getReferenceNumber(), vendorDetails.getReferenceDate());
//					sessionService.setEntityDefaults(vendorAuctionMapper);	
					vendorAuctionMapper.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
							request.getUserId());
					itemAuctionDetail.addVendorAuctionMapper(vendorAuctionMapper);
				}
			}
			itemAuctionDetail.setStatus(new Status(item.getStatusId()));
			itemAuctionDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
			itemAuctionDetail.setUpdatedByUser(request.getUserId());
		}
		itemAuction.setStatus(new Status(request.getStatusId()));
		itemAuction.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		itemAuction.setUpdatedByUser(request.getUserId());
		itemForAuctionDao.saveItemAuction(itemAuction);

		CommonResponse response = new CommonResponse();
		response.setId(itemAuction.getId().longValue());
		response.setMessage("Item for auction saved successfully");

		return response;
	}
	
	@Override
	@Transactional
	public Object approveItemAuction(ItemForAuctionRequest request)
	{
		for(Integer id : request.getIds())
		{
			ItemAuction itemAuction = itemForAuctionDao.getItemAuctionById(id);

			AuctionVendorApproval auctionVendorApproval = itemAuction.addAuctionVendorApproval(new AuctionVendorApproval(new ItemAuction(itemAuction.getId()),
					new Status(com.param.common.constants.Status.Pending.getStatus())));
			auctionVendorApproval.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
			for (ItemAuctionDetail detail : itemAuction.getItemAuctionDetail()) {
				detail.setStatus(new Status(request.getStatusId()));
				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
				detail.setUpdatedByUser(request.getUserId());
				
				AuctionVendorApprovalDetail auctionVendorApprovalDetail = auctionVendorApproval.addAuctionVendorApprovalDetail(new AuctionVendorApprovalDetail(new ItemAuctionDetail(detail.getId())));
//				sessionService.setEntityDefaults(auctionVendorApprovalDetail);
				auctionVendorApprovalDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				for(VendorAuctionMapper vendorAuctionMapper : detail.getVendorAuctionMapper())
				{
					VendorApprovalAuctionMapper vendorApprovalAuctionMapper=	new VendorApprovalAuctionMapper(new VendorAuctionMapper(vendorAuctionMapper.getId()));
//					sessionService.setEntityDefaults(vendorApprovalAuctionMapper);	
					vendorApprovalAuctionMapper.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
							request.getUserId());
					auctionVendorApprovalDetail.addVendorApprovalAuctionMapper(vendorApprovalAuctionMapper);
				}
			}
			itemAuction.setStatus(new Status(request.getStatusId()));
			itemAuction.setApprovedBy(new User(request.getUserId()));
			itemAuction.setAppprovedOn(TimeZoneComponent.getDateByzone(request.getUnitId()));
			itemAuction.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
			itemAuction.setUpdatedByUser(request.getUserId());
			itemAuction.setOrganizationId(request.getOrganizationId());
			itemAuction.setUnitId(request.getUnitId());
			itemForAuctionDao.saveItemAuction(itemAuction);
		}
		
		CommonResponse response = new CommonResponse();
		response.setMessage("Item for auction Approved successfully");

		return response;
	}

}

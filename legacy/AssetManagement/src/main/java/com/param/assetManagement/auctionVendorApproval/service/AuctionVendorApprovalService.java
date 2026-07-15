package com.param.assetManagement.auctionVendorApproval.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.assetManagement.auctionVendorApproval.dao.IAuctionVendorApprovalDao;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceDropdown;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceRequest;
import com.param.assetManagement.itemForAuction.dto.ItemAuctionDetails;
import com.param.assetManagement.itemForAuction.dto.ItemAuctionResponse;
import com.param.assetManagement.itemForAuction.dto.ItemForAuctionDetailRequest;
import com.param.assetManagement.itemForAuction.dto.ItemForAuctionRequest;
import com.param.assetManagement.itemForAuction.dto.VendorDetails;
import com.param.assetManagement.itemForAuction.dto.VendorDetailsResponse;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.asset.AuctionVendorApproval;
import com.param.entity.model.asset.AuctionVendorApprovalDetail;
import com.param.entity.model.asset.VendorApprovalAuctionMapper;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Store;
import com.param.global.dao.global.IGlobalDao;
import com.param.utility.TimeZoneComponent;

@Service("AuctionVendorApprovalService")
public class AuctionVendorApprovalService implements IAuctionVendorApprovalService {

	private Integer screenId = Screen.AuctionVendorApproval.getScreen();

	@Autowired
	IAuctionVendorApprovalDao auctionVendorApprovalDao;

	@Autowired
	IGlobalDao GlobalDao;

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
	public Object getAuctionVendorApprovalList(FixedAssetOpeningBalanceRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<ItemAuctionResponse> response = new ArrayList<ItemAuctionResponse>();
		for (AuctionVendorApproval auctionVendorApproval : auctionVendorApprovalDao.searchAuctionVendorApproval(request, screenId)) {
			ItemAuctionResponse item = new ItemAuctionResponse(auctionVendorApproval.getId(), auctionVendorApproval.getAddedDate(), auctionVendorApproval.getItemAuction().getRequestNo(), 
					auctionVendorApproval.getItemAuction().getAuctionRequision().getAuctionRequisitionNo(), auctionVendorApproval.getItemAuction().getStore().getStoreId(),
					auctionVendorApproval.getItemAuction().getStore().getStoreName(), auctionVendorApproval.getItemAuction().getAddedBy().getUserName(), auctionVendorApproval.getStatus().getId(), auctionVendorApproval.getStatus().getStatus());
			response.add(item);
		}

		return response;
	}
	
	@Override
	@Transactional
	public Object getAuctionVendorApprovalById(Integer id)
	{
		AuctionVendorApproval auctionVendorApproval = auctionVendorApprovalDao.getAuctionVendorApprovalById(id);

		ItemAuctionResponse response = new ItemAuctionResponse(auctionVendorApproval.getId(), auctionVendorApproval.getAddedDate(), auctionVendorApproval.getItemAuction().getRequestNo(), 
				auctionVendorApproval.getItemAuction().getAuctionRequision().getAuctionRequisitionNo(), auctionVendorApproval.getItemAuction().getStore().getStoreId(),
				auctionVendorApproval.getItemAuction().getStore().getStoreName(), auctionVendorApproval.getItemAuction().getAddedBy().getUserName(), auctionVendorApproval.getStatus().getId(), 
				auctionVendorApproval.getStatus().getStatus());
	
		List<ItemAuctionDetails> items = new ArrayList<ItemAuctionDetails>();
		for (ItemAuctionDetails detail : auctionVendorApprovalDao.getAuctionVendorApprovalDetail(id))
		{
			AuctionVendorApprovalDetail auctionVendorApprovalDetail = auctionVendorApprovalDao.getAuctionAuctionVendorApprovalDetail(detail.getId());
			List<VendorDetailsResponse> vendorDetails = new ArrayList<VendorDetailsResponse>();
			for(VendorApprovalAuctionMapper vendorApprovalAuctionMapper : auctionVendorApprovalDetail.getVendorApprovalAuctionMapper())
			{
				VendorDetailsResponse vendorDetail = new VendorDetailsResponse(vendorApprovalAuctionMapper.getVendorAuctionMapper().getId(), auctionVendorApprovalDetail.getItemAuctionDetail().getAuctionRequisitionDetail().getAuctionQuantity(),
						vendorApprovalAuctionMapper.getApprovedQuantity(), vendorApprovalAuctionMapper.getVendorAuctionMapper().getVendor().getId(),
						vendorApprovalAuctionMapper.getVendorAuctionMapper().getVendor().getName(), vendorApprovalAuctionMapper.getVendorAuctionMapper().getVendorAmount(),
						vendorApprovalAuctionMapper.getVendorAuctionMapper().getPaymentTerms(), vendorApprovalAuctionMapper.getVendorAuctionMapper().getReferenceNumber(),
						vendorApprovalAuctionMapper.getVendorAuctionMapper().getReferenceDate());
				
				vendorDetails.add(vendorDetail);
			}
			detail.setVendorDetails(vendorDetails);
			items.add(detail);
		}

		response.setItems(items);
		return response;
	}
	
	@Override
	@Transactional
	public Object saveAuctionVendorApproval(ItemForAuctionRequest request)
	{
		
		AuctionVendorApproval auctionVendorApproval = auctionVendorApprovalDao.getAuctionVendorApprovalById(request.getId());
		for(AuctionVendorApprovalDetail auctionVendorApprovalDetails : auctionVendorApproval.getAuctionVendorApprovalDetail())
		{
			for (ItemForAuctionDetailRequest item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(auctionVendorApprovalDetails.getId())) {
					
					AuctionVendorApprovalDetail auctionVendorApprovalDetail = auctionVendorApprovalDao.getAuctionAuctionVendorApprovalDetail(item.getId());
					for(VendorApprovalAuctionMapper vendorApprovalAuctionMappers : auctionVendorApprovalDetail.getVendorApprovalAuctionMapper())
					{
						for(VendorDetails vendorDetails : item.getVendorDetails())
						{
							if (vendorDetails.getId() != null && vendorDetails.getId().equals(vendorApprovalAuctionMappers.getId())) {
								
								VendorApprovalAuctionMapper vendorApprovalAuctionMapper = auctionVendorApprovalDao.getVendorApprovalAuctionMapper(vendorDetails.getId());
								if(vendorDetails.getIsAccepted() == true)
								{
									vendorApprovalAuctionMapper.setApprovedQuantity(vendorDetails.getApprovedAuctionQuantity());
									vendorApprovalAuctionMapper.setIsAccept(true);
									vendorApprovalAuctionMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(auctionVendorApproval.getUnitId()));
//									vendorApprovalAuctionMapper.setUpdatedBy(vendorApprovalAuctionMapper.getUser());
									vendorApprovalAuctionMapper.setUpdatedByUser(request.getUserId());
								}
								else
								{
									vendorApprovalAuctionMapper.setRejectNote(vendorDetails.getRejectNote());
									vendorApprovalAuctionMapper.setIsAccept(false);
									vendorApprovalAuctionMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(auctionVendorApproval.getUnitId()));
//									vendorApprovalAuctionMapper.setUpdatedBy(vendorApprovalAuctionMapper.getUser());
									vendorApprovalAuctionMapper.setUpdatedByUser(request.getUserId());
								}
							}
						}
					}
				}
			}
			
			auctionVendorApprovalDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(auctionVendorApproval.getUnitId()));
//			auctionVendorApprovalDetails.setUpdatedBy(auctionVendorApprovalDetails.getUser());
			auctionVendorApprovalDetails.setUpdatedByUser(request.getUserId());
		}
		auctionVendorApproval.setUpdatedDate(TimeZoneComponent.getDateByzone(auctionVendorApproval.getUnitId()));
//		auctionVendorApproval.setUpdatedBy(auctionVendorApproval.getUser());
		auctionVendorApproval.setUpdatedByUser(request.getUserId());
		
		CommonResponse response = new CommonResponse();
		response.setId(auctionVendorApproval.getId().longValue());
		response.setMessage("Item for auction saved successfully");

		return response;
	}
	
}

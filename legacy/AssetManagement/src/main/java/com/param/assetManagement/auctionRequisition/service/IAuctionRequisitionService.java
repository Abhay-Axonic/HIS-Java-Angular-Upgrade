package com.param.assetManagement.auctionRequisition.service;

import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionDropdown;
import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionListResponse;
import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionRequest;

public interface IAuctionRequisitionService {

	AuctionRequisitionDropdown getDropdownList(Integer unitId, Integer userId);

	Object getAuctionRequisitionList(AuctionRequisitionRequest request);

	Object getAuctionRequisition(Integer id, Integer unitId, Integer orgId, Integer userId);

	Object saveAuctionRequisition(AuctionRequisitionListResponse request);

	Object approveAuctionRequisition(AuctionRequisitionListResponse request);

	Object cancelAuctionRequisition(AuctionRequisitionListResponse request);

	Object updateAuctionRequisition(AuctionRequisitionListResponse request);

}

package com.param.assetManagement.auctionRequisition.service;

import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionApprovalRequest;
import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionDropdown;
import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionRequest;

public interface IAuctionRequisiApprovalService {

	AuctionRequisitionDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId);

	Object getAuctionRequisitionApprovalList(AuctionRequisitionRequest request);

	Object getAuctionRequisitionApproval(Integer id);

	Object saveAuctionRequisitionApproval(AuctionRequisitionApprovalRequest request);

	Object approveAuctionRequisitionApproval(AuctionRequisitionApprovalRequest request);

}

package com.param.assetManagement.auctionRequisition.dao;

import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionRequest;

public interface IAuctionRequisiApprovalDao {

	Object searchAuctionRequisitionApproval(AuctionRequisitionRequest request, Integer screenId);

}

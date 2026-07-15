package com.param.assetManagement.auctionRequisition.dao;

import java.util.List;

import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionListResponse;
import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionRequest;
import com.param.entity.model.asset.AuctionRequisition;
import com.param.entity.model.asset.ItemAuction;
import com.param.entity.model.master.RejectReason;

public interface IAuctionRequisitionDao {

	List<AuctionRequisitionListResponse> searchAuctionRequisition(AuctionRequisitionRequest request, Integer screenId);

	AuctionRequisition getAuctionRequisition(Integer id);

	AuctionRequisition saveAuctionRequisition(AuctionRequisition auctionRequisition);

	AuctionRequisition updateAuctionRequisition(AuctionRequisition auctionRequisition);

	List<RejectReason> getRejectReasons();

	ItemAuction saveItemAuction(ItemAuction itemAuction);

}

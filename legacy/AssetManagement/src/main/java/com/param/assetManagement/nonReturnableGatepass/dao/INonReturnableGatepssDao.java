package com.param.assetManagement.nonReturnableGatepass.dao;

import java.util.List;

import com.param.assetManagement.itemForAuction.dto.ItemAuctionDetails;
import com.param.assetManagement.nonReturnableGatepass.dto.NonReturnableGatepssRequest;
import com.param.assetManagement.nonReturnableGatepass.dto.NonReturnableGatepssResponse;
import com.param.assetManagement.nonReturnableGatepass.dto.NonReturnableGatepssResponseItem;
import com.param.entity.model.asset.AuctionVendorApprovalDetail;
import com.param.entity.model.asset.NonReturnableGatepass;
import com.param.entity.model.asset.VendorApprovalAuctionMapper;

public interface INonReturnableGatepssDao {

	List<NonReturnableGatepass> searchNonReturnableGatepass(NonReturnableGatepssRequest request, Integer screenId);
	
	NonReturnableGatepass getNonReturnableGatepassById(Integer id);

	List<ItemAuctionDetails> getAuctionVendorApprovalDetail(Integer id);
	
	AuctionVendorApprovalDetail getAuctionAuctionVendorApprovalDetail(Integer id);

	VendorApprovalAuctionMapper getVendorApprovalAuctionMapper(Integer id);
	
	NonReturnableGatepass saveNonReturnableGatepass(NonReturnableGatepass nonReturableGatepass);
	
	NonReturnableGatepass updateNonReturnableGatepass(NonReturnableGatepass nonReturableGatepass);
	
	List<NonReturnableGatepssResponse> getAuctionVendorApprovalList(NonReturnableGatepssRequest nonReturableGatepass);
	
	List<NonReturnableGatepssResponse> getAuctionVendorApprovalVendorList(NonReturnableGatepssRequest nonReturableGatepass, Integer id);
	
	List<NonReturnableGatepssResponseItem> getAuctionVendorApprovalItemList(Integer id);
	
}
package com.param.assetManagement.indent.rejectedAssetAcceptance.dao;

import java.util.List;

import com.param.assetManagement.indent.assetIssue.dto.AssetIssueRequest;
import com.param.assetManagement.indent.rejectedAssetAcceptance.dto.RejectAssetResponse;
import com.param.entity.model.asset.RejectedAssetAcceptance;
import com.param.entity.model.asset.RejectedAssetAcceptanceDetail;

public interface IRejectedAssetAcceptanceDao {

	List<RejectAssetResponse> searchRejectAssetAcceptanceList(AssetIssueRequest request, Integer screenId);

	RejectedAssetAcceptance getRejectAssetAcceptance(Integer id);

	List<RejectedAssetAcceptanceDetail> getRejectAssetAcceptanceDetail(Integer id);

	RejectedAssetAcceptance updateRejectdAssetAcceptance(RejectedAssetAcceptance rejectedAssetAcceptance);

	RejectedAssetAcceptance save(RejectedAssetAcceptance rejectedAsset);

}

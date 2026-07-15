package com.param.assetManagement.indent.assetReturnAcceptance.dao;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequest;
import com.param.assetManagement.indent.assetReturn.dto.AssetReturnResponse;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.asset.AssetIssue;
import com.param.entity.model.asset.AssetReturn;
import com.param.entity.model.asset.AssetReturnDetail;
import com.param.entity.model.asset.RejectedAssetAcceptance;
import com.param.global.utility.Util;

@Repository
public class AssetReturnAcceptanceDao extends AbstractDao<Integer, AssetReturn> implements IAssetReturnAcceptanceDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<AssetReturnResponse> searchAssetReturnAcceptanceList(AssetIndentRequest request, Integer screenId) {

		String hql = "SELECT assetReturn.id AS id, assetReturn.addedDate AS returnDate, assetReturn.returnNo AS returnNo, "
				+ "		fromStore.storeId AS fromStoreId, fromStore.storeName AS fromStore, toStore.storeId AS toStoreId, toStore.storeName AS toStore, "
				+ "		user.userN"
				+ "ame AS returnBy, "
				+ "     assetReturn.remark AS remark, status.status AS status, status.id AS statusId "
				+ " FROM AssetReturn AS assetReturn " 
				+ "		INNER JOIN assetReturn.addedBy as user "
				+ "		INNER JOIN assetReturn.fromStore AS fromStore "
				+ "		INNER JOIN assetReturn.toStore AS toStore " 
				+ "		INNER JOIN assetReturn.status AS status "
				+ "		INNER JOIN assetReturn.approvalStatus AS approvalStatus WITH approvalStatus.isActive = true AND approvalStatus.isDeleted = false "
				+ "	WHERE assetReturn.isActive = true AND assetReturn.isDeleted = false AND assetReturn.unit.unitId = :unitId "
				+ " 	AND assetReturn.addedDate BETWEEN :fromDate AND :toDate "
				+ "     AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) "
				+ "     AND (:toStoreId = 0 OR toStore.storeId = :toStoreId ) "
				+ "     AND (:returnNo = '' OR lower(assetReturn.returnNo) LIKE :returnNo) "
				+ "     AND (:statusId = 0 OR status.id = :statusId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ "ORDER BY assetReturn.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(request.getFromDate()));
		query.setParameter("toDate", Util.getDate(request.getToDate()));
		query.setParameter("fromStoreId", request.getFromStoreId() != null ? request.getFromStoreId() : 0);
		query.setParameter("toStoreId", request.getToStoreId() != null ? request.getToStoreId() : 0);
		query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("returnNo", request.getReturnNo() != null ? '%'+request.getReturnNo().toLowerCase() + '%' : "");
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(AssetReturnResponse.class));

		return query.list();
	}

	@Override
	public AssetReturn getAssetReturn(Integer id) {

		String hql = " SELECT assetReturn FROM AssetReturn AS assetReturn "
				+ "	WHERE assetReturn.id = :id AND assetReturn.isActive = true AND assetReturn.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (AssetReturn) query.uniqueResult();
	}

	@Override
	public List<AssetReturnDetail> getAssetReturnDetail(Integer id) {

		String hql = "SELECT assetReturnDetail FROM AssetReturnDetail AS assetReturnDetail "
				+ "	INNER JOIN assetReturnDetail.assetReturn AS assetReturn WITH assetReturn.isActive = true AND assetReturn.isDeleted = false "
				+ " WHERE assetReturn.id = :id AND assetReturnDetail.isActive = true AND assetReturnDetail.isDeleted = false  "
				+ " ORDER BY assetReturnDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public AssetReturn updateAssetReturn(AssetReturn assetReturn) {
		getSession().update(assetReturn);
		return assetReturn;
	}

	@Override
	public RejectedAssetAcceptance save(RejectedAssetAcceptance rejectedAssetAcceptance) {
		getSession().save(rejectedAssetAcceptance);
		return rejectedAssetAcceptance;
	}

	@Override
	public void issueUpdate(AssetIssue issue) {

		getSession().update(issue); 
	
	}

}

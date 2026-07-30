package com.param.assetManagement.auctionRequisition.dao;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionListResponse;
import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionRequest;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.asset.AuctionRequisition;
import com.param.entity.model.asset.ItemAuction;
import com.param.entity.model.master.RejectReason;
import com.param.global.utility.Util;

@Repository
public class AuctionRequisitionDao extends AbstractDao<Integer, AuctionRequisition> implements IAuctionRequisitionDao {
	@Autowired
	private SessionService sessionService;

	@Override
	public List<AuctionRequisitionListResponse> searchAuctionRequisition(AuctionRequisitionRequest request, Integer screenId) {
		String hql = "SELECT auctionRequisition.id AS id, auctionRequisition.addedDate AS requisitionDate, auctionRequisition.auctionRequisitionNo AS requisitionNo, "
				+ "		user.userName as requisitionName, user.userId AS requisitionBy, store.storeId AS storeId, store.storeName AS store, "
				+ "     status.status AS status, status.id AS statusId, auctionRequisition.approvalStatus.id AS approvalStatusId, "
				+ "		auctionRequisition.approvalStatus.status AS approvalStatus "
				+ "     FROM AuctionRequisition AS auctionRequisition INNER JOIN auctionRequisition.store AS store WITH store.isActive = true AND store.isDeleted = false " 
				+ "		INNER JOIN auctionRequisition.addedBy as user INNER JOIN auctionRequisition.status AS status WITH status.isActive = true AND status.isDeleted = false "
				+ "	    WHERE auctionRequisition.isActive = true AND auctionRequisition.isDeleted = false AND auctionRequisition.unit.unitId = :unitId "
				+ " 	AND auctionRequisition.addedDate BETWEEN :fromDate AND :toDate "
				+ "     AND (:storeId = 0 OR store.storeId = :storeId) "
				+ "     AND (:statusId = 0 OR status.id = :statusId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "		INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "		INNER JOIN screenStatus.status as status) " + "ORDER BY auctionRequisition.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(request.getFromDate()));
		query.setParameter("toDate", Util.getDate(request.getToDate()));
		query.setParameter("storeId",request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("statusId",request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(AuctionRequisitionListResponse.class));

		return query.list();
	}

	@Override
	public AuctionRequisition getAuctionRequisition(Integer id, Integer unitId) {
		String hql = "SELECT auctionRequisition FROM com.param.entity.model.asset.AuctionRequisition AS auctionRequisition "
				+ "WHERE auctionRequisition.id = :id AND auctionRequisition.unit.unitId = :unitId "
				+ "AND auctionRequisition.isActive = true AND auctionRequisition.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		return (AuctionRequisition) query.uniqueResult();
	}

	@Override
	public AuctionRequisition saveAuctionRequisition(AuctionRequisition auctionRequisition) {
		getSession().save(auctionRequisition);
		return auctionRequisition;
	}

	@Override
	public AuctionRequisition updateAuctionRequisition(AuctionRequisition auctionRequisition) {
		getSession().update(auctionRequisition);
		return auctionRequisition;
	}
	
	@Override
	public ItemAuction saveItemAuction(ItemAuction itemAuction){
		getSession().save(itemAuction);
		return itemAuction;
	}

	@Override
	public List<RejectReason> getRejectReasons() {
		String hql = "SELECT rejectReason FROM com.param.entity.model.master.RejectReason AS rejectReason "
				+ "WHERE rejectReason.isActive = true AND rejectReason.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		return query.list();
	}

}

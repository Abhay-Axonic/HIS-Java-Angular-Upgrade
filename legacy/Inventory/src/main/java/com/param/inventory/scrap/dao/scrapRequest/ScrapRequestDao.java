package com.param.inventory.scrap.dao.scrapRequest;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.AssetDisposalWorklistRequest;
import com.param.entity.model.inventory.ScrapRequest;
import com.param.entity.model.inventory.ScrapRequestDetail;
import com.param.global.utility.Util;
import com.param.inventory.scrap.dto.scrapRequest.ScrapResponse;

@Repository
public class ScrapRequestDao extends AbstractDao<Integer, ScrapRequest> implements IScrapRequestDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<ScrapResponse> scrapRequestList(com.param.inventory.scrap.dto.scrapRequest.ScrapRequest request,
			Integer screenId) {
		String hql = "SELECT scrapRequest.id AS id, scrapRequest.addedDate AS requestDate, scrapRequest.requestNo AS requestNo, "
				+ "		store.storeId AS storeId, store.storeName AS storeName, assetType.id AS assetTypeId, assetType.type AS assetType,"
				+ "		user.userName AS requestedBy, approvalStatus.status AS approvalStatus, status.status AS status, scrapRequest.nearByExpiry AS nearByExpiry "
				+ " FROM com.param.entity.model.inventory.ScrapRequest AS scrapRequest "
				+ "		INNER JOIN scrapRequest.addedBy as user " + "		INNER JOIN scrapRequest.store AS store "
				+ "		INNER JOIN scrapRequest.assetType AS assetType "
				+ "		INNER JOIN scrapRequest.status AS status "
				+ "		INNER JOIN scrapRequest.approvalStatus AS approvalStatus WITH approvalStatus.isActive = true AND approvalStatus.isDeleted = false "
				+ "	WHERE scrapRequest.isActive = true AND scrapRequest.isDeleted = false AND scrapRequest.unit.unitId = :unitId "
				+ "		AND scrapRequest.addedDate BETWEEN :fromDate AND :toDate "
				+ "     AND (:storeId = 0 OR store.storeId = :storeId) "
				+ "     AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ "     AND (:statusId = 0 OR status.id = :statusId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ "ORDER BY scrapRequest.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(request.getFromDate()));
		query.setParameter("toDate", Util.getDate(request.getToDate()));
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
		query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId(): sessionService.getUnitId());

		query.setResultTransformer(Transformers.aliasToBean(ScrapResponse.class));

		return query.list();
	}

	@Override
	public ScrapRequest saveScrapRequest(ScrapRequest scrapRequest) {
		getSession().save(scrapRequest);
		return scrapRequest;
	}

	@Override
	public ScrapRequest getScrapRequest(Integer id) {

		String hql = "SELECT scrapRequest FROM com.param.entity.model.inventory.ScrapRequest AS scrapRequest WHERE scrapRequest.id = :id  AND scrapRequest.isActive = true AND scrapRequest.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (ScrapRequest) query.uniqueResult();
	}

	@Override
	public List<ScrapRequestDetail> getScrapRequestdetail(Integer id) {
		String hql = "SELECT scrapRequestDetail FROM com.param.entity.model.inventory.ScrapRequestDetail AS scrapRequestDetail \r\n"
				+ "	INNER JOIN scrapRequestDetail.scrapRequest AS scrapRequest WITH scrapRequest.isActive = true AND scrapRequest.isDeleted = false \r\n"
				+ "WHERE scrapRequest.id = :id AND scrapRequestDetail.isActive = true AND scrapRequestDetail.isDeleted = false \r\n"
				+ "ORDER BY scrapRequestDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public ScrapRequest updateScrapRequest(ScrapRequest scrapRequest) {

		getSession().update(scrapRequest);
		return scrapRequest;
	}

	@Override
	public AssetDisposalWorklistRequest saveScrapRequest(AssetDisposalWorklistRequest assetDisposalWorklistRequest) {
		getSession().save(assetDisposalWorklistRequest);
		return assetDisposalWorklistRequest;
	}

}

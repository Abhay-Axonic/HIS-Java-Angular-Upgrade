package com.param.procurement.loanInOut.dao.loanOut;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.procurement.LoanOut;
import com.param.entity.model.procurement.LoanOutDetail;
import com.param.global.utility.Util;
import com.param.procurement.loanInOut.dto.LoanInOutRequest;
import com.param.procurement.loanInOut.dto.LoanInOutResponse;
import com.param.global.common.ServiceException;
import org.apache.log4j.Logger;

@Repository
public class LoanOutDao extends AbstractDao<Integer, LoanOut> implements ILoanOutDao {

	final static Logger logger = Logger.getLogger(LoanOutDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<LoanInOutResponse> searchLoanOutList(LoanInOutRequest request, Integer screenId) {
		try {

			String hql = "SELECT loanOut.id AS Id, loanOut.loanOutNo AS loanOutNo, loanOut.addedDate AS loanOutDate,"
					+ " store.storeId AS storeId, store.storeName AS store, vendor.id AS vendorId, vendor.name AS vendor,"
					+ " user.userId AS outwardById, user.userName AS outwardBy, assetType.id AS assetTypeId, "
					+ " assetType.type AS assetType, status.id As statusId, status.status AS status, loanOut.remark AS remark   "
					+ " FROM LoanOut AS loanOut " + " INNER JOIN loanOut.addedBy as user WITH user.unitId = :unitId "
					+ " INNER JOIN loanOut.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN loanOut.vendor As vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
					+ " INNER JOIN loanOut.assetType AS assetType WITH assetType.isActive = true AND assetType.isDeleted = false "
					+ " INNER JOIN loanOut.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " WHERE loanOut.unit.unitId = :unitId AND loanOut.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND ( status.id = :statusId OR :statusId = 0 ) "
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND ( assetType.id = :assetTypeId OR :assetTypeId = 0 ) "
					+ " AND ( user.userId = :outWardById OR :outWardById = 0 ) "
					+ "	AND ((:outWardById != 0 AND :outWardBy != '') OR (:outWardBy = '' OR lower(user.userName) LIKE :outWardBy)) "
					+ " AND ( vendor.id = :vendorId OR :vendorId = 0 ) "
					+ "	AND ((:vendorId != 0 AND :vendorName != '') OR (:vendorName = '' OR lower(vendor.name) LIKE :vendorName)) "
					+ " AND loanOut.isActive = true AND loanOut.isDeleted = false "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "	INNER JOIN screenStatus.status as status) " + " ORDER BY loanOut.id DESC ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
			query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
			query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
			query.setParameter("vendorId", request.getVendorId() != null ? request.getVendorId() : 0);
			query.setParameter("vendorName",
					request.getVendorName() != null ? request.getVendorName().toLowerCase() + '%' : "");
			query.setParameter("outWardById", request.getOutWardById() != null ? request.getOutWardById() : 0);
			query.setParameter("outWardBy",
					request.getSearchKeyword() != null ? request.getSearchKeyword().toLowerCase() + '%' : "");
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
			query.setParameter("screenId", screenId);
			query.setResultTransformer(Transformers.aliasToBean(LoanInOutResponse.class));

			return query.list();
		} catch (Exception e) {
			logger.error("searchLoanOutList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public LoanOut saveLoanIn(LoanOut loanOut) {

		getSession().save(loanOut);
		return loanOut;
	}

	@Override
	public LoanOut getLoanOut(Integer id,Integer unitId) {

		String hql = " SELECT loanOut FROM LoanOut AS loanOut "
				+ " WHERE loanOut.id = :id AND loanOut.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());

		return (LoanOut) query.uniqueResult();
	}

	@Override
	public LoanOut updateLoanOut(LoanOut loanOut) {

		getSession().update(loanOut);
		return loanOut;
	}

	@Override
	public List<LoanOutDetail> getLoanOutDetails(Integer id) {
		try {
			String hql = " SELECT loanOutDetail FROM LoanOutDetail AS loanOutDetail "
					+ " INNER JOIN loanOutDetail.loanOut AS loanOut WITH loanOut.isActive = true AND loanOut.isDeleted = false "
					+ " INNER JOIN loanOutDetail.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false "
					//+ " INNER JOIN loanOutDetail.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
					//+ " INNER JOIN loanOutDetail.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
					+ " WHERE loanOut.id = :id AND loanOut.unit.unitId = :unitId AND loanOutDetail.isActive = true AND loanOutDetail.isDeleted = false ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", sessionService.getUnitId());

			return query.list();
		} catch (Exception e) {
			logger.error("getLoanOutDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}

package com.param.procurement.loanInOut.dao.loanIn;

import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.procurement.LoanIn;
import com.param.entity.model.procurement.LoanInDetail;
import com.param.entity.model.procurement.ShelfItem;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.procurement.loanInOut.dto.LoanInOutRequest;

@Repository
public class LoanInDao extends AbstractDao<Integer, LoanIn> implements ILoanInDao {

	final static Logger logger = Logger.getLogger(LoanInDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<LoanIn> getLoanInList(LoanInOutRequest request, Integer screenId) {
		try {

			String hql = "SELECT loanIn FROM LoanIn AS loanIn "
					+ " INNER JOIN loanIn.addedBy as user "
					+ " INNER JOIN loanIn.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN loanIn.vendor As vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
					+ " INNER JOIN loanIn.assetType AS assetType WITH assetType.isActive = true AND assetType.isDeleted = false "
					+ " INNER JOIN loanIn.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " WHERE loanIn.unit.unitId = :unitId AND loanIn.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND ( status.id = :statusId OR :statusId = 0 ) "
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND ( assetType.id = :assetTypeId OR :assetTypeId = 0 ) "
					+ " AND ( user.userId = :inWardById OR :inWardById = 0 ) "
					+ "	AND ((:inWardById != 0 AND :inWardBy != '') OR (:inWardBy = '' OR lower(user.userName) LIKE :inWardBy)) "
					+ " AND ( vendor.id = :vendorId OR :vendorId = 0 ) "
					+ "	AND ((:vendorId != 0 AND :vendorName != '') OR (:vendorName = '' OR lower(vendor.name) LIKE :vendorName) OR lower(vendor.code) LIKE :vendorName) "
					+ " AND loanIn.isActive = true AND loanIn.isDeleted = false "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "	INNER JOIN screenStatus.status as status) " + " ORDER BY loanIn.id DESC ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
			query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
			query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
			query.setParameter("vendorName",
					request.getVendorName() != null ? request.getVendorName().toLowerCase() + '%' : "");
			query.setParameter("vendorId", request.getVendorId() != null ? request.getVendorId() : 0);
			query.setParameter("inWardBy",
					request.getInWardBy() != null ? request.getInWardBy().toLowerCase() + '%' : "");
			query.setParameter("inWardById", request.getInWardById() != null ? request.getInWardById() : 0);
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
			query.setParameter("screenId", screenId);

			return query.list();
		} catch (Exception e) {
			logger.error("getLoanInList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public LoanIn getLoanInDetails(Integer id,Integer unitId) {
		try {
			String hql = " SELECT loanIn FROM LoanIn AS loanIn "
					+ " WHERE loanIn.id = :id AND loanIn.unit.unitId = :unitId "
					+ "AND loanIn.isActive = true AND loanIn.isDeleted = false";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());

			return (LoanIn) query.uniqueResult();
		} catch (Exception e) {
			logger.error("getLoanInDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<LoanInDetail> getLoanInDetailList(Integer loanInId) {
		try {
			String hql = " SELECT loanInDetail FROM LoanInDetail AS loanInDetail "
					+ " INNER JOIN loanInDetail.loanIn AS loanIn WITH loanIn.isActive = true AND loanIn.isDeleted = false "
					// + " INNER JOIN loanInDetail.item AS item WITH item.isActive = true AND
					// item.isDeleted = false "
					// + " INNER JOIN loanInDetail.uomType AS uomType WITH uomType.isActive = true
					// AND uomType.isDeleted = false "
					// + " INNER JOIN loanInDetail.uomUnit AS uomUnit WITH uomUnit.isActive = true
					// AND uomUnit.isDeleted = false "
					+ " WHERE loanIn.id = :id AND loanIn.unit.unitId = :unitId AND loanInDetail.isActive = true AND loanInDetail.isDeleted = false ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", loanInId);
			query.setParameter("unitId", sessionService.getUnitId());

			return query.list();
		} catch (Exception e) {
			logger.error("getLoanInDetailList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public LoanIn saveLoanIn(LoanIn loanIn) {
		getSession().save(loanIn);
		return loanIn;
	}

	@Override
	public LoanIn updateLoanIn(LoanIn loanIn) {
		getSession().update(loanIn);
		return loanIn;
	}

	@Override
	public Batch saveBatch(Batch batch) {
		getSession().save(batch);
		return batch;
	}

	@Override
	public LoanInDetail getLoanInDetailById(Integer id) {
		try {
			String hql = " SELECT loanInDetail FROM LoanInDetail AS loanInDetail "
					+ " WHERE loanInDetail.isDeleted = false AND loanInDetail.isActive = true "
					+ " AND loanInDetail.id = :id AND loanInDetail.unit.unitId = :unitId";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", sessionService.getUnitId());

			return (LoanInDetail) query.uniqueResult();
		} catch (Exception e) {
			logger.error("getLoanInDetailById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public ShelfItem saveShelfItem(ShelfItem shelfItem) {
		getSession().save(shelfItem);
		return shelfItem;
	}

	@Override
	public Batch updateBatchQuantityAndFlag(Batch batch) {
		getSession().update(batch);
		return batch;
	}
}
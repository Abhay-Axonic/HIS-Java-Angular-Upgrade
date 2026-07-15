package com.param.procurement.loanInOut.dao.loanOutReceive;

import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.procurement.LoanOut;
import com.param.entity.model.procurement.LoanOutDetail;
import com.param.entity.model.procurement.LoanOutReceive;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.procurement.loanInOut.dto.LoanInOutRequest;
import com.param.procurement.loanInOut.dto.LoanInOutResponse;

@Repository
public class LoanOutReceiveDao extends AbstractDao<Integer, LoanOutReceive> implements ILoanOutReceiveDao {

	final static Logger logger = Logger.getLogger(LoanOutReceiveDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<LoanInOutResponse> searchLoanOutReceiveList(LoanInOutRequest request, Integer screenId) {
		try {

			String hql = "SELECT loanOutReceive.id AS Id, loanOutReceive.loanoutReceiveNo AS loanOutReceiveNo, loanOutReceive.addedDate AS loanOutReceiveDate,"
					+ " store.storeId AS storeId, store.storeName AS store, vendor.id AS vendorId, vendor.name AS vendor,"
					+ " user.userId AS inwardById, user.userName AS inwardBy, status.id As statusId, status.status AS status,"
					+ " assetType.id AS assetTypeId, assetType.type AS assetType, loanOutReceive.remark AS remark "
					+ " FROM LoanOutReceive AS loanOutReceive "
					+ " INNER JOIN loanOutReceive.addedBy as user WITH user.unitId = :unitId "
					+ " INNER JOIN loanOutReceive.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN loanOutReceive.vendor As vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
					+ " INNER JOIN loanOutReceive.assetType AS assetType WITH assetType.isActive = true AND assetType.isDeleted = false "
					+ " INNER JOIN loanOutReceive.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " WHERE loanOutReceive.unit.unitId = :unitId AND loanOutReceive.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND ( status.id = :statusId OR :statusId = 0 ) "
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND ( assetType.id = :assetTypeId OR :assetTypeId = 0 ) "
					+ " AND ( user.userId = :inWardById OR :inWardById = 0 ) "
					+ "	AND ((:inWardById != 0 AND :inWardBy != '') OR (:inWardBy = '' OR lower(user.userName) LIKE :inWardBy)) "
					+ " AND ( vendor.id = :vendorId OR :vendorId = 0 ) "
					+ "	AND ((:vendorId != 0 AND :vendorName != '') OR (:vendorName = '' OR lower(vendor.name) LIKE :vendorName)) "
					+ " AND loanOutReceive.isActive = true AND loanOutReceive.isDeleted = false "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "	INNER JOIN screenStatus.status as status) " + " ORDER BY loanOutReceive.id DESC ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
			query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
			query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
			query.setParameter("vendorId", request.getVendorId() != null ? request.getVendorId() : 0);
			query.setParameter("vendorName",
					request.getVendorName() != null ? request.getVendorName().toLowerCase() + '%' : "");
			query.setParameter("inWardBy",
					request.getInWardBy() != null ? request.getInWardBy().toLowerCase() + '%' : "");
			query.setParameter("inWardById", request.getInWardById() != null ? request.getInWardById() : 0);
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
			query.setParameter("screenId", screenId);
			query.setResultTransformer(Transformers.aliasToBean(LoanInOutResponse.class));

			return query.list();
		} catch (Exception e) {
			logger.error("searchLoanOutReceiveList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public LoanOutReceive saveLoanOutReceive(LoanOutReceive loanOutReceive) {
		getSession().save(loanOutReceive);
		return loanOutReceive;
	}

	@Override
	public LoanOutReceive getLoanOutReceive(Integer id, Integer unitId) {
		String hql = " SELECT loanOutReceive FROM LoanOutReceive AS loanOutReceive "
				+ " WHERE loanOutReceive.id = :id AND loanOutReceive.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());

		return (LoanOutReceive) query.uniqueResult();
	}

	@Override
	public List<LoanOut> getLoanOutItems(LoanInOutRequest request, List<Integer> statusIds) {

		String hql = "SELECT loanOut FROM LoanOut AS loanOut "
				+ " INNER JOIN loanOut.vendor As vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ " INNER JOIN loanOut.assetType AS assetType WITH assetType.isActive = true AND assetType.isDeleted = false "
				+ " INNER JOIN loanOut.store AS store WITH store.isActive = true AND store.isDeleted = false "
				+ "	INNER JOIN loanOut.status as status "
				//+ " WHERE loanOut.unit.unitId = :unitId AND loanOut.addedDate BETWEEN :loanOutDate AND :toDate "
				+ " WHERE loanOut.unit.unitId = :unitId AND loanOut.addedDate BETWEEN :loanOutDate AND :toDate "
				+ " AND loanOut.isActive = true AND loanOut.isDeleted = false "
				+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
				+ " AND ( assetType.id = :assetTypeId OR :assetTypeId = 0 ) "
				//+ " AND ( vendor.id = :vendorId OR :vendorId = 0 ) "
			    + " AND (status.id  IN (:statusId)) "  //HIS-16135 //HIS-17381
				+ " AND (:loanOutNo = '' OR lower(loanOut.loanOutNo) LIKE :loanOutNo) " + " ORDER BY loanOut.id DESC ";

		Query query = getSession().createQuery(hql);
		query.setParameter("loanOutNo", request.getLoanInNo() != null ? request.getLoanInNo().toLowerCase() + '%' : "");
		query.setParameter("loanOutDate", Util.getDate(request.getFromDate()));
		query.setParameter("toDate", Util.getDate(request.getToDate()));
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
		//query.setParameter("vendorId", request.getVendorId() != null ? request.getVendorId() : 0);
		query.setParameterList("statusId", statusIds);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());

		return query.list();
	}

	@Override
	public List<LoanOutDetail> getLoanOutItemsDetails(LoanInOutRequest request) {

		String hql = " SELECT loanOutDetail FROM LoanOutDetail AS loanOutDetail "
				+ " WHERE loanOutDetail.isDeleted = false AND loanOutDetail.isActive = true "
				+ " AND loanOutDetail.id IN (:id) AND loanOutDetail.unit.unitId = :unitId";

		Query query = getSession().createQuery(hql);
		query.setParameterList("id", request.getLoanOutId());
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());

		return query.list();
	}

	@Override
	public LoanOutReceive updateLoanOutReceive(LoanOutReceive loanOutReceive) {
		getSession().update(loanOutReceive);
		return loanOutReceive;
	}

	@Override
	public List<LoanOutReceive>  getLoanOutReceive1(Integer id, Integer unitId) {

		String hql = " SELECT loanOutReceive FROM LoanOutReceive AS loanOutReceive "
				+ " WHERE loanOutReceive.loanOut.id = :id AND loanOutReceive.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());

		return   query.list();
	
	}

}

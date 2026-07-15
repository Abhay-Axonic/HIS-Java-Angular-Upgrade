package com.param.procurement.loanInOut.dao.loanInOut;

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
import com.param.entity.model.procurement.LoanInOut;
import com.param.entity.model.procurement.LoanInOutDetail;
import com.param.entity.model.procurement.ShelfItem;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.procurement.loanInOut.dto.LoanInOutRequest;

@Repository
public class LoanInOutDao extends AbstractDao<Integer, LoanInOut> implements ILoanInOutDao {

	final static Logger logger = Logger.getLogger(LoanInOutDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<LoanInOut> getloanInOutList(LoanInOutRequest request, Integer screenId) {
		try
		{
			
			String hql = "SELECT loanInOut FROM LoanInOut AS loanInOut "
					+ " INNER JOIN loanInOut.loanIn AS loanIn WITH loanIn.isActive = true AND loanIn.isDeleted = false "
					+ " INNER JOIN loanInOut.addedBy as user  "
					+ " INNER JOIN loanInOut.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN loanIn.vendor As vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
					+ " INNER JOIN loanIn.assetType AS assetType WITH assetType.isActive = true AND assetType.isDeleted = false "
					+ " INNER JOIN loanIn.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " WHERE loanInOut.unit.unitId = :unitId AND loanInOut.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND ( status.id = :statusId OR :statusId = 0 ) "
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND ( assetType.id = :assetTypeId OR :assetTypeId = 0 ) "
					+ " AND ( user.userId = :outWardById OR :outWardById = 0 ) "
					+ "	AND ((:outWardById != 0 AND :outWardBy != '') OR (:outWardBy = '' OR lower(user.userName) LIKE :outWardBy))"
					+ " AND ( vendor.id = :vendorId OR :vendorId = 0 ) "
					+ "	AND ((:vendorId != 0 AND :vendorName != '') OR (:vendorName = '' OR lower(vendor.name) LIKE :vendorName) OR lower(vendor.code) LIKE :vendorName) "
					+ " AND loanIn.isActive = true AND loanIn.isDeleted = false "
					+ " AND loanInOut.isActive = true AND loanInOut.isDeleted = false "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
					+ "	INNER JOIN screenStatus.status as status) "
					+ " ORDER BY loanInOut.id DESC ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
			query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
			query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
			query.setParameter("vendorId", request.getVendorId() != null ? request.getVendorId() : 0);
			query.setParameter("vendorName", request.getVendorName() != null ? request.getVendorName().toLowerCase() + '%' : "");
			query.setParameter("outWardBy", request.getOutWardBy() != null ? request.getOutWardBy().toLowerCase() + '%' : "");
			query.setParameter("outWardById", request.getOutWardById() != null ? request.getOutWardById() : 0);
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
			query.setParameter("screenId", screenId);
			
		return query.list();
	}
	catch(Exception e)
	{
		logger.error("getloanInOutList failed", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public LoanInOut getloanInOutDetails(Integer id,Integer unitId)
	{
		try
		{
			 String hql = " SELECT loanInOut FROM LoanInOut AS loanInOut "
	    				+ " WHERE loanInOut.id = :id AND loanInOut.unit.unitId = :unitId ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
			
			return (LoanInOut)query.uniqueResult();
		}
	catch(Exception e)
	{
		logger.error("getloanInOutDetails failed", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public LoanInOut saveloanInOut(LoanInOut loanInOut)
	{
		getSession().save(loanInOut);
		return loanInOut;
	}
	
	@Override
	public LoanInOut updateloanInOut(LoanInOut loanInOut)
	{
		getSession().update(loanInOut);
		return loanInOut;
	}
	
	@Override
	public Batch saveBatch(Batch batch) {
		getSession().save(batch);
		return batch;
	}
	
	@Override
	public LoanInOutDetail getloanInOutDaoDetailById(Integer id)
	{
		try
		{
			String hql = " SELECT loanInOutDetail FROM LoanInOutDetail AS loanInOutDetail "
					+ " WHERE loanInOutDetail.isDeleted = false AND loanInOutDetail.isActive = true "
					+ " AND loanInOutDetail.id = :id AND loanInOutDetail.unit.unitId = :unitId";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", sessionService.getUnitId());
			
			return (LoanInOutDetail)query.uniqueResult();
		}
		catch(Exception e)
		{
			logger.error("getloanInOutDaoDetailById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public 	ShelfItem saveShelfItem(ShelfItem shelfItem)
	{
		getSession().save(shelfItem);
		return shelfItem;
	}
	
	@Override
	public Batch updateBatch(Batch batch)
	{
		getSession().update(batch);
		return batch;
	}
	
	@Override
	public List<LoanIn> getLoanInItems(LoanInOutRequest request)
	{
		try
		{
			String hql = "SELECT loanIn FROM LoanIn AS loanIn "
					+ " INNER JOIN loanIn.vendor As vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
					+ " INNER JOIN loanIn.assetType AS assetType WITH assetType.isActive = true AND assetType.isDeleted = false "
					+ " INNER JOIN loanIn.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " INNER JOIN loanIn.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " WHERE loanIn.unit.unitId = :unitId AND loanIn.addedDate BETWEEN  :fromDate AND :toDate  "
					+ " AND status.id = 8 AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND ( assetType.id = :assetTypeId OR :assetTypeId = 0 ) "
					+ " AND ( vendor.id = :vendorId OR :vendorId = 0 ) "
					+ " AND loanIn.isActive = true AND loanIn.isDeleted = false "
					+ " AND (:loanInNo = '' OR lower(loanIn.inwardNo) LIKE :loanInNo) "
					+ " ORDER BY loanIn.id DESC ";

			Query query = getSession().createQuery(hql);
			query.setParameter("loanInNo", request.getLoanInNo() != null ? request.getLoanInNo().toLowerCase() + '%' : "");
			//query.setParameter("loanInDate", Util.getDate(request.getLoanInDate()));
			//query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
			query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
			query.setParameter("vendorId", request.getVendorId() != null ? request.getVendorId() : 0);
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
			
		return query.list();
	}
	catch(Exception e)
	{
		logger.error("getLoanInItems failed", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public List<LoanInDetail> getLoanInItemDetails(LoanInOutRequest request)
	{
		try
		{
			String hql = " SELECT loanInDetail FROM LoanInDetail AS loanInDetail "
					+ " WHERE loanInDetail.isDeleted = false AND loanInDetail.isActive = true "
					+ " AND loanInDetail.id IN (:id) AND loanInDetail.unit.unitId = :unitId";

			Query query = getSession().createQuery(hql);
			query.setParameterList("id", request.getLoanInId());
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
			
			return query.list();
		}
		catch(Exception e)
		{
			logger.error("getLoanInItemDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<LoanInDetail> getLoanInItemDetails(Integer id,Integer unitId)
	{
		try
		{
			String hql = " SELECT loanInDetail FROM LoanInDetail AS loanInDetail "
					+ " INNER JOIN loanInDetail.loanIn AS loanIn WITH loanIn.isActive = true AND loanIn.isDeleted = false "
					+ " INNER JOIN loanInDetail.status AS status WITH status.isActive = true AND status.isDeleted = false "
//					+ " WHERE loanInDetail.status = 8 AND loanInDetail.isDeleted = false AND loanInDetail.isActive = true "
					+ " WHERE loanIn.id = :id AND loanInDetail.unit.unitId = :unitId AND loanInDetail.isDeleted = false AND loanInDetail.isActive = true";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
			
			return query.list();
		}
		catch(Exception e)
		{
			logger.error("getLoanInItemDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<LoanInOutDetail> getLoanInOutDtls(Integer loanInDetailId, Integer screenId, Integer unitId) {
		String hql = " SELECT loanInOutDetail FROM LoanInOutDetail AS loanInOutDetail "
				+ " Inner join loanInOutDetail.loanInDetail AS loanInDtl "
				+ " Inner join loanInOutDetail.loanInOut AS loanInOut WITH loanInOut.isActive = true AND loanInOut.isDeleted = false "
				+ " INNER JOIN loanInOut.status AS status WITH status.isActive = true AND status.isDeleted = false "
				+ " WHERE loanInDtl.isDeleted = false AND loanInDtl.isActive = true "
				+ " AND loanInDtl.id = :loanInDetailId AND loanInOutDetail.unit.unitId = :unitId "
				+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus " + 
				  "	INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " + 
				  "	INNER JOIN screenStatus.status as status) ";

		Query query = getSession().createQuery(hql);
		query.setParameter("loanInDetailId", loanInDetailId);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		
		return query.list();
	}
}
package com.param.procurement.consignment.dao.ConsignmentInword;

import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.procurement.ConsignmentInward;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.procurement.consignment.dto.consignment.ConsignmentRequest;

@Repository
public class ConsignmentInwordDao extends AbstractDao<Integer, ConsignmentInward> implements IConsignmentInwordDao {

	final static Logger logger = Logger.getLogger(ConsignmentInwordDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<ConsignmentInward> getConsignmentInwardList(ConsignmentRequest request, Integer screenId) {
		try {

			String hql = "SELECT consignmentInward FROM ConsignmentInward AS consignmentInward "
					
					+ " INNER JOIN consignmentInward.addedBy as user WITH user.unitId = :unitId "
					+ " INNER JOIN consignmentInward.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN consignmentInward.vendor As vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
					+ " INNER JOIN consignmentInward.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " WHERE consignmentInward.unit.unitId = :unitId AND consignmentInward.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND ( status.id = :statusId OR :statusId = 0 ) "
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND ( vendor.id = :vendorId OR :vendorId = 0 ) "
					+ " AND ( consignmentInward.inwardNumber like :inwardNumber OR :inwardNumber = '' ) "
					//+ "	AND ((:vendorId != 0 AND :vendorName != '') OR (:vendorName = '' OR lower(vendor.name) LIKE :vendorName) OR lower(vendor.code) LIKE :vendorName) "
					+ " AND consignmentInward.isActive = true AND consignmentInward.isDeleted = false "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "	INNER JOIN screenStatus.status as status) " + " ORDER BY consignmentInward.id DESC ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
			query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
			query.setParameter("inwardNumber",request.getInwardNo() != null ? request.getInwardNo() + '%' : "");
			query.setParameter("vendorId", request.getVendorId() != null ? request.getVendorId() : 0);
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
			query.setParameter("screenId", screenId);

			return query.list();
		} catch (Exception e) {
			logger.error("getConsignmentInwardList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public ConsignmentInward getConsignmentInwardDetails(Integer id,Integer unitId) {
		try {
			String hql = " SELECT consignmentInward FROM ConsignmentInward AS consignmentInward "
					+ " WHERE consignmentInward.id = :id AND consignmentInward.unit.unitId = :unitId "
					+ "AND consignmentInward.isActive = true AND consignmentInward.isDeleted = false";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId",unitId!=null ? unitId : sessionService.getUnitId());

			return (ConsignmentInward) query.uniqueResult();
		} catch (Exception e) {
			logger.error("getConsignmentInwardDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public ConsignmentInward saveConsignmentInword(ConsignmentInward consignmentInward) {
		getSession().save(consignmentInward);
		return consignmentInward;
	}

	@Override
	public ConsignmentInward updateConsignmentInword(ConsignmentInward consignmentInward) {
		getSession().update(consignmentInward);
		return consignmentInward;
	}

	@Override
	public Batch saveBatch(Batch batch) {
		getSession().save(batch);
		return batch;
	}

}

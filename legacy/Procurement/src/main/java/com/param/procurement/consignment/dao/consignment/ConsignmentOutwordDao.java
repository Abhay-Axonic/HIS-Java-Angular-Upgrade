package com.param.procurement.consignment.dao.consignment;

import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.procurement.ConsignmentOutward;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.procurement.consignment.dto.consignment.ConsignmentRequest;

@Repository
public class ConsignmentOutwordDao extends AbstractDao<Integer, ConsignmentOutward> implements IConsignmentOutwordDao
{

	final static Logger logger = Logger.getLogger(ConsignmentOutwordDao.class);

	@Autowired
	private SessionService sessionService;

		@Override
		public List<ConsignmentOutward> getConsignmentOutwardList(ConsignmentRequest consignmentOutwardRequest, Integer screenId) {

			String hql = "SELECT consignmentOutward FROM ConsignmentOutward AS consignmentOutward "
					+ " INNER JOIN consignmentOutward.addedBy as user WITH user.unitId = :unitId "
					+ " INNER JOIN consignmentOutward.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN consignmentOutward.vendor As vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
					+ " INNER JOIN consignmentOutward.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " WHERE consignmentOutward.unit.unitId = :unitId AND consignmentOutward.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND ( status.id = :statusId OR :statusId = 0 ) "
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND ( vendor.id = :vendorId OR :vendorId = 0 ) "
					+ "	AND ((:vendorId != 0 AND :vendorName != '') OR (:vendorName = '' OR lower(vendor.name) LIKE :vendorName) OR lower(vendor.code) LIKE :vendorName) "
					+ " AND consignmentOutward.isActive = true AND consignmentOutward.isDeleted = false "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "	INNER JOIN screenStatus.status as status) " + " ORDER BY consignmentOutward.id DESC ";
					
			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(consignmentOutwardRequest.getFromDate()));
			query.setParameter("toDate", Util.getDate(consignmentOutwardRequest.getToDate()));
			query.setParameter("vendorName",consignmentOutwardRequest.getVendorName() != null ? consignmentOutwardRequest.getVendorName().toLowerCase() + '%' : "");
			query.setParameter("storeId", consignmentOutwardRequest.getStoreId() != null ? consignmentOutwardRequest.getStoreId() : 0);
			query.setParameter("vendorId", consignmentOutwardRequest.getVendorId() != null ? consignmentOutwardRequest.getVendorId() : 0);
			query.setParameter("statusId", consignmentOutwardRequest.getStatusId() != null ? consignmentOutwardRequest.getStatusId() : 0);
			query.setParameter("screenId", screenId);
			query.setParameter("unitId", consignmentOutwardRequest.getUnitId()!=null ? consignmentOutwardRequest.getUnitId() : sessionService.getUnitId());
		return query.list();
		}
		
		@Override
		public ConsignmentOutward getConsignmentOutwardDetails(Integer id,Integer unitId)
		{
			try {
				String hql = " SELECT consignmentOutward FROM ConsignmentOutward AS consignmentOutward "
						+ " WHERE consignmentOutward.id = :id AND consignmentOutward.unit.unitId = :unitId "
						+ "AND consignmentOutward.isActive = true AND consignmentOutward.isDeleted = false";

				Query query = getSession().createQuery(hql);
				query.setParameter("id", id);
				query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());

				return (ConsignmentOutward) query.uniqueResult();
			} catch (Exception e)
			{
				logger.error("getConsignmentOutwardDetails failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
		
		@Override
		public ConsignmentOutward saveConsignmentOutward(ConsignmentOutward consignmentOutward) {
			getSession().save(consignmentOutward);
			return consignmentOutward;
			
		}
		
		@Override
		public ConsignmentOutward updateConsignmentOutword(ConsignmentOutward consignmentOutward) {
			getSession().update(consignmentOutward);
			return consignmentOutward;
		}
		
		@Override
		public Batch saveBatch(Batch batch) {
			getSession().save(batch);
			return batch;
		}

		
}

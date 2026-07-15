package com.param.procurement.consignment.dao.ConsignmentPOApproval;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.procurement.ConsignmentPurchaseOrder;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.ConsignmentPOCreationRequest;
import com.param.service.global.SessionService;

@Repository
public class ConsignmentPOApprovalDao extends AbstractDao<Integer, ConsignmentPurchaseOrder> implements IConsignmentPOApprovalDao{

	final static Logger logger = Logger.getLogger(ConsignmentPOApprovalDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<ConsignmentPurchaseOrder> searchConsignmentPurchaseOrderList(ConsignmentPOCreationRequest PurchaseOrdDto,Integer screenId) {
		String hql = "SELECT purchaseOrder FROM ConsignmentPurchaseOrder AS purchaseOrder "
				+ " 	INNER JOIN purchaseOrder.addedBy as user WITH user.unitId = :unitId "
				+ " 	LEFT JOIN purchaseOrder.store as store WITH store.unit.unitId = :unitId "
				+ "		LEFT JOIN purchaseOrder.status as status "
				+ "		LEFT JOIN purchaseOrder.approveStatusId as approvalStatus "
				
				+ " 	LEFT JOIN purchaseOrder.vendor as vendor "
				+ " WHERE purchaseOrder.isActive = true AND purchaseOrder.isDeleted = false AND purchaseOrder.unit.unitId = :unitId "
			    + " 	AND purchaseOrder.addedDate BETWEEN :fromDate AND :toDate "
			    + "		AND (:storeId = 0 OR store.id = :storeId) "
			    + "		AND (:vendorId = 0 OR purchaseOrder.vendor.id = :vendorId) "
			    + " 	AND (:poNumber = '' OR lower(purchaseOrder.poNumber) LIKE :poNumber) "
			    + " 	AND (:vendorName = '' OR lower(purchaseOrder.vendor.name) LIKE :vendorName) "
			    + " 	AND (:statusId = 0 OR status.id = :statusId) "
			    + "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " ORDER BY purchaseOrder.id DESC";
		
		//+ " 	AND (:vendorName = '' OR lower(purchaseOrder.vendor.name) LIKE :vendorName) OR lower(purchaseOrder.vendor.code) LIKE :vendorName )"

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(PurchaseOrdDto.getFromDate()));
		query.setParameter("toDate", Util.getDate(PurchaseOrdDto.getToDate()));
		query.setParameter("storeId",PurchaseOrdDto.getStoreId() != null ? PurchaseOrdDto.getStoreId() : 0);
		query.setParameter("vendorId",PurchaseOrdDto.getVendorId() != null ? PurchaseOrdDto.getVendorId() : 0);
		query.setParameter("poNumber",PurchaseOrdDto.getPoNumber() != null ? PurchaseOrdDto.getPoNumber().toLowerCase() + '%' : "");
		query.setParameter("vendorName",PurchaseOrdDto.getVendorName() !=null ? PurchaseOrdDto.getVendorName().toLowerCase() + '%' : "");
		query.setParameter("statusId", PurchaseOrdDto.getStatusId() != null ? PurchaseOrdDto.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", PurchaseOrdDto.getUnitId()!=null ? PurchaseOrdDto.getUnitId() : sessionService.getUnitId());
		return query.list();
	}
	
	
	 @Override
		public ConsignmentPurchaseOrder getConsignmentPOCreationDetails(Integer id,Integer unitId) {
			try {
				String hql = " SELECT consignmentPurchaseOrder FROM ConsignmentPurchaseOrder AS consignmentPurchaseOrder "
						+ " WHERE consignmentPurchaseOrder.id = :id AND consignmentPurchaseOrder.unit.unitId = :unitId "
						+ "AND consignmentPurchaseOrder.isActive = true AND consignmentPurchaseOrder.isDeleted = false";

				Query query = getSession().createQuery(hql);
				query.setParameter("id", id);
				query.setParameter("unitId", unitId!=null ? unitId :sessionService.getUnitId());

				return (ConsignmentPurchaseOrder) query.uniqueResult();
			} catch (Exception e) {
				logger.error("getConsignmentPOCreationDetails failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	 
	 @Override
		public ConsignmentPurchaseOrder updateConsignmentPO(ConsignmentPurchaseOrder consignmentPurchaseOrder) {
			getSession().update(consignmentPurchaseOrder);
			return consignmentPurchaseOrder;
		}
}

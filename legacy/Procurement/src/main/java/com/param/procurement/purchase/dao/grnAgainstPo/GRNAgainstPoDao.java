package com.param.procurement.purchase.dao.grnAgainstPo;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.procurement.GoodReceiptNote;
import com.param.entity.model.procurement.PurchaseOrder;
import com.param.global.utility.Util;
import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;

@Repository
public class GRNAgainstPoDao extends AbstractDao<Integer, PurchaseOrder> implements IGRNAgainstPoDao {

	@Autowired
	private SessionService sessionService;
	
	@Override
	public List<PurchaseOrder> searchPurchaseOrderList(PurchaseOrderMasterDto PurchaseOrdDto,Integer screenId,List<Integer> statusId) {
		String hql = "SELECT purchaseOrder FROM PurchaseOrder AS purchaseOrder "
				+ " 	INNER JOIN purchaseOrder.addedBy as user WITH user.unitId = :unitId "
				+ " 	LEFT JOIN purchaseOrder.store as store WITH store.unit.unitId = :unitId "
				+ "		LEFT JOIN purchaseOrder.status as status "
				+ "		LEFT JOIN purchaseOrder.approvalStatus as approvalStatus "
				+ " 	LEFT JOIN purchaseOrder.assetType as assetType "
				+ " 	LEFT JOIN purchaseOrder.requestType as requestType "
				+ " 	LEFT JOIN purchaseOrder.vendor as vendor "
				+ " WHERE purchaseOrder.isActive = true AND purchaseOrder.isDeleted = false AND purchaseOrder.unit.unitId = :unitId "
			    + " 	AND purchaseOrder.addedDate BETWEEN :fromDate AND :toDate "
			    + "		AND (:storeId = 0 OR store.id = :storeId) "
			    + " 	AND (:poNumber = '' OR lower(purchaseOrder.poNumber) LIKE :poNumber) "
			    + " 	AND (:vendorId = 0 OR purchaseOrder.vendor.id = :vendorId) "
			    + " 	AND (:vendorName = '' OR lower(purchaseOrder.vendor.name) LIKE :vendorName ) "
			    + " 	AND (:statusId = 0 OR status.id = :statusId) "
			    + "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ "     AND (approvalStatus.id IN (:approvalStatusId))"
				+ " ORDER BY purchaseOrder.id DESC";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(PurchaseOrdDto.getFromDate()));
		query.setParameter("toDate", Util.getDate(PurchaseOrdDto.getToDate()));
		query.setParameter("storeId",PurchaseOrdDto.getStoreId() != null ? PurchaseOrdDto.getStoreId() : 0);
		query.setParameter("poNumber",PurchaseOrdDto.getPoNumber() != null ? PurchaseOrdDto.getPoNumber().toLowerCase() + '%' : "");
		if(PurchaseOrdDto.getVendorId()!=null && PurchaseOrdDto.getVendorId()>0) {
			query.setParameter("vendorId",PurchaseOrdDto.getVendorId() !=null ? PurchaseOrdDto.getVendorId() : 0);
			query.setParameter("vendorName", "");
		}else if(PurchaseOrdDto.getVendorName()!=null){
			query.setParameter("vendorId", 0);
			query.setParameter("vendorName",PurchaseOrdDto.getVendorName() !=null ? PurchaseOrdDto.getVendorName().toLowerCase() + '%' : "");
		}else
		{
			query.setParameter("vendorId", 0);
			query.setParameter("vendorName", "");
		}		
		query.setParameter("statusId", PurchaseOrdDto.getStatusId() != null ? PurchaseOrdDto.getStatusId() : 0);
		query.setParameterList("approvalStatusId", statusId );
		query.setParameter("screenId", screenId);
		query.setParameter("unitId",PurchaseOrdDto.getUnitId()!=null ?PurchaseOrdDto.getUnitId():sessionService.getUnitId());
		return query.list();
	}
	
	@Override
	public List<GoodReceiptNote> getGoodReceiptNote(Integer id)
	{
		String hql = "SELECT goodReceiptNote FROM GoodReceiptNote AS goodReceiptNote "
            + "WHERE goodReceiptNote.purchaseOrder.id = :id  AND goodReceiptNote.isActive = true AND goodReceiptNote.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return query.list();
	}
	
	@Override
	public Double getGoodReceiptNoteAmount(Integer id)
	{
		String hql = "SELECT SUM(goodReceiptNote.totalAmount) AS amount FROM GoodReceiptNote AS goodReceiptNote "
            + " WHERE goodReceiptNote.purchaseOrder.id = :id  AND goodReceiptNote.isActive = true AND goodReceiptNote.isDeleted = false "
            + " GROUP BY goodReceiptNote.purchaseOrder.id";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return (Double)query.uniqueResult();
	}

}

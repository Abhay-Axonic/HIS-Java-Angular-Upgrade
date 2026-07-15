package com.param.procurement.workOrder.dao.workOrderSelection;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.procurement.WoQuotationSelection;
import com.param.global.utility.Util;
import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.workOrder.dto.workOrderSelection.WOSelectionRequest;

@Repository
public class WOSelectionDao extends AbstractDao<Integer, WoQuotationSelection> implements IWOSelectionDao{

	@Autowired
	private SessionService sessionService;

	@Override
	public List<WoQuotationSelection> searchWoQuotationSelectionList(WOSelectionRequest woSelection,Integer screenId) {
		String hql = "SELECT woQuotationSelection FROM WoQuotationSelection AS woQuotationSelection "
				+ "	INNER JOIN woQuotationSelection.addedBy as user WITH user.unitId = :unitId " 
				+ "	LEFT JOIN woQuotationSelection.status as status "
				+ " LEFT JOIN woQuotationSelection.quotationRequest AS quotationRequest "
				+ " LEFT JOIN quotationRequest.store AS store "
				+ " WHERE woQuotationSelection.isActive = true AND woQuotationSelection.isDeleted = false AND woQuotationSelection.unit.unitId = :unitId "
				+ " 	AND woQuotationSelection.addedDate BETWEEN :fromDate AND :toDate "
			    + " 	AND (:statusId = 0 OR status.id = :statusId) "
			    + "		AND (:storeId = 0 OR store.id = :storeId) "
			    + " 	AND ((:workOrderQRNo = '')"
			    + "	          OR (quotationRequest.workOrderQRNo = upper(:workOrderQRNo)) "
			    + "			  OR (quotationRequest.workOrderQRNo = lower(:workOrderQRNo)) "
			    + " 		  OR (quotationRequest.workOrderQRNo like upper(:workOrderQRNoLike) ) "
			    + "			  OR  (quotationRequest.workOrderQRNo like lower(:workOrderQRNoLike) ) )"
			    + "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " ORDER BY woQuotationSelection.id DESC";
		
			
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(woSelection.getFromDate()));
		query.setParameter("toDate", Util.getDate(woSelection.getToDate()));
		query.setParameter("storeId",woSelection.getStoreId() != null ? woSelection.getStoreId() : 0);
		query.setParameter("workOrderQRNo",woSelection.getQrNo() != null ? woSelection.getQrNo()  : "");
		query.setParameter("workOrderQRNoLike",woSelection.getQrNo() != null ? woSelection.getQrNo()+"%"  : "");
		query.setParameter("statusId", woSelection.getStatusId() != null ? woSelection.getStatusId() : 0);
		//query.setParameter("vendorId", woQNego.getVendorId() != null ? woQNego.getVendorId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", sessionService.getUnitId());
		return query.list();
	}
	
	@Override
	public WoQuotationSelection getWoQuotationSelection(Integer id)
	{
		String hql = "SELECT woQuotationSelection FROM WoQuotationSelection AS woQuotationSelection "
            + "WHERE woQuotationSelection.id = :id  AND woQuotationSelection.isActive = true AND woQuotationSelection.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (WoQuotationSelection) query.uniqueResult();
	}
	
	@Override
	public WoQuotationSelection saveWoQuotationSelection(WoQuotationSelection woQuotationSelection) {
		getSession().save(woQuotationSelection);
		return woQuotationSelection;
		
	}
	
	@Override
	public WoQuotationSelection updateWoQuotationSelection(WoQuotationSelection woQuotationSelection) {
		getSession().update(woQuotationSelection);
		return woQuotationSelection;
	}

}

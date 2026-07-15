package com.param.procurement.workOrder.dao.workOrderNegotiation;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.procurement.WoQuotationNegotiation;
import com.param.entity.model.procurement.WoQuotationSelection;
import com.param.entity.model.procurement.WoQuotationVendor;
import com.param.entity.model.procurement.WorkOrderQuotationRequest;
import com.param.global.utility.Util;
import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.workOrder.dto.workOrderNegotiation.WONegotiationRequest;

@Repository
public class WorkOrderNegotiationDao extends AbstractDao<Integer, WoQuotationNegotiation> implements IWorkOrderNegotiationDao{

	@Autowired
	private SessionService sessionService;

	@Override
	public List<WoQuotationNegotiation> searchWorkOrderNegotiationList(WONegotiationRequest woQNego,Integer screenId) {
		String hql = "SELECT woQuotationNegotiation FROM WoQuotationNegotiation AS woQuotationNegotiation "
				+ "	INNER JOIN woQuotationNegotiation.addedBy as user WITH user.unitId = :unitId " 
				+ "	LEFT JOIN woQuotationNegotiation.status as status "
				+ " INNER JOIN woQuotationNegotiation.vendorQuotation AS vendorQuotation "
				+ " LEFT JOIN vendorQuotation.WorkOrderQR AS WorkOrderQR "
				+ " LEFT JOIN WorkOrderQR.store AS store "
				+ " INNER JOIN vendorQuotation.vendor AS vendor "
				+ " WHERE woQuotationNegotiation.isActive = true AND woQuotationNegotiation.isDeleted = false AND woQuotationNegotiation.unit.unitId = :unitId "
				+ " 	AND woQuotationNegotiation.addedDate BETWEEN :fromDate AND :toDate "
			    + " 	AND (:statusId = 0 OR status.id = :statusId) "
			   // + "		AND (:storeId = 0 OR store.id = :storeId) "
			    //+"      AND (:vendorId = 0 OR vendor.id = :vendorId) "
			    + "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " ORDER BY woQuotationNegotiation.id DESC";
		
			
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(woQNego.getFromDate()));
		query.setParameter("toDate", Util.getDate(woQNego.getToDate()));
	//	query.setParameter("storeId",woQNego.getStoreId() != null ? woQNego.getStoreId() : 0);
		query.setParameter("statusId", woQNego.getStatusId() != null ? woQNego.getStatusId() : 0);
		//query.setParameter("vendorId", woQNego.getVendorId() != null ? woQNego.getVendorId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", woQNego.getUnitId()!=null ? woQNego.getUnitId() : sessionService.getUnitId());
		return query.list();
	}
	
	@Override
	public WoQuotationNegotiation getWoQuotationNegotiation(Integer id)
	{
		String hql = "SELECT woQuotationNegotiation FROM WoQuotationNegotiation AS woQuotationNegotiation "
            + "WHERE woQuotationNegotiation.id = :id  AND woQuotationNegotiation.isActive = true AND woQuotationNegotiation.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (WoQuotationNegotiation) query.uniqueResult();
	}
	
	@Override
	public List<WoQuotationVendor> getWoQuotationVendor(Integer id)
	{
		String hql = "SELECT woQuotationVendor FROM WoQuotationVendor AS woQuotationVendor "
            + "WHERE woQuotationVendor.WorkOrderQR.id = :id  AND woQuotationVendor.isActive = true AND woQuotationVendor.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}
	
	@Override
	public WorkOrderQuotationRequest getWoWorkOrderQuotationRequest(Integer id)
	{
		String hql = "SELECT workOrderQR FROM WorkOrderQuotationRequest AS workOrderQR "
            + "WHERE workOrderQR.id = :id  AND workOrderQR.isActive = true AND workOrderQR.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (WorkOrderQuotationRequest) query.uniqueResult();
	}
	
	
	@Override
	public WoQuotationSelection saveWoQuotationSelection(WoQuotationSelection woQuotationSelection) {
		getSession().save(woQuotationSelection);
		return woQuotationSelection;
		
	}
	
	
	@Override
	public WoQuotationNegotiation saveWoQuotationNegotiation(WoQuotationNegotiation woQuotationNegotiation) {
		getSession().save(woQuotationNegotiation);
		return woQuotationNegotiation;
		
	}
	
	@Override
	public WoQuotationNegotiation updateWoQuotationNegotiation(WoQuotationNegotiation woQuotationNegotiation) {
		getSession().update(woQuotationNegotiation);
		return woQuotationNegotiation;
	}
}

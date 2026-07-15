package com.param.procurement.purchase.dao.itemRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.procurement.ItemRequest;
import com.param.entity.model.procurement.ItemRequestDetail;
import com.param.global.utility.Util;
import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.purchase.dto.itemRequest.ItemRequestDto;

@Repository("itemRequestDao")
public class ItemRequestDao extends AbstractDao<Integer, ItemRequest> implements IItemRequestDao{

	@Autowired
	private SessionService sessionService;

	@Override
	public List<ItemRequest> searchItemRequest(ItemRequestDto request,Integer screenId){
		  String hql = "SELECT itemRequest FROM ItemRequest AS itemRequest "
					//+ " 	INNER JOIN itemRequest.addedBy as user WITH user.unitId = :unitId "
					+ " 	LEFT JOIN itemRequest.status as status "
					+ " 	LEFT JOIN itemRequest.status as approvalStatus "
					+ " 	LEFT JOIN itemRequest.assetType as assetType "					
					+ " WHERE itemRequest.isActive = true "
					+ "		AND itemRequest.isDeleted = false "
					+ "		AND itemRequest.unit.unitId = :unitId "
					+ "		AND itemRequest.addedDate BETWEEN :fromDate AND :toDate "
					+ " 	AND (:statusId = 0 OR status.id = :statusId) "
					+ " 	AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " 					INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
					+ "						INNER JOIN screenStatus.status as status) "
					+ " ORDER BY itemRequest.id DESC ";			
					
					Query query = getSession().createQuery(hql);
					query.setParameter("fromDate", Util.getDate(request.getFromDate()));
					query.setParameter("toDate", Util.getDate(request.getToDate()));
					query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
					query.setParameter("screenId", screenId);
					query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
                    return query.list();
	}
		
	@Override
	public List<ItemRequest> searchItemCreationPending(ItemRequestDto request,Integer screenId){
		  String hql = "SELECT itemRequest FROM ItemRequest AS itemRequest "
				//	+ " 	INNER JOIN itemRequest.unit.unitId = :unitId "
					+ " 	LEFT JOIN itemRequest.status as status "
					+ " 	LEFT JOIN itemRequest.approvalStatus as approvalStatus "
					+ " 	LEFT JOIN itemRequest.assetType as assetType "					
					+ " WHERE itemRequest.isActive = true AND itemRequest.isDeleted = false AND itemRequest.unit.unitId = :unitId "
					+ "		AND itemRequest.addedDate BETWEEN :fromDate AND :toDate "
					+ " 	AND (:statusId = 0 OR status.id = :statusId) "
					+ " 	AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " 					INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
					+ "						INNER JOIN screenStatus.status as status) "
					+ "     AND (approvalStatus.id IN (:approvalStatusId))"
					+ " ORDER BY itemRequest.id DESC ";			
					
					Query query = getSession().createQuery(hql);
					query.setParameter("fromDate", Util.getDate(request.getFromDate()));
					query.setParameter("toDate", Util.getDate(request.getToDate()));
					query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
					query.setParameter("screenId", screenId);
					query.setParameterList("approvalStatusId", new ArrayList<Integer>(Arrays.asList(com.param.common.constants.Status.Approved.getStatus(),
							com.param.common.constants.Status.PartiallyApproved.getStatus())));
					query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
                    return query.list();
	}

	@Override
	public ItemRequest getItemRequest(Integer id) {
		String hql = "SELECT itemRequest FROM ItemRequest AS itemRequest WHERE itemRequest.id = :id  AND itemRequest.isActive = true AND itemRequest.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (ItemRequest) query.uniqueResult();
	}

	@Override
	public List<ItemRequestDetail> getItemRequestDetail(Integer itemRequestId) {
		String hql = "SELECT itemRequestDetail FROM ItemRequestDetail AS itemRequestDetail \r\n"
				+ "		INNER JOIN itemRequestDetail.itemRequest AS itemRequest WITH itemRequest.isActive = true AND itemRequest.isDeleted = false \r\n"
				+ "WHERE itemRequest.id = :itemRequestId AND itemRequestDetail.isActive = true AND itemRequestDetail.isDeleted = false \r\n"
				+ "ORDER BY itemRequestDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("itemRequestId", itemRequestId);
		return query.list();
	}
	
	@Override
	public ItemRequest saveItemRequest(ItemRequest itemRequest) {
		  getSession().save(itemRequest);
			return itemRequest;
	}
	
	
	@Override
	public ItemRequest updateItemRequest(ItemRequest itemRequest) {
		getSession().update(itemRequest);
		return itemRequest;
	}

}

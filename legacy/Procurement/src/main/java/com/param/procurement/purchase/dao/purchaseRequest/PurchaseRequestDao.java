package com.param.procurement.purchase.dao.purchaseRequest;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.procurement.PurchaseRequest;
import com.param.entity.model.procurement.PurchaseRequestDetail;
import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*import com.param.procurement.purchase.dto.PurchaseRequestDto;*/
/*import com.param.procurement.purchase.model.ItemRequest;*/
/*import com.param.procurement.purchase.model.PurchaseRequest;*/

@Repository
public class PurchaseRequestDao extends AbstractDao<Integer, PurchaseRequest> implements IPurchaseRequestDao {

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private SessionService sessionService;

	/*@Override
	@Transactional
	public List<PurchaseRequest> retrivePurchaseRequestData() {
		Criteria cr = sessionFactory.getCurrentSession().createCriteria(PurchaseRequest.class);
		cr.addOrder(Order.asc("purchaseRequestId"));
		List<PurchaseRequest> results = Util.castList(PurchaseRequest.class, cr.list());
		return results;
	}

	@Override
	@Transactional
	public boolean createPurchaseRequest(PurchaseRequestDto purchaseRequestDto) {
		Date date = TimeZoneComponent.getDateByzone(unitId);	
		PurchaseRequest purchaseRequest = purchaseRequestDto.getPurchaseRequest();
		purchaseRequestDto.getPurchaseRequest().setAddedDate(date);
		purchaseRequestDto.getPurchaseRequest().setUpdatedDate(date);
		purchaseRequestDto.getPurchaseRequest().setIsDeleted(false);
				
		sessionFactory.getCurrentSession().save(purchaseRequest);
		
		for (ItemRequest itemRequest : purchaseRequestDto.getItemRequests()) {
			itemRequest.setPurchaseRequestId(purchaseRequest.getPurchaseRequestId());
			sessionFactory.getCurrentSession().save(itemRequest);
		}
		return (purchaseRequest.getPurchaseRequestId() > 0);		 
	}

	@Override
	@Transactional
	public boolean purchaseRequestApproval(PurchaseRequestDto purchaseRequestDto) {
		String sql = "update PurchaseRequest set statusId=:statusId where purchaseRequestId =:purchaseRequestId";
		Query query = sessionFactory.getCurrentSession().createQuery(sql);
		query.setParameter("statusId", purchaseRequestDto.getPurchaseRequest().getStatusId());
		query.setParameter("purchaseRequestId", purchaseRequestDto.getPurchaseRequest().getPurchaseRequestId());		 
		return (query.executeUpdate()>0);
	}*/


	@Override
	public List<PurchaseRequest> purchaseRequestSearch(com.param.procurement.purchase.dto.purchaseRequest.PurchaseRequest search, Integer screenId) {

		List<com.param.entity.model.procurement.PurchaseRequest> purchaseRequests = new ArrayList<PurchaseRequest>();

		String hql = "SELECT distinct purchaseRequest FROM PurchaseRequest AS purchaseRequest \r\n" +
				 	 " INNER JOIN purchaseRequest.requestingStore as store WITH store.isDeleted = false AND store.isActive = true "+
					 " INNER JOIN store.userStoreMappersList as userStore WITH userStore.isActive = true AND userStore.isDeleted = false "+
					 "WHERE " +
					 "DATE(purchaseRequest.addedDate) BETWEEN :fromDate AND :toDate "+
					 "AND (:storeId = 0 OR purchaseRequest.store.storeId = :storeId) " +
					 "AND (:requestingStoreId = 0 OR purchaseRequest.requestingStore.storeId = :requestingStoreId) " +
					 "AND (:screenId = 0 OR purchaseRequest.screen.id = :screenId) " +
					 "AND (:statusId = 0 OR purchaseRequest.status.id = :statusId) " +
					 "AND (:priorityId = 0 OR purchaseRequest.priority.priorityId = :priorityId)   AND (purchaseRequest.unit.unitId=:unitId)  " +
					 "ORDER BY purchaseRequest.id DESC\r\n";


		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", search.getFromDate());
		query.setParameter("toDate", search.getToDate());
		query.setParameter("screenId", screenId);
		query.setParameter("storeId", search.getStoreId() != null ? search.getStoreId() : 0);
		query.setParameter("requestingStoreId", search.getRequestingStoreId() != null ? search.getRequestingStoreId() : 0);
		query.setParameter("statusId", search.getStatusId() != null ? search.getStatusId() : 0);
		query.setParameter("priorityId", search.getPriorityId() != null ? search.getPriorityId() : 0);
		//query.setParameter("userId", sessionService.getUserId());
		query.setParameter("unitId", search.getUnitId()!=null ? search.getUnitId() : sessionService.getUnitId());


		purchaseRequests = query.list();

		return purchaseRequests;
	}

	@Override
	public List<PurchaseRequestDetail> purchaseRequestDetails(Long purchaseRequestId) {
		String hql = "SELECT purchaseRequestDetail FROM PurchaseRequestDetail AS purchaseRequestDetail \r\n" +
                    "WHERE purchaseRequestDetail.purchaseRequest.id = :id \n" +
					"AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.isDeleted = false " +
                    "ORDER BY purchaseRequestDetail.id \r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", purchaseRequestId);

		return query.list();
	}

	@Override
	public PurchaseRequest savePurchaseRequest(PurchaseRequest purchaseRequest) {
		Serializable id	= getSession().save(purchaseRequest);
		//purchaseRequest.setPurchaseNo("PR-" + id);

		return purchaseRequest;
	}

	@Override
	public PurchaseRequest getPurchaseRequest(Integer id) {
		String hql = "SELECT purchaseRequest FROM PurchaseRequest AS purchaseRequest WHERE purchaseRequest.id = :id  AND purchaseRequest.isActive = true AND purchaseRequest.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id.longValue());

		return (PurchaseRequest) query.uniqueResult();
	}

	@Override
	public PurchaseRequestDetail getPurchaseRequestDetail(Integer id, Integer pr_id) {
		String hql = "SELECT purchaseRequestDetail FROM PurchaseRequestDetail AS purchaseRequestDetail WHERE purchaseRequestDetail.id = :id AND purchaseRequestDetail.purchaseRequest.id = :pr_id   AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id.longValue());
		query.setParameter("pr_id", pr_id.longValue());

		return (PurchaseRequestDetail) query.uniqueResult();
	}

	@Override
	public PurchaseRequest updatePurchaseRequest(PurchaseRequest purchaseRequest) {
		getSession().update(purchaseRequest);

		return purchaseRequest;
	}
	
	@Override
	public Integer deletePurchaseRequestDetails(Long id, List<Integer> item_id) {
		String hql = "update PurchaseRequestDetail set PurchaseRequestDetail.isDeleted = true where PurchaseRequestDetail.purchaseRequest.id = :id and PurchaseRequestDetail.id not in (:item_id)";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("item_id", item_id);

		return query.executeUpdate();
	}
	
	@Override
	public Long getLeaseQunatity(Integer storeId, Integer itemId) {

		String hql = "SELECT SUM (CASE WHEN purchaseOrderDetail.status.id = :statusId THEN COALESCE((purchaseOrderDetail.poLeaseQuantity + purchaseOrderDetail.bonusLeaseQuantity),0 )"
				+ " ELSE COALESCE((purchaseRequestDetail.leaseQuantity),0) END ) "
				+ " FROM PurchaseRequestDetail AS purchaseRequestDetail  "
				+ " INNER JOIN purchaseRequestDetail.purchaseRequest AS purchaseRequest WITH purchaseRequest.isDeleted = false AND purchaseRequest.isActive = true AND purchaseRequest.unit.unitId = :unitId \r\n"
				+ " LEFT JOIN purchaseRequestDetail.purchaseOrderDetail AS purchaseOrderDetail WITH purchaseOrderDetail.isDeleted = false AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.unit.unitId = :unitId "
				+ " WHERE purchaseRequest.store.storeId = :storeId AND purchaseRequestDetail.item.id = :itemId AND purchaseRequestDetail.goodReceiptNoteDetail is null  \r\n"
				+ " AND purchaseRequestDetail.isDeleted = false AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.unit.unitId = :unitId "
				+ " AND purchaseRequestDetail.status.id != :cancelStatusId \r\n";

		Query query = getSession().createQuery(hql);
		query.setParameter("statusId", 5);
		query.setParameter("cancelStatusId", 9);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("unitId", sessionService.getUnitId());

		return  (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0L);

	}
}

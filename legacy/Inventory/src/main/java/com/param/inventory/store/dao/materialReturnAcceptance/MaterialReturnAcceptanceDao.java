package com.param.inventory.store.dao.materialReturnAcceptance;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.global.common.ServiceException;
import com.param.entity.model.inventory.Reject;
import com.param.entity.model.inventory.Return;
import com.param.entity.model.inventory.ReturnDetail;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.global.utility.Util;
import com.param.inventory.store.dto.materialReturn.MaterialResponse;
import com.param.inventory.store.dto.materialReturn.MaterialReurnRequest;
import com.param.service.global.SessionService;

@Repository
public class MaterialReturnAcceptanceDao extends AbstractDao<Integer, Return> implements IMaterialReturnAcceptanceDao {

	private static final Logger logger = Logger.getLogger(MaterialReturnAcceptanceDao.class);

	@Autowired
	private SessionService sessionService;
	
    @Autowired
    SessionFactory sessionFactory;

	@Override
	public List<MaterialResponse> searchMaterialReturnAcceptanceList(MaterialReurnRequest request, Integer screenId) {
		String hql = "SELECT return.id AS id, return.addedDate AS returnDate, return.returnNo AS returnNo, "
				+ "		fromStore.storeId AS fromStoreId, fromStore.storeName AS fromStore, toStore.storeId AS toStoreId, toStore.storeName AS toStore, "
				+ "		return.isConsignment AS isConsignment, user.userName AS returnBy, approvalStatus.status AS approvalStatus, "
				+ "     return.remark AS remark, status.status AS status, return.nearByExpiry AS nearByExpiry "
				+ " FROM com.param.entity.model.inventory.Return AS return "
				+ "		INNER JOIN return.addedBy as user "
				+ "		INNER JOIN return.fromStore AS fromStore "
				+ "		INNER JOIN return.toStore AS toStore "
				+ "		INNER JOIN return.status AS status "
				+ "		INNER JOIN return.approvalStatus AS approvalStatus WITH approvalStatus.isActive = true AND approvalStatus.isDeleted = false "
				+ "	WHERE return.isActive = true AND return.isDeleted = false AND return.unit.unitId = :unitId "
				+ " 	AND return.addedDate BETWEEN :fromDate AND :toDate "
				+ "     AND (:fromStoreId = 0 OR fromStore.storeId = :fromStoreId) "
				+ "     AND (:toStoreId = 0 OR   toStore.storeId = :toStoreId  ) "
				+ "		AND toStore.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "							WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "							 AND userStore.store.isDeleted = false AND userStore.store.isActive = true) "
				+ "     AND (:returnNo = '' OR lower(return.returnNo) LIKE :returnNo) "
				+ "     AND (:statusId = 0 OR status.id = :statusId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) " 
				+ "ORDER BY return.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(request.getFromDate()));
		query.setParameter("toDate", Util.getDate(request.getToDate()));
		query.setParameter("fromStoreId", request.getFromStoreId() != null ? request.getFromStoreId() : 0);
		query.setParameter("toStoreId", request.getToStoreId() != null ? request.getToStoreId() : 0);
		query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("returnNo", request.getReturnNo() != null ? '%'+request.getReturnNo().toLowerCase() + '%' : "");
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId());
		query.setParameter("userId",request.getUserId() != null  ? request.getUserId() : sessionService.getUserId());
		query.setResultTransformer(Transformers.aliasToBean(MaterialResponse.class));

		return query.list();
	}

	@Override
	public Return getReturn(Integer id) {
		String hql = " SELECT return FROM com.param.entity.model.inventory.Return AS return "
				+ "	WHERE return.id = :id AND return.isActive = true AND return.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (Return) query.uniqueResult();
	}

	@Override
	public List<ReturnDetail> getReturnDetail(Integer id) {
		
		String hql = "SELECT returnDetail FROM com.param.entity.model.inventory.ReturnDetail AS returnDetail \r\n"
				+ "	INNER JOIN returnDetail.mreturn AS return WITH return.isActive = true AND return.isDeleted = false \r\n"
				+ " WHERE return.id = :id AND returnDetail.isActive = true AND returnDetail.isDeleted = false \r\n"
				+ " ORDER BY returnDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}
	
	
	@Override
	public Return updateReturn(Return returnData) {
		getSession().update(returnData);
		return returnData;
	}

	@Override
	public Reject save(Reject reject) {
		getSession().save(reject);
		return reject;	
	}

	@Override
	public StoreStock saveStoreStock(StoreStock storeStock) {
		getSession().save(storeStock);
		return storeStock;
	}

	@Override
	public Integer updateIntransitQty(Integer detailsId, Integer screenId) {
        try {
            try {
            	
            	Integer count = 0;
                String query = "select id as \"stockTransactionId\" from inventory.t_stock_transaction where  transaction_details_id =:detailsId and doc_id =:screenId and is_transit = true order by id desc limit 1 ";

                MaterialReurnRequest dto = (MaterialReurnRequest) sessionFactory.getCurrentSession()
                        .createSQLQuery(query).setParameter("detailsId", detailsId).setParameter("screenId", screenId)
                        .setResultTransformer(Transformers.aliasToBean(MaterialReurnRequest.class)).uniqueResult();
                
                if(dto!= null) {
                    Integer result = getSession()
                            .createSQLQuery(" update inventory.t_stock_transaction "
                            		+ "  set is_transit  = false where id =:id ")
                            .setParameter("id", dto.getStockTransactionId())
                            .executeUpdate();
                    count= 1;
                }
                return count;	
            } catch (HibernateException he) {
                throw new HibernateException(he.getMessage());
            } catch (Exception e) {
                logger.error("Error while updating in-transit quantity", e);
                throw new ServiceException(e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Error while updating in-transit quantity for the return detail", e);
            throw new ServiceException(e.getMessage());
        }
	}

	@Override
	public void updateIntransitByBatchIdQty(Integer batchId, Integer totalRejectedQty, Integer screenId) {
	      try {
	            try {
	            	
	                String query = "select id as \"stockTransactionId\" from inventory.t_stock_transaction where  batch_id =:batchId and doc_id =:screenId and is_transit = true and out_qty=:totalRejectedQty  order by id desc limit 1 ";

	                MaterialReurnRequest dto = (MaterialReurnRequest) sessionFactory.getCurrentSession()
	                        .createSQLQuery(query).setParameter("batchId", batchId).setParameter("screenId", screenId).setParameter("totalRejectedQty", totalRejectedQty)
	                        .setResultTransformer(Transformers.aliasToBean(MaterialReurnRequest.class)).uniqueResult();
	                
	                if(dto!= null) {
	                    Integer result = getSession()
	                            .createSQLQuery(" update inventory.t_stock_transaction "
	                            		+ "  set is_transit  = false where id =:id ")
	                            .setParameter("id", dto.getStockTransactionId())
	                            .executeUpdate();
	                }
	            } catch (HibernateException he) {
	                throw new HibernateException(he.getMessage());
	            } catch (Exception e) {
	                logger.error("Error while updating in-transit quantity by batch id", e);
	                throw new ServiceException(e.getMessage());
	            }
	        } catch (Exception e) {
	            logger.error("Error while updating in-transit quantity for the batch rejection", e);
	            throw new ServiceException(e.getMessage());
	        }

	}

}

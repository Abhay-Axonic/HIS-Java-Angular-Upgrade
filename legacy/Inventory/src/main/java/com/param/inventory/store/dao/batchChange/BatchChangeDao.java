package com.param.inventory.store.dao.batchChange;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchExp;
import com.param.entity.model.inventory.BatchExpDetail;
import com.param.entity.model.inventory.BatchUomMapper;
import com.param.global.utility.Util;
import com.param.inventory.store.dto.batchChange.BatchChangeRequest;
import com.param.inventory.store.dto.batchChange.BatchChangeResponse;
import com.param.inventory.store.dto.batchChange.BatchResponse;

@Repository
public class BatchChangeDao extends AbstractDao<Integer, BatchExp> implements IBatchChangeDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public BatchExp saveBatchChange(BatchExp batchExp) {
		getSession().save(batchExp);
		return batchExp;
	}

	@Override
	public List<BatchChangeResponse> getBatchChangeList(BatchChangeRequest request, Integer screenId) {

		String hql = "SELECT batchExp.id AS id, batchExp.adjustmentNumber AS adjustmentNo, batchExp.addedDate AS adjustmentDate, store.storeId AS storeId, store.storeName AS storeName, "
				+ "		user.userName AS adjustmentBy, status.id AS statusId, status.status AS status, approvalStatus.id AS approvalStatusId, approvalStatus.status AS approvalStatus "
				+ "	FROM BatchExp AS batchExp "
				// + " INNER JOIN batchExp.store AS store WITH store.storeId = :storeId AND
				// store.isActive = true AND store.isDeleted = false "
				+ "		INNER JOIN batchExp.store AS store WITH store.isActive = true AND store.isDeleted = false "
				+ "		INNER JOIN batchExp.addedBy as user WITH user.status = 'A' "
				+ "		INNER JOIN batchExp.status AS status WITH status.isActive = true AND status.isDeleted = false "
				+ "		INNER JOIN batchExp.approvalStatus AS approvalStatus WITH approvalStatus.isActive = true AND approvalStatus.isDeleted = false "
				+ "	WHERE batchExp.isActive = true AND batchExp.isDeleted = false AND batchExp.unit.unitId = :unitId "
				+ "		AND batchExp.addedDate BETWEEN :fromDate AND :toDate "
				+ " 	AND (:storeId = 0 OR store.storeId = :storeId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId AND screen.isDeleted = false "
				+ "										AND screen.isActive = true "
				+ "								INNER JOIN screenStatus.status as status WITH status.isDeleted = false AND status.isActive = true "
				+ "						  WHERE screenStatus.isActive = true AND screenStatus.isDeleted = false) "
				+ "	ORDER BY batchExp.id DESC ";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(request.getFromDate()));
		query.setParameter("toDate", Util.getDate(request.getToDate()));
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(BatchChangeResponse.class));

		return query.list();
	}

	@Override
	public BatchChangeResponse getBatchChange(Integer id) {

		String hql = "SELECT batchExp.id AS id, batchExp.adjustmentNumber AS adjustmentNo, batchExp.addedDate AS adjustmentDate, store.storeId AS storeId, store.storeName AS storeName, "
				+ "		user.userName AS adjustmentBy, status.id AS statusId, status.status AS status, approvalStatus.id AS approvalStatusId, approvalStatus.status AS approvalStatus "
				+ "	FROM BatchExp AS batchExp "
				+ "		INNER JOIN batchExp.store AS store WITH store.isActive = true AND store.isDeleted = false "
				+ "		INNER JOIN batchExp.addedBy as user WITH user.status = 'A' "
				+ "		INNER JOIN batchExp.status AS status WITH status.isActive = true AND status.isDeleted = false "
				+ "		INNER JOIN batchExp.approvalStatus AS approvalStatus WITH approvalStatus.isActive = true AND approvalStatus.isDeleted = false "
				+ "	WHERE batchExp.id = :id AND batchExp.isActive = true AND batchExp.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(BatchChangeResponse.class));

		return (BatchChangeResponse) query.uniqueResult();
	}

	@Override
	public List<BatchResponse> getBatchChangeDetails(Integer id) {

		String hql = "SELECT batchExpDetail.id AS id, item.id AS itemId,item.code AS itemCode, item.name AS itemName, uom.uomType.type AS uomType, uom.uomUnit.units AS uomUnit, "
				+ "		batch.id AS batchId, batchExpDetail.oldBatchNo AS oldBatchNo, batchExpDetail.oldBatchExpiry AS oldBatchExpiry, batchExpDetail.oldMrp AS oldMrp, "
				+ "		batchExpDetail.newBatchNo AS newBatchNo, batchExpDetail.newBatchExpiry AS newBatchExpiry, batchExpDetail.newMrp AS newMrp, status.id AS statusId, "
				+ "     batchExpDetail.mrpUomType.id AS mrpUomTypeId , batchExpDetail.lcop as lcop ,  batchExpDetail.cop as cop, item.isThreeUOMApplicable as isThreeUOMApplicable "
				+ "	FROM BatchExp AS batchExp "
				+ "		INNER JOIN batchExp.batchExpDetails AS batchExpDetail WITH batchExpDetail.isActive = true AND batchExpDetail.isDeleted = false "
				+ "		INNER JOIN batchExpDetail.status AS status WITH status.isActive = true AND status.isDeleted = false "
				+ "		INNER JOIN batchExpDetail.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false "
				+ "		INNER JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
				+ "		INNER JOIN batch.batchUomList AS uom WITH uom.ipUom = true AND uom.isActive = true AND uom.isDeleted = false "
				+ "	WHERE batchExp.id = :id AND batchExp.isActive = true AND batchExp.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(BatchResponse.class));

		return query.list();
	}

	@Override
	public BatchExp getBathExp(Integer id) {
		String hql = "SELECT batchExp FROM BatchExp AS batchExp "
				+ "	WHERE batchExp.id = :id AND batchExp.isActive = true AND batchExp.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (BatchExp) query.uniqueResult();
	}

	@Override
	public BatchExp updateBatchChange(BatchExp batchExp) {
		getSession().update(batchExp);

		return batchExp;
	}

	@Override
	public void updateBatch(Batch b) {

		getSession().update(b);
 
	
	}

	@Override
	public Batch getbatch(Integer id , BatchExpDetail details , Double mrpLeaseQuantity, Double lcopLeaseQuantity , Double copLeaseQuantity ) {
		String sql = "  update inventory.t_batch set batch_no=:batchNo , expiry_date=:expDate , lending_price=:p, selling_price=:p ,rate=:r , actual_lending_price=:lcop , lcop=:lcop , cop=:cop where id="+id;

		Query query = getSession().createSQLQuery(sql);
		query.setParameter("batchNo", details.getNewBatchNo()!=null ? details.getNewBatchNo():null);
		query.setParameter("expDate", details.getNewBatchExpiry()!=null ? details.getNewBatchExpiry() :null);
		query.setParameter("p", mrpLeaseQuantity);
		query.setParameter("r", mrpLeaseQuantity);
		query.setParameter("cop", copLeaseQuantity);
		query.setParameter("lcop", lcopLeaseQuantity);
        query.executeUpdate();	
        
        
        if(details.getNewBatchNo()!=null && details.getNewBatchExpiry()!=null)
        {
        	/*Query query = getSession().createSQLQuery(sql);
    		query.setParameter("batchNo", details.getNewBatchNo());
    		query.setParameter("expDate", details.getNewBatchExpiry());
    		query.setParameter("p", mrpLeaseQuantity);
    		query.setParameter("r", mrpLeaseQuantity);
    		query.setParameter("cop", copLeaseQuantity);
    		query.setParameter("lcop", lcopLeaseQuantity);
            query.executeUpdate();	*/
        }
        else
        {/*
        	String sql1 = "  update inventory.t_batch set selling_price=:p ,rate=:r  where id="+id;

    		Query query1 = getSession().createSQLQuery(sql1); 
    		query1.setParameter("p", mrpLeaseQuantity);
    		query1.setParameter("r", mrpLeaseQuantity);
            query1.executeUpdate();
        */}
    	
		return null;
	}

	@Override
	public List<BatchExpDetail> getBatchChangeDetailsById(Integer id) {
		String hql = "SELECT batchExpDetail FROM BatchExpDetail as batchExpDetail   "
				+ "	WHERE batchExpDetail.batchExp.id = :id AND batchExpDetail.isActive = true AND batchExpDetail.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return  query.list();
	}

	@Override
	public Integer getBatchId(Integer id) {
		String hql = "  select batch_id from inventory.t_batch_exp_detail where id = "+id;

		Query query = getSession().createSQLQuery(hql); 

		return  (Integer) query.uniqueResult();
	}

	@Override
	public List<BatchUomMapper> getBatchUomMapper(Integer batchId) {
		String hql = "SELECT batchUomMapper FROM BatchUomMapper as batchUomMapper   "
				+ "	WHERE batchUomMapper.batch.id = :id AND batchUomMapper.isActive = true AND batchUomMapper.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", batchId.longValue());

		return  query.list();
	}

	@Override
	public Batch getbatchWithOutLcop(Integer batchId, BatchExpDetail details, Double mrpLeaseQuantity,
			Double copLeaseQuantity) {
		String sql = "  update inventory.t_batch set batch_no=:batchNo , expiry_date=:expDate , lending_price=:p, selling_price=:p ,rate=:r  , cop=:cop where id="+batchId;

		Query query = getSession().createSQLQuery(sql);
		query.setParameter("batchNo", details.getNewBatchNo()!=null ? details.getNewBatchNo():null);
		query.setParameter("expDate", details.getNewBatchExpiry()!=null ? details.getNewBatchExpiry() :null);
		query.setParameter("p", mrpLeaseQuantity);
		query.setParameter("r", mrpLeaseQuantity);
		query.setParameter("cop", copLeaseQuantity); 
        query.executeUpdate();	
         
    	
		return null;
	}
}

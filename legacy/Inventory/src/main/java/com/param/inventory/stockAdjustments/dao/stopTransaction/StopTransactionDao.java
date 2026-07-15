package com.param.inventory.stockAdjustments.dao.stopTransaction;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.StopTransaction;
import com.param.entity.model.inventory.StopTransactionItemDetail;
import com.param.entity.model.master.ProductCategory;
import com.param.entity.model.master.Store;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionProcessResponse;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionRequest;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionResponse;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionSearchResponse;

@Repository("StopTransactionDao")
public class StopTransactionDao extends AbstractDao<Integer, StopTransaction> implements IStopTransactionDao {

	private static final Logger logger = Logger.getLogger(StopTransactionDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public StopTransaction saveAddNewStopTransaction(StopTransaction stopTransaction) {
		getSession().save(stopTransaction);
		return stopTransaction;
	}

	@Override
	public StopTransaction getStopTransaction(Integer id) {

		String hql = "SELECT stopTransaction FROM com.param.entity.model.inventory.StopTransaction AS stopTransaction "
				+ " WHERE stopTransaction.id = :id  AND stopTransaction.isActive = true AND stopTransaction.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (StopTransaction) query.uniqueResult();
	}

	@Override
	public StopTransaction updateStopTransaction(StopTransaction stopTransaction) {
		getSession().saveOrUpdate(stopTransaction);
		return stopTransaction;
	}

	@Override
	public List<StopTransactionResponse> searchStopTransactionList(StopTransactionRequest request, Integer screenId) {
		String hql = "SELECT stopTransaction.id AS id, stopTransaction.addedDate AS requestDate, stopTransaction.requestNo AS requestNo, "
				+ "		store.storeId AS storeId, store.storeName AS store, stopTransaction.startDateTime AS startDateTime, "
				+ "stopTransaction.stopDateTime AS stopDateTime, user.userName as requestedBy, status.id AS statusId , status.status AS status "
				+ " FROM StopTransaction AS stopTransaction " + "		INNER JOIN stopTransaction.store AS store "
				+ "		INNER JOIN stopTransaction.addedBy as user "
				+ "		INNER JOIN stopTransaction.status AS status "
				+ "	WHERE stopTransaction.isActive = true AND stopTransaction.isDeleted = false AND stopTransaction.unit.unitId = :unitId  AND stopTransaction.organization.organizationId =:orgId"
				+ " 	AND stopTransaction.startDateTime BETWEEN :fromDate AND  :toDate"
				+ " 	AND stopTransaction.stopDateTime BETWEEN :fromDate AND  :toDate"
//				+ " 	AND stopTransaction.addedDate BETWEEN :fromDate AND :toDate "
				+ "     AND (:statusId = 0 OR status.id = :statusId) "
				+ "		AND (:storeId = 0 OR  store.storeId = :storeId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "		INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "		INNER JOIN screenStatus.status as status) " + "		ORDER BY stopTransaction.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(request.getFromDate()));
		query.setParameter("toDate", Util.getDate(request.getToDate()));
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setParameter("orgId", request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId());
		query.setResultTransformer(Transformers.aliasToBean(StopTransactionResponse.class));

		return query.list();
	}

	@Override 
	public List<StopTransactionSearchResponse> searchStopTransactionNewList(StopTransactionRequest request,
			Integer screenId) {

		String hql = "SELECT item.id AS itemId, item.name AS itemName, item.code AS itemCode, batch.id AS batchId, batch.batchNo AS batchNo, batch.cop as cop,"
				+ " batch.rate as mrp, batch.expiryDate AS expiryDate, productCategory.id AS productCategoryId, productCategory.category AS productCategory,"
				+ " assetCategory.id AS assetCategoryId, assetCategory.category AS assetCategory, uomType.id AS uomTypeId, uomType.type AS uomType,"
				+ " uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, store.storeId AS storeId, storeStock.availableQty  as avilableQuantity,(batch.rate * storeStock.availableQty) AS totalMrp "
				+ " FROM com.param.entity.model.master.Item AS item "
				+ " INNER JOIN item.batchList AS batch WITH batch.isActive = true AND batch.isDeleted = false"
				+ " INNER JOIN item.generic AS generic WITH generic.isActive = true AND generic.isDeleted = false "
				+ " INNER JOIN generic.productCategory AS productCategory WITH productCategory.isActive = true AND productCategory.isDeleted = false "
				+ " INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isActive = true AND assetCategory.isDeleted = false "
				+ " INNER JOIN assetCategory.assetType AS assetType WITH assetType.isActive = true AND assetType.isDeleted = false "
				+ " LEFT JOIN generic.storewiseGenericList AS storeWiseGeneric WITH storeWiseGeneric.isActive = true AND storeWiseGeneric.isDeleted = false " 
				+ " INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isActive = true AND storeStock.isDeleted = false "
				+ " INNER JOIN storeStock.store AS store " + "	INNER JOIN item.addedBy AS user "
				+ " INNER JOIN batch.batchUomList AS batchUomList WITH batchUomList.isActive = true AND batchUomList.isDeleted = false AND batchUomList.storeUom = true"
				+ " INNER JOIN batchUomList.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false  "
				+ " INNER JOIN batchUomList.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
				+ " LEFT JOIN storeWiseGeneric.ved AS ved "
				+ "	WHERE item.isActive = true AND item.isDeleted = false AND item.unit.unitId = :unitId "
				//+ " 	AND item.addedDate BETWEEN :fromDate AND :toDate " 
				+ "		AND (:storeId = 0 OR  store.storeId = :storeId) "
				+ "		AND (:itemName = '' OR  lower(item.name) like :itemName) "
				+ "		AND (:assetTypeId = 0 OR  assetType.id = :assetTypeId) " + "     AND (ved.id IN (:vedList))"
				+ " AND batch.isConsignment = false"
				
				/*
				 * +
				 * "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				 * +
				 * "		INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				 * + "		INNER JOIN screenStatus.status as status) "
				 */ + "		ORDER BY item.name";

		Query query = getSession().createQuery(hql);
		//query.setParameter("fromDate", Util.getDate(request.getFromDate())); 
		//query.setParameter("toDate", Util.getDate(request.getToDate())); 
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
		query.setParameterList("vedList", request.getVedId());
		query.setParameter("itemName", request.getItemName()!=null ? "%"+request.getItemName().toLowerCase()+"%" :"");	
		// query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId(): sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(StopTransactionSearchResponse.class));

		return query.list();
	}
	
	
	@Override
	public List<ProductCategory> getProductCategoryList(String name) {
		String hql = "SELECT productCategory  FROM ProductCategory as productCategory "
				+ " INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isActive = true AND assetCategory.isDeleted = false "
				+ " WHERE (lower(productCategory.category) LIKE :name OR lower(productCategory.code) LIKE :name) " 
				+ " AND productCategory.isActive = true AND productCategory.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("name", name != null ? name.toLowerCase() + '%' : "");
		return query.list();

	}

//	@Override
//	public List<StopTransactionSearchResponse> searchAllStopTransactionNewList(StopTransactionRequest request,
//			Integer screenId) {
//		String hql = "SELECT item.id AS itemId, item.name AS itemName, item.code AS itemCode, batch.id AS batchId, batch.batchNo AS batchNo, batch.cop as cop,"
//				+ " batch.rate as mrp, batch.expiryDate AS expiryDate, productCategory.id AS productCategoryId, productCategory.category AS productCategory,"
//				+ " assetCategory.id AS assetCategoryId, assetCategory.category AS assetCategory, uomType.id AS uomTypeId, uomType.type AS uomType,"
//				+ " uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, store.storeId AS storeId,storeStock.availableQty  as avilableQuantity,(batch.rate * storeStock.availableQty) AS totalMrp" 
//				+ " FROM com.param.entity.model.master.Item AS item "
//				+ " INNER JOIN item.batchList AS batch WITH batch.isActive = true AND batch.isDeleted = false"
//				+ " INNER JOIN item.generic AS generic WITH generic.isActive = true AND generic.isDeleted = false "
//				+ " INNER JOIN generic.productCategory AS productCategory WITH productCategory.isActive = true AND productCategory.isDeleted = false "
//				+ " INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isActive = true AND assetCategory.isDeleted = false "
//				+ " INNER JOIN assetCategory.assetType AS assetType WITH assetType.isActive = true AND assetType.isDeleted = false "
////				+ " INNER JOIN generic.storewiseGenericList AS storeWiseGeneric WITH storeWiseGeneric.isActive = true AND storeWiseGeneric.isDeleted = false " 
//				//+ " INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isActive = true AND storeStock.isDeleted = false "
//				+ " INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "// AND storeStock.availableQty > 0 " 
//				+ " INNER JOIN storeStock.store AS store " + "	INNER JOIN item.addedBy AS user "
//				+ " INNER JOIN batch.batchUomList AS batchUomList WITH batchUomList.isActive = true AND batchUomList.isDeleted = false AND batchUomList.storeUom = true"
//				+ " INNER JOIN batchUomList.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false  "
//				+ " INNER JOIN batchUomList.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
//				+ "		INNER JOIN item.storeItemList AS storeMapper WITH storeMapper.isDeleted = false AND storeMapper.isActive = true "
//				+ "	WHERE item.isActive = true AND item.isDeleted = false AND item.unit.unitId = :unitId "
////				+ " 	AND item.addedDate BETWEEN :fromDate AND :toDate "   
//				+ "		AND (:storeId = 0 OR  store.storeId = :storeId) "
//				+ "		AND (:assetTypeId = 0 OR  assetType.id = :assetTypeId) "
//				+ "		AND (:storeId = 0 OR storeMapper.store.storeId = :storeId) "
//
//				/*
//				 * +
//				 * "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
//				 * +
//				 * "		INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
//				 * + "		INNER JOIN screenStatus.status as status) "
//				 */ + "		ORDER BY item.id DESC";
//
//		Query query = getSession().createQuery(hql);
////		query.setParameter("fromDate", Util.getDate(request.getFromDate())); 
////		query.setParameter("toDate", Util.getDate(request.getToDate())); 
//		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
//		query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
//		// query.setParameter("screenId", screenId);
//		query.setParameter("unitId", sessionService.getUnitId());
//		
//		query.setResultTransformer(Transformers.aliasToBean(StopTransactionSearchResponse.class));
//
//		return query.list();
//	}
	
	@Override 
	public List<StopTransactionSearchResponse> searchAllStopTransactionNewList(StopTransactionRequest request,
			Integer screenId) {
		String hql = "SELECT  item.id AS itemId, item.name AS itemName, item.code AS itemCode, batch.id AS batchId, batch.batchNo AS batchNo, batch.cop as cop,"
				+ " batch.rate as mrp, batch.expiryDate AS expiryDate, productCategory.id AS productCategoryId, productCategory.category AS productCategory,"
				+ " assetCategory.id AS assetCategoryId, assetCategory.category AS assetCategory, uomType.id AS uomTypeId, uomType.type AS uomType,"
				+ " uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, store.storeId AS storeId,storeStock.availableQty  as avilableQuantity,(batch.rate * storeStock.availableQty) AS totalMrp " 
				+ " FROM com.param.entity.model.master.Item AS item "
				+ " INNER JOIN item.batchList AS batch WITH batch.isActive = true AND batch.isDeleted = false"
				+ " INNER JOIN item.generic AS generic WITH generic.isActive = true AND generic.isDeleted = false "
				+ " INNER JOIN generic.productCategory AS productCategory WITH productCategory.isActive = true AND productCategory.isDeleted = false "
				+ " INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isActive = true AND assetCategory.isDeleted = false "
				+ " INNER JOIN assetCategory.assetType AS assetType WITH assetType.isActive = true AND assetType.isDeleted = false "
			//    + " LEFT JOIN generic.storewiseGenericList AS storeWiseGeneric WITH storeWiseGeneric.isActive = true AND storeWiseGeneric.isDeleted = false " 
			   //	+ " INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isActive = true AND storeStock.isDeleted = false "
				+ " INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "// AND storeStock.availableQty > 0 " 
				+ " INNER JOIN storeStock.store AS store " + "	INNER JOIN item.addedBy AS user "
				+ " INNER JOIN batch.batchUomList AS batchUomList WITH batchUomList.isActive = true AND batchUomList.isDeleted = false AND batchUomList.storeUom = true"
				+ " INNER JOIN batchUomList.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false  "
				+ " INNER JOIN batchUomList.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
				+ "		INNER JOIN item.storeItemList AS storeMapper WITH storeMapper.isDeleted = false AND storeMapper.isActive = true "
				+ "	WHERE item.isActive = true AND item.isDeleted = false AND item.unit.unitId = :unitId and storeStock.totalQty is not null and  storeStock.availableQty is not null "
     			//+ " 	AND item.addedDate BETWEEN :fromDate AND :toDate "   
				+ "		AND (:storeId = 0 OR  store.storeId = :storeId) "
				+ "		AND (:itemName = '' OR  lower(item.name) like :itemName) "
				+ "		AND (:assetTypeId = 0 OR  assetType.id = :assetTypeId) "
				+ "		AND (:storeId = 0 OR storeMapper.store.storeId = :storeId) "
				+ "		AND batch.isConsignment = false"

				/*
				 * +
				 * "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				 * +
				 * "		INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				 * + "		INNER JOIN screenStatus.status as status) "
				 */ + "		ORDER BY item.name";

		Query query = getSession().createQuery(hql);
		//query.setParameter("fromDate", Util.getDate(request.getFromDate())); 
    	//query.setParameter("toDate", Util.getDate(request.getToDate())); 
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
		// query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId(): sessionService.getUnitId());	
		query.setParameter("itemName", request.getItemName()!=null ? "%"+request.getItemName().toLowerCase()+"%" :"");	
		query.setResultTransformer(Transformers.aliasToBean(StopTransactionSearchResponse.class));

		return query.list();
	}	

	
	
	public Boolean isProductExclusionExist(Integer stopTransactionid,Integer prodId){		
		Boolean  isExit = false ;
		try {
			SQLQuery query = getSession().             
					createSQLQuery("select 1 from inventory.t_stop_transaction_product_category_exclusion_detail where stop_transaction_id = :stop_transaction_id and product_category_id = :product_category_id and is_deleted= false ");
			          query.setParameter("stop_transaction_id",stopTransactionid );
			          query.setParameter("product_category_id",prodId );
			
			isExit = query.uniqueResult() != null;

		} catch (Exception e) {
			logger.error("Error while checking product exclusion existence", e);
			throw new ServiceException(e.getMessage());
		}
		return  isExit;
	}


	public Boolean isItemWiseExclutionExist(Integer stopTransactionid,Integer itemId){
		Boolean  isExit = false ;
		try {
			SQLQuery query = getSession().             
					createSQLQuery("select 1 from inventory.t_stop_transaction_item_exclusion_detail where stop_transaction_id = :stop_transaction_id and item_id = :item_id and is_deleted= false");
			          query.setParameter("stop_transaction_id",stopTransactionid );
			          query.setParameter("item_id",itemId );
			
			isExit = query.uniqueResult() != null;

		} catch (Exception e) {
			logger.error("Error while checking item-wise exclusion existence", e);
			throw new ServiceException(e.getMessage());
		}
		return  isExit;
	}

	public Boolean isStopTransactionItemDetail(Integer stopTransactionid,Long batchId){
		Boolean  isExit = false ;
		try {
			SQLQuery query = getSession().             
					createSQLQuery("select 1 from inventory.t_stop_transaction_item_detail where stop_transaction_id = :stop_transaction_id and batch_id = :batchId and is_deleted= false");
			          query.setParameter("stop_transaction_id",stopTransactionid );
			          query.setParameter("batchId",batchId );
			
			isExit = query.uniqueResult() != null;

		} catch (Exception e) {
			logger.error("Error while checking stop transaction item detail existence", e);
			throw new ServiceException(e.getMessage());
		}
		return  isExit;
	}


	public Long ChecStockInTransit(Integer storeId){
		
		String hql = "SELECT  SUM(storeStock.tQty) FROM StoreStock AS storeStock "
				+ "		INNER JOIN storeStock.store AS store WITH store.id = :storeId AND store.isDeleted = false AND store.isActive = true "
				+ "		INNER JOIN storeStock.batch AS batch WITH batch.isDeleted = false AND batch.isActive = true "
				+ "WHERE storeStock.isDeleted = false AND storeStock.isActive = true ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);	
		Long tQty= (Long)query.uniqueResult();		

		tQty = tQty == null ? 0 : tQty;

		return tQty;
	}
	
	public Object CheckStopTransactionDuration(Integer storeId , Integer orgId, Integer unitId){		
		int statusId = 9; // Cancel Status of Stop Transaction
		try	{			
			SQLQuery query = getSession().             
					createSQLQuery(" SELECT st.stop_date_time,st.start_date_time,store_id, "
								   +" (DATE_PART('day', st.stop_date_time - now()) * 24 / 24) as days, "
								   +" (DATE_PART('hour', st.stop_date_time - now()) * 60 / 60) as hours, "
								   +" (DATE_PART('minute', st.stop_date_time - now())) as minutes "
								   +"  from inventory.t_stop_transaction as st "
								   +"  where st.store_id = :storeId AND "
								   + " (now() BETWEEN st.start_date_time AND st.stop_date_time) "
								   +"  and st.is_active = true and is_deleted = false "
								   +"  AND st.unit_id = :unitId"
								   +"  AND st.organization_id = :orgId "
								   +"  and status_id != :statusId");
			          query.setParameter("storeId",storeId);		
			          query.setParameter("statusId",statusId);
			  		  query.setParameter("unitId", unitId!=null && unitId > 0 ? unitId : sessionService.getUnitId());	
			  		  query.setParameter("orgId", orgId!=null && orgId > 0 ? orgId : sessionService.getOrganizationId());
			  return query.uniqueResult();
		}catch(Exception e) {
			logger.error("Error while checking stop transaction duration", e);
			throw new ServiceException(e.getMessage());
		}
	}
	//start code for resolved HIS-10092 While selecting start date time and stop date time then wrong validation showing.
	public Object CheckStopTransactionDuration(StopTransactionRequest request){		
		int statusId = 9; // Cancel Status of Stop Transaction
		String format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat df = new SimpleDateFormat(format);
		try	{
			SQLQuery query = getSession().             
					createSQLQuery(" SELECT st.stop_date_time,st.start_date_time,store_id, "
								   +" (DATE_PART('day', st.stop_date_time - now()) * 24 / 24) as days, "
								   +" (DATE_PART('hour', st.stop_date_time - now()) * 60 / 60) as hours, "
								   +" (DATE_PART('minute', st.stop_date_time - now())) as minutes "
								   +"  from inventory.t_stop_transaction as st "
								   +"  where st.store_id = :storeId AND "
								   + " ( :startDateTime BETWEEN st.start_date_time AND st.stop_date_time) "
								   +"  and st.is_active = true and is_deleted = false "
								   +"  AND st.unit_id = :unitId"
								   +"  AND st.organization_id = :orgId "
								   +"  and status_id != :statusId");
			          query.setParameter("storeId",request.getStoreId());		
			          query.setParameter("startDateTime",df.parse(request.getStartDateTime()));
			          query.setParameter("statusId",statusId);
			  		  query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId(): sessionService.getUnitId());	
			  		  query.setParameter("orgId", request.getOrganizationId()!=null ? request.getOrganizationId(): sessionService.getOrganizationId());
			  return query.uniqueResult();
		}catch(Exception e) {
			logger.error("Error while checking stop transaction duration", e);
			throw new ServiceException(e.getMessage());
		}
	}
	//end code for resolved HIS-10092 While selecting start date time and stop date time then wrong validation showing.
	
	public List<StopTransactionProcessResponse> CheckStopTransactionBatchWise(Integer storeId,List<Long> batchIds,  StopTransactionRequest request){			
		int statusId = 2; // Pending Status of Stop Transaction
		try	{			
			SQLQuery query = getSession().             
					createSQLQuery("SELECT stop_item.item_id as itemid,stop_item.batch_id as batchid,st.start_date_time as startdate,st.stop_date_time as stopdate,"
								 + " (DATE_PART('day', st.stop_date_time - now()) * 24 / 24) as days,"
								 + " (DATE_PART('hour', st.stop_date_time - now()) * 60 / 60) as hours,"
								 + " (DATE_PART('minute', st.stop_date_time - now())) as minutes"
								 + "  from inventory.t_stop_transaction as st"
								 + "  inner join inventory.t_stop_transaction_item_detail as stop_item ON st.id = stop_item.stop_transaction_id "
								 + "  Left join inventory.t_stop_transaction_item_exclusion_detail as item_exclution on st.id =  item_exclution.stop_transaction_id"
								 + "  Left join inventory.t_stop_transaction_product_category_exclusion_detail as product_exclution ON st.id=  product_exclution.stop_transaction_id"
								 + "  WHERE st.store_id= :storeId and  st.unit_id = :unitId "
								 + "  AND st.organization_id = :orgId "
								 + "  and  (now() BETWEEN st.start_date_time AND st.stop_date_time)"
								 + "  and st.is_active = true and st.is_deleted = false AND st.status_id =:statusId"
								 + "  AND stop_item.is_active = true and stop_item.is_deleted = false  "								
								 + "  and stop_item.batch_id in(:batchIds) ");
			
			          query.setParameter("storeId",storeId);				       
			          query.setParameterList("batchIds",batchIds);			         
			          query.setParameter("statusId",statusId);
			  		  query.setParameter("unitId",request.getUnitId()!=null ? request.getUnitId(): sessionService.getUnitId());	
			  		  query.setParameter("orgId", request.getOrganizationId()!=null ? request.getOrganizationId(): sessionService.getOrganizationId());
			  		  
			  		query.setResultTransformer(Transformers.aliasToBean(StopTransactionProcessResponse.class));

					return query.list();

		}catch(Exception e) {
			logger.error("Error while checking stop transaction batch-wise", e);
			throw new ServiceException(e.getMessage());
		}
	}

	public List<StopTransactionProcessResponse> CheckStopTransactionItemWise(Integer storeId,List<Integer> itemIds, StopTransactionRequest request){			
		int statusId = 2; // Pending Status of Stop Transaction
		try	{			
			SQLQuery query = getSession().             
					createSQLQuery("SELECT stop_item.item_id as itemid,stop_item.batch_id as batchid,st.start_date_time as startdate,st.stop_date_time as stopdate,"
								 + " (DATE_PART('day', st.stop_date_time - now()) * 24 / 24) as days,"
								 + " (DATE_PART('hour', st.stop_date_time - now()) * 60 / 60) as hours,"
								 + " (DATE_PART('minute', st.stop_date_time - now())) as minutes"
								 + "  from inventory.t_stop_transaction as st"
								 + "  inner join inventory.t_stop_transaction_item_detail as stop_item ON st.id = stop_item.stop_transaction_id "
								 + "  Left join inventory.t_stop_transaction_item_exclusion_detail as item_exclution on st.id =  item_exclution.stop_transaction_id"
								 + "  Left join inventory.t_stop_transaction_product_category_exclusion_detail as product_exclution ON st.id=  product_exclution.stop_transaction_id"
								 + "  WHERE st.store_id= :storeId and  st.unit_id = :unitId "
								 + "  AND st.organization_id = :orgId "
								 + "  and  (now() BETWEEN st.start_date_time AND st.stop_date_time)"
								 + "  and st.is_active = true and st.is_deleted = false AND st.status_id =:statusId"
								 + "  AND stop_item.is_active = true and stop_item.is_deleted = false  "								
								 + "  and stop_item.item_id in(:itemIds) ");
			          query.setParameter("storeId",storeId);				       
			          query.setParameterList("itemIds",itemIds);			         
			          query.setParameter("statusId",statusId);
			  		  query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId(): sessionService.getUnitId());	
			  		  query.setParameter("orgId",request.getOrganizationId()!=null ? request.getOrganizationId(): sessionService.getOrganizationId());
			  		  
			  		query.setResultTransformer(Transformers.aliasToBean(StopTransactionProcessResponse.class));

					return query.list();

		}catch(Exception e) {
			logger.error("Error while checking stop transaction item-wise", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Store getStoreForAlert(Integer storeId, Integer unitId) {
		String hql = "SELECT store FROM Store AS store "				
				+ " WHERE store.isDeleted = false AND store.isActive = true "				
				+ "	AND store.storeId = :storeId AND store.unit.unitId = :unitId ";
		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("unitId", unitId!=null && unitId > 0 ? unitId : sessionService.getUnitId());
		return (Store) query.uniqueResult();
	}
	
	@Override
	public Store updateStoreforAlert(Store store) {
		getSession().update(store);
		return store;
	}	
	
	@Override
	public List<StopTransactionItemDetail> getStopTransactionItemDetail(Integer ProductId,Integer stopTransactionId) {		
		String hql = " SELECT  stopItemDetail FROM StopTransactionItemDetail as stopItemDetail "
					+" INNER JOIN stopItemDetail.batch as batch WITH batch.isDeleted = false AND batch.isActive = true "
					+" INNER JOIN batch.item as item WITH item.isDeleted = false AND item.isActive = true "
					+" INNER JOIN item.generic as generic WITH generic.isDeleted = false AND generic.isActive = true "
					+" WHERE stopItemDetail.StopTransaction.id =:stopTransactionId AND stopItemDetail.isDeleted = false"
					+" AND stopItemDetail.isActive = true AND generic.productCategory.id =:ProductId ";		
		
		Query query = getSession().createQuery(hql);
		query.setParameter("ProductId", ProductId);
		query.setParameter("stopTransactionId", stopTransactionId);	
		//query.setParameter("unitId", sessionService.getUnitId());
		return (List<StopTransactionItemDetail>) query.list();
	}
	
	
	@Override
	public List<StopTransactionItemDetail> getStopTransactionItemDetailByItemId(List<Integer> itemIds,Integer stopTransactionId) {	
		
		String hql = " SELECT stopItemDetail FROM StopTransactionItemDetail as stopItemDetail " 				
				+" INNER JOIN stopItemDetail.batch as batch WITH batch.isDeleted = false AND batch.isActive = true "				
				+" INNER JOIN batch.item as item WITH item.isDeleted = false AND item.isActive = true "
				+" WHERE stopItemDetail.itemId IN (:itemIds) AND stopItemDetail.StopTransaction.id =:stopTransactionId AND "  
				+" stopItemDetail.isDeleted = false AND stopItemDetail.isActive = true";	
		
		Query query = getSession().createQuery(hql);
		query.setParameterList("itemIds", itemIds);
		query.setParameter("stopTransactionId", stopTransactionId);	
		//query.setParameter("unitId", sessionService.getUnitId());
		return (List<StopTransactionItemDetail>) query.list();
	}
	@Override
	public StopTransactionItemDetail updateStopTransactionItemDetail(StopTransactionItemDetail stopTransactionItemDetail) {
		getSession().update(stopTransactionItemDetail);
		return stopTransactionItemDetail;
	}

	/* start code for resolved HIS-10094 Store / Inventory - Without add stop transaction details able to save request for stop transaction */
	@Override
	public List<BigInteger> CheckStopTransactionDetail(StopTransactionRequest request) {
		try {
			String sql ="SELECT st.id FROM inventory.t_stop_transaction AS st "
					+ " INNER JOIN inventory.t_stop_transaction_item_detail AS stid ON stid.stop_transaction_id = st.id WHERE st.id in (:ids) group by st.id ";
			Query query = getSession().createSQLQuery(sql);
			query.setParameterList("ids", request.getStopTransactionId());
			return query.list();
		} catch(Exception e) {
			logger.error("Error while checking stop transaction detail", e);
			throw new ServiceException(e.getMessage());
		}
	}
	/* end code for resolved HIS-10094 Store / Inventory - Without add stop transaction details able to save request for stop transaction */

	@Override
	public List<StopTransactionSearchResponse> searchAllStopTransactionNonZeroNewList(StopTransactionRequest request,
			Integer screenId) {
		if(request.getAssetTypeId()==2)
		{
	        String hql1 = "SELECT "
	        		+ " item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory," 
	        		+ " 	item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiryDate,"
	        		+ " 	batch.rate AS mrp, cast(COALESCE(SUM(storeStock.availableQty), 0) as int) AS avilableQuantity, batch.cop AS cop, " 
	        		+ "		assetCategory.id as assetCategoryId,(batch.rate * storeStock.availableQty) AS totalMrp, " 
	        		+ "		productCategory.id as productCategoryId , item.isThreeUOMApplicable as isThreeUOMApplicable "
	                + "    FROM Batch AS batch "
	                + "        INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
	                + "        LEFT JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
	                + "        INNER JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
	                + "        INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
	                + "        INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
	                + "        LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true  "
	                + "        LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
	                + "        LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
	                + "        INNER JOIN item.storeItemList AS storeMapperFrom WITH storeMapperFrom.isDeleted = false AND storeMapperFrom.isActive = true "
	                + "    WHERE batch.isDeleted = false AND batch.isActive = true  AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
	                + "     AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
	                + "        AND (:storeId = 0 OR storeStock.store.storeId = :storeId) "
	                + "        AND (:storeId = 0 OR storeMapperFrom.store.storeId = :storeId) "
	                + "     and ( :itemName='' or upper(item.name) like upper(:itemName)) "
	                + "        AND (:storeId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
	                + "                                                        FROM StoreProductCategoryMapper AS storeProduct "
	                + "                                                        WHERE storeProduct.store.storeId = :storeId) "
	                + "                            AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
	                + "                                                WHERE itemRestriction.store.storeId = :storeId "
	                + "                                                AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
	                + "    GROUP BY item.id, assetCategory.category, productCategory.category, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.rate, "
	                + "         batch.cop,storeStock.availableQty,item.isThreeUOMApplicable, "
	                + " assetCategory.id , productCategory.id  "
	                + "    ORDER BY item.id ";

	        Query query1 = getSession().createQuery(hql1);
	        query1.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
	        query1.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
	        query1.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
	        query1.setParameter("itemName", request.getItemName() != null ? '%'+request.getItemName().toUpperCase() + '%' : "");
	        query1.setResultTransformer(Transformers.aliasToBean(StopTransactionSearchResponse.class));
	        return query1.list();
		}
		else
		{
		String hql = "SELECT  item.id AS itemId, item.name AS itemName, item.code AS itemCode, batch.id AS batchId, batch.batchNo AS batchNo, batch.cop as cop,"
				+ " batch.rate as mrp, batch.expiryDate AS expiryDate, productCategory.id AS productCategoryId, productCategory.category AS productCategory,"
				+ " assetCategory.id AS assetCategoryId, assetCategory.category AS assetCategory, uomType.id AS uomTypeId, uomType.type AS uomType,"
				+ " uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, store.storeId AS storeId,storeStock.availableQty  as avilableQuantity,(batch.rate * storeStock.availableQty) AS totalMrp, item.isThreeUOMApplicable as isThreeUOMApplicable " 
				+ " FROM com.param.entity.model.master.Item AS item "
				+ " INNER JOIN item.batchList AS batch WITH batch.isActive = true AND batch.isDeleted = false"
				+ " INNER JOIN item.generic AS generic WITH generic.isActive = true AND generic.isDeleted = false "
				+ " INNER JOIN generic.productCategory AS productCategory WITH productCategory.isActive = true AND productCategory.isDeleted = false "
				+ " INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isActive = true AND assetCategory.isDeleted = false "
				+ " INNER JOIN assetCategory.assetType AS assetType WITH assetType.isActive = true AND assetType.isDeleted = false "
			    + " LEFT JOIN generic.storewiseGenericList AS storeWiseGeneric WITH storeWiseGeneric.isActive = true AND storeWiseGeneric.isDeleted = false " 
			   //	+ " INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isActive = true AND storeStock.isDeleted = false "
				+ " INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "// AND storeStock.availableQty > 0 " 
				+ " INNER JOIN storeStock.store AS store " + "	INNER JOIN item.addedBy AS user "
				+ " INNER JOIN batch.batchUomList AS batchUomList WITH batchUomList.isActive = true AND batchUomList.isDeleted = false AND batchUomList.storeUom = true"
				+ " INNER JOIN batchUomList.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false  "
				+ " INNER JOIN batchUomList.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
				+ "		INNER JOIN item.storeItemList AS storeMapper WITH storeMapper.isDeleted = false AND storeMapper.isActive = true "
				+ "	WHERE item.isActive = true AND item.isDeleted = false AND item.unit.unitId = :unitId "
				+ "  and storeStock.totalQty is not null and  storeStock.availableQty is not null  "
     			//+ " 	AND item.addedDate BETWEEN :fromDate AND :toDate "   
				+ "		AND (:storeId = 0 OR  store.storeId = :storeId) "
				+ "		AND (:itemName = '' OR  lower(item.name) like :itemName) "
				+ "		AND (:batchNo = '' OR  lower(batch.batchNo) like :batchNo) "
				+ "		AND (:assetTypeId = 0 OR  assetType.id = :assetTypeId) "
				+ "		AND (:storeId = 0 OR storeMapper.store.storeId = :storeId) "
				+ "		AND batch.isConsignment = false"

				/*
				 * +
				 * "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				 * +
				 * "		INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				 * + "		INNER JOIN screenStatus.status as status) "
				 */ + "		ORDER BY item.name";

		Query query = getSession().createQuery(hql);
		//query.setParameter("fromDate", Util.getDate(request.getFromDate())); 
    	//query.setParameter("toDate", Util.getDate(request.getToDate())); 
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
		// query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId(): sessionService.getUnitId());	
		query.setParameter("itemName", request.getItemName()!=null ? "%"+request.getItemName().toLowerCase()+"%" :"");	
		query.setParameter("batchNo", request.getBatchNo()!=null ? "%"+request.getBatchNo().toLowerCase()+"%" :"");	
		query.setResultTransformer(Transformers.aliasToBean(StopTransactionSearchResponse.class));

		return query.list();
	}
	
	}

	@Override
	public List<StopTransactionSearchResponse> searchStopTransactionNonZeroNewList(StopTransactionRequest request,
			Integer screenId) {


		String hql = "SELECT item.id AS itemId, item.name AS itemName, item.code AS itemCode, batch.id AS batchId, batch.batchNo AS batchNo, batch.cop as cop,"
				+ " batch.rate as mrp, batch.expiryDate AS expiryDate, productCategory.id AS productCategoryId, productCategory.category AS productCategory,"
				+ " assetCategory.id AS assetCategoryId, assetCategory.category AS assetCategory, uomType.id AS uomTypeId, uomType.type AS uomType,"
				+ " uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, store.storeId AS storeId, storeStock.availableQty  as avilableQuantity,(batch.rate * storeStock.availableQty) AS totalMrp, item.isThreeUOMApplicable as isThreeUOMApplicable "
				+ " FROM com.param.entity.model.master.Item AS item "
				+ " INNER JOIN item.batchList AS batch WITH batch.isActive = true AND batch.isDeleted = false"
				+ " INNER JOIN item.generic AS generic WITH generic.isActive = true AND generic.isDeleted = false "
				+ " INNER JOIN generic.productCategory AS productCategory WITH productCategory.isActive = true AND productCategory.isDeleted = false "
				+ " INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isActive = true AND assetCategory.isDeleted = false "
				+ " INNER JOIN assetCategory.assetType AS assetType WITH assetType.isActive = true AND assetType.isDeleted = false "
				+ " LEFT JOIN generic.storewiseGenericList AS storeWiseGeneric WITH storeWiseGeneric.isActive = true AND storeWiseGeneric.isDeleted = false " 
				+ " INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isActive = true AND storeStock.isDeleted = false "
				+ " INNER JOIN storeStock.store AS store " + "	INNER JOIN item.addedBy AS user "
				+ " INNER JOIN batch.batchUomList AS batchUomList WITH batchUomList.isActive = true AND batchUomList.isDeleted = false AND batchUomList.storeUom = true"
				+ " INNER JOIN batchUomList.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false  "
				+ " INNER JOIN batchUomList.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
				+ " INNER JOIN storeWiseGeneric.ved AS ved "
				+ "	WHERE item.isActive = true AND item.isDeleted = false AND item.unit.unitId = :unitId  "
				//+ " 	AND item.addedDate BETWEEN :fromDate AND :toDate " 
				+ "		AND (:storeId = 0 OR  store.storeId = :storeId) "
				+ "		AND (:itemName = '' OR  lower(item.name) like :itemName) "
				+ "		AND (:assetTypeId = 0 OR  assetType.id = :assetTypeId) " + "     AND (ved.id IN (:vedList))"
				+ " AND batch.isConsignment = false"
				
				/*
				 * +
				 * "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				 * +
				 * "		INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				 * + "		INNER JOIN screenStatus.status as status) "
				 */ + "		ORDER BY item.name";

		Query query = getSession().createQuery(hql);
		//query.setParameter("fromDate", Util.getDate(request.getFromDate())); 
		//query.setParameter("toDate", Util.getDate(request.getToDate())); 
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
		query.setParameterList("vedList", request.getVedId());
		query.setParameter("itemName", request.getItemName()!=null ? "%"+request.getItemName().toLowerCase()+"%" :"");	
		// query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId(): sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(StopTransactionSearchResponse.class));

		return query.list();
	
	}
}

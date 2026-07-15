package com.param.inventory.store.dao.returnableGatepass;

import java.util.ArrayList;
import java.util.List;

import com.param.service.global.SessionService;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.global.common.ServiceException;
import com.param.entity.model.inventory.ReturnableGatepass;
import com.param.entity.model.inventory.ReturnableGatepassAccessories;
import com.param.global.utility.Util;
import com.param.inventory.store.dto.returnableGatepass.ItemResponse;
import com.param.inventory.store.dto.returnableGatepass.ReturnableGatepassRequest;
import com.param.inventory.store.dto.returnableGatepass.ReturnableGatepassResponse;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionRequest;


@Repository
public class ReturnableGatepassDao extends AbstractDao<Integer, ReturnableGatepass> implements IReturnableGatepassDao {

	private static final Logger logger = Logger.getLogger(ReturnableGatepassDao.class);

	@Autowired
	private SessionService sessionService;
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public List<ReturnableGatepassResponse> getReturnableGatepassList(ReturnableGatepassRequest request,
			Integer screenId) {

		String hql = "SELECT gatepass.id AS id, gatepass.retGpNo AS gatepassNo, gatepass.addedDate AS gatepassDate, assetType.type AS assetType, requestType.type AS against, "
				+ "		store.storeId AS storeId, store.storeName AS store, vendor.name AS vendor, user.userName AS requestedBy, status.id AS statusId, "
				+ "		status.status AS status, approvalStatus.id AS approvalStatusId, approvalStatus.status AS approvalStatus  "
				+ "	FROM ReturnableGatepass AS gatepass "
				+ "		INNER JOIN gatepass.assetType AS assetType WITH assetType.isActive = true AND assetType.isDeleted = false "
				+ "		INNER JOIN gatepass.requestType AS requestType WITH requestType.isActive = true AND requestType.isDeleted = false "
				+ "		INNER JOIN gatepass.store AS store WITH store.isActive = true AND store.isDeleted = false "
			//	+ "		INNER JOIN gatepass.requestingStore AS requestingStore WITH requestingStore.isActive = true AND requestingStore.isDeleted = false "
				+ "		INNER JOIN gatepass.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ "		INNER JOIN gatepass.addedBy AS user WITH user.status = 'A' "
				+ "		INNER JOIN gatepass.status AS status WITH status.isActive = true AND status.isDeleted = false "
				+ "		INNER JOIN gatepass.approvalStatus AS approvalStatus WITH approvalStatus.isActive = true AND approvalStatus.isDeleted = false "
				+ "	WHERE gatepass.isActive = true AND gatepass.isDeleted = false AND gatepass.unit.unitId = :unitId "
				+ "		AND gatepass.addedDate BETWEEN :fromDate AND :toDate AND (:storeId = 0 OR store.id = :storeId) "
				//+ " 	AND (:requestingStoreId = 0 OR requestingStore.id = :requestingStoreId) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND ((:vendorId != 0 AND :vendor != '') OR :vendor = '' OR upper(vendor.contactPersonName) like upper(:vendor)) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) AND (:againstId = 0 OR requestType.id = :againstId) "
				+ " 	AND (:gatepassId = 0 OR gatepass.id = :gatepassId) "
				+ "		AND ((:gatepassId != 0 AND :gatepassNo != '') OR :gatepassNo = '' OR upper(gatepass.retGpNo) like upper(:gatepassNo)) "
				+ "		AND (:statusId = 0 OR status.id = :statusId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId AND screen.isDeleted = false "
				+ "										AND screen.isActive = true "
				+ "								INNER JOIN screenStatus.status as status WITH status.isDeleted = false AND status.isActive = true "
				+ "						  WHERE screenStatus.isActive = true AND screenStatus.isDeleted = false) "
				+ "	ORDER BY gatepass.id DESC ";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(request.getFromDate()));
		query.setParameter("toDate", Util.getDate(request.getToDate()));
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
	//	query.setParameter("requestingStoreId", request.getRequestingStoreId() != null ? request.getRequestingStoreId() : 0);
		query.setParameter("vendorId", request.getVendorId() != null ? request.getVendorId() : 0);
		query.setParameter("vendor", request.getVendor() != null ? request.getVendor() + "%" : "");
		query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
		query.setParameter("againstId", request.getAgainstId() != null ? request.getAgainstId() : 0);
		query.setParameter("gatepassId", request.getId() != null ? request.getId() : 0);
		query.setParameter("gatepassNo", request.getGatepassNo() != null ? '%'+request.getGatepassNo() + "%" : "");
		query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ReturnableGatepassResponse.class));

		return query.list();
	}

	@Override
	public ReturnableGatepass saveReturnableGatepass(ReturnableGatepass gatepass) {
		getSession().save(gatepass);

		return gatepass;
	}
	
	@Override
	public List<ReturnableGatepassAccessories> getReturnableGatepassAccessoriesList(Integer id) {
		
		String hql = "SELECT returnableGatepassAccessories FROM ReturnableGatepassAccessories AS returnableGatepassAccessories "
				+ " INNER JOIN returnableGatepassAccessories.returnableGatepassDetail AS returnableGatepassDetail WITH returnableGatepassDetail.id = :id AND returnableGatepassDetail.isDeleted = false AND returnableGatepassDetail.isActive = true "
				+ "WHERE returnableGatepassAccessories.isDeleted = false AND returnableGatepassAccessories.isActive = true ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
}

	@Override
	public ReturnableGatepassResponse getReturnableGatepassResponse(Integer id) {

		String hql = "SELECT gatepass.id AS id, gatepass.retGpNo AS gatepassNo, gatepass.addedDate AS gatepassDate, assetType.id AS assetTypeId, assetType.type AS assetType, "
				+ "		requestType.id AS againstId, requestType.type AS against, store.storeId AS storeId, store.storeName AS store, vendor.id AS vendorId, vendor.contactPersonName AS vendor, "
				+ "		gatepass.referenceNo AS referenceNo, user.userName AS requestedBy, status.id AS statusId, status.status AS status, approvalStatus.id AS approvalStatusId, "
				+ "		approvalStatus.status AS approvalStatus, gatepass.totalAmt AS totalAmount, gatepass.totalTaxAmount AS totalTaxAmount, gatepass.netAmount AS netAmount, "
				+ "		gatepass.remark AS remark "
				+ "	FROM ReturnableGatepass AS gatepass "
				+ "		INNER JOIN gatepass.assetType AS assetType WITH assetType.isActive = true AND assetType.isDeleted = false "
				+ "		INNER JOIN gatepass.requestType AS requestType WITH requestType.isActive = true AND requestType.isDeleted = false "
				+ "		INNER JOIN gatepass.store AS store WITH store.isActive = true AND store.isDeleted = false "
				+ "		INNER JOIN gatepass.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ "		INNER JOIN gatepass.addedBy AS user WITH user.status = 'A' "
				+ "		INNER JOIN gatepass.status AS status WITH status.isActive = true AND status.isDeleted = false "
				+ "		INNER JOIN gatepass.approvalStatus AS approvalStatus WITH approvalStatus.isActive = true AND approvalStatus.isDeleted = false "
				+ "	WHERE gatepass.id = :id AND gatepass.isActive = true AND gatepass.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(ReturnableGatepassResponse.class));

		return (ReturnableGatepassResponse) query.uniqueResult();
	}

	@Override
	public List<ItemResponse> getReturnableGatepassDetails(Integer id) {
		
		

		String hql = "SELECT detail.id AS id, item.code AS itemCode, item.name AS itemName, uomType.id AS uomTypeId, uomType.type AS uomType, "
				+ "		uomUnit.id AS uomUnitId, uomUnit.units AS uomUnit, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ "		batch.lotNo AS lotNo, vendor.id AS vendorId, vendor.contactPersonName AS vendor, COALESCE(storeStock.availableQty, 0) AS availableQuantity, "
				+ "		detail.returnQty AS returnQuantity, batch.cop AS cop, tax.id AS taxId, tax.name AS taxName, detail.taxPercentage AS taxPercentage, "
				+ "		detail.totalAmt AS totalAmount, detail.totalTaxAmount AS totalTaxAmount, detail.netAmount AS netAmount, status.id AS statusId,"
				+ "		detail.approvedQty AS approvedQuantity, detail.rejectQty AS rejectedQuantity, detail.totalDiscount AS totalDiscount "
				+ "	FROM ReturnableGatepass AS gatepass "
				+ "		INNER JOIN gatepass.returnableGatepassDetails AS detail WITH detail.isActive = true AND detail.isDeleted = false "
				+ "		LEFT JOIN detail.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				+ "		INNER JOIN detail.tax AS tax WITH tax.isActive = true AND tax.isDeleted = false "
				+ "		INNER JOIN detail.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
				+ "		INNER JOIN detail.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
				+ "		INNER JOIN detail.status AS status WITH status.isActive = true AND status.isDeleted = false "
				+ "		INNER JOIN detail.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false "
				+ "		INNER JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
				+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isActive = true AND storeStock.isDeleted = false "
				+ "	WHERE gatepass.id = :id AND gatepass.isActive = true AND gatepass.isDeleted = false AND storeStock.store.storeId = gatepass.store.storeId ";

		
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(ItemResponse.class));

		return query.list();
	}
	
	@Override
	public List<ItemResponse> getReturnableGatepassResponseFixedType(Integer id) {
		
		String hql = "SELECT returnableGatepassDetails FROM ReturnableGatepassDetail AS returnableGatepassDetails "
				+"INNER JOIN returnableGatepassDetails.returnableGatepass AS detail WITH detail.isActive = true AND detail.isDeleted = false "
				+ "	WHERE gatepass.id = :id AND gatepass.isActive = true AND gatepass.isDeleted = false ";

		/*
		 * String hql =
		 * "SELECT detail.id AS id, item.code AS itemCode, item.name AS itemName,  GatepassAccessories.quantity AS quantity, "
		 * +
		 * "		 batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
		 * +
		 * "		batch.lotNo AS lotNo, vendor.id AS vendorId, vendor.contactPersonName AS vendor, COALESCE(storeStock.availableQty, 0) AS availableQuantity, "
		 * +
		 * "		detail.returnQty AS returnQuantity, batch.cop AS cop, tax.id AS taxId, tax.name AS taxName, detail.taxPercentage AS taxPercentage, "
		 * +
		 * "		detail.totalAmt AS totalAmount, detail.totalTaxAmount AS totalTaxAmount, detail.netAmount AS netAmount, status.id AS statusId,"
		 * +
		 * "		detail.approvedQty AS approvedQuantity, detail.rejectQty AS rejectedQuantity "
		 * + "	FROM ReturnableGatepass AS gatepass " +
		 * "		INNER JOIN gatepass.returnableGatepassDetails AS detail WITH detail.isActive = true AND detail.isDeleted = false "
		 * +
		 * "		LEFT JOIN detail.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
		 * +
		 * "		INNER JOIN detail.tax AS tax WITH tax.isActive = true AND tax.isDeleted = false "
		 * 
		 * +
		 * "		INNER JOIN detail.status AS status WITH status.isActive = true AND status.isDeleted = false "
		 * +
		 * "		INNER JOIN detail.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false "
		 * +
		 * "		INNER JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
		 * +
		 * "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isActive = true AND storeStock.isDeleted = false "
		 * +
		 * "		INNER JOIN detail.returnableGatepassAccessoriesList AS GatepassAccessories WITH GatepassAccessories.isActive = true AND GatepassAccessories.isDeleted = false "
		 * 
		 * +
		 * "	WHERE gatepass.id = :id AND gatepass.isActive = true AND gatepass.isDeleted = false AND storeStock.store.storeId = gatepass.store.storeId "
		 * ;
		 */
		
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(ItemResponse.class));

		return query.list();
	}

	@Override
	public ReturnableGatepass updateReturnableGatepass(ReturnableGatepass gatepass) {
		getSession().update(gatepass);

		return gatepass;
	}

	@Override
	public ReturnableGatepass getReturnableGatepass(Integer id) {
		String hql = "SELECT gatepass FROM ReturnableGatepass AS gatepass "
				+ "	WHERE gatepass.id = :id AND gatepass.isActive = true AND gatepass.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (ReturnableGatepass) query.uniqueResult();
	}
	
	@Override
	public Integer getReturnableGatepassAssetType(Integer id) {
		String hql = "SELECT assetType.id FROM ReturnableGatepass AS gatepass "
				+ "		INNER JOIN gatepass.assetType AS assetType WITH assetType.isActive = true AND assetType.isDeleted = false "
				+ "	WHERE gatepass.id = :id AND gatepass.isActive = true AND gatepass.isDeleted = false ";
		
		
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return  (Integer) query.uniqueResult();
	}

	@Override
	public Integer getQuantity(Integer id) {
		String sql=" select tpid.reject_quantity  from procurement.t_physical_inspection_detail tpid " + 
				" inner join procurement.t_physical_inspection tpi on tpid.physical_inspection_id =tpi.id " + 
				" inner join  inventory.t_returnable_gatepass_detail trgd on trgd.grn_detail_id =tpid.grn_detail_id " + 
				" where trgd.id =:Id " + 
				" order by trgd.id desc ";
        Query query = getSession().createSQLQuery(sql);
     query.setParameter("Id", id !=null ? id : 0 );

     return (Integer) query.uniqueResult();
	}

	@Override
	public Integer getid(Integer id) {
		String sql=" select trgd.grn_detail_id  from inventory.t_returnable_gatepass_detail trgd  " + 
				" where trgd.id =:Id ";
        Query query = getSession().createSQLQuery(sql);
     query.setParameter("Id", id !=null ? id : 0 );

     return (Integer) query.uniqueResult();
	}

	@Override
	public Integer getDetailApprovedQantity(Integer id,Integer reqTypeId) {
		String sql=" select CAST(SUM(trgd.approved_qty) AS INT)  "
				+ "from inventory.t_returnable_gatepass_detail trgd  " + 
				" inner join inventory.t_returnable_gatepass trg on trg.id=trgd.returnable_gatepass_id "
				+ " inner join m_request_type mrt on mrt.id=trg.request_type_id "
				+ " where trgd.grn_detail_id =:Id and mrt.id="+reqTypeId+" ";
        Query query = getSession().createSQLQuery(sql);
     query.setParameter("Id", id !=null ? id : 0 );

     return (Integer) query.uniqueResult();
	}

	@Override
	public Integer getDetailApprovedQantity1(Integer gatepassId) {
		String sql=" select CAST((trgd.grn_approved_quantity + trgd.rejected_quantity) AS INT)  from procurement.t_good_receipt_note_detail trgd  " + 
				" where trgd.id =:Id ";
        Query query = getSession().createSQLQuery(sql);
     query.setParameter("Id", gatepassId !=null ? gatepassId : 0 );

     return (Integer) query.uniqueResult();
	}

	@Override
	public Double totalDiscountAmount(Integer gatepassId) {
		String sql=" select trgd.total_discount  from procurement.t_good_receipt_note_detail trgd  " + 
				" where trgd.id =:Id ";
        Query query = getSession().createSQLQuery(sql);
     query.setParameter("Id", gatepassId !=null ? gatepassId : 0 );

     return (Double) query.uniqueResult();
	}

	@Override
	public Double totalAmount(Integer gatepassId) {
		String sql=" select trgd.total_amount  from procurement.t_good_receipt_note_detail trgd  " + 
				" where trgd.id =:Id ";
        Query query = getSession().createSQLQuery(sql);
     query.setParameter("Id", gatepassId !=null ? gatepassId : 0 );

     return (Double) query.uniqueResult();
	}

	@Override
	public void updateIntransitQty(Integer detailsId, Integer leaseRejectedQty, Boolean inTransit, Integer screenId, String screenCode) {
		try {
			try {
				String query = "select id as \"stockTransactionId\" from inventory.t_stock_transaction where  transaction_details_id =:detailsId and doc_id in(31,13) order by id desc limit 1 ";

				PhysicalInspectionRequest dto = (PhysicalInspectionRequest) sessionFactory.getCurrentSession()
						.createSQLQuery(query).setParameter("detailsId", detailsId)
						.setResultTransformer(Transformers.aliasToBean(PhysicalInspectionRequest.class)).uniqueResult();
				
				if(dto!= null) {
					Integer result = getSession()
							.createSQLQuery(" update inventory.t_stock_transaction  set is_transit  = :intrasit ,out_qty=:rejectedQuantity  where id =:id ")
							.setParameter("id", dto.getStockTransactionId())
							.setParameter("rejectedQuantity", leaseRejectedQty)
							.setParameter("intrasit", inTransit)
							.executeUpdate();
				}
				
			} catch (HibernateException he) {
				throw new HibernateException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateIntransitQty inner failed", e);
				throw new ServiceException(e.getMessage());
			}
		} catch (Exception e) {
			logger.error("updateIntransitQty failed", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public List<ReturnableGatepassResponse> checkReturnableGatePass(List<ReturnableGatepassRequest> request) {

		List<ReturnableGatepassResponse> list = new ArrayList<>();
		List<ReturnableGatepassResponse> list1 = new ArrayList<>();
		for(int i =0 ; i<= request.size(); i++){

			if(list.size()>0){
				for(int j =0 ; j< list.size(); j++){
					try{
						list1.add(list.get(j));
					}
					catch (Exception e){
						logger.error("checkReturnableGatePass failed", e);
						throw new ServiceException(e.getMessage());
					}

				}
			}
			if(i<request.size()){
				String sql = " select  distinct trgd.id as \"returnableGatePassDetailId\", trgd.returnable_gatepass_id as \"id\", "
						+ " trgd.grn_detail_id as \"grnDetailId\" "
						+ " from inventory.t_returnable_gatepass_detail trgd "
						+ " inner join inventory.t_returnable_gatepass trg on trg.id = trgd.returnable_gatepass_id "
						+ " where trgd.grn_detail_id =:grnDetailId and trg.request_type_id =:requestTypeId";

				Query query = getSession().createSQLQuery(sql);
				query.setParameter("requestTypeId", request.get(i).getRequestTypeId() !=null ? request.get(i).getRequestTypeId()  : 0 );
				query.setParameter("grnDetailId", request.get(i).getGrnDetailId() != null ? request.get(i).getGrnDetailId() : 0);
				query.setResultTransformer(Transformers.aliasToBean(ReturnableGatepassResponse.class));

				list = query.list();
			}
		}
		return list1;
	}

	@Override
	public Double totalDiscountAmountUsingBatchId(Long id, Integer getpassDetails) {
		String sql=" select trgd.total_discount  from procurement.t_good_receipt_note_detail trgd  " + 
				" where trgd.batch_Id =:Id and trgd.id =:getpassDetailsId ";
        Query query = getSession().createSQLQuery(sql);
        query.setParameter("Id", id !=null ? id : 0 );
        query.setParameter("getpassDetailsId", getpassDetails !=null ? getpassDetails : 0 );

        return (Double) query.uniqueResult();
	}

	@Override
	public Double totalAmountUsingBatchId(Long id, Integer getpassDetails) {
		String sql=" select trgd.total_amount  from procurement.t_good_receipt_note_detail trgd  " + 
				" where trgd.batch_Id =:Id and trgd.id =:getpassDetailsId ";
        Query query = getSession().createSQLQuery(sql);
        query.setParameter("Id", id !=null ? id : 0 );
        query.setParameter("getpassDetailsId", getpassDetails !=null ? getpassDetails : 0 );

        return (Double) query.uniqueResult();
	}

}

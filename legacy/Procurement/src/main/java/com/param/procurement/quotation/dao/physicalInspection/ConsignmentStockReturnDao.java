package com.param.procurement.quotation.dao.physicalInspection;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.procurement.ConsignmentStockReturn;
import com.param.entity.model.procurement.ConsignmentStockReturnDetail;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNDetailResponse;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNResponse;
import com.param.procurement.quotation.dto.consignmentStockReturn.ConsignmentStockReturnDetailHistory;
import com.param.procurement.quotation.dto.consignmentStockReturn.ConsignmentStockReturnDetailResponse;
import com.param.procurement.quotation.dto.consignmentStockReturn.ConsignmentStockReturnDetailSaveRequest;
import com.param.procurement.quotation.dto.physicalInspection.ConsignmentStockReturnRequest;
import com.param.procurement.quotation.dto.physicalInspection.ConsignmentStockReturnResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Repository
public class ConsignmentStockReturnDao extends AbstractDao<Integer, ConsignmentStockReturn>
		implements IConsignmentStockReturnDao {

	final static Logger logger = Logger.getLogger(ConsignmentStockReturnDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<ConsignmentStockReturnResponse> getConsignmentStockListResponse(
			ConsignmentStockReturnRequest consignmentStockReturnRequest, Integer screenId) {
		try {
			String hql = "SELECT consignmentStockReturn.id as id, consignmentStockReturn.crNumber as crNo, "
					+ " consignmentStockReturn.status.status as status, consignmentStockReturn.status.id as statusId, "
					+ " store.storeId as storeId, store.storeName as storeName, "
					+ " goodReceiptNote.id as grnId, goodReceiptNote.grnNumber as grnNo, "
					+ " assetType.id as assetTypeId, assetType.type as assetType, "
					+ " vendor.id as vendorId, vendor.name as vendorName, "
					+ " status.status as grnStatus,status.id as grnStatusId, "
					+ " goodReceiptNote.isConsignment as isConsignment "
					+ " FROM ConsignmentStockReturn AS consignmentStockReturn "
					+ " INNER JOIN consignmentStockReturn.goodReceiptNote AS goodReceiptNote "
					+ " INNER JOIN goodReceiptNote.status as status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.assetType AS assetType WITH assetType.isActive = true AND assetType.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " WHERE consignmentStockReturn.isActive = true AND consignmentStockReturn.isDeleted = false AND consignmentStockReturn.unit.unitId = :unitId "
					+ " AND consignmentStockReturn.isConsignment = true "
					+ " AND consignmentStockReturn.addedDate BETWEEN :fromDate AND :toDate "
					+ "	AND (:storeId = 0 OR store.storeId = :storeId) "
					+ " AND ( assetType.id = :assetType OR :assetType = 0 ) "
					+ " AND (:vendorId = 0 OR consignmentStockReturn.vendor.id = :vendorId) "
					+ " AND (:vendorName = '' OR lower(consignmentStockReturn.vendor.name) LIKE :vendorName ) "
					+ " AND (:statusId = 0 OR status.id = :statusId) "
					+ "	AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "								INNER JOIN screenStatus.status as status) "
					+ " ORDER BY consignmentStockReturn.id DESC";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(consignmentStockReturnRequest.getFromDate()));
			query.setParameter("toDate", Util.getDate(consignmentStockReturnRequest.getToDate()));
			query.setParameter("statusId",
					consignmentStockReturnRequest.getStatusId() != null ? consignmentStockReturnRequest.getStatusId()
							: 0);
			query.setParameter("storeId",
					consignmentStockReturnRequest.getStoreId() != null ? consignmentStockReturnRequest.getStoreId()
							: 0);
			query.setParameter("assetType",
					consignmentStockReturnRequest.getAssetType() != null ? consignmentStockReturnRequest.getAssetType()
							: 0);
			if (consignmentStockReturnRequest.getVendorId() != null
					&& consignmentStockReturnRequest.getVendorId() > 0) {
				query.setParameter("vendorId",
						consignmentStockReturnRequest.getVendorId() != null
								? consignmentStockReturnRequest.getVendorId()
								: 0);
				query.setParameter("vendorName", "");
			} else if (consignmentStockReturnRequest.getVendorName() != null) {
				query.setParameter("vendorId", 0);
				query.setParameter("vendorName",
						consignmentStockReturnRequest.getVendorName() != null
								? consignmentStockReturnRequest.getVendorName().toLowerCase() + '%'
								: "");
			} else {
				query.setParameter("vendorId", 0);
				query.setParameter("vendorName", "");
			}
			query.setParameter("unitId", consignmentStockReturnRequest.getUnitId()!=null ? consignmentStockReturnRequest.getUnitId(): sessionService.getUnitId());
			query.setParameter("screenId", screenId);
			query.setResultTransformer(Transformers.aliasToBean(ConsignmentStockReturnResponse.class));
			return query.list();
		} catch (Exception e) {
			logger.error("getConsignmentStockListResponse failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/* added by minesh */
	public List<Batch> getBatchListFromItem(Integer itemNum, String batchNum, String lotNum , Integer unitId) {
		Date current = TimeZoneComponent.getDateByzone(unitId!=null && unitId > 0 ? unitId : sessionService.getUnitId());
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();
		try {
			/*
			 * String hql = "SELECT batch FROM Batch AS batch " +
			 * "	INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true AND item.unit.unitId = :unitId "
			 * + " INNER JOIN FETCH batch.storeStockList AS storeStock " +
			 * " WHERE item.id = :id AND batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
			 * +
			 * " AND batch.item = :id AND batch.batchNo = :batchNum AND batch.lotNo = :lotNo "
			 * //minesh +
			 * " AND ((batch.isPharmacy = false ) OR (batch.isIndent = false) OR (batch.isReturn = false)) "
			 * +
			 * " AND  storeStock.store.storeId = :storeId AND batch.expiryDate > :greaterThanCurrentMonth "
			 * + " ORDER BY batch.expiryDate ASC";
			 */

			String sql = "select  batch.id as id,batch.item_id as item , batch.batch_no as batchNo ,batch.lot_no as lotNo ,batch.quantity as quantity, "
					+ "batch.current_quantity as currentQuantity, batch.expiry_date as expiryDate, batch.cop as cop, batch.rate as rate, "
					+ "batch.selling_price as sellingPrice, batch.lending_price as lendingPrice, batch.actual_lending_price as actualLendingPrice, "
					+ "batch.year_of_manufacture as yearOfManufacture, batch.rejected_quantity as rejectedQuantity, batch.returned_quantity as returnedQuantity, "
					+ "batch.is_consignment as isConsignment " + "from inventory.t_batch batch "
					+ "inner join public.m_item item1 on batch.item_id=item1.id and (item1.is_deleted=false and item1.is_active=true and item1.unit_id= :unitId) "
					+ "inner join inventory.t_store_stock storestock2 on batch.id=storestock2.batch_id where item1.id= :id and batch.is_deleted=false and "
					+ "batch.is_active=true and batch.unit_id=:unitId and batch.item_id= :id and batch.batch_no= :batchNum and batch.lot_no= :lotNo "
					+ "and (batch.is_pharmacy=false or batch.is_indent=false) and batch.is_return=false and batch.is_quarantine=false and "
					+ "storestock2.store_id= :storeId and batch.expiry_date >= :greaterThanCurrentMonth order by batch.expiry_date ASC";

	       if(batchNum == null)
	       {
	    	   sql =sql.replace(" and batch.batch_no= :batchNum", " ");
	       }
			SQLQuery query = getSession().createSQLQuery(sql).addScalar("id", StandardBasicTypes.LONG)
					.addScalar("batchNo", StandardBasicTypes.STRING).addScalar("lotNo", StandardBasicTypes.STRING)
					.addScalar("quantity", StandardBasicTypes.INTEGER)
					.addScalar("currentQuantity", StandardBasicTypes.INTEGER)
					.addScalar("expiryDate", StandardBasicTypes.DATE).addScalar("cop", StandardBasicTypes.DOUBLE)
					.addScalar("rate", StandardBasicTypes.DOUBLE).addScalar("sellingPrice", StandardBasicTypes.DOUBLE)
					.addScalar("lendingPrice", StandardBasicTypes.DOUBLE)
					.addScalar("actualLendingPrice", StandardBasicTypes.DOUBLE)
					.addScalar("yearOfManufacture", StandardBasicTypes.INTEGER)
					.addScalar("rejectedQuantity", StandardBasicTypes.INTEGER)
					.addScalar("returnedQuantity", StandardBasicTypes.INTEGER)
					.addScalar("isConsignment", StandardBasicTypes.BOOLEAN);
			query.setParameter("id", itemNum);
			if(batchNum != null)
		       {
				query.setParameter("batchNum", batchNum);	
		       }
			
			query.setParameter("lotNo", lotNum);
			query.setParameter("unitId",unitId!=null && unitId > 0 ? unitId : sessionService.getUnitId());
			query.setParameter("storeId", sessionService.getStoreId());
			query.setParameter("greaterThanCurrentMonth", current);
			// query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			query.setResultTransformer(Transformers.aliasToBean(Batch.class));
			return query.list();
		} catch (Exception e) {
			logger.error("getBatchListFromItem failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	public List<Batch> getSameBatchList(Integer itemId, String batchNum, String lotNum , Integer unitId) {
		Date current = TimeZoneComponent.getDateByzone(unitId);
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();
		try {
			String sql = "select  batch.id as id,batch.item_id as item , batch.batch_no as batchNo ,batch.lot_no as lotNo ,batch.quantity as quantity, "
					+ "batch.current_quantity as currentQuantity, batch.expiry_date as expiryDate, batch.cop as cop, batch.rate as rate, "
					+ "batch.selling_price as sellingPrice, batch.lending_price as lendingPrice, batch.actual_lending_price as actualLendingPrice, "
					+ "batch.year_of_manufacture as yearOfManufacture, batch.rejected_quantity as rejectedQuantity, batch.returned_quantity as returnedQuantity, "
					+ "batch.is_consignment as isConsignment " + "from inventory.t_batch batch "
					+ "inner join public.m_item item1 on batch.item_id=item1.id and (item1.is_deleted=false and item1.is_active=true and item1.unit_id= :unitId) "
					+ "inner join inventory.t_store_stock storestock2 on batch.id=storestock2.batch_id where item1.id= :id and batch.is_deleted=false and "
					+ "batch.is_active=true and batch.unit_id=:unitId and batch.item_id= :id and batch.batch_no= :batchNum and batch.lot_no= :lotNo "
					+ "and (batch.is_pharmacy=false or batch.is_indent=false) and batch.is_return=false and batch.is_quarantine=false and "
					+ "storestock2.store_id= :storeId and storestock2.available_qty > 0 and batch.expiry_date >= :greaterThanCurrentMonth order by batch.current_quantity ASC";

			SQLQuery query = getSession().createSQLQuery(sql).addScalar("id", StandardBasicTypes.LONG)
					.addScalar("batchNo", StandardBasicTypes.STRING).addScalar("lotNo", StandardBasicTypes.STRING)
					.addScalar("quantity", StandardBasicTypes.INTEGER)
					.addScalar("currentQuantity", StandardBasicTypes.INTEGER)
					.addScalar("expiryDate", StandardBasicTypes.DATE).addScalar("cop", StandardBasicTypes.DOUBLE)
					.addScalar("rate", StandardBasicTypes.DOUBLE).addScalar("sellingPrice", StandardBasicTypes.DOUBLE)
					.addScalar("lendingPrice", StandardBasicTypes.DOUBLE)
					.addScalar("actualLendingPrice", StandardBasicTypes.DOUBLE)
					.addScalar("yearOfManufacture", StandardBasicTypes.INTEGER)
					.addScalar("rejectedQuantity", StandardBasicTypes.INTEGER)
					.addScalar("returnedQuantity", StandardBasicTypes.INTEGER)
					.addScalar("isConsignment", StandardBasicTypes.BOOLEAN);
			query.setParameter("id", itemId);
			query.setParameter("batchNum", batchNum);
			query.setParameter("lotNo", lotNum);
			query.setParameter("unitId", unitId!=null && unitId > 0 ? unitId : sessionService.getUnitId());
			query.setParameter("storeId", sessionService.getStoreId());
			query.setParameter("greaterThanCurrentMonth", current);
			query.setResultTransformer(Transformers.aliasToBean(Batch.class));
			return query.list();

		} catch (Exception e) {
			logger.error("getSameBatchList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	/* end added by minesh */

	@Override
	public List<ConsignmentStockReturnDetailSaveRequest> getAllGoodReceiptNoteCR(
			ConsignmentStockReturnRequest consignmentStockReturnRequest) {
		try {
			String hql = "SELECT grn.id as id, assetType.id as assetTypeId, assetType.type as assetType, "
					+ " requestType.id as grnTypeId, requestType.type as grnType,"
					+ " grn.grnNumber as grnNumber, grn.addedDate as grnDate, "
					+ " status.id as statusId, status.status as status,"
					+ " approvalStatus.id as approvalStatusId, approvalStatus.status as approvalStatus,"
					+ " store.storeId as storeId, store.storeName as StoreName,"
					+ " grn.isConsignment as isConsignment, grn.totalAmount as totalAmount, "
					+ " vendor.id as vendorId, vendor.name as vendorName,"
					+ " grn.remark as remark, addedBy.userName as addedBy " + " FROM GoodReceiptNote AS grn "
					+ " LEFT JOIN grn.assetType AS assetType " + " LEFT JOIN grn.requestType AS requestType "
					+ " LEFT JOIN grn.discountType AS discountType " + " LEFT JOIN grn.status AS status "
					+ " LEFT JOIN grn.approvalStatus AS approvalStatus " + " LEFT JOIN grn.store AS store "
					+ " LEFT JOIN grn.requestingStore AS requestingStore " + " LEFT JOIN grn.vendor AS vendor "
					+ " LEFT JOIN grn.addedBy AS addedBy "
					+ " LEFT JOIN grn.assetType as assetType WITH assetType.isActive = true AND assetType.isDeleted = false " // add  by  minesh
					+ " WHERE grn.isActive = true AND grn.isDeleted = false AND grn.isConsignment = true AND status.id = 8 " //grn status close id = 8 (after phy approve)
					+ " AND grn.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND (:grnNumber = '' OR lower(grn.grnNumber) LIKE :grnNumber) "
					+ " AND (:vendorId = 0 OR grn.vendor.id = :vendorId) "
					+ " AND (:vendorName = '' OR lower(grn.vendor.name) LIKE :vendorName ) "
					+ "	AND (:storeId = 0 OR store.storeId = :storeId) AND grn.unit.unitId = :unitId "
					+ "	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(consignmentStockReturnRequest.getFromDate()));
			query.setParameter("toDate", Util.getDate(consignmentStockReturnRequest.getToDate()));
			if (consignmentStockReturnRequest.getVendorId() != null
					&& consignmentStockReturnRequest.getVendorId() > 0) {
				query.setParameter("vendorId",
						consignmentStockReturnRequest.getVendorId() != null
								? consignmentStockReturnRequest.getVendorId()
								: 0);
				query.setParameter("vendorName", "");
			} else if (consignmentStockReturnRequest.getVendorName() != null) {
				query.setParameter("vendorId", 0);
				query.setParameter("vendorName",
						consignmentStockReturnRequest.getVendorName() != null
								? consignmentStockReturnRequest.getVendorName().toLowerCase() + '%'
								: "");
			} else {
				query.setParameter("vendorId", 0);
				query.setParameter("vendorName", "");
			}
			query.setParameter("grnNumber",
					consignmentStockReturnRequest.getGrnNo() != null
							? '%'+consignmentStockReturnRequest.getGrnNo().toLowerCase()+'%'
							: "");
			query.setParameter("storeId",
					consignmentStockReturnRequest.getStoreId() != null ? consignmentStockReturnRequest.getStoreId()
							: 0);
			query.setParameter("unitId", consignmentStockReturnRequest.getUnitId()!=null ? consignmentStockReturnRequest.getUnitId():sessionService.getUnitId());
			query.setParameter("assetTypeId",
					consignmentStockReturnRequest.getAssetTypeId() != null
							? consignmentStockReturnRequest.getAssetTypeId()
							: 0);

			query.setResultTransformer(Transformers.aliasToBean(ConsignmentStockReturnDetailSaveRequest.class));
			return query.list();
		} catch (Exception e) {
			logger.error("getAllGoodReceiptNoteCR failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public ConsignmentStockReturnDetailSaveRequest getGoodReceiptNoteCR(
			ConsignmentStockReturnRequest consignmentStockReturnRequest) {
		try {
			String hql = "SELECT grn.id as id, assetType.id as assetTypeId, assetType.type as assetType, "
					+ " requestType.id as grnTypeId, requestType.type as grnType,"
					+ " grn.grnNumber as grnNumber, grn.addedDate as grnDate, "
					+ " status.id as statusId, status.status as status,"
					+ " approvalStatus.id as approvalStatusId, approvalStatus.status as approvalStatus,"
					+ " store.storeId as storeId, store.storeName as StoreName,"
					+ " grn.isConsignment as isConsignment, grn.totalAmount as totalAmount, "
					+ " vendor.id as vendorId, vendor.name as vendorName,"
					+ " grn.remark as remark, addedBy.userName as addedBy " + " FROM GoodReceiptNote AS grn "
					+ " LEFT JOIN grn.assetType AS assetType " + " LEFT JOIN grn.requestType AS requestType "
					+ " LEFT JOIN grn.discountType AS discountType " + " LEFT JOIN grn.status AS status "
					+ " LEFT JOIN grn.approvalStatus AS approvalStatus " + " LEFT JOIN grn.store AS store "
					+ " LEFT JOIN grn.requestingStore AS requestingStore " + " LEFT JOIN grn.vendor AS vendor "
					+ " LEFT JOIN grn.addedBy AS addedBy "
					+ " LEFT JOIN grn.assetType as assetType WITH assetType.isActive = true AND assetType.isDeleted = false " // add
																																// by
																																// minesh
					+ " WHERE grn.isActive = true AND grn.isDeleted = false AND grn.isConsignment = true"
					+ " AND grn.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND (:grnNumber = '' OR lower(grn.grnNumber) LIKE :grnNumber) "
					+ " AND (:vendorId = 0 OR grn.vendor.id = :vendorId) "
					+ " AND (:vendorName = '' OR lower(grn.vendor.name) LIKE :vendorName ) "
					+ "	AND (:storeId = 0 OR store.storeId = :storeId) AND grn.unit.unitId = :unitId "
					+ "	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(consignmentStockReturnRequest.getFromDate()));
			query.setParameter("toDate", Util.getDate(consignmentStockReturnRequest.getToDate()));
			if (consignmentStockReturnRequest.getVendorId() != null
					&& consignmentStockReturnRequest.getVendorId() > 0) {
				query.setParameter("vendorId",
						consignmentStockReturnRequest.getVendorId() != null
								? consignmentStockReturnRequest.getVendorId()
								: 0);
				query.setParameter("vendorName", "");
			} else if (consignmentStockReturnRequest.getVendorName() != null) {
				query.setParameter("vendorId", 0);
				query.setParameter("vendorName",
						consignmentStockReturnRequest.getVendorName() != null
								? consignmentStockReturnRequest.getVendorName().toLowerCase() + '%'
								: "");
			} else {
				query.setParameter("vendorId", 0);
				query.setParameter("vendorName", "");
			}
			query.setParameter("grnNumber",
					consignmentStockReturnRequest.getGrnNo() != null
							? consignmentStockReturnRequest.getGrnNo().toLowerCase()
							: "");
			query.setParameter("storeId",
					consignmentStockReturnRequest.getStoreId() != null ? consignmentStockReturnRequest.getStoreId()
							: 0);
			query.setParameter("unitId", consignmentStockReturnRequest.getUnitId()!=null ? consignmentStockReturnRequest.getUnitId(): sessionService.getUnitId());
			query.setParameter("assetTypeId",
					consignmentStockReturnRequest.getAssetTypeId() != null
							? consignmentStockReturnRequest.getAssetTypeId()
							: 0);

			query.setResultTransformer(Transformers.aliasToBean(ConsignmentStockReturnDetailSaveRequest.class));
			return (ConsignmentStockReturnDetailSaveRequest) query.setMaxResults(1).uniqueResult();
		} catch (Exception e) {
			logger.error("getGoodReceiptNoteCR failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<GRNResponse> getGoodReceiptNoteBPList(ConsignmentStockReturnRequest consignmentStockReturnRequest) {
		try {
			String hql = "SELECT grn.id as id, assetType.id as assetTypeId, assetType.type as assetType, "
					+ "requestType.id as grnTypeId, requestType.type as grnType,"
					+ "grn.grnNumber as grnNumber, grn.addedDate as grnDate, "
					+ "discountType.id as discountTypeId, discountType.type as discountType,"
					+ "status.id as statusId, status.status as status,"
					+ "approvalStatus.id as approvalStatusId, approvalStatus.status as approvalStatus,"
					+ "store.storeId as storeId, store.storeName as StoreName,"
					+ "grn.dcDate as dcDate, grn.dcNumber as dcNumber, grn.discount as discount, grn.invoiceDate as invoiceDate,"
					+ " grn.invoiceNumber as invoiceNumber, grn.inwardDate as inwardDate, grn.inwardNumber as inwardNumber, "
					+ " grn.isConsignment as isConsignment, grn.netAmount as netAmount, grn.roundOff as roundOff,"
					+ " grn.totalAmount as totalAmount, grn.totalDiscount as totalDiscount, grn.totalOtherAmount as totalOtherAmount,"
					+ " grn.totalTaxAmmount as totalTaxAmount, " + "vendor.id as vendorId, vendor.name as vendorName,"
					+ " grn.remark as remark , grn.billTypeName as billTypeName,"
					+ " addedBy.userName as addedBy , requestingStore.storeId as requestingStoreId , requestingStore.storeName as requestingStoreName  "
					+ " FROM GoodReceiptNote AS grn " + " LEFT JOIN grn.assetType AS assetType "
					+ " LEFT JOIN grn.requestType AS requestType " + " LEFT JOIN grn.discountType AS discountType "
					+ " LEFT JOIN grn.status AS status " + " LEFT JOIN grn.approvalStatus AS approvalStatus "
					+ " LEFT JOIN grn.store AS store " + " LEFT JOIN grn.requestingStore AS requestingStore "
					+ " LEFT JOIN grn.vendor AS vendor " + " LEFT JOIN grn.addedBy AS addedBy "
					+ " WHERE grn.isActive = true AND grn.isDeleted = false "
					+ " AND grn.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND (:grnNumber = '' OR lower(grn.grnNumber) LIKE :grnNumber) "
					+ " AND (:vendorId = 0 OR grn.vendor.id = :vendorId) "
					+ " AND (:vendorName = '' OR lower(grn.vendor.name) LIKE :vendorName ) "
					+ "	AND (:storeId = 0 OR store.storeId = :storeId) AND grn.unit.unitId = :unitId ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(consignmentStockReturnRequest.getFromDate()));
			query.setParameter("toDate", Util.getDate(consignmentStockReturnRequest.getToDate()));
			if (consignmentStockReturnRequest.getVendorId() != null
					&& consignmentStockReturnRequest.getVendorId() > 0) {
				query.setParameter("vendorId",
						consignmentStockReturnRequest.getVendorId() != null
								? consignmentStockReturnRequest.getVendorId()
								: 0);
				query.setParameter("vendorName", "");
			} else if (consignmentStockReturnRequest.getVendorName() != null) {
				query.setParameter("vendorId", 0);
				query.setParameter("vendorName",
						consignmentStockReturnRequest.getVendorName() != null
								? consignmentStockReturnRequest.getVendorName().toLowerCase() + '%'
								: "");
			} else {
				query.setParameter("vendorId", 0);
				query.setParameter("vendorName", "");
			}
			query.setParameter("grnNumber",
					consignmentStockReturnRequest.getGrnNo() != null
							? consignmentStockReturnRequest.getGrnNo().toLowerCase()
							: "");
			query.setParameter("storeId", sessionService.getStoreId());
			query.setParameter("unitId", sessionService.getUnitId());

			query.setResultTransformer(Transformers.aliasToBean(GRNResponse.class));
			return query.list();
		} catch (Exception e) {
			logger.error("getGoodReceiptNoteBPList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<GRNDetailResponse> getGoodReceiptNoteDetailsBP(
			ConsignmentStockReturnRequest consignmentStockReturnRequest) {

		String hql = "SELECT grnDtls.id as id, item.id as itemId, "
				+ "grnDtls.grnApprovedQuantity as grnApprovedQuantity, grnDtls.bonusApprovedQuantity as bonusApprovedQuantity, "
				+ "purchaseOrderDetail.poApprovedQuantity as poQuantity, purchaseOrderDetail.bonusApprovedQuantity as poBonusQuantity,"
				+ "grnDtls.grnReceivedQuantity as grnReceivedQuantity, grnDtls.bonusReceivedQuantity as bonusReceivedQuantity,"
				+ "grnDtls.batchNo as batchNo, grnUomType.id as grnUomTypeId, grnUomUnit.id as grnUomUnitId, "
				+ "bonusUomType.id as bonusUomTypeId, bonusUomUnit.id as bonusUomUnitId, "
				+ "grnDtls.cop as cop, grnDtls.mrp as mrp, grnDtls.discount as discount, "
				+ "grnDtls.expiryDate as expiryDate, grnDtls.lotNumber as lotNumber, "
				+ "manufacturer.id as manufacturerId, tax.id as taxId, grnDtls.taxPercentage as taxPercentage, grnDtls.totalAmount as totalAmount,"
				+ "grnDtls.taxAmount as taxAmount, grnDtls.netAmount as netAmount, grnDtls.totalDiscount as totalDiscount, "
				+ "grnDtls.totalOtherAmount as totalOtherAmount, "
				+ "status.id as statusId, grnDtls.specification as grnSpecification,"
				+ "grnDtls.remark as remark, grnDtls.rejectedQuantity as rejectedQuantity, "
				+ "item.code as itemCode, item.name as itemName, "
				+ "grnUomType.type as grnUomType, grnUomUnit.units as grnUomUnit, "
				+ "bonusUomType.type as bonusUomType,  bonusUomUnit.units as bonusUomUnit, "
				+ "manufacturer.name as manufacturerName, tax.code as taxCode, status.status as status,"
				+ "grnDtls.grnReceivedQuantity as poGrnQuantity, purchaseOrderDetail.poApprovedQuantity as poPendingQuantity, "
				+ " store.storeId as storeId, assetType.id as assetTypeId, purchaseOrderDetail.id AS poDetailId, purchaseOrder.poNumber AS poNo "
				+ " ,batch.currentQuantity as availableBatchQuantity  " // added by minesh
				+ " FROM GoodReceiptNoteDetail AS grnDtls " + " INNER JOIN grnDtls.goodReceiptNote AS grn "
				+ " LEFT JOIN grn.store AS store " + " LEFT JOIN grn.assetType AS assetType "
				+ " LEFT JOIN grnDtls.item AS item " + " LEFT JOIN grnDtls.grnUomType AS grnUomType "
				+ " LEFT JOIN grnDtls.grnUomUnit AS grnUomUnit " + " LEFT JOIN grnDtls.bonusUomType AS bonusUomType "
				+ " LEFT JOIN grnDtls.bonusUomUnit AS bonusUomUnit " + " LEFT JOIN grnDtls.tax AS tax "
				+ " LEFT JOIN grnDtls.status AS status  " + " LEFT JOIN grnDtls.manufacturer AS manufacturer "
				+ " LEFT JOIN grnDtls.purchaseOrderDetail AS purchaseOrderDetail "
				+ " LEFT JOIN purchaseOrderDetail.purchaseOrder AS purchaseOrder "
				+ " INNER JOIN grnDtls.batch as batch "
				+ " WHERE grn.id = :id  AND grn.isActive = true AND grn.isDeleted = false"
				+ " AND grnDtls.isActive = true AND grnDtls.isDeleted = false order by grnDtls.id";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", consignmentStockReturnRequest.getGrnId());

		query.setResultTransformer(Transformers.aliasToBean(GRNDetailResponse.class));

		return query.list();
	}

	@Override
	public List<ConsignmentStockReturnDetailResponse> getGoodReceiptNoteDetailsCR(
			ConsignmentStockReturnRequest consignmentStockReturnRequest) {
		try {

			String hql = "SELECT grnDtls.id as goodReceiptNoteDetailId, item.id as itemId, "
					+ " grnDtls.grnReceivedQuantity as grnReceivedQuantity, grnDtls.batchNo as batchNo, grnUomType.id as grnUomTypeId, grnUomUnit.id as grnUomUnitId, "
					+ " grnDtls.cop as cop, grnDtls.mrp as mrp, grnDtls.expiryDate as expiryDate, grnDtls.lotNumber as lotNumber, "
					+ " manufacturer.id as manufacturerId, tax.id as taxId, grnDtls.taxPercentage as taxPercentage, grnDtls.totalAmount as totalAmount, "
					+ " grnDtls.taxAmount as taxAmount, grnDtls.netAmount as netAmount, " + " status.id as statusId, "
					+ " grnDtls.remark as remark," + " item.code as itemCode, item.name as itemName, "
					+ " grnUomType.type as grnUomType, grnUomUnit.units as grnUomUnit, "
					+ " manufacturer.name as manufacturerName, tax.code as taxCode, status.status as status,"
					+ " store.storeId as storeId, assetType.id as assetTypeId, "
					+ " batch.currentQuantity as availableBatchQuantity, batch.id as batchId  ,"
					+ " batch.quantity as totalConsignmentStock, batch.lotNo as lotNo ,  grnDtls.specification as specification " // added
																																								// by
																																								// minesh
					+ " FROM GoodReceiptNoteDetail AS grnDtls " + " INNER JOIN grnDtls.goodReceiptNote AS grn "
					+ " LEFT JOIN grn.store AS store " + " LEFT JOIN grn.assetType AS assetType "
					+ " LEFT JOIN grnDtls.item AS item " + " LEFT JOIN grnDtls.grnUomType AS grnUomType "
					+ " LEFT JOIN grnDtls.grnUomUnit AS grnUomUnit " + " LEFT JOIN grnDtls.tax AS tax "
					+ " LEFT JOIN grnDtls.status AS status  " + " LEFT JOIN grnDtls.manufacturer AS manufacturer "
					+ " INNER JOIN grnDtls.batch as batch "
					+ " WHERE grn.id = :id  AND grn.isActive = true AND grn.isDeleted = false"
					+ " AND grnDtls.isActive = true AND grnDtls.isDeleted = false order by grnDtls.id";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", consignmentStockReturnRequest.getGrnId());

			query.setResultTransformer(Transformers.aliasToBean(ConsignmentStockReturnDetailResponse.class));

			return query.list();
		} catch (Exception e) {
			logger.error("getGoodReceiptNoteDetailsCR failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public ConsignmentStockReturn saveConsignmentStockReturn(ConsignmentStockReturn consignmentStockReturn) {
		getSession().save(consignmentStockReturn);
		return consignmentStockReturn;
	}

	@Override
	public ConsignmentStockReturn getConsignmentStockReturn(Integer id , Integer unitId) {
		try {
			String hql = "SELECT consignmentStockReturn FROM ConsignmentStockReturn AS consignmentStockReturn"
					+ " INNER JOIN consignmentStockReturn.goodReceiptNote AS goodReceiptNote WITH goodReceiptNote.isActive = true AND goodReceiptNote.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.status as status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.assetType AS assetType WITH assetType.isActive = true AND assetType.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " WHERE consignmentStockReturn.isActive = true AND consignmentStockReturn.isDeleted = false AND "
					+ " consignmentStockReturn.id=:id AND consignmentStockReturn.unit.unitId = :unitId "
					// + " AND (store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND consignmentStockReturn.isActive = true AND consignmentStockReturn.isDeleted = false "
					+ " ORDER BY consignmentStockReturn.id DESC";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId!=null && unitId > 0 ? unitId :sessionService.getUnitId());
			// query.setParameter("storeId", sessionService.getStoreId());

			return (ConsignmentStockReturn) query.uniqueResult();
		} catch (Exception e) {
			logger.error("getConsignmentStockReturn failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public ConsignmentStockReturnDetail getConsignmentStockReturnDetail(Integer id , Integer unitId) {
		// TODO Auto-generated method stub
		String hql = " SELECT consignmentStockReturnDetail FROM ConsignmentStockReturnDetail AS consignmentStockReturnDetail "
				+ " INNER JOIN consignmentStockReturnDetail.consignmentStockReturn AS consignmentStockReturn WITH consignmentStockReturn.isActive = true AND consignmentStockReturn.isDeleted = false "
				+ " INNER JOIN consignmentStockReturnDetail.goodReceiptNoteDetail AS goodReceiptNoteDetail WITH goodReceiptNoteDetail.isActive = true AND goodReceiptNoteDetail.isDeleted = false "
				+ " INNER JOIN goodReceiptNoteDetail.item AS item WITH item.isActive = true AND item.isDeleted = false "
				+ " WHERE consignmentStockReturnDetail.isDeleted = false AND consignmentStockReturnDetail.isActive = true "
				+ " AND consignmentStockReturnDetail.id = :id AND consignmentStockReturnDetail.unit.unitId = :unitId";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("unitId", unitId!=null && unitId > 0 ? unitId : sessionService.getUnitId());

		return (ConsignmentStockReturnDetail) query.setMaxResults(1).uniqueResult();
	}

	@Override
	public List<ConsignmentStockReturnDetail> getConsignmentStockReturnDetails(Integer id , Integer unitId) {
		try {
			String hql = " SELECT consignmentStockReturnDetail FROM ConsignmentStockReturnDetail AS consignmentStockReturnDetail "
					+ " INNER JOIN consignmentStockReturnDetail.consignmentStockReturn AS consignmentStockReturn WITH consignmentStockReturn.isActive = true AND consignmentStockReturn.isDeleted = false "
					+ " INNER JOIN consignmentStockReturnDetail.goodReceiptNoteDetail AS goodReceiptNoteDetail WITH goodReceiptNoteDetail.isActive = true AND goodReceiptNoteDetail.isDeleted = false "
					+ " INNER JOIN goodReceiptNoteDetail.item AS item WITH item.isActive = true AND item.isDeleted = false "
					// + " INNER JOIN goodReceiptNoteDetail.grnUomType AS grnUomType WITH
					// grnUomType.isActive = true AND grnUomType.isDeleted = false"
					// + " INNER JOIN goodReceiptNoteDetail.grnUomUnit AS grnUomUnit WITH
					// grnUomUnit.isActive = true AND grnUomUnit.isDeleted = false "
					+ " WHERE consignmentStockReturnDetail.isDeleted = false AND consignmentStockReturnDetail.isActive = true "
					+ " AND consignmentStockReturn.id = :id AND consignmentStockReturnDetail.unit.unitId = :unitId";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId",unitId!=null && unitId > 0 ? unitId : sessionService.getUnitId());

			return query.list();
		} catch (Exception e) {
			logger.error("getConsignmentStockReturnDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public ConsignmentStockReturnDetail updateconsignmentStockReturnDetail(
			ConsignmentStockReturnDetail consignmentStockReturnDetail) {
		getSession().update(consignmentStockReturnDetail);
		return consignmentStockReturnDetail;
	}

	@Override
	public ConsignmentStockReturn updateconsignmentStockReturn(ConsignmentStockReturn consignmentStockReturn) {
		getSession().update(consignmentStockReturn);
		return consignmentStockReturn;
	}

	@Override
	public void updateBatch(Long batchId, Integer returnQuantity) {
		String sql = " update inventory.t_batch set returned_quantity=returned_quantity+" + returnQuantity
				+ "  where id=" + batchId;
		Query query = getSession().createSQLQuery(sql);
		query.executeUpdate();
	}

	@Override
	public void updateBatchData(Long batchId, Integer returnBatchQuantity) {

		String sql = " update inventory.t_batch set returned_quantity=returned_quantity+" + returnBatchQuantity
				+ " , current_quantity=current_quantity-" + returnBatchQuantity + "  where id=" + batchId +" and is_consignment=true";
		Query query = getSession().createSQLQuery(sql);
		query.executeUpdate();

	}

	@Override
	public List<Batch> getBatchList(String batchNo, String lotNo) {

		try {
		//	String hql = " SELECT batch FROM Batch AS batch   WHERE batch.batchNo =:batchNo and batch.lotNo =:lotNo";
			
			String hql = " SELECT batch FROM Batch AS batch   WHERE batch.batchNo =:batchNo and batch.lotNo =:lotNo order by batch.id";
			Query query = getSession().createQuery(hql);
			query.setParameter("batchNo", batchNo);
			query.setParameter("lotNo", lotNo);
			return query.list();
		} catch (Exception e) {
			logger.error("getBatchList failed", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public void updateStoreStock(String batchNo, String lotNo, Integer returnQtyFromUi) {
		//String sql = " update inventory.t_store_stock storeStock set storestock.available_qty=storestock.available_qty-"+returnQtyFromUi+"  from table inventory.t_store_stock storeStock  join table inventory.t_batch bth on storeStock.batch_id =bth.id where bth.batch_no=:batchNo and bth.lot_no=:lotNo  ";
		String sql=" update inventory.t_store_stock  set available_qty= coalesce(available_qty, 0) -"+returnQtyFromUi+" where available_qty in( select storestock.available_qty from inventory.t_store_stock storeStock join inventory.t_batch bth on storeStock.batch_id =bth.id where bth.batch_no=:batchNo and bth.lot_no=:lotNo and bth.is_consignment=true) ";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("batchNo", batchNo);
		query.setParameter("lotNo", lotNo);
		query.executeUpdate();	
	}
	//start code by minesh to changed above query(updateStoreStock)
	@Override
	public void updateStoreStock(String batchNo, String lotNo, Integer returnQtyFromUi,Long batchId, Integer storeId) {
		String sql=" update inventory.t_store_stock  set available_qty= coalesce(available_qty, 0) -"+returnQtyFromUi+" "
				+ "where available_qty in( select storestock.available_qty from inventory.t_store_stock storeStock join inventory.t_batch bth on storeStock.batch_id =bth.id "
				+ "where bth.batch_no=:batchNo and bth.lot_no=:lotNo and bth.is_consignment=true) and batch_id = :batchId and store_id=:storeId";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("batchNo", batchNo);
		query.setParameter("lotNo", lotNo);
		query.setParameter("batchId", batchId);
		query.setParameter("storeId", storeId);
		query.executeUpdate();
	}
	//end code by minesh to changed above query(updateStoreStock)

	@Override
	public void updateConsignmentTrueBatchData(Long id, Integer returnBatchQuantity) {


		String sql = " update inventory.t_batch set returned_quantity=returned_quantity+" + returnBatchQuantity
				+ " , current_quantity=current_quantity-" + returnBatchQuantity + "  where id=" + id +" and is_consignment=true";
		Query query = getSession().createSQLQuery(sql);
		query.executeUpdate();

	
		
	}

	@Override
	public void updateConsignmentFalseBatchData(Long id, Integer returnBatchQuantity) {



		String sql = " update inventory.t_batch set returned_quantity=returned_quantity+" + returnBatchQuantity
				+ " , current_quantity=current_quantity-" + returnBatchQuantity + "  where id=" + id +" and is_consignment=false";
		Query query = getSession().createSQLQuery(sql);
		query.executeUpdate();

	
		
	
		
	}

	@Override
	public void updateConsignmentFalseStoreStock(String batchNo, String lotNo, Integer returnQtyFromUi) {
		//String sql = " update inventory.t_store_stock storeStock set storestock.available_qty=storestock.available_qty-"+returnQtyFromUi+"  from table inventory.t_store_stock storeStock  join table inventory.t_batch bth on storeStock.batch_id =bth.id where bth.batch_no=:batchNo and bth.lot_no=:lotNo  ";
		String sql=" update inventory.t_store_stock  set available_qty= coalesce(available_qty, 0) -"+returnQtyFromUi+" where available_qty in( select storestock.available_qty from inventory.t_store_stock storeStock join inventory.t_batch bth on storeStock.batch_id =bth.id where bth.batch_no=:batchNo and bth.lot_no=:lotNo and bth.is_consignment=false) ";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("batchNo", batchNo);
		query.setParameter("lotNo", lotNo);
		query.executeUpdate();
	}
	//start code by minesh to changed above query(updateConsignmentFalseStoreStock)
	@Override
	public void updateConsignmentFalseStoreStock(String batchNo, String lotNo, Integer returnQtyFromUi,Long batchId,Integer storeId) {
		String sql=" update inventory.t_store_stock  set available_qty= coalesce(available_qty, 0) -"+returnQtyFromUi+" "
				+ " where available_qty in( select storestock.available_qty from inventory.t_store_stock storeStock join inventory.t_batch bth on storeStock.batch_id =bth.id "
				+ " where bth.batch_no=:batchNo and bth.lot_no=:lotNo and bth.is_consignment=false) and batch_id = :batchId and store_id=:storeId";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("batchNo", batchNo);
		query.setParameter("lotNo", lotNo);
		query.setParameter("batchId", batchId);
		query.setParameter("storeId", storeId);
		query.executeUpdate();
	}
	//end code by minesh to changed above query(updateConsignmentFalseStoreStock)
	
	public Batch getConsignmentBatch(Integer itemId,String batchNo, String lotNo) {
		String batchNoCondition = " ";
		String lotCondition = " ";
		if (batchNo != null) {
			batchNoCondition =" AND batch.batchNo ='"+batchNo+"' ";
		}
		if (lotNo != null) {
			lotCondition =" AND batch.lotNo='"+lotNo+"' ";
		}
		String hql = "SELECT batch FROM Batch as batch WHERE batch.isActive = true AND batch.isDeleted = false AND batch.isQuarantine = false "
				+ " AND batch.isConsignment = true AND batch.unit.unitId ="+sessionService.getUnitId()+" AND batch.item.id ="+itemId+""
				+ ""+batchNoCondition+""+lotCondition;

		Query query = getSession().createQuery(hql);
		return (Batch) query.uniqueResult();
	}

	@Override
	public List<ConsignmentStockReturnDetailHistory> getConsignmentStockreturnDetailHistory(Integer grnDetailId,Integer crId , Integer unitId) {
		Integer unit=unitId!=null && unitId > 0 ? unitId : sessionService.getUnitId();
		String sql ="select cr.cr_number as \"crNumber\",cr.added_date as \"crDate\" , crd.return_quantity as \"returnedQuantity\" from procurement.t_consignment_return_detail as crd " + 
				" inner join  procurement.t_consignment_return as cr  on cr.id = crd.cr_id " + 
				" where crd.grn_detail_id="+grnDetailId+" and crd.unit_id="+unit /*+" and cr.id < "+crId*/+ " and crd.status_id = 4";
	
		Query query = getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.aliasToBean(ConsignmentStockReturnDetailHistory.class));
		return query.list();
		
	}
	
	@Override
	public BigInteger getTotalPOCreatedQuantity(Integer grndId) {
		String sql = " select COALESCE(SUM(pod.po_approved_quantity),0) from procurement.t_purchase_order_detail as pod where pod.grn_detail_id ="+grndId+" and pod.unit_id="+sessionService.getUnitId();
		Query query = getSession().createSQLQuery(sql);		
			return (BigInteger) query.uniqueResult();		
	}

	@Override
	public Integer getconsignmentReturnedQuantity(ConsignmentStockReturnDetailResponse grnDetailResponse) {
		String sql="select cast(COALESCE(SUM(tcrd.return_quantity),0) as int) from procurement.t_consignment_return_detail tcrd where tcrd.grn_detail_id="+grnDetailResponse.getGoodReceiptNoteDetailId()+"";
		Query query = getSession().createSQLQuery(sql);		
		return (Integer) query.uniqueResult();
	}
	
}
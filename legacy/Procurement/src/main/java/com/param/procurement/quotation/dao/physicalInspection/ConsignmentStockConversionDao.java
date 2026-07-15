package com.param.procurement.quotation.dao.physicalInspection;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.procurement.GoodReceiptNote;
import com.param.entity.model.procurement.PhysicalInspection;
import com.param.entity.model.procurement.PhysicalInspectionDetail;
import com.param.entity.model.procurement.ShelfItem;
import com.param.global.dto.AllDataResponse;
import com.param.global.utility.Util;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionRequest;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionSave;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;
import com.param.global.common.ServiceException;
import org.apache.log4j.Logger;

@Repository
public class ConsignmentStockConversionDao extends AbstractDao<Integer, GoodReceiptNote>
		implements IConsignmentStockConversionDao {

	final static Logger logger = Logger.getLogger(ConsignmentStockConversionDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<PhysicalInspection> getPhysicalnspectionList(PhysicalInspectionRequest physicalInspectionRequest,
			Integer screenId) {
		try {

			String hql = "SELECT physicalInspection FROM PhysicalInspection AS physicalInspection "
					+ " INNER JOIN physicalInspection.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN physicalInspection.goodReceiptNote AS goodReceiptNote WITH goodReceiptNote.isActive = true AND goodReceiptNote.isDeleted = false "
					+ " AND goodReceiptNote.isConsignment = true " // added by minesh
					+ " INNER JOIN goodReceiptNote.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.assetType AS assetType WITH assetType.isActive = true AND assetType.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.conversionStatus AS conversionStatus "
					//start code for conversionStatus from batch rather than grn by minesh 
					/*+ " INNER JOIN goodReceiptNote.goodReceiptNoteDetailsList AS goodReceiptNoteDetailsList WITH goodReceiptNoteDetailsList.isActive = true AND goodReceiptNoteDetailsList.isDeleted = false "
					+ " INNER JOIN goodReceiptNoteDetailsList.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false "
					+ " INNER JOIN batch.conversionStatus AS conversionStatus "*/
					//end code for conversionStatus from batch rather than grn by minesh 
					+ " WHERE physicalInspection.unit.unitId = :unitId AND physicalInspection.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND ( conversionStatus.id = :conversionStatusId OR :conversionStatusId = 0 ) "
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND ( assetType.id = :assetType OR :assetType = 0 ) "
					+ " AND physicalInspection.isActive = true AND physicalInspection.isDeleted = false "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen AS screen WITH screen.id = :screenId "
					+ "	INNER JOIN screenStatus.status AS status) " + " ORDER BY physicalInspection.id DESC ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(physicalInspectionRequest.getFromDate()));
			query.setParameter("toDate", Util.getDate(physicalInspectionRequest.getToDate()));
			// query.setParameter("statusId", physicalInspectionRequest.getStatusId() !=
			// null ? physicalInspectionRequest.getStatusId() : 0);
			// use for close satus of physical inspection
			query.setParameter("conversionStatusId", physicalInspectionRequest.getStatusId() != null ? physicalInspectionRequest.getStatusId() : 0);
			query.setParameter("storeId",
					physicalInspectionRequest.getStoreId() != null ? physicalInspectionRequest.getStoreId() : 0);
			query.setParameter("assetType",
					physicalInspectionRequest.getAssetType() != null ? physicalInspectionRequest.getAssetType() : 0);
			query.setParameter("unitId", physicalInspectionRequest.getUnitId()!=null ? physicalInspectionRequest.getUnitId(): sessionService.getUnitId());
			query.setParameter("screenId", screenId);

			return query.list();
		} catch (Exception e) {
			logger.error("getPhysicalnspectionList failed", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public PhysicalInspection getPhysicalnspectionDetails(Integer id) {
		try {

			String hql = " SELECT physicalInspection FROM PhysicalInspection AS physicalInspection "
					+ " INNER JOIN physicalInspection.goodReceiptNote AS goodReceiptNote WITH goodReceiptNote.isActive = true AND goodReceiptNote.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " LEFT JOIN store.rack AS rack WITH rack.isActive = true AND rack.isDeleted = false "
					+ " LEFT JOIN rack.shelfList AS shelf WITH shelf.isActive = true AND shelf.isDeleted = false "
					+ " LEFT JOIN shelf.shelfList AS shelfItem WITH shelfItem.isActive = true AND shelfItem.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.vendor As vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.assetType AS assetType WITH assetType.isActive = true AND assetType.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.goodReceiptNoteDetailsList AS goodReceiptNoteDetailsList WITH goodReceiptNoteDetailsList.isActive = true AND goodReceiptNoteDetailsList.isDeleted = false "
					+ " LEFT JOIN goodReceiptNoteDetailsList.purchaseOrderDetail AS purchaseOrderDetail WITH purchaseOrderDetail.isActive = true"
					+ " AND purchaseOrderDetail.isDeleted = false "
					+ " WHERE physicalInspection.id = :id AND physicalInspection.unit.unitId = :unitId "
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND physicalInspection.isActive = true AND physicalInspection.isDeleted = false ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", sessionService.getUnitId());
			query.setParameter("storeId", sessionService.getStoreId());

			return (PhysicalInspection) query.uniqueResult();
		} catch (Exception e) {
			logger.error("getPhysicalnspectionDetails failed", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public PhysicalInspection getPhysicalnspectionDetailsForSearch(PhysicalInspectionSave physicalInspectionSave) {
		try {

			String hql = " SELECT physicalInspection FROM PhysicalInspection AS physicalInspection "
					+ " INNER JOIN physicalInspection.goodReceiptNote AS goodReceiptNote WITH goodReceiptNote.isActive = true AND goodReceiptNote.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " LEFT JOIN store.rack AS rack WITH rack.isActive = true AND rack.isDeleted = false "
					+ " LEFT JOIN rack.shelfList AS shelf WITH shelf.isActive = true AND shelf.isDeleted = false "
					+ " LEFT JOIN shelf.shelfList AS shelfItem WITH shelfItem.isActive = true AND shelfItem.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.vendor As vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.assetType AS assetType WITH assetType.isActive = true AND assetType.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.goodReceiptNoteDetailsList AS goodReceiptNoteDetailsList WITH goodReceiptNoteDetailsList.isActive = true AND goodReceiptNoteDetailsList.isDeleted = false "
					+ " LEFT JOIN goodReceiptNoteDetailsList.purchaseOrderDetail AS purchaseOrderDetail WITH purchaseOrderDetail.isActive = true"
					+ " AND purchaseOrderDetail.isDeleted = false "
					+ " WHERE physicalInspection.id = :id AND physicalInspection.unit.unitId = :unitId "
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND physicalInspection.isActive = true AND physicalInspection.isDeleted = false ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", physicalInspectionSave.getId());
			query.setParameter("unitId", sessionService.getUnitId());
			query.setParameter("storeId", physicalInspectionSave.getStoreId());

			return (PhysicalInspection) query.uniqueResult();
		} catch (Exception e) {
			logger.error("getPhysicalnspectionDetailsForSearch failed", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public PhysicalInspection getPhysicalnspectionDetailsForSearch(
			PhysicalInspectionRequest physicalInspectionRequest) {
		try {

			String hql = " SELECT physicalInspection FROM PhysicalInspection AS physicalInspection "
					+ " INNER JOIN physicalInspection.goodReceiptNote AS goodReceiptNote WITH goodReceiptNote.isActive = true AND goodReceiptNote.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " LEFT JOIN store.rack AS rack WITH rack.isActive = true AND rack.isDeleted = false "
					+ " LEFT JOIN rack.shelfList AS shelf WITH shelf.isActive = true AND shelf.isDeleted = false "
					+ " LEFT JOIN shelf.shelfList AS shelfItem WITH shelfItem.isActive = true AND shelfItem.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.vendor As vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.assetType AS assetType WITH assetType.isActive = true AND assetType.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.goodReceiptNoteDetailsList AS goodReceiptNoteDetailsList WITH goodReceiptNoteDetailsList.isActive = true AND goodReceiptNoteDetailsList.isDeleted = false "
					+ " LEFT JOIN goodReceiptNoteDetailsList.purchaseOrderDetail AS purchaseOrderDetail WITH purchaseOrderDetail.isActive = true"
					+ " AND purchaseOrderDetail.isDeleted = false "
					+ " WHERE physicalInspection.id = :id AND physicalInspection.unit.unitId = :unitId "
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND physicalInspection.isActive = true AND physicalInspection.isDeleted = false ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", physicalInspectionRequest.getId());
			query.setParameter("unitId", physicalInspectionRequest.getUnitId()!=null ? physicalInspectionRequest.getUnitId(): sessionService.getUnitId());
			query.setParameter("storeId", physicalInspectionRequest.getStoreId());

			return (PhysicalInspection) query.uniqueResult();
		} catch (Exception e) {
			logger.error("getPhysicalnspectionDetailsForSearch failed", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public PhysicalInspection savePhysicalnspection(PhysicalInspection physicalInspection) {
		getSession().update(physicalInspection);
		return physicalInspection;
	}

	@Override
	public PhysicalInspectionDetail getPhysicalInspectionDetailById(Integer id) {
		try {
			String hql = " SELECT physicalInspectionDetail FROM PhysicalInspectionDetail AS physicalInspectionDetail "
					+ " WHERE physicalInspectionDetail.isDeleted = false AND physicalInspectionDetail.isActive = true "
					+ " AND physicalInspectionDetail.id = :id AND physicalInspectionDetail.unit.unitId = :unitId";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", sessionService.getUnitId());

			return (PhysicalInspectionDetail) query.uniqueResult();
		} catch (Exception e) {
			logger.error("getPhysicalInspectionDetailById failed", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public List<PhysicalInspectionDetail> getPhysicalInspectionDetailByInspectionId(Integer id , Integer unitId) {
		try {
			String hql = " SELECT physicalInspectionDetail FROM PhysicalInspectionDetail AS physicalInspectionDetail "
					+ " INNER JOIN physicalInspectionDetail.PhysicalInspection AS PhysicalInspection WITH PhysicalInspection.isActive = true AND PhysicalInspection.isDeleted = false "
					+ " INNER JOIN physicalInspectionDetail.goodReceiptNoteDetail AS goodReceiptNoteDetail WITH goodReceiptNoteDetail.isActive = true AND goodReceiptNoteDetail.isDeleted = false "
					+ " INNER JOIN goodReceiptNoteDetail.item AS item WITH item.isActive = true AND item.isDeleted = false "
					// + " INNER JOIN goodReceiptNoteDetail.grnUomType AS grnUomType WITH
					// grnUomType.isActive = true AND grnUomType.isDeleted = false"
					// + " INNER JOIN goodReceiptNoteDetail.grnUomUnit AS grnUomUnit WITH
					// grnUomUnit.isActive = true AND grnUomUnit.isDeleted = false "
					// + " INNER JOIN goodReceiptNoteDetail.bonusUomType AS bonusUomType WITH
					// bonusUomType.isActive = true AND bonusUomType.isDeleted = false "
					// + " INNER JOIN goodReceiptNoteDetail.bonusUomUnit AS bonusUomUnit WITH
					// bonusUomUnit.isActive = true AND bonusUomUnit.isDeleted = false "
					+ " WHERE physicalInspectionDetail.isDeleted = false AND physicalInspectionDetail.isActive = true "
					+ " AND PhysicalInspection.id = :id AND physicalInspectionDetail.unit.unitId = :unitId";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId!=null && unitId > 0 ? unitId :sessionService.getUnitId());
			//System.out.println("unitIdSession:-" + sessionService.getUnitId());

			return query.list();
		} catch (Exception e) {
			logger.error("getPhysicalInspectionDetailByInspectionId failed", e);
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

			/*
			 * String sql =
			 * "select  batch.id as id,batch.item_id as item , batch.batch_no as \"batchNo\" ,batch.lot_no as \"lotNo\" ,batch.quantity as quantity, "
			 * +
			 * "batch.current_quantity as \"currentQuantity\", batch.expiry_date as \"expiryDate\", batch.cop as cop, batch.rate as rate, "
			 * +
			 * "batch.selling_price as \"sellingPrice\", batch.lending_price as \"lendingPrice\", batch.actual_lending_price as \"actualLendingPrice\", "
			 * +
			 * "batch.year_of_manufacture as \"yearOfManufacture\", batch.rejected_quantity as \"rejectedQuantity\", batch.is_consignment as \"isConsignment\" "
			 * + "from inventory.t_batch batch " +
			 * "inner join public.m_item item1 on batch.item_id=item1.id and (item1.is_deleted=false and item1.is_active=true and item1.unit_id= :unitId) "
			 * +
			 * "inner join inventory.t_store_stock storestock2 on batch.id=storestock2.batch_id where item1.id= :id and batch.is_deleted=false and "
			 * +
			 * "batch.is_active=true and batch.is_quarantine=false and batch.unit_id=1 and batch.item_id= :id and batch.batch_no= :batchNum and batch.lot_no= :lotNo "
			 * +
			 * "and (batch.is_pharmacy=false or batch.is_indent=false or batch.is_return=false) and "
			 * +
			 * "storestock2.store_id= :storeId and batch.expiry_date > :greaterThanCurrentMonth order by batch.expiry_date ASC"
			 * ;
			 */

			String sql = "select  batch.id as id,batch.item_id as item , batch.batch_no as batchNo ,batch.lot_no as lotNo ,batch.quantity as quantity, "
					+ "batch.current_quantity as currentQuantity, batch.expiry_date as expiryDate, batch.cop as cop, batch.rate as rate, "
					+ "batch.selling_price as sellingPrice, batch.lending_price as lendingPrice, batch.actual_lending_price as actualLendingPrice, "
					+ "batch.year_of_manufacture as yearOfManufacture, batch.rejected_quantity as rejectedQuantity, batch.returned_quantity as returnedQuantity, "
					+ "batch.is_consignment as isConsignment " + "from inventory.t_batch batch "
					+ "inner join public.m_item item1 on batch.item_id=item1.id and (item1.is_deleted=false and item1.is_active=true and item1.unit_id= :unitId) "
					+ "inner join inventory.t_store_stock storestock2 on batch.id=storestock2.batch_id where item1.id= :id and batch.is_deleted=false and "
					+ "batch.is_active=true and batch.is_quarantine=false and batch.unit_id=:unitId and batch.item_id= :id and upper(batch.batch_no)= :batchNum and batch.lot_no= :lotNo "
					+ "and (batch.is_pharmacy=false or batch.is_indent=false or batch.is_return=false) and "
				//	+ "storestock2.store_id= :storeId and batch.expiry_date >= :greaterThanCurrentMonth order by batch.expiry_date ASC";
					+ "storestock2.store_id= :storeId ";
			
			if(batchNum==null)
			{
				sql=sql.replace("batch.batch_no= :batchNum and", " ");
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
			if(batchNum!=null)
			{
				query.setParameter("batchNum", batchNum.toUpperCase());
			}
			
			query.setParameter("lotNo", lotNum);
			query.setParameter("unitId", unitId!=null && unitId > 0 ? unitId : sessionService.getUnitId());
			query.setParameter("storeId", sessionService.getStoreId());
			//query.setParameter("greaterThanCurrentMonth", current);
			// query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			query.setResultTransformer(Transformers.aliasToBean(Batch.class));
			return query.list();
		} catch (Exception e) {
			logger.error("getBatchListFromItem failed", e);
			throw new ServiceException(e.getMessage());
		}

	}
	/* end added by minesh */

	@Override
	public ShelfItem saveShelfItem(ShelfItem shelfItem) {
		getSession().save(shelfItem);
		return shelfItem;
	}

	@Override
	public Batch updateBatchQuantityAndFlag(Batch batch) {
		getSession().update(batch);
		return batch;
	}

	@Override
	public Batch getBatch(Long id) {

		try {

			String hql = " SELECT batch FROM Batch AS batch  where  batch.isActive = true AND batch.isDeleted = false and batch.id=:id and batch.isConsignment=false OR batch.isConsignment=true";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);

			return (Batch) query.uniqueResult();
		} catch (Exception e) {
			logger.error("getBatch failed", e);
			throw new ServiceException(e.getMessage());
		}

	
	}

	@Override
	public BigInteger getBatchReturnQty(Long batchId , Integer unitId) {
		BigInteger t = BigInteger.valueOf(0);
		//String sql = " select sum(returned_quantity) from inventory.t_batch where id=" + batchId;
		String sql = "SELECT COALESCE(SUM(batch1.returned_quantity), 0) FROM inventory.t_batch AS batch1 "
				+ " LEFT JOIN inventory.t_batch AS batch2 ON " + " batch1.batch_no = batch2.batch_no AND "
				+ " batch1.item_id=batch2.item_id " + " AND batch1.lot_no=batch2.lot_no "
				+ " WHERE batch2.id = :batchId And batch2.unit_id=:unitId";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("batchId", batchId);
		query.setParameter("unitId", unitId!=null && unitId > 0 ? unitId : sessionService.getUnitId());		
		return (BigInteger) query.uniqueResult();
	}

	@Override
	public Integer getAvailableConsignmentStock(Long batchId) {

		String sql = " select current_quantity from inventory.t_batch where is_consignment=true and   id=" + batchId;
		Query query = getSession().createSQLQuery(sql);
		if (query.uniqueResult() != null) {
			return (Integer) query.uniqueResult();
		} else {
			return 0;
		}

	}

	@Override
	public List<AllDataResponse> allDataList(Integer storeId, Integer batchId , String batchNo, String lotNo) {

		// Batch b =new Batch();
		int org_id = sessionService.getOrganizationId();
		int unit_id = sessionService.getUnitId();
		/*String sql = " select distinct sto.store_name as storeName , bth.batch_no as batchNo ,case   when stomap.available_qty < 0 then 0   else stomap.available_qty   end  as currentStock from public.m_store sto  inner join inventory.t_store_stock stomap on sto.store_id = stomap.store_id inner join inventory.t_batch bth on stomap.batch_id = bth.id inner join public.m_item itm on bth.item_id = itm.id  where  stomap.store_id !="
				+ storeId + " and bth.id=" + batchId
				+ " and  bth.is_consignment=true or bth.is_consignment=false and  bth.is_active=true  and bth.is_deleted=false  and sto.is_active=true and sto.is_deleted=false order by sto.store_name ,bth.batch_no   ";
*/
		String s= " select distinct sto.store_name as storeName , bth.batch_no as batchNo ,case   when stomap.available_qty < 0 then 0   else stomap.available_qty   end  as currentStock  from public.m_store sto  inner join inventory.t_store_stock stomap on sto.store_id = stomap.store_id inner join inventory.t_batch bth on stomap.batch_id = bth.id inner join public.m_item itm on bth.item_id = itm.id  where  (stomap.store_id !="+storeId+" and bth.id =:batchId ) and ( bth.is_consignment=true or bth.is_consignment=false ) and  (bth.is_active=true  and bth.is_deleted=false  and sto.is_active=true and sto.is_deleted=false ) order by sto.store_name ,bth.batch_no ";
		Query query = getSession().createSQLQuery(s);
		query.setParameter("batchId", batchId);
		//query.setParameter("lotNo", lotNo);
		query.setResultTransformer(Transformers.aliasToBean(AllDataResponse.class));
		return query.list();

	}

	@Override
	public BigInteger getStoreStockTransfered(Long batchId, Integer grnStoreId) {

		BigInteger t = BigInteger.valueOf(0);
		String sql = " select  sum (case   when stomap.available_qty < 0 then 0   else stomap.available_qty   end ) as currentStock from public.m_store sto  inner join inventory.t_store_stock stomap on sto.store_id = stomap.store_id  inner join inventory.t_batch bth on stomap.batch_id = bth.id  inner join public.m_item itm on bth.item_id = itm.id where  (bth.id="+batchId+") and (bth.is_consignment=true or bth.is_consignment=false) and  bth.is_active=true  and bth.is_deleted=false  and sto.is_active=true  and sto.is_deleted=false and stomap.store_id!="
				+ grnStoreId;
		 
		Query query = getSession().createSQLQuery(sql);
		 
		if (query.uniqueResult() != null) {
			return (BigInteger) query.uniqueResult();
		} else {
			return t;
		}

	}

	@Override
	public BigInteger getConsumedQty(String batchNo) {
		BigInteger t = BigInteger.valueOf(0);
		String sql = " select sum(quantity-current_quantity) from inventory.t_batch where batch_no=:batchNo and is_consignment=false";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("batchNo", batchNo);
		if (query.uniqueResult() != null) {
			return (BigInteger) query.uniqueResult();
		} else {
			return t;
		}
	}
	/* start code by minesh to changed above query(getConsumedQty) */
	@Override
	public BigInteger getConsumedQtyWithReturnedQty(String batchNo) {
		BigInteger t = BigInteger.valueOf(0);
		String sql = " select sum(quantity-current_quantity-returned_quantity) from inventory.t_batch where batch_no=:batchNo and is_consignment=false";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("batchNo", batchNo);
		if (query.uniqueResult() != null) {
			return (BigInteger) query.uniqueResult();
		} else {
			return t;
		}
	}
	//start code for resolved HIS-10266 Consignment - Incorrect converted stock , consumed consignment stock and pending PO quantity
	@Override
	public BigInteger getConsumedQtyWithReturnedQty(String batchNo, String lotNo) {		
		String batchNoCondition = " ",lotNoCondition = " ";
		if(batchNo!=null) {
			batchNoCondition = " and upper(batch_no)='"+batchNo.toUpperCase()+"' " ;
		}
		if(lotNo !=null) {
			lotNoCondition = " and lot_no='"+lotNo+"' ";
		}		
		String sql = " select COALESCE(sum(quantity-current_quantity-returned_quantity),0) from inventory.t_batch where is_consignment=true "
				+ " "+batchNoCondition+" "+ lotNoCondition;
		Query query = getSession().createSQLQuery(sql);
		return (BigInteger) query.uniqueResult();
	}
	//end code for resolved HIS-10266 Consignment - Incorrect converted stock , consumed consignment stock and pending PO quantity
	/*end code by minesh to changed above query(getConsumedQty)*/

	@Override
	public BigInteger getconvertedConsignmentStock(String batchNo) {

		BigInteger t = BigInteger.valueOf(0);
		String sql = " select sum(current_quantity) from inventory.t_batch where batch_no=:batchNo and is_consignment=false";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("batchNo", batchNo);
		if (query.uniqueResult() != null) {
			return (BigInteger) query.uniqueResult();
		} else {
			return t;
		}	
		
	}
	/*start code for resolved HIS-10266 Consignment - Incorrect converted stock , consumed consignment stock and pending PO quantity*/
	@Override
	public BigInteger getconvertedConsignmentStock(String batchNo, String lotNo) {
		String batchNoCondition = " ",lotNoCondition = " ";
		if(batchNo!=null) {
			batchNoCondition = " and upper(batch_no)='"+batchNo.toUpperCase()+"' " ;
		}
		if(lotNo !=null) {
			lotNoCondition = " and lot_no='"+lotNo+"' ";
		}		
		String sql = " select COALESCE(sum(quantity),0) from inventory.t_batch where is_consignment=false "
				+ " "+batchNoCondition+" "+ lotNoCondition;
		Query query = getSession().createSQLQuery(sql);
		return (BigInteger) query.uniqueResult();
	}
	/*end code for resolved HIS-10266 Consignment - Incorrect converted stock , consumed consignment stock and pending PO quantity*/

	@Override
	public Integer getTotalConsignmentStock(Integer id) {

		String sql = "  select approved_quantity from procurement.t_physical_inspection_detail where grn_detail_id="
				+ id;
		Query query = getSession().createSQLQuery(sql);
		if (query.uniqueResult() != null) {
			return (Integer) query.uniqueResult();
		} else {
			return 0;
		}

	}

	@Override
	public Integer getTotalConsignmentStockFromBatch(String batchNo, String lotNo) {

		Integer t = BigInteger.valueOf(0).intValue();
		String sql = " select sum(quantity) from inventory.t_batch where (upper(batch_no)=:batchNo and lot_no=:lotNo) and (is_consignment=true or is_consignment=false) ";
		
		//String s=" select sum(ss.total_qty) from inventory.t_batch b join inventory.t_store_stock ss\r\n" + 
		//		"on ss.batch_id=b.id\r\n" + 
		//		"where b.batch_no='ASP001' and b.lot_no='LOT-408' and ss.store_id=5; ";
		if(batchNo == null)
		{
			sql=sql.replace("batch_no=:batchNo and", "  ");	
		}
		Query query = getSession().createSQLQuery(sql);
		if(batchNo != null)
		{
			query.setParameter("batchNo", batchNo.toUpperCase());	
		}
	
		query.setParameter("lotNo", lotNo);
		if (query.uniqueResult() != null) {
			BigInteger stock = (BigInteger) query.uniqueResult();
			return stock.intValue();
		} else {
			return t;
		}

	}

	@Override
	public Integer getTotalStock(String batchNo) {


		Integer t = BigInteger.valueOf(0).intValue();
		String sql = " select sum(quantity) from inventory.t_batch where batch_no=:batchNo";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("batchNo", batchNo);
		//query.setParameter("lotNo", lotNo);
		if (query.uniqueResult() != null) {
			BigInteger stock = (BigInteger) query.uniqueResult();
			return stock.intValue();
		} else {
			return t;
		}

	
	}

	@Override
	public BigInteger getAvailableStock(String batchNo, String lotno, Boolean isconsignment,int storeId, int itemId,Integer unitId) {

//and b.item_id=995 and s.store_id=3
		BigInteger t = BigInteger.valueOf(0);
		//String sql = " select SUM(S.available_qty) from inventory.t_batch b inner join  inventory.t_store_stock S on B.id=S.batch_id where B.is_consignment=:isconsignment and B.batch_no=:batchNo and b.lot_no =:lotno";
		String sql = " select SUM(S.available_qty) from inventory.t_batch b inner join  inventory.t_store_stock S on B.id=S.batch_id where B.is_consignment=:isconsignment and B.batch_no=:batchNo and b.lot_no =:lotno"
				+ " and b.item_id =:itemId and s.store_id =:storeId and b.unit_id=:unitId and s.unit_id=:unitId and S.total_qty is not null and S.available_qty is not null ";
		
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("batchNo", batchNo);
		query.setParameter("lotno", lotno);
		
		query.setParameter("isconsignment", isconsignment);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		
		
		if (query.uniqueResult() != null) {
			return (BigInteger) query.uniqueResult();
		} else {
			return t;
		}

	
	}

	@Override
	public Integer getConsignmentReturnQty(String batchNo, String lotNumber, Integer id) {
		String sql = "  select return_quantity  from procurement.t_consignment_return_detail  where batch_no=:batchNo and lot_number=:lotNumber and grn_detail_id=:id  ";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("batchNo", batchNo);
		query.setParameter("lotNumber", lotNumber);
		query.setParameter("id", id);
		if (query.uniqueResult() != null) {
			return (Integer) query.uniqueResult();
		} else {
			return 0;
		}	
	}
	
	@Override
	public BigInteger getTotalConsignmentReturnQtyAgainGRNDetailId(Integer grnDetailId , Integer unitId) {
		String sql = "SELECT COALESCE(SUM(return_quantity), 0) FROM procurement.t_consignment_return_detail WHERE grn_detail_id=:grnDetailId AND unit_id=:unitId";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("grnDetailId", grnDetailId);
		query.setParameter("unitId",unitId!=null && unitId > 0 ? unitId : sessionService.getUnitId());		
		return (BigInteger) query.uniqueResult();
	}
	

	@Override
	public BigInteger getAvailableWithoutLotStock(String batchNo, Boolean isconsignment, int storeId, int itemId,Integer unitId) {


		//and b.item_id=995 and s.store_id=3
				BigInteger t = BigInteger.valueOf(0);
				//String sql = " select SUM(S.available_qty) from inventory.t_batch b inner join  inventory.t_store_stock S on B.id=S.batch_id where B.is_consignment=:isconsignment and B.batch_no=:batchNo and b.lot_no =:lotno";
				String sql = " select SUM(S.available_qty) from inventory.t_batch b inner join  inventory.t_store_stock S on B.id=S.batch_id where B.is_consignment=:isconsignment and B.batch_no=:batchNo "
						+ " and b.item_id =:itemId and s.store_id =:storeId and b.unit_id=:unitId and s.unit_id=:unitId and S.total_qty is not null and S.available_qty is not null ";
				
				Query query = getSession().createSQLQuery(sql);
				query.setParameter("batchNo", batchNo);
			//	query.setParameter("lotno", lotno);
				query.setParameter("isconsignment", isconsignment);
				query.setParameter("storeId", storeId);
				query.setParameter("itemId", itemId);
				query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
				
				if (query.uniqueResult() != null) {
					return (BigInteger) query.uniqueResult();
				} else {
					return t;
				}

			
			
	}

	@Override
	public BigInteger getAvailableWithoutBatchStock(String lotno, boolean isConsignmentFlag, int storeId, int itemId,Integer unitId) {

		//and b.item_id=995 and s.store_id=3
				BigInteger t = BigInteger.valueOf(0);
				//String sql = " select SUM(S.available_qty) from inventory.t_batch b inner join  inventory.t_store_stock S on B.id=S.batch_id where B.is_consignment=:isconsignment and B.batch_no=:batchNo and b.lot_no =:lotno";
				String sql = " select SUM(S.available_qty) from inventory.t_batch b inner join  inventory.t_store_stock S on B.id=S.batch_id where B.is_consignment=:isconsignment "
						+ " and b.item_id =:itemId and s.store_id =:storeId and b.unit_id=:unitId and s.unit_id=:unitId and S.total_qty is not null and S.available_qty is not null ";
				
				StringBuffer sb = new StringBuffer(sql);
				//sb.append((lotno!=null && !lotno.isEmpty())?" and  b.lot_no ='"+lotno+"'":"");

				if (lotno != null && !lotno.isEmpty()) {
					sb.append(" and b.lot_no =:lotno ");
					}

				Query query = getSession().createSQLQuery(sb.toString());
			//	query.setParameter("batchNo", batchNo);
				query.setParameter("isconsignment", isConsignmentFlag);
				query.setParameter("storeId", storeId);
				query.setParameter("itemId", itemId);
				query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
				if (lotno != null && !lotno.isEmpty()) {
					query.setParameter("lotno", lotno);
				}


				if (query.uniqueResult() != null) {
					return (BigInteger) query.uniqueResult();
				} else {
					return t;
				}

			
			
	}

	@Override
	public BigInteger getTotalGrnApprovedQty(String batchno, String lotno, boolean isConsignmentFlag, int storeId,
			int itemId) {
		String batchCondation="";
		if (batchno!= null) {
			batchCondation = " and b.batch_no='"+batchno+"' ";
		}
		String lotCondation="";
		if(lotno!=null) {
			lotCondation= " and b.lot_no='"+batchno+"' ";
		}
		
		String sql = "select  COALESCE(SUM(grnd.grn_received_quantity), 0) from inventory.t_batch B " +
				" inner join  inventory.t_store_stock S on B.id=S.batch_id "+
				" inner join  procurement.t_good_receipt_note_detail grnd on grnd.batch_id= B.id "+
				" where B.is_consignment="+isConsignmentFlag+" and S.store_id="+storeId+" and B.item_id ="+itemId+""+
				" and B.unit_id="+sessionService.getUnitId()+" and S.unit_id="+sessionService.getUnitId()+" "+
				batchCondation +""+lotCondation ;
				
		Query query = getSession().createSQLQuery(sql);
		return (BigInteger) query.uniqueResult();
	}

	@Override
	public BigInteger getTotalStoreTotalQty(String batchno, String lotno, boolean isConsignmentFlag, int storeId,
			int itemId, Integer unitId) {

		String batchCondation="";
		if (batchno!= null) {
			batchCondation = " and b.batch_no='"+batchno+"' ";
		}
		String lotCondation="";
		if(lotno!=null) {
			lotCondation= " and b.lot_no='"+lotno+"' ";
		}
		
		/*String sql = "select  COALESCE(SUM(S.total_qty), 0) from inventory.t_batch B " +
				" inner join  inventory.t_store_stock S on B.id=S.batch_id "+
				" where B.is_consignment="+isConsignmentFlag+" and S.store_id="+storeId+" and B.item_id ="+itemId+""+
				" and B.unit_id="+sessionService.getUnitId()+" and S.unit_id="+sessionService.getUnitId()+" "+
				batchCondation +""+lotCondation ;*/
		String sql = "select  COALESCE(SUM(S.total_qty), 0) from inventory.t_batch B " +
				" inner join  inventory.t_store_stock S on B.id=S.batch_id "+
				" where B.is_consignment="+isConsignmentFlag+" and B.item_id ="+itemId+" and S.total_qty is not null and S.available_qty is not null "+
				" and B.unit_id="+ (unitId !=null ? unitId : sessionService.getUnitId())+" and S.unit_id="+(unitId !=null ? unitId : sessionService.getUnitId())+" "+
				batchCondation +""+lotCondation ;
				
		Query query = getSession().createSQLQuery(sql);
		return (BigInteger) query.uniqueResult();
	}
	
	
	public BigInteger getAvailableBatchStock(String batchno, String lotno, int storeId, int itemId,Integer unitId) {

		String batchCondation="";
		if (batchno!= null) {
			batchCondation = " and B.batch_no='"+batchno+"' ";
		}
		String lotCondation="";
		if(lotno!=null) {
			lotCondation= " and B.lot_no='"+lotno+"' ";
		}
				
		String sql = "select  COALESCE(SUM(S.available_qty), 0) from inventory.t_batch B " +
				" inner join  inventory.t_store_stock S on B.id=S.batch_id  and S.total_qty is not null and S.available_qty is not null "+
				" where B.item_id ="+itemId+" and B.unit_id="+(unitId!=null ? unitId : sessionService.getUnitId())+" and S.unit_id="+(unitId!=null ? unitId : sessionService.getUnitId())+" and S.store_id="+storeId+" "+
				" and B.is_consignment is null " +
				batchCondation +" "+lotCondation ;
				
		Query query = getSession().createSQLQuery(sql);
		return (BigInteger) query.uniqueResult();
	}

	@Override
	public BigInteger getAvailableWithBatchIdStock(Long batchId, boolean isConsignmentFlag, Integer storeId,
			Integer id, Integer unitId) {



		//and b.item_id=995 and s.store_id=3
				BigInteger t = BigInteger.valueOf(0);
				//String sql = " select SUM(S.available_qty) from inventory.t_batch b inner join  inventory.t_store_stock S on B.id=S.batch_id where B.is_consignment=:isconsignment and B.batch_no=:batchNo and b.lot_no =:lotno";
				String sql = " select SUM(S.available_qty) from inventory.t_batch b inner join  inventory.t_store_stock S on B.id=S.batch_id where  B.id=:batchId "
						+ " and b.item_id =:itemId and s.store_id =:storeId and b.unit_id=:unitId and s.unit_id=:unitId and S.total_qty is not null and S.available_qty is not null ";
				
				Query query = getSession().createSQLQuery(sql);
				query.setParameter("batchId", batchId);
			//	query.setParameter("lotno", lotno);
				//query.setParameter("isconsignment", isConsignmentFlag);
				query.setParameter("storeId", storeId);
				query.setParameter("itemId", id);
				query.setParameter("unitId", unitId!=null ?unitId : sessionService.getUnitId());
				
				if (query.uniqueResult() != null) {
					return (BigInteger) query.uniqueResult();
				} else {
					return t;
				}

			
			
	
	}

	@Override
	public BigInteger getStoreStockTransferedByLotNo(String lotNo, Integer grnStoreId) {

		BigInteger t = BigInteger.valueOf(0);
		String sql = " select  sum (case   when stomap.available_qty < 0 then 0   else stomap.available_qty   end ) as currentStock from public.m_store sto  inner join inventory.t_store_stock stomap on sto.store_id = stomap.store_id  inner join inventory.t_batch bth on stomap.batch_id = bth.id  inner join public.m_item itm on bth.item_id = itm.id where  (bth.lot_no=:batchNo) and (bth.is_consignment=true or bth.is_consignment=false) and  bth.is_active=true  and bth.is_deleted=false  and sto.is_active=true  and sto.is_deleted=false and stomap.store_id!="
				+ grnStoreId;
		 
		Query query = getSession().createSQLQuery(sql);
		 query.setParameter("batchNo", lotNo);	
		 
		if (query.uniqueResult() != null) {
			return (BigInteger) query.uniqueResult();
		} else {
			return t;
		}

	}

	@Override
	public BigInteger getAvailableStockWithoutLotStock(Integer batchId, Boolean isconsignment, int storeId, int itemId,
			Integer unitId) {



		//and b.item_id=995 and s.store_id=3
				BigInteger t = BigInteger.valueOf(0);
				//String sql = " select SUM(S.available_qty) from inventory.t_batch b inner join  inventory.t_store_stock S on B.id=S.batch_id where B.is_consignment=:isconsignment and B.batch_no=:batchNo and b.lot_no =:lotno";
				String sql = " select SUM(S.available_qty) from inventory.t_batch b inner join  inventory.t_store_stock S on B.id=S.batch_id where B.is_consignment=:isconsignment and B.id=:batchId "
						+ " and b.item_id =:itemId and s.store_id =:storeId and b.unit_id=:unitId and s.unit_id=:unitId and S.total_qty is not null and S.available_qty is not null ";
				
				Query query = getSession().createSQLQuery(sql);
				query.setParameter("batchId", batchId);
			//	query.setParameter("lotno", lotno);
				query.setParameter("isconsignment", isconsignment);
				query.setParameter("storeId", storeId);
				query.setParameter("itemId", itemId);
				query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
				
				if (query.uniqueResult() != null) {
					return (BigInteger) query.uniqueResult();
				} else {
					return t;
				}

			
			
	
	}

}
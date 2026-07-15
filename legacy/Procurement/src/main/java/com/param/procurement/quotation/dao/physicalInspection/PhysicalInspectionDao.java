package com.param.procurement.quotation.dao.physicalInspection;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.constants.Status;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.procurement.GoodReceiptNote;
import com.param.entity.model.procurement.PhysicalInspection;
import com.param.entity.model.procurement.PhysicalInspectionDetail;
import com.param.entity.model.procurement.PurchaseOrderDetail;
import com.param.entity.model.procurement.ShelfItem;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionRequest;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionSave;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Repository
public class PhysicalInspectionDao extends AbstractDao<Integer, GoodReceiptNote> implements IPhysicalInspectionDao {

	final static Logger logger = Logger.getLogger(PhysicalInspectionDao.class);

	@Autowired
	private SessionService sessionService;
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public List<PhysicalInspection> getPhysicalnspectionList(PhysicalInspectionRequest physicalInspectionRequest,
			Integer screenId) {
		try {

			String hql = "SELECT physicalInspection FROM PhysicalInspection AS physicalInspection "
					+ " INNER JOIN physicalInspection.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN physicalInspection.goodReceiptNote AS goodReceiptNote WITH goodReceiptNote.isActive = true AND goodReceiptNote.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.assetType AS assetType WITH assetType.isActive = true AND assetType.isDeleted = false "
					+ " INNER JOIN goodReceiptNote.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " WHERE physicalInspection.unit.unitId = :unitId AND physicalInspection.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND ( status.id = :statusId OR :statusId = 0 ) "
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND ( assetType.id = :assetType OR :assetType = 0 ) "
					+ " AND physicalInspection.isActive = true AND physicalInspection.isDeleted = false "
					+ "  AND goodReceiptNote.approvalStatus.id !=:approvalStatusId "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen AS screen WITH screen.id = :screenId "
					+ "	INNER JOIN screenStatus.status AS status) " + " ORDER BY physicalInspection.id DESC ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(physicalInspectionRequest.getFromDate()));
			query.setParameter("toDate", Util.getDate(physicalInspectionRequest.getToDate()));
			query.setParameter("statusId",
					physicalInspectionRequest.getStatusId() != null ? physicalInspectionRequest.getStatusId() : 0);
			query.setParameter("storeId",
					physicalInspectionRequest.getStoreId() != null ? physicalInspectionRequest.getStoreId() : 0);
			query.setParameter("assetType",
					physicalInspectionRequest.getAssetType() != null ? physicalInspectionRequest.getAssetType() : 0);
			query.setParameter("unitId",
					physicalInspectionRequest.getUnitId() != null ? physicalInspectionRequest.getUnitId()
							: sessionService.getUnitId());
			query.setParameter("screenId", screenId);
			query.setParameter("approvalStatusId", Status.Rejected.getStatus());

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
			query.setParameter("unitId", physicalInspectionSave.getUnitId()!=null ? physicalInspectionSave.getUnitId() : sessionService.getUnitId());
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

			Query query = sessionFactory.getCurrentSession().createQuery(hql);
			query.setParameter("id", physicalInspectionRequest.getId());
			query.setParameter("unitId",
					physicalInspectionRequest.getUnitId() != null ? physicalInspectionRequest.getUnitId()
							: sessionService.getUnitId());
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
	public PhysicalInspectionDetail getPhysicalInspectionDetailById(Integer id,Integer unitId) {
		try {
			String hql = " SELECT physicalInspectionDetail FROM PhysicalInspectionDetail AS physicalInspectionDetail "
					+ " WHERE physicalInspectionDetail.isDeleted = false AND physicalInspectionDetail.isActive = true "
					+ " AND physicalInspectionDetail.id = :id AND physicalInspectionDetail.unit.unitId = :unitId";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());

			return (PhysicalInspectionDetail) query.uniqueResult();
		} catch (Exception e) {
			logger.error("getPhysicalInspectionDetailById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<PhysicalInspectionDetail> getPhysicalInspectionDetailByInspectionId(Integer id, Integer unitId) {
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
			query.setParameter("unitId", unitId != null && unitId > 0 ? unitId : sessionService.getUnitId());
			// System.out.println("unitIdSession:-"+sessionService.getUnitId());

			return query.list();
		} catch (Exception e) {
			logger.error("getPhysicalInspectionDetailByInspectionId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/* added by minesh */
	public List<Batch> getBatchListFromItem(Integer itemNum, String batchNum, String lotNum , Integer unitId) {
		Date current = TimeZoneComponent.getDateByzone(unitId);
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
					+ "batch.year_of_manufacture as yearOfManufacture, batch.rejected_quantity as rejectedQuantity, batch.is_consignment as isConsignment "
					+ "from inventory.t_batch batch "
					+ "inner join public.m_item item1 on batch.item_id=item1.id and (item1.is_deleted=false and item1.is_active=true and item1.unit_id= :unitId) "
					+ "inner join inventory.t_store_stock storestock2 on batch.id=storestock2.batch_id where item1.id= :id and batch.is_deleted=false and "
					+ "batch.is_active=true and batch.is_quarantine=false and batch.unit_id=:unitId and batch.item_id= :id and batch.batch_no= :batchNum and batch.lot_no= :lotNo "
					+ "and (batch.is_pharmacy=false or batch.is_indent=false or batch.is_return=false) and "
					+ "storestock2.store_id= :storeId and batch.expiry_date >= :greaterThanCurrentMonth order by batch.expiry_date ASC";

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
					.addScalar("isConsignment", StandardBasicTypes.BOOLEAN);
			query.setParameter("id", itemNum);
			query.setParameter("batchNum", batchNum);
			query.setParameter("lotNo", lotNum);
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
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
	public PurchaseOrderDetail getPoDetails(Integer id) {

		String hql = "SELECT purchaseOrderDetail FROM PurchaseOrderDetail AS purchaseOrderDetail "
				+ " WHERE purchaseOrderDetail.id = :id  AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false "
				+ " ORDER BY purchaseOrderDetail.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return (PurchaseOrderDetail) query.uniqueResult();

	}

	@Override
	public PhysicalInspection getPhysicalnspectionDetailsForAuto(Integer id, Integer unitId, Integer storeId) {

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
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
			query.setParameter("storeId", storeId != null ? storeId : sessionService.getStoreId());

			return (PhysicalInspection) query.uniqueResult();
		} catch (Exception e) {
			logger.error("getPhysicalnspectionDetailsForAuto failed", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Integer getGrnSize(Integer poId, List<Integer> poDetailsId) {

		String sql = " select count(gd.id) from procurement.t_good_receipt_note_detail  gd join procurement.t_good_receipt_note g "
				   + " on gd.grn_id=g.id and g.po_id=:poId  and gd.po_detail_id in (:poDetailsId)  ";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("poId", poId);
		query.setParameterList("poDetailsId", poDetailsId);
		BigInteger co=(BigInteger) query.uniqueResult();
		Integer c=co.intValue();
		return c;

	}

	@Override
	public Long getBatchId(Integer id) {


		String sql = " select batch_id from procurement.t_good_receipt_note_detail where id=:id  ";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("id", id); 
		Integer co=(Integer) query.uniqueResult(); 
		return co.longValue();

	
	}

	@Override
	public void updateStockTransaction(Integer detailsId,Integer availableQty, Integer leaseQty,Integer screenId, String screenCode, Boolean intrasit, Integer rejectedQuantity,boolean phyFlag) {
		try {
			try {
				String query="";
				if(phyFlag==false)
				{
					 query = "select MAX(id) as \"stockTransactionId\" from inventory.t_stock_transaction where  transaction_details_id =:detailsId and doc_id =:screenId and quantity="+leaseQty+"+"+rejectedQuantity+" group by id  ";
				}
				else
				{
				 query = "select MAX(id) as \"stockTransactionId\" from inventory.t_stock_transaction where  transaction_details_id =:detailsId and doc_id =:screenId and quantity="+leaseQty+" group by id  ";
				}
				PhysicalInspectionRequest dto = (PhysicalInspectionRequest) sessionFactory.getCurrentSession()
						.createSQLQuery(query).setParameter("detailsId", detailsId).setParameter("screenId", 13)
						.setResultTransformer(Transformers.aliasToBean(PhysicalInspectionRequest.class)).uniqueResult();
				
				if(dto!= null) {
					Integer rejectedQty=0;
					Integer result = getSession()
							.createSQLQuery(" update inventory.t_stock_transaction  set is_transit  = :intrasit ,quantity=:leaseQty,current_bal =:availableQty ,out_qty=:rejectedQuantity,in_qty=:leaseQty, doc_id=:screenId,doc_type=:screenCode where id =:id ")
							.setParameter("id", dto.getStockTransactionId())
							.setParameter("availableQty", availableQty)
							.setParameter("leaseQty", leaseQty)
							.setParameter("screenId", screenId)
							.setParameter("screenCode", screenCode)
							.setParameter("rejectedQuantity", rejectedQty)
							.setParameter("intrasit", intrasit)
							.executeUpdate();
				}
				
			} catch (HibernateException he) {
				throw new HibernateException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateStockTransaction failed", e);
				throw new ServiceException(e.getMessage());
			}
		} catch (Exception e) {
			logger.error("updateStockTransaction failed", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Integer getGrnApproveedQty(Integer id) {
		try {
			String query = "select " + 
					"tgrnd.grn_approved_quantity as \"approvedQuantity\" " + 
					"from procurement.t_physical_inspection_detail tpid  " + 
					"inner join procurement.t_good_receipt_note_detail tgrnd on tgrnd.id = tpid.grn_detail_id  " + 
					"where tpid.id = :id ";
			PhysicalInspectionDetail dto = (PhysicalInspectionDetail) sessionFactory.getCurrentSession()
					.createSQLQuery(query).setParameter("id", id)
					.setResultTransformer(Transformers.aliasToBean(PhysicalInspectionDetail.class)).uniqueResult();
			return dto.getApprovedQuantity()!= null ? dto.getApprovedQuantity() : 0;
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
			logger.error("getGrnApproveedQty failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<Batch> getBatchIds(Integer id) {
		String sql = "select  cast(batch.batch_id as int) as id "
				+ "from procurement.t_grn_bonus_batch_expiry batch where batch.grn_detail_id=:id ";

		SQLQuery query = getSession().createSQLQuery(sql).addScalar("id", StandardBasicTypes.LONG);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(Batch.class));
		return query.list();
	}

	@Override
	public void updateStockTransactionForBonusBatch(Integer detailsId,Integer availableQty, Integer leaseQty,Integer screenId, String screenCode, Boolean intrasit, Integer rejectedQuantity, Integer batchId) {
		try {
			try {
				String query = "select MAX(id) as \"stockTransactionId\" from inventory.t_stock_transaction where  transaction_details_id =:detailsId and doc_id =:screenId and batch_id=:batchId and quantity="+leaseQty+" group by id order by id asc limit 1 ";

				PhysicalInspectionRequest dto = (PhysicalInspectionRequest) sessionFactory.getCurrentSession()
						.createSQLQuery(query).setParameter("detailsId", detailsId).setParameter("screenId", 13).setParameter("batchId", batchId)
						.setResultTransformer(Transformers.aliasToBean(PhysicalInspectionRequest.class)).uniqueResult();
				
				if(dto!= null) {
					Integer rejectedQty=0;
					Integer result = getSession()
							.createSQLQuery(" update inventory.t_stock_transaction  set is_transit  = :intrasit ,current_bal =:availableQty ,out_qty=:rejectedQuantity,in_qty=:leaseQty, doc_id=:screenId,doc_type=:screenCode where id =:id and batch_id=:batchId ")
							.setParameter("id", dto.getStockTransactionId())
							.setParameter("availableQty", availableQty)
							.setParameter("leaseQty", leaseQty)
							.setParameter("screenId", screenId)
							.setParameter("screenCode", screenCode)
							.setParameter("rejectedQuantity", rejectedQty)
							.setParameter("intrasit", intrasit)
							.setParameter("batchId", batchId)
							.executeUpdate();
				}
				
			} catch (HibernateException he) {
				throw new HibernateException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateStockTransactionForBonusBatch failed", e);
				throw new ServiceException(e.getMessage());
			}
		} catch (Exception e) {
			logger.error("updateStockTransactionForBonusBatch failed", e);
			throw new ServiceException(e.getMessage());
		}

	}
}
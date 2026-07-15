package com.param.global.pharmacy.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.dao.AbstractDao;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.master.Item;
import com.param.entity.model.pharmacy.BatchAllocation;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.entity.model.pharmacy.TempPatientRegistration;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.AdmissionListIpdChargesResDto;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.Response;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.AllDataResponse;
import com.param.global.dto.AvailableQty;
import com.param.global.dto.CommonDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.pharmacy.dto.SaleDetailPackageMapperDto;
import com.param.global.pharmacy.dto.WalkInSalesRequest;
import com.param.service.global.SessionService;

import org.apache.log4j.Logger;
@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class WalkInSalesDao extends AbstractDao<Integer, Item> implements IWalkInSalesDao {
	final static Logger logger = Logger.getLogger(WalkInSalesDao.class);


	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	private SessionService sessionService;

	@Autowired
	IGlobalDao iGlobalDao;

	@Override
	public Integer allocateQuantity(BatchAllocation batchAllocation) {
		getSession().save(batchAllocation);
		return batchAllocation.getId();
	}

	@Override
	public List<Long> allocateQuantity(List<BatchAllocation> batchAllocationlist) {

		List<Long> allocationIds = new ArrayList<Long>();
		for (BatchAllocation batchAllocation : batchAllocationlist) {
			getSession().save(batchAllocation);
			allocationIds.add(batchAllocation.getId().longValue());
		}
		return allocationIds;
	}

	@Override
	public Integer updateAllocatedQuantity(BatchAllocation batchAllocation) {

		return batchAllocation.getId();
	}

	@Override
	public List<Long> updateAllocatedQuantity(List<BatchAllocation> batchAllocationlist) {
		List<Long> allocationIds = new ArrayList<Long>();
		for (BatchAllocation batchAllocation : batchAllocationlist) {
			getSession().update(batchAllocation);
			allocationIds.add(batchAllocation.getId().longValue());
		}
		return allocationIds;
	}

	@Override
	public BatchAllocation getBatchAllocation(Integer allocationId) {

		String hql = "SELECT batchAllocation FROM BatchAllocation AS batchAllocation \r\n "
				+ " INNER JOIN batchAllocation.batch AS batch WITH batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false  "
				+ " WHERE batchAllocation.id = :allocationId AND batchAllocation.isDeleted = false AND batchAllocation.isActive = true \r\n"
				+ " and batchAllocation.unit.unitId=:unitId";
		Query query = getSession().createQuery(hql);
		query.setParameter("allocationId", allocationId);
		query.setParameter("unitId", sessionService.getUnitId());

		return (BatchAllocation) query.uniqueResult();
	}

	@Override
	public List<BatchAllocation> getBatchAllocations(Long tempBillId) {

		String hql = "SELECT batchAllocation FROM BatchAllocation AS batchAllocation \r\n"
				+ "WHERE batchAllocation.tempBillId = :tempBillId AND batchAllocation.isDeleted = false AND batchAllocation.isActive = true \r\n"
				+ " and batchAllocation.unit.unitId=:unitId";

		Query query = getSession().createQuery(hql);
		query.setParameter("tempBillId", tempBillId);
		query.setParameter("unitId", sessionService.getUnitId());

		return query.list();
	}

	@Override
	public boolean deleteBatchAllocation(Integer allocationId) {

		String hql = "DELETE FROM BatchAllocation AS batchAllocation WHERE batchAllocation.id = :allocationId";

		Query query = getSession().createQuery(hql);
		query.setParameter("allocationId", allocationId);

		return query.executeUpdate() > 0;
	}

	@Override
	public boolean deleteBatchAllocations(Long tempBillId) {

		String hql = "DELETE FROM BatchAllocation AS batchAllocation WHERE batchAllocation.tempBillId = :tempBillId";

		Query query = getSession().createQuery(hql);
		query.setParameter("tempBillId", tempBillId);

		return query.executeUpdate() > 0;
	}

	@Override
	public Sale saveBill(Sale sale) {
		try {
			getSession().save(sale);
			return sale;

		} catch (HibernateException he) {
		logger.error("saveBill failed", he);
		} catch (Exception e) {
		logger.error("saveBill failed", e);
		}
		return null;
	}

	@Override
	public StockTransaction saveStockTransaction(StockTransaction stockTransaction) {
		getSession().save(stockTransaction);

		return stockTransaction;
	}

	@Override
	public Prescription getPrescription(Long id) {
		try {

			String hql = "SELECT prescription FROM Prescription AS prescription \r\n"
					+ "WHERE prescription.id = :id AND prescription.isDeleted = false AND prescription.isActive = true \r\n";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);

			return (Prescription) query.uniqueResult();
		} catch (Exception e) {
		logger.error("getPrescription failed", e);
		}
		return null;
	}

	@Override
	public PrescriptionDetail getPrescriptionDetail(Long id) {
		try {
			String hql = "SELECT prescriptionDetail FROM PrescriptionDetail AS prescriptionDetail \r\n"
					+ "WHERE prescriptionDetail.id = :id AND prescriptionDetail.isDeleted = false AND prescriptionDetail.isActive = true \r\n";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);

			return (PrescriptionDetail) query.uniqueResult();
		} catch (Exception e) {
		logger.error("getPrescriptionDetail failed", e);
		}
		return null;
	}

	@Override
	public Prescription updatePrescription(Prescription prescription) {
		getSession().update(prescription);

		return prescription;
	}

	@Override
	public BatchAllocation getBatchAllocation(Long tempBillId, Long batchId) {
		String hql = "SELECT allocation FROM BatchAllocation AS allocation "
				+ "		INNER JOIN allocation.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false  AND batch.isPharmacy = false  "
				+ "	WHERE allocation.isDeleted = false AND allocation.isActive = true "
				+ "		AND allocation.tempBillId = :billId AND batch.id = :batchId " + "	ORDER BY allocation.id";

		Query query = getSession().createQuery(hql);
		query.setParameter("billId", tempBillId);
		query.setParameter("batchId", batchId);
		query.setMaxResults(1);

		return (BatchAllocation) query.uniqueResult();
	}

	@Override
	public SaleDetail saveTax(SaleDetail saleDetail) {
		getSession().save(saleDetail);
		return saleDetail;
	}

	@Override
	public TempPatientRegistration savePatient(TempPatientRegistration tempPatientRegistration) {
		getSession().save(tempPatientRegistration);
		return tempPatientRegistration;
	}

	@Override
	public Integer updateConsolidatedBillStatus(Long id, Integer billId) {
		String sql = "update pharmacy.t_sale set is_consolidated_bill = true,bill_id =:billId where id= :saleId ";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("saleId", id);
		query.setParameter("billId", billId);
		return (Integer) query.executeUpdate();
	}

	@Override
	public Integer updateConsolidatedBillStatusBySaleDetails(List<Integer> orderDetailsId, Integer billId) {
		if (orderDetailsId != null && orderDetailsId.size() > 0) {
			String sql = "update pharmacy.t_sale set is_consolidated_bill = true,bill_id =:billId,status_id=4,paid_amount = outstanding_amount where id in (select coalesce(om.sale_id,0) from public.t_order_details tod inner join public.t_order_master om on om.order_id = tod.order_id where tod.order_details_id  In (:id)) ";
			Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
			query.setParameterList("id", orderDetailsId);
			query.setParameter("billId", billId);
			query.executeUpdate();

			String sql2 = "update pharmacy.t_pharmacy_bill_payee_details set due_amount = 0 where t_sale_id in (select coalesce(om.sale_id,0) from public.t_order_details tod inner join public.t_order_master om on om.order_id = tod.order_id where tod.order_details_id  In (:id)) ";
			Query query2 = sessionFactory.getCurrentSession().createSQLQuery(sql2);
			query2.setParameterList("id", orderDetailsId);
			return (Integer) query2.executeUpdate();
		}
		return 0;
	}

	@Override
	public Integer activatePharmacyBillAfterBillCancel(Integer billId) {
		if (billId != null && billId > 0) {

			String stringQuery = "select id from pharmacy.t_sale ts where bill_id =" + billId;

			List<Integer> saleIds = sessionFactory.getCurrentSession().createSQLQuery(stringQuery).list();

			if (saleIds != null && saleIds.size() > 0) {
				String sql = "update pharmacy.t_sale set status_id = 2 ,paid_amount = 0,outstanding_amount=paid_amount where id in (:saleIds) ";
				Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
				query.setParameter("saleIds", saleIds);
				query.executeUpdate();

				String sql2 = "update pharmacy.t_pharmacy_bill_payee_details set due_amount = bill_amount "
						+ "	where t_sale_id in (:saleIds) ";
				Query query2 = sessionFactory.getCurrentSession().createSQLQuery(sql2);
				query.setParameter("saleIds", saleIds);
				return (Integer) query2.executeUpdate();
			}

		}
		return 0;
	}

	@Override
	public Integer updateConsolidatedBillStatusId(Integer billId, Integer statusId) {
		String sql = "update pharmacy.t_sale set status_id  = :statusId ,paid_amount = outstanding_amount where bill_id = :billId ";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("billId", billId);
		query.setParameter("statusId", statusId);
		return (Integer) query.executeUpdate();
	}

	@Override
	public Integer sameQtySaleReturnSaleStatusUpdate(List<Integer> saleIds, Integer statusId) {
		String sql = "update pharmacy.t_sale set status_id  = :statusId  where id in ( :saleId )";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameterList("saleId", saleIds);
		query.setParameter("statusId", statusId);
		return (Integer) query.executeUpdate();
	}

	@Override
	public Integer updateOrderMasterAndSaleDetailsMaster(List<Integer> orderDetailIds, Set<Integer> saleDetailIds)
			throws Exception {

		Session session = sessionFactory.openSession();
		Transaction tr = session.beginTransaction();
		if (orderDetailIds != null && orderDetailIds.size() > 0) 
		{
			String sqlContract = "delete FROM  public.t_order_details_payee_mapper  where order_details_id in (:orderDetailsId)";
			Query queryContract = session.createSQLQuery(sqlContract);
			queryContract.setParameterList("orderDetailsId", orderDetailIds);
			queryContract.executeUpdate();

			String sqlPackage = "delete FROM public.t_order_details_package_mapper where order_details_id in (:orderDetailsId)";
			Query queryPackage = session.createSQLQuery(sqlPackage);
			queryPackage.setParameterList("orderDetailsId", orderDetailIds);
			queryPackage.executeUpdate();

		}
		if (saleDetailIds != null && saleDetailIds.size() > 0) {
			String sqlContract = "delete FROM  pharmacy.t_sale_detail_payee_mapper where t_sale_details_id  in (:saleDetailsIds) ";
			Query queryContract = session.createSQLQuery(sqlContract);
			queryContract.setParameterList("saleDetailsIds", saleDetailIds);
			queryContract.executeUpdate();

			String sqlPackage = "delete FROM  public.t_sale_detail_package_mapper  where t_sale_details_id  in (:saleDetailsIds) ";
			Query queryPackage = session.createSQLQuery(sqlPackage);
			queryPackage.setParameterList("saleDetailsIds", saleDetailIds);
			queryPackage.executeUpdate();
		}
		tr.commit();
		session.close();
		return 0;
	}

	@Override
	public Sale getSaleBySaleId(Long saleId) {

		String hql = "SELECT sale FROM Sale AS sale "
				+ "	WHERE sale.id = :saleId AND sale.isDeleted = false AND sale.isActive = true ";

		Query query = getSession().createQuery(hql);
		query.setParameter("saleId", saleId);

		return (Sale) query.uniqueResult();

	}

	@Override
	public List<AllDataResponse> allDataList(Integer itemid, Integer storeId, Integer orgId, Integer unitId) {
		// Batch b =new Batch();
		int org_id = orgId != null ? orgId : sessionService.getOrganizationId();
		int unit_id = unitId != null ? unitId : sessionService.getUnitId();

		/*
		 * String sql =
		 * " select sto.storeName   from Store sto join StoreItemMapper stomap on (sto.store_id = stomap.store_id    and sto.is_active=true and sto.is_deleted=false and sto.organization_id=1 and sto.unit_id=1   and stomap.is_active=true and stomap.is_deleted=false and stomap.organization_id=1 and stomap.unit_id=1)"
		 * +
		 * "  join Item itm on (itm.id = stomap.item_id and itm.is_active=true and itm.is_deleted=false and itm.organization_id=1 and itm.unit_id=1) "
		 * +
		 * "join Batch bth on (itm.id = bth.item_id  and bth.is_active=true and bth.is_deleted=false and bth.organization_id=1 and bth.unit_id=1) "
		 * ;
		 */
		/*
		 * String sql
		 * ="  select sto.store_name as storeName , bth.batch_no as batchNo , bth.expiry_date as expDate , bth.current_quantity as currentStock, bth.selling_price as mrp , r.name as Location , s.name as shelf from public.m_store sto join public.m_store_item_mapper stomap on (sto.store_id = stomap.store_id  and sto.is_active=true and sto.is_deleted=false and sto.organization_id="
		 * +org_id+" and sto.unit_id="+unit_id + " "+
		 * "  and stomap.is_active=true and stomap.is_deleted=false and stomap.organization_id="
		 * +org_id+"  and stomap.unit_id="+unit_id +
		 * ")  join public.m_item itm on (itm.id = stomap.item_id and stomap.item_id ="
		 * +itemid+" and itm.is_active=true and itm.is_deleted=false and itm.organization_id="
		 * +org_id+"  and itm.unit_id="+unit_id +
		 * ")  join inventory.t_batch bth on (itm.id = bth.item_id  and bth.is_active=true and bth.is_deleted=false and bth.organization_id="
		 * +org_id+"  and bth.unit_id="+unit_id +
		 * ") left join procurement.t_shelf_item  loc on (bth.id = loc.batch_id)  left join public.m_rack  r on (loc.rack_id = r.id) left join  public.m_shelf s on (loc.shelf_id = s.id) "
		 * ;
		 */

		/*
		 * String sql
		 * ="  select distinct sto.store_name as storeName , bth.batch_no as batchNo , bth.expiry_date as expDate , bth.current_quantity as currentStock, bth.selling_price as mrp , r.name as Location , s.name as shelf , 	uomunit.unit as uom , 	uomtype.type as uomunit from public.m_store sto join public.m_store_item_mapper stomap on (sto.store_id = stomap.store_id  and sto.is_active=true and sto.is_deleted=false and sto.organization_id="
		 * +org_id+" and sto.unit_id="+unit_id + " "+
		 * "  and stomap.is_active=true and stomap.is_deleted=false and stomap.organization_id="
		 * +org_id+"  and stomap.unit_id="+unit_id +
		 * ")  join public.m_item itm on (itm.id = stomap.item_id and stomap.item_id ="
		 * +itemid+" and itm.is_active=true and itm.is_deleted=false and itm.organization_id="
		 * +org_id+"  and itm.unit_id="+unit_id +
		 * ")  join inventory.t_batch bth on (itm.id = bth.item_id  and bth.is_active=true and bth.is_deleted=false and bth.organization_id="
		 * +org_id+"  and bth.unit_id="+unit_id +
		 * ") inner join inventory.t_batch_uom_mapper uommap on ( uommap.batch_id = bth.id ) inner join public.m_uom_unit  uomunit on  ( uomunit.id = bth.uom_unit_id ) inner join public.m_uom_type uomtype on ( uomtype.id = uommap.uom_type_id and uomtype.id=3 ) left join procurement.t_shelf_item  loc on (bth.id = loc.batch_id)  left join public.m_rack  r on (loc.rack_id = r.id) left join  public.m_shelf s on (loc.shelf_id = s.id)  // bth.is_pharmacy=true"
		 * ;
		 */
		// String sql = " select distinct sto.store_name as storeName , bth.batch_no as
		// batchNo , bth.expiry_date as expDate , bth.current_quantity as currentStock,
		// bth.selling_price as mrp , r.name as Location , s.name as shelf ,
		// uomunit.unit as uom , uomtype.type as uomunit from public.m_store sto inner
		// join inventory.t_store_stock stomap on sto.store_id = stomap.store_id inner
		// join inventory.t_batch bth on stomap.batch_id = bth.id inner join
		// public.m_item itm on bth.item_id = itm.id inner join
		// inventory.t_batch_uom_mapper uommap on uommap.batch_id = bth.id inner join
		// public.m_uom_unit uomunit on uomunit.id = bth.uom_unit_id inner join
		// public.m_uom_type uomtype on uomtype.id = uommap.uom_type_id and uomtype.id=3
		// left join procurement.t_shelf_item loc on (bth.id = loc.batch_id) left join
		// public.m_rack r on loc.rack_id = r.id left join public.m_shelf s on
		// loc.shelf_id = s.id where itm.id="+itemid+" and bth.is_active=true and
		// bth.is_quarantine=false and bth.is_deleted=false and sto.is_active=true and
		// sto.is_deleted=false order by sto.store_name ,bth.batch_no desc";
		String sql = "  select distinct sto.store_name as storeName , bth.batch_no as batchNo , "
				+ "bth.expiry_date as expDate ,  stomap.available_qty as currentStock , bth.selling_price as mrp , "
				+ "r.name as Location , s.name as shelf , 	uomunit.unit as uom ,  bth.cop as cop ,	"
				+ "uomtype.type as uomunit from public.m_store sto   "
				+ "inner join inventory.t_store_stock stomap on sto.store_id = stomap.store_id and stomap.unit_id="
				+ unit_id + " inner join inventory.t_batch bth on stomap.batch_id = bth.id  "
				+ "inner join public.m_item itm on bth.item_id = itm.id   "
				+ "inner join inventory.t_batch_uom_mapper uommap on  uommap.batch_id = bth.id   "
				+ "inner join public.m_uom_unit  uomunit on   uomunit.id = bth.uom_unit_id   "
				+ "inner join public.m_uom_type uomtype on uomtype.id = uommap.uom_type_id and uomtype.id=3   "
				+ "left join procurement.t_shelf_item  loc on (bth.id = loc.batch_id)  "
				+ "left join public.m_rack  r on loc.rack_id = r.id "
				+ "left join  public.m_shelf s on loc.shelf_id = s.id  " + "where itm.id=" + itemid
				+ " and stomap.store_id !=" + storeId + " AND bth.unit_id= " + unit_id
				+ " and bth.is_active=true and bth.is_quarantine=false and bth.is_deleted=false  "
				+ "and sto.is_active=true and sto.is_deleted=false and stomap.available_qty is not null " + 
				"	and stomap.available_qty > 0 order by sto.store_name ,bth.batch_no  " + "desc";

		Query query = getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.aliasToBean(AllDataResponse.class));
		return query.list();
	}

	@Override
	public AvailableQty availableQty(Integer storeid, Integer itemid, Integer batchNo, Integer uontypeid,
			Integer unitId) {

		// Batch b =new Batch();
		// int org_id = sessionService.getOrganizationId();
		// int unit_id = sessionService.getUnitId();
		// String sql = "select bth.current_quantity as availableQty from public.m_store
		// sto inner join inventory.t_store_stock stomap on sto.store_id =
		// stomap.store_id inner join inventory.t_batch bth on stomap.batch_id = bth.id
		// inner join public.m_item itm on bth.item_id = itm.id inner join
		// inventory.t_batch_uom_mapper uommap on uommap.batch_id = bth.id inner join
		// public.m_uom_unit uomunit on uomunit.id = bth.uom_unit_id inner join
		// public.m_uom_type uomtype on uomtype.id = uommap.uom_type_id and
		// uomtype.id="+uontypeid+" where stomap.store_id ="+storeid+" and
		// itm.id="+itemid+" and bth.id =:batchNo and bth.is_active=true and
		// bth.is_deleted=false and sto.is_active=true and sto.is_deleted=false ";

		String sql = "select " + "	 cast(sum(coalesce( stomap.available_qty, 0 )) as integer) " + " as  availableQty "
				+ "from public.m_store sto  "
				+ "inner join inventory.t_store_stock stomap on sto.store_id = stomap.store_id  "
				+ "inner join inventory.t_batch bth on stomap.batch_id = bth.id  "
				+ "inner join public.m_item itm on bth.item_id = itm.id  "
				+ "inner join inventory.t_batch_uom_mapper uommap on  uommap.batch_id = bth.id  "
				+ "inner join public.m_uom_unit  uomunit on   uomunit.id = bth.uom_unit_id "
				+ "inner join public.m_uom_type uomtype on uomtype.id = uommap.uom_type_id " + "and uomtype.id="
				+ uontypeid + "  " + "where  stomap.store_id =" + storeid + "  " + " and  itm.id=" + itemid + "  "
				+ " and bth.is_active=true  " + "and bth.is_deleted=false  "
				+ " and sto.is_active=true and sto.is_deleted=false and stomap.available_qty is not null  and stomap.total_qty >0 and stomap.available_qty  >=0 "
				+ "  and stomap.total_qty is not null and stomap.unit_id=" + unitId + " and bth.unit_id=" + unitId;

		StringBuffer sb = new StringBuffer(sql);
		sb.append(batchNo != null && batchNo > 0 ? " and bth.id = " + batchNo : " ");
		Query query = getSession().createSQLQuery(sb.toString());
		query.setResultTransformer(Transformers.aliasToBean(AvailableQty.class));
		return (AvailableQty) query.uniqueResult();

	}

	@Override
	public Prescription savePrescription(Prescription prescription) {
		getSession().save(prescription);

		return prescription;
	}

	@Override
	public List<BatchAllocation> getBatchAllocation(Date date) {
		String hql = "SELECT batchAllocation FROM BatchAllocation AS batchAllocation \r\n "
				+ " INNER JOIN batchAllocation.batch AS batch WITH batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.isPharmacy = false "
				+ " WHERE batchAllocation.updatedDate <= :date AND batchAllocation.isDeleted = false AND batchAllocation.isActive = true AND (batchAllocation.isFromOt = false OR batchAllocation.isFromOt is null) ";

		Query query = getSession().createQuery(hql);
		query.setParameter("date", date);

		return query.list();
	}

	@Override
	public BatchAllocation saveBatchAllocation(BatchAllocation batchAllocation) {
		getSession().save(batchAllocation);
		return batchAllocation;
	}

	@Override
	public List<BatchAllocation> getBatchAllocationByTempBillId(Long tempBillId) {

		String hql = "SELECT batchAllocation FROM BatchAllocation AS batchAllocation "
				+ " WHERE batchAllocation.tempBillId = :tempBillId AND batchAllocation.isDeleted = false AND batchAllocation.isActive = true "
				+ " AND batchAllocation.quantity > 0  order by batchAllocation.batch.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("tempBillId", tempBillId);

		return query.list();
	}

	@Override
	public Integer inactiveOrderDetailsBySaleId(Integer saleId, Integer billId) throws Exception {
		String sql = "update public.t_order_master set order_status = 'I' where sale_id = :saleId ";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("saleId", saleId);
		query.executeUpdate();

		String sql1 = " update public.t_order_details  set status  = 'I' where order_id  in  (select order_id  from public.t_order_master  where sale_id = :saleId) ";
		Query query1 = sessionFactory.getCurrentSession().createSQLQuery(sql1);
		query1.setParameter("saleId", saleId);
		query1.executeUpdate();
		return 1;
	}

	@Override
	public Integer inactiveSaleBySaleId(Integer saleId) throws Exception {
		String sql = "update pharmacy.t_sale set status_id  = 9  where id = :saleId ";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("saleId", saleId);
		query.executeUpdate();

		String sql1 = " update pharmacy.t_sale_detail set status_id = 9 where sale_id = :saleId ";
		Query query1 = sessionFactory.getCurrentSession().createSQLQuery(sql1);
		query1.setParameter("saleId", saleId);
		query1.executeUpdate();

		
		String sql2 = " update public.t_order_details set status ='I' where sale_detail_id in (select coalesce(dtls.id,0) from pharmacy.t_sale_detail dtls where dtls.sale_id=:saleId) ";
		Query query2 = sessionFactory.getCurrentSession().createSQLQuery(sql2);
		query2.setParameter("saleId", saleId);
		query2.executeUpdate();

		String sql3 = " update pharmacy.t_billable_consumption set status_id = 9 where sale_id = :saleId ";
		Query query3 = sessionFactory.getCurrentSession().createSQLQuery(sql3);
		query3.setParameter("saleId", saleId);
		query3.executeUpdate();
		
		/*
		 * String sql3 =
		 * " update pharmacy.t_prescription set status_id  = 9  where id in (select coalesce(prescription_id,0) from pharmacy.t_sale where id = :saleId) "
		 * ; Query query3 = sessionFactory.getCurrentSession().createSQLQuery(sql3);
		 * query3.setParameter("saleId", saleId); query3.executeUpdate();
		 * 
		 * String sql4 =
		 * " update pharmacy.t_prescription_detail set status_id  = 9 where prescription_id in (select coalesce(prescription_id,0) from pharmacy.t_sale where id = :saleId) "
		 * ; Query query4 = sessionFactory.getCurrentSession().createSQLQuery(sql4);
		 * query4.setParameter("saleId", saleId); query4.executeUpdate();
		 */

		return 1;
	}

	@Override
	public Response getAdmissionsById(int admissionId) throws BillingException {
		try {
			AdmissionListIpdChargesResDto dto = (AdmissionListIpdChargesResDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMISSION_LIST_FOR_IPD_CHARGES_BY_PATIENT")
					.setParameter("admissionId", admissionId)
					.setResultTransformer(Transformers.aliasToBean(AdmissionListIpdChargesResDto.class)).uniqueResult();
			
			if(dto == null ||  dto.getAdmissionId() == null ){
				AdmissionListIpdChargesResDto dto1 = (AdmissionListIpdChargesResDto) sessionFactory.getCurrentSession()
						.getNamedQuery("GET_ADMISSION_LIST_FOR_IPD_CHARGES_BY_ADMISSION_ID")
						.setParameter("admissionId", admissionId)
						.setResultTransformer(Transformers.aliasToBean(AdmissionListIpdChargesResDto.class)).uniqueResult();
				return new Response<>(null, null, null, null, dto1);
			}else{
				return new Response<>(null, null, null, null, dto);
			}
			
			
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public List<AllDataResponse> allExpiredDataList(Integer itemid, Integer storeId, Integer unitId) {

		// Batch b =new Batch();
		// int org_id = sessionService.getOrganizationId();
		// int unit_id = sessionService.getUnitId();
		String sql = "  select distinct sto.store_name as storeName , bth.batch_no as batchNo , "
				+ "bth.expiry_date as expDate ,  case   when bth.current_quantity < 0 then 0   "
				+ "else bth.current_quantity     end  as currentStock, bth.selling_price as mrp , "
				+ "r.name as Location , s.name as shelf , 	uomunit.unit as uom , 	"
				+ "uomtype.type as uomunit from public.m_store sto   "
				+ "inner join inventory.t_store_stock stomap on sto.store_id = stomap.store_id "
				+ "inner join inventory.t_batch bth on stomap.batch_id = bth.id  "
				+ "inner join public.m_item itm on bth.item_id = itm.id   "
				+ "inner join inventory.t_batch_uom_mapper uommap on  uommap.batch_id = bth.id   "
				+ "inner join public.m_uom_unit  uomunit on   uomunit.id = bth.uom_unit_id   "
				+ "inner join public.m_uom_type uomtype on uomtype.id = uommap.uom_type_id and uomtype.id=3   "
				+ "left join procurement.t_shelf_item  loc on (bth.id = loc.batch_id)  "
				+ "left join public.m_rack  r on loc.rack_id = r.id "
				+ "left join  public.m_shelf s on loc.shelf_id = s.id  "
				+ "where  bth.expiry_date < current_date and  itm.id=" + itemid + " and stomap.store_id !=" + storeId
				+ " AND bth.unit_id= " + (unitId != null ? unitId : sessionService.getUnitId())
				+ " and bth.is_active=true and bth.is_quarantine=false and bth.is_deleted=false  "
				+ "and sto.is_active=true and sto.is_deleted=false order by sto.store_name ,bth.batch_no  " + "desc";

		Query query = getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.aliasToBean(AllDataResponse.class));
		return query.list();

	}

	@Override
	public BatchAllocation getBatchAllocationsNew(Long tempBillId) {

		String hql = "SELECT batchAllocation FROM BatchAllocation AS batchAllocation \r\n "
				+ " INNER JOIN batchAllocation.batch AS batch WITH batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false   "
				+ " WHERE batchAllocation.id = " + tempBillId
				+ " AND batchAllocation.isDeleted = false AND batchAllocation.isActive = true \r\n";

		Query query = getSession().createQuery(hql);
		// Integer id = tempBillId.intValue();
		// query.setParameter("allocationId", id);

		return (BatchAllocation) query.uniqueResult();

	}

	@Override
	public Response updateIssueDetails(Long saleDetailId) {
		
		sessionFactory.getCurrentSession().createSQLQuery(" update pharmacy.t_issue_detail set is_active = false , is_deleted = true, status_id=9  where sale_detail_id = :saleDetailId ")
		.setParameter("saleDetailId", saleDetailId).executeUpdate();
		
		return new Response<>("success", "200", null, null, null);
	}

	@Override
	public Response updateIssue(Long saleId) {
		//HIS-15311
		String sql = " update pharmacy.t_issue  set is_active = false , is_deleted = true, status_id=9  where sale_id = :saleId ";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("saleId", saleId);
		query.executeUpdate();

		return new Response<>("success", "200", null, null, null);
	}

	@Override
	public List<BatchAllocation> getBatchAllocation(Integer orgId, Integer unitId) {
		String hql = "SELECT batchAllocation FROM BatchAllocation AS batchAllocation \r\n "
				+ " WHERE batchAllocation.quantity >0 AND  batchAllocation.unit.unitId =" + unitId;

		Query query = getSession().createQuery(hql);
		// Integer id = tempBillId.intValue();
		// query.setParameter("allocationId", id);
		return query.setMaxResults(1000).list();
	}

	@Override
	public BigInteger issueCountByPrescritpion(Integer prescriptionId) {

		BigInteger count = BigInteger.ZERO;
		String sql = " select count(ti.id) from pharmacy.t_issue ti where ti.prescription_id  = :prescriptionId and ti.is_active  = true and ti.status_id not in (9) ";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("prescriptionId", prescriptionId);
		count = (BigInteger) query.uniqueResult();
		return count == null ? BigInteger.ZERO : count;
	}

	@Override
	public CommonResponse updateBill(WalkInSalesRequest request) {
		String sql1="select tod.order_details_id,ts.bill_id from public.t_order_details tod "
				+ " left join pharmacy.t_sale_detail tsd on tsd.id=tod.sale_detail_id "
				+ " left join pharmacy.t_sale ts on ts.id =tsd.sale_id "
				+ " where ts.id =:saleId ";
		Query query1=sessionFactory.getCurrentSession().createSQLQuery(sql1);
		String sql = " update pharmacy.t_sale   set status_id = 18 where id = :saleId ";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("saleId", request.getSaleTypeId());
		query1.setParameter("saleId", request.getSaleTypeId());
		List<Integer> Id = query1.setMaxResults(1000).list();
		query.executeUpdate();

		return new CommonResponse<>("success", "200", null, null, Id);
	}

	@Override
	public CommonResponse getStatus(Integer prescriptionId) {
		try
		{
			Integer idStatus = (Integer) sessionFactory.getCurrentSession()
					.createSQLQuery("	select status_id from  pharmacy.t_prescription tp  where  is_active = true and is_deleted = false and id ="+prescriptionId.longValue()).uniqueResult();
			CommonResponse dto = new CommonResponse();
			dto.setId(idStatus.longValue());
			return dto;
		}catch (Exception e) {
		logger.error("getStatus failed", e);
		}
		return null;
	}

	@Override
	public Integer getIssueDetialsStatus(Integer prescriptionId) {
		List<CommonResponse> idStatus = sessionFactory.getCurrentSession()
				.createSQLQuery("SELECT tpd.id  as \"bigValue\" FROM pharmacy.t_prescription tp  " + 
						"	inner join pharmacy.t_prescription_detail tpd on tp.id = tpd.prescription_id  " + 
						"	where tp.id  ="+prescriptionId)
				.setResultTransformer(Transformers.aliasToBean(CommonResponse.class)).list();
		Boolean check = true;
		Integer com = 4;Integer perCom = 18;
		if(idStatus != null) {
			 
			for (CommonResponse commonResponse : idStatus) {
				
				CommonDto idStatus1 = (CommonDto) sessionFactory.getCurrentSession()
						.createSQLQuery("select tid.organization_id as \"id\" from pharmacy.t_issue_detail tid " + 
								"	where tid.is_active = true and tid.is_deleted = false  and tid.status_id  in(4) and tid.prescription_detail_id =:prescriptionDetailId  "
										+ " order by tid.id desc limit 1 ")
						.setParameter("prescriptionDetailId",commonResponse.getBigValue())
						.setResultTransformer(Transformers.aliasToBean(CommonDto.class)).uniqueResult();
				
				if(idStatus1 == null) {
					check =  false;
				}
			}
			 
		}else {
			return com;
		}
		
		if(check == true) {
			return com;
		}else {
			return perCom;
		}
		
	}

	@Override
	public void updateIssueDetailsStatus(Long saleDetailId, Integer sts) {
        Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update pharmacy.t_issue_detail set status_id ="+sts+" where sale_detail_id ="+saleDetailId)
                .executeUpdate();
	}
	@Override
	public List<SaleDetail> getSaleDetailsBySaleId(Long ids) throws BillingException {
		String hql = "SELECT saleDetail FROM SaleDetail AS saleDetail WHERE saleDetail.sale.id = :id AND saleDetail.isDeleted = false AND saleDetail.isActive = true ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", ids);
		return query.list();
	}

	@Override
	public void updateTempDoctorId(Long id,Integer tempDoctorId) {
		Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update pharmacy.t_sale set temp_doctor_id =:tempDoctorId  where id =:id ")
				.setParameter("id",id)
				.setParameter("tempDoctorId",tempDoctorId)
                .executeUpdate();
		
	}

	@Override
	public List<SaleDetailPackageMapperDto> getPharmacyDepositConsumsedAmount(Long saleId) {
		String sql =
		 		"select "  + 
		 		"tpdc.consumed_amount as \"amount\", "  + 
		 		"tpdc.dep_consumption_id as \"paymentRecieptId\" "  + 
		 		"from billing.t_payment_deposite_consumption tpdc  "  + 
		 		"inner join pharmacy.t_sale ts  on ts.id = tpdc.sale_id  " + 
		 		"where ts.id =:saleId  and tpdc.status='A'";
	List<SaleDetailPackageMapperDto> list = sessionFactory.getCurrentSession().createSQLQuery(sql)
			.setParameter("saleId",saleId)
			.setResultTransformer(Transformers.aliasToBean(SaleDetailPackageMapperDto.class)).list();

	List<SaleDetailPackageMapperDto> packageList = new ArrayList<SaleDetailPackageMapperDto>();
	for (SaleDetailPackageMapperDto pkgMpper : list) 
	{
			packageList.add(pkgMpper);
	}
	return packageList;
	}

	@Override
	public Integer updateDepositDetails(List<SaleDetailPackageMapperDto> getDepositDetails) {
		for (SaleDetailPackageMapperDto getDepositDetails1 : getDepositDetails) 
		{
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	        Date currentDate = new Date();
	        String dateString = formatter.format(currentDate);
	        
			if(getDepositDetails1.getAmount()!=null && getDepositDetails1.getAmount()>0)
			{
				sessionFactory.getCurrentSession().createSQLQuery(" update billing.t_payment_deposite_consumption  set status = 'I'  where dep_consumption_id  = "+getDepositDetails1.getPaymentRecieptId())
				.executeUpdate();
				

				String sqlQueryss = "update billing.t_payment_deposite_consumption set  updated_date ='"+dateString+"' ,cancel_by ="+getDepositDetails1.getCreatedBy()+" where id = "+ getDepositDetails1.getPaymentRecieptId();
				sessionFactory.getCurrentSession().createSQLQuery(sqlQueryss.toString()).executeUpdate();
				
				String sql =
				 		" select d.deposit_id as \"id\" , d.consumed_amount \"amount\" from billing.t_payment_deposite_consumption_details d where d.dep_consumption_id = "+getDepositDetails1.getPaymentRecieptId();
				
				List<SaleDetailPackageMapperDto> list = sessionFactory.getCurrentSession().createSQLQuery(sql)
					.setResultTransformer(Transformers.aliasToBean(SaleDetailPackageMapperDto.class)).list();
				
				
				if(list != null && list.size()>0){
					
					for (SaleDetailPackageMapperDto dto : list) {
						
						if(dto.getAmount() != null && dto.getId() != null ){
							String sqla = "update billing.m_deposit_master set available_deposit = available_deposit + :amount, consumed_deposit = consumed_deposit - :amount where deposit_master_id = :id";
							Query query = sessionFactory.getCurrentSession().createSQLQuery(sqla);
							query.setParameter("amount", dto.getAmount());
							query.setParameter("id", dto.getId());
							query.executeUpdate();
							
						}
						
					}
					
				}
				
			}
		}
		
		return 0;
	}

	@Override
	public void updateDoctorId(Long id,Integer tempDoctorId) {

		String sql = "update pharmacy.t_sale set doctor_id = :doctorId where id = :id";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("doctorId", tempDoctorId);
		query.setParameter("id", id);
		Integer count = query.executeUpdate();
		
	}

	@Override
	public CommonResponse getStatus(Long id) {
			CommonDto idStatus = (CommonDto) sessionFactory.getCurrentSession()
					.createSQLQuery("	select status_id as \"id\" from  pharmacy.t_sale tp  where  is_active = true and is_deleted = false and id =:id ")
					.setParameter("id",id)
					.setResultTransformer(Transformers.aliasToBean(CommonDto.class)).uniqueResult();
			CommonResponse dto = new CommonResponse();
			dto.setId(idStatus.getId().longValue());
			return dto;
		}

	@Override
	public void updateSalePaymentModeIdInDepositMaster(Integer depositMasterIdToUpdateUsingSalePaymentMode, Long id) {
		String sql = "update billing.m_deposit_master set pharmacy_sale_mode_id = :pharmacySaleModeId where deposit_master_id = :depositMasterId";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("pharmacySaleModeId", id);
		query.setParameter("depositMasterId", depositMasterIdToUpdateUsingSalePaymentMode);
		Integer count = query.executeUpdate();
		
	}

	@Override
	public Response updatediscountCategory(OrderDetailsMasterDto detailsMasterDto) {
		String sql = "update t_order_details set discount_category_id = :discountCategoryId where order_details_id = :orderDetailsId";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		query.setParameter("discountCategoryId", detailsMasterDto.getDiscountCategoryId());
		query.setParameter("orderDetailsId", detailsMasterDto.getOrderDetailsId());
		Integer count = query.executeUpdate();
		return null;
	}

	@Override
	public List<PaymentDepositDetailsDto> getSalePaymentModeDetailsConsumsedAmount(Long saleId,Integer userId) {
		
		
		String sql =
		 		"select "  + 
		 		"tpdc.cancel_by as \"createdBy\", "  + 
		 		"tpdc.id as \"paymentRecieptId\" "  + 
		 		"from pharmacy.t_sale_payment_mode tpdc  "  + 
		 		"inner join pharmacy.t_sale ts  on ts.id = tpdc.sale_id  " + 
		 		"where ts.id =:saleId  and tpdc.is_active=true ";
	List<PaymentDepositDetailsDto> list = sessionFactory.getCurrentSession().createSQLQuery(sql)
			.setParameter("saleId",saleId)
			.setResultTransformer(Transformers.aliasToBean(PaymentDepositDetailsDto.class)).list();

	List<PaymentDepositDetailsDto> packageList = new ArrayList<PaymentDepositDetailsDto>();
	for (PaymentDepositDetailsDto pkgMpper : list) 
	{
			packageList.add(pkgMpper);
	}
	
	for (PaymentDepositDetailsDto getDepositDetails1 : packageList) 
	{
		
		/*SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date currentDate = new Date();
        String dateString = formatter.format(currentDate);*/

		Date currentDate = new Date();
		LocalDateTime localDateTime = new Timestamp(currentDate.getTime()).toLocalDateTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDate = localDateTime.format(formatter);
		Timestamp dateString = Timestamp.valueOf(localDateTime);

		String sqlQuery = "update pharmacy.t_sale_payment_mode set updated_date = :updatedDate, cancel_by = :cancelBy where id = :paymentRecieptId";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery);
		query.setParameter("updatedDate", dateString);
		query.setParameter("cancelBy", userId);
		query.setParameter("paymentRecieptId", getDepositDetails1.getPaymentRecieptId());
		query.executeUpdate();


		String sqlQuery2 = "update pharmacy.t_sale_payment_mode set updated_date = :updatedDate, updated_by = :cancelBy ,cancel_by = :cancelBy where sale_id = :saleId";
		Query query2 = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery2);
		query2.setParameter("updatedDate", dateString);
		query2.setParameter("cancelBy", userId);
		query2.setParameter("saleId", saleId);
		query2.executeUpdate();
	}
			
	return packageList;
	}
	
	
	
}

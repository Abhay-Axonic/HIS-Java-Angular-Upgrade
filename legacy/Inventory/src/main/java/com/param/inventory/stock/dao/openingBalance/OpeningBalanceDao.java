package com.param.inventory.stock.dao.openingBalance;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.transform.Transformers;
import org.hibernate.type.TrueFalseType;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.OpbUomMapper;
import com.param.entity.model.inventory.OpeningBalShelfItem;
import com.param.entity.model.inventory.OpeningBalance;
import com.param.entity.model.inventory.OpeningBalanceDetail;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.procurement.ShelfItem;
import com.param.global.dto.items.UomResponse;
import com.param.inventory.stock.dto.openingBalance.ItemResponse;
import com.param.inventory.stock.dto.openingBalance.OpeningBalanceDetailsResponse;
import com.param.inventory.stock.dto.openingBalance.OpeningBalanceRequest;
import com.param.inventory.stock.dto.openingBalance.OpeningBalanceResponse;
import com.param.inventory.stock.dto.openingBalance.StorageDetails;
import com.param.inventory.stock.dto.openingBalance.StorageLocationDetails;

@Repository
public class OpeningBalanceDao extends AbstractDao<Integer, OpeningBalance> implements IOpeningBalanceDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<OpeningBalance> searchOpeningBalance(OpeningBalanceRequest request) {

		String hql = "SELECT openingBalance FROM OpeningBalance AS openingBalance "
				+ "		LEFT JOIN openingBalance.store as store "
				+ "WHERE openingBalance.isActive = true AND openingBalance.isDeleted = false AND openingBalance.unit.unitId = :unitId "
				+ "		AND openingBalance.addedDate BETWEEN :fromDate AND :toDate "
				+ " 	AND (:storeId = 0 OR store.storeId = :storeId) "
				+ "		AND store.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "								WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "										AND userStore.unit.unitId = :unitId AND userStore.store.isDeleted = false AND userStore.store.isActive = true) "
				+ "ORDER BY openingBalance.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", request.getFromDate());
		query.setParameter("toDate", request.getToDate());
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setParameter("userId", sessionService.getUserId());

		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<OpeningBalanceResponse> searchOpeningBalanceBetterPerformance(OpeningBalanceRequest request) {

		String hql = "SELECT openingBalance.id as id, " + "openingBalance.addedDate as opbDate,"
				+ "openingBalance.opbNo as opbNo," + "store.storeName as storeName,"
				+ "addedBy.userName as requestedBy," + "status.id as statusId," + "status.status as status"
				+ " FROM OpeningBalance AS openingBalance " + "		LEFT JOIN openingBalance.store as store "
				+ "LEFT JOIN openingBalance.addedBy as addedBy " + "LEFT JOIN openingBalance.status as status "
				+ "WHERE openingBalance.isActive = true AND openingBalance.isDeleted = false "
				+ "     AND openingBalance.unit.unitId = :unitId  AND openingBalance.obType is null  "
				+ "		AND openingBalance.addedDate BETWEEN :fromDate AND :toDate "
				+ " 	AND (:storeId = 0 OR store.storeId = :storeId) "
				+ "		AND store.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
				+ "								WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
				+ "										AND userStore.unit.unitId = :unitId AND userStore.store.isDeleted = false AND userStore.store.isActive = true) "
				+ "ORDER BY openingBalance.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", request.getFromDate());
		query.setParameter("toDate", request.getToDate());
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("unitId", request.getUnitId()!=null?request.getUnitId():sessionService.getUnitId());
		query.setParameter("userId", request.getUserId()!=null?request.getUserId():sessionService.getUserId());
		
		
		return query.setResultTransformer(Transformers.aliasToBean(OpeningBalanceResponse.class)).list();
	}

	@Override
	public OpeningBalance saveOpeningBalance(OpeningBalance openingBalance) {
		getSession().save(openingBalance);

		return openingBalance;
	}

	@Override
	public OpeningBalance getOpeningBalace(Integer id) {
		String hql = "SELECT openingBalance FROM OpeningBalance AS openingBalance "
				+ " WHERE openingBalance.id = :id AND openingBalance.isActive = true "
				+ " AND openingBalance.isDeleted = false";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return (OpeningBalance) query.uniqueResult();
	}

	@Override
	public List<OpeningBalanceDetail> getOpeningbalaceDetail(Integer openingBalanceId) {
		String hql = "SELECT openingBalanceDetail FROM OpeningBalanceDetail AS openingBalanceDetail \r\n"
				+ " INNER JOIN openingBalanceDetail.openingBalance AS openingBalance WITH openingBalance.isActive = true AND openingBalance.isDeleted = false \r\n"
				+ " WHERE openingBalance.id = :openingBalanceId AND openingBalanceDetail.isActive = true AND openingBalanceDetail.isDeleted = false \r\n"
				+ " ORDER BY openingBalanceDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("openingBalanceId", openingBalanceId);

		return query.list();
	}

	@Override
	public List<OpbUomMapper> getOpbUomMapperList(Integer id) {
		String hql = "SELECT opbUomMapper FROM OpbUomMapper AS opbUomMapper "
				+ " INNER JOIN opbUomMapper.openingBalanceDetail AS openingBalanceDetail WITH openingBalanceDetail.id = :id AND openingBalanceDetail.isDeleted = false AND openingBalanceDetail.isActive = true "
				+ "WHERE opbUomMapper.isDeleted = false AND opbUomMapper.isActive = true ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public OpeningBalance updateOpeningBalance(OpeningBalance openingBalance) {
		getSession().update(openingBalance);

		return openingBalance;
	}

	@Override
	public Batch saveBatch(Batch batch) {
		getSession().save(batch);
		return batch;
	}

	@Override
	public StoreStock saveStoreStock(StoreStock storeStock) {
		getSession().save(storeStock);
		return storeStock;
	}

	@Override
	public StockTransaction saveStockTransaction(StockTransaction stockTransaction) {
		getSession().save(stockTransaction);
		return stockTransaction;
	}

	@Override
	public ShelfItem saveShelfItem(ShelfItem shelfItem) {
		getSession().save(shelfItem);
		return shelfItem;
	}

	@Override
	public OpeningBalShelfItem saveOpeningBalShelfItem(OpeningBalShelfItem shelfItem) {
		getSession().save(shelfItem);
		return shelfItem;
	}

	@Override
	public List<StorageLocationDetails> getStogareLocation(Integer id) {

		String sql = "   select s.id as shelfitemid , s.opening_balance_details_id as openingbaldetid , shelf.id as shelfid  , shelf.name as shelfname, rack.id as rackid, rack.name as rackname , s.remark as remark from inventory.t_opening_bal_shelf_item  s join inventory.t_opening_balance_detail ob on s.opening_balance_details_id=ob.id join public.m_shelf shelf on s.shelf_id=shelf.id join public.m_rack rack on s.rack_id=rack.id  where  s.is_deleted=false and s.opening_balance_details_id=:id";

		Query query = getSession().createSQLQuery(sql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(StorageLocationDetails.class));

		return query.list();

	}

	@Override
	public List<OpeningBalShelfItem> getStorageLocationById(Integer id) {
		String hql = "SELECT openingBalShelfItem FROM OpeningBalShelfItem AS openingBalShelfItem "
				+ "		 JOIN openingBalShelfItem.openingBalanceDetail as openingBalanceDetail  "
				+ "WHERE openingBalanceDetail.isActive = true AND openingBalanceDetail.isDeleted = false AND openingBalanceDetail.id = :id ";
			
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
			return query.list();
	}

	@Override
	public OpeningBalanceDetailsResponse getOpeningBalacePT(Integer id) {
		String hql = "SELECT openingBalance.id as id, openingBalance.addedDate as opbDate, "
				+ " openingBalance.opbNo as opbNo, store.storeId as storeId, store.storeName as store, "
				+ " addedBy.userName as requestedBy, status.status as status, status.id as statusId "
				+ " FROM OpeningBalance AS openingBalance "
				+ " WHERE openingBalance.id = :id AND openingBalance.isActive = true "
				+ " AND openingBalance.isDeleted = false";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(OpeningBalanceDetailsResponse.class));
		return (OpeningBalanceDetailsResponse) query.uniqueResult();
	}

	@Override
	public List<ItemResponse> getOpeningbalaceDetailPT(Integer id) {
		String hql = " SELECT openingBalanceDetail.id as id, item.id as itemId, item.code as itemCode, "
				+ " item.name as itemName, uomType.id as uomTypeId, uomType.type as uomType, "  
				+ " uomUnit.id as uomUnitId, uomUnit.units as uomUnit, "
				+ " mrpUomType.id as mrpUomTypeId, openingBalanceDetail.expiryDate as expiryDate, "
				+ " openingBalanceDetail.quantity as quantity, openingBalanceDetail.cop as cop, "
				+ " openingBalanceDetail.mrp as mrp, openingBalanceDetail.amountCop as copAmount, "
				+ " openingBalanceDetail.markup as markup, tax.id as taxId, "
				+ " tax.name as taxName, openingBalanceDetail.taxPercentage as taxPercent, "
				+ " openingBalanceDetail.taxAmount as taxAmount, openingBalanceDetail.remark as remark, "
				+ " status.id as statusId, openingBalanceDetail.lcop as lcop, "
				+ " item.isBatchRequired as isBatchReq, "
				+ " openingBalanceDetail.lotNumber as lotNo, openingBalanceDetail.batchNo as batchNo, item.isThreeUOMApplicable as isThreeUOMApplicable "
				+ " FROM OpeningBalanceDetail AS openingBalanceDetail "
				+ " INNER JOIN openingBalanceDetail.openingBalance AS openingBalance WITH openingBalance.isActive = true AND openingBalance.isDeleted = false \r\n"
				+ " INNER JOIN openingBalanceDetail.item as item "
				+ " LEFT JOIN openingBalanceDetail.uomType as uomType "
				+ " LEFT JOIN openingBalanceDetail.uomUnit as uomUnit "
				+ " LEFT JOIN openingBalanceDetail.mrpUomType as mrpUomType "
				+ " LEFT JOIN openingBalanceDetail.tax as tax "
				+ " INNER JOIN openingBalanceDetail.status as status "
				+ " WHERE openingBalance.id = :openingBalanceId AND openingBalanceDetail.isActive = true AND openingBalanceDetail.isDeleted = false \r\n"
				+ " ORDER BY openingBalanceDetail.id ";
			
		Query query = getSession().createQuery(hql);
		query.setResultTransformer(Transformers.aliasToBean(ItemResponse.class));
		query.setParameter("openingBalanceId", id);
		return query.list();
	}

	@Override
	public List<StorageDetails> getStogareLocationPT(List<Integer> openingBalanceDetailIds) {

		String sql = " select s.id as id   ,  s.id as shelfItemId , s.opening_balance_details_id as "
				+ " openingBalDetId , shelf.id as shelfId  , shelf.name as shelfName, "
				+ " rack.id as rackId, rack.name as rackName , s.remark as remark "
				+ " from inventory.t_opening_bal_shelf_item s "
				+ " join inventory.t_opening_balance_detail ob on s.opening_balance_details_id=ob.id "
				+ " join public.m_shelf shelf on s.shelf_id=shelf.id "
				+ " join public.m_rack rack on s.rack_id=rack.id "
				+ " where s.is_deleted=false and s.opening_balance_details_id in (:openingBalanceDetailIds) ";

		Query query = getSession().createSQLQuery(sql).addScalar("shelfItemId")
				.addScalar("openingBalDetId").addScalar("shelfId")
				.addScalar("shelfName").addScalar("rackId").addScalar("rackName")
				.addScalar("remark");
		query.setParameterList("openingBalanceDetailIds", openingBalanceDetailIds);
		query.setResultTransformer(Transformers.aliasToBean(StorageDetails.class));

		return query.list();
	}

	@Override
	public List<UomResponse> getOpbUomMapperListPT(List<Integer> opbDtlIds) {
		String hql = " SELECT opbUomMapper.id as id, uomType.type as uomType, uomType.id as uomTypeId, "
				+ " uomUnit.id as uomUnitId, uomUnit.units as uomUnit, opbUomMapper.conversions as conversion, concat (uomType.code ,'-',uomUnit.units) as uomDetails,  "
				+ " opbUomMapper.ipUom as ipUom, opbUomMapper.opUom as opUom, opbUomMapper.storeUom as storeUom, "
				+ " openingBalanceDetail.id as opbDtlId "
				+ " FROM OpbUomMapper AS opbUomMapper "
				+ " INNER JOIN opbUomMapper.openingBalanceDetail AS openingBalanceDetail WITH openingBalanceDetail.id in (:opbDtlIds) AND openingBalanceDetail.isDeleted = false AND openingBalanceDetail.isActive = true "
				+ " INNER JOIN opbUomMapper.uomUnit AS uomUnit WITH uomUnit.isDeleted = false AND uomUnit.isActive = true "
				+ " INNER JOIN opbUomMapper.uomType AS uomType WITH uomType.isDeleted = false AND uomType.isActive = true "
				+ " WHERE opbUomMapper.isDeleted = false AND opbUomMapper.isActive = true ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("opbDtlIds", opbDtlIds);
		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));
		return query.list();
	}

	@Override
	public Integer getBatchId(String batchNo, Integer unitId) {
		String sql = " select CAST(tb.id AS int) from inventory.t_batch tb where tb.batch_no=:batchNo"
				+ " and tb.unit_id=:unitId limit 1";

		Query query = getSession().createSQLQuery(sql);
		query.setParameter("batchNo", batchNo);
		query.setParameter("unitId", unitId);
//		query.setResultTransformer(Transformers.aliasToBean(UomResponse.class));
		return (Integer) query.uniqueResult();
	}
}

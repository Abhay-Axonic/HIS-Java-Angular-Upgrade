package com.param.inventory.store.dao.storeStock;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.constants.Constants;
import com.param.common.dao.AbstractDao;
import com.param.global.common.ServiceException;
import com.param.global.dto.items.BatchResponse1;
import com.param.global.dto.items.ItemWiseBatchResponse;
import com.param.inventory.store.dto.storeStock.ExpiredStoreStockRequest;
import com.param.inventory.store.dto.storeStock.ExpiredStoreStockResponse;
import com.param.inventory.store.dto.storeStock.StockInTransitResponse;
import com.param.inventory.store.dto.storeStock.StoreStockRequest;
import com.param.inventory.store.dto.storeStock.StoreStockRequestDate;
import com.param.inventory.store.dto.storeStock.StoreStockResponse;
import com.param.inventory.store.dto.storeStock.SubstituteStoreStockDashboardDto;
import com.param.pharmacy.master.unit.dto.MultipleStoreRequest;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;
@SuppressWarnings({"unchecked","unused","deprecation"})
@Repository
public class StoreStockDao extends AbstractDao<Integer, StoreStockRequest> implements IStoreStockDao {

	private static final Logger logger = Logger.getLogger(StoreStockDao.class);

	@Autowired
	private SessionService sessionService;

	
	@Override
	public List<StoreStockResponse> getStoreStock(StoreStockRequest request) {

		String sql = "select item.id as id, item.code as itemCode, item.name as itemName, assetCategory.category as assetCategory, "
				+ " 	productCategory.category as productCategory, coalesce(storeItemMapper.min_qty, 0) as minimumStock, "
				+ " 	coalesce(stock.totalQty, 0) as currentStock, coalesce(stock.totalTransitQty, 0) as stockInTransit, "
				+ " 	coalesce(storeIndent.totalPendingQty, 0) as pendingIndentQuantity "
				+ " from public.m_item item "
				+ " 	inner join public.m_generic generic on item.generic_id=generic.id and (generic.is_deleted=false and generic.is_active=true) "
				+ " 	inner join public.m_product_category productCategory on generic.product_category_id=productCategory.id and (productCategory.is_deleted=false and productCategory.is_active=true) "
				+ " 	inner join public.m_asset_category assetCategory on productCategory.asset_category_id=assetCategory.id and (assetCategory.is_deleted=false and assetCategory.is_active=true) "
				+ " 	inner join public.m_asset_type assetType on assetCategory.asset_type_id=assetType.id and (assetType.is_deleted=false and assetType.is_active=true) "
				+ " 	left outer join public.m_store_item_mapper storeItemMapper on item.id=storeItemMapper.item_id and (storeItemMapper.store_id=:storeId and storeItemMapper.is_active=true and storeItemMapper.is_deleted=false) "
				+ " 	LEFT JOIN (SELECT item.id itemId, sum(stock.available_qty) totalQty, SUM(stock.t_qty) totalTransitQty "
				+ " 				FROM public.m_item item "
				+ " 					INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true) "
				+ " 					INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and stock.store_id = :storeId and (stock.is_deleted=false and stock.is_active=true and stock.available_qty > 0) "
				+ " 				GROUP BY item.id) stock ON stock.itemId = item.id	"
				+ "		LEFT JOIN (SELECT storeIndentDetail.item_id AS itemId, sum(storeIndentDetail.pending_quantity) as totalPendingQty"
				+ "					FROM inventory.t_store_indent storeIndent "
				+ "						INNER JOIN inventory.t_store_indent_detail storeIndentDetail ON storeIndentDetail.store_indent_id = storeIndent.id "
				+ "							AND (storeIndentDetail.is_active=true AND storeIndentDetail.is_deleted=false) "
				+ "					WHERE storeIndent.from_store_id = :storeId AND storeIndent.is_active=true AND storeIndent.is_deleted=false  "
				+ "					GROUP BY storeIndentDetail.item_id)	AS storeIndent ON storeIndent.itemId = item.id "
				+ " where item.is_active=true and item.is_deleted=false and item.unit_id=:unitId and item.is_consignment=:isConsignment "
				+ " 	and (:itemId=0 or item.id=:itemId) "
				+ " 	and (:itemId<>0 and :itemCode<>'' or :itemCode='' or upper(item.code) like upper(:itemCode)) "
				+ " 	and (:itemId<>0 and :itemName<>'' or :itemName='' or upper(item.name) like upper(:itemName)) "
				+ " 	and (:assetTypeId=0 or assetType.id=:assetTypeId) "
				+ " 	and (:assetCategoryId=0 or assetCategory.id=:assetCategoryId) "
				+ " 	and (:productCategoryId=0 or productCategory.id=:productCategoryId) "
				+ " 	and (:storeId=0 or :vedId=0 or generic.id in (select generic.id "
				+ " 										from public.m_storewise_generic storewisGeneric "
				+ " 											inner join public.m_generic generic on storewisGeneric.generic_id=generic.id "
				+ " 												and (generic.is_active=true and generic.is_deleted=false) "
				+ " 											inner join public.m_ved ved on storewisGeneric.ved_id=ved.id "
				+ " 												and (ved.id=:vedId and ved.is_active=true and ved.is_deleted=false) "
				+ " 											inner join public.m_store store on storewisGeneric.store_id=store.store_id "
				+ " 												and (store.store_id=:storeId and store.is_active=true and store.is_deleted=false))) "
				+ " 	and (:storeId=0 or (productCategory.id in (select storePcMapper.product_category_id "
				+ " 									from public.m_store_pc_mapper storePcMapper "
				+ " 									where storePcMapper.store_id=:storeId)) "
				+ " 				and (item.id not in  (select storeItemRestriction.item_id "
				+ " 										from public.m_store_item_restriction storeItemRestriction "
				+ " 										where storeItemRestriction.store_id=:storeId and storeItemRestriction.is_deleted=false and storeItemRestriction.is_active=true))) "
				+ " order by item.id";

		Query query = getSession().createSQLQuery(sql).addScalar("id").addScalar("itemCode").addScalar("itemName")
				.addScalar("assetCategory").addScalar("productCategory").addScalar("minimumStock")
				.addScalar("currentStock", StandardBasicTypes.LONG).addScalar("stockInTransit", StandardBasicTypes.LONG)
				.addScalar("pendingIndentQuantity", StandardBasicTypes.LONG);
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("itemId", request.getItemId() != null ? request.getItemId() : 0);
		query.setParameter("itemCode", request.getItemCode() != null ? request.getItemCode() + '%' : "");
		query.setParameter("itemName", request.getItemName() != null ? request.getItemName() + '%' : "");
		query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId", request.getAssetCategoryId() != null ? request.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				request.getProductCategoryId() != null ? request.getProductCategoryId() : 0);
		query.setParameter("vedId", request.getVedId() != null ? request.getVedId() : 0);
		query.setParameter("isConsignment", request.getIsConsignment() != null ? request.getIsConsignment() : false);
		query.setParameter("unitId", sessionService.getUnitId());

		query.setResultTransformer(Transformers.aliasToBean(StoreStockResponse.class));

		return query.list();
	}

	@Override
	public List<com.param.global.dto.items.BatchResponse1> getBatchDetails(int storeId, int itemId, boolean showDetails,
			Integer unitId) {

		/*
		 * String hql =
		 * "SELECT batch.batchNo AS batchNo, batch.expiryDate AS expiry, uom.uomType.type AS uomType, uom.uomUnit.units AS uomUnit, "
		 * + "		COALESCE(storeStock.availableQty, 0) AS availableStockQuantity" +
		 * "	FROM Batch AS batch " +
		 * "		INNER JOIN batch.item AS item WITH item.id = :itemId AND item.isDeleted = false AND item.isActive = true "
		 * +
		 * "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.store.storeId = :storeId "
		 * +
		 * "				AND storeStock.isActive = true AND storeStock.isDeleted = false AND storeStock.availableQty > 0"
		 * +
		 * "		INNER JOIN batch.batchUomList AS uom WITH uom.uomType.id = :id AND uom.isActive = true AND uom.isDeleted = false "
		 * + " where batch.isActive = true and batch.isDeleted = false";
		 */

		// String hql1 = " select batch0_.batch_no as batchNo , batch0_.expiry_date as
		// expiry, uomtype4_.type as uomType, uomunit5_.unit as uomUnit,
		// coalesce(storestock2_.available_qty, 0) as availableStockQuantity
		// ,batch0_.quantity as totalstock , batch0_.is_consignment as isConsignment ,
		// batch0_.is_quarantine as quarantine , batch0_.selling_price as mrp , r.name
		// as rack , s.name as shelf , batch0_.lot_no as lotNo from inventory.t_batch
		// batch0_ inner join public.m_item item1_ on batch0_.item_id=item1_.id and
		// (item1_.id= :itemId and item1_.is_deleted=false and item1_.is_active=true)
		// inner join inventory.t_store_stock storestock2_ on
		// batch0_.id=storestock2_.batch_id and (storestock2_.store_id=:storeId and
		// storestock2_.is_active=true and storestock2_.is_deleted=false and
		// storestock2_.available_qty>0) inner join inventory.t_batch_uom_mapper
		// batchuomli3_ on batch0_.id=batchuomli3_.batch_id and
		// (batchuomli3_.uom_type_id=:id and batchuomli3_.is_active=true and
		// batchuomli3_.is_deleted=false) left join procurement.t_shelf_item loc on
		// batch0_.id = loc.batch_id and loc.is_active=true and loc.is_deleted=false
		// left join public.m_rack r on loc.rack_id = r.id left join public.m_shelf s on
		// loc.shelf_id = s.id, public.m_uom_type uomtype4_, public.m_uom_unit uomunit5_
		// where batchuomli3_.uom_type_id=uomtype4_.id and
		// batchuomli3_.uom_unit_id=uomunit5_.id and batch0_.is_active=true and
		// batch0_.is_deleted=false ";
		/*
		 * String
		 * hql1="  select batch0_.batch_no as batchNo ,sum (batch0_.current_quantity) as totalstock, batch0_.expiry_date as expiry, uomtype4_.type as uomType, uomunit5_.unit as uomUnit, \r\n"
		 * + " batch0_.is_consignment as isConsignment ,\r\n" +
		 * " batch0_.is_quarantine as quarantine , batch0_.selling_price as mrp  , r.name as rack , s.name as shelf , batch0_.lot_no as lotNo \r\n"
		 * +
		 * " from inventory.t_batch batch0_  inner join public.m_item item1_ on batch0_.item_id=item1_.id   \r\n"
		 * +
		 * " and (item1_.id =:itemId  and item1_.is_deleted=false and item1_.is_active=true)  \r\n"
		 * +
		 * " inner join inventory.t_store_stock storestock2_ on batch0_.id=storestock2_.batch_id  and (storestock2_.store_id=:storeId and storestock2_.is_active=true \r\n"
		 * +
		 * " and storestock2_.is_deleted=false and storestock2_.available_qty>0) inner join inventory.t_batch_uom_mapper batchuomli3_ \r\n"
		 * +
		 * " on batch0_.id=batchuomli3_.batch_id and (batchuomli3_.uom_type_id=:id and batchuomli3_.is_active=true and batchuomli3_.is_deleted=false) \r\n"
		 * +
		 * " left join procurement.t_shelf_item  loc on batch0_.id = loc.batch_id and loc.is_active=true and loc.is_deleted=false left join public.m_rack  r \r\n"
		 * +
		 * " on loc.rack_id = r.id left join  public.m_shelf s on loc.shelf_id = s.id, public.m_uom_type uomtype4_, public.m_uom_unit uomunit5_  \r\n"
		 * +
		 * " where batchuomli3_.uom_type_id=uomtype4_.id and batchuomli3_.uom_unit_id=uomunit5_.id and batch0_.is_active=true and batch0_.is_deleted=false \r\n"
		 * +
		 * " group by batchNo,expiry,uomType,uomUnit,isConsignment,quarantine,mrp,rack,shelf,lotNo\r\n"
		 * + " order by batch0_.is_consignment  ";
		 */

		StringBuffer sb = new StringBuffer();
		String hql1 = "  select   batch0_.id   as  batchid , batch0_.batch_no as batchNo ,sum (batch0_.quantity) as totalstock, batch0_.expiry_date as expiry, uomtype4_.type as uomType, uomunit5_.unit as uomUnit, \r\n"
				+ " batch0_.is_consignment as isConsignment ,\r\n"
				+ " batch0_.is_quarantine as quarantine , batch0_.selling_price as mrp , batch0_.cop as cop , r.name as rack , s.name as shelf , batch0_.lot_no as lotNo ,  batch0_ .actual_lending_price  as actualCost \r\n"
				+ " from inventory.t_batch batch0_  inner join public.m_item item1_ on batch0_.item_id=item1_.id   \r\n"
				+ " and (item1_.id =:itemId  and item1_.is_deleted=false and item1_.is_active=true)  \r\n"
				+ " inner join inventory.t_store_stock storestock2_ on batch0_.id=storestock2_.batch_id  and (storestock2_.store_id=:storeId and storestock2_.is_active=true \r\n"
				+ " and storestock2_.is_deleted=false and storestock2_.available_qty>0) left join inventory.t_batch_uom_mapper batchuomli3_ \r\n"
				+ " on batch0_.id=batchuomli3_.batch_id and (batchuomli3_.uom_type_id=:id and batchuomli3_.is_active=true and batchuomli3_.is_deleted=false) \r\n"
				+ " left join procurement.t_shelf_item  loc on batch0_.id = loc.batch_id and loc.is_active=true and loc.is_deleted=false left join public.m_rack  r \r\n"
				+ " on loc.rack_id = r.id left join  public.m_shelf s on loc.shelf_id = s.id  left join  public.m_uom_type uomtype4_ on  batchuomli3_.uom_type_id=uomtype4_.id  left join  public.m_uom_unit uomunit5_   on   batchuomli3_.uom_unit_id=uomunit5_.id  \r\n"
				+ " where batch0_.is_active=true and batch0_.is_deleted=false and   batch0_.is_quarantine=false and   "
				+ "  batch0_.unit_id = :unitId and storestock2_.total_qty is not null and storestock2_.available_qty is not null "
				+ "  and (batch0_.expiry_date >=current_date or batch0_.expiry_date is null) ";

		sb.append(hql1);
		//sb.append(!showDetails ? " and ( batch0_.batch_no is not null or  batch0_.batch_no is null )  " : ""); // HIS-10524
		sb.append(" and (:showDetails = true OR (batch0_.batch_no is not null OR batch0_.batch_no is null)) ");
		sb.append(
				" group by actualCost,cop,batchNo,expiry,uomType,uomUnit,isConsignment,quarantine,mrp,rack,shelf,lotNo , batchid \r\n"
						+ " order by batch0_.batch_no asc, batch0_.expiry_date asc ");
		/*
		 * String hql1 =
		 * " select batch0_.batch_no as batchNo ,sum (batch0_.quantity) as totalstock, batch0_.expiry_date as expiry, uomtype4_.type as uomType, uomunit5_.unit as uomUnit, \r\n"
		 * + " batch0_.is_consignment as isConsignment ,\r\n" +
		 * " batch0_.is_quarantine as quarantine , batch0_.selling_price as mrp  , r.name as rack , s.name as shelf , batch0_.lot_no as lotNo \r\n"
		 * +
		 * "  from inventory.t_batch batch0_  inner  join public.m_item item1_ on batch0_.item_id=item1_.id   \r\n"
		 * +
		 * " and (item1_.id =:itemId  and item1_.is_deleted=false and item1_.is_active=true)  \r\n"
		 * +
		 * " inner join inventory.t_store_stock storestock2_ on batch0_.id=storestock2_.batch_id\r\n"
		 * + " and (storestock2_.store_id=:storeId and storestock2_.is_active=true \r\n"
		 * +
		 * " and storestock2_.is_deleted=false and storestock2_.available_qty>0) left join inventory.t_batch_uom_mapper batchuomli3_ \r\n"
		 * +
		 * " on batch0_.id=batchuomli3_.batch_id and (batchuomli3_.uom_type_id=:id and batchuomli3_.is_active=true and batchuomli3_.is_deleted=false) \r\n"
		 * +
		 * " left join procurement.t_shelf_item  loc on batch0_.id = loc.batch_id and loc.is_active=true and loc.is_deleted=false \r\n"
		 * + " left join public.m_rack  r on loc.rack_id = r.id \r\n" +
		 * " left join  public.m_shelf s on loc.shelf_id = s.id\r\n" +
		 * " left join  public.m_uom_type uomtype4_ on  batchuomli3_.uom_type_id=uomtype4_.id \r\n"
		 * +
		 * " left join  public.m_uom_unit uomunit5_   on   batchuomli3_.uom_unit_id=uomunit5_.id \r\n"
		 * + " where   batch0_.is_active=true and batch0_.is_deleted=false \r\n" +
		 * "  group by batchNo,expiry,uomType,uomUnit,isConsignment,quarantine,mrp,rack,shelf,lotNo\r\n"
		 * + "  order by batch0_.is_consignment ";
		 */

		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("id", Constants.uomTypeId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("showDetails", showDetails);

		query.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.items.BatchResponse1.class));

		return query.list();
	}

	@Override
	public List<BatchResponse1> getBatchNotApplicableDetails(int storeId, int itemId, Integer unitId) {
		String hql1 = "SELECT Sum (batch0_.quantity)     AS totalstock, "
				+ "       uomtype4_.type             AS uomType, " + "       uomunit5_.unit             AS uomUnit, "
				+ "       batch0_.is_consignment     AS isConsignment, "
				+ "       batch0_.is_quarantine      AS quarantine, " + "       Avg(batch0_.selling_price) AS mrp "
				+ "FROM   inventory.t_batch batch0_ " + "       INNER JOIN PUBLIC.m_item item1_ "
				+ "               ON batch0_.item_id = item1_.id " + "                  AND ( item1_.id = :itemId "
				+ "                        AND item1_.is_deleted = false "
				+ "                        AND item1_.is_active = true ) "
				+ "       INNER JOIN inventory.t_store_stock storestock2_ "
				+ "               ON batch0_.id = storestock2_.batch_id "
				+ "                  AND ( storestock2_.store_id = :storeId "
				+ "                        AND storestock2_.is_active = true "
				+ "                        AND storestock2_.is_deleted = false "
				+ "                        AND storestock2_.available_qty > 0 ) "
				+ "       LEFT JOIN inventory.t_batch_uom_mapper batchuomli3_ "
				+ "              ON batch0_.id = batchuomli3_.batch_id "
				+ "                 AND ( batchuomli3_.uom_type_id = :id "
				+ "                       AND batchuomli3_.is_active = true "
				+ "                       AND batchuomli3_.is_deleted = false ) "
				+ "       LEFT JOIN PUBLIC.m_uom_type uomtype4_ "
				+ "              ON batchuomli3_.uom_type_id = uomtype4_.id "
				+ "                 AND uomtype4_.is_active = true "
				+ "                 AND uomtype4_.is_deleted = false " + "       LEFT JOIN PUBLIC.m_uom_unit uomunit5_ "
				+ "              ON batchuomli3_.uom_unit_id = uomunit5_.id "
				+ "                 AND uomunit5_.is_active = true "
				+ "                 AND uomunit5_.is_deleted = false "
				+ "       LEFT JOIN procurement.t_shelf_item loc " + "              ON batch0_.id = loc.batch_id "
				+ "                 AND loc.is_active = true " + "                 AND loc.is_deleted = false "
				+ "       LEFT JOIN PUBLIC.m_rack r " + "              ON loc.rack_id = r.id "
				+ "                 AND r.is_active = true " + "                 AND r.is_deleted = false "
				+ "       LEFT JOIN PUBLIC.m_shelf s " + "              ON loc.shelf_id = s.id "
				+ "                 AND loc.is_active = true " + "                 AND loc.is_deleted = false "
				+ "WHERE  batch0_.is_active = true " + "       AND batch0_.is_deleted = false "
				+ "       AND batch0_.unit_id = :unitId " + "       AND batch0_.is_batch_required = false "
				+ "GROUP  BY uomtype, " + "          uomunit, " + "          isconsignment, " + "          quarantine "
				+ "ORDER  BY batch0_.is_consignment";

		/*
		 * String hql1 =
		 * " select batch0_.batch_no as batchNo ,sum (batch0_.quantity) as totalstock, batch0_.expiry_date as expiry, uomtype4_.type as uomType, uomunit5_.unit as uomUnit, \r\n"
		 * + " batch0_.is_consignment as isConsignment ,\r\n" +
		 * " batch0_.is_quarantine as quarantine , batch0_.selling_price as mrp  , r.name as rack , s.name as shelf , batch0_.lot_no as lotNo \r\n"
		 * +
		 * "  from inventory.t_batch batch0_  inner  join public.m_item item1_ on batch0_.item_id=item1_.id   \r\n"
		 * +
		 * " and (item1_.id =:itemId  and item1_.is_deleted=false and item1_.is_active=true)  \r\n"
		 * +
		 * " inner join inventory.t_store_stock storestock2_ on batch0_.id=storestock2_.batch_id\r\n"
		 * + " and (storestock2_.store_id=:storeId and storestock2_.is_active=true \r\n"
		 * +
		 * " and storestock2_.is_deleted=false and storestock2_.available_qty>0) left join inventory.t_batch_uom_mapper batchuomli3_ \r\n"
		 * +
		 * " on batch0_.id=batchuomli3_.batch_id and (batchuomli3_.uom_type_id=:id and batchuomli3_.is_active=true and batchuomli3_.is_deleted=false) \r\n"
		 * +
		 * " left join procurement.t_shelf_item  loc on batch0_.id = loc.batch_id and loc.is_active=true and loc.is_deleted=false \r\n"
		 * + " left join public.m_rack  r on loc.rack_id = r.id \r\n" +
		 * " left join  public.m_shelf s on loc.shelf_id = s.id\r\n" +
		 * " left join  public.m_uom_type uomtype4_ on  batchuomli3_.uom_type_id=uomtype4_.id \r\n"
		 * +
		 * " left join  public.m_uom_unit uomunit5_   on   batchuomli3_.uom_unit_id=uomunit5_.id \r\n"
		 * + " where   batch0_.is_active=true and batch0_.is_deleted=false \r\n" +
		 * "  group by batchNo,expiry,uomType,uomUnit,isConsignment,quarantine,mrp,rack,shelf,lotNo\r\n"
		 * + "  order by batch0_.is_consignment ";
		 */

		Query query = getSession().createSQLQuery(hql1);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("id", Constants.uomTypeId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.items.BatchResponse1.class));

		return query.list();
	}

	@Override
	public List<StoreStockResponse> getStoreIndentDateWiseList(StoreStockRequestDate request) {

		String isConsignmentCondation = request.getIsConsignment() != null
				? " and item.is_consignment=" + request.getIsConsignment() + " "
				: " ";
		String searchItem = request.getSearch()!= null ? " and ( (upper(item.code) like '%"+request.getSearch().toUpperCase()+"%') or (upper(item.name) like '%"+request.getSearch().toUpperCase()+"%') ) ":"" ;

		String sql = "select distinct item.id as id, item.code as itemCode, item.name as itemName, assetCategory.category as assetCategory, generic.name as genericName , m.name as menufactureName , "
				+ " 	productCategory.category as productCategory, coalesce(storeItemMapper.min_qty, 0) as minimumStock, "
				+ " 	 coalesce(stock.totalQty, 0) as currentStock,   case  when coalesce(stock.totalTransitQty, 0) >=0 then coalesce(stock.totalTransitQty, 0)  else   0  end as stockInTransit , "
				+ " 	coalesce(storeIndent.totalPendingQty, 0) as pendingIndentQuantity "
				+ " from public.m_item item "
				+ " 	inner join public.m_generic generic on item.generic_id=generic.id and (generic.is_deleted=false and generic.is_active=true) "
				+ " 	inner join public.m_product_category productCategory on generic.product_category_id=productCategory.id and (productCategory.is_deleted=false and productCategory.is_active=true) "
				+ " 	inner join public.m_asset_category assetCategory on productCategory.asset_category_id=assetCategory.id and (assetCategory.is_deleted=false and assetCategory.is_active=true) "
				+ " 	inner join public.m_asset_type assetType on assetCategory.asset_type_id=assetType.id and (assetType.is_deleted=false and assetType.is_active=true) "
				+ " 	inner join  public.m_store_item_mapper storeItemMapper on item.id=storeItemMapper.item_id and (storeItemMapper.store_id=:storeId and storeItemMapper.is_active=true and storeItemMapper.is_deleted=false) "
				+ " 	LEFT JOIN (SELECT item.id itemId, sum(stock.available_qty) totalQty, SUM(stock.t_qty) totalTransitQty "
				+ " 				FROM public.m_item item "
				+ " 					INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true and batch.unit_id=:unitId and batch.is_quarantine=false and batch.added_date <=:added_date and (batch.expiry_date >= current_date  or batch.expiry_date is null)  ) "
				+ " 					INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and stock.store_id = :storeId and (stock.is_deleted=false and stock.is_active=true)  and stock.unit_id=:unitId   and stock.total_qty is not null  "
				+ " 				GROUP BY item.id) stock ON stock.itemId = item.id	"
				+ "		LEFT JOIN (SELECT storeIndentDetail.item_id AS itemId, sum(storeIndentDetail.pending_quantity) as totalPendingQty"
				+ "					FROM inventory.t_store_indent storeIndent "
				+ "						INNER JOIN inventory.t_store_indent_detail storeIndentDetail ON storeIndentDetail.store_indent_id = storeIndent.id "
				+ "							AND (storeIndentDetail.is_active=true AND storeIndentDetail.is_deleted=false AND storeIndentDetail.pending_quantity > 0) "
				+ "					WHERE storeIndent.from_store_id = :storeId AND  storeIndent.status_id!=1  AND storeIndent.is_active=true AND storeIndent.is_deleted=false  and storeIndent.unit_id=:unitId  "
				+ "					GROUP BY storeIndentDetail.item_id)	AS storeIndent ON storeIndent.itemId = item.id "
				  + " 	Left join public.m_manufacturer m on item.manufacturer_id=m.id and (m.is_deleted=false and m.is_active=true) "
				+ " where item.is_active=true and item.is_deleted=false  " + isConsignmentCondation + searchItem
				+ " and stock.totalQty >0 "
				+ " 	and (:itemId=0 or item.id=:itemId) "
				+ " 	and (:itemId<>0 and :itemCode<>'' or :itemCode='' or upper(item.code) like upper(:itemCode)) "
				+ " 	and (:itemId<>0 and :itemName<>'' or :itemName='' or upper(item.name) like upper(:itemName)) "
				+ " 	and (:assetTypeId=0 or assetType.id=:assetTypeId) "
				+ " 	and (:assetCategoryId=0 or assetCategory.id=:assetCategoryId) "
				+ " 	and (:productCategoryId=0 or productCategory.id=:productCategoryId) "
				+ " 	and (:storeId=0 or :vedId=0 or generic.id in (select generic.id "
				+ " 										from public.m_storewise_generic storewisGeneric "
				+ " 											inner join public.m_generic generic on storewisGeneric.generic_id=generic.id "
				+ " 												and (generic.is_active=true and generic.is_deleted=false) "
				+ " 											inner join public.m_ved ved on storewisGeneric.ved_id=ved.id "
				+ " 												and (ved.id=:vedId and ved.is_active=true and ved.is_deleted=false) "
				+ " 											inner join public.m_store store on storewisGeneric.store_id=store.store_id "
				+ " 												and (store.store_id=:storeId and store.is_active=true and store.is_deleted=false))) "
				+ " 	and (:storeId=0 or (productCategory.id in (select storePcMapper.product_category_id "
				+ " 									from public.m_store_pc_mapper storePcMapper "
				+ " 									where storePcMapper.store_id=:storeId)) "
				+ " 				and (item.id not in  (select storeItemRestriction.item_id "
				+ " 										from public.m_store_item_restriction storeItemRestriction "
				+ " 										where storeItemRestriction.store_id=:storeId and storeItemRestriction.is_deleted=false and storeItemRestriction.is_active=true and storeItemRestriction.organization_id=:organizationId))) "
				+ " union"

				+ " select distinct item.id as id, item.code as itemCode, item.name as itemName, assetCategory.category as assetCategory,  '-' as genericName , m.name as menufactureName , "
				+ " 	productCategory.category as productCategory, coalesce(storeItemMapper.min_qty, 0) as minimumStock, "
				+ " 	coalesce(stock.totalQty, 0) as currentStock, case  when coalesce(stock.totalTransitQty, 0) >=0 then coalesce(stock.totalTransitQty, 0)  else   0  end as stockInTransit , "
				+ " 	coalesce(storeIndent.totalPendingQty, 0) as pendingIndentQuantity "
				+ " from public.m_item item "
				+ " 	inner join public.m_product_category productCategory on item.product_category_id=productCategory.id and (productCategory.is_deleted=false and productCategory.is_active=true) "
				+ " 	inner join public.m_asset_category assetCategory on productCategory.asset_category_id=assetCategory.id and (assetCategory.is_deleted=false and assetCategory.is_active=true) "
				+ " 	inner join public.m_asset_type assetType on assetCategory.asset_type_id=assetType.id and (assetType.is_deleted=false and assetType.is_active=true) "
				+ " 	inner  join public.m_store_item_mapper storeItemMapper on item.id=storeItemMapper.item_id and (storeItemMapper.store_id=:storeId and storeItemMapper.is_active=true and storeItemMapper.is_deleted=false) "
				+ " 	LEFT JOIN (SELECT item.id itemId, sum(stock.available_qty) totalQty, SUM(stock.t_qty) totalTransitQty "
				+ " 				FROM public.m_item item "
				+ " 					INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_quarantine=false and batch.is_active=true and batch.unit_id=:unitId and batch.added_date <=:added_date    ) "
				+ " 					INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and stock.store_id = :storeId and (stock.is_deleted=false and stock.is_active=true) and stock.unit_id=:unitId  and stock.total_qty is not null  "
				+ " 				GROUP BY item.id) stock ON stock.itemId = item.id	"
				+ "		LEFT JOIN (SELECT storeIndentDetail.item_id AS itemId, sum(storeIndentDetail.pending_quantity) as totalPendingQty"
				+ "					FROM inventory.t_store_indent storeIndent "
				+ "						INNER JOIN inventory.t_store_indent_detail storeIndentDetail ON storeIndentDetail.store_indent_id = storeIndent.id "
				+ "							AND (storeIndentDetail.is_active=true AND storeIndentDetail.is_deleted=false AND storeIndentDetail.pending_quantity > 0) "
				+ "					WHERE storeIndent.from_store_id = :storeId AND storeIndent.is_active=true AND storeIndent.is_deleted=false and storeIndent.unit_id=:unitId "
				+ "					GROUP BY storeIndentDetail.item_id)	AS storeIndent ON storeIndent.itemId = item.id "
				   + " 	Left join public.m_manufacturer m on item.manufacturer_id=m.id and (m.is_deleted=false and m.is_active=true) "
				+ " where item.is_active=true and item.is_deleted=false " + isConsignmentCondation + searchItem
				+ " and stock.totalQty >0 "
				+ " 	and (:itemId=0 or item.id=:itemId) "
				+ " 	and (:itemId<>0 and :itemCode<>'' or :itemCode='' or upper(item.code) like upper(:itemCode)) "
				+ " 	and (:itemId<>0 and :itemName<>'' or :itemName='' or upper(item.name) like upper(:itemName)) "
				+ " 	and (:assetTypeId=0 or assetType.id=:assetTypeId) "
				+ " 	and (:assetCategoryId=0 or assetCategory.id=:assetCategoryId) "
				+ " 	and (:productCategoryId=0 or productCategory.id=:productCategoryId) "
				+ "     and item.generic_id is null "
				+ " 				and (item.id not in  (select storeItemRestriction.item_id "
				+ " 										from public.m_store_item_restriction storeItemRestriction "
				+ " 										where storeItemRestriction.store_id=:storeId and storeItemRestriction.is_deleted=false and storeItemRestriction.is_active=true and storeItemRestriction.organization_id=:organizationId)) ";

		Query query = getSession().createSQLQuery(sql).addScalar("id").addScalar("itemCode").addScalar("itemName")
				.addScalar("assetCategory").addScalar("genericName").addScalar("menufactureName").addScalar("productCategory").addScalar("minimumStock")
				.addScalar("currentStock", StandardBasicTypes.LONG).addScalar("stockInTransit", StandardBasicTypes.LONG)
				.addScalar("pendingIndentQuantity", StandardBasicTypes.LONG);
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("itemId", request.getItemId() != null ? request.getItemId() : 0);
		query.setParameter("itemCode", request.getItemCode() != null ? request.getItemCode() + '%' : "");
		query.setParameter("itemName", request.getItemName() != null ? request.getItemName() + '%' : "");
		query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId", request.getAssetCategoryId() != null ? request.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				request.getProductCategoryId() != null ? request.getProductCategoryId() : 0);
		query.setParameter("vedId", request.getVedId() != null ? request.getVedId() : 0);
		query.setParameter("unitId", request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId());
		query.setParameter("added_date", request.getFromDate());
		query.setParameter("organizationId",
				request.getOrganizationId() != null ? request.getOrganizationId() : sessionService.getOrganizationId());
		query.setResultTransformer(Transformers.aliasToBean(StoreStockResponse.class));
		query.setFirstResult(request.getOffset() != null ? request.getOffset() : 0);
		query.setMaxResults(request.getRecordPerPage() != null ? request.getRecordPerPage() : 50);
		return query.list();

	}

	public List<ItemWiseBatchResponse> getItemWiseBatchDetails(int storeId, int itemId) {

		String hql = "SELECT storeStock.store.storeId as storeId,storeStock.store.storeName as storeName,item.id as itemId,item.code as itemCode,item.name as  itemName,batch.batchNo AS batchNo, batch.expiryDate AS expiry,"
				/* + "		COALESCE(storeStock.availableQty, 0) AS availableStockQuantity" */
				+ "		storeStock.availableQty AS availableStockQuantity" + "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.id = :itemId AND item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.store.storeId = :storeId "
				+ "				AND storeStock.isActive = true AND storeStock.isDeleted = false AND storeStock.availableQty > 0"
				+ " where batch.isActive = true and batch.isDeleted = false and batch.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("unitId", sessionService.getUnitId());

		query.setResultTransformer(Transformers.aliasToBean(ItemWiseBatchResponse.class));

		return query.list();
	}

	public List<ItemWiseBatchResponse> getItemWiseBatchDetailsWithUnit(int itemId, Integer unitId) {

		String hql = "SELECT storeStock.store.storeId as storeId,storeStock.store.storeName as storeName,item.id as itemId,item.code as itemCode,item.name as  itemName,batch.batchNo AS batchNo, batch.expiryDate AS expiry,"
				/* + "		COALESCE(storeStock.availableQty, 0) AS availableStockQuantity" */
				+ "		storeStock.availableQty AS availableStockQuantity" + "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.id = :itemId AND item.isDeleted = false AND item.isActive = true "
				/*
				 * +
				 * "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.store.storeId = :storeId "
				 */
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isActive = true AND storeStock.isDeleted = false AND storeStock.availableQty > 0"
				+ " where batch.isActive = true and batch.isDeleted = false and batch.unit.unitId = :unitId";

		Query query = getSession().createQuery(hql);
		// query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		// query.setParameter("id", Constants.uomTypeId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		query.setResultTransformer(Transformers.aliasToBean(ItemWiseBatchResponse.class));

		return query.list();
	}

	@Override
	public List<ItemWiseBatchResponse> getBatchWiseBatchDetails(int batchId, Integer unitId) {

		String hql = "SELECT storeStock.store.storeId as storeId,storeStock.store.storeName as storeName,item.id as itemId,item.code as itemCode,item.name as  itemName,batch.batchNo AS batchNo, batch.expiryDate AS expiry,"
				+ "		COALESCE(storeStock.availableQty, 0) AS availableStockQuantity" + "	FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isActive = true AND storeStock.isDeleted = false "
				+ " where batch.id= :batchId AND batch.isActive = true and batch.isDeleted = false and batch.unit.unitId = :unitId ";

		Query query = getSession().createQuery(hql);
		// query.setParameter("storeId", storeId);
		query.setParameter("batchId", Long.parseLong(String.valueOf(batchId)));
		// query.setParameter("id", Constants.uomTypeId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ItemWiseBatchResponse.class));

		return query.list();
	}

	@Override
	public List<ExpiredStoreStockResponse> expiredStoreStockDashboard(
			ExpiredStoreStockRequest request) {

		Integer storeId = 0;
		for (MultipleStoreRequest s : request.getMultipleStore()) {
			storeId = s.getId();
		}

		Calendar cal = Calendar.getInstance();
		if (request.getFromDate() != null) {
			cal.setTime(request.getFromDate());
			cal.set(request.getFromDate().getYear() + 1900, request.getFromDate().getMonth(), 1);
			// cal1.add(Calendar.YEAR, request.getToDate().getYear());
			request.setFromDate(cal.getTime());
		} else {
			Date today = cal.getTime();
			cal.add(Calendar.YEAR, -5); // to get previous year add -1
			Date preYear = cal.getTime();
		}

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(request.getToDate());
		cal1.set(request.getToDate().getYear() + 1900, request.getToDate().getMonth(), 1);
		// cal1.add(Calendar.YEAR, request.getToDate().getYear());
		request.setToDate(cal1.getTime());
		SimpleDateFormat simpleDateFormat1 = null;
		Date todate = null;
		SimpleDateFormat simpleDateFormat2 = null;
		Date fromdate = null;
		try {

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy"); // Existing Pattern
			todate = simpleDateFormat.parse(cal1.getTime().toString()); // Returns Date Format,
			fromdate = simpleDateFormat.parse(cal.getTime().toString()); // Returns Date Format,
			simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd"); // New Pattern
			// //System.out.println(simpleDateFormat1.format(todate));

		} catch (ParseException e) {
			logger.error("Error while parsing expired store stock dashboard date range", e);
			throw new ServiceException(e.getMessage());
		}
		String dd1 = simpleDateFormat1.format(fromdate);
		String dd2 = simpleDateFormat1.format(todate);
		// System.out.println(" formatted from date : " + dd1);
		// System.out.println(" formatted to date : " + dd2);

		String isConsignmentCondation = request.getIsConsignment() != null
				? " and item.is_consignment=" + request.getIsConsignment() + " "
				: " ";
		String searchItem = request.getSearch()!= null ? " and ( (upper(item.code) like '%"+request.getSearch().toUpperCase()+"%') or (upper(item.name) like '%"+request.getSearch().toUpperCase()+"%') ) ":"" ;

		String sql = "select distinct item.id as id, item.code as itemCode, item.name as itemName, assetCategory.category as assetCategory, generic.name as genericName , m.name as menufactureName ,  "
				+ " 	productCategory.category as productCategory, coalesce(storeItemMapper.min_qty, 0) as minimumStock, "
				+ " 	 coalesce(stock.totalQty, 0) as currentStock,   coalesce(stock.totalTransitQty, 0)  as stockInTransit, "
				+ " 	coalesce(storeIndent.totalPendingQty, 0) as pendingIndentQuantity "
				+ " from public.m_item item "
				+ " 	inner join public.m_generic generic on item.generic_id=generic.id and (generic.is_deleted=false and generic.is_active=true) "
				+ " 	inner join public.m_product_category productCategory on generic.product_category_id=productCategory.id and (productCategory.is_deleted=false and productCategory.is_active=true) "
				+ " 	inner join public.m_asset_category assetCategory on productCategory.asset_category_id=assetCategory.id and (assetCategory.is_deleted=false and assetCategory.is_active=true) "
				+ " 	inner join public.m_asset_type assetType on assetCategory.asset_type_id=assetType.id and (assetType.is_deleted=false and assetType.is_active=true) "
				+ " 	inner join  public.m_store_item_mapper storeItemMapper on item.id=storeItemMapper.item_id and (storeItemMapper.store_id=:storeId and storeItemMapper.is_active=true and storeItemMapper.is_deleted=false) "
				+ " 	LEFT JOIN (SELECT item.id itemId, sum(stock.available_qty) totalQty, SUM(stock.t_qty) totalTransitQty "
				+ " 				FROM public.m_item item "
				+ " 					INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true and batch.unit_id=:unitId) "
				+ "      AND batch.expiry_date  BETWEEN to_date( '" + dd1.trim() + "','YYYY-MM-DD') AND to_date( '"
				+ dd2.trim() + "','YYYY-MM-DD')   "
				+ " 					INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and stock.store_id = :storeId and (stock.is_deleted=false and stock.is_active=true)  and stock.unit_id=:unitId "
				+ " 				GROUP BY item.id) stock ON stock.itemId = item.id	"
				+ "		LEFT JOIN (SELECT storeIndentDetail.item_id AS itemId, sum(storeIndentDetail.pending_quantity) as totalPendingQty"
				+ "					FROM inventory.t_store_indent storeIndent "
				+ "						INNER JOIN inventory.t_store_indent_detail storeIndentDetail ON storeIndentDetail.store_indent_id = storeIndent.id "
				+ "							AND (storeIndentDetail.is_active=true AND storeIndentDetail.is_deleted=false AND storeIndentDetail.pending_quantity > 0) "
				+ "					WHERE storeIndent.from_store_id = :storeId AND storeIndent.is_active=true AND storeIndent.is_deleted=false  and storeIndent.unit_id=:unitId  "
				+ "					GROUP BY storeIndentDetail.item_id)	AS storeIndent ON storeIndent.itemId = item.id "
				   + " 	Left join public.m_manufacturer m on item.manufacturer_id=m.id and (m.is_deleted=false and m.is_active=true) "
				+ " where item.is_active=true and item.is_deleted=false  " + isConsignmentCondation + searchItem
				+ " and stock.totalQty >0 "
				+ " 	and (:itemId=0 or item.id=:itemId) "
				+ " 	and (:itemId<>0 and :itemCode<>'' or :itemCode='' or upper(item.code) like upper(:itemCode)) "
				+ " 	and (:itemId<>0 and :itemName<>'' or :itemName='' or upper(item.name) like upper(:itemName)) "
				+ " 	and (:assetTypeId=0 or assetType.id=:assetTypeId) "
				+ " 	and (:assetCategoryId=0 or assetCategory.id=:assetCategoryId) "
				+ " 	and (:productCategoryId=0 or productCategory.id=:productCategoryId) "
				+ " 	and (:storeId=0 or :vedId=0 or generic.id in (select generic.id "
				+ " 										from public.m_storewise_generic storewisGeneric "
				+ " 											inner join public.m_generic generic on storewisGeneric.generic_id=generic.id "
				+ " 												and (generic.is_active=true and generic.is_deleted=false) "
				+ " 											inner join public.m_ved ved on storewisGeneric.ved_id=ved.id "
				+ " 												and (ved.id=:vedId and ved.is_active=true and ved.is_deleted=false) "
				+ " 											inner join public.m_store store on storewisGeneric.store_id=store.store_id "
				+ " 												and (store.store_id=:storeId and store.is_active=true and store.is_deleted=false))) "
				+ " 	and (:storeId=0 or (productCategory.id in (select storePcMapper.product_category_id "
				+ " 									from public.m_store_pc_mapper storePcMapper "
				+ " 									where storePcMapper.store_id=:storeId)) "
				+ " 				and (item.id not in  (select storeItemRestriction.item_id "
				+ " 										from public.m_store_item_restriction storeItemRestriction "
				+ " 										where storeItemRestriction.store_id=:storeId and storeItemRestriction.is_deleted=false and storeItemRestriction.is_active=true and storeItemRestriction.organization_id=:organizationId))) "
				+ " union"

				+ " select distinct item.id as id, item.code as itemCode, item.name as itemName, assetCategory.category as assetCategory, '-' as genericName , m.name as menufactureName ,   "
				+ " 	productCategory.category as productCategory, coalesce(storeItemMapper.min_qty, 0) as minimumStock, "
				+ " 	coalesce(stock.totalQty, 0) as currentStock, coalesce(stock.totalTransitQty, 0) as stockInTransit, "
				+ " 	coalesce(storeIndent.totalPendingQty, 0) as pendingIndentQuantity "
				+ " from public.m_item item "
				+ " 	inner join public.m_product_category productCategory on item.product_category_id=productCategory.id and (productCategory.is_deleted=false and productCategory.is_active=true) "
				+ " 	inner join public.m_asset_category assetCategory on productCategory.asset_category_id=assetCategory.id and (assetCategory.is_deleted=false and assetCategory.is_active=true) "
				+ " 	inner join public.m_asset_type assetType on assetCategory.asset_type_id=assetType.id and (assetType.is_deleted=false and assetType.is_active=true) "
				+ " 	inner  join public.m_store_item_mapper storeItemMapper on item.id=storeItemMapper.item_id and (storeItemMapper.store_id=:storeId and storeItemMapper.is_active=true and storeItemMapper.is_deleted=false) "
				+ " 	LEFT JOIN (SELECT item.id itemId, sum(stock.available_qty) totalQty, SUM(stock.t_qty) totalTransitQty "
				+ " 				FROM public.m_item item "
				+ " 					INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true and batch.unit_id=:unitId) "
				+ "      AND batch.expiry_date  BETWEEN to_date( '" + dd1.trim() + "','YYYY-MM-DD') AND to_date( '"
				+ dd2.trim() + "','YYYY-MM-DD')  "
				+ " 					INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and stock.store_id = :storeId and (stock.is_deleted=false and stock.is_active=true) and stock.unit_id=:unitId "
				+ " 				GROUP BY item.id) stock ON stock.itemId = item.id	"
				+ "		LEFT JOIN (SELECT storeIndentDetail.item_id AS itemId, sum(storeIndentDetail.pending_quantity) as totalPendingQty"
				+ "					FROM inventory.t_store_indent storeIndent "
				+ "						INNER JOIN inventory.t_store_indent_detail storeIndentDetail ON storeIndentDetail.store_indent_id = storeIndent.id "
				+ "							AND (storeIndentDetail.is_active=true AND storeIndentDetail.is_deleted=false AND storeIndentDetail.pending_quantity > 0) "
				+ "					WHERE storeIndent.from_store_id = :storeId AND storeIndent.is_active=true AND storeIndent.is_deleted=false and storeIndent.unit_id=:unitId "
				+ "					GROUP BY storeIndentDetail.item_id)	AS storeIndent ON storeIndent.itemId = item.id "
				  + " 	Left join public.m_manufacturer m on item.manufacturer_id=m.id and (m.is_deleted=false and m.is_active=true) "
				+ " where item.is_active=true and item.is_deleted=false " + isConsignmentCondation + searchItem
				+ " and stock.totalQty >0 "
				+ " 	and (:itemId=0 or item.id=:itemId) "
				+ " 	and (:itemId<>0 and :itemCode<>'' or :itemCode='' or upper(item.code) like upper(:itemCode)) "
				+ " 	and (:itemId<>0 and :itemName<>'' or :itemName='' or upper(item.name) like upper(:itemName)) "
				+ " 	and (:assetTypeId=0 or assetType.id=:assetTypeId) "
				+ " 	and (:assetCategoryId=0 or assetCategory.id=:assetCategoryId) "
				+ " 	and (:productCategoryId=0 or productCategory.id=:productCategoryId) "
				+ " 				and (item.id not in  (select storeItemRestriction.item_id "
				+ " 										from public.m_store_item_restriction storeItemRestriction "
				+ " 										where storeItemRestriction.store_id=:storeId and storeItemRestriction.is_deleted=false and storeItemRestriction.is_active=true and storeItemRestriction.organization_id=:organizationId)) ";

		Query query = getSession().createSQLQuery(sql).addScalar("id").addScalar("itemCode").addScalar("itemName")
				.addScalar("assetCategory").addScalar("genericName").addScalar("menufactureName").addScalar("productCategory").addScalar("minimumStock")
				.addScalar("currentStock", StandardBasicTypes.LONG).addScalar("stockInTransit", StandardBasicTypes.LONG)
				.addScalar("pendingIndentQuantity", StandardBasicTypes.LONG);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", request.getItemId() != null ? request.getItemId() : 0);
		query.setParameter("itemCode", request.getItemCode() != null ? request.getItemCode() + '%' : "");
		query.setParameter("itemName", request.getItemName() != null ? request.getItemName() + '%' : "");
		query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId", request.getAssetCategoryId() != null ? request.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				request.getProductCategoryId() != null ? request.getProductCategoryId() : 0);
		query.setParameter("vedId", request.getVedId() != null ? request.getVedId() : 0);
		query.setParameter("unitId", request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId());
		query.setParameter("organizationId",
				request.getOrganizationId() != null ? request.getOrganizationId() : sessionService.getOrganizationId());
		query.setParameter("unitId", request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ExpiredStoreStockResponse.class));
		query.setFirstResult(request.getOffset() != null ? request.getOffset() : 0);
		query.setMaxResults(request.getRecordPerPage() != null ? request.getRecordPerPage() : 50);
		return query.list();

	}

	@Override
	public List<BatchResponse1> getItemBatchDetails(Integer storeId, Integer itemId, Long batchId, Integer uomTypeId) {

		String hql1 = "  select batch0_.batch_no as batchNo ,sum (batch0_.quantity) as totalstock, batch0_.expiry_date as expiry, uomtype4_.type as uomType, uomunit5_.unit as uomUnit, \r\n"
				+ " batch0_.is_consignment as isConsignment ,\r\n"
				+ " batch0_.is_quarantine as quarantine , batch0_.selling_price as mrp  , r.name as rack , s.name as shelf , batch0_.lot_no as lotNo \r\n"
				+ " from inventory.t_batch batch0_  inner join public.m_item item1_ on batch0_.item_id=item1_.id   \r\n"
				+ " and (item1_.id =:itemId  and item1_.is_deleted=false and item1_.is_active=true)  \r\n"
				+ " inner join inventory.t_store_stock storestock2_ on batch0_.id=storestock2_.batch_id  and (storestock2_.store_id=:storeId and storestock2_.is_active=true \r\n"
				+ " and storestock2_.is_deleted=false and storestock2_.available_qty>0) inner join inventory.t_batch_uom_mapper batchuomli3_ \r\n"
				+ " on batch0_.id=batchuomli3_.batch_id and (batchuomli3_.uom_type_id=:id  and batchuomli3_.is_active=true and batchuomli3_.is_deleted=false) \r\n"
				+ " left join procurement.t_shelf_item  loc on batch0_.id = loc.batch_id and loc.is_active=true and loc.is_deleted=false left join public.m_rack  r \r\n"
				+ " on loc.rack_id = r.id left join  public.m_shelf s on loc.shelf_id = s.id, public.m_uom_type uomtype4_, public.m_uom_unit uomunit5_  \r\n"
				+ " where batchuomli3_.uom_type_id=uomtype4_.id and batchuomli3_.uom_unit_id=uomunit5_.id and batch0_.is_active=true and batch0_.is_deleted=false  and batch0_.id=:batchId \r\n"
				+ " group by batchNo,expiry,uomType,uomUnit,isConsignment,quarantine,mrp,rack,shelf,lotNo\r\n"
				+ " order by batch0_.is_consignment  ";

		Query query = getSession().createSQLQuery(hql1);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("batchId", batchId);
		query.setParameter("id", uomTypeId);

		query.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.items.BatchResponse1.class));

		return query.list();

	}

	@Override
	public List<BatchResponse1> getExpiredBatchDetails(int storeId, int itemId, boolean showDetails, Integer unitId) {

		StringBuffer sb = new StringBuffer();
		String hql1 = "  select batch0_.batch_no as batchNo ,sum (batch0_.quantity) as totalstock, batch0_.expiry_date as expiry, uomtype4_.type as uomType, uomunit5_.unit as uomUnit, \r\n"
				+ " batch0_.is_consignment as isConsignment ,\r\n"
				+ " batch0_.is_quarantine as quarantine , batch0_.selling_price as mrp  , r.name as rack , s.name as shelf , batch0_.lot_no as lotNo ,  batch0_ .actual_lending_price  as actualCost \r\n"
				+ "  , sum (storestock2_.available_qty) as availablestockquantity        from inventory.t_batch batch0_  inner join public.m_item item1_ on batch0_.item_id=item1_.id   \r\n"
				+ " and (item1_.id =:itemId  and item1_.is_deleted=false and item1_.is_active=true)  \r\n"
				+ " inner join inventory.t_store_stock storestock2_ on batch0_.id=storestock2_.batch_id  and (storestock2_.store_id=:storeId and storestock2_.is_active=true \r\n"
				+ " and storestock2_.is_deleted=false and storestock2_.available_qty>0) left join inventory.t_batch_uom_mapper batchuomli3_ \r\n"
				+ " on batch0_.id=batchuomli3_.batch_id and (batchuomli3_.uom_type_id=:id and batchuomli3_.is_active=true and batchuomli3_.is_deleted=false) \r\n"
				+ " left join procurement.t_shelf_item  loc on batch0_.id = loc.batch_id and loc.is_active=true and loc.is_deleted=false left join public.m_rack  r \r\n"
				+ " on loc.rack_id = r.id left join  public.m_shelf s on loc.shelf_id = s.id  left join  public.m_uom_type uomtype4_ on  batchuomli3_.uom_type_id=uomtype4_.id  left join  public.m_uom_unit uomunit5_   on   batchuomli3_.uom_unit_id=uomunit5_.id  \r\n"
				+ " where batch0_.expiry_date < current_date    and batch0_.is_active=true and batch0_.is_deleted=false and   batch0_.unit_id = :unitId ";

		sb.append(hql1);
		//sb.append(!showDetails ? " and batch0_.batch_no is not null" : "");
		sb.append(" and (:showDetails = false OR batch0_.batch_no is not null) ");
		sb.append(" group by batchNo,expiry,uomType,uomUnit,isConsignment,quarantine,mrp,rack,shelf,lotNo , batch0_ .actual_lending_price \r\n"
				+ " order by batch0_.expiry_date ");

		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("id", Constants.uomTypeId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("showDetails", showDetails);

		query.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.items.BatchResponse1.class));

		return query.list();

	}

	@Override
	public BatchResponse1 getUpdatedBatchDetails(int storeId, BigInteger batchId, Integer unitId) {

		String hql1 = "  select details.new_mrp as mrp , details.new_batch_no as batchno  , details.new_batch_expiry as expiry "
				+ " from  inventory.t_batch_exp_detail details "
				+ " join inventory.t_batch_exp batchexp on details.batch_expiry_id = batchexp.id "
				+ " where batchexp.store_id =:storeId and details.batch_id =:batchId and batchexp.unit_id =:unitId ";

		Query query = getSession().createSQLQuery(hql1);
		query.setParameter("storeId", storeId);
		query.setParameter("batchId", batchId.intValue());
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		query.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.items.BatchResponse1.class));

		if (query.list().size() > 0) {
			return (BatchResponse1) query.list().get(0);
		} else {
			return null;
		}

	}

	@Override
	public List<StoreStockResponse> getStoreIndentDateWiseZeroStockList(StoreStockRequestDate request) {

		String isConsignmentCondation = request.getIsConsignment() != null
				? " and item.is_consignment=" + request.getIsConsignment() + " "
				: " ";
		
		String searchItem = request.getSearch()!= null ? " and ( (upper(item.code) like '%"+request.getSearch().toUpperCase()+"%') or (upper(item.name) like '%"+request.getSearch().toUpperCase()+"%') ) ":"" ;


		String sql = "select distinct item.id as id, item.code as itemCode, item.name as itemName, assetCategory.category as assetCategory, generic.name as genericName , m.name as menufactureName , "
				+ " 	productCategory.category as productCategory, coalesce(storeItemMapper.min_qty, 0) as minimumStock, "
				+ " 	 coalesce(stock.totalQty, 0) as currentStock,   coalesce(stock.totalTransitQty, 0)  as stockInTransit, "
				+ " 	coalesce(storeIndent.totalPendingQty, 0) as pendingIndentQuantity "
				+ " from public.m_item item "
				+ " 	inner join public.m_generic generic on item.generic_id=generic.id and (generic.is_deleted=false and generic.is_active=true) "
				+ " 	inner join public.m_product_category productCategory on generic.product_category_id=productCategory.id and (productCategory.is_deleted=false and productCategory.is_active=true) "
				+ " 	inner join public.m_asset_category assetCategory on productCategory.asset_category_id=assetCategory.id and (assetCategory.is_deleted=false and assetCategory.is_active=true) "
				+ " 	inner join public.m_asset_type assetType on assetCategory.asset_type_id=assetType.id and (assetType.is_deleted=false and assetType.is_active=true) "
				+ " 	inner join  public.m_store_item_mapper storeItemMapper on item.id=storeItemMapper.item_id and (storeItemMapper.store_id=:storeId and storeItemMapper.is_active=true and storeItemMapper.is_deleted=false) "
				+ " 	LEFT JOIN (SELECT item.id itemId, sum(stock.available_qty) totalQty, SUM(stock.t_qty) totalTransitQty "
				+ " 				FROM public.m_item item "
				+ " 					LEFT JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true and batch.unit_id=:unitId and (batch.expiry_date >= current_date  or batch.expiry_date is null)  ) "
				+ " 					LEFT JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and stock.store_id = :storeId and (stock.is_deleted=false and stock.is_active=true)  and stock.unit_id=:unitId "
				+ " 				GROUP BY item.id) stock ON stock.itemId = item.id	"
				+ "		LEFT JOIN (SELECT storeIndentDetail.item_id AS itemId, sum(storeIndentDetail.pending_quantity) as totalPendingQty"
				+ "					FROM inventory.t_store_indent storeIndent "
				+ "						INNER JOIN inventory.t_store_indent_detail storeIndentDetail ON storeIndentDetail.store_indent_id = storeIndent.id "
				+ "							AND (storeIndentDetail.is_active=true AND storeIndentDetail.is_deleted=false AND storeIndentDetail.pending_quantity >= 0) "
				+ "					WHERE storeIndent.from_store_id = :storeId AND storeIndent.is_active=true AND storeIndent.is_deleted=false  and storeIndent.unit_id=:unitId  "
				+ "					GROUP BY storeIndentDetail.item_id)	AS storeIndent ON storeIndent.itemId = item.id "
				+ " 	Left join public.m_manufacturer m on item.manufacturer_id=m.id and (m.is_deleted=false and m.is_active=true) "
				+ " where item.is_active=true and item.is_deleted=false  " + isConsignmentCondation + searchItem
				+ " and  ( stock.totalQty <1 or stock.totalQty is null ) "

				+ " 	and (:itemId=0 or item.id=:itemId) "
				+ " 	and (:itemId<>0 and :itemCode<>'' or :itemCode='' or upper(item.code) like upper(:itemCode)) "
				+ " 	and (:itemId<>0 and :itemName<>'' or :itemName='' or upper(item.name) like upper(:itemName)) "
				+ " 	and (:assetTypeId=0 or assetType.id=:assetTypeId) "
				+ " 	and (:assetCategoryId=0 or assetCategory.id=:assetCategoryId) "
				+ " 	and (:productCategoryId=0 or productCategory.id=:productCategoryId) "
				// + " and (item.added_date <=:added_date) "

				+ " 	and (:storeId=0 or :vedId=0 or generic.id in (select generic.id "
				+ " 										from public.m_storewise_generic storewisGeneric "
				+ " 											inner join public.m_generic generic on storewisGeneric.generic_id=generic.id "
				+ " 												and (generic.is_active=true and generic.is_deleted=false) "
				+ " 											inner join public.m_ved ved on storewisGeneric.ved_id=ved.id "
				+ " 												and (ved.id=:vedId and ved.is_active=true and ved.is_deleted=false) "
				+ " 											inner join public.m_store store on storewisGeneric.store_id=store.store_id "
				+ " 												and (store.store_id=:storeId and store.is_active=true and store.is_deleted=false))) "
				+ " 	and (:storeId=0 or (productCategory.id in (select storePcMapper.product_category_id "
				+ " 									from public.m_store_pc_mapper storePcMapper "
				+ " 									where storePcMapper.store_id=:storeId)) "
				+ " 				and (item.id not in  (select storeItemRestriction.item_id "
				+ " 										from public.m_store_item_restriction storeItemRestriction "
				+ " 										where storeItemRestriction.store_id=:storeId and storeItemRestriction.is_deleted=false and storeItemRestriction.is_active=true and storeItemRestriction.organization_id=:organizationId))) "
				+ " union"
				+ " select distinct item.id as id, item.code as itemCode, item.name as itemName, assetCategory.category as assetCategory,  '-' as genericName , m.name as menufactureName ,  "
				+ " 	productCategory.category as productCategory, coalesce(storeItemMapper.min_qty, 0) as minimumStock, "
				+ " 	coalesce(stock.totalQty, 0) as currentStock, coalesce(stock.totalTransitQty, 0) as stockInTransit, "
				+ " 	coalesce(storeIndent.totalPendingQty, 0) as pendingIndentQuantity "
				+ " from public.m_item item "
				+ " 	inner join public.m_product_category productCategory on item.product_category_id=productCategory.id and (productCategory.is_deleted=false and productCategory.is_active=true) "
				+ " 	inner join public.m_asset_category assetCategory on productCategory.asset_category_id=assetCategory.id and (assetCategory.is_deleted=false and assetCategory.is_active=true) "
				+ " 	inner join public.m_asset_type assetType on assetCategory.asset_type_id=assetType.id and (assetType.is_deleted=false and assetType.is_active=true) "
				+ " 	inner  join public.m_store_item_mapper storeItemMapper on item.id=storeItemMapper.item_id and (storeItemMapper.store_id=:storeId and storeItemMapper.is_active=true and storeItemMapper.is_deleted=false) "
				+ " 	LEFT JOIN (SELECT item.id itemId, sum(stock.available_qty) totalQty, SUM(stock.t_qty) totalTransitQty "
				+ " 				FROM public.m_item item "
				+ " 					LEFT JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true and batch.unit_id=:unitId      ) "
				+ " 					LEFT JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and stock.store_id = :storeId and (stock.is_deleted=false and stock.is_active=true) and stock.unit_id=:unitId "
				+ " 				GROUP BY item.id) stock ON stock.itemId = item.id	"
				+ "		LEFT JOIN (SELECT storeIndentDetail.item_id AS itemId, sum(storeIndentDetail.pending_quantity) as totalPendingQty"
				+ "					FROM inventory.t_store_indent storeIndent "
				+ "						INNER JOIN inventory.t_store_indent_detail storeIndentDetail ON storeIndentDetail.store_indent_id = storeIndent.id "
				+ "							AND (storeIndentDetail.is_active=true AND storeIndentDetail.is_deleted=false AND storeIndentDetail.pending_quantity >= 0) "
				+ "					WHERE storeIndent.from_store_id = :storeId AND storeIndent.is_active=true AND storeIndent.is_deleted=false and storeIndent.unit_id=:unitId "
				+ "					GROUP BY storeIndentDetail.item_id)	AS storeIndent ON storeIndent.itemId = item.id "
				   + " 	Left join public.m_manufacturer m on item.manufacturer_id=m.id and (m.is_deleted=false and m.is_active=true) "
				+ " where item.is_active=true and item.is_deleted=false " + isConsignmentCondation + searchItem
				+ " and ( stock.totalQty <1 or stock.totalQty is null) "

				+ " 	and (:itemId=0 or item.id=:itemId) "
				+ " 	and (:itemId<>0 and :itemCode<>'' or :itemCode='' or upper(item.code) like upper(:itemCode)) "
				+ " 	and (:itemId<>0 and :itemName<>'' or :itemName='' or upper(item.name) like upper(:itemName)) "
				+ " 	and (:assetTypeId=0 or assetType.id=:assetTypeId) "
				+ " 	and (:assetCategoryId=0 or assetCategory.id=:assetCategoryId) "
				+ " 	and (:productCategoryId=0 or productCategory.id=:productCategoryId) "
				+ " 				and (item.id not in  (select storeItemRestriction.item_id "
				+ " 										from public.m_store_item_restriction storeItemRestriction "
				+ " 										where storeItemRestriction.store_id=:storeId and storeItemRestriction.is_deleted=false and storeItemRestriction.is_active=true and storeItemRestriction.organization_id=:organizationId)) ";

		Query query = getSession().createSQLQuery(sql).addScalar("id").addScalar("itemCode").addScalar("itemName")
				.addScalar("assetCategory").addScalar("genericName").addScalar("menufactureName").addScalar("productCategory").addScalar("minimumStock")
				.addScalar("currentStock", StandardBasicTypes.LONG).addScalar("stockInTransit", StandardBasicTypes.LONG)
				.addScalar("pendingIndentQuantity", StandardBasicTypes.LONG);
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("itemId", request.getItemId() != null ? request.getItemId() : 0);
		query.setParameter("itemCode", request.getItemCode() != null ? request.getItemCode() + '%' : "");
		query.setParameter("itemName", request.getItemName() != null ? request.getItemName() + '%' : "");
		query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId", request.getAssetCategoryId() != null ? request.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				request.getProductCategoryId() != null ? request.getProductCategoryId() : 0);
		query.setParameter("vedId", request.getVedId() != null ? request.getVedId() : 0);
		//query.setParameter("unitId", sessionService.getUnitId());
		query.setParameter("organizationId",
				request.getOrganizationId() != null ? request.getOrganizationId() : sessionService.getOrganizationId());
		query.setParameter("unitId", request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(StoreStockResponse.class));
		query.setFirstResult(request.getOffset() != null ? request.getOffset() : 0);
		query.setMaxResults(request.getRecordPerPage() != null ? request.getRecordPerPage() : 50);
		return query.list();

	}

	@Override
	public List<BatchResponse1> getZeroStockBatchDetails(int storeId, int itemId, boolean showDetails) {

		StringBuffer sb = new StringBuffer();
		String hql1 = "  select   batch0_.id   as  batchid , batch0_.batch_no as batchNo ,sum (batch0_.quantity) as totalstock, batch0_.expiry_date as expiry, uomtype4_.type as uomType, uomunit5_.unit as uomUnit, \r\n"
				+ " batch0_.is_consignment as isConsignment ,\r\n"
				+ " batch0_.is_quarantine as quarantine , batch0_.selling_price as mrp  , r.name as rack , s.name as shelf , batch0_.lot_no as lotNo ,  batch0_ .actual_lending_price  as actualCost \r\n"
				+ " from inventory.t_batch batch0_  inner join public.m_item item1_ on batch0_.item_id=item1_.id   \r\n"
				+ " and (item1_.id =:itemId  and item1_.is_deleted=false and item1_.is_active=true)  \r\n"
				+ " inner join inventory.t_store_stock storestock2_ on batch0_.id=storestock2_.batch_id  and (storestock2_.store_id=:storeId and storestock2_.is_active=true \r\n"
				+ " and storestock2_.is_deleted=false and storestock2_.available_qty < 1) left join inventory.t_batch_uom_mapper batchuomli3_ \r\n"
				+ " on batch0_.id=batchuomli3_.batch_id and (batchuomli3_.uom_type_id=:id and batchuomli3_.is_active=true and batchuomli3_.is_deleted=false) \r\n"
				+ " left join procurement.t_shelf_item  loc on batch0_.id = loc.batch_id and loc.is_active=true and loc.is_deleted=false left join public.m_rack  r \r\n"
				+ " on loc.rack_id = r.id left join  public.m_shelf s on loc.shelf_id = s.id  left join  public.m_uom_type uomtype4_ on  batchuomli3_.uom_type_id=uomtype4_.id  left join  public.m_uom_unit uomunit5_   on   batchuomli3_.uom_unit_id=uomunit5_.id  \r\n"
				+ " where batch0_.is_active=true and batch0_.is_deleted=false and   batch0_.unit_id = :unitId ";

		sb.append(hql1);
		//sb.append(!showDetails ? "and ( batch0_.batch_no is not null or  batch0_.batch_no is null )  " : ""); // HIS-10524
		sb.append(" and (:showDetails = true OR (batch0_.batch_no is not null OR batch0_.batch_no is null)) ");
		sb.append(
				" group by batchNo,expiry,uomType,uomUnit,isConsignment,quarantine,mrp,rack,shelf,lotNo , batchid \r\n"
						+ " order by batch0_.expiry_date  ");

		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("id", Constants.uomTypeId);
		query.setParameter("unitId", sessionService.getUnitId());
		query.setParameter("showDetails", showDetails);

		query.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.items.BatchResponse1.class));

		return query.list();

	}

	@Override
	public List<StoreStockResponse> getStoreIndentDateWiseZeroWikthStockList(StoreStockRequestDate request) {

		String isConsignmentCondation = request.getIsConsignment() != null
				? " and item.is_consignment=" + request.getIsConsignment() + " "
				: " ";
		
		String searchItem = request.getSearch()!= null ? " and ( (upper(item.code) like '%"+request.getSearch().toUpperCase()+"%') or (upper(item.name) like '%"+request.getSearch().toUpperCase()+"%') ) ":"" ;

		String sql = "select distinct item.id as id, item.code as itemCode, item.name as itemName, item.is_narcotic as isNarcotic, assetCategory.category as assetCategory , generic.id as genericId, generic.name as genericName , m.name as menufactureName ,  "
				+ " 	productCategory.category as productCategory, coalesce(storeItemMapper.min_qty, 0) as minimumStock, "
				+ " 	 coalesce(stock.totalQty, 0) as currentStock,   coalesce(stockTra.totalTransitQty, 0)  as stockInTransit, "
				+ " 	coalesce(storeIndent.totalPendingQty, 0) as pendingIndentQuantity "
				+ " from public.m_item item "
				+ " 	Left join public.m_generic generic on item.generic_id=generic.id and (generic.is_deleted=false and generic.is_active=true) "
				
				+ " 	inner join public.m_product_category productCategory on generic.product_category_id=productCategory.id and (productCategory.is_deleted=false and productCategory.is_active=true) "
				+ " 	inner join public.m_asset_category assetCategory on productCategory.asset_category_id=assetCategory.id and (assetCategory.is_deleted=false and assetCategory.is_active=true) "
				+ " 	inner join public.m_asset_type assetType on assetCategory.asset_type_id=assetType.id and (assetType.is_deleted=false and assetType.is_active=true) "
				+ " 	inner join  public.m_store_item_mapper storeItemMapper on item.id=storeItemMapper.item_id and (storeItemMapper.store_id=:storeId and storeItemMapper.is_active=true and storeItemMapper.is_deleted=false) "
				+ " 	LEFT JOIN (SELECT item.id itemId, sum(stock.available_qty) totalQty  "
				+ " 				FROM public.m_item item "
				+ " 					LEFT JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true and batch.unit_id=:unitId and batch.is_quarantine=false and (batch.expiry_date >= current_date  or batch.expiry_date is null)  ) "
				+ " 					LEFT JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and stock.store_id = :storeId "
				+ "  and (stock.is_deleted=false and stock.is_active=true)  and stock.unit_id=:unitId and stock.total_qty is not null and stock.available_qty is not null "
				+ " 				GROUP BY item.id) stock ON stock.itemId = item.id	"
				+ " left join ( select item.id itemId, SUM(stock.out_qty) totalTransitQty from public.m_item item "  + 
				"	left join inventory.t_batch batch on "  + 
				"		batch.item_id = item.id "  + 
				"		and (batch.is_deleted = false and batch.is_active = true and batch.unit_id =:unitId)  "  + 
				"	left join inventory.t_stock_transaction stock  on stock.batch_id = batch.id "  + 
				"		and stock.store_id = :storeId and stock.unit_id =:unitId and stock.out_qty is not null and stock.is_transit = true "  + 
				"	group by item.id) stockTra on stockTra.itemId = item.id"
				+ "		LEFT JOIN (SELECT storeIndentDetail.item_id AS itemId, sum(storeIndentDetail.pending_quantity) as totalPendingQty"
				+ "					FROM inventory.t_store_indent storeIndent "
				+ "						INNER JOIN inventory.t_store_indent_detail storeIndentDetail ON storeIndentDetail.store_indent_id = storeIndent.id "
				+ "							AND (storeIndentDetail.is_active=true AND storeIndentDetail.is_deleted=false AND storeIndentDetail.pending_quantity >= 0) "
				+ "					WHERE storeIndent.from_store_id = :storeId AND storeIndent.is_active=true AND storeIndent.is_deleted=false  "
				+ " and storeIndent.unit_id=:unitId  AND storeIndent.status_id=2 "
				+ "					GROUP BY storeIndentDetail.item_id)	AS storeIndent ON storeIndent.itemId = item.id "
				+ " 	Left join public.m_manufacturer m on item.manufacturer_id=m.id and (m.is_deleted=false and m.is_active=true) "
				+ " where item.is_active=true and item.is_deleted=false  " + isConsignmentCondation + searchItem
				+ " and  ( stock.totalQty <1 or stock.totalQty >=1 or stock.totalQty is null ) "

				+ " 	and (:itemId=0 or item.id=:itemId) "
				+ " 	and (:itemId<>0 and :itemCode<>'' or :itemCode='' or upper(item.code) like upper(:itemCode)) "
				+ " 	and (:itemId<>0 and :itemName<>'' or :itemName='' or upper(item.name) like upper(:itemName)) "
				+ " 	and (:assetTypeId=0 or assetType.id=:assetTypeId) "
				+ " 	and (:assetCategoryId=0 or assetCategory.id=:assetCategoryId) "
				+ " 	and (:productCategoryId=0 or productCategory.id=:productCategoryId) "
				+ " 	and (:storeId=0 or :vedId=0 or generic.id in (select generic.id "
				+ " 										from public.m_storewise_generic storewisGeneric "
				+ " 											inner join public.m_generic generic on storewisGeneric.generic_id=generic.id "
				+ " 												and (generic.is_active=true and generic.is_deleted=false) "
				+ " 											inner join public.m_ved ved on storewisGeneric.ved_id=ved.id "
				+ " 												and (ved.id=:vedId and ved.is_active=true and ved.is_deleted=false) "
				+ " 											inner join public.m_store store on storewisGeneric.store_id=store.store_id "
				+ " 												and (store.store_id=:storeId and store.is_active=true and store.is_deleted=false))) "
				+ " 	and (:storeId=0 or (productCategory.id in (select storePcMapper.product_category_id "
				+ " 									from public.m_store_pc_mapper storePcMapper "
				+ " 									where storePcMapper.store_id=:storeId)) "
				+ " 				and (item.id not in  (select storeItemRestriction.item_id "
				+ " 										from public.m_store_item_restriction storeItemRestriction "
				+ " 										where storeItemRestriction.store_id=:storeId and storeItemRestriction.is_deleted=false and storeItemRestriction.is_active=true and storeItemRestriction.organization_id=:organizationId))) "
				
				+ " union"

				+ " select distinct item.id as id, item.code as itemCode, item.name as itemName,item.is_narcotic as isNarcotic, assetCategory.category as assetCategory, 0 as genericId,  '-' as genericName , m.name as menufactureName ,  "
				+ " 	productCategory.category as productCategory, coalesce(storeItemMapper.min_qty, 0) as minimumStock, "
				+ " 	coalesce(stock.totalQty, 0) as currentStock, coalesce(stockTra.totalTransitQty, 0) as stockInTransit, "
				+ " 	coalesce(storeIndent.totalPendingQty, 0) as pendingIndentQuantity "
				+ " from public.m_item item "
				+ " 	inner join public.m_product_category productCategory on item.product_category_id=productCategory.id and (productCategory.is_deleted=false and productCategory.is_active=true) "
				+ " 	inner join public.m_asset_category assetCategory on productCategory.asset_category_id=assetCategory.id and (assetCategory.is_deleted=false and assetCategory.is_active=true) "
				+ " 	inner join public.m_asset_type assetType on assetCategory.asset_type_id=assetType.id and (assetType.is_deleted=false and assetType.is_active=true) "
				+ " 	inner  join public.m_store_item_mapper storeItemMapper on item.id=storeItemMapper.item_id and (storeItemMapper.store_id=:storeId and storeItemMapper.is_active=true and storeItemMapper.is_deleted=false) "
				+ " 	LEFT JOIN (SELECT item.id itemId, sum(stock.available_qty) totalQty  "
				+ " 				FROM public.m_item item "
				+ " 					LEFT JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true and batch.unit_id=:unitId  and batch.is_quarantine=false    ) "
				+ " 					LEFT JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and stock.store_id = :storeId and (stock.is_deleted=false and stock.is_active=true) and stock.unit_id=:unitId "
				+ " 				GROUP BY item.id) stock ON stock.itemId = item.id	"
				+ " left join ( "  + 
				"	select "  + 
				"	item.id itemId, "  + 
				"		SUM(stock.out_qty) totalTransitQty "  + 
				"	from "  + 
				"		public.m_item item "  + 
				"	left join inventory.t_batch batch on "  + 
				"		batch.item_id = item.id "  + 
				"		and (batch.is_deleted = false "  + 
				"			and batch.is_active = true "  + 
				"			and batch.is_quarantine = false "  + 
				"			and batch.unit_id =:unitId)  "  + 
				"	left join inventory.t_stock_transaction stock  on "  + 
				"		stock.batch_id = batch.id "  + 
				"		and stock.store_id = :storeId "  + 
				"		and stock.unit_id =:unitId "  + 
				"		and stock.out_qty is not null "  + 
				"		and stock.is_transit = true "  + 
				"	group by "  + 
				"		item.id) stockTra on "  + 
				"	stockTra.itemId = item.id"
				+ "		LEFT JOIN (SELECT storeIndentDetail.item_id AS itemId, sum(storeIndentDetail.pending_quantity) as totalPendingQty"
				+ "					FROM assets.t_asset_indent storeIndent "
				+ "						INNER JOIN assets.t_asset_indent_detail storeIndentDetail ON storeIndentDetail.indent_id = storeIndent.id "
				+ "							AND (storeIndentDetail.is_active=true AND storeIndentDetail.is_deleted=false AND storeIndentDetail.pending_quantity >= 0) "
				+ "					WHERE storeIndent.from_store_id = :storeId AND storeIndent.is_active=true AND storeIndent.is_deleted=false"
				+ "  and storeIndent.unit_id=:unitId AND storeIndent.status_id=2 "
				+ "					GROUP BY storeIndentDetail.item_id)	AS storeIndent ON storeIndent.itemId = item.id "
				+ " 	Left join public.m_manufacturer m on item.manufacturer_id=m.id and (m.is_deleted=false and m.is_active=true) "
				+ " where item.is_active=true and item.is_deleted=false " + isConsignmentCondation + searchItem
				+ " and ( stock.totalQty <1 or stock.totalQty >=1 or stock.totalQty is null) "

				+ " 	and (:itemId=0 or item.id=:itemId) "
				+ " 	and (:itemId<>0 and :itemCode<>'' or :itemCode='' or upper(item.code) like upper(:itemCode)) "
				+ " 	and (:itemId<>0 and :itemName<>'' or :itemName='' or upper(item.name) like upper(:itemName)) "
				+ " 	and (:assetTypeId=0 or assetType.id=:assetTypeId) "
				+ " 	and (:assetCategoryId=0 or assetCategory.id=:assetCategoryId) "
				+ " 	and (:productCategoryId=0 or productCategory.id=:productCategoryId) "
				+ "     and item.generic_id is null "
				+ " 				and (item.id not in  (select storeItemRestriction.item_id "
				+ " 										from public.m_store_item_restriction storeItemRestriction "
				+ " 										where storeItemRestriction.store_id=:storeId and storeItemRestriction.is_deleted=false and storeItemRestriction.is_active=true and storeItemRestriction.organization_id=:organizationId)) ";

		Query query = getSession().createSQLQuery(sql).addScalar("id").addScalar("itemCode").addScalar("itemName").addScalar("isNarcotic").addScalar("genericId")
				.addScalar("assetCategory").addScalar("genericName").addScalar("menufactureName").addScalar("productCategory").addScalar("minimumStock")
				.addScalar("currentStock", StandardBasicTypes.LONG).addScalar("stockInTransit", StandardBasicTypes.LONG)
				.addScalar("pendingIndentQuantity", StandardBasicTypes.LONG);
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("itemId", request.getItemId() != null ? request.getItemId() : 0);
		query.setParameter("itemCode", request.getItemCode() != null ? request.getItemCode() + '%' : "");
		query.setParameter("itemName", request.getItemName() != null ? request.getItemName() + '%' : "");
		query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId", request.getAssetCategoryId() != null ? request.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				request.getProductCategoryId() != null ? request.getProductCategoryId() : 0);
		query.setParameter("vedId", request.getVedId() != null ? request.getVedId() : 0);
		// query.setParameter("added_date", request.getFromDate());
		query.setParameter("organizationId",
				request.getOrganizationId() != null ? request.getOrganizationId() : sessionService.getOrganizationId());
		query.setParameter("unitId", request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(StoreStockResponse.class));
		query.setFirstResult(request.getOffset() != null ? request.getOffset() : 0);
		query.setMaxResults(request.getRecordPerPage() != null ? request.getRecordPerPage() : 50);
		return query.list();

	}

	@Override
	public List<ExpiredStoreStockResponse> nextExpiredStoreStockDashboard(ExpiredStoreStockRequest request) {

		Integer storeId = 0;
		for (MultipleStoreRequest s : request.getMultipleStore()) {
			storeId = s.getId();
		}

		Calendar cal = Calendar.getInstance();
		if (request.getFromDate() != null) {
			cal.setTime(request.getFromDate());
			cal.set(request.getFromDate().getYear() + 1900, request.getFromDate().getMonth(), 1);
			request.setFromDate(cal.getTime());
		} else {
			Date today = cal.getTime();
			Date preYear = cal.getTime();
		}

		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(request.getToDate());
		cal1.set(request.getToDate().getYear() + 1900, request.getToDate().getMonth() + request.getMonth(),
				cal1.getActualMaximum(Calendar.DATE));

		request.setToDate(cal1.getTime());
		SimpleDateFormat simpleDateFormat1 = null;
		Date todate = null;
		SimpleDateFormat simpleDateFormat2 = null;
		Date fromdate = null;
		try {

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy"); // Existing Pattern
			todate = simpleDateFormat.parse(cal1.getTime().toString()); // Returns Date Format,
			fromdate = simpleDateFormat.parse(cal.getTime().toString()); // Returns Date Format,
			simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd"); // New Pattern

		} catch (ParseException e) {
			logger.error("Error while parsing next expired store stock dashboard date range", e);
			throw new ServiceException(e.getMessage());
		}
		String dd1 = simpleDateFormat1.format(fromdate);
		String dd2 = simpleDateFormat1.format(todate);

		String isConsignmentCondation = request.getIsConsignment() != null
				? " and item.is_consignment=" + request.getIsConsignment() + " "
				: " ";

		String searchItem = request.getSearch()!= null ? " and ( (upper(item.code) like '%"+request.getSearch().toUpperCase()+"%') or (upper(item.name) like '%"+request.getSearch().toUpperCase()+"%') ) ":"" ;


		String sql = "select distinct item.id as id, item.code as itemCode, item.name as itemName, assetCategory.category as assetCategory,  generic.name as genericName , m.name as menufactureName , "
				+ " 	productCategory.category as productCategory, coalesce(storeItemMapper.min_qty, 0) as minimumStock, "
				+ " 	 coalesce(stock.totalQty, 0) as currentStock,   coalesce(stock.totalTransitQty, 0)  as stockInTransit, "
				+ " 	coalesce(storeIndent.totalPendingQty, 0) as pendingIndentQuantity "
				+ " from public.m_item item "
				+ " 	inner join public.m_generic generic on item.generic_id=generic.id and (generic.is_deleted=false and generic.is_active=true) "
				+ " 	inner join public.m_product_category productCategory on generic.product_category_id=productCategory.id and (productCategory.is_deleted=false and productCategory.is_active=true) "
				+ " 	inner join public.m_asset_category assetCategory on productCategory.asset_category_id=assetCategory.id and (assetCategory.is_deleted=false and assetCategory.is_active=true) "
				+ " 	inner join public.m_asset_type assetType on assetCategory.asset_type_id=assetType.id and (assetType.is_deleted=false and assetType.is_active=true) "
				+ " 	inner join  public.m_store_item_mapper storeItemMapper on item.id=storeItemMapper.item_id and (storeItemMapper.store_id=:storeId and storeItemMapper.is_active=true and storeItemMapper.is_deleted=false) "
				+ " 	LEFT JOIN (SELECT item.id itemId, sum(stock.available_qty) totalQty, SUM(stock.t_qty) totalTransitQty "
				+ " 				FROM public.m_item item "
				+ " 					INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true and batch.unit_id=:unitId) "
				+ "      AND batch.expiry_date  BETWEEN to_date( '" + dd1.trim() + "','YYYY-MM-DD') AND to_date( '"
				+ dd2.trim() + "','YYYY-MM-DD')   "
				+ " 					INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and stock.store_id = :storeId and (stock.is_deleted=false and stock.is_active=true)  and stock.unit_id=:unitId "
				+ " 				GROUP BY item.id) stock ON stock.itemId = item.id	"
				+ "		LEFT JOIN (SELECT storeIndentDetail.item_id AS itemId, sum(storeIndentDetail.pending_quantity) as totalPendingQty"
				+ "					FROM inventory.t_store_indent storeIndent "
				+ "						INNER JOIN inventory.t_store_indent_detail storeIndentDetail ON storeIndentDetail.store_indent_id = storeIndent.id "
				+ "							AND (storeIndentDetail.is_active=true AND storeIndentDetail.is_deleted=false AND storeIndentDetail.pending_quantity > 0) "
				+ "					WHERE storeIndent.from_store_id = :storeId AND storeIndent.is_active=true AND storeIndent.is_deleted=false  and storeIndent.unit_id=:unitId  "
				+ "					GROUP BY storeIndentDetail.item_id)	AS storeIndent ON storeIndent.itemId = item.id "
				   + " 	Left join public.m_manufacturer m on item.manufacturer_id=m.id and (m.is_deleted=false and m.is_active=true) "
				+ " where item.is_active=true and item.is_deleted=false  " + isConsignmentCondation + searchItem
				+ " and stock.totalQty >0 "
				+ " 	and (:itemId=0 or item.id=:itemId) "
				+ " 	and (:itemId<>0 and :itemCode<>'' or :itemCode='' or upper(item.code) like upper(:itemCode)) "
				+ " 	and (:itemId<>0 and :itemName<>'' or :itemName='' or upper(item.name) like upper(:itemName)) "
				+ " 	and (:assetTypeId=0 or assetType.id=:assetTypeId) "
				+ " 	and (:assetCategoryId=0 or assetCategory.id=:assetCategoryId) "
				+ " 	and (:productCategoryId=0 or productCategory.id=:productCategoryId) "
				+ " 	and (:storeId=0 or :vedId=0 or generic.id in (select generic.id "
				+ " 										from public.m_storewise_generic storewisGeneric "
				+ " 											inner join public.m_generic generic on storewisGeneric.generic_id=generic.id "
				+ " 												and (generic.is_active=true and generic.is_deleted=false) "
				+ " 											inner join public.m_ved ved on storewisGeneric.ved_id=ved.id "
				+ " 												and (ved.id=:vedId and ved.is_active=true and ved.is_deleted=false) "
				+ " 											inner join public.m_store store on storewisGeneric.store_id=store.store_id "
				+ " 												and (store.store_id=:storeId and store.is_active=true and store.is_deleted=false))) "
				+ " 	and (:storeId=0 or (productCategory.id in (select storePcMapper.product_category_id "
				+ " 									from public.m_store_pc_mapper storePcMapper "
				+ " 									where storePcMapper.store_id=:storeId)) "
				+ " 				and (item.id not in  (select storeItemRestriction.item_id "
				+ " 										from public.m_store_item_restriction storeItemRestriction "
				+ " 										where storeItemRestriction.store_id=:storeId and storeItemRestriction.is_deleted=false and storeItemRestriction.is_active=true and storeItemRestriction.organization_id=:organizationId))) "
				+ " union"

				+ " select distinct item.id as id, item.code as itemCode, item.name as itemName, assetCategory.category as assetCategory, '-' as genericName , m.name as menufactureName ,   "
				+ " 	productCategory.category as productCategory, coalesce(storeItemMapper.min_qty, 0) as minimumStock, "
				+ " 	coalesce(stock.totalQty, 0) as currentStock, coalesce(stock.totalTransitQty, 0) as stockInTransit, "
				+ " 	coalesce(storeIndent.totalPendingQty, 0) as pendingIndentQuantity "
				+ " from public.m_item item "
				+ " 	inner join public.m_product_category productCategory on item.product_category_id=productCategory.id and (productCategory.is_deleted=false and productCategory.is_active=true) "
				+ " 	inner join public.m_asset_category assetCategory on productCategory.asset_category_id=assetCategory.id and (assetCategory.is_deleted=false and assetCategory.is_active=true) "
				+ " 	inner join public.m_asset_type assetType on assetCategory.asset_type_id=assetType.id and (assetType.is_deleted=false and assetType.is_active=true) "
				+ " 	inner  join public.m_store_item_mapper storeItemMapper on item.id=storeItemMapper.item_id and (storeItemMapper.store_id=:storeId and storeItemMapper.is_active=true and storeItemMapper.is_deleted=false) "
				+ " 	LEFT JOIN (SELECT item.id itemId, sum(stock.available_qty) totalQty, SUM(stock.t_qty) totalTransitQty "
				+ " 				FROM public.m_item item "
				+ " 					INNER JOIN inventory.t_batch batch ON batch.item_id = item.id and (batch.is_deleted=false and batch.is_active=true and batch.unit_id=:unitId) "
				+ "      AND batch.expiry_date  BETWEEN to_date( '" + dd1.trim() + "','YYYY-MM-DD') AND to_date( '"
				+ dd2.trim() + "','YYYY-MM-DD')  "
				+ " 					INNER JOIN inventory.t_store_stock stock ON stock.batch_id = batch.id and stock.store_id = :storeId and (stock.is_deleted=false and stock.is_active=true) and stock.unit_id=:unitId "
				+ " 				GROUP BY item.id) stock ON stock.itemId = item.id	"
				+ "		LEFT JOIN (SELECT storeIndentDetail.item_id AS itemId, sum(storeIndentDetail.pending_quantity) as totalPendingQty"
				+ "					FROM inventory.t_store_indent storeIndent "
				+ "						INNER JOIN inventory.t_store_indent_detail storeIndentDetail ON storeIndentDetail.store_indent_id = storeIndent.id "
				+ "							AND (storeIndentDetail.is_active=true AND storeIndentDetail.is_deleted=false AND storeIndentDetail.pending_quantity > 0) "
				+ "					WHERE storeIndent.from_store_id = :storeId AND storeIndent.is_active=true AND storeIndent.is_deleted=false and storeIndent.unit_id=:unitId "
				+ "					GROUP BY storeIndentDetail.item_id)	AS storeIndent ON storeIndent.itemId = item.id "
				   + " 	Left join public.m_manufacturer m on item.manufacturer_id=m.id and (m.is_deleted=false and m.is_active=true) "
				+ " where item.is_active=true and item.is_deleted=false " + isConsignmentCondation + searchItem
				+ " and stock.totalQty >0 "
				+ " 	and (:itemId=0 or item.id=:itemId) "
				+ " 	and (:itemId<>0 and :itemCode<>'' or :itemCode='' or upper(item.code) like upper(:itemCode)) "
				+ " 	and (:itemId<>0 and :itemName<>'' or :itemName='' or upper(item.name) like upper(:itemName)) "
				+ " 	and (:assetTypeId=0 or assetType.id=:assetTypeId) "
				+ " 	and (:assetCategoryId=0 or assetCategory.id=:assetCategoryId) "
				+ " 	and (:productCategoryId=0 or productCategory.id=:productCategoryId) "
				+ " 				and (item.id not in  (select storeItemRestriction.item_id "
				+ " 										from public.m_store_item_restriction storeItemRestriction "
				+ " 										where storeItemRestriction.store_id=:storeId and storeItemRestriction.is_deleted=false and storeItemRestriction.is_active=true and storeItemRestriction.organization_id=:organizationId)) ";

		Query query = getSession().createSQLQuery(sql).addScalar("id").addScalar("itemCode").addScalar("itemName")
				.addScalar("assetCategory").addScalar("genericName").addScalar("menufactureName").addScalar("productCategory").addScalar("minimumStock")
				.addScalar("currentStock", StandardBasicTypes.LONG).addScalar("stockInTransit", StandardBasicTypes.LONG)
				.addScalar("pendingIndentQuantity", StandardBasicTypes.LONG);
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", request.getItemId() != null ? request.getItemId() : 0);
		query.setParameter("itemCode", request.getItemCode() != null ? request.getItemCode() + '%' : "");
		query.setParameter("itemName", request.getItemName() != null ? request.getItemName() + '%' : "");
		query.setParameter("assetTypeId", request.getAssetTypeId() != null ? request.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId", request.getAssetCategoryId() != null ? request.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				request.getProductCategoryId() != null ? request.getProductCategoryId() : 0);
		query.setParameter("vedId", request.getVedId() != null ? request.getVedId() : 0);
		query.setParameter("organizationId",
				request.getOrganizationId() != null ? request.getOrganizationId() : sessionService.getOrganizationId());
		query.setParameter("unitId", request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ExpiredStoreStockResponse.class));
		query.setFirstResult(request.getOffset() != null ? request.getOffset() : 0);
		query.setMaxResults(request.getRecordPerPage() != null ? request.getRecordPerPage() : 50);
		return query.list();

	}

	@Override
	public List<BatchResponse1> getNextExpiredBatchDetails(int storeId, int itemId, Integer month, Integer unitId) {

		Calendar cal = Calendar.getInstance();

		Date today = cal.getTime();
		// cal.add(Calendar.YEAR, -5); // to get previous year add -1
		Date preYear = cal.getTime();
		Date toDate = TimeZoneComponent.getDateByzone( unitId != null ? unitId : sessionService.getUnitId());
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(toDate);
		cal1.set(toDate.getYear() + 1900, toDate.getMonth() + month, cal1.getActualMaximum(Calendar.DATE));

		SimpleDateFormat simpleDateFormat1 = null;
		Date todate = null;
		SimpleDateFormat simpleDateFormat2 = null;
		Date fromdate = null;
		try {

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy"); // Existing Pattern
			todate = simpleDateFormat.parse(cal1.getTime().toString()); // Returns Date Format,
			fromdate = simpleDateFormat.parse(cal.getTime().toString()); // Returns Date Format,
			simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd"); // New Pattern
// //System.out.println(simpleDateFormat1.format(todate));

		} catch (ParseException e) {
			logger.error("Error while parsing next expired batch details date range", e);
			throw new ServiceException(e.getMessage());
		}
		String dd1 = simpleDateFormat1.format(fromdate);
		String dd2 = simpleDateFormat1.format(todate);

		StringBuffer sb = new StringBuffer();
		String hql1 = "  select batch0_.batch_no as batchNo ,sum (batch0_.quantity) as totalstock, batch0_.expiry_date as expiry, uomtype4_.type as uomType, uomunit5_.unit as uomUnit, \r\n"
				+ " batch0_.is_consignment as isConsignment ,\r\n"
				+ " batch0_.is_quarantine as quarantine , batch0_.selling_price as mrp  , r.name as rack , s.name as shelf , batch0_.lot_no as lotNo ,  batch0_ .actual_lending_price  as actualCost \r\n"
				+ "  , sum (storestock2_.available_qty) as availablestockquantity        from inventory.t_batch batch0_  inner join public.m_item item1_ on batch0_.item_id=item1_.id   \r\n"
				+ " and (item1_.id =:itemId  and item1_.is_deleted=false and item1_.is_active=true)  \r\n"
				+ " inner join inventory.t_store_stock storestock2_ on batch0_.id=storestock2_.batch_id  and (storestock2_.store_id=:storeId and storestock2_.is_active=true \r\n"
				+ " and storestock2_.is_deleted=false and storestock2_.available_qty>0) left join inventory.t_batch_uom_mapper batchuomli3_ \r\n"
				+ " on batch0_.id=batchuomli3_.batch_id and (batchuomli3_.uom_type_id=:id and batchuomli3_.is_active=true and batchuomli3_.is_deleted=false) \r\n"
				+ " left join procurement.t_shelf_item  loc on batch0_.id = loc.batch_id and loc.is_active=true and loc.is_deleted=false left join public.m_rack  r \r\n"
				+ " on loc.rack_id = r.id left join  public.m_shelf s on loc.shelf_id = s.id  left join  public.m_uom_type uomtype4_ on  batchuomli3_.uom_type_id=uomtype4_.id  left join  public.m_uom_unit uomunit5_   on   batchuomli3_.uom_unit_id=uomunit5_.id  \r\n"
				+ " where  (batch0_.expiry_date between to_date( '" + dd1.trim() + "','YYYY-MM-DD') and  to_date( '"
				+ dd2.trim() + "','YYYY-MM-DD') or batch0_.expiry_date is null)    "
				+ " and batch0_.is_active=true and batch0_.is_deleted=false and   batch0_.unit_id = :unitId ";

		sb.append(hql1);
		sb.append(" group by batchNo,expiry,uomType,uomUnit,isConsignment,quarantine,mrp,rack,shelf,lotNo, batch0_ .actual_lending_price\r\n"
				+ " order by batch0_.expiry_date ");

		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
		query.setParameter("id", Constants.uomTypeId);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		query.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.items.BatchResponse1.class));

		return query.list();

	}

	@Override
	public List<StockInTransitResponse> stockInTransit(ExpiredStoreStockRequest request) {

		String hql1 = "   select tst.id as id,  tst .current_bal as currentbal , tst.out_qty as transitStock , ms.name as screen , transit_no  as transactionnumber, tst.added_date  as date " + 
				"from inventory.t_stock_transaction tst " + 
				"join public.m_screen ms  on tst.doc_id =ms.id  " + 
				"where  tst.is_transit =true " + 
				"and tst.store_id =:storeId and tst.batch_id in(select tb.id from inventory.t_batch tb where tb.item_id = :itemId) and tst.unit_id =:unitId and tst.out_qty is not null " + 
				" order by 1 desc ";
//		and tst .current_bal !=0
		Query query = getSession().createSQLQuery(hql1);
		query.setParameter("storeId", request.getStoreId());
		query.setParameter("itemId", request.getItemId()); 
		query.setParameter("unitId", request.getUnitId());

		query.setResultTransformer(Transformers.aliasToBean(StockInTransitResponse.class));

		return query.list();

	}

	 @Override
		public List<SubstituteStoreStockDashboardDto> getSubstituteByIdForStoreStockDB(Integer genericId, Integer itemId, Boolean isNarcotic, Integer storeId, Integer unitId, String dateString) {

			try {
				String sql = "select  "
						+ "	distinct sto.store_name as \"storeName\" ,  "
						+ " sto.store_id as \"storeId\", "
						+ "	itm.\"name\" as \"itemName\",  " 
						+ "	itm.generic_id as \"genericId\",  " 
						+ "	itm.id as \"itemId\",  " 
						+ "	itm.is_narcotic as \"narcotic\",  " 
						+ "	mm.name as \"manufactureName\",  "
						+ "	bth.batch_no as \"batchNo\" ,  " 
						+ "	uomtype.type as \"uom\" ,  "
						+ "	 uomunit.unit as \"uomUnit\",  " 
						+ "	to_char(bth.expiry_date, 'dd/mm/yyyy') as \"expDate\" ,  "
						+ "	stomap.available_qty as \"currentStock\" ,  " 
						+ "	bth.selling_price as \"mrp\" ,  "
						+ "	bth.cop as \"cop\" ,  " 
						+ "	r.name as \"location\" ,  " 
						+ "	s.name as \"shelf\"   " 
						+ "from  "
						+ "	public.m_store sto  " 
						+ "inner join inventory.t_store_stock stomap on  "
						+ "	sto.store_id = stomap.store_id  " 
						+ "	and stomap.unit_id =:unitId  "
						+ "inner join inventory.t_batch bth on  " 
						+ "	stomap.batch_id = bth.id  "
						+ "inner join public.m_item itm on  " 
						+ "	bth.item_id = itm.id  "
						+ "inner join public.m_manufacturer mm on  " 
						+ "	mm.id = itm.manufacturer_id   "
						+ "inner join inventory.t_batch_uom_mapper uommap on  " 
						+ "	uommap.batch_id = bth.id  "
						+ "inner join public.m_uom_unit uomunit on  " 
						+ "	uomunit.id = uommap.uom_unit_id  "
						+ "inner join public.m_uom_type uomtype on  " 
						+ "	uomtype.id = uommap.uom_type_id  "
						+ "	and uomtype.id=3  "
						+ "left join procurement.t_shelf_item loc on  "
						+ "	(bth.id = loc.batch_id)  " 
						+ "left join public.m_rack r on  " 
						+ "	loc.rack_id = r.id  "
						+ "left join public.m_shelf s on  " 
						+ "	loc.shelf_id = s.id  " 
						+ "where  "
						+ "	(0 =:storeId or stomap.store_id =:storeId)  " 
						+ "	and bth.unit_id =:unitId  " 
						+ "	and bth.is_active = true  "
						+ "	and bth.is_quarantine = false  " 
						+ "	and bth.is_deleted = false  "
						+ "	and sto.is_active = true  " 
						+ "	and sto.is_deleted = false  " 
						+ "	and itm.is_active = true  "
						+ "	and itm.is_deleted = false  " 
						+ "	and itm.generic_id = :genericId  "
						+ "	and to_char(bth.expiry_date, 'yyyy-mm-dd') >= :dateString  " 
						+ "order by sto.store_name ,  "
						+ "	itm.name," 
						+ "	bth.batch_no desc";

				{
				StringBuffer s1 = new StringBuffer(sql);
				
				Query query = getSession().createSQLQuery(sql)
						.addScalar("storeName").addScalar("itemName")
						.addScalar("manufactureName").addScalar("batchNo")
						.addScalar("genericId").addScalar("itemId")
						.addScalar("narcotic")
						.addScalar("uom").addScalar("uomunit").addScalar("storeId")
						.addScalar("expDate").addScalar("cop").addScalar("mrp")
						.addScalar("currentStock")
						.addScalar("location").addScalar("shelf");
				
				/*if(isNarcotic==false) {
					s1.append("AND item.isNarcotic=:isNarcotic ");
				}*/
				query.setParameter("genericId", genericId);
//				query.setParameter("itemId", itemId);
				query.setParameter("storeId", storeId != null ? storeId :0);
				query.setParameter("unitId", unitId);
				query.setParameter("dateString", dateString);

					/*if(isNarcotic==false) {
						query.setParameter("isNarcotic", isNarcotic);
					}
*/
				query.setResultTransformer(Transformers.aliasToBean(SubstituteStoreStockDashboardDto.class));
				

				return query.list();
			}
			} catch (Exception e) {
				logger.error("Error while fetching substitute store stock dashboard data", e);
				throw new ServiceException(e.getMessage());
			}

		}

	@Override
	public List<BatchResponse1> getUOMUnitType(int storeId, int itemId, Integer unitId,String batchno) {
		StringBuffer sb = new StringBuffer();
		String sql=" select uomtype.type as uomType, uomunit.unit as uomUnit from inventory.t_material_inwards_detail tmid " + 
				" inner join inventory.t_batch tb on tb.item_id =tmid.item_id " + 
				" inner join inventory.t_material_inwards tmi on tmi.id =tmid.material_inwards_id " + 
				" inner join public.m_uom_type uomtype on  tmid.uom_type_id =uomtype.id " + 
				" inner join public.m_uom_unit uomunit on   tmid.uom_unit_id =uomunit.id " + 
				" where tmi.store_id =:storeId and tmid.item_id =:itemId and tmid.unit_id =:unitId and tb.batch_no =:batchNo  order by 1 desc limit 1";

		sb.append(sql);
		Query query = getSession().createSQLQuery(sb.toString());
		query.setParameter("storeId", storeId);
		query.setParameter("itemId", itemId);
//		query.setParameter("id", Constants.uomTypeId);//and uomtype.id =:id
		query.setParameter("batchNo", batchno);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		query.setResultTransformer(Transformers.aliasToBean(com.param.global.dto.items.BatchResponse1.class));

		return query.list();

	}

}

package com.param.inventory.nursing.dao.nonBillableConsumption;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.NonBillableConsumption;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.global.GlobalDao;
import com.param.global.dto.items.BatchListResponse;
import com.param.global.dto.items.ItemsRequest;
import com.param.global.dto.items.UomResponse;
import com.param.global.otims.dto.BatchDto;
import com.param.global.utility.Util;
import com.param.inventory.nursing.dto.nonBillableConsumption.BatchResponse;
import com.param.inventory.nursing.dto.nonBillableConsumption.ConsumptionRequest;
import com.param.inventory.nursing.dto.nonBillableConsumption.ConsumptionResponse;
import com.param.inventory.nursing.dto.nonBillableConsumption.NonBillableConsumptionDto;
import com.param.service.global.SessionService;

@Repository
public class ConsumptionDao extends AbstractDao<Integer, NonBillableConsumption> implements IConsumptionDao,ICommonConstants {

	@Autowired
	private SessionService sessionService;
	
	@Autowired
	GlobalDao GlobalDao;

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public NonBillableConsumption saveConsumption(NonBillableConsumption consumption) {
	/*	getSession().saveOrUpdate(consumption);*/
		sessionFactory.getCurrentSession().saveOrUpdate(consumption);
		return consumption;
	}

	@Override
	public List<ConsumptionResponse> getConsumptionList(ConsumptionRequest request, Integer screenId) {

		String hql = "SELECT  distinct consumption.id AS id, consumption.consumptionNo AS consumptionNo, consumption.addedDate AS consumptionDate, store.storeId AS storeId, store.storeName AS storeName, "
				+ "		consumption.remark AS remark, user.userName AS requestedBy, status.id AS statusId, status.status AS status, approvalStatus.id AS approvalStatusId, approvalStatus.status AS approvalStatus "
				+ "	FROM NonBillableConsumption AS consumption "
				+ "		INNER JOIN consumption.store.storeItemMapperList AS storeMapper WITH storeMapper.isActive = true AND storeMapper.isDeleted = false and storeMapper.isSellable=false "	
				+ "		INNER JOIN consumption.store AS store WITH store.isActive = true AND store.isDeleted = false "
			//	+ "		INNER JOIN consumption.store.storeItemMapperList AS storeMapper WITH storeMapper.isActive = true AND storeMapper.isDeleted = false and storeMapper.isSellable=false "
				+ "		INNER JOIN consumption.addedBy as user WITH user.status = 'A' "
				+ "		INNER JOIN consumption.status AS status WITH status.isActive = true AND status.isDeleted = false "
				+ "		INNER JOIN consumption.approvalStatus AS approvalStatus WITH approvalStatus.isActive = true AND approvalStatus.isDeleted = false "
				+ "	WHERE consumption.isActive = true AND consumption.isDeleted = false AND consumption.unit.unitId = :unitId "
				+ "		AND consumption.addedDate BETWEEN :fromDate AND :toDate DEPT "
				+ "		AND (:consumptionNo = '' OR upper(consumption.consumptionNo) like upper(:consumptionNo)) "
			//	+ "		AND (:storeMapper = 0 OR storeMapper.id = :storeMapper) "
				+ "		AND (:storeId = 0 OR store.id = :storeId) "
				+ "		AND (:statusId = 0 OR status.id = :statusId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId AND screen.isDeleted = false "
				+ "										AND screen.isActive = true "
				+ "								INNER JOIN screenStatus.status as status WITH status.isDeleted = false AND status.isActive = true "
				+ "						  WHERE screenStatus.isActive = true AND screenStatus.isDeleted = false and storeMapper.isSellable=false)  "
				+ "	ORDER BY consumption.id DESC ";

		
		if(request.getRequestFrom()!=null && request.getRequestFrom().equals("departmentConsumption"))
		{
			hql=hql.replace("DEPT", " AND consumption.requestFrom=:requestFrom ");
		}
		else
		{
			hql=hql.replace("DEPT", " ");
		}
		
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(request.getFromDate()));
		query.setParameter("toDate", Util.getDate(request.getToDate()));
		//query.setParameter("storeMapper", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
		query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
		query.setParameter("consumptionNo", request.getConsumptionNo() != null ? '%'+request.getConsumptionNo() + "%" : "");
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ConsumptionResponse.class));
		
		if(request.getRequestFrom()!=null && request.getRequestFrom().equals("departmentConsumption"))
		{
			query.setParameter("requestFrom", "DepartmentConsumption");
		}
		 
		
		
		return query.list();
	}

	@Override
	public ConsumptionResponse getConsumption(Integer id) {

		String hql = "SELECT consumption.id AS id, consumption.consumptionNo AS consumptionNo, consumption.addedDate AS consumptionDate, store.storeId AS storeId, store.storeName AS storeName, "
				+ "		consumption.remark AS remark, user.userName AS requestedBy, status.id AS statusId, status.status AS status, approvalStatus.id AS approvalStatusId, "
				+ "		approvalStatus.status AS approvalStatus " + "	FROM NonBillableConsumption AS consumption "
				+ "		INNER JOIN consumption.store AS store WITH store.isActive = true AND store.isDeleted = false "
				+ "		INNER JOIN consumption.addedBy as user WITH user.status = 'A' "
				+ "		INNER JOIN consumption.status AS status WITH status.isActive = true AND status.isDeleted = false "
				+ "		INNER JOIN consumption.approvalStatus AS approvalStatus WITH approvalStatus.isActive = true AND approvalStatus.isDeleted = false "
				+ "	WHERE consumption.id = :id AND consumption.isActive = true AND consumption.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(ConsumptionResponse.class));

		return (ConsumptionResponse) query.uniqueResult();
	}

	@Override
	public List<BatchResponse> getConsumptionDetail(Integer id) {

		String hql = "SELECT consumptionDetail.id AS id, item.code AS itemCode, item.name AS itemName, item.id AS itemId,uomType.type AS uomType, "
				+ "		uomUnit.units AS uomUnit, uomType.id AS uomTypeId, uomUnit.id AS uomUnitId, consumption.organization.organizationId as organizationId, "
				+ "		consumptionDetail.acceptedQty AS acceptedQuantity, consumptionDetail.rejectQty AS rejectedQuantity, batch.id AS batchId, "
				+ "		batch.batchNo AS batchNo, batch.expiryDate AS expiry, COALESCE(storeStock.availableQty, 0) AS availableQuantity, "
				+ "		consumptionDetail.consumedQty AS consumedQuantity, status.id AS statusId, item.isThreeUOMApplicable AS isThreeUOMApplicable "
				+ "	FROM NonBillableConsumption AS consumption "
				+ "		INNER JOIN consumption.nonBillableConsumptionDetails AS consumptionDetail WITH consumptionDetail.isActive = true AND consumptionDetail.isDeleted = false "
				+ "		INNER JOIN consumptionDetail.status AS status WITH status.isActive = true AND status.isDeleted = false "
				+ "		INNER JOIN consumptionDetail.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false "				
                + "		LEFT JOIN consumptionDetail.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
                + "		LEFT JOIN consumptionDetail.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "				
				+ "		LEFT JOIN batch.batchUomList AS uom WITH uom.storeUom = true AND uom.isActive = true AND uom.isDeleted = false "
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
				+ "		INNER JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
				+ "	WHERE consumption.id = :id AND consumption.store.storeId = storeStock.store.storeId "
				+ " AND consumption.isActive = true AND consumption.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(BatchResponse.class));

		List<BatchResponse> batchList = new ArrayList<>();
		if(query.list().size() > 0) {
			batchList = query.list();
			for (BatchResponse  batchDto : batchList) {
				List<UomResponse> uom= GlobalDao.getUnitOfMeasurementListByItem(batchDto.getItemId(), batchDto.getOrganizationId());
				batchDto.setUom(uom);
			}
		}
		
		return batchList;
	}

	@Override
	public NonBillableConsumption getNonBillableConsumption(Integer id) {
		String hql = "SELECT consumption FROM NonBillableConsumption AS consumption "
				+ "	WHERE consumption.id = :id AND consumption.isActive = true AND consumption.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (NonBillableConsumption) query.uniqueResult();
	}

	@Override
	public NonBillableConsumption updateConsumption(NonBillableConsumption consumption) {
		getSession().update(consumption);

		return consumption;
	}

	/* start code for resolved HIS-10494 While Updating consumption then batch number is not getting display in non billable consumption screen */
	// reference from batchListBillableConsumptionNew(ItemsServiceImpl.java L : 1605)  -> List<BatchListResponse> batchListStoreWise(ItemsRequest itemRequest) (ItemsDaoImpl.java L:923);
	@Override
	public List<BatchListResponse> batchListStoreWiseForNonBillableConsumption(ItemsRequest itemsRequest) {
		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, productCategory.id as prodCatId, generic.name AS genericName, "
				+ " 	item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
				+ " 	batch.rate AS mrp, COALESCE(SUM(storeStock.availableQty), 0) AS availableQuantity, batch.lotNo AS lotNo, batch.cop AS cop, "
				+ "		purchaseTax.id AS taxId, purchaseTax.name AS taxName, purchaseTax.taxPercentage AS taxPercentage, vendor.id AS vendorId, "
				+ "		vendor.contactPersonName AS vendor, batch.yearOfManufacture AS manufactureYear, item.isBatchRequired AS isBatchRequired,  "
				+ "		COALESCE(assetCategory.id, itemAssetCategory.id) as assetCategoryId, "
				+ "		COALESCE(productCategory.id, itemProductCategory.id) as productCategoryId  "
				+ "	    FROM Batch AS batch "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN item.purchaseTax AS purchaseTax WITH purchaseTax.isActive = true AND purchaseTax.isDeleted = false "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		LEFT JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		LEFT JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		LEFT JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		LEFT JOIN item.productCategory AS itemProductCategory WITH itemProductCategory.isDeleted = false AND itemProductCategory.isActive = true "
				+ "		LEFT JOIN itemProductCategory.assetCategory AS itemAssetCategory WITH itemAssetCategory.isDeleted = false AND itemAssetCategory.isActive = true  "
				+ "		LEFT JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true AND storeStock.availableQty > 0 "
				+ "		LEFT JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ "		LEFT JOIN batch.vendor AS vendor WITH vendor.isActive = true AND vendor.isDeleted = false "
				//+ "	WHERE batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.unit.unitId = :unitId "
				+ "	    WHERE batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId "
				//+ " and  extract(MONTH FROM batch.expiryDate) >= extract(MONTH FROM now()) AND  extract(YEAR FROM batch.expiryDate) >= extract(YEAR FROM now()) "
				+ "		AND (:consignment = 0 OR item.isConsignment = :isConsignment) "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ " 	AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND (:storeId = 0 OR storeStock.store.storeId = :storeId) "
				+ "		AND (:itemCode = '' OR upper(item.code) like upper(:itemCode)) "
				+ "		AND (:itemName = '' OR upper(item.name) like upper(:itemName)) "
				+ " 	and ( :batchName='' or upper(batch.batchNo) like upper(:batchName)) "
				+ "		AND (:storeId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :storeId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :storeId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true and itemRestriction .unit.unitId = :unitId)) "
				+ "	GROUP BY item.id, assetCategory.category, productCategory.category, generic.name, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate, batch.rate, "
				+ "		batch.lotNo, batch.cop, purchaseTax.id, purchaseTax.name, purchaseTax.taxPercentage, vendor.id, vendor.contactPersonName, productCategory.id, item.isBatchRequired, "
				+ "	assetCategory.id, itemAssetCategory.id, " 
				+ "	productCategory.id, itemProductCategory.id "
				+ "	ORDER BY item.id, batch.id, assetCategory.id, itemAssetCategory.id, "
				+ "	productCategory.id, itemProductCategory.id";

		Query query = getSession().createQuery(hql);
        query.setParameter("assetTypeId", itemsRequest.getAssetTypeId() != null ? itemsRequest.getAssetTypeId() : 0);
        query.setParameter("assetCategoryId",
        		itemsRequest.getAssetCategoryId() != null ? itemsRequest.getAssetCategoryId() : 0);
        query.setParameter("productCategoryId",
        		itemsRequest.getProductCategoryId() != null ? itemsRequest.getProductCategoryId() : 0);
        query.setParameter("vendorId", itemsRequest.getVendorId() != null ? itemsRequest.getVendorId() : 0);
        query.setParameter("storeId", itemsRequest.getStoreId() != null ? itemsRequest.getStoreId() : 0);
        query.setParameter("isConsignment",
        		itemsRequest.getIsConsignment() != null ? itemsRequest.getIsConsignment() : false);
        query.setParameter("consignment", itemsRequest.getIsConsignment() != null ? 1 : 0);
        query.setParameter("itemCode", itemsRequest.getItemCode() != null ? itemsRequest.getItemCode() : "");
        query.setParameter("itemName", itemsRequest.getItemName() != null ? itemsRequest.getItemName() : "");
        query.setParameter("batchName", itemsRequest.getBatchName() != null ? itemsRequest.getBatchName() + '%' : "");
        query.setParameter("unitId", sessionService.getUnitId());
        query.setResultTransformer(Transformers.aliasToBean(BatchListResponse.class));

        return query.list();
		
	}
	/* end code for resolved HIS-10494 While Updating consumption then batch number is not getting display in non billable consumption screen */

	@Override
	public Response getConsumptionDetailsByTemplateTransactionId(ConsumptionRequest request) {

		String sql = "select mi.code  as \"itemCode\" , "  
						+ " tnbc.id  as \"id\","
						+ " mi.name  as \"itemName\" , " 
						+ " concat(tb.batch_no , ' | ' ,to_char(tb.expiry_date,'Mon-YYYY'))  as \"batchNo\", "
						+ " to_char(tb.expiry_date,'Mon-YYYY') as \"expiryDate\","
						+ " concat(uomType.code  ,'-', uomUnit.unit)  as \"uomDetails\","
						+ " tnbcd.consumed_qty  as \"consumedQty\","
						+ " tnbcd.accepted_qty as \"acceptedQty\","
						+ " stock.available_qty  as \"availableQty\" "
						+ " from inventory.t_non_billable_consumption tnbc " 
						+ " inner join inventory.t_non_billable_consumption_detail tnbcd  on tnbcd.non_billable_consumption_id  = tnbc.id "
						+ " and tnbcd.is_active =true and tnbcd.is_deleted =false and tnbcd.organization_id =:organizationId and tnbcd.unit_id =:unitId "
						+ " inner join inventory.t_batch tb on tb.id  = tnbcd.batch_id and tb.is_active =true and tb.is_deleted =false "
						+ " and tb.organization_id =:organizationId and tb.unit_id =:unitId "
						+ " inner join public.m_item mi on mi.id = tb.item_id and mi.is_active =true and mi.is_deleted =false "
						+ " and mi.organization_id =:organizationId and mi.unit_id =:unitId "
						+ " inner join public.m_uom_type uomType on uomType.id  = tnbcd.uom_type_id  "
						+ " inner join public.m_uom_unit uomUnit on uomUnit.id = tnbcd.uom_unit_id and uomUnit.unit_id =:unitId "
						+ " inner join inventory.t_store_stock stock on stock.batch_id  = tb.id "
						+ " where tnbc.organization_id = :organizationId "
						+ " and tnbc.unit_id = :unitId "
						+ " and tnbc.request_from_id  = :requestFromId "
						+ " and tnbc.is_active =true and tnbc.is_deleted = false "
						+ " and stock.store_id = :storeId ";

		Query query = getSession().createSQLQuery(sql);
		query.setParameter("requestFromId", request.getRequestFromId());
		query.setParameter("organizationId", request.getOrganizationId());
		query.setParameter("unitId", request.getUnitId());
		query.setParameter("storeId", request.getStoreId()!=null ? request.getStoreId(): 0);
		query.setResultTransformer(Transformers.aliasToBean(NonBillableConsumptionDto.class));
        return new Response<>(SUCCESS, SUCCESS_CODE, null, query.list(), null);
	}
	
}

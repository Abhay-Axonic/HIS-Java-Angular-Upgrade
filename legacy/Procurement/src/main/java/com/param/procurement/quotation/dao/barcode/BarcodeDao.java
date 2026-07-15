package com.param.procurement.quotation.dao.barcode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.Batch;
import com.param.global.common.ServiceException;
import com.param.global.dto.items.ItemsDetail;
import com.param.global.dto.items.ItemsRequest;
import com.param.procurement.quotation.dto.barcode.BarcodeBatchListResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Repository
public class BarcodeDao extends AbstractDao<Integer, Batch> implements IBarcodeDao {

	final static Logger logger = Logger.getLogger(BarcodeDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<BarcodeBatchListResponse> GetBatchList(ItemsRequest itemRequest) {

		Date current = TimeZoneComponent.getDateByzone(itemRequest.getUnitId()!=null ? itemRequest.getUnitId(): sessionService.getUnitId());
		Calendar cal = Calendar.getInstance();
		cal.setTime(current);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		current = cal.getTime();
		
		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, "
				+ " item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, batch.lotNo as lotNo  , "
				+ " batch.sellingPrice as mrp, batch.cop as cop , batch.organization.organizationName as hospitalName,batch.yearOfManufacture as manufactureYear, batch.printedCount as printedCount, "
				+ " COALESCE(SUM(storeStock.availableQty), 0) AS availableQuantity,batch.prePrintedBarcode AS prePrintedBarcode, COALESCE(batch.barcodePrintCount, 0) as barcodePrintCount " + "FROM Batch AS batch "
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
				+ "		INNER JOIN generic.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ " WHERE batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:name = '' OR lower(item.name) LIKE :name) "
				+ " 	AND (:batchNo = '' OR lower(batch.batchNo) LIKE :batchNo) "
				+ " 	AND (:code = '' OR lower(item.code) LIKE :code) "
				
				+ " AND ( batch.expiryDate >= :greaterThanCurrentMonth or batch.expiryDate is  null )"
				
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ "		AND (:storeId = 0 OR store.storeId = :storeId) "
				+ "		AND (:storeId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :storeId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :storeId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ " GROUP BY item.id, assetCategory.category, productCategory.category, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate,"
				+ "		batch.barcodePrintCount, batch.printedCount, batch.sellingPrice, batch.organization.organizationName "
				+ "ORDER BY item.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("assetTypeId", itemRequest.getAssetTypeId() != null ? itemRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemRequest.getAssetCategoryId() != null ? itemRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemRequest.getProductCategoryId() != null ? itemRequest.getProductCategoryId() : 0);
		query.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		query.setParameter("unitId",itemRequest.getUnitId()!=null ? itemRequest.getUnitId(): sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(BarcodeBatchListResponse.class));
		query.setParameter("name", itemRequest.getItemName() != null ? '%'+itemRequest.getItemName().toLowerCase()+"%" : "");
		query.setParameter("batchNo", itemRequest.getBatchNo() != null ? '%'+itemRequest.getBatchNo().toLowerCase()+"%" : "");
		query.setParameter("code", itemRequest.getItemCode() != null ? '%'+itemRequest.getItemCode().toLowerCase()+"%" : "");
		query.setParameter("greaterThanCurrentMonth", current);

		return query.list();
	}
	
	
	@Override
	public List<BarcodeBatchListResponse> GetBatcByFixedAssetTypeList(ItemsRequest itemRequest) {

		String hql = "SELECT item.id AS itemId, assetCategory.category AS assetCategory, productCategory.category AS productCategory, batch.yearOfManufacture as manufactureYear,"
				+ " item.code AS itemCode, item.name AS itemName, batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS expiry, batch.printedCount as printedCount, "
				+ " batch.sellingPrice as mrp, batch.organization.organizationName as hospitalName, "
				+ " COALESCE(SUM(storeStock.availableQty), 0) AS availableQuantity, COALESCE(batch.barcodePrintCount, 0) as barcodePrintCount,batch.prePrintedBarcode as prePrintedBarcode "
				+ "		FROM Batch AS batch "
				+ "		INNER JOIN batch.storeStockList AS storeStock WITH storeStock.isDeleted = false AND storeStock.isActive = true "
				+ "		INNER JOIN batch.item AS item WITH item.isDeleted = false AND item.isActive = true "
				+ "		INNER JOIN item.productCategory AS productCategory WITH productCategory.isDeleted = false AND productCategory.isActive = true "
				+ "		INNER JOIN productCategory.assetCategory AS assetCategory WITH assetCategory.isDeleted = false AND assetCategory.isActive = true "
				+ "		INNER JOIN assetCategory.assetType as assetType WITH assetType.isDeleted = false AND assetType.isActive = true "
				+ "		INNER JOIN storeStock.store AS store WITH store.isDeleted = false AND store.isActive = true "
				+ " WHERE batch.isDeleted = false AND batch.isActive = true AND batch.unit.unitId = :unitId "
				+ " 	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " 	AND (:assetCategoryId = 0 OR assetCategory.id = :assetCategoryId) "
				+ " 	AND (:productCategoryId = 0 OR productCategory.id = :productCategoryId) "
				+ "		AND (:storeId = 0 OR store.storeId = :storeId) "
				+ " 	AND (:name = '' OR lower(item.name) LIKE :name) "
				+ " 	AND (:batchNo = '' OR lower(batch.batchNo) LIKE :batchNo) "
				+ " 	AND (:code = '' OR lower(item.code) LIKE :code) "
				+ "		AND (:storeId = 0 OR productCategory.id in (SELECT storeProduct.productCategory.id "
				+ "														FROM StoreProductCategoryMapper AS storeProduct "
				+ "														WHERE storeProduct.store.storeId = :storeId) "
				+ "							AND item.id NOT IN (SELECT itemRestriction.item.id FROM StoreItemRestriction AS itemRestriction "
				+ "												WHERE itemRestriction.store.storeId = :storeId "
				+ "												AND itemRestriction.isDeleted = false AND itemRestriction.isActive = true)) "
				+ " GROUP BY item.id, assetCategory.category, productCategory.category, item.code, item.name, batch.id, batch.batchNo, batch.expiryDate,"
				+ "		batch.barcodePrintCount, batch.printedCount, batch.sellingPrice, batch.organization.organizationName "
				+ "ORDER BY item.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("assetTypeId", itemRequest.getAssetTypeId() != null ? itemRequest.getAssetTypeId() : 0);
		query.setParameter("assetCategoryId",
				itemRequest.getAssetCategoryId() != null ? itemRequest.getAssetCategoryId() : 0);
		query.setParameter("productCategoryId",
				itemRequest.getProductCategoryId() != null ? itemRequest.getProductCategoryId() : 0);
		query.setParameter("storeId", itemRequest.getStoreId() != null ? itemRequest.getStoreId() : 0);
		query.setParameter("unitId", itemRequest.getUnitId()!=null ? itemRequest.getUnitId():  sessionService.getUnitId());
		query.setParameter("name", itemRequest.getItemName() != null ? '%'+itemRequest.getItemName().toLowerCase()+"%" : "");
		query.setParameter("batchNo", itemRequest.getBatchNo() != null ? '%'+itemRequest.getBatchNo().toLowerCase()+"%" : "");
		query.setParameter("code", itemRequest.getItemCode() != null ? '%'+itemRequest.getItemCode().toLowerCase()+"%" : "");
	
		query.setResultTransformer(Transformers.aliasToBean(BarcodeBatchListResponse.class));

		return query.list();
	}


	@Override
	public List<ItemsDetail> savePrePrintedBarcode(ItemsRequest itemRequest) {
        List<ItemsDetail> responseList = new ArrayList<ItemsDetail>();
        responseList=itemRequest.getItems();
        for (ItemsDetail itemsDetail : responseList) {
        	int res = getSession().createSQLQuery(
                    " update inventory.t_batch set pre_printed_barcode ='"+itemsDetail.getPrePrintedBarcode()+"'"
                            + "  where id ="+itemsDetail.getBatchId()+" and item_id="+itemsDetail.getItemId()+""
                           ).executeUpdate();
		}
        return responseList;
	}


	@Override
	public boolean isManualBarcodeRequired(Integer unitId) {
		boolean res = (boolean) getSession().createSQLQuery(
		" select mac.is_manual_barcode_required from public.m_application_configuration mac where mac.unit_id="+unitId+"").uniqueResult();
		return res;
	}


	@Override
	public List<BarcodeBatchListResponse> searchPrePrintedBarcode(ItemsRequest itemRequest) {
		String hql = "SELECT batch.prePrintedBarcode as prePrintedBarcode,cast(batch.item as int) as itemId,batch.id as batchId "
				+ "		FROM Batch AS batch "
				+ "WHERE batch.unit.unitId = :unitId  ";
		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", itemRequest.getUnitId()!=null ? itemRequest.getUnitId():  sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(BarcodeBatchListResponse.class));

		return query.list();

	}
	@Override
	public List<BarcodeBatchListResponse> getPrePrintedBarcode(ItemsRequest itemRequest) {
		String hql = "SELECT batch.prePrintedBarcode as prePrintedBarcode ,cast(batch.item as int) as itemId,batch.id as batchId "
				+ "		FROM Batch AS batch "
				+ "WHERE batch.unit.unitId = :unitId and (:prePrintedBarcodeNo = '' OR lower(batch.prePrintedBarcode) LIKE :prePrintedBarcodeNo) and batch.prePrintedBarcode is not null and batch.prePrintedBarcode!='' ";
		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", itemRequest.getUnitId()!=null ? itemRequest.getUnitId():  sessionService.getUnitId());
		query.setParameter("prePrintedBarcodeNo", itemRequest.getPriPrintedBarcodeNo() != null ? '%'+itemRequest.getPriPrintedBarcodeNo().toLowerCase()+"%" : "");
		query.setResultTransformer(Transformers.aliasToBean(BarcodeBatchListResponse.class));

		return query.list();

	}


	@Override
	public boolean isPRNRequired(Integer unitId) {
			boolean res = (boolean) getSession().createSQLQuery(
			" select mac.is_prn_required from public.m_application_configuration mac where mac.unit_id="+unitId+"").uniqueResult();
			return res;
	}

	@Override
	public String pharmacyPRNFormat(Integer unitId) {
		String res = (String) getSession().createSQLQuery(
				" select mac.pharmacy_prn_format from public.m_application_configuration mac where mac.unit_id="+unitId+"").uniqueResult();
				return res;
	}

	@Override
	public List<BarcodeBatchListResponse> getGrnInfo(Integer itemId, String batchNo, Date expiry, String lotNo) {
		List<BarcodeBatchListResponse> list = new ArrayList<>();
		if(itemId != 0 && !batchNo.equalsIgnoreCase("NA") && !batchNo.isEmpty() && expiry != null)
		{
			String sql = "Select to_char(tgrn.added_date,'yyyy-MM-dd') as \"grnDate\", tgrn.grn_number as \"grnNo\", mv.name as \"vendorName\"  "
				 +	 " from procurement.t_good_receipt_note_detail tgrnd "
				 + 	 " inner join procurement.t_good_receipt_note tgrn on tgrn.id = tgrnd.grn_id  "
				 + 	 " inner join public.m_vendor mv on mv.id = tgrn.vendor_id  "
				 + 	 " where tgrnd.item_id  = " + itemId
				 + 	 " and upper (tgrnd.batch_no) = upper ('"+ batchNo +"')"
				 +	 " and tgrnd.expiry_date = '"+ expiry +"' ";

			Query query = getSession().createSQLQuery(sql);
			query.setResultTransformer(Transformers.aliasToBean(BarcodeBatchListResponse.class));
			list =  query.list();
		}

		else if ((batchNo.equalsIgnoreCase("NA") || batchNo.isEmpty()) && lotNo != null) {

			String sql1 = "Select to_char(tgrn.added_date,'yyyy-MM-dd') as \"grnDate\", tgrn.grn_number as \"grnNo\", mv.name as \"vendorName\"  "
					+	 " from procurement.t_good_receipt_note_detail tgrnd "
					+ 	 " inner join procurement.t_good_receipt_note tgrn on tgrn.id = tgrnd.grn_id "
					+ 	 " inner join public.m_vendor mv on mv.id = tgrn.vendor_id  "
					+ 	 " where tgrnd.item_id  = " + itemId
					+ 	 " and upper (tgrnd.batch_no) = upper ('"+ batchNo +"')"
					+ 	 " and upper (tgrnd.lot_number) = upper ('"+ lotNo +"')";

			Query query = getSession().createSQLQuery(sql1);
			query.setResultTransformer(Transformers.aliasToBean(BarcodeBatchListResponse.class));
			list =  query.list();
		}

		return list;
	}


	@Override
	public List<BarcodeBatchListResponse> getGrnInfoAll(List<Integer> itemIds, List<String> batchNos,
			List<Date> allExpiries, List<String> allLotNos) {
			List<Date> notNullExpiries = new ArrayList<>();
			List<String> notNullBatchNo = new ArrayList<>();
			List<Integer> notNullItemIds = new ArrayList<>();
			for (Date expiry : allExpiries)
			{
				if(expiry != null){
					notNullExpiries.add(expiry);
				}
			}

		for (String batchNo : batchNos)
		{
			if(batchNo != null){
				notNullBatchNo.add(batchNo);
			}
		}

		for (Integer itemId : itemIds)
		{
			if(itemId != null){
				notNullItemIds.add(itemId);
			}
		}

		String sql = "Select to_char(tgrn.added_date,'yyyy-MM-dd') as \"grnDate\", tgrn.grn_number as \"grnNo\", mv.name as \"vendorName\" "
				 + " , tgrnd.item_id as \"itemId\" , tgrnd.batch_no as \"batchNo\" , tgrnd.expiry_date as \"expiry\" , tgrnd.lot_number as \"lotNumber\" "
				 +	 " from procurement.t_good_receipt_note_detail tgrnd "
				 + 	 " inner join procurement.t_good_receipt_note tgrn on tgrn.id = tgrnd.grn_id  "
				 + 	 " inner join public.m_vendor mv on mv.id = tgrn.vendor_id  "
				 + 	 " where tgrnd.item_id  in (:itemId) "
				 + 	 " and upper (tgrnd.batch_no) in(:batchNo)"
				 +	 " and tgrnd.expiry_date in(:expiry) ";
			Query query = getSession().createSQLQuery(sql);
			query.setParameterList("itemId", notNullItemIds);
			query.setParameterList("batchNo", notNullBatchNo);
			query.setParameterList("expiry", notNullExpiries);
			query.setResultTransformer(Transformers.aliasToBean(BarcodeBatchListResponse.class));
			return query.list();
	}


	@Override
	public Integer updatePrintedCount(List <ItemsRequest> itemRequestList) {
		try {
			for (ItemsRequest itemsRequest : itemRequestList) {
				getSession().createSQLQuery("update inventory.t_batch set printed_count = COALESCE(printed_count, 0) + "+ itemsRequest.getPrintedCount()+" "
						+   " where id=:batchId")
						.setParameter("batchId", itemsRequest.getBatchId() != null ? itemsRequest.getBatchId() : 0L )
						.executeUpdate();
			}
			return 1;
		} catch (Exception e) {
			logger.error("updatePrintedCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<BarcodeBatchListResponse> getUpdatedPrintedCount(List <Long> batchIds) {

			String sql1 = "Select batch.id as \"batchId\", batch.printed_count as \"printedCount\", batch.batch_no as \"batchNo\" "
					+	 " from inventory.t_batch batch "
					+ 	 " where batch.id in (:batchIds)"
					+ 	 " order by batch.id desc";
			Query query = getSession().createSQLQuery(sql1).addScalar("batchId", StandardBasicTypes.LONG)
					.addScalar("printedCount", StandardBasicTypes.INTEGER)
					.addScalar("batchNo", StandardBasicTypes.STRING)
					.setParameterList("batchIds", batchIds);
			query.setResultTransformer(Transformers.aliasToBean(BarcodeBatchListResponse.class));
		return query.list();
	}

}

package com.param.procurement.purchase.dao.purchaseOrder;

import com.param.entity.model.procurement.*;
import com.param.global.utility.Util;
import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.purchase.consignmentstockpo.dto.ConsignmentStockPOHistoryDto;
import com.param.procurement.purchase.dto.purchaseOrder.PoOtherChargesMapperDto;
import com.param.procurement.purchase.dto.purchaseOrder.PoPurchaseRequestArray;
import com.param.procurement.purchase.dto.purchaseOrder.PurchaseOrderStagedDto;
import com.param.procurement.purchase.dto.purchaseOrder.PurchaseOrderUomMapperDto;
import com.param.procurement.purchase.purchaseOrder.dto.*;
import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository()
public class PurchaseOrderDao extends AbstractDao<Integer, PurchaseOrder> implements IPurchaseOrderDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<PurchaseOrder> searchPurchaseOrderList(PurchaseOrderMasterDto PurchaseOrdDto, Integer screenId) {
		String hql = "SELECT distinct purchaseOrder FROM PurchaseOrderDetail as purchaseOrderDetail join purchaseOrderDetail.purchaseOrder AS purchaseOrder "
				// + " INNER JOIN purchaseOrder.addedBy as user WITH user.unitId = :unitId "
				+ " 	LEFT JOIN purchaseOrder.store as store WITH store.unit.unitId = :unitId "
				+ " 	LEFT JOIN purchaseOrder.requestingStore as requestingStore"

				+ "		LEFT JOIN purchaseOrder.status as status "
				+ "		LEFT JOIN purchaseOrder.approvalStatus as approvalStatus "
				+ " 	LEFT JOIN purchaseOrder.assetType as assetType "
				+ " 	LEFT JOIN purchaseOrder.requestType as requestType "
				+ " 	LEFT JOIN purchaseOrder.vendor as vendor "
				+ " 	LEFT JOIN purchaseOrderDetail.item as item "
				+ " WHERE purchaseOrder.isActive = true AND purchaseOrder.isDeleted = false AND purchaseOrder.unit.unitId = :unitId "
				+ " 	AND purchaseOrder.addedDate BETWEEN :fromDate AND :toDate "
				+ "		AND (:storeId = 0 OR store.id = :storeId) "
				+ "		AND (:requestingStoreId = 0 OR requestingStore.id = :requestingStoreId) "
				+ " 	AND (:poNumber = '' OR lower(purchaseOrder.poNumber) LIKE :poNumber) "
				+ " 	AND (:vendorName = '' OR lower(purchaseOrder.vendor.name) LIKE :vendorName) "
				+ " 	AND (:statusId = 0 OR status.id = :statusId) "
				+ "		AND (:itemId = 0 OR item.id = :itemId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " ORDER BY purchaseOrder.id DESC";

		// + " AND (:vendorName = '' OR lower(purchaseOrder.vendor.name) LIKE
		// :vendorName) OR lower(purchaseOrder.vendor.code) LIKE :vendorName )"

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(PurchaseOrdDto.getFromDate()));
		query.setParameter("toDate", Util.getDate(PurchaseOrdDto.getToDate()));
		query.setParameter("storeId", PurchaseOrdDto.getStoreId() != null ? PurchaseOrdDto.getStoreId() : 0);
		query.setParameter("requestingStoreId",
				PurchaseOrdDto.getRequestingStoreId() != null ? PurchaseOrdDto.getRequestingStoreId() : 0);
		query.setParameter("poNumber",
				PurchaseOrdDto.getPoNumber() != null ? '%'+PurchaseOrdDto.getPoNumber().toLowerCase() + '%' : "");
		query.setParameter("vendorName",
				PurchaseOrdDto.getVendorName() != null ? PurchaseOrdDto.getVendorName().toLowerCase() + '%' : "");
		query.setParameter("statusId", PurchaseOrdDto.getStatusId() != null ? PurchaseOrdDto.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", PurchaseOrdDto.getUnitId()!=null ? PurchaseOrdDto.getUnitId(): sessionService.getUnitId());
		query.setParameter("itemId", PurchaseOrdDto.getItemId()!=null ? PurchaseOrdDto.getItemId() : 0);
		return query.list();
	}

	@Override
	public PurchaseOrder getPurchaseOrder(Integer id) {
		String hql = "SELECT purchaseOrder FROM PurchaseOrder AS purchaseOrder "
				+ "WHERE purchaseOrder.id = :id  AND purchaseOrder.isActive = true AND purchaseOrder.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (PurchaseOrder) query.uniqueResult();
	}

	@Override
	public List<PurchaseOrderDetail> getPurchaseOrderDetail(Integer id) {
		String hql = "SELECT purchaseOrderDetail FROM PurchaseOrderDetail AS purchaseOrderDetail "
				+ " WHERE purchaseOrderDetail.purchaseOrder.id = :id  AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false "
				+ " ORDER BY purchaseOrderDetail.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return query.list();
	}

	@Override
	public List<PurchaseOrderUomMapper> getPurchaseOrderUomMapper(Integer id) {
		String hql = "SELECT purchaseOrderUomMapper FROM PurchaseOrderUomMapper AS purchaseOrderUomMapper "
				+ " WHERE purchaseOrderUomMapper.purchaseOrderDetail.id = :id "
				+ " ORDER BY purchaseOrderUomMapper.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public List<BonusUomMapper> getBonusUomMapper(Integer id) {
		String hql = "SELECT bonusUomMapper FROM BonusUomMapper AS bonusUomMapper "
				+ " WHERE bonusUomMapper.purchaseOrderDetail.id = :id  " + " ORDER BY bonusUomMapper.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public List<PurchaseOrderOtherChargesMapper> getPoOtherChargesMapper(Integer id) {
		String hql = "SELECT poOtherChargesMapper FROM PurchaseOrderOtherChargesMapper AS poOtherChargesMapper "
				+ " WHERE poOtherChargesMapper.purchaseOrderDetail.id = :id  AND poOtherChargesMapper.isActive = true AND poOtherChargesMapper.isDeleted = false "
				+ " ORDER BY poOtherChargesMapper.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public List<PurchaseOrderStaged> getPoStaged(Integer id) {
		String hql = "SELECT purchaseOrderStaged FROM PurchaseOrderStaged AS purchaseOrderStaged "
				+ " WHERE purchaseOrderStaged.purchaseOrderDetail.id = :id  AND purchaseOrderStaged.isActive = true AND purchaseOrderStaged.isDeleted = false "
				+ " ORDER BY purchaseOrderStaged.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public List<PoAccessoriesMapper> getPoAccessoriesMapper(Integer id) {
		String hql = "SELECT poAccessoriesMapper FROM PoAccessoriesMapper AS poAccessoriesMapper "
				+ " WHERE poAccessoriesMapper.purchaseOrderDetail.id = :id  AND poAccessoriesMapper.isActive = true AND poAccessoriesMapper.isDeleted = false "
				+ " ORDER BY poAccessoriesMapper.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return query.list();
	}

	@Override
	public List<PoPurchaseRequestArray> getPurchaseRequestTypeId(Integer purchaseOrderDetailId) {

		String hql = " SELECT purchaseRequestDetail.id AS id, purchaseRequestDetail.approvedQuantity AS approvedQuantity ,"
				+ " purchaseRequest.purchaseNo AS prNo " + " FROM PurchaseRequestDetail AS purchaseRequestDetail "
				+ " INNER JOIN purchaseRequestDetail.purchaseOrderDetails AS purchaseOrderDetail WITH purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false "
				+ " INNER JOIN purchaseRequestDetail.purchaseRequest AS purchaseRequest WITH purchaseRequest.isActive = true AND purchaseRequest.isDeleted = false "
				+ " WHERE purchaseOrderDetail.id = :purchaseOrderDetailId AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.isDeleted = false "
				+ " ORDER BY purchaseRequestDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("purchaseOrderDetailId", purchaseOrderDetailId);
		query.setResultTransformer(Transformers.aliasToBean(PoPurchaseRequestArray.class));
		return query.list();
	}

	@Override
	public List<PurchaseRequestDetail> getPurchaseRequestDetails(List<Integer> purchaseRequestDetailIds) {
		String hql = "SELECT purchaseRequestDetail FROM PurchaseRequestDetail AS purchaseRequestDetail \r\n"
				+ "WHERE purchaseRequestDetail.id in :purchaseRequestDetailIds "
				+ "		AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.isDeleted = false";

		Query query = getSession().createQuery(hql);
		query.setParameterList("purchaseRequestDetailIds", purchaseRequestDetailIds);
		return query.list();
	}

	@Override
	public PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder) {
		getSession().save(purchaseOrder);
		return purchaseOrder;

	}

	@Override
	public PurchaseOrder updatePurchaseOrder(PurchaseOrder purchaseOrder) {
		getSession().update(purchaseOrder);
		return purchaseOrder;
	}

	@Override
	public PurchaseOrderMasterDetailsDto getPurchaseOrderBP(Integer id) {
		String hql = "SELECT purchaseOrder.id as id, "
				+ "purchaseOrder.poNumber as poNumber, purchaseOrder.discount as discount, purchaseOrder.isConsignment as isConsignment,"
				+ "purchaseOrder.netAmount as netAmount, purchaseOrder.roundOffAmount as roundOffAmount, purchaseOrder.totalAmount as totalAmount,"
				+ "purchaseOrder.totalDiscount as totalDiscount, purchaseOrder.totalOtherAmount as totalOtherAmount,"
				+ "purchaseOrder.totalTaxAmount as totalTaxAmount, purchaseOrder.paymentTerm as paymentTerm, purchaseOrder.remark as remark, purchaseOrder.addedDate as addedDate,"
				+ "to_char(purchaseOrder.dueDate,'YYYY-MM-dd') as dueDate,"
				+ "assetType.id as assetTypeId, assetType.type as assetType,"
				+ "requestType.id as poTypeId, requestType.type as purchaseType,"
				+ "discountType.id as discountTypeId, discountType.type as discountType,"
				+ "status.id as statusId, status.status as status,"
				// + "status.id as approvalStatusId, status.status as approvalStatus,"
				+ " approvalStatus.id as approvalStatusId, approvalStatus.status as approvalStatus, "
				+ "store.storeId as storeId, store.storeName as StoreName,"
				+ "vendor.id as vendorId, vendor.name as vendorName," + "addedBy.userName as addedBy , "
				+ "requestingStore.storeId as requestingStoreId, requestingStore.storeName as requestingStoreName , reviewedBy.userId as reviewedBy"
				+ " FROM PurchaseOrder AS purchaseOrder " + " LEFT JOIN purchaseOrder.assetType AS assetType "
				+ " LEFT JOIN purchaseOrder.requestType AS requestType "
				+ " LEFT JOIN purchaseOrder.discountType AS discountType "
				+ " LEFT JOIN purchaseOrder.reviewedBy AS reviewedBy "
				+ " LEFT JOIN purchaseOrder.status AS status "
				+ " LEFT JOIN purchaseOrder.approvalStatus as approvalStatus "// added to resolve HIS-9504
				+ " LEFT JOIN purchaseOrder.store AS store "
				+ " LEFT JOIN purchaseOrder.requestingStore AS requestingStore "
				+ " LEFT JOIN purchaseOrder.vendor AS vendor " + " LEFT JOIN purchaseOrder.addedBy AS addedBy "
				+ "WHERE purchaseOrder.id = :id  AND purchaseOrder.isActive = true AND purchaseOrder.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		query.setResultTransformer(Transformers.aliasToBean(PurchaseOrderMasterDetailsDto.class));
		return (PurchaseOrderMasterDetailsDto) query.uniqueResult();
	}

	@Override
	public List<PurchaseOrderDetailsRespDto> getPoDetailsConsumablesByPoIdBP(Integer id) {

		String hql = "SELECT purchaseOrderDetail.id as id, item.id as itemId, item.code as itemCode, item.name as itemName, "
				+ " purchaseOrderDetail.poQuantity as poQuantity, poUomType.id as poUomTypeId, poUomType.type as poUomType, "
				+ " poUomUnit.id as poUomUnitId, poUomUnit.units as poUomUnit, purchaseOrderDetail.bonusQuantity as bonusQuantity, "
				+ " bonusUomType.id as bonusUomTypeId, bonusUomType.type as bonusUomType, bonusUomUnit.id as bonusUomUnitId, bonusUomUnit.units as bonusUomUnit, "
				+ " purchaseOrderDetail.cop as cop, purchaseOrderDetail.mrp as mrp, purchaseOrderDetail.discount as discount, "
				+ " tax.id as taxId, tax.name as taxCode, purchaseOrderDetail.taxPercentage as taxPercentage, "
				+ " purchaseOrderDetail.totalAmount as totalAmount, purchaseOrderDetail.totalOtherAmount as totalOtherAmount, "
				+ " purchaseOrderDetail.taxAmount as taxAmount, purchaseOrderDetail.netAmount as netAmount, purchaseOrderDetail.totalDiscount as totalDiscount, "
				+ " status.id as statusId, status.status as status, manufacturer.id as manufacturerId, manufacturer.name as manufacturerName, "
				+ " purchaseOrderDetail.specification as specification, purchaseOrderDetail.remark as remark, "
				+ " purchaseOrderDetail.poApprovedQuantity as approvedQuantity, purchaseOrderDetail.bonusApprovedQuantity as bonusApprovedQuantity, "
				+ " purchaseOrderDetail.rejectedQuantity as rejectQuantity, store.storeId as storeId, assetType.id as assetTypeId , purchaseOrderDetail.quotationRequestDetail.id as qrdid "
				+ " , serviceProvider.id as serviceProviderId ,serviceProvider.name as serviceProvider , purchaseOrderDetail.warrantyPeriod  as warrantyPeriod "
				+ " , purchaseOrderDetail.supportTerms as supportTerms , purchaseOrderDetail.technicalSpecification as technicalSpecification , "
				+ " purchaseOrderDetail.expectedPayment as expectedPayment, purchaseOrderDetail.warrantyDate as warrantyDate , purchaseOrderDetail.warrantyPeriod as warrantyPeriod "
				+ " , purchaseOrderDetail.pendingQuantity as pendingQuantity,purchaseOrderDetail.roundOffAmount as roundOffAmount,purchaseOrderDetail.totalNetAmount as totalNetAmount, item.isThreeUOMApplicable as isThreeUOMApplicable  FROM PurchaseOrderDetail AS purchaseOrderDetail "
				+ " INNER JOIN purchaseOrderDetail.purchaseOrder AS purchaseOrder "
				+ " LEFT JOIN purchaseOrder.store AS store " + " LEFT JOIN purchaseOrder.assetType AS assetType "
				+ " LEFT JOIN purchaseOrderDetail.item AS item "
				+ " LEFT JOIN purchaseOrderDetail.poUomType AS poUomType "
				+ " LEFT JOIN purchaseOrderDetail.poUomUnit AS poUomUnit "
				+ " LEFT JOIN purchaseOrderDetail.bonusUomType AS bonusUomType "
				+ " LEFT JOIN purchaseOrderDetail.bonusUomUnit AS bonusUomUnit "
				+ " LEFT JOIN purchaseOrderDetail.tax AS tax " + " LEFT JOIN purchaseOrderDetail.status AS status  "
				+ " LEFT JOIN purchaseOrderDetail.manufacturer AS manufacturer "
				+ " LEFT JOIN purchaseOrderDetail.serviceProvider AS serviceProvider "
				+ " WHERE purchaseOrder.id = :id  AND purchaseOrder.isActive = true AND purchaseOrder.isDeleted = false"
				+ " AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false order by purchaseOrderDetail.id";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		query.setResultTransformer(Transformers.aliasToBean(PurchaseOrderDetailsRespDto.class));

		return query.list();

	}

	// create by minesh for consignment po
	@Override
	public List<PurchaseOrder> searchConsignmentStockPurchaseOrderList(PurchaseOrderMasterDto PurchaseOrdDto,
			Integer screenId) {
		String hql = "SELECT purchaseOrder FROM PurchaseOrder AS purchaseOrder "
				// + " INNER JOIN purchaseOrder.addedBy as user WITH user.unitId = :unitId "
				+ " LEFT JOIN purchaseOrder.store as store WITH store.unit.unitId = :unitId "
				+ " LEFT JOIN purchaseOrder.requestingStore as requestingStore "
				+ "	LEFT JOIN purchaseOrder.status as status "
				+ "	LEFT JOIN purchaseOrder.approvalStatus as approvalStatus "
				+ " LEFT JOIN purchaseOrder.assetType as assetType "
				+ " LEFT JOIN purchaseOrder.requestType as requestType " + " LEFT JOIN purchaseOrder.vendor as vendor "
				+ " WHERE purchaseOrder.isActive = true AND purchaseOrder.isDeleted = false AND purchaseOrder.unit.unitId = :unitId "

				+ " AND purchaseOrder.isConsignment = true "

				+ " AND purchaseOrder.addedDate BETWEEN :fromDate AND :toDate "
				+ "	AND (:storeId = 0 OR store.id = :storeId) "
				+ "	AND (:requestingStoreId = 0 OR requestingStore.id = :requestingStoreId) "
				+ " AND (:poNumber = '' OR lower(purchaseOrder.poNumber) LIKE :poNumber) "
				+ " AND (:vendorName = '' OR lower(purchaseOrder.vendor.name) LIKE :vendorName) "
				+ " AND (:statusId = 0 OR status.id = :statusId) "
				/*
				 * + "	AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus " +
				 * "						INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				 * + "						INNER JOIN screenStatus.status as status) "
				 */
				+ " ORDER BY purchaseOrder.id DESC";

		// + " AND (:vendorName = '' OR lower(purchaseOrder.vendor.name) LIKE
		// :vendorName) OR lower(purchaseOrder.vendor.code) LIKE :vendorName )"

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(PurchaseOrdDto.getFromDate()));
		query.setParameter("toDate", Util.getDate(PurchaseOrdDto.getToDate()));
		query.setParameter("storeId", PurchaseOrdDto.getStoreId() != null ? PurchaseOrdDto.getStoreId() : 0);
		query.setParameter("requestingStoreId",
				PurchaseOrdDto.getRequestingStoreId() != null ? PurchaseOrdDto.getRequestingStoreId() : 0);
		query.setParameter("poNumber",
				PurchaseOrdDto.getPoNumber() != null ? '%'+PurchaseOrdDto.getPoNumber().toLowerCase() + '%' : "");
		query.setParameter("vendorName",
				PurchaseOrdDto.getVendorName() != null ? PurchaseOrdDto.getVendorName().toLowerCase() + '%' : "");
		query.setParameter("statusId", PurchaseOrdDto.getStatusId() != null ? PurchaseOrdDto.getStatusId() : 0);
		// query.setParameter("screenId", screenId);
		query.setParameter("unitId",PurchaseOrdDto.getUnitId()!=null ? PurchaseOrdDto.getUnitId(): sessionService.getUnitId());
		return query.list();
	}

	@Override
	public List<PurchaseOrder> searchNonConsignmentPurchaseOrderList(PurchaseOrderMasterDto PurchaseOrdDto,
			Integer screenId) {
		String hql = "SELECT purchaseOrder FROM PurchaseOrder AS purchaseOrder "
				+ " 	INNER JOIN purchaseOrder.addedBy as user WITH user.unitId = :unitId "
				+ " 	LEFT JOIN purchaseOrder.store as store WITH store.unit.unitId = :unitId "
				+ " 	LEFT JOIN purchaseOrder.requestingStore as requestingStore"

				+ "		LEFT JOIN purchaseOrder.status as status "
				+ "		LEFT JOIN purchaseOrder.approvalStatus as approvalStatus "
				+ " 	LEFT JOIN purchaseOrder.assetType as assetType "
				+ " 	LEFT JOIN purchaseOrder.requestType as requestType "
				+ " 	LEFT JOIN purchaseOrder.vendor as vendor "
				+ " WHERE purchaseOrder.isActive = true AND purchaseOrder.isDeleted = false AND purchaseOrder.unit.unitId = :unitId "
				// start code for get only normal po (not consignment po).
				+ " AND purchaseOrder.isConsignment = false "
				// end code for get only normal po (not consignment po).
				+ " 	AND purchaseOrder.addedDate BETWEEN :fromDate AND :toDate "
				+ "		AND (:storeId = 0 OR store.id = :storeId) "
				+ "		AND (:requestingStoreId = 0 OR requestingStore.id = :requestingStoreId) "
				+ " 	AND (:poNumber = '' OR lower(purchaseOrder.poNumber) LIKE :poNumber) "
				+ " 	AND (:vendorName = '' OR lower(purchaseOrder.vendor.name) LIKE :vendorName) "
				+ " 	AND (:statusId = 0 OR status.id = :statusId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " ORDER BY purchaseOrder.id DESC";

		// + " AND (:vendorName = '' OR lower(purchaseOrder.vendor.name) LIKE
		// :vendorName) OR lower(purchaseOrder.vendor.code) LIKE :vendorName )"

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(PurchaseOrdDto.getFromDate()));
		query.setParameter("toDate", Util.getDate(PurchaseOrdDto.getToDate()));
		query.setParameter("storeId", PurchaseOrdDto.getStoreId() != null ? PurchaseOrdDto.getStoreId() : 0);
		query.setParameter("requestingStoreId",
				PurchaseOrdDto.getRequestingStoreId() != null ? PurchaseOrdDto.getRequestingStoreId() : 0);
		query.setParameter("poNumber",
				PurchaseOrdDto.getPoNumber() != null ? PurchaseOrdDto.getPoNumber().toLowerCase() + '%' : "");
		query.setParameter("vendorName",
				PurchaseOrdDto.getVendorName() != null ? PurchaseOrdDto.getVendorName().toLowerCase() + '%' : "");
		query.setParameter("statusId", PurchaseOrdDto.getStatusId() != null ? PurchaseOrdDto.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", sessionService.getUnitId());
		return query.list();
	}

	@Override
	public List<PurchaseOrderDetailsRespDto> getConsignmentStockPoDetailsConsumablesByPoIdBP(Integer id) {

		String hql = "SELECT purchaseOrderDetail.id as id, item.id as itemId, item.code as itemCode, item.name as itemName, "
				+ " purchaseOrderDetail.poQuantity as poQuantity, poUomType.id as poUomTypeId, poUomType.type as poUomType, "
				+ " poUomUnit.id as poUomUnitId, poUomUnit.units as poUomUnit, purchaseOrderDetail.bonusQuantity as bonusQuantity, "
				+ " bonusUomType.id as bonusUomTypeId, bonusUomType.type as bonusUomType, bonusUomUnit.id as bonusUomUnitId, bonusUomUnit.units as bonusUomUnit, "
				+ " purchaseOrderDetail.cop as cop, purchaseOrderDetail.mrp as mrp, purchaseOrderDetail.discount as discount, "
				+ " tax.id as taxId, tax.name as taxCode, purchaseOrderDetail.taxPercentage as taxPercentage, "
				+ " purchaseOrderDetail.totalAmount as totalAmount, purchaseOrderDetail.totalOtherAmount as totalOtherAmount, "
				+ " purchaseOrderDetail.taxAmount as taxAmount, purchaseOrderDetail.netAmount as netAmount, purchaseOrderDetail.totalDiscount as totalDiscount, "
				+ " status.id as statusId, status.status as status, manufacturer.id as manufacturerId, manufacturer.name as manufacturerName, "
				+ " purchaseOrderDetail.specification as specification, purchaseOrderDetail.remark as remark, "
				+ " purchaseOrderDetail.poApprovedQuantity as approvedQuantity, purchaseOrderDetail.bonusApprovedQuantity as bonusApprovedQuantity, "
				+ " purchaseOrderDetail.rejectedQuantity as rejectQuantity, store.storeId as storeId, assetType.id as assetTypeId , purchaseOrderDetail.quotationRequestDetail.id as qrdid "

				+ " ,goodReceiptNote.id AS grnId, goodReceiptNote.grnNumber AS grnNumber, goodReceiptNote.addedDate as grnDate, "
				+ " goodReceiptNoteDetail.id as grnDetailId, goodReceiptNoteDetail.batch.id AS batchId, goodReceiptNoteDetail.batchNo AS batchNo ,goodReceiptNoteDetail.lotNumber AS lotNo "

				+ " FROM PurchaseOrderDetail AS purchaseOrderDetail "
				+ " INNER JOIN purchaseOrderDetail.purchaseOrder AS purchaseOrder "
				+ " INNER JOIN purchaseOrderDetail.grnDetail AS goodReceiptNoteDetail "
				+ " INNER JOIN goodReceiptNoteDetail.goodReceiptNote AS goodReceiptNote "
				+ " LEFT JOIN purchaseOrder.store AS store " + " LEFT JOIN purchaseOrder.assetType AS assetType "
				+ " LEFT JOIN purchaseOrderDetail.item AS item "
				+ " LEFT JOIN purchaseOrderDetail.poUomType AS poUomType "
				+ " LEFT JOIN purchaseOrderDetail.poUomUnit AS poUomUnit "
				+ " LEFT JOIN purchaseOrderDetail.bonusUomType AS bonusUomType "
				+ " LEFT JOIN purchaseOrderDetail.bonusUomUnit AS bonusUomUnit "
				+ " LEFT JOIN purchaseOrderDetail.tax AS tax " + " LEFT JOIN purchaseOrderDetail.status AS status  "
				+ " LEFT JOIN purchaseOrderDetail.manufacturer AS manufacturer "
				+ " WHERE purchaseOrder.id = :id  AND purchaseOrder.isActive = true AND purchaseOrder.isDeleted = false"
				+ " AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false order by purchaseOrderDetail.id";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		query.setResultTransformer(Transformers.aliasToBean(PurchaseOrderDetailsRespDto.class));

		return query.list();

	}

	@Override
	public BigInteger getTotalPOApprovedQuantityAgainGRNDetail(Integer id , Integer unitId) {
		Integer unit=unitId!=null && unitId > 0 ? unitId : sessionService.getUnitId();
		String sql = "select COALESCE(SUM(po_approved_quantity), 0) from procurement.t_purchase_order_detail where grn_detail_id ="
				+ id + " and unit_id =" +unit ;
		Query query = getSession().createSQLQuery(sql);
		return (BigInteger) query.uniqueResult();
	}

	public List<ConsignmentStockPOHistoryDto> getConsignmentStockPOHistory(Integer grnDetailId , Integer unitId) {
	    Integer unit= unitId!=null && unitId > 0 ? unitId :sessionService.getUnitId();
		String sql = "select po.po_number as \"poNumber\", po.added_date as \"poDate\", podetail.po_quantity as \"poApprovedQuantity\", podetail.cop as \"cop\" ,podetail.mrp as \"mrp\" "
				+ " from procurement.t_purchase_order_detail as podetail"
				+ " inner join procurement.t_purchase_order as po on po.id=podetail.purchase_order_id "
				+ " where podetail.grn_detail_id =" + grnDetailId + " and podetail.is_from_consignment_po_creation=true and podetail.unit_id="
				+ unit + " order by podetail.id desc;";
		Query query = getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.aliasToBean(ConsignmentStockPOHistoryDto.class));
		return query.list();
	}

	// end code by minesh

	@Override
	public List<PurchaseOrderDetailsRespDto> getPoDetailsByAssetsPoIdBP(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PoPurchaseRequestArray> getPurchaseRequestTypeIdBP(List<Integer> detailsIds) {
		String hql = " SELECT purchaseRequestDetail.id AS id, purchaseRequestDetail.approvedQuantity AS approvedQuantity ,"
				+ " purchaseRequest.purchaseNo AS prNo, purchaseOrderDetail.id as poDetailId "
				+ " FROM PurchaseRequestDetail AS purchaseRequestDetail "
				+ " INNER JOIN purchaseRequestDetail.purchaseOrderDetails AS purchaseOrderDetail WITH purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false "
				+ " INNER JOIN purchaseRequestDetail.purchaseRequest AS purchaseRequest WITH purchaseRequest.isActive = true AND purchaseRequest.isDeleted = false "
				+ " WHERE purchaseOrderDetail.id in (:purchaseOrderDetailId) AND purchaseRequestDetail.isActive = true AND purchaseRequestDetail.isDeleted = false "
				+ " ORDER BY purchaseRequestDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("purchaseOrderDetailId", detailsIds);
		query.setResultTransformer(Transformers.aliasToBean(PoPurchaseRequestArray.class));
		return query.list();
	}

	@Override
	public List<PurchaseOrderUomMapperDto> getPurchaseOrderUomMapperBP(List<Integer> detailsIds) {

		String hql = "SELECT purchaseOrderUomMapper.id as id, "
				+ " uomType.type as uomType, uomType.id as uomTypeId, uomUnit.id as uomUnitId, uomUnit.units as uomUnit, "
				+ " purchaseOrderUomMapper.conversions as conversion, purchaseOrderUomMapper.ipUom as ipUom, purchaseOrderUomMapper.opUom as opUom, "
				+ " purchaseOrderUomMapper.storeUom as storeUom, purchaseOrderUomMapper.purchaseOrderDetail.id as poDetailId "
				+ " FROM PurchaseOrderUomMapper AS purchaseOrderUomMapper "
				+ " LEFT JOIN purchaseOrderUomMapper.uomType as uomType "
				+ " LEFT JOIN purchaseOrderUomMapper.uomUnit as uomUnit "
				+ " WHERE purchaseOrderUomMapper.purchaseOrderDetail.id in (:id) "
				+ " ORDER BY purchaseOrderUomMapper.id ";
		Query query = getSession().createQuery(hql);
		query.setParameterList("id", detailsIds);

		query.setResultTransformer(Transformers.aliasToBean(PurchaseOrderUomMapperDto.class));

		return query.list();
	}

	@Override
	public List<PurchaseOrderUomMapperDto> getBonusUomMapperBP(List<Integer> detailsIds) {

		String hql = "SELECT bonusUomMapper.id as id, bonusUomMapper.conversions as conversion, "
				+ "bonusUomMapper.ipUom as ipUom, bonusUomMapper.opUom as opUom, bonusUomMapper.storeUom as storeUom, "
				+ "uomType. id as uomTypeId, uomType.type as uomType, "
				+ "uomUnit.id as uomUnitId, uomUnit.units as uomUnit, "
				+ "bonusUomMapper.purchaseOrderDetail.id as poDetailId " + " FROM BonusUomMapper AS bonusUomMapper "
				+ " LEFT JOIN bonusUomMapper.uomType as uomType " + " LEFT JOIN bonusUomMapper.uomUnit as uomUnit "
				+ " WHERE bonusUomMapper.purchaseOrderDetail.id in (:id)  " + " ORDER BY bonusUomMapper.id ";
		Query query = getSession().createQuery(hql);
		query.setParameterList("id", detailsIds);

		query.setResultTransformer(Transformers.aliasToBean(PurchaseOrderUomMapperDto.class));

		return query.list();
	}

	@Override
	public List<PoOtherChargesMapperDto> getPoOtherChargesMapperBP(Integer poId) {

		String hql = "SELECT poOtherChargesMapper.amount as amount, otherCharge.id as otherChargesId, otherCharge.description as otherCharges, "
				// + " poOtherChargesMapper.purchaseOrder.id as poId "
				+ " purchaseOrder.id as poId " + " ,poOtherChargesMapper.id as id,poOtherChargesMapper.isFromPo as isFromPo "
				+ " FROM PurchaseOrderOtherChargesMapper AS poOtherChargesMapper "
				+ " INNER JOIN poOtherChargesMapper.purchaseOrder as purchaseOrder "
				+ " LEFT JOIN poOtherChargesMapper.otherCharge as otherCharge "
				// + " WHERE poOtherChargesMapper.purchaseOrder.id =:id AND
				// poOtherChargesMapper.isActive = true AND poOtherChargesMapper.isDeleted =
				// false "
				+ " WHERE purchaseOrder.id =:id  AND poOtherChargesMapper.isActive = true AND poOtherChargesMapper.isDeleted = false AND poOtherChargesMapper.isFromPo=true "
				+ " ORDER BY poOtherChargesMapper.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", poId);

		query.setResultTransformer(Transformers.aliasToBean(PoOtherChargesMapperDto.class));

		return query.list();
	}

	@Override
	public List<PurchaseOrderStagedDto> getPoStagedBP(List<Integer> detailsIds) {

		String hql = "SELECT purchaseOrderStaged.quantity as quantity, purchaseOrderStaged.approvedQunatity as approvedQuantity, purchaseOrderStaged.id as id, purchaseOrderStaged.stagedDate as stagedDate, purchaseOrderStaged.purchaseOrderDetail.id as poDetailId FROM PurchaseOrderStaged AS purchaseOrderStaged "
				+ " WHERE purchaseOrderStaged.purchaseOrderDetail.id in (:id)  AND purchaseOrderStaged.isActive = true AND purchaseOrderStaged.isDeleted = false "
				+ " ORDER BY purchaseOrderStaged.id ";
		Query query = getSession().createQuery(hql);
		query.setParameterList("id", detailsIds);

		query.setResultTransformer(Transformers.aliasToBean(PurchaseOrderStagedDto.class));

		return query.list();
	}

	@Override
	public List<com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper> getPoAccessoriesMapperBP(
			List<Integer> consumabelItemIds) {

		String hql = "SELECT poAccessoriesMapper. id as id, "
				+ "item.id as itemId, item.name as itemName, item.code as itemCode, poAccessoriesMapper.purchaseOrderDetail.id as poDetailId "
				+ " FROM PoAccessoriesMapper AS poAccessoriesMapper " + " LEFT JOIN poAccessoriesMapper.item as item "
				+ " WHERE poAccessoriesMapper.purchaseOrderDetail.id in (:id)  AND poAccessoriesMapper.isActive = true AND poAccessoriesMapper.isDeleted = false "
				+ " ORDER BY poAccessoriesMapper.id ";
		Query query = getSession().createQuery(hql);
		query.setParameterList("id", consumabelItemIds);

		query.setResultTransformer(
				Transformers.aliasToBean(com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper.class));

		return query.list();
	}

	@Override
	public List<PurchaseOrder> getPurchaseOrders(List<Integer> poIds) {
		String hql = "SELECT purchaseOrder FROM PurchaseOrder AS purchaseOrder "
				+ "WHERE purchaseOrder.id in (:id)  AND purchaseOrder.isActive = true AND purchaseOrder.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameterList("id", poIds);

		return query.list();
	}

	@Override
	public List<PurchaseOrderDetail> getPurchaseOrderDetails(List<Integer> poIds) {
		String hql = "SELECT purchaseOrderDetail FROM PurchaseOrderDetail AS purchaseOrderDetail "
				+ " WHERE purchaseOrderDetail.purchaseOrder.id in (:id)  AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false "
				+ " ORDER BY purchaseOrderDetail.id ";
		Query query = getSession().createQuery(hql);
		query.setParameterList("id", poIds);
		return query.list();
	}

	@Override
	public List<QrDateDto> getqrDetails(Integer qrdid) {

		String hql = "  SELECT qr.id as id , qr.qr_no as qrno , qr.added_date as qrdate FROM procurement.t_quotation_request qr join procurement.t_quotation_request_detail qrd on qr.id = qrd.qr_id where qrd.id="
				+ qrdid;
		Query query = getSession().createSQLQuery(hql);
		query.setResultTransformer(Transformers.aliasToBean(QrDateDto.class));
		return query.list();

	}

	@Override
	public List<LastPurchaseOrderDto> lastPurchaseOrderList(Integer itemid, Integer vendorid,Integer poType) {

		/*String hql = "SELECT rtype.type as retype , po.po_number as poNumber , po.added_date as podate , pod.po_quantity  as quantity , uomunit.unit  as quantityuom , pod.bonus_quantity as bonusquantity, uomunit.unit as bonusuomunit , pod.cop as cop, pod.mrp as mrp , pod.tax_percentage as taxpercentage, pod.net_amount as netamount , pod.discount as discount , pod.total_discount  as totaldiscount , pod.tax_amount  as taxamount , pod.total_other_amount as otheramount, pod.total_amount  as totalamount FROM procurement.t_purchase_order po join procurement.t_purchase_order_detail pod on po.id = pod.purchase_order_id join public.m_request_type rtype on po.po_type_id = rtype.id join public.m_item itm on pod.item_id = itm.id join public.m_uom_unit  uomunit on pod.po_uom_unit_id =uomunit.id  join public.m_uom_type uomtype on pod.po_uom_type_id = uomtype.id where po.vendor_id ="
				+ vendorid + " and pod.item_id =" + itemid;
		*/
		if(poType==1)
		{
			String hql = "SELECT mv.name as \"vendorName\", rtype.type as retype , po.po_number as poNumber , po.added_date as podate , pod.po_quantity  as quantity , uomunit.unit  as quantityuom , pod.bonus_quantity as bonusquantity, uomunit.unit as bonusuomunit , pod.cop as cop, pod.mrp as mrp , pod.tax_percentage as taxpercentage, pod.net_amount as netamount , pod.discount as discount , pod.total_discount  as totaldiscount , pod.tax_amount  as taxamount , pod.total_other_amount as otheramount, pod.total_amount  as totalamount FROM procurement.t_purchase_order po join procurement.t_purchase_order_detail pod on po.id = pod.purchase_order_id join public.m_request_type rtype on po.po_type_id = rtype.id join public.m_item itm on pod.item_id = itm.id join public.m_uom_unit  uomunit on pod.po_uom_unit_id =uomunit.id  join public.m_uom_type uomtype on pod.po_uom_type_id = uomtype.id join public.m_vendor mv  on po.vendor_id  = mv.id where  "
					+ "   pod.item_id =" + itemid +"  limit 10 ";
			
			Query query = getSession().createSQLQuery(hql);
			query.setResultTransformer(Transformers.aliasToBean(LastPurchaseOrderDto.class));
			return query.list();
		}
		else
		{
		String hql = "SELECT mv.name as \"vendorName\", rtype.type as retype , po.po_number as poNumber , po.added_date as podate , pod.po_quantity  as quantity , pod.bonus_quantity as bonusquantity , pod.cop as cop, pod.mrp as mrp , pod.tax_percentage as taxpercentage, pod.net_amount as netamount , pod.discount as discount , pod.total_discount  as totaldiscount , pod.tax_amount  as taxamount , pod.total_other_amount as otheramount, pod.total_amount  as totalamount FROM procurement.t_purchase_order po join procurement.t_purchase_order_detail pod on po.id = pod.purchase_order_id join public.m_request_type rtype on po.po_type_id = rtype.id join public.m_item itm on pod.item_id = itm.id  join public.m_vendor mv  on po.vendor_id  = mv.id where  "
				+ "   pod.item_id =" + itemid +"  limit 10 ";
		
		Query query = getSession().createSQLQuery(hql);
		query.setResultTransformer(Transformers.aliasToBean(LastPurchaseOrderDto.class));
		return query.list();
		}

	}

	@Override
	public PurchaseOrderDetail getPurchaseOrderDetailById(int id) {

		String hql = "SELECT purchaseOrderDetail FROM PurchaseOrderDetail AS purchaseOrderDetail "
				+ " WHERE purchaseOrderDetail.grnDetail.id = :id  AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false "
				+ " ORDER BY purchaseOrderDetail.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return (PurchaseOrderDetail) query.uniqueResult();

	}

	@Override
	public PurchaseOrderOtherChargesMapper getPoOtherChargesMapperById(Integer id) {

		String hql = "SELECT poOtherChargesMapper FROM PurchaseOrderOtherChargesMapper AS poOtherChargesMapper "
				+ " WHERE poOtherChargesMapper.id = :id  AND poOtherChargesMapper.isActive = true AND poOtherChargesMapper.isDeleted = false "
				+ " ORDER BY poOtherChargesMapper.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (PurchaseOrderOtherChargesMapper) query.uniqueResult();

	}

	@Override
	public void updateOtherCharges(PurchaseOrderOtherChargesMapper purchaseOrderOtherChargesMapper) {

		getSession().saveOrUpdate(purchaseOrderOtherChargesMapper);

	}

	@Override
	public void updateTaxId(Integer id, Integer taxId,Integer purchaseOrderId) {
		sessionFactory.getCurrentSession().createSQLQuery(" update procurement.t_purchase_order_detail set tax_id="+taxId+" where  item_id="+id+" and purchase_order_id="+purchaseOrderId+"")
        .executeUpdate();
		
	}

	@Override
	public void updateOtherCharges(Integer id) {
		sessionFactory.getCurrentSession().createSQLQuery(" update procurement.t_other_charges_po_mapper set is_active=false and is_deleted=true where  purchase_order_id="+id)
        .executeUpdate();
		
	}

	@Override
	public boolean getFinalPOApprovalFlag(Integer unitId) {
		String hql = "SELECT is_skip_po_approval from public.m_application_configuration where unit_id="+unitId+" and is_active=true and is_deleted=false ";
		Query query = getSession().createSQLQuery(hql);
		return (boolean) query.uniqueResult();
	}

	@Override
	public List<PoOtherChargesMapperDto> getPoOtherChargesMapperBPPoDetails(Integer id) {
		String hql = "SELECT poOtherChargesMapper.amount as amount, otherCharge.id as otherChargesId, otherCharge.description as otherCharges, "
				// + " poOtherChargesMapper.purchaseOrder.id as poId "
				+ " purchaseOrder.id as poId " + " ,poOtherChargesMapper.id as id,poOtherChargesMapper.isFromPo as isFromPo "
				+ " FROM PurchaseOrderOtherChargesMapper AS poOtherChargesMapper "
				+ " INNER JOIN poOtherChargesMapper.purchaseOrder as purchaseOrder "
				+ " LEFT JOIN poOtherChargesMapper.otherCharge as otherCharge "
				// + " WHERE poOtherChargesMapper.purchaseOrder.id =:id AND
				// poOtherChargesMapper.isActive = true AND poOtherChargesMapper.isDeleted =
				// false "
				+ " WHERE purchaseOrder.id =:id  AND poOtherChargesMapper.isActive = true AND poOtherChargesMapper.isDeleted = false "
				+ " ORDER BY poOtherChargesMapper.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		query.setResultTransformer(Transformers.aliasToBean(PoOtherChargesMapperDto.class));

		return query.list();
	}
	
	@Override
	public List<PurchaseOrderStagedDto> getInitialPurchaseOrderStagedDto(List<Integer> detailsIds) {

		String hql = "SELECT purchaseOrderStaged.quantity as quantity, purchaseOrderStaged.approvedQunatity as approvedQuantity, purchaseOrderStaged.id as id, purchaseOrderStaged.stagedDate as stagedDate, purchaseOrderStaged.purchaseOrderDetail.id as poDetailId FROM PurchaseOrderStaged AS purchaseOrderStaged "
				+ " WHERE purchaseOrderStaged.purchaseOrderDetail.id in (:id)  AND purchaseOrderStaged.isActive = true "
				+ " ORDER BY purchaseOrderStaged.id ";
		Query query = getSession().createQuery(hql);
		query.setParameterList("id", detailsIds);

		query.setResultTransformer(Transformers.aliasToBean(PurchaseOrderStagedDto.class));

		return query.list();
	}

}

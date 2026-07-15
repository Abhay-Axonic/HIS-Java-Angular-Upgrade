package com.param.procurement.purchase.dao.goodReceiptNote;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.param.entity.model.master.DiscountType;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.apache.log4j.Logger;

import com.param.common.constants.Status;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.ApplicationConfiguration;
import com.param.entity.model.procurement.GoodReceiptNote;
import com.param.entity.model.procurement.GoodReceiptNoteDetail;
import com.param.entity.model.procurement.GoodReceiptNoteDetailLog;
import com.param.entity.model.procurement.GrnAccessory;
import com.param.entity.model.procurement.GrnBonusUomMapper;
import com.param.entity.model.procurement.GrnOtherChargesMapper;
import com.param.entity.model.procurement.GrnSettlement;
import com.param.entity.model.procurement.GrnUomMapper;
import com.param.entity.model.procurement.PhysicalInspection;
import com.param.entity.model.procurement.PurchaseOrderDetail;
import com.param.global.dto.TaxComponentDetailsDto;
import com.param.global.utility.Util;
import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.purchase.dto.goodReceiptNote.CreditNoteAmountList;
import com.param.procurement.purchase.dto.goodReceiptNote.CreditNoteVendorWise;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNDetailResponse;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNOtherChargesMapper;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNRequest;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNResponse;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNSearchResponse;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNSearchResponse1;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNSettledDetailsResponse;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNSettledSearchResponse;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNStagedDto;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNStagedList;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNUomMapper;
import com.param.procurement.purchase.dto.goodReceiptNote.GrnAccessoriesMapper;
import com.param.procurement.purchase.dto.goodReceiptNote.PreviousGRNSearchResponse;
import com.param.procurement.purchase.dto.goodReceiptNote.PurchasedOrderResponse;
import com.param.procurement.purchase.dto.goodReceiptNote.SettledDetails;
import com.param.procurement.purchase.service.goodReceiptNote.GrnNetAmount;
import com.param.procurement.quotation.dto.physicalInspection.DetailsConsignmentStockConversionSaveRequest;
import com.param.service.global.SessionService;
import com.param.global.common.ServiceException;

@Repository
public class GoodReceiptNoteDao extends AbstractDao<Integer, GoodReceiptNote> implements IGoodReceiptNoteDao {

	final static Logger logger = Logger.getLogger(GoodReceiptNoteDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<GoodReceiptNote> searchGRNList(GRNRequest grnRequest, Integer screenId) {
		String hql = "SELECT goodReceiptNote FROM GoodReceiptNote AS goodReceiptNote "
				+ " 	INNER JOIN goodReceiptNote.addedBy as user WITH user.unitId = :unitId "
				+ " 	LEFT JOIN goodReceiptNote.store as store WITH store.unit.unitId = :unitId "
				+ "		LEFT JOIN goodReceiptNote.status as status "
				// + " LEFT JOIN goodReceiptNote.approvalStatus as approvalStatus "
				+ " 	LEFT JOIN goodReceiptNote.assetType as assetType "
				+ " 	LEFT JOIN goodReceiptNote.vendor as vendor "
				+ " WHERE goodReceiptNote.isActive = true AND goodReceiptNote.isDeleted = false AND goodReceiptNote.unit.unitId = :unitId "
				+ " 	AND goodReceiptNote.addedDate BETWEEN :fromDate AND :toDate "
				+ "		AND (:storeId = 0 OR store.id = :storeId) "
				+ " 	AND (:grnNumber = '' OR lower(goodReceiptNote.grnNumber) LIKE :grnNumber) "
				+ " 	AND (:vendorId = 0 OR goodReceiptNote.vendor.id = :vendorId) "
				+ " 	AND (:vendorName = '' OR lower(goodReceiptNote.vendor.name) LIKE :vendorName ) "
				+ " 	AND (:statusId = 0 OR status.id = :statusId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " ORDER BY goodReceiptNote.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(grnRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(grnRequest.getToDate()));
		query.setParameter("storeId", grnRequest.getStoreId() != null ? grnRequest.getStoreId() : 0);
		query.setParameter("grnNumber",
				grnRequest.getGrnNumber() != null ? '%'+grnRequest.getGrnNumber().toLowerCase() + '%' : "");
		if (grnRequest.getVendorId() != null && grnRequest.getVendorId() > 0) {
			query.setParameter("vendorId", grnRequest.getVendorId() != null ? grnRequest.getVendorId() : 0);
			query.setParameter("vendorName", "");
		} else if (grnRequest.getVendorName() != null) {
			query.setParameter("vendorId", 0);
			query.setParameter("vendorName",
					grnRequest.getVendorName() != null ? grnRequest.getVendorName().toLowerCase() + '%' : "");
		} else {
			query.setParameter("vendorId", 0);
			query.setParameter("vendorName", "");
		}
		query.setParameter("statusId", grnRequest.getStatusId() != null ? grnRequest.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", sessionService.getUnitId());
		return query.list();
	}

	@Override
	public GoodReceiptNote getGoodReceiptNote(Integer id) {
		try{
			String hql = "SELECT goodReceiptNote FROM GoodReceiptNote AS goodReceiptNote "
					+ "WHERE goodReceiptNote.id = :id  AND goodReceiptNote.isActive = true AND goodReceiptNote.isDeleted = false ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);

			return (GoodReceiptNote) query.uniqueResult();
		}catch (Exception e){
			throw e;
		}

	}

	@Override
	public DiscountType getDiscountTypeById(Integer discountTypeId) {
		try {
			String hql = "SELECT discountType FROM DiscountType AS discountType WHERE discountType.id = :id";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", discountTypeId);
			return (DiscountType) query.uniqueResult();
		} catch (Exception e) {
			logger.error("getDiscountTypeById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<GoodReceiptNoteDetail> getGoodReceiptNoteDetail(Integer id) {
		String hql = "SELECT goodReceiptNoteDetail FROM GoodReceiptNoteDetail AS goodReceiptNoteDetail "
				+ " WHERE goodReceiptNoteDetail.goodReceiptNote.id = :id  AND goodReceiptNoteDetail.isActive = true AND goodReceiptNoteDetail.isDeleted = false "
				+ " ORDER BY goodReceiptNoteDetail.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return query.list();
	}

	@Override
	public List<GrnBonusUomMapper> getGRNBonusUomMapper(Integer id) {
		String hql = "SELECT grnBonusUomMapper FROM com.param.entity.model.procurement.GrnBonusUomMapper AS grnBonusUomMapper "
				+ " WHERE grnBonusUomMapper.goodReceiptNoteDetail.id = :id  AND grnBonusUomMapper.isActive = true AND grnBonusUomMapper.isDeleted = false "
				+ " ORDER BY grnBonusUomMapper.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return query.list();
	}

	@Override
	public List<GrnUomMapper> getGRNUomMapper(Integer id) {
		String hql = "SELECT grnUomMapper FROM com.param.entity.model.procurement.GrnUomMapper AS grnUomMapper "
				+ " WHERE grnUomMapper.goodReceiptNoteDetail.id = :id  AND grnUomMapper.isActive = true AND grnUomMapper.isDeleted = false "
				+ " ORDER BY grnUomMapper.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return query.list();
	}

	@Override
	public List<GrnOtherChargesMapper> getGRNOtherChargesMapper(Integer id) {
		String hql = "SELECT grnOtherChargesMapper FROM com.param.entity.model.procurement.GrnOtherChargesMapper AS grnOtherChargesMapper "
				+ " WHERE grnOtherChargesMapper.goodReceiptNoteDetail.id = :id  AND grnOtherChargesMapper.isActive = true AND grnOtherChargesMapper.isDeleted = false "
				+ " ORDER BY grnOtherChargesMapper.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return query.list();
	}

	@Override
	public PurchaseOrderDetail getPurchaseOrderDetails(Integer purchaseOrderDetailIds) {
		String hql = "SELECT purchaseOrderDetail FROM PurchaseOrderDetail AS purchaseOrderDetail \r\n"
				+ "WHERE purchaseOrderDetail.id in :purchaseOrderDetailIds "
				+ "		AND purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false";

		Query query = getSession().createQuery(hql);
		query.setParameter("purchaseOrderDetailIds", purchaseOrderDetailIds);
		return (PurchaseOrderDetail) query.uniqueResult();
	}

	@Override
	public List<GrnAccessory> getGrnAccessory(Integer id) {
		String hql = "SELECT grnAccessory FROM com.param.entity.model.procurement.GrnAccessory AS grnAccessory "
				+ " WHERE grnAccessory.grnDetail.id = :id  AND grnAccessory.isActive = true AND grnAccessory.isDeleted = false "
				+ " ORDER BY grnAccessory.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return query.list();
	}

	@Override
	public List<PurchasedOrderResponse> getPurchaseOrderId(Integer goodReceiptNoteDetailId) {

		String hql = " SELECT purchaseOrderDetail.id AS id, purchaseOrderDetail.poApprovedQuantity AS approvedQuantity ,"
				+ " purchaseOrder.poNumber AS poNo " + " FROM GoodReceiptNoteDetail AS goodReceiptNoteDetail "
				+ " INNER JOIN goodReceiptNoteDetail.purchaseOrderDetail AS purchaseOrderDetail WITH purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false "
				+ " INNER JOIN purchaseOrderDetail.purchaseOrder AS purchaseOrder WITH purchaseOrder.isActive = true AND purchaseOrder.isDeleted = false "
				+ " WHERE goodReceiptNoteDetail.id = :goodReceiptNoteDetailId AND goodReceiptNoteDetail.isActive = true AND goodReceiptNoteDetail.isDeleted = false "
				+ " ORDER BY goodReceiptNoteDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("goodReceiptNoteDetailId", goodReceiptNoteDetailId);
		query.setResultTransformer(Transformers.aliasToBean(PurchasedOrderResponse.class));
		return query.list();
	}

	@Override
	public List<GRNStagedDto> getGRNStagedQuantity(Integer id) {
		/*
		 * String hql =
		 * "SELECT grnStaged.id AS id, grnStaged.receivedQuantity AS receivedQuantity, "
		 * + "			poStaged.approvedQunatity AS poApprovedQuantity, " +
		 * " 		poStaged.approvedQunatity - grnStaged.receivedQuantity AS pendingQuantity,  "
		 * + " 		poStaged.stagedDate AS poStagedDate, " +
		 * "			COALESCE(grnStaged.approvedQuantity, 0) AS approvedQuantity, " +
		 * "			goodReceiptNoteDetail.id = grnStaged.goodReceiptNoteDetail.id AS isEditable "
		 * + " FROM GoodReceiptNoteDetail AS goodReceiptNoteDetail " +
		 * "		INNER JOIN goodReceiptNoteDetail.purchaseOrderDetail AS purchaseOrderDetail WITH purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false "
		 * +
		 * "		INNER JOIN purchaseOrderDetail.purchaseOrderStagedList AS poStaged WITH poStaged.isDeleted = false AND poStaged.isActive = true "
		 * +
		 * "		LEFT OUTER JOIN poStaged.grnStagedQuantities as grnStaged WITH grnStaged.isDeleted = false AND grnStaged.isActive = true "
		 * +
		 * " WHERE goodReceiptNoteDetail.id = :id AND goodReceiptNoteDetail.isActive = true AND goodReceiptNoteDetail.isDeleted = false "
		 * + " ORDER BY grnStaged.id ";
		 */

		String hql1 = "SELECT " + "			poStaged.approvedQunatity AS poApprovedQuantity, "
				+ " 		poStaged.stagedDate AS poStagedDate "
				+ " FROM GoodReceiptNoteDetail AS goodReceiptNoteDetail "
				+ "		INNER JOIN goodReceiptNoteDetail.purchaseOrderDetail AS purchaseOrderDetail WITH purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false "
				+ "		INNER JOIN purchaseOrderDetail.purchaseOrderStagedList AS poStaged WITH poStaged.isDeleted = false AND poStaged.isActive = true "
				+ " WHERE goodReceiptNoteDetail.id = :id AND goodReceiptNoteDetail.isActive = true AND goodReceiptNoteDetail.isDeleted = false ";

		Query query = getSession().createQuery(hql1);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(GRNStagedDto.class));
		return query.list();
	}

	@Override
	public List<GRNStagedList> getGRNQuantityByDcDate(Integer id) {

		/*
		 * select purchaseor1_.id poid, grnstagedq3_.id stageQtyId,
		 * grnstagedq3_.grn_detail_id from procurement.t_good_receipt_note_detail
		 * goodreceip0_
		 * 
		 * inner join procurement.t_purchase_order_detail purchaseor1_ on
		 * goodreceip0_.po_detail_id=purchaseor1_.id and (purchaseor1_.is_active=true
		 * and purchaseor1_.is_deleted=false)
		 * 
		 * inner join procurement.t_grn_staged_qunatity grnstagedq3_ on
		 * grnstagedq3_.po_detail_id=purchaseor1_.id and (grnstagedq3_.is_deleted=false
		 * and grnstagedq3_.is_active=true)
		 * 
		 * where goodreceip0_.id=215
		 */

		String hql = "SELECT grnStaged.id AS id, grnStaged.receivedQuantity AS stagedReceivedQuantity, grnStaged.stagedDcDate AS stagedDcDate, "
				+ "			COALESCE(grnStaged.approvedQuantity, 0) AS approvedQuantity, "
				+ "			goodReceiptNoteDetail.id = grnStaged.goodReceiptNoteDetail.id AS isEditable "
				+ " FROM GoodReceiptNoteDetail AS goodReceiptNoteDetail "
				+ "		INNER JOIN goodReceiptNoteDetail.purchaseOrderDetail AS purchaseOrderDetail WITH purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false "
				+ "		INNER JOIN purchaseOrderDetail.grnStagedQuantities as grnStaged WITH grnStaged.isDeleted = false AND grnStaged.isActive = true "
				+ " WHERE goodReceiptNoteDetail.id = :id AND goodReceiptNoteDetail.isActive = true AND goodReceiptNoteDetail.isDeleted = false "
				+ " ORDER BY grnStaged.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(GRNStagedList.class));
		return query.list();

	}

	@Override
	public List<GRNStagedDto> getGRNStagedQuantityForApproval(Integer id) {
		/*
		 * String hql =
		 * "SELECT grnStaged.id AS id, grnStaged.receivedQuantity AS receivedQuantity, "
		 * + "			poStaged.approvedQunatity AS poApprovedQuantity, " +
		 * " 		poStaged.approvedQunatity - grnStaged.receivedQuantity AS pendingQuantity,  "
		 * + " 		poStaged.stagedDate AS poStagedDate, " +
		 * "			COALESCE(grnStaged.approvedQuantity, 0) AS approvedQuantity " +
		 * " FROM GoodReceiptNoteDetail AS goodReceiptNoteDetail " +
		 * "		INNER JOIN goodReceiptNoteDetail.grnStagedQuantityList AS grnStaged WITH grnStaged.isDeleted = false AND grnStaged.isActive = true "
		 * +
		 * "		INNER JOIN grnStaged.purchaseOrderStaged AS poStaged WITH poStaged.isDeleted = false AND poStaged.isActive = true "
		 * +
		 * " WHERE goodReceiptNoteDetail.id = :id AND goodReceiptNoteDetail.isActive = true AND goodReceiptNoteDetail.isDeleted = false "
		 * + " ORDER BY grnStaged.id ";
		 */

		String hql = "SELECT grnStaged.id AS id, grnStaged.receivedQuantity AS receivedQuantity, grnStaged.stagedDcDate AS poStagedDate, "
				+ "			COALESCE(grnStaged.approvedQuantity, 0) AS approvedQuantity "
				+ " FROM GoodReceiptNoteDetail AS goodReceiptNoteDetail "
				+ "		INNER JOIN goodReceiptNoteDetail.grnStagedQuantityList AS grnStaged WITH grnStaged.isDeleted = false AND grnStaged.isActive = true "
				+ " WHERE goodReceiptNoteDetail.id = :id AND goodReceiptNoteDetail.isActive = true AND goodReceiptNoteDetail.isDeleted = false "
				+ " ORDER BY grnStaged.id ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(GRNStagedDto.class));
		return query.list();
	}

	@Override
	public GoodReceiptNote saveGoodReceiptNote(GoodReceiptNote goodReceiptNote) {
		getSession().save(goodReceiptNote);
		return goodReceiptNote;

	}

	@Override
	public GRNSearchResponse1 getNetAndPaidAmt(List<Integer> grnIds) {
		String hql = "SELECT sum(grn.netAmount) AS netAmount, sum(grn.paidAmount) AS paidAmount FROM GoodReceiptNote AS grn WHERE grn.id in (:grnIds)";

		Query query = getSession().createQuery(hql);
		query.setParameterList("grnIds", grnIds);
		query.setResultTransformer(Transformers.aliasToBean(GRNSearchResponse1.class));
		return (GRNSearchResponse1) query.uniqueResult();
	}

	@Override
	public GoodReceiptNote updateGoodReceiptNote(GoodReceiptNote goodReceiptNote) {
		getSession().update(goodReceiptNote);
		return goodReceiptNote;
	}

	@Override
	public PhysicalInspection savePhysicalInspection(PhysicalInspection physicalInspection) {
		getSession().save(physicalInspection);
		return physicalInspection;
	}

	@Override
	public Batch saveBatch(Batch batch) {
		getSession().save(batch);
		return batch;
	}

	@Override
	public GRNResponse getGoodReceiptNoteBP(Integer id) {
		String hql = "SELECT grn.id as id, assetType.id as assetTypeId, assetType.type as assetType, "
				+ "to_char(grn.dueDate,'yyyy-MM-dd') as dueDate,"
				+ "requestType.id as grnTypeId, requestType.type as grnType,"
				+ "grn.grnNumber as grnNumber, grn.addedDate as grnDate, "
				+ "discountType.id as discountTypeId, discountType.type as discountType,"
				+ "status.id as statusId, status.status as status,"
				+ "approvalStatus.id as approvalStatusId, approvalStatus.status as approvalStatus,"
				+ "store.storeId as storeId, store.storeName as StoreName,"
				+ "grn.dcDate as dcDate, grn.dcNumber as dcNumber, grn.discount as discount, grn.invoiceDate as invoiceDate,"
				+ "grn.invoiceNumber as invoiceNumber, grn.inwardDate as inwardDate, grn.inwardNumber as inwardNumber, "
				+ "grn.isConsignment as isConsignment, grn.netAmount as netAmount, grn.roundOff as roundOff,"
				+ "grn.totalAmount as totalAmount, grn.totalDiscount as totalDiscount, grn.totalOtherAmount as totalOtherAmount,"
				+ "grn.totalTaxAmmount as totalTaxAmount, " + "vendor.id as vendorId, vendor.name as vendorName,"
				+ "grn.remark as remark , grn.billTypeName as billTypeName,"
				+ "addedBy.userName as addedBy , requestingStore.storeId as requestingStoreId , requestingStore.storeName as requestingStoreName , grn.totalCreditNoteAmount as totalCreditNoteAmount  "
				+ " FROM GoodReceiptNote AS grn " + " LEFT JOIN grn.assetType AS assetType "
				+ " LEFT JOIN grn.requestType AS requestType " + " LEFT JOIN grn.discountType AS discountType "
				+ " LEFT JOIN grn.status AS status " + " LEFT JOIN grn.approvalStatus AS approvalStatus "
				+ " LEFT JOIN grn.store AS store " + " LEFT JOIN grn.requestingStore AS requestingStore "
				+ " LEFT JOIN grn.vendor AS vendor " + " LEFT JOIN grn.addedBy AS addedBy "
				+ "WHERE grn.id = :id  AND grn.isActive = true AND grn.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		query.setResultTransformer(Transformers.aliasToBean(GRNResponse.class));
		return (GRNResponse) query.uniqueResult();
	}

	@Override
	public List<GRNDetailResponse> getGoodReceiptNoteDetailsBP(Integer id) {

		String hql = "SELECT grnDtls.id as id, item.id as itemId,batch.id as batchId, "
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
				+ "manufacturer.name as manufacturerName, tax.name as taxCode, status.status as status,"
				+ "grnDtls.grnReceivedQuantity as poGrnQuantity, purchaseOrderDetail.poApprovedQuantity as poPendingQuantity, grnDtls.warrantyDateTo as warrantyDateTo , "
				+ " store.storeId as storeId, assetType.id as assetTypeId, purchaseOrderDetail.id AS poDetailId, purchaseOrder.poNumber AS poNo, purchaseOrder.id AS poId ,  purchaseOrder.addedDate AS poDate , grnDtls.lcop as lcop"
				// + " , grnDtls.pendingQuantity as pendingQuantity FROM GoodReceiptNoteDetail
				// AS grnDtls " + " INNER JOIN grnDtls.goodReceiptNote AS grn "
				+ "  , grnDtls.pendingQuantity as pendingQuantity ,  vendor.id as serviceProviderId  , vendor.name as serviceProvider ,grnDtls.warrentyDate as warrantyDate "
				+ " , grnDtls.supportTerm as supportTerms , grnDtls.technicalSpec as technicalSpecification , grnDtls.expectedPayment as expectedPayment , grnDtls.warrentyPeriod as warrentyPeriod,grnDtls.roundOffAmount as roundOffAmount,grnDtls.totalNetAmount as totalNetAmount, item.isThreeUOMApplicable as isThreeUOMApplicable,purchaseOrderDetail.poUomType.id as poGrnUomTypeId    FROM GoodReceiptNoteDetail AS grnDtls "
				+ " INNER JOIN grnDtls.goodReceiptNote AS grn " + " LEFT JOIN grn.store AS store "
				+ " LEFT JOIN grn.assetType AS assetType " + "  LEFT JOIN grnDtls.vendor  AS vendor "
				+ " LEFT JOIN grnDtls.item AS item " + " LEFT JOIN grnDtls.grnUomType AS grnUomType "
				+ " LEFT JOIN grnDtls.grnUomUnit AS grnUomUnit " + " LEFT JOIN grnDtls.bonusUomType AS bonusUomType "
				+ " LEFT JOIN grnDtls.bonusUomUnit AS bonusUomUnit " + " LEFT JOIN grnDtls.tax AS tax "
				+ " LEFT JOIN grnDtls.status AS status  " + " LEFT JOIN grnDtls.manufacturer AS manufacturer "
				+ " LEFT JOIN grnDtls.purchaseOrderDetail AS purchaseOrderDetail "
				+ " LEFT JOIN grnDtls.purchaseOrderDetail.purchaseOrder AS purchaseOrder "
				+ " LEFT JOIN grn.store AS store " + " LEFT JOIN grn.assetType AS assetType"
				+ " LEFT JOIN grnDtls.batch as batch "
				+ " WHERE grn.id = :id  AND grn.isActive = true AND grn.isDeleted = false"
				+ " AND grnDtls.isActive = true AND grnDtls.isDeleted = false order by grnDtls.id";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		query.setResultTransformer(Transformers.aliasToBean(GRNDetailResponse.class));

		return query.list();
	}

	@Override
	public List<PurchasedOrderResponse> getPurchaseOrderIdBP(List<Integer> allDetailIds) {
		String hql = " SELECT purchaseOrderDetail.id AS id, purchaseOrderDetail.poApprovedQuantity AS approvedQuantity ,"
				+ " purchaseOrder.poNumber AS poNo, goodReceiptNoteDetail.id as grnDetailId , purchaseOrder.addedDate as poDate"
				+ " FROM GoodReceiptNoteDetail AS goodReceiptNoteDetail "
				+ " INNER JOIN goodReceiptNoteDetail.purchaseOrderDetail AS purchaseOrderDetail WITH purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false "
				+ " INNER JOIN purchaseOrderDetail.purchaseOrder AS purchaseOrder WITH purchaseOrder.isActive = true AND purchaseOrder.isDeleted = false "
				+ " WHERE goodReceiptNoteDetail.id in (:goodReceiptNoteDetailId) AND goodReceiptNoteDetail.isActive = true AND goodReceiptNoteDetail.isDeleted = false "
				+ " ORDER BY goodReceiptNoteDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("goodReceiptNoteDetailId", allDetailIds);
		query.setResultTransformer(Transformers.aliasToBean(PurchasedOrderResponse.class));
		return query.list();
	}

	@Override
	public List<GRNStagedDto> getGRNStagedQuantityBP(List<Integer> allDetailIds) {
		String hql1 = "SELECT " + "			poStaged.approvedQunatity AS poApprovedQuantity, "
				+ " 		poStaged.stagedDate AS poStagedDate, goodReceiptNoteDetail.id as grnDetailId "
				+ " FROM GoodReceiptNoteDetail AS goodReceiptNoteDetail "
				+ "		INNER JOIN goodReceiptNoteDetail.purchaseOrderDetail AS purchaseOrderDetail WITH purchaseOrderDetail.isActive = true AND purchaseOrderDetail.isDeleted = false "
				+ "		INNER JOIN purchaseOrderDetail.purchaseOrderStagedList AS poStaged WITH poStaged.isDeleted = false AND poStaged.isActive = true "
				+ " WHERE goodReceiptNoteDetail.id in (:id) AND goodReceiptNoteDetail.isActive = true AND goodReceiptNoteDetail.isDeleted = false order by goodReceiptNoteDetail.addedDate desc  ";

		Query query = getSession().createQuery(hql1);
		query.setParameterList("id", allDetailIds);
		query.setResultTransformer(Transformers.aliasToBean(GRNStagedDto.class));
		return query.list();
	}

	@Override
	public List<GRNStagedList> getGRNQuantityByDcDateBP(List<Integer> poDetailId, Integer grnId) {
		String hql = "SELECT grn.id AS grnId, grnDetails.id AS grnDetailId, poDetails.id AS poDetailId, "
				+ " 		grnStaged.id AS id, grnStaged.receivedQuantity AS stagedReceivedQuantity, "
				+ "			COALESCE(grnStaged.approvedQuantity, 0) AS approvedQuantity, grnStaged.stagedDcDate AS stagedDcDate, "
				+ "			grn.id = :grnId AS isEditable" + " FROM PurchaseOrderDetail AS poDetails "
				+ "		INNER JOIN poDetails.grnStagedQuantities as grnStaged WITH grnStaged.isDeleted = false AND grnStaged.isActive = true "
				+ "		INNER JOIN grnStaged.goodReceiptNoteDetail AS grnDetails WITH grnDetails.isActive = true AND grnDetails.isDeleted = false "
				+ "		INNER JOIN grnDetails.goodReceiptNote AS grn WITH grn.isActive = true AND grn.isDeleted = false "
				+ " WHERE poDetails.id in (:poDetailId) AND poDetails.isActive = true AND poDetails.isDeleted = false AND grn.approvalStatus.id !=:approvalStatusId";

		Query query = getSession().createQuery(hql);
		query.setParameterList("poDetailId", poDetailId);
		query.setParameter("grnId", grnId);
		query.setParameter("approvalStatusId", com.param.common.constants.Status.Cancelled.getStatus());
		query.setResultTransformer(Transformers.aliasToBean(GRNStagedList.class));
		return query.list();
	}

	@Override
	public List<GRNOtherChargesMapper> getGRNOtherChargesMapperBP(Integer allDetailIds) {
		/*
		 * String hql =
		 * "SELECT grnOtherChargesMapper.id as id, grnOtherChargesMapper.otherCharge.id as otherChargesId, "
		 * +
		 * "grnOtherChargesMapper.otherCharge.description as otherCharges, grnOtherChargesMapper.amount as amount, "
		 * + "grnOtherChargesMapper.goodReceiptNoteDetail.id as grnDetailId " +
		 * " FROM com.param.entity.model.procurement.GrnOtherChargesMapper AS grnOtherChargesMapper "
		 * +
		 * " WHERE grnOtherChargesMapper.goodReceiptNoteDetail.id in (:id)  AND grnOtherChargesMapper.isActive = true AND grnOtherChargesMapper.isDeleted = false "
		 * + " ORDER BY grnOtherChargesMapper.id ";
		 */
		String hql = "SELECT grnOtherChargesMapper.id as id, grnOtherChargesMapper.otherCharge.id as otherChargesId, "
				+ "grnOtherChargesMapper.otherCharge.description as otherCharges, grnOtherChargesMapper.amount as amount, "
				+ "grnOtherChargesMapper.goodReceiptNote.id as grnId,grnOtherChargesMapper.isFromGrn as isFromGrn "
				+ " FROM com.param.entity.model.procurement.GrnOtherChargesMapper AS grnOtherChargesMapper "
				+ " WHERE grnOtherChargesMapper.goodReceiptNote.id =:id  AND grnOtherChargesMapper.isActive = true AND grnOtherChargesMapper.isDeleted = false "
				+ " ORDER BY grnOtherChargesMapper.id ASC ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", allDetailIds);
//		query.setMaxResults(1);
		query.setResultTransformer(Transformers.aliasToBean(GRNOtherChargesMapper.class));
		return query.list();
	}

	@Override
	public List<GRNUomMapper> getGRNUomMapperBP(List<Integer> consumableDetailIds) {

		String hql = "SELECT grnUomMapper.id as id, grnUomMapper.uomType.id as uomTypeId, "
				+ "grnUomMapper.uomType.type as uomType, grnUomMapper.uomUnit.id as uomUnitId, grnUomMapper.uomUnit.units as uomUnit, "
				+ "grnUomMapper.conversions as conversion, grnUomMapper.ipUom as ipUom, grnUomMapper.opUom as opUom, "
				+ "grnUomMapper.storeUom as storeUom, grnUomMapper.goodReceiptNoteDetail.id as grnDetailId "
				+ " FROM com.param.entity.model.procurement.GrnUomMapper AS grnUomMapper "
				+ " WHERE grnUomMapper.goodReceiptNoteDetail.id in (:id)  AND grnUomMapper.isActive = true AND grnUomMapper.isDeleted = false "
				+ " ORDER BY grnUomMapper.id ";
		Query query = getSession().createQuery(hql);
		query.setParameterList("id", consumableDetailIds);
		query.setResultTransformer(Transformers.aliasToBean(GRNUomMapper.class));
		return query.list();
	}

	@Override
	public List<GRNUomMapper> getGRNBonusUomMapperBP(List<Integer> consumabelItemIds) {

		String hql = "SELECT grnBonusUomMapper.id as id, "
				+ "grnBonusUomMapper.uomType.id as uomTypeId, grnBonusUomMapper.uomType.type as uomType, "
				+ "grnBonusUomMapper.uomUnit.id as uomUnitId, grnBonusUomMapper.uomUnit.units as uomUnit, "
				+ "grnBonusUomMapper.conversions as conversion, grnBonusUomMapper.ipUom as ipUom, "
				+ "grnBonusUomMapper.opUom as opUom, grnBonusUomMapper.storeUom as storeUom, "
				+ "grnBonusUomMapper.goodReceiptNoteDetail.id as grnDetailId "
				+ " FROM com.param.entity.model.procurement.GrnBonusUomMapper AS grnBonusUomMapper "
				+ " WHERE grnBonusUomMapper.goodReceiptNoteDetail.id in (:id)  AND grnBonusUomMapper.isActive = true AND grnBonusUomMapper.isDeleted = false "
				+ " ORDER BY grnBonusUomMapper.id ";
		Query query = getSession().createQuery(hql);
		query.setParameterList("id", consumabelItemIds);
		query.setResultTransformer(Transformers.aliasToBean(GRNUomMapper.class));
		return query.list();
	}

	@Override
	public List<GrnAccessoriesMapper> getGrnAccessoryBP(List<Integer> fixedDetaildId) {

		String hql = "SELECT grnAccessory.id as id, grnAccessory.item.id as itemId, grnAccessory.item.name as itemName,"
				+ "grnAccessory.item.code as itemCode, grnAccessory.serialNo as serialNo, grnAccessory.warrentyFrom as warrentyFrom, "
				+ "grnAccessory.warrentyTo as warrentyTo, grnAccessory.quantity as quantity, grnAccessory.specification as specification,"
				+ "grnAccessory.grnDetail.id as grnDetailId "
				+ " FROM com.param.entity.model.procurement.GrnAccessory AS grnAccessory "
				+ " WHERE grnAccessory.grnDetail.id in (:id)  AND grnAccessory.isActive = true AND grnAccessory.isDeleted = false "
				+ " ORDER BY grnAccessory.id ";
		Query query = getSession().createQuery(hql);
		query.setParameterList("id", fixedDetaildId);
		query.setResultTransformer(Transformers.aliasToBean(GrnAccessoriesMapper.class));
		return query.list();
	}

	@Override
	public List<GRNSearchResponse> searchGRNListBP(GRNRequest grnRequest, Integer screenId) {

		String hql = "SELECT distinct goodReceiptNote.id as id, store.storeId as storeId, store.storeName as storeName, "
				+ "goodReceiptNote.grnNumber as grnNo, goodReceiptNote.addedDate as grnDate, "
				+ "goodReceiptNote.invoiceNumber as invoiceNo, goodReceiptNote.invoiceDate as invoiceDate, "
				+ "goodReceiptNote.dcNumber as dcNo,"
				+ "goodReceiptNote.dcDate as dcDate, vendor.name as vendorName, user.userName as addedBy, "
				+ "status.status as status, approvalStatus.status as approvalStatus, approvedBy.userName as approvedBy,"
				+ "goodReceiptNote.approvedDate as approvedDate , requestingStore.storeId as requestingStoreId , requestingStore.storeName as requestingStoreName,"
				/* Added By Atmiya 20191108 */
				+ " goodReceiptNote.isConsignment as isConsignment "
				/* End Added By Atmiya 20191108 */
				+ "FROM GoodReceiptNoteDetail as goodReceiptNoteDetail join  goodReceiptNoteDetail.goodReceiptNote AS goodReceiptNote " + " 	INNER JOIN goodReceiptNote.addedBy as user"
				+ " 	LEFT JOIN goodReceiptNote.store as store WITH store.unit.unitId = :unitId "
				+ " 	LEFT JOIN goodReceiptNote.requestingStore as requestingStore "
				+ "		LEFT JOIN goodReceiptNote.status as status "
				+ "		LEFT JOIN goodReceiptNote.approvalStatus as approvalStatus "
				+ " 	LEFT JOIN goodReceiptNote.assetType as assetType "
				+ " 	LEFT JOIN goodReceiptNote.vendor as vendor "
				+ "		LEFT JOIN goodReceiptNote.approvedBy as approvedBy "
				+ "		LEFT JOIN goodReceiptNoteDetail.item as item "
				+ " WHERE goodReceiptNote.isActive = true AND goodReceiptNote.isDeleted = false AND goodReceiptNote.unit.unitId = :unitId "
				+ " 	AND goodReceiptNote.addedDate BETWEEN :fromDate AND :toDate "
				+ "		AND (:storeId = 0 OR store.id = :storeId) "
				+ "		AND (:requestingStoreId = 0 OR requestingStore.id = :requestingStoreId) "
				+ " 	AND (:grnNumber = '' OR lower(goodReceiptNote.grnNumber) LIKE :grnNumber) "
				+ " 	AND (:vendorId = 0 OR goodReceiptNote.vendor.id = :vendorId) "
				+ " 	AND (:vendorName = '' OR lower(goodReceiptNote.vendor.name) LIKE :vendorName ) "
				+ " 	AND (:statusId = 0 OR status.id = :statusId) "
				+ "		AND (:itemId = 0 OR item.id = :itemId) "
				+ "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " ORDER BY goodReceiptNote.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(grnRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(grnRequest.getToDate()));
		query.setParameter("storeId", grnRequest.getStoreId() != null ? grnRequest.getStoreId() : 0);
		query.setParameter("requestingStoreId",
				grnRequest.getRequestingStoreId() != null ? grnRequest.getRequestingStoreId() : 0);
		query.setParameter("grnNumber",
				grnRequest.getGrnNumber() != null ? '%'+grnRequest.getGrnNumber().toLowerCase() + '%' : "");
		if (grnRequest.getVendorId() != null && grnRequest.getVendorId() > 0) {
			query.setParameter("vendorId", grnRequest.getVendorId() != null ? grnRequest.getVendorId() : 0);
			query.setParameter("vendorName", "");
		} else if (grnRequest.getVendorName() != null) {
			query.setParameter("vendorId", 0);
			query.setParameter("vendorName",
					grnRequest.getVendorName() != null ? grnRequest.getVendorName().toLowerCase() + '%' : "");
		} else {
			query.setParameter("vendorId", 0);
			query.setParameter("vendorName", "");
		}
		query.setParameter("statusId", grnRequest.getStatusId() != null ? grnRequest.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("itemId", grnRequest.getItemId()!=null ? grnRequest.getItemId() : 0);
		query.setParameter("unitId",
				grnRequest.getUnitId() != null ? grnRequest.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(GRNSearchResponse.class));
		return query.list();
	}

	@Override
	public List<GRNStagedDto> getGRNStagedQuantityForApprovalBP(List<Integer> allDetailIds) {
		String hql = "SELECT grnStaged.id AS id, grnStaged.receivedQuantity AS receivedQuantity, grnStaged.stagedDcDate AS poStagedDate, "
				+ "			COALESCE(goodReceiptNoteDetail.grnApprovedQuantity, 0) AS approvedQuantity, goodReceiptNoteDetail.id AS grnDetailId "
				+ " FROM GoodReceiptNoteDetail AS goodReceiptNoteDetail "
				+ "		INNER JOIN goodReceiptNoteDetail.grnStagedQuantityList AS grnStaged WITH grnStaged.isDeleted = false AND grnStaged.isActive = true "
				+ " WHERE goodReceiptNoteDetail.id in (:id) AND goodReceiptNoteDetail.isActive = true AND goodReceiptNoteDetail.isDeleted = false "
				+ " ORDER BY grnStaged.id ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("id", allDetailIds);
		query.setResultTransformer(Transformers.aliasToBean(GRNStagedDto.class));
		return query.list();
	}

	@Override
	public List<GoodReceiptNote> getByInvoiceNumberAndVendor(Integer vendorId, String invoiceNumber) {
		String hql = "SELECT goodReceiptNote " + " FROM GoodReceiptNote AS goodReceiptNote "
				+ "	INNER JOIN goodReceiptNote.vendor vendor "
				+ " WHERE vendor.id = :vendorId AND goodReceiptNote.invoiceNumber = :invoiceNumber "
				+ "	AND goodReceiptNote.isActive = true AND goodReceiptNote.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("vendorId", vendorId);
		query.setParameter("invoiceNumber", invoiceNumber);
		return query.list();
	}

	@Override
	public List<GoodReceiptNote> getByInvoiceNumberAndDCNumber(Integer vendorId, String dcNumber) {
		String hql = "SELECT goodReceiptNote " + " FROM GoodReceiptNote AS goodReceiptNote "
				+ "	INNER JOIN goodReceiptNote.vendor vendor "
				+ " WHERE vendor.id = :vendorId AND goodReceiptNote.dcNumber = :dcNumber "
				+ "	AND goodReceiptNote.isActive = true AND goodReceiptNote.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("vendorId", vendorId);
		query.setParameter("dcNumber", dcNumber);
		return query.list();
	}

	@Override
	public List<PreviousGRNSearchResponse> previousGrnDetails(Integer vendor_id, Integer item_id, Integer grnId) {

		// String hql = " select grn.added_date as grnDate, grn.grn_number as grnNumber,
		// grn.invoice_number as invoiceNumber, grn.dc_number as dcNumber,
		// gd.grn_approved_quantity as grnApprovedQty , gd.grn_received_quantity as
		// grnReceivedQty, uomunit.unit as Unit , gd.bonus_approved_quantity as
		// bonusApprovedQty , gd.bonus_received_quantity as bonusReceivedQty ,
		// bonusuomunit.unit as bonusUnit, gd.mrp as mrp, gd.cop as cop, gd.expiry_date
		// as expiryDate, gd.batch_no as batchNumber from
		// procurement.t_good_receipt_note grn join
		// procurement.t_good_receipt_note_detail gd on (grn.id = gd.grn_id) left join
		// public.m_uom_unit uomunit on (gd.grn_uom_unit_id = uomunit.id) left join
		// public.m_uom_unit bonusuomunit on (gd.bonus_uom_unit_id = bonusuomunit.id)
		// where grn.vendor_id = :vendor_id and gd.item_id = :item_id and grn.id!=:grnId
		// ";
		String hql = "  select mv.name as \"vendorName\", grn.added_date as grnDate, grn.grn_number as grnNumber, grn.invoice_number as invoiceNumber, grn.dc_number as dcNumber, gd.grn_approved_quantity as grnApprovedQty , gd.grn_received_quantity as grnReceivedQty, uomunit.unit as Unit , gd.bonus_approved_quantity  as bonusApprovedQty , gd.bonus_received_quantity as bonusReceivedQty , bonusuomunit.unit as bonusUnit, gd.mrp as mrp, gd.cop as cop, 	gd.expiry_date as expiryDate, gd.batch_no as batchNumber from 	procurement.t_good_receipt_note grn join public.m_vendor mv  on grn.vendor_id = mv.id   join procurement.t_good_receipt_note_detail gd  on (grn.id = gd.grn_id) left join public.m_uom_unit uomunit on (gd.grn_uom_unit_id = uomunit.id)  left join public.m_uom_unit bonusuomunit on (gd.bonus_uom_unit_id = bonusuomunit.id) where   gd.item_id = :item_id and grn.id=:grnId and mv.id=:vendor_id limit 10 ";

		// grn.vendor_id = 1 and gd.item_id =12
		Query query = getSession().createSQLQuery(hql);
		 query.setParameter("vendor_id", vendor_id);
		query.setParameter("item_id", item_id);
		query.setParameter("grnId", grnId);

		query.setResultTransformer(Transformers.aliasToBean(PreviousGRNSearchResponse.class));

		return query.list();

	}

	@Override
	public List<GRNSearchResponse1> searchGRNListSettlement(GRNRequest grnRequest, Integer screenId) {
		
		String hql = "SELECT goodReceiptNote.id as id, store.storeId as storeId, store.storeName as storeName, "
				+ "goodReceiptNote.grnNumber as grnNo, goodReceiptNote.addedDate as grnDate, "
				+ "goodReceiptNote.invoiceNumber as invoiceNo, goodReceiptNote.invoiceDate as invoiceDate, "
				+ "goodReceiptNote.dcNumber as dcNo,"
				+ "goodReceiptNote.totalCreditNoteAmount as totalCreditAmount,"
				+ "goodReceiptNote.dcDate as dcDate, vendor.name as vendorName, user.userName as addedBy, "
				+ "status.status as status, approvalStatus.status as approvalStatus, approvedBy.userName as approvedBy,"
				+ "goodReceiptNote.approvedDate as approvedDate , requestingStore.storeId as requestingStoreId , requestingStore.storeName as requestingStoreName "
				+ ", goodReceiptNote.netAmount as netAmount, goodReceiptNote.paidAmount as paidAmount, goodReceiptNote.balanceAmount as balanceAmount , goodReceiptNote.billTypeName as  billType ,"
				+ " vendor.id as vendorId,goodReceiptNote.roundOff as roundOffAmount,goodReceiptNote.dueDate as payDueDate "
				+ " FROM GoodReceiptNote AS goodReceiptNote "
						+ "	INNER JOIN goodReceiptNote.addedBy as user"
				+ " 	LEFT JOIN goodReceiptNote.store as store WITH store.unit.unitId = :unitId "
				+ " 	LEFT JOIN goodReceiptNote.requestingStore as requestingStore "
				+ "		LEFT JOIN goodReceiptNote.status as status "
				+ "		LEFT JOIN goodReceiptNote.approvalStatus as approvalStatus "
				+ " 	LEFT JOIN goodReceiptNote.assetType as assetType "
				+ " 	LEFT JOIN goodReceiptNote.vendor as vendor "
				+ "		LEFT JOIN goodReceiptNote.approvedBy as approvedBy "
				+ " WHERE goodReceiptNote.isActive = true AND goodReceiptNote.isDeleted = false AND goodReceiptNote.netAmount>0  AND goodReceiptNote.unit.unitId = :unitId "
				+ " 	AND goodReceiptNote.dueDate BETWEEN :fromDate AND :toDate "
				+ "     AND goodReceiptNote.approvalStatus.id  not in (:approvalStatusId) "
				+ "		AND (:storeId = 0 OR store.id = :storeId) "
				+ "		AND (:requestingStoreId = 0 OR requestingStore.id = :requestingStoreId) "
				+ " 	AND (:grnNumber = '' OR lower(goodReceiptNote.grnNumber) LIKE :grnNumber) "
				+ " 	AND (:vendorId = 0 OR goodReceiptNote.vendor.id = :vendorId) "
				+ " 	AND (:vendorName = '' OR lower(goodReceiptNote.vendor.name) LIKE :vendorName ) "
				+ " 	AND ( status.id in (5,8)) ";

		if (grnRequest.getBillTypeName() != null) {
			hql = hql + " AND (goodReceiptNote.billTypeName = :billTypeName) ";
		}

		hql = hql + "	AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " ORDER BY goodReceiptNote.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(grnRequest.getFromDate()));
		query.setParameter("toDate", Util.getDate(grnRequest.getToDate()));
		query.setParameter("storeId", grnRequest.getStoreId() != null ? grnRequest.getStoreId() : 0);
		query.setParameter("requestingStoreId",
				grnRequest.getRequestingStoreId() != null ? grnRequest.getRequestingStoreId() : 0);
		query.setParameter("grnNumber",
				grnRequest.getGrnNumber() != null ? '%'+grnRequest.getGrnNumber().toLowerCase() + '%' : "");
		if (grnRequest.getVendorId() != null && grnRequest.getVendorId() > 0) {
			query.setParameter("vendorId", grnRequest.getVendorId() != null ? grnRequest.getVendorId() : 0);
			query.setParameter("vendorName", "");
		} else if (grnRequest.getVendorName() != null) {
			query.setParameter("vendorId", 0);
			query.setParameter("vendorName",
					grnRequest.getVendorName() != null ? grnRequest.getVendorName().toLowerCase() + '%' : "");
		} else {
			query.setParameter("vendorId", 0);
			query.setParameter("vendorName", "");
		}

		if (grnRequest.getBillTypeName() != null) {
			query.setParameter("billTypeName", grnRequest.getBillTypeName());
		}

		// query.setParameter("statusId", grnRequest.getPaymentStatuses() != null ?
		// grnRequest.getPaymentStatuses() : 0);
		// query.setParameter("statusId",8);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId",
				grnRequest.getUnitId() != null ? grnRequest.getUnitId() : sessionService.getUnitId());
		query.setParameterList("approvalStatusId", Arrays.asList(Status.Cancelled.getStatus(), Status.Rejected.getStatus()));
		query.setResultTransformer(Transformers.aliasToBean(GRNSearchResponse1.class));
		return query.list();
	}

	@Override
	public GrnNetAmount getGrnNetAmt(Integer grnId, Integer vendorId, Integer orgnizationId, Integer unitId) {
		String hql = "  SELECT id as id , balance_amount as netAmount FROM procurement.t_good_receipt_note where id="
				+ grnId + " and vendor_id =" + vendorId + " and organization_id=" + orgnizationId + " and unit_id="
				+ unitId;

		Query query = sessionFactory.getCurrentSession().createSQLQuery(hql);
		query.setResultTransformer(Transformers.aliasToBean(GrnNetAmount.class));

		return (GrnNetAmount) query.uniqueResult();
	}

	@Override
	public Integer saveGrnSettlement(GrnSettlement grnsettle) {

		getSession().save(grnsettle);
		return grnsettle.getId();

	}

	@Override
	public void updateGrnNetAmt(Integer grnId, Integer vendorId, Integer orgnizationId, Integer unitId, Double amount) {
		/*
		 * String hql =
		 * " update  procurement.t_good_receipt_note set net_amount =net_amount-" +
		 * amount + " , paid_amount = paid_amount+" + amount +
		 * " , balance_amount=net_amount-" + amount + " where id=" + grnId +
		 * " and vendor_id =" + vendorId + " and organization_id=" + orgnizationId +
		 * " and unit_id=" + unitId;
		 */
		String hql = " update  procurement.t_good_receipt_note set net_amount =net_amount-" + amount
				+ " , paid_amount = paid_amount+" + amount + " , balance_amount=net_amount-" + amount + " where id="
				+ grnId + " and vendor_id =" + vendorId + " and organization_id=" + orgnizationId + " and unit_id="
				+ unitId;

		Session session = null;
		Transaction tx = null;

		String hql2 = "SELECT SUM(tgrnd.net_amount) FROM procurement.t_good_receipt_note_detail tgrnd "
				+ "WHERE tgrnd.unit_id = :unitId AND tgrnd.organization_id=:organizationId AND tgrnd.grn_id=:grnId AND tgrnd.is_active = true AND tgrnd.is_deleted = false ";
		Query query1 = getSession().createSQLQuery(hql2);
		query1.setParameter("grnId", grnId);
		query1.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query1.setParameter("organizationId", orgnizationId != null ? orgnizationId : sessionService.getOrganizationId());
		Double netAmount= (Double) query1.uniqueResult();
		double Output= new BigDecimal(netAmount).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        netAmount=Output;
		
		String hql1 = "SELECT tgrnd.round_off_amount FROM procurement.t_good_receipt_note_detail tgrnd "
				+ "WHERE tgrnd.unit_id = :unitId AND tgrnd.organization_id=:organizationId AND tgrnd.grn_id=:grnId AND tgrnd.is_active = true AND tgrnd.is_deleted = false ";
		Query query2 = getSession().createSQLQuery(hql1);
		query2.setParameter("grnId", grnId);
		query2.setMaxResults(1);
		query2.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query2.setParameter("organizationId", orgnizationId != null ? orgnizationId : sessionService.getOrganizationId());
		Double roundOffAmount= (Double) query2.uniqueResult();
		
		Double detailsFinalNetAmount = 0.0d;
		Double checkdetailsFinalNetAmount=(netAmount+(roundOffAmount!=null ? roundOffAmount :0));

		int amt = (int) amount.doubleValue();
		int bal = (int) checkdetailsFinalNetAmount.doubleValue();
		if(amt != bal){
			detailsFinalNetAmount = (netAmount+(roundOffAmount!=null ? roundOffAmount :0));
		}else{
			detailsFinalNetAmount = amount;
		}

		String hql3 = "SELECT tgrnd.paid_amount FROM procurement.t_good_receipt_note tgrnd "
				+ "WHERE tgrnd.unit_id = :unitId AND tgrnd.organization_id=:organizationId AND tgrnd.id=:grnId AND tgrnd.is_active = true AND tgrnd.is_deleted = false ";
		Query query3 = getSession().createSQLQuery(hql3);
		query3.setParameter("grnId", grnId);
		query3.setMaxResults(1);
		query3.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query3.setParameter("organizationId", orgnizationId != null ? orgnizationId : sessionService.getOrganizationId());
		Double paidAmount= (Double) query3.uniqueResult();
	    String sql = "  SELECT total_credit_note_amount FROM procurement.t_good_receipt_note where id="+ grnId + " and unit_id="+ unitId;
				Query getCreditNoteAmount = sessionFactory.getCurrentSession().createSQLQuery(sql);
				Double creditNoteAmount=(Double) getCreditNoteAmount.uniqueResult();
		if(detailsFinalNetAmount.equals(amount+(paidAmount!=null ? paidAmount:0)))
		{
			try {
				session = sessionFactory.openSession();
				tx = session.beginTransaction();
				// some action
				if(creditNoteAmount!=null)
				{
					amount=amount-creditNoteAmount;
				}
				String s = " update  procurement.t_good_receipt_note set  paid_amount = coalesce(paid_amount,0) +" + amount
						+ " , balance_amount=0 "
								+ "  where id=" + grnId
						+ " and vendor_id =" + vendorId + " and organization_id=" + orgnizationId + " and unit_id="
						+ unitId;
				Query query = session.createSQLQuery(s);
				query.executeUpdate();

				tx.commit();

			} catch (Exception ex) {
				logger.error("updateGrnNetAmt failed", ex);
				tx.rollback();
				throw new ServiceException(ex.getMessage());
			} finally {
				session.close();
			}
		}
		else {
		try {
			session = sessionFactory.openSession();
			tx = session.beginTransaction();
			// some action
			if(creditNoteAmount!=null)
			{
				amount=amount-creditNoteAmount;
			}
			String s = " update  procurement.t_good_receipt_note set  paid_amount = coalesce(paid_amount,0) +" + amount
					+ " , balance_amount=coalesce(balance_amount)-" + amount + "  where id=" + grnId
					+ " and vendor_id =" + vendorId + " and organization_id=" + orgnizationId + " and unit_id="
					+ unitId;
			Query query = session.createSQLQuery(s);
			query.executeUpdate();

			tx.commit();

		} catch (Exception ex) {
			logger.error("updateGrnNetAmt failed", ex);
			tx.rollback();
			throw new ServiceException(ex.getMessage());
		} finally {
			session.close();
		}
		}
	}

	@Override
	public void updatePartiallyGrnNetAmt(Integer grnId, Integer vendorId, Integer orgnizationId, Integer unitId,
			Double amount) {
		/*
		 * String hql =
		 * " update  procurement.t_good_receipt_note set net_amount ="+amount+
		 * "-net_amount"
		 * +" , paid_amount = paid_amount+"+amount+" , balance_amount=net_amount-"
		 * +amount +"where id=" + grnId + " and vendor_id =" + vendorId +
		 * " and organization_id=" + orgnizationId + " and unit_id=" + unitId;
		 */
		String hql = " update  procurement.t_good_receipt_note set  paid_amount = paid_amount+" + amount
				+ " , balance_amount=net_amount-" + amount + " where id=" + grnId + " and vendor_id =" + vendorId
				+ " and organization_id=" + orgnizationId + " and unit_id=" + unitId;
		String s = " update  procurement.t_good_receipt_note set  paid_amount = coalesce(paid_amount,0) +" + amount
				+ " , balance_amount=coalesce(net_amount,0)-" + amount + "  where id=" + grnId + " and vendor_id ="
				+ vendorId + " and organization_id=" + orgnizationId + " and unit_id=" + unitId;

		Query query = getSession().createSQLQuery(s);
		query.executeUpdate();

	}

	@Override
	public List<GRNSettledSearchResponse> getGoodReceiptNoteSettledList(GRNRequest grnRequest) {

		String hql = " select distinct grn.id as grnid   , grns.id as receiptid, grns.receipt_number as receiptnumber ,"
				+ "  grns.added_date as receiptdate, v.id as vendorid, v.name as vendorname , grn.paid_amount as paidamount  , grn.balance_amount as balanceamount  , grn.net_amount as netamount , grn.grn_number as grnnumber "
				+ " from procurement.t_good_receipt_note grn  join public.m_vendor v on grn.vendor_id=v.id  "
				+ " join procurement.t_grn_settlement_receipt_detail grnsd on grnsd.grn_id = grn.id "
				+ " join procurement.t_grn_settlement_receipt grns on grnsd.grn_settlement_id = grns.id "
				+ " where grn.unit_id=:unitId  and  grns.added_date between  :fromDate and   :toDate";

		if (grnRequest.getReceiptNo() != null) {
			hql = hql + " and lower(grns.receipt_number)  LIKE :receiptNumber";
		}

		if (grnRequest.getVendorId() != null) {
			hql = hql + " and  grn.vendor_id =:vId ";
		}

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("unitId",
				grnRequest.getUnitId() != null ? grnRequest.getUnitId() : sessionService.getUnitId());
		query.setParameter("fromDate", grnRequest.getFromDate());
		query.setParameter("toDate", grnRequest.getToDate());
		if (grnRequest.getReceiptNo() != null) {
			query.setParameter("receiptNumber", '%'+grnRequest.getReceiptNo()+'%');

		}
		if (grnRequest.getVendorId() != null) {
			query.setParameter("vId", grnRequest.getVendorId());
		}

		// need to set search parameter
		query.setResultTransformer(Transformers.aliasToBean(GRNSettledSearchResponse.class));
		return query.list();

	}

	@Override
	public GRNSettledDetailsResponse getGoodReceiptNoteSettledDetails(Integer grnid, Integer unitId) {

		String hql = " select distinct grn.id as grnid   , grns.id as receiptid, grns.receipt_number as receiptnumber , grns.added_date as receiptdate, v.id as vendorid, v.name as vendorname , grn.paid_amount as paidamount from procurement.t_good_receipt_note grn  join public.m_vendor v on grn.vendor_id=v.id  join procurement.t_grn_settlement_receipt_detail grnsd on grnsd.grn_id = grn.id join procurement.t_grn_settlement_receipt grns on grnsd.grn_settlement_id = grns.id  where grnsd.grn_id="
				+ grnid + " and  grn.unit_id=:unitId ";
		Query query = getSession().createSQLQuery(hql);
		// query.setParameter("id", id);
		query.setParameter("unitId", unitId != null && unitId > 0 ? unitId : sessionService.getUnitId());

		// need to set search parameter
		query.setResultTransformer(Transformers.aliasToBean(GRNSettledDetailsResponse.class));
		return (GRNSettledDetailsResponse) query.list().get(0);

	}

	@Override
	public List<SettledDetails> settledDetails(Integer grnid) {

		String hql = " select  distinct grnsd.id  as id ,  reqsto.store_name as requestingstore, sto.store_name as storename, req.type as grntype, grn.grn_number as grnnumber, grn.added_date as grndate , grn.invoice_number as invoicenumber , grn.invoice_date as invoicedate, status.status as status, (SUM(grnd.net_amount)+COALESCE(grnd.round_off_amount,0)+COALESCE(grnd.total_other_amount,0)) as netamount,  cast(grnsd.amount as decimal(11,4)) as paidamount , grn.balance_amount as balanceamount, paystatus.status as paymentstatus from procurement.t_good_receipt_note grn   join  procurement.t_good_receipt_note_detail grnd on grn.id=grnd.grn_id  join public.m_vendor v on grn.vendor_id=v.id  join procurement.t_grn_settlement_receipt_detail grnsd on grnsd.grn_id = grn.id join procurement.t_grn_settlement_receipt grns on grnsd.grn_settlement_id = grns.id  join public.m_store reqsto on grn.requesting_store_id = reqsto.store_id join public.m_store sto on grn.store_id = sto.store_id join public.m_request_type req on  grn.request_id=req.id join public.m_status status on  grn.status_id = status.id  left join public.m_status paystatus on grnsd.status_id =paystatus.id  where grnsd.grn_id="
				+ grnid +" group by grnsd.id,reqsto.store_name,sto.store_name,req.type,grn.grn_number,grn.added_date,grn.invoice_number,grn.invoice_date,status.status,grnd.net_amount,grnd.round_off_amount,grnsd.amount,grn.balance_amount,paystatus.status,grnd.total_other_amount ";
		Query query = getSession().createSQLQuery(hql);
		// query.setParameter("id", id);

		// need to set search parameter
		query.setResultTransformer(Transformers.aliasToBean(SettledDetails.class));
		return query.list();

	}

	@Override
	public List<CreditNoteAmountList> getCreditNoteAmt(Integer id) {
		/*
		 * String hql =
		 * " select id as id , mateinwardid as mateinwardid ,mateinwarddetailsid as mateinwarddetailsid, taxamount as taxamount, taxpercentage as taxpercentage, totalamount as totalamount , creditnoteamount as creditnoteamount , credittaxamount as credittaxamount ,  rgnumber as rgnumber , rgdate as rgdate , replacenumber as replacenumber, creditnetamount as creditnetamount , consumeNetAmount as consumeNetAmount , availablenetamount as availablenetamount from com.param.entity.model.procurement.CreditNoteAmount AS creditNoteAmount "
		 * +
		 * " WHERE creditNoteAmount.goodReceiptNote.id = :id and creditNoteAmount.isDeleted=false"
		 * ;
		 */
		// String hql = " SELECT credit.id as id , credit.mate_inward_id as mateinwardid
		// ,credit.mate_inwarddetails_id as mateinwarddetailsid, credit.tax_amount as
		// taxamount, credit.tax_percentage as taxpercentage, credit.total_amount as
		// totalamount , credit.credit_note_amount as creditnoteamount ,
		// credit.credit_tax_amount as credittaxamount , credit.rg_number as rgnumber ,
		// credit.rg_date as rgdate , credit.replacement_number as replacenumber,
		// credit.credit_net_amount as creditnetamount , credit.consume_net_amount as
		// consumeNetAmount , mateInwardDetails.available_amount as availablenetamount
		// FROM inventory.t_material_inwards_detail mateInwardDetails INNER JOIN
		// procurement.t_grn_detail_creditnote_amount credit ON
		// mateInwardDetails.id=credit.mate_inwarddetails_id where
		// credit.is_deleted=false and credit.grn_id=:id";
		String sql = " SELECT credit.id as id , credit.mate_inward_id as mateinwardid ,credit.mate_inwarddetails_id as mateinwarddetailsid,   credit.tax_amount as taxamount, credit.tax_percentage as taxpercentage,  credit.total_amount as totalamount ,   credit.credit_note_amount as creditnoteamount , credit.credit_tax_amount as credittaxamount ,  credit.rg_number as rgnumber , credit.rg_date as rgdate   , credit.replacement_number as replacenumber, credit.credit_net_amount as creditnetamount , credit.consume_net_amount as consumeNetAmount ,  mateInwards.available_amount as availablenetamount , mateInwards.inword_no as inwordno FROM inventory.t_material_inwards mateInwards  INNER JOIN procurement.t_grn_detail_creditnote_amount credit  ON mateInwards.id=credit.mate_inward_id where credit.is_deleted=false  and credit.grn_id=:id ";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(CreditNoteAmountList.class));
		return query.list();
	}

	@Override
	public void updateCreditNoteAmt(Integer mateInwardId, Double consumeAmt) {
		// String sql = " update inventory.t_material_inwards_detail set
		// available_amount=available_amount-" + consumeAmt+ " where id=" +
		// mateinwarddetailsid;

		String sql = " update inventory.t_material_inwards set available_amount=available_amount-" + consumeAmt
				+ " where id=" + mateInwardId;
		Query query = getSession().createSQLQuery(sql);
		query.executeUpdate();

	}

	@Override
	public GRNResponse getGoodReceiptNoteSettledBP(Integer id) {

		String hql = "SELECT grn.id as id, assetType.id as assetTypeId, assetType.type as assetType, "
				+ "to_char(grn.dueDate,'yyyy-MM-dd') as dueDate,"
				+ "requestType.id as grnTypeId, requestType.type as grnType,"
				+ "grn.grnNumber as grnNumber, grn.addedDate as grnDate, "
				+ "discountType.id as discountTypeId, discountType.type as discountType,"
				+ "status.id as statusId, status.status as status,"
				+ "approvalStatus.id as approvalStatusId, approvalStatus.status as approvalStatus,"
				+ "store.storeId as storeId, store.storeName as StoreName,"
				+ "grn.dcDate as dcDate, grn.dcNumber as dcNumber, grn.discount as discount, grn.invoiceDate as invoiceDate,"
				+ "grn.invoiceNumber as invoiceNumber, grn.inwardDate as inwardDate, grn.inwardNumber as inwardNumber, "
				+ "grn.isConsignment as isConsignment, grn.netAmount as netAmount, grn.roundOff as roundOff,"
				+ "grn.totalAmount as totalAmount, grn.totalDiscount as totalDiscount, grn.totalOtherAmount as totalOtherAmount,"
				+ "grn.totalTaxAmmount as totalTaxAmount, " + "vendor.id as vendorId, vendor.name as vendorName,"
				+ "grn.remark as remark , grn.billTypeName as billTypeName,"
				+ "addedBy.userName as addedBy , requestingStore.storeId as requestingStoreId , requestingStore.storeName as requestingStoreName , grn.totalCreditNoteAmount as totalCreditNoteAmount  "
				+ " FROM GoodReceiptNote AS grn " + " LEFT JOIN grn.assetType AS assetType "
				+ " LEFT JOIN grn.requestType AS requestType " + " LEFT JOIN grn.discountType AS discountType "
				+ " LEFT JOIN grn.status AS status " + " LEFT JOIN grn.approvalStatus AS approvalStatus "
				+ " LEFT JOIN grn.store AS store " + " LEFT JOIN grn.requestingStore AS requestingStore "
				+ " LEFT JOIN grn.vendor AS vendor " + " LEFT JOIN grn.addedBy AS addedBy "
				+ "WHERE grn.id = :id  AND grn.isActive = true AND grn.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		// query.setParameter("unitId", sessionService.getUnitId());

		query.setResultTransformer(Transformers.aliasToBean(GRNResponse.class));
		return (GRNResponse) query.uniqueResult();

	}

	@Override
	public List<PreviousGRNSearchResponse> previousNewGrnDetails(Integer vendorId, Integer itemid) {

		String hql = "select mv.name as \"vendorName\",grn.added_date as grnDate, grn.grn_number as grnNumber, grn.invoice_number as invoiceNumber, grn.dc_number as dcNumber, gd.grn_approved_quantity as grnApprovedQty , gd.grn_received_quantity as grnReceivedQty, uomunit.unit as Unit , gd.bonus_approved_quantity  as bonusApprovedQty , gd.bonus_received_quantity as bonusReceivedQty , bonusuomunit.unit as bonusUnit, gd.mrp as mrp, gd.cop as cop, 	gd.expiry_date as expiryDate, gd.batch_no as batchNumber from 	procurement.t_good_receipt_note grn join procurement.t_good_receipt_note_detail gd  on (grn.id = gd.grn_id) left join public.m_uom_unit uomunit on (gd.grn_uom_unit_id = uomunit.id)  left join public.m_uom_unit bonusuomunit on (gd.bonus_uom_unit_id = bonusuomunit.id) left join public.m_vendor mv on grn.vendor_id  = mv.id where  gd.item_id = :item_id  ";
		// grn.vendor_id = 1 and gd.item_id =12
		Query query = getSession().createSQLQuery(hql);
		//query.setParameter("vendor_id", vendorId);
		query.setParameter("item_id", itemid);
		// query.setParameter("grnId", grnId);

		query.setResultTransformer(Transformers.aliasToBean(PreviousGRNSearchResponse.class));

		return query.list();

	}

	@Override
	public List<CreditNoteVendorWise> getReturnableGatepassDetails(Integer mateinwardid) {

		// String hql = "select mateInward.id as mateInwardId , mateInwardDetails.id as
		// mateInwardDetailsId, mateInwardDetails.tax_amount as taxAmount,
		// mateInwardDetails.tax_percentage as
		// taxPercentage,mateInwardDetails.total_amount as totalAmount,
		// mateInwardDetails.credit_note_amount as creditNoteAmount,
		// mateInwardDetails.credit_tax_amount as creditTaxAmount ,
		// mateInwardDetails.credit_net_amount as creditNetAmount , grn.grn_number as
		// grnNumber , grn.added_date as grnDate, mateInward.replacement_number as
		// replaceNumber from inventory.t_material_inwards mateInward join
		// inventory.t_material_inwards_detail mateInwardDetails on
		// mateInward.id=mateInwardDetails.material_inwards_id join
		// procurement.t_good_receipt_note_detail grndetails on
		// mateInwardDetails.returnable_gatepass_detail_id=grndetails.id join
		// procurement.t_good_receipt_note grn on grndetails.grn_id=grn.id where
		// mateInward.vendor_id="+vendorid +" and mateInward.status_id in (2)" ;
		String sql = " select distinct mateInward.id as mateInwardId , grn.ret_gp_no as rgNumber , grn.added_date as rgDate from inventory.t_material_inwards mateInward join inventory.t_material_inwards_detail mateInwardDetails  on mateInward.id=mateInwardDetails.material_inwards_id join inventory.t_returnable_gatepass_detail grndetails on  mateInwardDetails.returnable_gatepass_detail_id=grndetails.id join inventory.t_returnable_gatepass grn on grndetails.returnable_gatepass_id=grn.id where mateInward.id="
				+ mateinwardid + " and mateInward.status_id in (2) ";
		Query query = getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.aliasToBean(CreditNoteVendorWise.class));
		// query.setParameter("vendorid", vendorid);

		return query.list();

	}

	@Override
	public GoodReceiptNoteDetail getGoodReceiptNoteDetailById(Integer id) {

		String hql = "SELECT goodReceiptNoteDetail FROM GoodReceiptNoteDetail AS goodReceiptNoteDetail "
				+ " WHERE goodReceiptNoteDetail.id = :id  AND goodReceiptNoteDetail.isActive = true AND goodReceiptNoteDetail.isDeleted = false "
				+ " ORDER BY goodReceiptNoteDetail.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return (GoodReceiptNoteDetail) query.uniqueResult();

	}

	@Override
	public GrnOtherChargesMapper getGRNOtherChargesMapperById(Integer id) {

		String hql = "SELECT grnOtherChargesMapper FROM com.param.entity.model.procurement.GrnOtherChargesMapper AS grnOtherChargesMapper "
				+ " WHERE grnOtherChargesMapper.id = :id  AND grnOtherChargesMapper.isActive = true AND grnOtherChargesMapper.isDeleted = false "
				+ " ORDER BY grnOtherChargesMapper.id ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return (GrnOtherChargesMapper) query.uniqueResult();

	}

	@Override
	public void updateOtherCharges(GrnOtherChargesMapper otherChargesmapper) {

		getSession().saveOrUpdate(otherChargesmapper);

	}

	@Override
	public BigInteger getLastId() {

		String sql = " select count(id) from procurement.t_grn_settlement_receipt  ";
		Query query = getSession().createSQLQuery(sql);

		return (BigInteger) query.uniqueResult();

	}

	@Override
	public ApplicationConfiguration getApplicationConfiguration(Integer unitId) {

		String hql = "SELECT applicationConfiguration FROM ApplicationConfiguration AS applicationConfiguration "
				+ "WHERE applicationConfiguration.unit.unitId = :unitId AND applicationConfiguration.isActive = true AND applicationConfiguration.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());

		return (ApplicationConfiguration) query.uniqueResult();

	}

	@Override
	@Transactional
	public Batch saveOrUpdateBatch(Batch batchToSave) {

		getSession().saveOrUpdate(batchToSave);
		return batchToSave;

	}

	@Override
	public List<TaxComponentDetailsDto> getTaxDetails(Integer id) {
		String sql = " select  " + 
				"mt.id  as \"taxId\", " + 
				"mt.tax_percentage as \"taxPercentage\", " + 
				"mtd.tax_component_id  as \"taxComponentId\", " + 
				"mtd.tax_percentage as \"taxComponentPercentage\", " + 
				"mtc.component as \"taxComponentName\" " + 
				"from m_tax_detail mtd " + 
				"inner join m_tax mt on mt.id  = mtd.tax_id  " + 
				"inner join m_tax_component mtc on mtc.id  = mtd.tax_component_id   " + 
				"where mt.id  ="+id;
		Query query = getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.aliasToBean(TaxComponentDetailsDto.class));

		return query.list();

	}

	@Override
	public Object getInvoiceNumber(GRNRequest grnRequest) {
		String sql=" select invoice_number as \"invoiceNumber\" from procurement.t_good_receipt_note where organization_id=:orgId "
				+ " and unit_id=:unitId  and vendor_id=:vendorId";
				Query query = getSession().createSQLQuery(sql);
		query.setParameter("unitId", grnRequest.getUnitId() != null ? grnRequest.getUnitId() : sessionService.getUnitId());
		query.setParameter("orgId", grnRequest.getOrgId() != null ? grnRequest.getOrgId() : sessionService.getOrganizationId());
//		query.setParameter("invoiceNumber", grnRequest.getInvoiceNumber() != null ? grnRequest.getInvoiceNumber() : 0);//and invoice_number=:invoiceNumber
		query.setParameter("vendorId", grnRequest.getVendorId() != null ? grnRequest.getVendorId() : 0);
		
		return query.list();
	}
	@Override
	public Double getNetAmountSumByGrnIdFromGrnDetails(Integer id, Integer unitId, Integer organizationId) {
		String hql = "SELECT SUM(tgrnd.net_amount) FROM procurement.t_good_receipt_note_detail tgrnd "
				+ "WHERE tgrnd.unit_id = :unitId AND tgrnd.organization_id=:organizationId AND tgrnd.grn_id=:grnId AND tgrnd.is_active = true AND tgrnd.is_deleted = false ";
		Query query = getSession().createSQLQuery(hql);
		query.setParameter("grnId", id);
		query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query.setParameter("organizationId", organizationId != null ? organizationId : sessionService.getOrganizationId());

		return (Double) query.uniqueResult();
	}

	@Override
	public DetailsConsignmentStockConversionSaveRequest getTransactionDetails(Long id) {
		
		String sql = " select tst.transaction_details_id as \"transactionDetailsId\" ,tst.transaction_id as \"transactionId\" "
				+ " from  inventory.t_stock_transaction tst  where tst.batch_id = "+id+" order by tst.id asc limit 1  ";
		Query query = getSession().createSQLQuery(sql);
		query.setResultTransformer(Transformers.aliasToBean(DetailsConsignmentStockConversionSaveRequest.class));
		
		return  (DetailsConsignmentStockConversionSaveRequest) query.uniqueResult();
	}

	@Override
	public Boolean getIsSkipPhysicalInspection(Integer unitId, Integer orgId) {
		String sql = " select mac.is_skip_physical_inspection "
				+ " from  public.m_application_configuration mac  where mac.unit_id = "+unitId+" and mac.organization_id="+orgId+"";
		Query query = getSession().createSQLQuery(sql);
		return (Boolean) query.uniqueResult();
	}

	@Override
	public List<GRNOtherChargesMapper> getGRNOtherChargesMapperBP1(Integer allDetailIds) {
		/*
		 * String hql =
		 * "SELECT grnOtherChargesMapper.id as id, grnOtherChargesMapper.otherCharge.id as otherChargesId, "
		 * +
		 * "grnOtherChargesMapper.otherCharge.description as otherCharges, grnOtherChargesMapper.amount as amount, "
		 * + "grnOtherChargesMapper.goodReceiptNoteDetail.id as grnDetailId " +
		 * " FROM com.param.entity.model.procurement.GrnOtherChargesMapper AS grnOtherChargesMapper "
		 * +
		 * " WHERE grnOtherChargesMapper.goodReceiptNoteDetail.id in (:id)  AND grnOtherChargesMapper.isActive = true AND grnOtherChargesMapper.isDeleted = false "
		 * + " ORDER BY grnOtherChargesMapper.id ";
		 */
		String hql = "SELECT grnOtherChargesMapper.id as id, grnOtherChargesMapper.otherCharge.id as otherChargesId, "
				+ "grnOtherChargesMapper.otherCharge.description as otherCharges, grnOtherChargesMapper.amount as amount, "
				+ "grnOtherChargesMapper.goodReceiptNote.id as grnId "
				+ " FROM com.param.entity.model.procurement.GrnOtherChargesMapper AS grnOtherChargesMapper "
				+ " WHERE grnOtherChargesMapper.goodReceiptNote.id =:id  AND grnOtherChargesMapper.isActive = true AND grnOtherChargesMapper.isDeleted = false AND grnOtherChargesMapper.isFromGrn=true "
				+ " ORDER BY grnOtherChargesMapper.id ASC ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", allDetailIds);
//		query.setMaxResults(1);
		query.setResultTransformer(Transformers.aliasToBean(GRNOtherChargesMapper.class));
		return query.list();
	}

	@Override
	public Double geRoundOffAmountSumByGrnIdFromGrnDetails(Integer id, Integer unitId, Integer organizationId) {
			String hql = "SELECT tgrnd.round_off_amount FROM procurement.t_good_receipt_note_detail tgrnd "
					+ "WHERE tgrnd.unit_id = :unitId AND tgrnd.organization_id=:organizationId AND tgrnd.grn_id=:grnId AND tgrnd.is_active = true AND tgrnd.is_deleted = false ";
			Query query = getSession().createSQLQuery(hql);
			query.setMaxResults(1);
			query.setParameter("grnId", id);
			query.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
			query.setParameter("organizationId", organizationId != null ? organizationId : sessionService.getOrganizationId());
			return (Double) query.uniqueResult();
	}

	@Override
	public boolean updateBatchId(Long id, Integer grnBonusBatchExpiryId) {
		getSession().createSQLQuery("update procurement.t_grn_bonus_batch_expiry set batch_id="+id+" where grn_bonus_batch_expiry_id="+grnBonusBatchExpiryId+" ").executeUpdate();
		return true;
		
		
	}

	@Override
	public boolean updateBatchIdForBonusGrn(Integer key, Long value) {
		getSession().createSQLQuery("update procurement.t_grn_bonus_batch_expiry set batch_id="+value+" where grn_bonus_batch_expiry_id="+key+" ").executeUpdate();
		return true;
	}

	@Override
	public Date getexpiry(Integer grnBonusBatchExpiryId) {
		return (Date) getSession().createSQLQuery("select date(expiry_date) from procurement.t_grn_bonus_batch_expiry where grn_bonus_batch_expiry_id="+grnBonusBatchExpiryId+" ").uniqueResult();
	}

	@Override
	public Double getCreditNoteAmount(Integer grnId, Integer unitId) {
		String hql = "  SELECT total_credit_note_amount FROM procurement.t_good_receipt_note where id="
				+ grnId + " and unit_id="
				+ unitId;

		Query query = sessionFactory.getCurrentSession().createSQLQuery(hql);
		return (Double) query.uniqueResult();
	}

	@Override
	public void updateOtherCharges(Integer id) {
		sessionFactory.getCurrentSession().createSQLQuery(" update procurement.t_other_charges_grn_mapper set is_active=false and is_deleted=true where  grn_id="+id)
        .executeUpdate();
	}

	@Override
	public Double getPaidAmount(Integer grnId, Integer orgnizationId, Integer unitId) {
		String hql3 = "SELECT tgrnd.paid_amount FROM procurement.t_good_receipt_note tgrnd "
				+ "WHERE tgrnd.unit_id = :unitId AND tgrnd.organization_id=:organizationId AND tgrnd.id=:grnId AND tgrnd.is_active = true AND tgrnd.is_deleted = false ";
		Query query3 = getSession().createSQLQuery(hql3);
		query3.setParameter("grnId", grnId);
		query3.setMaxResults(1);
		query3.setParameter("unitId", unitId != null ? unitId : sessionService.getUnitId());
		query3.setParameter("organizationId", orgnizationId != null ? orgnizationId : sessionService.getOrganizationId());
		return (Double) query3.uniqueResult();
	}

	@Override
	public void updateSettlementReceiptDetailsStatusByGrnId(Integer grnId) {
		if(grnId!=null)
		{
		String s = " update  procurement.t_grn_settlement_receipt_detail set status_id=20 where grn_id="+grnId;
		Query query = getSession().createSQLQuery(s);
		query.executeUpdate();
		}
		
	}

	@Override
	public BigInteger alreadyGoodReceiptNoteExits(List<Integer> grnIds) {
		String hql = " select count(*) from  procurement.t_physical_inspection tpi where tpi.is_active  = true and is_deleted = false and grn_id in("+grnIds.toString().replaceAll("\\[", "").replaceAll("\\]", "")+") ";

		Query query = sessionFactory.getCurrentSession().createSQLQuery(hql);
		return (BigInteger) query.uniqueResult();
	}


	@Override
	public void saveDetails(GoodReceiptNoteDetailLog log1) {
		getSession().save(log1);
		
	}

	@Override
	public List<GoodReceiptNote> getGoodReceiptNoteAll(List<Integer> grnIds) {
		String hql = "SELECT goodReceiptNote FROM GoodReceiptNote AS goodReceiptNote "
				+ "WHERE goodReceiptNote.id in (:id)  AND goodReceiptNote.isActive = true AND goodReceiptNote.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameterList("id", grnIds);

		return  query.list();
	}

	@Override
	public BigInteger checkAlreadyExists(GRNRequest grnRequest) {
		String hql = " select count(*) from procurement.t_good_receipt_note tgrn where"
				+ " tgrn.vendor_id = "+grnRequest.getVendorId()+" and tgrn.invoice_number ='"+grnRequest.getInvoiceNumber()+"' "
						+ "and tgrn.organization_id =  "+grnRequest.getOrgId();

		Query query = sessionFactory.getCurrentSession().createSQLQuery(hql);
		return (BigInteger) query.uniqueResult();
	}

}

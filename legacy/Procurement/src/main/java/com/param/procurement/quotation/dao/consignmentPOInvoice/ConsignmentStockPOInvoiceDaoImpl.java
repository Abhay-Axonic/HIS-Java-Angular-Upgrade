package com.param.procurement.quotation.dao.consignmentPOInvoice;

import java.math.BigInteger;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.procurement.ConsignmentStockPOInvoice;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.procurement.quotation.dto.consignmentPOInvoice.ConsignmentStockPODetailResponseDto;
import com.param.procurement.quotation.dto.consignmentPOInvoice.ConsignmentStockPORequestDto;
import com.param.procurement.quotation.dto.consignmentPOInvoice.ConsignmentStockPOResponseDto;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Repository
public class ConsignmentStockPOInvoiceDaoImpl extends AbstractDao<Integer, Integer> implements IConsignmentStockPOInvoiceDao  {

	final static Logger logger = Logger.getLogger(ConsignmentStockPOInvoiceDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	SessionService sessionService;
	
	@Override
	public List<ConsignmentStockPOResponseDto> getConsignmentPOInvoiceList(
			ConsignmentStockPORequestDto consignmentStockPORequestDto) {
		try {
		String hql = "SELECT cpoi.id as id, cpoi.invoiceNumber as invoiceNumber,"
				+ " po.poNumber as poNumber, po.addedDate as poDate, grn.grnNumber as grnNumber, "
				+ " requestStore.storeName as requestingStore,issueStore.storeName as issuingStore, po.id as poId , issueStore.storeId as issueingStoreId,"
				+ " vendor.id as vendorId, vendor.name as vendorName, user.userName as addedBy,status.status as status, status.id as statusId "
				
				+ " ,cpoi.invoiceDate as invoiceDate, assetType.id as assetTypeId "
				
				+ " FROM ConsignmentStockPOInvoice as cpoi "
				+ " INNER JOIN cpoi.purchaseOrder as po "
				+ " INNER JOIN cpoi.goodReceiptNote as grn "
				+ " INNER JOIN grn.requestingStore  as requestStore "
				+ " INNER JOIN grn.store as issueStore "
				+ " INNER JOIN cpoi.vendor as vendor "
				+ " INNER JOIN cpoi.assetType as assetType "
				+ " INNER JOIN cpoi.addedBy as user "
				+ " INNER JOIN cpoi.status as status "
				+ " WHERE cpoi.isActive = true AND cpoi.isDeleted = false AND cpoi.unit.unitId = :unitId "
				+ " AND cpoi.addedDate BETWEEN :fromDate AND :toDate "
				+ "	AND (:requestingStoreId = 0 OR requestStore.id = :requestingStoreId) "
				+ "	AND (:issuingStoreId = 0 OR issueStore.id = :issuingStoreId) "
				+ " AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ " AND (:vendorName = '' OR lower(cpoi.vendor.name) LIKE :vendorName ) "
				+ " AND (:statusId=0  OR status.id =:statusId) "
				+ " AND (:poNumber = '' OR lower(po.poNumber) LIKE :poNumber)";
				
				Query query = getSession().createQuery(hql);
				query.setParameter("fromDate", Util.getDate(consignmentStockPORequestDto.getFromDate()));
				query.setParameter("toDate", Util.getDate(consignmentStockPORequestDto.getToDate()));				
				query.setParameter("requestingStoreId",consignmentStockPORequestDto.getRequestingStoreId() != null 
									? consignmentStockPORequestDto.getRequestingStoreId() : 0);
				query.setParameter("issuingStoreId",consignmentStockPORequestDto.getIssuingStoreId() != null 
									? consignmentStockPORequestDto.getIssuingStoreId() : 0);
				if (consignmentStockPORequestDto.getVendorId() != null
						&& consignmentStockPORequestDto.getVendorId() > 0) {
					query.setParameter("vendorId", consignmentStockPORequestDto.getVendorId() != null
									? consignmentStockPORequestDto.getVendorId() : 0);
					query.setParameter("vendorName", "");
				} else if (consignmentStockPORequestDto.getVendorName() != null) {
					query.setParameter("vendorId", 0);
					query.setParameter("vendorName", consignmentStockPORequestDto.getVendorName() != null
									? consignmentStockPORequestDto.getVendorName().toLowerCase() + '%' : "");
				} else {
					query.setParameter("vendorId", 0);
					query.setParameter("vendorName", "");
				}
				query.setParameter("poNumber", consignmentStockPORequestDto.getPoNumber() != null ? '%'+consignmentStockPORequestDto.getPoNumber().toLowerCase() + '%' : "" );
				query.setParameter("statusId",consignmentStockPORequestDto.getStatusId() != null 
						?  consignmentStockPORequestDto.getStatusId() : 0);
				query.setParameter("unitId", consignmentStockPORequestDto.getUnitId()!=null ? consignmentStockPORequestDto.getUnitId(): sessionService.getUnitId());
				query.setResultTransformer(Transformers.aliasToBean(ConsignmentStockPOResponseDto.class));
				return query.list();
		}catch (Exception e) {
			logger.error("getConsignmentPOInvoiceList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<ConsignmentStockPOResponseDto> searchConsignmentPOBP(ConsignmentStockPORequestDto consignmentStockPORequestDto) {

		String hql = "SELECT distinct  purchaseOrder.id as id, vendor.name as vendorName, "
				+ " store.storeId as storeId, store.storeName as storeName, purchaseOrder.poNumber as poNumber,"
				+ " purchaseOrder.addedDate as poDate"
				+ " ,grn.id as grnId, grn.grnNumber as grnNumber , requestingStore.storeId as requestingStoreId "
				+ " FROM PurchaseOrder AS purchaseOrder "				
				+ " INNER JOIN purchaseOrder.addedBy as user   "
				+ " LEFT JOIN purchaseOrder.store as store WITH store.unit.unitId = :unitId "				
				+ " LEFT JOIN purchaseOrder.requestingStore as requestingStore WITH store.unit.unitId = :unitId "				
				+ " LEFT JOIN purchaseOrder.assetType as assetType  "
				+ "	LEFT JOIN purchaseOrder.status as status "
				+ " INNER JOIN purchaseOrder.approvalStatus as approvalStatus "
				+ " LEFT JOIN purchaseOrder.assetType as assetType "
				+ " LEFT JOIN purchaseOrder.requestType as requestType "
				+ " LEFT JOIN purchaseOrder.vendor as vendor "				
				+ " INNER JOIN purchaseOrder.purchaseOrderDetailList as pod "//added
				+ " INNER JOIN pod.grnDetail as grnd "//added
				+ " INNER JOIN grnd.goodReceiptNote as grn "//added		
				+ " WHERE purchaseOrder.isActive = true AND purchaseOrder.isDeleted = false AND purchaseOrder.unit.unitId = :unitId "				
				+ " AND purchaseOrder.isConsignment = true "//added				
				+ " AND purchaseOrder.addedDate BETWEEN :fromDate AND :toDate "
				+ "	AND (:storeId = 0 OR store.id = :storeId) "
				+ "	AND (:assetTypeId = 0 OR assetType.id = :assetTypeId) "
				+ " AND (:vendorId = 0 OR purchaseOrder.vendor.id = :vendorId) "
				+ " AND (:vendorName = '' OR lower(purchaseOrder.vendor.name) LIKE :vendorName ) "
				+ " AND (approvalStatus.id IN (5,10)) "
				+ " AND (:poNumber = '' OR lower(purchaseOrder.poNumber) LIKE :poNumber)";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(consignmentStockPORequestDto.getFromDate()));
		query.setParameter("toDate", Util.getDate(consignmentStockPORequestDto.getToDate()));
		query.setParameter("storeId",
				consignmentStockPORequestDto.getStoreId() != null ? consignmentStockPORequestDto.getStoreId() : 0);
		query.setParameter("assetTypeId",
				consignmentStockPORequestDto.getAssetTypeId() != null ? consignmentStockPORequestDto.getAssetTypeId() : 0);

		if (consignmentStockPORequestDto.getVendorId() != null && consignmentStockPORequestDto.getVendorId() > 0) {
			query.setParameter("vendorId",
					consignmentStockPORequestDto.getVendorId() != null ? consignmentStockPORequestDto.getVendorId() : 0);
			query.setParameter("vendorName", "");
		} else if (consignmentStockPORequestDto.getVendorName() != null) {
			query.setParameter("vendorId", 0);
			query.setParameter("vendorName",
					consignmentStockPORequestDto.getVendorName() != null
							? consignmentStockPORequestDto.getVendorName().toLowerCase() + '%'
							: "");
		} else {
			query.setParameter("vendorId", 0);
			query.setParameter("vendorName", "");
		}
		//query.setParameter("approvalStatusId", consignmentStockPORequestDto.getStatusId()!= null ? consignmentStockPORequestDto.getStatusId() : 0);
		//query.setParameter("approvalStatusId", 5);// 5 indicate that status is approved
		
		query.setParameter("poNumber", consignmentStockPORequestDto.getPoNumber() != null ? consignmentStockPORequestDto.getPoNumber().toLowerCase() + '%' : "" );
		
		//query.setParameter("detailStatusId", 5);
		query.setParameter("unitId",consignmentStockPORequestDto.getUnitId()!=null ? consignmentStockPORequestDto.getUnitId(): sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ConsignmentStockPOResponseDto.class));
		return query.list();
	}

	@Override
	public List<ConsignmentStockPODetailResponseDto> getConsignmentPoDetails(List<Integer> poIds) {
		String sql = " select pod.id as id, po.id as poId, item.id as itemId, item.code as itemCode, item.name as itemName,"
				+ " coalesce(pod.po_quantity,0) as poQuantity, coalesce(pod.bonus_approved_quantity, 0) as bonusQuantity, "
				+ " pod.cop as cop, pod.mrp as mrp "
				
				+ " ,pod.tax_id as \"taxId\", pod.tax_percentage as \"taxPercentage\", tax.name as \"taxName\", pod.manufacturer_id as \"manufacturerId\", "//added
				+ " manufacturer.name as \"manufacturerName\" ,pod.specification as \"specification\",pod.remark as \"remark\", pod.total_amount as \"totalAmount\" ,"//added
				+ " pod.tax_amount as \"taxAmount\", pod.discount as \"discount\", pod.total_discount as \"discountAmount\", "
				+ " pod.total_other_amount as \"totalOtherAmount\", pod.net_amount as \"netAmount\" ,pod.grn_detail_id as \"grnDetailId\" "//added
				
				+ "	from procurement.t_purchase_order_detail pod "
				
				+ " inner join public.m_tax as tax on  tax.id = pod.tax_id "//added
				+ " inner join public.m_manufacturer as manufacturer on  manufacturer.id = pod.manufacturer_id "//added
				
				//+ " inner join procurement.t_consignment_purchase_order_invoice_detail as cpoid on  cpoid.po_detail_id != pod.id or cpoid.status_id !=8 "//added
				
				+ " inner join procurement.t_purchase_order po on po.id = pod.purchase_order_id and po.id in (:poIds) "
				+ " inner join public.m_item item on item.id = pod.item_id"
				+ " left join(select pod.id as podId, sum(case "
				+ " when "
				+ "	grnd.grn_approved_quantity isnull"
				+ "	then"
				+ "		coalesce(grnd.grn_received_quantity,0)"
				+ "	else	"
				+ "		grnd.grn_approved_quantity"
				+ "		end) as qty  "
				+ "	from procurement.t_good_receipt_note_detail grnd"
				+ "	inner join procurement.t_purchase_order_detail pod on pod.id = grnd.po_detail_id where grnd.status_id !=:grnDetailsStatusId  "
				+ "	group by pod.id) as grnDetails on grnDetails.podId = pod.id"
				+ "	where pod.status_id in (5,10) and pod.is_active = true and pod.is_deleted = false "
				+ " and pod.id not in (select po_detail_id from procurement.t_consignment_purchase_order_invoice_detail where status_id!=9) "//added
				+ " order by pod.id";

		Query query = getSession().createSQLQuery(sql).addScalar("id").addScalar("itemId").addScalar("itemCode")
				.addScalar("itemName").addScalar("poQuantity").addScalar("bonusQuantity").addScalar("cop")
				.addScalar("mrp").addScalar("poId")
				//.addScalar("grnApprovedAndReceivedSum", StandardBasicTypes.INTEGER);
				.addScalar("taxId").addScalar("taxPercentage").addScalar("taxName").addScalar("taxAmount").addScalar("manufacturerId").addScalar("manufacturerName")
				.addScalar("specification").addScalar("remark").addScalar("totalAmount")
				.addScalar("discount").addScalar("discountAmount").addScalar("totalOtherAmount").addScalar("netAmount").addScalar("grnDetailId");

		query.setParameterList("poIds", poIds);
	//	query.setParameter("detailStatusId", 5);
		query.setParameter("grnDetailsStatusId", com.param.common.constants.Status.Closed.getStatus());
		query.setResultTransformer(Transformers.aliasToBean(ConsignmentStockPODetailResponseDto.class));
		return query.list();
	}

	@Override
	public ConsignmentStockPOInvoice saveConsignmentPOInvoice(ConsignmentStockPOInvoice consignmentStockPOInvoice) {
		getSession().save(consignmentStockPOInvoice);
		return consignmentStockPOInvoice;
	}

	@Override
	public List<ConsignmentStockPODetailResponseDto> getConsignmentStockPOInvoiceDetail(ConsignmentStockPOResponseDto consignmentStockPOResponseDto) {
		try {
		/*String sql = " select cpoid.id as id, cpoid.specification as specification, cpoid.remark as remark, "
				
				+ " item.id as itemId, item.code as itemCode, item.name as itemName, "				
				+ " coalesce(pod.po_approved_quantity,0) as poQuantity, coalesce(pod.bonus_approved_quantity, 0) as bonusQuantity, "
				+ " pod.cop as cop, pod.mrp as mrp, pod.tax_id as taxId, pod.tax_percentage as taxPercentage, pod.manufacturer_id as manufacturerId, "
				+ " pod.tax_amount as taxAmount, pod.discount as discount, pod.total_discount as discountAmount, "
				+ " pod.total_other_amount as totalOtherAmount, pod.net_amount as netAmount, pod.total_amount as totalAmount, pod.grn_detail_id as grnDetailId, "
				+ " tax.name as taxName, manufacturer.name as manufacturerName "
				
				+ "	from procurement.t_consignment_purchase_order_invoice_detail cpoid "//added
				
				+ " inner join procurement.t_purchase_order_detail as pod on  pod.id= cpoid.po_detail_id "
				+ " inner join public.m_tax as tax on  tax.id = pod.tax_id "
				+ " inner join public.m_manufacturer as manufacturer on  manufacturer.id = pod.manufacturer_id "
				+ " inner join public.m_item item on item.id = pod.item_id"
				+ "	where cpoid.cpo_invoice_id = :cpoiId and cpoid.is_active = true and cpoid.is_deleted = false order by cpoid.id";*/
		
		/* start code for resolved HIS-9322 Consignment PO invoice to have editable fields */
		String sql = " select cpoid.id as id, cpoid.specification as specification, cpoid.remark as remark, "				
				+ " item.id as itemId, item.code as itemCode, item.name as itemName, "				
				+ " coalesce(pod.po_quantity,0) as poQuantity, coalesce(pod.bonus_approved_quantity, 0) as bonusQuantity, "
				+ " cpoid.cop as cop, cpoid.mrp as mrp, cpoid.tax_id as taxId, cpoid.tax_percentage as taxPercentage, "
				+ " pod.manufacturer_id as manufacturerId, "
				+ " cpoid.tax_amount as taxAmount, cpoid.discount as discount, cpoid.total_discount as discountAmount, "
				+ " cpoid.total_other_amount as totalOtherAmount, cpoid.net_amount as netAmount, cpoid.total_amount as totalAmount, "
				+ "	pod.grn_detail_id as grnDetailId, "
				+ " tax.name as taxName, manufacturer.name as manufacturerName "				
				+ "	from procurement.t_consignment_purchase_order_invoice_detail cpoid "//added				
				+ " inner join procurement.t_purchase_order_detail as pod on  pod.id= cpoid.po_detail_id "
				+ " inner join public.m_tax as tax on  tax.id = pod.tax_id "
				+ " inner join public.m_manufacturer as manufacturer on  manufacturer.id = pod.manufacturer_id "
				+ " inner join public.m_item item on item.id = pod.item_id"
				+ "	where cpoid.cpo_invoice_id = :cpoiId and cpoid.is_active = true and cpoid.is_deleted = false order by cpoid.id";
		/* end code for resolved HIS-9322 Consignment PO invoice to have editable fields */

		Query query = getSession().createSQLQuery(sql).addScalar("id").addScalar("itemId").addScalar("itemCode")
				.addScalar("itemName").addScalar("poQuantity").addScalar("bonusQuantity")
				.addScalar("cop").addScalar("mrp")
				.addScalar("taxId").addScalar("taxPercentage").addScalar("taxName").addScalar("taxAmount").addScalar("manufacturerId").addScalar("manufacturerName")
				.addScalar("specification").addScalar("remark").addScalar("totalAmount")
				.addScalar("discount").addScalar("discountAmount").addScalar("totalOtherAmount").addScalar("netAmount").addScalar("grnDetailId");

		query.setParameter("cpoiId", consignmentStockPOResponseDto.getId());
		query.setResultTransformer(Transformers.aliasToBean(ConsignmentStockPODetailResponseDto.class));
		return query.list();
		}catch(Exception e) {
			logger.error("getConsignmentStockPOInvoiceDetail failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Integer updateConsignmentStockPOInvoiceDetail(ConsignmentStockPODetailResponseDto consignmentStockPODetailResponseDto) {
		try {
		String sql=" update procurement.t_consignment_purchase_order_invoice_detail  set remark= :remark ,specification = :specification, "
				+ " updated_date =:updatedDate, updated_by =:updatedBy "
				+ " where id = :id";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("id", consignmentStockPODetailResponseDto.getId());
		query.setParameter("remark", consignmentStockPODetailResponseDto.getRemark());
		query.setParameter("specification", consignmentStockPODetailResponseDto.getSpecification());
		query.setParameter("updatedDate", TimeZoneComponent.getDateByzone(sessionService.getUnitId())); //method not in use
		query.setParameter("updatedBy", sessionService.getUserId());
		query.executeUpdate();
		return consignmentStockPODetailResponseDto.getId();
		} catch(Exception e) {
			logger.error("updateConsignmentStockPOInvoiceDetail failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public ConsignmentStockPOInvoice updateConsignmentStockPOInvoice(ConsignmentStockPOResponseDto consignmentStockPOResponseDto) {
		try {
			String hql = "SELECT consignmentStockPOInvoice FROM ConsignmentStockPOInvoice AS consignmentStockPOInvoice "					
					+ " WHERE consignmentStockPOInvoice.isActive = true AND consignmentStockPOInvoice.isDeleted = false AND "
					+ " consignmentStockPOInvoice.id=:id AND consignmentStockPOInvoice.unit.unitId = :unitId "
					+ " ORDER BY consignmentStockPOInvoice.id DESC";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", consignmentStockPOResponseDto.getId());
			query.setParameter("unitId", consignmentStockPOResponseDto.getUnitId()!=null ? consignmentStockPOResponseDto.getUnitId(): sessionService.getUnitId());
			return (ConsignmentStockPOInvoice) query.uniqueResult();
		} catch(Exception e) {
			logger.error("updateConsignmentStockPOInvoice failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public BigInteger getInvoiceNumberCount(String invoiceNumber) {
		try {
		String sql ="SELECT count(*) as count FROM procurement.t_consignment_purchase_order_invoice as cpoinvoice "
				+ "where cpoinvoice.invoice_number ='"+invoiceNumber+"' and  cpoinvoice.unit_id=" + sessionService.getUnitId();
		Query query = getSession().createSQLQuery(sql);
		return (BigInteger) query.uniqueResult();
		} catch(Exception e) {
			logger.error("getInvoiceNumberCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public ConsignmentStockPOInvoice getPurchaseOrderInvoice(Integer id) {

		try {
			String hql = "SELECT consignmentStockPOInvoice FROM ConsignmentStockPOInvoice AS consignmentStockPOInvoice "					
					+ " WHERE consignmentStockPOInvoice.isActive = true AND consignmentStockPOInvoice.isDeleted = false AND "
					+ " consignmentStockPOInvoice.id=:id  "
					+ " ORDER BY consignmentStockPOInvoice.id DESC";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			return (ConsignmentStockPOInvoice) query.uniqueResult();
		} catch(Exception e) {
			logger.error("getPurchaseOrderInvoice failed", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public void update(ConsignmentStockPOInvoice consignmentStockPOInvoice) {
		getSession().update(consignmentStockPOInvoice);
		
	}
	
}

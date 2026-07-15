package com.param.procurement.consignment.dao.ConsignmentPOCreation;

import java.util.List;

import com.param.common.constants.Status;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.model.procurement.ConsignmentInward;
import com.param.entity.model.procurement.ConsignmentPurchaseOrder;
import com.param.entity.model.procurement.PurchaseOrder;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.procurement.common.dao.AbstractDao;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.ConsignmentPOCreationDetailResponse;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.ConsignmentPOCreationRequest;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.PatientWisePOIssueList;
import com.param.service.global.SessionService;

@Repository
public class ConsignmentPOCreationDao extends AbstractDao<Integer, ConsignmentPurchaseOrder> implements IConsignmentPOCreationDao{

	final static Logger logger = Logger.getLogger(ConsignmentPOCreationDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<ConsignmentPurchaseOrder> searchConsignmentPurchaseOrderList(ConsignmentPOCreationRequest PurchaseOrdDto,Integer screenId) {
		String hql = "SELECT purchaseOrder FROM ConsignmentPurchaseOrder AS purchaseOrder "
				+ " 	INNER JOIN purchaseOrder.addedBy as user WITH user.unitId = :unitId "
				+ " 	LEFT JOIN purchaseOrder.store as store WITH store.unit.unitId = :unitId "
				+ "		LEFT JOIN purchaseOrder.status as status "
				+ "		LEFT JOIN purchaseOrder.approveStatusId as approvalStatus "
				
				+ " 	LEFT JOIN purchaseOrder.vendor as vendor "
				+ " WHERE purchaseOrder.isActive = true AND purchaseOrder.isDeleted = false AND purchaseOrder.unit.unitId = :unitId "
			    + " 	AND purchaseOrder.addedDate BETWEEN :fromDate AND :toDate "
			    + "		AND (:storeId = 0 OR store.id = :storeId) "
			    + "		AND (:vendorId = 0 OR purchaseOrder.vendor.id = :vendorId) "
			    + " 	AND (:poNumber = '' OR lower(purchaseOrder.poNumber) LIKE :poNumber) "
			    + " 	AND (:vendorName = '' OR lower(purchaseOrder.vendor.name) LIKE :vendorName) "
			    + " 	AND (:statusId = 0 OR status.id = :statusId) "
			    + "		AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ "								INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
				+ "								INNER JOIN screenStatus.status as status) "
				+ " ORDER BY purchaseOrder.id DESC";
		
		//+ " 	AND (:vendorName = '' OR lower(purchaseOrder.vendor.name) LIKE :vendorName) OR lower(purchaseOrder.vendor.code) LIKE :vendorName )"

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(PurchaseOrdDto.getFromDate()));
		query.setParameter("toDate", Util.getDate(PurchaseOrdDto.getToDate()));
		query.setParameter("storeId",PurchaseOrdDto.getStoreId() != null ? PurchaseOrdDto.getStoreId() : 0);
		query.setParameter("vendorId",PurchaseOrdDto.getVendorId() != null ? PurchaseOrdDto.getVendorId() : 0);
		query.setParameter("poNumber",PurchaseOrdDto.getPoNumber() != null ? PurchaseOrdDto.getPoNumber().toLowerCase() + '%' : "");
		query.setParameter("vendorName",PurchaseOrdDto.getVendorName() !=null ? PurchaseOrdDto.getVendorName().toLowerCase() + '%' : "");
		query.setParameter("statusId", PurchaseOrdDto.getStatusId() != null ? PurchaseOrdDto.getStatusId() : 0);
		query.setParameter("screenId", screenId);
		query.setParameter("unitId", PurchaseOrdDto.getUnitId()!=null ? PurchaseOrdDto.getUnitId() : sessionService.getUnitId());
		return query.list();
	}

	@Override
	public List<ConsignmentPOCreationDetailResponse> getConsignmentgetissueListFor(ConsignmentPOCreationRequest PurchaseOrdDto,Integer screenId) {
		String hql = "SELECT issue.id AS id, issue.store.id AS storeId,issue.store.storeName AS store, item.id AS itemId,item.code AS itemCode,item.name AS itemName,batch.expiryDate AS expiry, " 
				+"		issueDetailBatchMapper.rejectedQuantity AS quantity,batch.id AS batchId,batch.batchNo AS batchNo,"
				+"		  batch.cop AS cop,tax.id AS taxId,tax.code AS taxCode,tax.name AS taxName, batch.taxPercentage AS taxPercentage,batch.sellingPrice AS mrp,"
				+"		uomUnit.id AS uomUnitId,uomType.id AS uomTypeId,vendor.id AS vendorId"
				+" FROM Issue AS issue "
				+ " 	INNER JOIN issue.issueDetail AS issueDetail  "
				+ " 	INNER JOIN issue.store AS store  "
				+ " 	INNER JOIN issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper  WITH  issueDetailBatchMapper.rejectedQuantity is not null "
				+ "	 	INNER JOIN issueDetailBatchMapper.batch AS batch  "
				+ " 	LEFT JOIN issueDetailBatchMapper.uomUnit AS uomUnit  "
				+ " 	LEFT JOIN issueDetailBatchMapper.uomType AS uomType  "
				+ " 	INNER JOIN issueDetailBatchMapper.patientIssueReturnDetail "
				+ " 	INNER JOIN batch.vendor AS vendor  "
				+ " 	INNER JOIN batch.item AS item  "
				+ " 	INNER JOIN batch.tax AS tax  "
				+ "		LEFT JOIN issue.status as status "
				+ " WHERE issue.isActive = true AND issue.isDeleted = false AND issue.unit.unitId = :unitId "
			    + " 	AND issue.addedDate BETWEEN :fromDate AND :toDate "
			    + "		AND (:storeId = 0 OR issue.store.id = :storeId) "
			    + "		AND (:vendorId = 0 OR vendor.id = :vendorId) "
				+ "		AND status.id != :status "

				+ " ORDER BY issue.id DESC";
		
		
		
		
		
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(PurchaseOrdDto.getFromDate()));
		query.setParameter("toDate", Util.getDate(PurchaseOrdDto.getToDate()));
		query.setParameter("storeId",PurchaseOrdDto.getStoreId() != null ? PurchaseOrdDto.getStoreId() : 0);
		query.setParameter("vendorId",PurchaseOrdDto.getVendorId() != null ? PurchaseOrdDto.getVendorId() : 0);
		query.setParameter("status", Status.Closed.getStatus());

		query.setParameter("unitId", PurchaseOrdDto.getUnitId()!=null ? PurchaseOrdDto.getUnitId() : sessionService.getUnitId());
		query.setResultTransformer(Transformers.aliasToBean(ConsignmentPOCreationDetailResponse.class));
		return ( List<ConsignmentPOCreationDetailResponse>)query.list();
	}
	
	@Override
	public List<PatientWisePOIssueList> getConsignmentgetissueListForSavePO(ConsignmentPOCreationRequest PurchaseOrdDto,Integer itemid,Integer vendorId,Long batchId) {
		String hql = " SELECT item.code AS issueItemCode,item.name AS issueItemName, "
				+"   batch.cop AS issueCop,tax.code  AS issueTaxCode,batch.taxPercentage AS issueTaxPercentage,batch.sellingPrice AS issueMrp,"
						
				+"	 issue.issueNo AS issueNo,issue.addedDate AS issueDate,visitType.visitTypeId AS visitTypeId, "	
				+"	 visitType.visitTypeName AS visitType,concat(patient.prefix.prefixCode ,' ',patient.firstName,' ',patient.middleName,' ',patient.lastName) AS patientDetails,"
				+" 	 visitType.visitTypeCode AS visitNo,batch.id AS batchId, batch.batchNo AS batchNo, batch.expiryDate AS issueExpiry, "
				+" 	 issueDetailBatchMapper.rejectedQuantity AS issueQty,patient.id AS patientId ,issue.id AS issueId"
				
				+ "  FROM Issue AS issue "
				+ " INNER JOIN issue.issueDetail AS issueDetail  "
				+ " INNER JOIN issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH  issueDetailBatchMapper.rejectedQuantity is not null "
				+ " LEFT JOIN issue.visitType AS visitType WITH visitType.status = 'A' "
				+ " INNER JOIN issue.implantIndent AS implantIndent WITH implantIndent.isActive = true AND implantIndent.isDeleted = false "
				+ "	INNER JOIN implantIndent.patient AS patient  "
				+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
				+ " LEFT JOIN issueDetailBatchMapper.uomUnit AS uomUnit  "
				+ " LEFT JOIN issueDetailBatchMapper.uomType AS uomType  "
				+ " INNER JOIN issueDetailBatchMapper.patientIssueReturnDetail  "
				
				+ " INNER JOIN issueDetailBatchMapper.batch AS batch WITH batch.isDeleted = false AND batch.isActive = true  AND batch.isReturn = false "
				+ " LEFT JOIN batch.vendor AS vendor  "
				+ " LEFT JOIN batch.tax AS tax  "
				+ " INNER JOIN batch.item AS item  "
				+ " WHERE issue.isActive = true AND issue.isDeleted = false  "
			    + " AND issue.addedDate BETWEEN :fromDate AND :toDate "
				+ " AND issue.unit.unitId = :unitId "
				+ " AND item.id=:itemId AND vendor.id=:vendorId AND batch.id=:batchId"
				+ " ORDER BY issue.id DESC";
		
		
		
		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(PurchaseOrdDto.getFromDate()));
		query.setParameter("toDate", Util.getDate(PurchaseOrdDto.getToDate()));
		query.setParameter("itemId", itemid);
		query.setParameter("vendorId", vendorId);
		query.setParameter("batchId",batchId);
		query.setParameter("unitId", PurchaseOrdDto.getUnitId()!=null ? PurchaseOrdDto.getUnitId() : sessionService.getUnitId());
		
		query.setResultTransformer(Transformers.aliasToBean(PatientWisePOIssueList.class));

		return (List<PatientWisePOIssueList>)query.list();
	}

	 @Override
		public ConsignmentPurchaseOrder saveConsignmentOutward(ConsignmentPurchaseOrder consignmentPurchaseOrder) {
			getSession().save(consignmentPurchaseOrder);
			return consignmentPurchaseOrder;
			
		}

	 @Override
		public ConsignmentPurchaseOrder getConsignmentPOCreationDetails(Integer id,Integer unitId) {
			try {
				String hql = " SELECT consignmentPurchaseOrder FROM ConsignmentPurchaseOrder AS consignmentPurchaseOrder "
						+ " WHERE consignmentPurchaseOrder.id = :id AND consignmentPurchaseOrder.unit.unitId = :unitId "
						+ "AND consignmentPurchaseOrder.isActive = true AND consignmentPurchaseOrder.isDeleted = false";

				Query query = getSession().createQuery(hql);
				query.setParameter("id", id);
				query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());

				return (ConsignmentPurchaseOrder) query.uniqueResult();
			} catch (Exception e) {
				logger.error("getConsignmentPOCreationDetails failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	 
	 @Override
		public ConsignmentPurchaseOrder updateConsignmentPO(ConsignmentPurchaseOrder consignmentPurchaseOrder) {
			getSession().update(consignmentPurchaseOrder);
			return consignmentPurchaseOrder;
		}
}

package com.param.procurement.consignment.dao.consignmentReturn;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.PatientIssueReturn;
import com.param.entity.model.pharmacy.PatientIssueReturnDetail;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.ConsignmentPOCreationResponse;
import com.param.procurement.consignment.dto.consignmentReturn.PatientAgainstItemResponse;
import com.param.procurement.consignment.dto.consignmentReturn.PatientIssueReturnAddItem;
import com.param.procurement.consignment.dto.consignmentReturn.PatientIssueReturnRequest;
import com.param.service.global.SessionService;

@Repository("ConsignmentReturnDao")
public class ConsignmentReturnDao extends AbstractDao<Integer, ConsignmentReturnDao> implements IConsignmentReturnDao
{
	final static Logger logger = Logger.getLogger(ConsignmentReturnDao.class);

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private SessionService sessionService;
	
	@Override
	public List<PatientIssueReturn> consignmentMainSearch(PatientIssueReturnRequest patientIssueReturnRequest, Integer screenId)
	{
		try
		{
			String hql = " SELECT patientIssueReturn FROM PatientIssueReturn AS patientIssueReturn "
					+ " INNER JOIN patientIssueReturn.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " INNER JOIN patientIssueReturn.visitType AS visitType WITH visitType.status = 'A' "
					+ " INNER JOIN patientIssueReturn.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " INNER JOIN patientIssueReturn.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " LEFT JOIN patientIssueReturn.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN patientIssueReturn.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN patientIssueReturn.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
					+ " LEFT JOIN patientIssueReturn.bedMaster AS bedMaster WITH bedMaster.status = 'A' "
					+ " WHERE patientIssueReturn.unit.unitId = :unitId AND patientIssueReturn.isActive = true AND patientIssueReturn.isDeleted = false "
					+ " AND patientIssueReturn.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "		
					+ "	OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName )) "
					+ " AND (:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid )"
					+ " AND ( wardMaster.wardId = :wardId OR :wardId = 0 ) "
					+ " AND( :returnNo = '' OR lower(patientIssueReturn.returnNo) LIKE :returnNo) "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
					+ "	INNER JOIN screenStatus.status as status) "
					+ " ORDER BY patientIssueReturn.id DESC ";
			
			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(patientIssueReturnRequest.getFromDate()));
			query.setParameter("toDate", Util.getDate(patientIssueReturnRequest.getToDate()));
			query.setParameter("visitType",patientIssueReturnRequest.getVisitType() != null ? patientIssueReturnRequest.getVisitType() : 0);
			query.setParameter("patientId",patientIssueReturnRequest.getPatientId() != null ? patientIssueReturnRequest.getPatientId() : 0);
			query.setParameter("patientName",patientIssueReturnRequest.getPatientName() != null ? patientIssueReturnRequest.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("uhid",patientIssueReturnRequest.getUhid()!= null ? patientIssueReturnRequest.getUhid().toLowerCase() + '%' : "");
			query.setParameter("returnNo",patientIssueReturnRequest.getReturnNo()!= null ? patientIssueReturnRequest.getReturnNo().toLowerCase() + '%' : "");
			query.setParameter("wardId",patientIssueReturnRequest.getWardId() != null ? patientIssueReturnRequest.getWardId() : 0);
			query.setParameter("screenId", screenId);
			query.setParameter("unitId", sessionService.getUnitId());
			
		return query.list();
	}
	catch(Exception e)
	{
		logger.error("consignmentMainSearch failed", e);
		throw new ServiceException(e.getMessage());
	}
	}
	
	@Override
	public List<PatientIssueReturn> implantMainSearch(PatientIssueReturnRequest patientIssueReturnRequest, Integer screenId)
	{
		try
		{
			String hql = " SELECT patientIssueReturn FROM PatientIssueReturn AS patientIssueReturn "
					+ " INNER JOIN patientIssueReturn.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " INNER JOIN patientIssueReturn.visitType AS visitType WITH visitType.status = 'A' "
					+ " INNER JOIN patientIssueReturn.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " INNER JOIN patientIssueReturn.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " LEFT JOIN patientIssueReturn.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN patientIssueReturn.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN patientIssueReturn.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
					+ " LEFT JOIN patientIssueReturn.bedMaster AS bedMaster WITH bedMaster.status = 'A' "
					+ " WHERE patientIssueReturn.unit.unitId = :unitId AND patientIssueReturn.isActive = true AND patientIssueReturn.isDeleted = false "
					+ " AND patientIssueReturn.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "		
					+ "	OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName )) "
					+ " AND (:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid )"
					+ " AND ( wardMaster.wardId = :wardId OR :wardId = 0 ) "
					+ " AND( :returnNo = '' OR lower(patientIssueReturn.returnNo) LIKE :returnNo) "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
					+ "	INNER JOIN screenStatus.status as status) "
					+ " ORDER BY patientIssueReturn.id DESC ";
			
			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(patientIssueReturnRequest.getFromDate()));
			query.setParameter("toDate", Util.getDate(patientIssueReturnRequest.getToDate()));
			query.setParameter("visitType",patientIssueReturnRequest.getVisitType() != null ? patientIssueReturnRequest.getVisitType() : 0);
			query.setParameter("patientId",patientIssueReturnRequest.getPatientId() != null ? patientIssueReturnRequest.getPatientId() : 0);
			query.setParameter("patientName",patientIssueReturnRequest.getPatientName() != null ? patientIssueReturnRequest.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("uhid",patientIssueReturnRequest.getUhid()!= null ? patientIssueReturnRequest.getUhid().toLowerCase() + '%' : "");
			query.setParameter("returnNo",patientIssueReturnRequest.getReturnNo()!= null ? patientIssueReturnRequest.getReturnNo().toLowerCase() + '%' : "");
			query.setParameter("wardId",patientIssueReturnRequest.getWardId() != null ? patientIssueReturnRequest.getWardId() : 0);
			query.setParameter("screenId", screenId);
			query.setParameter("unitId", sessionService.getUnitId());
			
		return query.list();
	}
	catch(Exception e)
	{
		logger.error("implantMainSearch failed", e);
		throw new ServiceException(e.getMessage());
	}
	}
	
	
	@Override
	public List<Issue> patientAgainstIssueSearch(PatientIssueReturnRequest patientIssueReturnRequest)
	{
		try
		{
			String hql = " SELECT issue FROM Issue AS issue "
					+ " INNER JOIN issue.visitType AS visitType WITH visitType.status = 'A'"
					+ " INNER JOIN issue.patientIndent AS patientIndent WITH patientIndent.isActive = true AND patientIndent.isDeleted = false "
					+ " INNER JOIN issue.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " LEFT JOIN issue.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN admission.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN issue.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN encounter.patient AS patientEncounter WITH patientEncounter.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH prefixEncounter.status = 'A' "
					+ " WHERE issue.unit.unitId = :unitId AND issue.isActive = true AND issue.isDeleted = false "
					+ " AND issue.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND ((:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid OR lower(patientEncounter.uhidNumber) LIKE :uhid))"
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId OR patientEncounter.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					+ " OR lower(COALESCE(CONCAT(prefixEncounter.prefixCode,' ',patientEncounter.firstName,' ',patientEncounter.middleName,' ', patientEncounter.lastName))) LIKE :patientName "
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName OR lower(prefixEncounter.prefixCode) LIKE :patientName OR "
					+ "	lower(patientEncounter.firstName) LIKE :patientName OR lower(patientEncounter.middleName) LIKE :patientName "
					+ "	OR lower(patientEncounter.lastName) LIKE :patientName)) "
					+ " AND (:visitNo = '' OR lower(admission.admissionNumber) LIKE :visitNo OR lower(encounter.encounterNumber) LIKE :visitNo ) "
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND( :issueNo = '' OR lower(issue.issueNo) LIKE :issueNo) "
					+ " AND issue.isConsignment='false' "
					+ " ORDER BY issue.id DESC ";
					
			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(patientIssueReturnRequest.getFromDate()));
			query.setParameter("toDate", Util.getDate(patientIssueReturnRequest.getToDate()));
			query.setParameter("visitType",patientIssueReturnRequest.getVisitType() != null ? patientIssueReturnRequest.getVisitType() : 0);
			query.setParameter("patientId",patientIssueReturnRequest.getPatientId() != null ? patientIssueReturnRequest.getPatientId() : 0);
			query.setParameter("patientName",patientIssueReturnRequest.getPatientName() != null ? patientIssueReturnRequest.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("uhid",patientIssueReturnRequest.getUhid()!= null ? patientIssueReturnRequest.getUhid().toLowerCase() + '%' : "");
			query.setParameter("visitNo",patientIssueReturnRequest.getVisitNo()!= null ? patientIssueReturnRequest.getVisitNo().toLowerCase() + '%' : "");
			query.setParameter("storeId",patientIssueReturnRequest.getStoreId() != null ? patientIssueReturnRequest.getStoreId() : 0);
			query.setParameter("issueNo",patientIssueReturnRequest.getIssueNo()!= null ? patientIssueReturnRequest.getIssueNo().toLowerCase() + '%' : "");
			query.setParameter("unitId",patientIssueReturnRequest.getUnitId()!=null ? patientIssueReturnRequest.getUnitId() : sessionService.getUnitId());
			
		return query.list();
	}
	catch(Exception e)
	{
		logger.error("patientAgainstIssueSearch failed", e);
		throw new ServiceException(e.getMessage());
	}
	}
	
	@Override
	public List<PatientAgainstItemResponse> patientAgainstItemSearch(PatientIssueReturnRequest patientIssueReturnRequest)
	{
		try
		{
			String hql = " SELECT issue.id AS id, issue.issueNo AS issueNo, issue.addedDate AS issueDate, item.id AS itemId, "
					+ " item.name AS itemName, item.code AS itemCode, generic.name AS genericName,uomType.type AS uom, uomUnit.units AS uomUnit, "
					+ " issueDetailBatchMapper.issueQuantity AS quantityIssued, batch.batchNo AS batchNo, batch.expiryDate AS expiry, "
					+ " issueDetailBatchMapper.acceptedQuantity AS returnQuantity, patientIndent.indentNo AS indentNo "
					+ " FROM Issue AS issue "
					+ " INNER JOIN issue.issueDetail AS issueDetail WITH issueDetail.isActive = true AND issueDetail.isDeleted = false "
					+ " INNER JOIN issue.patientIndent AS patientIndent WITH patientIndent.isActive = true AND patientIndent.isDeleted = false "
					+ " INNER JOIN issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false AND batch.isPharmacy = false  "
					+ " INNER JOIN batch.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
					+ "	INNER JOIN batch.batchUomList AS uom WITH uom.storeUom = true AND uom.isActive = true AND uom.isDeleted = false "
					+ "	INNER JOIN uom.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
					+ " INNER JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
					+ "	INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
					+ " INNER JOIN issue.visitType AS visitType WITH visitType.status = 'A' "
					+ " INNER  JOIN issue.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " LEFT JOIN issue.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN admission.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN issue.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN encounter.patient AS patientEncounter WITH patientEncounter.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH prefixEncounter.status = 'A' "
					+ " WHERE issue.unit.unitId = :unitId AND issue.isActive = true AND issue.isDeleted = false "
					+ " AND issue.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND ((:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid OR lower(patientEncounter.uhidNumber) LIKE :uhid))"
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId OR patientEncounter.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					+ " OR lower(COALESCE(CONCAT(prefixEncounter.prefixCode,' ',patientEncounter.firstName,' ',patientEncounter.middleName,' ', patientEncounter.lastName))) LIKE :patientName "
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName OR lower(prefixEncounter.prefixCode) LIKE :patientName OR "
					+ "	lower(patientEncounter.firstName) LIKE :patientName OR lower(patientEncounter.middleName) LIKE :patientName "
					+ "	OR lower(patientEncounter.lastName) LIKE :patientName)) "
					+ " AND (:visitNo = '' OR lower(admission.admissionNumber) LIKE :visitNo OR lower(encounter.encounterNumber) LIKE :visitNo ) "
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND( :issueNo = '' OR lower(issue.issueNo) LIKE :issueNo) "
					+ " AND( :batchNo = '' OR lower(batch.batchNo) LIKE :batchNo) "
					+ "	AND (:itemId = 0 OR item.id = :itemId) "
					+ "	AND ((:itemId != 0 AND :itemCode != '') OR :itemCode = '' OR lower(item.code) like lower(:itemCode)) "
					+ "	AND ((:itemId != 0 AND :itemName != '') OR :itemName = '' OR lower(item.name) like lower(:itemName)) "
					+ " ORDER BY issue.id DESC ";
			
			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(patientIssueReturnRequest.getFromDate()));
			query.setParameter("toDate", Util.getDate(patientIssueReturnRequest.getToDate()));
			query.setParameter("visitType",patientIssueReturnRequest.getVisitType() != null ? patientIssueReturnRequest.getVisitType() : 0);
			query.setParameter("patientId",patientIssueReturnRequest.getPatientId() != null ? patientIssueReturnRequest.getPatientId() : 0);
			query.setParameter("patientName",patientIssueReturnRequest.getPatientName() != null ? patientIssueReturnRequest.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("uhid",patientIssueReturnRequest.getUhid()!= null ? patientIssueReturnRequest.getUhid().toLowerCase() + '%' : "");
			query.setParameter("visitNo",patientIssueReturnRequest.getVisitNo()!= null ? patientIssueReturnRequest.getVisitNo().toLowerCase() + '%' : "");
			query.setParameter("itemId",patientIssueReturnRequest.getItemId() != null ? patientIssueReturnRequest.getItemId() : 0);
			query.setParameter("itemName",patientIssueReturnRequest.getItemName()!= null ? patientIssueReturnRequest.getItemName().toLowerCase() + '%' : "");
			query.setParameter("itemCode",patientIssueReturnRequest.getItemCode()!= null ? patientIssueReturnRequest.getItemName().toLowerCase() + '%' : "");
			query.setParameter("batchNo",patientIssueReturnRequest.getBatchNo()!= null ? patientIssueReturnRequest.getBatchNo().toLowerCase() + '%' : "");
			query.setParameter("storeId",patientIssueReturnRequest.getStoreId() != null ? patientIssueReturnRequest.getStoreId() : 0);
			query.setParameter("issueNo",patientIssueReturnRequest.getIssueNo()!= null ? patientIssueReturnRequest.getIssueNo().toLowerCase() + '%' : "");
			query.setParameter("unitId", patientIssueReturnRequest.getUnitId()!=null ? patientIssueReturnRequest.getUnitId() : sessionService.getUnitId());
			
		query.setResultTransformer(Transformers.aliasToBean(PatientAgainstItemResponse.class));
		return query.list();
	}
	catch(Exception e)
	{
		logger.error("patientAgainstItemSearch failed", e);
		throw new ServiceException(e.getMessage());
	}
	}
	
	@Override
	public List<Issue> patientIssueReturnAddItemByItemId(PatientIssueReturnAddItem patientIssueReturnAddItem)
	{
		try
		{
			String hql = " SELECT issue FROM Issue AS issue "
					+ " INNER JOIN issue.issueDetail AS issueDetail WITH issueDetail.isActive = true AND issueDetail.isDeleted = false "
					+ " INNER JOIN issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false AND batch.isReturn = false "
					+ " INNER JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
					+ " WHERE issue.isActive = true AND issue.isDeleted = false "
					+ " AND issue.unit.unitId = :unitId AND item.id IN (:itemId) "
					+ " AND issue.id IN (:issueId) ";
					
			Query query = getSession().createQuery(hql);
			query.setParameterList("itemId", patientIssueReturnAddItem.getItemIds());
			query.setParameterList("issueId", patientIssueReturnAddItem.getIssueIds());
			query.setParameter("unitId", patientIssueReturnAddItem.getUnitId()!=null ? patientIssueReturnAddItem.getUnitId() : sessionService.getUnitId());
			
		return query.list();
	}
	catch(Exception e)
	{
		logger.error("patientIssueReturnAddItemByItemId failed", e);
		throw new ServiceException(e.getMessage());
	}
	}
	
	@Override
	public List<Issue> patientIssueReturnAddItemByIssueId(PatientIssueReturnAddItem patientIssueReturnAddItem)
	{
		try
		{
			String hql = " SELECT issue FROM Issue AS issue "
					+ " WHERE issue.isActive = true AND issue.isDeleted = false "
					+ " AND issue.unit.unitId = :unitId AND issue.id IN (:issueId) ";
			Query query = getSession().createQuery(hql);
		
			query.setParameterList("issueId", patientIssueReturnAddItem.getIssueIds());
			query.setParameter("unitId", patientIssueReturnAddItem.getUnitId()!=null ? patientIssueReturnAddItem.getUnitId() : sessionService.getUnitId());
			
		return query.list();
	}
	catch(Exception e)
	{
		logger.error("patientIssueReturnAddItemByIssueId failed", e);
		throw new ServiceException(e.getMessage());
	}
	}
	
	@Override
	public List<Object> getItemByIssueAndItemId(Integer issueId, List<Integer> itemIds, Integer unitId)
	{	
		try
		{
			String hql = " SELECT issue.id AS id, issue.issueNo AS issueNo, issue.addedDate AS issueDate, "
					+ " item.id AS itemId, item.name AS itemName, item.code AS itemCode, generic.name AS genericName,  "
					+ " uom.uomType.type AS uom, uomUnit.units AS uomUnit, batch.batchNo AS batchNo, issueDetailBatchMapper.issueQuantity AS quantityIssued,"
					+ " batch.expiryDate AS expiry, issueDetailBatchMapper.acceptedQuantity AS returnQuantity, issueDetailBatchMapper.id AS batchMapperId"
					+ " FROM Issue AS issue "
					+ " INNER JOIN issue.issueDetail AS issueDetail WITH issueDetail.isActive = true AND issueDetail.isDeleted = false "
					+ " INNER JOIN issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false "
					+ " INNER JOIN batch.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
					+ " INNER JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
					+ "	INNER JOIN batch.batchUomList AS uom WITH uom.storeUom = true AND uom.isActive = true AND uom.isDeleted = false "
					+ "	INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
					+ " WHERE issue.id = :issueId AND ( item.id IN (:itemId) ) AND issue.unit.unitId = :unitId AND issue.isActive = true AND issue.isDeleted = false ";
			
			Query query = getSession().createQuery(hql);
			query.setParameter("issueId", issueId);
			query.setParameterList("itemId", itemIds);
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
			
		query.setResultTransformer(Transformers.aliasToBean(PatientAgainstItemResponse.class));
		return query.list();
	}
	catch(Exception e)
	{
		logger.error("getItemByIssueAndItemId failed", e);
		throw new ServiceException(e.getMessage());
	}
	}
	
	@Override
	public List<Object> getItemByIssueId(Integer issueId, Integer unitId)
	{
		
		try
		{
			String hql = " SELECT issue.id AS id, issue.issueNo AS issueNo, issue.addedDate AS issueDate, "
					+ " item.id AS itemId, item.name AS itemName, item.code AS itemCode, generic.name AS genericName,  "
					+ " uom.uomType.type AS uom, uomUnit.units AS uomUnit, batch.batchNo AS batchNo, issueDetailBatchMapper.issueQuantity AS quantityIssued,"
					+ " batch.expiryDate AS expiry, issueDetailBatchMapper.acceptedQuantity AS returnQuantity, issueDetailBatchMapper.id AS batchMapperId"
					+ " FROM Issue AS issue "
					+ " left JOIN issue.issueDetail AS issueDetail WITH issueDetail.isActive = true AND issueDetail.isDeleted = false "
					+ " left JOIN issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
					+ " left JOIN issueDetailBatchMapper.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false "
					+ " left JOIN batch.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
					+ " left JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
					+ "	left JOIN batch.batchUomList AS uom WITH uom.storeUom = true AND uom.isActive = true AND uom.isDeleted = false "
					+ "	left JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
					+ " WHERE issue.id = :issueId AND issue.unit.unitId = :unitId AND issue.isActive = true AND issue.isDeleted = false ";
			
			Query query = getSession().createQuery(hql);
			query.setParameter("issueId", issueId);
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
			
		query.setResultTransformer(Transformers.aliasToBean(PatientAgainstItemResponse.class));
		return query.list();
	}
	catch(Exception e)
	{
		logger.error("getItemByIssueId failed", e);
		throw new ServiceException(e.getMessage());
	}
	}
	
	@Override
	public PatientIssueReturn patientIssueReturnById(Integer id, Integer unitId)
	{
		try
		{
			String hql = " SELECT patientIssueReturn FROM PatientIssueReturn AS patientIssueReturn "
					+ "	INNER JOIN patientIssueReturn.patientIssueReturnDetail AS patientIssueReturnDetail WITH patientIssueReturnDetail.isActive = true AND patientIssueReturnDetail.isDeleted = false "
					+ " INNER JOIN patientIssueReturnDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.batch AS batch WITH batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.isReturn = false "
					+ " WHERE patientIssueReturn.isActive = true AND patientIssueReturn.isDeleted = false AND patientIssueReturn.id = :id"
					+ " AND patientIssueReturn.unit.unitId = :unitId ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
			
		return (PatientIssueReturn) query.uniqueResult();
	}
	catch(Exception e)
	{
		logger.error("patientIssueReturnById failed", e);
		throw new ServiceException(e.getMessage());
	}
	}
	
	@Override
	public ConsignmentPOCreationResponse patientIssueReturnForPO(Integer id)
	{
		try
		{
			String hql = " SELECT vendor.id AS vendorId,patientIssueReturn.store.id AS storeId,item.id AS itemId, "
					+"   batch.cop AS cop,tax.id AS taxId,batch.taxPercentage AS taxPercentage,batch.sellingPrice AS mrp,"
					+"	 uomUnit.id AS uomUnitId, uomType.id AS uomTypeId,issue.isConsignment AS isConsignment,issue.isImplant AS isImplant "		
					
					+ "  FROM PatientIssueReturn AS patientIssueReturn "
					+ "	INNER JOIN patientIssueReturn.patientIssueReturnDetail AS patientIssueReturnDetail WITH patientIssueReturnDetail.isActive = true AND patientIssueReturnDetail.isDeleted = false "
					+ " INNER JOIN patientIssueReturnDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.issueDetail AS issueDetail  "
					+ " LEFT JOIN issueDetailBatchMapper.uomUnit AS uomUnit  "
					+ " LEFT JOIN issueDetailBatchMapper.uomType AS uomType  "
					+ " INNER JOIN issueDetail.issue AS issue  "
					+ " INNER JOIN issueDetailBatchMapper.batch AS batch WITH batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.isReturn = false "
					+ " LEFT JOIN batch.vendor AS vendor  "
					+ " LEFT JOIN batch.tax AS tax  "
					+ " INNER JOIN batch.item AS item  "
					+ " WHERE patientIssueReturn.isActive = true AND patientIssueReturn.isDeleted = false AND patientIssueReturn.id = :id"
					+ " AND patientIssueReturn.unit.unitId = :unitId ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", sessionService.getUnitId());
			query.setResultTransformer(Transformers.aliasToBean(ConsignmentPOCreationResponse.class));
		return (ConsignmentPOCreationResponse) query.uniqueResult();
	}
	catch(Exception e)
	{
		logger.error("patientIssueReturnForPO failed", e);
		throw new ServiceException(e.getMessage());
	}
	}
	
	@Override
	public List<PatientIssueReturnDetail> patientIssueReturnDetailById(Integer issueReturnId, Integer unitId)
	{

		try
		{
			String hql = " SELECT patientIssueReturnDetail FROM PatientIssueReturnDetail AS patientIssueReturnDetail "
					+ " left JOIN patientIssueReturnDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
					+ " left JOIN issueDetailBatchMapper.batch AS batch WITH batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.isReturn = false "
					+ " left JOIN patientIssueReturnDetail.patientIssueReturn AS patientIssueReturn WITH patientIssueReturn.isActive = true AND patientIssueReturn.isDeleted = false "
					+ " WHERE patientIssueReturnDetail.isActive = true AND patientIssueReturnDetail.isDeleted = false AND patientIssueReturn.id = :id"
					+ " AND patientIssueReturn.unit.unitId = :unitId ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", issueReturnId);
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
			
		return query.list();
	}
	catch(Exception e)
	{
		logger.error("patientIssueReturnDetailById failed", e);
		throw new ServiceException(e.getMessage());
	}
	}
	
	@Override
	public 	PatientIssueReturn implantReturnSave(PatientIssueReturn patientIssueReturn)
	{
		getSession().save(patientIssueReturn);
		return patientIssueReturn;
	}
	
	
}

package com.param.pharmacy.nursing.workstation.dao.patientIssueReturnConsumable;

import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.PatientIssueReturn;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientAgainstItemResponse;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnRequest;

@Repository("patientIssueReturnConsumableDao")
public class PatientIssueReturnConsumableDao extends AbstractDao<Integer, PatientIssueReturn> implements IPatientIssueReturnConsumableDao {
	
	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private SessionService sessionService;

	private static final Logger logger = Logger.getLogger(PatientIssueReturnConsumableDao.class);

	@Override
	public List<PatientIssueReturn> patientIssueReturnSearch(PatientIssueReturnRequest patientIssueReturnRequest, Integer screenId)
	{
		try
		{
			String hql = " SELECT patientIssueReturn FROM PatientIssueReturn AS patientIssueReturn "
					//+ " INNER JOIN patientIssueReturn.patientIssueReturnDetail AS patientIssueReturnDetail WITH patientIssueReturnDetail.isDeleted = false "
					+ " INNER JOIN patientIssueReturn.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " INNER JOIN patientIssueReturn.visitType AS visitType WITH visitType.status = 'A' "
					+ " INNER JOIN patientIssueReturn.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " INNER JOIN patientIssueReturn.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " LEFT JOIN patientIssueReturn.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN patientIssueReturn.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN patientIssueReturn.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
					+ " LEFT JOIN patientIssueReturn.bedMaster AS bedMaster WITH bedMaster.status = 'A' "
					+ " WHERE patientIssueReturn.unit.unitId = :unitId AND patientIssueReturn.isActive = true AND patientIssueReturn.isDeleted = false and patientIssueReturn.fromMedication !=true "
					+ " AND patientIssueReturn.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND (:visitNo = '' OR lower(admission.admissionNumber) LIKE :visitNo OR lower(encounter.encounterNumber) LIKE :visitNo ) "
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "		
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName )) "
					+ " AND (:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid )"
//					+ " AND ( wardMaster.wardId in ( :wardId )) "
					+ " AND( :returnNo = '' OR lower(patientIssueReturn.returnNo) LIKE :returnNo) "
					+ " AND( :bedNo = '' OR lower(bedMaster.bedNumber) LIKE :bedNo) "
//					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
//					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
//					+ "	INNER JOIN screenStatus.status as status) "
//					+ " ORDER BY patientIssueReturn.id DESC "
					;
			

			StringBuffer subQuery=new StringBuffer();
			int id=patientIssueReturnRequest.getStatusId() != null ? patientIssueReturnRequest.getStatusId(): 0;
			
			if(id != 0){
				subQuery.append(" AND status.id =:statusId ");
			}
			else{
				subQuery.append(" AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus INNER JOIN screenStatus.screen as screen WITH screen.id =:screenId INNER JOIN screenStatus.status as status) ");

			}
			if(patientIssueReturnRequest.getWardList()!=null && patientIssueReturnRequest.getWardList().size()>0) {

				subQuery.append(" AND wardMaster.wardId IN (:wardIds) ");
			}else {
				subQuery.append(" AND wardMaster.wardId in (0)");
			}
			subQuery.append(" ORDER BY patientIssueReturn.id DESC");
			String hql1 = hql.concat(subQuery.toString());
			Query query = getSession().createQuery(hql1);
			query.setParameter("fromDate", Util.getDate(patientIssueReturnRequest.getFromDate()));
			query.setParameter("toDate", Util.getDate(patientIssueReturnRequest.getToDate()));
			query.setParameter("visitType",patientIssueReturnRequest.getVisitType() != null ? patientIssueReturnRequest.getVisitType() : 0);
			query.setParameter("patientId",patientIssueReturnRequest.getPatientId() != null ? patientIssueReturnRequest.getPatientId() : 0);
			query.setParameter("patientName",patientIssueReturnRequest.getPatientName() != null ? patientIssueReturnRequest.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("uhid",patientIssueReturnRequest.getUhid()!= null ? patientIssueReturnRequest.getUhid().toLowerCase() + '%' : "");
			query.setParameter("visitNo",patientIssueReturnRequest.getVisitNo()!= null ? patientIssueReturnRequest.getVisitNo().toLowerCase() + '%' : "");
			query.setParameter("returnNo",patientIssueReturnRequest.getReturnNo()!= null ? patientIssueReturnRequest.getReturnNo().toLowerCase() + '%' : "");
//			query.setParameterList("wardId",patientIssueReturnRequest.getWardList());
			query.setParameter("bedNo",patientIssueReturnRequest.getBedNo()!= null ? patientIssueReturnRequest.getBedNo().toLowerCase() + '%' : "");
//			query.setParameter("screenId", screenId);
			query.setParameter("unitId",patientIssueReturnRequest.getUnitId()!=null ? patientIssueReturnRequest.getUnitId() : sessionService.getUnitId());
			if(id != 0){
				query.setParameter("statusId", patientIssueReturnRequest.getStatusId());
			}
			else{
				query.setParameter("screenId", screenId);
			}
			if(patientIssueReturnRequest.getWardList()!=null && patientIssueReturnRequest.getWardList().size()>0) {
				query.setParameterList("wardIds", patientIssueReturnRequest.getWardList());
			}
			
		return query.list();
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
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
					+ " INNER  JOIN issue.toStore AS toStore WITH toStore.isActive = true AND toStore.isDeleted = false "  
					+ " LEFT JOIN issue.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN admission.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN issue.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN encounter.patient AS patientEncounter WITH patientEncounter.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH prefixEncounter.status = 'A' "
					+ " WHERE issue.unit.unitId = :unitId AND issue.isActive = true AND issue.isDeleted = false  " // and issue.isReturn= false
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
					+ " AND ( toStore.storeId = :tostoreId OR :tostoreId = 0 ) "
					+ " AND( :issueNo = '' OR lower(issue.issueNo) LIKE :issueNo) "
					+ " AND( (select COALESCE(sum(issueDetailBatchMapper.acceptedQuantity),0) from IssueDetail AS issueDetail "  
					+"       inner join issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper  "  
					+"       where issueDetail.issue.id = issue.id) "  
					+" > "  
					+"      (select COALESCE(sum(patientIssueReturnDetail.returnQuantity),0) from IssueDetail AS issueDetail "  
					+"      inner join issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper  "  
					+"      inner join issueDetailBatchMapper.patientIssueReturnDetail AS patientIssueReturnDetail  "  
					+"      where issueDetail.issue.id = issue.id) ) "
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
			query.setParameter("tostoreId",patientIssueReturnRequest.getToStoreId() != null ? patientIssueReturnRequest.getToStoreId() : 0);
			query.setParameter("issueNo",patientIssueReturnRequest.getIssueNo()!= null ? patientIssueReturnRequest.getIssueNo().toLowerCase() + '%' : "");
			query.setParameter("unitId", patientIssueReturnRequest.getUnitId()!=null ? patientIssueReturnRequest.getUnitId() : sessionService.getUnitId());
			
		return query.list();
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
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
					+ " issueDetailBatchMapper.acceptedQuantity AS returnQuantity, issueDetailBatchMapper.returnAcceptedQuantity+issueDetailBatchMapper.returnRejectedQuantity AS returnAcceptedQuantity, patientIndent.indentNo AS indentNo ,issueDetailBatchMapper.id as batchMapperId  FROM Issue AS issue "
					+ " INNER JOIN issue.issueDetail AS issueDetail WITH issueDetail.isActive = true AND issueDetail.isDeleted = false "
					+ " INNER JOIN issue.patientIndent AS patientIndent WITH patientIndent.isActive = true AND patientIndent.isDeleted = false "
					+ " INNER JOIN issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false   "
					+ " INNER JOIN batch.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
					+ "	INNER JOIN batch.batchUomList AS uom WITH uom.storeUom = true AND uom.isActive = true AND uom.isDeleted = false "
					+ "	INNER JOIN uom.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
					+ " INNER JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
					+ "	INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
					+ " INNER JOIN issue.visitType AS visitType WITH visitType.status = 'A' "
					+ " INNER  JOIN issue.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " INNER  JOIN issue.toStore AS toStore WITH toStore.isActive = true AND toStore.isDeleted = false "  
					+ " LEFT JOIN issue.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN admission.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN issue.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN encounter.patient AS patientEncounter WITH patientEncounter.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH prefixEncounter.status = 'A' "
                    + " LEFT JOIN issueDetail.issueDetailBatchMapper AS allIssueDetailBatchMapper "
                    + " LEFT JOIN allIssueDetailBatchMapper.patientIssueReturnDetail AS patientIssueReturnDetail "
//                    + " LEFT JOIN issueDetailBatchMapper.patientIssueReturnDetail as patientIssueReturnDetail WITH patientIssueReturnDetail.isActive=true AND patientIssueReturnDetail.isDeleted = false  "
                    + " WHERE issue.unit.unitId = :unitId AND issue.isActive = true AND issue.isDeleted = false "
//                    + " and (issueDetailBatchMapper.acceptedQuantity!=((select COALESCE(cast(sum(patientIssueReturnDetail.returnQuantity)as int),0) from IssueDetail AS issueDetail1 "
//					+"      inner join issueDetail1.issueDetailBatchMapper AS issueDetailBatchMapper "
//					+"      inner join issueDetailBatchMapper.patientIssueReturnDetail AS patientIssueReturnDetail  "
//					+"      where issueDetail1.id = issueDetail.id)+issueDetailBatchMapper.returnRejectedQuantity)) "
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
					+ " AND ( toStore.storeId = :tostoreId OR :tostoreId = 0 ) "
					+ " AND( :issueNo = '' OR lower(issue.issueNo) LIKE :issueNo) "
					+ " AND( :batchNo = '' OR lower(batch.batchNo) LIKE :batchNo) "
					+ "	AND (:itemId = 0 OR item.id = :itemId) "
					+ "	AND ((:itemId != 0 AND :itemCode != '') OR :itemCode = '' OR lower(item.code) like lower(:itemCode)) "
					+ "	AND ((:itemId != 0 AND :itemName != '') OR :itemName = '' OR lower(item.name) like lower(:itemName)) "
//					+ " AND( (select COALESCE(sum(issueDetailBatchMapper.acceptedQuantity),0) from IssueDetail AS issueDetail "  
//                    +"       inner join issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper  "  
//                    +"       where issueDetail.issue.id = issue.id) "  
//                    +" > "  
//                    +"      (select COALESCE(sum(patientIssueReturnDetail.returnQuantity),0) from IssueDetail AS issueDetail "  
//                    +"      inner join issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper  "  
//                    +"      inner join issueDetailBatchMapper.patientIssueReturnDetail AS patientIssueReturnDetail  "  
//                    +"      where issueDetail.issue.id = issue.id) ) "
                    + " GROUP BY "
                    + "   issue.id, issue.issueNo, issue.addedDate, "
                    + "   item.id, item.name, item.code, "
                    + "   generic.name, "
                    + "   uomType.type, uomUnit.units, "
                    + "   issueDetailBatchMapper.issueQuantity, "
                    + "   batch.batchNo, batch.expiryDate, "
                    + "   issueDetailBatchMapper.acceptedQuantity, "
                    + "   issueDetailBatchMapper.returnAcceptedQuantity, "
                    + "   issueDetailBatchMapper.returnRejectedQuantity, "
                    + "   patientIndent.indentNo, "
                    + "   issueDetailBatchMapper.id "
                    + " HAVING issueDetailBatchMapper.acceptedQuantity <> "
                    + "        (COALESCE(SUM(patientIssueReturnDetail.returnQuantity), 0) "
                    + "         + issueDetailBatchMapper.returnRejectedQuantity) "
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
			query.setParameter("itemCode",patientIssueReturnRequest.getItemCode()!= null ? patientIssueReturnRequest.getItemCode().toLowerCase() + '%' : "");
			query.setParameter("batchNo",patientIssueReturnRequest.getBatchNo()!= null ? patientIssueReturnRequest.getBatchNo().toLowerCase() + '%' : "");
			query.setParameter("storeId",patientIssueReturnRequest.getStoreId() != null ? patientIssueReturnRequest.getStoreId() : 0);
			query.setParameter("tostoreId",patientIssueReturnRequest.getToStoreId() != null ? patientIssueReturnRequest.getToStoreId() : 0);
			query.setParameter("issueNo",patientIssueReturnRequest.getIssueNo()!= null ? patientIssueReturnRequest.getIssueNo().toLowerCase() + '%' : "");
			query.setParameter("unitId", patientIssueReturnRequest.getUnitId()!=null ? patientIssueReturnRequest.getUnitId() : sessionService.getUnitId());
			
		query.setResultTransformer(Transformers.aliasToBean(PatientAgainstItemResponse.class));
		return query.list();
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Integer patientIssueReturnDao(Integer batchMapperId) {
		Integer qty = (Integer) sessionFactory.getCurrentSession()
				.createSQLQuery("	select coalesce(cast(sum(ti.return_qty) as int),0) from pharmacy.t_patient_issue_return_detail ti  where  ti.is_active = true and ti.is_deleted = false and ti.issue_batch_mapper_id =:batchMapperId ")
				.setParameter("batchMapperId",batchMapperId)
				.uniqueResult();
		return qty;
	}
	
	
}
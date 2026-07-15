package com.param.pharmacy.ip.dao.pendingIssues.acceptance.consumable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.Issue;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceConsumableSearch;

@Repository("pendingIssuesAcceptancConsumableDao")
public class PendingIssuesAcceptanceConsumableDao extends AbstractDao<Integer, Issue> implements IPendingIssuesAcceptanceConsumableDao {

	@Autowired
	private SessionService sessionService;

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public 	List<Issue> pendingIssuesAcceptanceConsumableSearch(PendingIssuesAcceptanceConsumableSearch pendingIssuesAcceptanceSearch, Integer screenId)
	{
		try
		{
			StringBuilder hql = new StringBuilder( " SELECT distinct issue FROM Issue AS issue "
					+ " LEFT JOIN issue.doctor AS doctor WITH doctor.status = 'A'"
					+ " LEFT JOIN issue.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A'"
					+ " INNER JOIN issue.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " LEFT JOIN issue.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN admission.admissionDetail AS admissionDetail WITH admissionDetail.status = 'A' "
					+ " LEFT JOIN admissionDetail.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
					+ " LEFT JOIN admissionDetail.bedMaster AS bedMaster WITH bedMaster.status = 'A' "
					+ " LEFT JOIN issue.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN admission.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN encounter.patient AS patientEncounter WITH patientEncounter.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH prefixEncounter.status = 'A' "
					+ " INNER JOIN issue.patientIndent AS patientIndent WITH patientIndent.isActive = true AND patientIndent.isDeleted = false "
					+ " INNER JOIN issue.visitType AS visitType WITH visitType.status = 'A' "
					+ " INNER JOIN issue.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " WHERE  (:statusId = 0 OR status.id = :statusId) AND  (status.id!=2) and issue.isActive = true AND issue.isDeleted = false AND issue.unit.unitId = :unitId AND issue.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId OR patientEncounter.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					+ " OR lower(COALESCE(CONCAT(prefixEncounter.prefixCode,' ',patientEncounter.firstName,' ',patientEncounter.middleName,' ', patientEncounter.lastName))) LIKE :patientName "
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName OR lower(prefixEncounter.prefixCode) LIKE :patientName OR "
					+ "	lower(patientEncounter.firstName) LIKE :patientName OR lower(patientEncounter.middleName) LIKE :patientName "
					+ "	OR lower(patientEncounter.lastName) LIKE :patientName)) "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND (:consignment = false OR patientIndent.isConsignment = :consignment )"
					+ " AND ( store.storeId = :storeId OR :storeId = 0 ) "
					+ " AND( :uhid = '' OR lower(patient.uhidNumber) LIKE :uhid OR lower(patientEncounter.uhidNumber) LIKE :uhid) "
					+ " AND( :issueNo = '' OR lower(issue.issueNo) LIKE :issueNo) "
					+ " AND( :uhid = '' OR lower(patient.uhidNumber) LIKE :uhid OR lower(patientEncounter.uhidNumber) LIKE :uhid ) "
					+ " AND( :visitNo = '' OR lower(admission.admissionNumber) LIKE :visitNo OR lower(encounter.encounterNumber) LIKE :visitNo ) "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
					+ "	INNER JOIN screenStatus.status as status) "
			);

			boolean getVisitTypeValid = false;
			if(pendingIssuesAcceptanceSearch.getVisitType() != null && !Objects.equals(pendingIssuesAcceptanceSearch.getVisitType(), 1)){
				hql.append(" AND( wardMaster.wardId in ( :wardId )) ");
				getVisitTypeValid = true;
			}

			hql.append( " ORDER BY issue.id DESC ");

			Query query = getSession().createQuery(hql.toString());
			query.setParameter("fromDate", Util.getDate(pendingIssuesAcceptanceSearch.getFromDate()));
			query.setParameter("toDate", Util.getDate(pendingIssuesAcceptanceSearch.getToDate()));
			query.setParameter("patientId",pendingIssuesAcceptanceSearch.getPatientId() != null ? pendingIssuesAcceptanceSearch.getPatientId() : 0);
			query.setParameter("patientName",pendingIssuesAcceptanceSearch.getPatientName() != null ? '%'+pendingIssuesAcceptanceSearch.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("issueNo",pendingIssuesAcceptanceSearch.getIssueNo() != null ? '%'+pendingIssuesAcceptanceSearch.getIssueNo().toLowerCase() + '%' : "");
			query.setParameter("visitType",pendingIssuesAcceptanceSearch.getVisitType() != null ? pendingIssuesAcceptanceSearch.getVisitType() : 0);
			query.setParameter("visitNo",pendingIssuesAcceptanceSearch.getVisitNo() != null ? '%'+pendingIssuesAcceptanceSearch.getVisitNo().toLowerCase() + '%' : "");
			query.setParameter("storeId",pendingIssuesAcceptanceSearch.getStoreId() != null ? pendingIssuesAcceptanceSearch.getStoreId() : 0);
			query.setParameter("consignment",pendingIssuesAcceptanceSearch.isConsignment() != null ? pendingIssuesAcceptanceSearch.isConsignment() : false);

			if(getVisitTypeValid == true){
				List<Integer> wardList = new ArrayList<>();
				if(pendingIssuesAcceptanceSearch.getWardList()!=null && pendingIssuesAcceptanceSearch.getWardList().size()>0) {
					wardList.addAll(pendingIssuesAcceptanceSearch.getWardList());
				}else {
					wardList.add(0);
				}
				query.setParameterList("wardId", wardList);
			}

			query.setParameter("uhid",pendingIssuesAcceptanceSearch.getUhid() != null ? '%'+pendingIssuesAcceptanceSearch.getUhid().toLowerCase() + '%' : "");
			query.setParameter("unitId", pendingIssuesAcceptanceSearch.getUnitId()!=null ? pendingIssuesAcceptanceSearch.getUnitId() : sessionService.getUnitId());
			query.setParameter("screenId", screenId);
			query.setParameter("statusId", pendingIssuesAcceptanceSearch.getStatusId() != null ? pendingIssuesAcceptanceSearch.getStatusId() : 0);
			
		return query.list();
	}
	catch(Exception e)
	{
		throw e;
	}
	}

}
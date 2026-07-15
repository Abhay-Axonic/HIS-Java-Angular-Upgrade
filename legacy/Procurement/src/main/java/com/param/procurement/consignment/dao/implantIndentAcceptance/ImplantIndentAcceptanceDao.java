package com.param.procurement.consignment.dao.implantIndentAcceptance;

import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.PatientIssueReturn;
import com.param.entity.model.pharmacy.Sale;
import com.param.global.common.ServiceException;
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentRequest;
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentResponse;

@Repository
public class ImplantIndentAcceptanceDao extends AbstractDao<Integer, PatientIssueReturn>
		implements IImplantIndentAcceptanceDao {

	final static Logger logger = Logger.getLogger(ImplantIndentAcceptanceDao.class);

	@Autowired
	private SessionService sessionService;

	@Override
	public List<ImplantIndentResponse> searchMaterialIssueAcceptanceList(ImplantIndentRequest request,
			Integer screenId) {
		try {
			String hql = "SELECT issue.id AS id, issue.addedDate AS issueDate, issue.issueNo AS issueNo,"
					+ " implantIndent.implantIndentNo AS indentNo,"
					+ " store.storeId AS storeId, store.storeName AS store,"
					+ " priority.priorityDesc AS priority, indentType.type AS indentType,"
					+ " issue.isConsignment AS isConsignment, issue.remark AS remark, status.status AS status,"
					+"  concat(COALESCE(patient.firstName, ''),' ',COALESCE(patient.middleName, ''), ' ', COALESCE(patient.lastName,'')) AS patientName,"
					+"  patient.uhidNumber AS uhid,status.id AS statusId"
					+ " FROM ImplantIndent AS implantIndent " 
					+ "	INNER JOIN implantIndent.issue as issue "
					+ "	INNER JOIN issue.addedBy as user "
					+ "	INNER JOIN implantIndent.patient as patient "
					+ "	LEFT JOIN issue.store as store " 
					+ "	LEFT JOIN issue.status as status "
					+ "	LEFT JOIN issue.implantIndent.indentType as indentType "
					+ "	LEFT JOIN issue.implantIndent.priority as priority "
					+ " WHERE issue.isActive = true AND issue.isDeleted = false AND issue.unit.unitId = :unitId "
					+ "	AND issue.addedDate BETWEEN :fromDate AND :toDate "
					+ " AND (store.storeId = :storeId OR :storeId = 0 ) "

					//+ " AND (:indentNo = '' OR lower(issue.implantIndent.implantIndentNo) LIKE :indentNo) "
					+ " AND (:issueNo = '' OR issue.issueNo LIKE :issueNo) "
					+ " AND (:statusId = 0 OR status.id = :statusId) "
					+ " AND (:indentTypeId = 0 OR indentType.id = :indentTypeId) "
					+ " AND (:patientId = 0 OR patient.patientId = :patientId) "
					+ "	AND status.id = :implantStatusId   "
					+ "ORDER BY implantIndent.id DESC";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", request.getFromDate());
			query.setParameter("toDate", request.getToDate());
			query.setParameter("storeId", request.getStoreId() != null ? request.getStoreId() : 0);
			//query.setParameter("indentNo",request.getIndentNo() != null ? request.getIndentNo().toLowerCase() + '%' : "");
			query.setParameter("issueNo",
					request.getIssueNo() != null ? request.getIssueNo() + '%' : "");
			query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
			query.setParameter("implantStatusId", com.param.common.constants.Status.Completed.getStatus());
			query.setParameter("indentTypeId", request.getIndentTypeId() != null ? request.getIndentTypeId() : 0);
			query.setParameter("patientId", request.getPatientId() != null ? request.getPatientId() : 0);
			
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
			//query.setParameter("userId", sessionService.getUserId());
			query.setResultTransformer(Transformers.aliasToBean(ImplantIndentResponse.class));
			//com.param.common.constants.Status.Closed.getStatus()

			return query.list();
		} catch (Exception e) {
			logger.error("searchMaterialIssueAcceptanceList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Issue getIssue(Integer id) {
		String hql = " SELECT issue FROM Issue AS issue "
				+ "	WHERE issue.id = :id AND issue.isActive = true AND issue.isDeleted = false ";

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);

		return (Issue) query.uniqueResult();
	}

	@Override
	public PatientIssueReturn savePatientIssueReturn(PatientIssueReturn issueReturn) {
		getSession().save(issueReturn);
		return issueReturn;
	}

	@Override
	public StoreStock saveStoreStock(StoreStock storeStockNew) {
		getSession().save(storeStockNew);
		return storeStockNew;
	}
	
	@Override
	public Sale saveSale(Sale sale) {
		getSession().save(sale);
		return sale;
	}

}

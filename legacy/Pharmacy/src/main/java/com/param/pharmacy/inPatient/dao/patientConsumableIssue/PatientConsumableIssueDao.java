package com.param.pharmacy.inPatient.dao.patientConsumableIssue;

import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.PatientIndent;
import com.param.entity.model.pharmacy.Sale;
import com.param.global.common.ServiceException;
import com.param.global.dto.global.PatientSearchDetails;
import com.param.global.utility.Util;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.PatientConsumableIssueRequest;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.SubstitutePatientConsumableDto;

@Repository("patientConsumableIssueDao")
public class PatientConsumableIssueDao extends AbstractDao<Integer, Issue> implements IPatientConsumableIssueDao {

	private static final Logger logger = Logger.getLogger(PatientConsumableIssueDao.class);

	@Autowired
	private SessionService sessionService;

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public List<Issue> patientConsumableIssueSearch(PatientConsumableIssueRequest request, Integer screenId) {
		try {
			String hql = " SELECT issue FROM Issue AS issue "
					+ " LEFT JOIN issue.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN issue.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN admission.admissionDetail AS admissionDetail WITH admissionDetail.status = 'A' "
					+ " LEFT JOIN admissionDetail.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
					+ " LEFT JOIN admissionDetail.bedMaster AS bedMaster WITH bedMaster.status = 'A' "
					+ " LEFT JOIN issue.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ "	LEFT JOIN issue.toStore as toStore WITH toStore.isActive = true AND toStore.isDeleted = false  "
					+ " INNER JOIN issue.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " LEFT JOIN admission.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN encounter.patient AS patientEncounter WITH patientEncounter.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH prefixEncounter.status = 'A' "
					+ " INNER JOIN issue.visitType AS visitType WITH visitType.status = 'A' "
					+ " LEFT JOIN issue.patientIndent AS patientIndent WITH patientIndent.isActive = true AND patientIndent.isDeleted = false "
					+ " LEFT JOIN issue.implantIndent AS implantIndent WITH implantIndent.isActive = true AND implantIndent.isDeleted = false "
					+ " WHERE issue.isActive = true AND issue.isDeleted = false AND issue.unit.unitId = :unitId AND issue.addedDate BETWEEN :fromDate AND :toDate"
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId OR patientEncounter.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					+ " OR lower(COALESCE(CONCAT(prefixEncounter.prefixCode,' ',patientEncounter.firstName,' ',patientEncounter.middleName,' ', patientEncounter.lastName))) LIKE :patientName "
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName OR lower(prefixEncounter.prefixCode) LIKE :patientName OR "
					+ "	lower(patientEncounter.firstName) LIKE :patientName OR lower(patientEncounter.middleName) LIKE :patientName "
					+ "	OR lower(patientEncounter.lastName) LIKE :patientName)) "
					+ " AND ((:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid OR lower(patientEncounter.uhidNumber) LIKE :uhid))"
					+ " AND ( status.id = :statusId OR :statusId = 0 ) "
					// + " AND ( store.storeId = :store OR :store = 0 ) "

					+ " 	AND (:fromStoreId = 0 OR store.storeId = :fromStoreId) "
					+ " 	AND (:toStoreId = 0 OR (toStore.storeId = :toStoreId "
					+ "			AND store.storeId IN (SELECT userStore.store.storeId FROM UserStoreMapper AS userStore "
					+ "								WHERE userStore.userId = :userId AND userStore.isActive = true AND userStore.isDeleted = false "
					+ "								 AND userStore.store.isDeleted = false AND userStore.store.isActive = true))) "

					+ " AND ( wardMaster.wardId = :wardId OR :wardId = 0 ) "
					+ " AND (:bedNo = '' OR lower(bedMaster.bedNumber) LIKE :bedNo )"
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND( :visitNo = '' OR lower(admission.admissionNumber) LIKE :visitNo OR lower(encounter.encounterNumber) LIKE :visitNo) "
					+ " AND(( :indentNo = '' OR lower(patientIndent.indentNo) LIKE :indentNo) "
					+ " OR( :indentNo = '' OR lower(implantIndent.implantIndentNo) LIKE :indentNo)) "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "	INNER JOIN screenStatus.status as status "
					+ " WHERE status.id NOT IN (4, 8)) ) "
					+ " ORDER BY issue.id DESC";
			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("visitType", request.getVisitType() != null ? request.getVisitType() : 0);
			query.setParameter("patientId", request.getPatientId() != null ? request.getPatientId() : 0);
			query.setParameter("patientName",
					request.getPatientName() != null ? '%'+request.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("uhid", request.getUhid() != null ? '%'+request.getUhid().toLowerCase() + '%' : "");
			// query.setParameter("issueNo",request.getIssueNo()!= null ?
			// request.getIssueNo().toLowerCase() + '%' : "");
			query.setParameter("indentNo",
					request.getIndentNo() != null ? '%'+ request.getIndentNo().toLowerCase() + '%' : "");
			query.setParameter("visitNo", request.getVisitNo() != null ? '%'+request.getVisitNo().toLowerCase() + '%' : "");
			// query.setParameter("store", sessionService.getStoreId() != null ?
			// sessionService.getStoreId() : 0);
			query.setParameter("fromStoreId", request.getFromStoreId() != null ? request.getFromStoreId() : 0);
			query.setParameter("toStoreId", request.getToStoreId() != null ? request.getToStoreId() : 0);
			query.setParameter("wardId", request.getWardId() != null ? request.getWardId() : 0);
			query.setParameter("bedNo", request.getBedNo() != null ? '%'+request.getBedNo().toLowerCase() + '%' : "");
			query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
			query.setParameter("screenId", screenId);
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
			query.setParameter("userId", request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

			return query.list();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Issue patientConsumableIssue(Integer id) {
		try {

			String hql = " SELECT issue FROM Issue AS issue "
					+ "	WHERE issue.isActive = true AND issue.isDeleted = false AND "
					+ "((issue.isImplant=true and issue.isConsignment=false) OR "
					+ "(issue.isImplant=false and issue.isConsignment=true) OR "
					+ "(issue.isImplant=false and issue.isConsignment=false)) "
					+ " AND issue.id = :id AND issue.unit.unitId = :unitId";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", sessionService.getUnitId());

			return (Issue) query.uniqueResult();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Issue patientConsumableIssueById(Integer id, Integer unitId) {
		try {

			String hql = " SELECT issue FROM Issue AS issue "
					+ " LEFT JOIN issue.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN issue.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN admission.admissionDetail AS admissionDetail WITH admissionDetail.status = 'A' "
					+ " LEFT JOIN admissionDetail.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
					+ " LEFT JOIN admissionDetail.bedMaster AS bedMaster WITH bedMaster.status = 'A' "
					+ " INNER JOIN issue.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " INNER JOIN issue.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " LEFT JOIN admission.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN encounter.patient AS patientEncounter WITH patientEncounter.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH prefixEncounter.status = 'A' "
					+ " INNER JOIN issue.visitType AS visitType WITH visitType.status = 'A' "
					+ " LEFT JOIN issue.patientIndent AS patientIndent WITH patientIndent.isActive = true AND patientIndent.isDeleted = false "
					+ " LEFT JOIN issue.implantIndent AS implantIndent WITH implantIndent.isActive = true AND implantIndent.isDeleted = false "
					+ " WHERE issue.isActive = true AND issue.isDeleted = false AND issue.unit.unitId = :unitId AND issue.id = :id";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());

			return (Issue) query.uniqueResult();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public IssueDetail patientConsumableIssueDetail(Integer id) {
		try {
			String hql = " SELECT issueDetail FROM IssueDetail AS issueDetail "
					+ " WHERE issueDetail.isActive = true AND issueDetail.isDeleted = false AND issueDetail.id = :id ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			//query.setParameter("unitId", sessionService.getUnitId());

			return (IssueDetail) query.uniqueResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<IssueDetail> getpatientConsumableIssueDetail(Integer issueId) {
		try {
			String hql = " SELECT distinct issueDetail FROM IssueDetail AS issueDetail "
					+ " INNER JOIN issueDetail.issue AS issue WITH issue.isActive = true AND issue.isDeleted = false "
					+ " WHERE issueDetail.isActive = true AND issueDetail.isDeleted = false AND issue.id = :id ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", issueId);
			//query.setParameter("unitId", sessionService.getUnitId());

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public PatientIndent savePatientIndent(PatientIndent patientIndent) {
		getSession().save(patientIndent);
		return patientIndent;
	}

	@Override
	public Sale saveSale(Sale sale) {
		getSession().save(sale);
		return sale;
	}

	@Override
	public List<Integer> getIssueDetailids(Integer id, Integer unitId) {

		try {
			/*
			 * String sql = "  select  max(issuedetai0_.id) " +
			 * " from pharmacy.t_issue_detail issuedetai0_ inner join pharmacy.t_issue issue1_ on issuedetai0_.issue_id=issue1_.id "
			 * + " and (issue1_.is_active=true and issue1_.is_deleted=false)  " +
			 * " where issuedetai0_.is_active=true and issuedetai0_.is_deleted=false and issue1_.id=:id and issue1_.unit_id=:unitId "
			 * + " group by issuedetai0_.issue_id, issuedetai0_.patient_indent_detail_id";
			 */

			String sql = "  select   max(issuedetai0_.id) "
					+ " from pharmacy.t_issue_detail issuedetai0_ inner join pharmacy.t_issue issue1_ on issuedetai0_.issue_id=issue1_.id "
					+ " and (issue1_.is_active=true and issue1_.is_deleted=false)  "
					+ " where issuedetai0_.is_active=true and issuedetai0_.is_deleted=false and issue1_.id=:id  "
					+ " group by issuedetai0_.issue_id, issuedetai0_.patient_indent_detail_id";
			Query query = getSession().createSQLQuery(sql);
			query.setParameter("id", id);
			//query.setParameter("unitId", sessionService.getUnitId());

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IssueDetail> getpatientConsumableIssueDetailByIds(List<Integer> ids, Integer screenFlag, Integer unitId) {

		try {
			if (screenFlag.equals(1)) {
				String hql = " SELECT distinct issueDetail FROM IssueDetail AS issueDetail "
						+ " INNER JOIN issueDetail.issue AS issue WITH issue.isActive = true AND issue.isDeleted = false "
						+ " INNER JOIN issueDetail.status status "
						+ " WHERE issueDetail.isActive = true AND issueDetail.isDeleted = false AND issueDetail.id in (:ids) "
						+ " AND issue.unit.unitId = :unitId AND status.id in (2, 4) ";
				Query query = getSession().createQuery(hql);
				query.setParameterList("ids", ids);
				query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());

				return query.list();
			} else {
				String hql = " SELECT distinct issueDetail FROM IssueDetail AS issueDetail "
						+ " INNER JOIN issueDetail.issue AS issue WITH issue.isActive = true AND issue.isDeleted = false "
						+ " INNER JOIN issueDetail.status status "
						+ " WHERE issueDetail.isActive = true AND issueDetail.isDeleted = false AND issueDetail.id in (:ids) "
						+ " AND issue.unit.unitId = :unitId AND status.id not in (2) ";
				Query query = getSession().createQuery(hql);
				query.setParameterList("ids", ids);
				query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());

				return query.list();
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public List<Integer> getBatchMapperIds(Integer id) {
		String sql = "   select max(id) from pharmacy.t_issue_detail_batch_mapper where issue_detail_id=:id "
				+ " and is_active=true and is_deleted=false ";
		// " group by issuedetai0_.issue_id, issuedetai0_.patient_indent_detail_id";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("id", id);
		// query.setParameter("unitId", sessionService.getUnitId());
		return query.list();
	}

	@Override
	public List<IssueDetailBatchMapper> getBatchMapperList(List<Integer> batchMapperIds) {

		try {
			String hql = " SELECT issueDetailBatchMapper FROM IssueDetailBatchMapper AS issueDetailBatchMapper "
					+ " WHERE issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
					+ " AND issueDetailBatchMapper.unit.unitId = :unitId ";
			Query query = getSession().createQuery(hql);
			query.setParameterList("id", batchMapperIds);
			//query.setParameter("unitId", sessionService.getUnitId());

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public List<IssueDetail> patientConsumableIssueDetail(Integer id, List<Integer> ids) {

		try {
			String hql = " SELECT issueDetail FROM IssueDetail AS issueDetail  INNER JOIN issueDetail.issue AS issue WITH issue.isActive = true AND issue.isDeleted = false "
					+ " WHERE issue.id=:id AND issueDetail.isActive = true AND issueDetail.isDeleted = false AND issueDetail.id not in (:ids) ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			query.setParameterList("ids", ids);

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public void updateIssueDetail(IssueDetail issueD) {

		getSession().update(issueD);
		
	
		
	}

	@Override
	public void updatePatientIndent(PatientIndent patientIndent) {
		getSession().update(patientIndent);
		
	
	}

	@Override
	public List<SubstitutePatientConsumableDto> getSubstituteIssueDetails(Integer id) {
		try {
			String hql = " select " + 
					"	tid.id as \"issueDetailId\", " + 
					"	tid.sale_detail_id as \"saleDetailId\" ,issue_id as \"issueId\", " + 
					"	tid.patient_indent_detail_id as \"patientIndentDetailId\", " + 
					"	tid.status_id as \"statusId\" , " + 
					"	tid.issue_quantity as \"issueQuantity\", " + 
					"	tid.batch_id as \"batchId\", " + 
					"	tid.uom_type_id as \"uomTypeId\", " + 
					"	tid.uom_unit_id as \"uomUnitId\", " + 
					"	tid.net_amount as \"netAmount\", " + 
					"	tid.credit_payable as \"creditPayable\" , " + 
					"	tid.self_payable as \"selfPayable\", " + 
					"	tid.discount_amount as \"discountAmount\" ,rate as \"rate\", " + 
					"	tid.tax_amount as \"taxAmount\", " + 
					"	tid.tax_percentage as \"taxPercentage\", " + 
					"	tid.total_amount as \"totalAmount\", " + 
					"	tid.unit_discount_amount as \"unitDiscountAmount\", " + 
					"	tid.package_consumption_amt as \"packageConsumptionAmount\", consession_amount as \"concessionAmount\" , " + 
					"	tid.unit_consession_amount as \"unitConsessionAmount\", " + 
					"	tid.consession_percentage as \"consessionPercentage\", " + 
					"	tid.parent_issue_detail_id as \"parentIssueDetailId\" ,is_substitute as \"isSubstitute\", " + 
					"	tid.is_narcotic_substitute as \"isNarcoticSubstitute\", " + 
					"	mi.name as \"itemName\", " + 
					"	mi.code as \"itemCode\", " + 
					"	muu.unit as \"uomUnit\", " + 
					"	mut.type as \"uomType\","
					+ " tpid.item_id as \"itemId\","
					+ " generic.name as \"genericName\", " + 
					"	tpid.indent_quantity as \"indentQuantity\" " + 
					" from " + 
					"	pharmacy.t_issue_detail tid " + 
					"	inner join pharmacy.t_patient_indent_detail tpid on tpid.id =tid.patient_indent_detail_id  " + 
					"	inner join public.m_item mi on tpid.item_id =mi.id  " + 
					"	inner join public.m_uom_unit muu on muu.id =tpid.uom_unit_id  " + 
					"	inner join public.m_uom_type mut on mut.id=tpid.uom_type_id  "
					+ " 	inner join public.m_generic generic on generic.id =mi.generic_id " + 
					" where " + 
                    "    tid.issue_id = :id" + 
					"	and tid.parent_issue_detail_id is not null; "
					;
			Query query = getSession().createSQLQuery(hql)
					.addScalar("issueQuantity")
					.addScalar("itemId")
					.addScalar("itemName")
					.addScalar("itemCode")
					.addScalar("uomUnit")
					.addScalar("uomType")
					.addScalar("genericName")
					.addScalar("indentQuantity")
					.addScalar("issueId")
					.addScalar("saleDetailId")
					.addScalar("patientIndentDetailId")
					.addScalar("statusId")
					.addScalar("batchId")
					.addScalar("netAmount")
					.addScalar("creditPayable")
					.addScalar("discountAmount")
					.addScalar("rate")
					.addScalar("taxAmount")
					.addScalar("taxPercentage")
					.addScalar("unitDiscountAmount")
					.addScalar("packageConsumptionAmount")
					.addScalar("concessionAmount")
					.addScalar("uomUnitId")
					.addScalar("uomTypeId")
					.addScalar("parentIssueDetailId")
					.addScalar("isNarcoticSubstitute")
					.addScalar("isSubstitute")
					.addScalar("totalAmount")
					.addScalar("issueDetailId");
			query.setParameter("id", id);
			query.setResultTransformer(Transformers.aliasToBean(SubstitutePatientConsumableDto.class));

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public List<IssueDetailBatchMapper> getBatchMapperForSubstitute(Integer issueDetailId) {
		try {
			String hql = " SELECT issueDetailBatchMapper FROM IssueDetailBatchMapper AS issueDetailBatchMapper "
					+ " WHERE issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false AND issueDetailBatchMapper.issueDetail.id =:id ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", issueDetailId);
			//query.setParameter("unitId", sessionService.getUnitId());

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}



}
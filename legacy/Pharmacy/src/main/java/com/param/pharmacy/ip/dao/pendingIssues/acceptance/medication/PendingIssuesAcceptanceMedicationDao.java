package com.param.pharmacy.ip.dao.pendingIssues.acceptance.medication;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.MedicationHandover;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.Reject;
import com.param.entity.model.pharmacy.ReturnOfEmptyAmpoules;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceItem;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSave;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSearch;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationSearchDto;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationSearchRequest;
import com.param.pharmacy.nursing.workstation.dto.drugAdministration.DrugAdministrationItem;
import com.param.procurement.purchase.dto.itemRequest.ItemRequestDto;
import com.param.service.global.SessionService;

@Repository("pendingIssuesAcceptanceMedicationDao")
public class PendingIssuesAcceptanceMedicationDao extends AbstractDao<Integer, Issue>
		implements IPendingIssuesAcceptanceMedicationDao {

	@Autowired
	private SessionService sessionService;

	@Autowired
	SessionFactory sessionFactory;

	private static final Logger logger = Logger.getLogger(PendingIssuesAcceptanceMedicationDao.class);

	@Override
	public List<Issue> pendingIssuesAcceptanceMedicationSearch(
			PendingIssuesAcceptanceSearch pendingIssuesAcceptanceSearch, Integer screenId) {

		try {

			String hql = " SELECT issue FROM Issue AS issue "
					+ " INNER JOIN issue.sale AS sale WITH sale.isActive = true AND sale.isDeleted = false "
					+ " INNER JOIN issue.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " LEFT JOIN issue.doctor AS doctor WITH doctor.status = 'A'"
					+ " LEFT JOIN issue.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A'"
					+ " INNER JOIN issue.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
					+ " LEFT JOIN issue.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN admission.admissionDetail AS admissionDetail WITH admissionDetail.status = 'A' "
					+ " LEFT JOIN admissionDetail.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
					+ " LEFT JOIN admissionDetail.bedMaster AS bedMaster WITH bedMaster.status = 'A' "
					+ " LEFT JOIN bedMaster.floor AS floor WITH floor.status = 'A' "
					+ " LEFT JOIN issue.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN admission.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN encounter.patient AS patientEncounter WITH patientEncounter.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH prefixEncounter.status = 'A' "
					+ " INNER JOIN issue.visitType AS visitType WITH visitType.status = 'A' "
					+ " INNER JOIN issue.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " WHERE    (:statusId = 0 OR status.id = :statusId) AND (status.id!=2 ) and issue.isActive = true AND issue.isDeleted = false AND issue.onGoing = false AND issue.unit.unitId = :unitId AND issue.addedDate BETWEEN :fromDate AND :toDate "
					/*+ " AND ((:doctorName = '' OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ', doctor.lastName))) LIKE :doctorName OR "
					+ " lower(doctor.firstName) LIKE :doctorName) OR (:doctorName = '' OR lower(doctor.middleName) LIKE :doctorName)"
					+ "	OR (:doctorName = '' OR lower(doctor.lastName) LIKE :doctorName))"*/
					+ " AND ( prescription.doctor.doctorId = :doctorId OR :doctorId = 0 ) "
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId OR patientEncounter.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					+ " OR lower(COALESCE(CONCAT(prefixEncounter.prefixCode,' ',patientEncounter.firstName,' ',patientEncounter.middleName,' ', patientEncounter.lastName))) LIKE :patientName "
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName OR lower(prefixEncounter.prefixCode) LIKE :patientName OR "
					+ "	lower(patientEncounter.firstName) LIKE :patientName OR lower(patientEncounter.middleName) LIKE :patientName "
					+ "	OR lower(patientEncounter.lastName) LIKE :patientName)) "
					+ " AND ( floor.id = :floor OR :floor = 0 ) "
					+ " AND( :uhid = '' OR lower(patient.uhidNumber) LIKE :uhid OR lower(patientEncounter.uhidNumber) LIKE :uhid) "
					+ " AND( wardMaster.wardId in ( :wardId )) "
					+ " AND( :issueNo = '' OR lower(issue.issueNo) LIKE :issueNo) "
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
					+ "	INNER JOIN screenStatus.status as status) " + " ORDER BY issue.id DESC ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(pendingIssuesAcceptanceSearch.getFromDate()));
			query.setParameter("toDate", Util.getDate(pendingIssuesAcceptanceSearch.getToDate()));
			query.setParameter("visitType",
					pendingIssuesAcceptanceSearch.getVisitType() != null ? pendingIssuesAcceptanceSearch.getVisitType()
							: 0);
			query.setParameter("patientId",
					pendingIssuesAcceptanceSearch.getPatientId() != null ? pendingIssuesAcceptanceSearch.getPatientId()
							: 0);
			query.setParameter("patientName",
					pendingIssuesAcceptanceSearch.getPatientName() != null
							? '%'+pendingIssuesAcceptanceSearch.getPatientName().toLowerCase() + '%'
							: "");
			/*query.setParameter("doctorName",
					pendingIssuesAcceptanceSearch.getDoctorName() != null
							? '%'+pendingIssuesAcceptanceSearch.getDoctorName().toLowerCase() + '%'
							: "");*/
			query.setParameter("doctorId",
					pendingIssuesAcceptanceSearch.getDoctorId() != null
							? pendingIssuesAcceptanceSearch.getDoctorId()
							: 0);
			
			query.setParameter("issueNo",
					pendingIssuesAcceptanceSearch.getIssueNo() != null
							?'%'+ pendingIssuesAcceptanceSearch.getIssueNo().toLowerCase() + '%'
							: "");
			List<Integer> wardList = new ArrayList<>();
			if(pendingIssuesAcceptanceSearch.getWardList()!=null && pendingIssuesAcceptanceSearch.getWardList().size()>0) {
				wardList.addAll(pendingIssuesAcceptanceSearch.getWardList());
			}else {
				wardList.add(0);
			}
			query.setParameterList("wardId", wardList);
//			query.setParameterList("wardId", pendingIssuesAcceptanceSearch.getWardList());
			query.setParameter("floor",
					pendingIssuesAcceptanceSearch.getFloor() != null ? pendingIssuesAcceptanceSearch.getFloor() : 0);
			query.setParameter("uhid",
					pendingIssuesAcceptanceSearch.getUhid() != null
							? '%'+pendingIssuesAcceptanceSearch.getUhid().toLowerCase() + '%'
							: "");
			query.setParameter("unitId", pendingIssuesAcceptanceSearch.getUnitId()!=null ? pendingIssuesAcceptanceSearch.getUnitId() : sessionService.getUnitId());
			query.setParameter("screenId", screenId);
			query.setParameter("statusId", pendingIssuesAcceptanceSearch.getStatusId() != null ? pendingIssuesAcceptanceSearch.getStatusId() : 0);

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Issue pendingIssuesAcceptanceMedicationById(Integer id) {
		try {
			String hql = "  SELECT issue FROM Issue AS issue "
					+ " WHERE issue.isActive = true AND issue.isDeleted = false " + " AND issue.id = :id ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);

			return (Issue) query.uniqueResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<PendingIssuesAcceptanceItem> pendingIssuesAcceptanceMedicationDetails(Integer issueId) {
		try {
			String hql = "  SELECT issueDetail.id AS id, item.code AS itemCode, item.name AS itemName, item.id AS itemId, generic.name AS genericName, prescriptionDetail.priorityId as priorityId, prescriptionDetail.orderQty as orderQty, "
					+ " generic.id AS genericId, manufacturer.name AS manufacturerName, manufacturer.id AS manufacturerId, item.isOtc AS issueAcceptanceflag, "
					+ " uomType.type AS uom, uomUnit.units AS uomUnit, uomUnit.id AS uomUnitId, batch.batchNo AS batchNo, batch.expiryDate AS expiryDate, "
					+ " issueDetailBatchMapper.amount AS rate, issueDetailBatchMapper.issueQuantity AS quantityIssued, issueDetailBatchMapper.id AS batchMapperId,"
					+ " status.id AS statusId, status.status AS status, issueDetailBatchMapper.acceptedQuantity AS acceptQuantity, issueDetailBatchMapper.rejectedQuantity AS rejectQuantity, "
					+ " rejectReason.id AS rejectReasonId, issueDetailBatchMapper.rejectNote AS rejectNote , uomType.id as uomTypeId "
					+ " FROM IssueDetail AS issueDetail "
					+ " INNER JOIN issueDetail.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.uomType AS uomType WITH uomType.isActive = true AND uomType.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false   "
					+ " INNER JOIN issueDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false   "
					+ " LEFT JOIN issueDetailBatchMapper.rejectReason AS rejectReason WITH rejectReason.isActive = true AND rejectReason.isDeleted = false "
					+ " INNER JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
					+ " INNER JOIN item.generic AS generic WITH generic.isActive = true AND generic.isDeleted = false "
					+ " INNER JOIN item.manufacturer AS manufacturer WITH manufacturer.isActive = true AND manufacturer.isDeleted = false "
					+ " INNER JOIN issueDetail.issue AS issue WITH issue.isActive = true AND issue.isDeleted = false "
					+ " WHERE issueDetail.isActive = true AND issueDetail.isDeleted = false " + " AND issue.id = :id ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", issueId);
			query.setResultTransformer(Transformers.aliasToBean(PendingIssuesAcceptanceItem.class));
			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Issue pendingIssuesAcceptanceMedicationSave(Issue issue) {
		getSession().update(issue);
		return issue;
	}

	@Override
	public IssueDetail pendingIssuesAcceptanceMedicationDetailById(Integer issueDetailId) {
		try {
			String hql = "  SELECT issueDetail FROM IssueDetail AS issueDetail "
					+ " WHERE issueDetail.isActive = true AND issueDetail.isDeleted = false "
					+ " AND issueDetail.id = :id ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", issueDetailId);

			return (IssueDetail) query.uniqueResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Reject pendingIssuesAcceptanceMedicationRejectSave(Reject reject) {
		getSession().save(reject);
		return reject;
	}

	@Override
	public ReturnOfEmptyAmpoules saveReturnOfEmptyAmpules(ReturnOfEmptyAmpoules ampoules) {
		getSession().save(ampoules);
		return ampoules;
	}

	@Override
	public List<PrescriptionDetail> medicationHandoverSearch(MedicationReconciliationSearchRequest request,
			Integer screenId, Integer admissionId) {

		try {
			String hql = " SELECT prescriptionDetail FROM PrescriptionDetail AS prescriptionDetail "
					+ " INNER JOIN prescriptionDetail.prescription AS prescription WITH   prescription.isActive = true AND prescription.isDeleted = false "
					+ " INNER JOIN prescription.doctor AS doctor WITH doctor.status = 'A' "
					+ " INNER JOIN prescription.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A' "
					+ " LEFT JOIN prescription.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN admission.admissionDetail AS admissionDetail WITH admissionDetail.status = 'A' "
					+ " LEFT JOIN admissionDetail.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
					+ " LEFT JOIN admissionDetail.bedMaster AS bedMaster WITH bedMaster.status = 'A' "
					+ " LEFT JOIN bedMaster.floor AS floor WITH floor.status = 'A' "
					// + " LEFT JOIN issue.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN admission.patient AS patient WITH patient.status = 'A' "
					// + " LEFT JOIN encounter.patient AS patientEncounter WITH
					// patientEncounter.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					// + " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH
					// prefixEncounter.status = 'A' "
					+ " INNER JOIN prescription.visitType AS visitType WITH visitType.status = 'A' "
					+ " INNER JOIN prescriptionDetail.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ " INNER JOIN store.storeType AS storeType WITH storeType.isActive = true AND storeType.isDeleted = false "
					+ " left outer  JOIN prescription.reconciliationStatus AS reconciliationStatus WITH reconciliationStatus.isActive = true AND reconciliationStatus.isDeleted = false "
					// + " INNER JOIN issue.prescription AS prescription WITH prescription.isActive
					// = true AND prescription.isDeleted = false "
					// + " INNER JOIN prescription.priority AS priority WITH priority.status = 'A' "
					+ " WHERE admission.admissionId =:admissionId and  prescriptionDetail.isDischarge = true and prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false AND prescription.unit.unitId = :unitId  "
					// + " AND ((:doctorName = '' OR lower(COALESCE(CONCAT(doctor.firstName,'
					// ',doctor.middleName,' ', doctor.lastName))) LIKE :doctorName OR "
					// + " lower(doctor.firstName) LIKE :doctorName) OR (:doctorName = '' OR
					// lower(doctor.middleName) LIKE :doctorName)"
					// + " OR (:doctorName = '' OR lower(doctor.lastName) LIKE :doctorName))"
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					// + " OR lower(COALESCE(CONCAT(prefixEncounter.prefixCode,'
					// ',patientEncounter.firstName,' ',patientEncounter.middleName,' ',
					// patientEncounter.lastName))) LIKE :patientName "
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName)) "
					// + " lower(patientEncounter.firstName) LIKE :patientName OR
					// lower(patientEncounter.middleName) LIKE :patientName "
					// + " OR lower(patientEncounter.lastName) LIKE :patientName)) "
					+ " AND ( reconciliationStatus.id = :statusId OR :statusId = 0 ) "
					+ " AND( :uhid = '' OR lower(patient.uhidNumber) LIKE :uhid ) "
					+ " AND( wardMaster.wardId = :wardId OR :wardId = 0 ) "
					+ " AND( doctor.doctorId = :doctorId OR :doctorId = 0 ) "
					+ " AND( floor.floorId = :floorId OR :floorId = 0 ) "

					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					// + " AND reconciliationStatus.id IN (SELECT status.id FROM ScreenStatus AS
					// screenStatus INNER JOIN screenStatus.screen as screen WITH screen.id =
					// :screenId INNER JOIN screenStatus.status as status)"
					+ " ORDER BY prescriptionDetail.id DESC";

			Query query = getSession().createQuery(hql);
			// query.setParameter("fromDate", Util.getDate(request.getFromDate()));
			// query.setParameter("toDate", Util.getDate(request.getToDate()));
			query.setParameter("visitType", request.getVisitType() != null ? request.getVisitType() : 0);
			query.setParameter("patientId", request.getPatientId() != null ? request.getPatientId() : 0);
			query.setParameter("patientName",
					request.getPatientName() != null ? request.getPatientName().toLowerCase() + '%' : "");
			// query.setParameter("doctorName", request.getDoctorName() != null ?
			// request.getDoctorName().toLowerCase() + '%' : "");
			query.setParameter("wardId", request.getWardId() != null ? request.getWardId() : 0);
			query.setParameter("doctorId", request.getDoctorId() != null ? request.getDoctorId() : 0);
			query.setParameter("floorId", request.getFloorId() != null ? request.getFloorId() : 0);
			query.setParameter("statusId", request.getStatusId() != null ? request.getStatusId() : 0);
			query.setParameter("uhid", request.getUhid() != null ? request.getUhid().toLowerCase() + '%' : "");
			query.setParameter("unitId", sessionService.getUnitId());
			// query.setParameter("screenId", screenId);
			query.setParameter("admissionId", admissionId);

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public MedicationReconciliationSearchDto getDischargeDate(Integer admissionId) {
		// String sql = " select discharge_date as dischargedate from adt.t_discharge
		// where admission_id=:admissionId";
		String sql = " select  dis.discharge_date as dischargedate , dis.discharge_status_id as dischargeStatusId, disStatus.discharge_desc as dischargeDesc "
				+ " from adt.t_discharge dis join adt.m_discharge_status_master disStatus on dis.discharge_status_id=disStatus.discharge_status_id"
				+ " where  disStatus.discharge_status_id=1 and  admission_id =:admissionId";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("admissionId", admissionId);
		query.setResultTransformer(Transformers.aliasToBean(MedicationReconciliationSearchDto.class));
		return (MedicationReconciliationSearchDto) query.uniqueResult();
	}

	@Override
	public void medicationHandoverSave(MedicationHandover mh) {

		getSession().save(mh);

	}

	@Override
	public void saveAmpoules(ReturnOfEmptyAmpoules ampoules) {

		getSession().save(ampoules);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response getIssueStatus(Integer id) {
		 try{
			 ItemRequestDto Obj = (ItemRequestDto)sessionFactory.getCurrentSession().createSQLQuery(
					" select status_id as \"statusId\" from pharmacy.t_issue where id  =:id ")
					 .setParameter("id", id)
					.setResultTransformer(Transformers.aliasToBean(ItemRequestDto.class)).uniqueResult();
				return new Response<>(null, null, null, null, Obj);
	        } catch (Exception e)
	        {
	        	logger.error(e.getMessage(), e);
	        	throw new ServiceException(e.getMessage());
	        }
	}

	@Override
	public Prescription getPrescription(Integer prescriptionId) {
		try {
			String hql = "  SELECT prescription FROM Prescription AS prescription "
					+ " WHERE prescription.isActive = true AND prescription.isDeleted = false " + " AND prescription.id = :id ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", prescriptionId.longValue());

			return (Prescription) query.uniqueResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public void update(Prescription p) {
		getSession().update(p);
		 
	}

	@Override
	public MedicationReconciliationSearchDto getIssueAcceptedQuantity(Long id) {
		String SQL ="select sum(tidbm.accepted_quantity) as \"acceptedQuantity\" "
				+ " from pharmacy.t_issue_detail_batch_mapper tidbm "
				+ " join pharmacy.t_issue_detail tid "
				+ " on tidbm.issue_detail_id = tid .id "
				+ " where tid.prescription_detail_id =:id ";
		Query query = getSession().createSQLQuery(SQL);
		query.setParameter("id",id);
		query.setResultTransformer(Transformers.aliasToBean(MedicationReconciliationSearchDto.class));
		
	return  (MedicationReconciliationSearchDto) query.uniqueResult();
	
	}

	@Override
	public Boolean getMedicationFlag(PendingIssuesAcceptanceSave pendingIssuesAcceptanceSave) {
        
        String SQL="select is_auto_medication_acceptance from public.m_application_configuration where organization_id=:organizationId and unit_id=:unitId ";
        Query query = getSession().createSQLQuery(SQL);
		query.setParameter("organizationId", pendingIssuesAcceptanceSave.getOrganizationId());
		query.setParameter("unitId", pendingIssuesAcceptanceSave.getUnitId());

		return (Boolean) query.uniqueResult();
	}

	@Override
	public String getRecievedBy(Integer id) {
		
		String SQL=" select array_to_string(array_agg(ts.received_by), ',') from  pharmacy.t_sale ts  where ts.issue_id =:id ";
        Query query = getSession().createSQLQuery(SQL);
		query.setParameter("id", id);
        return  (String) query.uniqueResult();
		
	}
}
package com.param.pharmacy.ip.dao.assignPicker;

import java.util.List;

import com.param.service.global.SessionService;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.TrueFalseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.global.common.ServiceException;
import com.param.entity.model.master.User;
import com.param.entity.model.pharmacy.AssignPicker;
import com.param.entity.model.pharmacy.AssignPickerDetail;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.PrescriptionFrequencyDetail;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dto.assignPicker.AssignPickerDetailItem;
import com.param.pharmacy.ip.dto.assignPicker.PrescriptionDetailsDto;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyItem;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationDetails;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;

@Repository("assignPickerDao")
public class AssignPickerDao extends AbstractDao<Integer, AssignPicker> implements IAssignPickerDao {

	private static final Logger logger = Logger.getLogger(AssignPickerDao.class);

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private SessionService sessionService;

	@Override
	public AssignPicker updateAssignPicker(AssignPicker assignPicker)
	{
		getSession().update(assignPicker);
		return assignPicker;
	}
	
	@Override
	public Issue saveIssue(Issue issue)
	{
		getSession().save(issue);
		return issue;
	}
	
	@Override
	public AssignPicker getAssignPicker(Integer id)
	{
		try
		{
			String hql = "SELECT assignPicker FROM AssignPicker AS assignPicker "
					+ " WHERE (:id = assignPicker.id) AND assignPicker.isActive = true AND assignPicker.isDeleted = false ";
					

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			
			return (AssignPicker) query.uniqueResult();
	    }
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public List<AssignPicker> assignPickerSearch(PharmacologyValidationSearch pharmacologyValidationSearch, Integer screenId)
	{
		try
		{
			String hql = "SELECT assignPicker FROM AssignPicker AS assignPicker "
					+ " INNER JOIN assignPicker.status as status WITH status.isActive = true AND status.isDeleted = false "
					+ " INNER JOIN assignPicker.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
					+ " INNER JOIN prescription.visitType as visitType WITH visitType.status = 'A'  "
					+ " INNER JOIN prescription.priority AS priority WITH priority.status = 'A'"
					+ " INNER JOIN prescription.doctor AS doctor WITH doctor.status = 'A'"
					+ " LEFT JOIN prescription.admission as admission WITH admission.status = 'A' "
					+ " LEFT JOIN admission.patient as patient WITH patient.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN admission.admissionDetail as admissionDetail WITH admissionDetail.status = 'A' "
					+ " LEFT JOIN admissionDetail.wardMaster as wardMaster WITH wardMaster.status = 'A' "
					+ " LEFT JOIN admissionDetail.bedMaster as bedMaster WITH bedMaster.status = 'A' "
					+ " WHERE prescription.id IN (  SELECT prescription1.id FROM Prescription AS prescription1 INNER JOIN prescription1.prescriptionDetailsList AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
					+ "								INNER JOIN prescriptionDetail.store AS store WITH store.isActive = true AND store.isDeleted = false "
					+ "							    INNER JOIN store.storeType AS storeType WITH storeType.isActive = true AND storeType.isDeleted = false "
					/*+ "						        WHERE  storeType.id != 6 "*/
					+ "							  ) "
					+ " AND assignPicker.isActive = true AND assignPicker.isDeleted = false AND assignPicker.addedDate BETWEEN  :fromDate AND :toDate "	
					+ " AND ( :wardName = '' OR lower(wardMaster.wardName) LIKE :wardName) "
					+ " AND assignPicker.unit.unitId = :unitId AND ( visitType.visitTypeId = :visitTypeId OR :visitTypeId = 0 ) "
					+ " AND visitType.visitTypeCode != 'OP'"
					+ " AND ( status.id = :statusId OR :statusId = 0 )"
					+ " AND ((:doctorName = '' OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ', doctor.lastName))) LIKE :doctorName OR lower(doctor.firstName) LIKE :doctorName) OR (:doctorName = '' OR lower(doctor.middleName) LIKE :doctorName)" 
					+ "	OR (:doctorName = '' OR lower(doctor.lastName) LIKE :doctorName))"
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "		
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName )) "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
					+ "	INNER JOIN screenStatus.status as status) "
					+ " ORDER BY  :orderby ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(pharmacologyValidationSearch.getFromDate()));
			query.setParameter("toDate", Util.getDate(pharmacologyValidationSearch.getToDate()));
			query.setParameter("statusId", pharmacologyValidationSearch.getStatusId() != null ? pharmacologyValidationSearch.getStatusId() : 0);
			query.setParameter("visitTypeId",pharmacologyValidationSearch.getVisitType() != null ? pharmacologyValidationSearch.getVisitType() : 0);
			query.setParameter("patientId",pharmacologyValidationSearch.getPatientId() != null ? pharmacologyValidationSearch.getPatientId() : 0);
			query.setParameter("patientName",pharmacologyValidationSearch.getPatientName() != null ? pharmacologyValidationSearch.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("doctorName",pharmacologyValidationSearch.getDoctorName()!= null ? pharmacologyValidationSearch.getDoctorName().toLowerCase() + '%' : "");
			query.setParameter("wardName",pharmacologyValidationSearch.getWardName() != null ? pharmacologyValidationSearch.getWardName().toLowerCase() + '%' : "");
			query.setParameter("unitId", sessionService.getUnitId());
			query.setParameter("screenId", screenId);
			query.setParameter("orderby", "FIELD(priority.priorityDesc, 'High','Normal','Low'), assignPicker.id DESC");
			
		return query.list();
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}

	}

	@Override
	public List<AssignPickerDetail> assignPickerDetails(Integer id)
	{
		try
		{
			String hql = "SELECT assignPickerDetail FROM AssignPickerDetail AS assignPickerDetail "
					+ " INNER JOIN assignPickerDetail.assignPicker AS assignPicker WITH assignPicker.isActive = true AND assignPicker.isDeleted = false "
					+ " WHERE (:id = assignPicker.id) AND assignPickerDetail.isActive = true AND assignPickerDetail.isDeleted = false ";
					

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			
			return query.list();
	    }
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public List<PharmacologyValidationSearchResult> assignPickerSearchBP(PharmacologyValidationSearch pharmacologyValidationSearch, Integer screenId,Integer unitId)
	{
		String hql = "SELECT assignPicker.id as assignPickerId, visitType.visitTypeName as visitType, "
				+ " admission.createdDate as visitDate, prescription.prescriptionDate as prescriptionDate, "
				+ " patient.uhidNumber as uhid, "
				+ " patient.gender.genderId as genderId, "
				+ " patient.imagePath as imagePath, "
				+ "	CONCAT(prefix.prefixCode, ' ', patient.firstName, ' ', coalesce(patient.middleName, ''), ' ', coalesce(patient.lastName, ''))AS patientName, "
				+ " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, patient.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, patient.dob)),'M ') as ageString, "
				+ " gender.genderCode as genderCode, "
				+ " specialityMaster.specialityName as specialityName, "
				+ " priority.priorityDesc as priority, "
				+ " status.id as statusId, status.status as status, "
				+ " wardMaster.wardName as wardName, bedMaster.bedNumber as bedNo, "
				+ "	CONCAT(prefix.prefixCode, ' ', patient.firstName, ' ', coalesce(patient.middleName, ''), ' ', coalesce(patient.lastName, ''), ' | ',patient.uhidNumber, ' | ', gender.genderCode) AS patientDetail, "
				+ " CONCAT(doctor.firstName, ' ', coalesce(doctor.middleName, ''), ' ', coalesce(doctor.lastName, '')) as doctorDetail, "
				+ " patient.id as patientId, patient.dob as patientDob, doctor.id as doctorId, admission.id as admissionId, "
				+ " prescription.id as prescriptionId, assignPicker.id as assignPickerId "
				+ " FROM AssignPicker AS assignPicker "
				+ " INNER JOIN assignPicker.status as status WITH status.isActive = true AND status.isDeleted = false "
				+ " INNER JOIN assignPicker.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false AND prescription.isValidate = true "
				+ " INNER JOIN prescription.visitType as visitType WITH visitType.status = 'A'  "
				+ " LEFT JOIN prescription.specialityMaster as specialityMaster WITH specialityMaster.status = 'A'  "
				+ " INNER JOIN prescription.priority AS priority WITH priority.status = 'A'"
				+ " LEFT JOIN prescription.doctor AS doctor WITH doctor.status = 'A'"
				+ " LEFT JOIN prescription.admission as admission "
				+ " LEFT JOIN admission.patient as patient "
				+ " LEFT JOIN patient.prefix AS prefix "
				+ " LEFT JOIN patient.gender as gender "
				+ " LEFT JOIN admission.admissionDetail as admissionDetail WITH admissionDetail.status = 'A' AND admissionDetail.unitId = :unitId"
				+ " LEFT JOIN admissionDetail.wardMaster as wardMaster "
				+ " LEFT JOIN admissionDetail.bedMaster as bedMaster "
				+ " WHERE prescription.id IN (  SELECT prescription1.id FROM Prescription AS prescription1 INNER JOIN prescription1.prescriptionDetailsList AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
				+ "								INNER JOIN prescriptionDetail.store AS store WITH store.isActive = true AND store.isDeleted = false "
				+ "							    INNER JOIN store.storeType AS storeType WITH storeType.isActive = true AND storeType.isDeleted = false "
				/*+ "						        WHERE  storeType.id != 6 "*/
				+ "							  ) "
				+ " AND assignPicker.isActive = true AND assignPicker.isDeleted = false AND assignPicker.addedDate BETWEEN  :fromDate AND :toDate "	
				+ " AND ( :wardName = '' OR lower(wardMaster.wardName) LIKE :wardName) "
				+ " AND assignPicker.unit.unitId = :unitId AND ( visitType.visitTypeId = :visitTypeId OR :visitTypeId = 0 ) "
				+ " AND visitType.visitTypeCode != 'OP'"
				+ " AND ( status.id = :statusId OR :statusId = 0 )"
				+ " AND ((:doctorName = '' OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ', doctor.lastName))) LIKE :doctorName OR lower(doctor.firstName) LIKE :doctorName) OR (:doctorName = '' OR lower(doctor.middleName) LIKE :doctorName)" 
				+ "	OR (:doctorName = '' OR lower(doctor.lastName) LIKE :doctorName))"
				+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
				+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "		
				+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
				+ "	OR lower(patient.lastName) LIKE :patientName )) "
				+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
				+ "	INNER JOIN screenStatus.status as status) "
				+ " ORDER BY  :orderby ";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(pharmacologyValidationSearch.getFromDate()));
		query.setParameter("toDate", Util.getDate(pharmacologyValidationSearch.getToDate()));
		query.setParameter("statusId", pharmacologyValidationSearch.getStatusId() != null ? pharmacologyValidationSearch.getStatusId() : 0);
		query.setParameter("visitTypeId",pharmacologyValidationSearch.getVisitType() != null ? pharmacologyValidationSearch.getVisitType() : 0);
		query.setParameter("patientId",pharmacologyValidationSearch.getPatientId() != null ? pharmacologyValidationSearch.getPatientId() : 0);
		query.setParameter("patientName",pharmacologyValidationSearch.getPatientName() != null ? pharmacologyValidationSearch.getPatientName().toLowerCase() + '%' : "");
		query.setParameter("doctorName",pharmacologyValidationSearch.getDoctorName()!= null ? pharmacologyValidationSearch.getDoctorName().toLowerCase() + '%' : "");
		query.setParameter("wardName",pharmacologyValidationSearch.getWardName() != null ? pharmacologyValidationSearch.getWardName().toLowerCase() + '%' : "");
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		query.setParameter("screenId", screenId);
		query.setParameter("orderby", "FIELD(priority.priorityDesc, 'High','Normal','Low'), assignPicker.id DESC");
		query.setResultTransformer(Transformers.aliasToBean(PharmacologyValidationSearchResult.class));
		return query.list();
	}

	@Override
	public PharmacologyValidationDetails getAssignPickerBP(Integer id,Integer unitId) 
	{
		String hql = "SELECT assignPicker.id as assignPickerId, prescription.id as prescriptionId, patient.id as patientId, "
				+ " visitType.visitTypeId as visitType, "
				+ " status.id as statusId, status.status as status, "
				+ " patient.uhidNumber as uhid, "
				+ " wardMaster.wardName as wardName, bedMaster.bedNumber as bedNo, specialityMaster.specialityName as specialityName, "
				+ " CONCAT(doctor.firstName, ' | ', doctor.middleName, ' ', doctor.lastName) as doctorDetail, "
				+ "	CONCAT(prefix.prefixCode, ' ', patient.firstName, ' ', coalesce(patient.middleName, ''), ' ', coalesce(patient.lastName, ''), ' | ', gender.genderCode) AS patientDetail, "
				+ " patient.dob as patientDob, patient.dob as dob, doctor.id as doctorId, admission.id as admissionId,  patient.imagePath as imagePath, patient.gender.genderId as genderId,"
				+ " admissionDetail.doa as doa, admissionDetail.pdd as pdd "
				+ " FROM AssignPicker AS assignPicker "
				+ " INNER JOIN assignPicker.prescription AS prescription "
				+ " INNER JOIN assignPicker.status as status "
				+ " INNER JOIN prescription.visitType as visitType "
				+ " LEFT JOIN prescription.doctor AS doctor "
				+ " LEFT JOIN prescription.specialityMaster AS specialityMaster "
				+ " LEFT JOIN prescription.admission as admission "
				+ " LEFT JOIN admission.patient as patient "
				+ " LEFT JOIN patient.prefix AS prefix "
				+ " LEFT JOIN admission.admissionDetail as admissionDetail WITH admissionDetail.status = 'A' AND admissionDetail.unitId = :unitId"
				+ " LEFT JOIN admissionDetail.wardMaster as wardMaster "
				+ " LEFT JOIN admissionDetail.bedMaster as bedMaster "
				+ " LEFT JOIN patient.gender as gender "
				+ " WHERE (:id = assignPicker.id) AND assignPicker.isActive = true AND assignPicker.isDeleted = false ";
				

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(PharmacologyValidationDetails.class));
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		return (PharmacologyValidationDetails) query.uniqueResult();
	}

	@Override
	public List<PharmacologyItem> assignPickerDetailsBP(Integer id) {
		String hql = "SELECT assignPickerDetail.id as assignPickerDetailId, assignPickerDetail.parentAssignPickerId as parentAssignPickerId,  item.id as itemId, item.code as itemCode, "
				+ " 	item.name as itemName, generic.name as genericName,  assignPicker.id as assignPickerId, item.isNarcotic as isNarcotic, "
				+ " 	prescription.isIntervention as interventionFlag, prescriptionDetail.orderQty as quantityOrdered, item.isNarcotic as isNarcotic, "
				+ " 	generic.id as genericId, prescriptionDetail.id as prescriptionDetailId, status.id as statusId, item.formulationType.id as formulationTypeId ,"
				+ "		status.status as status, assignPickerDetail.issueQuantity as quantity, "
				+ "		assignPickerDetail.uomType.id AS uomTypeId,	assignPickerDetail.uomUnit.id AS uomUnitId, " +
                " assignPickerDetail.batch.id as batchId, prescriptionDetail.store.storeId as storeId, assignPickerDetail.pickedQuantity as pickedQuantity ,"
                + "(case " + 
                "		when instruction.id is null  then  prescriptionDetail.instructionDesc  " + 
                "		else instruction.instructionDesc " + 
                "	end) as instructionDesc  "
				+ " FROM AssignPickerDetail AS assignPickerDetail "
				+ " INNER JOIN assignPickerDetail.assignPicker AS assignPicker WITH assignPicker.isActive = true AND assignPicker.isDeleted = false "
				+ " INNER JOIN assignPickerDetail.prescriptionDetail as prescriptionDetail "
				+ " left JOIN prescriptionDetail.instruction as instruction "
				+ " INNER JOIN prescriptionDetail.item as item "
				+ " INNER JOIN item.generic as generic "
				+ " INNER JOIN prescriptionDetail.prescription as prescription "
				+ " INNER JOIN assignPickerDetail.status as status "
				+ " WHERE (:id = assignPicker.id) AND assignPickerDetail.isActive = true AND assignPickerDetail.isDeleted = false"
                + " order by assignPickerDetail.id asc "

				//+ " AND prescriptionDetail.parentSubstituteId is null "
				//+ " AND assignPickerDetail.parentAssignPickerId is null"
				;
				
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(PharmacologyItem.class));
		return query.list();
	}
	
	@Override
	public List<PharmacologyItem> assignPickerAddedItemsDetailsBP(Integer id) {
		String hql = "SELECT issueDetail.id as IssueDetailsId, assignPickerDetail.id as assignPickerDetailId, item.id as itemId, item.code as itemCode, "
				+ " 	item.name as itemName, generic.name as genericName, item.isNarcotic as isNarcotic, "
				+ "     assignPicker.id as assignPickerId ,"
				+ " 	prescription.isIntervention as interventionFlag, prescriptionDetail.orderQty as quantityOrdered, "
				+ " 	generic.id as genericId, prescriptionDetail.id as prescriptionDetailId, status.id as statusId, "
				+ "		status.status as status, assignPickerDetail.issueQuantity as quantity,"
				+ "		prescriptionDetail.parentSubstituteId as parentSubstituteId , "
				+ "		assignPickerDetail.uomType.id AS uomTypeId,	assignPickerDetail.uomUnit.id AS uomUnitId, assignPickerDetail.batch.id as batchId ,"
				+ "(case " + 
                "		when instruction.id is null  then  prescriptionDetail.instructionDesc  " + 
                "		else instruction.instructionDesc " + 
                "	end) as instructionDesc  "
				+ " FROM AssignPickerDetailAddedItems AS assignPickerDetail "
				+ " INNER JOIN assignPickerDetail.assignPicker AS assignPicker WITH assignPicker.isActive = true AND assignPicker.isDeleted = false "
				+ " INNER JOIN assignPickerDetail.prescriptionDetail as prescriptionDetail "
				+ " left JOIN prescriptionDetail.instruction as instruction "
				+ " LEFT JOIN assignPickerDetail.issueDetail issueDetail "
				+ " INNER JOIN prescriptionDetail.item as item "
				+ " INNER JOIN item.generic as generic "
				+ " INNER JOIN prescriptionDetail.prescription as prescription "
				+ " INNER JOIN assignPickerDetail.status as status "
				+ " WHERE (:id = assignPicker.id) AND assignPickerDetail.isActive = true AND assignPickerDetail.isDeleted = false "
                + " order by assignPickerDetail.id asc "

			//	+ " AND prescriptionDetail.parentSubstituteId is null "
				;
				
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setResultTransformer(Transformers.aliasToBean(PharmacologyItem.class));
		return query.list();
	}

	@Override
	public List<PharmacologyItem> getIssuedQuantity(List<Long> prescriptionDetailId) {
		String hql = "SELECT prescriptionDetail.id AS prescriptionDetailId, COALESCE(SUM(issueDetailBatchMapper.issueQuantity), 0) AS issuedQuantity "
				+ " FROM IssueDetail AS issueDetail "
				+ "		INNER JOIN issueDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isDeleted = false AND prescriptionDetail.isActive = true "
				+ "		LEFT JOIN issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isDeleted = false AND issueDetailBatchMapper.isActive = true "
				+ "	WHERE issueDetail.isDeleted = false AND issueDetail.isActive = true "
				+ "     AND prescriptionDetail.id IN (:prescriptionDetailId) "
				+ " GROUP BY prescriptionDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("prescriptionDetailId", prescriptionDetailId);
		query.setResultTransformer(Transformers.aliasToBean(PharmacologyItem.class));

		return query.list();
	}

	@Override
	public List<PharmacologyItem> getPendingIssueQuantity(List<Long> prescriptionDetailId) {
		String hql = "SELECT prescriptionDetail.id AS prescriptionDetailId, COALESCE(SUM(issueDetail.issueQuantity), 0) AS pendingIssueQuantity "
				+ " FROM IssueDetail AS issueDetail "
				+ "		INNER JOIN issueDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isDeleted = false AND prescriptionDetail.isActive = true "
				+ "	WHERE issueDetail.isDeleted = false AND issueDetail.isActive = true "
				+ "     AND prescriptionDetail.id IN (:prescriptionDetailId) AND issueDetail.saleDetail IS NULL"
				+ " GROUP BY prescriptionDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("prescriptionDetailId", prescriptionDetailId);
		query.setResultTransformer(Transformers.aliasToBean(PharmacologyItem.class));

		return query.list();
	}

	@Override
	public List<Issue> getIssuesOfPicker(Integer id) {
		String hql = "SELECT issue from Issue issue "
				+ " INNER JOIN issue.assignPicker picker WITH picker.isDeleted = false AND picker.isActive = true "
				+ " INNER JOIN issue.status status WITH status.isDeleted = false AND status.isActive = true "
				+ " WHERE picker.id = :id AND status.id = :statusId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("pickerId", id);
		query.setParameter("statusId", com.param.common.constants.Status.Pending.getStatus());

		return query.list();
	}

	@Override
	public List<PharmacologyItem> getIssuedQuantity(List<Long> prescriptionDetailId, List<Integer> addedITemIds) {
		if(!prescriptionDetailId.isEmpty() && !addedITemIds.isEmpty()) {
			String hql = "SELECT assignPickerDetailAddedItems.id AS addedItemId, "
					+ " prescriptionDetail.id AS prescriptionDetailId,"
					+ "  COALESCE(SUM(issueDetailBatchMapper.issueQuantity), 0) AS issuedQuantity "
					+ " FROM AssignPickerDetailAddedItems AS assignPickerDetailAddedItems "
					+ "		INNER JOIN assignPickerDetailAddedItems.issueDetail AS issueDetail "
					+ "		INNER JOIN issueDetail.prescriptionDetail AS prescriptionDetail "
					+ " WITH prescriptionDetail.isDeleted = false AND prescriptionDetail.isActive = true "
					+ "		LEFT JOIN issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper"
					+ " WITH issueDetailBatchMapper.isDeleted = false AND issueDetailBatchMapper.isActive = true "
					+ "	WHERE issueDetail.isDeleted = false AND issueDetail.isActive = true "
					+ "     AND prescriptionDetail.id IN (:prescriptionDetailId) "
					+ "		AND assignPickerDetailAddedItems.id in (:addedITemIds) "
					+ " GROUP BY prescriptionDetail.id, assignPickerDetailAddedItems.id ";
	
			Query query = getSession().createQuery(hql);
			query.setParameterList("prescriptionDetailId", prescriptionDetailId);
			query.setParameterList("addedITemIds", addedITemIds);
			query.setResultTransformer(Transformers.aliasToBean(PharmacologyItem.class));
	
			return query.list();
		}
		else
			return null;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public PrescriptionDetailsDto getPrescriptionDtlsByPrescriptionId(Long id, Integer itemId) {
		try
		{
			String sql = "select CAST(presDtl.id as int) as \"prescriptionDetailsid\"," + 
					/*" presDtl.item_id as \"itemId\", " + 
					" itm.name as \"itemName\", " + 
					" preFreDtl.dose as \"doseDesc\", " + 
					" su.unit as \"strengthDesc\", " + 
					" su.id as \"doseUnitId\", " + 
					" presDtl.statflag as \"isStat\" , " + 
					" preFreDtl.frequency_id as \"frequencyId\", " + 
					" freq.description as \"frequencyDesc\", " + 
					" preFreDtl.route_id as \"roaId\", " + */
					" roa.description as \"roa_details\", " + 
					/*" itm.formulation_type_id as \"formulationTypeId\", " + 
					" presDtl.is_medication_stop as \"isMedicationStopHis\", " + 
					" itm.generic_id as \"genericId\", " + 
					" pres.priority_id as \"priorityId\" , " + 
					" presDtl.order_qty as \"quantity\", " + 
					" to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"startDate\"," + 
					" to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\", " + 
					" presDtl.instruction_id as \"instructionId\" ," + 
					" instr.instruction_desc as \"instructionDesc\", " + 
					" gen.name as \"genericName\", " + 
					" pres.encounter_id as \"encounterId\"," + 
					" pres.admission_id as \"admissionId\""*/
//					 " select presDtl.id as \"prescriptionDetailId\"," + 
					 " presDtl.organization_id  as \"organizationId\"," + 
					 " presDtl.unit_id  as \"unitId\"," + 
					 " presDtl.prescription_id  as \"prescriptionId\"," + 
					 " presDtl.item_id as \"itemId\"," + 
					 " presDtl.store_id as \"storeId\"," + 
					 " presDtl.uom_unit_id as \"uomUnitId\"," + 
					 " presDtl.order_qty as \"orderQty\"," + 
//					 " presDtl.pending_qty as \"pendingQty\"," + 
//					 " presDtl.refill_qty as \"refillQty\"," + 
					 " presDtl.strength as \"strength\"," + 
//					 " presDtl.is_discontinued as \"isDiscontinued\"," + 
//					 " presDtl.discontinue_by as \"discontinueBy\"," + 
//					 " presDtl.discontinue_on as \"discontinueOn\"," + 
					" presDtl.prescription_date as \"prescriptionDate\"," + 
					" presDtl.infusion_total_volume as \"infusionTotalVolume\"," + 
					 " presDtl.authorised_by  as \"authorisedBy\"," + 
					 " presDtl.authorised_date as \"authorisedDate\"," + 
					 " presDtl.is_pre_stop as \"isPreStop\"," + 
					 " presDtl.is_intervention as \"isIntervention\"," + 
					 " presDtl.updated_by as \"updatedBy\"," + 
					 " to_char(presDtl.updated_date,'YYYY-MM-DD HH:mm:ss') as \"updatedDate\"," + 
					 " presDtl.added_by as \"addedBy\"," + 
					 " presDtl.added_date as \"addedDate\"," + 
					 " presDtl.is_deleted as \"isDeleted\"," + 
					 " presDtl.is_active as \"isActive\"," + 
					 " presDtl.status_id as \"statusId\"," + 
					 " presDtl.remark as \"remark\"," + 
					 " presDtl.prnflag as \"prnflag\"," + 
					 " presDtl.statflag as \"statflag\"," + 
					 " presDtl.infusionflag as \"infusionflag\"," + 
					 " presDtl.infusion_solution_id as \"infusionSolutionId\"," + 
					 " to_char(presDtl.pre_stop_start_date,'YYYY-MM-DD HH:mm:ss') as \"preStopStartDate\"," + 
					 " to_char(presDtl.pre_stop_end_date,'YYYY-MM-DD HH:mm:ss') as \"preStopEndDate\"," + 
//					 " presDtl.consumable_method_id as \"consumableMethodId\"," + 
//					 " to_char(presDtl.prn_start_date,'YYYY-MM-DD HH:mm:ss') as \"prnStartDate\"," + 
//					 " presDtl.uom_type_id as \"uomTypeId\"," + 
					" presDtl.instruction_id as \"instructionId\"," + 
					" presDtl.instruction_desc as \"instructionDesc\"," + 
//					 " presDtl.revised_ordered_quantity as \"revisedOrderedQuantity\"," + 
//					 " presDtl.is_vaccine as \"isVaccine\"," + 
//					 " presDtl.is_discharge_medication as \"isDischargeMedication\"," + 
					 " presDtl.is_non_formulary as \"isNonFormulary\"," + 
					 " presDtl.generic_name as \"genericName\"," + 
					 " presDtl.item_name as \"itemName\"," + 
					 " presDtl.justification_stop as \"justificationStop\"," + 
					 " presDtl.is_medication_stop as \"isMedicationStop\"," + 
//					 " presDtl.dis_presribed_qty as \"disPresribedQty\"," + 
					 " presDtl.parent_substitute_id as \"parentSubstituteId\"," + 
//					 " preFreDtl.prescription_detail_id as \"prescriptionDetailId\"," + 
					 " preFreDtl.dose as \"dose\"," + 
					 " preFreDtl.dose_unit_id as \"doseUnitId\"," + 
					 " preFreDtl.frequency_id as \"frequencyId\"," + 
					 " preFreDtl.route_id as \"routeId\"," + 
					 " preFreDtl.start_date as \"startDate\"," + 
					 " preFreDtl.stop_date as \"stopDate\"," + 
					 " preFreDtl.instructions as \"instructions\","
					 + " itm.is_narcotic as \"isNarcotic\" " + 
//					 " preFreDtl.remark as \"remark\"," + 
//					 " preFreDtl.is_active as \"isActive\"" + 
					" from pharmacy.t_prescription_detail presDtl" + 
					" INNER JOIN pharmacy.t_prescription pres on pres.id=presDtl.prescription_id" + 
					" INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id " + 
					" LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " + 
					" INNER JOIN public.m_item itm on itm.id=presDtl.item_id " + 
					" left JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id " + 
					" INNER JOIN public.m_generic gen on itm.generic_id = gen.id " + 
					" INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id " + 
					" LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id " + 
					" where presdtl.prescription_id=:id "
					+ " and presdtl.item_id=:itemId ";
					

			Query query = getSession().createSQLQuery(sql).addScalar("prescriptionDetailsid").addScalar("roa_details")
					.addScalar("organizationId").addScalar("unitId")
					.addScalar("itemId").addScalar("storeId").addScalar("uomUnitId")
					.addScalar("orderQty").addScalar("strength").addScalar("prescriptionId")
					.addScalar("prescriptionDate")
					.addScalar("authorisedBy").addScalar("authorisedDate").addScalar("isDeleted", new TrueFalseType()).addScalar("isActive", new TrueFalseType())
					.addScalar("statusId").addScalar("remark").addScalar("prnflag", new TrueFalseType()).addScalar("statflag", new TrueFalseType())
					.addScalar("infusionflag", new TrueFalseType()).addScalar("infusionSolutionId").addScalar("infusionTotalVolume")
					.addScalar("instructionId")
					.addScalar("isNonFormulary").addScalar("genericName").addScalar("itemName").addScalar("justificationStop")
					.addScalar("isMedicationStop").addScalar("parentSubstituteId").addScalar("dose").addScalar("doseUnitId")
					.addScalar("frequencyId").addScalar("routeId").addScalar("startDate").addScalar("stopDate")
					.addScalar("instructions").addScalar("instructionDesc").addScalar("isNarcotic", new TrueFalseType());
			query.setParameter("id", id);
			query.setParameter("itemId", itemId);
			query.setMaxResults(1);
			query.setResultTransformer(Transformers.aliasToBean(PrescriptionDetailsDto.class));
			return (PrescriptionDetailsDto) query.uniqueResult();
	    }
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public AssignPickerDetailItem getAssignPickerDetailItemByPrescriptionId(Integer id, Integer prescriptionDetailsid) {
		try {
			String sql = "select  " + 
					" distinct(assignDtl.assign_picker_id) as \"assignPickerId\", " + 
					" assignDtl.prescription_detail_id  as \"prescriptionDetailid\", " + 
					" assignDtl.id  as \"id\", " + 
					" assignDtl.uom_type_id as \"uomTypeId\", " + 
					" assignDtl.uom_unit_id as \"uomUnitId\", " + 
					" assignDtl.issue_quantity as \"quanityIssued\", " + 
					" assignDtl.is_active as \"isActive\", " + 
					" assignDtl.is_deleted as \"isDeleted\", " + 
					" assignDtl.status_id as \"statusId\", " + 
					" assignDtl.picked_quantity as \"pickedQuantity\", " + 
					" assignDtl.issue_detail_id as \"issueDetailId\", " + 
					" assignDtl.parent_assign_picker_id as \"parentAssignPickerId\" " + 
					" from pharmacy.t_assign_picker_detail assignDtl " + 
					" left join pharmacy.t_assign_picker assignPick on assignPick.id=assignDtl.assign_picker_id " + 
					" left join pharmacy.t_assign_picker_detail_added_items assignPickAdded on assignDtl.prescription_detail_id=assignPickAdded.prescription_detail_id" + 
					" where assignDtl.assign_picker_id=:id "
					+ " and assignDtl.prescription_detail_id=:prescriptionDetailsid ";
					

			Query query = getSession().createSQLQuery(sql).addScalar("assignPickerId").addScalar("prescriptionDetailid").addScalar("id")
					.addScalar("uomTypeId").addScalar("uomUnitId").addScalar("quanityIssued")
					.addScalar("isActive", new TrueFalseType()).addScalar("isDeleted", new TrueFalseType())
					.addScalar("statusId")
					.addScalar("pickedQuantity").addScalar("issueDetailId")
					.addScalar("parentAssignPickerId");
			query.setParameter("id", id);
			query.setParameter("prescriptionDetailsid", prescriptionDetailsid);
			query.setResultTransformer(Transformers.aliasToBean(AssignPickerDetailItem.class));
			return (AssignPickerDetailItem) query.uniqueResult();
	    }
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public AssignPicker updateassignPicker(AssignPicker assignPicker) throws ApplicationException {
		try {
		getSession().update(assignPicker);
		return assignPicker;
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public AssignPicker getAssignPickerList(Integer id) throws ApplicationException {
		try {
		String hql = "SELECT assignPicker FROM AssignPicker AS assignPicker "
				+ " INNER JOIN assignPicker.prescription prescription "
				+ " WHERE (:id = prescription.id) AND assignPicker.isActive = true AND assignPicker.isDeleted = false "
				+ " AND prescription.isActive = true AND prescription.isDeleted = false ";
				

		Query query = getSession().createQuery(hql);
		query.setParameter("id", id.longValue());
		
		return (AssignPicker) query.uniqueResult();
		 }
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		}

	@Override
	public List<PharmacologyItem> getIssuedQty(Long id) throws ApplicationException {
		try {
			String sql = "select issDetails.batch_id  as \"tempBatchId\","
					+ " issDetails.id as  \"IssueDetailsId\", "
					+ " issDetails.prescription_detail_id as \"parentIssueDetailsId\", "
					+ " issDetails.assign_picker_detail_id as \"assignPickerDetailId\", "
					+ " issDetails.issue_quantity \"quantityIssued\" " + 
					" from pharmacy.t_issue_detail  issDetails " + 
					" inner join pharmacy.t_prescription_detail predetails  on issDetails.prescription_detail_id = predetails.id " + 
					" where predetails.prescription_id = :id ";

			Query query = getSession().createSQLQuery(sql).addScalar("tempBatchId").addScalar("IssueDetailsId").addScalar("parentIssueDetailsId").addScalar("assignPickerDetailId").addScalar("quantityIssued");
			query.setParameter("id", id);
			query.setResultTransformer(Transformers.aliasToBean(PharmacologyItem.class));
			return query.list();
	    }
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

}
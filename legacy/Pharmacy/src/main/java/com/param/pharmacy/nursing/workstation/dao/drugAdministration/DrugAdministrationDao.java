package com.param.pharmacy.nursing.workstation.dao.drugAdministration;

import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.DrugAdministration;
import com.param.entity.model.pharmacy.DrugAdministrationDetail;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.Prescription;
import com.param.global.common.ServiceException;
import com.param.global.utility.Util;
import com.param.pharmacy.nursing.workstation.dto.drugAdministration.DoseTime;
import com.param.pharmacy.nursing.workstation.dto.drugAdministration.DoseTime1;
import com.param.pharmacy.nursing.workstation.dto.drugAdministration.DrugAdministrationItem;
import com.param.pharmacy.nursing.workstation.dto.drugAdministration.DrugAdministrationRequest;

@Repository("drugAdministrationDao")
public class DrugAdministrationDao extends AbstractDao<Integer, Issue> implements IDrugAdministrationDao {

	@Autowired
	private SessionService sessionService;

	@Autowired
	SessionFactory sessionFactory;

	private static final Logger logger = Logger.getLogger(DrugAdministrationDao.class);

	@Override
	public List<DrugAdministrationItem> patientIssueReturnSearch(DrugAdministrationRequest request)
	{
		try
		{
			String hql = " SELECT prescriptionDetail.id AS id, prescriptionDetail.addedDate AS prescriptionDate, "
					+ " item.id AS itemId, item.code AS itemCode, item.name AS itemName, routeOfAdministration.description AS consumptionMethod, "
					+ " frequency.description AS frequency, frequency.id AS frequencyId, frequency.noOfAdministrationTime AS noOfAdministration, PrescriptionFrequencyDetails.dose AS dose, PrescriptionFrequencyDetails.startDate AS startDate, "
					+ " PrescriptionFrequencyDetails.stopDate AS stopDate, "
					+ " prescriptionDetail.isPreStop AS isPreStop, prescriptionDetail.preStopStartDate AS preStopStartDate, prescriptionDetail.preStopEndDate AS preStopEndDate, "
					+ "	prescriptionDetail.prnFlag AS prnFlag, prescriptionDetail.statFlag AS statFlag, prescriptionDetail.infusionFlag AS infusionFlag, "
 					+ " prescriptionDetail.prnStartDate AS prnStartDate, prescriptionDetail.prnInstruction AS prnInstruction"
 					+ " FROM PrescriptionDetail AS prescriptionDetail "
					+ " LEFT JOIN prescriptionDetail.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
					+ " LEFT JOIN prescriptionDetail.PrescriptionFrequencyDetails AS PrescriptionFrequencyDetails WITH PrescriptionFrequencyDetails.isActive = true AND PrescriptionFrequencyDetails.isDeleted = false "
					+ " LEFT JOIN PrescriptionFrequencyDetails.routeOfAdministration AS routeOfAdministration WITH routeOfAdministration.isActive = true AND routeOfAdministration.isDeleted = false "
					+ " LEFT JOIN PrescriptionFrequencyDetails.frequency AS frequency WITH frequency.isActive = true AND frequency.isDeleted = false "
					+ " LEFT JOIN prescriptionDetail.item AS item WITH item.isActive = true AND item.isDeleted = false "
					+ " LEFT JOIN prescriptionDetail.issueDetail AS issueDetail WITH issueDetail.isActive = true AND issueDetail.isDeleted = false "
					+ " LEFT JOIN issueDetail.saleDetail AS saleDetail WITH saleDetail.isActive = true AND saleDetail.isDeleted = false "
					+ " INNER JOIN issueDetail.issue AS issue WITH issue.isActive = true AND issue.isDeleted = false "
					+ " INNER JOIN issue.status AS status WITH status.isActive = true AND status.isDeleted = false AND status.id = :statusId"
					+ " LEFT JOIN saleDetail.sale AS sale WITH sale.isActive = true AND sale.isDeleted = false AND sale.isHandover = true "
					+ " LEFT JOIN prescriptionDetail.consumableMethod AS consumableMethod WITH consumableMethod.isActive = true AND consumableMethod.isDeleted = false "
					+ " LEFT JOIN prescription.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN prescription.encounter AS encounter WITH encounter.status = 'A' "
					+ " WHERE ( frequency.id IN (SELECT frequency FROM Frequency AS frequency "
					+ " LEFT JOIN frequency.frequencyDetailList AS frequencyDetailList WITH frequencyDetailList.isActive = true AND frequencyDetailList.isDeleted = false WHERE frequency.isActive = true AND frequency.isDeleted = false ) ) "
					+ "	AND PrescriptionFrequencyDetails.startDate <= :date AND PrescriptionFrequencyDetails.stopDate >= :date AND prescriptionDetail.isDiscontinued = false AND prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false AND prescriptionDetail.unit.unitId = :unitId "
					+ " AND ( :admissionId = 0 OR admission.admissionId = :admissionId ) "
					+ " AND ( :encounterId = 0 OR encounter.encounterId = :encounterId ) ORDER BY prescriptionDetail.id DESC ";
					
			Query query = getSession().createQuery(hql);
			query.setParameter("date",Util.getDate(request.getDate()));
			query.setParameter("admissionId",request.getAdmissionId() != null ? request.getAdmissionId() : 0);
			query.setParameter("encounterId",request.getEncounterId() != null ? request.getEncounterId() : 0);
			query.setParameter("statusId",com.param.common.constants.Status.Closed.getStatus());
			query.setParameter("unitId", sessionService.getUnitId());
			query.setResultTransformer(Transformers.aliasToBean(DrugAdministrationItem.class));
			
		return query.list();
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public List<DoseTime1> getDrugAdministrationItem(DrugAdministrationRequest request)
	{
		try
		{
			

/*			String hql = " SELECT frequencyDetail.id AS id, frequencyDetail.administrationTime AS administrationTime,"
					+ " frequencyDetail.preBufferAdministrationTime AS preBufferAdministrationTime, frequencyDetail.postBufferAdministrationTime AS postBufferAdministrationTime, "
					+ " drugAdministrationDetail.administrationTime AS actualAdministrationTime, reason.reasonMasterId AS reasonId ,reason.reasonDesc AS reason, drugAdministrationDetail.note AS note "
					+ " FROM PrescriptionFrequencyDetail AS prescriptionFrequencyDetail "
					+ " INNER JOIN prescriptionFrequencyDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false AND prescriptionDetail.unit.unitId = :unitId "
					+ "	INNER JOIN prescriptionDetail.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false AND prescription.unit.unitId = :unitId "
					+ " INNER JOIN prescriptionFrequencyDetail.frequency AS frequency WITH frequency.isActive = true AND frequency.isDeleted = false AND frequency.unit.unitId = :unitId "
					+ " INNER JOIN frequency.frequencyDetailList AS frequencyDetail WITH frequencyDetail.isActive = true AND frequencyDetail.isDeleted = false AND frequencyDetail.unit.unitId = :unitId "
					+ " LEFT JOIN prescriptionDetail.drugAdministrationDetail AS drugAdministrationDetail WITH drugAdministrationDetail.isActive = true AND drugAdministrationDetail.isDeleted = false AND drugAdministrationDetail.unit.unitId = :unitId AND drugAdministrationDetail.addedDate BETWEEN :date AND :toDate "
					+ " LEFT JOIN drugAdministrationDetail.reason AS reason WITH reason.status = 'A' "
					+ " WHERE prescriptionDetail.id = :id ORDER BY frequencyDetail.id DESC ";
					
			Query query = getSession().createQuery(hql);
			query.setParameter("id",request.getId());
			query.setParameter("date",Util.getDate(request.getDate()));
			query.setParameter("toDate",Util.getDate(request.getToDate()));
			query.setParameter("unitId", sessionService.getUnitId());
			query.setResultTransformer(Transformers.aliasToBean(DoseTime.class));*/
			
			
			String SQL ="select" + 
					" freqDtl.id as \"id\"," +
				    " to_char(freqDtl.administration_time,'HH24:MI') as \"administrationTime\", presDtl.order_qty as \"orderQty\", " +
					" to_char(freqDtl.pre_buffer_administration_time,'HH24:MI') as \"preBufferAdministrationTime\", " +
					" to_char(freqDtl.post_buffer_administration_time,'HH24:MI') as \"postBufferAdministrationTime\", " +
					" to_char(drugadmin.administration_time,'HH24:MI') as \"actualAdministrationTime\", " +
					" rsnMst.reason_master_id as \"reasonId\", " +
					" rsnMst.reason_desc as \"reason\", " +
					" drugadmin.note as \"note\", "
					+ "  drugadmin.batch_id as \"batchId\" ," + 
					" bth.batch_no as \"batchNo\" " + 
					" from " + 
					"	pharmacy.t_prescription_frequency_detail presFreqDtl" + 
					" inner join pharmacy.t_prescription_detail presDtl on" + 
					"	presFreqDtl.prescription_detail_id = presDtl.id" + 
					"	and(" + 
					"		presDtl.is_active = true" + 
					"		and presDtl.is_deleted = false" + 
					"		and presDtl.unit_id  = :unitId " + 
					"	)" + 
					" inner join pharmacy.t_prescription pres on" + 
					"	presDtl.prescription_id = pres.id" + 
					"	and(" + 
					"		pres.is_active = true" + 
					"		and pres.is_deleted = false" + 
					"		and pres.unit_id  = :unitId " + 
					"	)" + 
					" inner join public.m_frequency freq on" + 
					"	presFreqDtl.frequency_id = freq.id" + 
					"	and(" + 
					"		freq.is_active = true" + 
					"		and freq.is_deleted = false" + 
					"		and freq.unit_id  = :unitId " + 
					"	)" + 
					" inner join m_frequency_detail freqDtl on" + 
					"	freq.id = freqDtl.frequency_id" + 
					"	and(" + 
					"		freqDtl.is_active = true" + 
					"		and freqDtl.is_deleted = false" + 
					"		and freqDtl.unit_id  = :unitId " + 
					"	)" + 
					" left outer join pharmacy.t_drug_administration_detail drugadmin on" + 
					"	(freqDtl.id = drugadmin.frequency_detail_id and presDtl.id = drugadmin.prescription_detail_id) " + 
					"	and(" + 
					"		drugadmin.is_active = true" + 
					"		and drugadmin.is_deleted = false" + 
					"		and drugadmin.unit_id  = :unitId " + 
					"		and(" + 
					"			drugadmin.added_date between :date and :toDate " + 
					"		)" + 
					"	)" + 
					" left outer join adt.m_reason_master rsnMst on" + 
					"	drugadmin.reason_id = rsnMst.reason_master_id" + 
					"	and(" + 
					"		rsnMst.status = 'A'" + 
					"	)"
					+ " left join inventory.t_batch bth on " + 
					"	bth.id = drugadmin.batch_id" + 
					" where" + 
					"	presDtl.id = :id " + 
					" order by" + 
					"	freqDtl.id , drugadmin.administration_time desc";
			Query query = getSession().createSQLQuery(SQL)
					.addScalar("id")
					.addScalar("administrationTime")
					.addScalar("preBufferAdministrationTime") 
					.addScalar("postBufferAdministrationTime")
					.addScalar("actualAdministrationTime")
					.addScalar("reasonId")
			        .addScalar("reason")
			        .addScalar("batchId")
			        .addScalar("batchNo")
			        .addScalar("orderQty")
			        .addScalar("note");
			query.setParameter("id",request.getId());
			query.setParameter("date",Util.getDate(request.getDate()));
			query.setParameter("toDate",Util.getDate(request.getToDate()));
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
			query.setResultTransformer(Transformers.aliasToBean(DoseTime1.class));
			
		return query.list();

		}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}

	}

	@Override
	public List<Issue> getPatientDetails(DrugAdministrationRequest request)
	{
		try
		{
			String hql = " SELECT issue FROM Issue AS issue "
					+ " LEFT JOIN issue.doctor AS doctor WITH doctor.status = 'A' "
					//+ " LEFT JOIN issue.specialityMaster AS doctor WITH specialityMaster.status = 'A' "
					+ " LEFT JOIN issue.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A' "//above line changed by minesh for resolved HIS-9053
					+ " LEFT JOIN issue.sale AS sale WITH sale.isActive = true AND sale.isDeleted = false AND sale.isHandover = true "
					+ " LEFT JOIN issue.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
					+ " LEFT JOIN issue.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN issue.encounter AS encounter WITH encounter.status = 'A' "
					+ " WHERE issue.isActive = true AND issue.isDeleted = false "
					+ " AND ( :admissionId = 0 OR admission.admissionId = :admissionId ) "
					+ " AND ( :encounterId = 0 OR encounter.encounterId = :encounterId ) ";
			
			Query query = getSession().createQuery(hql);
			query.setParameter("admissionId",request.getAdmissionId() != null ? request.getAdmissionId() : 0);
			query.setParameter("encounterId",request.getEncounterId() != null ? request.getEncounterId() : 0);
			//query.setParameter("unitId", sessionService.getUnitId());
			
			return query.list();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public DrugAdministration drugAdministrationSave(DrugAdministration request)
	{
		getSession().save(request);
		return request;
	}
	
	@Override
	public DrugAdministrationDetail drugAdministrationDetail(Long prescriptionDetailId, Integer frequencyDetailId)
	{
		try
		{
			String hql = " SELECT drugAdministrationDetail FROM DrugAdministrationDetail AS drugAdministrationDetail "
					+ " INNER JOIN drugAdministrationDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false "
					+ " INNER JOIN drugAdministrationDetail.frequencyDetail AS frequencyDetail WITH frequencyDetail.isActive = true AND frequencyDetail.isDeleted = false "
					+ " WHERE drugAdministrationDetail.isActive = true AND drugAdministrationDetail.isDeleted = false AND drugAdministrationDetail.unit.unitId = :unitId "
					+ " AND ( prescriptionDetail.id = :prescriptionDetailId ) "
					+ " AND ( frequencyDetail.id = :frequencyDetailId ) ";
			
			Query query = getSession().createQuery(hql);
			query.setParameter("prescriptionDetailId",prescriptionDetailId);
			query.setParameter("frequencyDetailId",frequencyDetailId);
			query.setParameter("unitId", sessionService.getUnitId());
			
			return (DrugAdministrationDetail) query.uniqueResult();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public DrugAdministration getDrugAdministration(Integer id)
	{
		try
		{
			String hql = " SELECT drugAdministration FROM DrugAdministration AS drugAdministration "
					+ " WHERE drugAdministration.isActive = true AND drugAdministration.isDeleted = false AND drugAdministration.unit.unitId = :unitId "
					+ " AND ( drugAdministration.id = :id ) ";
			
			Query query = getSession().createQuery(hql);
			query.setParameter("id",id);
			query.setParameter("unitId", sessionService.getUnitId());
			
			return (DrugAdministration) query.uniqueResult();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public DrugAdministrationDetail getDrugAdministrationDetail(Integer id, Integer frequencyDetailId)
	{
		try
		{
			String hql = " SELECT drugAdministrationDetail FROM DrugAdministrationDetail AS drugAdministrationDetail "
					+ " INNER JOIN drugAdministrationDetail.frequencyDetail AS frequencyDetail WITH frequencyDetail.isActive = true AND frequencyDetail.isDeleted = false "
					+ " WHERE drugAdministrationDetail.isActive = true AND drugAdministrationDetail.isDeleted = false AND drugAdministrationDetail.unit.unitId = :unitId "
					+ " AND ( drugAdministrationDetail.id = :id ) AND ( frequencyDetail.id = :frequencyDetailId ) ";
			
			Query query = getSession().createQuery(hql);
			query.setParameter("id",id);
			query.setParameter("frequencyDetailId",frequencyDetailId);
			query.setParameter("unitId", sessionService.getUnitId());
			
			return (DrugAdministrationDetail) query.uniqueResult();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public DrugAdministrationDetail getDrugAdministrationDetailById(Integer id)
	{
		try
		{
			String hql = " SELECT drugAdministrationDetail FROM DrugAdministrationDetail AS drugAdministrationDetail "
					+ " WHERE drugAdministrationDetail.isActive = true AND drugAdministrationDetail.isDeleted = false AND drugAdministrationDetail.unit.unitId = :unitId "
					+ " AND ( drugAdministrationDetail.id = :id ) ";
			
			Query query = getSession().createQuery(hql);
			query.setParameter("id",id);
			query.setParameter("unitId", sessionService.getUnitId());
			
			return (DrugAdministrationDetail) query.uniqueResult();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public 	DrugAdministrationDetail getDrugAdministrationDetailByDrugId(Integer drugId)
	{
		try
		{
			String hql = " SELECT drugAdministrationDetail FROM DrugAdministrationDetail AS drugAdministrationDetail "
					+ " INNER JOIN drugAdministrationDetail.drugAdministration AS drugAdministration WITH drugAdministration.isActive = true AND drugAdministration.isDeleted = false "
					+ " WHERE drugAdministrationDetail.isActive = true AND drugAdministrationDetail.isDeleted = false AND drugAdministrationDetail.unit.unitId = :unitId "
					+ " AND ( drugAdministration.id = :id ) ";
			
			Query query = getSession().createQuery(hql);
			query.setParameter("id",drugId);
			query.setParameter("unitId", sessionService.getUnitId());
			
			return (DrugAdministrationDetail) query.uniqueResult();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public DrugAdministrationDetail drugAdministrationDetailUpdate(DrugAdministrationDetail request)
	{
		getSession().update(request);
		return request;
	}

	@Override
	public List<Prescription> getPatientDetailsPrescription(DrugAdministrationRequest request) {
		String hql = " SELECT prescription FROM Prescription AS prescription "
				+ " LEFT JOIN prescription.doctor AS doctor WITH doctor.status = 'A' "
				+ " LEFT JOIN prescription.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A' "
				//+ " LEFT JOIN issue.sale AS sale WITH sale.isActive = true AND sale.isDeleted = false AND sale.isHandover = true "
				//+ " LEFT JOIN issue.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
				+ " LEFT JOIN prescription.admission AS admission WITH admission.status = 'A' "
				+ " LEFT JOIN prescription.encounter AS encounter WITH encounter.status = 'A' "
				+ " INNER JOIN prescription.prescriptionDetailsList AS prescripotionDetails WITH prescripotionDetails.isActive = true and prescripotionDetails.isDeleted = false and prescripotionDetails.statFlag = true "
				+ " WHERE prescription.isActive = true AND prescription.isDeleted = false AND prescription.unit.unitId = :unitId "
				+ " AND ( :admissionId = 0 OR admission.admissionId = :admissionId ) "
				+ " AND ( :encounterId = 0 OR encounter.encounterId = :encounterId ) ";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("admissionId",request.getAdmissionId() != null ? request.getAdmissionId() : 0);
		query.setParameter("encounterId",request.getEncounterId() != null ? request.getEncounterId() : 0);
		query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
		
		return query.list();
	}

	@Override
	public List<DrugAdministrationItem> patientIssueReturnSearchStatMedication(DrugAdministrationRequest request) {
		
			String hql = " SELECT distinct(prescriptionDetail.id) AS id, prescription.id as prescriptionId , prescriptionDetail.addedDate AS prescriptionDate, "
					+ " item.id AS itemId, item.code AS itemCode, item.name AS itemName, routeOfAdministration.description AS consumptionMethod, "
					+ " frequency.description AS frequency, frequency.id AS frequencyId, frequency.noOfAdministrationTime AS noOfAdministration, PrescriptionFrequencyDetails.dose AS dose, PrescriptionFrequencyDetails.startDate AS startDate, "
					+ " PrescriptionFrequencyDetails.stopDate AS stopDate, to_char(PrescriptionFrequencyDetails.startDate, 'HH:mm:ss') as startTime, to_char(PrescriptionFrequencyDetails.stopDate, 'HH:mm:ss') as stopTime,"
					+ " prescriptionDetail.isPreStop AS isPreStop, prescriptionDetail.preStopStartDate AS preStopStartDate, prescriptionDetail.preStopEndDate AS preStopEndDate, "
					+ "	prescriptionDetail.prnFlag AS prnFlag, prescriptionDetail.statFlag AS statFlag, prescriptionDetail.infusionFlag AS infusionFlag,prescriptionDetail.orderQty AS orderQty, "
 					+ " prescriptionDetail.prnStartDate AS prnStartDate, prescriptionDetail.prnInstruction AS prnInstruction, prescriptionDetail.isMedicationStop as isMedicationStop, "
 					+ " pharmacologicalCla.antibioticTest as antibioticTest ,saleDetail.id as  saleDetailsId  "
 					+ " FROM PrescriptionDetail AS prescriptionDetail "
					+ " LEFT JOIN prescriptionDetail.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
					+ " LEFT JOIN prescriptionDetail.issueDetail AS issueDetail WITH issueDetail.isActive = true AND issueDetail.isDeleted = false "
					+ " LEFT JOIN issueDetail.saleDetail as saleDetail With saleDetail.isActive = true AND saleDetail.isDeleted = false  "
					+ " LEFT JOIN prescriptionDetail.PrescriptionFrequencyDetails AS PrescriptionFrequencyDetails WITH PrescriptionFrequencyDetails.isActive = true AND PrescriptionFrequencyDetails.isDeleted = false "
					+ " LEFT JOIN PrescriptionFrequencyDetails.routeOfAdministration AS routeOfAdministration WITH routeOfAdministration.isActive = true AND routeOfAdministration.isDeleted = false "
					+ " LEFT JOIN PrescriptionFrequencyDetails.frequency AS frequency WITH frequency.isActive = true AND frequency.isDeleted = false "
					+ " LEFT JOIN prescriptionDetail.item AS item WITH item.isActive = true AND item.isDeleted = false "
					+ " Left Join item.pharmacologicalClassification  AS pharmacologicalCla with pharmacologicalCla.isActive = true AND pharmacologicalCla.isDeleted = false "
					+ " LEFT JOIN prescription.sales AS sale WITH sale.isActive = true AND sale.isDeleted = false AND sale.isHandover = true "
					+ " LEFT JOIN prescriptionDetail.consumableMethod AS consumableMethod WITH consumableMethod.isActive = true AND consumableMethod.isDeleted = false "
					+ " LEFT JOIN prescription.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN prescription.encounter AS encounter WITH encounter.status = 'A' "
					+ " WHERE ( frequency.id IN (SELECT frequency FROM Frequency AS frequency "
					+ " LEFT JOIN frequency.frequencyDetailList AS frequencyDetailList WITH frequencyDetailList.isActive = true AND frequencyDetailList.isDeleted = false WHERE frequency.isActive = true AND frequency.isDeleted = false ) ) "
					+ "	AND PrescriptionFrequencyDetails.startDate <= :date AND PrescriptionFrequencyDetails.stopDate >= :date AND prescriptionDetail.isDiscontinued = false AND prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false AND prescriptionDetail.unit.unitId = :unitId "
					+ " AND ( :admissionId = 0 OR admission.admissionId = :admissionId ) "
					+ " AND ( :encounterId = 0 OR encounter.encounterId = :encounterId )"
					+ " AND prescriptionDetail.statFlag = true "
					+ " AND prescriptionDetail.isNonFormulary = 'N'"
					+ " ORDER BY prescriptionDetail.id DESC ";
					
			Query query = getSession().createQuery(hql);
			query.setParameter("date",Util.getDate(request.getDate()));
			query.setParameter("admissionId",request.getAdmissionId() != null ? request.getAdmissionId() : 0);
			query.setParameter("encounterId",request.getEncounterId() != null ? request.getEncounterId() : 0);
			query.setParameter("unitId", request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
			query.setResultTransformer(Transformers.aliasToBean(DrugAdministrationItem.class));
			
		return query.list();
	}

	@Override
	public List<DrugAdministrationItem> patientIssueReturnForDrugAdministration(DrugAdministrationRequest request) {
		try
		{
			String hql = " SELECT distinct(prescriptionDetail.id) AS id, prescription.id as prescriptionId, prescriptionDetail.addedDate AS prescriptionDate,issue.infusionTolalVolume as infusionTolalVolume, issueDetail.isSubstitute as isSubstitute, prescriptionDetail.parentSubstituteId as parentSubstituteId, "
					+ " item.id AS itemId, item.code AS itemCode, item.name AS itemName, routeOfAdministration.description AS consumptionMethod, "
					+ " frequency.description AS frequency, frequency.id AS frequencyId, frequency.noOfAdministrationTime AS noOfAdministration, PrescriptionFrequencyDetails.dose AS dose, PrescriptionFrequencyDetails.startDate AS startDate, "
					+ " PrescriptionFrequencyDetails.stopDate AS stopDate, to_char(PrescriptionFrequencyDetails.startDate, 'HH24:MI') as startTime, to_char(PrescriptionFrequencyDetails.stopDate, 'HH24:MI') as stopTime,"
					+ " prescriptionDetail.isPreStop AS isPreStop, prescriptionDetail.preStopStartDate AS preStopStartDate, prescriptionDetail.preStopEndDate AS preStopEndDate, "
					+ "	prescriptionDetail.prnFlag AS prnFlag, prescriptionDetail.statFlag AS statFlag, prescriptionDetail.infusionFlag AS infusionFlag, prescriptionDetail.orderQty AS orderQty, "
 					+ " prescriptionDetail.infusionSolutionId AS infusionSolutionId, prescriptionDetail.prnStartDate AS prnStartDate, prescriptionDetail.prnInstruction AS prnInstruction, prescriptionDetail.isMedicationStop as isMedicationStop,"
 					+ " pharmacologicalCla.antibioticTest as antibioticTest ,saleDetail.id as  saleDetailsId "
 					+ " FROM PrescriptionDetail AS prescriptionDetail "
					+ " LEFT JOIN prescriptionDetail.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
					+ " LEFT JOIN prescriptionDetail.issueDetail AS issueDetail WITH issueDetail.isActive = true AND issueDetail.isDeleted = false "
					+ " LEFT JOIN issueDetail.saleDetail as saleDetail With saleDetail.isActive = true AND saleDetail.isDeleted = false  "
					+ " LEFT JOIN issueDetail.issue AS issue WITH issue.isActive = true AND issue.isDeleted = false "
					+ " LEFT JOIN prescriptionDetail.PrescriptionFrequencyDetails AS PrescriptionFrequencyDetails WITH PrescriptionFrequencyDetails.isActive = true AND PrescriptionFrequencyDetails.isDeleted = false "
					+ " LEFT JOIN PrescriptionFrequencyDetails.routeOfAdministration AS routeOfAdministration WITH routeOfAdministration.isActive = true AND routeOfAdministration.isDeleted = false "
					+ " LEFT JOIN PrescriptionFrequencyDetails.frequency AS frequency WITH frequency.isActive = true AND frequency.isDeleted = false "
					+ " LEFT JOIN prescriptionDetail.item AS item WITH item.isActive = true AND item.isDeleted = false "
					+ " Left Join item.pharmacologicalClassification  AS pharmacologicalCla with pharmacologicalCla.isActive = true AND pharmacologicalCla.isDeleted = false "
					+ " LEFT JOIN prescription.sales AS sale WITH sale.isActive = true AND sale.isDeleted = false AND sale.isHandover = true "
					+ " LEFT JOIN prescriptionDetail.consumableMethod AS consumableMethod WITH consumableMethod.isActive = true AND consumableMethod.isDeleted = false "
					+ " LEFT JOIN prescription.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN prescription.encounter AS encounter WITH encounter.status = 'A' "
					+ " WHERE ( frequency.id IN (SELECT frequency FROM Frequency AS frequency "
					+ " LEFT JOIN frequency.frequencyDetailList AS frequencyDetailList WITH frequencyDetailList.isActive = true AND frequencyDetailList.isDeleted = false WHERE frequency.isActive = true AND frequency.isDeleted = false ) ) "
					+ "	AND to_char(PrescriptionFrequencyDetails.startDate, 'YYYY-MM-DD') <= :date"
					+ " AND to_char(PrescriptionFrequencyDetails.stopDate, 'YYYY-MM-DD') >= :date "
					+ " AND prescriptionDetail.isDiscontinued = false AND prescriptionDetail.isActive = true AND prescriptionDetail.isDeleted = false AND prescriptionDetail.unit.unitId = :unitId "
					+ " AND ( :admissionId = 0 OR admission.admissionId = :admissionId ) "
					+ " AND ( :encounterId = 0 OR encounter.encounterId = :encounterId )"
					+ " AND issueDetail.issueQuantity is not null "
					+ " AND prescriptionDetail.statFlag = false "
					+ " AND prescriptionDetail.isAdditive = false  "
					+ " ORDER BY prescriptionDetail.id DESC";					
			Query query = getSession().createQuery(hql);
			query.setParameter("date",request.getDateString());
			query.setParameter("admissionId",request.getAdmissionId() != null ? request.getAdmissionId() : 0);
			query.setParameter("encounterId",request.getEncounterId() != null ? request.getEncounterId() : 0);
			query.setParameter("unitId",request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
			query.setResultTransformer(Transformers.aliasToBean(DrugAdministrationItem.class));
			
		return query.list();
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public DrugAdministrationItem checkReturnQuantity(DrugAdministrationRequest request) {
		String SQL ="select sum(tpird.accept_qty) as \"acceptQty\" "
				+ " from pharmacy.t_patient_issue_return_detail tpird "
				+ " join pharmacy.t_issue_detail_batch_mapper tidbm "
				+ " on tpird.issue_batch_mapper_id =tidbm.id "
				+ " join pharmacy.t_issue_detail tid "
				+ " on tidbm.issue_detail_id =tid.id "
				+ " where tid.prescription_detail_id =:id ";
		Query query = getSession().createSQLQuery(SQL);
		query.setParameter("id",request.getId());
		query.setResultTransformer(Transformers.aliasToBean(DrugAdministrationItem.class));
		
	return (DrugAdministrationItem) query.uniqueResult();
	}

	@Override
	public DrugAdministrationItem checkRejectedIssueQuantity(DrugAdministrationRequest request) {
		String SQL ="select sum(tidbm.rejected_quantity) as \"rejectedQuantity\" "
				+ " from pharmacy.t_issue_detail_batch_mapper tidbm "
				+ " join pharmacy.t_issue_detail tid "
				+ " on tidbm.issue_detail_id = tid .id "
				+ " where tid.prescription_detail_id =:id ";
		Query query = getSession().createSQLQuery(SQL);
		query.setParameter("id",request.getId());
		query.setResultTransformer(Transformers.aliasToBean(DrugAdministrationItem.class));
		
	return  (DrugAdministrationItem) query.uniqueResult();
	}

	@Override
	public DrugAdministrationItem checkReturnedIssueQuantity(DrugAdministrationRequest request) {
		String SQL =/*"select sum(tidbm.return_qty) as \"returnQuantity\" "
				+ " from pharmacy.t_issue_detail_batch_mapper tidbm "
				+ " join pharmacy.t_issue_detail tid "
				+ " on tidbm.issue_detail_id = tid .id "*/
				"select  " + 
				"	sum(tpird.return_qty) as \"returnQuantity\"  " + 
				"from  " + 
				"	pharmacy.t_patient_issue_return_detail tpird  " + 
				"join pharmacy.t_issue_detail_batch_mapper tidbm on  " + 
				"	tpird.issue_batch_mapper_id = tidbm.id  " + 
				"join pharmacy.t_issue_detail tid on  " + 
				"	tidbm.issue_detail_id = tid.id"
				+ " where tid.prescription_detail_id =:id ";
		Query query = getSession().createSQLQuery(SQL);
		query.setParameter("id",request.getId());
		query.setResultTransformer(Transformers.aliasToBean(DrugAdministrationItem.class));
		
	return  (DrugAdministrationItem) query.uniqueResult();
	}
}
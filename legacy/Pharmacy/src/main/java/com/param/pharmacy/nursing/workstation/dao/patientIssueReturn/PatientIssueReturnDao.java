package com.param.pharmacy.nursing.workstation.dao.patientIssueReturn;

import java.util.List;

import com.param.service.global.SessionService;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.constants.Status;
import com.param.common.dao.AbstractDao;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.PatientIssueReturn;
import com.param.entity.model.pharmacy.PatientIssueReturnDetail;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.CommonDto;
import com.param.global.dto.PatientDetailsDto;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dto.returnAcceptance.PharmacySaleReturnDto;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceResponse;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceSave;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientAgainstItemResponse;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnAddItem;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnRequest;

@Repository("patientIssueReturnDao")
public class PatientIssueReturnDao extends AbstractDao<Integer, PatientIssueReturn> implements IPatientIssueReturnDao, ICommonConstants {

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private SessionService sessionService;

	private static final Logger logger = Logger.getLogger(PatientIssueReturnDao.class);

	@Override
	public List<PatientIssueReturn> patientIssueReturnSearch(PatientIssueReturnRequest patientIssueReturnRequest,
	Integer screenId) {
	try {
	String hql = " SELECT patientIssueReturn FROM PatientIssueReturn AS patientIssueReturn "
	// + " INNER JOIN patientIssueReturn.patientIssueReturnDetail AS
	// patientIssueReturnDetail WITH patientIssueReturnDetail.isDeleted = false "
	+ " INNER JOIN patientIssueReturn.patient AS patient WITH patient.status = 'A' "
	+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
	+ " INNER JOIN patientIssueReturn.visitType AS visitType WITH visitType.status = 'A' "
	+ " INNER JOIN patientIssueReturn.store AS store WITH store.isActive = true AND store.isDeleted = false "
	+ " INNER JOIN patientIssueReturn.status AS status WITH status.isActive = true AND status.isDeleted = false "
	+ " LEFT JOIN patientIssueReturn.admission AS admission WITH admission.status = 'A' "
	+ " LEFT JOIN patientIssueReturn.encounter AS encounter WITH encounter.status = 'A' "
	+ " LEFT JOIN patientIssueReturn.wardMaster AS wardMaster WITH wardMaster.status = 'A' "
	+ " LEFT JOIN patientIssueReturn.bedMaster AS bedMaster WITH bedMaster.status = 'A' "
	+ " WHERE patientIssueReturn.unit.unitId = :unitId AND patientIssueReturn.isActive = true AND patientIssueReturn.isDeleted = false and patientIssueReturn.fromMedication=true "
	+ " AND patientIssueReturn.addedDate BETWEEN :fromDate AND :toDate "
	+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
	+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
	+ " AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
	+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
	+ " OR lower(patient.lastName) LIKE :patientName )) "
	+ " AND (:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid )"
//	+ " AND ( wardMaster.wardId in ( :wardId )) "
	+ " AND( :returnNo = '' OR lower(patientIssueReturn.returnNo) LIKE :returnNo) "
	// + " AND (:statusId = 0 OR status.id = :statusId) "
	// + " AND ((case when (:statusId = 0) then (status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
	// + " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId "
	// + " INNER JOIN screenStatus.status as status)) else (status.id = :statusId) end)) "
	// + " ORDER BY patientIssueReturn.id DESC "
	;

	StringBuffer subQuery=new StringBuffer();
	int id=patientIssueReturnRequest.getStatusId() != null ? patientIssueReturnRequest.getStatusId(): 0;
	
	if(id != 0){
		subQuery.append(" AND status.id =:statusId ");
	}
	else{
		subQuery.append(" AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId INNER JOIN screenStatus.status as status)");
	}
	if(patientIssueReturnRequest.getWardList()!=null && patientIssueReturnRequest.getWardList().size()>0) {
		
		subQuery.append(" AND wardMaster.wardId in (:wardIds)");
		
	}else {
		subQuery.append(" AND wardMaster.wardId in (0)");
	}
	subQuery.append(" ORDER BY patientIssueReturn.id DESC");
	String hql1 = hql.concat(subQuery.toString());
	Query query = getSession().createQuery(hql1);
	query.setParameter("fromDate", Util.getDate(patientIssueReturnRequest.getFromDate()));
	query.setParameter("toDate", Util.getDate(patientIssueReturnRequest.getToDate()));
	query.setParameter("visitType",
	patientIssueReturnRequest.getVisitType() != null ? patientIssueReturnRequest.getVisitType() : 0);
	query.setParameter("patientId",
	patientIssueReturnRequest.getPatientId() != null ? patientIssueReturnRequest.getPatientId() : 0);
	query.setParameter("patientName",
	patientIssueReturnRequest.getPatientName() != null
	? patientIssueReturnRequest.getPatientName().toLowerCase() + '%'
	: "");
	query.setParameter("uhid",
	patientIssueReturnRequest.getUhid() != null
	? patientIssueReturnRequest.getUhid().toLowerCase() + '%'
	: "");
	query.setParameter("returnNo",
	patientIssueReturnRequest.getReturnNo() != null
	? patientIssueReturnRequest.getReturnNo().toLowerCase() + '%'
	: "");
		if (id != 0) {
			query.setParameter("statusId", patientIssueReturnRequest.getStatusId());
		}
		if (id == 0) {
			query.setParameter("screenId", screenId);
		}
		if (patientIssueReturnRequest.getWardList() != null && patientIssueReturnRequest.getWardList().size() > 0) {
			query.setParameterList("wardIds", patientIssueReturnRequest.getWardList());
		}
//	query.setParameterList("wardId", patientIssueReturnRequest.getWardList());
	// query.setParameter("screenId", screenId);
	// query.setParameter("statusId",
	// patientIssueReturnRequest.getStatusId() != null ? patientIssueReturnRequest.getStatusId() : 0);
	query.setParameter("unitId",
	patientIssueReturnRequest.getUnitId() != null ? patientIssueReturnRequest.getUnitId()
	: sessionService.getUnitId());



	return query.list();
	} catch (Exception e) {
	logger.error(e.getMessage(), e);
	throw new ServiceException(e.getMessage());
	}

	}

	@Override
	public List<PatientIssueReturnRequest> patientAgainstIssueSearch(PatientIssueReturnRequest patientIssueReturnRequest) {
		try {

			/*String hql = " SELECT issue FROM Issue AS issue "
					+ " INNER JOIN issue.visitType AS visitType WITH visitType.status = 'A' "
					+ " INNER JOIN issue.status AS status  WITH status.isActive = true AND status.isDeleted = false  "
					+ " INNER  JOIN issue.store AS store WITH store.isActive = true AND store.isDeleted = false "
                    + " INNER JOIN issue.sale AS sale WITH sale.isActive = true AND sale.isDeleted = false"
					// + " INNER JOIN issue.toStore AS toStore WITH toStore.isActive = true AND
					// toStore.isDeleted = false "
					+ " LEFT JOIN issue.admission AS admission WITH admission.status = 'A' "
					+ " LEFT JOIN admission.patient AS patient WITH patient.status = 'A' "
					+ " LEFT JOIN issue.encounter AS encounter WITH encounter.status = 'A' "
					+ " LEFT JOIN encounter.patient AS patientEncounter WITH patientEncounter.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter WITH prefixEncounter.status = 'A' "
					+ " WHERE issue.unit.unitId = :unitId AND issue.isActive = true AND issue.isDeleted = false  " // and issue.isReturn=false
					+ " AND issue.updatedDate BETWEEN :fromDate AND :toDate  " // add HIS-10721 removed HIS-10553
																				// //HIS-11055
					+ " AND ( visitType.visitTypeId = :visitType OR :visitType = 0 ) "
					//+ " AND issue.status.id= 8  "
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
					// + " AND ( toStore.storeId = :tostoreId OR :tostoreId = 0 ) "
					+ " AND( :issueNo = '' OR lower(issue.issueNo) LIKE :issueNo) "
					+ " AND( (select COALESCE(sum(issueDetailBatchMapper.acceptedQuantity),0) from IssueDetail AS issueDetail " + 
					"inner join issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper  " + 
					"where issueDetail.issue.id = issue.id) " + 
					"> " + 
					"(select COALESCE(sum(patientIssueReturnDetail.returnQuantity),0) from IssueDetail AS issueDetail " + 
					"inner join issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper  " + 
					"inner join issueDetailBatchMapper.patientIssueReturnDetail AS patientIssueReturnDetail  " + 
					"where issueDetail.issue.id = issue.id) ) "
					+ " ORDER BY issue.id DESC ";
			
			Query query = getSession().createQuery(hql);*/
			
			Integer visitType = patientIssueReturnRequest.getVisitType() != null ? patientIssueReturnRequest.getVisitType() : 0;
			Integer patientId = patientIssueReturnRequest.getPatientId() != null ? patientIssueReturnRequest.getPatientId() : 0;
			String  patientName = patientIssueReturnRequest.getPatientName() != null ? patientIssueReturnRequest.getPatientName().toLowerCase() : "";
		    String  uhid = patientIssueReturnRequest.getUhid() != null ?  patientIssueReturnRequest.getUhid().toLowerCase() : "";
		    String  visitNo = patientIssueReturnRequest.getVisitNo() != null ? patientIssueReturnRequest.getVisitNo().toLowerCase() : "";
		    Integer  storeId = patientIssueReturnRequest.getStoreId() != null ? patientIssueReturnRequest.getStoreId() : 0;
		    String  issueNo = patientIssueReturnRequest.getIssueNo() != null ?  patientIssueReturnRequest.getIssueNo().toLowerCase() : "";
		    Integer unitId = patientIssueReturnRequest.getUnitId()!=null ? patientIssueReturnRequest.getUnitId() : sessionService.getUnitId();
			
			List<PatientIssueReturnRequest> PatientIssueReturnRequestList = null;
			String sql = " select  " +
					"	issue0_.id as \"id\", " +
					"	issue0_.is_active as \"isActive\", " +
					"	issue0_.is_deleted as \"isDeleted\", " +
					"	issue0_.organization_id as \"organizationId\", " +
					"	issue0_.unit_id as \"unitId\", " +
					"	issue0_.admission_id as \"admissionId\", " +
					"	issue0_.assign_picker_id as \"assignPickerId\", " +
					"	issue0_.carrier_id as \"carrierId\", " +
					"	issue0_.discount as \"discount\", " +
					"	issue0_.doctor_id as \"doctorId\", " +
					"	issue0_.encounter_id as \"encounterId\", " +
					"	issue0_.implant_indent_id as \"implantIndentId\", " +
					"	issue0_.is_consignment as \"isConsignment\", " +
					"	issue0_.is_implant as \"isImplant\", " +
					"	issue0_.is_narcotics as \"isNarcotics\", " +
					"	issue0_.is_return as \"isReturn\", " +
					"	ts.bill_no as \"issueNo\", " +
					"	issue0_.net_amount as \"netAmount\", " +
					"	issue0_.ongoing as \"ongoing\", " +
					"	issue0_.outstanding_amount as \"outstandingAmount\", " +
					"	issue0_.paid_amount as \"paidAmount\", " +
					"	issue0_.patient_indent_id as \"patientIndentId\", " +
					"	issue0_.prescription_id as \"prescriptionId\", " +
					"	issue0_.remark as \"remark\", " +
					"	issue0_.round_off as \"roundOff\", " +
					"	cast (ts.id as int4) as \"saleId\", " +
					"	issue0_.doc_spl_id as \"docSplId\", " +
					"	issue0_.status_id as \"statusId\", " +
					"	issue0_.store_id as \"storeId\", " +
					"	issue0_.tax_amount as \"taxAmount\", " +
					"	issue0_.to_store_id as \"toStoreId\", " +
					"	issue0_.total_amount as \"totalAmount\", " +
					"	issue0_.total_credit_payable as \"totalCreditPayable\", " +
					"	issue0_.total_discount_amount as \"totalDiscountAmount\", " +
					"	issue0_.total_self_payable as \"totalSelfPayable\", " +
					"	issue0_.visit_type_id as \"visitTypeId\","
					+ "  store3_.store_name as \"storeName\", "+
					"  coalesce( case when issue0_.admission_id > 0 then patientreg6_.dob when issue0_.encounter_id > 0 then patientreg8_.dob end ) as \"patientDob\", " +
					"  coalesce( case when issue0_.admission_id > 0 then patientreg6_.uhid_number when issue0_.encounter_id > 0 then patientreg8_.uhid_number end ) as \"uhidNumber\", " +
					"  coalesce( case when issue0_.admission_id > 0 then gender.gender_code when issue0_.encounter_id > 0 then genderenc.gender_code end ) as \"genderCode\", " +
					"  issue0_.added_date as \"createdDate\", "+
					" 	coalesce( case when issue0_.admission_id > 0 then admission5_.admission_number when issue0_.encounter_id > 0 then encounter7_.encounter_number end ) as \"admissionNumber\", " +
					"  concat(prefix9_.prefix_code, '. ',patientreg6_.first_name,' ', patientreg6_.middle_name, ' ',patientreg6_.last_name ) as \"patientDetails\", "+
					"  concat(prefix9_.prefix_code, '. ',patientreg8_.first_name,' ', patientreg8_.middle_name, ' ',patientreg8_.last_name ) as \"patientDetailsEnc\" "+
					" from " +
					"	pharmacy.t_issue issue0_"
					+ " inner join  pharmacy.t_sale ts on ts.issue_id = issue0_.id " +
					" inner join public.m_visit_type_master visittype1_ on " +
					"	issue0_.visit_type_id = visittype1_.visit_type_id " +
					"	and (visittype1_.status = 'A') " +
					" inner join public.m_status status2_ on " +
					"	issue0_.status_id = status2_.id " +
					"	and (status2_.is_active = true " +
					"		and status2_.is_deleted = false )" +
					" inner join public.m_store store3_ on" +
					"	issue0_.store_id = store3_.store_id" +
					"	and (store3_.is_active = true " +
					"		and store3_.is_deleted = false )" +
					" inner join pharmacy.t_sale sale4_ on" +
					"	issue0_.sale_id = sale4_.id" +
					"	and (sale4_.is_active = true " +
					"		and sale4_.is_deleted = false and sale4_.sale_type_id=1) " +
					" left outer join billing.m_billing_master billingMaster on billingMaster.billing_master_id=sale4_.bill_id "+
					" left outer join adt.t_admission admission5_ on " +
					"	issue0_.admission_id = admission5_.admission_id " +
					"	and (admission5_.status = 'A' )" +
					" left outer join patient.t_patient_registration patientreg6_ on" +
					"	admission5_.patient_id = patientreg6_.patient_id" +
					"	and (patientreg6_.status = 'A' )" +
					" left outer join public.m_prefix_master prefix9_ on" +
					"	patientreg6_.prefix_id = prefix9_.prefix_id" +
					"	and (prefix9_.status = 'A')" +
					" left outer join opd.t_encounter_master encounter7_ on" +
					"	issue0_.encounter_id = encounter7_.encounter_id" +
					"	and (encounter7_.status ='A' )" +
					" left outer join patient.t_patient_registration patientreg8_ on" +
					"	encounter7_.patient_id = patientreg8_.patient_id" +
					"	and (patientreg8_.status = 'A')" +
					" left outer join public.m_prefix_master prefix10_ on" +
					"	patientreg8_.prefix_id = prefix10_.prefix_id " +
					"	and (prefix10_.status = 'A')"
					+ " left join public.m_gender_master gender on"
					+ " patientreg6_.gender_id = gender.gender_id "
					+ " left join public.m_gender_master genderenc on"
					+ " patientreg8_.gender_id = gender.gender_id " +
                    " left join ( " +
                    "   select idet.issue_id " +
                    "   from pharmacy.t_issue_detail idet " +
                    "   left join pharmacy.t_issue_detail_batch_mapper idbm ON " +
                    "       idet.id = idbm.issue_detail_id " +
                    "       left join pharmacy.t_patient_issue_return_detail pir ON " +
                    "           idbm.id = pir.issue_batch_mapper_id " +
                    "       GROUP BY idet.issue_id " +
                    "       HAVING COALESCE(SUM(idbm.accepted_quantity), 0) > COALESCE(SUM(pir.return_qty), 0) " +
                    "       ) agg ON agg.issue_id = issue0_.id " +
//					" left  join " +
//					" ( " +
//					"	select" +
//					"		coalesce(sum(issuedetai12_.accepted_quantity), 0) as A , issuedetai11_.issue_id" +
//					"	from" +
//					"		pharmacy.t_issue_detail issuedetai11_" +
//					"	inner join pharmacy.t_issue_detail_batch_mapper issuedetai12_ on" +
//					"		issuedetai11_.id = issuedetai12_.issue_detail_id" +
//					"		group by issuedetai11_.issue_id " +
//					"		) abc on abc.issue_id = issue0_.id" +
//					"		" +
//					" left  join " +
//					"		(" +
//					"	select" +
//					"		coalesce(sum(patientiss15_.return_qty), 0) as B , issuedetai13_.issue_id" +
//					"	from" +
//					"		pharmacy.t_issue_detail issuedetai13_" +
//					"	inner join pharmacy.t_issue_detail_batch_mapper issuedetai14_ on" +
//					"		issuedetai13_.id = issuedetai14_.issue_detail_id" +
//					"	inner join pharmacy.t_patient_issue_return_detail patientiss15_ on" +
//					"		issuedetai14_.id = patientiss15_.issue_batch_mapper_id " +
//					"		group by issuedetai13_.issue_id " +
//					"		) xyz on xyz.issue_id = issue0_.id" +
//					"	" +
					" where " +
					"	  issue0_.unit_id = :unitId " +
					"	and issue0_.is_active = true " +
					"	and issue0_.is_deleted = false " +
					"   and (to_char(issue0_.updated_date,'YYYY-MM-DD') between  :fromDate and :toDate) " +
					"   and (visittype1_.visit_type_id = :visitType or :visitType = 0) " +
					"   and (:uhid='' or lower(patientreg6_.uhid_number) like :uhid " +
					"       or lower(patientreg8_.uhid_number) like  :uhid) " +
					"   and (:patientId = 0 or patientreg6_.patient_id = :patientId " +
					"       or patientreg8_.patient_id = :patientId ) " +
					"   and ((:patientId <> 0 " +
					"       and :patientName <> '' " +
					"       or :patientName = '' " +
					"       or lower(COALESCE(CONCAT(prefix9_.prefix_code ,' ', patientreg6_.first_name ,' ', patientreg6_.middle_name ,' ', patientreg6_.last_name))) LIKE :patientName " +
					"       or lower(prefix9_.prefix_code) like :patientName " +
					"       or lower(patientreg6_.first_name) like  :patientName " +
					"       or lower(patientreg6_.middle_name) like  :patientName " +
					"       or lower(patientreg6_.last_name) like  :patientName " +
					"       or lower(prefix10_.prefix_code) like  :patientName " +
					"       or lower(patientreg8_.first_name) like  :patientName " +
					"       or lower(patientreg8_.middle_name) like  :patientName " +
					"       or lower(patientreg8_.last_name) like :patientName)) " +
					"   and (:visitNo='' or lower(admission5_.admission_number) like :visitNo " +
					"       or lower(encounter7_.encounter_number) like :visitNo) " +
					"   and (store3_.store_id = :storeId or :storeId = 0) " +
					"   and (:issueNo='' or lower(ts.bill_no) like :issueNo) "
//					+ "	and coalesce(abc.A,0) > coalesce(xyz.B,0) "
					+ " and (sale4_.bill_id is null or (sale4_.bill_id is not null and billingMaster.bill_cancelled='Y')) " +
					"	 order by issue0_.id DESC ";


			Query query = getSession().createSQLQuery(sql);


			query.setParameter("unitId", unitId);
			query.setParameter("fromDate", GlobalCommonDateUtils.getStringDate(patientIssueReturnRequest.getFromDate(), "yyyy-MM-dd"));
			query.setParameter("toDate", GlobalCommonDateUtils.getStringDate(patientIssueReturnRequest.getToDate(), "yyyy-MM-dd"));
			query.setParameter("visitType", visitType);
			query.setParameter("patientId", patientId);
			query.setParameter("uhid", "%"+uhid+"%");
//			query.setParameter("patientName", patientIssueReturnRequest.getPatientName().toLowerCase());
			query.setParameter("patientName", "%"+patientName+"%");
			query.setParameter("visitNo", "%"+visitNo+"%");
			query.setParameter("storeId", storeId);
			query.setParameter("issueNo", "%"+issueNo+"%");
			// query.setParameter("statusId", Status.Closed.getStatus());
			query.setResultTransformer(Transformers.aliasToBean(PatientIssueReturnRequest.class)).list();
			PatientIssueReturnRequestList = query.list();
			return PatientIssueReturnRequestList;
//			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<PatientAgainstItemResponse> patientAgainstItemSearch(
			PatientIssueReturnRequest patientIssueReturnRequest) {
		try {
			
			String sql=" select " + 
					"	issue.id as \"id\", " + 
					"	sale.bill_no as \"issueNo\", " + 
					"	issue.added_date as \"issueDate\", " + 
					"	item.id as \"itemId\", " + 
					"	item.name as \"itemName\", " + 
					"	item.code as \"itemCode\", " + 
					"	generic.name as \"genericName\", " + 
					"	uomUnit.unit as \"uomUnit\", " + 
					"	issueDetailBatchMapper.issue_quantity as \"quantityIssued\", " + 
					"	batch.batch_no as \"batchNo\", " + 
					"	batch.expiry_date as \"expiry\", " + 
					"	issueDetailBatchMapper.accepted_quantity as \"returnQuantity\", " + 
					"	issueDetailBatchMapper.return_accepted_quantity as \"returnAcceptedQuantity\", " + 
					"	issueDetailBatchMapper.id as \"batchMapperId\", " + 
					" cast(issueDetailBatchMapper.return_rejected_quantity as int4) as \"returnRejectedQuantity\", "+
					" tempVal.return_qty as \"returnIssuedQty\" "+
					"from " + 
					"	pharmacy.t_issue issue " + 
					"inner join pharmacy.t_issue_detail issueDetail on " + 
					"	issue.id = issueDetail.issue_id " + 
					"	and (issueDetail.is_active = true " + 
					"		and issueDetail.is_deleted = false) "
					+ " inner join pharmacy.t_sale_detail tsd  on tsd.id  = issueDetail.sale_detail_id  " + 
					"inner join pharmacy.t_issue_detail_batch_mapper issueDetailBatchMapper on " + 
					"	issueDetail.id = issueDetailBatchMapper.issue_detail_id " + 
					"	and (issueDetailBatchMapper.is_active = true " + 
					"		and issueDetailBatchMapper.is_deleted = false) " + 
					"left join ( " + 
					"	select " + 
					"		coalesce(cast(sum(returnDetail1.return_qty) as int4), 0) as return_qty,issueDetail1.issue_id as issueId " + 
					"	from " + 
					"		pharmacy.t_issue_detail issueDetail1 " + 
					"	inner join pharmacy.t_issue_detail_batch_mapper issuedetailbatchmapper1 on " + 
					"		issueDetail1.id = issuedetailbatchmapper1.issue_detail_id " + 
					"	inner join pharmacy.t_patient_issue_return_detail returnDetail1 on " + 
					"		issuedetailbatchmapper1.id = returnDetail1.issue_batch_mapper_id " + 
					"	group by issueDetail1.issue_id) as tempVal on tempVal.issueId=issue.id " + 
					"	 " + 
					"inner join inventory.t_batch batch on " + 
					"	issueDetailBatchMapper.batch_id = batch.id " + 
					"	and (batch.is_active = true " + 
					"		and batch.is_deleted = false) " + 
					"inner join public.m_uom_unit uomUnit on " + 
					"	batch.uom_unit_id = uomUnit.id " + 
					"	and (uomUnit.is_active = true " + 
					"		and uomUnit.is_deleted = false) " + 
					"inner join public.m_item item on " + 
					"	batch.item_id = item.id " + 
					"	and (item.is_active = true " + 
					"		and item.is_deleted = false) " + 
					"inner join public.m_generic generic on " + 
					"	item.generic_id = generic.id " + 
					"	and (generic.is_deleted = false " + 
					"		and generic.is_active = true) " + 
					"inner join pharmacy.t_sale sale on " + 
					"	tsd.sale_id = sale.id " + 
					"	and (sale.is_active = true " + 
					"		and sale.is_deleted = false " + 
					"		and sale.sale_type_id = 1) " + 
					"left outer join billing.m_billing_master billingMaster on " + 
					"	sale.bill_id = billingMaster.billing_master_id " + 
					"inner join public.m_visit_type_master visittype8_ on " + 
					"	issue.visit_type_id = visittype8_.visit_type_id " + 
					"	and (visittype8_.status = 'A') " + 
					"inner join public.m_store store on " + 
					"	issue.store_id = store.store_id " + 
					"	and (store.is_active = true " +
					"		and store.is_deleted = false) " + 
					"left outer join public.m_store toStore on " + 
					"	issue.to_store_id = toStore.store_id " + 
					"	and (toStore.is_active = true " + 
					"		and toStore.is_deleted = false) " + 
					"left outer join adt.t_admission admission on " + 
					"	issue.admission_id = admission.admission_id " + 
					"	and (admission.status = 'A') " + 
					"left outer join patient.t_patient_registration patientRegistration on " + 
					"	admission.patient_id = patientRegistration.patient_id " + 
					"	and (patientRegistration.status = 'A') " + 
					"left outer join public.m_prefix_master prefix15_ on " + 
					"	patientRegistration.prefix_id = prefix15_.prefix_id " + 
					"	and (prefix15_.status = 'A') " + 
					"left outer join opd.t_encounter_master encounter on " + 
					"	issue.encounter_id = encounter.encounter_id " + 
					"	and (encounter.status = 'A') " + 
					"left outer join patient.t_patient_registration patientreg14_ on " + 
					"	encounter.patient_id = patientreg14_.patient_id " + 
					"	and (patientreg14_.status = 'A') " + 
					"left outer join public.m_prefix_master prefix on " + 
					"	patientreg14_.prefix_id = prefix.prefix_id " + 
					"	and (prefix.status = 'A') " + 
					"where " + 
					"	(issueDetail.patient_indent_detail_id is null) " + 
					"	and issue.unit_id =:unitId " + 
					"	and issue.is_active = true " + 
					"	and issue.is_deleted = false " + 
					"	and (issue.added_date between :fromDate and :toDate) " + 
					"	and (visittype8_.visit_type_id =:visitType " + 
					"		or :visitType= 0) " + 
					"	and (:uhid= '' " + 
					"		or lower(patientRegistration.uhid_number) like :uhid " + 
					"			or lower(patientreg14_.uhid_number) like :uhid) " + 
					"	and (:patientId= 0 " + 
					"		or patientRegistration.patient_id =:patientId " + 
					"		or patientreg14_.patient_id =:patientId) " + 
					"	and (:patientId!= 0 " + 
					"		and :patientName!= '' " + 
					"		or :patientName= '' " + 
					"		or lower(coalesce((prefix15_.prefix_code || ' ' || patientRegistration.first_name || ' ' || patientRegistration.middle_name || ' ' || patientRegistration.last_name))) like :patientName " + 
					"			or lower(coalesce((prefix.prefix_code || ' ' || patientreg14_.first_name || ' ' || patientreg14_.middle_name || ' ' || patientreg14_.last_name))) like :patientName " + 
					"				or lower(prefix15_.prefix_code) like :patientName " + 
					"					or lower(patientRegistration.first_name) like :patientName " + 
					"						or lower(patientRegistration.middle_name) like :patientName " + 
					"							or lower(patientRegistration.last_name) like :patientName " + 
					"								or lower(prefix.prefix_code) like :patientName " + 
					"									or lower(patientreg14_.first_name) like :patientName " + 
					"										or lower(patientreg14_.middle_name) like :patientName " + 
					"											or lower(patientreg14_.last_name) like :patientName) " + 
					"	and (:visitNo= '' " + 
					"		or lower(admission.admission_number) like :visitNo " + 
					"			or lower(encounter.encounter_number) like :visitNo) " + 
					"	and (store.store_id =:storeId " + 
					"		or :storeId= 0) " + 
					"	and (toStore.store_id =:tostoreId " + 
					"		or :tostoreId= 0) " + 
					"	and (:issueNo= '' " + 
					"		or lower(sale.bill_no) like :issueNo) " + 
					"	and (:batchNo= '' " + 
					"		or lower(batch.batch_no) like :batchNo) " + 
					"	and (:itemId= 0 " + 
					"		or item.id =:itemId) " + 
					"	and (:itemId!= 0 " + 
					"		and :itemCode!= '' " + 
					"		or :itemCode= '' " + 
					"		or lower(item.code) like lower(:itemCode)) " + 
					"	and (:itemId!= 0 " + 
					"		and :itemName!= '' " + 
					"		or :itemName= '' " + 
					"		or lower(item.name) like lower(:itemName)) " + 
					"	and (sale.bill_id is null " + 
					"		or (sale.bill_id is not null) " + 
					"			and billingMaster.bill_cancelled = 'Y') " + 
					" order by " + 
					"	issue.id desc";
			Query query = getSession().createSQLQuery(sql);
			query.setParameter("fromDate", Util.getDate(patientIssueReturnRequest.getFromDate()));
			query.setParameter("toDate", Util.getDate(patientIssueReturnRequest.getToDate()));
			query.setParameter("visitType",
					patientIssueReturnRequest.getVisitType() != null ? patientIssueReturnRequest.getVisitType() : 0);
			query.setParameter("patientId",
					patientIssueReturnRequest.getPatientId() != null ? patientIssueReturnRequest.getPatientId() : 0);
			query.setParameter("patientName",
					patientIssueReturnRequest.getPatientName() != null
							? patientIssueReturnRequest.getPatientName().toLowerCase() + '%'
							: "");
			query.setParameter("uhid",
					patientIssueReturnRequest.getUhid() != null
							? patientIssueReturnRequest.getUhid().toLowerCase() + '%'
							: "");
			query.setParameter("visitNo",
					patientIssueReturnRequest.getVisitNo() != null
							? patientIssueReturnRequest.getVisitNo().toLowerCase() + '%'
							: "");
			query.setParameter("itemId",
					patientIssueReturnRequest.getItemId() != null ? patientIssueReturnRequest.getItemId() : 0);
			query.setParameter("itemName",
					patientIssueReturnRequest.getItemName() != null
							? patientIssueReturnRequest.getItemName().toLowerCase() + '%'
							: "");
			query.setParameter("itemCode",
					patientIssueReturnRequest.getItemCode() != null
							? patientIssueReturnRequest.getItemCode().toLowerCase() + '%'
							: "");
			query.setParameter("batchNo",
					patientIssueReturnRequest.getBatchNo() != null
							? patientIssueReturnRequest.getBatchNo().toLowerCase() + '%'
							: "");
			query.setParameter("storeId",
					patientIssueReturnRequest.getStoreId() != null ? patientIssueReturnRequest.getStoreId() : 0);
			query.setParameter("tostoreId",
					patientIssueReturnRequest.getToStoreId() != null ? patientIssueReturnRequest.getToStoreId() : 0);
			query.setParameter("issueNo",
					patientIssueReturnRequest.getIssueNo() != null
							? patientIssueReturnRequest.getIssueNo().toLowerCase() + '%'
							: "");
			query.setParameter("unitId",patientIssueReturnRequest.getUnitId()!=null ? patientIssueReturnRequest.getUnitId():sessionService.getUnitId());

			query.setResultTransformer(Transformers.aliasToBean(PatientAgainstItemResponse.class));
			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public List<Issue> patientIssueReturnAddItemByIssueId(PatientIssueReturnAddItem patientIssueReturnAddItem) {
		try {
			String hql = " SELECT issue FROM Issue AS issue "
					+ " WHERE issue.isActive = true AND issue.isDeleted = false "
					+ " AND issue.unit.unitId = :unitId AND issue.id IN (:issueId) ";
			Query query = getSession().createQuery(hql);

			query.setParameterList("issueId", patientIssueReturnAddItem.getIssueIds());
			query.setParameter("unitId", patientIssueReturnAddItem.getUnitId()!=null ? patientIssueReturnAddItem.getUnitId() : sessionService.getUnitId());

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public List<Issue> patientIssueReturnAddItemByItemId(PatientIssueReturnAddItem patientIssueReturnAddItem) {
		try {
			String hql = " SELECT issue FROM Issue AS issue "
					+ " INNER JOIN issue.issueDetail AS issueDetail WITH issueDetail.isActive = true AND issueDetail.isDeleted = false "
					+ " INNER JOIN issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false AND batch.isReturn = false "
					+ " INNER JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
					+ " WHERE issue.isActive = true AND issue.isDeleted = false and issueDetailBatchMapper.acceptedQuantity >0 AND  (issueDetailBatchMapper.acceptedQuantity!=issueDetailBatchMapper.returnAcceptedQuantity) "
					+ " AND issue.unit.unitId = :unitId AND item.id IN (:itemId) " + " AND issue.id IN (:issueId) AND issueDetailBatchMapper.id in (:batchId)  ";

			Query query = getSession().createQuery(hql);
			query.setParameterList("itemId", patientIssueReturnAddItem.getItemIds());
			query.setParameterList("issueId", patientIssueReturnAddItem.getIssueIds());
			query.setParameterList("batchId", patientIssueReturnAddItem.getBatchIds());
			query.setParameter("unitId", patientIssueReturnAddItem.getUnitId()!=null ? patientIssueReturnAddItem.getUnitId() : sessionService.getUnitId());

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public List<PatientAgainstItemResponse> getItemByIssueId(List<Integer> issueIds,Integer unitId) {

		try {
			String hql = " SELECT issue.id AS id,sale.billNo AS issueNo, issue.addedDate AS issueDate, issueDetail.id as issuDetailId,"
					+ " item.id AS itemId, item.name AS itemName, item.code AS itemCode, item.isThreeUOMApplicable AS isThreeUOMApplicable, generic.name AS genericName,  "
					+ " uom.type AS uom, uomUnit.units AS uomUnit, batch.batchNo AS batchNo,  batch.id as batchId , issueDetailBatchMapper.issueQuantity AS quantityIssued,"
					+ " sale.id AS saleId, "
					+ " batch.expiryDate AS expiry, issueDetailBatchMapper.acceptedQuantity AS returnQuantity, issueDetailBatchMapper.id AS batchMapperId, "
					+ " 	(select sum(patientIssueReturnDetail.returnQuantity) from PatientIssueReturnDetail AS patientIssueReturnDetail "
					+ "  where patientIssueReturnDetail.issueDetailBatchMapper.id = issueDetailBatchMapper.id) AS patientReturnedQuantity  "
					+ " FROM Issue AS issue "
					+ " INNER JOIN issue.issueDetail AS issueDetail WITH issueDetail.isActive = true AND issueDetail.isDeleted = false "
					+ " INNER JOIN issueDetail.saleDetail AS saleDetail WITH saleDetail.isActive = true AND saleDetail.isDeleted = false "
					+ " INNER JOIN saleDetail.sale AS sale WITH  sale.isActive = true AND sale.isDeleted = false "
					+ " INNER JOIN issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
					+ " INNER JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
					+ "	INNER JOIN issueDetailBatchMapper.uomType AS uom WITH  uom.isActive = true AND uom.isDeleted = false "
					+ "	INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
					+ " WHERE   issueDetailBatchMapper.acceptedQuantity >0 AND issue.id in(:issueId) AND issue.unit.unitId = :unitId AND issue.isActive = true AND issue.isDeleted = false ";

			Query query = getSession().createQuery(hql);
			query.setParameterList("issueId", issueIds);
			query.setParameter("unitId", unitId!=null ? unitId :  sessionService.getUnitId());

			query.setResultTransformer(Transformers.aliasToBean(PatientAgainstItemResponse.class));
			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public List<PatientAgainstItemResponse> getItemByIssueAndItemId(Integer issueId, List<Integer> itemIds, Integer unitid,List<Integer> batchIds) {
		try {
			String hql = " SELECT issue.id AS id, sale.billNo AS issueNo, issue.addedDate AS issueDate, "
					+ " item.id AS itemId, item.name AS itemName, item.code AS itemCode, item.isThreeUOMApplicable AS isThreeUOMApplicable, generic.name AS genericName,  issueDetail.id AS issuDetailId, "
					+ " uom.type AS uom, uomUnit.units AS uomUnit, batch.batchNo AS batchNo, issueDetailBatchMapper.issueQuantity AS quantityIssued, sale.id AS saleId, "
					+ " batch.expiryDate AS expiry, issueDetailBatchMapper.acceptedQuantity AS returnQuantity, issueDetailBatchMapper.id AS batchMapperId, batch.id as batchId ,coalesce(patientIssueReturnDetail.returnQuantity,0) as patientIssueReturnQuantity,  "
					+ " sum((issueDetailBatchMapper.returnAcceptedQuantity+issueDetailBatchMapper.returnRejectedQuantity)-(COALESCE(issueDetailBatchMapper.rejectedRejectionAcceptedQuantity,0))) as  patientReturnedQuantity  "
					+ " FROM Issue AS issue "
					+ " INNER JOIN issue.issueDetail AS issueDetail WITH issueDetail.isActive = true AND issueDetail.isDeleted = false "
					+ " INNER JOIN issueDetail.saleDetail AS saleDetail WITH saleDetail.isActive = true AND saleDetail.isDeleted = false "
					+ " INNER JOIN saleDetail.sale AS sale WITH  sale.isActive = true AND sale.isDeleted = false "
					+ " INNER JOIN issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.batch AS batch WITH batch.isActive = true AND batch.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.uomUnit AS uomUnit WITH uomUnit.isActive = true AND uomUnit.isDeleted = false "
					+ " INNER JOIN batch.item AS item WITH item.isActive = true AND item.isDeleted = false "
					+ "	INNER JOIN issueDetailBatchMapper.uomType AS uom WITH  uom.isActive = true AND uom.isDeleted = false "
					+ "	INNER JOIN item.generic AS generic WITH generic.isDeleted = false AND generic.isActive = true "
                    + " LEFT JOIN issueDetailBatchMapper.patientIssueReturnDetail as patientIssueReturnDetail WITH patientIssueReturnDetail.isActive=true AND patientIssueReturnDetail.isDeleted = false  "
					+ " WHERE issue.id = :issueId AND ( item.id IN (:itemId) ) and issueDetailBatchMapper.acceptedQuantity > 0 AND issue.unit.unitId = :unitId AND issue.isActive = true AND issue.isDeleted = false "
					+ " AND issueDetailBatchMapper.acceptedQuantity> coalesce(patientIssueReturnDetail.returnQuantity,0) AND issueDetailBatchMapper.id in(:batchId) "
					+ " group by  issue.id , sale.billNo, issue.addedDate ,\r\n" + 
					"item.id , item.name  , item.code  , generic.name  ,\r\n"
					+ " issueDetail.id ," + 
					"uom.type , uomUnit.units , batch.batchNo  , issueDetailBatchMapper.issueQuantity ,\r\n" + 
					"batch.expiryDate , issueDetailBatchMapper.acceptedQuantity , issueDetailBatchMapper.id, batch.id,patientIssueReturnDetail.returnQuantity ,sale.id ";

			Query query = getSession().createQuery(hql);
			query.setParameter("issueId", issueId);
			query.setParameterList("itemId", itemIds);
			query.setParameterList("batchId", batchIds);
			query.setParameter("unitId", unitid!=null ? unitid : sessionService.getUnitId());

			query.setResultTransformer(Transformers.aliasToBean(PatientAgainstItemResponse.class));
			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public PatientIssueReturn patientIssueReturnById(Integer id, Integer unitId) {
		try {
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
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<PatientIssueReturnDetail> patientIssueReturnDetailById(Integer issueReturnId, Integer unitId) {

		try {
			String hql = " SELECT patientIssueReturnDetail FROM PatientIssueReturnDetail AS patientIssueReturnDetail "
					+ " INNER JOIN patientIssueReturnDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.batch AS batch WITH batch.isDeleted = false AND batch.isActive = true AND batch.isQuarantine = false AND batch.isReturn = false "
					+ " INNER JOIN patientIssueReturnDetail.patientIssueReturn AS patientIssueReturn WITH patientIssueReturn.isActive = true AND patientIssueReturn.isDeleted = false "
					+ " WHERE patientIssueReturnDetail.isActive = true AND patientIssueReturnDetail.isDeleted = false AND patientIssueReturn.id = :id"
					+ " AND patientIssueReturn.unit.unitId = :unitId ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", issueReturnId);
			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());

			return query.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public PatientIssueReturn patientIssueReturnSave(PatientIssueReturn patientIssueReturn) {
		getSession().save(patientIssueReturn);
		return patientIssueReturn;
	}

	@Override
	public PatientIssueReturnDetail patientIssueReturnDetailByReturnId(Integer issueReturnId) {
		try {
			String hql = " SELECT patientIssueReturnDetail FROM PatientIssueReturnDetail AS patientIssueReturnDetail "
					+ " INNER JOIN patientIssueReturnDetail.patientIssueReturn AS patientIssueReturn WITH patientIssueReturn.isActive = true AND patientIssueReturn.isDeleted = false "
					+ " WHERE patientIssueReturnDetail.isActive = true AND patientIssueReturnDetail.isDeleted = false AND patientIssueReturn.id = :id  ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", issueReturnId);
			//query.setParameter("unitId", sessionService.getUnitId());

			return (PatientIssueReturnDetail) query.uniqueResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	// Function For Get SaleDetailId Using Reff of IssueDetailBatchMapper Id

	@Override
	public PharmacySaleReturnDto getSaleDetailIdByIssueDetailMapper(Integer id) {
		try {
			String hql = " SELECT saleDetail.id as saleDetailId,issueDetailBatchMapper.batch.id as batchId,bat.item.id as itemId,saleDetail.sale.id as saleId,saleDetail.productCategoryId as productCategoryId  FROM IssueDetailBatchMapper AS issueDetailBatchMapper  "
					+ "	INNER JOIN issueDetailBatchMapper.issueDetail AS issueDetail WITH issueDetail.isActive = true AND issueDetail.isDeleted = false "
					+ " INNER JOIN issueDetail.saleDetail AS saleDetail WITH saleDetail.isActive = true AND saleDetail.isDeleted = false "
					+ " INNER JOIN issueDetailBatchMapper.batch AS bat WITH bat.isActive = true AND bat.isDeleted = false "
					+ " WHERE issueDetailBatchMapper.isActive = true AND issueDetailBatchMapper.isDeleted = false AND issueDetailBatchMapper.id = :id ";
			// + " AND issueDetailBatchMapper.unit.unitId = :unitId ";
			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			// query.setParameter("unitId", sessionService.getUnitId());
			return (PharmacySaleReturnDto) query
					.setResultTransformer(Transformers.aliasToBean(PharmacySaleReturnDto.class)).uniqueResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Issue getIssue(Integer id) {

		try {
			String hql = " SELECT issue FROM Issue AS issue  WHERE issue.isActive = true AND issue.isDeleted = false AND issue.id =:issueId ";

			Query query = getSession().createQuery(hql);
			query.setParameter("issueId", id);

			return (Issue) query.uniqueResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public void updateIssue(Issue issue) {

		try {
			getSession().update(issue);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public boolean getCOPFlagFromSale(Integer saleDetailsId) {
		String hql = " SELECT (case when ts.cop is not null then ts.cop else false end)  from pharmacy.t_issue tsd INNER JOIN pharmacy.t_sale ts on  tsd.sale_id=ts.id where tsd.id=:saleDetailsId ";

		Query query = getSession().createSQLQuery(hql);
		query.setParameter("saleDetailsId", saleDetailsId);

		return (boolean) query.uniqueResult();
	}

	@Override
	public List<PatientIssueReturnRequest> patientAgainstIssueDetailsSearch(
			PatientIssueReturnRequest patientIssueReturnRequest) {

		try {
			List<PatientIssueReturnRequest> IssueDetailsList = null;
			String hql = " select issueDetail.id as issueDetailId FROM  IssueDetail  as issueDetail  where issueDetail.isActive = true AND issueDetail.isDeleted = false AND issueDetail.issue.id =:issueId ";  
			Query query = getSession().createQuery(hql);
			query.setParameter("issueId", patientIssueReturnRequest.getId());
			query.setResultTransformer(Transformers.aliasToBean(PatientIssueReturnRequest.class)).list();
			
			IssueDetailsList = query.list();
			return IssueDetailsList;
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public List<PatientIssueReturnRequest> patientAgainstIssueDetailsBatchMapperSearch(
			PatientIssueReturnRequest issueDetail) {

		try {
			List<PatientIssueReturnRequest> getIssueDetailBatchMapperList = null;
			String hql = " select issueDetailBatchMapper.returnAcceptedQuantity as returnAcceptedQuantity FROM  IssueDetailBatchMapper  as issueDetailBatchMapper  where issueDetail.isActive = true AND issueDetail.isDeleted = false AND issueDetail.issue.id =:issueDetailId ";  
			Query query = getSession().createQuery(hql);
			query.setParameter("issueDetailId", issueDetail.getIssueDetailId());
			query.setResultTransformer(Transformers.aliasToBean(PatientIssueReturnRequest.class)).list();

			getIssueDetailBatchMapperList = query.list();
			return getIssueDetailBatchMapperList;
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public ReturnAcceptanceResponse getDocDetailForPatientIssueReturn(Integer id, Integer unitId) {
		try {
			String sql = " select" + 
//					"	ti.doctor_id as \"doctorId\"," + 
					"	(case" + 
					"		when mdm.doctor_id != 0 then concat('Dr.', ' ', coalesce(mdm.first_name), ' ', coalesce(mdm.middle_name), ' ', coalesce(mdm.last_name), '|', msm.speciality_name )" + 
					"		else ('')" + 
					"	end) as \"doctorDetail\"" + 
					" from" + 
					"	pharmacy.t_patient_issue_return rtn" + 
					" left join pharmacy.t_patient_issue_return_detail tpird on" + 
					"	tpird.patient_issue_return_id = rtn.id" + 
					" left join pharmacy.t_issue_detail_batch_mapper tidbm on" + 
					"	tidbm.id = tpird.issue_batch_mapper_id" + 
					" left join pharmacy.t_issue_detail tid on" + 
					"	tid.id = tidbm.issue_detail_id" + 
					" left join pharmacy.t_issue ti on" + 
					"	ti.id = tid.issue_id" + 
					" left join doctor.m_doctor_master mdm on" + 
					"	ti.doctor_id = mdm.doctor_id" + 
					" left join doctor.m_speciality_master msm on" + 
					"	msm.speciality_id = mdm.speciality_id" + 
					" where" + 
					"	rtn.id = :id" + 
					"	and rtn.unit_id = :unitId order by 1 desc";
//			Query query = getSession().createSQLQuery(sql);
//			Query query = getSession().createSQLQuery(sql).addScalar("doctorDetail");
//			query.setParameter("id", id);
//			query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());

			ReturnAcceptanceResponse dto = (ReturnAcceptanceResponse) getSession().createSQLQuery(sql)
					.setParameter("id", id)
					.setParameter("unitId", unitId)
					.setMaxResults(1)
					.setResultTransformer(Transformers.aliasToBean(ReturnAcceptanceResponse.class))
					.uniqueResult();
//			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
			
			return dto;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Integer patientReturnAcceptanceStatus(ReturnAcceptanceSave returnAcceptanceSave) {
		CommonDto idStatus = (CommonDto) sessionFactory.getCurrentSession()
				.createSQLQuery("	select status_id as \"id\" from pharmacy.t_patient_issue_return ti  where  is_active = true and is_deleted = false and id =:id ")
				.setParameter("id", returnAcceptanceSave.getId())
				.setResultTransformer(Transformers.aliasToBean(CommonDto.class)).uniqueResult();
		return idStatus.getId();
	}

	@Override
	public Integer patientIssueReturnDao(Integer batchMapperId) {
		Integer qty = (Integer) sessionFactory.getCurrentSession()
				.createSQLQuery("	select coalesce(cast(sum(ti.return_qty) as int),0) from pharmacy.t_patient_issue_return_detail ti  where  ti.is_active = true and ti.is_deleted = false and ti.issue_batch_mapper_id =:batchMapperId ")
				.setParameter("batchMapperId", batchMapperId)
				.uniqueResult();
		return qty;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PatientAgainstItemResponse> patientIssueReturnList(List<Integer> batchMapperIds) {
		
		List<PatientAgainstItemResponse> qtyList = sessionFactory.getCurrentSession()
				.createSQLQuery("	select coalesce(cast(sum(ti.return_qty) as int),0) as \"returnQuantity\" , ti.issue_batch_mapper_id as \"batchMapperId\" "
						+ "  from pharmacy.t_patient_issue_return_detail ti "
						+ " where  ti.is_active = true and ti.is_deleted = false and ti.issue_batch_mapper_id in(:batchMapperIds) group  by ti.issue_batch_mapper_id ")
				.setParameterList("batchMapperIds", batchMapperIds)
				.setResultTransformer(Transformers.aliasToBean(PatientAgainstItemResponse.class)).list();
		
		return qtyList;
	}

}
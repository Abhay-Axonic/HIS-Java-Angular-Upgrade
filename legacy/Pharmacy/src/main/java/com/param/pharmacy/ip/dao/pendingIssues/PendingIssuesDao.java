package com.param.pharmacy.ip.dao.pendingIssues;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.Sale;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.CommonDto;
import com.param.global.pharmacy.dto.IssueDetailsDto;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dto.pharmacology.DrugItems;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyItem;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;
import com.param.pharmacy.ip.dto.pharmacology.ReturnDetails;
import com.param.service.global.SessionService;
@SuppressWarnings({ "unchecked", "rawtypes" })
@Repository("pendingIssuesDao")
public class PendingIssuesDao extends AbstractDao<Integer, Issue> implements IPendingIssuesDao {

	@Autowired
	private SessionService sessionService;

	@Autowired
	SessionFactory sessionFactory;

	private static final Logger logger = Logger.getLogger(PendingIssuesDao.class);

	@Override
	public List<Issue> pendingIssuesSearch(PharmacologyValidationSearch pharmacologyValidationSearch, Integer screenId)
	{

		try
		{
			String hql = "SELECT issue FROM Issue AS issue"
					+ " INNER JOIN issue.status AS status WITH status.isActive = true AND status.isDeleted = false "
					+ " LEFT JOIN issue.assignPicker AS assignPicker WITH assignPicker.isActive = true AND assignPicker.isDeleted = false "
					+ " INNER JOIN issue.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
					+ " AND prescription.unit.unitId = :unitId "
					+ " INNER JOIN issue.visitType as visitType WITH visitType.status = 'A' "
					+ " LEFT JOIN prescription.priority AS priority WITH priority.status = 'A'"
					+ " LEFT JOIN prescription.specialityMaster AS speciality WITH speciality.status = 'A'"
					+ " LEFT JOIN issue.doctor AS doctor WITH doctor.status = 'A'"
					+ " LEFT JOIN issue.admission as admission "
//					WITH admission.status = 'A'
					+ " LEFT JOIN admission.admissionDetail as admissionDetail WITH admissionDetail.status = 'A' "
					+ " LEFT JOIN admissionDetail.wardMaster as wardMaster WITH wardMaster.status = 'A' "
					+ " LEFT JOIN admissionDetail.bedMaster as bedMaster WITH bedMaster.status = 'A' "
					+ " LEFT JOIN admission.patient as patient WITH patient.status = 'A' "
					+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
					+" WHERE issue.unit.unitId=:unitId AND issue.isActive = true AND issue.isDeleted = false AND prescription.prescriptionDate BETWEEN  :fromDate AND :toDate "	
					+ " AND issue.isNarcotics = false "
					+ " AND ( visitType.visitTypeId = :visitTypeId OR :visitTypeId = 0 ) "
					+ " AND ( status.id = :statusId OR :statusId = 0 )"
					+ " AND ((:doctorName = '' OR lower(COALESCE(concat('Dr. ',CONCAT(doctor.firstName,' ',doctor.middleName,' ',doctor.lastName)))) LIKE :doctorName OR lower(doctor.firstName) LIKE :doctorName OR lower(doctor.middleName) LIKE :doctorName " 
					+ "	OR lower(doctor.lastName) LIKE :doctorName))"
					+ " AND ( :patientId = 0 OR patient.patientId = :patientId  ) "
					+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
					+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
					+ "	OR lower(patient.lastName) LIKE :patientName  "
					+ ")) "
					+ " AND ((:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid ))"
					+ " AND (:wardName = '' OR lower(wardMaster.wardName) LIKE :wardName) "
					+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
					+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
					+ "	INNER JOIN screenStatus.status as status)"
				/*	+ " AND  prescription.id in ( select  prescriptionDetailsList.prescription.id from PrescriptionDetail as prescriptionDetailsList  "
					+ "	left JOIN prescriptionDetailsList.item as item with item.isNarcotic = false where prescriptionDetailsList.statFlag= false)"*/
					+ " ORDER BY issue.id DESC ";

			Query query = getSession().createQuery(hql);
			query.setParameter("fromDate", Util.getDate(pharmacologyValidationSearch.getFromDate()));
			query.setParameter("toDate", Util.getDate(pharmacologyValidationSearch.getToDate()));
			query.setParameter("statusId", pharmacologyValidationSearch.getStatusId() != null ? pharmacologyValidationSearch.getStatusId() : 0);
			query.setParameter("visitTypeId",pharmacologyValidationSearch.getVisitType() != null ? pharmacologyValidationSearch.getVisitType() : 0);
			query.setParameter("patientId",pharmacologyValidationSearch.getPatientId() != null ? pharmacologyValidationSearch.getPatientId() : 0);
			query.setParameter("patientName",pharmacologyValidationSearch.getPatientName() != null ? '%' + pharmacologyValidationSearch.getPatientName().toLowerCase() + '%' : "");
			query.setParameter("doctorName",pharmacologyValidationSearch.getDoctorName()!= null ? pharmacologyValidationSearch.getDoctorName().toLowerCase() + '%' : "");
			query.setParameter("wardName",pharmacologyValidationSearch.getWardName() != null ? pharmacologyValidationSearch.getWardName().toLowerCase() + '%' : "");
			query.setParameter("uhid",pharmacologyValidationSearch.getUhid() != null ? '%' + pharmacologyValidationSearch.getUhid().toLowerCase() + '%' : "");
//			query.setParameter("unitId", sessionService.getUnitId());
			query.setParameter("screenId", screenId);
			query.setParameter("unitId", pharmacologyValidationSearch.getUnitId()!=null ? pharmacologyValidationSearch.getUnitId() : sessionService.getUnitId());
			
		return query.list();
	}
	catch(Exception e)
	{
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}

	}

	@Override
	public Issue pendingIssuesById(Integer id)
	{
		try
		{
			String hql = "SELECT issue FROM Issue AS issue  "
					+ " inner join issue.prescription prescription "
					+ " left join prescription.doctor "
					+ " WHERE issue.isActive = true AND issue.isDeleted = false AND issue.id = :id ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			
			return (Issue) query.uniqueResult();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Long getItemWiseIssueQuantity(Integer issueDetailId)
	{
		String hql = "SELECT COALESCE(SUM(issueDetailBatchMapper.issueQuantity), 0) FROM IssueDetailBatchMapper AS issueDetailBatchMapper "
				+ "		INNER JOIN issueDetailBatchMapper.issueDetail AS issueDetail WITH issueDetail.isDeleted = false AND issueDetail.isActive = true "
				+ "		WHERE issueDetailBatchMapper.isDeleted = false AND issueDetailBatchMapper.isActive = true  "
				+ "     AND issueDetail.id = :issueDetailId ";

		Query query = getSession().createQuery(hql);
		query.setParameter("issueDetailId", issueDetailId);
		//query.setParameter("unitId", sessionService.getUnitId());

		return  (query.uniqueResult() != null ? (Long) query.uniqueResult() : 0);
	}
	
	@Override
	public List<IssueDetail> pendingIssuesDetails(Integer id)
	{
		try
		{
			String hql = "SELECT issueDetail FROM IssueDetail AS issueDetail "
					+ " INNER JOIN issueDetail.issue AS issue WITH issue.isActive = true AND issue.isDeleted = false "
					+ " WHERE issueDetail.isActive = true AND issueDetail.isDeleted = false AND issue.id = :id and issueDetail.isSubstitute=false and issueDetail.prescriptionDetail.statFlag=false ";

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
	public Issue pendingIssuesSave(Issue issue)
	{
		getSession().save(issue);
		return issue;
	}
	
	@Override
	public Issue pendingIssuesUpdate(Issue issue)
	{
		getSession().update(issue);
		return issue;
	}
	
	@Override
	public Issue getIssue(Integer id)
	{
		try
		{
			String hql = "SELECT issue FROM Issue AS issue "
					+ " WHERE issue.isActive = true AND issue.isDeleted = false AND issue.id = :id ";

			Query query = getSession().createQuery(hql);
			query.setParameter("id", id);
			
			return (Issue)query.uniqueResult();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Sale saveBill(Sale sale)
	{
		getSession().save(sale);
		return sale;
	}
	
	@Override
	public List<DrugItems> getDrugItems(PharmacologyValidationSearch request)
	{
		try
		{
			String query= " select " 
					 + " 	issue.id as \"id\", " 
					 + " 	sale.id as \"salesId\", "
					 + "	orderDetails.order_id as \"orderId\", " 
					 + " 	doctor.doctor_id as \"doctorId\", " 
					 + " 	(case " 
					 + " 		when doctor.doctor_id >0 then concat('Dr. ', coalesce(doctor.first_name), ' ', coalesce(doctor.middle_name), ' ', coalesce(doctor.last_name)) " 
					 + " 		else null " 
					 + " 	end ) as \"doctorName\", " 
					 + " 	(case " 
					 + " 		when sale.bill_no is not null then sale.bill_no " 
					 + " 		when bill_consumption.consumable_number is not null then bill_consumption.consumable_number " 
					 + "		when crash_cart.crash_cart_no is not null then crash_cart.crash_cart_no "
					 + "		else sale.bill_no " 
					 + " 	end ) as \"issueNo\", " 
					 + " 	(case " 
					 + " 		when issue.added_date is not null then issue.added_date " 
					 + " 		when bill_consumption.added_date is not null then bill_consumption.added_date " 
					 + " 		when crash_cart.added_date is not null then crash_cart.added_date " 
					 + " 		else sale.added_date " 
					 + " 	end ) as \"issueDate\", "
					 + " 	to_char((case " 
					 + " 		when issue.added_date is not null then issue.added_date " 
					 + " 		when bill_consumption.added_date is not null then bill_consumption.added_date " 
					 + " 		when crash_cart.added_date is not null then crash_cart.added_date " 
					 + " 		else sale.added_date " 
					 + " 	end ),'DD/MM/YYYY HH24:MI:SS') as \"issueDateString\", "
					 + " 	item.code as \"itemCode\", " 
					 + " 	item.name as \"itemName\", " 
					 + " 	saleDetail.rate as \"unitPrice\", " 
					 + " 	orderDetails.order_qty as \"quantity\", " 
					 + " 	cast(orderDetails.ord_total_amt as double precision) as \"totalAmount\", " 
					 + " 	cast(orderDetails.net_amount as double precision) as \"netAmount\", " 
					 + " 	cast(orderDetails.ord_concession as double precision) as \"ordConcession\", " 
					 + " 	cast(orderDetails.ord_discount as double precision) as \"ordDiscount\", " 
					 + " 	cast(orderDetails.self_payable as double precision) as \"selfPayable\", " 
					 + " 	cast(orderDetails.credit_payable as double precision) as \"creditPayable\", "
					 + "	cast(orderDetails.tax_amount as double precision) as \"taxAmount\",  " 
					 + " 	cast(orderDetails.package_consumption_amt as double precision) as \"packageConsumptionAmt\", " 
					 + " 	concat(coalesce(prefix.prefix_code), ' ', coalesce(employee.first_name), ' ', coalesce(employee.middle_name), ' ', coalesce(employee.last_name)) as \"issueFrom\",saleDetail.is_dummy_service as \"isDummyService\"  " 
					 + " from " 
					 + " 	pharmacy.t_sale_detail saleDetail " 
					 + " inner join public.t_order_details orderDetails on " 
					 + " 	saleDetail.id = orderDetails.sale_detail_id " 
					 + " 	and saleDetail.is_active = true " 
					 + " 	and saleDetail.is_deleted = false " 
					 + " inner join pharmacy.t_sale sale on " 
					 + " 	saleDetail.sale_id = sale.id " 
					 + " inner join public.t_order_master ordermst on " 
					 + " 	ordermst.order_id = orderDetails.order_id " 
					 + " inner join public.m_item item on " 
					 + " 	item.id = orderDetails.drug_id " 
					 + " 	and item.is_active = true " 
					 + " 	and item.is_deleted = false " 
					 + " left join doctor.m_doctor_master doctor on " 
					 + " 	doctor.doctor_id = sale.doctor_id " 
					 + " 	and doctor.status = 'A' " 
					 + " left join pharmacy.t_issue issue on " 
					 + " 	issue.sale_id = sale.id " 
					 + " 	and issue.is_active = true " 
					 + " 	and issue.is_deleted = false " 
					 + " left join pharmacy.t_billable_consumption bill_consumption on " 
					 + " 	sale.id = bill_consumption.sale_id " 
					 + " 	and bill_consumption.is_active = true " 
					 + " left join pharmacy.t_crash_cart_request crash_cart on " 
					 + " 	sale.id = crash_cart.sale_id " 
					 + " 	and crash_cart.is_active = true " 
					 + " left join public.m_employee_master employee on " 
					 + " 	saleDetail.added_by = employee.user_id " 
					 + " left join public.m_prefix_master prefix on " 
					 + " 	employee.prefix_id = prefix.prefix_id " 
					 + " where " 
					 + " 	orderDetails.status = 'A' " 
					 + " 	and sale.unit_id = :unitId " ;
			
			if(request.getAdmissionId()!=null) {
				query = query.concat(" and ordermst.admission_id =:admissionId ");
			}
			if(request.getEncounterId()!=null) {
				query = query.concat(" and ordermst.encounter_id =:encounterId ");
			}

			query= query.concat(" order by orderDetails.order_details_id asc ");
			
			Query sqlquery = getSession().createSQLQuery(query);
			sqlquery.setParameter("unitId",request.getUnitId());
			if(request.getAdmissionId()!=null) {
				sqlquery.setParameter("admissionId", request.getAdmissionId());
			}
			if(request.getEncounterId()!=null) {
				sqlquery.setParameter("encounterId", request.getEncounterId());
			}
			sqlquery.setResultTransformer(Transformers.aliasToBean(DrugItems.class));
			return sqlquery.list();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<ReturnDetails> getReturnDetails(List<Long>  saleId)
	{
		try
		{
			if(saleId != null && !saleId.isEmpty()){
				String hql = " SELECT returnDetail.returnQty AS returnQuantity, returnDetail.returnAmt AS totalAmount , sale.id AS saleId FROM ReturnDetail AS returnDetail "
						+ " INNER JOIN returnDetail.saleDetail AS saleDetail WITH saleDetail.isActive = true AND saleDetail.isDeleted = false "
						+ " INNER JOIN saleDetail.sale AS sale WITH sale.isActive = true AND sale.isDeleted = false "
						+ "    WHERE sale.id in (:ids) AND sale.isActive = true AND sale.isDeleted = false  ";

				Query query = getSession().createQuery(hql);
				query.setParameterList("ids", saleId);
				//query.setParameter("unitId", sessionService.getUnitId());
				query.setResultTransformer(Transformers.aliasToBean(ReturnDetails.class));
				return query.list();
			}else{
				return null;
			}
		}
		catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public List<PharmacologyValidationSearchResult> pendingIssuesSearchBP(
			PharmacologyValidationSearch pharmacologyValidationSearch, Integer screenId) {
		
		String hql = "SELECT assignPicker.id as assignPickerId, visitType.visitTypeName as visitType, "
				+ " admission.createdDate as visitDate, prescription.prescriptionDate as prescriptionDate, "
				+ " patient.uhidNumber as uhid, "
				+ " priority.priorityDesc as priority, "
				+ " status.id as statusId, status.status as status, "
				+ " wardMaster.wardName as wardName, bedMaster.bedNumber as bedNo, "
				+ "	CONCAT(prefix.prefixCode, ' ', patient.firstName, ' ', patient.middleName, ' ', patient.lastName, ' | ', gender.genderCode) AS patientDetail, "
				+ " CONCAT(doctor.firstName, ' | ', doctor.middleName, ' ', doctor.lastName) as doctorDetail, "
				+ " patient.id as patientId, patient.dob as patientDob, doctor.id as doctorId, admission.id as admissionId, "
				+ " prescription.id as prescriptionId, assignPicker.id as assignPickerId "
				+ " FROM Issue AS issue"
				+ " INNER JOIN issue.status AS status WITH status.isActive = true AND status.isDeleted = false "
				+ " INNER JOIN issue.assignPicker AS assignPicker WITH assignPicker.isActive = true AND assignPicker.isDeleted = false "
				+ " INNER JOIN issue.prescription AS prescription WITH prescription.isActive = true AND prescription.isDeleted = false "
				+ " INNER JOIN issue.visitType as visitType WITH visitType.status = 'A' "
				+ " INNER JOIN prescription.priority AS priority WITH priority.status = 'A'"
				+ " LEFT JOIN issue.doctor AS doctor WITH doctor.status = 'A'"
				+ " LEFT JOIN issue.admission as admission "
				+ " LEFT JOIN admission.admissionDetail as admissionDetail "
				+ " LEFT JOIN admissionDetail.wardMaster as wardMaster "
				+ " LEFT JOIN admissionDetail.bedMaster as bedMaster "
				+ " LEFT JOIN issue.encounter AS encounter "
				+ " LEFT JOIN admission.patient as patient "
				+ " LEFT JOIN encounter.patient as patientEncounter "
				+ " LEFT JOIN admission.patient as patient "
				+ " LEFT JOIN encounter.patient as patientEncounter "
				+ " LEFT JOIN patient.prefix AS prefix "
				+ " LEFT JOIN patientEncounter.prefix AS prefixEncounter "
				+" WHERE issue.isActive = true AND issue.isDeleted = false AND issue.addedDate BETWEEN  :fromDate AND :toDate "	
				+ " AND issue.unit.unitId = :unitId AND ( visitType.visitTypeId = :visitTypeId OR :visitTypeId = 0 ) "
				+ " AND ( status.id = :statusId OR :statusId = 0 )"
				+ " AND ((:doctorName = '' OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ',doctor.lastName))) LIKE :doctorName OR lower(doctor.firstName) LIKE :doctorName OR lower(doctor.middleName) LIKE :doctorName " 
				+ "	OR lower(doctor.lastName) LIKE :doctorName))"
				+ " AND ( :patientId = 0 OR patient.patientId = :patientId OR patientEncounter.patientId = :patientId ) "
				+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
				+ " OR lower(COALESCE(CONCAT(prefixEncounter.prefixCode,' ',patientEncounter.firstName,' ',patientEncounter.middleName,' ', patientEncounter.lastName))) LIKE :patientName "
				+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
				+ "	OR lower(patient.lastName) LIKE :patientName OR lower(prefixEncounter.prefixCode) LIKE :patientName OR "
				+ "	lower(patientEncounter.firstName) LIKE :patientName OR lower(patientEncounter.middleName) LIKE :patientName "
				+ "	OR lower(patientEncounter.lastName) LIKE :patientName)) "
				+ " AND ((:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid OR lower(patientEncounter.uhidNumber) LIKE :uhid))"
				+ " AND (:wardName = '' OR lower(wardMaster.wardName) LIKE :wardName) "
				+ " AND status.id IN (SELECT status.id FROM ScreenStatus AS screenStatus "
				+ " INNER JOIN screenStatus.screen as screen WITH screen.id = :screenId " 
				+ "	INNER JOIN screenStatus.status as status) "
				+ " ORDER BY issue.id DESC ";

		Query query = getSession().createQuery(hql);
		query.setParameter("fromDate", Util.getDate(pharmacologyValidationSearch.getFromDate()));
		query.setParameter("toDate", Util.getDate(pharmacologyValidationSearch.getToDate()));
		query.setParameter("statusId", pharmacologyValidationSearch.getStatusId() != null ? pharmacologyValidationSearch.getStatusId() : 0);
		query.setParameter("visitTypeId",pharmacologyValidationSearch.getVisitType() != null ? pharmacologyValidationSearch.getVisitType() : 0);
		query.setParameter("patientId",pharmacologyValidationSearch.getPatientId() != null ? pharmacologyValidationSearch.getPatientId() : 0);
		query.setParameter("patientName",pharmacologyValidationSearch.getPatientName() != null ? pharmacologyValidationSearch.getPatientName().toLowerCase() + '%' : "");
		query.setParameter("doctorName",pharmacologyValidationSearch.getDoctorName()!= null ? pharmacologyValidationSearch.getDoctorName().toLowerCase() + '%' : "");
		query.setParameter("wardName",pharmacologyValidationSearch.getWardName() != null ? pharmacologyValidationSearch.getWardName().toLowerCase() + '%' : "");
		query.setParameter("uhid",pharmacologyValidationSearch.getUhid() != null ? pharmacologyValidationSearch.getUhid().toLowerCase() + '%' : "");
		query.setParameter("unitId", sessionService.getUnitId());
		query.setParameter("screenId", screenId);
		
		return query.list();
	}

	@Override
	public List<PharmacologyItem> getIssuedQuantity(List<Long> prescriptionDetailId) {
		String hql = "SELECT prescriptionDetail.id AS prescriptionDetailId, COALESCE(SUM(issueDetailBatchMapper.issueQuantity), 0) AS issuedQuantity "
				+ " FROM IssueDetail AS issueDetail "
				+ "		INNER JOIN issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isDeleted = false AND issueDetailBatchMapper.isActive = true "
				+ "		INNER JOIN issueDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isDeleted = false AND prescriptionDetail.isActive = true "
				+ "	WHERE issueDetail.isDeleted = false AND issueDetail.isActive = true "
				+ "     AND prescriptionDetail.id IN (:prescriptionDetailId) "
				+ " GROUP BY prescriptionDetail.id ";

		Query query = getSession().createQuery(hql);
		query.setParameterList("prescriptionDetailId", prescriptionDetailId);
		query.setResultTransformer(Transformers.aliasToBean(PharmacologyItem.class));

		return query.list();
	}

	@Override
	public List<PharmacologyItem> getQuantityDetails(List<Long> prescriptionDetailId) {
		String hql = "SELECT prescriptionDetail.id AS prescriptionDetailId, prescriptionDetail.orderQty AS quantityOrdered, COALESCE(SUM(issueDetailBatchMapper.issueQuantity), 0) AS issuedQuantity "
				+ " FROM IssueDetail AS issueDetail "
				+ "		INNER JOIN issueDetail.prescriptionDetail AS prescriptionDetail WITH prescriptionDetail.isDeleted = false AND prescriptionDetail.isActive = true "
				+ "		LEFT JOIN issueDetail.issueDetailBatchMapper AS issueDetailBatchMapper WITH issueDetailBatchMapper.isDeleted = false AND issueDetailBatchMapper.isActive = true "
				+ "	WHERE issueDetail.isDeleted = false AND issueDetail.isActive = true "
				+ "     AND prescriptionDetail.id IN (:prescriptionDetailId) "
				+ " GROUP BY prescriptionDetail.id, prescriptionDetail.orderQty ";

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
	public Response issueDetailsIssueQuentity(Integer id) {
		List<IssueDetail> issueQuentitylist = sessionFactory.getCurrentSession()
				.createSQLQuery("select id as \"id\", batch_id as \"batchId\" "
						+ "from  pharmacy.t_issue_detail "
						+ "where  issue_id =:id ")
				.setParameter("id", id)
				.setResultTransformer(Transformers.aliasToBean(IssueDetail.class))
.list();
		return new Response<>(null, null, null, issueQuentitylist, null);
	}
	@Override
	public Response getOtherIssueDetails(Long prescriptionId, Integer issueId) {
		List<IssueDetail> issueQuentitylist = sessionFactory.getCurrentSession()
				.createSQLQuery("select issDetails.id as \"id\"  "
						+ " from pharmacy.t_issue_detail issDetails " + 
						" inner join pharmacy.t_issue isss  on issDetails.issue_id = isss.id " + 
						" where isss.prescription_id =:prescriptionId "+" and isss.id not in (:issueId ) and issDetails.status_id != 4")
				.setParameter("prescriptionId", prescriptionId)
				.setParameter("issueId", issueId)
				.setResultTransformer(Transformers.aliasToBean(IssueDetail.class)).list();
		return new Response<>(null, null, null, issueQuentitylist, null);
	}

	@Override
	public Response getOrederQty(Long prescriptionId) {
		
		Integer preId = prescriptionId.intValue();
		
		IssueDetailsDto status = (IssueDetailsDto) sessionFactory.getCurrentSession()
				.createSQLQuery(" select( case when " + 
						" (select sum(order_qty) from pharmacy.t_prescription_detail where prescription_id =:preId and parent_substitute_id is null ) <= " +
						" (select sum(lease_quantity)  from pharmacy.t_assign_picker_detail_added_items  " + 
						" where  prescription_detail_id in(select id  from pharmacy.t_prescription_detail  where prescription_id =:preId )) then 4 else 18 end ) as \"statusId\" ")
				.setParameter("preId", preId)
				.setResultTransformer(Transformers.aliasToBean(IssueDetailsDto.class)).uniqueResult();
		return new Response<>(null, null, null, null, status);
	}

	@Override
	public Response updateIssueDetailForSaledetailId(Integer prescriptionDtlsId, Integer batchId) {
		Session session = sessionFactory.getCurrentSession();
		
		int res = session.createSQLQuery(
				" update pharmacy.t_issue_detail set sale_detail_id = (select id from pharmacy.t_sale_detail where batch_id =:batchId "
						 + " and prescription_detail_id =:prescriptionDtlsId"  + " order by 1 desc limit 1) where sale_detail_id is null and  batch_id =:batchId "
						 + " and prescription_detail_id =:prescriptionDtlsId ")
				.setParameter("batchId", batchId)
				.setParameter("prescriptionDtlsId", prescriptionDtlsId).executeUpdate();
		return new Response<>(null, null, null, null, res);
	}

	@Override
	public List<Prescription> getPreviousIssues(PharmacologyValidationSearch pharmacologyValidationSearch) {


		String hql = "SELECT prescription FROM Prescription AS prescription "
				+ " INNER JOIN prescription.status as status WITH status.isActive = true AND status.isDeleted = false "
				+ " INNER JOIN prescription.visitType as visitType WITH visitType.status = 'A'  "
				+ " LEFT JOIN prescription.priority AS priority WITH priority.status = 'A'"
				+ " LEFT JOIN prescription.doctor AS doctor WITH doctor.status = 'A'"
				+ " LEFT JOIN prescription.specialityMaster AS specialityMaster WITH specialityMaster.status = 'A'"
				+ " LEFT JOIN prescription.encounter as encounter WITH encounter.status = 'A'"
				+ " LEFT JOIN prescription.patient as patient WITH patient.status = 'A'"
				+ " LEFT JOIN patient.prefix AS prefix WITH prefix.status = 'A' "
				+ "  WHERE  prescription.isActive = true AND prescription.isDeleted = false   "
				+ " AND prescription.unit.unitId = :unitId AND ( visitType.visitTypeId = :visitTypeId OR :visitTypeId = 0 ) "
				+ " AND  ( status.id = :statusId OR :statusId = 0 )"
				+ " AND ((:doctorName = '' OR lower(COALESCE(CONCAT(doctor.firstName,' ',doctor.middleName,' ', doctor.lastName))) LIKE :doctorName OR lower(doctor.firstName) LIKE :doctorName) OR (:doctorName = '' OR lower(doctor.middleName) LIKE :doctorName)"
				+ "	OR (:doctorName = '' OR lower(doctor.lastName) LIKE :doctorName))"
				+ " AND ( :patientId = 0 OR patient.patientId = :patientId ) "
				+ "	AND ((:patientId != 0 AND :patientName != '') OR (:patientName = '' OR lower(COALESCE(CONCAT(prefix.prefixCode,' ',patient.firstName,' ',patient.middleName,' ', patient.lastName))) LIKE :patientName "
				+ " OR lower(prefix.prefixCode) LIKE :patientName OR lower(patient.firstName) LIKE :patientName OR lower(patient.middleName) LIKE :patientName "
				+ "	OR lower(patient.lastName) LIKE :patientName )) "
				+ " AND (:uhid = '' OR lower(patient.uhidNumber) LIKE :uhid)  "
				+ " ORDER BY  :orderby ";

		Query query = getSession().createQuery(hql);
		//query.setParameter("fromDate", Util.getDate(pharmacologyValidationSearch.getFromDate()));
		//query.setParameter("toDate", Util.getDate(pharmacologyValidationSearch.getToDate()));
		query.setParameter("statusId",
				pharmacologyValidationSearch.getStatusId() != null ? pharmacologyValidationSearch.getStatusId() : 0);
		query.setParameter("visitTypeId", pharmacologyValidationSearch.getVisitType());
		query.setParameter("patientId",
				pharmacologyValidationSearch.getPatientId() != null ? pharmacologyValidationSearch.getPatientId() : 0);
		query.setParameter("patientName",
				pharmacologyValidationSearch.getPatientName() != null
						? pharmacologyValidationSearch.getPatientName().toLowerCase() + '%'
						: "");
		query.setParameter("doctorName",
				pharmacologyValidationSearch.getDoctorName() != null
						? pharmacologyValidationSearch.getDoctorName().toLowerCase() + '%'
						: "");
		query.setParameter("uhid", pharmacologyValidationSearch.getUhid() != null
				? pharmacologyValidationSearch.getUhid().toLowerCase() + '%'
				: "");
		// query.setParameter("wardName",pharmacologyValidationSearch.getWardName() !=
		// null ? pharmacologyValidationSearch.getWardName().toLowerCase() + '%' : "");
		query.setParameter("unitId", pharmacologyValidationSearch.getUnitId()); 
		query.setParameter("orderby", "FIELD(priority.priorityDesc, 'High','Normal','Low'), prescription.id DESC");

		return query.list();
	
	}
	@Override
	public Integer getIssueStatus(Integer id) {
		try {
			Issue idStatus = (Issue) sessionFactory.getCurrentSession()
					.createSQLQuery("	select status_id as \"id\" from pharmacy.t_issue ti  where  is_active = true and is_deleted = false and id =:id ")
					.setParameter("id", id)
					.setResultTransformer(Transformers.aliasToBean(Issue.class)).uniqueResult();
			return idStatus.getId();
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}
	@Override
	public Integer getIssueDetialsStatus(Integer id, Integer screenId) {
		
		boolean isNarcotics = false;
		if(screenId == 1) {
			isNarcotics = true;
		}
		
		Integer com = 4;Integer perCom = 18;
		List<CommonDto> idStatus = sessionFactory.getCurrentSession()
				.createSQLQuery("select tid.organization_id as \"id\" from pharmacy.t_issue_detail tid " + 
						"	inner join pharmacy.t_prescription_detail tpd  on tid.prescription_detail_id = tpd.id " + 
						"	inner join m_item mi  on mi.id  = tpd.item_id  " + 
						"	where tid.is_active = true and tid.is_deleted = false  and tid.status_id  in(2,18) and mi.is_narcotic =:isNarcotics "+
						" and issue_id =:id ")
				.setParameter("isNarcotics", isNarcotics)
				.setParameter("id", id)
				.setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();
		
		if(idStatus == null || idStatus.size() == 0) {
			return com;
		}else {
			return perCom;
		}
		
		
	}

}
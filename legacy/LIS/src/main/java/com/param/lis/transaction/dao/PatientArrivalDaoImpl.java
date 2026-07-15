package com.param.lis.transaction.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.transaction.LabPatientArrivalMapperMaster;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.entity.lis.transaction.PatientArrivalMasterId;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.lis.common.dto.CommonAutoCompleteDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.common.StringCommonMethods;
import com.param.lis.transaction.dto.LabCommonDto;
import com.param.lis.transaction.dto.PatientArrivalDto;
import com.param.lis.transaction.dto.PatientArrivalMapperMasterDto;
import com.param.lis.transaction.dto.SearchCommonDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class PatientArrivalDaoImpl extends  GenericDao<LabPatientArrivalMapperMaster, Integer> implements IPatientArrivalDao, ICommonConstants, IError,ITransactionConstants{
	
  public PatientArrivalDaoImpl() {
	super(LabPatientArrivalMapperMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;

    final static Logger logger = Logger.getLogger(PatientArrivalDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Response patientArrival(LabCommonDto labCommonDto)
			throws ApplicationException {
		
		List<PatientArrivalDto>  listPatientArrivalDto  = null; 
		try
		{
			listPatientArrivalDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATENT_ARRIVAL_LIST")
					.setParameter("orgId", labCommonDto.getOrgId())
					.setParameter("orgUnitId", labCommonDto.getOrgUnitId())
					.setParameter("isOutSource", IS_OUT_SOURCE)
					.setParameter("statusId", labCommonDto.getStatusId())
					.setParameter("fromDate",  GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(labCommonDto.getOrgUnitId()),"yyyy-MM-dd"))
					.setParameter("toDate",   GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(labCommonDto.getOrgUnitId()),"yyyy-MM-dd"))
					.setFirstResult(labCommonDto.getOffset() != null ? labCommonDto.getOffset()  : 0)
					.setMaxResults(labCommonDto.getNoOfRecordsPerPage() != null ? labCommonDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(PatientArrivalDto.class)).list();
			
			if(listPatientArrivalDto.isEmpty())
			{
				return new Response(SUCCESS, SUCCESS_200,null, null, null);
				
			}
			else{
				return new Response(SUCCESS, SUCCESS_200, null, listPatientArrivalDto, null);
			}
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
		
	
	}

	@Override
	public Response patientArrivalList(LabCommonDto labCommonDto)
			throws ApplicationException {
		try
		{
			BigInteger patientArrivalTotalRecordCount = (BigInteger) sessionFactory.getCurrentSession()
                    .getNamedQuery("GET_PATENT_ARRIVAL_COUNT")
					.setParameter("orgId", labCommonDto.getOrgId())
					.setParameter("orgUnitId", labCommonDto.getOrgUnitId())
					.setParameter("statusId", labCommonDto.getStatusId())
					.setParameter("fromDate", labCommonDto.getFromDate() != null? labCommonDto.getFromDate() :GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(labCommonDto.getOrgUnitId()),"yyyy-MM-dd"))
					.setParameter("toDate",  labCommonDto.getToDate() != null ? labCommonDto.getToDate() :GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(labCommonDto.getOrgUnitId()),"yyyy-MM-dd"))
					.setParameter("isOutSource", IS_OUT_SOURCE).uniqueResult();
			
					//.setParameter("currentStatus",REJECT).uniqueResult();
			if (patientArrivalTotalRecordCount!=null && patientArrivalTotalRecordCount.compareTo(BigInteger.ZERO) == 1)
			{
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, patientArrivalTotalRecordCount);
			} else
			{
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response patientArrivalMapperMaster(
			List<PatientArrivalMapperMasterDto> listPatientArrivalMapperMasterDto)
			throws ApplicationException {
		
		try
		{
			LabPatientArrivalMapperMaster labPatientArrivalMapperMst =null;
			for (Iterator iterator = listPatientArrivalMapperMasterDto.iterator(); iterator.hasNext();)
			{	
				PatientArrivalMapperMasterDto patientArrivalMapperMasterDto = (PatientArrivalMapperMasterDto) iterator.next();

				labPatientArrivalMapperMst = new LabPatientArrivalMapperMaster();
				labPatientArrivalMapperMst.setCollectionTime(patientArrivalMapperMasterDto.getCollectionTime());
				labPatientArrivalMapperMst.setCreatedBy(patientArrivalMapperMasterDto.getCreatedBy());
				labPatientArrivalMapperMst.setCreatedDate(TimeZoneComponent.getDateByzone(patientArrivalMapperMasterDto.getOrgUnitId()).getTime());
				labPatientArrivalMapperMst.setDeptId(patientArrivalMapperMasterDto.getDeptId());
				labPatientArrivalMapperMst.setDoctorId(patientArrivalMapperMasterDto.getDoctorId());
				labPatientArrivalMapperMst.setOrgId(patientArrivalMapperMasterDto.getOrgId());
				labPatientArrivalMapperMst.setOrgUnitId(patientArrivalMapperMasterDto.getOrgUnitId());
				labPatientArrivalMapperMst.setPendingCount(patientArrivalMapperMasterDto.getPendingCount());
				labPatientArrivalMapperMst.setOrderDetailsId(patientArrivalMapperMasterDto.getOrderDetailsId());
				labPatientArrivalMapperMst.setTestId(patientArrivalMapperMasterDto.getTestId());
				
				PatientArrivalMasterId patientArrivalMasterId= new PatientArrivalMasterId();
				patientArrivalMasterId.setLabSmpDtlsId(patientArrivalMapperMasterDto.getPatientArrivalMasterId().getLabSmpDtlsId());
				patientArrivalMasterId.setLabSmpMstId(patientArrivalMapperMasterDto.getPatientArrivalMasterId().getLabSmpMstId());
				patientArrivalMasterId.setOrderId(patientArrivalMapperMasterDto.getPatientArrivalMasterId().getOrderId());
				patientArrivalMasterId.setPatientId(patientArrivalMapperMasterDto.getPatientArrivalMasterId().getPatientId());
				patientArrivalMasterId.setStatusId(patientArrivalMapperMasterDto.getPatientArrivalMasterId().getStatusId());
				labPatientArrivalMapperMst.setPatientArrivalMasterId(patientArrivalMasterId);
				labPatientArrivalMapperMst.setCreatedDate(TimeZoneComponent.getDateByzone(patientArrivalMapperMasterDto.getOrgUnitId()).getTime());
				labPatientArrivalMapperMst.setCollectionTime(patientArrivalMapperMasterDto.getCollectionTime());
				labPatientArrivalMapperMst = save(labPatientArrivalMapperMst);
			}
			
			if (labPatientArrivalMapperMst != null)
			{
				return new Response(SUCCESS, SUCCESS_200, PATIENT_ARRIVAL_SUCC, null, null);
			} else
			{
				return new Response(ERROR, ERR_500, PATIENT_ARRIVAL_FAIL, null, null);
			}
			
		}catch(HibernateException er){
			logger.error("Exection", er);throw er;
		} catch (Exception e)
		{
			logger.error("Exection", e);throw e;
		}
		
	}

	@Override
	public Response patientNotArrivalData(
			LabSampleDetailsMaster labSampleDetailsMaster)
			throws ApplicationException {
		try
		{
			LabPatientArrivalMapperMaster labPatientArrivalMapperMaster= new LabPatientArrivalMapperMaster();
			PatientArrivalMasterId PatientArrivalMasterId = new PatientArrivalMasterId();
			
			labPatientArrivalMapperMaster.setCreatedBy(labSampleDetailsMaster.getCreatedBy());
			labPatientArrivalMapperMaster.setTestId(labSampleDetailsMaster.getTestId());
			labPatientArrivalMapperMaster.setDeptId(labSampleDetailsMaster.getSubDeptId());
			labPatientArrivalMapperMaster.setPendingCount(labSampleDetailsMaster.getSamplePendingCount());
			labPatientArrivalMapperMaster.setOrgId(labSampleDetailsMaster.getOrgId());
			labPatientArrivalMapperMaster.setOrgUnitId(labSampleDetailsMaster.getOrgUnitId());
			labPatientArrivalMapperMaster.setDoctorId(labSampleDetailsMaster.getDoctorId());
			labPatientArrivalMapperMaster.setCreatedDate(TimeZoneComponent.getDateByzone(labSampleDetailsMaster.getOrgUnitId()).getTime());
			labPatientArrivalMapperMaster.setOrderDetailsId(labSampleDetailsMaster.getOrderDetailsId());
			PatientArrivalMasterId.setPatientId(labSampleDetailsMaster.getPatientId());
			PatientArrivalMasterId.setOrderId(labSampleDetailsMaster.getOrderId());
			PatientArrivalMasterId.setLabSmpDtlsId(Long.valueOf(labSampleDetailsMaster.getLabSampleDtlsId()));
			PatientArrivalMasterId.setStatusId(PATIRNT_NOT_ARRIVAL);
			PatientArrivalMasterId.setLabSmpMstId(labSampleDetailsMaster.getLabSampleId());
			labPatientArrivalMapperMaster.setPatientArrivalMasterId(PatientArrivalMasterId);
			LabPatientArrivalMapperMaster labPatientArrivalMapperMst =save(labPatientArrivalMapperMaster);
			
			if (labPatientArrivalMapperMst != null)
			{
				return new Response(SUCCESS, SUCCESS_200, PATIENT_ARRIVAL_SUCC, null, null);
			} else
			{
				return new Response(SUCCESS, SUCCESS_200, PATIENT_ARRIVAL_FAIL, null, null);
			}
		}catch(HibernateException we){
			logger.error("Exection", we);throw we;
		} catch (Exception e)
		{
			logger.error("Exection", e);throw e;
		}
	}
	

	@Override
	public Response getFilteredPatientArrivalList(SearchDto searchDto) throws ApplicationException {
		List<PatientArrivalDto> patientList = null;
		List<PatientArrivalDto> patientListForCount = null;
		List<Integer>  subDeptIds = new ArrayList<Integer>();
		List<Integer>  testTypes = new ArrayList<Integer>();
		List<Integer>  visitTypes = new ArrayList<Integer>();

		try {
			String pattern = "YYYY-MM-DD";
			String formattedDate = new SimpleDateFormat(pattern).format(TimeZoneComponent.getDateByzone(searchDto.getOrgUnitId()));

			// Get the query for patient arrival list and split for GROUP BY clause
			Query query = sessionFactory.getCurrentSession().getNamedQuery("GET_PATENT_ARRIVAL_LIST");
			String arr[] = query.getQueryString().split(" GROUP BY ");
			StringBuilder stringQuery = new StringBuilder(arr[0]);

			// Append conditions to the query based on search criteria
			if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
				stringQuery.append(" AND lbsampledtls.assign_unit = :assignUnit");

			if (searchDto.getPatientId() != null)
				stringQuery.append(" AND pati_mst.patient_id = :patientId");

			if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()) {
				visitTypes = searchDto.getVisitTypes().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());

				stringQuery.append(" AND vistmst.visit_type_id IN (:visitTypes)");
			}

			if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()) {
				subDeptIds = searchDto.getSubDepts().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());
				stringQuery.append(" AND lbsampledtls.sub_dept_id IN (:subDepts)");
			}

			if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()) {
				testTypes = searchDto.getTestTypes().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());

				stringQuery.append(" AND lbsampledtls.test_id IN (:testTypes)");
			}
			stringQuery.append(" GROUP BY ");
			stringQuery.append(arr[1]);
			Query createdQuery = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());
			createdQuery.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
					.setParameter("isOutSource", IS_OUT_SOURCE)
					.setParameter("statusId", searchDto.getSampleStatusId())
					.setParameter("fromDate", searchDto.getFromDt() != null ? searchDto.getFromDt() : formattedDate)
					.setParameter("toDate", searchDto.getToDt() != null ? searchDto.getToDt() : formattedDate);
			if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0) {
				createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());
			}

			if (searchDto.getPatientId() != null) {
				createdQuery.setParameter("patientId", searchDto.getPatientId());
			}

			if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()) {
				createdQuery.setParameterList("visitTypes", visitTypes);
			}

			if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()) {
				createdQuery.setParameter("subDepts", subDeptIds);
			}

			if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()) {
				createdQuery.setParameter("testTypes", testTypes);
			}
			createdQuery.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
					.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
					.setResultTransformer(Transformers.aliasToBean(PatientArrivalDto.class));
			patientList = createdQuery.list();
			int count = 0;
			if (patientList != null && !patientList.isEmpty()) {
				Query createdQueryForCount = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());
				createdQueryForCount.setParameter("orgId", searchDto.getOrgId())
						.setParameter("orgUnitId", searchDto.getOrgUnitId())
						.setParameter("isOutSource", IS_OUT_SOURCE)
						.setParameter("statusId", searchDto.getSampleStatusId())
						.setParameter("fromDate", searchDto.getFromDt() != null ? searchDto.getFromDt() : formattedDate)
						.setParameter("toDate", searchDto.getToDt() != null ? searchDto.getToDt() : formattedDate);

				if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0) {
					createdQueryForCount.setParameter("assignUnit", searchDto.getAssignUnit());
				}

				if (searchDto.getPatientId() != null) {
					createdQueryForCount.setParameter("patientId", searchDto.getPatientId());
				}

				if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()) {
					createdQueryForCount.setParameterList("visitTypes", visitTypes);
				}

				if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()) {
					createdQueryForCount.setParameter("subDepts", subDeptIds);
				}

				if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()) {
					createdQueryForCount.setParameter("testTypes", testTypes);
				}
				patientListForCount = createdQueryForCount.list();
				count = patientListForCount.size();
			}
			return new Response(SUCCESS, SUCCESS_200, null, patientList, count);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getFilteredPatientArrivalPatientSearchList(SearchCommonDto searchDto) throws ApplicationException {
		List<CommonAutoCompleteDto> listDto = null;

		try {
			StringBuilder sb = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_ARRIVAL_LIST_PATIENT_SEARCH").getQueryString());

			// Check if search keyword is provided and add it to the query string
			if (searchDto.getSearchKeyword() != null && !searchDto.getSearchKeyword().trim().isEmpty()) {
				String searchKeyword = "%" + searchDto.getSearchKeyword().trim().toLowerCase() + "%";
				sb.append(" AND (LOWER(pati_mst.first_name) LIKE :searchKeyword "
						+ " OR LOWER(pati_mst.last_name) LIKE :searchKeyword "
						+ " OR LOWER(pati_mst.uhid_number) LIKE :searchKeyword )");
			}

			// Create the query and set parameters conditionally
			Query createQuery  = sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
					.setParameter("isOutSource", IS_OUT_SOURCE);

			// If searchKeyword is provided, set the parameter for it
			if (searchDto.getSearchKeyword() != null && !searchDto.getSearchKeyword().trim().isEmpty()) {
				String searchKeyword = "%" + searchDto.getSearchKeyword().trim().toLowerCase() + "%";
			     createQuery.setParameter("searchKeyword", searchKeyword);
			}

			// Execute the query and return the result
			listDto = createQuery.setResultTransformer(Transformers.aliasToBean(CommonAutoCompleteDto.class)).list();
			return new Response(SUCCESS, SUCCESS_200, null, listDto, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}

	}

}

package com.param.lis.transaction.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.lis.common.dto.CommonAutoCompleteDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.common.StringCommonMethods;
import com.param.lis.transaction.dto.LabCommonDto;
import com.param.lis.transaction.dto.LabDashBoardDetailsDto;
import com.param.lis.transaction.dto.LabDashBoardDto;
import com.param.lis.transaction.dto.SearchCommonDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class LabDashBoardDaoImpl extends GenericDao<LabSampleDetailsMaster, Integer>
    implements ILabDashBoardDao, ICommonConstants, IError, ITransactionConstants {

  final static Logger logger = Logger.getLogger(LabDashBoardDaoImpl.class);

  public LabDashBoardDaoImpl() {
    super(LabSampleDetailsMaster.class);
  }


  @Override
  public Response getlabDashBoardList(LabCommonDto labCommonDto) throws ApplicationException {
    try {
    	String fromDate =GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(labCommonDto.getOrgUnitId()), "yyyy-MM-dd");
		String toDate =GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(labCommonDto.getOrgUnitId()), "yyyy-MM-dd");
    	
		
	String query = sessionFactory.getCurrentSession().getNamedQuery("GET_LAB_DASHBOARD_LIST").getQueryString();
	StringBuilder sb  = new StringBuilder(query);
	sb.append("  and to_char(order_details.created_date, 'YYYY-MM-DD') >=:fromDate "
      		+ "  and to_char(order_details.created_date, 'YYYY-MM-DD') <=:toDate  ");
	
      List<LabDashBoardDto> listLabDashBoardDto =
          sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
              .setParameter("orgId", labCommonDto.getOrgId())
              .setParameter("orgUnitId", labCommonDto.getOrgUnitId())
				  .setParameter("fromDate",fromDate)
				  .setParameter("toDate",toDate)
              .setFirstResult(labCommonDto.getOffset() != null ? labCommonDto.getOffset() : 0)
              .setMaxResults(labCommonDto.getNoOfRecordsPerPage() != null
                  ? labCommonDto.getNoOfRecordsPerPage()
                  : 10)
              .setResultTransformer(Transformers.aliasToBean(LabDashBoardDto.class)).list();

      if (listLabDashBoardDto.isEmpty()) {
        return new Response(ERROR, ERR_500, "No Speciman Records Found.", null, null);
      } else {
        return new Response(SUCCESS, SUCCESS_200, null, listLabDashBoardDto, null);
      }

    } catch (Exception e) {
      logger.error("Exection", e);
      throw e;
    }
  }

  @Override
  public Response getlabDashBoardListCount(LabCommonDto labCommonDto) throws ApplicationException {
    try {

		String query = sessionFactory.getCurrentSession().getNamedQuery("GET_LAB_DASHBOARD_LIST_COUNT").getQueryString();
		StringBuilder sb = new StringBuilder(query);
		if (labCommonDto.getFromDate() != null) {
			sb.append(" AND to_char(lbsampledtls.updated_date, 'YYYY-MM-DD') >= :fromDate ");
		}

		if (labCommonDto.getToDate() != null) {
			sb.append(" AND to_char(lbsampledtls.updated_date, 'YYYY-MM-DD') <= :toDate ");
		}

		if (labCommonDto.getSearchField() != null && !labCommonDto.getSearchField().isEmpty()) {
			sb.append(" AND (lower(pati_mst.uhid_number) LIKE concat('%', LOWER(:searchField), '%') OR "
					+ " lower(t_samp_mst.sample_no) LIKE concat('%', LOWER(:searchField), '%') OR "
					+ " lower(pati_mst.first_name) LIKE concat('%', LOWER(:searchField), '%') OR "
					+ " lower(pati_mst.middle_name) LIKE concat('%', LOWER(:searchField), '%') OR "
					+ " lower(pati_mst.last_name) LIKE concat('%', LOWER(:searchField), '%') OR "
					+ " lower(test_mst.test_desc) LIKE concat('%', LOWER(:searchField), '%') OR "
					+ " lower(test_mst.test_code) LIKE concat('%', LOWER(:searchField), '%') OR "
					+ " lower(subspe_mst.sub_speciality_name) LIKE concat('%', LOWER(:searchField), '%'))");
		}
		if (labCommonDto.getDeptId() != null && labCommonDto.getDeptId() > 0) {
			sb.append(" AND lbsampledtls.dept_id = :deptId ");
		}
		Query createQuery =  sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
				.setParameter("orgId", labCommonDto.getOrgId())
				.setParameter("orgUnitId", labCommonDto.getOrgUnitId());

		if (labCommonDto.getFromDate() != null) {
			createQuery.setParameter("fromDate", labCommonDto.getFromDate());
		}

		if (labCommonDto.getToDate() != null) {
			createQuery.setParameter("toDate", labCommonDto.getToDate());
		}

		if (labCommonDto.getSearchField() != null && !labCommonDto.getSearchField().isEmpty()) {
			createQuery.setParameter("searchField", labCommonDto.getSearchField().toLowerCase());
		}

		if (labCommonDto.getDeptId() != null && labCommonDto.getDeptId() > 0) {
			createQuery.setParameter("deptId", labCommonDto.getDeptId());
		}

// Execute and return result
		BigInteger labDashBoardCount = (BigInteger) createQuery.uniqueResult();

		if (labDashBoardCount.compareTo(BigInteger.ZERO) == 1) {
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, labDashBoardCount);
      } else {
        return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
      }
      
    } catch (Exception e) {
      logger.error("Exection", e);
      throw e;
    }
  }


  @Override
  public Response getSampleLog(Integer labSampleDtlsId, Integer orgId, Integer orgUnitId)
      throws ApplicationException {
    try {
      List<LabDashBoardDetailsDto> listLabDashBoardDetailsDto =
          sessionFactory.getCurrentSession().getNamedQuery("GET_LAB_DASHBOARD_DETAILS")
              .setParameter("orgId", orgId)
              .setParameter("labSampleDtlsId", labSampleDtlsId)
              .setResultTransformer(Transformers.aliasToBean(LabDashBoardDetailsDto.class)).list();

      if (listLabDashBoardDetailsDto.isEmpty()) {
        return new Response(ERROR, ERR_500, "No Speciman Records Found.", null, null);
      } else {
        return new Response(SUCCESS, SUCCESS_200, null, listLabDashBoardDetailsDto, null);
      }

    } catch (Exception e) {
      logger.error("Exection", e);
      throw e;
    }
  }


  @Override
  public Response getFilteredDashBoardList(SearchDto searchDto) throws ApplicationException {
    try
    {
		String fromDate = (searchDto.getFromDt() != null) ? searchDto.getFromDt() : GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(searchDto.getOrgUnitId()), "yyyy-MM-dd");
		String toDate = (searchDto.getToDt() != null) ? searchDto.getToDt() : GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(searchDto.getOrgUnitId()), "yyyy-MM-dd");

		StringBuilder getDashBoardSearchQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_LAB_DASHBOARD_LIST").getQueryString().toString());

	//Map<String, Object> parameters = new HashMap<>();

		if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
			getDashBoardSearchQuery.append(" AND pati_mst.patient_id = :patientId");

		}

		if (searchDto.getSpecimanTypes() != null && !searchDto.getSpecimanTypes().isEmpty()) {
			getDashBoardSearchQuery.append(" AND speciman_mst.speciman_id IN (:specimanTypes)");

			List<Integer>  specimanTypes = new ArrayList<Integer>();
			specimanTypes = searchDto.getSpecimanTypes().stream()
					.map(obj -> obj.getId())
					.collect(Collectors.toList());
		}

		if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()) {
			getDashBoardSearchQuery.append(" AND visit_type_master.visit_type_id IN (:visitTypes)");
			List<Integer>  visitTypes = new ArrayList<Integer>();
			visitTypes = searchDto.getVisitTypes().stream()
					.map(obj -> obj.getId())
					.collect(Collectors.toList());
		}

		if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()) {
			getDashBoardSearchQuery.append(" AND test_mst.test_id IN (:testTypes)");
			List<Integer>  testTypes = new ArrayList<Integer>();
			testTypes = searchDto.getTestTypes().stream()
					.map(obj -> obj.getId())
					.collect(Collectors.toList());

		}

		if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()) {
			getDashBoardSearchQuery.append(" AND test_mst.dept_id IN (:subDepts)");
			List<Integer>  subDepts = new ArrayList<Integer>();
			subDepts = searchDto.getSubDepts().stream()
					.map(obj -> obj.getId())
					.collect(Collectors.toList());

		}

		if (searchDto.getOutsourced() != null && searchDto.getOutsourced() == 'Y') {
			getDashBoardSearchQuery.append(" AND test_mst.is_outsourced = 'Y'");
		}

		if (searchDto.getSampleStatusId() != null && searchDto.getSampleStatusId() > 0) {
			getDashBoardSearchQuery.append(" AND lbsampledtls.sample_status_id = :sampleStatusId");
		}

		if (searchDto.getOutSource() != null && searchDto.getOutSource() == 'Y') {
			getDashBoardSearchQuery.append(" AND lbsampledtls.sample_status_id != :reportReleased");
		}

		if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0) {
			getDashBoardSearchQuery.append(" AND lbsampledtls.lab_sample_dtls_id = :labSampleDtlsId");
		}

		if (searchDto.getDeptId() != null && searchDto.getDeptId() > 0) {
			getDashBoardSearchQuery.append(" AND lbsampledtls.dept_id = :deptId");
		}

		if (searchDto.getCompleted() != null && searchDto.getCompleted() == 'Y') {
			getDashBoardSearchQuery.append(" AND lbsampledtls.sample_status_id = :completedStatus");
		}

		if (searchDto.getUhidNumber() != null && !searchDto.getUhidNumber().isEmpty()) {
			getDashBoardSearchQuery.append(" AND lower(pati_mst.uhid_number) LIKE :uhidNumber");
		}

		if (searchDto.getSampleNo() != null && !searchDto.getSampleNo().isEmpty()) {
			getDashBoardSearchQuery.append(" AND lower(t_samp_mst.sample_no) LIKE :sampleNo");
		}

		getDashBoardSearchQuery.append(" AND to_char(lbsampledtls.updated_date, 'YYYY-MM-DD') >= :fromDate AND to_char(lbsampledtls.updated_date, 'YYYY-MM-DD') <= :toDate");


		if(searchDto.getSearchField()!=null && searchDto.getSearchField()!="") {
			getDashBoardSearchQuery.append(" AND (lower(pati_mst.uhid_number) LIKE :searchField OR "
					+ " lower(t_samp_mst.sample_no) LIKE :searchField OR "
					+ " lower(pati_mst.first_name) LIKE :searchField OR "
					+ " lower(pati_mst.middle_name) LIKE :searchField OR "
					+ " lower(pati_mst.last_name) LIKE :searchField OR "
					+ " lower(test_mst.test_desc) LIKE :searchField OR"
					+ " lower(test_mst.test_code) LIKE :searchField OR "
					+ " lower(srvismstr.service_standard_name) LIKE concat(:searchField) OR "
					+ " lower(panelsrvismstr.service_standard_name) LIKE concat(:searchField) OR "
					+ " lower((case  "
					+ "			when opdReferral.referral_id is not null then "
					+ "				(select mrm.referral_name from adt.m_referral_master mrm where mrm.referral_id = opdReferral.referral_id) "
					+ "			else "
					+ "				(select mrm.referral_name from adt.m_referral_master mrm where mrm.referral_id = ipdReferral.referral_id) "
					+ "	end)) LIKE :searchField OR "
					+ " lower((case "
					+ "			when order_master.encounter_id is not null  then "
					+ " 		(case "
					+ "				when tpsd.payment_entitlement_id=2 then "
					+ " 				(select 	concat(tcm.company_desc, ' / ', insurance.insurance_desc) "
					+ " 				from public.t_company_master tcm  "
					+ " 				inner join service.t_insurance_tpa_mapper iMapp on iMapp.company_id = tcm.company_id "
					+ " 				inner join adt.t_insurance insurance on insurance.insurance_id = iMapp.insurance_id "
					+ " 				where tcm.company_type_id =2 and tcm.company_id = tpsd.payee_id limit 1) "
					+ " 			when tpsd.payment_entitlement_id=3 then "
					+ " 				(select 	tcm.company_desc "
					+ " 				from public.t_company_master tcm  "
					+ " 				where tcm.company_type_id =3 and tcm.company_id = tpsd.payee_id ) "
					+ " 		end) "
					+ " else "
					+ " 		(case "
					+ "				when tpsd2.payment_entitlement_id=2 then "
					+ " 				(select concat(tcm.company_desc, ' / ', insurance.insurance_desc) "
					+ " 				from public.t_company_master tcm  "
					+ " 				inner join service.t_insurance_tpa_mapper iMapp on iMapp.company_id = tcm.company_id "
					+ " 				inner join adt.t_insurance insurance on insurance.insurance_id = iMapp.insurance_id "
					+ " 				where tcm.company_type_id = 2 and tcm.company_id = tpsd2.payee_id limit 1) "
					+ " 	 		when tpsd2.payment_entitlement_id=3 then "
					+ " 				(select 	tcm.company_desc "
					+ " 				from public.t_company_master tcm  "
					+ " 				where tcm.company_type_id = 3 and tcm.company_id = tpsd2.payee_id ) "
					+ " 		end) "
					+ " end)) LIKE concat(:searchField) OR "
					+ " lower(subspe_mst.sub_speciality_name) LIKE :searchField )");

		}

		if(searchDto.getPaymentEntitlementId()!=null && searchDto.getPaymentEntitlementId()==ICommonConstants.INSURANCE) {
			if (searchDto.getIds()!= null) {
				getDashBoardSearchQuery.append(" AND (case when order_master.encounter_id > 0 then  " +
						" ( tpsd.insurance_id)  " +
						" else ( tpsd2.insurance_id) end ) in (:insuranceIds)");
				List<Integer> insuranceIds = searchDto.getIds().stream()
						.map(obj -> Integer.valueOf(obj.getId()))
						.collect(Collectors.toList());


			}
		}else if(searchDto.getPaymentEntitlementId()!=null && searchDto.getPaymentEntitlementId()==ICommonConstants.COMPANY) {
			if (searchDto.getIds()!= null) {
				getDashBoardSearchQuery.append(" AND (case when order_master.encounter_id > 0 then  " +
						" ( tpsd.payee_id)  " +
						" else ( tpsd2.payee_id) end ) in (:companyIds)");
				List<Integer>  companyIds = new ArrayList<Integer>();
				companyIds = searchDto.getIds().stream()
						.map(obj -> Integer.valueOf(obj.getId()))
						.collect(Collectors.toList());
			}
		}
		if(searchDto.getReferralDocId()!=null) {
			getDashBoardSearchQuery.append(" and (" +
					" case when bill.encounter_id is not null then " +
					"	(select referal.referral_id  from public.t_referal_details referal where  referal.encounter_id = bill.encounter_id) " +
					" else " +
					"	(select referal.referral_id  from public.t_referal_details referal where  referal.admission_id = bill.admission_id) " +
					" end) =:referralDocId ");
		}

		Query createdQuery = sessionFactory.getCurrentSession().createSQLQuery(getDashBoardSearchQuery.toString());
		Query QQUERY = createdQuery
				.setParameter("orgId", searchDto.getOrgId())
				.setParameter("orgUnitId", searchDto.getOrgUnitId())
				.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
				.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50);

		if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
			QQUERY.setParameter("patientId", searchDto.getPatientId());

		}
		if (searchDto.getSpecimanTypes() != null && !searchDto.getSpecimanTypes().isEmpty()) {

			List<Integer>  specimanTypes = new ArrayList<Integer>();
			specimanTypes = searchDto.getSpecimanTypes().stream()
					.map(obj -> obj.getId())
					.collect(Collectors.toList());
			QQUERY.setParameterList("specimanTypes", specimanTypes);
		}

		if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()) {
			List<Integer>  visitTypes = new ArrayList<Integer>();
			visitTypes = searchDto.getVisitTypes().stream()
					.map(obj -> obj.getId())
					.collect(Collectors.toList());
			QQUERY.setParameterList("visitTypes", visitTypes);
		}

		if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()) {
			List<Integer>  testTypes = new ArrayList<Integer>();
			testTypes = searchDto.getTestTypes().stream()
					.map(obj -> obj.getId())
					.collect(Collectors.toList());

			QQUERY.setParameterList("testTypes", testTypes);
		}

		if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()) {
			List<Integer>  subDepts = new ArrayList<Integer>();
			subDepts = searchDto.getSubDepts().stream()
					.map(obj -> obj.getId())
					.collect(Collectors.toList());

			QQUERY.setParameterList("subDepts",subDepts);
		}



		if (searchDto.getSampleStatusId() != null && searchDto.getSampleStatusId() > 0) {
			QQUERY.setParameter("sampleStatusId", searchDto.getSampleStatusId());
		}

		if (searchDto.getOutSource() != null && searchDto.getOutSource() == 'Y') {
			QQUERY.setParameter("reportReleased", REPORT_RELEASED);
		}

		if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0) {
			QQUERY.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId());
		}

		if (searchDto.getDeptId() != null && searchDto.getDeptId() > 0) {
			QQUERY.setParameter("deptId", searchDto.getDeptId());
		}

		if (searchDto.getCompleted() != null && searchDto.getCompleted() == 'Y') {
			QQUERY.setParameter("completedStatus", REPORT_RELEASED);
		}

		if (searchDto.getUhidNumber() != null && !searchDto.getUhidNumber().isEmpty()) {
			QQUERY.setParameter("uhidNumber", "%" + searchDto.getUhidNumber().toLowerCase() + "%");
		}

		if (searchDto.getSampleNo() != null && !searchDto.getSampleNo().isEmpty()) {
			QQUERY.setParameter("sampleNo", "%" + searchDto.getSampleNo().toLowerCase() + "%");
		}

		QQUERY.setParameter("fromDate", fromDate);
		QQUERY.setParameter("toDate", toDate);


		if(searchDto.getSearchField()!=null && searchDto.getSearchField()!="") {
			QQUERY.setParameter("searchField", "%" + searchDto.getSearchField().toLowerCase() + "%");
		}

		if(searchDto.getPaymentEntitlementId()!=null && searchDto.getPaymentEntitlementId()==ICommonConstants.INSURANCE) {
			if (searchDto.getIds()!= null) {

				List<Integer> insuranceIds = searchDto.getIds().stream()
						.map(obj -> Integer.valueOf(obj.getId()))
						.collect(Collectors.toList());


				QQUERY.setParameter("insuranceIds", insuranceIds);
			}
		}else if(searchDto.getPaymentEntitlementId()!=null && searchDto.getPaymentEntitlementId()==ICommonConstants.COMPANY) {
			if (searchDto.getIds()!= null) {

				List<Integer>  companyIds = new ArrayList<Integer>();
				companyIds = searchDto.getIds().stream()
						.map(obj -> Integer.valueOf(obj.getId()))
						.collect(Collectors.toList());
				QQUERY.setParameter("companyIds",companyIds);
			}
		}
		if(searchDto.getReferralDocId()!=null) {

			QQUERY.setParameter("referralDocId", Integer.valueOf(searchDto.getReferralDocId()));
		}



		List<LabDashBoardDto> listLabDashBoardDto =QQUERY.setResultTransformer(Transformers.aliasToBean(LabDashBoardDto.class))
				.list();

		if (listLabDashBoardDto.isEmpty()) {
			return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.", listLabDashBoardDto, null);
		} else {
			return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, listLabDashBoardDto, null);
		}



	} catch (Exception e)
    {
        logger.error("Exection", e);
        throw e;
    }
   
    
  }


@Override
public Response searchAcceptedSpecimanByPatient(SearchCommonDto searchCommonDto) throws ApplicationException {
	List<CommonAutoCompleteDto>  patientList  = null; 
	try 
	{
		patientList = (List<CommonAutoCompleteDto>) sessionFactory.getCurrentSession()
				.getNamedQuery("LAB_PATIENT_SEARCH")
				.setParameter("orgId", searchCommonDto.getOrgId())
				.setParameter("searchKeyword", "%"+searchCommonDto.getSearchKeyword().trim().toLowerCase().trim()+"%")
                .setResultTransformer(Transformers.aliasToBean(CommonAutoCompleteDto.class)).list();					
		return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, patientList, null);

	} catch (Exception e)
	{
		logger.error("Exection", e);
		throw e;
	}
}


@Override
public Response searchAcceptedSampleNo(SearchCommonDto searchCommonDto) throws ApplicationException {
	List<LabDashBoardDto>  patientList  = null; 
	try
	{
		patientList = (List<LabDashBoardDto>) sessionFactory.getCurrentSession()
				.getNamedQuery("SEARCH_SAMPLE_NO_LAB_DASHBOARD_LIST")
				.setParameter("orgId", searchCommonDto.getOrgId())
				.setParameter("orgUnitId", searchCommonDto.getOrgUnitId())
				.setParameter("searchKeyword", "%"+searchCommonDto.getSearchKeyword().trim().toLowerCase().trim()+"%")
                .setResultTransformer(Transformers.aliasToBean(LabDashBoardDto.class)).list();
		return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, patientList, null);

	} catch (Exception e)
	{
		logger.error("Exection", e);
		throw e;
	}
}


@Override
public Response universalSearchForDashBoardList(SearchDto searchDto)
		throws ApplicationException {
	try {
		String fromDate = (searchDto.getFromDt() != null)
				? searchDto.getFromDt()
				: GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(searchDto.getOrgUnitId()), "yyyy-MM-dd");
		String toDate = (searchDto.getToDt() != null)
				? searchDto.getToDt()
				: GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(searchDto.getOrgUnitId()), "yyyy-MM-dd");

		StringBuilder getDashBoardSearchQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_LAB_DASHBOARD_LIST").getQueryString().toString());

		Map<String, Object> queryParams = new HashMap<>();

		if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
			getDashBoardSearchQuery.append(" AND pati_mst.patient_id = :patientId");
			queryParams.put("patientId", searchDto.getPatientId());
		}

		if (searchDto.getSpecimanTypes() != null && !searchDto.getSpecimanTypes().isEmpty()) {
			getDashBoardSearchQuery.append(" AND speciman_mst.speciman_id IN (:specimanTypes)");
			queryParams.put("specimanTypes", searchDto.getSpecimanTypes());
		}

		if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()) {
			getDashBoardSearchQuery.append(" AND visit_type_master.visit_type_id IN (:visitTypes)");
			queryParams.put("visitTypes", searchDto.getVisitTypes());
		}

		if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()) {
			getDashBoardSearchQuery.append(" AND test_mst.test_id IN (:testTypes)");
			queryParams.put("testTypes", searchDto.getTestTypes());
		}

		if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()) {
			getDashBoardSearchQuery.append(" AND test_mst.dept_id IN (:subDepts)");
			queryParams.put("subDepts", searchDto.getSubDepts());
		}

		if ("Y".equals(searchDto.getOutsourced())) {
			getDashBoardSearchQuery.append(" AND test_mst.is_outsourced = 'Y'");
		}

		if (searchDto.getSampleStatusId() != null && searchDto.getSampleStatusId() > 0) {
			getDashBoardSearchQuery.append(" AND lbsampledtls.sample_status_id = :sampleStatusId");
			queryParams.put("sampleStatusId", searchDto.getSampleStatusId());
		}

		if ("Y".equals(searchDto.getOutSource())) {
			getDashBoardSearchQuery.append(" AND order_details.service_rendered = 0");
		}

		if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0) {
			getDashBoardSearchQuery.append(" AND lbsampledtls.lab_sample_dtls_id = :labSampleDtlsId");
			queryParams.put("labSampleDtlsId", searchDto.getLabSampleDtlsId());
		}

		if (searchDto.getDeptId() != null && searchDto.getDeptId() > 0) {
			getDashBoardSearchQuery.append(" AND lbsampledtls.dept_id = :deptId");
			queryParams.put("deptId", searchDto.getDeptId());
		}

		if ("Y".equals(searchDto.getCompleted())) {
			getDashBoardSearchQuery.append(" AND lbsampledtls.sample_status_id = :completedStatus");
			queryParams.put("completedStatus", REPORT_RELEASED);
		}

		if (searchDto.getUhidNumber() != null && !searchDto.getUhidNumber().isEmpty()) {
			getDashBoardSearchQuery.append(" AND lower(pati_mst.uhid_number) LIKE :uhidNumber");
			queryParams.put("uhidNumber", "%" + searchDto.getUhidNumber().toLowerCase() + "%");
		}

		if (searchDto.getSampleNo() != null && !searchDto.getSampleNo().isEmpty()) {
			getDashBoardSearchQuery.append(" AND lower(t_samp_mst.sample_no) LIKE :sampleNo");
			queryParams.put("sampleNo", "%" + searchDto.getSampleNo().toLowerCase() + "%");
		}

		getDashBoardSearchQuery.append(" AND to_char(lbsampledtls.updated_date, 'YYYY-MM-DD') >= :fromDate")
				.append(" AND to_char(lbsampledtls.updated_date, 'YYYY-MM-DD') <= :toDate");
		queryParams.put("fromDate", fromDate);
		queryParams.put("toDate", toDate);



		if(searchDto.getSearchField()!=null && searchDto.getSearchField()!="") {
			String searchField = "%" + searchDto.getSearchField().toLowerCase() + "%";
			getDashBoardSearchQuery.append(" AND (lower(pati_mst.uhid_number) LIKE :searchField OR "
					+ " lower(t_samp_mst.sample_no) LIKE :searchField OR "
					+ " lower(pati_mst.first_name) LIKE :searchField OR "
					+ " lower(pati_mst.middle_name) LIKE :searchField OR "
					+ " lower(pati_mst.last_name) LIKE :searchField OR "
					+ " lower(test_mst.test_desc) LIKE :searchField OR"
					+ " lower(test_mst.test_code) LIKE :searchField OR "
					+ " lower(srvismstr.service_standard_name) LIKE :searchField OR "
					+ " lower(panelsrvismstr.service_standard_name) LIKE :searchField OR "
					+ " lower((case  "
					+ "			when opdReferral.referral_id is not null then "
					+ "				(select mrm.referral_name from adt.m_referral_master mrm where mrm.referral_id = opdReferral.referral_id) "
					+ "			else "
					+ "				(select mrm.referral_name from adt.m_referral_master mrm where mrm.referral_id = ipdReferral.referral_id) "
					+ "	end)) LIKE :searchField OR "
					+ " lower((case "
					+ "			when order_master.encounter_id is not null  then "
					+ " 		(case "
					+ "				when tpsd.payment_entitlement_id=2 then "
					+ " 				(select 	concat(tcm.company_desc, ' / ', insurance.insurance_desc) "
					+ " 				from public.t_company_master tcm  "
					+ " 				inner join service.t_insurance_tpa_mapper iMapp on iMapp.company_id = tcm.company_id "
					+ " 				inner join adt.t_insurance insurance on insurance.insurance_id = iMapp.insurance_id "
					+ " 				where tcm.company_type_id =2 and tcm.company_id = tpsd.payee_id limit 1) "
					+ " 			when tpsd.payment_entitlement_id=3 then "
					+ " 				(select 	tcm.company_desc "
					+ " 				from public.t_company_master tcm  "
					+ " 				where tcm.company_type_id =3 and tcm.company_id = tpsd.payee_id ) "
					+ " 		end) "
					+ " else "
					+ " 		(case "
					+ "				when tpsd2.payment_entitlement_id=2 then "
					+ " 				(select concat(tcm.company_desc, ' / ', insurance.insurance_desc) "
					+ " 				from public.t_company_master tcm  "
					+ " 				inner join service.t_insurance_tpa_mapper iMapp on iMapp.company_id = tcm.company_id "
					+ " 				inner join adt.t_insurance insurance on insurance.insurance_id = iMapp.insurance_id "
					+ " 				where tcm.company_type_id = 2 and tcm.company_id = tpsd2.payee_id limit 1) "
					+ " 	 		when tpsd2.payment_entitlement_id=3 then "
					+ " 				(select 	tcm.company_desc "
					+ " 				from public.t_company_master tcm  "
					+ " 				where tcm.company_type_id = 3 and tcm.company_id = tpsd2.payee_id ) "
					+ " 		end) "
					+ " end)) LIKE :searchField OR "
					+ " lower(subspe_mst.sub_speciality_name) LIKE :searchField )");
			queryParams.put("searchField", searchField);
		}

		if (searchDto.getPaymentEntitlementId() != null) {
			if (searchDto.getPaymentEntitlementId() == ICommonConstants.INSURANCE && searchDto.getIds() != null) {
				getDashBoardSearchQuery.append(" AND (case when order_master.encounter_id > 0 then tpsd.insurance_id else tpsd2.insurance_id end) IN (:ids)");
				queryParams.put("ids", searchDto.getIds());
			} else if (searchDto.getPaymentEntitlementId() == ICommonConstants.COMPANY && searchDto.getIds() != null) {
				getDashBoardSearchQuery.append(" AND (case when order_master.encounter_id > 0 then tpsd.payee_id else tpsd2.payee_id end) IN (:ids)");
				queryParams.put("ids", searchDto.getIds());
			}
		}

		if (searchDto.getReferralDocId() != null) {
			getDashBoardSearchQuery.append(" AND (case when bill.encounter_id is not null then ")
					.append(" (select referal.referral_id from public.t_referal_details referal where referal.encounter_id = bill.encounter_id)")
					.append(" else ")
					.append(" (select referal.referral_id from public.t_referal_details referal where referal.admission_id = bill.admission_id)")
					.append(" end) = :referralDocId");
			queryParams.put("referralDocId", searchDto.getReferralDocId());
		}

		Query createdQuery = sessionFactory.getCurrentSession().createSQLQuery(getDashBoardSearchQuery.toString());
		queryParams.forEach(createdQuery::setParameter);

		List<LabDashBoardDto> listLabDashBoardDto = createdQuery
				.setParameter("orgId", searchDto.getOrgId())
				.setParameter("orgUnitId", searchDto.getOrgUnitId())
				.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
				.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
				.setResultTransformer(Transformers.aliasToBean(LabDashBoardDto.class))
				.list();

		if (listLabDashBoardDto.isEmpty()) {
			return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.", listLabDashBoardDto, null);
		} else {
			Query createdQuery1 = sessionFactory.getCurrentSession().createSQLQuery(getDashBoardSearchQuery.toString());
			queryParams.forEach(createdQuery1::setParameter);
			List<LabDashBoardDto> listLabDashBoardCountList = createdQuery1
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
					.setResultTransformer(Transformers.aliasToBean(LabDashBoardDto.class))
					.list();
			int count = listLabDashBoardCountList.size();
			return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, listLabDashBoardDto, count);
		}
    
	}catch(Exception e)
	{
		logger.error("Exection", e);
		throw e;
	}
	}
}

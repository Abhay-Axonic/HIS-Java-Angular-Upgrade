package com.param.lis.transaction.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.transaction.LabResultMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ServiceException;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.common.StringCommonMethods;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked","unused" })
public class ReportHandOverDaoImpl extends GenericDao<LabResultMaster, Integer>
implements IReportHandOverDao, ICommonConstants, ITransactionConstants, IError {

public ReportHandOverDaoImpl() 
{
  super(LabResultMaster.class);
}


@Autowired
ILabTransactionDao iLabTransactionDao;

final static Logger logger = Logger.getLogger(ReportHandOverDaoImpl.class);

@Override
public Response getReportHandList(BioChemParamDto bioChemParamDto) throws ApplicationException {
	List<LabResultMasterDto> listLabResultMasterDto = null;
	try {
		String baseQuery = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_BIOCHEM_REPORT_HAND_OVER_ENTRY_LIST")
				.getQueryString();
		StringBuilder queryBuilder = new StringBuilder(baseQuery);
		String startDate = GlobalCommonDateUtils.getStringDate(
				TimeZoneComponent.getDateByzone(bioChemParamDto.getOrgUnitId()), "yyyy-MM-dd");
		String endDate = GlobalCommonDateUtils.getStringDate(
				TimeZoneComponent.getDateByzone(bioChemParamDto.getOrgUnitId()), "yyyy-MM-dd");
		queryBuilder.append(" AND to_char(ord_mst.order_date, 'YYYY-MM-DD') >= :startDate ")
				.append(" AND to_char(ord_mst.order_date, 'YYYY-MM-DD') <= :endDate ")
				.append(" AND lbsampledtls.is_on_hold_parameter = false ")
				.append(" ORDER BY lbsampledtls.priority_id, ord_mst.order_date");
		String finalQuery = queryBuilder.toString();
		Query query = sessionFactory.getCurrentSession()
				.createSQLQuery(finalQuery)
				.setParameter("orgId", bioChemParamDto.getOrgId())
				.setParameter("orgUnitId", bioChemParamDto.getOrgUnitId())
				.setParameter("sampleStatusId", REPORT_RELEASED)
				.setParameter("deptId", bioChemParamDto.getDeptId())
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate);
		if (bioChemParamDto.getOffset() != null) {
			query.setFirstResult(bioChemParamDto.getOffset());
		} else {
			query.setFirstResult(0);
		}
		if (bioChemParamDto.getRecordPerPage() != null) {
			query.setMaxResults(bioChemParamDto.getRecordPerPage());
		} else {
			query.setMaxResults(10);
		}
		 listLabResultMasterDto = query.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class))
				.list();
		return new Response(SUCCESS, SUCCESS_200, null, listLabResultMasterDto, null);
	} catch (Exception e) {
		logger.error("Exception", e);
		throw e;
	}
}

@Override
public Response getReportHandListCount(BioChemParamDto bioChemParamDto) throws ApplicationException {
	try {
		String baseQuery = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_BIOCHEM_REPORT_HAND_OVER_COUNT")
				.getQueryString();
		StringBuilder queryBuilder = new StringBuilder(baseQuery);
		if (bioChemParamDto.getFromDate() != null) {
			queryBuilder.append(" AND to_char(lbsampledtls.updated_date, 'YYYY-MM-DD') >= :fromDate ");
		}

		if (bioChemParamDto.getToDate() != null) {
			queryBuilder.append(" AND to_char(lbsampledtls.updated_date, 'YYYY-MM-DD') <= :toDate ");
		}

		if (bioChemParamDto.getIsMailSend() != null) {
			queryBuilder.append(" AND lbsampledtls.is_mail_send = :isMailSend ");
		}
		String finalQuery = queryBuilder.toString();
		Query query = sessionFactory.getCurrentSession()
				.createSQLQuery(finalQuery)
				.setParameter("orgId", bioChemParamDto.getOrgId())
				.setParameter("sampleStatusId", REPORT_RELEASED)
				.setParameter("deptId", bioChemParamDto.getDeptId())
				.setParameter("orgUnitId", bioChemParamDto.getOrgUnitId());
		if (bioChemParamDto.getFromDate() != null) {
			query.setParameter("fromDate", bioChemParamDto.getFromDate());
		}
		if (bioChemParamDto.getToDate() != null) {
			query.setParameter("toDate", bioChemParamDto.getToDate());
		}
		if (bioChemParamDto.getIsMailSend() != null) {
			query.setParameter("isMailSend", bioChemParamDto.getIsMailSend());
		}
		BigInteger sampleResultEntryCount = (BigInteger) query.uniqueResult();

		if (sampleResultEntryCount.compareTo(BigInteger.ZERO) == 1) {
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, sampleResultEntryCount);
		} else {
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, BigInteger.ZERO);
		}

	} catch (Exception e) {
		logger.error("Exception", e);
		throw e;
	}
}

@Override
public Response reportHandOverListSearch(SearchDto searchDto) throws ApplicationException {
	List<LabResultMasterDto> listLabResultMasterDto = null;
	try {
		Response resp = iLabTransactionDao.getSpecialityAndSubSpecialityListByUserIdByUnitId(searchDto);
		List<Integer> deptIds = new ArrayList<>();
		List<Integer> subDeptIds = new ArrayList<>();
		List<SearchDto> specialityList = resp.getListObject() != null ? resp.getListObject() : new ArrayList<>();
		List<SearchDto> subSpecialityList = resp.getListObject1() != null ? resp.getListObject1() : new ArrayList<>();
		if (!specialityList.isEmpty()) {
			for (SearchDto dto : specialityList) {
				deptIds.add(dto.getSpecialityId());
			}
		} else {
			deptIds.add(searchDto.getDeptId());
		}

		if (!subSpecialityList.isEmpty()) {
			for (SearchDto dto2 : subSpecialityList) {
				subDeptIds.add(dto2.getSubSpecialityMasterId());
			}
		}
		searchDto.setSubDeptIds(subDeptIds);

		List<Integer>  testTypes = new ArrayList<Integer>();
		List<Integer>  visitTypes = new ArrayList<Integer>();
		List<Integer>  specimanTypes = new ArrayList<Integer>();

		if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0) {
			visitTypes = searchDto.getVisitTypes().stream()
					.map(obj -> obj.getId())
					.collect(Collectors.toList());
		}

		if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0) {
			subDeptIds = searchDto.getSubDepts().stream()
					.map(obj -> obj.getId())
					.collect(Collectors.toList());
		}

		if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0) {
			testTypes = searchDto.getTestTypes().stream()
					.map(obj -> obj.getId())
					.collect(Collectors.toList());
		}

		if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0) {
			specimanTypes = searchDto.getSpecimanTypes().stream()
					.map(obj -> obj.getId())
					.collect(Collectors.toList());
		}


		String baseQuery = sessionFactory.getCurrentSession()
				.getNamedQuery("GET_BIOCHEM_REPORT_HAND_OVER_ENTRY_LIST")
				.getQueryString();
		StringBuilder sb = new StringBuilder(baseQuery);
		if (searchDto.getFromDt() != null) {
			sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >= :fromDt ");
		}
		if (searchDto.getToDt() != null) {
			sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <= :toDt ");
		}
		if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
			sb.append(" AND pati_mst.patient_id = :patientId ");
		}
		if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()) {
			sb.append(" AND vistmst.visit_type_id IN (:visitTypes) ");
		}
		if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()) {
			sb.append(" AND test_mst.dept_id IN (:subDepts) ");
		}
		if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()) {
			sb.append(" AND test_mst.test_id IN (:testTypes) ");
		}
		if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0) {
			sb.append(" AND lbsampledtls.lab_sample_dtls_id = :labSampleDtlsId ");
		}
		if (searchDto.getIsMailSend() != null) {
			sb.append(" AND lbsampledtls.is_mail_send = :isMailSend ");
		}
		if (searchDto.getUhidNumber() != null && !searchDto.getUhidNumber().isEmpty()) {
			sb.append(" AND LOWER(pati_mst.uhid_number) = :uhidNumber ");
		}
		sb.append(" AND lbsampledtls.is_on_hold_parameter = false ");
		if (searchDto.getLabSortingId() != null) {
			switch (searchDto.getLabSortingId()) {
				case 1:
					sb.append(" ORDER BY lbsampledtls.patient_id, lbsampledtls.updated_date DESC ");
					break;
				case 2:
					sb.append(" ORDER BY ord_mst.order_id DESC ");
					break;
				case 3:
					sb.append(" ORDER BY sample_mst.sample_id DESC ");
					break;
				case 4:
					sb.append(" ORDER BY lbsampledtls.updated_date DESC ");
					break;
				default:
					sb.append(" ORDER BY lbsampledtls.patient_id ");
			}
		} else {
			sb.append(" ORDER BY lbsampledtls.patient_id ");
		}
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
				.setParameter("orgId", searchDto.getOrgId())
				.setParameter("orgUnitId", searchDto.getOrgUnitId())
				.setParameter("sampleStatusId", REPORT_RELEASED)
				.setParameterList("deptId", deptIds)
				.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
				.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50);
		if (searchDto.getFromDt() != null) {
			query.setParameter("fromDt", searchDto.getFromDt());
		}
		if (searchDto.getToDt() != null) {
			query.setParameter("toDt", searchDto.getToDt());
		}
		if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
			query.setParameter("patientId", searchDto.getPatientId());
		}
		if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()) {
			query.setParameterList("visitTypes", visitTypes);
		}
		if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()) {
			query.setParameterList("subDepts", subDeptIds);
		}
		if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()) {
			query.setParameterList("testTypes", testTypes);
		}
		if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0) {
			query.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId());
		}
		if (searchDto.getIsMailSend() != null) {
			query.setParameter("isMailSend", searchDto.getIsMailSend());
		}
		if (searchDto.getUhidNumber() != null && !searchDto.getUhidNumber().isEmpty()) {
			query.setParameter("uhidNumber", searchDto.getUhidNumber().toLowerCase());
		}
		listLabResultMasterDto = query.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
		return new Response(SUCCESS, SUCCESS_200, null, listLabResultMasterDto, null);
	} catch (Exception e) {
		logger.error("Exception", e);
		throw e;
	}
}

@Override
public Response updateLabsampDetails(List<LabResultMasterDto> list) throws ApplicationException {
	try{	
		for (LabResultMasterDto labResultMasterDto : list) {
			Session session = sessionFactory.getCurrentSession();
			Integer count = session.createSQLQuery(
							"UPDATE lab.t_lab_sample_dtls " +
									"SET print = :print, re_print = :rePrint " +
									"WHERE lab_sample_dtls_id = :labSampleDtlsId")
					.setParameter("print", labResultMasterDto.getPrint())
					.setParameter("rePrint", labResultMasterDto.getRePrint())
					.setParameter("labSampleDtlsId", labResultMasterDto.getLabSampleDtlsId())
					.executeUpdate();
		}
		   return new Response(SUCCESS, "", null, null, null);

	} catch (HibernateException he) {
		logger.error("Exception in updateLabsampDetails", he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error("Exception in updateLabsampDetails", e);
		throw new ServiceException(e.getMessage());
	}
}

@Override
public Response saveReportHandOverImage(LabResultMasterDto dto) {
	try
    {
		if(dto.getOrderDetailsId() == 0) {
			Integer count = sessionFactory.getCurrentSession()
					.createSQLQuery(
							"UPDATE LAB.t_lab_sample_dtls " +
									"SET p_image = :imagePath, p_image_name = :fileName " +
									"WHERE lab_sample_dtls_id = :labSampleDtlsId")
					.setParameter("imagePath", dto.getImagePath())
					.setParameter("fileName", dto.getFileName())
					.setParameter("labSampleDtlsId", dto.getLabSampleDtlsId())
					.executeUpdate();

		}else{
			Integer count = sessionFactory.getCurrentSession()
					.createSQLQuery(
							"UPDATE public.t_order_details " +
									"SET p_image = :imagePath, p_image_name = :fileName " +
									"WHERE order_details_id = :orderDetailsId")
					.setParameter("imagePath", dto.getImagePath())
					.setParameter("fileName", dto.getFileName())
					.setParameter("orderDetailsId", dto.getOrderDetailsId())
					.executeUpdate();

		}
		
		
			return new Response<>(SUCCESS, null, null, null, null);

    } catch (Exception e)
    {
        logger.error("Exception in saveReportHandOverImage", e);
        throw new ServiceException(e.getMessage());
    }
}

@Override
public Response reportHandOverListTypeSearch(SearchDto searchDto) throws ApplicationException {
	List<LabResultMasterDto> listLabResultMasterDto = null;
	try {

		String query = 	sessionFactory.getCurrentSession().getNamedQuery("GET_BIOCHEM_REPORT_HAND_OVER_ENTRY_LIST").getQueryString();
		StringBuffer sb = new StringBuffer(query);

		if (searchDto.getFromDt() != null ) {
			sb.append(
					" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt  ");
		}

		if (searchDto.getToDt() != null ) {
			sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt ");
		}

		if (searchDto.getIsMailSend()!= null) {
			sb.append(" AND lbsampledtls.is_mail_send =:isMailSend " );
		}

		if(searchDto.getSearchField()!="") {
			sb.append(" AND (lower(pati_mst.uhid_number) LIKE :searchField OR "
					+ " lower(sample_mst.sample_id) LIKE :searchField OR "
					+ " lower(pati_mst.first_name) LIKE :searchField OR "
					+ " lower(pati_mst.middle_name) LIKE :searchField OR "
					+ " lower(pati_mst.last_name) LIKE :searchField OR "
					+ " lower(test_mst.test_desc) LIKE :searchField OR"
					+ " lower(test_mst.test_code) LIKE :searchField OR "
					+ " lower(subspe_mst.sub_speciality_name) LIKE :searchField )");
		}

		sb.append(" AND lbsampledtls.is_on_hold_parameter = false ");
		if (searchDto.getLabSortingId() != null) {
			if (searchDto.getLabSortingId() == 1) {
				sb.append(" ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
			} else if (searchDto.getLabSortingId() == 2) {
				sb.append(" ORDER BY    ord_mst.order_id DESC ");
			} else if (searchDto.getLabSortingId() == 3) {
				sb.append(" ORDER BY    sample_mst.sample_id  DESC ");
			} else if (searchDto.getLabSortingId() == 4) {
				sb.append(" ORDER BY  lbsampledtls.updated_date DESC ");
			}
		} else {
			sb.append(" ORDER BY  ord_mst.order_date DESC ");
		}

		Query hqlQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
				.setInteger("orgId", searchDto.getOrgId())
				.setInteger("orgUnitId", searchDto.getOrgUnitId())
				.setInteger("sampleStatusId", REPORT_RELEASED)
				.setInteger("deptId", searchDto.getDeptId())
				.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
				.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50);

		if (searchDto.getFromDt() != null) {
			hqlQuery.setParameter("fromDt", searchDto.getFromDt());
		}
		if (searchDto.getToDt() != null) {
			hqlQuery.setParameter("toDt", searchDto.getToDt());
		}
		if (searchDto.getIsMailSend() != null) {
			hqlQuery.setParameter("isMailSend", searchDto.getIsMailSend());
		}
		if (searchDto.getSearchField() != null && !searchDto.getSearchField().isEmpty()) {
			String searchFieldValue = "%" + searchDto.getSearchField().toLowerCase() + "%";
			hqlQuery.setParameter("searchField", searchFieldValue);
		}


		listLabResultMasterDto =	hqlQuery.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
		return new Response(SUCCESS, SUCCESS_200, null, listLabResultMasterDto, null);
	} catch (Exception e) {
		logger.error("Exception", e);
		throw e;
	}



}

@Override
public Response bulkEmailWorklistPatientSearch(SearchDto searchDto) throws ApplicationException {
	try {
		List<LabResultMasterDto> listLabResultMasterDto = null;
		String query = sessionFactory.getCurrentSession().getNamedQuery("BULK_EMAIL_WORKLIST_PATIENT_SEARCH").getQueryString();
		StringBuilder sb = new StringBuilder(query);

		if (searchDto.getFromDt() != null) {
			sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >= :fromDt");
		}
		if (searchDto.getToDt() != null) {
			sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <= :toDt");
		}
		if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
			sb.append(" AND pati_mst.patient_id = :patientId");
		}
		if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()) {
			sb.append(" AND vistmst.visit_type_id IN (:visitTypes)");
		}
		if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()) {
			sb.append(" AND test_mst.dept_id IN (:subDepts)");
		}
		if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()) {
			sb.append(" AND test_mst.test_id IN (:testTypes)");
		}
		if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0) {
			sb.append(" AND lbsampledtls.lab_sample_dtls_id = :labSampleDtlsId");
		}
		if (searchDto.getIsMailSend() != null) {
			sb.append(" AND lbsampledtls.is_mail_send = :isMailSend");
		}
		if (searchDto.getPaymentEntitlementId() != null) {
			if (searchDto.getPaymentEntitlementId() == ICommonConstants.INSURANCE) {
				if (searchDto.getIds() != null) {
					sb.append(" AND (CASE WHEN ord_mst.encounter_id > 0 THEN tpsd.insurance_id ELSE tpsd2.insurance_id END) IN (:ids)");
				}
			} else if (searchDto.getPaymentEntitlementId() == ICommonConstants.COMPANY) {
				if (searchDto.getIds() != null) {
					sb.append(" AND (CASE WHEN ord_mst.encounter_id > 0 THEN tpsd.payee_id ELSE tpsd2.payee_id END) IN (:ids)");
				}
			}
		}

		sb.append(" ORDER BY ");
		if (searchDto.getLabSortingId() != null) {
			switch (searchDto.getLabSortingId()) {
				case 1: sb.append(" lbsampledtls.patient_id, lbsampledtls.updated_date DESC"); break;
				case 2: sb.append(" ord_mst.order_id DESC"); break;
				case 3: sb.append(" sample_mst.sample_id DESC"); break;
				case 4: sb.append(" lbsampledtls.updated_date DESC"); break;
				default: sb.append(" lbsampledtls.patient_id"); break;
			}
		} else {
			sb.append(" lbsampledtls.patient_id");
		}

		Query createQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
				.setInteger("orgId", searchDto.getOrgId())
				.setInteger("orgUnitId", searchDto.getOrgUnitId())
				.setInteger("sampleStatusId", REPORT_RELEASED)
				.setInteger("deptId", searchDto.getDeptId())
				.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
				.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50);

		List<Integer>  subDeptIds = new ArrayList<Integer>();
		List<Integer>  testTypes = new ArrayList<Integer>();
		List<Integer>  visitTypes = new ArrayList<Integer>();
		List<Integer>  iDs = new ArrayList<Integer>();

		if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0) {
			visitTypes = searchDto.getVisitTypes().stream()
					.map(obj -> obj.getId())
					.collect(Collectors.toList());
		}

		if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0) {
			subDeptIds = searchDto.getSubDepts().stream()
					.map(obj -> obj.getId())
					.collect(Collectors.toList());
		}

		if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0) {
			testTypes = searchDto.getTestTypes().stream()
					.map(obj -> obj.getId())
					.collect(Collectors.toList());
		}

		if (searchDto.getIds() != null && searchDto.getIds().size()>0) {
			iDs = searchDto.getIds().stream()
					.map(obj -> obj.getId())
					.collect(Collectors.toList());
		}


		if (searchDto.getFromDt() != null) createQuery.setParameter("fromDt", searchDto.getFromDt());
		if (searchDto.getToDt() != null) createQuery.setParameter("toDt", searchDto.getToDt());
		if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) createQuery.setParameter("patientId", searchDto.getPatientId());
		if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()) createQuery.setParameterList("visitTypes", visitTypes);
		if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()) createQuery.setParameterList("subDepts", subDeptIds);
		if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()) createQuery.setParameterList("testTypes", testTypes);
		if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0) createQuery.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId());
		if (searchDto.getIsMailSend() != null) createQuery.setParameter("isMailSend", searchDto.getIsMailSend());
		if (searchDto.getPaymentEntitlementId() != null && searchDto.getIds() != null) createQuery.setParameterList("ids",iDs);

		listLabResultMasterDto = createQuery.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_200, null, listLabResultMasterDto, listLabResultMasterDto.size());
	} catch (Exception e) {
		logger.error("Exception", e);
		throw e;
	}
}


@Override
public Response getReportHandOverUrl(List<Integer> labSampleDtlsIdsList, Integer orgUnitId, Integer orgId) {
	try {
		
		String query = "select test.lab_report_url as \"labReportUrl\" "
				+ " from lab.t_lab_sample_dtls test "
				+ " where test.lab_sample_dtls_id in (:list) "
				+ " and test.org_id =:orgId "
				+ " and test.org_unit_id =:unitId";
		
		List<SmsUtilityConfigInfoDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
				.setParameterList("list", labSampleDtlsIdsList)
				.setParameter("orgId", orgId)
				.setParameter("unitId", orgUnitId)
				.setResultTransformer(Transformers.aliasToBean(SmsUtilityConfigInfoDto.class)).list();
		
		 return new Response(SUCCESS, SUCCESS_200_MESSAGE,null, list, null);
	} catch (HibernateException he) {
		throw new HibernateException(he.getMessage());
	} catch (Exception e) {
		logger.error("Exception in getReportHandOverUrl", e);
		throw new ServiceException(e.getMessage());
	}
}

}

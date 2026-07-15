package com.param.lis.transaction.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.param.lis.global.common.*;
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.transaction.LabResultDetailsMaster;
import com.param.entity.lis.transaction.LabResultMaster;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.unit.dao.ILabConfigurationDao;
import com.param.global.unit.dto.LabConfigurationDto;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.LabResultDetailsViewDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BiochemValidationWorklistDaoImpl extends GenericDao<LabResultMaster, Integer>
implements IBiochemValidationWorklistDao, ICommonConstants, ITransactionConstants, IError {

public BiochemValidationWorklistDaoImpl() {
super(LabResultMaster.class);
}

@Autowired
private DozerBeanMapper mapper;

@Autowired
ILabConfigurationDao iLabConfigurationDao;

@Autowired
ILabTransactionDao iLabTransactionDao;

final static Logger logger = Logger.getLogger(BiochemValidationWorklistDaoImpl.class);

@Override
public Response getValidationWorkList(BioChemParamDto bioChemParamDto) throws ApplicationException {
	List<LabResultMasterDto> listLabResultMasterDto = null;
	try {
		
		String query = 	sessionFactory.getCurrentSession().getNamedQuery("GET_BIOCHEM_VALIDATION_ENTRY").getQueryString();  
		StringBuffer sb = new StringBuffer(query);
		sb.append("  and lbsampledtls.assign_unit  =:orgUnitId");
		sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') >=:currentDate ");
		sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') <=:currentDate ");
		sb.append( " GROUP BY  " 
		        + "  labresmst.lab_test_res_id,  "
		        + "  bill_mst.bill_number, " 
		        + "  lbsampledtls.lab_sample_dtls_id,  "
		        + "  sample_mst.sample_no,  "
		        + "  visit_type_master.visit_type_id,  " 
		        + "  pati_mst.patient_id,  "
		        + "  test_mst.test_id,  " 
		        + "  pre_mst.prefix_id,  " 
		        + "  gen_mst.gender_id,  "
		        + "  doct_mst.doctor_id,  " 
		        + "  pri_mst.priority_id,  " 
		        + "  wardmast.ward_id,  "
		        + "  bedmst.bed_id,  " 
		        + "  samp_mst.sample_id , " 
		        + "  panel_master.panel_id,  " 
		        + "  speciality_master.speciality_id,  "
		        + "  order_details.order_details_id ,"
		        + " order_master.encounter_id ,"  
		         + "  order_master.admission_id " 
		       // + " retest.retestcount "
		        
		        + " ORDER BY  " 
		        + "  lbsampledtls.priority_id,  "
		        + "  lbsampledtls.created_date  ");
		
		listLabResultMasterDto = sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
				.setParameter("orgId", bioChemParamDto.getOrgId())
				.setParameter("orgUnitId", bioChemParamDto.getOrgUnitId())
//				.setParameter("sampleStatusId", RESULT_ENTRY_DONE)
				.setParameter("deptId", bioChemParamDto.getDeptId())
				.setParameter("subDeptId", bioChemParamDto.getSubDeptId())
				.setParameter("currentDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(bioChemParamDto.getOrgUnitId()),"yyyy-MM-dd"))
				.setFirstResult(bioChemParamDto.getOffset() != null ? bioChemParamDto.getOffset() : 0)
				.setMaxResults(bioChemParamDto.getRecordPerPage() != null ? bioChemParamDto.getRecordPerPage() : 10)
				.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
		return new Response(SUCCESS, SUCCESS_200, null, listLabResultMasterDto, null);
	} catch (Exception e) {
		logger.error("Exception", e);
		throw e;
	}
}

@Override
public Response getValidationWorkListCount(BioChemParamDto bioChemParamDto) throws ApplicationException {
	try {
		String query = 	sessionFactory.getCurrentSession().getNamedQuery("GET_BIOCHEM_RESULT_ENTRY_LIST_COUNT").getQueryString();  
		StringBuffer sb = new StringBuffer(query);
		//sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') >= '" + GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId),"yyyy-MM-dd") + "' ");
		//sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') <= '" + GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId),"yyyy-MM-dd") + "' ");
		List<Integer> statusIds = new ArrayList<Integer>(); 
	      if(bioChemParamDto.getIsOnHoldParameter() == false) {
	   			statusIds = Arrays.asList(RESULT_ENTRY_DONE);
	      }
		if (bioChemParamDto.getFromDate() != null ) 
        {
            sb.append( " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDate ");
        }

        if (bioChemParamDto.getToDate() != null ) {
            sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDate ");
        }
        if (bioChemParamDto.getIsOnHoldParameter() == true) {
      	  statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
      	  /*statusIds.add(RESULT_ENTRY_DONE);
      	  statusIds.add(RESULT_VALIDATION_DONE);
      	  statusIds.add(REPORT_RELEASED);
      	  statusIds.add(PARTIAL_REPORT_RELEASED);
      	  statusIds.add(PROVISIONAL_REPORT_RELEASED);*/
      	  sb.append(" and lbsampledtls.sample_status_id IN(:statusIds)");
            sb.append(" AND lbsampledtls.is_on_hold_parameter =:isOnHoldParameter ");
        }
        if (statusIds != null && statusIds.size() > 0 && bioChemParamDto.getIsOnHoldParameter() == false) {
            sb.append(" and lbsampledtls.sample_status_id IN(:statusIds)");
        }
		sb.append(" AND lbsampledtls.assign_unit  =:assignUnit");
		
		Query sql = sessionFactory.getCurrentSession()
				.createSQLQuery(sb.toString())
				.setParameter("orgId", bioChemParamDto.getOrgId())
//				.setParameter("sampleStatusId", RESULT_ENTRY_DONE)
				.setParameter("deptId", bioChemParamDto.getDeptId())
				.setParameter("subDeptId", bioChemParamDto.getSubDeptId())
				.setParameter("assignUnit", bioChemParamDto.getAssignUnit())
				.setParameter("orgUnitId", bioChemParamDto.getOrgUnitId());
		if (bioChemParamDto.getFromDate() != null )
		{
			sql.setParameter("fromDate", bioChemParamDto.getFromDate());
		}

		if (bioChemParamDto.getToDate() != null ) {
			sql.setParameter("toDate", bioChemParamDto.getToDate());
		}
		if (bioChemParamDto.getIsOnHoldParameter() == true) {
			statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
			sql.setParameterList("statusIds", statusIds);
			sql.setParameter("isOnHoldParameter", bioChemParamDto.getIsOnHoldParameter());
		}
		if (statusIds != null && statusIds.size() > 0 && bioChemParamDto.getIsOnHoldParameter() == false) {
			sql.setParameterList("statusIds", statusIds);
		}
		BigInteger sampleResultEntryCount = (BigInteger) sql.uniqueResult();
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
public Response getValidationWorkListDetails(BioChemParamDto bioChemParamDto) throws ApplicationException 
{
	try {
		String GET_REPORT_DETTAILS = sessionFactory.getCurrentSession().getNamedQuery("GET_LAB_RESULT_DETAILS").getQueryString();
		List<LabResultDetailsViewDto> listLabResultDetailsMasterDto = null;
			if(bioChemParamDto.getTestType()==SINGLE_PARAM_TEST)
			{
				List<LabResultDetailsViewDto> listLabResultDetailsMstDto = new ArrayList<>();
				LabResultDetailsViewDto labResultDetailsMasterDto = (LabResultDetailsViewDto) sessionFactory.getCurrentSession().createQuery(GET_REPORT_DETTAILS)
						.setParameter("orgId", bioChemParamDto.getOrgId())
						//.setParameter("orgUnitId", bioChemParamDto.getOrgUnitId())
						.setParameter("labResultId", bioChemParamDto.getLabResultId())
						.setResultTransformer(Transformers.aliasToBean(LabResultDetailsViewDto.class)).uniqueResult();
				listLabResultDetailsMstDto.add(labResultDetailsMasterDto);
				return new Response(SUCCESS, SUCCESS_200, null, listLabResultDetailsMstDto, null);	
			}
			else if(bioChemParamDto.getTestType()==MULTY_PARAM_TEST)
			{
				StringBuilder GET_REPORT_DETTAILS_MULTYPARAM  = new StringBuilder(GET_REPORT_DETTAILS);
				//.append(" AND labResultDetailsMaster.headerId= :headerId");
				listLabResultDetailsMasterDto = sessionFactory.getCurrentSession().createQuery(GET_REPORT_DETTAILS_MULTYPARAM.toString())
						.setParameter("orgId", bioChemParamDto.getOrgId())
						//.setParameter("orgUnitId", bioChemParamDto.getOrgUnitId())
					//	.setParameter("headerId", bioChemParamDto.getHeaderId())
						.setParameter("labResultId", bioChemParamDto.getLabResultId())
						.setResultTransformer(Transformers.aliasToBean(LabResultDetailsViewDto.class)).list();
				return new Response(SUCCESS, SUCCESS_200, null, listLabResultDetailsMasterDto, null);
			}
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	} catch (Exception e) {
		logger.error("Exception", e);
		throw e;
	}
}

@Override
public Response saveValidationWorkList(List<LabResultMasterDto> listLabResultMasterDto) throws ApplicationException {
	try {
	  for (Iterator iterator = listLabResultMasterDto.iterator(); iterator.hasNext();) {
	    LabResultMasterDto labResultMasterDto = (LabResultMasterDto) iterator.next();
		LabResultMaster labResultMaster = mapper.map(labResultMasterDto, LabResultMaster.class,
				"LabResultMasterDtoToLabResultMaster");
		labResultMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(labResultMasterDto.getOrgUnitId()).getTime());
		labResultMaster.setUpdatedBy(labResultMasterDto.getCreatedBy());
		labResultMaster.setResultValidatedBy(labResultMasterDto.getCreatedBy());
		labResultMaster.setManualResultEnterTime(labResultMaster.getResultEnterDatetime());
		labResultMaster.setSampleNo(labResultMasterDto.getSampleNo());
		labResultMaster.setResultValidatedDatetime(TimeZoneComponent.getDateByzone(labResultMasterDto.getOrgUnitId()).getTime());
		labResultMaster.setManualValidationTime(TimeZoneComponent.getDateByzone(labResultMasterDto.getOrgUnitId()).getTime());
		labResultMaster.setCreatedDate(labResultMasterDto.getCreatedDate());
		labResultMaster.setCreatedBy(labResultMasterDto.getCreatedBy());
		labResultMaster.setReportType(labResultMasterDto.getReportType());
		labResultMaster.setStatus('A');
		LabResultMaster labResultMst = update(labResultMaster);
		LabSampleDetailsMaster labSampleDetailsMaster = (LabSampleDetailsMaster) sessionFactory.getCurrentSession()
				.get(LabSampleDetailsMaster.class, labResultMst.getLabSampleDtlsId());
		
		labSampleDetailsMaster.setUpdatedBy(labResultMst.getUpdatedBy());
		labSampleDetailsMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(labResultMasterDto.getOrgUnitId()).getTime());
		labSampleDetailsMaster.setSampleStatusId(RESULT_VALIDATION_DONE);
		labSampleDetailsMaster.setProvisional(labResultMasterDto.getProvisional()!= null ? labResultMasterDto.getProvisional() : false);
		labSampleDetailsMaster.setIsOnHoldParameter(labResultMasterDto.getIsOnHoldParameter());
		sessionFactory.getCurrentSession().update(labSampleDetailsMaster);
			for (Iterator iterator1 = labResultMst.getListLabSampleResultDetailsMaster().iterator(); iterator1
					.hasNext();) {
				LabResultDetailsMaster labResultDetailsMaster = (LabResultDetailsMaster) iterator1.next();
				labResultDetailsMaster.setLabResultId(labResultMst.getLabTestResId());
	            labSampleDetailsMaster.setInternalNotes(labResultMasterDto.getInternalNotes());
	            labSampleDetailsMaster.setSrfId(labResultMasterDto.getSrfId());
	    		labSampleDetailsMaster.setIcmrId(labResultMasterDto.getIcmrId());
				labResultDetailsMaster.setSecondLevelResult(labResultDetailsMaster.getFinalResult());
				labResultDetailsMaster.setUpdatedBy(labResultMasterDto.getCreatedBy());
				labResultDetailsMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(labResultDetailsMaster.getOrgUnitId()).getTime());
				labSampleDetailsMaster.setPerformedUnit(labResultMst.getOrgUnitId());
				sessionFactory.getCurrentSession().update(labResultDetailsMaster);
		}
	  }
			return new Response(SUCCESS, SUCCESS_200, SAMPLE_RESULT_ENTRY_VALIDATION_SUCC, null, null);
	} catch (Exception e) {
		logger.error("Exception", e);
		return new Response(ERROR, ERR_500, SAMPLE_RESULT_ENTRY_VALIDATION_FAIL, null, null);
	}
}

@Override
public Response getPrivousResultBySample(BioChemParamDto bioChemParamDto) throws ApplicationException {
	try {
		String GET_REPORT_DETTAILS = sessionFactory.getCurrentSession().getNamedQuery("GET_LAB_RESULT_DETAILS").getQueryString();
		List<LabResultDetailsViewDto> listLabResultDetailsMasterDto = null;
			if(bioChemParamDto.getTestType()==SINGLE_PARAM_TEST)
			{
				List<LabResultDetailsViewDto> listLabResultDetailsMstDto = new ArrayList<>();
				LabResultDetailsViewDto labResultDetailsMasterDto = (LabResultDetailsViewDto) sessionFactory.getCurrentSession().createQuery(GET_REPORT_DETTAILS)
						.setParameter("orgId", bioChemParamDto.getOrgId())
						//.setParameter("orgUnitId", bioChemParamDto.getOrgUnitId())
						.setParameter("labResultId", bioChemParamDto.getLabResultId())
						.setResultTransformer(Transformers.aliasToBean(LabResultDetailsViewDto.class)).uniqueResult();
				listLabResultDetailsMstDto.add(labResultDetailsMasterDto);
				return new Response(SUCCESS, SUCCESS_200, null, listLabResultDetailsMstDto, null);	
			}
			else if(bioChemParamDto.getTestType()==MULTY_PARAM_TEST)
			{
				String GET_REPORT_DETTAILS_FOR_MULTIPARAMETER = sessionFactory.getCurrentSession().getNamedQuery("GET_LAB_RESULT_DETAILS_FOR_MULTIPARAMETER").getQueryString();
				
				StringBuilder GET_REPORT_DETTAILS_MULTYPARAM  = new StringBuilder(GET_REPORT_DETTAILS_FOR_MULTIPARAMETER).append(" AND labResultDetailsMaster.headerId= :headerId order by labResultDetailsMaster.labResDtlsId asc ");
				listLabResultDetailsMasterDto = sessionFactory.getCurrentSession().createQuery(GET_REPORT_DETTAILS_MULTYPARAM.toString())
						.setParameter("orgId", bioChemParamDto.getOrgId())
						//.setParameter("orgUnitId", bioChemParamDto.getOrgUnitId())
						.setParameter("headerId", bioChemParamDto.getHeaderId())
						.setParameter("labResultId", bioChemParamDto.getLabResultId())
						.setResultTransformer(Transformers.aliasToBean(LabResultDetailsViewDto.class)).list();
				return new Response(SUCCESS, SUCCESS_200, null, listLabResultDetailsMasterDto, null);
			}
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	} catch (Exception e) {
		logger.error("Exception", e);
		throw e;
	}
}

@Override
public Response labValidationWorklistSearch(SearchDto searchDto) throws ApplicationException {
	try {

	      String query = sessionFactory.getCurrentSession()
	          .getNamedQuery("GET_BIOCHEM_VALIDATION_ENTRY").getQueryString().toString();

	      StringBuilder sb = new StringBuilder(query);
	      List<Integer> statusIds = new ArrayList<Integer>(); 
	      if(searchDto.getIsOnHoldParameter() == false) {
	   			statusIds = Arrays.asList(RESULT_ENTRY_DONE);
	      }
	      
	      if (searchDto.getFromDt() != null ) {
	    	  sb.append(
	            " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt ");
	      }

	      if (searchDto.getToDt() != null ) {
	    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt ");
	      }

	      if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
	    	  sb
	            .append(" AND pati_mst.patient_id =:patientId ");

	      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0)
	    	  sb.append(" AND visit_type_master.visit_type_id IN (:visitTypes)");

	      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0)
	    	  sb.append(" AND test_mst.dept_id IN (:subDepts)");
	      
	      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size() > 0)
	    	  sb.append(" AND test_mst.test_id in (:testTypes)");

	      if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0)
	    	  sb
	            .append(" AND lbsampledtls.lab_sample_dtls_id =:labSampleDtlsId ");

	      if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
	          sb.append("  and lbsampledtls.assign_unit  =:assignUnit ");
	      
          if(searchDto.getUhidNumber()!="") {
              sb.append(" AND lower(pati_mst.uhid_number) LIKE concat('%', LOWER(:uhidNumber), '%') ");
          }
          
          if(searchDto.getSampleNo()!="") {
              sb.append(" AND lower(sample_mst.sample_no) LIKE concat('%',LOWER(:sampleNo) ");
          }
          if (searchDto.getIsOnHoldParameter() == true) {
        	  statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
        	  sb.append(" and lbsampledtls.sample_status_id IN(:statusIds)");
              sb.append(" AND lbsampledtls.is_on_hold_parameter =:isOnHoldParameter ");
          }
          if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
              sb.append(" and (lbsampledtls.sample_status_id IN(:statusIds) or lbsampledtls.is_on_hold_parameter = true)");
          }
	      
	      sb.append( " GROUP BY  " 
			        + "  labresmst.lab_test_res_id, "
			        + "  bill_mst.bill_number, "  
			        + "  lbsampledtls.lab_sample_dtls_id,  "
			        + "  sample_mst.sample_no,  "
			        + "  visit_type_master.visit_type_id,  " 
			        + "  pati_mst.patient_id,  "
			        + "  test_mst.test_id,  " 
			        + "  pre_mst.prefix_id,  " 
			        + "  gen_mst.gender_id,  "
			        + "  doct_mst.doctor_id,  " 
			        + "  wardmast.ward_id,  "
			        + "  bedmst.bed_id,  " 
			        + "  samp_mst.sample_id , " 
			        + "  panel_master.panel_id,  " 
			        + "  speciality_master.speciality_id,  "
			        + "  order_details.order_details_id ," 
			        + " order_master.encounter_id ," + 
			          "  order_master.admission_id ,"+
			          " tpsd.payment_entitlement_id,"+
			          " tpsd2.payment_entitlement_id,"
			         + " order_master.order_id,"
			         + " sample_mst.sample_id ");
			        // + " retest.retestcount "
	      
			if (searchDto.getLabSortingId() != null) {
				if (searchDto.getLabSortingId() == 1) {
					sb.append("ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
				} else if (searchDto.getLabSortingId() == 2) {
					sb.append("ORDER BY    order_master.order_id DESC ");
				} else if (searchDto.getLabSortingId() == 3) {
					sb.append("ORDER BY   sample_mst.sample_id  DESC ");
				} else if (searchDto.getLabSortingId() == 4) {
					sb.append("ORDER BY    lbsampledtls.updated_date DESC ");
				}
			} else {
				sb.append("ORDER BY    lbsampledtls.patient_id,    lbsampledtls.updated_date  DESC ");
			}
	      
	      Query createdQuery = sessionFactory.getCurrentSession()
	          .createSQLQuery(sb.toString());
		if (searchDto.getFromDt() != null ) {
			createdQuery.setParameter("fromDt", searchDto.getFromDt());
		}

		if (searchDto.getToDt() != null ) {
			createdQuery.setParameter("toDt", searchDto.getToDt());
		}

		if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
			createdQuery.setParameter("patientId", searchDto.getPatientId());
		}

		if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0){
			List<obj> visitTypes = searchDto.getVisitTypes();
			List<Integer> visitTypeIds = visitTypes.stream()
					.map(obj::getId)
					.collect(Collectors.toList());
			createdQuery.setParameterList("visitTypes", visitTypeIds);
		}

		if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0){
			List<obj> subDepts = searchDto.getSubDepts();
			List<Integer> subDeptIds = subDepts.stream()
					.map(obj::getId)
					.collect(Collectors.toList());
			createdQuery.setParameterList("subDepts", subDeptIds);
		}

		if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size() > 0){
			List<obj> testTypes = searchDto.getTestTypes();
			List<Integer> testTypeIds = testTypes.stream()
					.map(obj::getId)
					.collect(Collectors.toList());
			createdQuery.setParameterList("testTypes", testTypeIds);
		}
			sb.append(" AND test_mst.test_id in ( " +StringCommonMethods.convertToStringJoiner( searchDto.getTestTypes())+")");

		if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0){
			createdQuery.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId());
		}

		if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0){
			createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());
		}

		if(searchDto.getUhidNumber()!="") {
			createdQuery.setParameter("uhidNumber", searchDto.getUhidNumber());
		}

		if(searchDto.getSampleNo()!="") {
			createdQuery.setParameter("sampleNo", searchDto.getSampleNo());
		}
		if (searchDto.getIsOnHoldParameter() == true) {
			createdQuery.setParameterList("statusIds", statusIds);
			createdQuery.setParameter("isOnHoldParameter", searchDto.getIsOnHoldParameter());
		}
		if (statusIds != null && statusIds.size() > 0  && searchDto.getIsOnHoldParameter() == false) {
			createdQuery.setParameterList("statusIds", statusIds);
		}

	      List<LabResultMasterDto>  list = createdQuery
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
					.setParameter("deptId", searchDto.getDeptId())
					.setParameter("subDeptId", searchDto.getSubDeptId())
					.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
					.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
					.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();


	      if (list.isEmpty()) {
	        return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.",
	        		list, null);
	      } else {
	    	  
              String sql="    select count(sample_dtls_id) as \"retestCount\"  , sample_dtls_id as \"labSampleDtlsId\"  from lab.t_sample_audit_trail sample_audit_trail  "
                      + "where test_status_id = 7 and sample_audit_trail.org_id =:orgId  group by sample_dtls_id  ";
              List<LabResultMasterDto> list1  = sessionFactory.getCurrentSession()
                        .createSQLQuery(sql)
			  			.setParameter("orgId", searchDto.getOrgId())
                      .setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
              
              
               for (LabResultMasterDto labResultMasterDto : list) {
                
                  LabResultMasterDto retestDto = list1.stream()
                         .filter(order -> labResultMasterDto.getLabSampleDtlsId().equals(order.getLabSampleDtlsId()))
                         .findAny()
                         .orElse(null);
                  
                  if(retestDto != null){
                      labResultMasterDto.setRetestCount(retestDto.getRetestCount()!= null ? retestDto.getRetestCount() : null);
                  }
                  
               } 
	    	  
	        return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, list,
	            null);
	      }


	    } catch (Exception e) {
	      logger.error("Exection", e);
	      throw e;
	    }
  }

@Override
public Response consolidatedWorklistSearch(SearchDto searchDto) throws ApplicationException {
	try {

		
		
	    Response resp = iLabTransactionDao.getSpecialityAndSubSpecialityListByUserIdByUnitId(searchDto);
        List<Integer> deptIds = new ArrayList<>();
        List<Integer> subDeptIds = new ArrayList<>();
        List<SearchDto> specialityList = new ArrayList<>();
        List<SearchDto> subSpecialityList = new ArrayList<>();
		List<Integer>  testTypes = new ArrayList<Integer>();
		List<Integer>  visitTypes = new ArrayList<Integer>();
		List<Integer>  subDepts = new ArrayList<Integer>();

        if(resp.getListObject().size() > 0 && resp.getListObject() != NULL) {
	    	specialityList = resp.getListObject();
	    	if(resp.getListObject1() != null) {
	    		subSpecialityList = resp.getListObject1();
	    	}
	    }
        if(specialityList != null && specialityList.size() > 0) {
				for (Iterator iterator = specialityList.iterator(); iterator.hasNext();) {
					SearchDto dto = (SearchDto) iterator.next();
					deptIds.add(dto.getSpecialityId());
				}
	        }else {
	        	deptIds.add(searchDto.getDeptId());
	        }

		
	      String query = sessionFactory.getCurrentSession()
	          .getNamedQuery("CONSOLIDATED_VALIDATION_ENTRY").getQueryString().toString();
	      com.param.global.common.Response res =  iLabConfigurationDao.getLabConfigurationList(searchDto.getOrgId(),searchDto.getAssignUnit());

	      Character c = null;
	        Character conflag = null;
	        if(res.getListObject() != null && res.getListObject().size()>0) {
		    	   
		    	   List<LabConfigurationDto> list = res.getListObject();
		    	  c = list.getFirst().getSkipValidation();
		    	   conflag = list.getFirst().getConsolidatedResultEntry();
		    	   subDeptIds.add(list.getFirst().getBiochemistryId()!=null ? list.getFirst().getBiochemistryId() :0);
		    	   subDeptIds.add(list.getFirst().getHematologyId()!=null ? list.getFirst().getHematologyId() :0);
		    	   subDeptIds.add(list.getFirst().getMicrobiologyId()!=null ? list.getFirst().getMicrobiologyId() :0);
		    	   subDeptIds.add(list.getFirst().getHistopathologyId()!=null ? list.getFirst().getHistopathologyId() :0);
		    	   subDeptIds.add(list.getFirst().getCystopathologyId()!=null ? list.getFirst().getCystopathologyId() :0);
		    	   subDeptIds.add(list.getFirst().getSerologyId()!=null ? list.getFirst().getSerologyId() :0);
		    	   subDeptIds.add(list.getFirst().getClinicalPathologyId() !=null ? list.getFirst().getClinicalPathologyId() :0);
		       }
	     
	      List<Integer> statusIds = new ArrayList<Integer>(); 
	      if(searchDto.getIsOnHoldParameter() == false) {
	    	  statusIds = Arrays.asList(RESULT_ENTRY_DONE);
	      }
	    	  
	   			
	      StringBuilder sb = new StringBuilder(query);
	      
	      if (searchDto.getFromDt() != null ) {
	    	  sb.append(
	            " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt ");
	      }

	      if (searchDto.getToDt() != null ) {
	    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt ");
	      }

	      if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
	    	  sb
	            .append(" AND pati_mst.patient_id =:patientId ");

	      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
			  visitTypes = searchDto.getVisitTypes().stream()
					  .map(obj -> obj.getId())
					  .collect(Collectors.toList());
			  sb.append(" AND visit_type_master.visit_type_id IN (:visitTypes)");
		  }

		  if(searchDto.getCheckForIsPOC() != null && searchDto.getCheckForIsPOC() == Boolean.TRUE){
			  visitTypes = Arrays.asList(ICommonConstants.ER);
			  sb.append(" AND visit_type_master.visit_type_id IN (:visitTypes)");
			  sb.append(" AND service_dtls.status = 'A'");
			  sb.append(" AND service_dtls.is_poc = 'Y'");
		  }

	      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0) {
			  subDepts = searchDto.getSubDepts().stream()
					  .map(obj -> obj.getId())
					  .collect(Collectors.toList());
			  sb.append(" AND test_mst.dept_id IN (:subDepts)");
		  }
	      
	      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size() > 0) {
			  testTypes = searchDto.getTestTypes().stream()
					  .map(obj -> obj.getId())
					  .collect(Collectors.toList());

			  sb.append(" AND test_mst.test_id in (:testTypes)");
		  }

	      if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0)
	    	  sb
	            .append(" AND lbsampledtls.lab_sample_dtls_id =:labSampleDtlsId ");

	      if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
	          sb.append("  and lbsampledtls.assign_unit  =:assignUnit ");
	      if (searchDto.getIsOnHoldParameter() == true) {
        	  statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
        	  sb.append(" and lbsampledtls.sample_status_id IN(:statusIds)");
              sb.append(" AND lbsampledtls.is_on_hold_parameter =:isOnHoldParameter ");
          }
          if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
              sb.append(" and (lbsampledtls.sample_status_id IN(:statusIds) or lbsampledtls.is_on_hold_parameter = true) ");
          }
        if(searchDto.getUhidNumber()!="") {
            sb.append(" AND lower(pati_mst.uhid_number) LIKE concat('%', LOWER(:uhidNumber), '%') ");
        }
        
        if(searchDto.getSampleNo()!="") {
            sb.append(" AND lower(sample_mst.sample_no) LIKE concat('%',LOWER(:sampleNo) ");
        }

        if(conflag != null && conflag=='N'){
	    	  sb.append(" and lbsampledtls.sub_dept_id not IN(:subDeptIds) ");
	      }
	      
	      sb.append( " GROUP BY  " 
	    		  	+" subspe_mst.sub_speciality_name, "
			        + "  labresmst.lab_test_res_id, "
			        + "  bill.bill_number, " 
			        + "  lbsampledtls.lab_sample_dtls_id,  "
			        + "  sample_mst.sample_no,  "
			        + "  visit_type_master.visit_type_id,  " 
			        + "  pati_mst.patient_id,  "
			        + "  test_mst.test_id,  " 
			        + "  pre_mst.prefix_id,  " 
			        + "  gen_mst.gender_id,  "
			        + "  doct_mst.doctor_id,  " 
			        + "  wardmast.ward_id,  "
			        + "  bedmst.bed_id,  " 
			        + "  samp_mst.sample_id , " 
			        + "  panel_master.panel_id,  " 
			        + "  speciality_master.speciality_id,  "
			        + "  order_details.order_details_id , " 
	                + " order_master.encounter_id ," + 
	                  "  order_master.admission_id ," +
	                  " tpsd.payment_entitlement_id ,"+
	                  " tpsd2.payment_entitlement_id,"
	                  + " order_master.order_id,"
	                  + " sample_mst.sample_id, "
	                  + " intervention.clinical_intervention_procedure_id, "
	                  + " intervention.intervention_processed_doctor_id, "
	                  + " intervention.is_clinical_intervention_required, "
	                  + " intervention.intervention_remark, "
	                  + " intervention.intervention_completed_date_time ");
	                //  + " retest.retestcount "
			       /* + " ORDER BY  " 
			        + "  lbsampledtls.patient_id  ");*/
			       
			        
	      if(searchDto.getLabSortingId()!=null) {
				if (searchDto.getLabSortingId() == 1) {
					sb.append("ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
				} else if (searchDto.getLabSortingId() == 2) {
					sb.append("ORDER BY    order_master.order_id DESC ");
				} else if (searchDto.getLabSortingId() == 3) {
					sb.append("ORDER BY    sample_mst.sample_id  DESC ");
				} else if (searchDto.getLabSortingId() == 4) {
					sb.append("ORDER BY   lbsampledtls.updated_date DESC ");
				}
			} else {
				sb.append("ORDER BY    lbsampledtls.patient_id,    lbsampledtls.updated_date DESC ");
			}
			        //+ "  lbsampledtls.updated_date  ");
	      
	      Query createdQuery = sessionFactory.getCurrentSession()
	          .createSQLQuery(sb.toString());
		if (searchDto.getFromDt() != null ) {
			createdQuery.setParameter("fromDt", searchDto.getFromDt());
		}

		if (searchDto.getToDt() != null ) {
			createdQuery.setParameter("toDt", searchDto.getToDt());
		}

		if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
			createdQuery.setParameter("patientId", searchDto.getPatientId());
		}

		if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0){
			createdQuery.setParameterList("visitTypes", visitTypes);
		}

		if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0){
			createdQuery.setParameterList("subDepts", subDepts);
		}

		if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size() > 0){
			createdQuery.setParameterList("testTypes", testTypes);
		}

		if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0){
			createdQuery.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId());
		}

		if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0){
			createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());
		}
		if (searchDto.getIsOnHoldParameter() == true) {
			createdQuery.setParameter("isOnHoldParameter", searchDto.getIsOnHoldParameter());
			createdQuery.setParameterList("statusIds", statusIds);
		}
		if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
			createdQuery.setParameterList("statusIds", statusIds);
		}
		if(searchDto.getUhidNumber()!="") {
			createdQuery.setParameter("uhidNumber", searchDto.getUhidNumber());
		}

		if(searchDto.getSampleNo()!="") {
			createdQuery.setParameter("sampleNo", searchDto.getSampleNo());
		}

		if(conflag != null && conflag=='N'){
			createdQuery.setParameterList("subDeptIds", subDeptIds);
		}

		if(searchDto.getCheckForIsPOC() != null && searchDto.getCheckForIsPOC() == Boolean.TRUE){
			createdQuery.setParameterList("visitTypes", visitTypes);
		}
		else{
			//		Condition added for IS POC check for poc worklist skipping pagination condition.
			createdQuery.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
					.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50);
		}

	      StringBuilder sb1 = sb;
	      List<LabResultMasterDto>  list = createdQuery
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
//					.setParameter("sampleStatusId", RESULT_ENTRY_DONE)
//					.setParameter("deptId", searchDto.getDeptId())
					.setParameterList("deptId", deptIds)
					.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
	      
	      Integer count = 0;
	      Query createdQuery1 = sessionFactory.getCurrentSession()
		          .createSQLQuery(sb1.toString());

		if (searchDto.getFromDt() != null ) {
			createdQuery1.setParameter("fromDt", searchDto.getFromDt());
		}

		if (searchDto.getToDt() != null ) {
			createdQuery1.setParameter("toDt", searchDto.getToDt());
		}

		if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
			createdQuery1.setParameter("patientId", searchDto.getPatientId());
		}

		if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0){
			createdQuery1.setParameterList("visitTypes", visitTypes);
		}

		if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0){
			createdQuery1.setParameterList("subDepts", subDepts);
		}

		if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size() > 0){
			createdQuery1.setParameterList("testTypes", testTypes);
		}

		if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0){
			createdQuery1.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId());
		}

		if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0){
			createdQuery1.setParameter("assignUnit", searchDto.getAssignUnit());
		}
		if (searchDto.getIsOnHoldParameter() == true) {
			createdQuery1.setParameter("isOnHoldParameter", searchDto.getIsOnHoldParameter());
			createdQuery1.setParameterList("statusIds", statusIds);
		}
		if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
			createdQuery1.setParameterList("statusIds", statusIds);
		}
		if(searchDto.getUhidNumber()!="") {
			createdQuery1.setParameter("uhidNumber", searchDto.getUhidNumber());
		}

		if(searchDto.getSampleNo()!="") {
			createdQuery1.setParameter("sampleNo", searchDto.getSampleNo());
		}

		if(conflag != null && conflag=='N'){
			createdQuery1.setParameterList("subDeptIds", subDeptIds);
		}

		if(searchDto.getCheckForIsPOC() != null && searchDto.getCheckForIsPOC() == Boolean.TRUE){
			createdQuery1.setParameterList("visitTypes", visitTypes);
		}

	      List<LabResultMasterDto>  list1 = createdQuery1
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
//					.setParameter("sampleStatusId", RESULT_ENTRY_DONE)
					//.setParameter("deptId", searchDto.getDeptId())
					.setParameterList("deptId", deptIds)
					.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
	      
	      if (list.isEmpty()) {
	    	  
	    	  return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.",list, null);
	        
	      } else {
	    	  
	    	  if(list1 != null && list1.size()>0) {
	    		  count = list1.size();
	    	  }
	    	  
	    	  String sql="    select count(sample_dtls_id) as \"retestCount\"  , sample_dtls_id as \"labSampleDtlsId\"  from lab.t_sample_audit_trail sample_audit_trail  "
                      + "where test_status_id = 7 and sample_audit_trail.org_id =:orgId "+"  group by sample_dtls_id  ";
              List<LabResultMasterDto> list2  = sessionFactory.getCurrentSession()
                        .createSQLQuery(sql)
					  .setParameter("orgId", searchDto.getOrgId())
                      .setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
              
              
               for (LabResultMasterDto labResultMasterDto : list) {
                
                  LabResultMasterDto retestDto = list2.stream()
                         .filter(order -> labResultMasterDto.getLabSampleDtlsId().equals(order.getLabSampleDtlsId()))
                         .findAny()
                         .orElse(null);
                  
                  if(retestDto != null){
                      labResultMasterDto.setRetestCount(retestDto.getRetestCount()!= null ? retestDto.getRetestCount() : null);
                  }
                  
               } 
    		  
    	  
	    	  return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, list, count);
	      }


	    } catch (Exception e) {
	      logger.error("Exection", e);
	      throw e;
	    }
}

@Override
public Response consolidatedSerologyValidationSearch(SearchDto searchDto) throws ApplicationException {
	try {

		
	    Response resp = iLabTransactionDao.getSpecialityAndSubSpecialityListByUserIdByUnitId(searchDto);
        List<Integer> deptIds = new ArrayList<>();
        List<Integer> subDeptIds = new ArrayList<>();
        List<Integer> subDepts = new ArrayList<>();
        List<SearchDto> specialityList = new ArrayList<>();
        List<SearchDto> subSpecialityList = new ArrayList<>();
		List<Integer>  testTypes = new ArrayList<Integer>();
		List<Integer>  visitTypes = new ArrayList<Integer>();

        if(resp.getListObject().size() > 0 && resp.getListObject() != NULL) {
	    	specialityList = resp.getListObject();
	    	if(resp.getListObject1() != null) {
	    		subSpecialityList = resp.getListObject1();
	    	}
	    }
        if(specialityList != null && specialityList.size() > 0) {
				for (Iterator iterator = specialityList.iterator(); iterator.hasNext();) {
					SearchDto dto = (SearchDto) iterator.next();
					deptIds.add(dto.getSpecialityId());
				}
	        }else {
	        	deptIds.add(searchDto.getDeptId());
	        }
			
			if(subSpecialityList != null && subSpecialityList.size() > 0) {
				for (Iterator iterator = subSpecialityList.iterator(); iterator.hasNext();) {
					SearchDto dto2 = (SearchDto) iterator.next();
					subDeptIds.add(dto2.getSubSpecialityMasterId());
				}
	        }
		searchDto.setSubDeptIds(subDeptIds);
		
		LabResultMasterDto dto = (LabResultMasterDto) sessionFactory.getCurrentSession()
				.createSQLQuery(" select " + 
						" lab_config.lab_speciality_id as \"labSpecialityId\", " + 
						" lab_config.serology_id as \"serologyId\" " + 
						" from " + 
						"    public.t_lab_configuration lab_config " + 
						" where " + 
						"	lab_config.unit_id =:orgUnitId")
				.setParameter("orgUnitId", searchDto.getOrgUnitId())
				.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).uniqueResult();
	
		if((dto!= null) && (dto.getSerologyId()!= null && dto.getSerologyId() != 0)) {
			

			List<SearchDto> labSpecialityList = specialityList.stream()
					.filter(bill -> bill.getSpecialityId().equals(dto.getLabSpecialityId())).collect(Collectors.toList());
			
			List<SearchDto> serologyList = subSpecialityList.stream()
					.filter(bill -> bill.getSubSpecialityMasterId().equals(dto.getSerologyId())).collect(Collectors.toList());
			
		      String query = sessionFactory.getCurrentSession()
		          .getNamedQuery("GET_SEROLOGY_RESULT_ENTRY_LIST").getQueryString().toString();

		      StringBuilder sb = new StringBuilder(query);
		      List<Integer> statusIds = new ArrayList<Integer>(); 
		      if(searchDto.getIsOnHoldParameter() == false) {
		    	  statusIds = Arrays.asList(RESULT_ENTRY_DONE);
		      }
		      
		      
		      if(labSpecialityList != null && labSpecialityList.size() > 0) {
	        	  sb.append(" AND lbsampledtls.dept_id IN (:deptIds)");
		      }
		      
	    	  if(serologyList != null && serologyList.size() > 0) {
	    		  sb.append(" AND lbsampledtls.sub_dept_id IN (:serologyIds)");
	    	  }
	      
		      if (searchDto.getFromDt() != null ) 
		      {
		    	  sb.append(
		            " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt ");
		      }

		      if (searchDto.getToDt() != null ) {
		    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt ");
		      }

		      if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
		    	  sb
		            .append(" AND pati_mst.patient_id =:patientId  ");

		      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
				  visitTypes = searchDto.getVisitTypes().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());

				  sb.append(" AND visit_type_master.visit_type_id IN (:visitTypes)");
			  }

			if(searchDto.getCheckForIsPOC() != null && searchDto.getCheckForIsPOC() == Boolean.TRUE){
				visitTypes = Arrays.asList(ICommonConstants.ER);
				sb.append(" AND visit_type_master.visit_type_id IN (:visitTypes)");
				sb.append(" AND service_dtls.status = 'A'");
				sb.append(" AND service_dtls.is_poc = 'Y'");
			}

		      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0) {
				  subDepts = searchDto.getSubDepts().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());
				  sb.append(" AND test_mst.dept_id IN (:subDepts)");
			  }
		      
		      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size() > 0) {
				  testTypes = searchDto.getTestTypes().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());

				  sb.append(" AND test_mst.test_id in (:testTypes)");
			  }
		      
		      if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0)
		    	  sb
		            .append(" AND lbsampledtls.lab_sample_dtls_id =:labSampleDtlsId ");

		      if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
		          sb.append("  and lbsampledtls.assign_unit  =:assignUnit");
		      
		      if(searchDto.getUhidNumber()!="") {
		    	  sb.append(" AND lower(pati_mst.uhid_number) LIKE concat('%',LOWER(:uhidNumber),'%') ");
		      }
		      
		      if(searchDto.getSampleNo()!="") {
		    	  sb.append(" AND lower(sample_mst.sample_id) LIKE concat('%',LOWER(:sampleNo),'%') ");
		      }
		      if (searchDto.getIsOnHoldParameter() == true) {
	        	  statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
	        	  sb.append(" and lbsampledtls.sample_status_id IN(:statusIds)");
	              sb.append(" AND lbsampledtls.is_on_hold_parameter =:isOnHoldParameter ");
	          }
	          if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
	              sb.append(" and (lbsampledtls.sample_status_id IN(:statusIds) or lbsampledtls.is_on_hold_parameter = true) ");
	          }
	       
	          
		    /*  sb.append( " ORDER BY  " 
				        + "  lbsampledtls.patient_id  ");*/
				if (searchDto.getLabSortingId() != null) {
					if (searchDto.getLabSortingId() == 1) {
						sb.append(" ORDER BY   lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
					} else if (searchDto.getLabSortingId() == 2) {
						sb.append(" ORDER BY   order_master.order_id DESC ");
					} else if (searchDto.getLabSortingId() == 3) {
						sb.append(" ORDER BY   sample_mst.sample_id  DESC ");
					} else if (searchDto.getLabSortingId() == 4) {
						sb.append(" ORDER BY    lbsampledtls.updated_date DESC ");
					}
				} else {
					sb.append(" ORDER BY    lbsampledtls.patient_id,    lbsampledtls.updated_date DESC ");
				}
				        //+ "  lbsampledtls.updated_date  "  );
		      
		      Query createdQuery = sessionFactory.getCurrentSession()
		          .createSQLQuery(sb.toString());

			if(labSpecialityList != null && labSpecialityList.size() > 0) {
				createdQuery.setParameterList("deptIds", deptIds);
			}

			if(serologyList != null && serologyList.size() > 0) {
				createdQuery.setParameter("serologyIds", dto.getSerologyId());
			}

			if (searchDto.getFromDt() != null )
			{
				createdQuery.setParameter("fromDt", searchDto.getFromDt());
			}

			if (searchDto.getToDt() != null ) {
				createdQuery.setParameter("toDt", searchDto.getToDt());
			}

			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
				createdQuery.setParameter("patientId", searchDto.getPatientId());
			}

			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0){
				createdQuery.setParameterList("visitTypes", visitTypes);
			}

			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0){
				createdQuery.setParameterList("subDepts", subDepts);
			}

			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size() > 0){
				createdQuery.setParameterList("testTypes", testTypes);
			}

			if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0){
				createdQuery.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId());
			}

			if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0) {
				createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());
			}
			if(searchDto.getUhidNumber()!="") {
				createdQuery.setParameter("uhidNumber", searchDto.getUhidNumber());
			}

			if(searchDto.getSampleNo()!="") {
				createdQuery.setParameter("sampleNo", searchDto.getSampleNo());
			}
			if (searchDto.getIsOnHoldParameter() == true) {
				createdQuery.setParameterList("statusIds", statusIds);
				createdQuery.setParameter("isOnHoldParameter", searchDto.getIsOnHoldParameter());
			}
			if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
				createdQuery.setParameterList("statusIds", statusIds);
			}

			if(searchDto.getCheckForIsPOC() != null && searchDto.getCheckForIsPOC() == Boolean.TRUE){
				createdQuery.setParameterList("visitTypes", visitTypes);
			}

		      StringBuilder sb1 = sb;
			List<LabResultMasterDto> list1  = createdQuery
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
//						.setParameter("sampleStatusId", RESULT_ENTRY_DONE)
					.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();

			Integer count = 0;
			List<LabResultMasterDto> list = new ArrayList<>();
			if(searchDto.getCheckForIsPOC() != null && searchDto.getCheckForIsPOC() == Boolean.TRUE){
				//		Condition added for IS POC check for poc worklist skipping pagination condition.
				list = list1;
			}
			else{
				list = createdQuery
						.setParameter("orgId", searchDto.getOrgId())
						.setParameter("orgUnitId", searchDto.getOrgUnitId())
						//.setParameter("sampleStatusId", RESULT_ENTRY_DONE)
						.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
						.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
						.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
			}

		      if (list.isEmpty()) {
		    	  
		    	  return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.",list, null);
		        
		      } else {
		    	  
		    	  if(list1 != null && list1.size()>0) {
		    		  count = list1.size();
		    	  }
	    	  
		    	  return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, list, count);
		      }

		}else {
			 return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.",null, null);
		}

	    


	    } catch (Exception e) {
	      logger.error("Exection", e);
	      throw e;
	    }
}

@Override
public Response labValidationWorklistTypeSearch(SearchDto searchDto) throws ApplicationException {

	try {

	      String query = sessionFactory.getCurrentSession()
	          .getNamedQuery("GET_BIOCHEM_VALIDATION_ENTRY").getQueryString().toString();

	      StringBuilder sb = new StringBuilder(query);

	      if (searchDto.getFromDt() != null ) {
	    	  sb.append(
	            " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt ");
	      }

	      if (searchDto.getToDt() != null ) {
	    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt ");
	      }

	      if(searchDto.getSearchField()!="") {
	    	  sb.append(" AND (lower(pati_mst.uhid_number) LIKE concat('%',LOWER(:searchField),'%') OR "
	    	  + " lower(sample_mst.sample_id) LIKE concat('%',LOWER(:searchField),'%') OR "
	    	  + " lower(pati_mst.first_name) LIKE concat('%',LOWER(:searchField),'%') OR "
	    	  + " lower(pati_mst.middle_name) LIKE concat('%',LOWER(:searchField),'%') OR "
	    	  + " lower(pati_mst.last_name) LIKE concat('%',LOWER(:searchField),'%') OR "
	    	  + " lower(test_mst.test_desc) LIKE concat('%',LOWER(:searchField),'%') OR "
					  + " lower(test_mst.test_code) LIKE concat('%',LOWER(:searchField),'%') OR "
					  + " lower(subspe_mst.sub_speciality_name) LIKE concat('%',LOWER(:searchField),'%') ) ");
	    	  }

		if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
			sb.append("  and lbsampledtls.assign_unit = :assignUnit ");
	  	if (searchDto.getIsOnHoldParameter() == true) {
        	  sb.append(" and (lbsampledtls.sample_status_id ="+RESULT_VALIDATION_DONE+"or lbsampledtls.sample_status_id ="+REPORT_RELEASED
        			  +" or lbsampledtls.sample_status_id ="+PARTIAL_REPORT_RELEASED+" or lbsampledtls.sample_status_id = "+PROVISIONAL_REPORT_RELEASED+")");
              sb.append(" AND lbsampledtls.is_on_hold_parameter =" + searchDto.getIsOnHoldParameter());
          }else {
              sb.append(" and (lbsampledtls.sample_status_id ="+RESULT_ENTRY_DONE+ " or lbsampledtls.is_on_hold_parameter = true ) ");
          }
	      
	      sb.append( " GROUP BY  " 
			        + "  labresmst.lab_test_res_id,  " 
			        + "  lbsampledtls.lab_sample_dtls_id,  "
			        + "  sample_mst.sample_no,  "
			        + "  visit_type_master.visit_type_id,  " 
			        + "  pati_mst.patient_id,  "
			        + "  test_mst.test_id,  " 
			        + "  pre_mst.prefix_id,  " 
			        + "  gen_mst.gender_id,  "
			        + "  doct_mst.doctor_id,  " 
			        + "  wardmast.ward_id,  "
			        + "  bedmst.bed_id,  " 
			        + "  samp_mst.sample_id , " 
			        + "  panel_master.panel_id,  " 
			        + "  speciality_master.speciality_id,  "
			        + "  order_details.order_details_id ," 
			        + " order_master.encounter_id ," + 
			          "  order_master.admission_id ,"+
			          " tpsd.payment_entitlement_id,"+
			          " tpsd2.payment_entitlement_id ,"
			          + " order_master.order_id,"
			          + " sample_mst.sample_id, "
			        //  + " retest.retestcount ,"
			          + " bill_mst.bill_number ");
	      
		

			if (searchDto.getLabSortingId() != null) {
				if (searchDto.getLabSortingId() == 1) {
					sb.append(" ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
				} else if (searchDto.getLabSortingId() == 2) {
					sb.append(" ORDER BY    order_master.order_id DESC ");
				} else if (searchDto.getLabSortingId() == 3) {
					sb.append(" ORDER BY   sample_mst.sample_id  DESC ");
				} else if (searchDto.getLabSortingId() == 4) {
					sb.append(" ORDER BY    lbsampledtls.updated_date DESC ");
				}
			} else {
				sb.append(" ORDER BY   lbsampledtls.patient_id,    lbsampledtls.updated_date DESC ");
			}
	      Query createdQuery = sessionFactory.getCurrentSession()
	          .createSQLQuery(sb.toString());
		if (searchDto.getFromDt() != null ) {
			createdQuery.setParameter("fromDt", searchDto.getFromDt());
		}

		if (searchDto.getToDt() != null ) {
			createdQuery.setParameter("toDt", searchDto.getToDt());
		}

		if(searchDto.getSearchField()!="") {
			createdQuery.setParameter("searchField", searchDto.getSearchField());
		}

		if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0){
			createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());
		}

	      List<LabResultMasterDto>  list = createdQuery
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
					.setParameter("deptId", searchDto.getDeptId())
					.setParameter("subDeptId", searchDto.getSubDeptId())
					.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
					.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
					.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();


	      if (list.isEmpty()) {
	        return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.",
	        		list, null);
	      } else {
	    	  
	    	  String sql="    select count(sample_dtls_id) as \"retestCount\"  , sample_dtls_id as \"labSampleDtlsId\"  from lab.t_sample_audit_trail sample_audit_trail  "
                      + "where test_status_id = 7 and sample_audit_trail.org_id =:orgId group by sample_dtls_id  ";
              List<LabResultMasterDto> list1  = sessionFactory.getCurrentSession()
                        .createSQLQuery(sql)
					  .setParameter("orgId", searchDto.getOrgId())
                      .setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
              
              
               for (LabResultMasterDto labResultMasterDto : list) {
                
                  LabResultMasterDto retestDto = list1.stream()
                         .filter(order -> labResultMasterDto.getLabSampleDtlsId().equals(order.getLabSampleDtlsId()))
                         .findAny()
                         .orElse(null);
                  
                  if(retestDto != null){
                      labResultMasterDto.setRetestCount(retestDto.getRetestCount()!= null ? retestDto.getRetestCount() : null);
                  }
                  
               } 
	    	  
	        return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, list,
	            null);
	      }


	    } catch (Exception e) {
	      logger.error("Exection", e);
	      throw e;
	    }
  
}

@Override
public Response consolidatedWorklistTypeSearch(SearchDto searchDto) throws ApplicationException {

	try {
		
		Response resp = iLabTransactionDao.getSpecialityAndSubSpecialityListByUserIdByUnitId(searchDto);
        List<Integer> deptIds = new ArrayList<>();
        List<Integer> subDeptIds = new ArrayList<>();
        List<SearchDto> specialityList = new ArrayList<>();
        List<SearchDto> subSpecialityList = new ArrayList<>();
		List<LabResultMasterDto>  list1 = new ArrayList<>();
		List<LabResultMasterDto>  list = new ArrayList<>();
		Integer count = 0;

        if(specialityList != null && specialityList.size() > 0) {
			for (Iterator iterator = specialityList.iterator(); iterator.hasNext();) {
				SearchDto dto = (SearchDto) iterator.next();
				deptIds.add(dto.getSpecialityId());
			}
        }else {
        	deptIds.add(searchDto.getDeptId());
        }


		LabResultMasterDto dto = (LabResultMasterDto) sessionFactory.getCurrentSession()
				.createSQLQuery(" select " +
						" distinct(tlc.serology_id ) as \"serologyId\" " +
						"	 from t_lab_configuration tlc " +
						"	 where " +
						"	 tlc.organisation_id =:orgId and tlc.unit_id =:orgUnitId")
				.setParameter("orgId", searchDto.getOrgId())
				.setParameter("orgUnitId", searchDto.getOrgUnitId())
				.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).uniqueResult();


		boolean isSerologyIdPresent = false;

		if(dto.getSerologyId() != null) {
			for (obj item : searchDto.getSubDepts()) {
				if (dto.getSerologyId().equals(item.getId())) {
					isSerologyIdPresent = true;
				}else {
					isSerologyIdPresent = false;
					break;
				}
			}
		}

		if(isSerologyIdPresent==false) {

			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("CONSOLIDATED_VALIDATION_ENTRY").getQueryString().toString();
			com.param.global.common.Response res = iLabConfigurationDao.getLabConfigurationList(searchDto.getOrgId(), searchDto.getAssignUnit());
			Character c = null;
			Character conflag = null;

			if (res.getListObject() != null && res.getListObject().size() > 0) {

				List<LabConfigurationDto> listGet = res.getListObject();
				c = listGet.getFirst().getSkipValidation();
				conflag = listGet.getFirst().getConsolidatedResultEntry();
				subDeptIds.add(listGet.getFirst().getBiochemistryId() != null ? listGet.getFirst().getBiochemistryId() : 0);
				subDeptIds.add(listGet.getFirst().getHematologyId() != null ? listGet.getFirst().getHematologyId() : 0);
				subDeptIds.add(listGet.getFirst().getMicrobiologyId() != null ? listGet.getFirst().getMicrobiologyId() : 0);
				subDeptIds.add(listGet.getFirst().getHistopathologyId() != null ? listGet.getFirst().getHistopathologyId() : 0);
				subDeptIds.add(listGet.getFirst().getCystopathologyId() != null ? listGet.getFirst().getCystopathologyId() : 0);
				subDeptIds.add(listGet.getFirst().getSerologyId() != null ? listGet.getFirst().getSerologyId() : 0);
				subDeptIds.add(listGet.getFirst().getClinicalPathologyId() != null ? listGet.getFirst().getClinicalPathologyId() : 0);
			}

//	      List<Integer> statusIds = new ArrayList<Integer>();
//	      if(searchDto.getIsOnHoldParameter() == false) {
//	    	  if(c.equals('Y')) {
//		    		statusIds = Arrays.asList(RESULT_ENTRY_DONE,PROVISIONAL_REPORT_RELEASED);
//		          }else {
//		        		statusIds = BIOCHEM_REPORT_RELEASE_WORKLIST;
//		          }
//
//	      }

			List<Integer> statusIds = new ArrayList<Integer>();
			if (searchDto.getIsOnHoldParameter() == false) {
				statusIds = Arrays.asList(RESULT_ENTRY_DONE);
			}

			StringBuilder sb = new StringBuilder(query);
			if (searchDto.getFromDt() != null) {
				sb.append(
						" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt ");
			}

			if (searchDto.getToDt() != null) {
				sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt  ");
			}

			if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
				sb.append("  and lbsampledtls.assign_unit  =:assignUnit");

			if (searchDto.getIsOnHoldParameter() == true) {
				statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
				sb.append(" and lbsampledtls.sample_status_id IN(:statusIds)");
				sb.append(" AND lbsampledtls.is_on_hold_parameter =:isOnHoldParameter ");
			}
			if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
				sb.append(" and (lbsampledtls.sample_status_id IN(:statusIds) or lbsampledtls.is_on_hold_parameter = true) ");
			}

			if (conflag != null && conflag == 'N') {
				sb.append(" and lbsampledtls.sub_dept_id not IN(:subDeptIds)");
			}

			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0)
				sb.append(" AND test_mst.dept_id IN (:subDepts)");


			if (searchDto.getSearchField() != "") {
				sb.append(" AND (lower(pati_mst.uhid_number) LIKE concat('%',LOWER(:searchField),'%') OR "
						+ " lower(sample_mst.sample_id) LIKE concat('%',LOWER(:searchField),'%') OR "
						+ " lower(pati_mst.first_name) LIKE concat('%',LOWER(:searchField),'%') OR "
						+ " lower(pati_mst.middle_name) LIKE concat('%',LOWER(:searchField),'%') OR "
						+ " lower(pati_mst.last_name) LIKE concat('%',LOWER(:searchField),'%') OR "
						+ " lower(test_mst.test_desc) LIKE concat('%',LOWER(:searchField),'%') OR"
						+ " lower(test_mst.test_code) LIKE concat('%',LOWER(:searchField),'%') OR "
						+ " lower(subspe_mst.sub_speciality_name) LIKE concat('%',LOWER(:searchField),'%') )");
			}

			sb.append(" GROUP BY  "
					+ " subspe_mst.sub_speciality_name, "
					+ "  labresmst.lab_test_res_id,  "
					+ "  lbsampledtls.lab_sample_dtls_id,  "
					+ "  sample_mst.sample_no,  "
					+ "  visit_type_master.visit_type_id,  "
					+ "  pati_mst.patient_id,  "
					+ "  test_mst.test_id,  "
					+ "  pre_mst.prefix_id,  "
					+ "  gen_mst.gender_id,  "
					+ "  doct_mst.doctor_id,  "
					+ "  wardmast.ward_id,  "
					+ "  bedmst.bed_id,  "
					+ "  samp_mst.sample_id , "
					+ "  panel_master.panel_id,  "
					+ "  speciality_master.speciality_id,  "
					+ "  order_details.order_details_id , "
					+ " order_master.encounter_id ," +
					"  order_master.admission_id ," +
					" tpsd.payment_entitlement_id ," +
					" tpsd2.payment_entitlement_id,"
					+ " order_master.order_id,"
					+ " sample_mst.sample_id, "
					// + " retest.retestcount ,"
					+ " bill.bill_number, "
					+ " intervention.clinical_intervention_procedure_id, "
					+ " intervention.intervention_processed_doctor_id, "
					+ " intervention.is_clinical_intervention_required, "
					+ " intervention.intervention_completed_date_time, "
					+ " intervention.intervention_remark ");


			if (searchDto.getLabSortingId() != null) {
				if (searchDto.getLabSortingId() == 1) {
					sb.append(" ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
				} else if (searchDto.getLabSortingId() == 2) {
					sb.append(" ORDER BY    order_master.order_id DESC ");
				} else if (searchDto.getLabSortingId() == 3) {
					sb.append(" ORDER BY    sample_mst.sample_id  DESC ");
				} else if (searchDto.getLabSortingId() == 4) {
					sb.append(" ORDER BY lbsampledtls.updated_date DESC ");
				}
			} else {
				sb.append(" ORDER BY   lbsampledtls.patient_id,    lbsampledtls.updated_date DESC ");
			}
			        /*+ " ORDER BY  " 
			        + "  lbsampledtls.priority_id,  "
			        + "  lbsampledtls.updated_date  ");*/

			Query createdQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString());
			if (searchDto.getFromDt() != null) {
				createdQuery.setParameter("fromDt", searchDto.getFromDt());
			}

			if (searchDto.getToDt() != null) {
				createdQuery.setParameter("toDt", searchDto.getToDt());
			}

			if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0){
				createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());
			}

			if (searchDto.getIsOnHoldParameter() == true) {
				createdQuery.setParameterList("statusIds", statusIds);
				createdQuery.setParameter("isOnHoldParameter", searchDto.getIsOnHoldParameter());
			}
			if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
				createdQuery.setParameterList("statusIds", statusIds);
			}

			if (conflag != null && conflag == 'N') {
				createdQuery.setParameterList("subDeptIds", subDeptIds);
			}

			/*if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0) {
				createdQuery.setParameterList("subDepts", searchDto.getSubDepts());
			}*/

			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0) {
				List<Integer> subDeptIds2 = searchDto.getSubDepts()
						.stream()
						.map(subDept -> subDept.getId()) // Extract the IDs from each SubDept object
						.collect(Collectors.toList());

				// Pass the list of IDs to the query
				createdQuery.setParameterList("subDepts", subDeptIds2);
			}

			if (searchDto.getSearchField() != "") {
				createdQuery.setParameter("searchField", searchDto.getSearchField());
			}

			StringBuilder sb1 = sb;
			list = createdQuery
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
//					.setParameter("sampleStatusId", RESULT_ENTRY_DONE)
					.setParameter("deptId", searchDto.getDeptId())
					.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
					.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
					.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();

			Query createdQuery1 = sessionFactory.getCurrentSession()
					.createSQLQuery(sb1.toString());
			if (searchDto.getFromDt() != null) {
				createdQuery1.setParameter("fromDt", searchDto.getFromDt());
			}

			if (searchDto.getToDt() != null) {
				createdQuery1.setParameter("toDt", searchDto.getToDt());
			}

			if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0){
				createdQuery1.setParameter("assignUnit", searchDto.getAssignUnit());
			}

			if (searchDto.getIsOnHoldParameter() == true) {
				createdQuery1.setParameterList("statusIds", statusIds);
				createdQuery1.setParameter("isOnHoldParameter", searchDto.getIsOnHoldParameter());
			}
			if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
				createdQuery1.setParameterList("statusIds", statusIds);
			}

			if (conflag != null && conflag == 'N') {
				createdQuery1.setParameterList("subDeptIds", subDeptIds);
			}

			/*if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0) {
				createdQuery1.setParameterList("subDepts", searchDto.getSubDepts());
			}*/

			if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()) {
				List<Integer> subDeptIds1 = searchDto.getSubDepts()
						.stream()
						.map(subDept -> subDept.getId()) // Extract the IDs from each SubDept object
						.collect(Collectors.toList());

				// Pass the list of IDs to the query
				createdQuery1.setParameterList("subDepts", subDeptIds1);
			}

			if (searchDto.getSearchField() != "") {
				createdQuery1.setParameter("searchField", searchDto.getSearchField());
			}
			list1 = createdQuery1
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
//					.setParameter("sampleStatusId", RESULT_ENTRY_DONE)
					.setParameter("deptId", searchDto.getDeptId())
					.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();

		}

	      if (list.isEmpty()) {
	    	  
	    	  return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.",list, null);
	        
	      } else {
	    	  
	    	  if(list1 != null && list1.size()>0) {
	    		  
	    		  count = list1.size();
	    	  }
    	  
	    	  String sql="    select count(sample_dtls_id) as \"retestCount\"  , sample_dtls_id as \"labSampleDtlsId\"  from lab.t_sample_audit_trail sample_audit_trail  "
                      + "where test_status_id = 7 and sample_audit_trail.org_id =:orgId "+"  group by sample_dtls_id  ";
              List<LabResultMasterDto> list2  = sessionFactory.getCurrentSession()
                        .createSQLQuery(sql)
					  .setParameter("orgId", searchDto.getOrgId())
                      .setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
              
              
               for (LabResultMasterDto labResultMasterDto : list) {
                
                  LabResultMasterDto retestDto = list2.stream()
                         .filter(order -> labResultMasterDto.getLabSampleDtlsId().equals(order.getLabSampleDtlsId()))
                         .findAny()
                         .orElse(null);
                  
                  if(retestDto != null){
                      labResultMasterDto.setRetestCount(retestDto.getRetestCount()!= null ? retestDto.getRetestCount() : null);
                  }
                  
               } 
    		  
	    	  
	    	  return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, list, count);
	      }


	    } catch (Exception e) {
	      logger.error("Exection", e);
	      throw e;
	    }

}

@Override
public Response consolidatedSerologyValidationTypeSearch(SearchDto searchDto) throws ApplicationException {

	try {

		List<LabResultMasterDto> list = new ArrayList<>();
		List<LabResultMasterDto> list1 = new ArrayList<>();
		Integer count = 0;

		LabResultMasterDto dto = (LabResultMasterDto) sessionFactory.getCurrentSession()
				.createSQLQuery(" select " +
						" distinct(tlc.serology_id ) as \"serologyId\" " +
						"	 from t_lab_configuration tlc " +
						"	 where " +
						"	 tlc.organisation_id =:orgId and tlc.unit_id =:orgUnitId")
				.setParameter("orgId", searchDto.getOrgId())
				.setParameter("orgUnitId", searchDto.getOrgUnitId())
				.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).uniqueResult();

		boolean isSerologyIdPresent = false;

		if(dto.getSerologyId() != null) {
			for (obj item : searchDto.getSubDepts()) {
				if (dto.getSerologyId().equals(item.getId())) {
					isSerologyIdPresent = true;
					break;
				}
			}
		}

		if(isSerologyIdPresent == true) {

			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SEROLOGY_RESULT_ENTRY_LIST").getQueryString().toString();

			StringBuilder sb = new StringBuilder(query);
			List<Integer> statusIds = new ArrayList<Integer>();
			if (searchDto.getIsOnHoldParameter() == false) {
				statusIds = Arrays.asList(RESULT_ENTRY_DONE);
			}
			if (searchDto.getFromDt() != null) {
				sb.append(
						" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt ");
			}

			if (searchDto.getToDt() != null) {
				sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt ");
			}

			if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
				sb.append("  and lbsampledtls.assign_unit  =:assignUnit");

			if (searchDto.getSearchField() != "") {
				sb.append(" AND (lower(pati_mst.uhid_number) LIKE concat('%',LOWER(:searchField),'%') OR "
						+ " lower(sample_mst.sample_id) LIKE concat('%',LOWER(:searchField),'%') OR "
						+ " lower(pati_mst.first_name) LIKE concat('%',LOWER(:searchField),'%') OR "
						+ " lower(pati_mst.middle_name) LIKE concat('%',LOWER(:searchField),'%') OR "
						+ " lower(pati_mst.last_name) LIKE concat('%',LOWER(:searchField),'%') OR "
						+ " lower(test_mst.test_desc) LIKE concat('%',LOWER(:searchField),'%') OR"
						+ " lower(test_mst.test_code) LIKE concat('%',LOWER(:searchField),'%') OR "
						+ " lower(subspe_mst.sub_speciality_name) LIKE concat('%',LOWER(:searchField),'%') )");
			}
			if (searchDto.getIsOnHoldParameter() == true) {
				statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
				sb.append(" and lbsampledtls.sample_status_id IN(:statusIds)");
				sb.append(" AND lbsampledtls.is_on_hold_parameter =:isOnHoldParameter");
			}
			if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
				sb.append(" and (lbsampledtls.sample_status_id IN(:statusIds) or lbsampledtls.is_on_hold_parameter = true) ");
			}
			if (searchDto.getLabSortingId() != null) {
				if (searchDto.getLabSortingId() == 1) {
					sb.append("ORDER BY  " + "  lbsampledtls.patient_id," + "  lbsampledtls.updated_date  DESC ");
				} else if (searchDto.getLabSortingId() == 2) {
					sb.append("ORDER BY  " + "  order_master.order_id DESC ");
				} else if (searchDto.getLabSortingId() == 3) {
					sb.append("ORDER BY  " + "  sample_mst.sample_id  DESC ");
				} else if (searchDto.getLabSortingId() == 4) {
					sb.append("ORDER BY  " + "  lbsampledtls.updated_date DESC ");
				}
			} else {
				sb.append("ORDER BY  " + "  lbsampledtls.patient_id,  " + "  lbsampledtls.updated_date DESC ");
			}

			Query createdQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString());
			if (searchDto.getFromDt() != null) {
				createdQuery.setParameter("fromDt", searchDto.getFromDt());
			}

			if (searchDto.getToDt() != null) {
				createdQuery.setParameter("toDt", searchDto.getToDt());
			}

			if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0){
				createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());
			}

			if (searchDto.getSearchField() != "") {
				createdQuery.setParameter("searchField",searchDto.getSearchField());
			}
			if (searchDto.getIsOnHoldParameter() == true) {
				createdQuery.setParameterList("statusIds", statusIds);
				createdQuery.setParameter("isOnHoldParameter", searchDto.getIsOnHoldParameter());
			}
			if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
				createdQuery.setParameterList("statusIds", statusIds);
			}
			StringBuilder sb1 = sb;
			list = createdQuery
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
//					.setParameter("sampleStatusId", RESULT_ENTRY_DONE)
					.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
					.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
					.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();


			Query createdQuery1 = sessionFactory.getCurrentSession()
					.createSQLQuery(sb1.toString());
			if (searchDto.getFromDt() != null) {
				createdQuery1.setParameter("fromDt", searchDto.getFromDt());
			}

			if (searchDto.getToDt() != null) {
				createdQuery1.setParameter("toDt", searchDto.getToDt());
			}

			if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0){
				createdQuery1.setParameter("assignUnit", searchDto.getAssignUnit());
			}

			if (searchDto.getSearchField() != "") {
				createdQuery1.setParameter("searchField",searchDto.getSearchField());
			}
			if (searchDto.getIsOnHoldParameter() == true) {
				createdQuery1.setParameterList("statusIds", statusIds);
				createdQuery1.setParameter("isOnHoldParameter", searchDto.getIsOnHoldParameter());
			}
			if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
				createdQuery1.setParameterList("statusIds", statusIds);
			}
			list1 = createdQuery1
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
//					.setParameter("sampleStatusId", RESULT_ENTRY_DONE)
					.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();

		}

	      if (list.isEmpty()) {
	    	  
	    	  return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.",list, null);
	        
	      } else {
	    	  
	    	  if(list1 != null && list1.size()>0) {
	    		  count = list1.size();
	    	  }
    	  
	    	  return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, list, count);
	      }



	    } catch (Exception e) {
	      logger.error("Exection", e);
	      throw e;
	    }

}
}

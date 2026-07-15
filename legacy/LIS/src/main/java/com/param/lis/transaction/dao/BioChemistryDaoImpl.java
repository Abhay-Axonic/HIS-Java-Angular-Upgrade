package com.param.lis.transaction.dao;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.param.lis.global.common.*;
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.global.ReportHistoryMaster;
import com.param.entity.lis.transaction.InformToMaster;
import com.param.entity.lis.transaction.LabResultDetailsMaster;
import com.param.entity.lis.transaction.LabResultMaster;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ServiceException;
import com.param.lis.common.dto.CommonAutoCompleteDto;
import com.param.lis.common.dto.CommonDto;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.InformToMasterDto;
import com.param.lis.transaction.dto.LabDashBoardDto;
import com.param.lis.transaction.dto.LabResultDetailsViewDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;
import com.param.lis.transaction.dto.ParameterHistoryDto;
import com.param.lis.transaction.dto.ParameterResultDto;
import com.param.lis.transaction.dto.ParameterSearchDto;
import com.param.lis.transaction.dto.ResultEntryDataDto;
import com.param.lis.transaction.dto.SampleAcceptancePendingDto;
import com.param.lis.transaction.dto.SearchCommonDto;
import com.param.lis.transaction.dto.SingParamTestResDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;


@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class BioChemistryDaoImpl extends GenericDao<LabSampleDetailsMaster, Integer>
    implements IBioChemistryDao, ICommonConstants, ITransactionConstants, IError {
  @Autowired
  private SessionFactory sessionFactory;

  public BioChemistryDaoImpl() {
    super(LabSampleDetailsMaster.class);
  }

  final static Logger logger = Logger.getLogger(BioChemistryDaoImpl.class);

  @Autowired
  private DozerBeanMapper mapper;


  @Override
  public Response searchbioChemistryPatientlist(SearchCommonDto searchCommonDto)
      throws ApplicationException {
    List<CommonAutoCompleteDto> patientList = null;
    try {
      patientList = (List<CommonAutoCompleteDto>) sessionFactory.getCurrentSession()
          .getNamedQuery("LAB_PATIENT_SEARCH")
          .setParameter("orgId", searchCommonDto.getOrgId())
         // .setParameter("orgUnitId", searchCommonDto.getOrgUnitId())
          .setString("searchKeyword",
              "%" + searchCommonDto.getSearchKeyword().trim().toLowerCase().trim() + "%")
          .setResultTransformer(Transformers.aliasToBean(CommonAutoCompleteDto.class)).list();
      return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, patientList, null);

    } catch (Exception e) {
      logger.error("Exection", e);
      throw e;
    }
  }

  @Override
  public Response searchBioChemistrySampleNo(SearchCommonDto searchCommonDto)
      throws ApplicationException {
    List<LabDashBoardDto> patientList = null;
    try {
      patientList = (List<LabDashBoardDto>) sessionFactory.getCurrentSession()
          .getNamedQuery("SEARCH_SAMPLE_NO")
          .setParameter("orgId", searchCommonDto.getOrgId())
          .setParameter("orgUnitId", searchCommonDto.getOrgUnitId())
          .setParameter("deptId", searchCommonDto.getDeptId())
          .setParameter("subDeptId", searchCommonDto.getSubDeptId())
          .setString("searchKeyword",
              "%" + searchCommonDto.getSearchKeyword().trim().toLowerCase().trim() + "%")
          .setResultTransformer(Transformers.aliasToBean(LabDashBoardDto.class)).list();
      return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, patientList, null);

    } catch (Exception e) {
      logger.error("Exection", e);
      throw e;
    }
  }

  @Override
  public Response getFilteredBioChemistryList(SearchDto searchDto) throws ApplicationException {
    try {

      String query = sessionFactory.getCurrentSession()
          .getNamedQuery("GET_ACCEPTANCE_PENDING_LIST").getQueryString().toString();

      StringBuilder sb = new StringBuilder(query);

      if (searchDto.getFromDt() != null) {
          sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt ");
      }
      if (searchDto.getToDt() != null ) {
          sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt ");
      }
      if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
          sb.append(" AND pati_mst.patient_id =:patientId ");
      }
      if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()) {
          sb.append(" AND visit_type_master.visit_type_id IN (:visitTypes)");
      }
      if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()) {
          sb.append(" AND test_mst.dept_id IN (:subDepts)");
      }
      if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0) {
          sb.append(" AND lbsampledtls.lab_sample_dtls_id =:labSampleDtlsId ");
      }
      if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()) {
          sb.append(" AND test_mst.test_id in (:testTypes)");
      }
      if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0) {
          sb.append("  and lbsampledtls.assign_unit  =:assignUnit ");
      }
      if(searchDto.getUhidNumber()!="") {
    	  sb.append(" AND lower(pati_mst.uhid_number) LIKE :uhidNumber ");
      }
      
      if(searchDto.getSampleNo()!="") {
    	  sb.append(" AND lower(sample_mst.sample_no) LIKE :sampleNo ");
      }
      
      sb.append( 	 " ORDER BY   " 
				+ "  lbsampledtls.priority_id,   " 
				+ "  lbsampledtls.sample_accept_datetime   ");
      Query createdQuery = sessionFactory.getCurrentSession()
          .createSQLQuery(sb.toString());

        if (searchDto.getFromDt() != null) {
            createdQuery.setParameter("fromDt", searchDto.getFromDt());
        }
        if (searchDto.getToDt() != null ){
            createdQuery.setParameter("toDt", searchDto.getToDt());
        }

        if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
            createdQuery.setParameter("patientId", searchDto.getPatientId());
        }

        if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()){
            List<obj> visitTypes = searchDto.getVisitTypes();
            List<Integer> visitTypeIds = visitTypes.stream()
                    .map(obj::getId)
                    .collect(Collectors.toList());
            createdQuery.setParameterList("visitTypes", visitTypeIds);
        }

        if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()){
            List<obj> subDepts = searchDto.getSubDepts();
            List<Integer> subDeptIds = subDepts.stream()
                    .map(obj::getId)
                    .collect(Collectors.toList());
            createdQuery.setParameterList("subDepts", subDeptIds);
        }

        if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0){
            createdQuery.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId());
        }

        if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()){
            List<obj> testTypes = searchDto.getTestTypes();
            List<Integer> testTypeIds = testTypes.stream()
                    .map(obj::getId)
                    .collect(Collectors.toList());
            createdQuery.setParameterList("testTypes", testTypeIds);
        }

        if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0){
            createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());
        }

        if(searchDto.getUhidNumber()!="") {
            createdQuery.setParameter("uhidNumber", "%" + searchDto.getUhidNumber().toLowerCase() + "%");
        }

        if(searchDto.getSampleNo()!="") {
            createdQuery.setParameter("sampleNo", "%" + searchDto.getSampleNo().toLowerCase() + "%");
        }
      List<Integer> statusIds = new ArrayList<Integer>();
      
		if(searchDto.getIsSampleAcceptanceApplicable() == 'Y') {
			
			statusIds = Arrays.asList(SAMPLE_SEND_TO_DEPT);
			
		}else if (searchDto.getIsSendToCrApplicable() != null && searchDto.getIsSendToCrApplicable() == 'Y') {
						
			statusIds = Arrays.asList(SAMPLE_SEND_TO_CR);
			
		}else {
			
			statusIds = Arrays.asList(SAMPLE_COLLECTED);
		}
      List<SampleAcceptancePendingDto> listSampleAcceptancePendingDto = createdQuery
          .setParameter("orgId", searchDto.getOrgId())
          .setParameter("orgUnitId", searchDto.getOrgUnitId())
          .setParameterList("sampleStatusId", statusIds)
          .setParameter("deptId", searchDto.getDeptId())
          .setParameter("subDeptId", searchDto.getSubDeptId())
          .setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
          .setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
          .setResultTransformer(Transformers.aliasToBean(SampleAcceptancePendingDto.class)).list();


      if (listSampleAcceptancePendingDto.isEmpty()) 
      {
        return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.",
            listSampleAcceptancePendingDto, null);
      } else {
        return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, listSampleAcceptancePendingDto,
            null);
      }


    } catch (Exception e) {
      logger.error("Exection", e);
      throw e;
    }
  }

  @Override
  public Response getFilteredWorklistForDepartment(SearchDto searchDto)
      throws ApplicationException {
    try {

      String query = sessionFactory.getCurrentSession()
          .getNamedQuery("GET_BIOCHEMISTRY_WORKLIST").getQueryString().toString();

      StringBuilder sb = new StringBuilder(query);

      if (searchDto.getFromDt() != null ) {
    	  sb.append(
            " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt ");
      }

      if (searchDto.getToDt() != null ) {
    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt ");
      }

      if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
          sb.append(" AND pati_mst.patient_id =:patientId ");
      }
      if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()) {
          sb.append(" AND visit_type_master.visit_type_id IN (:visitTypes)");
      }
      if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()) {
          sb.append(" AND test_mst.dept_id IN (:subDepts)");
      }
      if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()) {
          sb.append(" AND test_mst.test_id in (:testTypes)");
      }
      if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0) {
          sb.append(" AND lbsampledtls.lab_sample_dtls_id =:labSampleDtlsId ");
      }
      if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0) {
          sb.append("  and lbsampledtls.assign_unit  =:assignUnit ");
      }
      if(searchDto.getUhidNumber()!="") {
    	  sb.append(" AND lower(pati_mst.uhid_number) LIKE :uhidNumber ");
      }
      
      if(searchDto.getSampleNo()!="") {
    	  sb.append(" AND lower(sample_mst.sample_no) LIKE :sampleNo ");
      }

      
      sb.append( 	 " ORDER BY   " 
				+ "  lbsampledtls.priority_id,   " 
				+ "  lbsampledtls.sample_accept_datetime   ");
      
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

        if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()){
            List<obj> visitTypes = searchDto.getVisitTypes();
            List<Integer> visitTypeIds = visitTypes.stream()
                    .map(obj::getId)
                    .collect(Collectors.toList());
            createdQuery.setParameterList("visitTypes", visitTypeIds);
        }

        if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()){
            List<obj> subDepts = searchDto.getSubDepts();
            List<Integer> subDeptIds = subDepts.stream()
                    .map(obj::getId)
                    .collect(Collectors.toList());
            createdQuery.setParameterList("subDepts", subDeptIds);
        }

        if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()){
            List<obj> testTypes = searchDto.getTestTypes();
            List<Integer> testTypeIds = testTypes.stream()
                    .map(obj::getId)
                    .collect(Collectors.toList());
            createdQuery.setParameterList("testTypes", testTypeIds);
        }

        if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0){
            createdQuery.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId());
        }

        if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0){
            createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());
        }

        if(searchDto.getUhidNumber()!="") {
            createdQuery.setParameter("uhidNumber", "%" + searchDto.getUhidNumber().toLowerCase() + "%");
        }

        if(searchDto.getSampleNo()!="") {
            createdQuery.setParameter("sampleNo", "%" + searchDto.getSampleNo().toLowerCase() + "%");
        }

      List<Integer> statusIds = new ArrayList<Integer>();
      
      if(searchDto.getIsAcceptanceInDepartment() == 'Y') {
			
			statusIds = bioChemStatus;
			
		}else if(searchDto.getIsSampleAcceptanceApplicable() == 'Y') {
			
			statusIds = Arrays.asList(SAMPLE_SEND_TO_DEPT,RETESTING);
			
		}else if (searchDto.getIsSendToCrApplicable() != null && searchDto.getIsSendToCrApplicable() == 'Y') {
						
			statusIds = Arrays.asList(SAMPLE_SEND_TO_CR,RETESTING);
			
		}else {
			
			statusIds = Arrays.asList(SAMPLE_COLLECTED,RETESTING);
		}

      List<SampleAcceptancePendingDto> listSampleAcceptancePendingDto = createdQuery

          .setParameter("orgId", searchDto.getOrgId())
          .setParameter("orgUnitId", searchDto.getOrgUnitId())
          .setParameterList("sampleStatusIds", statusIds)
          .setParameter("deptId", searchDto.getDeptId())
          .setParameter("subDeptId", searchDto.getSubDeptId())
          .setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
		  .setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
          .setResultTransformer(Transformers.aliasToBean(SampleAcceptancePendingDto.class)).list();


      if (listSampleAcceptancePendingDto.isEmpty()) {
        return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.",
            listSampleAcceptancePendingDto, null);
      } else {
        return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, listSampleAcceptancePendingDto,
            null);
      }


    } catch (Exception e) {
      logger.error("Exection", e);
      throw e;
    }
  }
  @Override
  public Response sampleAcceptancePending(BioChemParamDto bioChemParamDto)
      throws ApplicationException {
    List<SampleAcceptancePendingDto> listSampleAcceptancePendingDto = null;
    try 
    {
    	
    	String query =  sessionFactory.getCurrentSession()
    	          .getNamedQuery("GET_ACCEPTANCE_PENDING_LIST").getQueryString();
    	StringBuffer sb = new StringBuffer(query);
    	sb.append("  and lbsampledtls.assign_unit  =:orgUnitId ");
    	sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') >=:currentDate" );
		sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') <=:currentDate ");
    	sb.append( 	 " ORDER BY   " 
				+ "  lbsampledtls.priority_id,   " 
				+ "  lbsampledtls.sample_accept_datetime   ");
   
       Query createdQuery  =sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
        createdQuery .setParameter("orgId", bioChemParamDto.getOrgId());
        createdQuery .setParameter("orgUnitId", bioChemParamDto.getOrgUnitId());
        createdQuery .setParameter("sampleStatusId", SAMPLE_SEND_TO_DEPT);
        createdQuery .setParameter("deptId", bioChemParamDto.getDeptId());
        createdQuery .setParameter("subDeptId", bioChemParamDto.getSubDeptId());
        createdQuery .setParameter("orgUnitId", bioChemParamDto.getOrgUnitId());
        createdQuery .setParameter("currentDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(bioChemParamDto.getOrgUnitId()),"yyyy-MM-dd"));
        createdQuery .setFirstResult(bioChemParamDto.getOffset() != null ? bioChemParamDto.getOffset() : 0);
        createdQuery .setMaxResults(
              bioChemParamDto.getRecordPerPage() != null ? bioChemParamDto.getRecordPerPage() : 10);
        listSampleAcceptancePendingDto= createdQuery .setResultTransformer(Transformers.aliasToBean(SampleAcceptancePendingDto.class)).list();
      return new Response(SUCCESS, SUCCESS_200, null, listSampleAcceptancePendingDto, null);
    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }

  @Override
  public Response sampleAcceptancePendingCount(BioChemParamDto bioChemParamDto)
      throws ApplicationException {
    try {
    	
    	String query =  sessionFactory.getCurrentSession()
  	          .getNamedQuery("GET_ACCEPTANCE_PENDING_COUNT").getQueryString();
  	StringBuffer sb = new StringBuffer(query);
  	sb.append("  and lbsampledtls.assign_unit  =:assignUnit ");
	//sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') >= '" + GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId),"yyyy-MM-dd") + "' ");
	//sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') <= '" + GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId),"yyyy-MM-dd") + "' ");
  	
  	 if (bioChemParamDto.getFromDate() != null ) 
     {
   	  sb.append( " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDate ");
     }

     if (bioChemParamDto.getToDate() != null ) {
   	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDate ");
     }
  	
     List<Integer> statusIds = new ArrayList<Integer>();
     
   		if(bioChemParamDto.getIsSampleAcceptanceApplicable() == 'Y') {
   			
   			statusIds = Arrays.asList(SAMPLE_SEND_TO_DEPT);
   			
   		}else if (bioChemParamDto.getIsSendToCrApplicable() != null && bioChemParamDto.getIsSendToCrApplicable() == 'Y') {
   						
   			statusIds = Arrays.asList(SAMPLE_SEND_TO_CR,SAMPLE_COLLECTED);
   			
   		}else {
   			
   			statusIds = Arrays.asList(SAMPLE_COLLECTED);
   		}
   		
     Query sql = sessionFactory.getCurrentSession()
          .createSQLQuery(sb.toString())
          .setParameter("orgId", bioChemParamDto.getOrgId())
          .setParameterList("sampleStatusId", statusIds)
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
        BigInteger sampleAcceptancePendingCount = (BigInteger) sql.uniqueResult();
      if (sampleAcceptancePendingCount.compareTo(BigInteger.ZERO) == 1) {
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null,
            sampleAcceptancePendingCount);
      } else {
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, BigInteger.ZERO);
      }

    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }

  @Override
  public Response bioChemistryAcceptRejectSample(
      List<LabSampleDetailsMasterDto> listlabSampleDetailsMasterDto) throws ApplicationException {
    try {
      Integer flag = null;
      LabSampleDetailsMaster labSampleDetailsMaster;
      if (!listlabSampleDetailsMasterDto.isEmpty()) {
    	  Date date = TimeZoneComponent.getDateByzone(listlabSampleDetailsMasterDto.get(0).getOrgUnitId());
    	  
        for (Iterator iterator = listlabSampleDetailsMasterDto.iterator(); iterator.hasNext();) {
          LabSampleDetailsMasterDto labSampleDetailsMasterDto =
              (LabSampleDetailsMasterDto) iterator.next();
          labSampleDetailsMaster = findById(labSampleDetailsMasterDto.getLabSampleDtlsId());
          if (labSampleDetailsMasterDto.getCurrStatus().equals(ACCEPT)) 
          {
            labSampleDetailsMaster.setSampleAcceptBy(labSampleDetailsMasterDto.getSampleAcceptBy());
            labSampleDetailsMaster.setSamplePendingCount(labSampleDetailsMasterDto.getSamplePendingCount());
            labSampleDetailsMaster.setSampleAcceptDatetime(date.getTime());
            labSampleDetailsMaster.setUpdatedBy(labSampleDetailsMasterDto.getUpdatedBy());
            labSampleDetailsMaster.setUpdatedDate(date.getTime());
            labSampleDetailsMaster.setSampleStatusId(SAMPLE_ACCEPTED);
            labSampleDetailsMaster.setPerformedUnit(labSampleDetailsMasterDto.getOrgUnitId());
            update(labSampleDetailsMaster);
            flag = 0;

          } else if (labSampleDetailsMasterDto.getCurrStatus().equals(REJECT)) {
            labSampleDetailsMaster.setSampleStatusId(SAMPLE_REJECTED);
            labSampleDetailsMaster
                .setSampleRejectReason(labSampleDetailsMasterDto.getSampleRejectReason());
            labSampleDetailsMaster
                .setSampleRejectReasonId(labSampleDetailsMasterDto.getSampleRejectReasonId());
            labSampleDetailsMaster.setSampleRejectBy(labSampleDetailsMasterDto.getSampleRejectBy());
            labSampleDetailsMaster.setUpdatedBy(labSampleDetailsMasterDto.getUpdatedBy());
            labSampleDetailsMaster.setUpdatedDate(date.getTime());
            labSampleDetailsMaster.setPerformedUnit(labSampleDetailsMasterDto.getOrgUnitId());
            update(labSampleDetailsMaster);
            flag = 1;
          }
        }
        //if (flag == 0) {
          return new Response(SUCCESS, SUCCESS_200, SAMPLE_ACCEPT_SUCC, null, null);
      //  } else {
       //   return new Response(ERROR, ERR_500, SAMPLE_REJECT_SUCC, null, null);
       // }
        // return new Response(SUCCESS, SUCCESS_200, SAMPLE_COLLECT_SUCC, null, null);
      }
    } catch (Exception e) {
      logger.error("Exection", e);
      return new Response(ERROR, ERR_500, SAMPLE_REJECT_SUCC, null, null);
    }
    return null;

  }

  @Override
  public Response getBioChemistryWorklist(BioChemParamDto bioChemParamDto)
      throws ApplicationException {
    List<SampleAcceptancePendingDto> list = null;
    try {
    	
    	String query =  sessionFactory.getCurrentSession().getNamedQuery("GET_BIOCHEMISTRY_WORKLIST").getQueryString();
    	StringBuffer sb = new StringBuffer(query);
    	sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') >=:currentDate ");
    	sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') <=:currentDate ");
    	sb.append("  and lbsampledtls.assign_unit  =:orgUnitId ");
    	sb.append( 	 " ORDER BY   " 
  				+ "  lbsampledtls.priority_id,   " 
  				+ "  lbsampledtls.sample_accept_datetime   ");
     
    	list = sessionFactory.getCurrentSession()
          .createSQLQuery(sb.toString())
          .setParameter("orgId", bioChemParamDto.getOrgId())
          .setParameter("orgUnitId", bioChemParamDto.getOrgUnitId())
          .setParameterList("sampleStatusIds", bioChemStatus)
          .setParameter("deptId", bioChemParamDto.getDeptId())
          .setParameter("subDeptId", bioChemParamDto.getSubDeptId())
          .setParameter("currentDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(bioChemParamDto.getOrgUnitId()),"yyyy-MM-dd"))
          .setFirstResult(bioChemParamDto.getOffset() != null ? bioChemParamDto.getOffset() : 0)
          .setMaxResults(
              bioChemParamDto.getRecordPerPage() != null ? bioChemParamDto.getRecordPerPage() : 10)
          .setResultTransformer(Transformers.aliasToBean(SampleAcceptancePendingDto.class)).list();
      return new Response(SUCCESS, SUCCESS_200, null, list, null);
    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }

  @Override
  public Response getBioChemistryWorklistCount(BioChemParamDto bioChemParamDto)
      throws ApplicationException {
    try {
    	
    	
    	String query =  sessionFactory.getCurrentSession().getNamedQuery("GET_BIOCHEMISTRY_WORKLIST_COUNT").getQueryString();
    	StringBuffer sb = new StringBuffer(query);
    	//sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') >= '" + GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId),"yyyy-MM-dd") + "' ");
    	//sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') <= '" + GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId),"yyyy-MM-dd") + "' ");
    	
    	 if (bioChemParamDto.getFromDate() != null ) 
	      {
	    	  sb.append( " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDate ");
	      }

	      if (bioChemParamDto.getToDate() != null ) {
	    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDate ");
	      }
    	
    	sb.append("  and lbsampledtls.assign_unit  =:assignUnit ");
    	
    	 List<Integer> statusIds = new ArrayList<Integer>();
         
         if(bioChemParamDto.getIsAcceptanceInDepartment() == 'Y') {
   			
   			statusIds = bioChemStatus;
   			
   		}else if(bioChemParamDto.getIsSampleAcceptanceApplicable() == 'Y') {
   			
   			statusIds = Arrays.asList(SAMPLE_SEND_TO_DEPT);
   			
   		}else if (bioChemParamDto.getIsSendToCrApplicable() != null && bioChemParamDto.getIsSendToCrApplicable() == 'Y') {
   						
   			statusIds =Arrays.asList(SAMPLE_SEND_TO_CR,SAMPLE_COLLECTED);
   			
   		}else {
   			
   			statusIds = Arrays.asList(SAMPLE_COLLECTED);
   		}

    	
      Query sql = sessionFactory.getCurrentSession()
          .createSQLQuery(sb.toString())
          .setParameter("orgId", bioChemParamDto.getOrgId())
          .setParameterList("sampleStatusIds", statusIds)
          .setParameter("deptId", bioChemParamDto.getDeptId())
          .setParameter("subDeptId", bioChemParamDto.getSubDeptId())
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
        BigInteger sampleAcceptancePendingCount = (BigInteger) sql.uniqueResult();
      if (sampleAcceptancePendingCount.compareTo(BigInteger.ZERO) == 1) {
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null,
            sampleAcceptancePendingCount);
      } else {
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, BigInteger.ZERO);
      }

    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }

  @Override
  public Response sendToResultEntry(List<LabSampleDetailsMasterDto> listlabSampleDetailsMasterDto)
      throws ApplicationException {
    try {
      LabSampleDetailsMaster labSampleDetailsMaster;
      if (!listlabSampleDetailsMasterDto.isEmpty()) {
    	  Date date = TimeZoneComponent.getDateByzone(listlabSampleDetailsMasterDto.get(0).getOrgUnitId());
    	  
        for (Iterator iterator = listlabSampleDetailsMasterDto.iterator(); iterator.hasNext();) {
          LabSampleDetailsMasterDto labSampleDetailsMasterDto =
              (LabSampleDetailsMasterDto) iterator.next();
          labSampleDetailsMaster = findById(labSampleDetailsMasterDto.getLabSampleDtlsId());
          labSampleDetailsMaster.setUpdatedBy(labSampleDetailsMasterDto.getUpdatedBy());
          labSampleDetailsMaster.setUpdatedDate(date.getTime());
          labSampleDetailsMaster.setSampleStatusId(SEND_TO_REPORT_ENTRY);
          labSampleDetailsMaster.setPerformedUnit(labSampleDetailsMasterDto.getOrgUnitId());
          update(labSampleDetailsMaster);
        }
      }
      return new Response(SUCCESS, SUCCESS_200, SAMPLE_SEND_TO_RES_ENTRY_SUCC, null, null);
    } catch (Exception e) {
      logger.error("Exection", e);
      return new Response(ERROR, ERR_500, SAMPLE_SEND_TO_RES_ENTRY_FAIL, null, null);
    }
  }

  @Override
  public Response sampleRetest(List<LabResultMasterDto> listLabResultMasterDto)
      throws ApplicationException {

    try {

    	 Date date = TimeZoneComponent.getDateByzone(listLabResultMasterDto!=null && listLabResultMasterDto.size()>0 
    			 ? listLabResultMasterDto.get(0).getOrgUnitId() : null);
    	
      for (Iterator iterator = listLabResultMasterDto.iterator(); iterator.hasNext();) 
      {
        LabResultMasterDto labResultMasterDto = (LabResultMasterDto) iterator.next();
        LabResultMaster labResultMaster = mapper.map(labResultMasterDto, LabResultMaster.class,
            "LabResultMasterDtoToLabResultMaster");
        labResultMaster.setResultEnterBy(labResultMasterDto.getCreatedBy());
        labResultMaster.setCreatedDate(date.getTime());
        
        labResultMaster.setCreatedBy(labResultMasterDto.getCreatedBy());
        labResultMaster.setResultEnterDatetime(date.getTime());
        labResultMaster.setManualResultEnterTime(date.getTime());
        labResultMaster.setStatus('A');
        Integer labResultId = (Integer) sessionFactory.getCurrentSession().save(labResultMaster);
        LabSampleDetailsMaster labSampleDetailsMaster =
            (LabSampleDetailsMaster) sessionFactory.getCurrentSession()
                .get(LabSampleDetailsMaster.class, labResultMasterDto.getLabSampleDtlsId());
        Integer sampleStatusId = 8;
        if(labSampleDetailsMaster.getSampleStatusId().equals(sampleStatusId)) {
        	Integer performTestId = labSampleDetailsMaster.getPerformTestId();
        	Integer performedUnitId = labSampleDetailsMaster.getPerformedUnit();
        	Integer testId = labSampleDetailsMaster.getTestId();
        	String date1 = GlobalCommonDateUtils.getAvalDate(TimeZoneComponent.getDateByzone(labSampleDetailsMaster.getOrgUnitId()), "yyyy-M-dd HH:mm:ss");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
            Date parsedDate = dateFormat.parse(date1);
            Timestamp packageApplicableTimestamp = new Timestamp(parsedDate.getTime());

        	Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update lab.t_lab_sample_dtls  set "
        			+ " sample_status_id =:retesting "
        			+ ", perform_test_id =:testId "
        			+ ",assign_unit =:performedUnitId "
        			+ ", test_id =:performTestId "
        			+ ", performed_unit =:orgUnitId "
        			//+ ", updated_by =:updatedBy "
        			+ ", updated_date =:updatedDate ,"
        			+ " is_on_hold_parameter = false "
        			+ "where lab_sample_dtls_id = :labSampleDtlsId ")
                    .setParameter("retesting", RETESTING)
                    .setParameter("testId", testId)
                    .setParameter("performedUnitId", performedUnitId)
                    .setParameter("performTestId", performTestId)
                    .setParameter("orgUnitId", labSampleDetailsMaster.getOrgUnitId())
                    //.setParameter("updatedBy", labResultMasterDto.getUpdatedBy())
                    .setParameter("updatedDate", packageApplicableTimestamp)
                    .setParameter("labSampleDtlsId", labSampleDetailsMaster.getLabSampleDtlsId())
					.executeUpdate();
        	
        	
        }else {
        	 labSampleDetailsMaster.setUpdatedBy(labResultMasterDto.getUpdatedBy());
             labSampleDetailsMaster.setUpdatedDate(date.getTime());
             labSampleDetailsMaster.setSampleStatusId(RETESTING);
             labSampleDetailsMaster.setIsOnHoldParameter(false);
             sessionFactory.getCurrentSession().save(labSampleDetailsMaster);
        }
        
       
        for (Iterator iterator1 =
            labResultMasterDto.getListLabSampleResultDetailsMaster().iterator(); iterator1
                .hasNext();) {
          LabResultDetailsViewDto labResultDetailsMasterDto =
              (LabResultDetailsViewDto) iterator1.next();
          LabResultDetailsMaster labResultDetailsMaster = mapper.map(labResultDetailsMasterDto,
              LabResultDetailsMaster.class, "LabResultDetailsViewDtoTOLabResultDetailsMaster");
          labResultDetailsMaster.setLabResultId(labResultId);
          labResultDetailsMaster.setFirstLevelResult(labResultDetailsMaster.getFinalResult());
          labResultDetailsMaster.setCreatedBy(labResultMasterDto.getCreatedBy());
          labResultDetailsMaster.setCreatedDate(date.getTime());
          labSampleDetailsMaster.setPerformedUnit(labResultMasterDto.getOrgUnitId());
          sessionFactory.getCurrentSession().save(labResultDetailsMaster);
        }

      }
      return new Response(SUCCESS, SUCCESS_200, SAMPLE_RETEST_SUCC, null, null);
    } catch (Exception e) {
      logger.error("Exception", e);
      return new Response(ERROR, ERR_500, SAMPLE_RETEST_FAIL, null, null);
    }
  }

  @Override
  public Response biochemFinalReportRelease(LabSampleDetailsMasterDto labSampleDetailsMasterDto)
      throws ApplicationException {
    try {
    	
      LabSampleDetailsMaster labSampleDetailsMaster = null;
      if (labSampleDetailsMasterDto != null) {
    	  Date date = TimeZoneComponent.getDateByzone(labSampleDetailsMasterDto.getOrgUnitId());
        labSampleDetailsMaster = findById(labSampleDetailsMasterDto.getLabSampleDtlsId());
        labSampleDetailsMaster.setUpdatedBy(labSampleDetailsMasterDto.getUpdatedBy());
        labSampleDetailsMaster.setUpdatedDate(date.getTime());
        labSampleDetailsMaster.setSampleStatusId(FINAL_REPORT_RELEASED);
        labSampleDetailsMaster.setPerformedUnit(labSampleDetailsMasterDto.getOrgUnitId());
        labSampleDetailsMaster = update(labSampleDetailsMaster);
      }
      return new Response(SUCCESS, SUCCESS_200, SAMPLE_RETEST_SUCC, null, labSampleDetailsMaster);


    } catch (Exception e) {
      logger.error("Exection", e);
      return new Response(ERROR, ERR_500, SAMPLE_RETEST_FAIL, null, null);
    }
  }

  @Override
  public Response sampleRecollect(List<LabResultMasterDto> listLabResultMasterDto)
      throws ApplicationException {


    try {

    	 Date date = TimeZoneComponent.getDateByzone(listLabResultMasterDto!=null && listLabResultMasterDto.size()>0 
    			 ? listLabResultMasterDto.get(0).getOrgUnitId() : null );
      for (Iterator iterator = listLabResultMasterDto.iterator(); iterator.hasNext();) 
      
      {
        LabResultMasterDto labResultMasterDto = (LabResultMasterDto) iterator.next();
        LabResultMaster labResultMaster = mapper.map(labResultMasterDto, LabResultMaster.class,
            "LabResultMasterDtoToLabResultMaster");
        labResultMaster.setResultEnterBy(labResultMasterDto.getCreatedBy());
        labResultMaster.setCreatedDate(date.getTime());
        labResultMaster.setCreatedBy(labResultMasterDto.getCreatedBy());
        labResultMaster.setResultEnterDatetime(date.getTime());
        labResultMaster.setManualResultEnterTime(date.getTime());
        labResultMaster.setStatus('A');
        Integer labResultId = (Integer) sessionFactory.getCurrentSession().save(labResultMaster);
        LabSampleDetailsMaster labSampleDetailsMaster =
            (LabSampleDetailsMaster) sessionFactory.getCurrentSession()
                .get(LabSampleDetailsMaster.class, labResultMasterDto.getLabSampleDtlsId());
        labSampleDetailsMaster.setUpdatedBy(labResultMasterDto.getUpdatedBy());
        labSampleDetailsMaster.setUpdatedDate(date.getTime());
        labSampleDetailsMaster.setSampleStatusId(SAMPLE_REJECTED);
        labSampleDetailsMaster.setSampleRejectReasonId(labResultMasterDto.getSampleRejectReasonId());
        labSampleDetailsMaster.setSampleRejectReason(labResultMasterDto.getSampleRejectReason());
        labSampleDetailsMaster.setPerformedUnit(labResultMasterDto.getOrgUnitId());
        labSampleDetailsMaster.setAssignUnit(labResultMasterDto.getAssignUnit());
        labSampleDetailsMaster.setTestId(labResultMasterDto.getPerformTestId()!= null ? labResultMasterDto.getPerformTestId(): labResultMasterDto.getTestId());
        labSampleDetailsMaster.setPerformTestId(labResultMasterDto.getTestId());
        labSampleDetailsMaster.setIsOnHoldParameter(false);
        sessionFactory.getCurrentSession().update(labSampleDetailsMaster);
        for (Iterator iterator1 =
            labResultMasterDto.getListLabSampleResultDetailsMaster().iterator(); iterator1
                .hasNext();) {
          LabResultDetailsViewDto labResultDetailsMasterDto =
              (LabResultDetailsViewDto) iterator1.next();
          LabResultDetailsMaster labResultDetailsMaster = mapper.map(labResultDetailsMasterDto,
              LabResultDetailsMaster.class, "LabResultDetailsViewDtoTOLabResultDetailsMaster");
          labResultDetailsMaster.setLabResultId(labResultId);
          labResultDetailsMaster.setFirstLevelResult(labResultDetailsMaster.getFinalResult());
          labResultDetailsMaster.setCreatedBy(labResultMasterDto.getCreatedBy());
          labResultDetailsMaster.setCreatedDate(date.getTime());
          sessionFactory.getCurrentSession().save(labResultDetailsMaster);
        }

      }
      return new Response(SUCCESS, SUCCESS_200, SAMPLE_SEND_RECOLLECT_SUCC, null, null);
    } catch (Exception e) {
      logger.error("Exception", e);
      return new Response(ERROR, ERR_500, SAMPLE_SEND_RECOLLECT_FAIL, null, null);
    }

  }

  @Override
  public Response resultEntryData(ResultEntryDataDto resultEntryDataDto)
      throws ApplicationException {
    List<ResultEntryDataDto> listResultEntryDataDto = null;
    try {
      String level_data = "";
      Integer resultLevel = null;
      switch (resultEntryDataDto.getFirstLevelResult()) {
        case "firstLevel":
          level_data = ".first_level_result";
          resultLevel = 1;
          break;

        case "secondLevel":
          level_data = ".second_level_result";
          resultLevel = 2;
          break;

        case "thirdLevel":
          level_data = ".third_level_result";
          resultLevel = 3;
          break;

        default:
          level_data = "";
          break;
      }


      listResultEntryDataDto = (List<ResultEntryDataDto>) sessionFactory.getCurrentSession()
          .createSQLQuery("select "
        		  + "   labresdtls.final_result as \"firstlevelresult\" "
        		  + "from "
        		  + "	lab.t_lab_res_dtls labresdtls "
        		  + "inner join lab.t_lab_test_result labtestresult on "
        		  + "labtestresult.lab_test_res_id = labresdtls.lab_result_id "
        		  + "where "
        		  + "	 labtestresult.lab_sample_dtls_id =:labSampleDetailsId "
        		  + "	and labtestresult.org_id = :orgId "
        		  + "	and labtestresult.org_unit_id = :orgUnitId "
        		  + "   and labtestresult.sample_status_id = :sampleStatusId "
                  +"    and labresdtls.parameter_id =:parameterId "
        		  + "	order by labtestresult.lab_test_res_id DESC"
        		  )
          .setParameter("labSampleDetailsId", resultEntryDataDto.getSampleRecollectAgainstId())
          .setParameter("orgId", resultEntryDataDto.getOrgId())
          .setParameter("orgUnitId", resultEntryDataDto.getOrgUnitId())
          .setParameter("parameterId", resultEntryDataDto.getParameterId())
          .setParameter("sampleStatusId", SAMPLE_REJECTED)
          .setResultTransformer(Transformers.aliasToBean(ResultEntryDataDto.class)).list();
      return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listResultEntryDataDto, null);


      /*
       * listResultEntryDataDto = sessionFactory.getCurrentSession()
       * .getNamedQuery("GET_FIRST_LEVEL_RESULT") .setParameter("orgId",
       * resultEntryDataDto.getOrgId()) .setParameter("orgUnitId", resultEntryDataDto.getOrgUnitId())
       * .setParameter("labResultId", resultEntryDataDto.getLabResultId())
       * .setResultTransformer(Transformers.aliasToBean(ResultEntryDataDto.class)).list(); return
       * new Response(SUCCESS, SUCCESS_200, null, listResultEntryDataDto, null);
       */
    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }

  @Override
  public Response addReportHistroyMaster(LabSampleDetailsMaster labSampleDetailsMaster,
      Integer resultId) throws ApplicationException {
    try {
      ReportHistoryMaster reportHistoryMaster = new ReportHistoryMaster();
      reportHistoryMaster.setCreatedBy(labSampleDetailsMaster.getCreatedBy());
      reportHistoryMaster.setCreatedDate(labSampleDetailsMaster.getCreatedDate());
      reportHistoryMaster.setUpdatedBy(labSampleDetailsMaster.getUpdatedBy());
      reportHistoryMaster.setUpdatedDate(labSampleDetailsMaster.getUpdatedDate());
      reportHistoryMaster.setDeptId(labSampleDetailsMaster.getDeptId());
      reportHistoryMaster.setIsReportRelease('Y');
      reportHistoryMaster.setOrgId(labSampleDetailsMaster.getOrgId());
      reportHistoryMaster.setOrgUnitId(labSampleDetailsMaster.getOrgUnitId());
      reportHistoryMaster.setStatus('A');
      reportHistoryMaster.setPatientId(labSampleDetailsMaster.getPatientId());
      reportHistoryMaster.setResultId(resultId);
      reportHistoryMaster.setSubdeptId(labSampleDetailsMaster.getSubDeptId());
      reportHistoryMaster.setLabSampleDtlsId(labSampleDetailsMaster.getLabSampleDtlsId());
      reportHistoryMaster.setTestId(labSampleDetailsMaster.getTestId());

      Integer result = (Integer) sessionFactory.getCurrentSession().save(reportHistoryMaster);
      if (result > 0) {
        return new Response<>(SUCCESS, SUCCESS_200, REPORT_HISTORY_ADD_SUCC, null, null);
      } else {
        return new Response<>(ERROR, ERR_201, REPORT_HISTORY_ADD_FAIL, null, null);
      }

    } catch (HibernateException he) {
      logger.error("Exception in addReportHistroyMaster", he);
      throw new ServiceException(he.getMessage());
    } catch (Exception e) {
      logger.error("Exception in addReportHistroyMaster", e);
      throw new ServiceException(e.getMessage());
    }
  }

  @Override
  public Response getParameterHistory(ParameterSearchDto parameterSearchDto)
      throws ApplicationException {
    try {

      String GET_PARAMETER_FOR_HISTORY = sessionFactory.getCurrentSession()
          .getNamedQuery("GET_PARAMETER_FOR_HISTORY").getQueryString().toString();
      StringBuilder GET_PARAMETER_DETAILS = new StringBuilder(GET_PARAMETER_FOR_HISTORY);
      List<ParameterHistoryDto> parameterHistoryDtoList = null;
      if (parameterSearchDto.getParameterIds().size() > 0
          && !parameterSearchDto.getParameterIds().isEmpty()) {
        GET_PARAMETER_DETAILS.append(" AND parameter_master.parameter_id IN (:parameterIds)");
        parameterHistoryDtoList = (List<ParameterHistoryDto>) sessionFactory.getCurrentSession()
            .createSQLQuery(GET_PARAMETER_DETAILS.toString())
            .setParameter("testId", parameterSearchDto.getTestId())
            .setParameter("orgId", parameterSearchDto.getOrgId())
            .setParameter("orgUnitId", parameterSearchDto.getOrgUnitId())
            .setParameterList("parameterIds", parameterSearchDto.getParameterIds())
            .setResultTransformer(Transformers.aliasToBean(ParameterHistoryDto.class)).list();
      } else {
        parameterHistoryDtoList = (List<ParameterHistoryDto>) sessionFactory.getCurrentSession()
            .createSQLQuery(GET_PARAMETER_DETAILS.toString())
            .setParameter("testId", parameterSearchDto.getTestId())
            .setParameter("orgId", parameterSearchDto.getOrgId())
            .setParameter("orgUnitId", parameterSearchDto.getOrgUnitId())
            .setResultTransformer(Transformers.aliasToBean(ParameterHistoryDto.class)).list();
      }
      if (!parameterHistoryDtoList.isEmpty()) {
        for (Iterator iterator = parameterHistoryDtoList.iterator(); iterator.hasNext();) {
          ParameterHistoryDto parameterHistoryDto = (ParameterHistoryDto) iterator.next();
         Query sql = sessionFactory.getCurrentSession()
                  .createSQLQuery(
                      " SELECT  "
                      +" CAST(  "
                      +"     lab_res_dtls.final_result AS TEXT  "
                      +" ) AS \"result\",  "
                      +" lab_res_dtls.created_date AS \"resultDateTime\"  "
                      +"FROM  "
                      +" lab.t_lab_test_result lab_test_result  "
                      +"INNER JOIN lab.t_lab_res_dtls lab_res_dtls ON  "
                      +" lab_res_dtls.lab_result_id = lab_test_result.lab_test_res_id  "
                      +"WHERE  "
                      +" lab_test_result.sample_status_id =:sampleStatusId "
                      +" AND lab_test_result.test_id =:testId "
                      +" AND lab_test_result.patient_id =:patientId "
                      +" AND lab_res_dtls.parameter_id =:parameterId "
                      +" AND lab_test_result.org_id =:orgId "
                      +" AND lab_test_result.org_unit_id =:orgUnitId "
                      +" AND lab_test_result.result_level = 3  "
                      +" ORDER BY  "
                      +" lab_res_dtls.created_date DESC LIMIT "+ parameterSearchDto.getLimit());
                     sql .setParameter("sampleStatusId", REPORT_RELEASED);
                     sql .setParameter("testId", parameterSearchDto.getTestId());
                     sql .setParameter("patientId", parameterSearchDto.getPatientId());
                     sql .setParameter("parameterId", parameterHistoryDto.getParameterId());
                     sql .setParameter("orgId", parameterSearchDto.getOrgId());
                     sql .setParameter("orgUnitId", parameterSearchDto.getOrgUnitId());

            List<ParameterResultDto> parameterResultDtoList =
                    (List<ParameterResultDto>) sql.setResultTransformer(Transformers.aliasToBean(ParameterResultDto.class)).list();
          parameterHistoryDto.setListParameterResultDto(parameterResultDtoList);
        }
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, parameterHistoryDtoList,
            null);
      } else {
        return new Response(SUCCESS, SUCCESS_200, "No Parameter Found.", null, null);
      }


    } catch (HibernateException he) {
      logger.error("Exception in getParameterHistory", he);
      throw new ServiceException(he.getMessage());
    } catch (Exception e) {
      logger.error("Exception in getParameterHistory", e);
      throw new ServiceException(e.getMessage());
    }
  }

  @Override
  public Response getPreviousResultIdByTest(BioChemParamDto bioChemParamDto)
      throws ApplicationException {
    try {
      Integer labResultId =
          (Integer) sessionFactory.getCurrentSession().getNamedQuery("GET_PRIVIOUS_TEST_RELEASE_ID")
              .setParameter("orgId", bioChemParamDto.getOrgId())
              .setParameter("orgUnitId", bioChemParamDto.getOrgUnitId())
              .setParameter("deptId", bioChemParamDto.getDeptId())
              .setParameter("subDeptId", bioChemParamDto.getSubDeptId())
              .setParameter("sampleStatusId", REPORT_RELEASED)
              .setParameter("patientId", bioChemParamDto.getPatientId())
              .setParameter("testId", bioChemParamDto.getTestId()).uniqueResult();
      if (labResultId != null && labResultId > 0) {
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, labResultId);
      } else {
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, BigInteger.ZERO);
      }

    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }

  @Override
  public Response getRetestedRecollectedResult(BioChemParamDto bioChemParamDto)
      throws ApplicationException {
    List<SingParamTestResDto> listLabResultMasterDto = null;
    try {
      listLabResultMasterDto =
          sessionFactory.getCurrentSession().getNamedQuery("GET_RETEST_RECOLLECT_LAB_RESULT_ID")
              .setParameter("orgId", bioChemParamDto.getOrgId())
              .setParameter("orgUnitId", bioChemParamDto.getOrgUnitId())
              .setParameter("retest", RETESTING)
              .setParameter("recollect", SAMPLE_REJECTED)
              .setParameter("deptId", bioChemParamDto.getDeptId())
              .setParameter("subDeptId", bioChemParamDto.getSubDeptId())
              .setParameter("patientId", bioChemParamDto.getPatientId())
              .setParameter("labSampleDtlsId", bioChemParamDto.getLabSampleDtlsId())
              .setParameter("testId", bioChemParamDto.getTestId())
              .setResultTransformer(Transformers.aliasToBean(SingParamTestResDto.class)).list();
      return new Response(SUCCESS, SUCCESS_200, null, listLabResultMasterDto, null);
    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }

  @Override
  public Response getResultEnteryByDateTime(BioChemParamDto bioChemParamDto)
      throws ApplicationException {
    try {
      SingParamTestResDto singParamTestResDto =
          (SingParamTestResDto) sessionFactory.getCurrentSession().getNamedQuery("GET_RESULT_ENTERY_BY_DATE_TIME")
          .setParameter("orgId", bioChemParamDto.getOrgId())
          .setParameter("orgUnitId", bioChemParamDto.getOrgUnitId())
          .setParameter("labTestResId", bioChemParamDto.getLabResultId())
          .setResultTransformer(Transformers.aliasToBean(SingParamTestResDto.class)).uniqueResult();
      return new Response(SUCCESS, SUCCESS_200, null, null, singParamTestResDto);
    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }

  @Override
  public Response getResultValidatedByDateTime(BioChemParamDto bioChemParamDto)
      throws ApplicationException {
    try {
      SingParamTestResDto singParamTestResDto =
          (SingParamTestResDto) sessionFactory.getCurrentSession().getNamedQuery("GET_RESULT_VALIDATED_BY_DATE_TIME")
          .setParameter("orgId", bioChemParamDto.getOrgId())
        //  .setParameter("orgUnitId", bioChemParamDto.getOrgUnitId())
          .setParameter("labTestResId", bioChemParamDto.getLabResultId())
          .setResultTransformer(Transformers.aliasToBean(SingParamTestResDto.class)).uniqueResult();
      return new Response(SUCCESS, SUCCESS_200, null, null, singParamTestResDto);
    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }

  @Override
  public Response getResultReleaseByDateTime(BioChemParamDto bioChemParamDto)
      throws ApplicationException {
    try {
      SingParamTestResDto singParamTestResDto =
          (SingParamTestResDto) sessionFactory.getCurrentSession().getNamedQuery("GET_RESULT_RELEASE_BY_DATE_TIME")
          .setParameter("orgId", bioChemParamDto.getOrgId())
          .setParameter("orgUnitId", bioChemParamDto.getOrgUnitId())
          .setParameter("labTestResId", bioChemParamDto.getLabResultId())
          .setResultTransformer(Transformers.aliasToBean(SingParamTestResDto.class)).uniqueResult();
      return new Response(SUCCESS, SUCCESS_200, null, null, singParamTestResDto);
    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }

@Override
public Response getPatientPreviousTest(BioChemParamDto bioChemParamDto) throws ApplicationException {
	List<CommonDto>  listDeparmentDto  = null; 
	try
	{
		listDeparmentDto = (List<CommonDto>) sessionFactory.getCurrentSession()
				.createSQLQuery("select "
						+ "	distinct test_mast.test_id as id, "
						+ "	test_mast.test_desc as label "
						+ "from "
						+ "	lab.t_lab_sample_dtls dtls "
						+ "inner join lab.m_test_master test_mast on "
						+ "	dtls.test_id = test_mast.test_id "
						+ "where "
						+ "	dtls.sample_status_id = 8 "
						+ "	and dtls.patient_id = :patientId")
				.setParameter("patientId", bioChemParamDto.getPatientId())
                .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
		return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listDeparmentDto, null);

	} catch (Exception e)
	{
		logger.error("Exection", e);
		throw e;
	}
}

@Override
public Response patientTestParameter(BioChemParamDto bioChemParamDto) throws ApplicationException {
	List<CommonDto>  listDeparmentDto  = null; 
	try
	{
		listDeparmentDto = (List<CommonDto>) sessionFactory.getCurrentSession()
				.createSQLQuery("select "
						+ "	distinct paramter_mst.parameter_id as id, "
						+ "	paramter_mst.parameter_name as label "
						+ "from "
						+ "	lab.t_lab_sample_dtls dtls "
						+ "inner join lab.t_lab_test_result test_res on "
						+ "	dtls.lab_sample_dtls_id = test_res.lab_sample_dtls_id "
						+ "inner join lab.t_lab_res_dtls res_dlts on "
						+ "	test_res.lab_test_res_id = res_dlts.lab_result_id "
						+ "inner join lab.m_parameter_master paramter_mst on "
						+ "	res_dlts.parameter_id = paramter_mst.parameter_id "
						+ "where "
						+ "	dtls.sample_status_id = 8 "
						+ "	and test_res.sample_status_id = 8 "
						+ "	and dtls.patient_id = :patientId "
						+ "	and dtls.test_id = :testId")
				.setParameter("patientId", bioChemParamDto.getPatientId())
				.setParameter("testId", bioChemParamDto.getTestId())
                .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
		return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listDeparmentDto, null);

	} catch (Exception e)
	{
		logger.error("Exection", e);
		throw e;
	}
}

@Override
public Response saveInformTo(InformToMasterDto dto) throws ApplicationException {
	try
	{
		if(dto.getInformToId()==null)
		{
			InformToMaster master = new InformToMaster();
			master.setLabSampleDtlsId(dto.getLabSampleDtlsId());
			master.setDoctorId(dto.getDoctorId());
			master.setInformById(dto.getInformById());
			master.setCreatedBy(dto.getCreatedBy());
			master.setNote(dto.getNote());
			master.setInformDate(dto.getInformDate() != null && !dto.getInformDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getInformDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setOrganizationId(dto.getOrganizationId());
			master.setUnitId(dto.getUnitId());
			master.setStatus(ACTIVE);
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			Integer informToId = (Integer) sessionFactory.getCurrentSession().save(master);
			return new Response(SUCCESS, SUCCESS_200, COMMON_SUCCESS, null, null);
		}
		else
		{
			InformToMaster master = (InformToMaster) sessionFactory.getCurrentSession().get(InformToMaster.class, dto.getInformToId());
			master.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setNote(dto.getNote());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setInformDate((dto.getInformDate()!=null ? GlobalCommonDateUtils.getDate(dto.getInformDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId())));;
			sessionFactory.getCurrentSession().update(master);
			return new Response(SUCCESS, SUCCESS_200, COMMON_UPDATE, null, null);
		}

	} catch (Exception e)
	{
		logger.error("Exection", e);
		throw e;
	}
}

@Override
public Response getInformTo(InformToMasterDto dto) throws ApplicationException {
	try 
		{
		 try {
			 List<InformToMasterDto> list =
		          (List<InformToMasterDto>) sessionFactory.getCurrentSession().getNamedQuery("GET_INFORM_TO_LIST")
		          .setParameter("labSampleDtlsId", dto.getLabSampleDtlsId())
		          .setResultTransformer(Transformers.aliasToBean(InformToMasterDto.class)).list();
		      return new Response(SUCCESS, SUCCESS_200, null, list, null);
		    } catch (Exception e) {
		      logger.error("Exception", e);
		      throw e;
		    }
	}catch(

	Exception e)
	{
		logger.error("Exection", e);
		throw e;
	}
}

	@Override
	public Response getTreatingDoctor(InformToMasterDto dto) throws ApplicationException {
		try {
			try {
				List<InformToMasterDto> list = (List<InformToMasterDto>) sessionFactory.getCurrentSession()
						.getNamedQuery("GET_TREATING_DOCTOR_ID_BY_SAMPLE")
						.setParameter("labSampleDtlsId", dto.getLabSampleDtlsId())
						.setResultTransformer(Transformers.aliasToBean(InformToMasterDto.class)).list();
				if(list!=null && list.size()>0)
				{
					InformToMasterDto idto  =list.get(0);
					return new Response(SUCCESS, SUCCESS_200, null, list, idto);
				}
				else {
					return new Response(ERROR, ERR_500, "No treating doctor found.", null, null);
				}
				
			
			} catch (Exception e) {
				logger.error("Exception", e);
				throw e;
			}
		} catch (

		Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}
}

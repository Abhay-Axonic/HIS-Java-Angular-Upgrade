package com.param.lis.transaction.dao;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.param.entity.model.inventory.StoreStock;
import com.param.global.org.dto.OrganizationMasterDto;
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.transaction.LabResultDetailsMaster;
import com.param.entity.lis.transaction.LabResultMaster;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ServiceException;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.unit.dao.ILabConfigurationDao;
import com.param.global.unit.dto.LabConfigurationDto;
import com.param.lis.common.dto.CommonAutoCompleteDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.common.StringCommonMethods;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.LabResultDetailsViewDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.transaction.dto.SearchCommonDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BiochemReportReleaseDaoImpl extends GenericDao<LabResultMaster, Integer>
implements IBiochemReportReleaseDao, ICommonConstants, ITransactionConstants, IError {

public BiochemReportReleaseDaoImpl() 
{
  super(LabResultMaster.class);
}

@Autowired
private DozerBeanMapper mapper;

@Autowired
ILabConfigurationDao iLabConfigurationDao;

@Autowired
ILabTransactionDao iLabTransactionDao;

final static Logger logger = Logger.getLogger(BiochemReportReleaseDaoImpl.class);

@Override
public Response getReportReleaseWorkList(BioChemParamDto bioChemParamDto) throws ApplicationException {
	List<LabResultMasterDto> listLabResultMasterDto = null;
	try {
		
		  com.param.global.common.Response res =  iLabConfigurationDao.getLabConfigurationList(bioChemParamDto.getOrgId(),bioChemParamDto.getAssignUnit());
	      Character c = null;
	       if(res.getListObject() != null && res.getListObject().size()>0) {
	    	   
	    	   List<LabConfigurationDto> list = res.getListObject() ;
	    	  c = list.get(0).getSkipValidation();
	       }
	      
	       List<Integer> statusIds = new ArrayList<Integer>(); 
		      if(bioChemParamDto.getIsOnHoldParameter() == false) {
		       if(bioChemParamDto.getIsWorklistDepartment() == 'Y') {
		   			
		   			statusIds = Arrays.asList(SEND_TO_REPORT_ENTRY);
		   			
		   		}else if(bioChemParamDto.getIsAcceptanceInDepartment() == 'Y') {
		   			
		   			statusIds = bioChemStatus;
		   			
		   		}else if(bioChemParamDto.getIsSampleAcceptanceApplicable() == 'Y') {
		   			
		   			statusIds = Arrays.asList(SAMPLE_SEND_TO_DEPT,RETESTING);
		   			
		   		}else if (bioChemParamDto.getIsSendToCrApplicable() != null && bioChemParamDto.getIsSendToCrApplicable() == 'Y') {
		   						
		   			statusIds =Arrays.asList(SAMPLE_SEND_TO_CR,SAMPLE_COLLECTED,RETESTING);
		   			
		   		}else {
		   			
		   			statusIds = Arrays.asList(SAMPLE_COLLECTED,RETESTING);
		   		}
		      }
		
		String query = 	sessionFactory.getCurrentSession().getNamedQuery("GET_BIOCHEM_REPORT_RELEASE").getQueryString();  
		StringBuffer sb = new StringBuffer(query);
		sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') >=:currentDate ");
		sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') <=:currentDate ");
		sb.append("  AND lbsampledtls.assign_unit  =:orgUnitId");
		/* if (bioChemParamDto.getIsOnHoldParameter() == true) {
	      	  statusIds = Arrays.asList(REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
	      	  sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
	            sb.append(" AND lbsampledtls.is_on_hold_parameter =" + bioChemParamDto.getIsOnHoldParameter());
	        }*/
	        if (statusIds != null && statusIds.size() > 0 ) {
	            sb.append(" and lbsampledtls.sample_status_id IN(:statusIds)");
	            sb.append(" AND lbsampledtls.is_on_hold_parameter = false ");
	        }
		sb.append(  " GROUP BY  " 
		        + "  labresmst.lab_test_res_id,  " 
		        + "  lbsampledtls.lab_sample_dtls_id,  "
		        + "  sample_mst.sample_no,  "
		        + "  visit_type_master.visit_type_id,  " 
		        + "  pati_mst.patient_id,  "
		        + "  test_mst.test_id,  " 
		        + "  pre_mst.prefix_id,  " 
		        + "  gen_mst.gender_id,  "
		        + "  doct_mst.doctor_id,  " 
		        + "  usmd.is_review_applicable, "
		        + "  pri_mst.priority_id,  " 
		        + "  wardmast.ward_id,  "
		        + "  bedmst.bed_id, "
		        + " subspe_mst.sub_speciality_name , " 
		        + "  samp_mst.sample_id , " 
		        + "  panel_master.panel_id,  " 
		        + "  speciality_master.speciality_id,  "
		        + "  order_details.order_details_id  ," 
                + " order_master.encounter_id ," + 
                  "  order_master.admission_id " 
                 // + " retest.retestcount "
		        + "ORDER BY  " 
		        + "  labresmst.report_type DESC,  "
		        + "  lbsampledtls.priority_id ASC,  "
		        + "  lbsampledtls.created_date ASC ");
		
		
		Query sql = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		sql.setParameter("orgId", bioChemParamDto.getOrgId());
		sql.setParameter("orgUnitId", bioChemParamDto.getOrgUnitId());
		sql.setParameter("deptId", bioChemParamDto.getDeptId());
		sql.setParameter("subDeptId", bioChemParamDto.getSubDeptId());
		sql.setParameter("orgUnitId", bioChemParamDto.getOrgUnitId());
		sql.setParameter("currentDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(bioChemParamDto.getOrgUnitId()),"yyyy-MM-dd"));
		if (statusIds != null && statusIds.size() > 0) {
			sql.setParameterList("statusIds", statusIds);
		}
		sql.setFirstResult(bioChemParamDto.getOffset() != null ? bioChemParamDto.getOffset() : 0);
		sql.setMaxResults(bioChemParamDto.getRecordPerPage() != null ? bioChemParamDto.getRecordPerPage() : 10);
		listLabResultMasterDto = sql.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
		return new Response(SUCCESS, SUCCESS_200, null, listLabResultMasterDto, null);
	} catch (Exception e) {
		logger.error("Exception", e);
		throw e;
	}
}

@Override
public Response getReportReleaseWorkListCount(BioChemParamDto bioChemParamDto) throws ApplicationException {
	try {
		List<Integer> statusIds = new ArrayList<Integer>();
		  
	        
		      if(bioChemParamDto.getIsOnHoldParameter() == false) {
		    	  com.param.global.common.Response res =  iLabConfigurationDao.getLabConfigurationList(bioChemParamDto.getOrgId(),bioChemParamDto.getAssignUnit());
			      Character c = null;
			       if(res.getListObject() != null && res.getListObject().size()>0) {
			    	   
			    	   List<LabConfigurationDto> list = res.getListObject() ;
			    	  c = list.get(0).getSkipValidation();
			       }
			       if(c.equals('Y')) {
			    		statusIds = Arrays.asList(RESULT_ENTRY_DONE,PROVISIONAL_REPORT_RELEASED);
			          }else {
			        		statusIds = BIOCHEM_REPORT_RELEASE_WORKLIST;
			          }  
		      }
		      
		String query = 	sessionFactory.getCurrentSession().getNamedQuery("GET_BIOCHEM_REPORT_RELEASE_COUNT").getQueryString();  
		StringBuffer sb = new StringBuffer(query);
        if (bioChemParamDto.getFromDate() != null ) 
        {
            sb.append( " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDate ");
        }

        if (bioChemParamDto.getToDate() != null ) {
            sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDate ");
        }

       /* if (bioChemParamDto.getIsOnHoldParameter() == true) {
      	  statusIds = Arrays.asList(REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
      	  sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
            sb.append(" AND lbsampledtls.is_on_hold_parameter =" + bioChemParamDto.getIsOnHoldParameter());
        }*/
        if (statusIds != null && statusIds.size() > 0 ) {
            sb.append(" and lbsampledtls.sample_status_id IN(:statusIds)");
            sb.append(" AND lbsampledtls.is_on_hold_parameter = false ");
        }
		sb.append("  AND lbsampledtls.assign_unit  =:assignUnit ");
		
		Query query1 = sessionFactory.getCurrentSession()
				.createSQLQuery(sb.toString())
				.setParameter("orgId", bioChemParamDto.getOrgId())
				.setParameter("deptId", bioChemParamDto.getDeptId())
				.setParameter("subDeptId", bioChemParamDto.getSubDeptId())
				.setParameter("assignUnit", bioChemParamDto.getAssignUnit())
				.setParameter("orgUnitId", bioChemParamDto.getOrgUnitId());
		if (bioChemParamDto.getFromDate() != null )
		{
			query1.setParameter("fromDate", bioChemParamDto.getFromDate());
		}

		if (bioChemParamDto.getToDate() != null ) {
			query1.setParameter("toDate", bioChemParamDto.getToDate());
		}
		if (statusIds != null && statusIds.size() > 0) {
			query1.setParameterList("statusIds", statusIds);
		}
		BigInteger sampleResultEntryCount = (BigInteger) query1.uniqueResult();
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
public Response getReportReleaseWorkListDetails(BioChemParamDto bioChemParamDto) throws ApplicationException 
{
	
	try {
		String GET_REPORT_DETTAILS = sessionFactory.getCurrentSession().getNamedQuery("GET_LAB_RESULT_DETAILS").getQueryString();
		List<LabResultDetailsViewDto> listLabResultDetailsMasterDto = null;
		if(bioChemParamDto.getTestType()==SINGLE_PARAM_TEST)
		{
			 listLabResultDetailsMasterDto = sessionFactory.getCurrentSession().createQuery(GET_REPORT_DETTAILS)
					.setParameter("orgId", bioChemParamDto.getOrgId())
					.setParameter("labResultId", bioChemParamDto.getLabResultId())
					.setResultTransformer(Transformers.aliasToBean(LabResultDetailsViewDto.class)).list();
			return new Response(SUCCESS, SUCCESS_200, null, listLabResultDetailsMasterDto, null);	
		}
		else if(bioChemParamDto.getTestType()==MULTY_PARAM_TEST)
		{
			StringBuilder GET_REPORT_DETTAILS_MULTYPARAM  = new StringBuilder(GET_REPORT_DETTAILS).append(" AND labResultDetailsMaster.headerId= :headerId ");
			listLabResultDetailsMasterDto = sessionFactory.getCurrentSession().createQuery(GET_REPORT_DETTAILS_MULTYPARAM.toString())
					.setParameter("orgId", bioChemParamDto.getOrgId())
					.setParameter("orgUnitId", bioChemParamDto.getOrgUnitId())
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
public Response saveReportReleaseWorkList(LabResultMasterDto labResultMasterDto) throws ApplicationException {
	try {
		
		LabResultMaster labResultMaster = mapper.map(labResultMasterDto, LabResultMaster.class,
				"LabResultMasterDtoToLabResultMaster");
		  labResultMaster.setStatus('A');
		LabResultMaster labResultMst = update(labResultMaster);
		LabSampleDetailsMaster labSampleDetailsMaster = (LabSampleDetailsMaster) sessionFactory.getCurrentSession()
				.get(LabSampleDetailsMaster.class, labResultMst.getLabSampleDtlsId());
		
		labSampleDetailsMaster.setCreatedBy(labResultMst.getCreatedBy() != null ? labResultMst.getCreatedBy() : 1);
		labSampleDetailsMaster.setCreatedDate(TimeZoneComponent.getDateByzone(labResultMst.getOrgUnitId()).getTime());
		labResultMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(labResultMst.getOrgUnitId()).getTime());
		labResultMaster.setUpdatedBy(labResultMst.getCreatedBy() != null ? labResultMst.getCreatedBy() : 1);
		labResultMaster.setCreatedDate(TimeZoneComponent.getDateByzone(labResultMst.getOrgUnitId()).getTime());
		labResultMaster.setCreatedBy(labResultMst.getCreatedBy() != null ? labResultMst.getCreatedBy() : 1);
		labResultMaster.setManualReleaseTime(TimeZoneComponent.getDateByzone(labResultMst.getOrgUnitId()).getTime());
		labResultMaster.setManualResultEnterTime(labResultMasterDto.getResultEnterDatetime().getTime());
		labResultMaster.setManualValidationTime(labResultMasterDto.getResultValidatedDatetime() != null ? labResultMasterDto.getResultValidatedDatetime().getTime() : labResultMasterDto.getResultEnterDatetime().getTime());
		labResultMaster.setSampleNo(labResultMasterDto.getSampleNo() != null ? labResultMasterDto.getSampleNo() : "0");
		labSampleDetailsMaster.setUpdatedBy(labResultMst.getUpdatedBy() != null ? labResultMst.getUpdatedBy() : 1);
		labSampleDetailsMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(labResultMst.getOrgUnitId()).getTime());
		labResultMaster.setSampleNo(labResultMasterDto.getSampleNo() != null ? labResultMasterDto.getSampleNo() : "0");
		labSampleDetailsMaster.setSampleStatusId(labResultMasterDto.getSampleStatusId() != null ? labResultMasterDto.getSampleStatusId() : 0);
//		labSampleDetailsMaster.setPerformedUnit(labResultMst.getOrgUnitId());
//		labSampleDetailsMaster.setAssignUnit(labResultMasterDto.getAssignUnit());
		labSampleDetailsMaster.setInternalNotes(labResultMasterDto.getInternalNotes() != null ? labResultMasterDto.getInternalNotes() : "");
		labSampleDetailsMaster.setSrfId(labResultMasterDto.getSrfId() != null ? labResultMasterDto.getSrfId() : "0");
		labSampleDetailsMaster.setIcmrId(labResultMasterDto.getIcmrId() != null ? labResultMasterDto.getIcmrId() : "0");
		
		if(labResultMasterDto.getSampleStatusId() == 8){
			labSampleDetailsMaster.setPerformTestId(labResultMasterDto.getTestId() != null ? labResultMasterDto.getTestId() : 0);
			labSampleDetailsMaster.setTestId(labResultMasterDto.getPerformTestId() != null ? labResultMasterDto.getPerformTestId() : 0);
			labSampleDetailsMaster.setAssignUnit(labResultMasterDto.getAssignUnit() != null ? labResultMasterDto.getAssignUnit() : 0);
			labSampleDetailsMaster.setPerformedUnit(labResultMst.getOrgUnitId() != null ? labResultMst.getOrgUnitId() : 0);
		}else{
			labSampleDetailsMaster.setPerformTestId(labResultMasterDto.getPerformTestId() != null ? labResultMasterDto.getPerformTestId() : 0);
			labSampleDetailsMaster.setTestId(labResultMasterDto.getTestId() != null ? labResultMasterDto.getTestId() : 0);
			labSampleDetailsMaster.setAssignUnit(labResultMst.getOrgUnitId() != null ? labResultMst.getOrgUnitId() : 0);
			labSampleDetailsMaster.setPerformedUnit(labResultMasterDto.getAssignUnit() != null ? labResultMasterDto.getAssignUnit() : 0);
			labSampleDetailsMaster.setProvisional(true);
		}
		
		labSampleDetailsMaster.setIsReportRecall(labResultMasterDto.getIsReportRecall());
		labSampleDetailsMaster.setIsOnHoldParameter(labResultMasterDto.getIsOnHoldParameter());
		sessionFactory.getCurrentSession().update(labSampleDetailsMaster);
			for (Iterator iterator = labResultMst.getListLabSampleResultDetailsMaster().iterator(); iterator
					.hasNext();) 
			{
				LabResultDetailsMaster labResultDetailsMaster = (LabResultDetailsMaster) iterator.next();
				labResultDetailsMaster.setLabResultId(labResultMst.getLabTestResId());
	            labSampleDetailsMaster.setInternalNotes(labResultMasterDto.getInternalNotes() != null ? labResultMasterDto.getInternalNotes() : "");
	            labSampleDetailsMaster.setSrfId(labResultMasterDto.getSrfId() != null ? labResultMasterDto.getSrfId() : "0");
	    		labSampleDetailsMaster.setIcmrId(labResultMasterDto.getIcmrId() != null ? labResultMasterDto.getIcmrId() : "0");
				labResultDetailsMaster.setOrgId(labResultMasterDto.getOrgId() != null ? labResultMasterDto.getOrgId() : 0);
				labResultDetailsMaster.setThirdLevelResult(labResultDetailsMaster.getFinalResult() != null ? labResultDetailsMaster.getFinalResult() : "N");
				sessionFactory.getCurrentSession().update(labResultDetailsMaster);
		   }
			return new Response(SUCCESS, SUCCESS_200, REPORT_RELEASE_SUCC, null, labSampleDetailsMaster);
	} catch (Exception e) {
		logger.error("Exception", e);
		return new Response(ERROR, ERR_500, REPORT_RELEASE_FAIL, null, null);
	}
}

@Override
public Response releaseNormanReports(LabResultMasterDto labResultMasterDto)
    throws ApplicationException {
  try {
    
      LabResultMaster labResultMaster = mapper.map(labResultMasterDto, LabResultMaster.class,
          "LabResultMasterDtoToLabResultMaster");
      labResultMaster.setStatus('A');
      LabResultMaster labResultMst = update(labResultMaster);
      LabSampleDetailsMaster labSampleDetailsMaster = (LabSampleDetailsMaster) sessionFactory.getCurrentSession()
          .get(LabSampleDetailsMaster.class, labResultMst.getLabSampleDtlsId());
  
          labResultMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(labResultMst.getOrgUnitId()).getTime());
          labResultMaster.setUpdatedBy(labResultMasterDto.getCreatedBy());
          labResultMaster.setCreatedDate(TimeZoneComponent.getDateByzone(labResultMst.getOrgUnitId()).getTime());
          labResultMaster.setCreatedBy(labResultMasterDto.getCreatedBy());
          labResultMaster.setSampleNo(labResultMasterDto.getSampleNo());
          labResultMaster.setSampleNo(labResultMasterDto.getSampleNo());
          
          labSampleDetailsMaster.setCreatedBy(labResultMst.getCreatedBy());
          labSampleDetailsMaster.setCreatedDate(TimeZoneComponent.getDateByzone(labResultMst.getOrgUnitId()).getTime());
          labSampleDetailsMaster.setUpdatedBy(labResultMst.getUpdatedBy());
          labSampleDetailsMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(labResultMst.getOrgUnitId()).getTime());
          labSampleDetailsMaster.setSampleStatusId(REPORT_RELEASED);
          labSampleDetailsMaster.setPerformedUnit(labResultMst.getOrgUnitId());
          List<LabResultDetailsViewDto>   listLabResultDetailsMaster = (List<LabResultDetailsViewDto>)sessionFactory.getCurrentSession().getNamedQuery("GET_LAB_RESULT_DETAILS")
              .setParameter("orgId", labResultMaster.getOrgId())
              .setParameter("labResultId", labResultMaster.getLabTestResId())
              .setResultTransformer(Transformers.aliasToBean(LabResultDetailsViewDto.class))
             .list();
          
          for (Iterator iterator1 = listLabResultDetailsMaster.iterator(); iterator1
                  .hasNext();) 
          {
        	  LabResultDetailsViewDto labResultDetailsViewDto = (LabResultDetailsViewDto) iterator1.next();
        	  LabResultDetailsMaster labResultDetailsMaster = mapper.map(labResultDetailsViewDto, LabResultDetailsMaster.class,"LabResultDetailsViewDtoTOLabResultDetailsMaster");
              labResultDetailsMaster.setLabResultId(labResultMst.getLabTestResId());
              labResultDetailsMaster.setThirdLevelResult(labResultDetailsMaster.getFinalResult());
              sessionFactory.getCurrentSession().update(labResultDetailsMaster);
          }
          sessionFactory.getCurrentSession().update(labSampleDetailsMaster);
        return new Response(SUCCESS, SUCCESS_200, REPORT_RELEASE_SUCC, null, null);
} 
  catch (Exception e) 
  {
    logger.error("Exception", e);
    return new Response(ERROR, ERR_500, REPORT_RELEASE_FAIL, null, null);
  }
}

@Override
public Response searchPatient(SearchCommonDto searchCommonDto) throws ApplicationException {
  List<CommonAutoCompleteDto>  patientList  = null; 
  try 
  {
      patientList = (List<CommonAutoCompleteDto>) sessionFactory.getCurrentSession()
              .getNamedQuery("SEARCH_LAB_PATIENT")
              .setParameter("orgId", searchCommonDto.getOrgId())
              .setParameter("orgUnitId", searchCommonDto.getOrgUnitId())
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
public Response getFilteredReportReleaseWorkList(SearchDto searchDto) throws ApplicationException {
  try
  {

	  List<Integer>  visitTypes = new ArrayList<Integer>();
	  List<Integer> statusIds = new ArrayList<Integer>(); 
	      if(searchDto.getIsOnHoldParameter() == false) {
	    	  com.param.global.common.Response res =  iLabConfigurationDao.getLabConfigurationList(searchDto.getOrgId(),searchDto.getAssignUnit());
	          Character c = null;
	           if(res.getListObject() != null && res.getListObject().size()>0) {
	        	   
	        	   List<LabConfigurationDto> list = res.getListObject() ;
	        	  c = list.get(0).getSkipValidation();
	           }
	           
	           if(c.equals('Y')) {
	        		statusIds = Arrays.asList(RESULT_ENTRY_DONE,PROVISIONAL_REPORT_RELEASED);
	              }else {
	            		statusIds = BIOCHEM_REPORT_RELEASE_WORKLIST;
	              } 
	      }
	      
      String query =sessionFactory.getCurrentSession().getNamedQuery("GET_BIOCHEM_REPORT_RELEASE").getQueryString().toString();
      
      
      StringBuilder sb =new StringBuilder(query);
      if (searchDto.getFromDt() != null ) {
    	  sb.append(
            " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt ");
      }

      if (searchDto.getToDt() != null && searchDto.getIsOnHoldParameter() == false) {
    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt ");
      }
      
      if(searchDto.getReportType()!=null &&searchDto.getReportType() >0)
      {
    	  sb.append(" AND labresmst.report_type =:reportType ");
      }
      if(searchDto.getPatientId()!=null &&searchDto.getPatientId() >0)
      {
    	  sb.append(" AND lbsampledtls.patient_id =:patientId ");
      }
      if(searchDto.getVisitTypes()!=null && searchDto.getVisitTypes().size() > 0)
      {
		  visitTypes = searchDto.getVisitTypes().stream()
				  .map(obj -> obj.getId())
				  .collect(Collectors.toList());
    	  sb.append(" AND order_master.visit_type_id IN (:visitTypes)");
      }
      if(searchDto.getUhidNumber()!="") {
         // sb.append(" AND lower(pati_mst.uhid_number) LIKE :uhidNumber ");
		  sb.append(" AND lower(pati_mst.uhid_number) LIKE concat('%', LOWER(:uhidNumber), '%') ");
      }
      
      if(searchDto.getAssignUnit()!=null && searchDto.getAssignUnit()>0)
      	sb.append("  and lbsampledtls.assign_unit =:assignUnit ");
      
     /* if (searchDto.getIsOnHoldParameter() == true) {
    	  statusIds = Arrays.asList(RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
    	  sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
          sb.append(" AND lbsampledtls.is_on_hold_parameter =" + searchDto.getIsOnHoldParameter());
      }*/
      if (statusIds != null && statusIds.size() > 0 ) {
          sb.append(" and lbsampledtls.sample_status_id IN(:statusIds)");
          sb.append(" AND lbsampledtls.is_on_hold_parameter = false ");
      }
      sb.append(   " GROUP BY  " 
		        + "  labresmst.lab_test_res_id,  "
		        + "   bill_mst.bill_number, " 
		        + "  lbsampledtls.lab_sample_dtls_id,  "
		        + "  sample_mst.sample_no,  "
		        + "  visit_type_master.visit_type_id,  " 
		        + "  pati_mst.patient_id,  "
		        + "  test_mst.test_id,  " 
		        + "  pre_mst.prefix_id,  " 
		        + "  gen_mst.gender_id,  "
		        + "  doct_mst.doctor_id,  " 
		        + "  usmd.is_review_applicable, "
		        + "  wardmast.ward_id,  "
		        + "  bedmst.bed_id, "
		        + " subspe_mst.sub_speciality_name , " 
		        + "  samp_mst.sample_id , " 
		        + "  panel_master.panel_id,  " 
		        + "  speciality_master.speciality_id,  "
		        + "  order_details.order_details_id , "
                + " order_master.encounter_id ," + 
                  "  order_master.admission_id ,"  +
                  " tpsd.payment_entitlement_id ," + 
                  "	tpsd2.payment_entitlement_id, "+
                  " tpsd.payee_id ," + 
                  "	tpsd2.payee_id,"
                  + " order_master.order_id ,"
                  + " sample_mst.sample_id ");
                //  + " retest.retestcount ");
      
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
				sb.append(" ORDER BY   lbsampledtls.patient_id ");
			}
      Query createdQuery=sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
	  if (searchDto.getFromDt() != null ) {
		  createdQuery.setParameter("fromDt", searchDto.getFromDt());
	  }

	  if (searchDto.getToDt() != null && searchDto.getIsOnHoldParameter() == false) {
		  createdQuery.setParameter("toDt", searchDto.getToDt());
	  }

	  if(searchDto.getReportType()!=null &&searchDto.getReportType() >0)
	  {
		  createdQuery.setParameter("reportType", searchDto.getReportType());
	  }
	  if(searchDto.getPatientId()!=null &&searchDto.getPatientId() >0)
	  {
		  createdQuery.setParameter("patientId", searchDto.getPatientId());
	  }
	  if(searchDto.getVisitTypes()!=null && searchDto.getVisitTypes().size() > 0)
	  {
		  createdQuery.setParameterList("visitTypes", visitTypes);
	  }
	  if(searchDto.getUhidNumber()!="") {
		  createdQuery.setParameter("uhidNumber", searchDto.getUhidNumber());
	  }

	  if(searchDto.getAssignUnit()!=null && searchDto.getAssignUnit()>0) {
		  createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());
	  }
	  if (statusIds != null && statusIds.size() > 0 ) {
		  createdQuery.setParameterList("statusIds", statusIds);
	  }
      List<LabResultMasterDto> listLabResultMasterDto = createdQuery
              .setParameter("orgId", searchDto.getOrgId())
              .setParameter("orgUnitId", searchDto.getOrgUnitId())
              .setParameter("deptId", searchDto.getDeptId())
              .setParameter("subDeptId", searchDto.getSubDeptId())
              .setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
              .setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
          .setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
      
      
      if(listLabResultMasterDto.isEmpty())
      {
        return new Response(SUCCESS, SUCCESS_200_MESSAGE,"No Records Found.",listLabResultMasterDto , null);
      }
      else {
    	  

    	  String sql="    select count(sample_dtls_id) as \"retestCount\"  , sample_dtls_id as \"labSampleDtlsId\"  from lab.t_sample_audit_trail sample_audit_trail  "
                  + "where test_status_id = 7 and sample_audit_trail.org_id = "+searchDto.getOrgId()+"  group by sample_dtls_id  ";
          List<LabResultMasterDto> list2  = sessionFactory.getCurrentSession()
                    .createSQLQuery(sql)
                  .setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
          
          
           for (LabResultMasterDto labResultMasterDto : listLabResultMasterDto) {
            
              LabResultMasterDto retestDto = list2.stream()
                     .filter(order -> labResultMasterDto.getLabSampleDtlsId().equals(order.getLabSampleDtlsId()))
                     .findAny()
                     .orElse(null);
              
              if(retestDto != null){
                  labResultMasterDto.setRetestCount(retestDto.getRetestCount()!= null ? retestDto.getRetestCount() : null);
              }
              
           } 
		  
    	  
        return new Response(SUCCESS, SUCCESS_200_MESSAGE,null, listLabResultMasterDto, null);
      }
     
      
  } catch (Exception e)
  {
      logger.error("Exection", e);
      throw e;
  }
}

@Override
public Response printCoverSheetReports(Integer labResultId) throws ApplicationException {
	 try 
	  {
	      List<LabResultMasterDto> list =  sessionFactory.getCurrentSession()
	              .getNamedQuery("PRINT_COVER_SHEET_REPORTS")
	              .setParameter("labResultId", labResultId)
	              .setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();                    
	      return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, list, null);

	  } catch (Exception e)
	  {
	      logger.error("Exection", e);
	      throw e;
	  }
}

@Override
public Response ConsolidatedReleaseWorkList(SearchDto searchDto) throws ApplicationException {
	 try
	  {
		 
		 
		   Response resp = iLabTransactionDao.getSpecialityAndSubSpecialityListByUserIdByUnitId(searchDto);
	        List<Integer> deptIds = new ArrayList<>();
	        List<Integer> subDeptIds = new ArrayList<>();
	        List<SearchDto> specialityList = new ArrayList<>();
	        List<SearchDto> subSpecialityList = new ArrayList<>();
	        List<Integer> visitTypes = new ArrayList<>();
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
			
			
			
	      String query =sessionFactory.getCurrentSession().getNamedQuery("CONSOLIDATED_RELEASE").getQueryString().toString();
	      com.param.global.common.Response res =  iLabConfigurationDao.getLabConfigurationList(searchDto.getOrgId(),searchDto.getAssignUnit());
	        Character c = null;
	        Character conflag = null;
	        
	       if(res.getListObject() != null && res.getListObject().size()>0) {
	    	   
	    	   List<LabConfigurationDto> list = res.getListObject();
	    	  c = list.get(0).getSkipValidation();
	    	   conflag = list.get(0).getConsolidatedResultEntry();
	    	   subDeptIds.add(list.get(0).getBiochemistryId()!=null ? list.get(0).getBiochemistryId() :0);
	    	   subDeptIds.add(list.get(0).getHematologyId()!=null ? list.get(0).getHematologyId() :0);
	    	   subDeptIds.add(list.get(0).getMicrobiologyId()!=null ? list.get(0).getMicrobiologyId() :0);
	    	   subDeptIds.add(list.get(0).getHistopathologyId()!=null ? list.get(0).getHistopathologyId() :0);
	    	   subDeptIds.add(list.get(0).getCystopathologyId()!=null ? list.get(0).getCystopathologyId() :0);
	    	   subDeptIds.add(list.get(0).getSerologyId()!=null ? list.get(0).getSerologyId() :0);
	    	   subDeptIds.add(list.get(0).getClinicalPathologyId() !=null ? list.get(0).getClinicalPathologyId() :0);
	       }
	       
	      List<Integer> statusIds = new ArrayList<Integer>();
		      if(searchDto.getIsOnHoldParameter() == false) {
			        
			       if(c.equals('Y')) {
			    		statusIds = Arrays.asList(RESULT_ENTRY_DONE,PROVISIONAL_REPORT_RELEASED);
			          }else {
			        		statusIds = BIOCHEM_REPORT_RELEASE_WORKLIST;
			          }
		      }
		      
	      StringBuilder sb =new StringBuilder(query);
	      if (searchDto.getFromDt() != null ) {
	    	  sb.append(
	            " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt ");
	      }

	      if (searchDto.getToDt() != null ) {
	    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt ");
	      }
	      
	      if(searchDto.getReportType()!=null &&searchDto.getReportType() >0)
	      {
	    	  sb.append(" AND labresmst.report_type =:reportType ");
	      }
	      if(searchDto.getPatientId()!=null &&searchDto.getPatientId() >0)
	      {
	    	  sb.append(" AND lbsampledtls.patient_id =:patientId ");
	      }
	      if(searchDto.getVisitTypes()!=null && searchDto.getVisitTypes().size() > 0)
	      {
			  visitTypes = searchDto.getVisitTypes().stream()
					  .map(obj -> obj.getId())
					  .collect(Collectors.toList());

			  sb.append(" AND order_master.visit_type_id IN (:visitTypes)");
	      }
	      if(searchDto.getUhidNumber()!="") {
	          sb.append(" AND lower(pati_mst.uhid_number) LIKE :uhidNumber ");
	      }
	    
	      if (statusIds != null && statusIds.size() > 0 ) {
	          sb.append(" and lbsampledtls.sample_status_id IN(:statusIds)");
	          sb.append(" AND lbsampledtls.is_on_hold_parameter = false ");
	      }
	    
	      if(conflag != null && conflag=='N'){
	    	  sb.append(" and lbsampledtls.sub_dept_id not IN(:subDeptIds)");
	      }
	      
	      if(searchDto.getAssignUnit()!=null && searchDto.getAssignUnit()>0) {
			  sb.append("  and lbsampledtls.assign_unit =:assignUnit ");
		  }
	      sb.append(   " GROUP BY  " 
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
			        + "  usmd.is_review_applicable, "
			        + "  wardmast.ward_id,  "
			        + "  bedmst.bed_id, "
			        + " subspe_mst.sub_speciality_name , " 
			        + "  samp_mst.sample_id , " 
			        + "  panel_master.panel_id,  " 
			        + "  speciality_master.speciality_id,  "
			        + "  order_details.order_details_id ," 
			        + " order_master.encounter_id ," + 
	                  "  order_master.admission_id, " +
	                  " tpsd.payment_entitlement_id, "+
	                  " tpsd2.payment_entitlement_id, " +
	                  " tpsd.payee_id, "+
	                  " tpsd2.payee_id, "
	                  + "order_master.order_id,"
	                  + "sample_mst.sample_id ");
	               //   + " retest.retestcount "
	      
	      if (searchDto.getLabSortingId() != null) {
				if (searchDto.getLabSortingId() == 1) {
					sb.append(" ORDER BY    lbsampledtls.patient_id, lbsampledtls.updated_date  DESC ");
				} else if (searchDto.getLabSortingId() == 2) {
					sb.append(" ORDER BY    order_master.order_id DESC ");
				} else if (searchDto.getLabSortingId() == 3) {
					sb.append(" ORDER BY   sample_mst.sample_id  DESC ");
				} else if (searchDto.getLabSortingId() == 4) {
					sb.append(" ORDER BY    lbsampledtls.updated_date DESC ");
				}
			} else {
				sb.append(" ORDER BY    lbsampledtls.patient_id,    lbsampledtls.updated_date DESC ");
			}
	      
	      Query createdQuery=sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		  if (searchDto.getFromDt() != null ) {
			  createdQuery.setParameter("fromDt", searchDto.getFromDt());
		  }

		  if (searchDto.getToDt() != null ) {
			  createdQuery.setParameter("toDt", searchDto.getToDt());
		  }

		  if(searchDto.getReportType()!=null &&searchDto.getReportType() >0)
		  {
			  createdQuery.setParameter("reportType", searchDto.getReportType());
		  }
		  if(searchDto.getPatientId()!=null &&searchDto.getPatientId() >0)
		  {
			  createdQuery.setParameter("patientId", searchDto.getPatientId());
		  }
		  if(searchDto.getVisitTypes()!=null && searchDto.getVisitTypes().size()>0)
		  {
			  createdQuery.setParameterList("visitTypes", visitTypes);
		  }
		  if(searchDto.getUhidNumber()!="") {
			  createdQuery.setParameter("uhidNumber", searchDto.getUhidNumber());
		  }

		  if (statusIds != null && statusIds.size() > 0) {
			  createdQuery.setParameterList("statusIds", statusIds);
		  }

		  if(conflag != null && conflag=='N'){
			  createdQuery.setParameterList("subDeptIds", subDeptIds);
		  }

		  if(searchDto.getAssignUnit()!=null && searchDto.getAssignUnit()>0){
			  createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());
		  }
	      List<LabResultMasterDto> listLabResultMasterDto = createdQuery
	              .setParameter("orgId", searchDto.getOrgId())
	              .setParameter("orgUnitId", searchDto.getOrgUnitId())
	              .setParameterList("deptId", deptIds)
	              .setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
	              .setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
	          .setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
	      
	      Integer count = 0;
	      List<LabResultMasterDto> listLabResultMasterDto1 = createdQuery
	              .setParameter("orgId", searchDto.getOrgId())
	              .setParameter("orgUnitId", searchDto.getOrgUnitId())
	              .setParameterList("deptId", deptIds)
	          .setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
	      
    	  
	      if(listLabResultMasterDto1.isEmpty())
	      {
	        return new Response(SUCCESS, SUCCESS_200_MESSAGE,"No Records Found.",listLabResultMasterDto1 , count);
	      } 
	      else {
	    	  
	    	  if(listLabResultMasterDto1 != null && listLabResultMasterDto1.size()>=0) {
	    		  count =  listLabResultMasterDto1.size();
	    	  }
	    	  
	    	  String sql="    select count(sample_dtls_id) as \"retestCount\"  , sample_dtls_id as \"labSampleDtlsId\"  from lab.t_sample_audit_trail sample_audit_trail  "
                      + "where test_status_id = 7 and sample_audit_trail.org_id =:orgId "
					  +"  group by sample_dtls_id  ";
              List<LabResultMasterDto> list2  = sessionFactory.getCurrentSession()
                        .createSQLQuery(sql)
					  .setParameter("orgId", searchDto.getOrgId())
                      .setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
              
              
               for (LabResultMasterDto labResultMasterDto : listLabResultMasterDto) {
                
                  LabResultMasterDto retestDto = list2.stream()
                         .filter(order -> labResultMasterDto.getLabSampleDtlsId().equals(order.getLabSampleDtlsId()))
                         .findAny()
                         .orElse(null);
                  
                  if(retestDto != null){
                      labResultMasterDto.setRetestCount(retestDto.getRetestCount()!= null ? retestDto.getRetestCount() : null);
                  }
                  
               } 
	    	  
	    	  
	        return new Response(SUCCESS, SUCCESS_200_MESSAGE,null, listLabResultMasterDto, count);
	      }
	     
	      
	  } catch (Exception e)
	  {
	      logger.error("Exection", e);
	      throw e;
	  }
}

@Override
public Response consolidatedSerologyReleaseSearch(SearchDto searchDto) throws ApplicationException {
	try {

	      String query = sessionFactory.getCurrentSession()
	          .getNamedQuery("GET_SEROLOGY_RESULT_ENTRY_LIST").getQueryString().toString();
	      StringBuilder sb = new StringBuilder(query);
	      List<Integer> statusIds = new ArrayList<Integer>(); 
	      List<Integer>  subDeptIds = new ArrayList<Integer>();
	      List<Integer>  testTypes = new ArrayList<Integer>();
		  List<Integer>  visitTypes = new ArrayList<Integer>();


	      if(searchDto.getIsOnHoldParameter() == false) {
	      com.param.global.common.Response res =  iLabConfigurationDao.getLabConfigurationList(searchDto.getOrgId(),searchDto.getAssignUnit());
	        Character c = null;
	        Integer serologyId= null;
	       if(res.getListObject() != null && res.getListObject().size()>0) {
	    	   
	    	   List<LabConfigurationDto> list = res.getListObject() ;
	    	  c = list.get(0).getSkipValidation();
	    	  
	    	  if(list.get(0).getSerologyId() == null ){
	    		  return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.",null, 0);
	    	  }
	    	  
	       }
	     
	       if(c != null && c.equals('Y')) {
	    		statusIds = Arrays.asList(RESULT_ENTRY_DONE,PROVISIONAL_REPORT_RELEASED);
	          }else {
	        		statusIds = BIOCHEM_REPORT_RELEASE_WORKLIST;
	          }
	      }
	      if (searchDto.getFromDt() != null ) {
	    	  
	    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt ");
	      }

	      if (searchDto.getToDt() != null ) {
	    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt ");
	      }
	      
	      if (statusIds.size()>0) {
	    	  sb.append(" and  lbsampledtls.sample_status_id IN (:statusIds) ");
	      }

	      if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
	    	  sb
	            .append(" AND pati_mst.patient_id =:patientId ");
//		 List<Integer>  visitTypeIds = new ArrayList<Integer>();
	      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
			  visitTypes = searchDto.getVisitTypes().stream()
					  .map(obj -> obj.getId())
					  .collect(Collectors.toList());

			  sb.append(" AND visit_type_master.visit_type_id IN (:visitTypes)");
		  }

	      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0) {
			  subDeptIds = searchDto.getSubDepts().stream()
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
	            .append(" AND lbsampledtls.lab_sample_dtls_id =:labSampleDtlsId");

	      if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
	          sb.append("  and lbsampledtls.assign_unit  =:assignUnit ");
	      
	      if(searchDto.getUhidNumber()!="") {
	    	  sb.append(" AND lower(pati_mst.uhid_number) LIKE :uhidNumber ");
	      }
	    /*  if (searchDto.getIsOnHoldParameter() == true) {
	    	  statusIds = Arrays.asList(RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
	    	  sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
	          sb.append(" AND lbsampledtls.is_on_hold_parameter =" + searchDto.getIsOnHoldParameter());
	      }*/
	      if (statusIds != null && statusIds.size() > 0) {
	          sb.append(" and lbsampledtls.sample_status_id IN(:statusIds)");
	          sb.append(" AND lbsampledtls.is_on_hold_parameter = false");
	      }
	      
	      if (searchDto.getLabSortingId() != null) {
				if (searchDto.getLabSortingId() == 1) {
					sb.append(" ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
				} else if (searchDto.getLabSortingId() == 2) {
					sb.append(" ORDER BY    order_master.order_id DESC ");
				} else if (searchDto.getLabSortingId() == 3) {
					sb.append(" ORDER BY    sample_mst.sample_id  DESC ");
				} else if (searchDto.getLabSortingId() == 4) {
					sb.append(" ORDER BY   lbsampledtls.updated_date DESC ");
				}
			} else {
				sb.append(" ORDER BY  lbsampledtls.patient_id,  lbsampledtls.updated_date DESC ");
			}
	      Query createdQuery = sessionFactory.getCurrentSession()
	          .createSQLQuery(sb.toString());
		if (searchDto.getFromDt() != null ) {
			createdQuery.setParameter("fromDt", searchDto.getFromDt());
		}

		if (searchDto.getToDt() != null ) {
			createdQuery.setParameter("toDt", searchDto.getToDt());
		}

		if (statusIds.size() > 0) {
			createdQuery.setParameterList("statusIds", statusIds);
		}

		if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
			createdQuery.setParameter("patientId", searchDto.getPatientId());
		}

		if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0 ){
			createdQuery.setParameterList("visitTypes", visitTypes);
		}

		if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0 ){
			createdQuery.setParameterList("subDepts", subDeptIds);
		}

		if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size() > 0 ){
			createdQuery.setParameterList("testTypes", testTypes);
		}

		if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0){
			createdQuery.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId());
		}

		if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0){
			createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());
		}

		if(searchDto.getUhidNumber()!="") {
			createdQuery.setParameter("uhidNumber", searchDto.getUhidNumber());
		}
	    /*  if (searchDto.getIsOnHoldParameter() == true) {
	    	  statusIds = Arrays.asList(RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
	    	  sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
	          sb.append(" AND lbsampledtls.is_on_hold_parameter =" + searchDto.getIsOnHoldParameter());
	      }*/
		if (statusIds != null && statusIds.size() > 0 ) {
			createdQuery.setParameterList("statusIds", statusIds);
		}

	      List<LabResultMasterDto> list  = createdQuery
	    			.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
					.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
					.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
					.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
	      
	      Integer count = 0;
	      List<LabResultMasterDto> list1  = createdQuery
	    			.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
					.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
	      
	      if (list.isEmpty()) {
	    	  
	        return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.",list, count);
	        
	      } else {
	    	  
	    	  if(list1 != null && list1.size()>=0) {
	    		  count =  list1.size();
	    	  }
	    	  
	        return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, list,count);
	      }


	    } catch (Exception e) {
	      logger.error("Exection", e);
	      throw e;
	    }
}

@Override
public Response ConsolidatedReleaseWorkTypeSearchList(SearchDto searchDto) throws ApplicationException {

	 try
	  {
		 Response resp = iLabTransactionDao.getSpecialityAndSubSpecialityListByUserIdByUnitId(searchDto);
	        List<Integer> deptIds = new ArrayList<>();
	        List<Integer> subDeptIds = new ArrayList<>();
	        List<SearchDto> specialityList = new ArrayList<>();
	        List<SearchDto> subSpecialityList = new ArrayList<>();
	        
	        if(specialityList != null && specialityList.size() > 0) {
				for (Iterator iterator = specialityList.iterator(); iterator.hasNext();) {
					SearchDto dto = (SearchDto) iterator.next();
					deptIds.add(dto.getSpecialityId());
				}
	        }else {
	        	deptIds.add(searchDto.getDeptId());
	        }
	      String query =sessionFactory.getCurrentSession().getNamedQuery("CONSOLIDATED_RELEASE").getQueryString().toString();
	      com.param.global.common.Response res =  iLabConfigurationDao.getLabConfigurationList(searchDto.getOrgId(),searchDto.getAssignUnit());

	      Character c = null;
	        Character conflag = null;
	        
	        if(res.getListObject() != null && res.getListObject().size()>0) {
		    	   
		    	   List<LabConfigurationDto> list = res.getListObject();
		    	  c = list.get(0).getSkipValidation();
		    	   conflag = list.get(0).getConsolidatedResultEntry();
		    	   subDeptIds.add(list.get(0).getBiochemistryId()!=null ? list.get(0).getBiochemistryId() :0);
		    	   subDeptIds.add(list.get(0).getHematologyId()!=null ? list.get(0).getHematologyId() :0);
		    	   subDeptIds.add(list.get(0).getMicrobiologyId()!=null ? list.get(0).getMicrobiologyId() :0);
		    	   subDeptIds.add(list.get(0).getHistopathologyId()!=null ? list.get(0).getHistopathologyId() :0);
		    	   subDeptIds.add(list.get(0).getCystopathologyId()!=null ? list.get(0).getCystopathologyId() :0);
		    	   subDeptIds.add(list.get(0).getSerologyId()!=null ? list.get(0).getSerologyId() :0);
		    	   subDeptIds.add(list.get(0).getClinicalPathologyId() !=null ? list.get(0).getClinicalPathologyId() :0);
		       }
	        
	       List<Integer> statusIds = new ArrayList<Integer>(); 
		      if(searchDto.getIsOnHoldParameter() == false) {
			     
			       if(res.getListObject() != null && res.getListObject().size()>0) {
			    	   
			    	   List<LabConfigurationDto> list = res.getListObject() ;
			    	  c = list.get(0).getSkipValidation();
			       }
			       if(c.equals('Y')) {
			    		statusIds = Arrays.asList(RESULT_ENTRY_DONE,PROVISIONAL_REPORT_RELEASED);
			          }else {
			        		statusIds = BIOCHEM_REPORT_RELEASE_WORKLIST;
			          }
		      }
	      
	      StringBuilder sb =new StringBuilder(query);
	      if (searchDto.getFromDt() != null ) {
	    	  sb.append(
	            " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt ");
	      }

	      if (searchDto.getToDt() != null ) {
	    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt ");
	      }
	     /* if (searchDto.getIsOnHoldParameter() == true) {
	    	  statusIds = Arrays.asList(RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
	    	  sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
	          sb.append(" AND lbsampledtls.is_on_hold_parameter =" + searchDto.getIsOnHoldParameter());
	      }*/
	      if (statusIds != null && statusIds.size() > 0) {
	          sb.append(" and lbsampledtls.sample_status_id IN(:statusIds)");
	          sb.append(" AND lbsampledtls.is_on_hold_parameter = false ");
	      }
	      if(conflag != null && conflag=='N'){
	    	  sb.append(" and lbsampledtls.sub_dept_id not IN(:subDeptIds)");
	      }
	      
	      if(searchDto.getSearchField()!="") {
	    	  sb.append(" AND (lower(pati_mst.uhid_number) LIKE concat('%',LOWER(:searchField),'%') OR "
	    	  + " lower(sample_mst.sample_id) LIKE concat('%',LOWER(:searchField),'%') OR "
	    	  + " lower(pati_mst.first_name) LIKE concat('%',LOWER(:searchField),'%') OR "
	    	  + " lower(pati_mst.middle_name) LIKE concat('%',LOWER(:searchField),'%') OR "
	    	  + " lower(pati_mst.last_name) LIKE concat('%',LOWER(:searchField),'%') OR "
	    	  + " lower(test_mst.test_desc) LIKE concat('%',LOWER(:searchField),'%') OR"
	    	  + " lower(test_mst.test_code) LIKE concat('%',LOWER(:searchField),'%') OR "
	    	  + " lower(subspe_mst.sub_speciality_name) LIKE concat('%',LOWER(:searchField),'%') )");
	    	  }
	      
	      if(searchDto.getAssignUnit()!=null && searchDto.getAssignUnit()>0)
	      	sb.append("  and lbsampledtls.assign_unit ="+searchDto.getAssignUnit());
	      sb.append(   " GROUP BY  " 
			        + "  labresmst.lab_test_res_id,  " 
			        + "  lbsampledtls.lab_sample_dtls_id,  "
			        + "  sample_mst.sample_no,  "
			        + "  visit_type_master.visit_type_id,  " 
			        + "  pati_mst.patient_id,  "
			        + "  test_mst.test_id,  " 
			        + "  pre_mst.prefix_id,  " 
			        + "  gen_mst.gender_id,  "
			        + "  doct_mst.doctor_id,  " 
			        + "  usmd.is_review_applicable, "
			        + "  wardmast.ward_id,  "
			        + "  bedmst.bed_id, "
			        + " subspe_mst.sub_speciality_name , " 
			        + "  samp_mst.sample_id , " 
			        + "  panel_master.panel_id,  " 
			        + "  speciality_master.speciality_id,  "
			        + "  order_details.order_details_id ," 
			        + " order_master.encounter_id ," + 
	                  "  order_master.admission_id, " +
	                  " tpsd.payment_entitlement_id, "+
	                  " tpsd2.payment_entitlement_id ,"+
	                  " tpsd.payee_id, "+
	                  " tpsd2.payee_id, "+
	                  " order_master.order_id,"+
	                  " sample_mst.sample_id, "
	               //   + " retest.retestcount ,"
	                  + " bill.bill_number ");
	      
	      if (searchDto.getLabSortingId() != null) {
				if (searchDto.getLabSortingId() == 1) {
					sb.append(" ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
				} else if (searchDto.getLabSortingId() == 2) {
					sb.append(" ORDER BY   order_master.order_id DESC ");
				} else if (searchDto.getLabSortingId() == 3) {
					sb.append(" ORDER BY    sample_mst.sample_id  DESC ");
				} else if (searchDto.getLabSortingId() == 4) {
					sb.append(" ORDER BY   lbsampledtls.updated_date DESC ");
				}
			} else {
				sb.append(" ORDER BY labresmst.report_type DESC, lbsampledtls.priority_id ASC, lbsampledtls.created_date ASC ");
			}
	      Query createdQuery=sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		  if (searchDto.getFromDt() != null ) {
			  createdQuery.setParameter("fromDt", searchDto.getFromDt());
		  }

		  if (searchDto.getToDt() != null ) {
			  createdQuery.setParameter("toDt", searchDto.getToDt());
		  }
		  if (statusIds != null && statusIds.size() > 0 ) {
			  createdQuery.setParameterList("statusIds", statusIds);
		  }
		  if(conflag != null && conflag=='N'){
			  createdQuery.setParameterList("subDeptIds", subDeptIds);
		  }

		  if(searchDto.getSearchField()!="") {
			  createdQuery.setParameter("searchField", searchDto.getSearchField());;
		  }

		  List<LabResultMasterDto> listLabResultMasterDto = createdQuery
	              .setParameter("orgId", searchDto.getOrgId())
	              .setParameter("orgUnitId", searchDto.getOrgUnitId())
	              .setParameter("deptId", searchDto.getDeptId())
	              .setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
	              .setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
	          .setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
	      
	      Integer count = 0;
	      List<LabResultMasterDto> listLabResultMasterDto1 = createdQuery
	              .setParameter("orgId", searchDto.getOrgId())
	              .setParameter("orgUnitId", searchDto.getOrgUnitId())
	              .setParameter("deptId", searchDto.getDeptId())
	          .setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
	      
	      if(listLabResultMasterDto.isEmpty())
	      {
	        return new Response(SUCCESS, SUCCESS_200_MESSAGE,"No Records Found.",listLabResultMasterDto , count);
	      }
	      else {
	    	  
	    	  if(listLabResultMasterDto1 != null && listLabResultMasterDto1.size()>=0) {
	    		  count =  listLabResultMasterDto1.size();
	    	  }
	    	  
	    	  String sql="    select count(sample_dtls_id) as \"retestCount\"  , sample_dtls_id as \"labSampleDtlsId\"  from lab.t_sample_audit_trail sample_audit_trail  "
                      + "where test_status_id = 7 and sample_audit_trail.org_id =:orgId "
					  +"  group by sample_dtls_id  ";
              List<LabResultMasterDto> list2  = sessionFactory.getCurrentSession()
                        .createSQLQuery(sql)
			  			.setParameter("orgId", searchDto.getOrgId())
                      .setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
              
              
               for (LabResultMasterDto labResultMasterDto : listLabResultMasterDto) {
                
                  LabResultMasterDto retestDto = list2.stream()
                         .filter(order -> labResultMasterDto.getLabSampleDtlsId().equals(order.getLabSampleDtlsId()))
                         .findAny()
                         .orElse(null);
                  
                  if(retestDto != null){
                      labResultMasterDto.setRetestCount(retestDto.getRetestCount()!= null ? retestDto.getRetestCount() : null);
                  }
                  
               } 
	    	  
	        return new Response(SUCCESS, SUCCESS_200_MESSAGE,null, listLabResultMasterDto, count);
	      }
	     
	      
	  } catch (Exception e)
	  {
	      logger.error("Exection", e);
	      throw e;
	  }

}

@Override
public Response consolidatedSerologyReleaseTypeSearch(SearchDto searchDto) throws ApplicationException {

	try {

	      String query = sessionFactory.getCurrentSession()
	          .getNamedQuery("GET_SEROLOGY_RESULT_ENTRY_LIST").getQueryString().toString();
	      query = query.replace("lbsampledtls.sample_status_id = :sampleStatusId ", "lbsampledtls.sample_status_id IN(:sampleStatusIds) ");
	      StringBuilder sb = new StringBuilder(query);
	      List<Integer> statusIds = new ArrayList<Integer>();
	      if(searchDto.getIsOnHoldParameter() == false) {
	      com.param.global.common.Response res =  iLabConfigurationDao.getLabConfigurationList(searchDto.getOrgId(),searchDto.getAssignUnit());
	      Character c = null;
	       if(res.getListObject() != null && res.getListObject().size()>0) {
	    	   
	    	   List<LabConfigurationDto> list = res.getListObject() ;
	    	  c = list.get(0).getSkipValidation();
	       }
	       if(c.equals('Y')) {
	    		statusIds = Arrays.asList(RESULT_ENTRY_DONE);
	          }else {
	        		statusIds = BIOCHEM_REPORT_RELEASE_WORKLIST;
	          }
	      
	      }
	      
	      if (searchDto.getFromDt() != null ) {
	    	  
	    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt ");
	      }

	      if (searchDto.getToDt() != null ) {
	    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt ");
	      }

		if(searchDto.getSearchField() != null && !searchDto.getSearchField().isEmpty()) {
			sb.append(" AND (lower(pati_mst.uhid_number) LIKE concat('%',LOWER(:searchField),'%') OR "
					+ " lower(sample_mst.sample_id) LIKE concat('%',LOWER(:searchField),'%') OR "
					+ " lower(pati_mst.first_name) LIKE concat('%',LOWER(:searchField),'%') OR "
					+ " lower(pati_mst.middle_name) LIKE concat('%',LOWER(:searchField),'%') OR "
					+ " lower(pati_mst.last_name) LIKE concat('%',LOWER(:searchField),'%') OR "
					+ " lower(test_mst.test_desc) LIKE concat('%',LOWER(:searchField),'%') OR "
					+ " lower(test_mst.test_code) LIKE concat('%',LOWER(:searchField),'%') OR " // Fixed the missing parenthesis
					+ " lower(subspe_mst.sub_speciality_name) LIKE concat('%',LOWER(:searchField),'%') )");
		}


		if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0) {
			  sb.append("  and lbsampledtls.assign_unit  =:assignUnit ");
		  }
	   /*   if (searchDto.getIsOnHoldParameter() == true) {
	    	  statusIds = Arrays.asList(RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
	    	  sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
	          sb.append(" AND lbsampledtls.is_on_hold_parameter =" + searchDto.getIsOnHoldParameter());
	      }*/
	      if (statusIds != null && statusIds.size() > 0) {
	          sb.append(" and lbsampledtls.sample_status_id IN(:statusIds)");
	          sb.append(" AND lbsampledtls.is_on_hold_parameter = false ");
	      }
	      if (searchDto.getLabSortingId() != null) {
				if (searchDto.getLabSortingId() == 1) {
					sb.append(" ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
				} else if (searchDto.getLabSortingId() == 2) {
					sb.append(" ORDER BY    order_master.order_id DESC ");
				} else if (searchDto.getLabSortingId() == 3) {
					sb.append(" ORDER BY    sample_mst.sample_id  DESC ");
				} else if (searchDto.getLabSortingId() == 4) {
					sb.append(" ORDER BY   lbsampledtls.updated_date DESC ");
				}
			} else {
				sb.append(" ORDER BY  lbsampledtls.priority_id, lbsampledtls.updated_date  DESC ");
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
		if (statusIds != null && statusIds.size() > 0) {
			createdQuery.setParameterList("statusIds", statusIds);
		}

	      List<LabResultMasterDto> list  = createdQuery
	    			.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
					.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
					.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
					.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
	      
	      Integer count = 0;
	      List<LabResultMasterDto> list1  = createdQuery
	    			.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
					.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
	      
	      if (list.isEmpty()) {
	    	  
	        return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.",list, count);
	        
	      } else {
	    	  
	    	  if(list1 != null && list1.size()>=0) {
	    		  count =  list1.size();
	    	  }
	    	  
	        return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, list,count);
	      }


	    } catch (Exception e) {
	      logger.error("Exection", e);
	      throw e;
	    }

}

@Override
public Response getFilteredReportReleaseTypeSearchWorkList(SearchDto searchDto) throws ApplicationException {

	  try
	  {
	      String query =sessionFactory.getCurrentSession().getNamedQuery("GET_BIOCHEM_REPORT_RELEASE").getQueryString().toString();
	     
	      List<Integer> statusIds = new ArrayList<Integer>(); 
	      if(searchDto.getIsOnHoldParameter() == false) {
	      com.param.global.common.Response res =  iLabConfigurationDao.getLabConfigurationList(searchDto.getOrgId(),searchDto.getAssignUnit());
	      Character c = null;
	       if(res.getListObject() != null && res.getListObject().size()>0) {
	    	   
	    	   List<LabConfigurationDto> list = res.getListObject() ;
	    	  c = list.get(0).getSkipValidation();
	       }
	      
	       if(c.equals('Y')) {
	    		statusIds = Arrays.asList(RESULT_ENTRY_DONE,PROVISIONAL_REPORT_RELEASED);
	          }else {
	        		statusIds = BIOCHEM_REPORT_RELEASE_WORKLIST;
	          }
	      }
	      
	      StringBuilder sb =new StringBuilder(query);
	      if (searchDto.getFromDt() != null ) {
	    	  sb.append(
	            " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt ");
	      }

	      if (searchDto.getToDt() != null ) {
	    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt ");
	      }
	     /* if (searchDto.getIsOnHoldParameter() == true) {
	      	  statusIds = Arrays.asList(REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
	      	  sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
	            sb.append(" AND lbsampledtls.is_on_hold_parameter =" + searchDto.getIsOnHoldParameter());
	        }*/
	        if (statusIds != null && statusIds.size() > 0 ) {
	            sb.append(" and lbsampledtls.sample_status_id IN(:statusIds)");
	            sb.append(" AND lbsampledtls.is_on_hold_parameter = false ");
	        }
	      if(searchDto.getSearchField()!="") {
	    	  sb.append(" AND (lower(pati_mst.uhid_number) LIKE concat('%',LOWER(:searchField),'%') OR "
	    	  + " lower(sample_mst.sample_id) LIKE concat('%',LOWER(:searchField),'%') OR "
	    	  + " lower(pati_mst.first_name) LIKE concat('%',LOWER(:searchField),'%') OR "
	    	  + " lower(pati_mst.middle_name) LIKE concat('%',LOWER(:searchField),'%') OR "
	    	  + " lower(pati_mst.last_name) LIKE concat('%',LOWER(:searchField),'%') OR "
	    	  + " lower(test_mst.test_desc) LIKE concat('%',LOWER(:searchField),'%') OR"
	    	  + " lower(test_mst.test_code) LIKE concat('%',LOWER(:searchField),'%') OR "
	    	  + " lower(subspe_mst.sub_speciality_name) LIKE concat('%',LOWER(:searchField),'%') )");
	    	  }
	      
	      if(searchDto.getAssignUnit()!=null && searchDto.getAssignUnit()>0) {
			  sb.append("  and lbsampledtls.assign_unit =:assignUnit ");
		  }
	      sb.append(   " GROUP BY  " 
			        + "  labresmst.lab_test_res_id,  " 
			        + "  lbsampledtls.lab_sample_dtls_id,  "
			        + "  sample_mst.sample_no,  "
			        + "  visit_type_master.visit_type_id,  " 
			        + "  pati_mst.patient_id,  "
			        + "  test_mst.test_id,  " 
			        + "  pre_mst.prefix_id,  " 
			        + "  gen_mst.gender_id,  "
			        + "  doct_mst.doctor_id,  " 
			        + "  usmd.is_review_applicable, "
			        + "  wardmast.ward_id,  "
			        + "  bedmst.bed_id, "
			        + " subspe_mst.sub_speciality_name , " 
			        + "  samp_mst.sample_id , " 
			        + "  panel_master.panel_id,  " 
			        + "  speciality_master.speciality_id,  "
			        + "  order_details.order_details_id , "
	                + " order_master.encounter_id ," + 
	                  "  order_master.admission_id ,"  +
	                  " tpsd.payment_entitlement_id ," + 
	                  "	tpsd2.payment_entitlement_id,"
	                  + " order_master.order_id ,"
	                  + " sample_mst.sample_id,"
	                  + " tpsd.payee_id,"
	                  + " tpsd2.payee_id,"
	                  + "  order_master.order_id,"
	                  + " sample_mst.sample_id, "
	                 // + " retest.retestcount ,  "
	                  + " bill_mst.bill_number ");
	      
			if (searchDto.getLabSortingId() != null) {
				if (searchDto.getLabSortingId() == 1) {
					sb.append(" ORDER BY   lbsampledtls.patient_id, lbsampledtls.updated_date  DESC ");
				} else if (searchDto.getLabSortingId() == 2) {
					sb.append(" ORDER BY   order_master.order_id DESC ");
				} else if (searchDto.getLabSortingId() == 3) {
					sb.append(" ORDER BY   sample_mst.sample_id DESC ");
				} else if (searchDto.getLabSortingId() == 4) {
					sb.append(" ORDER BY   lbsampledtls.updated_date  DESC ");
				}
			} else {
				sb.append(" ORDER BY  labresmst.report_type DESC,lbsampledtls.priority_id ASC,lbsampledtls.created_date DESC");
			}
	      Query createdQuery=sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		  if (searchDto.getFromDt() != null ) {
			  createdQuery.setParameter("fromDt", searchDto.getFromDt());
		  }

		  if (searchDto.getToDt() != null ) {
			  createdQuery.setParameter("toDt", searchDto.getToDt());
		  }

		  if (statusIds != null && statusIds.size() > 0 ) {
			  createdQuery.setParameterList("statusIds", statusIds);
		  }
		  if(searchDto.getSearchField()!="") {
			  createdQuery.setParameter("searchField", searchDto.getSearchField());
		  }

		  if(searchDto.getAssignUnit()!=null && searchDto.getAssignUnit()>0){
			  createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());
		  }
	      List<LabResultMasterDto> listLabResultMasterDto = createdQuery
	              .setParameter("orgId", searchDto.getOrgId())
	              .setParameter("orgUnitId", searchDto.getOrgUnitId())
	              .setParameter("deptId", searchDto.getDeptId())
	              .setParameter("subDeptId", searchDto.getSubDeptId())
	              .setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
	              .setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
	          .setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
	      
	      
	      if(listLabResultMasterDto.isEmpty())
	      {
	        return new Response(SUCCESS, SUCCESS_200_MESSAGE,"No Records Found.",listLabResultMasterDto , null);
	      }
	      else {
	    	  
	    	  String sql="    select count(sample_dtls_id) as \"retestCount\"  , sample_dtls_id as \"labSampleDtlsId\"  from lab.t_sample_audit_trail sample_audit_trail  "
	                  + "where test_status_id = 7 and sample_audit_trail.org_id =:orgId "
					  +"  group by sample_dtls_id  ";
	          List<LabResultMasterDto> list2  = sessionFactory.getCurrentSession()
	                    .createSQLQuery(sql)
					  .setParameter("orgId", searchDto.getOrgId())
	                  .setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
	          
	          
	           for (LabResultMasterDto labResultMasterDto : listLabResultMasterDto) {
	            
	              LabResultMasterDto retestDto = list2.stream()
	                     .filter(order -> labResultMasterDto.getLabSampleDtlsId().equals(order.getLabSampleDtlsId()))
	                     .findAny()
	                     .orElse(null);
	              
	              if(retestDto != null){
	                  labResultMasterDto.setRetestCount(retestDto.getRetestCount()!= null ? retestDto.getRetestCount() : null);
	              }
	              
	           }   
	    	  
	        return new Response(SUCCESS, SUCCESS_200_MESSAGE,null, listLabResultMasterDto, null);
	      }
	     
	      
	  } catch (Exception e)
	  {
	      logger.error("Exection", e);
	      throw e;
	  }

}

@Override
public Response getDoctorIdByLabSampleDetailsId(Integer labSampleDtlsId) throws ApplicationException {
	try
    {
		AdmissionDto checkAdmitionId = (AdmissionDto)sessionFactory.getCurrentSession().createSQLQuery(
			"select (case " + 
			"		when tlsm.encounter_id is not null then (select tem.doctor_id from opd.t_encounter_master tem where tem.encounter_id = tlsm.encounter_id) " + 
			"		else (select  ta.doctor_id from adt.t_admission ta  where ta.admission_id = tlsm.admission_id) " + 
			"		end ) as \"doctorId\" , " + 
			"		(case " + 
			"		when tlsm.encounter_id is not null then (select trd.referral_id from t_referal_details trd where trd.encounter_id = tlsm.encounter_id) " + 
			"		else (select trd.referral_id from t_referal_details trd  where trd.admission_id = tlsm.admission_id) " + 
			"		end ) as \"referralId\" ,"
			+ "     tlsm.patient_id as \"patientId\" " + 
			"		from lab.t_lab_sample_dtls tlsd  " + 
			"		inner join lab.t_lab_sample_master tlsm  on tlsd.lab_sample_id  = tlsm.lab_sample_id   " + 
			"		where tlsd.lab_sample_dtls_id ="+labSampleDtlsId)
			.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).uniqueResult();
		
			return new Response<>(SUCCESS, null, null, null, checkAdmitionId);
    } catch (Exception e)
    {
        logger.error("Exection", e);
        throw new ServiceException(e.getMessage());
    }
}

@Override
public Response getOrderIdByOrderNumber(List<String> orderNos, Integer unitId) throws ApplicationException {
	
	  try 
	  {
	      List<OrderMasterDto> orderList=  sessionFactory.getCurrentSession()
	              .getNamedQuery("GET_ORDER_ID_BY_ORDER_NUMBER")
	              .setParameterList("orderNos", orderNos)
	              .setParameter("unitId", unitId)
	              .setResultTransformer(Transformers.aliasToBean(OrderMasterDto.class)).list();                    
	      return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, orderList, null);

	  } catch (Exception e)
	  {
	      logger.error("Exection", e);
	      throw e;
	  }
}

@Override
public Response getCompanyIdByOrderId(Integer orderId) throws ApplicationException {
	  try 
	  {
	      OrderMasterDto orderDto=  (OrderMasterDto) sessionFactory.getCurrentSession()
	              .getNamedQuery("GET_COMPANY_ID_BY_ORDER_ID")
	              .setParameter("orderId", orderId)
	              .setResultTransformer(Transformers.aliasToBean(OrderMasterDto.class)).uniqueResult();                    
	      return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, orderDto);

	  } catch (Exception e)
	  {
	      logger.error("Exection", e);
	      throw e;
	  }
}

@Override
public Response getLabSampleDetailsIdByOrderId(SearchDto searchDto) throws ApplicationException {
	 try 
	  {
			List<SearchDto> LabSampleDetailsMasterList = sessionFactory.getCurrentSession().getNamedQuery("GET_LAB_SAMPLEDETAILS_ID_BY_ORDER_ID")
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orderId", searchDto.getOrderId())
					.setResultTransformer(Transformers.aliasToBean(SearchDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_200, null, LabSampleDetailsMasterList , null);
	  } catch (Exception e)
	  {
	      logger.error("Exection", e);
	      throw e;
	  }
}

@Override
public Response updateQAPathInLabsampleDetails(ByteArrayOutputStream bos, String docPath, Integer labSampleDtlsId, String labAPIUrl)
		throws ApplicationException {
	try {
		
		 byte [] data = bos.toByteArray();

		Session session=sessionFactory.getCurrentSession();
		String hql = "UPDATE lab.t_lab_sample_dtls SET qr_image = :b, qr_path = :docPath, lab_report_url = :labAPIUrl WHERE lab_sample_dtls_id = :labSampleDtlsId";
		Integer count = session.createSQLQuery(hql)
				.setBinary("b", data)
				.setParameter("docPath", docPath)
				.setParameter("labAPIUrl", labAPIUrl)
				.setParameter("labSampleDtlsId", labSampleDtlsId)
				.executeUpdate();

		return new Response(SUCCESS, SUCCESS_200, null, null, count);
} catch (Exception e) {
	 logger.error("Exection", e);
	throw e;
}
}

@Override
public Response checkOutStandingAmount(Integer encounterId) throws ApplicationException {
	  try {
		  BigInteger count = (BigInteger) sessionFactory.getCurrentSession()
					.createSQLQuery("select count(*)  from billing.m_billing_master  where encounter_id  =:encounterId and outstanding >0 ").setParameter("encounterId", encounterId).uniqueResult();
			return new Response(SUCCESS, SUCCESS_200, null, null, count.intValue());

		} catch (HibernateException he) {
			logger.error("Exection", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("Exection", e);
			throw new ServiceException(e.getMessage());
		}
}

@Override
public Response getPayee(OrderMasterDto orderDto) throws ApplicationException {
	try {	
		
		 if(orderDto.getPaymentEntitlementId() != null && orderDto.getPaymentEntitlementId() == ICommonConstants.INSURANCE) {
			  BigInteger count = (BigInteger) sessionFactory.getCurrentSession()
						.createSQLQuery("select count(*) from adt.t_insurance ti where insurance_id  =:payeeId and  block_report_print = 'Y' ").setParameter("payeeId", orderDto.getInsuranceId()).uniqueResult();
				return new Response(SUCCESS, SUCCESS_200, null, null, count.intValue());
			
		 }else  if(orderDto.getPaymentEntitlementId() != null && orderDto.getPaymentEntitlementId() == ICommonConstants.COMPANY) {
			  BigInteger count = (BigInteger) sessionFactory.getCurrentSession()
						.createSQLQuery("SELECT count(*) FROM public.t_company_master where company_id  =:payeeId and  block_report_print = 'Y' ").setParameter("payeeId", orderDto.getPayeeId()).uniqueResult();
				return new Response(SUCCESS, SUCCESS_200, null, null, count.intValue());
		 }
		
		
	}catch(Exception e) {
		
		throw e;
	}
	return new Response(ERROR, null, null, null, null);
	}

@Override
public Response getCheckIsParameterCritical(List<Integer> labSampleDetlsIds) throws ApplicationException {
	try {
		
		BigInteger count = (BigInteger) sessionFactory.getCurrentSession()
					.createSQLQuery("select count(*) from lab.t_lab_test_result tltr " +
							"	inner join lab.t_lab_res_dtls tlrd on tltr.lab_test_res_id  =tlrd.lab_result_id " +
							"	where lab_sample_dtls_id in(:labSampleDetlsIds) "
							+ "and tlrd.is_abnormal  = 'Y' ")
				.setParameterList("labSampleDetlsIds", labSampleDetlsIds)
				.uniqueResult();
			return new Response(SUCCESS, SUCCESS_200, null, null, count.intValue());
		
		}catch(Exception e) {
			
			throw e;
		}
	
	}

@Override
public Response getLabExclusion(Integer labSampleDtlsId, Integer unitId, Integer orgId) throws ApplicationException {
	try {
		
		BigInteger count = (BigInteger) sessionFactory.getCurrentSession()
					.createSQLQuery(" select count(*) from lab.t_lab_sample_dtls tlsd  " + 
							" inner join lab.m_test_master mtm  on mtm.test_id  = tlsd.test_id   " + 
							" inner  join service.t_unit_service_master_details tusmd on tusmd.service_master_id  =  mtm.service_id  " + 
							" where tusmd.lab_auto_mail_exclusion = 'Y' and tlsd.lab_sample_dtls_id  =:labSampleDtlsId "+" and tusmd.organization_id =:orgId "+" and tusmd.unit_id =:unitId "+" limit 1")
				.setParameter("labSampleDtlsId", labSampleDtlsId)
				.setParameter("orgId", orgId)
				.setParameter("unitId", unitId)
				.uniqueResult();
			return new Response(SUCCESS, SUCCESS_200, null, null, count.intValue());
		
		}catch(Exception e) {
			
			throw e;
		}
	}

@Override
public Response getbucketTypeForUpload(Integer orgId,Integer unitId) throws ApplicationException {
try {		
	Integer bucketType = (Integer) sessionFactory.getCurrentSession()
				.createSQLQuery("select bucket_flag from t_unit_application_billing_configuration where organization_id =:orgId "+" and unit_id =:unitId")
			.setParameter("orgId", orgId)
			.setParameter("unitId", unitId)
			.uniqueResult();
		
		if(bucketType == null) {
			bucketType=1;
		}
		
		return new Response(SUCCESS, SUCCESS_200, null, null, bucketType);
		
		}catch(Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getAwsCreds() throws ApplicationException {
		try {
			String queryAWSCred = "select access_key as \"awsAccessKey\", secret_key as \"awsSecretKey\" from t_aws_credentials " +
					" where status = 'A' order by aws_key_id desc limit 1";
			OrganizationMasterDto awsCredDto = (OrganizationMasterDto) sessionFactory.getCurrentSession().createSQLQuery(queryAWSCred)
					.setResultTransformer(Transformers.aliasToBean(OrganizationMasterDto.class)).uniqueResult();

			return new Response(SUCCESS, SUCCESS_200, null, null, awsCredDto);

		}catch(Exception e) {

			throw e;
		}
	}

}

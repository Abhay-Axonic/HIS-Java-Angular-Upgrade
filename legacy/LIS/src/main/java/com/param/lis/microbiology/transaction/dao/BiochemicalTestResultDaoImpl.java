package com.param.lis.microbiology.transaction.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.micro.TBiochemicalTestDetailsMaster;
import com.param.entity.lis.micro.TBiochemicalTestMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.unit.dto.LabConfigurationDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.common.StringCommonMethods;
import com.param.lis.microbiology.transaction.dto.MicrobioParamDto;
import com.param.lis.microbiology.transaction.dto.TBiochemicalTestMasterDto;
import com.param.lis.transaction.dto.SampleAcceptancePendingDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BiochemicalTestResultDaoImpl extends GenericDao<TBiochemicalTestMaster, Integer>
implements IBiochemicalTestResultDao, ICommonConstants, ITransactionConstants, IError {

  public BiochemicalTestResultDaoImpl() {
    super(TBiochemicalTestMaster.class);
  }
  
  @Autowired
  private SessionFactory sessionFactory;
  
  final static Logger logger = Logger.getLogger(BiochemicalTestResultDaoImpl.class);

  @Autowired
  private DozerBeanMapper mapper;

  @Override
  public Response getBiochemicalWorkList(MicrobioParamDto microbioParamDto)
      throws ApplicationException {
    List<SampleAcceptancePendingDto> listMicrobioResultEntryMasterDto = null;
    try {
    	
    	List<LabConfigurationDto> list1=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_LAB_CONFIG_LIST")
				.setParameter("orgId", microbioParamDto.getOrgId())
				.setParameter("unitId", microbioParamDto.getOrgUnitId())
				.setResultTransformer(Transformers.aliasToBean(LabConfigurationDto.class)).list();
    	
    	 List<Integer> statusId = new ArrayList<Integer>();
		    
    	 if(list1.get(0).getMacroscopicExaminationApplicable() == 'Y') {
				
				statusId = BIOCHEMICAL_SAMPLE_STATUS_IDS;
				
			}else if(list1.get(0).getIncubationMasterApplicable() == 'Y') {
				
				statusId =  Arrays.asList(INCUBATION_OBJSERVATION_COMPLETED,DURING_BIOCHEMICAL_RESULT_ENTRY);
				
			}else  if(list1.get(0).getIsWorklistDepartment() == 'Y') {
				
				statusId = Arrays.asList(SEND_FOR_INCUBATION_OBERVATION,DURING_BIOCHEMICAL_RESULT_ENTRY);
				
			}else if(list1.get(0).getIsAcceptanceInDepartment() == 'Y') {
				
				statusId = Arrays.asList(SAMPLE_ACCEPTED,DURING_BIOCHEMICAL_RESULT_ENTRY);
				
			}else if(list1.get(0).getIsSampleAcceptanceApplicable() == 'Y') {
				
				statusId = Arrays.asList(SAMPLE_SEND_TO_DEPT,DURING_BIOCHEMICAL_RESULT_ENTRY);
				
			}else if (list1.get(0).getIsSendToCrApplicable() != null && list1.get(0).getIsSendToCrApplicable() == 'Y') {
							
				statusId = Arrays.asList(SAMPLE_SEND_TO_CR,DURING_BIOCHEMICAL_RESULT_ENTRY);
				
			}else {
				
				statusId = Arrays.asList(SAMPLE_COLLECTED,DURING_BIOCHEMICAL_RESULT_ENTRY);
			}
    	
    	
    	String query = sessionFactory.getCurrentSession().getNamedQuery("BIOCHEMICAL_RESULT_ENTRY_WORKLIST").getQueryString();
		StringBuffer sb= new StringBuffer(query);
	    sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:currentDate ");
	    sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:currentDate ");
	    sb.append(" order by lbsampledtls.updated_date asc ");
    	
        listMicrobioResultEntryMasterDto = sessionFactory.getCurrentSession()
        		.createSQLQuery(sb.toString())
                .setParameter("orgId", microbioParamDto.getOrgId())
                .setParameter("orgUnitId", microbioParamDto.getOrgUnitId())
                .setParameterList("sampleStatusIds", statusId)
                .setParameter("deptId", microbioParamDto.getDeptId())
				.setParameter("currentDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(microbioParamDto.getOrgUnitId()),"yyyy-MM-dd"))
                .setParameter("subDeptId", microbioParamDto.getSubDeptId())
                .setFirstResult(microbioParamDto.getOffset() != null ? microbioParamDto.getOffset() : 0)
                .setMaxResults(
                        microbioParamDto.getRecordPerPage() != null ? microbioParamDto.getRecordPerPage() : 50)
                .setResultTransformer(Transformers.aliasToBean(SampleAcceptancePendingDto.class)).list();
        return new Response(SUCCESS, SUCCESS_200, null, listMicrobioResultEntryMasterDto, null);
    } catch (Exception e) {
        logger.error("Exception", e);
        throw e;
    }
  }

  @Override
  public Response getBiochemicalWorkListCount(MicrobioParamDto microbioParamDto)
      throws ApplicationException {
    try {
    	
    	List<LabConfigurationDto> list1=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_LAB_CONFIG_LIST")
				.setParameter("orgId", microbioParamDto.getOrgId())
				.setParameter("unitId", microbioParamDto.getOrgUnitId())
				.setResultTransformer(Transformers.aliasToBean(LabConfigurationDto.class)).list();
    	
    	 List<Integer> statusId = new ArrayList<Integer>();
		    
    	 if(list1.get(0).getMacroscopicExaminationApplicable() == 'Y') {
				
				statusId = BIOCHEMICAL_SAMPLE_STATUS_IDS;
				
			}else if(list1.get(0).getIncubationMasterApplicable() == 'Y') {
				
				statusId =  Arrays.asList(INCUBATION_OBJSERVATION_COMPLETED,DURING_BIOCHEMICAL_RESULT_ENTRY);
				
			}else  if(list1.get(0).getIsWorklistDepartment() == 'Y') {
				
				statusId = Arrays.asList(SEND_FOR_INCUBATION_OBERVATION,DURING_BIOCHEMICAL_RESULT_ENTRY);
				
			}else if(list1.get(0).getIsAcceptanceInDepartment() == 'Y') {
				
				statusId = Arrays.asList(SAMPLE_ACCEPTED,DURING_BIOCHEMICAL_RESULT_ENTRY);
				
			}else if(list1.get(0).getIsSampleAcceptanceApplicable() == 'Y') {
				
				statusId = Arrays.asList(SAMPLE_SEND_TO_DEPT,DURING_BIOCHEMICAL_RESULT_ENTRY);
				
			}else if (list1.get(0).getIsSendToCrApplicable() != null && list1.get(0).getIsSendToCrApplicable() == 'Y') {
							
				statusId = Arrays.asList(SAMPLE_SEND_TO_CR,DURING_BIOCHEMICAL_RESULT_ENTRY);
				
			}else {
				
				statusId = Arrays.asList(SAMPLE_COLLECTED,DURING_BIOCHEMICAL_RESULT_ENTRY);
			}
    	
    	String query = sessionFactory.getCurrentSession().getNamedQuery("BIOCHEMICAL_RESULT_ENTRY_WORKLIST_COUNT").getQueryString();
		StringBuffer sb= new StringBuffer(query);
	    sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:currentDate ");
	    sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:currentDate ");
    	
      BigInteger incubationObservationListCount = (BigInteger) sessionFactory.getCurrentSession()
    		  .createSQLQuery(sb.toString())
              .setParameter("orgId", microbioParamDto.getOrgId())
              .setParameterList("sampleStatusIds", statusId)
              .setParameter("deptId", microbioParamDto.getDeptId())
              .setParameter("subDeptId", microbioParamDto.getSubDeptId())
			  .setParameter("currentDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(microbioParamDto.getOrgUnitId()),"yyyy-MM-dd"))
              .setParameter("orgUnitId", microbioParamDto.getOrgUnitId()).uniqueResult();
      if (incubationObservationListCount.compareTo(BigInteger.ZERO) == 1) {
          return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, incubationObservationListCount);
      } else {
          return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, BigInteger.ZERO);
      }

  } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
  }
  }

  @Override
  public Response saveBiochemicalResult(TBiochemicalTestMasterDto tBiochemicalTestMasterDto)
      throws ApplicationException {
    try {

      if(tBiochemicalTestMasterDto!=null)
      {
        TBiochemicalTestMaster tBiochemicalTestMaster = mapper.map(tBiochemicalTestMasterDto,
            TBiochemicalTestMaster.class, "TBiochemicalTestMasterDtoToTBiochemicalTestMaster");
        TBiochemicalTestMaster tMicroIncubationMst = save(tBiochemicalTestMaster);
          for (Iterator iterator = tMicroIncubationMst.getListTBiochemicalTestDetailsMaster().iterator(); iterator.hasNext();) 
          {
            TBiochemicalTestDetailsMaster tBiochemicalTestDetailsMaster = (TBiochemicalTestDetailsMaster) iterator.next();
            tBiochemicalTestDetailsMaster.setBiochemTestId(tBiochemicalTestMaster.gettBiochemicalTestId());
            sessionFactory.getCurrentSession().save(tBiochemicalTestDetailsMaster);
          }
          return new Response(SUCCESS, SUCCESS_200,SUCCESS_200_MESSAGE, null, null);
      }
      else {
        return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
      }
        
  } catch (HibernateException er) {
      logger.error("Exection", er);throw er;
  } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
  }
  }

  @Override
  public Response updateBiochemicalResult(TBiochemicalTestMasterDto tBiochemicalTestMasterDto)
      throws ApplicationException {
    try {

      if(tBiochemicalTestMasterDto!=null)
      {
        TBiochemicalTestMaster tBiochemicalTestMaster = mapper.map(tBiochemicalTestMasterDto,
            TBiochemicalTestMaster.class, "TBiochemicalTestMasterDtoToTBiochemicalTestMaster");
        TBiochemicalTestMaster tBiochemicalTestMst = update(tBiochemicalTestMaster);
          for (Iterator iterator = tBiochemicalTestMst.getListTBiochemicalTestDetailsMaster().iterator(); iterator.hasNext();) 
          {
            TBiochemicalTestDetailsMaster tBiochemicalTestDetailsMaster = (TBiochemicalTestDetailsMaster) iterator.next();
            sessionFactory.getCurrentSession().update(tBiochemicalTestDetailsMaster);
          }
          return new Response(SUCCESS, SUCCESS_200,SUCCESS_200_MESSAGE, null, null);
      }
      else {
        return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
      }
        
  } catch (HibernateException er) {
      logger.error("Exection", er);throw er;
  } catch (Exception e) {
      logger.error("Exception", e);throw e;
  }
  }

  @Override
  public Response getBiochemicalResultDetails(Integer tBiochemicalTestId, Integer orgId,
      Integer orgUnitId) throws ApplicationException {
    try 
    {
        TBiochemicalTestMaster tBiochemicalTestMaster =findById(tBiochemicalTestId);
        if(tBiochemicalTestMaster!=null)
        {
          TBiochemicalTestMasterDto tBiochemicalTestMasterDto =   mapper.map( tBiochemicalTestMaster, TBiochemicalTestMasterDto.class, "TBiochemicalTestMasterDtoToTBiochemicalTestMaster");
          return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, tBiochemicalTestMasterDto);
        }
        else 
        {
          return new Response(ERROR, ERR_500, "No Previous Details Found.", null, null);
        }
    } catch (HibernateException er) 
    {
      logger.error("Exection", er);
      throw er;
    } catch (Exception e)
    {
      logger.error("Exception", e);
      throw e;
    }
  }

@Override
public Response biochemicalWorkListSearch(SearchDto searchDto) throws ApplicationException {
	try {
		
		List<LabConfigurationDto> list1=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_LAB_CONFIG_LIST")
				.setParameter("orgId", searchDto.getOrgId())
				.setParameter("unitId", searchDto.getOrgUnitId())
				.setResultTransformer(Transformers.aliasToBean(LabConfigurationDto.class)).list();
    	
    	 List<Integer> statusId = new ArrayList<Integer>();
		    
    	 if(list1.get(0).getMacroscopicExaminationApplicable() == 'Y') {
				
				statusId = BIOCHEMICAL_SAMPLE_STATUS_IDS;
				
			}else if(list1.get(0).getIncubationMasterApplicable() == 'Y') {
				
				statusId =  Arrays.asList(INCUBATION_OBJSERVATION_COMPLETED,DURING_BIOCHEMICAL_RESULT_ENTRY);
				
			}else  if(list1.get(0).getIsWorklistDepartment() == 'Y') {
				
				statusId = Arrays.asList(SEND_FOR_INCUBATION_OBERVATION,DURING_BIOCHEMICAL_RESULT_ENTRY);
				
			}else if(list1.get(0).getIsAcceptanceInDepartment() == 'Y') {
				
				statusId = Arrays.asList(SAMPLE_ACCEPTED,DURING_BIOCHEMICAL_RESULT_ENTRY);
				
			}else if(list1.get(0).getIsSampleAcceptanceApplicable() == 'Y') {
				
				statusId = Arrays.asList(SAMPLE_SEND_TO_DEPT,DURING_BIOCHEMICAL_RESULT_ENTRY);
				
			}else if (list1.get(0).getIsSendToCrApplicable() != null && list1.get(0).getIsSendToCrApplicable() == 'Y') {
							
				statusId = Arrays.asList(SAMPLE_SEND_TO_CR,DURING_BIOCHEMICAL_RESULT_ENTRY);
				
			}else {
				
				statusId = Arrays.asList(SAMPLE_COLLECTED,DURING_BIOCHEMICAL_RESULT_ENTRY);
			}
		
	      String query = sessionFactory.getCurrentSession()
	          .getNamedQuery("BIOCHEMICAL_RESULT_ENTRY_WORKLIST").getQueryString().toString();

	      StringBuilder sb = new StringBuilder(query);

	      if (searchDto.getFromDt() != null ) {
	    	  sb.append(
	            " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt ");
	      }

	      if (searchDto.getToDt() != null ) {
	    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt ");
	      }

	      if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
	    	  sb.append(" AND pati_mst.patient_id =:patientId ");

	      if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty())
	    	  sb.append(" AND visit_type_master.visit_type_id IN (:visitTypes)");

	      if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty())
	    	  sb.append(" AND test_mst.dept_id IN (:subDepts)");
	      
	      if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty())
	    	  sb.append(" AND test_mst.test_id in (:testTypes)");
	      
	      if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0)
	    	  sb.append(" AND lbsampledtls.lab_sample_dtls_id =:labSampleDtlsId  ");

	      if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
	          sb.append("  and lbsampledtls.assign_unit  =:assignUnit ");
	      
	      if(searchDto.getUhidNumber()!="") {
	    	  sb.append(" AND lower(pati_mst.uhid_number) LIKE :uhidNumber  ");
	      }
	      
	      if(searchDto.getSampleNo()!="") {
	    	  sb.append(" AND lower(sample_mst.sample_no) LIKE :sampleNo ");
	      }

	      
	      sb.append(" order by lbsampledtls.updated_date asc ");
	      
	      Query createdQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
	     createdQuery.setParameter("orgId", searchDto.getOrgId())
	                .setParameter("orgUnitId", searchDto.getOrgUnitId())
	                .setParameterList("sampleStatusIds", statusId)
	                .setParameter("deptId", searchDto.getDeptId())
	                .setParameter("subDeptId", searchDto.getSubDeptId());
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
			createdQuery.setParameterList("visitTypes", searchDto.getVisitTypes());
		}
		if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()){
			createdQuery.setParameterList("subDepts", searchDto.getSubDepts());
		}
		if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()){
			createdQuery.setParameterList("testTypes", searchDto.getTestTypes());
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

		List<SampleAcceptancePendingDto> list  = createdQuery.setResultTransformer(Transformers.aliasToBean(SampleAcceptancePendingDto.class)).list();


	      if (list.isEmpty()) {
	        return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.",
	        		list, null);
	      } else {
	        return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, list,
	            null);
	      }


	    } catch (Exception e) {
	      logger.error("Exection", e);
	      throw e;
	    }
}
  
 

}

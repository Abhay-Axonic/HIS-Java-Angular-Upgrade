
package com.param.lis.transaction.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.param.global.dto.TemplateTransactionDto;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.transaction.LabResultDetailsMaster;
import com.param.entity.lis.transaction.LabResultMaster;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.entity.lis.unit.TestMaster;
import com.param.entity.lis.unit.TestParamMppr;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ServiceException;
import com.param.global.dto.BillingMasterDto;
import com.param.global.otims.dto.ProcedureSchedulingDto;
import com.param.global.unit.dao.ILabConfigurationDao;
import com.param.global.unit.dto.LabConfigurationDto;
import com.param.lis.common.dto.LabMachinResultDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.common.StringCommonMethods;
import com.param.lis.global.common.obj;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.LabResultDetailsViewDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.transaction.dto.LabSampleMasterDto;
import com.param.lis.transaction.dto.MultyParamTestHeaderDto;
import com.param.lis.transaction.dto.SingParamTestResDto;
import com.param.lis.unit.dao.IFormulaMasterDao;
import com.param.lis.unit.dao.IParameterMasterDao;
import com.param.lis.unit.dto.HelpValueMasterDto;
import com.param.lis.unit.dto.ParameterMasterDto;
import com.param.lis.unit.dto.TestMasterDto;
import com.param.lis.unit.dto.TestParamMpprDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BioChemResultEntryDaoImpl extends GenericDao<LabResultMaster, Integer>
		implements IBioChemResultEntryDao, ICommonConstants, ITransactionConstants, IError {

	public BioChemResultEntryDaoImpl() {
		super(LabResultMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;
	
	@Autowired
    private IParameterMasterDao iParameterMasterDao;
	
	@Autowired
    private IFormulaMasterDao iFormulaMasterDao;
	
	@Autowired
	private IBiochemValidationWorklistDao iBiochemValidationWorklistDao;
	
	@Autowired
	ILabConfigurationDao iLabConfigurationDao;
	
	@Autowired
	ILabTransactionDao iLabTransactionDao;

	// @Autowired
	// private SessionFactory sessionFactory;

	final static Logger logger = Logger.getLogger(BioChemResultEntryDaoImpl.class);

	@Override
	public Response getResultEntryList(BioChemParamDto bioChemParamDto) throws ApplicationException {
		List<LabResultMasterDto> listLabResultMasterDto = null;
		try {
			
			  
			
		    String query = sessionFactory.getCurrentSession().getNamedQuery("GET_BIOCHEM_RESULT_ENTRY_LIST").getQueryString();
			StringBuffer sb= new StringBuffer(query);
			sb.append("  and lbsampledtls.assign_unit  =:orgUnitId ");
			sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') >=:currentDate ");
			sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') <=:currentDate ");
			sb.append(" ORDER BY  " 
			        + "  lbsampledtls.priority_id,  "
			        + "  order_master.order_date  "  );
			  
			listLabResultMasterDto = sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
					.setParameter("orgId", bioChemParamDto.getOrgId())
					.setParameter("orgUnitId", bioChemParamDto.getOrgUnitId())
					.setParameter("sampleStatusId", SEND_TO_REPORT_ENTRY)
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
	public Response getResultEntryListCount(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {
			
			 String query = sessionFactory.getCurrentSession().getNamedQuery("GET_BIOCHEM_RESULT_ENTRY_LIST_COUNT").getQueryString();
			 StringBuffer sb= new StringBuffer(query);
			 sb.append("  and lbsampledtls.assign_unit  =:assignUnit ");
			 List<Integer> statusIds = new ArrayList<Integer>(); 
			 if(bioChemParamDto.getIsOnHoldParameter() == false) {
			if(bioChemParamDto.getIsWorklistDepartment() == 'Y') {
		   			
		   			statusIds = Arrays.asList(SEND_TO_REPORT_ENTRY);
		   			
		   		}else if(bioChemParamDto.getIsAcceptanceInDepartment() == 'Y') {
		   			
		   			statusIds = Arrays.asList(SAMPLE_ACCEPTED);
		   			
		   		}else if(bioChemParamDto.getIsSampleAcceptanceApplicable() == 'Y') {
		   			
		   			statusIds = Arrays.asList(SAMPLE_SEND_TO_DEPT);
		   			
		   		}else if (bioChemParamDto.getIsSendToCrApplicable() != null && bioChemParamDto.getIsSendToCrApplicable() == 'Y') {
		   						
		   			statusIds =Arrays.asList(SAMPLE_SEND_TO_CR,SAMPLE_COLLECTED);
		   			
		   		}else {
		   			
		   			statusIds = Arrays.asList(SAMPLE_COLLECTED);
		   		}
		   		}
			 
		   //  sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') >= '" + GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId),"yyyy-MM-dd") + "' ");
		   //  sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') <= '" + GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId),"yyyy-MM-dd") + "' ");
			
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
              if (statusIds != null && statusIds.size()>0 && bioChemParamDto.getIsOnHoldParameter() == false) {
                  sb.append(" and (lbsampledtls.sample_status_id IN(:statusIds) or lbsampledtls.is_on_hold_parameter = true )");
              }
		      
		      

		     
			Query sql = sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("orgId", bioChemParamDto.getOrgId())
//					.setParameterList("sampleStatusId", statusIds)
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
				sql.setParameter("isOnHoldParameter", bioChemParamDto.getIsOnHoldParameter());
				sql.setParameterList("statusIds", statusIds);
			}
			if (statusIds != null && statusIds.size()>0 && bioChemParamDto.getIsOnHoldParameter() == false) {
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
	public Response getResultEntryDetails(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {

		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, TEST_NOT_FOUND, null, null);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response saveEntryDetails(List<LabResultMasterDto> listLabResultMasterDto) throws ApplicationException {
	  

    try {

      for (Iterator iterator = listLabResultMasterDto.iterator(); iterator.hasNext();) {
        LabResultMasterDto labResultMasterDto = (LabResultMasterDto) iterator.next();
        Long currentTimeWithZone  = TimeZoneComponent.getDateByzone(labResultMasterDto.getOrgUnitId()).getTime();
        LabResultMaster labResultMaster = mapper.map(labResultMasterDto, LabResultMaster.class,
            "LabResultMasterDtoToLabResultMaster");
        labResultMaster.setResultEnterBy(labResultMasterDto.getCreatedBy());
        labResultMaster.setCreatedDate(currentTimeWithZone);
        labResultMaster.setCreatedBy(labResultMasterDto.getCreatedBy());
        labResultMaster.setResultEnterDatetime(currentTimeWithZone);
        labResultMaster.setManualResultEnterTime(currentTimeWithZone);
        labResultMaster.setStatus('A');
        LabResultMaster labResultMst = save(labResultMaster);
        
        LabSampleDetailsMaster labSampleDetailsMaster =
            (LabSampleDetailsMaster) sessionFactory.getCurrentSession()
                .get(LabSampleDetailsMaster.class, labResultMst.getLabSampleDtlsId());
        labSampleDetailsMaster.setInternalNotes(labResultMasterDto.getInternalNotes());
        labSampleDetailsMaster.setSrfId(labResultMasterDto.getSrfId());
		labSampleDetailsMaster.setIcmrId(labResultMasterDto.getIcmrId());
        labSampleDetailsMaster.setUpdatedBy(labResultMst.getUpdatedBy());
        labSampleDetailsMaster.setUpdatedDate(currentTimeWithZone);
        if(labResultMasterDto.getSampleStatusId() != null && (labResultMasterDto.getSampleStatusId() == 8 || labResultMasterDto.getSampleStatusId() == 35)){
        	labSampleDetailsMaster.setSampleStatusId(labResultMasterDto.getSampleStatusId());
        }else{
        	labSampleDetailsMaster.setSampleStatusId(RESULT_ENTRY_DONE);
        }
        
        labSampleDetailsMaster.setIsOnHoldParameter(labResultMasterDto.getIsOnHoldParameter());
        labSampleDetailsMaster.setProvisional(labResultMasterDto.getProvisional()!= null ? labResultMasterDto.getProvisional() : false);
        sessionFactory.getCurrentSession().update(labSampleDetailsMaster);
        
       com.param.global.common.Response res =  iLabConfigurationDao.getLabConfigurationList(labSampleDetailsMaster.getOrgId(),labSampleDetailsMaster.getAssignUnit());
        Character c = null;
       if(res.getListObject() != null && res.getListObject().size()>0) {
    	   
    	   List<LabConfigurationDto> list = res.getListObject() ;
    	  c = list.getFirst().getSkipValidation();
       }
       
       
	   	String sql=" select parameter_id as \"parameterId\" , lab_res_dtls_id \"labResDtlsId\" from lab.t_lab_res_dtls where lab_result_id =:testResId  ";
		List<LabResultDetailsMaster> list1  = sessionFactory.getCurrentSession()
		          .createSQLQuery(sql)
				.setParameter("testResId", labResultMst.getLabTestResId())
				.setResultTransformer(Transformers.aliasToBean(LabResultDetailsMaster.class)).list();
       
       if(list1 != null && list1.size()>0){
    	   
    	   for (Iterator iterator1 =  labResultMst.getListLabSampleResultDetailsMaster().iterator(); iterator1.hasNext();) {
    		   
	   	          LabResultDetailsMaster labResultDetailsMaster = (LabResultDetailsMaster) iterator1.next();
	   	          
	   	          
	   	          labResultDetailsMaster.setLabResultId(labResultMst.getLabTestResId());
	   	          labResultDetailsMaster.setFirstLevelResult(labResultDetailsMaster.getFinalResult());
	   	          if(c.equals('Y')) {
	   	        	  labResultDetailsMaster.setSecondLevelResult(labResultDetailsMaster.getFinalResult()); 
	   	          }
	   	          
	   	          for (LabResultDetailsMaster dtores : list1) {
					
	   	        	  if(dtores.getParameterId().equals(labResultDetailsMaster.getParameterId()) ){
	   	        		  
	   	        		labResultDetailsMaster.setLabResDtlsId(dtores.getLabResDtlsId());
	   	        	  }
				}
	   	          
	   	          labResultDetailsMaster.setCreatedBy(labResultMasterDto.getCreatedBy());
	   	          labResultDetailsMaster.setCreatedDate(currentTimeWithZone);
	   	          sessionFactory.getCurrentSession().saveOrUpdate(labResultDetailsMaster);
   	          
   	        } 
    	   
       }else{
    	   
    	   for (Iterator iterator1 =
    	            labResultMst.getListLabSampleResultDetailsMaster().iterator(); iterator1.hasNext();) {
    	          LabResultDetailsMaster labResultDetailsMaster = (LabResultDetailsMaster) iterator1.next();
    	          labResultDetailsMaster.setLabResultId(labResultMst.getLabTestResId());
    	          labResultDetailsMaster.setFirstLevelResult(labResultDetailsMaster.getFinalResult());
    	          if(c.equals('Y')) {
    	        	  labResultDetailsMaster.setSecondLevelResult(labResultDetailsMaster.getFinalResult()); 
    	          }
    	          labResultDetailsMaster.setCreatedBy(labResultMasterDto.getCreatedBy());
    	          labResultDetailsMaster.setCreatedDate(currentTimeWithZone);
    	          sessionFactory.getCurrentSession().save(labResultDetailsMaster);
    	        } 
    	   
       }
       

      }

      return new Response(SUCCESS, SUCCESS_200, SAMPLE_RESULT_ENTRY_SUCC, null, null);
    } catch (Exception e) {
      logger.error("Exception", e);
      return new Response(ERROR, ERR_500, SAMPLE_RESULT_ENTRY_FAIL, null, null);
    }
  }

	@Override
	public Response getSingleParamTestResult(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {
			List<ParameterMasterDto> listParameterMasterDto = new ArrayList();
			TestMaster testMaster = (TestMaster) sessionFactory.getCurrentSession().getNamedQuery("GET_TEST_BY_TEST_ID")
					.setParameter("orgId", bioChemParamDto.getOrgId())
					//.setParameter("orgUnitId", bioChemParamDto.getOrgUnitId())
					.setParameter("testId", bioChemParamDto.getTestId())
					.setParameter("testType", SINGLE_PARAM_TEST)
					.uniqueResult();

			TestMasterDto testMasterDto = testMaster != null
					? mapper.map(testMaster, TestMasterDto.class, "TestMasterDtoTOTestMaster") : null;
					
			
			if (testMaster != null) 
			{
				testMasterDto.setTechniqueName(testMaster.getTechniqueMaster()!=null?testMaster.getTechniqueMaster().getDesc():null);
				
//				List<TestParamMpprDto> sortedList=testMasterDto.getListTestParamMppr().stream().sorted(Comparator.comparing(TestParamMpprDto::getParaSequence)).collect(Collectors.toList());
	
				for (Iterator iterator = testMasterDto.getListTestParamMppr().iterator(); iterator.hasNext();) 
				{
					TestParamMpprDto testParamMpprDto = (TestParamMpprDto) iterator.next();
				/*	ParameterMaster parameterMst = (ParameterMaster) sessionFactory.getCurrentSession()
							.getNamedQuery("GET_PARAMETER_BY_PARAMETER_ID")
							.setParameter("orgId", bioChemParamDto.getOrgId())
							.setParameter("orgUnitId", bioChemParamDto.getOrgUnitId())
							.setParameter("parameterId", testParamMpprDto.getParameterId())
							.uniqueResult();

					for (Iterator iterator2 = parameterMst.getListParamRefrengMaster().iterator(); iterator2
							.hasNext();) {
						ParamRefrengMaster paramRefrengMaster = (ParamRefrengMaster) iterator2.next();
						if (paramRefrengMaster.getIsDeleted() != 'Y') {
							ParamRefrengMasterDto paramRefrengMasterDto = paramRefrengMaster != null
									? mapper.map(paramRefrengMaster, ParamRefrengMasterDto.class,
											"ParamRefrengMasterDtoToParamRefrengMaster")
									: null;
							paramRefrengMasterDto.setAgeFromDay(paramRefrengMaster.getAgeGroupMaster().getAgeFromDay());
							paramRefrengMasterDto.setAgeToDay(paramRefrengMaster.getAgeGroupMaster().getAgeToday());
							listParamRefrengMasterDto.add(paramRefrengMasterDto);
						}
					}
					ParameterMasterDto parameterMasterDto = parameterMst != null
							? mapper.map(parameterMst, ParameterMasterDto.class, "ParameterMasterDtoTOParameterMaster")
							: null;
					List<HelpValueMasterDto> listHelpValueMasterDto = parameterMasterDto.getListHelpValueMaster()
							.stream().filter(help -> help.getIsDeleted() != 'Y').collect(Collectors.toList());
					parameterMasterDto.setListParamRefrengMaster(listParamRefrengMasterDto);
					parameterMasterDto.setListHelpValueMaster(listHelpValueMasterDto);
					parameterMasterDto.setUnitName(parameterMst.getLabUnitMaster().getDesc());
					parameterMasterDto.setParameterName(parameterMst.getParameterName());*/
					
					 Response<ParameterMasterDto, Integer> paramRes = iParameterMasterDao
			              .getParameterById(bioChemParamDto.getOrgId(), bioChemParamDto.getOrgUnitId(), testParamMpprDto.getParameterId());
			          ParameterMasterDto parameterMasterDto = paramRes.getObject();
					listParameterMasterDto.add(parameterMasterDto);
				}
				testMasterDto.setListParameterMasterDto(listParameterMasterDto);
				testMasterDto.setTechniqueName(testMaster.getTechniqueMaster().getDesc());
			}else {
				return new Response(ERROR, ERR_500, "Parameter Not Mapped For This Test ...", null, null);
			}
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, testMasterDto);

		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getMultyParamTestResult(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {
			List<ParameterMasterDto> listParameterMasterDto = new ArrayList();
			List<TestParamMpprDto> listTestParamMpprDto = new ArrayList();

			TestMaster testMaster = (TestMaster) sessionFactory.getCurrentSession().getNamedQuery("GET_TEST_BY_TEST_ID")
					.setParameter("orgId", bioChemParamDto.getOrgId())
					//.setParameter("orgUnitId", bioChemParamDto.getAssingnedFrom())
					.setParameter("testId", bioChemParamDto.getTestId()).setParameter("testType", bioChemParamDto.getTestType())
					.uniqueResult();

			TestMasterDto testMasterDto = testMaster != null
					? mapper.map(testMaster, TestMasterDto.class, "TestMasterDtoTOTestMaster") : null;
			testMasterDto.setTechniqueName(testMaster.getTechniqueMaster()!=null?testMaster.getTechniqueMaster().getDesc():"");
			
			if (testMasterDto != null) 
			{
				
				List<TestParamMppr> sortedList=testMaster.getListTestParamMppr()
				
						.stream().filter(x->x.getIsDeleted()=='N' && x.getParaSequence()!=null).sorted(Comparator.comparing(TestParamMppr::getParaSequence)).collect(Collectors.toList());
				for (Iterator iterator =sortedList.iterator(); iterator.hasNext();) 
				{
			      TestParamMppr testParamMppr = (TestParamMppr) iterator.next();
				  if(testMaster.getTestId()==testParamMppr.getTestId().intValue()&&testParamMppr.getIsDeleted()=='N')
				  {
				    TestParamMpprDto testParamMpprDto = testParamMppr != null
                        ? mapper.map(testParamMppr, TestParamMpprDto.class, "TestParamMpprDtoToTestParamMppr")
                        : null;

                     testParamMpprDto.setHeaderDesc(testParamMppr.getHeaderMaster().getDesc());
                     testParamMpprDto.setHeaderCode(testParamMppr.getHeaderMaster().getCode());
                     listTestParamMpprDto.add(testParamMpprDto);
				  }
					
				}
				testMasterDto.setListParameterMasterDto(listParameterMasterDto);
				testMasterDto.setListTestParamMppr(listTestParamMpprDto);
				testMasterDto.setTechniqueName(testMaster.getTechniqueMaster().getDesc());
			}
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, testMasterDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}


	@Override
	public Response labResultEntrySearch(SearchDto searchDto) throws ApplicationException {
		try {

		      String query = sessionFactory.getCurrentSession()
		          .getNamedQuery("GET_BIOCHEM_RESULT_ENTRY_LIST").getQueryString().toString();

		      StringBuilder sb = new StringBuilder(query);
		      
		      List<Integer> statusIds = new ArrayList<Integer>(); 
		      if(searchDto.getIsOnHoldParameter() == false) {
		       if(searchDto.getIsWorklistDepartment() == 'Y') {
		   			
		   			statusIds = Arrays.asList(SEND_TO_REPORT_ENTRY);
		   			
		   		}else if(searchDto.getIsAcceptanceInDepartment() == 'Y') {
		   			
		   			statusIds = bioChemStatus;
		   			
		   		}else if(searchDto.getIsSampleAcceptanceApplicable() == 'Y') {
		   			
		   			statusIds = Arrays.asList(SAMPLE_SEND_TO_DEPT,RETESTING);
		   			
		   		}else if (searchDto.getIsSendToCrApplicable() != null && searchDto.getIsSendToCrApplicable() == 'Y') {
		   						
		   			statusIds =Arrays.asList(SAMPLE_SEND_TO_CR,RETESTING);
		   			
		   		}else {
		   			
		   			statusIds = Arrays.asList(SAMPLE_COLLECTED,RETESTING);
		   		}
		      }
		      if (searchDto.getFromDt() != null ) {
		    	  sb.append(
		            " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt ");
		      }

		      if (searchDto.getToDt() != null ) {
		    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt  ");
		      }

		      if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
		    	  sb
		            .append(" AND pati_mst.patient_id =:patientId  ");

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
		          sb.append("  and lbsampledtls.assign_unit  =:assignUnit  ");
		      
		      if(searchDto.getUhidNumber()!="") {
		    	  sb.append(" AND lower(pati_mst.uhid_number) LIKE :uhidNumber  ");
		      }
              if (searchDto.getIsOnHoldParameter() == true) {
            	  statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
            	  sb.append(" and lbsampledtls.sample_status_id IN(:statusIds)");
                  sb.append(" AND lbsampledtls.is_on_hold_parameter =:isOnHoldParameter  ");

              }
              if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
                  sb.append(" and (lbsampledtls.sample_status_id IN(:statusIds) or lbsampledtls.is_on_hold_parameter = true )");
              }

		      if(searchDto.getSampleNo()!="") {
		    	  sb.append(" AND lower(sample_mst.sample_id) LIKE :sampleNo ");
		      }
		      
			if (searchDto.getLabSortingId() != null) {
				if (searchDto.getLabSortingId() == 1) {
					sb.append(" ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
				} else if (searchDto.getLabSortingId() == 2) {
					sb.append(" ORDER BY   order_master.order_id DESC ");
				} else if (searchDto.getLabSortingId() == 3) {
					sb.append(" ORDER BY    sample_mst.sample_id DESC ");
				} else if (searchDto.getLabSortingId() == 4) {
					sb.append(" ORDER BY   lbsampledtls.updated_date  DESC ");
				}
			} else {
				sb.append(" ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
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

			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size() > 0 ){
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
			if (searchDto.getIsOnHoldParameter() == true) {
				createdQuery.setParameter("isOnHoldParameter", searchDto.getIsOnHoldParameter());
				createdQuery.setParameterList("statusIds", statusIds);

			}
			if (statusIds != null && statusIds.size() > 0  && searchDto.getIsOnHoldParameter() == false) {
				createdQuery.setParameterList("statusIds", statusIds);
			}

			if(searchDto.getSampleNo()!="") {
				createdQuery.setParameter("sampleNo", "%" + searchDto.getSampleNo().toLowerCase() + "%");
			}
		      List<LabResultMasterDto> list  = createdQuery
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
		    	  
		    	  

		  		String sql="	select count(sample_dtls_id) as \"retestCount\"  , sample_dtls_id as \"labSampleDtlsId\"  from lab.t_sample_audit_trail sample_audit_trail  "
		  				+ "where test_status_id = 7 and sample_audit_trail.org_id =:orgId "+"  group by sample_dtls_id  ";
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
	public Response getTestMachineResult(Map<String, Integer> sampleIds) throws ApplicationException {
		
		try {
				List<LabMachinResultDto> listLabMachineResult = new ArrayList<LabMachinResultDto>();
			
			for (Map.Entry<String, Integer> entry : sampleIds.entrySet())
			{
				List<LabMachinResultDto>  list = sessionFactory.getCurrentSession().getNamedQuery("GET_MACHINE_RESULT_BY_SAMPLE_NO")
						.setString("sampleNo", entry.getKey())
						.setResultTransformer(Transformers.aliasToBean(LabMachinResultDto.class)).list();
				if(list!=null && list.size()>0)
				{
					listLabMachineResult.addAll(list);
				}
			}
			return new Response(SUCCESS, SUCCESS_200, null, listLabMachineResult, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getMultyParameterTest(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {
			List<ParameterMasterDto> listParameterMasterDto = new ArrayList();
			List<TestParamMpprDto> listTestParamMpprDto = new ArrayList();

			TestMaster testMaster = (TestMaster) sessionFactory.getCurrentSession().getNamedQuery("GET_TEST_BY_TEST_ID")
					.setParameter("orgId", bioChemParamDto.getOrgId())
					//.setParameter("orgUnitId", bioChemParamDto.getAssingnedFrom())
					.setParameter("testId", bioChemParamDto.getTestId()).setParameter("testType", bioChemParamDto.getTestType())
					.uniqueResult();

			TestMasterDto testMasterDto = testMaster != null
					? mapper.map(testMaster, TestMasterDto.class, "TestMasterDtoTOTestMaster") : null;
			testMasterDto.setTechniqueName(testMaster.getTechniqueMaster()!=null?testMaster.getTechniqueMaster().getDesc():"");
			
			if (testMasterDto != null) 
			{
				
				List<TestParamMppr> sortedList=testMaster.getListTestParamMppr()
				
						.stream().filter(x->x.getIsDeleted()=='N' && x.getParaSequence()!=null).sorted(Comparator.comparing(TestParamMppr::getParaSequence)).collect(Collectors.toList());
				for (Iterator iterator =sortedList.iterator(); iterator.hasNext();) 
				{
			      TestParamMppr testParamMppr = (TestParamMppr) iterator.next();
				  if(testMaster.getTestId()==testParamMppr.getTestId().intValue()&&testParamMppr.getIsDeleted()=='N')
				  {
				    TestParamMpprDto testParamMpprDto = testParamMppr != null
                        ? mapper.map(testParamMppr, TestParamMpprDto.class, "TestParamMpprDtoToTestParamMppr")
                        : null;

                     testParamMpprDto.setHeaderDesc(testParamMppr.getHeaderMaster().getDesc());
                     testParamMpprDto.setHeaderCode(testParamMppr.getHeaderMaster().getCode());
                     listTestParamMpprDto.add(testParamMpprDto);
				  }
					
				}
				testMasterDto.setListParameterMasterDto(listParameterMasterDto);
				testMasterDto.setListTestParamMppr(listTestParamMpprDto);
				testMasterDto.setTechniqueName(testMaster.getTechniqueMaster().getDesc());
			}
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, testMasterDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getMultyParameterTestResult(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {

	     SingParamTestResDto testDto =
		(SingParamTestResDto) sessionFactory.getCurrentSession().getNamedQuery("GET_TEST_FOR_RESULT")
					.setParameter("labSampleDtlsId", bioChemParamDto.getLabSampleDtlsId())
					.setParameter("testId", bioChemParamDto.getTestId())
					.setResultTransformer(Transformers.aliasToBean(SingParamTestResDto.class))
					.uniqueResult();
			
			if (testDto != null) 
			{
				
			      List<LabResultDetailsViewDto> listResult =
			    		  (List<LabResultDetailsViewDto>)  sessionFactory.getCurrentSession().getNamedQuery("GET_PARAMETER_FOR_RESULT")
			    						.setParameter("labSampleDtlsId", bioChemParamDto.getLabSampleDtlsId())
			    						.setParameter("testId", bioChemParamDto.getTestId())
			    						.setResultTransformer(Transformers.aliasToBean(LabResultDetailsViewDto.class))
			    						.list();
			      
			      if(listResult!=null && listResult.size()>0)
			      {
			    	  List<Integer> hederSq  = new ArrayList<>();
			    	  for (LabResultDetailsViewDto labResultDetailsViewDto : listResult) {
						if(hederSq.size() == 0 ) {
							hederSq.add(labResultDetailsViewDto.getHeaderId());
						}else {
							int count = 0 ;
							for (Integer herderId : hederSq) {
								if(herderId.equals(labResultDetailsViewDto.getHeaderId())) {
									count = 1;
								}
							}
							
							if(count == 0 ) {
								hederSq.add(labResultDetailsViewDto.getHeaderId());
							}
						}
					}
			    	  
		         List<MultyParamTestHeaderDto> headerList = new  ArrayList<>();
	              for(Integer herderId : hederSq)
	                { 
	            	 
	                     List<LabResultDetailsViewDto> resDtoList = new  ArrayList<>();
	                     MultyParamTestHeaderDto headerDto = new MultyParamTestHeaderDto();
				    	  for (LabResultDetailsViewDto dto : listResult) 
				    	  {
				    		  if(herderId.equals(dto.getHeaderId())) { 
				    			  
				    		  dto.setReportType(testDto.getReportType());
				    		  headerDto.setHeaderId(dto.getHeaderId());
				    		  headerDto.setHeaderDesc(dto.getHeaderDesc());
				    		  headerDto.setHeaderCode(dto.getHeaderCode());
				    		  headerDto.setTotalCount(dto.getTotalCount());
				    		  headerDto.setIsParameterCalculation(dto.getIsParameterCalculation());
				    		  
				    		  dto.setOrgId(testDto.getOrgId());
				    		  dto.setOrgUnitId(testDto.getUnitId());
				    		  if(dto.getRefrangeTypeId()==REFERENCE_VALUE)
				    		  {
				    			  
				    			  LabResultDetailsViewDto numberRef= new  LabResultDetailsViewDto();
				    			  
				    				
				    				  
				    				  
				    				  if(dto.getAgeGroupReqiured()!= null && dto.getGenderReqiured()!= null && dto.getAgeGroupReqiured() == 'Y' && dto.getGenderReqiured() == 'Y'){
				    					 
				    					  String query=sessionFactory.getCurrentSession().getNamedQuery("GET_NUMERIC_REF_RANGE_BY_PARAMETER").getQueryString();
					    				  StringBuffer subQuery = new StringBuffer();
										  subQuery.append(" AND numref.gender_id = :genderId ")
												  .append(" AND age_group.age_group_status = 'A' ")
												  .append(" AND :patientAgeDays >= age_group.age_from_day ")
												  .append(" AND :patientAgeDays <= age_group.age_to_day ")
												  .append(" ORDER BY 1");
				    					  query = query.concat(subQuery.toString());
			  								numberRef = (LabResultDetailsViewDto)sessionFactory.getCurrentSession().createSQLQuery(query)
					    								.setParameter("genderId", dto.getGenderId())
														.setParameter("patientAgeDays", bioChemParamDto.getPatientAgeDays())
														.setParameter("parameterId", dto.getParameterId())
					    								.setMaxResults(1)
					    								.setResultTransformer(Transformers.aliasToBean(LabResultDetailsViewDto.class))
					    								.uniqueResult();
				    					  
				    					  
				    				  }else  if(dto.getAgeGroupReqiured()!= null &&  dto.getAgeGroupReqiured() == 'Y'){

				    					  String query=sessionFactory.getCurrentSession().getNamedQuery("GET_NUMERIC_REF_RANGE_BY_PARAMETER").getQueryString();
					    				  StringBuffer subQuery = new StringBuffer();
										  subQuery.append(" AND age_group.age_group_status = 'A' ")
												  .append(" AND :patientAgeDays >= age_group.age_from_day ")
												  .append(" AND :patientAgeDays <= age_group.age_to_day ")
												  .append(" ORDER BY 1");
				    					  
				    					  query = query.concat(subQuery.toString());
			  								numberRef = (LabResultDetailsViewDto)sessionFactory.getCurrentSession().createSQLQuery(query)
					    								.setParameter("parameterId", dto.getParameterId())
														.setParameter("patientAgeDays", bioChemParamDto.getPatientAgeDays())
					    								.setMaxResults(1)
					    								.setResultTransformer(Transformers.aliasToBean(LabResultDetailsViewDto.class))
					    								.uniqueResult();
				    					  
				    				  }else  if( dto.getGenderReqiured()!= null && dto.getGenderReqiured() == 'Y'){
				    					  
				    					  String query=sessionFactory.getCurrentSession().getNamedQuery("GET_NUMERIC_REF_RANGE_BY_PARAMETER_WITHOUT_INNER").getQueryString();
					    				  StringBuffer subQuery = new StringBuffer(); 
				    					  subQuery.append("	AND numref.gender_id =:genderId "
		    					    		+" order by 1 " );
				    					  query = query.concat(subQuery.toString());
			  								numberRef = (LabResultDetailsViewDto)sessionFactory.getCurrentSession().createSQLQuery(query)
					    								.setParameter("parameterId", dto.getParameterId())
														.setParameter("genderId", dto.getGenderId())
					    								.setMaxResults(1)
					    								.setResultTransformer(Transformers.aliasToBean(LabResultDetailsViewDto.class))
					    								.uniqueResult();
				    					  
				    				  }else {
				    					  String query=sessionFactory.getCurrentSession().getNamedQuery("GET_NUMERIC_REF_RANGE_BY_PARAMETER_WITHOUT_INNER").getQueryString();
					    				  StringBuffer subQuery = new StringBuffer(); 
				    					  subQuery.append("	order by 1" );
				    					  query = query.concat(subQuery.toString());
			  								numberRef = (LabResultDetailsViewDto)sessionFactory.getCurrentSession().createSQLQuery(query)
					    								.setParameter("parameterId", dto.getParameterId())
					    								.setMaxResults(1)
					    								.setResultTransformer(Transformers.aliasToBean(LabResultDetailsViewDto.class))
					    								.uniqueResult();
				    				  }
					  					
				  							
				    			
				    			   
				    			  if(numberRef != null) 
                                 {
				    				  dto.setParamAbnrmlMax(numberRef.getParamAbnrmlMax());
					    			  dto.setParamAbnrmlMin(numberRef.getParamAbnrmlMin());
					    			  dto.setParameterMax(numberRef.getParameterMax());
					    			  dto.setParameterMin(numberRef.getParameterMin());
					    			  dto.setOriginalParameterMax(numberRef.getParameterMax());
					    			  dto.setOriginalParameterMin(numberRef.getParameterMin());
					    			  dto.setOriginalParamAbnrmlMin(numberRef.getParamAbnrmlMin());
					    			  dto.setOriginalParamAbnrmlMax(numberRef.getParamAbnrmlMax());
					    			  dto.setDefaultParamUnit(dto.getParamUnit());
					    			  dto.setRemarks(numberRef.getRemarks());
                                 }
                                 else {
                                   return new Response(ERROR, ERR_500,  "Invalid Age Group Mapping  For "+dto.getParamName()+" Parameter", null, null);
                                 }
				    			 
				    		  }
				    		  else if(dto.getRefrangeTypeId()==TEXTUAL_RANGE)
				    		  {
				    			  
				    			  LabResultDetailsViewDto texRenge= new  LabResultDetailsViewDto();
				    			
			    				  
			    				  if(dto.getAgeGroupReqiured()!= null && dto.getGenderReqiured()!= null && dto.getAgeGroupReqiured() == 'Y' && dto.getGenderReqiured() == 'Y'){
			    					
			    					  String query=sessionFactory.getCurrentSession().getNamedQuery("GET_TEXTAUL_REF_RANGE_BY_PARAMETER").getQueryString();
				    				  StringBuffer subQuery = new StringBuffer();
									  subQuery.append(" AND txt_range.gender_id = :genderId ")
											  .append(" AND age_group.age_group_status = 'A' ")
											  .append(" AND :patientAgeDays >= age_group.age_from_day ")
											  .append(" AND :patientAgeDays <= age_group.age_to_day ")
											  .append(" ORDER BY 1");
			    					  query = query.concat(subQuery.toString());
			  							texRenge = (LabResultDetailsViewDto)sessionFactory.getCurrentSession().createSQLQuery(query)
					    								.setParameter("parameterId", dto.getParameterId())
														.setParameter("genderId", dto.getGenderId())
														.setParameter("patientAgeDays", bioChemParamDto.getPatientAgeDays())
												.setMaxResults(1)
					    								.setResultTransformer(Transformers.aliasToBean(LabResultDetailsViewDto.class))
					    								.uniqueResult();
			    					  
			    				  }else  if(dto.getAgeGroupReqiured()!= null &&  dto.getAgeGroupReqiured() == 'Y'){
			    					  
			    					  String query=sessionFactory.getCurrentSession().getNamedQuery("GET_TEXTAUL_REF_RANGE_BY_PARAMETER").getQueryString();
				    				  StringBuffer subQuery = new StringBuffer();
									  subQuery.append(" AND age_group.age_group_status = 'A' ")
											  .append(" AND :patientAgeDays >= age_group.age_from_day ")
											  .append(" AND :patientAgeDays <= age_group.age_to_day ")
											  .append(" ORDER BY 1");
			    					  query = query.concat(subQuery.toString());
			  							texRenge = (LabResultDetailsViewDto)sessionFactory.getCurrentSession().createSQLQuery(query)
					    								.setParameter("parameterId", dto.getParameterId())
														.setParameter("patientAgeDays", bioChemParamDto.getPatientAgeDays())
					    								.setMaxResults(1)
					    								.setResultTransformer(Transformers.aliasToBean(LabResultDetailsViewDto.class))
					    								.uniqueResult();
			    					  
			    				  }else  if( dto.getGenderReqiured()!= null && dto.getGenderReqiured() == 'Y'){
			    					  
			    					  String query=sessionFactory.getCurrentSession().getNamedQuery("GET_TEXTAUL_REF_RANGE_BY_PARAMETER_WITHOUT_INNER").getQueryString();
				    				  StringBuffer subQuery = new StringBuffer(); 
			    					  subQuery.append("	AND txt_range.gender_id = "+dto.getGenderId()
	    					    		+"order by 1" );
			    					  query = query.concat(subQuery.toString());
			  							texRenge = (LabResultDetailsViewDto)sessionFactory.getCurrentSession().createSQLQuery(query)
					    								.setParameter("parameterId", dto.getParameterId())
					    								.setMaxResults(1)
					    								.setResultTransformer(Transformers.aliasToBean(LabResultDetailsViewDto.class))
					    								.uniqueResult();
			    					  
			    				  }else {
			    					  
			    					  String query=sessionFactory.getCurrentSession().getNamedQuery("GET_TEXTAUL_REF_RANGE_BY_PARAMETER_WITHOUT_INNER").getQueryString();
				    				  StringBuffer subQuery = new StringBuffer(); 
			    					  subQuery.append("	order by 1" );
			    					  query = query.concat(subQuery.toString());
			  							texRenge = (LabResultDetailsViewDto)sessionFactory.getCurrentSession().createSQLQuery(query)
					    								.setParameter("parameterId", dto.getParameterId())
					    								.setMaxResults(1)
					    								.setResultTransformer(Transformers.aliasToBean(LabResultDetailsViewDto.class))
					    								.uniqueResult();
			    					  
			    					  
			    				  }
				  					
			  						
				    			  if(texRenge != null) 
                                 {
				    				  dto.setTextualRangeId(texRenge.getTextualRangeId());
					    			  dto.setTextualRangeName(texRenge.getTextualRangeName());
					    			  dto.setOriginalTextualRangeName(texRenge.getTextualRangeName());
					    			  dto.setDefaultParamUnit(dto.getParamUnit());
					    			  dto.setRemarks(texRenge.getRemarks());
                                 }
                                 else {
                                   return new Response(ERROR, ERR_500,  "Invalid Age Group Mapping  For "+dto.getParamName()+" Parameter", null, null);
                                 }
				    			  
				    			  
				    		  }
				    		  else if(dto.getRefrangeTypeId()==MULTIVALUED_RANGE){
				    			  
				    			  
				    			  LabResultDetailsViewDto mulTexRange= new  LabResultDetailsViewDto();
				    			  
			    				  
			    				  if(dto.getAgeGroupReqiured()!= null && dto.getGenderReqiured()!= null && dto.getAgeGroupReqiured() == 'Y' && dto.getGenderReqiured() == 'Y'){
			    					 
			    					  String query=sessionFactory.getCurrentSession().getNamedQuery("GET_MULTEXTUAL_REF_RANGE_BY_PARAMETER").getQueryString();
				    				  StringBuffer subQuery = new StringBuffer();
									  subQuery.append(" AND mulref.gender_id = :genderId ")
											  .append(" AND age_group.age_group_status = 'A' ")
											  .append(" AND :patientAgeDays >= age_group.age_from_day ")
											  .append(" AND :patientAgeDays <= age_group.age_to_day ")
											  .append(" ORDER BY 1");
			    						query = query.concat(subQuery.toString());
			  							mulTexRange = (LabResultDetailsViewDto)sessionFactory.getCurrentSession().createSQLQuery(query)
					    								.setParameter("parameterId", dto.getParameterId())
														.setParameter("genderId", dto.getGenderId())
														.setParameter("patientAgeDays", bioChemParamDto.getPatientAgeDays())
					    								.setMaxResults(1)
					    								.setResultTransformer(Transformers.aliasToBean(LabResultDetailsViewDto.class))
					    								.uniqueResult();
			    				  }else  if(dto.getAgeGroupReqiured()!= null &&  dto.getAgeGroupReqiured() == 'Y'){
			    					 
			    					  String query=sessionFactory.getCurrentSession().getNamedQuery("GET_MULTEXTUAL_REF_RANGE_BY_PARAMETER").getQueryString();
				    				  StringBuffer subQuery = new StringBuffer();
									  subQuery.append(" AND age_group.age_group_status = 'A' ")
											  .append(" AND :patientAgeDays >= age_group.age_from_day ")
											  .append(" AND :patientAgeDays <= age_group.age_to_day ")
											  .append(" ORDER BY 1");

									  query = query.concat(subQuery.toString());
			  							mulTexRange = (LabResultDetailsViewDto)sessionFactory.getCurrentSession().createSQLQuery(query)
					    								.setParameter("parameterId", dto.getParameterId())
														.setParameter("patientAgeDays", bioChemParamDto.getPatientAgeDays())
					    								.setMaxResults(1)
					    								.setResultTransformer(Transformers.aliasToBean(LabResultDetailsViewDto.class))
					    								.uniqueResult();
			    				  }else  if( dto.getGenderReqiured()!= null && dto.getGenderReqiured() == 'Y'){
			    					
			    					  String query=sessionFactory.getCurrentSession().getNamedQuery("GET_MULTEXTUAL_REF_RANGE_BY_PARAMETER_WITHOUT_INNER").getQueryString();
				    				  StringBuffer subQuery = new StringBuffer(); 
			    					  subQuery.append("	AND mulref.gender_id =:genderId "
	    					    		+" order by 1 " );
			    						query = query.concat(subQuery.toString());
			  							mulTexRange = (LabResultDetailsViewDto)sessionFactory.getCurrentSession().createSQLQuery(query)
					    								.setParameter("parameterId", dto.getParameterId())
														.setParameter("genderId", dto.getGenderId())
					    								.setMaxResults(1)
					    								.setResultTransformer(Transformers.aliasToBean(LabResultDetailsViewDto.class))
					    								.uniqueResult();
			    				  }else {
			    					  
			    					  String query=sessionFactory.getCurrentSession().getNamedQuery("GET_MULTEXTUAL_REF_RANGE_BY_PARAMETER_WITHOUT_INNER").getQueryString();
				    				  StringBuffer subQuery = new StringBuffer();
			    					  subQuery.append("	order by 1" );
			    						query = query.concat(subQuery.toString());
			  							mulTexRange = (LabResultDetailsViewDto)sessionFactory.getCurrentSession().createSQLQuery(query)
					    								.setParameter("parameterId", dto.getParameterId())
					    								.setMaxResults(1)
					    								.setResultTransformer(Transformers.aliasToBean(LabResultDetailsViewDto.class))
					    								.uniqueResult();
			    				  }
				  					
			  						
				    			  if(mulTexRange != null) 
                                 {
				    				  dto.setMultitextaulRange(mulTexRange.getMultitextaulRange());
				    				  dto.setOriginalMultitextaulRange(mulTexRange.getMultitextaulRange());
				    				  dto.setDefaultParamUnit(dto.getParamUnit());
				    				  dto.setRemarks(mulTexRange.getRemarks());
                                 }
                                 else {
                                   return new Response(ERROR, ERR_500,  "Invalid Age Group Mapping  For "+dto.getParamName()+" Parameter", null, null);
                                 }
				    			  
				    			  
				    			 
				    		  }
				    		  
				    		  if(dto.getIsFormula()!=null && dto.getIsFormula()=='Y')
				    		  {
				    			  Response formula =   iFormulaMasterDao.getFormulaByParameter(dto.getParameterId(), bioChemParamDto.getOrgId(), bioChemParamDto.getOrgUnitId());
                                 if(formula.getListObject()!=null && formula.getListObject().size()>0) 
                                 {
                               	  dto.setListFormulaDetails(formula.getListObject());
                                 }
                                 else {
                                   return new Response(ERROR, ERR_500,  " Formula Not Found For "+dto.getParamName()+" Parameter", null, null);
                                 }
				    		  }
				    		  
				    		 if(dto.getRefrangeTypeId()==MULTIVALUED_RANGE || dto.getRefrangeTypeId()==TEXTUAL_RANGE){
				    			 
				    			 Response helpRes = iParameterMasterDao.getHelpValueByParameter(bioChemParamDto.getOrgId(),  bioChemParamDto.getOrgUnitId(), dto.getParameterId());
					    		 if(helpRes.getListObject()!=null && helpRes.getListObject().size()>0) {
					    			 List<HelpValueMasterDto> listHelpValueMasterDto = helpRes.getListObject();
					    			 dto.setListHelpValueMaster(listHelpValueMasterDto);
					    		 }
				    		 } 
				    		
				    		  resDtoList.add(dto);
				    		  
				    	  }
						   }
				    	  headerDto.setListLabResultDetailsViewDto(resDtoList);
				    	  headerList.add(headerDto);
	               }
		         
		         
		              testDto.setListMultyParamTestHeaderDto(headerList);
			      }
			      else {
						return new Response(ERROR, ERR_500, "Parameter Not Mapped For This Test ...", null, null);
					}
			      
			}else {
				return new Response(ERROR, ERR_500, "Parameter Not Mapped For This Test ...", null, null);
			}
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, testDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getMultyParameterTestDetails(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {

		     SingParamTestResDto testDto =
			(SingParamTestResDto) sessionFactory.getCurrentSession().getNamedQuery("GET_TEST_FOR_RESULT_FOR_VALIDATION_AND_RELEASE")
						.setParameter("labSampleDtlsId", bioChemParamDto.getLabSampleDtlsId())
						.setParameter("testId", bioChemParamDto.getTestId())
						.setParameter("labTestResId", bioChemParamDto.getLabResultId())
						.setResultTransformer(Transformers.aliasToBean(SingParamTestResDto.class))
						.uniqueResult();
				
				if (testDto != null) 
				{
					
				      Response<LabResultDetailsViewDto, Integer> validationRes = iBiochemValidationWorklistDao.getValidationWorkListDetails(bioChemParamDto);
				      List<LabResultDetailsViewDto> listResult =validationRes.getListObject();
				      if(listResult!=null && listResult.size()>0)
				      {
				    	  
				    	  
	                      List<Integer> hederSq  = new ArrayList<>();
	                      for (LabResultDetailsViewDto labResultDetailsViewDto : listResult) {
	                        if(hederSq.size() == 0 ) {
	                            hederSq.add(labResultDetailsViewDto.getHeaderId());
	                        }else {
	                            int count = 0 ;
	                            for (Integer herderId : hederSq) {
	                                if(herderId.equals(labResultDetailsViewDto.getHeaderId())) {
	                                    count = 1;
	                                }
	                            }
	                            
	                            if(count == 0 ) {
	                                hederSq.add(labResultDetailsViewDto.getHeaderId());
	                            }
	                        }
	                    }
				    	  
	                   /*   List<Integer> uniqueHeaderList = new ArrayList<Integer>();
	                      int previourHdr = 0;
	                      for (LabResultDetailsViewDto labResultDetailsViewDto : listResult) {
	                         if(previourHdr == 0) {
	                             uniqueHeaderList.add(labResultDetailsViewDto.getHeaderId());
	                             previourHdr = labResultDetailsViewDto.getHeaderId();
	                         }else {
	                             if(previourHdr != labResultDetailsViewDto.getHeaderId()) {
	                                 uniqueHeaderList.add(labResultDetailsViewDto.getHeaderId());
	                                 previourHdr = labResultDetailsViewDto.getHeaderId();
	                             }
	                         }
	                      }*/
				    	  
			              /* Map<Integer, List<LabResultDetailsViewDto>> sortedByHeader =  listResult
																			                .stream()
																			                .collect(Collectors.groupingBy(LabResultDetailsViewDto::getHeaderId));
			            */
			               List<MultyParamTestHeaderDto> headerList = new  ArrayList<>();
			             // for(Map.Entry<Integer, List<LabResultDetailsViewDto>> entry : sortedByHeader.entrySet())
		                    /*  for(Integer herderId : uniqueHeaderList)
			                { 
			            	   
			                     List<LabResultDetailsViewDto> resDtoList = new  ArrayList<>();
			                     MultyParamTestHeaderDto headerDto = new MultyParamTestHeaderDto();
	                             Boolean firstFind = false;
						    	  for (LabResultDetailsViewDto dto : listResult) 
						    	  {
						    		  
	                                  Boolean isParameterExist = false;
	                                  if(headerList.size()>0) {
	                                      List<MultyParamTestHeaderDto> filteredHeaderList  = headerList.stream().filter(obj->obj.getHeaderId()==dto.getHeaderId()).collect(Collectors.toList());
	                                
	                                      if(filteredHeaderList!=null) {
	                                          for (MultyParamTestHeaderDto multyParamTestHeaderDto : filteredHeaderList) {
	                                            if(multyParamTestHeaderDto.getListLabResultDetailsViewDto()!=null && multyParamTestHeaderDto.getListLabResultDetailsViewDto().size()>0) {
	                                                
	                                                 List<LabResultDetailsViewDto> filteredParamList  = multyParamTestHeaderDto.getListLabResultDetailsViewDto().stream().filter(obj->obj.getParameterId()==dto.getParameterId()).collect(Collectors.toList());
	                                                 if(filteredParamList!=null && filteredParamList.size()>0) {
	                                                     isParameterExist = true;
	                                                     break;
	                                                 }
	                                            }
	                                        }
	                                      }
	                                  }
	                                  if(herderId.equals(dto.getHeaderId()) && !isParameterExist) { 
	                                      firstFind = true;
							    		  headerDto.setHeaderId(dto.getHeaderId());
							    		  headerDto.setHeaderDesc(dto.getHeaderDesc());
							    		  headerDto.setHeaderCode(dto.getHeaderCode());
							    		  headerDto.setIsParameterCalculation(dto.getIsParameterCalculation());
							    		  headerDto.setTotalCount(dto.getTotalCount());
							    		  dto.setOrgId(testDto.getOrgId());
							    		  dto.setOrgUnitId(testDto.getUnitId());
							    		  if(dto.getIsFormula()!=null && dto.getIsFormula()=='Y')
							    		  {
							    			  Response formula =   iFormulaMasterDao.getFormulaByParameter(dto.getParameterId(), bioChemParamDto.getOrgId(), bioChemParamDto.getOrgUnitId());
			                                  if(formula.getListObject()!=null && formula.getListObject().size()>0) 
			                                  {
			                                	  dto.setListFormulaDetails(formula.getListObject());
			                                  }
			                                  else {
			                                    return new Response(ERROR, ERR_500, "Formula Not Found..", null, null);
			                                  }
							    		  }
							    		  resDtoList.add(dto);
							    	  }else if(firstFind == true){
		                                  break;
		                              }
								   }
						    	  headerDto.setListLabResultDetailsViewDto(resDtoList);
						    	  headerList.add(headerDto);
			               }*/
			               
			               
			               for(Integer herderId : hederSq)
			                { 
			            	   
			                     List<LabResultDetailsViewDto> resDtoList = new  ArrayList<>();
			                     MultyParamTestHeaderDto headerDto = new MultyParamTestHeaderDto();
						    	  for (LabResultDetailsViewDto dto : listResult) 
						    	  {
	                                  if(herderId.equals(dto.getHeaderId())) { 
							    		  headerDto.setHeaderId(dto.getHeaderId());
							    		  headerDto.setHeaderDesc(dto.getHeaderDesc());
							    		  headerDto.setHeaderCode(dto.getHeaderCode());
							    		  headerDto.setIsParameterCalculation(dto.getIsParameterCalculation());
							    		  headerDto.setTotalCount(dto.getTotalCount());
							    		  dto.setOrgId(testDto.getOrgId());
							    		  dto.setOrgUnitId(testDto.getUnitId());
							    		  if(dto.getIsFormula()!=null && dto.getIsFormula()=='Y')
							    		  {
							    			  Response formula =   iFormulaMasterDao.getFormulaByParameter(dto.getParameterId(), bioChemParamDto.getOrgId(), bioChemParamDto.getOrgUnitId());
			                                  if(formula.getListObject()!=null && formula.getListObject().size()>0) 
			                                  {
			                                	  dto.setListFormulaDetails(formula.getListObject());
			                                  }
			                                  else {
			                                    return new Response(ERROR, ERR_500, "Formula Not Found..", null, null);
			                                  }
							    		  }
							    		  resDtoList.add(dto);
							    	  }
								   }
						    	  headerDto.setListLabResultDetailsViewDto(resDtoList);
						    	  headerList.add(headerDto);
			               }
			              testDto.setListMultyParamTestHeaderDto(headerList);
				      }
				      
				}
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, testDto);
			} catch (Exception e) {
				logger.error("Exception", e);
				throw e;
			}
	}

	@Override
	public Response saveReleaseEntryDetails(LabResultMasterDto labResultMasterDto)
			throws ApplicationException {

	    try {
	        Long currentTimeWithZone  = TimeZoneComponent.getDateByzone(labResultMasterDto.getOrgUnitId()).getTime();
	        LabResultMaster labResultMaster = mapper.map(labResultMasterDto, LabResultMaster.class,
	            "LabResultMasterDtoToLabResultMaster");
	        labResultMaster.setResultEnterBy(labResultMasterDto.getCreatedBy());
	        labResultMaster.setCreatedDate(currentTimeWithZone);
	        labResultMaster.setCreatedBy(labResultMasterDto.getCreatedBy());
	        labResultMaster.setResultEnterDatetime(currentTimeWithZone);
	        labResultMaster.setResultValidatedDatetime(currentTimeWithZone);
	        labResultMaster.setResultValidatedBy(labResultMasterDto.getCreatedBy());
	        labResultMaster.setResultAuthorisedBy(labResultMasterDto.getCreatedBy());
	        labResultMaster.setResultAuthorisedDatetime(currentTimeWithZone);
	        labResultMaster.setManualResultEnterTime(currentTimeWithZone);
	        labResultMaster.setManualValidationTime(currentTimeWithZone);
	        labResultMaster.setManualReleaseTime(currentTimeWithZone);
	        labResultMaster.setReportType(labResultMasterDto.getListLabSampleResultDetailsMaster().getFirst().getReportType() != null ? labResultMasterDto.getListLabSampleResultDetailsMaster().getFirst().getReportType(): labResultMasterDto.getReportType());
	        labResultMaster.setResultAuthorisedFlag('Y');
	        labResultMaster.setSampleStatusId(8);
	        labResultMaster.setStatus('A');
	        LabResultMaster labResultMst = save(labResultMaster);
	        
	        LabSampleDetailsMaster labSampleDetailsMaster =
	            (LabSampleDetailsMaster) sessionFactory.getCurrentSession()
	                .get(LabSampleDetailsMaster.class, labResultMst.getLabSampleDtlsId());
	        labSampleDetailsMaster.setUpdatedBy(labResultMst.getUpdatedBy());
	        labSampleDetailsMaster.setUpdatedDate(currentTimeWithZone);
	        labSampleDetailsMaster.setSampleStatusId(8);
	        labSampleDetailsMaster.setAssignUnit(labResultMasterDto.getAssignUnit());
	        labSampleDetailsMaster.setTestId(labResultMasterDto.getPerformTestId()!= null ? labResultMasterDto.getPerformTestId(): labResultMasterDto.getTestId());
	        labSampleDetailsMaster.setPerformTestId(labResultMasterDto.getTestId());
	        labSampleDetailsMaster.setInternalNotes(labResultMasterDto.getInternalNotes());
	        labSampleDetailsMaster.setIsOnHoldParameter(labResultMasterDto.getIsOnHoldParameter());
	        labSampleDetailsMaster.setSrfId(labResultMasterDto.getSrfId());
			labSampleDetailsMaster.setIcmrId(labResultMasterDto.getIcmrId());
	        sessionFactory.getCurrentSession().update(labSampleDetailsMaster);
	        
	        for (LabResultDetailsMaster labResultDetailsMaster : labResultMst.getListLabSampleResultDetailsMaster()) {
				
	          labResultDetailsMaster.setLabResultId(labResultMst.getLabTestResId());
	          labResultDetailsMaster.setFirstLevelResult(labResultDetailsMaster.getFinalResult());
	          labResultDetailsMaster.setSecondLevelResult(labResultDetailsMaster.getFinalResult());
	          labResultDetailsMaster.setThirdLevelResult(labResultDetailsMaster.getFinalResult());
	          labResultDetailsMaster.setCreatedBy(labResultMasterDto.getCreatedBy());
	          labResultDetailsMaster.setCreatedDate(currentTimeWithZone);
	          labResultDetailsMaster.setRemarks(labResultDetailsMaster.getRemarks());
	          sessionFactory.getCurrentSession().save(labResultDetailsMaster);
	        }

	        labResultMst.setLabSampleDetailsMaster(labSampleDetailsMaster);
	      

	      return new Response(SUCCESS, SUCCESS_200, SAMPLE_RESULT_ENTRY_SUCC, null, labResultMst);
	    } catch (Exception e) {
	      logger.error("Exception", e);
	      return new Response(ERROR, ERR_500, SAMPLE_RESULT_ENTRY_FAIL, null, null);
	    }
	}

	@Override
	public Response consolidatedResultEntrySearch(SearchDto searchDto) throws ApplicationException {
		try {
			
		    Response resp = iLabTransactionDao.getSpecialityAndSubSpecialityListByUserIdByUnitId(searchDto);
	        List<Integer> deptIds = new ArrayList<>();
	        List<Integer> subDeptIds = new ArrayList<>();
	        List<SearchDto> specialityList = new ArrayList<>();
	        List<SearchDto> subSpecialityList = new ArrayList<>();
			List<Integer>  testTypes = new ArrayList<Integer>();
			List<Integer>  visitTypes = new ArrayList<Integer>();


			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0) {
				visitTypes = searchDto.getVisitTypes().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());
			}

			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0) {
				testTypes = searchDto.getTestTypes().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());
			}

	        if(resp.getListObject() != NULL && resp.getListObject().size() > 0 )  {
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
			
			Integer serologyId = 0;
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
			
			List<SearchDto> filteredList = subSpecialityList.stream()
					.filter(bill -> bill.getSubSpecialityMasterId().equals(dto.getSerologyId())).collect(Collectors.toList());
			List<LabResultMasterDto> list =  new ArrayList<>();
			 Integer countSero = 0;
			 //if(searchDto.getIsOnHoldParameter() == false) {
			 if(dto.getSerologyId() != null && dto.getSerologyId() != 0 && (filteredList!= null && filteredList.size() > 0 && subSpecialityList.size() > 0  || (subSpecialityList.size() == 0)) && isSerologyIdPresent==true){
				
				 String query1 = sessionFactory.getCurrentSession().getNamedQuery("GET_ALL_SERO_RESULT_ENTRY_LIST").getQueryString().toString();
				 
				      StringBuilder sb1 = ganarateQuery(query1,searchDto);
				      
				      Query createdQuery1 = sessionFactory.getCurrentSession().createSQLQuery(sb1.toString());
				      
				      
				      
				     /* if (searchDto.getSearchKeyword() != null && !searchDto.getSearchKeyword().isEmpty()) {
				    	    sb1.append(" AND ( "
				    	            + " lbsampledtls.sub_dept_id = " + searchDto.getSearchKeyword()
								//	+ " AND lower(subspe_mst.sub_speciality_name) LIKE concat('%',LOWER('"+searchDto.getSearchField()+ "%'"

				    	            + " ) ");
				    	}
*/
				      
				  
				      List<LabResultMasterDto> listSero  = createdQuery1
								.setParameter("orgId", searchDto.getOrgId())
								.setParameter("orgUnitId", searchDto.getOrgUnitId())
								.setParameterList("deptId", deptIds)
								.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
								.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
								.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
				      
				      String querySer = sessionFactory.getCurrentSession().getNamedQuery("GET_ALL_SERO_RESULT_ENTRY_LIST_COUNT").getQueryString().toString();
				      
				      StringBuilder sbSer = ganarateQuery(querySer,searchDto);
				      
				      Query createdQuerySer = sessionFactory.getCurrentSession().createSQLQuery(sbSer.toString());
				      List<LabResultMasterDto> listcount  = createdQuerySer
								.setParameter("orgId", searchDto.getOrgId())
								.setParameter("orgUnitId", searchDto.getOrgUnitId())
								.setParameterList("deptId", deptIds)
								.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
				      
				      if(listSero != null && listSero.size()>0) {
				    	  countSero = listcount.size();
				    	  list.addAll(listSero);
				      }
		   		}

			 if(searchDto.getSubDepts().size()==1 && isSerologyIdPresent == true){
				 return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, list, list.size());
			 }
			
		      String query = sessionFactory.getCurrentSession()
		          .getNamedQuery("GET_ALL_SUBDEPTS_RESULT_ENTRY_LIST").getQueryString().toString();

		      searchDto.setSeroDeptId(dto.getSerologyId() != null ? dto.getSerologyId(): null);
		      StringBuilder sb = ganarateAllSubDeptQuery(query,searchDto);
		      
		      Query createdQuery = sessionFactory.getCurrentSession()
		          .createSQLQuery(sb.toString());
			List<Integer> statusIds = new ArrayList<Integer>();
			List<Integer> subSpeciIds = new ArrayList<Integer>();

			if(searchDto.getIsOnHoldParameter() == false) {

				if(searchDto.getIsSampleAcceptanceApplicable() == 'Y') {

					statusIds = Arrays.asList(SAMPLE_SEND_TO_DEPT,RETESTING);

				}else if (searchDto.getIsSendToCrApplicable() != null && searchDto.getIsSendToCrApplicable() == 'Y') {

					statusIds =Arrays.asList(SAMPLE_SEND_TO_CR,RETESTING);

				}else {

					statusIds = Arrays.asList(SAMPLE_COLLECTED,RETESTING);
				}
			}

			if (searchDto.getSubDeptIds() != null  && searchDto.getSubDeptIds().size() > 0) {
				subSpeciIds = searchDto.getSubDeptIds();
				createdQuery.setParameterList("subDeptIds", subSpeciIds);

			}

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
			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0 )

				if(searchDto.getIsAcceptanceInDepartment() == 'Y') {

					List<obj> withoutSerosubDepts = new ArrayList<>();
					String sero = "Serology";
					for (obj lan : searchDto.getSubDepts()) {

						if(lan.getLabel()==null ) {
							if(searchDto.getDepartmentList()!=null) {

								obj newDto = searchDto.getDepartmentList().stream()
										.filter(ob -> ob.getId().equals(lan.getId()))
										.findAny()
										.orElse(null);
								if(newDto!=null) {
									lan.setLabel(newDto.getLabel());
								}
							}
						}

						if(lan.getLabel()!=null && !(lan.getLabel().toLowerCase()).equals(sero.toLowerCase()) && searchDto.getSeroDeptId() != null) {
							withoutSerosubDepts.add(lan);
						}
					}

					if (withoutSerosubDepts != null && withoutSerosubDepts.size() > 0) {
						List<Integer>  withoutSerosubDeptsInt = new ArrayList<Integer>();
						withoutSerosubDeptsInt = withoutSerosubDepts.stream()
								.map(obj -> obj.getId())
								.collect(Collectors.toList());

						createdQuery.setParameterList("withoutSerosubDepts", withoutSerosubDeptsInt);
					}

				}else {
					List<Integer>  subDepts = new ArrayList<Integer>();
					subDepts = searchDto.getSubDepts().stream()
							.map(obj -> obj.getId())
							.collect(Collectors.toList());
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

			if(searchDto.getUhidNumber()!="") {
				createdQuery.setParameter("uhidNumber", searchDto.getUhidNumber());
			}

			if(searchDto.getSampleNo()!="") {
				createdQuery.setParameter("sampleNo", searchDto.getSampleNo());
			}

			if (searchDto.getIsOnHoldParameter() == true) {
				statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
				createdQuery.setParameterList("statusIds", statusIds);
				createdQuery.setParameter("isOnHoldParameter", searchDto.getIsOnHoldParameter());
			}
			if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
				createdQuery.setParameterList("statusIds", statusIds);
			}
		       List<LabResultMasterDto> listall  = createdQuery
						.setParameter("orgId", searchDto.getOrgId())
						.setParameter("orgUnitId", searchDto.getOrgUnitId())
//						.setParameterList("sampleStatusId", statusIds)
						//.setParameter("deptId", searchDto.getDeptId())
						.setParameterList("deptId", deptIds)
						.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
						.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
						.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();

		       
		       String query2 = sessionFactory.getCurrentSession()
				          .getNamedQuery("GET_ALL_SUBDEPTS_RESULT_ENTRY_LIST_COUNT").getQueryString().toString();

		       StringBuilder  sb2 = ganarateAllSubDeptQuery(query2,searchDto);
		       
		       Query createdQuery2 = sessionFactory.getCurrentSession()
				          .createSQLQuery(sb2.toString());
			if (searchDto.getSubDeptIds() != null  && searchDto.getSubDeptIds().size() > 0) {
				subSpeciIds = searchDto.getSubDeptIds();
				createdQuery2.setParameterList("subDeptIds", subSpeciIds);

			}

			if (searchDto.getFromDt() != null ) {
				createdQuery2.setParameter("fromDt", searchDto.getFromDt());
			}

			if (searchDto.getToDt() != null ) {
				createdQuery2.setParameter("toDt", searchDto.getToDt());
			}

			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
				createdQuery2.setParameter("patientId", searchDto.getPatientId());
			}

			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0){
				createdQuery2.setParameterList("visitTypes", visitTypes);
			}
			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0 )

				if(searchDto.getIsAcceptanceInDepartment() == 'Y') {

					List<obj> withoutSerosubDepts = new ArrayList<>();
					String sero = "Serology";
					for (obj lan : searchDto.getSubDepts()) {

						if(lan.getLabel()==null ) {
							if(searchDto.getDepartmentList()!=null) {

								obj newDto = searchDto.getDepartmentList().stream()
										.filter(ob -> ob.getId().equals(lan.getId()))
										.findAny()
										.orElse(null);
								if(newDto!=null) {
									lan.setLabel(newDto.getLabel());
								}
							}
						}

						if(lan.getLabel()!=null && !(lan.getLabel().toLowerCase()).equals(sero.toLowerCase()) && searchDto.getSeroDeptId() != null) {
							withoutSerosubDepts.add(lan);
						}
					}

					if (withoutSerosubDepts != null && withoutSerosubDepts.size() > 0) {
						List<Integer>  withoutSerosubDeptsInt = new ArrayList<Integer>();
						withoutSerosubDeptsInt = withoutSerosubDepts.stream()
								.map(obj -> obj.getId())
								.collect(Collectors.toList());

						createdQuery2.setParameterList("withoutSerosubDepts", withoutSerosubDeptsInt);
					}

				}else {
					List<Integer>  subDepts = new ArrayList<Integer>();
					subDepts = searchDto.getSubDepts().stream()
							.map(obj -> obj.getId())
							.collect(Collectors.toList());
					createdQuery2.setParameterList("subDepts", subDepts);
				}

			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size() > 0){
				createdQuery2.setParameterList("testTypes", testTypes);
			}

			if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0){
				createdQuery2.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId());
			}

			if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0){
				createdQuery2.setParameter("assignUnit", searchDto.getAssignUnit());
			}

			if(searchDto.getUhidNumber()!="") {
				createdQuery2.setParameter("uhidNumber", searchDto.getUhidNumber());
			}

			if(searchDto.getSampleNo()!="") {
				createdQuery2.setParameter("sampleNo", searchDto.getSampleNo());
			}

			if (searchDto.getIsOnHoldParameter() == true) {
				statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
				createdQuery2.setParameterList("statusIds", statusIds);
				createdQuery2.setParameter("isOnHoldParameter", searchDto.getIsOnHoldParameter());
			}
			if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
				createdQuery2.setParameterList("statusIds", statusIds);
			}
		      List<LabResultMasterDto> list1  = createdQuery2
						.setParameter("orgId", searchDto.getOrgId())
						.setParameter("orgUnitId", searchDto.getOrgUnitId())
//						.setParameterList("sampleStatusId", statusIds)
						.setParameterList("deptId", deptIds)
						//.setParameter("deptId", searchDto.getDeptId())
						.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
		      Integer count = 0;
		      
		      
		      if(listall != null && listall.size()>0) {
		    	  list.addAll(listall);
		      }
   			
		      
		      if (list.isEmpty()) {
		    	  
		        return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.", list, count);
		        
		      } else {
		    	  
		    	  if(list1 != null && list1.size()>0) {
		    		  count = list1.size();
		    		  count = count + countSero;
		    	  }else {
		    		  count = count + countSero;
		    	  }
		    	  
		        return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, list, count);
		      }


		    } catch (Exception e) {
		      logger.error("Exection", e);
		      throw e;
		    }
	}

	private StringBuilder ganarateAllSubDeptQuery(String query, SearchDto searchDto) {
		
		
		 List<Integer> statusIds = new ArrayList<Integer>(); 
		 List<Integer> subSpeciIds = new ArrayList<Integer>(); 
	      if(searchDto.getIsOnHoldParameter() == false) {
	    	  
	       if(searchDto.getIsSampleAcceptanceApplicable() == 'Y') {
	   			
	   			statusIds = Arrays.asList(SAMPLE_SEND_TO_DEPT,RETESTING);
	   			
	   		}else if (searchDto.getIsSendToCrApplicable() != null && searchDto.getIsSendToCrApplicable() == 'Y') {
	   						
	   			statusIds =Arrays.asList(SAMPLE_SEND_TO_CR,RETESTING);
	   			
	   		}else {
	   			
	   			statusIds = Arrays.asList(SAMPLE_COLLECTED,RETESTING);
	   		}
	      }
		
		StringBuilder sb = new StringBuilder(query);
		
		 if (searchDto.getSubDeptIds() != null  && searchDto.getSubDeptIds().size() > 0) {
			 sb.append(
					 " AND (CASE WHEN tlc.consolidated_result_entry = 'N' THEN lbsampledtls.sub_dept_id NOT IN (:subDeptIds) "
							 + " ELSE (lbsampledtls.sub_dept_id NOT IN (tlc.serology_id) OR tlc.serology_id IS NULL OR tlc.serology_id = 0) END) ");
	      }else {
	    	  sb.append(
	    	  "  AND (case when tlc.consolidated_result_entry = 'N' then lbsampledtls.sub_dept_id not in (tlc.biochemistry_id ,tlc.hematology_id ,tlc.clinical_pathology_id ,tlc.serology_id) else  (lbsampledtls.sub_dept_id not in (tlc.serology_id) or tlc.serology_id is  null or tlc.serology_id =0 )  end) ");
	      }

		 if (searchDto.getFromDt() != null ) {
	    	  sb.append(
	            " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt  ");
	      }

	      if (searchDto.getToDt() != null ) {
	    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt  ");
	      }

	      if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
			  sb.append(" AND pati_mst.patient_id =:patientId  ");
		  }
	      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0) {
			  sb.append(" AND visit_type_master.visit_type_id IN (:visitTypes)");
		  }
	      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0 )
	    	  
	    	  if(searchDto.getIsAcceptanceInDepartment() == 'Y') {
	    		  
	    		  List<obj> withoutSerosubDepts = new ArrayList<>();
	    		  String sero = "Serology";
	    		  for (obj lan : searchDto.getSubDepts()) {
	    			  
	    			  if(lan.getLabel()==null ) {
	    				  if(searchDto.getDepartmentList()!=null) {
	    					  
	    					  obj newDto = searchDto.getDepartmentList().stream()
	    							  .filter(ob -> ob.getId().equals(lan.getId()))
	    							  .findAny()
	    							  .orElse(null);
	    					  if(newDto!=null) {
	    						  lan.setLabel(newDto.getLabel());
	    					  }
	    				  }
	    			  }
	    			  
	    			  if(lan.getLabel()!=null && !(lan.getLabel().toLowerCase()).equals(sero.toLowerCase()) && searchDto.getSeroDeptId() != null) {
	    				  withoutSerosubDepts.add(lan);
	    			  }
	    		  }
	    		  
	    		   if (withoutSerosubDepts != null && withoutSerosubDepts.size() > 0) {
	    			   sb.append(" AND test_mst.dept_id IN (:withoutSerosubDepts)");
	    		   }
	    		  
	    	  }else {
	    		  sb.append(" AND test_mst.dept_id IN (:subDepts)");
	    	  }
	      
	      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size() > 0 )
	    	  sb.append(" AND test_mst.test_id in (:testTypes)");
	      
	      if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0)
	    	  sb
	            .append(" AND lbsampledtls.lab_sample_dtls_id =:labSampleDtlsId ");

	      if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
	          sb.append("  and lbsampledtls.assign_unit  =:assignUnit ");
	      
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
              sb.append("  AND  (lbsampledtls.sub_dept_id not in (tlc.serology_id) or tlc.serology_id is  null or tlc.serology_id =0 ) and (lbsampledtls.sample_status_id IN(:statusIds) or lbsampledtls.is_on_hold_parameter = true ) ");
          }
	      
		if (searchDto.getLabSortingId() != null) {
			if (searchDto.getLabSortingId() == 1) {
				sb.append(" ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
			} else if (searchDto.getLabSortingId() == 2) {
				sb.append(" ORDER BY    order_master.order_id  DESC ");
			} else if (searchDto.getLabSortingId() == 3) {
				sb.append(" ORDER BY    sample_mst.sample_id  DESC ");
			} else if (searchDto.getLabSortingId() == 4) {
				sb.append(" ORDER BY    lbsampledtls.updated_date DESC ");
			}
		} else {
			sb.append(" ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
		}
	      
		return sb;
	}

	private StringBuilder ganarateQuery(String query, SearchDto searchDto) {
		
		
		 List<Integer> statusIds = new ArrayList<Integer>(); 
		 
	      if(searchDto.getIsOnHoldParameter() == false) {
	  
		      if(searchDto.getIsAcceptanceInDepartment() == 'Y') {
		   			
		   			statusIds = bioChemStatus;
		   			
		   		}else if(searchDto.getIsSampleAcceptanceApplicable() == 'Y') {
		   			
		   			statusIds = Arrays.asList(SAMPLE_SEND_TO_DEPT,RETESTING);
		   			
		   		}else if (searchDto.getIsSendToCrApplicable() != null && searchDto.getIsSendToCrApplicable() == 'Y') {
		   						
		   			statusIds =Arrays.asList(SAMPLE_SEND_TO_CR,RETESTING);
		   			
		   		}else {
		   			
		   			statusIds = Arrays.asList(SAMPLE_COLLECTED,RETESTING);
		   		}
		      
	    	  
	    	  
	      }

		 StringBuilder sb = new StringBuilder(query);
		 
		 if (searchDto.getFromDt() != null ) {
	    	  sb.append(
	            " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >= '" + searchDto.getFromDt()+ "' ");
	      }

	      if (searchDto.getToDt() != null ) {
	    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <= '" + searchDto.getToDt() + "' ");
	      }

	      if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
	    	  sb
	            .append(" AND pati_mst.patient_id = " + searchDto.getPatientId());

	      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0)
	    	  sb.append(" AND visit_type_master.visit_type_id IN ("
	            + StringCommonMethods.convertToStringJoiner(searchDto.getVisitTypes()) + ")");

	      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size() > 0)
	    	  sb.append(" AND test_mst.test_id in ( " +StringCommonMethods.convertToStringJoiner( searchDto.getTestTypes())+")");
	      
	      if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0)
	    	  sb
	            .append(" AND lbsampledtls.lab_sample_dtls_id =" + searchDto.getLabSampleDtlsId());

	      if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
	          sb.append("  and lbsampledtls.assign_unit  ="+searchDto.getAssignUnit());
	      
	      if(searchDto.getUhidNumber()!="") {
	    	  sb.append(" AND lower(pati_mst.uhid_number) LIKE concat('%',LOWER('"+searchDto.getUhidNumber()+"'),'%') "); 
	      }
	      
	      if(searchDto.getSampleNo()!="") {
	    	  sb.append(" AND lower(sample_mst.sample_id) LIKE concat('%',LOWER('"+searchDto.getSampleNo()+"'),'%') ");  
	      }
	      
	      if (searchDto.getIsOnHoldParameter() == true) {
        	  statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
        	  sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
              sb.append(" AND lbsampledtls.is_on_hold_parameter =" + searchDto.getIsOnHoldParameter());
          }
          if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
              sb.append(" and ( lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+") or lbsampledtls.is_on_hold_parameter = true )");
          }
	if (searchDto.getLabSortingId() != null) {
		if (searchDto.getLabSortingId() == 1) {
			sb.append(" ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
		} else if (searchDto.getLabSortingId() == 2) {
			sb.append(" ORDER BY   order_master.order_id DESC ");
		} else if (searchDto.getLabSortingId() == 3) {
			sb.append(" ORDER BY    sample_mst.sample_id  DESC ");
		} else if (searchDto.getLabSortingId() == 4) {
			sb.append(" ORDER BY    lbsampledtls.updated_date DESC ");
		}
	} else {
		sb.append(" ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
	}
		 
		return sb;
	}



	@Override
	public Response labResultEntryTypeSearch(SearchDto searchDto) throws ApplicationException {
		try {

		      String query = sessionFactory.getCurrentSession()
		          .getNamedQuery("GET_BIOCHEM_RESULT_ENTRY_LIST").getQueryString().toString();

		      StringBuilder sb = new StringBuilder(query);

		      List<Integer> statusIds = new ArrayList<Integer>(); 
		      if(searchDto.getIsOnHoldParameter() == false) {
		       if(searchDto.getIsWorklistDepartment() == 'Y') {
		   			
		   			statusIds = Arrays.asList(SEND_TO_REPORT_ENTRY);
		   			
		   		}else if(searchDto.getIsAcceptanceInDepartment() == 'Y') {
		   			
		   			statusIds = bioChemStatus;
		   			
		   		}else if(searchDto.getIsSampleAcceptanceApplicable() == 'Y') {
		   			
		   			statusIds = Arrays.asList(SAMPLE_SEND_TO_DEPT,RETESTING);
		   			
		   		}else if (searchDto.getIsSendToCrApplicable() != null && searchDto.getIsSendToCrApplicable() == 'Y') {
		   						
		   			statusIds =Arrays.asList(SAMPLE_SEND_TO_CR,RETESTING);
		   			
		   		}else {
		   			
		   			statusIds = Arrays.asList(SAMPLE_COLLECTED,RETESTING);
		   		}
		      }
		      if (searchDto.getFromDt() != null ) {
		    	  sb.append(
		            " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDate ");
		      }

		      if (searchDto.getToDt() != null ) {
		    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDate ");
		      }
		      
		      if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0) {
		    	  sb.append(" and lbsampledtls.assign_unit =:assignUnit  ");
		      }
		      
		      if (searchDto.getIsOnHoldParameter() == true) {
            	  statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
            	  sb.append(" and lbsampledtls.sample_status_id IN(:statusIds)");
                  sb.append(" AND lbsampledtls.is_on_hold_parameter =:isOnHoldParameter ");
              }
              if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
                  sb.append(" and (lbsampledtls.sample_status_id IN(:statusIds) or lbsampledtls.is_on_hold_parameter = true )");
              }
		      if(searchDto.getSearchField()!="") {
		    	  sb.append(" AND (lower(pati_mst.uhid_number) LIKE concat('%',LOWER(:searchField),'%')  OR "
		    	  		   + " lower(sample_mst.sample_id) LIKE concat('%',LOWER(:searchField),'%')  OR "
		    	  		   + " lower(pati_mst.first_name) LIKE concat('%',LOWER(:searchField),'%')  OR "
		    	  		   + " lower(pati_mst.middle_name) LIKE concat('%',LOWER(:searchField),'%')  OR "
		    	  		   + " lower(pati_mst.last_name) LIKE concat('%',LOWER(:searchField),'%')  OR "
		    	  		   + " lower(test_mst.test_desc) LIKE concat('%',LOWER(:searchField),'%')  OR"
		    	  		   + " lower(test_mst.test_code) LIKE concat('%',LOWER(:searchField),'%')  OR "
		    	  		   + " lower(subspe_mst.sub_speciality_name) LIKE concat('%',LOWER(:searchField),'%') ) ");
		      }
		      
			if (searchDto.getLabSortingId() != null) {
				if (searchDto.getLabSortingId() == 1) {
					sb.append(" ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
				} else if (searchDto.getLabSortingId() == 2) {
					sb.append(" ORDER BY    order_master.order_id  DESC ");
				} else if (searchDto.getLabSortingId() == 3) {
					sb.append(" ORDER BY    sample_mst.sample_id  DESC ");
				} else if (searchDto.getLabSortingId() == 4) {
					sb.append(" ORDER BY   lbsampledtls.updated_date  DESC ");
				}
			} else {
				sb.append(" ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
			}
		      
		    
		      
		      Query createdQuery = sessionFactory.getCurrentSession()
		          .createSQLQuery(sb.toString());

			if (searchDto.getFromDt() != null ) {
				createdQuery.setParameter("fromDate", searchDto.getFromDt());
			}

			if (searchDto.getToDt() != null ) {
				createdQuery.setParameter("toDate", searchDto.getToDt());
			}

			if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0) {
				createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());
			}

			if (searchDto.getIsOnHoldParameter() == true) {
				createdQuery.setParameterList("statusIds", statusIds);
				createdQuery.setParameter("isOnHoldParameter", searchDto.getIsOnHoldParameter());
			}
			if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
				createdQuery.setParameterList("statusIds", statusIds);
			}
			if(searchDto.getSearchField()!="") {
				createdQuery.setParameter("searchField", searchDto.getSearchField());
			}



			List<LabResultMasterDto> list  = createdQuery
						.setParameter("orgId", searchDto.getOrgId())
						.setParameter("orgUnitId", searchDto.getOrgUnitId())
						.setParameter("deptId", searchDto.getDeptId())
						.setParameter("subDeptId", searchDto.getSubDeptId())
						.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
						.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
						.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();

		      Integer count =0 ;
		      if (list.isEmpty()) {
		        return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.", list, count);
		      } else {
		    	  
		    	  String sql="	select count(sample_dtls_id) as \"retestCount\"  , sample_dtls_id as \"labSampleDtlsId\"  from lab.t_sample_audit_trail sample_audit_trail  "
			  				+ "where test_status_id = 7 and sample_audit_trail.org_id =:orgId "+"  group by sample_dtls_id  ";
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
		    	  
		    	  count = list.size();
		    	  
		        return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, list,count);
		      }


		    } catch (Exception e) {
		      logger.error("Exection", e);
		      throw e;
		    }
	}

	@Override
	public Response getParameterValueByMachineId(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {
			
			List<LabResultDetailsViewDto> finalList = new ArrayList<>();

			  List<LabResultDetailsViewDto> refeRenge = sessionFactory.getCurrentSession().getNamedQuery("GET_NUMERIC_REF_RANGE_BY_MACHINE_ID")
			    				.setParameter("testId", bioChemParamDto.getTestId())
  								.setParameter("genderId", bioChemParamDto.getGenderId())
  								.setParameter("ageInDays", bioChemParamDto.getPatientAgeDays())
  								.setParameter("machineId", bioChemParamDto.getMachineId())
  								.setResultTransformer(Transformers.aliasToBean(LabResultDetailsViewDto.class)).list();
			  if(refeRenge != null && refeRenge.size()>0) {
				  finalList.addAll(refeRenge);
			  }

			  List<LabResultDetailsViewDto> texRenge = sessionFactory.getCurrentSession().getNamedQuery("GET_TEXTAUL_REF_RANGE_BY_MACHINE_ID")
	    				.setParameter("testId", bioChemParamDto.getTestId())
						.setParameter("genderId", bioChemParamDto.getGenderId())
						.setParameter("ageInDays", bioChemParamDto.getPatientAgeDays())
						.setParameter("machineId", bioChemParamDto.getMachineId())
						.setResultTransformer(Transformers.aliasToBean(LabResultDetailsViewDto.class)).list();
			  
			  if(texRenge != null && texRenge.size()>0) {
				  finalList.addAll(texRenge);
			  }
			  
			  List<LabResultDetailsViewDto> multiRenge = sessionFactory.getCurrentSession().getNamedQuery("GET_MULTEXTUAL_REF_RANGE_BY_MACHINE_ID")
	    				.setParameter("testId", bioChemParamDto.getTestId())
						.setParameter("genderId", bioChemParamDto.getGenderId())
						.setParameter("ageInDays", bioChemParamDto.getPatientAgeDays())
						.setParameter("machineId", bioChemParamDto.getMachineId())
						.setResultTransformer(Transformers.aliasToBean(LabResultDetailsViewDto.class)).list();
			  
			  if(multiRenge != null && multiRenge.size()>0) {
				  finalList.addAll(multiRenge);
			  }
			  
		return new Response(SUCCESS, SUCCESS_200, null, finalList, null);
	} catch (Exception e) {
		logger.error("Exception", e);
		throw e;
	}
	}

	@Override
	public Response checkLabResultIsExistAndUpdateStatus(List<LabResultMasterDto> listLabResultMasterDto)
			throws ApplicationException {
		try {

			for (LabResultMasterDto labResultMasterDto : listLabResultMasterDto) {
				
				List<LabResultMasterDto> resultList =  sessionFactory.getCurrentSession()
						.createSQLQuery("select tlrd.lab_sample_dtls_id as \"labSampleDtlsId\" , tlrd.lab_test_res_id as \"labTestResId\"  FROM lab.t_lab_test_result tlrd where tlrd.lab_sample_dtls_id =:labSampleDtlsId AND tlrd.status='A' AND tlrd.sample_status_id in (6,17,8) ")
						.setParameter("labSampleDtlsId", labResultMasterDto.getLabSampleDtlsId())
						.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
				
				if(resultList!=null && resultList.size()>0 || (labResultMasterDto.getAmend()!= null && labResultMasterDto.getAmend() == true)) {
					sessionFactory.getCurrentSession().createSQLQuery(" update lab.t_lab_test_result  set "
		        			+ " status ='I' where lab_sample_dtls_id =:labSampleDtlsId " + " AND sample_status_id in (6,17,8,7) ")
							.setParameter("labSampleDtlsId", labResultMasterDto.getLabSampleDtlsId())
							.executeUpdate();
				}
			}
			
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
		
	}

    @Override
    public Response<LabResultMasterDto, Integer> updateEntryDetails(List<LabResultMasterDto> listLabResultMasterDto)
            throws ApplicationException {
          try {

              for (Iterator iterator = listLabResultMasterDto.iterator(); iterator.hasNext();) {
                LabResultMasterDto labResultMasterDto = (LabResultMasterDto) iterator.next();
                Long currentTimeWithZone  = TimeZoneComponent.getDateByzone(labResultMasterDto.getOrgUnitId()).getTime();
                LabResultMaster labResultMaster = mapper.map(labResultMasterDto, LabResultMaster.class,
                    "LabResultMasterDtoToLabResultMaster");
                labResultMaster.setResultEnterBy(labResultMasterDto.getCreatedBy());
                labResultMaster.setCreatedDate(currentTimeWithZone);
                labResultMaster.setCreatedBy(labResultMasterDto.getCreatedBy());
                labResultMaster.setResultEnterDatetime(currentTimeWithZone);
                labResultMaster.setStatus('A');
                LabResultMaster labResultMst = update(labResultMaster);
                
                LabSampleDetailsMaster labSampleDetailsMaster =
                    (LabSampleDetailsMaster) sessionFactory.getCurrentSession()
                        .get(LabSampleDetailsMaster.class, labResultMst.getLabSampleDtlsId());
                labSampleDetailsMaster.setInternalNotes(labResultMasterDto.getInternalNotes());
                labSampleDetailsMaster.setSrfId(labResultMasterDto.getSrfId());
                labSampleDetailsMaster.setIcmrId(labResultMasterDto.getIcmrId());
                labSampleDetailsMaster.setIsOnHoldParameter(labResultMasterDto.getIsOnHoldParameter());
                labSampleDetailsMaster.setUpdatedBy(labResultMst.getUpdatedBy());
                labSampleDetailsMaster.setUpdatedDate(currentTimeWithZone);
                if(labResultMasterDto.getSampleStatusId() != null && (labResultMasterDto.getSampleStatusId() == 8 || labResultMasterDto.getSampleStatusId() == 35)){
                	labSampleDetailsMaster.setSampleStatusId(labResultMasterDto.getSampleStatusId());
                }else{
                	labSampleDetailsMaster.setSampleStatusId(RESULT_ENTRY_DONE);
                }
                sessionFactory.getCurrentSession().update(labSampleDetailsMaster);
                
               com.param.global.common.Response res =  iLabConfigurationDao.getLabConfigurationList(labSampleDetailsMaster.getOrgId(),labSampleDetailsMaster.getAssignUnit());
                Character c = null;
               if(res.getListObject() != null && res.getListObject().size()>0) {
                   
                   List<LabConfigurationDto> list = res.getListObject() ;
                  c = list.getFirst().getSkipValidation();
               }
                for (Iterator iterator1 =
                    labResultMst.getListLabSampleResultDetailsMaster().iterator(); iterator1.hasNext();) {
                  LabResultDetailsMaster labResultDetailsMaster = (LabResultDetailsMaster) iterator1.next();
                  labResultDetailsMaster.setLabResultId(labResultMst.getLabTestResId());
                  labResultDetailsMaster.setFirstLevelResult(labResultDetailsMaster.getFinalResult());
                  if(c.equals('Y')) {
                      labResultDetailsMaster.setSecondLevelResult(labResultDetailsMaster.getFinalResult()); 
                  }
                  labResultDetailsMaster.setCreatedBy(labResultMasterDto.getCreatedBy());
                  labResultDetailsMaster.setCreatedDate(currentTimeWithZone);
                  sessionFactory.getCurrentSession().update(labResultDetailsMaster);
                }

              }

              return new Response(SUCCESS, SUCCESS_200, SAMPLE_RESULT_ENTRY_SUCC, null, null);
            } catch (Exception e) {
              logger.error("Exception", e);
              return new Response(ERROR, ERR_500, SAMPLE_RESULT_ENTRY_FAIL, null, null);
            }
          }

	private StringBuilder generateAllSubDeptQueryForTypeSearch(String query, SearchDto searchDto) {
		
		
		 List<Integer> statusIds = new ArrayList<Integer>(); 
		 List<Integer> subSpeciIds = new ArrayList<Integer>(); 
	      if(searchDto.getIsOnHoldParameter() == false) {
	    	  
	       if(searchDto.getIsSampleAcceptanceApplicable() == 'Y') {
	   			
	   			statusIds = Arrays.asList(SAMPLE_SEND_TO_DEPT,RETESTING);
	   			
	   		}else if (searchDto.getIsSendToCrApplicable() != null && searchDto.getIsSendToCrApplicable() == 'Y') {
	   						
	   			statusIds =Arrays.asList(SAMPLE_SEND_TO_CR,RETESTING);
	   			
	   		}else {
	   			
	   			statusIds = Arrays.asList(SAMPLE_COLLECTED,RETESTING);
	   		}
	      }
		
		StringBuilder sb = new StringBuilder(query);
		
		 if (searchDto.getSubDeptIds() != null  && searchDto.getSubDeptIds().size() > 0) {
			 subSpeciIds = searchDto.getSubDeptIds();
	    	  sb.append(
	    			   " AND (case when tlc.consolidated_result_entry = 'N' then lbsampledtls.sub_dept_id not in ("+subSpeciIds.toString().replaceAll("\\[","").replaceAll("\\]","")+") else  (lbsampledtls.sub_dept_id not in (tlc.serology_id) or tlc.serology_id is  null or tlc.serology_id =0 )  end) ");
	    			     
	      }else {
	    	  sb.append(
	    	  "  AND (case when tlc.consolidated_result_entry = 'N' then lbsampledtls.sub_dept_id not in (tlc.biochemistry_id ,tlc.hematology_id ,tlc.clinical_pathology_id ,tlc.serology_id) else  (lbsampledtls.sub_dept_id not in (tlc.serology_id) or tlc.serology_id is  null or tlc.serology_id =0 )  end) ");
	      }
		
		
		 if (searchDto.getFromDt() != null ) {
	    	  sb.append(
	            " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >= '" + searchDto.getFromDt()+ "' ");
	      }

	      if (searchDto.getToDt() != null ) {
	    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <= '" + searchDto.getToDt() + "' ");
	      }
	      if (searchDto.getIsOnHoldParameter() == true) {
       	  statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
       	  sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
             sb.append(" AND lbsampledtls.is_on_hold_parameter =" + searchDto.getIsOnHoldParameter());
         }
         if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
             sb.append(" and (lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+") or lbsampledtls.is_on_hold_parameter = true) ");
         }
	      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0)
	    	  
	    	  if(searchDto.getIsAcceptanceInDepartment() == 'Y') {
	    		  
	    		  List<obj> withoutSerosubDepts = new ArrayList<>();
	    		  String sero = "Serology";
	    		  for (obj lan : searchDto.getSubDepts()) {
	    			  
	    			  if(lan.getLabel()==null ) {
	    				  if(searchDto.getDepartmentList()!=null) {
	    					  
	    					  obj newDto = searchDto.getDepartmentList().stream()
	    							  .filter(ob -> ob.getId().equals(lan.getId()))
	    							  .findAny()
	    							  .orElse(null);
	    					  if(newDto!=null) {
	    						  lan.setLabel(newDto.getLabel());
	    					  }
	    				  }
	    			  }
	    			  
	    			  if(lan.getLabel()!=null && !(lan.getLabel().toLowerCase()).equals(sero.toLowerCase())) {
	    				  withoutSerosubDepts.add(lan);
	    			  }
	    		  }
	    		  
	    		   if (withoutSerosubDepts != null && withoutSerosubDepts.size() > 0) {
	    			   sb.append(" AND test_mst.dept_id IN ( "
			  		            + StringCommonMethods.convertToStringJoiner(withoutSerosubDepts) + ")");
	    		   }
	    		  
	    	  }else {
	    		  sb.append(" AND test_mst.dept_id IN ( "
	  		            + StringCommonMethods.convertToStringJoiner(searchDto.getSubDepts()) + ")");
	    	  }
	      
	     
	      if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
	          sb.append("  and lbsampledtls.assign_unit  ="+searchDto.getAssignUnit());
	      
	      if(searchDto.getSearchField()!="") {
	    	  sb.append(" AND (lower(pati_mst.uhid_number) LIKE concat('%',LOWER('"+searchDto.getSearchField()+"'),'%')  OR "
	    	  		   + " lower(sample_mst.sample_id) LIKE concat('%',LOWER('"+searchDto.getSearchField()+"'),'%')  OR "
	    	  		   + " lower(pati_mst.first_name) LIKE concat('%',LOWER('"+searchDto.getSearchField()+"'),'%')  OR "
	    	  		   + " lower(pati_mst.middle_name) LIKE concat('%',LOWER('"+searchDto.getSearchField()+"'),'%')  OR "
	    	  		   + " lower(pati_mst.last_name) LIKE concat('%',LOWER('"+searchDto.getSearchField()+"'),'%')  OR "
	    	  		   + " lower(test_mst.test_desc) LIKE concat('%',LOWER('"+searchDto.getSearchField()+"'),'%')  OR"
	    	  		   + " lower(test_mst.test_code) LIKE concat('%',LOWER('"+searchDto.getSearchField()+"'),'%')  OR "
	    	  		   + " lower(subspe_mst.sub_speciality_name) LIKE concat('%',LOWER('"+searchDto.getSearchField()+"'),'%') ) "); 
	      }
	     
		if (searchDto.getLabSortingId() != null) {
			if (searchDto.getLabSortingId() == 1) {
				sb.append(" ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
			} else if (searchDto.getLabSortingId() == 2) {
				sb.append(" ORDER BY    order_master.order_id  DESC ");
			} else if (searchDto.getLabSortingId() == 3) {
				sb.append(" ORDER BY    sample_mst.sample_id  DESC ");
			} else if (searchDto.getLabSortingId() == 4) {
				sb.append(" ORDER BY    lbsampledtls.updated_date DESC  ");
			}
		} else {
			sb.append(" ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
		}
	      
		return sb;
	}

	private StringBuilder generateQueryForTypeSearch(String query, SearchDto searchDto) {
		
		
		 List<Integer> statusIds = new ArrayList<Integer>(); 
	      if(searchDto.getIsOnHoldParameter() == false) {
	  
		      if(searchDto.getIsAcceptanceInDepartment() == 'Y') {
		   			
		   			statusIds = bioChemStatus;
		   			
		   		}else if(searchDto.getIsSampleAcceptanceApplicable() == 'Y') {
		   			
		   			statusIds = Arrays.asList(SAMPLE_SEND_TO_DEPT,RETESTING);
		   			
		   		}else if (searchDto.getIsSendToCrApplicable() != null && searchDto.getIsSendToCrApplicable() == 'Y') {
		   						
		   			statusIds =Arrays.asList(SAMPLE_SEND_TO_CR,RETESTING);
		   			
		   		}else {
		   			
		   			statusIds = Arrays.asList(SAMPLE_COLLECTED,RETESTING);
		   		}
		      
	    	  
	      }

		 StringBuilder sb = new StringBuilder(query);
		 if (searchDto.getFromDt() != null ) {
	    	  sb.append(
	            " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >= '" + searchDto.getFromDt()+ "' ");
	      }

	      if (searchDto.getToDt() != null ) {
	    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <= '" + searchDto.getToDt() + "' ");
	      }

	      if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
	          sb.append("  and lbsampledtls.assign_unit  ="+searchDto.getAssignUnit());
	      
	      if(searchDto.getSearchField()!="") {
	    	  sb.append(" AND (lower(pati_mst.uhid_number) LIKE concat('%',LOWER('"+searchDto.getSearchField()+"'),'%')  OR "
	    	  		   + " lower(sample_mst.sample_id) LIKE concat('%',LOWER('"+searchDto.getSearchField()+"'),'%')  OR "
	    	  		   + " lower(pati_mst.first_name) LIKE concat('%',LOWER('"+searchDto.getSearchField()+"'),'%')  OR "
	    	  		   + " lower(pati_mst.middle_name) LIKE concat('%',LOWER('"+searchDto.getSearchField()+"'),'%')  OR "
	    	  		   + " lower(pati_mst.last_name) LIKE concat('%',LOWER('"+searchDto.getSearchField()+"'),'%')  OR "
	    	  		   + " lower(test_mst.test_desc) LIKE concat('%',LOWER('"+searchDto.getSearchField()+"'),'%')  OR"
	    	  		   + " lower(test_mst.test_code) LIKE concat('%',LOWER('"+searchDto.getSearchField()+"'),'%')  OR "
	    	  		   + " lower(subspe_mst.sub_speciality_name) LIKE concat('%',LOWER('"+searchDto.getSearchField()+"'),'%') ) "); 
	      }
	      if (searchDto.getIsOnHoldParameter() == true) {
       	  statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
       	  sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
             sb.append(" AND lbsampledtls.is_on_hold_parameter =" + searchDto.getIsOnHoldParameter());
         }
         if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
             sb.append(" and (lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+") or lbsampledtls.is_on_hold_parameter = true) ");
         }
	if (searchDto.getLabSortingId() != null) {
		if (searchDto.getLabSortingId() == 1) {
			sb.append(" ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
		} else if (searchDto.getLabSortingId() == 2) {
			sb.append(" ORDER BY   order_master.order_id DESC ");
		} else if (searchDto.getLabSortingId() == 3) {
			sb.append(" ORDER BY    sample_mst.sample_id DESC ");
		} else if (searchDto.getLabSortingId() == 4) {
			sb.append(" ORDER BY    lbsampledtls.updated_date DESC ");
		}
	} else {
		sb.append(" ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
	}
		 
		return sb;
	}
	
	@Override
	public Response consolidatedResultEntryTypeSearch(SearchDto searchDto) throws ApplicationException {
		try {
			
			
			List<LabResultMasterDto> list =  new ArrayList<>();

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

			 Integer countSero = 0;
			 if(searchDto.getIsOnHoldParameter() == false && isSerologyIdPresent == true) {
				 String query = sessionFactory.getCurrentSession()
				          .getNamedQuery("GET_ALL_SERO_RESULT_ENTRY_LIST").getQueryString().toString();

				 StringBuilder sb = generateQueryForTypeSearch(query,searchDto);

				      
				      Query createdQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());

				      StringBuilder sb1 = sb;
				      List<LabResultMasterDto> listSero  = createdQuery
								.setParameter("orgId", searchDto.getOrgId())
								.setParameter("orgUnitId", searchDto.getOrgUnitId())
								.setParameter("deptId", searchDto.getDeptId())
								.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
								.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
								.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();

				      Query createdQuery1 = sessionFactory.getCurrentSession().createSQLQuery(sb1.toString());
				      List<LabResultMasterDto> listcount  = createdQuery1
								.setParameter("orgId", searchDto.getOrgId())
								.setParameter("orgUnitId", searchDto.getOrgUnitId())
								.setParameter("deptId", searchDto.getDeptId())
								.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
				     
				      
				      if(listSero != null && listSero.size()>0) {
				    	  countSero = listcount.size();
				    	  list.addAll(listSero);
				      }
		   			
		}

			if(searchDto.getSubDepts().size()==1 && isSerologyIdPresent == true){
				return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, list, list.size());
			}

		      String query = sessionFactory.getCurrentSession()
		          .getNamedQuery("GET_ALL_SUBDEPTS_RESULT_ENTRY_LIST").getQueryString().toString();

		      StringBuilder sb = generateAllSubDeptQueryForTypeSearch(query,searchDto);
		      
		      Query createdQuery = sessionFactory.getCurrentSession()
		          .createSQLQuery(sb.toString());

		       StringBuilder sb1 = sb;
		      
		       List<LabResultMasterDto> listall  = createdQuery
						.setParameter("orgId", searchDto.getOrgId())
						.setParameter("orgUnitId", searchDto.getOrgUnitId())
						.setParameter("deptId", searchDto.getDeptId())
						.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
						.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
						.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();

		       Query createdQuery1 = sessionFactory.getCurrentSession()
				          .createSQLQuery(sb1.toString());
		      List<LabResultMasterDto> list1  = createdQuery1
						.setParameter("orgId", searchDto.getOrgId())
						.setParameter("orgUnitId", searchDto.getOrgUnitId())
						.setParameter("deptId", searchDto.getDeptId())
						.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
		      Integer count = 0;
		      
		      
		      if(listall != null && listall.size()>0) {
		    	  list.addAll(listall);
		      }
   			
		      
		      if (list.isEmpty()) {
		    	  
		        return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.", list, count);
		        
		      } else {
		    	  
		    	  if(list1 != null && list1.size()>0) {
		    		  count = list1.size();
		    		  count = count + countSero;
		    	  }else {
		    		  count = count + countSero;
		    	  }
		    	  
		        return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, list, count);
		      }


		    } catch (Exception e) {
		      logger.error("Exection", e);
		      throw e;
		    }
	}

	@Override
	public Response<LabSampleMasterDto, Integer> barcodeScannerListSearch(SearchDto searchDto) {
			try {
				
				String sb2="select " + 
						"	panel_master.panel_code as \"panelCode\", " + 
						"	lbsampledtls.lab_sample_dtls_id as \"labSampleDtlsId\", " + 
						"	lbsampledtls.org_id as \"orgId\", " + 
						"	lbsampledtls.org_unit_id as \"orgUnitId\", " + 
						"	lbsampledtls.assign_unit as \"assignUnit\", " + 
						"	lbsampledtls.sample_reception as \"sampleReception\", " + 
						"	lbsampledtls.sample_processing as \"sampleProcessing\", " + 
						"	lbsampledtls.sample_analysis as \"sampleAnalysisAndDetection\", " + 
						"	visit_type_master.visit_type_code as \"visitType\", " + 
						"	test_master.test_desc as \"testDesc\", " + 
						"	test_master.test_code as \"testCode\", " + 
						"	test_master.test_id as \"testId\", " + 
						"	test_master.service_id as \"serviceId\", " + 
						"	lbsampledtls.perform_test_id as \"performTestId\", " + 
						"	samp_mst.sample_desc as \"sampleType\", " + 
						"	sample_mst.sample_no as \"labSampleNo\", " + 
						" mum.user_name || '-' || mum.user_id as \"userName\", "+
						" tltr.result_enter_datetime as \"resultEntryDate\", "+
						" tltr.result_validated_datetime as \"resultValidatedDate\", "+
						" tltr.result_authorised_datetime as \"reportUploadedDate\", "+
						" to_char(tltr.result_enter_datetime, 'DD/MM/YYYY HH24:MI:SS') as \"resultEntryDateString\", "+
						" to_char(tltr.result_validated_datetime, 'DD/MM/YYYY HH24:MI:SS') as \"resultValidatedDateString\", "+
						" to_char(tltr.result_authorised_datetime, 'DD/MM/YYYY HH24:MI:SS') as \"reportUploadedDateString\", "+
						"	pati_mst.uhid_number as uhid, " + 
						"	pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name) || ' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob) "
						+ " || '/' || gen_mst.gender_code as \"patientDetails\", " + 
						"	subspe_mst.sub_speciality_name as \"deptName\", " + 
						"	contai_mst.container_name as \"containerName\", " + 
						"	order_details.created_date as \"orderDateTime\", " + 
						"	lbsampledtls.sample_collection_datetime as \"sampleCollectionDatetime\", " + 
				        "  to_char(order_details.created_date, 'DD/MM/YYYY HH24:MI:SS') AS \"orderDateTimeString\",   "+
				        "  to_char(lbsampledtls.sample_collection_datetime, 'DD/MM/YYYY HH24:MI:SS') AS \"sampleCollectionDatetimeString\"   "+
						"from " + 
						"	lab.t_lab_sample_dtls lbsampledtls " + 
						"inner join public.t_order_details order_details on " + 
						"	order_details.order_details_id = lbsampledtls.order_details_id " + 
						"inner join public.t_order_master order_master on " + 
						"	order_master.order_id = order_details.order_id " + 
						"inner join lab.t_lab_sample_master sample_mst on " + 
						"	lbsampledtls.lab_sample_id = sample_mst.lab_sample_id " + 
						"inner join public.m_visit_type_master visit_type_master on " + 
						"	visit_type_master.visit_type_id = order_master.visit_type_id " + 
						"inner join patient.t_patient_registration pati_mst on " + 
						"	lbsampledtls.patient_id = pati_mst.patient_id " + 
						"inner join m_prefix_master pre_mst on " + 
						"	pati_mst.prefix_id = pre_mst.prefix_id " + 
						"inner join m_gender_master gen_mst on " + 
						"	pati_mst.gender_id = gen_mst.gender_id " + 
						"inner join lab.m_test_master test_master on " + 
						"	test_master.test_id = lbsampledtls.test_id " + 
						"inner join lab.m_container_master contai_mst on " + 
						"	contai_mst.container_id = lbsampledtls.container_id " + 
						"inner join lab.m_sample_master samp_mst on " + 
						"	samp_mst.sample_id = test_master.sample_id " + 
						"left join adt.t_admission ta on " + 
						"	ta.admission_id = order_master.admission_id " + 
						"	and ta.status = 'A' " + 
						"left join doctor.m_sub_speciality_master subspe_mst on " + 
						"	subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id " + 
						"left join lab.t_panel_master panel_master on " + 
						"	panel_master.panel_id = lbsampledtls.profile_id " + 
						"inner join public.t_lab_configuration lab_config on " + 
						"	lab_config.organisation_id = :orgId " + 
						"	and lab_config.unit_id = :orgUnitId " + 
						"  left join lab.t_lab_test_result tltr on tltr.lab_sample_dtls_id =lbsampledtls.lab_sample_dtls_id  "+
						" inner join public.m_user_master mum on mum.user_id =lbsampledtls.created_by  "+
						"where " + 
						"	 lbsampledtls.org_id = :orgId " + 
						"	and lbsampledtls.org_unit_id = :orgUnitId " + 
						"	and lbsampledtls.dept_id = :deptId " + 
						"	and test_master.is_histo_cyto = 'N' " + 
						"	and order_details.ord_cancelled = 'N' " + 
						"	and order_details.status = 'A' " + 
						"   and to_char(lbsampledtls.sample_collection_datetime,'YYYY-MM-DD') between :fromDt and :toDt" +
						"	and lbsampledtls.assign_unit = :assignUnit " + 
						"		AND (:UHID = '' OR pati_mst.uhid_number = :UHID) "+
						"		AND (:sampleNo = '' OR sample_mst.sample_no = :sampleNo) "+
						" order by " + 
						"	lbsampledtls.priority_id, " + 
						"	lbsampledtls.sample_accept_datetime";
			    List<LabResultMasterDto> list = sessionFactory.getCurrentSession().createSQLQuery(sb2)
			    		.setParameter("sampleNo", searchDto.getSampleNo())
			    		.setParameter("UHID", searchDto.getUhidNumber())
			    		.setParameter("orgId", searchDto.getOrgId())
						.setParameter("orgUnitId", searchDto.getOrgUnitId())
						.setParameter("deptId", searchDto.getDeptId())
						.setParameter("fromDt", searchDto.getFromDt())
						.setParameter("toDt", searchDto.getToDt())
						.setParameter("assignUnit", searchDto.getAssignUnit())
			            .setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
			      Integer count = 0;
			      if (list.isEmpty()) {
			        return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.", list, count);
			      } else {
			        return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, list, count);
			      }


			    } catch (Exception e) {
			      logger.error("Exection", e);
			      throw e;
			    }
		
	}


	@Override
	public Response<LabResultMasterDto, Integer> barcodeScannerListSave(
			List<LabResultMasterDto> listLabResultMasterDto) {
		Integer count=0;
		for (LabResultMasterDto labResultMasterDto : listLabResultMasterDto) {
			 Integer  count1=sessionFactory.getCurrentSession()    .createSQLQuery("UPDATE lab.t_lab_sample_dtls SET sample_reception = :sampleReception, "
							 + "sample_processing = :sampleProcessing, "
							 + "sample_analysis = :sampleAnalysisAndDetection "
							 + "WHERE lab_sample_dtls_id = :labSampleDtlsId")
					.setParameter("sampleReception", labResultMasterDto.getSampleReception())
					.setParameter("sampleProcessing", labResultMasterDto.getSampleProcessing())
					.setParameter("sampleAnalysisAndDetection", labResultMasterDto.getSampleAnalysisAndDetection())
					.setParameter("labSampleDtlsId", labResultMasterDto.getLabSampleDtlsId())
					 .executeUpdate();
		}
     
		 return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.", listLabResultMasterDto, count);
	}

	@Override
	public Response<LabSampleMasterDto, Integer> getSampleRecollection(SearchDto searchDto) {
		String sql="select sample_recollection as \"sampleRecollection\" from public.t_user_unit_mapper where unit_id=:orgUnitId and organisation_id=:orgId and user_id=:userId and status='A' ";
		List<LabResultMasterDto> list1  = sessionFactory.getCurrentSession()
		          .createSQLQuery(sql)
				.setParameter("orgId", searchDto.getOrgId())
				.setParameter("orgUnitId", searchDto.getOrgUnitId())
				.setParameter("userId", searchDto.getUserId())
				.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
		return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, list1, null);
	}

	@Override
	public Response<LabResultMasterDto, Integer> saveInterfaceResult(LabResultMasterDto labResultMasterDto) {
	try{
//		Long currentTimeWithZone  = TimeZoneComponent.getDateByzone(labResultMasterDto.getOrgUnitId()).getTime();
//		String ldt = String.valueOf(currentTimeWithZone);

		String sql = "INSERT INTO lab.t_lab_machine_interface "
				+ "(sample_no, parameter_name, parameter_value, "
				+ " min_value, max_value, param_result, note, "
				+ " machine_name, result_date_time, status, created_by, created_date, "
				+ " updated_by, updated_date, organisation_id, unit_id) "
				+ "VALUES(:sampleNo, :parameterName, :parameterValue, "
				+ "NULL, NULL, :paramResult, 'Interface Result', :machineName, "
				+ "NOW(), 'A', 1, NULL, NULL, NULL, "
				+ ":organisationId, :unitId)";
        
        sessionFactory.getCurrentSession()
		.createSQLQuery(sql)
				.setParameter("sampleNo", labResultMasterDto.getSampleNo())
				.setParameter("parameterName", labResultMasterDto.getTestDesc())
				.setParameter("parameterValue", labResultMasterDto.getInterfaceResult())
				.setParameter("paramResult", labResultMasterDto.getInterfaceResult())
				.setParameter("machineName", labResultMasterDto.getInterfaceName())
				.setParameter("organisationId", labResultMasterDto.getOrgId())
				.setParameter("unitId", labResultMasterDto.getOrgUnitId())
				.executeUpdate();
		
		return new Response(SUCCESS, SUCCESS_200, SAMPLE_RESULT_ENTRY_SUCC, null, null);
	}
	 catch (Exception e) {
      logger.error("Exception", e);
      return new Response(ERROR, ERR_500, SAMPLE_RESULT_ENTRY_FAIL, null, null);
    }
  }

    @Override
    public List<Integer> getLabSampleDetailIdsByPatientId (LabResultMasterDto labResultMasterDto, List<Integer> patientIds) {
        List<Integer> idsList = new ArrayList<>();
        try {
            if(patientIds != null && !patientIds.isEmpty()) {
                String sql = " select lab_sample_dtls_id "
                        + " from lab.t_lab_test_result "
                        + " where patient_id in (:patientIds) "
                        + " and org_id =:orgId "
                        + " and org_unit_id =:unitId "
                        + " and result_authorised_datetime is not null "
                        + " and result_authorised_datetime >= date_trunc('day', current_timestamp - interval '3 months') "
                        + " and result_authorised_datetime <= current_timestamp "
                        + " and status='A' ";
                idsList = sessionFactory.getCurrentSession()
                        .createSQLQuery(sql)
                        .setParameterList("patientIds", patientIds)
                        .setParameter("orgId", labResultMasterDto.getOrgId())
                        .setParameter("unitId", labResultMasterDto.getOrgUnitId())
                        .list();

                return idsList;
            }
            return idsList;
        }catch (Exception e){
            logger.error("Exception", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Integer> getLabSampleDetailIdsByMobileNo (LabResultMasterDto labResultMasterDto) {
        List<Integer> idsList = new ArrayList<>();
        List<Integer> patientIds = new ArrayList<>();
        try {
            String sql = " select patient_id "
                    + " from patient.t_patient_registration "
                    + " where mobile_number =:mobileNo "
                    + " and organization_id =:orgId "
                    + " and unit_id =:unitId "
                    + " and status='A' ";
            patientIds = sessionFactory.getCurrentSession()
                    .createSQLQuery(sql)
                    .setParameter("mobileNo", labResultMasterDto.getMobileNo())
                    .setParameter("orgId", labResultMasterDto.getOrgId())
                    .setParameter("unitId", labResultMasterDto.getOrgUnitId())
                    .list();

            return patientIds;
        }catch (Exception e){
            logger.error("Exception", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<TemplateTransactionDto> getRISReportsByPatientId(LabResultMasterDto labResultMasterDto, List<Integer> patientIds) {
        List<TemplateTransactionDto> risReportsList = new ArrayList<>();
        try {
            if(patientIds != null && !patientIds.isEmpty()) {
                String sql = " select template_transaction_id as \"templateTransactionId\", "
                        + " radiology_report_link as \"templateUrl\", "
                        + " patient_id as \"patientId\" "
                        + " from t_template_transaction "
                        + " where patient_id in (:patientIds) "
                        + " and org_id =:orgId "
                        + " and unit_id =:unitId "
                        + " and report_release_datetime is not null "
                        + " and report_release_datetime >= date_trunc('day', current_timestamp - interval '3 months') "
                        + " and report_release_datetime <= current_timestamp "
                        + " and status='A' ";
                risReportsList = sessionFactory.getCurrentSession()
                        .createSQLQuery(sql)
                        .setParameterList("patientIds", patientIds)
                        .setParameter("orgId", labResultMasterDto.getOrgId())
                        .setParameter("unitId", labResultMasterDto.getOrgUnitId())
                        .setResultTransformer(Transformers.aliasToBean(TemplateTransactionDto.class)).list();

                return risReportsList;
            }
            return risReportsList;
        }catch (Exception e){
            logger.error("Exception", e);
            throw new ServiceException(e.getMessage());
        }
    }
}

package com.param.lis.microbiology.transaction.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.param.entity.lis.micro.SensitivityTestResultDetailsMaster;
import com.param.entity.lis.micro.SensitivityTestResultMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ServiceException;
import com.param.global.unit.dto.LabConfigurationDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.common.StringCommonMethods;
import com.param.lis.microbiology.transaction.dto.MicrobioParamDto;
import com.param.lis.microbiology.transaction.dto.SensitivityTestResultDetailsMasterDto;
import com.param.lis.microbiology.transaction.dto.SensitivityTestResultMasterDto;
import com.param.lis.microbiology.transaction.dto.SensitivityTestResultSubDetailsMasterDto;
import com.param.lis.microbiology.transaction.dto.TMicroIncubationMasterDto;
import com.param.lis.transaction.dto.SampleAcceptancePendingDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class SensitivityTestingDaoImpl extends GenericDao<SensitivityTestResultMaster, Integer>
implements ISensitivityTestingDao, ICommonConstants, ITransactionConstants, IError{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private DozerBeanMapper mapper;
	
	@Autowired
	private ISensitivityTestResultMasterDao iSensitivityTestResultMasterDao;

	public SensitivityTestingDaoImpl() {
		super(SensitivityTestResultMaster.class);
	}

	final static Logger logger = Logger.getLogger(SensitivityTestingDaoImpl.class);

	@Override
	public Response getSensitivityTestingList(MicrobioParamDto microbioParamDto) throws ApplicationException 
	{
		List<SampleAcceptancePendingDto> listMicrobioResultEntryMasterDto = null;
		try
		{
			
			List<LabConfigurationDto> list1=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LAB_CONFIG_LIST")
					.setParameter("orgId", microbioParamDto.getOrgId())
					.setParameter("unitId", microbioParamDto.getOrgUnitId())
					.setResultTransformer(Transformers.aliasToBean(LabConfigurationDto.class)).list();
	    	
			 List<Integer> statusId = new ArrayList<Integer>();
			    
	    	 	if(list1.get(0).getMicroscopicExaminationApplicable() == 'Y') {
					
					statusId = SENSITIVITY_SAMPLE_STATUS_IDS;
					
				}else if(list1.get(0).getBiochemicalTestingApplicable() == 'Y') {
					
					statusId = Arrays.asList(BIOCHEMICAL_RESULT_ENTRY_DONE,SENSITIVITY_TEST,SENSITIVITY_TEST_CHECK); 
					
				}else if(list1.get(0).getMacroscopicExaminationApplicable() == 'Y') {
					
					statusId = Arrays.asList(MACROSCOPIC_EXAMINATION_COMPLETED,SENSITIVITY_TEST,SENSITIVITY_TEST_CHECK); 
					
				}else if(list1.get(0).getIncubationMasterApplicable() == 'Y') {
					
					statusId = Arrays.asList(INCUBATION_OBJSERVATION_COMPLETED,SENSITIVITY_TEST,SENSITIVITY_TEST_CHECK);
					
				}else  if(list1.get(0).getIsWorklistDepartment() == 'Y') {
					
					statusId = Arrays.asList(SEND_FOR_INCUBATION_OBERVATION,SENSITIVITY_TEST,SENSITIVITY_TEST_CHECK);
					
				}else if(list1.get(0).getIsAcceptanceInDepartment() == 'Y') {
					
					statusId = Arrays.asList(SAMPLE_ACCEPTED,SENSITIVITY_TEST,SENSITIVITY_TEST_CHECK);
					
				}else{
					
					statusId = Arrays.asList(SAMPLE_COLLECTED,SENSITIVITY_TEST,SENSITIVITY_TEST_CHECK);
				}
			
			String query = sessionFactory.getCurrentSession().getNamedQuery("GET_SENSITIVITY_TESTING_LIST").getQueryString();
			StringBuffer sb= new StringBuffer(query);
		    sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') >=:fromDate ");
		    sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') <=:toDate ");
		    sb.append(" order by order_master.order_date asc ");
			
			listMicrobioResultEntryMasterDto = sessionFactory.getCurrentSession()
						.createSQLQuery(sb.toString())
						.setParameter("orgId", microbioParamDto.getOrgId())
						.setParameter("orgUnitId", microbioParamDto.getOrgUnitId())
						.setParameterList("sampleStatusIds", statusId)
						.setParameter("deptId", microbioParamDto.getDeptId())
						.setParameter("subDeptId", microbioParamDto.getSubDeptId())
						.setParameter("fromDate", microbioParamDto.getFromDate())
						.setParameter("toDate", microbioParamDto.getToDate())
						.setFirstResult(microbioParamDto.getOffset() != null ? microbioParamDto.getOffset() : 0)
						.setMaxResults(microbioParamDto.getRecordPerPage() != null ? microbioParamDto.getRecordPerPage()  : 10)
						.setResultTransformer(Transformers.aliasToBean(SampleAcceptancePendingDto.class)).list();
				return new Response(SUCCESS, SUCCESS_200, null, listMicrobioResultEntryMasterDto, null);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getSensitivityTestingListCount(MicrobioParamDto microbioParamDto) throws ApplicationException {
		try
		{
			
			List<LabConfigurationDto> list1=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LAB_CONFIG_LIST")
					.setParameter("orgId", microbioParamDto.getOrgId())
					.setParameter("unitId", microbioParamDto.getOrgUnitId())
					.setResultTransformer(Transformers.aliasToBean(LabConfigurationDto.class)).list();
	    	
			 List<Integer> statusId = new ArrayList<Integer>();
			    
	    	 	if(list1.get(0).getMicroscopicExaminationApplicable() == 'Y') {
					
					statusId = SENSITIVITY_SAMPLE_STATUS_IDS;
					
				}else if(list1.get(0).getBiochemicalTestingApplicable() == 'Y') {
					
					statusId = Arrays.asList(BIOCHEMICAL_RESULT_ENTRY_DONE,SENSITIVITY_TEST,SENSITIVITY_TEST_CHECK); 
					
				}else if(list1.get(0).getMacroscopicExaminationApplicable() == 'Y') {
					
					statusId = Arrays.asList(MACROSCOPIC_EXAMINATION_COMPLETED,SENSITIVITY_TEST,SENSITIVITY_TEST_CHECK); 
					
				}else if(list1.get(0).getIncubationMasterApplicable() == 'Y') {
					
					statusId = Arrays.asList(INCUBATION_OBJSERVATION_COMPLETED,SENSITIVITY_TEST,SENSITIVITY_TEST_CHECK);
					
				}else  if(list1.get(0).getIsWorklistDepartment() == 'Y') {
					
					statusId = Arrays.asList(SEND_FOR_INCUBATION_OBERVATION,SENSITIVITY_TEST,SENSITIVITY_TEST_CHECK);
					
				}else if(list1.get(0).getIsAcceptanceInDepartment() == 'Y') {
					
					statusId = Arrays.asList(SAMPLE_ACCEPTED,SENSITIVITY_TEST,SENSITIVITY_TEST_CHECK);
					
				}else{
					
					statusId = Arrays.asList(SAMPLE_COLLECTED,SENSITIVITY_TEST,SENSITIVITY_TEST_CHECK);
				}
			
			String query = sessionFactory.getCurrentSession().getNamedQuery("GET_SENSITIVITY_TESTING_LIST_COUNT").getQueryString();
			StringBuffer sb= new StringBuffer(query);
		    sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') >=:currentDate ");
		    sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') <=:currentDate ");
			
			BigInteger sensitivityTestingListCount = (BigInteger) sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("orgId", microbioParamDto.getOrgId())
					.setParameterList("sampleStatusIds", statusId)
					.setParameter("deptId", microbioParamDto.getDeptId())
					.setParameter("subDeptId", microbioParamDto.getSubDeptId())
					.setParameter("orgUnitId", microbioParamDto.getOrgUnitId())
					.setParameter("orgUnitId", microbioParamDto.getOrgUnitId())
					.setParameter("currentDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(microbioParamDto.getOrgUnitId()),"yyyy-MM-dd"))
					.uniqueResult();
			if (sensitivityTestingListCount.compareTo(BigInteger.ZERO) == 1)
			{
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, sensitivityTestingListCount);
			} else
			{
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getSensitivityTestingDetails(MicrobioParamDto microbioParamDto) throws ApplicationException {
		return null;
	}

	@Override
	public Response saveSensitivityTestingObservation(SensitivityTestResultMasterDto dto)
			throws ApplicationException {
		try
		{
			if (dto !=null)
			{
					SensitivityTestResultMaster sensitivityTestResultMaster = mapper.map(dto,
							SensitivityTestResultMaster.class, "SensitivityTestResultMasterDtoToSensitivityTestResultMaster");
					sensitivityTestResultMaster.setOrgId(dto.getOrgId());
					sensitivityTestResultMaster.setOrgUnitId(dto.getOrgUnitId());
					sensitivityTestResultMaster.setReportReleaseBy((dto.getReportReleaseBy()!=null && dto.getReportReleaseBy()>0)?dto.getReportReleaseBy():dto.getCreatedBy());
					sensitivityTestResultMaster.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getOrgUnitId()).getTime());
					Response<SensitivityTestResultMaster, Integer> sensitivityTestResultMste = iSensitivityTestResultMasterDao.saveSensitivityTestResultMasterDao(sensitivityTestResultMaster,dto.getAmend());
                      
				if(sensitivityTestResultMste.getCode().equals(SUCCESS_200))
				{
					return new Response(SUCCESS, SUCCESS_200, SUCCESS_200, null, null);
				}else
				{
					return new Response(SUCCESS, SUCCESS_200, SUCCESS_200, null, null);
				}
			
			}
		} catch (Exception e)
		{
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, SAMPLE_REJECT_SUCC, null, null);
		}
		return new Response(SUCCESS, SUCCESS_200, SUCCESS_200, null, null);
	}

	 
	@Override
	public Response getSensitivityDetails(Integer labSampleDtlsId, Integer orgId, Integer orgUnitId)
			throws ApplicationException {
		try {
			SensitivityTestResultMasterDto listSensitivityTestResultMasterDto = (SensitivityTestResultMasterDto) sessionFactory
					.getCurrentSession().getNamedQuery("GET_SENSITIVITY_EXAMINATION_MASTER_BY_RESULT_ID")
					.setParameter("labSampleDtlsId", labSampleDtlsId)
					.setParameter("orgId", orgId)
					.setParameter("orgUnitId", orgUnitId)
					.setResultTransformer(Transformers.aliasToBean(SensitivityTestResultMasterDto.class))
					.uniqueResult();
					
				if (listSensitivityTestResultMasterDto != null) 
				{
					

					List<SensitivityTestResultDetailsMasterDto> listSensitivityTestResultDetailsMasterDtoMst = (List<SensitivityTestResultDetailsMasterDto>) sessionFactory
							.getCurrentSession().getNamedQuery("GET_SENSITIVITY_EXAMINATION_DETAILS")
							.setParameter("sensitivityResultId", listSensitivityTestResultMasterDto.getSensitivityResultId())
							.setParameter("orgId", orgId)
							.setParameter("orgUnitId", orgUnitId)
							.setResultTransformer(Transformers.aliasToBean(SensitivityTestResultDetailsMasterDto.class))
							.list();

					if (listSensitivityTestResultDetailsMasterDtoMst != null) 
					{
						List<SensitivityTestResultDetailsMasterDto> listMicroscopicExaminationDetailsMasterDto = new ArrayList<>();
						for (Iterator iterator2 = listSensitivityTestResultDetailsMasterDtoMst.iterator(); iterator2
								.hasNext();) {

							SensitivityTestResultDetailsMasterDto sensitivityTestResultDetailsMasterDto = (SensitivityTestResultDetailsMasterDto) iterator2
									.next();

					
							
							List<SensitivityTestResultSubDetailsMasterDto> listSensitivityTestResultSubDetailsMaster = (List<SensitivityTestResultSubDetailsMasterDto>) sessionFactory
									.getCurrentSession().getNamedQuery("GET_SENSITIVITY_EXAMINATION_SUB_DETAILS")
									.setParameter("sesitivityResultDetailsId",
											sensitivityTestResultDetailsMasterDto.getSesitivityResultDetailsId())
									.setParameter("orgId", orgId)
									.setParameter("orgUnitId",orgUnitId)
									.setResultTransformer(Transformers.aliasToBean(SensitivityTestResultSubDetailsMasterDto.class))
									.list();
							
							if(listSensitivityTestResultSubDetailsMaster!=null)
							{
								List<SensitivityTestResultSubDetailsMasterDto> listsensitivityTestResultSubDetailsMasterDto = new ArrayList<>();
								for (Iterator iterator3 = listSensitivityTestResultSubDetailsMaster.iterator(); iterator3
										.hasNext();) {
									SensitivityTestResultSubDetailsMasterDto sensitivityTestResultSubDetailsMasterDto = (SensitivityTestResultSubDetailsMasterDto) iterator3
											.next();
									

									listsensitivityTestResultSubDetailsMasterDto
											.add(sensitivityTestResultSubDetailsMasterDto);
								}
								sensitivityTestResultDetailsMasterDto.setListSensitivityTestResultSubDetailsMaster(
										listsensitivityTestResultSubDetailsMasterDto);
								listMicroscopicExaminationDetailsMasterDto.add(sensitivityTestResultDetailsMasterDto);
							}

							
						}
						listSensitivityTestResultMasterDto.setListSensitivityTestResultDetailsMaster(
								listSensitivityTestResultDetailsMasterDtoMst);
					}

				}

			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null,listSensitivityTestResultMasterDto);

		} catch (HibernateException er) {
			logger.error("Exection", er);throw er;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response updateLabSampleDetailsMaster(SensitivityTestResultMasterDto sensitivityTestResultMasterDto)
			throws ApplicationException 
	{
		
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response sensitivityTestingListListSearch(SearchDto searchDto) throws ApplicationException {
		try {

			List<Integer>  subDeptIds = new ArrayList<Integer>();
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


			List<LabConfigurationDto> list1=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LAB_CONFIG_LIST")
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("unitId", searchDto.getOrgUnitId())
					.setResultTransformer(Transformers.aliasToBean(LabConfigurationDto.class)).list();
	    	
	    	 List<Integer> statusId = new ArrayList<Integer>();
			    
	    	 	if(list1.get(0).getMicroscopicExaminationApplicable() == 'Y') {
					
					statusId = SENSITIVITY_SAMPLE_STATUS_IDS;
					
				}else if(list1.get(0).getBiochemicalTestingApplicable() == 'Y') {
					
					statusId = Arrays.asList(BIOCHEMICAL_RESULT_ENTRY_DONE,SENSITIVITY_TEST,SENSITIVITY_TEST_CHECK); 
					
				}else if(list1.get(0).getMacroscopicExaminationApplicable() == 'Y') {
					
					statusId = Arrays.asList(MACROSCOPIC_EXAMINATION_COMPLETED,SENSITIVITY_TEST,SENSITIVITY_TEST_CHECK); 
					
				}else if(list1.get(0).getIncubationMasterApplicable() == 'Y') {
					
					statusId = Arrays.asList(INCUBATION_OBJSERVATION_COMPLETED,SENSITIVITY_TEST,SENSITIVITY_TEST_CHECK,PARTIAL_REPORT_RELEASED);
					
				}else  if(list1.get(0).getIsWorklistDepartment() == 'Y') {
					
					statusId = Arrays.asList(SEND_FOR_INCUBATION_OBERVATION,SENSITIVITY_TEST,SENSITIVITY_TEST_CHECK);
					
				}else if(list1.get(0).getIsAcceptanceInDepartment() == 'Y') {
					
					statusId = Arrays.asList(SAMPLE_ACCEPTED,SENSITIVITY_TEST,SENSITIVITY_TEST_CHECK);
					
				}else if(list1.get(0).getIsSampleAcceptanceApplicable() == 'Y') {
					
					statusId = Arrays.asList(SAMPLE_SEND_TO_DEPT,SENSITIVITY_TEST,SENSITIVITY_TEST_CHECK);
					
				}else if (list1.get(0).getIsSendToCrApplicable() != null && list1.get(0).getIsSendToCrApplicable() == 'Y') {
								
					statusId = Arrays.asList(SAMPLE_SEND_TO_CR,SENSITIVITY_TEST,SENSITIVITY_TEST_CHECK);
					
				}else {
					
					statusId = Arrays.asList(SAMPLE_COLLECTED,SENSITIVITY_TEST,SENSITIVITY_TEST_CHECK);
				}
			
		      String query = sessionFactory.getCurrentSession()
		          .getNamedQuery("GET_SENSITIVITY_TESTING_LIST").getQueryString().toString();

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
		      if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0){
		    	  sb.append(" AND lbsampledtls.lab_sample_id =:labSampleId ");
		      }
		      
		      if(searchDto.getSampleNo()!=null && searchDto.getSampleNo()!="") {
		    	  sb.append(" AND CAST(sample_mst.sample_no  AS varchar )like :sampleNo ");
		      }
		      
		      if (searchDto.getUhidNumber() != "" && searchDto.getUhidNumber()!=null) {
					sb.append(" AND lower(pati_mst.uhid_number) LIKE :uhidNumber ");
				}

		      if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0) {
				  sb.append("  and lbsampledtls.assign_unit  =:assignUnit");
			  }
		      sb.append(" order by lbsampledtls.updated_date asc ");
		      
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
				createdQuery.setParameterList("visitTypes", visitTypes);
			}

			if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()){
				createdQuery.setParameterList("subDepts", subDeptIds);
			}

			if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()){
				createdQuery.setParameterList("testTypes", testTypes);
			}

			if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0){
				createdQuery.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId());
			}

			if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0){
				createdQuery.setParameter("labSampleId", searchDto.getLabSampleId());
			}

			if(searchDto.getSampleNo()!=null && searchDto.getSampleNo()!="") {
				createdQuery.setParameter("sampleNo", "%" + searchDto.getSampleNo().trim() + "%");
			}

			if (searchDto.getUhidNumber() != "" && searchDto.getUhidNumber()!=null) {
				createdQuery.setParameter("uhidNumber", "%" + searchDto.getUhidNumber().toLowerCase() + "%");
			}

			if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0){
				createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());
			}

		      List<SampleAcceptancePendingDto> list  = createdQuery
		    		  .setParameter("orgId", searchDto.getOrgId())
		    		  .setParameter("orgUnitId", searchDto.getOrgUnitId())
						.setParameterList("sampleStatusIds", statusId)
						.setParameter("deptId", searchDto.getDeptId())
						.setParameter("subDeptId", searchDto.getSubDeptId())
						 .setFirstResult(searchDto.getOffset()!= null ? searchDto.getOffset() : 0)
						  .setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
						.setResultTransformer(Transformers.aliasToBean(SampleAcceptancePendingDto.class)).list();


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

	@Override
	public Response sensitivityTestingListListSearchCount(SearchDto searchDto) throws ApplicationException {
		try {
			
			
			List<LabConfigurationDto> list1=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LAB_CONFIG_LIST")
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("unitId", searchDto.getOrgUnitId())
					.setResultTransformer(Transformers.aliasToBean(LabConfigurationDto.class)).list();
	    	
	    	 List<Integer> statusId = new ArrayList<Integer>();
			    
	    	 	if(list1.get(0).getMicroscopicExaminationApplicable() == 'Y') {
					
					statusId = SENSITIVITY_SAMPLE_STATUS_IDS;
					
				}else if(list1.get(0).getBiochemicalTestingApplicable() == 'Y') {
					
					statusId = MICROSCOPI_EXA_SAMPLE_STATUS_IDS;
					
				}else if(list1.get(0).getMacroscopicExaminationApplicable() == 'Y') {
					
					statusId = BIOCHEMICAL_SAMPLE_STATUS_IDS;
					
				}else if(list1.get(0).getIncubationMasterApplicable() == 'Y') {
					
					statusId = Arrays.asList(INCUBATION_OBJSERVATION_COMPLETED);
					
				}else  if(list1.get(0).getIsWorklistDepartment() == 'Y') {
					
					statusId = MICRO_INCUBATION_SAMPLE_STATUS_IDS;
					
				}else if(list1.get(0).getIsAcceptanceInDepartment() == 'Y') {
					
					statusId = Arrays.asList(SAMPLE_ACCEPTED);
					
				}else if(list1.get(0).getIsSampleAcceptanceApplicable() == 'Y') {
					
					statusId = Arrays.asList(SAMPLE_SEND_TO_DEPT);
					
				}else if (list1.get(0).getIsSendToCrApplicable() != null && list1.get(0).getIsSendToCrApplicable() == 'Y') {
								
					statusId = Arrays.asList(SAMPLE_SEND_TO_CR);
					
				}else {
					
					statusId = Arrays.asList(SAMPLE_COLLECTED);
				}


		      String query = sessionFactory.getCurrentSession()
		          .getNamedQuery("GET_SENSITIVITY_TESTING_LIST").getQueryString().toString();

		      StringBuilder sb = new StringBuilder(query);

		      if (searchDto.getFromDt() != null ) {
		    	  sb.append( " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt ");
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
		      sb.append(" order by lbsampledtls.updated_date asc ");
		      
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

			sb.append(" order by lbsampledtls.updated_date asc ");

		      List<SampleAcceptancePendingDto> list  = createdQuery
		    		  .setParameter("orgId", searchDto.getOrgId())
		    		  .setParameter("orgUnitId", searchDto.getOrgUnitId())
					  .setParameterList("sampleStatusIds", statusId)
					  .setParameter("deptId", searchDto.getDeptId())
					  .setParameter("subDeptId", searchDto.getSubDeptId())
					  .setResultTransformer(Transformers.aliasToBean(SampleAcceptancePendingDto.class)).list();


		      if (list.isEmpty()) {
		        return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.",
		        		null, 0);
		      } else {
		        return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, null,
		        		list.size());
		      }


		    } catch (Exception e) {
		      logger.error("Exection", e);
		      throw e;
		    }

	}

	@Override
	public Response updateAmendLogs(SensitivityTestResultMasterDto listSensitivityTestResultMasterDto)
			throws ApplicationException {

		try {
				if(listSensitivityTestResultMasterDto.getAmend() == true) {
					sessionFactory.getCurrentSession().createSQLQuery(" update lab.t_sesitivity_test_result  set "
		        			+ " is_deleted ='Y' where lab_sample_dtls_id =:labSampleDtlsId ")
							.setParameter("labSampleDtlsId", listSensitivityTestResultMasterDto.getLabSampleDtlsId())
							.executeUpdate();
				}
			
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
		
	
	}

	@Override
	public Response saveCultureTest(TMicroIncubationMasterDto tMicroIncubationMasterDto,List<SensitivityTestResultDetailsMasterDto> sensitivityTestResultDetailsMasterDto)
			throws ApplicationException {

		  try 
	      {
			  SensitivityTestResultMaster master = new SensitivityTestResultMaster();
			  
			  master.setCheckTest('Y');
			  master.setCreatedBy(tMicroIncubationMasterDto.getCreatedBy());
			  master.setCreatedDate(TimeZoneComponent.getDateByzoneLong(tMicroIncubationMasterDto.getOrgUnitId()));
			  master.setIsDeleted('N');
			  master.setLabSampleDtlsId(tMicroIncubationMasterDto.getLabSampleDtlsId());
			  master.setOrgId(tMicroIncubationMasterDto.getOrgId());
			  master.setOrgUnitId(tMicroIncubationMasterDto.getOrgUnitId());
			  master.setReportReleaseBy(tMicroIncubationMasterDto.getCreatedBy());
			  master.setUpdatedBy(tMicroIncubationMasterDto.getCreatedBy());
			  master.setUpdatedDate(TimeZoneComponent.getDateByzoneLong(tMicroIncubationMasterDto.getOrgUnitId()));
			  if(tMicroIncubationMasterDto.getSensitivityResultId() != null){
				  master.setSensitivityResultId(tMicroIncubationMasterDto.getSensitivityResultId());
				  update(master);
			  }else{
				  master = save(master);
			  }
			  
			  
			  for (SensitivityTestResultDetailsMasterDto dto : sensitivityTestResultDetailsMasterDto) {
				
				  SensitivityTestResultDetailsMaster detDto = new SensitivityTestResultDetailsMaster();
				  
				  detDto.setCreatedBy(tMicroIncubationMasterDto.getCreatedBy());
				  detDto.setCreatedDate(TimeZoneComponent.getDateByzoneLong(tMicroIncubationMasterDto.getOrgUnitId()));
				 
				  detDto.setOrganismGroupId(dto.getOrganismGroupId());
				  detDto.setOrganismId(dto.getOrganismId());
				  detDto.setOrgId(tMicroIncubationMasterDto.getOrgId());
				  detDto.setOrgUnitId(tMicroIncubationMasterDto.getOrgUnitId());
				  detDto.setUpdatedBy(tMicroIncubationMasterDto.getCreatedBy());
				  detDto.setUpdatedDate(TimeZoneComponent.getDateByzoneLong(tMicroIncubationMasterDto.getOrgUnitId()));
				  detDto.setSensitivityResultId(master.getSensitivityResultId());
				  
				  if(dto.getSesitivityResultDetailsId() != null ){
					  
					  detDto.setSesitivityResultDetailsId(dto.getSesitivityResultDetailsId());
					  detDto.setIsDeleted(dto.getIsDeleted());
					  sessionFactory.getCurrentSession().update(detDto); 
					  
				  }else{
					  detDto.setIsDeleted('N');
					  sessionFactory.getCurrentSession().save(detDto); 
				  }
				  
				  
			  }
			  
			  
			  return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, null,  master);
			  
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
	public Response removeSensitivityTestResultSubDetails(Integer sesitivityResultSubDetailsId, Integer orgId,
			Integer orgUnitId) {
		
		try {
			 Integer updatedNumber = sessionFactory.getCurrentSession()
			.getNamedQuery("UPDATE_SENSITIVITY_EXAMINATION_SUB_DETAILS")
			.setParameter("sesitivityResultSubDetailsId", sesitivityResultSubDetailsId)
			.setParameter("orgId", orgId)
  		  	.setParameter("orgUnitId", orgUnitId)
			.executeUpdate();
			 
			if (updatedNumber > 0) {
				return new Response(SUCCESS, SUCCESS_200_MESSAGE, " Record removed successfully.",
		        		null, sesitivityResultSubDetailsId);
				
			}
			else {
				return new Response(ERROR, ERR_204_MESSAGE, "No Content found.",
		        		null, sesitivityResultSubDetailsId);
			}
			 
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response removeSensitivityTestResultDetails(Integer sesitivityResultDetailsId, Integer sensitivityResultId,
			Integer orgId, Integer orgUnitId) {
		try{
			Integer updatedNumber = sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_SENSITIVITY_TEST_RESULT_DETAILS_MASTER")
					.setParameter("sesitivityResultDetailsId", sesitivityResultDetailsId)
					.setParameter("sensitivityResultId", sensitivityResultId)
					.setParameter("orgId", orgId)
		  		  	.setParameter("orgUnitId", orgUnitId)
					.executeUpdate();
			if (updatedNumber > 0) {
				return new Response(SUCCESS, SUCCESS_200_MESSAGE, "Antibiotic And Organism Group Removed Successfully.",
		        		null, sesitivityResultDetailsId);
				
			}
			else {
				return new Response(ERROR, ERR_204_MESSAGE, "No Content found.",
		        		null, sesitivityResultDetailsId);
			}
		} catch (Exception e){
			logger.error("Exception in removeSensitivityTestResultDetails", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	

}

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

import com.param.entity.lis.micro.TMicroIncubationDetailsMaster;
import com.param.entity.lis.micro.TMicroIncubationMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.common.StringCommonMethods;
import com.param.lis.microbiology.transaction.dto.MicrobioParamDto;
import com.param.lis.microbiology.transaction.dto.SensitivityTestResultDetailsMasterDto;
import com.param.lis.microbiology.transaction.dto.TMicroIncubationMasterDto;
import com.param.lis.transaction.dto.SampleAcceptancePendingDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class IncubationObservationDaoImpl extends GenericDao<TMicroIncubationMaster, Integer>
		implements IincubationObservationDao, ICommonConstants, ITransactionConstants, IError {

	@Autowired
	private SessionFactory sessionFactory;



	@Autowired
	private DozerBeanMapper mapper;

	public IncubationObservationDaoImpl() {
		super(TMicroIncubationMaster.class);
	}

	final static Logger logger = Logger.getLogger(IncubationObservationDaoImpl.class);

	@Override
	public Response getIncubationObservationList(MicrobioParamDto microbioParamDto) throws ApplicationException {
		List<SampleAcceptancePendingDto> listMicrobioResultEntryMasterDto = null;
		try {
			
			String query = sessionFactory.getCurrentSession().getNamedQuery("INCUBATION_OBJSERVATION_WORKLIST").getQueryString();
			StringBuffer sb= new StringBuffer(query);
		    sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:currentDate ");
		    sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:currentDate ");
		    sb.append(" order by lbsampledtls.updated_date asc ");
			
		    List<Integer> statusId = new ArrayList<Integer>();
		    
		    if(microbioParamDto.getIsWorklistDepartment() == 'Y') {
				
		    	statusId = Arrays.asList(DURING_OBJSERVATION,SEND_FOR_INCUBATION_OBERVATION);
				
			}else if(microbioParamDto.getIsAcceptanceInDepartment() == 'Y') {
				
				statusId = Arrays.asList(SAMPLE_ACCEPTED,DURING_OBJSERVATION);
				
			}else if(microbioParamDto.getIsSampleAcceptanceApplicable() == 'Y') {
				
				statusId = Arrays.asList(SAMPLE_SEND_TO_DEPT,DURING_OBJSERVATION);
				
			}else if (microbioParamDto.getIsSendToCrApplicable() != null && microbioParamDto.getIsSendToCrApplicable() == 'Y') {
							
				statusId = Arrays.asList(SAMPLE_SEND_TO_CR,DURING_OBJSERVATION);
				
			}else {
				
				statusId = Arrays.asList(SAMPLE_COLLECTED,DURING_OBJSERVATION);
			}

		    
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
	public Response getIncubationObservationListCount(MicrobioParamDto microbioParamDto) throws ApplicationException {
		try {
			
			String query = sessionFactory.getCurrentSession().getNamedQuery("INCUBATION_OBJSERVATION_WORKLIST_COUNT").getQueryString();
			StringBuffer sb= new StringBuffer(query);
		    sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:currentDate ");
		    sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:currentDate ");
			
		    List<Integer> statusId = new ArrayList<Integer>();
		    
		    if(microbioParamDto.getIsWorklistDepartment() == 'Y') {
				
		    	statusId = Arrays.asList(DURING_OBJSERVATION,SEND_FOR_INCUBATION_OBERVATION);
				
			}else if(microbioParamDto.getIsAcceptanceInDepartment() == 'Y') {
				
				statusId = Arrays.asList(SAMPLE_ACCEPTED,DURING_OBJSERVATION);
				
			}else if(microbioParamDto.getIsSampleAcceptanceApplicable() == 'Y') {
				
				statusId = Arrays.asList(SAMPLE_SEND_TO_DEPT,DURING_OBJSERVATION);
				
			}else if (microbioParamDto.getIsSendToCrApplicable() != null && microbioParamDto.getIsSendToCrApplicable() == 'Y') {
							
				statusId = Arrays.asList(SAMPLE_SEND_TO_CR,DURING_OBJSERVATION);
				
			}else {
				
				statusId = Arrays.asList(SAMPLE_COLLECTED,DURING_OBJSERVATION);
			}

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
	public Response saveIncubationTrasaction(TMicroIncubationMasterDto tMicroIncubationMasterDto)
			throws ApplicationException 
	{
	  try {

	    if(tMicroIncubationMasterDto!=null)
	    {
	      TMicroIncubationMaster tMicroIncubationMaster = mapper.map(tMicroIncubationMasterDto,
	            TMicroIncubationMaster.class, "TMicroIncubationMasterDtoToTMicroIncubationMaster");
	        TMicroIncubationMaster tMicroIncubationMst = save(tMicroIncubationMaster);
	        for (Iterator iterator = tMicroIncubationMaster.getListTMicroIncubationDetailsMaster().iterator(); iterator.hasNext();) 
	        {
	          TMicroIncubationDetailsMaster tMicroIncubationDetailsMaster = (TMicroIncubationDetailsMaster) iterator.next();
	          tMicroIncubationDetailsMaster.settIncubationId(tMicroIncubationMst.gettIncubationId());
	          sessionFactory.getCurrentSession().save(tMicroIncubationDetailsMaster);
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
    public Response updateIncubationTransaction(TMicroIncubationMasterDto tMicroIncubationMasterDto)
            throws ApplicationException 
    {
      try {

        if(tMicroIncubationMasterDto!=null)
        {
          TMicroIncubationMaster tMicroIncubationMaster = mapper.map(tMicroIncubationMasterDto,
                TMicroIncubationMaster.class, "TMicroIncubationMasterDtoToTMicroIncubationMaster");
            TMicroIncubationMaster tMicroIncubationMst = update(tMicroIncubationMaster);
            for (Iterator iterator = tMicroIncubationMaster.getListTMicroIncubationDetailsMaster().iterator(); iterator.hasNext();) 
            {
              TMicroIncubationDetailsMaster tMicroIncubationDetailsMaster = (TMicroIncubationDetailsMaster) iterator.next();
              tMicroIncubationDetailsMaster.settIncubationId(tMicroIncubationMst.gettIncubationId());
              sessionFactory.getCurrentSession().update(tMicroIncubationDetailsMaster);
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
	public Response getIncubationDetails(Integer tIncubationId,Integer orgId,Integer orgUnitId) throws ApplicationException {
		  try 
	      {
	          TMicroIncubationMaster tMicroIncubationMaster = findById(tIncubationId); 
	          if(tMicroIncubationMaster!=null)
	          {
	            TMicroIncubationMasterDto tMicroIncubationMasterDto = 
	                mapper.map(tMicroIncubationMaster, TMicroIncubationMasterDto.class, "TMicroIncubationMasterDtoToTMicroIncubationMaster");
	            return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, tMicroIncubationMasterDto);
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
	public Response getIncubationObservationListSearch(SearchDto searchDto) throws ApplicationException {
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




			String query = sessionFactory.getCurrentSession()
		          .getNamedQuery("INCUBATION_OBJSERVATION_WORKLIST").getQueryString().toString();

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
		    	  sb.append(" AND lbsampledtls.lab_sample_dtls_id =:labSampleDtlsId ");

		      if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
		          sb.append("  and lbsampledtls.assign_unit  =:assignUnit ");
		      
		      if(searchDto.getSampleNo()!=null && searchDto.getSampleNo()!="") {
		    	  sb.append(" AND CAST(sample_mst.sample_no  AS varchar )like :sampleNo ");
		      }
		      if (searchDto.getUhidNumber() != "" && searchDto.getUhidNumber()!=null) {
					sb.append(" AND lower(pati_mst.uhid_number) LIKE :uhidNumber ");
				}
		      sb.append(" order by lbsampledtls.updated_date asc ");
		      
		      Query createdQuery = sessionFactory.getCurrentSession()
		          .createSQLQuery(sb.toString());

		      List<Integer> statusId = new ArrayList<Integer>();
			    
			    if(searchDto.getIsWorklistDepartment() == 'Y') {
					
					statusId = Arrays.asList(DURING_OBJSERVATION,SEND_FOR_INCUBATION_OBERVATION);
					
				}else if(searchDto.getIsAcceptanceInDepartment() == 'Y') {
					
					statusId = Arrays.asList(SAMPLE_ACCEPTED,DURING_OBJSERVATION);
					
				}else if(searchDto.getIsSampleAcceptanceApplicable() == 'Y') {
					
					statusId = Arrays.asList(SAMPLE_SEND_TO_DEPT,DURING_OBJSERVATION);
					
				}else if (searchDto.getIsSendToCrApplicable() != null && searchDto.getIsSendToCrApplicable() == 'Y') {
								
					statusId = Arrays.asList(SAMPLE_SEND_TO_CR,DURING_OBJSERVATION);
					
				}else {
					
					statusId = Arrays.asList(SAMPLE_COLLECTED,DURING_OBJSERVATION);
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
			if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0){
				createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());
			}
			if(searchDto.getSampleNo()!=null && searchDto.getSampleNo()!="") {
				createdQuery.setParameter("sampleNo", "%" + searchDto.getSampleNo().trim() + "%");
			}
			if (searchDto.getUhidNumber() != "" && searchDto.getUhidNumber()!=null) {
				createdQuery.setParameter("uhidNumber", "%" + searchDto.getUhidNumber().toLowerCase() + "%");
			}
		      List<SampleAcceptancePendingDto> list  = createdQuery
		    		    .setParameter("orgId", searchDto.getOrgId())
						.setParameter("orgUnitId", searchDto.getOrgUnitId())
						.setParameterList("sampleStatusIds", statusId)
						.setParameter("deptId", searchDto.getDeptId())
						.setParameter("subDeptId", searchDto.getSubDeptId())
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
	public Response getCultureTestResult(TMicroIncubationMasterDto tMicroIncubationMasterDto)
			throws ApplicationException {

		try {
			
			List<SensitivityTestResultDetailsMasterDto> listObj = sessionFactory.getCurrentSession()
					.createSQLQuery("  select tstrd.sesitivity_result_details_id as \"sesitivityResultDetailsId\" , "
							+ "tstrd.sensitivity_result_id as \"sensitivityResultId\" , "
							+ "tstrd.organism_group_id as \"organismGroupId\" ,"
							+ "tstrd.organism_id as \"organismId\" ,"
							+ "mogm.organism_group_name as \"organismGroupName\" ,"
							+ "mom.organism_name as \"organismName\" "
							+ " from "
							+ " lab.t_sesitivity_test_result_details tstrd "
							+ "inner join lab.t_sesitivity_test_result tstr on tstr.sensitivity_result_id = tstrd.sensitivity_result_id "
							+ "inner join lab.m_organism_group_master mogm on tstrd.organism_group_id  = mogm.organism_group_id "
							+ "inner join lab.m_organism_master mom on mom.organism_id  = tstrd.organism_id "
							+ "where tstr.lab_sample_dtls_id  =:labSampleDtlsId and tstrd.is_deleted  = 'N'")
						.setParameter("labSampleDtlsId", tMicroIncubationMasterDto.getLabSampleDtlsId())
					.setResultTransformer(Transformers.aliasToBean(SensitivityTestResultDetailsMasterDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_200, null, listObj, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	
	}

	


	
}

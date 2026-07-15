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

import com.param.entity.lis.micro.MicroscopicExaminationDetailsMaster;
import com.param.entity.lis.micro.MicroscopicExaminationMaster;
import com.param.entity.lis.micro.MicroscopicExaminationSubDetailsMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.unit.dto.LabConfigurationDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.common.StringCommonMethods;
import com.param.lis.microbiology.transaction.dto.MicrobioParamDto;
import com.param.lis.microbiology.transaction.dto.MicroscopicExaminationDetailsMasterDto;
import com.param.lis.microbiology.transaction.dto.MicroscopicExaminationMasterDto;
import com.param.lis.microbiology.transaction.dto.MicroscopicExaminationSubDetailsMasterDto;
import com.param.lis.transaction.dto.SampleAcceptancePendingDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class MicroscopicExaminationDaoImpl extends GenericDao<MicroscopicExaminationMaster, Integer>
    implements IMicroscopicExaminationDao, ICommonConstants, ITransactionConstants, IError {


  @Autowired
  private SessionFactory sessionFactory;

  @Autowired
  private DozerBeanMapper mapper;

  @Autowired
  private ImicroscopicExaminationDetailsMaster imicroscopicExaminationDetailsMaster;

  public MicroscopicExaminationDaoImpl() {
    super(MicroscopicExaminationMaster.class);
  }

  final static Logger logger = Logger.getLogger(MicroscopicExaminationDaoImpl.class);

  @Override
  public Response getMicroScopicExaminationList(MicrobioParamDto microbioParamDto)
      throws ApplicationException {
    List<SampleAcceptancePendingDto> listMicrobioResultEntryMasterDto = null;
    try {
    	
    	List<LabConfigurationDto> list1=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_LAB_CONFIG_LIST")
				.setParameter("orgId", microbioParamDto.getOrgId())
				.setParameter("unitId", microbioParamDto.getOrgUnitId())
				.setResultTransformer(Transformers.aliasToBean(LabConfigurationDto.class)).list();
    	
   	 List<Integer> statusId = new ArrayList<Integer>();
	    
	 	if(list1.getFirst().getBiochemicalTestingApplicable() == 'Y') {
			
			statusId = MICROSCOPI_EXA_SAMPLE_STATUS_IDS;
			
		}else if(list1.getFirst().getMacroscopicExaminationApplicable() == 'Y') {
			
			statusId = Arrays.asList(MACROSCOPIC_EXAMINATION_COMPLETED,MICROSCOPIC_EXAMINATION); 
			
		}else if(list1.getFirst().getIncubationMasterApplicable() == 'Y') {
			
			statusId = Arrays.asList(INCUBATION_OBJSERVATION_COMPLETED,MICROSCOPIC_EXAMINATION);
			
		}else  if(list1.getFirst().getIsWorklistDepartment() == 'Y') {
			
			statusId = Arrays.asList(SEND_FOR_INCUBATION_OBERVATION,MICROSCOPIC_EXAMINATION);
			
		}else if(list1.getFirst().getIsAcceptanceInDepartment() == 'Y') {
			
			statusId = Arrays.asList(SAMPLE_ACCEPTED,MICROSCOPIC_EXAMINATION);
			
		}else{
			
			statusId = Arrays.asList(SAMPLE_COLLECTED,MICROSCOPIC_EXAMINATION);
		}
    	
    	String query = sessionFactory.getCurrentSession().getNamedQuery("MACROSCOPIC_EXAMINATION_WORKLIST").getQueryString();
		StringBuffer sb= new StringBuffer(query);
	    sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') >=:currentDate ");
	    sb.append(" AND to_char(order_master.order_date,'YYYY-MM-DD') <=:currentDate ");
	    sb.append(" order by order_master.order_date asc ");
    	
      listMicrobioResultEntryMasterDto = sessionFactory.getCurrentSession()
          .createSQLQuery(sb.toString())
          .setParameter("orgId", microbioParamDto.getOrgId())
          .setParameter("orgUnitId", microbioParamDto.getOrgUnitId())
          .setParameterList("sampleStatusIds", statusId)
          .setParameter("deptId", microbioParamDto.getDeptId())
          .setParameter("subDeptId", microbioParamDto.getSubDeptId())
          .setParameter("currentDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(microbioParamDto.getOrgUnitId()),"yyyy-MM-dd"))
          .setFirstResult(microbioParamDto.getOffset() != null ? microbioParamDto.getOffset() : 0)
          .setMaxResults(
              microbioParamDto.getRecordPerPage() != null ? microbioParamDto.getRecordPerPage()
                  : 10)
          .setResultTransformer(Transformers.aliasToBean(SampleAcceptancePendingDto.class)).list();
      return new Response(SUCCESS, SUCCESS_200, null, listMicrobioResultEntryMasterDto, null);
    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }

  @Override
  public Response getMicroScopicExaminationListCount(MicrobioParamDto microbioParamDto)
      throws ApplicationException {
    try {
    	
    	List<LabConfigurationDto> list1=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_LAB_CONFIG_LIST")
				.setParameter("orgId", microbioParamDto.getOrgId())
				.setParameter("unitId", microbioParamDto.getOrgUnitId())
				.setResultTransformer(Transformers.aliasToBean(LabConfigurationDto.class)).list();
    	
    	 List<Integer> statusId = new ArrayList<Integer>();
		    
    	 	if(list1.getFirst().getBiochemicalTestingApplicable() == 'Y') {
				
				statusId = MICROSCOPI_EXA_SAMPLE_STATUS_IDS;
				
			}else if(list1.getFirst().getMacroscopicExaminationApplicable() == 'Y') {
				
				statusId = Arrays.asList(MACROSCOPIC_EXAMINATION_COMPLETED,MICROSCOPIC_EXAMINATION); 
				
			}else if(list1.getFirst().getIncubationMasterApplicable() == 'Y') {
				
				statusId = Arrays.asList(INCUBATION_OBJSERVATION_COMPLETED,MICROSCOPIC_EXAMINATION);
				
			}else  if(list1.getFirst().getIsWorklistDepartment() == 'Y') {
				
				statusId = Arrays.asList(SEND_FOR_INCUBATION_OBERVATION,MICROSCOPIC_EXAMINATION);
				
			}else if(list1.getFirst().getIsAcceptanceInDepartment() == 'Y') {
				
				statusId = Arrays.asList(SAMPLE_ACCEPTED,MICROSCOPIC_EXAMINATION);
				
			}else{
				
				statusId = Arrays.asList(SAMPLE_COLLECTED,MICROSCOPIC_EXAMINATION);
			}
    	
    	String query = sessionFactory.getCurrentSession().getNamedQuery("MACROSCOPIC_EXAMINATION_WORKLIST_COUNT").getQueryString();
		StringBuffer sb= new StringBuffer(query);
		if (microbioParamDto.getFromDate() != null ) 
	      {
	    	  sb.append( " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDate ");
	      }

	      if (microbioParamDto.getToDate() != null ) {
	    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDate ");
	      }
      BigInteger incubationObservationListCount = (BigInteger) sessionFactory.getCurrentSession()
    	  .createSQLQuery(sb.toString())
          .setParameter("orgId", microbioParamDto.getOrgId())
          .setParameterList("sampleStatusIds", statusId)
          .setParameter("deptId", microbioParamDto.getDeptId())
          .setParameter("subDeptId", microbioParamDto.getSubDeptId())
          .setParameter("fromDate", microbioParamDto.getFromDate())
          .setParameter("toDate", microbioParamDto.getToDate())
          .setParameter("orgUnitId", microbioParamDto.getOrgUnitId()).uniqueResult();
      if (incubationObservationListCount.compareTo(BigInteger.ZERO) == 1) {
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null,
            incubationObservationListCount);
      } else {
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, BigInteger.ZERO);
      }

    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }

  @Override
  public Response saveMicroScopicExamination(
      MicroscopicExaminationMasterDto microscopicExaminationMasterDto)
      throws ApplicationException {

    try {
      MicroscopicExaminationMaster microscopicExaminationMaster =
          microscopicExaminationMasterDto != null
              ? mapper.map(microscopicExaminationMasterDto, MicroscopicExaminationMaster.class, "MicroscopicExaminationMasterDtoToMicroscopicExaminationMaster")
              : null;
      MicroscopicExaminationMaster micrscopiceExam = save(microscopicExaminationMaster);
      if (micrscopiceExam.getListMicroscopicExaminationDetailsMasters() != null) {
        for (Iterator iterator =
            micrscopiceExam.getListMicroscopicExaminationDetailsMasters().iterator(); iterator
                .hasNext();) {
          MicroscopicExaminationDetailsMaster microscopicExaminationDetailsMaster =
              (MicroscopicExaminationDetailsMaster) iterator.next();
          microscopicExaminationDetailsMaster
              .setMicroscopicExaId(micrscopiceExam.getMicroscopicExaId());
          microscopicExaminationDetailsMaster.setCreatedDate(TimeZoneComponent.getDateByzone(microscopicExaminationDetailsMaster.getOrgUnitId()).getTime());
          
          Response<MicroscopicExaminationDetailsMaster, Integer> microscopicExaminationMstRes =    imicroscopicExaminationDetailsMaster
              .savemicroscopicExaminationDetailsMaster(microscopicExaminationDetailsMaster);
          if(microscopicExaminationMstRes.getCode().equals(SUCCESS_200))
          {
            continue;
          }
          else {
            return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
          }
        }
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, null);

      } else {
        return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
      }
    } catch (HibernateException er) {
      logger.error("Exection", er);throw er;
    } catch (Exception e) {
      logger.error("Exception", e);throw e;
    }


  }

  @Override
  public Response updateMicroScopicExamination(
      MicroscopicExaminationMasterDto microscopicExaminationMasterDto)
      throws ApplicationException {

      try {
        MicroscopicExaminationMaster microscopicExaminationMaster =
            microscopicExaminationMasterDto != null
                ? mapper.map(microscopicExaminationMasterDto, MicroscopicExaminationMaster.class, "MicroscopicExaminationMasterDtoToMicroscopicExaminationMaster")
                : null;
        MicroscopicExaminationMaster micrscopiceExam = update(microscopicExaminationMaster);
        if (micrscopiceExam.getListMicroscopicExaminationDetailsMasters() != null) 
        {
          for (Iterator iterator =  micrscopiceExam.getListMicroscopicExaminationDetailsMasters().iterator(); iterator
                  .hasNext();) {
            MicroscopicExaminationDetailsMaster microscopicExaminationDetailsMaster =
                (MicroscopicExaminationDetailsMaster) iterator.next();
            microscopicExaminationDetailsMaster
                .setMicroscopicExaId(micrscopiceExam.getMicroscopicExaId());
            if(microscopicExaminationDetailsMaster.getExaminationDetailsId()==0)
            {
              microscopicExaminationDetailsMaster.setCreatedDate(TimeZoneComponent.getDateByzone(microscopicExaminationDetailsMaster.getOrgUnitId()).getTime());
              Response<MicroscopicExaminationSubDetailsMaster, Integer> MicroscopicExaminationSubDetailsMasterRes =  imicroscopicExaminationDetailsMaster
                  .savemicroscopicExaminationDetailsMaster(microscopicExaminationDetailsMaster);
              if(MicroscopicExaminationSubDetailsMasterRes.getCode().equals(SUCCESS_200))
              {
                continue;
              }
              else {
                return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
              }
            }
            else {
              microscopicExaminationDetailsMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(microscopicExaminationDetailsMaster.getOrgUnitId()).getTime());
              Response<MicroscopicExaminationSubDetailsMaster, Integer> MicroscopicExaminationSubDetailsMasterRes =imicroscopicExaminationDetailsMaster
                  .updatemicroscopicExaminationDetailsMaster(microscopicExaminationDetailsMaster);
              if(MicroscopicExaminationSubDetailsMasterRes.getCode().equals(SUCCESS_200))
              {
                continue;
              }
              else {
                return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
              }
            }
           
          }
          return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, null);

        } else {
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
  public Response getMicroScopicExamination(Integer labSampleDtlsId, Integer orgId, Integer orgUnitId)
      throws ApplicationException {
    try {
      List<MicroscopicExaminationMasterDto> listMicroscopicExaminationMasterDto = new ArrayList<>();
      List<MicroscopicExaminationMasterDto> listMicroscopicExaminationMstDto =
          (List<MicroscopicExaminationMasterDto>) sessionFactory.getCurrentSession()
              .getNamedQuery("GET_MICRO_SCOPIC_EXAMINATION_MASTER_BY_RESULT_ID")
              .setParameter("labSampleDtlsId", labSampleDtlsId)
              .setParameter("orgId", orgId)
              .setParameter("orgUnitId", orgUnitId)
              .setResultTransformer(Transformers.aliasToBean(MicroscopicExaminationMasterDto.class))
              .list();

      for (Iterator iterator = listMicroscopicExaminationMstDto.iterator(); iterator.hasNext();) {
        MicroscopicExaminationMasterDto microscopicExaminationMasterDto =
            (MicroscopicExaminationMasterDto) iterator.next();
        if (microscopicExaminationMasterDto != null) {


          List<MicroscopicExaminationDetailsMasterDto> listMicroscopicExaminationDetailsDtoMst =
              (List<MicroscopicExaminationDetailsMasterDto>) sessionFactory.getCurrentSession()
                  .getNamedQuery("GET_MICRO_SCOPIC_EXAMINATION_DETAILS")
                  .setParameter("microscopicExaId",
                      microscopicExaminationMasterDto.getMicroscopicExaId())
                  .setParameter("orgId", orgId).setParameter("orgUnitId", orgUnitId)
                  .setResultTransformer(
                      Transformers.aliasToBean(MicroscopicExaminationDetailsMasterDto.class))
                  .list();

          if (listMicroscopicExaminationDetailsDtoMst != null) {
            List<MicroscopicExaminationDetailsMasterDto> listMicroscopicExaminationDetailsMasterDto =
                new ArrayList<>();
            for (Iterator iterator2 = listMicroscopicExaminationDetailsDtoMst.iterator(); iterator2
                .hasNext();) {

              MicroscopicExaminationDetailsMasterDto microscopicExaminationDetailsMasterDto =
                  (MicroscopicExaminationDetailsMasterDto) iterator2.next();

              List<MicroscopicExaminationSubDetailsMasterDto> listMicroscopicExaminationSubDetailsMaster =
                  (List<MicroscopicExaminationSubDetailsMasterDto>) sessionFactory
                      .getCurrentSession().getNamedQuery("GET_MICRO_SCOPIC_EXAMINATION_SUB_DETAILS")
                      .setParameter("examinationDetailsId",
                          microscopicExaminationDetailsMasterDto.getExaminationDetailsId())
                      .setParameter("orgId", orgId).setParameter("orgUnitId", orgUnitId)
                      .setResultTransformer(
                          Transformers.aliasToBean(MicroscopicExaminationSubDetailsMasterDto.class))
                      .list();

              if (listMicroscopicExaminationSubDetailsMaster != null) {
                List<MicroscopicExaminationSubDetailsMasterDto> listMicroscopicExaminationSubDetailsMasterDto =
                    new ArrayList<>();
                for (Iterator iterator3 =
                    listMicroscopicExaminationSubDetailsMaster.iterator(); iterator3.hasNext();) {
                  MicroscopicExaminationSubDetailsMasterDto microscopicExaminationSubDetailsMasterDto =
                      (MicroscopicExaminationSubDetailsMasterDto) iterator3.next();

                  listMicroscopicExaminationSubDetailsMasterDto
                      .add(microscopicExaminationSubDetailsMasterDto);
                }
                microscopicExaminationDetailsMasterDto
                    .setListMicroscopicExaminationSubDetailsMaster(
                        listMicroscopicExaminationSubDetailsMasterDto);
                listMicroscopicExaminationDetailsMasterDto
                    .add(microscopicExaminationDetailsMasterDto);
              }

            }
            microscopicExaminationMasterDto.setListMicroscopicExaminationDetailsMasters(
                listMicroscopicExaminationDetailsMasterDto);
            listMicroscopicExaminationMasterDto.add(microscopicExaminationMasterDto);
          }

        }

      }
      return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE,
          listMicroscopicExaminationMasterDto, null);

    } catch (HibernateException er) {
      logger.error("Exection", er);throw er;
    } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
    }
  }

@Override
public Response getMicroScopicExaminationListSearch(SearchDto searchDto) throws ApplicationException {
	try {

		
		List<LabConfigurationDto> list1=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_LAB_CONFIG_LIST")
				.setParameter("orgId", searchDto.getOrgId())
				.setParameter("unitId", searchDto.getOrgUnitId())
				.setResultTransformer(Transformers.aliasToBean(LabConfigurationDto.class)).list();
    	
    	 List<Integer> statusId = new ArrayList<Integer>();
		    
    	 	if(list1.getFirst().getBiochemicalTestingApplicable() == 'Y') {
				
				statusId = MICROSCOPI_EXA_SAMPLE_STATUS_IDS;
				
			}else if(list1.getFirst().getMacroscopicExaminationApplicable() == 'Y') {
				
				statusId = Arrays.asList(MACROSCOPIC_EXAMINATION_COMPLETED,MICROSCOPIC_EXAMINATION); 
				
			}else if(list1.getFirst().getIncubationMasterApplicable() == 'Y') {
				
				statusId = Arrays.asList(INCUBATION_OBJSERVATION_COMPLETED,MICROSCOPIC_EXAMINATION);
				
			}else  if(list1.getFirst().getIsWorklistDepartment() == 'Y') {
				
				statusId = Arrays.asList(SEND_FOR_INCUBATION_OBERVATION,MICROSCOPIC_EXAMINATION);
				
			}else if(list1.getFirst().getIsAcceptanceInDepartment() == 'Y') {
				
				statusId = Arrays.asList(SAMPLE_ACCEPTED,MICROSCOPIC_EXAMINATION);
				
			}else if(list1.getFirst().getIsSampleAcceptanceApplicable() == 'Y') {
				
				statusId = Arrays.asList(SAMPLE_SEND_TO_DEPT,MICROSCOPIC_EXAMINATION);
				
			}else if (list1.getFirst().getIsSendToCrApplicable() != null && list1.getFirst().getIsSendToCrApplicable() == 'Y') {
							
				statusId = Arrays.asList(SAMPLE_SEND_TO_CR,MICROSCOPIC_EXAMINATION);
				
			}else {
				
				statusId = Arrays.asList(SAMPLE_COLLECTED,MICROSCOPIC_EXAMINATION);
			}
		
	      String query = sessionFactory.getCurrentSession()
	          .getNamedQuery("MACROSCOPIC_EXAMINATION_WORKLIST").getQueryString().toString();

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
	          if(searchDto.getUhidNumber()!="") {
		    	  sb.append(" AND lower(pati_mst.uhid_number) LIKE :uhidNumber ");
		      }
		      
		      if(searchDto.getSampleNo()!="") {
		    	  sb.append(" AND lower(sample_mst.sample_no) LIKE :sampleNo ");
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

        if(searchDto.getUhidNumber()!="") {
            createdQuery.setParameter("uhidNumber", "%" + searchDto.getUhidNumber().toLowerCase() + "%");
        }

        if(searchDto.getSampleNo()!="") {
            createdQuery.setParameter("sampleNo", "%" + searchDto.getSampleNo().toLowerCase() + "%");
        }

	      List<SampleAcceptancePendingDto> list  = createdQuery
	    		  .setParameter("orgId", searchDto.getOrgId())
	              .setParameter("orgUnitId", searchDto.getOrgUnitId())
	              .setParameterList("sampleStatusIds", statusId)
	              .setParameter("deptId", searchDto.getDeptId())
	              .setParameter("subDeptId", searchDto.getSubDeptId())
	              .setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
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

}

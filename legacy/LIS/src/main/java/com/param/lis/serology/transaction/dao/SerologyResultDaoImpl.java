package com.param.lis.serology.transaction.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.serology.SerologyResultTestMapper;
import com.param.entity.lis.serology.SerologyTestResult;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.unit.dao.ILabConfigurationDao;
import com.param.global.unit.dto.LabConfigurationDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.common.StringCommonMethods;
import com.param.lis.microbiology.transaction.dto.MicrobioParamDto;
import com.param.lis.serology.dto.SerologyResultTestMapperDto;
import com.param.lis.serology.dto.SerologyTestResultDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.unit.dao.IParameterMasterDao;
import com.param.lis.unit.dto.ParameterMasterDto;
import com.param.opd.coversheet.dto.ICPCSystemDTO;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" ,"unused"})
public class SerologyResultDaoImpl  implements ISerologyResultDao, ICommonConstants, ITransactionConstants, IError {

	
	@Autowired
	SessionFactory sessionFactory;
	

	@Autowired
	ILabConfigurationDao iLabConfigurationDao;
	
	  @Autowired
	  private IParameterMasterDao iParameterMasterDao;
	
	final static Logger logger = Logger.getLogger(SerologyResultDaoImpl.class);
	@Override
	public Response getSerologyResultEntryList(MicrobioParamDto dto) throws ApplicationException {
		List<LabResultMasterDto> listLabResultMasterDto = null;
		try {
			
			  
			
		    String query = sessionFactory.getCurrentSession().getNamedQuery("GET_SEROLOGY_RESULT_ENTRY_LIST").getQueryString();
			StringBuffer sb= new StringBuffer(query);
			 List<Integer> statusIds = new ArrayList<Integer>(); 
		      if(dto.getIsOnHoldParameter() == false) {
		    	  
		        if(dto.getIsAcceptanceInDepartment() == 'Y') {
		   			
		   			statusIds = bioChemStatus;
		   			
		   		}else if(dto.getIsSampleAcceptanceApplicable() == 'Y') {
					
					statusIds = Arrays.asList(SAMPLE_SEND_TO_DEPT);
					
				}else if (dto.getIsSendToCrApplicable() != null && dto.getIsSendToCrApplicable() == 'Y') {
								
					statusIds = Arrays.asList(SAMPLE_SEND_TO_CR);
					
				}else{
		   			
		   			statusIds = Arrays.asList(SAMPLE_COLLECTED,RETESTING);
		   		}
		      }
		    sb.append( " and to_char(order_master.order_date,'YYYY-MM-DD') >= '" + GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getOrgUnitId()),"yyyy-MM-dd") + "' ");
		    sb.append(" and to_char(order_master.order_date,'YYYY-MM-DD') <= '" + GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getOrgUnitId()),"yyyy-MM-dd") + "' ");
			sb.append("  and lbsampledtls.assign_unit  ="+dto.getOrgUnitId());
			 if (dto.getIsOnHoldParameter() == true) {
           	  statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
           	  sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
                 sb.append(" AND lbsampledtls.is_on_hold_parameter =" + dto.getIsOnHoldParameter());
             }
             if (statusIds != null && statusIds.size() > 0 && dto.getIsOnHoldParameter() == false) {
                 sb.append(" and ( lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+") or lbsampledtls.is_on_hold_parameter = true) ");
             }
			sb.append(" ORDER BY  " 
			         + "  lbsampledtls.priority_id,  "
			         + "  order_master.order_date  "  );
			  
		
			
			listLabResultMasterDto = sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
					.setInteger("orgId", dto.getOrgId())
					.setInteger("orgUnitId", dto.getOrgUnitId())
//					.setInteger("sampleStatusId", statusId)
					.setFirstResult(dto.getOffset() != null ? dto.getOffset() : 0)
					.setMaxResults(dto.getRecordPerPage() != null ? dto.getRecordPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_200, null, listLabResultMasterDto, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getSerologyResultEntryListCount(MicrobioParamDto dto) throws ApplicationException {
		try {
			
			 String query = sessionFactory.getCurrentSession().getNamedQuery("GET_SEROLOGY_RESULT_ENTRY_LIST_COUNT").getQueryString();
				StringBuffer sb= new StringBuffer(query);
				sb.append("  and lbsampledtls.assign_unit  ="+dto.getAssignUnit());
				  List<Integer> statusIds = new ArrayList<Integer>(); 
			      if(dto.getIsOnHoldParameter() == false) {
			    	  
				        if(dto.getIsAcceptanceInDepartment() == 'Y') {
				   			
				   			statusIds = bioChemStatus;
				   			
				   		}else if(dto.getIsSampleAcceptanceApplicable() == 'Y') {
							
							statusIds = Arrays.asList(SAMPLE_SEND_TO_DEPT);
							
						}else if (dto.getIsSendToCrApplicable() != null && dto.getIsSendToCrApplicable() == 'Y') {
										
							statusIds = Arrays.asList(SAMPLE_SEND_TO_CR);
							
						}else{
				   			
				   			statusIds = Arrays.asList(SAMPLE_COLLECTED,RETESTING);
				   		}
				      }
				if (dto.getFromDate() != null ) 
			      {
			    	  sb.append( " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >= '" +  dto.getFromDate() + "' ");
			      }

			      if (dto.getToDate() != null ) {
			    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <= '" + dto.getToDate() + "' ");
			      }
			      if (dto.getIsOnHoldParameter() == true) {
	            	  statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
	            	  sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
	                  sb.append(" AND lbsampledtls.is_on_hold_parameter =" + dto.getIsOnHoldParameter());
	              }
	              if (statusIds != null && statusIds.size() > 0 && dto.getIsOnHoldParameter() == false) {
	                  sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
	              }

			BigInteger sampleResultEntryCount = (BigInteger) sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setInteger("orgId", dto.getOrgId())
					.setInteger("orgUnitId", dto.getOrgUnitId()).uniqueResult();
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
	public Response saveResultEntry(SerologyTestResultDto dto) throws ApplicationException {
		try{
			SerologyTestResult master = new SerologyTestResult();
			master.setLabSampleDtlsId(dto.getLabSampleDtlsId());
			
			master.setResultEnterBy(dto.getCreatedBy());
			master.setResultEnterDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getOrgUnitId()));
			master.setManualResultEnterTime(TimeZoneComponent.getDateByzone(dto.getOrgUnitId()));
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getOrgUnitId()));
			master.setOrgId(dto.getOrgId());
			master.setOrgUnitId(dto.getOrgUnitId());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getOrgUnitId()));
			
			
			Integer id = (Integer)sessionFactory.getCurrentSession().save(master);
			return new Response<>(SUCCESS, SUCCESS_200, null, null, id);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response serologyResultTestMapper(SerologyResultTestMapperDto dto) throws ApplicationException {
		try{
			SerologyResultTestMapper master = new SerologyResultTestMapper();
			master.setTechniqueId(dto.getTechniqueId());
			master.setSerologyTestResultId(dto.getSerologyTestResultId());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getOrgUnitId()));
			master.setOrgId(dto.getOrgId());
			master.setOrgUnitId(dto.getOrgUnitId());
			master.setUpdatedBy(dto.getCreatedBy());
			master.setUpdatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getOrgUnitId()));
			master.setParameterId(dto.getParameterId());
			master.setFindingResult(dto.getFirstFinding());
			master.setFindingResultDesc(dto.getFirstFindingDesc());
			master.setFirstFinding(dto.getFirstFinding());
			master.setFirstFindingDesc(dto.getFirstFindingDesc());
            master.setIsAbnormal(dto.getIsAbnormal()!=null ? dto.getIsAbnormal(): 'N');
			
			 com.param.global.common.Response res =  iLabConfigurationDao.getLabConfigurationList(dto.getOrgId(),dto.getOrgUnitId());
		        Character c = null;
		       if(res.getListObject() != null && res.getListObject().size()>0) {
		    	   
		    	   List<LabConfigurationDto> list = res.getListObject() ;
		    	  c = list.get(0).getSkipValidation();
		       }
			
		       if(c.equals('Y')) {
		    	   master.setSecondFinding(dto.getFirstFinding());
					master.setSecondFindingDesc(dto.getFirstFindingDesc());
		          }
			
			master.setThiredFinding(dto.getThiredFinding());
			master.setThirdFindingDesc(dto.getThirdFindingDesc());
			Integer id = (Integer)sessionFactory.getCurrentSession().save(master);
			return new Response<>(SUCCESS, SUCCESS_200, null, null, id);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response searchSerologyResultEntryList(SearchDto dto) throws ApplicationException {
		try {

		      String query = sessionFactory.getCurrentSession()
		          .getNamedQuery("GET_SEROLOGY_RESULT_ENTRY_LIST").getQueryString().toString();

		      StringBuilder sb = new StringBuilder(query);
		      List<Integer> statusIds = new ArrayList<Integer>(); 
		      if(dto.getIsOnHoldParameter() == false) {
		    	  
		        if(dto.getIsAcceptanceInDepartment() == 'Y') {
		   			
		   			statusIds = bioChemStatus;
		   			
		   		}else if(dto.getIsSampleAcceptanceApplicable() == 'Y') {
		   			
		   			statusIds = Arrays.asList(SAMPLE_SEND_TO_DEPT,RETESTING);
		   			
		   		}else if (dto.getIsSendToCrApplicable() != null && dto.getIsSendToCrApplicable() == 'Y') {
		   						
		   			statusIds =Arrays.asList(SAMPLE_SEND_TO_CR,SAMPLE_COLLECTED,RETESTING);
		   			
		   		}else {
		   			
		   			statusIds = Arrays.asList(SAMPLE_COLLECTED,RETESTING);
		   		}
		      }
		      if (dto.getFromDt() != null ) 
		      {
		    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >= '" + dto.getFromDt()+ "' ");
		      }

		      if (dto.getToDt() != null ) {
		    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <= '" + dto.getToDt()+ "' ");
		      }

		      if (dto.getPatientId() != null && dto.getPatientId() > 0)
		    	  sb
		            .append(" AND pati_mst.patient_id = " + dto.getPatientId());

		      if (dto.getVisitTypes() != null && dto.getVisitTypes().size() > 0)
		    	  sb.append(" AND visit_type_master.visit_type_id IN ("
		            + StringCommonMethods.convertToStringJoiner(dto.getVisitTypes()) + ")");

		      if (dto.getSubDepts() != null && dto.getSubDepts().size() > 0)
		    	  sb.append(" AND test_mst.dept_id IN ( "
		            + StringCommonMethods.convertToStringJoiner(dto.getSubDepts()) + ")");
		      
		      if (dto.getTestTypes() != null && dto.getTestTypes().size() > 0)
		    	  sb.append(" AND test_mst.test_id in ( " +StringCommonMethods.convertToStringJoiner( dto.getTestTypes())+")");
		      
		      if (dto.getLabSampleDtlsId() != null && dto.getLabSampleDtlsId() > 0)
		    	  sb
		            .append(" AND lbsampledtls.lab_sample_dtls_id =" + dto.getLabSampleDtlsId());

		      if (dto.getAssignUnit() != null && dto.getAssignUnit() > 0)
		          sb.append("  and lbsampledtls.assign_unit  ="+dto.getAssignUnit());
		      
		      if(dto.getUhidNumber()!="") {
		    	  sb.append(" AND lower(pati_mst.uhid_number) LIKE concat('%',LOWER('"+dto.getUhidNumber()+"'),'%') "); 
		      }
		      
		      if(dto.getSampleNo()!="") {
		    	  sb.append(" AND lower(sample_mst.sample_id) LIKE concat('%',LOWER('"+dto.getSampleNo()+"'),'%') ");  
		      }
		      if (dto.getIsOnHoldParameter() == true) {
            	  statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
            	  sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
                  sb.append(" AND lbsampledtls.is_on_hold_parameter =" + dto.getIsOnHoldParameter());
              }
              if (statusIds != null && statusIds.size() > 0 && dto.getIsOnHoldParameter() == false) {
                  sb.append(" and (lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+") or lbsampledtls.is_on_hold_parameter = true) ");
              }
              
		      if (dto.getLabSortingId() != null) {
  				if (dto.getLabSortingId() == 1) {
  					sb.append(" ORDER BY  " + "  lbsampledtls.patient_id," + "  lbsampledtls.updated_date  DESC ");
  				} else if (dto.getLabSortingId() == 2) {
  					sb.append(" ORDER BY  " + "  order_master.order_id DESC ");
  				} else if (dto.getLabSortingId() == 3) {
  					sb.append(" ORDER BY  " + "  sample_mst.sample_id  DESC ");
  				} else if (dto.getLabSortingId() == 4) {
  					sb.append(" ORDER BY  " + "  lbsampledtls.updated_date DESC ");
  				}
  			} else {
  				sb.append(" ORDER BY  lbsampledtls.priority_id, lbsampledtls.updated_date DESC ");
  			}
		      
		      Query createdQuery = sessionFactory.getCurrentSession()
		          .createSQLQuery(sb.toString());
		      
		      
		      List<LabResultMasterDto> list  = createdQuery
		    			.setInteger("orgId", dto.getOrgId())
						.setInteger("orgUnitId", dto.getOrgUnitId())
						.setFirstResult(dto.getOffset() != null ? dto.getOffset() : 0)
						.setMaxResults(dto.getRecordPerPage() != null ? dto.getRecordPerPage() : 50)
						.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();

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
	public Response getSerologyReportEntryDetails(List<Integer> labSampleDtlsId) throws ApplicationException {
	
			try 
			{
				List<SerologyTestResultDto> list = new ArrayList<>();
				for (Integer labSamDtlsId : labSampleDtlsId) {
					
					List<SerologyTestResultDto> lis = sessionFactory.getCurrentSession().getNamedQuery("GET_SERO_REPORT_ENTRY_DETAILS")
						.setInteger("labSampleDtlsId", labSamDtlsId)
						.setResultTransformer(Transformers.aliasToBean(SerologyTestResultDto.class)).list();
					
					if(lis!=null && lis.size()>0) {
						for (SerologyTestResultDto serologyTestResultDto : lis) {
							Response<ParameterMasterDto, Integer> response =  iParameterMasterDao.getParameterById(serologyTestResultDto.getOrgId(), serologyTestResultDto.getOrgUnitId(), serologyTestResultDto.getParameterId());
							if(response.getObject()!=null) {
								ParameterMasterDto parameterMasterDto = (ParameterMasterDto) response.getObject();
								 if(parameterMasterDto.getListHelpValueMaster()!=null) {
									 serologyTestResultDto.setListHelpValueMaster(parameterMasterDto.getListHelpValueMaster());
		                           }
							}
							
						}
					}
					list.addAll(lis);
				}
				return new Response(SUCCESS, SUCCESS_200, null, list, null);
			} catch (Exception e) {
				logger.error("Exception", e);
				throw e;
			}
	}

	@Override
	public Response getSerologyValidationList(MicrobioParamDto dto) throws ApplicationException {
		List<LabResultMasterDto> listLabResultMasterDto = null;
		try {
			
			
		    String query = sessionFactory.getCurrentSession().getNamedQuery("GET_SEROLOGY_RESULT_ENTRY_LIST").getQueryString();
			StringBuffer sb= new StringBuffer(query);
			  List<Integer> statusIds = new ArrayList<Integer>(); 
		      if(dto.getIsOnHoldParameter() == false) {
		   			
		   			statusIds = Arrays.asList(RESULT_ENTRY_DONE);
		   		
		      }
		    sb.append( " and to_char(order_master.order_date,'YYYY-MM-DD') >= '" + GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getOrgUnitId()),"yyyy-MM-dd") + "' ");
		    sb.append(" and to_char(order_master.order_date,'YYYY-MM-DD') <= '" + GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getOrgUnitId()),"yyyy-MM-dd") + "' ");
			sb.append("  and lbsampledtls.assign_unit  ="+dto.getOrgUnitId());
			 if (dto.getIsOnHoldParameter() == true) {
           	  statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
           	  sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
                 sb.append(" AND lbsampledtls.is_on_hold_parameter =" + dto.getIsOnHoldParameter());
             }
             if (statusIds != null && statusIds.size() > 0 && dto.getIsOnHoldParameter() == false) {
                 sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
                 sb.append(" AND lbsampledtls.is_on_hold_parameter =" + dto.getIsOnHoldParameter());
             }
			sb.append(" ORDER BY  " 
			         + "  lbsampledtls.priority_id,  "
			         + "  order_master.order_date  "  );
			  
			listLabResultMasterDto = sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
					.setInteger("orgId", dto.getOrgId())
					.setInteger("orgUnitId", dto.getOrgUnitId())
//					.setInteger("sampleStatusId", RESULT_ENTRY_DONE)
					.setFirstResult(dto.getOffset() != null ? dto.getOffset() : 0)
					.setMaxResults(dto.getRecordPerPage() != null ? dto.getRecordPerPage() : 50)
					.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_200, null, listLabResultMasterDto, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getSerologyValidationListCount(MicrobioParamDto dto) throws ApplicationException {
		try {
			
			 String query = sessionFactory.getCurrentSession().getNamedQuery("GET_SEROLOGY_RESULT_ENTRY_LIST_COUNT").getQueryString();
				StringBuffer sb= new StringBuffer(query);
				
				  List<Integer> statusIds = new ArrayList<Integer>(); 
			      if(dto.getIsOnHoldParameter() == false) {
			   			
			   			statusIds = Arrays.asList(RESULT_ENTRY_DONE);
			   		
			      }
			      
				sb.append("  and lbsampledtls.assign_unit  ="+dto.getAssignUnit());
			
				if (dto.getFromDate() != null ) 
			      {
			    	  sb.append( " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >= '" +  dto.getFromDate() + "' ");
			      }

			      if (dto.getToDate() != null ) {
			    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <= '" + dto.getToDate() + "' ");
			      }
			      if (dto.getIsOnHoldParameter() == true) {
	            	  statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
	            	  sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
	                  sb.append(" AND lbsampledtls.is_on_hold_parameter =" + dto.getIsOnHoldParameter());
	              }
	              if (statusIds != null && statusIds.size() > 0 && dto.getIsOnHoldParameter() == false) {
	                  sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
	              }
			BigInteger sampleResultEntryCount = (BigInteger) sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setInteger("orgId", dto.getOrgId())
//					.setInteger("sampleStatusId", RESULT_ENTRY_DONE)
					.setInteger("orgUnitId", dto.getOrgUnitId()).uniqueResult();
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
	public Response searchSerologyValidationList(SearchDto dto) throws ApplicationException {
		try {

		      String query = sessionFactory.getCurrentSession()
		          .getNamedQuery("GET_SEROLOGY_RESULT_ENTRY_LIST").getQueryString().toString();

		      StringBuilder sb = new StringBuilder(query);
		      List<Integer> statusIds = new ArrayList<Integer>(); 
		      if(dto.getIsOnHoldParameter() == false) {
		   			statusIds = Arrays.asList(RESULT_ENTRY_DONE);
		      }
		      if (dto.getFromDt() != null ) 
		      {
		    	  sb.append(
		            " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >= '" + dto.getFromDt()+ "' ");
		      }

		      if (dto.getToDt() != null ) {
		    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <= '" +dto.getToDt()+ "' ");
		      }

		      if (dto.getPatientId() != null && dto.getPatientId() > 0)
		    	  sb
		            .append(" AND pati_mst.patient_id = " + dto.getPatientId());

		      if (dto.getVisitTypes() != null && dto.getVisitTypes().size() > 0)
		    	  sb.append(" AND visit_type_master.visit_type_id IN ("
		            + StringCommonMethods.convertToStringJoiner(dto.getVisitTypes()) + ")");

		      if (dto.getSubDepts() != null && dto.getSubDepts().size() > 0)
		    	  sb.append(" AND test_mst.dept_id IN ( "
		            + StringCommonMethods.convertToStringJoiner(dto.getSubDepts()) + ")");
		      
		      if (dto.getTestTypes() != null && dto.getTestTypes().size() > 0)
		    	  sb.append(" AND test_mst.test_id in ( " +StringCommonMethods.convertToStringJoiner( dto.getTestTypes())+")");
		      
		      if (dto.getLabSampleDtlsId() != null && dto.getLabSampleDtlsId() > 0)
		    	  sb
		            .append(" AND lbsampledtls.lab_sample_dtls_id =" + dto.getLabSampleDtlsId());

		      if (dto.getAssignUnit() != null && dto.getAssignUnit() > 0)
		          sb.append("  and lbsampledtls.assign_unit  ="+dto.getAssignUnit());
		      
		      if(dto.getUhidNumber()!="") {
		    	  sb.append(" AND lower(pati_mst.uhid_number) LIKE concat('%',LOWER('"+dto.getUhidNumber()+"'),'%') "); 
		      }
		      
		      if(dto.getSampleNo()!="") {
		    	  sb.append(" AND lower(sample_mst.sample_id) LIKE concat('%',LOWER('"+dto.getSampleNo()+"'),'%') ");  
		      }
		      if (dto.getIsOnHoldParameter() == true) {
            	  statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
            	  sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
                  sb.append(" AND lbsampledtls.is_on_hold_parameter =" + dto.getIsOnHoldParameter());
              }
              if (statusIds != null && statusIds.size() > 0 && dto.getIsOnHoldParameter() == false) {
                  sb.append(" and (lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+") or lbsampledtls.is_on_hold_parameter = true)");
              }
		      if (dto.getLabSortingId() != null) {
					if (dto.getLabSortingId() == 1) {
						sb.append(" ORDER BY   lbsampledtls.patient_id, lbsampledtls.updated_date  DESC ");
					} else if (dto.getLabSortingId() == 2) {
						sb.append(" ORDER BY   order_master.order_id DESC ");
					} else if (dto.getLabSortingId() == 3) {
						sb.append(" ORDER BY    sample_mst.sample_id DESC ");
					} else if (dto.getLabSortingId() == 4) {
						sb.append(" ORDER BY    lbsampledtls.updated_date  DESC ");
					}
				} else {
					sb.append(" ORDER BY  lbsampledtls.priority_id,lbsampledtls.updated_date DESC");
				}
		      
		     /* sb.append( " ORDER BY  " 
				        + "  lbsampledtls.priority_id,  "
				        + "  lbsampledtls.updated_date  "  );*/
		      
		      Query createdQuery = sessionFactory.getCurrentSession()
		          .createSQLQuery(sb.toString());


		      List<LabResultMasterDto> list  = createdQuery
		    			.setInteger("orgId", dto.getOrgId())
						.setInteger("orgUnitId", dto.getOrgUnitId())
//						.setInteger("sampleStatusId", RESULT_ENTRY_DONE)
						.setFirstResult(dto.getOffset() != null ? dto.getOffset() : 0)
						.setMaxResults(dto.getRecordPerPage() != null ? dto.getRecordPerPage() : 50)
						.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();


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
	public Response saveValidation(SerologyTestResultDto dto) throws ApplicationException {
		try{
			
			
			Integer/* count = sessionFactory.getCurrentSession().createQuery("UPDATE SerologyTestResult serologyTest set "
					+ "serologyTest.resultValidatedBy=:resultValidatedBy, "
					+ "serologyTest.resultValidatedDate=:updatedDate,"
					+ "serologyTest.manualValidationTime=:manualValidationTime, "
					+ "serologyTest.updatedBy=:resultValidatedBy, "
					+ "serologyTest.updatedDate=:updatedDate "
					+ "WHERE serologyTest.labSampleDtlsId=:labSampleDtlsId ")
				.setInteger("resultValidatedBy", dto.getCreatedBy())
				.setInteger("labSampleDtlsId", dto.getLabSampleDtlsId())
				.setDate("updatedDate", GlobalCommonDateUtils.getDate(dto.getResultValidatedDate(), "dd-M-yyyy HH:mm:ss"))
				.setDate("manualValidationTime", GlobalCommonDateUtils.getDate(dto.getResultValidatedDate(), "dd-M-yyyy HH:mm:ss"))
			.executeUpdate();
			
			
			*/count = sessionFactory.getCurrentSession()
					.createSQLQuery("update lab.t_serology_test_result  set  result_validated_by = "+ dto.getCreatedBy()
							+ ", result_validated_date = '"+dto.getResultValidatedDate()+"' "
							+ ", manual_validation_time= '"+dto.getResultValidatedDate()+"' "
							+ ", updated_by= "+ dto.getCreatedBy()
							+ ", updated_date= '"+dto.getResultValidatedDate()+"' "
							+ " where lab_sample_dtls_id = " + dto.getLabSampleDtlsId())
					.executeUpdate();
			
			
			for (Iterator iterator = dto.getSerologyResultTestMapperDtoList().iterator(); iterator.hasNext();) 
			{
			
				SerologyResultTestMapperDto mapperDto = (SerologyResultTestMapperDto) iterator.next();
				Integer count1 = sessionFactory.getCurrentSession().createQuery("UPDATE SerologyResultTestMapper serologyTest set "
						+ "serologyTest.findingResult=:findingResult, "
						+ "serologyTest.findingResultDesc=:findingResultDesc, "
						+ "serologyTest.secondFinding=:findingResult, "
						+ "serologyTest.secondFindingDesc=:findingResultDesc, "
						+ "serologyTest.isAbnormal=:isAbnormal "
						+ "WHERE serologyTest.serologyResultTestMpprId=:serologyResultTestMpprId ")
						.setString("findingResult", mapperDto.getFindingResult())
						.setString("findingResultDesc", mapperDto.getFindingResultDesc())
						.setCharacter("isAbnormal", mapperDto.getIsAbnormal()!=null ? mapperDto.getIsAbnormal():'N')
						.setInteger("serologyResultTestMpprId", mapperDto.getSerologyResultTestMpprId())
				.executeUpdate();
			}
			if(count>0)
			{
				return new Response<>(SUCCESS, SUCCESS_200, null, null, count);
			}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getSerologyReportValidationDetails(List<Integer> labSampleDtlsIds) throws ApplicationException {
		try {
			List<SerologyTestResultDto> SerologyTestResultList = new ArrayList<>();;
			try 
			{
				List<SerologyTestResultDto> list = null;
				for (Integer labSampleDtlsId : labSampleDtlsIds) {

					list = sessionFactory.getCurrentSession().getNamedQuery("GET_SERO_REPORT_VALIDATION_DETAILS")
							.setInteger("labSampleDtlsId", labSampleDtlsId)
							.setResultTransformer(Transformers.aliasToBean(SerologyTestResultDto.class)).list();
					
					if(list!=null && list.size()>0) {
						for (SerologyTestResultDto serologyTestResultDto : list) {
							
							serologyTestResultDto.setLabSampleDtlsId(labSampleDtlsId);
                            Response<ParameterMasterDto, Integer> response =  iParameterMasterDao.getParameterById(serologyTestResultDto.getOrgId(), serologyTestResultDto.getOrgUnitId(), serologyTestResultDto.getParameterId());
                            if(response.getObject()!=null) {
                                ParameterMasterDto parameterMasterDto = (ParameterMasterDto) response.getObject();
                                 if(parameterMasterDto.getListHelpValueMaster()!=null) {
                                     serologyTestResultDto.setListHelpValueMaster(parameterMasterDto.getListHelpValueMaster());
                                   }
                            }
						}
					}
					
					SerologyTestResultList.addAll(list);
				}
				
				return new Response(SUCCESS, SUCCESS_200, null, SerologyTestResultList, null);
			} catch (Exception e) {
				logger.error("Exception", e);
				throw e;
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getSerologyReportReleaseList(MicrobioParamDto dto) throws ApplicationException {
		try {

			List<LabResultMasterDto> listLabResultMasterDto = null;

			String query = sessionFactory.getCurrentSession().getNamedQuery("GET_SEROLOGY_RESULT_ENTRY_LIST")
					.getQueryString();
			StringBuffer sb = new StringBuffer(query);
			  List<Integer> statusIds = new ArrayList<Integer>(); 
		      if(dto.getIsOnHoldParameter() == false) {
		   			
		   			statusIds = Arrays.asList(RESULT_VALIDATION_DONE);
		   		
		      }
			sb.append(" and to_char(order_master.order_date,'YYYY-MM-DD') >= '"
					+ GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getOrgUnitId()), "yyyy-MM-dd") + "' ");
			sb.append(" and to_char(order_master.order_date,'YYYY-MM-DD') <= '"
					+ GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getOrgUnitId()), "yyyy-MM-dd") + "' ");
			sb.append("  and lbsampledtls.assign_unit  =" + dto.getOrgUnitId());
			 if (dto.getIsOnHoldParameter() == true) {
           	  statusIds = Arrays.asList(REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
           	  sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
                 sb.append(" AND lbsampledtls.is_on_hold_parameter =" + dto.getIsOnHoldParameter());
             }
             if (statusIds != null && statusIds.size() > 0 && dto.getIsOnHoldParameter() == false) {
                 sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
             }
			sb.append(" ORDER BY  " + "  lbsampledtls.priority_id,  " + "  order_master.order_date  ");

			listLabResultMasterDto = sessionFactory.getCurrentSession().createSQLQuery(sb.toString())
					.setInteger("orgId", dto.getOrgId()).setInteger("orgUnitId", dto.getOrgUnitId())
//					.setInteger("sampleStatusId", RESULT_VALIDATION_DONE)
					.setFirstResult(dto.getOffset() != null ? dto.getOffset() : 0)
					.setMaxResults(dto.getRecordPerPage() != null ? dto.getRecordPerPage() : 50)
					.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_200, null, listLabResultMasterDto, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getSerologyReportReleaseCount(MicrobioParamDto dto) throws ApplicationException {
		try {
			
			 String query = sessionFactory.getCurrentSession().getNamedQuery("GET_SEROLOGY_RESULT_ENTRY_LIST_COUNT").getQueryString();
				StringBuffer sb= new StringBuffer(query);
				sb.append("  and lbsampledtls.assign_unit  ="+dto.getAssignUnit());
				  List<Integer> statusIds = new ArrayList<Integer>(); 
			      if(dto.getIsOnHoldParameter() == false) {
			   			
			   			statusIds = Arrays.asList(RESULT_VALIDATION_DONE);
			   		
			      }
				if (dto.getFromDate() != null ) 
			      {
			    	  sb.append( " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >= '" +  dto.getFromDate() + "' ");
			      }

			      if (dto.getToDate() != null ) {
			    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <= '" + dto.getToDate() + "' ");
			      }
			     
	              if (statusIds != null && statusIds.size() > 0 && dto.getIsOnHoldParameter() == false) {
	                  sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
	                  sb.append(" AND lbsampledtls.is_on_hold_parameter = false ");
	              }
			BigInteger sampleResultEntryCount = (BigInteger) sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setInteger("orgId", dto.getOrgId())
//					.setInteger("sampleStatusId", RESULT_VALIDATION_DONE)
					.setInteger("orgUnitId", dto.getOrgUnitId()).uniqueResult();
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
	public Response searchSerologyReportReleaseList(SearchDto dto) throws ApplicationException {
		try {

		      String query = sessionFactory.getCurrentSession()
		          .getNamedQuery("GET_SEROLOGY_RESULT_ENTRY_LIST").getQueryString().toString();
		      query = query.replace("lbsampledtls.sample_status_id = :sampleStatusId ", "lbsampledtls.sample_status_id IN(:sampleStatusIds) ");
		      StringBuilder sb = new StringBuilder(query);

		      
		      
		       List<Integer> statusIds = new ArrayList<Integer>(); 
			      if(dto.getIsOnHoldParameter() == false) {
			    	  com.param.global.common.Response res =  iLabConfigurationDao.getLabConfigurationList(dto.getOrgId(),dto.getAssignUnit());
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
		      
		      if (dto.getFromDt() != null ) 
		      {
		    	  sb.append(
		            " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >= '" + dto.getFromDt()+ "' ");
		      }

		      if (dto.getToDt() != null ) {
		    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <= '" +dto.getToDt()+ "' ");
		      }

		      if (dto.getPatientId() != null && dto.getPatientId() > 0)
		    	  sb
		            .append(" AND pati_mst.patient_id = " + dto.getPatientId());

		      if (dto.getVisitTypes() != null && dto.getVisitTypes().size() > 0)
		    	  sb.append(" AND visit_type_master.visit_type_id IN ("
		            + StringCommonMethods.convertToStringJoiner(dto.getVisitTypes()) + ")");

		      if (dto.getSubDepts() != null && dto.getSubDepts().size() > 0)
		    	  sb.append(" AND test_mst.dept_id IN ( "
		            + StringCommonMethods.convertToStringJoiner(dto.getSubDepts()) + ")");
		      
		      if (dto.getTestTypes() != null && dto.getTestTypes().size() > 0)
		    	  sb.append(" AND test_mst.test_id in ( " +StringCommonMethods.convertToStringJoiner( dto.getTestTypes())+")");
		      
		      if (dto.getLabSampleDtlsId() != null && dto.getLabSampleDtlsId() > 0)
		    	  sb
		            .append(" AND lbsampledtls.lab_sample_dtls_id =" + dto.getLabSampleDtlsId());

		      if (dto.getAssignUnit() != null && dto.getAssignUnit() > 0)
		          sb.append("  and lbsampledtls.assign_unit  ="+dto.getAssignUnit());
		      
		      if(dto.getUhidNumber()!="") {
		    	  sb.append(" AND lower(pati_mst.uhid_number) LIKE concat('%',LOWER('"+dto.getUhidNumber()+"'),'%') "); 
		      }
		      
		      if(dto.getSampleNo()!="") {
		    	  sb.append(" AND lower(sample_mst.sample_id) LIKE concat('%',LOWER('"+dto.getSampleNo()+"'),'%') ");  
		      }
		      
              if (statusIds != null && statusIds.size() > 0 && dto.getIsOnHoldParameter() == false) {
                  sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
                  sb.append(" AND lbsampledtls.is_on_hold_parameter = false ");
              }
		      if (dto.getLabSortingId() != null) {
					if (dto.getLabSortingId() == 1) {
						sb.append(" ORDER BY   lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
					} else if (dto.getLabSortingId() == 2) {
						sb.append(" ORDER BY    order_master.order_id DESC ");
					} else if (dto.getLabSortingId() == 3) {
						sb.append(" ORDER BY    sample_mst.sample_id DESC ");
					} else if (dto.getLabSortingId() == 4) {
						sb.append(" ORDER BY    lbsampledtls.updated_date  DESC ");
					}
				} else {
					sb.append(" ORDER BY  lbsampledtls.priority_id,lbsampledtls.updated_date DESC");
				}
		      
		      Query createdQuery = sessionFactory.getCurrentSession()
		          .createSQLQuery(sb.toString());


		      List<LabResultMasterDto> list  = createdQuery
		    			.setInteger("orgId", dto.getOrgId())
						.setInteger("orgUnitId", dto.getOrgUnitId())
						.setFirstResult(dto.getOffset() != null ? dto.getOffset() : 0)
						.setMaxResults(dto.getRecordPerPage() != null ? dto.getRecordPerPage() : 50)
						.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();


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
	public Response saveReportRelease(SerologyTestResultDto dto) throws ApplicationException {
		try{
			Integer count = sessionFactory.getCurrentSession()
					.createSQLQuery("update lab.t_serology_test_result  set  report_release_by = "+ dto.getCreatedBy()
							+ ", report_release_date = '"+dto.getReportReleaseDate()+"' "
							+ ", manual_release_time = '"+dto.getReportReleaseDate()+"' "
							+ ", updated_by= "+ dto.getCreatedBy()
							+ ", updated_date= '"+dto.getReportReleaseDate()+"' "
							+ " where lab_sample_dtls_id = " + dto.getLabSampleDtlsId())
					.executeUpdate();
			
			
			
			if(count>0)
			{
				return new Response<>(SUCCESS, SUCCESS_200, null, null, count);
			}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateReportRelease(SerologyResultTestMapperDto mapperDto) throws ApplicationException {
		try{
			Integer count1 = sessionFactory.getCurrentSession().createQuery("UPDATE SerologyResultTestMapper serologyTest set "
					+ "serologyTest.findingResult=:findingResult, "
					+ "serologyTest.findingResultDesc=:findingResultDesc, "
					+ "serologyTest.thiredFinding=:findingResult, "
					+ "serologyTest.thirdFindingDesc=:findingResultDesc, "
					+ "serologyTest.isAbnormal=:isAbnormal "
					+ "WHERE serologyTest.serologyResultTestMpprId=:serologyResultTestMpprId ")
					.setString("findingResult", mapperDto.getFindingResult())
					.setString("findingResultDesc", mapperDto.getFindingResultDesc())
					.setCharacter("isAbnormal", mapperDto.getIsAbnormal()!=null ? mapperDto.getIsAbnormal():'N')
					.setInteger("serologyResultTestMpprId", mapperDto.getSerologyResultTestMpprId())
			.executeUpdate();
		
	
		if(count1>0)
		{
			return new Response<>(SUCCESS, SUCCESS_200, null, null, count1);
		}
	return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}catch(Exception e){
		
		throw e;
	}
		}

	@Override
	public Response ResultEntryOrReleaseEntry(SerologyTestResultDto dto) throws ApplicationException {
		try{
			SerologyTestResult master = new SerologyTestResult();
			master.setLabSampleDtlsId(dto.getLabSampleDtlsId());
			
			master.setResultEnterBy(dto.getCreatedBy());
			master.setResultEnterDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getOrgUnitId()));
			master.setResultValidatedBy(dto.getCreatedBy());
			master.setResultValidatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getOrgUnitId()));
			master.setReportReleaseBy(dto.getCreatedBy());
			master.setReportReleaseDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getOrgUnitId()));
			master.setManualReleaseTime(TimeZoneComponent.getDateByzone(dto.getOrgUnitId()));
			master.setManualResultEnterTime(TimeZoneComponent.getDateByzone(dto.getOrgUnitId()));
			master.setManualValidationTime(TimeZoneComponent.getDateByzone(dto.getOrgUnitId()));
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getOrgUnitId()));
			master.setOrgId(dto.getOrgId());
			master.setOrgUnitId(dto.getOrgUnitId());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getOrgUnitId()));
			
			
			Integer id = (Integer)sessionFactory.getCurrentSession().save(master);
			return new Response<>(SUCCESS, SUCCESS_200, null, null, id);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response serologyResultTestMapperAndRelease(SerologyResultTestMapperDto dto)
			throws ApplicationException {
		try{
			SerologyResultTestMapper master = new SerologyResultTestMapper();
			master.setTechniqueId(dto.getTechniqueId());
			master.setSerologyTestResultId(dto.getSerologyTestResultId());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getOrgUnitId()));
			master.setOrgId(dto.getOrgId());
			master.setOrgUnitId(dto.getOrgUnitId());
			master.setUpdatedBy(dto.getCreatedBy());
			master.setUpdatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getOrgUnitId()));
			master.setParameterId(dto.getParameterId());
			master.setFindingResult(dto.getFirstFinding());
			master.setFindingResultDesc(dto.getFirstFindingDesc());
			master.setFirstFinding(dto.getFirstFinding());
			master.setFirstFindingDesc(dto.getFirstFindingDesc());
			master.setSecondFinding(dto.getFirstFinding());
			master.setSecondFindingDesc(dto.getFirstFindingDesc());
			master.setThiredFinding(dto.getFirstFinding());
			master.setThirdFindingDesc(dto.getFirstFindingDesc());
			master.setIsAbnormal(dto.getIsAbnormal()!=null ? dto.getIsAbnormal(): 'N');
			Integer id = (Integer)sessionFactory.getCurrentSession().save(master);
			return new Response<>(SUCCESS, SUCCESS_200, null, null, id);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}
	
	@Override
	public Response updateResultEntry(SerologyTestResultDto dto) throws ApplicationException {
		try{
			SerologyTestResult master = (SerologyTestResult) sessionFactory.getCurrentSession()
                    .get(SerologyTestResult.class, dto.getSerologyTestResultId());
			master.setLabSampleDtlsId(dto.getLabSampleDtlsId());
			
			master.setResultEnterBy(dto.getCreatedBy());
			master.setResultEnterDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getOrgUnitId()));
			master.setManualResultEnterTime(TimeZoneComponent.getDateByzone(dto.getOrgUnitId()));
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getOrgUnitId()));
			master.setOrgId(dto.getOrgId());
			master.setOrgUnitId(dto.getOrgUnitId());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getOrgUnitId()));
			
			
			sessionFactory.getCurrentSession().update(master);
			dto.setSerologyTestResultId(master.getSerologyTestResultId());
			return new Response<>(SUCCESS, SUCCESS_200, null, null, dto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response updateSerologyResultTestMapper(SerologyResultTestMapperDto dto) throws ApplicationException {
		try{
			SerologyResultTestMapper master = (SerologyResultTestMapper) sessionFactory.getCurrentSession()
                    .get(SerologyResultTestMapper.class, dto.getSerologyResultTestMpprId());
			master.setTechniqueId(dto.getTechniqueId());
			master.setSerologyTestResultId(dto.getSerologyTestResultId());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getOrgUnitId()));
			master.setOrgId(dto.getOrgId());
			master.setOrgUnitId(dto.getOrgUnitId());
			master.setUpdatedBy(dto.getCreatedBy());
			master.setUpdatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getOrgUnitId()));
			master.setParameterId(dto.getParameterId());
			master.setFindingResult(dto.getFirstFinding());
			master.setFindingResultDesc(dto.getFirstFindingDesc());
			master.setFirstFinding(dto.getFirstFinding());
			master.setFirstFindingDesc(dto.getFirstFindingDesc());
            master.setIsAbnormal(dto.getIsAbnormal()!=null ? dto.getIsAbnormal(): 'N');
			
			 com.param.global.common.Response res =  iLabConfigurationDao.getLabConfigurationList(dto.getOrgId(),dto.getOrgUnitId());
		        Character c = null;
		       if(res.getListObject() != null && res.getListObject().size()>0) {
		    	   
		    	   List<LabConfigurationDto> list = res.getListObject() ;
		    	  c = list.get(0).getSkipValidation();
		       }
			
		       if(c.equals('Y')) {
		    	   master.setSecondFinding(dto.getFirstFinding());
					master.setSecondFindingDesc(dto.getFirstFindingDesc());
		          }
			
			master.setThiredFinding(dto.getThiredFinding());
			master.setThirdFindingDesc(dto.getThirdFindingDesc());
			sessionFactory.getCurrentSession().update(master);
			dto.setSerologyTestResultId(master.getSerologyResultTestMpprId());
			return new Response<>(SUCCESS, SUCCESS_200, null, null, dto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response recollectSampleForSerlogy(SearchDto dto) throws ApplicationException {
		try{
			if(dto.getSerologyResultTestsList() != null && dto.getSerologyResultTestsList().size() > 0) {
				for (SearchDto seroDto : dto.getSerologyResultTestsList()) {
					Integer count1 = sessionFactory.getCurrentSession()
							.createSQLQuery(" update" + 
											"	lab.t_lab_sample_dtls " + 
											" set" + 
											"	sample_status_id = 15  , updated_by="+ dto.getUpdatedBy()+ ", updated_date='"+dto.getUpdatedDate()+"', sample_reject_reason_id="+dto.getSampleRejectReasonId()
											+" , sample_reject_reason ='"+dto.getSampleRejectReason()+"' , is_on_hold_parameter = false "+
											" where" + 
											"	lab_sample_dtls_id ="+ seroDto.getLabSampleDtlsId()+
											" and org_id="+dto.getOrgId()+
											" and org_unit_id="+dto.getOrgUnitId())
					.executeUpdate();
					
					if(seroDto.getSerologyTestResultId() != null) {
						
						sessionFactory.getCurrentSession()
						.createSQLQuery(" update" + 
										"	lab.t_serology_test_result " + 
										" set" + 
										"	status = 'I' , updated_by="+ dto.getUpdatedBy()+ ", updated_date='"+dto.getUpdatedDate()+"'"+
										" where" + 
										"	serology_test_result_id ="+ seroDto.getSerologyTestResultId()+
										" and org_id="+dto.getOrgId()+
										" and org_unit_id="+dto.getOrgUnitId())
										.executeUpdate();
						
						sessionFactory.getCurrentSession()
						.createSQLQuery(" update" + 
										"	lab.t_serology_result_test_mppr " + 
										" set" + 
										"	status = 'I'  , updated_by="+ dto.getUpdatedBy()+ ", updated_date='"+dto.getUpdatedDate()+"'"+
										" where" + 
										"	serology_test_result_id ="+ seroDto.getSerologyTestResultId()+
										" and org_id="+dto.getOrgId()+
										" and org_unit_id="+dto.getOrgUnitId())
										.executeUpdate();

					}
					
					if(count1>0)
					{
						return new Response<>(SUCCESS, SUCCESS_200, "Sample send to sample recollection successfully.", null, count1);
					}
				}
				
			}
	return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}catch(Exception e){
		
		throw e;
	}
		}

	@Override
	public Response retestSampleForSerlogy(SearchDto dto) throws ApplicationException {
		try{
			
			
			if(dto.getSerologyResultTestsList() != null && dto.getSerologyResultTestsList().size() > 0) {
				for (SearchDto seroDto : dto.getSerologyResultTestsList()) {
					Integer count1 = sessionFactory.getCurrentSession()
							.createSQLQuery(" update" + 
											"	lab.t_lab_sample_dtls " + 
											" set" + 
											"	sample_status_id = 7  , updated_by="+ dto.getUpdatedBy()+ ", updated_date='"+dto.getUpdatedDate()+"', sample_reject_reason_id="+dto.getSampleRejectReasonId()+
											" , sample_reject_reason ='"+dto.getSampleRejectReason()+"' , is_on_hold_parameter = false "+
											" where" + 
											"	lab_sample_dtls_id ="+ seroDto.getLabSampleDtlsId()+
											" and org_id="+dto.getOrgId()+
											" and org_unit_id="+dto.getOrgUnitId())
					.executeUpdate();
					
					if(seroDto.getSerologyTestResultId() != null) {
						
						sessionFactory.getCurrentSession()
						.createSQLQuery(" update" + 
										"	lab.t_serology_test_result " + 
										" set" + 
										"	status = 'I'  , updated_by="+ dto.getUpdatedBy()+ ", updated_date='"+dto.getUpdatedDate()+"'"+
										" where" + 
										"	serology_test_result_id ="+ seroDto.getSerologyTestResultId()+
										" and org_id="+dto.getOrgId()+
										" and org_unit_id="+dto.getOrgUnitId())
										.executeUpdate();
						
						sessionFactory.getCurrentSession()
						.createSQLQuery(" update" + 
										"	lab.t_serology_result_test_mppr " + 
										" set" + 
										"	status = 'I'  , updated_by="+ dto.getUpdatedBy()+ ", updated_date='"+dto.getUpdatedDate()+"'"+ 
										" where" + 
										"	serology_test_result_id ="+ seroDto.getSerologyTestResultId()+
										" and org_id="+dto.getOrgId()+
										" and org_unit_id="+dto.getOrgUnitId())
										.executeUpdate();

					}
					
					if(count1>0)
					{
						return new Response<>(SUCCESS, SUCCESS_200, "Sample send to Retesting successfully.", null, count1);
					}
				}
				
			}
		
	return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}catch(Exception e){
		
		throw e;
	}
	}

	@Override
	public Response updateAmend(List<SerologyTestResultDto> list) throws ApplicationException {

		try{
				for (SerologyTestResultDto seroDto : list) {
						
					if(seroDto.getAmend() !=  null && seroDto.getAmend() == true ){
						sessionFactory.getCurrentSession()
						.createSQLQuery(" update" + 
										"	lab.t_serology_test_result " + 
										" set" + 
										"	status = 'I' "+
										" where" + 
										"	lab_sample_dtls_id = "+ seroDto.getLabSampleDtlsId())
										.executeUpdate();
					}
						
					
						return new Response<>(SUCCESS, SUCCESS_200, null, null, null);
				}
				
		
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}catch(Exception e){
		
		throw e;
	}
	
	}
}

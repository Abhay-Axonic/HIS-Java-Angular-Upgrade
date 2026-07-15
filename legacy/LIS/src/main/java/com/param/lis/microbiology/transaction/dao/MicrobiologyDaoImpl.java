package com.param.lis.microbiology.transaction.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

import com.param.entity.lis.micro.TMicroSampleMedia;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ServiceException;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.common.StringCommonMethods;
import com.param.lis.microbiology.transaction.dto.TMicroSampleMediaDto;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;
import com.param.lis.transaction.dto.LabSampleMasterDto;
import com.param.lis.transaction.dto.SampleAcceptancePendingDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked","unused" })
public class MicrobiologyDaoImpl extends GenericDao<LabSampleDetailsMaster, Integer>
		implements IMicrobiologyDao, ICommonConstants, ITransactionConstants, IError {
	@Autowired
	private SessionFactory sessionFactory;
	
	

	public MicrobiologyDaoImpl() {
		super(LabSampleDetailsMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;

	final static Logger logger = Logger.getLogger(MicrobiologyDaoImpl.class);

	@Override
	public Response microAcceptancePending(BioChemParamDto bioChemParamDto) throws ApplicationException {
		List<SampleAcceptancePendingDto> list = null;
		try {
			
			String query = sessionFactory.getCurrentSession().getNamedQuery("MICROBIOLOGY_WORKLIST_BY_STATUS").getQueryString();
			StringBuffer sb= new StringBuffer(query);
			sb.append(" and lbsampledtls.assign_unit =:assignUnit ");
		    sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:currentDate ");
		    sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:currentDate ");
		    sb.append(" order by lbsampledtls.updated_date asc ");
		
		    List<Integer> statusIds = new ArrayList<Integer>();
		      
			if(bioChemParamDto.getIsSampleAcceptanceApplicable() == 'Y') {
				
				statusIds = Arrays.asList(SAMPLE_SEND_TO_DEPT);
				
			}else if (bioChemParamDto.getIsSendToCrApplicable() != null && bioChemParamDto.getIsSendToCrApplicable() == 'Y') {
							
				statusIds = Arrays.asList(SAMPLE_SEND_TO_CR,SAMPLE_COLLECTED);
				
			}else {
				
				statusIds = Arrays.asList(SAMPLE_COLLECTED);
			}
			list = sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("orgId", bioChemParamDto.getOrgId())
					.setParameter("orgUnitId", bioChemParamDto.getOrgUnitId())
					.setParameterList("sampleStatusIds", statusIds)
					.setParameter("deptId", bioChemParamDto.getDeptId())
					.setParameter("assignUnit", bioChemParamDto.getAssignUnit())
					.setParameter("currentDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(bioChemParamDto.getOrgUnitId()),"yyyy-MM-dd"))
					.setFirstResult(bioChemParamDto.getOffset() != null ? bioChemParamDto.getOffset() : 0)
					.setMaxResults(bioChemParamDto.getRecordPerPage() != null ? bioChemParamDto.getRecordPerPage() : 50)
					.setResultTransformer(Transformers.aliasToBean(SampleAcceptancePendingDto.class)).list();
			return new Response(SUCCESS, SUCCESS_200, null, list, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response microAcceptancePendingCount(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {
			
			String query = sessionFactory.getCurrentSession().getNamedQuery("MICROBIOLOGY_WORKLIST_COUNT_BY_STATUS").getQueryString();
			StringBuffer sb= new StringBuffer(query);
		    sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:currentDate ");
		    sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:currentDate ");
		    sb.append(" and lbsampledtls.assign_unit =:assignUnit ");
		    
		      List<Integer> statusIds = new ArrayList<Integer>();
		      
				if(bioChemParamDto.getIsSampleAcceptanceApplicable() == 'Y') {
					
					statusIds = Arrays.asList(SAMPLE_SEND_TO_DEPT);
					
				}else if (bioChemParamDto.getIsSendToCrApplicable() != null && bioChemParamDto.getIsSendToCrApplicable() == 'Y') {
								
					statusIds = Arrays.asList(SAMPLE_SEND_TO_CR,SAMPLE_COLLECTED);
					
				}else {
					
					statusIds = Arrays.asList(SAMPLE_COLLECTED);
				}
		    
			BigInteger sampleAcceptancePendingCount = (BigInteger) sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("orgId", bioChemParamDto.getOrgId())
					.setParameterList("sampleStatusIds",statusIds)
					.setParameter("deptId", bioChemParamDto.getDeptId())
					.setParameter("assignUnit", bioChemParamDto.getAssignUnit())
					.setParameter("currentDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(bioChemParamDto.getOrgUnitId()),"yyyy-MM-dd"))
					.setParameter("orgUnitId", bioChemParamDto.getOrgUnitId()).uniqueResult();
			if (sampleAcceptancePendingCount.compareTo(BigInteger.ZERO) == 1) {
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, sampleAcceptancePendingCount);
			} else {
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response microChemistryAcceptRejectSample(List<LabSampleDetailsMasterDto> listlabSampleDetailsMasterDto)
			throws ApplicationException {
		try {
			Integer flag = null;
			LabSampleDetailsMaster labSampleDetailsMaster;
			if (!listlabSampleDetailsMasterDto.isEmpty()) {
				for (Iterator iterator = listlabSampleDetailsMasterDto.iterator(); iterator.hasNext();) {
					LabSampleDetailsMasterDto labSampleDetailsMasterDto = (LabSampleDetailsMasterDto) iterator.next();
					labSampleDetailsMaster = findById(labSampleDetailsMasterDto.getLabSampleDtlsId());
					if (labSampleDetailsMasterDto.getCurrStatus().equals(ACCEPT)) {
						labSampleDetailsMaster.setSampleAcceptBy(labSampleDetailsMasterDto.getSampleAcceptBy());
						labSampleDetailsMaster.setSamplePendingCount(labSampleDetailsMasterDto.getSamplePendingCount());
						labSampleDetailsMaster.setSampleAcceptDatetime(TimeZoneComponent.getDateByzone(labSampleDetailsMasterDto.getOrgUnitId()).getTime());
						labSampleDetailsMaster.setUpdatedBy(labSampleDetailsMasterDto.getCreatedBy());
						labSampleDetailsMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(labSampleDetailsMasterDto.getOrgUnitId()).getTime());
						labSampleDetailsMaster.setSampleStatusId(SAMPLE_ACCEPTED);
						labSampleDetailsMaster.setPerformedUnit(labSampleDetailsMasterDto.getOrgUnitId());
						labSampleDetailsMaster.setAssignUnit(labSampleDetailsMasterDto.getOrgUnitId());
						update(labSampleDetailsMaster);

					} else if (labSampleDetailsMasterDto.getCurrStatus().equals(REJECT)) {
						labSampleDetailsMaster.setSampleStatusId(SAMPLE_REJECTED);
						labSampleDetailsMaster.setSampleRejectReason(labSampleDetailsMasterDto.getSampleRejectReason());
						labSampleDetailsMaster
								.setSampleRejectReasonId(labSampleDetailsMasterDto.getSampleRejectReasonId());
						labSampleDetailsMaster.setSampleRejectBy(labSampleDetailsMasterDto.getSampleRejectBy());
						labSampleDetailsMaster.setCreatedBy(labSampleDetailsMasterDto.getCreatedBy());
						labSampleDetailsMaster.setCreatedDate(TimeZoneComponent.getDateByzone(labSampleDetailsMasterDto.getOrgUnitId()).getTime());
						labSampleDetailsMaster.setUpdatedBy(labSampleDetailsMasterDto.getCreatedBy());
						labSampleDetailsMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(labSampleDetailsMasterDto.getOrgUnitId()).getTime());
						labSampleDetailsMaster.setPerformedUnit(labSampleDetailsMasterDto.getOrgUnitId());
						labSampleDetailsMaster.setAssignUnit(labSampleDetailsMasterDto.getOrgUnitId());
						update(labSampleDetailsMaster);
					}
				}
					return new Response(SUCCESS, SUCCESS_200, SAMPLE_ACCEPT_SUCC, null, null);
				} else {
					return new Response(ERROR, ERR_500, SAMPLE_ACCEPT_FAIL, null, null);
				}

		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, SAMPLE_REJECT_SUCC, null, null);
		}

	}

	@Override
	public Response getMicroChemistryWorklist(BioChemParamDto bioChemParamDto) throws ApplicationException {
		List<SampleAcceptancePendingDto> listSampleAcceptancePendingDto = null;
		try {
			
			
			String query = sessionFactory.getCurrentSession().getNamedQuery("MICROBIOLOGY_WORKLIST_DEPT").getQueryString();
			StringBuffer sb= new StringBuffer(query);
		    sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:currentDate ");
		    sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:currentDate ");
		    sb.append(" order by lbsampledtls.updated_date asc ");
			List<Integer> statusId = new ArrayList<Integer>();
			if(bioChemParamDto.getIsAcceptanceInDepartment() == 'Y') {
				
				statusId = Arrays.asList(SAMPLE_ACCEPTED);
				
			}else if(bioChemParamDto.getIsSampleAcceptanceApplicable() == 'Y') {
				
				statusId = Arrays.asList(SAMPLE_SEND_TO_DEPT);
				
			}else if (bioChemParamDto.getIsSendToCrApplicable() != null && bioChemParamDto.getIsSendToCrApplicable() == 'Y') {
							
				statusId = Arrays.asList(SAMPLE_SEND_TO_CR,SAMPLE_COLLECTED);
				
			}else {
				
				statusId = Arrays.asList(SAMPLE_COLLECTED);
			}

			listSampleAcceptancePendingDto = sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("orgId", bioChemParamDto.getOrgId())
					.setParameter("orgUnitId", bioChemParamDto.getOrgUnitId())
					.setParameterList("sampleStatusIds",statusId)
					.setParameter("deptId", bioChemParamDto.getDeptId())
					.setParameter("currentDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(bioChemParamDto.getOrgUnitId()),"yyyy-MM-dd"))
					.setFirstResult(bioChemParamDto.getOffset() != null ? bioChemParamDto.getOffset() : 0)
					.setMaxResults(bioChemParamDto.getRecordPerPage() != null ? bioChemParamDto.getRecordPerPage() : 50)
					.setResultTransformer(Transformers.aliasToBean(SampleAcceptancePendingDto.class)).list();
			return new Response(SUCCESS, SUCCESS_200, null, listSampleAcceptancePendingDto, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getMicroChemistryWorklistCount(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {
			
			String query = sessionFactory.getCurrentSession().getNamedQuery("MICROBIOLOGY_WORKLIST_DEPT_COUNT").getQueryString();
			StringBuffer sb= new StringBuffer(query);
		    sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:currentDate ");
		    sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:currentDate ");
			
		    List<Integer> statusId = new ArrayList<Integer>();
			if(bioChemParamDto.getIsAcceptanceInDepartment() == 'Y') {
				
				statusId = Arrays.asList(SAMPLE_ACCEPTED);
				
			}else if(bioChemParamDto.getIsSampleAcceptanceApplicable() == 'Y') {
				
				statusId = Arrays.asList(SAMPLE_SEND_TO_DEPT);
				
			}else if (bioChemParamDto.getIsSendToCrApplicable() != null && bioChemParamDto.getIsSendToCrApplicable() == 'Y') {
							
				statusId = Arrays.asList(SAMPLE_SEND_TO_CR,SAMPLE_COLLECTED);
				
			}else {
				
				statusId = Arrays.asList(SAMPLE_COLLECTED);
			}

			BigInteger sampleAcceptancePendingCount = (BigInteger) sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("orgId", bioChemParamDto.getOrgId())
					.setParameterList("sampleStatusIds", statusId)
					.setParameter("deptId", bioChemParamDto.getDeptId())
					.setParameter("currentDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(bioChemParamDto.getOrgUnitId()),"yyyy-MM-dd"))
					.setParameter("orgUnitId", bioChemParamDto.getOrgUnitId()).uniqueResult();
			if (sampleAcceptancePendingCount.compareTo(BigInteger.ZERO) == 1) {
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, sampleAcceptancePendingCount);
			} else {
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response sendForIncubationObjservation(List<LabSampleDetailsMasterDto> listlabSampleDetailsMasterDto)
			throws ApplicationException {
		try {
			
			//Integer labSampleDetailsStatus = 0;
			LabSampleDetailsMaster labSampleDetailsMaster = new LabSampleDetailsMaster();
			if (!listlabSampleDetailsMasterDto.isEmpty()) {
				for (Iterator iterator = listlabSampleDetailsMasterDto.iterator(); iterator.hasNext();) {
					LabSampleDetailsMasterDto labSampleDetailsMasterDto = (LabSampleDetailsMasterDto) iterator.next();
					/*labSampleDetailsStatus = (Integer) sessionFactory.getCurrentSession()
							.getNamedQuery("UPDATE_LAB_SAMPLE_DETAILS")
							.setParameter("assignUnit", labSampleDetailsMasterDto.getOrgUnitId())
							.setParameter("performedUnit", labSampleDetailsMasterDto.getOrgUnitId())
							.setParameter("updatedBy", labSampleDetailsMasterDto.getCreatedBy())
							.setParameter("updatedDate",TimeZoneComponent.getDateByzone(unitId).getTime())
							.setParameter("sampleStatausId", SEND_FOR_INCUBATION_OBERVATION)
							.setParameter("labSampleDtlsId", labSampleDetailsMasterDto.getLabSampleDtlsId())
							.executeUpdate();*/
					
					labSampleDetailsMaster = findById(labSampleDetailsMasterDto.getLabSampleDtlsId());
					labSampleDetailsMaster.setUpdatedBy(labSampleDetailsMasterDto.getCreatedBy());
					labSampleDetailsMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(labSampleDetailsMasterDto.getOrgUnitId()).getTime());
					labSampleDetailsMaster.setSampleStatusId(SEND_FOR_INCUBATION_OBERVATION);
					labSampleDetailsMaster.setPerformedUnit(labSampleDetailsMasterDto.getOrgUnitId());
					labSampleDetailsMaster.setAssignUnit(labSampleDetailsMasterDto.getOrgUnitId());
					labSampleDetailsMaster = update(labSampleDetailsMaster);

				}
				if (labSampleDetailsMaster != null) {
					return new Response(SUCCESS, SUCCESS_200, SAMPLE_SEND_TO_INCUBATION_OBJSERVATION_SUCC, null, null);
				} else {
					return new Response(ERROR, ERR_500, SAMPLE_SEND_TO_INCUBATION_OBJSERVATION_FAIL, null, null);
				}
			} else {
				return new Response(ERROR, ERR_500, SAMPLE_SEND_TO_INCUBATION_OBJSERVATION_FAIL, null, null);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, SAMPLE_SEND_TO_INCUBATION_OBJSERVATION_FAIL, null, null);
		}
	}

	@Override
	public Response updateLabSampleDetailsMaster(LabSampleDetailsMasterDto labSampleDetailsMasterDto)
			throws ApplicationException {
		try {
			
				LabSampleDetailsMaster labSampleDetailsMaster = findById(labSampleDetailsMasterDto.getLabSampleDtlsId());
				if (labSampleDetailsMaster!=null) {
				labSampleDetailsMaster.setUpdatedBy(labSampleDetailsMasterDto.getCreatedBy());
				labSampleDetailsMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(labSampleDetailsMasterDto.getOrgUnitId()).getTime());
				labSampleDetailsMaster.setInternalNotes(labSampleDetailsMasterDto.getInternalNotes());
				labSampleDetailsMaster.setInternalNotes(labSampleDetailsMasterDto.getInternalNotes());
				labSampleDetailsMaster.setIsOnHoldParameter(labSampleDetailsMasterDto.getIsOnHoldParameter());
				labSampleDetailsMaster.setSampleStatusId(labSampleDetailsMasterDto.getSampleStatusId());
				labSampleDetailsMaster.setIsReportRecall(labSampleDetailsMasterDto.getIsReportRecall());
				labSampleDetailsMaster.setProvisional(labSampleDetailsMasterDto.getProvisional() != null ? labSampleDetailsMasterDto.getProvisional() : null);
				update(labSampleDetailsMaster);

			
			
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, labSampleDetailsMaster);
			} else {
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, Integer.valueOf(0));
			}
		} catch (HibernateException er) {
			logger.error("Exection", er);throw er;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response getAddedMediaForIncuabation(Integer labSampleDtlsId, Integer orgId, Integer orgUniId)
			throws ApplicationException {
		List<TMicroSampleMediaDto> listTMicroSampleMediaDto = null;
		try {
			listTMicroSampleMediaDto = sessionFactory.getCurrentSession().getNamedQuery("GET_INCUBATION_MEDIA")
					.setParameter("orgId", orgId).setParameter("orgUnitId", orgUniId)
					.setParameter("labSampleDtlsId", labSampleDtlsId)
					.setResultTransformer(Transformers.aliasToBean(TMicroSampleMediaDto.class)).list();
			return new Response(SUCCESS, SUCCESS_200, null, listTMicroSampleMediaDto, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response addMediaForIncuabation(List<TMicroSampleMediaDto> listTMicroSampleMediaDto)
			throws ApplicationException {

		try {
			if (!listTMicroSampleMediaDto.isEmpty()) {
				for (Iterator iterator = listTMicroSampleMediaDto.iterator(); iterator.hasNext();) {
					TMicroSampleMediaDto tMicroSampleMediaDto = (TMicroSampleMediaDto) iterator.next();
					TMicroSampleMedia tMicroSampleMedia = mapper.map(tMicroSampleMediaDto, TMicroSampleMedia.class,
							"TMicroSampleMediaDtoToTMicroSampleMedia");
					sessionFactory.getCurrentSession().saveOrUpdate(tMicroSampleMedia);
				}
				return new Response(SUCCESS, SUCCESS_200, MEDIA_ADD_SUCC, null, null);
			} else {
				return new Response(ERROR, ERR_500, MEDIA_ADD_FAIL, null, null);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			return new Response(ERROR, ERR_500, MEDIA_ADD_FAIL, null, null);
		}
	}

	@Override
	public Response microAcceptancePendingSearch(SearchDto searchDto) throws ApplicationException {
		try {
			List<Integer>  subDeptIds = new ArrayList<Integer>();
			List<Integer>  testTypes = new ArrayList<Integer>();
			List<Integer>  visitTypes = new ArrayList<Integer>();

		      String query = sessionFactory.getCurrentSession()
		          .getNamedQuery("MICROBIOLOGY_WORKLIST_BY_STATUS").getQueryString().toString();

		      StringBuilder sb = new StringBuilder(query);

		      if (searchDto.getFromDt() != null ) {
		    	  sb.append(
		            " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt ");
		      }

		      if (searchDto.getToDt() != null ) {
		    	  sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt ");
		      }

		      if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
				  sb
						  .append(" AND pati_mst.patient_id =:patientId ");
			  }
		      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0) {
				  visitTypes = searchDto.getVisitTypes().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());

				  sb.append(" AND visit_type_master.visit_type_id IN (:visitTypes)");
			  }
		      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0) {
				  subDeptIds = searchDto.getSubDepts().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());

				  sb.append(" AND test_mst.dept_id IN (:subDepts)");
			  }
		      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0) {
				  testTypes = searchDto.getTestTypes().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());

				  sb.append(" AND test_mst.test_id in (:testTypes)");
			  }
		      if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0) {
				  sb
						  .append(" AND lbsampledtls.lab_sample_dtls_id =:labSampleDtlsId ");
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

		      
		      
		      sb.append( " ORDER BY  " 
				        + "  lbsampledtls.priority_id,  "
				        + "  lbsampledtls.updated_date  "  );
		      
		      Query createdQuery = sessionFactory.getCurrentSession()
		          .createSQLQuery(sb.toString());

		      List<Integer> statusIds = new ArrayList<Integer>();
		      
				if(searchDto.getIsSampleAcceptanceApplicable() == 'Y') {
					
					statusIds = Arrays.asList(SAMPLE_SEND_TO_DEPT);
					
				}else if (searchDto.getIsSendToCrApplicable() != null && searchDto.getIsSendToCrApplicable() == 'Y') {
								
					statusIds = Arrays.asList(SAMPLE_SEND_TO_CR,SAMPLE_COLLECTED);
					
				}else {
					
					statusIds = Arrays.asList(SAMPLE_COLLECTED);
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

			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0){
				createdQuery.setParameterList("visitTypes", visitTypes);
			}
			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0){
				createdQuery.setParameterList("subDepts", subDeptIds);
			}
			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0){
				createdQuery.setParameterList("testTypes", testTypes);
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
						.setParameterList("sampleStatusIds",statusIds)
						.setParameter("deptId", searchDto.getDeptId())
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
	public Response getMicroChemistryWorklistSearch(SearchDto searchDto) throws ApplicationException {
		try {
				List<Integer>  subDeptIds = new ArrayList<Integer>();
				List<Integer>  testTypes = new ArrayList<Integer>();
				List<Integer>  visitTypes = new ArrayList<Integer>();

		      String query = sessionFactory.getCurrentSession()
		          .getNamedQuery("MICROBIOLOGY_WORKLIST_DEPT").getQueryString().toString();

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
		      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0) {
				  visitTypes = searchDto.getVisitTypes().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());

				  sb.append(" AND visit_type_master.visit_type_id IN (:visitTypes)");
			  }
		      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0) {
				  subDeptIds = searchDto.getSubDepts().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());

				  sb.append(" AND test_mst.dept_id IN (:subDepts)");
			  }
		      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0) {
				  testTypes = searchDto.getTestTypes().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());

				  sb.append(" AND test_mst.test_id in (:testTypes)");
			  }
		      if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0) {
				  sb.append(" AND lbsampledtls.lab_sample_dtls_id =:labSampleDtlsId ");
			  }
		      if(searchDto.getSampleNo()!=null && searchDto.getSampleNo()!="") {
		    	  sb.append(" AND CAST(sample_mst.sample_no  AS varchar )like :sampleNo ");
		      }
		      
		      if (searchDto.getUhidNumber() != "" && searchDto.getUhidNumber()!=null) {
					sb.append(" AND lower(pati_mst.uhid_number) LIKE :uhidNumber ");
				}

		      if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
		          sb.append("  and lbsampledtls.assign_unit  =:assignUnit ");
		      
		      sb.append(" ORDER BY  " 
				        + "  lbsampledtls.priority_id,  "
				        + "  lbsampledtls.updated_date  "  );
		      
		      Query createdQuery = sessionFactory.getCurrentSession()
		          .createSQLQuery(sb.toString());
		      
		      List<Integer> statusId = new ArrayList<Integer>();
				if(searchDto.getIsAcceptanceInDepartment() == 'Y') {
					
					statusId = Arrays.asList(SAMPLE_ACCEPTED);
					
				}else if(searchDto.getIsSampleAcceptanceApplicable() == 'Y') {
					
					statusId = Arrays.asList(SAMPLE_SEND_TO_DEPT);
					
				}else if (searchDto.getIsSendToCrApplicable() != null && searchDto.getIsSendToCrApplicable() == 'Y') {
								
					statusId = Arrays.asList(SAMPLE_SEND_TO_CR,SAMPLE_COLLECTED);
					
				}else {
					
					statusId = Arrays.asList(SAMPLE_COLLECTED);
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

			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0){
				createdQuery.setParameterList("visitTypes", visitTypes);
			}

			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0){
				createdQuery.setParameterList("subDepts", subDeptIds);
			}

			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0){
				createdQuery.setParameterList("testTypes", testTypes);
			}

			if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0){
				createdQuery.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId());
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
	public Response getMicroTempletWorkbeanchSearch(SearchDto searchDto) throws ApplicationException {
		try {
			List<Integer>  testTypes = new ArrayList<Integer>();
			List<Integer>  visitTypes = new ArrayList<Integer>();

			  String query =  null;
			
			  if (searchDto.getReportType() == 1 || searchDto.getReportType() == 2) {
				  
				  query = sessionFactory.getCurrentSession()
				          .getNamedQuery("MICROBIOLOGY_TEMPLATE_WORKBEANCH").getQueryString().toString();
				  
		      }else {
		    	  
		    	  query = sessionFactory.getCurrentSession()
				          .getNamedQuery("MICROBIOLOGY_TEMPLATE_WORKBEANCH_ATFER_WORKBEANCH").getQueryString().toString();
		      }
		       

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
			  if(searchDto.getUhidNumber()!=null && searchDto.getUhidNumber()!=null) {
				sb.append(" AND lower(pati_mst.uhid_number) LIKE :uhidNumber ");
			 }

		      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0) {
				  visitTypes = searchDto.getVisitTypes().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());

				  sb.append(" AND visit_type_master.visit_type_id IN (:visitTypes)");
			  }
		      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0) {
				  testTypes = searchDto.getTestTypes().stream()
						  .map(obj -> obj.getId())
						  .collect(Collectors.toList());

				  sb.append(" AND test_mst.test_id in (:testTypes)");
			  }
		      if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0) {
				  sb.append(" AND lbsampledtls.lab_sample_dtls_id =:labSampleDtlsId ");
			  }
		      if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0) {
				  sb.append("  and lbsampledtls.assign_unit  =:assignUnit ");
			  }
		      
		      sb.append("  and lbsampledtls.sample_status_id not in (4,7)  "); 
		      
		      
		      if (searchDto.getReportType() == 1) {
		    	  sb.append("  and lbsampledtls.micro_template_status_id is null ");
		      } else  if(searchDto.getReportType() == 2) {
		    	  sb.append("  and (case when lbsampledtls.micro_template_status_id = 38 then lbsampledtls.micro_template_status_id = 38 and microTemp.is_provisional_release is null " + 
		    	  		"	else lbsampledtls.micro_template_status_id = 39 and microTemp.is_provisional_release is null end ) ");
		      }else  if(searchDto.getReportType() == 3) {
		    	  sb.append("  and lbsampledtls.micro_template_status_id in(39,40)  ");
		    	  sb.append("  and (microTemp.is_final_release = 'N' or microTemp.is_final_release is null ) and microTemp.is_provisional_release is not null ");
		      }else {
		    	  sb.append("  and lbsampledtls.micro_template_status_id in(41) ");
		    	  sb.append("  	and microTemp.is_final_release = 'Y' ");
		      }
		      
		      sb.append(" ORDER BY  " 
				        + "  lbsampledtls.priority_id,  "
				        + "  lbsampledtls.updated_date  "  );
		      
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


			if (searchDto.getUhidNumber() != "" && searchDto.getUhidNumber()!=null) {
				createdQuery.setParameter("uhidNumber", "%" + searchDto.getUhidNumber().toLowerCase() + "%");
			}

			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0) {
				createdQuery.setParameterList("visitTypes", visitTypes);
			}

			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0) {
				createdQuery.setParameterList("testTypes", testTypes);
			}

			if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0) {
				createdQuery.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId());
			}

			if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0) {
				createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());
			}
		      StringBuilder newSb =   sb ;

		      List<SampleAcceptancePendingDto> list  = createdQuery
		    		  .setParameter("orgId", searchDto.getOrgId())
					  .setParameter("orgUnitId", searchDto.getOrgUnitId())
					  .setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
		                .setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 10)
				      .setResultTransformer(Transformers.aliasToBean(SampleAcceptancePendingDto.class)).list();

		      int count = 0;
		      if (list.isEmpty()) {
		        return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.",
		        		list, count);
		      } else {
		    	  Query countQuery = sessionFactory.getCurrentSession() .createSQLQuery(newSb.toString())
			    		  .setParameter("orgId", searchDto.getOrgId())
						  .setParameter("orgUnitId", searchDto.getOrgUnitId());


				  if (searchDto.getFromDt() != null ) {
					  countQuery.setParameter("fromDt", searchDto.getFromDt());
				  }

				  if (searchDto.getToDt() != null ) {
					  countQuery.setParameter("toDt", searchDto.getToDt());
				  }

				  if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
					  countQuery.setParameter("patientId", searchDto.getPatientId());
				  }

				  if (searchDto.getUhidNumber() != "" && searchDto.getUhidNumber()!=null) {
					  countQuery.setParameter("uhidNumber", "%" + searchDto.getUhidNumber().toLowerCase() + "%");
				  }

				  if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0) {
					  countQuery.setParameterList("visitTypes", visitTypes);
				  }

				  if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0) {
					  countQuery.setParameterList("testTypes", testTypes);
				  }

				  if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0) {
					  countQuery.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId());
				  }

				  if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0) {
					  countQuery.setParameter("assignUnit", searchDto.getAssignUnit());
				  }

				  List<SampleAcceptancePendingDto> listForCount  =     countQuery.setResultTransformer(Transformers.aliasToBean(SampleAcceptancePendingDto.class)).list();



		    	  if(listForCount!=null && listForCount.size()>0) {
		    		  count = listForCount.size();
		    	  }
		        return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, list,
		        		count);
		      }


		    } catch (Exception e) {
		      logger.error("Exection", e);
		      throw e;
		    }
	}

	@Override
	public Response updateLabDetailsStatus(SearchDto searchDto) throws ApplicationException {
		try{	
			
			Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update lab.t_lab_sample_dtls  set micro_template_status_id =:orderStatusId "+
					" where lab_sample_dtls_id=:labSampleDtlsId ")
					.setParameter("orderStatusId", searchDto.getOrderStatusId())
					.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId())
					.executeUpdate();
			
			return new Response(SUCCESS, null, "Accepted Successfully", null, null);

		} catch (HibernateException he) {
			logger.error("Exception in updateLabDetailsStatus", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("Exception in updateLabDetailsStatus", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response<LabSampleMasterDto, Integer> getDetailsBySampleDetailId(Integer labSampleDetailId) {
		try {
		String query=" select " + 
	    		" visit_type_master.visit_type_code as \"visitType\", "
	    		+ " order_details.priority_id AS \"priorityId\", " + 
	    		" subspe_mst.sub_speciality_name as \"deptName\", " + 
	    		" panel_master.panel_code as \"panelCode\", " + 
	    		" panel_master.panel_id as \"panelId\", " + 
	    		" lbsampledtls.gender_id as \"genderId\", " + 
	    		" lbsampledtls.lab_sample_dtls_id as \"labSampleDtlsId\", " + 
	    		" test_mst.sample_id as \"sampleId\", " + 
	    		" lbsampledtls.sample_recollect_flag as \"sampleRecollectFlag\", " + 
	    		" lbsampledtls.sample_recollect_against_id as \"sampleRecollectAgainstId\", " + 
	    		" pati_mst.uhid_number as uhid, " + 
	    		" sample_mst.sample_no as \"labSampleNo\", " + 
	    		" test_mst.test_desc as \"testDesc\", " + 
	    		" test_mst.service_id as \"serviceId\", " + 
	    		" gen_mst.gender_code as \"genderCode\", " + 
	    		" bedmst.bed_number as \"bedNumber\", " + 
	    		" order_master.admission_id as \"visitAdmId\", " + 
	    		" order_master.visit_type_id as \"visitTypeId\", " + 
	    		" lbsampledtls.dept_id as \"deptId\", " + 
	    		" lbsampledtls.test_id as \"testId\", " + 
	    		" test_mst.test_type as \"testType\", " + 
	    		" lbsampledtls.sub_dept_id as \"subDeptId\", " + 
	    		" lbsampledtls.patient_id as \"patientId\", " + 
	    		" lbsampledtls.order_details_id as \"orderDetailsId\", " + 
	    		" to_char(pati_mst.dob, 'DD-MM-YYYY') as \"dob\", " + 
	    		" to_char(admission_dtls.doa, 'dd/MM/yyyy') as doa, " + 
	    		" to_char(admission_dtls.pdd, 'dd/MM/yyyy') as pdd, " + 
	    		" concat(to_char(extract(year from age(current_timestamp, pati_mst.dob)), '999'), ' Y') as \"ageString\", " + 
	    		" pati_mst.is_vip as \"isVip\", " + 
	    		" (case " + 
	    		" when pati_mst.is_vip = 'Y' then coalesce(pati_mst.alise_name, pati_mst.first_name) " + 
	    		" else concat(coalesce(pati_mst.first_name) , ' ', coalesce(pati_mst.middle_name) , ' ', coalesce(pati_mst.last_name)) " + 
	    		" end) as \"patientName\", " + 
	    		" to_char(order_master.order_date, 'dd/mm/yyyy') as \"orderDate\", " + 
	    		"	to_char(order_master.order_date, 'dd/MM/yyyy HH12:MI AM') as \"orderDtTime\","+
	    		" pre_mst.prefix_code as \"prefixCode\", " + 
	    		" pre_mst.prefix_code || ' ' || pati_mst.first_name || ' ' || coalesce(pati_mst.middle_name)||' ' || pati_mst.last_name || ' ' || calculate_age(pati_mst.dob) "
	    		+ " || '/' || gen_mst.gender_code as \"patientDetails\", " + 
	    		" cast(lbsampledtls.patient_visit_age as DATE) - cast(pati_mst.dob as DATE) as \"patientAgeDays\", " + 
	    		" 'Dr.' || doct_mst.first_name || ' ' || doct_mst.last_name || ' (' || speciality_master.speciality_name || ')' as \"doctorDetails\", " + 
	    		" lbsampledtls.sample_barcode as \"sampleBarcode\", " + 
	    		" order_details.created_date as \"orderDateTime\", " + 
	    		" lbsampledtls.sample_collection_datetime as \"sampleCollectionDatetime\" , " + 
	    		" to_char(microTemp.report_entry_date, 'dd/MM/yyyy HH12:MI AM') as \"reportEntryDate\" , " + 
	    		" to_char(microTemp.report_validation_date,'dd/MM/yyyy HH12:MI AM') as \"reportValidationDate\" , " + 
	    		" to_char(microTemp.report_release_date ,'dd/MM/yyyy HH12:MI AM') as \"reportReleaseDate\" , " + 
	            "    microTemp.is_report_recall as \"isReportRecall\", "+
	    		" wardmast.ward_code as \"wardCode\" , " + 
	    		" test_mst.test_code as \"testCode\" , " + 
	    		" samp_mst.sample_desc as \"sampleType\" , " + 
	    		" lbsampledtls.micro_template_status_id as \"microTemplateStatusId\", " + 
	    		" order_master.order_no as \"orderNo\", " + 
	    		" order_master.admission_id as \"admissionId\", " + 
	    		" order_master.encounter_id as \"encounterId\", " + 
	    		" microTemp.is_provisional_release as \"isProvisionalRelease\", " + 
	    		" microTemp.template_object as \"templateObject\", " + 
	    		" refM.referral_name as \"referralName\", " + 
	    		" pati_mst.email as \"email\" , " + 
	    		" microTemp.is_final_release as \"isFinalRelease\", " + 
	    		" (case " + 
	    		" when order_master.encounter_id > 0 then tem.encounter_number " + 
	    		" else ta.admission_number " + 
	    		" end) as \"visitNo\", " + 
	    		" (case " + 
	    		" when app.is_pre_or_post_billing = 'Y' then tbpd.due_amount = 0 " + 
	    		" when app.is_pre_or_post_billing = 'N' then true " + 
	    		" end) as \"billSettled\", " + 
	    		" (case when order_master.encounter_id > 0 then " +
	    		" ( tpsd.payment_entitlement_id ) " +
	    		" else ( tpsd2.payment_entitlement_id ) end ) as \"paymentEntitlementId\","
	    		+ " (case when order_master.encounter_id > 0 then  " + 
	    		" ( tcm.address )  " + 
	    		" else ( tcm1.address ) end ) as \"comAddress\" ,"
	    		+ " (case when order_master.encounter_id > 0 then  " + 
	    		" ( tcm.company_desc )  " + 
	    		" else ( tcm1.company_desc ) end ) as \"companyDesc\" ,"
	    		+ "(case when order_master.encounter_id > 0 then " + 
	    		" ( to_char(tem.encounter_date, 'dd/MM/yyyy HH12:MI AM')) " + 
	    		" else (  to_char(admission_dtls.doa, 'dd/MM/yyyy HH12:MI AM') ) end ) as \"registerDate\" "+
	    		" from " + 
	    		" lab.t_lab_sample_dtls lbsampledtls " + 
	    		" inner join lab.t_microbiology_template microTemp on " + 
	    		" lbsampledtls.lab_sample_dtls_id = microTemp.lab_sample_dtls_id " + 
	    		" inner join public.t_order_details order_details on " + 
	    		" order_details.order_details_id = lbsampledtls.order_details_id " + 
	    		" inner join t_unit_application_billing_configuration app on " + 
	    		" app.unit_id = order_details.org_unit_id " + 
	    		" left join billing.t_billing_details tbd on " + 
	    		" order_details.order_details_id = tbd.order_details_id " + 
	    		" left join billing.m_billing_master mbm on " + 
	    		" mbm.billing_master_id = tbd.billing_master_id " + 
	    		" left join billing.t_bill_payee_details tbpd on " + 
	    		" tbpd.bill_id = mbm.billing_master_id " + 
	    		" and tbpd.payee_id = app.self_payee_id " + 
	    		" inner join public.t_order_master order_master on " + 
	    		" order_master.order_id = order_details.order_id " + 
	    		" inner join lab.t_lab_sample_master sample_mst on " + 
	    		" lbsampledtls.lab_sample_id = sample_mst.lab_sample_id " + 
	    		" inner join public.m_visit_type_master visit_type_master on " + 
	    		" visit_type_master.visit_type_id = order_master.visit_type_id " + 
	    		" inner join lab.m_test_master test_mst on " + 
	    		" lbsampledtls.test_id = test_mst.test_id " + 
	    		" inner join lab.m_report_type_master reportType on " + 
	    		" test_mst.report_type_id = reportType.report_type_id " + 
	    		" inner join patient.t_patient_registration pati_mst on " + 
	    		" lbsampledtls.patient_id = pati_mst.patient_id " + 
	    		" inner join m_prefix_master pre_mst on " + 
	    		" pati_mst.prefix_id = pre_mst.prefix_id " + 
	    		" inner join m_gender_master gen_mst on " + 
	    		" pati_mst.gender_id = gen_mst.gender_id " + 
	    		" inner join lab.m_test_master test_master on " + 
	    		" test_master.test_id = lbsampledtls.test_id " + 
	    		" inner join lab.m_container_master contai_mst on " + 
	    		" contai_mst.container_id = lbsampledtls.container_id " + 
	    		" inner join lab.m_sample_master samp_mst on " + 
	    		" samp_mst.sample_id = test_master.sample_id " + 
	    		" inner join public.t_lab_configuration lab_config on " + 
	    		" lab_config.unit_id = lbsampledtls.org_unit_id " + 
	    		" left join doctor.m_sub_speciality_master subspe_mst on " + 
	    		" subspe_mst.sub_speciality_id = lbsampledtls.sub_dept_id " + 
	    		" left join doctor.m_doctor_master doct_mst on " + 
	    		" order_master.ord_doctor_id = doct_mst.doctor_id " + 
	    		" left join doctor.m_speciality_master speciality_master on " + 
	    		" speciality_master.speciality_id = order_master.ord_doc_spl_id " + 
	    		 " left join adt.t_admission ta on ta.admission_id = order_master.admission_id and ta.status='A' "
				+ " left join opd.t_patient_sponsor_details tpsd "
				+ "left join public.t_company_master tcm  on tcm.company_id  =  tpsd.payee_id "
				+ " on order_master.encounter_id = tpsd.encounter_id and tpsd.priority_id = 1 and tpsd.status='A' "
				+ " left join opd.t_patient_sponsor_details tpsd2 "
				+ " on tpsd2.admission_id = ta.admission_id and tpsd2.priority_id = 1 and tpsd2.status='A' "
				+ " left join public.t_company_master tcm1  on tcm1.company_id  =  tpsd2.payee_id "
	    		+" left join adt.t_admission_details admission_dtls on " + 
	    		" order_master.admission_id = admission_dtls.admission_id " + 
	    		" and admission_dtls.status = 'A' " + 
	    		" and admission_dtls.nursing_acceptance = 'Y' " + 
	    		" left join adt.m_ward_master wardmast on " + 
	    		" admission_dtls.ward_id = wardmast.ward_id " + 
	    		" left join adt.m_bed_master bedmst on " + 
	    		" admission_dtls.bed_id = bedmst.bed_id " + 
	    		" left join lab.t_panel_master panel_master on " + 
	    		" panel_master.panel_id = lbsampledtls.profile_id " + 
	    		" left join t_referal_details dtls1 on " + 
	    		" case " + 
	    		" when order_master.encounter_id > 0 then dtls1.encounter_id = order_master.encounter_id " + 
	    		" else dtls1.admission_id = order_master.admission_id " + 
	    		" end " + 
	    		" left join adt.m_referral_master refM on " + 
	    		" dtls1.referral_id = refM.referral_id " + 
	    		" left join opd.t_encounter_master tem on " + 
	    		" tem.encounter_id = order_master.encounter_id " + 
	    		" where " + 
	    		" lbsampledtls.lab_sample_dtls_id = :labSampleDetId ";
	    		
		SampleAcceptancePendingDto dto=(SampleAcceptancePendingDto)sessionFactory.getCurrentSession().createSQLQuery(query)
				.setParameter("labSampleDetId", labSampleDetailId)
				.setResultTransformer(Transformers.aliasToBean(SampleAcceptancePendingDto.class)).uniqueResult();
		  return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, null,dto);
		} catch (HibernateException he) {
			logger.error("Exception in getDetailsBySampleDetailId", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getDetailsBySampleDetailId", e);
			throw new ServiceException(e.getMessage());
		}
		}
}

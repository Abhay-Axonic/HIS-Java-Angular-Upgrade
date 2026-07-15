package com.param.lis.histopathology.transaction.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.histo.OutSourceMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ServiceException;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.lis.common.dto.CommonAutoCompleteDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.common.StringCommonMethods;
import com.param.lis.histopathology.transaction.dto.OutSourceMasterDto;
import com.param.lis.microbiology.transaction.dao.IincubationObservationDao;
import com.param.lis.transaction.dto.SearchCommonDto;
import com.param.opd.coversheet.dto.RoaMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class OutSourceMasterDaoImpl extends GenericDao<OutSourceMaster, Integer>
implements IOutSourceMasterDao, ICommonConstants, IError,ITransactionConstants
{
	
	final static Logger logger = Logger.getLogger(OutSourceMasterDaoImpl.class);
	
	public OutSourceMasterDaoImpl() 
	{
		super(OutSourceMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;


	@Autowired
	IincubationObservationDao iincubationObservationDao;

	
	@Override
	public Response listOutSourceMaster(OutSourceMasterDto outSourceMasterDto) throws ApplicationException {
		try
		{
			
			 Integer fromSeroOrMicro = 0;
			 List<OutSourceMasterDto> list  = new ArrayList<>();
					 
			 if(outSourceMasterDto.getIsSampleAcceptanceApplicable() == 'Y') {
					
				 fromSeroOrMicro = 1;
					
				}else if(outSourceMasterDto.getIsSendToCrApplicable() == 'Y'){
					
					 fromSeroOrMicro = 1;
				}
			 if(fromSeroOrMicro == 1) {
				list = getSeroAndMicroOutSourceTestList(outSourceMasterDto);
			 }
			
			
			Date date = TimeZoneComponent.getDateByzone(outSourceMasterDto.getOrgUnitId());
			String query = sessionFactory.getCurrentSession().getNamedQuery("GET_PAGINATED_OUT_SOURCE_PENDING_LIST")
					.getQueryString();
			StringBuffer sb = new StringBuffer(query);
			sb.append(" AND to_char(lbsampledtls.updated_date, 'YYYY-MM-DD') >= :startDate");
			sb.append(" AND to_char(lbsampledtls.updated_date, 'YYYY-MM-DD') <= :endDate");
			sb.append(" ORDER BY lbsampledtls.updated_date");
			List<Integer> statusId = new ArrayList<Integer>();
			if(outSourceMasterDto.getIsSampleAcceptanceApplicable() == 'Y') {
				
				statusId = Arrays.asList(SAMPLE_SEND_TO_DEPT,OP_IP_ACCEPTED);
				
			}else if(outSourceMasterDto.getIsSendToCrApplicable() == 'Y'){
				
				statusId = Arrays.asList(SAMPLE_SEND_TO_CR,SAMPLE_COLLECTED);
			}else {
				statusId = Arrays.asList(SAMPLE_COLLECTED);
			}
			
			Query sql = sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("orgId", outSourceMasterDto.getOrgId())
					.setParameter("orgUnitId", outSourceMasterDto.getOrgUnitId())
					.setParameter("startDate", GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd"))
					.setParameter("endDate", GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd"))
					.setParameterList("sampleStatusIds", statusId)
					.setFirstResult(outSourceMasterDto.getOffset() != null ? outSourceMasterDto.getOffset() : 0)
					.setMaxResults(outSourceMasterDto.getRecordPerPage() != null ? outSourceMasterDto.getRecordPerPage() : 50);
			List<OutSourceMasterDto> listOutSourceMasterDto = sql.setResultTransformer(Transformers.aliasToBean(OutSourceMasterDto.class)).list();
			
			  if(list != null && list.isEmpty()) {
	        		listOutSourceMasterDto.addAll(list);
	        	}
			
			if(listOutSourceMasterDto.isEmpty())
			{
				return new Response(ERROR, ERR_500, "No Out Source Records Found.", null, null);
			}
			else{
				return new Response(SUCCESS, SUCCESS_200, null, listOutSourceMasterDto, null);
			}
			
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	private List<OutSourceMasterDto> getSeroAndMicroOutSourceTestList(OutSourceMasterDto outSourceMasterDto) {
		try
		{
			
			Date date = TimeZoneComponent.getDateByzone(outSourceMasterDto.getOrgUnitId());
			String query = sessionFactory.getCurrentSession().getNamedQuery("SEROANDMICRO_OUT_SOURCE_PENDING_LIST")
					.getQueryString();
			StringBuffer sb = new StringBuffer(query);
			sb.append(" AND to_char(lbsampledtls.updated_date, 'YYYY-MM-DD') >= :startDate ");
			sb.append(" AND to_char(lbsampledtls.updated_date, 'YYYY-MM-DD') <= :endDate ");
			sb.append(" ORDER BY lbsampledtls.updated_date ");
			
			List<Integer> statusId = new ArrayList<Integer>();
				statusId = Arrays.asList(SAMPLE_COLLECTED);
			
			List<OutSourceMasterDto> listOutSourceMasterDto = sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("orgId", outSourceMasterDto.getOrgId())
					.setParameter("orgUnitId", outSourceMasterDto.getOrgUnitId())
					.setParameter("startDate", GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd"))
					.setParameter("endDate", GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd"))
					.setParameterList("sampleStatusIds", statusId)
					.setFirstResult(outSourceMasterDto.getOffset() != null ? outSourceMasterDto.getOffset() : 0)
					.setMaxResults(outSourceMasterDto.getRecordPerPage() != null ? outSourceMasterDto.getRecordPerPage() : 50)
					.setResultTransformer(Transformers.aliasToBean(OutSourceMasterDto.class)).list();
			
			return listOutSourceMasterDto;
			
		} catch (Exception e)
		{
			logger.error("Exection", e);
			return null;
		}
	}

	@Override
	public Response getOutSourceMasterCount(OutSourceMasterDto outSourceMasterDto) throws ApplicationException {
		try
		{
			Date date = TimeZoneComponent.getDateByzone(outSourceMasterDto.getOrgUnitId());
			String query = sessionFactory.getCurrentSession().getNamedQuery("GET_OUT_SOURCE_PENDING_COUNT")
					.getQueryString();
			StringBuffer sb = new StringBuffer(query);
			sb.append(" AND to_char(lbsampledtls.updated_date, 'YYYY-MM-DD') >= :startDate ");
			sb.append(" AND to_char(lbsampledtls.updated_date, 'YYYY-MM-DD') <= :endDate ");

			List<Integer> statusId = new ArrayList<Integer>();
			if(outSourceMasterDto.getIsSampleAcceptanceApplicable() == 'Y') {
				
				statusId = Arrays.asList(SAMPLE_SEND_TO_DEPT,OP_IP_ACCEPTED);
				
			}else if(outSourceMasterDto.getIsSendToCrApplicable() == 'Y'){
				
				statusId = Arrays.asList(SAMPLE_SEND_TO_CR,SAMPLE_COLLECTED);
			}else {
				statusId = Arrays.asList(SAMPLE_COLLECTED);
			}
			
			BigInteger outSourceMasterCount = (BigInteger) sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString())
					.setParameter("orgId", outSourceMasterDto.getOrgId())
					.setParameter("orgUnitId", outSourceMasterDto.getOrgUnitId())
					.setParameter("startDate", GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd"))
					.setParameter("endDate", GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd"))
					.setParameterList("sampleStatusIds", statusId)
					.uniqueResult();
			if (outSourceMasterCount!=null)
			{
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, outSourceMasterCount);
			} else
			{
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	

	@Override
	public Response saveOutSourceMasterDetails(OutSourceMasterDto outSourceMasterDto) throws ApplicationException 
	{
		try
		{
			OutSourceMaster outSourceMst = mapper.map(outSourceMasterDto, OutSourceMaster.class,
					"OutSourceMasterDtoToOutSourceMaster");
			outSourceMst.setAccPayableMstId(outSourceMasterDto.getAccPayableMstId());
			OutSourceMaster outSourceMasterDtoObj = save(outSourceMst);
			if (outSourceMasterDtoObj != null)
			{
				return new Response(SUCCESS, SUCCESS_200, "Out Source Details Added Successfully.", null, null);
				
			} else
			{
				return new Response(ERROR, ERR_500, "Failed to Added Out Source Details.", null, null);
			}
		}catch(HibernateException ex){
			logger.error("Exection", ex);throw ex;
		} catch (Exception e)
		{
			logger.error("Exection", e);throw e;
		}
				
	}

	@Override
	public Response searchOutSourcelistPatient(SearchCommonDto searchCommonDto) throws ApplicationException {
		List<CommonAutoCompleteDto>  patientList  = null; 
		try 
		{
			patientList = (List<CommonAutoCompleteDto>) sessionFactory.getCurrentSession()
					.getNamedQuery("LAB_PATIENT_SEARCH")
					.setParameter("orgId", searchCommonDto.getOrgId())
					//.setParameter("orgUnitId", searchCommonDto.getOrgUnitId())
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
	public Response getFilteredOutSourceList(SearchDto searchDto) throws ApplicationException {
		 try
		    {
		        
			 Integer fromSeroOrMicro = 0;
			 List<OutSourceMasterDto> list  = new ArrayList<>();
					 
			 if(searchDto.getIsSampleAcceptanceApplicable() == 'Y') {
					
				 fromSeroOrMicro = 1;
					
				}else if(searchDto.getIsSendToCrApplicable() == 'Y'){
					
					 fromSeroOrMicro = 1;
				}
			 if(fromSeroOrMicro == 1) {
				list = getSeroAndMicroOutSourceTest(searchDto);
			 }
			 
			 String getOutSourceListQuery =sessionFactory.getCurrentSession().getNamedQuery("GET_PAGINATED_OUT_SOURCE_PENDING_LIST").getQueryString().toString();
		        
		     	        
		        StringBuilder getPendingOutSourceListSearchQueryPart =new StringBuilder(getOutSourceListQuery);
		        
				 if (searchDto.getFromDt() != null ) {
					 getPendingOutSourceListSearchQueryPart.append(
			            " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDate");
			      }
			      if (searchDto.getToDt() != null ) {
			    	  getPendingOutSourceListSearchQueryPart.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDate ");
			      }
		        
		        if(searchDto.getPatientId()!=null &&searchDto.getPatientId() >0)
		        	getPendingOutSourceListSearchQueryPart.append(" AND pati_mst.patient_id =:patientId");
		        
		        if(searchDto.getVisitTypes()!=null && !searchDto.getVisitTypes().isEmpty())
		        	getPendingOutSourceListSearchQueryPart.append(" AND visit_type_master.visit_type_id IN (:visitTypes)");
		        
		        if(searchDto.getTestTypes()!=null && !searchDto.getTestTypes().isEmpty())
		        	getPendingOutSourceListSearchQueryPart.append(" AND lbsampledtls.test_id IN (:testTypes)");
		      
		          
		        if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
		        	getPendingOutSourceListSearchQueryPart.append("  AND lbsampledtls.assign_unit  =:assignUnit ");
		        
		        if (searchDto.getUhidNumber()!= null && searchDto.getUhidNumber() != "")
		        	getPendingOutSourceListSearchQueryPart.append( " AND lower(pati_mst.uhid_number) LIKE :uhidNumber");
		        
		        Query createdQuery=sessionFactory.getCurrentSession().createSQLQuery(getPendingOutSourceListSearchQueryPart.toString());
		        
		        List<Integer> statusId = new ArrayList<Integer>();
				if(searchDto.getIsSampleAcceptanceApplicable() == 'Y') {
					
					statusId = Arrays.asList(SAMPLE_SEND_TO_DEPT,OP_IP_ACCEPTED);
					
				}else if(searchDto.getIsSendToCrApplicable() == 'Y'){
					
					statusId = Arrays.asList(SAMPLE_SEND_TO_CR,SAMPLE_COLLECTED);
				}else {
					statusId = Arrays.asList(SAMPLE_COLLECTED);
				}
				
		        Query sql = createdQuery
		        		.setParameter("orgId", searchDto.getOrgId())
						.setParameter("orgUnitId", searchDto.getOrgUnitId())
						.setParameterList("sampleStatusIds", statusId);
				if (searchDto.getFromDt() != null ) {
					  sql.setParameter("fromDate", searchDto.getFromDt());
				}
				if (searchDto.getToDt() != null ) {
					 sql.setParameter("toDate", searchDto.getToDt());
				}
				if(searchDto.getPatientId()!=null &&searchDto.getPatientId() >0){
					sql.setParameter("patientId", searchDto.getPatientId());
				}
				if(searchDto.getVisitTypes()!=null && !searchDto.getVisitTypes().isEmpty()){
					List<Integer> visitTypeIds = searchDto.getVisitTypes()
							.stream()
							.map(obj -> obj.getId()) // Replace `getId()` with the correct method
							.collect(Collectors.toList());

					sql.setParameterList("visitTypes", visitTypeIds);
				}
				if(searchDto.getTestTypes()!=null && !searchDto.getTestTypes().isEmpty()){
					sql.setParameterList("testTypes", searchDto.getTestTypes());
				}
				if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0){
					sql.setParameter("assignUnit", searchDto.getAssignUnit());
				}
				if (searchDto.getUhidNumber()!= null && searchDto.getUhidNumber() != ""){
					 sql.setParameter("uhidNumber", "%" + searchDto.getUhidNumber().toLowerCase() + "%");
				}
				List<OutSourceMasterDto> listOutSourceMasterDto = sql.setResultTransformer(Transformers.aliasToBean(OutSourceMasterDto.class)).list();
		        
		        if(list != null && list.isEmpty()) {
	        		listOutSourceMasterDto.addAll(list);
	        	}
		        
		        if(listOutSourceMasterDto.isEmpty())
		        {
		        	
		          return new Response(SUCCESS, SUCCESS_200_MESSAGE,"No Records Found.",listOutSourceMasterDto , null);
		        }
		        else {
		        	
		          return new Response(SUCCESS, SUCCESS_200_MESSAGE,null, listOutSourceMasterDto, null);
		        }
		       
		        
		    } catch (Exception e)
		    {
		        logger.error("Exection", e);
		        throw e;
		    }
	}
	
	
	private List<OutSourceMasterDto> getSeroAndMicroOutSourceTest(SearchDto searchDto) {
		 try
		    {

				Date date = TimeZoneComponent.getDateByzone(searchDto.getOrgUnitId());
			 String getOutSourceListQuery =sessionFactory.getCurrentSession().getNamedQuery("SEROANDMICRO_OUT_SOURCE_PENDING_LIST").getQueryString().toString();
		        
		     	        
		        StringBuilder getPendingOutSourceListSearchQueryPart =new StringBuilder(getOutSourceListQuery);
		        
				 if (searchDto.getFromDate() != null ) {
					 getPendingOutSourceListSearchQueryPart.append(
			            " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDate ");
			      }else if(searchDto.getFromDt() != null){
					 getPendingOutSourceListSearchQueryPart.append(
							 " AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt ");
				 }else{
					 getPendingOutSourceListSearchQueryPart.append(" AND to_char(lbsampledtls.updated_date, 'YYYY-MM-DD') >= :currentDate ");
				 }

			      if (searchDto.getToDate() != null ) {
			    	  getPendingOutSourceListSearchQueryPart.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDate ");
			      }else  if (searchDto.getToDt() != null ) {
					  getPendingOutSourceListSearchQueryPart.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt ");
				  }else{
					  getPendingOutSourceListSearchQueryPart.append(" AND to_char(lbsampledtls.updated_date, 'YYYY-MM-DD') <= :currentDate ");
				  }
			     
			      
		        if(searchDto.getPatientId()!=null &&searchDto.getPatientId() >0)
		        	getPendingOutSourceListSearchQueryPart.append(" AND pati_mst.patient_id =:patientId ");
		        
		        if(searchDto.getVisitTypes()!=null && !searchDto.getVisitTypes().isEmpty())
		        	getPendingOutSourceListSearchQueryPart.append(" AND visit_type_master.visit_type_id IN (:visitTypes)");
		        
		        if(searchDto.getTestTypes()!=null && !searchDto.getTestTypes().isEmpty())
		        	getPendingOutSourceListSearchQueryPart.append(" AND lbsampledtls.test_id IN (:testTypes)");
		      
		          
		        if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
		        	getPendingOutSourceListSearchQueryPart.append("  AND lbsampledtls.assign_unit  =:assignUnit ");
		        
		        if (searchDto.getUhidNumber()!= null && searchDto.getUhidNumber() != "")
		        	getPendingOutSourceListSearchQueryPart.append( " AND lower(pati_mst.uhid_number) LIKE :uhidNumber ");
		        
		        Query createdQuery=sessionFactory.getCurrentSession().createSQLQuery(getPendingOutSourceListSearchQueryPart.toString());
		        
		        List<Integer> statusId = new ArrayList<Integer>();
				
					statusId = Arrays.asList(SAMPLE_COLLECTED);
				
		        Query query = createdQuery
		        		.setParameter("orgId", searchDto.getOrgId())
						.setParameter("orgUnitId", searchDto.getOrgUnitId())
						.setParameterList("sampleStatusIds", statusId);
				if (searchDto.getFromDate() != null ) {
					query.setParameter("fromDate", GlobalCommonDateUtils.getStringDate(searchDto.getFromDate(), "yyyy-MM-dd"));
					}
				else if(searchDto.getFromDt() != null){
					query.setParameter("fromDt", searchDto.getFromDt());
				}else{
					query.setParameter("currentDate", GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd"));
				}

				if (searchDto.getToDate() != null ) {
					query.setParameter("toDate", GlobalCommonDateUtils.getStringDate(searchDto.getToDate(), "yyyy-MM-dd"));
				}else if (searchDto.getToDt() != null ) {
					query.setParameter("toDt",searchDto.getToDt());
				}
				if(searchDto.getPatientId()!=null &&searchDto.getPatientId() >0){
					query.setParameter("patientId", searchDto.getPatientId());
				}
				if(searchDto.getVisitTypes()!=null && !searchDto.getVisitTypes().isEmpty()){
					query.setParameterList("visitTypes", searchDto.getVisitTypes());
				}
				if(searchDto.getTestTypes()!=null && !searchDto.getTestTypes().isEmpty()){
					query.setParameterList("testTypes", searchDto.getTestTypes());
				}
				if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0){
					query.setParameter("assignUnit", searchDto.getAssignUnit());
				}
				if (searchDto.getUhidNumber()!= null && searchDto.getUhidNumber() != ""){
					query.setParameter("uhidNumber", "%" + searchDto.getUhidNumber().toLowerCase() + "%");
				}
				List<OutSourceMasterDto> listOutSourceMasterDto =  query.setResultTransformer(Transformers.aliasToBean(OutSourceMasterDto.class)).list();
		        
		      return  listOutSourceMasterDto;
		        
		    } catch (Exception e)
		    {
		        logger.error("Exection", e);
		        return null;
		    }
	}

	@Override
	public Response getFilteredOutSourceCommonList(SearchDto searchDto) throws ApplicationException {
		 try
		    {
		        
			 String getPendingOutSourceListQuery =sessionFactory.getCurrentSession().getNamedQuery("GET_PENDING_OUT_SOURCE_SAMPLE_DISPATCH_LIST").getQueryString().toString();
		        
		     	        
		        StringBuilder getPendingOutSourceListSearchQueryPart =new StringBuilder(getPendingOutSourceListQuery);
		        
				 if (searchDto.getFromDt() != null ) {
					 getPendingOutSourceListSearchQueryPart.append(
			            " AND to_char(lab_sample_dtls.updated_date,'YYYY-MM-DD') >=:fromDt ");
			      }
			      if (searchDto.getToDt() != null ) {
			    	  getPendingOutSourceListSearchQueryPart.append(" AND to_char(lab_sample_dtls.updated_date,'YYYY-MM-DD') <=:toDt ");
			      }
			       
			      if (searchDto.getOutSourceLabName() != null && !searchDto.getOutSourceLabName().isEmpty() ) {
			    	  getPendingOutSourceListSearchQueryPart.append(" AND lower(account_payable_mst.acc_desc) like :outSourceLabName");
			    	  
			      }
		        if(searchDto.getPatientId()!=null &&searchDto.getPatientId() >0)
		        	getPendingOutSourceListSearchQueryPart.append(" AND pati_mst.patient_id =:patientId ");
		        
		        if(searchDto.getVisitTypes()!=null && !searchDto.getVisitTypes().isEmpty())
		        	getPendingOutSourceListSearchQueryPart.append(" AND visit_type_master.visit_type_id IN (:visitTypes)");
		        
		        if(searchDto.getTestTypes()!=null && !searchDto.getTestTypes().isEmpty())
		        	getPendingOutSourceListSearchQueryPart.append(" AND lab_sample_dtls.test_id IN (:testTypes)");
		      
		        if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
		        	getPendingOutSourceListSearchQueryPart.append("  and lab_sample_dtls.assign_unit  =:assignUnit ");
		        
		        if (searchDto.getUhidNumber()!= null && searchDto.getUhidNumber() != "")
		        	getPendingOutSourceListSearchQueryPart.append( " AND lower(pati_mst.uhid_number) LIKE :uhidNumber ");
		        
		        getPendingOutSourceListSearchQueryPart.append("  ORDER BY lab_sample_dtls.updated_date  ");
		        Query createdQuery=sessionFactory.getCurrentSession().createSQLQuery(getPendingOutSourceListSearchQueryPart.toString());
		        
		        Query query= createdQuery
		        		.setParameter("orgId", searchDto.getOrgId())
						.setParameter("orgUnitId", searchDto.getOrgUnitId())
						.setParameter("sampleStatusId", searchDto.getSampleStatusId());
				if (searchDto.getFromDt() != null ) {
					query.setParameter("fromDt", searchDto.getFromDt());
				}
				if (searchDto.getToDt() != null ) {
					query.setParameter("toDt", searchDto.getToDt());
				}

				if (searchDto.getOutSourceLabName() != null && !searchDto.getOutSourceLabName().isEmpty() ) {
					query.setParameter("outSourceLabName", "%" + searchDto.getOutSourceLabName().toLowerCase() + "%");
				}
				if(searchDto.getPatientId()!=null &&searchDto.getPatientId() >0){
					query.setParameter("patientId", searchDto.getPatientId());
				}
				List<Integer>  visitTypes = new ArrayList<Integer>();
				if(searchDto.getVisitTypes()!=null && !searchDto.getVisitTypes().isEmpty()){
					visitTypes = searchDto.getVisitTypes().stream()
							.map(obj -> obj.getId())
							.collect(Collectors.toList());
					query.setParameterList("visitTypes", visitTypes);
				}
				List<Integer>  testTypes = new ArrayList<Integer>();
				if(searchDto.getTestTypes()!=null && !searchDto.getTestTypes().isEmpty()){
					testTypes = searchDto.getTestTypes().stream()
							.map(obj -> obj.getId())
							.collect(Collectors.toList());
					query.setParameterList("testTypes", testTypes);
				}
				if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0){
					query.setParameter("assignUnit", searchDto.getAssignUnit());
				}
				if (searchDto.getUhidNumber()!= null && searchDto.getUhidNumber() != ""){
					query.setParameter("uhidNumber", "%" + searchDto.getUhidNumber().toLowerCase() + "%");
				}
				List<OutSourceMasterDto> listOutSourceMasterDto =  query.setResultTransformer(Transformers.aliasToBean(OutSourceMasterDto.class)).list();
		        
		        
		        if(listOutSourceMasterDto.isEmpty())
		        {
		          return new Response(SUCCESS, SUCCESS_200_MESSAGE,"No Records Found.",listOutSourceMasterDto , null);
		        }
		        else {
		          return new Response(SUCCESS, SUCCESS_200_MESSAGE,null, listOutSourceMasterDto, null);
		        }
		       
		        
		    } catch (Exception e)
		    {
		        logger.error("Exection", e);
		        throw e;
		    }
	}

	@Override
	public Response getOutsourceReportUrl(List<Integer> labSampleDtlsIdsList, Integer unitId, Integer orgId)
			throws ApplicationException {
	try {
			
			String query = "select test.document_path as \"documentPath\" "
					+ " from opd.t_encounter_doc_details test "
					+ " where test.lab_sample_dtls_id in (:list) "
					+ " and test.organisation_id =:orgId "
					+ " and test.unit_id =:unitId";
			
			List<SmsUtilityConfigInfoDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameterList("list", labSampleDtlsIdsList)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(SmsUtilityConfigInfoDto.class)).list();
			
			 return new Response(SUCCESS, SUCCESS_200_MESSAGE,null, list, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
			logger.error("Exection", e);
			throw new ServiceException(e.getMessage());
		}
	}

	

}

package com.param.lis.histopathology.transaction.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.histo.TSpecimanMaster;
import com.param.entity.lis.histo.TSubSpecimanMaster;
import com.param.lis.common.dto.CommonAutoCompleteDto;
import com.param.lis.common.dto.CommonDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.common.StringCommonMethods;
import com.param.lis.histopathology.transaction.dto.TGrossMasterDto;
import com.param.lis.histopathology.transaction.dto.TMicroExaDetailsDto;
import com.param.lis.histopathology.transaction.dto.TSpecimanMasterDto;
import com.param.lis.histopathology.transaction.dto.TSubSpecimanMasterDto;
import com.param.lis.transaction.dto.SearchCommonDto;
import com.param.lis.unit.dto.TestMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class HistoMacroScopicExaminationDaoImpl extends GenericDao<TSpecimanMaster, Integer>
implements IHistoMacroScopicExaminationDao, ICommonConstants, IError,ITransactionConstants
{
	
	final static Logger logger = Logger.getLogger(HistoMacroScopicExaminationDaoImpl.class);
	
	public HistoMacroScopicExaminationDaoImpl() 
	{
		super(TSpecimanMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;
	
	@Autowired
	private ITSubSpecimemDao iTSubSpecimemDao;

	@Override
	public Response listSpecimanReceipt(SearchDto searchDto) throws ApplicationException {
		try
		{
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

			Query query= sessionFactory.getCurrentSession()
					.createQuery(getCollectedSpecimanQuery(searchDto))
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
					.setParameter("sampleStatusId", SPECIMAN_ACCEPTED);
			if (searchDto.getFromDt() != null ){
				query.setParameter("fromDt", searchDto.getFromDt());
			}
			if (searchDto.getToDt() != null ){
				query.setParameter("toDt", searchDto.getToDt());
			}
			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
				query.setParameter("patientId", searchDto.getPatientId());
			}
			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0){
				query.setParameterList("visitTypes", visitTypes);
			}
			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0){
				query.setParameterList("subDepts", subDeptIds);
			}
			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0){
				query.setParameterList("testTypes", testTypes);
			}
			if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0){
				query.setParameter("labSampleId", searchDto.getLabSampleId());
			}
			if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0){
				query.setParameterList("specimanTypes", specimanTypes);
			}
			query.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0);
			query.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50);
			List<TSpecimanMasterDto> listTSpecimanMasterDto = query.setResultTransformer(Transformers.aliasToBean(TSpecimanMasterDto.class)).list();
			
			if(listTSpecimanMasterDto.isEmpty())
			{
				return new Response(SUCCESS, SUCCESS_200, "No Speciman Records Found.", null, null);
			}
			else{
				return new Response(SUCCESS, SUCCESS_200, null, listTSpecimanMasterDto, null);
			}
			
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getCollectedSpecimensCount(SearchDto searchDto) throws ApplicationException {
		try
		{
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

			Query query = sessionFactory.getCurrentSession()
					.createQuery(getCollectedSpecimanQueryCount(searchDto))
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
					.setParameter("sampleStatusId", SPECIMAN_ACCEPTED);
			if (searchDto.getFromDt() != null ){
				query.setParameter("fromDt", searchDto.getFromDt());
			}
			if (searchDto.getToDt() != null ){
				query.setParameter("toDt", searchDto.getToDt());
			}
			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
				query.setParameter("patientId", searchDto.getPatientId());
			}
			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0){
				query.setParameterList("visitTypes", visitTypes);
			}
			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0){
				query.setParameterList("subDepts", subDeptIds);
			}
			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0){
				query.setParameterList("testTypes", testTypes);
			}
			if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0){
				query.setParameter("labSampleId", searchDto.getLabSampleId());
			}
			if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0){
				query.setParameterList("specimanTypes", specimanTypes);
			}
			Long specimenListCount = (Long)	query.uniqueResult();
			if (specimenListCount!=null)
			{
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, specimenListCount);
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
	public Response saveMicroscopicExaminationDetails(TSpecimanMasterDto tSpecimanMasterDto)
			throws ApplicationException 
	{
		try
		{
		 TSpecimanMaster tSpecimenMaster = mapper.map(tSpecimanMasterDto, TSpecimanMaster.class,"TSpecimanMasterDtoToTSpecimanMaster");
		 tSpecimenMaster.setIsDeleted('N');
		 TSpecimanMaster tSpecimenMasterObj = update(tSpecimenMaster);
		  if(!tSpecimenMaster.getListTSubSpecimanMaster().isEmpty())
		  {
			  for (Iterator iterator = tSpecimenMaster.getListTSubSpecimanMaster().iterator(); iterator.hasNext();) 
			  {
				TSubSpecimanMaster tSubSpecimanMaster = (TSubSpecimanMaster) iterator.next();
				tSubSpecimanMaster.setLabSampleDtlsId(tSpecimenMasterObj.getLabSampleDtlsId());
				tSubSpecimanMaster.settSpecimanId(tSpecimenMasterObj.gettSpecimanId());
				tSubSpecimanMaster.setCreatedBy(tSpecimenMasterObj.getUpdatedBy());
				tSubSpecimanMaster.setCreatedDate(tSpecimenMasterObj.getUpdatedDate());
				iTSubSpecimemDao.saveSubSpecimanDetails(tSubSpecimanMaster);
			  }
			  return new Response(SUCCESS, SUCCESS_200, GROSS_CREATE_SUCCESS, null, null);
		  }
		  else 
		  {
			  return new Response(ERROR, ERR_500, GROSS_CREATE_FAIL, null, null);
		  }
			
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response saveSpecimanDetails(TSpecimanMasterDto tSpecimanMasterDto) throws ApplicationException 
	{
		try
		{
			TSpecimanMaster tSpecimanMaster = mapper.map(tSpecimanMasterDto, TSpecimanMaster.class,
					"TSpecimanMasterDtoToTSpecimanMaster");
			TSpecimanMaster tSpecimanObj = save(tSpecimanMaster);
			if (tSpecimanObj != null)
			{
				return new Response(SUCCESS, SUCCESS_200, "Specimen Accepted Successfully.", null, tSpecimanObj);
			} else
			{
				return new Response(ERROR, ERR_500, "Failed to Accept Specimen.", null, null);
			}
		}catch(HibernateException ex){
			logger.error("Exection", ex);throw ex;
		} catch (Exception e)
		{
			logger.error("Exection", e);throw e;
		}
	}

	
	@Override
	public Response searchAcceptedSpecimanByPatient(SearchCommonDto searchCommonDto) throws ApplicationException 
	{
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
	public Response searchPatientbyVisitType(SearchCommonDto searchCommonDto) throws ApplicationException {
		 List<CommonAutoCompleteDto>  visityTypeList  = null; 
		try
		{
			visityTypeList = 	(List<CommonAutoCompleteDto>) sessionFactory.getCurrentSession()
			.createSQLQuery(" SELECT "
					 +"	visit_mst.visit_type_id AS id, "
					 +"	visit_mst.visit_type_code AS label "
					 +"FROM "
					 +"	public.m_visit_type_master visit_mst "
					 +"ORDER BY "
					 +"	visit_mst.visit_type_code ASC LIMIT 4 ")
                       .setResultTransformer(Transformers.aliasToBean(CommonAutoCompleteDto.class)).list();
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, visityTypeList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response searchBySpecimenType(SearchCommonDto searchCommonDto) throws ApplicationException {
		List<CommonAutoCompleteDto>  organismList  = null; 
		try
		{
			organismList = (List<CommonAutoCompleteDto>) sessionFactory.getCurrentSession()
					.getNamedQuery("SEARCH_ACCEPTED_SPECIMEN_TYPE")
					.setParameter("orgId", searchCommonDto.getOrgId())
					/*.setParameter("orgUnitId", searchCommonDto.getOrgUnitId())
					.setParameter("sampleStatusId", SPECIMAN_ACCEPTED)*/
					//.setParameter("detpId", searchCommonDto.getDeptId())
					//.setParameter("subDeptId", searchCommonDto.getSubDeptId())
                    .setResultTransformer(Transformers.aliasToBean(CommonAutoCompleteDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, organismList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response searchByTest(SearchCommonDto searchCommonDto) throws ApplicationException {
		List<CommonDto>  testList  = null; 
		try
		{
			testList = (List<CommonDto>) sessionFactory.getCurrentSession()
					.getNamedQuery("SEARCH_SPECIMEN_ACCEPTED_TEST")
					.setParameter("orgId", searchCommonDto.getOrgId())
					.setParameter("orgUnitId", searchCommonDto.getOrgUnitId())
					/*.setParameter("sampleStatusId", SPECIMAN_ACCEPTED)*/
					//.setParameter("detpId", searchCommonDto.getDeptId())
					//.setParameter("subDeptId", searchCommonDto.getSubDeptId())
                    .setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, testList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response accetedSpecimenSearch(SearchDto searchDto) throws ApplicationException
	{
		try
		{
			List<Integer>  subDeptIds = new ArrayList<Integer>();
			List<Integer>  testTypes = new ArrayList<Integer>();
			List<Integer>  visitTypes = new ArrayList<Integer>();
			List<Integer>  specimanTypes = new ArrayList<Integer>();

			StringBuilder getSecimanSearchQuery =new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_SPCIMEN_LIST").getQueryString().toString());
			
			
			if(searchDto.getFromDate()!=null)
				getSecimanSearchQuery.append(" AND tSpecimanMaster.createdDate > :fromDate ");
			
			if(searchDto.getToDate()!=null)
				getSecimanSearchQuery.append(" AND tSpecimanMaster.createdDate < :toDate ");
			
			if(searchDto.getPatientId()!=null &&searchDto.getPatientId() >0)
				getSecimanSearchQuery.append(" AND patientReg.patientId =:patientId ");
			
			if(searchDto.getSpecimanTypes()!=null && searchDto.getSpecimanTypes().size()>0) {
				specimanTypes = searchDto.getSpecimanTypes().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());

				getSecimanSearchQuery.append(" AND tSpecimanMaster.specimanTypeId IN (:specimanTypes)");
			}
			
			if(searchDto.getVisitTypes()!=null && searchDto.getVisitTypes().size()>0) {
				visitTypes = searchDto.getVisitTypes().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());

				getSecimanSearchQuery.append(" AND labSampleMst.visitTypeId IN (:visitTypes)");
			}
			
			if(searchDto.getTestTypes()!=null && searchDto.getTestTypes().size()>0) {
				testTypes = searchDto.getTestTypes().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());

				getSecimanSearchQuery.append(" AND labSampleDetailsMst.testId IN (:testTypes)");
			}
			
			Query createdQuery=sessionFactory.getCurrentSession().createQuery(getSecimanSearchQuery.toString());
			
			
			Query query = createdQuery
				.setParameter("orgId", searchDto.getOrgId())
				.setParameter("orgUnitId", searchDto.getOrgUnitId())
				.setParameter("sampleStatusId", SPECIMAN_ACCEPTED);
			if(searchDto.getFromDate()!=null){
				query.setParameter("fromDate", searchDto.getFromDate());
			}
			if(searchDto.getToDate()!=null){
				query.setParameter("toDate", searchDto.getToDate());
			}
			if(searchDto.getPatientId()!=null &&searchDto.getPatientId() >0){
				query.setParameter("patientId", searchDto.getPatientId());
			}
			if(searchDto.getSpecimanTypes()!=null && searchDto.getSpecimanTypes().size()>0){
				query.setParameterList("specimanTypes", specimanTypes);
			}
			if(searchDto.getVisitTypes()!=null && searchDto.getVisitTypes().size()>0){
				query.setParameterList("visitTypes", visitTypes);
			}
			if(searchDto.getTestTypes()!=null && searchDto.getTestTypes().size()>0){
				query.setParameterList("testTypes", testTypes);
			}
			List<TSpecimanMasterDto> listTSpecimanMasterDto = query.setResultTransformer(Transformers.aliasToBean(TSpecimanMasterDto.class)).list();
			return new Response(SUCCESS, null, null, listTSpecimanMasterDto, null);
			
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
		
		
	}

	@Override
	public Response getMicroscopicExamDataBySubSpecimenId(Integer labSampleDtlsId, Integer orgId, Integer orgUnitId,Character isHistoCyto)
			throws ApplicationException {
		try {
			
			if(isHistoCyto!=null && isHistoCyto=='H')
			{
				
			
				List<Integer> subSpecimanIds = (List<Integer>) sessionFactory.getCurrentSession()
				    .getNamedQuery("GET_SUB_SPECIMAN_BY_LAB_SAMPLEDTLS_ID")
				    .setParameter("orgId", orgId)
				    .setParameter("orgUnitId", orgUnitId)
				    .setParameter("labSampleDtlsId", labSampleDtlsId)
				    .list();
				List<TSubSpecimanMasterDto> list = new ArrayList<>();
				subSpecimanIds.forEach(subSpecimanId -> {
					TSubSpecimanMaster tSubSpecimanMst = (TSubSpecimanMaster) sessionFactory.getCurrentSession()
							.getNamedQuery("GET_MICROSCOPIC_EXAM_DATA_BY_SUB_SPCIMEN_ID")
							.setParameter("orgId", orgId)
							.setParameter("orgUnitId",orgUnitId)
							.setParameter("tSubSpecimanId", subSpecimanId)
		                   .uniqueResult();
				
					 TSubSpecimanMasterDto tSubSpecimanMasterDto = mapper.map(tSubSpecimanMst, TSubSpecimanMasterDto.class,
								"TSubSpecimanMasterDtoToTSubSpecimanMaster");
						list.add(tSubSpecimanMasterDto);
				});
							return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, list, null);
			}
			else if(isHistoCyto!=null && isHistoCyto=='C')
			{
				List<TMicroExaDetailsDto> list = (List<TMicroExaDetailsDto>) sessionFactory.getCurrentSession()
						.getNamedQuery("GET_TOTAL_SLIDES_BY_SPECIMEN")
						.setParameter("orgId", orgId)
						.setParameter("orgUnitId",orgUnitId)
						.setParameter("labSampleDtlsId", labSampleDtlsId)
						.setResultTransformer(Transformers.aliasToBean(TMicroExaDetailsDto.class)).list();
				
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, list, null);
			}
			
		}catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
		return null;
	}
	
	@Override
	public Response getSubSpecimanByLabSampleDtlsId(Integer labSampleDtlsId,Integer orgId, Integer orgUnitId) throws ApplicationException {
		try{
		TSubSpecimanMaster tSubSpecimanMst = (TSubSpecimanMaster) sessionFactory.getCurrentSession()
				.getNamedQuery("GET_SUB_SPECIMAN_BY_LAB_SAMPLE_DETAILS_ID")
				.setParameter("orgId", orgId)
				.setParameter("orgUnitId",orgUnitId)
				.setParameter("labSampleDtlsId", labSampleDtlsId)
               .uniqueResult();	
		     return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, null);
		}catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}	
	

	@Override
	public Response getTestList(Integer orgId, Integer orgUnitId) throws ApplicationException {
		List<TestMasterDto>  testList  = null; 
		try
		{
			testList = (List<TestMasterDto>) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_TEST_LIST")
					.setParameter("orgId", orgId)
					.setParameter("orgUnitId", orgUnitId)
				   .setResultTransformer(Transformers.aliasToBean(TestMasterDto.class)).list();					
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, testList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}


	public String getCollectedSpecimanQuery(SearchDto searchDto)
	{
		
		 StringBuffer query = new StringBuffer();
		 query.append(sessionFactory.getCurrentSession().getNamedQuery("GET_SPCIMEN_LIST").getQueryString());
		
		 if (searchDto.getFromDt() != null ) 
			  query.append(" AND to_char(labSampleDetailsMst.updatedDate,'YYYY-MM-DD') >=:fromDt ");
		  if (searchDto.getToDt() != null ) 
	    	  query.append(" AND to_char(labSampleDetailsMst.updatedDate,'YYYY-MM-DD') <=:toDt ");
	      
		  if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
	    	  query.append(" AND patientReg.patientId =:patientId ");

	      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0)
	    	  query.append(" AND visitMst.visitTypeId IN :visitTypes");

	      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0)
	    	  query.append(" AND testMst.deptId IN :subDepts");
	      
	      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0)
	    	  query.append(" AND testMst.testId in :testTypes");
	      
	      if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0)
	    	  query.append(" AND labSampleMst.labSampleId =:labSampleId");
	      
	      if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0)
	    	  query.append(" AND testMst.specimanId in :specimanTypes");
	      
	      query.append(" order by labSampleDetailsMst.updatedDate");
	      
	      return query.toString();
	}
	
	public String getCollectedSpecimanQueryCount(SearchDto searchDto)
	{
		
		 StringBuffer query = new StringBuffer();
		 query.append(sessionFactory.getCurrentSession().getNamedQuery("GET_SPCIMEN_LIST_COUNT").getQueryString());
		
		 if (searchDto.getFromDt() != null ) 
			  query.append(" AND to_char(labSampleDetailsMst.updatedDate,'YYYY-MM-DD') >=:fromDt ");
		  if (searchDto.getToDt() != null ) 
	    	  query.append(" AND to_char(labSampleDetailsMst.updatedDate,'YYYY-MM-DD') <=:toDt ");
	      
		  if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
	    	  query.append(" AND patientReg.patientId =:patientId ");

	      if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0)
	    	  query.append(" AND visitMst.visitTypeId IN :visitTypes");

	      if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size()>0)
	    	  query.append(" AND testMst.deptId IN :subDepts");
	      
	      if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0)
	    	  query.append(" AND testMst.testId in :testTypes");
	      
	      if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0)
	    	  query.append(" AND labSampleMst.labSampleId =:labSampleId");
	      
	      if (searchDto.getSpecimanTypes() != null && searchDto.getSpecimanTypes().size()>0)
	    	  query.append(" AND testMst.specimanId in :specimanTypes");
	      
	      
	      return query.toString();
	}

}

package com.param.lis.histopathology.transaction.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.histo.TMicroExaDetails;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.common.StringCommonMethods;
import com.param.lis.histopathology.transaction.dto.TGrossMasterDto;
import com.param.lis.histopathology.transaction.dto.TMicroExaDetailsDto;
import com.param.lis.histopathology.transaction.dto.TSubSpecimanMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class MicroScopicExaminationDaoImpl extends GenericDao<TMicroExaDetails, Integer>
    implements IMicroScopicExaminationDao, ICommonConstants, IError, ITransactionConstants {

  final static Logger logger = Logger.getLogger(MicroScopicExaminationDaoImpl.class);

  public MicroScopicExaminationDaoImpl() {
    super(TMicroExaDetails.class);
  }

  @Autowired
  private DozerBeanMapper mapper;


  @Override
  public Response getMicroscopicWorkList(SearchDto searchDto) throws ApplicationException 
  {
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

      Query query = sessionFactory.getCurrentSession()
          .createQuery(getMicroscopicWorkListQuery(searchDto))
          .setParameter("orgId", searchDto.getOrgId())
          .setParameter("orgUnitId", searchDto.getOrgUnitId())
          .setParameterList("sampleStatusIds", HISTO_MICROEXA_STATUS)
          .setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
          .setMaxResults(
        		  searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50);
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
		List<TSubSpecimanMasterDto> listTSubSpecimanMasterDto = query .setResultTransformer(Transformers.aliasToBean(TSubSpecimanMasterDto.class)).list();

      if (listTSubSpecimanMasterDto.isEmpty()) {
        return new Response(ERROR, ERR_500, "No Speciman Records Found.", null, null);
      } else {
        return new Response(SUCCESS, SUCCESS_200, null, listTSubSpecimanMasterDto, null);
      }

    } catch (Exception e) {
      logger.error("Exection", e);
      throw e;
    }
  }

  @Override
  public Response getMicroscopicWorkListCount(SearchDto searchDto)
      throws ApplicationException {
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

      Query query = sessionFactory.getCurrentSession()
    		   .createQuery(getMicroscopicWorkListQueryCount(searchDto))
          .setParameter("orgId", searchDto.getOrgId())
          .setParameter("orgUnitId", searchDto.getOrgUnitId())
          .setParameterList("sampleStatusIds", HISTO_MICROEXA_STATUS);
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
		Long subSpecimenListCount = (Long) query.uniqueResult();
      if (subSpecimenListCount != null) {
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, subSpecimenListCount);
      } else {
        return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
      }

    } catch (Exception e) {
      logger.error("Exection", e);
      throw e;
    }
  }

  @Override
  public Response getMiscroscopicSlides(TSubSpecimanMasterDto tSubSpecimanMasterDto)
      throws ApplicationException {
    try {
      List<TMicroExaDetailsDto> listTMicroExaDetailsDto =
          sessionFactory.getCurrentSession().getNamedQuery("GET_TOTAL_SLIDES_BY_SPECIMEN")
              .setParameter("orgId", tSubSpecimanMasterDto.getOrgId())
              .setParameter("orgUnitId", tSubSpecimanMasterDto.getOrgUnitId())
              .setParameter("labSampleDtlsId", tSubSpecimanMasterDto.getLabSampleDtlsId())
              .setResultTransformer(Transformers.aliasToBean(TMicroExaDetailsDto.class)).list();
      if (listTMicroExaDetailsDto.isEmpty()) {
        return new Response(ERROR, ERR_500, "No Records Found..", null, null);
      } else {
        return new Response(SUCCESS, SUCCESS_200, null, listTMicroExaDetailsDto, null);
      }

    } catch (Exception e) {
      logger.error("Exection", e);
      throw e;
    }
  }

  @Override
  public Response saveSlideExaminationsDetails(List<TMicroExaDetailsDto> listTMicroExaDetailsDto)
      throws ApplicationException {	
    try {
      if (!listTMicroExaDetailsDto.isEmpty()) {
        for (Iterator iterator = listTMicroExaDetailsDto.iterator(); iterator.hasNext();) {
          TMicroExaDetailsDto tMicroExaDetailsDto = (TMicroExaDetailsDto) iterator.next();
          TMicroExaDetails tMicroExaDetails =
              mapper.map(tMicroExaDetailsDto, TMicroExaDetails.class, "TMicroExaDetailsDtoToTMicroExaDetails");
          tMicroExaDetails.setCreatedDate(TimeZoneComponent.getDateByzone(tMicroExaDetailsDto.getOrgUnitId()).getTime());
          tMicroExaDetails.setIsNad(tMicroExaDetailsDto.getIsNad());
          tMicroExaDetails.setImpression(tMicroExaDetailsDto.getImpression());
          save(tMicroExaDetails);
  
        }
        return new Response(SUCCESS, SUCCESS_200, SLIDES_EXAMINATION_SAVE_SUCCESS, null, null);
      } else {
        return new Response(ERROR, ERR_500, SLIDES_EXAMINATION_SAVE_FAIL, null, null);
      }

    } catch (Exception e) {
      logger.error("Exection", e);
      
      throw e;
    }
  }

  public String getMicroscopicWorkListQuery(SearchDto searchDto)
	{
		
		 StringBuffer query = new StringBuffer();
		 query.append(sessionFactory.getCurrentSession().getNamedQuery("GET_SUB_SPCIMEN_LIST").getQueryString());
		
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
	      
	      query.append(" order by ordermst.orderDate");
	      
	      return query.toString();
	}
	
	public String getMicroscopicWorkListQueryCount(SearchDto searchDto)
	{
		
		 StringBuffer query = new StringBuffer();
		 query.append(sessionFactory.getCurrentSession().getNamedQuery("GET_SUB_SPCIMEN_LIST_COUNT").getQueryString());
		
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

	@Override
	public Response getMicroscopicWorkListLabSampleDetails(SearchDto searchDto) throws ApplicationException {
		 try {
			 
			
			 StringBuffer query = new StringBuffer();
			 query.append(sessionFactory.getCurrentSession().getNamedQuery("GETMICROSCOPICEXAMINITON").getQueryString());
			 
		      TSubSpecimanMasterDto listTSubSpecimanMasterDto = (TSubSpecimanMasterDto) sessionFactory.getCurrentSession()
		          .createQuery(query.toString())
		          .setParameter("labSampleDetailsId", searchDto.getLabSampleDtlsId())
		          
		          .setResultTransformer(Transformers.aliasToBean(TSubSpecimanMasterDto.class)).uniqueResult();

		      if (listTSubSpecimanMasterDto == null) {
		        return new Response(ERROR, ERR_500, "No Speciman Records Found.", null, null);
		      } else {
		        return new Response(SUCCESS, SUCCESS_200, "Speciman Record found", null, listTSubSpecimanMasterDto);
		      }

		    } catch (Exception e) {
		      logger.error("Exection", e);
		      throw e;
		    }
	}
	
	@Override
	public Response getMicroscopicWorkListBySubSpeciman(int tSpecimanId) throws ApplicationException {
		try
		{
			List<TMicroExaDetailsDto> listTMicroExaDetailsDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SUB_SPECIMAN_DETAIL")
					.setParameter("tSpecimanId", tSpecimanId)
					.setResultTransformer(Transformers.aliasToBean(TGrossMasterDto.class)).list();
			if (listTMicroExaDetailsDto.isEmpty()) {
		        return new Response(ERROR, ERR_500, "No Records Found..", null, null);
		      } else {
		        return new Response(SUCCESS, SUCCESS_200, null, listTMicroExaDetailsDto, null);
		      }
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}


}

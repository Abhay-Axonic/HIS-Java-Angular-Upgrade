package com.param.lis.histopathology.transaction.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.histo.TBlockMaster;
import com.param.entity.lis.histo.TGrossMaster;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.global.dto.AdmissionNoteDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.common.StringCommonMethods;
import com.param.lis.histopathology.transaction.dto.TGrossMasterDto;
import com.param.lis.histopathology.transaction.dto.TSubSpecimanMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class BlockCreationDaoImpl  extends GenericDao<TBlockMaster, Integer>
implements IBlockCreationDao, ICommonConstants, IError,ITransactionConstants{

	final static Logger logger = Logger.getLogger(BlockCreationDaoImpl.class);
	
	public BlockCreationDaoImpl()
	{
		super(TBlockMaster.class);
	}
	

	
	@Autowired
	private DozerBeanMapper mapper;

	@Override
	public Response getBlockCreationList(SearchDto searchDto) throws ApplicationException {
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

			Query queryObj = sessionFactory.getCurrentSession()
					.createQuery(getBlockCreationQuery(searchDto))
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
				    .setParameter("histoSampleStatus", GROSS_CREATED);
			if (searchDto.getFromDt() != null ) {
				queryObj.setParameter("fromDate", searchDto.getFromDt());
			}
			if (searchDto.getToDt() != null ) {
				queryObj.setParameter("toDate", searchDto.getToDt());
			}
			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
				queryObj.setParameter("patientId", searchDto.getPatientId());
			}

			if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()){

				queryObj.setParameterList("visitTypes", visitTypes);
			}
			if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()){
				queryObj.setParameterList("subDepts", subDeptIds);
			}
			if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()){
				queryObj.setParameterList("testTypes", testTypes);
			}
			if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0){
				queryObj.setParameter("labSampleId", searchDto.getLabSampleId());
			}
			if (searchDto.getSpecimanTypes() != null && !searchDto.getSpecimanTypes().isEmpty()){
				queryObj.setParameterList("specimanTypes", specimanTypes);
			}
			queryObj.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0);
			queryObj.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50);
			List<TSubSpecimanMasterDto> listTSubSpecimanMasterDto =	queryObj.setResultTransformer(Transformers.aliasToBean(TSubSpecimanMasterDto.class)).list();
			
			
			if(listTSubSpecimanMasterDto.isEmpty())
			{
				return new Response(ERROR, ERR_500, "No Speciman Records Found.", null, null);
			}
			else{
				return new Response(SUCCESS, SUCCESS_200, null, listTSubSpecimanMasterDto, null);
			}
			
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getBlockCreationListCount(SearchDto searchDto) throws ApplicationException {
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

			Query queryObj= sessionFactory.getCurrentSession()
					.createQuery(getBlockCreationQueryCount(searchDto))
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
				    .setParameter("histoSampleStatus", GROSS_CREATED);
			if (searchDto.getFromDt() != null ){
				queryObj.setParameter("fromDate", searchDto.getFromDt());
			}
			if (searchDto.getToDt() != null ){
				queryObj.setParameter("toDate", searchDto.getToDt());
			}
			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
				queryObj.setParameter("patientId", searchDto.getPatientId());
			}
			if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty()){
				queryObj.setParameterList("visitTypes", visitTypes);
			}
			if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty()){
				queryObj.setParameterList("subDepts",subDeptIds);
			}
			if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty()){
				queryObj.setParameterList("testTypes", testTypes);
			}
			if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0){
				queryObj.setParameter("labSampleId", searchDto.getLabSampleId());
			}
			if (searchDto.getSpecimanTypes() != null && !searchDto.getSpecimanTypes().isEmpty()){
				queryObj.setParameterList("specimanTypes",specimanTypes);
			}
			Long subSpeciamanCount =(Long)queryObj.uniqueResult();
			if (subSpeciamanCount!=null)
			{
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, subSpeciamanCount);
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
	public Response getCreatedGross(TSubSpecimanMasterDto tSubSpecimanMasterDto) throws ApplicationException
	{
		try
		{
			List<TGrossMasterDto> listTGrossMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_GROSS_LIST_BY_SUBSPECIAMAN")
					.setParameter("orgId", tSubSpecimanMasterDto.getOrgId())
					.setParameter("orgUnitId", tSubSpecimanMasterDto.getOrgUnitId())
					//.setParameter("tSpecimanId", tSubSpecimanMasterDto.gettSpecimanId())
					.setParameter("labSampleDtlsId",tSubSpecimanMasterDto.getLabSampleDtlsId())
					.setResultTransformer(Transformers.aliasToBean(TGrossMasterDto.class)).list();
		
			if(listTGrossMasterDto.isEmpty())
			{
				return new Response(ERROR, ERR_500, "No Gross Found..", null, null);
			}
			else{
				return new Response(SUCCESS, SUCCESS_200, null, listTGrossMasterDto, null);
			}

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response creteBlocks(List<TGrossMasterDto> listTGrossMasterDto) throws ApplicationException
	{
		try
		{
		  if(!listTGrossMasterDto.isEmpty())
		  {
			  LabSampleDetailsMaster labSampleDetailsMaster = new LabSampleDetailsMaster(); 
			  for (Iterator iterator =listTGrossMasterDto.iterator(); iterator.hasNext();) 
			  {
				  TGrossMasterDto tGrossMasterDto = (TGrossMasterDto) iterator.next();
				  labSampleDetailsMaster.setLabSampleDtlsId(tGrossMasterDto.getLabSampleDtlsId());
				 
				  TGrossMaster tGrossMaster = mapper.map(tGrossMasterDto, TGrossMaster.class,"TGrossMasterDtoToTGrossMaster");
				  if(tGrossMaster != null)
				  {
					  List<TBlockMaster> tBlockMasterList = tGrossMaster.getListTBlockMaster();
					    if (tBlockMasterList != null) {
				 for (Iterator iterator1 =tGrossMaster.getListTBlockMaster().iterator(); iterator1.hasNext();) 
				  {
					TBlockMaster tBlockMaster = (TBlockMaster) iterator1.next();
					tBlockMaster.setLabSampleDtlsId(tGrossMaster.getLabSampleDtlsId());
					tBlockMaster.settGrossId(tGrossMaster.gettGrossId());
					tBlockMaster.setCreatedDate(TimeZoneComponent.getDateByzone(tBlockMaster.getOrgUnitId()).getTime());
					
					/**Code for Setting Object for update Lab Sample Status*/
					labSampleDetailsMaster.setUpdatedBy(tBlockMaster.getCreatedBy());
					labSampleDetailsMaster.setUpdatedDate(tBlockMaster.getCreatedDate());
					labSampleDetailsMaster.setPerformedUnit(tBlockMaster.getOrgUnitId());
					labSampleDetailsMaster.setAssignUnit(tBlockMaster.getOrgUnitId());
					labSampleDetailsMaster.setOrgId(tBlockMaster.getOrgId());
					labSampleDetailsMaster.setOrgUnitId(tBlockMaster.getOrgUnitId());
					sessionFactory.getCurrentSession().save(tBlockMaster);
				  }
					    	}
				  }
			
			  }
			  return new Response(SUCCESS, SUCCESS_200, BLOCK_CREATE_SUCCESS, null, labSampleDetailsMaster);
		  }
		  else 
		  {
			  return new Response(ERROR, ERR_500, BLOCK_CREATE_FAIL, null, null);
		  }
			
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}
	
	public String getBlockCreationQuery(SearchDto searchDto)
	{
		StringBuffer query = new StringBuffer();
		 query.append(sessionFactory.getCurrentSession().getNamedQuery("GET_GROSS_SPCIMEN_LIST").getQueryString());
		 if (searchDto.getFromDt() != null ) 
			  query.append(" AND to_char(labSampleDetailsMst.updatedDate,'YYYY-MM-DD') >=:fromDate ");
		  if (searchDto.getToDt() != null ) 
	    	  query.append(" AND to_char(labSampleDetailsMst.updatedDate,'YYYY-MM-DD') <=:toDate ");
		  if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
	    	  query.append(" AND patientReg.patientId =:patientId ");

	      if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty())
	    	  query.append(" AND visitMst.visitTypeId IN (:visitTypes)");

	      if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty())
	    	  query.append(" AND testMst.deptId IN (:subDepts)");
	      if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty())
	    	  query.append(" AND testMst.testId in (:testTypes)");
	      if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0)
	    	  query.append(" AND labSampleMst.labSampleId =:labSampleId ");
	      if (searchDto.getSpecimanTypes() != null && !searchDto.getSpecimanTypes().isEmpty())
	    	  query.append(" AND testMst.specimanId in (:specimanTypes)");
	      query.append(" order by labSampleDetailsMst.updatedDate");
	      return query.toString();	
	}
	
	public String getBlockCreationQueryCount(SearchDto searchDto)
	{
		
		 StringBuffer query = new StringBuffer();
		 query.append(sessionFactory.getCurrentSession().getNamedQuery("GET_GROSS_SPCIMEN_LIST_COUNT").getQueryString());
		
		 if (searchDto.getFromDt() != null ) 
			  query.append(" AND to_char(labSampleDetailsMst.updatedDate,'YYYY-MM-DD') >=:fromDate ");
		  if (searchDto.getToDt() != null ) 
	    	  query.append(" AND to_char(labSampleDetailsMst.updatedDate,'YYYY-MM-DD') <=:toDate ");
	      
		  if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
	    	  query.append(" AND patientReg.patientId =:patientId ");

	      if (searchDto.getVisitTypes() != null && !searchDto.getVisitTypes().isEmpty())
	    	  query.append(" AND visitMst.visitTypeId IN (:visitTypes)");

	      if (searchDto.getSubDepts() != null && !searchDto.getSubDepts().isEmpty())
	    	  query.append(" AND testMst.deptId IN (:subDepts)");
	      
	      if (searchDto.getTestTypes() != null && !searchDto.getTestTypes().isEmpty())
	    	  query.append(" AND testMst.testId in (:testTypes)");
	      
	      if (searchDto.getLabSampleId() != null && searchDto.getLabSampleId() > 0)
	    	  query.append(" AND labSampleMst.labSampleId =:labSampleId ");
	      
	      if (searchDto.getSpecimanTypes() != null && !searchDto.getSpecimanTypes().isEmpty())
	    	  query.append(" AND testMst.specimanId in (:specimanTypes )");
	      
	      return query.toString();
	}

	@Override
	public Response getSubSpeciman(int tSpecimanId) {
	 try
		{
			List<TGrossMasterDto> listTGrossMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SUB_SPECIMAN_DETAIL")
					.setParameter("tSpecimanId", tSpecimanId)
					.setResultTransformer(Transformers.aliasToBean(TGrossMasterDto.class)).list();
			if(listTGrossMasterDto.isEmpty())
			{
				return new Response(ERROR, ERR_500, "No Sub Speciman Found..", null, null);
			}
			else{
				return new Response(SUCCESS, SUCCESS_200,null, listTGrossMasterDto, null);
			}

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

}

package com.param.lis.global.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.unit.TestCostHead;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.dto.TestCodeHeadDto;
import com.param.lis.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})	
public class TestCostHeadDaoImpl extends GenericDao<TestCostHead, Integer> implements ITestCostHeadDao,ICommonConstants,IError{

	
	public TestCostHeadDaoImpl() {
		super(TestCostHead.class);
	}
	@Autowired SessionFactory sessionFactory;
	@Override
	public Response addTestCodeHead(TestCodeHeadDto dto) throws ApplicationException {
		try{
			TestCostHead model=new TestCostHead();
				model.setTestCostHeadDesc(dto.getTestCostHeadDesc());
				model.setTestCostHeadCode(dto.getTestCostHeadCode());
				model.setStatus(dto.getStatus());
//				model.setUnitId(dto.getUnitId());
				model.setOrganizationId(dto.getOrganizationId());
				model.setCreatedBy(dto.getCreatedBy());
				model.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(),"dd-M-yyy HH:mm:ss"));
				model.setUpdatedBy(dto.getUpdatedBy());
				model.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(),"dd-M-yyy HH:mm:ss"));
				save(model);
				return new Response<>(SUCCESS, SUCCESS_CODE,SUCCESS_MESSAGE, null, null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response updateTestCodeHead(TestCodeHeadDto dto) throws ApplicationException {
		try{
				TestCostHead model= findById(dto.getTestCosHeadId());
				model.setTestCostHeadDesc(dto.getTestCostHeadDesc());
				model.setTestCostHeadCode(dto.getTestCostHeadCode());
//				model.setUnitId(dto.getUnistId());
				model.setOrganizationId(dto.getOrganizationId());
				model.setUpdatedBy(dto.getUpdatedBy());
				model.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(),"dd-M-yyy HH:mm:ss"));
				update(model);
				return new Response<>(SUCCESS, SUCCESS_CODE,COMMON_UPDATE, null, null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			throw e;
		}
	}


	@Override
	public Response getTestCodeHeadList(TestCodeHeadDto testCodeHeadDto) throws ApplicationException {
		try{
			
			List<TestCodeHeadDto> listTestCodeHeadDto=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEST_CODE_HEAD_LIST")
					.setParameter("orgId", testCodeHeadDto.getOrganizationId())
					//.setParameter("unitId", testCodeHeadDto.getUnitId())
					.setFirstResult(testCodeHeadDto.getOffset() != null ? testCodeHeadDto.getOffset() : 0)
					.setMaxResults(testCodeHeadDto.getNoOfRecordsPerPage() != null ? testCodeHeadDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(TestCodeHeadDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listTestCodeHeadDto, null);
		}catch(HibernateException he){
			throw he;
		}
		catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response updateTestCodeHeadStatus(TestCodeHeadDto dto) throws ApplicationException {
		try{
			TestCostHead model= findById(dto.getTestCosHeadId());
			model.setStatus(dto.getStatus());
			model.setUpdatedBy(dto.getUpdatedBy());
			model.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(),"dd-M-yyy HH:mm:ss"));
			update(model);
			return new Response<>(SUCCESS, SUCCESS_CODE,COMMON_UPDATE, null, null);
	}catch(HibernateException he){
		throw he;
	}catch(Exception e){
		throw e;
	}
	}

	@Override
	public Response countOfTestCodeHeadList(TestCodeHeadDto dto) throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(TestCostHead.class);
			c.add(Restrictions.eq("organizationId", dto.getOrganizationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response getTestCodeHeadListById(TestCodeHeadDto testCodeHeadDto) throws ApplicationException {
		try{
			
			List<TestCodeHeadDto> listTestCodeHeadDto=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEST_CODE_HEAD_LIST_BY_ID")
					.setParameter("orgId", testCodeHeadDto.getOrganizationId())
					.setParameter("id", testCodeHeadDto.getTestCosHeadId())
					.setResultTransformer(Transformers.aliasToBean(TestCodeHeadDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listTestCodeHeadDto, null);
		}catch(HibernateException he){
			throw he;
		}
		catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response getActiveTestCodeHeadList(TestCodeHeadDto dto) throws ApplicationException {
			try{
			
			List<TestCodeHeadDto> listTestCodeHeadDto=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_TEST_CODE_HEAD_LIST")
					.setParameter("orgId", dto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(TestCodeHeadDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listTestCodeHeadDto, null);
		}catch(HibernateException he){
			throw he;
		}
		catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response checkForUniqueCode(TestCodeHeadDto testCodeHeadDto) throws ApplicationException {
		try{
			List<TestCodeHeadDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEST_COST_HEAD_BY_CODE")
					.setParameter("testCostHeadCode", testCodeHeadDto.getTestCostHeadCode().trim().toLowerCase())
					.setParameter("organizationId", testCodeHeadDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(TestCodeHeadDto.class)).list();
			
				return new Response<>(SUCCESS, SUCCESS_CODE,SUCCESS_MESSAGE, list, null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response checkForUniqueDesc(TestCodeHeadDto testCodeHeadDto) throws ApplicationException {
		try{
			List<TestCodeHeadDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEST_COST_HEAD_BY_DESCRIPTION")
					.setParameter("testCostHeadDesc", testCodeHeadDto.getTestCostHeadDesc().trim().toLowerCase())
					.setParameter("organizationId", testCodeHeadDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(TestCodeHeadDto.class)).list();
			
				return new Response<>(SUCCESS, SUCCESS_CODE,SUCCESS_MESSAGE, list, null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response checkUpdateForUniqueCode(TestCodeHeadDto testCodeHeadDto) throws ApplicationException {
		try{
			List<TestCodeHeadDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_UPDATE_TEST_COST_HEAD_BY_CODE")
					.setParameter("testCostHeadCode", testCodeHeadDto.getTestCostHeadCode().trim().toLowerCase())
					.setParameter("organizationId", testCodeHeadDto.getOrganizationId())
					.setParameter("testCosHeadId", testCodeHeadDto.getTestCosHeadId())
					.setResultTransformer(Transformers.aliasToBean(TestCodeHeadDto.class)).list();
			
				return new Response<>(SUCCESS, SUCCESS_CODE,SUCCESS_MESSAGE, list, null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response checkUpdateForUniqueDesc(TestCodeHeadDto testCodeHeadDto) throws ApplicationException {
		try{
			List<TestCodeHeadDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_UPDATE_TEST_COST_HEAD_BY_DESCRIPTION")
					.setParameter("testCostHeadDesc", testCodeHeadDto.getTestCostHeadDesc().trim().toLowerCase())
					.setParameter("organizationId", testCodeHeadDto.getOrganizationId())
					.setParameter("testCosHeadId", testCodeHeadDto.getTestCosHeadId())
					.setResultTransformer(Transformers.aliasToBean(TestCodeHeadDto.class)).list();
			
				return new Response<>(SUCCESS, SUCCESS_CODE,SUCCESS_MESSAGE, list, null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			throw e;
		}
	}

}

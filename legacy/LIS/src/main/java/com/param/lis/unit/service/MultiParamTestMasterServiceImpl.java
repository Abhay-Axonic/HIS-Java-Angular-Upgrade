package com.param.lis.unit.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.unit.dao.IMultiParamTestMasterDao;
import com.param.lis.unit.dto.TestMasterDto;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class MultiParamTestMasterServiceImpl
		implements IMultiParamTestMasterService, ICommonConstants, IError, ITransactionConstants
{

	@Autowired
	IMultiParamTestMasterDao iMultiParamTestMasterDao;
	
	@Autowired
	IEmrService EmrService;   


	@Autowired
	private SessionFactory sessionFactory;

	final static Logger logger = Logger.getLogger(MultiParamTestMasterServiceImpl.class);

	@Override
	@Transactional
	public Response addMultiParameterTest(TestMasterDto testMasterDto) throws ApplicationException
	{
		try
		{
			List<TestMasterDto> testMstDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEST_BY_TEST_CODE")
					.setParameter("testCode", testMasterDto.getTestCode().trim().toLowerCase())
					.setParameter("orgId", testMasterDto.getOrgId())
					.setParameter("orgUnitId", testMasterDto.getOrgUnitId())
					.setParameter("testType", MULTY_PARAM_TEST)
					.setResultTransformer(Transformers.aliasToBean(TestMasterDto.class)).list();
			if (testMstDto != null && testMstDto.size() > 0)
			{
				return new Response(ERROR, ERR_500, TEST_CODE_EXISIS, null, null);
			} else
			{
				testMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(testMasterDto.getOrgUnitId()).getTime());
				 iMultiParamTestMasterDao.addMultiParameterTest(testMasterDto);
				
				return new Response(SUCCESS, SUCCESS_200, TEST_ADD_SUCC, null, null);
				
			}

		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getMultyParameterTest(Integer orgId, Integer orgUnitId, Integer testId) throws ApplicationException
	{
		try
		{
			return iMultiParamTestMasterDao.getMultyParameterTest(orgId, orgUnitId, testId);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateMultiParameterTest(TestMasterDto testMasterDto) throws ApplicationException
	{
		try
		{
			List<TestMasterDto> testMstDto =  sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEST_BY_TEST_CODE_UPDATE")
					.setParameter("testCode", testMasterDto.getTestCode().trim().toLowerCase())
					.setParameter("orgId", testMasterDto.getOrgId())
					.setParameter("orgUnitId", testMasterDto.getOrgUnitId())
					.setParameter("testType", MULTY_PARAM_TEST)
					.setParameter("testId", testMasterDto.getTestId())
					.setResultTransformer(Transformers.aliasToBean(TestMasterDto.class)).list();
			if (testMstDto != null  && testMstDto.size()>0)
			{
				return new Response(ERROR, ERR_500, TEST_CODE_EXISIS, null, null);
				
			} else
			{
				iMultiParamTestMasterDao.updateTestParemMapper(testMasterDto.getTestId());
				return iMultiParamTestMasterDao.updateMultiParameterTest(testMasterDto);
				
			}
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response activeInactiveMultiParamTest(Integer orgId, Integer orgUnitId, Integer testId,
			Character testStatus,Integer serviceId) throws ApplicationException
	{
		try
		{
			return iMultiParamTestMasterDao.activeInactiveMultiParamTest(orgId, orgUnitId, testId,
					testStatus,serviceId);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response listMultiParamTestMaster(Integer orgId,Character isHistoCyto, Integer orgUnitId, Integer offset, Integer recordPerPage)
			throws ApplicationException
	{
		try
		{
			return iMultiParamTestMasterDao.listMultiParamTestMaster(orgId, isHistoCyto, orgUnitId, offset, recordPerPage);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTotalMultiTestRecord(Integer orgId, Character isHistoCyto, Integer orgUnitId) throws ApplicationException
	{
		try
		{
			return iMultiParamTestMasterDao.getTotalMultiTestRecord(orgId,isHistoCyto ,orgUnitId);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getParametersByTest(Integer testId) throws ApplicationException {
		try
		{
			return iMultiParamTestMasterDao.getParametersByTest(testId);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response<TestMasterDto, Integer> multiParamTestMasterUniversalSearch(Integer orgId, Character isHistoCyto,
			Integer orgUnitId, Integer offset, Integer recordPerPage, String testDesc) throws ApplicationException {
		try
		{
			return iMultiParamTestMasterDao.multiParamTestMasterUniversalSearch(orgId, isHistoCyto, orgUnitId, offset, recordPerPage,testDesc);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

}

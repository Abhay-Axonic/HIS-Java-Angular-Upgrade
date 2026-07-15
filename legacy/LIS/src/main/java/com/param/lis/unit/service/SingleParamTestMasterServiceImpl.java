package com.param.lis.unit.service;

import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.param.entity.lis.unit.TestMaster;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.unit.dao.ISingleParamTestMasterDao;
import com.param.lis.unit.dto.TestMasterDto;
import com.param.lis.unit.dto.TestMasterForOnHoldDto;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class SingleParamTestMasterServiceImpl implements ISingleParamTestMasterService,ITransactionConstants, ICommonConstants, IError
{

	@Autowired
	ISingleParamTestMasterDao iTestMasterDao;
	
	@Autowired
	IEmrService EmrService;   


	@Autowired
	private SessionFactory sessionFactory;

	final static Logger logger = Logger.getLogger(SingleParamTestMasterServiceImpl.class);

	@Override
	@Transactional
	public Response addSingleParameterTest(TestMasterDto testMasterDto) throws ApplicationException
	{
		try
		{
			TestMasterDto testMstDto = (TestMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEST_BY_TEST_CODE")
					.setParameter("testCode", testMasterDto.getTestCode().trim().toLowerCase())
					.setParameter("testType", SINGLE_PARAM_TEST)
					.setParameter("orgId", testMasterDto.getOrgId()).setParameter("orgUnitId", testMasterDto.getOrgUnitId())
					.setResultTransformer(Transformers.aliasToBean(TestMasterDto.class)).uniqueResult();
			if (testMstDto == null)
			{
				testMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(testMasterDto.getOrgUnitId()).getTime());
				Response res=  iTestMasterDao.addSingleParameterTest(testMasterDto);
				
				if(res.getObject()!= null) {
					TestMaster dto = (TestMaster)res.getObject();
					testMasterDto.setTestId(dto.getTestId());
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(testMasterDto);
					request.setKey(MasterKeyConstants.TestMaster.toString());
					request.setPayload(payload);
					//EmrService.publish(request);
				}
				  return new Response(SUCCESS, SUCCESS_200, TEST_ADD_SUCC, null, null);
			} else
			{
				return new Response(ERROR, ERR_500, TEST_CODE_EXISIS, null, null);
			}

		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getSingleParameterTest(Integer orgId, Integer orgUnitId, Integer testId) throws ApplicationException
	{
		try
		{
			return iTestMasterDao.getSingleParameterTest(orgId, orgUnitId, testId);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateSingleParameterTest(TestMasterDto testMasterDto) throws ApplicationException
	{
		try
		{
			TestMasterDto testMstDto = (TestMasterDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEST_BY_TEST_CODE_UPDATE")
					.setParameter("testCode", testMasterDto.getTestCode().trim().toLowerCase())
					.setParameter("orgId", testMasterDto.getOrgId())
					.setParameter("orgUnitId", testMasterDto.getOrgUnitId())
					//.setInteger("testType", SINGLE_PARAM_TEST)
					.setParameter("testType", testMasterDto.getTestType())
					.setParameter("testId", testMasterDto.getTestId())
					.setResultTransformer(Transformers.aliasToBean(TestMasterDto.class)).uniqueResult();
			if (testMstDto == null)
			{
				return iTestMasterDao.updateSingleParameterTest(testMasterDto);
			} else
			{
				return new Response(ERROR, ERR_500, TEST_CODE_EXISIS, null, null);
			}
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response activeInactiveTest(Integer orgId, Integer orgUnitId, Integer testId, Integer parameterId,
			Character testStatus,Integer serviceId) throws ApplicationException
	{
		try
		{
			return iTestMasterDao.activeInactiveTest(orgId, orgUnitId, testId, parameterId, testStatus, serviceId);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response listTestMaster(Integer orgId, Character isHistoCyto, Integer orgUnitId, Integer offset, Integer recordPerPage,String searchKeyword)
			throws ApplicationException
	{
		try
		{
				return iTestMasterDao.listTestMaster(orgId,isHistoCyto,orgUnitId, offset, recordPerPage, searchKeyword);

		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTotalTestRecord(Integer orgId, Character isHistoCyto,Integer orgUnitId) throws ApplicationException
	{
		try
		{
			return iTestMasterDao.getTotalTestRecord(orgId,isHistoCyto, orgUnitId);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response checkServiceAvaiable(TestMasterDto testMasterDto,boolean addOrUpdate)
			throws ApplicationException {
		try
		{
			return iTestMasterDao.checkServiceAvaiable(testMasterDto,addOrUpdate);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTestDetails(Integer orgId, Integer unitId, Integer testId) throws ApplicationException {
		try
		{
			return iTestMasterDao.getTestDetails(orgId,unitId,testId);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getDeltaDetails(Integer orgId, Integer unitId, Integer testId) throws ApplicationException {
		try
		{
			return iTestMasterDao.getDeltaDetails(orgId,unitId,testId);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getEquipmentList(Integer testId, Integer orgId, Integer unitId)
			throws ApplicationException {
		try
		{
			return iTestMasterDao.getEquipmentList(testId,orgId,unitId);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getParameterTestOnReportEntry(@RequestBody Map<Integer,Integer> labAndTestIds)throws ApplicationException {
		try
		{
			return iTestMasterDao.getParameterTestOnReportEntry(labAndTestIds);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response<TestMasterDto, Integer> cytologyTestMasterUniversalSearch(Integer orgId, Integer orgUnitId,
			Integer offset, Integer recordPerPage, String testDesc,Character isHistoCyto) throws ApplicationException {
		try
		{
			return iTestMasterDao.cytologyTestMasterUniversalSearch(orgId,orgUnitId,offset,recordPerPage,testDesc,isHistoCyto);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response testMasterUniversalSearch(Integer orgId, Character isHistoCyto,
			Integer orgUnitId, Integer offset, Integer recordPerPage, String testDesc) throws ApplicationException {
		try
		{
			return iTestMasterDao.testMasterUniversalSearch(orgId,isHistoCyto,orgUnitId,offset,recordPerPage,testDesc);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTotalSerologyTestRecord(Integer orgId, Character isHistoCyto,
			Integer orgUnitId,  String searchKeyword) throws ApplicationException {
		try
		{
			return iTestMasterDao.getTotalSerologyTestRecord(orgId,isHistoCyto, orgUnitId, searchKeyword);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getOnHoldReportEntryDetail(TestMasterForOnHoldDto dto) throws ApplicationException {
		try
		{
			return iTestMasterDao.getOnHoldReportEntryDetail(dto);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

}

package com.param.lis.unit.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.unit.EquipmentIdTestMaster;
import com.param.entity.lis.unit.ParameterMaster;
import com.param.entity.lis.unit.TestMaster;
import com.param.entity.lis.unit.TestParamMppr;
import com.param.global.dto.CommonDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.ParameterMasterDto;
import com.param.lis.unit.dto.TestMasterDto;
import com.param.lis.unit.dto.TestParamMpprDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class MultiParamTestMasterDaoImpl extends GenericDao<TestMaster, Integer>
implements IMultiParamTestMasterDao, ICommonConstants, IError, ITransactionConstants
{

	public MultiParamTestMasterDaoImpl() {
		super(TestMaster.class);
	}
	
	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private SessionFactory sessionFactory;

	
	 @Autowired
	 private IParameterMasterDao iParameterMasterDao;

	
	
	@Autowired
	private ITestParamMpprDao iTestParamMpprDao;
	
	final static Logger logger = Logger.getLogger(MultiParamTestMasterDaoImpl.class);

	@Override
	public Response addMultiParameterTest(TestMasterDto testMasterDto) throws ApplicationException
	{
		try
		{
			TestMaster testMaster = mapper.map(testMasterDto, TestMaster.class, "TestMasterDtoTOTestMaster");
			testMaster.setCreatedBy(testMasterDto.getCreatedBy());
			testMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(testMasterDto.getOrgUnitId()).getTime());
			TestMaster testMst = save(testMaster);
			  if(testMasterDto.getEquipmentList()!=null && testMasterDto.getEquipmentList().size()>0)
			     {
			    	 for(CommonDto visitTypeObj:testMasterDto.getEquipmentList()){
			       	  EquipmentIdTestMaster equipmentIdTestMaster = new EquipmentIdTestMaster();
			       	  equipmentIdTestMaster.setEquipmentId(visitTypeObj.getId());
			       	  equipmentIdTestMaster.setTestId(testMst.getTestId());
			       	  equipmentIdTestMaster.setUnitId(testMaster.getOrgUnitId());
			       	  equipmentIdTestMaster.setOrgId(testMasterDto.getOrgId());
			       	  equipmentIdTestMaster.setCreatedBy(testMasterDto.getCreatedBy());
			       	  equipmentIdTestMaster.setStatus('A');
			       	  equipmentIdTestMaster.setCreatedDate(TimeZoneComponent.getDateByzone(testMaster.getOrgUnitId()));
			       	  equipmentIdTestMaster.setUpdatedBy(testMasterDto.getCreatedBy());
			       	  equipmentIdTestMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(testMaster.getOrgUnitId()));
			    		     sessionFactory.getCurrentSession().save(equipmentIdTestMaster);
			    	     }
			     }
			if (testMst != null)
			{
				int i = 0;
				
				for (Iterator iterator = testMst.getListTestParamMppr().iterator(); iterator.hasNext();)
				{
					    TestParamMppr testParamMppr= (TestParamMppr) iterator.next();
					    testParamMppr.setTestId(testMst.getTestId());
					    testParamMppr.setCreatedBy(testMaster.getCreatedBy());
					    testParamMppr.setCreatedDate(TimeZoneComponent.getDateByzone(testMaster.getOrgUnitId()).getTime());
					    testParamMppr.setOrgId(testMaster.getOrgId());
					    testParamMppr.setOrgUnitId(testMaster.getOrgUnitId());
					    testParamMppr.setHeaderSequence(i);
					    sessionFactory.getCurrentSession().save(testParamMppr);
					    i++;
					    
				}
				return new Response(SUCCESS, SUCCESS_200, TEST_ADD_SUCC, null, testMst);
			}
			else
			{
				return new Response(SUCCESS, SUCCESS_200, TEST_ADD_FAIL, null, null);
			}
		} catch (HibernateException er)
		{
			logger.error("Exception", er);throw er;
		} catch (Exception e)
		{
			logger.error("Exception", e);throw e;
		}
	}

	@Override
	public Response getMultyParameterTest(Integer orgId, Integer orgUnitId, Integer testId) throws ApplicationException
	{
		try
		{
			List<ParameterMasterDto> listParameterMasterDto = new ArrayList();
			List<TestParamMpprDto> listTestParamMpprDto = new ArrayList();
			TestMaster testMaster = (TestMaster) sessionFactory.getCurrentSession().getNamedQuery("GET_TEST_BY_TEST_ID")
					.setParameter("orgId", orgId)
					//.setParameter("orgUnitId", orgUnitId)
					.setParameter("testId", testId)
					.setParameter("testType", MULTY_PARAM_TEST)
					.uniqueResult();
			TestMasterDto testMasterDto = testMaster!=null?mapper.map(testMaster, TestMasterDto.class, "TestMasterDtoTOTestMaster"):null;
			if (testMasterDto != null)
			{
				for (Iterator iterator = testMaster.getListTestParamMppr().iterator(); iterator.hasNext();)
				{
					TestParamMppr testParamMppr = (TestParamMppr) iterator.next();
					if(testParamMppr.getIsDeleted()=='N')
					{
					  Response<ParameterMasterDto, Integer> response =  iParameterMasterDao.getParameterById(testParamMppr.getOrgId(), testParamMppr.getOrgUnitId(), testParamMppr.getParameterId());
	                  ParameterMasterDto parameterMasterDto = (ParameterMasterDto) response.getObject();
	                  TestParamMpprDto testParamMpprDto = testParamMppr != null
	                                    ? mapper.map(testParamMppr, TestParamMpprDto.class, "TestParamMpprDtoToTestParamMppr")
	                                    : null;     
	                    testParamMpprDto.setParameterDesc(parameterMasterDto.getParameterName());
	                    testParamMpprDto.setHeaderDesc(testParamMppr.getHeaderMaster().getDesc());
	                    parameterMasterDto.setUnitName(parameterMasterDto.getUnitName());
	                    listParameterMasterDto.add(parameterMasterDto);
	                    listTestParamMpprDto.add(testParamMpprDto);     
	                    listParameterMasterDto.add(parameterMasterDto);
					}
				
				}
				testMasterDto.setListParameterMasterDto(listParameterMasterDto);
				testMasterDto.setListTestParamMppr(listTestParamMpprDto);
			}
			List<CommonDto> equipmentList = sessionFactory.getCurrentSession()
					.createSQLQuery("select  em.equipment_id as \"id\","
							+"	i.name as \"name\" " 
							+"  from lab.m_equipment_id_test_master em "  
							+"	join  public.m_item i on  em.equipment_id=i.id "  
							+"  where " 
							+"	em.test_id =:testId " 
							+"	and em.unit_id =:unitId and em.org_id=:orgId and status='A'")
					.setParameter("testId", testId)
					.setParameter("unitId", orgUnitId)
					.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, equipmentList, testMasterDto);

		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	public Response updateMultiParameterTest(TestMasterDto testMasterDto) throws ApplicationException
	{
		try
		{
			TestMaster testMaster = mapper.map(testMasterDto, TestMaster.class, "TestMasterDtoTOTestMaster");
			testMaster.setUpdatedBy(testMasterDto.getCreatedBy());
			testMaster.setUpdatedDate(new  Date().getTime());
			TestMaster testMst = update(testMaster);
			 Integer count=sessionFactory.getCurrentSession().createQuery(" update EquipmentIdTestMaster tvm set status='I' where tvm.testId=:testId "	)
			   			.setParameter("testId", testMasterDto.getTestId()).executeUpdate();
			     
			      
			      for(CommonDto visitTypeObj:testMasterDto.getEquipmentList()){
			    	  EquipmentIdTestMaster equipmentIdTestMaster = new EquipmentIdTestMaster();
			    	  equipmentIdTestMaster.setEquipmentId(visitTypeObj.getId());
			    	  equipmentIdTestMaster.setTestId(testMst.getTestId());
			    	  equipmentIdTestMaster.setUnitId(testMaster.getOrgUnitId());
			    	  equipmentIdTestMaster.setOrgId(testMasterDto.getOrgId());
			    	  equipmentIdTestMaster.setCreatedBy(testMasterDto.getCreatedBy());
			    	  equipmentIdTestMaster.setStatus('A');
			    	  equipmentIdTestMaster.setCreatedDate(TimeZoneComponent.getDateByzone(testMaster.getOrgUnitId()));
			    	  equipmentIdTestMaster.setUpdatedBy(testMasterDto.getCreatedBy());
			    	  equipmentIdTestMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(testMaster.getOrgUnitId()));
			 		     sessionFactory.getCurrentSession().save(equipmentIdTestMaster);
			 	     }
			if (testMst != null)
			{
				int i = 0;
				for (Iterator iterator = testMasterDto.getListTestParamMpprList().iterator(); iterator.hasNext();)
				{
					TestParamMpprDto dto= (TestParamMpprDto) iterator.next();
					
					if(dto.getIsDeleted() == 'N') {
						
						TestParamMppr testParamMpr = new TestParamMppr();
						testParamMpr.setCreatedBy(dto.getCreatedBy());
						testParamMpr.setHeaderId(dto.getHeaderId());
						testParamMpr.setParameterId(dto.getParameterId());
						testParamMpr.setParaSequence(dto.getParaSequence());
						testParamMpr.setTestParaStatus('A');
						testParamMpr.setTestId(dto.getTestId());
						testParamMpr.setUpdatedBy(dto.getCreatedBy());
						testParamMpr.setUpdatedDate(TimeZoneComponent.getDateByzone(testMaster.getOrgUnitId()).getTime());
						testParamMpr.setIsDeleted('N');
						testParamMpr.setCreatedDate(TimeZoneComponent.getDateByzone(testMaster.getOrgUnitId()).getTime());
						testParamMpr.setOrgId(testMaster.getOrgId());
						testParamMpr.setOrgUnitId(testMaster.getOrgUnitId());
						testParamMpr.setHeaderSequence(i);
						sessionFactory.getCurrentSession().save(testParamMpr);
					
					i++;
					}
					
				}
				return new Response(SUCCESS, SUCCESS_200, TEST_UPDATE_SUCC, null, null);
			} else
			{
				return new Response(SUCCESS, SUCCESS_200, TEST_UPDATE_FAIL, null, null);
			}
		} catch (HibernateException er)
		{
			logger.error("HibernateException", er);throw er;
		} catch (Exception e)
		{
			logger.error("Exception", e);throw e;
		}
	}

	@Override
	public Response activeInactiveMultiParamTest(Integer orgId, Integer orgUnitId, Integer testId,
			Character testStatus,Integer serviceId) throws ApplicationException
	{
		
		try
		{   
			BigInteger testMasterSameCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CONT_OF_SAME_TEST_MASTER_WITH_DIFF_STATUS").setParameter("orgUnitId", orgUnitId)
					.setParameter("serviceId", serviceId).setParameter("testId", testId).uniqueResult();

			if (testMasterSameCount.compareTo(BigInteger.ZERO) == 1 && testStatus == 'A') {
				return new Response(ERROR, ERR_400, "SAME SERVICE ALREADY EXIST WITH ACTIVE STATUS", null,0);
			}
			
			TestMaster testMaster = findById(testId);
			testMaster.setTestStatus(testStatus);
			TestMaster testMst = update(testMaster);
			Integer helpValueStatus = 0;
			if (testMst != null)
			{
				for (Iterator iterator = testMst.getListTestParamMppr().iterator(); iterator.hasNext();)
				{
					TestParamMppr testParamMppr = (TestParamMppr) iterator.next();
					testParamMppr.setTestParaStatus(testStatus);
					sessionFactory.getCurrentSession().update(testParamMppr);
					 ParameterMaster parameterMaster = (ParameterMaster) sessionFactory.getCurrentSession()
			              .get(ParameterMaster.class, testParamMppr.getParameterId());
			          Integer result = 0;
			          Integer paraResult = sessionFactory.getCurrentSession()
			              .getNamedQuery("UPDATE_PARAMETER_STATUS").setParameter("status", testStatus)
			              .setParameter("parameterId", parameterMaster.getParameterId()).executeUpdate();
			          if (paraResult > 0) {
			            if (parameterMaster.getRefrangeTypeId() == REFERENCE_VALUE) {
			              result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_REF_RANGE_MASTER")
			                  .setParameter("status", testStatus)
			                  .setParameter("parameterId", parameterMaster.getParameterId()).executeUpdate();
			            } else if (parameterMaster.getRefrangeTypeId() == TEXTUAL_RANGE) {
			              result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_TEXTUAL_STATUS")
			                  .setParameter("status", testStatus)
			                  .setParameter("parameterId", parameterMaster.getParameterId()).executeUpdate();
			            } else if (parameterMaster.getRefrangeTypeId() == MULTIVALUED_RANGE) {
			              result = sessionFactory.getCurrentSession()
			                  .getNamedQuery("UPDATE_MULTIVALUED_TEXTUAL_STATUS")
			                  .setParameter("status", testStatus)
			                  .setParameter("parameterId", parameterMaster.getParameterId()).executeUpdate();
			            }
			             helpValueStatus = sessionFactory.getCurrentSession()
			                .getNamedQuery("UPDATE_HELP_MASTER_STATUS").setParameter("status", testStatus)
			                .setParameter("parameterId", parameterMaster.getParameterId()).executeUpdate();
			          }
					if(result>0||helpValueStatus>0)
					{
					  if (testMst.getTestStatus() == ACTIVE)
		                {
		                    return new Response(SUCCESS, SUCCESS_200, TEST_ACTIVATE_SUCC, null, null);
		                } else if (testMst.getTestStatus() == INACTIVE)
		                {
		                    return new Response(SUCCESS, SUCCESS_200, TEST_ACTIVATE_FAIL, null, null);
		                }
					}
					else 
					{
					  if (testMst.getTestStatus() == INACTIVE)
                      {
                          return new Response(SUCCESS, SUCCESS_200, TEST_INACTIVATE_SUCC, null, null);
                          
                      } else if (testMst.getTestStatus() == INACTIVE)
                      {
                          return new Response(SUCCESS, SUCCESS_200, TEST_INACTIVATE_FAIL, null, null);
                      }
					}
				}
			
			}

		} catch (HibernateException er)
		{
			logger.error("Exection", er);throw er;
		} catch (Exception e)
		{
			logger.error("Exection", e);throw e;
		}
		return new Response(ERROR, ERR_500, TEST_UPDATE_FAIL, null, null);
	}

	@Override
	public Response listMultiParamTestMaster(Integer orgId, Character isHistoCyto,Integer orgUnitId, Integer offset, Integer recordPerPage)
			throws ApplicationException
	{
		List<TestMasterDto> testMasterDtoList = new ArrayList();
		try
		{
			List<TestMaster> testMasterList = (List<TestMaster>) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEST_MASTER_LIST")
					.setParameter("orgId", orgId)
					.setParameter("isHistoCyto", isHistoCyto)
					.setParameter("orgUnitId", orgUnitId)
					.setParameter("testType", MULTY_PARAM_TEST)
					.setFirstResult(offset != null ? offset : 0)
					.setMaxResults(recordPerPage != null ? recordPerPage : 10).list();
			for (Iterator iterator = testMasterList.iterator(); iterator.hasNext();)
			{
				TestMaster testMaster = (TestMaster) iterator.next();
				TestMasterDto testMasterDto = mapper.map(testMaster, TestMasterDto.class, "TestMasterDtoTOTestMaster");
				testMasterDto.setSampleName(testMaster.getSampleMaster().getDesc());
				testMasterDto.setTechniqueName(testMaster.getTechniqueMaster().getDesc());
				testMasterDto.setPrerequisitName(testMaster.getPrerequisitesMaster().getDesc());
				testMasterDto.setNormlTime(testMaster.getNorTurnAroundTimeMaster().getTatTimeName()==null?"":
					                     testMaster.getNorTurnAroundTimeMaster().getTatTimeName());
			    testMasterDto.setUrgentTime(testMaster.getUrgTurnAroundTimeMaster().getTatTimeName()==null?"":
			    							testMaster.getUrgTurnAroundTimeMaster().getTatTimeName());
				testMasterDto.setSampleName(testMaster.getSampleMaster().getDesc());
				testMasterDto.setDeptName(testMaster.getSubSpecialityMaster().getSubSpecialityMasterName());
				testMasterDtoList.add(testMasterDto);
			}

			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, testMasterDtoList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response getTotalMultiTestRecord(Integer orgId,Character isHistoCyto, Integer orgUnitId) throws ApplicationException
	{
		try
		{
			BigInteger testMasterTotalRecordCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TOTAL_TEST_RECORD")
					.setParameter("orgId", orgId)
					.setParameter("isHistoCyto", isHistoCyto)
					.setParameter("orgUnitId", orgUnitId)
					.setParameter("testType", MULTY_PARAM_TEST)
					.uniqueResult();
			if (testMasterTotalRecordCount.compareTo(BigInteger.ZERO) == 1)
			{
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, testMasterTotalRecordCount);
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
	public Response getParametersByTest(Integer testId) throws ApplicationException {
		try
		{
			List<TestParamMpprDto> list =  sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PARAMETERS_BY_TEST_ID")
					.setParameter("testId", testId)
					.setResultTransformer(Transformers.aliasToBean(TestParamMpprDto.class)).list();
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, list, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response<TestMasterDto, Integer> multiParamTestMasterUniversalSearch(Integer orgId, Character isHistoCyto,
			Integer orgUnitId, Integer offset, Integer recordPerPage, String testDesc) throws ApplicationException {
		List<TestMasterDto> testMasterDtoList = new ArrayList();
		try
		{
			List<TestMaster> testMasterList = (List<TestMaster>) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_MULTI_PARAM_TEST_MASTER_DETAILS_LIST_SEARCH")
					.setParameter("orgId", orgId)
					.setParameter("isHistoCyto", isHistoCyto)
					.setParameter("orgUnitId", orgUnitId)
					.setParameter("testType", MULTY_PARAM_TEST)
					.setParameter("testDesc", "%"+testDesc.trim().toLowerCase()+"%")
					.setFirstResult(offset != null ? offset : 0)
					.setMaxResults(recordPerPage != null ? recordPerPage : 10).list();
			for (Iterator iterator = testMasterList.iterator(); iterator.hasNext();)
			{
				TestMaster testMaster = (TestMaster) iterator.next();
				TestMasterDto testMasterDto = mapper.map(testMaster, TestMasterDto.class, "TestMasterDtoTOTestMaster");
				testMasterDto.setSampleName(testMaster.getSampleMaster().getDesc());
				testMasterDto.setTechniqueName(testMaster.getTechniqueMaster().getDesc());
				testMasterDto.setPrerequisitName(testMaster.getPrerequisitesMaster().getDesc());
				testMasterDto.setNormlTime(testMaster.getNorTurnAroundTimeMaster().getTatTimeName()==null?"":
					                     testMaster.getNorTurnAroundTimeMaster().getTatTimeName());
			    testMasterDto.setUrgentTime(testMaster.getUrgTurnAroundTimeMaster().getTatTimeName()==null?"":
			    							testMaster.getUrgTurnAroundTimeMaster().getTatTimeName());
				testMasterDto.setSampleName(testMaster.getSampleMaster().getDesc());
				testMasterDto.setDeptName(testMaster.getSubSpecialityMaster().getSubSpecialityMasterName());
				testMasterDtoList.add(testMasterDto);
			}

			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, testMasterDtoList, null);

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	public Response updateTestParemMapper(Integer testId) throws ApplicationException {
		try{	
			
			Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update lab.t_test_parameter_mppr  set test_para_status='I' ,is_deleted = 'Y' "
					+ " where test_id =:testId ")
					.setParameter("testId", testId)
					.executeUpdate();
			if(count>0) {
				 return new Response(SUCCESS, null, null, null, count);
			}else {
				 return new Response(SUCCESS, null, null, null, 0);
			}
			  
			
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

}

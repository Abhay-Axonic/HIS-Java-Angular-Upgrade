package com.param.lis.unit.dao;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.*;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.unit.EquipmentIdTestMaster;
import com.param.entity.lis.unit.ParameterMaster;
import com.param.entity.lis.unit.TestMaster;
import com.param.entity.lis.unit.TestParamMppr;
import com.param.global.common.ServiceException;
import com.param.global.dto.CommonDto;
import com.param.global.dto.UnitServiceMasterDetailsDto;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.ParameterMasterDto;
import com.param.lis.unit.dto.TestDetailsDto;
import com.param.lis.unit.dto.TestMasterDto;
import com.param.lis.unit.dto.TestMasterForOnHoldDto;
import com.param.lis.unit.dto.TestMasterViewDto;
import com.param.lis.unit.dto.TestParamMpprDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class SingleParamTestMasterDaoImpl extends GenericDao<TestMaster, Integer>
    implements ISingleParamTestMasterDao, ICommonConstants, IError, ITransactionConstants {

  public SingleParamTestMasterDaoImpl() {
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

  final static Logger logger = Logger.getLogger(SingleParamTestMasterDaoImpl.class);

  @Override
  public Response addSingleParameterTest(TestMasterDto testMasterDto) throws ApplicationException 
  {
    try {
      TestMaster testMaster =
          mapper.map(testMasterDto, TestMaster.class, "TestMasterDtoTOTestMaster");
      testMaster.setCreatedDate(TimeZoneComponent.getDateByzone(testMaster.getOrgUnitId()).getTime());
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
      if (testMst != null) {
    	  
				if (testMasterDto.getTestType() == 0) {
					
					ParameterMasterDto parameterMasterDto = testMasterDto.getListParameterMasterDto().getFirst();
					parameterMasterDto.setTestType(SINGLE_PARAM_TEST);
					parameterMasterDto.setIsMultyparameter('N');
					parameterMasterDto.setStatus('A');
					parameterMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(testMaster.getOrgUnitId()).getTime());
					Response<ParameterMaster, Integer> paramRes = iParameterMasterDao.addParameter(parameterMasterDto);
					if (paramRes.getObject() != null) {
						TestParamMppr testParamMppr = new TestParamMppr();
						testParamMppr.setTestId(testMst.getTestId());
						testParamMppr.setParameterId(paramRes.getObject().getParameterId());
						testParamMppr.setParaSequence(0);
						testParamMppr.setHeaderId(null);
						testParamMppr.setCreatedBy(testMaster.getCreatedBy());
						testParamMppr.setCreatedDate(TimeZoneComponent.getDateByzone(testMaster.getOrgUnitId()).getTime());
						testParamMppr.setOrgId(testMaster.getOrgId());
						testParamMppr.setOrgUnitId(testMaster.getOrgUnitId());
						testParamMppr.setIsDeleted('N');
						testParamMppr.setTestParaStatus('A');
						testParamMppr.setHeaderSequence(0);
						sessionFactory.getCurrentSession().save(testParamMppr);
						return new Response(SUCCESS, SUCCESS_200, TEST_ADD_SUCC, null, testMst);
					} else {
						return new Response(SUCCESS, SUCCESS_200, TEST_ADD_FAIL, null, null);
					}
				} else {
					// multiparam
					if(testMst.getListTestParamMppr()!=null && testMst.getListTestParamMppr().size()>0)
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
		                    testParamMppr.setIsDeleted('N');
		                    testParamMppr.setTestParaStatus('A');
		                    testParamMppr.setHeaderSequence(i);
		                    sessionFactory.getCurrentSession().save(testParamMppr);
		                   i++;
		           	     }
		           	 return new Response(SUCCESS, SUCCESS_200, TEST_ADD_SUCC, null, testMst);
		            }else {
						return new Response(SUCCESS, SUCCESS_200, TEST_ADD_FAIL, null, null);
					}
				}
    	}else {
        return new Response(SUCCESS, SUCCESS_200, TEST_ADD_FAIL, null, null);
      }
    } catch (HibernateException exception) {
      logger.error("Exection", exception);
    } catch (Exception e) {
      logger.error("Exection", e);
    }
    return new Response(ERROR, ERR_500, TEST_ADD_FAIL, null, null);
  }

  @Override
  public Response getSingleParameterTest(Integer orgId, Integer orgUnitId, Integer testId)
      throws ApplicationException {
    try {
    	
    	TestMaster mstr = findById(testId);
    	char isHistoCyto = mstr.getIsHistoCyto();
      List<ParameterMasterDto> listParameterMasterDto = new ArrayList();
      List<TestParamMpprDto> listTestParamMpprDto = new ArrayList();
     /* TestMaster testMaster = (TestMaster) sessionFactory.getCurrentSession()
          .getNamedQuery("GET_TEST_BY_TEST_ID").setInteger("orgId", orgId)
          .setInteger("orgUnitId", orgUnitId).setInteger("testId", testId)
          .setInteger("testType", SINGLE_PARAM_TEST).uniqueResult();*/
      
      Session session = sessionFactory.getCurrentSession();
		StringBuilder query=new StringBuilder(session.getNamedQuery("GET_TEST_BY_TEST_ID_ON_SINGLE_PARAMETER").getQueryString());
		
		StringBuffer subQuery = new StringBuffer();
		if(isHistoCyto !='S'){
			subQuery.append("  AND tm.testType = 0 ");
		}
		
		query = query.append(subQuery.toString());
		Query actualQuery = session.createQuery(query.toString());
			TestMaster testMaster = (TestMaster) actualQuery
				.setParameter("orgId", orgId)
				.setParameter("orgUnitId", orgUnitId)
				.setParameter("testId", testId)
				.uniqueResult();
      
      TestMasterDto testMasterDto = testMaster != null? mapper.map(testMaster, TestMasterDto.class, "TestMasterDtoTOTestMaster"): null;
      if (testMasterDto != null) {
        for (Iterator iterator = testMaster.getListTestParamMppr().iterator(); iterator.hasNext();) {
            /*for (Iterator iterator = testMasterDto.getListTestParamMppr().iterator(); iterator.hasNext();) {*/
         /* TestParamMpprDto testParamMpprDto = (TestParamMpprDto) iterator.next();
          Response<ParameterMasterDto, Integer> paramRes = iParameterMasterDao.getParameterById(orgId, orgUnitId, testParamMpprDto.getParameterId());
          ParameterMasterDto parameterMasterDto = paramRes.getObject();
          listParameterMasterDto.add(parameterMasterDto);*/
          
          
        	TestParamMppr testParamMppr = (TestParamMppr) iterator.next();
			if(testParamMppr.getIsDeleted()=='N')
			{
				Response<ParameterMasterDto, Integer> response =  iParameterMasterDao.getParameterById(testParamMppr.getOrgId(), testParamMppr.getOrgUnitId(), testParamMppr.getParameterId());
				ParameterMasterDto parameterMasterDto = (ParameterMasterDto) response.getObject();
				 TestParamMpprDto testParamMpprDto = testParamMppr != null
                         ? mapper.map(testParamMppr, TestParamMpprDto.class, "TestParamMpprDtoToTestParamMppr")
                         : null;        
                         
                         if(testParamMppr.getHeaderMaster() != null) {
                        	 
                        	 testParamMpprDto.setParameterDesc(parameterMasterDto.getParameterName() == null ? null :parameterMasterDto.getParameterName());
                             testParamMpprDto.setHeaderDesc(testParamMppr.getHeaderMaster().getDesc());
                             parameterMasterDto.setUnitName(parameterMasterDto.getUnitName() == null ? null :parameterMasterDto.getUnitName());
                             listTestParamMpprDto.add(testParamMpprDto);   
                         }
             
              listParameterMasterDto.add(parameterMasterDto);
              listParameterMasterDto.add(parameterMasterDto);
			}
          
        }
		testMasterDto.setListTestParamMppr(listTestParamMpprDto);
        testMasterDto.setListParameterMasterDto(listParameterMasterDto);
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

    } catch (Exception e) {
      logger.error("Exection", e);
      return new Response(ERROR, ERR_500, TEST_NOT_FOUND, null, null);
    }
  }

  @Override
  public Response updateSingleParameterTest(TestMasterDto testMasterDto)
      throws ApplicationException {
    try {
      TestMaster testMaster = mapper.map(testMasterDto, TestMaster.class, "TestMasterDtoTOTestMaster");
    //  if (testMasterDto.getTestType() == 1) {
      Integer count1=sessionFactory.getCurrentSession().createQuery(" update TestParamMppr mppr set mppr.isDeleted='Y' where mppr.testId=:testId ")
			.setParameter("testId", testMasterDto.getTestId()).executeUpdate();
    //  }
      
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
      if (testMst != null) {
    	  if (testMasterDto.getTestType() == 0) {
        for (Iterator iterator = testMasterDto.getListParameterMasterDto().iterator(); iterator
            .hasNext();) {
          ParameterMasterDto parameterMasterDto = (ParameterMasterDto) iterator.next();
          parameterMasterDto.setTestType(SINGLE_PARAM_TEST);
          parameterMasterDto.setIsMultyparameter('N');
          parameterMasterDto.setUpdatedBy(testMst.getCreatedBy());
          parameterMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(testMaster.getOrgUnitId()).getTime());
          Response<ParameterMaster, Integer> paramRes =
              iParameterMasterDao.updateParameter(parameterMasterDto);
          if (paramRes.getObject() != null) {
        	  if(testMasterDto.getIsHistoCyto()=='S') {
        	  TestParamMppr testParamMppr = new TestParamMppr();
				testParamMppr.setTestId(testMst.getTestId());
				testParamMppr.setParameterId(paramRes.getObject().getParameterId());
				testParamMppr.setParaSequence(0);
				testParamMppr.setHeaderId(null);
				testParamMppr.setCreatedBy(testMaster.getCreatedBy());
				testParamMppr.setCreatedDate(TimeZoneComponent.getDateByzone(testMaster.getOrgUnitId()).getTime());
				testParamMppr.setOrgId(testMaster.getOrgId());
				testParamMppr.setOrgUnitId(testMaster.getOrgUnitId());
				testParamMppr.setIsDeleted('N');
				testParamMppr.setTestParaStatus('A');
				sessionFactory.getCurrentSession().save(testParamMppr);
				}else {
					
					 if(testMst.getListTestParamMppr().size() == 0) {
						 
						 TestParamMppr testParamMppr = new TestParamMppr();
							testParamMppr.setTestId(testMst.getTestId());
							testParamMppr.setParameterId(paramRes.getObject().getParameterId());
							testParamMppr.setParaSequence(0);
							testParamMppr.setHeaderId(null);
							testParamMppr.setCreatedBy(testMaster.getCreatedBy());
							testParamMppr.setCreatedDate(TimeZoneComponent.getDateByzone(testMaster.getOrgUnitId()).getTime());
							testParamMppr.setOrgId(testMaster.getOrgId());
							testParamMppr.setOrgUnitId(testMaster.getOrgUnitId());
							testParamMppr.setHeaderSequence(0);
							testParamMppr.setIsDeleted('N');
							testParamMppr.setTestParaStatus('A');
							sessionFactory.getCurrentSession().save(testParamMppr);
					 }else {
						 int i = 0;
						 for (Iterator iterator2 = testMst.getListTestParamMppr().iterator(); iterator2.hasNext();) {
				              TestParamMppr testParamMppr = (TestParamMppr) iterator2.next();
				              testParamMppr.setTestParaStatus('A');
				              testParamMppr.setIsDeleted('N');
				              testParamMppr.setUpdatedBy(testMaster.getUpdatedBy());
				              testParamMppr.setUpdatedDate(TimeZoneComponent.getDateByzone(testMaster.getOrgUnitId()).getTime());
				              testParamMppr.setOrgId(testMaster.getOrgId());
				              testParamMppr.setOrgUnitId(testMaster.getOrgUnitId());
				              testParamMppr.setHeaderSequence(i);
				              sessionFactory.getCurrentSession().update(testParamMppr);
				              i++;
				        }
					 }
					 
					
				}
        	  
            return new Response(SUCCESS, SUCCESS_200, TEST_UPDATE_SUCC, null, null);
          } else {
            return new Response(SUCCESS, SUCCESS_200, TEST_UPDATE_SUCC, null, null);
          }
        }
    	  }else {
    		// multiparam
    		  
    		
				if(testMst.getListTestParamMppr()!=null && testMst.getListTestParamMppr().size()>0)
	            {
					int i = 0;
					for (Iterator iterator = testMst.getListTestParamMppr().iterator(); iterator.hasNext();)
					{
						TestParamMppr testParamMpr= (TestParamMppr) iterator.next();
						if(testParamMpr.getTestPerMpprId()!=0)
						{
							testParamMpr.setCreatedBy(testMaster.getCreatedBy());
							testParamMpr.setCreatedDate(TimeZoneComponent.getDateByzone(testMaster.getOrgUnitId()).getTime());
							testParamMpr.setUpdatedBy(testMaster.getCreatedBy());
							testParamMpr.setUpdatedDate(TimeZoneComponent.getDateByzone(testMaster.getOrgUnitId()).getTime());
							testParamMpr.setOrgId(testMaster.getOrgId());
							testParamMpr.setOrgUnitId(testMaster.getOrgUnitId());
							testParamMpr.setHeaderSequence(i);
							sessionFactory.getCurrentSession().update(testParamMpr);
						}
						else if(testParamMpr.getTestPerMpprId()==0)
						{
							testParamMpr.setCreatedBy(testMaster.getCreatedBy());
							testParamMpr.setCreatedDate(TimeZoneComponent.getDateByzone(testMaster.getOrgUnitId()).getTime());
							testParamMpr.setOrgId(testMaster.getOrgId());
							testParamMpr.setHeaderSequence(i);
							testParamMpr.setOrgUnitId(testMaster.getOrgUnitId());
							iTestParamMpprDao.addTestParamMppr(testParamMpr);
						}
						i++;
					}
					return new Response(SUCCESS, SUCCESS_200, TEST_UPDATE_SUCC, null, null);
					/*for (Iterator iterator = testMst.getListTestParamMppr().iterator(); iterator.hasNext();)
					{
						TestParamMppr testParamMppr= (TestParamMppr) iterator.next();
						testParamMppr.setTestId(testMst.getTestId());
						testParamMppr.setUpdatedBy(testMaster.getUpdatedBy());
	                    testParamMppr.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId).getTime());
	                    testParamMppr.setOrgId(testMaster.getOrgId());
	                    testParamMppr.setOrgUnitId(testMaster.getOrgUnitId());
	                    testParamMppr.setIsDeleted('N');
	                    testParamMppr.setTestParaStatus('A');
	                    sessionFactory.getCurrentSession().update(testParamMppr);
	                   
	           	     }*/
	         //  	 return new Response(SUCCESS, SUCCESS_200, TEST_ADD_SUCC, null, testMst);
	            }else {
					return new Response(SUCCESS, SUCCESS_200, TEST_ADD_FAIL, null, null);
				}
    	  }
    	  
      } else {
        return new Response(SUCCESS, SUCCESS_200, TEST_UPDATE_FAIL, null, null);
      }
    } catch (HibernateException exception) {
      logger.error("Exection", exception);
    } catch (Exception e) {
      logger.error("Exection", e);
    }
    return new Response(ERROR, ERR_500, TEST_UPDATE_FAIL, null, null);
  }

  @Override
  public Response activeInactiveTest(Integer orgId, Integer orgUnitId, Integer testId,
      Integer parameterId, Character testStatus, Integer serviceId) throws ApplicationException {
    try {
    	
    	 BigInteger testMasterSameCount =(BigInteger) sessionFactory.getCurrentSession()
    	          .getNamedQuery("GET_CONT_OF_SAME_TEST_MASTER_WITH_DIFF_STATUS")
    	          .setParameter("orgUnitId", orgUnitId)
    	          .setParameter("serviceId", serviceId)
    	          .setParameter("testId", testId)
    	          .uniqueResult();

     if(testMasterSameCount.compareTo(BigInteger.ZERO) != 0 && testStatus =='A') {
    	 return new Response(ERROR, ERR_400, "SAME SERVICE ALREADY EXIST WITH ACTIVE STATUS" , null,0);
     }
    	
      int count = 0;
      Integer helpValueStatus = 0;
      TestMaster testMaster = findById(testId);
      testMaster.setTestStatus(testStatus);
      TestMaster testMst = update(testMaster);
      count++;
      if (count > 0) {
        for (Iterator iterator = testMst.getListTestParamMppr().iterator(); iterator.hasNext();) {
          TestParamMppr testParamMppr = (TestParamMppr) iterator.next();
          testParamMppr.setTestParaStatus(testStatus);
          sessionFactory.getCurrentSession().update(testParamMppr);
          count++;
        }
        if (count > 1) {
          ParameterMaster parameterMaster = (ParameterMaster) sessionFactory.getCurrentSession()
              .get(ParameterMaster.class, parameterId);
          parameterMaster.setStatus(testStatus);
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
          if (result > 0 || helpValueStatus > 0) {
            if (testMst.getTestStatus() == ACTIVE) {
              return new Response(SUCCESS, SUCCESS_200, TEST_ACTIVATE_SUCC, null, null);
            } else if (testMst.getTestStatus() == INACTIVE) {
              return new Response(SUCCESS, SUCCESS_200, TEST_INACTIVATE_SUCC, null, null);
            }
          } else if (testMst.getTestStatus() == ACTIVE) {
            return new Response(SUCCESS, SUCCESS_200, TEST_ACTIVATE_SUCC, null, null);
          } else if (testMst.getTestStatus() == INACTIVE) {
            return new Response(SUCCESS, SUCCESS_200, TEST_INACTIVATE_FAIL, null, null);
          }
        }
      }
    } catch (HibernateException exception) {
      logger.error("Exection", exception);
    } catch (Exception e) {
      logger.error("Exection", e);
    }
    return new Response(ERROR, ERR_500, TEST_UPDATE_FAIL, null, null);

  }

  @Override
  public Response listTestMaster(Integer orgId, Character isHistoCyto, Integer orgUnitId,
      Integer offset, Integer recordPerPage, String searchKeyword) throws ApplicationException {

    //try {
     /* List<ParameterMasterDto> listParameterMasterDto = new ArrayList();
      List<ParamRefrengMasterDto> listParamRefrengMasterDto = new ArrayList();

      List<TestMaster> testMasterList = (List<TestMaster>) sessionFactory.getCurrentSession()
          .getNamedQuery("GET_TEST_MASTER_LIST").setInteger("orgId", orgId)
          .setInteger("orgUnitId", orgUnitId).setCharacter("isHistoCyto", isHistoCyto)
          .setInteger("testType", SINGLE_PARAM_TEST).setFirstResult(offset != null ? offset : 0)
          .setMaxResults(recordPerPage != null ? recordPerPage : 10).list();

      for (Iterator iterator = testMasterList.iterator(); iterator.hasNext();) {
        listParameterMasterDto.clear();
        TestMaster testMaster = (TestMaster) iterator.next();
        TestMasterDto testMasterDto =
            mapper.map(testMaster, TestMasterDto.class, "TestMasterDtoTOTestMaster");
        testMasterDto.setSampleName(testMaster.getSampleMaster().getDesc());

        if (isHistoCyto == 'N') {
          testMasterDto.setTechniqueName(testMaster.getTechniqueMaster() == null ? ""
              : testMaster.getTechniqueMaster().getDesc());
          testMasterDto.setPrerequisitName(testMaster.getPrerequisitesMaster() == null ? ""
              : testMaster.getPrerequisitesMaster().getDesc());
        } else if (isHistoCyto == 'Y') {
          testMasterDto.setContainerName(testMaster.getContainerMaster().getDesc());
          testMasterDto.setReportTypeName(testMaster.getReportTypeMaster().getDesc());
          testMasterDto.setSpecimanName(testMaster.getSpecimanMaster().getDesc());
        }

        testMasterDto
            .setNormlTime(testMaster.getNorTurnAroundTimeMaster().getTatTimeName() == null ? ""
                : testMaster.getNorTurnAroundTimeMaster().getTatTimeName());
        testMasterDto
            .setUrgentTime(testMaster.getUrgTurnAroundTimeMaster().getTatTimeName() == null ? ""
                : testMaster.getUrgTurnAroundTimeMaster().getTatTimeName());
        testMasterDto.setSampleName(testMaster.getSampleMaster().getDesc());
        testMasterDto.setDeptName(testMaster.getSubSpecialityMaster().getSubSpecialityMasterName());
        for (Iterator iterator1 = testMasterDto.getListTestParamMppr().iterator(); iterator1
            .hasNext();) {
          listParamRefrengMasterDto.clear();
          TestParamMpprDto testParamMpprDto = (TestParamMpprDto) iterator1.next();

          Response<ParameterMasterDto, Integer> response =
              iParameterMasterDao.getParameterById(testParamMpprDto.getOrgId(),
                  testParamMpprDto.getOrgUnitId(), testParamMpprDto.getParameterId());
          ParameterMasterDto parameterMasterDto = (ParameterMasterDto) response.getObject();

          if (isHistoCyto == 'N') {
            List<HelpValueMasterDto> listHelpValueMasterDto =
                parameterMasterDto.getListHelpValueMaster().stream()
                    .filter(help -> help.getIsDeleted() != 'Y').collect(Collectors.toList());
            // parameterMasterDto.setListParamRefrengMaster(listParamRefrengMasterDto);
            parameterMasterDto.setListHelpValueMaster(listHelpValueMasterDto);
          }

          listParameterMasterDto.add(parameterMasterDto);
          // parameterMasterDto.setDeltaDays(parameterMst.getLabDayMaster().getDay()!=null?parameterMst.getLabDayMaster().getDay():null);
          listParameterMasterDto.add(parameterMasterDto);
        }
        List<ParameterMasterDto> newParamList = new ArrayList<>(listParameterMasterDto);
        testMasterDto.setListParameterMasterDto(newParamList);
        testMasterDtoList.add(testMasterDto);
      }*/
    	
    	
    	
	
    	 /**/

	  
		try
		{
			  if(isHistoCyto == 'N'){
				  List<TestMasterViewDto> listTestMasterViewDto = sessionFactory.getCurrentSession()
		    	          .getNamedQuery("GET_SINGLE_PARAM_TEST_LIST")
		    	          .setParameter("orgId", orgId)
		    	          .setParameter("unitId", orgUnitId)
		    	          .setParameter("isHistoCyto", isHistoCyto)
		    	          .setFirstResult(offset != null ? offset : 0)
						  .setMaxResults(recordPerPage != null ? recordPerPage : 10)
		    	          .setResultTransformer(Transformers.aliasToBean(TestMasterViewDto.class)).list();

				  return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listTestMasterViewDto, null);
				}else {
					List<TestMasterDto> testMasterDtoList = new ArrayList();
					Session session = sessionFactory.getCurrentSession();
					StringBuilder query=new StringBuilder(session.getNamedQuery("GET_TEST_MASTER_LIST_ON_SINGLE_PARAMETER").getQueryString());


					  if (searchKeyword != null && searchKeyword !="" && !searchKeyword.isEmpty()) {
						  query.append(" AND ( "
								  + " lower(tm.testDesc) like :searchKeyword "
								  + " OR lower(tm.testCode) like :searchKeyword "
								  + " OR lower(tm.testAlies) like :searchKeyword "
								  + " OR lower(subSpecialityMst.subSpecialityMasterName) like :searchKeyword "
								  + " OR lower(tm.identificationNo) like :searchKeyword "
								  + " ) ");
					  }


				  StringBuffer subQuery = new StringBuffer();
					if(isHistoCyto !='S'){
						subQuery.append(" and tm.testType=0 ");
					}
					
					query = query.append(subQuery.toString());
					Query actualQuery = session.createQuery(query.toString());

				  if (searchKeyword != null && searchKeyword !="" && !searchKeyword.isEmpty())
					  actualQuery.setParameter("searchKeyword", "%" +searchKeyword.toLowerCase() + "%");

						List<TestMaster> testMasterList = actualQuery
							.setParameter("orgId", orgId)
							.setParameter("isHistoCyto", isHistoCyto)
							.setParameter("orgUnitId", orgUnitId)
							//.setParameter("testType", MULTY_PARAM_TEST)
							.setFirstResult(offset != null ? offset : 0)
							.setMaxResults(recordPerPage != null ? recordPerPage : 10).list();
					for (Iterator iterator = testMasterList.iterator(); iterator.hasNext();)
					{
						TestMaster testMaster = (TestMaster) iterator.next();
						TestMasterDto testMasterDto = mapper.map(testMaster, TestMasterDto.class, "TestMasterDtoTOTestMaster");
						testMasterDto.setSampleName(testMaster.getSampleMaster().getDesc());
						testMasterDto.setTechniqueName(testMaster.getTechniqueMaster()==null?"":testMaster.getTechniqueMaster().getDesc());
						testMasterDto.setPrerequisitName(testMaster.getPrerequisitesMaster()==null?"":testMaster.getPrerequisitesMaster().getDesc());
						testMasterDto.setNormlTime(testMaster.getNorTurnAroundTimeMaster().getTatTimeName()==null?"":
							                     testMaster.getNorTurnAroundTimeMaster().getTatTimeName());
					    testMasterDto.setUrgentTime(testMaster.getUrgTurnAroundTimeMaster().getTatTimeName()==null?"":
					    							testMaster.getUrgTurnAroundTimeMaster().getTatTimeName());
						testMasterDto.setSampleDesc(testMaster.getSampleMaster().getDesc());
						testMasterDto.setStatus(testMaster.getTestStatus());
						testMasterDto.setDeptName(testMaster.getSubSpecialityMaster().getSubSpecialityMasterName());
						testMasterDtoList.add(testMasterDto);
					}

					return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, testMasterDtoList, null);
				}
		    	
	  
    } catch (Exception e) {
      logger.error("Exection", e);
      return new Response(ERROR, ERR_500, TEST_NOT_FOUND, null, null);
    }
  }

  @Override
  public Response getTotalTestRecord(Integer orgId, Character isHistoCyto, Integer orgUnitId)
      throws ApplicationException {
    try {
      BigInteger testMasterTotalRecordCount = (BigInteger) sessionFactory.getCurrentSession()
          .getNamedQuery("GET_TOTAL_TEST_RECORD").setParameter("orgId", orgId)
          .setParameter("isHistoCyto", isHistoCyto).setParameter("orgUnitId", orgUnitId)
          .setParameter("testType", SINGLE_PARAM_TEST).uniqueResult();
      if (testMasterTotalRecordCount.compareTo(BigInteger.ZERO) == 1) {
        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null,
            testMasterTotalRecordCount);
      } else {
        return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
      }

    } catch (Exception e) {
      logger.error("Exection", e);
      return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
    }
  }

  @Override
  public Response checkServiceAvaiable(TestMasterDto testMasterDto,boolean addOrUpdate) throws ApplicationException {
    try {
    	
    	
    	String query = null;
    	String queryAddCheck ="SELECT  "
				+ " COUNT(*) FROM lab.m_test_master " 
				+ " WHERE org_id=:orgId " 
				+ " AND org_unit_id=:orgUnitId "
				+ " AND service_id=:serviceId "
				+ " AND test_status= 'A' ";
    	     //   + " AND test_type = :testType ";
    	
			String queryUpdateCheck = "SELECT  " 
			   + " COUNT(*) FROM lab.m_test_master " 
					+ " WHERE org_id=:orgId "
					+ " AND org_unit_id=:orgUnitId " 
					+ " AND service_id=:serviceId " 
					+ " AND test_status= 'A' "
					//+ " AND test_type = :testType " 
					+ " AND test_id <> :testId ";
			 
     if(addOrUpdate)
     {
    	  query = queryAddCheck.toString();
     }
     else{
    	  query = queryUpdateCheck.toString();
     }
     
     
    // .setInteger("testType",testMasterDto.getTestType())
      Query sqlQuery =  sessionFactory.getCurrentSession()
         .createSQLQuery(query)
          .setParameter("orgId", testMasterDto.getOrgId())
          .setParameter("orgUnitId", testMasterDto.getOrgUnitId())
          .setParameter("serviceId", testMasterDto.getServiceId());

	 if(!addOrUpdate){
		 sqlQuery.setParameter("testId", testMasterDto.getTestId());
	 }

		BigInteger serviceCount = (BigInteger) sqlQuery.uniqueResult();
      
      
      
      UnitServiceMasterDetailsDto dto = (UnitServiceMasterDetailsDto)sessionFactory.getCurrentSession()
				.createSQLQuery("select is_outsource as \"isOutsource\" from service.t_unit_service_master_details where service_master_id  =:serviceId and unit_id =:unitId ")
				.setParameter("unitId", testMasterDto.getOrgUnitId())
				 .setParameter("serviceId", testMasterDto.getServiceId())
				.setResultTransformer(Transformers.aliasToBean(UnitServiceMasterDetailsDto.class)).uniqueResult();
      
      
      return new Response(SUCCESS, null, null, null, serviceCount,dto);
    } catch (Exception e) {
      logger.error("Exection", e);
      throw new ServiceException(e.getMessage());
    }
  }

@Override
public Response getTestDetails(Integer orgId, Integer unitId, Integer testId) throws ApplicationException {
    try {
       
       	 List<TestDetailsDto> listTestDetailsDto = sessionFactory.getCurrentSession()
       	          .getNamedQuery("GET_SINGLE_PARAM_TEST_DETAILS")
       	          .setParameter("orgId", orgId)
       	          .setParameter("unitId", unitId)
       	       .setParameter("testId", testId)
       	          .setResultTransformer(Transformers.aliasToBean(TestDetailsDto.class)).list();

         return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listTestDetailsDto, null);

       } catch (Exception e) {
         logger.error("Exection", e);
         return new Response(ERROR, ERR_500, TEST_NOT_FOUND, null, null);
       }
}

@Override
public Response getDeltaDetails(Integer orgId, Integer unitId, Integer testId) throws ApplicationException {
    try {
      
       	 List<TestDetailsDto> listTestDetailsDto = sessionFactory.getCurrentSession()
       	          .getNamedQuery("GET_SINGLE_PARAM_DELTA_DETAILS")
       	          .setParameter("testId", testId)
       	          .setResultTransformer(Transformers.aliasToBean(TestDetailsDto.class)).list();

         return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listTestDetailsDto, null);

       } catch (Exception e) {
         logger.error("Exection", e);
         return new Response(ERROR, ERR_500, TEST_NOT_FOUND, null, null);
       }
}

@Override
public Response getEquipmentList(Integer testId, Integer orgId, Integer unitId) throws ApplicationException {
	try {

		List<CommonDto> equipmentList = sessionFactory.getCurrentSession()
				.createSQLQuery("select  em.equipment_id as \"id\","
						+"	i.name as \"name\" " 
						+"  from lab.m_equipment_id_test_master em "  
						+"	join  public.m_item i on  em.equipment_id=i.id "  
						+"  where " 
						+"	em.test_id =:testId " 
						+"	and em.unit_id =:unitId and em.org_id=:orgId and status='A'")
				.setParameter("testId", testId)
				.setParameter("unitId", unitId)
				.setParameter("orgId", orgId)
				.setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();
		//System.out.println(equipmentList.toString());
		return new Response<>(SUCCESS, null, null, equipmentList, null);
	} catch (HibernateException he) {
		logger.error("Exection", he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error("Exection", e);
		throw new ServiceException(e.getMessage());
	}
}

@Override
public Response getParameterTestOnReportEntry(Map<Integer,Integer> labAndTestIds)throws ApplicationException {
	
	try {
		
		// List<CommonDto> equipmentList =  new ArrayList<>();
		
		List<TestMasterDto> TestMasterList =  new ArrayList<>();
		for (Entry<Integer,Integer> a : labAndTestIds.entrySet()) {
			
				Integer testId = a.getValue();
			
		    	TestMaster testMaster = findById(testId);
		    	char isHistoCyto = testMaster.getIsHistoCyto();
		    	List<ParameterMasterDto> listParameterMasterDto = new ArrayList();
		    	List<TestParamMpprDto> listTestParamMpprDto = new ArrayList();
		    
		      TestMasterDto testMasterDto = testMaster != null? mapper.map(testMaster, TestMasterDto.class, "TestMasterDtoTOTestMaster"): null;
		      if (testMaster != null) {
		    	  for (Iterator iterator = testMaster.getListTestParamMppr().iterator(); iterator.hasNext();) {
		            
		        	TestParamMppr testParamMppr = (TestParamMppr) iterator.next();
					if(testParamMppr.getIsDeleted()=='N')
					{
						Response<ParameterMasterDto, Integer> response =  iParameterMasterDao.getParameterById(testParamMppr.getOrgId(), testParamMppr.getOrgUnitId(), testParamMppr.getParameterId());
						ParameterMasterDto parameterMasterDto = (ParameterMasterDto) response.getObject();
						 TestParamMpprDto testParamMpprDto = testParamMppr != null
		                         ? mapper.map(testParamMppr, TestParamMpprDto.class, "TestParamMpprDtoToTestParamMppr")
		                         : null;        
		                         
		                         testParamMpprDto.setTechniqueId(testMaster.getTechniqueId()!=null ? testMaster.getTechniqueId():null);
		                         if(testParamMppr.getHeaderMaster() != null) {
		                        	 
		                        	 testParamMpprDto.setParameterDesc(parameterMasterDto.getParameterName() == null ? null :parameterMasterDto.getParameterName());
		                             testParamMpprDto.setHeaderDesc(testParamMppr.getHeaderMaster().getDesc());
		                             parameterMasterDto.setUnitName(parameterMasterDto.getUnitName() == null ? null :parameterMasterDto.getUnitName());
		                             if(parameterMasterDto.getListHelpValueMaster()!=null) {
			                        	   testParamMpprDto.setListHelpValueMaster(parameterMasterDto.getListHelpValueMaster());
			                           }
		                             listTestParamMpprDto.add(testParamMpprDto);   
		                         }else {
		                        	 testParamMpprDto.setParameterDesc(parameterMasterDto.getParameterName() == null ? null :parameterMasterDto.getParameterName());
		                            // testParamMpprDto.setHeaderDesc(testParamMppr.getHeaderMaster().getDesc());
		                           //  parameterMasterDto.setUnitName(parameterMasterDto.getUnitName() == null ? null :parameterMasterDto.getUnitName());
		                           if(parameterMasterDto.getListHelpValueMaster()!=null) {
		                        	   testParamMpprDto.setListHelpValueMaster(parameterMasterDto.getListHelpValueMaster());
		                           }
		                        	 listTestParamMpprDto.add(testParamMpprDto);    
		                         }
		             
		              listParameterMasterDto.add(parameterMasterDto);
		              listParameterMasterDto.add(parameterMasterDto);
					}
		          
		        }
		    	  testMasterDto.setListTestParamMppr(listTestParamMpprDto);
		    	  testMasterDto.setListParameterMasterDto(listParameterMasterDto);
		    	  testMasterDto.setLabSampleDtlsId(a.getKey());
		        TestMasterList.add(testMasterDto);
		      }
		      
		    
		}
    		  
      return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, TestMasterList, null);

    } catch (Exception e) {
      logger.error("Exection", e);
      return new Response(ERROR, ERR_500, TEST_NOT_FOUND, null, null);
    }
}

@Override
public Response<TestMasterDto, Integer> cytologyTestMasterUniversalSearch(Integer orgId, Integer orgUnitId,
		Integer offset, Integer recordPerPage, String testDesc,Character isHistoCyto) throws ApplicationException {
	try
	{
			  List<TestMasterViewDto> listTestMasterViewDto = sessionFactory.getCurrentSession()
	    	          .getNamedQuery("GET_PAGINATED_CYOTOLOGY_TEST_MASTER_DETAILS_LIST_SEARCH")  
	    	          .setParameter("orgId",orgId)
	    	          .setParameter("unitId",orgUnitId)
	    	          .setParameter("testDesc",  "%"+testDesc.trim().toLowerCase()+"%")
	    	          .setParameter("isHistoCyto", isHistoCyto)
	    	          .setFirstResult(offset!= null ? offset : 0)
					  .setMaxResults(recordPerPage != null ? recordPerPage : 10)
	    	          .setResultTransformer(Transformers.aliasToBean(TestMasterViewDto.class)).list();

			  return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listTestMasterViewDto, null);

} catch (HibernateException he) {
	logger.error("Exection", he);
	throw new ServiceException(he.getMessage());
} catch (Exception e) {
	logger.error("Exection", e);
	throw new ServiceException(e.getMessage());
}
}

@Override
public Response testMasterUniversalSearch(Integer orgId, Character isHistoCyto, Integer orgUnitId, Integer offset,
		Integer recordPerPage, String testDesc) throws ApplicationException {
	List<TestMasterViewDto> listTestMasterViewDto = new ArrayList();
	 if(isHistoCyto == 'N'){
		 listTestMasterViewDto = sessionFactory.getCurrentSession()
  	          .getNamedQuery("GET_PAGINATED_TEST_MASTER_DETAILS_LIST_SEARCH")
  	          .setParameter("orgId", orgId)
  	          .setParameter("unitId", orgUnitId)
  	          .setParameter("isHistoCyto", isHistoCyto)
  	          .setParameter("testDesc",  "%"+testDesc.trim().toLowerCase()+"%")
  	          .setFirstResult(offset != null ? offset : 0)
  	          .setMaxResults(recordPerPage != null ? recordPerPage : 10)
  	          .setResultTransformer(Transformers.aliasToBean(TestMasterViewDto.class)).list();
	 }
		  return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listTestMasterViewDto, null);
}

@Override
public Response getTotalSerologyTestRecord(Integer orgId, Character isHistoCyto, Integer orgUnitId, String searchKeyword)
		throws ApplicationException {
	 try {


		 Session session = sessionFactory.getCurrentSession();
		 StringBuilder query=new StringBuilder(session.getNamedQuery("GET_TOTAL_SEROLOGY_TEST_RECORD").getQueryString());

		 if (searchKeyword != null && searchKeyword !="" && !searchKeyword.isEmpty()) {
			 query.append(" AND ( "
					 + " lower(testmst.test_desc) like :searchKeyword "
					 + " OR lower(testmst.test_code) like :searchKeyword "
					 + " OR lower(testmst.test_alies) like :searchKeyword  "
					 + " OR lower(sub_speciality_mst.sub_speciality_name) like :searchKeyword  "
					 + " OR lower(testmst.identification_no) like :searchKeyword "
					 + " ) ");
		 }

		 Query sqlQuery = session.createSQLQuery(query.toString())
				 .setParameter("orgId", orgId)
				 .setParameter("isHistoCyto", isHistoCyto)
				 .setParameter("orgUnitId", orgUnitId);

		 if (searchKeyword != null && searchKeyword !="" && !searchKeyword.isEmpty())
			 sqlQuery.setParameter("searchKeyword", "%" + searchKeyword.toLowerCase() + "%");

		 BigInteger testMasterTotalRecordCount = (BigInteger) sqlQuery.uniqueResult();

	      if (testMasterTotalRecordCount.compareTo(BigInteger.ZERO) == 1) {
	        return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null,
	            testMasterTotalRecordCount);
	      } else {
	        return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
	      }

	    } catch (Exception e) {
		 	throw e;
	    }
}

@Override
public Response getOnHoldReportEntryDetail(TestMasterForOnHoldDto dto) throws ApplicationException {
	try {
    	List<TestMasterForOnHoldDto> listFirst=new ArrayList<>();
    	List<TestMasterForOnHoldDto> mainlist=new ArrayList<>();
    	
    	mainlist=sessionFactory.getCurrentSession()
    			.createSQLQuery("select " + 
    					"	distinct(serRslt.lab_sample_dtls_id) as \"labSampleDtlsId\", " + 
    					"	serRslt.serology_test_result_id as \"serologyTestResultId\", " + 
    					"	tlsd.dept_id as \"deptId\", " + 
    					"	serRslt.org_id as \"orgId\", " + 
    					"	serRslt.org_unit_id as \"orgUnitId\" " + 
    					" from " + 
    					"	lab.t_serology_test_result serRslt " + 
    					" inner join lab.t_lab_sample_dtls tlsd on " + 
    					"	tlsd.lab_sample_dtls_id = serRslt.lab_sample_dtls_id " + 
    					" inner join lab.m_test_master testmst on " + 
    					"	testmst.test_id = tlsd.test_id " + 
    					" where " + 
    					"	serRslt.status = 'A' and tlsd.lab_sample_dtls_id =:labSampleDtlsId")
    			.setParameter("labSampleDtlsId", dto.getLabSampleDtlsId())
    			.setResultTransformer(Transformers.aliasToBean(TestMasterForOnHoldDto.class)).list();
    	
    	for(TestMasterForOnHoldDto dto1 : mainlist) 
		{
    		listFirst=sessionFactory.getCurrentSession()
						.createSQLQuery("select 	"
								+ " tech_mst.technique_name as \"techniqueName\",  "
								+ " tech_mst.technique_id as \"techniqueId\",  "
								+ "	sero_test_mppr.serology_result_test_mppr_id as \"serologyResultTestMpprId\",  "
								+ "	para_mst.parameter_name as \"parameterName\",  "
								+ "	para_mst.parameter_name as \"parameterDesc\",  "
								+ "	sero_test_mppr.serology_test_result_id as \"serologyTestResultId\",  "
								+ "	sero_test_mppr.first_finding as \"firstFinding\",  "
								+ "	sero_test_mppr.first_finding_desc as \"desc\",  "
								+ "	sero_test_mppr.first_finding_desc as \"firstFindingDesc\",  "
								+ "	sero_test_mppr.second_finding as \"secondFinding\",  "
								+ "	sero_test_mppr.second_finding_desc as \"secondFindingDesc\", "
								+ " sero_test_mppr.finding_result as \"findingResult\" , "
								+ " sero_test_mppr.finding_result_desc as \"findingResultDesc\",  "
								+ " dtl.internal_notes as \"internalNotes\",  "
								+ " dtl.is_on_hold_parameter as \"isOnHoldParameter\",  "
								+ "	emp_mst.first_name || ' ' || emp_mst.last_name as \"resultValidatedUser\",  "
								+ " para_mst.parameter_id as \"parameterId\",  " + " test_result.org_id as \"orgId\", "
								+ " test_result.org_unit_id as \"orgUnitId\", "
								+ "	to_char(test_result.result_enter_date, 'DD-MM-YYYY') as \"resultValidatedDate\"  "
								+ "from  " + "	lab.t_serology_test_result test_result  "
								+ " left join lab.t_serology_result_test_mppr sero_test_mppr on  "
								+ "	test_result.serology_test_result_id = sero_test_mppr.serology_test_result_id  "
								+ " left join lab.m_technique_master tech_mst on  "
								+ "	sero_test_mppr.technique_id = tech_mst.technique_id  "
								+ " inner join lab.m_parameter_master para_mst on  "
								+ "	sero_test_mppr.parameter_id = para_mst.parameter_id  "
								+ " inner join lab.t_lab_sample_dtls dtl on  "
								+ "	dtl.lab_sample_dtls_id = test_result.lab_sample_dtls_id  "
								+ "left join public.m_employee_master emp_mst on  "
								+ "	test_result.result_validated_by = emp_mst.user_id  " + " where test_result.status = 'A' and "
								+ "	test_result.lab_sample_dtls_id =:labSampleDtlsId")
			.setParameter("labSampleDtlsId", dto1.getLabSampleDtlsId())
			
			.setResultTransformer(Transformers.aliasToBean(TestMasterForOnHoldDto.class)).list();

		dto1.setListTestParamMppr(listFirst);
		}
		return new Response(SUCCESS, null, null, mainlist, null);
} catch (Exception e) {
      logger.error("Exection", e);
      return new Response(ERROR, ERR_500, TEST_NOT_FOUND, null, null);
    }
}
}

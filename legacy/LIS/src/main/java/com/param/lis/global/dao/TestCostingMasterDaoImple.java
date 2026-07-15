package com.param.lis.global.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.global.TestCostingMapper;
import com.param.entity.lis.global.TestCostingMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dto.TestCostingMasterDto;

import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TestCostingMasterDaoImple implements ITestCostingMasterDao,ICommonConstants,IError {

	
	@Autowired SessionFactory sessionFactory;
	
	@Override
	public Response addTestHeadCosting(TestCostingMasterDto dto) throws ApplicationException {
		try{
			
			TestCostingMaster master=new TestCostingMaster();
			master.setCreatedBy(dto.getCreatedBy());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyy HH:mm:ss"));
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyy HH:mm:ss"));
			master.setStatus(dto.getStatus());
			master.setOrganizationId(dto.getOrganizationId());
			master.setUnitId(dto.getUnitId());
			master.setSpecialityId(dto.getSpecialityId());
			master.setSubSpecialityId(dto.getSubSpecialityId());
			master.setServiceId(dto.getServiceId());
			Integer id=(Integer) sessionFactory.getCurrentSession().save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null,id);
			
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){			
			throw e;
		}
		
	}

	@Override
	public Response saveTestCodeMapperDetails(TestCostingMasterDto dto) throws ApplicationException {
		try{
			
			TestCostingMapper master=new TestCostingMapper();
			master.setCreatedBy(dto.getCreatedBy());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyy HH:mm:ss"));
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyy HH:mm:ss"));
			master.setStatus(dto.getStatus());
			master.setOrganizationId(dto.getOrganizationId());
			master.setUnitId(dto.getUnitId());
			master.setTestCostHeadId(dto.getCostHeadId());
			master.setCost(dto.getCost());
			master.setTestCostingMasterId(dto.getTestCostingMasterId());
			sessionFactory.getCurrentSession().save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null,null);
			
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){			
			throw e;
		}
	}

	@Override
	public Response getTestHeadCostingById(TestCostingMasterDto dto) throws ApplicationException {
		
			try{
				List<TestCostingMasterDto> listTestCostingMasterDto=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_TEST_HEAD_COSTING_BY_ID")
						.setParameter("orgId", dto.getOrganizationId())
						.setParameter("unitId", dto.getUnitId())
						.setParameter("testCostingId", dto.getTestCostingMasterId())
						.setResultTransformer(Transformers.aliasToBean(TestCostingMasterDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, listTestCostingMasterDto,null);
			}catch(HibernateException he){
				throw he;
			}
			catch(Exception e){
				throw e;
			}
		
	}

	@Override
	public Response getTestHeadCostingList(TestCostingMasterDto dto) throws ApplicationException {
		try{
			List<TestCostingMasterDto> listTestCostingMasterDto=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEST_HEAD_COSTING_LIST")
					.setParameter("orgId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(TestCostingMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTestCostingMasterDto,null);
		}catch(HibernateException he){
			throw he;
		}
		catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response getTestHeadCostByCostingMstrId(TestCostingMasterDto dto) throws ApplicationException {
		try{
			List<TestCostingMasterDto> listTestCostingMasterDto=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEST_HEAD_COST_BY_COSTING_MSTR_ID")
					.setParameter("testCostingId", dto.getTestCostingMasterId())
					.setResultTransformer(Transformers.aliasToBean(TestCostingMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listTestCostingMasterDto,null);
		}catch(HibernateException he){
			throw he;
		}
		catch(Exception e){
			throw e;
		}
	}

}

package com.param.lis.unit.dao;

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


import com.param.entity.lis.unit.MicrobiologyTestMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.MicrobiologyTestMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class MicrobiologyTestMasterDaoImpl extends GenericDao<MicrobiologyTestMaster, Integer>implements IMicrobiologyTestMasterDao, ICommonConstants{

	public MicrobiologyTestMasterDaoImpl() {
		super(MicrobiologyTestMaster.class);
		
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveMicrobiologyTestMaster(MicrobiologyTestMasterDto microbiologyTestMasterDto)
			throws ApplicationException {
		try{
			MicrobiologyTestMaster microbiologyTestMaster=new MicrobiologyTestMaster();
			microbiologyTestMaster.setMicroTestCode(microbiologyTestMasterDto.getMicroTestCode());
			microbiologyTestMaster.setMicroTestDesc(microbiologyTestMasterDto.getMicroTestDesc());
			microbiologyTestMaster.setOrgId(microbiologyTestMasterDto.getOrgId());
			microbiologyTestMaster.setOrgUnitId(microbiologyTestMasterDto.getOrgUnitId());
			microbiologyTestMaster.setStatus(microbiologyTestMasterDto.getStatus());
			microbiologyTestMaster.setCreatedBy(microbiologyTestMasterDto.getCreatedBy());
			microbiologyTestMaster.setCreatedDate(GlobalCommonDateUtils.getDate(microbiologyTestMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			microbiologyTestMaster.setCreatedBy(microbiologyTestMasterDto.getUpdatedBy());
			microbiologyTestMaster.setUpdatedBy(microbiologyTestMasterDto.getUpdatedBy());
			microbiologyTestMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(microbiologyTestMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			save(microbiologyTestMaster);
			
		return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	} catch (HibernateException he) {
		throw he;
	} catch (Exception e) {
		throw e;
	}
}

	@Override
	public Response updateMicrobiologyTestMaster(MicrobiologyTestMasterDto microbiologyTestMasterDto)
			throws ApplicationException {
		try{
			MicrobiologyTestMaster microbiologyTestMaster=findById(microbiologyTestMasterDto.getMicroTestId());
			microbiologyTestMaster.setMicroTestCode(microbiologyTestMasterDto.getMicroTestCode());
			microbiologyTestMaster.setMicroTestDesc(microbiologyTestMasterDto.getMicroTestDesc());
			microbiologyTestMaster.setOrgId(microbiologyTestMasterDto.getOrgId());
			microbiologyTestMaster.setOrgUnitId(microbiologyTestMasterDto.getOrgUnitId());
			microbiologyTestMaster.setUpdatedBy(microbiologyTestMasterDto.getUpdatedBy());
			microbiologyTestMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(microbiologyTestMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(microbiologyTestMaster);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response updateMicrobiologyTestMasterStatus(MicrobiologyTestMasterDto microbiologyTestMasterDto)
			throws ApplicationException {
		try{
			MicrobiologyTestMaster microbiologyTestMaster=findById(microbiologyTestMasterDto.getMicroTestId());
			microbiologyTestMaster.setStatus(microbiologyTestMasterDto.getStatus());
			microbiologyTestMaster.setUpdatedBy(microbiologyTestMasterDto.getUpdatedBy());
			microbiologyTestMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(microbiologyTestMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(microbiologyTestMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}
	

	@Override
	public Response getMicrobiologyTestMasterList(MicrobiologyTestMasterDto microbiologyTestMasterDto)
			throws ApplicationException {
		try{
			List<MicrobiologyTestMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MICROBIOLOGY_TEST_LIST")
					//.setInteger("orgUnitId", microbiologyTestMasterDto.getOrgUnitId()) //dilip
					.setParameter("orgId", microbiologyTestMasterDto.getOrgId())
					.setFirstResult(microbiologyTestMasterDto.getOffset() != null ? microbiologyTestMasterDto.getOffset() : 0)
					.setMaxResults(microbiologyTestMasterDto.getNoOfRecordsPerPage() != null ? microbiologyTestMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(MicrobiologyTestMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE,  list, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@Override
	public Response getMicrobiologyTestMasterById(Integer microTestId, Integer orgId, Integer orgUnitId)
			throws ApplicationException {
		try{
			List<MicrobiologyTestMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MICROBIOLOGY_TEST_BY_ID")
					.setParameter("id", microTestId)
					.setParameter("orgId", orgId)
					//.setInteger("orgUnitId", orgUnitId)  //dilip
					.setResultTransformer(Transformers.aliasToBean(MicrobiologyTestMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null,  list, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}
	

	@Override
	public Response getMicrobiologyTestMasterCount(MicrobiologyTestMasterDto microbiologyTestMasterDto)
			throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(MicrobiologyTestMaster.class);
			crit.add(Restrictions.eq("orgId", microbiologyTestMasterDto.getOrgId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, count);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}
	

	@Override
	public Response getActiveMicrobiologyTestMasterList(MicrobiologyTestMasterDto microbiologyTestMasterDto)
			throws ApplicationException {
		try{
			List<MicrobiologyTestMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_MICROBIOLOGY_TEST_LIST")
					//.setInteger("orgUnitId", microbiologyTestMasterDto.getOrgUnitId())  //dilip
					.setParameter("orgId", microbiologyTestMasterDto.getOrgId())
					.setResultTransformer(Transformers.aliasToBean(MicrobiologyTestMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE,  list, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@Override
	public Response getMicrobiologyTestMasterByName(MicrobiologyTestMasterDto microbiologyTestMasterDto)
			throws ApplicationException {
		try{
			
			List<MicrobiologyTestMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MICROBIOLOGY_TEST_BY_NAME")
					.setParameter("orgId",microbiologyTestMasterDto.getOrgId())
					.setParameter("desc", microbiologyTestMasterDto.getMicroTestDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(MicrobiologyTestMasterDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}
	

	@Override
	public Response getMicrobiologyTestMasterByNameNotId(MicrobiologyTestMasterDto microbiologyTestMasterDto)
			throws ApplicationException {
		try{
			
			List<MicrobiologyTestMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MICROBIOLOGY_TEST_BY_NAME_NOT_ID")
					.setParameter("id", microbiologyTestMasterDto.getMicroTestId())
					.setParameter("desc", microbiologyTestMasterDto.getMicroTestDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(MicrobiologyTestMasterDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response getMicrobiologyTestMasterByCode(MicrobiologyTestMasterDto microbiologyTestMasterDto)
			throws ApplicationException {
try{
			
			List<MicrobiologyTestMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MICROBIOLOGY_TEST_BY_CODE")
					.setParameter("orgId",microbiologyTestMasterDto.getOrgId())
					.setParameter("code", microbiologyTestMasterDto.getMicroTestCode().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(MicrobiologyTestMasterDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response checkMicrobiologyTestMasterByCode(MicrobiologyTestMasterDto microbiologyTestMasterDto)
			throws ApplicationException {
		try{
			
			List<MicrobiologyTestMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_CHECK_UNIQUE_MICROBIOLOGY_TEST_CODE")
					.setParameter("id", microbiologyTestMasterDto.getMicroTestId())
					.setParameter("code", microbiologyTestMasterDto.getMicroTestCode().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(MicrobiologyTestMasterDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

}

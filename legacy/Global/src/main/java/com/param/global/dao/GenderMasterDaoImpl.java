package com.param.global.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.GenderMasterDto;
import com.param.global.model.GenderMaster;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class GenderMasterDaoImpl extends GenericDao<GenderMaster, Integer>
		implements IGenderMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(GenderMasterDaoImpl.class);


	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	SessionService sessionService;

	public GenderMasterDaoImpl() {
		super(GenderMaster.class);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public Response getGenderMasterList(GenderMasterDto genderMasterDto) throws ApplicationException {
		// TODO Auto-generated method stub
		try {
			List<GenderMasterDto> GenderMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_GENDER_LIST")
					.setFirstResult(genderMasterDto.getOffset() != null ? genderMasterDto.getOffset() : 0)
					.setMaxResults(genderMasterDto.getNoOfRecordsPerPage() != null ? genderMasterDto.getNoOfRecordsPerPage() : 10)
					.setParameter("orgId", genderMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(GenderMasterDto.class)).list();
			return new Response(SUCCESS, null, null, GenderMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getGenderMasterList failed", he);
		} catch (Exception e) {
		logger.error("getGenderMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getGenderByName(GenderMasterDto genderMasterDto) throws ApplicationException {
		try
		{
			List<GenderMasterDto> genderMasterDtoList = sessionFactory.getCurrentSession().getNamedQuery("GET_GENDER_BY_NAME")
					.setParameter("desc", genderMasterDto.getDesc().toLowerCase())
					.setParameter("orgId", genderMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(GenderMasterDto.class)).list();

			Integer prefixId=(Integer)sessionFactory.getCurrentSession()
					.createSQLQuery("select pm.prefix_id from m_prefix_master pm "
							+ " where pm.gender_id=:genderId")
					.setParameter("genderId", genderMasterDtoList.get(0).getId())
					.setMaxResults(1)
					.uniqueResult();
			genderMasterDtoList.get(0).setPreFixId(prefixId);
			return new Response(SUCCESS, null, null, genderMasterDtoList,null);
 			
		}catch(HibernateException he){
		logger.error("getGenderByName failed", he);
		} catch (Exception e) {
		logger.error("getGenderByName failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addGenderMaster(GenderMasterDto genderMasterDto) throws ApplicationException {
		try {
			GenderMaster genderMaster = mapper.map(genderMasterDto, GenderMaster.class,
					"genderMasterDto_to_genderMaster");
			genderMaster.setCreatedDate(TimeZoneComponent.getDateByzone(0));
			genderMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
			genderMaster.setStatus('A');
			genderMaster = save(genderMaster);
			return new Response(SUCCESS, null, null, null, genderMaster);
		} catch (HibernateException he) {
		logger.error("addGenderMaster failed", he);
		} catch (Exception e) {
		logger.error("addGenderMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);

	}

	@Override
	public Response getGenderByID(GenderMasterDto genderMasterDto) throws ApplicationException {
		try {
			List<GenderMasterDto> genderyMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_GENDER_BY_ID")
					.setParameter("id", genderMasterDto.getId())
					.setResultTransformer(Transformers.aliasToBean(GenderMasterDto.class)).list();
			return new Response(SUCCESS, null, null, genderyMasterDtoList, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getGenderByNameNotId(GenderMasterDto genderMasterDto) throws ApplicationException {
		try
		{
			List<GenderMasterDto> genderMasterDtoList = sessionFactory.getCurrentSession().getNamedQuery("GET_GENDER_BY_NAME_NOT_ID")
					.setParameter("id", genderMasterDto.getId())
					.setParameter("desc", genderMasterDto.getDesc().toLowerCase())
					.setParameter("orgId", genderMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(GenderMasterDto.class)).list();
			return new Response(SUCCESS, null, null, genderMasterDtoList,null);
 			
		}catch(HibernateException he){
		logger.error("getGenderByNameNotId failed", he);
		} catch (Exception e) {
		logger.error("getGenderByNameNotId failed", e);
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateGenderMaster(GenderMasterDto genderMasterDto) throws ApplicationException {
		try {
			GenderMaster genderMaster = findById(genderMasterDto.getId());
			genderMaster.setDesc(genderMasterDto.getDesc());
			genderMaster.setCode(genderMasterDto.getCode());
			genderMaster.setUpdatedBy(genderMasterDto.getUpdatedBy());
			genderMaster
					.setUpdatedDate(GlobalCommonDateUtils.getDate(genderMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(genderMaster);
			return new Response<GenderMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
		logger.error("updateGenderMaster failed", he);
			return new Response<GenderMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("updateGenderMaster failed", e);
			return new Response<GenderMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
			 ;
		}

	}

	@Override
	public Response updateGenderStatus(GenderMasterDto genderMasterDto) throws ApplicationException {
		try {

			GenderMaster genderMaster = findById(genderMasterDto.getId());
			genderMaster.setUpdatedBy(genderMasterDto.getUpdatedBy());
			genderMaster.setStatus(genderMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);

			genderMaster
					.setUpdatedDate(GlobalCommonDateUtils.getDate(genderMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(genderMaster);
			return new Response<GenderMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
		logger.error("updateGenderStatus failed", he);
			return new Response<GenderMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("updateGenderStatus failed", e);
			return new Response<GenderMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
			 ;
		}

	}

	@Override
	public Response getActiveGenderList() throws ApplicationException {
		//System.out.println(sessionService.getOrganizationId());
		try {
			List<GenderMasterDto> genderMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_GENDER_LIST")
					//.setParameter("orgId", sessionService.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(GenderMasterDto.class)).list();
			return new Response(SUCCESS, null, null, genderMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getActiveGenderList failed", he);
		} catch (Exception e) {
		logger.error("getActiveGenderList failed", e);
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}
	@Override
	public Response getCount(GenderMasterDto genderMasterDto)
	{
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(GenderMaster.class); 
			c.add(Restrictions.eq("organizationId", genderMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		}catch(Exception e)
		{
		logger.error("getCount failed", e);
			return new Response(ERROR, null, null, null, null);
		}
	}


	@Override
	public Integer getGenderIdByName(String gender, Integer hospitalId) throws ApplicationException {
		try{
			Integer genderId=(Integer)sessionFactory.getCurrentSession()
					.createSQLQuery("select gm.gender_id from m_gender_master gm "
					+ " where lower(gm.gender_name)=:name") 
					.setParameter("name", gender.toLowerCase())
					.uniqueResult();
			return genderId;
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}


	@Override
	public Integer getPrefixFromGenderId(Integer genderId, Integer hospitalId ,String prefixDesc) {try{
		
		Integer prefixId = 0;
		if(prefixDesc != null && prefixDesc != "") {
			
			prefixId=(Integer)sessionFactory.getCurrentSession()
					.createSQLQuery("select "
					+ " prefix_id  "
					+ " from "
					+ " 	m_prefix_master mpm "
					+ " where "
					+ " 	gender_id = "+genderId
					+ " 	and lower(prefix_desc) like '%"+prefixDesc.toLowerCase()+"%'"
					+ " 	and organization_id ="+hospitalId
					+ " 	order by 1 desc limit 1 ") 
					.uniqueResult();
		}
		

		if(  prefixId == null || prefixId == 0) {
			
			 prefixId=(Integer)sessionFactory.getCurrentSession()
					.createSQLQuery("select "
					+ " prefix_id  "
					+ " from "
					+ " 	m_prefix_master mpm "
					+ " where "
					+ " 	gender_id = "+genderId
					+ " 	and lower(prefix_desc) like 'm%' "
					+ " 	and organization_id ="+hospitalId
					+ " 	order by 1 desc limit 1 ") 
					.uniqueResult();
		}
		
		return prefixId;
	}catch (HibernateException he) {
		throw he;
	} catch (Exception e) {
		
		throw e;
	}
}

}

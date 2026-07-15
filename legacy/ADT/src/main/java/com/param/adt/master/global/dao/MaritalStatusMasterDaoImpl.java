package com.param.adt.master.global.dao;

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

import com.param.adt.common.ADTCommonDateUtils;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.MaritalStatusDto;
import com.param.adt.master.global.model.MaritalStatusMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class MaritalStatusMasterDaoImpl extends GenericDao<MaritalStatusMaster, Integer>
		implements IMaritalStatusMasterDao, ICommonConstants {

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private SessionFactory sessionFactory;

	public MaritalStatusMasterDaoImpl() {
		super(MaritalStatusMaster.class);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public Response getMaritalStatusByName(MaritalStatusDto maritalStatusDto) throws ApplicationException {
		try {
			List<MaritalStatusDto> maritalStatusMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MARITAL_STATUS_BY_NAME")
					.setParameter("desc", maritalStatusDto.getDesc().toLowerCase())
					.setParameter("orgId", maritalStatusDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(MaritalStatusDto.class)).list();
			return new Response(SUCCESS, null, null, maritalStatusMasterDtoList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addMaritalStatusMaster(MaritalStatusDto maritalStatusDto) throws ApplicationException {
		try {
			MaritalStatusMaster maritalStatusMaster = mapper.map(maritalStatusDto, MaritalStatusMaster.class,
					"maritalStatusDto_to_maritalStatusMaster");
			maritalStatusMaster.setCreatedDate(TimeZoneComponent.getDateByzone(0));
			maritalStatusMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
			maritalStatusMaster.setStatus('A');
			maritalStatusMaster = save(maritalStatusMaster);
			return new Response(SUCCESS, null, null, null, maritalStatusMaster);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getMaritalStatusByNameNotId(MaritalStatusDto maritalStatusDto) throws ApplicationException {
		try {
			List<MaritalStatusDto> maritalMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MARITAL_STATUS_BY_NAME_NOT_ID")
					.setParameter("id", maritalStatusDto.getId())
					.setParameter("desc", maritalStatusDto.getDesc().toLowerCase())
					.setParameter("orgId", maritalStatusDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(MaritalStatusDto.class)).list();
			return new Response(SUCCESS, null, null, maritalMasterDtoList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateMaritalStatus(MaritalStatusDto maritalStatusDto) throws ApplicationException {
		try {
			MaritalStatusMaster maritalMaster = findById(maritalStatusDto.getId());
			maritalMaster.setUpdatedBy(maritalStatusDto.getUpdatedBy());
			maritalMaster.setStatus(maritalStatusDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			maritalMaster
					.setUpdatedDate(ADTCommonDateUtils.getDate(maritalStatusDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(maritalMaster);
			return new Response<MaritalStatusDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, maritalMaster.getOrganizationId());

		} catch (HibernateException he) {
			return new Response<MaritalStatusDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			
			return new Response<MaritalStatusDto, Integer>(ERROR, null, null, null, null);
		} finally {
			 ;
		}
		
	}

	@Override
	public Response getMaritalStatusByID(MaritalStatusDto maritalStatusDto) throws ApplicationException {
		try {
			List<MaritalStatusDto> maritalMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MARITAL_STATUS_BY_ID")
					.setParameter("id", maritalStatusDto.getId())
					.setResultTransformer(Transformers.aliasToBean(MaritalStatusDto.class)).list();
			return new Response(SUCCESS, null, null, maritalMasterDtoList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveMaritalStatusList(Integer orgId) throws ApplicationException {
		try {
			List<MaritalStatusDto> maritalMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_MARITAL_STATUS_LIST")
					.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(MaritalStatusDto.class)).list();
			return new Response(SUCCESS, null, null, maritalMasterDtoList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getMaritalStatusMasterList(MaritalStatusDto maritalStatusDto) throws ApplicationException {
		try {
			List<MaritalStatusDto> maritalStatusMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MARITAL_STATUS_LIST")
					.setParameter("orgId",maritalStatusDto.getOrganizationId())
					.setFirstResult(maritalStatusDto.getOffset() != null ? maritalStatusDto.getOffset() : 0)
					.setMaxResults(maritalStatusDto.getNoOfRecordsPerPage() != null ? maritalStatusDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(MaritalStatusDto.class)).list();
			return new Response(SUCCESS, null, null, maritalStatusMasterDtoList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateMaritalStatusMaster(MaritalStatusDto maritalStatusDto) throws ApplicationException {
	
		try {

			MaritalStatusMaster maritalMaster = findById(maritalStatusDto.getId());
			maritalMaster.setDesc(maritalStatusDto.getDesc());
			maritalMaster.setCode(maritalStatusDto.getCode());
			maritalMaster.setUpdatedBy(maritalStatusDto.getUpdatedBy());
			maritalMaster
					.setUpdatedDate(ADTCommonDateUtils.getDate(maritalStatusDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(maritalMaster);
			return new Response<MaritalStatusDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, maritalMaster);

		} catch (HibernateException he) {
			return new Response<MaritalStatusDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			
			return new Response<MaritalStatusDto, Integer>(ERROR, null, null, null, null);
		} finally {
			 ;
		}
	}
	
	@Override
	public Response getCount(MaritalStatusDto maritalStatusDto)
	{
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(MaritalStatusMaster.class); 
			c.add(Restrictions.eq("organizationId", maritalStatusDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		}catch(Exception e)
		{
			
			return new Response(ERROR, null, null, null, null);
		}
	}

}

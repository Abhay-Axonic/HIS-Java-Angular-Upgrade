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
import com.param.adt.master.global.dto.ConsentTypeMasterDto;
import com.param.adt.master.global.model.ConsentTypeMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ConsentTypeMasterDaoImpl extends GenericDao<ConsentTypeMaster, Integer>
		implements IConsentTypeMasterDao, ICommonConstants {

	public ConsentTypeMasterDaoImpl() {
		super(ConsentTypeMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Response getConsentTypeByName(ConsentTypeMasterDto consentTypeMasterDto) throws ApplicationException {
		try {
			List<ConsentTypeMasterDto> consentTypeMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CONSENT_TYPE_LIST_BY_NAME")
					.setParameter("orgId", consentTypeMasterDto.getOrganizationId())
					.setParameter("unitId", consentTypeMasterDto.getUnitId())
					.setParameter("consentTypeName", consentTypeMasterDto.getConsentTypeMasterName())
					.setResultTransformer(Transformers.aliasToBean(ConsentTypeMasterDto.class)).list();
			return new Response(SUCCESS, null, null, consentTypeMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addConsentTypeMaster(ConsentTypeMasterDto consentTypeMasterDto) throws ApplicationException {
		try {
			ConsentTypeMaster consentTypeMaster = mapper.map(consentTypeMasterDto, ConsentTypeMaster.class,
					"ConsentTypeMasterDto_to_ConsentTypeMaster");
			consentTypeMaster.setCreatedDate(TimeZoneComponent.getDateByzone(consentTypeMasterDto.getUnitId() != null ? consentTypeMasterDto.getUnitId() : 0));
			consentTypeMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(consentTypeMasterDto.getUnitId() != null ? consentTypeMasterDto.getUnitId() : 0));
			consentTypeMaster.setStatus('A');
			consentTypeMaster = save(consentTypeMaster);
			return new Response(SUCCESS, null, null, null, consentTypeMaster);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getConsentTypeByNameNotId(ConsentTypeMasterDto consentTypeMasterDto) throws ApplicationException {
		try {
			List<ConsentTypeMasterDto> consentTypeMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CONSENT_TYPE_LIST_BY_NAME_NOT_ID")
					.setParameter("consentTypeName", consentTypeMasterDto.getConsentTypeMasterName())
					.setParameter("orgId", consentTypeMasterDto.getOrganizationId())
					.setParameter("unitId", consentTypeMasterDto.getUnitId())
					.setParameter("consentTypeId", consentTypeMasterDto.getConsentTypeMasterId())
					.setResultTransformer(Transformers.aliasToBean(ConsentTypeMasterDto.class)).list();
			return new Response(SUCCESS, null, null, consentTypeMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateConsentTypeMaster(ConsentTypeMasterDto consentTypeMasterDto) throws ApplicationException {
		try {

			ConsentTypeMaster consentTypeMaster = findById(consentTypeMasterDto.getConsentTypeMasterId());
			consentTypeMaster.setConsentTypeMasterCode(consentTypeMasterDto.getConsentTypeMasterCode());
			consentTypeMaster.setConsentTypeMasterName(consentTypeMasterDto.getConsentTypeMasterName());
			consentTypeMaster.setAdmissionConsent(consentTypeMasterDto.getAdmissionConsent());
			consentTypeMaster.setUpdatedBy(consentTypeMasterDto.getUpdatedBy());
			consentTypeMaster.setUpdatedDate(
					ADTCommonDateUtils.getDate(consentTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(consentTypeMaster);
			return new Response<ConsentTypeMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
			return new Response<ConsentTypeMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			
			return new Response<ConsentTypeMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}

	@Override
	public Response getConsentTypeById(ConsentTypeMasterDto consentTypeMasterDto) throws ApplicationException {
		try {
			List<ConsentTypeMasterDto> consentTypeMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CONSENT_TYPE_LIST_BY_ID")
					.setParameter("consentTypeId", consentTypeMasterDto.getConsentTypeMasterId())
					.setResultTransformer(Transformers.aliasToBean(ConsentTypeMasterDto.class)).list();
			return new Response(SUCCESS, null, null, consentTypeMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateConsentTypeStatus(ConsentTypeMasterDto consentTypeMasterDto) throws ApplicationException {
		try {

			ConsentTypeMaster consentTypeMaster = findById(consentTypeMasterDto.getConsentTypeMasterId());
			consentTypeMaster.setStatus(consentTypeMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			consentTypeMaster.setUpdatedBy(consentTypeMasterDto.getUpdatedBy());
			consentTypeMaster.setUpdatedDate(
					ADTCommonDateUtils.getDate(consentTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(consentTypeMaster);
			return new Response<ConsentTypeMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
			return new Response<ConsentTypeMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			
			return new Response<ConsentTypeMasterDto, Integer>(ERROR, null, null, null, null);
		} 
	}

	@Override
	public Response getConsentTypeMasterList(ConsentTypeMasterDto consentTypeMasterDto) throws ApplicationException {
		try {
			List<ConsentTypeMasterDto> consentTypeMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CONSENT_TYPE_LIST")
					.setParameter("orgId", consentTypeMasterDto.getOrganizationId())
					.setParameter("unitId", consentTypeMasterDto.getUnitId())
					.setFirstResult(consentTypeMasterDto.getOffset() != null ? consentTypeMasterDto.getOffset() : 0)
					.setMaxResults(consentTypeMasterDto.getNoOfRecordsPerPage() != null ? consentTypeMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(ConsentTypeMasterDto.class)).list();
			return new Response(SUCCESS, null, null, consentTypeMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveConsentTypeList(Integer orgId,Integer unitId) throws ApplicationException {
		try {
			List<ConsentTypeMasterDto> consentTypeMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_CONSENT_TYPE_LIST")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(ConsentTypeMasterDto.class)).list();
			return new Response(SUCCESS, null, null, consentTypeMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getCount(ConsentTypeMasterDto consentTypeMasterDto)
	{
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(ConsentTypeMaster.class); 
			c.add(Restrictions.eq("organizationId", consentTypeMasterDto.getOrganizationId()));
			c.add(Restrictions.eq("unitId", consentTypeMasterDto.getUnitId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		}catch(Exception e)
		{
			
			return new Response(ERROR, null, null, null, null);
		}
	}

}

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
import com.param.adt.master.global.dto.PatientSourceMasterDto;
import com.param.adt.master.global.model.PatientSourceMaster;
import com.param.global.service.VerifiedIdentityService;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes","unchecked"})
@Repository
public class PatientSourceMasterDaoImpl extends GenericDao<PatientSourceMaster, Integer> implements ICommonConstants,IPatientServiceMasterDao{

	public PatientSourceMasterDaoImpl( ) {
		super(PatientSourceMaster.class);
		// TODO Auto-generated constructor stub
	}

	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;
	
	@Override
	public Response getPatientSourceByName(PatientSourceMasterDto patientSourceMasterDto) throws ApplicationException {
		try {
			List<PatientSourceMasterDto> patientSourceMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_SOURCE_LIST_BY_NAME")
					.setParameter("orgId", patientSourceMasterDto.getOrganizationId())
					.setParameter("desc", patientSourceMasterDto.getDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(PatientSourceMasterDto.class)).list();
			return new Response(SUCCESS, null, null, patientSourceMasterDtoList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addPatientSourceTypeMaster(PatientSourceMasterDto patientSourceMasterDto)
			throws ApplicationException {
		try {
			PatientSourceMaster patientSourceMaster = mapper.map(patientSourceMasterDto, PatientSourceMaster.class, "PatientSourceMasterDto_to_PatientSourceMaster");

			// BR-143: cross-tenant IDOR fix -- create is bound to the JWT-verified
			// org, never a client-supplied organizationId from the DTO.
			patientSourceMaster.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			patientSourceMaster.setCreatedDate(TimeZoneComponent.getDateByzone(0));
			patientSourceMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
			patientSourceMaster.setStatus('A');
			patientSourceMaster = save(patientSourceMaster);
			return new Response(SUCCESS, null, null, null, patientSourceMaster);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientSourceMasterList(PatientSourceMasterDto patientSourceMasterDto) throws ApplicationException {
		try {
			List<PatientSourceMasterDto> patientSourceMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_SOURCE_LIST")
					.setParameter("orgId", patientSourceMasterDto.getOrganizationId())
					.setFirstResult(patientSourceMasterDto.getOffset() != null ? patientSourceMasterDto.getOffset() : 0)
					.setMaxResults(patientSourceMasterDto.getNoOfRecordsPerPage() != null ? patientSourceMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(PatientSourceMasterDto.class)).list();
			return new Response(SUCCESS, null, null, patientSourceMasterDtoList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientSourceById(PatientSourceMasterDto patientSourceMasterDto) throws ApplicationException {
		try {
			List<PatientSourceMasterDto> patientSourceMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_SOURCE_LIST_BY_ID")
					.setParameter("id", patientSourceMasterDto.getId())
					.setResultTransformer(Transformers.aliasToBean(PatientSourceMasterDto.class)).list();

			// BR-143: cross-tenant IDOR fix -- filter the by-id result to the
			// JWT-verified org so a forged id can never read another org's row.
			if (patientSourceMasterDtoList != null && !patientSourceMasterDtoList.isEmpty()) {
				PatientSourceMaster row = findById(patientSourceMasterDtoList.get(0).getId());
				if (row == null || row.getOrganizationId() == null
						|| !row.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())) {
					patientSourceMasterDtoList = new java.util.ArrayList<>();
				}
			}
			return new Response(SUCCESS, null, null, patientSourceMasterDtoList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActivePatientSourceList(Integer organizationId) throws ApplicationException {
		try {
			List<PatientSourceMasterDto> patientSourceMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_PATIENT_SOURCE_LIST")
					.setParameter("orgId", organizationId)
					.setResultTransformer(Transformers.aliasToBean(PatientSourceMasterDto.class)).list();
			return new Response(SUCCESS, null, null, patientSourceMasterDtoList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientSourceByNameNotId(PatientSourceMasterDto patientSourceMasterDto)
			throws ApplicationException {
		try {
			List<PatientSourceMasterDto> patientSourceMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_SOURCE_LIST_BY_NAME_NOT_ID")
					.setParameter("id", patientSourceMasterDto.getId())
					.setParameter("orgId", patientSourceMasterDto.getOrganizationId())
					.setParameter("desc", patientSourceMasterDto.getDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(PatientSourceMasterDto.class)).list();
			return new Response(SUCCESS, null, null, patientSourceMasterDtoList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updatePatientSource(PatientSourceMasterDto patientSourceMasterDto) throws ApplicationException {
		try {
			PatientSourceMaster patientSourceMaster = findById(patientSourceMasterDto.getId());

			// BR-143: cross-tenant IDOR fix -- the target row's organization must
			// match the JWT-verified identity, never a client-supplied value.
			// (VerifiedIdentityService derives org from the unitId header, which
			// JwtTokenFilter has already validated against the signed JWT.)
			if (patientSourceMaster == null || patientSourceMaster.getOrganizationId() == null
					|| !patientSourceMaster.getOrganizationId()
							.equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
			}

			patientSourceMaster.setDesc(patientSourceMasterDto.getDesc());
			patientSourceMaster.setCode(patientSourceMasterDto.getCode());
			patientSourceMaster.setUpdatedBy(patientSourceMasterDto.getUpdatedBy());
			patientSourceMaster.setUpdatedDate(ADTCommonDateUtils.getDate(patientSourceMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
			update(patientSourceMaster);
			return new Response<PatientSourceMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, patientSourceMaster);

		} catch (HibernateException he) {
			return new Response<PatientSourceMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			
			return new Response<PatientSourceMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}

	@Override
	public Response updatePatientSourceStatus(PatientSourceMasterDto patientSourceMasterDto)
			throws ApplicationException {
		try {
			PatientSourceMaster patientSourceMaster = findById(patientSourceMasterDto.getId());

			// BR-143: cross-tenant IDOR fix -- same ownership check as updatePatientSource.
			if (patientSourceMaster == null || patientSourceMaster.getOrganizationId() == null
					|| !patientSourceMaster.getOrganizationId()
							.equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
			}

			patientSourceMaster.setUpdatedBy(patientSourceMasterDto.getUpdatedBy());
			patientSourceMaster.setUpdatedDate(ADTCommonDateUtils.getDate(patientSourceMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
			patientSourceMaster.setStatus(patientSourceMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			update(patientSourceMaster);
			return new Response<PatientSourceMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, patientSourceMaster.getOrganizationId());

		} catch (HibernateException he) {
			return new Response<PatientSourceMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			
			return new Response<PatientSourceMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}
	
	@Override
	public Response getCount(PatientSourceMasterDto patientSourceMasterDto)
	{
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(PatientSourceMaster.class); 
			c.add(Restrictions.eq("organizationId", patientSourceMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		}catch(Exception e)
		{
			
			return new Response(ERROR, null, null, null, null);
		}
	}

}

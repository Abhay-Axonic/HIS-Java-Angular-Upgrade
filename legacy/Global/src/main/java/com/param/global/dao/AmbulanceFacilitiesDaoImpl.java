package com.param.global.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.AmbulanceFacilitiesDto;
import com.param.global.dto.AmbulanceTypeDto;
import com.param.global.model.AmbulanceFacilities;
import com.param.global.model.AmbulanceType;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AmbulanceFacilitiesDaoImpl extends GenericDao<AmbulanceFacilities, Integer>
		implements IAmbulanceFacilitiesDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(AmbulanceFacilitiesDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	public AmbulanceFacilitiesDaoImpl() {
		super(AmbulanceFacilities.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response getAmbulanceFacilitiesByName(AmbulanceFacilitiesDto ambulanceFacilitiesDto) throws ApplicationException {
		try {
			List<AmbulanceFacilitiesDto> tarrifMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_FACILITIES_LIST_BY_NAME")
					.setParameter("ambFacilities", ambulanceFacilitiesDto.getAmbFacilities().toLowerCase())
					.setParameter("organizationId", ambulanceFacilitiesDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AmbulanceFacilitiesDto.class)).list();
			return new Response(SUCCESS, null, null, tarrifMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveAmbulanceFacilities(AmbulanceFacilitiesDto ambulanceFacilitiesDto) throws ApplicationException {
		try {

			AmbulanceFacilities ambulanceFacilities=new AmbulanceFacilities();
			ambulanceFacilities.setAmbFacilities(ambulanceFacilitiesDto.getAmbFacilities());
			ambulanceFacilities.setStatus(ambulanceFacilitiesDto.getStatus());
			ambulanceFacilities.setOrganizationId(ambulanceFacilitiesDto.getOrganizationId());
			ambulanceFacilities.setCreatedBy(ambulanceFacilitiesDto.getCreatedBy());
			ambulanceFacilities.setCreatedDate(ambulanceFacilitiesDto.getCreatedDate().toString());
			ambulanceFacilities.setUpdatedBy(ambulanceFacilitiesDto.getUpdatedBy());
			ambulanceFacilities.setUpdatedDate(ambulanceFacilitiesDto.getUpdatedDate().toString());

		    sessionFactory.getCurrentSession().save(ambulanceFacilities);

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAmbulanceFacilitiesList(AmbulanceFacilitiesDto ambulanceFacilitiesDto) throws ApplicationException {
		try {

			Integer count=0;
			List<AmbulanceFacilitiesDto> ambulanceTypeMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_FACILITIES_LIST")
					.setParameter("organizationId", ambulanceFacilitiesDto.getOrganizationId())
					.setFirstResult(ambulanceFacilitiesDto.getOffset() != null ? ambulanceFacilitiesDto.getOffset() : 0)
					.setMaxResults(ambulanceFacilitiesDto.getNoOfRecordsPerPage() != null ? ambulanceFacilitiesDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(AmbulanceFacilitiesDto.class)).list();


			List<AmbulanceFacilitiesDto> ambulanceTypeMasterList1 = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_FACILITIES_LIST")
					.setParameter("organizationId", ambulanceFacilitiesDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AmbulanceFacilitiesDto.class)).list();

			  if(ambulanceTypeMasterList1 != null && ambulanceTypeMasterList1.size()>0) {
		    	  count = ambulanceTypeMasterList1.size();
		      }

			return new Response(SUCCESS, null, null, ambulanceTypeMasterList, count);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAmbulanceFacilitiesActiveList(AmbulanceFacilitiesDto ambulanceFacilitiesDto) throws ApplicationException {
		try {
			List<AmbulanceFacilitiesDto> ambulanceTarrifMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_AMBULANCE_FACILITIES_LIST")
					.setParameter("organizationId", ambulanceFacilitiesDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AmbulanceFacilities.class)).list();
			return new Response(SUCCESS, null, null, ambulanceTarrifMasterList, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAmbulanceFacilitiesByID(AmbulanceFacilitiesDto ambulanceFacilitiesDto) throws ApplicationException {
		try {
			List<AmbulanceFacilitiesDto> ambTarrifMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_FACILITIES_LIST_BY_ID")
					.setParameter("ambulanceFacilitiesMasterId", ambulanceFacilitiesDto.getAmbulanceFacilitiesMasterId())
					.setParameter("organizationId", ambulanceFacilitiesDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AmbulanceFacilitiesDto.class)).list();
			return new Response(SUCCESS, null, null, ambTarrifMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSingleAmbulanceFacilities(AmbulanceFacilitiesDto ambulanceFacilitiesDto) throws ApplicationException {
		try {
			List<AmbulanceFacilitiesDto> ambulanceTarrifMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_FACILITIES_SINGLE_REC")
					.setParameter("organizationId", ambulanceFacilitiesDto.getOrganizationId())
					.setParameter("ambulanceFacilitiesMasterId", ambulanceFacilitiesDto.getAmbulanceFacilitiesMasterId())
					.setFirstResult(ambulanceFacilitiesDto.getOffset() != null ? ambulanceFacilitiesDto.getOffset() : 0)
					.setMaxResults(ambulanceFacilitiesDto.getNoOfRecordsPerPage() != null ? ambulanceFacilitiesDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(AmbulanceFacilitiesDto.class)).list();
			return new Response(SUCCESS, null, null, ambulanceTarrifMasterList, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateAmbulanceFacilities(AmbulanceFacilitiesDto ambulanceFacilitiesDto) throws ApplicationException {
		try {
			AmbulanceFacilities ambulanceFacilities = findById(ambulanceFacilitiesDto.getAmbulanceFacilitiesMasterId());
			ambulanceFacilities.setAmbFacilities(ambulanceFacilitiesDto.getAmbFacilities());
			ambulanceFacilities.setOrganizationId(ambulanceFacilitiesDto.getOrganizationId());
			ambulanceFacilities.setUpdatedBy(ambulanceFacilitiesDto.getUpdatedBy());
			ambulanceFacilities.setUpdatedDate(ambulanceFacilitiesDto.getUpdatedDate());
			sessionFactory.getCurrentSession().update(ambulanceFacilities);
			return new Response<AmbulanceFacilitiesDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			return new Response<AmbulanceFacilitiesDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new Response<AmbulanceFacilitiesDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}

	@Override
	public Response changeAmbulanceFacilitiesStatus(AmbulanceFacilitiesDto ambulanceFacilitiesDto) throws ApplicationException {
		try {

			AmbulanceFacilities ambulanceFacilities = findById(ambulanceFacilitiesDto.getAmbulanceFacilitiesMasterId());
			ambulanceFacilities.setUpdatedBy(ambulanceFacilitiesDto.getUpdatedBy());
			ambulanceFacilities.setStatus(ambulanceFacilitiesDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			ambulanceFacilities.setUpdatedDate(ambulanceFacilitiesDto.getUpdatedDate());
			sessionFactory.getCurrentSession().update(ambulanceFacilities);
			return new Response<AmbulanceFacilitiesDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			return new Response<AmbulanceFacilitiesDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new Response<AmbulanceFacilitiesDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}



}

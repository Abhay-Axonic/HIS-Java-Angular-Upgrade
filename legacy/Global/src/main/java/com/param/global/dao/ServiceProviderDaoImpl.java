package com.param.global.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.ServiceProviderDto;
import com.param.global.model.ServiceProvider;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ServiceProviderDaoImpl extends GenericDao<ServiceProvider, Integer>
		implements IServiceProviderDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(ServiceProviderDaoImpl.class);


	@Autowired
	private SessionFactory sessionFactory;

	public ServiceProviderDaoImpl() {
		super(ServiceProvider.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response getServiceProviderByName(ServiceProviderDto serviceProviderDto) throws ApplicationException {
		try {
			List<ServiceProviderDto> tarrifMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_PROVIDER_LIST_BY_NAME")
					.setParameter("serviceProviderName", serviceProviderDto.getServiceProviderName().toLowerCase())
					.setParameter("organizationId", serviceProviderDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ServiceProviderDto.class)).list();
			return new Response(SUCCESS, null, null, tarrifMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getServiceProviderByName failed", he);
		} catch (Exception e) {
		logger.error("getServiceProviderByName failed", e);
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveServiceProvider(ServiceProviderDto serviceProviderDto) throws ApplicationException {
		try {
			
			ServiceProvider serviceProvider=new ServiceProvider();
			serviceProvider.setServiceProviderName(serviceProviderDto.getServiceProviderName());
			serviceProvider.setServiceProviderAddress(serviceProviderDto.getServiceProviderAddress());
			serviceProvider.setServiceProviderCountry(serviceProviderDto.getServiceProviderCountry());
			serviceProvider.setServiceProviderState(serviceProviderDto.getServiceProviderState());
			serviceProvider.setServiceProviderDistrict(serviceProviderDto.getServiceProviderDistrict());
			serviceProvider.setServiceProviderCity(serviceProviderDto.getServiceProviderCity());
			serviceProvider.setServiceProviderArea(serviceProviderDto.getServiceProviderArea());
			serviceProvider.setServiceProviderZipCode(serviceProviderDto.getServiceProviderZipCode());
			serviceProvider.setServiceProviderAmbulanceType(serviceProviderDto.getServiceProviderAmbulanceType());
			serviceProvider.setServiceProviderPersonName(serviceProviderDto.getServiceProviderPersonName());
			serviceProvider.setServiceProviderPhoneNo(serviceProviderDto.getServiceProviderPhoneNo());
			serviceProvider.setServiceProviderCellNo(serviceProviderDto.getServiceProviderCellNo());
			serviceProvider.setServiceProviderEmailAddress(serviceProviderDto.getServiceProviderEmailAddress());
			serviceProvider.setServiceProviderContractFromDate(serviceProviderDto.getServiceProviderContractFromDate());
			serviceProvider.setServiceProviderContractToDate(serviceProviderDto.getServiceProviderContractToDate());
			serviceProvider.setServiceProviderFirstDistance(serviceProviderDto.getServiceProviderFirstDistance());
			serviceProvider.setServiceProviderFirstDistanceRate(serviceProviderDto.getServiceProviderFirstDistanceRate());
			serviceProvider.setServiceProviderAdditionalMileage(serviceProviderDto.getServiceProviderAdditionalMileage());
			serviceProvider.setServiceProviderAdditionalMileageRate(serviceProviderDto.getServiceProviderAdditionalMileageRate());
			serviceProvider.setStatus(serviceProviderDto.getStatus());
			serviceProvider.setOrganizationId(serviceProviderDto.getOrganizationId());
			serviceProvider.setCreatedBy(serviceProviderDto.getCreatedBy());
			serviceProvider.setCreatedDate(serviceProviderDto.getCreatedDate().toString());
			serviceProvider.setUpdatedBy(serviceProviderDto.getUpdatedBy());
			serviceProvider.setUpdatedDate(serviceProviderDto.getUpdatedDate().toString());
			
		    sessionFactory.getCurrentSession().save(serviceProvider);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("saveServiceProvider failed", he);
		} catch (Exception e) {
		logger.error("saveServiceProvider failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getServiceProviderList(ServiceProviderDto serviceProviderDto) throws ApplicationException {
		try {
			List<ServiceProviderDto> ambulanceTypeMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_PROVIDER_LIST")
					.setParameter("organizationId", serviceProviderDto.getOrganizationId())
					.setFirstResult(serviceProviderDto.getOffset() != null ? serviceProviderDto.getOffset() : 0)
					.setMaxResults(serviceProviderDto.getNoOfRecordsPerPage() != null ? serviceProviderDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(ServiceProviderDto.class)).list();
			return new Response(SUCCESS, null, null, ambulanceTypeMasterList, null);

		} catch (HibernateException he) {
		logger.error("getServiceProviderList failed", he);
		} catch (Exception e) {
		logger.error("getServiceProviderList failed", e);
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getServiceProviderActiveList(ServiceProviderDto serviceProviderDto) throws ApplicationException {
		try {
			List<ServiceProviderDto> ambulanceTarrifMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_SERVICE_PROVIDER_LIST")
					.setParameter("organizationId", serviceProviderDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ServiceProvider.class)).list();
			return new Response(SUCCESS, null, null, ambulanceTarrifMasterList, null);

		} catch (HibernateException he) {
		logger.error("getServiceProviderActiveList failed", he);
		} catch (Exception e) {
		logger.error("getServiceProviderActiveList failed", e);
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getServiceProviderByID(ServiceProviderDto serviceProviderDto) throws ApplicationException {
		try {
			List<ServiceProviderDto> ambTarrifMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_PROVIDER_LIST_BY_ID")
					.setParameter("serviceProviderMasterId", serviceProviderDto.getServiceProviderMasterId())
					.setParameter("organizationId", serviceProviderDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ServiceProviderDto.class)).list();
			return new Response(SUCCESS, null, null, ambTarrifMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getServiceProviderByID failed", he);
		} catch (Exception e) {
		logger.error("getServiceProviderByID failed", e);
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSingleServiceProvider(ServiceProviderDto serviceProviderDto) throws ApplicationException {
		try {
			List<ServiceProviderDto> ambulanceTarrifMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SERVICE_PROVIDER_SINGLE_REC")
					.setParameter("organizationId", serviceProviderDto.getOrganizationId())
					.setParameter("serviceProviderMasterId", serviceProviderDto.getServiceProviderMasterId())
					.setFirstResult(serviceProviderDto.getOffset() != null ? serviceProviderDto.getOffset() : 0)
					.setMaxResults(serviceProviderDto.getNoOfRecordsPerPage() != null ? serviceProviderDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(ServiceProviderDto.class)).list();
			return new Response(SUCCESS, null, null, ambulanceTarrifMasterList, null);

		} catch (HibernateException he) {
		logger.error("getSingleServiceProvider failed", he);
		} catch (Exception e) {
		logger.error("getSingleServiceProvider failed", e);
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateServiceProvider(ServiceProviderDto serviceProviderDto) throws ApplicationException {
		try {
			ServiceProvider serviceProvider = findById(serviceProviderDto.getServiceProviderMasterId());
			serviceProvider.setServiceProviderName(serviceProviderDto.getServiceProviderName());
			serviceProvider.setServiceProviderAddress(serviceProviderDto.getServiceProviderAddress());
			serviceProvider.setServiceProviderCountry(serviceProviderDto.getServiceProviderCountry());
			serviceProvider.setServiceProviderState(serviceProviderDto.getServiceProviderState());
			serviceProvider.setServiceProviderDistrict(serviceProviderDto.getServiceProviderDistrict());
			serviceProvider.setServiceProviderCity(serviceProviderDto.getServiceProviderCity());
			serviceProvider.setServiceProviderArea(serviceProviderDto.getServiceProviderArea());
			serviceProvider.setServiceProviderZipCode(serviceProviderDto.getServiceProviderZipCode());
			serviceProvider.setServiceProviderAmbulanceType(serviceProviderDto.getServiceProviderAmbulanceType());
			serviceProvider.setServiceProviderPersonName(serviceProviderDto.getServiceProviderPersonName());
			serviceProvider.setServiceProviderPhoneNo(serviceProviderDto.getServiceProviderPhoneNo());
			serviceProvider.setServiceProviderCellNo(serviceProviderDto.getServiceProviderCellNo());
			serviceProvider.setServiceProviderEmailAddress(serviceProviderDto.getServiceProviderEmailAddress());
			serviceProvider.setServiceProviderContractFromDate(serviceProviderDto.getServiceProviderContractFromDate());
			serviceProvider.setServiceProviderContractToDate(serviceProviderDto.getServiceProviderContractToDate());
			serviceProvider.setServiceProviderFirstDistance(serviceProviderDto.getServiceProviderFirstDistance());
			serviceProvider.setServiceProviderFirstDistanceRate(serviceProviderDto.getServiceProviderFirstDistanceRate());
			serviceProvider.setServiceProviderAdditionalMileage(serviceProviderDto.getServiceProviderAdditionalMileage());
			serviceProvider.setServiceProviderAdditionalMileageRate(serviceProviderDto.getServiceProviderAdditionalMileageRate());
			serviceProvider.setOrganizationId(serviceProviderDto.getOrganizationId());
			serviceProvider.setUpdatedBy(serviceProviderDto.getUpdatedBy());
			serviceProvider.setUpdatedDate(serviceProviderDto.getUpdatedDate());
			sessionFactory.getCurrentSession().update(serviceProvider);
			return new Response<ServiceProviderDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
		logger.error("updateServiceProvider failed", he);
			return new Response<ServiceProviderDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("updateServiceProvider failed", e);
			return new Response<ServiceProviderDto, Integer>(ERROR, null, null, null, null);
		} finally {
			 ;
		}
	}

	@Override
	public Response changeServiceProviderStatus(ServiceProviderDto serviceProviderDto) throws ApplicationException {
		try {

			ServiceProvider serviceProvider = findById(serviceProviderDto.getServiceProviderMasterId());
			serviceProvider.setUpdatedBy(serviceProviderDto.getUpdatedBy());
			serviceProvider.setStatus(serviceProviderDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			serviceProvider.setUpdatedDate(serviceProviderDto.getUpdatedDate());
			sessionFactory.getCurrentSession().update(serviceProvider);
			return new Response<ServiceProviderDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
		logger.error("changeServiceProviderStatus failed", he);
			return new Response<ServiceProviderDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("changeServiceProviderStatus failed", e);
			return new Response<ServiceProviderDto, Integer>(ERROR, null, null, null, null);
		} finally {
			 ;
		}
	}

	

}

package com.param.global.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AmbulanceTarrifDto;
import com.param.global.dto.AmbulanceTypeDto;
import com.param.global.model.AmbulanceTarrif;
import com.param.global.model.AmbulanceType;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AmbulanceTarrifDaoImpl extends GenericDao<AmbulanceTarrif, Integer>
		implements IAmbulanceTarrifDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(AmbulanceTarrifDaoImpl.class);


	@Autowired
	private SessionFactory sessionFactory;

	public AmbulanceTarrifDaoImpl() {
		super(AmbulanceTarrif.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response getAmbulanceTarrifByName(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException {
		try {
			List<AmbulanceTarrifDto> tarrifMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_TARRIF_LIST_BY_NAME")
					.setParameter("ambulanceType", ambulanceTarrifDto.getAmbulanceType().toLowerCase())
					//.setParameter("ambulanceOutSrcVender", ambulanceTarrifDto.getAmbulanceOutSrcVender().toLowerCase())
					.setParameter("organizationId", ambulanceTarrifDto.getOrganizationId())
					.setParameter("tariffMasterId", ambulanceTarrifDto.getAmbulanceTarrifMasterId())
					.setResultTransformer(Transformers.aliasToBean(AmbulanceTarrifDto.class)).list();
			return new Response(SUCCESS, null, null, tarrifMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getAmbulanceTarrifByName failed", he);
		} catch (Exception e) {
		logger.error("getAmbulanceTarrifByName failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveAmbulanceTarrif(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException {
		try {
			
			AmbulanceTarrif ambulanceTarrif=new AmbulanceTarrif();
			ambulanceTarrif.setAmbulanceType(ambulanceTarrifDto.getAmbulanceType());
			ambulanceTarrif.setAmbulanceProvider(ambulanceTarrifDto.getAmbulanceProvider());
			ambulanceTarrif.setAmbulanceOutSrcVender(ambulanceTarrifDto.getAmbulanceOutSrcVender());
			ambulanceTarrif.setAmbulanceFirstDist(ambulanceTarrifDto.getAmbulanceFirstDist());
			ambulanceTarrif.setAmbulanceFirstDistRate(ambulanceTarrifDto.getAmbulanceFirstDistRate());
			ambulanceTarrif.setAmbulanceAddMileage(ambulanceTarrifDto.getAmbulanceAddMileage());
			ambulanceTarrif.setAmbulanceAddMileageRate(ambulanceTarrifDto.getAmbulanceAddMileageRate());
			ambulanceTarrif.setStatus(ambulanceTarrifDto.getStatus());
			ambulanceTarrif.setOrganizationId(ambulanceTarrifDto.getOrganizationId());
			ambulanceTarrif.setCreatedBy(ambulanceTarrifDto.getCreatedBy());
			ambulanceTarrif.setCreatedDate(ambulanceTarrifDto.getCreatedDate().toString());
			ambulanceTarrif.setUpdatedBy(ambulanceTarrifDto.getUpdatedBy());
			ambulanceTarrif.setUpdatedDate(ambulanceTarrifDto.getUpdatedDate().toString());
			
		    sessionFactory.getCurrentSession().save(ambulanceTarrif);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("saveAmbulanceTarrif failed", he);
		} catch (Exception e) {
		logger.error("saveAmbulanceTarrif failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAmbulanceTarrifList(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException {
		try {
			List<AmbulanceTarrifDto> ambulanceTypeMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_TARIFF_LIST")
					.setParameter("organizationId", ambulanceTarrifDto.getOrganizationId())
					.setFirstResult(ambulanceTarrifDto.getOffset() != null ? ambulanceTarrifDto.getOffset() : 0)
					.setMaxResults(ambulanceTarrifDto.getNoOfRecordsPerPage() != null ? ambulanceTarrifDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(AmbulanceTarrifDto.class)).list();
			
			Integer count = 0;
			List<AmbulanceTarrifDto> ambulanceTypeMasterList1 = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_TARIFF_LIST")
					.setParameter("organizationId", ambulanceTarrifDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AmbulanceTarrifDto.class)).list();
			
			  if(ambulanceTypeMasterList1 != null && ambulanceTypeMasterList1.size()>0) {
		    	  count = ambulanceTypeMasterList1.size();
		      }
			
			return new Response(SUCCESS, null, null, ambulanceTypeMasterList, count);

		} catch (HibernateException he) {
		logger.error("getAmbulanceTarrifList failed", he);
		} catch (Exception e) {
		logger.error("getAmbulanceTarrifList failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAmbulanceTarrifActiveList(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException {
		try {
			List<AmbulanceTarrifDto> ambulanceTarrifMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_AMBULANCE_TARRIF_LIST")
					.setParameter("organizationId", ambulanceTarrifDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AmbulanceTarrif.class)).list();
			return new Response(SUCCESS, null, null, ambulanceTarrifMasterList, null);

		} catch (HibernateException he) {
		logger.error("getAmbulanceTarrifActiveList failed", he);
		} catch (Exception e) {
		logger.error("getAmbulanceTarrifActiveList failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAmbulanceTarrifByID(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException {
		try {
			List<AmbulanceTarrifDto> ambTarrifMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_TARRIF_LIST_BY_ID")
					.setParameter("ambulanceTarrifMasterId", ambulanceTarrifDto.getAmbulanceTarrifMasterId())
					.setParameter("organizationId", ambulanceTarrifDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AmbulanceTarrifDto.class)).list();
			return new Response(SUCCESS, null, null, ambTarrifMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getAmbulanceTarrifByID failed", he);
		} catch (Exception e) {
		logger.error("getAmbulanceTarrifByID failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSingleAmbulanceTarrif(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException {
		try {
			List<AmbulanceTarrifDto> ambulanceTarrifMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_TARRIF_SINGLE_REC")
					.setParameter("organizationId", ambulanceTarrifDto.getOrganizationId())
					.setParameter("ambulanceTarrifMasterId", ambulanceTarrifDto.getAmbulanceTarrifMasterId())
					.setFirstResult(ambulanceTarrifDto.getOffset() != null ? ambulanceTarrifDto.getOffset() : 0)
					.setMaxResults(ambulanceTarrifDto.getNoOfRecordsPerPage() != null ? ambulanceTarrifDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(AmbulanceTarrifDto.class)).list();
			return new Response(SUCCESS, null, null, ambulanceTarrifMasterList, null);

		} catch (HibernateException he) {
		logger.error("getSingleAmbulanceTarrif failed", he);
		} catch (Exception e) {
		logger.error("getSingleAmbulanceTarrif failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateAmbulanceTarrif(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException {
		try {
			AmbulanceTarrif ambulanceTarrif = findById(ambulanceTarrifDto.getAmbulanceTarrifMasterId());
			ambulanceTarrif.setAmbulanceType(ambulanceTarrifDto.getAmbulanceType());
			ambulanceTarrif.setAmbulanceProvider(ambulanceTarrifDto.getAmbulanceProvider());
			ambulanceTarrif.setAmbulanceOutSrcVender(ambulanceTarrifDto.getAmbulanceOutSrcVender());
			ambulanceTarrif.setAmbulanceFirstDist(ambulanceTarrifDto.getAmbulanceFirstDist().toString());
			ambulanceTarrif.setAmbulanceFirstDistRate(ambulanceTarrifDto.getAmbulanceFirstDistRate().toString());
			ambulanceTarrif.setAmbulanceAddMileage(ambulanceTarrifDto.getAmbulanceAddMileage().toString());
			ambulanceTarrif.setAmbulanceAddMileageRate(ambulanceTarrifDto.getAmbulanceAddMileageRate().toString());
			ambulanceTarrif.setOrganizationId(ambulanceTarrifDto.getOrganizationId());
			ambulanceTarrif.setUpdatedBy(ambulanceTarrifDto.getUpdatedBy());
			ambulanceTarrif.setUpdatedDate(ambulanceTarrifDto.getUpdatedDate());
			sessionFactory.getCurrentSession().update(ambulanceTarrif);
			return new Response<AmbulanceTarrifDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
		logger.error("updateAmbulanceTarrif failed", he);
			return new Response<AmbulanceTarrifDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("updateAmbulanceTarrif failed", e);
			return new Response<AmbulanceTarrifDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}

	@Override
	public Response changeAmbulanceTarrifStatus(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException {
		try {

			AmbulanceTarrif ambulanceTarrif = findById(ambulanceTarrifDto.getAmbulanceTarrifMasterId());
			ambulanceTarrif.setUpdatedBy(ambulanceTarrifDto.getUpdatedBy());
			ambulanceTarrif.setStatus(ambulanceTarrifDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			ambulanceTarrif.setUpdatedDate(ambulanceTarrifDto.getUpdatedDate());
			sessionFactory.getCurrentSession().update(ambulanceTarrif);
			return new Response<AmbulanceTarrifDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
		logger.error("changeAmbulanceTarrifStatus failed", he);
			return new Response<AmbulanceTarrifDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("changeAmbulanceTarrifStatus failed", e);
			return new Response<AmbulanceTarrifDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}

	

}

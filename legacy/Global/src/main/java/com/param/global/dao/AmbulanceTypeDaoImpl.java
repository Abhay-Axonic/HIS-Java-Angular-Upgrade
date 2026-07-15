package com.param.global.dao;

import java.util.List;

import org.apache.log4j.Logger;
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

import com.param.bloodBank.model.BloodBankMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.AmbulanceTarrifDto;
import com.param.global.dto.AmbulanceTypeDto;
import com.param.global.model.AmbulanceTarrif;
import com.param.global.model.AmbulanceType;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AmbulanceTypeDaoImpl extends GenericDao<AmbulanceType, Integer>
		implements IAmbulanceTypeDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(AmbulanceTypeDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	public AmbulanceTypeDaoImpl() {
		super(AmbulanceType.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response getAmbulanceByName(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException {
		try {
			List<AmbulanceTypeDto> nationalityMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_LIST_BY_NAME")
					.setParameter("ambulanceType", ambulanceTypeDto.getAmbulanceType().toLowerCase())
					.setParameter("organizationId", ambulanceTypeDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AmbulanceType.class)).list();
			return new Response(SUCCESS, null, null, nationalityMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getAmbulanceByName failed", he);
		} catch (Exception e) {
			logger.error("getAmbulanceByName failed", e);
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addAmbulanceType(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException {
		try {
			
			AmbulanceType ambulanceType=new AmbulanceType();
			ambulanceType.setAmbulanceTypeId(ambulanceTypeDto.getAmbulanceTypeId());
			ambulanceType.setAmbulanceType(ambulanceTypeDto.getAmbulanceType());
			ambulanceType.setStatus(ambulanceTypeDto.getStatus());
			ambulanceType.setOrganizationId(ambulanceTypeDto.getOrganizationId());
			ambulanceType.setCreatedBy(ambulanceTypeDto.getCreatedBy());
			ambulanceType.setCreatedDate(ambulanceTypeDto.getCreatedDate().toString());
			ambulanceType.setUpdatedBy(ambulanceTypeDto.getUpdatedBy());
			ambulanceType.setUpdatedDate(ambulanceTypeDto.getUpdatedDate().toString());
			
		     sessionFactory.getCurrentSession().save(ambulanceType);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			logger.error("addAmbulanceType failed", he);
		} catch (Exception e) {
			logger.error("addAmbulanceType failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAmbulanceTypeList(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException {
		try {
			List<AmbulanceTypeDto> ambulanceTypeMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_LIST")
					.setParameter("organizationId", ambulanceTypeDto.getOrganizationId())
					.setFirstResult(ambulanceTypeDto.getOffset() != null ? ambulanceTypeDto.getOffset() : 0)
					.setMaxResults(ambulanceTypeDto.getNoOfRecordsPerPage() != null ? ambulanceTypeDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(AmbulanceType.class)).list();
			
			List<AmbulanceTypeDto> ambulanceTypeMasterList1 = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_LIST")
					.setParameter("organizationId", ambulanceTypeDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AmbulanceType.class)).list();
			Integer count = 0;
			if(ambulanceTypeMasterList1.size() > 0) {
				count = ambulanceTypeMasterList1.size();
			}
			return new Response(SUCCESS, null, null, ambulanceTypeMasterList, count);

		} catch (HibernateException he) {
			logger.error("getAmbulanceTypeList failed", he);
		} catch (Exception e) {
			logger.error("getAmbulanceTypeList failed", e);
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAmbulanceTypeActiveList(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException {
		try {
			List<AmbulanceTypeDto> ambulanceTypeMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_AMBULANCE_LIST")
					.setParameter("organizationId", ambulanceTypeDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AmbulanceType.class)).list();
			return new Response(SUCCESS, null, null, ambulanceTypeMasterList, null);

		} catch (HibernateException he) {
			logger.error("getAmbulanceTypeActiveList failed", he);
		} catch (Exception e) {
			logger.error("getAmbulanceTypeActiveList failed", e);
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSingleAmbulanceType(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException {
		try {
			List<AmbulanceTypeDto> ambulanceTypeMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_SINGLE_REC")
					.setParameter("organizationId", ambulanceTypeDto.getOrganizationId())
					.setParameter("ambulanceTypeMasterId", ambulanceTypeDto.getAmbulanceTypeMasterId())
					.setFirstResult(ambulanceTypeDto.getOffset() != null ? ambulanceTypeDto.getOffset() : 0)
					.setMaxResults(ambulanceTypeDto.getNoOfRecordsPerPage() != null ? ambulanceTypeDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(AmbulanceTypeDto.class)).list();
			return new Response(SUCCESS, null, null, ambulanceTypeMasterList, null);

		} catch (HibernateException he) {
			logger.error("getSingleAmbulanceType failed", he);
		} catch (Exception e) {
			logger.error("getSingleAmbulanceType failed", e);
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateAmbulanceType(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException {
		try {
			AmbulanceType ambulanceType = findById(ambulanceTypeDto.getAmbulanceTypeMasterId());
			ambulanceType.setAmbulanceType(ambulanceTypeDto.getAmbulanceType());
			ambulanceType.setAmbulanceTypeId(ambulanceTypeDto.getAmbulanceTypeId());
			ambulanceType.setUpdatedBy(ambulanceTypeDto.getUpdatedBy());
			ambulanceType.setUpdatedDate(ambulanceTypeDto.getUpdatedDate());
			sessionFactory.getCurrentSession().update(ambulanceType);
			return new Response<AmbulanceTypeDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
			logger.error("updateAmbulanceType failed", he);
			return new Response<AmbulanceTypeDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			logger.error("updateAmbulanceType failed", e);
			return new Response<AmbulanceTypeDto, Integer>(ERROR, null, null, null, null);
		} finally {
			 ;
		}
	}

	@Override
	public Response getAmbulanceByID(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException {
		try {
			List<AmbulanceTypeDto> nationalityMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_LIST_BY_ID")
					.setParameter("ambulanceTypeMasterId", ambulanceTypeDto.getAmbulanceTypeMasterId())
					.setParameter("organizationId", ambulanceTypeDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AmbulanceTypeDto.class)).list();
			return new Response(SUCCESS, null, null, nationalityMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getAmbulanceByID failed", he);
		} catch (Exception e) {
			logger.error("getAmbulanceByID failed", e);
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}
	@Override
	public Response changeAmbulanceStatus(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException {
		try {

			AmbulanceType ambulanceType = findById(ambulanceTypeDto.getAmbulanceTypeMasterId());
			ambulanceType.setUpdatedBy(ambulanceTypeDto.getUpdatedBy());
			ambulanceType.setStatus(ambulanceTypeDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			ambulanceType.setUpdatedDate(ambulanceTypeDto.getUpdatedDate());
			update(ambulanceType);
			return new Response<AmbulanceTypeDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
			logger.error("changeAmbulanceStatus failed", he);
			return new Response<AmbulanceTypeDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			logger.error("changeAmbulanceStatus failed", e);
			return new Response<AmbulanceTypeDto, Integer>(ERROR, null, null, null, null);
		} finally {
			 ;
		}
	}

	@Override
	public Response getAmbulanceTarrifByName(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException {
		try {
			List<AmbulanceTarrifDto> tarrifMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_TARRIF_LIST_BY_NAME")
					.setParameter("ambulanceType", ambulanceTarrifDto.getAmbulanceType().toLowerCase())
					.setParameter("organizationId", ambulanceTarrifDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AmbulanceTarrifDto.class)).list();
			return new Response(SUCCESS, null, null, tarrifMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getAmbulanceTarrifByName failed", he);
		} catch (Exception e) {
			logger.error("getAmbulanceTarrifByName failed", e);
		} finally {
			 ;
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
			List<AmbulanceTypeDto> ambulanceTypeMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_TARIFF_LIST")
					.setParameter("organizationId", ambulanceTarrifDto.getOrganizationId())
					.setFirstResult(ambulanceTarrifDto.getOffset() != null ? ambulanceTarrifDto.getOffset() : 0)
					.setMaxResults(ambulanceTarrifDto.getNoOfRecordsPerPage() != null ? ambulanceTarrifDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(AmbulanceTarrifDto.class)).list();
			return new Response(SUCCESS, null, null, ambulanceTypeMasterList, null);

		} catch (HibernateException he) {
			logger.error("getAmbulanceTarrifList failed", he);
		} catch (Exception e) {
			logger.error("getAmbulanceTarrifList failed", e);
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}



}

package com.param.global.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AmbulanceMasterDto;
import com.param.global.dto.AmbulanceTypeDto;
import com.param.global.model.AmbulanceMaster;
import com.param.global.model.AmbulanceType;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AmbulanceMasterDaoImpl extends GenericDao<AmbulanceMaster, Integer>
		implements IAmbulanceMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(AmbulanceMasterDaoImpl.class);


	@Autowired
	private SessionFactory sessionFactory;

	public AmbulanceMasterDaoImpl() {
		super(AmbulanceMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response saveAmbulanceMasterData(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException {
		try {
			
			AmbulanceMaster ambulanceMaster =new AmbulanceMaster();
			ambulanceMaster.setAmbulanceType(ambulanceMasterDto.getAmbulanceType());
			ambulanceMaster.setServiceProvider(ambulanceMasterDto.getServiceProvider());
			ambulanceMaster.setAmbulanceVehicalNo(ambulanceMasterDto.getAmbulanceVehicalNo());
			ambulanceMaster.setAvailableFacilities(ambulanceMasterDto.getAvailableFacilities());
			ambulanceMaster.setInhouse(ambulanceMasterDto.getInhouse());
			ambulanceMaster.setStatus(ambulanceMasterDto.getStatus());
			ambulanceMaster.setOrganizationId(ambulanceMasterDto.getOrganizationId());
			ambulanceMaster.setCreatedBy(ambulanceMasterDto.getCreatedBy());
			ambulanceMaster.setCreatedDate(ambulanceMasterDto.getCreatedDate().toString());
			ambulanceMaster.setUpdatedBy(ambulanceMasterDto.getUpdatedBy());
			ambulanceMaster.setUpdatedDate(ambulanceMasterDto.getUpdatedDate().toString());
			
		     sessionFactory.getCurrentSession().save(ambulanceMaster);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("saveAmbulanceMasterData failed", he);
		} catch (Exception e) {
		logger.error("saveAmbulanceMasterData failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	
	@Override
	public Response getAmbulanceMasterDataList(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException{
		
		try{
			List<AmbulanceMasterDto> ambulanceMasterDataList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_MASTER_DATA_LIST")
					.setParameter("organizationId", ambulanceMasterDto.getOrganizationId())
					.setFirstResult(ambulanceMasterDto.getOffset() != null ? ambulanceMasterDto.getOffset() : 0)
					.setMaxResults(ambulanceMasterDto.getNoOfRecordsPerPage() != null ? ambulanceMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(AmbulanceMasterDto.class)).list();
			return new Response(SUCCESS, null, null, ambulanceMasterDataList, null);

		} catch (HibernateException he) {
		logger.error("getAmbulanceMasterDataList failed", he);
		} catch (Exception e) {
		logger.error("getAmbulanceMasterDataList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAmbulanceMasterDataActiveList(AmbulanceMasterDto ambulanceMasterDto)
			throws ApplicationException {
		try {
			List<AmbulanceMasterDto> ambulanceTypeMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_AMBULANCE_MASTER_LIST")
					.setParameter("organizationId", ambulanceMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AmbulanceMaster.class)).list();
			return new Response(SUCCESS, null, null, ambulanceTypeMasterList, null);

		} catch (HibernateException he) {
		logger.error("getAmbulanceMasterDataActiveList failed", he);
		} catch (Exception e) {
		logger.error("getAmbulanceMasterDataActiveList failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSingleAmbulanceMasterData(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException {
		try {
			List<AmbulanceMasterDto> ambulanceTypeMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_MASTER_SINGLE_REC")
					.setParameter("organizationId", ambulanceMasterDto.getOrganizationId())
					.setParameter("ambulanceMasterId", ambulanceMasterDto.getAmbulanceMasterId())
					.setFirstResult(ambulanceMasterDto.getOffset() != null ? ambulanceMasterDto.getOffset() : 0)
					.setMaxResults(ambulanceMasterDto.getNoOfRecordsPerPage() != null ? ambulanceMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(AmbulanceMasterDto.class)).list();
			return new Response(SUCCESS, null, null, ambulanceTypeMasterList, null);

		} catch (HibernateException he) {
		logger.error("getSingleAmbulanceMasterData failed", he);
		} catch (Exception e) {
		logger.error("getSingleAmbulanceMasterData failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateAmbulanceMasterData(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException {
		try {
			AmbulanceMaster ambulanceMaster = findById(ambulanceMasterDto.getAmbulanceMasterId());
			ambulanceMaster.setAmbulanceType(ambulanceMasterDto.getAmbulanceType());
			ambulanceMaster.setUpdatedBy(ambulanceMasterDto.getUpdatedBy());
			ambulanceMaster.setUpdatedDate(ambulanceMasterDto.getUpdatedDate());
			ambulanceMaster.setServiceProvider(ambulanceMasterDto.getServiceProvider());
			ambulanceMaster.setAmbulanceVehicalNo(ambulanceMasterDto.getAmbulanceVehicalNo());
			ambulanceMaster.setAvailableFacilities(ambulanceMasterDto.getAvailableFacilities());
			ambulanceMaster.setInhouse(ambulanceMasterDto.getInhouse());
			sessionFactory.getCurrentSession().update(ambulanceMaster);
			return new Response<AmbulanceMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
		logger.error("updateAmbulanceMasterData failed", he);
			return new Response<AmbulanceMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("updateAmbulanceMasterData failed", e);
			return new Response<AmbulanceMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}

	@Override
	public Response changeAmbulanceMasterDataStatus(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException {
		try {

			AmbulanceMaster ambulanceMaster = findById(ambulanceMasterDto.getAmbulanceMasterId());
			ambulanceMaster.setUpdatedBy(ambulanceMasterDto.getUpdatedBy());
			ambulanceMaster.setStatus(ambulanceMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			ambulanceMaster.setUpdatedDate(ambulanceMasterDto.getUpdatedDate());
			update(ambulanceMaster);
			return new Response<AmbulanceMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
		logger.error("changeAmbulanceMasterDataStatus failed", he);
			return new Response<AmbulanceMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("changeAmbulanceMasterDataStatus failed", e);
			return new Response<AmbulanceMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}

	@Override
	public Response getAmbulanceMasterByName(AmbulanceMasterDto ambulanceMasterDto) throws ApplicationException {
		try {
			List<AmbulanceMasterDto> nationalityMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AMBULANCE_MASTER_LIST_BY_NAME")
					.setParameter("ambulanceNo", ambulanceMasterDto.getAmbulanceVehicalNo().toLowerCase())
					.setParameter("organizationId", ambulanceMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AmbulanceMasterDto.class)).list();
			return new Response(SUCCESS, null, null, nationalityMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getAmbulanceMasterByName failed", he);
		} catch (Exception e) {
		logger.error("getAmbulanceMasterByName failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	

}

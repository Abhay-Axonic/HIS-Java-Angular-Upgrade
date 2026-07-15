package com.param.bloodBank.dao;

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
import com.param.bloodBank.dto.CampMasterDto;
import com.param.bloodBank.model.CampMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class CampMasterDaoImpl extends GenericDao<CampMaster, Integer> implements ICampMasterDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(CampMasterDaoImpl.class);


	public CampMasterDaoImpl() {
		super(CampMaster.class);
	}
	
	@Autowired
	SessionFactory sessionFactory;
	
	
	@Override
	public Response saveCampMaster(CampMasterDto campMasterDto) throws ApplicationException {
		try {
			
			CampMaster mstr = new CampMaster();
			mstr.setAddress(campMasterDto.getAddress());
			mstr.setAreaId(campMasterDto.getAreaId());
			mstr.setCampCode(campMasterDto.getCampCode());
			mstr.setCampName(campMasterDto.getCampName());
			mstr.setCityId(campMasterDto.getCityId());
			mstr.setCountryId(campMasterDto.getCountryId());
			mstr.setCreatedBy(campMasterDto.getCreatedBy());
			mstr.setCreatedDate(GlobalCommonDateUtils.getDate(campMasterDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setDistrictId(campMasterDto.getDistrictId());
			mstr.setOrganizationId(campMasterDto.getOrganizationId());
			mstr.setPincode(campMasterDto.getPincode());
			mstr.setStateId(campMasterDto.getStateId());
			mstr.setStatus(campMasterDto.getStatus());
			mstr.setUnitId(campMasterDto.getUnitId());
			mstr.setUpdatedBy(campMasterDto.getUpdatedBy());
			mstr.setPatientTypeId(campMasterDto.getPatientTypeId());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(campMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			save(mstr);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
			} catch (HibernateException he) {
				logger.error("saveCampMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveCampMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}


	@Override
	public Response getCampMasterList(CampMasterDto campMasterDto) throws ApplicationException {
		try {
			List<CampMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CAMP_MASTER_LIST")
					.setParameter("organizationId", campMasterDto.getOrganizationId())
					.setParameter("unitId", campMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(CampMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getCampMasterList failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getCampMasterList failed", e);
				throw new ServiceException(e.getMessage());
			}
	}


	@Override
	public Response updateCampMasterStatus(CampMasterDto campMasterDto) throws ApplicationException {
		try {
			CampMaster mstr = findById(campMasterDto.getCampId());
			mstr.setUpdatedBy(campMasterDto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(campMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setStatus(campMasterDto.getStatus());
			mstr.setOrganizationId(campMasterDto.getOrganizationId());
			mstr.setUnitId(campMasterDto.getUnitId());
			update(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateCampMasterStatus failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateCampMasterStatus failed", e);
				throw new ServiceException(e.getMessage());
			}
	}


	@Override
	public Response updateCampMaster(CampMasterDto campMasterDto) throws ApplicationException {
		try {
			CampMaster mstr = findById(campMasterDto.getCampId());
			mstr.setAddress(campMasterDto.getAddress());
			mstr.setAreaId(campMasterDto.getAreaId());
			mstr.setCampCode(campMasterDto.getCampCode());
			mstr.setCampName(campMasterDto.getCampName());
			mstr.setCityId(campMasterDto.getCityId());
			mstr.setCountryId(campMasterDto.getCountryId());
			mstr.setDistrictId(campMasterDto.getDistrictId());
			//mstr.setOrganizationId(campMasterDto.getOrganizationId());
			mstr.setPincode(campMasterDto.getPincode());
			mstr.setStateId(campMasterDto.getStateId());
			//mstr.setUnitId(campMasterDto.getUnitId());
			mstr.setUpdatedBy(campMasterDto.getUpdatedBy());
			mstr.setPatientTypeId(campMasterDto.getPatientTypeId());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(campMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setStatus(campMasterDto.getStatus());
			update(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateCampMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateCampMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}


	@Override
	public Response getCampMasterListPaginationList(CampMasterDto campMasterDto) throws ApplicationException {
		try {
			List<CampMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CAMP_MASTER_LIST")
					.setParameter("organizationId", campMasterDto.getOrganizationId())
					.setParameter("unitId", campMasterDto.getUnitId())
					.setFirstResult(campMasterDto.getOffset() != null ? campMasterDto.getOffset()  : 0)
					.setMaxResults(campMasterDto.getNoOfRecordsPerPage() != null ? campMasterDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(CampMasterDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);

		} catch (Exception e) {
			logger.error("getCampMasterListPaginationList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getCampMasterListCount(CampMasterDto campMasterDto) throws ApplicationException {
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(CampMaster.class);
			crit.add(Restrictions.eq("organizationId", campMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", campMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
			logger.error("getCampMasterListCount failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getCampMasterListCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}

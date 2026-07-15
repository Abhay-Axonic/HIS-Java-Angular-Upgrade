package com.param.global.unit.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.CabinMaster;
import com.param.global.model.OutsideHospitalMaster;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.global.unit.dto.OutsideHospitalMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository

public class OutsideHospitalMasterDaoImpl extends GenericDao<OutsideHospitalMaster, Integer> implements IOutsideHospitalMasterDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(OutsideHospitalMasterDaoImpl.class);


	public OutsideHospitalMasterDaoImpl() {
		super(OutsideHospitalMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response saveOutsideHospitalMaster(OutsideHospitalMasterDto outsideHospitalMasterDto)
			throws ApplicationException {
		try {
			OutsideHospitalMaster outsideHospitalMaster=new OutsideHospitalMaster();
			outsideHospitalMaster.setOutsideHospitalCode(outsideHospitalMasterDto.getOutsideHospitalCode());
			outsideHospitalMaster.setOutsideHospitalDesc(outsideHospitalMasterDto.getOutsideHospitalDesc());
			outsideHospitalMaster.setOrganizationId(outsideHospitalMasterDto.getOrganizationId());
			outsideHospitalMaster.setUnitId(outsideHospitalMasterDto.getUnitId());
			outsideHospitalMaster.setStatus(outsideHospitalMasterDto.getStatus());
			outsideHospitalMaster.setCreatedBy(outsideHospitalMasterDto.getCreatedBy());
			outsideHospitalMaster.setCreatedDate(GlobalCommonDateUtils.getDate(outsideHospitalMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			outsideHospitalMaster.setUpdatedBy(outsideHospitalMasterDto.getUpdatedBy());
			outsideHospitalMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(outsideHospitalMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			save(outsideHospitalMaster);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveOutsideHospitalMaster failed", he);
		} catch (Exception e) {
		logger.error("saveOutsideHospitalMaster failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOutsideHospitalMasterByName(OutsideHospitalMasterDto outsideHospitalMasterDto)
			throws ApplicationException {
		try {
			
			List<CabinMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OUT_SIDE_HOSPITAL_MASTER_BY_NAME")
					.setParameter("outsideHospitalDesc", outsideHospitalMasterDto.getOutsideHospitalDesc().toLowerCase())
					.setParameter("unitId", outsideHospitalMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(OutsideHospitalMasterDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getOutsideHospitalMasterByName failed", he);
		} catch (Exception e) {
		logger.error("getOutsideHospitalMasterByName failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOutsideHospitalMasterByNameNotId(OutsideHospitalMasterDto outsideHospitalMasterDto)
			throws ApplicationException {
		try {
			List<CabinMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OUT_SIDE_HOSPITAL_MASTER_BY_NAME_NOT_ID")
					.setParameter("outsideHospitalId", outsideHospitalMasterDto.getOutsideHospitalId())
					.setParameter("unitId", outsideHospitalMasterDto.getUnitId())
					.setParameter("outsideHospitalDesc", outsideHospitalMasterDto.getOutsideHospitalDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(OutsideHospitalMasterDto.class)).list();
			 
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getOutsideHospitalMasterByNameNotId failed", he);
		} catch (Exception e) {
		logger.error("getOutsideHospitalMasterByNameNotId failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateOutsideHospitalMaster(OutsideHospitalMasterDto outsideHospitalMasterDto)
			throws ApplicationException {
		try {
			OutsideHospitalMaster outsideHospitalMaster=findById(outsideHospitalMasterDto.getOutsideHospitalId());
			outsideHospitalMaster.setOutsideHospitalCode(outsideHospitalMasterDto.getOutsideHospitalCode());
			outsideHospitalMaster.setOutsideHospitalDesc(outsideHospitalMasterDto.getOutsideHospitalDesc());
			outsideHospitalMaster.setOrganizationId(outsideHospitalMasterDto.getOrganizationId());
			outsideHospitalMaster.setUnitId(outsideHospitalMasterDto.getUnitId());
			outsideHospitalMaster.setUpdatedBy(outsideHospitalMasterDto.getUpdatedBy());
			outsideHospitalMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(outsideHospitalMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		 
		update(outsideHospitalMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	
	} catch (HibernateException he) {
	logger.error("updateOutsideHospitalMaster failed", he);
	} catch (Exception e) {
	logger.error("updateOutsideHospitalMaster failed", e);
	}
return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response updateOutsideHospitalMasterStatus(OutsideHospitalMasterDto outsideHospitalMasterDto)
			throws ApplicationException {
		try {
			OutsideHospitalMaster outsideHospitalMaster=findById(outsideHospitalMasterDto.getOutsideHospitalId());
			
			outsideHospitalMaster.setStatus(outsideHospitalMasterDto.getStatus());
			outsideHospitalMaster.setUpdatedBy(outsideHospitalMasterDto.getUpdatedBy());
			outsideHospitalMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(outsideHospitalMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(outsideHospitalMaster);
	
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	
	} catch (HibernateException he) {
	logger.error("updateOutsideHospitalMasterStatus failed", he);
	} catch (Exception e) {
	logger.error("updateOutsideHospitalMasterStatus failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_UPDATE, null, null);
}

	@Override
	public Response getOutsideHospitalMasterById(Integer outsideHospitalId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<CabinMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OUT_SIDE_HOSPITAL_MASTER_BY_ID")
					.setParameter("outsideHospitalId", outsideHospitalId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(OutsideHospitalMasterDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getOutsideHospitalMasterById failed", he);
		} catch (Exception e) {
		logger.error("getOutsideHospitalMasterById failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	

	@Override
	public Response getOutsideHospitalMasterCount(OutsideHospitalMasterDto outsideHospitalMasterDto)
			throws ApplicationException {
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(OutsideHospitalMaster.class);
			crit.add(Restrictions.eq("organizationId", outsideHospitalMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", outsideHospitalMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
		logger.error("getOutsideHospitalMasterCount failed", he);
		} catch (Exception e) {
		logger.error("getOutsideHospitalMasterCount failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveOutsideHospitalMaster(Integer organizationId) throws ApplicationException {
		try {
			List<CabinMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_OUT_SIDE_HOSPITAL_MASTER")
					.setParameter("organizationId", organizationId)
					.setResultTransformer(Transformers.aliasToBean(OutsideHospitalMasterDto.class)).list();	
	
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getActiveOutsideHospitalMaster failed", he);
		} catch (Exception e) {
		logger.error("getActiveOutsideHospitalMaster failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOutsideHospitalMasterList(OutsideHospitalMasterDto outsideHospitalMasterDto)
			throws ApplicationException {
		try {
			List<OutsideHospitalMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OUT_SIDE_HOSPITAL_MASTER_LIST")
					.setParameter("organizationId",  outsideHospitalMasterDto.getOrganizationId())
					.setFirstResult(outsideHospitalMasterDto.getOffset() != null ? outsideHospitalMasterDto.getOffset()  : 0)
					.setMaxResults(outsideHospitalMasterDto.getNoOfRecordsPerPage() != null ? outsideHospitalMasterDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(OutsideHospitalMasterDto.class)).list();
			List<OutsideHospitalMasterDto> list1=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OUT_SIDE_HOSPITAL_MASTER_LIST")
					.setParameter("organizationId",  outsideHospitalMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(OutsideHospitalMasterDto.class)).list();
			Integer count = 0;
              if(list1.size() > 0) {
            	  count = list1.size();
              }

		return new Response(SUCCESS, SUCCESS_CODE, null, list, count);
		
		} catch (HibernateException he) {
		logger.error("getOutsideHospitalMasterList failed", he);
		} catch (Exception e) {
		logger.error("getOutsideHospitalMasterList failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}

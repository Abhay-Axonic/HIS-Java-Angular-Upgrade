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

import com.param.bloodBank.dto.BloodTransfusionPrecheckMasterDto;
import com.param.bloodBank.model.BloodTransfusionPrecheckMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.CabinMaster;
import com.param.global.unit.dto.CabinMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class BloodTransfusionPrecheckMasterDaoImpl extends GenericDao<BloodTransfusionPrecheckMaster, Integer> implements IBloodTransfusionPrecheckMasterDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodTransfusionPrecheckMasterDaoImpl.class);


	public BloodTransfusionPrecheckMasterDaoImpl() {
		super(BloodTransfusionPrecheckMaster.class);
	}
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveBloodTransfusionPrecheckMaster(
			BloodTransfusionPrecheckMasterDto bloodTransfusionPrecheckMasterDto) throws ApplicationException {
try {
			
		BloodTransfusionPrecheckMasterDto dto = new BloodTransfusionPrecheckMasterDto();
		if(bloodTransfusionPrecheckMasterDto.getCode()!=null && bloodTransfusionPrecheckMasterDto.getDescription()!=null) {	
			dto = (BloodTransfusionPrecheckMasterDto) sessionFactory.getCurrentSession()
						.getNamedQuery("CHECK_FOR_UNIQUE_PRECHECK_MASTER")
						.setParameter("organizationId", bloodTransfusionPrecheckMasterDto.getOrganizationId())
						.setParameter("unitId", bloodTransfusionPrecheckMasterDto.getUnitId())
						.setParameter("code", bloodTransfusionPrecheckMasterDto.getCode().toLowerCase())
						.setParameter("description", bloodTransfusionPrecheckMasterDto.getDescription().toLowerCase())
						.setResultTransformer(Transformers.aliasToBean(BloodTransfusionPrecheckMasterDto.class)).uniqueResult();
		}
		if(dto!=null)
		{
			return new Response<>(ALREADY_EXIST, ALREADY_EXIST_CODE,ALREADY_EXIST, null, null);
		}
		BloodTransfusionPrecheckMaster mstr = new BloodTransfusionPrecheckMaster();
		mstr.setCode(bloodTransfusionPrecheckMasterDto.getCode());
		mstr.setDescription(bloodTransfusionPrecheckMasterDto.getDescription());
		mstr.setCreatedBy(bloodTransfusionPrecheckMasterDto.getCreatedBy());
		mstr.setCreatedDate(GlobalCommonDateUtils.getDate(bloodTransfusionPrecheckMasterDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
		mstr.setOrganizationId(bloodTransfusionPrecheckMasterDto.getOrganizationId());
		mstr.setStatus(bloodTransfusionPrecheckMasterDto.getStatus());
		mstr.setUnitId(bloodTransfusionPrecheckMasterDto.getUnitId());
		mstr.setUpdatedBy(bloodTransfusionPrecheckMasterDto.getUpdatedBy());
		mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodTransfusionPrecheckMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
		
		if(bloodTransfusionPrecheckMasterDto.getTransfusionPrecheckMasterId()!=null) {
			mstr.setTransfusionPrecheckMasterId(bloodTransfusionPrecheckMasterDto.getTransfusionPrecheckMasterId());
			update(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}else {
			save(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}
			} catch (HibernateException he) {
				logger.error("saveBloodTransfusionPrecheckMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveBloodTransfusionPrecheckMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateBloodTransfusionPrecheckMasterStatus(
			BloodTransfusionPrecheckMasterDto bloodTransfusionPrecheckMasterDto) throws ApplicationException {
		try {
			BloodTransfusionPrecheckMaster mstr = findById(bloodTransfusionPrecheckMasterDto.getTransfusionPrecheckMasterId());
			mstr.setStatus(bloodTransfusionPrecheckMasterDto.getStatus());
			mstr.setUpdatedBy(bloodTransfusionPrecheckMasterDto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodTransfusionPrecheckMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			update(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
			logger.error("updateBloodTransfusionPrecheckMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBloodTransfusionPrecheckMasterList(
			BloodTransfusionPrecheckMasterDto bloodTransfusionPrecheckMasterDto) throws ApplicationException {
		try {
			List<BloodTransfusionPrecheckMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PRECHECK_MASTER_LIST")
					.setParameter("organizationId", bloodTransfusionPrecheckMasterDto.getOrganizationId())
					.setParameter("unitId", bloodTransfusionPrecheckMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BloodTransfusionPrecheckMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			logger.error("getBloodTransfusionPrecheckMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveBloodTransfusionPrecheckMasterList(
			BloodTransfusionPrecheckMasterDto bloodTransfusionPrecheckMasterDto) throws ApplicationException {
		try {
			List<BloodTransfusionPrecheckMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PRECHECK_MASTER_ACTIVE_LIST")
					.setParameter("organizationId", bloodTransfusionPrecheckMasterDto.getOrganizationId())
					.setParameter("unitId", bloodTransfusionPrecheckMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BloodTransfusionPrecheckMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			logger.error("getActiveBloodTransfusionPrecheckMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBloodTransfusionPrecheckMasterListPagination(
			BloodTransfusionPrecheckMasterDto bloodTransfusionPrecheckMasterDto) throws ApplicationException {
		try {
			List<BloodTransfusionPrecheckMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PRECHECK_MASTER_LIST")
					.setParameter("organizationId", bloodTransfusionPrecheckMasterDto.getOrganizationId())
					.setParameter("unitId", bloodTransfusionPrecheckMasterDto.getUnitId())
					.setFirstResult(bloodTransfusionPrecheckMasterDto.getOffset() != null ? bloodTransfusionPrecheckMasterDto.getOffset()  : 0)
					.setMaxResults(bloodTransfusionPrecheckMasterDto.getNoOfRecordsPerPage() != null ? bloodTransfusionPrecheckMasterDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(BloodTransfusionPrecheckMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (Exception e) {
			logger.error("getBloodTransfusionPrecheckMasterListPagination failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBloodTransfusionPrecheckMasterListCount(
			BloodTransfusionPrecheckMasterDto bloodTransfusionPrecheckMasterDto) throws ApplicationException {
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(BloodTransfusionPrecheckMaster.class);
			crit.add(Restrictions.eq("organizationId", bloodTransfusionPrecheckMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", bloodTransfusionPrecheckMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
			logger.error("getBloodTransfusionPrecheckMasterListCount failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getBloodTransfusionPrecheckMasterListCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}

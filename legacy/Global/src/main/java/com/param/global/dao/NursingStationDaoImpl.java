package com.param.global.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.master.unit.model.NursingStationMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.NursingStationMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NursingStationDaoImpl extends GenericDao<NursingStationMaster, Integer> implements INursingStationDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(NursingStationDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;
	
	public NursingStationDaoImpl() {
		super(NursingStationMaster.class);
	}

	@Override
	public Response getListOfNursingStationMaster(NursingStationMasterDto nursingStationMasterDto)
			throws ApplicationException {
		try
		{
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<NursingStationMasterDto> listNursingStationMasterDto = statlessSession.getNamedQuery("GET_LIST_NURSING_STATION")
					.setParameter("organizationId", nursingStationMasterDto.getOrganizationId())
					.setParameter("unitId", nursingStationMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(NursingStationMasterDto.class)).list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listNursingStationMasterDto, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
			logger.error("getListOfNursingStationMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getListOfNursingStationMasterByClinicId(NursingStationMasterDto nursingStationMasterDto)throws ApplicationException {
		try
		{
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<NursingStationMasterDto> listNursingStationMasterDto = statlessSession.getNamedQuery("GET_NURSING_LIST_BY_CLINIC_ID")
					.setParameter("organizationId", nursingStationMasterDto.getOrganizationId())
					.setParameter("unitId", nursingStationMasterDto.getUnitId())
					.setParameter("clinicId", nursingStationMasterDto.getClinicId())
					.setResultTransformer(Transformers.aliasToBean(NursingStationMasterDto.class)).list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listNursingStationMasterDto, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
			logger.error("getListOfNursingStationMasterByClinicId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	@Override
	public Response saveNursingStationMaster(NursingStationMasterDto nursingStationMasterDto)
			throws ApplicationException {
		try {
			NursingStationMaster nuStationMaster=new NursingStationMaster();
			nuStationMaster.setNursingStationCode(nursingStationMasterDto.getNursingStationCode());
			nuStationMaster.setNursingStationDesc(nursingStationMasterDto.getNursingStationDesc());
			nuStationMaster.setOrganizationId(nursingStationMasterDto.getOrganizationId());
			nuStationMaster.setUnitId(nursingStationMasterDto.getUnitId());
			nuStationMaster.setStatus(nursingStationMasterDto.getStatus());
			nuStationMaster.setCreatedBy(nursingStationMasterDto.getCreatedBy());
			nuStationMaster.setStoreId(nursingStationMasterDto.getStoreId());
			nuStationMaster.setCreatedDate(GlobalCommonDateUtils.getDate(nursingStationMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			nuStationMaster.setUpdatedBy(nursingStationMasterDto.getCreatedBy());
			nuStationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(nursingStationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			save(nuStationMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("saveNursingStationMaster failed", he);
		} catch (Exception e) {
		logger.error("saveNursingStationMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingStationMasterById(int nursingId, int orgId,int unitId) throws ApplicationException {
		try {
			List<NursingStationMasterDto> listNursingStationMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NURSING_STATION_MASTER_BY_ID")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setParameter("nursingId", nursingId)
					.setResultTransformer(Transformers.aliasToBean(NursingStationMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listNursingStationMasterDto, null);
		} catch (HibernateException he) {
		logger.error("getNursingStationMasterById failed", he);
		} catch (Exception e) {
		logger.error("getNursingStationMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingStationMasterList(NursingStationMasterDto nursingStationMasterDto)
			throws ApplicationException {
		try {
			List<NursingStationMasterDto> listNursingStationMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NURSING_STATION_MASTER_LIST")
					.setParameter("orgId", nursingStationMasterDto.getOrganizationId())
					.setParameter("unitId", nursingStationMasterDto.getUnitId())
					.setFirstResult(nursingStationMasterDto.getOffset() != null ? nursingStationMasterDto.getOffset() : 0)
					.setMaxResults(nursingStationMasterDto.getNoOfRecordsPerPage() != null ? nursingStationMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(NursingStationMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listNursingStationMasterDto, null);
		} catch (HibernateException he) {
		logger.error("getNursingStationMasterList failed", he);
		} catch (Exception e) {
		logger.error("getNursingStationMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateNursingStationMaster(NursingStationMasterDto nursingStationMasterDto)
			throws ApplicationException {
		try {
			NursingStationMaster nuStationMaster=findById(nursingStationMasterDto.getNursingStationId());
			nuStationMaster.setNursingStationCode(nursingStationMasterDto.getNursingStationCode());
			nuStationMaster.setNursingStationDesc(nursingStationMasterDto.getNursingStationDesc());	
			nuStationMaster.setStoreId(nursingStationMasterDto.getStoreId());
			nuStationMaster.setUpdatedBy(nursingStationMasterDto.getCreatedBy());
			nuStationMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(nursingStationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(nuStationMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateNursingStationMaster failed", he);
		} catch (Exception e) {
		logger.error("updateNursingStationMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateNursingStationMasterStatus(NursingStationMasterDto nursingStationMasterDto)
			throws ApplicationException {
		try {
			NursingStationMaster nuStationMaster=findById(nursingStationMasterDto.getNursingStationId());
			nuStationMaster.setStatus(nursingStationMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			nuStationMaster.setUpdatedBy(nursingStationMasterDto.getUpdatedBy());
			nuStationMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(nursingStationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(nuStationMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
		logger.error("updateNursingStationMasterStatus failed", he);
		} catch (Exception e) {
		logger.error("updateNursingStationMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingStationMasterCount(NursingStationMasterDto nursingStationMasterDto)
			throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(NursingStationMaster.class); 
			c.add(Restrictions.eq("organizationId", nursingStationMasterDto.getOrganizationId()));
			c.add(Restrictions.eq("unitId", nursingStationMasterDto.getUnitId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
		logger.error("getNursingStationMasterCount failed", he);
		} catch (Exception e) {
		logger.error("getNursingStationMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingStationMasterByName(NursingStationMasterDto nursingStationMasterDto)
			throws ApplicationException {
		try {

			List<NursingStationMasterDto> listNursingStationMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NURSING_STATION_MASTER_BY_NAME")
					.setParameter("orgId", nursingStationMasterDto.getOrganizationId())
					.setParameter("unitId", nursingStationMasterDto.getUnitId())
					.setParameter("nursingDesc", nursingStationMasterDto.getNursingStationDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(NursingStationMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listNursingStationMasterDto, null);
		} catch (HibernateException he) {
		logger.error("getNursingStationMasterByName failed", he);
		} catch (Exception e) {
		logger.error("getNursingStationMasterByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingStationMasterByNameNotById(NursingStationMasterDto nursingStationMasterDto)
			throws ApplicationException {
		try {
			List<NursingStationMasterDto> listNursingStationMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NURSING_STATIONMASTER_BYNAME_NOTBYID")
					.setParameter("orgId", nursingStationMasterDto.getOrganizationId())
					.setParameter("nursingId", nursingStationMasterDto.getNursingStationId())
					.setParameter("nursingDesc", nursingStationMasterDto.getNursingStationDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(NursingStationMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listNursingStationMasterDto, null);
		} catch (HibernateException he) {
		logger.error("getNursingStationMasterByNameNotById failed", he);
		} catch (Exception e) {
		logger.error("getNursingStationMasterByNameNotById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
}

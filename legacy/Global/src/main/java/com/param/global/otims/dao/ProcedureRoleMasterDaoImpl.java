package com.param.global.otims.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.ProcedureRoleMasterDto;
import com.param.otims.model.ProcedureRoleMaster;

import in.param.common.dao.GenericDao;
import com.param.global.common.ServiceException;
import org.apache.log4j.Logger;

@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class ProcedureRoleMasterDaoImpl extends GenericDao<ProcedureRoleMaster, Integer> implements IProcedureRoleMasterDao, ICommonConstants{

	private static final Logger logger = Logger.getLogger(ProcedureRoleMasterDaoImpl.class);

	public ProcedureRoleMasterDaoImpl() {
		super(ProcedureRoleMaster.class);
	}

	@Override
	public Response saveProcedureRoleMaster(ProcedureRoleMasterDto procedureRoleMasterDto) throws ApplicationException {
		try{
			ProcedureRoleMaster procedureRoleMaster=new ProcedureRoleMaster();
			procedureRoleMaster.setProcedureRoleCode(procedureRoleMasterDto.getProcedureRoleCode());
			procedureRoleMaster.setProcedureRoleDesc(procedureRoleMasterDto.getProcedureRoleDesc());
			procedureRoleMaster.setProcedureRoleName(procedureRoleMasterDto.getProcedureRoleName());
			procedureRoleMaster.setOrgId(procedureRoleMasterDto.getOrgId());
			procedureRoleMaster.setUnitId(procedureRoleMasterDto.getUnitId());
			procedureRoleMaster.setCreatedBy(procedureRoleMasterDto.getCreatedBy());
			procedureRoleMaster.setCreatedDate(GlobalCommonDateUtils.getDate(procedureRoleMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			procedureRoleMaster.setUpdatedBy(procedureRoleMasterDto.getUpdatedBy());
			procedureRoleMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(procedureRoleMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			procedureRoleMaster.setStatus(procedureRoleMasterDto.getStatus());
			
			save(procedureRoleMaster);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		} catch (HibernateException he) {
			logger.error("saveProcedureRoleMaster failed", he);
		} catch (Exception e) {
			logger.error("saveProcedureRoleMaster failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateProcedureRoleMaster(ProcedureRoleMasterDto procedureRoleMasterDto)
			throws ApplicationException {
		try{
			ProcedureRoleMaster procedureRoleMaster=findById(procedureRoleMasterDto.getProcedureRoleId());
			procedureRoleMaster.setProcedureRoleCode(procedureRoleMasterDto.getProcedureRoleCode());
			procedureRoleMaster.setProcedureRoleDesc(procedureRoleMasterDto.getProcedureRoleDesc());
			procedureRoleMaster.setProcedureRoleName(procedureRoleMasterDto.getProcedureRoleName());
			procedureRoleMaster.setOrgId(procedureRoleMasterDto.getOrgId());
			procedureRoleMaster.setUnitId(procedureRoleMasterDto.getUnitId());
			procedureRoleMaster.setUpdatedBy(procedureRoleMasterDto.getUpdatedBy());
			procedureRoleMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(procedureRoleMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			
			update(procedureRoleMaster);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
			logger.error("updateProcedureRoleMaster failed", he);
		} catch (Exception e) {
			logger.error("updateProcedureRoleMaster failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response updateProcedureRoleMasterStatus(ProcedureRoleMasterDto procedureRoleMasterDto)
			throws ApplicationException {
		try{
			ProcedureRoleMaster procedureRoleMaster=findById(procedureRoleMasterDto.getProcedureRoleId());
			procedureRoleMaster.setUpdatedBy(procedureRoleMasterDto.getUpdatedBy());
			procedureRoleMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(procedureRoleMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			procedureRoleMaster.setStatus(procedureRoleMasterDto.getStatus());
			
			update(procedureRoleMaster);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
			logger.error("updateProcedureRoleMasterStatus failed", he);
		} catch (Exception e) {
			logger.error("updateProcedureRoleMasterStatus failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getProcedureRoleMasterList(ProcedureRoleMasterDto procedureRoleMasterDto)
			throws ApplicationException {
		try{
			List<ProcedureRoleMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PROCEDURE_ROLE_MASTER_LIST")
					.setParameter("orgId", procedureRoleMasterDto.getOrgId())
					.setParameter("unitId", procedureRoleMasterDto.getUnitId())
					.setFirstResult(procedureRoleMasterDto.getOffset() != null ? procedureRoleMasterDto.getOffset() : 0)
					.setMaxResults(procedureRoleMasterDto.getNoOfRecordsPerPage() != null ? procedureRoleMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(ProcedureRoleMasterDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getProcedureRoleMasterList failed", he);
		} catch (Exception e) {
			logger.error("getProcedureRoleMasterList failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getProcedureRoleMasterById(Integer procedureRoleId, Integer orgId, Integer unitId)
			throws ApplicationException {
		try{
			
			List<ProcedureRoleMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PROCEDURE_ROLE_MASTER_BY_ID")
					.setParameter("procedureRoleId", procedureRoleId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(ProcedureRoleMasterDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getProcedureRoleMasterById failed", he);
		} catch (Exception e) {
			logger.error("getProcedureRoleMasterById failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getProcedureRoleMasterCount(ProcedureRoleMasterDto procedureRoleMasterDto)
			throws ApplicationException {
		try{
			
			Session session = sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria(ProcedureRoleMaster.class);
			c.add(Restrictions.eq("orgId", procedureRoleMasterDto.getOrgId()));
			c.add(Restrictions.eq("unitId", procedureRoleMasterDto.getUnitId()));
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, count);

		} catch (HibernateException he) {
			logger.error("getProcedureRoleMasterCount failed", he);
		} catch (Exception e) {
			logger.error("getProcedureRoleMasterCount failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getActiveProcedureRoleMasterList(ProcedureRoleMasterDto procedureRoleMasterDto)
			throws ApplicationException {
		try{
			
			List<ProcedureRoleMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_PROCEDURE_ROLE_MASTER_LIST")
					.setParameter("orgId", procedureRoleMasterDto.getOrgId())
					.setParameter("unitId", procedureRoleMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ProcedureRoleMasterDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getActiveProcedureRoleMasterList failed", he);
		} catch (Exception e) {
			logger.error("getActiveProcedureRoleMasterList failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getProcedureRoleMasterByName(ProcedureRoleMasterDto procedureRoleMasterDto)
			throws ApplicationException {
		try{
			
			List<ProcedureRoleMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PROCEDURE_ROLE_MASTER_BY_NAME")
					.setParameter("procedureRoleName", procedureRoleMasterDto.getProcedureRoleName().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(ProcedureRoleMasterDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
			
		} catch (HibernateException he) {
		logger.error("getProcedureRoleMasterByName failed", he);
		} catch (Exception e) {
		logger.error("getProcedureRoleMasterByName failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@Override
	public Response getProcedureRoleMasterByNameNotId(ProcedureRoleMasterDto procedureRoleMasterDto)
			throws ApplicationException {
		try{
					
			List<ProcedureRoleMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PROCEDURE_ROLE_MASTER_BY_NAME_NOT_ID")
					.setParameter("procedureRoleId", procedureRoleMasterDto.getProcedureRoleId())
					.setParameter("procedureRoleName", procedureRoleMasterDto.getProcedureRoleName().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(ProcedureRoleMasterDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
					
				} catch (HibernateException he) {
				logger.error("getProcedureRoleMasterByNameNotId failed", he);
				} catch (Exception e) {
				logger.error("getProcedureRoleMasterByNameNotId failed", e);
				}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
}

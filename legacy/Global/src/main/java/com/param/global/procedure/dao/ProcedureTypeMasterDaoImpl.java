package com.param.global.procedure.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.procedure.dto.ProcedureTypeMasterDto;
import com.param.otims.model.ProcedureTypeMaster;

import in.param.common.dao.GenericDao;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class ProcedureTypeMasterDaoImpl extends GenericDao<ProcedureTypeMaster, Integer> implements IProcedureTypeMasterDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(ProcedureTypeMasterDaoImpl.class);


	public ProcedureTypeMasterDaoImpl() {
		super(ProcedureTypeMaster.class);
	}

	@Autowired
	SessionFactory sessionfactory;
	@Override
	public Response getProcedureTypeList(int orgId, int unitId) throws ApplicationException {
		try{
			List<ProcedureTypeMasterDto> listProcedureTypeMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_PROCEDURE_TYPE_LIST").setParameter("orgId", orgId).setParameter("unitId", unitId)
						.setResultTransformer(Transformers.aliasToBean(ProcedureTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listProcedureTypeMasterDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	
	@Override
	public Response saveProcedureTypeMaster(ProcedureTypeMasterDto procedureTypeMasterDto) throws ApplicationException {
		try{ 
			ProcedureTypeMaster procedureTypeMaster=new ProcedureTypeMaster();
			procedureTypeMaster.setProcedureTypeCode(procedureTypeMasterDto.getProcedureTypeCode());
			procedureTypeMaster.setProcedureTypeDesc(procedureTypeMasterDto.getProcedureTypeDesc());
			procedureTypeMaster.setProcedureTypeName(procedureTypeMasterDto.getProcedureTypeName());
			procedureTypeMaster.setOrgId(procedureTypeMasterDto.getOrgId());
			procedureTypeMaster.setUnitId(procedureTypeMasterDto.getUnitId());
			procedureTypeMaster.setCreatedBy(procedureTypeMasterDto.getCreatedBy());
			procedureTypeMaster.setCreatedDate(GlobalCommonDateUtils.getDate(procedureTypeMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			procedureTypeMaster.setUpdatedBy(procedureTypeMasterDto.getUpdatedBy());
			procedureTypeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(procedureTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			procedureTypeMaster.setStatus(procedureTypeMasterDto.getStatus());
			procedureTypeMaster=save(procedureTypeMaster);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, procedureTypeMaster);
			}catch (HibernateException he) {
			logger.error("saveProcedureTypeMaster failed", he);
			}catch (Exception e) {
			logger.error("saveProcedureTypeMaster failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	

	@Override
	public Response updateProcedureTypeMaster(ProcedureTypeMasterDto procedureTypeMasterDto) throws ApplicationException {
			
		try{
			ProcedureTypeMaster procedureTypeMaster=findById(procedureTypeMasterDto.getProcedureTypeId());
			procedureTypeMaster.setProcedureTypeCode(procedureTypeMasterDto.getProcedureTypeCode());
			procedureTypeMaster.setProcedureTypeDesc(procedureTypeMasterDto.getProcedureTypeDesc());
			procedureTypeMaster.setProcedureTypeName(procedureTypeMasterDto.getProcedureTypeName());
			procedureTypeMaster.setOrgId(procedureTypeMasterDto.getOrgId());
			procedureTypeMaster.setUnitId(procedureTypeMasterDto.getUnitId());
			procedureTypeMaster.setUpdatedBy(procedureTypeMasterDto.getUpdatedBy());
			procedureTypeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(procedureTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(procedureTypeMaster);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}catch (HibernateException he) {
			logger.error("updateProcedureTypeMaster failed", he);
			}catch (Exception e) {
			logger.error("updateProcedureTypeMaster failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	

	@Override
	public Response updateProcedureTypeMasterStatus(ProcedureTypeMasterDto procedureTypeMasterDto) throws ApplicationException {
			
		try{
			ProcedureTypeMaster procedureTypeMaster=findById(procedureTypeMasterDto.getProcedureTypeId());
			procedureTypeMaster.setUpdatedBy(procedureTypeMasterDto.getUpdatedBy());
			procedureTypeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(procedureTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			procedureTypeMaster.setStatus(procedureTypeMasterDto.getStatus());
			update(procedureTypeMaster);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}catch (HibernateException he) {
			logger.error("updateProcedureTypeMasterStatus failed", he);
			}catch (Exception e) {
			logger.error("updateProcedureTypeMasterStatus failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	
	
	@Override
	public Response getListProcedureTypeMaster(ProcedureTypeMasterDto procedureTypeMasterDto) throws ApplicationException {
			
		try{
			List<ProcedureTypeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_PROCEDURE_TYPE_MASTER")
					.setParameter("orgId", procedureTypeMasterDto.getOrgId())
					.setParameter("unitId",procedureTypeMasterDto.getUnitId())
					.setFirstResult(procedureTypeMasterDto.getOffset() != null ? procedureTypeMasterDto.getOffset() : 0)
					.setMaxResults(procedureTypeMasterDto.getNoOfRecordsPerPage() != null ? procedureTypeMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(ProcedureTypeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			}catch (HibernateException he) {
			logger.error("getListProcedureTypeMaster failed", he);
			}catch (Exception e) {
			logger.error("getListProcedureTypeMaster failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	
	@Override
	public Response getProcedureTypeMasterById(Integer procedureTypeId, Integer orgId, Integer unitId) throws ApplicationException {
			
		try{
			List<ProcedureTypeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PROCEDURE_TYPE_MASTER_BY_ID")
					.setParameter("procedureTypeId", procedureTypeId)
					.setParameter("orgId", orgId)
					.setParameter("unitId",unitId)
					.setResultTransformer(Transformers.aliasToBean(ProcedureTypeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			}catch (HibernateException he) {
			logger.error("getProcedureTypeMasterById failed", he);
			}catch (Exception e) {
			logger.error("getProcedureTypeMasterById failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	
	
	@Override
	public Response getProcedureTypeMasterCount(ProcedureTypeMasterDto procedureTypeMasterDto) throws ApplicationException {
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(ProcedureTypeMaster.class);
			crit.add(Restrictions.eq("orgId", procedureTypeMasterDto.getOrgId()));
			crit.add(Restrictions.eq("unitId", procedureTypeMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
			}catch (HibernateException he) {
			logger.error("getProcedureTypeMasterCount failed", he);
			}catch (Exception e) {
			logger.error("getProcedureTypeMasterCount failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getProcedureTypeMasterByName(ProcedureTypeMasterDto procedureTypeMasterDto) throws ApplicationException {
			
		try{
			List<ProcedureTypeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PROCEDURE_TYPE_MASTER_BY_NAME")
					.setParameter("procedureTypeName",procedureTypeMasterDto.getProcedureTypeName().toLowerCase())
					.setParameter("unitId", procedureTypeMasterDto.getUnitId())
					.setParameter("orgId", procedureTypeMasterDto.getOrgId())
					.setResultTransformer(Transformers.aliasToBean(ProcedureTypeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			}catch (HibernateException he) {
			logger.error("getProcedureTypeMasterByName failed", he);
			}catch (Exception e) {
			logger.error("getProcedureTypeMasterByName failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getProcedureTypeMasterByNameNotId(ProcedureTypeMasterDto procedureTypeMasterDto) throws ApplicationException {
		
		try{
			List<ProcedureTypeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PROCEDURE_TYPE_MASTER_BY_NAME_NOT_ID")
					.setParameter("procedureTypeId", procedureTypeMasterDto.getProcedureTypeId())
					.setParameter("orgId", procedureTypeMasterDto.getOrgId())
					.setParameter("unitId",procedureTypeMasterDto.getUnitId())
					.setParameter("procedureTypeName",procedureTypeMasterDto.getProcedureTypeName().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(ProcedureTypeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			}catch (HibernateException he) {
			logger.error("getProcedureTypeMasterByNameNotId failed", he);
			}catch (Exception e) {
			logger.error("getProcedureTypeMasterByNameNotId failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

}

package com.param.global.otims.dao;

import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.SubSequentDetailsDto;
import com.param.global.otims.dto.SubSequentProcedureConfigDto;
import com.param.otims.model.SubSequentProcedureConfig;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class SubSequentProcedureConfigDaoImpl extends GenericDao<SubSequentProcedureConfig, Integer>implements ISubSequentProcedureConfigDao, ICommonConstants{

	private static final Logger logger = Logger.getLogger(SubSequentProcedureConfigDaoImpl.class);

	public SubSequentProcedureConfigDaoImpl() {
		super(SubSequentProcedureConfig.class);

	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveSubSequentProcedureConfig(SubSequentProcedureConfigDto subSequentProcedureConfigDto) throws ApplicationException {
		try{
			
				SubSequentProcedureConfig subSequentProcedure=new SubSequentProcedureConfig();
				subSequentProcedure.setSpecialityId(subSequentProcedureConfigDto.getSpecialityId());
				subSequentProcedure.setOrgId(subSequentProcedureConfigDto.getOrgId());
				subSequentProcedure.setUnitId(subSequentProcedureConfigDto.getUnitId());
				subSequentProcedure.setCreatedBy(subSequentProcedureConfigDto.getCreatedBy());
				subSequentProcedure.setCreatedDate(GlobalCommonDateUtils.getDate(subSequentProcedureConfigDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				subSequentProcedure.setUpdatedBy(subSequentProcedureConfigDto.getUpdatedBy());
				subSequentProcedure.setUpdatedDate(GlobalCommonDateUtils.getDate(subSequentProcedureConfigDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				subSequentProcedure.setStatus(subSequentProcedureConfigDto.getStatus());
				if(subSequentProcedureConfigDto.getSubSequentProcConfigId()!=null) {
					subSequentProcedure.setSubSequentProcConfigId(subSequentProcedureConfigDto.getSubSequentProcConfigId());
					subSequentProcedure = update(subSequentProcedure);
				}else {
					subSequentProcedure = save(subSequentProcedure);
				}
				
				
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, subSequentProcedure);

		}catch (HibernateException he) {
			logger.error("saveSubSequentProcedureConfig failed", he);
		}catch (Exception e) {
			logger.error("saveSubSequentProcedureConfig failed", e);
		}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateSubSequentProcedureConfig(SubSequentProcedureConfigDto subSequentProcedureConfigDto) throws ApplicationException {
			
		try{
				SubSequentProcedureConfig subSequentProcedure=findById(subSequentProcedureConfigDto.getSubSequentProcConfigId());
				subSequentProcedure.setSpecialityId(subSequentProcedureConfigDto.getSpecialityId());
				subSequentProcedure.setOrgId(subSequentProcedureConfigDto.getOrgId());
				subSequentProcedure.setUnitId(subSequentProcedureConfigDto.getUnitId());
				subSequentProcedure.setUpdatedBy(subSequentProcedureConfigDto.getUpdatedBy());
				subSequentProcedure.setUpdatedDate(GlobalCommonDateUtils.getDate(subSequentProcedureConfigDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			
				update(subSequentProcedure);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		}catch (HibernateException he) {
			logger.error("updateSubSequentProcedureConfig failed", he);
		}catch (Exception e) {
			logger.error("updateSubSequentProcedureConfig failed", e);
		}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateSubSequentProcedureConfigStatus(SubSequentProcedureConfigDto subSequentProcedureConfigDto) throws ApplicationException {
			
		try{
			
				SubSequentProcedureConfig subSequentProcedure=findById(subSequentProcedureConfigDto.getSubSequentProcConfigId());			
				subSequentProcedure.setUpdatedBy(subSequentProcedureConfigDto.getUpdatedBy());
				subSequentProcedure.setUpdatedDate(GlobalCommonDateUtils.getDate(subSequentProcedureConfigDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				subSequentProcedure.setStatus(subSequentProcedureConfigDto.getStatus());
				update(subSequentProcedure);
				
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		}catch (HibernateException he) {
			logger.error("updateSubSequentProcedureConfigStatus failed", he);
		}catch (Exception e) {
			logger.error("updateSubSequentProcedureConfigStatus failed", e);
		}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSubSequentProcedureConfigList(SubSequentProcedureConfigDto subSequentProcedureConfigDto) throws ApplicationException {
			
		try{
			
				List<SubSequentProcedureConfigDto> list=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_SUBSEQUENT_PROCEDURE_CONFIG_LIST")
						.setParameter("orgId",subSequentProcedureConfigDto.getOrgId())
						.setParameter("unitId",subSequentProcedureConfigDto.getUnitId())
						.setFirstResult(subSequentProcedureConfigDto.getOffset() != null ? subSequentProcedureConfigDto.getOffset() : 0)
						.setMaxResults(subSequentProcedureConfigDto.getNoOfRecordsPerPage() != null ? subSequentProcedureConfigDto.getNoOfRecordsPerPage() : 10)
						.setResultTransformer(Transformers.aliasToBean(SubSequentProcedureConfigDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		}catch (HibernateException he) {
			logger.error("getSubSequentProcedureConfigList failed", he);
		}catch (Exception e) {
			logger.error("getSubSequentProcedureConfigList failed", e);
		}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getSubSequentProcedureConfigById(Integer subSequentProcConfigId, Integer orgId, Integer unitId) throws ApplicationException {
			
		try{
				List<SubSequentProcedureConfigDto> list=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_SUBSEQUENT_PROCEDURE_CONFIG_BY_ID")
						.setParameter("subSequentProcConfigId", subSequentProcConfigId)
						.setParameter("orgId", orgId)
						.setParameter("unitId", unitId)
						.setResultTransformer(Transformers.aliasToBean(SubSequentProcedureConfigDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		}catch (HibernateException he) {
			logger.error("getSubSequentProcedureConfigById failed", he);
		}catch (Exception e) {
			logger.error("getSubSequentProcedureConfigById failed", e);
		}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getActiveSubSequentProcedureConfigList(SubSequentProcedureConfigDto subSequentProcedureConfigDto) throws ApplicationException {
			
		try{
				List<SubSequentProcedureConfigDto> list=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_ACTIVE_SUBSEQUENT_PROCEDURE_CONFIG_LIST")
						.setParameter("orgId", subSequentProcedureConfigDto.getOrgId())
						.setResultTransformer(Transformers.aliasToBean(SubSequentProcedureConfigDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		}catch (HibernateException he) {
			logger.error("getActiveSubSequentProcedureConfigList failed", he);
		}catch (Exception e) {
			logger.error("getActiveSubSequentProcedureConfigList failed", e);
		}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getSubSequentProcedureConfigCount(SubSequentProcedureConfigDto subSequentProcedureConfigDto) throws ApplicationException {
			
		try{
				Session session = sessionFactory.getCurrentSession();
				Criteria crit = session.createCriteria(SubSequentProcedureConfig.class);
				crit.add(Restrictions.eq("orgId", subSequentProcedureConfigDto.getOrgId()));
				crit.add(Restrictions.eq("unitId", subSequentProcedureConfigDto.getUnitId()));
				crit.setProjection(Projections.rowCount());
				Long count = (Long) crit.uniqueResult();
				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);

		}catch (HibernateException he) {
			logger.error("getSubSequentProcedureConfigCount failed", he);
		}catch (Exception e) {
			logger.error("getSubSequentProcedureConfigCount failed", e);
		}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getSubSequentProcedureConfigByIdNotName(SubSequentProcedureConfigDto subSequentProcedureConfigDto) throws ApplicationException {
			
		try{
			if(subSequentProcedureConfigDto.getSubSequentProcConfigId() !=null) {
				List<SubSequentProcedureConfigDto> list=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_SUBSEQUENT_PROCEDURE_CONFIG_BY_PRIMID")
						.setParameter("specialityId", subSequentProcedureConfigDto.getSpecialityId())
						.setParameter("PrimaryKey", subSequentProcedureConfigDto.getSubSequentProcConfigId() )
						.setParameter("orgId", subSequentProcedureConfigDto.getOrgId())
						.setParameter("unitId", subSequentProcedureConfigDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(SubSequentProcedureConfigDto.class)).list();
						
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);	
			}else {
				List<SubSequentProcedureConfigDto> list=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_SUBSEQUENT_PROCEDURE_CONFIG_BY_ID_NOT_NAME")
						.setParameter("specialityId", subSequentProcedureConfigDto.getSpecialityId())
						.setParameter("orgId", subSequentProcedureConfigDto.getOrgId())
						.setParameter("unitId", subSequentProcedureConfigDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(SubSequentProcedureConfigDto.class)).list();
						
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);	
			}

		}catch (HibernateException he) {
			logger.error("getSubSequentProcedureConfigByIdNotName failed", he);
		}catch (Exception e) {
			logger.error("getSubSequentProcedureConfigByIdNotName failed", e);
		}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSubSequentProcedureConfigByOrgUnitStatus(int orgId, int unitId, char status)throws ApplicationException {
		try{
			SubSequentProcedureConfigDto dto = (SubSequentProcedureConfigDto)sessionFactory.getCurrentSession().getNamedQuery("GET_SUBSEQUENT_PROCEDURE_CONFIG_BY_ORG_UNIT_STATUS").setParameter("orgId", orgId).setParameter("unitId", unitId)
												.setCharacter("status", status).setResultTransformer(Transformers.aliasToBean(SubSequentProcedureConfigDto.class)).uniqueResult();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getSubSequentDetailsListById(SubSequentProcedureConfigDto subSequentProcedureConfigDto)
			throws ApplicationException {
		try{
			List<SubSequentDetailsDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SUBSEQUENT_DETAILS_LIST_BY_ID")
					.setParameter("subSequentProcConfigId", subSequentProcedureConfigDto.getSubSequentProcConfigId())
					.setParameter("orgId", subSequentProcedureConfigDto.getOrgId())
					.setParameter("unitId", subSequentProcedureConfigDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(SubSequentDetailsDto.class)).list();
					
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	}catch (HibernateException he) {
		logger.error("getSubSequentDetailsListById failed", he);
	}catch (Exception e) {
		logger.error("getSubSequentDetailsListById failed", e);
	}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSubSequentDetailsListBySpecialityId(SubSequentProcedureConfigDto subSequentProcedureConfigDto)
			throws ApplicationException {
		try{
			List<SubSequentDetailsDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SUBSEQUENT_DETAILS_LIST_BY_SPECIALITY_ID")
					.setParameter("specialityId", subSequentProcedureConfigDto.getSpecialityId())
					.setParameter("orgId", subSequentProcedureConfigDto.getOrgId())
					.setParameter("unitId", subSequentProcedureConfigDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(SubSequentDetailsDto.class)).list();
					
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	}catch (HibernateException he) {
		logger.error("getSubSequentDetailsListBySpecialityId failed", he);
	}catch (Exception e) {
		logger.error("getSubSequentDetailsListBySpecialityId failed", e);
	}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSubSequentDetailsAndInfoListBySpecialityId(
			SubSequentProcedureConfigDto subSequentProcedureConfigDto) throws ApplicationException {
		try{
			List<SubSequentDetailsDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SUBSEQUENT_DETAILS_LIST_BY_SPECIALITY_ID")
					.setParameter("specialityId", subSequentProcedureConfigDto.getSpecialityId())
					.setParameter("orgId", subSequentProcedureConfigDto.getOrgId())
					.setParameter("unitId", subSequentProcedureConfigDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(SubSequentDetailsDto.class)).list();
			
			if(list == null || list.size() == 0) {
				 list=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_SUBSEQUENT_DETAILS_LIST_BY_SPECIALITY_ID")
						.setParameter("specialityId", 0)
						.setParameter("orgId", subSequentProcedureConfigDto.getOrgId())
						.setParameter("unitId", subSequentProcedureConfigDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(SubSequentDetailsDto.class)).list();
			}
			
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

	}catch (HibernateException he) {
		logger.error("getSubSequentDetailsAndInfoListBySpecialityId failed", he);
	}catch (Exception e) {
		logger.error("getSubSequentDetailsAndInfoListBySpecialityId failed", e);
	}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}

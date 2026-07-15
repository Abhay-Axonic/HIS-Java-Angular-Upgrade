package com.param.global.unit.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;

import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.access.model.ActionMasterGlobal;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.ActionMasterDto;



import in.param.common.dao.GenericDao;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class ActionMasterGlobalDaoImpl extends GenericDao<ActionMasterGlobal, Integer> implements IActionMasterDaoGlobal, ICommonConstants{
	final static Logger logger = Logger.getLogger(ActionMasterGlobalDaoImpl.class);


	public ActionMasterGlobalDaoImpl() {
		super(ActionMasterGlobal.class);
		}
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveActionMaster(ActionMasterDto actionMasterDto) throws ApplicationException {
		try {
			
		
				ActionMasterGlobal actionMaster=new ActionMasterGlobal();
				actionMaster.setActionName(actionMasterDto.getActionName());
				actionMaster.setActionIcon(actionMasterDto.getActionIcon());
				actionMaster.setActionLink(actionMasterDto.getActionLink());				
				actionMaster.setParentId(actionMasterDto.getParentId());
				actionMaster.setCreatedBy(actionMasterDto.getCreatedBy());
				actionMaster.setCreatedDate(GlobalCommonDateUtils.getDate(actionMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				actionMaster.setStatus(actionMasterDto.getStatus());
				actionMaster.setModuleType(1);
				 
				save(actionMaster);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
		} catch (HibernateException he) {
		logger.error("saveActionMaster failed", he);
		} catch (Exception e) {
		logger.error("saveActionMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateActionMaster(ActionMasterDto actionMasterDto) throws ApplicationException {
		try {
				ActionMasterGlobal actionMaster=findById(actionMasterDto.getActionId());
				actionMaster.setActionName(actionMasterDto.getActionName());
				actionMaster.setActionIcon(actionMasterDto.getActionIcon());
				actionMaster.setActionLink(actionMasterDto.getActionLink());
				actionMaster.setParentId(actionMasterDto.getParentId());
				actionMaster.setModuleType(1);
				update(actionMaster);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			
		} catch (HibernateException he) {
		logger.error("updateActionMaster failed", he);
		} catch (Exception e) {
		logger.error("updateActionMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateActionMasterStatus(ActionMasterDto actionMasterDto) throws ApplicationException {
		try {
				ActionMasterGlobal actionMaster=findById(actionMasterDto.getActionId());
				actionMaster.setStatus(actionMasterDto.getStatus());
				
				update(actionMaster);			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			
		} catch (HibernateException he) {
		logger.error("updateActionMasterStatus failed", he);
		} catch (Exception e) {
		logger.error("updateActionMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActionMasterList(Integer offset, Integer noOfRecordsPerPage) throws ApplicationException {
		try {
				List<ActionMasterDto> list=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_ACTION_MASTER_LIST")
						.setFirstResult(offset != null ? offset : 0)
						.setMaxResults(noOfRecordsPerPage != null ? noOfRecordsPerPage : 10)
						.setResultTransformer(Transformers.aliasToBean(ActionMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
		} catch (HibernateException he) {
		logger.error("getActionMasterList failed", he);
		} catch (Exception e) {
		logger.error("getActionMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActionMasterById(Integer actionId) throws ApplicationException {
		try {
				List<ActionMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTION_MASTER_BY_ID")
					.setParameter("actionId", actionId)
					.setResultTransformer(Transformers.aliasToBean(ActionMasterDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
		} catch (HibernateException he) {
		logger.error("getActionMasterById failed", he);
		} catch (Exception e) {
		logger.error("getActionMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActionMasterCount() throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(ActionMasterGlobal.class);
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
			
		} catch (HibernateException he) {
		logger.error("getActionMasterCount failed", he);
		} catch (Exception e) {
		logger.error("getActionMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveActionMaster() throws ApplicationException {
		try {
				List<ActionMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_ACTION_MASTER")
					.setResultTransformer(Transformers.aliasToBean(ActionMasterDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
		} catch (HibernateException he) {
		logger.error("getActiveActionMaster failed", he);
		} catch (Exception e) {
		logger.error("getActiveActionMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActionMasterByName(ActionMasterDto actionMasterDto) throws ApplicationException {
		try {
				List<ActionMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTION_MASTER_BY_NAME")
					.setParameter("parentId", actionMasterDto.getParentId())
					.setParameter("actionName", actionMasterDto.getActionName().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(ActionMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
		} catch (HibernateException he) {
		logger.error("getActionMasterByName failed", he);
		} catch (Exception e) {
		logger.error("getActionMasterByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActionMasterByNameNotId(ActionMasterDto actionMasterDto) throws ApplicationException {
		try {
				List<ActionMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTION_MASTER_BY_NAME_NOT_ID")
					.setParameter("actionId", actionMasterDto.getActionId())
					.setParameter("parentId", actionMasterDto.getParentId())
					.setParameter("actionName", actionMasterDto.getActionName().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(ActionMasterDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
		} catch (HibernateException he) {
		logger.error("getActionMasterByNameNotId failed", he);
		} catch (Exception e) {
		logger.error("getActionMasterByNameNotId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActionId(ActionMasterDto actionMasterDto) throws ApplicationException {
		try {
			Integer id=null;

			if(actionMasterDto.getGrandParentActionName()!=null && actionMasterDto.getGrandParentActionName()!=""){
				ActionMasterDto masterDto =(ActionMasterDto)sessionFactory.getCurrentSession()
						.getNamedQuery("GET_PARENT_ACTION_NAME")
						.setParameter("actionName", actionMasterDto.getParentActioName().toLowerCase())
						.setParameter("grandParentActionName", actionMasterDto.getGrandParentActionName().toLowerCase())
						.setParameter("parentId", actionMasterDto.getParentId())

						.setResultTransformer(Transformers.aliasToBean(ActionMasterDto.class)).uniqueResult();
					  id=masterDto.getActionId();
				
			}else{
				ActionMasterDto masterDto =(ActionMasterDto)sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ACTION_ID")
				.setParameter("actionName", actionMasterDto.getParentActioName().toLowerCase())
				.setParameter("parentId", actionMasterDto.getParentId())

				.setResultTransformer(Transformers.aliasToBean(ActionMasterDto.class)).uniqueResult();
			  id=masterDto.getActionId();
			}

	return new Response(SUCCESS, SUCCESS_CODE, null, null, id);
		
	} catch (HibernateException he) {
	logger.error("getActionId failed", he);
	} catch (Exception e) {
	logger.error("getActionId failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response searchActionMasterByParentActionName(ActionMasterDto actionMasterDto) throws ApplicationException {
		try {
			List<ActionMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("SEARCH_ACTION_MASTER_BY_PARENT_ACTION_NAME")
				.setParameter("actionName", actionMasterDto.getActionName().toLowerCase()+"%")
				.setResultTransformer(Transformers.aliasToBean(ActionMasterDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
	} catch (HibernateException he) {
	logger.error("searchActionMasterByParentActionName failed", he);
	} catch (Exception e) {
	logger.error("searchActionMasterByParentActionName failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getParentId() throws ApplicationException {
		try {
			List<ActionMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PARENT_ID")
					.setResultTransformer(Transformers.aliasToBean(ActionMasterDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
	} catch (HibernateException he) {
	logger.error("getParentId failed", he);
	} catch (Exception e) {
	logger.error("getParentId failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
	
}

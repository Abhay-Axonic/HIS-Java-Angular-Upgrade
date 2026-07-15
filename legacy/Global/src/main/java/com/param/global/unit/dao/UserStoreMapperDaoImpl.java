package com.param.global.unit.dao;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.master.unit.model.UserStoreMapperNew;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.unit.dto.UserStoreMapperDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class UserStoreMapperDaoImpl extends GenericDao<UserStoreMapperNew, Integer> implements IUserStoreMapperDao, ICommonConstants, IError{
	final static Logger logger = Logger.getLogger(UserStoreMapperDaoImpl.class);

	public UserStoreMapperDaoImpl() {
		super(UserStoreMapperNew.class);
	}
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveUserStoreMapper(UserStoreMapperDto userStoreMapperDto) throws ApplicationException {
		try{
			UserStoreMapperNew userStoreMapper=new UserStoreMapperNew();
			userStoreMapper.setUserId(userStoreMapperDto.getUserId());
			userStoreMapper.setStoreId(userStoreMapperDto.getStoreId());
			userStoreMapper.setIsActive(userStoreMapperDto.getIsActive());
			userStoreMapper.setOrganizationId(userStoreMapperDto.getOrganizationId());
			userStoreMapper.setUnitId(userStoreMapperDto.getUnitId());
			userStoreMapper.setAddedBy(userStoreMapperDto.getAddedBy());
			userStoreMapper.setIsDeleted(FALSE);
			userStoreMapper.setAddedDate(GlobalCommonDateUtils.getDate(userStoreMapperDto.getAddedDate(), "dd-M-yyyy HH:mm:ss"));
			userStoreMapper.setUpdatedBy(userStoreMapperDto.getUpdatedBy());
			userStoreMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(userStoreMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			
			save(userStoreMapper);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("saveUserStoreMapper failed", he);
		} catch (Exception e) {
		logger.error("saveUserStoreMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateUserStoreMapper(List<UserStoreMapperDto> listuserStoreMapperDto) throws ApplicationException {
		try{
			Integer result = sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_USER_STORE_MAPPER")
					.setParameter("isDeleted", TRUE)
					.setParameter("organizationId", listuserStoreMapperDto.get(0).getOrganizationId())
					.setParameter("unitId", listuserStoreMapperDto.get(0).getUnitId())
					.setParameter("userId", listuserStoreMapperDto.get(0).getUserId())
					.executeUpdate();
			if (result > 0) {
				for (Iterator iterator = listuserStoreMapperDto.iterator(); iterator.hasNext();) {
					UserStoreMapperDto listUserStoreMapper = (UserStoreMapperDto) iterator
							.next();
					UserStoreMapperNew userStoreMapper=new UserStoreMapperNew();
					userStoreMapper.setStoreId(listUserStoreMapper.getStoreId());
					userStoreMapper.setUserId(listUserStoreMapper.getUserId());
					userStoreMapper.setIsActive(listUserStoreMapper.getIsActive());
					userStoreMapper.setOrganizationId(listUserStoreMapper.getOrganizationId());
					userStoreMapper.setUnitId(listUserStoreMapper.getUnitId());
					userStoreMapper.setIsDeleted(FALSE);
					userStoreMapper.setAddedBy(listUserStoreMapper.getAddedBy());
					userStoreMapper.setUpdatedBy(listUserStoreMapper.getUpdatedBy());
					userStoreMapper.setAddedDate(GlobalCommonDateUtils.getDate(listUserStoreMapper.getAddedDate(), "dd-M-yyyy HH:mm:ss"));
					userStoreMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(listUserStoreMapper.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

					
					save(userStoreMapper);
				}
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} 
		} catch (HibernateException he) {
		logger.error("updateUserStoreMapper failed", he);
		} catch (Exception e) {
		logger.error("updateUserStoreMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
		

	@Override
	public Response updateUserStoreMapperStatus(Integer UserId, Integer organizationId, Integer unitId,
			Boolean isActive) throws ApplicationException {
		try{
			if (UserId != 0) {

				Integer result = sessionFactory.getCurrentSession()
						.getNamedQuery("UPDATE_USER_STORE_MAPPER_STATUS")
						.setParameter("organizationId", organizationId)
						.setParameter("unitId", unitId)
						.setParameter("isActive", isActive)
						.setParameter("userId", UserId)
						.executeUpdate();
				if (result > 0) {
					if (isActive.equals(ACTIVE)) {
						return new Response(SUCCESS,SUCCESS_CODE, COMMON_UPDATE, null, null);
					} else if (isActive.equals(INACTIVE)) {
						return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
					}
				} else {
					return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
				}

			} else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateUserStoreMapperStatus failed", he);
		} catch (Exception e) {
		logger.error("updateUserStoreMapperStatus failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getUserStoreMapperlist(UserStoreMapperDto userStoreMapperDto) throws ApplicationException {
		try{
			List<UserStoreMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_USER_STORE_MAPPER_LIST")
					.setParameter("organizationId", userStoreMapperDto.getOrganizationId())
					.setParameter("unitId", userStoreMapperDto.getUnitId())
					.setFirstResult(userStoreMapperDto.getOffset() != null ? userStoreMapperDto.getOffset() : 0)
					.setMaxResults(userStoreMapperDto.getNoOfRecordsPerPage() != null ? userStoreMapperDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(UserStoreMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getUserStoreMapperlist failed", he);
		} catch (Exception e) {
		logger.error("getUserStoreMapperlist failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserStoreMapperlistUserNameWise(UserStoreMapperDto userStoreMapperDto) throws ApplicationException {
		try{
			List<UserStoreMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_USER_STORE_MAPPER_LIST_USER_NAME_WISE")
					.setParameter("organizationId", userStoreMapperDto.getOrganizationId())
					.setParameter("unitId", userStoreMapperDto.getUnitId())
					.setFirstResult(userStoreMapperDto.getOffset() != null ? userStoreMapperDto.getOffset() : 0)
					.setResultTransformer(Transformers.aliasToBean(UserStoreMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getUserStoreMapperlistUserNameWise failed", he);
		} catch (Exception e) {
		logger.error("getUserStoreMapperlistUserNameWise failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getUserStoreMapperCount(Integer organizationId, Integer unitId) throws ApplicationException {
		try{
			BigInteger userStoreMapperrCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_USER_STORE_MAPPER_COUNT")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.uniqueResult();
				if (userStoreMapperrCount.compareTo(BigInteger.ZERO) == 1) {
					return new Response(SUCCESS, SUCCESS_CODE, null, null, userStoreMapperrCount);
				} else {
					return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, BigInteger.ZERO);
				}
		} catch (HibernateException he) {
		logger.error("getUserStoreMapperCount failed", he);
		} catch (Exception e) {
		logger.error("getUserStoreMapperCount failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getActiveUserStoreMapper(Integer organizationId, Integer unitId) throws ApplicationException {
		try{
			
			List<UserStoreMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_USER_STORE_MAPPER_LIST")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UserStoreMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getActiveUserStoreMapper failed", he);
		} catch (Exception e) {
		logger.error("getActiveUserStoreMapper failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response getUserStoreMapperById(Integer UserId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try{
			List<UserStoreMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_USER_STORE_MAPPER_BY_ID")
					.setParameter("userId", UserId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UserStoreMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getUserStoreMapperById failed", he);
		} catch (Exception e) {
		logger.error("getUserStoreMapperById failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response checkUserStoreMapperAvailable(UserStoreMapperDto userStoreMapperDto)
			throws ApplicationException {
		try{
			List<UserStoreMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_USER_STORE_MAPPER_AVAILABLE")
					.setParameter("userId", userStoreMapperDto.getUserId())
					.setParameter("organizationId", userStoreMapperDto.getOrganizationId())
					.setParameter("unitId", userStoreMapperDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(UserStoreMapperDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("checkUserStoreMapperAvailable failed", he);
		} catch (Exception e) {
		logger.error("checkUserStoreMapperAvailable failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


//	@Override
//	public Response getUserList(Integer organizationId, Integer unitId) throws ApplicationException {
//		try{
//			List<UserStoreMapperDto> list=sessionFactory.getCurrentSession()
//					.getNamedQuery("GET_LIST_OF_USER")
//					.setParameter("organizationId", organizationId)
//					.setParameter("unitId", unitId)
//					.setResultTransformer(Transformers.aliasToBean(UserStoreMapperDto.class)).list();
//			
//			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
//		} catch (HibernateException he) {
//			h
//		} catch (Exception e) {
//			
//		}
//		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
//	}


	@Override
	public Response getDetailedStoreList(Integer UserId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try{
			List<UserStoreMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DETAILED_STORE_LIST")
					.setParameter("userId", UserId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UserStoreMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getDetailedStoreList failed", he);
		} catch (Exception e) {
		logger.error("getDetailedStoreList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getStoreList(Integer organizationId, Integer unitId) throws ApplicationException {
		try{
			List<UserStoreMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_STORE_MASTER_LIST")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UserStoreMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getStoreList failed", he);
		} catch (Exception e) {
		logger.error("getStoreList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
}

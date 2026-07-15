package com.param.global.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.master.unit.model.UserNursingStationMapper;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.UserNursingStationMapperDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class UserNursingStationMapperDaoImpl extends GenericDao<UserNursingStationMapper, Integer> implements IUserNursingStationMapperDao, ICommonConstants{

	private static final Logger logger = Logger.getLogger(UserNursingStationMapperDaoImpl.class);

	public UserNursingStationMapperDaoImpl() {
		super(UserNursingStationMapper.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveUserNursingStationMapper(UserNursingStationMapperDto userNursingStationMapperDto) throws ApplicationException {
		try{
			UserNursingStationMapper userNursingStationMapperr=new UserNursingStationMapper();
			userNursingStationMapperr.setUserId(userNursingStationMapperDto.getUserId());
			userNursingStationMapperr.setNursingStationId(userNursingStationMapperDto.getNursingStationId());
			userNursingStationMapperr.setOrganisationId(userNursingStationMapperDto.getOrganisationId());
			userNursingStationMapperr.setUnitId(userNursingStationMapperDto.getUnitId());
			userNursingStationMapperr.setCreatedBy(userNursingStationMapperDto.getCreatedBy());
			userNursingStationMapperr.setStatus(userNursingStationMapperDto.getStatus());
			userNursingStationMapperr.setIsDeleted('N');
			userNursingStationMapperr.setCreatedDate(GlobalCommonDateUtils.getDate(userNursingStationMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			userNursingStationMapperr.setUpdatedBy(userNursingStationMapperDto.getUpdatedBy());
			userNursingStationMapperr.setUpdatedDate(GlobalCommonDateUtils.getDate(userNursingStationMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

			save(userNursingStationMapperr);

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			logger.error("Error while saving user nursing station mapper", he);
		} catch (Exception e) {
			logger.error("Error while saving user nursing station mapper", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateUserNursingStationMapper(List<UserNursingStationMapperDto> listUserNursingStationMapperDto,UserNursingStationMapperDto userNursingStationMapperDto) throws ApplicationException {
		try{


			Integer result = sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_USER_NURSING_STATION_MAPPER")
					.setParameter("isDeleted", 'Y')
					.setParameter("organisationId", userNursingStationMapperDto.getOrganisationId())
					.setParameter("unitId", userNursingStationMapperDto.getUnitId())
					.setParameter("userId", userNursingStationMapperDto.getUserId())
					.executeUpdate();
//			if (result > 0) {
			if(listUserNursingStationMapperDto!=null && listUserNursingStationMapperDto.size()>0) {
				for (Iterator iterator = listUserNursingStationMapperDto.iterator(); iterator.hasNext();) {
					UserNursingStationMapperDto listUserNursingStationMapper = (UserNursingStationMapperDto) iterator
							.next();
					UserNursingStationMapper userNursingStationMapperr=new UserNursingStationMapper();
					userNursingStationMapperr.setNursingStationId(listUserNursingStationMapper.getNursingStationId());
					userNursingStationMapperr.setUserId(listUserNursingStationMapper.getUserId());
					userNursingStationMapperr.setOrganisationId(listUserNursingStationMapper.getOrganisationId());
					userNursingStationMapperr.setUnitId(listUserNursingStationMapper.getUnitId());
					userNursingStationMapperr.setIsDeleted('N');
					userNursingStationMapperr.setCreatedBy(listUserNursingStationMapper.getCreatedBy());
					userNursingStationMapperr.setStatus(listUserNursingStationMapper.getStatus());
					userNursingStationMapperr.setUpdatedBy(listUserNursingStationMapper.getUpdatedBy());
					userNursingStationMapperr.setCreatedDate(GlobalCommonDateUtils.getDate(listUserNursingStationMapper.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
					userNursingStationMapperr.setUpdatedDate(GlobalCommonDateUtils.getDate(listUserNursingStationMapper.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));


					save(userNursingStationMapperr);
				}
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

			}
		} catch (HibernateException he) {
			logger.error("Error while updating user nursing station mapper", he);
		} catch (Exception e) {
			logger.error("Error while updating user nursing station mapper", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateUserNursingStationMapperStatus(Integer UserId, Integer organisationId, Integer unitId,
			Character status) throws ApplicationException {
		try{
			if (UserId != 0) {

				Integer result = sessionFactory.getCurrentSession()
						.getNamedQuery("UPDATE_USER_NURSING_STATION_MAPPER_STATUS")
						.setParameter("organisationId", organisationId)
						.setParameter("unitId", unitId)
						.setParameter("status", status)
						.setParameter("userId", UserId)
						.executeUpdate();
				if (result > 0) {
					if (status.equals(ACTIVE)) {
						return new Response(SUCCESS,SUCCESS_CODE, COMMON_UPDATE, null, null);
					} else if (status.equals(INACTIVE)) {
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
			logger.error("Error while updating user nursing station mapper status", he);
		} catch (Exception e) {
			logger.error("Error while updating user nursing station mapper status", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getUserNursingStationMapperlist(UserNursingStationMapperDto userNursingStationMapperDto) throws ApplicationException {
		try{
			List<UserNursingStationMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_USER_NURSING_STATION_MAPPER_LIST")
					.setParameter("organisationId", userNursingStationMapperDto.getOrganisationId())
					.setParameter("unitId", userNursingStationMapperDto.getUnitId())
					.setFirstResult(userNursingStationMapperDto.getOffset() != null ? userNursingStationMapperDto.getOffset() : 0)
					.setMaxResults(userNursingStationMapperDto.getNoOfRecordsPerPage() != null ? userNursingStationMapperDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(UserNursingStationMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("Error while getting user nursing station mapper list", he);
		} catch (Exception e) {
			logger.error("Error while getting user nursing station mapper list", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getUserNursingStationMapperCount(Integer organisationId, Integer unitId) throws ApplicationException {
		try{
			BigInteger userNursingStationMapperrCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_USER_NURSING_STATION_MAPPER_COUNT")
					.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.uniqueResult();
				if (userNursingStationMapperrCount.compareTo(BigInteger.ZERO) == 1) {
					return new Response(SUCCESS, SUCCESS_CODE, null, null, userNursingStationMapperrCount);
				} else {
					return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, BigInteger.ZERO);
				}
		} catch (HibernateException he) {
			logger.error("Error while getting user nursing station mapper count", he);
		} catch (Exception e) {
			logger.error("Error while getting user nursing station mapper count", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getActiveUserNursingStationMapper(Integer organisationId, Integer unitId) throws ApplicationException {
		try{

			List<UserNursingStationMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_USER_NURSING_STATION_MAPPER_LIST")
					.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UserNursingStationMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("Error while getting active user nursing station mapper", he);
		} catch (Exception e) {
			logger.error("Error while getting active user nursing station mapper", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getUserNursingStationMapperById(Integer UserId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try{
			List<UserNursingStationMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_USER_NURSING_STATION_MAPPER_BY_ID")
					.setParameter("userId", UserId)
					.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UserNursingStationMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("Error while getting user nursing station mapper by id", he);
		} catch (Exception e) {
			logger.error("Error while getting user nursing station mapper by id", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response checkUserNursingStationMapperAvailable(UserNursingStationMapperDto userNursingStationMapperDto)
			throws ApplicationException {
		try{
			List<UserNursingStationMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_USER_NURSING_STATION_MAPPER_AVAILABLE")
					.setParameter("userId", userNursingStationMapperDto.getUserId())
					.setParameter("organisationId", userNursingStationMapperDto.getOrganisationId())
					.setParameter("unitId", userNursingStationMapperDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(UserNursingStationMapperDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("Error while checking user nursing station mapper available", he);
		} catch (Exception e) {
			logger.error("Error while checking user nursing station mapper available", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getUserList(Integer organisationId, Integer unitId) throws ApplicationException {
		try{
			List<UserNursingStationMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_OF_USER")
					.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UserNursingStationMapperDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("Error while getting user list", he);
		} catch (Exception e) {
			logger.error("Error while getting user list", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getDetailedNursingStationList(Integer UserId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try{
			List<UserNursingStationMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DETAILED_NURSING_STATION_LIST")
					.setParameter("userId", UserId)
					.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UserNursingStationMapperDto.class)).list();

			Collection<UserNursingStationMapperDto> uniqList = list.stream()
					.collect(Collectors.toMap(UserNursingStationMapperDto::getNursingStationId, Function.identity(),(station1, station2) -> station1)).values();
					List<UserNursingStationMapperDto> newList = new ArrayList<>(uniqList);

				return new Response(SUCCESS, SUCCESS_CODE, null, newList, null);
		} catch (HibernateException he) {
			logger.error("Error while getting detailed nursing station list", he);
		} catch (Exception e) {
			logger.error("Error while getting detailed nursing station list", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getNursingStationList(Integer organisationId, Integer unitId) throws ApplicationException {
		try{
			List<UserNursingStationMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NURSING_STATION_MASTER_LIST")
					.setParameter("orgId", organisationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UserNursingStationMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("Error while getting nursing station list", he);
		} catch (Exception e) {
			logger.error("Error while getting nursing station list", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getUserListForStore(Integer organisationId, Integer unitId) throws ApplicationException {
		try{
			List<UserNursingStationMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_OF_USER_FOR_STORE")
					.setParameter("organisationId", organisationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UserNursingStationMapperDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			logger.error("Error while getting user list for store", he);
		} catch (Exception e) {
			logger.error("Error while getting user list for store", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}


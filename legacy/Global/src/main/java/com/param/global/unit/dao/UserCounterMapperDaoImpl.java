package com.param.global.unit.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.dto.UserCounterMapperDto;
import com.param.global.model.UserCounterMapper;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UserCounterMapperDaoImpl  extends GenericDao<UserCounterMapper, Integer>
implements IUserCounterMapperDao, ICommonConstants, IError{

	public UserCounterMapperDaoImpl() {
		super(UserCounterMapper.class);
	}

	@Override
	public Response addUserCounterMapper(UserCounterMapperDto dto) throws ApplicationException {
		try{
			UserCounterMapper master = new UserCounterMapper();
			master.setUserId(dto.getUserId());
			master.setCounterId(dto.getCounterId());
			master.setIsDeleted('N');
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setOrganisationId(dto.getOrganisationId());
			master.setStatus(ACTIVE);
			master.setUnitId(dto.getUnitId());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master = save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateUserCounterMapper(UserCounterMapperDto dto) throws ApplicationException {
		try{
			try{
				UserCounterMapper master = new UserCounterMapper();
				master.setUserId(dto.getUserId());
				master.setCounterId(dto.getCounterId());
				master.setIsDeleted('N');
				master.setCreatedBy(dto.getCreatedBy());
				master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				master.setOrganisationId(dto.getOrganisationId());
				master.setStatus(ACTIVE);
				master.setUnitId(dto.getUnitId());
				master.setUpdatedBy(dto.getUpdatedBy());
				master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
				master = save(master);
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, master);
			}catch(Exception e){
				
				throw e;
			}
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response checkUserIsMappedOrNotAdd(UserCounterMapperDto userCounterMapperDto) throws ApplicationException {
		try{
			BigInteger count = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_USER_EXIST_OR_NOT")
					.setParameter("userId", userCounterMapperDto.getUserId())
					.setParameter("unitId", userCounterMapperDto.getUnitId())
					.setParameter("organisationId", userCounterMapperDto.getOrganisationId())
					.uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, count);
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	public Response listUserCounterMapper(Integer orgId, Integer unitId, Integer offset, Integer recordPerPage)
			throws ApplicationException 
	{
		try{
			List<UserCounterMapperDto> listUserCounterMapperDto = sessionFactory.getCurrentSession().getNamedQuery("USER_COUNTER_MAPPER_LIST")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setFirstResult(offset)
					.setMaxResults(recordPerPage != 0 ? recordPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(UserCounterMapperDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listUserCounterMapperDto, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getTotalUserCounterMapperCount(Integer orgId, Integer unitId) throws ApplicationException {
		try{
			BigInteger count = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("TOTAL_USER_MAPPER_COUNT")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, count);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response activateInactivateUserCounterMapper(Integer userId, char status) throws ApplicationException {
		try{
			int count = (int) sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_USER_COUNTER_MAPPER_STATUS")
					.setParameter("userId", userId)
					.setParameter("status", status)
					.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, count);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getUserCounterMapperViewById(Integer orgId, Integer unitId, Integer userId)
			throws ApplicationException {
		try{
			List<UserCounterMapperDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_COUNTER_BY_USER")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setParameter("userId", userId)
					.setResultTransformer(Transformers.aliasToBean(UserCounterMapperDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getUserCounterMapperById(Integer orgId, Integer unitId, Integer userId)
			throws ApplicationException {
		try{
			List<UserCounterMapperDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_COUNTER_BY_USERID")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setParameter("userId", userId)
					.setResultTransformer(Transformers.aliasToBean(UserCounterMapperDto.class)).list();


			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, list);

		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response deleteUserCounerMapper(UserCounterMapperDto userCounterMapperDto) throws ApplicationException {
		try{
			int count = (int) sessionFactory.getCurrentSession()
					.getNamedQuery("DELETE_USER_COUNTER_MAPPER")
					.setParameter("userId", userCounterMapperDto.getUserId())
					.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE,COMMON_UPDATE, null, count);
		}catch(Exception e){
			
			throw e;
		}
	}

	

}

package com.param.global.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.StoreTypeMasterDto;
import com.param.global.model.StoreTypeMaster;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class StoreTypeMasterDaoImpl extends GenericDao<StoreTypeMaster, Integer>
		implements IStoreTypeMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(StoreTypeMasterDaoImpl.class);


	public StoreTypeMasterDaoImpl() {
		super(StoreTypeMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	private SessionService sessionService;


	@Override
	public Response saveStoreType(StoreTypeMasterDto dto) throws ApplicationException {
		try {
			StoreTypeMaster master=new StoreTypeMaster();
			master.setCode(dto.getCode());
			master.setType(dto.getType());
			master.setAddedBy(dto.getAddedBy());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setIsActive(true);
			master.setIsDeleted(false);
			master.setOrganizationId(dto.getOrganizationId());
			master.setUnitId(dto.getUnitId());
			master.setAddedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			sessionFactory.getCurrentSession().save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("saveStoreType failed", he);
		} catch (Exception e) {
		logger.error("saveStoreType failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateStoreType(StoreTypeMasterDto dto) throws ApplicationException {
		try {
			StoreTypeMaster master=findById(dto.getStoreTypeId());
			master.setCode(dto.getCode());
			master.setType(dto.getType());
			master.setOrganizationId(dto.getOrganizationId());
			master.setUnitId(dto.getUnitId());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			sessionFactory.getCurrentSession().update(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateStoreType failed", he);
		} catch (Exception e) {
		logger.error("updateStoreType failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateStoreTypeStatus(StoreTypeMasterDto dto) throws ApplicationException {
		try {
			StoreTypeMaster master=findById(dto.getStoreTypeId());
			master.setIsActive(dto.getIsActive()!=null ? dto.getIsActive() : true);
			//master.setOrganizationId(dto.getOrganizationId());
			//master.setUnitId(dto.getUnitId());
			master.setUpdatedBy(sessionService.getUserId());
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			sessionFactory.getCurrentSession().update(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateStoreTypeStatus failed", he);
		} catch (Exception e) {
		logger.error("updateStoreTypeStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	
	@Override
	public Response getStoreTypeList(StoreTypeMasterDto dto) throws ApplicationException {
		try {
			List<StoreTypeMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_STORE_TYPE_MASTER_LIST")
					.setParameter("orgId",dto.getOrganizationId())
					//.setParameter("unitId", dto.getUnitId())
					.setFirstResult(dto.getOffset() != null ? dto.getOffset() : 0)
					.setMaxResults(dto.getNoOfRecordsPerPage() != null ? dto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(StoreTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, dtoList, null);
		} catch (HibernateException he) {
		logger.error("getStoreTypeList failed", he);
		} catch (Exception e) {
		logger.error("getStoreTypeList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getStoreTypeCount(StoreTypeMasterDto dto) throws ApplicationException {
		try {
			Criteria  c=sessionFactory.getCurrentSession().createCriteria(StoreTypeMaster.class);
			c.add(Restrictions.eq("organizationId", dto.getOrganizationId()));
			c.add(Restrictions.eq("unitId",dto.getUnitId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response<>(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
		logger.error("getStoreTypeCount failed", he);
		} catch (Exception e) {
		logger.error("getStoreTypeCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getStoreTypeById(StoreTypeMasterDto dto) throws ApplicationException {
		try {
			List<StoreTypeMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_STORE_TYPE_BY_ID")
					.setParameter("orgId",dto.getOrganizationId())
					//.setParameter("unitId", dto.getUnitId())
					.setParameter("id", dto.getStoreTypeId())
					.setResultTransformer(Transformers.aliasToBean(StoreTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, dtoList, null);
		} catch (HibernateException he) {
		logger.error("getStoreTypeById failed", he);
		} catch (Exception e) {
		logger.error("getStoreTypeById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getStoreTypeAlreadyExistOrNot(StoreTypeMasterDto dto) throws ApplicationException {
		try{			
				List<StoreTypeMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_STORE_TYPE_ALREADY_EXIST_OR_NOT")
						.setParameter("orgId",dto.getOrganizationId())
						//.setParameter("unitId", dto.getUnitId())
						.setParameter("code", dto.getCode().toLowerCase())
						.setParameter("type", dto.getType().toLowerCase())
						.setResultTransformer(Transformers.aliasToBean(StoreTypeMasterDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, dtoList, null);
		}catch(HibernateException he){
		logger.error("getStoreTypeAlreadyExistOrNot failed", he);
		}
		catch(Exception e){
		logger.error("getStoreTypeAlreadyExistOrNot failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getStoreTypeAlreadyExistOrNotById(StoreTypeMasterDto dto) throws ApplicationException {
		try{
			List<StoreTypeMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_STORE_TYPE_ALREADY_EXIST_OR_NOT_BY_ID")
					.setParameter("orgId",dto.getOrganizationId())
					//.setParameter("unitId", dto.getUnitId())
					.setParameter("id", dto.getStoreTypeId())
					.setParameter("code", dto.getCode().toLowerCase())
					.setParameter("type", dto.getType().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(StoreTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, dtoList, null);
		}catch(HibernateException he){
		logger.error("getStoreTypeAlreadyExistOrNotById failed", he);
		}
		catch(Exception e){
		logger.error("getStoreTypeAlreadyExistOrNotById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

}

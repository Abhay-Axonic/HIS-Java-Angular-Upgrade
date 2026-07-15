package com.param.global.unit.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.master.unit.model.DiscountTypeMaster;
import com.param.entity.model.master.Organization;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.unit.dto.DiscountCategoryTypeMasterDto;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.global.User;
import com.param.entity.model.master.DiscountCategory;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings({"unchecked","rawtypes"})
@Repository
public class DiscountTypeMasterDaoImpl extends GenericDao<com.param.entity.model.master.DiscountCategory, Integer>
		implements IDiscountTypeMasterDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(DiscountTypeMasterDaoImpl.class);

	public DiscountTypeMasterDaoImpl() {
		super(com.param.entity.model.master.DiscountCategory.class);
	}
	@Autowired
	SessionFactory sessionFactory;

	
	// As Discussed with divyesh, purva, changed discount type to discount category and naming convention is same for UI
	
	@Override
	public Response saveDiscountCategory(DiscountCategoryTypeMasterDto dto) throws ApplicationException {
		try {
			//DiscountTypeMaster master =new DiscountTypeMaster();
			com.param.entity.model.master.DiscountCategory  master = new com.param.entity.model.master.DiscountCategory();
			master.setAddedByUser(dto.getAddedBy());
			master.setAddedDate(GlobalCommonDateUtils.getDate(dto.getAddedDate(), "dd-M-yyyy HH:mm:ss"));
			master.setUpdatedByUser(dto.getUpdatedBy());
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			master.setCode(dto.getCode());
			master.setCategory(dto.getType());
		//	master.setType(dto.getType());
			master.setIsActive(true);
			master.setIsDeleted(false);
			master.setOrganizationId(dto.getOrganizationId());
			master.setUnitId(dto.getUnitId());
			
			sessionFactory.getCurrentSession().save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			logger.error("saveDiscountCategory failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveDiscountCategory failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDiscountCategory(DiscountCategoryTypeMasterDto dto) throws ApplicationException {
		try {
			com.param.entity.model.master.DiscountCategory master=findById(dto.getDiscountTypeId());
			master.setUpdatedByUser(dto.getUpdatedBy());
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			master.setCode(dto.getCode());
			master.setCategory(dto.getType());
			sessionFactory.getCurrentSession().update(master);
			return new Response<>(SUCCESS, null, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updateDiscountCategory failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateDiscountCategory failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDiscountCategoryStatus(DiscountCategoryTypeMasterDto dto) throws ApplicationException {
		try {
			DiscountCategory master=findById(dto.getDiscountTypeId());
			master.setUpdatedByUser(dto.getUpdatedBy());
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			master.setIsActive(dto.getIsActive()==true ? true : false);
			sessionFactory.getCurrentSession().update(master);
			return new Response<>(SUCCESS, null, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updateDiscountCategoryStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateDiscountCategoryStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDiscountCategoryList(DiscountCategoryTypeMasterDto dto) throws ApplicationException {
		try {
			
			
			String sql=" select id as    \"discountTypeId\"," +
			            " category as \"type\" , code as \"code\", " +
			            " is_active as \"isActive\" "
			            + " from public.m_discount_category  where unit_id="+dto.getUnitId();
			
			List<DiscountCategoryTypeMasterDto> dtoList = sessionFactory.getCurrentSession().createSQLQuery(sql)
					.setFirstResult(dto.getOffset() != null ? dto.getOffset() : 0)
					.setMaxResults(dto.getNoOfRecordsPerPage() != null ? dto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(DiscountCategoryTypeMasterDto.class)).list();
			
			return new Response<>(SUCCESS, null, null, dtoList, null);
		} catch (HibernateException he) {
			logger.error("getDiscountCategoryList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getDiscountCategoryList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDiscountCategoryCount(DiscountCategoryTypeMasterDto dto) throws ApplicationException {
		try {
			Criteria c=sessionFactory.getCurrentSession().createCriteria(DiscountCategory.class).createAlias("unit","unit");;
			c.add(Restrictions.eq("isActive", true));
			c.add(Restrictions.eq("isDeleted", false));
			c.add(Restrictions.eq("unit.unitId", dto.getUnitId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response<>(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
			logger.error("getDiscountCategoryCount failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getDiscountCategoryCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDiscountCategoryById(DiscountCategoryTypeMasterDto dto) throws ApplicationException {
		try {
			
			
			String sql=" select id as    \"discountTypeId\"," +
		            " category as type , code as code"+
					" , is_active as \"isActive\" "+
		            " from public.m_discount_category  where id="+dto.getDiscountTypeId();
		
		List<DiscountCategoryTypeMasterDto> dtoList = sessionFactory.getCurrentSession().createSQLQuery(sql)
				.setFirstResult(dto.getOffset() != null ? dto.getOffset() : 0)
				.setMaxResults(dto.getNoOfRecordsPerPage() != null ? dto.getNoOfRecordsPerPage() : 10)
				.setResultTransformer(Transformers.aliasToBean(DiscountCategoryTypeMasterDto.class)).list();
			
			
			
			
			return new Response<>(SUCCESS, null, null, dtoList, null);
		} catch (HibernateException he) {
			logger.error("getDiscountCategoryById failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getDiscountCategoryById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDiscountCategoryExistOrNotByName(DiscountCategoryTypeMasterDto dto) throws ApplicationException {
		try{
			List<DiscountCategoryTypeMasterDto> dtoList = sessionFactory.getCurrentSession().getNamedQuery("GET_DISCOUNT_CATEGORY_EXIST_OR_NOT_BY_NAME")
					.setParameter("code", dto.getCode().toLowerCase())
					.setParameter("type", dto.getType().toLowerCase())
					.setParameter("orgId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DiscountCategoryTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, dtoList, null);
		}catch(HibernateException he){
			logger.error("getDiscountCategoryExistOrNotByName failed", he);
			throw new ServiceException(he.getMessage());
		}catch(Exception e){
			logger.error("getDiscountCategoryExistOrNotByName failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDiscountCategoryExistOrNotByNameNotById(DiscountCategoryTypeMasterDto dto)
			throws ApplicationException {
		try{
			List<DiscountCategoryTypeMasterDto> dtoList = sessionFactory.getCurrentSession().getNamedQuery("GET_DISCOUNT_CATEGORY_EXIST_OR_NOT_BY_NAME_NOT_BY_ID")
					.setParameter("id", dto.getDiscountTypeId())
					.setParameter("code", dto.getCode().toLowerCase())
					.setParameter("type", dto.getType().toLowerCase())
					.setParameter("orgId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DiscountCategoryTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, dtoList, null);
		}catch(HibernateException he){
			logger.error("getDiscountCategoryExistOrNotByNameNotById failed", he);
			throw new ServiceException(he.getMessage());
		}catch(Exception e){
			logger.error("getDiscountCategoryExistOrNotByNameNotById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}

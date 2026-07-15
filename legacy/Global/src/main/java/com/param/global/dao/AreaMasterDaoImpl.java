package com.param.global.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
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
import com.param.global.dto.AreaMasterDto;
import com.param.global.dto.CityMasterDto;
import com.param.global.model.AreaMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AreaMasterDaoImpl extends GenericDao<AreaMaster, Integer> implements ICommonConstants, IAreaMasterDao {
	final static Logger logger = Logger.getLogger(AreaMasterDaoImpl.class);


	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private SessionFactory sessionFactory;

	public AreaMasterDaoImpl() {
		super(AreaMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response getAreaByName(AreaMasterDto areaMasterDto) throws ApplicationException {
		try {
			List<AreaMasterDto> areaMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AREA_LIST_BY_NAME")
					.setParameter("areaName", areaMasterDto.getAreaName().toLowerCase())
					.setParameter("countryId", areaMasterDto.getCountryId())
					.setParameter("stateId", areaMasterDto.getStateId())
					.setParameter("districtId", areaMasterDto.getDistrictId())
					.setParameter("cityId", areaMasterDto.getCityId())
					.setParameter("orgId", areaMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AreaMasterDto.class)).list();
			return new Response(SUCCESS, null, null, areaMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getAreaByName failed", he);
		} catch (Exception e) {
		logger.error("getAreaByName failed", e);
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addAreaMaster(AreaMasterDto areaMasterDto) throws ApplicationException {
		try {
			AreaMaster areaMaster = mapper.map(areaMasterDto, AreaMaster.class, "AreaMasterDto_to_AreaMaster");
			areaMaster.setStatus('A');
			areaMaster = save(areaMaster);
			areaMasterDto.setAreaId(areaMaster.getAreaId());
			return new Response(SUCCESS, null, null, null, areaMasterDto);
		} catch (HibernateException he) {
		logger.error("addAreaMaster failed", he);
		} catch (Exception e) {
		logger.error("addAreaMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAreaMasterList(AreaMasterDto areaMasterDto) throws ApplicationException {
		try {
			List<AreaMasterDto> areaMasterDtoList = sessionFactory.getCurrentSession().getNamedQuery("GET_AREA_LIST")
					.setParameter("orgId", areaMasterDto.getOrganizationId())
					.setFirstResult(areaMasterDto.getOffset() != null ? areaMasterDto.getOffset() : 0)
					.setMaxResults(areaMasterDto.getNoOfRecordsPerPage() != null ? areaMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(AreaMasterDto.class)).list();
			return new Response(SUCCESS, null, null, areaMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getAreaMasterList failed", he);
		} catch (Exception e) {
		logger.error("getAreaMasterList failed", e);
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAreaByNameNotId(AreaMasterDto areaMasterDto) throws ApplicationException {
		try {
			List<AreaMasterDto> areaMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AREA_LIST_BY_NAME_NOT_ID").setParameter("areaId", areaMasterDto.getAreaId())
					.setParameter("areaName", areaMasterDto.getAreaName().toLowerCase())
					.setParameter("countryId", areaMasterDto.getCountryId())
					.setParameter("stateId", areaMasterDto.getStateId())
					.setParameter("districtId", areaMasterDto.getDistrictId())
					.setParameter("cityId", areaMasterDto.getCityId())
					.setParameter("orgId", areaMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AreaMasterDto.class)).list();
			return new Response(SUCCESS, null, null, areaMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getAreaByNameNotId failed", he);
		} catch (Exception e) {
		logger.error("getAreaByNameNotId failed", e);
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAreaById(AreaMasterDto areaMasterDto) throws ApplicationException {
		try {
			List<AreaMasterDto> areaMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AREA_LIST_BY_ID").setParameter("areaId", areaMasterDto.getAreaId())
					.setResultTransformer(Transformers.aliasToBean(AreaMasterDto.class)).list();
			return new Response(SUCCESS, null, null, areaMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getAreaById failed", he);
		} catch (Exception e) {
		logger.error("getAreaById failed", e);
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveAreaList() throws ApplicationException {
		try {
			List<AreaMasterDto> areaMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_AREA_LIST")
					.setResultTransformer(Transformers.aliasToBean(AreaMasterDto.class)).list();
			return new Response(SUCCESS, null, null, areaMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getActiveAreaList failed", he);
		} catch (Exception e) {
		logger.error("getActiveAreaList failed", e);
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveCityListByDistrictId(CityMasterDto cityMasterDto) throws ApplicationException {
		try {
			List<CityMasterDto> cityMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CITY_LIST_BY_DISTRICT_ID")
					.setParameter("districtId", cityMasterDto.getDistrictId())
					.setResultTransformer(Transformers.aliasToBean(CityMasterDto.class)).list();
			return new Response(SUCCESS, null, null, cityMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getActiveCityListByDistrictId failed", he);
		} catch (Exception e) {
		logger.error("getActiveCityListByDistrictId failed", e);
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateAreaStatus(AreaMasterDto areaMasterDto) throws ApplicationException {
		try {

			AreaMaster areaMaster = findById(areaMasterDto.getAreaId());
			areaMaster.setStatus(areaMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			areaMaster.setUpdatedBy(areaMasterDto.getUpdatedBy());
			areaMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(areaMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(areaMaster);
			return new Response<AreaMasterDto, Integer>(SUCCESS, null, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
		logger.error("updateAreaStatus failed", he);
			return new Response<AreaMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("updateAreaStatus failed", e);
			return new Response<AreaMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
			 ;
		}
	}

	@Override
	public Response updateAreaMaster(AreaMasterDto areaMasterDto) throws ApplicationException {
		try {

			AreaMaster areaMaster = findById(areaMasterDto.getAreaId());

			areaMaster.setAreaName(areaMasterDto.getAreaName());
			areaMaster.setAreaCode(areaMasterDto.getAreaCode());
			areaMaster.setCityId(areaMasterDto.getCityId());
			areaMaster.setCountryId(areaMasterDto.getCountryId());
			areaMaster.setStateId(areaMasterDto.getStateId());
			areaMaster.setDistrictId(areaMasterDto.getDistrictId());
			areaMaster.setCityId(areaMasterDto.getCityId());
			areaMaster.setUpdatedBy(areaMasterDto.getUpdatedBy());
			areaMaster.setPostCode(areaMasterDto.getPostCode());
			areaMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(areaMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(areaMaster);
			return new Response<AreaMasterDto, Integer>(SUCCESS, null, COMMON_UPDATE, null, areaMaster);

		} catch (HibernateException he) {
		logger.error("updateAreaMaster failed", he);
			return new Response<AreaMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
		logger.error("updateAreaMaster failed", e);
			return new Response<AreaMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
			 ;
		}
	}

	@Override
	public Response getCount(AreaMasterDto areaMasterDto)
	{
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(AreaMaster.class); 
//			c.add(Restrictions.eq("organizationId", areaMasterDto.getOrganizationId()));
			c.add(Restrictions.disjunction()
					.add(Restrictions.eq("organizationId",areaMasterDto.getOrganizationId()))
					.add(Restrictions.isNull("organizationId")));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		}catch(Exception e)
		{
		logger.error("getCount failed", e);
			return new Response(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response getDataByPincode(String code, Integer orgId) {
		try{
			
			/*Criteria c=sessionFactory.getCurrentSession().createCriteria(AreaMaster.class); 
				c.add(Restrictions.like("postCode", "%"+code+"%"));
				c.add(Restrictions.eq("organizationId", orgId));
				c.add(Restrictions.isNull("organizationId"));*/
			
			List<AreaMaster> areaMastersList = sessionFactory.getCurrentSession()
					.createQuery("from AreaMaster "
							+ " WHERE postCode like '%"+code+"%' "
							+ " AND (organizationId=:orgId OR organizationId is NULL) ")
					.setParameter("orgId", orgId)
					.list();
			
			AreaMasterDto dto = new AreaMasterDto();
			
			if(areaMastersList!=null && areaMastersList.size()>0)
			{
					dto.setCountryId(areaMastersList.get(0).getCountryId());
					dto.setStateId(areaMastersList.get(0).getStateId());
					dto.setDistrictId(areaMastersList.get(0).getDistrictId());
					dto.setCityId(areaMastersList.get(0).getCityId());
					dto.setAreaId(areaMastersList.get(0).getAreaId());
					dto.setPostCode(areaMastersList.get(0).getPostCode());
				return new Response(SUCCESS, null, null, null, dto);
			}else{
				return new Response(ERROR, null, null, null, null);
			}
		}catch(Exception e)
		{
		logger.error("getDataByPincode failed", e);
			return new Response(ERROR, null, null, null, null);
		}
	}

}

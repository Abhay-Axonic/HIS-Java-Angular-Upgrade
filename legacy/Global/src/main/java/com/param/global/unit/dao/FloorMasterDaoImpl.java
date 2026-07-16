package com.param.global.unit.dao;

import java.util.List;

import org.apache.log4j.Logger;
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

import com.param.adt.master.unit.model.FloorMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.unit.dto.FloorMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes","unchecked"})
@Repository
public class FloorMasterDaoImpl extends GenericDao<FloorMaster, Integer> implements IFloorMasterDao,ICommonConstants{

	final static Logger logger = Logger.getLogger(FloorMasterDaoImpl.class);

	public FloorMasterDaoImpl() {
		super(FloorMaster.class);
		// TODO Auto-generated constructor stub
	}
	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Response getFloorByName(FloorMasterDto floorMasterDto) throws ApplicationException {
		try {
			List<FloorMasterDto> nationalityMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_FLOOR_LIST_BY_NAME")
					.setParameter("floorName", floorMasterDto.getFloorName().toLowerCase())
					.setParameter("organizationId", floorMasterDto.getOrganizationId())
					.setParameter("unitId", floorMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(FloorMasterDto.class)).list();
			return new Response(SUCCESS, null, null, nationalityMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getFloorByName failed", he);
		} catch (Exception e) {
			logger.error("getFloorByName failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addFloorMaster(FloorMasterDto floorMasterDto) throws ApplicationException {
		try {
			FloorMaster floorMaster = mapper.map(floorMasterDto, FloorMaster.class,
					"FloorMasterDto_to_FloorMaster");
			floorMaster.setCreatedDate(TimeZoneComponent.getDateByzone(floorMasterDto.getUnitId()));
			floorMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(floorMasterDto.getUnitId()));
			floorMaster.setStatus('A');
			floorMaster = save(floorMaster);
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			logger.error("addFloorMaster failed", he);
		} catch (Exception e) {
			logger.error("addFloorMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getFloorMasterList(FloorMasterDto floorMasterDto) throws ApplicationException {
		try {
			List<FloorMasterDto> floorMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_FLOOR_LIST")
					.setParameter("orgId", floorMasterDto.getOrganizationId())
					.setParameter("unitId", floorMasterDto.getUnitId())
					.setFirstResult(floorMasterDto.getOffset() != null ? floorMasterDto.getOffset() : 0)
					.setMaxResults(floorMasterDto.getNoOfRecordsPerPage() != null ? floorMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(FloorMasterDto.class)).list();
			
			return new Response(SUCCESS, null, null, floorMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getFloorMasterList failed", he);
		} catch (Exception e) {
			logger.error("getFloorMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getFloorByID(FloorMasterDto floorMasterDto) throws ApplicationException {
		try {
			List<FloorMasterDto> floorMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_FLOOR_LIST_BY_ID").setParameter("floorId", floorMasterDto.getFloorId())
					.setResultTransformer(Transformers.aliasToBean(FloorMasterDto.class)).list();
			return new Response(SUCCESS, null, null, floorMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getFloorByID failed", he);
		} catch (Exception e) {
			logger.error("getFloorByID failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateFloorStatus(FloorMasterDto floorMasterDto) throws ApplicationException {
		try {

			FloorMaster floorMaster = findById(floorMasterDto.getFloorId());
			floorMaster.setUpdatedBy(floorMasterDto.getUpdatedBy());
			floorMaster.setStatus(floorMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			floorMaster
					.setUpdatedDate(GlobalCommonDateUtils.getDate(floorMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(floorMaster);
			return new Response<FloorMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
			logger.error("updateFloorStatus failed", he);
			return new Response<FloorMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			logger.error("updateFloorStatus failed", e);
			return new Response<FloorMasterDto, Integer>(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response updateFloorMaster(FloorMasterDto floorMasterDto) throws ApplicationException {
		try {
			FloorMaster floorMaster = findById(floorMasterDto.getFloorId());
			floorMaster.setFloorName(floorMasterDto.getFloorName());
			floorMaster.setFloorCode(floorMasterDto.getFloorCode());
			floorMaster.setUpdatedBy(floorMasterDto.getUpdatedBy());
			floorMaster
					.setUpdatedDate(GlobalCommonDateUtils.getDate(floorMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(floorMaster);
			return new Response<FloorMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
			logger.error("updateFloorMaster failed", he);
			return new Response<FloorMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			logger.error("updateFloorMaster failed", e);
			return new Response<FloorMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}

	@Override
	public Response getFloorByNameNotId(FloorMasterDto floorMasterDto) throws ApplicationException {
		try {
			List<FloorMasterDto> nationalityMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_FLOOR_LIST_BY_NAME_NOT_ID")
					.setParameter("floorId", floorMasterDto.getFloorId())
					.setParameter("floorName", floorMasterDto.getFloorName().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(FloorMasterDto.class)).list();
			return new Response(SUCCESS, null, null, nationalityMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getFloorByNameNotId failed", he);
		} catch (Exception e) {
			logger.error("getFloorByNameNotId failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveFloorList() throws ApplicationException {
		try {
			List<FloorMasterDto> nationalityMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_FLOOR_LIST")
					.setResultTransformer(Transformers.aliasToBean(FloorMasterDto.class)).list();
			return new Response(SUCCESS, null, null, nationalityMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getActiveFloorList failed", he);
		} catch (Exception e) {
			logger.error("getActiveFloorList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCount(FloorMasterDto floorMasterDto)
	{
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(FloorMaster.class); 
			c.add(Restrictions.eq("organizationId", floorMasterDto.getOrganizationId()));
			c.add(Restrictions.eq("unitId", floorMasterDto.getUnitId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		}catch(Exception e)
		{
			logger.error("getCount failed", e);
			return new Response(ERROR, null, null, null, null);
		}
	}

}

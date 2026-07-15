package com.param.adt.master.unit.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.*;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.common.ADTCommonDateUtils;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.unit.dto.UnitSubSpecialityMapperDto;
import com.param.adt.master.unit.model.UnitSubSpecialityMapper;
import com.param.global.dto.SpecialityMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Repository
public class UnitSubSpecialityDaoImpl extends GenericDao<UnitSubSpecialityMapper, Integer>
		implements IUnitSubSpecialityDao, ICommonConstants {

	public UnitSubSpecialityDaoImpl() {
		super(UnitSubSpecialityMapper.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	DozerBeanMapper mapper;

	@Override
	public Response saveUnitSubSpecialitiy(UnitSubSpecialityMapperDto subSpecialityMapperDto) {
		try {
			UnitSubSpecialityMapper unitSubSpecialityMapper = mapper.map(subSpecialityMapperDto,
					UnitSubSpecialityMapper.class, "UnitSubSpecialtyMapperDto_to_UnitSubSpecialtyMapper");
			unitSubSpecialityMapper.setCreatedDate(TimeZoneComponent.getDateByzone(subSpecialityMapperDto.getUnitId() != null ? subSpecialityMapperDto.getUnitId() : 0));
			unitSubSpecialityMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(subSpecialityMapperDto.getUnitId() != null ? subSpecialityMapperDto.getUnitId() : 0));
			unitSubSpecialityMapper.setStatus('A');
			save(unitSubSpecialityMapper);
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getUnitSubSpecialityList(UnitSubSpecialityMapperDto subSpecialityMapperDto)
			throws ApplicationException {
		try {
			List<UnitSubSpecialityMapperDto> unitSubSpecialtyMapperDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_UNIT_SUB_SPECIALITY_LIST")
					.setParameter("orgId", subSpecialityMapperDto.getOrganizationId())
					.setParameter("unitId", subSpecialityMapperDto.getUnitId())
					.setFirstResult(subSpecialityMapperDto.getOffset() != null ? subSpecialityMapperDto.getOffset() : 0)
					.setMaxResults(subSpecialityMapperDto.getNoOfRecordsPerPage() != null
							? subSpecialityMapperDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(UnitSubSpecialityMapperDto.class)).list();
			return new Response(SUCCESS, null, null, unitSubSpecialtyMapperDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
			 ;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getUnitSubSpecialityById(UnitSubSpecialityMapperDto unitSubSpecialityMapperDto)
			throws ApplicationException {
		try
		{
			UnitSubSpecialityMapper unitSubSpecialityMapper=findById(unitSubSpecialityMapperDto.getUnitSubSpecialityId());
		if(unitSubSpecialityMapper.getUnitSubSpecialityId()==unitSubSpecialityMapperDto.getUnitSubSpecialityId())
			return new Response(SUCCESS, null, null, null, null);
		else 
			return new Response(ERROR, null, null, null, null);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForUnitSpecialityList(UnitSubSpecialityMapperDto unitSubSpecialityMapperDto)
			throws ApplicationException {
		try {
			UnitSubSpecialityMapper unitSubSpecialityMapper = findById(
					unitSubSpecialityMapperDto.getUnitSubSpecialityId());

			unitSubSpecialityMapper.setStatus(unitSubSpecialityMapperDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			unitSubSpecialityMapper.setUpdatedBy(unitSubSpecialityMapperDto.getUpdatedBy());
			unitSubSpecialityMapper.setUpdatedDate(
					ADTCommonDateUtils.getDate(unitSubSpecialityMapperDto.getUpdatedDate(), "dd-M-yyy HH:mm:ss"));
			update(unitSubSpecialityMapper);
			return new Response<SpecialityMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
			return new Response<SpecialityMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			
			return new Response<SpecialityMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
			 ;
		}
	}

	@Override
	public Response truncateUnitSubSpecialtiy(UnitSubSpecialityMapperDto obj) throws ApplicationException {
		try {
			// Updating status from A(active) to T(Truncated)
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("UPDATE UnitSubSpecialityMapper ussm " +
					"SET ussm.status = :status, " +
					"ussm.updatedBy = :updatedBy, " +
					"ussm.updatedDate = :updatedDate " +
					"WHERE ussm.status = :currentStatus");
			query.setParameter("status", 'T');
			query.setParameter("updatedBy", obj.getUpdatedBy());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(obj.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			query.setParameter("currentStatus", 'A');
			session.close();
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getUnitSubSpecialityCount(UnitSubSpecialityMapperDto unitSubSpecialityMapperDto)
			throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria(UnitSubSpecialityMapper.class);
			Criterion unit = Restrictions.eq("unitId", unitSubSpecialityMapperDto.getUnitId());
			Criterion org = Restrictions.eq("organizationId", unitSubSpecialityMapperDto.getOrganizationId());
			LogicalExpression andExp = Restrictions.and(unit, org);
			c.add(andExp);
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (Exception e) {
			
			return new Response(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response getSubSpecialityBySpecialityId(UnitSubSpecialityMapperDto unitSubSpecialityMapperDto)
			throws ApplicationException {
		try {
			List<UnitSubSpecialityMapperDto> unitSubSpecialtyMapperDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_UNIT_SUB_SPECIALITY_LIST_BY_SPECIALITY_ID")
					.setParameter("orgId", unitSubSpecialityMapperDto.getOrganizationId())
					.setParameter("unitId", unitSubSpecialityMapperDto.getUnitId())
					.setParameter("specialityId", unitSubSpecialityMapperDto.getSpecialityId())
					.setResultTransformer(Transformers.aliasToBean(UnitSubSpecialityMapperDto.class)).list();
			return new Response(SUCCESS, null, null, unitSubSpecialtyMapperDtosList, null);

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSubSpecialityBySpecialityArray(UnitSubSpecialityMapperDto unitSubSpecialityMapperDto)
			throws ApplicationException {
		try {
			List<UnitSubSpecialityMapperDto> unitSubSpecialtyMapperDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_UNIT_SUB_SPECIALITY_LIST_BY_SPECIALITY_ARRAY")
					.setParameter("orgId", unitSubSpecialityMapperDto.getOrganizationId())
					.setParameter("unitId", unitSubSpecialityMapperDto.getUnitId())
					.setParameterList("specialityList", unitSubSpecialityMapperDto.getSpecialityIdList())
					.setResultTransformer(Transformers.aliasToBean(UnitSubSpecialityMapperDto.class)).list();
			return new Response(SUCCESS, null, null, unitSubSpecialtyMapperDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveUnitSubSpecialityList(UnitSubSpecialityMapperDto unitSubSpecialityMapperDto)
			throws ApplicationException {
		try {
			List<UnitSubSpecialityMapperDto> unitSubSpecialtyMapperDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_UNIT_SUB_SPECIALITY_LIST")
					.setParameter("orgId", unitSubSpecialityMapperDto.getOrganizationId())
					.setParameter("unitId", unitSubSpecialityMapperDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(UnitSubSpecialityMapperDto.class)).list();
			return new Response(SUCCESS, null, null, unitSubSpecialtyMapperDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSubSpecialityBySpecialityArrayForTariff(UnitSubSpecialityMapperDto unitSubSpecialityMapperDto)
			throws ApplicationException {
		try {
			List<UnitSubSpecialityMapperDto> unitSubSpecialtyMapperDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_UNIT_SUB_SPECIALITY_LIST_BY_SPECIALITY_ARRAY_FOR_TARIFF")
					.setParameter("orgId", unitSubSpecialityMapperDto.getOrganizationId())
					.setParameter("unitId", unitSubSpecialityMapperDto.getUnitId())
					.setParameterList("specialityList", unitSubSpecialityMapperDto.getSpecialityIdList())
					.setResultTransformer(Transformers.aliasToBean(UnitSubSpecialityMapperDto.class)).list();
			return new Response(SUCCESS, null, null, unitSubSpecialtyMapperDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response unitSubDepartmentMasterUniversalSearch(UnitSubSpecialityMapperDto unitSubSpecialityMapperDto)
			throws ApplicationException {
		try {
			List<UnitSubSpecialityMapperDto> unitSpecialtyMapperDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_UNIT_SUB_DEPARTMENT_MASTER_DETAILS_LIST_SEARCH")
					.setParameter("orgId", unitSubSpecialityMapperDto.getOrganizationId())
					.setParameter("unitId", unitSubSpecialityMapperDto.getUnitId())
					.setParameter("specialityName", "%"+unitSubSpecialityMapperDto.getSpecialityName().trim().toLowerCase()+"%")
					.setFirstResult(unitSubSpecialityMapperDto.getOffset() != null ? unitSubSpecialityMapperDto.getOffset() : 0)
					.setMaxResults(unitSubSpecialityMapperDto.getNoOfRecordsPerPage() != null ? unitSubSpecialityMapperDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(UnitSubSpecialityMapperDto.class)).list();
			return new Response(SUCCESS, null, null, unitSpecialtyMapperDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}
}

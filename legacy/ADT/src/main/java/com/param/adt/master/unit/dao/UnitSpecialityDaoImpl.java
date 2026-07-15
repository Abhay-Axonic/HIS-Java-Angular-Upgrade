package com.param.adt.master.unit.dao;

import java.util.List;

import com.param.global.service.ISpecialityService;
import com.param.global.service.SpecialityServiceImpl;
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
import com.param.adt.master.unit.dto.UnitSpecialtyMapperDto;
import com.param.adt.master.unit.model.UnitSpecialityMapper;
import com.param.global.dto.SpecialityMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UnitSpecialityDaoImpl extends GenericDao<UnitSpecialityMapper, Integer>
		implements ICommonConstants, IUnitSpecialityDao {

	public UnitSpecialityDaoImpl() {
		super(UnitSpecialityMapper.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private ISpecialityService iSpecialityService;

	@Autowired
	DozerBeanMapper mapper;

	@Override
	public Response saveUnitSpecialtiy(UnitSpecialtyMapperDto unitSpecialtyMapperDto) throws ApplicationException {
		try {
			UnitSpecialityMapper unitSpecialityMapper = mapper.map(unitSpecialtyMapperDto, UnitSpecialityMapper.class,
					"UnitSpecialtyMapperDto_to_UnitSpecialtyMapper");
			unitSpecialityMapper.setCreatedDate(TimeZoneComponent.getDateByzone(unitSpecialtyMapperDto.getUnitId() != null ? unitSpecialtyMapperDto.getUnitId() : 0));
			unitSpecialityMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(unitSpecialtyMapperDto.getUnitId() != null ? unitSpecialtyMapperDto.getUnitId() : 0));
			unitSpecialityMapper.setStatus('A');
			unitSpecialityMapper.setIsClinicalSpeciality(unitSpecialityMapper.getIsClinicalSpeciality()=='\u0000'?'N':unitSpecialityMapper.getIsClinicalSpeciality());
			unitSpecialityMapper.setIsRenderingSpeciality(unitSpecialityMapper.getIsRenderingSpeciality()=='\u0000'?'N':unitSpecialityMapper.getIsRenderingSpeciality());
			unitSpecialityMapper.setIsSurgicalCode(unitSpecialityMapper.getIsSurgicalCode()=='\u0000'?'N':unitSpecialityMapper.getIsSurgicalCode());
			unitSpecialityMapper.setDeptWiseAppointment(unitSpecialityMapper.getDeptWiseAppointment()=='\u0000'?'N':unitSpecialityMapper.getDeptWiseAppointment());
			save(unitSpecialityMapper);
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getUnitSpecialtiyList(UnitSpecialtyMapperDto unitSpecialtyMapperDto) throws ApplicationException {
		try {
			List<UnitSpecialtyMapperDto> unitSpecialtyMapperDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_UNIT_SPECIALITY_LIST")
					.setParameter("orgId", unitSpecialtyMapperDto.getOrganizationId())
					.setParameter("unitId", unitSpecialtyMapperDto.getUnitId())
					.setFirstResult(unitSpecialtyMapperDto.getOffset() != null ? unitSpecialtyMapperDto.getOffset() : 0)
					.setMaxResults(unitSpecialtyMapperDto.getNoOfRecordsPerPage() != null ? unitSpecialtyMapperDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(UnitSpecialtyMapperDto.class)).list();
			return new Response(SUCCESS, null, null, unitSpecialtyMapperDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getUnitSpecialityById(UnitSpecialtyMapperDto unitSpecialtyMapperDto) throws ApplicationException {
		/*try
		{
			UnitSpecialityMapper unitSpecialityMapper=findById(unitSpecialtyMapperDto.getUnitSpecialityMapperId());
		if(unitSpecialityMapper.getUnitSpecialityMapperId()==unitSpecialtyMapperDto.getUnitSpecialityMapperId())
			return new Response(SUCCESS, null, null, null, null);
		else 
			return new Response(ERROR, null, null, null, null);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, null, null, null, null);*/
		try {
			List<UnitSpecialtyMapperDto> unitSpecialtyMapperDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_UNIT_SPECIALITY_BY_ID")
					.setParameter("unitSpecialityMapperId", unitSpecialtyMapperDto.getUnitSpecialityMapperId())
					.setParameter("orgId", unitSpecialtyMapperDto.getOrganizationId())
					.setParameter("unitId", unitSpecialtyMapperDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(UnitSpecialtyMapperDto.class)).list();
			return new Response(SUCCESS, null, null, unitSpecialtyMapperDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	
	}

	@Override
	public Response updateStatusForUnitSpecialityList(UnitSpecialtyMapperDto unitSpecialtyMapperDto)
			throws ApplicationException {
		try {
			UnitSpecialityMapper unitSpecialityMapper = findById(unitSpecialtyMapperDto.getUnitSpecialityMapperId());

			unitSpecialityMapper.setStatus(unitSpecialtyMapperDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			unitSpecialityMapper.setUpdatedBy(unitSpecialtyMapperDto.getUpdatedBy());
			unitSpecialityMapper.setUpdatedDate(
					ADTCommonDateUtils.getDate(unitSpecialtyMapperDto.getUpdatedDate(), "dd-M-yyy HH:mm:ss"));
			update(unitSpecialityMapper);
			return new Response<SpecialityMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
			return new Response<SpecialityMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			
			return new Response<SpecialityMasterDto, Integer>(ERROR, null, null, null, null);
		} 
	}

	@Override
	public Response truncateUnitSpecialtiy(UnitSpecialtyMapperDto obj) {
		try {
			// Updating status from A(active) to T(Truncated)
			Session session = sessionFactory.getCurrentSession();
			Query query = session.createQuery("UPDATE UnitSpecialityMapper ussm " +
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
	public Response getCount(UnitSpecialtyMapperDto unitSpecialtyMapperDto) throws ApplicationException {
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(UnitSpecialityMapper.class); 
			Criterion unit=Restrictions.eq("unitId", unitSpecialtyMapperDto.getUnitId());
			Criterion org=Restrictions.eq("organizationId", unitSpecialtyMapperDto.getOrganizationId());
			LogicalExpression andExp = Restrictions.and(unit, org);
			c.add(andExp);
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		}catch(Exception e)
		{
			
			return new Response(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response getSpecialityListByDoctorId(UnitSpecialtyMapperDto unitSpecialtyMapperDto)
			throws ApplicationException {
		try {
			List<UnitSpecialtyMapperDto> unitSpecialtyMapperDtosList=null;
			String roughQuery= sessionFactory.getCurrentSession().getNamedQuery("GET_SPECIALITY_LIST_BY_DOCTOR_ID").getQueryString();
			StringBuilder stringBuilderQuery = new StringBuilder(roughQuery);
			if(unitSpecialtyMapperDto.getDoctorId()!=null)
			{
				stringBuilderQuery.append(" AND dsml.doctorId=:doctorId");
				stringBuilderQuery.append(" ORDER BY speciality.specialityName");
				unitSpecialtyMapperDtosList = sessionFactory.getCurrentSession()
						.createQuery(stringBuilderQuery.toString())
						.setParameter("orgId", unitSpecialtyMapperDto.getOrganizationId())
						.setParameter("unitId", unitSpecialtyMapperDto.getUnitId())
						.setParameter("doctorId", unitSpecialtyMapperDto.getDoctorId())
						.setResultTransformer(Transformers.aliasToBean(UnitSpecialtyMapperDto.class)).list();
			}
			else
			{
				stringBuilderQuery.append(" ORDER BY speciality.specialityName");
			unitSpecialtyMapperDtosList = sessionFactory.getCurrentSession()
					.createQuery(stringBuilderQuery.toString())
					.setParameter("orgId", unitSpecialtyMapperDto.getOrganizationId())
					.setParameter("unitId", unitSpecialtyMapperDto.getUnitId())
//					.setParameter("doctorId", unitSpecialtyMapperDto.getDoctorId())
					.setResultTransformer(Transformers.aliasToBean(UnitSpecialtyMapperDto.class)).list();
			}
			return new Response(SUCCESS, null, null, unitSpecialtyMapperDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveUnitSpecialityList(UnitSpecialtyMapperDto unitSpecialtyMapperDto)
			throws ApplicationException {
		try {
			List<UnitSpecialtyMapperDto> unitSpecialtyMapperDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_UNIT_SPECIALITY_LIST")
					.setParameter("orgId", unitSpecialtyMapperDto.getOrganizationId())
					.setParameter("unitId", unitSpecialtyMapperDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(UnitSpecialtyMapperDto.class)).list();
			return new Response(SUCCESS, null, null, unitSpecialtyMapperDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response unitDepartmentMasterUniversalSearch(UnitSpecialtyMapperDto unitSpecialtyMapperDto)
			throws ApplicationException {
		try {
			List<UnitSpecialtyMapperDto> unitSpecialtyMapperDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_UNIT_DEPARTMENT_MASTER_DETAILS_LIST_SEARCH")
					.setParameter("orgId", unitSpecialtyMapperDto.getOrganizationId())
					.setParameter("unitId", unitSpecialtyMapperDto.getUnitId())
					.setParameter("specialityName", "%"+unitSpecialtyMapperDto.getSpecialityName().trim().toLowerCase()+"%")
					.setFirstResult(unitSpecialtyMapperDto.getOffset() != null ? unitSpecialtyMapperDto.getOffset() : 0)
					.setMaxResults(unitSpecialtyMapperDto.getNoOfRecordsPerPage() != null ? unitSpecialtyMapperDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(UnitSpecialtyMapperDto.class)).list();
			return new Response(SUCCESS, null, null, unitSpecialtyMapperDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSpecialityListByDoctorIdByOrg(UnitSpecialtyMapperDto unitSpecialtyMapperDto) {
		try {
			List<UnitSpecialtyMapperDto> unitSpecialtyMapperDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SPECIALITY_LIST_BY_DOCTOR_ID_ORG")
					.setParameter("orgId", unitSpecialtyMapperDto.getOrganizationId())
					//.setParameter("unitId", unitSpecialtyMapperDto.getUnitId())
					.setParameter("doctorId", unitSpecialtyMapperDto.getDoctorId())
					.setResultTransformer(Transformers.aliasToBean(UnitSpecialtyMapperDto.class)).list();
			
			List<UnitSpecialtyMapperDto> unitSubSpecialtyMapperDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SUB_SPECIALITY_LIST_BY_DOCTOR_ID_ORG")
					.setParameter("orgId", unitSpecialtyMapperDto.getOrganizationId())
					//.setParameter("unitId", unitSpecialtyMapperDto.getUnitId())
					.setParameter("doctorId", unitSpecialtyMapperDto.getDoctorId())
					.setResultTransformer(Transformers.aliasToBean(UnitSpecialtyMapperDto.class)).list();
			
			
			return new Response(SUCCESS, null, null, unitSpecialtyMapperDtosList, unitSubSpecialtyMapperDtosList);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateUnitSpecialtyMapperDetails(UnitSpecialtyMapperDto unitSpecialtyMapperDto)
			throws ApplicationException {
			try {
			
				Integer result  = (Integer)sessionFactory.getCurrentSession().createSQLQuery(" update "
						+ "	public.t_unit_speciality_mapper set "
						+ "	 is_surgical_code =:iSurgicalCode , "
						+ "	is_clinical_speciality =:isClinicalSpeciality ,"
						+ "	is_rendering_speciality =:isRenderingSpeciality, "
						+ "	dept_wise_appointment =:deptWiseAppointment, "
						+ "	is_kiosk_dept =:isKioskDept, "
						+ "	is_nursing_assessment_required =:isNursingAssessmentRequired, "
						+ "	allowed_overbooking_slots =:allowedOverbookingSlots, "
						+ "	pdd_days =:pddDays "
						+ "	where "
						+ "		unit_speciality_mapper_id = :unitSpecialityMapperId" )
						.setParameter("iSurgicalCode", unitSpecialtyMapperDto.getIsSurgicalCode())
						.setParameter("isClinicalSpeciality", unitSpecialtyMapperDto.getIsClinicalSpeciality())
						.setParameter("isRenderingSpeciality", unitSpecialtyMapperDto.getIsRenderingSpeciality())
						.setParameter("deptWiseAppointment", unitSpecialtyMapperDto.getDeptWiseAppointment())
						.setParameter("isKioskDept", unitSpecialtyMapperDto.getIsKioskDept())
						.setParameter("isNursingAssessmentRequired", unitSpecialtyMapperDto.getIsNursingAssessmentRequired())
						.setParameter("allowedOverbookingSlots", unitSpecialtyMapperDto.getAllowedOverbookingSlots()!=null ? unitSpecialtyMapperDto.getAllowedOverbookingSlots() : 0)
						.setParameter("pddDays", unitSpecialtyMapperDto.getPddDays()!=null ? unitSpecialtyMapperDto.getPddDays() : 0)
					.setParameter("unitSpecialityMapperId", unitSpecialtyMapperDto.getUnitSpecialityMapperId())
					.executeUpdate();

				if(result == 1){
					iSpecialityService.invalidateSpecialityMasterCache(unitSpecialtyMapperDto.getUnitId());
				}
			return new Response<SpecialityMasterDto, Integer>(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			return new Response<SpecialityMasterDto, Integer>(ERROR, null, null, null, null);
		}
	}

	@Override
	public Response getSpecialityListByUserIdByUnitId(UnitSpecialtyMapperDto unitSpecialtyMapperDto)
			throws ApplicationException {
		try {
			List<UnitSpecialtyMapperDto> unitSpecialtyMapperDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SPECIALITY_LIST_BY_USER_ID_UNIT")
					.setParameter("userId", unitSpecialtyMapperDto.getUserId())
					.setResultTransformer(Transformers.aliasToBean(UnitSpecialtyMapperDto.class)).list();
			
			List<UnitSpecialtyMapperDto> unitSubSpecialtyMapperDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SUB_SPECIALITY_LIST_BY_USER_ID_UNIT")
					.setParameter("userId", unitSpecialtyMapperDto.getUserId())
					.setResultTransformer(Transformers.aliasToBean(UnitSpecialtyMapperDto.class)).list();
			
			
			return new Response(SUCCESS, null, null, unitSpecialtyMapperDtosList, unitSubSpecialtyMapperDtosList);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


}

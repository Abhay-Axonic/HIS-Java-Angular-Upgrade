package com.param.global.dao;

import java.util.ArrayList;
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

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.SpecialityMasterDto;
import com.param.global.model.SpecialityMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class SpecialityMasterDaoImpl extends GenericDao<SpecialityMaster, Integer>
		implements ISpecialityMasterDao, ICommonConstants {
	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private SessionFactory sessionFactory;

	private static final Logger logger = Logger.getLogger(SpecialityMasterDaoImpl.class);

	public SpecialityMasterDaoImpl() {
		super(SpecialityMaster.class);
	}

	@Override
	public Response getSpecialityMasterList(SpecialityMasterDto SpecialityMasterDto) throws ApplicationException {
		try {
			List<SpecialityMasterDto> specialityMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SPECIALITY_LIST")
					.setParameter("orgId", SpecialityMasterDto.getOrganizationId())
					.setFirstResult(SpecialityMasterDto.getOffset() != null ? SpecialityMasterDto.getOffset() : 0)
					.setMaxResults(SpecialityMasterDto.getNoOfRecordsPerPage() != null ? SpecialityMasterDto.getNoOfRecordsPerPage() : 1000)
					.setResultTransformer(Transformers.aliasToBean(SpecialityMasterDto.class)).list();
			return new Response(SUCCESS, null, null, specialityMasterDto, null);

		} catch (HibernateException he) {
			logger.error("getSpecialityMasterList failed", he);
		} catch (Exception e) {
			logger.error("getSpecialityMasterList failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response addSpecialityMaster(SpecialityMasterDto specialityMasterDto) throws ApplicationException {
		try {
			SpecialityMaster SpecialityMaster = mapper.map(specialityMasterDto, SpecialityMaster.class,
					"SpecialityMasterDto_to_SpecialityMaster");
			SpecialityMaster.setStatus('A');
			SpecialityMaster.setIsSurgicalCode(SpecialityMaster.getIsSurgicalCode()=='\u0000'?'Y':SpecialityMaster.getIsSurgicalCode());
			SpecialityMaster.setStatus('A');
			SpecialityMaster.setIsRenderingSpeciality(SpecialityMaster.getIsRenderingSpeciality()=='\u0000'?'N':SpecialityMaster.getIsRenderingSpeciality());
			SpecialityMaster.setIsClinicalSpeciality(SpecialityMaster.getIsClinicalSpeciality()=='\u0000'?'N':SpecialityMaster.getIsClinicalSpeciality());
			SpecialityMaster.setDeptWiseAppointment(SpecialityMaster.getDeptWiseAppointment()=='\u0000'?'N':SpecialityMaster.getDeptWiseAppointment());


			SpecialityMaster.setCreatedDate(TimeZoneComponent.getDateByzone(specialityMasterDto.getUnitId() != null ? specialityMasterDto.getUnitId():0));
			SpecialityMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(specialityMasterDto.getUnitId() != null ? specialityMasterDto.getUnitId():0));
			//SpecialityMaster.setSpecialitySAC(specialityMasterDto.getSpecialitySAC());
			if(specialityMasterDto.getDeptWiseAppointment()!='\0' && specialityMasterDto.getDeptWiseAppointment()=='N' )
				SpecialityMaster.setAllowedOverbookingSlots(0);
			//XSpecialityMaster xSpecialityMaster=(XSpecialityMaster) specialityMaster;
			save(SpecialityMaster);
			
			
			/*Session session=sessionFactory.getCurrentSession();
			Transaction tr=session.beginTransaction();
			XSpecialityMaster xSpecialityMaster=new XSpecialityMaster();
				xSpecialityMaster.setSpecialityCode(specialityMasterDto.getSpecialityCode());
				xSpecialityMaster.setSpecialityName(specialityMasterDto.getSpecialityName());
				xSpecialityMaster.setIsSurgicalCode(specialityMasterDto.getIsSurgicalCode());
				xSpecialityMaster.setGeneralLedgerId(specialityMasterDto.getGeneralLedgerId());
				xSpecialityMaster.setCreatedBy(specialityMasterDto.getCreatedBy());
				xSpecialityMaster.setCreatedDate(ADTCommonDateUtils.getDate(specialityMasterDto.getCreatedDate(),"dd-M-yyyy HH:mm:ss"));
				xSpecialityMaster.setUpdatedBy(specialityMasterDto.getUpdatedBy());
				xSpecialityMaster.setUpdatedDate(ADTCommonDateUtils.getDate(specialityMasterDto.getCreatedDate(),"dd-M-yyyy HH:mm:ss"));
				xSpecialityMaster.setStatus(specialityMasterDto.getStatus());
				xSpecialityMaster.setOrganizationId(specialityMasterDto.getOrganizationId());
			session.save(xSpecialityMaster);
			tr.commit();
	*/		
			
			return new Response(SUCCESS, null, null, null, SpecialityMaster);
		} catch (HibernateException he) {
			logger.error("addSpecialityMaster failed", he);
		} catch (Exception e) {
			logger.error("addSpecialityMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response editSpecialityMaster(SpecialityMasterDto SpecialityMasterDto) throws ApplicationException {
		try {
			List<SpecialityMasterDto> listSpecialityMasterDto = new ArrayList<SpecialityMasterDto>();
			listSpecialityMasterDto = sessionFactory.getCurrentSession().getNamedQuery("GET_SPECIALITY_LIST_BY_ID")
					.setParameter("specialityId", SpecialityMasterDto.getSpecialityId())
					.setResultTransformer(Transformers.aliasToBean(SpecialityMasterDto.class)).list();
			return new Response<SpecialityMasterDto, Integer>(SUCCESS, null, null, listSpecialityMasterDto, null);

		} catch (HibernateException he) {
			logger.error("editSpecialityMaster failed", he);
			return new Response<SpecialityMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			logger.error("editSpecialityMaster failed", e);
			return new Response<SpecialityMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}

	@Override
	public Response updateSpecialityMaster(SpecialityMasterDto SpecialityMasterDto) throws ApplicationException {
		try {
			
			SpecialityMaster Speciality = findById(SpecialityMasterDto.getSpecialityId());
			Speciality.setSpecialityName(SpecialityMasterDto.getSpecialityName());
			Speciality.setSpecialityCode(SpecialityMasterDto.getSpecialityCode());
			Speciality.setStoreId(SpecialityMasterDto.getStoreId());
			Speciality.setGeneralLedgerId(SpecialityMasterDto.getGeneralLedgerId());
			Speciality.setIsSurgicalCode(SpecialityMasterDto.getIsSurgicalCode());
			Speciality.setIsClinicalSpeciality(SpecialityMasterDto.getIsClinicalSpeciality());
			Speciality.setIsRenderingSpeciality(SpecialityMasterDto.getIsRenderingSpeciality());
			Speciality.setDeptWiseAppointment(SpecialityMasterDto.getDeptWiseAppointment());
			Speciality.setIsKioskDept(SpecialityMasterDto.getIsKioskDept());
			Speciality.setIsNursingAssessmentRequired(SpecialityMasterDto.getIsNursingAssessmentRequired());
			Speciality.setSpecialitySAC(SpecialityMasterDto.getSpecialitySAC());
			Speciality.setUpdatedDate(GlobalCommonDateUtils.getDate(SpecialityMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
			Speciality.setSpecialityName(SpecialityMasterDto.getSpecialityName());
			if(Speciality.getDeptWiseAppointment()!='\0' && Speciality.getDeptWiseAppointment()=='N' )
				Speciality.setAllowedOverbookingSlots(0);
			else
				Speciality.setAllowedOverbookingSlots(SpecialityMasterDto.getAllowedOverbookingSlots());
			
			update(Speciality);
			return new Response<SpecialityMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, Speciality);

		} catch (HibernateException he) {
			logger.error("updateSpecialityMaster failed", he);
			return new Response<SpecialityMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			logger.error("updateSpecialityMaster failed", e);
			return new Response<SpecialityMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}
	}

	@Override
	public Response getSpecialityByNameNotId(SpecialityMasterDto specialityMasterDto) throws ApplicationException {
		try {
			List<SpecialityMasterDto> listDepartmentMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SPECIALITY_LIST_BY_NAME_NOT_ID")
					.setParameter("specialityName", specialityMasterDto.getSpecialityName().toLowerCase())
					.setParameter("specialityId", specialityMasterDto.getSpecialityId())
					.setParameter("orgId", specialityMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(SpecialityMasterDto.class)).list();
			if (listDepartmentMasterDto != null) {
				return new Response(SUCCESS, null, null, listDepartmentMasterDto, null);
			}
		} catch (HibernateException he) {
			logger.error("getSpecialityByNameNotId failed", he);
		} catch (Exception e) {
			logger.error("getSpecialityByNameNotId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSpecialityById(SpecialityMasterDto specialityMasterDto) throws ApplicationException {
		try {
			List<SpecialityMasterDto> listSpecialityMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SPECIALITY_LIST_BY_ID")
					.setParameter("specialityId", specialityMasterDto.getSpecialityId())
					.setResultTransformer(Transformers.aliasToBean(SpecialityMasterDto.class)).list();
			if (listSpecialityMasterDto != null) {
				return new Response(SUCCESS, null, null, listSpecialityMasterDto, null);
			}
		} catch (HibernateException he) {
			logger.error("getSpecialityById failed", he);
		} catch (Exception e) {
			logger.error("getSpecialityById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveSpecialityList(SpecialityMasterDto specialityMasterDto) throws ApplicationException {
		try {
			List<SpecialityMasterDto> specialityMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_SPECIALITY_LIST")
					.setParameter("organizationId", specialityMasterDto.getOrganizationId())
					.setParameter("unitId", specialityMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(SpecialityMasterDto.class)).list();
			return new Response(SUCCESS, null, null, specialityMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error("getActiveSpecialityList failed", he);
		} catch (Exception e) {
			logger.error("getActiveSpecialityList failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSpecialityByName(SpecialityMasterDto specialityMasterDto) throws ApplicationException {
		try {
			List<SpecialityMasterDto> specialityMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SPECIALITY_LIST_BY_NAME")
					.setParameter("specialityName", specialityMasterDto.getSpecialityName().toLowerCase())
					.setParameter("orgId", specialityMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(SpecialityMasterDto.class)).list();
			return new Response(SUCCESS, null, null, specialityMasterList, null);

		} catch (HibernateException he) {
			logger.error("getSpecialityByName failed", he);
		} catch (Exception e) {
			logger.error("getSpecialityByName failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateSpecialityStatus(SpecialityMasterDto SpecialityMasterDto) {
		try {
			SpecialityMaster Speciality = findById(SpecialityMasterDto.getSpecialityId());

			Speciality.setStatus(SpecialityMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			Speciality.setUpdatedBy(SpecialityMasterDto.getUpdatedBy());
			Speciality.setUpdatedDate(GlobalCommonDateUtils.getDate(SpecialityMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
			update(Speciality);
			return new Response<SpecialityMasterDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (HibernateException he) {
			logger.error("updateSpecialityStatus failed", he);
			return new Response<SpecialityMasterDto, Integer>(ERROR, null, null, null, null);
		} catch (Exception e) {
			logger.error("updateSpecialityStatus failed", e);
			return new Response<SpecialityMasterDto, Integer>(ERROR, null, null, null, null);
		} finally {
		}

	}

	@Override
	public Response getActiveSpecialityMasterList() throws ApplicationException {
		try {
			List<SpecialityMasterDto> specialityMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_SPECIALITY_LIST")
					.setResultTransformer(Transformers.aliasToBean(SpecialityMasterDto.class)).list();
			return new Response(SUCCESS, null, null, specialityMasterList, null);

		} catch (HibernateException he) {
			logger.error("getActiveSpecialityMasterList failed", he);
		} catch (Exception e) {
			logger.error("getActiveSpecialityMasterList failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSpecialityListForSubSpeciality(int orgId) throws ApplicationException {
		try {
			List<SpecialityMasterDto> specialityMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SPECIALITY_LIST_FOR_SUB_SPE")
					.setParameter("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(SpecialityMasterDto.class)).list();
			return new Response(SUCCESS, null, null, specialityMasterDto, null);

		} catch (HibernateException he) {
			logger.error("getSpecialityListForSubSpeciality failed", he);
		} catch (Exception e) {
			logger.error("getSpecialityListForSubSpeciality failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getCount(SpecialityMasterDto SpecialityMasterDto)
	{
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(SpecialityMaster.class); 
			c.add(Restrictions.eq("organizationId", SpecialityMasterDto.getOrganizationId()));
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
	public Response getOrgActiveSpecialityList(SpecialityMasterDto specialityMasterDto) throws ApplicationException {
		try {
			List<SpecialityMasterDto> specialityMasterDtolist = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ORG_ACTIVE_SPECIALITY_LIST_NOT_MAPPED_TO_UNIT")
					.setParameter("unitId", specialityMasterDto.getUnitId())
					.setParameter("orgId", specialityMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(SpecialityMasterDto.class)).list();
			return new Response(SUCCESS, null, null, specialityMasterDtolist, null);


		} catch (HibernateException he) {
			logger.error("getOrgActiveSpecialityList failed", he);
		} catch (Exception e) {
			logger.error("getOrgActiveSpecialityList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSpecialityNameById(SpecialityMasterDto specialityMasterDto) throws ApplicationException {
		try {
			SpecialityMasterDto listSpecialityMasterDto = (SpecialityMasterDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SPECIALITY_NAME_BY_ID")
					.setParameter("specialityId", specialityMasterDto.getSpecialityId())
					.setResultTransformer(Transformers.aliasToBean(SpecialityMasterDto.class)).uniqueResult();
			if (listSpecialityMasterDto != null) {
				return new Response(SUCCESS, null, null, null, listSpecialityMasterDto);
			}
		} catch (HibernateException he) {
			logger.error("getSpecialityNameById failed", he);
		} catch (Exception e) {
			logger.error("getSpecialityNameById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getStoreBySpecialtyId(SpecialityMasterDto specialityMasterDto) throws ApplicationException {
		try {
			List<SpecialityMasterDto> specialityMasterDtolist = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_STORE_BY_SPECIALTY_ID")
					.setParameter("orgId", specialityMasterDto.getOrganizationId())
					.setParameter("specialityId", specialityMasterDto.getSpecialityId())
					.setParameter("unitId", specialityMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(SpecialityMasterDto.class)).list();
			return new Response(SUCCESS, null, null, specialityMasterDtolist, null);
			

		} catch (HibernateException he) {
		logger.error("getStoreBySpecialtyId failed", he);
		} catch (Exception e) {
		logger.error("getStoreBySpecialtyId failed", e);
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSpecialityListOfActiveInactive(SpecialityMasterDto specialityMasterDto)
			throws ApplicationException {
		try {
			List<SpecialityMasterDto> specialityMaster = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SPECIALITY_LIST_OF_ACTIVE_INACTIVE")
					.setParameter("orgId", specialityMasterDto.getOrganizationId())
					.setFirstResult(specialityMasterDto.getOffset() != null ? specialityMasterDto.getOffset() : 0)
					.setMaxResults(specialityMasterDto.getNoOfRecordsPerPage() != null ? specialityMasterDto.getNoOfRecordsPerPage() : 1000)
					.setResultTransformer(Transformers.aliasToBean(SpecialityMasterDto.class)).list();
			return new Response(SUCCESS, null, null, specialityMaster, null);

		} catch (HibernateException he) {
		logger.error("getSpecialityListOfActiveInactive failed", he);
		} catch (Exception e) {
		logger.error("getSpecialityListOfActiveInactive failed", e);
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response departmentMasterUniversalSearch(SpecialityMasterDto specialityMasterDto)
			throws ApplicationException {
		try {
			List<SpecialityMasterDto> listSpecialityMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_DEPARTMENT_MASTER_DETAILS_LIST_SEARCH")
					.setParameter("orgId", specialityMasterDto.getOrganizationId())
					.setParameter("specialityName", "%"+specialityMasterDto.getSpecialityName().trim().toLowerCase()+"%")
					.setFirstResult(specialityMasterDto.getOffset() != null ? specialityMasterDto.getOffset() : 0)
					.setMaxResults(specialityMasterDto.getNoOfRecordsPerPage() != null ? specialityMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(SpecialityMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listSpecialityMasterDto, null);
		}catch (HibernateException he) {
		logger.error("departmentMasterUniversalSearch failed", he);
		} catch (Exception e) {
		logger.error("departmentMasterUniversalSearch failed", e);
		} 
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSpecialityCheckWithUnitMapper(SpecialityMasterDto specialityMasterDto)
			throws ApplicationException {
		try {
			List<SpecialityMasterDto> listSpecialityMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CHECK_SPECIALITY_IN_UNIT_MAPPER")
					.setParameter("specialityId", specialityMasterDto.getSpecialityId())
					.setParameter("organizationId", specialityMasterDto.getOrganizationId())
					
					.setResultTransformer(Transformers.aliasToBean(SpecialityMasterDto.class)).list();
			if (listSpecialityMasterDto != null) {
				return new Response(SUCCESS, null, null, listSpecialityMasterDto, null);
			}
		} catch (HibernateException he) {
		logger.error("getSpecialityCheckWithUnitMapper failed", he);
		} catch (Exception e) {
		logger.error("getSpecialityCheckWithUnitMapper failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveSpecialityListForModality(SpecialityMasterDto specialityMasterDto)
			throws ApplicationException {
		try {
			List<SpecialityMasterDto> listSpecialityMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_SPECIALITY_FOR_MODALITY_SCHEDULING")
					.setParameter("unitId", specialityMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(SpecialityMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listSpecialityMasterDto, null);
		}catch (HibernateException he) {
		logger.error("getActiveSpecialityListForModality failed", he);
		} catch (Exception e) {
		logger.error("getActiveSpecialityListForModality failed", e);
		} 
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	

	
}
